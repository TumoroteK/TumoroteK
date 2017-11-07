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
package fr.aphp.tumorotek.action.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.echantillon.ListeEchantillon;
import fr.aphp.tumorotek.action.prodderive.ListeProdDerive;
import fr.aphp.tumorotek.action.recherche.ExportModale;
import fr.aphp.tumorotek.action.recherche.FicheRechercheAvancee;
import fr.aphp.tumorotek.action.recherche.FicheRechercheAvanceeCession;
import fr.aphp.tumorotek.action.recherche.FicheRechercheINCa;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.general.export.Export;

public abstract class AbstractListeController2 extends AbstractController {

	private static final long serialVersionUID = -7175263022919263339L;
	
	// Row actuellement sélectionnée.
	private Row currentRow;

	protected Panel listPanel;
	protected Panelchildren scrollPan;
	protected Grid objectsListGrid;
	protected Checkbox checkAll;
	protected Menuitem modificationItem;
	protected Menuitem exportItem;
	protected Menuitem exportItemAdv;
	protected Menuitem etiquetteItem;
	protected Menuitem changeCollectionItem;
	protected Menuitem retourItem;
	protected Menuitem deleteItem;
	protected Menuitem incompRetoursItem;
	
	protected Button find;
	protected Button addNew;
	protected Button select;
	protected Button cancelSelection;
	protected Menubar menuBar;
	
	protected Menuitem patientsItem;
	protected Menuitem prelevementsItem;
	protected Menuitem echantillonsItem;
	protected Menuitem derivesItem;
	protected Menuitem derivesAscItem;
	protected Menuitem cessionsItem;

	// citere date creation
	protected Radio dateCreation;
	protected Listbox dateCreationBox;
	
	private Calendar searchDateCreation;
	private String selectedDate;
	
	private AbstractObjectTabController objectTabController;

	private String path;
	private String mode = "liste";
	private String onGetEventName;

	private final Integer nbLastObjs = 30;

	// Variable de droits.
	private boolean canModifMultiple;
	private boolean canExport;
	private boolean canNew;
	private boolean canEtiquette;
	private boolean isAdmin;
	private boolean canDelete;

	private TKdataObject currentObject;

	private List<Integer> resultatsIds = new ArrayList<Integer>();
	private List<Integer> restrictedTableIds = new ArrayList<Integer>();

	public TKdataObject getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(TKdataObject o) {
		this.currentObject = o;
	}

	public Row getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(Row cRow) {
		this.currentRow = cRow;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String p) {
		this.path = p;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String m) {
		this.mode = m;
	}

	public Integer getNbLastObjs() {
		return nbLastObjs;
	}

	public String getOnGetEventName() {
		return onGetEventName;
	}

	public void setOnGetEventName(String onName) {
		this.onGetEventName = onName;
	}

	public AbstractObjectTabController getObjectTabController() {
		return objectTabController;
	}

	public void setObjectTabController(AbstractObjectTabController otc) {
		this.objectTabController = otc;
		
		// if (getExportItemAdv() != null) {
		//	getExportItemAdv().setVisible(otc.hasAnnoTables());
		//}
	}

	/**
	 * Calcule la date de référence (en fonction de selection de la liste de
	 * String creationDates) à passer à la méthode pour qui cherche les objets
	 * en fonction de leur date de création.
	 * 
	 * @return Date de référence
	 */
	public Calendar getSearchDateCreation() {
		int ind = dateCreationBox.getSelectedIndex();
		// on récupère la date du jour
		Calendar today = Calendar.getInstance();
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		// en fonction de la sélection, on enlève des jours
		if (ind <= 0) {
			today.add(Calendar.DAY_OF_YEAR, -1);
		} else if (ind == 1) {
			today.add(Calendar.DAY_OF_YEAR, -10);
		} else if (ind == 2) {
			today.add(Calendar.DAY_OF_YEAR, -30);
		}
		searchDateCreation = today;
		/*
		 * try { searchDateCreation = new
		 * SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		 * .parse(sdf.format(today.getTime())); } catch (ParseException e) {
		 * log.error(e); }
		 */
		return searchDateCreation;
	}

	public void setSearchDateCreation(Calendar search) {
		this.searchDateCreation = search;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selected) {
		this.selectedDate = selected;
	}

	public Grid getObjectsListGrid() {
		return objectsListGrid;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);

		// recoit le renderer en argument
		if (arg != null && arg.containsKey("renderer")) {
			setListObjectsRenderer((TKSelectObjectRenderer) arg.get("renderer"));
		}
		
		// reference liste selectedObjects dans le renderer
		if (getListObjectsRenderer() != null) {
			getListObjectsRenderer().setSelectedObjects(getSelectedObjects());
		}

		// IE8 hack
		if (Executions.getCurrent().getBrowser("ie") != null 
				&& Executions.getCurrent().getBrowser("ie") <= 8 && scrollPan != null) {
			scrollPan.setStyle("overflow-y: auto;");
		}

		setBinder(new AnnotateDataBinder(comp));
		getBinder().loadAll();
		if (listPanel != null) {
			listPanel.setHeight(getMainWindow().getListPanelHeight() + "px");
		}

		// applique le scroll que si l'écran est trop petit
		// 340 px = taille demandée pour afficher page de grid 10 objets
		/*
		 * if (Integer.valueOf(getMainWindow() .getListPanelHeight()) < 340 &&
		 * objectsListGrid != null) {
		 * objectsListGrid.setStyle("overflow-y:scroll"); }
		 */

		// applyDroitsOnListe();
		if (sessionScope.containsKey("Anonyme")
				&& (Boolean) sessionScope.get("Anonyme")) {
			setAnonyme(true);
		} else {
			setAnonyme(false);
		}
		
		// applique l'affichage de la Banque si Toutes collections
		if (getListObjectsRenderer() != null) {
			getListObjectsRenderer().setTtesCollections(SessionUtils
						.getSelectedBanques(sessionScope).size() > 1);
		}

		initObjectsBox();
	}

	/**
	 * Passe le composant en mode de liste.
	 */
	public void switchToListMode() {
		setMode("liste");
		clearSelection();
		this.find.setVisible(true);
		this.addNew.setVisible(true);
		this.select.setVisible(false);
		this.cancelSelection.setVisible(false);
		this.menuBar.setVisible(true);
	}

	/**
	 * Passe le composant en mode de sélection.
	 */
	public void switchToSelectMode() {
		setMode("select");
		clearSelection();
		this.find.setVisible(true);
		this.addNew.setVisible(false);
		this.select.setVisible(true);
		this.cancelSelection.setVisible(true);
		this.menuBar.setVisible(false);		
	}

	/**
	 * Gèle ou dégèle les boutons de la toolbar.
	 * 
	 * @param b
	 */
	public void disableToolBar(boolean b) {
		disableObjetsSelectionItems(b);
		if (addNew != null) {
			addNew.setDisabled(b || !isCanNew());
		}
	}

	/**
	 * Compose et renvoie la liste affichables de dates par défaut utilisables
	 * comme critère de recherche dans la sélection.
	 * 
	 * @return String[] liste de dates proposées
	 */
	public String[] getCreationDates() {
		String[] dates = new String[] {
				Labels.getLabel("general.date.depuis1jour"),
				Labels.getLabel("general.date.depuis10jour"),
				Labels.getLabel("general.date.depuis30jour"),
				Labels.getLabel("general.30.derniers") };
		return dates;
	}

	/**
	 * Cette méthode retourne le numéro de la page sur laquelle se trouve
	 * l'objet passé en paramètre.
	 * 
	 * @param obj
	 *            Objet pour lequel on recherche une page.
	 * @return Numéro de la page.
	 */
	public int getPageNumberForObject(Object obj) {
		int ind = getListObjects().indexOf(obj);
		int page = 0;

		if (ind > -1) {
			page = ind / objectsListGrid.getPageSize();
		}

		return page;
	}

	/**
	 * Cette méthode déselectionne l'objet courant et sélectionne celui passé en
	 * paramètre.
	 * 
	 * @param newCurrent
	 *            Objet que l'on souhaite sélectionner.
	 */
	public void changeCurrentObject(TKdataObject newCurrent) {

		// si la liste contient l'objet
		if (getListObjects().contains(newCurrent)) {
			deselectRow();

			// this.currentObject = newCurrent;
			int ind = getListObjects().indexOf(newCurrent);
			
			objectsListGrid.getPaginal().setActivePage(
					getPageNumberForObject(currentObject));

			Rows rows = objectsListGrid.getRows();
			List<Component> comps = rows.getChildren();
			// this.currentRow = (Row) comps.get(ind);
			// this.currentRow.setStyle("background-color : #b3c8e8");
			
			// Bug Romain Casey 20/06/2014 getListObjects et Rows 
			// ne sont pas dans le même état? Pas reproduit...
			// suppose que aucune Rows dessinées alors que ListObjects 
			// n'est pas vide
			if (comps.size() < ind) {
				getBinder().loadAttribute(objectsListGrid, "model");
				objectsListGrid.getPaginal().setActivePage(
						getPageNumberForObject(currentObject));
			}
			
			
			selectRow((Row) comps.get(ind), newCurrent);

		}
	}

	/**
	 * Déselectionne la ligne actuellement sélectionnée.
	 */
	public void deselectRow() {
		// on vérifie qu'une ligne est bien sélectionnée
		if (getCurrentObject() != null && getCurrentRow() != null) {
			int ind = getListObjects().indexOf(currentObject);
			// on lui spécifie une couleur en fonction de son
			// numéro de ligne
			if (ind > -1) {
				getCurrentRow().setStyle("background-color : #e2e9fe");
				/*
				 * if (ind % 2 == 0) {
				 * currentRow.setStyle("background-color : #FFFFFF"); } else {
				 * currentRow.setStyle("background-color : #e2e9fe"); }
				 */
				setCurrentRow(null);
				setCurrentObject(null);
			}
		}
	}

	/**
	 * Sélectionne la ligne passée en paramètre.
	 * 
	 * @param row
	 *            Row à sélectionner.
	 * @param obj
	 *            Objet se trouvant dans la ligne
	 */
	public void selectRow(Row row, TKdataObject obj) {

		setCurrentRow(row);
		setCurrentObject(obj);

		getCurrentRow().setStyle("background-color : #b3c8e8");
	}

	/**
	 * Méthode appelée lors du check de la checkbox des colonnes : sélectionne
	 * ou déselctionne tous les éléments.
	 */
	public void onCheck$checkAll() {
		if (!checkAll.isChecked()) {
			clearSelection();
		} else {
			getListObjectsRenderer().setCheckAll(true);
			checkOrNotAllElements(true);
			passListToSelected();
			disableObjetsSelectionItems(false);
		}
	}

	/**
	 * Déselectionne tous les objets de la grid.
	 */
	public void clearSelection() {
		if (getSelectedObjects() != null) {
			getSelectedObjects().clear();
			// getResultatsIds().clear();
			
			disableObjetsSelectionItems(true);

			getListObjectsRenderer().setCheckAll(false);
			checkAll.setChecked(false);
			checkOrNotAllElements(false);
		}
	}

	/**
	 * Check ou non tous les éléments de la liste.
	 * 
	 * @param check
	 *            Si true, coche toutes les checkboxs.
	 */
	public void checkOrNotAllElements(boolean check) {
		Rows rows = objectsListGrid.getRows();
		List<Component> allRow = rows.getChildren();
		for (int i = 0; i < allRow.size(); i++) {
			List<Component> comps = allRow.get(i).getChildren();
			int j = 0;
			while (j < comps.size()
					&& !comps.get(j).getClass().getSimpleName()
							.equals("Checkbox")) {
				j++;
			}

			if (j < comps.size()
					&& comps.get(j).getClass().getSimpleName()
							.equals("Checkbox")) {
				Checkbox box = (Checkbox) comps.get(j);
				box.setChecked(check);
			}
		}
	}

	/**
	 * Clique sur la checkbox d'un objet.
	 * 
	 * @param event
	 */
	public void onCheckObject(ForwardEvent event) {
		// on récupère la checkbox associé à l'event
		Checkbox box = (Checkbox) event.getOrigin().getTarget();
		// on récupère l'Echantillon associé à l'event
		TKdataObject obj = (TKdataObject) event.getData();

		if (box.isChecked()) {
			addToSelectedObjects(obj);
		} else {
			removeFromSelectedObjects(obj);
		}

		// les actions ne sont disponibles que si des objets sont
		// sélectionnés
		disableObjetsSelectionItems(false);
	}

	/**
	 * Inactive/active les composants de manipulation des sélections multiples
	 * venant de la liste en fonction des droits sur les opérations.
	 */
	public void disableObjetsSelectionItems(boolean disable) {
		if (modificationItem != null) {
			modificationItem.setDisabled(disable || !isCanModifMultiple()
					|| getSelectedObjects().size() < 2);
		}
		if (exportItem != null) {
			exportItem.setDisabled(disable || !isCanExport()
					|| getSelectedObjects().size() < 1);
		}
		if (exportItemAdv != null && exportItemAdv.isVisible()) {
			exportItemAdv.setDisabled(disable || !isCanExport()
					|| getSelectedObjects().size() < 1);
		}
		if (changeCollectionItem != null) {
			changeCollectionItem.setDisabled(disable || !isCanExport()
					|| getSelectedObjects().size() < 1);
		}
		if (etiquetteItem != null) {
			etiquetteItem.setDisabled(disable || !isCanEtiquette()
					|| getSelectedObjects().size() < 1);
		}
		if (retourItem != null && retourItem.isVisible()) {
			retourItem.setDisabled(disable || getSelectedObjects().size() < 1);
		}
		if (deleteItem != null && deleteItem.isVisible()) {
			deleteItem.setDisabled(disable || !isCanDelete() 
					|| getSelectedObjects().size() < 1);
		}
		disableObjectTreeButtons(disable);
	}

//	/**
//	 * Vérifie que tous les objets sont ENCOURS.
//	 * Affiche le bouton permettant la complétion des Evènements
//	 * @return true/false
//	 */
//	private boolean allSelectedEncours() {
//
//		if (getSelectedObjects().isEmpty()) {
//			return false;
//		} else {
//			for (Object obj : getSelectedObjects()) {
//				if (!((TKStockableObject) obj)
//					.getObjetStatut().getStatut().equals("ENCOURS")) {
//					return false;
//				}
//			}
//		}
//		
//		return true;
//	}

	/**
	 * Forwarded Event. Sélectionne la ligne concernée par l'event et affiche
	 * les infos de l'objet contenu dans celle-ci.
	 * 
	 * @param event
	 *            forwardé depuis le lable nom cliquable (event.getData contient
	 *            l'objet).
	 */
	public void onClickObject(Event event) {
		if (!getMode().equals("select")) {
			// déselection de la ligne courante
			deselectRow();

			selectRowAndDisplayObject(getRow((ForwardEvent) event),
					(TKdataObject) event.getData());
		}
	}

	/**
	 * Selectionne l'objet dans la liste et affiche le contenu de l'objet
	 * selectionné dans la fiche statique.
	 * 
	 * @param row
	 * @param data
	 *            Objet à afficher
	 */
	public void selectRowAndDisplayObject(Row row, TKdataObject obj) {
		deselectRow();
		// sélection de la nouvelle ligne
		selectRow(row, obj);

		// on envoie l'objet à la fiche
		TKdataObject edit = ((TKdataObject) currentObject).clone();

		if (getObjectTabController().isStaticEditMode()) {
			getObjectTabController().getFicheStatic().setObject(edit);
			// on envoie le patient à la fiche annotation
			if (getObjectTabController().getFicheAnnotation() != null) {
				getObjectTabController().getFicheAnnotation().setObj(
						(TKAnnotableObject) edit);
			}
		} else { // fiche combine
			getObjectTabController().getFicheCombine().setObject(edit);
			getObjectTabController().getFicheCombine().switchToStaticMode();
		}

		// on passe en mode fiche & liste
		getObjectTabController().switchToFicheAndListeMode();
	}

	/**
	 * Selectionne le premier objet de la liste par défaut.
	 */
	public void selectFirstObjet() {
		if (getListObjects().size() > 0) {
			selectRowAndDisplayObject((Row) objectsListGrid.getRows()
					.getFirstChild(), getListObjects().get(0));
		}
	}

	/**
	 * Selectionne dans la liste la row correspondant à l'objet passé en
	 * paramètre.
	 * 
	 * @param obj
	 */
	public void selectlastRow(TKdataObject obj) {
		Iterator<Component> rowsIt = objectsListGrid.getRows().getChildren()
				.iterator();

		Row row = null;
		while (rowsIt.hasNext()) {
			row = (Row) rowsIt.next();
		}
		selectRowAndDisplayObject(row, obj);
	}

	/**
	 * Passe la fiche associee en formulaire de création.
	 * 
	 * @param event
	 *            Event : clique du bouton addNewEchantillon.
	 * @throws Exception
	 */
	public void onClick$addNew(Event event) throws Exception {
		getObjectTabController().switchToCreateMode(null);
	}

	/**
	 * Méthode appelée lorsque l'utilisateur utiliser la touche ENTREE sur l'un
	 * des éléments du formulaire de recherche : redirection vers la méthode de
	 * recherche onClick$find().
	 */
	public void onPressEnterKey() {
		onClick$find();
	}

	/**
	 * Méthode appelée après lors du focus sur le champ dateCreationBoxPatient.
	 * Le radiobutton correspondant sera automatiquement sélectionné.
	 */
	public void onSelect$dateCreationBox() {
		dateCreation.setChecked(true);
	}

	/**
	 * Recherche la liste des échantillons à afficher.
	 * 
	 * @param event
	 *            Event : clique du bouton find.
	 * @throws Exception
	 */
	public void onClick$find() {
		clearSelection();
		if (dateCreation.isChecked() && dateCreationBox.getSelectedIndex() == 3) {
			setListObjects(extractLastObjectsCreated());
		} else {
			setResultatsIds(doFindObjects());
			if (getResultatsIds().size() > 500) {
				openResultatsWindow(page, getResultatsIds(), self, 
									getEntiteNom(), getObjectTabController());
			} else if (getResultatsIds().size() > 0) {
				// setListObjects(extractObjectsFromIds(resultatsIds));
				onShowResults();
			} else {
				// setListObjects(new ArrayList<Object>());
				onShowResults();
				Messagebox.show(
						Labels.getLabel("recherche.avancee.no.results"),
						Labels.getLabel("recherche.avancee.no.results.title"),
						Messagebox.OK, Messagebox.INFORMATION);
			}
		}
	}

	/**
	 * Gère l'affichage des résultats obtenus par une recherche sur une liste de
	 * critères.
	 * 
	 * @param res
	 *            Liste des ids des objets à afficher.
	 */
	public void showResultsAfterSearchByList(List<Integer> res) {
		clearSelection();
		setResultatsIds(res);
		if (res.size() > 500) {
			openResultatsWindow(page, res, self, getEntiteNom(), 
											getObjectTabController());
		} else if (res.size() > 0) {
			onShowResults();
		} else {
			onShowResults();
			Messagebox.show(Labels.getLabel("recherche.avancee.no.results"),
					Labels.getLabel("recherche.avancee.no.results.title"),
					Messagebox.OK, Messagebox.INFORMATION);
		}
		setCurrentRow(null);
		setCurrentObject(null);
		getObjectTabController().clearStaticFiche();
		getObjectTabController().switchToOnlyListeMode();
	}

	public void onShowResults() {
		List<Integer> ids = new ArrayList<Integer>();
		if (getResultatsIds().size() > 500) {
			Collections.reverse(getResultatsIds());
			ids = getResultatsIds().subList(0, 500);
		} else {
			ids = getResultatsIds();
		}
		
		clearSelection();
		setListObjects(extractObjectsFromIds(ids));
	
		setCurrentRow(null);
		setCurrentObject(null);
		getObjectTabController().clearStaticFiche();
		getObjectTabController().switchToOnlyListeMode();
		objectsListGrid.setActivePage(0);
		getBinder().loadComponent(objectsListGrid);
		
		updateListResultsLabel(ids.size());
	}
	
	/**
	 * Evenement relayant l'envoi vers une nouvelle cession 
	 * d'un trop grand nombre de résultats (envoyé depuis ResultatsModale)
	 */
	public void onDoNewCession(Event e) {
		Clients.showBusy(Labels.getLabel("cession.select.wait"));
		Events.echoEvent("onLaterNewCession", self, null);
	}
	
	public void onLaterNewCession() {
		setResultatsIds(getResultatsIds());

		onNewCessionFromResultatModale();
		
		Clients.clearBusy();
	}

	
	/**
	 * Méthode appelée par la fenêtre FicheRechercheAvancee quand l'utilisateur
	 * fait une recherche.
	 * 
	 * @param Event contenant les résultats de la recherche.
	 */
	@SuppressWarnings("unchecked")
	public void onGetObjectFromResearch(Event e) {

		// si des patients sont renvoyés
		if (e.getData() != null) {
			setListObjects(new ArrayList<TKAnnotableObject>());
			clearSelection();
			setListObjects((List<? extends TKdataObject>) e.getData());
			setCurrentRow(null);
			setCurrentObject(null);
			getObjectTabController().clearStaticFiche();
			getObjectTabController().switchToOnlyListeMode();
			getBinder().loadComponent(objectsListGrid);
			
			updateListResultsLabel(getListObjects().size());
		}
		
	}
	
	/**
	 * Clique sur le bouton exportItem
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void onClick$exportItem() {
		onLaterExport(true);
	}
	
	/**
	 * Export affichant la modale permettant la restriction 
	 * des tables d'annotations concernées par l'export.
	 * @since 2.0.10
	 */
	public void onClick$exportItemAdv() {
		openRestrictTablesModale(this, null, 
				getObjectTabController().getEntiteTab());
	}

	/**
	 * Evenement relayant l'export d'un trop grand nombre de 
	 * résultats (envoyé depuis ResultatsModale)
	 * @version 2.1
	 */
	public void onDoExport(Event e) {
		if (e.getData() == null) {
			onLaterExport(false);
		} else if (e.getData().equals("TVGSO")) {
			onDoExportTVGSO(false);
		} else if (e.getData().equals("INCa")) {
			onDoExportINCa();
		} else if (e.getData().equals("BIOCAP")) {
			onDoExportBIOCAP();
		} else if (e.getData().equals("BIOBANQUES")) { //@since 2.1
			onDoExportBIOBANQUES();
		}
	}

	public void onDoExportTVGSO(boolean csv) {		
	}
	
	public void onDoExportINCa() {		
	}
	
	public void onDoExportBIOCAP() {		
	}
	
	public void onDoExportBIOBANQUES() {		
	}

	/**
	 * Lance l'export à partir d'une selection ou d'une recherche 
	 * vide
	 * @param fromSelection
	 */
	@SuppressWarnings("unchecked")
	public void onLaterExport(boolean fromSelection) {	
		if (fromSelection) {
			getResultatsIds().clear();
			extractIdsFromList((List<TKdataObject>) getSelectedObjects(), 
					getResultatsIds());
		}
		try {	
			// ajoute toutes les tables disponibles aucune restriction
			if (getRestrictedTableIds() != null) {
				if (getRestrictedTableIds().isEmpty()) {
					for (TableAnnotation tb : ManagerLocator
							.getTableAnnotationManager()
							.findByBanquesManager(SessionUtils
									.getSelectedBanques(sessionScope), true)) {
						getRestrictedTableIds().add(tb.getTableAnnotationId());
					}
				}
			} else {
				setRestrictedTableIds(new ArrayList<Integer>());
			}
			
			Class<?> exportThread = (Class<?>) Class.forName(SessionUtils
					.getDatabasePathClass());
			Constructor<?> constr = exportThread.getConstructor(Desktop.class,
					int.class, List.class, List.class, boolean.class, short.class,
					Utilisateur.class, List.class, HtmlMacroComponent.class, Map.class);
			Object o = constr.newInstance(desktop, getObjectTabController()
					.getEntiteTab().getEntiteId(), getResultatsIds(), SessionUtils
					.getSelectedBanques(sessionScope), isAnonyme(),
					ConfigManager.DEFAULT_EXPORT, SessionUtils
							.getLoggedUser(sessionScope), 
					getRestrictedTableIds(),
					callProgressBar(), null);
			Method method = exportThread.getMethod("start");
			
			// put into session
			if (!desktop.hasAttribute("threads")) {
				desktop.setAttribute("threads", new ArrayList<Export>());
			}
			((List<Export>) desktop
					.getAttribute("threads")).add((Export) o);
			
			method.invoke(o);
			
			getRestrictedTableIds().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ajoute un objet (venant du formulaire de creation) a la liste Cet objet
	 * devient l'objet courant.
	 * 
	 * @param objet
	 */
	public void addToObjectList(Object newObj) {
		if (getSelectedObjects() != null) {
			clearSelection();
		}
		// L'objet inséré est un clone de celui du formulaire
		// afin d'éviter des effets de bord lors de la modif
		// du formulaire
		TKdataObject objClone = ((TKdataObject) newObj).clone();
		addToListObjects(objClone, 0);

		// déselection de la liste courante
		deselectRow();

		// on affiche la page sur laquelle se trouve l'objet
		objectsListGrid.getPaginal().setActivePage(
				getPageNumberForObject(currentObject));

		// ListModel list = new SimpleListModel(getListObjects());
		// objectsListGrid.setModel(list);
		//
		// Row row = new Row();
		// row.setValue(objClone);
		// row.setParent(objectsListGrid.getRows());

		// update de la liste
		getBinder().loadAttribute(objectsListGrid, "model");
		
		updateListResultsLabel(getListObjects().size());
	}

	/**
	 * Ajoute une liste d'objet (venant du formulaire de creation) a la liste.
	 * 
	 * @param objet
	 */
	public void addListToObjectList(List<Object> newObjs) {
		if (getSelectedObjects() != null) {
			clearSelection();
		}

		for (int i = 0; i < newObjs.size(); i++) {
			// L'objet inséré est un clone de celui du formulaire
			// afin d'éviter des effets de bord lors de la modif
			// du formulaire
			TKdataObject objClone = ((TKdataObject) newObjs.get(i))
					.clone();
			addToListObjects(objClone, 0);
		}

		// déselection de la liste courante
		deselectRow();

		// update de la liste
		getBinder().loadAttribute(objectsListGrid, "model");

		// on affiche la page sur laquelle se trouve l'objet
		objectsListGrid.getPaginal().setActivePage(
				getPageNumberForObject(currentObject));
		
		updateListResultsLabel(getListObjects().size());
	}

	/**
	 * Supprime un objet a la liste.
	 * 
	 * @param objet
	 */
	public void removeObjectAndUpdateList(TKdataObject obj) {
		if (getSelectedObjects() != null) {
			clearSelection();
		}
		// on déselectionne la ligne courante
		deselectRow();
		// suppression de la liste
		removeObjectFromList(obj);

		// update de la grille
		getBinder().loadAttribute(objectsListGrid, "model");
		
		updateListResultsLabel(getListObjects().size());

	}
	
	/**
	 * Supprime une liste d'objets a la liste d'affichage.
	 * 
	 * @param objet
	 */
	public void removeObjectsListAndUpdateList(List<? extends TKdataObject> objs) {
	}

	/**
	 * Met à jour la liste des objets.
	 */
	public void refreshListe() {
		getBinder().loadAttribute(objectsListGrid, "model");	
		updateListResultsLabel(getListObjects().size());
		getObjectTabController().clearStaticFiche();
	}
	
	/**
	 * Met à jour la liste des objets.
	 */
	public void refreshListe2() {
		getBinder().loadAttribute(objectsListGrid, "model");	
		updateListResultsLabel(getListObjects().size());
	//	getObjectTabController().clearStaticFiche();
		if (getObjectTabController().getFicheStatic() != null) {	
			getObjectTabController().getFicheStatic().reloadObject();
		}
	}

	/**
	 * Met à jour le contenu de la liste avec les éléments passés en paramètre.
	 * 
	 * @param objets
	 *            à mettre dans la liste.
	 */
	public void updateListContent(List<? extends TKdataObject> objs) {
		clearSelection();
		setListObjects(objs);
		setCurrentRow(null);
		setCurrentObject(null);

		getObjectTabController().clearStaticFiche();
		// update de la grille
		getBinder().loadAttribute(objectsListGrid, "model");
		clearSelection();
	}
	
	public void updateMultiObjectsGridListInPlace(List<? extends TKdataObject> objs) {
		for (Object obj : objs) {
			TKdataObject edit = ((TKdataObject) obj).clone();

			// si la liste contient l'objet updaté
			if (getListObjects().contains(edit)) {

				// déselection de la liste courante
				deselectRow();

				// on récupère l'objet et on le met à jour par
				// suppression/insertion dans la liste
				int ind = getListObjects().indexOf(edit);
				getListObjects().remove(ind);
				addToListObjects(edit, new Integer(ind));
			}
		}
		// maj de la grille
		getBinder().loadAttribute(objectsListGrid, "model");
		
		updateListResultsLabel(objs.size());
	}

	/**
	 * Mets à jour l'objet sélectionné de la liste.
	 * 
	 * @param objet
	 *            Objet à mettre à jour.
	 */
	public void updateObjectGridList(Object obj) {
		// l'objet passé en paramètre est cloné
		TKdataObject edit = ((TKdataObject) obj).clone();

		// on vérifie que la liste a bien un objet sélectionné
		if (this.currentObject != null) {
			// si l'objet édité est dans la liste, il est forcément
			// sélectionné.
			// On vérifie donc que l'objet sélectionné a le meme id
			// que celui édité
			Integer idSelected = ((TKdataObject) this.currentObject)
					.listableObjectId();
			Integer idUpdated = edit.listableObjectId();
			if (idSelected.equals(idUpdated)) {
				int ind = getListObjects().indexOf(this.currentObject);
				// si c'est le cas, maj de la liste par
				// suppression/insertion
				if (ind > -1) {
					getListObjects().remove(ind);
					addToListObjects(edit, new Integer(ind));

					getBinder().loadAttribute(objectsListGrid, "model");

					// on re-sélctionne la liste contenant l'obj
					Rows rows = objectsListGrid.getRows();
					List<Component> comps = rows.getChildren();
					selectRow((Row) comps.get(ind), edit);
				}
			}
		}
	}

	/**
	 * Mets à jour la liste après un update depuis une autre page et selectionne
	 * l'objet mis à jour.
	 * 
	 * @param objet Objet a mettre a jour.
	 * @param boolean si selection de l'objet dans la liste
	 * @return true si liste updated.
	 */
	public boolean updateObjectGridListFromOtherPage(Object obj, boolean select) {

		boolean updated = false;

		// l'objet passé en paramètre est cloné
		TKdataObject edit = ((TKdataObject) obj).clone();

		// si la liste contient l'objet updaté
		if (getListObjects().contains(edit)) {

			// déselection de la liste courante
			deselectRow();

			// on récupère l'objet et on le met à jour par
			// suppression/insertion dans la liste
			int ind = getListObjects().indexOf(edit);
			getListObjects().remove(ind);
			addToListObjects(edit, new Integer(ind));

			// maj de la grille
			getBinder().loadAttribute(objectsListGrid, "model");
			
			if (select) {
				// on récupère toutes les lignes de la grille et on
				// sélectionne celle qui contient l'obj updaté
				Rows rows = objectsListGrid.getRows();
				List<Component> comps = rows.getChildren();
				selectRow((Row) comps.get(ind), edit);
	
				// on affiche la page contenant l'objet
				objectsListGrid.getPaginal().setActivePage(
						getPageNumberForObject(currentObject));
	
				// on passe l'objet à la fiche
				getObjectTabController().getFicheStatic().setObject(edit);
			}
			updated = true;
		}
		return updated;
	}

	/**
	 * Mets à jour la liste après un update multiple de plusieurs objets.
	 * Recharge la liste directement
	 * 
	 * @param objects
	 *            Liste d'objets a mettre a jour.
	 */
	public void updateMultiObjectsGridListFromOtherPage(List<TKdataObject> objects) {
		if (objects != null && !objects.isEmpty()) {
			clearList();
			// setListObjects(objects);
			// refreshListe();
			List<Integer> ids = new ArrayList<Integer>();
			extractIdsFromList(objects, ids);
			updateGridByIds(ids, false, true);
		}
	}

	/**
	 * Mets à jour la liste avec une liste d'objets suite à la modification du
	 * parent de ces objets.
	 * 
	 * @param objects
	 *            Liste d'objets a mettre a jour.
	 * @param isDelete
	 *            true si l'operation est une deletion.
	 * @return enfant selectionné avant update, null si aucun
	 */
	public TKdataObject updateGridListChildrenObjectsFromOtherPage(
			List<? extends TKdataObject> objects, boolean isDelete) {
		Iterator<? extends TKdataObject> it = objects.iterator();
		TKdataObject next;
		TKdataObject selected = null;
		while (it.hasNext()) {
			next = it.next();

			int ind = getListObjects().indexOf(next);
			// si c'est le cas, maj de la liste par
			// suppression/insertion
			if (ind > -1) {
				getListObjects().remove(ind);
				if (!isDelete) {
					addToListObjects(next, new Integer(ind));
				}
				// si une enfant etait selectionné
				if (next.equals(currentObject)) {
					selected = next;
				}

			}
		}
		refreshListe();

		return selected;
	}
	
	/**
	 * Mets à jour la liste avec une liste d'objets suite à la modification du
	 * parent de ces objets.
	 * @param objects Liste d'objets a mettre a jour.
	 * @param isDelete true si l'operation est une deletion.
	 * @param met à jour le composant si true
	 * @return enfant selectionné avant update, null si aucun
	 */
	@SuppressWarnings("unchecked")
	public void updateGridByIds(List<Integer> objectIds, boolean isDelete, 
												boolean updateListeComposant) {
		
		List<Integer> listObjsIds = new ArrayList<Integer>();
		
		// si la liste n'est pas vide
		// mise à jour des objets
		if (!getListObjects().isEmpty()) {
		
			extractIdsFromList((List<TKdataObject>) getListObjects(), listObjsIds);
				
			for (Integer idx : objectIds) {
	
				int ind = listObjsIds.indexOf(idx);
				// si c'est le cas, maj de la liste par
				// suppression/insertion
				if (ind > -1) {
					getListObjects().remove(ind);
					// listObjsIds.remove(ind);
					if (!isDelete) { // remplacement
						addIdToListObjects(idx, new Integer(ind));
					} else { // suppression
						listObjsIds.remove(ind);
					}
				}
			}
		} else { // liste vide, on ajoute dans l'ordre
			int i = 0;
			for (Integer idx : objectIds) {
				addIdToListObjects(idx, i);
				i++;
			}
		}
			
		if (updateListeComposant) {
			clearSelection();
			refreshListe2();
		}
	}

	/**
	 * Efface le contenu de la liste.
	 */
	public void clearList() {
		clearSelection();
		getListObjects().clear();
		setCurrentRow(null);
		setCurrentObject(null);

		refreshListe();
	}
	
	public void onSelectFromResultatModale() {
		setSelectedObjects(extractObjectsFromIds(getResultatsIds()));
		onClick$select();
	}
	
	public void onNewCessionFromResultatModale() {
		setSelectedObjects(extractObjectsFromIds(getResultatsIds()));
		if (this instanceof ListeEchantillon) {
			((ListeEchantillon) this).onClick$newCessionItem();
		} else if (this instanceof ListeProdDerive) {
			((ListeProdDerive) this).onClick$newCessionItem();
		}
	}
	
	/**
	 * Reception de l'évènement qd la modale de résultat est ouverte
	 * directement depuis le listeController (recherche rapide).
	 * @param Event 
	 */
	public void onDoBatchDelete(Event e) {		
		List<Integer> ids = new ArrayList<Integer>();
		ids.addAll(getResultatsIds());
		Events.echoEvent("onDeleteIdsFromModaleEvent", self, 
				ids);
	}	
	
	/**
	 * Méthode appelée lors du clic sur le bouton select. Tous les échantillons
	 * sélectionnés seront envoyés à la page demandant cette sélection.
	 */
	public void onClick$select() {
		// on vérifie que la page devant récupérer la sélection
		// existe
		if (Path.getComponent(path) != null) {
			List<Object> data = new ArrayList<Object>();
			data.addAll(getSelectedObjects());
			// on envoie un event à cette page avec
			// les objets sélectionnés
			Events.postEvent(new Event(getOnGetEventName(), Path
					.getComponent(path), data));
		}
		// fermeture de la fenêtre
		getMainWindow().unblockAllPanels();

		clearSelection();
		getObjectTabController().switchToNormalMode();
	}

	/**
	 * Méthode appelée lors du clic sur le bouton cancelSelection.
	 */
	public void onClick$cancelSelection() {
		// fermeture de la fenêtre
		getMainWindow().unblockAllPanels();

		clearSelection();
		getObjectTabController().switchToNormalMode();
	}

	/**
	 * Methode appelée lors de la modification multiple.
	 */
	public void onClick$modificationItem() {
		Clients.showBusy(Labels.getLabel("general.display.wait"));
		Events.echoEvent("onLaterUpdateMulti", self, null);
	}

	public void onLaterUpdateMulti() {
		passSelectedToList();
		setCurrentObject(null);
		setCurrentRow(null);
		getObjectTabController().clearStaticFiche();
		// update de la grille
		getBinder().loadAttribute(objectsListGrid, "model");

		List<TKdataObject> objs = new ArrayList<TKdataObject>();
		objs.addAll(getListObjects());
		getObjectTabController().switchToModifMultiMode(objs);

		clearSelection();

		Clients.clearBusy();
	}

	/**
	 * Renvoie le nom de l'entite associée à la liste.
	 * 
	 * @return nom de l'entite.
	 */
	public String getEntiteNom() {
		return getObjectTabController().getEntiteTab().getNom();
	}

	/***************** abstract methods. **************************/

	public abstract List<? extends TKdataObject> getListObjects();

	public abstract void setListObjects(List<? extends TKdataObject> objs);

	public abstract void addToListObjects(TKdataObject obj, Integer pos);

	public abstract void removeObjectFromList(TKdataObject obj);

	public abstract List<? extends Object> getSelectedObjects();

	public abstract void setSelectedObjects(List<? extends TKdataObject> objs);

	public abstract void addToSelectedObjects(TKdataObject obj);

	public abstract void removeFromSelectedObjects(TKdataObject obj);

	public abstract TKSelectObjectRenderer getListObjectsRenderer();

	public void setListObjectsRenderer(TKSelectObjectRenderer renderer) {
	};

	/**
	 * Copy la selection dans la liste d'objets courants.
	 */
	public abstract void passSelectedToList();

	/**
	 * Copy la liste d'objets courants dans la selection.
	 */
	public abstract void passListToSelected();

	/**
	 * Initialise le contenu de la liste d'éléments.
	 * 
	 * @param nbObjects
	 *            Nombre max d'objets souhaités dans la liste.
	 */
	public abstract void initObjectsBox();

	/**
	 * Applique les critères pour lancer réaliser l'opération de recherche dans
	 * la base.
	 * 
	 * @return liste d'objets resultats
	 */
	public abstract List<Integer> doFindObjects();

	/**
	 * Méthode qui va retourner une liste d'objets dont les ids se trouvent dans
	 * la liste d'identifiants.
	 * 
	 * @param ids
	 *            Liste des identifiants.
	 * @return Liste d'objets.
	 */
	public abstract List<? extends TKdataObject> extractObjectsFromIds(
			List<Integer> ids);
	
	/**
	 * Méthode qui va retourner une d'ids se trouvent dans
	 * la liste des objets.
	 * 
	 * @param liste objetds
	 * @param liste ids à peupler
	 */
	public void extractIdsFromList(
			List<? extends TKdataObject> objs, List<Integer> ids) {
		
		for (TKdataObject obj : objs) {
			ids.add(obj.listableObjectId());
		}		
	}
	
	public void addIdToListObjects(Integer id, Integer pos) {
		TKdataObject obj = getObjectTabController().loadById(id);
		addToListObjects(obj, pos);
	}

	
	/**
	 * Supprime de la base de données une liste d'objets 
	 * à partir de leurs ids
	 * @param ids
	 * @String comment ajout suppr
	 */
	public void batchDelete(List<Integer> ids, String comment) {
	}	


	/**
	 * Méthode qui va retourner une liste des derniers objets enregistrés.
	 * 
	 * @param ids
	 *            Liste des identifiants.
	 * @return Liste d'objets.
	 */
	public abstract List<? extends TKdataObject> extractLastObjectsCreated();

	/*************************************************************************/
	/******************************** Statics. *******************************/
	/*************************************************************************/

	/**
	 * Renvoie l'objet 'bound' a la Ligne d'une liste (ou grid) ou le composant
	 * représentant la ligne.
	 * 
	 * @param event
	 * @param getTarget
	 *            boolean si true renvoie le composant, l'objet sinon
	 * @return object bound ou composant
	 */
	public static Object getBindingData(ForwardEvent event, boolean getTarget) {
		Component target = event.getOrigin().getTarget();
		try {
			while (!(target instanceof Row || target instanceof Listitem)) {
				target = target.getParent();
			}
			Map<?,?> map = (Map<?,?>) target.getAttribute("zkplus.databind.TEMPLATEMAP");
			if (!getTarget) {
				return map.get(target.getAttribute("zkplus.databind.VARNAME"));
			} else {
				return target;
			}
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Cette méthode va retourner la ligne courante.
	 * 
	 * @param event
	 *            Event sur la grille contenant la liste des objets.
	 * @return Une Row.
	 */
	public static Row getRow(ForwardEvent event) {
		Component target = event.getOrigin().getTarget();
		try {
			while (!(target instanceof Row)) {
				target = target.getParent();
			}
			// Map map = (Map) target
			// .getAttribute("zkplus.databind.TEMPLATEMAP");
			return (Row) target;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/*************************************************************************/
	/************************** DROITS ***************************************/
	/*************************************************************************/
	public boolean isCanNew() {
		return canNew;
	}

	public void setCanNew(boolean cNew) {
		this.canNew = cNew;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean i) {
		this.isAdmin = i;
	}

	public boolean isCanModifMultiple() {
		return canModifMultiple;
	}

	public void setCanModifMultiple(boolean can) {
		this.canModifMultiple = can;
	}

	public boolean isCanExport() {
		return canExport;
	}

	public void setCanExport(boolean can) {
		this.canExport = can;
	}

	public boolean isCanEtiquette() {
		return canEtiquette;
	}

	public void setCanEtiquette(boolean canE) {
		this.canEtiquette = canE;
	}
	
	public boolean isCanDelete() {
		return canDelete;
	}
	
	public void setCanDelete(boolean cDelete) {
		this.canDelete = cDelete;
	}

	/**
	 * Genere les droits supplémentaire à ceux appliqués aux boutons de creation
	 * et de modification multiples.
	 */
	public void applyDroitsOnListe() {
		// application aux buttons
		if (addNew != null) {
			addNew.setDisabled(!isCanNew());
		}
		disableObjetsSelectionItems(true);

		if (sessionScope.containsKey("Anonyme")
				&& (Boolean) sessionScope.get("Anonyme")) {
			setAnonyme(true);
		} else {
			setAnonyme(false);
		}
	}
	
	public void disableObjectTreeButtons(boolean disable) {
		// objets tree item
		// recupère LA banque courante ou la première banque en toutes collections 
		// car par définition, même profil pour toutes les banques en toutes collections
		Banque banqueToGetAuthorisation = null;
		if (!SessionUtils.getSelectedBanques(sessionScope).isEmpty()) {
			banqueToGetAuthorisation = SessionUtils
			.getSelectedBanques(sessionScope).get(0);
		}
		if (patientsItem != null) {
			patientsItem.setDisabled(disable || getSelectedObjects().size() < 1 
					|| !getDroitEntiteObjectConsultation(banqueToGetAuthorisation, 
							"Patient"));
		}
		if (prelevementsItem != null) {
			prelevementsItem.setDisabled(disable || getSelectedObjects().size() < 1 
					|| !getDroitEntiteObjectConsultation(banqueToGetAuthorisation, 
					"Prelevement"));
		}
		if (echantillonsItem != null) {
			echantillonsItem.setDisabled(disable || getSelectedObjects().size() < 1 
					|| !getDroitEntiteObjectConsultation(banqueToGetAuthorisation, 
							"Echantillon"));
		}
		if (derivesItem != null) {
			derivesItem.setDisabled(disable || getSelectedObjects().size() < 1 
					|| !getDroitEntiteObjectConsultation(banqueToGetAuthorisation, 
							"ProdDerive"));
		}
		if (derivesAscItem != null) {
			derivesAscItem.setDisabled(disable || getSelectedObjects().size() < 1 
					|| !getDroitEntiteObjectConsultation(banqueToGetAuthorisation, 
							"ProdDerive"));
		}
		if (cessionsItem != null) {
			cessionsItem.setDisabled(disable || getSelectedObjects().size() < 1 
					|| !getDroitEntiteObjectConsultation(banqueToGetAuthorisation, 
							"Cession"));
		}
	}

	/**
	 * Attribues les droits de creation et de modification multiples.
	 */
	@SuppressWarnings("unchecked")
	public void drawActionsButtons() {
		Boolean admin = false;
		if (sessionScope.containsKey("Admin")) {
			admin = (Boolean) sessionScope.get("Admin");
		}
		setAdmin(admin);

		// si l'utilisateur est admin => boutons cliquables
		if (isAdmin()) {
			setCanNew(true);
			setCanModifMultiple(true);
			setCanExport(true);
			setCanDelete(true);
		} else {
			// on extrait les OperationTypes de la base
			OperationType creation = ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager("Creation", true).get(0);
			OperationType modifMulti = ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager("ModifMultiple", true).get(0);
			OperationType archivage = ManagerLocator
					.getOperationTypeManager()
					.findByNomLikeManager("Archivage", true).get(0);

			Hashtable<String, List<OperationType>> droits = new Hashtable<String, List<OperationType>>();

			if (sessionScope.containsKey("Droits")) {
				// on extrait les droits de l'utilisateur
				droits = (Hashtable<String, List<OperationType>>) sessionScope
						.get("Droits");

				List<OperationType> ops = droits.get(getEntiteNom());
				setCanNew(ops.contains(creation));
				setCanModifMultiple(ops.contains(modifMulti));
				setCanDelete(ops.contains(archivage));
			}

			// gestion de l'export
			if (sessionScope.containsKey("Export")) {
				if (sessionScope.get("Export").equals("Export")) {
					setCanExport(true);
				} else if (sessionScope.get("Export").equals("ExportAnonyme")) {
					setCanExport(true);
				} else {
					setCanExport(false);
				}
			} else {
				setCanExport(false);
			}
		}
		
		// retour
		if (retourItem != null) {
			retourItem.setVisible(isAdmin());
		}

		if (SessionUtils.getSelectedBanques(sessionScope).size() > 0) {
			// on récupère les imprimantes associées au compte
			// pour la banque courante
//			List<AffectationImprimante> imprimantes = ManagerLocator
//					.getAffectationImprimanteManager()
//					.findByBanqueUtilisateurManager(
//							SessionUtils.getSelectedBanques(sessionScope)
//									.get(0),
//							SessionUtils.getLoggedUser(sessionScope));		
//			setCanEtiquette(imprimantes.size() > 0);
			setCanEtiquette(!ManagerLocator.getImprimanteManager()
					.findByPlateformeManager(SessionUtils
							.getPlateforme(sessionScope)).isEmpty());
		} else {
			setCanEtiquette(false);
		}
	}

	/**
	 * Méthode appelée lorsque l'utilisateur lance une recherche avançées Cette
	 * méthode va créer une nouvelle fenêtre.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param objToPrint
	 *            Objet à imprimer.
	 */
	public void openRechercheAvanceeWindow(Page page, String title,
			Entite entiteToSearch, String p, boolean anonyme, 
			AbstractListeController2 listecontroller) {
		if (!isBlockModal()) {

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("rechercheAvanceeWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(title);
			win.setBorder("normal");
			win.setHflex("1");
			win.setWidth("90%");
			win.setFocus(true);
			int height = getMainWindow().getPanelHeight() + 30;
			win.setHeight(height + "px");
			win.setClosable(true);
			// win.setZclass("z-window-modal");

			final HtmlMacroComponent ua = populateRechercheAvanceeModal(win,
					page, entiteToSearch, p, anonyme, sessionScope, 
					listecontroller);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					// progress.detach();
					ua.setVisible(true);
				}
			});

			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try {
				win.doModal();

				setBlockModal(false);

			} catch (SuspendNotAllowedException e) {
				log.error(e);
			}
		}
	}

	private static HtmlMacroComponent populateRechercheAvanceeModal(Window win,
			Page page, Entite entiteToSearch, String path, boolean anonyme,
			Map<?, ?> sessionScope, AbstractListeController2 listeController) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua = null;

		if (entiteToSearch.getNom().equals("Patient")) {
			ua = (HtmlMacroComponent) page.getComponentDefinition(
					"ficheRechercheAvanceePatient", false).newInstance(page,
					null);
			ua.setParent(win);
			ua.setId("openRechercheAvanceePatientModale");
			ua.applyProperties();
			ua.afterCompose();

			((FicheRechercheAvancee) ua
					.getFellow("fwinRechercheAvanceePatient")
					.getAttributeOrFellow(
							"fwinRechercheAvanceePatient$composer", true))
					.initRechercheAvancee(entiteToSearch, path, anonyme, 
							listeController);

		} else if (entiteToSearch.getNom().equals("Prelevement")) {
			String pageDef = "ficheRechercheAvanceePrelevement";
			String winDef = "fwinRechercheAvanceePrelevement";
			if (SessionUtils.isSeroContexte(sessionScope)) {
				pageDef = "ficheRechercheAvanceePrelevementSero";
				winDef = "fwinRechercheAvanceePrelevementSero";
			}
			ua = (HtmlMacroComponent) page.getComponentDefinition(pageDef,
					false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("openRechercheAvanceePrelevementModale");
			ua.applyProperties();
			ua.afterCompose();

			((FicheRechercheAvancee) ua.getFellow(winDef).getAttributeOrFellow(
					winDef + "$composer", true)).initRechercheAvancee(
					entiteToSearch, path, anonyme, listeController);
		} else if (entiteToSearch.getNom().equals("Echantillon")) {
			ua = (HtmlMacroComponent) page.getComponentDefinition(
					"ficheRechercheAvanceeEchantillon", false).newInstance(
					page, null);
			ua.setParent(win);
			ua.setId("openRechercheAvanceeEchantillonModale");
			ua.applyProperties();
			ua.afterCompose();

			((FicheRechercheAvancee) ua.getFellow(
					"fwinRechercheAvanceeEchantillon").getAttributeOrFellow(
					"fwinRechercheAvanceeEchantillon$composer", true))
					.initRechercheAvancee(entiteToSearch, path, anonyme, 
							listeController);
		} else if (entiteToSearch.getNom().equals("ProdDerive")) {
			ua = (HtmlMacroComponent) page.getComponentDefinition(
					"ficheRechercheAvanceeProdDerive", false).newInstance(page,
					null);
			ua.setParent(win);
			ua.setId("openRechercheAvanceeProdDeriveModale");
			ua.applyProperties();
			ua.afterCompose();

			((FicheRechercheAvancee) ua.getFellow(
					"fwinRechercheAvanceeProdDerive").getAttributeOrFellow(
					"fwinRechercheAvanceeProdDerive$composer", true))
					.initRechercheAvancee(entiteToSearch, path, anonyme, 
							listeController);
		}

		return ua;
	}

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien pour imprimer la
	 * page. Cette méthode va créer une nouvelle fenêtre.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param objToPrint
	 *            Objet à imprimer.
	 */
	public void openRechercheAvanceeCessionWindow(Page page, String title,
			Entite entiteToSearch, String p) {
		if (!isBlockModal()) {

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("rechercheAvanceeWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(title);
			win.setBorder("normal");
			win.setHflex("1");
			win.setWidth("90%");
			win.setFocus(true);
			int height = getMainWindow().getPanelHeight() + 30;
			win.setHeight(height + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua = populateRechercheAvanceeCessionModal(
					win, page, entiteToSearch, p);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					// progress.detach();
					ua.setVisible(true);
				}
			});

			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try {
				win.doModal();

				setBlockModal(false);

			} catch (SuspendNotAllowedException e) {
				log.error(e);
			}
		}
	}

	public HtmlMacroComponent populateRechercheAvanceeCessionModal(
			Window win, Page page, Entite entiteToSearch, String path) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua = null;

		ua = (HtmlMacroComponent) page.getComponentDefinition(
				"ficheRechercheAvanceeCession", false).newInstance(page, null);
		ua.setParent(win);
		ua.setId("openRechercheAvanceeCessionModale");
		ua.applyProperties();
		ua.afterCompose();

		((FicheRechercheAvanceeCession) ua.getFellow(
				"fwinRechercheAvanceeCession").getAttributeOrFellow(
				"fwinRechercheAvanceeCession$composer", true))
				.initRechercheAvancee(entiteToSearch, path, this);

		return ua;
	}

	/**
	 * Ouverture de la modale permettant une recherche avancée à partir des 
	 * items INCa.
	 */
	public void openRechercheINCaWindow(Page page, Entite entiteToSearch,
			String p) {
		if (!isBlockModal()) {

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("rechercheINCaWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("recherche.inca.titre"));
			win.setBorder("normal");
			win.setHflex("1");
			win.setWidth("90%");
			win.setFocus(true);
			int height = getMainWindow().getPanelHeight() + 30;
			win.setHeight(height + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua = populateRechercheINCa(win, page,
					entiteToSearch, p, this);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					// progress.detach();
					ua.setVisible(true);
				}
			});

			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try {
				win.doModal();

				setBlockModal(false);

			} catch (SuspendNotAllowedException e) {
				log.error(e);
			}
		}
	}

	private static HtmlMacroComponent populateRechercheINCa(Window win,
			Page page, Entite entiteToSearch, String path, AbstractListeController2 controller) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua = null;

		ua = (HtmlMacroComponent) page.getComponentDefinition(
				"ficheRechercheINCa", false).newInstance(page, null);
		ua.setParent(win);
		ua.setId("openRechercheINCaModale");
		ua.applyProperties();
		ua.afterCompose();

		((FicheRechercheINCa) ua.getFellow("fwinRechercheINCa")
				.getAttributeOrFellow("fwinRechercheINCa$composer", true))
				.initRechercheAvancee(entiteToSearch, path, controller);

		return ua;
	}

	/**
	 * PopUp window appelée pour attente lors de l'export.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param message
	 *            affiché à l'utilisateur.
	 * @param boolean cascadable si possibilité de cascader la délétion ou
	 *        l'archivage
	 * @param deletable
	 *            si la deletion est possible.
	 * @param controller
	 *            parent ayant demandé la délétion.
	 */
	@SuppressWarnings("unchecked")
	public void openExportWindow(Page page, String entite, List<?> objs,
			List<Banque> banques, boolean isExportAnonyme, Utilisateur user) {
		if (!isBlockModal()) {

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("exportWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setBorder("normal");
			win.setWidth("400px");
			int height = 175;
			win.setHeight(String.valueOf(height) + "px");
			win.setClosable(false);

			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent) page.getComponentDefinition(
					"exportModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("exportModaleComponent");
			ua.applyProperties();
			ua.afterCompose();

			((ExportModale) ua.getFellow("fwinExportModale")
					.getAttributeOrFellow("fwinExportModale$composer", true))
					.init(entite, (List<Object>) objs, banques, isExportAnonyme, user);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					// progress.detach();
					ua.setVisible(true);
				}
			});

			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try {
				win.onModal();
				setBlockModal(false);

			} catch (SuspendNotAllowedException e) {
				log.error(e);
			}
		}
	}

	public List<Integer> getResultatsIds() {
		return resultatsIds;
	}

	public void setResultatsIds(List<Integer> ids) {
		this.resultatsIds.clear();
		if (ids != null) {
			this.resultatsIds.addAll(ids);
		}
	}

	public List<Integer> getRestrictedTableIds() {
		return restrictedTableIds;
	}

	public void setRestrictedTableIds(List<Integer> rI) {
		this.restrictedTableIds = rI;
	}
	
	public Menuitem getExportItemAdv() {
		return exportItemAdv;
	}

	public void switchToEditMode(boolean b) {
		disableToolBar(b);
	}

	/**
	 * Ouvre une fenêtre pour uploader un fichier contenant une liste de
	 * valeurs, extrait ces valeurs et les retourne pour permettre la recherche.
	 * 
	 * @return
	 */
	public List<String> getListStringToSearch() {
		List<String> liste = new ArrayList<String>();
		Media[] medias;
		InputStream fileInputStream = null;
		medias = Fileupload
				.get(ObjectTypesFormatters.getLabel("general.upload.limit",
						new String[] { String.valueOf(10000) }), Labels
						.getLabel("general.search.file.upload"), 1, 10000, true);
		if (medias != null && medias.length > 0) {
			fileInputStream = medias[0].getStreamData();
			liste = ManagerLocator.getImportManager()
					.extractListOfStringFromExcelFile(fileInputStream, true);
		}
		
		// since 2.1
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fileInputStream = null;
			}
		} 
		
		return liste;
	}
	
	/**
	 * Impression des étiquettes pour les TKStockableObject
	 */
	@SuppressWarnings("unchecked")
	public void onClick$etiquetteItem() {
		
		AffectationImprimante affectation = null;
		
		List<AffectationImprimante> affs = ManagerLocator
			.getAffectationImprimanteManager()
			.findByBanqueUtilisateurManager(
					SessionUtils.getSelectedBanques(sessionScope).get(0),
					SessionUtils.getLoggedUser(sessionScope));
		if (affs.size() > 0) {
			affectation = affs.get(0);
		}
		
		openImprimanteModeleModale(
				SessionUtils.getPlateforme(sessionScope), 
				(List<? extends TKStockableObject>) getSelectedObjects(), 
				affectation, null);
	}
	
	@SuppressWarnings("unchecked")
	public void onDeleteIdsFromModaleEvent(Event e) {
		getResultatsIds().clear();
		if (e.getData() != null && e.getData() instanceof List) {
			getResultatsIds().addAll((List<Integer>) e.getData());
		}
		openDeleteWindow(getPage(), "message", true, true, true, self, false);
	}
	
	/**
	 * batch delete objects from Liste selection
	 * clear resultatsIds avant toute chose, récupère 
	 * les ids depuis la résultatModale
	 * @since 2.0.12
	 */
	public void onClick$deleteItem(Event e) {
		Messagebox.show(ObjectTypesFormatters.getLabel("message.deletion.multiple", 
			new String[]{String.valueOf(getSelectedIds(true).size())}), 
		    Labels.getLabel("general.warning"), 
		    Messagebox.OK | Messagebox.CANCEL,
		    Messagebox.EXCLAMATION,
		        new org.zkoss.zk.ui.event.EventListener<Event>(){
		            public void onEvent(Event e){
		                if (Messagebox.ON_OK.equals(e.getName())){
		                	getResultatsIds().clear();
		            		openDeleteWindow(getPage(), "message", 
		            				true, true, true, self, false);
		                } else if (Messagebox.ON_CANCEL.equals(e.getName())){
		                    //Cancel is clicked
		                }
		            }
		        }
		    );
	}
	
	public void onDeleteTriggered(Event event) {
		Clients.showBusy(Labels.getLabel("deletion.general.wait"));
		String comments = null;
		if (event.getData() != null) {
			comments = (String) event.getData();
		}
		Events.echoEvent("onLaterBatchDelete", self, comments);
	}
		
	public void onLaterBatchDelete(Event e) {
		try {
			if (getResultatsIds().isEmpty()) {
				setResultatsIds(getSelectedIds(true));
			}
			
			Map<Entite, List<Integer>> childrens = getObjectTabController()
					.getChildrenObjectsIds(getResultatsIds());
	
			Map<Entite, List<Integer>> parents = getObjectTabController()
							.getParentsObjectsIds(getResultatsIds());
	
			batchDelete(getResultatsIds(), (String) e.getData());

			getObjectTabController().getListe()
				.updateGridByIds(getResultatsIds(), true, true);
//				}
//				getObjectTabController().clearStaticFiche();
//				getObjectTabController().switchToOnlyListeMode();
//			}
//			
//			
//			refreshListe();
//			
			
			
						
			// update de la liste des parents
			getObjectTabController().updateParentsReferences(parents);
			
			// update de la liste des enfants
			getObjectTabController().updateChildrenReferences(childrens, true);
			
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} finally {
			// ferme wait message
			Clients.clearBusy();
		}
	}

	/**
	 * Vérifie que dans la liste d'objets sélectionnés, au moins un 
	 * n'est pas stocké afin de rendre le bouton 'Stocker' cliquable. 
	 * Cette méthode n'est appelée que pour les listes Echantillon et Derives.
	 * @return false si au moins un objet n'est pas stocke.
	 */
	public boolean areAllObjectsStocked() {
		boolean out = true;
		
		for (Object tkSObj : getSelectedObjects()) {
			if (((TKStockableObject) tkSObj)
					.getObjetStatut().getStatut().equals("NON STOCKE")) {
				return false;
			}
		}
		
		return out;
	}
	
	/**
	 * Vérifie que dans la liste d'objets sélectionnés, au moins a un statut STOCKE 
	 * ou non STOCKE. 
	 * Cette méthode n'est appelée que pour les listes Echantillon et Derives.
	 * @return false si au moins un objet n'est pas stocke.
	 */
	public boolean areAllObjectsCessibles() {
		boolean out = true;
		
		for (Object tkSObj : getSelectedObjects()) {
			if (((TKStockableObject) tkSObj)
					.getObjetStatut().getStatut().equals("STOCKE")
					|| ((TKStockableObject) tkSObj)
					.getObjetStatut().getStatut().equals("NON STOCKE")) {
				return false;
			}
		}
		
		return out;
	}
	
	/**
	 * Ouvre la modale permettant de renseigner un évènement de stockage 
	 * à tous les objets sélectionnés.
	 * Ouvre une warning modale si un objet est en statut
	 * @since 2.0.10
	 */
	@SuppressWarnings("unchecked")
	public void onClick$retourItem() {
		
		if (!getObjStatutIncompatibleForRetour(getSelectedObjects(), null)) {
			openRetourFormModale(null, true, null, 
					null, (List<TKStockableObject>) getSelectedObjects(),
					null, null, 
					null, null, null, null, null);	
			
			clearSelection();
		} 
	}
	
	public boolean isTtesCollection() {
		return SessionUtils.getSelectedBanques(sessionScope).size() > 1;
	}
	
	@Override
	public void onClickObjectEmplacement(Event event) {
		if (!getMode().equals("select")) {
			super.onClickObjectEmplacement(event);
		}
	}
	
	/**
	 * Bouton de menu affichant la modale permettant la complétion 
	 * des évènements de stockage incomplets
	 * @since 2.0.10
	 */
	@SuppressWarnings("unchecked")
	public void onClick$incompRetoursItem() {
		openDateRetourModale(this, (List<TKStockableObject>) getSelectedObjects());
	}
	
	/**
	 * Appelles l'ouverture de la resultats modale depuis un autre
	 * component
	 * @param ids Resultats
	 */
	public void callResultatsModale(List<Integer> res) {
		getResultatsIds().clear();
		getResultatsIds().addAll(res);
		openResultatsWindow(page, res, self, 
				getEntiteNom(), getObjectTabController());
	}
	
	/*************************************************************************/
	/****************** Evenements on click arbre d'objets *******************/
	/*************************************************************************/
	private void postTargetObjectsIds(String eNom, Boolean deriveDesc, 
													List<Integer> ids) {
		
		// recuperation des ids target
		List<Integer> resIds= ManagerLocator.getCorrespondanceIdManager()
				.findTargetIdsFromIdsManager(ids, 
		getObjectTabController().getEntiteTab(), 
		ManagerLocator.getEntiteManager().findByNomManager(eNom).get(0), 
		SessionUtils.getSelectedBanques(sessionScope), deriveDesc);
		
		// changement onglet
		getObjectTabController().postIdsToOtherEntiteTab(eNom, resIds);
	}
	
	@SuppressWarnings("unchecked")
	private List<Integer> getSelectedIds(boolean fromSelection) {
		List<Integer> ids = new ArrayList<Integer>();
		if (fromSelection) {
			extractIdsFromList((List<TKdataObject>) getSelectedObjects(), 
				ids);
		}
		return ids;
	}
	
	
	@SuppressWarnings("unchecked")
	public void onClick$patientsItem(Event e) {
		if (e.getData() != null && !((List<Integer>) e.getData()).isEmpty()) {
			postTargetObjectsIds("Patient", null, (List<Integer>) e.getData());
		} else {
			postTargetObjectsIds("Patient", null, getSelectedIds(true));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$prelevementsItem(Event e) {
		if (e.getData() != null && !((List<Integer>) e.getData()).isEmpty()) {
			postTargetObjectsIds("Prelevement", null, (List<Integer>) e.getData());
		} else {
			postTargetObjectsIds("Prelevement", null, getSelectedIds(true));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$echantillonsItem(Event e) {
		if (e.getData() != null && !((List<Integer>) e.getData()).isEmpty()) {
			postTargetObjectsIds("Echantillon", null, (List<Integer>) e.getData());
		} else {
			postTargetObjectsIds("Echantillon", null, getSelectedIds(true));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$derivesItem(Event e) {
		if (e.getData() != null && !((List<Integer>) e.getData()).isEmpty()) {
			postTargetObjectsIds("ProdDerive", true, (List<Integer>) e.getData());
		} else {
			postTargetObjectsIds("ProdDerive", true, getSelectedIds(true));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$derivesAscItem(Event e) {
		if (e.getData() != null && !((List<Integer>) e.getData()).isEmpty()) {
			postTargetObjectsIds("ProdDerive", false, (List<Integer>) e.getData());
		} else {
			postTargetObjectsIds("ProdDerive", false, getSelectedIds(true));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$cessionsItem(Event e) {
		if (e.getData() != null && !((List<Integer>) e.getData()).isEmpty()) {
			postTargetObjectsIds("Cession", null, (List<Integer>) e.getData());
		} else {
			postTargetObjectsIds("Cession", null, getSelectedIds(true));
		}
	}
	
	public void updateListResultsLabel(Integer nbResults) {
		 if (getObjectTabController() != null 
				 && getObjectTabController().getListeRegion() != null) {
			 getObjectTabController().getListeRegion()
			 	//.getCaption().setLabel
			 	.setTitle(Labels.getLabel("general.recherche") 
			 			+ " (" + 
			 		(nbResults != null ? nbResults : 
			 			getListObjects() != null ? getListObjects().size() : 0)
			 			+ ")");			 
		 }
	}
}
