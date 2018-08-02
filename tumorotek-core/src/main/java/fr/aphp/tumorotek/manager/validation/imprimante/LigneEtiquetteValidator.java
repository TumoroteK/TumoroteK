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
package fr.aphp.tumorotek.manager.validation.imprimante;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;

public class LigneEtiquetteValidator implements Validator
{

   
   @Override
   public boolean supports(final Class<?> clazz){
      return LigneEtiquette.class.equals(clazz);
   }

   @Override
   public void validate(final Object target, final Errors errors){

      final LigneEtiquette ligneEtiquette = (LigneEtiquette) target;

      //entete valide
      if(ligneEtiquette.getEntete() != null){
         if(ligneEtiquette.getEntete().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("entete", "ligneEtiquette.entete.empty");
         }
         if(!ligneEtiquette.getEntete().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("entete", "ligneEtiquette.entete.illegal");
         }
         if(ligneEtiquette.getEntete().length() > 25){
            errors.rejectValue("entete", "ligneEtiquette.entete.tooLong");
         }
      }

      //contenu valide
      if(ligneEtiquette.getContenu() != null){
         if(ligneEtiquette.getContenu().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("contenu", "ligneEtiquette.contenu.empty");
         }
         if(!ligneEtiquette.getContenu().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("contenu", "ligneEtiquette.contenu.illegal");
         }
         if(ligneEtiquette.getContenu().length() > 50){
            errors.rejectValue("contenu", "ligneEtiquette.contenu.tooLong");
         }
      }

      //font valide
      if(ligneEtiquette.getFont() != null){
         if(ligneEtiquette.getFont().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("font", "ligneEtiquette.font.empty");
         }
         if(!ligneEtiquette.getFont().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("font", "ligneEtiquette.font.illegal");
         }
         if(ligneEtiquette.getFont().length() > 25){
            errors.rejectValue("font", "ligneEtiquette.font.tooLong");
         }
      }

      //style valide
      if(ligneEtiquette.getStyle() != null){
         if(ligneEtiquette.getStyle().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errors.rejectValue("style", "ligneEtiquette.style.empty");
         }
         if(!ligneEtiquette.getStyle().matches(ValidationUtilities.MOTREGEXP)){
            errors.rejectValue("style", "ligneEtiquette.style.illegal");
         }
         if(ligneEtiquette.getStyle().length() > 25){
            errors.rejectValue("style", "ligneEtiquette.style.tooLong");
         }
      }

      //ordre valide
      if(ligneEtiquette.getOrdre() == null){
         errors.rejectValue("ordre", "ligneEtiquette.ordre.empty");
      }else{
         if(ligneEtiquette.getOrdre() < 1){
            errors.rejectValue("ordre", "ligneEtiquette.ordre.illegal");
         }
      }

      //size valide
      if(ligneEtiquette.getSize() != null && ligneEtiquette.getSize() < 1){
         errors.rejectValue("size", "ligneEtiquette.size.illegal");
      }
   }

}
