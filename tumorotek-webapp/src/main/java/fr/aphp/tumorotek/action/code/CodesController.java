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
package fr.aphp.tumorotek.action.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.decorator.CodeItemRenderer;
import fr.aphp.tumorotek.decorator.I3listBoxItemRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.code.CodeNode;

/**
 *
 * @author mathieu
 *
 */
public class CodesController extends AbstractController
{

   //private Log log = LogFactory.getLog(CodesController.class);

   private static final long serialVersionUID = 1L;

   private Borderlayout mainBorder;

   private boolean isModal = false;

   private String fromComponentPath;

   private boolean isOrgane = false;

   private boolean isMorpho = false;

   private boolean isDiagnostic = false;

   private boolean toCims = false;

   private Listbox codifBox;

   private Hbox resHBox;

   private Textbox codeBox;

   private Textbox libelleBox;

   private Textbox dosNomBox;

   private Textbox dosDescrBox;

   private Textbox codeOrLibelleBox;

   private Checkbox exactMatchBox;

   private Label currLabel;

   private Label currLabel2;

   private Panel selPanel;

   private Rows selCodesRows;

   private Grid selGrid;

   private Rows transCodesRows;

   private Panel transPanel;

   private Grid transGrid;

   private Column transboxCheckboxColumn;

   private Column transCodesDeleteColumn;

   private Button validate;

   private Button validateDos;

   private Button cancel;

   private Menuitem newCodeButton;

   private Menuitem newDossierButton;

   private Menuitem transcodeButton;

   private Menuitem editButton;

   private Menuitem deleteButton;

   private Menuitem addToListButton;

   private Button addTranscodeToSelectedButton;

   private Button addSelectedToTranscodeButton;

   private Button addSelectedToBookmarksButton;

   private Button returnSelCodesButton;

   private boolean isCreation = true;

   private final I3listBoxItemRenderer codageRenderer = new I3listBoxItemRenderer("nom");

   private static CodesSelectedRowRenderer selCodesRenderer = new CodesSelectedRowRenderer();

   private static CodesSelectedRowRenderer transCodesRenderer = new CodesSelectedRowRenderer();
   static{
      transCodesRenderer.setUpAndDown(false);
   }

   private List<TableCodage> tableCodages = new ArrayList<>();

   private TableCodage selectedTableCodage;

   private String codeOrLibelle;

   private Boolean exactMatch = false;

   private CodeCommon currCode;

   private CodeDossier currDossier;

   private TableCodage currTable;

   private CodeNode selectedNode;

   private final List<CodeCommon> selectedCodes = new ArrayList<>();

   private List<CodeCommon> transCodes = new ArrayList<>();

   private final List<CodeCommon> selTransCodes = new ArrayList<>();

   private final List<CodeCommon> selSelectedCodes = new ArrayList<>();

   private final List<CodeCommon> res = new ArrayList<>();

   private boolean isInEdition = false;

   private boolean isBrowsing = true;

   private Label currLabelValue1;

   private Label currLabelValue2;

   private Utilisateur utilisateur;
   //private Banque selectedBanque;

   private static ConstCode codeConstraint = new ConstCode();
   {
      codeConstraint.setNullable(false);
      codeConstraint.setSize(20);
   }

   private static ConstWord libelleConstraint = new ConstWord();
   {
      libelleConstraint.setNullable(true);
      libelleConstraint.setSize(150);
   }

   private AnnotateDataBinder binder;

   public TableCodage getSelectedTableCodage(){
      return selectedTableCodage;
   }

   public void setSelectedTableCodage(final TableCodage stCodage){
      this.selectedTableCodage = stCodage;
   }

   public I3listBoxItemRenderer getCodageRenderer(){
      return codageRenderer;
   }

   public CodesSelectedRowRenderer getTransCodesRenderer(){
      return transCodesRenderer;
   }

   public CodesSelectedRowRenderer getSelCodesRenderer(){
      return selCodesRenderer;
   }

   public List<TableCodage> getTableCodages(){
      return tableCodages;
   }

   public String getCodeOrLibelle(){
      return codeOrLibelle;
   }

   public void setCodeOrLibelle(final String c){
      this.codeOrLibelle = c;
   }

   public void setExactMatch(final Boolean e){
      this.exactMatch = e;
   }

   public Boolean getExactMatch(){
      return exactMatch;
   }

   public CodeCommon getCurrCode(){
      return currCode;
   }

   /**
    * Selectionne un code.
    * Methode d'entrée vers l'édition, manipulation, ou envoie (si diagnostic)
    * d'un code.
    * @param cCode
    */
   public void setCurrCode(final CodeCommon cCode){
      if(!isDiagnostic){
         this.currCode = cCode;
         if(cCode != null){
            this.currDossier = null;
         }

         //transcodage
         this.transCodes = transcode();

         //this.selSelectedCodes.clear();

         binder.loadAttribute(transPanel, "title");
         binder.loadAttribute(transGrid, "model");
         //binder.loadAttribute(selGrid, "model");

         reloadCurrCodeOrDossier();

         // ajoute automatiquement à la selection
         if(currCode != null){
            if(!selectedCodes.contains(currCode)){
               selectedCodes.add(currCode);
               binder.loadAttribute(selPanel, "title");
               binder.loadAttribute(selGrid, "model");
            }else{ // fait le lien en cas modification sera reportée.
               selectedCodes.set(selectedCodes.indexOf(currCode), currCode);
            }
         }
      }else if(cCode != null){ // renvoie directement le code cliqué.
         if(Path.getComponent(fromComponentPath) != null){
            final String eventName = "onGetCodeFromAssist";

            Events.postEvent(new Event(eventName, Path.getComponent(fromComponentPath), cCode));
         }

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));
      }
   }

   public CodeDossier getCurrDossier(){
      return currDossier;
   }

   public void setCurrDossier(final CodeDossier cDos){
      if(cDos != null){
         this.currCode = null;
      }
      this.currDossier = cDos;
      reloadCurrCodeOrDossier();
   }

   public void setCurrTable(final TableCodage cTable){
      this.currTable = cTable;
   }

   public Constraint getCodeConstraint(){
      return codeConstraint;
   }

   public ConstWord getLibelleConstraint(){
      return libelleConstraint;
   }

   //	public Banque getSelectedBanque() {
   //		return selectedBanque;
   //	}

   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public boolean isInEdition(){
      return isInEdition;
   }

   public List<CodeCommon> getSelectedCodes(){
      return selectedCodes;
   }

   public List<CodeCommon> getTransCodes(){
      return transCodes;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      binder = new AnnotateDataBinder(comp);

      tableCodages = ManagerLocator.getTableCodageManager().findAllObjectsManager();

      super.doAfterCompose(comp);

      utilisateur = SessionUtils.getLoggedUser(sessionScope);

      if(self.getParent() != null && self.getParent().getParent() != null){
         if(self.getParent().getParent() instanceof Window){ // is modale
            selectedTableCodage = ManagerLocator.getTableCodageManager().findByNomManager("ADICAP").get(0);
            isModal = true;
            mainBorder.setHeight((getMainWindow().getWindowAvailableHeight() - 37) + "px");
            mainBorder.setWidth((getMainWindow().getWindowAvailableWidth() - 20) + "px");

            //selectedBanque = (Banque) getMainWindow().getSelectedBanque();
         }else{
            selectedTableCodage = ManagerLocator.getTableCodageManager().findByNomManager("UTILISATEUR").get(0);
            isModal = false;
            mainBorder.setHeight((getMainWindow().getWindowAvailableHeight() - 150) + "px");
            mainBorder.setWidth((getMainWindow().getWindowAvailableWidth() - 150) + "px");
            returnSelCodesButton.setVisible(false);

            disableButtons();
            browseClassification();
         }
      }

      selCodesRows.setAttribute("checkedCodes", selSelectedCodes);

      selCodesRows.addEventListener("onClickDeleteCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            selectedCodes.remove(event.getData());
            selSelectedCodes.remove(event.getData());
            if(selSelectedCodes.isEmpty()){
               // active ou non le bouton d'ajout selection
               addSelectedToTranscodeButton.setDisabled(true);
               addSelectedToBookmarksButton.setDisabled(true);
               returnSelCodesButton.setDisabled(true);
            }
         }
      });

      selCodesRows.addEventListener("onClickUpCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            upCode(event.getData());
         }
      });

      selCodesRows.addEventListener("onClickDownCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            downCode(event.getData());
         }
      });

      selCodesRows.addEventListener("onCheckObject", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onCheckCode((ForwardEvent) event, selSelectedCodes);
            // active ou non le bouton d'ajout selection
            addSelectedToTranscodeButton.setDisabled(!isInEdition || currDossier != null || selSelectedCodes.size() == 0);

            addSelectedToBookmarksButton.setDisabled(isInEdition || selSelectedCodes.size() == 0
               || (!sessionScope.containsKey("Admin") || !((Boolean) sessionScope.get("Admin"))));

            returnSelCodesButton.setDisabled(!isModal || isInEdition || selSelectedCodes.size() == 0);
         }
      });

      transCodesRows.setAttribute("checkedCodes", selTransCodes);

      transCodesRows.addEventListener("onClickDeleteCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            transCodes.remove(event.getData());
            selTransCodes.remove(event.getData());
            if(selTransCodes.isEmpty()){
               // active ou non le bouton d'ajout selection
               addTranscodeToSelectedButton.setDisabled(true);
            }
         }
      });

      transCodesRows.addEventListener("onCheckObject", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onCheckCode((ForwardEvent) event, selTransCodes);
            // active ou non le bouton d'ajout selection
            addTranscodeToSelectedButton.setDisabled(selTransCodes.size() == 0);
         }
      });

      binder.loadComponent(codifBox);

      switchToStaticMode();
   }

   /**
    * Init methode. Sets les booleens qui dicteront le comportement
    * de l'interface et qui seront assignés dans les codes assignés.
    * @param path
    * @param isOrgane
    * @param isMorpho
    * @param isDiagnostic
    * @param toCIms true pour afficher catégorie cims
    */
   public void init(final String path, final boolean isOrg, final boolean isMor, final boolean isDiag, final boolean toC){
      this.fromComponentPath = path;
      this.isOrgane = isOrg;
      this.isMorpho = isMor;
      this.isDiagnostic = isDiag;
      this.toCims = toC;

      // affiche codif par defaut
      if(isOrgane){
         setSelectedTableCodage(ManagerLocator.getTableCodageManager().findByNomManager("ADICAP").get(0));

         browseClassification();
      }else if(toCims){
         setSelectedTableCodage(ManagerLocator.getTableCodageManager().findByNomManager("CIM_MASTER").get(0));
         browseClassification();
      }else{ // affiche favoris
         setSelectedTableCodage(ManagerLocator.getTableCodageManager().findByNomManager("FAVORIS").get(0));
         browseClassification();
      }
      disableTables();
   }

   /**
    * Rend inutilisables les classifications
    * non utilisables pour assigner un code
    * organe. CIMO moprho pour l'instant.
    */
   private void disableTables(){
      codifBox.getItemAtIndex(tableCodages.indexOf(ManagerLocator.getTableCodageManager().findByNomManager("CIMO_MORPHO").get(0)))
         .setDisabled(isOrgane || isDiagnostic || toCims);
      codifBox.getItemAtIndex(tableCodages.indexOf(ManagerLocator.getTableCodageManager().findByNomManager("ADICAP").get(0)))
         .setDisabled(isDiagnostic || toCims);
      codifBox.getItemAtIndex(tableCodages.indexOf(ManagerLocator.getTableCodageManager().findByNomManager("CIM_MASTER").get(0)))
         .setDisabled(isMorpho);
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return binder;
   }

   /**
    * Formate le label avec le nombre de codes dans la liste
    * pour affichage.
    * @return Label + (nb)
    */
   public String getSelectedCodesLabel(){
      return Labels.getLabel("code.selections") + "(" + selectedCodes.size() + ")";
   }

   /**
    * Formate le label avec le nombre de transcodes dans la liste
    * pour affichage.
    * @return Label + (nb)
    */
   public String getTransCodesLabel(){
      return ObjectTypesFormatters.getLabel("code.recherche.transcodage", new String[] {String.valueOf(transCodes.size())});
   }

   /**
    * Renvoie la valeur du premier champ cad le code du code courant
    * ou le nom du dossier.
    * @return value1
    */
   public String getCurrValue1(){
      if(currCode != null){
         return currCode.getCode();
      }else if(currDossier != null){
         return currDossier.getNom();
      }
      return null;
   }

   /**
    * Renvoie la valeur du deuxieme champ cad le libelle du code courant
    * ou la descr du dossier.
    * @return value2
    */
   public String getCurrValue2(){
      if(currCode != null){
         return currCode.getLibelle();
      }else if(currDossier != null){
         return currDossier.getDescription();
      }
      return null;
   }

   /**
    * Dessine le panel affichant les resultats d'une recherche ou
    * d'un transcodage. Prépare les paramètres qui seront passés
    * au panel.
    * @param res Liste de codes résultat
    * @param Component dans laquelle le Panel sera dessiné.
    * @param label à afficher en titre.
    */
   private void drawCodeResultsPanel(final List<CodeCommon> codes, final Component parentDiv, final String label,
      final boolean transcode){
      // arguments
      final Map<Object, Object> args = new HashMap<>();
      args.put("title", ObjectTypesFormatters.getLabel(label, new String[] {String.valueOf(codes.size())}));
      if(!codes.isEmpty()){
         final SimpleListModel<CodeCommon> model = new SimpleListModel<>(codes);
         args.put("codes", model);
         final CodeItemRenderer renderer = new CodeItemRenderer();
         args.put("renderer", renderer);
      }
      if(!transcode){
         args.put("boxId", "resBox");
      }else{
         args.put("boxId", "transResBox");
      }

      Executions.createComponents("/zuls/code/CodeResultsPanel.zul", parentDiv, args);
   }

   /**
    * Recherche tous les codes associés par transcodage.
    * Affiche les codes dans un CodeResultPanel qui sera placé
    * dans le deuxième div.
    * Si modale alors transcode uniquement pour les tables codages associées à
    * la collection
    * Sinon administration transcode donc pour toutes les codifications et les
    * banques (codes Utilisateurs) pour lesquelles l'utilisateur est admin.
    * @return liste de codes résultat du transcodage.
    */
   private List<CodeCommon> transcode(){
      this.selTransCodes.clear();

      if(isModal){ // ne transcode que pour les codifs associées à la banque
         return ManagerLocator.getTableCodageManager().transcodeManager(currCode, getTablesForBanques(),
            SessionUtils.getSelectedBanques(applicationScope));
      } // administration -> transcode sur toutes les tables
      return ManagerLocator.getTableCodageManager().transcodeManager(currCode,
         ManagerLocator.getTableCodageManager().findAllObjectsManager(), ManagerLocator.getBanqueManager()
            .findByUtilisateurIsAdminManager(SessionUtils.getLoggedUser(sessionScope), SessionUtils.getPlateforme(sessionScope)));
   }

   /**
    * Affiche le browser de la codification quand la liste
    * est changée.
    */
   public void onSelect$codifBox(){
      isBrowsing = true;
      cleanUp();

      selectedTableCodage = (TableCodage) codifBox.getSelectedItem().getValue();

      if(!"CIMO_MORPHO".equals(selectedTableCodage.getNom())){
         codeOrLibelle = null;
         // affiche le browser de la classification
         browseClassification();
      }else{
         codeOrLibelle = "%";
         CodeUtils.findCodes(codeOrLibelle, isOrgane, isMorpho, res, selectedTableCodage, exactMatch,
            SessionUtils.getSelectedBanques(sessionScope));
         drawCodeResultsPanel(res, resHBox, "code.recherche.resultats", false);
      }
   }

   /**
    * Lance la recherche sur la codification choisie.
    * Si codeOrLibelle est vide, affiche le browser sur toute la codification.
    */
   public void onClick$find(){

      codeOrLibelle = codeOrLibelleBox.getValue();
      exactMatch = exactMatchBox.isChecked();

      isBrowsing = false;
      cleanUp();

      if(codeOrLibelle != null && !codeOrLibelle.equals("")){
         CodeUtils.findCodes(codeOrLibelle, isOrgane, isMorpho, res, selectedTableCodage, exactMatch,
            SessionUtils.getSelectedBanques(sessionScope));
         drawCodeResultsPanel(res, resHBox, "code.recherche.resultats", false);
      }
      disableTables();
   }

   /**
    * Nettoie tous les panels contenant des codes.
    */
   private void cleanUp(){
      // nettoie les Divs
      Components.removeAllChildren(resHBox);
      resHBox.setWidth("100%");

      setCurrTable(selectedTableCodage.clone());
      setCurrCode(null);
      setCurrDossier(null);

      // selSelectedCodes.clear();
      selTransCodes.clear();

      // disableButtons();
   }

   /**
    * Dessine le browser et lui assigne la codification, et les
    * filtres éventuels portant sur l'utilisation (topo, diagMaladie, diag)
    * et sur un noeud de départ.
    * @param isorgane
    * @param isRootCode
    */
   private void browseClassification(){

      Executions.createComponents("/zuls/code/CodeBrowserComponent.zul", resHBox, null);
      //
      //		((CodeBrowserComponent)
      //				resHBox.getFellow("winCodeTree")
      //					.getAttributeOrFellow("winCodeTree$composer", true))
      //						.setIsInModale(isModal);

      ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .setCodification(selectedTableCodage);

      ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .setIsOrgane(isOrgane);

      ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .setIsMorpho(isMorpho);

      ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .populateCodeTreeModel();
   }

   /**
    * Recoit le code venant de la selection à partir d'un composant resultats.
    * @param event
    */
   public void onSelectCode(final Event event){
      final Listbox target = (Listbox) ((ForwardEvent) event).getOrigin().getTarget();
      currTable = selectedTableCodage.clone();
      setCurrCode((CodeCommon) target.getSelectedItem().getValue());
   }

   /**
    * Controller d'activation/desactivation de tous les boutons 'outils'.
    */
   private void disableButtons(){
      transcodeButton.setDisabled(isInEdition || currCode == null);
      addToListButton.setDisabled(isInEdition || currCode == null);

      addTranscodeToSelectedButton.setDisabled(isInEdition || currCode == null || selTransCodes.size() == 0);
      returnSelCodesButton.setDisabled(!isModal || isInEdition || selSelectedCodes.size() == 0);

      if(("UTILISATEUR".equals(selectedTableCodage.getNom()) || "FAVORIS".equals(selectedTableCodage.getNom()))
         && sessionScope.containsKey("Admin") && (Boolean) sessionScope.get("Admin")){
         addSelectedToTranscodeButton.setDisabled(!isInEdition || currDossier != null || selSelectedCodes.size() == 0);
         deleteButton.setDisabled(isInEdition || (currCode == null && currDossier == null));
         if("UTILISATEUR".equals(selectedTableCodage.getNom())){
            newDossierButton.setDisabled(isInEdition || !isBrowsing || currCode != null);
            newCodeButton.setDisabled(isInEdition || !isBrowsing);
            editButton.setDisabled(isInEdition || (currCode == null && currDossier == null));
         }else{
            newDossierButton.setDisabled(isInEdition || !isBrowsing || currCode != null);
            newCodeButton.setDisabled(true);
            editButton.setDisabled(isInEdition || currDossier == null);
            addSelectedToBookmarksButton.setDisabled(isInEdition || selSelectedCodes.size() == 0);

         }
      }else{
         // pour les codifications en general
         deleteButton.setDisabled(true);
         editButton.setDisabled(true);
         newDossierButton.setDisabled(true);
         newCodeButton.setDisabled(true);
         addSelectedToBookmarksButton.setDisabled(true);
      }
      addSelectedToTranscodeButton.setVisible("UTILISATEUR".equals(selectedTableCodage.getNom()));
      addSelectedToBookmarksButton.setVisible("FAVORIS".equals(selectedTableCodage.getNom()));
   }

   public void onClick$transcodeButton(){
      if(currCode != null && currTable != null){
         // nettoie la Hbox du deuxieme panel au besoin
         if(resHBox.getFirstChild().getNextSibling() != null){
            resHBox.getFirstChild().getNextSibling().detach();
         }
         resHBox.setWidths("50%,50%");
         drawCodeResultsPanel(transcode(), resHBox, "code.recherche.transcodage", true);
      }
   }

   /**
    * Reload les composants detaillant le code ou le dossier courant.
    */
   public void reloadCurrCodeOrDossier(){
      switchToStaticMode();
      binder.loadComponent(currLabelValue1);
      binder.loadComponent(currLabelValue2);
      disableButtons();
   }

   /**
    * Affiche le formulaire de creation d'un nouveau code utilisateur.
    */
   public void onClick$newCodeButton(){
      currDossier = null;
      currCode = new CodeUtilisateur();
      switchToCodeEditionMode();
      isCreation = true;
   }

   /**
    * Affiche le formulaire de creation d'un nouveau dossier.
    */
   public void onClick$newDossierButton(){

      currCode = null;
      currDossier = new CodeDossier();
      currDossier.setCodeSelect("FAVORIS".equals(selectedTableCodage.getNom()));

      switchToDossierEditionMode();

      isCreation = true;
   }

   /**
    * Enregistre un nouveau code ou dossier utilisateur.
    */
   public void onClick$validate(){

      // prepare currCode
      if(currCode != null && currCode.getLibelle().equals("")){
         currCode.setLibelle(null);
      }

      boolean isParentItemUpdate = false;

      try{
         if(isCreation){
            // si code ou dossier parent
            this.selectedNode = getSelectedParentNodeFromBrowser();
            CodeUtilisateur codeParent = null;
            CodeDossier dosParent = null;
            if(selectedNode != null){
               isParentItemUpdate = true;
               if(selectedNode.getDossier() != null){
                  dosParent = selectedNode.getDossier();
               }else if(selectedNode.getCode() != null){
                  codeParent = (CodeUtilisateur) selectedNode.getCode();
               }
               ((CodeUtilisateur) currCode).setCodeParent(codeParent);
               ((CodeUtilisateur) currCode).setCodeDossier(dosParent);
            }

            ManagerLocator.getCodeUtilisateurManager().createOrUpdateManager((CodeUtilisateur) currCode, null,
               SessionUtils.getSelectedBanques(sessionScope).get(0), utilisateur, null, new LinkedHashSet<>(transCodes),
               "creation");

            // cree le nouveau node a partir de l'ancien
            if(selectedNode != null){
               selectedNode.readChildren();
            }

         }else{ //modification
            ManagerLocator.getCodeUtilisateurManager().createOrUpdateManager((CodeUtilisateur) currCode, null, null, null, null,
               new LinkedHashSet<>(transCodes), "modification");

            if(isBrowsing){
               selectedNode.setCode(currCode);
            }
         }
         if(isBrowsing){
            // update browser
            ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
               .reloadTree(selectedNode, isParentItemUpdate);

            isInEdition = false;
            // remet la selection depuis le browser dans le panel
            ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
               .onSelect$mainTreeContext(null);

         }else{
            // recupere la liste de resultats
            final SimpleListModel<CodeCommon> model = new SimpleListModel<>(res);
            ((Listbox) resHBox.getFirstChild().getFellow("resBox")).setModel(model);
            ((Listbox) resHBox.getFirstChild().getFellow("resBox")).invalidate();
            setCurrCode(currCode);
         }
      }catch(final RuntimeException e){
         // catch doublon ou autre exception du systeme
         // pour remettre champs a jour avant d'afficher
         // le message box.
         setCurrCode(null);
         binder.loadAttribute(currLabelValue1, "value");
         binder.loadAttribute(currLabelValue2, "value");
         switchToStaticMode();

         if(isBrowsing){
            // efface la selection
            ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
               .getMainTreeContext().setSelectedItem(null);
         }

         throw e;
      }
   }

   /**
    * Valide l'enregistrement d'un dossier.
    */
   public void onClick$validateDos(){

      // prepare currDos

      if(currDossier != null && currDossier.getDescription().equals("")){
         currDossier.setDescription(null);
      }

      boolean isParentItemUpdate = false;

      try{
         if(isCreation){
            // si dossier parent
            selectedNode = getSelectedParentNodeFromBrowser();
            CodeDossier dosParent = null;
            if(selectedNode != null && selectedNode.getDossier() != null){
               dosParent = selectedNode.getDossier();
               currDossier.setDossierParent(dosParent);
               isParentItemUpdate = true;
            }

            ManagerLocator.getCodeDossierManager().createOrUpdateManager(currDossier, null,
               SessionUtils.getSelectedBanques(sessionScope).get(0), utilisateur, "creation");

            // cree le nouveau node a partir de l'ancien
            if(selectedNode != null){
               selectedNode.readChildren();
            }

         }else{ //modification
            ManagerLocator.getCodeDossierManager().createOrUpdateManager(currDossier, null, null, null, "modification");
            if(isBrowsing){
               selectedNode.setDossier(currDossier);
            }
         }

         if(isBrowsing){
            // update browser
            ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
               .reloadTree(selectedNode, isParentItemUpdate);
            isInEdition = false;
            // remet la selection depuis le browser dans le panel
            ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
               .onSelect$mainTreeContext(null);
         }
      }catch(final RuntimeException e){
         // catch doublon ou autre exception du systeme
         // pour remettre champs a jour avant d'afficher
         // le message box.
         setCurrDossier(null);
         binder.loadAttribute(currLabelValue1, "value");
         binder.loadAttribute(currLabelValue2, "value");
         switchToStaticMode();

         // efface la selection
         ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
            .getMainTreeContext().setSelectedItem(null);

         throw e;
      }
   }

   public void onClick$cancel(){
      isInEdition = false;
      // re-initialise transcodage
      if(currCode != null && !currCode.equals(new CodeUtilisateur())){
         this.transCodes = transcode();
      }

      switchToStaticMode();
      disableButtons();
   }

   /**
    * Supprime un code ou un dossier (ainsi que tout leur
    * contenu ou codes hérités) de la codification utilisateur.
    */
   public void onClick$deleteButton(){
      CodeCommon codeToDelete = null;
      CodeDossier dossierToDelete = null;
      // recupere l'objet a supprimer
      if(isBrowsing){
         selectedNode = getSelectedNodeFromBrowser();
         if(selectedNode != null){
            codeToDelete = selectedNode.getCode();
            dossierToDelete = selectedNode.getDossier();
         }
      }else{
         codeToDelete = currCode;
         dossierToDelete = currDossier;
      }
      if(codeToDelete != null || dossierToDelete != null){
         if("UTILISATEUR".equals(selectedTableCodage.getNom())){
            String labelMessage = "message.deletion.code";
            if(dossierToDelete != null){
               labelMessage = "message.deletion.dossier";
            }

            if(Messagebox.show(
               ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(labelMessage)}),
               Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               if(codeToDelete != null){
                  ManagerLocator.getCodeUtilisateurManager().removeObjectManager((CodeUtilisateur) codeToDelete);
                  selectedCodes.remove(codeToDelete);
               }else if(dossierToDelete != null){
                  ManagerLocator.getCodeDossierManager().removeObjectManager(dossierToDelete);
               }
               if(isBrowsing){
                  // update browser
                  selectedNode = null;
                  ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
                     .reloadTree(selectedNode, false);
               }else{
                  onClick$find();
               }
            }
         }else{ // pas de message pour favoris
            if(codeToDelete != null){
               // code select embarqué?
               if(codeToDelete.getCodeSelect() != null){
                  ManagerLocator.getCodeSelectManager().removeObjectManager(codeToDelete.getCodeSelect());
               }
               selectedCodes.remove(codeToDelete);
            }else if(dossierToDelete != null){
               ManagerLocator.getCodeDossierManager().removeObjectManager(dossierToDelete);
            }
            if(isBrowsing){
               // update browser
               selectedNode = null;
               ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
                  .reloadTree(selectedNode, false);
            }else{
               onClick$find();
            }
         }
      }
      setCurrCode(null);
      setCurrDossier(null);
   }

   /**
    * Modifie un code ou un dossier.
    */
   public void onClick$editButton(){
      CodeCommon codeToEdit = null;
      CodeDossier dossierToEdit = null;
      // recupere l'objet a editer
      if(isBrowsing){
         selectedNode = getSelectedNodeFromBrowser();
         if(selectedNode != null){
            codeToEdit = selectedNode.getCode();
            dossierToEdit = selectedNode.getDossier();
         }
      }else{
         codeToEdit = currCode;
         dossierToEdit = currDossier;
      }
      if(codeToEdit != null){
         //currCode = codeToEdit.clone();
         currCode = codeToEdit;
         switchToCodeEditionMode();
      }else if(dossierToEdit != null){
         currDossier = dossierToEdit.clone();
         switchToDossierEditionMode();
      }
      isCreation = false;
   }

   /**
    * Ajoute un code à la liste de code selectionnés.
    */
   public void onClick$addToListButton(){
      if(currCode != null && !selectedCodes.contains(currCode)){
         selectedCodes.add(currCode);
      }
   }

   /**
    * Ajoute les transcodes selectionnés à la liste de code selectionnés
    * si cette dernière ne contient pas les premiers.
    */
   public void onClick$addTranscodeToSelectedButton(){
      for(int i = 0; i < selTransCodes.size(); i++){
         if(!selectedCodes.contains(selTransCodes.get(i))){
            selectedCodes.add(selTransCodes.get(i));
         }
      }
   }

   /**
    * Ajoute les codes selectionnés à la liste de transcodes
    * si cette dernière ne contient pas les premiers.
    */
   public void onClick$addSelectedToTranscodeButton(){
      for(int i = 0; i < selSelectedCodes.size(); i++){
         if(!transCodes.contains(selSelectedCodes.get(i))){
            transCodes.add(selSelectedCodes.get(i));
         }
      }
   }

   /**
    * Ajoute les codes selectionnés aux code favoris.
    * Peut Ajouter dans un dossier selectionné ou à la racine par
    * défaut si les codes ne sont deja pas ajoutés comme favoris.
    */
   public void onClick$addSelectedToBookmarksButton(){

      boolean isParentItemUpdate = false;

      // si dossier parent
      this.selectedNode = getSelectedParentNodeFromBrowser();
      CodeDossier dosParent = null;
      if(selectedNode != null){
         if(selectedNode.getDossier() != null){
            isParentItemUpdate = true;
            dosParent = selectedNode.getDossier();
         }
      }
      // liste des codes deja enregistrés
      final List<CodeCommon> bookmarked = ManagerLocator.getCodeSelectManager()
         .findCodesFromSelectByUtilisateurAndBanqueManager(utilisateur, SessionUtils.getSelectedBanques(sessionScope).get(0));

      final Iterator<CodeCommon> it = selSelectedCodes.iterator();
      CodeCommon next;
      CodeSelect sel;
      final List<CodeCommon> alreadyBookmarked = new ArrayList<>();
      while(it.hasNext()){
         next = it.next();
         if(!bookmarked.contains(next)){
            sel = new CodeSelect();
            sel.setCodeId(next.getCodeId());
            sel.setTableCodage(ManagerLocator.getCommonUtilsManager().getTableCodageFromCodeCommonManager(next));

            ManagerLocator.getCodeSelectManager().createOrUpdateManager(sel, dosParent,
               SessionUtils.getSelectedBanques(sessionScope).get(0), utilisateur, "creation");
         }else{
            alreadyBookmarked.add(next);
         }
      }

      // cree le nouveau node a partir de l'ancien
      if(selectedNode != null){
         selectedNode.readChildren();
      }

      // update browser
      ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .reloadTree(selectedNode, isParentItemUpdate);

      // affiche un warning si des codes n'ont pas été enregistrés
      if(!alreadyBookmarked.isEmpty()){
         final StringBuilder listAlreadyBld = new StringBuilder();
         for(int i = 0; i < alreadyBookmarked.size(); i++){
            listAlreadyBld.append("\n");
            listAlreadyBld.append("- ");
            listAlreadyBld.append(alreadyBookmarked.get(i).getCode());
            listAlreadyBld.append(" ");
            listAlreadyBld.append(alreadyBookmarked.get(i).getLibelle());
         }
         Messagebox.show(
            ObjectTypesFormatters.getLabel("message.bookmark.alreadyRecorded", new String[] {listAlreadyBld.toString()}),
            Labels.getLabel("message.bookmark.alreadyWarning"), Messagebox.OK, Messagebox.INFORMATION);
      }
   }

   /**
    * Renvoie la liste de
    * code common selectionnés. Ferme la modale et renvoie la liste
    * au controller demandant l'assistant.
    *
    * @since 2.0.10
    */
   public void onClick$returnSelCodesButton(){

      //		Iterator<CodeCommon> it = selSelectedCodes.iterator();
      //		CodeCommon selCode;
      //		CodeAssigne codeAs;
      //		List<CodeAssigne> codes = new ArrayList<CodeAssigne>();
      //		while (it.hasNext()) {
      //			selCode = it.next();
      //			codeAs = new CodeAssigne();
      //			codeAs.setCodeRefId(selCode.getCodeId());
      //			codeAs.setCode(selCode.getCode());
      //			codeAs.setLibelle(selCode.getLibelle());
      //			codeAs.setTableCodage(ManagerLocator.getCommonUtilsManager()
      //							.getTableCodageFromCodeCommonManager(selCode));
      //			codeAs.setIsMorpho(isMorpho);
      //			codeAs.setIsOrgane(isOrgane);
      //			codes.add(codeAs);
      //		}

      if(Path.getComponent(fromComponentPath) != null){
         // on envoie un event à cette page avec
         // la liste de codes assignes
         final String eventName = "onGetCodesFromAssist";

         Events.postEvent(new Event(eventName, Path.getComponent(fromComponentPath), selSelectedCodes));
      }

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));

   }

   /**
    * Efface le formulaire pour l'affichage statique.
    * @param isStatic
    */
   private void switchToStaticMode(){

      this.isInEdition = false;
      currLabelValue1.setVisible(true);
      codeBox.setVisible(false);
      currLabelValue2.setVisible(true);
      libelleBox.setVisible(false);
      validate.setVisible(false);
      dosNomBox.setVisible(false);
      dosDescrBox.setVisible(false);
      validateDos.setVisible(false);
      cancel.setVisible(false);

      transboxCheckboxColumn.setVisible(true);
      transCodesDeleteColumn.setVisible(false);
   }

   /**
    * Affiche le formulaire de creation/modification d'un code.
    */
   private void switchToCodeEditionMode(){

      switchLabelsToCodeOrDossier(true);

      // affiche formulaire
      currLabelValue1.setVisible(false);
      codeBox.setVisible(true);
      currLabelValue2.setVisible(false);
      libelleBox.setVisible(true);
      dosNomBox.setVisible(false);
      dosDescrBox.setVisible(false);
      validate.setVisible(true);
      validateDos.setVisible(false);
      cancel.setVisible(true);

      transboxCheckboxColumn.setVisible(false);
      transCodesDeleteColumn.setVisible(true);

      isInEdition = true;

      disableButtons();
   }

   /**
    * Affiche le formulaire de creation/modification d'un dossier.
    */
   private void switchToDossierEditionMode(){

      switchLabelsToCodeOrDossier(false);

      // affiche formulaire
      currLabelValue1.setVisible(false);
      codeBox.setVisible(false);
      currLabelValue2.setVisible(false);
      libelleBox.setVisible(false);
      dosNomBox.setVisible(true);
      dosDescrBox.setVisible(true);
      validate.setVisible(false);
      validateDos.setVisible(true);
      cancel.setVisible(true);

      isInEdition = true;

      disableButtons();
   }

   /**
    * Affiche ou non les labels de champs de formulaire 'dossier' ou 'code'.
    * @param toCode true si affichage 'code'
    */
   public void switchLabelsToCodeOrDossier(final boolean toCode){
      if(toCode){
         currLabel.setValue(Labels.getLabel("code.code"));
         currLabel2.setValue(Labels.getLabel("code.libelle"));
      }else{
         currLabel.setValue(Labels.getLabel("code.dossier.nom"));
         currLabel2.setValue(Labels.getLabel("code.dossier.descr"));
      }
   }

   /**
    * Recupere le node selectionné dans le browser.
    * @return CodeNode selected.
    */
   private CodeNode getSelectedNodeFromBrowser(){
      return ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .getTreeSelectedValue();
   }

   /**
    * Recupere le node selectionné dans le browser comme
    * parent du nouveau noeud.
    * @return CodeNode parent selected.
    */
   private CodeNode getSelectedParentNodeFromBrowser(){

      // clean up et refresh codification
      // si drawResult a remplacé winCodeTree
      if(!resHBox.hasFellow("winCodeTree")){
         cleanUp();
         browseClassification();
      }

      return ((CodeBrowserComponent) resHBox.getFellow("winCodeTree").getAttributeOrFellow("winCodeTree$composer", true))
         .getTreeSelectedValueAsParent();
   }

   /**
    * Monte le code selectionné dans l'ordre de la liste.
    * @param code
    */
   private void upCode(final Object code){
      final int codeIndex = selectedCodes.indexOf(code);
      CodeCommon supObjectInList = null;
      if(codeIndex - 1 > -1){
         supObjectInList = selectedCodes.get(codeIndex - 1);
         selectedCodes.set(codeIndex, supObjectInList);
         selectedCodes.set(codeIndex - 1, (CodeCommon) code);
      }
   }

   /**
    * Descend le code selectionné dans la liste.
    * Equivaut à remonter le code suivant le code passé
    * en paramètre.
    * @param code
    */
   private void downCode(final Object code){
      final int codeIndex = selectedCodes.indexOf(code);
      if(codeIndex + 1 < selectedCodes.size()){
         upCode(selectedCodes.get(codeIndex + 1));
      }
   }

   /**
    * Gere le clique sur la checkbox au sein d'une liste
    * de transcodes ou de codes selectionnés.
    * Ajoute ou supprime le code de la liste si la box est
    * respectivement cochée ou décochée.
    * @param event
    * @param liste de CodeCommon manipulée par l'event (ie selTranscodes ou
    * selSelectedCodes)
    */
   private void onCheckCode(final ForwardEvent event, final List<CodeCommon> selected){
      // on récupère la checkbox associé à l'event
      final Checkbox box = (Checkbox) event.getOrigin().getTarget();
      final CodeCommon code = (CodeCommon) event.getData();

      if(box.isChecked()){
         selected.add(code);
      }else{
         selected.remove(code);
      }
   }

   public void clearData(){
      currCode = null;
      currDossier = null;
      switchToStaticMode();
      disableButtons();
      onSelect$codifBox();
   }

   /**
    * Méthode appelée lorsque l'utilisateur utiliser la touche ENTREE sur
    * l'un des éléments du formulaire de recherche : redirection vers la
    * méthode de recherche onClick$find().
    */
   public void onPressEnterKey(){
      onClick$find();
   }
}
