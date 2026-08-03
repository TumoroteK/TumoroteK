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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
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
   private static final Logger log = LoggerFactory.getLogger(ModificationMultipleListbox.class);

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

   //TK-893 on ne peut pas appliquer la méthode setConsraint sur une ListBox (pour gérer le cas de sexe pour lequel on ne veut pas valoriser une valeur par
   //défaut au chargement de la page donc la liste des valeurs proposées contient null associé à "" (cf PatientUtils.getSexes()) => contrôle à la validation
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
      //TK-890 : isObligatoire est utilisé dans extractValuesFromObjects elle-même appelée dans le init
      //donc il faut qu'il soit valorisé avant l'appel (initialement il était après)
      isObligatoire = isOblig;
      super.init(pathToPage, methodToCall, objs, label, champToEdit, entiteNom, constr, isCombined);
   }

   @Override
   public void extractValuesFromObjects(){

      super.extractValuesFromObjects();

      // pour chaque objet du thésaurus, on va extraire la valeur du
      // champ à afficher ???
      for(final Object object : allValues){
         try{

            // Très bizarre car object est un élément d'un thesaurus donc il ne s'agit pas d'un objet maladie,
            // prélèvement ou echantillon qui peut avec un "delegate"... il y a du y avoir une confusion
            // De plus, dans l'absolu allStringValues ne concerne pas les valeurs associées aux objets sélectionnés mais toutes les
            // valeurs possibles dans le thesaurus associés donc il n'y a rien à ajouter (vue l'initialisation faite dans init)
            // et si il y avait eu quelque chose à ajouter, cela aurait dû être fait dans initComponentsInWindow()
            boolean isDelegateProperty = false;
            TKDelegateObject<?> delegate = null;

            if(object instanceof TKDelegetableObject){
               delegate = ((TKDelegetableObject<?>) object).getDelegate();
               isDelegateProperty = delegate != null && PropertyUtils.describe(delegate).keySet().contains(getChampThesaurus());
            }

            String stringTmp = null;

            if(null != object){

               if(isDelegateProperty){
                  stringTmp = (String) PropertyUtils.getSimpleProperty(delegate, getChampThesaurus());
               }else if(!"bool".equals(getChamp()) && !"bool".equals(getChampThesaurus())){
                  stringTmp = (String) PropertyUtils.getSimpleProperty(object, getChampThesaurus());
               }else{
                  stringTmp = ObjectTypesFormatters.booleanLitteralFormatter((Boolean) object);
               }

               allStringValues.add(stringTmp);

            }

         }catch(final IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            log.error(e.getMessage(), e);
         }

      }
      // ce bloc devrait être dans initComponentsInWindow puisqu'il concerne allValues et allStringValues utilisées par
      // eraseListBox (composant non lié aux valeurs existant dans la séletion des objets (sur lesquels portent la modification multiple)
      // cette "verrue" est nécessaire car pour le sexe, la valeur "vide" est contenue dans la liste de toutes les valeurs possibles 
      // (cf PatientUtils.getSexes()) demande du métier pour que lors de la création d'un patient aucune valeur ne soit prérenseignée
      // dans le champ sens (obligatoire dans les collections non Gatsbi) pour éviter d'oublier de changer la valeur
      // Par contre, cette règle n'a pas été appliquée pour PatientEtat car c'es Vivant par défaut ce qui reprénte quasiment 100% des cas
      // Par conséquent, ajouter la condition sur patientEtat empêche de ne pas renseigner cette valeur même si elle n'est pas obligatoire (collection Gatsbi)
      // d'où la mise en commentaire (TK-895).
      if(!("sexe".equals(getChamp()) /*|| "patientEtat".equals(getChamp())*/) && (isObligatoire == null || !isObligatoire)
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
         log.error(e.getMessage(), e);
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

      // /!\ ce code est surprenant car values et StringValues sont les valeurs récupérées des objets sélectionnés (traitement fait 
      // dans super.extractValuesFromObjects())
      // donc si le champ est obligatoire, il ne devrait / doit pas contenir "" ...
      // Ne pas confondre values et StringValues avec allValues et allStringValues qui elles sont initialisées avec toutes les valeurs
      // possibles et peuvent donc contenir null / "" pour sexe notamment (cf PatientUtils.getSexes())
      if(isObligatoire != null && isObligatoire && getStringValues().contains("")){
         int empty = getValues().indexOf("");
         //TK-893 : dans le cas du sexe, une valeur "" est ajoutée même quand c'est obligatoire pour ne pas renseigner par une valeur par défaut (et risquer qu'elle reste à tort)
         //mais cette valeur affichée à l'utilisateur qui provient de PatientUtils.getSexes() est associé au "code" null et non "" comme ça semble être le cas pour d'autres usages
         //Dans l'absolu, il semblerait plus judicieux de récupérer l'index de empty par la même règle que pour le test qui a détecté un empty (à savoir utiliser getStringValues() 
         //et non getValues()) mais pour éviter une régression si la vraie raison de ce code m'échappe, utilisation d'un "double test" pour récupérer empty dans le cas du sexe
         // /!\ les attributs values et stringValues ne sont pas utilisées dans le composant qui définit
         // allValues et allStringValues :-( ... les modifications de values et stringValues faites ci-dessous ne
         // servent qu'à valoriser selectedStringValue mais allValues et allStringValues contiennent donc toujours null / ""
         if(empty == -1) {
            empty = getValues().indexOf(null);
         }
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
   
   @Override
   public void onClick$validate(){

      if(isObligatoire()){
         //TK-893 : gestion des cas où la liste des valeurs contient une ligne vide pour ne pas prérenseigner le champ (exemple sexe) :
         if(rowOneValue.isVisible()){
            //if(getSelectedStringValue() == null || getSelectedStringValue().equals("")) {
            if(oneValueListBox.getSelectedItem().getValue() == null || oneValueListBox.getSelectedItem().getValue().equals("")) {
               throw new WrongValueException(oneValueListBox, Labels.getLabel("anno.thes.empty"));
            }
         }
         else if (rowMultiValue.isVisible()) {
            if(eraseListBox.getSelectedItem() == null || eraseListBox.getSelectedItem().getValue().equals("")) {
               throw new WrongValueException(eraseListBox, Labels.getLabel("anno.thes.empty"));
            }
         }
      }

      super.onClick$validate();
   }
}



