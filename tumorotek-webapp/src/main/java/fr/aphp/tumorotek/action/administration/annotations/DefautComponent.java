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
package fr.aphp.tumorotek.action.administration.annotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstAlphanum;
import fr.aphp.tumorotek.action.constraints.ConstHyperlien;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.utils.Duree;

/**
 * MacroComponent dessinant les composant editables permettant à
 * l'utilisateur d'enregistrer des valeurs par défaut.
 * Date 12/05/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class DefautComponent extends HtmlMacroComponent
{

   private final Logger log = LoggerFactory.getLogger(DefautComponent.class);

   private static final long serialVersionUID = 1L;

   private Component box = null;

   private ChampAnnotationDecorator chpDeco;

   private Component backComp;

   private Column editCol;

   private Column deleteCol;

   private ChampCalcule champCalculeCopy;

   //   private Champ selectedChamp;

   private final List<Item> itemsCopy = new ArrayList<>();

   private Set<AnnotationDefaut> defauts = new LinkedHashSet<>();

   private List<Listitem> boolsItem;
   //   private List<DataType> allowedDataTypeList = new ArrayList<>();

   // flag blockModal utilise pour ouverture modale item
   private boolean blockModal = false;

   public void setChpDeco(final ChampAnnotationDecorator cpD){
      this.chpDeco = cpD;
   }

   public void setBackComp(final Component bC){
      this.backComp = bC;
   }

   @Override
   public void afterCompose(){

      super.afterCompose();

      drawDefautEditablesComponents();

      addEventListeners();
   }

   /**
    * Dessine les composants editables permettant de renseigner les valeurs
    * par défaut en fonction du type de ChampAnnotation.
    */
   private void drawDefautEditablesComponents(){
      if(this.chpDeco.isAlphanum()){
         box = drawAlphanumBox();
      }else if(this.chpDeco.isBoolean()){
         box = createBoolBox();
      }else if(this.chpDeco.isDate()){
         box = drawDateBox();
      }else if(this.chpDeco.isDatetime()){
         box = drawDatetimeBox();
      }else if(this.chpDeco.isNum()){
         box = drawNumBox();
      }else if(this.chpDeco.isDuree()){
         box = drawDureeBox();
      }else if(this.chpDeco.isTexte()){
         box = drawTextBox();
      }else if(this.chpDeco.isThesaurus()){
         box = drawItemGrid();
      }else if(this.chpDeco.isChampCalcule()){
         box = drawChampCalculeGrid();
      }else if(this.chpDeco.isHyperlien()){
         box = drawHyperlienBox();
      }

      defauts.clear();
      defauts.addAll(chpDeco.getDefauts());

      getFirstChild().getFellow("defautBoxDiv").appendChild(box);
   }

   /**
    * Dessine la Textbox pour un Champ alphanumérique
    * @return Textbox pour un Champ alphanumérique
    */
   private Textbox drawAlphanumBox(){
      final Textbox textbox = new Textbox();
      textbox.setHflex("1");
      // ((Textbox) box).setCols(15);
      final ConstAlphanum constr = new ConstAlphanum();
      constr.setNullable(true);
      textbox.setConstraint(constr);
      if(chpDeco.getDefauts().iterator().next().getAlphanum() != null){
         textbox.setValue(chpDeco.getDefauts().iterator().next().getAlphanum());
      }
      return textbox;
   }

   /**
    * Dessine la DecimalBox pour un Champ numérique
    * @return DecimalBox pour un Champ numérique
    */
   private Decimalbox drawNumBox(){
      final Decimalbox decimalbox = new Decimalbox();
      decimalbox.setHflex("1");
      decimalbox.setFormat("0.###");
      decimalbox.setLocale("en");
      decimalbox.setScale(3);
      if(chpDeco.getDefauts().iterator().next().getAlphanum() != null){
         decimalbox.setValue(new BigDecimal(chpDeco.getDefauts().iterator().next().getAlphanum()));
      }
      return decimalbox;
   }

   /**
    * Dessine la Textbox pour un Champ texte
    * @return la Textbox pour un Champ texte
    */
   private Textbox drawTextBox(){
      final Textbox textbox = new Textbox();
      textbox.setRows(3);
      textbox.setHflex("1");
      if(chpDeco.getDefauts().iterator().next().getTexte() != null){
         textbox.setValue(chpDeco.getDefauts().iterator().next().getTexte());
      }

      return textbox;
   }

   /**
    * Dessine la Datebox pour un Champ date
    * @return la Datebox pour un Champ date
    */
   private Datebox drawDateBox(){
      final Datebox datebox = new Datebox();
      datebox.setFormat(Labels.getLabel("validation.date.format.simple"));
      datebox.setWidth("200px");
      if(chpDeco.getDefauts().iterator().next().getDate() != null){
         datebox.setValue(chpDeco.getDefauts().iterator().next().getDate().getTime());
      }

      return datebox;
   }

   /**
    * Dessine la Textbox pour un Champ hyperlien
    * @return Textbox pour un Champ hyperlien
    */
   private Textbox drawHyperlienBox(){
      final Textbox textbox = new Textbox();
      textbox.setHflex("1");
      final ConstHyperlien constr = new ConstHyperlien();
      constr.setNullable(true);
      textbox.setConstraint(constr);
      if(chpDeco.getDefauts().iterator().next().getAlphanum() != null){
         textbox.setValue(chpDeco.getDefauts().iterator().next().getAlphanum());
      }

      return textbox;
   }

   /**
    * Dessine la CalendarBox pour un Champ datetime
    * @return la CalendarBox pour un Champ datetime
    */
   private CalendarBox drawDatetimeBox(){
      CalendarBox calendarbox = null;
      final Component component =
         getPage().getComponentDefinition("calendarbox", false).newInstance(getPage(), "fr.aphp.tumorotek.component.CalendarBox");
      if(CalendarBox.class.isInstance(component)){
         calendarbox = CalendarBox.class.cast(component);
         //box.setId("defautMacroComponent");
         calendarbox.applyProperties();
         calendarbox.afterCompose();
         // ((Datebox) box).setWidth("200px");
         if(chpDeco.getDefauts().iterator().next().getDate() != null){
            calendarbox.setValue(chpDeco.getDefauts().iterator().next().getDate());
         }
      }

      return calendarbox;
   }

   /**
    * Dessine le DureeComponent pour un Champ Duree
    * @return DureeComponent Div pour un Champ Duree
    * @since 2.2.0
    */
   private DureeComponent drawDureeBox(){
      final DureeComponent dureebox = new DureeComponent();
      dureebox.setWidth("400px");
      // Remplissage des champs si modification et si valeur existante
      final String valeurDefaut = chpDeco.getDefauts().iterator().next().getAlphanum();
      if(null != valeurDefaut && !"".equals(valeurDefaut)){
         dureebox.setDuree(new Duree(new Long(valeurDefaut), Duree.SECONDE));
      }

      // Event listener permettant d'enregistrer la durée à la validation
      getFirstChild().getFellow("validateButton").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            final Duree duree = dureebox.getDuree();
            final Long secondes = duree.getTemps(Duree.SECONDE);
            defauts.iterator().next().setAlphanum(secondes.toString());
         }
      });
      return dureebox;
   }

   /**
    * Dessine la Grid listant les items.
    * L'élément Rows gere les evenements renvoyes depuis les images
    * boutons delete-edit.
    * La Grid utilise pour model la deep copy de la liste items.
    * @return la Grid
    */
   private Grid drawItemGrid(){
      final Grid itemGrid = new Grid();
      itemGrid.setHflex("1");
      itemGrid.setSclass("gridListStyle");
      itemGrid.setMold("paging");
      itemGrid.setPageSize(20);
      itemGrid.setSizedByContent(true);
      itemGrid.setSpan(true);
      //Columns
      final Columns cols = new Columns();
      itemGrid.appendChild(cols);
      final Column labelCol = new Column();
      //labelCol.setWidth("250px");
      labelCol.setLabel(Labels.getLabel("annotation.item.label"));
      cols.appendChild(labelCol);
      final Column valCol = new Column();
      //valCol.setWidth("250px");
      valCol.setLabel(Labels.getLabel("annotation.item.valeur"));
      cols.appendChild(valCol);
      final Column defautCol = new Column();
      defautCol.setLabel(Labels.getLabel("annotation.item.defaut"));
      // defautCol.setWidth("60px");
      cols.appendChild(defautCol);
      editCol = new Column();
      // editCol.setWidth("10px");
      cols.appendChild(editCol);
      deleteCol = new Column();
      // deleteCol.setWidth("10px");
      cols.appendChild(deleteCol);
      //Rows recoit les event listeners
      final Rows rows = new Rows();
      itemGrid.appendChild(rows);
      //Model
      //copy la liste items
      itemsCopy.clear();
      final List<Item> its = new ArrayList<>(chpDeco.getItems());
      for(int i = 0; i < its.size(); i++){
         itemsCopy.add(i, its.get(i).clone());
      }

      final ListModel<Item> model = new ListModelList<>(itemsCopy);
      itemGrid.setModel(model);
      //Renderer
      final AnnoItemRowRenderer renderer = new AnnoItemRowRenderer();
      renderer.setDefauts(defauts);
      itemGrid.setRowRenderer(renderer);

      // Ajout des events listeners pour les actions
      rows.addEventListener("onClickUpdateItem", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            openItemModale((Item) event.getData(), false);
         }
      });
      rows.addEventListener("onClickDeleteItem", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            deleteItemFromList((Item) event.getData());
         }
      });
      // affiche le bouton addItem et le rend fonctionnel
      getFirstChild().getFellow("addItemButton").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            openItemModale(new Item(), true);
         }
      });
      getFirstChild().getFellow("addItemButton").setVisible(true);

      this.addEventListener("onAddItem", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            addOrUpdateItemIntoList((Item) event.getData(), true);
         }
      });

      this.addEventListener("onUpdateItem", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            addOrUpdateItemIntoList((Item) event.getData(), false);
         }
      });

      this.addEventListener("onAddDefautForItem", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            addDefautsToList((Item) event.getData());
         }
      });

      this.addEventListener("onDropDefautForItem", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            removeDefautsFromList((Item) event.getData());
         }
      });

      return itemGrid;
   }

   /**
    * Déssine la Listbox pour un Champ boolean
    * @return la Listbox pour un Champ boolean
    */
   private Listbox createBoolBox(){
      Listitem listIt = null;

      final Listbox listbox = new Listbox();
      listbox.setMold("select");

      boolsItem = new ArrayList<>();

      listIt = new Listitem();
      listIt.setLabel("");
      listbox.appendChild(listIt);
      boolsItem.add(listIt);

      // OUI
      final Boolean oui = new Boolean(true);
      listIt = new Listitem();
      listIt.setLabel(Labels.getLabel("annotation.boolean.oui"));
      listIt.setValue(oui);
      listbox.appendChild(listIt);
      boolsItem.add(listIt);

      // NON
      final Boolean non = new Boolean(false);
      listIt = new Listitem();
      listIt.setLabel(Labels.getLabel("annotation.boolean.non"));
      listIt.setValue(non);
      listbox.appendChild(listIt);
      boolsItem.add(listIt);

      listbox.setSelectedItem(boolsItem.get(0));

      if(chpDeco.getDefauts().iterator().next().getBool() != null){
         if(chpDeco.getDefauts().iterator().next().getBool()){
            listbox.setSelectedItem(boolsItem.get(1));
         }else{
            listbox.setSelectedItem(boolsItem.get(2));
         }
      }else{
         listbox.setSelectedItem(boolsItem.get(0));
      }

      return listbox;
   }

   /**
    * Dessine la Grid pour l'édition d'un ChampCalcule
    * @return ChampCalculeComponent Grid pour ChampCalcule
    * @since 2.2.0
    */
   private ChampCalculeComponent drawChampCalculeGrid(){
      champCalculeCopy = chpDeco.getChampCalcule();
      final ChampCalculeComponent champCalculeGrid = new ChampCalculeComponent();
      champCalculeGrid.setBanque(this.chpDeco.getCurrentBanque());
      if(null != champCalculeCopy){
         champCalculeGrid.setChampCalcule(champCalculeCopy.clone());
      }else{ // Nouveau champCalcule
         champCalculeCopy = new ChampCalcule();
         champCalculeCopy.setChampAnnotation(chpDeco.getChamp());
         champCalculeGrid.setChampCalcule(champCalculeCopy);
      }

      // Event listener permettant de récupérer le champCalculer à la validation
      getFirstChild().getFellow("validateButton").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            // Validation de la saisie du champ Calculé
            final ChampCalcule champCalcule = champCalculeGrid.getChampCalcule();
            if(null == champCalcule){
               throw new WrongValueException(box, Labels.getLabel("anno.champ1.empty"));
            }else if(null == champCalcule.getChamp1()){
               throw new WrongValueException(box, Labels.getLabel("anno.champCalcule.champ1.empty"));
            }else if(null == champCalcule.getOperateur()){
               throw new WrongValueException(box, Labels.getLabel("anno.champCalcule.operateur.empty"));
            }else if(null == champCalcule.getChamp2()){
               if(null == champCalcule.getValeur()){
                  throw new WrongValueException(box, Labels.getLabel("anno.champCalcule.champ2orValue.empty"));
               }
            }

            champCalculeCopy = champCalcule;
         }
      });

      return champCalculeGrid;
   }

   /**
    * Ajoute des listeners aux boutons valider et annuler
    */
   private void addEventListeners(){
      // Events listeners
      getFirstChild().getFellow("validateButton").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            recordAnnotationDefaut();
         }
      });
      getFirstChild().getFellow("cancelButton").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            cancelAnnotationDefaut();
         }
      });
   }

   /**
    * Recupere les valeurs specifiée pour les assigner au
    * ChampAnnotationDecorator.
    * Ferme la modale.
    * Post back un event pour rafraichir la div defaut dans la ligne
    * de la liste de champs FicheTableAnnotation.
    */
   private void recordAnnotationDefaut(){
      extractValuesFromDefautBox();
      Events.postEvent(new Event("onUpdateDefautDiv", backComp, null));

      Events.postEvent(new Event("onClose", getParent()));
   }

   /**
    * Ferme la modale.
    */
   private void cancelAnnotationDefaut(){
      Events.postEvent(new Event("onClose", getParent()));
   }

   /**
    * Extrait les valeurs d'AnnotationDefaut
    * à assigner au champAnnotationDecorator.
    * Récupère également les items pour les thesaurus
    */
   private void extractValuesFromDefautBox(){
      if(this.chpDeco.isAlphanum() || this.chpDeco.isHyperlien()){
         if(!((Textbox) box).getValue().equals("")){
            defauts.iterator().next().setAlphanum(((Textbox) box).getValue());
         }else{
            defauts.iterator().next().setAlphanum(null);
         }
      }else if(this.chpDeco.isBoolean()){
         if(((Listbox) box).getSelectedItem() != null){
            defauts.iterator().next().setBool((Boolean) ((Listbox) box).getSelectedItem().getValue());
         }else{
            defauts.iterator().next().setBool(null);
         }
      }else if(this.chpDeco.isDate()){
         if(((Datebox) box).getValue() != null){
            final Calendar sDate = Calendar.getInstance();
            sDate.setTime(((Datebox) box).getValue());
            defauts.iterator().next().setDate(sDate);
         }else{
            defauts.iterator().next().setDate(null);
         }
      }else if(this.chpDeco.isDatetime()){
         if(((CalendarBox) box).getValue() != null){
            final Calendar sDate = ((CalendarBox) box).getValue();
            defauts.iterator().next().setDate(sDate);
         }else{
            defauts.iterator().next().setDate(null);
         }
      }else if(this.chpDeco.isNum()){
         if(((Decimalbox) box).getValue() != null){
            defauts.iterator().next().setAlphanum(((Decimalbox) box).getValue().toString());
         }else{
            defauts.iterator().next().setAlphanum(null);
         }
      }else if(this.chpDeco.isTexte()){
         if(!((Textbox) box).getValue().equals("")){
            defauts.iterator().next().setTexte(((Textbox) box).getValue());
         }else{
            defauts.iterator().next().setTexte(null);
         }
      }else if(this.chpDeco.isThesaurus()){
         chpDeco.setItems(itemsCopy);
      }else if(this.chpDeco.isChampCalcule()){
         chpDeco.setChampCalcule(champCalculeCopy);
      }else if(this.chpDeco.isDuree()){
         if(null != DureeComponent.class.cast(box).getDuree()
            && 0 != DureeComponent.class.cast(box).getDuree().getTemps(Duree.SECONDE)){
            defauts.iterator().next().setAlphanum(DureeComponent.class.cast(box).getDuree().getTemps(Duree.SECONDE).toString());
         }else{
            defauts.iterator().next().setAlphanum(null);
         }
      }
      chpDeco.setDefauts(defauts);
   }

   /**
    * Ouvre la modale permettant l'enregistrement/modification
    * d'un item de thesaurus.
    * Si thesaurus simple et un defaut deja selectionné, envoie
    * l'instruction disable defaut.
    * Disable le checkbox defaut si thesaurus simple avec une
    * AnnotationDefaut non associe a l'Item passe en parametre.
    * @param Item item
    * @param boolean flag isCreate si modal en mode creation
    * @param boolean disableDefaut
    */
   public void openItemModale(final Item item, final boolean isCreate){

      if(!blockModal){

         blockModal = true;

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("itemModal");
         win.setPage(getPage());
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("annotation.item.createOrUpdate"));
         win.setBorder("normal");
         win.setPosition("center");
         //win.setWidth("75%");
         //win.setHeight("75px");
         win.setClosable(true);

         final ItemComponent ua = (ItemComponent) getPage().getComponentDefinition("itemComp", false).newInstance(getPage(),
            "fr.aphp.tumorotek.action.administration." + "annotations.ItemComponent");
         ua.setItem(item);
         ua.setBackComp(this);
         ua.setIsCreate(isCreate);

         // checkbox defaut
         if(!chpDeco.isThesaurusM()){
            final Iterator<AnnotationDefaut> itor = defauts.iterator();
            Item it;
            while(itor.hasNext()){
               it = itor.next().getItem();
               if(it != null && !it.equals(item)){
                  ua.setDisableDefaut(true);
                  break;
               }
            }
         }
         ua.setIsDefaut(AnnoItemRowRenderer.isItemDefaut(item, defauts));

         ua.setParent(win);
         ua.setId("itemMacroComponent");
         ua.applyProperties();
         ua.afterCompose();
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               ua.setVisible(true);
               blockModal = false;
            }
         });
         final Timer timer = new Timer();
         timer.setDelay(200);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();
         try{
            win.onModal();

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   /**
    * Methode generale de mise à jour la liste d'Item a partir
    * de itemsCopy.
    */
   private void updateItemList(){

      final ListModel<Item> model = new ListModelList<>(itemsCopy);
      ((Grid) box).setModel(model);

      ((Grid) box).renderAll();
   }

   /**
    * Met a jour la liste d'Item apres retour de l'utilisation
    * de la modale Item.
    * @param Item renvoyé par la modale
    * @param isCreate si nouvel, false sinon.
    */
   private void addOrUpdateItemIntoList(final Item item, final boolean isCreate){
      // ajout
      if(item != null && isCreate){
         item.setChampAnnotation(chpDeco.getChamp());

         if(chpDeco.getCatalogueChp()){
            item.setPlateforme(chpDeco.getCurrentBanque().getPlateforme());
         }

         if(!itemsCopy.contains(item)){
            itemsCopy.add(item);
         }
      }
      updateItemList();
   }

   /**
    * Retire un item de la liste.
    * @param it
    */
   private void deleteItemFromList(final Item it){
      if(it != null){
         // warning
         if(it.getItemId() != null && ManagerLocator.getChampAnnotationManager().isUsedItemManager(it)){
            // confirmation demandée
            if(Messagebox.show(Labels.getLabel("item.deletion.isUsed"), Labels.getLabel("message.deletion.title"),
               Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               itemsCopy.remove(it);
               final Iterator<AnnotationDefaut> itor = defauts.iterator();
               AnnotationDefaut def;
               while(itor.hasNext()){
                  def = itor.next();
                  if(it.equals(def.getItem())){
                     def.setItem(null);
                     break;
                  }
               }
               updateItemList();
            }
         }else{ // suppression sans confirmation
            itemsCopy.remove(it);
            final Iterator<AnnotationDefaut> itor = defauts.iterator();
            AnnotationDefaut def;
            while(itor.hasNext()){
               def = itor.next();
               if(it.equals(def.getItem())){
                  def.setItem(null);
                  break;
               }
            }
            updateItemList();
         }
      }
   }

   /**
    * Met à jour les valeurs par défaut pour un thesaurus
    * simple ou à choix multiple.
    */
   private void addDefautsToList(final Item it){
      final AnnotationDefaut def = new AnnotationDefaut();
      def.setObligatoire(false);
      def.setChampAnnotation(chpDeco.getChamp());
      //def.setBanque(bank);
      def.setItem(it);
      defauts.add(def);
      ((Grid) box).renderAll();
   }

   /**
    * Supprime une annotation defaut de la liste pour un thesaurus
    * simple ou a choix multiple.
    * @param item
    */
   private void removeDefautsFromList(final Item item){
      final Iterator<AnnotationDefaut> itor = defauts.iterator();
      AnnotationDefaut def;
      while(itor.hasNext()){
         def = itor.next();
         if(def.getItem() != null && def.getItem().equals(item)){
            defauts.remove(def);
            break;
         }
      }
      ((Grid) box).renderAll();
   }

   /**
    * Affiche la modale en mode statique en rendant les boutons invisible.
    * @return
    */
   public void setStatique(){
      getFirstChild().getFellow("buttonsBox").setVisible(false);
      if(editCol != null){ // thesaurus
         editCol.setVisible(false);
         deleteCol.setVisible(false);
      }
   }

   public Set<AnnotationDefaut> getDefauts(){
      return defauts;
   }

   public void setDefauts(final Set<AnnotationDefaut> defs){
      this.defauts = defs;
   }

}