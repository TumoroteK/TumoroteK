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
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.function.Consumer;

import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.ext.TreeOpenableModel;
import org.zkoss.zul.ext.TreeSelectableModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.utils.AffichageUtils;
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
 * @version 2.2.3-genno
 * @author Mathieu BARTHELEMY
 *
 */
public class ListeStockages extends AbstractController
{

	private final Logger log = LoggerFactory.getLogger(ListeStockages.class);

	private static final long serialVersionUID = -836695461692709048L;

	// @since 2.1
	List<Conteneur> rootConteneurs = new ArrayList<>();

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
	List<int[]> searchPaths = new ArrayList<>();
	// TK-314 : mémorisation des paths correspondant aux conteneurs interdits pour annuler le passage en rouge fait éventuellement lors de l'appel précédent
	List<int[]> forbiddenConteneurPaths = new ArrayList<>();

   // @since 2.2.1
	// TK-251
	private List<Banque> restrictedBanqueDeplacement = 
			new ArrayList<Banque>();
	
	// @since 2.2.3-genno
	private Plateforme curPf;

	//lié au bouton "Activer le filtre des places vides" au niveau de la liste des conteneurs dans le cas d'un stockage
	private boolean hideCompleteTerminales = false;

	/**
	 * Variables pour l'arbre.
	 */
	private TumoTreeModel ttm;
	private StockageTreeItemRenderer ctr;
	// liste de noeuds principaux de l'arbre
	private List<TumoTreeNode> rootNodes = new ArrayList<>();

	private String deplacementMode = "normal";
	private Object currentObject;

	private AnnotateDataBinder stockageBinder;

	// @since 2.1
	private Conteneur selectedConteneur;
	private Enceinte selectedEnceinte;
	private Terminale selectedTerminale;

	private int maxContainersToPrint;


	@Override
	public AnnotateDataBinder getBinder(){
		return stockageBinder;
	}

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		curPf = SessionUtils.getCurrentPlateforme();

		final ParametresManager parametresManager = ManagerLocator.getManager(ParametresManager.class);

		ParametreValeurSpecifique maxParametre = parametresManager.findParametresByPlateformeIdAndCode(
				curPf.getPlateformeId(),
				EParametreValeurParDefaut.STOCKAGE_NB_MAX_CONTENEUR_A_IMPRIMER.getCode());
		maxContainersToPrint = Integer.parseInt(maxParametre.getValeur());

		stockageBinder = new AnnotateDataBinder(comp);
		stockageBinder.loadAll();

		// Init du noeud root de l'arbre
		final StockageRootNode root = 
				new StockageRootNode(SessionUtils.getSelectedBanques(sessionScope), curPf);
		root.readChildren();
		rootNodes = root.getChildren();
		//@since 2.1
		getRootConteneurs().addAll(root.getConteneurs());

		// Init de l'arbre et de son affichage
		ttm = new TumoTreeModel(root);
		ctr = new StockageTreeItemRenderer();

		drawActionsForStockage();

		final int height = getMainWindow().getListPanelHeight() + 145;
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
	public StockageController getStockageController(){
		return stockageController;
	}

	public void setStockageController(final StockageController sc){
		this.stockageController = sc;
	}

	/**
	 * Passe la liste en mode "déplacement", à partir d'une enceinte 
	 * ou d'une terminale.
	 * @since 2.2.1 TK-251: Utilise toutes les banques représentées par le contenu de 
	 * l'enceinte ou la terminale afin de restreindre les destinations possibles aux conteneurs 
	 * accessibles
	 * @since 2.2.1-IRELEC retire de la liste tout les conteneurs dont la modification 
	 * de structure et de contenu sont restreintes à la plateforme d'origine.
	 * @params banks
	 * @version 2.2.1
	 */
	public void switchToDeplacementMode(List<Banque> banks){
		deplacementMode = "deplacement";
		menuBar.setVisible(false);
		hideCompleteButton.setVisible(false);
		hideCompleteTerminales = false;
		
		//TK-314
		manageForbiddenConteneurs();

		restrictedBanqueDeplacement.clear();
		restrictedBanqueDeplacement.addAll(banks);


		// récupère la ligne sélectionnée et la colore en jaune
		final org.zkoss.zul.Treerow row = selectedItem.getTreerow();
		AffichageUtils.colorateRowInYellow(row);      
	}

	/**
	 * Passe la liste en mode "déplacement emplacement".
	 */
	public void switchToDeplacementEmplacementMode(){
		deplacementMode = "deplacementEmplacement";
		menuBar.setVisible(false);
		hideCompleteButton.setVisible(false);
		hideCompleteTerminales = false;
		
		//TK-314
		//déselection de treeitem courant
		mainTreeContext.setSelectedItem(null);
		currentObject = null;
		manageForbiddenConteneurs();
	}

	/**
	 * @version 2.2.1-IRELEC
	 */
	public void switchToStockerMode(){
		switchToDeplacementEmplacementMode();
		hideCompleteButton.setVisible(true);
	}
	
	
	//TK-314 : gère l'affichage des conteneurs interdits au stockage dans l'arbre des conteneurs pour un cas d'utilisation de stockage ou de déplacement :
	// - désactive l'accès au conteneur
	// - ajoute un sens interdit après le nom du conteneur
	private void manageForbiddenConteneurs() {
      //réinitialisation avant traitement :
      forbiddenConteneurPaths=new ArrayList<>();
	   
	   //vérification qu'on est bien dans le cas de déplacement :
	   if(isDeplacementOrDeplacementEmplacementMode()) {
	      List<ConteneurNode> listForbiddenConteneurNode = new ArrayList<ConteneurNode>();
	      for(TumoTreeNode conteneurNode : rootNodes) {
           //par sécurité, vérification qu'on est bien sur un ConteneurNode :
           if(conteneurNode instanceof ConteneurNode) {
              Conteneur conteneur = ((ConteneurNode)conteneurNode).getConteneur();
              //Le conteneur est interdit si :
              //- il appartient à une plateforme différente de la plateforme courante
              //- le partage du conteneur est restreint pour le stockage déplacement aux admin
              //- l'utilisateur connecté n'est pas admin
              if(!isAccessibleConteneurForCurrentPlateform(conteneur)) {
                 //mémorisation du path pour gérer le nettoyage de l'affichage passé eventuellement en rouge si l'utilisateur l'a sélectionné
                 int[] conteneurPath = ttm.getConteneurPath((ConteneurNode)conteneurNode);
                 forbiddenConteneurPaths.add(conteneurPath);
                
                 Treeitem currentItem = mainTreeContext.renderItemByPath(conteneurPath);
                 //nettoyage avant ajout pour ne pas ajouter en double :
                 AffichageUtils.removeFirstImageInTreerow(currentItem.getTreerow());
                 AffichageUtils.addImageInterditToTreecell((Treecell) currentItem.getTreerow().getFirstChild());
                 currentItem.setDisabled(true);
                 currentItem.setOpen(false);

              }
           }
   	   }
	   } 
	}

	public void onClick$hideCompleteButton(){
		hideCompleteTerminales = !hideCompleteTerminales;
		searchPaths.clear();
		setSelectedConteneur(null);
		setSelectedDestinationItem(null);
		setSelectedEnceinte(null);
		updateAllConteneurs(false);
		
	   //TK-314 :
	   Events.postEvent(new Event("onLaterUpdateAllContainerToManageForbiddenConteneurs", self, null));
      
	}

	 //TK-314 :
	 /**
    * Permet de gérer l'affichage des conteneurs "interdits" après l'appel d'un updateAllConteneurs qui a entrainé le passage dans StockageTreeItemRenderer 
    * et donc la réinialisation du Tree des conteneurs.
    * 
    */
	public void onLaterUpdateAllContainerToManageForbiddenConteneurs(final Event e) {
	   manageForbiddenConteneurs();
	}
	
	/**
	 * Passe la liste en mode "normal".
	 * @param afterDone : true si l'appel de la méthode se fait dans le cadre d'une validation. 
	 * un appel à updateAllConteneur sera alors fait. false s'il c'est dans le cadre d'une annulation
	 * 
	 */
	public void switchToNormalMode(boolean afterDone){

		restrictedBanqueDeplacement.clear();

		deplacementMode = "normal";
		menuBar.setVisible(true);
		hideCompleteButton.setVisible(false);
		hideCompleteTerminales = false;
		
		// on met les deux lignes en blanc
		if(selectedItem != null){
			final org.zkoss.zul.Treerow row = selectedItem.getTreerow();
			row.setStyle(null);
			row.invalidate();
		}

		if(selectedDestinationItem != null){
			final org.zkoss.zul.Treerow row2 = selectedDestinationItem.getTreerow();
			row2.setStyle(null);
			row2.invalidate();
			selectedDestinationItem = null;
		}

		mainTreeContext.setSelectedItem(null);
		
	   //TK-314
		if(afterDone) {
		   //on est dans le cadre d'une validation donc il faut réinitialiser les conteneurs
		   updateAllConteneurs(false);
		}
		else {
		   //on est dans le cadre d'une annulation, il faut juste nettoyer l'affichage des conteneurs interdits
		   cleanAffichageForbiddenConteneurs();
		}
	}

	//TK-314 : réinitialise tout ce qui a pu être fait au niveau affichage concernant la restriction des conteneurs partagés
	private void cleanAffichageForbiddenConteneurs() {
	   redrawTreeForbiddenConteneur();
      forbiddenConteneurPaths.clear();
	}
	
	//TK-314 : réinitialise l'affichage des conteneurs interdits : ils redeviennent alors accessibles
   private void redrawTreeForbiddenConteneur() {
      try {
         if (ttm!=null) {
            for(final int[] path : forbiddenConteneurPaths){
               if(path != null){
                  Treeitem conteneurItem = mainTreeContext.renderItemByPath(path);
                  ctr.render(conteneurItem, ttm.getChild(ttm.getRoot(), path[0]), path[0]);
               }
            }
         }
      }
      catch(Exception e){
         log.error(e.getMessage(), e); 
      }
   }  
	
	/**
	 * Lors de la sélection d'un noeud de l'arbre, nous afficherons la fiche
	 * correspondant à cet élément.
	 * @version 2.1
	 */
	public void onSelect$mainTreeContext(){

		// since 2.1
	   cleanBlueRowsColor();
	   //TK-314
	   cleanRedRowsColorForForbiddenConteneurs();
		setSelectedConteneur(null);
		setSelectedEnceinte(null);
		setSelectedTerminale(null);

		// Mode normal
		if(deplacementMode.equals("normal")){
			selectForView();
			// Mode déplacement
		}else if(deplacementMode.equals("deplacement")){
			selectForDeplaceEnceinte();
		}else if(deplacementMode.equals("deplacementEmplacement")){
			selectForDeplaceEmplacement();
		}
	}

	public void onClick$deplacerEmplacements(){
		getStockageController().switchToDeplacerEmplacementsMode(null, null);
	}

	/**
	 * Sélectionne un item pour le visualiser.
	 */
	public void selectForView(){
		if(mainTreeContext.getSelectedItem() != null){
			currentObject = mainTreeContext.getSelectedItem().getValue();
		}
		selectedItem = mainTreeContext.getSelectedItem();

		// Si c'est un noeud conteneur => FicheConteneur
		// Si c'est un noeud enceinte => FicheEnceinte
		// Si c'est un noeud terminale => FicheTerminale
		if(currentObject instanceof ConteneurNode){

			final ConteneurNode node = (ConteneurNode) currentObject;
			getStockageController().switchToFicheConteneurMode(node.getConteneur().clone());
			// @since 2.1
			setSelectedConteneur(((ConteneurNode) currentObject).getConteneur());
		}else if(currentObject instanceof EnceinteNode){
			final EnceinteNode node = (EnceinteNode) currentObject;
			getStockageController().switchToFicheEnceinteMode(node.getEnceinte().clone());
			// @since 2.1
			setSelectedEnceinte(((EnceinteNode) currentObject).getEnceinte());
		}else if(currentObject instanceof TerminaleNode){
			final TerminaleNode node = (TerminaleNode) currentObject;
			getStockageController().switchToFicheTerminaleMode(node.getTerminale().clone(), null);
		}
	}

	/**
	 * Sélectionne un item pour le déplacer.
	 */
	public void selectForDeplaceEnceinte(){
		// si une destination était sélectionnée, on la déselctionne
		if(selectedDestinationItem != null){
			final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
			row.setStyle(null);
			row.invalidate();
		}

		// on récupère la destination
		final Object obj = mainTreeContext.getSelectedItem().getValue();
		selectedDestinationItem = mainTreeContext.getSelectedItem();


		// si l'objet à déplacer est une enceinte
		if(currentObject instanceof EnceinteNode){
			// la destination doit être une enceinte
			if(obj instanceof EnceinteNode){
				final EnceinteNode nodeDep = (EnceinteNode) currentObject;
				final EnceinteNode nodeDest = (EnceinteNode) obj;

				// la destination doit être différente du départ
				if(!nodeDep.equals(nodeDest)){
					// la destination doit être valide
					if(nodeDep.getConteneur().getNbrNiv().equals(nodeDest.getConteneur().getNbrNiv())
							&& nodeDep.getNiveau() == nodeDest.getNiveau()){
						
						// TK-251 conteneur banks coherence
						if (checkConteneurDeplacementCoherence(nodeDest)) {
							// on met la destination en vert
							final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
							AffichageUtils.colorateRowInGreen(row);
							// on l'envoie à la fiche enceinte
							getStockageController().getFicheEnceinte().definirEnceinteDestination(nodeDest, null, null);
						}
					}else{
						// on met la destination en rouge
						final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
						AffichageUtils.colorateRowInRed(row);
						getStockageController().getFicheEnceinte().definirEnceinteDestination(null, null, null);
					}
				}else{
					selectedDestinationItem = null;
				}

			}else{
				// on met la destination en rouge
				final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
				AffichageUtils.colorateRowInRed(row);
				getStockageController().getFicheEnceinte().definirEnceinteDestination(null, null, null);
			}

		}else if(currentObject instanceof TerminaleNode){
			// la destination doit être une boite
			if(obj instanceof TerminaleNode){
				final TerminaleNode nodeDep = (TerminaleNode) currentObject;
				final TerminaleNode nodeDest = (TerminaleNode) obj;

				// la destination doit être différente du départ
				if(!nodeDep.equals(nodeDest)){
					// TK-251 conteneur banks coherence
					if (checkConteneurDeplacementCoherence(nodeDest)) {
						// on met la destination en vert
						final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
						AffichageUtils.colorateRowInGreen(row);
						// on l'envoie à la fiche enceinte
						getStockageController().getFicheTerminale().definirTerminaleDestination(nodeDest, null, null);
					}
				}else{
					selectedDestinationItem = null;
				}

			}else{
				// on met la destination en rouge
				final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
				AffichageUtils.colorateRowInRed(row);
				getStockageController().getFicheTerminale().definirTerminaleDestination(null, null, null);

				// since 2.1
				// afin de permettre la restriction par arborescence 
				// sur une recherche
				if(obj instanceof ConteneurNode){
					setSelectedConteneur(((ConteneurNode) obj).getConteneur());
				}else if(obj instanceof EnceinteNode){
					setSelectedEnceinte(((EnceinteNode) obj).getEnceinte());
				}
			}
		}
	}
	
	// TK-251
	// check conteneurs accessibles aux banques du contenu 
	// verifie si les banques auxquelles les conteneurs de destination 
	// et départ sont assignés 
	// contiennent au moins toutes les banques représentant le contenu 
	// du couple enceintes/terminales qui fait le déplacement
	private boolean checkConteneurDeplacementCoherence (TumoTreeNode destObj) {
	   int[] departContPath = ttm.getPath((TumoTreeNode) currentObject);
	   Conteneur departCont = getRootConteneurs().get(departContPath[0]);
		Conteneur destCont = getRootConteneurs().get(ttm.getSelectionPath()[0]);
	
		Set<Banque> departContBanks = ManagerLocator.getConteneurManager()
				.getBanquesManager(departCont);
		Set<Banque> destContBanks = ManagerLocator.getConteneurManager()
				.getBanquesManager(destCont);
	
		// current objects banks
		List<Banque> movedObjectBanks = destObj instanceof EnceinteNode ? 
			ManagerLocator.getEnceinteManager().getDistinctBanquesFromTkObjectsManager(((EnceinteNode) destObj).getEnceinte())
			: ManagerLocator.getTerminaleManager().getDistinctBanquesFromTkObjectsManager(((TerminaleNode) destObj).getTerminale());
		
		Conteneur unavailableConteneur = null;
		List<Banque> missingBanks = null;
		// le conteneur de destination est-il accessible par les collections 
		// représentées par le contenu de la boite déplacée
		if (!destContBanks.containsAll(restrictedBanqueDeplacement)) {
			unavailableConteneur = destCont;
			missingBanks = restrictedBanqueDeplacement;
		} else if (!departContBanks.containsAll(movedObjectBanks)) {
			unavailableConteneur = departCont;
			missingBanks = movedObjectBanks;
		}
				
		if (unavailableConteneur != null) { // incoherence -> on met la destination en rouge
			final org.zkoss.zul.Treerow row = selectedDestinationItem.getTreerow();
			AffichageUtils.colorateRowInRed(row);
			if(currentObject instanceof EnceinteNode){
				getStockageController().getFicheEnceinte()
					.definirEnceinteDestination(null, unavailableConteneur, missingBanks);
			} else {
				getStockageController().getFicheTerminale()
					.definirTerminaleDestination(null, unavailableConteneur, missingBanks);
			}
			return false;
		}
			
		return true;
	}

	/**
	 * Depuis version 2.1, conserve le conteneur ou l'enceinte selectionnée afin 
	 * d'être réutilisable lors d'un full-rack scan
	 * @version 2.1
	 */
	public void selectForDeplaceEmplacement(){
		currentObject = mainTreeContext.getSelectedItem().getValue();
		selectedItem = mainTreeContext.getSelectedItem();

		if(currentObject instanceof TerminaleNode){
			final TerminaleNode node = (TerminaleNode) currentObject;
			getStockageController().getFicheDeplacerEmplacements().definirTerminaleDestination(node, 
					getRootConteneurs().get(ttm.getSelectionPath()[0]), null);
		}else if(currentObject instanceof ConteneurNode){
			setSelectedConteneur(((ConteneurNode) currentObject).getConteneur());
		}else if(currentObject instanceof EnceinteNode){
			setSelectedEnceinte(((EnceinteNode) currentObject).getEnceinte());
		}
	}

	/**
	 * Création d'un nouveau conteneur.
	 */
	public void onClick$addNewConteneur(){
		getStockageController().switchToFicheConteneurCreateMode();
	}

	/**
	 * Méthode appelée lorsqu'on clique pour générer le plan avec les boites.
	 *
	 */
	public void onClick$generateWithBoxes(){
		// Création des arguments pour la fenêtre en incluant les cases à cocher
		Map<String, Object> windowArgs = createSelectionWindowArguments(true);

		// Création de la fenêtre modale avec les arguments spécifiés
		Window selectionWindow = (Window) Executions.createComponents("/zuls/modales/SelectionModale.zul", null, windowArgs);

		// Affichage de la fenêtre modale en mode modal
		selectionWindow.doModal();	}


	/**
	 * Méthode appelée lorsqu'on clique pour générer le plan sans les boites.
	 *
	 */
	public void onClick$generateWithoutBoxes(){
		Map<String, Object> windowArgs = createSelectionWindowArguments(false);
		Window selectionWindow = (Window) Executions.createComponents("/zuls/modales/SelectionModale.zul", null, windowArgs);
		selectionWindow.doModal();
	}


	/**
	 * Création des arguments pour la fenêtre de sélection.
	 *
	 * @param avecBoites 
	 * @return Map contenant les arguments nécessaires pour créer la fenêtre de sélection.
	 */
	public Map<String, Object> createSelectionWindowArguments(boolean avecBoites){
		Map<String, Object> windowArgs = new HashMap<>();

		// Détermination du titre de la fenêtre en fonction de la présence des boites
		String titleKey = avecBoites ? "stockage.generate.with.boite" : "stockage.generate.without.boite";
		windowArgs.put("title", Labels.getLabel(titleKey));

		// Ajout des autres labels nécessaires pour la fenêtre SelectionModale.zul
		windowArgs.put("mainLabel", Labels.getLabel("stockage.selection.window.mainLabel"));
		System.out.println(Labels.getLabel("stockage.selection.window.mainLabel"));
		windowArgs.put("listHeaderLabel", Labels.getLabel("stockage.selection.window.listHeaderLabel"));
		windowArgs.put("itemList", Arrays.asList("Mary", "John", "Jane", "Henry", "Mark", "Jeffery", "Rebecca"));


		windowArgs.put("selectedLabel", Labels.getLabel("stockage.selection.window.selectedLabel", new String[] {String.valueOf(maxContainersToPrint)}));
		windowArgs.put("max", maxContainersToPrint);

		if (avecBoites) {
			windowArgs.put("callback", (Consumer<List<String>>) selectedItems -> {
				Clients.showNotification("You clicked on 'Generate With Boxes' " + selectedItems, "info", null, "middle_center", 3000);
			});
		}

		windowArgs.put("callback", (Consumer<List<String>>) selectedItems -> {
			Clients.showNotification("Selected items: " + selectedItems);
		});

		return windowArgs;
	}

	public void updateConteneur(final Conteneur conteneur){

		final ConteneurNode node = new ConteneurNode(conteneur, null, curPf);

		((TreeOpenableModel) ttm).clearOpen();
		selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));

		ttm.addOpenPath(ttm.getPath(node));
	}

	/**
	 * Mets à jour la liste des conteneurs
	 * @version 2.1
	 */
	public void updateAllConteneurs(final boolean isAdd){
	   // Init du noeud root de l'arbre
		final StockageRootNode root = 
				new StockageRootNode(SessionUtils.getSelectedBanques(sessionScope), curPf);
		root.setHideComplete(hideCompleteTerminales);
		root.readChildren();
		rootNodes = root.getChildren();
		// @since 2.1
		setSelectedConteneur(null);
		setSelectedEnceinte(null);
		getRootConteneurs().clear();
		getRootConteneurs().addAll(root.getConteneurs());
	
		restrictedBanqueDeplacement.clear();
	
		// Init de l'arbre et de son affichage
		ttm = new TumoTreeModel(root);
		
		//TK-314 : mise en commentaire de la réinstantiation de ctr 
		//En effet, cet objet n'a pas d'état donc ça ne sert à rien et il est apparu lors d'un test que si ctr avait un état, le fait de le réiniatiliser
		//n'impacte pas l'instance attachée à la page courante
		//ctr = new StockageTreeItemRenderer();
				

		// Maj de l'arbre
		getBinder().loadAttribute(self.getFellow("mainTreeContext"), "model");
	
		if(isAdd){
			mainTreeContext.setSelectedItem((Treeitem) mainTreeContext.getLastChild().getLastChild());
		}

		// update hideCompleteButton
		if(hideCompleteTerminales){
			hideCompleteButton.setLabel(Labels.getLabel("stockage.liste.showComplete"));
		}else{
			hideCompleteButton.setLabel(Labels.getLabel("stockage.liste.hideComplete"));
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
	public void updateEnceinte(final Enceinte enceinte, final boolean cascadeUp){
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
	public void updateEnceintebyCascade(final Treeitem item, final Enceinte enceinte){
		// on met à jour le noeud de l'arbre
		final EnceinteNode node = new EnceinteNode(enceinte, null, null, SessionUtils.getSelectedBanques(sessionScope).get(0));
		final EnceinteNode oldNode = (EnceinteNode) item.getValue();
		node.setConteneur(oldNode.getConteneur());
		node.setNiveau(oldNode.getNiveau());

		try{
			ctr.render(item, node, 0);
			item.setOpen(true);
		}catch(final Exception e){
			log.error(e.getMessage(), e); 
		}

		// on met à jour en cascade les noeuds parents
		if(enceinte.getEnceintePere() != null){
			updateEnceintebyCascade(item.getParentItem(), enceinte.getEnceintePere());
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
	public void updateTerminale(final Terminale terminale, final boolean cascadeUp){
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
	public void deleteTerminale(final Enceinte enceinte, final Integer position){
		// remet le tree à l'état initial
		updateAllConteneurs(false);

		// création d'une terminale vide
		final Terminale terminale = new Terminale();
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
	public void deleteEnceinte(final Conteneur conteneur, final Enceinte enceinte, final Integer position){
		final Enceinte enc = new Enceinte();
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
	public void updateChildrenOfParent(final Treeitem parent){
		if(parent.getValue() instanceof ConteneurNode){
			final ConteneurNode nodeC = (ConteneurNode) parent.getValue();
			nodeC.readChildren();
			parent.removeChild(parent.getTreechildren());
			Treechildren tc = new Treechildren();
			tc.setParent(parent);
			tc = null;
		}else if(parent.getValue() instanceof EnceinteNode){
			final EnceinteNode nodeP = (EnceinteNode) parent.getValue();
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
	public void selectTreeItem(final Treeitem parent, final Integer position){
		final List<Component> items = parent.getTreechildren().getChildren();

		if(position - 1 < items.size()){
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
	public void deplacerDeuxEnceintes(final Enceinte enc1, Enceinte enc2){
		switchToNormalMode(true);

		final EnceinteNode oldNode = (EnceinteNode) selectedItem.getValue();
		// si aucune enceinte se trouve à la destination
		// on met le noeud à vide
		if(enc2 == null){
			enc2 = new Enceinte();
			enc2.setEnceintePere(oldNode.getEnceinte().getEnceintePere());
			enc2.setConteneur(oldNode.getEnceinte().getConteneur());
			enc2.setPosition(oldNode.getEnceinte().getPosition());
		}

     //TK-315 : on passe par un Event pour que le renderer qui redessine le Tree soit appelé avant de gérer les conséquences du 
     //déplacement au niveau affichage du Tree des conteneurs 
     //(il est déclenché par updateAllConteneurs() appelé dans switchToNormalMode())
     List<Enceinte> enceintesConcernees = new ArrayList<Enceinte>();
     enceintesConcernees.add(enc1);
     enceintesConcernees.add(enc2);
     Events.postEvent("onLaterDeplacementDeuxEnceintes", self, enceintesConcernees);
	}

	/**
	 * Echange deux enceintes terminales.
	 * 
	 * @param term1
	 *            Terminale de "départ".
	 * @param term2
	 *            Terminale de "destination".
	 */
	public void deplacerDeuxTerminales(final Terminale term1, Terminale term2){
	   switchToNormalMode(true);

		final TerminaleNode oldNode = (TerminaleNode) selectedItem.getValue();
		// si aucune terminale se trouve à la destination
		// on met le noeud à vide
		if(term2 == null){
			term2 = new Terminale();
			term2.setEnceinte(oldNode.getTerminale().getEnceinte());
			term2.setPosition(oldNode.getTerminale().getPosition());
		}

	    //TK-315 : on passe par un Event pour que le renderer qui redessine le Tree soit appelé avant de gérer les conséquences du 
		//déplacement au niveau affichage du Tree des conteneurs
      //(il est déclenché par updateAllConteneurs() appelé dans switchToNormalMode())
      List<Terminale> terminalesConcernees = new ArrayList<Terminale>();
      terminalesConcernees.add(term1);
      terminalesConcernees.add(term2);
      Events.postEvent("onLaterDeplacementDeuxTerminales", self, terminalesConcernees);
	}

	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForStockage(){

		if(getDroitOnAction("Stockage", "Creation")){
			if(SessionUtils.getSelectedBanques(sessionScope).size() == 1){
				addNewConteneur.setDisabled(false);
			}else{
				addNewConteneur.setDisabled(true);
			}
		}else{
			addNewConteneur.setDisabled(true);
		}

		etiquettes.setDisabled(
				ManagerLocator.getImprimanteManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope)).isEmpty());
	}

	public Treeitem getSelectedItem(){
		return selectedItem;
	}

	public void setSelectedItem(final Treeitem selected){
		this.selectedItem = selected;
	}

	public boolean isOldOpen(){
		return oldOpen;
	}

	public void setOldOpen(final boolean old){
		this.oldOpen = old;
	}

	public TumoTreeModel getTtm(){
		return ttm;
	}

	public void setTtm(final TumoTreeModel t){
		this.ttm = t;
	}

	public StockageTreeItemRenderer getCtr(){
		return ctr;
	}

	public void setCtr(final StockageTreeItemRenderer c){
		this.ctr = c;
	}

	public List<TumoTreeNode> getRootNodes(){
		return rootNodes;
	}

	public void setRootNodes(final List<TumoTreeNode> nodes){
		this.rootNodes = nodes;
	}

	public String getDeplacementMode(){
		return deplacementMode;
	}

	public void setDeplacementMode(final String deplacement){
		this.deplacementMode = deplacement;
	}

	public Treeitem getSelectedDestinationItem(){
		return selectedDestinationItem;
	}

	public void setSelectedDestinationItem(final Treeitem selectedDestinationI){
		this.selectedDestinationItem = selectedDestinationI;
	}

	public void onClick$etiquettes(){
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

		final List<AffectationImprimante> affs = ManagerLocator.getAffectationImprimanteManager().findByBanqueUtilisateurManager(
				SessionUtils.getSelectedBanques(sessionScope).get(0), SessionUtils.getLoggedUser(sessionScope));
		if(affs.size() > 0){
			affectation = affs.get(0);
		}

		openImprimanteModeleModale(SessionUtils.getPlateforme(sessionScope), null, affectation, null);

		//		openEtiquetteWindow(getPage(), false, mod, lignes, 
		//				imprimantes.get(0).getImprimante());
	}

	/**
	 * Navigue dans la liste stockage pour ouvrir la terminale qui correspond à
	 * l'emplacement passé en paramètre.
	 * 
	 * @param empl
	 */
	public void openTerminaleFromEmplacement(final Emplacement empl){
		// cree la liste remontante des contenants.
		final List<Enceinte> contenants = new ArrayList<>();
		Enceinte enc = empl.getTerminale().getEnceinte();
		Conteneur cont = null;
		while(cont == null){
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

		final TerminaleNode node = new TerminaleNode(empl.getTerminale(), null);
		//int[] p = ttm.getPath(node);
		final int cPath = rootNodes.indexOf(new ConteneurNode(cont, null, curPf));
		final int[] p2 = ttm.getPathFromNode(node, rootNodes.get(cPath), cPath);
		ttm.addSelectionPath(p2);

		selectedItem = mainTreeContext.renderItemByPath(p2);
		if(selectedItem == null){
			Messagebox.show(Labels.getLabel("stockage.link.error"), Labels.getLabel("general.warning"), Messagebox.OK,
					Messagebox.ERROR);
		}else{
			currentObject = selectedItem.getValue();
			mainTreeContext.setSelectedItem(selectedItem);

			getStockageController().switchToFicheTerminaleMode(empl.getTerminale().clone(), empl);
		}
	}

	//TK-315 :
   /**
    * Permet d'ouvrir les terminales concernées par le déplacement après un updateAllConteneurs qui a redessiné le Tree des conteneurs
    */
	public void onLaterDeplacementDeuxTerminales(final Event e){
      List<Terminale> data = (List<Terminale>)e.getData();
      //appelé après un updateAllConteneurs donc tout est fermé => false en 3e param
      //la fiche terminale est déjà ouverte donc false pour le 2 param
      Terminale term1 = data.get(0);
      openTerminale(term1,false, false);//terminale 1 (celle à déplacer)
      openTerminale(data.get(1),false, false);//terminale 2 (celle à remplacer)
      
      //on reste sur la terminale 1
      selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(new TerminaleNode(term1, null)));
      currentObject = selectedItem.getValue();
      mainTreeContext.setSelectedItem(selectedItem);
 	}
	
	
   //TK-315 :
   /**
    * Permet d'ouvrir les enceintes concernées par le déplacement après un updateAllConteneurs qui a redessiné le Tree des conteneurs
    */
   public void onLaterDeplacementDeuxEnceintes(final Event e){
        List<Enceinte> data = (List<Enceinte>)e.getData();
        //la fiche enceinte est déjà ouverte donc false pour le 2 param
        Enceinte enceinte1 = data.get(0);
        openEnceinte(enceinte1,false);//enceinte 1 (celle à déplacer)
        
        openEnceinte(data.get(1),false);//enceinte 2 (celle à remplacer)
        
        //on reste sur la enceinte 1
        selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(new EnceinteNode(enceinte1, null, null, null)));
        currentObject = selectedItem.getValue();
        mainTreeContext.setSelectedItem(selectedItem);
   }
	
	/**
	 * Navigue dans la liste stockage pour ouvrir une terminale.
	 * 
	 * @version 2.1
	 * @param terminale
	 * @param select
	 * @param reset @since 2.1 clearOpen tree si true
	 */
	public void openTerminale(final Terminale terminale, final boolean select, final boolean reset){
		if(terminale != null){
			// cree la liste remontante des contenants.
			final List<Enceinte> contenants = new ArrayList<>();
			Enceinte enc = terminale.getEnceinte();
			Conteneur cont = null;
			while(cont == null){
				contenants.add(enc);
				cont = enc.getConteneur();
				enc = enc.getEnceintePere();
			}

			// remet le tree à l'état initial
			if(reset){
				// updateAllConteneurs(false);
				((TreeOpenableModel) ttm).clearOpen();
			}

			ttm.addOpenPath(ttm.getPath(new ConteneurNode(cont, null, curPf)));
			for(final Enceinte enct : contenants){
				ttm.addOpenPath(ttm.getPath(new EnceinteNode(enct, null, null, null)));
			}

			final TerminaleNode node = new TerminaleNode(terminale, null);

			ttm.addSelectionPath(ttm.getPath(node));

			if(select){
				selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
				currentObject = selectedItem.getValue();
				mainTreeContext.setSelectedItem(selectedItem);
				getStockageController().switchToFicheTerminaleMode(node.getTerminale().clone(), null);
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
	public void openTerminaleForDeplacement(final Terminale terminale){
		if(terminale != null){
			openTerminale(terminale, false, false);
			final TerminaleNode node = new TerminaleNode(terminale, null);

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
	public void openTerminaleForDeplacementEmplacement(final Terminale terminale){
		if(terminale != null){
			// ouvre l'arborescence vers cette terminale
			openTerminale(terminale, false, false);
			final TerminaleNode node = new TerminaleNode(terminale, null);

			selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
			if(null != selectedItem){
				currentObject = selectedItem.getValue();
				mainTreeContext.setSelectedItem(selectedItem);
				selectForDeplaceEmplacement();
			}
		}
	}

	/**
	 * Navigue dans la liste stockage pour ouvrir une terminale.
	 * 
	 * @param empl
	 */
	public void openEnceinte(final Enceinte enceinte, final boolean select){
		// cree la liste remontante des contenants.
		final List<Enceinte> contenants = new ArrayList<>();
		Enceinte enc = enceinte.getEnceintePere();
		Conteneur cont = enceinte.getConteneur();
		while(cont == null){
			contenants.add(enc);
			cont = enc.getConteneur();
			enc = enc.getEnceintePere();
		}

		ttm.addOpenPath(ttm.getPath(new ConteneurNode(cont, null, curPf)));
		for(final Enceinte enct : contenants){
			ttm.addOpenPath(ttm.getPath(new EnceinteNode(enct, null, null, null)));
		}

		final EnceinteNode node = new EnceinteNode(enceinte, null, null, null);

		ttm.addOpenPath(ttm.getPath(node));
		ttm.addSelectionPath(ttm.getPath(node));

		if(select){
			selectedItem = mainTreeContext.renderItemByPath(ttm.getPath(node));
			currentObject = selectedItem.getValue();
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

	public void disableToolbar(final boolean b){
		addNewConteneur.setDisabled(b);
		etiquettes.setDisabled(b);
	}

	public Enceinte getSelectedEnceinte(){
		return selectedEnceinte;
	}

	public void setSelectedEnceinte(final Enceinte _s){
		this.selectedEnceinte = _s;
	}

	public Terminale getSelectedTerminale(){
		return selectedTerminale;
	}

	public void setSelectedTerminale(final Terminale _t){
		this.selectedTerminale = _t;
	}

	public Conteneur getSelectedConteneur(){
		return selectedConteneur;
	}

	public void setSelectedConteneur(final Conteneur _c){
		this.selectedConteneur = _c;
	}

	/**
	 * Sélectionne et affiche dans l'arborescence la terminale 
	 * à partir du scan, en mode view.
	 * @param TKScanDTO dto
	 * @since 2.1
	 * @version 2.1
	 */
	public void selectTerminaleFromScan(final TKScanTerminaleDTO dto){
		if(dto.getTerminale() != null){
			Events.echoEvent("onLaterScanTerminale", self, dto);
			Clients.showBusy(self, Labels.getLabel("general.display.wait"));
		}
	}

	public void onLaterScanTerminale(final Event e){
		final TKScanTerminaleDTO dto = (TKScanTerminaleDTO) e.getData();
		if(deplacementMode.equals("deplacementEmplacement")){
			openTerminaleForDeplacementEmplacement(dto.getTerminale());
		}else if(deplacementMode.equals("deplacement") && currentObject instanceof TerminaleNode){
			openTerminale(dto.getTerminale(), false, false);
			selectForDeplaceEnceinte();
		}else{
			openTerminale(dto.getTerminale(), true, false);
			getStockageController().getFicheTerminale().applyChecksOnScan(dto);

		}
		Clients.clearBusy(self);
	}

	public List<Conteneur> getRootConteneurs(){
		return rootConteneurs;
	}

	/**
	 * Renvoie la liste de conteneurs accessibles OU le conteneur 
	 * selectionné si il existe.
	 * @since 2.1
	 * @version 2.1
	 * @return liste Conteneur
	 */
	public List<Conteneur> getSelectedConteneurs(){
		List<Conteneur> selConts = new ArrayList<>();
		if(getSelectedConteneur() != null){
			selConts.add(getSelectedConteneur());
		}else{ // all available conteneurs
			selConts = getRootConteneurs();
		}
		return selConts;
	}

	public void onPressEnterKey(){
		onClick$findTerminaleBoxButton();
	}

	/**
	 * Déplie dans l'arborescence tous les noeuds representant 
	 * aux terminales dont le nom correspond à la valeur recherchée.
	 * @since 2.1
	 * @version 2.1 
	 */
	public void onClick$findTerminaleBoxButton(){
	   cleanBlueRowsColor();
		final String tNameToSearch = findTerminaleBox.getValue();
		if(tNameToSearch != null){
			Events.echoEvent("onLaterTerminaleSearch", self, tNameToSearch);
			Clients.showBusy(self, Labels.getLabel("general.display.wait"));
		}
	}

	public void onLaterTerminaleSearch(final Event e){
	   List<Conteneur> conteneursToUse = new ArrayList<Conteneur>(getSelectedConteneurs());
      //TK-314 : dans le cas d'un déplacement, il ne faut pas prendre en compte les terminales 
	   //dont le conteneur est interdit (mode deplacement ou deplacementEmplacement) :
	   if(isDeplacementOrDeplacementEmplacementMode()) {
   		//TK-314 : filtre sur les conteneurs accessibles :
   	   List<Conteneur> conteneursToRemove = new ArrayList<Conteneur>();
   	   for(Conteneur conteneur : conteneursToUse) {
   	      if(!isAccessibleConteneurForCurrentPlateform(conteneur)) {
   	         conteneursToRemove.add(conteneur);
   	      }
   	   }
   	   conteneursToUse.removeAll(conteneursToRemove);
   	   //
	   }
	   
	   final List<Integer> termIds = ManagerLocator.getTerminaleManager().findTerminaleIdsFromNomManager((String) e.getData(),
				getSelectedEnceinte(), conteneursToUse);

		if(!termIds.isEmpty()){

			Terminale term;
			for(int i = 0; i < termIds.size(); i++){
				term = ManagerLocator.getTerminaleManager().findByIdManager(termIds.get(i));

				// select 
				if(i == termIds.size() - 1){
					if(deplacementMode.equals("deplacementEmplacement")){
						openTerminaleForDeplacementEmplacement(term);
					}else if(deplacementMode.equals("deplacement")){
						openTerminaleForDeplacement(term);
					}else{
						openTerminale(term, true, false);
					}
				}else{
					openTerminale(term, false, false);
				}
				searchPaths.add(ttm.getSelectionPath());
			}

			// color results;
			for(final int[] path : searchPaths){
				if(null != path){
				   AffichageUtils.colorateRowInLightBlue(mainTreeContext.renderItemByPath(path).getTreerow());
				}
			}
		}else{ // notification
			Clients.showNotification(
					ObjectTypesFormatters.getLabel("search.terminale.notfound.warning",
							new String[] {(String) e.getData(),
									(getSelectedEnceinte() != null ? getSelectedEnceinte().getNom()
											: getSelectedConteneur() != null ? getSelectedConteneur().getCode() : "")}),
					"warning", null, null, 2000, true);
		}

		Clients.clearBusy(self);
	}

   private void cleanBlueRowsColor(){
      for(final int[] path : searchPaths){
         if(path != null){
            AffichageUtils.unColorateRow(mainTreeContext.renderItemByPath(path).getTreerow());
         }
      }
      
      searchPaths.clear();
   }	
	
   //TK-314
   //NB : un seul item est à réinitialiser mais c'est plus simple de réinitialiser tous les conteneurs
   //pouvant avoir été passés en rouge c'est-à-dire tous les conteneurs interdits. Le nb de conteneurs partagés
   //étant limité, cela n'a pas d'impact au niveau performance.
   private void cleanRedRowsColorForForbiddenConteneurs(){
      for(final int[] path : forbiddenConteneurPaths){
         if(path != null){
            AffichageUtils.unColorateRow(mainTreeContext.renderItemByPath(path).getTreerow());
         }
      }
      
      ///!\ contrairement à  cleanBlueRowsColor, la liste forbiddenConteneurPaths n'est pas nettoyée 
      //car elle sert aussi à désactiver les items des conteneurs interdits.
      //le nettoyage de la liste sera donc fait à la fin du process dans cleanAffichageForbiddenConteneurs
   }  
   

	/**
	 * Reset le tree
	 * @since 2.1
	 * @version 2.1 
	 */
	public void onClick$resetTree(){
		updateAllConteneurs(false);
		//TK-314
		Events.postEvent(new Event("onLaterUpdateAllContainerToManageForbiddenConteneurs", self, null));

		if(!deplacementMode.equals("deplacementEmplacement")){
			switchToNormalMode(false);
			getStockageController().clearAllPage();
		}

	}


	//TK-314
	private boolean isDeplacementOrDeplacementEmplacementMode() {
	   return deplacementMode != null && (deplacementMode.equals("deplacement") || deplacementMode.equals("deplacementEmplacement"));
	}
	
}