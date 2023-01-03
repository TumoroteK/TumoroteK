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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientValidator;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.patient.MaladieValidator;
import fr.aphp.tumorotek.manager.validation.coeur.patient.gatsbi.MaladieGatsbiValidator;
import fr.aphp.tumorotek.manager.validation.coeur.patient.gatsbi.MaladieValidatorDateCoherenceOverride;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Maladie.
 * Classe créée le 30/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public class MaladieManagerImpl implements MaladieManager
{

   private final Log log = LogFactory.getLog(MaladieManager.class);

   /* Beans injectes par Spring*/
   private MaladieDao maladieDao;

   private PatientDao patientDao;

   private CollaborateurDao collaborateurDao;

   private MaladieValidator maladieValidator;
   
   //@since 2.3.0-gatsbi
   private MaladieValidatorDateCoherenceOverride maladieValidatorDateCoherenceOverride;

   private PatientValidator patientValidator;

   private OperationTypeDao operationTypeDao;

   private OperationManager operationManager;

   public MaladieManagerImpl(){}

   /* Properties setters */
   public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setMaladieValidator(final MaladieValidator mValidator){
      this.maladieValidator = mValidator;
   }

   public void setMaladieValidatorDateCoherenceOverride(MaladieValidatorDateCoherenceOverride _v){
      this.maladieValidatorDateCoherenceOverride = _v;
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

   @Override
   public void createOrUpdateObjectManager(Maladie maladie, final Patient patient, final List<Collaborateur> medecins,
      final Utilisateur utilisateur, final String operation){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Verifie required Objects associes et validation
      checkRequiredObjectsAndValidate(maladie, patient, operation);

      //Doublon
      if(!findDoublonManager(maladie, patient)){
         if((operation.equals("creation") || operation.equals("modification"))){
            if(operation.equals("creation")){
               maladieDao.createObject(maladie);
               log.info("Enregistrement objet Maladie " + maladie.toString());
               CreateOrUpdateUtilities.createAssociateOperation(maladie, operationManager,
                  operationTypeDao.findByNom("Creation").get(0), utilisateur);
            }else{

               maladie = maladieDao.mergeObject(maladie);
               log.info("Modification objet Maladie " + maladie.toString());
               CreateOrUpdateUtilities.createAssociateOperation(maladie, operationManager,
                  operationTypeDao.findByNom("Modification").get(0), utilisateur);
            }
            // ajout association vers collaborateurs
            if(medecins != null){
               updateCollaborateurs(maladie, medecins);
            }
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors " + operation + " objet Maladie " + maladie.toString());
         throw new DoublonFoundException("Maladie", operation);
      }
   }

   @Override
   public boolean findDoublonManager(final Maladie maladie, final Patient patient){
      if(patient != null){
         if(patient.getPatientId() != null){ // si le patient n'est pas encore enregistré, doublon impossible
            final List<Maladie> mals = maladieDao.findByLibelleAndPatient(maladie.getLibelle(), patient);
            if(mals.contains(maladie)){
               if(maladie.getMaladieId() == null){
                  return true;
               }
               return maladie.getMaladieId() != mals.get(mals.indexOf(maladie)).getMaladieId();
            }
         }
      }else{ // ancienne recherche de doublons ne reposant pas sur le patient
         if(maladie.getMaladieId() == null){
            return maladieDao.findByLibelle(maladie.getLibelle()).contains(maladie);
         }else{
            return maladieDao.findByExcludedId(maladie.getMaladieId(), maladie.getLibelle()).contains((maladie));
         }
      }
      return false;
   }

   @Override
   public Set<Prelevement> getPrelevementsManager(Maladie maladie){
      maladie = maladieDao.mergeObject(maladie);
      final Set<Prelevement> prelevements = maladie.getPrelevements();
      prelevements.isEmpty(); // empechant LazyInitialisationException
      return prelevements;
   }

   @Override
   public List<Maladie> findAllObjectsManager(){
      log.debug("Recherche totalite des Maladie");
      return maladieDao.findAll();
   }

   @Override
   public List<Maladie> findByLibelleLikeManager(String libelle, final boolean exactMatch){
      if(!exactMatch){
         libelle = libelle + "%";
      }
      log.debug("Recherche Maladie par libelle: " + libelle + " exactMatch " + String.valueOf(exactMatch));
      return maladieDao.findByLibelle(libelle);
   }

   @Override
   public List<Maladie> findByCodeLikeManager(String code, final boolean exactMatch){
      if(!exactMatch){
         code = code + "%";
      }
      log.debug("Recherche Maladie par code: " + code + " exactMatch " + String.valueOf(exactMatch));
      return maladieDao.findByCode(code);
   }

   @Override
   public void removeObjectManager(final Maladie maladie, final String comments, final Utilisateur user){
      if(maladie != null){
         if(!isUsedObjectManager(maladie)){
            //Supprime operations associes
            CreateOrUpdateUtilities.removeAssociateOperations(maladie, operationManager, comments, user);

            maladieDao.removeObject(maladie.getMaladieId());
            log.info("Suppression objet Maladie " + maladie.toString());
         }else{
            log.warn("Suppression Maladie " + maladie.toString() + " impossible car Objet est reference " + "(par Prelevement)");
            throw new ObjectUsedException("maladie.deletion.isUsed", false);
         }
      }else{
         log.warn("Suppression d'un Maladie null");
      }
   }

   @Override
   public boolean isUsedObjectManager(final Maladie maladie){
      return getPrelevementsManager(maladie).size() > 0;
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes
    * sont non nulls et lance la validation via la Validator.
    * @param maladie
    * @param patient
    * @param operation demandant la verification
    * @version 2.3.0-gatsbi
    */
   private void checkRequiredObjectsAndValidate(final Maladie maladie, final Patient patient, final String operation){
      //Patient required
      if(patient != null){
         if(patient.getPatientId() == null){ // creation conjointe
            // validation patient
            // TODO cette création conjointe n'est jamais appliquée !?
            // sinon pas de trace de création patient dans table Operation
            BeanValidator.validateObject(patient, new Validator[] {patientValidator});
         }
         maladie.setPatient(patientDao.mergeObject(patient));
      }else if(maladie.getPatient() == null){
         log.warn("Objet obligatoire Patient manquant" + " lors de la " + operation + " d'une Maladie");
         throw new RequiredObjectIsNullException("Maladie", operation, "Patient");
      }
      
      // Gatsbi required
      final List<Integer> requiredChampEntiteId = new ArrayList<>();
      if(maladie.getPatient().getBanque() != null && maladie.getPatient().getBanque().getEtude() != null){
         final Contexte maladieContexte = maladie.getPatient().getBanque().getEtude().getContexteForEntite(7);
         if(maladieContexte != null){
            requiredChampEntiteId.addAll(maladieContexte.getRequiredChampEntiteIds());
         }
      }

      //Validation maladie
      Validator[] validators;
      if(requiredChampEntiteId.isEmpty()){ // pas de restriction gatsbi
         validators = new Validator[] {maladieValidator};
      }else{ // gatsbi définit certain champs obligatoires
         final MaladieGatsbiValidator gValidator = 
            new MaladieGatsbiValidator("maladie", requiredChampEntiteId);
         validators = new Validator[] {gValidator, maladieValidatorDateCoherenceOverride};
      }

      BeanValidator.validateObject(maladie, validators);   
   }

   @Override
   public Set<Maladie> getMaladiesManager(Patient patient){
      Set<Maladie> maladies = new HashSet<>();
      if(patient != null && patient.getPatientId() != null){
         patient = patientDao.mergeObject(patient);
         maladies = patient.getMaladies();
         maladies.isEmpty(); // operation LazyInitialisationExcep
      }
      return maladies;
   }

   @Override
   public Set<Collaborateur> getCollaborateursManager(Maladie maladie){
      maladie = maladieDao.mergeObject(maladie);
      final Set<Collaborateur> collabs = maladie.getCollaborateurs();
      return collabs;
   }

   /**
    * Cette méthode met à jour les associations entre une maladie et
    * une liste de collaborateurs.
    * @param maladie pour laquelle on veut mettre à jour
    * les associations.
    * @param collaborateurs Liste des collaborateur que l'on veut associer
    * a la maladie.
    */
   private void updateCollaborateurs(final Maladie mal, final List<Collaborateur> collaborateurs){

      final Iterator<Collaborateur> it = mal.getCollaborateurs().iterator();
      final List<Collaborateur> collabsToRemove = new ArrayList<>();
      // on parcourt les collaborateurs qui sont actuellement associés
      // a la maladie
      while(it.hasNext()){
         final Collaborateur tmp = it.next();
         // si un collaborateur n'est pas dans la nouvelle liste, on
         // le conserve afin de le retirer par la suite
         if(!collaborateurs.contains(tmp)){
            collabsToRemove.add(tmp);
         }
      }

      // on parcourt la liste la liste des collaborateurs à retirer de
      // l'association
      for(int i = 0; i < collabsToRemove.size(); i++){
         final Collaborateur coll = collaborateurDao.mergeObject(collabsToRemove.get(i));
         // on retire le collaborateur de chaque coté de l'association
         coll.getMaladies().remove(mal);
         mal.getCollaborateurs().remove(coll);

         log.debug(
            "Suppression de l'association entre la maladie : " + mal.toString() + " et le collaborateur : " + coll.toString());
      }

      // on parcourt la nouvelle liste de collaborateurs
      for(int i = 0; i < collaborateurs.size(); i++){
         // si un collaborateur n'était pas associé a la maladie
         if(!mal.getCollaborateurs().contains(collaborateurs.get(i))){
            // on ajoute le collaborateur des deux cotés de l'association
            mal.getCollaborateurs().add(collaborateurDao.mergeObject(collaborateurs.get(i)));
            collaborateurDao.mergeObject(collaborateurs.get(i)).getMaladies().add(mal);

            log.debug("Ajout de l'association entre la maladie : " + mal.toString() + " et le collaborateur : "
               + collaborateurs.get(i).toString());
         }
      }
   }

   @Override
   public List<Maladie> findByPatientNoSystemNorVisiteManager(final Patient patient){
      return maladieDao.findByPatientNoSystemNorVisite(patient);
   }

   @Override
   public List<Maladie> findAllByPatientManager(final Patient patient){
      return maladieDao.findAllByPatient(patient);
   }
   
   @Override
   public List<Maladie> findByPatientExcludingVisitesManager(final Patient patient){
      return maladieDao.findByPatientExcludingVisites(patient);
   }

   @Override
   public Long findCountByReferentManager(final Collaborateur colla){
      if(colla != null){
         return maladieDao.findCountByReferent(colla).get(0);
      }
      return new Long(0);
   }

   @Override
   public List<Maladie> findByLibelleAndPatientManager(final String libelle, final Patient patient){
      return maladieDao.findByLibelleAndPatient(libelle, patient);
   }

   @Override
   public List<Maladie> findVisitesManager(Patient patient, Banque banque){
      return maladieDao.findVisites(patient, banque);
   }
}