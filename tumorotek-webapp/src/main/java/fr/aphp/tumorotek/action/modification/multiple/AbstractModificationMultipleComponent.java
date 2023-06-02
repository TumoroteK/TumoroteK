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
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;

import fr.aphp.tumorotek.action.constraints.TumoTextConstraint;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;

/**
 * Classe Abstraite gérant les fenêtres modales pour la modification multiple.
 *
 * Classe créée le 15/03/09.
 *
 * @author Pierre Ventadour
 *
 */
public abstract class AbstractModificationMultipleComponent extends AbstractController
{


   /**
    * Components.
    */
   protected Label presentationLabel;

   protected Row rowOneValue;

   protected Row rowMultiValue;

   protected Label champAttentionLabel;

   protected Label champEcraserLabel;

   protected Image lock;

   protected Listbox multiListBox;

   protected Checkbox combine;

   protected Checkbox eraseCombine;

   protected Button validate;

   private boolean hasNulls;

   /**
    * Objets principaux.
    */
   // Valeurs du champ des différents objets
   private List<Object> values = new ArrayList<>();

   private final List<String> stringValues = new ArrayList<>();

   // Si une seule valeur : ancienne
   private Object oldUniqueValue;

   // Nouvelle valeur
   private Object newValue;

   // Valeur sélectionnée dans la liste
   private Object selectedValue;

   // liste des objets à modifier
   private List<Object> listObjets = new ArrayList<>();

   // Code pour label du champ dans .properties internationalisation
   private String champLabel = "";

   // Classe des objets à modifier
   private String entite = "";

   // Champ à modifier
   private String champ = "";

   // Champ du thesaurus a afficher
   private String champThesaurus = "";

   // Chemin pour retrouver la page de modif
   private String path = "";

   // Méthode à qui renvoyer la modif
   private String methode = "";

   private Boolean isCombined = false;

   // Modification ou non
   private boolean changedValue = false;

   private Constraint constraint = null;

   private static final long serialVersionUID = 1L;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
   }

   public Boolean getIsCombined(){
      return isCombined;
   }

   public void setValues(final List<Object> vals){
      this.values = vals;
   }

   public void setHasNulls(final boolean h){
      this.hasNulls = h;
   }

   public boolean getHasNulls(){
      return this.hasNulls;
   }

   public List<Object> getValues(){
      return values;
   }

   public Object getNewValue(){
      return newValue;
   }

   public void setNewValue(final Object value){
      this.newValue = value;
   }

   public Object getOldUniqueValue(){
      return oldUniqueValue;
   }

   public void setOldUniqueValue(final Object oUValue){
      this.oldUniqueValue = oUValue;
   }

   public void setSelectedValue(final Object s){
      this.selectedValue = s;
   }

   public Object getSelectedValue(){
      return selectedValue;
   }

   public List<Object> getListObjets(){
      return listObjets;
   }

   public void setListObjets(final List<Object> list){
      this.listObjets = list;
   }

   public String getChampLabel(){
      return champLabel;
   }

   public void setChampLabel(final String cl){
      this.champLabel = cl;
   }

   public String getEntite(){
      return entite;
   }

   public void setEntite(final String e){
      this.entite = e;
   }

   public String getChamp(){
      return champ;
   }

   public void setChamp(final String c){
      this.champ = c;
   }

   public String getChampThesaurus(){
      return champThesaurus;
   }

   public void setChampThesaurus(final String cs){
      this.champThesaurus = cs;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   public String getMethode(){
      return methode;
   }

   public void setMethode(final String m){
      this.methode = m;
   }

   public boolean isChangedValue(){
      return changedValue;
   }

   public void setChangedValue(final boolean cValue){
      this.changedValue = cValue;
   }

   public List<String> getStringValues(){
      return stringValues;
   }

   public Constraint getConstraint(){
      return constraint;
   }

   public void setConstraint(final Constraint constraint){
      this.constraint = constraint;
   }

   /**
    * Cette méthode crée le titre de la fenêtre.
    * Cherche dans le fichier d'internationalization les traductions des
    * champs et entités.
    * @return Le titre de la fenêtre.
    */
   public String createPresentationLabel(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("champ.presentation.1"));
      final String i3label = Labels.getLabel(champLabel);
      if(i3label != null){
         sb.append(i3label);
      }else{
         sb.append(champLabel);
      }
      sb.append(Labels.getLabel("champ.presentation.2"));
      sb.append(" ");
      sb.append(Labels.getLabel("Entite." + entite));
      sb.append(Labels.getLabel("champ.presentation.3"));

      return sb.toString();
   }

   /**
    * Renvoie l'evenement à la fiche commandant le pop up de
    * modification multiple. Peut renvoyer la nouvelle valeur
    * à appliquer ou un signal de 'reset'.
    * @param finalValue valeur à appliquer
    */
   public void postBack(final Object finalValue){
      if(changedValue){
         // on vérifie que la page devant récupérer la sélection
         // existe
         if(Path.getComponent(path) != null){
            // on envoie un event à cette page avec
            // les échantillons sélectionnés
            Events.postEvent(new Event(methode, Path.getComponent(path), finalValue));
         }
      }else{
         if(Path.getComponent(path) != null){
            // on envoie un event à cette page avec
            // les échantillons sélectionnés
            Events.postEvent(new Event("onResetMulti", Path.getComponent(path), finalValue));
         }
      }
   }

   /**
    * Méthode intialisant le composant.
    * @param pathToPage Chemin vers la page qui demande une modif.
    * @param methodToCall Méthode à appeler
    * @param objs Liste des objets à modifier
    * @param label Code pour label du champ dans .properties
    * internationalisation.
    * @param entiteToEdit Nom de l'entité à modifier.
    * @param champToEdit Champ de l'entité à modifier.
    * @param ent nom de l'entite a afficher dans l'intitulé
    * @param Constraint à appliquer
    * @param Boolean true si champAnnotation combine
    */
   public void init(final String pathToPage, final String methodToCall, final List<? extends Object> objs, final String label,
      final String champToEdit, final String entiteNom, final Constraint constr, final Boolean isComb){
      this.path = pathToPage;
      this.methode = methodToCall;
      this.listObjets.clear();
      this.listObjets.addAll(objs);
      this.champLabel = label;
      if(entiteNom == null){
         this.entite = listObjets.get(0).getClass().getSimpleName();
      }else{
         this.entite = entiteNom;
      }
      this.champ = champToEdit;
      this.isCombined = isComb;
      if(combine != null){
         combine.setVisible(isComb);
      }
      this.constraint = constr;

      // Initialisation du titre de la fenêtre
      presentationLabel.setValue(createPresentationLabel());

      // application de la contrainte
      setConstraintsToBoxes(constr);

      // extraction des valeurs
      extractValuesFromObjects();

      // initialisation des composants
      initComponentsInWindow();
      getBinder().loadComponent(self);
   }

   /**
    * Extrait toutes les valeurs du champ à modifier.
    */
   public void extractValuesFromObjects(){

      // pour chaque objet à modifier
      // on extrait la valeur actuelle du champ à modifier
      // toutes ces valeurs sont placées dans la liste values
      hasNulls = false;
      for(final Object object : listObjets){
         try{

            boolean isDelegateProperty = false;
            TKDelegateObject<?> delegate = null;

            if(object instanceof TKDelegetableObject){
               delegate = ((TKDelegetableObject<?>) object).getDelegate();
               isDelegateProperty = delegate != null && PropertyUtils.describe(delegate).keySet().contains(champ);
            }

            Object tmp = null;
            if(isDelegateProperty){
               tmp = PropertyUtils.getSimpleProperty(delegate, champ);
            }else{
               tmp = PropertyUtils.getSimpleProperty(object, champ);
            }

            // recupere les valeurs non vides
            if(tmp == null){
               if(isCombined){
                  tmp = PropertyUtils.getSimpleProperty(object, "alphanum");
               }
            }
            if(tmp != null && !tmp.equals("")){
               final Object formatted = formatValue(tmp);
               if(!values.contains(formatted)){
                  values.add(formatted);
                  stringValues.add(formatLocalObject(formatted));
               }
            }else{
               hasNulls = true;
            }
         }catch(final IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            log.error(e.getMessage(), e); 
         }
      }
   }

   /**
    * Initialise les composants de la fenêtre.
    */
   public void initComponentsInWindow(){
      // si plusieurs sont saisies pour les différents objets.
      // on affiche la liste permettant de choisir une de ces
      // valeurs ou bien de les écraser par une nouvelle
      rowOneValue.setVisible(values.size() <= 1);
      rowMultiValue.setVisible(values.size() > 1);
      if(values.size() != 1){
         if(values.size() > 1){
            if(combine != null){
               combine.setVisible(false);
            }
         }
         values.add(0, null);
         stringValues.add(0, "");
         // selectionne le premier item de la liste
         selectedValue = values.get(0);
      }else if(values.size() == 1){
         oldUniqueValue = values.get(0);
         setUniqueValueToMultiBox();
      }
   }

   /**
    * Assigne la valeur unique au multibox.
    */
   public void setUniqueValueToMultiBox(){
      newValue = oldUniqueValue;
      if(isCombined){
         // affiche le checkbox combine et checked au besoin
         if(values.get(0).equals("system.tk.unknownExistingValue")){
            combine.setChecked(true);
            oldUniqueValue = "system.tk.unknownExistingValue";
            newValue = null;
         }
      }
   }

   /**
    * Méthode appelée lors du clic sur le bouton lock.
    */
   public void onClick$lock(){
      // si la modification était impossible, on la rend
      // possible.
      if(multiListBox.isDisabled()){
         multiListBox.setDisabled(false);
         champAttentionLabel.setVisible(false);
         champEcraserLabel.setVisible(true);
         setEraserBoxeVisible(true);
         if(eraseCombine != null && isCombined){
            eraseCombine.setVisible(true);
         }
         lock.setSrc("/images/icones/unlocked.png");
         lock.setTooltiptext(Labels.getLabel("tooltip.modifmultiple.lock"));
         // pour jamais être egal à une nouvelle valeur
         setOldUniqueValueAsNewObject();
      }else{
         // sinon, on empêche toute modif
         multiListBox.setDisabled(true);
         champAttentionLabel.setVisible(true);
         champEcraserLabel.setVisible(false);
         setEraserBoxeVisible(false);
         if(eraseCombine != null){
            eraseCombine.setVisible(false);
         }
         lock.setSrc("/images/icones/locked.png");
         lock.setTooltiptext(Labels.getLabel("tooltip.modifmultiple.unlock"));
         oldUniqueValue = null;
      }
   }

   public void setOldUniqueValueAsNewObject(){
      oldUniqueValue = new Object();
   }

   /**
    * Prepare la valeur issu de la selection de la liste de valeurs
    * existantes. Passe la valeur au composant eraser;
    */
   public void onSelect$multiListBox(){
      selectedValue = values.get(multiListBox.getSelectedIndex());
      if(isCombined){
         if(selectedValue != null){
            if(!selectedValue.equals("system.tk.unknownExistingValue")){
               eraseCombine.setChecked(false);
               passValueToEraserBox();
            }else{
               eraseCombine.setChecked(true);
               passNullToEraserBox();
            }
         }else{
            eraseCombine.setChecked(false);
            passNullToEraserBox();
         }
      }else{
         passValueToEraserBox();
      }
   }

   /**
    * Méthode appelée lors du clic sur le bouton validate : on va
    * sauvegarder la modification.
    */
   public void onClick$validate(){
      Object finalValue = null;
      changedValue = false;
      // si on était en mode liste
      if(values.size() > 1){
         if(!multiListBox.isDisabled()){
            // si une valeur a été saisie dans le nouveau champ,
            // c'est la nouvelle valeur
            // sinon
            newValue = extractValueFromEraserBox();
            if(newValue == null || newValue.equals("")){
               // verifie que si eraseCombine box est checke
               if(isCombined && (eraseCombine.isChecked())){
                  newValue = "system.tk.unknownExistingValue";
               }else{
                  newValue = null;
               }
            }
         }
      }else{ // mode unique
         newValue = extractValueFromMultiBox();
         if(newValue == null || newValue.equals("")){
            // verifie que si eraseCombine box est checke
            if(isCombined && combine.isChecked()){
               newValue = "system.tk.unknownExistingValue";
            }else{
               newValue = null;
            }
         }
      }

      // verifie le changement
      if((newValue != null && (!newValue.equals(oldUniqueValue) || hasNulls))
         || (newValue == null && (oldUniqueValue != null && !isObligatoire()))){
         finalValue = newValue;
         changedValue = true;
      }

      final SimpleChampValue scv = new SimpleChampValue();
      scv.setChamp(getChamp());
      scv.setValue(finalValue);

      scv.setPrintValue(getPrintFinalValue(finalValue));

      postBack(scv);

      Events.postEvent(new Event("onClose", self.getRoot()));

   }

   /**
    * Méthode appelée lors du clic sur le bouton revert : on ferme
    * la fenêtre.
    */
   public void onClick$revert(){
      if(Path.getComponent(path) != null){
         // on envoie un event à cette page avec
         // les échantillons sélectionnés
         final SimpleChampValue scv = new SimpleChampValue();
         scv.setChamp(getChamp());
         Events.postEvent(new Event("onResetMulti", Path.getComponent(path), scv));
      }
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /***************** abstract methods **************************/

   /**
    * Applique la contrainte passée à l'initialisation au boxes.
    * @param constr
    */
   public abstract void setConstraintsToBoxes(Constraint constr);

   /**
    * Formate la valeur pour l'affichage.
    * @param obj
    * @return
    */
   public abstract String formatLocalObject(Object obj);

   /**
    * Formate au besoin la valeur extraite de la bd en un objet
    * intermédiaire (LableCodeItem pour sexe ou état patient).
    * @param obj
    * @return
    */
   public Object formatValue(final Object obj){
      return obj;
   }

   /**
    * Affiche ou cache le composant 'eraser'.
    * @param visible
    */
   public abstract void setEraserBoxeVisible(boolean visible);

   /**
    * Recupere la valeur specifiée dans le Box uniqueValue.
    * @return valeur Object
    */
   public abstract Object extractValueFromMultiBox();

   /**
    * Recupere la valeur specifiée dans le Box 'eraser'.
    * @return valeur Object
    */
   public abstract Object extractValueFromEraserBox();

   /**
    * Prepare le String qui sera utilisé pour l'affichage de la
    * valeur issue de la modification multiple.
    * @param obj valeur issu modification multiple
    * @return String utilisé pour affichage
    */
   public String getPrintFinalValue(final Object obj){
      return formatLocalObject(obj);
   }

   /**
    * Passe la valeur choisie dans la liste de valeurs existantes
    * au composant eraserBox.
    */
   public abstract void passValueToEraserBox();

   /**
    * Passe une valeur nulle pour re-initialiser l'eraserBox.
    */
   public abstract void passNullToEraserBox();

   /**
    * Indique si le champ est obligatoire en fonction de la contrainte
    * passée lors de la init.
    * @return boolean true si obligatoire
    */
   public Boolean isObligatoire(){
      return (getConstraint() != null
         && (getConstraint() instanceof TumoTextConstraint && !((TumoTextConstraint) getConstraint()).getNullable())
         || (getConstraint() instanceof SimpleConstraint
            && !((SimpleConstraint) getConstraint()).equals(new SimpleConstraint("no empty"))));
   }
}
