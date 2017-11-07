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
package fr.aphp.tumorotek.action.contexte;


import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstEmail;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheTransporteur extends AbstractFicheCombineController {
	
	// private Log log = LogFactory.getLog(FicheTransporteur.class);

	private static final long serialVersionUID = 3040639427194909476L;
	
	// Static Components pour le mode static.
	private Label nomLabel;
	private Label adresseLabel;
	private Label cpLabel;
	private Label villeLabel;
	private Label paysLabel;
	private Label telLabel;
	private Label faxLabel;
	private Label mailLabel;
	private Label contactNomLabel;
	private Label contactPrenomLabel;
	private Label contactTelLabel;
	private Label contactFaxLabel;
	private Label contactMailLabel;
	private Menubar menuBar;
	private Label archiveLabel;
	
	// Editable components : mode d'édition ou de création.
	private Label nomRequired;
	private Label contactNomRequired;
	private Textbox nomBox;
	private Textbox adresseBox;
	private Textbox cpBox;
	private Textbox villeBox;
	private Textbox paysBox;
	private Textbox telBox;
	private Textbox faxBox;
	private Textbox mailBox;
	private Textbox contactNomBox;
	private Textbox contactPrenomBox;
	private Textbox contactTelBox;
	private Textbox contactFaxBox;
	private Textbox contactMailBox;
	private Checkbox archiveBox;
	
	// Objets Principaux.
	private Transporteur transporteur;
	private Coordonnee coordonnee;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setDeletionMessage("message.deletion.transporteur");
		setCascadable(false);
		setDeletable(true);

		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
				this.nomLabel,
				this.adresseLabel,
				this.cpLabel,
				this.villeLabel,
				this.paysLabel,
				this.telLabel,
				this.faxLabel,
				this.mailLabel,
				this.contactNomLabel,
				this.contactPrenomLabel,
				this.contactTelLabel,
				this.contactFaxLabel,
				this.contactMailLabel,
				this.menuBar,
				this.archiveLabel
		});
		
		setObjBoxsComponents(new Component[]{
				this.nomRequired,
				this.contactNomRequired
		});
		
		setRequiredMarks(new Component[]{
				this.nomBox,
				this.adresseBox,
				this.cpBox,
				this.villeBox,
				this.paysBox,
				this.telBox,
				this.faxBox,
				this.mailBox,
				this.contactNomBox,
				this.contactPrenomBox,
				this.contactTelBox,
				this.contactFaxBox,
				this.contactMailBox,
				this.archiveBox
		});	
				
		drawActionsForTransporteur();
		
		if (winPanel != null) {
			winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
		}
		
		menuBar.setVisible(false);
		
		getBinder().loadAll();
		
	}
	
	@Override
	public void setObject(TKdataObject obj) {
		this.transporteur = (Transporteur) obj;
		
		if (transporteur != null 
				&& transporteur.getTransporteurId() != null) {
			coordonnee = transporteur.getCoordonnee();
		} else {
			coordonnee = null;
		}
		
		if (coordonnee == null) {
			coordonnee = new Coordonnee();
		}
				
		super.setObject(transporteur);		
	}
	
	@Override
	public void cloneObject() {
		setClone(this.transporteur.clone());
	}
	
	@Override
	public Transporteur getObject() {
		return this.transporteur;
	}
	
	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public void setParentObject(TKdataObject obj) {
	}

	@Override
	public TransporteurController getObjectTabController() {
		return (TransporteurController) super.getObjectTabController();
	}

	@Override
	public void setNewObject() {
		setObject(new Transporteur());
		super.setNewObject();
	}
	
	@Override
	public void switchToCreateMode() {
		
		super.switchToCreateMode();
		
		getBinder().loadComponent(self);
		
	}
	
	@Override
	public void switchToStaticMode() {
		super.switchToStaticMode(this.transporteur.equals(
				new Transporteur()));
		
		if (this.transporteur.getTransporteurId() == null) {
			menuBar.setVisible(false);
		}
				
		getBinder().loadComponent(self);
	}
	
	@Override
	public void switchToEditMode() {
		super.switchToEditMode();
				
		getBinder().loadComponent(self);
		
	}
	
	@Override
	public void setFocusOnElement() {
		nomBox.setFocus(true);
	}

	@Override
	public void clearData() {
		clearConstraints();
		super.clearData();
	}
	
	/**
	 * Recupere le controller du composant representant la liste associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche ListeProfil
	 */
	private ListeTransporteur getListeTransporteur() {
		return (ListeTransporteur) getObjectTabController().getListe();
	}
	
	@Override
	public void onClick$addNewC() {
		switchToCreateMode();
	}

	@Override
	public void onClick$cancelC() {
		clearData();
	}

	@Override
	public void onClick$createC() {
		Clients.showBusy(Labels.getLabel(
				"transporteur.creation.encours"));
		Events.echoEvent("onLaterCreate", self, null);
	}
	
	public void onLaterCreate() {
		try {
			createNewObject();
			cloneObject();
			switchToStaticMode();
			disableToolBar(false);
		
			// ferme wait message
			Clients.clearBusy();
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@Override
	public void onClick$editC() {
		if (this.transporteur != null) {
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
		Clients.showBusy(Labels.getLabel(
				"transporteur.creation.encours"));
		Events.echoEvent("onLaterUpdate", self, null);
	}
	
	public void onLaterUpdate() {
		try {
			updateObject();
			cloneObject();
			switchToStaticMode();

			// ferme wait message
			Clients.clearBusy();
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} 
	}

	@Override
	public void createNewObject() {
		// on remplit le transporteur en fonction des champs nulls
		setEmptyToNulls();
		setFieldsToUpperCase();
		transporteur.setNom(transporteur.getNom().toUpperCase());
		transporteur.setArchive(archiveBox.isChecked());
			
		// update de l'objet
		ManagerLocator.getTransporteurManager()
			.createObjectManager(transporteur, coordonnee,
					SessionUtils.getLoggedUser(sessionScope));
		
		if (getListeTransporteur() != null) {
			// ajout du transporteur à la liste
			getListeTransporteur().addToObjectList(transporteur);
		}
	}

	@Override
	public void setEmptyToNulls() {
		if (this.transporteur.getContactPrenom().equals("")) {
			this.transporteur.setContactPrenom(null);
		}
		
		if (this.transporteur.getContactTel().equals("")) {
			this.transporteur.setContactTel(null);
		}
		
		if (this.transporteur.getContactFax().equals("")) {
			this.transporteur.setContactFax(null);
		}
		
		if (this.transporteur.getContactMail().equals("")) {
			this.transporteur.setContactMail(null);
		}
		
		if (this.coordonnee.getAdresse().equals("")) {
			this.coordonnee.setAdresse(null);
		}
		if (this.coordonnee.getCp().equals("")) {
			this.coordonnee.setCp(null);
		}
		if (this.coordonnee.getVille().equals("")) {
			this.coordonnee.setVille(null);
		}
		if (this.coordonnee.getPays().equals("")) {
			this.coordonnee.setPays(null);
		}
		if (this.coordonnee.getTel().equals("")) {
			this.coordonnee.setTel(null);
		}
		if (this.coordonnee.getFax().equals("")) {
			this.coordonnee.setFax(null);
		}
		if (this.coordonnee.getMail().equals("")) {
			this.coordonnee.setMail(null);
		}
	}

	@Override
	public void updateObject() {
		// on remplit le transporteur en fonction des champs nulls
		setEmptyToNulls();
		setFieldsToUpperCase();
		transporteur.setNom(transporteur.getNom().toUpperCase());
		transporteur.setArchive(archiveBox.isChecked());
			
		// update de l'objet
		ManagerLocator.getTransporteurManager()
			.updateObjectManager(transporteur, coordonnee,
					SessionUtils.getLoggedUser(sessionScope));
		
		if (getListeTransporteur() != null) {
			// update du transporteur dans la liste
			getListeTransporteur().updateObjectGridList(this.transporteur);
		}
	}
	
	@Override
	public boolean prepareDeleteObject() {
		boolean isReferenced = ManagerLocator
			.getTransporteurManager()
						.isReferencedObjectManager(getObject());
		setDeleteMessage(ObjectTypesFormatters
				.getLabel("message.deletion.message", 
						new String[]{Labels
								.getLabel(getDeletionMessage())}));
		setDeletable(true);
		if (isReferenced) {
			setDeleteMessage(Labels
				.getLabel("transporteur.deletion.isReferenced"));
			if (getObject().getArchive()) { // deja archivé
				setDeletable(false);
				setDeleteMessage(Labels
						.getLabel("transporteur.deletion.isAlreadyArchived"));
			} 
		}
		setFantomable(!isReferenced);
		return isReferenced;
	}
	
	@Override
	public void removeObject(String comments) {		
		if (isFantomable()) { // suppression possible
			ManagerLocator.getTransporteurManager().
				removeObjectManager(getObject(), comments, 
						SessionUtils.getLoggedUser(sessionScope));
		} else { // archivage
			getObject().setArchive(true);
			ManagerLocator.getTransporteurManager()
				.updateObjectManager(getObject(),
						getObject().getCoordonnee(),
						SessionUtils.getLoggedUser(sessionScope));
		}
	}
	
	@Override
	public void onLaterDelete(Event event) {
		try {		
			removeObject((String) event.getData());
			
			if (isFantomable()) {
				if (getListeTransporteur() != null) {
					getListeTransporteur()
							.removeObjectAndUpdateList(getObject());
				}
				// clear form
				clearData();
			} else {
				if (getListeTransporteur() != null) {
					// update du transporteur dans la liste
					getListeTransporteur()
						.updateObjectGridList(getObject());
				}
			}
			
			
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
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(nomBox);
		Clients.clearWrongValue(adresseBox);
		Clients.clearWrongValue(cpBox);
		Clients.clearWrongValue(villeBox);
		Clients.clearWrongValue(paysBox);
		Clients.clearWrongValue(telBox);
		Clients.clearWrongValue(faxBox);
		Clients.clearWrongValue(mailBox);
		Clients.clearWrongValue(contactNomBox);
		Clients.clearWrongValue(contactPrenomBox);
		Clients.clearWrongValue(contactTelBox);
		Clients.clearWrongValue(contactFaxBox);
		Clients.clearWrongValue(contactMailBox);
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForTransporteur() {
		boolean admin = false;
		if (sessionScope.containsKey("AdminPF")) {
			admin = (Boolean) sessionScope.get("AdminPF");
		}
		
		// si l'utilisateur est admin PF => boutons cliquables
		if (admin) {
			setCanNew(true);
			setCanEdit(true);
			setCanDelete(true);
			setCanSeeHistorique(true);
		} else {
			setCanNew(false);
			setCanEdit(false);
			setCanDelete(false);
			setCanSeeHistorique(false);
		}
	}
	
	@Override
	public void disableToolBar(boolean b) {
		if (sessionScope.containsKey("Admin")
				&& (Boolean) sessionScope.get("Admin")) {
			super.disableToolBar(false);
		} else {
			super.disableToolBar(true);
		}
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * nomBox. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$nomBox(Event event) {
		nomBox.setValue(nomBox.getValue().toUpperCase().trim());
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * paysBox. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$paysBox(Event event) {
		paysBox.setValue(paysBox.getValue().toUpperCase().trim());
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * villeBox. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$villeBox(Event event) {
		villeBox.setValue(villeBox.getValue().toUpperCase().trim());
	}
	
	@Override
	public void setFieldsToUpperCase() {
		if (this.transporteur.getNom() != null) {
			this.transporteur.setNom(
				this.transporteur.getNom().toUpperCase().trim());
		}
		
		if (this.coordonnee.getVille() != null) {
			this.coordonnee.setVille(
				this.coordonnee.getVille().toUpperCase().trim());
		}
		
		if (this.coordonnee.getPays() != null) {
			this.coordonnee.setPays(
				this.coordonnee.getPays().toUpperCase().trim());
		}
	}
	
	/*********************************************************/
	/********************** ACCESSEURS. **********************/
	/*********************************************************/
	
	public ConstWord getNomTransporteurConstraint() {
		return ContexteConstraints.getNomTransporteurConstraint();
	}
	
	public ConstWord getPrenomTransporteurConstraint() {
		return ContexteConstraints.getPrenomTransporteurConstraint();
	}
	
	public ConstText getAddrConstraint() {
		return ContexteConstraints.getAddrConstraint();
	}
	
	public ConstWord getVillePaysConstraint() {
		return ContexteConstraints.getVillePaysConstraint();
	}
	
	public ConstCode getCpConstraint() {
		return ContexteConstraints.getCpConstraint();
	}
	
	public ConstCode getTelFaxConstraint() {
		return ContexteConstraints.getTefFaxConstraint();
	}
	
	public ConstEmail getEmailConstraint() {
		return ContexteConstraints.getEmailConstraint();
	}

	public Transporteur getTransporteur() {
		return transporteur;
	}

	public void setTransporteur(Transporteur t) {
		this.transporteur = t;
	}

	public Coordonnee getCoordonnee() {
		return coordonnee;
	}

	public void setCoordonnee(Coordonnee c) {
		this.coordonnee = c;
	}
	
	@Override
	public String getDeleteWaitLabel() {
		if (isFantomable()) {
			return Labels.getLabel("deletion.general.wait");
		} else {
			return Labels.getLabel("archivage.general.wait");
		}
	}
	
	/**
	 * Formate la valeur du champ Archive.
	 * @return Oui ou non.
	 */
	public String getArchiveFormated() {
		
		if (this.transporteur != null) {
			return ObjectTypesFormatters
				.booleanLitteralFormatter(this.transporteur.getArchive());
		} else {
			return "";
		}
	}

}
