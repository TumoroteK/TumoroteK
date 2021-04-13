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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.I3listBoxItemRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheTableAnnotation extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(FicheTableAnnotation.class);

   private static final long serialVersionUID = 6300875937416491348L;

   //Buttons
   private Button addChamp;

   private Grid champsGrid;
   private Column editColumn;
   private Column deleteColumn;
   private Column upColumn;
   private Column downColumn;

   // Labels
   private Label nomLabel;
   private Label descriptionLabel;
   private Label entiteLabel;

   // Editable components : mode d'édition ou de création.
   private Label nomRequired;
   private Textbox nomBox;
   private Textbox descriptionBox;
   private Listbox entiteBox;

   // banque
   private Group groupBanques;
   private Div banquesAssociees;

   private Menubar menuBar;
   private Rows rows;

   // Objets Principaux.
   private TableAnnotation table;

   // Champ annotations decorators.
   private final List<ChampAnnotationDecorator> champs = new ArrayList<>();
   private final List<ChampAnnotationDecorator> copyChamps = new ArrayList<>();
   private final List<ChampAnnotationDecorator> champsToCreateOrEdit = new ArrayList<>();
   private final List<ChampAnnotation> champsToDelete = new ArrayList<>();
   private ChampAnnotation beforeEditClone;
   private ChampAnnotationDecorator currentChampEdited;
   private int ordreMax = 0;

   // Associations.
   private final List<Banque> banques = new ArrayList<>();
   private final List<Banque> copyBanques = new ArrayList<>();
   private final List<Entite> entites = new ArrayList<>();
   private Entite selectedEntite;
   //private List<Catalogue> catalogues = new ArrayList<Catalogue>();
   //private Catalogue selectedCatalogue;
   //private Catalogue emptyCatalogue = new Catalogue();
   private final List<DataType> types = new ArrayList<>();
   private List<String> booleanValue = new ArrayList<>();

   private DataType selectedDataType;

   private static I3listBoxItemRenderer entiteRenderer = new I3listBoxItemRenderer("nom");
   private static I3listBoxItemRenderer typeRenderer = new I3listBoxItemRenderer("type");

   private boolean isCatalogueTable = false;
   private String selectedBooleanValue;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.tableAnnotation");
      setCascadable(true);
      setDeletable(true);
      setFantomable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.entiteLabel, this.nomLabel, this.descriptionLabel, this.menuBar
         //this.catalogueLabel
      });

      setObjBoxsComponents(new Component[] {this.entiteBox, this.nomBox, this.descriptionBox});

      setRequiredMarks(new Component[] {this.nomRequired});

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      drawActionsForAnnotations();

      //emptyCatalogue.setNom("--");

      Executions.createComponents("/zuls/administration/BanquesAssociees.zul", banquesAssociees, null);
      getBanquesAssociees().setGroupHeader(groupBanques);

      menuBar.setVisible(false);

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.table = (TableAnnotation) obj;

      isCatalogueTable = table.getCatalogue() != null;

      initAssociations();

      super.setObject(table);
   }

   @Override
   public void disableToolBar(final boolean b){
      super.disableToolBar(b);

      deleteC.setDisabled(isCatalogueTable);
      addChamp.setDisabled(isCatalogueTable);
   }

   @Override
   public void cloneObject(){
      setClone(this.table.clone());
      copyChamps.clear();
      for(int i = 0; i < champs.size(); i++){
         copyChamps.add(champs.get(i).clone());
      }
      copyBanques.clear();
      for(int i = 0; i < banques.size(); i++){
         copyBanques.add(banques.get(i).clone());
      }
   }

   @Override
   public void revertObject(){
      super.revertObject();
      setChamps(getCopyChamps());
      setBanques(getCopyBanques());
   }

   @Override
   public TableAnnotation getObject(){
      return this.table;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public Prelevement getParentObject(){
      return null;
   }

   @Override
   public AnnotationsController getObjectTabController(){
      return (AnnotationsController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new TableAnnotation());
      super.setNewObject();
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      addChamp.setVisible(true);
      editColumn.setVisible(true);
      deleteColumn.setVisible(true);
      upColumn.setVisible(true);
      downColumn.setVisible(true);

      // ajoute la banque courante en creation
      getBanquesAssociees().switchToCreateMode();

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.table.equals(new TableAnnotation()));

      editColumn.setVisible(false);
      deleteColumn.setVisible(false);
      upColumn.setVisible(false);
      downColumn.setVisible(false);
      addChamp.setVisible(false);

      this.currentChampEdited = null;
      this.beforeEditClone = null;

      // re-init des listes des champs a manipuler
      this.champsToDelete.clear();
      this.champsToCreateOrEdit.clear();

      getBanquesAssociees().switchToStaticMode();

      if(this.table.getTableAnnotationId() == null){
         menuBar.setVisible(false);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      initEditableMode();

      addChamp.setVisible(true);
      editColumn.setVisible(true);
      deleteColumn.setVisible(true);
      upColumn.setVisible(true);
      downColumn.setVisible(true);

      // entite non modifiable
      entiteLabel.setVisible(true);
      entiteBox.setVisible(false);

      // si catalogue champ le nom et la description de la table
      // ne sont pas modifiables
      if(isCatalogueTable){
         nomLabel.setVisible(true);
         nomBox.setVisible(false);
         descriptionLabel.setVisible(true);
         descriptionBox.setVisible(false);

         getBanquesAssociees().switchToStaticMode();
      }else{
         getBanquesAssociees().switchToEditMode(true);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){
      setEmptyToNulls();

      final List<ChampAnnotation> chps = prepareChamps();

      if(banques.isEmpty()){
         banques.add(SessionUtils.getSelectedBanques(sessionScope).get(0));
      }
      ManagerLocator.getTableAnnotationManager().createOrUpdateObjectManager(table, selectedEntite, null, chps, banques, null,
         SessionUtils.getLoggedUser(sessionScope), "creation", SessionUtils.getSystemBaseDir(),
         SessionUtils.getPlateforme(sessionScope));

      // ordonne les champs
      ManagerLocator.getTableAnnotationManager().updateChampOrdersManager(ChampAnnotationDecorator.unDecore(champs));
      updateDecorators(chps);

      // ajout de la banque à la liste
      getObjectTabController().getListe().addToObjectList(table);
      getObjectTabController().getListe().selectlastRow(table);

      // update si banque courante
      if(banques.contains(SessionUtils.getSelectedBanques(sessionScope).get(0))){
         getMainWindow().updateSelectedBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      }
   }

   @Override
   public void onClick$addNewC(){
      switchToCreateMode();
   }

   @Override
   public void onClick$createC(){
      Clients.showBusy(Labels.getLabel("ficheAnno.modification.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   /**
    * @see super.onClick$createC. Ajoute la gestion de la fermeture
    * de la busy box.
    */
   @Override
   public void onLaterCreate(){
      try{
         revertCurrentEdition();
         createNewObject();
         cloneObject();
         switchToStaticMode();
         disableToolBar(false);
         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public boolean prepareDeleteObject(){
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      return false;
   }

   @Override
   public void removeObject(final String comments){
      // suppression
      ManagerLocator.getTableAnnotationManager().removeObjectManager(table, comments, SessionUtils.getLoggedUser(sessionScope),
         SessionUtils.getSystemBaseDir());
   }

   @Override
   public void onLaterDelete(final Event event){
      super.onLaterDelete(event);

      // update si banque courante
      if(banques.contains(SessionUtils.getSelectedBanques(sessionScope).get(0))){
         getMainWindow().updateSelectedBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      }
   }

   @Override
   public void onLaterRevert(){
      try{
         revertCurrentEdition();
         clearConstraints();
         switchToStaticMode();
         revertObject();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   @Override
   public void onClick$validateC(){
      Clients.showBusy(Labels.getLabel("ficheAnno.modification.encours"));
      Events.echoEvent("onLaterValidate", self, null);
   }

   @Override
   public void onLaterValidate(){
      try{
         revertCurrentEdition();
         updateObject();
         cloneObject();
         switchToStaticMode();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   @Override
   public void setEmptyToNulls(){
      if(this.table.getDescription().equals("")){
         this.table.setDescription(null);
      }
      //		if (this.selectedCatalogue != null
      //				&& this.selectedCatalogue.equals(emptyCatalogue)) {
      //			this.selectedCatalogue = null;
      //		}
   }

   @Override
   public void updateObject(){

      setEmptyToNulls();

      final List<ChampAnnotation> chps = prepareChamps();

      // délétion des champs à supprimer
      for(int i = 0; i < champsToDelete.size(); i++){
         ManagerLocator.getChampAnnotationManager().removeObjectManager(champsToDelete.get(i), null,
            SessionUtils.getLoggedUser(sessionScope), SessionUtils.getSystemBaseDir());
      }

      ManagerLocator.getTableAnnotationManager().createOrUpdateObjectManager(table, null, null, chps, banques,
         SessionUtils.getSelectedBanques(sessionScope).get(0), SessionUtils.getLoggedUser(sessionScope), "modification",
         SessionUtils.getSystemBaseDir(), null);

      // ordonne les champs
      if(!isCatalogueTable){
         ManagerLocator.getTableAnnotationManager().updateChampOrdersManager(ChampAnnotationDecorator.unDecore(champs));
      }

      updateDecorators(chps);

      // update de la liste
      getObjectTabController().getListe().updateObjectGridList(table);

      // update si banque courante
      if(banques.contains(SessionUtils.getSelectedBanques(sessionScope).get(0))){
         getMainWindow().updateSelectedBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (collaborateur, services...).
    */
   public void initEditableMode(){
      // init de la liste d'entites (valeur non modifiable)
      if(entites.isEmpty()){
         entites.addAll(ManagerLocator.getEntiteManager().findAnnotablesManager());
      }
      // init de la liste de catalogues (valeur non modifiable)
      //		if (catalogues.isEmpty()) {
      //			catalogues.add(emptyCatalogue);
      //			catalogues.addAll(ManagerLocator.getCatalogueManager()
      //													.findAllObjectsManager());
      //		}
      // init de la liste de types (valeur non modifiable)
      if(types.isEmpty()){
         types.addAll(ManagerLocator.getDataTypeManager().findAllObjectsManager());
      }
      this.selectedDataType = this.types.get(0);

      if(booleanValue.isEmpty()){
         booleanValue.add("");
         booleanValue.add("true");
         booleanValue.add("false");
      }

      this.selectedBooleanValue = this.booleanValue.get(0);
      //initAssociations();

      if(selectedEntite == null){
         selectedEntite = entites.get(0);
         table.setEntite(selectedEntite);
      }

      //ListModel list = new ListModelList(entites);
      //entiteBox.setModel(list);
      //entiteBox.setSelectedIndex(entites.indexOf(selectedEntite));
      getBinder().loadAttribute(entiteBox, "model");

      //ListModel list2 = new ListModelList(catalogues);
      //catalogueBox.setModel(list);
      //catalogueBox.setSelectedIndex(catalogues.indexOf(selectedCatalogue));
      //getBinder().loadAttribute(catalogueBox, "model");
   }

   /**
    * Etablit les associations de la table courante.
    */
   private void initAssociations(){
      champs.clear();
      banques.clear();
      champsToCreateOrEdit.clear();
      champsToDelete.clear();

      selectedEntite = null;
      //selectedCatalogue = null;

      if(table.getTableAnnotationId() != null){
         List<ChampAnnotationDecorator> list;
         if(table.getCatalogue() == null){
            list = ChampAnnotationDecorator.decorateListe(
               new ArrayList<>(ManagerLocator.getTableAnnotationManager().getChampAnnotationsManager(table)),
               SessionUtils.getSelectedBanques(sessionScope).get(0), false);
         }else{
            list = ChampAnnotationDecorator.decorateListe(
               ManagerLocator.getChampAnnotationManager().findByEditByCatalogueManager(table),
               SessionUtils.getSelectedBanques(sessionScope).get(0), true);
         }
         //			Collections.sort(list,
         //					new ChampAnnotationDecorator
         //									.ChampAnnotationDecoratorComparator());
         if(list.size() > 0){
            ordreMax = list.get(list.size() - 1).getChamp().getOrdre();
         }
         this.champs.addAll(list);

         selectedEntite = table.getEntite();
         //			if (table.getCatalogue() != null) {
         //				selectedCatalogue = table.getCatalogue();
         //			}

         // banques
         banques.addAll(ManagerLocator.getTableAnnotationManager().getBanquesManager(table));
      }else{
         if(!SessionUtils.getSelectedBanques(sessionScope).isEmpty()){
            banques.add(SessionUtils.getSelectedBanques(sessionScope).get(0));
         }
      }

      getBanquesAssociees().setObjects(banques);
   }

   public void onOpen$groupBanques(){
      if(groupBanques.isOpen()){
         getBanquesAssociees().updateComponent();
      }
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForAnnotations(){
      //drawActionsButtons("Annotation");
      Boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }
      setCanNew(admin);
      setCanEdit(admin);
      setCanDelete(admin);
      setCanSeeHistorique(admin);
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/
   public TableAnnotation getTable(){
      return table;
   }

   public void setChamps(final List<ChampAnnotationDecorator> chps){
      this.champs.clear();
      this.champs.addAll(chps);
   }

   public List<ChampAnnotationDecorator> getCopyChamps(){
      return copyChamps;
   }

   public List<Entite> getEntites(){
      return entites;
   }

   public List<DataType> getTypes(){
      return types;
   }

   public List<String> getBooleanValue(){
      return booleanValue;
   }

   public void setBooleanValue(final List<String> booleanValue){
      this.booleanValue = booleanValue;
   }

   public Entite getSelectedEntite(){
      return selectedEntite;
   }

   public void setSelectedEntite(final Entite selected){
      this.selectedEntite = selected;
   }

   public DataType getSelectedDataType(){
      return selectedDataType;
   }

   public void setSelectedDataType(final DataType sType){
      this.selectedDataType = sType;
   }

   //	public List<Catalogue> getCatalogues() {
   //		return catalogues;
   //	}
   //
   //	public Catalogue getSelectedCatalogue() {
   //		return selectedCatalogue;
   //	}
   //
   //	public void setSelectedCatalogue(Catalogue selected) {
   //		this.selectedCatalogue = selected;
   //	}

   public String getSelectedBooleanValue(){
      return selectedBooleanValue;
   }

   public void setSelectedBooleanValue(final String selectedBooleanValue){
      this.selectedBooleanValue = selectedBooleanValue;
   }

   public I3listBoxItemRenderer getEntiteRenderer(){
      return entiteRenderer;
   }

   public I3listBoxItemRenderer getTypeRenderer(){
      return typeRenderer;
   }

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/
   //	/**
   //	 * Indique si une image doit etre visible pour le catalogue.
   //	 * Permet de rafraichir plus vite l'affichage en cas de modification
   //	 * d'un catalogue vers une image icone = null.
   //	 * @return true si le catalogue présente une image
   //	 */
   //	public Boolean getIconeVisible() {
   //		return this.selectedCatalogue != null
   //							&& this.selectedCatalogue.getIcone() != null;
   //	}

   public String getGridAvailableHeight(){
      return String.valueOf(getMainWindow().getPanelHeight() - 212) + "px";
   }

   public String getEntiteTableNom(){
      if(this.table != null && this.table.getEntite() != null){
         return (Labels.getLabel("Entite." + this.table.getEntite().getNom()));
      }
      return null;
   }

   /*********************************************************/
   /********************** CHAMPS. **************************/
   /*********************************************************/
   public List<ChampAnnotationDecorator> getChamps(){
      return this.champs;
   }

   /**
    * Cree un nouveau champ et l'ajoute a la liste en lui
    * specifant l'ordre maximal.
    * Verifie qu'un autre champ n'est pas en cours d'edition,
    * si c'est un champ en cours modification -> revert
    * si c'est un champ en cours de creation -> remove.
    */
   public void onClick$addChamp(){

      // si un champ en cours edition
      // -> revert et mode statique
      if(this.beforeEditClone != null){
         // champ en cours edition deja enregistre en base
         if(this.beforeEditClone.getId() != null){
            this.currentChampEdited.setChampAnnotation(this.beforeEditClone);
            this.currentChampEdited.setEdition(false);
         }else{ // nouveau champ
            champs.remove(this.currentChampEdited);
         }
         beforeEditClone = null;
      }

      // le nouveau champ prend l'ordre maximum
      final ChampAnnotation newChamp = new ChampAnnotation();
      newChamp.setDataType(selectedDataType);
      ++ordreMax;
      newChamp.setOrdre(ordreMax);
      newChamp.setTableAnnotation(this.table);
      this.currentChampEdited =
         new ChampAnnotationDecorator(newChamp, SessionUtils.getSelectedBanques(sessionScope).get(0), false);
      this.currentChampEdited.setCreation(true);
      this.currentChampEdited.setEdition(true);
      this.beforeEditClone = newChamp;
      champs.add(this.currentChampEdited);

      // maj de la liste des champs
      final ListModel<ChampAnnotationDecorator> list = new ListModelList<>(champs);
      champsGrid.setModel(list);

      //getBinder().loadAttribute(champsGrid, "model");

      getBinder().loadComponent(champsGrid);

      // colore Row
      ((Row) rows.getChildren().get(champs.indexOf(currentChampEdited))).setStyle("background-color: #fddfa9");
   }

   /**
    * Passe la ligne en mode éditable en assignant true le booleen
    * edition du champAnnotationDecorator.
    * Verifie qu'un autre champ n'est pas en cours d'edition,
    * si c'est un champ en cours modification -> revert
    * si c'est un champ en cours de creation -> remove.
    * @param event
    * @throws IOException
    */
   public void onClick$editChamp(final Event event) throws IOException{
      revertCurrentEdition();
      this.currentChampEdited = (ChampAnnotationDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      this.beforeEditClone = this.currentChampEdited.getChamp().clone();
      this.currentChampEdited.cloneDefauts();

      this.currentChampEdited.setEdition(true);
      getBinder().loadAttribute(champsGrid, "model");

      // colore Row
      ((Row) rows.getChildren().get(champs.indexOf(currentChampEdited))).setStyle("background-color: #fddfa9");

   }

   /**
    * Supprime un champ de la liste.
    * @param event
    */
   public void onClick$deleteChamp(final Event event){

      revertCurrentEdition();

      this.currentChampEdited = (ChampAnnotationDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      String delMessage = "message.deletion.champAnnotation.simple";

      List<? extends Object> refChps = new ArrayList<>();
      if(this.currentChampEdited.getChamp().getId() != null){
         refChps = ManagerLocator.getChampAnnotationManager().isUsedObjectManager(currentChampEdited.getChamp());
         delMessage = "message.deletion.champAnnotation";
      }

      if(refChps.isEmpty()){
         // confirmation
         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(delMessage)}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

            champs.remove(this.currentChampEdited);

            if(champsToCreateOrEdit.contains(this.currentChampEdited)){
               champsToCreateOrEdit.remove(this.currentChampEdited);
            }

            // si le champ existait dans la BDD on l'ajoute à la
            // liste des champs à supprimer (il ne sera délété que
            // lors de la sauvegarde finale)
            if(this.currentChampEdited.getChamp().getId() != null){
               champsToDelete.add(this.currentChampEdited.getChamp());
            }

         }

      }else{ // warning modale ChampAnnotation isUsed
         final Iterator<? extends Object> refsIt = refChps.iterator();
         final StringBuilder sb = new StringBuilder();
         sb.append(Labels.getLabel("ficheAnno.champ.isUsed.start"));
         sb.append("<ul>");
         boolean hasImports = false;
         boolean hasResultats = false;
         boolean hasCriteres = false;
         boolean hasLEtiquettes = false;
         while(refsIt.hasNext()){
            final Object o = refsIt.next();
            if(o instanceof ImportColonne){
               hasImports = true;
            }else if(o instanceof Resultat){
               hasResultats = true;
            }else if(o instanceof Critere){
               hasCriteres = true;
            }else if(o instanceof ChampLigneEtiquette){
               hasLEtiquettes = true;
            }
         }
         if(hasImports){
            sb.append("<li>");
            sb.append(Labels.getLabel("ficheAnno.champ.isUsed.import"));
            sb.append("</li>");
         }
         if(hasCriteres){
            sb.append("<li>");
            sb.append(Labels.getLabel("ficheAnno.champ.isUsed.critere"));
            sb.append("</li>");
         }
         if(hasResultats){
            sb.append("<li>");
            sb.append(Labels.getLabel("ficheAnno.champ.isUsed.resultat"));
            sb.append("</li>");
         }
         if(hasLEtiquettes){
            sb.append("<li>");
            sb.append(Labels.getLabel("ficheAnno.champ.isUsed.ligneEtiquette"));
            sb.append("</li>");
         }
         sb.append("</ul>");
         sb.append(Labels.getLabel("ficheAnno.champ.isUsed.end"));
         openDeleteWindow(page, sb.toString(), false, false, false, self, false);
      }

      this.currentChampEdited = null;
      // colore Row
      getBinder().loadAttribute(champsGrid, "model");
   }

   /**
    * Passe la ligne en mode statique en validant la creation ou la
    * modification du decorator.
    * Ajoute le champ Annotation dans les champs a editer ou créer.
    * @param event
    */
   public void onClick$validateChamp(final Event event){
      final Row row = (Row) AbstractListeController2.getBindingData((ForwardEvent) event, true);
      //Validation saisie ChampCalcule
      if("calcule".equals(this.currentChampEdited.getChamp().getDataType().getType())){
         final ChampCalcule champCalcule = this.currentChampEdited.getChampCalcule();
         if(null == champCalcule || null == champCalcule.getChamp1()){
            throw new WrongValueException(row, Labels.getLabel("anno.champCalcule.empty"));
         }
      }

      // lance constraint nom champ
      ((Textbox) row.getFirstChild().getFirstChild()).getValue();
      // re-init la liste
      this.selectedDataType = this.types.get(0);
      this.selectedBooleanValue = this.booleanValue.get(0);
      this.currentChampEdited.setEdition(false);
      this.currentChampEdited.setCreation(false);
      //this.currentChampEdited.prepareDefautsBeforeValidation();
      this.currentChampEdited.setValidated(true);
      // ajout a la liste des champs a creer ou editer
      // nettoie la liste de defauts au besoin
      champsToCreateOrEdit.add(this.currentChampEdited);
      getBinder().loadAttribute(champsGrid, "model");
      this.beforeEditClone = null;
      this.currentChampEdited = null;
   }

   /**
    * Annule la modification ou la creation du champAnnotationDecorator
    * en cours.
    * @param event
    */
   public void onClick$revertChamp(final Event event){
      revertCurrentEdition();
      this.currentChampEdited = null;
      this.beforeEditClone = null;
      getBinder().loadAttribute(champsGrid, "model");
   }

   /**
    * Monte le champ dans l'ordre de la liste.
    * @param event
    */
   public void onClick$upChamp(final Event event){
      revertCurrentEdition();
      upObject((ChampAnnotationDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false));
      getBinder().loadAttribute(champsGrid, "model");
   }

   /**
    * Descend le champ dans l'ordre de la liste.
    * @param event
    */
   public void onClick$downChamp(final Event event){
      revertCurrentEdition();
      final ChampAnnotationDecorator chpDeco =
         (ChampAnnotationDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      final int tabIndex = champs.indexOf(chpDeco);
      if(tabIndex + 1 < champs.size()){
         upObject(champs.get(tabIndex + 1));
      }
      getBinder().loadAttribute(champsGrid, "model");
   }

   /**
    * Effectue l'operation de mouvements des objets au sein de la liste.
    * @param objet a monter d'un cran
    */
   private void upObject(final ChampAnnotationDecorator obj){
      final int tabIndex = champs.indexOf(obj);
      ChampAnnotationDecorator supObjectInList = null;
      if(tabIndex - 1 > -1){
         supObjectInList = champs.get(tabIndex - 1);
         champs.set(tabIndex, supObjectInList);
         champs.set(tabIndex - 1, obj);
      }
   }

   /**
    * Ouvre la modale en lui spécifiant le decorateur en cours d'edition
    * et la reference vers le composant SELF pour permettre de traiter le
    * post back event qui rafraichira la div defautDiv.
    * @param event
    */
   public void onClick$addDefaut(final Event event){
      openAnnotationDefautModal(event, this.currentChampEdited, self, false);
   }

   /**
    * Ouvre la modale en lui spécifiant le decorateur ChampAnnotation qui
    * contient la liste de valeurs par défaut et items
    * qui seront alors renseignables par l'utilisateur.
    * @param event
    * @param chpDeco ChampAnnotationDecorator
    * @param component auquel sera adressé le post back event
    * @param isStatic true si la modale doit être affichée en static
    */
   public void openAnnotationDefautModal(final Event event, final ChampAnnotationDecorator chpDeco, final Component backComp,
      final boolean isStatic){

      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("defautModal");
         win.setPage(page);
         win.setPosition("center, center");
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("annotation.champ.defaut"));
         win.setBorder("normal");
         win.setClosable(true);
         //win.setWidth("70%");

         final DefautComponent ua = (DefautComponent) page.getComponentDefinition("defautComp", false).newInstance(page,
            "fr.aphp.tumorotek.action.administration." + "annotations.DefautComponent");
         ua.setChpDeco(chpDeco);
         ua.setBackComp(backComp);
         //ua.setScrollable(scrollable);
         ua.setParent(win);
         ua.setId("defautMacroComponent");
         ua.applyProperties();
         ua.afterCompose();
         if(isStatic){
            ua.setStatique();
         }
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               ua.setVisible(true);
               setBlockModal(false);
               win.setPosition("center");
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
            win.setPosition("center");
         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   /**
    * Ouvre la modale en lui spécifiant le decorateur en cours d'edition
    * et la reference vers le composant SELF pour permettre de traiter le
    * post back event qui rafraichira la div defautDiv.
    * @param event
    */
   public void onClick$editDefaut(final Event event){
      openAnnotationDefautModal(event, this.currentChampEdited, self, false);
   }

   /**
    * Ouvre la modale pour afficher les items en mode statique.
    * @param event
    */
   public void onClick$detailsLabel(final Event event){

      final ChampAnnotationDecorator deco =
         (ChampAnnotationDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      if(deco != null && deco.getChamp().getDataType() != null && deco.getChamp().getDataType().getType().matches("thesaurus.*")){
         openAnnotationDefautModal(event, deco, self, true);
      }
   }

   /**
    * Traite le post back event venant de la modale de
    * renseignement des valeurs d'annotation defaut.
    * Rafraichit la liste de champs.
    */
   public void onUpdateDefautDiv(){
      getBinder().loadAttribute(champsGrid, "model");

      // garde la couleur de la Row
      ((Row) rows.getChildren().get(champs.indexOf(currentChampEdited))).setStyle("background-color: #fddfa9");
   }

   /**
    * Passe la ligne qui est en cours d'edition (creation ou modification)
    * en statique en appliquant revert si modification et deletion si
    * creation.
    * Revert modification si objet deja enregistre en base ou cree precedemment
    * mais validé!
    * @return index du chps qui était en cours d'edition.
    */
   private void revertCurrentEdition(){
      if(this.currentChampEdited != null){
         // champ en cours edition deja enregistre en base ou valide
         if(this.currentChampEdited.getChamp().getId() != null || this.currentChampEdited.isValidated()){
            this.currentChampEdited.setChampAnnotation(this.beforeEditClone);
            this.currentChampEdited.revertDefauts();
            this.currentChampEdited.setEdition(false);
         }else{ // nouveau champ
            champs.remove(this.currentChampEdited);
            this.selectedDataType = this.types.get(0);
         }
      }
   }

   /**
    * En mode creation, quand le type de champ change, reinitialisation
    * de(s) valeur(s) par defaut, et de la possibilite de combine le champ
    * ou pas.
    * @param event
    */
   public void onSelect$typeBoxEachChamp(final Event event){
      // AnnotationDefaut
      this.currentChampEdited.setDataType(selectedDataType);
      this.currentChampEdited.reset();
      //this.currentChampEdited.reset(true);

      // Obligatoire
      final Checkbox obligBox = ((Checkbox) ((Row) AbstractListeController2.getBindingData((ForwardEvent) event, true))
         .getChildren().get(3).getFirstChild());
      // Combine
      final Checkbox combiBox = ((Checkbox) ((Row) AbstractListeController2.getBindingData((ForwardEvent) event, true))
         .getChildren().get(4).getFirstChild());

      // defautDiv
      final Div defautDiv =
         ((Div) ((Row) AbstractListeController2.getBindingData((ForwardEvent) event, true)).getChildren().get(2));

      defautDiv.setVisible(true);
      combiBox.setVisible(true);
      combiBox.getNextSibling().setVisible(false);
      combiBox.setChecked(false);
      obligBox.setVisible(true);
      obligBox.getNextSibling().setVisible(false);
      obligBox.setChecked(false);
      // affiche le label non italic et efface checkbox
      if(this.selectedDataType.getType().equals("fichier") || 
    	this.selectedDataType.getType().equals("boolean")
         || this.selectedDataType.getType().equals("calcule")){
         combiBox.getNextSibling().setVisible(true);
         combiBox.setVisible(false);
         this.currentChampEdited.setCombine(null);
         // fichier annotation ne peut être obigatoire
         if(this.selectedDataType.getType().equals("fichier") || this.selectedDataType.getType().equals("calcule")){
            this.currentChampEdited.setObligatoire(false);
            obligBox.getNextSibling().setVisible(true);
            obligBox.setVisible(false);
            if(this.selectedDataType.getType().equals("fichier")){
               defautDiv.setVisible(false);
            }
         }
      }
   }

   /**
    * Pour chaque ChampAnnotationDecorator cree ou modifie, extrait le champ
    * et  s'assure que si il est associe a l'AnnotationDefaut vide par defaut,
    * la supprime. Verifie le champ afin que ce dernier
    * a au moins une valeur défaut non vide, sinon aucune.
    */
   private List<ChampAnnotation> prepareChamps(){
      final List<ChampAnnotation> chps = new ArrayList<>();
      ChampAnnotation chpAnno;
      for(int i = 0; i < champsToCreateOrEdit.size(); i++){
         chpAnno = champsToCreateOrEdit.get(i).getChamp();
         chps.add(chpAnno);

         final Iterator<AnnotationDefaut> itor = champsToCreateOrEdit.get(i).getDefauts().iterator();

         // cree le set annotation defaut dans l'ordre (la premiere est celle
         // qui contient l'info obligatoire ou non)
         // avec les defauts non vides
         final Set<AnnotationDefaut> defs = new LinkedHashSet<>();
         AnnotationDefaut def;
         while(itor.hasNext()){
            def = itor.next();
            if(!def.isEmpty() || def.getObligatoire()){
               defs.add(def);
            }
         }

         chpAnno.setAnnotationDefauts(defs);

         final Set<Item> its = new LinkedHashSet<>();

         if(!champsToCreateOrEdit.get(i).getItems().isEmpty()){
            its.addAll(champsToCreateOrEdit.get(i).getItems());
         }
         chpAnno.setItems(its);

         final ChampCalcule cc = champsToCreateOrEdit.get(i).getChampCalcule();
         chpAnno.setChampCalcule(cc);

      }
      return chps;
   }

   /**
    * Met à jour les valeurs d'annotation defaut et les items
    * pour le champ spécifié. Utilise HashSet et LinkedHashSet
    * pour caster les PersistentSet.
    * @param chps
    */
   private void updateDecorators(final List<ChampAnnotation> chps){
      for(int i = 0; i < champsToCreateOrEdit.size(); i++){
         // empty renvoye quand modification du champ
         if(!chps.get(i).getAnnotationDefauts().isEmpty()){
            final Set<AnnotationDefaut> defs = new LinkedHashSet<>();
            defs.addAll(chps.get(i).getAnnotationDefauts());
            champsToCreateOrEdit.get(i).setDefauts(defs);
         }
         if(!chps.get(i).getItems().isEmpty()){
            final List<Item> its = new ArrayList<>();
            its.addAll(chps.get(i).getItems());
            champsToCreateOrEdit.get(i).setItems(its);
         }
         if(null != chps.get(i).getChampCalcule()){
            final ChampCalcule cc = chps.get(i).getChampCalcule();
            champsToCreateOrEdit.get(i).setChampCalcule(cc);
         }

      }
   }

   /*************************************************************************/
   /************************** BANQUEs **************************************/
   /*************************************************************************/
   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> banks){
      this.banques.clear();
      this.banques.addAll(banks);
   }

   public List<Banque> getCopyBanques(){
      return copyBanques;
   }

   /**
    * Renvoie le controller associe au composant permettant la gestion
    * des associations one-to-many avec les banques.
    */
   public BanquesAssociees getBanquesAssociees(){
      return (BanquesAssociees) self.getFellow("banquesAssociees").getFellow("winBanquesAssociees")
         .getAttributeOrFellow("winBanquesAssociees$composer", true);
   }

   public ConstWord getNomConstraint(){
      return AnnotationsConstraints.getNomConstraint();
   }

   public ConstText getDescrConstraint(){
      return AnnotationsConstraints.getDescrConstraint();
   }

   public ConstWord getNomChampConstraint(){
      return AnnotationsConstraints.getNomChampConstraint();
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }
}
