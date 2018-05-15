/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.annotation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Group;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.XulElement;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.annotations.DureeComponent;
import fr.aphp.tumorotek.action.constraints.ConstAlphanum;
import fr.aphp.tumorotek.action.constraints.ConstDateLimit;
import fr.aphp.tumorotek.action.constraints.ConstFilename;
import fr.aphp.tumorotek.action.constraints.ConstHyperlien;
import fr.aphp.tumorotek.action.constraints.TumoTextConstraint;
import fr.aphp.tumorotek.action.modification.multiple.FilePack;
import fr.aphp.tumorotek.action.modification.multiple.SimpleChampValue;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller d'un composant representant un champ Annotation.
 * Controller générique adapté à tous les types de champs. Gere l'affichage,
 * preparation , creation, modification et suppression de ou des objets
 * AnnotationValeur associé(s).
 * <p>
 * Si le champ est 'combiné' (pour les types alphanum, date, num, texte,
 * et thesaurus), et qu'aucune valeur n'est spécifiée alors le champ
 * alphanum est utilisé pour recevoir la valeur
 * 'system.tk.unknownExistingValue' qui indique que la combineBox
 * a été cochée mais qu'aucune valeur n'a été spécifiée.
 * <p>
 * Le composant se base sur quatre liste de AnnotatioValeur:
 * - valeurs: qui sert a l'affichage
 * - valeursToCreate: valeurs qui seront créées
 * - valeursToUpdate: valeurs qui seront modifiées
 * - valeursToDelete: valeurs qui seront supprimées
 * <p>
 * Date creation : 22/02/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class AnnotationComponent extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 1L;

   private final Log log = LogFactory.getLog(AnnotationComponent.class);

   private Label annoLabel;
   private Label annoValue;
   private Checkbox combineBox;
   private Hlayout mainBox;
   private Hbox annoBox;

   private String colClass = null;

   // composant groupe (table) auquel appartient le champ
   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    */
   //private Group annoGroup;
   private Component tableComponent;

   // file edit composants
   private Textbox fileNameBox;
   private Image addFile;
   private Image deleteFile;
   private Fichier fichier = null;
   // private File temp = null;
   private InputStream stream = null;
   private final int sizeLimit = 10000;

   // composant representant les différents types de box possibles
   private HtmlBasedComponent box = null;

   // contrainet appliquée au champ editable box
   private Constraint boxConstr = null;

   // objets principaux
   private List<AnnotationValeur> valeurs = new ArrayList<>();

   private final List<AnnotationValeur> backUp = new ArrayList<>();

   private final List<AnnotationValeur> valeursToCreateOrUpdate = new ArrayList<>();
   private final List<AnnotationValeur> valeursToDelete = new ArrayList<>();
   private ChampAnnotation champAnnotation = null;
   private List<XulElement> listitems;
   private Object selectedItem = null;
   private final Set<Listitem> selectedItems = new HashSet<>();
   private List<AnnotationDefaut> defauts = null;
   private boolean hasDefaut;
   private boolean isCombined;

   private List<Item> selectedIts = new ArrayList<>();

   // modification multiple
   private List<TKAnnotableObject> multiObjs;
   private Label annoMultiLabel;
   // ??
   private String multiComponentType;
   // ??
   private String champToEdit;
   private final List<Object> allItems = new ArrayList<>();
   private String itemProp = null;
   private final List<Object> annoMultiValeurs = new ArrayList<>();

   /**
    * Methode appelée à l'initialisation de la fiche annotation.
    * Crée le composant à partir du champAnnotation, sans valeurs.
    *
    * @param c
    * @param multi si Annotation en modif multiple.
    */
   public void setChamp(final ChampAnnotation c, final boolean multi){
      this.champAnnotation = c;
      annoLabel.setValue(champAnnotation.getNom());

      this.isCombined = !(this.champAnnotation.getCombine() == null || !this.champAnnotation.getCombine());

      // valeurs par defaut
      this.defauts =
         new ArrayList<>(ManagerLocator.getChampAnnotationManager().getAnnotationDefautsManager(this.champAnnotation));
      this.hasDefaut = defauts.size() > 0;

      if(!multi){
         drawEmptyComponent();
      }else{
         drawAnnotModifMultiple();
      }
   }

   public ChampAnnotation getChamp(){
      return champAnnotation;
   }

   public void setColClass(final String c){
      this.colClass = c;
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    */
   /*public void setAnnoGroup(Group aG) {
   	this.annoGroup = aG;
   }*/

   public void setTableComponent(final Component _t){
      this.tableComponent = _t;
   }

   /** END **/

   private boolean getIsObligatoire(){
      return this.hasDefaut && this.defauts.get(0).getObligatoire();
   }

   /**
    * Assigne la ou les valeurs spécifiée(s) pour l'annotation.
    * Passe en mode static le composant car cette methode est appelée
    * lors de la selection d'un nouveau TKAnnotableObjet.
    * Rafraichit les listes de manipulations.
    *
    * @param vs
    */
   public void setValeurs(final List<AnnotationValeur> vs){
      switchToStaticOrEditMode(true);
      this.valeurs = vs;
      // deep copy
      backUpValeursBeforeEdition();
      this.valeursToCreateOrUpdate.clear();
      this.valeursToDelete.clear();
      setAnnotationValeur();
   }

   public List<AnnotationValeur> getValeurs(){
      return valeurs;
   }

   public List<AnnotationValeur> getValeursToCreateOrUpdate(){
      return valeursToCreateOrUpdate;
   }

   public List<AnnotationValeur> getValeursToDelete(){
      return valeursToDelete;
   }

   /**
    * Affiche le composant en mode static ou editable.
    */
   public void switchToStaticOrEditMode(final boolean isStatic){
      // Les champs calculés ne sont pas éditables
      if(!"calcule".equals(this.champAnnotation.getDataType().getType())){
         annoValue.setVisible(isStatic);
         annoBox.setVisible(!isStatic);
         if(colClass != null){
            if(isStatic){
               annoLabel.setSclass("formLabel");
            }else{
               annoLabel.setSclass(colClass);
            }
         }
      }
   }

   /*************** Event listeners. ******************/
   /**
    * Lors du cochage de la checkBox (champ combiné), le champ n'est plus grisé pour le remplir.
    * Si elle n'est pas cochée, le champs est grisé et sa valeur est mise à null
    */
   public void onCheck$combineBox(){
      String dataType = this.champAnnotation.getDataType().getType();
      switch(dataType){
         default:
            break;
         case "alphanum":
            checkTextbox();
            break;
         case "date":
            Datebox.class.cast(box).setDisabled(!combineBox.isChecked());
            if(!combineBox.isChecked()){
               detachAnReattachDatebox(); //dateBox null
            }
            break;
         case "datetime":
            CalendarBox.class.cast(box).setDisabled(!combineBox.isChecked());
            if(!combineBox.isChecked()){
               CalendarBox.class.cast(box).setValue(null); //dateBox null
            }
            break;
         case "num":
            Decimalbox.class.cast(box).setDisabled(!combineBox.isChecked());
            if(!combineBox.isChecked()){
               Decimalbox.class.cast(box).setRawValue(null);
            }
            break;
         case "texte":
            checkTextbox();
            break;
         case "thesaurus":
            Combobox.class.cast(box).setDisabled(!combineBox.isChecked());
            if(!combineBox.isChecked()){
               Combobox.class.cast(box).setSelectedItem(null);
            }
            break;
         case "thesaurusM":
            toggleDisabledListbox(!combineBox.isChecked());
            if(!combineBox.isChecked()){
               this.selectedItems.clear();
               updateListboxSelection();
            }
            break;
         case "fichier":
            setAnnotationValeurFichier();
            break;
         case "hyperlien":
            checkTextbox();
            setAnnotationValeurHyperlien();
            break;
         case "duree":
            DureeComponent.class.cast(box).setDisabled(!combineBox.isChecked());
            if(!combineBox.isChecked()){
               DureeComponent.class.cast(box).setDuree(null);
            }
            break;
      }
   }

   /**
    * Lors du cochage de la checkBox (champ combiné), le champ n'est plus grisé pour le remplir.
    * Si elle n'est pas cochée, le champs est grisé et sa valeur est mise à null
    */
   private void checkTextbox(){
      Textbox.class.cast(box).setDisabled(!combineBox.isChecked());
      if(!combineBox.isChecked()){
         Textbox.class.cast(box).setValue(null);
      }
   }

   /**
    * Rafraichit le contenu selectionné de la listbox.
    */
   private void updateListboxSelection(){
      Listbox.class.cast(box).clearSelection();
      Listbox.class.cast(box).setSelectedItems(this.selectedItems);
   }

   /**
    * Disbale la liste de checkboxes.
    */
   private void toggleDisabledListbox(final boolean disable){
      for(int i = 0; i < this.listitems.size(); i++){
         Listitem.class.cast(this.listitems.get(i)).setDisabled(disable);
      }
   }

   /*************** Methodes privées. ******************/

   /**
    * Renvoie le controller du composant FicheAnnotation
    * contenant le composant.
    */
   private FicheAnnotation getFicheAnnotation(){
      return (FicheAnnotation) self.getParent().getParent().getParent().getAttributeOrFellow("fwinAnnotation$composer", true);
   }

   /**
    * Dessine le composant avec tous ses elements en fonction des
    * informations contenues dans ChampAnnotation: son
    * type, valeur par defaut, combine.
    */
   private void drawEmptyComponent(){
      if(this.champAnnotation != null){
         drawEmptyComponent(this.champAnnotation.getDataType());
      }
   }

   /**
    * Dessine le composant avec tous ses elements en fonction des
    * informations contenues dans ChampAnnotation: son
    * type, valeur par defaut, combine.
    * 
    * @param dataType dataType
    */
   private void drawEmptyComponent(DataType dataType){
      switch(dataType.getType()){
         default:
            break;
         case "alphanum":
            createTextbox();
            break;
         case "hyperlien":
            createTextbox();
            break;
         case "texte":
            createTextboxForText();
            break;
         case "boolean":
            createBoolbox();
            break;
         case "date":
            createDatebox();
            break;
         case "datetime":
            createCalendarbox();
            break;
         case "num":
            createDoublebox();
            break;
         case "thesaurus":
            createThesaurusBox();
            break;
         case "thesaurusM":
            createThesaurusMBox();
            break;
         case "fichier":
            createFilebox();
            break;
         case "calcule":
            drawEmptyComponent(this.champAnnotation.getChampCalcule().getDataType());
            break;
         case "duree":
            createDureeBox();
            break;
      }

      // ajoute le composant formulaire adequat à la vue
      annoBox.appendChild(box);

      // affiche ou non le combinebox
      if(this.champAnnotation.getCombine() != null && this.champAnnotation.getCombine()){
         combineBox.setVisible(true);
      }
   }

   /**
    * Méthode d'entrée appelant la methode setter adéquate en fonction
    * du data type du champ annotation.
    */
   public void setAnnotationValeur(){
      if(this.champAnnotation != null){
         setAnnotationValeur(this.champAnnotation.getDataType());
      }
   }

   /**
    * Méthode d'entrée appelant la methode setter adéquate en fonction
    * du data type du champ annotation.
    * 
    * @param dataType champAnnotation
    */
   public void setAnnotationValeur(DataType dataType){
      switch(dataType.getType()){
         default:
            break;
         case "alphanum":
            setAnnotationValeurAlphanum();
            break;
         case "hyperlien":
            setAnnotationValeurHyperlien();
            break;
         case "texte":
            setAnnotationValeurTexte();
            break;
         case "boolean":
            setAnnotationValeurBool();
            break;
         case "date":
            setAnnotationValeurDate();
            break;
         case "datetime":
            setAnnotationValeurDatetime();
            break;
         case "num":
            setAnnotationValeurNum();
            break;
         case "thesaurus":
            setAnnotationValeurThesaurus();
            break;
         case "thesaurusM":
            setAnnotationValeurThesaurusM();
            break;
         case "fichier":
            setAnnotationValeurFichier();
            break;
         case "calcule":
            setAnnotationValeur(this.champAnnotation.getChampCalcule().getDataType());
            break;
         case "duree":
            setAnnotationValeurDuree();
            break;
      }
   }

   /**
    * Récupère et affiche la durée formattée dans les composants static (lecture) et edit (edition)
    */
   private void setAnnotationValeurDuree(){
      Duree duree = null;
      String newAnnoValue = null;
      // Récupération de la valeur ou de la valeur par défaut pour l'édition
      if(!this.valeurs.isEmpty() && null != valeurs.get(0).getAlphanum() && !"".equals(valeurs.get(0).getAlphanum())){
         newAnnoValue = valeurs.get(0).getAlphanum();
      }else if(!defauts.isEmpty() && null != defauts.get(0).getAlphanum()){
         Long secondes = new Long(defauts.get(0).getAlphanum());
         duree = new Duree(secondes, Duree.SECONDE);
      }

      if(isCombined){
         if("system.tk.unknownExistingValue".equals(newAnnoValue)){
            newAnnoValue = Labels.getLabel(newAnnoValue);
            annoValue.setStyle("font-style: italic");
            combineBox.setChecked(true);
            DureeComponent.class.cast(box).setDisabled(false);
         }else if(null != newAnnoValue){
            Long secondes = new Long(newAnnoValue);
            duree = new Duree(secondes, Duree.SECONDE);
            newAnnoValue = ObjectTypesFormatters.formatDuree(duree);
            annoValue.setStyle("font-style: normal");
            combineBox.setChecked(false);
         }else{
            combineBox.setChecked(false);
            DureeComponent.class.cast(box).setDisabled(true);
         }
      }else if(null != newAnnoValue){
         Long secondes = new Long(newAnnoValue);
         duree = new Duree(secondes, Duree.SECONDE);
         newAnnoValue = ObjectTypesFormatters.formatDuree(duree);
      }

      DureeComponent.class.cast(box).setDuree(duree);
      annoValue.setValue(newAnnoValue);
   }

   /**
    * Enregistre la durée dans l'annotationValeur après la saisie pour enregistrement
    * @param oldAnnoValeur l'annotationValeur avant modification
    * @return si l'annotationValeur doit être enregistrée (nouvelle valeur différente)
    */
   private Boolean prepareAnnotationValeurDuree(AnnotationValeur oldAnnoValeur){
      Duree duree = null;
      String newVal = null;
      
      duree = DureeComponent.class.cast(box).getDuree();
      
      // Vérifie si le champ est obligatoire et renseigné
      if(getIsObligatoire() && (duree == null
         || duree.getTemps(Duree.SECONDE) == 0)){
         throw new WrongValueException(box, Labels.getLabel("anno.duree.empty"));
      }
      
      if(0 != duree.getTemps(Duree.SECONDE)){
         newVal = duree.getTemps(Duree.SECONDE).toString();
      }else if(isCombined && combineBox.isChecked()){
         newVal = "system.tk.unknownExistingValue";
      }

      // skip create si la valeur est inchangée
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(this.valeurs.get(0).getAnnotationValeurId() != null && (this.valeurs.get(0).getAlphanum() == newVal
            || (this.valeurs.get(0).getAlphanum() != null && this.valeurs.get(0).getAlphanum().equals(newVal)))){
            log.debug("Annotation alphanum " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }

      oldAnnoValeur.setAlphanum(newVal);
      return false;
   }

   /**
    * Affiche la valeur spécifiée alphanum
    * dans les composants static et edit.
    */
   private void setAnnotationValeurAlphanum(){

      String alphanumV = null;

      // recupere la valeur
      if(this.valeurs != null && this.valeurs.size() > 0){
         alphanumV = valeurs.get(0).getAlphanum();
         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(alphanumV)){
               annoValue.setValue(alphanumV);
               annoValue.setStyle("font-style: normal");
               ((Textbox) box).setValue(alphanumV);
               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(alphanumV));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);
               ((Textbox) box).setValue(null);
               ((Textbox) box).setDisabled(false);
            }
         }else{
            annoValue.setValue(alphanumV);
            ((Textbox) box).setValue(alphanumV);
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((Textbox) box).setDisabled(true);
         }
         if(!this.hasDefaut){
            ((Textbox) box).setValue(null);
         }else{
            if(getIsObligatoire() && defauts.get(0).getAlphanum() == null){
               final Constraint c = null;
               ((Textbox) box).setConstraint(c);
               ((Textbox) box).setValue(null);
               ((Textbox) box).setConstraint(this.boxConstr);
            }else{
               ((Textbox) box).setValue(defauts.get(0).getAlphanum());
            }
         }
      }
   }

   /**
    * Prepare la 'objet AnnotationValeur pour les types alphanum ou hyperlien.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private boolean prepareAnnotationValeurAlphanumOrHyperlien(final AnnotationValeur valeur){
      String newVal = null;

      if(((Textbox) box).getValue() != null && !((Textbox) box).getValue().equals("")){
         newVal = ((Textbox) box).getValue();
      }else if(isCombined && combineBox.isChecked()){
         newVal = "system.tk.unknownExistingValue";
      }

      // skip create si la valeur est inchangée
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(this.valeurs.get(0).getAnnotationValeurId() != null && (this.valeurs.get(0).getAlphanum() == newVal
            || (this.valeurs.get(0).getAlphanum() != null && this.valeurs.get(0).getAlphanum().equals(newVal)))){
            log.debug("Annotation alphanum " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }
      valeur.setAlphanum(newVal);
      return false;
   }

   /**
    * Affiche la valeur spécifiée bool
    * dans les composants static et edit.
    * champ booleen.
    */
   private void setAnnotationValeurBool(){
      Clients.clearWrongValue(box);
      findSelectedBool(true);
      ((Listbox) box).clearSelection();
      if(this.valeurs != null && this.valeurs.size() > 0){
         // recupere la valeur et assigne dans selectedItem ou selectedItems
         annoValue.setValue(((Listitem) this.selectedItem).getLabel());
         ((Listbox) box).setSelectedItem((Listitem) this.selectedItem);
      }else{
         annoValue.setValue(null);
         if(this.hasDefaut){ // valeurs par défaut si non nulles
            findSelectedBool(false);
         }
         ((Listbox) box).setSelectedItem((Listitem) this.selectedItem);
      }
   }

   /**
    * Prepare l'objet AnnotationValeur pour le type booleen.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private boolean prepareAnnotationValeurBooleen(final AnnotationValeur valeur){
      Boolean newVal = null;

      // attribue newVal, newVals et unkonwExisting
      if(((Listbox) box).getSelectedItem() != null){
         newVal = ((Boolean) ((Listbox) box).getSelectedItem().getValue());
      }else{
         if(getIsObligatoire() && this.defauts.get(0).getBool() == null){
            throw new WrongValueException(box, Labels.getLabel("anno.thes.empty"));
         }
      }

      //skipCreate si valeurs inchangées
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(this.valeurs.get(0).getAnnotationValeurId() != null && (this.valeurs.get(0).getBool() == newVal
            || (this.valeurs.get(0).getBool() != null && this.valeurs.get(0).getBool().equals(newVal)))){
            log.debug("Annotation thesaurus " + champAnnotation.toString() + " inchangée");
            return true;
         }else if(newVal == null){ // suppression valeur existante
            this.valeursToDelete.add(this.valeurs.get(0).clone());
            valeurs.clear();
            return true; //skip create only delete
         }
      }

      // prepare AnnotationValeur
      valeur.setBool(newVal);

      return false;
   }

   /**
    * Affiche la valeur spécifiée date
    * dans les composants static et edit.
    */
   private void setAnnotationValeurDate(){

      Calendar dateV = null;

      // recupere la valeur
      if(this.valeurs != null && this.valeurs.size() > 0){
         dateV = valeurs.get(0).getDate();
         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(this.valeurs.get(0).getAlphanum())){
               annoValue.setValue(ObjectTypesFormatters.dateRenderer2(dateV));
               annoValue.setStyle("font-style: normal");
               if(dateV != null){
                  ((Datebox) box).setValue(dateV.getTime());
               }else{
                  detachAnReattachDatebox(); //dateBox null
               }
               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(this.valeurs.get(0).getAlphanum()));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);
               detachAnReattachDatebox(); //dateBox null
               ((Datebox) box).setDisabled(false);
            }
         }else{
            annoValue.setValue(ObjectTypesFormatters.dateRenderer2(dateV));
            if(dateV != null){
               ((Datebox) box).setValue(dateV.getTime());
            }else{
               detachAnReattachDatebox(); //dateBox null
            }
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((Datebox) box).setDisabled(true);
         }
         if(!this.hasDefaut){
            detachAnReattachDatebox();
         }else{
            if(defauts.get(0).getDate() != null){
               ((Datebox) box).setValue(defauts.get(0).getDate().getTime());
            }else{
               detachAnReattachDatebox(); //dateBox null
            }
         }
      }
   }

   /**
    * Affiche la valeur spécifiée Calendar
    * dans les composants static et edit.
    */
   private void setAnnotationValeurDatetime(){

      Calendar dateV = null;

      // recupere la valeur
      if(this.valeurs != null && this.valeurs.size() > 0){
         dateV = valeurs.get(0).getDate();
         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(this.valeurs.get(0).getAlphanum())){
               annoValue.setValue(ObjectTypesFormatters.dateRenderer2(dateV));
               annoValue.setStyle("font-style: normal");
               if(dateV != null){
                  ((CalendarBox) box).setValue(dateV);
               }else{
                  ((CalendarBox) box).setValue(null);
               }
               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(this.valeurs.get(0).getAlphanum()));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);
               ((CalendarBox) box).setValue(null);
               ((CalendarBox) box).setDisabled(false);
            }
         }else{
            annoValue.setValue(ObjectTypesFormatters.dateRenderer2(dateV));
            if(dateV != null){
               ((CalendarBox) box).setValue(dateV);
            }else{
               ((CalendarBox) box).setValue(null);
            }
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((CalendarBox) box).setDisabled(true);
         }
         if(!this.hasDefaut){
            ((CalendarBox) box).setValue(null);
         }else{
            if(defauts.get(0).getDate() != null){
               ((CalendarBox) box).setValue(defauts.get(0).getDate());
            }else{
               ((CalendarBox) box).setValue(null);
            }
         }
      }
   }

   /**
    * Prepare la 'objet AnnotationValeur pour le type date.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private boolean prepareAnnotationValeurDate(final AnnotationValeur valeur){

      Calendar newVal = Calendar.getInstance();
      String unknownExisting = null;

      if(((Datebox) box).getValue() != null){
         newVal.setTime(((Datebox) box).getValue());
      }else{
         newVal = null;
         if(isCombined && combineBox.isChecked()){
            unknownExisting = "system.tk.unknownExistingValue";
         }
      }

      // skip create si la valeur est inchangée
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(unknownExisting == null){
            if(this.valeurs.get(0).getAlphanum() == null && this.valeurs.get(0).getAnnotationValeurId() != null
               && (this.valeurs.get(0).getDate() == newVal || (this.valeurs.get(0).getDate() != null && newVal != null
                  && this.valeurs.get(0).getDate().compareTo(newVal) == 0))){
               log.debug("Annotation date " + champAnnotation.toString() + " inchangée");
               return true;
            }
         }else if(this.valeurs.get(0).getAlphanum() != null){
            // unknownExisting inchangee
            log.debug("Annotation date system.tk.unknownExistingValue " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }
      valeur.setDate(newVal);
      valeur.setAlphanum(unknownExisting);
      return false;
   }

   /**
    * Prepare la 'objet AnnotationValeur pour le type Calendar.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private boolean prepareAnnotationValeurCalendar(final AnnotationValeur valeur){

      Calendar newVal = Calendar.getInstance();
      String unknownExisting = null;

      if(((CalendarBox) box).getValue() != null){
         newVal = (((CalendarBox) box).getValue());
      }else{
         newVal = null;
         if(isCombined && combineBox.isChecked()){
            unknownExisting = "system.tk.unknownExistingValue";
         }
      }

      // skip create si la valeur est inchangée
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(unknownExisting == null){
            if(this.valeurs.get(0).getAlphanum() == null && this.valeurs.get(0).getAnnotationValeurId() != null
               && (this.valeurs.get(0).getDate() == newVal || (this.valeurs.get(0).getDate() != null && newVal != null
                  && this.valeurs.get(0).getDate().compareTo(newVal) == 0))){
               log.debug("Annotation date " + champAnnotation.toString() + " inchangée");
               return true;
            }
         }else if(this.valeurs.get(0).getAlphanum() != null){
            // unknownExisting inchangee
            log.debug("Annotation date system.tk.unknownExistingValue " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }
      valeur.setDate(newVal);
      valeur.setAlphanum(unknownExisting);
      return false;
   }

   /**
    * Affiche la valeur spécifiée numeric
    * dans les composants static et edit.
    */
   private void setAnnotationValeurNum(){

      String numV = null;
      BigDecimal doubleV = null;

      // recupere la valeur
      if(this.valeurs != null && this.valeurs.size() > 0){
         numV = this.valeurs.get(0).getAlphanum();
         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(numV)){
               if(numV != null){
                  doubleV = new BigDecimal(numV);
                  annoValue.setValue(ObjectTypesFormatters.doubleLitteralFormatter(doubleV.doubleValue()));
               }
               annoValue.setStyle("font-style: normal");
               ((Decimalbox) box).setValue(doubleV);
               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(this.valeurs.get(0).getAlphanum()));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);
               ((Decimalbox) box).setRawValue(null);
               ((Decimalbox) box).setDisabled(false);
            }
         }else{
            if(numV != null){
               doubleV = new BigDecimal(numV);
               annoValue.setValue(ObjectTypesFormatters.doubleLitteralFormatter(doubleV.doubleValue()));
            }
            annoValue.setValue(null);
            ((Decimalbox) box).setValue(doubleV);
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((Decimalbox) box).setDisabled(true);
         }
         if(!this.hasDefaut){
            ((Decimalbox) box).setRawValue(null);
         }else{
            if(getIsObligatoire() && defauts.get(0).getAlphanum() == null){
               final Constraint c = null;
               ((Decimalbox) box).setConstraint(c);
               ((Decimalbox) box).setRawValue(null);
               ((Decimalbox) box).setConstraint(this.boxConstr);
            }else{
               if(defauts.get(0).getAlphanum() != null){
                  ((Decimalbox) box).setValue(new BigDecimal(defauts.get(0).getAlphanum()));
               }else{
                  ((Decimalbox) box).setRawValue(null);
               }
            }
         }
      }
   }

   /**
    * Prepare la 'objet AnnotationValeur pour le type numeric.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private boolean prepareAnnotationValeurNum(final AnnotationValeur valeur){

      String newVal = null;

      if(((Decimalbox) box).getValue() != null){
         newVal = ObjectTypesFormatters.doubleLitteralFormatter(((Decimalbox) box).getValue().doubleValue());
      }else if(isCombined && combineBox.isChecked()){
         newVal = "system.tk.unknownExistingValue";
      }

      // skip create si la valeur est inchangée
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(this.valeurs.get(0).getAlphanum() == null && this.valeurs.get(0).getAnnotationValeurId() != null
            && (this.valeurs.get(0).getAlphanum() == newVal
               || (this.valeurs.get(0).getAlphanum() != null && this.valeurs.get(0).getAlphanum().equals(newVal)))){
            log.debug("Annotation numerique " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }
      valeur.setAlphanum(newVal);
      return false;
   }

   /**
    * Affiche la valeur spécifiée texte
    * dans les composants static et edit.
    */
   private void setAnnotationValeurTexte(){

      String texteV = null;

      // recupere la valeur
      if(this.valeurs != null && this.valeurs.size() > 0){
         texteV = valeurs.get(0).getTexte();
         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(this.valeurs.get(0).getAlphanum())){
               annoValue.setValue(texteV);
               annoValue.setStyle("font-style: normal");
               ((Textbox) box).setValue(texteV);
               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(this.valeurs.get(0).getAlphanum()));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);
               ((Textbox) box).setValue(null);
               ((Textbox) box).setDisabled(false);
            }
         }else{
            annoValue.setValue(texteV);
            ((Textbox) box).setValue(texteV);
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((Textbox) box).setDisabled(true);
         }
         if(!this.hasDefaut){
            ((Textbox) box).setValue(null);
         }else{
            if(getIsObligatoire() && defauts.get(0).getTexte() == null){
               final Constraint c = null;
               ((Textbox) box).setConstraint(c);
               ((Textbox) box).setValue(null);
               ((Textbox) box).setConstraint(this.boxConstr);
            }else{
               ((Textbox) box).setValue(defauts.get(0).getTexte());
            }
         }
      }
   }

   /**
    * Prepare la 'objet AnnotationValeur pour le type texte.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private boolean prepareAnnotationValeurTexte(final AnnotationValeur valeur){

      String newVal = null;
      String unknownExisting = null;

      if(((Textbox) box).getValue() != null && !((Textbox) box).getValue().equals("")){
         newVal = ((Textbox) box).getValue();
      }else if(isCombined && combineBox.isChecked()){
         unknownExisting = "system.tk.unknownExistingValue";
      }

      // skip create si la valeur est inchangée
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(unknownExisting == null){
            if(this.valeurs.get(0).getAlphanum() == null && this.valeurs.get(0).getAnnotationValeurId() != null
               && (this.valeurs.get(0).getTexte() == newVal
                  || (this.valeurs.get(0).getTexte() != null && this.valeurs.get(0).getTexte().equals(newVal)))){
               log.debug("Annotation texte " + champAnnotation.toString() + " inchangée");
               return true;
            }
         }else if(this.valeurs.get(0).getAlphanum() != null){
            // unknownExisting inchangee
            log.debug("Annotation date system.tk.unknownExistingValue " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }
      valeur.setTexte(newVal);
      valeur.setAlphanum(unknownExisting);
      return false;
   }

   /**
    * Affiche la valeur spécifiée thesaurus
    * dans les composants static et edit.
    */
   private void setAnnotationValeurThesaurus(){

      findSelectedItems(true);

      if(this.valeurs != null && this.valeurs.size() > 0){
         // recupere la valeur et assigne dans selectedItem ou selectedItems

         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(this.valeurs.get(0).getAlphanum())){
               annoValue.setStyle("font-style: normal");

               if(this.selectedItem != null){
                  annoValue.setValue(((Comboitem) this.selectedItem).getLabel());
               }else{
                  annoValue.setValue(null);
               }
               ((Combobox) box).setSelectedItem((Comboitem) this.selectedItem);

               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(this.valeurs.get(0).getAlphanum()));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);

               ((Combobox) box).setSelectedItem(null);
            }
         }else{
            annoValue.setValue(((Comboitem) this.selectedItem).getLabel());
            ((Combobox) box).setSelectedItem((Comboitem) this.selectedItem);
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((Combobox) box).setDisabled(true);
         }
         if(this.hasDefaut){ // valeurs par défaut si non nulles
            findSelectedItems(false);
            // retire la constraint avant setValue si pas valeurs defauts
            if(this.defauts.get(0).getObligatoire() && selectedItem == null){
               final Constraint c = null;
               ((Combobox) box).setConstraint(c);
               ((Combobox) box).setSelectedItem((Comboitem) this.selectedItem);
               ((Combobox) box).setConstraint(boxConstr);
            }else{
               ((Combobox) box).setSelectedItem((Comboitem) this.selectedItem);
            }
         }else{
            ((Combobox) box).setSelectedItem(null);
         }
      }
   }

   /**
    * Prepare la 'objet AnnotationValeur pour le type thesaurus simple.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si les valeurs ont ete crees
    */
   private boolean prepareAnnotationValeurThesaurus(final AnnotationValeur valeur){

      Item newVal = null;
      String unknownExisting = null;

      // attribue newVal, newVals et unkonwExisting
      if(((Combobox) box).getSelectedItem() != null){
         newVal = ((Item) ((Combobox) box).getSelectedItem().getAttribute("tkItem"));
      }else if(isCombined && combineBox.isChecked()){
         unknownExisting = "system.tk.unknownExistingValue";
      }

      //skipCreate si valeurs inchangées
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(unknownExisting == null){
            if(this.valeurs.get(0).getAlphanum() == null && this.valeurs.get(0).getAnnotationValeurId() != null
               && (this.valeurs.get(0).getItem() == newVal
                  || (this.valeurs.get(0).getItem() != null && this.valeurs.get(0).getItem().equals(newVal)))){
               log.debug("Annotation thesaurus " + champAnnotation.toString() + " inchangée");
               return true;
            }else if(newVal == null){ // suppression valeur existante
               this.valeursToDelete.add(this.valeurs.get(0).clone());
               valeurs.clear();
               return true; //skip create only delete
            }
         }else if(this.valeurs.get(0).getAlphanum() != null){
            // unknownExisting inchangee
            log.debug("Annotation thesaurus system.tk.unknownExistingValue " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }

      if(getIsObligatoire() && selectedItem == null && newVal == null){
         throw new WrongValueException(box, Labels.getLabel("anno.thes.empty"));
      }

      // prepare AnnotationValeur
      valeur.setItem(newVal);

      valeur.setAlphanum(unknownExisting);

      return false;
   }

   /**
    * Affiche la valeur spécifiée thesaurus multiple
    * dans les composants static et edit.
    */
   private void setAnnotationValeurThesaurusM(){

      findSelectedItemsM(true);

      if(this.valeurs != null && this.valeurs.size() > 0){
         // recupere la valeur et assigne dans selectedItem ou selectedItems

         // assigne la valeur
         if(isCombined){
            if(!"system.tk.unknownExistingValue".equals(this.valeurs.get(0).getAlphanum())){
               annoValue.setStyle("font-style: normal");

               annoValue.setValue(renderItems(this.selectedItems));
               updateListboxSelection();

               combineBox.setChecked(false);
            }else{
               annoValue.setValue(Labels.getLabel(this.valeurs.get(0).getAlphanum()));
               annoValue.setStyle("font-style: italic");
               combineBox.setChecked(true);
               // multiple ou non
               if(!"thesaurusM".equals(this.champAnnotation.getDataType().getType())){
                  ((Listbox) box).setSelectedItem(null);
               }else{
                  ((Listbox) box).clearSelection();
               }
            }
         }else{
            annoValue.setValue(renderItems(this.selectedItems));
            updateListboxSelection();
         }
      }else{
         annoValue.setValue(null);
         if(isCombined){
            combineBox.setChecked(false);
            ((Listbox) box).setDisabled(true);
         }
         if(this.hasDefaut){ // valeurs par défaut si non nulles
            findSelectedItemsM(false);
         }
         updateListboxSelection();
      }
   }

   /**
    * Prepare l'objet AnnotationValeur pour le type thesaurus multiple.
    * Enregistre/supprime directement les valeurs pour thesaurus multiple et
    * renvoie true.
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si les valeurs ont ete crees
    */
   private boolean prepareAnnotationValeurThesaurusM(final AnnotationValeur valeur){

      final List<Item> newVals = new ArrayList<>();
      String unknownExisting = null;

      // attribue newVal, newVals et unkonwExisting
      if(((Listbox) box).getSelectedItems().size() > 0){
         // recupere les nouvelles valeurs
         final Iterator<Listitem> listIts = ((Listbox) box).getSelectedItems().iterator();
         while(listIts.hasNext()){
            newVals.add((Item) listIts.next().getAttribute("tkItem"));
         }
      }else if(isCombined && combineBox.isChecked()){
         unknownExisting = "system.tk.unknownExistingValue";
      }

      //skipCreate si valeurs inchangées
      if(this.valeurs != null && this.valeurs.size() > 0){
         if(unknownExisting == null && this.valeurs.get(0).getAlphanum() == null){
            final Iterator<AnnotationValeur> valsIt = this.valeurs.iterator();
            boolean valsChanged = false;
            // verifie si toutes les anciennes valeurs sont contenues
            // dans les nouvelles
            AnnotationValeur val;
            while(valsIt.hasNext()){
               val = valsIt.next();
               if(val.getAnnotationValeurId() == null || !newVals.contains(val.getItem())){
                  valsChanged = true;
               }
            }
            // verifie que les nouvelles valeurs ne contiennent
            // pas de valeur en plus des anciennes
            valsChanged = (valsChanged || this.valeurs.size() != newVals.size());
            if(!valsChanged){
               log.debug("Annotation thesaurus multiple" + champAnnotation.toString() + " inchangée");
               return true;
            }
         }else if(this.valeurs.get(0).getAlphanum() != null){
            // unknownExisting inchangee
            log.debug("Annotation thesaurus system.tk.unknownExistingValue " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }

      if(getIsObligatoire() && newVals.isEmpty()){
         throw new WrongValueException(box, Labels.getLabel("anno.thes.empty"));
      }

      // prepare AnnotationValeur
      this.selectedIts = newVals;

      valeur.setAlphanum(unknownExisting);

      return false;
   }

   /**
    * Affiche la valeur spécifiée hyperlien
    * dans les composants static et edit.
    */
   private void setAnnotationValeurHyperlien(){

      setAnnotationValeurAlphanum();

      if(valeurs != null && valeurs.size() > 0 && !"system.tk.unknownExistingValue".equals(valeurs.get(0).getAlphanum())){
         annoValue.setClass("formLink");
         annoValue.addEventListener(Events.ON_CLICK, new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event){
               Clients.evalJavaScript("window.open('" + valeurs.get(0).getAlphanum() + "'" + ",'mywindow','')");

            }
         });
         //annoValue.setAction("onclick: window.open('"
         //		+ valeurs.get(0).getAlphanum() + "')");
      }else{
         annoValue.setClass("formValue");
      }
   }

   /**
    * Affiche la valeur spécifiée fichier
    * dans les composants static et edit.
    */
   private void setAnnotationValeurFichier(){

      String fichierV = null;

      // recupere la valeur
      if(this.valeurs != null && this.valeurs.size() > 0){
         this.fichier = valeurs.get(0).getFichier();
         fichierV = this.fichier.getNom();
         // assigne la valeur
         annoValue.setClass("formLink");
         annoValue.addEventListener("onClick", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               AMedia fMedia = null;
               try{
                  fMedia = new AMedia(valeurs.get(0).getFichier().getNom(), valeurs.get(0).getFichier().getMimeType(), null,
                     new File(valeurs.get(0).getFichier().getPath()), true);
                  // valeurs.get(0).getFichier().getNom());

                  Filedownload.save(fMedia);

               }catch(final Exception e){
                  e.printStackTrace();
               }
            }
         });
      }else{
         this.fichier = new Fichier();
      }
      annoValue.setValue(fichierV);
      fileNameBox.setValue(fichierV);
      showDeleteAndFileNameBox((this.valeurs != null && this.valeurs.size() > 0));
   }

   /**
    * Prepare la 'objet AnnotationValeur pour le type fichier.
    * Cree un fichier temporaire en attendant la validation.
    *
    * @return boolean skipCreate si aucun fichier a charger.
    */
   private boolean prepareAnnotationValeurFichier(){

      String newVal = null;

      if(fileNameBox.getValue() != null && fileNameBox.getValue() != ""){
         newVal = fileNameBox.getValue();
      }

      // skip create si la valeur est inchangée cad meme nom de fichier
      // et pas de File temp
      if(this.valeurs != null && this.valeurs.size() > 0){
         if((this.valeurs.get(0).getFichier().getNom() == newVal
            || (this.valeurs.get(0).getFichier().getNom() != null && this.valeurs.get(0).getFichier().getNom().equals(newVal)))
            // && temp == null) {
            && stream == null){
            log.debug("Annotation fichier " + champAnnotation.toString() + " inchangée");
            return true;
         }
      }

      // prepare les objets pour la creation de la valeur de fichier
      if(newVal != null){
         fichier.setNom(newVal);
      }else{ // supprime le fichier
         if(this.valeurs != null && this.valeurs.size() > 0){
            this.valeursToDelete.add(valeurs.get(0));
            this.valeurs.clear();
         }
         //setAnnotationValeurFichier();
         return true;
      }
      return false;
   }

   /**
    * Prepare l'objet AnnotationValeur
    *
    * @param valeur AnnotationValeur
    * @return boolean skipCreate si la valeur est inchangée
    */
   private Boolean prepareAnnotationValeur(AnnotationValeur valeur){
      Boolean skipCreate = false;
      String dataType = this.champAnnotation.getDataType().getType();

      switch(dataType){
         default:
            break;
         case "alphanum":
            skipCreate = prepareAnnotationValeurAlphanumOrHyperlien(valeur);
            break;
         case "hyperlien":
            skipCreate = prepareAnnotationValeurAlphanumOrHyperlien(valeur);
            break;
         case "texte":
            skipCreate = prepareAnnotationValeurTexte(valeur);
            break;
         case "boolean":
            skipCreate = prepareAnnotationValeurBooleen(valeur);
            break;
         case "date":
            skipCreate = prepareAnnotationValeurDate(valeur);
            break;
         case "datetime":
            skipCreate = prepareAnnotationValeurCalendar(valeur);
            break;
         case "num":
            skipCreate = prepareAnnotationValeurNum(valeur);
            break;
         case "thesaurus":
            skipCreate = prepareAnnotationValeurThesaurus(valeur);
            break;
         case "thesaurusM":
            // met a jour les valeurs si elles ont changé
            skipCreate = prepareAnnotationValeurThesaurusM(valeur);
            if(!skipCreate){ // les valeurs ont changé
               skipCreate = createValeursThesaurusMultiple();
            }
            break;
         case "fichier":
            skipCreate = prepareAnnotationValeurFichier();
            valeur.setFichier(fichier);
            valeur.setStream(stream);
            break;
         case "calcule":
            //La valeur du calcul des champs calculés n'est pas stockée en base
            skipCreate = true;
            break;
         case "duree":
            skipCreate = prepareAnnotationValeurDuree(valeur);
            break;
      }

      return skipCreate;
   }

   /**
    * Realise les listes qui enverront les objets AnnotationValeur vers les
    * opérations enregistrement/modification commandées par la
    * fiche annotation (sauf pour le thesaurus multiple, fonction déléguée
    * à {@link #createValeursThesaurusMultiple()}).
    *
    * @param forceNew true force la creation d'une nouvelle valeur (utile
    *                 pour la creation multiple)
    */
   public void createOrUpdateAnnotationValeur(final boolean forceNew){

      try{
         AnnotationValeur valeur = null;
         boolean isCreation = true;

         // verifie si une valeur est assignée et doit être modifiée
         // forcement creation si thesaurus multiple
         if(this.valeurs != null && this.valeurs.size() > 0 && !forceNew
            && !this.champAnnotation.getDataType().getType().equals("thesaurusM")){
            valeur = this.valeurs.get(0);
            isCreation = false;
         }else{ // creation nouvelle valeur
            if(this.champAnnotation.getDataType().getType().equals("boolean") && this.valeurs != null && this.valeurs.size() > 0){
               // c'est la valeur boolInit qui est modifiée
               valeur = this.valeurs.get(0);
               isCreation = false;
            }else{
               valeur = new AnnotationValeur();
               valeur.setBanque(getBanqueForValeur(false));
            }
         }

         // flag utile si thesaurus multiple car operations create/update
         // sont faites dans la boucle
         Boolean skipCreate = prepareAnnotationValeur(valeur);

         if(!skipCreate){
            if(!valeur.isEmpty()){
               valeur.setChampAnnotation(this.champAnnotation);
               // passe la valeur dans la liste d'affichage et
               // dans la bonne liste
               if(isCreation){
                  this.valeurs.add(valeur);
               }
               if(!this.valeursToCreateOrUpdate.contains(valeur)){
                  this.valeursToCreateOrUpdate.add(valeur);
               }
            }else{
               this.valeurs.clear();
               this.valeursToDelete.add(valeur.clone());
            }
         }
      }catch(final WrongValueException ve){
         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         /*annoGroup.setOpen(true);
         Clients.scrollIntoView(annoGroup);*/
         if(tableComponent instanceof Group){
            ((Group) tableComponent).setOpen(true);
         }
         Clients.scrollIntoView(tableComponent);
         /** END **/
         throw ve;
      }
   }

   /**
    * Recherche banque depuis l'objet pour permettre modification
    * en modes toutes collections. Si patient, aucune banque associée à
    * l'objet donc recherche l'attribut 'bank' passé au groupe lors de
    * la création (lorsque 'ToutesCollections', les groupes peuvent avoir
    * des références vers Banque différentes)
    *
    * @param forceByGroup force la recherche de la banque dans
    *                     l'attribut passé au groupe. Utile pour les modifications multiples pour
    *                     toutes collections.
    * @return Banque
    */
   private Banque getBanqueForValeur(final boolean forceByGroup){
      if(!forceByGroup){
         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         if(null != getFicheAnnotation() && null != getFicheAnnotation().getObject()
            && ("Patient".equals(getFicheAnnotation().getObject().entiteNom())
               || "Prelevement".equals(getFicheAnnotation().getObject().entiteNom())
               || "Echantillon".equals(getFicheAnnotation().getObject().entiteNom())
               || "Cession".equals(getFicheAnnotation().getObject().entiteNom()))){
            if(getFicheAnnotation().getObject().getBanque() != null){
               return getFicheAnnotation().getObject().getBanque();
            }
            return getFicheAnnotation().getBankUsedToDrawChamps();
         }
         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         if(null != getFicheAnnotationInline() && null != getFicheAnnotationInline().getObject()
            && ("Patient".equals(getFicheAnnotationInline().getObject().entiteNom())
            || "Prelevement".equals(getFicheAnnotationInline().getObject().entiteNom())
            || "Echantillon".equals(getFicheAnnotationInline().getObject().entiteNom())
            || "Cession".equals(getFicheAnnotation().getObject().entiteNom()))){
            if(null != getFicheAnnotationInline().getObject().getBanque()){
               return getFicheAnnotationInline().getObject().getBanque();
            }
            return getFicheAnnotationInline().getBankUsedToDrawChamps();
         }
      }
      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       */
      //return (Banque) annoGroup.getAttribute("bank");
      return (Banque) tableComponent.getAttribute("bank");
      /** END **/
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    */
   private FicheAnnotationInline getFicheAnnotationInline(){
      return (FicheAnnotationInline) self.getParent().getParent().getParent()
         .getAttributeOrFellow("fwinAnnotationInline$composer", true);
   }

   /**
    * Enregistre les annotations valeurs provenant d'un thesaurus multiple
    * dans la base de données.
    * Supprime les anciennes. Enregistre les nouvelles ou une seule valeur
    * system.tk.unknownExistingValue ou rien du tout.
    *
    * @return boolean true si skipCreate
    */
   private boolean createValeursThesaurusMultiple(){

      this.valeursToDelete.addAll(this.valeurs);
      this.valeurs.clear();

      if(this.selectedIts.size() > 0){
         // enregistre les nouvelles valeurs
         AnnotationValeur valeur;
         final Iterator<Item> listIts = this.selectedIts.iterator();
         while(listIts.hasNext()){
            valeur = new AnnotationValeur();
            valeur.setItem(listIts.next());
            valeur.setBanque(getBanqueForValeur(false));
            valeur.setChampAnnotation(this.champAnnotation);
            this.valeurs.add(valeur);
            // thesaurus multiple implique creation
            this.valeursToCreateOrUpdate.add(valeur);
         }

         // met a jour les valeurs
         setAnnotationValeurThesaurusM();

         return true;
      }
      return false;
   }

   /**
    * Trouve parmi leslistitems l'objet
    * selectionnés correspondant à l'item spécifié dans les
    * AnnotationValeur ou AnnotationDefaut.
    *
    * @boolean isValeur
    */
   private void findSelectedItems(final boolean isValeur){

      this.selectedItem = null;
      this.selectedItems.clear();

      if(isValeur){
         if(this.valeurs != null && this.valeurs.size() > 0){
            // parcoure les items pour trouver selectedItem
            for(int i = 0; i < this.listitems.size(); i++){
               if(((Comboitem) this.listitems.get(i)).getAttribute("tkItem") != null
                  && ((Comboitem) this.listitems.get(i)).getAttribute("tkItem").equals(valeurs.get(0).getItem())){
                  this.selectedItem = this.listitems.get(i);
                  break;
               }
            }
         }else{ // select valeur vide thesaurus
            for(int i = 0; i < this.listitems.size(); i++){
               if(((Comboitem) this.listitems.get(i)).getAttribute("tkItem") == null){
                  this.selectedItem = this.listitems.get(i);
                  break;
               }
            }
         }
      }else{ // donc isDefaut
         if(this.defauts != null && this.defauts.size() > 0){
            for(int i = 0; i < this.defauts.size(); i++){
               // parcoure les comboItems pour trouver selectedItem
               if(defauts.get(i).getItem() != null){
                  for(int j = 0; j < this.listitems.size(); j++){
                     if(defauts.get(i).getItem().equals(((Comboitem) this.listitems.get(j)).getAttribute("tkItem"))){
                        this.selectedItem = this.listitems.get(j);
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Trouve parmi les listitems les objets
    * selectionnés correspondant aux Item(s) spécifiés dans les
    * AnnotationValeur ou AnnotationDefaut.
    *
    * @boolean isValeur
    */
   private void findSelectedItemsM(final boolean isValeur){

      this.selectedItem = null;
      this.selectedItems.clear();

      if(isValeur){
         if(this.valeurs != null && this.valeurs.size() > 0){
            for(int i = 0; i < this.valeurs.size(); i++){
               // parcoure les listItems pour trouver les selectedItems
               for(int j = 0; j < this.listitems.size(); j++){
                  if(((Listitem) this.listitems.get(j)).getAttribute("tkItem") != null
                     && ((Listitem) this.listitems.get(j)).getAttribute("tkItem").equals(valeurs.get(i).getItem())){
                     this.selectedItems.add((Listitem) this.listitems.get(j));
                     break;
                  }
               }
            }
         }else{ // select valeur vide thesaurus
            for(int i = 0; i < this.listitems.size(); i++){
               if(((Listitem) this.listitems.get(i)).getAttribute("tkItem") == null){
                  this.selectedItem = this.listitems.get(i);
                  break;
               }
            }
         }
      }else{ // donc isDefaut
         if(this.defauts != null && this.defauts.size() > 0){
            for(int i = 0; i < this.defauts.size(); i++){
               // parcoure les listItems pour trouver les selectedItems
               for(int j = 0; j < this.listitems.size(); j++){
                  if(((Listitem) this.listitems.get(j)).getAttribute("tkItem") != null
                     && ((Listitem) this.listitems.get(j)).getAttribute("tkItem").equals(defauts.get(i).getItem())){
                     this.selectedItems.add((Listitem) this.listitems.get(j));
                     break;
                  }
               }
            }
         }
      }
   }

   /**
    * Trouve parmi les listitems l'objet correspondant
    * au booleen spécifié dans les
    * AnnotationValeur ou AnnotationDefaut.
    *
    * @boolean isValeur
    */
   private void findSelectedBool(final boolean isValeur){

      this.selectedItem = null;

      if(isValeur){
         if(this.valeurs != null && this.valeurs.size() > 0){
            // parcoure les items pour trouver selectedItem
            for(int i = 0; i < this.listitems.size(); i++){
               if(((Listitem) this.listitems.get(i)).getValue() != null
                  && ((Listitem) this.listitems.get(i)).getValue().equals(valeurs.get(0).getBool())){
                  this.selectedItem = this.listitems.get(i);
                  break;
               }
            }
         }else{ // select valeur vide thesaurus
            for(int i = 0; i < this.listitems.size(); i++){
               if(((Listitem) this.listitems.get(i)).getValue() == null){
                  this.selectedItem = this.listitems.get(i);
                  break;
               }
            }
         }
      }else{ // donc isDefaut
         if(this.defauts != null && this.defauts.size() > 0){
            for(int i = 0; i < this.defauts.size(); i++){
               // parcoure les listItems pour trouver selectedItem
               if(defauts.get(i).getBool() != null){
                  for(int j = 0; j < this.listitems.size(); j++){
                     if(defauts.get(i).getBool().equals(((Listitem) this.listitems.get(j)).getValue())){
                        this.selectedItem = this.listitems.get(j);
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Workaround pour pouvoir spécifier une datebox à null si
    * elle a deja eu une valeur.
    */
   private void detachAnReattachDatebox(){
      box.detach();
      createDatebox();
      annoBox.appendChild(box);
   }

   /***** create composants methodes ********/

   /**
    * Dessine la dateBox avec contrainte et valeur par défaut.
    */
   private void createDatebox(){
      box = new Datebox();
      ((Datebox) box).setFormat(Labels.getLabel("validation.date.format.simple"));
      ((Datebox) box).setCols(10);

      initConstraintDate();
      ((Datebox) box).setConstraint(this.boxConstr);
      ((Datebox) box).setLenient(false);

      if(isCombined){
         ((Datebox) box).setDisabled(true);
      }
   }

   /**
    * Dessine la calendar avec contrainte et valeur par défaut.
    */
   private void createCalendarbox(){
      box = (CalendarBox) getPage().getComponentDefinition("calendarbox", false).newInstance(getPage(),
         "fr.aphp.tumorotek.component.CalendarBox");

      //box.setId("defautMacroComponent");
      box.applyProperties();
      ((CalendarBox) box).afterCompose();

      if(getIsObligatoire()){
         ((CalendarBox) box).setConstraint("no empty");
      }

      if(isCombined){
         ((CalendarBox) box).setDisabled(true);
      }
   }

   /**
    * Cree la contrainte appliquée à au composant editable pour
    * une annotation date.
    */
   private void initConstraintDate(){
      //applique la possibilite nullable
      if(getIsObligatoire()){
         //this.boxConstr = new SimpleConstraint(SimpleConstraint.NO_EMPTY,
         //							Labels.getLabel("anno.date.empty"));
         final ConstDateLimit cst = new ConstDateLimit();
         cst.setNullable(false);
         this.boxConstr = cst;
      }else{
         final ConstDateLimit cst = new ConstDateLimit();
         cst.setNullable(true);
         this.boxConstr = cst;
      }
   }

   /**
    * Dessine la box Duree avec contrainte et valeur par défaut.
    */
   private void createDureeBox(){
      DureeComponent dureeBox = new DureeComponent();

      if(isCombined){
         dureeBox.setDisabled(true);
      }

      this.box = dureeBox;
   }

   /**
    * Dessine la TextBox avec contrainte et valeur par défaut.
    */
   private void createTextbox(){
      Textbox textbox = new Textbox();
      // ((Textbox) box).setCols(15);
      textbox.setHflex("1");

      initConstraintAlphanum();
      textbox.setConstraint(this.boxConstr);

      if(isCombined){
         textbox.setDisabled(true);
      }

      this.box = textbox;
   }

   /**
    * Cree la contrainte appliquée à au composant editable pour
    * une annotation alphanum.
    */
   private void initConstraintAlphanum(){
      // applique contrainte format
      if("alphanum".equals(this.champAnnotation.getDataType().getType())){
         this.boxConstr = new ConstAlphanum();
      }else if("hyperlien".equals(this.champAnnotation.getDataType().getType())){
         this.boxConstr = new ConstHyperlien();
      }

      //applique la possibilite nullable
      ((TumoTextConstraint) this.boxConstr).setNullable(!this.hasDefaut || !this.defauts.get(0).getObligatoire());
      // applique la contrainte de taille
      ((TumoTextConstraint) this.boxConstr).setSize(100);
   }

   /**
    * Dessine la drop down liste oui-non pour un composant
    * de formulaire de type booleen.
    * Si non obligatoire, ajoute une ligne vide.
    */

   private void createBoolbox(){
      Listitem listIt = null;

      box = new Listbox();
      box.setMold("select");

      listitems = new ArrayList<>();

      // valeur par defaut ou non
      if(!getIsObligatoire()){
         listIt = new Listitem();
         listIt.setLabel("");
         this.listitems.add(listIt);
         box.appendChild(listIt);
      }

      // OUI
      final Boolean oui = new Boolean(true);
      listIt = new Listitem();
      this.listitems.add(listIt);
      listIt.setLabel(Labels.getLabel("annotation.boolean.oui"));
      listIt.setValue(oui);
      box.appendChild(listIt);

      // NON
      final Boolean non = new Boolean(false);
      listIt = new Listitem();
      this.listitems.add(listIt);
      listIt.setLabel(Labels.getLabel("annotation.boolean.non"));
      listIt.setValue(non);
      box.appendChild(listIt);
   }

   /**
    * Dessine la DooubleBox avec contrainte et valeur par défaut.
    */
   private void createDoublebox(){
      box = new Decimalbox();
      box.setHflex("1");

      ((Decimalbox) box).setFormat("0.###");
      ((Decimalbox) box).setLocale("en");
      ((Decimalbox) box).setScale(3);

      initConstraintNum();
      ((Decimalbox) box).setConstraint(this.boxConstr);

      if(isCombined){
         ((Decimalbox) box).setDisabled(true);
      }
   }

   /**
    * Cree la contrainte appliquée à au composant editable pour
    * une annotation numerique.
    */
   private void initConstraintNum(){
      //applique la possibilite nullable
      if(getIsObligatoire()){
         this.boxConstr = new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("anno.num.empty"));
      }
   }

   /**
    * Dessine la TextBox avec contrainte et valeur par défaut
    * pour un champ texte.
    */
   private void createTextboxForText(){
      box = new Textbox();
      ((Textbox) box).setRows(3);
      box.setHflex("1");

      // ((Textbox) box).setCols(15);

      initConstraintTexte();
      ((Textbox) box).setConstraint(this.boxConstr);

      if(isCombined){
         ((Textbox) box).setDisabled(true);
      }
   }

   /**
    * Cree la contrainte appliquée à au composant editable pour
    * une annotation texte.
    */
   private void initConstraintTexte(){
      //applique la possibilite nullable
      if(getIsObligatoire()){
         this.boxConstr = new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("anno.texte.empty"));
      }
   }

   /**
    * Dessine le thesaurus simple sous la forme d'un Listbox..
    */

   private void createThesaurusBox(){
      final Set<Item> its = ManagerLocator.getChampAnnotationManager().getItemsManager(this.champAnnotation,
         SessionUtils.getSelectedBanques(sessionScope).get(0));
      // Items si thesaurus
      Iterator<Item> itemItor = null;
      // recupere les items
      itemItor = its.iterator();
      Integer maxLength = ManagerLocator.getChampAnnotationManager().findMaxItemLength(its);

      if(maxLength == 0){
         maxLength = 15;
      }

      Comboitem comboIt = null;

      box = new Combobox();
      ((Combobox) box).setButtonVisible(true);
      ((Combobox) box).setAutodrop(true);
      // ((Combobox) box).setWidth("150px");
      box.setHflex("1");

      listitems = new ArrayList<>();

      // ajoute items au thesaurus
      if(itemItor != null){
         Item it;

         // valeur par defaut ou non
         if(!getIsObligatoire()){
            comboIt = new Comboitem();
            comboIt.setLabel("");
            this.listitems.add(comboIt);
            comboIt.setAttribute("tkItem", null);
            box.appendChild(comboIt);
         }
         while(itemItor.hasNext()){
            it = itemItor.next();
            comboIt = new Comboitem();
            this.listitems.add(comboIt);
            comboIt.setLabel(it.getLabel());
            comboIt.setValue(it.getValeur());
            // ajoute l'item en attr
            comboIt.setAttribute("tkItem", it);
            box.appendChild(comboIt);
         }
         if(isCombined){
            ((Combobox) box).setDisabled(true);
         }
      }

      initConstrThesaurus();
      ((Combobox) box).setConstraint(boxConstr);

   }

   /**
    * Dessine le thesaurus multiple sous la forme d'un Listbox..
    */

   private void createThesaurusMBox(){
      final Set<Item> its = ManagerLocator.getChampAnnotationManager().getItemsManager(this.champAnnotation,
         SessionUtils.getSelectedBanques(sessionScope).get(0));
      // Items si thesaurus
      Iterator<Item> itemItor = null;
      // recupere les items
      itemItor = its.iterator();
      Integer maxLength = ManagerLocator.getChampAnnotationManager().findMaxItemLength(its);

      if(maxLength == 0){
         maxLength = 15;
      }

      Listitem listIt = null;

      box = new Listbox();
      //((Listbox) box).setWidth(String.valueOf(maxLength * 10) + "px");
      ((Listbox) box).setMultiple(true);
      ((Listbox) box).setCheckmark(true);
      box.setHflex("1");

      // ((Listbox) box).setFixedLayout(false);

      listitems = new ArrayList<>();

      // ajoute items au thesaurus
      if(itemItor != null){
         Item it;

         // ajoute contrainte non nulle
         for(int i = 0; i < this.defauts.size(); i++){
            if(this.hasDefaut && this.defauts.get(i).getObligatoire()){
               box.addEventListener("onSelect", new EventListener<Event>()
               {
                  @Override
                  public void onEvent(final Event event) throws Exception{
                     if(((Listbox) box).getSelectedItems().size() == 0){
                        throw new WrongValueException(box, Labels.getLabel("anno.thes.empty"));
                     }
                     Clients.clearWrongValue((box));
                  }
               });
               break;
            }
         }
         while(itemItor.hasNext()){
            it = itemItor.next();
            listIt = new Listitem();
            this.listitems.add(listIt);
            listIt.setLabel(it.getLabel());
            listIt.setValue(it.getValeur());
            // ajoute l'item en attr
            listIt.setAttribute("tkItem", it);
            listIt.setParent(box);
         }
         if(isCombined){
            ((Listbox) box).setDisabled(true);
         }
      }

      // pour affichage a la ligne des valeurs
      annoValue.setPre(true);
   }

   /**
    * Cree la contrainte appliquée à au composant editable pour
    * une annotation thesaurus.
    */
   private void initConstrThesaurus(){
      //applique la possibilite nullable
      if(getIsObligatoire()){
         this.boxConstr = new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("anno.thes.empty"));
      }
   }

   /**
    * Dessine la TextBox avec aucunes contrainte.
    * N'accepte ni les valeurs par defaut, ni le combine, ni obligatoire.
    */
   private void createFilebox(){
      box = new Hbox();
      box.setHflex("1");
      ((Hbox) box).setPack("start");
      ((Hbox) box).setAlign("start");

      fileNameBox = new Textbox();
      fileNameBox.setCols(15);
      box.appendChild(fileNameBox);
      addFile = new Image();
      addFile.setSrc("/images/icones/fileimport.png");
      //addFile.setImage("/images/icones/fileimport.png");
      //addFile.setLabel(Labels.getLabel("general.upload"));
      addFile.setStyle("cursor: pointer");
      addFile.addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            final Media[] medias =
               Fileupload.get(ObjectTypesFormatters.getLabel("general.upload.limit", new String[] {String.valueOf(sizeLimit)}),
                  "annotation.upload.title", 1, sizeLimit, true);
            if(medias != null && medias.length > 0){
               handleUploadFile(medias[0]);

            }
         }

      });
      box.appendChild(addFile);

      deleteFile = new Image();
      deleteFile.setSrc("/images/icones/small_delete12.png");
      deleteFile.setStyle("cursor: pointer");
      deleteFile.addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            if(fileNameBox.getErrorMessage() == null){
               fileNameBox.setValue(null);
               showDeleteAndFileNameBox(false);
            }
         }

      });
      box.appendChild(deleteFile);

      // visibilité de delete/textbox
      showDeleteAndFileNameBox((this.valeurs != null && this.valeurs.size() > 0));

      final ConstFilename fileNameConstraint = new ConstFilename();
      fileNameConstraint.setNullable(false);
      fileNameConstraint.setSize(50);

      fileNameBox.addEventListener("onBlur", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            fileNameConstraint.validate(fileNameBox, fileNameBox.getValue());
         }
      });

   }

   /**************** file composant specific methods *************/

   /**
    * Affiche ou non les composants delete/textbox pour une annotation fichier
    * en mode edit.
    *
    * @param boolean visible
    */
   private void showDeleteAndFileNameBox(final boolean visible){
      fileNameBox.setVisible(visible);
      deleteFile.setVisible(visible);
   }

   /**
    * Cree un fichier temporaire dans l'orborescence.
    * assigne le nouveau nom de fichier dans la textbox.
    *
    * @param file
    * @throws Exception
    */
   private void handleUploadFile(final Media file) throws Exception{
      if(file != null){

         // log.debug("creation fichier temporaire " + temp.getAbsolutePath());
         if(stream != null){
            stream.close();
         }
         stream = new ByteArrayInputStream(file.getByteData());
         fileNameBox.setValue(file.getName());
         Events.postEvent("onBlur", fileNameBox, null);

         // showDeleteAndFileNameBox(temp != null);
         showDeleteAndFileNameBox(true);
      }
   }

   public void clearValeurLists(){
      //clear listes
      this.valeursToCreateOrUpdate.clear();
      this.valeursToDelete.clear();

      try{
         if(stream != null){
            stream.close();
            stream = null;
         }
      }catch(final IOException e){
         e.printStackTrace();
      }
   }

   /**
    * Realise une deep copy de la liste de valeurs d'annotation afin d'avoir
    * un etat conservé de ces valeurs avant modification.
    */
   public void backUpValeursBeforeEdition(){
      this.backUp.clear();
      for(int i = 0; i < this.valeurs.size(); i++){
         this.backUp.add(this.valeurs.get(i).clone());
      }
   }

   /**
    * Recharge les valeurs du backUp par deep copy dans valeurs.
    * Methode appelée lors de l'echec de l'enregistrement de
    * creations/modifications.
    */
   public void revertValeurs(){
      clearValeurLists();
      this.valeurs.clear();
      for(int i = 0; i < this.backUp.size(); i++){
         this.valeurs.add(this.backUp.get(i).clone());
      }
   }

   /********************* Modification multiple *******************/

   /**
    * Cree la liste d'AnnotationValeur qui sera passée à la modale
    * de modification multiple, à partir de la liste de TKAnnotableObject.
    * Si le TKAnnotableObject n'a pas de valeur, une empty lui est
    * attribuée en vue de la création.
    *
    * @param liste d'objets en modification multiple
    */
   public void setMultiObjs(final List<TKAnnotableObject> objs){
      this.multiObjs = objs;
      // cree la liste de Valeurs
      for(int i = 0; i < objs.size(); i++){
         final List<AnnotationValeur> vals =
            ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(this.champAnnotation, objs.get(i));
         if(vals.size() > 0){
            if(!this.champAnnotation.getDataType().getType().equals("thesaurusM")){
               this.annoMultiValeurs.addAll(vals);
            }else{
               this.annoMultiValeurs.add(vals);
            }
         }else{ // ajoute une empty valeurs pour l'objet
            final AnnotationValeur empty = new AnnotationValeur();
            empty.setObjetId(objs.get(i).listableObjectId());
            empty.setChampAnnotation(champAnnotation);
            empty.setBanque(getBanqueForValeur(true));
            if(!this.champAnnotation.getDataType().getType().equals("thesaurusM")){
               this.annoMultiValeurs.add(empty);
            }else{
               vals.add(empty);
               this.annoMultiValeurs.add(vals);
            }

         }
      }
   }

   /**
    * Dessine le champ Annotation dans le cadre d'une modification
    * multiple.
    * Recupere les valeurs d'annotations pour chacun des TKAnnotableObjet
    * et les arguments qui seront passés à la modal contenant le
    * composant de modification multiple.
    */
   private void drawAnnotModifMultiple(){
      String dataType = this.champAnnotation.getDataType().getType();
      annoMultiLabel = new Label();
      mainBox.appendChild(annoMultiLabel);
      if("calcule".equals(dataType)){
         annoMultiLabel.setValue(Labels.getLabel("general.edit.modification.multiple.champCalcule"));
      }else{
         annoMultiLabel.setValue(Labels.getLabel("general.edit.modification.multiple"));
         annoMultiLabel.setSclass("formLink");

         this.allItems.clear();
         // prepare les arguments a passer a la modale en fonction
         // du type du champ.

         switch(dataType){
            default:
               break;
            case "alphanum":
               this.multiComponentType = "Textbox";
               this.champToEdit = "alphanum";
               initConstraintAlphanum();
               break;
            case "hyperlien":
               this.multiComponentType = "Textbox";
               this.champToEdit = "alphanum";
               initConstraintAlphanum();
               break;
            case "texte":
               this.multiComponentType = "BigTextbox";
               this.champToEdit = "texte";
               initConstraintTexte();
               break;
            case "boolean":
               this.multiComponentType = "Listbox";
               this.allItems.add(new Boolean(true));
               this.allItems.add(new Boolean(false));
               this.champToEdit = "bool";
               break;
            case "date":
               this.multiComponentType = "Datebox";
               this.champToEdit = "date";
               initConstraintDate();
               break;
            case "datetime":
               this.multiComponentType = "Calendarbox";
               this.champToEdit = "date";
               initConstraintDate();
               break;
            case "num":
               this.multiComponentType = "Doublebox";
               this.champToEdit = "alphanum";
               initConstraintNum();
               break;
            case "thesaurus":
               this.champToEdit = "item";
               this.allItems.addAll(ManagerLocator.getChampAnnotationManager().getItemsManager(this.champAnnotation,
                  SessionUtils.getSelectedBanques(sessionScope).get(0)));
               this.itemProp = "label";
               this.multiComponentType = "Combobox";
               break;
            case "thesaurusM":
               this.champToEdit = "item";
               this.allItems.addAll(ManagerLocator.getChampAnnotationManager().getItemsManager(this.champAnnotation,
                  SessionUtils.getSelectedBanques(sessionScope).get(0)));
               this.itemProp = "label";
               this.multiComponentType = "MultiListbox";
               break;
            case "fichier":
               this.multiComponentType = "Filebox";
               this.champToEdit = "fichier";
               break;
            case "duree":
               this.multiComponentType = "DureeBox";
               this.champToEdit = "alphanum";
               break;
         }

         if(!annoMultiLabel.getValue().equals("-")){
            annoMultiLabel.addEventListener("onClick", new EventListener<Event>()
            {
               @Override
               public void onEvent(final Event event) throws Exception{
                  getFicheAnnotation().openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnAnnot",
                     multiComponentType, annoMultiValeurs, champAnnotation.getNom(), champToEdit, allItems, itemProp,
                     champAnnotation.getTableAnnotation().getEntite().getNom(), boxConstr, isCombined, null,
                     (!champToEdit.equals("bool") && getIsObligatoire()));
               }
            });
         }
      }
   }

   /**
    * ???
    * @param value
    * @param its
    * @return
    */
   private String getProperty(){
      String property = null;
      switch(this.champAnnotation.getDataType().getType()){
         default:
            break;
         case "alphanum":
            property = "alphanum";
            break;
         case "hyperlien":
            property = "alphanum";
            break;
         case "texte":
            property = "texte";
            break;
         case "boolean":
            property = "bool";
            break;
         case "date":
            property = "date";
            break;
         case "datetime":
            break;
         case "num":
            property = "alphanum";
            break;
         case "thesaurus":
            property = "item";
            break;
         case "thesaurusM":
            property = "multiThes";
            break;
         case "fichier":
            property = "fichier";
            break;
         case "calcule":
            break;
         case "duree":
            property = "alphanum";
            break;
      }
      return property;
   }

   private void formatValue(String property, SimpleChampValue value, Set<?> its){
      switch(property){
         default:
            break;
         case "alphanum":
            if(value.getValue() != null && value.getValue() instanceof BigDecimal){
               value.setValue(ObjectTypesFormatters.doubleLitteralFormatter(((BigDecimal) value.getValue()).doubleValue()));
            }
            break;
         case "multiThes":
            its.clear();
            if(value.getValue() != null){
               if(!value.getValue().equals("system.tk.unknownExistingValue")){
                  Object champValue = value.getValue();
                  if(Set.class.isInstance(champValue)){
                     its = Set.class.cast(champValue);
                  }
               }
            }else if(hasDefaut){
               // recupere list Item depuis defauts
               its = extractItemsFromDefauts(defauts);
               final StringBuilder bld = new StringBuilder();
               final Iterator<?> objsIt = its.iterator();
               while(objsIt.hasNext()){
                  Item item = Item.class.cast(objsIt.next());
                  bld.append(item.getLabel());
                  // connecteur ' - '
                  if(objsIt.hasNext()){
                     bld.append(" - ");
                  }
               }
               value.setPrintValue(bld.toString());
            }
            break;
      }
   }

   /**
    * ??
    * @param value
    */
   private void creerAnnotationFichier(SimpleChampValue value){
      // créé annotation fichier
      if(value.getValue() == null){ // delete ttes valeurs
         for(int i = 0; i < this.annoMultiValeurs.size(); i++){
            this.valeursToDelete.add((AnnotationValeur) this.annoMultiValeurs.get(i));
         }
      }else{
         final FilePack pck = (FilePack) value.getValue();
         // stream -> // delete ttes valeurs pour reecrire autres
         if(pck.getStream() != null){
            for(int i = 0; i < this.annoMultiValeurs.size(); i++){
               this.valeursToDelete.add((AnnotationValeur) this.annoMultiValeurs.get(i));
            }

            final AnnotationValeur val = new AnnotationValeur();
            val.setChampAnnotation(this.champAnnotation);
            val.setBanque(getBanqueForValeur(false));
            val.setFichier(pck.getFile());
            val.setStream(pck.getStream());
            this.valeursToCreateOrUpdate.add(val);
         }else{ // maj valeurs
            AnnotationValeur fileVal;
            for(int i = 0; i < this.annoMultiValeurs.size(); i++){
               fileVal = (AnnotationValeur) this.annoMultiValeurs.get(i);

               // complète la valeur vide pour les objets qui n'en avaient pas
               if(fileVal.getFichier() == null){
                  fileVal.setFichier(pck.getFile().cloneNoId());
                  this.valeursToCreateOrUpdate.add(fileVal);
               }else{
                  // modification nom fichier uniquement
                  if(pck.getFile().getPath().equals(fileVal.getFichier().getPath())){
                     if(!fileVal.getFichier().getNom().equals(pck.getFile().getNom())){
                        fileVal.getFichier().setNom(pck.getFile().getNom());
                        this.valeursToCreateOrUpdate.add(fileVal);
                     }
                  }else{ // path est different -> implique suppression + recreation
                     this.valeursToDelete.add(fileVal);
                     final AnnotationValeur val = new AnnotationValeur();
                     val.setChampAnnotation(this.champAnnotation);
                     val.setBanque(getBanqueForValeur(false));
                     val.setObjetId(fileVal.getObjetId());
                     val.setFichier(pck.getFile().cloneNoId());
                     this.valeursToCreateOrUpdate.add(val);
                  }
               }
            }
         }
      }

   }

   /**
    * ??
    * @param its
    * @param setUnknowExistingValue
    */
   private void creerAnnotationMultiThesaurus(Set<?> its, boolean setUnknowExistingValue){
      for(Object obj : this.annoMultiValeurs){
         if(List.class.isInstance(obj)){
            List<?> annoValeurLisr = List.class.cast(obj);
            List<AnnotationValeur> toDelete = new ArrayList<>();
            for(Object o : annoValeurLisr){
               if(AnnotationValeur.class.isInstance(o)){
                  toDelete.add(AnnotationValeur.class.cast(o));
               }
            }
            this.valeursToDelete.addAll(toDelete);
         }
      }
      // recree les valeurs pour chaque objet
      AnnotationValeur val;
      if(!setUnknowExistingValue){
         Iterator<?> itsIt;
         for(int j = 0; j < this.multiObjs.size(); j++){
            itsIt = its.iterator();
            while(itsIt.hasNext()){
               val = new AnnotationValeur();
               val.setChampAnnotation(this.champAnnotation);
               val.setBanque(getBanqueForValeur(true));
               val.setObjetId(this.multiObjs.get(j).listableObjectId());
               val.setItem(Item.class.cast(itsIt.next()));
               this.valeursToCreateOrUpdate.add(val);
            }
         }
      }else{
         for(int j = 0; j < this.multiObjs.size(); j++){
            val = new AnnotationValeur();
            val.setChampAnnotation(this.champAnnotation);
            val.setBanque(getBanqueForValeur(true));
            val.setObjetId(this.multiObjs.get(j).listableObjectId());
            val.setAlphanum("system.tk.unknownExistingValue");
            this.valeursToCreateOrUpdate.add(val);
         }
      }
   }

   /**
    * ??
    * @param property
    * @param value
    * @param setUnknowExistingValue
    */
   private void creerAnnotation(String property, SimpleChampValue value, Boolean setUnknowExistingValue){
      for(int i = 0; i < this.annoMultiValeurs.size(); i++){
         AnnotationValeur cloneVal = ((AnnotationValeur) this.annoMultiValeurs.get(i)).clone();
         try{
            if(isCombined){ // nettoie par defaut
               PropertyUtils.setSimpleProperty(cloneVal, "alphanum", null);
            }
            if(!setUnknowExistingValue){
               PropertyUtils.setSimpleProperty(cloneVal, property, value.getValue());
            }else{
               PropertyUtils.setSimpleProperty(cloneVal, property, null);
               PropertyUtils.setSimpleProperty(cloneVal, "alphanum", value.getValue());
            }
         }catch(final Exception e){
            log.error(e);
         }
         addValeursIntoActionLists(cloneVal);
      }
   }

   /**
    * ??
    * @param valueFormatted
    */
   private void dessinerValeur(String valueFormatted){
      // dessine la valeur
      final StringBuffer sb = new StringBuffer();
      sb.append("[");
      if(valueFormatted != null){
         sb.append(valueFormatted);
      }else{
         sb.append(" ");
      }
      sb.append("]");
      sb.append(" - ");
      annoValue.setValue(sb.toString());
      annoValue.setVisible(true);
   }

   /**
    * Traitement de modifications multiples d'annotations
    * @param event retourne par la validation de la modification multiple.
    */

   public void onGetChangeOnAnnot(final Event event){
      this.valeursToCreateOrUpdate.clear();
      this.valeursToDelete.clear();

      final SimpleChampValue value = (SimpleChampValue) event.getData();
      Set<?> its = new HashSet<>();
      String property = getProperty();
      formatValue(property, value, its);

      String valueFormatted = value.getPrintValue();
      boolean setUnknowExistingValue = false;

      if(value.getValue() != null && value.getValue().equals("system.tk.unknownExistingValue")){
         valueFormatted = Labels.getLabel((String) value.getValue());
         setUnknowExistingValue = true;
      }

      // transformation en Calendar
      if(value.getValue() != null && value.getValue() instanceof Date){
         final Calendar c = Calendar.getInstance();
         c.setTime((Date) value.getValue());
         value.setValue(c);
      }

      if("multiThes".equals(property)){
         creerAnnotationMultiThesaurus(its, setUnknowExistingValue);
      }else if("fichier".equals(property)){
         creerAnnotationFichier(value);
      }else{
         creerAnnotation(property, value, setUnknowExistingValue);
      }

      dessinerValeur(valueFormatted);
   }

   /**
    * Ajoute la valeur dans la liste des AnnotationValeur
    * à modifier ou supprimer si elle n'est pas devenu empty, dans la
    * liste des valeurs à supprimer sinon.
    *
    * @param AnnotationValeur
    */
   private void addValeursIntoActionLists(final AnnotationValeur val){
      if(!val.isEmpty()){
         this.valeursToCreateOrUpdate.add(val);
      }else{
         this.valeursToDelete.add(val);
      }
   }

   /**
    * Recoit l'information reset venant de la page de modification
    * multiple.
    */
   public void onResetMulti(){
      this.valeursToCreateOrUpdate.clear();
      this.valeursToDelete.clear();
      annoValue.setValue(null);
   }

   /**
    * Formate l'affichage d'une liste d'item(s) pour le mode static.
    *
    * @param its liste Listitem à afficher
    * @return liste formatée pour affichage
    */
   private String renderItems(final Set<Listitem> items){
      final StringBuilder strbld = new StringBuilder();
      final Iterator<Listitem> itor = items.iterator();
      Component item;
      String valeur;
      while(itor.hasNext()){
         item = itor.next();
         strbld.append("- ");
         strbld.append(((Listitem) item).getLabel());
         valeur = (String) ((Listitem) item).getValue();
         if(valeur != null){
            strbld.append(" [");
            strbld.append(valeur);
            strbld.append("]");
         }
         if(itor.hasNext()){ // ajoute pas ligne si dernier
            strbld.append("\n");
         }
      }
      return strbld.toString();
   }

   /**
    * Recupere une liste d'Item à partir d'une liste d'AnnotationDefaut
    * aspécifiée pour un champ thesaurus choix multiples.
    *
    * @param defs defauts
    * @return liste d'Item.
    */
   private Set<Item> extractItemsFromDefauts(final List<AnnotationDefaut> defs){
      final Set<Item> its = new HashSet<>();
      final Iterator<AnnotationDefaut> it = defs.iterator();
      AnnotationDefaut next;
      while(it.hasNext()){
         next = it.next();
         if(next.getItem() != null){
            its.add(next.getItem());
         }
      }
      return its;
   }

   /**
    * Appelle la methode getValue ou les contrôles d'obligation
    * pour générer au besoin la levée d'une wrongValueException.
    */
   public void validateComponent(){

      try{
         switch(this.champAnnotation.getDataType().getType()){
            default:
               break;
            case "alphanum":
               Textbox.class.cast(box).getValue();
               break;
            case "hyperlien":
               Textbox.class.cast(box).getValue();
               break;
            case "texte":
               Textbox.class.cast(box).getValue();
               break;
            case "boolean":
               break;
            case "date":
               Datebox.class.cast(box).getValue();
               break;
            case "datetime":
               CalendarBox.class.cast(box).getValue();
               break;
            case "num":
               Decimalbox.class.cast(box).getValue();
               break;
            case "thesaurus":
               if(getIsObligatoire() && selectedItem == null && Combobox.class.cast(box).getSelectedItem() == null){
                  throw new WrongValueException(box, Labels.getLabel("anno.thes.empty"));
               }
               break;
            case "thesaurusM":
               if(getIsObligatoire() && Listbox.class.cast(box).getSelectedItems() == null){
                  throw new WrongValueException(box, Labels.getLabel("anno.thes.empty"));
               }
               break;
            case "fichier":
               break;
            case "calcule":
               break;
            case "duree":
               if(getIsObligatoire() && (DureeComponent.class.cast(box).getDuree() == null
                  || DureeComponent.class.cast(box).getDuree().getTemps(Duree.SECONDE) == 0)){
                  throw new WrongValueException(box, Labels.getLabel("anno.duree.empty"));
               }
               break;
         }

      }catch(final WrongValueException ve){
         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         /*annoGroup.setOpen(true);
         Clients.scrollIntoView(annoGroup);*/
         if(tableComponent instanceof Group){
            ((Group) tableComponent).setOpen(true);
         }
         Clients.scrollIntoView(tableComponent);
         /** END **/
         throw ve;
      }
   }
}
