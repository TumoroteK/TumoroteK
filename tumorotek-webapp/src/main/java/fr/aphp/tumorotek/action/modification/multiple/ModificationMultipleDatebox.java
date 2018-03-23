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

import java.util.Calendar;
import java.util.Date;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'un
 * Datebox.
 * Classe créée le 15/03/09. Modifiée par Mathieu pour utiliser
 * factorisation AbstractModificationMultipleComponent.
 *
 * @author Pierre Ventadour
 * @version 2.0
 */
public class ModificationMultipleDatebox extends AbstractModificationMultipleComponent
{

   private static final long serialVersionUID = 4487271130091905174L;

   private Datebox multiDatebox;
   private Datebox eraseMultiDatebox;

   private boolean isCalendar = false;

   /**
    * Applique le format d'affichage aux dateboxes.
    * @param df
    */
   public void setDateboxFormat(final String df){
      multiDatebox.setFormat(df);
      eraseMultiDatebox.setFormat(df);
   }

   public void setCalendar(final boolean cal){
      this.isCalendar = cal;
      if(!isCalendar){
         setDateboxFormat(Labels.getLabel("validation.date.format.simple"));
      }
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
   }

   @Override
   public Object getNewValue(){
      if(super.getNewValue() == null || super.getNewValue() instanceof Date){
         return super.getNewValue();
      }else{
         return ((Calendar) super.getNewValue()).getTime();
      }
   }

   @Override
   public Object extractValueFromEraserBox(){
      if(eraseMultiDatebox.getValue() == null || !isCalendar){
         return eraseMultiDatebox.getValue();
      }else{
         final Calendar cal = Calendar.getInstance();
         cal.setTime(eraseMultiDatebox.getValue());
         return cal;
      }
   }

   @Override
   public Object extractValueFromMultiBox(){
      if(multiDatebox.getValue() == null || !isCalendar){
         return multiDatebox.getValue();
      }else{
         final Calendar cal = Calendar.getInstance();
         cal.setTime(multiDatebox.getValue());
         return cal;
      }
   }

   @Override
   public String formatLocalObject(final Object obj){
      // verifie la presence annotation combine
      if(getIsCombined() && obj != null && obj.equals("system.tk.unknownExistingValue")){
         return Labels.getLabel("system.tk.unknownExistingValue");
      }
      return ObjectTypesFormatters.dateRenderer2(obj);
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){
      multiDatebox.setConstraint(constr);
      eraseMultiDatebox.setConstraint(constr);
   }

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      eraseMultiDatebox.setVisible(visible);
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationDatebox", true));
   }

   @Override
   public void passValueToEraserBox(){
      if(getValues().get(multiListBox.getSelectedIndex()) != null){
         if(!isCalendar){
            eraseMultiDatebox.setValue((Date) getValues().get(multiListBox.getSelectedIndex()));
         }else{
            eraseMultiDatebox.setValue(((Calendar) getValues().get(multiListBox.getSelectedIndex())).getTime());
         }
      }else{
         eraseMultiDatebox.setValue(null);
      }
   }

   @Override
   public void passNullToEraserBox(){
      eraseMultiDatebox.setValue(null);
   }
}
