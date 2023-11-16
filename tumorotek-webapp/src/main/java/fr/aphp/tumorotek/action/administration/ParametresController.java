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
package fr.aphp.tumorotek.action.administration;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.model.config.Parameter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GCH
 *
 */
public class ParametresController extends AbstractController
{

   private List<Parameter> parameterList;

   private Map<Parameter, Boolean> editModeMap;

   public List<Parameter> getParameterList(){
      return parameterList;
   }

   @Override
   public void doAfterCompose(Component comp) throws Exception{
      super.doAfterCompose(comp);
      // Initialize parameterList with sample data
      parameterList = new ArrayList<>();
      parameterList.add(new Parameter("Key1", "Value1", "string"));
      parameterList.add(new Parameter("Key2", "Value2", "boolean"));
      parameterList.add(new Parameter("Key3", "Value3", "string"));

      // Initialize the editModeMap
      editModeMap = new HashMap<>();
      for(Parameter parameter : parameterList){
         editModeMap.put(parameter, false);
      }
   }

   public boolean isEditMode(Parameter parameter){
      return editModeMap.get(parameter);
   }


   @Init
   public void init(){
      // Initialize parameterList with sample data
      parameterList = new ArrayList<>();
      parameterList.add(new Parameter("Key1", "Value1", "string"));
      parameterList.add(new Parameter("Key2", "Value2", "boolean"));
      parameterList.add(new Parameter("Key3", "Value3", "string"));

      // Initialize the editModeMap
      editModeMap = new HashMap<>();
      for(Parameter parameter : parameterList){
         editModeMap.put(parameter, false);
      }
   }

   @Command
   @NotifyChange("parameterList")
   public void editParameter(@BindingParam("parameter") Parameter parameter){
      boolean isEditMode = isEditMode(parameter);

      if(!isEditMode){
         editModeMap.put(parameter, true);
      }else{
         // Save to the database here
         // Get the index of the parameter in the list
         int index = parameterList.indexOf(parameter);

         // Update the parameter with the edited values
         Parameter editedParameter = parameterList.get(index);
         // Assuming Parameter class has appropriate setter methods
         editedParameter.setCode(parameter.getCode());
         editedParameter.setValue(parameter.getValue());
         editedParameter.setType(parameter.getType());

         // After updating, set edit mode to false
         editModeMap.put(parameter, false);

         // After saving, clear the edited value and set edit mode to false
         editModeMap.put(parameter, false);
      }
   }


   @Command
   @NotifyChange("parameterList")
   public void cancelEdit(@BindingParam("parameter") Parameter parameter) {
      // Revert to the original values


      // Set edit mode to false to cancel the editing
      editModeMap.put(parameter, false);


   }


}
