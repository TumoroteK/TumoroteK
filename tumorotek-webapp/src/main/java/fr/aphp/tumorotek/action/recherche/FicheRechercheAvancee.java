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
package fr.aphp.tumorotek.action.recherche;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 * @since 2.2.1 date stockage echantillon/dérivés
 */
public class FicheRechercheAvancee extends AbstractFicheRechercheAvancee {

	// @since 2.2.1 chrome fix to div flex display
	protected Div topDivForScroll;
	
	// Components patient
	private Checkbox searchPatientsBox;
	private Textbox nipPatientBox;
	private Textbox nomPatientBox;
	private Textbox nomNaissancePatientBox;
	private Textbox prenomPatientBox;
	private Datebox dateNaissance1Box;
	private Datebox dateNaissance2Box;
	private Listbox operateursDateNaissanceBox;
	private Checkbox sexeFBox;
	private Checkbox sexeHBox;
	private Checkbox sexeIndBox;
	private Checkbox etatVBox;
	private Checkbox etatDBox;
	private Checkbox etatIncBox;
	private Combobox medecinsBox;
	// Components maladie
	private Textbox libelleMaladieBox;
	private Textbox codeMaladieBox;
	private Datebox dateDebutMaladie1Box;
	private Datebox dateDebutMaladie2Box;
	private Listbox operateursDateDebutBox;
	private Datebox dateDiagnosticMaladie1Box;
	private Datebox dateDiagnosticMaladie2Box;
	private Listbox operateursDateDiagBox;
	private Combobox medecinsMaPatBox;
	// Components prélèvement
	private Textbox ndaPatientBox;
	private Textbox codePrelevementBox;
	private Textbox codeLaboPrelevementBox;
	private Listbox natutePrelevementBox;
	private CalendarBox datePrelevement1Box;
	private CalendarBox datePrelevement2Box;
	private Listbox operateursDatePrlvtBox;
	private Listbox statutJuridiqueBox;
	private Listbox etablissementPreleveurBox;
	private Listbox servicePreleveurBox;
	private Intbox nbEchantillonBox;
	private Listbox operateursNbEchantillonBox;
	private Intbox agePrlvtBox;
	private Listbox operateursAgePrlvtBox;
	// since 2.0.13
	// private Textbox risquesBox;
	private Listbox risquesBox;
	// since 2.2.1
	private Listbox etablissementLaboBox;
	private Listbox serviceLaboBox;
	private Combobox operateurLaboBox;

	// non conformités
	private Listbox conformeBoolBox;
	private Listbox conformeEchanTraitementBoolBox;
	private Listbox conformeEchanCessionBoolBox;
	private Listbox conformeDeriveTraitementBoolBox;
	private Listbox conformeDeriveCessionBoolBox;
	private Textbox nonConformitesArriveeBox;
	private Listbox nonConformitesArriveeBoxHelper;
	private Textbox nonConformitesEchanTraitementBox;
	private Listbox nonConformitesEchanTraitementBoxHelper;
	private Textbox nonConformitesEchanCessionBox;
	private Listbox nonConformitesEchanCessionBoxHelper;
	private Textbox nonConformitesDeriveTraitementBox;
	private Listbox nonConformitesDeriveTraitementBoxHelper;
	private Textbox nonConformitesDeriveCessionBox;
	private Listbox nonConformitesDeriveCessionBoxHelper;

	// Components echantillon
	private Textbox codeEchantillonBox;
	private Listbox typeEchantillonBox;
	private Listbox qualiteEchantillonBox;
	private Listbox statutEchantillonBox;
	private Listbox modePreparationEchantillonBox;
	private Decimalbox quantiteEchantillonBox;
	private Listbox operateursQuantiteEchantillonBox;
	private Decimalbox delaiCglEchantillonBox;
	private Listbox operateursDelaiCglEchantillonBox;
	// date Stock since 2.2.1
	private CalendarBox dateStock1Box;
	private CalendarBox dateStock2Box;
	private Listbox operateursDateStockBox;
	// temp Stock since 2.0.13
	private Decimalbox tempStockEchantillonBox;
	private Listbox operateursTempStockEchantillonBox;
	private Textbox codeLesionnelBox;
	private Textbox codeOrganeBox;
	private Listbox crAnapathFilebox;
	// impact since 2.3
	private Checkbox impactEchanTBox;
	private Checkbox impactEchanFBox;
	
	// Components dérivés
	private Textbox codeProdDeriveBox;
	private Textbox codeLaboProdDeriveBox;
	private Listbox typeProdDeriveBox;
	private Listbox qualiteProdDeriveBox;
	private Listbox statutProdDeriveBox;
	private Decimalbox volumeDeriveBox;
	private Listbox operateursVolumeDerivesBox;
	private Decimalbox quantiteDeriveBox;
	private Listbox operateursQuantiteDeriveBox;
	// date Stock since 2.2.1
	private CalendarBox dateStockDerive1Box;
	private CalendarBox dateStockDerive2Box;
	private Listbox operateursDateStockDeriveBox;
	// temp Stock since 2.0.13
	private Decimalbox tempStockDeriveBox;
	private Listbox operateursTempStockDeriveBox;
	  // impact since 2.3
   private Checkbox impactDeriveTBox;
   private Checkbox impactDeriveFBox;

	// Groupes
	private Group groupPatients;
	private Group groupMaladies;
	private Group groupPrelevements;
	protected Group groupLaboInters;
	private Group groupEchantillons;
	private Group groupProdDerives;

	private Boolean groupPatientsOpened;
	private Boolean groupMaladiesOpened;
	private Boolean groupPrelevementsOpened;
	private Boolean groupLaboIntersOpened;
	private Boolean groupEchantillonsOpened;
	private Boolean groupProdDerivesOpened;

	/**
	 * Objets principaux.
	 */
	private boolean anonyme;
	protected Champ parent1ToQueryPatient;
	protected Champ parent2ToQueryPatient;
	protected Champ parent1ToQueryMaladie;
	protected Champ parent2ToQueryMaladie;
	protected Champ parent1ToQueryPrlvt;
	protected Champ parent2ToQueryPrlvt;
	private Champ parentToQueryEchantillon;
	protected boolean critereOnEchantillon = false;

	/**
	 * Liste d'objets.
	 */
	private List<Nature> natures;
	private List<ConsentType> consentTypes;
	private List<Service> services;
	private List<Etablissement> etablissements;
	private List<Collaborateur> collaborateurs;
	private List<EchantillonType> echantillonTypes;
	private List<EchanQualite> echanQualites;
	private List<ObjetStatut> objetStatuts;
	private List<ProdType> prodDeriveTypes;
	private List<ProdQualite> deriveQualites;
	private List<ModePrepa> modePrepas;

	private SimpleListModel<Service> servicesLabo;
	private SimpleListModel<Etablissement> etablissementsLabo;
	private SimpleListModel<Collaborateur> collaborateursLabo;

	private List<NonConformite> nCarrivee;
	private List<NonConformite> nCechanTraitement;
	private List<NonConformite> nCechanCession;
	private List<NonConformite> nCderiveTraitement;
	private List<NonConformite> nCderiveCession;

	private ListModelList<Risque> risquesModel;

	private static final long serialVersionUID = -7186817237148944889L;

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		objPatientComponents = new Component[] {this.nipPatientBox, this.nomPatientBox, this.nomNaissancePatientBox,
				this.prenomPatientBox, this.dateNaissance1Box, this.dateNaissance2Box, this.sexeFBox, this.sexeHBox, this.sexeIndBox,
				this.etatVBox, this.etatDBox, this.etatIncBox, this.medecinsBox};

		objMaladieComponents = new Component[] {this.libelleMaladieBox, this.codeMaladieBox, this.dateDebutMaladie1Box,
				this.dateDebutMaladie2Box, this.dateDiagnosticMaladie1Box, this.dateDiagnosticMaladie2Box, this.medecinsMaPatBox};

		objPrelevementComponents = new Component[] {this.ndaPatientBox, this.codePrelevementBox, this.natutePrelevementBox,
				this.codeLaboPrelevementBox, this.datePrelevement1Box, this.datePrelevement2Box, this.statutJuridiqueBox,
				this.etablissementPreleveurBox, this.servicePreleveurBox, this.nbEchantillonBox, this.agePrlvtBox, this.risquesBox,
				this.conformeBoolBox, this.nonConformitesArriveeBox, this.etablissementLaboBox, this.serviceLaboBox, this.operateurLaboBox};

		objEchantillonComponents = new Component[] {this.codeEchantillonBox, this.typeEchantillonBox, this.qualiteEchantillonBox,
				this.statutEchantillonBox, this.quantiteEchantillonBox, this.delaiCglEchantillonBox, 
				this.dateStock1Box, this.dateStock2Box, this.tempStockEchantillonBox,
				this.modePreparationEchantillonBox, this.codeLesionnelBox, this.codeOrganeBox, this.conformeEchanTraitementBoolBox,
				this.nonConformitesEchanTraitementBox, this.conformeEchanCessionBoolBox, this.nonConformitesEchanCessionBox,
				this.crAnapathFilebox, this.impactEchanFBox, this.impactEchanTBox};

		objProdDeriveComponents = new Component[] {this.codeProdDeriveBox, this.typeProdDeriveBox, this.codeLaboProdDeriveBox,
				this.qualiteProdDeriveBox, this.statutProdDeriveBox, this.volumeDeriveBox, this.quantiteDeriveBox,
				this.dateStockDerive1Box, this.dateStockDerive2Box, this.tempStockDeriveBox, 
				this.conformeDeriveTraitementBoolBox, this.nonConformitesDeriveTraitementBox,
				this.conformeDeriveCessionBoolBox, this.nonConformitesDeriveCessionBox, this.impactDeriveTBox, this.impactDeriveFBox};

		objAnonymeComponents = new Component[] {this.nipPatientBox, this.nomPatientBox, this.ndaPatientBox,
				this.nomNaissancePatientBox, this.dateNaissance1Box, this.dateNaissance2Box};

		objOperateurs = new Component[] {this.operateursAgePrlvtBox, this.operateursDateDebutBox, this.operateursDateDiagBox,
				this.operateursDateNaissanceBox, this.operateursDatePrlvtBox, this.operateursDelaiCglEchantillonBox,
				this.operateursDateStockBox, this.operateursTempStockEchantillonBox, 
				this.operateursNbEchantillonBox, this.operateursQuantiteDeriveBox,
				this.operateursQuantiteEchantillonBox, this.operateursVolumeDerivesBox, 
				this.operateursTempStockDeriveBox, this.operateursDateStockDeriveBox};

		prepareContextComponents();

		openGroups();

		getBinder().loadAll();

	}
	
	/**
	 * Gatsbi surcharge cette méthode
	 */
	protected void openGroups() {
		setGroupPatientsOpened(groupPatients.isOpen());
		setGroupMaladiesOpened(groupMaladies.isOpen());
		setGroupPrelevementsOpened(groupPrelevements.isOpen());
		if (groupLaboInters != null) {
			setGroupLaboIntersOpened(groupLaboInters.isOpen());
		}
		setGroupEchantillonsOpened(groupEchantillons.isOpen());
		setGroupProdDerivesOpened(groupProdDerives.isOpen());
	}

	/**
	 * Crééer les composants spécifiques au contexte de la banque courante
	 */
	protected void prepareContextComponents(){}

	/**
	 * Peuple les listes des composants spécifiques au contexte de la banque
	 * courante.
	 */
	protected void initContextsLists(){}

	/**
	 * Initialise la fiche de recherche.
	 * 
	 * @param entite
	 *            Entité que l'on recherche.
	 * @param path
	 *            Path pour renvoyer les résultats.
	 * @param ano
	 *            True si la recherche est anonyme.
	 */

	public void initRechercheAvancee(final Entite entite, final String path, final boolean ano,
			final AbstractListeController2 listeController){
		this.entiteToSearch = entite;
		this.pathToRespond = path;
		this.anonyme = ano;

		setObjectTabController(listeController.getObjectTabController());

		if(anonyme){
			for(int i = 0; i < objAnonymeComponents.length; i++){
				if(objAnonymeComponents[i].getClass().getSimpleName().equals("Textbox")){
					final Textbox box = (Textbox) objAnonymeComponents[i];
					box.setDisabled(true);
				}else if(objAnonymeComponents[i].getClass().getSimpleName().equals("Datebox")){
					final Datebox box = (Datebox) objAnonymeComponents[i];
					box.setDisabled(true);
				}
			}
		}

		// init des listes
		natures = ManagerLocator.getNatureManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		natures.add(0, null);
		consentTypes = ManagerLocator.getConsentTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		consentTypes.add(0, null);
		services = ManagerLocator.getServiceManager().findAllObjectsWithOrderManager();
		services.add(0, null);
		etablissements = ManagerLocator.getEtablissementManager().findAllObjectsWithOrderManager();
		etablissements.add(0, null);
		collaborateurs = ManagerLocator.getCollaborateurManager().findAllObjectsWithOrderManager();
		collaborateurs.add(0, null);
		echantillonTypes = ManagerLocator.getEchantillonTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		echantillonTypes.add(0, null);
		echanQualites = ManagerLocator.getEchanQualiteManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		echanQualites.add(0, null);
		objetStatuts = ManagerLocator.getObjetStatutManager().findAllObjectsManager();
		objetStatuts.add(0, null);
		prodDeriveTypes = ManagerLocator.getProdTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		prodDeriveTypes.add(0, null);
		deriveQualites = ManagerLocator.getProdQualiteManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		deriveQualites.add(0, null);
		modePrepas = ManagerLocator.getModePrepaManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		modePrepas.add(0, null);

		// init des listbox labo intermedaires 
		// seulement sur etablissement
		etablissementsLabo = new SimpleListModel<Etablissement>(etablissements);
		servicesLabo = new SimpleListModel<Service>(new ArrayList<Service>());
		collaborateursLabo = new SimpleListModel<Collaborateur>(ManagerLocator
				.getCollaborateurManager().findAllActiveObjectsWithOrderManager());

		nCarrivee = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
				SessionUtils.getPlateforme(sessionScope), "Arrivee",
				ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0));
		nCarrivee.add(0, null);

		nCechanTraitement = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
				SessionUtils.getPlateforme(sessionScope), "Traitement",
				ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
		nCechanTraitement.add(0, null);

		nCechanCession = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
				SessionUtils.getPlateforme(sessionScope), "Cession",
				ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
		nCechanCession.add(0, null);

		nCderiveTraitement = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
				SessionUtils.getPlateforme(sessionScope), "Traitement",
				ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
		nCderiveTraitement.add(0, null);

		nCderiveCession = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
				SessionUtils.getPlateforme(sessionScope), "Cession",
				ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
		nCderiveCession.add(0, null);

		// since 2.0.13
		risquesModel = new ListModelList<>();
		risquesModel.addAll(ManagerLocator.getRisqueManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));

		initContextsLists();

		operateursDecimaux = new ArrayList<>();
		operateursDecimaux.add("=");
		operateursDecimaux.add("<");
		operateursDecimaux.add(">");

		operateursDates = new ArrayList<>();
		operateursDates.add("=");
		operateursDates.add("<");
		operateursDates.add(">");
		operateursDates.add("[..]");
		
		initGroupAnnotations();

		createSearchHistoryListbox(entiteToSearch.getNom());
		
		// gatsbi overrides
		applyThesaurusRestrictions();

		getBinder().loadComponent(self);
	}
	
	/**
	 * Retire certaines valeurs de thésaurus suivant 
	 * le contexte appliqué par Gatsbi
	 * @since 2.3.0-gatsbi
	 */
	protected void applyThesaurusRestrictions() {		
	}

	/**
	 * Gatsbi surcharge cette méthode
	 */
	protected void initGroupAnnotations() {
		if(entiteToSearch.getNom().equals("Patient")){
			groupAnnotations.setLabel(Labels.getLabel("recherche.avancee.patient.annotations"));
		}else if(entiteToSearch.getNom().equals("Prelevement")){
			groupAnnotations.setLabel(Labels.getLabel("recherche.avancee.prelevements.annotations"));
		}else if(entiteToSearch.getNom().equals("Echantillon")){
			critereOnEchantillon = true;
			groupAnnotations.setLabel(Labels.getLabel("recherche.avancee.echantillons.annotations"));
		}else if(entiteToSearch.getNom().equals("ProdDerive")){
			groupAnnotations.setLabel(Labels.getLabel("recherche.avancee.prodDerives.annotations"));
			searchForProdDerives = true;
		}
	}

	@Override
	public void onOK(){
		onClick$find();
	}

	// Méthodes gérants l'ouverture des groupes pour descendre le scroll
	@Override
	public void onOpen$groupAnnotations(){
		// si le groupe n'a jamais été ouvert
		if(groupAnnotations.isOpen() && !annotationAlreadyOpen){
			// on init les annotations a afficher
			annotationAlreadyOpen = true;
			objAnnotationsComponent = new ArrayList<>();

			final List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
			if(banks.size() == 1){
				drawAnnotationPanelContent(banks.get(0));
			}else{
				for(int i = 0; i < banks.size(); i++){
					drawAnnotationPanelContent(banks.get(i));
				}
			}
		}

		final String id = groupAnnotations.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
	}

	public void onOpen$groupPatients(){
		final String id = groupPatients.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
		setGroupPatientsOpened(true);
	}

	public void onOpen$groupMaladies(){
		final String id = groupMaladies.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
		setGroupMaladiesOpened(true);
	}

	public void onOpen$groupPrelevements(){
		final String id = groupPrelevements.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
		setGroupPrelevementsOpened(true);
	}

	public void onOpen$groupLaboInters(){
		final String id = groupLaboInters.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
		setGroupLaboIntersOpened(true);
	}

	public void onOpen$groupEchantillons(){
		final String id = groupEchantillons.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
		setGroupEchantillonsOpened(true);
	}

	public void onOpen$groupProdDerives(){
		final String id = groupProdDerives.getUuid();
		final String idTop = topDivForScroll.getUuid();
		Clients.evalJavaScript(
				"document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
		setGroupProdDerivesOpened(true);
	}

	@Override
	public void onSelect$etablissementPreleveurBox() {

		Etablissement etab = null;
		if(etablissementPreleveurBox.getListModel() != null){
			etab =
					(Etablissement) etablissementPreleveurBox.getListModel().getElementAt(etablissementPreleveurBox.getSelectedIndex());
		}else{
			etab = (Etablissement) etablissementPreleveurBox.getSelectedItem().getValue();
		}

		if(etab != null){
			services = ManagerLocator.getEtablissementManager().getActiveServicesWithOrderManager(etab);
		}else{
			services = ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager();
		}
		services.add(0, null);

		final ListModel<Service> list = new ListModelList<>(services);
		servicePreleveurBox.setModel(list);

		getBinder().loadComponent(servicePreleveurBox);
	}

	@Override
	public void onSelect$etablissementLaboBox() {

		Etablissement etab = !etablissementsLabo.isSelectionEmpty() ? 
				etablissementsLabo.getSelection().iterator().next() : null;

		// may be selectedIndex (si put search history)
		if (etab == null && etablissementLaboBox.getSelectedIndex() > -1) {
			etab = etablissementsLabo.getElementAt(etablissementLaboBox.getSelectedIndex());
		}

		if(etab != null){
			List<Service> servs = ManagerLocator.getEtablissementManager()
					.getActiveServicesWithOrderManager(etab);
			servs.add(0, null);
			servicesLabo = new SimpleListModel<Service>(servs);
		}

		// serviceLaboBox.setModel(servicesLabo);

		getBinder().loadComponent(serviceLaboBox);
	}

	@Override
	public void onSelect$serviceLaboBox() {

		Service srv = !servicesLabo.isSelectionEmpty() ? 
				servicesLabo.getSelection().iterator().next() : null;

		// may be selectedIndex (si put search history)
		if (srv == null && serviceLaboBox.getSelectedIndex() > -1) {
			srv = servicesLabo.getElementAt(serviceLaboBox.getSelectedIndex());
		}

		List<Collaborateur> collabs = new ArrayList<Collaborateur>();
		if(srv != null){
			collabs.addAll(ManagerLocator.getCollaborateurManager()
					.findByServicesAndArchiveManager(srv, false));         
		} else {
			collabs.addAll(ManagerLocator.getCollaborateurManager()
					.findAllActiveObjectsWithOrderManager());
		}
		// collabs.add(0, null);

		collaborateursLabo = new SimpleListModel<Collaborateur>(collabs);

		// serviceLaboBox.setModel(servicesLabo);

		getBinder().loadComponent(operateurLaboBox);
	}



	@Override
	public void onClick$find(){
		oneValueEntered = false;
		resultats = new ArrayList<>();
		resultatsIds = new ArrayList<>();

		// création des chemins d'accès aux differents éléments
		if(entiteToSearch.getNom().equals("Patient")){
			createChampsParentsToQueryPatients();
		}else if(entiteToSearch.getNom().equals("Prelevement")){
			createChampsParentsToQueryPrelevements();
		}else if(entiteToSearch.getNom().equals("Echantillon")){
			createChampsParentsToQueryEchantillons();
		}else if(entiteToSearch.getNom().equals("ProdDerive")){
			createChampsParentsToQueryProdDerives();
		}

		// traite les critères sur les champs des patients
		if(getGroupPatientsOpened()){
			executeQueriesForPatients();
			if(objPatientContextComponents != null){
				executeQueriesForPatientsContext();
			}
		}

		// traite les critères sur les champs des maladies
		if(getGroupMaladiesOpened()){
			executeQueriesForMaladies();
			if(objMaladieContextComponents != null){
				executeQueriesForMaladiesContext();
			}
		}

		// traite les critères sur les champs des prlvts
		if(getGroupPrelevementsOpened() || getGroupLaboIntersOpened()){
			executeQueriesForPrelevements();
			if(objPrelevementContextComponents != null){
				executeQueriesForPrelevementsContext();
			}
		}

		// traite les critères sur les champs des échantillons
		if(getGroupEchantillonsOpened()){
			executeQueriesForEchantillons();
		}

		// traite les critères sur les champs des dérivés
		if(getGroupProdDerivesOpened()){
			executeQueriesForProdDerives();
		}

		// traite les critères sur les anntations
		executeQueriesForAnnotations();

		boolean areYouSure = true;
		if(!oneValueEntered){
			if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {entiteToSearch.getNom()}),
					Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.NO){
				areYouSure = false;
			}
		}

		if(areYouSure){
			Clients.showBusy(Labels.getLabel("recherche.avancee.en.cours"));
			Events.echoEvent("onLaterFind", self, null);
		}
	}

	/**
	 * Exécution de la requête.
	 */

	public void onLaterFind(){

		// si aucune valeur n'a été saisie dans aucun des champs, on
		// récupère tous les objets
		if(!oneValueEntered){
			// si l'utilisateur a validé la recherche
			if(entiteToSearch.getNom().equals("Patient")){
				if(searchPatientsBox != null && searchPatientsBox.isChecked()){
					resultatsIds.addAll(ManagerLocator.getPatientManager()
							.findAllObjectsIdsWithBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
				}else{
					resultatsIds.addAll(ManagerLocator.getPatientManager().findAllObjectsIdsManager());
				}
			}else if(entiteToSearch.getNom().equals("Prelevement")){
				resultatsIds.addAll(ManagerLocator.getPrelevementManager()
						.findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
			}else if(entiteToSearch.getNom().equals("Echantillon")){
				resultatsIds.addAll(ManagerLocator.getEchantillonManager()
						.findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
			}else if(entiteToSearch.getNom().equals("ProdDerive")){
				resultatsIds.addAll(ManagerLocator.getProdDeriveManager()
						.findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
			}
		}else{
			final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);
			// ajout d'un critère si on souhaite limiter la recherche
			// des patients à la collection courante
			addCritereToLimitPatients();
			if(criteresStandards.size() > 0){
				if(!otherQuery){
					resultatsIds = ManagerLocator.getTraitementQueryManager().findObjetByCriteresWithBanquesManager(criteresStandards,
							banques, valeursStandards);
				}else{
					// sinon on fait une intersection des résultats de la
					// requête avec ceux déjà trouvés
					final List<Integer> objectsIds = ManagerLocator.getTraitementQueryManager()
							.findObjetByCriteresWithBanquesManager(criteresStandards, banques, valeursStandards);

					resultatsIds = ListUtils.intersection(resultatsIds, objectsIds);
				}
				otherQuery = true;
			}

			if(criteresDerives1.size() > 0){
				if(critereOnEchantillon){
					criteresDerives2.clear();
					valeursDerives2.clear();
				}

				// on récupère les résultats pour le 1er chemin
				final List<Integer> objs1 =
						ManagerLocator.getTraitementQueryManager().findObjetByCriteresWithBanquesDeriveVersionManager(criteresDerives1,
								banques, valeursDerives1, searchForProdDerives);
				final List<Integer> finale = new ArrayList<>();
				finale.addAll(objs1);

				// si un 2eme chemin existe, on l'interroge et on fait
				// l'union des résultats
				if(!criteresDerives2.isEmpty()){
					final List<Integer> objs2 =
							ManagerLocator.getTraitementQueryManager().findObjetByCriteresWithBanquesDeriveVersionManager(criteresDerives2,
									banques, valeursDerives2, searchForProdDerives);

					for(int i = 0; i < objs2.size(); i++){
						if(!finale.contains(objs2.get(i))){
							finale.add(objs2.get(i));
						}
					}
				}

				// si c'est la 1ère requête les résultats vont directement
				// dans la liste
				if(!otherQuery){
					resultatsIds.addAll(finale);
				}else{
					// sinon on fait une intersection des résultats de la
					// requête avec ceux déjà trouvés
					resultatsIds = ListUtils.intersection(resultatsIds, finale);
				}
			}
		}

		// ferme wait message
		Clients.clearBusy();

		createSearchHistory(entiteToSearch.getNom());

		// critereOnEchantillon = false;
		if(resultatsIds.size() > 500){
			openResultatsWindow(page, resultatsIds, self, entiteToSearch.getNom(), getObjectTabController());
			// search history settings
			// createSearchHistory();
		}else if(resultatsIds.size() > 0){
			onShowResults();
			// createSearchHistory();
		}else{
			Messagebox.show(Labels.getLabel("recherche.avancee.no.results"), Labels.getLabel("recherche.avancee.no.results.title"),
					new Messagebox.Button[] {Messagebox.Button.CANCEL, Messagebox.Button.RETRY},
					new String[] {Labels.getLabel("general.cancel"), Labels.getLabel("general.retry")}, Messagebox.QUESTION, null,
					new EventListener<ClickEvent>()
			{
				@Override
				public void onEvent(final ClickEvent e){
					if(e.getName().equals("onClose")){
						Events.postEvent(new Event("onClose", self.getRoot()));
					}else if(e.getButton() != null){
						switch(e.getButton()){
						case CANCEL: // CANCEL is clicked
							Events.postEvent(new Event("onClose", self.getRoot()));
							break;
						case RETRY: // RETRY is clicked
							// ICI Nettoyer champs!
							criteresStandards.clear();
							valeursStandards.clear();
							criteresDerives1.clear();
							valeursDerives1.clear();
							criteresDerives2.clear();
							valeursDerives2.clear();
							getUsedComponents().clear();
							getUsedAnnoComponents().clear();
							otherQuery = false;
							break;
						default: // if e.getButton() returns null
						}
					}
				}
			});
		}
	}

	/**
	 * Ajoute un critere aux prélèvements pour filtrer les patients en fct de la
	 * collection.
	 */
	public void addCritereToLimitPatients(){
		if(entiteToSearch.getNom().equals("Patient") && searchPatientsBox != null && searchPatientsBox.isChecked()){
			boolean present = false;
			final Entite ePrlvt = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
			final ChampEntite ce = ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(ePrlvt, "Code").get(0);
			for(int i = 0; i < criteresStandards.size(); i++){
				final Critere crit = criteresStandards.get(i);
				if(crit.getChamp() != null && crit.getChamp().getChampEntite() != null
						&& crit.getChamp().getChampEntite().equals(ce)){
					present = true;
				}
			}

			if(!present){
				// création du champ avec ajout du parent si nécessaire
				final Champ champ = new Champ(ce);
				champ.setChampParent(parent1ToQueryPrlvt);

				final Critere crit = new Critere(champ, "like", null);
				criteresStandards.add(crit);
				valeursStandards.add("%");
			}
		}
	}

	/**
	 * Crée les champs parents qui permettront d'accéder aux éléments lors d'une
	 * recherche sur les patients.
	 */
	public void createChampsParentsToQueryPatients(){
		parent1ToQueryPatient = null;
		parent2ToQueryPatient = null;
		parent2ToQueryMaladie = null;
		parent2ToQueryPrlvt = null;
		// accès aux maladies
		final Entite patientEntite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
		final ChampEntite champEntiteMaladies =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(patientEntite, "Maladies").get(0);
		parent1ToQueryMaladie = new Champ(champEntiteMaladies);
		// accès aux prélèvements
		final Entite maladieEntite = ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0);
		final ChampEntite champEntitePrelevements =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(maladieEntite, "Prelevements").get(0);
		parent1ToQueryPrlvt = new Champ(champEntitePrelevements);
		parent1ToQueryPrlvt.setChampParent(parent1ToQueryMaladie);
		// accès aux échantillons
		final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
		final ChampEntite champEntiteEchantillons =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "Echantillons").get(0);
		parentToQueryEchantillon = new Champ(champEntiteEchantillons);
		parentToQueryEchantillon.setChampParent(parent1ToQueryPrlvt);
		// 1er accès aux produits dérivés
		final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
		final ChampEntite champEntiteEchanProdDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "ProdDerives").get(0);
		parent1ToQueryProdDerive = new Champ(champEntiteEchanProdDerives);
		parent1ToQueryProdDerive.setChampParent(parentToQueryEchantillon);
		// 2eme accès aux produits dérivés
		final ChampEntite champEntitePrlvtProdDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "ProdDerives").get(0);
		parent2ToQueryProdDerive = new Champ(champEntitePrlvtProdDerives);
		parent2ToQueryProdDerive.setChampParent(parent1ToQueryPrlvt);
	}

	/**
	 * Crée les champs parents qui permettront d'accéder aux éléments lors d'une
	 * recherche sur les prélèvements.
	 */
	public void createChampsParentsToQueryPrelevements(){
		// accès aux prélèvements
		parent1ToQueryPrlvt = null;
		parent2ToQueryPrlvt = null;
		parent2ToQueryPatient = null;
		parent2ToQueryMaladie = null;
		// accès aux maladies
		final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
		final ChampEntite champEntiteMaladie =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "MaladieId").get(0);
		parent1ToQueryMaladie = new Champ(champEntiteMaladie);
		// Accès aux patients
		final Entite maladieEntite = ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0);
		final ChampEntite champEntitePatient =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(maladieEntite, "PatientId").get(0);
		parent1ToQueryPatient = new Champ(champEntitePatient);
		parent1ToQueryPatient.setChampParent(parent1ToQueryMaladie);
		// accès aux échantillons
		final ChampEntite champEntiteEchantillons =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "Echantillons").get(0);
		parentToQueryEchantillon = new Champ(champEntiteEchantillons);
		// 1er accès aux produits dérivés
		final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
		final ChampEntite champEntiteEchanProdDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "ProdDerives").get(0);
		parent1ToQueryProdDerive = new Champ(champEntiteEchanProdDerives);
		parent1ToQueryProdDerive.setChampParent(parentToQueryEchantillon);
		// 2eme accès aux produits dérivés
		final ChampEntite champEntitePrlvtProdDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "ProdDerives").get(0);
		parent2ToQueryProdDerive = new Champ(champEntitePrlvtProdDerives);
		parent2ToQueryProdDerive.setChampParent(parent1ToQueryPrlvt);
	}

	/**
	 * Crée les champs parents qui permettront d'accéder aux éléments lors d'une
	 * recherche sur les échantillons.
	 */
	public void createChampsParentsToQueryEchantillons(){
		// accès aux échantillons
		parentToQueryEchantillon = null;
		parent2ToQueryPrlvt = null;
		parent2ToQueryPatient = null;
		parent2ToQueryMaladie = null;
		// accès aux prélèvements
		final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
		final ChampEntite champEntitePrelevement =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "PrelevementId").get(0);
		parent1ToQueryPrlvt = new Champ(champEntitePrelevement);
		// accès aux maladies
		final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
		final ChampEntite champEntiteMaladie =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "MaladieId").get(0);
		parent1ToQueryMaladie = new Champ(champEntiteMaladie);
		parent1ToQueryMaladie.setChampParent(parent1ToQueryPrlvt);
		// Accès aux patients
		final Entite maladieEntite = ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0);
		final ChampEntite champEntitePatient =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(maladieEntite, "PatientId").get(0);
		parent1ToQueryPatient = new Champ(champEntitePatient);
		parent1ToQueryPatient.setChampParent(parent1ToQueryMaladie);
		// 1er accès aux produits dérivés
		final ChampEntite champEntiteEchanProdDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "ProdDerives").get(0);
		parent1ToQueryProdDerive = new Champ(champEntiteEchanProdDerives);
		parent1ToQueryProdDerive.setChampParent(parentToQueryEchantillon);
		parent2ToQueryProdDerive = null;

	}

	/**
	 * Crée les champs parents qui permettront d'accéder aux éléments lors d'une
	 * recherche sur les dérivés.
	 */
	public void createChampsParentsToQueryProdDerives(){
		// accès aux échantillons
		final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
		final ChampEntite champEntiteEchanDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "ProdDerives").get(0);
		parentToQueryEchantillon = new Champ(champEntiteEchanDerives);
		// accès aux prélèvements
		final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
		final ChampEntite champEntitePrelevement =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "PrelevementId").get(0);
		parent1ToQueryPrlvt = new Champ(champEntitePrelevement);
		parent1ToQueryPrlvt.setChampParent(parentToQueryEchantillon);
		final ChampEntite champEntitePrlvtDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "ProdDerives").get(0);
		parent2ToQueryPrlvt = new Champ(champEntitePrlvtDerives);
		// accès aux maladies
		final ChampEntite champEntiteMaladie =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "MaladieId").get(0);
		parent1ToQueryMaladie = new Champ(champEntiteMaladie);
		parent1ToQueryMaladie.setChampParent(parent1ToQueryPrlvt);
		parent2ToQueryMaladie = new Champ(champEntiteMaladie);
		parent2ToQueryMaladie.setChampParent(parent2ToQueryPrlvt);
		// Accès aux patients
		final Entite maladieEntite = ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0);
		final ChampEntite champEntitePatient =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(maladieEntite, "PatientId").get(0);
		parent1ToQueryPatient = new Champ(champEntitePatient);
		parent1ToQueryPatient.setChampParent(parent1ToQueryMaladie);
		parent2ToQueryPatient = new Champ(champEntitePatient);
		parent2ToQueryPatient.setChampParent(parent2ToQueryMaladie);
		// 1er accès aux produits dérivés
		final ChampEntite champEntiteEchanProdDerives =
				ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "ProdDerives").get(0);
		parent1ToQueryProdDerive = new Champ(champEntiteEchanProdDerives);
		parent1ToQueryProdDerive.setChampParent(parentToQueryEchantillon);
		parent2ToQueryProdDerive = null;

	}

	/**
	 * Exécute les requêtes avec des critères sur les champs des patients.
	 */
	public void executeQueriesForPatients(){
		// pour chaque champ interrogeable pour les patients
		final List<Object> sexes = new ArrayList<>();
		final List<Object> etats = new ArrayList<>();
		for(int i = 0; i < objPatientComponents.length; i++){
			if(objPatientComponents[i] != null){
				// si c'est un textbox
				if(objPatientComponents[i].getClass().getSimpleName().equals("Textbox")){
					final Textbox current = (Textbox) objPatientComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null && !current.getValue().equals("")){

						// exécution de la requête
						executeSimpleQueryForTextbox(current, parent1ToQueryPatient, parent2ToQueryPatient,  false);

						oneValueEntered = true;
					}
				}else if(objPatientComponents[i].getClass().getSimpleName().equals("Listbox")){
					final Listbox current = (Listbox) objPatientComponents[i];
					// si une valeur a été saisie
					if(current.getSelectedIndex() > 0){
						Champ parent1 = null;
						Champ parent2 = null;
						// si le critère est sur le médecin, il faut passer
						// par le PatientMedecin
						if(current.getId().equals("medecinsBox")){
							final Entite patientEntite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
							final ChampEntite champMedecins =
									ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(patientEntite, "PatientMedecins").get(0);
							parent1 = new Champ(champMedecins);
							parent1.setChampParent(parent1ToQueryPatient);
							parent2 = new Champ(champMedecins);
							parent2.setChampParent(parent2ToQueryPatient);
						}

						// exécution de la requête
						executeSimpleQueryForListbox(current, parent1, parent2,  null);

						oneValueEntered = true;
					}
				}else if(objPatientComponents[i].getClass().getSimpleName().equals("Combobox")){
					final Combobox current = (Combobox) objPatientComponents[i];
					// si une valeur a été saisie
					if(current.getSelectedIndex() > 0){
						Champ parent1 = null;
						Champ parent2 = null;
						// si le critère est sur le médecin, il faut passer
						// par le PatientMedecin
						if(current.getId().equals("medecinsBox")){
							final Entite patientEntite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
							final ChampEntite champMedecins =
									ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(patientEntite, "PatientMedecins").get(0);
							parent1 = new Champ(champMedecins);
							parent1.setChampParent(parent1ToQueryPatient);
							parent2 = new Champ(champMedecins);
							parent2.setChampParent(parent2ToQueryPatient);
						}

						// exécution de la requête
						executeSimpleQueryForCombobox(current, parent1, parent2,  null);

						oneValueEntered = true;
					}
				}else if(objPatientComponents[i].getClass().getSimpleName().equals("Datebox")){
					final Datebox current = (Datebox) objPatientComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){
						String operateur = "";
						// si la requete porte sur la 1er date de naissance
						if(current.getId().equals("dateNaissance1Box")){
							int idx = 0;
							if(operateursDateNaissanceBox.getSelectedIndex() > 0){
								idx = operateursDateNaissanceBox.getSelectedIndex();
							}
							operateur = (String) operateursDateNaissanceBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("dateNaissance2Box")){
							// si la requete porte sur la 2eme date de naissance
							int idx = 0;
							if(operateursDateNaissanceBox.getSelectedIndex() > 0){
								idx = operateursDateNaissanceBox.getSelectedIndex();
							}
							operateur = (String) operateursDateNaissanceBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}

						// exécution de la requête
						if(operateur != null){
							executeSimpleQueryForDatebox(current, parent1ToQueryPatient, parent2ToQueryPatient, 
									operateur, false);
						}

						oneValueEntered = true;
					}
				}else if(objPatientComponents[i].getClass().getSimpleName().equals("Checkbox")){
					final Checkbox current = (Checkbox) objPatientComponents[i];
					// si une valeur a été saisie
					if(current.isChecked()){
						if(current.hasAttribute("sexe")){
							sexes.add(current.getAttribute("sexe"));
						}else{
							etats.add(current.getAttribute("etat"));
						}
						final RechercheCompValues rcv = new RechercheCompValues();
						rcv.setCompClass(Checkbox.class);
						rcv.setCompId(current.getId());
						rcv.setCheckedValue(current.isChecked());
						getUsedComponents().add(rcv);
					}
				}
			}
		}

		if(sexes.size() > 0){
			executeListQuery("Sexe", "Patient", ManagerLocator.getEntiteManager().findByIdManager(1), null,  sexes,
					false);
			oneValueEntered = true;
		}
		if(etats.size() > 0){
			executeListQuery("PatientEtat", "Patient", ManagerLocator.getEntiteManager().findByIdManager(1), null, 
					etats, false);
			oneValueEntered = true;
		}
	}

	/**
	 * Exécute les requêtes avec des critères sur les champs des maladies.
	 */
	public void executeQueriesForMaladies(){
		// pour chaque champ interrogeable pour les maladies
		for(int i = 0; i < objMaladieComponents.length; i++){
			if(objMaladieComponents[i] != null){
				// si c'est un textbox
				if(objMaladieComponents[i].getClass().getSimpleName().equals("Textbox")){
					final Textbox current = (Textbox) objMaladieComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null && !current.getValue().equals("")){
						;
						// exécution de la requête
						executeSimpleQueryForTextbox(current, parent1ToQueryMaladie, parent2ToQueryMaladie,  false);

						oneValueEntered = true;
					}
				}else if(objMaladieComponents[i].getClass().getSimpleName().equals("Datebox")){
					final Datebox current = (Datebox) objMaladieComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){

						String operateur = "";
						// si la requete porte sur la 1er date de début
						if(current.getId().equals("dateDebutMaladie1Box")){
							int idx = 0;
							if(operateursDateDebutBox.getSelectedIndex() > 0){
								idx = operateursDateDebutBox.getSelectedIndex();
							}
							operateur = (String) operateursDateDebutBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("dateDebutMaladie2Box")){
							// si la requete porte sur la 2eme date de début
							int idx = 0;
							if(operateursDateDebutBox.getSelectedIndex() > 0){
								idx = operateursDateDebutBox.getSelectedIndex();
							}
							operateur = (String) operateursDateDebutBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}else if(current.getId().equals("dateDiagnosticMaladie1Box")){
							// si la requete porte sur la 1er date de diag
							int idx = 0;
							if(operateursDateDiagBox.getSelectedIndex() > 0){
								idx = operateursDateDiagBox.getSelectedIndex();
							}
							operateur = (String) operateursDateDiagBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("dateDiagnosticMaladie2Box")){
							// si la requete porte sur la 2eme date de diag
							int idx = 0;
							if(operateursDateDiagBox.getSelectedIndex() > 0){
								idx = operateursDateDiagBox.getSelectedIndex();
							}
							operateur = (String) operateursDateDiagBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}
						// exécution de la requête
						if(operateur != null){
							executeSimpleQueryForDatebox(current, parent1ToQueryMaladie, parent2ToQueryMaladie, 
									operateur, false);
						}

						oneValueEntered = true;
					}
				}else if(objMaladieComponents[i].getClass().getSimpleName().equals("Combobox")){
					final Combobox current = (Combobox) objMaladieComponents[i];
					// si une valeur a été saisie
					if(current.getSelectedIndex() > 0){
						Champ parent1 = null;
						Champ parent2 = null;
						// si le critère est sur le médecin, il faut passer
						// par le PatientMedecin
						if(current.getId().equals("medecinsMaPatBox")){
							final Entite maladieEntite = ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0);
							final ChampEntite champMedecins =
									ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(maladieEntite, "Collaborateurs").get(0);
							parent1 = new Champ(champMedecins);
							parent1.setChampParent(parent1ToQueryMaladie);
							parent2 = new Champ(champMedecins);
							parent2.setChampParent(parent2ToQueryMaladie);
						}

						// exécution de la requête
						executeSimpleQueryForCombobox(current, parent1, parent2,  null);

						oneValueEntered = true;
					}
				}
			}
		}
	}

	/**
	 * Exécute les requêtes avec des critères sur les champs des prlvts.
	 * @version 2.0.13 risques as ListBox choix multiple
	 */
	public void executeQueriesForPrelevements(){
		// pour chaque champ interrogeable pour les prlvts
		for(int i = 0; i < objPrelevementComponents.length; i++){
			if(objPrelevementComponents[i] != null){
				// si c'est un textbox
				if(objPrelevementComponents[i].getClass().getSimpleName().equals("Textbox")){
					final Textbox current = (Textbox) objPrelevementComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null && !current.getValue().equals("")){
						Champ parent1 = parent1ToQueryPrlvt;
						Champ parent2 = parent2ToQueryPrlvt;
						if(current.getId().equals("nonConformitesArriveeBox")){
							final ChampEntite idPrel = ManagerLocator.getChampEntiteManager().findByIdManager(21);

							if(!entiteToSearch.getNom().equals("Prelevement")){
								parent1 = new Champ(idPrel);
								parent1.setChampParent(parent1ToQueryPrlvt);
								parent2 = new Champ(idPrel);
								parent2.setChampParent(parent2ToQueryPrlvt);

								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(1), current.getValue(), parent1, parent2,
										searchForProdDerives);
							}else{
								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(1), current.getValue(), null, null,
										searchForProdDerives);
							}

						}else{
							// exécution de la requête
							executeSimpleQueryForTextbox(current, parent1, parent2,  false);
						}

						oneValueEntered = true;
					}
				}else if(objPrelevementComponents[i].getClass().getSimpleName().equals("Listbox")){
					final Listbox current = (Listbox) objPrelevementComponents[i];
					// si une valeur a été saisie
					Champ parent1 = null;
					Champ parent2 = null;
					if(!current.isMultiple()){
						if(current.getSelectedIndex() > 0){
							if(current.getId().equals("medecinsMaPatBox")){
								// si la requete porte le medecin référent
								final Collaborateur collab = getCollaborateurs().get(current.getSelectedIndex());
								// execution de la requête
								executePrelevementsByMedecinQuery(collab);
								final RechercheCompValues rcv = new RechercheCompValues();
								rcv.setCompClass(Listbox.class);
								rcv.setCompId(current.getId());
								rcv.setSelectedIndexValue(current.getSelectedIndex());
								getUsedComponents().add(rcv);
							} else if (current.getId().equals("etablissementLaboBox")) { // since 2.2.1
								final Etablissement etab = getEtablissementsLabo().getElementAt(current.getSelectedIndex());
								executePrelevementViaLaboInterQuery(etab, current, current.getSelectedIndex());
							} else if (current.getId().equals("serviceLaboBox")) { // since 2.2.1
								final Service serv = getServicesLabo().getElementAt(current.getSelectedIndex());
								executePrelevementViaLaboInterQuery(serv, current, current.getSelectedIndex());
							}  else {
								// si le critère est sur l'établissement, il faut passer
								// par le service
								if(current.getId().equals("etablissementPreleveurBox")){
									final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
									final ChampEntite champService = ManagerLocator.getChampEntiteManager()
											.findByEntiteAndNomManager(prlvtEntite, "ServicePreleveurId").get(0);
									parent1 = new Champ(champService);
									parent1.setChampParent(parent1ToQueryPrlvt);
									parent2 = new Champ(champService);
									parent2.setChampParent(parent2ToQueryPrlvt);
								}else{
									parent1 = parent1ToQueryPrlvt;
									parent2 = parent2ToQueryPrlvt;
								}

								// exécution de la requête
								executeSimpleQueryForListbox(current, parent1, parent2,  null);
								// si le critère est sur le risque, il faut passer
								// par le Risques
							}
							oneValueEntered = true;
						}
					}else{ // multiples list boxes
						if(current.getId().equals("risquesBox") && !getRisquesModel().getSelection().isEmpty()){
							final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
							final ChampEntite champRisques =
									ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "Risques").get(0);
							parent1 = new Champ(champRisques);
							// parent1.setChampParent(parent1ToQueryPrlvt);
							// parent2 = new Champ(champRisques);
							// parent2.setChampParent(parent2ToQueryPrlvt);

							// exécution de la requête
							final List<Object> rNoms = new ArrayList<>();
							for(final Risque r : getRisquesModel().getSelection()){
								rNoms.add(r.getNom());
							}

							final boolean cumulative = ((Checkbox) current.getNextSibling()).isChecked();
							executeListQuery("nom", "Risque", prlvtEntite, parent1,  rNoms, cumulative);

							final RechercheCompValues rcv = new RechercheCompValues();
							rcv.setCompClass(Listbox.class);
							rcv.setCompId(current.getId());
							rcv.getSelectedValues().addAll(getRisquesModel().getSelection());
							rcv.setCumulative(cumulative);
							getUsedComponents().add(rcv);

							oneValueEntered = true;
						}
					}
				}else if(objPrelevementComponents[i].getClass().getSimpleName().equals("Datebox")){
					final Datebox current = (Datebox) objPrelevementComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){

						String operateur = "";
						// si la requete porte sur la 1er date de naissance
						if(current.getId().equals("datePrelevement1Box")){
							int idx = 0;
							if(operateursDatePrlvtBox.getSelectedIndex() > 0){
								idx = operateursDatePrlvtBox.getSelectedIndex();
							}
							operateur = (String) operateursDatePrlvtBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("datePrelevement2Box")){
							// si la requete porte sur la 2eme date
							int idx = 0;
							if(operateursDatePrlvtBox.getSelectedIndex() > 0){
								idx = operateursDatePrlvtBox.getSelectedIndex();
							}
							operateur = (String) operateursDatePrlvtBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}

						// exécution de la requête
						if(operateur != null){
							executeSimpleQueryForDatebox(current, parent1ToQueryPrlvt, parent2ToQueryPrlvt,  operateur,
									false);
						}

						oneValueEntered = true;
					}
				}else if(objPrelevementComponents[i].getClass().getSimpleName().equals("CalendarBox")){
					final CalendarBox current = (CalendarBox) objPrelevementComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){

						String operateur = "";
						// si la requete porte sur la 1er date de naissance
						if(current.getId().equals("datePrelevement1Box")){
							int idx = 0;
							if(operateursDatePrlvtBox.getSelectedIndex() > 0){
								idx = operateursDatePrlvtBox.getSelectedIndex();
							}
							operateur = (String) operateursDatePrlvtBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("datePrelevement2Box")){
							// si la requete porte sur la 2eme date de naissance
							int idx = 0;
							if(operateursDatePrlvtBox.getSelectedIndex() > 0){
								idx = operateursDatePrlvtBox.getSelectedIndex();
							}
							operateur = (String) operateursDatePrlvtBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}

						// exécution de la requête
						if(operateur != null){
							executeSimpleQueryForCalendarbox(current, parent1ToQueryPrlvt, parent2ToQueryPrlvt, 
									operateur);
						}

						oneValueEntered = true;
					}
				}else if(objPrelevementComponents[i].getClass().getSimpleName().equals("Intbox")){
					final Intbox current = (Intbox) objPrelevementComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){

						String operateur = "";
						// si la requete porte sur la quantité
						if(current.getId().equals("nbEchantillonBox")){
							int idx = 0;
							if(operateursNbEchantillonBox.getSelectedIndex() > 0){
								idx = operateursNbEchantillonBox.getSelectedIndex();
							}
							operateur = (String) operateursNbEchantillonBox.getListModel().getElementAt(idx);
							final Integer nb = current.getValue();
							// execution de la requête
							executeNbEchantillonsQuery(operateur, nb);
						}else if(current.getId().equals("agePrlvtBox")){
							// si la requete porte sur le delai
							int idx = 0;
							if(operateursAgePrlvtBox.getSelectedIndex() > 0){
								idx = operateursAgePrlvtBox.getSelectedIndex();
							}
							operateur = (String) operateursAgePrlvtBox.getListModel().getElementAt(idx);
							final Integer age = current.getValue();
							// execution de la requête
							executeAgeAuPrelevementQuery(operateur, age);
						}

						oneValueEntered = true;
						final RechercheCompValues rcv = new RechercheCompValues();
						rcv.setCompClass(Intbox.class);
						rcv.setCompId(current.getId());
						rcv.setTextValue(current.getText());
						getUsedComponents().add(rcv);
					}
				} else if (objPrelevementComponents[i].getClass().getSimpleName().equals("Combobox")){ // laboInter operateur
					final Combobox current = (Combobox) objPrelevementComponents[i];
					// si une valeur a été saisie
					if(current.getSelectedIndex() > 0){
						Object obj = null;
						if(current.getModel() != null){
							obj = current.getModel().getElementAt(current.getSelectedIndex());
						}else{
							obj = current.getSelectedItem().getValue();
						}
						executePrelevementViaLaboInterQuery(obj, current, current.getSelectedIndex());
						
						oneValueEntered = true;
					}
				}
			}
		}
	}

	/**
	 * Exécute les requêtes avec des critères sur les champs des échantillons.
	 */
	public void executeQueriesForEchantillons(){
		// pour chaque champ interrogeable pour les échantillons
	   final List<Boolean> impacts = new ArrayList<>();
		for(int i = 0; i < objEchantillonComponents.length; i++){
			if(objEchantillonComponents[i] != null){
				// si c'est un textbox
				if(objEchantillonComponents[i].getClass().getSimpleName().equals("Textbox")){
					final Textbox current = (Textbox) objEchantillonComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null && !current.getValue().equals("")){
						critereOnEchantillon = true;
						// si la requete porte sur la quantité
						if(current.getId().equals("codeOrganeBox")){

							// execution de la requête
							executeCodesQuery(current, parentToQueryEchantillon,  "CodesAssignes", "CodeOrgane", false);

						}else if(current.getId().equals("codeLesionnelBox")){

							// execution de la requête
							executeCodesQuery(current, parentToQueryEchantillon,  "CodesAssignes", "CodeOrgane", true);

						}else if(current.getId().equals("nonConformitesEchanTraitementBox")){

							if(!entiteToSearch.getNom().equals("Echantillon")){
								final ChampEntite idEchan = ManagerLocator.getChampEntiteManager().findByIdManager(50);
								final Champ parent1 = new Champ(idEchan);
								parent1.setChampParent(parentToQueryEchantillon);

								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(2), current.getValue(), parent1, null,
										searchForProdDerives);
							}else{
								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(2), current.getValue(), null, null,
										searchForProdDerives);
							}
						}else if(current.getId().equals("nonConformitesEchanCessionBox")){
							if(!entiteToSearch.getNom().equals("Echantillon")){
								final ChampEntite idEchan = ManagerLocator.getChampEntiteManager().findByIdManager(50);
								final Champ parent1 = new Champ(idEchan);
								parent1.setChampParent(parentToQueryEchantillon);

								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(3), current.getValue(), parent1, null,
										searchForProdDerives);
							}else{
								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(3), current.getValue(), null, null,
										searchForProdDerives);
							}
						}else{
							// exécution de la requête
							executeSimpleQueryForTextbox(current, parentToQueryEchantillon, null,  false);

						}
						oneValueEntered = true;
					}
				} else if(objEchantillonComponents[i].getClass().getSimpleName().equals("Listbox")){
					final Listbox current = (Listbox) objEchantillonComponents[i];
					// si une valeur a été saisie
					if(current.getSelectedIndex() > 0){
						if(!current.getId().equals("crAnapathFilebox")){
							critereOnEchantillon = true;
							// exécution de la requête
							executeSimpleQueryForListbox(current, parentToQueryEchantillon, null,  null);
						}else{ // filebox recherche
							executeFileQuery(current, getEntiteToSearch(), new Boolean((String) current.getSelectedItem().getValue()));
						}
						oneValueEntered = true;
					}
				} else if(objEchantillonComponents[i].getClass().getSimpleName().equals("Decimalbox")){
					final Decimalbox current = (Decimalbox) objEchantillonComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){
						critereOnEchantillon = true;
						String operateur = "";
						// si la requete porte sur la quantité
						if(current.getId().equals("tempStockEchantillonBox")){
							// si la requete porte sur le delai
							int idx = 0;
							if(operateursTempStockEchantillonBox.getSelectedIndex() > 0){
								idx = operateursTempStockEchantillonBox.getSelectedIndex();
							}
							operateur = (String) operateursTempStockEchantillonBox.getListModel().getElementAt(idx);

							executeTempStockQuery(current, operateur, entiteToSearch);
						}else{
							if(current.getId().equals("quantiteEchantillonBox")){
								int idx = 0;
								if(operateursQuantiteEchantillonBox.getSelectedIndex() > 0){
									idx = operateursQuantiteEchantillonBox.getSelectedIndex();
								}
								operateur = (String) operateursQuantiteEchantillonBox.getListModel().getElementAt(idx);
							}else if(current.getId().equals("delaiCglEchantillonBox")){
								// si la requete porte sur le delai
								int idx = 0;
								if(operateursDelaiCglEchantillonBox.getSelectedIndex() > 0){
									idx = operateursDelaiCglEchantillonBox.getSelectedIndex();
								}
								operateur = (String) operateursDelaiCglEchantillonBox.getListModel().getElementAt(idx);
							}

							// exécution de la requête
							executeSimpleQueryForDecimalbox(current, parentToQueryEchantillon, null,  operateur, false);
						}
						oneValueEntered = true;
					}
				} else if(objEchantillonComponents[i].getClass().getSimpleName().equals("CalendarBox")){
					final CalendarBox current = (CalendarBox) objEchantillonComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){
						critereOnEchantillon = true;
						String operateur = "";
						// si la requete porte sur la 1er date de stockage
						if(current.getId().equals("dateStock1Box")){
							int idx = 0;
							if(operateursDateStockBox.getSelectedIndex() > 0){
								idx = operateursDateStockBox.getSelectedIndex();
							}
							operateur = (String) operateursDateStockBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("dateStock2Box")){
							// si la requete porte sur la 2eme date de naissance
							int idx = 0;
							if(operateursDateStockBox.getSelectedIndex() > 0){
								idx = operateursDateStockBox.getSelectedIndex();
							}
							operateur = (String) operateursDateStockBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}

						// exécution de la requête
						if(operateur != null){
							executeSimpleQueryForCalendarbox(current, parentToQueryEchantillon, null, 
									operateur);
						}

						oneValueEntered = true;
					}
				} else if(objEchantillonComponents[i].getClass().getSimpleName().equals("Checkbox")){
               final Checkbox current = (Checkbox) objEchantillonComponents[i];
               // si une valeur a été saisie
               if(current.isChecked()){
                  if (("impactEchanTBox").equals(current.getId())){
                     critereOnEchantillon = true;
                     // execution de la requête
                     impacts.add(true);
                  }
                  if (("impactEchanFBox").equals(current.getId())){
                     critereOnEchantillon = true;
                     // execution de la requête
                     impacts.add(false);
                  }
                  final RechercheCompValues rcv = new RechercheCompValues();
                  rcv.setCompClass(Textbox.class);
                  rcv.setCompId(current.getId());
                  rcv.setCheckedValue(current.isChecked());
                  getUsedComponents().add(rcv);
                  //oneValueEntered = true;
               }
            }
			}
		}
      if(impacts.size() > 0){
         executeImpactQuery("Echantillon", impacts);
         oneValueEntered = true;
      }
	}

	/**
	 * Exécute les requêtes avec des critères sur les champs des dérivés.
	 */
	public void executeQueriesForProdDerives(){
		// pour chaque champ interrogeable pour les dérivés
	   final List<Boolean> impacts = new ArrayList<>();
		for(int i = 0; i < objProdDeriveComponents.length; i++){
			if(objProdDeriveComponents[i] != null){
				// si c'est un textbox
				if(objProdDeriveComponents[i].getClass().getSimpleName().equals("Textbox")){
					final Textbox current = (Textbox) objProdDeriveComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null && !current.getValue().equals("")){
						if(current.getId().equals("nonConformitesDeriveTraitementBox")){
							if(!entiteToSearch.getNom().equals("ProdDerive")){
								final ChampEntite idDerive = ManagerLocator.getChampEntiteManager().findByIdManager(76);
								final Champ parent1 = new Champ(idDerive);
								parent1.setChampParent(parent1ToQueryProdDerive);
								final Champ parent2 = new Champ(idDerive);
								parent2.setChampParent(parent2ToQueryProdDerive);

								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(4), current.getValue(), parent1, parent2,
										searchForProdDerives);
							}else{
								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(4), current.getValue(), null, null,
										searchForProdDerives);
							}

						}else if(current.getId().equals("nonConformitesDeriveCessionBox")){

							if(!entiteToSearch.getNom().equals("ProdDerive")){
								final ChampEntite idDerive = ManagerLocator.getChampEntiteManager().findByIdManager(76);
								final Champ parent1 = new Champ(idDerive);
								parent1.setChampParent(parent1ToQueryProdDerive);
								final Champ parent2 = new Champ(idDerive);
								parent2.setChampParent(parent2ToQueryProdDerive);

								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(5), current.getValue(), parent1, parent2,
										searchForProdDerives);
							}else{
								executeObjetByNonConformite(current, 
										ManagerLocator.getConformiteTypeManager().findByIdManager(5), current.getValue(), null, null,
										searchForProdDerives);
							}
						}else{
							// exécution de la requête
							executeDeriveQueryForTextbox(current);
						}

						oneValueEntered = true;
					}
				} else if(objProdDeriveComponents[i].getClass().getSimpleName().equals("Listbox")){
					final Listbox current = (Listbox) objProdDeriveComponents[i];
					// si une valeur a été saisie
					if(current.getSelectedIndex() > 0){
						// exécution de la requête
						executeDeriveQueryForListbox(current);

						oneValueEntered = true;
					}
				} else if(objProdDeriveComponents[i].getClass().getSimpleName().equals("Decimalbox")){
					final Decimalbox current = (Decimalbox) objProdDeriveComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){

						String operateur = "";
						if(current.getId().equals("tempStockDeriveBox")){
							// si la requete porte sur le delai
							int idx = 0;
							if(operateursTempStockDeriveBox.getSelectedIndex() > 0){
								idx = operateursTempStockDeriveBox.getSelectedIndex();
							}
							operateur = (String) operateursTempStockDeriveBox.getListModel().getElementAt(idx);

							executeTempStockQuery(current, operateur, entiteToSearch);
						}else{
							// si la requete porte sur la quantité
							if(current.getId().equals("quantiteDeriveBox")){
								int idx = 0;
								if(operateursQuantiteDeriveBox.getSelectedIndex() > 0){
									idx = operateursQuantiteDeriveBox.getSelectedIndex();
								}
								operateur = (String) operateursQuantiteDeriveBox.getListModel().getElementAt(idx);
							}else if(current.getId().equals("volumeDeriveBox")){
								// si la requete porte sur le delai
								int idx = 0;
								if(operateursVolumeDerivesBox.getSelectedIndex() > 0){
									idx = operateursVolumeDerivesBox.getSelectedIndex();
								}
								operateur = (String) operateursVolumeDerivesBox.getListModel().getElementAt(idx);
							}

							// exécution de la requête
							executeDeriveQueryForDecimalbox(current, operateur);
						}

						oneValueEntered = true;
					}
				} else if(objProdDeriveComponents[i].getClass().getSimpleName().equals("CalendarBox")){
					final CalendarBox current = (CalendarBox) objProdDeriveComponents[i];
					// si une valeur a été saisie
					if(current.getValue() != null){
						String operateur = "";
						// si la requete porte sur la 1er date de stockage
						if(current.getId().equals("dateStockDerive1Box")){
							int idx = 0;
							if(operateursDateStockDeriveBox.getSelectedIndex() > 0){
								idx = operateursDateStockDeriveBox.getSelectedIndex();
							}
							operateur = (String) operateursDateStockDeriveBox.getListModel().getElementAt(idx);
							if(operateur.equals("[..]")){
								operateur = ">=";
							}
						}else if(current.getId().equals("dateStockDerive2Box")){
							// si la requete porte sur la 2eme date de naissance
							int idx = 0;
							if(operateursDateStockDeriveBox.getSelectedIndex() > 0){
								idx = operateursDateStockDeriveBox.getSelectedIndex();
							}
							operateur = (String) operateursDateStockDeriveBox.getListModel().getElementAt(idx);

							if(!operateur.equals("[..]")){
								operateur = null;
							}else{
								operateur = "<=";
							}
						}

						// exécution de la requête
						if(operateur != null){
							executeDeriveQueryForCalendarbox(current, operateur);
						}

						oneValueEntered = true;
					}
				}else if(objProdDeriveComponents[i].getClass().getSimpleName().equals("Checkbox")){
               final Checkbox current = (Checkbox) objProdDeriveComponents[i];
               if(current.isChecked()){
                  if (("impactDeriveTBox").equals(current.getId())){
                     impacts.add(true);
                  }
                  if (("impactDeriveFBox").equals(current.getId())){
                     impacts.add(false);
                  }
                  final RechercheCompValues rcv = new RechercheCompValues();
                  rcv.setCompClass(Textbox.class);
                  rcv.setCompId(current.getId());
                  rcv.setCheckedValue(current.isChecked());
                  getUsedComponents().add(rcv);
               }
            }
         }
      }
      if(impacts.size() > 0){
         executeImpactQuery("ProdDerive", impacts);
         oneValueEntered = true;
      }
	}

	/**
	 * Exécute la requête permettant de récupérer des prlvts en fonction du
	 * nombre d'échantillons.
	 * 
	 * @param operateur
	 *            Opérateur de la requête.
	 * @param nbEchantillons
	 *            Nombre d'échantillons.
	 * @return La liste de résultats mise à jour.
	 */

	public void executeNbEchantillonsQuery(final String operateur, final Integer nbEchantillons){

		// on récupère la ou les banques sélectionnée(s)
		final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if(!otherQuery){
			resultatsIds = ManagerLocator.getTraitementQueryManager().findPrelevementsByNbEchantillonsWithBanquesManager(operateur,
					nbEchantillons, banques);
		}else{
			// sinon on fait une intersection des résultats de la
			// requête avec ceux déjà trouvés
			final List<Integer> objects = ManagerLocator.getTraitementQueryManager()
					.findPrelevementsByNbEchantillonsWithBanquesManager(operateur, nbEchantillons, banques);

			resultatsIds = ListUtils.intersection(resultatsIds, objects);
		}
		otherQuery = true;
	}

	/**
	 * Exécute la requête permettant de récupérer des prlvts en fonction de
	 * l'age du patient.
	 * 
	 * @param operateur
	 *            Opérateur de la requête.
	 * @param age
	 *            Age du patient.
	 * @return La liste de résultats mise à jour.
	 */

	public void executeAgeAuPrelevementQuery(final String operateur, final Integer age){

		// on récupère la ou les banques sélectionnée(s)
		final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);
		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if(!otherQuery){
			resultatsIds = ManagerLocator.getTraitementQueryManager().findPrelevementsByAgePatientWithBanquesManager(operateur,
					age, banques, SessionUtils.getDbms());
		}else{
			// sinon on fait une intersection des résultats de la
			// requête avec ceux déjà trouvés
			final List<Integer> objects = ManagerLocator.getTraitementQueryManager()
					.findPrelevementsByAgePatientWithBanquesManager(operateur, age, banques, SessionUtils.getDbms());

			resultatsIds = ListUtils.intersection(resultatsIds, objects);
		}
		otherQuery = true;
	}

	/**
	 * Exécute la requête permettant de récupérer tous prlvts prélevés par un
	 * medecin.
	 * 
	 * @param collab
	 *            .
	 * @return La liste de résultats mise à jour.
	 */

	public void executePrelevementsByMedecinQuery(final Collaborateur collab){

		// on récupère la ou les banques sélectionnée(s)
		final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if(!otherQuery){
			resultatsIds = ManagerLocator.getTraitementQueryManager().findPrelevementsByMedecinsManager(collab, banques);
		}else{
			// sinon on fait une intersection des résultats de la
			// requête avec ceux déjà trouvés
			final List<Integer> objects =
					ManagerLocator.getTraitementQueryManager().findPrelevementsByMedecinsManager(collab, banques);

			resultatsIds = ListUtils.intersection(resultatsIds, objects);
		}
		otherQuery = true;
	}

	/**
	 * Exécute la requête permettant de récupérer des prlvts en fonction d'une 
	 * information de site intermédiaire: Etablissement/Service/Prelevement
	 * 
	 * @param laboInter etablissement/service/collaborateur
	 * @apram component listbox/combobox
	 * @param selected index
	 * @since 2.2.1
	 */
	public void executePrelevementViaLaboInterQuery(final Object obj, 
			final Component current, final int selectedIndex){

		// on récupère la ou les banques sélectionnée(s)
		final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);
		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if(!otherQuery){
			resultatsIds = ManagerLocator.getTraitementQueryManager()
					.findPrelevementIdsViaLaboInterManager(obj, banques);
		}else{
			// sinon on fait une intersection des résultats de la
			// requête avec ceux déjà trouvés
			resultatsIds = ListUtils.intersection(resultatsIds, ManagerLocator.getTraitementQueryManager()
					.findPrelevementIdsViaLaboInterManager(obj, banques));
		}
		otherQuery = true;

		final RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(current.getClass());
		rcv.setCompId(current.getId());
		rcv.setSelectedIndexValue(selectedIndex);
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute la requête permettant de récupérer tous les objets à partir 
	 * d'une non conformité.
	 * Met à jour la liste des resultats ids.
	 * @param current
	 * @param cType
	 * @param p1
	 * @param p2
	 * @param searchForDerive
	 */

	public void executeObjetByNonConformite(final Textbox current, final ConformiteType cType,
			final String cNom, final Champ p1, final Champ p2, final boolean searchForDerive){

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		final List<Integer> objects = ManagerLocator.getTraitementQueryManager().findObjetIdsFromNonConformiteNomManager(cNom,
				cType, SessionUtils.getPlateforme(sessionScope), SessionUtils.getSelectedBanques(sessionScope));

		List<? extends Object> o1 = null;
		List<? extends Object> o2 = null;
		if(p1 != null && p1.getChampParent() != null && !p1.getChampParent().getChampEntite().equals(cType.getEntite())){
			final Critere c = new Critere(p1, "in", "");

			if(!searchForDerive && !p1.getChampEntite().getNom().matches(".*ProdDerive.*")){
				o1 = ManagerLocator.getTraitementQueryManager().findObjetByCritereWithBanquesManager(c,
						SessionUtils.getSelectedBanques(sessionScope), objects, true);
			}else{
				o1 = ManagerLocator.getTraitementQueryManager().findObjetByCritereWithBanquesDeriveVersionManager(c,
						SessionUtils.getSelectedBanques(sessionScope), objects, searchForDerive, true);
			}
		}
		if(p2 != null && p2.getChampParent() != null && !p2.getChampParent().getChampEntite().equals(cType.getEntite())){
			final Critere c = new Critere(p2, "in", "");

			if(!searchForDerive && !p2.getChampEntite().getNom().matches(".*ProdDerive.*")){
				o2 = ManagerLocator.getTraitementQueryManager().findObjetByCritereWithBanquesManager(c,
						SessionUtils.getSelectedBanques(sessionScope), objects, true);
			}else{
				o2 = ManagerLocator.getTraitementQueryManager().findObjetByCritereWithBanquesDeriveVersionManager(c,
						SessionUtils.getSelectedBanques(sessionScope), objects, searchForDerive, true);
			}
		}

		if(o1 != null || o2 != null){
			objects.clear();
			if(o1 != null){
				objects.addAll((Collection<? extends Integer>) o1);
			}
			if(o2 != null){
				objects.addAll((Collection<? extends Integer>) o2);
			}
		}

		if(!otherQuery){
			resultatsIds = objects;
		}else{
			resultatsIds = ListUtils.intersection(resultatsIds, objects);
		}
		otherQuery = true;
		final RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Textbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute la requête permettant de récupérer des prlvts en fonction du
	 * nombre d'échantillons.
	 * 
	 * @param operateur
	 *            Opérateur de la requête.
	 * @param nbEchantillons
	 *            Nombre d'échantillons.
	 * @return La liste de résultats mise à jour.
	 */

	public void executeCodesQuery(final Textbox current, final Champ parent1,
			final String entiteInEchantillon, final String entiteFinale, final boolean isMorpho){

		// on récupère la ou les banques sélectionnée(s)
		final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);

		final String value = current.getValue();
		final List<CodeCommon> liste = ManagerLocator.getTableCodageManager().findCodesAndTranscodesFromStringManager(value,
				getTablesForBanques(), banques, true);

		// value = value + "%";

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if(!searchForProdDerives){
			final Entite codesEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
			final ChampEntite champCodes =
					ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(codesEntite, entiteInEchantillon).get(0);
			final Champ tmp = new Champ(champCodes);
			tmp.setChampParent(parent1);

			final Entite codeAssigne = ManagerLocator.getEntiteManager().findByNomManager(entiteFinale).get(0);
			final ChampEntite champCode =
					ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(codeAssigne, "Code").get(0);
			final Champ champFinal = new Champ(champCode);
			champFinal.setChampParent(tmp);

			// création du critère
			final Critere critere = new Critere(champFinal, "", "");

			if(!otherQuery){
				resultatsIds = ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesManager(critere,
						banques, ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
						ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho);
			}else{
				// sinon on fait une intersection des résultats de la
				// requête avec ceux déjà trouvés
				final List<Integer> objects = ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesManager(
						critere, banques, ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
						ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho);

				resultatsIds = ListUtils.intersection(resultatsIds, objects);
			}
		}else{
			final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
			if(!otherQuery){
				resultatsIds = ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesDerivesVersionManager(
						banques, ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
						ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho, echanEntite);
			}else{
				// sinon on fait une intersection des résultats de la
				// requête avec ceux déjà trouvés
				final List<Integer> objects =
						ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesDerivesVersionManager(banques,
								ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
								ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho, echanEntite);

				resultatsIds = ListUtils.intersection(resultatsIds, objects);
			}
		}
		otherQuery = true;
		final RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Textbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute la requête permettant de récupérer tous TKStockableObjects 
	 * (échantillons ou dérivés) à partir d'une température de stockage
	 * medecin.
	 * @param Decimalbox imput
	 * @param String opérateur
	 * @param Entite du tkStockableObject
	 * 
	 * @since 2.0.13	       .
	 * @return La liste de résultats mise à jour.
	 */

	@SuppressWarnings("unchecked")
	public void executeTempStockQuery(final Decimalbox current, final String op, final Entite ent){

		// on récupère la ou les banques sélectionnée(s)
		final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if(!otherQuery){
			resultatsIds.addAll((List<Integer>) ManagerLocator.getTraitementQueryManager()
					.findTKStockableObjectsByTempStockWithBanquesManager(ent, current.getValue().floatValue(), op, banques, true));
		}else{
			// sinon on fait une intersection des résultats de la
			// requête avec ceux déjà trouvés
			final List<Integer> objects = (List<Integer>) ManagerLocator.getTraitementQueryManager()
					.findTKStockableObjectsByTempStockWithBanquesManager(ent, current.getValue().floatValue(), op, banques, true);

			resultatsIds = ListUtils.intersection(resultatsIds, objects);
		}
		otherQuery = true;

		// ajout du composant à l'historique
		final RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Decimalbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}
	
	/**
    * Exécute la requête permettant de récupérer tous les échantillons ayant une dégradation possible du matériel
    * @param impact
    * @return La liste de résultats mise à jour.
    */

   public void executeImpactQuery(final String nomEntite, List<Boolean> impact){
      final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);
      if ("Echantillon".equals(nomEntite)) {
         executeQueryForEntityToSearch(ManagerLocator.getEntiteManager().findByIdManager(3), ManagerLocator.getEchantillonManager().findByBanksAndImpact(banques, impact));
      }
      if ("ProdDerive".equals(nomEntite)) {
         executeQueryForEntityToSearch(ManagerLocator.getEntiteManager().findByIdManager(8), ManagerLocator.getProdDeriveManager().findByBanksAndImpact(banques, impact));
      }
      
   }

	@Override
	public void cloneObject(){}

	@Override
	public void createNewObject(){}

	@Override
	public void onClick$addNewC(){}

	@Override
	public void onClick$deleteC(){}

	@Override
	public void onClick$editC(){}

	@Override
	public void setEmptyToNulls(){}

	@Override
	public void switchToStaticMode(){

	}

	@Override
	public void updateObject(){

	}

	@Override
	public TKdataObject getObject(){
		return null;
	}

	@Override
	public Entite getEntiteToSearch(){
		return entiteToSearch;
	}

	@Override
	public void setEntiteToSearch(final Entite entite){
		this.entiteToSearch = entite;
	}

	@Override
	public String getPathToRespond(){
		return pathToRespond;
	}

	@Override
	public void setPathToRespond(final String path){
		this.pathToRespond = path;
	}

	@Override
	public boolean isAnonyme(){
		return anonyme;
	}

	@Override
	public void setAnonyme(final boolean ano){
		this.anonyme = ano;
	}

	public List<Nature> getNatures(){
		return natures;
	}

	public void setNatures(final List<Nature> n){
		this.natures = n;
	}

	public List<ConsentType> getConsentTypes(){
		return consentTypes;
	}

	public void setConsentTypes(final List<ConsentType> consentTs){
		this.consentTypes = consentTs;
	}

	public List<Service> getServices(){
		return services;
	}

	public void setServices(final List<Service> servs){
		this.services = servs;
	}

	public List<EchanQualite> getEchanQualites(){
		return echanQualites;
	}

	public void setEchanQualites(final List<EchanQualite> es){
		this.echanQualites = es;
	}

	public List<ObjetStatut> getObjetStatuts(){
		return objetStatuts;
	}

	public void setObjetStatuts(final List<ObjetStatut> objetSs){
		this.objetStatuts = objetSs;
	}

	public List<EchantillonType> getEchantillonTypes(){
		return echantillonTypes;
	}

	public void setEchantillonTypes(final List<EchantillonType> eTypes){
		this.echantillonTypes = eTypes;
	}

	public List<Etablissement> getEtablissements(){
		return etablissements;
	}

	public void setEtablissements(final List<Etablissement> etabs){
		this.etablissements = etabs;
	}

	public List<Collaborateur> getCollaborateurs(){
		return collaborateurs;
	}

	public void setCollaborateurs(final List<Collaborateur> cs){
		this.collaborateurs = cs;
	}

	public List<NonConformite> getNCarrivee(){
		return nCarrivee;
	}

	public void setNCarrivee(final List<NonConformite> n){
		this.nCarrivee = n;
	}

	public List<NonConformite> getNCechanTraitement(){
		return nCechanTraitement;
	}

	public void setNCechanTraitement(final List<NonConformite> t){
		this.nCechanTraitement = t;
	}

	public List<NonConformite> getNCechanCession(){
		return nCechanCession;
	}

	public void setNCechanCession(final List<NonConformite> t){
		this.nCechanCession = t;
	}

	public List<NonConformite> getNCderiveTraitement(){
		return nCderiveTraitement;
	}

	public void setNCderiveTraitement(final List<NonConformite> t){
		this.nCderiveTraitement = t;
	}

	public List<NonConformite> getNCderiveCession(){
		return nCderiveCession;
	}

	public void setNCderiveCession(final List<NonConformite> t){
		this.nCderiveCession = t;
	}

	@Override
	public List<String> getOperateursDecimaux(){
		return operateursDecimaux;
	}

	@Override
	public void setOperateursDecimaux(final List<String> opsDecimaux){
		this.operateursDecimaux = opsDecimaux;
	}

	@Override
	public List<String> getOperateursDates(){
		return operateursDates;
	}

	@Override
	public void setOperateursDates(final List<String> oDates){
		this.operateursDates = oDates;
	}

	public Champ getParent1ToQueryPatient(){
		return parent1ToQueryPatient;
	}

	public void setParent1ToQueryPatient(final Champ parentToQuery){
		this.parent1ToQueryPatient = parentToQuery;
	}

	public Champ getParent2ToQueryPatient(){
		return parent2ToQueryPatient;
	}

	public void setParent2ToQueryPatient(final Champ parentToQuery){
		this.parent2ToQueryPatient = parentToQuery;
	}

	public Champ getParent1ToQueryMaladie(){
		return parent1ToQueryMaladie;
	}

	public void setParent1ToQueryMaladie(final Champ parentToQuery){
		this.parent1ToQueryMaladie = parentToQuery;
	}

	public Champ getParent2ToQueryMaladie(){
		return parent2ToQueryMaladie;
	}

	public void setParent2ToQueryMaladie(final Champ parentToQuery){
		this.parent2ToQueryMaladie = parentToQuery;
	}

	public Champ getParent1ToQueryPrlvt(){
		return parent1ToQueryPrlvt;
	}

	public void setParent1ToQueryPrlvt(final Champ parent1ToQuery){
		this.parent1ToQueryPrlvt = parent1ToQuery;
	}

	public Champ getParentToQueryEchantillon(){
		return parentToQueryEchantillon;
	}

	public void setParentToQueryEchantillon(final Champ parentToQuery){
		this.parentToQueryEchantillon = parentToQuery;
	}

	@Override
	public boolean isOneValueEntered(){
		return oneValueEntered;
	}

	@Override
	public void setOneValueEntered(final boolean oneValue){
		this.oneValueEntered = oneValue;
	}

	@Override
	public List<Object> getResultats(){
		return resultats;
	}

	@Override
	public void setResultats(final List<Object> res){
		this.resultats = res;
	}

	@Override
	public boolean isAnnotationAlreadyOpen(){
		return annotationAlreadyOpen;
	}

	@Override
	public void setAnnotationAlreadyOpen(final boolean aOpen){
		this.annotationAlreadyOpen = aOpen;
	}

	@Override
	public Champ getParent1ToQueryProdDerive(){
		return parent1ToQueryProdDerive;
	}

	@Override
	public void setParent1ToQueryProdDerive(final Champ parent1ProdDerive){
		this.parent1ToQueryProdDerive = parent1ProdDerive;
	}

	@Override
	public Champ getParent2ToQueryProdDerive(){
		return parent2ToQueryProdDerive;
	}

	@Override
	public void setParent2ToQueryProdDerive(final Champ parent2ProdDerive){
		this.parent2ToQueryProdDerive = parent2ProdDerive;
	}

	@Override
	public boolean isSearchForProdDerives(){
		return searchForProdDerives;
	}

	@Override
	public void setSearchForProdDerives(final boolean searchDerives){
		this.searchForProdDerives = searchDerives;
	}

	public List<ProdType> getProdDeriveTypes(){
		return prodDeriveTypes;
	}

	public void setProdDeriveTypes(final List<ProdType> prodTypes){
		this.prodDeriveTypes = prodTypes;
	}

	public List<ProdQualite> getDeriveQualites(){
		return deriveQualites;
	}

	public void setDeriveQualites(final List<ProdQualite> dQualites){
		this.deriveQualites = dQualites;
	}

	public Champ getParent2ToQueryPrlvt(){
		return parent2ToQueryPrlvt;
	}

	public void setParent2ToQueryPrlvt(final Champ parent2ToQuery){
		this.parent2ToQueryPrlvt = parent2ToQuery;
	}

	public List<ModePrepa> getModePrepas(){
		return modePrepas;
	}

	public void setModePrepas(final List<ModePrepa> mPrepas){
		this.modePrepas = mPrepas;
	}

	@Override
	public List<Critere> getCriteresStandards(){
		return criteresStandards;
	}

	@Override
	public void setCriteresStandards(final List<Critere> criteres){
		this.criteresStandards = criteres;
	}

	@Override
	public List<Object> getValeursStandards(){
		return valeursStandards;
	}

	@Override
	public void setValeursStandards(final List<Object> valeurs){
		this.valeursStandards = valeurs;
	}

	@Override
	public boolean isOtherQuery(){
		return otherQuery;
	}

	@Override
	public void setOtherQuery(final boolean other){
		this.otherQuery = other;
	}

	public boolean isCritereOnEchantillon(){
		return critereOnEchantillon;
	}

	public void setCritereOnEchantillon(final boolean critereOnE){
		this.critereOnEchantillon = critereOnE;
	}

	@Override
	public List<Critere> getCriteresDerives1(){
		return criteresDerives1;
	}

	@Override
	public void setCriteresDerives1(final List<Critere> criteres){
		this.criteresDerives1 = criteres;
	}

	@Override
	public List<Object> getValeursDerives1(){
		return valeursDerives1;
	}

	@Override
	public void setValeursDerives1(final List<Object> valeurs){
		this.valeursDerives1 = valeurs;
	}

	@Override
	public List<Critere> getCriteresDerives2(){
		return criteresDerives2;
	}

	@Override
	public void setCriteresDerives2(final List<Critere> criteres){
		this.criteresDerives2 = criteres;
	}

	@Override
	public List<Object> getValeursDerives2(){
		return valeursDerives2;
	}

	@Override
	public void setValeursDerives2(final List<Object> valeurs){
		this.valeursDerives2 = valeurs;
	}

	@Override
	public void setFocusOnElement(){}

	@Override
	public void setFieldsToUpperCase(){}

	@Override
	public TKdataObject getParentObject(){
		return null;
	}

	@Override
	public void setParentObject(final TKdataObject obj){}

	@Override
	public String getDeleteWaitLabel(){
		return null;
	}

	@Override
	public boolean prepareDeleteObject(){
		return false;
	}

	@Override
	public void removeObject(final String comments){}

	/**
	 * Exécute les requêtes avec des critères sur les champs des prlvts pour le
	 * contexte associé à la banque courante.
	 */
	protected void executeQueriesForPrelevementsContext(){}

	/**
	 * Exécute les requêtes avec des critères sur les champs des patients pour le
	 * contexte associé à la banque courante.
	 */
	protected void executeQueriesForPatientsContext(){}

	/**
	 * Exécute les requêtes avec des critères sur les champs des patients pour le
	 * contexte associé à la banque courante.
	 */
	protected void executeQueriesForMaladiesContext(){}

	/***************************** listbox helpers ****************************/
	public void onSelect$nonConformitesArriveeBoxHelper(){
		if(nonConformitesArriveeBoxHelper.getSelectedIndex() > 0){
			nonConformitesArriveeBox.setValue(getNCarrivee().get(nonConformitesArriveeBoxHelper.getSelectedIndex()).getNom());
		}else{
			nonConformitesArriveeBox.setValue(null);
		}
	}

	public void onSelect$nonConformitesEchanTraitementBoxHelper(){
		if(nonConformitesEchanTraitementBoxHelper.getSelectedIndex() > 0){
			nonConformitesEchanTraitementBox
			.setValue(getNCechanTraitement().get(nonConformitesEchanTraitementBoxHelper.getSelectedIndex()).getNom());
		}else{
			nonConformitesEchanTraitementBox.setValue(null);
		}
	}

	public void onSelect$nonConformitesEchanCessionBoxHelper(){
		if(nonConformitesEchanCessionBoxHelper.getSelectedIndex() > 0){
			nonConformitesEchanCessionBox
			.setValue(getNCechanCession().get(nonConformitesEchanCessionBoxHelper.getSelectedIndex()).getNom());
		}else{
			nonConformitesEchanCessionBox.setValue(null);
		}
	}

	public void onSelect$nonConformitesDeriveTraitementBoxHelper(){
		if(nonConformitesDeriveTraitementBoxHelper.getSelectedIndex() > 0){
			nonConformitesDeriveTraitementBox
			.setValue(getNCderiveTraitement().get(nonConformitesDeriveTraitementBoxHelper.getSelectedIndex()).getNom());
		}else{
			nonConformitesDeriveTraitementBox.setValue(null);
		}
	}

	public void onSelect$nonConformitesDeriveCessionBoxHelper(){
		if(nonConformitesDeriveCessionBoxHelper.getSelectedIndex() > 0){
			nonConformitesDeriveCessionBox
			.setValue(getNCderiveCession().get(nonConformitesDeriveCessionBoxHelper.getSelectedIndex()).getNom());
		}else{
			nonConformitesDeriveCessionBox.setValue(null);
		}
	}

	public Boolean getGroupPatientsOpened(){
		return groupPatientsOpened;
	}

	@Override
	public void setGroupPatientsOpened(final Boolean g){
		this.groupPatientsOpened = g;
	}

	public Boolean getGroupMaladiesOpened(){
		return groupMaladiesOpened;
	}

	@Override
	public void setGroupMaladiesOpened(final Boolean g){
		this.groupMaladiesOpened = g;
	}

	public Boolean getGroupPrelevementsOpened(){
		return groupPrelevementsOpened;
	}

	@Override
	public void setGroupPrelevementsOpened(final Boolean g){
		this.groupPrelevementsOpened = g;
	}

	public Boolean getGroupLaboIntersOpened(){
		if (groupLaboInters != null) {
			return groupLaboIntersOpened;
		}
		return false;
	}

	@Override
	public void setGroupLaboIntersOpened(final Boolean g){
		if (groupLaboInters != null) {
			this.groupLaboIntersOpened = g;
		}
	}

	public Boolean getGroupEchantillonsOpened(){
		return groupEchantillonsOpened;
	}

	@Override
	public void setGroupEchantillonsOpened(final Boolean g){
		this.groupEchantillonsOpened = g;
	}

	public Boolean getGroupProdDerivesOpened(){
		return groupProdDerivesOpened;
	}

	@Override
	public void setGroupProdDerivesOpened(final Boolean g){
		this.groupProdDerivesOpened = g;
	}

	@Override
	public List<String> getOpenedGroups(){
		final List<String> openedGroups = new ArrayList<>();
		if(groupPatients != null && groupPatientsOpened){
			openedGroups.add("groupPatients");
		}
		if(groupMaladies != null && groupMaladiesOpened){
			openedGroups.add("groupMaladies");
		}
		if(groupPrelevements != null && groupPrelevementsOpened){
			openedGroups.add("groupPrelevements");
		}
		if(groupLaboInters != null && groupLaboIntersOpened){
			openedGroups.add("groupLaboInters");
		}
		if(groupEchantillons != null && groupEchantillonsOpened){
			openedGroups.add("groupEchantillons");
		}
		if(groupProdDerives != null && groupProdDerivesOpened){
			openedGroups.add("groupProdDerives");
		}
		return openedGroups;
	}

	public ListModelList<Risque> getRisquesModel(){
		return risquesModel;
	}

	public SimpleListModel<Service> getServicesLabo() {
		return servicesLabo;
	}

	public SimpleListModel<Etablissement> getEtablissementsLabo() {
		return etablissementsLabo;
	}

	public SimpleListModel<Collaborateur> getCollaborateursLabo() {
		return collaborateursLabo;
	}

}
