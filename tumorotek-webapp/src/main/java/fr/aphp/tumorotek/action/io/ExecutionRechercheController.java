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
package fr.aphp.tumorotek.action.io;

import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentBanque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.listmodel.GridResultsModel;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.general.export.ExportRechercheComplexe;

/**
 * Classe gérant l'exécution d'une recherche.
 *
 * @author pierre
 *
 */
public class ExecutionRechercheController extends AbstractObjectTabController
{

   private final Logger log = LoggerFactory.getLogger(ExecutionRechercheController.class);

   private static final long serialVersionUID = -1932781990848916925L;

   private List<Recherche> recherches = new ArrayList<>();

   private List<Banque> banques = new ArrayList<>();

   private Banque selectedBanque;

   private Recherche selectedRecherche;

   private Paging resPaging;

   private GridResultsModel model;

   private int _pageSize = 20;

   private int _startPageNumber = 0;

   private int _totalSize = 0;

   private boolean _needsTotalSizeUpdate = true;

   private Grid listObjets;

   private final List<Object> objets = new ListModelList<>();

   private Listbox recherchesBox;

   private Affichage affichage;

   private Menuitem showPatients;

   private Menuitem showPrelevements;

   private Menuitem showEchantillons;

   private Menuitem showProdDerives;

   private Menuitem exportItemINCa;

   private Menuitem exportItemTVGSO;

   private Menuitem exportItemTVGSOcsv;

   private Menuitem exportItemBIOCAP;

   private String entite;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setBinder(new AnnotateDataBinder(comp));

      if(mainBorder != null){
         mainBorder.setHeight(getMainWindow().getPanelHeight() + "px");
      }

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() + "px");
      }

      initAssociations();

      applyDroitOnController();
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return null;
   }

   public void initAssociations(){

      final Banque banqueCourante = getCurrentBanque(sessionScope);
      final List<Banque> tmp = getMainWindow().getBanques();

      banques = new ArrayList<>();
      for(int i = 0; i < tmp.size(); i++){
         if(tmp.get(i) != null && tmp.get(i).getBanqueId() != null){
            banques.add(tmp.get(i));
         }
      }

      selectedBanque = banqueCourante;

      final List<Banque> listeBanqueRecherche = new ArrayList<>();
      listeBanqueRecherche.add(banqueCourante);

      recherches = ManagerLocator.getRechercheManager().findByBanqueInLIstManager(listeBanqueRecherche);
      Collections.sort(recherches);
      recherches.add(0, null);
      selectedRecherche = null;

      getBinder().loadComponent(self);
   }

   /**
    * Lance la recherche.
    */
   public void onClick$launchSearch(){
      if(selectedRecherche != null){

         try{
            if(isAnonyme()){
               // check anonyme
               // si un affichage ou un critère porte sur
               // l'identification du patient -> error
               boolean reqNominative = false;

               // affichages
               final Iterator<Resultat> itRes =
                  ManagerLocator.getResultatManager().findByAffichageManager(selectedRecherche.getAffichage()).iterator();
               while(itRes.hasNext()){
                  final Resultat res = itRes.next();
                  if(res.getChamp() != null && res.getChamp().getChampEntite() != null
                     && res.getChamp().getChampEntite().getEntite().getNom().equals("Patient")){
                     if(res.getChamp().getChampEntite().getNom().equals("Nip")
                        || res.getChamp().getChampEntite().getNom().equals("Nom")
                        || res.getChamp().getChampEntite().getNom().equals("Prenom")
                        || res.getChamp().getChampEntite().getNom().equals("NomNaissance")
                        || res.getChamp().getChampEntite().getNom().equals("DateNaissance")){
                        reqNominative = true;
                        break;
                     }
                  }
               }

               // criteres

               if(!reqNominative){
                  // criteres
                  final Iterator<Critere> itCrits = ManagerLocator.getGroupementManager()
                     .findCriteresManager(selectedRecherche.getRequete().getGroupementRacine()).iterator();
                  while(itCrits.hasNext()){
                     final Critere crit = itCrits.next();
                     if(crit.getChamp() != null && crit.getChamp().getChampEntite() != null
                        && crit.getChamp().getChampEntite().getEntite().getNom().equals("Patient")){
                        if(crit.getChamp().getChampEntite().getNom().equals("Nip")
                           || crit.getChamp().getChampEntite().getNom().equals("Nom")
                           || crit.getChamp().getChampEntite().getNom().equals("Prenom")
                           || crit.getChamp().getChampEntite().getNom().equals("NomNaissance")
                           || crit.getChamp().getChampEntite().getNom().equals("DateNaissance")){
                           reqNominative = true;
                           break;
                        }
                     }
                  }
               }

               if(reqNominative){
                  throw new RuntimeException(Labels.getLabel("execution.recherche.erreur.nominative"));
               }
            }
            //On execute la recherche
            openSetCriteresValuesWindow();
         }catch(final RuntimeException e){
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }
   }

   /**
    * Export les résultats au format excel.
    */
   public void onClick$exportItem(){
      if(selectedRecherche != null){
         desktop.enableServerPush(true);
         // REFLEXIVITE
         final ExportRechercheComplexe exp = new ExportRechercheComplexe();
         exp.init(desktop, objets, callProgressBar(), affichage, selectedRecherche.getIntitule(),
            SessionUtils.getLoggedUser(sessionScope));
         exp.start();
      }
   }

   public void onClick$exportItemINCa(){
      onLaterExportCatalogue((List<Integer>) getObjectsFromList("Echantillon", true), ConfigManager.INCA_EXPORT, 3, false, null);
   }

   public void onClick$exportItemTVGSO(){
      onLaterExportCatalogue((List<Integer>) getObjectsFromList("Echantillon", true), ConfigManager.TVGSO_EXPORT, 3, false, null);
   }

   public void onClick$exportItemTVGSOcsv(){
      onLaterExportCatalogue((List<Integer>) getObjectsFromList("Echantillon", true), ConfigManager.TVGSO_EXPORT, 3, true, null);
   }

   public void onClick$exportItemBIOCAP(){
      onLaterExportCatalogue((List<Integer>) getObjectsFromList("Echantillon", true), ConfigManager.BIOCAP_EXPORT, 3, false,
         null);
   }

   /**
    * PopUp window appelée permettre la définition de valeurs des critères de
    * la requête et l'exécution de la requête.
    *
    * @param page
    *            dans laquelle inclure la modale
    * @param oldSelected
    *            Liste des champs déjà sélectionnés.
    */
   public void openSetCriteresValuesWindow(){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("setCriteresValuesWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("requete.set.values.title"));
         win.setBorder("normal");
         win.setWidth("620px");
         final int height = 510;
         win.setHeight(String.valueOf(height) + "px");
         win.setClosable(true);

         final HtmlMacroComponent ua;
         ua = (HtmlMacroComponent) page.getComponentDefinition("setCriteresValuesModale", false).newInstance(page, null);
         ua.setParent(win);
         ua.setId("setCriteresValuesModaleComponent");
         ua.applyProperties();
         ua.afterCompose();

         final List<Banque> bks = SessionUtils.getSelectedBanques(sessionScope);
         ((FicheSetCriteresValues) ua.getFellow("fwinSetCriteresvaluesModale")
            .getAttributeOrFellow("fwinSetCriteresvaluesModale$composer", true)).init(bks, self, selectedRecherche);
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
            log.error(e.getMessage(), e); 
         }
      }
   }

   /******************************************************************/
   /************** Execution de la recherche *******************/
   /******************************************************************/

   /**
    * Ré-initialise la liste des résultats.
    */
   //private void testReload() {
   //	// On supprime toutes les lignes
   //	ListModel lm = new ListModelList(new ArrayList<Object>());
   //	listObjets.setModel(lm);
   //	getBinder().loadComponent(listObjets);
   //}

   //	private void cleanUp() {
   //		for (Component chld : listObjets.getChildren()) {
   //			if (chld instanceof Column && chld instanceof Row) {
   //				chld.detach();
   //			}
   //		};
   //	}

   /**
    * Récupération des résultats et affichage de ceux-ci.
    *
    * @param e
    */

   public void onGetSearchResults(final Event e){
      // cleanUp();

      affichage = selectedRecherche.getAffichage().clone();
      affichage.setResultats(ManagerLocator.getResultatManager().findByAffichageManager(affichage));

      // On modifie les attributs dynamiquement.
      winPanel.setTitle(selectedRecherche.getIntitule());

      // On construit les colonnes
      final Columns columns = new Columns();
      columns.setMenupopup("auto");
      final List<Resultat> resultats = affichage.getResultats();
      // On trie les résultats par ordre de position
      if(resultats.size() > 1){
         for(int i = 1; i < resultats.size(); i++){
            for(int j = i - 1; j >= 0; j--){
               if(resultats.get(j + 1).getPosition() < resultats.get(j).getPosition()){
                  final Resultat temp = resultats.get(j + 1);
                  resultats.set(j + 1, resultats.get(j));
                  resultats.set(j, temp);
               }
            }
         }
      }
      final Iterator<Resultat> itCol = resultats.iterator();
      while(itCol.hasNext()){
         final Resultat res = itCol.next();
         final Column colObjet = new Column(res.getNomColonne());
         colObjet.setParent(columns);
      }
      if(listObjets.getColumns() != null){
         listObjets.getColumns().setParent(null);
      }
      columns.setParent(listObjets);

      final ResultatRowRenderer renderer = new ResultatRowRenderer();
      renderer.setResultats(resultats);
      listObjets.setRowRenderer(renderer);

      // getBinder().loadComponent(listObjets);

      objets.clear();
      if(e.getData() != null){
         objets.addAll((List<Object>) e.getData());
      }

      // on génère l'affichage des résultats
      // objAffRenderer = new ObjetsAffichageRenderer(
      //		objets, affichage);
      // objAffRenderer.renderGrid(listObjets);

      //On rafraichit la page
      // getBinder().loadComponent(listObjets);

      // listObjets.setPageSize(affichage.getNbLignes());
      _pageSize = affichage.getNbLignes();
      _needsTotalSizeUpdate = true;
      resPaging.setActivePage(0);

      refreshModel(0);
   }

   public void onClick$showPatients(){
      entite = "Patient";
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterShowResultsInList", self, null);
   }

   public void onClick$showPrelevements(){
      entite = "Prelevement";
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterShowResultsInList", self, null);
   }

   public void onClick$showEchantillons(){
      entite = "Echantillon";
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterShowResultsInList", self, null);
   }

   public void onClick$showProdDerives(){
      entite = "ProdDerive";
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterShowResultsInList", self, null);
   }

   public List<? extends Object> getObjectsFromList(final String entiteObjects, final boolean getId){
      final List<List<Object>> matriceObjets = new ArrayList<>();

      for(int i = 0; i < objets.size(); i++){
         matriceObjets.add(RechercheUtilsManager.getListeObjetsCorrespondants(objets.get(i), affichage, entiteObjects));
      }

      final List<Object> resultats = new ArrayList<>();
      TKdataObject tkObj;
      for(int i = 0; i < matriceObjets.size(); i++){
         if(matriceObjets.get(i) != null && !matriceObjets.get(i).isEmpty()){
            tkObj = (TKdataObject) matriceObjets.get(i).get(0);
            if(tkObj != null){
               if(!getId){
                  resultats.add(tkObj);
               }else{
                  resultats.add(tkObj.listableObjectId());
               }
            }
         }
      }

      return resultats;

      //		ListModelList<Object> lm = (ListModelList<Object>)
      //										listObjets.getModel();
      //		List<Object> resultats = new ArrayList<Object>();
      //		if (lm != null) {
      //			// pour chaque ligne de résultats
      //			for (int i = 0; i < lm.getSize(); i++) {
      //				ResultatRow res = (ResultatRow) lm.get(i);
      //				boolean added = false;
      //				int j = 0;
      //				// on parcourt les colonnes
      //				while (j < res.getObjects().size() && !added) {
      //					// pour chaque objet contenu dans la colonne
      //					// on test s'il est du type d'objet que l'on souhaite
      //					// extraire.
      //					// si c'est la cas, on l'ajoute à la liste
      //					if (res.getObjects().get(j) != null
      //							&& res.getObjects().get(j).getClass()
      //									.getSimpleName().contains(entiteObjects)) {
      //						if (!resultats.contains(res.getObjects().get(j))) {
      //							resultats.add(res.getObjects().get(j));
      //						}
      //						added = true;
      //					}
      //					++j;
      //				}
      //			}
      //		}
      //
      //		return resultats;
   }

   /**
    * Cette méthode va extraire les objets de la liste de résultats et les
    * envoyer dans la liste de l'onglet correspondant à leur entité.
    */

   public void onLaterShowResultsInList(){
      final List<? extends Object> resultats = getObjectsFromList(entite, true);

      postIdsToOtherEntiteTab(entite, (List<Integer>) resultats);

      Clients.clearBusy();
   }

   /**
    * Forwarded Event. Sélectionne le patient concerné pour l'afficher dans la
    * fiche.
    *
    * @param event
    *            forwardé depuis le label Nip Patient cliquable (event.getData
    *            contient l'objet Prelevement).
    */
   public void onClickPatientNip(final Event event){

      final PatientController tabController = (PatientController) PatientController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode((Patient) event.getData());
      }
   }

   /**
    * Forwarded Event. Sélectionne le prelevement concerné pour l'afficher dans
    * la fiche.
    *
    * @param event
    *            forwardé depuis le label Code Prelevement cliquable
    *            (event.getData contient l'objet Prelevement).
    */
   public void onClickPrelevementCode(final Event event){

      final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode((Prelevement) event.getData());
      }
   }

   /**
    * Forwarded Event. Sélectionne l'echantillon concerné pour l'afficher dans
    * la fiche.
    *
    * @param event
    *            forwardé depuis le label Code Echantillon cliquable
    *            (event.getData contient l'objet Echantillon).
    */
   public void onClickEchantillonCode(final Event event){

      final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode((Echantillon) event.getData());
      }
   }

   /**
    * Forwarded Event. Sélectionne l'echantillon concerné pour l'afficher dans
    * la fiche.
    *
    * @param event
    *            forwardé depuis le label Code ProdDerive cliquable
    *            (event.getData contient l'objet ProdDerive).
    */
   public void onClickProdDeriveCode(final Event event){

      final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode((ProdDerive) event.getData());
      }
   }

   /**
    * Forwarded Event. Sélectionne la cession concernée pour l'afficher dans la
    * fiche.
    *
    * @param event
    *            forwardé depuis le label Numero Cession cliquable
    *            (event.getData contient l'objet Cession).
    */
   public void onClickCessionNumero(final Event event){

      final CessionController tabController = (CessionController) CessionController.backToMe(getMainWindow(), page);

      if(tabController != null){
         tabController.switchToFicheStaticMode((Cession) event.getData());
      }
   }

   public void applyDroitOnController(){
      //		List<String> entites = new ArrayList<String>();
      //		entites.add("Patient");
      //		entites.add("Prelevement");
      //		entites.add("Echantillon");
      //		entites.add("ProdDerive");
      //		setDroitsConsultation(drawConsultationLinks(entites));

      showPatients.setDisabled(!getDroitsConsultation().get("Patient"));
      showPrelevements.setDisabled(!getDroitsConsultation().get("Prelevement"));
      showEchantillons.setDisabled(!getDroitsConsultation().get("Echantillon"));
      showProdDerives.setDisabled(!getDroitsConsultation().get("ProdDerive"));

      if(sessionScope.containsKey("Anonyme") && (Boolean) sessionScope.get("Anonyme")){
         setAnonyme(true);
      }else{
         setAnonyme(false);
      }

      // droits sur les exports
      boolean inca = false;
      boolean tvgso = false;
      boolean biocap = false;
      if(sessionScope.containsKey("catalogues")){

         final Map<String, Catalogue> catsMap = (Map<String, Catalogue>) sessionScope.get("catalogues");

         inca = catsMap.containsKey("INCa");
         tvgso = catsMap.containsKey("TVGSO");
         biocap = catsMap.containsKey("BIOCAP");
      }
      exportItemINCa.setVisible(inca);
      exportItemTVGSO.setVisible(tvgso);
      exportItemTVGSOcsv.setVisible(tvgso);
      exportItemBIOCAP.setVisible(biocap);
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      return null;
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   @Override
   public AbstractFicheEditController getFicheEdit(){
      return null;
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return null;
   }

   @Override
   public AbstractFicheStaticController getFicheStatic(){
      return null;
   }

   @Override
   public AbstractListeController2 getListe(){
      return null;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> banques){
      this.banques = banques;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selectedBanque){
      this.selectedBanque = selectedBanque;
   }

   public List<Recherche> getRecherches(){
      return recherches;
   }

   public void setRecherches(final List<Recherche> r){
      this.recherches = r;
   }

   public Recherche getSelectedRecherche(){
      return selectedRecherche;
   }

   public void setSelectedRecherche(final Recherche selectedR){
      this.selectedRecherche = selectedR;
   }

   public String getEntite(){
      return entite;
   }

   public void setEntite(final String e){
      this.entite = e;
   }

   private void refreshModel(final int activePage){
      resPaging.setPageSize(_pageSize);
      model = new GridResultsModel(activePage, _pageSize, objets, affichage);

      if(_needsTotalSizeUpdate){
         _totalSize = model.getTotalSize();
         _needsTotalSizeUpdate = false;
      }

      resPaging.setTotalSize(_totalSize);

      listObjets.setModel(model);
   }

   public void onPaging$resPaging(final ForwardEvent event){
      final PagingEvent pe = (PagingEvent) event.getOrigin();
      _startPageNumber = pe.getActivePage();
      refreshModel(_startPageNumber);
   }

   public void onSelect$banquesBox(){

      selectedRecherche = null;

      final List<Banque> listeBanqueRecherche = new ArrayList<>();
      listeBanqueRecherche.add(selectedBanque);

      final List<Recherche> listeRecherche = ManagerLocator.getRechercheManager().findByBanqueInLIstManager(listeBanqueRecherche);
      listeRecherche.add(0, null);

      final ListModel<Recherche> listModel = new BindingListModelList<>(listeRecherche, true);
      recherchesBox.setModel(listModel);

   }

}

