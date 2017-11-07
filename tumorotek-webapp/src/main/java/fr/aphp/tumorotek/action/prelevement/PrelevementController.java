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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.echantillon.FicheMultiEchantillons;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.serotk.PrelevementSeroRowRenderer;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller de l'onglet prelevement.
 * Controller créé le 23/11/2009.
 * 
 * @author Pierre Ventadour
 * @version 2.0.6
 *
 */
public class PrelevementController extends AbstractObjectTabController {
	
	private Log log = LogFactory.getLog(PrelevementController.class);

	private static final long serialVersionUID = -5315034328095152785L;
	//private Prelevement prelevement;
	private Maladie maladie;
	
	private Div divPrelevementStatic;
	private Div divPrelevementEdit;
	private Div divLaboInter;
	//private Div divMultiEchantillons;
	private Div modifMultiDiv;
	
	// flag ordonnant le retour vers la fiche patient
	// et rafraichissement et ouverture panel maladie
	private boolean fromFichePatient = false;
	private boolean fromFicheMaladie = false;
	
	// flag indiquant que le panel echantillon a été atteint
	private boolean nextToEchanClicked = false;
	// flag indiquant que le code du prlvt a été modifié
	private boolean codeUpdated = false;
	// ancien code
	private String oldCode = null;
	
	// dossier externe provenant d'un SGL
	// private DossierExterne dossierExterne;
	
	private PatientSip patientSip;
	
	public Maladie getMaladie() {
		return maladie;
	}

	public void setMaladie(Maladie m) {
		this.maladie = m;
	}	

	
	public boolean getFromFichePatient() {
		return fromFichePatient;
	}

	public void setFromFichePatient(boolean fP) {
		this.fromFichePatient = fP;
	}

	public boolean getFromFicheMaladie() {
		return fromFicheMaladie;
	}

	public void setFromFicheMaladie(boolean fM) {
		this.fromFicheMaladie = fM;
	}

	public boolean getNextToEchanClicked() {
		return nextToEchanClicked;
	}

	public void setNextToEchanClicked(boolean n) {
		this.nextToEchanClicked = n;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		setEntiteTab(ManagerLocator.getEntiteManager()
				.findByNomManager("Prelevement").get(0));
		
		super.doAfterCompose(comp);
		
		setStaticDiv(divPrelevementStatic);
		setEditDiv(divPrelevementEdit);
		setModifMultiDiv(modifMultiDiv);
		setEditZulPath("/zuls/prelevement/FichePrelevementEdit.zul");
		setMultiEditZulPath("/zuls/prelevement/FicheModifMultiPrelevement.zul");
		setListZulPath("/zuls/prelevement/ListePrelevement.zul");
		
		// drawListe();
		
		initFicheStatic();
		
		switchToOnlyListeMode();
		orderAnnotationDraw(false);
	}
	
	@Override
	public TKdataObject loadById(Integer id) {
		return ManagerLocator.getPrelevementManager().findByIdManager(id);
	}
	
	@Override
	public void drawListe() {
		if (SessionUtils.isSeroContexte(sessionScope)) {
			setListZulPath("/zuls/prelevement/serotk/"
						+ "ListePrelevementSero.zul");
			setListRenderer(new PrelevementSeroRowRenderer(true, 
					SessionUtils.getSelectedBanques(sessionScope).size() > 1));
		}
		super.drawListe();
	}
	
	@Override
	public void populateFicheStatic() {
		setStaticZulPath("/zuls/prelevement/FichePrelevementStatic.zul");
		if (SessionUtils.isSeroContexte(sessionScope)) {
			setStaticZulPath("/zuls/prelevement/serotk/"
						+ "FichePrelevementStaticSero.zul");
		}
		super.populateFicheStatic();
	}
	
	@Override
	public void populateFicheEdit() {
		if (SessionUtils.isSeroContexte(sessionScope)) {
			setEditZulPath("/zuls/prelevement/serotk/FichePrelevementEditSero.zul");
		}
		super.populateFicheEdit();
	}
	
	@Override
	public FichePrelevementStatic getFicheStatic() {
		return ((FichePrelevementStatic) 
				this.self.getFellow("divPrelevementStatic")
				.getFellow("fwinPrelevementStatic")
				.getAttributeOrFellow("fwinPrelevementStatic$composer", true));
	}
	
	@Override
	public FichePrelevementEdit getFicheEdit() {
		return ((FichePrelevementEdit) 
				this.self.getFellow("divPrelevementEdit")
				.getFellow("fwinPrelevementEdit")
				.getAttributeOrFellow("fwinPrelevementEdit$composer", true));
	}
	
	@Override
	public ListePrelevement getListe() {
		return ((ListePrelevement) 
				//this.self.getFellow("listePrelevement")
				// self.getFellow("lwinPrelevement")
				self.getFellow("listePrelevement")
				.getFellow("lwinPrelevement")
				.getAttributeOrFellow("lwinPrelevement$composer", true));
	}
	
	@Override
	public AbstractFicheModifMultiController getFicheModifMulti() {
		return ((FicheModifMultiPrelevement) 
			self.getFellow("modifMultiDiv")
				.getFellow("fwinModifMultiPrelevement")
					.getAttributeOrFellow("fwinModifMultiPrelevement$composer", true));
	}
	
	@Override
	public FicheAnnotation getFicheAnnotation() {
		if (self.getFellowIfAny("ficheAnnoPrelevement") != null) {
			return ((FicheAnnotation) self.getFellow("ficheAnnoPrelevement")
											.getFellow("fwinAnnotation")
							.getAttributeOrFellow("fwinAnnotation$composer", true));
		} else {
			return null;
		}
	}
	
	@Override
	public void clearEditDiv() {
		super.clearEditDiv();
		Components.removeAllChildren(divLaboInter);
		//Components.removeAllChildren(divMultiEchantillons);
	}
	
	@Override 
	public void switchToFicheAndListeMode() {
		super.switchToFicheAndListeMode();	
	}
	
	@Override
	public void showStatic(boolean s) {
		super.showStatic(s);
		divLaboInter.setVisible(!s);
		//divMultiEchantillons.setVisible(!s);
	}
	
	@Override
	public void initLinkedControllers() {

		if (getMainWindow()
				.isFullfilledComponent("patientPanel", "winPatient")) {
			
			PatientController patController = (PatientController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("patientPanel")
					.getFellow("winPatient")
				.getAttributeOrFellow("winPatient$composer", true);
			getReferencingObjectControllers().add(patController);
			
			if (!patController.getReferencedObjectsControllers().contains(this)) {
				patController.getReferencedObjectsControllers().add(this);
			}
		}
		
		if (getMainWindow()
				.isFullfilledComponent("echantillonPanel", "winEchantillon")) {
			
			EchantillonController echanController = (EchantillonController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("echantillonPanel")
					.getFellow("winEchantillon")
			.getAttributeOrFellow("winEchantillon$composer", true);
			getReferencedObjectsControllers().add(echanController);
			
			if (!echanController.getReferencingObjectControllers().contains(this)) {
				echanController.getReferencingObjectControllers().add(this);
			}
		}
		
		// derives
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
	}
	
	/**
	 * Recupere la liste de controllers des objets qui sont référencés 
	 * par l'objet avec surcharge de méthode pour créer le controller echantillon si il n'existe 
	 * pas.
	 * @param boolean create cree le controller Echantillon si il n'existe pas. 
	 * @return la liste de controllers.
	 */
	public List<AbstractObjectTabController> getReferencedObjectsControllers(boolean force) {
		
		// echantillons
		// cree le contenu du panel au besoin
		if (force && !getMainWindow()
				.isFullfilledComponent("echantillonPanel", "winEchantillon")) {
			
			getMainWindow().createMacroComponent(
        			"/zuls/echantillon/Echantillon.zul", 
        			"winEchantillon", 
        			(Tabpanel) getMainWindow().getMainTabbox()
        			.getTabpanels().getFellow("echantillonPanel"));
			
			EchantillonController echanController = (EchantillonController) getMainWindow()
					.getMainTabbox()
					.getTabpanels()
					.getFellow("echantillonPanel")
					.getFellow("winEchantillon")
					.getAttributeOrFellow("winEchantillon$composer", true);
			
			getReferencedObjectsControllers().add(0, echanController);
			
			if (!echanController.getReferencingObjectControllers().contains(this)) {
				echanController.getReferencingObjectControllers().add(this);
			}
			
		}
		
		return getReferencedObjectsControllers();
	}
	
	/**
	 * Méthode permettant de passer en mode édition des labos.
	 * @param prelevement Prlvt.
	 */
	public void switchToLaboInterEditMode(Prelevement prlvt) {
		Prelevement edit = prlvt;
		divPrelevementStatic.setVisible(false);
		divPrelevementEdit.setVisible(false);
		divLaboInter.setVisible(true);
		//divMultiEchantillons.setVisible(false);

		if (divLaboInter.getChildren().size() == 0) {
			Executions
				.createComponents("/zuls/prelevement/FicheLaboInter.zul",
					divLaboInter, null);
			getFicheLaboInter().setObjectTabController(this);
			getFicheLaboInter().setObject(edit);
			getFicheLaboInter().switchToEditMode();
		}
		
	}
	
	/**
	 * Méthode permettant de passer en mode création des labos.
	 * @param prelevement Prlvt.
	 */
	public void switchToLaboInterCreateMode(Prelevement prlvt, 
			Maladie parent, List<LaboInter> labos) {
		
		Prelevement newPrlvt = prlvt;
		divPrelevementStatic.setVisible(false);
		divPrelevementEdit.setVisible(false);
		divLaboInter.setVisible(true);
		//divMultiEchantillons.setVisible(false);

		if (divLaboInter.getChildren().size() == 0) {
			Executions
				.createComponents("/zuls/prelevement/FicheLaboInter.zul",
					divLaboInter, null);
			getFicheLaboInter().setObjectTabController(this);
			getFicheLaboInter().setObject(newPrlvt);
			getFicheLaboInter().setOldLaboInters(labos);
			getFicheLaboInter().switchToCreateMode();
		}
	}
	
	/**
	 * Méthode permettant de passer en mode édition des échantillons
	 * d'un prélèvement.
	 * @param prelevement Prlvt.
	 */
	public void switchToMultiEchantillonsEditMode(Prelevement prlvt, 
			List<LaboInter> labos, 
			List<LaboInter> labosToDelete) {
		//divPrelevementStatic.setVisible(false);
		//divPrelevementEdit.setVisible(false);
		divLaboInter.setVisible(true);
		
		// enregistre le flag lors du premier acces a l'echantillon.
		if (!nextToEchanClicked) {
			((EchantillonController) getReferencedObjectsControllers(true).get(0))
									.switchToPrelevementEditMode(prlvt);
			getFicheMultiEchantillons().setPrelevementProcedure(true);
			getFicheMultiEchantillons().setLaboInters(labos);
			getFicheMultiEchantillons().setLaboIntersToDelete(labosToDelete);
			nextToEchanClicked = true;
		}
		
		// change d'onglet
		EchantillonController.backToMe(getMainWindow(), page);
	}
	
	/**
	 * Méthode permettant de passer en mode création des échantillons
	 * d'un prélèvement.
	 * @param prelevement Prlvt.
	 */
	public void switchToMultiEchantillonsCreateMode(
			Prelevement prlvt, Maladie parent, 
			List<LaboInter> labos) {
		
		//divPrelevementStatic.setVisible(false);
		//divPrelevementEdit.setVisible(false);
		divLaboInter.setVisible(true);
		//divMultiEchantillons.setVisible(true);
		
		// enregistre le flag lors du premier acces a l'echantillon.
		if (!nextToEchanClicked) {
			((EchantillonController) getReferencedObjectsControllers(true).get(0))
												.switchToCreateMode(prlvt);
			getFicheMultiEchantillons().setPrelevementProcedure(true);
			nextToEchanClicked = true;
		} else {
			if (((EchantillonController) 
					getReferencedObjectsControllers(true).get(0))
					.hasMultiFicheEdit()) {
				getFicheMultiEchantillons().setParentObject(prlvt);
			} else {
				((EchantillonController) 
					getReferencedObjectsControllers(true).get(0))
					.switchToCreateMode(prlvt);
				getFicheMultiEchantillons().setPrelevementProcedure(true);
				nextToEchanClicked = true;
			}
		}
		getFicheMultiEchantillons().setLaboInters(labos);
		
		// change d'onglet
		EchantillonController.backToMe(getMainWindow(), page);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton
	 * "précédent" quand il est sur la page FicheLaboInter.
	 */
	public void switchFromLaboToPrelevement() {
		divPrelevementStatic.setVisible(false);
		divPrelevementEdit.setVisible(true);
		divLaboInter.setVisible(false);
		//divMultiEchantillons.setVisible(false);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le bouton
	 * "précédent" quand il est sur la page FicheMultiEchantillons.
	 */
	public void switchFromEchantillonsToLabo() {
		divPrelevementStatic.setVisible(false);
		divPrelevementEdit.setVisible(false);
		divLaboInter.setVisible(true);
		//divMultiEchantillons.setVisible(false);
		// change d'onglet
		PrelevementController.backToMe(getMainWindow(), page);
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheLaboInter
	 */
	public FicheLaboInter getFicheLaboInter() {
		return ((FicheLaboInter) this.self.getFellow("divLaboInter")
				.getFellow("fwinLaboInter")
				.getAttributeOrFellow("fwinLaboInter$composer", true));
	}
	
	public boolean hasFicheLaboInter() {
		if (this.self.getFellow("divLaboInter")
				.getFellowIfAny("fwinLaboInter") != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFicheLaboOpened() {
		return this.self
			.getFellow("divLaboInter").getFirstChild() != null;
	}
	
	/**
	 * Recupere le controller du composant representant la fiche associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche FicheMultiEchantillons
	 */
	public FicheMultiEchantillons getFicheMultiEchantillons() {
		return ((EchantillonController) 
					getReferencedObjectsControllers(true).get(0))
														.getMultiFicheEdit();
	}
	
	/**
	 * Selectionne le tab et renvoie le controller Prelevement.
	 * @param page
	 * @return controller tab
	 */
	public static AbstractObjectTabController backToMe(MainWindow window, 
																Page page) {
		
		PrelevementController tabController = null;
		
		// on récupère les panels
		Tabbox panels = (Tabbox) page
								.getFellow("mainWin")
								.getFellow("main").getFellow("mainTabbox");
		
		if (panels != null) {
			// on récupère le panel de l'entite
			Tabpanel panel = (Tabpanel) panels.getFellow("prelevementPanel");
		
			window.createMacroComponent(
        			"/zuls/prelevement/Prelevement.zul", 
        			"winPrelevement", 
        			panel);
			
			tabController = 
				((PrelevementController) panel.getFellow("winPrelevement")
								.getAttributeOrFellow("winPrelevement$composer", true));
		
			panels.setSelectedPanel(panel);
		}
		
		return tabController;
	}

	@Override
	public void onCreateDone() {
		// create done hors fiche echantillon atteinte pourtant
		if (nextToEchanClicked) { 
			((EchantillonController) 
					getReferencedObjectsControllers(true).get(0)).onCancel();
			nextToEchanClicked = false;
		}
		super.onCreateDone();
	}
	
	@Override
	public void onEditDone(TKdataObject obj) {
		// update done hors fiche echantillon atteinte pourtant
		if (nextToEchanClicked) { 
			((EchantillonController) 
					getReferencedObjectsControllers(true).get(0)).onCancel();
			nextToEchanClicked = false;
		}
		super.onEditDone(obj);
	}

	@Override
	public AbstractFicheCombineController getFicheCombine() {
		return null;
	}
	
	/**
	 * Méthode permettant de passer en mode "création", collapse la liste
	 * et dessine la fiche édition/creation avec un objet vide.
	 */
	public void switchToCreateMode(TKdataObject parent) {
		
		super.switchToCreateMode(parent);
		
		if (nextToEchanClicked) { 
			((EchantillonController) 
					getReferencedObjectsControllers(true).get(0)).onCancel();
			nextToEchanClicked = false;
		}
		
		// on efface le dossier externe
		// setDossierExterne(null);
		SessionUtils.setDossierExterneInjection(sessionScope, null);
	}
	
	@Override
	public void onCancel() {
		if (nextToEchanClicked) { 
			((EchantillonController) 
					getReferencedObjectsControllers(true).get(0)).onCancel();
			nextToEchanClicked = false;
		}
		super.onCancel();
	}
	
	@Override
	public void onRevert() {
		// cancel done hors fiche echantillon atteinte pourtant
		if (nextToEchanClicked) { 
			((EchantillonController) 
					getReferencedObjectsControllers(true).get(0)).onCancel();
			nextToEchanClicked = false;
		}
		super.onRevert();
	}
	
	public void createAnotherPrelevement(Prelevement prlvt) {
		if (Messagebox.show(Labels.getLabel(
			"fichePrelevement.create.another.prelevement"), 
			Labels.getLabel("message.new.prelevement.title"), 
			Messagebox.YES | Messagebox.NO, 
			Messagebox.QUESTION) == Messagebox.YES) {
				backToMe(getMainWindow(), page);
				
				// si il y eu edit avant et addNew depuis liste
				clearEditDiv();
				Executions.createComponents(
						"/zuls/prelevement/FichePrelevementEdit.zul", 
						divPrelevementEdit, null);
				getFicheEdit().setObjectTabController(this);
				getFicheEdit().setNewObjectByCopy(prlvt);
				getFicheEdit().setParentObject(prlvt.getMaladie());
				getFicheEdit().switchToCreateMode();
				getFicheEdit().initSelectedInLists();
				
				if (canUpdateAnnotation()) {
					getFicheAnnotation()
						.switchToStaticOrEditMode(false, false);
				}
				
				if (!annoRegion.isOpen() && annoRegion.isVisible()) {
					annoRegion.setOpen(true);
				}
				
				getListeRegion().setOpen(false);
				
				showStatic(false);
		}
	}
	
	/**
	 * Si l'utilisateur a modifié le code d'un prélèvement, une 
	 * fenêtre sera affichée pour lui proposer d'afficher ses
	 * échantillons afin de mettre à jour leurs codes.
	 * @param prlvt
	 */
	public void showEchantillonsAfterUpdate(Prelevement prlvt) {
		List<Echantillon> echans = new ArrayList<Echantillon>(ManagerLocator
				.getPrelevementManager()
				.getEchantillonsManager(prlvt));
		// si le code a été mis à jour et que le prlvt a des
		// échantillons
		if (codeUpdated && echans.size() > 0
				&& oldCode != null) {
			openShowEchantillonsModaleWindow(echans,
					prlvt, oldCode);
		}
	}
	
	/**
	 * Méthode appelée demander à l'utilisateur s'il souhaite afficher
	 * les échantillons d'un prélèvement pour les mettre à jour.
	 * pour changer le prelevement de collection.
	 */
	public void openShowEchantillonsModaleWindow(List<Echantillon> echans,
			Prelevement prlvt,
			String oldPrefixe) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("showEchantillonsWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels
					.getLabel("general.edit"));
			win.setBorder("normal");
			win.setWidth("400px");
			win.setHeight("325px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = 
				populateShowEchantillonsModal(
						echans, prlvt, oldPrefixe,
						page, getMainWindow(), win);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
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
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent 
		populateShowEchantillonsModal(List<Echantillon> echans, 
				Prelevement prlvt,
				String oldPrefixe,
							Page page, MainWindow main,
							Window win) {
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("showEchantillonsModale", false)
												.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openShowEchantillonsModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((ShowEchantillonsModale) ua.getFellow("fwinShowEchantillons")
				.getAttributeOrFellow("fwinShowEchantillons$composer", true))
				.init(echans, prlvt, oldPrefixe, main, page);
		
		return ua;
	}

	public boolean isCodeUpdated() {
		return codeUpdated;
	}

	public void setCodeUpdated(boolean cUpdated) {
		this.codeUpdated = cUpdated;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String c) {
		this.oldCode = c;
	}
	
	/**
	 * Gère la communication entre TK et l'environnement applicatif 
	 * après création du dossier.
	 * Suppression d'un dossier temporaire si enregistré en base.
	 * Envoi d'un accusé de réception/synthèse du prélèvement.
	 * @param prel Prelevement
	 * @param controller (Prelvement/Echantillon) qui reçoit la validation
	 * @version 2.1
	 */
	public void handleExtCom(Prelevement prel, 
							AbstractObjectTabController controller) {
		
		String url = null;
		String dosExtId = null;
		
		// gère la suppression du dossier externe
		if (SessionUtils.getDossierExterneInjection(sessionScope) != null
				&& SessionUtils.getDossierExterneInjection(sessionScope)
					.getDossierExterne() != null
				&& SessionUtils.getDossierExterneInjection(sessionScope)
					.getDossierExterne().getDossierExterneId() != null) {
			
			dosExtId = SessionUtils.getDossierExterneInjection(sessionScope)
					.getDossierExterne()
					.getIdentificationDossier();
	
			// since 2.0.13
			// suppr si pas info echantillon + validation depuis onglet Prelevement
			// ou si info échantillons + validation depuis onglet échantillon
			
			if (ManagerLocator.getBlocExterneManager()
					.findByDossierExterneAndEntiteManager(SessionUtils
								.getDossierExterneInjection(sessionScope).getDossierExterne(), 
							ManagerLocator.getEntiteManager().findByIdManager(3)).isEmpty()
				|| controller.getEntiteTab().getNom().equals("Echantillon")) {
				ManagerLocator.getDossierExterneManager()
					.removeObjectManager(SessionUtils.getDossierExterneInjection(sessionScope
								).getDossierExterne());
			}
			
			// setDossierExterne(null);
			SessionUtils.setDossierExterneInjection(sessionScope, null);
			
			HttpServletRequest req = 
	 				(HttpServletRequest) 
	 					Executions.getCurrent().getNativeRequest();	
			url = req.getScheme() + "://" 
					+ req.getServerName();
			if (req.getServerPort() > -1) {
				url = url + ":" + req.getServerPort();
			}
			url = url + req.getContextPath() 
					+ "/ext/prelevement?id=" + prel.getPrelevementId();
		}
		
		try {
			// Recepteurs
			for (Recepteur recept : SessionUtils.getRecepteursInterfacages(sessionScope)) {
				ManagerLocator.getSenderFactory().sendMessage(recept, prel, dosExtId, url);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public PatientSip getPatientSip() {
		return patientSip;
	}

	public void setPatientSip(PatientSip patientSip) {
		this.patientSip = patientSip;
	}

	public void removePatientSip() {
		if (patientSip != null
				&& patientSip.getPatientSipId() != null) {
			ManagerLocator.getPatientSipManager()
				.removeObjectManager(patientSip);
			
			setPatientSip(null);
		}
	}
	
	@Override
	public List< ? extends Object> getChildrenObjects(TKdataObject obj) {
		List<TKAnnotableObject> childrens = 
				new ArrayList<TKAnnotableObject>(); 
		
		Set<Echantillon> echans = ManagerLocator.getPrelevementManager()
				.getEchantillonsManager((Prelevement) obj);
		childrens.addAll(echans);
		for (Echantillon echantillon : echans) {
			childrens.addAll(ManagerLocator.getProdDeriveManager()
							.findByParentManager(echantillon, true));
		}	
		childrens.addAll(ManagerLocator.getProdDeriveManager()
							.findByParentManager((Prelevement) obj, true));
		return childrens;
	}
	
	@Override
	public Map<Entite, List<Integer>> getChildrenObjectsIds(List<Integer> ids) {
		Map<Entite, List<Integer>> children = new HashMap<Entite, List<Integer>>(); 
		
		// echantillo,s
		Entite echanEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("Echantillon").get(0);
		children.put(echanEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), echanEntite,
				SessionUtils.getSelectedBanques(sessionScope), false));
				
		// derives
		Entite deriveEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("ProdDerive").get(0);
		children.put(deriveEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), deriveEntite,
				SessionUtils.getSelectedBanques(sessionScope), true));
		
		return children;
	}
	
	@Override
	public Map<Entite, List<Integer>> getParentsObjectsIds(List<Integer> ids) {
		Map<Entite, List<Integer>> parents = new HashMap<Entite, List<Integer>>(); 
		
		// patient
		Entite patEntite = ManagerLocator.getEntiteManager()
							.findByNomManager("Patient").get(0);
		parents.put(patEntite, ManagerLocator.getCorrespondanceIdManager()
			.findTargetIdsFromIdsManager(ids, getEntiteTab(), patEntite,
				SessionUtils.getSelectedBanques(sessionScope), true));
		
		return parents;
	}
}
