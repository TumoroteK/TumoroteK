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

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.modification.multiple.ConformitePack;
import fr.aphp.tumorotek.action.modification.multiple.SimpleChampValue;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant la fiche de modification multiple de prélèvements.
 * Controller créé le 20/02/2011.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class FicheModifMultiPrelevement extends 
								AbstractFicheModifMultiController {

	private static final long serialVersionUID = 4384639895874573764L;
	
	private Label numeroLaboLabelChanged;
	private Label natureLabelChanged;
	private Label datePrelevementLabelChanged;
	private Label prelevementTypeLabelChanged;
	private Label sterileLabelChanged;
	private Label risqueLabelChanged;
	private Label serviceLabelChanged;
	private Label preleveurLabelChanged;
	private Label conditTypeLabelChanged;
	private Label conditNbLabelChanged;
	private Label conditMilieuLabelChanged;
	private Label consentTypeLabelChanged;
	private Label consentDateLabelChanged;
	private Label dateDepartLabelChanged;
	private Label transporteurLabelChanged;
	private Label transportTempLabelChanged;
	private Label dateArriveeLabelChanged;
	private Label operateurLabelChanged;
	private Label quantiteLabelChanged;
	private Label nonConformeLabelChanged;
	
	private List<NonConformite> nonConformitesArrivee = null;

	private boolean cascadeNonSterile;
	
	public boolean isCascadeNonSterile() {
		return cascadeNonSterile;
	}

	public void setCascadeNonSterile(boolean cS) {
		this.cascadeNonSterile = cS;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	@Override
	public void setNewObject() {
		setBaseObject(new Prelevement());	
	}
	
	@Override
	public Prelevement getObject() {
		return (Prelevement) super.getObject();
	}
	
	@Override
	public AbstractObjectTabController getObjectTabController() {
		return ((PrelevementController) self.getParent().getParent()
				.getParent()
				.getFellow("winPrelevement")
				.getAttributeOrFellow("winPrelevement$composer", true));
	}
	
	@Override
	public void updateLabelChanged(String champ, String printValue, 
														boolean reset) {
		if ("numeroLabo".equals(champ)) {
			numeroLaboLabelChanged.setValue(printValue);
			numeroLaboLabelChanged.setVisible(!reset);
		} else if ("nature".equals(champ)) {
			natureLabelChanged.setValue(printValue);
			natureLabelChanged.setVisible(!reset);
		} else if ("datePrelevement".equals(champ)) {
			datePrelevementLabelChanged.setValue(printValue);
			datePrelevementLabelChanged.setVisible(!reset);
		} else if ("prelevementType".equals(champ)) {
			prelevementTypeLabelChanged.setValue(printValue);
			prelevementTypeLabelChanged.setVisible(!reset);
		} else if ("sterile".equals(champ)) {
			sterileLabelChanged.setValue(printValue);
			sterileLabelChanged.setVisible(!reset);
		} else if ("risques".equals(champ)) {
			risqueLabelChanged.setValue(printValue);
			risqueLabelChanged.setVisible(!reset);
		} else if ("servicePreleveur".equals(champ)) {
			serviceLabelChanged.setValue(printValue);
			serviceLabelChanged.setVisible(!reset);
		} else if ("preleveur".equals(champ)) {
			preleveurLabelChanged.setValue(printValue);
			preleveurLabelChanged.setVisible(!reset);
		} else if ("conditType".equals(champ)) {
			conditTypeLabelChanged.setValue(printValue);
			conditTypeLabelChanged.setVisible(!reset);
		} else if ("conditNbr".equals(champ)) {
			conditNbLabelChanged.setValue(printValue);
			conditNbLabelChanged.setVisible(!reset);
		} else if ("conditMilieu".equals(champ)) {
			conditMilieuLabelChanged.setValue(printValue);
			conditMilieuLabelChanged.setVisible(!reset);
		} else if ("consentType".equals(champ)) {
			consentTypeLabelChanged.setValue(printValue);
			consentTypeLabelChanged.setVisible(!reset);
		} else if ("consentDate".equals(champ)) {
			consentDateLabelChanged.setValue(printValue);
			consentDateLabelChanged.setVisible(!reset);
		} else if ("dateDepart".equals(champ)) {
			dateDepartLabelChanged.setValue(printValue);
			dateDepartLabelChanged.setVisible(!reset);
		} else if ("transporteur".equals(champ)) {
			transporteurLabelChanged.setValue(printValue);
			transporteurLabelChanged.setVisible(!reset);
		} else if ("transportTemp".equals(champ)) {
			transportTempLabelChanged.setValue(printValue);
			transportTempLabelChanged.setVisible(!reset);
		} else if ("dateArrivee".equals(champ)) {
			dateArriveeLabelChanged.setValue(printValue);
			dateArriveeLabelChanged.setVisible(!reset);
		} else if ("operateur".equals(champ)) {
			operateurLabelChanged.setValue(printValue);
			operateurLabelChanged.setVisible(!reset);
		} else if ("quantite".equals(champ)) {
			quantiteLabelChanged.setValue(printValue);
			quantiteLabelChanged.setVisible(!reset);
		} else if ("conformeArrivee".equals(champ)) {
			nonConformeLabelChanged.setValue(printValue);
			nonConformeLabelChanged.setVisible(!reset);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateMultiObjects() {

		List<Prelevement> clones = new ArrayList<Prelevement>();
		
		boolean hasAnyChange = false;
		for (int i = 0; i < getObjsToEdit().size(); i++) {
			Prelevement current = ((Prelevement) 
								getObjsToEdit().get(i)).clone();
			
			// maj du numero labo
			if (!numeroLaboLabelChanged.getValue().equals("")) {
				current.setNumeroLabo(getObject().getNumeroLabo());
				hasAnyChange = true;
			}
			
			// maj de nature
			if (!natureLabelChanged.getValue().equals("")) {
				current.setNature(getObject().getNature());
				hasAnyChange = true;
			}
			
			// datePrelevement
			if (!datePrelevementLabelChanged.getValue().equals("")) {
				current.setDatePrelevement(getObject().getDatePrelevement());
				hasAnyChange = true;
			}
			
			// prelevementType
			if (!prelevementTypeLabelChanged.getValue().equals("")) {
				current.setPrelevementType(getObject().getPrelevementType());
				hasAnyChange = true;
			}
			
			// sterile
			if (!sterileLabelChanged.getValue().equals("")) {
				current.setSterile(getObject().getSterile());
				hasAnyChange = true;
			}
			
			// risques
			if (!risqueLabelChanged.getValue().equals("")) {
				current.getRisques().clear();
				if (getObject().getRisques() != null) {
					current.getRisques().addAll(getObject().getRisques());
				}
				hasAnyChange = true;
			}
			
			// service
			if (!serviceLabelChanged.getValue().equals("")) {
				current.setServicePreleveur(getObject().getServicePreleveur());
				hasAnyChange = true;
			}
			
			// preleveur
			if (!preleveurLabelChanged.getValue().equals("")) {
				current.setPreleveur(getObject().getPreleveur());
				hasAnyChange = true;
			}
			
			// conditType
			if (!conditTypeLabelChanged.getValue().equals("")) {
				current.setConditType(getObject().getConditType());
				hasAnyChange = true;
			}
			
			// conditNbr
			if (!conditNbLabelChanged.getValue().equals("")) {
				current.setConditNbr(getObject().getConditNbr());
				hasAnyChange = true;
			}
			
			// conditMilieu
			if (!conditMilieuLabelChanged.getValue().equals("")) {
				current.setConditMilieu(getObject().getConditMilieu());
				hasAnyChange = true;
			}
			
			// consentType
			if (!consentTypeLabelChanged.getValue().equals("")) {
				current.setConsentType(getObject().getConsentType());
				hasAnyChange = true;
			}
			
			// consentDate
			if (!consentDateLabelChanged.getValue().equals("")) {
				current.setConsentDate(getObject().getConsentDate());
				hasAnyChange = true;
			}
			
			// dateDepart
			if (!dateDepartLabelChanged.getValue().equals("")) {
				current.setDateDepart(getObject().getDateDepart());
				hasAnyChange = true;
			}
			
			// transporteur
			if (!transporteurLabelChanged.getValue().equals("")) {
				current.setTransporteur(getObject().getTransporteur());
				hasAnyChange = true;
			}
			
			// transportTemp
			if (!transportTempLabelChanged.getValue().equals("")) {
				current.setTransportTemp(getObject().getTransportTemp());
				hasAnyChange = true;
			}
			
			// dateArrivee
			if (!dateArriveeLabelChanged.getValue().equals("")) {
				current.setDateArrivee(getObject().getDateArrivee());
				hasAnyChange = true;
			}
			
			// operateur
			if (!operateurLabelChanged.getValue().equals("")) {
				current.setOperateur(getObject().getOperateur());
				hasAnyChange = true;
			}
			
			// quantite
			if (!quantiteLabelChanged.getValue().equals("")) {
				current.setQuantite(getObject().getQuantite());
				current.setQuantiteUnite(getObject().getQuantiteUnite());
				hasAnyChange = true;
			}
			
			// conforme Arrivee
			if (!nonConformeLabelChanged.getValue().equals("")) {
				current.setConformeArrivee(getObject().getConformeArrivee());
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
			ManagerLocator.getPrelevementManager()
				.updateMultipleObjectsManager(clones, 
						(List<Prelevement>) getObjsToEdit(), 
						getObjectTabController()
						.getFicheAnnotation().getValeursToCreateOrUpdate(), 
					getObjectTabController()
						.getFicheAnnotation().getValeursToDelete(), 
					nonConformitesArrivee, cascadeNonSterile, 
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
	
	public void onClick$numeroLaboMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.NumeroLabo", 
				"numeroLabo",
				null,
				null,
				null, 
				PrelevementConstraints.getCodeNullConstraint(),
				false,
				false, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$natureMultiLabel() {					
		List< ? extends Object> natures = ManagerLocator
			.getNatureManager()
				.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.Nature", 
				"nature",
				(List<Object>) natures,
				"nature",
				null, 
				null,
				false,
				null, 
				true);
	}
	
	public void onClick$datePrelevementMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Calendarbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.DatePrelevement", 
				"datePrelevement",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$prelevementTypeMultiLabel() {		
		List< ? extends Object> pTypes = ManagerLocator
		.getPrelevementTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.PrelevementType", 
				"prelevementType",
				(List<Object>) pTypes,
				"type",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$sterileMultiLabel() {	
		
		List< ? extends Object> bools;
		List<Boolean> bools2 = new ArrayList<Boolean>();
		bools2.add(new Boolean(true));
		bools2.add(new Boolean(false));
		
		bools = bools2;
				
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"general.sterile", 
				"sterile",
				(List<Object>) bools,
				"bool",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$risqueMultiLabel() {	
		
		List< ? extends Object> risques = 
			ManagerLocator.getRisqueManager()
				.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		for (int i = 0; i < getObjsToEdit().size(); i++) {
			((Prelevement) getObjsToEdit().get(i))
				.setRisques(ManagerLocator.getPrelevementManager()
					.getRisquesManager((Prelevement) getObjsToEdit().get(i)));
		}
				
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"MultiListbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.Risque", 
				"risques",
				(List<Object>) risques,
				"nom",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$serviceMultiLabel() {	
		
		List< ? extends Object> services = 
			ManagerLocator.getServiceManager()
				.findAllActiveObjectsWithOrderManager();
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Combobox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Prelevement.ServicePreleveur", 
				"servicePreleveur",
				(List<Object>) services,
				"nom",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$preleveurMultiLabel() {	
		
		List< ? extends Object> collaborateurs = 
			ManagerLocator.getCollaborateurManager()
					.findAllActiveObjectsWithOrderManager();
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Combobox", 
				(List<Object>) getObjsToEdit(), 
				"Champ.Prelevement.Preleveur", 
				"preleveur",
				(List<Object>) collaborateurs,
				"nomAndPrenom",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$conditTypeMultiLabel() {		
		List< ? extends Object> cTypes = ManagerLocator
		.getConditTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.ConditType", 
				"conditType",
				(List<Object>) cTypes,
				"type",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	public void onClick$conditNbMultiLabel() {	
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Intbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.ConditNbr", 
				"conditNbr",
				null,
				null,
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$conditMilieuMultiLabel() {		
		List< ? extends Object> milieux = ManagerLocator
		.getConditMilieuManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.ConditMilieu", 
				"conditMilieu",
				(List<Object>) milieux,
				"milieu",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$consentTypeMultiLabel() {					
		List< ? extends Object> cTypes = ManagerLocator
			.getConsentTypeManager()
				.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.ConsentType", 
				"consentType",
				(List<Object>) cTypes,
				"type",
				null, 
				null,
				false,
				null, 
				true);
	}
	
	public void onClick$consentDateMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Datebox", 
				getObjsToEdit(), 
				"Champ.Prelevement.ConsentDate", 
				"consentDate",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}
	
	public void onClick$dateDepartMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Calendarbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.DateDepart", 
				"dateDepart",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$transporteurMultiLabel() {		
		List< ? extends Object> trsps = ManagerLocator.getTransporteurManager()
														.findAllActiveManager();
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Combobox", 
				getObjsToEdit(), 
				"Champ.Prelevement.Transporteur", 
				"transporteur",
				(List<Object>) trsps,
				"nom",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	public void onClick$transportTempMultiLabel() {	
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Floatbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.TransportTemp", 
				"transportTemp",
				null,
				null,
				null, 
				null,
				false,
				null, 
				false);
	}
	
	public void onClick$dateArriveeMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Calendarbox", 
				getObjsToEdit(), 
				"Champ.Prelevement.DateArrivee", 
				"dateArrivee",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$operateurMultiLabel() {		
		List< ? extends Object> ops = ManagerLocator.getCollaborateurManager()
		.findAllActiveObjectsWithOrderManager();
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Combobox", 
				getObjsToEdit(), 
				"Champ.Prelevement.Operateur", 
				"operateur",
				(List<Object>) ops,
				"nomAndPrenom",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$quantiteMultiLabel() {	
		
		List<Unite> quantiteUnites = ManagerLocator.getUniteManager()
			.findByTypeLikeManager("masse", true);
		quantiteUnites.addAll(ManagerLocator.getUniteManager()
			.findByTypeLikeManager("discret", true));
		quantiteUnites.addAll(ManagerLocator.getUniteManager()
			.findByTypeLikeManager("volume", true));
		
		List < ? extends Object> units = quantiteUnites;
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Quantification", 
				getObjsToEdit(), 
				"Champ.Prelevement.Quantite", 
				"quantite",
				(List<Object>) units,
				"unite",
				null, 
				null,
				false,
				null,
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$nonConformeMultiLabel() {	
		
		List <? extends Object> nonConfs = 
			ManagerLocator.getNonConformiteManager()
			.findByPlateformeEntiteAndTypeStringManager(
					SessionUtils.getPlateforme(sessionScope), 
					"Arrivee", getObjectTabController().getEntiteTab());
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Conformitebox", 
				getObjsToEdit(), 
				"Champ.Prelevement.ConformeArrivee", 
				"conformeArrivee",
				(List<Object>) nonConfs,
				"Arrivee",
				null, 
				null,
				false,
				null, 
				false);
	}

	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public void setParentObject(TKdataObject obj) {		
	}
	
	/**
	 * Surcharge la méthode pour court-circuiter la reception de 
	 * l'event te gérer les modifications sur la conformite.
	 */
	@Override
	public void onGetChangeOnChamp(Event e) {
		
		SimpleChampValue tmp = (SimpleChampValue) e.getData();
	
		if (tmp.getValue() instanceof ConformitePack) {	 
			if (nonConformitesArrivee != null) {
				nonConformitesArrivee.clear();
			} else {
				nonConformitesArrivee = new ArrayList<NonConformite>();
			}
			ConformitePack pck = (ConformitePack) tmp.getValue();
			if (pck.getNonConforme() != null) {
				if (pck.getNonConforme()) {
					getObject().setConformeArrivee(false);
					nonConformitesArrivee.addAll(pck.getNonConformites());
				} else if (!pck.getNonConforme()) {
					getObject().setConformeArrivee(true);
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			if (tmp.getPrintValue() != null) {
				sb.append(tmp.getPrintValue());
			} else {
				sb.append(" ");
			}
			sb.append("]");
			
			updateLabelChanged(tmp.getChamp(), sb.toString(), false);
		} else {	
			super.onGetChangeOnChamp(e);
		}
	}

	public void onResetMulti(Event event) {
		SimpleChampValue tmp = (SimpleChampValue) event.getData();
		
		if ("nonConformeArrivee".equals(tmp.getChamp())) {
			nonConformitesArrivee = null;
			updateLabelChanged(tmp.getChamp(), "", true);
			
		} else {	
			super.onResetMulti(event);
		}
	}
}
