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
import java.util.Collections;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Column;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.comparator.ProdDerivesNbCessionsComparator;
import fr.aphp.tumorotek.action.comparator.ProdDerivesNbDerivesComparator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant une liste de produits dérivés.
 * Controller créé le 02/11/2009.
 * 
 * Modifié le 15/04/2013 pour transfert fonction impression étiquettes 
 * au AbstractListController2
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class ListeProdDerive extends AbstractListeController2 {
	
	//private Log log = LogFactory.getLog(ListeProdDerive.class);
	
	private static final long serialVersionUID = -6167747099087709700L;
	
	private List<ProdDerive> listObjects = new ArrayList<ProdDerive>();
	private List<ProdDerive> selectedObjects = 
											new ArrayList<ProdDerive>();
	
	private Menuitem newCessionItem;
	private Menuitem stockageItem;
	
	// Critères de recherche.
	private Radio codeDerive;
	private Radio patientDerive;
	private Textbox codeBoxDerive;
	private Textbox patientBoxDerive;
	private Column nbProdDerivesColumn;
	private Column nbCessionsColumn;

	//Variables formulaire pour les critères.
	private String searchCode;
	private String searchPatientNom;
	
	private static ProdDeriveRowRenderer listObjectsRenderer 
							= new ProdDeriveRowRenderer(true, false);
	private ProdDerivesNbDerivesComparator comparatorDerivesAsc = 
		new ProdDerivesNbDerivesComparator(true);
	private ProdDerivesNbDerivesComparator comparatorDerivesDesc = 
		new ProdDerivesNbDerivesComparator(false);
	private ProdDerivesNbCessionsComparator comparatorCessionsAsc = 
		new ProdDerivesNbCessionsComparator(true);
	private ProdDerivesNbCessionsComparator comparatorCessionsDesc = 
		new ProdDerivesNbCessionsComparator(false);
	
	// Variable de droits.
	private boolean canCession;
	private boolean canStockage;
	
	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String search) {
		this.searchCode = search;
	}
	
	public String getSearchPatientNom() {
		return searchPatientNom;
	}

	public void setSearchPatientNom(String search) {
		this.searchPatientNom = search;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {	
		super.doAfterCompose(comp);
		
		setOnGetEventName("onGetDerivesFromSelection");
		
		nbProdDerivesColumn.setSortAscending(comparatorDerivesAsc);
		nbProdDerivesColumn.setSortDescending(comparatorDerivesDesc);
		nbCessionsColumn.setSortAscending(comparatorCessionsAsc);
		nbCessionsColumn.setSortDescending(comparatorCessionsDesc);
	}
	
	@Override
	public List<ProdDerive> getListObjects() {
		return listObjects;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setListObjects(List< ? extends TKdataObject> objs) {
		clearSelection();
		this.listObjects.clear();
		this.listObjects.addAll((List<ProdDerive>) objs);
	}
	
	@Override
	public void addToListObjects(TKdataObject obj, Integer pos) {
		if (obj != null) {
			if (pos != null) {
				getListObjects().add(pos.intValue(), (ProdDerive) obj);
			} else {
				getListObjects().add((ProdDerive) obj);
			}
		}
	}
	
	@Override
	public void removeObjectFromList(TKdataObject obj) {
		getListObjects().remove((ProdDerive) obj);
	}

	@Override
	public List<ProdDerive> getSelectedObjects() {
		return selectedObjects;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedObjects(List< ? extends TKdataObject> objs) {
		this.selectedObjects = (List<ProdDerive>) objs;
	}
	
	@Override
	public void addToSelectedObjects(TKdataObject obj) {
		if (!getSelectedObjects().contains(obj)) {
			getSelectedObjects().add((ProdDerive) obj);
		}
	}
	
	@Override
	public void removeFromSelectedObjects(TKdataObject obj) {
		if (getSelectedObjects().contains(obj)) {
			getSelectedObjects().remove((ProdDerive) obj);
		}
	}
	
	@Override
	public TKSelectObjectRenderer getListObjectsRenderer() {
		return listObjectsRenderer;
	}
	
	@Override
	public AbstractObjectTabController getObjectTabController() {
		return (ProdDeriveController) super.getObjectTabController();
	}
	
	@Override
	public void passSelectedToList() {
		getListObjects().clear();
		getListObjects().addAll(getSelectedObjects());		
	}
	
	@Override
	public void passListToSelected() {
		getSelectedObjects().clear();
		getSelectedObjects().addAll(getListObjects());		
	}
	
	@Override
	public void initObjectsBox() {
		
		List<ProdDerive> derives = ManagerLocator.getProdDeriveManager()
			.findLastCreationManager(SessionUtils
							.getSelectedBanques(sessionScope), getNbLastObjs());
		
		setListObjects(derives);
		setCurrentRow(null);
		setCurrentObject(null);
		
		getBinder().loadAttribute(
				self.getFellow("objectsListGrid"), "model");
	}
	
	@Override
	public void disableObjetsSelectionItems(boolean disable) {
		super.disableObjetsSelectionItems(disable);
		newCessionItem.setDisabled(disable || !isCanCession()
				|| getSelectedObjects().isEmpty() || areAllObjectsCessibles());
		stockageItem.setDisabled(disable || !isCanStockage()
				|| getSelectedObjects().isEmpty() || areAllObjectsStocked());
	}
	
	/**
	 * Méthode appelée après lors du focus sur le champ
	 * codeBoxDerive. Le radiobutton correspondant sera
	 * automatiquement sélectionné.
	 */
	public void onFocus$codeBoxDerive() {
		codeDerive.setChecked(true);
	}
	
	/**
	 * Méthode appelée après lors du focus sur le champ
	 * dateBoxDerive. Le radiobutton correspondant sera
	 * automatiquement sélectionné.
	 */
	public void onFocus$patientBoxDerive() {
		patientDerive.setChecked(true);
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * codeBoxDerive. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$codeBoxDerive() {
		codeBoxDerive.setValue(codeBoxDerive.getValue().toUpperCase());
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * patientBoxDerive. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$patientBoxDerive() {
		patientBoxDerive.setValue(patientBoxDerive.getValue().toUpperCase());
	}
	
	@Override
	public List<? extends TKdataObject> extractLastObjectsCreated() {
		return ManagerLocator.getProdDeriveManager()
			.findLastCreationManager(SessionUtils
					.getSelectedBanques(sessionScope), 
					getNbLastObjs());
	}
	
	@Override
	public List<Integer> doFindObjects() {
		List<Integer> derives = new ArrayList<Integer>();
		
		if (dateCreation.isChecked()) {			
				derives = ManagerLocator.getProdDeriveManager()
					.findAfterDateCreationReturnIdsManager(
							getSearchDateCreation(), 
							SessionUtils.getSelectedBanques(sessionScope));
		} else if (codeDerive.isChecked()) {			
			if (searchCode == null) {
				searchCode = "";
			}
			
			searchCode = searchCode.toUpperCase();
			codeBoxDerive.setValue(searchCode);
			if (!searchCode.equals("")) {
				if (searchCode.contains(",")) {
					List<String> codes = ObjectTypesFormatters
						.formateStringToList(searchCode);
					derives = ManagerLocator.getProdDeriveManager()
						.findByCodeInListManager(codes, 
								SessionUtils.getSelectedBanques(sessionScope), 
								new ArrayList<String>());
				} else {
					derives = 
						ManagerLocator.getProdDeriveManager()
							.findByCodeOrLaboBothSideWithBanqueReturnIdsManager(
									searchCode, 
								SessionUtils.getSelectedBanques(sessionScope), true);
				}
			} else {
				if (Messagebox.show(ObjectTypesFormatters
						.getLabel("message.research.message", 
						new String[]{"ProdDerive"}),
						Labels.getLabel("message.research.title"), 
						Messagebox.YES | Messagebox.NO, 
						Messagebox.QUESTION) == Messagebox.YES) {
					derives = ManagerLocator.getProdDeriveManager()
						.findAllObjectsIdsByBanquesManager(
								SessionUtils
							.getSelectedBanques(sessionScope));
				}
			}
		} else if (patientDerive.isChecked()) {
			if (searchPatientNom.contains(",")) {
				List<String> pats = ObjectTypesFormatters
					.formateStringToList(searchPatientNom);
				derives = 
					ManagerLocator.getProdDeriveManager()
					.findByPatientNomOrNipInListManager(pats, 
							SessionUtils.getSelectedBanques(sessionScope));
			} else {
				derives = 
					ManagerLocator.getProdDeriveManager()
						.findByPatientNomReturnIdsManager(
								searchPatientNom, 
						SessionUtils.getSelectedBanques(sessionScope), true);
			}
		}
		return derives;
	}
	
	@Override
	public List<? extends TKdataObject> extractObjectsFromIds(List<Integer> ids) {
		if (ids != null && ids.size() > 0) {
			return ManagerLocator.getProdDeriveManager()
				.findByIdsInListManager(ids);
		} else {
			return new ArrayList<ProdDerive>();
		}
	}
	
	/**
	 * Lance la recherche des derives en fournissant un fichier
	 * Excel contenant une liste de codes.
	 */
	public void onClick$findByListCodesDerive() {
		// récupère les codes des derives présents dans le
		// fichier excel que l'utilisateur va uploader
		List<String> codes = getListStringToSearch();
		List<Integer> derives = ManagerLocator.getProdDeriveManager()
			.findByCodeInListManager(codes, 
				SessionUtils.getSelectedBanques(sessionScope), new ArrayList<String>());
		// affichage de ces résultats
		showResultsAfterSearchByList(derives);
	}
	
	/**
	 * Lance la recherche des derives en fournissant un fichier
	 * Excel contenant une liste de patients (noms ou nips).
	 */
	public void onClick$findByListPatientsDerive() {
		// récupère les patients des derives présents dans le
		// fichier excel que l'utilisateur va uploader
		List<String> pats = getListStringToSearch();
		List<Integer> derives = ManagerLocator.getProdDeriveManager()
			.findByPatientNomOrNipInListManager(pats, 
					SessionUtils.getSelectedBanques(sessionScope));
		// affichage de ces résultats
		showResultsAfterSearchByList(derives);
	}
	
	/**
	 * Méthode appelée lors du clic sur le bouton de menu newCessionItem.
	 * Une nouvelle cession sera créée avec les échantillons sélectionnés
	 * en paramètre.
	 */
	public void onClick$newCessionItem() {		
		CessionController tabController = (CessionController) 
						CessionController.backToMe(getMainWindow(), page);
		
		Collections.sort(getSelectedObjects(), new ProdDerive.CodeComparator(true));
		tabController.switchToCreateMode(null, getSelectedObjects(), null);
		
		clearSelection();
	}
	
	/**
	 * Méthode appelée lors du clic sur le bouton de menu stockageItem.
	 * Les échantillons sélectionnés seront envoyés au module de stockage.
	 */
	public void onClick$stockageItem() {
		StockageController tabController = StockageController
										.backToMe(getMainWindow(), page);
		getMainWindow().blockAllPanelsExceptOne("stockageTab");
		tabController.clearAllPage();
		Collections.sort(getSelectedObjects(), new ProdDerive.CodeComparator(true));
		tabController.switchToStockerMode(null, getSelectedObjects(), 
													null, null, null);
		
		clearSelection();
	}
	
	@Override
	public void onClick$select() {
		super.onClick$select();
		CessionController.backToMe(getMainWindow(), page);
	}
	
	@Override
	public void onClick$cancelSelection() {
		super.onClick$cancelSelection();
		CessionController.backToMe(getMainWindow(), page);
	}
	
	/**
	 * Forwarded Event.
	 * Sélectionne le parent concernée pour l'afficher dans sa fiche. 
	 * @param event forwardé depuis le label parent cliquable 
	 * (event.getData contient l'objet Prelevement, Echantillon ou Derive 
	 * représentant le parent).
	 */
	public void onClickParent(Event event) {
		if (!getMode().equals("select")) {
			Object parent = event.getData();
			
			AbstractObjectTabController parentController = null;
			
			if (parent != null) {
				if (parent instanceof Echantillon) {
					parentController = EchantillonController
										.backToMe(getMainWindow(), page);
				} else if (parent instanceof Prelevement) {
					parentController = PrelevementController
										.backToMe(getMainWindow(), page);
				} else {
					onClickObject(event);
					return;
				}
			}
	
			if (parentController != null) {
				parentController
					.switchToFicheStaticMode((TKdataObject) event.getData());
			}
		}
	}
	
	/**
	 * Méthode appelée pour ouvrir la page de recherche avancée.
	 */
	public void onClick$findMore() {
		StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("recherche.avancee.prodDerives"));
		Entite entite = ManagerLocator.getEntiteManager()
			.findByNomManager("ProdDerive").get(0);
		
		openRechercheAvanceeWindow(page, 
				sb.toString(), 
				entite, 
				Path.getPath(self),
				isAnonyme(), this);
	}
	
	/*************************************************************************/
	/************************** DROITS ***************************************/
	/*************************************************************************/
	@Override
	public void applyDroitsOnListe() {		
		drawActionsButtons();
		listObjectsRenderer
			.setAccessStockage(getDroitOnAction("Stockage", "Consultation"));
		
		if (sessionScope.containsKey("ToutesCollections")) {
			// donne aucun droit en creation
			setCanNew(false);
			canCession = false;
			canStockage = false;
		} else {
			canCession = getDroitOnAction("Cession", "Creation");
			if (getDroitOnAction("ProdDerive", "Modification")
					&& getDroitOnAction("Stockage", "Consultation")) {
				canStockage = true;
			} else {
				canStockage = false;
			}
		}
		
		// List<String> entites = new ArrayList<String>();
		// entites.add("Prelevement");
		// entites.add("Echantillon");
		// entites.add("Cession");
		// setDroitsConsultation(drawConsultationLinks(entites));
		
		// si pas le droit d'accès aux parents, on cache le lien
		if (!getDroitsConsultation().get("Prelevement")) {
			listObjectsRenderer.setAccessPrelevement(false);
		}
		if (!getDroitsConsultation().get("Echantillon")) {
			listObjectsRenderer.setAccessEchantillon(false);
		}
		
		super.applyDroitsOnListe();
		listObjectsRenderer.setAnonyme(isAnonyme());
	}
	
	public boolean isCanCession() {
		return canCession;
	}

	public void setCanCession(boolean can) {
		this.canCession = can;
	}

	public boolean isCanStockage() {
		return canStockage;
	}

	public void setCanStockage(boolean can) {
		this.canStockage = can;
	}
	
	@Override
	public void clearSelection() {
		newCessionItem.setDisabled(true);
		stockageItem.setDisabled(true);
		super.clearSelection();
	}	
	
//	/**
//	 * Impression des étiquettes.
//	 */
//	public void onClick$etiquetteItem() {
//		// on récupère les imprimantes associées au compte
//		// pour la banque courante
//		List<AffectationImprimante> imprimantes = ManagerLocator
//			.getAffectationImprimanteManager()
//			.findByBanqueUtilisateurManager(SessionUtils
//					.getSelectedBanques(sessionScope).get(0), 
//					SessionUtils.getLoggedUser(sessionScope));
//		
//		Imprimante imp = null;
//		if (imprimantes.size() > 0) {
//			imp = imprimantes.get(0).getImprimante();
//		}
//		Modele mod = null;
//		if (imprimantes.size() > 0) {
//			mod = imprimantes.get(0).getModele();
//		}
//		if (imp != null) {
//			// si on utilise l'API tumo
//			if (imp.getImprimanteApi().getNom().equals("tumo")) {
//				try {            
//					int completed = 0;
//					
//					completed = ManagerLocator.getTumoBarcodePrinter()
//						.printDerive(
//							getSelectedObjects(), 1, imp, mod);
//					
//					if (completed != 1) {
//						if (completed == -1) {
//							Messagebox.show(ObjectTypesFormatters
//									.getLabel("validation.erreur" 
//									+ ".imprimante.non.detectee", 
//									new String[] {imp.getNom()}), 
//							"Error", Messagebox.OK, Messagebox.ERROR);
//						} else {
//							Messagebox.show(Labels
//							.getLabel("validation.erreur.impression"), 
//							"Error", Messagebox.OK, Messagebox.ERROR);
//						}
//					} else {
//						Messagebox.show(Labels
//								.getLabel("general.impression.ok"), 
//								"OK", Messagebox.OK, 
//								Messagebox.INFORMATION);
//					}
//				} catch (RuntimeException re) {
//					Messagebox.show(handleExceptionMessage(re), 
//							"Error", Messagebox.OK, Messagebox.ERROR);	
//		    	} catch (Exception e) {
//		    		StringBuffer sb = new StringBuffer();
//		    		sb.append(Labels
//								.getLabel("validation.erreur.impression"));
//		    		sb.append(" ");
//		    		sb.append(e.getMessage());
//		    		Messagebox.show(sb.toString(), 
//							"Error", Messagebox.OK, Messagebox.ERROR);
//		    	}
//			} else if (imp.getImprimanteApi().getNom().equals("mbio")) {
//				// si on utilise l'API mbio
//				MBioFileProperties mBioFile = 
//					new MBioFileProperties(imp.getMbioPrinter());
//				
//				MBioBarcodePrinter printer = new MBioBarcodePrinter(
//						mBioFile, 
//						mBioFile.getConfDir());
//				int completed = 0;
//				completed = printer.printDerive(getSelectedObjects(), 1);
//				
//				if (completed != 1) {
//					Messagebox.show(Labels
//							.getLabel("validation.erreur.impression"), 
//							"Error", Messagebox.OK, Messagebox.ERROR);
//				} else {
//					Messagebox.show(Labels
//							.getLabel("general.impression.ok"), 
//							"OK", Messagebox.OK, 
//							Messagebox.INFORMATION);
//				}
//			}
//		}
//	}

	public ProdDerivesNbDerivesComparator getComparatorDerivesAsc() {
		return comparatorDerivesAsc;
	}

	public void setComparatorDerivesAsc(
			ProdDerivesNbDerivesComparator c) {
		this.comparatorDerivesAsc = c;
	}

	public ProdDerivesNbDerivesComparator getComparatorDerivesDesc() {
		return comparatorDerivesDesc;
	}

	public void setComparatorDerivesDesc(
			ProdDerivesNbDerivesComparator c) {
		this.comparatorDerivesDesc = c;
	}

	public ProdDerivesNbCessionsComparator getComparatorCessionsAsc() {
		return comparatorCessionsAsc;
	}

	public void setComparatorCessionsAsc(
			ProdDerivesNbCessionsComparator c) {
		this.comparatorCessionsAsc = c;
	}

	public ProdDerivesNbCessionsComparator getComparatorCessionsDesc() {
		return comparatorCessionsDesc;
	}

	public void setComparatorCessionsDesc(
			ProdDerivesNbCessionsComparator c) {
		this.comparatorCessionsDesc = c;
	}
	
	@Override
	public void batchDelete(List<Integer> ids, String comment) {
		ManagerLocator.getProdDeriveManager().removeListFromIdsManager(ids, comment, 
				SessionUtils.getLoggedUser(sessionScope));
	}
	
	/**
	 * Affiche une dans la liste les dérivés à partir de la liste des codes, qui 
	 * peut être obtenue depuis un fichier Excel, ou un scan full-rack barcode 2D.
	 * Affiche une notification si codes proviennent d'un scan. 
	 * @since 2.1
	 */
	public void displayTKObjectsFromCodes(List<String> codes, ScanTerminale sT) {
		List<String> notfounds = new ArrayList<String>();
		List<Integer> derives = ManagerLocator.getProdDeriveManager()
				.findByCodeInListManager(codes,
						SessionUtils.getSelectedBanques(sessionScope), notfounds);
		if (sT == null || !derives.isEmpty()) {
			// affichage de ces résultats
			showResultsAfterSearchByList(derives);
		}
		
		if (sT != null) {
			Clients.showNotification(ObjectTypesFormatters.getLabel("scan.objects.display.info", 
					new String[] {sT.getName(), ObjectTypesFormatters.dateRenderer2(sT.getDateScan()),
						String.valueOf(sT.getNbTubesStored()),
						String.valueOf(derives.size())})
				, derives.size() == sT.getNbTubesStored() ? "info" : "warning", null, null, 3000);
		}
	}
}