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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.AnnotationDefautDao;
import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampCalculeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.AnnotationCommonValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.ChampAnnotationValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.ItemValidator;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine ChampAnnotation.
 * Classe créée le 02/02/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class ChampAnnotationManagerImpl implements ChampAnnotationManager
{

   private final Logger log = LoggerFactory.getLogger(ChampAnnotationManagerImpl.class);

   /** Bean Dao AffichageDao. */
   private ChampAnnotationDao champAnnotationDao;

   private ChampAnnotationValidator champAnnotationValidator;

   private DataTypeDao dataTypeDao;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   private ItemDao itemDao;

   private AnnotationDefautDao annotationDefautDao;

   private AnnotationValeurDao annotationValeurDao;

   private ItemValidator itemValidator;

   private AnnotationCommonValidator annotationCommonValidator;

   private TableAnnotationDao tableAnnotationDao;

   private BanqueDao banqueDao;

   private ChampCalculeManager champCalculeManager;

   /** Bean Dao EntityManagerFactory. */
   private EntityManagerFactory entityManagerFactory;

   public ChampAnnotationManagerImpl(){
      super();
   }

   /**
    * Setter du bean ChampAnnotationDao.
    * @param caDao est le bean Dao.
    */
   public void setChampAnnotationDao(final ChampAnnotationDao caDao){
      this.champAnnotationDao = caDao;
   }

   public void setChampAnnotationValidator(final ChampAnnotationValidator cValidator){
      this.champAnnotationValidator = cValidator;
   }

   public void setDataTypeDao(final DataTypeDao dtDao){
      this.dataTypeDao = dtDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setItemDao(final ItemDao itDao){
      this.itemDao = itDao;
   }

   public void setAnnotationDefautDao(final AnnotationDefautDao annoDDao){
      this.annotationDefautDao = annoDDao;
   }

   public void setItemValidator(final ItemValidator iValidator){
      this.itemValidator = iValidator;
   }

   public void setAnnotationCommonValidator(final AnnotationCommonValidator acValidator){
      this.annotationCommonValidator = acValidator;
   }

   public void setTableAnnotationDao(final TableAnnotationDao tDao){
      this.tableAnnotationDao = tDao;
   }

   /**
    * Setter du bean EntityManagerFactory.
    * @param eFactory est l'entityManagerFactory.
    */
   public void setEntityManagerFactory(final EntityManagerFactory eFactory){
      this.entityManagerFactory = eFactory;
   }

   public void setAnnotationValeurDao(final AnnotationValeurDao avDao){
      this.annotationValeurDao = avDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setChampCalculeManager(final ChampCalculeManager ccMgr){
      this.champCalculeManager = ccMgr;
   }

   @Override
   public List<ChampAnnotation> findByNomManager(final String nom){
      return champAnnotationDao.findByNom(nom);
   }

   @Override
   public ChampAnnotation findByIdManager(final Integer champAnnotationId){
      return champAnnotationDao.findById(champAnnotationId);
   }

   @Override
   public List<AnnotationValeur> findAnnotationValeurByChampAnnotationManager(final ChampAnnotation ca){
      List<AnnotationValeur> objets = null;
      final StringBuffer sb = new StringBuffer("");
      sb.append("SELECT va FROM AnnotationValeur as av " + "join av.champAnnotation as ca where ca.id = " + ca.getId());
      /* On exécute la requête. */
      log.debug("findAnnotationValeurByChampAnnotationManager : Exécution de la requête : \n{}", sb);
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<AnnotationValeur> query = em.createQuery(sb.toString(), AnnotationValeur.class);
      objets = query.getResultList();
      return objets;
   }

   @Override
   public List<ChampAnnotation> findByEntiteManager(final Entite e){
      List<ChampAnnotation> objets = null;
      final StringBuffer sb = new StringBuffer("");
      sb.append("SELECT DISTINCT ca FROM ChampAnnotation as ca " + "join ca.tableAnnotation.entite as en "
         + "WHERE en.entiteId = " + e.getEntiteId().intValue());
      /* On exécute la requête. */
      log.debug("findChampAnnotationByEntiteManager : Exécution de la requête : \n{}",sb);
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<ChampAnnotation> query = em.createQuery(sb.toString(), ChampAnnotation.class);
      objets = query.getResultList();
      return objets;
   }

   @Override
   public String findAnnotationValeurManager(final Object objet, final ChampAnnotation champAnnotation){
      Object retour = null;
      /* On récupère l'identifiant de l'objet. */
      Integer id = null;
      if(objet.getClass().getSimpleName().equals("Patient")){
         id = ((Patient) objet).getPatientId();
      }else if(objet.getClass().getSimpleName().equals("Maladie")){
         id = ((Maladie) objet).getMaladieId();
      }else if(objet.getClass().getSimpleName().equals("Prelevement")){
         id = ((Prelevement) objet).getPrelevementId();
      }else if(objet.getClass().getSimpleName().equals("Echantillon")){
         id = ((Echantillon) objet).getEchantillonId();
      }else if(objet.getClass().getSimpleName().equals("ProdDerive")){
         id = ((ProdDerive) objet).getProdDeriveId();
      }

      /* On récupère le type de l'annotation. */
      final String dataType = champAnnotation.getDataType().getType();
      String type = null;
      if(dataType.equals("num") || dataType.equals("alphanum")){
         type = "alphanum";
      }else if(dataType.equals("boolean")){
         type = "bool";
      }else if(dataType.equals("date")){
         type = "date";
      }else if(dataType.equals("datetime")){
         type = "date";
      }else if(dataType.equals("texte")){
         type = "texte";
      }else if(dataType.equals("thesaurus")){
         type = "item.valeur";
      }

      if(type != null && id != null){
         final StringBuffer sb = new StringBuffer("");
         sb.append("SELECT av." + type + " FROM AnnotationValeur as av " + "join av.champAnnotation as ca "
            + "join ca.tableAnnotation.entite as en " + "WHERE en.nom like '" + objet.getClass().getSimpleName()
            + "' and av.objetId = " + id + " and ca.id = " + champAnnotation.getId());
         /* On exécute la requête. */
         log.debug("findAnnotationValeurManager : Exécution de la requête : \n{}", sb);
         final EntityManager em = entityManagerFactory.createEntityManager();
         final Query query = em.createQuery(sb.toString());
         try{
            retour = query.getSingleResult();
         }catch(final NoResultException e){
            retour = null;
         }
      }
      if(retour != null){
         return retour.toString();
      }
      return null;
   }

   @Override
   public List<ChampAnnotation> findAllObjectsManager(){
      log.debug("Recherche totalite des ChampAnnotation");
      return champAnnotationDao.findAll();
   }

   @Override
   public List<ChampAnnotation> findByNomLikeManager(String nom, final boolean exactMatch){
      if(!exactMatch){
         nom = nom + "%";
      }
      log.debug("Recherche ChampAnnotation par nom: {} exactMatch {}", nom, exactMatch);
      return champAnnotationDao.findByNom(nom);
   }

   @Override
   public List<ChampAnnotation> findByTableManager(final TableAnnotation table){
      log.debug("Recherche des ChampAnnotation par Table");
      return champAnnotationDao.findByTable(table);
   }

   @Override
   public List<ChampAnnotation> findByTableAndDataTypeManager(final TableAnnotation table, final List<DataType> dataTypeList){
      log.debug("Recherche des ChampAnnotation par Table et par Datatype");
      return champAnnotationDao.findByTableAndDataType(table, dataTypeList);
   }

   @Override
   public boolean findDoublonManager(final ChampAnnotation champ){
      if(champ.getId() == null){
         return champAnnotationDao.findAll().contains(champ);
      }
      return champAnnotationDao.findByExcludedId(champ.getId()).contains(champ);
   }

   @Override
   public Set<AnnotationDefaut> getAnnotationDefautsManager(final ChampAnnotation chp){
      Set<AnnotationDefaut> defauts = new HashSet<>();
      if(chp.getId() != null){
         final ChampAnnotation champ = champAnnotationDao.mergeObject(chp);
         defauts = champ.getAnnotationDefauts();
         // operation empechant LazyInitialisationException
         defauts.isEmpty();
      }
      return defauts;
   }

   @Override
   public Set<AnnotationValeur> getAnnotationValeursManager(final ChampAnnotation chp){
      Set<AnnotationValeur> valeurs = new HashSet<>();
      if(chp.getId() != null){
         final ChampAnnotation champ = champAnnotationDao.mergeObject(chp);
         valeurs = champ.getAnnotationValeurs();
         // operation empechant LazyInitialisationException
         valeurs.isEmpty();
      }
      return valeurs;
   }

   @Override
   public Set<Item> getItemsManager(final ChampAnnotation chp, final Banque bank){
      Set<Item> items = new HashSet<>();
      if(chp.getId() != null){
         final ChampAnnotation champ = champAnnotationDao.mergeObject(chp);

         if(champ.getTableAnnotation().getCatalogue() == null){
            items = champ.getItems();
            // operation empechant LazyInitialisationException
            items.isEmpty();
         }else if(bank != null){
            items = new LinkedHashSet<>(itemDao.findByChampAndPlateforme(champ, bank.getPlateforme()));
         }
      }
      return items;
   }

   @Override
   public ChampCalcule getChampCalculeManager(final ChampAnnotation chpAnno){
      ChampCalcule champCalcule = null;
      if(chpAnno.getId() != null){
         final ChampAnnotation champAnnotation = champAnnotationDao.mergeObject(chpAnno);
         champCalcule = champCalculeManager.findByChampAnnotationManager(champAnnotation);
      }
      return champCalcule;
   }

   @Override
   public Integer findMaxItemLength(final Set<Item> items){
      final Iterator<Item> it = items.iterator();
      Integer max = 0;
      Integer current = 0;
      while(it.hasNext()){
         current = it.next().getLabel().length();
         if(current > max){
            max = current;
         }
      }
      return max;
   }

   @Override
   public void moveChampOrderUpDownManager(final ChampAnnotation champ, final boolean up){
      //		Set<ChampAnnotation> chps = tableAnnotationManager
      //					.getChampAnnotationsManager(champ.getTableAnnotation());
      final Set<ChampAnnotation> chps = getAllChampsFromTableManager(champ);

      ChampAnnotation chpPrev = null;
      ChampAnnotation chp = null;
      ChampAnnotation chpNext = null;

      final List<ChampAnnotation> chpsList = new ArrayList<>(chps);

      int i;
      for(i = 0; i < chpsList.size(); i++){
         chpPrev = chp;
         chp = chpsList.get(i);
         if(chp.equals(champ)){
            if(i + 1 < chpsList.size()){
               chpNext = chpsList.get(i + 1);
            }
            break;
         }
      }

      int newOrdre;
      if(up){
         if(null != chp && chpPrev != null){

            newOrdre = chpPrev.getOrdre();

            // assigne l'ordre au champ n-1
            chpPrev.setOrdre(chp.getOrdre());

            // assigne le newOrdre au champe n
            chp.setOrdre(newOrdre);
         }
      }else{
         if(null != chp && chpNext != null){

            newOrdre = chpNext.getOrdre();

            // assigne l'ordre a la table n-1
            chpNext.setOrdre(chp.getOrdre());

            // assigne le newOrdre a la table n
            chp.setOrdre(newOrdre);
         }
      }
      champAnnotationDao.mergeObject(chp);
      champAnnotationDao.mergeObject(chpPrev);
      champAnnotationDao.mergeObject(chpNext);
   }

   @Override
   public void removeObjectManager(final ChampAnnotation champAnnotation, final String comments, final Utilisateur user,
      final String baseDir){
      if(champAnnotation != null){
         // supprime le dossier si annotation fichier
         if(champAnnotation.getDataType().getType().equals("fichier")){
            createOrDeleteFileDirectoryManager(baseDir, champAnnotation, true, getBanquesFromTableManager(champAnnotation));
         }

         if(null != champAnnotation.getChampCalcule()){
            champCalculeManager.removeObjectManager(champAnnotation.getChampCalcule());
         }

         champAnnotationDao.removeObject(champAnnotation.getId());
         log.info("Suppression objet ChampAnnotation " + champAnnotation.toString());

         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(champAnnotation, operationManager, comments, user);

      }else{
         log.warn("Suppression d'un ChampAnnotation null");
      }
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes
    * sont non nulls et lance la validation via le Validator.
    * @param champ
    * @param table
    * @param dataType
    * @param items
    * @param defauts
    * @param operation demandant la verification
    */
   private void checkRequiredObjectsAndValidate(final ChampAnnotation champ, final TableAnnotation table, final DataType dataType,
      final List<Item> items, final List<AnnotationDefaut> defauts, final String operation){

      //table required
      if(table != null){
         // merge dataType object
         champ.setTableAnnotation(tableAnnotationDao.mergeObject(table));
      }else if(champ.getTableAnnotation() == null){
         log.warn("Objet obligatoire TableAnnotation manquant" + " lors de la " + operation + " de champ");
         throw new RequiredObjectIsNullException("ChampAnnotation", operation, "TableAnnotation");
      }

      //DataType required
      if(dataType != null){
         // merge dataType object
         champ.setDataType(dataTypeDao.mergeObject(dataType));
      }else if(champ.getDataType() == null){
         log.warn("Objet obligatoire DataType manquant" + " lors de la " + operation + " de champ");
         throw new RequiredObjectIsNullException("ChampAnnotation", operation, "DataType");
      }

      //Validation
      BeanValidator.validateObject(champ, new Validator[] {champAnnotationValidator});

      // on parcourt une premiere fois la liste d'items et de defauts pour
      // appliquer la validation
      if(items != null){
         for(int i = 0; i < items.size(); i++){
            BeanValidator.validateObject(items.get(i), new Validator[] {itemValidator});
         }
      }

      if(defauts != null){
         for(int i = 0; i < defauts.size(); i++){
            BeanValidator.validateObject(defauts.get(i), new Validator[] {annotationCommonValidator});
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre un champ et
    * une liste de Item.
    * @param champ
    * @param liste d'items
    * @param banque courante
    */
   private void updateItems(final ChampAnnotation champAnno, final List<Item> items, final Banque bank){

      final ChampAnnotation champ = champAnnotationDao.mergeObject(champAnno);

      final Set<Item> chpIts = getItemsManager(champAnno, bank);

      final Iterator<Item> it = chpIts.iterator();

      final List<Item> itemsToRemove = new ArrayList<>();
      // on parcourt les items qui sont actuellement associées
      // au champ
      while(it.hasNext()){
         final Item tmp = it.next();
         // si un item n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!items.contains(tmp)){
            itemsToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des items à retirer de
      // l'association
      for(int i = 0; i < itemsToRemove.size(); i++){
         final Item item = itemDao.mergeObject(itemsToRemove.get(i));
         // on retire l'item de l'association et on le supprime
         champ.getItems().remove(item);
         itemDao.removeObject(item.getItemId());
         log.debug("Suppression de l'association entre le champ : {} et l'item {}", champ, item);
      }

      final Set<Item> its = new LinkedHashSet<>();

      for(int i = 0; i < items.size(); i++){
         // si un item n'était pas associé au champ
         if(!chpIts.contains(items.get(i))){
            log.debug("Ajout de l'association entre le champ : {} et l'item : {}", champ, items.get(i));
         }else{
            log.debug("Merge/Update de l'item {} pour le champ {}", items.get(i), champ);
         }
         // on ajoute l'item dans l'association
         its.add(itemDao.mergeObject(items.get(i)));
      }
      champAnno.setItems(its);
   }

   /**
    * Cette méthode met à jour les associations entre un champ et
    * une liste de valeurs par défaut.
    * @param champ
    * @param liste d'AnnotationDefaut
    */
   private void updateDefauts(final ChampAnnotation champAnno, final List<AnnotationDefaut> defauts){

      final ChampAnnotation champ = champAnnotationDao.mergeObject(champAnno);

      final Iterator<AnnotationDefaut> it = getAnnotationDefautsManager(champAnno).iterator();

      final List<AnnotationDefaut> defautsToRemove = new ArrayList<>();
      // on parcoure les défauts qui sont actuellement associées
      // au champ
      while(it.hasNext()){
         final AnnotationDefaut tmp = it.next();
         // si un defaut n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!defauts.contains(tmp)){
            defautsToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des defauts à retirer de
      // l'association
      for(int i = 0; i < defautsToRemove.size(); i++){
         // AnnotationDefaut defaut = annotationDefautDao
         //							.mergeObject(defautsToRemove.get(i));
         // on retire l'item de l'association et on le supprime
         champ.getAnnotationDefauts().remove(defautsToRemove.get(i));
         annotationDefautDao.removeObject(defautsToRemove.get(i).getAnnotationDefautId());
         log.debug("Suppression de l'association entre le champ : {} et la valeur par defaut {}", champ, defautsToRemove.get(i));

      }

      final Set<AnnotationDefaut> defs = new LinkedHashSet<>();

      // on parcoure la nouvelle liste de valeurs par defaut
      for(int i = 0; i < defauts.size(); i++){
         // si un defaut n'était pas associé au champ
         if(!champ.getAnnotationDefauts().contains(defauts.get(i))){
            // on ajoute le defaut dans l'association sauf si elle porte
            // sur un item car deja cascadé
            if(defauts.get(i).getItem() != null){
               final Iterator<Item> itor = champ.getItems().iterator();
               while(itor.hasNext()){
                  final Item item = itor.next();
                  if(item.equals(defauts.get(i).getItem())){
                     defauts.get(i).setItem(item);
                  }
               }
            }
            log.debug("Ajout de l'association entre le champ : {} et la valeur par defaut : {}", champ, defauts.get(i));

         }else{
            log.debug("Modification de la valeur par defaut : {}",defauts.get(i));
         }
         defs.add(annotationDefautDao.mergeObject(defauts.get(i)));
      }
      champAnno.setAnnotationDefauts(defs);
   }

   /**
    * Misa à jour du champCalcule
    * @param champAnno
    * @param champCalcule
    */
   private void updateChampCalcule(final ChampAnnotation champAnno, final ChampCalcule champCalcule){
      final ChampCalcule oldChampCalcule = getChampCalculeManager(champAnno);
      if(null != oldChampCalcule){
         if(oldChampCalcule.getChampCalculeId() != champCalcule.getChampCalculeId()){
            champCalculeManager.removeObjectManager(oldChampCalcule);
            champCalculeManager.createObjectManager(champCalcule);
         }else{
            champCalculeManager.updateObjectManager(champCalcule);
         }
      }else{
         champCalculeManager.createObjectManager(champCalcule);
      }
   }

   @Override
   public void createOrDeleteFileDirectoryManager(final String baseDir, final ChampAnnotation chp, final boolean delete,
      final List<Banque> banques){
      if(chp.getId() != null){
         String path;
         Banque bank;
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            bank = it.next();
            path = Utils.writeAnnoFilePath(baseDir, bank, chp, null);
            if(!delete){
               if(new File(path).mkdirs()){
                  log.debug("Creation file system {}",  path);
               }else{
                  log.error("Erreur dans la creation du systeme " + "de fichier pour le champ " + chp.toString());
               }
            }else{
               // supprime le contenu
               final File[] annos = new File(path).listFiles();
               if(annos != null){
                  for(int i = 0; i < annos.length; i++){
                     annos[i].delete();
                     log.debug("Supprime {}",annos[i].getName());
                  }
               }
               // supprime le dossier
               new File(path).delete();
            }
         }
      }
   }

   @Override
   public void createOrUpdateObjectManager(final ChampAnnotation champ, final TableAnnotation table, final DataType dataType,
      final List<Item> items, final List<AnnotationDefaut> defauts, final Utilisateur utilisateur, final Banque banque,
      final String operation, final String baseDir){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      checkRequiredObjectsAndValidate(champ, table, dataType, items, defauts, operation);

      //Doublon
      if(!findDoublonManager(champ)){

         if((operation.equals("creation") || operation.equals("modification"))){
            try{
               if(operation.equals("creation")){
                  champAnnotationDao.createObject(champ);
                  log.info("Enregistrement objet ChampAnnotation " + champ.toString());
                  // cree l'arborescence si annotation fichier
                  if(champ.getDataType().getType().equals("fichier")){
                     createOrDeleteFileDirectoryManager(baseDir, champ, false, getBanquesFromTableManager(champ));
                  }
                  CreateOrUpdateUtilities.createAssociateOperation(champ, operationManager,
                     operationTypeDao.findByNom("Creation").get(0), utilisateur);
               }else{
                  champAnnotationDao.updateObject(champ);
                  log.info("Modification objet ChampAnnotation " + champ.toString());
                  CreateOrUpdateUtilities.createAssociateOperation(champ, operationManager,
                     operationTypeDao.findByNom("Modification").get(0), utilisateur);
               }
               // ajout association vers items
               if(items != null){
                  updateItems(champ, items, banque);
               }

               // ajout association vers items
               if(defauts != null){
                  updateDefauts(champ, defauts);
               }

            }catch(final RuntimeException re){
               // en cas d'erreur lors enregistrement d'un champ du a
               // l'acces au filesystem
               // le rollback se fera mais objet aura un id assigne
               // qui déclenchera une TransientException si on essaie
               // d'enregistrer a nouveau.
               if(operation.equals("creation")){
                  champ.setId(null);
               }
               throw re;
            }
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors " + operation + " objet ChampAnnotation " + champ.toString());
         throw new DoublonFoundException("ChampAnnotation", operation);
      }
   }

   @Override
   public void createOrUpdateObjectManager(final ChampAnnotation champ, final TableAnnotation table, final DataType dataType,
      final List<Item> items, final List<AnnotationDefaut> defauts, final ChampCalcule champCalcule,
      final Utilisateur utilisateur, final Banque banque, final String operation, final String baseDir){
      createOrUpdateObjectManager(champ, table, dataType, items, defauts, utilisateur, banque, operation, baseDir);

      if(champCalcule != null){
         updateChampCalcule(champ, champCalcule);
      }
   }

   @Override
   public Set<ChampAnnotation> getAllChampsFromTableManager(final ChampAnnotation chp){
      Set<ChampAnnotation> champs = new HashSet<>();
      final TableAnnotation tab = chp.getTableAnnotation();
      if(tab != null && tab.getTableAnnotationId() != null){
         final TableAnnotation table = tableAnnotationDao.mergeObject(tab);
         champs = table.getChampAnnotations();
         champs.isEmpty(); // operation empechant LazyInitialisationException
      }
      return champs;
   }

   @Override
   public List<Banque> getBanquesFromTableManager(final ChampAnnotation chp){
      final List<Banque> banks = new ArrayList<>();
      if(chp != null && chp.getTableAnnotation() != null && chp.getTableAnnotation().getTableAnnotationId() != null){
         return banqueDao.findByTableAnnotation(chp.getTableAnnotation());
      }
      return banks;

   }

   @Override
   public List<ChampAnnotation> findByEditByCatalogueManager(final TableAnnotation tab){
      return champAnnotationDao.findByEditByCatalogue(tab);
   }

   @Override
   public boolean isUsedItemManager(final Item item){
      return annotationValeurDao.findCountByItem(item).get(0) > 0;
   }

   @Override
   public List<ChampAnnotation> findChampsFichiersByTableManager(final TableAnnotation table){
      return champAnnotationDao.findByTableAndType(table, dataTypeDao.findByType("fichier").get(0));
   }

   @Override
   public List<? extends Object> isUsedObjectManager(final ChampAnnotation c){
      final List<Object> objs = new ArrayList<>();
      final Iterator<?> crits = champAnnotationDao.findCriteresByChampAnnotation(c).iterator();
      while(crits.hasNext()){
         objs.add(crits.next());
      }
      final Iterator<?> imports = champAnnotationDao.findImportColonnesByChampAnnotation(c).iterator();
      while(imports.hasNext()){
         objs.add(imports.next());
      }
      final Iterator<?> res = champAnnotationDao.findResultatsByChampAnnotation(c).iterator();
      while(res.hasNext()){
         objs.add(res.next());
      }
      final Iterator<?> chpL = champAnnotationDao.findChpLEtiquetteByChampAnnotation(c).iterator();
      while(chpL.hasNext()){
         objs.add(chpL.next());
      }
      return objs;
   }

   @Override
   public List<ChampAnnotation> findByImportTemplateAndEntiteManager(final ImportTemplate ip, final Entite e){
      return champAnnotationDao.findByImportTemplateAndEntite(ip, e);
   }
}
