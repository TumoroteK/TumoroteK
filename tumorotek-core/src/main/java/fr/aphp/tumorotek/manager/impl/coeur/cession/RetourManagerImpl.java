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
package fr.aphp.tumorotek.manager.impl.coeur.cession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.cession.RetourDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.stockage.IncidentDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectStatutException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.retour.RetourValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Retour.
 * Classe créée le 25/01/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public class RetourManagerImpl implements RetourManager
{

   private final Log log = LogFactory.getLog(RetourManager.class);

   /* Beans injectes par Spring*/
   private RetourDao retourDao;
   private EntiteDao entiteDao;
   private OperationManager operationManager;
   private OperationTypeDao operationTypeDao;

   private CollaborateurDao collaborateurDao;
   private CessionDao cessionDao;
   private TransformationDao transformationDao;
   private EmplacementManager emplacementManager;
   private EmplacementDao emplacementDao;
   private IncidentDao incidentDao;
   private RetourValidator retourValidator;
   private ObjetStatutDao objetStatutDao;
   private EchantillonDao echantillonDao;
   private ProdDeriveDao prodDeriveDao;
   private DataSource dataSource;

   public RetourManagerImpl(){}

   /* Properties setters */
   public void setRetourDao(final RetourDao rDao){
      this.retourDao = rDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setCessionDao(final CessionDao cDao){
      this.cessionDao = cDao;
   }

   public void setTransformationDao(final TransformationDao tDao){
      this.transformationDao = tDao;
   }

   public void setEmplacementManager(final EmplacementManager eM){
      this.emplacementManager = eM;
   }

   public void setEmplacementDao(final EmplacementDao ea){
      this.emplacementDao = ea;
   }

   public void setIncidentDao(final IncidentDao iDao){
      this.incidentDao = iDao;
   }

   public void setRetourValidator(final RetourValidator rValidator){
      this.retourValidator = rValidator;
   }

   public void setObjetStatutDao(final ObjetStatutDao oD){
      this.objetStatutDao = oD;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   public void setDataSource(final DataSource d){
      this.dataSource = d;
   }

   @Override
   public void createOrUpdateObjectManager(final Retour retour, TKStockableObject objet, Emplacement emp,
      final Collaborateur collaborateur, final Cession cession, final Transformation transformation, final Incident incident,
      final Utilisateur utilisateur, final String operation){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Objet rengresitré required
      if(objet != null && objet.listableObjectId() != null){
         retour.setObjetId(objet.listableObjectId());
         retour.setEntite(entiteDao.findByNom(objet.entiteNom()).get(0));
         retour.setTkObject(objet);
      }else if(retour.getObjetId() == null){
         log.warn("Objet obligatoire Objet manquant" + " lors de la " + operation + " d'un retour");
         throw new RequiredObjectIsNullException("Retour", operation, "Objet");
      }else{
         objet = getObjetFromRetourManager(retour);
      }

      if(emp == null && operation.equals("creation")){
         emp = objet.getEmplacement();
         emp = emplacementDao.mergeObject(emp);
      }

      checkRequiredObjectsAndValidate(retour, objet, emp, collaborateur, cession, transformation, incident, operation);

      //Doublon
      if(!findDoublonManager(retour)){

         // conserve le statut de l'objet en copie dans le retour
         // change le statut du StockableObject EVENEMENT EN COURS
         if(retour.getDateRetour() == null){
            if(!objet.getObjetStatut().getStatut().equals("ENCOURS")){
               retour.setObjetStatut(objet.getObjetStatut());
               objet.setObjetStatut(objetStatutDao.findByStatut("ENCOURS").get(0));
            }
         }else if(retour.getObjetStatut() != null){ // attribue le statut de l'objet courant
            objet.setObjetStatut(retour.getObjetStatut());
            retour.setObjetStatut(null);
         }

         // casse la sterilité
         if(retour.getSterile() != null && objet instanceof Echantillon && !retour.getSterile()
            && (((Echantillon) objet).getSterile() == null || ((Echantillon) objet).getSterile())){
            ((Echantillon) objet).setSterile(false);
         }

         if((operation.equals("creation") || operation.equals("modification"))){
            if(operation.equals("creation")){
               retourDao.createObject(retour);
               log.info("Enregistrement du retour " + retour.toString());
               CreateOrUpdateUtilities.createAssociateOperation(retour, operationManager,
                  operationTypeDao.findByNom("Creation").get(0), utilisateur);
            }else{
               retourDao.updateObject(retour);
               log.info("Modification objet Retour " + retour.toString());
               CreateOrUpdateUtilities.createAssociateOperation(retour, operationManager,
                  operationTypeDao.findByNom("Modification").get(0), utilisateur);
            }

            // merge
            if(objet instanceof Echantillon){
               echantillonDao.mergeObject((Echantillon) objet);
            }else if(objet instanceof ProdDerive){
               prodDeriveDao.mergeObject((ProdDerive) objet);
            }

         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors " + operation + " objet Retour " + retour.toString());
         throw new DoublonFoundException("Retour", operation);
      }
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes 
    * sont non nulls et lance la validation via le Validator.
    * Lance une exception si l'objet est en statut ENCOURS.
    * @param retour
    * @param objet
    * @param emplacement
    * @param collaborateur
    * @param cession
    * @param transformation
    * @param incident
    * @param operation
    */
   private void checkRequiredObjectsAndValidate(final Retour retour, final TKStockableObject objet, final Emplacement emplacement,
      final Collaborateur collaborateur, final Cession cession, final Transformation transformation, final Incident incident,
      final String operation){

      // collaborateur
      retour.setCollaborateur(collaborateurDao.mergeObject(collaborateur));
      retour.setCession(cessionDao.mergeObject(cession));
      retour.setTransformation(transformationDao.mergeObject(transformation));
      retour.setIncident(incidentDao.mergeObject(incident));

      // peule les champs emplAdrl et Conteneur à partir de Emplacement
      // utilise emplacement passé en paramètre car correspond à 
      // l'emplacement d'origine lors d'un déplacement
      if(emplacement != null){
         retour.setOldEmplacementAdrl(emplacementManager.getAdrlManager(emplacement, false));
         retour.setConteneur(emplacementManager.getConteneurManager(emplacement));
      }

      //Validation
      BeanValidator.validateObject(retour, new Validator[] {retourValidator});

   }

   @Override
   public boolean findDoublonManager(final Retour retour){
      if(retour.getRetourId() == null){
         return retourDao.findByObject(retour.getObjetId(), retour.getEntite()).contains(retour);
      }else{
         return retourDao.findByExcludedId(retour.getRetourId(), retour.getObjetId(), retour.getEntite()).contains((retour));
      }
   }

   @Override
   public List<Retour> findAllObjectsManager(){
      log.debug("Recherche totalite des Retour");
      return retourDao.findAll();
   }

   @Override
   public void removeObjectManager(final Retour retour){
      if(retour != null){
         if(retour.getObjetStatut() != null){
            final TKStockableObject obj = getObjetFromRetourManager(retour);
            obj.setObjetStatut(retour.getObjetStatut());
            if(obj instanceof Echantillon){
               echantillonDao.mergeObject((Echantillon) obj);
            }else{
               prodDeriveDao.mergeObject((ProdDerive) obj);
            }
         }
         retourDao.removeObject(retour.getRetourId());
         log.info("Suppression objet Retour " + retour.toString());
         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(retour, operationManager);
      }else{
         log.warn("Suppression d'un Retour null");
      }
   }

   @Override
   public List<Retour> getRetoursForObjectManager(final TKStockableObject objet){

      Entite entite = null;
      Integer objetId = null;

      if(objet != null){
         final String objetClass = objet.getClass().getSimpleName();
         // L'objet sur lequel s'applique un Retour peut être un échantillon 
         // ou un produit dérivé.
         entite = entiteDao.findByNom(objetClass).get(0);

         if(objetClass.equals("Echantillon")){
            objetId = ((Echantillon) objet).getEchantillonId();
         }else if(objetClass.equals("ProdDerive")){
            objetId = ((ProdDerive) objet).getProdDeriveId();
         }
      }
      return retourDao.findByObject(objetId, entite);
   }

   @Override
   public void createRetourListManager(final List<TKStockableObject> objects,
      final List<OldEmplTrace> oldEmpAdrls, final Retour retour, final Collaborateur collaborateur,
      final Cession cession, final Transformation transformation, final Incident incident, final Utilisateur utilisateur){

      Retour newRet;
      if(objects != null){
         for(int i = 0; i < objects.size(); i++){
            newRet = retour.clone();
            newRet.setRetourId(null);

            // emplacement actuel ou avant déplacement
            Emplacement emp = null;
            if(oldEmpAdrls != null && oldEmpAdrls.contains(new OldEmplTrace(objects.get(i), null, null, null))){
              emp = oldEmpAdrls.get(oldEmpAdrls.indexOf(new OldEmplTrace(objects.get(i), null, null, null))).getCurrent();
            }

            createOrUpdateObjectManager(newRet, objects.get(i), emp, collaborateur, cession, transformation, incident,
               utilisateur, "creation");
         }
      }

   }

   @Override
   public boolean createRetourHugeListManager(final List<TKStockableObject> objects,
      final List<OldEmplTrace> oldEmpAdrls, final Retour retour, final Collaborateur collaborateur,
      final Cession cession, final Transformation transformation, final Incident incident, final Utilisateur utilisateur){

      final boolean ok = true;

      if(objects != null){

         // ids objs dont les retours pourraient rentrer en conflit 
         // avec le retour créé
         final Set<Integer> objsEchanIds = new HashSet<>();
         objsEchanIds
            .addAll(retourDao.findObjIdsByDatesAndEntite(retour.getDateSortie(), entiteDao.findByNom("Echantillon").get(0)));
         objsEchanIds
            .addAll(retourDao.findObjIdsByDatesAndEntite(retour.getDateRetour(), entiteDao.findByNom("Echantillon").get(0)));
         objsEchanIds.addAll(retourDao.findObjIdsInsideDatesEntite(retour.getDateSortie(), retour.getDateRetour(),
            entiteDao.findByNom("Echantillon").get(0)));
         final Set<Integer> objsDeriveIds = new HashSet<>();
         objsDeriveIds
            .addAll(retourDao.findObjIdsByDatesAndEntite(retour.getDateSortie(), entiteDao.findByNom("ProdDerive").get(0)));
         objsDeriveIds
            .addAll(retourDao.findObjIdsByDatesAndEntite(retour.getDateRetour(), entiteDao.findByNom("ProdDerive").get(0)));
         objsDeriveIds.addAll(retourDao.findObjIdsInsideDatesEntite(retour.getDateSortie(), retour.getDateRetour(),
            entiteDao.findByNom("ProdDerive").get(0)));

         final List<Integer> echansId = new ArrayList<>();
         final List<Integer> derivesId = new ArrayList<>();

         Connection conn = null;
         PreparedStatement pstmt = null;
         PreparedStatement pstmtE = null;
         PreparedStatement pstmtEste = null;
         PreparedStatement pstmtD = null;
         // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

         // Integer idRetour = retourDao.findByMaxId().get(0);

         try{

            final String sql = "insert into RETOUR (OBJET_ID, " + "ENTITE_ID, DATE_SORTIE, DATE_RETOUR, TEMP_MOYENNE, "
               + "STERILE, IMPACT, COLLABORATEUR_ID, OBSERVATIONS, " + "OLD_EMPLACEMENT_ADRL, CESSION_ID, TRANSFORMATION_ID, "
               + "CONTENEUR_ID, INCIDENT_ID, OBJET_STATUT_ID) " + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            final String echanUp = "update ECHANTILLON set objet_statut_id=6 " + "where echantillon_id = ?";

            final String echanUpSte = "update ECHANTILLON set sterile=0 " + "where echantillon_id = ?";

            final String deriveUp = "update PROD_DERIVE set objet_statut_id=6  " + "where prod_derive_id = ?";

            conn = DataSourceUtils.getConnection(dataSource);

            pstmt = conn.prepareStatement(sql);
            pstmtE = conn.prepareStatement(echanUp);
            pstmtEste = conn.prepareStatement(echanUpSte);
            pstmtD = conn.prepareStatement(deriveUp);

            Emplacement emp = null;
            boolean isEchan = true;
            boolean isRetourComplete = false;

            for(int i = 0; i < objects.size(); i++){

               // RETOUR_ID
               // ++idRetour;
               // pstmt.setInt(1, idRetour);

               isEchan = objects.get(i) instanceof Echantillon;

               // OBJET_ID
               pstmt.setInt(1, objects.get(i).listableObjectId());
               if(isEchan){
                  echansId.add(objects.get(i).listableObjectId());
               }else{
                  derivesId.add(objects.get(i).listableObjectId());
               }

               // ENTITE_ID
               if(isEchan){
                  pstmt.setInt(2, 3);
               }else{
                  pstmt.setInt(2, 8);
               }

               // DATE_SORTIE
               if(retour.getDateSortie() != null){
                  //	String date = sdf.format(retour.getDateSortie()
                  //			.getTime());
                  pstmt.setTimestamp(3, new java.sql.Timestamp(retour.getDateSortie().getTimeInMillis()));
               }else{
                  pstmt.setNull(3, Types.DATE);
               }

               // DATE_RETOUR
               if(retour.getDateRetour() != null){
                  // String date = sdf.format(retour.getDateRetour()
                  //		.getTime());
                  pstmt.setTimestamp(4, new java.sql.Timestamp(retour.getDateRetour().getTimeInMillis()));
                  isRetourComplete = true;
               }else{
                  pstmt.setNull(4, Types.DATE);
                  isRetourComplete = false;
               }

               // TEMP_MOYENNE
               if(retour.getTempMoyenne() != null){
                  pstmt.setFloat(5, retour.getTempMoyenne());
               }else{
                  pstmt.setNull(5, Types.FLOAT);
               }

               // STERILE
               if(retour.getSterile() != null){
                  pstmt.setBoolean(6, retour.getSterile());
               }else{
                  pstmt.setNull(6, Types.TINYINT);
               }

               // IMPACT
               if(retour.getImpact() != null){
                  pstmt.setBoolean(7, retour.getImpact());
               }else{
                  pstmt.setNull(7, Types.TINYINT);
               }

               // COLLABORATEUR_ID
               if(collaborateur != null){
                  pstmt.setInt(8, collaborateur.getCollaborateurId());
               }else{
                  pstmt.setNull(8, Types.INTEGER);
               }

               // OBSERVATIONS
               if(retour.getObservations() != null){
                  pstmt.setString(9, retour.getObservations());
               }else{
                  pstmt.setNull(9, Types.VARCHAR);
               }

               // OLD_EMPLACEMENT_ADRL
               // @since 2.2.3-genno
               // fix https://tumorotek.myjetbrains.com/youtrack/issue/TK-291
               if(oldEmpAdrls != null && oldEmpAdrls.contains((new OldEmplTrace(objects.get(i), null, null, null)))){
                  pstmt.setString(10, oldEmpAdrls.get(oldEmpAdrls
                		.indexOf(new OldEmplTrace(objects.get(i), null, null, null))).getOldAdrl());
                  pstmt.setInt(13,  oldEmpAdrls.get(oldEmpAdrls
                  		.indexOf(new OldEmplTrace(objects.get(i), null, null, null))).getConteneur()
                		  	.getConteneurId());
               }else if(objects.get(i).getEmplacement() != null){
                  emp = emplacementDao.mergeObject(objects.get(i).getEmplacement());
                  pstmt.setString(10, emplacementManager.getAdrlManager(emp, false));
                  pstmt.setInt(13, emplacementManager.getConteneurManager(emp).getConteneurId());
               }else{
                  pstmt.setNull(10, Types.VARCHAR);
                  pstmt.setNull(13, Types.INTEGER);
               }

               // CESSION_ID
               if(cession != null){
                  pstmt.setInt(11, cession.getCessionId());
               }else{
                  pstmt.setNull(11, Types.INTEGER);
               }

               // TRANSFORMATION_ID
               if(transformation != null){
                  pstmt.setInt(12, transformation.getTransformationId());
               }else{
                  pstmt.setNull(12, Types.INTEGER);
               }

               // INCIDENT_ID
               if(incident != null){
                  pstmt.setInt(14, incident.getIncidentId());
               }else{
                  pstmt.setNull(14, Types.INTEGER);
               }

               // casse sterilité
               if(isEchan){
                  // casse sterilite
                  if(retour.getSterile() != null && !retour.getSterile()
                     && (((Echantillon) objects.get(i)).getSterile() == null || ((Echantillon) objects.get(i)).getSterile())){
                     pstmtEste.setInt(1, ((TKdataObject) objects.get(i)).listableObjectId());
                     pstmtEste.addBatch();
                  }
               }

               // update objet si retour incomplet
               if(!isRetourComplete && !(objects.get(i).getObjetStatut().getStatut().equals("EPUISE")
                  || objects.get(i).getObjetStatut().getStatut().equals("DETRUIT"))){
                  pstmt.setInt(15, objects.get(i).getObjetStatut().getObjetStatutId());
                  if(isEchan){
                     pstmtE.setInt(1, ((TKdataObject) objects.get(i)).listableObjectId());
                     pstmtE.addBatch();
                  }else{
                     pstmtD.setInt(1, ((TKdataObject) objects.get(i)).listableObjectId());
                     pstmtD.addBatch();
                  }
               }else{
                  pstmt.setNull(15, Types.INTEGER);
               }

               // check validation
               final TKStockableObject obj = objects.get(i);
               if(obj != null && (obj.getObjetStatut().getStatut().equals("EPUISE")
                  // || obj.getObjetStatut().getStatut().equals("RESERVE")
                  || obj.getObjetStatut().getStatut().equals("ENCOURS")) && retour.getRetourId() == null){
                  throw new ObjectStatutException(entiteDao.findByNom(obj.entiteNom()).get(0).getNom(), "évènement de stockage");
               }else if(retour.getDateSortie().before(obj.getDateStock())){
                  // throw new TKException("date.validation.infDateStockage: " 
                  //	+ obj.getCode());
                  continue;
               }else{
                  if(isEchan){
                     if(objsEchanIds.contains(obj.listableObjectId())){
                        throw new TKException("date.validation.retourExistant.incoherent", obj.getCode());
                     }
                  }else{
                     if(objsDeriveIds.contains(obj.listableObjectId())){
                        throw new TKException("date.validation.retourExistant.incoherent", obj.getCode());
                     }
                  }
               }
               // checkRequiredObjectsAndValidate(retour, isEchan ? (Echantillon) objects.get(i) : (ProdDerive) objects.get(i), 
               //		null, collaborateur, cession, transformation, incident, "creation");

               pstmt.addBatch();
            }

            pstmt.executeBatch();
            pstmtE.executeBatch();
            pstmtEste.executeBatch();
            pstmtD.executeBatch();

         }catch(final CannotGetJdbcConnectionException e1){
            throw new RuntimeException(e1);
         }catch(final SQLException e1){
            throw new RuntimeException(e1);
         }catch(final TKException r1){
            throw new RuntimeException(r1);
         }catch(final Exception e1){
            throw new RuntimeException(e1);
         }finally{
            if(pstmt != null){
               try{
                  pstmt.close();
               }catch(final Exception e){
                  pstmt = null;
               }
            }
            if(pstmtE != null){
               try{
                  pstmtE.close();
               }catch(final Exception e){
                  pstmtE = null;
               }
            }
            if(pstmtEste != null){
               try{
                  pstmtEste.close();
               }catch(final Exception e){
                  pstmtEste = null;
               }
            }
            if(pstmtD != null){
               try{
                  pstmtD.close();
               }catch(final Exception e){
                  pstmtD = null;
               }
            }
            if(conn != null){
               try{
                  conn.close();
               }catch(final Exception e){
                  conn = null;
               }
            }
         }
      }

      return ok;

   }

   @Override
   public TKStockableObject getObjetFromRetourManager(final Retour retour){
      if(retour.getEntite() != null){
         if(retour.getEntite().getNom().equals("Echantillon")){
            return echantillonDao.findById(retour.getObjetId());
         }else{
            return prodDeriveDao.findById(retour.getObjetId());
         }
      }
      return null;
   }

   @Override
   public List<Retour> findByObjectDateRetourEmptyManager(final List<Integer> objIds, final Entite e){
      List<Retour> rs = new ArrayList<>();

      if(objIds != null && !objIds.isEmpty()){
         rs = retourDao.findByObjectsDateRetourEmpty(objIds, e);
      }

      //		if (!rs.isEmpty()) {
      //			if (rs.size() == 1) {
      //				return rs.get(0);
      //			} else {
      //				throw new 
      //				ManyIncompleteRetourException(getObjetFromRetourManager(rs.get(0)));
      //			}
      //		}

      return rs;
   }

   @Override
   public void updateMultipleObjectManager(final List<Retour> retours, final Collaborateur collaborateur, final Cession cession,
      final Transformation transformation, final Incident incident, final Utilisateur utilisateur){
      if(retours != null){
         final Iterator<Retour> rIt = retours.iterator();
         Retour retour = null;
         try{
            while(rIt.hasNext()){
               retour = rIt.next();
               createOrUpdateObjectManager(retour, null, null, collaborateur != null ? collaborateur : retour.getCollaborateur(),
                  cession != null ? cession : retour.getCession(),
                  transformation != null ? transformation : retour.getTransformation(),
                  incident != null ? incident : retour.getIncident(), utilisateur, "modification");
            }
         }catch(final Exception e){
            final TKException te = new TKException();
            final TKStockableObject obj = getObjetFromRetourManager(retour);
            te.setEntiteObjetException(obj.entiteNom());
            te.setIdentificationObjetException(obj.getCode());

            if(e instanceof ValidationException){
               final Iterator<Errors> errs = (((ValidationException) e).getErrors()).iterator();
               while(errs.hasNext()){
                  te.setMessage(errs.next().getFieldError().getCode());
               }
            }else{
               te.setMessage(e.getMessage());
            }
            throw te;
         }
      }

   }

   @Override
   public List<Retour> findByObjectAndImpactManager(final TKStockableObject obj, final Boolean impact){
      if(obj != null){
         return retourDao.findByObjectAndImpact(obj.listableObjectId(), entiteDao.findByNom(obj.entiteNom()).get(0), impact);
      }
      return new ArrayList<>();
   }
}
