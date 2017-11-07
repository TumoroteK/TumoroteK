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
package fr.aphp.tumorotek.action.impression;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractImpressionController;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.BlocImpressionDecorator;
import fr.aphp.tumorotek.decorator.BlocImpressionRowRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheTemplate extends AbstractImpressionController {
	
	private Log log = LogFactory.getLog(FicheTemplate.class);

	private static final long serialVersionUID = -8743924081789346031L;
	
	private Group groupContenu;
	/**
	 *  Static Components pour le mode static.
	 */
	private Label nomLabel;
	private Label entiteLabel;
	private Label descriptionLabel;
	private Label enteteLabel;
	private Label piedPageLabel;
	private Grid contenuStaticGrid;
	
	/**
	 *  Editable components : mode d'édition ou de création.
	 */
	private Textbox nomBox;
	private Textbox descriptionBox;
	private Textbox enteteBox;
	private Textbox piedPageBox;
	private Label nomRequired;
	private Label entiteRequired;
	private Row defineBlocsRow;
	private Listbox entitesBox;
	
	/**
	 *  Objets Principaux.
	 */
	private Template template;
	
	/**
	 *  Associations.
	 */
	private List<Entite> entites = new ArrayList<Entite>();
	private Entite selectedEntite;
	
	/**
	 * Variables formulaire.
	 */
	
	private BlocImpressionRowRenderer blocImpressionRenderer 
		= new BlocImpressionRowRenderer(false);
	private BlocImpressionRowRenderer blocImpressionRendererEdit
		= new BlocImpressionRowRenderer(true);
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
				nomLabel,
				entiteLabel,
				descriptionLabel,
				enteteLabel,
				piedPageLabel,
				contenuStaticGrid,
				groupContenu
		});
		
		setObjBoxsComponents(new Component[]{
				nomBox,
				descriptionBox,
				enteteBox,
				piedPageBox,
				defineBlocsRow,
				entitesBox,
				contenuEditGrid
		});
		
		setRequiredMarks(new Component[]{
				nomRequired,
				entiteRequired
		});	
		
		initEditableMode();

		drawActionsForTemplate();
		
		if (winPanel != null) {
			winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
		}
		
		getBinder().loadAll();	
	}
	
	/**
	 * Méthode pour l'initialisation du mode d'édition : récupération du contenu
	 * des listes déroulantes (types, qualités...).
	 */
	public void initEditableMode() {
		// init des entités
		entites = new ArrayList<Entite>();
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("Patient").get(0));
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("Prelevement").get(0));
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("Echantillon").get(0));
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("ProdDerive").get(0));
		entites.add(ManagerLocator.getEntiteManager()
				.findByNomManager("Cession").get(0));
		selectedEntite = entites.get(0);
	}
	
	@Override
	public TKdataObject getObject() {
		return this.template;
	}

	@Override
	public void setObject(TKdataObject obj) {
		this.template = (Template) obj;
		blocImpressionRenderer.setTemplate(template);
		
		blocImpressionsDecorated = new ArrayList<BlocImpressionDecorator>();
		if (this.template.getTemplateId() != null) {
			generateListeBlocs();
		}
		
		switchToStaticMode();
		
		super.setObject(obj);
	}
	
	/**
	 * Méthode générant la liste des BlocImpressionDecorators en
	 * fonction de l'ordre des objets en base.
	 */
	public void generateListeBlocs() {
		List<BlocImpressionTemplate> temps = ManagerLocator
			.getBlocImpressionTemplateManager()
			.findByTemplateManager(template);
		
		List<TableAnnotationTemplate> tables = ManagerLocator
			.getTableAnnotationTemplateManager()
			.findByTemplateManager(template);
		
		int i = 0;
		int j = 0;
		// on parcourt les 2 listes en entier
		while (i < temps.size() || j < tables.size()) {
			BlocImpressionTemplate bloc = null;
			TableAnnotationTemplate anno = null;
			if (i < temps.size()) {
				bloc = temps.get(i);
			}
			if (j < tables.size()) {
				anno = tables.get(j);
			}
			
			// si on arrive a extraire un bloc et une annotation
			if (bloc != null && anno != null) {
				// on ajoute à la liste finale celui qui a le +
				// petit ordre
				if (anno.getOrdre() < bloc.getOrdre()) {
					BlocImpressionDecorator deco = 
						new BlocImpressionDecorator(
								null, anno.getTableAnnotation(), template);
					blocImpressionsDecorated.add(deco);
					++j;
				} else {
					BlocImpressionDecorator deco = 
						new BlocImpressionDecorator(
								bloc.getBlocImpression(), null, template);
					blocImpressionsDecorated.add(deco);
					++i;
				}
			} else if (bloc != null) {
				// s'il ne reste que des blocs
				BlocImpressionDecorator deco = 
					new BlocImpressionDecorator(
							bloc.getBlocImpression(), null, template);
				blocImpressionsDecorated.add(deco);
				++i;
			} else if (anno != null) {
				// s'il ne reste que des annotations
				BlocImpressionDecorator deco = 
					new BlocImpressionDecorator(
							null, anno.getTableAnnotation(), template);
				blocImpressionsDecorated.add(deco);
				++j;
			}
		}
		
		getBinder().loadAttribute(contenuStaticGrid, "model");
		getBinder().loadComponent(contenuStaticGrid);
	}
	
	@Override
	public void setNewObject() {
		setObject(new Template());	
		super.setNewObject();
	}
	
	@Override
	public void cloneObject() {
		setClone(this.template.clone());
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
		if (checksBlocValid()) {
			Clients.clearWrongValue(createC);
			Clients.showBusy(Labels.getLabel(
					"template.creation.encours"));
			Events.echoEvent("onLaterCreate", self, null);
		} else {
			throw new WrongValueException(
				createC, Labels.getLabel("impression.error"));
		}
	}
	
	public void onLaterCreate() {
		try {
			createNewObject();
			// log.debug("fiche: obj modifie: " + this.template.toString());
			
			if (getObjectTabController().getListe() != null) {
				// ajout du template à la liste
				getObjectTabController().getListe()
											.addToObjectList(this.template);
			}
			setObject(template);
			this.switchToStaticMode();
		} catch (RuntimeException e) {
			log.error(e);
			throw e;
		} finally {
			Clients.clearBusy();
		}
	}
	
	public void onLaterUpdate() {
		try {
			updateObject();
			// log.debug("fiche: obj modifie: " + this.template.toString());
			
			if (getObjectTabController().getListe() != null) {
				// update du template dans la liste
				getObjectTabController().getListe()
					.updateObjectGridList(this.template);
			}
			setObject(template);
			this.switchToStaticMode();
		} catch (RuntimeException e) {
			log.error(e);
			throw e;
		} finally {
			Clients.clearBusy();
		}
	}

	@Override
	public void onClick$deleteC() {
		if (this.template != null) {
			
			if (Messagebox.show(ObjectTypesFormatters.getLabel(
					"message.deletion.message", 
					new String[]{Labels
						.getLabel("message.deletion.template")}),
					Labels.getLabel("message.deletion.title"), 
					Messagebox.YES | Messagebox.NO, 
					Messagebox.QUESTION) == Messagebox.YES) {
			
				// suppression de l'objet
				ManagerLocator.getTemplateManager()
					.removeObjectManager(template);
				// log.debug("fiche: obj supprime: " + this.template);
				
				// on vérifie que l'on retrouve bien la page 
				// contenant la liste des objets
				if (getObjectTabController().getListe() != null) {
					
					// on enlève l'objet de la liste
					getObjectTabController().getListe().
						removeObjectAndUpdateList(this.template);
				}
				// clear form
				clearData();
			}
		
		}
	}

	@Override
	public void onClick$editC() {
		this.switchToEditMode();
	}

	@Override
	public void onClick$revertC() {		
		clearConstraints();
		super.onClick$revertC();
	}

	@Override
	public void onClick$validateC() {
		if (checksBlocValid()) {
			Clients.clearWrongValue(validateC);
			Clients.showBusy(Labels.getLabel(
					"template.creation.encours"));
			Events.echoEvent("onLaterUpdate", self, null);
		} else {
			throw new WrongValueException(
				createC, Labels.getLabel("impression.error"));
		}
	}
	
	@Override
	public void setEmptyToNulls() {
		if (this.template.getDescription().equals("")) {
			this.template.setDescription(null);
		}
		
		if (this.template.getEnTete().equals("")) {
			this.template.setEnTete(null);
		}
		
		if (this.template.getPiedPage().equals("")) {
			this.template.setPiedPage(null);
		}
	}
	
	@Override
	public void createNewObject() {
		// on remplit l'utilisateur en fonction des champs nulls
		setEmptyToNulls();
		
		// listes des objets associés au template
		List<BlocImpressionTemplate> blocs = 
			new ArrayList<BlocImpressionTemplate>();
		List<ChampImprime> champs = 
			new ArrayList<ChampImprime>();
		List<TableAnnotationTemplate> tables = 
			new ArrayList<TableAnnotationTemplate>();
		
		int ordre = 0;
		// pour chaque bloc
		for (int i = 0; i < blocImpressionsDecorated.size(); i++) {
			BlocImpressionDecorator deco = blocImpressionsDecorated.get(i);
			// si le bloc doit être imprimé
			if (deco.getImprimer()) {
				++ordre;
				// si c'est un bloc d'impression
				if (deco.getBlocImpression() != null) {
					// nouveau BlocImpressionTemplate
					BlocImpressionTemplate bit = 
						new BlocImpressionTemplate();
					bit.setOrdre(ordre);
					bit.setBlocImpression(deco.getBlocImpression());
					bit.setTemplate(template);
					blocs.add(bit);
					
					// si le bloc est une liste, on va traiter les
					// champs a afficher
					if (deco.getBlocImpression().getIsListe()) {
						int ordreChamps = 0;
						for (int j = 0; 
							j < deco.getChampEntites().size(); j++) {
							++ordreChamps;
							ChampEntite champ = 
								deco.getChampEntites().get(j);
							// nouveau ChampImprime
							ChampImprime ci = new ChampImprime();
							ci.setBlocImpression(deco.getBlocImpression());
							ci.setChampEntite(champ);
							ci.setTemplate(template);
							ci.setOrdre(ordreChamps);
							champs.add(ci);
						}
					}
				} else if (deco.getTableAnnotation() != null) {
					// si c'est une annotation
					// nouveau TableAnnotationTemplate
					TableAnnotationTemplate tat = 
						new TableAnnotationTemplate();
					tat.setOrdre(ordre);
					tat.setTableAnnotation(deco.getTableAnnotation());
					tat.setTemplate(template);
					tables.add(tat);
				}
			}
		}
					
		// create de l'objet
		ManagerLocator.getTemplateManager().createObjectManager(
				template, 
				SessionUtils.getSelectedBanques(sessionScope).get(0), 
				selectedEntite, 
				blocs, 
				champs, 
				tables);
	}
	

	@Override
	public void updateObject() {
		// on remplit l'utilisateur en fonction des champs nulls
		setEmptyToNulls();
		
		// listes des objets associés au template
		List<BlocImpressionTemplate> blocs = 
			new ArrayList<BlocImpressionTemplate>();
		List<ChampImprime> champs = 
			new ArrayList<ChampImprime>();
		List<TableAnnotationTemplate> tables = 
			new ArrayList<TableAnnotationTemplate>();
		List<BlocImpressionTemplate> blocsToCreate = 
			new ArrayList<BlocImpressionTemplate>();
		List<ChampImprime> champsToCreate = 
			new ArrayList<ChampImprime>();
		List<TableAnnotationTemplate> tablesToCreate = 
			new ArrayList<TableAnnotationTemplate>();
		List<BlocImpressionTemplate> blocsOld = ManagerLocator
			.getBlocImpressionTemplateManager()
			.findByTemplateManager(template);
		List<ChampImprime> champsOld = ManagerLocator
			.getChampImprimeManager().findByTemplateManager(template);
		List<TableAnnotationTemplate> tablesOld = ManagerLocator
			.getTableAnnotationTemplateManager()
			.findByTemplateManager(template);
		
		int ordre = 0;
		// pour chaque bloc
		for (int i = 0; i < blocImpressionsDecorated.size(); i++) {
			BlocImpressionDecorator deco = blocImpressionsDecorated.get(i);
			// si le bloc doit être imprimé
			if (deco.getImprimer()) {
				++ordre;
				// si c'est un bloc d'impression
				if (deco.getBlocImpression() != null) {
					// nouveau BlocImpressionTemplate
					BlocImpressionTemplate bit = 
						new BlocImpressionTemplate();
					bit.setOrdre(ordre);
					bit.setBlocImpression(deco.getBlocImpression());
					bit.setTemplate(template);
					blocs.add(bit);
					if (!blocsOld.contains(bit)) {
						blocsToCreate.add(bit);
					}
					
					// si le bloc est une liste, on va traiter les
					// champs a afficher
					if (deco.getBlocImpression().getIsListe()) {
						int ordreChamps = 0;
						for (int j = 0; 
							j < deco.getChampEntites().size(); j++) {
							++ordreChamps;
							ChampEntite champ = 
								deco.getChampEntites().get(j);
							// nouveau ChampImprime
							ChampImprime ci = new ChampImprime();
							ci.setBlocImpression(deco.getBlocImpression());
							ci.setChampEntite(champ);
							ci.setTemplate(template);
							ci.setOrdre(ordreChamps);
							champs.add(ci);
							if (!champsOld.contains(ci)) {
								champsToCreate.add(ci);
							}
						}
					}
				} else if (deco.getTableAnnotation() != null) {
					// si c'est une annotation
					// nouveau TableAnnotationTemplate
					TableAnnotationTemplate tat = 
						new TableAnnotationTemplate();
					tat.setOrdre(ordre);
					tat.setTableAnnotation(deco.getTableAnnotation());
					tat.setTemplate(template);
					tables.add(tat);
					if (!tablesOld.contains(tat)) {
						tablesToCreate.add(tat);
					}
				}
			}
		}
					
		// create de l'objet
		ManagerLocator.getTemplateManager().updateObjectManager(
				template, 
				SessionUtils.getSelectedBanques(sessionScope).get(0), 
				selectedEntite, 
				blocs, 
				blocsToCreate,
				champs, 
				champsToCreate,
				tables,
				tablesToCreate);
	}
	
	/* Methode comportementales */
	/**
	 * Change le mode de la fiche en creation.
	 */
	public void switchToCreateMode() {
		
		this.blocImpressionsDecorated = 
			new ArrayList<BlocImpressionDecorator>();
		
		super.switchToCreateMode();
		initEditableMode();
		
		contenuEditGrid.setVisible(false);
		
		getBinder().loadComponent(self);
	}
	
	public void switchToEditMode() {
		super.switchToEditMode();
		selectedEntite = this.template.getEntite();
		
		// on cache l'entité
		entiteRequired.setVisible(false);
		entitesBox.setVisible(false);
		defineBlocsRow.setVisible(false);
		
		entiteLabel.setVisible(true);
		contenuEditGrid.setVisible(true);
		groupContenu.setVisible(true);
		
		// on récupère tous les blocs pour l'entité
		List<BlocImpression> blocImpressions = 
			ManagerLocator.getBlocImpressionManager()
			.findByEntiteManager(selectedEntite);
		for (int i = 0; i < blocImpressions.size(); i++) {
			BlocImpressionDecorator deco = 
				new BlocImpressionDecorator(
						blocImpressions.get(i), null, template);
			if (!blocImpressionsDecorated.contains(deco)) {
				deco.setImprimer(false);
				blocImpressionsDecorated.add(deco);
			}
		}
		
		// on récupère toutes les tables d'annotations pour
		// l'entité et la banque
		List<TableAnnotation> tables = ManagerLocator
			.getTableAnnotationManager()
			.findByEntiteAndBanqueManager(
			selectedEntite, 
				SessionUtils.getSelectedBanques(sessionScope).get(0));
		for (int i = 0; i < tables.size(); i++) {
			BlocImpressionDecorator deco = 
				new BlocImpressionDecorator(null, 
						tables.get(i), template);
			if (!blocImpressionsDecorated.contains(deco)) {
				deco.setImprimer(false);
				blocImpressionsDecorated.add(deco);
			}
		}
		
		getBinder().loadComponent(self);
	}
	
	/**
	 * Change mode de la fiche en mode statique.
	 * @param detailMode Si true, la fiche se trouve dans une nouvelle
	 * fenêtre pour afficher un contrat
	 */
	public void switchToStaticMode() {
		super.switchToStaticMode(this.template.equals(new Template()));
	}
	
	@Override
	public void setFocusOnElement() {
		nomBox.setFocus(true);
	}
	
	@Override
	public void setFieldsToUpperCase() {
	}

	@Override
	public void clearData() {
		blocImpressionsDecorated = new ArrayList<BlocImpressionDecorator>();
		clearConstraints();
		super.clearData();			
	}

	/**
	 * Clic sur le bouton defineBlocs : l'utilisateur a choisi
	 * l'entité, on crée la table contenant les blocs à imprimer.
	 */
	public void onClick$defineBlocs() {
		groupContenu.setVisible(true);
		defineBlocsRow.setVisible(false);
		
		this.template.setEntite(selectedEntite);
		entitesBox.setVisible(false);
		entiteLabel.setVisible(true);
		contenuEditGrid.setVisible(true);
		
		// on récupère tous les blocs pour l'entité
		List<BlocImpression> blocImpressions = 
			ManagerLocator.getBlocImpressionManager()
			.findByEntiteManager(selectedEntite);
		
		for (int i = 0; i < blocImpressions.size(); i++) {
			BlocImpressionDecorator deco = 
				new BlocImpressionDecorator(
						blocImpressions.get(i), null, template);
			blocImpressionsDecorated.add(deco);
		}
		
		// on récupère toutes les tables d'annotations pour
		// l'entité et la banque
		List<TableAnnotation> tables = ManagerLocator
			.getTableAnnotationManager()
			.findByEntiteAndBanqueManager(
			selectedEntite, 
				SessionUtils.getSelectedBanques(sessionScope).get(0));
		for (int i = 0; i < tables.size(); i++) {
			BlocImpressionDecorator deco = 
				new BlocImpressionDecorator(null, 
						tables.get(i), template);
			blocImpressionsDecorated.add(deco);
		}
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForTemplate() {
		Boolean admin = false;
		if (sessionScope.containsKey("AdminPF")) {
			admin = (Boolean) sessionScope.get("AdminPF");
		} else if (sessionScope.containsKey("Admin")) {
			admin = (Boolean) sessionScope.get("Admin");
		}
		
		if (admin) {
			setCanNew(true);
			setCanEdit(true);
			setCanDelete(true);
		} else {
			setCanNew(false);
			setCanEdit(false);
			setCanDelete(false);
		}
	}
	
	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(nomBox);
		Clients.clearWrongValue(descriptionBox);
		Clients.clearWrongValue(enteteBox);
		Clients.clearWrongValue(piedPageBox);
	}
	
	
	
	/*******************************************************/
	/**                  GETTERS - SETTERS                 */
	/*******************************************************/
	public BlocImpressionRowRenderer getBlocImpressionRenderer() {
		return blocImpressionRenderer;
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

	public BlocImpressionRowRenderer getBlocImpressionRendererEdit() {
		return blocImpressionRendererEdit;
	}
	
	public ConstWord getNomConstraint() {
		return TemplateConstraints.getNomConstraint();
	}
	
	public ConstWord getNomNullConstraint() {
		return TemplateConstraints.getNomNullConstraint();
	}
	
	public ConstText getDescConstraint() {
		return TemplateConstraints.getDescConstraint();
	}

	@Override
	public String getDeleteWaitLabel() {
		return null;
	}

	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public boolean prepareDeleteObject() {
		return false;
	}

	@Override
	public void removeObject(String comments) {		
	}

	@Override
	public void setParentObject(TKdataObject obj) {		
	}

}
