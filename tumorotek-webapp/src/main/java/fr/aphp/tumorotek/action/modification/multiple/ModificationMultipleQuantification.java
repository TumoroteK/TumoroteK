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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une
 * Quantification. Classe créée le 15/03/09.
 *
 * @author Pierre Ventadour
 * @version 2.3.0-gatsbi
 */
public class ModificationMultipleQuantification extends AbstractModificationMultipleComponent
{
   private static final Logger log = LoggerFactory.getLogger(ModificationMultipleQuantification.class);

   private static final long serialVersionUID = -40186196122301556L;

   /**
    * Components.
    */
   private Decimalbox multiNumeriqueBox;

   private Decimalbox eraseMultiNumeriqueBox;

   private Listbox multiUnitesBox;

   private Listbox multiValuesListBox;

   private Listbox eraseMultiUnitesBox;

   private Label modifImpossibleLabel;

   private Row rowModifImpossible;

   /**
    * Variables formulaire.
    */
   // Valeurs numériques du champ des différents objets
   private List<Object> uniteValues = new ArrayList<>();

   private List<Object> allUniteValues = new ArrayList<>();

   private List<String> allStringUniteValues = new ArrayList<>();

   // Si une seule valeur : ancienne
   private Object oldUniqueUniteValue;

   // Nouvelle valeur
   private Object selectedUnite;

   private String champUnite = "";

   // Si false, la modif multiple n'est pas autorisée : un des objets a été
   // utilisé
   private boolean modifPossible = true;

   public void init(final String pathToPage, final String methodToCall, final List<? extends Object> objs, final String label,
      final String champToEdit, final List<Object> allValuesThesaurus, final String champNameThesaurus, final Constraint constr){
      setPath(pathToPage);
      setMethode(methodToCall);
      getListObjets().clear();
      getListObjets().addAll(objs);
      setChampLabel(label);
      setEntite(getListObjets().get(0).getClass().getSimpleName());
      setChamp(champToEdit);
      this.allUniteValues = allValuesThesaurus;
      setChampThesaurus(champNameThesaurus);
      setConstraint(constr);

      // on crée le nom du champ des unités
      if(getChamp().contains("Init")){
         champUnite = getChamp().replace("Init", "Unite");
         // on vérifie que la modif est possible
         validateModification();
      }else{
         champUnite = getChamp().concat("Unite");
      }

      // Initialisation du titre de la fenêtre
      presentationLabel.setValue(createPresentationLabel());

      // Si la modification est possible
      if(modifPossible){

         // extraction des valeurs
         extractValuesFromObjects();

         // initialisation des composants
         initComponentsInWindow();

         // @since 2.2.3-gatsbi
         setConstraintsToBoxes(constr);

         getBinder().loadComponent(self);

         if(getStringValues().size() <= 1){
            multiUnitesBox.setSelectedIndex(allUniteValues.indexOf(selectedUnite));
         }
      }else{
         // si la modif est interdite, on affiche un message d'erreur
         final String[] params = new String[] {Labels.getLabel(getChampLabel())};
         modifImpossibleLabel.setValue(ObjectTypesFormatters.getLabel("modification.multiple.impossible", params));
         rowModifImpossible.setVisible(true);
      }
   }

   /**
    * Cette méthode vérifie que la modif est possible : les valeurs du champ
    * initial et actuel doivent être les mêmes.
    */
   public void validateModification(){
      final String champActuel = getChamp().replace("Init", "");

      // pour chaque objet, on extrait les valeurs du champ
      // initial et actuel et on les compare
      for(int i = 0; i < getListObjets().size(); i++){
         try{
            final Float tmpFloatInit = (Float) PropertyUtils.getSimpleProperty(getListObjets().get(i), getChamp());

            final Float tmpFloat = (Float) PropertyUtils.getSimpleProperty(getListObjets().get(i), champActuel);

            if(tmpFloatInit == null){
               if(tmpFloat != null){
                  modifPossible = false;
               }
            }else if(!tmpFloatInit.equals(tmpFloat)){
               modifPossible = false;
            }

         }catch(final IllegalAccessException e){
            log.error(e.getMessage(), e);
         }catch(final InvocationTargetException e){
            log.error(e.getMessage(), e);
         }catch(final NoSuchMethodException e){
            log.error(e.getMessage(), e);
         }
      }
   }

   @Override
   public void extractValuesFromObjects(){
      setHasNulls(false);
      // pour chaque objet à modifier
      // on extrait la valeur numérique et l'unité actuelle du
      // champ à modifier
      for(int i = 0; i < getListObjets().size(); i++){
         try{
            // on extrait la valeur numérique
            final Float tmpFloat = (Float) PropertyUtils.getSimpleProperty(getListObjets().get(i), getChamp());

            // on extrait l'unité
            final Object tmp = PropertyUtils.getSimpleProperty(getListObjets().get(i), champUnite);

            // on construit une liste de strings contenant la
            // concaténation des 2 valeurs
            List<Object> tmps = null;
            if(tmpFloat != null && tmp != null){
               tmps = new ArrayList<>();
               tmps.add(tmpFloat);
               tmps.add(tmp);
            }else{
               setHasNulls(true);
            }

            if(tmps != null){
               final Object formatted = formatValue(tmpFloat);
               if(!getValues().contains(formatted)){
                  getValues().add(formatted);
                  getStringValues().add(formatLocalObject(tmps));
                  uniteValues.add(tmp);
               }
            }
         }catch(final IllegalAccessException e){
            log.error(e.getMessage(), e);
         }catch(final InvocationTargetException e){
            log.error(e.getMessage(), e);
         }catch(final NoSuchMethodException e){
            log.error(e.getMessage(), e);
         }
      }

      // pour chaque unité, on extrait la valeur de son champ pour
      // l'afficher dans les listes
      for(int i = 0; i < allUniteValues.size(); i++){
         try{
            String stringTmp = null;

            if(allUniteValues.get(i) != null){
               stringTmp = (String) PropertyUtils.getSimpleProperty(allUniteValues.get(i), getChampThesaurus());
            }else{
               stringTmp = "";
            }

            if(!allStringUniteValues.contains(stringTmp)){
               allStringUniteValues.add(stringTmp);
            }
         }catch(final IllegalAccessException e){
            log.error(e.getMessage(), e);
         }catch(final InvocationTargetException e){
            log.error(e.getMessage(), e);
         }catch(final NoSuchMethodException e){
            log.error(e.getMessage(), e);
         }
      }
   }

   @Override
   public void initComponentsInWindow(){
      super.initComponentsInWindow();
      // unite
      if(uniteValues.size() == 1){
         oldUniqueUniteValue = uniteValues.get(0);
         selectedUnite = oldUniqueUniteValue;
      }
   }

   @Override
   public void onClick$lock(){
      // si la modification était impossible, on la rend
      // possible.
      if(multiValuesListBox.isDisabled()){
         multiValuesListBox.setDisabled(false);
         champAttentionLabel.setVisible(false);
         champEcraserLabel.setVisible(true);
         eraseMultiNumeriqueBox.setVisible(true);
         eraseMultiUnitesBox.setVisible(true);
         lock.setSrc("/images/icones/unlocked.png");
         // pour jamais être egal à une nouvelle valeur
         setOldUniqueValueAsNewObject();
      }else{
         // sinon, on empêche toute modif
         multiValuesListBox.setDisabled(true);
         champAttentionLabel.setVisible(true);
         champEcraserLabel.setVisible(false);
         eraseMultiNumeriqueBox.setVisible(false);
         eraseMultiUnitesBox.setVisible(false);
         lock.setSrc("/images/icones/locked.png");
         setOldUniqueValue(null);
      }

      setConstraintsToBoxes(getConstraint());
   }

   @Override
   public Object extractValueFromEraserBox(){
      if(eraseMultiUnitesBox.getSelectedIndex() > -1){
         selectedUnite = allUniteValues.get(eraseMultiUnitesBox.getSelectedIndex());
      }else{
         selectedUnite = allUniteValues.get(0);
      }
      if(eraseMultiNumeriqueBox.getValue() != null){
         return ObjectTypesFormatters.floor(eraseMultiNumeriqueBox.getValue().floatValue(), 3);
      }
      return eraseMultiNumeriqueBox.getValue();
   }

   public void onSelect$eraseMultiUnitesBox(){
      selectedUnite = allUniteValues.get(eraseMultiUnitesBox.getSelectedIndex());
   }

   @Override
   public Object extractValueFromMultiBox(){
      if(multiUnitesBox.getSelectedIndex() > -1){
         selectedUnite = allUniteValues.get(multiUnitesBox.getSelectedIndex());
      }else{
         selectedUnite = allUniteValues.get(0);
      }
      if(multiNumeriqueBox.getValue() != null){
         return ObjectTypesFormatters.floor(multiNumeriqueBox.getValue().floatValue(), 3);
      }
      return multiNumeriqueBox.getValue();
   }

   @Override
   public void onClick$validate(){

      // checks unite is selected
      if(isObligatoire()){
         checkUniteSelected();
      }

      List<Object> finalValue = null;
      setChangedValue(false);
      // si on était en mode liste
      if(getValues().size() > 1){
         if(!multiValuesListBox.isDisabled()){
            // si une valeur a été saisie dans le nouveau champ,
            // c'est la nouvelle valeur
            // sinon
            setNewValue(extractValueFromEraserBox());
            if(getNewValue() == null || getNewValue().equals("")){
               if(multiValuesListBox.getSelectedIndex() > -1){
                  setNewValue(getValues().get(multiValuesListBox.getSelectedIndex()));
               }
            }
         }
      }else{ // mode unique
         setNewValue(extractValueFromMultiBox());
      }

      // verifie le changement
      if((getNewValue() != null
         && (!getNewValue().equals(getOldUniqueValue()) || !selectedUnite.equals(oldUniqueUniteValue) || getHasNulls()))
         || (getNewValue() == null && getOldUniqueValue() != null)){
         finalValue = new ArrayList<>();
         finalValue.add(getNewValue());
         if(getNewValue() != null){
            finalValue.add(selectedUnite);
         }else{
            finalValue.add(null);
         }
         setChangedValue(true);
      }

      final SimpleChampValue scv = new SimpleChampValue();
      scv.setChamp(getChamp());
      scv.setValue(finalValue);

      scv.setPrintValue(getPrintFinalValue(finalValue));

      postBack(scv);

      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * @since 2.3.0-gatsbi
    */
   private void checkUniteSelected(){
      if(rowOneValue.isVisible()){
         if(multiUnitesBox.getSelectedCount() == 0){
            throw new WrongValueException(multiUnitesBox, Labels.getLabel("anno.thes.empty"));
         }
      }else if(rowMultiValue.isVisible()){
         if(eraseMultiUnitesBox.getSelectedCount() == 0){
            throw new WrongValueException(eraseMultiUnitesBox, Labels.getLabel("anno.thes.empty"));
         }
      }
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationQuantification", true));
   }

   public List<Object> getUniteValues(){
      return uniteValues;
   }

   public void setUniteValues(final List<Object> uniteVal){
      this.uniteValues = uniteVal;
   }

   public List<Object> getAllUniteValues(){
      return allUniteValues;
   }

   public void setAllUniteValues(final List<Object> allUniteVal){
      this.allUniteValues = allUniteVal;
   }

   public List<String> getAllStringUniteValues(){
      return allStringUniteValues;
   }

   public void setAllStringUniteValues(final List<String> allStringUniteVal){
      this.allStringUniteValues = allStringUniteVal;
   }

   public String getChampUnite(){
      return champUnite;
   }

   public void setChampUnite(final String champUnit){
      this.champUnite = champUnit;
   }

   public boolean isModifPossible(){
      return modifPossible;
   }

   public void setModifPossible(final boolean modif){
      this.modifPossible = modif;
   }

   public Object getSelectedUnite(){
      return selectedUnite;
   }

   public void setSelectedUnite(final Object sU){
      this.selectedUnite = sU;
   }

   @Override
   public String formatLocalObject(final Object obj){
      if(obj != null && ((List<Object>) obj).get(0) != null){
         final StringBuffer sb = new StringBuffer();
         sb.append(String.valueOf(ObjectTypesFormatters.floor((Float) ((List<Object>) obj).get(0), 3)));
         sb.append(" ");
         sb.append(((Unite) ((List<Object>) obj).get(1)).getUnite());

         return sb.toString();
      }
      return null;
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){
      if(rowOneValue.isVisible()){
         multiNumeriqueBox.setConstraint(constr);
      }else if(rowMultiValue.isVisible()){

         if(constr != null && eraseMultiNumeriqueBox.isVisible()){
            eraseMultiNumeriqueBox.setConstraint(constr);
         }else{
            final SimpleConstraint nullCstr = null;
            eraseMultiNumeriqueBox.setConstraint(nullCstr);
         }
      }
   }

   @Override
   public void setEraserBoxeVisible(final boolean visible){}

   public void onSelect$multiValuesListBox(){
      setSelectedValue(getValues().get(multiValuesListBox.getSelectedIndex()));
      passValueToEraserBox();
   }

   @Override
   public void passValueToEraserBox(){
      if(getValues().get(multiValuesListBox.getSelectedIndex()) != null){
         eraseMultiNumeriqueBox
            .setValue(new BigDecimal(((Float) getValues().get(multiValuesListBox.getSelectedIndex())).floatValue()));
         eraseMultiUnitesBox.setSelectedIndex(allUniteValues.indexOf(uniteValues.get(multiValuesListBox.getSelectedIndex() - 1)));
      }else{ // TODO impossible de passer un decimalbox value à null
         // eraseMultiNumeriqueBox.setValue(null);
         // eraseMultiNumeriqueBox.;
      }
   }

   @Override
   public void passNullToEraserBox(){}

}
