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

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.EmplacementDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * 
 * Controller pour la page sur le stockage.
 * Controller créé le 25/03/2010.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class StockageController extends AbstractObjectTabController {

	private static final long serialVersionUID = -7451277316473328904L;
	
	private Borderlayout mainBorder;
	// div contenant le formulaire pour les conteneurs
	private Div divConteneur;
	// Div contenant le formulaire pour les enceintes
	private Div divEnceinte;
	// Div contenant le formulaire multiple pour les terminales
	private Div divTerminale;
	// Div contenant le formulaire multiple pour les déplacements
	private Div divDeplacerEmplacements;
	// Terminale actuelle
	private Terminale actuelleTerminale;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		mainBorder.setHeight(getMainWindow().getPanelHeight() + "px");
		setStaticEditMode(false);
		
		getFicheConteneur().setObjectTabController(this);
		getFicheEnceinte().setObjectTabController(this);
		getFicheTerminale().setObjectTabController(this);
		getListeStockages().setStockageController(this);
	}
	
	@Override
	public TKdataObject loadById(Integer id) {
		return null;
	}
	
	/**
	 * Recupere le controller du composant representant la liste des
	 * stockages.
	 * @param event Event
	 * @return fiche FicheCollaborateur
	 */
	public ListeStockages getListeStockages() {
		return ((ListeStockages) 
				this.self.getFellow("listeStockages")
				.getFellow("lwinStockages")
				.getAttributeOrFellow("lwinStockages$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheConteneur
	 */
	public FicheConteneur getFicheConteneur() {
		
		return ((FicheConteneur) 
				this.self.getFellow("ficheConteneur")
				.getFellow("fwinConteneur")
				.getAttributeOrFellow("fwinConteneur$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheEnceinte
	 */
	public FicheEnceinte getFicheEnceinte() {
		
		return ((FicheEnceinte) 
				this.self.getFellow("ficheEnceinte")
				.getFellow("fwinEnceinte")
				.getAttributeOrFellow("fwinEnceinte$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheEnceinte
	 */
	public FicheTerminale getFicheTerminale() {
		
		return ((FicheTerminale) 
				this.self.getFellow("ficheTerminale")
				.getFellow("fwinTerminale")
				.getAttributeOrFellow("fwinTerminale$composer", true));
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheDeplacerEmplacements
	 */
	public FicheDeplacerEmplacements getFicheDeplacerEmplacements() {
		
		return ((FicheDeplacerEmplacements) 
				this.self.getFellow("divDeplacerEmplacements")
				.getFellow("fwinDeplacerEmplacements")
				.getAttributeOrFellow("fwinDeplacerEmplacements$composer", true));
	}
	
	/**
	 * Retourne le controller de la fiche d'un échantillon.
	 * @param event
	 * @return
	 */
	public EchantillonController getEchantillonController() {
		if (getMainWindow()
				.isFullfilledComponent("echantillonPanel", "winEchantillon")) {
			return (EchantillonController) getMainWindow()
				.getMainTabbox()
				.getTabpanels()
				.getFellow("echantillonPanel")
				.getFellow("winEchantillon")
				.getAttributeOrFellow("winEchantillon$composer", true);
		} else {
			return null;
		}	
	}
	
	/**
	 * Retourne le controller de la fiche d'un produit dérivé.
	 * @param event
	 * @return
	 */
	public ProdDeriveController getProdDeriveController() {
		if (getMainWindow()
				.isFullfilledComponent("derivePanel", "winProdDerive")) {
			return (ProdDeriveController) getMainWindow()
				.getMainTabbox()
				.getTabpanels()
				.getFellow("derivePanel")
				.getFellow("winProdDerive")
				.getAttributeOrFellow("winProdDerive$composer", true);
		} else {
			return null;
		}
	}
	
	/**
	 * Retourne le controller de l'onglet prelevement.
	 * @param event
	 * @return
	 */
	public PrelevementController getPrelevementController() {
		if (getMainWindow()
				.isFullfilledComponent("prelevementPanel", "winPrelevement")) {
			return (PrelevementController) getMainWindow()
				.getMainTabbox()
				.getTabpanels()
				.getFellow("prelevementPanel")
				.getFellow("winPrelevement")
				.getAttributeOrFellow("winPrelevement$composer", true);
		} else {
			return null;
		}	
	}
	
	/**
	 * MAJ des listes d'échantillons et de dérivés.
	 */
	public void refreshListesEchantillonsDerives() {
		if (getMainWindow().isFullfilledComponent(
				"echantillonPanel", "winEchantillon")) {
			if (getEchantillonController() != null) {
				getEchantillonController().getListe().refreshListe();
			}
		}
		
		if (getMainWindow().isFullfilledComponent(
				"derivePanel", "winProdDerive")) {
			if (getProdDeriveController() != null) {
				getProdDeriveController().getListe().refreshListe();
			}
		}
	}
	
	public void initComponent() {
		mainBorder.setHeight(getMainWindow().getPanelHeight() + "px");
	}
	
	/**
	 * Affiche les infos d'un conteneur dans sa FicheConteneur.
	 * @param conteneur Conteneur à afficher.
	 */
	public void switchToFicheConteneurCreateMode() {
		divConteneur.setVisible(true);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(false);
		
		getFicheConteneur().switchToCreateMode();
	}
	
	/**
	 * Affiche les infos d'un conteneur dans sa FicheConteneur.
	 * @param conteneur Conteneur à afficher.
	 */
	public void switchToFicheConteneurMode(Conteneur conteneur) {
		divConteneur.setVisible(true);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(false);
		
		getFicheConteneur().setObject(conteneur);
		getFicheConteneur().switchToStaticMode();
	}
	
	/**
	 * Affiche les infos d'une enceinte.
	 * @param enceinte Enceinte à afficher.
	 */
	public void switchToFicheEnceinteMode(Enceinte enceinte) {
		divConteneur.setVisible(false);
		divEnceinte.setVisible(true);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(false);
		
		getFicheEnceinte().setObject(enceinte);
		getFicheEnceinte().switchToStaticMode();
	}
	
	/**
	 * Affiche les infos d'une terminale.
	 * @param enceinte Enceinte à afficher.
	 */
	public void switchToFicheTerminaleMode(Terminale terminale, Emplacement empl) {
		divConteneur.setVisible(false);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(true);
		divDeplacerEmplacements.setVisible(false);
		
		getFicheTerminale().setObject(terminale);
		getFicheTerminale().switchToStaticMode(empl);
		actuelleTerminale = terminale;
	}
	
	/**
	 * Ppasse en mode de déplacements des échantillons/dérivés.
	 * @param terminale Terminale à afficher.
	 * @param mismatches Une liste de mismatches entre un scan et le stockage virtuel 
	 * peux être passée en paramètre pour pré-remplir les déplacements
	 * @version 2.1
	 */
	public void switchToDeplacerEmplacementsMode(Terminale terminale, 
								Map<ScanTube, TKStockableObject> mismatches) {
		divConteneur.setVisible(false);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(true);
		Executions
		.createComponents("/zuls/stockage/FicheDeplacerEmplacements.zul",
				divDeplacerEmplacements, null);
		
		getListeStockages().switchToDeplacementEmplacementMode();
		getFicheDeplacerEmplacements().setObjectTabController(this);
		getFicheDeplacerEmplacements().setTerminale(terminale);
		getFicheDeplacerEmplacements().switchToSelectionMode(false, mismatches);
		getFicheDeplacerEmplacements().setObjectTabController(this);
		actuelleTerminale = terminale;
	}
	
	/**
	 * Passe en mode de stockage des échantillons/dérivés.
	 * @param echantillons Echantillons à stocker.
	 * @param derives Dérivés à Stocker.
	 */
	public void switchToStockerMode(
			List<Echantillon> echantillons,
			List<ProdDerive> derives,
			String path,
			String methode,
			List<Emplacement> reserves) {
		divConteneur.setVisible(false);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(true);
		Executions
		.createComponents("/zuls/stockage/FicheDeplacerEmplacements.zul",
				divDeplacerEmplacements, null);
		
		getListeStockages().switchToStockerMode();
		getFicheDeplacerEmplacements().setObjectTabController(this);
		getFicheDeplacerEmplacements().switchToStockerMode(
				echantillons, derives, path, methode, reserves);
		actuelleTerminale = null;
	}
	
	/**
	 * Passe en mode de déstockage des échantillons/dérivés.
	 * @param terminale Terminale à afficher.
	 * @param decos Liste Emplacement decorator pour sélection emplacement à destocker
	 * @version 2.1
	 */
	public void switchToDestockageEmplacementsMode(Terminale terminale, 
				List<EmplacementDecorator> decos) {
		divConteneur.setVisible(false);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(true);
		Executions
		.createComponents("/zuls/stockage/FicheDeplacerEmplacements.zul",
				divDeplacerEmplacements, null);
		
		getListeStockages().switchToDeplacementEmplacementMode();
		getFicheDeplacerEmplacements().setObjectTabController(this);
		getFicheDeplacerEmplacements().setTerminale(terminale);
		getFicheDeplacerEmplacements().switchToDestockageMode(decos);
		actuelleTerminale = terminale;
	}
	
	public void clearFiches() {
		getFicheConteneur().clearData();
		getFicheEnceinte().clearData();
		getFicheTerminale().clearData();
		
		divConteneur.setVisible(false);
		divEnceinte.setVisible(false);
		divTerminale.setVisible(false);
		divDeplacerEmplacements.setVisible(false);
		Components.removeAllChildren(divDeplacerEmplacements);
	}
	
	public void clearAllPage() {
		//getListeStockages().clearList();
		clearFiches();
	}
	
	public void initTree() {
		getListeStockages().updateAllConteneurs(false);
	}
	
	/**
	 * Selectionne le tab et renvoie le controller Stockage.
	 * @param page
	 * @return controller tab
	 */
	public static StockageController backToMe(
			MainWindow window, Page page) {
		
		StockageController tabController = null;
		
		// on récupère les panels
		Tabbox panels = (Tabbox) page
								.getFellow("mainWin")
								.getFellow("main").getFellow("mainTabbox");
		
		if (panels != null) {
			// on récupère le panel de l'entite
			Tabpanel panel = (Tabpanel) panels.getFellow("stockagePanel");
			
			window.createMacroComponent(
					"/zuls/stockage/Stockage.zul", 
        			"winStockages", 
        			panel);
		
			tabController = 
				((StockageController) panel.getFellow("winStockages")
								.getAttributeOrFellow("winStockages$composer", true));
		
			panels.setSelectedPanel(panel);
		}
		
		return tabController;
	}
	
	public void generateDroits() {
		getFicheConteneur().drawActionsForConteneur();
		getFicheEnceinte().drawActionsForEnceinte();
		getFicheTerminale().drawActionsForTerminale();
		getListeStockages().drawActionsForStockage();
	}
	
	/**
	 * Instruction recu quand les déplacements sont terminés.
	 * Detache le contenu de la div édition.
	 */
	public void onDeplacementDone() {
		getListeStockages().switchToNormalMode();
		getListeStockages().updateAllConteneurs(false);
		if (actuelleTerminale != null) {
			divDeplacerEmplacements.setVisible(false);
			Components.removeAllChildren(divDeplacerEmplacements);
			switchToFicheTerminaleMode(actuelleTerminale, null);
		} else {
			clearAllPage();
		}
	}

	public Terminale getActuelleTerminale() {
		return actuelleTerminale;
	}

	public void setActuelleTerminale(Terminale actuelle) {
		this.actuelleTerminale = actuelle;
	}

	@Override
	public FicheAnnotation getFicheAnnotation() {
		return null;
	}

	@Override
	public AbstractFicheCombineController getFicheCombine() {
		return null;
	}

	@Override
	public AbstractFicheEditController getFicheEdit() {
		return null;
	}

	@Override
	public AbstractFicheModifMultiController getFicheModifMulti() {
		return null;
	}

	@Override
	public AbstractFicheStaticController getFicheStatic() {
		return null;
	}

	@Override
	public AbstractListeController2 getListe() {
		return null;
	}
	
	/**
	 * Gère un scan full-rack barcode 2D au niveau de l'onglet stockage:
	 *  - si mode liste/consultation: affichage fiche boite
	 *  - si mode déplacement/stockage: sélection des emplacements à remplir
	 * @since 2.1
	 * @param ScanTerminale
	 */
	@Override
	public void handleScanTerminale(ScanTerminale sT) {
		if (sT != null) {
			
			TKScanTerminaleDTO scanDto = ManagerLocator.getScanTerminaleManager()
					.compareScanAndTerminaleManager(sT, getListeStockages().getSelectedEnceinte(), 
													getListeStockages().getSelectedConteneurs());
			
			if (scanDto.getTerminale() == null) {
				Clients.showNotification(ObjectTypesFormatters.getLabel("scan.objects.terminale.notfound.warning", 
						new String[] {sT.getName(), ObjectTypesFormatters.dateRenderer2(sT.getDateScan()), 
						(getListeStockages().getSelectedEnceinte() != null ? getListeStockages().getSelectedEnceinte().getNom() : 
							getListeStockages().getSelectedConteneur() != null ? getListeStockages().getSelectedConteneur().getCode() : "")}),
						"warning", null, null, 4000, true);
			} else {
				// deplacement/stockage
				if (divDeplacerEmplacements.isVisible()) {
					// stockage
					if (getFicheDeplacerEmplacements().isStockerMode()) {
						getFicheDeplacerEmplacements().selectImagesFromScan(scanDto);
					} else if (getFicheDeplacerEmplacements().isDeplacementMode()) {
						
					} 
					// destockage directly from FicheTerminale after scan apply checks
					// else if (getFicheDeplacerEmplacements().isDestockageMode()) {
					//	
					// }
				} else {
					Clients.showNotification(ObjectTypesFormatters.getLabel("scan.objects.terminale.display.info", 
						new String[] {sT.getName(), ObjectTypesFormatters.dateRenderer2(sT.getDateScan()), 
						(getListeStockages().getSelectedEnceinte() != null ? getListeStockages().getSelectedEnceinte().getNom() : 
							getListeStockages().getSelectedConteneur() != null ? getListeStockages().getSelectedConteneur().getCode() : "")}),
						"info", null, null, 4000, true);
			
					// affichage OU mode deplacement terminale -> destination
					getListeStockages().selectTerminaleFromScan(scanDto);
				}
			}
		}
	}
}
