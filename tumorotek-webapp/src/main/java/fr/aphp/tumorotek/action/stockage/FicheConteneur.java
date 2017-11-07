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
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.EnceinteDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheConteneur extends AbstractFicheCombineStockageController {
	
	private static final long serialVersionUID = 7811250115908558565L;
	
	/**
	 *  Static Components pour le mode static.
	 */
	private Label nomLabel;
	private Label codeLabel;
	private Label descriptionLabel;
	private Label typeLabel;
	private Label serviceLabel;
	private Label pieceLabel;
	private Label tempLabel;
	private Label nbrNivLabel;
	// private Group groupIncidents;
	// private Grid incidentsList;
	private Menubar menuBar;
	private Row etablissementRow;
	private Row rowGridRecapitulatif;
	private Row rowGridRecapitulatifTitle;
	private Menuitem numerotation;
	private Menuitem addIncidentItem;
	
	/**
	 *  Editable components : mode d'édition ou de création.
	 */
	private Label codeRequired;
	private Label nomRequired;
	private Label serviceRequired;
	private Label nbrNivRequired;
	private Textbox nomBox;
	private Textbox codeBox;
	private Textbox pieceBox;
	private Textbox descBox;
	private Decimalbox tempBox;
	private Listbox conteneurTypeBox;
	private Combobox serviceBox;
	private Label serviceAideSaisie;
//	private Grid incidentsListEdit;
	// composants pour la création
	private Component[] objCreateComponents;
	private Intbox nbNivBox;
	private Row createEnceintes;
	private Button createEnceintesButton;
	private Component[] objArborescenceComponents;
	private Row rowCreateTitle1;
	private Row rowCreateTitle2;
	private Row rowEnceintesEdit;
	private Grid enceintesListEdit;
	private Row rowTerminaleType;
	private Row rowNbPlacesTerminale;
	private Row rowTerminaleNumerotation;
	private Listbox typesTerminaleBox;
	private Listbox numerotationsBox;
	
	private Group groupPlateformes;
	private Row rowPlateformes;
	private Div plateformesAssociees;
		
	/**
	 * Conteneur à paillettes.
	 */
	private Row rowConteneurPaillettes;
	private Checkbox checkPaillettes;
	private Listbox paillettesSizeBox;
	private Integer sizePaillettes = null;
	private Row rowPaillettes;
	private Row rowNomBoitesPaillettes;
	private Checkbox checkNomBoitesPaillettes;
	
	

	/**
	 *  Objets Principaux.
	 */
	private Conteneur conteneur;
	
	/**
	 *  Associations.
	 */
//	private List<Incident> incidents = new ArrayList<Incident>();
//	private List<Incident> incidentsToRemove = new ArrayList<Incident>();
//	private List<IncidentDecorator> incidentsDecorated = 
//		new ArrayList<IncidentDecorator>();
	private List<Service> services = new ArrayList<Service>();
	private Service selectedService;
	private List<String> nomsService = new ArrayList<String>();
	private List<ConteneurType> types = new ArrayList<ConteneurType>();
	private ConteneurType selectedConteneurType;
	private List<Banque> banques = new ArrayList<Banque>();
	// private List<Plateforme> plateformes = new ArrayList<Plateforme>();
	private List<EnceinteType> enceinteTypes = new ArrayList<EnceinteType>();
	private EnceinteType selectedEnceinteType;
	private List<TerminaleType> terminaleTypes = new ArrayList<TerminaleType>();
	private TerminaleType selectedTerminaleType;
	private List<TerminaleNumerotation> numerotations = 
		new ArrayList<TerminaleNumerotation>();
	private TerminaleNumerotation selectedTerminaleNumerotation;
	private List<Enceinte> enceintes = new ArrayList<Enceinte>();
	private List<Enceinte> enceintesRecap = new ArrayList<Enceinte>();
	private List<EnceinteDecorator> decoratedEnceintes = 
		new ArrayList<EnceinteDecorator>();
	private Terminale terminale;
	private EnceinteRowRenderer enceinteRenderer = new EnceinteRowRenderer();
	
	/**
	 *  Variables formulaire.
	 */
	//private String incidentsGroupHeader = 
	//	Labels.getLabel("conteneur.incidents");
	private Boolean arborescenceCreee = false;

	@Override
	public void doAfterCompose(Component comp) throws Exception { 
		super.doAfterCompose(comp);
		
		setDeletionMessage("message.deletion.conteneur");
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
				this.nomLabel,
				this.codeLabel,
				this.descriptionLabel,
				this.typeLabel,
				this.serviceLabel,
				this.pieceLabel,
				this.tempLabel,
				this.incidentsList,
				this.menuBar,
				this.etablissementRow,
				this.rowGridRecapitulatif,
				this.rowGridRecapitulatifTitle
			});
		
		setObjBoxsComponents(new Component[]{
				this.nomBox,
				this.codeBox,
				this.pieceBox,
				this.tempBox,
				this.descBox,
				this.conteneurTypeBox,
				this.serviceBox,
				this.serviceAideSaisie,
				this.incidentsListEdit
			});
		
		setRequiredMarks(new Component[]{
				this.codeRequired,
				this.nomRequired,
				this.serviceRequired
		});	
		
		this.objCreateComponents = new Component[]{
				this.nbNivBox,
				this.nbrNivRequired,
				this.createEnceintes,
				this.createEnceintesButton,
				this.rowConteneurPaillettes
		};
		
		this.objArborescenceComponents = new Component[]{
				this.rowCreateTitle1,
				this.rowCreateTitle2,
				this.rowEnceintesEdit,
				this.rowTerminaleType,
				this.rowNbPlacesTerminale,
				this.rowTerminaleNumerotation,
				this.typesTerminaleBox,
				this.numerotationsBox,
				this.enceintesListEdit,
				this.rowNomBoitesPaillettes,
				this.rowPaillettes
		};
		
		// passe les refrences des group headers
		Executions.createComponents("/zuls/stockage/PlateformesAssociees.zul", 
												plateformesAssociees, null);
		getPlateformesAssociees().setGroupHeader(groupPlateformes);
		groupPlateformes.setOpen(false);
		
		drawActionsForConteneur();
		
		getBinder().loadAll();
		
	}
	
	@Override
	public void setObject(TKdataObject c) {
		this.conteneur = (Conteneur) c;
		
		setStockageObj(conteneur);
		
		if (conteneur.getConteneurId() != null) {
			
			initIncidents(ManagerLocator.getIncidentManager()
					.findAllObjectsByConteneurManager(conteneur));
			
			Iterator<Banque> it = ManagerLocator.getConteneurManager()
				.getBanquesManager(conteneur).iterator();
			while (it.hasNext()) {
				banques.add(it.next());
			}
			
			getPlateformesAssociees()
				.setObjects(new ArrayList<ConteneurPlateforme>(ManagerLocator
						.getConteneurManager()
						.getConteneurPlateformesManager(conteneur)));
			getPlateformesAssociees().setConteneur(conteneur);
			
			enceinteRenderer = new EnceinteRowRenderer();
			enceintesRecap = ManagerLocator.getEnceinteManager()
				.findByConteneurWithOrderManager(conteneur);
			enceintesRecap.add(null);
		} else {
			enceintesRecap = new ArrayList<Enceinte>();
		}
		
		boolean orig = 
				conteneur.getPlateformeOrig().equals(SessionUtils.getCurrentPlateforme());
		setCanEdit(orig);
		setCanDelete(orig);
		addIncidentItem.setDisabled(!orig);
		numerotation.setDisabled(!orig);
		
		// clone er reload
		super.setObject(conteneur);		
	}
	
	@Override
	public void setNewObject() {
		Conteneur c = new Conteneur();
		c.setPlateformeOrig(SessionUtils.getCurrentPlateforme());
		setObject(c);
		
		getPlateformesAssociees().getObjects().clear();
		
		super.setNewObject();
	}
	
	@Override
	public void cloneObject() {
		setClone(this.conteneur.clone());
	}

	@Override
	public Conteneur getObject() {
		return this.conteneur;
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
		
		super.switchToCreateMode();
		
		// Initialisation du mode d'édition (listes, valeurs formulaires...)
		initEditableMode();
		
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(true);
		}
		
		initCreateMode();
		
		groupIncidents.setVisible(false);
		incidentsListEdit.setVisible(false);
		nbrNivLabel.setVisible(false);
		
		groupPlateformes.setVisible(false);
		rowPlateformes.setVisible(false);
		
		if (!getDroitOnAction("Collaborateur", "Consultation")) {
			serviceAideSaisie.setVisible(false);
		}
		
		getBinder().loadAll();
	}
	
	@Override
	public void switchToStaticMode() {
		
		super.switchToStaticMode(
				this.conteneur.equals(new Conteneur()));
		
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(false);
		}
		
		for (int i = 0; i < objArborescenceComponents.length; i++) {
			objArborescenceComponents[i].setVisible(false);
		}
		
		groupIncidents.setVisible(true);
		groupIncidents.setOpen(false);
		groupPlateformes.setVisible(SessionUtils
			.getCurrentPlateforme().equals(this.conteneur.getPlateformeOrig()));
		rowPlateformes.setVisible(SessionUtils
				.getCurrentPlateforme().equals(this.conteneur.getPlateformeOrig()));
		if (groupPlateformes.isVisible()) {
			getPlateformesAssociees().switchToStaticMode();
		}
	}
	
	/**
	 * Change mode de la fiche en mode edition.
	 */
	public void switchToEditMode() {
		
		super.switchToEditMode();
		
		// Initialisation du mode (listes, valeurs...)
		initEditableMode();
		
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(false);
		}
		
		for (int i = 0; i < objArborescenceComponents.length; i++) {
			objArborescenceComponents[i].setVisible(false);
		}
		
		groupIncidents.setOpen(true);
		
		groupPlateformes.setVisible(SessionUtils
				.getCurrentPlateforme().equals(this.conteneur.getPlateformeOrig()));
		rowPlateformes.setVisible(SessionUtils
				.getCurrentPlateforme().equals(this.conteneur.getPlateformeOrig()));
		if (SessionUtils
				.getCurrentPlateforme()
				.equals(this.conteneur.getPlateformeOrig())) { 
			getPlateformesAssociees().switchToEditMode(true);
		}
		
		if (!getDroitOnAction("Collaborateur", "Consultation")) {
			serviceAideSaisie.setVisible(false);
		}
	}
	
	@Override
	public void setFocusOnElement() {
		nomBox.setFocus(true);
	}

	@Override
	public void clearData() {
		
		clearIncidents();
		
		// clearPfs;
	
		clearConstraints();
		
		super.clearData();
	}

	@Override
	public void createNewObject() {
		
		// si l'arborescence n'a pas été créée, on enregistre simplement le
		// conteneur
		if (arborescenceCreee) {
		
			// on remplit le conteneur en fonction des champs nulls
			setEmptyToNulls();
			setFieldsToUpperCase();
						
			this.conteneur.setService(selectedService);
			this.conteneur.setConteneurType(selectedConteneurType);
			this.conteneur.setArchive(false);
			
			this.terminale.setTerminaleType(selectedTerminaleType);
			
			// si vconteneur à paillettes, la numérotation est par position
			if (sizePaillettes != null) {
				for (int i = 0; i < numerotations.size(); i++) {
					if (numerotations.get(i).getLigne().equals("POS")) {
						selectedTerminaleNumerotation = numerotations.get(i);
					}
				}
			}
			this.terminale.setTerminaleNumerotation(
					selectedTerminaleNumerotation);
			
			List<Integer> firstPositions = new ArrayList<Integer>();
			enceintes = new ArrayList<Enceinte>();
			for (int i = 0; i < decoratedEnceintes.size(); i++) {
				Enceinte tmp = decoratedEnceintes.get(i).getEnceinte();
				if (i == 0) {
					this.conteneur.setNbrEnc(
							decoratedEnceintes.get(i).getNbPlaces());
				} else {
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
			//try {
				ManagerLocator.getConteneurManager()
						.createAllArborescenceManager(
							conteneur, 
							enceintes, 
							terminale,
							firstPositions,
							banques, 
							getPlateformesAssociees().getPlateformes(),
							sizePaillettes,
							checkNomBoitesPaillettes.isChecked(),
							SessionUtils.getLoggedUser(sessionScope), 
							SessionUtils.getCurrentPlateforme());
			/*} catch (RuntimeException re) {
				Clients.clearBusy();
				try {
					Messagebox.show(handleExceptionMessage(re), 
							"Error", Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					log.error(e);
				}
			} catch (Exception e) {
				log.error(e);
			}*/
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
		
		if (!arborescenceCreee) {
			throw new WrongValueException(
					createC, Labels.getLabel("conteneur.error.arborescence"));
		} else {		
			Clients.showBusy(Labels.getLabel("conteneur.creation.encours"));
			Events.echoEvent("onLaterCreate", self, null);
		}
	}

	@Override
	public void onClick$editC() {
		if (this.conteneur.getConteneurId() != null) {
			switchToEditMode();
		}
	}

	@Override
	public void onClick$revertC() {
		clearConstraints();
		super.onClick$revertC();
	}

	@Override
	public void onClick$validateC() {
		Clients.showBusy(Labels.getLabel("conteneur.creation.encours"));
		Events.echoEvent("onLaterUpdate", self, null);	
	}
	
	/**
	 * Clic sur le bouton createEnceintesButton pour créer l'arborescence.
	 */
	public void onClick$createEnceintesButton() {
		
		if (this.conteneur.getNbrNiv() < 2) {
			throw new WrongValueException(
					nbNivBox, Labels.getLabel("conteneur.nbrNiv.illegal"));
		} else {
			arborescenceCreee = true;
			if (checkPaillettes.isChecked()) {
				sizePaillettes = new Integer((String) paillettesSizeBox
								.getSelectedItem().getValue());
			}
			
			for (int i = 0; i < objCreateComponents.length; i++) {
				objCreateComponents[i].setVisible(false);
			}
			
			for (int i = 0; i < objArborescenceComponents.length; i++) {
				objArborescenceComponents[i].setVisible(true);
			}
			
			// on affiche la définition des boites en fct du type
			// de conteneur à créer (à pailletes ou non)
			if (sizePaillettes != null) {
				rowTerminaleType.setVisible(false);
				rowNbPlacesTerminale.setVisible(false);
				rowTerminaleNumerotation.setVisible(false);
			} else {
				rowPaillettes.setVisible(false);
				rowNomBoitesPaillettes.setVisible(false);
			}
			
			nbrNivLabel.setVisible(true);
			
			for (int i = 0; i < this.conteneur.getNbrNiv(); i++) {
				Enceinte tmp = new Enceinte();
				EnceinteDecorator deco = new EnceinteDecorator(tmp);
				tmp.setEnceinteType(selectedEnceinteType);
				deco.setNbNiveau(i + 1);
				
				if (i + 1 < this.conteneur.getNbrNiv()) {
					tmp.setNom(selectedEnceinteType.getPrefixe());
					deco.setIsTerminale(false);
				}
				
				if (i + 2 >= this.conteneur.getNbrNiv()
						&& sizePaillettes != null) {
					tmp.setEnceinteType(ManagerLocator.getEnceinteTypeManager()
							.findByTypeManager("GOBELET MARGUERITE", 
									SessionUtils.getPlateforme(sessionScope))
									.get(0));
					deco.getEnceinte().setNom("MAR");
					deco.setSizeVisoGobeletMarguerite(sizePaillettes);
				}
				
				if (i + 1 >= this.conteneur.getNbrNiv()) {
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

	@Override
	public void setEmptyToNulls() {
		if (this.conteneur.getNom().equals("")) {
			this.conteneur.setNom(null);
		}
		
		if (this.conteneur.getDescription().equals("")) {
			this.conteneur.setDescription(null);
		}
		
		if (this.conteneur.getPiece().equals("")) {
			this.conteneur.setPiece(null);
		}
		
		super.setEmptyToNulls();
	}
	
	/**
	 * Vérification de la validité du service.
	 */
	public void onBlur$serviceBox() {
		// Gestion du service
		String selectedNomService = this.serviceBox.getValue()
			.toUpperCase();
		this.serviceBox.setValue(selectedNomService);
		int ind = nomsService.indexOf(selectedNomService);
		if (ind > -1) {
			selectedService = services.get(ind);				
		} else {
			selectedService = null;
			throw new WrongValueException(
					serviceBox, Labels.getLabel("conteneur.service.invalid"));
		}
	}

	@Override
	public void updateObject() {
		
		// on remplit l'échantillon en fonction des champs nulls
		setEmptyToNulls();
		setFieldsToUpperCase();
		
		this.conteneur.setArchive(false);
		
		// update de l'objet
		ManagerLocator.getConteneurManager().updateObjectManager(
				conteneur, 
				selectedConteneurType, 
				selectedService, banques, 
				getPlateformesAssociees().getPlateformes(),
				getIncidents(),
				SessionUtils.getLoggedUser(sessionScope));
		
		super.updateObject();
		
		if (!this.conteneur.getCode()
				.equals(((Conteneur) getClone()).getCode())) {
			getObjectTabController().refreshListesEchantillonsDerives();
		}
	}
	
	@Override
	public boolean prepareDeleteObject() {
		boolean isUsed = ManagerLocator
			.getConteneurManager().isUsedObjectManager(getObject());
		
		setDeleteMessage(ObjectTypesFormatters
				.getLabel("message.deletion.message", 
						new String[]{Labels
								.getLabel(getDeletionMessage())}));
		setCascadable(false);
		if (isUsed) {
			setDeleteMessage(Labels
					.getLabel("conteneur.deletion.isUsed"));
		} 
		setDeletable(!isUsed);
		setFantomable(!isUsed);
		return false;
	}
	
	@Override
	public void removeObject(String comments) {		
		ManagerLocator.getConteneurManager().
			removeObjectManager(getObject(), comments, 
					SessionUtils.getLoggedUser(sessionScope));
	}

	/**
	 * Ouvre la modale Contexte pour proposer le choix de l'ajout
	 * d'un service.
	 */
	public void onClick$serviceAideSaisie() {
		// on récupère le collaborateur actuellement sélectionné
		// pour l'afficher dans la modale
		List<Object> old = new ArrayList<Object>();
		if (selectedService != null) {
			old.add(selectedService);
		}
		// ouvre la modale
		openCollaborationsWindow(page, 
				"general.recherche",
				"select", null,
				"Service", null, 
				Path.getPath(self),
				old);
	}
	
	/**
	 * Méthode appelée par la fenêtre CollaborationsController quand
	 * l'utilisateur sélectionne un service.
	 * @param e Event contenant le service sélectionné.
	 */
	public void onGetObjectFromSelection(Event e) {
		
		// init des services
		nomsService = new ArrayList<String>();
		services = ManagerLocator.getServiceManager()
			.findAllActiveObjectsWithOrderManager();
		for (int i = 0; i < services.size(); i++) {
			nomsService.add(services.get(i).getNom());
		}
		if (this.conteneur.getService() != null 
			&& !services.contains(this.conteneur.getService())) {
			services.add(this.conteneur.getService());
			nomsService.add(this.conteneur.getService().getNom());
		}
		serviceBox.setModel(new CustomSimpleListModel(nomsService));			
		
		// si un service a été sélectionné
		if (e.getData() != null) {
			Service serv = (Service) e.getData();
			if (nomsService.contains(serv.getNom())) {
				int ind = nomsService.indexOf(serv.getNom());
				selectedService = services.get(ind);
				serviceBox.setValue(selectedService.getNom());
			}
		}
	}
	
//	/**
//	 * Méthode appelée lors du clic sur le bouton addIncident. Elle
//	 * va créer un nouvel incident et l'ajouter à la liste.
//	 */
//	public void onClick$addIncident() {
//				
//		Incident ind = new Incident();
//		ind.setConteneur(conteneur);
//		
//		incidents.add(ind);
//		
//		// maj de la liste des labos
//		ListModel<Incident> list = new ListModelList<Incident>(incidents);
//		incidentsListEdit.setModel(list);
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append(Labels.getLabel("conteneur.incidents"));
//		sb.append(" (");
//		sb.append(incidents.size());
//		sb.append(")");
//		incidentsGroupHeader = sb.toString();
//		this.groupIncidents.setLabel(incidentsGroupHeader);
//		
//		String id = groupIncidents.getUuid();
//		String idTop = panelChildrenWithScroll.getUuid();
//		Clients.evalJavaScript("document.getElementById('" + idTop + "')" 
//				+ ".scrollTop = document.getElementById('" + id + "')" 
//				+ ".offsetTop;");
//	}
	
	@Override
	public void onClick$addIncidentItem() {
		openAddIncidentWindow(page, self, conteneur, null, null);
	}
	
//	/**
//	 * Méthode appelée après ajout d'un nouvel incident.
//	 * @param event
//	 */
//	public void onGetAddedIncident(Event event) {
//		if (event.getData() != null) {
//			incidents.add((Incident) event.getData());
//			incidentsDecorated = IncidentDecorator.decorateListe(incidents);
//			
//			// maj de la liste des labos
//			ListModel<IncidentDecorator> list = 
//					new ListModelList<IncidentDecorator>(incidentsDecorated);
//			incidentsList.setModel(list);
//			
//			ListModel<Incident> list2 = new ListModelList<Incident>(incidents);
//			incidentsListEdit.setModel(list2);
//			getBinder().loadComponent(incidentsListEdit);
//			
//			StringBuffer sb = new StringBuffer();
//			sb.append(Labels.getLabel("conteneur.incidents"));
//			sb.append(" (");
//			sb.append(incidents.size());
//			sb.append(")");
//			incidentsGroupHeader = sb.toString();
//			this.groupIncidents.setLabel(incidentsGroupHeader);
//			this.groupIncidents.setOpen(true);
//			
//			String id = groupIncidents.getUuid();
//			String idTop = panelChildrenWithScroll.getUuid();
//			Clients.evalJavaScript("document.getElementById('" + idTop + "')" 
//					+ ".scrollTop = document.getElementById('" + id + "')" 
//					+ ".offsetTop;");
//		}
//	}
//	
//	/**
//	 * Cette méthode va supprimer un LaboInter de la liste.
//	 * @param event Clic sur une image deleteIncident.
//	 */
//	public void onClick$deleteIncident(Event event) {
//		// on demande confirmation à l'utilisateur
//		// de supprimer l'incident
//		if (Messagebox.show(ObjectTypesFormatters.getLabel(
//				"message.deletion.message", 
//				new String[]{Labels
//					.getLabel("message.deletion.incident")}),
//		Labels.getLabel("message.deletion.title"), 
//		Messagebox.YES | Messagebox.NO, 
//		Messagebox.QUESTION) == Messagebox.YES) {
//			// on récupère l'incident que l'utilisateur veut
//			// suppimer
//			Incident ind = (Incident) 
//				getBindingData((ForwardEvent) event);
//			// on enlève le bao de la liste et on la met à jour
//			incidents.remove(ind);
//			
//			ListModel<Incident> list = new ListModelList<Incident>(incidents);
//			incidentsListEdit.setModel(list);
//			
//			// si l'incident existait dans la BDD on l'ajoute à la
//			// liste des incidents à supprimer (il ne sera délété que
//			// lors de la sauvegarde finale)
//			if (ind.getIncidentId() != null) {
//				incidentsToRemove.add(ind);
//			}
//		}
//	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * codeBox. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$codeBox() {
		codeBox.setValue(codeBox.getValue().toUpperCase().trim());
	}
	
	@Override
	public void setFieldsToUpperCase() {
		if (this.conteneur.getCode() != null) {
			this.conteneur.setCode(
				this.conteneur.getCode().toUpperCase().trim());
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
	
	public CalendarBox getCalendarbox(ForwardEvent event) {
		Component target = event.getOrigin().getTarget();
		try {
			while (!(target instanceof CalendarBox)) {
				target = target.getParent(); 
			}
			return (CalendarBox) target;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public void onLaterCreate() {
		
		try {
			createNewObject();
				
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {
				getObjectTabController().getListeStockages()
													.updateAllConteneurs(true);
			}
			
			setObject(this.conteneur);
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
		
		// s'il n'y a pas d'erreurs lors de l'update
		try {
			updateObject();
				
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {
				getObjectTabController().getListeStockages()
												.updateConteneur(conteneur);
			}
			
			//updateConteneurInFicheBanque();
			
			setObject(this.conteneur);
			switchToStaticMode();
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} finally {
			Clients.clearBusy();
		}	
	}
	
//	/**
//	 * Met à jour la liste des conteneurs au niveau d'une fiche banque
//	 * si elle contient le conteneur selectionné.
//	 */
//	private void updateConteneurInFicheBanque() {
//		if (getMainWindow()
//				.isFullfilledComponent("administrationPanel",
//											"winAdministration")) {
//			
//			AdministrationController controller = (AdministrationController) 
//										getMainWindow()
//										.getMainTabbox()
//										.getTabpanels()
//										.getFellow("administrationPanel")
//										.getFellow("winAdministration")
//						.getAttributeOrFellow("winAdministration$composer", true);
//			
//			// oblige la mise à jour des conteneurs
//			if (!((FicheBanque) controller.getBanqueController()
//					.getFicheCombine()).getConteneurs().isEmpty()) {
//				((FicheBanque) controller
//						.getBanqueController().getFicheCombine())
//													.updateConteneurs();
//			}
//		}
//	}
	
	@Override
	public void onLaterDelete(Event event) {
		
		try {
			removeObject((String) event.getData());
				
			// on vérifie que l'on retrouve bien la page contenant la liste
			// des stockages
			if (getObjectTabController().getListeStockages() != null) {
				getObjectTabController().getListeStockages()
												.updateAllConteneurs(false);
			}		
			clearData();
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
	 * Méthode pour l'initialisation du mode d'édition : récupération du contenu
	 * des listes déroulantes (types, qualités...).
	 */
	@SuppressWarnings("unchecked")
	public void initEditableMode() {
		
		types = (List<ConteneurType>) ManagerLocator.getConteneurTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		types.add(0, null);
		selectedConteneurType = this.conteneur.getConteneurType();
		
		// init des services
		nomsService = new ArrayList<String>();
		services = ManagerLocator.getServiceManager()
			.findAllActiveObjectsWithOrderManager();
		for (int i = 0; i < services.size(); i++) {
			nomsService.add(services.get(i).getNom());
		}
		serviceBox.setModel(new CustomSimpleListModel(nomsService));
		
		if (this.conteneur.getService() != null
			&& services.contains(this.conteneur.getService())) {
			selectedService = this.conteneur.getService();
			serviceBox.setValue(selectedService.getNom());
		} else if (this.conteneur.getService() != null) {
			services.add(this.conteneur.getService());
			nomsService.add(this.conteneur.getService().getNom());
			selectedService = this.conteneur.getService();
			serviceBox.setModel(new SimpleListModel<String>(nomsService));
			serviceBox.setValue(this.conteneur.getService().getNom());
		} else {
			serviceBox.setValue("");
		}
		
		sizePaillettes = null;
		checkPaillettes.setChecked(false);
		paillettesSizeBox.setVisible(true);
		paillettesSizeBox.setSelectedIndex(0);
	}
	
	/**
	 * Méthode pour l'initialisation du mode de création : récupération 
	 * du contenu des listes déroulantes (types, qualités...).
	 */
	@SuppressWarnings("unchecked")
	public void initCreateMode() {
		
		selectedEnceinteType = null;
		
		((Selectable<ConteneurType>) conteneurTypeBox.getModel()).clearSelection();
		
		enceinteTypes = ManagerLocator.getEnceinteTypeManager()
			.findAllObjectsExceptBoiteManager(SessionUtils
										.getPlateforme(sessionScope));
		// on enlève les gobelets marguerites de la liste
		List<EnceinteType> tmp = ManagerLocator.getEnceinteTypeManager()
			.findByTypeManager("GOBELET MARGUERITE", SessionUtils
					.getPlateforme(sessionScope));
		if (tmp.size() > 0) {
			enceinteTypes.remove(tmp.get(0));
		}
		if (!enceinteTypes.isEmpty()) {
			selectedEnceinteType = enceinteTypes.get(0);
		}		
		
		// since 2.0.13 
		// WARNING si thésaurus enceinte_type non renseigné
		if (selectedEnceinteType == null) {
			Messagebox.show(Labels.getLabel("conteneur.no.enceinte.type.warning"), 
					Labels.getLabel("general.warning"), Messagebox.OK, Messagebox.EXCLAMATION);
			switchToStaticMode();
			
		}
		
		// si les enceintes et les boites pour paillettes existent
		// on permet à l'utilisateur de créer un tel conteneur
		boolean okPaillettes = false;
		if (tmp.size() > 0) {
			if (ManagerLocator.getTerminaleTypeManager()
					.findByTypeManager("VISOTUBE_16_TRI").size() > 0) {
				if (ManagerLocator.getTerminaleTypeManager()
						.findByTypeManager("VISOTUBE_16_ROND").size() > 0) {
					okPaillettes = true;
				}
			}
		}
		rowConteneurPaillettes.setVisible(okPaillettes);
		
		terminaleTypes = ManagerLocator.getTerminaleTypeManager()
			.findAllObjectsManager();
		selectedTerminaleType = terminaleTypes.get(0);
		
		numerotations = ManagerLocator.getTerminaleNumerotationManager()
			.findAllObjectsManager();
		selectedTerminaleNumerotation = numerotations.get(0);
		
		enceintes = new ArrayList<Enceinte>();
		decoratedEnceintes = new ArrayList<EnceinteDecorator>();
		terminale = new Terminale();
		
		banques = new ArrayList<Banque>();
		banques.add(SessionUtils.getSelectedBanques(sessionScope).get(0));
		
		arborescenceCreee = false;
		
	//	getIncidents().clear();
	//	getIncidentsToRemove.clear();
	//	getIncidentsDecorated.clear();
		clearIncidents();
	}
	
	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(nomBox);
		Clients.clearWrongValue(codeBox);
		Clients.clearWrongValue(descBox);
		Clients.clearWrongValue(conteneurTypeBox);
		Clients.clearWrongValue(serviceBox);
		Clients.clearWrongValue(pieceBox);
		Clients.clearWrongValue(tempBox);
		Clients.clearWrongValue(nbNivBox);
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForConteneur() {
		drawActionsButtons("Stockage");
		
		if (SessionUtils.getSelectedBanques(sessionScope).size() == 1) {
			setCanNew(isCanNew());
		} else {
			setCanNew(false);
		}
		
		if (isCanEdit()) {
			addIncidentItem.setDisabled(false);
		} else {
			addIncidentItem.setDisabled(true);
		}
		
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
		openChangeNumerotationModaleWindow(this.conteneur);
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

//	public List<Incident> getIncidents() {
//		return incidents;
//	}
//
//	public void setIncidents(List<Incident> i) {
//		this.incidents = i;
//	}
//
//	public List<Incident> getIncidentsToRemove() {
//		return incidentsToRemove;
//	}
//
//	public void setIncidentsToRemove(List<Incident> iToRemove) {
//		this.incidentsToRemove = iToRemove;
//	}

//	public String getIncidentsGroupHeader() {
//		return incidentsGroupHeader;
//	}
//
//	public void setIncidentsGroupHeader(String groupHeader) {
//		this.incidentsGroupHeader = groupHeader;
//	}
//
//	public List<IncidentDecorator> getIncidentsDecorated() {
//		return incidentsDecorated;
//	}
//
//	public void setIncidentsDecorated(List<IncidentDecorator> isDecorated) {
//		this.incidentsDecorated = isDecorated;
//	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> s) {
		this.services = s;
	}

	public Service getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(Service selected) {
		this.selectedService = selected;
	}

	public List<String> getNomsService() {
		return nomsService;
	}

	public void setNomsService(List<String> nomsServ) {
		this.nomsService = nomsServ;
	}

	public List<ConteneurType> getTypes() {
		return types;
	}

	public void setTypes(List<ConteneurType> t) {
		this.types = t;
	}

	public ConteneurType getSelectedConteneurType() {
		return selectedConteneurType;
	}

	public void setSelectedConteneurType(ConteneurType selected) {
		this.selectedConteneurType = selected;
	}

	public List<Banque> getBanques() {
		return banques;
	}

	public void setBanques(List<Banque> b) {
		this.banques = b;
	}

	public Component[] getObjCreateComponents() {
		return objCreateComponents;
	}

	public void setObjCreateComponents(Component[] components) {
		this.objCreateComponents = components;
	}

	public List<EnceinteType> getEnceinteTypes() {
		return enceinteTypes;
	}

	public void setEnceinteTypes(List<EnceinteType> enceinteTs) {
		this.enceinteTypes = enceinteTs;
	}

	public EnceinteType getSelectedEnceinteType() {
		return selectedEnceinteType;
	}

	public void setSelectedEnceinteType(EnceinteType selected) {
		this.selectedEnceinteType = selected;
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

	public void setSelectedTerminaleType(TerminaleType selected) {
		this.selectedTerminaleType = selected;
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
			TerminaleNumerotation selected) {
		this.selectedTerminaleNumerotation = selected;
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

	public Boolean getArborescenceCreee() {
		return arborescenceCreee;
	}

	public void setArborescenceCreee(Boolean arborescence) {
		this.arborescenceCreee = arborescence;
	}

	public List<EnceinteDecorator> getDecoratedEnceintes() {
		return decoratedEnceintes;
	}

	public void setDecoratedEnceintes(List<EnceinteDecorator> decorated) {
		this.decoratedEnceintes = decorated;
	}
	
	public Constraint getNomConstraint() {
		return StockageConstraints.getNomConstraint();
	}
	
	public Constraint getNomNullConstraint() {
		return StockageConstraints.getNomNullConstraint();
	}
	
	public Constraint getDescConstraint() {
		return StockageConstraints.getDescConstraint();
	}
	
	public Constraint getCodeConstraint() {
		return StockageConstraints.getCodeConstraint();
	}
	
	public Constraint getPieceConstraint() {
		return StockageConstraints.getPieceConstraint();
	}
	
	public Constraint getIncidentConstraint() {
		return StockageConstraints.getIncidentConstraint();
	}
	
	public Constraint getEnceinteNomConstraint() {
		return StockageConstraints.getEnceinteNomConstraint();
	}

	@Override
	public String getDeleteWaitLabel() {
		return Labels.getLabel("conteneur.suppression.encours");
	}

	public List<Enceinte> getEnceintesRecap() {
		return enceintesRecap;
	}

	public void setEnceintesRecap(List<Enceinte> eRecap) {
		this.enceintesRecap = eRecap;
	}

	public EnceinteRowRenderer getEnceinteRenderer() {
		return enceinteRenderer;
	}

	public void setEnceinteRenderer(EnceinteRowRenderer eRenderer) {
		this.enceinteRenderer = eRenderer;
	}

	public Integer getSizePaillettes() {
		return sizePaillettes;
	}

	public void setSizePaillettes(Integer p) {
		this.sizePaillettes = p;
	}

	/**
	 * Renvoie le controller associe au composant permettant la getsion 
	 * des associations one-to-many avec les conteneurs.
	 */
	public PlateformesAssociees getPlateformesAssociees() {
		return (PlateformesAssociees) self.getFellow("plateformesAssociees")
					.getFellow("winPlateformesAssociees")
					.getAttributeOrFellow("winPlateformesAssociees$composer", true);
	}
}
