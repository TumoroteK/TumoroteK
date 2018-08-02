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
package fr.aphp.tumorotek.manager.validation.contexte;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Validator pour le bean domaine Banque (de contexte).
 * Classe creee le 15/10/10
 *
 * Regles de validation:
 * 	- le champ nom doit etre non vide, non null
 *  - les nom, identification, description et maladie defaut
 *  	doivent avoir une synthaxe valide
 *  - defMaladie ne peut être null
 *  - maladieDefaut ne peut être non null que si defMaladie true
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class BanqueValidator implements Validator
{

   @Override
   public boolean supports(final Class<?> clazz){
      return Banque.class.equals(clazz);
   }

   @Override
   public void validate(final Object target, final Errors errors){
      // nom non vide
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nom", "banque.nom.empty");

      final Banque bank = (Banque) target;
      // nom valide
      if(bank.getNom() != null){
         if(!bank.getNom().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("nom", "banque.nom.illegal");
         }
         if(bank.getNom().length() > 100){
            errors.rejectValue("nom", "banque.nom.tooLong");
         }
      }

      // identification valide
      if(bank.getIdentification() != null){
         if(bank.getIdentification().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("identification", "banque.identification.empty");
         }
         if(!bank.getIdentification().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("identification", "banque.identification.illegal");
         }
         if(bank.getIdentification().length() > 50){
            errors.rejectValue("identification", "banque.identification.tooLong");
         }
      }

      // description valide
      if(bank.getDescription() != null){
         if(bank.getDescription().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("description", "banque.description.empty");
         }
         //			if (bank.getDescription().length() > 250) {
         //				errors.rejectValue("description", 
         //									"banque.description.tooLong");
         //			}
      }

      // defMaladie not null
      if(bank.getDefMaladies() == null){
         errors.rejectValue("defMaladies", "banque.defMaladies.empty");
      }

      // maladie defaut valide
      if(bank.getDefautMaladie() != null){
         if(!bank.getDefMaladies()){
            errors.rejectValue("defautMaladie", "banque.defautMaladie.noDefMaladie");
         }
         if(bank.getDefautMaladie().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("defautMaladie", "banque.defautMaladie.empty");
         }
         if(!bank.getDefautMaladie().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("defautMaladie", "banque.defautMaladie.illegal");
         }
         if(bank.getDefautMaladie().length() > 250){
            errors.rejectValue("defautMaladie", "banque.defautMaladie.tooLong");
         }
      }

      if(bank.getDefautMaladieCode() != null){
         if(!bank.getDefMaladies()){
            errors.rejectValue("defautMaladieCode", "banque.defautMaladieCode.noDefMaladie");
         }
         if(bank.getDefautMaladieCode().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("defautMaladieCode", "banque.defautMaladieCode.empty");
         }
         if(!bank.getDefautMaladieCode().matches(ValidationUtilities.CODEREGEXP)){
            errors.rejectValue("defautMaladieCode", "banque.defautMaladieCode.illegal");
         }
         if(bank.getDefautMaladieCode().length() > 50){
            errors.rejectValue("defautMaladieCode", "banque.defautMaladieCode.tooLong");
         }
      }
   }
}
