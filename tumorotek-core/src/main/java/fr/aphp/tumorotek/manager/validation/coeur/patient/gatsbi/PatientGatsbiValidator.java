/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.validation.coeur.patient.gatsbi;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import fr.aphp.tumorotek.manager.validation.RequiredValueValidator;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.patient.Patient;

/**
 * Gatsbi validor appliquant de manière dynamique une validation sur les
 * champs obligatoires définis par un contexte.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class PatientGatsbiValidator extends RequiredValueValidator
{
   
   public PatientGatsbiValidator(final String _e, final List<Integer> _f){
      super(_e, _f);
   }

   @Override
   public boolean supports(final Class<?> clazz){
      return Patient.class.equals(clazz);
   }
   
   @Override
   public void validate(Object target, Errors errs){

      final Patient patient = (Patient) target;

      //identifiant obligatoire
      if (patient.getPatientIdentifiant() == null || StringUtils.isBlank(patient.getIdentifiant())) {
         errs.rejectValue("identifiant", "patient.identifiant.empty");
      } else { //identifiant valide
         if(!patient.getIdentifiant().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("identifiant", "patient.identifiant.illegal");
         }
         if(patient.getIdentifiant().length() > 20){
            errs.rejectValue("identifiant", "patient.identifiant.tooLong");
         }
      }
      super.validate(target, errs);
   }

   @Override
   protected void initChpIdNameMap(){
      chpIdNameMap.put(2, "nip");
      chpIdNameMap.put(3, "nom"); 
      chpIdNameMap.put(4, "nomNaissance");
      chpIdNameMap.put(5, "prenom");
      chpIdNameMap.put(6, "sexe");
      chpIdNameMap.put(7, "dateNaissance");
      chpIdNameMap.put(8, "villeNaissance");
      chpIdNameMap.put(9, "paysNaissance");
      chpIdNameMap.put(10, "patientEtat");
      chpIdNameMap.put(11, "dateEtat");
   }

   @Override
   protected void initFunctionalValidationMap(){}

   @Override
   protected String correctFieldNameIfNeeded(final String n){
      // pas de transformation du nom de la propriété
      return n;
   }
}
