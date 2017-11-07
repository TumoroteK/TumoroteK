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
package fr.aphp.tumorotek.action.utilisateur;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstEmail;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheUtilisateurModale extends AbstractFicheCombineController {
	
	// private Log log = LogFactory.getLog(FicheUtilisateurModale
	//		.class);

	private static final long serialVersionUID = 666483240911965652L;
	
	// Static Components pour le mode static.
	private Label emailLabel;
	private Row rowPfsAdmin;
	private Button close;
	
	// Editable components : mode d'édition ou de création.
	private Textbox emailBox;
	private Group groupRoles;
	
	// Objets Principaux.
	private Utilisateur user;
	private String path;
	private boolean changed = false;
	private Integer nbMoisMdp = null;
	
	// Associations.
	private List<ProfilUtilisateur> profilUtilisateurs = 
											new ArrayList<ProfilUtilisateur>();
	private List<ProfilUtilisateur> profilUtilisateursOtherBanques = 
		new ArrayList<ProfilUtilisateur>();
	private List<Plateforme> plateformes = new ArrayList<Plateforme>();
	
	// Variables formulaire.
	private String plateformesFormated = "";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setDeletionMessage("message.deletion.utilisateur");
		setFantomable(false);
		setDeletable(false);
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
				this.emailLabel,
				this.rowPfsAdmin,
				this.close
		});
		
		setObjBoxsComponents(new Component[]{
				this.emailBox
		});
		
		setRequiredMarks(new Component[]{
		});
		
		setCanEdit(true);
		setCanNew(false);
		setCanDelete(false);
		
		if (winPanel != null) {
			winPanel.setHeight("435px");
		}
		
		nbMoisMdp = ObjectTypesFormatters.getNbMoisMdp();
		
		groupRoles.setOpen(false);
		
		getBinder().loadAll();
		
	}
	
	/**
	 * Méthode intialisant le composant.
	 * @param utilisateur User.
	 */
	public void init(Utilisateur utilisateur, String p) {
		path = p;
		setObject(utilisateur);
		switchToStaticMode();
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void setObject(TKdataObject obj) {
		this.user = (Utilisateur) obj;
		
		profilUtilisateurs.clear();
		profilUtilisateursOtherBanques.clear();
		plateformes.clear();
		plateformesFormated = "";
		
		if (user.getUtilisateurId() != null) {
			// on récup tous les profils
			profilUtilisateurs = ManagerLocator
				.getProfilUtilisateurManager()
				.findByUtilisateurManager(user, null);
		
			plateformes.addAll(ManagerLocator.getUtilisateurManager()
				.getPlateformesManager(user));
			
		}
		
		StringBuffer sb = new StringBuffer();
		if (plateformes.size() > 0) {
			for (int i = 0; i < plateformes.size(); i++) {
				sb.append(plateformes.get(i).getNom());
				if (i + 1 < plateformes.size()) {
					sb.append(", ");
				}
			}
		} else {
			sb.append(Labels.getLabel("utilisateur.plateformes.aucune"));
		}
		plateformesFormated = sb.toString();
		
		super.setObject(user);		
	}
	
	@Override
	public void cloneObject() {
		setClone(this.user.clone());
	}
	
	@Override
	public Utilisateur getObject() {
		return this.user;
	}
	
	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public void setParentObject(TKdataObject obj) {
	}

	@Override
	public UtilisateurController getObjectTabController() {
		return (UtilisateurController) super.getObjectTabController();
	}

	@Override
	public void setNewObject() {
		setObject(new Utilisateur());
		super.setNewObject();
	}
	
	@Override
	public void switchToCreateMode() {
	}
	
	@Override
	public void switchToStaticMode() {
		super.switchToStaticMode(this.user.equals(new Utilisateur()));
		
		addNewC.setVisible(false);
		deleteC.setVisible(false);
		
		getBinder().loadComponent(self);
	}
	
	/**
	 * Change mode de la fiche en mode edition.
	 */
	public void switchToEditMode() {
		
		super.switchToEditMode();
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void setFocusOnElement() {
		emailBox.setFocus(true);
	}

	@Override
	public void clearData() {
		clearConstraints();
		super.clearData();
	}
	
	@Override
	public void createNewObject() {
	}

	@Override
	public void onClick$addNewC() {
	}

	@Override
	public void onClick$cancelC() {
		clearData();
	}

	@Override
	public void onClick$createC() {
	}
	
	@Override
	public boolean prepareDeleteObject() {
		return true;
	}
	
	@Override
	public void removeObject(String comments) {	
	}
	
	@Override
	public void onLaterDelete(Event event) {
	}
	
	@Override
	public void onClick$revertC() {
		clearConstraints();
		super.onClick$revertC();
	}
	
	public void onClick$close() {
		if (changed) {
			// si le chemin d'accès à la page est correcte
			if (Path.getComponent(path) != null) {
				if (user != null) {
					// on envoie un event à cette page avec
					// le patient sélectionné
					Events.postEvent(
						new Event("onGetUserUpdated", 
							Path.getComponent(path), 
							user));
				}
			}
		}
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}

	@Override
	public void onClick$validateC() {
		Clients.showBusy(Labels.getLabel("utilisateur.creation.encours"));
		Events.echoEvent("onLaterUpdate", self, null);
	}
	
	public void onLaterUpdate() {
		try {
			updateObject();
			cloneObject();
			switchToStaticMode(); 
			
			// ferme wait message
			Clients.clearBusy();
			
			changed = true;
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} 
	}

	@Override
	public void setEmptyToNulls() {
		if (this.user.getEmail().equals("")) {
			this.user.setEmail(null);
		}
	}

	@Override
	public void updateObject() {
		// on remplit l'utilisateur en fonction des champs nulls
		setEmptyToNulls();
			
		// on fusionne les 2 listes de profils
		List<ProfilUtilisateur> tmp = new ArrayList<ProfilUtilisateur>();
		tmp.addAll(profilUtilisateurs);
		tmp.addAll(profilUtilisateursOtherBanques);
						
		// update de l'objet
		ManagerLocator.getUtilisateurManager().updateObjectManager(
				user, 
				user.getCollaborateur(), 
				tmp, 
				null,
				SessionUtils.getLoggedUser(sessionScope),
				ManagerLocator.getOperationTypeManager()
					.findByNomLikeManager("Modification", true).get(0));
	}
	
	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(emailBox);
	}

	@Override
	public String getDeleteWaitLabel() {
		return null;
	}

	@Override
	public void setFieldsToUpperCase() {
	}

	public ConstEmail getEmailConstraint() {
		return UtilisateurConstraints.getEmailConstraint();
	}
	
	/**
	 * Formate la valeur du champ super.
	 * @return Oui ou non.
	 */
	public String getSuperFormated() {
		
		if (this.user != null) {
			return ObjectTypesFormatters
				.booleanLitteralFormatter(this.user.isSuperAdmin());
		} else {
			return "";
		}
	}
	
	/**
	 * Formate la date de création de l'utilisateur.
	 * @return Date de création formatée.
	 */
	public String getDateCreationFormated() {
		if (this.user != null && !this.user.equals(new Utilisateur())) {
			
			Calendar date = ManagerLocator.getOperationManager()
				.findDateCreationManager(user);
			
			if (date != null) {
				return ObjectTypesFormatters.dateRenderer2(date);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Formate la date de stockage de l'échantillon.
	 * @return Date de stockage formatée.
	 */
	public String getTimeoutFormated() {
		if (this.user != null) {
			return ObjectTypesFormatters
								.dateRenderer2(this.user.getTimeOut());
		} else {
			return null;
		}
	}
	
	/**
	 * Formate le message d'explication sur la durée de validité du MDP.
	 * @return
	 */
	public String getTimeoutLabelFormated() {
		String value = "";
		
		if (nbMoisMdp != null && nbMoisMdp > 0) {
			value = ObjectTypesFormatters.getLabel(
					"utilisateur.timeout.help", 
					new String[] {String.valueOf(nbMoisMdp)});
		}
		
		return value;
	}
	
	public void onClick$editPassword() {
		openPasswordWindow(page, self, user);
	}
	
	/**
	 * Méthode appelée après modification du mdp. On va alors
	 * mettre à jour la fiche et la liste de utilisateurs.
	 * @param event
	 */
	public void onGetPasswordUpdated(Event event) {
		if (event.getData() != null) {
			Utilisateur u = (Utilisateur) event.getData();
			
			// maj de la fiche
			setObject(u);
			
			changed = true;
		}
	}
	
	/*********************************************************/
	/********************** ACCESSEURS. **********************/
	/*********************************************************/

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur u) {
		this.user = u;
	}

	public List<ProfilUtilisateur> getProfilUtilisateurs() {
		return profilUtilisateurs;
	}

	public void setProfilUtilisateurs(List<ProfilUtilisateur> pu) {
		this.profilUtilisateurs = pu;
	}

	public List<ProfilUtilisateur> getProfilUtilisateursOtherBanques() {
		return profilUtilisateursOtherBanques;
	}

	public void setProfilUtilisateursOtherBanques(
			List<ProfilUtilisateur> pu) {
		this.profilUtilisateursOtherBanques = pu;
	}

	public List<Plateforme> getPlateformes() {
		return plateformes;
	}

	public void setPlateformes(List<Plateforme> p) {
		this.plateformes = p;
	}

	public String getPlateformesFormated() {
		return plateformesFormated;
	}

	public void setPlateformesFormated(String p) {
		this.plateformesFormated = p;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String p) {
		this.path = p;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean c) {
		this.changed = c;
	}

	public Integer getNbMoisMdp() {
		return nbMoisMdp;
	}

	public void setNbMoisMdp(Integer nb) {
		this.nbMoisMdp = nb;
	}

}
