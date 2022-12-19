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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.collection.PersistentSet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditMilieuDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterValidator;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prelevement.gastbi.PrelevementGatsbiValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Implémentation du manager du bean de domaine Prelevement. Classe créée le
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class PrelevementManagerImpl implements PrelevementManager
{

   private final Log log = LogFactory.getLog(PrelevementManager.class);

   private PrelevementDao prelevementDao;

   private PatientManager patientManager;

   private MaladieManager maladieManager;

   private BanqueDao banqueDao;

   private NatureDao natureDao;

   private MaladieDao maladieDao;

   private ConsentTypeDao consentTypeDao;

   private CollaborateurDao collaborateurDao;

   private ServiceDao serviceDao;

   private PrelevementTypeDao prelevementTypeDao;

   private ConditTypeDao conditTypeDao;

   private ConditMilieuDao conditMilieuDao;

   private TransporteurDao transporteurDao;

   private UniteDao uniteDao;

   private PrelevementValidator prelevementValidator;

   private OperationTypeDao operationTypeDao;

   private OperationManager operationManager;

   private EntityManagerFactory entityManagerFactory;

   private EntiteDao entiteDao;

   private TransformationManager transformationManager;

   private TransformationDao transformationDao;

   private LaboInterManager laboInterManager;

   private LaboInterValidator laboInterValidator;

   private EchantillonDao echantillonDao;

   private EchantillonManager echantillonManager;

   private AnnotationValeurManager annotationValeurManager;

   private ImportHistoriqueManager importHistoriqueManager;

   private ProdDeriveManager prodDeriveManager;

   private CederObjetManager cederObjetManager;

   private DossierExterneDao dossierExterneDao;

   private ObjetNonConformeManager objetNonConformeManager;
   //   private PrelevementDelegateDao prelevementDelegateDao;

   public PrelevementManagerImpl(){}

   /* Properties setters */
   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setPatientManager(final PatientManager pManager){
      this.patientManager = pManager;
   }

   public void setMaladieManager(final MaladieManager mManager){
      this.maladieManager = mManager;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setNatureDao(final NatureDao nDao){
      this.natureDao = nDao;
   }

   public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   /**
    * TODO Jamais utilisé ?
    * @param pDao
    */
   public void setPatientDao(final PatientDao pDao){}

   public void setConsentTypeDao(final ConsentTypeDao ctDao){
      this.consentTypeDao = ctDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   public void setPrelevementTypeDao(final PrelevementTypeDao ptDao){
      this.prelevementTypeDao = ptDao;
   }

   public void setConditTypeDao(final ConditTypeDao ctDao){
      this.conditTypeDao = ctDao;
   }

   public void setConditMilieuDao(final ConditMilieuDao cmDao){
      this.conditMilieuDao = cmDao;
   }

   public void setTransporteurDao(final TransporteurDao tDao){
      this.transporteurDao = tDao;
   }

   public void setUniteDao(final UniteDao uDao){
      this.uniteDao = uDao;
   }

   public void setPrelevementValidator(final PrelevementValidator pValidator){
      this.prelevementValidator = pValidator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setEntityManagerFactory(final EntityManagerFactory emFactory){
      this.entityManagerFactory = emFactory;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setTransformationManager(final TransformationManager tManager){
      this.transformationManager = tManager;
   }

   public void setTransformationDao(final TransformationDao tDao){
      this.transformationDao = tDao;
   }

   public void setLaboInterManager(final LaboInterManager lManager){
      this.laboInterManager = lManager;
   }

   public void setLaboInterValidator(final LaboInterValidator lValidator){
      this.laboInterValidator = lValidator;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public void setAnnotationValeurManager(final AnnotationValeurManager aManager){
      this.annotationValeurManager = aManager;
   }

   public void setImportHistoriqueManager(final ImportHistoriqueManager iManager){
      this.importHistoriqueManager = iManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setCederObjetManager(final CederObjetManager cManager){
      this.cederObjetManager = cManager;
   }

   public void setDossierExterneDao(final DossierExterneDao dDao){
      this.dossierExterneDao = dDao;
   }

   public void setObjetNonConformeManager(final ObjetNonConformeManager oM){
      this.objetNonConformeManager = oM;
   }

   @Override
   public void createObjectManager(final Prelevement prelevement, final Banque banque, final Nature nature, final Maladie maladie,
      final ConsentType consentType, final Collaborateur preleveur, final Service servicePreleveur,
      final PrelevementType prelevementType, final ConditType conditType, final ConditMilieu conditMilieu,
      final Transporteur transporteur, final Collaborateur operateur, final Unite quantiteUnite, final List<LaboInter> laboInters,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<File> filesCreated, final Utilisateur utilisateur,
      final boolean doValidation, final String baseDir, final boolean isImport){

      // flag qui declencheront un revert des ids si le prelevement
      // la maladie et le patient sont créés conjointement avec erreur
      boolean revertMaladie = false;
      boolean revertPatient = false;
      if(maladie != null){
         revertMaladie = (maladie.getMaladieId() == null);
         if(revertMaladie){
            revertPatient = (maladie.getPatient().getPatientId() == null);
         }
      }

      try{
         mergeNonRequiredObjects(prelevement, /*maladie,*/ preleveur, servicePreleveur, prelevementType, conditType, conditMilieu,
            transporteur, operateur, quantiteUnite);

         // Verifie required Objects associes et validation
         checkRequiredObjectsAndValidate(prelevement, banque, nature, consentType, maladie, laboInters, "creation", utilisateur,
            doValidation, baseDir);

         // Doublon
         if(!findDoublonManager(prelevement)){

            prelevementDao.createObject(prelevement);
            log.info("Enregistrement objet Prelevement " + prelevement.toString());
            // Enregistrement de l'operation associee
            final Operation creationOp = new Operation();
            creationOp.setDate(Utils.getCurrentSystemCalendar());
            operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Creation").get(0),
               prelevement);

            updateLaboInters(laboInters, prelevement);

            // Annotations
            // update les annotations, null operation pour
            // laisser la possibilité création/modification au sein
            // de la liste
            if(listAnnoToCreateOrUpdate != null){
               annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, prelevement, utilisateur, null,
                  baseDir, filesCreated, null);
            }

         }else{
            log.warn("Doublon lors creation objet Prelevement " + prelevement.toString());
            throw new DoublonFoundException("Prelevement", "creation", prelevement.getCode(), null);
         }
      }catch(final RuntimeException re){
         log.error(re);
         if(filesCreated != null){
            for(final File f : filesCreated){
               f.delete();
            }
         }else{
            log.warn("Rollback création fichier n'a pas pu être réalisée");
         }

         if(!isImport){
            prelevement.setPrelevementId(null);
            if(prelevement.getLaboInters() != null){
               final Iterator<LaboInter> it = prelevement.getLaboInters().iterator();
               while(it.hasNext()){
                  it.next().setLaboInterId(null);
               }
            }
            if(revertMaladie && null != maladie){
               maladie.setMaladieId(null);
            }
            if(revertPatient && null != maladie){
               maladie.getPatient().setPatientId(null);
            }
         }
         throw re;
      }
   }

   /**
    * Enregistre les labos ou les modifications portant sur eux. La validation
    * a été faite dans la validation du prelevement et est donc inutile dans
    * cette methode.
    *
    * @param laboInters
    */
   private void updateLaboInters(final List<LaboInter> laboInters, final Prelevement prelevement){
      // prelevement.setLaboInters(new HashSet<LaboInter>());
      if(laboInters != null){
         LaboInter labo;
         for(int i = 0; i < laboInters.size(); i++){
            labo = laboInters.get(i);
            if(labo.getLaboInterId() == null){
               laboInterManager.createObjectManager(labo, prelevement, labo.getService(), labo.getCollaborateur(),
                  labo.getTransporteur(), false);
            }else{
               laboInterManager.updateObjectManager(labo, prelevement, labo.getService(), labo.getCollaborateur(),
                  labo.getTransporteur(), false);
            }
         }
         prelevement.setLaboInters(new HashSet<>(laboInters));
      }
   }

   @Override
   public void updateObjectManager(final Prelevement prelevement, final Banque banque, final Nature nature, final Maladie maladie,
      final ConsentType consentType, final Collaborateur preleveur, final Service servicePreleveur,
      final PrelevementType prelevementType, final ConditType conditType, final ConditMilieu conditMilieu,
      final Transporteur transporteur, final Collaborateur operateur, final Unite quantiteUnite, final List<LaboInter> laboInters,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final List<File> filesCreated, final List<File> filesToDelete, final Utilisateur utilisateur,
      final Integer cascadeNonSterile, final boolean doValidation, final String baseDir, final boolean multiple){

      mergeNonRequiredObjects(prelevement, /*maladie,*/ preleveur, servicePreleveur, prelevementType, conditType, conditMilieu,
         transporteur, operateur, quantiteUnite);

      // Verifie required Objects associes et validation
      checkRequiredObjectsAndValidate(prelevement, banque, nature, consentType, maladie, laboInters, "modification", null,
         doValidation, baseDir);
      // Doublon
      if(findDoublonManager(prelevement)){
         log.warn("Doublon lors modification objet Prelevement " + prelevement.toString());
         throw new DoublonFoundException("Prelevement", "modification", prelevement.getCode(), null);
      }

      try{

         if(cascadeNonSterile != null){ // cascade non sterile condition
            log.info("Applique la cascade de sterilite" + " depuis le prelevement " + prelevement.toString());
            if(laboInters != null // aucune modif labos
               && laboInters.size() > 0){
               cascadeNonSterileManager(prelevement, laboInters, cascadeNonSterile, true);
            }else{
               cascadeNonSterileManager(prelevement, new ArrayList<>(getLaboIntersWithOrderManager(prelevement)),
                  cascadeNonSterile, true);
            }
         }

         prelevementDao.updateObject(prelevement);
         log.info("Modification objet Prelevement " + prelevement.toString());
         // Enregistrement de l'operation associee
         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         if(!multiple){
            operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("Modification").get(0),
               prelevement);
         }else{
            operationManager.createObjectManager(creationOp, utilisateur, operationTypeDao.findByNom("ModifMultiple").get(0),
               prelevement);
         }

         updateLaboInters(laboInters, prelevement);

         // Annotations
         // suppr les annotations
         if(listAnnoToDelete != null){
            annotationValeurManager.removeAnnotationValeurListManager(listAnnoToDelete, filesToDelete);
         }

         // update les annotations, null operation pour
         // laisser la possibilité création/modification au sein
         // de la liste
         if(listAnnoToCreateOrUpdate != null){
            annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, prelevement, utilisateur, null,
               baseDir, filesCreated, filesToDelete);
         }

         // enregistre operation associee annotation
         // si il y a eu des deletes et pas d'updates
         if((listAnnoToCreateOrUpdate == null || listAnnoToCreateOrUpdate.isEmpty())
            && (listAnnoToDelete != null && !listAnnoToDelete.isEmpty())){
            CreateOrUpdateUtilities.createAssociateOperation(prelevement, operationManager,
               operationTypeDao.findByNom("Annotation").get(0), utilisateur);
         }

         if(filesToDelete != null){
            for(final File f : filesToDelete){
               f.delete();
            }
         }

      }catch(final RuntimeException re){
         // rollback au besoin...
         if(filesCreated != null){
            for(final File f : filesCreated){
               f.delete();
            }
         }else{
            log.warn("Rollback création fichier n'a pas pu être réalisée");
         }
         throw (re);
      }
   }

   @Override
   public boolean findDoublonManager(final Prelevement prelevement){
      // Banque banque = prelevement.getBanque();
      final List<Prelevement> dbls = prelevementDao.findByCodeInPlateforme(prelevement.getCode(),
         prelevement.getBanque() != null ? prelevement.getBanque().getPlateforme() : null);
      //	.findByCodeOrNumLaboWithBanque(prelevement.getCode(), banque);

      if(!dbls.isEmpty()){
         if(prelevement.getPrelevementId() == null){
            return true;
         }
         for(final Prelevement d : dbls){
            if(!d.getPrelevementId().equals(prelevement.getPrelevementId())){
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public List<Prelevement> findAllObjectsManager(){
      log.debug("Recherche totalite des Prelevement");
      return prelevementDao.findAll();
   }

   @Override
   public List<Prelevement> findAfterDateConsentementManager(final Date date){
      if(date != null){
         log.debug(
            "Recherche des Prelevements consentis apres la date " + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
      }
      return prelevementDao.findByConsentDateAfterDate(date);
   }

   @Override
   public List<Prelevement> findAfterDatePrelevementManager(final Date date){
      Calendar cal = null;
      if(date != null){
         log.debug(
            "Recherche des Prelevements preleves apres la date " + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
         cal = Calendar.getInstance();
         cal.setTime(date);
      }
      return prelevementDao.findByDatePrelevementAfterDate(cal);
   }

   @Override
   public List<Prelevement> findAfterDatePrelevementWithBanqueManager(final Date date, final List<Banque> banques){
      Calendar cal = null;
      final List<Prelevement> res = new ArrayList<>();
      if(date != null && banques != null){
         log.debug(
            "Recherche des Prelevements preleves apres la date " + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
         cal = Calendar.getInstance();
         cal.setTime(date);
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            res.addAll(prelevementDao.findByDatePrelevementAfterDateWithBanque(cal, it.next()));
         }
      }
      return res;
   }

   @Override
   public List<Integer> findAfterDateCreationReturnIdsManager(final Calendar date, final List<Banque> banques){
      final List<Integer> liste = new ArrayList<>();
      if(date != null && banques != null){
         log.debug("Recherche des Prelevements enregistres apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            liste.addAll(findByOperationTypeAndDateReturnIds(operationTypeDao.findByNom("Creation").get(0), date, it.next()));
         }
      }
      return liste;
   }

   @Override
   public List<Prelevement> findAfterDateModificationManager(final Calendar date, final Banque banque){
      List<Prelevement> liste = new ArrayList<>();
      if(date != null && banque != null){
         log.debug("Recherche des Prelevements modifies apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         liste = findByOperationTypeAndDate(operationTypeDao.findByNom("Modification").get(0), date, banque);
      }
      return liste;
   }

   /**
    * Recherche un prélèvement dont l'identifiant est passé en paramètre.
    *
    * @param prelevementId
    *            Identifiant du prélèvement que l'on recherche.
    * @return Un Prelevement.
    */
   @Override
   public Prelevement findByIdManager(final Integer prelevementId){
      final Prelevement p = prelevementDao.findById(prelevementId);
      return p;
   }

   /**
    * Recupere la liste de prelevements en fonction du type d'operation et
    * d'une date a laquelle la date d'enregistrement de l'operation doit etre
    * superieure ou egale. Dans un premier temps, recupere la liste des
    * objetIds qui sont ensuite utilises pour recuperer les prelevements.
    *
    * @param oType
    *            OperationType
    * @param date
    * @param banque
    *            Banque à laquelle appartient le prlvt.
    * @return List de Prelevement
    */

   private List<Prelevement> findByOperationTypeAndDate(final OperationType oType, final Calendar date, final Banque banque){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Prelevement"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Prelevement> prelQuery;
      if(ids.size() > 1 && banque != null){ // HQL IN () si liste taille > 1
         prelQuery = em.createQuery(
            "SELECT DISTINCT p FROM Prelevement p " + "WHERE p.prelevementId IN (:ids) " + "AND p.banque = :banque",
            Prelevement.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banque", banque);
      }else if(ids.size() == 1 && banque != null){
         prelQuery =
            em.createQuery("SELECT DISTINCT p FROM Prelevement p " + "WHERE p.prelevementId = :id " + "AND p.banque = :banque",
               Prelevement.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banque", banque);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   /**
    * Recupere la liste d'Ids de prelevements en fonction du type d'operation
    * et d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale. Dans un premier temps, recupere la liste des
    * objetIds qui sont ensuite utilises pour recuperer les prelevements.
    *
    * @param oType
    *            OperationType
    * @param date
    * @param banque
    *            Banque à laquelle appartient le prlvt.
    * @return List de Prelevement
    */

   private List<Integer> findByOperationTypeAndDateReturnIds(final OperationType oType, final Calendar date, final Banque banque){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Prelevement"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Integer> prelQuery;
      if(ids.size() > 1 && banque != null){ // HQL IN () si liste taille > 1
         prelQuery = em.createQuery("SELECT DISTINCT p.prelevementId " + "FROM Prelevement p "
            + "WHERE p.prelevementId IN (:ids) " + "AND p.banque = :banque", Integer.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banque", banque);
      }else if(ids.size() == 1 && banque != null){
         prelQuery = em.createQuery("SELECT DISTINCT p.prelevementId " + "FROM Prelevement p " + "WHERE p.prelevementId = :id "
            + "AND p.banque = :banque", Integer.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banque", banque);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   @Override
   public List<Prelevement> findLastCreationManager(final List<Banque> banques, final int nbResults){

      final List<Prelevement> liste = new ArrayList<>();
      if(banques != null && banques.size() > 0 && nbResults > 0){

         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Prelevement> query =
            em.createQuery("SELECT p " + "FROM Prelevement p " + "WHERE p.banque in (:banque) " + "ORDER BY p.prelevementId DESC",
               Prelevement.class);
         query.setParameter("banque", banques);
         query.setFirstResult(0);
         query.setMaxResults(nbResults);

         liste.addAll(query.getResultList());

      }
      return liste;

   }

   @Override
   public List<Prelevement> findByCodeLikeManager(String code, final boolean exactMatch){
      if(!exactMatch){
         code = code + "%";
      }
      log.debug("Recherche Prelevement par code: " + code + " exactMatch " + String.valueOf(exactMatch));
      return prelevementDao.findByCode(code);
   }

   @Override
   public List<Prelevement> findByCodeOrNumLaboLikeWithBanqueManager(String code, final Banque banque, final boolean exactMatch){
      if(banque != null){
         if(!exactMatch){
            code = code + "%";
         }
         log.debug("Recherche Prelevement par code: " + code + " exactMatch " + String.valueOf(exactMatch));
         return prelevementDao.findByCodeOrNumLaboWithBanque(code, banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(String code, final List<Banque> banques,
      final boolean exactMatch){
      final List<Integer> res = new ArrayList<>();
      if(banques != null){
         log.debug("Recherche Prelevement par code: " + code + " exactMatch " + String.valueOf(exactMatch));
         if(!exactMatch){
            code = "%" + code + "%";
         }
         final Iterator<Banque> it = banques.iterator();
         while(it.hasNext()){
            res.addAll(prelevementDao.findByCodeOrNumLaboWithBanqueReturnIds(code, it.next()));
         }
      }

      return res;
   }

   /**
    * Recherche la maladie dont le prelevement est passé en paramètre.
    *
    * @param prelevement
    *            Prelevement pour lequel on recherche une maladie.
    * @return Une Maladie.
    */
   @Override
   public Maladie getMaladieManager(Prelevement prelevement){
      prelevement = prelevementDao.mergeObject(prelevement);
      Maladie parent = null;
      if(prelevement != null && prelevement.getMaladie() != null){
         parent = maladieDao.findById(prelevement.getMaladie().getMaladieId());
         parent = maladieDao.mergeObject(parent);
      }

      return parent;
   }

   @Override
   public List<Prelevement> findByMaladieLibelleLikeManager(String libelle, final boolean exactMatch){
      if(!exactMatch){
         libelle = libelle + "%";
      }
      log.debug("Recherche Prelevement par libelle de maladie: " + libelle + " exactMatch " + String.valueOf(exactMatch));
      return prelevementDao.findByMaladieLibelleLike(libelle);
   }

   @Override
   public List<Prelevement> findByNatureManager(final Nature nature){
      if(nature != null){
         log.debug("Recherche Prelevement par nature: " + nature.toString());
      }
      return prelevementDao.findByNature(nature);
   }

   @Override
   public List<Prelevement> findByTypeManager(final PrelevementType prelevementType){
      if(prelevementType != null){
         log.debug("Recherche Prelevement par prelevementType: " + prelevementType.toString());
      }
      return prelevementDao.findByPrelevementType(prelevementType);
   }

   @Override
   public List<Prelevement> findByConsentTypeManager(final ConsentType consentType){
      if(consentType != null){
         log.debug("Recherche Prelevement par consentType: " + consentType.toString());
      }
      return prelevementDao.findByConsentType(consentType);
   }

   @Override
   public Long findCountCreatedByCollaborateurManager(final Collaborateur colla){
      if(colla != null){
         return prelevementDao.findCountCreatedByCollaborateur(colla).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountByPreleveurManager(final Collaborateur colla){
      if(colla != null){
         return prelevementDao.findCountByPreleveur(colla).get(0);
      }
      return 0l;
   }

   @Override
   public Long findCountByServiceManager(final Service serv){
      if(serv != null){
         return prelevementDao.findCountByService(serv).get(0);
      }
      return 0l;
   }

   /**
    * Recherche la liste des codes utilisés par les prélèvements liés à la
    * banque passée en paramètre.
    *
    * @param banque
    *            Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   @Override
   public List<String> findAllCodesForBanqueManager(final Banque banque){
      return prelevementDao.findByBanqueSelectCode(banque);
   }

   @Override
   public List<String> findAllNdasForBanqueManager(final Banque banque){
      return prelevementDao.findByBanqueSelectNda(banque);
   }

   @Override
   public List<Prelevement> findByIdsInListManager(final List<Integer> ids){
      final List<Prelevement> prels = new ArrayList<>();

      if(ids != null && ids.size() > 0){
         // 2.0.10.5
         // Oracle split list to prevent ORA-01795 not more than 1000 elements
         // are allowed for 'in' subquery
         final List<List<Integer>> chunks = Utils.split(ids, 1000);

         for(final List<Integer> chks : chunks){
            prels.addAll(prelevementDao.findByIdInList(chks));
         }
      }
      return prels;
   }

   /**
    * Recherche une liste d'échantillons dont le prélèvement est passé en
    * paramètre.
    *
    * @param prelevement
    *            Prelevement pour lequel on recherche des échantillons.
    * @return List de Echantillon.
    */
   @Override
   public Set<Echantillon> getEchantillonsManager(Prelevement prelevement){
      Set<Echantillon> echans = new HashSet<>();

      if(prelevement != null){
         prelevement = prelevementDao.mergeObject(prelevement);
         echans = prelevement.getEchantillons();
         echans.size();
         return echans;
      }
      return new HashSet<>();
   }

   /**
    * Recherche une liste de labo inters dont le prélèvement est passé en
    * paramètre.
    *
    * @param prelevement
    *            Prelevement pour lequel on recherche des labos.
    * @return Set de LaboInter.
    */
   @Override
   public Set<LaboInter> getLaboIntersManager(Prelevement prelevement){
      Set<LaboInter> labos = new HashSet<>();

      if(prelevement != null){
         prelevement = prelevementDao.mergeObject(prelevement);
         labos = prelevement.getLaboInters();
         labos.size();
         return labos;
      }
      return new HashSet<>();
   }

   /**
    * Recherche une liste de labo inters dont le prélèvement est passé en
    * paramètre. Ces labos sont ordonnés par ordre.
    *
    * @param prelevement
    *            Prelevement pour lequel on recherche des labos.
    * @return Liste ordonnée de LaboInter.
    */
   @Override
   public List<LaboInter> getLaboIntersWithOrderManager(final Prelevement prelevement){
      if(prelevement != null){
         return laboInterManager.findByPrelevementWithOrder(prelevement);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche une liste de dérivés dont le prélèvement est passé en
    * paramètre.
    *
    * @param prelevement
    *            Prelevement pour lequel on recherche des dérivés.
    * @return List de ProdDerive.
    */
   @Override
   public List<ProdDerive> getProdDerivesManager(Prelevement prelevement){

      if(prelevement != null){
         prelevement = prelevementDao.mergeObject(prelevement);
         final List<Transformation> list = transformationManager.findByParentManager(prelevement);
         final List<ProdDerive> derives = new ArrayList<>();

         for(int i = 0; i < list.size(); ++i){
            final Transformation transfo = transformationDao.mergeObject(list.get(i));
            final Set<ProdDerive> tmp = transfo.getProdDerives();
            final Iterator<ProdDerive> it = tmp.iterator();
            while(it.hasNext()){
               final ProdDerive current = it.next();
               if(!derives.contains(current)){
                  derives.add(current);
               }
            }
         }

         return derives;
      }
      return new ArrayList<>();
   }

   //   public void setPrelevementDelegateDao(PrelevementDelegateDao prelevementDelegateDao){
   //      this.prelevementDelegateDao = prelevementDelegateDao;
   //   }

   @Override
   public void removeObjectManager(final Prelevement prelevement, final String comments, final Utilisateur u,
      final List<File> filesToDelete){
      if(prelevement != null){
         if(!isUsedObjectManager(prelevement)){
            // Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(prelevement, operationManager, comments, u);

            // Supprime importations associes
            CreateOrUpdateUtilities.removeAssociateImportations(prelevement, importHistoriqueManager);

            // Supprime annotations associes
            annotationValeurManager.removeAnnotationValeurListManager(annotationValeurManager.findByObjectManager(prelevement),
               filesToDelete);

            // Supprime non conformites associees
            CreateOrUpdateUtilities.removeAssociateNonConformites(prelevement, objetNonConformeManager);

            prelevementDao.removeObject(prelevement.getPrelevementId());
            log.info("Suppression objet Prelevement " + prelevement.toString());
         }else{
            log.warn("Suppression Prelevement " + prelevement.toString() + " impossible car Objet est reference "
               + "(par echantillon/derive)");
            throw new ObjectUsedException("prelevement.deletion.isUsedCascade", true);
         }
      }else{
         log.warn("Suppression d'un Prelevement null");
      }
   }

   @Override
   public void removeObjectCascadeManager(final Prelevement prelevement, final String comments, final Utilisateur user,
      final List<File> filesToDelete){
      if(prelevement != null){
         log.info("Suppression en cascade depuis objet Prelevement " + prelevement.toString());

         // suppression echantillon en mode cascade
         final Iterator<Echantillon> echansIt = getEchantillonsManager(prelevement).iterator();

         while(echansIt.hasNext()){
            echantillonManager.removeObjectCascadeManager(echansIt.next(), comments, user, filesToDelete);
         }
         prelevement.setEchantillons(new HashSet<Echantillon>());

         // suppression des dérivés en mode cascade
         final Iterator<Transformation> transfIt = transformationManager.findByParentManager(prelevement).iterator();
         while(transfIt.hasNext()){
            prodDeriveManager.removeObjectCascadeManager(transfIt.next(), comments, user, filesToDelete);
         }

         removeObjectManager(prelevement, comments, user, filesToDelete);
      }
   }

   @Override
   public boolean isUsedObjectManager(final Prelevement prelevement){
      final Prelevement prel = prelevementDao.mergeObject(prelevement);
      // References vers echantillons, derives?

      return prel.getEchantillons().size() > 0 || transformationManager.findByParentManager(prelevement).size() > 0;
   }

   @Override
   public void checkRequiredObjectsAndValidate(final Prelevement prelevement, final Banque banque, final Nature nature,
      final ConsentType consentType, final Maladie maladie, final List<LaboInter> laboInters, final String operation,
      final Utilisateur utilisateur, final boolean doValidation, final String baseDir){
      // Banque required
      if(banque != null){
         prelevement.setBanque(banque);
      }else if(prelevement.getBanque() == null){
         log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " d'un Prelevement");
         throw new RequiredObjectIsNullException("Prelevement", operation, "Banque");
      }

      // Gatsbi required
      final List<Integer> requiredChampEntiteId = new ArrayList<>();
      if(prelevement.getBanque().getEtude() != null){
         final Contexte prelContexte = prelevement.getBanque().getEtude().getContexteForEntite(2);
         if(prelContexte != null){
            requiredChampEntiteId.addAll(prelContexte.getRequiredChampEntiteIds());
         }
      }

      if(nature != null){
         prelevement.setNature(natureDao.mergeObject(nature));
      }else{ // valeur passée est nulle
         if(prelevement.getBanque().getEtude() == null || requiredChampEntiteId.contains(24)){ // obligatoire! 
            if(prelevement.getNature() == null){
               log.warn("Objet obligatoire Nature manquant" + " lors de la " + operation + " d'un Prelevement");
               throw new RequiredObjectIsNullException("Prelevement", operation, "Nature");
            }
         }else{ // gastbi contexte non obligatoire
            prelevement.setNature(null);
         }
      }

      if(consentType != null){
         prelevement.setConsentType(consentTypeDao.mergeObject(consentType));
      }else{ // valeur passée est nulle
         if(prelevement.getBanque().getEtude() == null || requiredChampEntiteId.contains(26)){ // obligatoire!
            if(prelevement.getConsentType() == null){
               log.warn("Objet obligatoire ConsentType manquant" + " lors de la " + operation + " d'un Prelevement");
               throw new RequiredObjectIsNullException("Prelevement", operation, "ConsentType");
            }
         }else{ // gastbi contexte non obligatoire
            prelevement.setConsentType(null);
         }
      }

      // Maladie non required mais utilise dans validation
      if(maladie != null){
         
         // @since gatsbi list maladies may be provided
         List<Maladie> visites =  new ArrayList<Maladie>();
         
         // creation patient
         if(maladie.getPatient().getPatientId() == null){               
            // les visites sont toutes à créer en même temps que le patient
            visites.addAll(maladie.getPatient().getMaladies()); 
            patientManager.createOrUpdateObjectManager(maladie.getPatient(), 
               visites.isEmpty() ? null : visites, 
               null, null, null, null, null, null,
               utilisateur, "creation", baseDir, false);              
         } else if (maladie.getPatient().isNewIdentifiantAdded()) { // update patient existant, ajout gatsbi
            visites.addAll(maladie.getPatient().getMaladies());
            patientManager.createOrUpdateObjectManager(maladie.getPatient(), null,
              // visites.isEmpty() ? null : visites, null 
               null, null, null, null, null, null,
               utilisateur, "modification", baseDir, false);
         }
         
         if(maladie.getMaladieId() == null){ // creation maladie conjointe
                  
            // @since gatsbi, creation de la visite si n'a pas été créé auparavant 
            // dans la liste de visites
            if (visites.isEmpty() || maladie.getPatient().getMaladies().stream()
                  .noneMatch(v -> v.getLibelle().equals(maladie.getLibelle()))) { 
               maladieManager.createOrUpdateObjectManager(maladie, null, null, utilisateur, "creation");
               maladieManager.getMaladiesManager(maladie.getPatient()).add(maladie);
               prelevement.setMaladie(maladie);
            } else { // la maladie a été créée comme une visite
               prelevement.setMaladie(maladieManager.findVisitesManager(maladie.getPatient(), banque).stream()
                  .filter(v -> v.getLibelle().equals(maladie.getLibelle())).findFirst().get());
            }

         } else { // maladie existante
            prelevement.setMaladie(maladie);
         }
      }
      if(laboInters != null){
         prelevement.setLaboInters(new HashSet<>(laboInters));
         // prelevement.setLaboInters(new HashSet<LaboInter>());
         for(int i = 0; i < laboInters.size(); i++){
            final LaboInter labo = laboInters.get(i);
            labo.setPrelevement(prelevement);
            BeanValidator.validateObject(labo, new Validator[] {laboInterValidator});
            // prelevement.getLaboInters().add(labo);
         }
         prelevement.setLaboInters(new HashSet<LaboInter>());
      }

      // Validation
      if(doValidation){
         
         // retire validation obligatoire sur nda
         if (prelevement.getMaladie() == null) {
            requiredChampEntiteId.removeIf(i -> i.equals(44));
         }
         
         Validator[] validators;
         if(requiredChampEntiteId.isEmpty()){ // pas de restriction gatsbi
            validators = new Validator[] {prelevementValidator};
         }else{ // gatsbi définit certain champs obligatoires
            final PrelevementGatsbiValidator gValidator = 
               new PrelevementGatsbiValidator("prelevement", requiredChampEntiteId);
            validators = new Validator[] {gValidator, prelevementValidator};
         }

         BeanValidator.validateObject(prelevement, validators);
      }
   }

   /**
    * Merge et assigne tous les objects associes non obligatoires au
    * prelevement (sauf maladie car utilisé dans validation).
    *
    * @param prelevement
    * @param preleveur
    * @param servicePreleveur
    * @param prelevementType
    * @param conditType
    * @param conditMilieu
    * @param transporteur
    * @param operateur
    * @param quantiteUnite
    */
   private void mergeNonRequiredObjects(final Prelevement prelevement, /*final Maladie maladie, */ final Collaborateur preleveur,
      final Service servicePreleveur, final PrelevementType prelevementType, final ConditType conditType,
      final ConditMilieu conditMilieu, final Transporteur transporteur, final Collaborateur operateur, final Unite quantiteUnite){

      prelevement.setPrelevementType(prelevementTypeDao.mergeObject(prelevementType));
      prelevement.setPreleveur(collaborateurDao.mergeObject(preleveur));
      prelevement.setServicePreleveur(serviceDao.mergeObject(servicePreleveur));
      prelevement.setConditMilieu(conditMilieuDao.mergeObject(conditMilieu));
      prelevement.setConditType(conditTypeDao.mergeObject(conditType));
      prelevement.setTransporteur(transporteurDao.mergeObject(transporteur));
      prelevement.setOperateur(collaborateurDao.mergeObject(operateur));
      prelevement.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));
   }

   @Override
   public List<Prelevement> findAllPrelevementsManager(final Patient patient){
      final List<Prelevement> prels = new ArrayList<>();
      if(patient.getPatientId() != null){
         // List<Maladie> maladies =
         // new ArrayList<Maladie>(maladieManager
         // .getMaladiesManager(patient));
         // for (int i = 0; i < maladies.size(); i++) {
         // prels.addAll(maladieDao
         // .mergeObject(maladies.get(i)).getPrelevements());
         // }
         prels.addAll(prelevementDao.findByPatient(patient));
      }

      return prels;
   }

   @Override
   public List<LaboInter> cascadeNonSterileManager(final Prelevement prelevement, final List<LaboInter> labos,
      final Integer apresOrdre, final boolean cascadeEchans){

      // laboInter steriles
      // List<LaboInter> labos =
      // new ArrayList<LaboInter>(prelevement.getLaboInters());

      if(labos != null && apresOrdre != null){
         for(int i = 0; i < labos.size(); i++){
            if(labos.get(i).getOrdre() >= apresOrdre){
               labos.get(i).setSterile(false);
            }
         }
      }

      if(cascadeEchans){
         // prelevement.setLaboInters(null);
         // Echantillons
         final List<Echantillon> echans = new ArrayList<>(getEchantillonsManager(prelevement));
         for(int j = 0; j < echans.size(); j++){
            echans.get(j).setSterile(false);
            echantillonDao.updateObject(echans.get(j));
         }
         // if (labos != null) {
         // prelevement.setLaboInters(new HashSet<LaboInter>(labos));
         // }
      }
      return labos;
   }

   @Override
   public List<Prelevement> findByMaladieAndBanqueManager(final Maladie mal, final Banque bank){
      return prelevementDao.findByMaladieAndBanque(mal, bank);
   }

   @Override
   public List<Prelevement> findByMaladieAndOtherBanquesManager(final Maladie mal, final Banque bank){
      return prelevementDao.findByMaladieAndOtherBanques(mal, bank);
   }

   @Override
   public List<Prelevement> findByBanquesManager(final List<Banque> banks){
      List<Prelevement> prels = new ArrayList<>();
      if(banks != null && !banks.isEmpty()){
         prels = prelevementDao.findByBanques(banks);
      }
      return prels;
   }

   @Override
   public List<Integer> findAllObjectsIdsByBanquesManager(final List<Banque> banques){
      if(banques != null && banques.size() > 0){
         return prelevementDao.findByBanquesAllIds(banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Prelevement> findByPatientManager(final Patient pat){
      return prelevementDao.findByPatient(pat);
   }

   @Override
   public List<Integer> findByPatientNomReturnIdsManager(String nom, final List<Banque> banks, final boolean exactMatch){
      final List<Integer> res = new ArrayList<>();
      if(nom != null && banks != null){
         if(!exactMatch){
            nom = "%" + nom + "%";
         }
         for(int i = 0; i < banks.size(); i++){
            res.addAll(prelevementDao.findByPatientNomReturnIds(nom, banks.get(i)));
         }
      }

      return res;
   }

   @Override
   public List<Integer> findByCodeOrNumLaboInListManager(final List<String> criteres, final List<Banque> banques){
      if(criteres != null && banques != null && criteres.size() > 0 && banques.size() > 0){
         return prelevementDao.findByCodeOrNumLaboInListWithBanque(criteres, banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByPatientNomOrNipInListManager(final List<String> criteres, final List<Banque> banks){
      if(criteres != null && banks != null && criteres.size() > 0 && banks.size() > 0){
         return prelevementDao.findByPatientNomOrNipInList(criteres, banks);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Prelevement> findByNdaLikeManager(final String nda){
      return prelevementDao.findByNdaLike(nda);
   }

   @Override
   public void createPrelAndEchansManager(final Prelevement prelevement, final List<AnnotationValeur> annosPrel,
      final List<Echantillon> echantillons, final List<AnnotationValeur> annosEchan, final Banque banque, final Utilisateur user,
      final boolean doValidation, final String baseDir){

      // revert maladie et patient au besoin
      boolean revertMaladie = false;
      boolean revertPatient = false;
      if(prelevement.getMaladie() != null){
         revertMaladie = prelevement.getMaladie().getMaladieId() == null;
         if(revertMaladie){
            revertPatient = prelevement.getMaladie().getPatient().getPatientId() == null;
         }
      }

      final List<File> filesCreated = new ArrayList<>();

      // enregistrement du prelevement
      createObjectManager(prelevement, banque, prelevement.getNature(), prelevement.getMaladie(), prelevement.getConsentType(),
         prelevement.getPreleveur(), prelevement.getServicePreleveur(), prelevement.getPrelevementType(),
         prelevement.getConditType(), prelevement.getConditMilieu(), prelevement.getTransporteur(), prelevement.getOperateur(),
         prelevement.getQuantiteUnite(), new ArrayList<>(prelevement.getLaboInters()), annosPrel, filesCreated, user,
         doValidation, baseDir, false);

      try{

         // enregistrement des echantillons
         for(int i = 0; i < echantillons.size(); i++){
            final Echantillon newEchan = echantillons.get(i);

            // création de l'objet
            echantillonManager.createObjectWithCrAnapathManager(newEchan, banque, prelevement, newEchan.getCollaborateur(),
               newEchan.getObjetStatut(), newEchan.getEmplacement(), newEchan.getEchantillonType(), null,
               newEchan.getQuantiteUnite(), newEchan.getEchanQualite(), newEchan.getModePrepa(), newEchan.getCrAnapath(),
               newEchan.getAnapathStream(), filesCreated, annosEchan, user, doValidation, baseDir, false);
         }
      }catch(final RuntimeException re){
         if(revertMaladie){
            prelevement.getMaladie().setMaladieId(null);
            if(revertPatient){
               prelevement.getMaladie().getPatient().setPatientId(null);
            }
         }

         prelevement.setPrelevementId(null);
         // revert Objects
         final Iterator<LaboInter> it = prelevement.getLaboInters().iterator();
         while(it.hasNext()){
            it.next().setLaboInterId(null);
         }
         final Iterator<Echantillon> itE = echantillons.iterator();
         Echantillon e;
         while(itE.hasNext()){
            e = itE.next();
            if(e.getEchantillonId() != null){
               e.setEchantillonId(null);
               if(e.getCrAnapath() != null){
                  e.getCrAnapath().setFichierId(null);
               }
            }else{ // la boucle arrive a l'echantillon planté.
               break;
            }
         }

         // rollback au besoin...
         for(final File f : filesCreated){
            f.delete();
         }

         throw re;
      }
   }

   @Override
   public Set<Risque> getRisquesManager(Prelevement prelevement){
      Set<Risque> risques = new HashSet<>();

      if(prelevement != null){
         prelevement = prelevementDao.mergeObject(prelevement);
         risques = prelevement.getRisques();
         risques.size();
      }
      return risques;
   }

   @Override
   public List<TKAnnotableObject> getPrelevementChildrenManager(final Prelevement p){
      final List<TKAnnotableObject> children = new ArrayList<>();
      final Iterator<Echantillon> echansIt = getEchantillonsManager(p).iterator();
      Echantillon echan;
      while(echansIt.hasNext()){
         echan = echansIt.next();
         children.add(echan);
         children.addAll(prodDeriveManager.findByParentManager(echan, true));
      }
      children.addAll(prodDeriveManager.findByParentManager(p, true));
      return children;
   }

   @Override
   public boolean hasCessedObjectManager(final Prelevement prel){
      final Iterator<TKAnnotableObject> childrenIt = getPrelevementChildrenManager(prel).iterator();
      while(childrenIt.hasNext()){
         if(!cederObjetManager.findByObjetManager(childrenIt.next()).isEmpty()){
            return true;
         }
      }
      return false;
   }

   @Override
   public void switchBanqueMultiplePrelevementManager(final Prelevement[] prlvts, final Banque bank, final boolean doValidation,
      final Utilisateur u){

      final List<File> filesToDelete = new ArrayList<>();
      final Set<MvFichier> dpcts = new HashSet<>();

      if(prlvts != null){
         for(final Prelevement p : prlvts){
            switchBanqueCascadeManager(p, bank, doValidation, u, filesToDelete, dpcts);
         }
      }

      // dpcts
      // Correctif bug TK-155
      MvFichier mvFichier = null;
      try{
         for(final MvFichier mv : dpcts){
            mvFichier = mv;
            mv.move();
         }
      }catch(final IOException ioe){ // problème survenu lors du déplacement
         log.error("un problème est survenu dans un déplacement de fichier: " + mvFichier.toString());

         log.error(ioe);

         // rollback
         try{
            for(final MvFichier mv : dpcts){
               mv.revert();
            }
         }catch(final IOException ioe2){ // problème survenu lors du rollback du déplacement
            log.error("un problème est survenu dans un rollabck de déplacement de fichier: " + mvFichier.toString());

            log.error(ioe2);
         }
         throw new RuntimeException("switch.banque.filesystem.error");
      }

      //      if(filesToDelete != null){
      for(final File f : filesToDelete){
         f.delete();
      }
      //      }
   }

   @Override
   public void switchBanqueCascadeManager(Prelevement prel, final Banque bank, final boolean doValidation, final Utilisateur u,
      final List<File> filesToDelete, final Set<MvFichier> filesToMove){
      if(bank != null && prel != null && !bank.equals(prel.getBanque())){

         final Iterator<Echantillon> echansIt = getEchantillonsManager(prel).iterator();

         while(echansIt.hasNext()){
            echantillonManager.switchBanqueCascadeManager(echansIt.next(), bank, doValidation, u, filesToDelete, filesToMove);
         }

         final Iterator<ProdDerive> derivesIt = getProdDerivesManager(prel).iterator();
         while(derivesIt.hasNext()){
            prodDeriveManager.switchBanqueCascadeManager(derivesIt.next(), bank, doValidation, u, filesToDelete, filesToMove);
         }

         //Suppression du délégué si la banque de destination n'est pas dans le même contexte que la banque d'origine
         if(!bank.getContexte().equals(prel.getBanque().getContexte())){
            prel.setDelegate(null);
         }

         prel.setBanque(bank);

         //Si le délégué présente des informations de validation, on 

         if(doValidation && findDoublonManager(prel)){
            log.warn("Doublon lors creation objet Prelevement " + prel.toString());
            throw new DoublonFoundException("Prelevement", "switchBanque", prel.getCode(), null);
         }
         prel = prelevementDao.mergeObject(prel);

         // annotations
         annotationValeurManager.switchBanqueManager(prel, bank, filesToDelete, filesToMove);
         // if (prel.getMaladie() != null) {
         // annotationValeurManager
         // .switchBanqueManager(prel.getMaladie().getPatient(), bank);
         // }

         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, u, operationTypeDao.findByNom("ChangeCollection").get(0), prel);
      }
   }

   @Override
   public Calendar getDateCongelationManager(final Prelevement prel){
      if(prel != null){
         if(prel.getCongDepart() != null && prel.getCongDepart()){
            return prel.getDateDepart();
         }else if(prel.getCongArrivee() != null && prel.getCongArrivee()){
            return prel.getDateArrivee();
         }else{
            Iterator<LaboInter> labosIt;
            if(prel.getLaboInters() instanceof PersistentSet){
               labosIt = getLaboIntersManager(prel).iterator();
            }else{
               labosIt = prel.getLaboInters().iterator();
            }
            LaboInter next;
            while(labosIt.hasNext()){
               next = labosIt.next();
               if(next.getCongelation() != null && next.getCongelation()){
                  return next.getDateDepart();
               }
            }
         }
      }
      return null;
   }

   @Override
   public void updateMultipleObjectsManager(final List<Prelevement> prelevements, final List<Prelevement> basePrelevements,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final List<NonConformite> ncf, final boolean cascadeNonSterile, final Utilisateur utilisateur, final String baseDir){

      Integer nosterile = null;
      if(cascadeNonSterile){
         nosterile = 0;
      }

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      for(int i = 0; i < prelevements.size(); i++){
         final Prelevement prel = prelevements.get(i);
         try{
            updateObjectManager(prel, prel.getBanque(), prel.getNature(), null, prel.getConsentType(), prel.getPreleveur(), prel.getServicePreleveur(),
               prel.getPrelevementType(), prel.getConditType(), prel.getConditMilieu(), prel.getTransporteur(),
               prel.getOperateur(), prel.getQuantiteUnite(), null, null, null, filesCreated, filesToDelete, utilisateur,
               nosterile, true, baseDir, true);

            // enregistrement de la conformité
            objetNonConformeManager.createUpdateOrRemoveListObjectManager(prel, ncf, "Arrivee");
         }catch(final RuntimeException e){
            if(e instanceof TKException){
               ((TKException) e).setEntiteObjetException("Prelevement");
               ((TKException) e).setIdentificationObjetException(prel.getCode());
            }
            throw e;
         }
      }

      try{
         // suppr les annotations pour tous les prelevements
         annotationValeurManager.removeAnnotationValeurListManager(listAnnoToDelete, filesToDelete);

         if(listAnnoToCreateOrUpdate != null){
            // traite en premier et retire les annotations
            // création de fichiers pour
            // enregistrement en batch
            final List<AnnotationValeur> fileVals = new ArrayList<>();
            for(final AnnotationValeur val : listAnnoToCreateOrUpdate){
               if(val.getFichier() != null && val.getStream() != null){
                  annotationValeurManager.createFileBatchForTKObjectsManager(basePrelevements, val.getFichier(), val.getStream(),
                     val.getChampAnnotation(), val.getBanque(), utilisateur, baseDir, filesCreated);
                  fileVals.add(val);
               }
            }
            listAnnoToCreateOrUpdate.removeAll(fileVals);

            // update les annotations, null operation pour
            // laisser la possibilité création/modification au sein
            // de la liste
            annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, null, utilisateur, null, baseDir,
               filesCreated, filesToDelete);
         }

         //         if(filesToDelete != null){
         for(final File f : filesToDelete){
            f.delete();
         }
         //         }
      }catch(final RuntimeException e){
         //         if(filesCreated != null){
         for(final File f : filesCreated){
            f.delete();
         }
         //         }
      }
   }

   @Override
   public void switchMaladieManager(Prelevement prel, final Maladie maladie, final Utilisateur usr){

      if(maladie != null && prel != null && usr != null && !maladie.equals(prel.getMaladie())){

         prel.setMaladie(maladie);
         prel = prelevementDao.mergeObject(prel);

         final Operation creationOp = new Operation();
         creationOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(creationOp, usr, operationTypeDao.findByNom("Modification").get(0), prel);
      }
   }

   @Override
   public List<Prelevement> findByDossierExternesManager(final List<Banque> banques, final List<Emetteur> emetteurs){
      final List<Prelevement> prlvts = new ArrayList<>();

      if(banques != null && banques.size() > 0 && emetteurs != null && emetteurs.size() > 0){
         final List<String> codes = dossierExterneDao.findByEmetteurInListSelectIdentification(emetteurs);

         if(codes.size() > 0){
            // 2.1.0
            // Oracle split list to prevent ORA-01795 not more than 1000 elements
            // are allowed for 'in' subquery
            final List<List<String>> chunks = Utils.split(codes, 1000);

            for(final List<String> chks : chunks){
               prlvts.addAll(prelevementDao.findByCodesAndBanquesInList(chks, banques));
            }
            // prlvts = prelevementDao.findByCodesAndBanquesInList(codes,
            //		banques);
         }
      }

      return prlvts;
   }

   @Override
   public void createObjectWithNonConformitesManager(final Prelevement prelevement, final Banque banque, final Nature nature,
      final Maladie maladie, final ConsentType consentType, final Collaborateur preleveur, final Service servicePreleveur,
      final PrelevementType prelevementType, final ConditType conditType, final ConditMilieu conditMilieu,
      final Transporteur transporteur, final Collaborateur operateur, final Unite quantiteUnite, final List<LaboInter> laboInters,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final Utilisateur utilisateur, final boolean doValidation,
      final String baseDir, final boolean isImport, final List<NonConformite> noconfs){

      if(noconfs != null && !noconfs.isEmpty()){
         prelevement.setConformeArrivee(false);
      }

      final List<File> filesCreated = new ArrayList<>();

      try{
         createObjectManager(prelevement, banque, nature, maladie, consentType, preleveur, servicePreleveur, prelevementType,
            conditType, conditMilieu, transporteur, operateur, quantiteUnite, laboInters, listAnnoToCreateOrUpdate, filesCreated,
            utilisateur, doValidation, baseDir, isImport);

         objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevement, noconfs, "Arrivee");
      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   @Override
   public void updateObjectWithNonConformitesManager(final Prelevement prelevement, final Banque banque, final Nature nature,
      final Maladie maladie, final ConsentType consentType, final Collaborateur preleveur, final Service servicePreleveur,
      final PrelevementType prelevementType, final ConditType conditType, final ConditMilieu conditMilieu,
      final Transporteur transporteur, final Collaborateur operateur, final Unite quantiteUnite, final List<LaboInter> laboInters,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final Utilisateur utilisateur, final Integer cascadeNonSterile, final boolean doValidation, final String baseDir,
      final boolean multiple, final List<NonConformite> noconfs){

      if(noconfs != null && !noconfs.isEmpty()){
         prelevement.setConformeArrivee(false);
      }

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      try{
         updateObjectManager(prelevement, banque, nature, maladie, consentType, preleveur, servicePreleveur, prelevementType,
            conditType, conditMilieu, transporteur, operateur, quantiteUnite, laboInters, listAnnoToCreateOrUpdate,
            listAnnoToDelete, filesCreated, filesToDelete, utilisateur, cascadeNonSterile, doValidation, baseDir, multiple);

         objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevement, noconfs, "Arrivee");

         for(final File f : filesToDelete){
            f.delete();
         }
      }catch(final RuntimeException e){

         for(final File f : filesCreated){
            f.delete();
         }

         throw e;
      }
   }

   @Override
   public void removeListFromIdsManager(final List<Integer> ids, final String comment, final Utilisateur u){
      if(ids != null){
         final List<File> filesToDelete = new ArrayList<>();
         Prelevement p;
         for(final Integer id : ids){
            p = findByIdManager(id);
            if(p != null){
               removeObjectCascadeManager(p, comment, u, filesToDelete);
            }
         }
         //         if(filesToDelete != null){
         for(final File f : filesToDelete){
            f.delete();
         }
         //         }
      }
   }

   @Override
   public List<Prelevement> findByPatientAndAuthorisationsManager(final Patient pat, final Plateforme pf,
      final Utilisateur utilisateur){

      if(utilisateur != null && pf != null){
         Set<Banque> banks;
         // banques consultables
         if(!utilisateur.isSuperAdmin()){
            banks = new HashSet<>(
               banqueDao.findByEntiteConsultByUtilisateur(utilisateur, entiteDao.findByNom("Prelevement").get(0), pf));
            banks.addAll(banqueDao.findByUtilisateurIsAdmin(utilisateur, pf));

            final Set<Banque> crossBanks = new HashSet<>(banqueDao.findByAutoriseCrossPatient(true));
            banks.addAll(crossBanks);
         }else{
            // @since 2.1 all banks
            banks = new HashSet<>(banqueDao.findByPlateformeAndArchive(pf, false));
            banks.addAll(banqueDao.findByPlateformeAndArchive(pf, true));
         }

         if(!banks.isEmpty()){
            return prelevementDao.findByPatientAndBanques(pat, new ArrayList<>(banks));
         }
      }
      return new ArrayList<>();
   }

   @Override
   public List<Prelevement> findByCodeInPlateformeManager(final String code, final Plateforme pf){
      return prelevementDao.findByCodeInPlateforme(code, pf);
   }

   @Override
   public List<Integer> findByPatientIdentifiantOrNomOrNipInListManager(List<String> idsNipsNoms, List<Banque> selectedBanques){
      if(idsNipsNoms != null && !idsNipsNoms.isEmpty() && selectedBanques != null && !selectedBanques.isEmpty()){
         return prelevementDao.findByPatientIdentifiantOrNomOrNipInList(idsNipsNoms, selectedBanques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByPatientIdentifiantOrNomOrNipReturnIdsManager(String search, List<Banque> banks, boolean exactMatch){
      final List<Integer> res = new ArrayList<>();
      if(!StringUtils.isEmpty(search) && banks != null && !banks.isEmpty()){
         if(!exactMatch){
            search = "%" + search + "%";
         }
         return prelevementDao.findByPatientIdentifiantOrNomOrNipReturnIds(search, banks);
      }

      return res;   
   }
}
