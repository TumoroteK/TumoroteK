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

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Modale permettant d'informer l'utilisateur sur le déplacement 
 * d'un prélèvement d'une maladie à une autre, et de réaliser 
 * ce déplacement.
 * Date: 16/11/2011
 * 
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
public class ChangeMaladieModale extends GenericForwardComposer<Component> {

	private static final long serialVersionUID = 2132993101599271792L;
	
	private Label prelCodeLabel;
	private Label prelNumLabel;
	private Label prelNatureLabel;
	private Listbox maladiesBox;
	private Label patientNipLabel;
	private Label ndaLabel;
	private Label patientNomLabel;
	private Label patientPrenomLabel;
	private Label patientDdnLabel;
	private Label patientSexeLabel;
	private Label maladieLibelleLabel;
	private Label maladieCodeLabel;
	
	private Prelevement prelevement;
	private Maladie currentMaladie;
	private Patient patient;
	private List<Maladie> maladies;
	
	private MainWindow main;
	
	public void init(Prelevement prel, MainWindow mw) {
		prelevement = prel;
		main = mw;
		prelCodeLabel.setValue(prelevement.getCode());
		prelNumLabel.setValue(prelevement.getNumeroLabo());
		prelNatureLabel.setValue(prelevement.getNature().getNom());
		
		currentMaladie = prelevement.getMaladie();
		if (currentMaladie != null && currentMaladie.getPatient() != null) {
			maladieLibelleLabel.setValue(currentMaladie.getLibelle());
			maladieCodeLabel.setValue(currentMaladie.getCode());
			
			patient = currentMaladie.getPatient();
			patientNipLabel.setValue(patient.getNip());
			ndaLabel.setValue(prelevement.getPatientNda());
			patientNomLabel.setValue(patient.getNom());
			patientPrenomLabel.setValue(patient.getPrenom());
			patientDdnLabel.setValue(ObjectTypesFormatters.dateRenderer2(
					patient.getDateNaissance()));
			patientSexeLabel.setValue(patient.getSexe());
			
			// recherche des maladies disponibles : celles du patient
			// du prélèvement
			maladies = ManagerLocator.getMaladieManager()
				.findByPatientManager(currentMaladie.getPatient());
			maladies.remove(currentMaladie);
			maladiesBox.setModel(new SimpleListModel<Maladie>(maladies));
			if (!maladies.isEmpty()) {
				maladiesBox.setSelectedItem(maladiesBox.getItemAtIndex(0));
			}
		}
	}
	
	public void onClick$cancel() {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	public void onClick$validate() {
		Clients.showBusy(Labels
			.getLabel("fichePrelevement.switchMaladie.encours"));
		Events.echoEvent("onLaterUpdate", self, null);
	}
	
	public void onLaterUpdate() {
		Maladie dest = null;
		if (maladiesBox.getSelectedItem() != null) {
			dest = (Maladie) maladiesBox.getSelectedItem().getValue();
		} else {
			// ferme wait message
			Clients.clearBusy();
			throw new WrongValueException(maladiesBox, Labels
				.getLabel("fichePrelevement.switchMaladie" 
						+ ".maladiesCibles.error"));
		}
		
		ManagerLocator.getPrelevementManager()
			.switchMaladieManager(prelevement, dest, 
					SessionUtils.getLoggedUser(sessionScope));
		
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
		
		// MAJ des fiches et des listes
		getPrelevementController()
			.getListe()
			.updateObjectGridListFromOtherPage(prelevement, true);
		if (!getPrelevementController()
				.getReferencingObjectControllers().isEmpty()) {
			getPrelevementController()
				.getReferencingObjectControllers()
					.get(0).getListe()
					.updateObjectGridListFromOtherPage(prelevement
								.getMaladie().getPatient(), true);
		}
		
		// ferme wait message
		Clients.clearBusy();
	}
	
	public PrelevementController getPrelevementController() {
		return (PrelevementController) main.getMainTabbox().getTabpanels()
				.getFellow("prelevementPanel")
				.getFellow("winPrelevement")
				.getAttributeOrFellow("winPrelevement$composer", true);
	}

}
