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
package fr.aphp.tumorotek.action.echantillon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Box;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller de l'onglet échantillon.
 * Controller créé le 02/11/2009.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class EchantillonController extends AbstractObjectTabController {

	private static final long serialVersionUID = -3799945305452822008L;
	
	private Div divEchantillonStatic;
	private Div divEchantillonEdit;
	private Div modifMultiDiv;
	
	private String createZulPath = 
						"/zuls/echantillon/FicheMultiEchantillons.zul";
	
	// flag ordonnant le retour vers la fiche prelevement
	private boolean fromFichePrelevement = false;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		setEntiteTab(ManagerLocator.getEntiteManager()
				.findByNomManager("Echantillon").get(0));
		
		super.doAfterCompose(comp);
		
		setStaticDiv(divEchantillonStatic);
		setEditDiv(divEchantillonEdit);
		setEditZulPath("/zuls/echantillon/FicheEchantillonEdit.zul");
		setModifMultiDiv(modifMultiDiv);
		setMultiEditZulPath("/zuls/echantillon/FicheModifMultiEchantillon.zul");
		setStaticZulPath("/zuls/echantillon/FicheEchantillonStatic.zul");
		
		initFicheStatic();
		
		switchToOnlyListeMode();
		orderAnnotationDraw(false);
	}	
	
	@Override
	public TKdataObject loadById(Integer id) {
		return ManagerLocator.getEchantillonManager().findByIdManager(id);
	}
	
	@Override
	public FicheEchantillonStatic getFicheStatic() {
		return ((FicheEchantillonStatic) 
				self.getFellow("divEchantillonStatic")
				//.getFellow("ficheEchantillon")
				.getFellow("fwinEchantillonStatic")
				.getAttributeOrFellow("fwinEchantillonStatic$composer", true));
	}
	
	@Override
	public FicheEchantillonEdit getFicheEdit() {
		if (hasMultiFicheEdit()) {
			return getMultiFicheEdit();
		}
		return ((FicheEchantillonEdit) 
				self.getFellow("divEchantillonEdit")
				.getFellow("fwinEchantillonEdit")
				.getAttributeOrFellow("fwinEchantillonEdit$composer", true));
	}
	
	public FicheMultiEchantillons getMultiFicheEdit() {
		return ((FicheMultiEchantillons) 
				self.getFellow("divEchantillonEdit")
				.getFellow("fwinMultiEchantillons")
				.getAttributeOrFellow("fwinMultiEchantillons$composer", true));
	}
	
	public boolean hasMultiFicheEdit() {
		if (self.getFellow("divEchantillonEdit")
				.getFellowIfAny("fwinMultiEchantillons") != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ListeEchantillon getListe() {
		return ((ListeEchantillon) 
				self.getFellow("listeEchantillon")
				.getFellow("lwinEchantillon")
				.getAttributeOrFellow("lwinEchantillon$composer", true));
	}
	
	@Override
	public AbstractFicheModifMultiController getFicheModifMulti() {
		return ((FicheModifMultiEchantillon) 
			self.getFellow("modifMultiDiv")
				.getFellow("fwinModifMultiEchantillon")
					.getAttributeOrFellow("fwinModifMultiEchantillon$composer", true));
	}
	
	@Override
	public FicheAnnotation getFicheAnnotation() {
		if (self.getFellowIfAny("ficheAnnoEchantillon") != null) {
			return ((FicheAnnotation) self.getFellow("ficheAnnoEchantillon")
											.getFellow("fwinAnnotation")
							.getAttributeOrFellow("fwinAnnotation$composer", true));
		} else {
			return null;
		}
	}
	
	@Override
	public void initLinkedControllers() {
		
		// prelevement
		if (!getMainWindow()
				.isFullfilledComponent("prelevementPanel", "winPrelevement")) {
			getMainWindow()
				.createMacroComponent("/zuls/prelevement/Prelevement.zul", 
											"winPrelevement", 
									(Tabpanel) getMainWindow().getMainTabbox()
								.getTabpanels().getFellow("prelevementPanel"));
		}
		
		PrelevementController prelController = (PrelevementController) getMainWindow()
				.getMainTabbox()
				.getTabpanels()
				.getFellow("prelevementPanel")
				.getFellow("winPrelevement")
				.getAttributeOrFellow("winPrelevement$composer", true);
		
		getReferencingObjectControllers().add(prelController);
		
		if (!prelController.getReferencedObjectsControllers().contains(this)) {
			prelController.getReferencedObjectsControllers().add(0, this);
		}
		
		// derive
		if (getMainWindow()
				.isFullfilledComponent("derivePanel", "winProdDerive")) {
			
			ProdDeriveController deriveController = (ProdDeriveController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("derivePanel")
					.getFellow("winProdDerive")
					.getAttributeOrFellow("winProdDerive$composer", true);
			
			getReferencedObjectsControllers().add(deriveController);
			
			if (!deriveController.getReferencingObjectControllers().contains(this)) {
				deriveController.getReferencingObjectControllers().add(this);
			}
		} 
		
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
	 * Selectionne le tab et renvoie le controller Echantillon.
	 * @param page
	 * @return controller tab
	 */
	public static AbstractObjectTabController backToMe(
											MainWindow window, Page page) {
		
		EchantillonController tabController = null;
		
		// on récupère les panels
		Tabbox panels = (Tabbox) page
								.getFellow("mainWin")
								.getFellow("main").getFellow("mainTabbox");
		
		if (panels != null) {
			// on récupère le panel de l'entite
			Tabpanel panel = (Tabpanel) panels.getFellow("echantillonPanel");
			
			window.createMacroComponent(
        			"/zuls/echantillon/Echantillon.zul", 
        			"winEchantillon", 
        			panel);
		
			tabController = 
				((EchantillonController) panel.getFellow("winEchantillon")
								.getAttributeOrFellow("winEchantillon$composer", true));
		
			panels.setSelectedPanel(panel);
		}
		
		return tabController;
	}
	
	/**
	 * Dessine le composant représentant l'affiche statique des codes assignes
	 * dans la fiche.
	 * @param echantillon dont on veut dessiner les codes
	 * @param parent dans lequel dessiner
	 * @param isOrgane dessine les codes organes, les codes morpho sinon.
	 * @param orientation de la liste verticale si true
	 */
	public static void drawCodesAssignes(Echantillon echan, Component parent,
										boolean isOrgane, boolean orientV) {
		
		//Codes assignes
		List<CodeAssigne> codes;
		CodeAssigne codeToExport = null;
		
		Box codeAssBox; 
		if (orientV) {
			codeAssBox = new Vbox();
		} else {
			codeAssBox = new Hbox();
			codeAssBox.setSpacing("5px");
		}
		
		// nettoie la div
		Components.removeAllChildren(parent);
		codeAssBox.setParent(parent);
		
		if (isOrgane) {
			codes = ManagerLocator.getCodeAssigneManager()
							.findCodesOrganeByEchantillonManager(echan);
			//codeToExport = echan.getCodeOrganeExport();
		} else {
			codes = ManagerLocator.getCodeAssigneManager()
							.findCodesMorphoByEchantillonManager(echan);
			//codeToExport = echan.getCodeLesExport();
		}
		
		for (int i = 0; i < codes.size(); i++) {
			if (codes.get(i).getExport()) {
				codeToExport = codes.get(i);
				break;
			}
		}
		
		Iterator<CodeAssigne> it = codes.iterator();
		CodeAssigne next;
		String label = null;
		Label codeStaticLabel = null;
		while (it.hasNext()) {
			next = it.next();
			if (isOrgane) { 
				if (next.getLibelle() != null) {
					label = next.getLibelle() + " [" + next.getCode() + "]";
				} else {
					label = next.getCode();
				}
			} else {
				label = next.getCode();
				if (next.getLibelle() != null) {
					label = label + " " + next.getLibelle();
				}
			}
			codeStaticLabel = new Label(label);
			// modifie le style pour le code exporté.
			if (next.equals(codeToExport) && codes.size() > 0) {
				codeStaticLabel
					.setStyle("font-style: italic; font-weight: bold");
			}
			codeStaticLabel.setSclass("formValue");
			
			codeAssBox.appendChild(codeStaticLabel);
			if (it.hasNext() && !orientV) {
				codeAssBox.appendChild(new Label(" - "));
			}
		}
	}

	@Override
	public AbstractFicheCombineController getFicheCombine() {
		return null;
	}
	
	public boolean getFromFichePrelevement() {
		return fromFichePrelevement;
	}

	public void setFromFichePrelevement(boolean fP) {
		this.fromFichePrelevement = fP;
	}
	
	@Override
	public void switchToCreateMode(TKdataObject parent) {
			
		// si il y eu edit avant et addNew depuis liste
		clearEditDiv();
		
		Executions.createComponents(createZulPath, divEchantillonEdit, null);
		getMultiFicheEdit().setObjectTabController(this);
		getMultiFicheEdit().setNewObject();
		//getMultiFicheEdit().setParentObject(parent);
		getMultiFicheEdit().switchToCreateMode((Prelevement) parent);
		
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
	
	public void switchToPrelevementEditMode(Object obj) {
		
		// si il y eu edit avant et addNew depuis liste
		clearEditDiv();
		
		Executions.createComponents(createZulPath, divEchantillonEdit, null);
		getMultiFicheEdit().setObjectTabController(this);
		getMultiFicheEdit().setNewObject();
		getMultiFicheEdit().switchToEditMode((Prelevement) obj);
		
		getFicheAnnotation().switchToStaticOrEditMode(false, false);
		//getFicheAnnotation().showButtonsBar(false);
		
		if (!annoRegion.isOpen() && annoRegion.isVisible()) {
			annoRegion.setOpen(true);
		}
		
		showStatic(false);
		
		getListeRegion().setOpen(false);
		if (getListe() != null) {
			getListe().switchToEditMode(true);
		}
	}
	
	@Override
	public List< ? extends Object> getChildrenObjects(TKdataObject obj) {
		List<TKAnnotableObject> childrens = 
			new ArrayList<TKAnnotableObject>(); 
		childrens.addAll(ManagerLocator.getProdDeriveManager()
									.findByParentManager((Echantillon) obj, true));
		Iterator<CederObjet> cedIt = ManagerLocator.getCederObjetManager()
							.findByObjetManager((Echantillon) obj).iterator();
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
		
		return parents;
	}
	
	/**
	 * Gère un scan full-rack barcode 2D au niveau de l'onglet echantillon:
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
