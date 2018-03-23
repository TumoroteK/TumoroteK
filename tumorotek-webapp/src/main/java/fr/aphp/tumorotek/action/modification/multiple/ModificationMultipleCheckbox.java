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
package fr.aphp.tumorotek.action.modification.multiple;

import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Image;
import org.zkoss.zul.Radiogroup;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'un
 * Checkbox.
 * Classe créée le 28/04/10 à partir de ModificationMultipleDatebox
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class ModificationMultipleCheckbox extends AbstractModificationMultipleComponent
{

   private static final long serialVersionUID = 4487271130091905174L;

   /**
    * Components specific checkbox.
    */
   private Checkbox multiCheckbox;
   private Radiogroup eraseMultiCheckbox;
   private Image delete;

   private boolean nullified = false;

   @Override
   public void setConstraintsToBoxes(final Constraint constr){}

   @Override
   public String formatLocalObject(final Object obj){
      return ObjectTypesFormatters.booleanLitteralFormatter((Boolean) obj);
   }

   @Override
   public Object extractValueFromEraserBox(){
      if(eraseMultiCheckbox.getSelectedItem() != null){
         return eraseMultiCheckbox.getSelectedItem().getValue();
      }else{
         return null;
      }
   }

   @Override
   public Object extractValueFromMultiBox(){
      if(!nullified){
         return new Boolean(multiCheckbox.isChecked());
      }else{
         return null;
      }
   }

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      eraseMultiCheckbox.setVisible(visible);
   }

   @Override
   public void initComponentsInWindow(){
      super.initComponentsInWindow();

      // affiche le bouton delete
      if(getStringValues().size() == 1){
         if(!getStringValues().get(0).equals("")){
            delete.setVisible(true);
         }
      }
   }

   /**
    * Efface la valeur du boolean.
    */
   public void onClick$delete(){
      setNewValue(null);
      delete.setVisible(false);
      multiCheckbox.setDisabled(true);
      nullified = true;
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationCheckbox", true));
   }

   @Override
   public void passValueToEraserBox(){}

   @Override
   public void passNullToEraserBox(){}
}
