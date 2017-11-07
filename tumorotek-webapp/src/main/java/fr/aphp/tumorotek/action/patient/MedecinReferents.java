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
package fr.aphp.tumorotek.action.patient;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 * Controller du composant Medecin referents.
 * Permet l'ajout d'association N - N entre un objet de des collaborateurs.
 * Date : 15/02/2010.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class MedecinReferents extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	
	private List<Collaborateur> medecins = new ArrayList<Collaborateur>();	
	private Collaborateur selectedMedecin;
	private AbstractController ficheParent;
	
	private Listbox referentsList;
	private Button addMed;
	private Listcell deleteCell;
	private Listheader deleteHeader;
	private Label medecinLabel;
	
	public List<Collaborateur> getMedecins() {
		return medecins;
	}

	public void setMedecins(List<Collaborateur> meds) {
		this.medecins = meds;
	}
	
	public Collaborateur getSelectedMedecin() {
		return selectedMedecin;
	}
	
	/**
	 * Rafraichit la liste des medecins referents.
	 * @param cur
	 */
	public void setSelectedMedecin(Collaborateur cur) {
		this.selectedMedecin = cur;
	}
	
	public void setFicheParent(AbstractController parent) {
		this.ficheParent = parent;
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		drawActionsForMedecins();
	}
	
	/**
	 * Affiche la fiche d'un medecin referent.
	 */
	public void onClick$medecinLink(Event event) {
		if (getDroitsConsultation().get("Collaborateur")) {
			// recupere le medecin
			Object med = AbstractListeController2
							.getBindingData((ForwardEvent) event, false); 
						
			// ouvre la modale
			openCollaborationsWindow(page, 
									"context.modal.referent",
									"details", med,
									null, null, null, null);
		}
	}
	
	/**
	 * Affiche les boutons delete et addNew.
	 */
	public void switchToEditMode() {
		deleteCell.setVisible(true);
		deleteHeader.setVisible(true);
		addMed.setVisible(true);
	}
	
	/**
	 * Affiche les boutons delete et addNew.
	 */
	public void switchToCreateMode() {
		this.medecins.clear();
		ficheParent.getBinder().loadComponent(referentsList);
		switchToEditMode();
	}
	
	/**
	 * Efface les boutons delete et addNew.
	 */
	public void switchToStaticMode() {
		deleteCell.setVisible(false);
		deleteHeader.setVisible(false);
		addMed.setVisible(false);
	}
	
	/**
	 * Retire le medecin de la liste des référents.
	 */
	public void onClick$deleteCell(Event event) {
		if (!addMed.isDisabled()) {
			// recupere le medecin
			Object med = AbstractListeController2
							.getBindingData((ForwardEvent) event, false); 
			
			this.medecins.remove(med);
			ficheParent.getBinder().loadAttribute(referentsList, "model");
			
			refreshReferentsHeaderCount();
		}
	}
	
	/**
	 * Ouvre la modale Contexte pour proposer le choix de l'ajout
	 * du nouveau medecin.
	 */
	public void onClick$addMed() {
		// on récupère le collaborateur actuellement sélectionné
		// pour l'afficher dans la modale
		List<Object> old = new ArrayList<Object>();
		for (int i = 0; i < medecins.size(); i++) {
			old.add(medecins.get(i));
		}
		// ouvre la modale
		openCollaborationsWindow(page, 
								"patient.button.newMedecin",
								"select", null,
								"Collaborateur", null, 
								Path.getPath(self),
								old);
		
		refreshReferentsHeaderCount();
	}
	
	/**
	 * Rafraichit le groupHeader du parent pour afficher le bon compte.
	 */
	private void refreshReferentsHeaderCount() {
		Events.postEvent("onRefreshHeader", 
			self.getParent().getParent().getFellow("referentsGroup"), null);
	}

	/**
	 * Méthode appelée par la fenêtre CollaborationsController quand
	 * l'utilisateur sélectionne un collaborateur.
	 * @param e Event contenant le collaborateur sélectionné.
	 */
	public void onGetObjectFromSelection(Event e) {
					
		// si un collaborateur a été sélectionné
		if (e.getData() != null) {
			Collaborateur coll = (Collaborateur) e.getData();
			if (!this.medecins.contains(coll)) {
				this.medecins.add(coll);
				ficheParent.getBinder()
								.loadAttribute(referentsList, "model");
			}
		}
	}
	
	public void onParentEdit() {
		switchToEditMode();		
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForMedecins() {
		addMed.setDisabled(!drawActionOnOneButton("Collaborateur", 
											"Consultation"));
		
		// List<String> entites = new ArrayList<String>();
		// entites.add("Collaborateur");
		// setDroitsConsultation(drawConsultationLinks(entites));
		
		// si pas le droit d'accès aux dérivés, on cache le lien
		if (!getDroitsConsultation().get("Collaborateur")) {
			medecinLabel.setSclass(null);
		} else {
			medecinLabel.setSclass("formLink");
		}
	}
}
