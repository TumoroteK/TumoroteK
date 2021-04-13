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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import fr.aphp.tumorotek.action.patient.LabelCodeItem;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une
 * Listbox.
 * Classe créée le 11/03/09. Modifiée par Mathieu pour utiliser
 * factorisation AbstractModificationMultipleComponent.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ModificationMultipleListbox extends AbstractModificationMultipleComponent
{

   private static final long serialVersionUID = 3551763682958457361L;

   /**
    * Components.
    */
   private Listbox eraseListBox;
   private Listbox oneValueListBox;
   private Boolean isObligatoire;

   // Toutes les valeurs possibles du champ
   private final List<Object> allValues = new ArrayList<>();
   // Valeurs String correspondantes
   private final List<String> allStringValues = new ArrayList<>();
   // valeur string correspondante
   private String selectedStringValue = null;

   @Override
   public Object extractValueFromEraserBox(){
      // si une valeur selectionnée
      if(eraseListBox.getSelectedIndex() > -1){
         return allValues.get(eraseListBox.getSelectedIndex());
      }else{
         if(!allValues.isEmpty()){
            return allValues.get(0);
         }
         return null;
      }
   }

   @Override
   public Object extractValueFromMultiBox(){
      // si une valeur selectionnée
      if(oneValueListBox.getSelectedIndex() > -1){
         return allValues.get(oneValueListBox.getSelectedIndex());
      }else{
         return null;
      }
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){}

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      eraseListBox.setVisible(visible);
   }

   @Override
   public void passValueToEraserBox(){
      eraseListBox.setSelectedItem(findItemForValue(multiListBox.getSelectedItem().getValue()));
   }

   @Override
   public void passNullToEraserBox(){
      eraseListBox.setSelectedItem(null);
   }

   private Listitem findItemForValue(final Object value){
      final Iterator<Listitem> its = eraseListBox.getItems().iterator();
      Listitem item;
      while(its.hasNext()){
         item = its.next();
         if(item.getValue() != null){
            if(item.getValue().equals(value)){
               return item;
            }
         }else if(value == null){
            return item;
         }
      }
      return null;
   }

   /**
    * Ecrase la methode héritée init pour gérer les values thesaurus.
    * @param pathToPage
    * @param methodToCall
    * @param objs
    * @param label
    * @param champToEdit
    * @param allValuesThesaurus
    * @param champNameThesaurus
    * @param entiteNom
    * @param constr
    * @param isCombined
    * @param isObligatoire true si chp annotation obligatoire
    */
   public void init(final String pathToPage, final String methodToCall, final List<? extends Object> objs, final String label,
      final String champToEdit, final List<Object> allValuesThesaurus, final String champNameThesaurus, final String entiteNom,
      final Constraint constr, final Boolean isCombined, final Boolean isOblig){

      // copie la liste pour eviter la modification de la liste originale
      this.allValues.clear();
      this.allValues.addAll(allValuesThesaurus);
      setChampThesaurus(champNameThesaurus);
      super.init(pathToPage, methodToCall, objs, label, champToEdit, entiteNom, constr, isCombined);
      isObligatoire = isOblig;
   }

   @Override
   public void extractValuesFromObjects(){

      super.extractValuesFromObjects();

      // pour chaque objet du thésaurus, on va extraire la valeur du
      // champ à afficher
      for(Object object : allValues) {
         try{

            boolean isDelegateProperty = false;
            TKDelegateObject<?> delegate = null;

            if(object instanceof TKDelegetableObject) {
               delegate = ((TKDelegetableObject<?>)object).getDelegate();
               isDelegateProperty =  delegate != null && 
            		 PropertyUtils.describe(delegate).keySet().contains(getChampThesaurus());
            }

            String stringTmp = null;

            if(null != object) {
               
               if(isDelegateProperty) {
                  stringTmp = (String) PropertyUtils.getSimpleProperty(delegate, getChampThesaurus());
               }
               else if(!"bool".equals(getChamp()) && !"bool".equals(getChampThesaurus())) {
                  stringTmp = (String) PropertyUtils.getSimpleProperty(object, getChampThesaurus());
               }
               else{
                  stringTmp = ObjectTypesFormatters.booleanLitteralFormatter((Boolean) object);
               }

               allStringValues.add(stringTmp);

            }

         }catch(final IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            log.error(e);
         }
         
      }
      if(!("sexe".equals(getChamp()) || "patientEtat".equals(getChamp())) && (isObligatoire == null || !isObligatoire)
         && !allValues.isEmpty()){
         allValues.add(0, null);
         allStringValues.add(0, "---");
      }
   }

   @Override
   public String formatLocalObject(final Object obj){
      if(getIsCombined() && obj != null && obj.equals("system.tk.unknownExistingValue")){
         return Labels.getLabel("system.tk.unknownExistingValue");
      }
      String out = null;
      try{
         if(obj != null){
            if(!getChamp().equals("bool") && !getChampThesaurus().equals("bool")){
               out = (String) PropertyUtils.getSimpleProperty(obj, getChampThesaurus());
            }else{
               out = ObjectTypesFormatters.booleanLitteralFormatter((Boolean) obj);
            }
         }
      }catch(final Exception e){
         log.error(e);
      }
      return out;
   }

   @Override
   public Object formatValue(final Object obj){
      if(!("sexe".equals(getChamp()) || "patientEtat".equals(getChamp()) || "lateralite".equals(getChamp()))){
         return obj;
      }else{
         if("sexe".equals(getChamp()) || "patientEtat".equals(getChamp())){
            // recupere le LabelCodeItem 
            // (au masculin nécessairement pour etat en modif multiple)
            return PatientUtils.getLabelCodeItemFromValue((String) obj, true);
         }else{ // lateralite
            for(int i = 0; i < allValues.size(); i++){
               if(((LabelCodeItem) allValues.get(i)).getCode().equals(obj)){
                  return allValues.get(i);
               }
            }
            return null;
         }
      }
   }

   /**
    * Initialise les composants de la fenêtre.
    */
   @Override
   public void initComponentsInWindow(){
      super.initComponentsInWindow();

      if(isObligatoire != null && isObligatoire && getStringValues().contains("")){
         final int empty = getValues().indexOf("");
         getValues().remove(empty);
         getStringValues().remove(empty);
         // selectionne le premier item de la liste
         setSelectedValue(getValues().get(0));
      }

      if(getValues().size() == 1){
         selectedStringValue = getStringValues().get(0);
      }
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationListbox", true));
   }

   public List<Object> getAllValues(){
      return allValues;
   }

   public List<String> getAllStringValues(){
      return allStringValues;
   }

   public String getSelectedStringValue(){
      return selectedStringValue;
   }

   public void setSelectedStringValue(final String sel){
      this.selectedStringValue = sel;
   }

   @Override
   public Boolean isObligatoire(){
      return isObligatoire;
   }
}
