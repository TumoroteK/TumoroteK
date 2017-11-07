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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.CouleurItemRenderer;
import fr.aphp.tumorotek.decorator.EnceinteDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.InvalidParentException;
import fr.aphp.tumorotek.manager.exception.InvalidPositionException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.stockage.EnceinteNode;

/**
 * @version 2.1
 * @author Mathieu BARTHELEMY
 *
 */
public class FicheEnceinte extends AbstractFicheCombineStockageController {
	
	//private Log log = LogFactory.getLog(FicheEnceinte.class);

	private static final long serialVersionUID = -7592543406710901340L;

	private Component[] objRowsComponents;
	private Button deplacer;
	private Menuitem numerotation;
	private Menuitem taille;
	
	/**
	 *  Static Components pour le mode static.
	 */
	private Label nomLabel;
	private Label aliasLabel;
	private Label typeLabel;
	private Label nbPlacesLabel;
	private Label entiteReserveeLabel;
	private Label banquesReserveesLabel;
	private Row entiteReserveeRow;
	private Row banquesReserveesRow;
	private Row rowNomAlias;
	private Row rowType;
	private Row rowNbPlaces;
	private Row rowNewEnceinte;
	private Menubar menuBar;
	private Image couleurEnceinteImg;
	
	/**
	 *  Editable components : mode d'édition ou de création.
	 */
	private Label nomRequired;
	private Label typeRequired;
	private Textbox nomBox;
	private Textbox aliasBox;
	private Listbox enceinteTypeBox;
	private Listbox entiteBox;
	private Row rowEntiteEdit;
	private Intbox nbPlacesBox;
	private Label nbPlacesRequired;
	// composants pour la création
	private Component[] objCreateComponents;
	private Row createEnceintes;
	private Button createEnceintesButton;
	private Component[] objArborescenceComponents;
	private Row rowCreateTitle1;
	private Row rowCreateTitle2;
	private Row rowEnceintesEdit;
	private Grid enceintesListEdit;
	private Row rowTerminaleType;
	private Listbox typesTerminaleBox;
	private Row rowNbPlacesTerminale;
	private Row rowTerminaleNumerotation;
	private Listbox numerotationsBox;
	private Intbox nbPlacesEachEnceinteBox;
	private Listbox couleurEnceinteBox;
	
	/**
	 * Compnents pour le mode déplacement.
	 */
	private Component[] objDeplacementComponents;
	private Row rowEmplacementTitle1;
	private Row rowAdresseActuelle;
	private Row rowPositionActuelle;
	private Row rowEmplacementTitle2;
	private Row rowAdresseDestination;
	private Row rowPositionDestination;
	private Row rowContenuDestination;
	private Row rowErreurDestination;
	private Row rowNomDepart;
	private Row rowNomDestination;
	private Button validateDeplacement;
	private Button revertDeplacement;
	private Label adresseDestinationLabel;
	private Label positionDestinationLabel;
	private Label contenuDestinationLabel;
	private Textbox nomDepartBox;
	private Textbox nomDestinationBox;
	
	/**
	 *  Objets Principaux.
	 */
	private Enceinte enceinte;
	private Enceinte enceintePere;
	private Conteneur conteneur;
	private Integer position;
	private EnceinteNode enceinteDestination;
	private Enceinte enceinteAEchanger;
	
	/**
	 *  Associations.
	 */
	private List<Banque> banques = new ArrayList<Banque>();
	private List<EnceinteType> types = new ArrayList<EnceinteType>();
	private EnceinteType selectedEnceinteType;
	private List<EnceinteType> childEnceintesTypes = 
		new ArrayList<EnceinteType>();
	private EnceinteType selectedChildEnceinteType;
	private List<Entite> entites = new ArrayList<Entite>();
	private Entite selectedEntite;
	private List<EnceinteDecorator> decoratedEnceintes = 
		new ArrayList<EnceinteDecorator>();
	private List<TerminaleType> terminaleTypes = new ArrayList<TerminaleType>();
	private TerminaleType selectedTerminaleType;
	private List<TerminaleNumerotation> numerotations = 
		new ArrayList<TerminaleNumerotation>();
	private TerminaleNumerotation selectedTerminaleNumerotation;
	private List<Enceinte> enceintes = new ArrayList<Enceinte>();
	private Terminale terminale;
	private CouleurItemRenderer couleurRenderer = new CouleurItemRenderer();
	private List<Couleur> couleurs = new ArrayList<Couleur>();
	private Couleur selectedCouleur;
	private HashMap<TKStockableObject, Emplacement> emplForRetours = 
		new HashMap<TKStockableObject, Emplacement>();
	
	/**
	 *  Variables formulaire.
	 */
	private String entiteReservee;
	private String banquesReservees;
	private String adresseActuelle = "";
	private String adresseDestination = "";
	private Integer positionDestination;
	private String contenuDestination = "";
	private Boolean arborescenceCreee = false;
	private static String colorSrc = "/images/icones/boites/big_pastille";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setDeletionMessage("message.deletion.enceinte");
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
				this.nomLabel,
				this.aliasLabel,
				this.typeLabel,
				this.entiteReserveeLabel,
				this.banquesReserveesLabel,
				this.menuBar,
				this.couleurEnceinteImg,
				this.incidentsList
			});
		
		setObjBoxsComponents(new Component[]{
				this.nomBox,
				this.aliasBox,
				this.enceinteTypeBox,
				this.entiteBox,
				this.couleurEnceinteBox,
				this.incidentsListEdit
			});
		
		setRequiredMarks(new Component[]{
				this.nomRequired,
				this.typeRequired
		});
		
		this.objRowsComponents = new Component[]{
				this.rowNomAlias,
				this.rowType,
				this.rowNbPlaces
		};
		
		this.objDeplacementComponents = new Component[]{
				this.rowEmplacementTitle1,
				this.rowAdresseActuelle,
				this.rowPositionActuelle,
				this.rowEmplacementTitle2,
				this.rowAdresseDestination,
				this.rowPositionDestination,
				this.rowContenuDestination,
				this.validateDeplacement,
				this.revertDeplacement,
				this.rowNomDepart,
				this.rowNomDestination
		};
		
		this.objCreateComponents = new Component[]{
				this.createEnceintes,
				this.createEnceintesButton
		};
		
		this.objArborescenceComponents = new Component[]{
				this.rowCreateTitle1,
				this.rowCreateTitle2,
				this.rowEnceintesEdit,
				this.rowTerminaleType,
				this.rowNbPlacesTerminale,
				this.rowTerminaleNumerotation,
				this.rowEnceintesEdit,
				this.typesTerminaleBox,
				this.numerotationsBox,
				this.enceintesListEdit,
				this.nbPlacesEachEnceinteBox
		};
		
		
		drawActionsForEnceinte();
		
		getBinder().loadAll();		
	}
	
	@Override
	public Enceinte getObject() {
		return enceinte;
	}

	@Override
	public void setObject(TKdataObject e) {
		this.enceinte = (Enceinte) e;
		selectedCouleur = null;
		
		setStockageObj(enceinte);

		
		if (enceinte.getEnceinteId() != null) {
			
			initIncidents(ManagerLocator.getIncidentManager()
					.findByEnceinteManager(enceinte));
			
			StringBuffer sb = new StringBuffer();
			if (this.enceinte.getEntite() != null) {
				sb.append(Labels.getLabel("enceinte.entite.reservee"));
				sb.append(" ");
				sb.append(this.enceinte.getEntite().getNom());
				sb.append("s.");
			}
			entiteReservee = sb.toString();
			
			Set<Banque> banks = ManagerLocator.getEnceinteManager()
				.getBanquesManager(enceinte);
			Iterator<Banque> it = banks.iterator();
			banques = new ArrayList<Banque>();
			while (it.hasNext()) {
				banques.add(it.next());
			}
			sb = new StringBuffer();
			if (banques.size() > 0) {
				sb.append(Labels.getLabel("enceinte.banques.reservees"));
				for (int i = 0; i < banques.size(); i++) {
					sb.append(" ");
					sb.append(banques.get(i).getNom());
					if (i < banques.size() - 1) {
						sb.append(",");
					}
				}
				sb.append(".");
			}
			banquesReservees = sb.toString();
			
			selectedCouleur = this.enceinte.getCouleur();
			
			boolean orig = 
					ManagerLocator.getEnceinteManager().getConteneurManager(enceinte)
					.getPlateformeOrig().equals(SessionUtils.getCurrentPlateforme());
			setCanEdit(orig);
			setCanDelete(orig);
			numerotation.setDisabled(!orig);
		}
		
		if (!enceinte.equals(new Enceinte())) {
			this.enceintePere = this.enceinte.getEnceintePere();
			this.conteneur = this.enceinte.getConteneur();
			this.position = this.enceinte.getPosition();
		}
		
		// clone er reload
		super.setObject(enceinte);	
	}
	
	@Override
	public void setNewObject() {
		setObject(new Enceinte());
		super.setNewObject();
	}
	
	@Override
	public void cloneObject() {
		setClone(this.enceinte.clone());
	}
	
	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public void setParentObject(TKdataObject obj) {
	}
	
	@Override
	public StockageController getObjectTabController() {
		return (StockageController) super.getObjectTabController();
	}
	
	@Override
	public void switchToCreateMode() {

		initEditableMode();
		
		super.switchToCreateMode();
		
		for (int i = 0; i < objDeplacementComponents.length; i++) {
			objDeplacementComponents[i].setVisible(false);
		}
		
		for (int i = 0; i < objRowsComponents.length; i++) {
			objRowsComponents[i].setVisible(true);
			rowNewEnceinte.setVisible(false);
		}
		
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(true);
		}
		
		groupIncidents.setVisible(false);
		incidentsListEdit.setVisible(false);
		
		rowEntiteEdit.setVisible(true);
		nbPlacesBox.setVisible(true);
		nbPlacesLabel.setVisible(false);
		nbPlacesRequired.setVisible(true);
		deplacer.setVisible(false);
		
		getBinder().loadComponent(self);
	}
	

	public void switchToStaticMode() {
		
		super.switchToStaticMode(
				this.enceinte.getEnceinteId() == null);
		
		if (this.enceinte.getEnceinteId() == null) {
			this.menuBar.setVisible(false);
		}
		
		// on cache le formulaire de déplacement
		for (int i = 0; i < objDeplacementComponents.length; i++) {
			objDeplacementComponents[i].setVisible(false);
		}
		
		groupIncidents.setVisible(true);
		
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(false);
		}
		
		for (int i = 0; i < objArborescenceComponents.length; i++) {
			objArborescenceComponents[i].setVisible(false);
		}
		
		// si c'est une nouvelle enceinte
		if (this.enceinte.getEnceinteId() == null) {
			for (int i = 0; i < objRowsComponents.length; i++) {
				objRowsComponents[i].setVisible(false);
				rowNewEnceinte.setVisible(true);
			}
			deplacer.setVisible(false);
		} else {
			for (int i = 0; i < objRowsComponents.length; i++) {
				objRowsComponents[i].setVisible(true);
			}
			
			// si l'enceinte n'est pas modifiable : elle est
			// réservée pour une autre banque
			if (banques.size() > 0
					&& !banques.contains(SessionUtils
								.getSelectedBanques(sessionScope).get(0))) {
				deplacer.setVisible(false);
				editC.setVisible(false);
				deleteC.setVisible(false);
			} else {
				deplacer.setVisible(true);
			}
			
			rowNewEnceinte.setVisible(false);
			addNewC.setVisible(false);
			
		}
		
		if (this.enceinte.getEntite() != null) {
			entiteReserveeRow.setVisible(true);
		} else {
			entiteReserveeRow.setVisible(true);
		}
		
		if (this.banques.size() > 0) {
			banquesReserveesRow.setVisible(true);
		} else {
			banquesReserveesRow.setVisible(true);
		}
		
		rowEntiteEdit.setVisible(false);
		nbPlacesBox.setVisible(false);
		nbPlacesLabel.setVisible(true);
		nbPlacesRequired.setVisible(false);
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void switchToEditMode() {
		// Initialisation du mode (listes, valeurs...)
		initEditableMode();
		
		super.switchToEditMode();
		
		for (int i = 0; i < objDeplacementComponents.length; i++) {
			objDeplacementComponents[i].setVisible(false);
		}
		
		for (int i = 0; i < objRowsComponents.length; i++) {
			objRowsComponents[i].setVisible(true);
			rowNewEnceinte.setVisible(false);
		}
		
		if (ManagerLocator.getEnceinteManager()
				.getEnceintesManager(enceinte).size() == 0
			&& ManagerLocator.getEnceinteManager()
				.getTerminalesManager(enceinte).size() == 0) {
			for (int i = 0; i < objCreateComponents.length; i++) {
				objCreateComponents[i].setVisible(true);
			}
		} else {
			for (int i = 0; i < objCreateComponents.length; i++) {
				objCreateComponents[i].setVisible(false);
			}
		}
		
		rowEntiteEdit.setVisible(true);
		nbPlacesBox.setVisible(false);
		nbPlacesLabel.setVisible(true);
		nbPlacesRequired.setVisible(false);
		deplacer.setVisible(false);
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void setFocusOnElement() {
		nomBox.setFocus(true);
	}
	
	/**
	 * Change mode de la fiche en mode déplacement.
	 */
	public void switchToDeplacerMode() {
		for (int i = 0; i < objDeplacementComponents.length; i++) {
			objDeplacementComponents[i].setVisible(true);
		}
		
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(false);
		}
		
		for (int i = 0; i < objArborescenceComponents.length; i++) {
			objArborescenceComponents[i].setVisible(false);
		}
		
		groupIncidents.setVisible(false);
		incidentsList.setVisible(false);
		
		deleteC.setVisible(false);
		this.deplacer.setVisible(false);
		editC.setVisible(false);
		rowNomDestination.setVisible(false);
		menuBar.setVisible(false);
		
		// adresse de départ
		adresseActuelle = ManagerLocator.getEnceinteManager()
			.getAdrlManager(enceinte);
		
		emplForRetours.clear();
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void clearData() {
		clearIncidents();
		clearConstraints();
		clearDestination();
		super.clearData();
	}

	@Override
	public void createNewObject() {
		try {
			// on remplit l'enceinte en fonction des champs nulls
			setEmptyToNulls();
			setFieldsToUpperCase();
			
			this.enceinte.setPosition(position);
			
			if (!arborescenceCreee) {
				// creation de l'objet
				ManagerLocator.getEnceinteManager().createObjectManager(
						enceinte, 
						selectedEnceinteType, 
						conteneur, 
						enceintePere, 
						selectedEntite, 
						banques,
						selectedCouleur,
						SessionUtils.getLoggedUser(sessionScope));
			} else {
				this.enceinte.setConteneur(this.conteneur);
				this.enceinte.setEnceintePere(this.enceintePere);
				this.enceinte.setEntite(selectedEntite);
				this.enceinte.setEnceinteType(selectedEnceinteType);
				this.enceinte.setArchive(false);
				
				this.terminale.setTerminaleType(selectedTerminaleType);
				this.terminale.setTerminaleNumerotation(
						selectedTerminaleNumerotation);
				this.terminale.setEntite(selectedEntite);
				
				List<Integer> firstPositions = new ArrayList<Integer>();
				for (int i = 0; i < decoratedEnceintes.size(); i++) {
					Enceinte tmp = decoratedEnceintes.get(i).getEnceinte();
					tmp.setEntite(selectedEntite);
					if (i > 0) {
						enceintes.get(i - 1).setNbPlaces(
								decoratedEnceintes.get(i).getNbPlaces());
					}
					
					if (i + 1 < decoratedEnceintes.size()) {
						enceintes.add(tmp);
					}
					
					firstPositions.add(decoratedEnceintes.get(i)
							.getFirstPosition());
				}
				
				this.terminale.setNom(decoratedEnceintes.get(
						decoratedEnceintes.size() - 1).getEnceinte().getNom());
			
					// création de l'objet
					ManagerLocator.getEnceinteManager()
						.createAllArborescenceManager(
							enceinte, 
							enceintes, 
							terminale, 
							firstPositions, 
							banques,
							SessionUtils.getLoggedUser(sessionScope));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void onClick$addNewC() {
		this.switchToCreateMode();
	}

	@Override
	public void onClick$cancelC() {
		clearData();
	}

	@Override
	public void onClick$createC() {
		Clients.showBusy(Labels.getLabel("enceinte.creation.encours"));
		Events.echoEvent("onLaterCreate", self, null);
	}

	@Override
	public void onClick$editC() {
		if (this.enceinte.getEnceinteId() != null) {
			switchToEditMode();
		}
	}
	
	/**
	 * Passe en mode de déplacement de l'enceinte.
	 */
	public void onClick$deplacer() {
		if (this.enceinte.getEnceinteId() != null) {
			cloneObject();
			switchToDeplacerMode();
			getObjectTabController().getListeStockages()
									.switchToDeplacementMode();
		}
	}
	
	public void onClick$revertDeplacement() {
		revertObject();
		clearConstraints();
		clearDestination();
		getBinder().loadComponent(self);
		switchToStaticMode();
		rowErreurDestination.setVisible(false);
		getObjectTabController().getListeStockages().switchToNormalMode();
	}

	@Override
	public void onClick$revertC() {
		clearConstraints();
		super.onClick$revertC();
	}

	@Override
	public void onClick$validateC() {
		Clients.showBusy(Labels.getLabel("enceinte.creation.encours"));
		Events.echoEvent("onLaterUpdate", self, null);
	}
	
	public void onClick$validateDeplacement() {
		Clients.showBusy(Labels.getLabel("enceinte.deplacement.encours"));
		Events.echoEvent("onLaterDeplacement", self, null);
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * nomBox. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$nomBox() {
		nomBox.setValue(nomBox.getValue().toUpperCase().trim());
		this.enceinte.setNom(nomBox.getValue());
	}
	
	@Override
	public void setFieldsToUpperCase() {
		if (this.enceinte.getNom() != null) {
			this.enceinte.setNom(
				this.enceinte.getNom().toUpperCase().trim());
		}
	}
	
	/**
	 * Clic sur le bouton createEnceintesButton pour créer l'arborescence.
	 */
	public void onClick$createEnceintesButton() {
		
		int start = 0;
		Conteneur cont = null;
		if (this.conteneur != null) {
			cont = this.conteneur;
			start = 1;
		} else if (this.enceintePere != null) {
			cont = ManagerLocator.getEnceinteManager()
				.getConteneurManager(this.enceintePere);
			start = ManagerLocator.getEnceinteManager()
				.getLevelEnceinte(this.enceintePere);
			++start;
		}
		
		if (cont != null && start > 0) {
			nbPlacesLabel.setVisible(true);
			nbPlacesBox.setVisible(false);
			nbPlacesRequired.setVisible(false);
			arborescenceCreee = true;
			for (int i = 0; i < objCreateComponents.length; i++) {
				objCreateComponents[i].setVisible(false);
			}
			
			for (int i = 0; i < objArborescenceComponents.length; i++) {
				objArborescenceComponents[i].setVisible(true);
			}
			
			for (int i = start; i < cont.getNbrNiv(); i++) {
				Enceinte tmp = new Enceinte();
				EnceinteDecorator deco = new EnceinteDecorator(tmp);
				tmp.setEnceinteType(selectedChildEnceinteType);
				deco.setNbNiveau(i + 1);
				if (i == start) {
					deco.setNbPlaces(this.enceinte.getNbPlaces());
					deco.setDisabled(true);
				}
				if (i + 1 < cont.getNbrNiv()) {
					tmp.setNom(selectedChildEnceinteType.getPrefixe());
					deco.setIsTerminale(false);
				} else {
					deco.getEnceinte().setNom("BT");
					deco.setIsTerminale(true);
				}
				decoratedEnceintes.add(deco);
				
			}
			
			ListModel<EnceinteDecorator> list = 
				new ListModelList<EnceinteDecorator>(decoratedEnceintes);
			enceintesListEdit.setModel(list);
			
			terminale = new Terminale();
			terminale.setNom("BT");
		}
	}
	
	/**
	 * Select sur la liste enceintesTypeBox.
	 */
	public void onSelect$enceintesTypeBox(Event event) {
		// on récupère l'enceinte pour la ligne courante
		EnceinteDecorator tmp = (EnceinteDecorator) getBindingData(
				(ForwardEvent) event);
		// on récupère la liste de la ligne courante
		Listbox list =  getListBox((ForwardEvent) event);
		
		// maj
		EnceinteType typeTmp = (EnceinteType) list.getSelectedItem()
			.getValue(); 
		tmp.getEnceinte().setEnceinteType(typeTmp);
		tmp.getEnceinte().setNom(typeTmp.getPrefixe());
	}
	
	/**
	 * Cette méthode va retourner le LaboInter contenu dans la ligne
	 * courante de la grille laboIntersGrid.
	 * @param event Event sur la grille contenant la liste des LaboInter.
	 * @return Un LaboInter.
	 */
	public Object getBindingData(ForwardEvent event) {
		Component target = event.getOrigin().getTarget();
		try {
			while (!(target instanceof Row || target instanceof Listitem)) {
				target = target.getParent(); 
			}
			Map<?,?> map = (Map<?,?>) 
					target.getAttribute("zkplus.databind.TEMPLATEMAP");
			return map.get(target.getAttribute("zkplus.databind.VARNAME"));
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Cette méthode va retourner la liste pour laquelle un event vient de
	 * se produire.
	 * @param event Event sur une liste.
	 * @return Une ListBox (de services, collabs ou transporteurs).
	 */
	public Listbox getListBox(ForwardEvent event) {
		Component target = event.getOrigin().getTarget();
		try {
			while (!(target instanceof Listbox)) {
				target = target.getParent(); 
			}
			return (Listbox) target;
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public void setEmptyToNulls() {
		
		if (this.enceinte.getAlias().equals("")) {
			this.enceinte.setAlias(null);
		}
		
		selectedCouleur = null;
		if (couleurEnceinteBox.getSelectedItem() != null) {
			if (((Couleur) couleurEnceinteBox.getSelectedItem().getValue())
									.getCouleurId() != null) {
				selectedCouleur = (Couleur) 
					couleurEnceinteBox.getSelectedItem().getValue();
			}
		}
		
	}

	@Override
	public void updateObject() {
		try {
			
			// on remplit l'enceinte en fonction des champs nulls
			setEmptyToNulls();
			setFieldsToUpperCase();
			
			if (!arborescenceCreee) {
				// update de l'objet
				ManagerLocator.getEnceinteManager().updateObjectManager(
						enceinte, 
						selectedEnceinteType, 
						this.enceinte.getConteneur(), 
						this.enceinte.getEnceintePere(), 
						selectedEntite, 
						banques,
						selectedCouleur,
						getIncidents(),
						SessionUtils.getLoggedUser(sessionScope),
						null);
				
				super.updateObject();
				
			} else {
				this.enceinte.setConteneur(this.enceinte.getConteneur());
				this.enceinte.setEnceintePere(this.enceinte.getEnceintePere());
				this.enceinte.setEntite(selectedEntite);
				this.enceinte.setEnceinteType(selectedEnceinteType);
				this.enceinte.setArchive(false);
				
				this.terminale.setTerminaleType(selectedTerminaleType);
				this.terminale.setTerminaleNumerotation(
						selectedTerminaleNumerotation);
				this.terminale.setEntite(selectedEntite);
				
				List<Integer> firstPositions = new ArrayList<Integer>();
				for (int i = 0; i < decoratedEnceintes.size(); i++) {
					Enceinte tmp = decoratedEnceintes.get(i).getEnceinte();
					tmp.setEntite(selectedEntite);
					if (i > 0) {
						enceintes.get(i - 1).setNbPlaces(
								decoratedEnceintes.get(i).getNbPlaces());
					}
					
					if (i + 1 < decoratedEnceintes.size()) {
						enceintes.add(tmp);
					}
					
					firstPositions.add(decoratedEnceintes.get(i)
							.getFirstPosition());
				}
				
				this.terminale.setNom(decoratedEnceintes.get(
						decoratedEnceintes.size() - 1).getEnceinte().getNom());
			
					// création de l'objet
					ManagerLocator.getEnceinteManager()
						.updatewithCreateAllArborescenceManager(
								enceinte, 
								enceintes, 
								terminale, 
								firstPositions, 
								banques,
								SessionUtils.getLoggedUser(sessionScope));
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		if (!this.enceinte.getNom().equals(((Enceinte) getClone()).getNom())) {
			getObjectTabController().refreshListesEchantillonsDerives();
		}

	}
	
	protected List<String> deplacerObject() {
		List<String> errorMsg = new ArrayList<String>();
		// on récupère l'emplacement de départ
		Conteneur c = enceinte.getConteneur();
		Enceinte ep = enceinte.getEnceintePere();
		Integer p = enceinte.getPosition();
		
		try {
			
			// si la destination est vide
			if (enceinteDestination.isVide()) {
				enceinte.setPosition(positionDestination);
				enceinteAEchanger = null;
				// update de l'objet
				List<OperationType> ops = ManagerLocator
					.getOperationTypeManager()
					.findByNomLikeManager("Deplacement", true);
				ManagerLocator.getEnceinteManager().updateObjectManager(
						enceinte, 
						enceinte.getEnceinteType(), 
						this.enceinteDestination.getEnceinte()
							.getConteneur(), 
						this.enceinteDestination.getEnceinte()
							.getEnceintePere(), 
						this.enceinte.getEntite(), 
						banques,
						this.enceinte.getCouleur(),
						null,
						SessionUtils.getLoggedUser(sessionScope),
						ops);
			} else {
				
				// on échange des emplacements des 2 enceintes
				enceinteAEchanger = enceinteDestination.getEnceinte().clone();
				enceinte.setConteneur(enceinteAEchanger.getConteneur());
				enceinte.setEnceintePere(enceinteAEchanger.getEnceintePere());
				enceinte.setPosition(enceinteAEchanger.getPosition());
				
				enceinteAEchanger.setConteneur(c);
				enceinteAEchanger.setEnceintePere(ep);
				enceinteAEchanger.setPosition(p);
				
				ManagerLocator.getEnceinteManager()
					.echangerDeuxEnceintesManager(
						enceinte, enceinteAEchanger,
						SessionUtils.getLoggedUser(sessionScope));
			}
		} catch (ValidationException ve) {
			errorMsg.add("- Erreur lors de la validation.");
		} catch (DoublonFoundException dfe) {
			errorMsg.add("- Doublon détecté lors du " 
					+ "déplacement de l'enceinte.");
		} catch (RequiredObjectIsNullException re) {
			errorMsg.add("Objet obligatoire manquant lors du déplacement " 
					+ "de l'enceinte.");
		} catch (InvalidParentException ipe) {
			errorMsg.add("- Erreur sur le " 
					+ "parent de l'enceinte.");
		} catch (InvalidPositionException ipose) {
			errorMsg.add("- Erreur sur la " 
					+ "position de l'enceinte.");
		} catch (UsedPositionException upe) {
			errorMsg.add("- Erreur sur la " 
					+ "position de l'enceinte.");
		}
		
		// s'il y a des erreurs, on fait apparaître une fenêtre contenant
		// la liste de celles-ci
		if (errorMsg.size() > 0) {
			// ferme wait message
			Clients.clearBusy();
			
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < errorMsg.size(); i++) {
				sb.append(errorMsg.get(i));
				if (i < errorMsg.size() - 1) {
					sb.append("\n");
				}
			}
			Messagebox.show(sb.toString(), "Error", 
					Messagebox.OK, Messagebox.ERROR);
			
			// en cas d'erreur, on annule les déplacements
			if (enceinteAEchanger != null) {
				enceinteAEchanger.setConteneur(enceinte.getConteneur());
				enceinteAEchanger.setEnceintePere(enceinte.getEnceintePere());
				enceinteAEchanger.setPosition(enceinte.getPosition());
			}
			enceinte.setConteneur(c);
			enceinte.setEnceintePere(ep);
			enceinte.setPosition(p);
		} else {
			getObjectTabController().refreshListesEchantillonsDerives();
		}
		
		return errorMsg;
	}
	
	@Override
	public boolean prepareDeleteObject() {
		boolean isUsed = ManagerLocator
			.getEnceinteManager().isUsedObjectManager(getObject());
		
		setDeleteMessage(ObjectTypesFormatters
				.getLabel("message.deletion.message", 
						new String[]{Labels
								.getLabel(getDeletionMessage())}));
		setCascadable(false);
		if (isUsed) {
			setDeleteMessage(Labels
					.getLabel("enceinte.deletion.isUsed"));
		} 
		setDeletable(!isUsed);
		setFantomable(!isUsed);
		return false;
	}
	
	@Override
	public void removeObject(String comments) {		
		ManagerLocator.getEnceinteManager().
			removeObjectManager(getObject(), comments, 
					SessionUtils.getLoggedUser(sessionScope));
	}
	
	public void onLaterCreate() {
		
		try { 
			createNewObject();
				
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {
				
				getObjectTabController().getListeStockages()
											.updateEnceinte(enceinte, true);
			}
			
			setObject(this.enceinte);
			switchToStaticMode();
			
		} catch (RuntimeException re) {
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} finally {
			Clients.clearBusy();
		}
		
	}
	
	public void onLaterUpdate() {
		
		try {
			updateObject();
				
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {			
				getObjectTabController().getListeStockages()
										.updateEnceinte(enceinte, false);
			}
			
			setObject(this.enceinte);
			switchToStaticMode();
			
		} catch (RuntimeException re) {
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} finally {
			Clients.clearBusy();
		}	
		
	}
	
	public void onLaterDeplacement() {
		
		// s'il n'y a pas d'erreurs lors de l'update
		if (!getObjStatutIncompatibleForRetour(prepareObjectsAndEmplacements(), null) 
				&& deplacerObject().size() == 0) {
				
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {
				// echange des enceintes
				getObjectTabController().
					getListeStockages().deplacerDeuxEnceintes(
											enceinte, enceinteAEchanger);
			}
			
			setObject(this.enceinte);
			
			clearDestination();			
			
			switchToStaticMode();
			
			Clients.clearBusy();
			
			createRetoursApresDeplacementTerminale();
			
		} else {
			Clients.clearBusy();
		}
		
	}
	
	private void clearDestination() {
		// clean destination
		setEnceinteDestination(null);
		setEnceinteAEchanger(null);
		positionDestination = null;
		contenuDestination = null;
		adresseDestination = null;
		validateDeplacement.setDisabled(true);
	}
	
	/**
	 * Propose à l'utilisateur puis crée les retours associés au
	 * déplacement de la terminale.
	 */
	public void createRetoursApresDeplacementTerminale() {
		// on calcule le nb d'emplacements non vides concernés par
		// ce déplacement
		// Integer nb = 0;
		// Iterator<String> it = emplForRetours.keySet().iterator();
		// while (it.hasNext()) {
		//	String key = it.next();
		//	nb += emplForRetours.get(key).size();
		// }
		if (!getEmplForRetours().isEmpty()) {
			// on demande à l'utilisateur s'il souhaite
			// créer des retours
			if (askForRetoursCreation(getEmplForRetours().size(), 
					null, null, 
					new Hashtable<TKStockableObject, Emplacement>(), 
					null, ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager(
							"creation", true).get(0))) {
//				List<TKStockableObject> objs = 
//					new ArrayList<TKStockableObject>();
//				Hashtable<TKStockableObject, Emplacement> hashEmps = 
//					new Hashtable<TKStockableObject, Emplacement>();
				
				// on va récupérer les objets concernés par ce
				// déplacement et reconstituer leur ancienne
				// adresse
//				it = emplForRetours.keySet().iterator();
//				while (it.hasNext()) {
//					String key = it.next();
//					List<Emplacement> empls = emplForRetours.get(key);
//					
//					for (int i = 0; i < empls.size(); i++) {
//						TKStockableObject obj = null;
//						
//						if (empls.get(i).getEntite()
//								.getNom().equals("Echantillon")) {
//							obj = ManagerLocator.getEchantillonManager()
//									.findByIdManager(empls.get(i)
//											.getObjetId());
//						} else if (empls.get(i).getEntite()
//								.getNom().equals("ProdDerive")) {
//							obj = ManagerLocator.getProdDeriveManager()
//									.findByIdManager(empls.get(i)
//											.getObjetId());
//						}
//						
//						objs.add(obj);
//						hashEmps.put(obj, empls.get(i));
//					}
//				}
				
				// ouverture de la modale
				openRetourFormModale(null, true, null, null, 
					new ArrayList<TKStockableObject>(emplForRetours.keySet()), emplForRetours,
					null, null, null, 
					null, 
					Labels.getLabel("ficheRetour.deplacement"), null);
			}
		}
	}
	
	@Override
	public void onLaterDelete(Event event) {
		try {
			removeObject((String) event.getData());			
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {
				
				getObjectTabController()
					.getListeStockages().deleteEnceinte(
						conteneur, enceintePere, position);
			}
			
			clearData();
			this.enceinte.setEnceintePere(enceintePere);
			this.enceinte.setConteneur(conteneur);
			this.enceinte.setPosition(position);
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
	 * Maj de l'arbre après modification de la taille d'une
	 * enceinte.
	 * @param event
	 */
	public void onGetUpdateTaille(Event event) {
		if (event.getData() != null) {
			setObject((TKdataObject) event.getData());
			
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {			
				getObjectTabController().getListeStockages()
										.updateEnceinte(enceinte, true);
			}
			
			switchToStaticMode();
		}
	}
	
	/**
	 * Méthode pour l'initialisation du mode d'édition : récupération du contenu
	 * des listes déroulantes (types, qualités...).
	 */
	@SuppressWarnings("unchecked")
	public void initEditableMode() {
		
		types = (List<EnceinteType>) 
			ManagerLocator.getEnceinteTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		if (this.enceinte.getEnceinteType() != null) {
			selectedEnceinteType = this.enceinte.getEnceinteType();
		} else {
			selectedEnceinteType = types.get(0);
		}
		
		entites = new ArrayList<Entite>();
		entites.add(null);
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("Echantillon").get(0));
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("ProdDerive").get(0));
		selectedEntite = this.enceinte.getEntite();
		
		// creation de la structure
		arborescenceCreee = false;
		childEnceintesTypes = ManagerLocator.getEnceinteTypeManager()
			.findAllObjectsExceptBoiteManager(SessionUtils
									.getPlateforme(sessionScope));
		selectedChildEnceinteType = childEnceintesTypes.get(0);
		
		terminaleTypes = ManagerLocator.getTerminaleTypeManager()
			.findAllObjectsManager();
		selectedTerminaleType = terminaleTypes.get(0);
		
		numerotations = ManagerLocator.getTerminaleNumerotationManager()
			.findAllObjectsManager();
		selectedTerminaleNumerotation = numerotations.get(0);
		
		enceintes = new ArrayList<Enceinte>();
		decoratedEnceintes = new ArrayList<EnceinteDecorator>();
		terminale = new Terminale();
		
		//init des couleurs
		if (couleurs.isEmpty()) {
			couleurs.add(new Couleur());
			couleurs.addAll(ManagerLocator.getCouleurManager()
											.findAllObjectsManager());
		}
		couleurEnceinteBox.setModel(new SimpleListModel<Couleur>(couleurs));
		couleurEnceinteBox
			.setSelectedIndex(couleurs.indexOf(selectedCouleur));
		
		// clearIncidents();

	}
	
	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(nomBox);
		Clients.clearWrongValue(aliasBox);
		Clients.clearWrongValue(nbPlacesBox);
		Clients.clearWrongValue(nomDepartBox);
		Clients.clearWrongValue(nomDestinationBox);
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur choisit un emplacement de
	 * destination pour l'enceinte.
	 * @param node EnceinteNode représentant la destination de l'enceinte.
	 */
	public void definirEnceinteDestination(EnceinteNode node) {
		// si le node est vide, c'est que la destination n'est pas valide
		if (node != null) {
			validateDeplacement.setDisabled(false);
			
			enceinteDestination = node;
			
			positionDestination = enceinteDestination
				.getEnceinte().getPosition();
			positionDestinationLabel.setValue(positionDestination.toString());
			
			// si la destination n'est pas vide, on récupère son contenu pour
			// l'afficher
			if (!enceinteDestination.isVide()) {
				contenuDestination = enceinteDestination.getEnceinte().getNom();
				adresseDestination = ManagerLocator.getEnceinteManager()
					.getAdrlManager(enceinteDestination.getEnceinte());
				rowNomDestination.setVisible(true);
				getBinder().loadAttribute(
						self.getFellow("nomDestinationBox"), "value");
				
			} else {
				// sinon on affiche un contenu vide
				contenuDestination = Labels.getLabel("stockage.position.libre");
				if (enceinteDestination.getEnceinte().getConteneur() != null) {
					adresseDestination = enceinteDestination
						.getEnceinte().getConteneur().getCode();
				} else {
					StringBuffer sb = new StringBuffer();
					sb.append(ManagerLocator.getEnceinteManager()
							.getAdrlManager(enceinteDestination
							.getEnceinte().getEnceintePere()));
					sb.append(".");
					sb.append(enceinteDestination.getEnceinte()
							.getEnceintePere().getNom());
					adresseDestination = sb.toString();
				}
				
				rowNomDestination.setVisible(false);
			}
			contenuDestinationLabel.setValue(contenuDestination);
			adresseDestinationLabel.setValue(adresseDestination);
			
			rowErreurDestination.setVisible(false);
		} else {
			// on affiche un message d'erreur
			validateDeplacement.setDisabled(true);
			adresseDestination = null;
			adresseDestinationLabel.setValue(adresseDestination);
			positionDestination = null;
			positionDestinationLabel.setValue(null);
			contenuDestination = null;
			contenuDestinationLabel.setValue(contenuDestination);
			rowNomDestination.setVisible(false);
			rowErreurDestination.setVisible(true);
		}
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForEnceinte() {
		drawActionsButtons("Stockage");
		
		boolean canDeplace = false;
		
		if (!sessionScope.containsKey("ToutesCollections")) {
			canDeplace = drawActionOnOneButton("Stockage", "Modification");
		}

		deplacer.setDisabled(!canDeplace);
		taille.setDisabled(!canDeplace);
		
		Boolean admin = false;
		if (sessionScope.containsKey("Admin")) {
			admin = (Boolean) sessionScope.get("Admin");
		}
		
		if (SessionUtils.getSelectedBanques(sessionScope).size() == 1) {
			numerotation.setDisabled(!admin);
		} else {
			numerotation.setDisabled(true);
		}
	}
	
	public void onClick$numerotation() {
		openChangeNumerotationModaleWindow(this.enceinte);
	}
	
	public void onClick$taille() {
		openChangeTailleEnceinteModaleWindow(self, enceinte);
	}
	
	public String getCouleurSrc() {
		StringBuffer sb = new StringBuffer();
		
		if (this.enceinte != null 
				&& this.enceinte.getEnceinteId() != null
				&& this.enceinte.getCouleur() != null) {
			sb.append(colorSrc);
			sb.append(this.enceinte.getCouleur().getCouleur());
			sb.append(".png");
		}
		return sb.toString();
	}
	
	/*************************************************/
	/**               ACCESSEURS                    **/
	/*************************************************/

	
	
	public String getDimensions() {
		StringBuffer sb = new StringBuffer();
		if (this.selectedTerminaleType != null) {
			if (this.selectedTerminaleType.getLongueur() != null) {
				sb.append(this.selectedTerminaleType.getLongueur());
			} else {
				sb.append("-");
			}
			sb.append(" X ");
			if (this.selectedTerminaleType.getHauteur() != null) {
				sb.append(this.selectedTerminaleType.getHauteur());
			} else {
				sb.append("-");
			}
		}
		
		return sb.toString();
	}

	public List<Banque> getBanques() {
		return banques;
	}

	public void setBanques(List<Banque> b) {
		this.banques = b;
	}

	public String getEntiteReservee() {
		return entiteReservee;
	}

	public void setEntiteReservee(String reservee) {
		this.entiteReservee = reservee;
	}

	public String getBanquesReservees() {
		return banquesReservees;
	}

	public void setBanquesReservees(String reservees) {
		this.banquesReservees = reservees;
	}

	public List<EnceinteType> getTypes() {
		return types;
	}

	public void setTypes(List<EnceinteType> t) {
		this.types = t;
	}

	public EnceinteType getSelectedEnceinteType() {
		return selectedEnceinteType;
	}

	public void setSelectedEnceinteType(EnceinteType selected) {
		this.selectedEnceinteType = selected;
	}

	public List<Entite> getEntites() {
		return entites;
	}

	public void setEntites(List<Entite> e) {
		this.entites = e;
	}

	public Entite getSelectedEntite() {
		return selectedEntite;
	}

	public void setSelectedEntite(Entite selected) {
		this.selectedEntite = selected;
	}

	public Enceinte getEnceintePere() {
		return enceintePere;
	}

	public void setEnceintePere(Enceinte ep) {
		this.enceintePere = ep;
	}

	public Conteneur getConteneur() {
		return conteneur;
	}

	public void setConteneur(Conteneur c) {
		this.conteneur = c;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer p) {
		this.position = p;
	}

	public String getAdresseActuelle() {
		return adresseActuelle;
	}

	public void setAdresseActuelle(String adresseAct) {
		this.adresseActuelle = adresseAct;
	}

	public String getAdresseDestination() {
		return adresseDestination;
	}

	public void setAdresseDestination(String adresseDest) {
		this.adresseDestination = adresseDest;
	}

	public Integer getPositionDestination() {
		return positionDestination;
	}

	public void setPositionDestination(Integer positionDest) {
		this.positionDestination = positionDest;
	}

	public String getContenuDestination() {
		return contenuDestination;
	}

	public void setContenuDestination(String contenu) {
		this.contenuDestination = contenu;
	}

	public EnceinteNode getEnceinteDestination() {
		return enceinteDestination;
	}

	public void setEnceinteDestination(EnceinteNode enceinteDest) {
		this.enceinteDestination = enceinteDest;
	}

	public Enceinte getEnceinteAEchanger() {
		return enceinteAEchanger;
	}

	public void setEnceinteAEchanger(Enceinte enceinteAEch) {
		this.enceinteAEchanger = enceinteAEch;
	}

	public Boolean getArborescenceCreee() {
		return arborescenceCreee;
	}

	public void setArborescenceCreee(Boolean arborescence) {
		this.arborescenceCreee = arborescence;
	}

	public List<EnceinteType> getChildEnceintesTypes() {
		return childEnceintesTypes;
	}

	public void setChildEnceintesTypes(List<EnceinteType> childEnceintes) {
		this.childEnceintesTypes = childEnceintes;
	}

	public EnceinteType getSelectedChildEnceinteType() {
		return selectedChildEnceinteType;
	}

	public void setSelectedChildEnceinteType(EnceinteType selectedChild) {
		this.selectedChildEnceinteType = selectedChild;
	}

	public List<EnceinteDecorator> getDecoratedEnceintes() {
		return decoratedEnceintes;
	}

	public void setDecoratedEnceintes(List<EnceinteDecorator> decorated) {
		this.decoratedEnceintes = decorated;
	}

	public List<TerminaleType> getTerminaleTypes() {
		return terminaleTypes;
	}

	public void setTerminaleTypes(List<TerminaleType> terminaleTs) {
		this.terminaleTypes = terminaleTs;
	}

	public TerminaleType getSelectedTerminaleType() {
		return selectedTerminaleType;
	}

	public void setSelectedTerminaleType(TerminaleType selectedTerminaleT) {
		this.selectedTerminaleType = selectedTerminaleT;
	}

	public List<TerminaleNumerotation> getNumerotations() {
		return numerotations;
	}

	public void setNumerotations(List<TerminaleNumerotation> nums) {
		this.numerotations = nums;
	}

	public TerminaleNumerotation getSelectedTerminaleNumerotation() {
		return selectedTerminaleNumerotation;
	}

	public void setSelectedTerminaleNumerotation(
			TerminaleNumerotation selectedTerminaleNum) {
		this.selectedTerminaleNumerotation = selectedTerminaleNum;
	}

	public List<Enceinte> getEnceintes() {
		return enceintes;
	}

	public void setEnceintes(List<Enceinte> e) {
		this.enceintes = e;
	}

	public Terminale getTerminale() {
		return terminale;
	}

	public void setTerminale(Terminale t) {
		this.terminale = t;
	}
	
	public Constraint getEnceinteNomConstraint() {
		return StockageConstraints.getEnceinteNomConstraint();
	}
	
	public Constraint getNomNullconstraint() {
		return StockageConstraints.getNomNullConstraint();
	}
	
	@Override
	public String getDeleteWaitLabel() {
		return Labels.getLabel("enceinte.suppression.encours");
	}

	public CouleurItemRenderer getCouleurRenderer() {
		return couleurRenderer;
	}

	public void setCouleurRenderer(CouleurItemRenderer c) {
		this.couleurRenderer = c;
	}

	public List<Couleur> getCouleurs() {
		return couleurs;
	}

	public void setCouleurs(List<Couleur> c) {
		this.couleurs = c;
	}

	public Couleur getSelectedCouleur() {
		return selectedCouleur;
	}

	public void setSelectedCouleur(Couleur s) {
		this.selectedCouleur = s;
	}

	public HashMap<TKStockableObject, Emplacement> getEmplForRetours() {
		return emplForRetours;
	}

	public void setEmplForRetours(HashMap<TKStockableObject, Emplacement> e) {
		this.emplForRetours = e;
	}
	
	private List<TKStockableObject> prepareObjectsAndEmplacements() {
		// on place dans la HashMap l'ancienne adresse de toutes
		// les terminales de l'enceinte avec tous les emplacements 
		// non vides.
		// ces valeurs seront utilisées pour la création des
		// RETOURs dans le cadre de ce déplacement
		List<Terminale> terms = ManagerLocator.getEnceinteManager()
			.getAllTerminalesInArborescenceManager(enceinte);
		for (int i = 0; i < terms.size(); i++) {	
			emplForRetours.putAll(ManagerLocator.getTerminaleManager()
					.getTkObjectsAndEmplacementsManager(terms.get(i)));
		}
					
		// si la destination n'est pas vide
		if (enceinteDestination != null && !enceinteDestination.isVide()) {
			// on place dans la HashMap l'ancienne adresse de toutes
			// les terminales de l'enceinte avec tous les emplacements 
			// non vides.
			// ces valeurs seront utilisées pour la création des
			// RETOURs dans le cadre de ce déplacement
			terms = ManagerLocator.getEnceinteManager()
				.getAllTerminalesInArborescenceManager(
						enceinteDestination.getEnceinte());
			for (int i = 0; i < terms.size(); i++) {
				emplForRetours.putAll(ManagerLocator.getTerminaleManager()
						.getTkObjectsAndEmplacementsManager(terms.get(i)));
			}
		}
		return new ArrayList<TKStockableObject>(emplForRetours.keySet());
	}

	@Override
	public void onClick$addIncidentItem() {
		openAddIncidentWindow(page, self, null, enceinte, null);		
	}
	
	public Constraint getIncidentConstraint() {
		return StockageConstraints.getIncidentConstraint();
	}
}
