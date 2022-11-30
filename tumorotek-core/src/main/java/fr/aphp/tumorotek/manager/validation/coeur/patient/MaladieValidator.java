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
package fr.aphp.tumorotek.manager.validation.coeur.patient;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Validator pour le bean domaine Maladie.<br>
 * Classe creee le 30/10/09<br>
 * <br>
 * Regles de validation:<br>
 * 	- le champ libelle doit etre non vide, non null, et valide litteralement et
 * 		de taille inferieure à 250<br>
 * 	- le champ code doit etre null ou valide litteralement,
 * 			et de taille inferieure à 20<br>
 * 	- le champ diagnostic doit etre null,
 * 		ou supérieure ou égale à la date de naissance
 * 		et inférieure ou égale à la date actuelle<br>
 *  - le champ date debut doit etre null,
 *  	ou supérieure ou égale à la date de naissance,
 *  	inferieure ou égale a la date diagnostic (soit inférieure ou égale à
 * 		la date actuelle si date diagnostic nulle),
 * 		et inferieure ou égale une eventuelle date de deces.<br>
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class MaladieValidator implements Validator
{

   @Override
   public boolean supports(final Class<?> clazz){
      return Maladie.class.equals(clazz);
   }

   @Override
   public void validate(final Object obj, final Errors errs){

      final Maladie maladie = (Maladie) obj;

      //libelle non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "libelle", "maladie.libelle.empty");
      //libelle valide
      if(maladie.getLibelle() != null){
         if(!maladie.getLibelle().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("libelle", "maladie.libelle.illegal");
         }
         if(maladie.getLibelle().length() > 300){
            errs.rejectValue("libelle", "maladie.libelle.tooLong");
         }
      }

      //code valide
      if(maladie.getCode() != null){
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "code", "maladie.code.empty");
         if(!maladie.getCode().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("code", "maladie.code.illegal");
         }
         if(maladie.getCode().length() > 50){
            errs.rejectValue("code", "maladie.code.tooLong");
         }
      }

      //date debut cohérente
      errs.addAllErrors(checkDateDebutCoherence(maladie));

      //date diagnostic cohérente
      errs.addAllErrors(checkDateDiagCoherence(maladie));
   }

   /**
    * Vérifie la cohérence de la date de début de la maladie avec les
    * dates de diagnostic, naissance et décès.
    * @param maladie
    * @return errors
    */
   public Errors checkDateDebutCoherence(final Maladie maladie){

      final BindException errs = new BindException(maladie, "fr.aphp.tumorotek.model.coeur.patient.Maladie");

      if(maladie.getDateDebut() != null){
         if(maladie.getPatient().getDateNaissance() != null
            && !(maladie.getDateDebut().after(maladie.getPatient().getDateNaissance())
               || maladie.getDateDebut().equals(maladie.getPatient().getDateNaissance()))){
            errs.rejectValue("dateDebut", "date.validation.infDateNaissance");
         }

         if(maladie.getPatient().getDateDeces() != null && !(maladie.getDateDebut().before(maladie.getPatient().getDateDeces())
            || maladie.getDateDebut().equals(maladie.getPatient().getDateDeces()))){
            errs.rejectValue("dateDebut", "date.validation.supDateDeces");
         }else{
            if(maladie.getDateDiagnostic() != null){
               if(!(maladie.getDateDebut().before(maladie.getDateDiagnostic())
                  || maladie.getDateDebut().equals(maladie.getDateDiagnostic()))){
                  errs.rejectValue("dateDebut", "maladie.dateDebut.supDateDiagnostic");
               }
            }else{
               if(!maladie.getDateDebut().before(Utils.getCurrentSystemDate())){
                  errs.rejectValue("dateDebut", "date.validation.supDateActuelle");
               }
            }
         }
      } // else { // melbase hack
        //errs.rejectValue("dateDebut", "malade.dateDebut.empty");
        //}
      return errs;
   }

   /**
    * Vérifie la cohérence de la date de diagnostic de la maladie avec les
    * dates de début de la maladie, naissance et décès.
    * @param maladie
    * @return Errors
    */
   public Errors checkDateDiagCoherence(final Maladie maladie){

      final BindException errs = new BindException(maladie, "fr.aphp.tumorotek.model.coeur.patient.Maladie");

      if(maladie.getDateDiagnostic() != null){
         if(maladie.getDateDebut() != null){
            if(!(maladie.getDateDiagnostic().after(maladie.getDateDebut())
               || maladie.getDateDiagnostic().equals(maladie.getDateDebut()))){
               errs.rejectValue("dateDiagnostic", "maladie.dateDiagnostic.infDateDebut");
            }
         }else{
            if(maladie.getPatient().getDateNaissance() != null
               && !(maladie.getDateDiagnostic().after(maladie.getPatient().getDateNaissance())
                  || maladie.getDateDiagnostic().equals(maladie.getPatient().getDateNaissance()))){
               errs.rejectValue("dateDiagnostic", "datedate.validation.infDateNaissance");
            }
         }

         if(!maladie.getDateDiagnostic().before(Utils.getCurrentSystemDate())){
            errs.rejectValue("dateDiagnostic", "date.validation.supDateActuelle");
         }
      }
      return errs;
   }
}
