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

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * Classes parentes de toutes les classes de contraintes portant sur
 * des champ de type textes, alphanumeriques.
 * Validation:<br>
 *  - sur la taille max (eventuelle)<br>
 *  - sur la taille min (eventuelle)<br>
 *  - sur la syntaxe (obligatoire)<br>
 * Date: 26/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class TumoTextConstraint
{

   private Integer size;

   private Boolean nullable = false;

   public TumoTextConstraint(){}

   public Integer getSize(){
      return size;
   }

   public Boolean getNullable(){
      return nullable;
   }

   public void setSize(final Integer s){
      this.size = s;
   }

   public void setNullable(final Boolean n){
      this.nullable = n;
   }

   /**
    * Applique la validation textBox par match avec la regexp.
    */
   public void validateWordOrCode(final Component comp, final Object value, String regexp, final String errorLabel,
      final Constraint constr){
      // on récupère la valeur dans textBox
      final String textValue = (String) value;
      // Si le text n'est pas vide, on applique la contrainte
      try{
         if(textValue != null){
            boolean isValide = true;
            // on vérifie que cet attribut est bien de type "size"
            if(this.size != null){
               // validité sur la taille
               if(textValue.length() > this.size){
                  throw new WrongValueException(comp,
                     ObjectTypesFormatters.getLabel(errorLabel + "tooLong", new String[] {this.size.toString()}));
               }
            }
            if(!nullable){
               // validité sur la présence
               if(textValue.equals("")){
                  throw new WrongValueException(comp, Labels.getLabel(errorLabel + "empty"));
               }
            }else if(regexp != null){
               regexp = regexp.substring(0, regexp.length() - 1).concat("*");
            }
            if(regexp != null){
               isValide = textValue.matches(regexp);
            }

            // validité sur la syntaxe
            if(!isValide){
               throw new WrongValueException(comp, Labels.getLabel(errorLabel + "illegal"));
            }

         }else{
            // la contrainte est retiree
            ((Textbox) comp).setConstraint("");
            ((Textbox) comp).clearErrorMessage(true);
            ((Textbox) comp).setValue(null);
            // on remet la contrainte
            ((Textbox) comp).setConstraint(constr);
         }
      }catch(final WrongValueException e){
         Clients.scrollIntoView(e.getComponent());
         throw (e);
      }
   }
}
