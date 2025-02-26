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
package fr.aphp.tumorotek.action.constraints;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

/**
 * Classe gérant la validité des mots de passe.
 * @author pierre
 *
 */
public class TumoPasswordConstraint
{

   private Integer minSize;

   private Integer maxSize;

   public TumoPasswordConstraint(){

   }

   public Integer getMinSize(){
      return minSize;
   }

   public void setMinSize(final Integer s){
      this.minSize = s;
   }

   public Integer getMaxSize(){
      return maxSize;
   }

   public void setMaxSize(final Integer s){
      this.maxSize = s;
   }

   /**
    * Valide le mot de passe en fonction des critères définis.
    *
    * <p>Critères de validation :
    * <ul>
    *   <li>La taille minimale et maximale du mot de passe (définie dans {@code UtilisateurConstraints}).</li>
    *   <li>Doit contenir au moins une lettre minuscule.</li>
    *   <li>Doit contenir au moins une lettre majuscule.</li>
    *   <li>Doit contenir au moins un chiffre.</li>
    *   <li>Doit contenir au moins un caractère spécial.</li>
    * </ul>
    * </p>
    *
    * @param comp   Le composant associé au champ du mot de passe.
    * @param value  La valeur saisie par l'utilisateur.
    * @param constr La contrainte associée au champ.
    * @throws WrongValueException Si le mot de passe ne respecte pas les critères définis.
    */
   public void validatePassword(final Component comp, final Object value, final Constraint constr){
      // on récupère la valeur dans textBox
      final String textValue = (String) value;
      try{
         // Vérifier si textValue n'est pas null
         if(textValue != null){

            // Valider la taille minimale (définie dans UtilisateurConstraints)
            if(this.minSize != null && textValue.length() < this.minSize){
               throw new WrongValueException(comp, Labels.getLabel("validation.password.illegal"));
            }

            // Valider la taille maximale (définie dans UtilisateurConstraints)
            if(this.maxSize != null && textValue.length() > this.maxSize){
               throw new WrongValueException(comp, Labels.getLabel("validation.password.illegal"));
            }

            // Vérifier les types de caractères requis
            boolean hasLowercase = false;
            boolean hasUppercase = false;
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean validationOK = false;

            for(char c : textValue.toCharArray()){
               if(Character.isLowerCase(c))
                  hasLowercase = true;
               else if(Character.isUpperCase(c))
                  hasUppercase = true;
               else if(Character.isDigit(c))
                  hasDigit = true;
               else if(!Character.isLetterOrDigit(c))
                  hasSpecialChar = true;

               // Optimisation : arrêter la vérification dès que toutes les conditions sont remplies
               if(hasLowercase && hasUppercase && hasDigit && hasSpecialChar) {
                  validationOK  = true;
                  break;
               }
            }
            // Si une condition n'est pas remplie, lever une exception
            if(!validationOK){
               throw new WrongValueException(comp, Labels.getLabel("validation.password.illegal"));
            }
         }else{
            // la contrainte est retiree
            ((Textbox) comp).setConstraint("");
            ((Textbox) comp).clearErrorMessage(true);
            ((Textbox) comp).setValue(null);
            // on remet la contrainte
            ((Textbox) comp).setConstraint(constr);
         }
      } catch(final WrongValueException e){
         Clients.scrollIntoView(e.getComponent());
         throw (e);
      }

   }


}
