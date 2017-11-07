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
package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.ext.TreeOpenableModel;
import org.zkoss.zul.ext.TreeSelectableModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.TumoTreeModel;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;
import fr.aphp.tumorotek.webapp.tree.stockage.ConteneurNode;
import fr.aphp.tumorotek.webapp.tree.stockage.EnceinteNode;
import fr.aphp.tumorotek.webapp.tree.stockage.StockageRootNode;
import fr.aphp.tumorotek.webapp.tree.stockage.StockageTreeItemRenderer;
import fr.aphp.tumorotek.webapp.tree.stockage.TerminaleNode;

/**
 * 
 * @version 2.1
 * @author Mathieu BARTHELEMY
 *
 */
public class ListeStockages extends AbstractController {

	private Log log = LogFactory.getLog(ListeStockages.class);

	private static final long serialVersionUID = -836695461692709048L;
	
	// @since 2.1
	List<Conteneur> rootConteneurs = new ArrayList<Conteneur>();

	private Panel listPanel;
	private Tree mainTreeContext;
	private Treeitem selectedItem;
	private Treeitem selectedDestinationItem;
	private boolean oldOpen;
	private Menubar menuBar;
	private Menuitem addNewConteneur;
	private Menuitem etiquettes;
	private Button hideCompleteButton;

	// @since 2.1
	private Textbox findTerminaleBox;
	private StockageController stockageController;
	List<int[]> searchPaths = new ArrayList<int[]>();
	
	private boolean hideCompleteTerminales = false;

	/**
	 * Variables pour l'arbre.
	 */
	private TumoTreeModel ttm;
	private StockageTreeItemRenderer ctr;
	// liste de noeuds principaux de l'arbre
	private List<TumoTreeNode> rootNodes = new ArrayList<TumoTreeNode>();

	private String deplacementMode = "normal";
	private Object currentObject;

	private AnnotateDataBinder stockageBinder;
	
	// @since 2.1
	private Conteneur selectedConteneur;
	private Enceinte selectedEnceinte;
	private Terminale selectedTerminale;

	@Override
	public AnnotateDataBinder getBinder() {
		return stockageBinder;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		stockageBinder = new AnnotateDataBinder(comp);
		stockageBinder.loadAll();

		// Init du noeud root de l'arbre
		StockageRootNode root = new StockageRootNode(
				SessionUtils.getSelectedBanques(sessionScope));
		root.readChildren();
		rootNodes = root.getChildren();
		//@since 2.1
		getRootConteneurs().addAll(root.getConteneurs()); 

		// Init de l'arbre et de son affichage
		ttm = new TumoTreeModel(root);
		ctr = new StockageTreeItemRenderer();

		drawActionsForStockage();

		int height = getMainWindow().getListPanelHeight() + 145;
		listPanel.setHeight(height + "px");

		mainTreeContext.setHeight(height - 50 + "px");

		getBinder().loadComponent(self);
	}

	/**
	 * Recupere le controller du panel.
	 * 
	 * @param event
	 *            Event
	 * @return fiche StockageController
	 */
	public StockageController getStockageController() {
		return stockageController;
	}

	public void setStockageController(StockageController sc) {
		this.stockageController = sc;
	}

	/**
	 * Passe la liste en mode "déplacement".
	 */
	public void switchToDeplacementMode() {
		deplacementMode = "deplacement";
		menuBar.setVisible(false);
		hideCompleteButton.setVisible(false);
		hideCompleteTerminales = false;

		// récupère la ligne sélectionnée et la colore en jaune
		org.zkoss.zul.Treerow row = selectedItem.getTreerow();
		colorateRowInYellow(row);
	}
	
	/**
	 * Passe la liste en mode "déplacement emplacement".
	 */
	public void switchToDeplacementEmplacementMode() {
		deplacementMode = "deplacementEmplacement";
		menuBar.setVisible(false);
		hideCompleteButton.setVisible(false);
		hideCompleteTerminales = false;
	}
	
	public void switchToStockerMode() {
		switchToDeplacementEmplacementMode();
		updateAllConteneurs(false);
		hideCompleteButton.setVisible(true);
	}
	
	public void onClick$hideCompleteButton() {
		hideCompleteTerminales = !hideCompleteTerminales;
		updateAllConteneurs(false);
	}

	/**
	 * Passe la liste en mode "normal".
	 */
	public void switchToNormalMode() {
		deplacementMode = "normal";
		menuBar.setVisible(true);
		hideCompleteButton.setVisible(false);
		hideCompleteTerminales = false;


		// on met les deux lignes en blanc
		if (selectedItem != null) {
			org.zkoss.zul.Treerow row = selectedItem.getTreerow();
			row.setStyle(null);
			row.invalidate();
		}

		if (selectedDestinationItem != null) {
			org.zkoss.zul.Treerow row2 = selectedDestinationItem.getTreerow();
			row2.setStyle(null);
			row2.invalidate();
			selectedDestinationItem = null;
		}

		mainTreeContext.setSelectedItem(null);
	}

	/**
	 * Lors de la sélection d'un noeud de l'arbre, nous afficherons la fiche
	 * correspondant à cet élément.
	 * @version 2.1
	 */
	public void onSelect$mainTreeContext() {
		
		// since 2.1
		cleanRowsColor();
		setSelectedConteneur(null);
		setSelectedEnceinte(null);
		setSelectedTerminale(null);
		
		// Mode normal
		if (deplacementMode.equals("normal")) {
			selectForView();
			// Mode déplacement
		} else if (deplacementMode.equals("deplacement")) {
			selectForDeplaceEnceinte();
		} else if (deplacementMode.equals("deplacementEmplacement")) {
			selectForDeplaceEmplacement();
		}
	}

	public void onClick$deplacerEmplacements() {
		getStockageController().switchToDeplacerEmplacementsMode(null, null);
	}

	/**
	 * Sélectionne un item pour le visualiser.
	 */
	public void selectForView() {
		if (mainTreeContext.getSelectedItem() != null) {
			currentObject = mainTreeContext.getSelectedItem().getValue();
		}
		selectedItem = mainTreeContext.getSelectedItem();

		// Si c'est un noeud conteneur => FicheConteneur
		// Si c'est un noeud enceinte => FicheEnceinte
		// Si c'est un noeud terminale => FicheTerminale
		if (currentObject instanceof ConteneurNode) {

			ConteneurNode node = (ConteneurNode) currentObject;
			getStockageController().switchToFicheConteneurMode(
					node.getConteneur().clone());
			// @since 2.1
			setSelectedConteneur(
					((ConteneurNode) currentObject).getConteneur());
		} else if (currentObject instanceof EnceinteNode) {
			EnceinteNode node = (EnceinteNode) currentObject;
			getStockageController().switchToFicheEnceinteMode(
					node.getEnceinte().clone());
			// @since 2.1
			setSelectedEnceinte(
					((EnceinteNode) currentObject).getEnceinte());
		} else if (currentObject instanceof TerminaleNode) {
			TerminaleNode node = (TerminaleNode) currentObject;
			getStockageController().switchToFicheTerminaleMode(
					node.getTerminale().clone(), null);
		}
	}

	/**
	 * Sélectionne un item pour le déplacer.
	 */
	public void selectForDeplaceEnceinte() {
		// si une destination était sélectionnée, on la déselctionne
		if (selectedDestinationItem != null) {
			org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
			row.setStyle(null);
			row.invalidate();
		}

		// on récupère la destination
		Object obj = mainTreeContext.getSelectedItem().getValue();
		selectedDestinationItem = mainTreeContext.getSelectedItem();

		// si l'objet à déplacer est une enceinte
		if (currentObject instanceof EnceinteNode) {
			// la destination doit être une enceinte
			if (obj instanceof EnceinteNode) {
				EnceinteNode nodeDep = (EnceinteNode) currentObject;
				EnceinteNode nodeDest = (EnceinteNode) obj;

				// la destination doit être différente du départ
				if (!nodeDep.equals(nodeDest)) {
					// la destination doit être valide
					if (nodeDep.getConteneur().getNbrNiv()
							.equals(nodeDest.getConteneur().getNbrNiv())
							&& nodeDep.getNiveau() == nodeDest.getNiveau()) {
						// on met la destination en vert
						org.zkoss.zul.Treerow row = selectedDestinationItem
								.getTreerow();
						colorateRowInGreen(row);
						// on l'envoie à la fiche enceinte
						getStockageController().getFicheEnceinte()
								.definirEnceinteDestination(nodeDest);
					} else {
						// on met la destination en rouge
						org.zkoss.zul.Treerow row = selectedDestinationItem
								.getTreerow();
						colorateRowInRed(row);
						getStockageController().getFicheEnceinte()
								.definirEnceinteDestination(null);
					}
				} else {
					selectedDestinationItem = null;
				}

			} else {
				// on met la destination en rouge
				org.zkoss.zul.Treerow row = selectedDestinationItem
						.getTreerow();
				colorateRowInRed(row);
				getStockageController().getFicheEnceinte()
						.definirEnceinteDestination(null);
			}

		} else if (currentObject instanceof TerminaleNode) {
			// la destination doit être une boite
			if (obj instanceof TerminaleNode) {
				TerminaleNode nodeDep = (TerminaleNode) currentObject;
				TerminaleNode nodeDest = (TerminaleNode) obj;

				// la destination doit être différente du départ
				if (!nodeDep.equals(nodeDest)) {
					// la destination doit être valide
//					if (nodeDep.getConteneur().getNbrNiv()
//							.equals(nodeDest.getConteneur().getNbrNiv())) {
						// on met la destination en vert
						org.zkoss.zul.Treerow row = selectedDestinationItem
								.getTreerow();
						colorateRowInGreen(row);
						// on l'envoie à la fiche enceinte
						getStockageController().getFicheTerminale()
								.definirTerminaleDestination(nodeDest);
//					} else {
//						// on met la destination en rouge
//						org.zkoss.zul.Treerow row = selectedDestinationItem
//								.getTreerow();
//						colorateRowInRed(row);
//						getStockageController().getFicheTerminale()
//								.definirTerminaleDestination(null);
//					}
				} else {
					selectedDestinationItem = null;
				}

			} else {
				// on met la destination en rouge
				org.zkoss.zul.Treerow row = selectedDestinationItem
						.getTreerow();
				colorateRowInRed(row);
				getStockageController().getFicheTerminale()
						.definirTerminaleDestination(null);
				
				// since 2.1
				// afin de permettre la restriction par arborescence 
				// sur une recherche
				if (obj instanceof ConteneurNode) {
					setSelectedConteneur(((ConteneurNode) obj).getConteneur());
				} else if (obj instanceof EnceinteNode) { 
					setSelectedEnceinte(((EnceinteNode) obj).getEnceinte());
				}
			}
		}
	}

	/**
	 * Depuis version 2.1, conserve le conteneur ou l'enceinte selectionnée afin 
	 * d'être réutilisable lors d'un full-rack scan
	 * @version 2.1
	 */
	public void selectForDeplaceEmplacement() {
		currentObject = mainTreeContext.getSelectedItem().getValue();
		selectedItem = mainTreeContext.getSelectedItem();

		if (currentObject instanceof TerminaleNode) {
			TerminaleNode node = (TerminaleNode) currentObject;
			getStockageController().getFicheDeplacerEmplacements()
					.definirTerminaleDestination(node);
		} else if (currentObject instanceof ConteneurNode) {
			setSelectedConteneur(
				((ConteneurNode) currentObject).getConteneur());
		} else if (currentObject instanceof EnceinteNode) {
			setSelectedEnceinte(
				((EnceinteNode) currentObject).getEnceinte());
		}
	}

	/**
	 * Création d'un nouveau conteneur.
	 */
	public void onClick$addNewConteneur() {
		getStockageController().switchToFicheConteneurCreateMode();
	}

	public void updateConteneur(Conteneur conteneur) {
		
		ConteneurNode node = new ConteneurNode(conteneur, null);
		
		((TreeOpenableModel) ttm).clearOpen();
		selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));

		ttm.addOpenPath(ttm.getPath(node));
	}

	/**
	 * Mets à jour la liste des conteneurs
	 * @version 2.1
	 */
	public void updateAllConteneurs(boolean isAdd) {
		// Init du noeud root de l'arbre
		StockageRootNode root = new StockageRootNode(
				SessionUtils.getSelectedBanques(sessionScope));
		root.setHideComplete(hideCompleteTerminales);
		root.readChildren();
		rootNodes = root.getChildren();
		// @since 2.1
		setSelectedConteneur(null);
		setSelectedEnceinte(null);
		getRootConteneurs().clear();
		getRootConteneurs().addAll(root.getConteneurs());

		// Init de l'arbre et de son affichage
		ttm = new TumoTreeModel(root);
		ctr = new StockageTreeItemRenderer();

		// Maj de l'arbre
		getBinder().loadAttribute(self.getFellow("mainTreeContext"), "model");
		
		if (isAdd) {
			mainTreeContext.setSelectedItem((Treeitem) mainTreeContext
					.getLastChild().getLastChild());
		}
		
		// update hideCompleteButton
		if (hideCompleteTerminales) {
			hideCompleteButton
				.setLabel(Labels.getLabel("stockage.liste.showComplete"));
		} else {
			hideCompleteButton
			.setLabel(Labels.getLabel("stockage.liste.hideComplete"));
		}
	}

	/**
	 * Méthode qui met à jour un noeud de l'arbre contenant une enceinte.
	 * 
	 * @param enceinte
	 *            Enceinte se trouvant dans le noeud.
	 * @param cascadeUp
	 *            Si true, maj en cascade des parents
	 */
	public void updateEnceinte(Enceinte enceinte, boolean cascadeUp) {
		// remet le tree à l'état initial
		updateAllConteneurs(false);

		openEnceinte(enceinte, true);
	}

	/**
	 * Méthode qui met à jour en cascade un noeud de l'arbre contenant une
	 * enceinte.
	 * 
	 * @param item
	 *            Treeitem contenant l'enceinte.
	 * @param enceinte
	 *            Enceinte.
	 */
	public void updateEnceintebyCascade(Treeitem item, Enceinte enceinte) {
		// on met à jour le noeud de l'arbre
		EnceinteNode node = new EnceinteNode(enceinte, null, null, SessionUtils
				.getSelectedBanques(sessionScope).get(0));
		EnceinteNode oldNode = (EnceinteNode) item.getValue();
		node.setConteneur(oldNode.getConteneur());
		node.setNiveau(oldNode.getNiveau());

		try {
			ctr.render(item, node, 0);
			item.setOpen(true);
		} catch (Exception e) {
			log.error(e);
		}

		// on met à jour en cascade les noeuds parents
		if (enceinte.getEnceintePere() != null) {
			updateEnceintebyCascade(item.getParentItem(),
					enceinte.getEnceintePere());
		}
	}

	/**
	 * Méthode qui met à jour un noeud de l'arbre contenant une enceinte
	 * terminale. Possibilité de mettre à jour ses parents.
	 * 
	 * @param terminale
	 *            Terminale se trouvant dans le noeud.
	 * @param cascadeUp
	 *            Si true, maj en cascade des parents
	 */
	public void updateTerminale(Terminale terminale, boolean cascadeUp) {
		// remet le tree à l'état initial
		updateAllConteneurs(false);

		openTerminale(terminale, true, true);
	}

	/**
	 * Supprime une enceinte terminale.
	 * 
	 * @param enceinte
	 *            Enceinte parente de la terminale à supprimer.
	 * @param position
	 *            Position de la terminale à supprimer.
	 */
	public void deleteTerminale(Enceinte enceinte, Integer position) {
		// remet le tree à l'état initial
		updateAllConteneurs(false);

		// création d'une terminale vide
		Terminale terminale = new Terminale();
		terminale.setEnceinte(enceinte);
		terminale.setPosition(position);

		openTerminale(terminale, true, true);
	}

	/**
	 * Supprime une enceinte.
	 * 
	 * @param conteneur
	 *            Conteneur parent de l'enceinte à supprimer.
	 * @param enceinte
	 *            Enceinte parente de l'enceinte à supprimer.
	 * @param position
	 *            Position de la terminale à supprimer.
	 */
	public void deleteEnceinte(Conteneur conteneur, Enceinte enceinte,
			Integer position) {
		Enceinte enc = new Enceinte();
		enc.setConteneur(conteneur);
		enc.setEnceintePere(enceinte);
		enc.setPosition(position);

		// remet le tree à l'état initial
		updateAllConteneurs(false);

		openEnceinte(enc, true);
	}

	/**
	 * Cette méthode met à jour les enfants d'un Treeitem.
	 * 
	 * @param parent
	 *            Treeitem à mettre à jour.
	 */
	public void updateChildrenOfParent(Treeitem parent) {
		if (parent.getValue() instanceof ConteneurNode) {
			ConteneurNode nodeC = (ConteneurNode) parent.getValue();
			nodeC.readChildren();
			parent.removeChild(parent.getTreechildren());
			Treechildren tc = new Treechildren();
			tc.setParent(parent);
			tc = null;
		} else if (parent.getValue() instanceof EnceinteNode) {
			EnceinteNode nodeP = (EnceinteNode) parent.getValue();
			nodeP.readChildren();
			/*
			 * parent.removeChild(parent.getTreechildren()); Treechildren tc =
			 * new Treechildren(); tc.setParent(parent); tc = null;
			 */
			// parent.getTreechildren().setParent(null);
			// parent.getTreerow().getChildren().clear();
			parent.getTreechildren().getChildren().clear();
		}
	}

	/**
	 * Sélectionne un enfant d'un Treeitem en fonction de son index.
	 * 
	 * @param parent
	 *            Treeitem parent.
	 * @param position
	 *            Position de l'enfant que l'on souhaite sélectionner.
	 */
	public void selectTreeItem(Treeitem parent, Integer position) {
		List<Component> items = parent.getTreechildren().getChildren();

		if (position - 1 < items.size()) {
			selectedItem = (Treeitem) items.get(position - 1);
			mainTreeContext.setSelectedItem(selectedItem);
		}
	}

	/**
	 * Echange deux enceintes.
	 * 
	 * @param enc1
	 *            Enceinte de "départ".
	 * @param enc2
	 *            Enceinte de "destination".
	 */
	public void deplacerDeuxEnceintes(Enceinte enc1, Enceinte enc2) {
		switchToNormalMode();
		// remet le tree à l'état initial
		updateAllConteneurs(false);

		EnceinteNode oldNode = (EnceinteNode) selectedItem.getValue();
		// si aucune enceinte se trouve à la destination
		// on met le noeud à vide
		if (enc2 == null) {
			enc2 = new Enceinte();
			enc2.setEnceintePere(oldNode.getEnceinte().getEnceintePere());
			enc2.setConteneur(oldNode.getEnceinte().getConteneur());
			enc2.setPosition(oldNode.getEnceinte().getPosition());
		}

		// ouverture des deux enceintes
		openEnceinte(enc2, false);
		openEnceinte(enc1, true);
	}

	/**
	 * Echange deux enceintes terminales.
	 * 
	 * @param term1
	 *            Terminale de "départ".
	 * @param term2
	 *            Terminale de "destination".
	 */
	public void deplacerDeuxTerminales(Terminale term1, Terminale term2) {
		switchToNormalMode();
		// remet le tree à l'état initial
		updateAllConteneurs(false);

		TerminaleNode oldNode = (TerminaleNode) selectedItem.getValue();
		// si aucune terminale se trouve à la destination
		// on met le noeud à vide
		if (term2 == null) {
			term2 = new Terminale();
			term2.setEnceinte(oldNode.getTerminale().getEnceinte());
			term2.setPosition(oldNode.getTerminale().getPosition());
		}

		// ouverture des deux terminales
		openTerminale(term2, false, true);
		openTerminale(term1, true, true);
	}

	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForStockage() {

		if (getDroitOnAction("Stockage", "Creation")) {
			if (SessionUtils.getSelectedBanques(sessionScope).size() == 1) {
				addNewConteneur.setDisabled(false);
			} else {
				addNewConteneur.setDisabled(true);
			}
		} else {
			addNewConteneur.setDisabled(true);
		}


		etiquettes.setDisabled(ManagerLocator
				.getImprimanteManager()
				.findByPlateformeManager(SessionUtils
						.getPlateforme(sessionScope)).isEmpty());
	}

	/**
	 * Met une ligne en jaune.
	 */
	public void colorateRowInYellow(org.zkoss.zul.Treerow row) {
		row.setStyle("background-color : #FDDFA9");
	}

	/**
	 * Met une ligne en vert.
	 */
	public void colorateRowInGreen(org.zkoss.zul.Treerow row) {
		row.setStyle("background-color : #90EE90");
	}

	/**
	 * Met une ligne en rouge.
	 */
	public void colorateRowInRed(org.zkoss.zul.Treerow row) {
		row.setStyle("background-color : #FEBAB3");
	}
	
	/**
	 * Surligne la row en bleu léger, couleur correspondante à la sélection 
	 * dans Tree.
	 * Coloration appelée pour surlignée les terminales après recherche.
	 * @since 2.1
	 * @version 2.1
	 * @param row
	 */
	public void colorateRowInLightBlue(org.zkoss.zul.Treerow row) {
		row.setStyle("background-color : #e6f7ff");
	}
	
	public void unColorateRow(org.zkoss.zul.Treerow row) {
		row.setStyle("background: none");
	}

	public Treeitem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Treeitem selected) {
		this.selectedItem = selected;
	}

	public boolean isOldOpen() {
		return oldOpen;
	}

	public void setOldOpen(boolean old) {
		this.oldOpen = old;
	}

	public TumoTreeModel getTtm() {
		return ttm;
	}

	public void setTtm(TumoTreeModel t) {
		this.ttm = t;
	}

	public StockageTreeItemRenderer getCtr() {
		return ctr;
	}

	public void setCtr(StockageTreeItemRenderer c) {
		this.ctr = c;
	}

	public List<TumoTreeNode> getRootNodes() {
		return rootNodes;
	}

	public void setRootNodes(List<TumoTreeNode> nodes) {
		this.rootNodes = nodes;
	}

	public String getDeplacementMode() {
		return deplacementMode;
	}

	public void setDeplacementMode(String deplacement) {
		this.deplacementMode = deplacement;
	}

	public Treeitem getSelectedDestinationItem() {
		return selectedDestinationItem;
	}

	public void setSelectedDestinationItem(Treeitem selectedDestinationI) {
		this.selectedDestinationItem = selectedDestinationI;
	}

	public void onClick$etiquettes() {
		// on récupère les imprimantes associées au compte
		// pour la banque courante
//		List<AffectationImprimante> imprimantes = ManagerLocator
//				.getAffectationImprimanteManager()
//				.findByBanqueUtilisateurManager(
//						SessionUtils.getSelectedBanques(sessionScope).get(0),
//						SessionUtils.getLoggedUser(sessionScope));
//		Modele mod = null;
//		if (imprimantes.size() > 0) {
//			mod = imprimantes.get(0).getModele();
//		}
//		List<LigneEtiquette> lignes = new ArrayList<LigneEtiquette>();
//		if (mod != null) {
//			lignes = ManagerLocator.getLigneEtiquetteManager()
//					.findByModeleManager(mod);
//		}

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
				SessionUtils.getPlateforme(sessionScope), null, 
				affectation, null);
		
//		openEtiquetteWindow(getPage(), false, mod, lignes, 
//				imprimantes.get(0).getImprimante());
	}

	/**
	 * Navigue dans la liste stockage pour ouvrir la terminale qui correspond à
	 * l'emplacement passé en paramètre.
	 * 
	 * @param empl
	 */
	public void openTerminaleFromEmplacement(Emplacement empl) {
		// cree la liste remontante des contenants.
		List<Enceinte> contenants = new ArrayList<Enceinte>();
		Enceinte enc = empl.getTerminale().getEnceinte();
		Conteneur cont = null;
		while (cont == null) {
			contenants.add(0, enc);
			cont = enc.getConteneur();
			enc = enc.getEnceintePere();
		}
		
		// remet le tree à l'état initial
		// updateAllConteneurs(false);
		((TreeSelectableModel) ttm).clearSelection();
		// ttm.clearOpen();
		((TreeOpenableModel) ttm).clearOpen();
		
//		ttm.addOpenPath(ttm.getPath(new ConteneurNode(cont, null)));
//		for (Enceinte enct : contenants) {
//			ttm.addOpenPath(ttm.getPath(new EnceinteNode(enct, 
//						null, null, null)));
//		}
		
		TerminaleNode node = new TerminaleNode(empl.getTerminale(), null);
		//int[] p = ttm.getPath(node);
		int cPath = rootNodes.indexOf(new ConteneurNode(cont, null));
		int[] p2 = ttm.getPathFromNode(node, rootNodes.get(cPath), cPath);
		ttm.addSelectionPath(p2);
				
		selectedItem = mainTreeContext.renderItemByPath(p2);
		if (selectedItem == null) {
			Messagebox.show(Labels
					.getLabel("stockage.link.error"), 
						Labels.getLabel("general.warning"), 
						Messagebox.OK, Messagebox.ERROR);
		} else {
			currentObject = (TumoTreeNode) selectedItem.getValue();
			mainTreeContext.setSelectedItem(selectedItem);
			
			getStockageController().switchToFicheTerminaleMode(
											empl.getTerminale().clone(), empl);
		}
	}

	/**
	 * Navigue dans la liste stockage pour ouvrir une terminale.
	 * 
	 * @version 2.1
	 * @param terminale
	 * @param select
	 * @param reset @since 2.1 clearOpen tree si true
	 */
	public void openTerminale(Terminale terminale, boolean select, boolean reset) {
		if (terminale != null) {
			// cree la liste remontante des contenants.
			List<Enceinte> contenants = new ArrayList<Enceinte>();
			Enceinte enc = terminale.getEnceinte();
			Conteneur cont = null;
			while (cont == null) {
				contenants.add(enc);
				cont = enc.getConteneur();
				enc = enc.getEnceintePere();
			}
			
			// remet le tree à l'état initial
			if (reset) {
				// updateAllConteneurs(false);
				((TreeOpenableModel) ttm).clearOpen();
			}
			
			ttm.addOpenPath(ttm.getPath(new ConteneurNode(cont, null)));
			for (Enceinte enct : contenants) {
				ttm.addOpenPath(ttm.getPath(new EnceinteNode(enct, 
							null, null, null)));
			}
			
			TerminaleNode node = new TerminaleNode(terminale, null);
			
			ttm.addSelectionPath(ttm.getPath(node));
			
			if (select) {
				selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
				currentObject = (TerminaleNode) selectedItem.getValue();
				mainTreeContext.setSelectedItem(selectedItem);
				getStockageController().switchToFicheTerminaleMode(node
												.getTerminale().clone(), null);
			}
		}
	}
	
	/**
	 * Navigue dans la liste stockage pour ouvrir une terminale dans un mode
	 * deplacement/stockage
	 * 
	 * @param terminale
	 * @since 2.1
	 */
	public void openTerminaleForDeplacement(Terminale terminale) {
		if (terminale != null) {
			openTerminale(terminale, false, false);
			TerminaleNode node = new TerminaleNode(terminale, null);
			
			selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
			mainTreeContext.setSelectedItem(selectedItem);
		
			selectForDeplaceEnceinte();
		}
	}
	
	/**
	 * Navigue dans la liste stockage pour ouvrir une terminale dans un mode
	 * deplacement emplacement/stockage
	 * 
	 * @param terminale
	 * @since 2.1
	 */
	public void openTerminaleForDeplacementEmplacement(Terminale terminale) {
		if (terminale != null) {
			// ouvre l'arborescence vers cette terminale
			openTerminale(terminale, false, false);
			TerminaleNode node = new TerminaleNode(terminale, null);
			
			selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
			currentObject = (TerminaleNode) selectedItem.getValue();
			mainTreeContext.setSelectedItem(selectedItem);
		
			selectForDeplaceEmplacement();
		}
	}
	
	

	/**
	 * Navigue dans la liste stockage pour ouvrir une terminale.
	 * 
	 * @param empl
	 */
	public void openEnceinte(Enceinte enceinte, boolean select) {
		// cree la liste remontante des contenants.
		List<Enceinte> contenants = new ArrayList<Enceinte>();
		Enceinte enc = enceinte.getEnceintePere();
		Conteneur cont = enceinte.getConteneur();
		while (cont == null) {
			contenants.add(enc);
			cont = enc.getConteneur();
			enc = enc.getEnceintePere();
		}
		
		ttm.addOpenPath(ttm.getPath(new ConteneurNode(cont, null)));
		for (Enceinte enct : contenants) {
			ttm.addOpenPath(ttm.getPath(new EnceinteNode(enct, 
						null, null, null)));
		}
		
		EnceinteNode node = new EnceinteNode(enceinte, null, null, null);
		
		ttm.addOpenPath(ttm.getPath(node));
		ttm.addSelectionPath(ttm.getPath(node));
		
		
		if (select) {
			selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
			currentObject = (EnceinteNode) selectedItem.getValue();
			mainTreeContext.setSelectedItem(selectedItem);
			// getStockageController().switchToFicheEnceinteMode(
			//		((EnceinteNode) selectedItem.getValue()).getEnceinte().clone());
			getStockageController().switchToFicheEnceinteMode(enceinte.clone());
		}
		
		

		// parcoure l'arborescence en commencant par le conteneur
//		Iterator<Treeitem> contItemsIt = (Iterator<Treeitem>) mainTreeContext
//				.getItems().iterator();
//		ConteneurNode contNode = null;
//		Treeitem contItem = null;
//		while (contItemsIt.hasNext()) {
//			contItem = contItemsIt.next();
//			contNode = (ConteneurNode) contItem.getValue();
//			if (cont.equals(contNode.getConteneur())) {
//				break;
//			}
//		}
//		if (contItem != null) {
//			// enceintes
//			mainTreeContext.setSelectedItem(contItem);
//			contItem.setOpen(true);
//			Treeitem parentIt = contItem;
//			for (int i = contenants.size() - 1; i >= 0; i--) {
//				selectTreeItem(parentIt, contenants.get(i).getPosition());
//				parentIt = mainTreeContext.getSelectedItem();
//				parentIt.setOpen(true);
//			}
//
//			if (select) {
//				selectTreeItem(parentIt, enceinte.getPosition());
//
//				currentObject = (EnceinteNode) selectedItem.getValue();
//				getStockageController().switchToFicheEnceinteMode(
//						((EnceinteNode) selectedItem.getValue()).getEnceinte()
//								.clone());
//
//				selectedItem.setOpen(true);
//			}
//		}
	}

	public void disableToolbar(boolean b) {
		addNewConteneur.setDisabled(b);
		etiquettes.setDisabled(b);
	}

	public Enceinte getSelectedEnceinte() {
		return selectedEnceinte;
	}

	public void setSelectedEnceinte(Enceinte _s) {
		this.selectedEnceinte = _s;
	}

	public Terminale getSelectedTerminale() {
		return selectedTerminale;
	}

	public void setSelectedTerminale(Terminale _t) {
		this.selectedTerminale = _t;
	}

	public Conteneur getSelectedConteneur() {
		return selectedConteneur;
	}

	public void setSelectedConteneur(Conteneur _c) {
		this.selectedConteneur = _c;
	}

	/**
	 * Sélectionne et affiche dans l'arborescence la terminale 
	 * à partir du scan, en mode view.
	 * @param TKScanDTO dto
	 * @since 2.1
	 * @version 2.1
	 */
	public void selectTerminaleFromScan(TKScanTerminaleDTO dto) {
		if (dto.getTerminale() != null) {
			Events.echoEvent("onLaterScanTerminale", self, dto);
			Clients.showBusy(self, Labels.getLabel("general.display.wait"));
		}
	}
	
	public void onLaterScanTerminale(Event e) {
		TKScanTerminaleDTO dto = (TKScanTerminaleDTO) e.getData(); 
		if (deplacementMode.equals("deplacementEmplacement")) {
			openTerminaleForDeplacementEmplacement(dto.getTerminale());
		} else if (deplacementMode.equals("deplacement")
				&& currentObject instanceof TerminaleNode) {
			openTerminale(dto.getTerminale(), false, false);
			selectForDeplaceEnceinte();
		} else {				
			openTerminale(dto.getTerminale(), true, false);
			getStockageController().getFicheTerminale().applyChecksOnScan(dto);
			
		}
		Clients.clearBusy(self);
	}

	public List<Conteneur> getRootConteneurs() {
		return rootConteneurs;
	}
	
	/**
	 * Renvoie la liste de conteneurs accessibles OU le conteneur 
	 * selectionné si il existe.
	 * @since 2.1
	 * @version 2.1
	 * @return liste Conteneur
	 */
	public List<Conteneur> getSelectedConteneurs() {
		List<Conteneur> selConts = new ArrayList<Conteneur>();
		if (getSelectedConteneur() != null) {
			selConts.add(getSelectedConteneur());
		} else { // all available conteneurs
			selConts = getRootConteneurs();
		}
		return selConts;
	}
	
	public void onPressEnterKey() {
		onClick$findTerminaleBoxButton();
	}
	
	/**
	 * Déplie dans l'arborescence tous les noeuds representant 
	 * aux terminales dont le nom correspond à la valeur recherchée.
	 * @since 2.1
	 * @version 2.1 
	 */
	public void onClick$findTerminaleBoxButton() {
		cleanRowsColor(); 
		String tNameToSearch = findTerminaleBox.getValue();
		if (tNameToSearch != null) {
			Events.echoEvent("onLaterTerminaleSearch", self, tNameToSearch);
			Clients.showBusy(self, Labels.getLabel("general.display.wait"));
		}
	}
	
	public void onLaterTerminaleSearch(Event e) {
		List<Integer> termIds = ManagerLocator.getTerminaleManager()
				.findTerminaleIdsFromNomManager((String) e.getData(), 
						getSelectedEnceinte(), getSelectedConteneurs());
		
		if (!termIds.isEmpty()) {
		
			Terminale term;
			for (int i = 0; i < termIds.size(); i++) {
				term = ManagerLocator.getTerminaleManager().findByIdManager(termIds.get(i));
				
				// select 
				if (i == termIds.size() - 1) {
					if (deplacementMode.equals("deplacementEmplacement")) {
						openTerminaleForDeplacementEmplacement(term);
					} else if (deplacementMode.equals("deplacement")) {
						openTerminaleForDeplacement(term);
					} else {
						openTerminale(term, true, false);
					}
				} else {
					openTerminale(term, false, false);
				}
				searchPaths.add(ttm.getSelectionPath());
			}
			
			// color results;
			for (int[] path : searchPaths) {
				colorateRowInLightBlue(
			    	((Treeitem) mainTreeContext.renderItemByPath(path)).getTreerow());
			}
		} else { // notification
			Clients.showNotification(ObjectTypesFormatters.getLabel("search.terminale.notfound.warning", 
					new String[] {(String) e.getData(),
					(getSelectedEnceinte() != null ? getSelectedEnceinte().getNom() : 
						getSelectedConteneur() != null ? getSelectedConteneur().getCode() : "")}),
					"warning", null, null, 2000, true);
		}
			
		Clients.clearBusy(self);
	}
	
	private void cleanRowsColor() {
		// color results;
		for (int[] path : searchPaths) {
		     unColorateRow(
		    	((Treeitem) mainTreeContext.renderItemByPath(path)).getTreerow());
		}
		searchPaths.clear();
	}
	
	/**
	 * Reset le tree
	 * @since 2.1
	 * @version 2.1 
	 */
	public void onClick$resetTree() {
//		((TreeOpenableModel) ttm).clearOpen();
//		ttm.clearSelection();
//		
//		setSelectedConteneur(null);
//		setSelectedEnceinte(null);
//		setSelectedTerminale(null);
		updateAllConteneurs(false);
		
		if (!deplacementMode.equals("deplacementEmplacement")) {
			switchToNormalMode();
				getStockageController().clearAllPage();
		}
	}
}
