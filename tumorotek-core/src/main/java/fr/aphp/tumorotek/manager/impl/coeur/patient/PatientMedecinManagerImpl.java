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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientMedecinDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientMedecinManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.patient.PatientMedecinValidator;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 *
 * Implémentation du manager du bean de domaine PatientMedecin.
 * Classe créée le 13/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientMedecinManagerImpl implements PatientMedecinManager
{

   private final Log log = LogFactory.getLog(PatientMedecinManager.class);

   /* Beans injectes par Spring*/
   private PatientMedecinDao patientMedecinDao;
   private PatientDao patientDao;
   private CollaborateurDao collaborateurDao;
   private PatientMedecinValidator patientMedecinValidator;

   public PatientMedecinManagerImpl(){}

   /* Properties setters */
   public void setPatientMedecinDao(final PatientMedecinDao pmDao){
      this.patientMedecinDao = pmDao;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setPatientMedecinValidator(final PatientMedecinValidator pmValidator){
      this.patientMedecinValidator = pmValidator;
   }

   @Override
   public void createOrUpdateObjectManager(final PatientMedecin medecin, final Patient patient, final Collaborateur collaborateur,
      final String operation){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Verifie required Objects associes
      checkRequiredObjectsAndValidate(medecin, patient, collaborateur, operation);

      //Doublon
      if(!findDoublonManager(medecin)){
         if(operation.equals("creation")){
            //if (operation.equals("creation")) {
            patientMedecinDao.createObject(medecin);
            log.debug("Enregistrement objet PatientMedecin " + medecin.toString());
            //				} else { //cree un nouvel objet car modification de la clef
            //					PatientMedecinDao.updateObject(PatientMedecin);
            //					log.info("Modification objet PatientMedecin "
            //												+ PatientMedecin.toString());
            //				}
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation' values");
         }
      }else{ //gere le modification du seul lien ordre
         if(operation.equals("modification")
            && !medecin.getOrdre().equals(patientMedecinDao.findById(medecin.getPk()).getOrdre())){
            patientMedecinDao.updateObject(medecin);
            log.debug("Modification objet PatientMedecin " + medecin.toString());
         }else{
            log.warn("Doublon lors " + operation + " objet PatientMedecin " + medecin.toString());
            throw new DoublonFoundException("PatientMedecin", operation);
         }
      }
   }

   @Override
   public void removeObjectManager(final PatientMedecin medecin){
      if(medecin != null){
         patientMedecinDao.removeObject(medecin.getPk());
         log.debug("Suppression objet PatientMedecin " + medecin.toString());
      }else{
         log.warn("Suppression d'un PatientMedecin null");
      }
   }

   @Override
   public boolean findDoublonManager(final PatientMedecin medecin){
      // l'utilisation de excludedId impossible a cause de la clef composite 
      return patientMedecinDao.findAll().contains(medecin);
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes 
    * sont non nulls.
    * @param TableAnnotationBanque
    * @param patient1
    * @param patient2
    * @param lienFamilial
    * @param operation demandant la verification
    */
   private void checkRequiredObjectsAndValidate(final PatientMedecin medecin, final Patient patient,
      final Collaborateur collaborateur, final String operation){
      //patient required
      if(patient != null){
         medecin.setPatient(patientDao.mergeObject(patient));
      }else if(medecin.getPatient() == null){
         log.warn("Objet obligatoire Patient manquant" + " lors de la " + operation + " d'un PatientMedecin");
         throw new RequiredObjectIsNullException("PatientMedecin", operation, "Patient");
      }
      //Collaborateur required
      if(collaborateur != null){
         medecin.setCollaborateur(collaborateurDao.mergeObject(collaborateur));
      }else if(medecin.getCollaborateur() == null){
         log.warn("Objet obligatoire Collaborateur  manquant" + " lors de la " + operation + " d'un PatientMedecin");
         throw new RequiredObjectIsNullException("PatientMedecin", operation, "Collaborateur");
      }

      //Validation
      BeanValidator.validateObject(medecin, new Validator[] {patientMedecinValidator});
   }
}
