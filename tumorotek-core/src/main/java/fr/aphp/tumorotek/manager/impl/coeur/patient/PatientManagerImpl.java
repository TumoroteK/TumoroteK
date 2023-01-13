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
package fr.aphp.tumorotek.manager.impl.coeur.patient;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientLienDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientMedecinDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientValidator;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.patient.gatsbi.PatientGatsbiValidator;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.patient.PatientLienPK;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecinPK;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Patient.
 * Classe créée le 30/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class PatientManagerImpl implements PatientManager
{

   private final Log log = LogFactory.getLog(PatientManager.class);

   /* Beans injectes par Spring*/
   private PatientDao patientDao;

   private MaladieDao maladieDao;

   private MaladieManager maladieManager;

   private PatientMedecinDao patientMedecinDao;

   private PatientLienDao patientLienDao;

   private PatientValidator patientValidator;

   private OperationTypeDao operationTypeDao;

   private OperationManager operationManager;

   private EntityManagerFactory entityManagerFactory;

   private EntiteDao entiteDao;

   private PrelevementDao prelevementDao;

   private AnnotationValeurManager annotationValeurManager;

   private ImportHistoriqueManager importHistoriqueManager;
   // private PatientDelegateDao patientDelegateDao;

   public PatientManagerImpl(){}

   /* Properties setters */
   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   public void setMaladieManager(final MaladieManager mManager){
      this.maladieManager = mManager;
   }

   public void setPatientMedecinDao(final PatientMedecinDao pmDao){
      this.patientMedecinDao = pmDao;
   }

   public void setPatientLienDao(final PatientLienDao plDao){
      this.patientLienDao = plDao;
   }

   public void setPatientValidator(final PatientValidator pValidator){
      this.patientValidator = pValidator;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setEntityManagerFactory(final EntityManagerFactory emf){
      this.entityManagerFactory = emf;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setPrelevementDao(final PrelevementDao prelDao){
      this.prelevementDao = prelDao;
   }

   public void setAnnotationValeurManager(final AnnotationValeurManager aManager){
      this.annotationValeurManager = aManager;
   }

   public void setImportHistoriqueManager(final ImportHistoriqueManager iManager){
      this.importHistoriqueManager = iManager;
   }

   @Override
   public void createOrUpdateObjectManager(final Patient patient, final List<Maladie> maladies,
      final List<Collaborateur> medecins, final List<PatientLien> patientLiens,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final List<File> filesCreated, final List<File> filesToDelete, final Utilisateur utilisateur, final String operation,
      final String baseDir, final boolean isImport){

      if(patient != null){
         if(operation == null){
            throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
         }
         
         // @since 2.3.0-gatsbi 
         final List<Integer> requiredChampEntiteId = new ArrayList<>();
         if(patient.getBanque() != null && patient.getBanque().getEtude() != null){ // creation / edition en contexte Gatsbi
            final Contexte patContexte = patient.getBanque().getEtude().getContexteForEntite(1);
            if(patContexte != null){
               requiredChampEntiteId.addAll(patContexte.getRequiredChampEntiteIds());
            }
         }
         Validator[] validators;
         if(requiredChampEntiteId.isEmpty()){ // pas de restriction gatsbi
            validators = new Validator[] {patientValidator};
         }else{ // gatsbi définit certain champs obligatoires
            final PatientGatsbiValidator gValidator = 
               new PatientGatsbiValidator("patient", requiredChampEntiteId);
            validators = new Validator[] {gValidator, patientValidator};
         }
         
         //Validation
         BeanValidator.validateObject(patient, validators);
         
         //Doublon
         Optional<PatientDoublonFound> optDbf = findDoublonManager(patient);
         if(!operation.equals("fusion") && optDbf.isPresent()){
            log.warn("Doublon lors " + operation + " objet Patient " + patient.toString());
            throw new DoublonFoundException("Patient", operation, optDbf.get());
         }
         if((operation.equals("creation") || operation.equals("modification")) || operation.equals("modifMulti")
            || operation.equals("synchronisation") || operation.equals("fusion") || operation.equals("validation")
            || operation.equals("invalidation")){

            OperationType oType;

            if(operation.equals("creation")){
               patientDao.createObject(patient);
               log.info("Enregistrement objet Patient " + patient.toString());

               oType = operationTypeDao.findByNom("Creation").get(0);
            }else{

               // patient.setDelegate(patientDelegateDao.mergeObject(patient.getDelegate()));

               patientDao.updateObject(patient);
               log.info("Modification objet Patient " + patient.toString());

               if(operation.equals("modification")){
                  oType = operationTypeDao.findByNom("Modification").get(0);
               }else if(operation.equals("synchronisation")){
                  oType = operationTypeDao.findByNom("Synchronisation").get(0);
               }else if(operation.equals("modifMulti")){
                  oType = operationTypeDao.findByNom("ModifMultiple").get(0);
               }else if(operation.equals("validation")){
                  oType = operationTypeDao.findByNom("ValidationEntite").get(0);
               }else if(operation.equals("invalidation")){
                  oType = operationTypeDao.findByNom("InvalidationEntite").get(0);
               }else{ // fusion
                  oType = operationTypeDao.findByNom("Fusion").get(0);
               }

            }

            CreateOrUpdateUtilities.createAssociateOperation(patient, operationManager, oType, utilisateur);

            // ajout association vers maladies
            if(maladies != null){
               updateMaladies(patient, maladies);
            }
            // ajout association vers collaborateurs
            if(medecins != null){
               updateMedecins(patient, medecins);
            }
            // ajout association vers liens familiaux
            if(patientLiens != null){
               updateLiens(patient, patientLiens);
            }

            try{
               // Annotations
               // suppr les annotations
               if(listAnnoToDelete != null){
                  annotationValeurManager.removeAnnotationValeurListManager(listAnnoToDelete, filesToDelete);
               }

               // update les annotations, null operation pour
               // laisser la possibilité création/modification au sein
               // de la liste
               if(listAnnoToCreateOrUpdate != null){
                  annotationValeurManager.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, patient, utilisateur, null,
                     baseDir, filesCreated, filesToDelete);
               }
               // enregistre operation associee annotation
               // si il y a eu des deletes et pas d'updates
               if((listAnnoToCreateOrUpdate == null || listAnnoToCreateOrUpdate.isEmpty())
                  && (listAnnoToDelete != null && !listAnnoToDelete.isEmpty())){
                  CreateOrUpdateUtilities.createAssociateOperation(patient, operationManager,
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
               if(operation.equals("creation") && !isImport){
                  patient.setPatientId(null);
               }
               throw re;
            }

         }else{
            throw new IllegalArgumentException(
               "Operation must match " + "'creation/modification/synchronisation/fusion/validation/invalidation' values");
         }
      }
   }

   @Override
   public Patient findByIdManager(final Integer id){
      final Patient a = patientDao.findById(id);
      return a;
   }

   @Override
   public Optional<PatientDoublonFound> findDoublonManager(final Patient patient){
      if (patient.getBanque() == null) {
         return findPatientDoublonOnIdentityOrNip(patient);
      } else { // gatsbi 
         // recherche d'abord par identifiant si existe pour la banque
         if (findPatientDoublonOnIdentifiant(patient)) {
            PatientDoublonFound df = new PatientDoublonFound();
            df.setIdentifiant(patient.getIdentifiantAsString());
            return Optional.of(df);
         }
         
         // sinon vérifie quand même les traits d'identités et le NIP
         return findPatientDoublonOnIdentityOrNip(patient);
      }  
   }
   
   private boolean findPatientDoublonOnIdentifiant(final Patient patient) {
      // un seul patient avec l'identifiant au max doit exister 
      List<Patient> existings = patientDao
         .findByIdentifiant(patient.getIdentifiantAsString(), Arrays.asList(patient.getBanque()));
      if(patient.getPatientId() == null){ // nouveau, aucun patient avec cet identifiant ne doit exister
         return !existings.isEmpty();
      }else{ // en modification, seul LE patient avec LE même id peut exister
         return !existings.isEmpty() && !patient.getPatientId().equals(existings.get(0).getPatientId());
      }
   }
      
   private Optional<PatientDoublonFound> findPatientDoublonOnIdentityOrNip(final Patient patient) {

      PatientDoublonFound df = new PatientDoublonFound();
      
      boolean doublon = false;
      if(patient.getPatientId() == null){ // creation
         doublon = patientDao.findByNom(patient.getNom()).contains(patient);
      }else{ // modification
         doublon = patientDao.findByExcludedId(patient.getPatientId(), patient.getNom()).contains((patient));
      }

      // il y un doublon sur les traits d'identité nom, prenom, date naissance
      // traits EQUALS du patient
      if(doublon){
         df.setNom(patient.getNom());
         return Optional.of(df);
      }
      
      // sinon érifier qu'il n'y a aucun patient ayant le même NIP
      if(patient.getPatientId() == null){ // creation
         doublon = patientDao.findByNip(patient.getNip()).size() > 0;
      } else { // modification
         doublon = patientDao.findByNipWithExcludedId(patient.getNip(), patient.getPatientId()).size() > 0;
      }
      
      if(doublon){
         df.setNip(patient.getNip());
         return Optional.of(df);
      }
      return Optional.empty();
   }

   @Override
   public Patient getExistingPatientManager(final Patient pat){
      if(pat != null){
         
         final List<Patient> patients = new ArrayList<Patient>();
         // @since gatsbi
         // recherche si le patient existant correspond à un identifiant
         if (pat.getBanque() != null) {
            patients.addAll(patientDao
               .findByIdentifiant(pat.getIdentifiantAsString(), Arrays.asList(pat.getBanque())));
            return patients.get(0); // un seul patient possible par identifiant par banque
         }
         
         // le patient existant porte sur l'identité
         if (patients.isEmpty()) {
            patients.addAll(patientDao.findByNom(pat.getNom()));
            // renvoie patient si contenu dans la liste
            if(patients.contains(pat)){
               return patients.get(patients.indexOf(pat));
            }
         }
      }
      return null;
   }

   @Override
   public List<Patient> findAllObjectsManager(){
      log.debug("Recherche totalite des Patients");
      return patientDao.findAll();
   }

   @Override
   public List<Integer> findAllObjectsIdsManager(){
      log.debug("Recherche totalite des ids des Patients");
      return patientDao.findByAllIds();
   }

   @Override
   public List<Integer> findAllObjectsIdsWithBanquesManager(final List<Banque> banques){
      log.debug("Recherche totalite des ids des Patients en" + "fonction d'une liste de banques");
      if(banques != null && banques.size() > 0){
         return patientDao.findByAllIdsWithBanques(banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Patient> findByNomLikeManager(String nom, final boolean exactMatch){
      if(!exactMatch){
         nom = "%" + nom + "%";
      }
      log.debug("Recherche Patient par nom: " + nom + " exactMatch " + String.valueOf(exactMatch));
      return patientDao.findByNom(nom);
   }

   @Override
   public List<Patient> findByNipLikeManager(String nip, final boolean exactMatch){
      if(!exactMatch){
         nip = "%" + nip + "%";
      }
      log.debug("Recherche Patient par nip: " + nip + " exactMatch " + String.valueOf(exactMatch));
      return patientDao.findByNip(nip);
   }

   /**
    * Recherche toutes les patients dont le nom est egal ou 'like'
    * celui passé en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   @Override
   public List<Patient> findByNomLikeBothSideManager(String nom, final boolean exactMatch){
      if(!exactMatch){
         nom = "%" + nom + "%";
      }
      log.debug("Recherche Patient par nom: " + nom + " exactMatch " + String.valueOf(exactMatch));
      return patientDao.findByNom(nom);
   }

   /**
    * Recherche toutes les patients dont le nom est egal ou 'like'
    * celui passé en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   @Override
   public List<Integer> findByNomLikeBothSideReturnIdsManager(String nom, final List<Banque> banques, final boolean exactMatch){
      if(!exactMatch){
         nom = "%" + nom + "%";
      }
      log.debug("Recherche Patient par nom: " + nom + " exactMatch " + String.valueOf(exactMatch));
      if(banques != null && banques.size() > 0){
         return patientDao.findByNomReturnIds(nom, banques);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche toutes les patients dont le nip est egal
    * ou 'like' celui en parametre.
    * @param nip
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   @Override
   public List<Integer> findByNipLikeBothSideReturnIdsManager(String nip, final List<Banque> banques, final boolean exactMatch){
      if(!exactMatch){
         nip = "%" + nip + "%";
      }
      log.debug("Recherche Patient par nip: " + nip + " exactMatch " + String.valueOf(exactMatch));
      if(banques != null && banques.size() > 0){
         return patientDao.findByNipReturnIds(nip, banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findAfterDateCreationReturnIdsManager(final Calendar date, final List<Banque> banques){
      List<Integer> liste = new ArrayList<>();
      if(date != null && banques != null){
         log.debug("Recherche des Patients enregistres apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         liste = findByOperationTypeAndDateWithBanquesReturnIds(operationTypeDao.findByNom("Creation").get(0), date, banques);
      }
      return liste;
   }

   @Override
   public List<Patient> findAfterDateModificationManager(final Calendar date){
      List<Patient> liste = new ArrayList<>();
      if(date != null){
         log.debug("Recherche des Patients modifies apres la date "
            + new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date.getTime()));
         liste = findByOperationTypeAndDate(operationTypeDao.findByNom("Modification").get(0), date);
      }
      return liste;
   }

   @Override
   public List<Patient> findByDateNaissanceManager(final Date date){
      if(date != null){
         log.debug("Recherche Patient par date de naissance: " + date.toString());
      }
      return patientDao.findByDateNaissance(date);
   }

   @Override
   public List<Patient> findByEtatIncompletManager(){
      log.debug("Recherche Patient incomplets");
      return patientDao.findByEtatIncomplet();
   }

   @Override
   public List<String> findAllNipsManager(){
      return patientDao.findAllNips();
   }

   @Override
   public List<String> findAllNomsManager(){
      return patientDao.findAllNoms();
   }

   @Override
   public List<Patient> findByNdaLikeManager(String nda, final boolean exactMatch){
      final List<Patient> patients = new ArrayList<>();
      if(!exactMatch){
         nda = "%" + nda + "%";
      }
      log.debug("Recherche Patient par nda: " + nda + " exactMatch " + String.valueOf(exactMatch));
      final List<Prelevement> prels = prelevementDao.findByNdaLike(nda);
      for(int i = 0; i < prels.size(); i++){
         if(prels.get(i).getMaladie() != null){
            patients.add(prels.get(i).getMaladie().getPatient());
         }
      }
      return patients;
   }

   @Override
   public void removeObjectManager(final Patient patient, final String comments, final Utilisateur user,
      final List<File> filesToDelete){
      if(patient != null){
         if(!isUsedObjectManager(patient)){

            // Supprime les maladies associées
            final Iterator<Maladie> malsIt = maladieManager.findAllByPatientManager(patient).iterator();
            while(malsIt.hasNext()){
               maladieManager.removeObjectManager(malsIt.next(), comments, user);
            }

            patientDao.removeObject(patient.getPatientId());
            log.info("Suppression objet Patient " + patient.toString());

            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(patient, operationManager, comments, user);

            //Supprime importations associes
            CreateOrUpdateUtilities.removeAssociateImportations(patient, importHistoriqueManager);

            //Supprime annotations associes
            annotationValeurManager.removeAnnotationValeurListManager(annotationValeurManager.findByObjectManager(patient),
               filesToDelete);
         }else{
            throw new ObjectUsedException("patient.deletion.isUsed", false);
         }
      }else{
         log.warn("Suppression d'un Patient null");
      }
   }

   @Override
   public Set<PatientMedecin> getPatientMedecinsManager(Patient patient){
      patient = patientDao.mergeObject(patient);
      final Set<PatientMedecin> medecins = patient.getPatientMedecins();
      medecins.isEmpty(); // operation empechant LazyInitialisationException
      return medecins;
   }

   @Override
   public List<Collaborateur> getMedecinsManager(Patient patient){
      final List<Collaborateur> colls = new ArrayList<>();
      if(patient != null){
         patient = patientDao.mergeObject(patient);
         final LinkedHashSet<PatientMedecin> medecins = new LinkedHashSet<>(patient.getPatientMedecins());
         final Iterator<PatientMedecin> it = medecins.iterator();
         while(it.hasNext()){
            final PatientMedecin pm = it.next();
            colls.add(pm.getCollaborateur());
         }
      }
      return colls;
   }

   @Override
   public Set<PatientLien> getPatientLiensManager(Patient patient){
      final Set<PatientLien> liens = new HashSet<>();
      if(patient != null){
         patient = patientDao.mergeObject(patient);
         liens.addAll(patient.getPatientLiens());
         liens.addAll(patient.getPatientLiens2());
         liens.isEmpty(); // operation empechant LazyInitialisationException
      }
      return liens;
   }

   /**
    * Cette méthode met à jour les associations entre un patient et
    * une liste de maladies.
    * @param patient pour lequel on veut mettre à jour
    * les associations.
    * @param maladies Liste des maladies que l'on veut associer
    * au patient.
    */
   private void updateMaladies(final Patient patient, final List<Maladie> maladies){

      final Patient pat = patientDao.mergeObject(patient);

      final Iterator<Maladie> it = pat.getMaladies().iterator();
      final List<Maladie> malsToRemove = new ArrayList<>();
      // on parcourt les maladies qui sont actuellement associées
      // au patient
      while(it.hasNext()){
         final Maladie tmp = it.next();
         // si une maladie n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!maladies.contains(tmp)){
            malsToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des maladies à retirer de
      // l'association
      for(int i = 0; i < malsToRemove.size(); i++){
         final Maladie mal = maladieDao.mergeObject(malsToRemove.get(i));
         // on retire la maladie de l'association et on la supprime
         pat.getMaladies().remove(mal);
         maladieDao.removeObject(mal.getMaladieId());

         log.debug("Suppression de l'association entre le patient : " + pat.toString() + " et suppression de la maladie : "
            + mal.toString());
      }

      // on parcourt la nouvelle liste de maladies
      for(int i = 0; i < maladies.size(); i++){
         // si une maladie n'était pas associé au patient
         if(!pat.getMaladies().contains(maladies.get(i))){
            // on ajoute la maladie à l'association
            pat.getMaladies().add(maladieDao.mergeObject(maladies.get(i)));

            log.debug(
               "Ajout de l'association entre le patient : " + pat.toString() + " et la maladie : " + maladies.get(i).toString());
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre un patient et
    * une liste de patientMedecin.
    * @param patient pour lequel on veut mettre à jour
    * les associations.
    * @param collaborateurs Liste ordonnée des medecins referents
    * que l'on veut associer au patient.
    */
   private void updateMedecins(final Patient patient, final List<Collaborateur> collaborateurs){

      final Patient pat = patientDao.mergeObject(patient);

      final Iterator<PatientMedecin> it = pat.getPatientMedecins().iterator();
      final List<PatientMedecin> medsToRemove = new ArrayList<>();
      // on parcourt les medecins qui sont actuellement associés
      // au patient
      while(it.hasNext()){
         final PatientMedecin tmp = it.next();
         // si un medecin n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!collaborateurs.contains(tmp.getCollaborateur())){
            medsToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des medecins à retirer de
      // l'association
      for(int i = 0; i < medsToRemove.size(); i++){
         final PatientMedecin med = patientMedecinDao.mergeObject(medsToRemove.get(i));
         // on retire le medecin de l'association et on le supprime
         pat.getPatientMedecins().remove(med);
         patientMedecinDao.removeObject(med.getPk());

         log.debug("Suppression de l'association entre le patient : " + pat.toString() + " et suppression du medecin : "
            + med.toString());
      }

      // on parcourt la nouvelle liste de medecins
      for(int i = 0; i < collaborateurs.size(); i++){
         PatientMedecin pm = new PatientMedecin();
         final PatientMedecinPK pk = new PatientMedecinPK(collaborateurs.get(i), pat);
         pm.setPk(pk);
         pm.setOrdre(i + 1);
         // si un medecin n'était pas associé au patient
         if(!pat.getPatientMedecins().contains(pm)){
            // on ajoute le medecin dans l'association dans le bon ordre
            pat.getPatientMedecins().add(patientMedecinDao.mergeObject(pm));

            log.debug("Ajout de l'association entre le patient : " + pat.toString() + " et le medecin : "
               + collaborateurs.get(i).toString());
         }else{ // on modifie l'ordre du medecin present avec la liste
            pm = patientMedecinDao.findById(pk);
            pm.setOrdre(i + 1);
            patientMedecinDao.mergeObject(pm);
         }
      }
   }

   /**
    * Cette méthode met à jour les associations entre un patient et
    * une liste de patientLien.
    * @param patient pour lequel on veut mettre à jour
    * les liens familiaux.
    * @param patients Liste de patients que l'on veut que
    * l'on veut lier au patient.
    */
   private void updateLiens(final Patient patient, final List<PatientLien> liens){

      final Patient pat = patientDao.mergeObject(patient);
      Patient pat2;

      final Iterator<PatientLien> it = pat.getPatientLiens().iterator();
      final List<PatientLien> liensToRemove = new ArrayList<>();
      // on parcourt les liens qui sont actuellement associés
      // au patient
      while(it.hasNext()){
         final PatientLien tmp = it.next();
         // si un lien n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!liens.contains(tmp)){
            liensToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des liens à retirer de
      // l'association
      for(int i = 0; i < liensToRemove.size(); i++){
         final PatientLien lien = patientLienDao.mergeObject(liensToRemove.get(i));
         // on retire le lien de l'association et on le supprime
         pat2 = lien.getPatient2();
         pat.getPatientLiens().remove(lien);
         pat2.getPatientLiens2().remove(lien);
         patientLienDao.removeObject(lien.getPk());

         log.debug(
            "Suppression de l'association entre le patient : " + pat.toString() + " et suppression du lien : " + lien.toString());
      }

      // on parcourt la nouvelle liste de liens
      for(int i = 0; i < liens.size(); i++){
         // si un lien n'était pas associé au patient
         if(!pat.getPatientLiens().contains(liens.get(i))){
            final PatientLien lien = new PatientLien();
            final PatientLienPK pk = new PatientLienPK();
            pk.setPatient1(pat);
            pk.setPatient2(liens.get(i).getPatient2());
            lien.setPk(pk);
            lien.setLienFamilial(liens.get(i).getLienFamilial());
            // on ajoute le lien dans l'association
            pat.getPatientLiens().add(patientLienDao.mergeObject(lien));
            //				patientDao.mergeObject((liens.get(i).getPatient2()))
            //									.getPatientLiens2().add(liens.get(i));

            log.debug("Ajout de l'association entre le patient : " + pat.toString() + " et le lien : " + liens.get(i).toString());
         }
      }
   }

   /**
    * Recupere la liste de patients en fonction du type d'operation et
    * d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale.
    * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
    * utilises pour recuperer les patients.
    * @param oType OperationType
    * @param date
    * @return List de Patient
    */

   private List<Patient> findByOperationTypeAndDate(final OperationType oType, final Calendar date){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Patient"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Patient> prelQuery;
      if(ids.size() > 1){ //HQL IN () si liste taille > 1
         prelQuery = em.createQuery("SELECT DISTINCT p FROM Patient p " + "WHERE p.patientId IN (:ids)", Patient.class);
         prelQuery.setParameter("ids", ids);
      }else if(ids.size() == 1){
         prelQuery = em.createQuery("SELECT DISTINCT p FROM Patient p " + "WHERE p.patientId = :id", Patient.class);
         prelQuery.setParameter("id", ids.get(0));
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   /**
    * Recupere la liste de patients en fonction du type d'operation et
    * d'une date a laquelle la date d'enregistrement de l'operation doit
    * etre superieure ou egale.
    * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
    * utilises pour recuperer les patients.
    * @param oType OperationType
    * @param date
    * @return List de Patient
    */

   private List<Integer> findByOperationTypeAndDateWithBanquesReturnIds(final OperationType oType, final Calendar date,
      final List<Banque> banques){
      final EntityManager em = entityManagerFactory.createEntityManager();
      final TypedQuery<Integer> opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
         + "Operation o WHERE o.entite = :entite AND " + "o.operationType = :oType AND date >= :date", Integer.class);
      opQuery.setParameter("entite", entiteDao.findByNom("Patient"));
      opQuery.setParameter("oType", oType);
      opQuery.setParameter("date", date);
      final List<Integer> ids = opQuery.getResultList();
      TypedQuery<Integer> prelQuery;
      if(banques.size() > 0 && ids.size() > 1){
         //HQL IN () si liste taille > 1
         prelQuery = em.createQuery("SELECT DISTINCT p.patientId " + "FROM Patient p " + "JOIN p.maladies m "
            + "JOIN m.prelevements prlvts " + "WHERE p.patientId IN (:ids) " + "AND prlvts.banque IN (:banques)", Integer.class);
         prelQuery.setParameter("ids", ids);
         prelQuery.setParameter("banques", banques);
      }else if(banques.size() > 0 && ids.size() == 1){
         prelQuery = em.createQuery("SELECT DISTINCT p.patientId " + "FROM Patient p " + "JOIN p.maladies m "
            + "JOIN m.prelevements prlvts " + "WHERE p.patientId = :id " + "AND prlvts.banque IN (:banques)", Integer.class);
         prelQuery.setParameter("id", ids.get(0));
         prelQuery.setParameter("banques", banques);
      }else{
         return new ArrayList<>();
      }

      return prelQuery.getResultList();
   }

   /**
    * Recherche les derniers Patients créés dans le systeme.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste de Patients.
    */
   @Override
   public List<Patient> findLastCreationManager(final List<Banque> banques, final int nbResults){

      final List<Patient> liste = new ArrayList<>();
      if(banques != null && banques.size() > 0 && nbResults > 0){
         log.debug("Recherche des " + nbResults + " derniers Patients " + "enregistres.");
         final EntityManager em = entityManagerFactory.createEntityManager();
         final TypedQuery<Patient> query = em.createQuery("SELECT distinct p " + "FROM Patient p " + "JOIN p.maladies m "
            + "JOIN m.prelevements prlvts " + "WHERE prlvts.banque in (:banques) " + "ORDER BY p.patientId DESC", Patient.class);

         query.setParameter("banques", banques);
         query.setFirstResult(0);
         query.setMaxResults(nbResults);

         liste.addAll(query.getResultList());
      }
      return liste;

   }

   @Override
   public Long getTotMaladiesCountManager(final Patient p){
      return patientDao.findCountMaladies(p).get(0);
   }

   @Override
   public Long getTotPrelevementsCountManager(final Patient p){
      return patientDao.findCountPrelevements(p).get(0);
   }

   @Override
   public Long getCountPrelevementsByBanqueManager(final Patient pat, final Banque bank){
      return patientDao.findCountPrelevementsByBanque(pat, bank).get(0);
   }

   @Override
   public void updateMultipleObjectsManager(final List<Patient> patients, final List<Patient> basePatients,
      final List<AnnotationValeur> listAnnoToCreateOrUpdate, final List<AnnotationValeur> listAnnoToDelete,
      final Utilisateur utilisateur, final String baseDir){

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      for(int i = 0; i < patients.size(); i++){
         final Patient pat = patients.get(i);
         try{
            createOrUpdateObjectManager(pat, null, null, null, null, null, null, null, utilisateur, "modifMulti", baseDir, false);
         }catch(final RuntimeException e){
            if(e instanceof TKException){
               ((TKException) e).setEntiteObjetException("Patient");
               ((TKException) e).setIdentificationObjetException(pat.getNom());
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
                  annotationValeurManager.createFileBatchForTKObjectsManager(basePatients, val.getFichier(), val.getStream(),
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

         for(final File f : filesToDelete){
            f.delete();
         }
      }catch(final RuntimeException e){
         for(final File f : filesCreated){
            f.delete();
         }
      }
   }

   @Override
   public boolean isUsedObjectManager(final Patient patient){
      return getTotPrelevementsCountManager(patient) > 0;
   }

   @Override
   public List<Patient> findByIdsInListManager(final List<Integer> ids){
      if(ids != null && ids.size() > 0){
         return patientDao.findByIdInList(ids);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Field> isSynchronizedPatientManager(final PatientSip sip, final Patient inBase){
      final List<Field> fields = new ArrayList<>();
      if(sip != null && inBase != null){
         try{
            if(sip.getNip() != null && !sip.getNip().equals(inBase.getNip())){
               fields.add(inBase.getClass().getDeclaredField("nip"));
            }
            if(!sip.getNom().equals(inBase.getNom())){
               fields.add(inBase.getClass().getDeclaredField("nom"));
            }
            // efface nom naissance si null sip
            if((sip.getNomNaissance() != null && !sip.getNomNaissance().equals(inBase.getNomNaissance()))
               || (sip.getNomNaissance() == null && inBase.getNomNaissance() != null)){
               fields.add(inBase.getClass().getDeclaredField("nomNaissance"));
            }
            if(!sip.getPrenom().equals(inBase.getPrenom())){
               fields.add(inBase.getClass().getDeclaredField("prenom"));
            }
            if(!sip.getDateNaissance().equals(inBase.getDateNaissance())){
               fields.add(inBase.getClass().getDeclaredField("dateNaissance"));
            }
            if(!sip.getSexe().equals(inBase.getSexe())){
               fields.add(inBase.getClass().getDeclaredField("sexe"));
            }
            if(sip.getVilleNaissance() != null && !sip.getVilleNaissance().equals(inBase.getVilleNaissance())){
               fields.add(inBase.getClass().getDeclaredField("villeNaissance"));
            }
            if(sip.getPaysNaissance() != null && !sip.getPaysNaissance().equals(inBase.getPaysNaissance())){
               fields.add(inBase.getClass().getDeclaredField("paysNaissance"));
            }
            if(!sip.getPatientEtat().equals(inBase.getPatientEtat())){
               fields.add(inBase.getClass().getDeclaredField("patientEtat"));
            }
            if(sip.getDateEtat() != null && !sip.getDateEtat().equals(inBase.getDateEtat())){
               fields.add(inBase.getClass().getDeclaredField("dateEtat"));
            }
            if(sip.getDateDeces() != null && !sip.getDateDeces().equals(inBase.getDateDeces())){
               fields.add(inBase.getClass().getDeclaredField("dateDeces"));
            }
         }catch(final NoSuchFieldException ne){
            log.error(ne.getMessage());
         }
      }
      return fields;
   }

   @Override
   public void fusionPatientManager(final Patient patient, final Patient passif, final Utilisateur u, final String comments){

      if(patient != null && passif != null){

         // recuperation des medecins referents
         final List<Collaborateur> medsP = getMedecinsManager(passif);
         final List<Collaborateur> medsA = getMedecinsManager(patient);
         // ajoute a la liste les nouveaux medecins
         for(int i = 0; i < medsP.size(); i++){
            if(!medsA.contains(medsP.get(i))){
               medsA.add(medsP.get(i));
            }
         }

         // recuperation des liens familiaux
         final List<PatientLien> liensP = new ArrayList<>(getPatientLiensManager(passif));
         final List<PatientLien> liensA = new ArrayList<>(getPatientLiensManager(patient));
         // ajoute a la liste les nouveaux liens
         for(int i = 0; i < liensP.size(); i++){
            liensP.get(i).getPk().setPatient1(patient);
            if(!liensA.contains(liensP.get(i))){
               liensA.add(liensP.get(i));
            }
         }

         // gestion des annotations
         final List<AnnotationValeur> valeursActives = annotationValeurManager.findByObjectManager(patient);
         final List<AnnotationValeur> valeursPassives = annotationValeurManager.findByObjectManager(passif);
         final Hashtable<ChampAnnotation, AnnotationValeur> champsValeurs = new Hashtable<>();
         // on classe les valeurs actives en fct de leur champ
         for(int i = 0; i < valeursActives.size(); i++){
            champsValeurs.put(valeursActives.get(i).getChampAnnotation(), valeursActives.get(i));
         }
         // liste des annotations du passif à conserver
         final List<AnnotationValeur> valeursAConserver = new ArrayList<>();
         // liste des annotations du passif à supprimer
         final List<AnnotationValeur> valeursASupprimer = new ArrayList<>();
         for(int i = 0; i < valeursPassives.size(); i++){
            // si le patient actif a déjà une annotation pour le
            // champ de l'annotation du passif
            if(champsValeurs.containsKey(valeursPassives.get(i).getChampAnnotation())){
               // si cette valeur est pour la même collection :
               // on va supprimer cette annotation
               valeursASupprimer.add(valeursPassives.get(i));
            }else{
               valeursAConserver.add(valeursPassives.get(i));
            }
         }

         // mise a jour du Patient actif
         createOrUpdateObjectManager(patient, null, medsA, liensA, valeursAConserver, null, null, null, u, "fusion", null, false);

         // recuperation des maladies
         final Set<Maladie> malP = maladieManager.getMaladiesManager(passif);
         final List<Maladie> malA = new ArrayList<>(maladieManager.getMaladiesManager(patient));
         // Ajoute la maladie si n'existe pas sinon ajoute ses prels
         Iterator<Prelevement> prelsIt;
         final List<Maladie> malsToRemove = new ArrayList<>();
         Maladie maladie;
         Prelevement prel;
         final Iterator<Maladie> malIt = malP.iterator();
         while(malIt.hasNext()){
            maladie = malIt.next();
            prelsIt = maladieManager.getPrelevementsManager(maladie).iterator();
            maladie.setPatient(patient); // pour appliquer equals()
            if(!malA.contains(maladie)){ // ajoute la maladie
               maladieDao.updateObject(maladie);
            }else{ // ajoute prelevements a la maladie existante
               final int index = malA.indexOf(maladie);
               maladie.setPatient(passif); //recupere son patient
               while(prelsIt.hasNext()){
                  prel = prelsIt.next();
                  prel.setMaladie(malA.get(index));
                  prelevementDao.updateObject(prel);
               }
               maladie.getPrelevements().clear();
               malsToRemove.add(maladie);
            }
         }

         // fantomization (oh le beau mot) du passif
         for(int i = 0; i < malsToRemove.size(); i++){
            maladieManager.removeObjectManager(malsToRemove.get(i), comments, u);
         }

         // remove patient et objets associes
         patientDao.removeObject(passif.getPatientId());
         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(passif, operationManager, comments, u);

         //Supprime importations associes
         CreateOrUpdateUtilities.removeAssociateImportations(passif, importHistoriqueManager);

         //Supprime annotations associes
         final List<File> filesToDelete = new ArrayList<>();
         annotationValeurManager.removeAnnotationValeurListManager(valeursASupprimer, filesToDelete);
         for(final File f : filesToDelete){
            f.delete();
         }
      }
   }

   @Override
   public List<Integer> findByNipInListManager(final List<String> criteres, final List<Banque> banques){
      if(criteres != null && criteres.size() > 0 && banques != null && banques.size() > 0){
         return patientDao.findByNipInList(criteres, banques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByNomInListManager(final List<String> criteres, final List<Banque> banques){
      if(criteres != null && criteres.size() > 0 && banques != null && banques.size() > 0){
         return patientDao.findByNomInList(criteres, banques);
      }
      return new ArrayList<>();
   }

   @Override
   public Long findCountByReferentManager(final Collaborateur colla){
      if(colla != null){
         return patientDao.findCountByReferent(colla).get(0);
      }
      return new Long(0);
   }

   @Override
   public void removeListFromIdsManager(final List<Integer> ids, final String comment, final Utilisateur u, final Banque b){
      if(ids != null){
         final List<File> filesToDelete = new ArrayList<>();
         Patient p;
         for(final Integer id : ids){
            p = findByIdManager(id);
            if(p != null){
               p.setBanque(b); // @since 2.3.0-gasbi, si banque non nulle alors identifiant sera chois comme phantomData
               removeObjectManager(p, comment, u, filesToDelete);
            }
         }
         for(final File f : filesToDelete){
            f.delete();
         }
      }
   }

   @Override
   public List<Integer> findByIdentifiantsInListManager(List<String> identifiants, List<Banque> selectedBanques){
      if(identifiants != null && !identifiants.isEmpty() && selectedBanques != null && !selectedBanques.isEmpty()){
         return patientDao.findByIdentifiantInList(identifiants, selectedBanques);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Integer> findByIdentifiantLikeBothSideReturnIdsManager(String identifiant,
      List<Banque> selectedBanques, boolean exactMatch){
      if(!exactMatch){
         identifiant = "%" + identifiant + "%";
      }
      log.debug("Recherche Patient par identifiant: " + identifiant + " exactMatch " + String.valueOf(exactMatch));
      if(selectedBanques != null && !selectedBanques.isEmpty()){
         return patientDao.findByIdentifiantReturnIds(identifiant, selectedBanques);
      }
      return new ArrayList<Integer>();
   }

   @Override
   public List<Patient> findByIdentifiantLikeManager(String ident, boolean exactMatch, List<Banque> selectedBanques){
      if(!exactMatch){
         ident = "%" + ident + "%";
      }
      log.debug("Recherche Patient par identifiant: " + ident + " exactMatch " + String.valueOf(exactMatch));
      if(selectedBanques != null && !selectedBanques.isEmpty()){
         return patientDao.findByIdentifiant(ident, selectedBanques);
      }
      return new ArrayList<Patient>();
   }
}