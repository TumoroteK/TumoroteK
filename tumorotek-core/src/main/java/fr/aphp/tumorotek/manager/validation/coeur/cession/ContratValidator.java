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
package fr.aphp.tumorotek.manager.validation.coeur.cession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.cession.Contrat;

/**
 * Validator pour le bean domaine Contrat.<br>
 * Classe creee le 28/01/10<br>
 * <br>
 * Regles de validation:<br>
 * 	- le champ numero doit etre non vide, non null, et valide litteralement et
 * 		de taille inferieure à 30<br>
 *
 * @author Pierre VENTADOUR.
 * @version 2.0
 */
public class ContratValidator implements Validator
{

   @Override
   public boolean supports(final Class<?> clazz){
      return Contrat.class.equals(clazz);
   }

   @Override
   public void validate(final Object target, final Errors errors){

      final Contrat contrat = (Contrat) target;

      //numero non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numero", "contrat.numero.empty");
      //numero valide
      if(contrat.getNumero() != null){
         if(!contrat.getNumero().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("numero", "contrat.numero.illegal");
         }
         if(contrat.getNumero().length() > 50){
            errors.rejectValue("numero", "contrat.numero.tooLong");
         }
      }

      //titre valide
      if(contrat.getTitreProjet() != null){
         if(contrat.getTitreProjet().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("titreProjet", "contrat.titreProjet.empty");
         }
         if(!contrat.getTitreProjet().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("titreProjet", "contrat.titreProjet.illegal");
         }
         if(contrat.getTitreProjet().length() > 100){
            errors.rejectValue("titreProjet", "contrat.titreProjet.tooLong");
         }
      }

      //description valide
      if(contrat.getDescription() != null){
         if(contrat.getDescription().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("description", "contrat.description.empty");
         }
      }

      //date cohérences
      errors.addAllErrors(checkDemandeDateCoherence(contrat));
      errors.addAllErrors(checkDateValidation(contrat));
      errors.addAllErrors(checkDateDemandeRedaction(contrat));
      errors.addAllErrors(checkDateEnvoiContrat(contrat));
      errors.addAllErrors(checkDateSignature(contrat));
   }

   /**
    * Vérifie la cohérence de la date de demande de collaboration avec les
    * dates de validation, rédaction, envoi et signature. 
    * @param contrat
    * @return Errors
    */
   public static Errors checkDemandeDateCoherence(final Contrat contrat){

      final BindException errs = new BindException(contrat, "fr.aphp.tumorotek.model.cession.Contrat");

      if(contrat.getDateDemandeCession() != null){
         if(contrat.getDateValidation() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeCession(), "dateDemandeCession", contrat.getDateValidation(),
               "date", "validation", "DateValidation", errs, true);
         }else if(contrat.getDateDemandeRedaction() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeCession(), "dateDemandeCession",
               contrat.getDateDemandeRedaction(), "date", "validation", "DateDemandeRedaction", errs, true);
         }else if(contrat.getDateEnvoiContrat() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeCession(), "dateDemandeCession",
               contrat.getDateEnvoiContrat(), "date", "validation", "DateEnvoiContrat", errs, true);
         }else if(contrat.getDateSignature() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeCession(), "dateDemandeCession", contrat.getDateSignature(),
               "date", "validation", "DateSignature", errs, true);
         }
      }
      return errs;
   }

   /**
    * Vérifie la cohérence de la date de validation du projet avec les
    * dates de demande, rédaction, envoi et signature. 
    * @param contrat
    * @return Errors
    */
   public static Errors checkDateValidation(final Contrat contrat){

      final BindException errs = new BindException(contrat, "fr.aphp.tumorotek.model.cession.Contrat");

      if(contrat.getDateValidation() != null){
         // limites inf
         if(contrat.getDateDemandeCession() != null){
            ValidationUtilities.checkWithDate(contrat.getDateValidation(), "dateValidation", contrat.getDateDemandeCession(),
               "date", "validation", "DemandeCession", errs, false);
         }
      }
      return errs;
   }

   /**
    * Vérifie la cohérence de la date de rédaction du projet avec les
    * dates de demande, validation, envoi et signature. 
    * @param contrat
    * @return Errors
    */
   public static Errors checkDateDemandeRedaction(final Contrat contrat){

      final BindException errs = new BindException(contrat, "fr.aphp.tumorotek.model.cession.Contrat");

      if(contrat.getDateDemandeRedaction() != null){
         // limites inf
         if(contrat.getDateDemandeCession() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeRedaction(), "dateDemandeRedaction",
               contrat.getDateDemandeCession(), "date", "validation", "DemandeCession", errs, false);
         }

         // limites sup
         if(contrat.getDateEnvoiContrat() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeRedaction(), "dateDemandeRedaction",
               contrat.getDateEnvoiContrat(), "date", "validation", "DateEnvoiContrat", errs, true);
         }else if(contrat.getDateSignature() != null){
            ValidationUtilities.checkWithDate(contrat.getDateDemandeRedaction(), "dateDemandeRedaction",
               contrat.getDateSignature(), "date", "validation", "DateSignature", errs, true);
         }
      }
      return errs;
   }

   /**
    * Vérifie la cohérence de la date d'envoi du contra avec les
    * dates de demande, validation, rédaction et signature. 
    * @param contrat
    * @return Errors
    */
   public static Errors checkDateEnvoiContrat(final Contrat contrat){

      final BindException errs = new BindException(contrat, "fr.aphp.tumorotek.model.cession.Contrat");

      if(contrat.getDateEnvoiContrat() != null){
         // limites inf
         if(contrat.getDateDemandeRedaction() != null){
            ValidationUtilities.checkWithDate(contrat.getDateEnvoiContrat(), "dateEnvoiContrat",
               contrat.getDateDemandeRedaction(), "date", "validation", "DateDemandeRedaction", errs, false);
         }else if(contrat.getDateDemandeCession() != null){
            ValidationUtilities.checkWithDate(contrat.getDateEnvoiContrat(), "dateEnvoiContrat", contrat.getDateDemandeCession(),
               "date", "validation", "DemandeCession", errs, false);
         }

         // limites sup
         if(contrat.getDateSignature() != null){
            ValidationUtilities.checkWithDate(contrat.getDateEnvoiContrat(), "dateEnvoiContrat", contrat.getDateSignature(),
               "date", "validation", "DateSignature", errs, true);
         }
      }
      return errs;
   }

   /**
    * Vérifie la cohérence de la date de signature avec les
    * dates de demande, validation, rédaction et d'envoi. 
    * @param contrat
    * @return Errors
    */
   public static Errors checkDateSignature(final Contrat contrat){

      final BindException errs = new BindException(contrat, "fr.aphp.tumorotek.model.cession.Contrat");

      if(contrat.getDateSignature() != null){
         // limites inf
         if(contrat.getDateEnvoiContrat() != null){
            ValidationUtilities.checkWithDate(contrat.getDateSignature(), "dateSignature", contrat.getDateEnvoiContrat(), "date",
               "validation", "DateEnvoiContrat", errs, false);
         }else if(contrat.getDateDemandeRedaction() != null){
            ValidationUtilities.checkWithDate(contrat.getDateSignature(), "dateSignature", contrat.getDateDemandeRedaction(),
               "date", "validation", "DateDemandeRedaction", errs, false);
         }else if(contrat.getDateDemandeCession() != null){
            ValidationUtilities.checkWithDate(contrat.getDateSignature(), "dateSignature", contrat.getDateDemandeCession(),
               "date", "validation", "DemandeCession", errs, false);
         }
      }
      return errs;
   }

}
