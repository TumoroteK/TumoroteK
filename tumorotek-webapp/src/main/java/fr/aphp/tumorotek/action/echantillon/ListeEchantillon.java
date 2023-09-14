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
package fr.aphp.tumorotek.action.echantillon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.comparator.EchantillonsNbCessionsComparator;
import fr.aphp.tumorotek.action.comparator.EchantillonsNbDerivesComparator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant une liste d'échantillons. Controller créé le 02/11/2009.
 *
 * @since 2.1 export biobanques
 *
 * @author Pierre Ventadour
 * @version 2.3.0-gatsbi
 *
 */
public class ListeEchantillon extends AbstractListeController2
{

	// private Log log = LogFactory.getLog(ListeEchantillon.class);

	private static final long serialVersionUID = -6167747099087709700L;

	private List<Echantillon> listObjects = new ArrayList<>();
	private List<Echantillon> selectedObjects = new ArrayList<>();

	private Menuitem newCessionItem;
	private Menuitem stockageItem;

	protected Menuitem storageRobotItem;

	// Critères de recherche.
	private Radio codeEchan;
	private Radio patientEchantillon;
	private Textbox codeBoxEchan;
	private Textbox patientBoxEchan;
	
	// gatsbi column créés dynamiquement, écrasées pour être sortable
	protected Column nbProdDerivesColumn;
	protected Column nbCessionsColumn;

	// Variables formulaire pour les critères.
	private String searchCode;
	private String searchPatientNom;

	// Variable de droits.
	private boolean canCession;
	private boolean canStockage;

	private Button findINCa;
	private Button findBiocap;

	private Menuitem exportItemINCa;
	private Menuitem exportItemTVGSO;
	private Menuitem exportItemTVGSOcsv;
	private Menuitem exportItemBIOCAP;
	// @since 2.1
	private Menuitem exportItemBIOBANQUES;

	protected EchantillonRowRenderer listObjectsRenderer= new EchantillonRowRenderer(true, false);
	private EchantillonsNbDerivesComparator comparatorDerivesAsc = new EchantillonsNbDerivesComparator(true);
	private EchantillonsNbDerivesComparator comparatorDerivesDesc = new EchantillonsNbDerivesComparator(false);
	private EchantillonsNbCessionsComparator comparatorCessionsAsc = new EchantillonsNbCessionsComparator(true);
	private EchantillonsNbCessionsComparator comparatorCessionsDesc = new EchantillonsNbCessionsComparator(false);

	public String getSearchCode(){
		return searchCode;
	}

	public void setSearchCode(final String search){
		this.searchCode = search;
	}

	public String getSearchPatientNom(){
		return searchPatientNom;
	}

	public void setSearchPatientNom(final String search){
		this.searchPatientNom = search;
	}

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		// recoit le renderer en argument
	      if(arg != null && arg.containsKey("renderer")){
	         setListObjectsRenderer((TKSelectObjectRenderer<? extends TKdataObject>) arg.get("renderer"));
	      }
	      
		// @since gatsbi
		try {
			drawColumnsForVisibleChampEntites();
		} catch (Exception e) {
			// une erreur inattendue levée dans la récupération 
			// ou le rendu d'une propriété prel
			// va arrêter le rendu du reste du tableau
			throw new RuntimeException(e);
		}

		setOnGetEventName("onGetEchantillonsFromSelection");
		listObjectsRenderer.setEmbedded(false);

		nbProdDerivesColumn.setSortAscending(comparatorDerivesAsc);
		nbProdDerivesColumn.setSortDescending(comparatorDerivesDesc);
		nbCessionsColumn.setSortAscending(comparatorCessionsAsc);
		nbCessionsColumn.setSortDescending(comparatorCessionsDesc);
	}
	
   /**
    * Cette méthode de dessin dynamique des colonnes est surchargée 
    * par Gatsbi
    * @since 2.3.0-gatsbi
    */
	protected void drawColumnsForVisibleChampEntites() 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
   }

   public void setListObjectsRenderer(final TKSelectObjectRenderer<? extends TKdataObject> listObjectsRenderer){
      this.listObjectsRenderer = (EchantillonRowRenderer) listObjectsRenderer;
   }

	@Override
	public List<Echantillon> getListObjects(){
		return listObjects;
	}

	@Override
	public void setListObjects(final List<? extends TKdataObject> objs){
		clearSelection();
		this.listObjects.clear();
		this.listObjects.addAll((List<Echantillon>) objs);
	}

	@Override
	public void addToListObjects(final TKdataObject obj, final Integer pos){
		if(obj != null){
			if(pos != null){
				getListObjects().add(pos.intValue(), (Echantillon) obj);
			}else{
				getListObjects().add((Echantillon) obj);
			}
		}
	}

	@Override
	public void removeObjectFromList(final TKdataObject obj){
		getListObjects().remove(obj);
	}

	@Override
	public List<Echantillon> getSelectedObjects(){
		return selectedObjects;
	}

	@Override
	public void setSelectedObjects(final List<? extends TKdataObject> objs){
		this.selectedObjects = (List<Echantillon>) objs;
	}

	@Override
	public void addToSelectedObjects(final TKdataObject obj){
		if(!getSelectedObjects().contains(obj)){
			getSelectedObjects().add((Echantillon) obj);
		}
	}

	@Override
	public void removeFromSelectedObjects(final TKdataObject obj){
		if(getSelectedObjects().contains(obj)){
			getSelectedObjects().remove(obj);
		}
	}

	@Override
	public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
		return listObjectsRenderer;
	}

	@Override
	public AbstractObjectTabController getObjectTabController(){
		return super.getObjectTabController();
	}

	@Override
	public void passSelectedToList(){
		getListObjects().clear();
		getListObjects().addAll(getSelectedObjects());
	}

	@Override
	public void passListToSelected(){
		getSelectedObjects().clear();
		getSelectedObjects().addAll(getListObjects());
	}

	@Override
	public void initObjectsBox(){

		final List<Echantillon> echans = ManagerLocator.getEchantillonManager()
				.findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope), getNbLastObjs());

		setListObjects(echans);
		setCurrentRow(null);
		setCurrentObject(null);

		getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
	}

	@Override
	public void disableObjetsSelectionItems(final boolean disable){
		super.disableObjetsSelectionItems(disable);
		//TK-314 :
		newCessionItem.setDisabled(disable || !isCanCession() || getSelectedObjects().isEmpty() || areAllObjectsNonCessibles());
		stockageItem.setDisabled(disable || !isCanStockage() || getSelectedObjects().isEmpty() || areAllObjectsStocked());
		if(exportItemINCa.isVisible()){
			exportItemINCa.setDisabled(disable || !isCanExport() || getSelectedObjects().size() < 1);
		}
		if(exportItemTVGSO.isVisible()){
			exportItemTVGSO.setDisabled(disable || !isCanExport() || getSelectedObjects().size() < 1);
		}
		if(exportItemTVGSOcsv.isVisible()){
			exportItemTVGSOcsv.setDisabled(disable || !isCanExport() || getSelectedObjects().size() < 1);
		}
		if(exportItemBIOCAP.isVisible()){
			exportItemBIOCAP.setDisabled(disable || !isCanExport() || getSelectedObjects().size() < 1);
		}
		//@since 2.1
		if(exportItemBIOBANQUES.isVisible()){
			exportItemBIOBANQUES.setDisabled(disable || !isCanExport() || getSelectedObjects().size() < 1);
		}
		//@since 2.2.1-IRELEC
		if (storageRobotItem.isVisible()) {
			storageRobotItem.setDisabled(disable 
					|| !hasAnySelectedInStock());
		}
	}

	/**
	 * @since 2.2.1-IRELEC
	 * @return
	 */
	private boolean hasAnySelectedInStock() {
		for (TKStockableObject tkO : getSelectedObjects()) {
			if (tkO.getEmplacement() != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Méthode appelée après lors du focus sur le champ codeBoxEchan. Le
	 * radiobutton correspondant sera automatiquement sélectionné.
	 */
	public void onFocus$codeBoxEchan(){
		codeEchan.setChecked(true);
	}

	/**
	 * Méthode appelée après lors du focus sur le champ dateBoxEchan. Le
	 * radiobutton correspondant sera automatiquement sélectionné.
	 */
	public void onFocus$patientBoxEchan(){
		patientEchantillon.setChecked(true);
	}

	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ codeBoxEchan.
	 * Cette valeur sera mise en majuscules.
	 */
	public void onBlur$codeBoxEchan(){
		codeBoxEchan.setValue(codeBoxEchan.getValue().toUpperCase());
	}

	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * patientBoxEchan. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$patientBoxEchan(){
		patientBoxEchan.setValue(patientBoxEchan.getValue().toUpperCase());
	}

	@Override
	public List<? extends TKdataObject> extractLastObjectsCreated(){
		return ManagerLocator.getEchantillonManager().findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope),
				getNbLastObjs());
	}

	@Override
	public List<Integer> doFindObjects(){
		List<Integer> echantillons = new ArrayList<>();

		if(dateCreation.isChecked()){
			echantillons = ManagerLocator.getEchantillonManager().findAfterDateCreationReturnIdsManager(getSearchDateCreation(),
					SessionUtils.getSelectedBanques(sessionScope));
		}else if(codeEchan.isChecked()){
			if(searchCode == null){
				searchCode = "";
			}

			searchCode = searchCode.toUpperCase();
			codeBoxEchan.setValue(searchCode);
			if(!searchCode.equals("")){
				if(searchCode.contains(",")){
					final List<String> codes = ObjectTypesFormatters.formateStringToList(searchCode);
					final List<String> notfounds = new ArrayList<>();
					echantillons = ManagerLocator.getEchantillonManager().findByCodeInListManager(codes,
							SessionUtils.getSelectedBanques(sessionScope), notfounds);
				}else{
					echantillons = ManagerLocator.getEchantillonManager().findByCodeLikeBothSideWithBanqueReturnIdsManager(searchCode,
							SessionUtils.getSelectedBanques(sessionScope), true);
				}
			}else{
				if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {"Echantillon"}),
						Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
					echantillons = ManagerLocator.getEchantillonManager()
							.findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope));
				}
			}
		}else if(patientEchantillon.isChecked()){
			if(searchPatientNom.contains(",")){
				final List<String> pats = ObjectTypesFormatters.formateStringToList(searchPatientNom);
				echantillons = findEchantillonByPatientCodes(pats);
			}else{
				echantillons = searchEchantillonByPatientInfos(searchPatientNom);
			}
		}
		return echantillons;
	}

	@Override
	public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
		if(ids != null && ids.size() > 0){
			return ManagerLocator.getEchantillonManager().findByIdsInListManager(ids);
		}
		return new ArrayList<Echantillon>();
	}

	/**
	 * Lance la recherche des échantillons en fournissant un fichier Excel
	 * contenant une liste de codes.
	 */
	public void onClick$findByListCodesEchan(){
		// récupère les codes des échantillons présents dans le
		// fichier excel que l'utilisateur va uploader
		final List<String> codes = getListStringToSearch();

		// @since2.1
		displayTKObjectsFromCodes(codes, null);
	}

	/**
	 * Affiche une dans la liste les échantillons à partir de la liste des codes, qui 
	 * peut être obtenue depuis un fichier Excel, ou un scan full-rack barcode 2D.
	 * Affiche une notification si codes proviennent d'un scan. 
	 * @since 2.1
	 */
	public void displayTKObjectsFromCodes(final List<String> codes, final ScanTerminale sT){
		final List<String> notfounds = new ArrayList<>();
		final List<Integer> echantillons = ManagerLocator.getEchantillonManager().findByCodeInListManager(codes,
				SessionUtils.getSelectedBanques(sessionScope), notfounds);
		if(sT == null || !echantillons.isEmpty()){
			// affichage de ces résultats
			showResultsAfterSearchByList(echantillons);
		}

		if(sT != null){
			Clients.showNotification(
					ObjectTypesFormatters.getLabel("scan.objects.display.info",
							new String[] {sT.getName(), ObjectTypesFormatters.dateRenderer2(sT.getDateScan()),
									String.valueOf(sT.getNbTubesStored()), String.valueOf(echantillons.size())}),
					echantillons.size() == sT.getNbTubesStored() ? "info" : "warning", null, null, 3000);
		}
	}

	/**
	 * Lance la recherche des échantillons en fournissant un fichier Excel
	 * contenant une liste de patients (noms ou nips).
	 */
	public void onClick$findByListPatientsEchan(){
		// récupère les patients présents dans le
		// fichier excel que l'utilisateur va uploader
		final List<String> pats = getListStringToSearch();
		final List<Integer> echantillons = findEchantillonByPatientCodes(pats);
		// affichage de ces résultats
		showResultsAfterSearchByList(echantillons);
	}
	
   /**
    * Sera surchargée par GATSBI.
    * @since 2.3.0-gatsbi
    * @param pats
    * @return
    */
   protected List<Integer> findEchantillonByPatientCodes(List<String> pats){
      return ManagerLocator.getPrelevementManager().findByPatientNomOrNipInListManager(pats,
         SessionUtils.getSelectedBanques(sessionScope));
   }

   /**
    * Sera surchargée par GATSBI.
    * @since 2.3.0-gatsbi
    * @param pats
    * @return
    */
   protected List<Integer> searchEchantillonByPatientInfos(String search){
      return ManagerLocator.getEchantillonManager().findByPatientNomReturnIdsManager(searchPatientNom,
         SessionUtils.getSelectedBanques(sessionScope), true);
   }

	/**
	 * Méthode appelée lors du clic sur le bouton de menu newCessionItem. Une
	 * nouvelle cession sera créée avec les échantillons sélectionnés en
	 * paramètre.
	 */
	public void onClick$newCessionItem(){
		final CessionController tabController = (CessionController) CessionController.backToMe(getMainWindow(), page);
		Collections.sort(getSelectedObjects(), new Echantillon.CodeComparator(true));
		tabController.switchToCreateMode(getSelectedObjects(), null, null);

		clearSelection();
	}

	/**
	 * Méthode appelée lors du clic sur le bouton de menu stockageItem. Les
	 * échantillons sélectionnés seront envoyés au module de stockage.
	 */
	public void onClick$stockageItem(){
		final StockageController tabController = StockageController.backToMe(getMainWindow(), page);
		getMainWindow().blockAllPanelsExceptOne("stockageTab");
		tabController.clearAllPage();
		Collections.sort(getSelectedObjects(), new Echantillon.CodeComparator(true));
		tabController.switchToStockerMode(getSelectedObjects(), null, null, null, null);

		clearSelection();
	}

	@Override
	public void onClick$select(){
		super.onClick$select();
		CessionController.backToMe(getMainWindow(), page);
	}

	@Override
	public void onClick$cancelSelection(){
		super.onClick$cancelSelection();
		CessionController.backToMe(getMainWindow(), page);
	}

	/**
	 * Méthode appelée pour ouvrir la page de recherche avancée.
	 */
	public void onClick$findMore(){
		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("recherche.avancee.echantillons"));
		final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);

		openRechercheAvanceeWindow(page, sb.toString(), entite, Path.getPath(self), isAnonyme(), this);
	}

	/**
	 * Méthode appelée pour ouvrir la page de recherche avancée INCa.
	 */
	public void onClick$findINCa(){
		final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);

		openRechercheINCaWindow(page, entite, Path.getPath(self));
	}

	/**
	 * Méthode appelée par la fenêtre FicheRechercheINCa quand l'utilisateur
	 * fait une recherche.
	 * 
	 * @param e
	 *            Event contenant les résultats de la recherche.
	 */

	public void onGetObjectFromResearchINCa(final Event e){

		// si des échantillons sont renvoyés
		if(e.getData() != null){
			listObjects = new ArrayList<>();
			clearSelection();
			setListObjects((List<? extends TKdataObject>) e.getData());
			setCurrentRow(null);
			setCurrentObject(null);
			getObjectTabController().clearStaticFiche();
			getObjectTabController().switchToOnlyListeMode();
			getBinder().loadComponent(objectsListGrid);
		}
	}

	public void onClick$findBiocap(){
		openRechercheBiocapWindow(page, self, getObjectTabController());
	}

	/*************************************************************************/
	/************************** DROITS ***************************************/
	/*************************************************************************/

	@Override
	public void applyDroitsOnListe(){
		drawActionsButtons();
		listObjectsRenderer.setAccessStockage(getDroitOnAction("Stockage", "Consultation"));

		if(sessionScope.containsKey("ToutesCollections")){
			// donne aucun droit en creation
			setCanNew(false);
			canCession = false;
			canStockage = false;
		}else{
			canCession = getDroitOnAction("Cession", "Creation");
			if(getDroitOnAction("Echantillon", "Modification") && getDroitOnAction("Stockage", "Consultation")){
				canStockage = true;
			}else{
				canStockage = false;
			}
		}

		boolean inca = false;
		boolean tvgso = false;
		boolean biocap = false;
		if(sessionScope.containsKey("catalogues")){
			final Map<String, Catalogue> catsMap = (Map<String, Catalogue>) sessionScope.get("catalogues");

			inca = catsMap.containsKey("INCa");
			tvgso = catsMap.containsKey("TVGSO");
			biocap = catsMap.containsKey("BIOCAP");
		}
		// findINCa.setVisible(inca);
		findINCa.setVisible(false);
		exportItemINCa.setVisible(inca);
		exportItemTVGSO.setVisible(tvgso);
		exportItemTVGSOcsv.setVisible(tvgso);
		exportItemBIOCAP.setVisible(biocap);
		findBiocap.setVisible(biocap);
		//@since 2.1
		// export biobanques concerne toutes collections CRB
		exportItemBIOBANQUES.setVisible(true);

		// @since 2.2.1-IRELEC
		// set storageRobotItem visible ssi Adin PF 
		// et IRELEC recepteur configuré
		storageRobotItem.setVisible(storageRobotItemVisible());

		super.applyDroitsOnListe();
		listObjectsRenderer.setAnonyme(isAnonyme());
	}

	public boolean isCanCession(){
		return canCession;
	}

	public void setCanCession(final boolean can){
		this.canCession = can;
	}

	public boolean isCanStockage(){
		return canStockage;
	}

	public void setCanStockage(final boolean can){
		this.canStockage = can;
	}

	@Override
	public void clearSelection(){
		newCessionItem.setDisabled(true);
		stockageItem.setDisabled(true);
		storageRobotItem.setDisabled(true);
		super.clearSelection();
	}

	//	/**
	//	 * Impression des étiquettes.
	//	 */
	//	public void onClick$etiquetteItem() {
	//		// on récupère les imprimantes associées au compte
	//		// pour la banque courante
	//		List<AffectationImprimante> imprimantes = ManagerLocator
	//				.getAffectationImprimanteManager()
	//				.findByBanqueUtilisateurManager(
	//						SessionUtils.getSelectedBanques(sessionScope).get(0),
	//						SessionUtils.getLoggedUser(sessionScope));
	//
	//		Imprimante imp = null;
	//		if (imprimantes.size() > 0) {
	//			imp = imprimantes.get(0).getImprimante();
	//		}
	//		Modele mod = null;
	//		if (imprimantes.size() > 0) {
	//			mod = imprimantes.get(0).getModele();
	//		}
	//
	//		if (imp != null) {
	//			// si on utilise l'API tumo
	//			if (imp.getImprimanteApi().getNom().equals("tumo")) {
	//				try {
	//					int completed = 0;
	//					// String texte = "";
	//					// if (mod != null && mod.getTexteLibre() != null) {
	//					// String texte = mod.getTexteLibre();
	//					// }
	//
	//					completed = ManagerLocator
	//							.getTumoBarcodePrinter()
	//							.printEchantillon(getSelectedObjects(), 1, imp, mod);
	//
	//					if (completed != 1) {
	//						if (completed == -1) {
	//							Messagebox
	//									.show(ObjectTypesFormatters
	//											.getLabel(
	//													"validation.erreur"
	//															+ ".imprimante.non.detectee",
	//													new String[] { imp.getNom() }),
	//											"Error", Messagebox.OK,
	//											Messagebox.ERROR);
	//						} else {
	//							Messagebox.show(Labels
	//									.getLabel("validation.erreur.impression"),
	//									"Error", Messagebox.OK, Messagebox.ERROR);
	//						}
	//					} else {
	//						Messagebox.show(
	//								Labels.getLabel("general.impression.ok"), "OK",
	//								Messagebox.OK, Messagebox.INFORMATION);
	//					}
	//				} catch (RuntimeException re) {
	//					Messagebox.show(handleExceptionMessage(re), "Error",
	//							Messagebox.OK, Messagebox.ERROR);
	//				} catch (Exception e) {
	//					StringBuffer sb = new StringBuffer();
	//					sb.append(Labels.getLabel("validation.erreur.impression"));
	//					sb.append(" ");
	//					sb.append(e.getMessage());
	//					Messagebox.show(sb.toString(), "Error", Messagebox.OK,
	//							Messagebox.ERROR);
	//				}
	//			} else if (imp.getImprimanteApi().getNom().equals("mbio")) {
	//				System.err.println("imprimante mbio");
	//				// si on utilise l'API mbio
	//				MBioFileProperties mBioFile = new MBioFileProperties(
	//						imp.getMbioPrinter());
	//
	//				MBioBarcodePrinter printer = new MBioBarcodePrinter(mBioFile,
	//						mBioFile.getConfDir());
	//				// MBioBarcodePrinter printer = new MBioBarcodePrinter();
	//				int completed = 0;
	//				completed = printer.printEchantillon(getSelectedObjects(), 1);
	//
	//				if (completed != 1) {
	//					Messagebox.show(
	//							Labels.getLabel("validation.erreur.impression"),
	//							"Error", Messagebox.OK, Messagebox.ERROR);
	//				} else {
	//					Messagebox.show(Labels.getLabel("general.impression.ok"),
	//							"OK", Messagebox.OK, Messagebox.INFORMATION);
	//				}
	//			}
	//		}
	//	}

	public EchantillonsNbDerivesComparator getComparatorDerivesAsc(){
		return comparatorDerivesAsc;
	}

	public void setComparatorDerivesAsc(final EchantillonsNbDerivesComparator c){
		this.comparatorDerivesAsc = c;
	}

	public EchantillonsNbDerivesComparator getComparatorDerivesDesc(){
		return comparatorDerivesDesc;
	}

	public void setComparatorDerivesDesc(final EchantillonsNbDerivesComparator c){
		this.comparatorDerivesDesc = c;
	}

	public EchantillonsNbCessionsComparator getComparatorCessionsAsc(){
		return comparatorCessionsAsc;
	}

	public void setComparatorCessionsAsc(final EchantillonsNbCessionsComparator c){
		this.comparatorCessionsAsc = c;
	}

	public EchantillonsNbCessionsComparator getComparatorCessionsDesc(){
		return comparatorCessionsDesc;
	}

	public void setComparatorCessionsDesc(final EchantillonsNbCessionsComparator c){
		this.comparatorCessionsDesc = c;
	}

	@Override
	public void onDoExportTVGSO(final boolean csv){
		onLaterExportCatalogue(false, ConfigManager.TVGSO_EXPORT, csv, null);
	}

	@Override
	public void onDoExportINCa(){
		onLaterExportCatalogue(false, ConfigManager.INCA_EXPORT, false, null);
	}

	@Override
	public void onDoExportBIOCAP(){
		onLaterExportCatalogue(false, ConfigManager.BIOCAP_EXPORT, false, null);
	}

	@Override
	public void onDoExportBIOBANQUES(){
		// csv export
		onLaterExportCatalogue(false, ConfigManager.BIOBANQUES_EXPORT, true, null);
	}

	public void onClick$exportItemTVGSO(){
		onLaterExportCatalogue(true, ConfigManager.TVGSO_EXPORT, false, null);
	}

	public void onClick$exportItemTVGSOcsv(){
		onLaterExportCatalogue(true, ConfigManager.TVGSO_EXPORT, true, null);
	}

	public void onClick$exportItemINCa(){
		onLaterExportCatalogue(true, ConfigManager.INCA_EXPORT, false, null);
	}

	public void onClick$exportItemBIOCAP(){
		onLaterExportCatalogue(true, ConfigManager.BIOCAP_EXPORT, false, null);
	}

	public void onClick$exportItemBIOBANQUES(){
		onLaterExportCatalogue(true, ConfigManager.BIOBANQUES_EXPORT, false, null);
	}

	/**
	 * Lance l'export à partir d'une selection ou d'une recherche 
	 * vide
	 * @param fromSelection
	 * @param csv si export csv demandé
	 * @version 2.1
	 */
	public void onLaterExportCatalogue(final boolean fromSelection, final short catalogue, final boolean csv,
			final Map<String, ?> params){
		if(fromSelection){
			getResultatsIds().clear();
			extractIdsFromList(new ArrayList<TKdataObject>(getSelectedObjects()), getResultatsIds());
		}
		getObjectTabController().onLaterExportCatalogue(getResultatsIds(), catalogue, null, csv, params);
	}

	@Override
	public void switchToSelectMode(){
		super.switchToSelectMode();
	}

	@Override
	public void batchDelete(final List<Integer> ids, final String comment){
		ManagerLocator.getEchantillonManager().removeListFromIdsManager(ids, comment, SessionUtils.getLoggedUser(sessionScope));
	}

	/***** Recepteur interfacage stockage automatisé -> portoir de transfert *******/

	/**
	 * @since 2.2.1-IRELEC
	 */
	public void onClick$storageRobotItem() {
		postStorageData(getSelectedObjects(), false);
	}
}
