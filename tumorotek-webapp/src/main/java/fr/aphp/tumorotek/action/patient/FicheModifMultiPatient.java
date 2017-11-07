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
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.contexte.ContexteConstraints;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.modification.multiple.SimpleChampValue;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant la fiche de modification multiple d'un patient.
 * Controller créé le 02/08/2010.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class FicheModifMultiPatient extends AbstractFicheModifMultiController {

	private static final long serialVersionUID = 4384639895874573764L;
	
	private Label nipLabelChanged;
	private Label nomLabelChanged;
	private Label nomNaissanceLabelChanged;
	private Label prenomLabelChanged;
	private Label sexeLabelChanged;
	private Label dateNaissanceLabelChanged;
	private Label paysNaissanceLabelChanged;
	private Label villeNaissanceLabelChanged;
	private Label etatLabelChanged;
	private Label dateEtatLabelChanged;
	private Label dateDecesLabelChanged;
	
	private Row dateEtatRow;
	private Row dateDecesRow;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	@Override
	public void setNewObject() {
		setBaseObject(new Patient());	
	}
	
	@Override
	public Patient getObject() {
		return (Patient) super.getObject();
	}
	
	@Override
	public AbstractObjectTabController getObjectTabController() {
		return ((PatientController) self.getParent().getParent()
				.getParent()
				.getFellow("winPatient")
				.getAttributeOrFellow("winPatient$composer", true));
	}
	
	@Override
	public void updateLabelChanged(String champ, String printValue, 
														boolean reset) {
		if ("nip".equals(champ)) {
			nipLabelChanged.setValue(printValue);
			nipLabelChanged.setVisible(!reset);
		} else if ("nom".equals(champ)) {
			nomLabelChanged.setValue(printValue);
			nomLabelChanged.setVisible(!reset);
		} else if ("nomNaissance".equals(champ)) {
			nomNaissanceLabelChanged.setValue(printValue);
			nomNaissanceLabelChanged.setVisible(!reset);
		} else if ("prenom".equals(champ)) {
			prenomLabelChanged.setValue(printValue);
			prenomLabelChanged.setVisible(!reset);
		} else if ("sexe".equals(champ)) {
			sexeLabelChanged.setValue(printValue);
			sexeLabelChanged.setVisible(!reset);
		} else if ("dateNaissance".equals(champ)) {
			dateNaissanceLabelChanged.setValue(printValue);
			dateNaissanceLabelChanged.setVisible(!reset);
		} else if ("paysNaissance".equals(champ)) {
			paysNaissanceLabelChanged.setValue(printValue);
			paysNaissanceLabelChanged.setVisible(!reset);
		} else if ("villeNaissance".equals(champ)) {
			villeNaissanceLabelChanged.setValue(printValue);
			villeNaissanceLabelChanged.setVisible(!reset);
		} else if ("patientEtat".equals(champ)) {
			etatLabelChanged.setValue(printValue);
			etatLabelChanged.setVisible(!reset);
		} else if ("dateEtat".equals(champ)) {
			dateEtatLabelChanged.setValue(printValue);
			dateEtatLabelChanged.setVisible(!reset);
		} else if ("dateDeces".equals(champ)) {
			dateDecesLabelChanged.setValue(printValue);
			dateDecesLabelChanged.setVisible(!reset);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateMultiObjects() {

		List<Patient> clones = new ArrayList<Patient>();
		
		boolean hasAnyChange = false;
		for (int i = 0; i < getObjsToEdit().size(); i++) {
			Patient current = ((Patient) getObjsToEdit().get(i)).clone();
			
			// maj du nip
			if (!nipLabelChanged.getValue().equals("")) {
				current.setNip(getObject().getNip());
				hasAnyChange = true;
			}
			
			// maj du nom
			if (!nomLabelChanged.getValue().equals("")) {
				current.setNom(getObject().getNom());
				hasAnyChange = true;
			}
			
			// maj nomNaissance
			if (!nomNaissanceLabelChanged.getValue().equals("")) {
				current.setNomNaissance(getObject().getNomNaissance());
				hasAnyChange = true;
			}
			
			// maj du prenom
			if (!prenomLabelChanged.getValue().equals("")) {
				current.setPrenom(getObject().getPrenom());
				hasAnyChange = true;
			}
			
			// maj du sexe
			if (!sexeLabelChanged.getValue().equals("")) {
				current.setSexe(getObject().getSexe());
				hasAnyChange = true;
			}
			
			// maj dateNaissance
			if (!dateNaissanceLabelChanged.getValue().equals("")) {
				current.setDateNaissance(getObject().getDateNaissance());
				hasAnyChange = true;
			}
			
			// maj du pays
			if (!paysNaissanceLabelChanged.getValue().equals("")) {
				current.setPaysNaissance(getObject().getPaysNaissance());
				hasAnyChange = true;
			}
			
			// maj de la ville
			if (!villeNaissanceLabelChanged.getValue().equals("")) {
				current.setVilleNaissance(getObject().getVilleNaissance());
				hasAnyChange = true;
			}
			
			// maj etat
			if (!etatLabelChanged.getValue().equals("")) {
				current.setPatientEtat(getObject().getPatientEtat());
				hasAnyChange = true;
			}
			
			// maj dateEtat
			if (!dateEtatLabelChanged.getValue().equals("")) {
				current.setDateEtat(getObject().getDateEtat());
				hasAnyChange = true;
			}
			
			// maj dateDeces
			if (!dateDecesLabelChanged.getValue().equals("")) {
				current.setDateDeces(getObject().getDateDeces());
				hasAnyChange = true;
			}
			
			// sort de la boucle de suite si pas chgts
			if (!hasAnyChange) {
				break;
			}			
			clones.add(current);
		}
		
		hasAnyChange = updateMultiAnnotationValeurs() || hasAnyChange; 
		
		if (hasAnyChange) {
			ManagerLocator.getPatientManager()
				.updateMultipleObjectsManager(clones, 
							(List<Patient>) getObjsToEdit(),
							getObjectTabController().getFicheAnnotation()
												.getValeursToCreateOrUpdate(), 
							getObjectTabController().getFicheAnnotation()
												.getValeursToDelete(),
									SessionUtils.getLoggedUser(sessionScope),
									SessionUtils.getSystemBaseDir());
			
			getObjectTabController()
								.getFicheAnnotation().clearValeursLists(true);
			
			// si aucune exception
			if (!clones.isEmpty()) {
				setObjsToEdit(clones);
			}
		}
	}	
	
	/*************************************************************************/	
	/************************** CHAMPS ***************************************/	
	/*************************************************************************/	
	
	public void onClick$nipMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				getObjsToEdit(), 
				"Champ.Patient.Nip", 
				"nip",
				null,
				null,
				null, 
				PatientConstraints.getCodeNullConstraint(),
				false,
				false, 
				null);
	}
	
	public void onClick$nomMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				getObjsToEdit(), 
				"Champ.Patient.Nom", 
				"nom",
				null,
				null,
				null, 
				PatientConstraints.getNomConstraint(),
				false,
				true, 
				null);
	}
	
	public void onClick$nomNaissanceMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				getObjsToEdit(), 
				"Champ.Patient.NomNaissance", 
				"nomNaissance",
				null,
				null,
				null, 
				PatientConstraints.getNomNullConstraint(),
				false,
				true, 
				null);
	}
	
	public void onClick$prenomMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				getObjsToEdit(), 
				"Champ.Patient.Prenom", 
				"prenom",
				null,
				null,
				null, 
				PatientConstraints.getNomNullConstraint(),
				false,
				true, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$sexeMultiLabel() {	
		
		List< ? extends Object> sexes = PatientUtils.getSexes();
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.Patient.Sexe", 
				"sexe",
				(List<Object>) sexes,
				"label",
				null, 
				null,
				false,
				null, 
				true);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$dateNaissanceMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Datebox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Patient.DateNaissance", 
				"dateNaissance",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$paysNaissanceMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Patient.PaysNaissance", 
				"paysNaissance",
				null,
				null,
				null, 
				ContexteConstraints.getVillePaysConstraint(),
				false,
				true, 
				null);
	}
	
	/*** ville Naissance. ***/
	@SuppressWarnings("unchecked")
	public void onClick$villeNaissanceMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				(List<Object>) getObjsToEdit(),
				"Champ.Patient.VilleNaissance", 
				"villeNaissance",
				null,
				null,
				null, 
				ContexteConstraints.getVillePaysConstraint(),
				false,
				true, 
				null);
	}
	
	/*** Etat. ***/	
	@SuppressWarnings("unchecked")
	public void onClick$etatMultiLabel() {	
		
		dateDecesRow.setVisible(true);
		dateEtatRow.setVisible(true);
		
		List< ? extends Object> etats = PatientUtils.getEtats();
		if ("F".equals(getObject().getSexe())) {
			etats = PatientUtils.getEtatsF();
		}
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Patient.PatientEtat", 
				"patientEtat",
				(List<Object>) etats,
				"label",
				null,
				null,
				false,
				null, 
				true);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$dateEtatMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Datebox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Patient.DateEtat", 
				"dateEtat",
				null,
				null,
				null, 
				null,
				false,
				null,
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$dateDecesMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Datebox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Patient.DateDeces", 
				"dateDeces",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}
	
	/**
	 * Surcharge la méthode pour court-circuiter la reception de 
	 * l'event te gérer les modifications sur le sexe et l'état. 
	 * Si l'etat change alors nullify date Etat ou date Deces.
	 */
	@Override
	public void onGetChangeOnChamp(Event e) {
		
		SimpleChampValue tmp = (SimpleChampValue) e.getData();
		
		if (tmp.getValue() instanceof LabelCodeItem) {
			
			// nullifier event
			if ("patientEtat".equals(tmp.getChamp())) {
				SimpleChampValue nullifier = new SimpleChampValue();
				((SimpleChampValue) nullifier).setValue(null);
				if (!((LabelCodeItem) tmp.getValue())
						.getCode().equals("D")) { // nullify dateDeces
					nullifier.setChamp("dateDeces");
					dateDecesRow.setVisible(false);
					dateEtatRow.setVisible(true);
				} else { // nullify dateEtat
					nullifier.setChamp("dateEtat");
					dateEtatRow.setVisible(false);
					dateDecesRow.setVisible(true);
				}
				Events.postEvent(new Event("onGetChangeOnChamp", 
										self, nullifier));
			}
			
			// transforme leLabelCodeItem en sa valeur
			tmp.setValue(((LabelCodeItem) tmp.getValue()).getCode()); 
			
			Events.postEvent(new Event("onGetChangeOnChamp", self, tmp));
			
		} else {		
			super.onGetChangeOnChamp(e);
		}
	}

	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public void setParentObject(TKdataObject obj) {		
	}
}
