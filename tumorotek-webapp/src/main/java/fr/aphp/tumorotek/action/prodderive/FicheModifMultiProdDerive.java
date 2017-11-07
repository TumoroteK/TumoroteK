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
package fr.aphp.tumorotek.action.prodderive;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.modification.multiple.ConformitePack;
import fr.aphp.tumorotek.action.modification.multiple.SimpleChampValue;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant la fiche de modification multiple d'un produit dérivé.
 * Controller créé le 06/05/2010.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class FicheModifMultiProdDerive extends 
									AbstractFicheModifMultiController {
	
	private static final long serialVersionUID = 4384639895874573764L;

	private Label codeLaboLabelChanged;
	private Label typeLabelChanged;
	private Label qualiteLabelChanged;
	private Label collaborateurLabelChanged;
	private Label dateStockageLabelChanged;
	private Label volumeLabelChanged;
	private Label concentrationLabelChanged;
	private Label quantiteLabelChanged;
	private Label nonConformeTraitementLabelChanged;
	private Label nonConformeCessionLabelChanged;
	
	private List<NonConformite> nonConformitesTraitement = null;
	
	private List<NonConformite> nonConformitesCession = null;

	@Override
	public void setNewObject() {
		setBaseObject(new ProdDerive());	
	}
	
	@Override
	public ProdDerive getObject() {
		return (ProdDerive) super.getObject();
	}
	
	@Override
	public AbstractObjectTabController getObjectTabController() {
		return ((ProdDeriveController) self.getParent().getParent()
				.getParent()
				.getFellow("winProdDerive")
				.getAttributeOrFellow("winProdDerive$composer", true));
	}
	
	@Override
	public void updateLabelChanged(String champ, String printValue, 
														boolean reset) {
		if ("codeLabo".equals(champ)) {
			codeLaboLabelChanged.setValue(printValue);
			codeLaboLabelChanged.setVisible(!reset);
		} else if ("prodType".equals(champ)) {
			typeLabelChanged.setValue(printValue);
			typeLabelChanged.setVisible(!reset);
		} else if ("dateStock".equals(champ)) {
			dateStockageLabelChanged.setValue(printValue);
			dateStockageLabelChanged.setVisible(!reset);
		} else if ("collaborateur".equals(champ)) {
			collaborateurLabelChanged.setValue(printValue);
			collaborateurLabelChanged.setVisible(!reset);
		} else if ("prodQualite".equals(champ)) {
			qualiteLabelChanged.setValue(printValue);
			qualiteLabelChanged.setVisible(!reset);
		} else if ("volumeInit".equals(champ)) {
			volumeLabelChanged.setValue(printValue);
			volumeLabelChanged.setVisible(!reset);
		} else if ("quantiteInit".equals(champ)) {
			quantiteLabelChanged.setValue(printValue);
			quantiteLabelChanged.setVisible(!reset);
		} else if ("conc".equals(champ)) {
			concentrationLabelChanged.setValue(printValue);
			concentrationLabelChanged.setVisible(!reset);
		} else if ("conformeTraitement".equals(champ)) {
			nonConformeTraitementLabelChanged.setValue(printValue);
			nonConformeTraitementLabelChanged.setVisible(!reset);
		} else if ("conformeCession".equals(champ)) {
			nonConformeCessionLabelChanged.setValue(printValue);
			nonConformeCessionLabelChanged.setVisible(!reset);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateMultiObjects() {
		
		List<ProdDerive> clones = new ArrayList<ProdDerive>();
		
		boolean hasAnyChange = false;
		for (int i = 0; i < getObjsToEdit().size(); i++) {
			ProdDerive current = ((ProdDerive) 
								getObjsToEdit().get(i)).clone();
			
			// maj du type
			if (!typeLabelChanged.getValue().equals("")) {
				current.setProdType(getObject().getProdType());
				hasAnyChange = true;
			}
						
			// maj du code labo 
			if (!codeLaboLabelChanged.getValue().equals("")) {
				current.setCodeLabo(getObject().getCodeLabo());
				hasAnyChange = true;
			}
						
			// maj du volume
			if (!volumeLabelChanged.getValue().equals("")) {
				current.setVolume(getObject().getVolumeInit());
				current.setVolumeInit(getObject().getVolumeInit());
				current.setVolumeUnite(getObject().getVolumeUnite());
				hasAnyChange = true;
			}
						
			// maj de la concentration
			if (!concentrationLabelChanged.getValue().equals("")) {
				current.setConc(getObject().getConc());
				current.setConcUnite(getObject().getConcUnite());
				hasAnyChange = true;
			}
						
			// maj de la quantité
			if (!quantiteLabelChanged.getValue().equals("")) {
				current.setQuantite(getObject().getQuantiteInit());
				current.setQuantiteInit(getObject().getQuantiteInit());
				current.setQuantiteUnite(getObject().getQuantiteUnite());
				hasAnyChange = true;
			}
						
			// maj date stockage
			if (!dateStockageLabelChanged.getValue().equals("")) {
				current.setDateStock(getObject().getDateStock());
				hasAnyChange = true;
			}
						
			// maj qualité
			if (!qualiteLabelChanged.getValue().equals("")) {
				current.setProdQualite(getObject().getProdQualite());
				hasAnyChange = true;
			}
						
			// maj opérateur
			if (!collaborateurLabelChanged.getValue().equals("")) {
				current.setCollaborateur(getObject().getCollaborateur());
				hasAnyChange = true;
			}
			
			// conforme Traitement
			if (!nonConformeTraitementLabelChanged.getValue().equals("")) {
				current.setConformeTraitement(getObject().getConformeTraitement());
				hasAnyChange = true;
			}
			
			// conforme Cession
			if (!nonConformeCessionLabelChanged.getValue().equals("")) {
				current.setConformeCession(getObject().getConformeCession());
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
			ManagerLocator.getProdDeriveManager()
				.updateMultipleObjectsManager(clones, 
						(List<ProdDerive>) getObjsToEdit(),
						getObjectTabController()
						.getFicheAnnotation().getValeursToCreateOrUpdate(), 
						getObjectTabController().getFicheAnnotation()
													.getValeursToDelete(), 
							nonConformitesTraitement, nonConformitesCession, 
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
	public void onClick$codeLaboMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Textbox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.codeLabo", 
				"codeLabo",
				null,
				null,
				null, 
				ProdDeriveConstraints.getCodeNullConstraint(),
				false,
				false, 
				null);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$typeMultiLabel() {					
		List< ? extends Object> types = ManagerLocator
			.getProdTypeManager()
				.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.ProdType.Type", 
				"prodType",
				(List<Object>) types,
				"type",
				null, 
				null,
				false,
				null, 
				true);
	}
	
	public void onClick$dateStockageMultiLabel() {			
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Calendarbox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.DateStock", 
				"dateStock",
				null,
				null,
				null, 
				null,
				false,
				null, 
				null);
	}


	@SuppressWarnings("unchecked")
	public void onClick$collaborateurMultiLabel() {		
		List< ? extends Object> ops = ManagerLocator.getCollaborateurManager()
						.findAllActiveObjectsWithOrderManager();
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Combobox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.Collaborateur", 
				"collaborateur",
				(List<Object>) ops,
				"nomAndPrenom",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$qualiteMultiLabel() {		
		List< ? extends Object> quals = ManagerLocator
			.getProdQualiteManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Listbox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.ProdQualite", 
				"prodQualite",
				(List<Object>) quals,
				"prodQualite",
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
		
		List < ? extends Object> units = quantiteUnites;
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Quantification", 
				getObjsToEdit(), 
				"Champ.ProdDerive.QuantiteInit", 
				"quantiteInit",
				(List<Object>) units,
				"unite",
				null, 
				null,
				false,
				null,
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$volumeMultiLabel() {	
		
		List<Unite> quantiteUnites = ManagerLocator.getUniteManager()
								.findByTypeLikeManager("volume", true);
		
		List < ? extends Object> units = quantiteUnites;
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Quantification", 
				getObjsToEdit(), 
				"Champ.ProdDerive.VolumeInit", 
				"volumeInit",
				(List<Object>) units,
				"unite",
				null, 
				null,
				false,
				null,
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$concentrationMultiLabel() {	
		
		List<Unite> quantiteUnites = ManagerLocator.getUniteManager()
						.findByTypeLikeManager("concentration", true);
		
		List < ? extends Object> units = quantiteUnites;
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Quantification", 
				getObjsToEdit(), 
				"Champ.ProdDerive.Conc", 
				"conc",
				(List<Object>) units,
				"unite",
				null, 
				null,
				false,
				null,
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$nonConformeTraitementMultiLabel() {	
		
		List <? extends Object> nonConfs = 
			ManagerLocator.getNonConformiteManager()
			.findByPlateformeEntiteAndTypeStringManager(
					SessionUtils.getPlateforme(sessionScope), 
					"Traitement", getObjectTabController().getEntiteTab());
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Conformitebox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.ConformeTraitement", 
				"conformeTraitement",
				(List<Object>) nonConfs,
				"Traitement",
				null, 
				null,
				false,
				null, 
				false);
	}
	
	@SuppressWarnings("unchecked")
	public void onClick$nonConformeCessionMultiLabel() {	
		
		List <? extends Object> nonConfs = 
			ManagerLocator.getNonConformiteManager()
			.findByPlateformeEntiteAndTypeStringManager(
					SessionUtils.getPlateforme(sessionScope), 
					"Cession", getObjectTabController().getEntiteTab());
		
		openModificationMultipleWindow(page, 
				Path.getPath(self),
				"onGetChangeOnChamp",
				"Conformitebox", 
				getObjsToEdit(), 
				"Champ.ProdDerive.ConformeCession", 
				"conformeCession",
				(List<Object>) nonConfs,
				"Cession",
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
	 * l'event te gérer les modifications sur le sexe et l'état. 
	 * Si l'etat change alors nullify date Etat ou date Deces.
	 */
	@Override
	public void onGetChangeOnChamp(Event e) {
		
		SimpleChampValue tmp = (SimpleChampValue) e.getData();
		
		 if (tmp.getValue() instanceof ConformitePack) {	
			ConformitePack pck = (ConformitePack) tmp.getValue();
			if (tmp.getChamp().equals("conformeTraitement")) {
				if (nonConformitesTraitement != null) {
					nonConformitesTraitement.clear();
				} else {
					nonConformitesTraitement = new ArrayList<NonConformite>();
				}
				if (pck.getNonConforme() != null) {
					if (pck.getNonConforme()) {
						getObject().setConformeTraitement(false);
						nonConformitesTraitement.addAll(pck.getNonConformites());
					} else if (!pck.getNonConforme()) {
						getObject().setConformeTraitement(true);
					}
				}
			} else if (tmp.getChamp().equals("conformeCession")) {
				if (nonConformitesCession != null) {
					nonConformitesCession.clear();
				} else {
					nonConformitesCession = new ArrayList<NonConformite>();
				}
				if (pck.getNonConforme() != null) {
					if (pck.getNonConforme()) {
						getObject().setConformeCession(false);
						nonConformitesCession.addAll(pck.getNonConformites());
					} else if (!pck.getNonConforme()) {
						getObject().setConformeCession(true);
					}
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
	
	@Override
	public void onResetMulti(Event event) {
		SimpleChampValue tmp = (SimpleChampValue) event.getData();
		
		if ("conformeTraitement".equals(tmp.getChamp())) {
			nonConformitesTraitement = null;
			updateLabelChanged(tmp.getChamp(), "", true);		
		} else if ("conformeCession".equals(tmp.getChamp())) {
			nonConformitesCession = null;
			updateLabelChanged(tmp.getChamp(), "", true);					
		} else {	
			super.onResetMulti(event);
		}
	}
}
