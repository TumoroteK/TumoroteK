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
package fr.aphp.tumorotek.manager.validation.stockage;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.stockage.Terminale;

public class TerminaleValidator implements Validator
{

   @Override
   public boolean supports(final Class<?> clazz){
      return Terminale.class.equals(clazz);
   }

   @Override
   public void validate(final Object target, final Errors errors){

      final Terminale terminale = (Terminale) target;

      // nom non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nom", "terminale.nom.empty");
      //nom valide
      if(terminale.getNom() != null){
         if(!terminale.getNom().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("nom", "terminale.nom.illegal");
         }
         if(terminale.getNom().length() > 50){
            errors.rejectValue("nom", "terminale.nom.tooLong");
         }
      }

      //position valide
      if(terminale.getPosition() != null){
         if(terminale.getPosition() < 1){
            errors.rejectValue("position", "terminale.position.illegal");
         }
      }else{
         errors.rejectValue("position", "terminale.position.null");
      }

      //alias valide
      if(terminale.getAlias() != null){
         if(terminale.getAlias().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("alias", "terminale.alias.empty");
         }
         if(!terminale.getAlias().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("alias", "terminale.alias.illegal");
         }
         if(terminale.getAlias().length() > 50){
            errors.rejectValue("alias", "terminale.alias.tooLong");
         }
      }
   }
}
