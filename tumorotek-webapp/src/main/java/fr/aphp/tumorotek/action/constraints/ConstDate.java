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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;

/**
 * Contrainte vérifiant que la date entrée sans l'assistant
 * est valide litteralement suivant ValidationUtils regexps.
 * Date: 26/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class ConstDate implements Constraint
{

   @Override
   public void validate(final Component comp, final Object value){
      validateDate(comp, value);
   }

   /**
    * Applique la validation sur la date par regexp choisie en fonction
    * de la locale, retrouvee à partir du format appliqué au Datebox.
    * @param comp
    * @param value
    */
   private void validateDate(final Component comp, final Object value){
      // on récupère la valeur dans la DateBox
      final Date dateValue = (Date) value;
      // Si le text n'est pas vide, on applique la contrainte
      if(dateValue != null && !dateValue.equals("")){
         final boolean isValide = true;
         if(((Datebox) comp).getFormat().equals("dd/MM/yyyy hh:mm:ss")){
            //isValide = dateValue.matches(DATE_FR_REGEXP);
            new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(dateValue);
         }else if(((Datebox) comp).getFormat().equals("MM/dd/yyyy hh:mm:ss")){
            //isValide = dateValue.matches(DATE_EN_REGEXP);
            new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(dateValue);
         }
         if(!isValide){
            throw new WrongValueException(comp, Labels.getLabel("validation.invalid.date"));
         }
      }else{
         // la contrainte est retiree
         ((Datebox) comp).setConstraint("");
         ((Datebox) comp).clearErrorMessage(true);
         ((Datebox) comp).setValue(null);
         // on remet la contrainte
         ((Datebox) comp).setConstraint(this);
      }
   }
}
