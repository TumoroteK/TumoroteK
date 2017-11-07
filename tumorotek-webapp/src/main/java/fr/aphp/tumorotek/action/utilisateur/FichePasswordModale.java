package fr.aphp.tumorotek.action.utilisateur;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstPassword;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FichePasswordModale extends GenericForwardComposer {

	private static final long serialVersionUID = -2054096107955103379L;
	
	private Component parent;
	private Utilisateur utilisateur;
	private String oldPassword;
	private String newPassword;
	private String confirmationPassword;
	
	private Textbox ancienPasswordBox;
	private Textbox confirmPasswordBox;
	private Row rowOldPassword;
	
	private boolean isAdminPF = false;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		AnnotateDataBinder binder = new AnnotateDataBinder(comp);	
		binder.loadComponent(comp);
	}
	
	public void initModale(Component comp, Utilisateur user) {
		parent = comp;
		utilisateur = user;
		
		if (sessionScope.containsKey("AdminPF")) {
			isAdminPF = (Boolean) sessionScope.get("AdminPF");
			rowOldPassword.setVisible(false);
		}
	}
	
	public void onOK() {
		onClick$validate();
	}
	
	public void onClick$validate() {
		if (!isAdminPF) {
			// vérification de l'ancien mdp
			if (!ObjectTypesFormatters.getEncodedPassword(oldPassword)
					.equals(this.utilisateur.getPassword())) {
				throw new WrongValueException(
						ancienPasswordBox, 
						Labels.getLabel("utilisateur.bad.old.password"));
			}
		}
		// vérification de la confirmation du mdp
		if (!confirmationPassword.equals(newPassword)) {
			throw new WrongValueException(
				confirmPasswordBox, 
				Labels.getLabel("utilisateur.bad.password"));
		}
		
		if (!ObjectTypesFormatters.getEncodedPassword(newPassword).equals(
				utilisateur.getPassword())) {
			Clients.showBusy(Labels.getLabel(
					"utilisateur.creation.encours"));
			Events.echoEvent("onLaterUpdate", self, null);
		} else {
			Messagebox.show(Labels.getLabel("utilisateur.same.password"), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onLaterUpdate() {
		try {
			// sauvegarde
			ManagerLocator.getUtilisateurManager().updatePasswordManager(
					utilisateur, 
					ObjectTypesFormatters.getEncodedPassword(newPassword),
					ObjectTypesFormatters.getNbMoisMdp(),
					SessionUtils.getLoggedUser(sessionScope));
			// ferme wait message
			Clients.clearBusy();
			// envoie de l'utilisateur modifié
			Events.postEvent("onGetPasswordUpdated", 
					getParent(), utilisateur);		
			// fermeture de la fenêtre
			Events.postEvent(new Event("onClose", self.getRoot()));
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
		}
	}
	
	public void onClick$cancel() {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	public ConstPassword getPasswordConstraint() {
		return UtilisateurConstraints.getPasswordConstraint();
	}

	public Component getParent() {
		return parent;
	}

	public void setParent(Component p) {
		this.parent = p;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur u) {
		this.utilisateur = u;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String pwd) {
		this.oldPassword = pwd;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String pwd) {
		this.newPassword = pwd;
	}

	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	public void setConfirmationPassword(String pwd) {
		this.confirmationPassword = pwd;
	}

	public boolean isAdminPF() {
		return isAdminPF;
	}

	public void setAdminPF(boolean isA) {
		this.isAdminPF = isA;
	}

}
