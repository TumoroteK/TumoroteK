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
package fr.aphp.tumorotek.action;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.web.context.ContextLoader;

import fr.aphp.tumorotek.interfacage.sender.SenderFactory;
import fr.aphp.tumorotek.interfacage.sgl.view.ViewHandlerFactory;
import fr.aphp.tumorotek.manager.code.AdicapManager;
import fr.aphp.tumorotek.manager.code.CimMasterManager;
import fr.aphp.tumorotek.manager.code.CimoMorphoManager;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.code.CodeDossierManager;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.code.CommonUtilsManager;
import fr.aphp.tumorotek.manager.code.TableCodageManager;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.CatalogueManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.annotation.DataTypeManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionExamenManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionStatutManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionTypeManager;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.coeur.cession.DestructionMotifManager;
import fr.aphp.tumorotek.manager.coeur.cession.ProtocoleTypeManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchanQualiteManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonTypeManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonValidator;
import fr.aphp.tumorotek.manager.coeur.echantillon.ModePrepaManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientValidator;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConditMilieuManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConditTypeManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConsentTypeManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterValidator;
import fr.aphp.tumorotek.manager.coeur.prelevement.NatureManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementTypeManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementValidator;
import fr.aphp.tumorotek.manager.coeur.prelevement.RisqueManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ModePrepaDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdQualiteManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdTypeManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.CategorieManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.ContexteManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.EtablissementManager;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.context.ProtocoleManager;
import fr.aphp.tumorotek.manager.context.ServiceManager;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.context.TitreManager;
import fr.aphp.tumorotek.manager.context.TransporteurManager;
import fr.aphp.tumorotek.manager.dto.EchantillonDTOManager;
import fr.aphp.tumorotek.manager.etiquettes.TumoBarcodePrinter;
import fr.aphp.tumorotek.manager.etiquettes.TumoPrinterUtilsManager;
import fr.aphp.tumorotek.manager.impression.BlocImpressionManager;
import fr.aphp.tumorotek.manager.impression.BlocImpressionTemplateManager;
import fr.aphp.tumorotek.manager.impression.ChampEntiteBlocManager;
import fr.aphp.tumorotek.manager.impression.ChampImprimeManager;
import fr.aphp.tumorotek.manager.impression.TableAnnotationTemplateManager;
import fr.aphp.tumorotek.manager.impression.TemplateManager;
import fr.aphp.tumorotek.manager.imprimante.AffectationImprimanteManager;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.ImprimanteApiManager;
import fr.aphp.tumorotek.manager.imprimante.ImprimanteManager;
import fr.aphp.tumorotek.manager.imprimante.LigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.ModeleManager;
import fr.aphp.tumorotek.manager.imprimante.ModeleTypeManager;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.ConsultationIntfManager;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.EmetteurManager;
import fr.aphp.tumorotek.manager.interfacage.InjectionManager;
import fr.aphp.tumorotek.manager.interfacage.PatientSipManager;
import fr.aphp.tumorotek.manager.interfacage.RecepteurManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.interfacage.scan.ScanTerminaleManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.CritereManager;
import fr.aphp.tumorotek.manager.io.export.ExportUtils;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.manager.io.export.RechercheManager;
import fr.aphp.tumorotek.manager.io.export.RequeteManager;
import fr.aphp.tumorotek.manager.io.export.ResultatManager;
import fr.aphp.tumorotek.manager.io.export.standard.ExportCatalogueManager;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.io.imports.ImportManager;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceIdManager;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementQueryManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementRequeteManager;
import fr.aphp.tumorotek.manager.qualite.ConformiteTypeManager;
import fr.aphp.tumorotek.manager.qualite.NonConformiteManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.report.IncaReportManager;
import fr.aphp.tumorotek.manager.stats.GraphesModeleManager;
import fr.aphp.tumorotek.manager.stats.IndicateurManager;
import fr.aphp.tumorotek.manager.stats.SModeleManager;
import fr.aphp.tumorotek.manager.stats.SubdivisionManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurTypeManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteTypeManager;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleNumerotationManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleTypeManager;
import fr.aphp.tumorotek.manager.systeme.CouleurEntiteTypeManager;
import fr.aphp.tumorotek.manager.systeme.CouleurManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.systeme.NumerotationManager;
import fr.aphp.tumorotek.manager.systeme.TemperatureManager;
import fr.aphp.tumorotek.manager.systeme.UniteManager;
import fr.aphp.tumorotek.manager.systeme.VersionManager;
import fr.aphp.tumorotek.manager.utilisateur.DroitObjetManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilUtilisateurManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.manager.validation.coeur.cession.retour.RetourValidator;
import fr.aphp.tumorotek.manager.xml.XmlUtils;
import fr.aphp.tumorotek.model.bundles.ResourceBundleMbio;
import fr.aphp.tumorotek.model.bundles.ResourceBundleSip;
import fr.aphp.tumorotek.model.bundles.ResourceBundleTumo;

//import fr.aphp.tumorotek.manager.code.CodeDossierManager;

public final class ManagerLocator {

	// private static final Log logger =
	// LogFactory.getLog(ManagerLocator.class);

	/*
	 * Constructeur
	 */
	private ManagerLocator() {
	}

	public static JpaTransactionManager getTxManager() {
		return (JpaTransactionManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("txManager");
	}

	public static RisqueManager getRisqueManager() {
		return (RisqueManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("risqueManager");
	}

	/* Managers du package contexte */
	public static CollaborateurManager getCollaborateurManager() {
		return (CollaborateurManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("collaborateurManager");
	}

	public static TitreManager getTitreManager() {
		return (TitreManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("titreManager");
	}

	public static SpecialiteManager getSpecialiteManager() {
		return (SpecialiteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("specialiteManager");
	}

	public static EtablissementManager getEtablissementManager() {
		return (EtablissementManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("etablissementManager");
	}

	public static ServiceManager getServiceManager() {
		return (ServiceManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("serviceManager");
	}

	public static BanqueManager getBanqueManager() {
		return (BanqueManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("banqueManager");
	}

	public static TransporteurManager getTransporteurManager() {
		return (TransporteurManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("transporteurManager");
	}

	public static CategorieManager getCategorieManager() {
		return (CategorieManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("categorieManager");
	}

	public static CoordonneeManager getCoordonneeManager() {
		return (CoordonneeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("coordonneeManager");
	}

	public static PlateformeManager getPlateformeManager() {
		return (PlateformeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("plateformeManager");
	}

	/* Managers du package patient */
	public static PatientManager getPatientManager() {
		return (PatientManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("patientManager");
	}

	public static PatientValidator getPatientValidator() {
		return (PatientValidator) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("patientValidator");
	}

	public static MaladieManager getMaladieManager() {
		return (MaladieManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("maladieManager");
	}

	/* Managers du package echantillon */
	public static EchantillonManager getEchantillonManager() {
		return (EchantillonManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("echantillonManager");
	}

	public static EchantillonValidator getEchantillonValidator() {
		return (EchantillonValidator) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("echantillonValidator");
	}

	public static EchantillonTypeManager getEchantillonTypeManager() {
		return (EchantillonTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("echantillonTypeManager");
	}

	public static EchanQualiteManager getEchanQualiteManager() {
		return (EchanQualiteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("echanQualiteManager");
	}

	public static ModePrepaManager getModePrepaManager() {
		return (ModePrepaManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("modePrepaManager");
	}

	public static FichierManager getCrAnapathManager() {
		return (FichierManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("fichierManager");
	}

	/* Managers du package divers */
	public static EntiteManager getEntiteManager() {
		return (EntiteManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("entiteManager");
	}

	public static UniteManager getUniteManager() {
		return (UniteManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("uniteManager");
	}

	public static ObjetStatutManager getObjetStatutManager() {
		return (ObjetStatutManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("objetStatutManager");
	}

	/* Package prodderive */
	public static ProdDeriveManager getProdDeriveManager() {
		return (ProdDeriveManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("prodDeriveManager");
	}

	public static ProdDeriveValidator getProdDeriveValidator() {
		return (ProdDeriveValidator) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("prodDeriveValidator");
	}

	public static ProdTypeManager getProdTypeManager() {
		return (ProdTypeManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("prodTypeManager");
	}

	public static ProdQualiteManager getProdQualiteManager() {
		return (ProdQualiteManager) ContextLoader
				.getCurrentWebApplicationContext()
				.getBean("prodQualiteManager");
	}

	public static TransformationManager getTransformationManager() {
		return (TransformationManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("transformationManager");
	}

	public static ModePrepaDeriveManager getModePrepaDeriveManager() {
		return (ModePrepaDeriveManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("modePrepaDeriveManager");
	}

	/* Managers du package utilisateur */
	public static UtilisateurManager getUtilisateurManager() {
		return (UtilisateurManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("utilisateurManager");
	}

	public static ProfilManager getProfilManager() {
		return (ProfilManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("profilManager");
	}

	public static ProfilUtilisateurManager getProfilUtilisateurManager() {
		return (ProfilUtilisateurManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("profilUtilisateurManager");
	}

	public static DroitObjetManager getDroitObjetManager() {
		return (DroitObjetManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("droitObjetManager");
	}

	/* Managers du package prelevement */
	public static PrelevementManager getPrelevementManager() {
		return (PrelevementManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("prelevementManager");
	}

	public static PrelevementValidator getPrelevementValidator() {
		return (PrelevementValidator) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("prelevementValidator");
	}

	public static LaboInterValidator getLaboInterValidator() {
		return (LaboInterValidator) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("laboInterValidator");
	}

	public static NatureManager getNatureManager() {
		return (NatureManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("natureManager");
	}

	public static PrelevementTypeManager getPrelevementTypeManager() {
		return (PrelevementTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("prelevementTypeManager");
	}

	public static ConditTypeManager getConditTypeManager() {
		return (ConditTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("conditTypeManager");
	}

	public static ConditMilieuManager getConditMilieuManager() {
		return (ConditMilieuManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("conditMilieuManager");
	}

	public static ConsentTypeManager getConsentTypeManager() {
		return (ConsentTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("consentTypeManager");
	}

	public static LaboInterManager getLaboInterManager() {
		return (LaboInterManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("laboInterManager");
	}

	/* Managers du package qualite */
	public static OperationManager getOperationManager() {
		return (OperationManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("operationManager");
	}

	public static OperationTypeManager getOperationTypeManager() {
		return (OperationTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("operationTypeManager");
	}

	public static ConformiteTypeManager getConformiteTypeManager() {
		return (ConformiteTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("conformiteTypeManager");
	}

	public static NonConformiteManager getNonConformiteManager() {
		return (NonConformiteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("nonConformiteManager");
	}

	public static ObjetNonConformeManager getObjetNonConformeManager() {
		return (ObjetNonConformeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("objetNonConformeManager");
	}

	/* Managers du package io */
	public static ChampEntiteManager getChampEntiteManager() {
		return (ChampEntiteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("champEntiteManager");
	}

	public static ChampManager getChampManager() {
		return (ChampManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("champManager");
	}

	public static RechercheManager getRechercheManager() {
		return (RechercheManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("rechercheManager");
	}

	public static AffichageManager getAffichageManager() {
		return (AffichageManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("affichageManager");
	}

	public static ResultatManager getResultatManager() {
		return (ResultatManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("resultatManager");
	}

	public static RequeteManager getRequeteManager() {
		return (RequeteManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("requeteManager");
	}

	public static GroupementManager getGroupementManager() {
		return (GroupementManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("groupementManager");
	}

	public static CritereManager getCritereManager() {
		return (CritereManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("critereManager");
	}

	public static ExportUtils getExportUtils() {
		return (ExportUtils) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("exportUtils");
	}

	/* Managers du package io utils */
	public static CorrespondanceManager getCorrespondanceManager() {
		return (CorrespondanceManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("correspondanceManager");
	}
	
	public static CorrespondanceIdManager getCorrespondanceIdManager() {
		return (CorrespondanceIdManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("correspondanceIdManager");
	}

	public static TraitementRequeteManager getTraitementRequeteManager() {
		return (TraitementRequeteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("traitementRequeteManager");
	}

	public static TraitementQueryManager getTraitementQueryManager() {
		return (TraitementQueryManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("traitementQueryManager");
	}

	/* Managers du package cession */
	public static CederObjetManager getCederObjetManager() {
		return (CederObjetManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("cederObjetManager");
	}

	public static CessionManager getCessionManager() {
		return (CessionManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("cessionManager");
	}

	public static CessionExamenManager getCessionExamenManager() {
		return (CessionExamenManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("cessionExamenManager");
	}

	public static CessionStatutManager getCessionStatutManager() {
		return (CessionStatutManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("cessionStatutManager");
	}

	public static CessionTypeManager getCessionTypeManager() {
		return (CessionTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("cessionTypeManager");
	}

	public static DestructionMotifManager getDestructionMotifManager() {
		return (DestructionMotifManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("destructionMotifManager");
	}

	public static ContratManager getContratManager() {
		return (ContratManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("contratManager");
	}

	public static ProtocoleTypeManager getProtocoleTypeManager() {
		return (ProtocoleTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("protocoleTypeManager");
	}

	/* Managers du package annotation */
	public static TableAnnotationManager getTableAnnotationManager() {
		return (TableAnnotationManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("tableAnnotationManager");
	}

	public static DataTypeManager getDataTypeManager() {
		return (DataTypeManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("dataTypeManager");
	}

	public static ChampAnnotationManager getChampAnnotationManager() {
		return (ChampAnnotationManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("champAnnotationManager");
	}

	public static AnnotationValeurManager getAnnotationValeurManager() {
		return (AnnotationValeurManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("annotationValeurManager");
	}

	public static CatalogueManager getCatalogueManager() {
		return (CatalogueManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("catalogueManager");
	}

	/***************** Package systeme. ************/

	public static FichierManager getFichierManager() {
		return (FichierManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("fichierManager");
	}

	public static CouleurEntiteTypeManager getCouleurEntiteTypeManager() {
		return (CouleurEntiteTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("couleurEntiteTypeManager");
	}

	public static TemperatureManager getTemperatureManager() {
		return (TemperatureManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("temperatureManager");
	}

	public static NumerotationManager getNumerotationManager() {
		return (NumerotationManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("numerotationManager");
	}

	public static VersionManager getVersionManager() {
		return (VersionManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("versionManager");
	}

	/***************** Package stockage. *********************/

	public static ConteneurManager getConteneurManager() {
		return (ConteneurManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("conteneurManager");
	}

	public static ConteneurTypeManager getConteneurTypeManager() {
		return (ConteneurTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("conteneurTypeManager");
	}

	public static EmplacementManager getEmplacementManager() {
		return (EmplacementManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("emplacementManager");
	}

	public static EnceinteManager getEnceinteManager() {
		return (EnceinteManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("enceinteManager");
	}

	public static EnceinteTypeManager getEnceinteTypeManager() {
		return (EnceinteTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("enceinteTypeManager");
	}

	public static IncidentManager getIncidentManager() {
		return (IncidentManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("incidentManager");
	}

	public static TerminaleManager getTerminaleManager() {
		return (TerminaleManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("terminaleManager");
	}

	public static TerminaleNumerotationManager getTerminaleNumerotationManager() {
		return (TerminaleNumerotationManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("terminaleNumerotationManager");
	}

	public static TerminaleTypeManager getTerminaleTypeManager() {
		return (TerminaleTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("terminaleTypeManager");
	}

	/***************** Package code. *********************/

	public static AdicapManager getAdicapManager() {
		return (AdicapManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("adicapManager");
	}

	public static CimMasterManager getCimMasterManager() {
		return (CimMasterManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("cimMasterManager");
	}

	public static CimoMorphoManager getCimoMorphoManager() {
		return (CimoMorphoManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("cimoMorphoManager");
	}

	public static TableCodageManager getTableCodageManager() {
		return (TableCodageManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("tableCodageManager");
	}

	public static CommonUtilsManager getCommonUtilsManager() {
		return (CommonUtilsManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("commonUtilsManager");
	}

	public static CodeUtilisateurManager getCodeUtilisateurManager() {
		return (CodeUtilisateurManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("codeUtilisateurManager");
	}

	public static CodeDossierManager getCodeDossierManager() {
		return (CodeDossierManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("codeDossierManager");
	}

	public static CodeAssigneManager getCodeAssigneManager() {
		return (CodeAssigneManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("codeAssigneManager");
	}

	public static CodeSelectManager getCodeSelectManager() {
		return (CodeSelectManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("codeSelectManager");
	}

	/*************** Package XML. ********************/
	public static XmlUtils getXmlUtils() {
		return (XmlUtils) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("xmlUtils");
	}

	/*************** Package Impression. ********************/
	public static BlocImpressionManager getBlocImpressionManager() {
		return (BlocImpressionManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("blocImpressionManager");
	}

	public static BlocImpressionTemplateManager getBlocImpressionTemplateManager() {
		return (BlocImpressionTemplateManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("blocImpressionTemplateManager");
	}

	public static ChampEntiteBlocManager getChampEntiteBlocManager() {
		return (ChampEntiteBlocManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("champEntiteBlocManager");
	}

	public static ChampImprimeManager getChampImprimeManager() {
		return (ChampImprimeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("champImprimeManager");
	}

	public static TableAnnotationTemplateManager getTableAnnotationTemplateManager() {
		return (TableAnnotationTemplateManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("tableAnnotationTemplateManager");
	}

	public static TemplateManager getTemplateManager() {
		return (TemplateManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("templateManager");
	}

	public static CouleurManager getCouleurManager() {
		return (CouleurManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("couleurManager");
	}

	public static ContexteManager getContexteManager() {
		return (ContexteManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("contexteManager");
	}

	public static IncaReportManager getIncaReportManager() {
		return (IncaReportManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("incaReportManager");
	}

	/****************** Etiquettes. ************************/
	public static TumoBarcodePrinter getTumoBarcodePrinter() {
		return (TumoBarcodePrinter) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("tumoBarcodePrinter");
	}

	public static TumoPrinterUtilsManager getTumoPrinterUtilsManager() {
		return (TumoPrinterUtilsManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("tumoPrinterUtilsManager");
	}

	/****************** Import. ************************/
	public static ImportManager getImportManager() {
		return (ImportManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("importManager");
	}

	public static ImportTemplateManager getImportTemplateManager() {
		return (ImportTemplateManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("importTemplateManager");
	}

	public static ImportColonneManager getImportColonneManager() {
		return (ImportColonneManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("importColonneManager");
	}

	public static ImportHistoriqueManager getImportHistoriqueManager() {
		return (ImportHistoriqueManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("importHistoriqueManager");
	}

	/****************** Imprimante. ************************/
	public static ImprimanteManager getImprimanteManager() {
		return (ImprimanteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("imprimanteManager");
	}

	public static ImprimanteApiManager getImprimanteApiManager() {
		return (ImprimanteApiManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("imprimanteApiManager");
	}

	public static ModeleManager getModeleManager() {
		return (ModeleManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("modeleManager");
	}

	public static ModeleTypeManager getModeleTypeManager() {
		return (ModeleTypeManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("modeleTypeManager");
	}

	public static AffectationImprimanteManager getAffectationImprimanteManager() {
		return (AffectationImprimanteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("affectationImprimanteManager");
	}

	public static LigneEtiquetteManager getLigneEtiquetteManager() {
		return (LigneEtiquetteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("ligneEtiquetteManager");
	}

	public static ChampLigneEtiquetteManager getChampLigneEtiquetteManager() {
		return (ChampLigneEtiquetteManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("champLigneEtiquetteManager");
	}

	public static RetourManager getRetourManager() {
		return (RetourManager) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("retourManager");
	}
	
	public static RetourValidator getRetourValidator() {
		return (RetourValidator) (ContextLoader.getCurrentWebApplicationContext())
				.getBean("retourValidator");
	}

	/*************** Package Bundle. ********************/

	public static ResourceBundleMbio getResourceBundleMbio() {
		return (ResourceBundleMbio) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("resourceBundleMbio");
	}

	public static ResourceBundleSip getResourceBundleSip() {
		return (ResourceBundleSip) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("resourceBundleSip");
	}

	public static ResourceBundleTumo getResourceBundleTumo() {
		return (ResourceBundleTumo) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("resourceBundleTumo");
	}

	/************* Export Catalogue. ****************/
	public static ExportCatalogueManager getExportINCaManager() {
		return (ExportCatalogueManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("exportINCaManager");
	}

	public static ExportCatalogueManager getExportTVGSOManager() {
		return (ExportCatalogueManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("exportTVGSOManager");
	}

	/*************** Package Interfacage. ********************/

	public static EmetteurManager getEmetteurManager() {
		return (EmetteurManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("emetteurManager");
	}
	
	public static RecepteurManager getRecepteurManager() {
		return (RecepteurManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("recepteurManager");
	}


	public static DossierExterneManager getDossierExterneManager() {
		return (DossierExterneManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("dossierExterneManager");
	}

	public static BlocExterneManager getBlocExterneManager() {
		return (BlocExterneManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("blocExterneManager");
	}

	public static ValeurExterneManager getValeurExterneManager() {
		return (ValeurExterneManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("valeurExterneManager");
	}

	public static InjectionManager getInjectionManager() {
		return (InjectionManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("injectionManager");
	}

	public static PatientSipManager getPatientSipManager() {
		return (PatientSipManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("patientSipManager");
	}
	
	public static ConsultationIntfManager getConsultationIntfManager() {
		return (ConsultationIntfManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("consultationIntfManager");
	}
	
	// la classe est packagée dans Tumo2-interface et n'est donc pas présente 
	// pour toutes les installations.
	public static SenderFactory getSenderFactory() {
		if ((ContextLoader
				.getCurrentWebApplicationContext()).containsBean("senderFactory")) {
			return (SenderFactory) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("senderFactory");	
		} 
		return null;
	}
	
	public static ProtocoleManager getProtocoleManager() {
		return (ProtocoleManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("protocoleManager");
	}

	public static IndicateurManager getIndicateurManager() {
		return (IndicateurManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("indicateurManager");
	}

	public static SubdivisionManager getSubdivisionManager() {
		return (SubdivisionManager) (ContextLoader
				.getCurrentWebApplicationContext()).getBean("subdivisionManager");
	}
	
	public static SModeleManager getSModeleManager() {
		return (SModeleManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("sModeleManager");
	}
	
	public static GraphesModeleManager getGraphesModeleManager() {
		return (GraphesModeleManager) (ContextLoader
			.getCurrentWebApplicationContext())
			.getBean("graphesModeleManager");
	}
	
	public static ScanTerminaleManager getScanTerminaleManager() {
		return (ScanTerminaleManager) (ContextLoader
			.getCurrentWebApplicationContext())
			.getBean("scanTerminaleManager");
	}
	
	/**
	 * @since 2.1
	 * @return ViewHandlerFactory
	 */
	public static ViewHandlerFactory getViewHandlerFactory() {
		return (ViewHandlerFactory) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("viewHandlerFactory");
	}
	
	/********************* DTO Managers *************************/
	public static EchantillonDTOManager getEchantillonDTOManager() {
		return (EchantillonDTOManager) (ContextLoader
				.getCurrentWebApplicationContext())
				.getBean("echantillonDTOManager");
	}
}
