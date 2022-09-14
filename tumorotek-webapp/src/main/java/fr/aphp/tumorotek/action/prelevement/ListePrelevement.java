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
package fr.aphp.tumorotek.action.prelevement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.comparator.PrelevementsNbEchantillonsComparator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.patient.FichePatientStatic;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class ListePrelevement extends AbstractListeController2
{

   private final Log log = LogFactory.getLog(ListePrelevement.class);

   private static final long serialVersionUID = -4444512949273177133L;

   private List<Prelevement> listObjects = new ArrayList<>();

   private List<Prelevement> selectedObjects = new ArrayList<>();

   // Critères de recherche.
   protected Radio codePrlvt;

   protected Radio patientPrlvt;

   protected Textbox codeBoxPrlvt;

   protected Textbox nomPatientPrlvt;

   protected Column nbEchantillonsColumn;

   protected Button findDossierExterne;

   // Variables formulaire pour les critères.
   private String searchCode;

   private String searchNomPatient;

   // Variable de droits.
   private boolean canAccessPatient;

   protected PrelevementRowRenderer listObjectsRenderer = new PrelevementRowRenderer(true, false);

   protected PrelevementsNbEchantillonsComparator comparatorAsc = new PrelevementsNbEchantillonsComparator(true);

   protected PrelevementsNbEchantillonsComparator comparatorDesc = new PrelevementsNbEchantillonsComparator(false);

   public String getSearchCode(){
      return searchCode;
   }

   public void setSearchCode(final String code){
      this.searchCode = code;
   }

   public String getSearchNomPatient(){
      return searchNomPatient;
   }

   public void setSearchNomPatient(final String searchN){
      this.searchNomPatient = searchN;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // recoit le renderer en argument
      if(arg != null && arg.containsKey("renderer")){
         setListObjectsRenderer((TKSelectObjectRenderer<? extends TKdataObject>) arg.get("renderer"));
      }

      // @since gatsbi
      try{
         drawColumnsForVisibleChampEntites();
      }catch(final Exception e){
         // une erreur inattendue levée dans la récupération
         // ou le rendu d'une propriété prel
         // va arrêter le rendu du reste du tableau
         throw new RuntimeException(e);
      }

      nbEchantillonsColumn.setSortAscending(comparatorAsc);
      nbEchantillonsColumn.setSortDescending(comparatorDesc);

      setOnGetEventName("onGetPrelevementsFromSelection");

      listObjectsRenderer.setEmetteurs(SessionUtils.getEmetteursInterfacages(sessionScope));
   }

   /**
    * Cette méthode de dessin dynamique des colonnes est surchargée par Gatsbi
    *
    * @since 2.3.0-gatsbi
    */
   protected void drawColumnsForVisibleChampEntites()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{}

   public void setListObjectsRenderer(final TKSelectObjectRenderer<? extends TKdataObject> listObjectsRenderer){
      this.listObjectsRenderer = (PrelevementRowRenderer) listObjectsRenderer;
   }

   @Override
   public List<Prelevement> getListObjects(){
      return this.listObjects;
   }

   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      clearSelection();
      this.listObjects.clear();
      this.listObjects.addAll((List<Prelevement>) objs);
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(obj != null){
         if(pos != null){
            getListObjects().add(pos.intValue(), (Prelevement) obj);
         }else{
            getListObjects().add((Prelevement) obj);
         }
      }
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){
      this.selectedObjects = (List<Prelevement>) objs;
   }

   @Override
   public List<Prelevement> getSelectedObjects(){
      return this.selectedObjects;
   }

   @Override
   public void addToSelectedObjects(final TKdataObject obj){
      if(!getSelectedObjects().contains(obj)){
         getSelectedObjects().add((Prelevement) obj);
      }
   }

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){
      if(getSelectedObjects().contains(obj)){
         getSelectedObjects().remove(obj);
      }
   }

   @Override
   public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
      return listObjectsRenderer;
   }

   @Override
   public PrelevementController getObjectTabController(){
      return (PrelevementController) super.getObjectTabController();
   }

   @Override
   public void passSelectedToList(){
      getListObjects().clear();
      getListObjects().addAll(getSelectedObjects());
   }

   @Override
   public void passListToSelected(){
      getSelectedObjects().clear();
      getSelectedObjects().addAll(getListObjects());
   }

   @Override
   public void initObjectsBox(){

      final List<Prelevement> prlvts = ManagerLocator.getPrelevementManager()
         .findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope), getNbLastObjs());

      setListObjects(prlvts);
      setCurrentRow(null);
      setCurrentObject(null);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   /**
    * Méthode appelée après lors du focus sur le champ codeBoxPrlvt. Le radiobutton
    * correspondant sera automatiquement sélectionné.
    */
   public void onSelect$dateCreationBoxPrlvt(){
      dateCreation.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ codeBoxPrlvt. Le radiobutton
    * correspondant sera automatiquement sélectionné.
    */
   public void onFocus$codeBoxPrlvt(){
      codePrlvt.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ nomPatientPrlvt. Le
    * radiobutton correspondant sera automatiquement sélectionné.
    */
   public void onFocus$nomPatientPrlvt(){
      patientPrlvt.setChecked(true);
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ codeBoxPrlvt.
    * Cette valeur sera mise en majuscules.
    */
   public void onBlur$codeBoxPrlvt(){
      codeBoxPrlvt.setValue(codeBoxPrlvt.getValue().toUpperCase());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ nomPatientPrlvt.
    * Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomPatientPrlvt(){
      nomPatientPrlvt.setValue(nomPatientPrlvt.getValue().toUpperCase());
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return ManagerLocator.getPrelevementManager().findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope),
         getNbLastObjs());
   }

   @Override
   public List<Integer> doFindObjects(){

      List<Integer> prelevements = new ArrayList<>();

      if(dateCreation.isChecked()){
         prelevements = ManagerLocator.getPrelevementManager().findAfterDateCreationReturnIdsManager(getSearchDateCreation(),
            SessionUtils.getSelectedBanques(sessionScope));
      }else if(codePrlvt.isChecked()){
         if(searchCode == null){
            searchCode = "";
         }
         searchCode = searchCode.toUpperCase();
         codeBoxPrlvt.setValue(searchCode);
         if(!searchCode.equals("")){
            if(searchCode.contains(",")){
               final List<String> codes = ObjectTypesFormatters.formateStringToList(searchCode);
               prelevements = ManagerLocator.getPrelevementManager().findByCodeOrNumLaboInListManager(codes,
                  SessionUtils.getSelectedBanques(sessionScope));
            }else{
               prelevements = ManagerLocator.getPrelevementManager().findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(
                  searchCode, SessionUtils.getSelectedBanques(sessionScope), true);
            }
         }else{
            if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {"Prelevement"}),
               Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               prelevements = ManagerLocator.getPrelevementManager()
                  .findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope));
            }
         }
      }else if(patientPrlvt.isChecked()){
         if(searchNomPatient.contains(",")){
            final List<String> pats = ObjectTypesFormatters.formateStringToList(searchNomPatient);
            prelevements = ManagerLocator.getPrelevementManager().findByPatientNomOrNipInListManager(pats,
               SessionUtils.getSelectedBanques(sessionScope));
         }else{
            prelevements = ManagerLocator.getPrelevementManager().findByPatientNomReturnIdsManager(searchNomPatient,
               SessionUtils.getSelectedBanques(sessionScope), true);
         }
      }
      return prelevements;
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      if(ids != null && ids.size() > 0){
         return ManagerLocator.getPrelevementManager().findByIdsInListManager(ids);
      }
      return new ArrayList<Prelevement>();
   }

   /**
    * Lance la recherche des prélèvements en fournissant un fichier Excel contenant
    * une liste de codes.
    */
   public void onClick$findByListCodes(){
      // récupère les codes des prlvts présents dans le
      // fichier excel que l'utilisateur va uploader
      final List<String> codes = getListStringToSearch();
      final List<Integer> prelevements = ManagerLocator.getPrelevementManager().findByCodeOrNumLaboInListManager(codes,
         SessionUtils.getSelectedBanques(sessionScope));
      // affichage de ces résultats
      showResultsAfterSearchByList(prelevements);
   }

   /**
    * Lance la recherche des prélèvements en fournissant un fichier Excel contenant
    * une liste de patients (noms ou nips).
    */
   public void onClick$findByListPatients(){
      // récupère les codes des prlvts présents dans le
      // fichier excel que l'utilisateur va uploader
      final List<String> pats = getListStringToSearch();
      final List<Integer> prelevements = findPrelevementsByPatientCodes(pats);
      // affichage de ces résultats
      showResultsAfterSearchByList(prelevements);
   }

   /**
    * Sera surchargée par GATSBI.
    * @since 2.3.0-gatsbi
    * @param pats
    * @return
    */
   protected List<Integer> findPrelevementsByPatientCodes(List<String> pats){
      return ManagerLocator.getPrelevementManager().findByPatientNomOrNipInListManager(pats,
         SessionUtils.getSelectedBanques(sessionScope));
   }

   /**
    * Forwarded Event. Sélectionne le patient concerné pour l'afficher dans la
    * fiche.
    *
    * @param event forwardé depuis le lable nom cliquable (event.getData contient
    *              l'objet Prelevement).
    */
   public void onClickPatient(final Event event){

      // déselection de la ligne courante
      this.deselectRow();

      // sélection de la nouvelle ligne
      selectRow(getRow((ForwardEvent) event), (TKdataObject) event.getData());

      final PatientController tabController = (PatientController) PatientController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode(((Prelevement) getCurrentObject()).getMaladie().getPatient());
      }
   }

   /**
    * Forwarded Event. Sélectionne la maladie concernée pour l'afficher dans la
    * fiche.
    *
    * @param event forwardé depuis le lable nom cliquable (event.getData contient
    *              l'objet Prelevement).
    */
   public void onClickMaladie(final Event event){

      // déselection de la ligne courante
      this.deselectRow();

      // sélection de la nouvelle ligne
      selectRow(getRow((ForwardEvent) event), (TKdataObject) event.getData());

      final PatientController tabController = (PatientController) PatientController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode(((Prelevement) getCurrentObject()).getMaladie().getPatient());
         ((FichePatientStatic) tabController.getFicheStatic()).openMaladiePanel(((Prelevement) getCurrentObject()).getMaladie());
      }
   }

   /**
    * Colore les lignes de la liste en fonction du nb d'échantillons restants.
    */
   public void colorateGrid(){
      final Rows rows = objectsListGrid.getRows();
      final List<Component> comps = rows.getChildren();

      for(int i = 0; i < listObjects.size(); i++){
         final Prelevement prel = listObjects.get(i);
         if(PrelevementUtils.getNbEchanRestants(prel) == 0){
            final Row row = (Row) comps.get(i);
            row.setStyle("background-color : #FEBAB3");
         }else if(PrelevementUtils.getNbEchanRestants(prel) == 1){
            final Row row = (Row) comps.get(i);
            row.setStyle("background-color : #FDDFA9");
         }
      }
   }

   /**
    * Déselectionne la ligne actuellement sélectionnée.
    */
   @Override
   public void deselectRow(){
      // on vérifie qu'une ligne est bien sélectionnée
      if(getCurrentObject() != null && getCurrentRow() != null){
         final int ind = getListObjects().indexOf(getCurrentObject());
         // on lui spécifie une couleur en fonction de son
         // numéro de ligne
         if(ind > -1){
            getCurrentRow().setStyle("background-color : #e2e9fe");

            if(PrelevementUtils.getNbEchanRestants((Prelevement) getCurrentObject()) == 0){
               getCurrentRow().setStyle("background-color : #FEBAB3");
            }else if(PrelevementUtils.getNbEchanRestants((Prelevement) getCurrentObject()) == 1){
               getCurrentRow().setStyle("background-color : #FDDFA9");
            }

            setCurrentRow(null);
            setCurrentObject(null);
         }
      }
   }

   /**
    * Méthode appelée pour ouvrir la page de recherche avancée.
    */
   public void onClick$findMore(){

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("recherche.avancee.prelevements"));
      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);

      openRechercheAvanceeWindow(page, sb.toString(), entite, Path.getPath(self), isAnonyme(), this);

   }

   /**********************************************************************/
   /****************** Modale changement collection **********************/
   /**********************************************************************/
   /**
    * Ouvre la modale permettant le changement de collection.
    */
   public void onClick$changeCollectionItem(){
      final Prelevement[] prlvts = getSelectedObjects().toArray(new Prelevement[getSelectedObjects().size()]);
      openChangeModaleWindow(prlvts);
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le menu item pour changer le
    * prelevement de collection.
    */
   public void openChangeModaleWindow(final Prelevement[] prlvts){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("changeCollectionWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fichePrelevement.switchBanque.title"));
         win.setBorder("normal");
         win.setWidth("650px");
         win.setPosition("center, top");
         // win.setHeight("600px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateChangeCollectionModal(prlvts, win, page, getMainWindow());
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               // progress.detach();
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   private static HtmlMacroComponent populateChangeCollectionModal(final Prelevement[] prlvts, final Window win, final Page page,
      final MainWindow main){
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("changeBanqueModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openchangeCollectionModale");
      ua.applyProperties();
      ua.afterCompose();

      ((ChangeBanqueModale) ua.getFellow("fwinChangeBanqueModale").getAttributeOrFellow("fwinChangeBanqueModale$composer", true))
         .init(prlvts, main);

      return ua;
   }

   public void onClick$findDossierExterne(){
      Clients.showBusy(Labels.getLabel("recherche.avancee.en.cours"));
      Events.echoEvent("onLaterFindDossiers", self, null);
   }

   public void onLaterFindDossiers(){
      listObjects = new ArrayList<>();
      clearSelection();
      setListObjects(ManagerLocator.getPrelevementManager().findByDossierExternesManager(
         SessionUtils.getSelectedBanques(sessionScope), SessionUtils.getEmetteursInterfacages(sessionScope)));
      setCurrentRow(null);
      setCurrentObject(null);
      getObjectTabController().clearStaticFiche();
      getObjectTabController().switchToOnlyListeMode();
      getBinder().loadComponent(objectsListGrid);
      Clients.clearBusy();
   }

   /**
    * Forwarded Event. Sélectionne le dossier externe pour fiche.
    *
    * @param event forwardé depuis l'enveloppe cliquable (event.getData contient
    *              l'objet Prelevement).
    * @since 2.1
    *
    */
   public void onClickDossierExt(final Event event){
      if(getObjectTabController() != null){
         getObjectTabController().switchToFicheStaticMode((Prelevement) event.getData());
         getObjectTabController().getFicheStatic().onClick$importDossier();
      }
   }

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   @Override
   public void applyDroitsOnListe(){
      drawActionsButtons();
      canAccessPatient = getDroitOnAction("Patient", "Consultation");
      listObjectsRenderer.setAccessPatient(canAccessPatient);

      if(sessionScope.containsKey("ToutesCollections")){
         // donne aucun droit en creation
         setCanNew(false);
      }

      super.applyDroitsOnListe();
      listObjectsRenderer.setAnonyme(isAnonyme());

      if(SessionUtils.getEmetteursInterfacages(sessionScope).size() > 0){
         findDossierExterne.setVisible(true);
      }else{
         findDossierExterne.setVisible(false);
      }
   }

   public boolean isCanAccessPatient(){
      return canAccessPatient;
   }

   public void setCanAccessPatient(final boolean canAccess){
      this.canAccessPatient = canAccess;
   }

   public boolean getBanqueDefMaladies(){
      if(sessionScope.containsKey("Banque")){
         return SessionUtils.getSelectedBanques(sessionScope).get(0).getDefMaladies();
      }
      return true;
   }

   public PrelevementsNbEchantillonsComparator getComparatorAsc(){
      return comparatorAsc;
   }

   public void setComparatorAsc(final PrelevementsNbEchantillonsComparator c){
      this.comparatorAsc = c;
   }

   public PrelevementsNbEchantillonsComparator getComparatorDesc(){
      return comparatorDesc;
   }

   public void setComparatorDesc(final PrelevementsNbEchantillonsComparator c){
      this.comparatorDesc = c;
   }

   @Override
   public void batchDelete(final List<Integer> ids, final String comment){
      ManagerLocator.getPrelevementManager().removeListFromIdsManager(ids, comment, SessionUtils.getLoggedUser(sessionScope));
   }
}
