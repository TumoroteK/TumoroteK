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
package fr.aphp.tumorotek.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.North;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.io.AffichageController;
import fr.aphp.tumorotek.action.io.RechercheComplexeController;
import fr.aphp.tumorotek.action.io.RechercheController;
import fr.aphp.tumorotek.action.io.RequeteController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.action.recherche.historique.SearchHistory;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.action.utilisateur.FicheUtilisateurModale;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.ConnexionUtils;
import fr.aphp.tumorotek.webapp.general.MainTabbox;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.general.ext.ResourceRequest;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class MainWindow extends GenericForwardComposer<Component> {

	private final Log log = LogFactory.getLog(MainWindow.class);

	private static final long serialVersionUID = -2216377552659874209L;

	private Listbox mainBanquesListBox;

	private Window mainWinRight;
	private Window mainWinLeft;
	private Window mainWinBottom;
	private Box mainCenterVbox;
	private Tabbox mainTabbox;
	private List<Banque> banques = new ArrayList<>();
	private Banque selectedBanque;
	// = ManagerLocator
	// .getBanqueManager().findAllObjectsManager().get(0);

	private Integer screenHeight = 800;
	private Integer screenWidth = 1280;
	private boolean screenUpdated = false;

	private Integer tabPanelsHeight;
	private Integer panelHeight;
	// liste contenant les noms des tabs
	private List<String> tabsNames = new ArrayList<>();
	private List<String> availableTabsNames = new ArrayList<>();
	// bouton de déconnexion
	// private Button buttonLogout;

	// @since 2.1 scan device
	protected Timer scanTimer;
	protected Div scanButtonDiv;

	// @since 2.2.3-genno
	private Div genno;
	
	// @since 2.30-gatsbi
	// conserve la banque pour remettre la liste
	// si erreur conn GATSBI
	private Banque previousBanque;

	private Hashtable<String, String> echantillonTypesCouleur = new Hashtable<>();
	private Hashtable<String, String> prodDeriveTypesCouleur = new Hashtable<>();

	// Search History
	public static String SEARCH_HISTORY_SESSIONS = "SearchHistorySessions";
	private Map<Etude, LinkedList<SearchHistory>> searchHistoryMap;

	private AnnotateDataBinder mainBinder;

	public AnnotateDataBinder getMainBinder() {
		return mainBinder;
	}

	public void setMainBinder(final AnnotateDataBinder mBinder) {
		this.mainBinder = mBinder;
	}

	private boolean blockModal = false;

	public boolean isBlockModal() {
		return blockModal;
	}

	public void setBlockModal(final boolean b) {
		this.blockModal = b;
	}

	@Override
	public void doAfterCompose(final Component comp) throws Exception {

		// Clients.showBusy("wait...");
		super.doAfterCompose(comp);
		mainBinder = new AnnotateDataBinder(comp);

		// mainBinder.loadAll();
		// Récupe de la banque sélectionnée et de l'utilisateur
		final Utilisateur user = SessionUtils.getLoggedUser(sessionScope);
		if (user != null) { // DEBUG only
			prepareListBanques();
		}
		initDroitsConsultation();

		// on récupère la taille de l'écran
		if (sessionScope.containsKey("screenHeight")) {
			screenHeight = (Integer) sessionScope.get("screenHeight");
		}
		if (sessionScope.containsKey("screenWidth")) {
			screenWidth = (Integer) sessionScope.get("screenWidth");
		}

		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("general.logout"));
		if (user != null) {
			sb.append(" ");
			sb.append(user.getLogin());
		}

		// Récupère la window principale
		final Window win = (Window) mainCenterVbox.getFellow("main");
		// Récupère le borderlayout principal contenant toute la
		// fenêtre TK
		final Borderlayout mainBorderLayout = (Borderlayout) win.getFellow("mainBorderLayout");
		// Définition de la hauteur et de la largeur de la fenêtre TK
		mainBorderLayout.setHeight(getWindowAvailableHeight() + "px");
		mainBorderLayout.setWidth(getWindowAvailableWidth() + "px");

		// Définition de la taille du div central de la bannière
		/*
		 * Div divSeparator = (Div) mainBorderLayout .getFellow("northTopBanniere")
		 * .getFellow("hboxTopBanniere") .getFellow("divSeparator");
		 * divSeparator.setWidth(getDivSeparatorWidth() + "px");
		 */

		// background
		final North north = (North) mainBorderLayout.getFellow("northTopBanniere");
		north.setStyle("background: url(${c:encodeURL('/images/topTumoBanniere_left_repeat.png')}); border:0;");

		// label du bouton
		final Button logout = (Button) mainBorderLayout.getFellow("northTopBanniere")
				// .getFellow("hboxTopBanniere")
				.getFellow("buttonLogout");
		logout.setLabel(sb.toString());

		// Définition des tailles des fenêtres entourant TK
		final int bottomHeight = screenHeight - getWindowAvailableHeight();
		mainWinRight.setWidth(Integer.toString(getRightWindowWidth()) + "px");
		mainWinRight.setHeight(Integer.toString(screenHeight) + "px");
		mainWinLeft.setWidth(Integer.toString(getLeftWindowWidth()) + "px");
		mainWinLeft.setHeight(Integer.toString(screenHeight) + "px");
		mainWinBottom.setHeight(bottomHeight + "px");

		initTabsNames();
		// init des couleurs en fct des types (pour l'affichage des boites)
		extractTypesColors();

		mainTabbox = (MainTabbox) mainBorderLayout.getFellow("mainCenterBorderLayout").getFellow("mainTabbox");
		initAvailableTabsNames();

		mainBanquesListBox = (Listbox) mainBorderLayout.getFellow("northTopBanniere").getFellow("mainBanquesListBox");

		final Button edit = (Button) mainBorderLayout.getFellow("northTopBanniere")
				// .getFellow("hboxTopBanniere")
				.getFellow("editUser");
		edit.addForward(null, self, "onClickEditUser", null);

		mainBinder.loadAll();

		// if (sessionScope.containsKey("patient")) {
		// launchImportPatientFromCrfProdedure((Patient) sessionScope
		// .get("patient"));
		// }

		// Initialise la sauvegarde de l'historique de recherche
		// @since 2.3.0-gatsbi, historique is mapped by etude (ie sets of fomr
		// contextes)
		searchHistoryMap = new HashMap<Etude, LinkedList<SearchHistory>>();
		sessionScope.put(SEARCH_HISTORY_SESSIONS, searchHistoryMap);

		if (sessionScope.containsKey("resourceRequest")) {
			displayTkObjDetails((ResourceRequest<?>) sessionScope.get("resourceRequest"));
		}

		scanButtonDiv = (Div) mainBorderLayout.getFellow("northTopBanniere").getFellow("scanButtonDiv");
		scanButtonDiv.addForward("onClick", self, "onSwitchScanButton");
		scanTimer = (Timer) scanButtonDiv.getFellow("scanTimer");
		scanTimer.addForward("onTimer", self, "onScanTimer");

		genno = (Div) mainBorderLayout.getFellow("northTopBanniere").getFellow("genno");
		genno.addForward("onClick", self, "onOpenDossierExternes");
	}

	public void prepareListBanques() {
		final Utilisateur user = SessionUtils.getLoggedUser(sessionScope);
		final Plateforme pf = SessionUtils.getPlateforme(sessionScope);

		// init des banques
		banques = ManagerLocator.getUtilisateurManager().getAvailableBanquesByPlateformeManager(user, pf, false);
		ConnexionUtils.initToutesCollectionsAccesses(banques, pf, user);
		if (sessionScope.containsKey("Banque")) {
			selectedBanque = (Banque) sessionScope.get("Banque");
		} else if (sessionScope.containsKey("ToutesCollections")) {
			Banque toutesColl = ConnexionUtils.initFakeToutesCollBankItem(pf);
			Etude etude = ((Banque) ((List<?>) sessionScope.get("ToutesCollections")).get(0)).getEtude();
			if (etude != null) { // gatsbi etude
				toutesColl.setNom(
						Labels.getLabel("select.banque.toutesCollection.gatsbi", new String[] { etude.getTitre() }));
			}
			selectedBanque = toutesColl;
		} else {
			selectedBanque = null;
			// selectedBanque = banques.get(0);
		}
	}

	public void resetMainBanquesListBox() {
		mainBinder.loadComponent(mainBanquesListBox);
	}

	/**
	 * Récupère la mainTabbox.
	 * 
	 * @return Tabbox.
	 */
	public Tabbox getMainTabbox() {
		return (Tabbox) self.getFellow("mainHbox").getFellow("mainCenterVbox").getFellow("main")
				.getFellow("mainBorderLayout").getFellow("mainCenterBorderLayout").getFellow("mainTabbox");
	}

	/**
	 * Disable ou non la liste des banques.
	 * 
	 * @param disable
	 */
	public void disableBanqueListbox(final boolean disable) {
		final Listbox banqueListbox = (Listbox) self.getFellow("main").getFellow("mainBanquesListBox");
		banqueListbox.setDisabled(disable);
	}

	/**
	 * @since 2.3.0-gatsbi vérifie que GATSBI est bien accessible
	 * @param bank
	 */
	public void updateSelectedBanque(Banque bank) {

		// @since 2.30-gatsbi
		// suppr err gatsbi éventuelle
		Clients.clearWrongValue(self.getFellow("main").getFellow("mainBanquesListBox"));

		// select banque
		if (bank != null) { // banque est modifié depuis un n'importe quel onglet
			setSelectedBanque(bank);
		} // sinon si bank == null, banque est modifiée depuis la liste
		  // MainBanquesListBox.onLaterUpdate et donc selectBanque a déja été appelé

		// user et plateforme peuvent être null car
		// ne seront jamais mis à jour depuis MainWindow update banque
		try {
			ConnexionUtils.selectConnection(null, null, selectedBanque, banques, session);
		} catch (GatsbiException e) {
			setSelectedBanque(previousBanque); // previous ne peut pas être nulle
			mainBinder.loadAttribute(self.getFellow("main").getFellow("mainBanquesListBox"), "selectedItem");
			Clients.clearBusy();
			throw new WrongValueException(mainBanquesListBox, e.getMessage());
		}

		// met à jour la pf si update banque cross-plateforme
		if (!selectedBanque.getPlateforme().equals(SessionUtils.getPlateforme(sessionScope))) {
			sessionScope.put("Plateforme", selectedBanque.getPlateforme());
			prepareListBanques();
			mainBinder.loadComponent(self.getFellow("main").getFellow("mainBanquesListBox"));
		}

		resetControllers();
		initAvailableTabsNames();
		initDroitsConsultation();
		// init des couleurs en fct des types (pour l'affichage des boites)
		extractTypesColors();
	}

	public void updateSelectedBanqueToutesColl() throws Exception {
		updateSelectedBanque(ConnexionUtils.initFakeToutesCollBankItem(SessionUtils.getCurrentPlateforme()));
	}

	private void resetControllers() {

		final Tabpanel selectedPanel = getMainTabbox().getSelectedPanel();

		final Iterator<Component> it = getMainTabbox().getTabpanels().getChildren().iterator();
		Tabpanel curr;
		while (it.hasNext()) {
			curr = (Tabpanel) it.next();
			// re-init stockage quoi qu'il arrive
			if (curr.getId().equals("stockagePanel")) {
				getStockageController().clearAllPage();
				getStockageController().generateDroits();
				getStockageController().initTree();
				getStockageController().switchToFicheAndListeMode();
				// efface le contenu des panels sinon
			} else if (!curr.equals(selectedPanel)) {
				Components.removeAllChildren(curr);
			} else { // reset le selectionné
				if (curr.getId().equals("patientPanel")) {
					Components.removeAllChildren(curr);
					mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("patientTab"));
					Events.postEvent("onSelect", mainTabbox, null);
					// ((PatientController) curr.getFellow("winPatient")
					// .getAttributeOrFellow("winPatient$composer",
					// true)).reset();
				} else if (curr.getId().equals("prelevementPanel")) {
					Components.removeAllChildren(curr);
					mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("prelevementTab"));
					Events.postEvent("onSelect", mainTabbox, null);
					// recharge la visibilité la colonne maladie de la liste
					// ((PrelevementController) curr.getFellow("winPrelevement")
					// .getAttributeOrFellow("winPrelevement$composer", true))
					// .getListe().getBinder().loadAll();
				} else if (curr.getId().equals("echantillonPanel")) {
					Components.removeAllChildren(curr);
					mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("echantillonTab"));
					Events.postEvent("onSelect", mainTabbox, null);
				} else if (curr.getId().equals("derivePanel")) {
					Components.removeAllChildren(curr);
					mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("deriveTab"));
					Events.postEvent("onSelect", mainTabbox, null);
				} else if (curr.getId().equals("cessionPanel")) {
					Components.removeAllChildren(curr);
					mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("cessionTab"));
					Events.postEvent("onSelect", mainTabbox, null);
				} else if (curr.getId().equals("recherchePanel")) {
					((RequeteController) getMainTabbox().getTabpanels().getFellow("recherchePanel")
							.getFellow("winRechercheComplexe").getFellow("exportTabbox").getFellow("panelModelisation")
							.getFellow("modelisationTabbox").getFellow("panelRequete").getFellow("requeteMacro")
							.getFellow("winRequete").getAttributeOrFellow("winRequete$composer", true)).reset();

					((AffichageController) getMainTabbox().getTabpanels().getFellow("recherchePanel")
							.getFellow("winRechercheComplexe").getFellow("exportTabbox").getFellow("panelModelisation")
							.getFellow("modelisationTabbox").getFellow("panelAffichage").getFellow("affichageMacro")
							.getFellow("winAffichage").getAttributeOrFellow("winAffichage$composer", true)).reset();

					((RechercheController) getMainTabbox().getTabpanels().getFellow("recherchePanel")
							.getFellow("winRechercheComplexe").getFellow("exportTabbox").getFellow("panelModelisation")
							.getFellow("modelisationTabbox").getFellow("panelRecherche").getFellow("rechercheMacro")
							.getFellow("winRecherche").getAttributeOrFellow("winRecherche$composer", true)).reset();

					((RechercheComplexeController) getMainTabbox().getTabpanels().getFellow("recherchePanel")
							.getFellow("winRechercheComplexe")
							.getAttributeOrFellow("winRechercheComplexe$composer", true)).resetExecutionController();
				} else if (curr.getId().equals("administrationPanel")) {
					Components.removeAllChildren(curr);
					if (sessionScope.containsKey("AccesAdmin") & (Boolean) sessionScope.get("AccesAdmin")) {
						mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("administrationTab"));
					} else {
						mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("prelevementTab"));
					}
					Events.postEvent("onSelect", mainTabbox, null);
				}
				// else if (curr.getId().equals("administrationPanel")) {
				// Components.removeAllChildren(curr);
				// mainTabbox
				// .setSelectedTab((Tab) mainTabbox
				// .getFellow("prelevementTab"));
				// }
			}
		}
	}

	/**
	 * Renvoie le controller du tab selectionné, si celui-ci gère le scan full-rack
	 * barcode donc Echantillon, Derive, Stockage, Cession
	 * 
	 * @return AbstractObjectTabController
	 * @since 2.1
	 */
	public AbstractObjectTabController getSelectedTabController() {

		final AbstractObjectTabController controller = null;
		final Tabpanel selectedPanel = getMainTabbox().getSelectedPanel();

		if (selectedPanel.getId().equals("echantillonPanel")) {
			return ((AbstractObjectTabController) selectedPanel.getFellow("winEchantillon")
					.getAttributeOrFellow("winEchantillon$composer", true));
		} else if (selectedPanel.getId().equals("derivePanel")) {
			return ((AbstractObjectTabController) selectedPanel.getFellow("winProdDerive")
					.getAttributeOrFellow("winProdDerive$composer", true));
		} else if (selectedPanel.getId().equals("cessionPanel")) {
			return ((AbstractObjectTabController) selectedPanel.getFellow("winCession")
					.getAttributeOrFellow("winCession$composer", true));
		} else if (selectedPanel.getId().equals("stockagePanel")) {
			return ((AbstractObjectTabController) selectedPanel.getFellow("winStockages")
					.getAttributeOrFellow("winStockages$composer", true));
		}
		return controller;
	}

	/**
	 * Méthode récupérant le controller du panel des stockages.
	 * 
	 * @return CessionController classe gérant le panel des stockages.
	 */
	public StockageController getStockageController() {

		final Tabpanels panels = getMainTabbox().getTabpanels();

		return (StockageController) panels.getFellow("stockagePanel").getFellow("winStockages")
				.getAttributeOrFellow("winStockages$composer", true);

	}

	/**
	 * Renvoie la hauteur disponible pour l'ensemble de la fenêtre TK : ceci
	 * représente 80% de la résolution de l'écran.
	 * 
	 * @return La hauteur disponible.
	 */
	public Integer getWindowAvailableHeight() {

		Integer mainHeigth = 0;
		if (screenHeight > 650) {
			mainHeigth = screenHeight;
		} else {
			mainHeigth = 600;
		}

		return mainHeigth;

	}

	/**
	 * Renvoie la largeur disponible pour l'ensemble de la fenêtre TK : ceci
	 * représente 95% de la résolution de l'écran.
	 * 
	 * @return La largeur disponible.
	 */
	public Integer getWindowAvailableWidth() {

		Integer width = 0;
		width = (screenWidth * 98) / 100;
		return width;
	}

	/**
	 * Renvoie l'espace à laisser à droite de la fenêtre TK.
	 * 
	 * @return L'espace à droite.
	 */
	public Integer getRightWindowWidth() {

		final Integer width = ((screenWidth - getWindowAvailableWidth()) / 2) - 5;

		return width;
	}

	/**
	 * Renvoie l'espace à laisser à gauche de la fenêtre TK.
	 * 
	 * @return L'espace à gauche.
	 */
	public Integer getLeftWindowWidth() {

		final Integer width = (screenWidth - getWindowAvailableWidth()) / 2 - 5;

		return width;
	}

	/**
	 * Renvoie la hauteur disponible pour la fenêtre contenue dans un TabPanel.
	 * 
	 * @return Hauteur d'un TabPanel.
	 */
	public Integer getTabPanelsHeight() {

		tabPanelsHeight = getWindowAvailableHeight() - 100;

		return tabPanelsHeight;
	}

	/**
	 * Renvoie la hauteur disponible pour un panel ou une fiche.
	 * 
	 * @return Hauteur d'un Panel.
	 */
	public Integer getPanelHeight() {

		panelHeight = getWindowAvailableHeight() - 140;

		return panelHeight;
	}

	/**
	 * Renvoie la hauteur disponible pour la liste des éléments.
	 * 
	 * @return Hauteur d'une liste.
	 */
	public Integer getListPanelHeight() {

		final int listPanelHeight = getPanelHeight() - 175;

		return listPanelHeight;
	}

	/**
	 * Renvoie la hauteur disponible pour une fiche annotation.
	 * 
	 * @return hauteur d'une fiche annotation.
	 */
	public Integer getAnnoPanelHeight() {
		final int listPanelHeight = getPanelHeight() - 26;
		return listPanelHeight;
	}

	/**
	 * Renvoie la largeur de la partie centrale de la bannière entre le logo TK et
	 * la sélection de la banque.
	 * 
	 * @return
	 */
	public Integer getDivSeparatorWidth() {
		final Integer width = getWindowAvailableWidth() - 560;

		return width;
	}

	/**
	 * Méthode qui initialise tous les noms des tabs.
	 */
	public void initTabsNames() {
		tabsNames.add("accueilTab");
		tabsNames.add("patientTab");
		tabsNames.add("prelevementTab");
		tabsNames.add("echantillonTab");
		tabsNames.add("deriveTab");
		tabsNames.add("cessionTab");
		tabsNames.add("stockageTab");
		tabsNames.add("rechercheTab");
		tabsNames.add("administrationTab");
		tabsNames.add("statTab");
	}

	/**
	 * Initialise les tabs qui sont disponibles pour l'utilisateur (en fonction de
	 * ses droits).
	 */
	public void initAvailableTabsNames() {

		availableTabsNames.clear();
		availableTabsNames.add("accueilTab");

		// Tab Patient
		if (!getDroitOnAction("Patient", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("patientTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("patientTab");
		}

		// Tab Prelevement
		if (!getDroitOnAction("Prelevement", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("prelevementTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("prelevementTab");
		}

		// Tab Echantillon
		if (!getDroitOnAction("Echantillon", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("echantillonTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("echantillonTab");
		}

		// Tab ProdDerive
		if (!getDroitOnAction("ProdDerive", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("deriveTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("deriveTab");
		}

		// Tab Cession
		if (!getDroitOnAction("Cession", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("cessionTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("cessionTab");
		}

		// Tab Stockage
		if (!getDroitOnAction("Stockage", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("stockageTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("stockageTab");
		}

		// Tab Requete
		if (!getDroitOnAction("Requete", "Consultation")) {
			final Tab tab = (Tab) mainTabbox.getFellow("rechercheTab");
			tab.setDisabled(true);
		} else {
			availableTabsNames.add("rechercheTab");
		}

		// si nous sommes en toutes collections
		if (sessionScope.containsKey("ToutesCollections")) {
			// on désactive l'onglet d'administration en toutes collections
			final Tab tab = (Tab) mainTabbox.getFellow("administrationTab");
			tab.setDisabled(true);
		} else {
			// Tab administration
			if (sessionScope.containsKey("AccesAdmin") && (Boolean) sessionScope.get("AccesAdmin")) {
				availableTabsNames.add("administrationTab");
			} else {
				final Tab tab = (Tab) mainTabbox.getFellow("administrationTab");
				tab.setDisabled(true);
			}
		}

		availableTabsNames.add("statTab");

		unblockAllPanels();

		setStartingPanel();
	}

	/**
	 * Selectionne l'onglet par defaut. Si un onglet a deja été choisi, le conserve
	 * si il est accessible, sinon selectionne l'onglet prelevement si il est
	 * accessible, sinon selectionne le premier onglet disponible.
	 * 
	 * @version 2.0.13.2
	 */
	private void setStartingPanel() {
		if (mainTabbox.getSelectedTab() == null || !availableTabsNames.contains(mainTabbox.getSelectedTab().getId())) {
			if (availableTabsNames.contains("prelevementTab")) {
				mainTabbox.setSelectedTab((Tab) mainTabbox.getFellow("prelevementTab"));
			} else {
				// Tab accueil = (Tab) mainTabbox.getFellow("accueilTab");
				// mainTabbox.setSelectedTab(accueil);
				// since 2.0.13.2
				// accueil default
				Tab firstAvail = (Tab) mainTabbox.getFellow(availableTabsNames.get(0));
				// if another tab can be selected, not statsTab
				if (availableTabsNames.size() > 1 && !availableTabsNames.get(1).equals("statTab")) {
					firstAvail = (Tab) mainTabbox.getFellow(availableTabsNames.get(1));
				}
				mainTabbox.setSelectedTab(firstAvail);
			}
		}

	}

	/**
	 * Méthode qui rend tous les tabs inactifs, sauf celui dont le nom est en
	 * paramètre.
	 * 
	 * @param tabName Nom du tab à garder actif.
	 */
	public void blockAllPanelsExceptOne(final String tabName) {
		if (tabName != null && tabsNames.contains(tabName)) {
			for (int i = 0; i < tabsNames.size(); i++) {
				if (!tabsNames.get(i).equals(tabName)) {
					final Tab tab = (Tab) getMainTabbox().getFellow(tabsNames.get(i));
					tab.setDisabled(true);
				}
			}
		}
	}

	/**
	 * Méthode qui rend actif tous les Tabs.
	 */
	public void unblockAllPanels() {
		for (int i = 0; i < availableTabsNames.size(); i++) {
			final Tab tab = (Tab) getMainTabbox().getFellow(availableTabsNames.get(i));
			tab.setDisabled(false);
		}
	}

	/**
	 * Extrait les couleurs définies pour les types d'échantillon et de produit
	 * dérivé.
	 */
	public void extractTypesColors() {
		echantillonTypesCouleur = new Hashtable<>();
		List<CouleurEntiteType> couleurs = ManagerLocator.getCouleurEntiteTypeManager()
				.findAllCouleursForEchanTypeByBanqueManager(selectedBanque);

		// pour chaque association, on l'ajoute dans l'hashtable
		for (int i = 0; i < couleurs.size(); i++) {
			final CouleurEntiteType couleur = couleurs.get(i);
			echantillonTypesCouleur.put(couleur.getEchantillonType().getNom(), couleur.getCouleur().getCouleur());
		}

		prodDeriveTypesCouleur = new Hashtable<>();
		couleurs = ManagerLocator.getCouleurEntiteTypeManager()
				.findAllCouleursForProdTypeByBanqueManager(selectedBanque);

		// pour chaque association, on l'ajoute dans l'hashtable
		for (int i = 0; i < couleurs.size(); i++) {
			final CouleurEntiteType couleur = couleurs.get(i);
			prodDeriveTypesCouleur.put(couleur.getProdType().getNom(), couleur.getCouleur().getCouleur());
		}
	}

	public List<Banque> getBanques() {
		return banques;
	}

	public void setBanques(final List<Banque> b) {
		this.banques = b;
	}

	public Banque getSelectedBanque() {
		return selectedBanque;
	}

	public void setSelectedBanque(final Banque selected) {
		this.previousBanque = selectedBanque;
		this.selectedBanque = selected;
	}

	/*
	 * public void onClientInfo$mainWin(ClientInfoEvent event) { int height =
	 * event.getScreenHeight(); Integer mainHeigth = 0; if (height > 650) {
	 * mainHeigth = (height * 80) / 100; } else { mainHeigth = 600; }
	 * 
	 * tabPanelsHeight = mainHeigth - 100; panelHeight = mainHeigth - 140;
	 * listPanelHeight = mainHeigth - 320; }
	 */

	public Integer getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(final Integer screenH) {
		this.screenHeight = screenH;
	}

	public Integer getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(final Integer screenW) {
		this.screenWidth = screenW;
	}

	public List<String> getTabsNames() {
		return tabsNames;
	}

	public void setTabsNames(final List<String> tabsNs) {
		this.tabsNames = tabsNs;
	}

	public Hashtable<String, String> getEchantillonTypesCouleur() {
		return echantillonTypesCouleur;
	}

	public void setEchantillonTypesCouleur(final Hashtable<String, String> eTypesCouleur) {
		this.echantillonTypesCouleur = eTypesCouleur;
	}

	public Hashtable<String, String> getProdDeriveTypesCouleur() {
		return prodDeriveTypesCouleur;
	}

	public void setProdDeriveTypesCouleur(final Hashtable<String, String> pTypesCouleur) {
		this.prodDeriveTypesCouleur = pTypesCouleur;
	}

	// /**
	// * Renvoie la liste des banques accessibles pour l'utilisateur.
	// * @param user
	// * @return
	// */
	// public List<Banque> getAvailableBanques(Utilisateur user) {
	// Set<Banque> availables = new LinkedHashSet<Banque>();
	//
	// // on parcourt les pfs dont l'utilisateur est l'admin
	// Set<Plateforme> pfs = ManagerLocator.getUtilisateurManager()
	// .getPlateformesManager(user);
	// Iterator<Plateforme> it = pfs.iterator();
	// while (it.hasNext()) {
	// // pour chaque pf on récupère ses banques
	// Set<Banque> bks = ManagerLocator.getPlateformeManager()
	// .getBanquesManager(it.next());
	// availables.addAll(bks);
	// }
	//
	// // on parcourt tous les profils de l'utilisateur pour avoir
	// // ses banques
	// availables.addAll(ManagerLocator.getBanqueManager()
	// .findByProfilUtilisateurManager(SessionUtils
	// .getLoggedUser(sessionScope)));
	//
	// return new ArrayList<Banque>(availables);
	// }

	public boolean isScreenUpdated() {
		return screenUpdated;
	}

	public void setScreenUpdated(final boolean screenUp) {
		this.screenUpdated = screenUp;
	}

	public void createCessionMacroComponent(final Tabpanel item) {
		if (!item.hasFellow("winCession")) {
			final Component comp = Executions.createComponents("/zuls/cession/Cession.zul", item, null);
			comp.setId("winCession");
		}
	}

	/**
	 * Méthode qui crée le macro component représentant le contenu d'un panel.
	 * 
	 * @param zul        Adresse du zul du MacroComponent.
	 * @param macroId    Id du MacroComponent.
	 * @param parentItem TabPanel parent.
	 */
	public void createMacroComponent(final String zul, final String macroId, final Tabpanel parentItem) {
		if (!parentItem.hasFellow(macroId)) {
			final Component comp = Executions.createComponents(zul, parentItem, null);
			comp.setId(macroId);
		}
	}

	/**
	 * Méthode qui détruit le macro component représentant le contenu d'un panel.
	 * 
	 * @param parentItem TabPanel parent.
	 */
	public void destroyContentPanel(final Tabpanel parentItem) {
		if (parentItem != null) {
			Components.removeAllChildren(parentItem);
		}
	}

	/**
	 * Cette méthode va placer les droits de l'utilisateur pour la banque
	 * sélectionnée en variables de la session.
	 */
	public void updateDroitsForSelectedBanque(final Banque bk) {

		ConnexionUtils.generateDroitsForSelectedBanque(bk, bk.getPlateforme(), (Utilisateur) sessionScope.get("User"),
				sessionScope);
	}

	/**
	 * Cette méthode créee une hashtable contenant, pour chaque entité, la liste des
	 * types d'operations possibles pour le profil du user.
	 * 
	 * @param profil Profil pour la banque sélectionnée et l'utilisateur.
	 * @return Hashtable contenant les OperationType.
	 */
	public Hashtable<String, List<OperationType>> getOperationsForProfil(final Profil profil) {
		final Hashtable<String, List<OperationType>> operations = new Hashtable<>();

		// droits sur les patients
		operations.put("Patient",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Patient"));

		// droits sur les Prelevements
		operations.put("Prelevement",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Prelevement"));

		// droits sur les Echantillons
		operations.put("Echantillon",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Echantillon"));

		// droits sur les ProdDerives
		operations.put("ProdDerive",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "ProdDerive"));

		// droits sur les Cessions
		operations.put("Cession",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Cession"));

		// droits sur les Stockages
		operations.put("Stockage",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Stockage"));

		// droits sur les Collaborateurs
		operations.put("Collaborateur",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Collaborateur"));

		// droits sur les Requetes
		operations.put("Requete",
				ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Requete"));

		return operations;
	}

	/**
	 * Méthode qui teste si le macrocomponent d'un panel a déja été chargé.
	 * 
	 * @param panelName Nom du Tabpanel.
	 * @param macroName Id du MacroComponent.
	 * @return True si le MacroComponent a été chargé.
	 */
	public boolean isFullfilledComponent(final String panelName, final String macroName) {
		final Tabpanels panels = mainTabbox.getTabpanels();
		final Tabpanel panel = (Tabpanel) panels.getFellow(panelName);

		return panel.hasFellow(macroName);
	}

	public List<String> getAvailableTabsNames() {
		return availableTabsNames;
	}

	public void setAvailableTabsNames(final List<String> availableTabs) {
		this.availableTabsNames = availableTabs;
	}

	/**
	 * Test si une action est réalisable en fonction des droits de l'utilisateur.
	 * 
	 * @param nomEntite    Entite (ex.:ProdDerive).
	 * @param nomOperation Type d'operation du bouton.
	 */

	public boolean getDroitOnAction(final String nomEntite, final String nomOperation) {
		Boolean admin = false;
		if (sessionScope.containsKey("Admin")) {
			admin = (Boolean) sessionScope.get("Admin");
		}

		// si l'utilisateur est admin => bouton cliquable
		if (admin) {
			return true;
		} else {
			// on extrait l'OperationType de la base
			final OperationType opeation = ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager(nomOperation, true).get(0);

			Hashtable<String, List<OperationType>> droits = new Hashtable<>();

			if (sessionScope.containsKey("Droits")) {
				// on extrait les droits de l'utilisateur
				droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

				final List<OperationType> ops = droits.get(nomEntite);
				return ops.contains(opeation);
			} else {
				return false;
			}
		}
	}

	public void onClickEditUser(final Event event) {
		openFicheUtilisateurWindow();
	}

	public void onGetUserUpdated(final Event e) {
		if (e.getData() != null) {

			final Utilisateur user = (Utilisateur) e.getData();

			// Maj de l'utilisateur dans le sessionScope
			sessionScope.remove("User");
			sessionScope.put("User", user);

			final StringBuffer sb = new StringBuffer();
			sb.append(Labels.getLabel("general.logout"));
			if (user != null) {
				sb.append(" ");
				sb.append(user.getLogin());
			}
			// maj affichage login
			// Récupère la window principale
			final Window win = (Window) mainCenterVbox.getFellow("main");
			// Récupère le borderlayout principal contenant toute la
			// fenêtre TK
			final Borderlayout mainBorderLayout = (Borderlayout) win.getFellow("mainBorderLayout");
			final Button logout = (Button) mainBorderLayout.getFellow("northTopBanniere").getFellow("buttonLogout");
			logout.setLabel(sb.toString());
			mainBinder.loadComponent(logout);
		}

	}

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien pour voir recherché
	 * les patients existants lors de la création d'un nouveau prélèvement.
	 * 
	 * @param page    dans laquelle inclure la modale
	 * @param path    Chemin vers la page ayant appelée cette modale.
	 * @param critere Critere de recherche des patients.
	 */
	public void openFicheUtilisateurWindow() {
		if (!isBlockModal()) {

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("ficheUtilisateurModaleWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("fiche.utilisateur.titre"));
			win.setBorder("normal");
			win.setWidth("570px");
			final int height = 470;
			win.setHeight(height + "px");
			win.setClosable(false);

			final Utilisateur user = SessionUtils.getLoggedUser(sessionScope).clone();

			final HtmlMacroComponent ua = populateFicheUtilisateurModal(win, page, user, Path.getPath(self));
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>() {
				@Override
				public void onEvent(final Event event) throws Exception {
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

			try {
				win.onModal();
				setBlockModal(false);

			} catch (final SuspendNotAllowedException e) {
				log.error(e);
			}
		}
	}

	private static HtmlMacroComponent populateFicheUtilisateurModal(final Window win, final Page page,
			final Utilisateur user, final String path) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent) page.getComponentDefinition("ficheUtilisateurModale", false).newInstance(page, null);
		ua.setParent(win);
		ua.setId("openFicheUtilisateurModale");
		ua.applyProperties();
		ua.afterCompose();

		((FicheUtilisateurModale) ua.getFellow("fwinUtilisateurModale")
				.getAttributeOrFellow("fwinUtilisateurModale$composer", true)).init(user, path);

		return ua;
	}

	// Browser history Management
	public void onBookmarkChange$mainWin(final BookmarkEvent event) {
		try {
			final Tabpanel item = (Tabpanel) ((MainTabbox) mainTabbox)
					.getFellow(mainCenterVbox.getDesktop().getBookmark());
			if (item != null) {
				((MainTabbox) mainTabbox).setSelectedPanel(item);
			}
		} catch (final ComponentNotFoundException ex) {
			((MainTabbox) mainTabbox).getSelectedTab().setSelected(true);
		}
	}

	/**
	 * Lance la procédure d'import d'un nouveau patient depuis une appli tiers :
	 * ouverture de la page nouveau prlvt avec le patient intégré à celle-ci.
	 * 
	 * @param patient
	 */
	public void launchImportPatientFromCrfProdedure(Patient patient) {
		Maladie maladie = new Maladie();
		maladie.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladie());
		maladie.setCode(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladieCode());

		// récupération du patient et de sa maladie s'ils existaient déjà
		// doublon patient
		// on regarde si le patient existe deja en base
		if (ManagerLocator.getPatientManager().findDoublonManager(patient)) {
			final List<Patient> liste = ManagerLocator.getPatientManager().findByNomLikeManager(patient.getNom(), true);

			for (int i = 0; i < liste.size(); i++) {
				final Patient p = liste.get(i);
				if (patient.equals(p)) {
					patient = p;
				}
			}

			maladie.setPatient(patient);
			// on regarde si la maladie existe deja en base
			// @since 2.2.3-genno optimisation = recherche la maladie par patient

			if (ManagerLocator.getMaladieManager().findDoublonManager(maladie, patient)) {
				// @since 2.2.3-genno optimisation = recherche la maladie par patient
				final List<Maladie> mals = ManagerLocator.getMaladieManager()
						.findByLibelleAndPatientManager(maladie.getLibelle(), patient);

				for (int i = 0; i < mals.size(); i++) {
					final Maladie m = mals.get(i);
					if (maladie.equals(m)) {
						maladie = m;
					}
				}
			}
		} else {
			maladie.setPatient(patient);
		}

		final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(this, page);

		// si on arrive à récupérer le panel prelevement et son controller
		if (tabController != null) {
			tabController.switchToCreateMode(null);
			tabController.getFicheEdit().injectPatientAndMaladieInFiche(patient, maladie);
			tabController.getFicheEdit().setFocusOnElement();
		}

		sessionScope.remove("patient");
	}

	/**
	 * Affiche la fiche détaillée d'un objet TK annotable passé en paramètre.
	 * 
	 * @param tkObj
	 * @since 2.0.10
	 */
	public void displayTkObjDetails(final TKAnnotableObject tkObj) {
		AbstractObjectTabController tabController = null;
		// TKAnnotableObject tkObj = (TKAnnotableObject) e.getData();
		if (tkObj instanceof Prelevement) {
			tabController = PrelevementController.backToMe(this, page);
		}

		// si on arrive à récupérer le panel prelevement et son controller
		if (tabController != null) {
			tabController.switchToFicheStaticMode(tkObj);
		}
	}

	/**
	 * Affiche une liste d'objects (prelevement) suite à une demande de resources
	 * externe, comme le lien http envoyé à DIAMIC
	 * 
	 * @param tkObjs
	 * @since 2.2.2-diamic
	 */
	public void displayTkObjDetails(final ResourceRequest<?> resReq) {
		if (resReq != null) {
			AbstractObjectTabController tabController = null;
			if (resReq.isPrelevement()) {
				tabController = PrelevementController.backToMe(this, page);
			}

			// si on arrive à récupérer le panel prelevement et son controller
			if (tabController != null) {

				// si un seul objet à afficher
				if (resReq.getTkObjs().size() == 1) {
					tabController.switchToFicheStaticMode(resReq.getTkObjs().get(0));
				} else {
					tabController.displayObjectsListData(resReq.getTkObjs());
				}
			}
		}
	}

	/******************** DROITS CONSULTATION SESSION UTILISATEUR ********/

	private final Hashtable<String, Boolean> droitsConsultation = new Hashtable<>();

	public Hashtable<String, Boolean> getDroitsConsultation() {
		return droitsConsultation;
	}

	public void initDroitsConsultation() {

		droitsConsultation.clear();

		final List<String> eNoms = new ArrayList<>();
		eNoms.add("Patient");
		eNoms.add("Prelevement");
		eNoms.add("Echantillon");
		eNoms.add("ProdDerive");
		eNoms.add("Cession");
		eNoms.add("Collaborateur");
		eNoms.add("Stockage");

		drawConsultationLinks(eNoms);
	}

	public void drawConsultationLinks(final List<String> entites) {
		if (sessionScope.containsKey("Admin") && (Boolean) sessionScope.get("Admin")) {
			for (int i = 0; i < entites.size(); i++) {
				droitsConsultation.put(entites.get(i), true);
			}
		} else {
			// on extrait l'OperationType de la base
			final OperationType operation = ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager("Consultation", true).get(0);

			Hashtable<String, List<OperationType>> droitsSession = new Hashtable<>();

			if (sessionScope.containsKey("Droits")) { // logiquement car SelectBanque
				// on extrait les droits de l'utilisateur
				droitsSession = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");
			}

			for (int i = 0; i < entites.size(); i++) {
				final List<OperationType> ops = droitsSession.get(entites.get(i));
				if (ops != null && ops.contains(operation)) {
					droitsConsultation.put(entites.get(i), true);
				} else {
					droitsConsultation.put(entites.get(i), false);
				}
			}
		}
	}

	public Component getSelfComponent() {
		return self;
	}

	public void onSwitchBanqueFromMaladiePrelClick(final Event event) {
		displayTkObjDetails((Prelevement) event.getData());
		Clients.clearBusy();
	}

	/**** since 2.1 2D full rack barcode scanne *******/

	public void onSwitchScanButton() {
		if (scanTimer != null) {
			if (!scanTimer.isRunning()) { // start
				scanTimer.start();
			} else {
				scanTimer.stop();
			}
			switchScanButtonDivSclass();
		}
	}

	private void switchScanButtonDivSclass() {
		if (scanTimer.isRunning()) {
			scanButtonDiv.setSclass("scanSwitch on");
			scanButtonDiv.setTooltiptext(Labels.getLabel("scan.button.swith.off"));
		} else {
			scanButtonDiv.setSclass("scanSwitch off");
			scanButtonDiv.setTooltiptext(Labels.getLabel("scan.button.swith.on"));
		}
	}

	public void onScanTimer() {
		if (scanTimer.isRunning()) {
			// send scan to be handled by selected capable tab
			final AbstractObjectTabController controller = getSelectedTabController();
			if (controller != null) {
				final ListModelList<ScanTerminale> scansModel = new ListModelList<>(
						ManagerLocator.getScanTerminaleManager().findByDeviceManager(null));
				if (!scansModel.isEmpty()) {
					scanTimer.stop();
					switchScanButtonDivSclass();

					// modale si plusieurs scans en attente
					if (scansModel.size() > 1) {
						final Map<String, Object> args = new HashMap<>();
						args.put("device", null);
						args.put("scans", scansModel);
						args.put("main", self);
						Executions.createComponents("/zuls/interfacage/scan/SelectScanModale.zul", null, args);
					} else if (scansModel.size() > 0) {
						final ScanTerminale lastScan = scansModel.get(0);
						controller.handleScanTerminale(lastScan);
						ManagerLocator.getScanTerminaleManager().removeObjectManager(lastScan);
					}
				}
			}
		}
	}

	public void onSelectScan(final Event ev) {
		ScanTerminale scan = null;
		if (ev != null) {
			scan = (ScanTerminale) ev.getData();
		}
		final AbstractObjectTabController controller = getSelectedTabController();

		if (scan != null && controller != null) {
			controller.handleScanTerminale(scan);
		}
	}

	public void onSelectScanAndDelete(final Event ev) {
		if (ev != null) {
			onSelectScan(ev);
		}
		ManagerLocator.getScanTerminaleManager().removeObjectManager((ScanTerminale) ev.getData());
	}

	/**** since 2.2.3-genno dossier externjes multiple integration *******/

	public boolean getGenno() {
		return findGennoEmetteurIfAny() != null;
	}

	private Emetteur findGennoEmetteurIfAny() {
		return SessionUtils.getEmetteursInterfacages(Sessions.getCurrent().getAttributes()).stream()
				.filter(e -> e.getIdentification().equalsIgnoreCase("Genno")).findFirst().orElse(null);
	}

	public void onOpenDossierExternes() {
		final Map<String, Object> args = new HashMap<>();
		args.put("emetteur", findGennoEmetteurIfAny());
		args.put("mainWindow", this);
		Executions.createComponents("/zuls/interfacage/SelectDossierExterneToSaveModale.zul", null, args);
	}

}