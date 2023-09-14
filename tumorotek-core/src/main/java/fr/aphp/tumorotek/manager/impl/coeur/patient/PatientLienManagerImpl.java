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

import fr.aphp.tumorotek.dao.coeur.patient.LienFamilialDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientLienDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientLienManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;

/**
 *
 * Implémentation du manager du bean de domaine PatientLien.
 * Classe créée le 13/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientLienManagerImpl implements PatientLienManager
{

   private final Log log = LogFactory.getLog(PatientLienManager.class);

   /* Beans injectes par Spring*/
   private PatientLienDao patientLienDao;

   private PatientDao patientDao;

   private LienFamilialDao lienFamilialDao;

   public PatientLienManagerImpl(){}

   /* Properties setters */
   public void setPatientLienDao(final PatientLienDao plDao){
      this.patientLienDao = plDao;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setLienFamilialDao(final LienFamilialDao lfDao){
      this.lienFamilialDao = lfDao;
   }

   @Override
   public void createOrUpdateObjectManager(final PatientLien patientLien, final Patient patient1, final Patient patient2,
      final LienFamilial lienFamilial, final String operation){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Verifie required Objects associes
      checkRequiredObjectsAndValidate(patientLien, patient1, patient2, lienFamilial, operation);

      //Doublon
      if(!findDoublonManager(patientLien)){
         if(operation.equals("creation")){
            //if (operation.equals("creation")) {
            patientLienDao.createObject(patientLien);
            log.debug("Enregistrement objet PatientLien " + patientLien.toString());
            //				} else { //cree un nouvel objet car modification de la clef
            //					patientLienDao.updateObject(patientLien);
            //					log.info("Modification objet PatientLien "
            //													+ patientLien.toString());
            //				}
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation' values");
         }
      }else{ //gere le modification du seul lien familial
         if(operation.equals("modification")
            && !patientLien.getLienFamilial().equals(patientLienDao.findById(patientLien.getPk()).getLienFamilial())){
            patientLienDao.updateObject(patientLien);
            log.debug("Modification objet PatientLien " + patientLien.toString());
         }else{
            log.warn("Doublon lors " + operation + " objet PatientLien " + patientLien.toString());
            throw new DoublonFoundException("PatientLien", operation);
         }
      }
   }

   @Override
   public void removeObjectManager(final PatientLien patientLien){
      if(patientLien != null){
         patientLienDao.removeObject(patientLien.getPk());
         log.debug("Suppression objet PatientLien " + patientLien.toString());
      }else{
         log.warn("Suppression d'un PatientLien null");
      }
   }

   @Override
   public boolean findDoublonManager(final PatientLien patientLien){
      // l'utilisation de excludedId impossible a cause de la clef composite
      return patientLienDao.findAll().contains(patientLien);
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes
    * sont non nulls.
    * @param patientLien
    * @param patient1
    * @param patient2
    * @param lienFamilial
    * @param operation demandant la verification
    */
   private void checkRequiredObjectsAndValidate(final PatientLien patientLien, final Patient patient1, final Patient patient2,
      final LienFamilial lienFamilial, final String operation){
      //patient1 required
      if(patient1 != null){
         patientLien.setPatient1(patientDao.mergeObject(patient1));
      }else if(patientLien.getPatient1() == null){
         log.warn("Objet obligatoire Patient1 manquant" + " lors de la " + operation + " d'un PatientLien");
         throw new RequiredObjectIsNullException("PatientLien", operation, "Patient1");
      }
      //patient2 required
      if(patient2 != null){
         patientLien.setPatient2(patientDao.mergeObject(patient2));
      }else if(patientLien.getPatient2() == null){
         log.warn("Objet obligatoire Patient2 manquant" + " lors de la " + operation + " d'un PatientLien");
         throw new RequiredObjectIsNullException("PatientLien", operation, "Patient2");
      }
      //LienFamilial required
      if(lienFamilial != null){
         patientLien.setLienFamilial(lienFamilialDao.mergeObject(lienFamilial));
      }else if(patientLien.getLienFamilial() == null){
         log.warn("Objet obligatoire LienFamilial  manquant" + " lors de la " + operation + " d'un PatientLien");
         throw new RequiredObjectIsNullException("PatientLien", operation, "LienFamilial");
      }

   }
}
