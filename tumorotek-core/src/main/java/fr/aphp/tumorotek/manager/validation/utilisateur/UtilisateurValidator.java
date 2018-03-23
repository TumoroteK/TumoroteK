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
package fr.aphp.tumorotek.manager.validation.utilisateur;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

public class UtilisateurValidator implements Validator
{

   @Override
   public boolean supports(final Class<?> clazz){
      return Utilisateur.class.equals(clazz);
   }

   @Override
   public void validate(final Object target, final Errors errors){

      final Utilisateur utilisateur = (Utilisateur) target;

      // login non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "utilisateur.login.empty");
      //login valide
      if(utilisateur.getLogin() != null){
         if(!utilisateur.getLogin().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("login", "utilisateur.login.illegal");
         }
         if(utilisateur.getLogin().length() > 100){
            errors.rejectValue("login", "utilisateur.login.tooLong");
         }
      }

      // password non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "utilisateur.password.empty");
      //password valide
      if(utilisateur.getPassword() != null){
         if(utilisateur.getPassword().length() > 100){
            errors.rejectValue("password", "utilisateur.password.tooLong");
         }
      }

      //dnLdap valide
      if(utilisateur.getDnLdap() != null){
         if(utilisateur.getDnLdap().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("dnLdap", "utilisateur.dnLdap.empty");
         }
         if(!utilisateur.getDnLdap().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("dnLdap", "utilisateur.dnLdap.illegal");
         }
         if(utilisateur.getDnLdap().length() > 100){
            errors.rejectValue("dnLdap", "utilisateur.dnLdap.tooLong");
         }
      }

      //encodedPassword valide
      if(utilisateur.getEncodedPassword() != null){
         if(utilisateur.getEncodedPassword().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("encodedPassword", "utilisateur.encodedPassword.empty");
         }
         if(!utilisateur.getEncodedPassword().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("encodedPassword", "utilisateur.encodedPassword.illegal");
         }
         if(utilisateur.getEncodedPassword().length() > 100){
            errors.rejectValue("encodedPassword", "utilisateur.encodedPassword.tooLong");
         }
      }

      //email valide
      if(utilisateur.getEmail() != null){
         if(utilisateur.getEmail().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("email", "utilisateur.email.empty");
         }
         if(!utilisateur.getEmail().matches(ValidationUtilities.MAILREGEXP)){
            errors.rejectValue("email", "utilisateur.email.illegal");
         }
         if(utilisateur.getEmail().length() > 50){
            errors.rejectValue("email", "utilisateur.email.tooLong");
         }
      }

      //errors.addAllErrors(checkDateDesactCoherence(utilisateur));

   }

   /**
    * Vérifie la cohérence de la date de desactivation avec la date actuelle.
    * @param utilisateur
    * @return Errors
    */
   public static Errors checkDateDesactCoherence(final Utilisateur user){

      final BindException errs = new BindException(user, "fr.aphp.tumorotek.model.utilisateur.Utilisateur");

      if(user.getTimeOut() != null){
         if(user.getTimeOut().before(Utils.getCurrentSystemDate())){
            errs.rejectValue("timeOut", "date.validation.infDateActuelle");
         }
      }
      return errs;
   }

}
