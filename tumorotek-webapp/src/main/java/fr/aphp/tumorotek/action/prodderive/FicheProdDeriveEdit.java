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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;

import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant la fiche d'édition d'un produit dérivé.
 * Controller créé le 04/11/2009.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class FicheProdDeriveEdit extends AbstractFicheEditController {

	private static final long serialVersionUID = 4384639895874573764L;

	protected Textbox codePrefixeLabelDerive;
	protected Textbox codeBoxDerive;
	protected Textbox codeLaboBoxDerive;
	protected Label volumeBoxDerive;
	protected Decimalbox volumeInitBoxDerive;
	protected Decimalbox concentrationBoxDerive;
	protected Label quantiteBoxDerive;
	protected Decimalbox quantiteInitBoxDerive;
	protected Decimalbox transfoQuantiteBoxDerive;
	protected CalendarBox dateStockCalBox;
	protected CalendarBox dateTransfoCalBox;
	protected Listbox typesBoxDerive;
	protected Listbox volumeUnitesBoxDerive;
	protected Listbox concentrationUnitesBoxDerive;
	protected Listbox quantiteUnitesBoxDerive;
	protected Listbox qualitesBoxDerive;
	protected Listbox modePrepaBoxDerive;
//	private Label transfoQuantiteUnitesBoxDerive;
	protected Combobox collabBoxDerive;
	protected Label operateurAideSaisieDerive;
	
	// Composants pour le parent
	private Group groupPrlvtDerive;
	private Group groupEchanDerive;
	private Group groupDeriveDerive;
	protected Row row1EchanDerive;
	protected Row row2EchanDerive;
	private Row row3EchanDerive;
	private Row row4EchanDerive;
	protected Row row1PrlvtDerive;
	protected Row row2PrlvtDerive;
	private Row row3PrlvtDerive;
	private Row row4PrlvtDerive;
	protected Row row1DeriveDerive;
	protected Row row2DeriveDerive;
	private Row row3DeriveDerive;
	private Row row4DeriveDerive;
	
	protected Checkbox conformeTraitementBoxOui;
	protected Checkbox conformeTraitementBoxNon;
	protected Div conformeTraitementBox;
	protected Checkbox conformeCessionBoxOui;
	protected Checkbox conformeCessionBoxNon;
	protected Div conformeCessionBox;
	protected Listbox nonConformitesTraitementBox;
	protected Listbox nonConformitesCessionBox;
	
	// Lignes pour la transformation
	protected Row rowTransformation1;
	protected Row rowTransformation2;
	protected Row transformationInconnueLabel;
	private Component[] objLabelsPrlvtParent;
	private Component[] objLabelsEchanParent;
	private Component[] objLabelsDeriveParent;
	private Component[] objLabelsTransformation;
	
	// Objets Principaux.
	private ProdDerive prodDerive = new ProdDerive();
	private TKdataObject parentObj;
	private Transformation transformation = new Transformation();
	// copie de la transformation, utilisée en cas de reverse
	private Transformation copyTransformation = new Transformation();
	
	// Objets pour le parent
//	private Prelevement parentPrlvt = new Prelevement();
//	private Echantillon parentEchantillon = new Echantillon();
//	private ProdDerive parentProdDerive = new ProdDerive();
	private String typeParent = null;
	private Emplacement oldEmplacement = null;


	// Associations.
	private List<ProdType> types = new ArrayList<ProdType>();
	private ProdType selectedType;
	private List<Unite> quantiteUnites = new ArrayList<Unite>();
	private Unite selectedQuantiteUnite;
	private List<Unite> volumeUnites = new ArrayList<Unite>();
	private Unite selectedVolumeUnite;
	private List<Unite> concUnites = new ArrayList<Unite>();
	private Unite selectedConcUnite;
	private Unite selectedTransfoQuantiteUnite;
	private List<ProdQualite> qualites = new ArrayList<ProdQualite>();
	private ProdQualite selectedQualite;
	private List<Collaborateur> collaborateurs = new ArrayList<Collaborateur>();
	private List<Collaborateur> multiCollaborateurs 
											= new ArrayList<Collaborateur>();
	private Collaborateur selectedCollaborateur;
	private List<String> nomsAndPrenoms = new ArrayList<String>();
	private List<ModePrepaDerive> modePrepaDerives = 
		new ArrayList<ModePrepaDerive>();
	private ModePrepaDerive selectedModePrepaDerive;
	private List<NonConformite> nonConformitesTraitement = 
		new ArrayList<NonConformite>();
	private NonConformite selectedNonConformiteTraitement;
	private List<NonConformite> nonConformitesCession = 
		new ArrayList<NonConformite>();
	private NonConformite selectedNonConformiteCession;
	private Set<Listitem> selectedNonConformitesTraitementItem = 
		new HashSet<Listitem>();
	private Set<Listitem> selectedNonConformitesCessionItem = 
		new HashSet<Listitem>();

	// Variables formulaire.
	private String valeurQuantite = "";
	private String valeurQuantiteRestante = "";
	private String valeurVolume = "";
	private String valeurVolumeRestant = "";
	private String valeurConcentration = "";
	private String valeurTransfoQuantite = "";
	private String codePrefixe = "";
	private String codeSuffixe = "";
	private String codeParent = "";
	private String emplacementAdrl = "";
	
	/**
	 * Gestion des contraintes sur les quantités et volumes .
	 * Variables conservant les valeurs saisies dans les champs. 
	 */
	private Float volume;
	private Float volumeInit;
	private Float quantite;
	private Float quantiteInit;
	// quantite max que peut saisir l'utilisateur pour la transformation
	private Float quantiteMax;
	private Float quantiteTransformation;
	private Date dateCongelation;
	private Date dateTransformation;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setWaitLabel("ficheProdDerive.creation.encours");
		
		// liste de composants pour le prlvt parent
		setObjLabelsPrlvtParent(new Component[] {this.groupPrlvtDerive, 
				this.row1PrlvtDerive, 
				this.row2PrlvtDerive, 
				this.row3PrlvtDerive,
				this.row4PrlvtDerive});
		// liste de composants pour l'échantillon parent
		setObjLabelsEchanParent(new Component[] {this.groupEchanDerive, 
				this.row1EchanDerive, 
				this.row2EchanDerive, 
				this.row3EchanDerive,
				this.row4EchanDerive});
		// liste de composants pour le dérivé parent
		setObjLabelsDeriveParent(new Component[] {this.groupDeriveDerive, 
				this.row1DeriveDerive, 
				this.row2DeriveDerive, 
				this.row3DeriveDerive,
				this.row4DeriveDerive});
		// liste de composants pour la transformation
		setObjLabelsTransformation(new Component[] {
			this.rowTransformation1,
			this.rowTransformation2,
		});
		
//		for (int i = 0; i < getObjLabelsPrlvtParent().length; i++) {
//			getObjLabelsPrlvtParent()[i].setVisible(false);
//			getObjLabelsEchanParent()[i].setVisible(false);
//			getObjLabelsDeriveParent()[i].setVisible(false);
//		}
//		for (int i = 0; i < objLabelsTransformation.length; i++) {
//			objLabelsTransformation[i].setVisible(true);
//		}
	}
	
	@Override
	public void setObject(TKdataObject pd) {
		this.prodDerive = (ProdDerive) pd;
		
		if (this.prodDerive.getTransformation() != null) {
			setTransformation(this.prodDerive.getTransformation());
			// copyTransformation = this.transformation.clone();
			// on récupère le type du parent (prlvt, echan, prodderive)
			setTypeParent(transformation.getEntite().getNom());

			// en fonction du type, on récupère l'objet
			if (getTypeParent().equals("Prelevement")) {
				setParentObject((Prelevement) ManagerLocator.getEntiteManager()
				.findObjectByEntiteAndIdManager(
						transformation.getEntite(),
						transformation.getObjetId()));
				groupPrlvtDerive.setVisible(true);
			} else if (getTypeParent().equals("Echantillon")) {
				setParentObject((Echantillon) ManagerLocator
				.getEntiteManager().findObjectByEntiteAndIdManager(
						transformation.getEntite(),
						transformation.getObjetId()));
				groupEchanDerive.setVisible(true);
			} else if (getTypeParent().equals("ProdDerive")) {
				setParentObject((ProdDerive) ManagerLocator
				.getEntiteManager().findObjectByEntiteAndIdManager(
						transformation.getEntite(),
						transformation.getObjetId()));
				groupDeriveDerive.setVisible(true);
			} 
		} 
		
		emplacementAdrl = null;
		
		// on récupère les objets associés
		if (!this.prodDerive.equals(new ProdDerive())) {
			// on extrait les dérivés et on initialise le nombre à afficher
			emplacementAdrl = ManagerLocator.getProdDeriveManager()
				.getEmplacementAdrlManager(prodDerive);
		}
		
		

		super.setObject(this.prodDerive);
		
		// statut EN COURS -> modification quantite impossible
		boolean isEncours = prodDerive
				.getObjetStatut() != null 
			&& prodDerive.getObjetStatut().getStatut().equals("ENCOURS");
		quantiteInitBoxDerive.setDisabled(isEncours);
		quantiteUnitesBoxDerive.setDisabled(isEncours);
		concentrationBoxDerive.setDisabled(isEncours);
		concentrationUnitesBoxDerive.setDisabled(isEncours);
		volumeInitBoxDerive.setDisabled(isEncours);
		volumeUnitesBoxDerive.setDisabled(isEncours);
	}
	
	@Override
	public TKdataObject getObject() {
		return this.prodDerive;
	}
	
	@Override
	public void setNewObject() {
		setObject(new ProdDerive());
	}
	
	public ProdDerive getProdDerive() {
		return prodDerive;
	}

	public void setProdDerive(ProdDerive pd) {
		this.prodDerive = pd;
	}
	
	@Override
	public TKdataObject getParentObject() {
		return parentObj;
	}

	@Override
	public void setParentObject(TKdataObject obj) {
		parentObj = obj;	
	}

	@Override
	public ProdDerive getCopy() {
		return (ProdDerive) super.getCopy();
	}

	@Override
	public ProdDeriveController getObjectTabController() {
		return (ProdDeriveController) super.getObjectTabController();
	}
	
	@Override
	public void switchToEditMode() {
		// Initialisation du mode (listes, valeurs...)
		initQuantiteVolumeAndConc();
		initEditableMode();
		showParentInformation();
		
		super.switchToEditMode();
		
		if (!getDroitOnAction("Collaborateur", "Consultation")) {
			operateurAideSaisieDerive.setVisible(false);
		}
		
		if (!getDroitOnAction("Prelevement", "Consultation")) {
			row1PrlvtDerive.setVisible(false);
		}
		
		if (!getDroitOnAction("Echantillon", "Consultation")) {
			row1EchanDerive.setVisible(false);
		}
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void onClick$create() {
		super.onClick$create();
	}
	
	@Override
	public void onClick$validate() {
		onBlur$dateStockCalBox();
		onBlur$dateTransfoCalBox();
		super.onClick$validate();
	}
	
	@Override
	public void createNewObject() {		
	}
	
	@Override
	protected void setEmptyToNulls() {
		// si le code_labo est vide, on l'enregistre comme null
		if (prodDerive.getCodeLabo().equals("")) {
			prodDerive.setCodeLabo(null);
		}
		
		if (this.prodDerive.getArchive() == null) {
			this.prodDerive.setArchive(false);
		}
		
		if (this.prodDerive.getEtatIncomplet() == null) {
			this.prodDerive.setEtatIncomplet(false);
		}
	}
	
	@Override
	public void updateObject() {
			
		// si le dérivé est issu d'un parent connu
		if (getParentObject() != null) {
			transformation.setQuantiteUnite(selectedTransfoQuantiteUnite);
		}
		
		// création du code échantillon en fct de celui du prlvt et
		// de celui saisi
//		StringBuffer sb = new StringBuffer();
//		sb.append(codePrefixe);
//		sb.append(".");
//		sb.append(codeSuffixe);
//		prodDerive.setCode(sb.toString());
		prodDerive.setCode(codePrefixe);

		// on remplit le dérivé en fonction des champs nulls
		setEmptyToNulls();
		setFieldsToUpperCase();
		
		// Gestion du collaborateur
		String selectedNomAndPremon = this.collabBoxDerive.getValue()
			.toUpperCase();
		this.collabBoxDerive.setValue(selectedNomAndPremon);
		int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
		if (ind > -1) {
			selectedCollaborateur = 
				collaborateurs.get(ind);				
		} else {
			selectedCollaborateur = null;
		}
		
		Emplacement emp = prodDerive.getEmplacement();
		
		// gestion de la non conformitée après traitement
		List<NonConformite> noconfsT = new ArrayList<NonConformite>();
		if (conformeTraitementBoxOui.isChecked()) {
			this.prodDerive.setConformeTraitement(true);
		} else if (conformeTraitementBoxNon.isChecked()) {
			this.prodDerive.setConformeTraitement(false);
			noconfsT.addAll(findSelectedNonConformitesTraitement());
		} else {
			this.prodDerive.setConformeTraitement(null);
		}
		
		// gestion de la non conformitée à la cession
		List<NonConformite> noconfsC = new ArrayList<NonConformite>();
		if (conformeCessionBoxOui.isChecked()) {
			this.prodDerive.setConformeCession(true);
		} else if (conformeCessionBoxNon.isChecked()) {
			this.prodDerive.setConformeCession(false);
			noconfsC.addAll(findSelectedNonConformitesCession());
		} else {
			this.prodDerive.setConformeCession(null);
		}

		// update de l'objet
		ManagerLocator.getProdDeriveManager()
								.updateObjectWithNonConformitesManager(
				prodDerive, prodDerive.getBanque(), selectedType, 
				findStatutForTKStockableObject(prodDerive),
				selectedCollaborateur, emp, selectedVolumeUnite,
				selectedConcUnite, selectedQuantiteUnite, 
				selectedModePrepaDerive,
				selectedQualite,
				prodDerive.getTransformation(), 
				getObjectTabController()
					.getFicheAnnotation().getValeursToCreateOrUpdate(),
				getObjectTabController()
					.getFicheAnnotation().getValeursToDelete(), null, 
				SessionUtils.getLoggedUser(sessionScope), true,
				null, SessionUtils.getSystemBaseDir(), 
				noconfsT, noconfsC);
		
		// s'il n'y a pas d'erreurs, on met à jour le parent : modif
		// de sa quantité et de son volume
		if (typeParent != null) {
			if (typeParent.equals("Prelevement")) {
				updateParentPrelevement();
			} else if (typeParent.equals("Echantillon")) {
				updateParentEchantillon(findStatutForTKStockableObject(getParentEchantillon()));
			} else if (typeParent.equals("ProdDerive")) {
				updateParentProduitDerive(findStatutForTKStockableObject(getParentProdDerive()));		
			}
		}
	}
		
	/**
	 * Mets a jour le prélèvement lié au produit dérivé.
	 */
	public void updateParentPrelevement() {
		
		// si la quantite max n'est pas null (le prlvt a une quantité)
		if (getQuantiteMax() != null) {
			// si une quantité a été saisie pour la transformation
			if (getTransformation().getQuantite() != null) {
				// maj de la quantité du prlvt
				Float tmp = 
					getQuantiteMax() - getTransformation().getQuantite();
				((Prelevement) getParentObject()).setQuantite(tmp);
			} else {
				((Prelevement) getParentObject()).setQuantite(getQuantiteMax());
			}
		}
		
		// Update du prlvt
		Maladie maladie = ((Prelevement) getParentObject()).getMaladie();
		ManagerLocator.getPrelevementManager().updateObjectManager(
				((Prelevement) getParentObject()), 
				((Prelevement) getParentObject()).getBanque(), 
				((Prelevement) getParentObject()).getNature(), 
				maladie, 
				((Prelevement) getParentObject()).getConsentType(), 
				((Prelevement) getParentObject()).getPreleveur(), 
				((Prelevement) getParentObject()).getServicePreleveur(), 
				((Prelevement) getParentObject()).getPrelevementType(), 
				((Prelevement) getParentObject()).getConditType(), 
				((Prelevement) getParentObject()).getConditMilieu(), 
				((Prelevement) getParentObject()).getTransporteur(), 
				((Prelevement) getParentObject()).getOperateur(), 
				((Prelevement) getParentObject()).getQuantiteUnite(), 
				null, null, null, null, null,
				SessionUtils.getLoggedUser(sessionScope), null, false,
				null, false);
		
		// on vérifie que l'on retrouve bien la page contenant la liste
		// des prélèvements
		if (getPrelevementController() != null) {		
			// update du prlvt dans la liste
			getPrelevementController().getListe()
				.updateObjectGridListFromOtherPage(((Prelevement)
													getParentObject()), true);
		}
	}
		
	/**
	 * Mets a jour l'échantillon lié au produit dérivé.
	 */
	public ObjetStatut prepareUpdateParentEchantillon() {
		
		// si la quantite max n'est pas null (l'echantillon a une quantité)
		if (getQuantiteMax() != null) {
			// si une quantité a été saisie pour la transformation
			if (getTransformation().getQuantite() != null) {
				// maj de la quantité de l'echantillon
				Float tmp = 
					getQuantiteMax() - getTransformation().getQuantite();
				((Echantillon) getParentObject()).setQuantite(tmp);
			} else {
				((Echantillon) getParentObject()).setQuantite(getQuantiteMax());
			}
		}
		
		ObjetStatut statut = 
				findStatutForTKStockableObject(((Echantillon) getParentObject()));
		
		// on conserve l'ancien emplacement pour pouvoir l'intégrer
		// à l'évènement de stockage
		oldEmplacement = ManagerLocator.getEchantillonManager()
				.getEmplacementManager((Echantillon) getParentObject());
		
		return statut;
	}
		

	public void updateParentEchantillon(ObjetStatut statut) {

		ObjetStatut oldStatut = ((Echantillon) getParentObject())
			.getObjetStatut();
		
		List<OperationType> ops = new ArrayList<OperationType>();
		if (statut.getStatut().equals("EPUISE")) {
			Emplacement empl = ManagerLocator.getEchantillonManager()
				.getEmplacementManager(((Echantillon) getParentObject()));
			if (empl != null) {
				empl.setEntite(null);
				empl.setObjetId(null);
				empl.setVide(true);
				((Echantillon) getParentObject()).setEmplacement(null);
				
				ManagerLocator.getEmplacementManager()
					.updateObjectManager(
							empl, 
							empl.getTerminale(), 
							null);
			}
			
			if (!statut.equals(oldStatut)) {
				ops = ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager("Destockage", true);
			}
		}
		
		// Update echantillon
		Prelevement prlvt = ManagerLocator.getEchantillonManager()
			.getPrelevementManager(((Echantillon) getParentObject()));
		ManagerLocator.getEchantillonManager().updateObjectManager(
				((Echantillon) getParentObject()), 
				((Echantillon) getParentObject()).getBanque(), 
				prlvt, 
				((Echantillon) getParentObject()).getCollaborateur(), 
				statut, 
				((Echantillon) getParentObject()).getEmplacement(), 
				((Echantillon) getParentObject()).getEchantillonType(), 
				null, null,
				((Echantillon) getParentObject()).getQuantiteUnite(), 
				((Echantillon) getParentObject()).getEchanQualite(), 
				((Echantillon) getParentObject()).getModePrepa(), 
				// null, null, 
				((Echantillon) getParentObject()).getReservation(), 
				null, null, null, null, 
				SessionUtils.getLoggedUser(sessionScope),
				false, ops, null);
		
		// on vérifie que l'on retrouve bien la page contenant la liste
		// des échantillons
		if (getEchantillonController() != null) {		
			// update de l'echantillon dans la liste
			getEchantillonController().getListe()
				.updateObjectGridListFromOtherPage(((Echantillon)
													getParentObject()), true);
		}
	}
		
	/**
	 * Mets a jour l'échantillon lié au produit dérivé.
	 */
	public ObjetStatut prepareUpdateParentProduitDerive() {
		
		// si la quantite max n'est pas null (le derive a une quantité)
		if (getQuantiteMax() != null) {
			// si une quantité a été saisie pour la transformation
			if (getTransformation().getQuantite() != null) {
				// maj de la quantité du prodderive
				Float tmp = 
					getQuantiteMax() - getTransformation().getQuantite();
				((ProdDerive) getParentObject()).setQuantite(tmp);
			} else {
				((ProdDerive) getParentObject()).setQuantite(getQuantiteMax());
			}
			
			// maj du volume
			if (((ProdDerive) getParentObject()).getQuantiteInit() != null 
				&& !((ProdDerive) getParentObject()).getQuantiteInit()
												.equals(new Float(0.0))) {
				if (((ProdDerive) getParentObject()).getVolumeInit() != null) {
					Float rapport = ((ProdDerive) getParentObject())
						.getQuantite() / ((ProdDerive) getParentObject())
														.getQuantiteInit();
					Float newVol = ((ProdDerive) getParentObject())
												.getVolumeInit() * rapport;
					((ProdDerive) getParentObject()).setVolume(newVol);
				}
			} else {
				((ProdDerive) getParentObject())
					.setVolume(((ProdDerive) getParentObject()).getQuantite());
			}
		}
		
		ObjetStatut statut = 
				findStatutForTKStockableObject(((ProdDerive) getParentObject()));
		
		// on conserve l'ancien emplacement pour pouvoir l'intégrer
		// à l'évènement de stockage
		oldEmplacement = ManagerLocator.getProdDeriveManager()
				.getEmplacementManager((ProdDerive) getParentObject());
		
		return statut;
		
	}
		
		/**
		 * Mets a jour l'échantillon lié au produit dérivé.
		 */
	public void updateParentProduitDerive(ObjetStatut statut) {
		ObjetStatut oldStatut = ((ProdDerive) getParentObject())
				.getObjetStatut();
		List<OperationType> ops = new ArrayList<OperationType>();
		if (statut.getStatut().equals("EPUISE")) {
			Emplacement empl = ManagerLocator.getProdDeriveManager()
				.getEmplacementManager(((ProdDerive) getParentObject()));
			if (empl != null) {
				empl.setEntite(null);
				empl.setObjetId(null);
				empl.setVide(true);
				((ProdDerive) getParentObject()).setEmplacement(null);
				
				ManagerLocator.getEmplacementManager()
					.updateObjectManager(
							empl, 
							empl.getTerminale(), 
							null);
			}
			
			if (!statut.equals(oldStatut)) {
				ops = ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager("Destockage", true);
			}
		}
		
		// update du dérvié
		ManagerLocator.getProdDeriveManager().updateObjectManager(
				((ProdDerive) getParentObject()), 
				((ProdDerive) getParentObject()).getBanque(), 
				((ProdDerive) getParentObject()).getProdType(), 
				statut, 
				((ProdDerive) getParentObject()).getCollaborateur(), 
				((ProdDerive) getParentObject()).getEmplacement(), 
				((ProdDerive) getParentObject()).getVolumeUnite(), 
				((ProdDerive) getParentObject()).getConcUnite(), 
				((ProdDerive) getParentObject()).getQuantiteUnite(), 
				((ProdDerive) getParentObject()).getModePrepaDerive(), 
				((ProdDerive) getParentObject()).getProdQualite(), 
				((ProdDerive) getParentObject()).getTransformation(), 
				null, null, null, null,
				((ProdDerive) getParentObject()).getReservation(), 
				SessionUtils.getLoggedUser(sessionScope),
				false, ops, null);
		
		// on vérifie que l'on retrouve bien la page contenant la liste
		// des derives
		if (getObjectTabController() != null) {
			
			// update du dérivé dans la liste
			getObjectTabController().getListe()
				.updateObjectGridListFromOtherPage(((ProdDerive)
						getParentObject()), true);
		}
	}
	
	
	/*************************************************************************/
	/************************** INITS ****************************************/
	/*************************************************************************/
	/**
	 * Méthode initialisant les champs de formulaire pour la quantité,
	 * le volume et la concentration.
	 */
	public void initQuantiteVolumeAndConc() {
		StringBuffer sb = new StringBuffer();
		if (this.prodDerive.getQuantite() != null) {
			sb.append(this.prodDerive.getQuantite());
		} else {
			sb.append("-");
		}
		valeurQuantiteRestante = sb.toString();
		sb.append(" / ");
		if (this.prodDerive.getQuantiteInit() != null) {
			sb.append(this.prodDerive.getQuantiteInit());
		} else {
			sb.append("-");
		}
		if (this.prodDerive.getQuantiteUnite() != null) {
			sb.append(" ");
			sb.append(this.prodDerive.getQuantiteUnite().getUnite());
		}
		valeurQuantite = sb.toString();

		sb = new StringBuffer();
		if (this.prodDerive.getVolume() != null) {
			sb.append(this.prodDerive.getVolume());
		} else {
			sb.append("-");
		}
		valeurVolumeRestant = sb.toString();
		sb.append(" / ");
		if (this.prodDerive.getVolumeInit() != null) {
			sb.append(this.prodDerive.getVolumeInit());
		} else {
			sb.append("-");
		}
		if (this.prodDerive.getVolumeUnite() != null) {
			sb.append(" ");
			sb.append(this.prodDerive.getVolumeUnite().getUnite());
		}
		valeurVolume = sb.toString();

		sb = new StringBuffer();
		if (this.prodDerive.getConc() != null) {
			sb.append(this.prodDerive.getConc());
		} else {
			sb.append("-");
		}
		if (this.prodDerive.getConcUnite() != null) {
			sb.append(" ");
			sb.append(this.prodDerive.getConcUnite().getUnite());
		}
		valeurConcentration = sb.toString();

		sb = new StringBuffer();
		if (this.transformation != null) {
			if (this.transformation.getQuantite() != null) {
				sb.append(this.transformation.getQuantite());
			} else {
				sb.append("-");
			}
			if (this.transformation.getQuantiteUnite() != null) {
				sb.append(" ");
				sb.append(this.transformation.getQuantiteUnite().getUnite());
			}
		}
		valeurTransfoQuantite = sb.toString();

	}

	/**
	 * Méthode pour l'initialisation du mode d'édition : récupération du contenu
	 * des listes déroulantes (types, qualités...).
	 */
	@SuppressWarnings("unchecked")
	public void initEditableMode() {
//		volume = null;
//		volumeInit = null;
//		quantite = null;
//		quantiteInit = null;
//		dateCongelation = null;
//		dateTransformation = null;
//		quantiteMax = null;
//		quantiteTransformation = null;
				
		codePrefixe = this.prodDerive.getCode();
		
		//codePrefixe = this.prodDerive.getCode().substring(0,
		//		this.prodDerive.getCode().lastIndexOf("."));
		
		// si le parent est un prlvt
		if (typeParent != null) {
			if (typeParent.equals("Prelevement")) {
				// l'unité de la transformation sera celle du parent
				selectedTransfoQuantiteUnite = 
					((Prelevement) getParentObject()).getQuantiteUnite();
				
				// on récupère la quantité et le volume disponible
				quantiteMax = ((Prelevement) getParentObject()).getQuantite();
				// si une quantité avait déjà été saisie pour la transfo
				if (quantiteMax != null 
						&& this.transformation.getQuantite() != null) {
					quantiteMax = 
						quantiteMax + this.transformation.getQuantite();
				}
				// si le parent est un echantillon
			} else if (typeParent.equals("Echantillon")) {
				// l'unité de la transformation sera celle du parent
				selectedTransfoQuantiteUnite = 
					((Echantillon) getParentObject()).getQuantiteUnite();
				
				// on récupère la quantité et le volume disponible
				quantiteMax = ((Echantillon) getParentObject()).getQuantite();
				// si une quantité avait déjà été saisie pour la transfo
				if (quantiteMax != null 
						&& this.transformation.getQuantite() != null) {
					quantiteMax = 
						quantiteMax + this.transformation.getQuantite();
				}
				// si le parent est un dérivé
			} else if (typeParent.equals("ProdDerive")) {
				// l'unité de la transformation sera celle du parent
				selectedTransfoQuantiteUnite = 
					((ProdDerive) getParentObject()).getQuantiteUnite();
				
				// on récupère la quantité et le volume disponible
				quantiteMax = ((ProdDerive) getParentObject()).getQuantite();
				// si une quantité avait déjà été saisie pour la transfo
				if (quantiteMax != null 
						&& this.transformation.getQuantite() != null) {
					quantiteMax = 
						quantiteMax + this.transformation.getQuantite();
				}
				// si un volume avait déjà été saisi pour la transfo
			}
		}

		types = (List<ProdType>) ManagerLocator.getProdTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		if (this.prodDerive.getProdType() != null) {
			selectedType = this.prodDerive.getProdType();
		} else if (!types.isEmpty()) {
			selectedType = types.get(0);
		}

		// on récupère les unités de quantité
		quantiteUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("masse", true);
		quantiteUnites.addAll(ManagerLocator.getUniteManager()
			.findByTypeLikeManager("discret", true));
		quantiteUnites.add(0, null);
		selectedQuantiteUnite = this.prodDerive.getQuantiteUnite();
		
		// on récupèé les unités de volume
		volumeUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("volume", true);
		volumeUnites.add(0, null);
		selectedVolumeUnite = this.prodDerive.getVolumeUnite();
		
		// on récupèé les unités de volume
		concUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("concentration", true);
		concUnites.add(0, null);
		selectedConcUnite = this.prodDerive.getConcUnite();

		qualites = (List<ProdQualite>) ManagerLocator.getProdQualiteManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		qualites.add(0, null);
		selectedQualite = this.prodDerive.getProdQualite();
		
		modePrepaDerives = (List<ModePrepaDerive>) 
			ManagerLocator.getModePrepaDeriveManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		modePrepaDerives.add(0, null);
		selectedModePrepaDerive = this.prodDerive.getModePrepaDerive();

		// init des collaborateurs
		nomsAndPrenoms = new ArrayList<String>();
		collaborateurs = ManagerLocator.getCollaborateurManager()
			.findAllActiveObjectsWithOrderManager();
		for (int i = 0; i < collaborateurs.size(); i++) {
			nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
		}
		collabBoxDerive.setModel(new CustomSimpleListModel(nomsAndPrenoms));
		
		if (this.prodDerive.getCollaborateur() != null
			&& collaborateurs.contains(this.prodDerive.getCollaborateur())) {
			selectedCollaborateur = this.prodDerive.getCollaborateur();
			collabBoxDerive.setValue(selectedCollaborateur.getNomAndPrenom());
		} else if (this.prodDerive.getCollaborateur() != null) {
			collaborateurs.add(this.prodDerive.getCollaborateur());
			selectedCollaborateur = this.prodDerive.getCollaborateur();
			nomsAndPrenoms.add(this.prodDerive.getCollaborateur()
				.getNomAndPrenom());
			collabBoxDerive.setModel(new CustomSimpleListModel(nomsAndPrenoms));
			collabBoxDerive.setValue(selectedCollaborateur.getNomAndPrenom());
		} else {
			collabBoxDerive.setValue("");
			selectedCollaborateur = null;
		}
		
		// si la quantité init est nulle mais pas la quantité restante
		// => quantiteInit = quantite
		if (this.prodDerive.getQuantite() != null
				&& this.prodDerive.getQuantiteInit() == null) {
			this.prodDerive.setQuantiteInit(
					this.prodDerive.getQuantite());
		}
		
		// si le volume init est null mais pas le volume restant
		// => volumeInit = volume
		if (this.prodDerive.getVolume() != null
				&& this.prodDerive.getVolumeInit() == null) {
			this.prodDerive.setVolumeInit(
					this.prodDerive.getVolume());
		}
		
		initNonConformites();
	}
	
	/*************************************************************************/
	/************************** PARENT    ************************************/
	/*************************************************************************/
	/**
	 * Cette méthode va afficher les données du parent du dérivé
	 * en fonction de son type (prlvt, échantillon, prodderive).
	 */
	public void showParentInformation() {
		
		 if (getTypeParent() == null || getTypeParent().equals("Aucun")) {
			for (int i = 0; i < getObjLabelsPrlvtParent().length; i++) {
				getObjLabelsPrlvtParent()[i].detach();
				getObjLabelsEchanParent()[i].detach();
				getObjLabelsDeriveParent()[i].detach();
			}
			for (int i = 0; i < getObjLabelsTransformation().length; i++) {
				getObjLabelsTransformation()[i].setVisible(false);
			}
			transformationInconnueLabel.setVisible(true);
		} else if (getTypeParent().equals("Prelevement")) {
			for (int i = 0; i < getObjLabelsPrlvtParent().length; i++) {
				getObjLabelsPrlvtParent()[i].setVisible(true);
				getObjLabelsEchanParent()[i].detach();
				getObjLabelsDeriveParent()[i].detach();
			}
			for (int i = 0; i < getObjLabelsTransformation().length; i++) {
				getObjLabelsTransformation()[i].setVisible(true);
			}
			transformationInconnueLabel.detach();
		} else if (getTypeParent().equals("Echantillon")) {
			for (int i = 0; i < getObjLabelsPrlvtParent().length; i++) {
				getObjLabelsPrlvtParent()[i].detach();
				getObjLabelsEchanParent()[i].setVisible(true);
				getObjLabelsDeriveParent()[i].detach();
			}
			for (int i = 0; i < getObjLabelsTransformation().length; i++) {
				getObjLabelsTransformation()[i].setVisible(true);
			}
			transformationInconnueLabel.detach();
		} else if (getTypeParent().equals("ProdDerive")) {
			for (int i = 0; i < getObjLabelsPrlvtParent().length; i++) {
				getObjLabelsPrlvtParent()[i].detach();
				getObjLabelsEchanParent()[i].detach();
				getObjLabelsDeriveParent()[i].setVisible(true);
			}
			for (int i = 0; i < getObjLabelsTransformation().length; i++) {
				getObjLabelsTransformation()[i].setVisible(true);
			}
			transformationInconnueLabel.detach();
		}
	}
	
	/**
	 * Affiche la fiche d'un prélèvement.
	 * @param event Event : clique sur un lien prlvtLinkDerive.
	 * @throws Exception
	 */
	public void onClick$prlvtLinkDerive(Event event) throws Exception {
		PrelevementController tabController = 
			(PrelevementController) PrelevementController
											.backToMe(getMainWindow(), page);
		
		if (getParentPrlvt() != null) {
			tabController.switchToFicheStaticMode(getParentPrlvt());
		}
	}
	
	/**
	 * Affiche la fiche d'un échantillon.
	 * @param event Event : clique sur un lien echanLinkDerive.
	 * @throws Exception
	 */
	public void onClick$echanLinkDerive(Event event) throws Exception {		
		EchantillonController tabController = 
			(EchantillonController) EchantillonController.backToMe(
					getMainWindow(), page);
		
		if (getParentEchantillon() != null) {
			tabController.switchToFicheStaticMode(getParentEchantillon());
		}
		
	}
	
	/*************************************************************************/
	/************************** OPERATEUR ************************************/
	/*************************************************************************/
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * operateurAideSaisieDerive. Cette méthode va créer une nouvelle
	 * fenêtre contenant l'aide pour la sélection d'un collaborateur.
	 * @version 2.1
	 */
	public void onClick$operateurAideSaisieDerive() {
		// on récupère le collaborateur actuellement sélectionné
		// pour l'afficher dans la modale
		List<Object> old = new ArrayList<Object>();
		if (getSelectedCollaborateur() != null) {
			old.add(getSelectedCollaborateur());
		}
		// on va appliquer à cette fenêtre un filtre sur le service de
		// la banque
		List<Object> list = new ArrayList<Object>();
		
		// choix de la banque si toutes collections, modification 
		// reste possible
		if (getBanque() == null) { // echantillon en creation
			if (SessionUtils.getSelectedBanques(sessionScope)
								.get(0).getProprietaire() != null) {
				list.add(SessionUtils.getSelectedBanques(sessionScope)
										.get(0).getProprietaire());
			}
		} else { // echantillon en modification
			if (getBanque().getProprietaire() != null) {
				list.add(getBanque().getProprietaire());
			}
		}
		
		// ouvre la modale
		openCollaborationsWindow(page, 
								"general.recherche",
								"select", null,
								"Collaborateur", list, 
								Path.getPath(self),
								old);
	}
	
	/**
	 * Méthode appelée par la fenêtre CollaborationsController quand
	 * l'utilisateur sélectionne un collaborateur.
	 * @param e Event contenant le collaborateur sélectionné.
	 */
	public void onGetObjectFromSelection(Event e) {
		
		// les collaborateurs peuvent être modifiés dans la fenêtre
		// d'aide => maj de ceux-ci
		nomsAndPrenoms = new ArrayList<String>();
		collaborateurs = ManagerLocator.getCollaborateurManager()
			.findAllActiveObjectsWithOrderManager();
		for (int i = 0; i < collaborateurs.size(); i++) {
			nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
		}
		collabBoxDerive.setModel(new CustomSimpleListModel(
				nomsAndPrenoms));			
		
		if (this.prodDerive.getCollaborateur() != null 
			&& !collaborateurs.contains(this.prodDerive.getCollaborateur())) {
			collaborateurs.add(this.prodDerive.getCollaborateur());
			nomsAndPrenoms.add(this.prodDerive.getCollaborateur()
					.getNomAndPrenom());
		}
		
		// si un collaborateur a été sélectionné
		if (e.getData() != null) {
			Collaborateur coll = (Collaborateur) e.getData();
			if (nomsAndPrenoms.contains(coll.getNomAndPrenom())) {
				int ind = nomsAndPrenoms.indexOf(coll.getNomAndPrenom());
				selectedCollaborateur = collaborateurs.get(ind);
				collabBoxDerive
					.setValue(selectedCollaborateur.getNomAndPrenom());
			}
		}
	}
	
	/*************************************************************************/
	/************************** FORMATTERS ***********************************/
	/*************************************************************************/
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * codeBoxDerive. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$codeBoxDerive() {
		codeBoxDerive.setValue(codeBoxDerive.getValue().toUpperCase().trim());
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * codePrefixeLabelDerive. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$codePrefixeLabelDerive() {
		codePrefixeLabelDerive.setValue(
				codePrefixeLabelDerive.getValue().toUpperCase().trim());
	}
	
	@Override
	public void setFieldsToUpperCase() {
		if (this.prodDerive.getCode() != null) {
			this.prodDerive.setCode(
				this.prodDerive.getCode().toUpperCase().trim());
		}
	}

	@Override
	public void setFocusOnElement() {
		codePrefixeLabelDerive.setFocus(true);
	}
	
	/**
	 * Méthode appelée lors d'une modification du champ quantiteInitBoxDerive.
	 * En fonction de la quantité initiale saisie, la quantité restante sera
	 * mise à jour.
	 */
	public void onBlur$quantiteInitBoxDerive() {
		
		BigDecimal value = quantiteInitBoxDerive.getValue();
		Float tmp = null;
		if (value != null) {
			tmp = value.floatValue();
		}
		updateQuantite(tmp);
		
	}
	
	/**
	 * Méthode appelée lors d'une modification du champ volumeInitBoxDerive.
	 * En fonction du volume initial saisi, le volume restant sera
	 * mis à jour.
	 */
	public void onBlur$volumeInitBoxDerive() {
		
		BigDecimal value = volumeInitBoxDerive.getValue();
		Float tmp = null;
		if (value != null) {
			tmp = value.floatValue();
		}
		updateVolume(tmp);
		
		if (prodDerive.getVolumeInit() != null) {
			
			if (prodDerive.getConc() != null) {
				
				Float res = prodDerive.getVolumeInit() * prodDerive.getConc();
				prodDerive.setQuantiteInit(res);
				updateQuantite(prodDerive.getQuantiteInit());
				
			}
		}
	}
	
	/**
	 * Méthode appelée lors d'une modification du champ quantiteInitBoxDerive.
	 * En fonction de la quantité initiale saisie, la quantité restante sera
	 * mise à jour.
	 */
	public void onBlur$concentrationBoxDerive() {
		
		if (prodDerive.getConc() != null) {
			
			if (prodDerive.getVolumeInit() != null) {
				
				Float res = prodDerive.getVolumeInit() * prodDerive.getConc();
				prodDerive.setQuantiteInit(res);
				updateQuantite(prodDerive.getQuantiteInit());
				
			}
		}
	}
	
	/**
	 * Cette méthode va mettre à jour la quantité en fonction de la
	 * quantité initiale passée en paramètre.
	 * @param value Quantité intiale.
	 */
	public void updateQuantite(Float value) {
		
		// si la quantité initiale a changé, on modifie la quantité
		// restante en fonction
		Float zero = new Float(0.0);
		if (value != null) {
			prodDerive.setQuantiteInit(value);
			Float diff = this.prodDerive.getQuantiteInit();
			// si une quantité initiale était saisie
			if (getCopy().getQuantiteInit() != null) {
				diff = diff - getCopy().getQuantiteInit();
			} else if (getCopy().getQuantiteInit() == null
					&& getCopy().getQuantite() != null) {
				prodDerive.setQuantite(prodDerive.getQuantiteInit());
				diff = zero;
				valeurQuantiteRestante = prodDerive.getQuantite().toString();
			}
			// on vérifie qu'un changement a eu lieu
			if (diff != zero) {
				Float restant;
				if (getCopy().getQuantite() != null) {
					restant = getCopy().getQuantite() + diff;
				} else {
					restant = diff;
				}
				// la quantité restante ne doit pas etre negative
				if (restant >= zero) {
					prodDerive.setQuantite(restant);
				} else {
					prodDerive.setQuantite(zero);
				}
				valeurQuantiteRestante = prodDerive.getQuantite().toString();
			}
		} else {
			prodDerive.setQuantiteInit(null);
			prodDerive.setQuantite(null);
			valeurQuantiteRestante = "-";
		}
		
	}
	
	/**
	 * Cette méthode va mettre à jour le volume en fonction du
	 * volume initial passé en paramètre.
	 * @param value Volume intial.
	 */
	public void updateVolume(Float value) {
		
		// si le volume initial a changé, on modifie le volume
		// restant en fonction
		Float zero = new Float(0.0);
		if (value != null) {
			prodDerive.setVolumeInit(value);
			Float diff = this.prodDerive.getVolumeInit();
			// si un volume initial était saisi
			if (getCopy().getVolumeInit() != null) {
				diff = diff - getCopy().getVolumeInit();
			} else if (getCopy().getVolumeInit() == null
					&& getCopy().getVolume() != null) {
				prodDerive.setVolume(prodDerive.getVolumeInit());
				diff = zero;
				valeurVolumeRestant = prodDerive.getVolume().toString();
			}
			// on vérifie qu'un changement a eu lieu
			if (diff != zero) {
				Float restant;
				if (getCopy().getVolume() != null) {
					restant = getCopy().getVolume() + diff;
				} else {
					restant = diff;
				}
				// le volume restant ne doit pas etre negatif
				if (restant >= zero) {
					prodDerive.setVolume(restant);
				} else {
					prodDerive.setVolume(zero);
				}
				valeurVolumeRestant = prodDerive.getVolume().toString();
			}
		} else {
			prodDerive.setVolumeInit(null);
			prodDerive.setVolume(null);
			valeurVolumeRestant = "-";
		}
		
	}
	
	/**
	 * Méthode appelée lors de la sélection d'un volume dans la liste 
	 * volumeUnitesBoxDerive. Les unités de concentration et quantité 
	 * seront mises à jour en fonction de cette sélection.
	 */
	public void onSelect$volumeUnitesBoxDerive() {
		if (getSelectedVolumeUnite() != null) {
			if (getSelectedVolumeUnite().getUnite()
										.toLowerCase().equals("ml")) {
				
				setSelectedConcUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("mg/ml", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("mg", true).get(0));
				
			} else if (getSelectedVolumeUnite().getUnite()
					.toLowerCase().equals("µl")) {
				
				setSelectedConcUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µg/µl", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µg", true).get(0));
				
			} else if (getSelectedVolumeUnite().getUnite()
					.toLowerCase().equals("nl")) {
				
				setSelectedConcUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ng/nl", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ng", true).get(0));
				
			}
		}
	}
	
	/**
	 * Méthode appelée lors de la sélection d'une concentration dans la liste 
	 * concentrationUnitesBoxDerive. Les unités de volume et de quantité 
	 * seront mises à jour en fonction de cette sélection.
	 */
	public void onSelect$concentrationUnitesBoxDerive() {
		if (getSelectedConcUnite() != null) {
			if (getSelectedConcUnite().getUnite()
										.toLowerCase().equals("mg/ml")) {
				
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ml", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("mg", true).get(0));
				
			} else if (getSelectedConcUnite().getUnite().toLowerCase()
					.equals("µg/µl")) {
				
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µl", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µg", true).get(0));
				
			} else if (getSelectedConcUnite().getUnite().toLowerCase()
													.equals("ng/nl")) {
				
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("nl", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ng", true).get(0));
				
			} else if (getSelectedConcUnite().getUnite().toLowerCase()
													.equals("ng/µl")) {
				
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µl", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ng", true).get(0));
				
			} else if (getSelectedConcUnite().getUnite().toLowerCase()
													.equals("µg/ml")) {
				
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ml", true).get(0));
				setSelectedQuantiteUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µg", true).get(0));
				
			}
			
		}
	}
	
	/**
	 * Méthode appelée lors de la sélection d'une quantité dans la liste 
	 * quantiteUnitesBoxDerive. Les unités de concentration et de volume 
	 * seront mises à jour en fonction de cette sélection.
	 */
	public void onSelect$quantiteUnitesBoxDerive() {
		if (getSelectedQuantiteUnite() != null) {
			if (getSelectedQuantiteUnite().getUnite()
											.toLowerCase().equals("mg")) {
				
				setSelectedConcUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("mg/ml", true).get(0));
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("ml", true).get(0));
				
			} else if (getSelectedQuantiteUnite().getUnite()
											.toLowerCase().equals("µg")) {
				
				setSelectedConcUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µg/µl", true).get(0));
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
					.findByUniteLikeManager("µl", true).get(0));
				
			} else if (getSelectedQuantiteUnite().getUnite()
											.toLowerCase().equals("ng")) {
					
				setSelectedConcUnite(ManagerLocator.getUniteManager()
							.findByUniteLikeManager("ng/nl", true).get(0));
				setSelectedVolumeUnite(ManagerLocator.getUniteManager()
								.findByUniteLikeManager("nl", true).get(0));
				
			}
		}
	}
	
	/**
	 * Formate la date du prélèvement parent.
	 * @return Date du prélèvement formatée.
	 */
	public String getDatePrelevementFormated() {
		if (getParentObject() != null 
				&& getParentObject() instanceof Prelevement) {
			return ObjectTypesFormatters
				.dateRenderer2(((Prelevement) getParentObject())
												.getDatePrelevement());
		} else {
			return null;
		}
	}
	
	/**
	 * Formate la date de l'échantillon parent.
	 * @return Date de l'échantillon formatée.
	 */
	public String getDateEchantillonFormated() {
		if (getParentObject() != null 
				&& getParentObject() instanceof Echantillon) {
			return ObjectTypesFormatters
				.dateRenderer2(((Echantillon) getParentObject())
													.getDateStock());
		} else {
			return null;
		}
	}
	
	/**
	 * Formate la date du dérivé parent.
	 * @return Date du dérivé formatée.
	 */
	public String getDateProdDeriveFormated() {
		if (getParentObject() != null 
				&& getParentObject() instanceof ProdDerive) {
			return ObjectTypesFormatters
				.dateRenderer2(((ProdDerive) getParentObject())
													.getDateStock());
		} else {
			return null;
		}
	}
	
	public String getSClassOperateur() {
		if (this.prodDerive != null) {
			return ObjectTypesFormatters
				.sClassCollaborateur(this.prodDerive.getCollaborateur());
		} else {
			return null;
		}
	}
	

	
	/**
	 * Recopie les informations d'une transformation dans sa copy.
	 */
	public void copyTransformation() {

		copyTransformation.setObjetId(transformation.getObjetId());
		copyTransformation.setEntite(transformation.getEntite());
		copyTransformation.setQuantite(transformation.getQuantite());
		copyTransformation.setQuantiteUnite(transformation.getQuantiteUnite());

	}

	/**
	 * Annule toutes les modifs faites sur la transformation.
	 */
	public void revertTransformation() {

		if (transformation != null) {
			transformation.setTransformationId(
					copyTransformation.getTransformationId());
			transformation.setObjetId(copyTransformation.getObjetId());
			transformation.setEntite(copyTransformation.getEntite());
			transformation.setQuantite(copyTransformation.getQuantite());
			transformation.setQuantiteUnite(copyTransformation
					.getQuantiteUnite());
		}

	}	
	
	public void initNonConformites() {
		// init des non conformites de traitement
		nonConformitesTraitement = ManagerLocator.getNonConformiteManager()
			.findByPlateformeEntiteAndTypeStringManager(
					SessionUtils.getPlateforme(sessionScope), 
					"Traitement", getObjectTabController().getEntiteTab());
		selectedNonConformiteTraitement = null;
		if (prodDerive != null && prodDerive.getProdDeriveId() != null) {
			List<ObjetNonConforme> tmp = ManagerLocator
				.getObjetNonConformeManager()
				.findByObjetAndTypeManager(prodDerive, "Traitement");
			if (tmp.size() > 0) {
				if (nonConformitesTraitement.contains(tmp.get(0)
						.getNonConformite())) {
					selectedNonConformiteTraitement = 
						tmp.get(0).getNonConformite();
				}
			}
		}
		if (this.prodDerive.getConformeTraitement() != null) {
			if (this.prodDerive.getConformeTraitement()) {
				conformeTraitementBoxOui.setChecked(true);
				conformeTraitementBoxNon.setChecked(false);
				conformeTraitementBox.setVisible(false);
			} else {
				conformeTraitementBoxOui.setChecked(false);
				conformeTraitementBoxNon.setChecked(true);
				conformeTraitementBox.setVisible(true);
			}
		}
		
		// init des non conformites à la cession
		nonConformitesCession = ManagerLocator.getNonConformiteManager()
			.findByPlateformeEntiteAndTypeStringManager(
					SessionUtils.getPlateforme(sessionScope), 
					"Cession", getObjectTabController().getEntiteTab());
		selectedNonConformiteCession = null;
		if (prodDerive != null && prodDerive.getProdDeriveId() != null) {
			List<ObjetNonConforme> tmp = ManagerLocator
				.getObjetNonConformeManager()
				.findByObjetAndTypeManager(prodDerive, "Cession");
			if (tmp.size() > 0) {
				if (nonConformitesCession.contains(tmp.get(0)
						.getNonConformite())) {
					selectedNonConformiteCession = 
						tmp.get(0).getNonConformite();
				}
			}
		}
		if (this.prodDerive.getConformeCession() != null) {
			if (this.prodDerive.getConformeCession()) {
				conformeCessionBoxOui.setChecked(true);
				conformeCessionBoxNon.setChecked(false);
				conformeCessionBox.setVisible(false);
			} else {
				conformeCessionBoxOui.setChecked(false);
				conformeCessionBoxNon.setChecked(true);
				conformeCessionBox.setVisible(true);
			}
		}
		
		getBinder().loadComponent(nonConformitesTraitementBox);
		getBinder().loadComponent(nonConformitesCessionBox);
		
		selectNonConformites();
	}
	
	/**
	 * Select les non conformites dans la dropdown list.
	 * @param risks liste à selectionner
	 */
	@SuppressWarnings("unchecked")
	public void selectNonConformites() {
		List<NonConformite> ncfTrait = new ArrayList<NonConformite>();
		List<NonConformite> ncfCess = new ArrayList<NonConformite>();
		if (prodDerive != null 
				&& prodDerive.getProdDeriveId() != null) {
			List<ObjetNonConforme> list = ManagerLocator
				.getObjetNonConformeManager()
				.findByObjetAndTypeManager(prodDerive, "Traitement");
			for (int i = 0; i < list.size(); i++) {
				ncfTrait.add(list.get(i).getNonConformite());
			}
			
			list = ManagerLocator
				.getObjetNonConformeManager()
				.findByObjetAndTypeManager(prodDerive, "Cession");
			for (int i = 0; i < list.size(); i++) {
				ncfCess.add(list.get(i).getNonConformite());
			}
		}
		
		((Selectable<NonConformite>) nonConformitesTraitementBox.getModel())
													.setSelection(ncfTrait);
		((Selectable<NonConformite>) nonConformitesCessionBox.getModel())
													.setSelection(ncfCess);

		
		getBinder().loadAttribute(nonConformitesTraitementBox, "selectedItems");
		getBinder().loadAttribute(nonConformitesCessionBox, "selectedItems");
	}
	
	public void onCheck$conformeTraitementBoxOui() {
		if (conformeTraitementBoxOui.isChecked()) {
			conformeTraitementBoxNon.setChecked(false);
			conformeTraitementBox.setVisible(false);
		}
	}
	
	public void onCheck$conformeTraitementBoxNon() {
		if (conformeTraitementBoxNon.isChecked()) {
			conformeTraitementBoxOui.setChecked(false);
			conformeTraitementBox.setVisible(true);
		} else {
			conformeTraitementBox.setVisible(false);
		}
	}
	
	public void onCheck$conformeCessionBoxOui() {
		if (conformeCessionBoxOui.isChecked()) {
			conformeCessionBoxNon.setChecked(false);
			conformeCessionBox.setVisible(false);
		}
	}
	
	public void onCheck$conformeCessionBoxNon() {
		if (conformeCessionBoxNon.isChecked()) {
			conformeCessionBoxOui.setChecked(false);
			conformeCessionBox.setVisible(true);
		} else {
			conformeCessionBox.setVisible(false);
		}
	}
	
	/**
	 * Retourne les non conformites sélectionnées.
	 * @return
	 */
	public List<NonConformite> findSelectedNonConformitesTraitement() {
		List<NonConformite> ncf = new ArrayList<NonConformite>();
		Iterator<Listitem> its = nonConformitesTraitementBox
			.getSelectedItems()
			.iterator();
		while (its.hasNext()) {
			ncf.add(nonConformitesTraitement.get(
					nonConformitesTraitementBox
					.getItems().indexOf(its.next())));
		}
		return ncf;
	}
	
	/**
	 * Retourne les non conformites sélectionnées.
	 * @return
	 */
	public List<NonConformite> findSelectedNonConformitesCession() {
		List<NonConformite> ncf = new ArrayList<NonConformite>();
		Iterator<Listitem> its = nonConformitesCessionBox
			.getSelectedItems()
			.iterator();
		while (its.hasNext()) {
			ncf.add(nonConformitesCession.get(
					nonConformitesCessionBox
					.getItems().indexOf(its.next())));
		}
		return ncf;
	}
	
	/*********************************************************/
	/********************** ACCESSEURS. **********************/
	/*********************************************************/	
	public Transformation getTransformation() {
		return transformation;
	}

	public void setTransformation(Transformation transfo) {
		this.transformation = transfo;
	}

	public Prelevement getParentPrlvt() {
		if (getParentObject() instanceof Prelevement) {
			return (Prelevement) getParentObject();
		} else {
			return new Prelevement();
		}
	}

	public Echantillon getParentEchantillon() {
		if (getParentObject() instanceof Echantillon) {
			return (Echantillon) getParentObject();
		} else {
			return new Echantillon();
		}
	}

	public ProdDerive getParentProdDerive() {
		if (getParentObject() instanceof ProdDerive) {
			return (ProdDerive) getParentObject();
		} else {
			return new ProdDerive();
		} 
	}
	
	

	public Component[] getObjLabelsPrlvtParent() {
		return objLabelsPrlvtParent;
	}

	public void setObjLabelsPrlvtParent(Component[] opp) {
		this.objLabelsPrlvtParent = opp;
	}

	public Component[] getObjLabelsEchanParent() {
		return objLabelsEchanParent;
	}

	public void setObjLabelsEchanParent(Component[] oep) {
		this.objLabelsEchanParent = oep;
	}

	public Component[] getObjLabelsDeriveParent() {
		return objLabelsDeriveParent;
	}

	public void setObjLabelsDeriveParent(Component[] op) {
		this.objLabelsDeriveParent = op;
	}

	public Component[] getObjLabelsTransformation() {
		return objLabelsTransformation;
	}

	public void setObjLabelsTransformation(Component[] ot) {
		this.objLabelsTransformation = ot;
	}

	public List<ProdType> getTypes() {
		return types;
	}

	public void setTypes(List<ProdType> t) {
		this.types = t;
	}

	public ProdType getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(ProdType selected) {
		this.selectedType = selected;
	}

	public Unite getSelectedQuantiteUnite() {
		return selectedQuantiteUnite;
	}

	public void setSelectedQuantiteUnite(Unite selected) {
		this.selectedQuantiteUnite = selected;
	}

	public Unite getSelectedVolumeUnite() {
		return selectedVolumeUnite;
	}

	public void setSelectedVolumeUnite(Unite selected) {
		this.selectedVolumeUnite = selected;
	}

	public Unite getSelectedConcUnite() {
		return selectedConcUnite;
	}

	public void setSelectedConcUnite(Unite selected) {
		this.selectedConcUnite = selected;
	}

	public Unite getSelectedTransfoQuantiteUnite() {
		return selectedTransfoQuantiteUnite;
	}

	public void setSelectedTransfoQuantiteUnite(Unite selected) {
		this.selectedTransfoQuantiteUnite = selected;
	}

	public List<ProdQualite> getQualites() {
		return qualites;
	}

	public void setQualites(List<ProdQualite> q) {
		this.qualites = q;
	}

	public ProdQualite getSelectedQualite() {
		return selectedQualite;
	}

	public void setSelectedQualite(ProdQualite selected) {
		this.selectedQualite = selected;
	}

	public List<Collaborateur> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(List<Collaborateur> c) {
		this.collaborateurs = c;
	}

	public List<Collaborateur> getMultiCollaborateurs() {
		return multiCollaborateurs;
	}

	public void setMultiCollaborateurs(List<Collaborateur> multi) {
		this.multiCollaborateurs = multi;
	}

	public Collaborateur getSelectedCollaborateur() {
		return selectedCollaborateur;
	}

	public void setSelectedCollaborateur(Collaborateur selected) {
		this.selectedCollaborateur = selected;
	}

	public List<String> getNomsAndPrenoms() {
		return nomsAndPrenoms;
	}

	public void setNomsAndPrenoms(List<String> nAndPs) {
		this.nomsAndPrenoms = nAndPs;
	}

	public String getValeurQuantite() {
		return valeurQuantite;
	}

	public void setValeurQuantite(String valeur) {
		this.valeurQuantite = valeur;
	}

	public String getValeurVolume() {
		return valeurVolume;
	}

	public void setValeurVolume(String valeur) {
		this.valeurVolume = valeur;
	}

	public String getValeurQuantiteRestante() {
		return valeurQuantiteRestante;
	}

	public void setValeurQuantiteRestante(String valeurRestante) {
		this.valeurQuantiteRestante = valeurRestante;
	}

	public String getValeurVolumeRestant() {
		return valeurVolumeRestant;
	}

	public void setValeurVolumeRestant(String valeurRestante) {
		this.valeurVolumeRestant = valeurRestante;
	}

	public String getValeurConcentration() {
		return valeurConcentration;
	}

	public void setValeurConcentration(String valeur) {
		this.valeurConcentration = valeur;
	}

	public String getValeurTransfoQuantite() {
		return valeurTransfoQuantite;
	}

	public void setValeurTransfoQuantite(String valeur) {
		this.valeurTransfoQuantite = valeur;
	}

	public String getCodePrefixe() {
		return codePrefixe;
	}

	public void setCodePrefixe(String code) {
		this.codePrefixe = code;
	}

	public String getCodeSuffixe() {
		return codeSuffixe;
	}

	public void setCodeSuffixe(String code) {
		this.codeSuffixe = code;
	}

	public String getTypeParent() {
		return typeParent;
	}

	public void setTypeParent(String s) {
		this.typeParent = s;
	}

	public String getCodeParent() {
		return codeParent;
	}

	public void setCodeParent(String code) {
		this.codeParent = code;
	}

	public Float getVolume() {
		return volume;
	}

	public void setVolume(Float v) {
		this.volume = v;
	}

	public Float getVolumeInit() {
		return volumeInit;
	}

	public void setVolumeInit(Float v) {
		this.volumeInit = v;
	}

	public Float getQuantite() {
		return quantite;
	}

	public void setQuantite(Float q) {
		this.quantite = q;
	}

	public Float getQuantiteInit() {
		return quantiteInit;
	}

	public void setQuantiteInit(Float qInit) {
		this.quantiteInit = qInit;
	}

	public Float getQuantiteMax() {
		return quantiteMax;
	}

	public void setQuantiteMax(Float max) {
		this.quantiteMax = max;
	}

	public Float getQuantiteTransformation() {
		return quantiteTransformation;
	}

	public void setQuantiteTransformation(Float qTransfo) {
		this.quantiteTransformation = qTransfo;
	}

	public Date getDateCongelation() {
		return dateCongelation;
	}

	public void setDateCongelation(Date date) {
		this.dateCongelation = date;
	}

	public Date getDateTransformation() {
		return dateTransformation;
	}

	public void setDateTransformation(Date date) {
		this.dateTransformation = date;
	}

	public List<Unite> getQuantiteUnites() {
		return quantiteUnites;
	}

	public void setQuantiteUnites(List<Unite> unites) {
		this.quantiteUnites = unites;
	}

	public List<Unite> getVolumeUnites() {
		return volumeUnites;
	}

	public void setVolumeUnites(List<Unite> unites) {
		this.volumeUnites = unites;
	}

	public List<Unite> getConcUnites() {
		return concUnites;
	}

	public void setConcUnites(List<Unite> unites) {
		this.concUnites = unites;
	}
	
	/*********************************************************/
	/********************** VALIDATION ***********************/
	/*********************************************************/
	private Constraint cttVolume = new ConstVolume();
	private Constraint cttVolumeInit = new ConstVolumeInit();
	private Constraint cttQuantite = new ConstQuantite();
	private Constraint cttQuantiteInit = new ConstQuantiteInit();
	private Constraint cttQuantiteTransfo = new ConstQuantiteTransformation();

	public Constraint getCttVolume() {
		return cttVolume;
	}

	public Constraint getCttVolumeInit() {
		return cttVolumeInit;
	}

	public Constraint getCttQuantite() {
		return cttQuantite;
	}

	public void setCttQuantite(Constraint ctt) {
		this.cttQuantite = ctt;
	}

	public Constraint getCttQuantiteInit() {
		return cttQuantiteInit;
	}
	
	public Constraint getCttQuantiteTransfo() {
		return cttQuantiteTransfo;
	}

	/**
	 * Contrainte vérifiant que le volume n'est pas supérieur
	 * au volume init.
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstVolume implements Constraint {
		/**
		 * Méthode de validation du champ volumeBox.
		 */
		public void validate(Component comp, Object value) {
			// on récupère la valeur dans volumeBox
			BigDecimal volumeValue = (BigDecimal) value;
			// Si le volumeInit est null (1ère édition de la page)
			// on va récupérer la valeur du champ volumeInitBox
			if (volumeInit == null) { 
				// on enlève la contrainte de volumeInitBox pour
				// pouvoir récupérer sa valeur
				volumeInitBoxDerive.setConstraint("");
				volumeInitBoxDerive.clearErrorMessage();
				BigDecimal volumeInitValue = volumeInitBoxDerive.getValue();
				// on remet la contrainte
				volumeInitBoxDerive.setConstraint(cttVolumeInit);
				// si la valeur n'est pas nulle, on initialise volumeInit
				if (volumeInitValue != null) {
					volumeInit = volumeInitValue.floatValue();
				}
			}
			
			// si une valeur est saisie dans le champ volumeBox
			if (volumeValue != null) {
				volume = volumeValue.floatValue();
				// le volume doit être positif
				if (volume < 0) {
					throw new WrongValueException(
							comp, "Seul les nombres non-négatifs " 
							+ "sont autorisés.");
				}
				// si une valeur est saisie dans le champ volumeInitBox
				if (volumeInit != null) {
					// si le volume est supérieur au volumeInit
					if (volume > volumeInit) {
						throw new WrongValueException(
								comp, "Le volume actuel ne peut " 
								+ "pas être supérieur au "
								+ "volume initial.");
					} else {
						// sinon on enlève toutes les erreurs affichées
						BigDecimal decimal = new BigDecimal(volumeInit);
						Clients.clearWrongValue(volumeInitBoxDerive);
						volumeInitBoxDerive.setConstraint("");
						volumeInitBoxDerive.setValue(decimal);
						volumeInitBoxDerive.setConstraint(cttVolumeInit);
					}
				} else {
					// si aucun volumeInit n'est saisie, on enlève les
					// erreurs
					Clients.clearWrongValue(volumeInitBoxDerive);
					volumeInitBoxDerive.setConstraint("");
					//volumeInitBoxDerive.setValue(null);
					volumeInitBoxDerive.setValue("");
					volumeInitBoxDerive.setConstraint(cttVolumeInit);
				}
			} else {
				// si aucun volume n'est saisie, on enlève les
				// erreurs
				volume = null;
				if (volumeInit != null) {
					BigDecimal decimal = new BigDecimal(volumeInit);
					Clients.clearWrongValue(volumeInitBoxDerive);
					volumeInitBoxDerive.setConstraint("");
					volumeInitBoxDerive.setValue(decimal);
					volumeInitBoxDerive.setConstraint(cttVolumeInit);
				} else {
					Clients.clearWrongValue(volumeInitBoxDerive);
					volumeInitBoxDerive.setConstraint("");
					//volumeInitBoxDerive.setValue(null);
					volumeInitBoxDerive.setValue("");
					volumeInitBoxDerive.setConstraint(cttVolumeInit);
				}
			}
		}
	}
	
	
	/**
	 * Contrainte vérifiant que le volumeInit n'est pas inférieur
	 * au volume.
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstVolumeInit implements Constraint {
		/**
		 * Méthode de validation du champ volumeInitBox.
		 */
		public void validate(Component comp, Object value) {
			// on récupère la valeur dans volumeInitBox
			BigDecimal volumeInitValue = (BigDecimal) value;

			// si une valeur est saisie dans le champ volumeInitBox
			if (volumeInitValue != null) {
				volumeInit = volumeInitValue.floatValue();
				// le volumeInit doit être positif
				if (volumeInit < 0) {
					throw new WrongValueException(
							comp, Labels.getLabel(
							"validation.negative.value"));
				} else {

					// si l'échantillon avait deja une valeur de volume et de
					// volumeInit
					if (getCopy().getVolume() != null 
							&& getCopy().getVolumeInit() != null) {
						
						Float diff = volumeInitValue.floatValue() 
							- getCopy().getVolumeInit();
						Float restant = getCopy().getVolume() + diff;
						Float zero = new Float(0.0);
						// si le volume restant est négatif
						if (restant < zero) {
							// on calcule le volumeInit minimal autorisé
							Float min = getCopy().getVolumeInit() 
												- getCopy().getVolume();
							throw new WrongValueException(
									comp, Labels.getLabel(
									"validation.invalid.volume.init")
									+ " " + min);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Contrainte vérifiant que la quantité n'est pas supérieure
	 * à la quantité init.
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstQuantite implements Constraint {
		/**
		 * Méthode de validation du champ quantiteBox.
		 */
		public void validate(Component comp, Object value) {
			// on récupère la valeur dans quantiteBox
			BigDecimal quantiteValue = (BigDecimal) value;
			// Si la QuantiteInit est null (1ère édition de la page)
			// on va récupérer la valeur du champ quantiteInitBox
			if (quantiteInit == null) { 
				// on enlève la contrainte de quantiteInitBox pour
				// pouvoir récupérer sa valeur
				quantiteInitBoxDerive.setConstraint("");
				quantiteInitBoxDerive.clearErrorMessage();
				BigDecimal quantiteInitValue = quantiteInitBoxDerive.getValue();
				// on remet la contrainte
				quantiteInitBoxDerive.setConstraint(cttQuantiteInit);
				// si la valeur n'est pas nulle, on initialise quantiteInit
				if (quantiteInitValue != null) {
					quantiteInit = quantiteInitValue.floatValue();
				}
			}
			
			// si une valeur est saisie dans le champ quantiteBox
			if (quantiteValue != null) {
				quantite = quantiteValue.floatValue();
				// la quantite doit être positive
				if (quantite < 0) {
					throw new WrongValueException(
					comp, "Seul les nombres non-négatifs sont autorisés.");
				}
				// si une valeur est saisie dans le champ quantiteInitBox
				if (quantiteInit != null) {
					// si la quantite est supérieure à la quantiteInit
					if (quantite > quantiteInit) {
						throw new WrongValueException(
						comp, "La quantité actuelle ne peut pas "
						+ "être supérieure à la quantité initiale.");
					} else {
						// sinon on enlève toutes les erreurs affichées
						BigDecimal decimal = new BigDecimal(quantiteInit);
						Clients.clearWrongValue(quantiteInitBoxDerive);
						quantiteInitBoxDerive.setConstraint("");
						quantiteInitBoxDerive.setValue(decimal);
						quantiteInitBoxDerive.setConstraint(cttQuantiteInit);
					}
				} else {
					// si aucune quantiteInit n'est saisie, on enlève les
					// erreurs
					Clients.clearWrongValue(quantiteInitBoxDerive);
					quantiteInitBoxDerive.setConstraint("");
					//quantiteInitBoxDerive.setValue(null);
					quantiteInitBoxDerive.setValue("");
					quantiteInitBoxDerive.setConstraint(cttQuantiteInit);
				}
			} else {
				// si aucune quantite n'est saisie, on enlève les
				// erreurs
				quantite = null;
				if (quantiteInit != null) {
					BigDecimal decimal = new BigDecimal(quantiteInit);
					Clients.clearWrongValue(quantiteInitBoxDerive);
					quantiteInitBoxDerive.setConstraint("");
					quantiteInitBoxDerive.setValue(decimal);
					quantiteInitBoxDerive.setConstraint(cttQuantiteInit);
				} else {
					Clients.clearWrongValue(quantiteInitBoxDerive);
					quantiteInitBoxDerive.setConstraint("");
					//quantiteInitBoxDerive.setValue(null);
					quantiteInitBoxDerive.setValue("");
					quantiteInitBoxDerive.setConstraint(cttQuantiteInit);
				}
			}
		}
	}
	
	
	/**
	 * Contrainte vérifiant que la quantiteInit n'est pas inférieure
	 * à la quantite.
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstQuantiteInit implements Constraint {
		/**
		 * Méthode de validation du champ quantiteInitBox.
		 */
		public void validate(Component comp, Object value) {
			// on récupère la valeur dans quantiteInitBox
			BigDecimal quantiteInitValue = (BigDecimal) value;

			// si une valeur est saisie dans le champ quantiteInitBox
			if (quantiteInitValue != null) {
				quantiteInit = quantiteInitValue.floatValue();
				// la quantiteInit doit être positive
				if (quantiteInit < 0) {
					throw new WrongValueException(
						comp, Labels.getLabel(
						"validation.negative.value"));
				} else {

					// si l'échantillon avait deja une valeur de quantite et de
					// quantiteInit
					if (getCopy().getQuantite() != null 
							&& getCopy().getQuantiteInit() != null) {
						
						Float diff = quantiteInitValue.floatValue() 
							- getCopy().getQuantiteInit();
						Float restant = getCopy().getQuantite() + diff;
						Float zero = new Float(0.0);
						// si la Quantite restante est négative
						if (restant < zero) {
							// on calcule la quantiteInit minimale autorisée
							Float min = getCopy().getQuantiteInit() 
								- getCopy().getQuantite();
							throw new WrongValueException(
									comp, Labels.getLabel(
									"validation.invalid.quantite.init") 
									+ " " + min);
						} else {
							// la quantité init doit respecter la formule :
							// qteInit = volInit * concentration
							if (prodDerive.getVolumeInit() != null
									&& prodDerive.getConc() != null) {
								
								Float res = prodDerive.getVolumeInit() 
									* prodDerive.getConc();
								res = ObjectTypesFormatters.floor(res, 3);
								
								if (!quantiteInit.equals(res)) {
									throw new WrongValueException(
									comp, Labels.getLabel(
									"validation.invalid.formule" 
									+ ".quantite.init"));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Contrainte vérifiant que la quantite de transformation n'est 
	 * pas inférieure à 0 et n'est pas supérieure à celle du parent.
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstQuantiteTransformation implements Constraint {
		/**
		 * Méthode de validation du champ quantiteInitBox.
		 */
		public void validate(Component comp, Object value) {
			BigDecimal quantiteTransfoValue = (BigDecimal) value;
			if (quantiteTransfoValue != null) {
				quantiteTransformation = quantiteTransfoValue.floatValue();
				
				if (quantiteTransformation < 0) {
					throw new WrongValueException(
					comp, "Seul les nombres positifs sont acceptés.");
				} else {
					// sinon on enlève toutes les erreurs affichées
					BigDecimal decimal = 
						new BigDecimal(quantiteTransformation);
					Clients.clearWrongValue(transfoQuantiteBoxDerive);
					transfoQuantiteBoxDerive.setConstraint("");
					transfoQuantiteBoxDerive.setValue(decimal);
					transfoQuantiteBoxDerive
						.setConstraint(cttQuantiteTransfo);
					
					if (quantiteMax != null
							&& quantiteTransformation > quantiteMax) {
						StringBuffer sb = new StringBuffer();
						sb.append("La quantité utilisée ne peut dépasser " 
								+ "celle disponible dans ");
						
						if (typeParent.equals("Prelevement")) {
							sb.append("le prélèvement parent : ");
							sb.append(quantiteMax);
							sb.append(((Prelevement) getParentObject())
									.getQuantiteUnite().getUnite());
							sb.append(".");
						} else if (typeParent.equals("Echantillon")) {
							sb.append("l'échantillon parent : ");
							sb.append(quantiteMax);
							sb.append(((Echantillon) getParentObject())
									.getQuantiteUnite().getUnite());
							sb.append(".");
						} else if (typeParent.equals("ProdDerive")) {
							sb.append("le produit dérivé parent : ");
							sb.append(quantiteMax);
							sb.append(((ProdDerive) getParentObject())
									.getQuantiteUnite().getUnite());
							sb.append(".");
						}
						
						throw new WrongValueException(
								comp, sb.toString());
					} else {
						// sinon on enlève toutes les erreurs affichées
						decimal = new BigDecimal(quantiteTransformation);
						Clients.clearWrongValue(transfoQuantiteBoxDerive);
						transfoQuantiteBoxDerive.setConstraint("");
						transfoQuantiteBoxDerive.setValue(decimal);
						transfoQuantiteBoxDerive
							.setConstraint(cttQuantiteTransfo);
					}
				}
			}
		}
	}

	
//	/**
//	 * Contrainte vérifiant que le volume de transformation n'est 
//	 * pas inférieur à 0 et n'est pas supérieur à celui du parent.
//	 * @author Pierre Ventadour.
//	 *
//	 */
//	public class ConstVolumeTransformation implements Constraint {
//		/**
//		 * Méthode de validation du champ quantiteInitBox.
//		 */
//		public void validate(Component comp, Object value) {
//			BigDecimal volumeTransfoValue = (BigDecimal) value;
//			if (volumeTransfoValue != null) {
//				volumeTransformation = volumeTransfoValue.floatValue();
//				
//				if (volumeTransformation < 0) {
//					throw new WrongValueException(
//					comp, "Seul les nombres positifs sont acceptés.");
//				} else {
//					// sinon on enlève toutes les erreurs affichées
//					BigDecimal decimal = 
//						new BigDecimal(volumeTransformation);
//					transfoVolumeBoxDerive.Clients.clearWrongValue();
//					transfoVolumeBoxDerive.setConstraint("");
//					transfoVolumeBoxDerive.setValue(decimal);
//					transfoVolumeBoxDerive
//						.setConstraint(cttVolumeTransfo);
//					
//					if (volumeMax != null
//							&& volumeTransformation > volumeMax) {
//						
//						StringBuffer sb = new StringBuffer();
//						sb.append("Le volume utilisé ne peut dépasser " 
//								+ "celui disponible dans ");
//						
//						if (typeParent.equals("Prelevement")) {
//							sb.append("le prélèvement parent : ");
//							sb.append(volumeMax);
//							sb.append(parentPrlvt.getVolumeUnite()
//									.getUnite());
//							sb.append(".");
//						} else if (typeParent.equals("Echantillon")) {
//							sb.append("l'échantillon parent : ");
//							sb.append(volumeMax);
//							sb.append(parentEchantillon.getVolumeUnite()
//									.getUnite());
//							sb.append(".");
//						} else if (typeParent.equals("ProdDerive")) {
//							sb.append("le produit dérivé parent : ");
//							sb.append(volumeMax);
//							sb.append(parentProdDerive.getVolumeUnite()
//									.getUnite());
//							sb.append(".");
//						}
//						
//						throw new WrongValueException(
//								comp, sb.toString());
//						
//					} else {
//						// sinon on enlève toutes les erreurs affichées
//						decimal = new BigDecimal(volumeTransformation);
//						transfoVolumeBoxDerive.Clients.clearWrongValue();
//						transfoVolumeBoxDerive.setConstraint("");
//						transfoVolumeBoxDerive.setValue(decimal);
//						transfoVolumeBoxDerive
//							.setConstraint(cttVolumeTransfo);
//					}
//				}
//			}
//		}
//	}
//	private Constraint cttVolumeTransfo = new ConstVolumeTransformation();
//	
//	/**
//	 * Contrainte vérifiant que la date de congélation n'est pas 
//	 * supérieure à la date de transformation.
//	 * @author Pierre Ventadour.
//	 *
//	 */
//	public class ConstDateCongelation implements Constraint {
//		/**
//		 * Méthode de validation du champ dateBox.
//		 */
//		public void validate(Component comp, Object value) {
//			// on récupère la valeur dans dateBox
//			dateCongelation = (Date) value;
//			// Si la date de transformation est null (1ère édition
//			// de la page) on va récupérer la valeur du champ 
//			// dateTransfoBox
//			if (dateTransformation == null) { 
//				// on enlève la contrainte de dateTransfoBox pour
//				// pouvoir récupérer sa valeur
//				dateTransfoBoxDerive.setConstraint("");
//				dateTransfoBoxDerive.clearErrorMessage();
//				dateTransformation = dateTransfoBoxDerive.getValue();
//				// on remet la contrainte
//				dateTransfoBoxDerive.setConstraint(cttDateTransformation);
//			}
//			
//			// si une valeur est saisie dans le champ dateBox
//			if (dateCongelation != null) {
//				// si une valeur est saisie dans le champ dateTransfoBox
//				if (dateTransformation != null) {
//					// si la dateCongelation est supérieure à la 
//					// dateTransformation
//					if (dateCongelation.before(dateTransformation)) {
//						throw new WrongValueException(
//						comp, "La date de transformation ne peut pas "
//						+ "être plus récente que la date de congélation.");
//					} else {
//						// sinon on enlève toutes les erreurs affichées
//						dateTransfoBoxDerive.Clients.clearWrongValue();
//						dateTransfoBoxDerive.setConstraint("");
//						dateTransfoBoxDerive.setValue(dateTransformation);
//						dateTransfoBoxDerive.setConstraint(
//								cttDateTransformation);
//					}
//				} else {
//					// si aucune dateTransformation n'est saisie, on enlève les
//					// erreurs
//					dateTransfoBoxDerive.Clients.clearWrongValue();
//					dateTransfoBoxDerive.setConstraint("");
//					dateTransfoBoxDerive.setValue(null);
//					dateTransfoBoxDerive.setConstraint(cttDateTransformation);
//				}
//			} else {
//				// si aucune dateCongelation n'est saisie, on enlève les
//				// erreurs
//				dateCongelation = null;
//				if (dateTransformation != null) {
//					dateTransfoBoxDerive.Clients.clearWrongValue();
//					dateTransfoBoxDerive.setConstraint("");
//					dateTransfoBoxDerive.setValue(dateTransformation);
//					dateTransfoBoxDerive.setConstraint(cttDateTransformation);
//				} else {
//					dateTransfoBoxDerive.Clients.clearWrongValue();
//					dateTransfoBoxDerive.setConstraint("");
//					dateTransfoBoxDerive.setValue(null);
//					dateTransfoBoxDerive.setConstraint(cttDateTransformation);
//				}
//			}
//		}
//	}
//	private Constraint cttDateCongelation = new ConstDateCongelation();
	
//	/**
//	 * Contrainte vérifiant que la date de congélation n'est pas 
//	 * supérieure à la date de transformation.
//	 * @author Pierre Ventadour.
//	 *
//	 */
//	public class ConstDateTransformation implements Constraint {
//		/**
//		 * Méthode de validation du champ dateTransfoBox.
//		 */
//		public void validate(Component comp, Object value) {
//			// on récupère la valeur dans dateTransfoBox
//			dateTransformation = (Date) value;
//			// Si la date de congélation est null (1ère édition
//			// de la page) on va récupérer la valeur du champ 
//			// dateBox
//			if (dateCongelation == null) { 
//				// on enlève la contrainte de dateTransfoBox pour
//				// pouvoir récupérer sa valeur
//				dateBoxDerive.setConstraint("");
//				dateBoxDerive.clearErrorMessage();
//				dateCongelation = dateBoxDerive.getValue();
//				// on remet la contrainte
//				dateBoxDerive.setConstraint(cttDateCongelation);
//			}
//			
//			// si une valeur est saisie dans le champ dateTransfoBox
//			if (dateTransformation != null) {
//				// si une valeur est saisie dans le champ dateBox
//				if (dateCongelation != null) {
//					// si la dateCongelation est supérieure à la 
//					// dateTransformation
//					if (dateCongelation.before(dateTransformation)) {
//						throw new WrongValueException(
//						comp, "La date de transformation ne peut "
//						+ "pas être plus récente que la date de congélation.");
//					} else {
//						// sinon on enlève toutes les erreurs affichées
//						dateBoxDerive.Clients.clearWrongValue();
//						dateBoxDerive.setConstraint("");
//						dateBoxDerive.setValue(dateCongelation);
//						dateBoxDerive.setConstraint(cttDateCongelation);
//					}
//				} else {
//					// si aucune dateTransformation n'est saisie, on enlève les
//					// erreurs
//					dateBoxDerive.Clients.clearWrongValue();
//					dateBoxDerive.setConstraint("");
//					dateBoxDerive.setValue(null);
//					dateBoxDerive.setConstraint(cttDateCongelation);
//				}
//			} else {
//				// si aucune dateCongelation n'est saisie, on enlève les
//				// erreurs
//				dateTransformation = null;
//				if (dateCongelation != null) {
//					dateBoxDerive.Clients.clearWrongValue();
//					dateBoxDerive.setConstraint("");
//					dateBoxDerive.setValue(dateCongelation);
//					dateBoxDerive.setConstraint(cttDateCongelation);
//				} else {
//					dateBoxDerive.Clients.clearWrongValue();
//					dateBoxDerive.setConstraint("");
//					dateBoxDerive.setValue(null);
//					dateBoxDerive.setConstraint(cttDateCongelation);
//				}
//			}
//		}
//	}
//	private Constraint cttDateTransformation = new ConstDateTransformation();
	

	public ConstCode getCodePrefixConstraint() {
		return ProdDeriveConstraints.getCodePrefixConstraint();
	}
	
	public ConstCode getCodePrefixNullableConstraint() {
		return ProdDeriveConstraints.getCodePrefixNullableConstraint();
	}
	
	public ConstCode getCodeSuffixConstraint() {
		return ProdDeriveConstraints.getCodeSuffixConstraint();
	}
	
	public ConstCode getCodeNullConstraint() {
		return ProdDeriveConstraints.getCodeNullConstraint();
	}

	
	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(codePrefixeLabelDerive);
		Clients.clearWrongValue(codeBoxDerive);
		Clients.clearWrongValue(volumeBoxDerive);
		Clients.clearWrongValue(volumeInitBoxDerive);
		Clients.clearWrongValue(quantiteBoxDerive);
		Clients.clearWrongValue(quantiteInitBoxDerive);
		Clients.clearWrongValue(dateStockCalBox);
		Clients.clearWrongValue(dateTransfoCalBox);
		Clients.clearWrongValue(transfoQuantiteBoxDerive);
		Clients.clearWrongValue(codeLaboBoxDerive);
		Clients.clearWrongValue(concentrationBoxDerive);
	}

	/**
	 * Applique la validation sur la date.
	 */
	public void onBlur$dateTransfoCalBox() {
		Datebox box = (Datebox) dateTransfoCalBox
			.getFirstChild().getFirstChild();
		boolean badDateFormat = false;
		if (box.getErrorMessage() != null
				&& box.getErrorMessage().contains(
						box.getFormat())) {
			badDateFormat = true;
		}
		if (!badDateFormat) {
			dateTransfoCalBox.clearErrorMessage(
					dateTransfoCalBox.getValue());
			validateCoherenceDate(dateTransfoCalBox, 
					dateTransfoCalBox.getValue());
		} else {
			throw new WrongValueException(
					dateTransfoCalBox, 
					Labels.getLabel("validation.invalid.date"));
		}
	}
	
	
	/**
	 * Applique la validation sur la date.
	 */
	public void onBlur$dateStockCalBox() {
		Datebox box = (Datebox) dateStockCalBox
			.getFirstChild().getFirstChild();
		boolean badDateFormat = false;
		if (box.getErrorMessage() != null
				&& box.getErrorMessage().contains(
						box.getFormat())) {
			badDateFormat = true;
		}
		if (!badDateFormat) {
			dateStockCalBox.clearErrorMessage(
					dateStockCalBox.getValue());
			validateCoherenceDate(dateStockCalBox,
					dateStockCalBox.getValue());
		} else {
			throw new WrongValueException(
					dateStockCalBox, 
					Labels.getLabel("validation.invalid.date"));
		}
	}
	

	@Override
	protected void validateCoherenceDate(Component comp, Object value) {
		Errors errs = null;
		String field = "";
				
		if (value == null || value.equals("")) { 
			((CalendarBox) comp)
				.clearErrorMessage(((CalendarBox) comp).getValue());
			((CalendarBox) comp).setValue(null);
			if (comp.getId().equals("dateStockCalBox")) {
				prodDerive.setDateStock(null);
			} else if (comp.getId().equals("dateTransfoCalBox")) {
				prodDerive.setDateTransformation(null);
			}
		} else {		
			// date transformation
			if (comp.getId().equals("dateTransfoCalBox")) {
				field = "dateTransformation";
				if (this.prodDerive.getProdDeriveId() == null
						&& (!(new Prelevement()).equals(getParentObject())
						|| !getParentObject().equals(new Echantillon())
							|| !getParentObject().equals(new ProdDerive()))) {
					transformation
						.setEntite(findEntiteAndSetObjetIdForTransformation());
					this.prodDerive.setTransformation(transformation);
				}
				getProdDerive().setDateTransformation((Calendar) value);
					
				errs = ManagerLocator.getProdDeriveValidator()
								.checkDateTransfoCoherence(this.prodDerive);
			}
			
			// date stockage
			if (comp.getId().equals("dateStockCalBox")) {
				field = "dateStock";
				if (this.prodDerive.getProdDeriveId() == null
						&& getParentObject() != null
						&& (!getParentObject().equals(new Prelevement())
						|| !getParentObject().equals(new Echantillon())
							|| !getParentObject().equals(new ProdDerive()))) {
					transformation
						.setEntite(findEntiteAndSetObjetIdForTransformation());
					this.prodDerive.setTransformation(transformation);
				}
				this.prodDerive.setDateStock((Calendar) value);
					
				errs = ManagerLocator.getProdDeriveValidator()
								.checkDateStockCoherence(this.prodDerive);
			}
			
			if (errs != null && errs.hasErrors()) {
				Clients.scrollIntoView(comp);
				throw new WrongValueException(
					comp, ObjectTypesFormatters.handleErrors(errs, field));
			}				
		} 
	}
	
	/**
	 * Recupere l'entite en fonction du type du parent et assigne l'Id
	 * du parent a la transformation.
	 * Renvoie null si le type parent n'est pas specifie. Utile pour 
	 * la validation des dates.
	 * @return Entite du parent
	 */
	public Entite findEntiteAndSetObjetIdForTransformation() {
		// en fonction du parent, on va rechercher l'objet Entité afin
		// de remplir la nouvelle transformation
		Entite entite = null;
		if (getParentObject() != null) {
			getTransformation().setObjetId(((TKAnnotableObject) 
									getParentObject()).listableObjectId());
			entite = ManagerLocator.getEntiteManager()
							.findByNomManager(((TKAnnotableObject) 
									getParentObject()).entiteNom()).get(0);
		}
		return entite;
	}
	
	

	public String getEmplacementAdrl() {
		boolean isAnonyme = false;
		if (sessionScope.containsKey("Anonyme")
				&& (Boolean) sessionScope.get("Anonyme")) {
			isAnonyme = true;
		} else {
			isAnonyme = false;
		}
		
		if (isAnonyme) {
			return "-";
		} else {
			return emplacementAdrl;
		}
	}

	public void setEmplacementAdrl(String eAdrl) {
		this.emplacementAdrl = eAdrl;
	}
	
	public String getTemperatureFormated() {
		Float temp = null;
		
		if (this.prodDerive != null 
				&& this.prodDerive.getProdDeriveId() != null) {
			Emplacement emp = ManagerLocator.getProdDeriveManager()
				.getEmplacementManager(prodDerive);
			
			if (emp != null) {
				Conteneur cont = ManagerLocator.getEmplacementManager()
					.getConteneurManager(emp);
				if (cont != null && cont.getTemp() != null) {
					temp = cont.getTemp();
				}
			}
		}
		
		return ObjectTypesFormatters.formatTemperature(temp);
	}
	
	@SuppressWarnings("unchecked")
	public void initModificationMultipleMode() {
		
		types = (List<ProdType>) ManagerLocator.getProdTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));

		// on récupère les unités de quantité
		quantiteUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("masse", true);
		quantiteUnites = ManagerLocator.getUniteManager()
				.findByTypeLikeManager("discret", true);
		quantiteUnites.add(0, null);
		
		// on récupère les unités de volume
		volumeUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("volume", true);
		volumeUnites.add(0, null);
		
		// on récupère les unités de volume
		concUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("concentration", true);
		concUnites.add(0, null);
		
		qualites = (List<ProdQualite>) ManagerLocator.getProdQualiteManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		qualites.add(0, null);

		// init des collaborateurs
		multiCollaborateurs = ManagerLocator.getCollaborateurManager()
			.findAllActiveObjectsWithOrderManager();
		// multiCollaborateurs.add(0, null);
				
	}
	
	public PrelevementController getPrelevementController() {
		if (getObjectTabController()
							.getReferencingObjectControllers() != null) {
			for (int i = 0; i < getObjectTabController()
					.getReferencingObjectControllers().size(); i++) {
				if (getObjectTabController()
					.getReferencingObjectControllers().get(i) 
									instanceof PrelevementController) {
					return (PrelevementController) getObjectTabController()
									.getReferencingObjectControllers().get(i);
				}
			}
		} 
		return null;
	}
	
	public EchantillonController getEchantillonController() {
		if (getObjectTabController()
							.getReferencingObjectControllers() != null) {
			for (int i = 0; i < getObjectTabController()
					.getReferencingObjectControllers().size(); i++) {
				if (getObjectTabController()
					.getReferencingObjectControllers().get(i) 
									instanceof EchantillonController) {
					return (EchantillonController) getObjectTabController()
									.getReferencingObjectControllers().get(i);
				}
			}
		} 
		return null;
	}

	public List<ModePrepaDerive> getModePrepaDerives() {
		return modePrepaDerives;
	}

	public void setModePrepaDerives(List<ModePrepaDerive> mDerives) {
		this.modePrepaDerives = mDerives;
	}

	public ModePrepaDerive getSelectedModePrepaDerive() {
		return selectedModePrepaDerive;
	}

	public void setSelectedModePrepaDerive(ModePrepaDerive selected) {
		this.selectedModePrepaDerive = selected;
	}
	
	public Emplacement getOldEmplacement() {
		return oldEmplacement;
	}

	public List<NonConformite> getNonConformitesTraitement() {
		return nonConformitesTraitement;
	}

	public void setNonConformitesTraitement(
			List<NonConformite> n) {
		this.nonConformitesTraitement = n;
	}

	public NonConformite getSelectedNonConformiteTraitement() {
		return selectedNonConformiteTraitement;
	}

	public void setSelectedNonConformiteTraitement(
			NonConformite n) {
		this.selectedNonConformiteTraitement = n;
	}

	public List<NonConformite> getNonConformitesCession() {
		return nonConformitesCession;
	}

	public void setNonConformitesCession(List<NonConformite> n) {
		this.nonConformitesCession = n;
	}

	public NonConformite getSelectedNonConformiteCession() {
		return selectedNonConformiteCession;
	}

	public void setSelectedNonConformiteCession(
			NonConformite s) {
		this.selectedNonConformiteCession = s;
	}

	public Set<Listitem> getSelectedNonConformitesTraitementItem() {
		return selectedNonConformitesTraitementItem;
	}

	public void setSelectedNonConformitesTraitementItem(
			Set<Listitem> s) {
		this.selectedNonConformitesTraitementItem = s;
	}

	public Set<Listitem> getSelectedNonConformitesCessionItem() {
		return selectedNonConformitesCessionItem;
	}

	public void setSelectedNonConformitesCessionItem(
			Set<Listitem> s) {
		this.selectedNonConformitesCessionItem = s;
	}
	
	public String getObjetStatut() {
		return ObjectTypesFormatters
				.ILNObjectStatut(((ProdDerive) getObject()).getObjetStatut());
	}
}
