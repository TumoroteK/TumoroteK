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
package fr.aphp.tumorotek.manager.validation.io.export;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.model.io.export.Critere;

/**
 * Validator pour le bean domaine Critere.
 * Classe créée le 05/11/09
 *
 * Regles de validation:
 * 	- le champ operateur doit être positif et non null
 *  - le champ valeur doit être non vide et non null
 *  - le champ ou la combinaison doit être null
 *
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 */
public class CritereValidator implements Validator
{

   
   @Override
   public boolean supports(final Class<?> clazz){
      return Critere.class.equals(clazz);
   }

   @Override
   public void validate(final Object obj, final Errors errs){
      //valeur non vide
      //ValidationUtils.rejectIfEmptyOrWhitespace(errs, "valeur",
      //	"critere.valeur.empty");

      final Critere critere = (Critere) obj;
      //operateur valide
      if(critere.getOperateur() == null){
         errs.rejectValue("operateur", "critere.operateur.empty");
      }

      //critere valide
      if(critere.getValeur() != null){
         if(critere.getValeur().length() > 40){
            errs.rejectValue("valeur", "critere.valeur.tooLong");
         }
      }

      //le critere doit contenir soit un champ soit une combinaison
      // (ni les deux, ni aucun)
      //le champ doit être soit un champAnnotation, soit un champEntite
      // (ni les deux, ni aucun)
      String type = null;
      if(critere.getChamp() == null){
         if(critere.getCombinaison() == null){
            errs.rejectValue("champ", "critere.champ.isNotNullAsCombinaison");
         }else{
            // On recupere le type de la combinaison
            if(critere.getCombinaison().getChamp1().getChampAnnotation() != null){
               type = critere.getCombinaison().getChamp1().getChampAnnotation().getDataType().getType();
            }else if(critere.getCombinaison().getChamp1().getChampEntite() != null){
               if(critere.getOperateur() != null){
                  type = critere.getCombinaison().getChamp1().getChampEntite().getDataType().getType();
               }
            }
         }

      }else if(critere.getCombinaison() != null){
         errs.rejectValue("champ", "critere.champ.isNotNullAsCombinaison");
      }else{
         // On recupere le type du champ
         if(critere.getChamp().getChampAnnotation() != null){
            type = critere.getChamp().getChampAnnotation().getDataType().getType();
         }else if(critere.getChamp().getChampEntite() != null){
            if(critere.getOperateur() != null){
               type = critere.getChamp().getChampEntite().getDataType().getType();
            }
         }
      }
      if(type != null && critere.getOperateur() != null){
         if(type.equals("num") || type.equals("text")){
            if(!critere.getOperateur().equals("<") && !critere.getOperateur().equals("<=") && !critere.getOperateur().equals(">")
               && !critere.getOperateur().equals(">=") && !critere.getOperateur().equals("=")
               && !critere.getOperateur().equals("!=") && !critere.getOperateur().equals("like")
               && !critere.getOperateur().equals("not like") && !critere.getOperateur().equals("contains")
               && !critere.getOperateur().equals("is null")){
               errs.rejectValue("operateur", "critere.operateur.illegal");
            }

         }else if(type.matches("date.*")){
            if(!critere.getOperateur().equals("<") && !critere.getOperateur().equals("<=") && !critere.getOperateur().equals(">")
               && !critere.getOperateur().equals(">=") && !critere.getOperateur().equals("=")
               && !critere.getOperateur().equals("!=") && !critere.getOperateur().equals("is null")){
               errs.rejectValue("operateur", "critere.operateur.illegal");
            }
         }else{
            if(!critere.getOperateur().equals("=") && !critere.getOperateur().equals("!=")
               && !critere.getOperateur().equals("like") && !critere.getOperateur().equals("not like")
               && !critere.getOperateur().equals("is null")){
               errs.rejectValue("operateur", "critere.operateur.illegal");
            }
         }
      }
   }
}
