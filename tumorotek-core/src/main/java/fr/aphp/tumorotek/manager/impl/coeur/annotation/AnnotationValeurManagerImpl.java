/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.impl.coeur.annotation;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampCalculeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.AnnotationCommonValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine AnnotationValeur.
 * Classe créée le 09/02/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.12
 *
 */
public class AnnotationValeurManagerImpl implements AnnotationValeurManager
{

   private final Log log = LogFactory.getLog(AnnotationValeurManager.class);

   /* Beans injectes par Spring*/
   private AnnotationValeurDao annotationValeurDao;
   private AnnotationCommonValidator annotationCommonValidator;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;
   private ChampAnnotationDao champAnnotationDao;
   private BanqueDao banqueDao;
   private FichierManager fichierManager;
   private EntiteManager entiteManager;
   private TableAnnotationDao tableAnnotationDao;
   private ChampCalculeManager champCalculeManager;

   public AnnotationValeurManagerImpl(){}

   public void setAnnotationValeurDao(final AnnotationValeurDao avDao){
      this.annotationValeurDao = avDao;
   }

   public void setAnnotationCommonValidator(final AnnotationCommonValidator acValidator){
      this.annotationCommonValidator = acValidator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setChampAnnotationDao(final ChampAnnotationDao caDao){
      this.champAnnotationDao = caDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setFichierManager(final FichierManager fManager){
      this.fichierManager = fManager;
   }

   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   public void setTableAnnotationDao(final TableAnnotationDao tDao){
      this.tableAnnotationDao = tDao;
   }

   public void setChampCalculeManager(final ChampCalculeManager champCalculeManager){
      this.champCalculeManager = champCalculeManager;
   }

   @Override
   public void createObject(AnnotationValeur annoVal){
      annotationValeurDao.createObject(annoVal);
   }

   @Override
   public void updateObject(AnnotationValeur annoVal){
      annotationValeurDao.updateObject(annoVal);
   }

   @Override
   public void createOrUpdateObjectManager(final AnnotationValeur valeur, final ChampAnnotation champ,
      final TKAnnotableObject obj, final Banque banque, final Fichier fichier, final Utilisateur utilisateur,
      final String operation, final String baseDir, final List<File> filesCreated, final List<File> filesToDelete){

      if(operation == null){
         throw new NullPointerException("operation cannot be set to null for createorUpdateMethod");
      }

      if(fichier != null && valeur.getAnnotationValeurId() == null){
         valeur.setFichier(null);
      }

      checkRequiredObjectsAndValidate(valeur, champ, obj, banque, fichier, operation);

      //Doublon
      if(!findDoublonManager(valeur)){
         if((operation.equals("creation") || operation.equals("modification"))){

            try{
               if(operation.equals("creation")){

                  annotationValeurDao.createObject(valeur);
                  log.info("Enregistrement objet AnnotationValeur " + valeur.toString());

                  CreateOrUpdateUtilities.createAssociateOperation(valeur, operationManager,
                     operationTypeDao.findByNom("Creation").get(0), utilisateur);

               }else{

                  annotationValeurDao.updateObject(valeur);
                  log.info("Modification objet AnnotationValeur " + valeur.toString());
                  CreateOrUpdateUtilities.createAssociateOperation(valeur, operationManager,
                     operationTypeDao.findByNom("Modification").get(0), utilisateur);

               }
               if(fichier != null && baseDir != null){
                  fichierManager.createOrUpdateFileForObject(valeur, fichier, valeur.getStream(),
                     Utils.writeAnnoFilePath(baseDir, valeur.getBanque(), valeur.getChampAnnotation(), fichier), filesCreated,
                     filesToDelete);
                  annotationValeurDao.updateObject(valeur);
               }

            }catch(final RuntimeException re){
               if(operation.equals("creation")){
                  valeur.setAnnotationValeurId(null);
               }
               // rollback creation fichier
               if(filesCreated != null){
                  for(final File f : filesCreated){
                     f.delete();
                  }
               }else{
                  log.warn("Rollback création fichier n'a pas pu être réalisée");
               }

               throw re;
            }
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors " + operation + " objet AnnotationValeur " + valeur.toString());
         throw new DoublonFoundException("AnnotationValeur", operation);
      }
   }

   @Override
   public List<AnnotationValeur> createAnnotationValeurListManager(final List<AnnotationValeur> valeurs,
      final TKAnnotableObject obj, final Utilisateur utilisateur, final String operation, final String baseDir,
      final List<File> filesCreated, final List<File> filesToDelete){

      String annotOperation = operation;
      final List<AnnotationValeur> valeursCreatedUpdated = new ArrayList<>();
      if(valeurs != null){
         AnnotationValeur clone;
         for(AnnotationValeur valeur : valeurs){
            clone = valeur.clone();
            if(operation == null){
               if(clone.getAnnotationValeurId() != null){
                  annotOperation = "modification";
               }else{
                  annotOperation = "creation";
               }
            }
            // clone permet de conserver l'etat transient en cas d'erreur
            // et rollback transaction... 
            // attention, de fait l'objet 
            // dans l'interface conserve id = null
            createOrUpdateObjectManager(clone, null, obj, null, clone.getFichier(), utilisateur, annotOperation, baseDir,
               filesCreated, filesToDelete);
            valeursCreatedUpdated.add(clone);
         }
         // operation associée
         if(!valeurs.isEmpty() && obj != null){
            CreateOrUpdateUtilities.createAssociateOperation(obj, operationManager,
               operationTypeDao.findByNom("Annotation").get(0), utilisateur);
         }
      }

      return valeursCreatedUpdated;
   }

   @Override
   public void prepareAnnotationValeurListJDBCManager(final EchantillonJdbcSuite jdbcSuite, final List<AnnotationValeur> valeurs,
      final TKAnnotableObject obj, final Utilisateur utilisateur) throws SQLException{

      if(valeurs != null){
         final Integer maxAnnoId = jdbcSuite.getMaxAnnotationValeurId();
         try{

            for(final AnnotationValeur av : valeurs){

               checkRequiredObjectsAndValidate(av, av.getChampAnnotation(), obj, obj.getBanque(), null, "creation");

               //Doublon
               if(!findDoublonManager(av)){
                  jdbcSuite.incrementMaxAnnotationValeurId();
                  jdbcSuite.getPstmtAnno().clearParameters();
                  jdbcSuite.getPstmtAnno().setInt(1, jdbcSuite.getMaxAnnotationValeurId());
                  jdbcSuite.getPstmtAnno().setInt(2, av.getChampAnnotation().getId());
                  jdbcSuite.getPstmtAnno().setInt(3, obj.listableObjectId());
                  jdbcSuite.getPstmtAnno().setInt(4, obj.getBanque().getBanqueId());
                  jdbcSuite.getPstmtAnno().setNull(5, Types.VARCHAR);
                  jdbcSuite.getPstmtAnno().setNull(6, Types.BOOLEAN);
                  jdbcSuite.getPstmtAnno().setNull(7, Types.DATE);
                  jdbcSuite.getPstmtAnno().setNull(8, Types.VARCHAR);
                  jdbcSuite.getPstmtAnno().setNull(9, Types.INTEGER);
                  if(av.getAlphanum() != null){
                     jdbcSuite.getPstmtAnno().setString(5, av.getAlphanum());
                  }else if(av.getBool() != null){
                     jdbcSuite.getPstmtAnno().setBoolean(6, av.getBool());
                  }else if(av.getDate() != null){
                     jdbcSuite.getPstmtAnno().setTimestamp(7, new java.sql.Timestamp(av.getDate().getTimeInMillis()));
                  }else if(av.getTexte() != null){
                     jdbcSuite.getPstmtAnno().setString(8, av.getTexte());
                  }else if(av.getItem() != null){
                     jdbcSuite.getPstmtAnno().setInt(9, av.getItem().getItemId());
                  }
                  jdbcSuite.getPstmtAnno().addBatch();

                  // operation
                  jdbcSuite.getPstmtOp().clearParameters();
                  jdbcSuite.getPstmtOp().setInt(1, utilisateur.getUtilisateurId());
                  jdbcSuite.getPstmtOp().setInt(2, jdbcSuite.getMaxAnnotationValeurId());
                  jdbcSuite.getPstmtOp().setInt(3, 31);
                  jdbcSuite.getPstmtOp().setInt(4, 3);
                  jdbcSuite.getPstmtOp().setTimestamp(5,
                     new java.sql.Timestamp(Utils.getCurrentSystemCalendar().getTimeInMillis()));
                  jdbcSuite.getPstmtOp().setBoolean(6, false);
                  jdbcSuite.getPstmtOp().addBatch();
               }else{
                  throwDoublonException();
               }
            }

         }catch(final Exception e){
            e.printStackTrace();
            jdbcSuite.setMaxAnnotationValeurId(maxAnnoId);
            throw e;
         }finally{}
      }

   }

   private void throwDoublonException(){
      throw new DoublonFoundException("AnnotationValeur", "creation");
   }

   @Override
   public List<AnnotationValeur> findAllObjectsManager(){
      log.debug("Recherche totalite des AnnotationValeur");
      return annotationValeurDao.findAll();
   }

   @Override
   public List<AnnotationValeur> findByChampAndObjetManager(final ChampAnnotation champ, final TKAnnotableObject obj){
      log.debug("Recherche des AnnotationValeur par champ et Objet");
      List<AnnotationValeur> valeurs = new ArrayList<>();
      if(obj != null && champ != null && obj.getClass().getSimpleName().equals(champ.getTableAnnotation().getEntite().getNom())){
         if("calcule".equals(champ.getDataType().getType())){
            AnnotationValeur av = new AnnotationValeur();
            Object valeur = champCalculeManager.getValueForObjectManager(champ.getChampCalcule(), obj);
            if(null != valeur){
               av.setValeur(valeur);
            }
            valeurs.add(av);
         }else{
            valeurs = annotationValeurDao.findByChampAndObjetId(champ, obj.listableObjectId());
         }
      }
      return valeurs;
   }

   @Override
   public List<AnnotationValeur> findByChampAndObjetManager(final ChampAnnotation champ, final TKAnnotableObject obj,
      Boolean discardCalcule){
      log.debug("Recherche des AnnotationValeur par champ et Objet");
      List<AnnotationValeur> valeurs = new ArrayList<>();
      if(obj != null && champ != null && obj.getClass().getSimpleName().equals(champ.getTableAnnotation().getEntite().getNom())){
         if(!discardCalcule){
            findByChampAndObjetManager(champ, obj);
         }else{
            valeurs = annotationValeurDao.findByChampAndObjetId(champ, obj.listableObjectId());
         }
      }
      return valeurs;
   }

   @Override
   public List<AnnotationValeur> findByTableAndBanqueManager(final TableAnnotation table, final Banque bank){
      log.debug("Recherche des AnnotationValeur par Table " + "et pour une banque");
      return annotationValeurDao.findByTableAndBanque(table, bank);
   }

   @Override
   public List<AnnotationValeur> findByObjectManager(final TKAnnotableObject obj){
      List<AnnotationValeur> res = new ArrayList<>();
      if(obj != null){
         log.debug("Recherche des AnnotationValeur pour l'objet " + obj.toString());
         res = annotationValeurDao.findByObjectIdAndEntite(obj.listableObjectId(),
            entiteManager.findByNomManager(obj.entiteNom()).get(0));
      }
      return res;
   }

   @Override
   public boolean findDoublonManager(final AnnotationValeur valeur){
      if(valeur.getAnnotationValeurId() == null){
         return annotationValeurDao.findByChampAndObjetId(valeur.getChampAnnotation(), valeur.getObjetId()).contains(valeur);
      }
      return annotationValeurDao
         .findByExcludedId(valeur.getChampAnnotation(), valeur.getObjetId(), valeur.getAnnotationValeurId()).contains(valeur);
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes 
    * sont non nulls et lance la validation via le Validator.
    * @param valeur
    * @param champ champAnnotation
    * @param obj
    * @param banque
    * @param fichier
    * @param operation demandant la verification
    */
   private void checkRequiredObjectsAndValidate(final AnnotationValeur valeur, final ChampAnnotation champ,
      final TKAnnotableObject obj, final Banque banque, final Fichier fichier, final String operation){
      // ChampAnnotation required
      if(champ != null){
         // merge dataType object
         valeur.setChampAnnotation(champAnnotationDao.mergeObject(champ));
      }else if(valeur.getChampAnnotation() == null){
         log.warn("Objet obligatoire ChampAnnotation manquant lors de la " + operation + " de valeur annotation");
         throw new RequiredObjectIsNullException("AnnotationValeur", operation, "ChampAnnotation");
      }

      // objrequired
      if(obj != null){
         valeur.setObjetId(obj.listableObjectId());
      }else if(valeur.getObjetId() == null){
         log.warn("Objet obligatoire objetId manquant lors de la " + operation + " de valeur annotation");
         throw new RequiredObjectIsNullException("AnnotationValeur", operation, "TKAnnotableObject");
      }

      // banque required
      if(banque != null){
         valeur.setBanque(banqueDao.mergeObject(banque));
      }else if(valeur.getBanque() == null){
         log.warn("Objet obligatoire Banque manquant lors de la " + operation + " de valeur annotation");
         throw new RequiredObjectIsNullException("AnnotationValeur", operation, "Banque");
      }

      // coherence entite
      if(obj != null
         && !obj.getClass().getSimpleName().equals(valeur.getChampAnnotation().getTableAnnotation().getEntite().getNom())){
         log.warn("Incoherence obj et table annotation sur entite");
         throw new RuntimeException("Incoherence entre entite objet et table annotation");
      }

      //		if (fichier != null) {
      //			valeur.setFichier(fichier);
      //		}

      //Validation
      BeanValidator.validateObject(valeur, new Validator[] {annotationCommonValidator});
   }

   @Override
   public void removeObjectManager(final AnnotationValeur valeur, final List<File> filesToDelete){
      if(valeur != null){
         annotationValeurDao.removeObject(valeur.getAnnotationValeurId());
         log.info("Suppression objet AnnotationValeur " + valeur.toString());
         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(valeur, operationManager);

         // suppression du fichier associé base et systeme
         if(valeur.getFichier() != null){
            fichierManager.removeObjectManager(valeur.getFichier(), filesToDelete);
         }

      }else{
         log.warn("Suppression d'un AnnotationValeur null");
      }
   }

   @Override
   public void removeAnnotationValeurListManager(final List<AnnotationValeur> valeurs, final List<File> filesToDelete){
      if(valeurs != null){
         for(AnnotationValeur valeur : valeurs){
            removeObjectManager(valeur, filesToDelete);
         }
      }
   }

   @Override
   public Object getValueForAnnotationValeur(final AnnotationValeur valeur){
      Object obj = null;
      if(valeur != null && valeur.getChampAnnotation() != null){
         String dataType = valeur.getChampAnnotation().getDataType().getType();
         switch(dataType){
            default:
               break;
            case "alphanum":
               obj = valeur.getAlphanum();
               break;
            case "boolean":
               obj = valeur.getBool();
               ;
               break;
            case "date":
               obj = valeur.getDate();
               break;
            case "datetime":
               obj = valeur.getDate();
               break;
            case "num":
               obj = valeur.getAlphanum();
               break;
            case "texte":
               obj = valeur.getTexte();
               break;
            case "thesaurus":
               obj = valeur.getItem();
               break;
            case "thesaurusM":
               obj = valeur.getItem();
               break;
            case "fichier":
               obj = valeur.getFichier();
               break;
            case "hyperlien":
               obj = valeur.getAlphanum();
               break;
         }
      }

      return obj;
   }

   @Override
   public AnnotationValeur findByIdManager(final Integer annotationValeurId){
      return annotationValeurDao.findById(annotationValeurId);
   }

   @Override
   public void switchBanqueManager(final TKAnnotableObject obj, final Banque bank, final List<File> filesToDelete, 
		   final Set<MvFichier> filesToMove){
      if(bank != null && obj != null){

         final Collection<?> sharedByBanques = CollectionUtils.intersection(
            tableAnnotationDao.findByEntiteAndBanque(entiteManager.findByNomManager(obj.entiteNom()).get(0), obj.getBanque()),
            tableAnnotationDao.findByEntiteAndBanque(entiteManager.findByNomManager(obj.entiteNom()).get(0), bank));

         final List<AnnotationValeur> annos = findByObjectManager(obj);
         for(int i = 0; i < annos.size(); i++){
            if(sharedByBanques.contains(annos.get(i).getChampAnnotation().getTableAnnotation())){
               annos.get(i).setBanque(bank);
               annotationValeurDao.updateObject(annos.get(i));
               
               // fichier?
               if (filesToMove != null && annos.get(i)
            		   .getChampAnnotation().getDataType().getType().equalsIgnoreCase("fichier")) {
            	   fichierManager.switchBanqueManager(annos.get(i).getFichier(), bank, filesToMove);
               }
               
            }else{
               removeObjectManager(annos.get(i), filesToDelete);
            }
         }
      }
   }

   @Override
   public void createFileBatchForTKObjectsManager(final List<? extends TKAnnotableObject> objs, final Fichier file,
      final InputStream stream, final ChampAnnotation champ, final Banque banque, final Utilisateur u, final String baseDir,
      final List<File> filesCreated){

      if(objs != null && file != null && stream != null){

         Fichier annoFile = null;
         String mimeType = null;
         String path = null;

         AnnotationValeur valeur;
         for(int i = 0; i < objs.size(); i++){

            valeur = new AnnotationValeur();
            valeur.setStream(stream);
            annoFile = file.cloneNoId();
            // recuperation dataType & path premier enregistrement
            if(i > 0){
               annoFile.setPath(path);
               annoFile.setMimeType(mimeType);
               valeur.setStream(null);
            }

            createOrUpdateObjectManager(valeur, champ, objs.get(i), banque, annoFile, u, "creation", baseDir, filesCreated, null);

            // recuperation dataType & path premier enregistrement
            if(i == 0){
               mimeType = valeur.getFichier().getMimeType();
               path = valeur.getFichier().getPath();
            }
         }

      }
   }

   @Override
   public Long findCountByTableAnnotationBanqueManager(final TableAnnotation tab, final Banque b){

      if(tab != null && b != null){
         return annotationValeurDao.findCountByTableAnnotationBanque(tab, b).get(0);
      }

      return null;
   }
}
