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
package fr.aphp.tumorotek.action.prodderive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller de l'onglet produit dérivé.
 * Controller créé le 02/11/2009.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class ProdDeriveController extends AbstractObjectTabController {

	private static final long serialVersionUID = -6252555557751742915L;

	private Div divProdDeriveStatic;
	private Div divProdDeriveEdit;
	private Div modifMultiDiv;
	
	private String createZulPath = 
		"/zuls/prodderive/FicheMultiProdDerive.zul";	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		setEntiteTab(ManagerLocator.getEntiteManager()
				.findByNomManager("ProdDerive").get(0));
		
		super.doAfterCompose(comp);
		
		setStaticDiv(divProdDeriveStatic);
		setEditDiv(divProdDeriveEdit);
		setEditZulPath("/zuls/prodderive/FicheProdDeriveEdit.zul");
		setModifMultiDiv(modifMultiDiv);
		setMultiEditZulPath("/zuls/prodderive/FicheModifMultiProdDerive.zul");
		setStaticZulPath("/zuls/prodderive/FicheProdDeriveStatic.zul");
		
		initFicheStatic();
		
		switchToOnlyListeMode();
		orderAnnotationDraw(false);
	}
	
	@Override
	public TKdataObject loadById(Integer id) {
		return ManagerLocator.getProdDeriveManager().findByIdManager(id);
	}
	
	@Override
	public FicheProdDeriveStatic getFicheStatic() {
		return ((FicheProdDeriveStatic) 
				self.getFellow("divProdDeriveStatic")
				//.getFellow("ficheProdDerive")
				.getFellow("fwinProdDeriveStatic")
				.getAttributeOrFellow("fwinProdDeriveStatic$composer", true));
	}
	
	@Override
	public FicheProdDeriveEdit getFicheEdit() {
		if (hasMultiFicheEdit()) {
			return getMultiFicheEdit();
		}
		return ((FicheProdDeriveEdit) 
				self.getFellow("divProdDeriveEdit")
				.getFellow("fwinProdDeriveEdit")
				.getAttributeOrFellow("fwinProdDeriveEdit$composer", true));
	}
	
	public FicheMultiProdDerive getMultiFicheEdit() {
		return ((FicheMultiProdDerive) 
				self.getFellow("divProdDeriveEdit")
				.getFellow("fwinMultiProdDerive")
				.getAttributeOrFellow("fwinMultiProdDerive$composer", true));
	}
	
	public boolean hasMultiFicheEdit() {
		if (self.getFellow("divProdDeriveEdit")
				.getFellowIfAny("fwinMultiProdDerive") != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ListeProdDerive getListe() {
		return ((ListeProdDerive) 
				this.self.getFellow("listeProdDerive")
				.getFellow("lwinProdDerive")
				.getAttributeOrFellow("lwinProdDerive$composer", true));
	}
	
	@Override
	public AbstractFicheModifMultiController getFicheModifMulti() {
		return ((FicheModifMultiProdDerive) 
			self.getFellow("modifMultiDiv")
				.getFellow("fwinModifMultiProdDerive")
					.getAttributeOrFellow("fwinModifMultiProdDerive$composer", true));
	}
	
	@Override
	public FicheAnnotation getFicheAnnotation() {
		if (self.getFellowIfAny("ficheAnnoProdDerive") != null) {
			return ((FicheAnnotation) self.getFellow("ficheAnnoProdDerive")
					.getFellow("fwinAnnotation")
					.getAttributeOrFellow("fwinAnnotation$composer", true));
		} else {
			return null;
		}
	}
	
	@Override
	public void clearEditDiv() {
		super.clearEditDiv();
	}
	
	@Override
	public void showStatic(boolean s) {
		super.showStatic(s);
	}
	
	@Override
	public void initLinkedControllers() {
		
		// prelevement
		if (getMainWindow()
				.isFullfilledComponent("prelevementPanel", "winPrelevement")) {
			
			PrelevementController prelController = (PrelevementController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("prelevementPanel")
					.getFellow("winPrelevement")
					.getAttributeOrFellow("winPrelevement$composer", true);
			
			getReferencingObjectControllers().add(prelController);
			
			if (!prelController.getReferencedObjectsControllers().contains(this)) {
				prelController.getReferencedObjectsControllers().add(this);
			}
		}
		
		// echantillon
		if (getMainWindow()
				.isFullfilledComponent("echantillonPanel", "winEchantillon")) {
			
			EchantillonController echanController = (EchantillonController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("echantillonPanel")
					.getFellow("winEchantillon")
					.getAttributeOrFellow("winEchantillon$composer", true);
			
			getReferencingObjectControllers().add(echanController);
			
			if (!echanController.getReferencedObjectsControllers().contains(this)) {
				echanController.getReferencedObjectsControllers().add(this);
			}
		} 
		
		// self
		getReferencingObjectControllers().add(this);
		getReferencedObjectsControllers().add(this);
	
		// cession
		if (getMainWindow()
				.isFullfilledComponent("cessionPanel", "winCession")) {
			
			CessionController cessionController = (CessionController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("cessionPanel")
					.getFellow("winCession")
					.getAttributeOrFellow("winCession$composer", true);
			
			getReferencedObjectsControllers().add(cessionController);
			
			if (!cessionController.getReferencedObjectsControllers().contains(this)) {
				cessionController.getReferencedObjectsControllers().add(this);
			}
		} 
		
	}
	
	/**
	 * Méthode permettant de passer en mode "création multiple" : 
	 * seule la fiche multiple de création des dérivés est visible. 
	 * Cette fiche est en mode "création".
	 * @param parent Objet parent du produit dérivé.
	 */
	public void switchToCreateMode(TKdataObject parent) {
		
		// si il y eu edit avant et addNew depuis liste
		clearEditDiv();
		
		Executions
			.createComponents(createZulPath, divProdDeriveEdit, null);
		
		getFicheMultiProdDerive().setObjectTabController(this);
		getFicheMultiProdDerive().switchToCreateMode(parent);
		getFicheMultiProdDerive().setNewObject();
		//getMultiFicheEdit().setParentObject(parent);
		
		getFicheAnnotation().switchToStaticOrEditMode(false, false);
		//getFicheAnnotation().showButtonsBar(false);
		
		if (!annoRegion.isOpen() && annoRegion.isVisible()) {
			annoRegion.setOpen(true);
		}
		
		getListeRegion().setOpen(false);
		if (getListe() != null) {
			getListe().switchToEditMode(true);
		}
		
		showStatic(false);
		
		

		
	}
	
	/**
	 * Recupere la fiche de création multiple des dérivés.
	 * @param event Event
	 * @return fiche FicheMultiProdDerive
	 */
	public FicheMultiProdDerive getFicheMultiProdDerive() {
		return (FicheMultiProdDerive) 
			this.self.getFellow("divProdDeriveEdit")
			.getFellow("fwinMultiProdDerive")
			.getAttributeOrFellow("fwinMultiProdDerive$composer", true);
	}
	
	/**
	 * Selectionne le tab et renvoie le controller ProdDerive.
	 * @param page
	 * @return controller tab
	 */
	public static AbstractObjectTabController backToMe(
			MainWindow window, Page page) {
		ProdDeriveController tabController = null;
		
		// on récupère les panels
		Tabbox panels = (Tabbox) page
								.getFellow("mainWin")
								.getFellow("main").getFellow("mainTabbox");
		
		if (panels != null) {
			// on récupère le panel de l'entite
			Tabpanel panel = (Tabpanel) panels.getFellow("derivePanel");
			
			window.createMacroComponent(
        			"/zuls/prodderive/ProdDerive.zul", 
        			"winProdDerive", 
        			panel);
		
			tabController = 
				((ProdDeriveController) panel.getFellow("winProdDerive")
								.getAttributeOrFellow("winProdDerive$composer", true));
		
			panels.setSelectedPanel(panel);
		}
		
		return tabController;
	}

	@Override
	public AbstractFicheCombineController getFicheCombine() {
		return null;
	}
	
	@Override
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
	
	@Override
	public List< ? extends Object> getChildrenObjects(TKdataObject obj) {
		List<TKAnnotableObject> childrens = 
			new ArrayList<TKAnnotableObject>(); 
		childrens.addAll(ManagerLocator.getProdDeriveManager()
							.getProdDerivesManager((ProdDerive) obj));
		Iterator<CederObjet> cedIt = ManagerLocator.getCederObjetManager()
								.findByObjetManager((ProdDerive) obj).iterator();
		while (cedIt.hasNext()) {
			childrens.add(cedIt.next().getCession());
		}
		return childrens;
	}
	
	@Override
	public Map<Entite, List<Integer>> getChildrenObjectsIds(List<Integer> ids) {
		Map<Entite, List<Integer>> children = new HashMap<Entite, List<Integer>>(); 
		
		// derives
		Entite deriveEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("ProdDerive").get(0);
		children.put(deriveEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), deriveEntite,
				SessionUtils.getSelectedBanques(sessionScope), true));
		
		// cession 
		Entite cessionEntite = ManagerLocator.getEntiteManager()
				.findByNomManager("Cession").get(0);
		
		children.put(cessionEntite, ManagerLocator.getCorrespondanceIdManager()
				.findTargetIdsFromIdsManager(ids, getEntiteTab(), cessionEntite,
					SessionUtils.getSelectedBanques(sessionScope), false));
		
		return children;
	}
	
	@Override
	public Map<Entite, List<Integer>> getParentsObjectsIds(List<Integer> ids) {
		Map<Entite, List<Integer>> parents = new HashMap<Entite, List<Integer>>(); 
		
		// prelevement
		Entite prelEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("Prelevement").get(0);
		parents.put(prelEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), prelEntite,
				SessionUtils.getSelectedBanques(sessionScope), true));
		
		// echantillon
		Entite echanEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("Echantillon").get(0);
		parents.put(echanEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), echanEntite,
				SessionUtils.getSelectedBanques(sessionScope), true));
		
		// derives
		Entite deriveEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("ProdDerive").get(0);
		parents.put(deriveEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), deriveEntite,
				SessionUtils.getSelectedBanques(sessionScope), false));
		
		return parents;
	}
	
	/**
	 * Gère un scan full-rack barcode 2D au niveau de l'onglet dérivé:
	 *  - si mode liste: affichage contenu
	 *  - si mode creation: application code 2D liste de travail
	 * @since 2.1
	 * @param ScanTerminale
	 */
	@Override
	public void handleScanTerminale(ScanTerminale sT) {
		if (sT != null) {
			if (getStaticDiv().isVisible()) {
				getListe().displayTKObjectsFromCodes(ManagerLocator.getScanTerminaleManager()
					.findTKObjectCodesManager(sT), sT);
			} else if (getEditDiv().isVisible() && hasMultiFicheEdit()) {
				getMultiFicheEdit()
					.applyTKObjectsCodesFromScan(sT);
			}
		}
	}	
}
