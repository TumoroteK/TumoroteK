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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.helper.FileBatch;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Controller gérant la fiche de création de
 * plusieurs échantillons.
 * Controller créé le 26/11/2009.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class FicheMultiEchantillons extends FicheEchantillonEdit {
	
	private Log log = LogFactory.getLog(FicheMultiEchantillons.class);

	private static final long serialVersionUID = 3863329092781960062L;
		
	// Buttons
	private Button previous;
	
	// Editable components : mode d'édition ou de création.
	private Label operateurAideSaisieEchan;
	private Combobox separatorBox;
	private Intbox premierCodeBoxEchan;
	private Intbox dernierCodeBoxEchan;
	private Textbox premiereLettreBoxEchan;
	private Textbox derniereLettreBoxEchan;
	private Radio numNombres;
	private Radio numLettres;
	private Box choixNumerotation;
	private Button changeNumerotation;

	private Grid echantillonsList;

	private Button stockageEchantillons;
	
	// Objets Principaux.
	private Maladie maladie = new Maladie();
	private List<Echantillon> echantillons = new ArrayList<Echantillon>();
	private List<Object> addedEchantillons = new ArrayList<Object>();
	private List<EchantillonDTO> echantillonsDecorated = 
										new ArrayList<EchantillonDTO>();
	private Map<TKStockableObject, Emplacement> echansEmpl = 
							new HashMap<TKStockableObject, Emplacement>();
	private List<String> usedCodesEchantillons = new ArrayList<String>();
	
	// Variables formulaire.
	private String[] connaissances = new String[]{"OUI", "NON"};
	
	private Integer premierCode;
	private Integer dernierCode;
	private String premiereLettre;
	private String derniereLettre;
	private List<String> lettres = new ArrayList<String>();
	
	private boolean isPrelevementProcedure = false;
	
	private EchantillonDecoratorRowRenderer echanDecoRenderer = 
										new EchantillonDecoratorRowRenderer();
	
	private List<FileBatch> batches = new ArrayList<FileBatch>();
			
	/**
	 * Indique au comportement de la fiche si elle appartient à 
	 * la procédure d'enregistrement d'un prélèvement ou non.
	 * @param isProcedure true si procédure.
	 */
	public void setPrelevementProcedure(boolean isProcedure) {
		this.isPrelevementProcedure = isProcedure;
		
		// bouton pour revenir sur la fiche prlvt
		this.previous.setVisible(isProcedure);
		if (isProcedure) {
			setWaitLabel("prelevement.creation.encours");
		} else {
			setWaitLabel("echantillon.creation.encours");
		}
		
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		if (getDroitOnAction("Stockage", "Consultation")) {
			stockageEchantillons.setVisible(true);
		} else {
			stockageEchantillons.setVisible(false);
		}
		
		getCodesOrganeController().setIsOrg(true);
		getCodesMorphoController().setIsOrg(false);
		getCodesMorphoController().setIsMorpho(true);	
		
		lettres = Utils.createListChars(26, null, 
				new ArrayList<String>());
				
		getBinder().loadAll();
	}
	
	@Override
	public void setObject(TKdataObject echan) {
		setEchantillon((Echantillon) echan);
		super.setObject(echan);
	}
	
	@Override
	public void setEchantillon(Echantillon e) {
		// indispensable pour le rendu des codes assignés
		// dans la table temporaire
		e.setBanque(SessionUtils
					.getSelectedBanques(sessionScope).get(0));
		super.setEchantillon(e);
	}
	
	/**
	 * Fiche en mode creation classique avec passage ou non 
	 * d'un prelevement parent.
	 */
	public void switchToCreateMode(Prelevement prel) {
		
		// Init du parent
		setParentObject(prel);
		
		// Initialisation du mode (listes, valeurs...)
		initEditableMode();
		initQuantiteAndVolume();
		initAssociations();
		
		// affiche le selecteur de parent au besoin.
		if (!isPrelevementProcedure) {
			if (getParentObject() == null) {
				addSelectParentMode();
			} else {
				sterileBox.setChecked(initSterileOrNot());
			}
		}
		
		// since 2.0.13
		initFormFromDossierExterne();
			
		super.switchToCreateMode();
		
		Clients.scrollIntoView(formGrid.getColumns());
	
		if (!getDroitOnAction("Collaborateur", "Consultation")) {
			operateurAideSaisieEchan.setVisible(false);
		}
		
		setFocusOnElement();
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void onClick$create() {
		// si l'utilisateur devait sélectionner un prlvt parent
//		if (selectParent
//				&& connaissancesBoxEchan.getSelectedIndex() == 0) {
//			// on valide la sélection
//			cttCodeParentEchan.validate(codesParentBoxEchan, null);
//			String tmp = codesParentBoxEchan.getValue();
//			// on récupère le prlvt en fonction de son code
//			this.prelevement = ManagerLocator.getPrelevementManager()
//				.findByCodeLikeManager(tmp, true).get(0);
//		} 
		
		boolean ok = false;
		
		if (addedEchantillons.size() == 0) {
			if (Messagebox.show(Labels.getLabel(
			"message.noEchantillonAdded"), 
			Labels.getLabel("message.save.title"), 
			Messagebox.YES | Messagebox.NO, 
			Messagebox.QUESTION) == Messagebox.YES) {
				// on vérifie que l'on vient bien de la fiche
				// prelevement
				if (getPrelevementController().hasFicheLaboInter()) {
					getPrelevementController()
					.getFicheLaboInter().onClick$create();
				} else {
					// si ce n'est pas le cas, aucune modif n'a été
					// fait sur le prlvt, aucun nouveaux echantillons
					// on va donc juste sauver les emplacements
					Clients.showBusy(Labels.getLabel(getWaitLabel()));
					Events.echoEvent("onLaterSaveEmplacements", self, null);
				}
			} else {
				ok = false;
			}
		} else {
			ok = true;
		}
		
		if (ok) {		
			super.onClick$create();
		}
	}
	
	public void onLaterSaveEmplacements() {
		try {
			// enregistrement des emplacements	
			saveEmplacements();
			
			createFileHtmlToPrint();
			
			getObjectTabController().onCancel();
			
			// ferme wait message
			Clients.clearBusy();
			
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		}	
			
	}
	
	@Override
	public boolean onLaterCreate() {
		
		try {
			// createObjectWithAnnots();
			createNewObject();
			
			// ajout des echantillons
			//if (!isPrelevementProcedure) {
				getObjectTabController()
				.getListe().addListToObjectList(addedEchantillons);
			//}
			
			if (isPrelevementProcedure) {
				getPrelevementController()
					.getListe().addToObjectList(getParentObject());	
			} else {
				if (getParentObject() != null) {
					if (getPrelevementController() != null) {
						// update du parent dans la liste
						//parentUpdated = 
						getPrelevementController().getListe()
						.updateObjectGridListFromOtherPage(getParentObject(), true);
					}	
					// retour vers la fiche prelevement au besoin
					if (getObjectTabController().getFromFichePrelevement()) {
						getObjectTabController().setFromFichePrelevement(false);
						PatientController.backToMe(getMainWindow(), page);
					}
				}
			}
			
			//update patient
			if (getParentObject() != null 
									&& getParentObject().getMaladie() != null) {
				if (!getPrelevementController()
								.getReferencingObjectControllers().isEmpty()) {
					getPrelevementController()
						.getReferencingObjectControllers().get(0).getListe()
							.updateObjectGridListFromOtherPage(getParentObject()
													.getMaladie().getPatient(), true);
				}
			}
			
//			if (isPrelevementProcedure) {
//				Tabpanel panel = (Tabpanel) getMainWindow()
//					.getMainTabbox().getFellow("echantillonPanel");
//				getMainWindow().destroyContentPanel(panel);
//			} else {
			getObjectTabController().onCreateDone();
//			}
			// commande le passage en mode statique
			if (isPrelevementProcedure) {
				getPrelevementController().setNextToEchanClicked(false);
				getPrelevementController().onCreateDone();
				// retour vers la fiche patient au besoin
				if (getPrelevementController().getFromFichePatient()) {
					getPrelevementController().setFromFichePatient(false);
					PatientController.backToMe(getMainWindow(), page);
				} else {
					PrelevementController.backToMe(getMainWindow(), page);
				}
			}
			
			
			// ferme wait message
			Clients.clearBusy();
			
			// si c'est une procédure de création d'un prlvt, on
			// demande si l'utilisateur veut en creer un autre
			if (isPrelevementProcedure) {
				getPrelevementController().createAnotherPrelevement(
						getParentObject());
			}
			
			return true;
		} catch (DoublonFoundException re) {
			Clients.clearBusy();

			HashMap <String, Object> map = new HashMap<String, Object>();
	        map.put("title", Labels.getLabel("error.unhandled"));
	        map.put("message", handleExceptionMessage(re));
	        map.put("exception", re);
			
			Window window = (Window) Executions
					.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", 
																null, map);
			window.doModal();
			
			return false;	
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
			return false;
		}	
	}
	
	@Override
	public void createNewObject() {
	
		boolean revertMaladie = false;
		boolean revertPatient = false; 
		
		// Enregistrement ou reference vers le prelevement
		if (isPrelevementProcedure) {
//			setParentObject(getPrelevementController()
//					.getFicheLaboInter().getPrelevementPrepared());
			getParentObject()
				.setLaboInters(new HashSet<LaboInter>(getLaboInters()));
			
			
			if (getParentObject().getMaladie() != null) {
				revertMaladie = getParentObject()
									.getMaladie().getMaladieId() == null;
				if (revertMaladie) {
					revertPatient = getParentObject().getMaladie()
									.getPatient().getPatientId() == null;
				}
			}
			
			getPrelevementController().getFicheAnnotation()
								.populateValeursActionLists(true, false);		
					
//			ManagerLocator.getPrelevementManager()
//				.createPrelAndEchansManager(getParentObject(), 
//					getPrelevementController().getFicheAnnotation()
//											.getValeursToCreateOrUpdate(), 
//													addedEchantillons, 
//					getObjectTabController().getFicheAnnotation()
//											.getValeursToCreateOrUpdate(),
//					getBanque(), SessionUtils.getLoggedUser(sessionScope), 
//								true);
//			
//			getPrelevementController()
//							.getFicheAnnotation().clearValeursLists(true);
				
		} else { // reference vers le patient	
			// si l'utilisateur devait sélectionner un prlvt parent
			if (selectParent
					&& connaissancesBoxEchan.getSelectedIndex() == 0) {
				setParentObject(getPrelevementFromSelect());
				setBanque(getParentObject().getBanque());
			}
		}
		
		
		List<File> filesCreated = new ArrayList<File>();
		// List<File> filesToDelete = new ArrayList<File>();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("updatePrelAndEchansTx");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		TransactionStatus status = ManagerLocator
										.getTxManager().getTransaction(def);
		
		try {
			if (isPrelevementProcedure) {
				//enregistrement du prelevement
				ManagerLocator.getPrelevementManager()
					.createObjectManager(
						getParentObject(), 
						getBanque(), 
						getParentObject().getNature(), 
						getParentObject().getMaladie(), 
						getParentObject().getConsentType(), 
						getParentObject().getPreleveur(),
						getParentObject().getServicePreleveur(), 
						getParentObject().getPrelevementType(), 
						getParentObject().getConditType(), 
						getParentObject().getConditMilieu(), 
						getParentObject().getTransporteur(), 
						getParentObject().getOperateur(), 
						getParentObject().getQuantiteUnite(), 
						new ArrayList<LaboInter>(getParentObject()
														.getLaboInters()), 
						getPrelevementController().getFicheAnnotation()
												.getValeursToCreateOrUpdate(),
						filesCreated,
						SessionUtils.getLoggedUser(sessionScope), true,
						SessionUtils.getSystemBaseDir(), false);				
			}
		
			//CodeAssigne codeOrgExp;
			//CodeAssigne codeLesExp;
			
			Fichier crAnapath = null;
			String crDataType = null;
			String crPath = null;
			
			//enregistrement de l'echantillon
			for (int i = 0; i < echantillonsDecorated.size(); i++) {
				if (echantillonsDecorated.get(i).isNew()) {
					Echantillon newEchan = echantillonsDecorated
										.get(i).getEchantillon();
					
					// nettoie les code de la reference vers l'echantillon
					// utilisé comme base objet pour la creation multiple
//					codeOrgExp = echantillonsDecorated
//											.get(i).getCodeOrganeToExport();
//					if (codeOrgExp != null) {
//						codeOrgExp.setEchantillon(null);
//						codeOrgExp.setEchanExpOrg(null);
//					}
//					codeLesExp = echantillonsDecorated
//												.get(i).getCodeLesToExport();
//					if (codeLesExp != null) {
//						codeLesExp.setEchantillon(null);
//						codeLesExp.setEchanExpLes(null);
//					}
					List<CodeAssigne> codes = new ArrayList<CodeAssigne>();
					codes.addAll(echantillonsDecorated
							.get(i).getCodesOrgsToCreateOrEdit());
					codes.addAll(echantillonsDecorated
							.get(i).getCodesLesToCreateOrEdit());
					
					// recupere path + data type si nouveau strea
					if (newEchan.getCrAnapath() != null 
							&& newEchan.getAnapathStream() == null ) {
						newEchan.getCrAnapath().setMimeType(crDataType);
						newEchan.getCrAnapath().setPath(crPath);
						// crAnapath = newEchan.getCrAnapath().cloneNoId();
						// crAnapath.setFichierId(null);
					}
					crAnapath = newEchan.getCrAnapath();
					newEchan.setCrAnapath(null);

					// création de l'objet
					ManagerLocator.getEchantillonManager()
						.createObjectWithCrAnapathManager(newEchan, 
							getBanque(),
							getParentObject(), 
							newEchan.getCollaborateur(), 
							ManagerLocator.getObjetStatutManager()
								.findByStatutLikeManager("NON STOCKE", true).get(0), 
							// newEchan.getEmplacement(), 
							null, // since 2.0.13.2
							newEchan.getEchantillonType(), 
							codes,
							newEchan.getQuantiteUnite(), 
							newEchan.getEchanQualite(), 
							newEchan.getModePrepa(), 
							crAnapath, 
							newEchan.getAnapathStream(), 
							filesCreated,
							newEchan.getReservation(), 
							//codeOrgExp, 
							//codeLesExp,
							echantillonsDecorated
								.get(i)
									.getValeursToCreateOrUpdate(), 
							SessionUtils.getLoggedUser(sessionScope), 
							true,
							SessionUtils.getSystemBaseDir(), 
							false);
					
					if (newEchan.getAnapathStream() != null) {
						crDataType = newEchan.getCrAnapath().getMimeType();
						crPath = newEchan.getCrAnapath().getPath();
					}
				}
			}
			
			// enregistrement des emplacements	
			saveEmplacements();
			
			// gestion de la non conformité
			if (isPrelevementProcedure) {
				if (getParentObject().getConformeArrivee() != null
						&& !getParentObject().getConformeArrivee()) {
					ManagerLocator.getObjetNonConformeManager()
						.createUpdateOrRemoveListObjectManager(
								getParentObject(), 
								getPrelevementController()
								.getFicheLaboInter()
								.findSelectedNonConformites(), 
								"Arrivee");
				}
				
				// suppression du patientSip
				getPrelevementController().removePatientSip();

			}
			
			for (int i = 0; i < echantillonsDecorated.size(); i++) {
				if (echantillonsDecorated.get(i).getEchantillon()
						.getConformeTraitement() != null
						&& !echantillonsDecorated.get(i).getEchantillon()
						.getConformeTraitement()) {
					// enregistrement de la non conformité 
					// après traitement
					ManagerLocator.getObjetNonConformeManager()
						.createUpdateOrRemoveListObjectManager(
								echantillonsDecorated
									.get(i).getEchantillon(), 
								echantillonsDecorated
									.get(i).getNonConformiteTraitements(), 
								"Traitement");
				}
				
				if (echantillonsDecorated.get(i).getEchantillon()
						.getConformeCession() != null
						&& !echantillonsDecorated.get(i).getEchantillon()
						.getConformeCession()) {
					// enregistrement de la non conformité 
					// à la cession
					ManagerLocator.getObjetNonConformeManager()
						.createUpdateOrRemoveListObjectManager(
								echantillonsDecorated
									.get(i).getEchantillon(), 
								echantillonsDecorated
								.get(i).getNonConformiteCessions(), 
								"Cession");
				}
			}
			
			// annotation file batches
			for (FileBatch batch : batches) {
				ManagerLocator.getAnnotationValeurManager()
					.createFileBatchForTKObjectsManager(batch.getObjs(), 
						batch.getFile(), batch.getStream(), batch.getChamp(), 
							getBanque(),
							SessionUtils.getLoggedUser(sessionScope), 
							SessionUtils.getSystemBaseDir(), filesCreated);
			}
			
//			for (File f : filesToDelete) {
//				f.delete();
//			}
			
		} catch (RuntimeException re) {
			ManagerLocator.getTxManager().rollback(status);
			
			for (File f : filesCreated) {
				f.delete();
			}
			
			if (isPrelevementProcedure) {	
				if (revertMaladie) {
					getParentObject().getMaladie().setMaladieId(null);
					if (revertPatient) {
						getParentObject().getMaladie()
									.getPatient().setPatientId(null);
					}
				}
				
				getParentObject().setPrelevementId(null);
				// revert Objects
				Iterator<LaboInter> it = 
							getParentObject().getLaboInters().iterator();
				while (it.hasNext()) {
					it.next().setLaboInterId(null);
				}
			}
			Iterator<EchantillonDTO> itE = 
									echantillonsDecorated.iterator();
			
			Echantillon e;
			EchantillonDTO ed;
			ObjetStatut stocke = ManagerLocator.getObjetStatutManager()
					.findByStatutLikeManager("STOCKE", false).get(0);
			ObjetStatut nonstocke = ManagerLocator.getObjetStatutManager()
					.findByStatutLikeManager("NON STOCKE", false).get(0);
			while (itE.hasNext()) {
				ed = itE.next();
				e = ed.getEchantillon();
				if (e.getEchantillonId() != null) {
					e.setEchantillonId(null);
					if (e.getCrAnapath() != null) {
						e.getCrAnapath().setFichierId(null);
					}
					// revert emplacement since 2.0.13.2
					// au stade onGetResultsFromStockage
					// se basant sur echEmpls
					if (echansEmpl.containsKey(e)) {
						e.setObjetStatut(stocke);
						e.setEmplacement(null);
						// clean echanEmpl from emplacement in error
						// doublon ou emplacement occupé 
						if ((re instanceof EmplacementDoublonFoundException 
								&& echansEmpl.get(e).equals(((EmplacementDoublonFoundException) re).getEmplacementMock()))
							|| (re instanceof TKException && re.getMessage().equals("error.emplacement.notEmpty")
									&& ((TKException) re).getTkObj().equals(e))) {
							echansEmpl.remove(e);
							// reset decorateur
							ed.setAdrlTmp(null);
							ed.getEchantillon().setObjetStatut(nonstocke);
						}
					}			
					
//						if (ed.getCodeOrganeToExport() != null) {
//							ed.getCodeOrganeToExport().setEchantillon(e);
//							ed.getCodeOrganeToExport().setEchanExpOrg(e);
//						}
//						if (ed.getCodeLesToExport() != null) {
//							ed.getCodeLesToExport().setEchantillon(e);
//							ed.getCodeLesToExport().setEchanExpLes(e);
//						}

				} else { // la boucle arrive a l'echantillon planté.
					break;
				}
			}
			
			updateDecoList(echansEmpl);
			
			throw re;
		} 	
		ManagerLocator.getTxManager().commit(status);	
		
		// envoi informations exterieures
		if (getParentObject() != null) {
			getPrelevementController().handleExtCom((Prelevement) getParentObject(), 
					getObjectTabController());
		}
		
		// since 2.1
		postStorageData(echansEmpl);
		
		createFileHtmlToPrint();

	}
	
	/**
	 * Recupere l'objet prelevement à partir du module de selection.
	 * @return prelevement parent choisi.
	 */
	private Prelevement getPrelevementFromSelect() {
		if (selectParentRowEchan.isVisible()) {
			if (codesParentBoxEchan.isVisible()) {
				// on valide la sélection
				cttCodeParentEchan.validate(codesParentBoxEchan, null);
				String tmp = codesParentBoxEchan.getSelectedItem().getValue();
				// on récupère le prlvt en fonction de son code
				return ManagerLocator.getPrelevementManager()
							.findByCodeLikeManager(tmp, true).get(0);
			}
		} 
		return getParentObject();
	}
	
	@Override
	public void onClick$cancel() {
		super.onClick$cancel();
		if (isPrelevementProcedure) {
			getPrelevementController().setNextToEchanClicked(false);
			if (getPrelevementController().hasFicheLaboInter()) {
				getPrelevementController()
							.getFicheLaboInter().onClick$cancel();
			}
		}
	}
	
	@Override
	public void onClick$validate() {
		
		boolean ok = false;
		
		if (addedEchantillons.size() == 0) {
			if (Messagebox.show(Labels.getLabel(
			"message.noEchantillonAdded"), 
			Labels.getLabel("message.save.title"), 
			Messagebox.YES | Messagebox.NO, 
			Messagebox.QUESTION) == Messagebox.YES) {
				getPrelevementController()
					.getFicheLaboInter().onClick$validate();
				saveEmplacements();
				createFileHtmlToPrint();
			} else {
				ok = false;
			}
		} else {
			ok = true;
		}
		
		if (ok) {
			super.onClick$validate();
		}
	}
	
	@Override
	public boolean onLaterUpdate() {
		
		try {
			// updateObjectWithAnnots();
			updateObject();
			
			// ajout des echantillons
			for (int i = 0; i < addedEchantillons.size(); i++) {
				getObjectTabController()
					.getListe().addToObjectList(addedEchantillons.get(i));
			}		
			
			getPrelevementController()
				.getListe().updateObjectGridList(getParentObject());
			
			//update patient
			if (getParentObject().getMaladie() != null) {
				if (!getPrelevementController()
						.getReferencingObjectControllers().isEmpty()) {
					getPrelevementController()
						.getReferencingObjectControllers().get(0).getListe()
							.updateObjectGridListFromOtherPage(getParentObject()
													.getMaladie().getPatient(), true);
				}
			}					
			
			// commande le passage en mode statique
			getObjectTabController().onCreateDone();
			getPrelevementController().setNextToEchanClicked(false);
			getPrelevementController().onEditDone(getParentObject());			
			
			PrelevementController.backToMe(getMainWindow(), page);
					
			// ferme wait message
			Clients.clearBusy();
			
			getPrelevementController().showEchantillonsAfterUpdate(
					getPrelevement());
			return true;
			
		} catch (DoublonFoundException re) {
			Clients.clearBusy();

			HashMap <String, Object> map = new HashMap<String, Object>();
	        map.put("title", Labels.getLabel("error.unhandled"));
	        map.put("message", handleExceptionMessage(re));
	        map.put("exception", re);
			
			Window window = (Window) Executions
					.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", 
																null, map);
			window.doModal();
			
			return false;	
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
			return false;
		}	
	}
	
	/**
	 * Modification de l'objet. Transactionnel.
	 * @return True si l'objet est modifié.
	 */
	@Override
	public void updateObject() {
		
		List<File> filesCreated = new ArrayList<File>();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("updatePrelAndEchansTx");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		TransactionStatus status = ManagerLocator
										.getTxManager().getTransaction(def);
		try {			
			getPrelevementController()
				.getFicheLaboInter().updateObjectWithAnnots();
			
			setParentObject(getPrelevementController()
										.getFicheLaboInter().getObject());
			getParentObject()
				.setLaboInters(new HashSet<LaboInter>(ManagerLocator
					.getPrelevementManager()
					.getLaboIntersWithOrderManager(getParentObject())));
						
			//CodeAssigne codeOrgExp;
			//CodeAssigne codeLesExp;
			
			Fichier crAnapath = null;
			String crDataType = null;
			String crPath = null;
						
			for (int i = 0; i < echantillonsDecorated.size(); i++) {
				if (echantillonsDecorated.get(i).isNew()) {
					Echantillon newEchan = echantillonsDecorated
													.get(i).getEchantillon();
					// nettoie les code de la reference vers l'echantillon
					// utilisé comme base objet pour la creation multiple
//					codeOrgExp = echantillonsDecorated
//											.get(i).getCodeOrganeToExport();
//					if (codeOrgExp != null) {
//						codeOrgExp.setEchantillon(null);
//						codeOrgExp.setEchanExpOrg(null);
//					}
//					codeLesExp = echantillonsDecorated
//												.get(i).getCodeLesToExport();
//					if (codeLesExp != null) {
//						codeLesExp.setEchantillon(null);
//						codeLesExp.setEchanExpLes(null);
//					}
				List<CodeAssigne> codes = new ArrayList<CodeAssigne>();
				codes.addAll(echantillonsDecorated
						.get(i).getCodesOrgsToCreateOrEdit());
				codes.addAll(echantillonsDecorated
						.get(i).getCodesLesToCreateOrEdit());
				
				// recupere path + data type si nouveau strea
				if (newEchan.getCrAnapath() != null 
						&& newEchan.getAnapathStream() == null ) {
					newEchan.getCrAnapath().setMimeType(crDataType);
					newEchan.getCrAnapath().setPath(crPath);
					// crAnapath = newEchan.getCrAnapath().cloneNoId();
					// crAnapath.setFichierId(null);
				}
				crAnapath = newEchan.getCrAnapath();
				newEchan.setCrAnapath(null);
								
				// création de l'objet
				ManagerLocator.getEchantillonManager()
					.createObjectWithCrAnapathManager(newEchan, 
						getBanque(),
						getParentObject(), 
						newEchan.getCollaborateur(), 
						ManagerLocator.getObjetStatutManager()
						.findByStatutLikeManager("NON STOCKE", true).get(0), 
						// newEchan.getEmplacement(),
						null, // since 2.0.13.2
						newEchan.getEchantillonType(), 
						codes,
						newEchan.getQuantiteUnite(), 
						newEchan.getEchanQualite(), 
						newEchan.getModePrepa(), 
						crAnapath, 
						newEchan.getAnapathStream(),
						filesCreated, 
						newEchan.getReservation(), 
						echantillonsDecorated
							.get(i).getValeursToCreateOrUpdate(), 
						SessionUtils.getLoggedUser(sessionScope), 
						true, SessionUtils.getSystemBaseDir(), 
						false);
				
					if (newEchan.getAnapathStream() != null) {
						crDataType = newEchan.getCrAnapath().getMimeType();
						crPath = newEchan.getCrAnapath().getPath();
					}
				}
			}			
			
			// enregistrement des emplacements	
			saveEmplacements();
			
			// gestion de la non conformité
			for (int i = 0; i < echantillonsDecorated.size(); i++) {
				if (echantillonsDecorated.get(i).getEchantillon()
						.getConformeTraitement() != null
						&& !echantillonsDecorated.get(i).getEchantillon()
						.getConformeTraitement()) {
					// enregistrement de la non conformité 
					// après traitement
					ManagerLocator.getObjetNonConformeManager()
						.createUpdateOrRemoveListObjectManager(
								echantillonsDecorated
									.get(i).getEchantillon(), 
								echantillonsDecorated
									.get(i).getNonConformiteTraitements(), 
								"Traitement");
				}
				
				if (echantillonsDecorated.get(i).getEchantillon()
						.getConformeCession() != null
						&& !echantillonsDecorated.get(i).getEchantillon()
						.getConformeCession()) {
					// enregistrement de la non conformité 
					// à la cession
					ManagerLocator.getObjetNonConformeManager()
						.createUpdateOrRemoveListObjectManager(
								echantillonsDecorated
									.get(i).getEchantillon(), 
								echantillonsDecorated
								.get(i).getNonConformiteCessions(), 
								"Cession");
				}
			}
			
		} catch (RuntimeException ex) {
		    ManagerLocator.getTxManager().rollback(status);
		    
		    for (File f : filesCreated) {
				f.delete();
			}
		    
		    Iterator<EchantillonDTO> itE = 
					echantillonsDecorated.iterator();
		    ObjetStatut stocke = ManagerLocator.getObjetStatutManager()
					.findByStatutLikeManager("STOCKE", false).get(0);
		    ObjetStatut nonstocke = ManagerLocator.getObjetStatutManager()
					.findByStatutLikeManager("NON STOCKE", false).get(0);
		    Echantillon e;
		    EchantillonDTO ed;
		    while (itE.hasNext()) {
		    	ed = itE.next();
		    	e = ed.getEchantillon();
		    	if (ed.isNew()) {
		    		e.setEchantillonId(null);
		    		if (e.getCrAnapath() != null) {
		    			e.getCrAnapath().setFichierId(null);
		    		}
		    	}
		    	// revert emplacement since 2.0.13.2
				// au stade onGetResultsFromStockage
				// se basant sur echEmpls
				if (echansEmpl.containsKey(e)) {
					e.setObjetStatut(stocke);
					e.setEmplacement(null);
					// clean echanEmpl from emplacement in error
					// doublon ou emplacement occupé 
					if ((ex instanceof EmplacementDoublonFoundException 
							&& echansEmpl.get(e).equals(((EmplacementDoublonFoundException) ex).getEmplacementMock()))
						|| (ex instanceof TKException && ex.getMessage().equals("error.emplacement.notEmpty")
								&& ((TKException) ex).getTkObj().equals(e))) {
						echansEmpl.remove(e);
						// reset decorateur
						ed.setAdrlTmp(null);
						ed.getEchantillon().setObjetStatut(nonstocke);
					}
				}
		    }
			updateDecoList(echansEmpl);
		    
		    throw ex;
		}
		ManagerLocator.getTxManager().commit(status);
		
		// envoi informations exterieures
		if (getParentObject() != null) {
			getPrelevementController().handleExtCom((Prelevement) getParentObject(), 
					getObjectTabController());
		}
		
		// since 2.1
		postStorageData(echansEmpl);
		
		createFileHtmlToPrint();
	}
	
	
	/**
	 * Méthode qui va sauvegarder le stockage des échantillons.
	 */
	public void saveEmplacements() {
		//List<String> errorMsg = new ArrayList<String>();
		
		// on parcourt la hashtable du stockage et on extrait
		// l'emplacement de chaque échantillon
		List<Emplacement> emplsFinaux = new ArrayList<Emplacement>();
		Set<TKStockableObject> echans = echansEmpl.keySet();
		Iterator<TKStockableObject> it = echans.iterator();
		while (it.hasNext()) {
			Echantillon echan = (Echantillon) it.next();
			Emplacement empl = echansEmpl.get(echan);
			empl.setObjetId(echan.getEchantillonId());
			emplsFinaux.add(empl);
		}
		
		// enregistrement des emplacements
		ManagerLocator.getEmplacementManager()
				.saveMultiEmplacementsManager(emplsFinaux);
						
		// on va MAJ chaque échantillon : son statut et son
		// emplacement
		echans = (Set<TKStockableObject>) echansEmpl.keySet();
		it = echans.iterator();
		ObjetStatut statut = ManagerLocator.getObjetStatutManager()
			.findByStatutLikeManager("STOCKE", true).get(0);
		while (it.hasNext()) {
			Echantillon echanToUpdate = (Echantillon) it.next();
			List<OperationType> ops = ManagerLocator.getOperationTypeManager()
				.findByNomLikeManager("Stockage", true);

			// update de l'objet
			ManagerLocator.getEchantillonManager()
				.saveEchantillonEmplacementManager(
					echanToUpdate, statut, 
					echansEmpl.get(echanToUpdate), 
					SessionUtils.getLoggedUser(sessionScope), 
					ops);
			
			EchantillonDTO deco = new 
			EchantillonDTO(echanToUpdate);
			// si l'échantillon existait déjà, on MAJ sa fiche
			// dans la liste des échantillons
			if (!addedEchantillons.contains(deco.getEchantillon())) {		
				// update de l'échantillon dans la liste
				//getObjectTabController().getListe()
					//.updateObjectGridList(echanToUpdate);
				getObjectTabController().getListe()
					.updateObjectGridListFromOtherPage(echanToUpdate, false);
			}
		}
	}
	
	/**
	 * Méthode qui permet d'afficher la page d'impression de la cession.
	 */
	public void createFileHtmlToPrint() {
		// s'il y a des éléments a stocker
		if (echansEmpl.size() > 0) {		
			// création du document, de la page et de son titre
			Document doc = ManagerLocator.getXmlUtils()
				.createJDomDocumentBoites();
			Element root = doc.getRootElement();
			Element page = ManagerLocator.getXmlUtils().addPageBoite(root, 
				Labels.getLabel(
						"impression.boite.title.stockage.echantillons"));
						
			List<BoiteImpression> listeBoites = 
				new ArrayList<BoiteImpression>();
			
			// on parcourt la hashtable du stockage et on extrait
			//  chaque dérivé
			Set<TKStockableObject> echans = (Set<TKStockableObject>) echansEmpl.keySet();
			// pour chaque échantillon
			for (int k = 0; k < echantillonsDecorated.size(); k++) {
				// s'il vient d'être stocké
				if (echans.contains(echantillonsDecorated.get(k)
						.getEchantillon())) {
					Echantillon ech = echantillonsDecorated.get(k)
						.getEchantillon();
				
					// on récupère son emplacement et la boite
					Emplacement emp = ManagerLocator.getEchantillonManager()
						.getEmplacementManager(ech);
					Terminale term = emp.getTerminale();
					
					BoiteImpression newBi = new BoiteImpression();
					newBi.setBoite(term);
					
					// si c'est la 1ère fois que l'on rencontre cette boite
					if (!listeBoites.contains(newBi)) {
						// on remplit tous titres et légendes
						newBi.setTitreModelisation(Labels
							.getLabel("impression.boite.title.visualisation"));
						newBi.setTitreInstructions(Labels
							.getLabel("impression.boite.title.instructions"));
						newBi.setNom(ObjectTypesFormatters
							.getLabel("impression.boite.nom", 
									new String[]{term.getNom()}));
						newBi.setTitreListe(Labels
							.getLabel("impression.boite.elements" 
								+ ".title.stockage.echantillons"));
						newBi.setLegendeVide(Labels
							.getLabel("impression.boite.legende.vide"));
						newBi.setLegendePris(Labels
							.getLabel("impression.boite.legende.pris"));
						newBi.setLegendeSelectionne(Labels
							.getLabel("impression.boite.legende.selectionne" 
								+ ".stockage.echantillons"));
						
						// création des intructions pour récupérer la boite
						// Récup des parents de la boite
						List<Object> parents = ManagerLocator
							.getTerminaleManager()
							.getListOfParentsManager(term);
						List<String> instructions = new ArrayList<String>();
						// pour chaque parent
						for (int j = 0; j < parents.size(); j++) {
							if (parents.get(j).getClass().getSimpleName()
									.equals("Conteneur")) {
								Conteneur c = (Conteneur) parents.get(j);
								instructions.add(ObjectTypesFormatters
								.getLabel("impression.boite" 
									+ ".instruction.conteneur", 
								new String[]{c.getCode()}));
							} else if (parents.get(j).getClass().getSimpleName()
									.equals("Enceinte")) {
								Enceinte e = (Enceinte) parents.get(j);
								instructions.add(ObjectTypesFormatters
								.getLabel("impression.boite" 
									+ ".instruction.enceinte", 
								new String[]{e.getNom()}));
							}
						}
						// ajout des instructions à la boite
						instructions.add(ObjectTypesFormatters
							.getLabel("impression.boite.instruction.terminale", 
							new String[]{term.getNom()}));
						instructions.add(Labels
								.getLabel("impression.boite.instruction" 
									+ ".stockage.echantillons"));
						newBi.setInstructions(instructions);
						
						// ajout du dérivé à la liste des éléments
						// a extraire
						List<String> elements = new ArrayList<String>();
						elements.add(ObjectTypesFormatters
							.getLabel("impression.boite.numero.echantillon", 
							new String[]{"1", ech.getCode()}));
						newBi.setElements(elements);
						
						// ajout de la position du dérivé
						List<Integer> positions = new ArrayList<Integer>();
						positions.add(emp.getPosition());
						newBi.setPositions(positions);
						
						// ajout de la boite à la liste
						listeBoites.add(newBi);
						
					} else {
						// sinon on récupère la boite dans la liste
						BoiteImpression bi = listeBoites
							.get(listeBoites.indexOf(newBi));
						
						// ajout du dérivé et de sa position aux
						// éléments à extraire
						Integer pos = bi.getPositions().size() + 1;
						bi.getPositions().add(emp.getPosition());
						bi.getElements().add(ObjectTypesFormatters
							.getLabel("impression.boite.numero.echantillon", 
							new String[]{pos.toString(), ech.getCode()}));
					}
				}
			}
			
			StringBuffer adrImages = new StringBuffer();
			adrImages.append(((HttpServletRequest) Executions.getCurrent()
					.getNativeRequest()).getContextPath());
			adrImages.append("/images/icones/emplacements/");
			for (int i = 0; i < listeBoites.size(); i++) {
				ManagerLocator.getXmlUtils()
					.addBoite(page, listeBoites.get(i), 
							adrImages.toString());
			}
			
			// Transformation du document en fichier
			byte[] dl = null;
			try {
				dl = ManagerLocator.getXmlUtils().creerBoiteHtml(doc);
			} catch (Exception e) {
				log.error(e);
			}
			
			// envoie du fichier à imprimer à l'utilisateur
			if (dl != null) {
				// si c'est au format html, on va ouvrir une nouvelle
				// fenêtre
				try {
					sessionScope.put("File", dl);
					execution.sendRedirect("/impression", "_blank");
				} catch (Exception e) {
					if (sessionScope.containsKey("File")) {
						sessionScope.remove("File");
						dl = null;
					}
				}
				dl = null;
			}
		}
		
	}

	@Override
	public void onClick$revert() {
		if (isPrelevementProcedure) {
			getPrelevementController().getFicheLaboInter().onClick$revert();
		}
		getObjectTabController().onCancel();
	}
	
	/**
	 * Revient sur la page FicheLaboInter dans l'état où elle
	 * était avant d'arriver sur la page FicheMultiEchantillons.
	 */
	public void onClick$previous() {
		clearConstraints();
		getPrelevementController().switchFromEchantillonsToLabo();
	}
	
	/**
	 * Méthode appelée par la fenêtre CollaborationsController quand
	 * l'utilisateur sélectionne un collaborateur.
	 * @param e Event contenant le collaborateur sélectionné.
	 */
	public void onGetObjectFromSelection(Event e) {
		
		// les collaborateurs peuvent être modifiés dans la fenêtre
		// d'aide => maj de ceux-ci
		getNomsAndPrenoms().clear();
		setCollaborateurs(ManagerLocator.getCollaborateurManager()
			.findAllActiveObjectsWithOrderManager());
		for (int i = 0; i < getCollaborateurs().size(); i++) {
			getNomsAndPrenoms().add(getCollaborateurs()
								.get(i).getNomAndPrenom());
		}
		collabBox.setModel(new CustomSimpleListModel(
				getNomsAndPrenoms()));			
		
		// si un collaborateur a été sélectionné
		if (e.getData() != null) {
			Collaborateur coll = (Collaborateur) e.getData();
			if (getNomsAndPrenoms().contains(coll.getNomAndPrenom())) {
				int ind = getNomsAndPrenoms().indexOf(coll.getNomAndPrenom());
				setSelectedCollaborateur(getCollaborateurs().get(ind));
				collabBox.setValue(getSelectedCollaborateur()
										.getNomAndPrenom());
			}
		}
	}
	
	/**
	 * Méthode exécutée lors du clic sur le bouton addEchantillons.
	 * Les nouveaux échantillons seront créés (mais pas encore
	 * sauvegardés) et ajoutés à la liste.
	 * @param Event : clic sur le lien addEchantillons.
	 */
	public void onClick$addEchantillons(Event event) {
		try {
			// onBlur$dateStockCalBox();
			if (getSelectedType() == null) {
				//Clients.scrollIntoView(typesBoxEchan);
				throw new WrongValueException(
					typesBoxEchan, 
					Labels.getLabel("ficheEchantillon.error.type"));
			}
			
			validateSterilite(sterileBox.isChecked());
		
			// on remplit l'échantillon en fonction des champs nulls
			setEmptyToNulls();
			getEchantillon().setTumoral(tumoraleBox.isChecked());
			getEchantillon().setSterile(sterileBox.isChecked());
			
			// gestion de la non conformitée après traitement
			if (conformeTraitementBoxOui.isChecked()) {
				getEchantillon().setConformeTraitement(true);
			} else if (conformeTraitementBoxNon.isChecked()) {
				getEchantillon().setConformeTraitement(false);
			} else {
				getEchantillon().setConformeTraitement(null);
			}
			if (getEchantillon().getConformeTraitement() == null) {
				setSelectedNonConformiteTraitement(null);
			} else if (getEchantillon().getConformeTraitement()) {
				setSelectedNonConformiteTraitement(null);
			}
			
			// gestion de la non conformitée à la cession
			if (conformeCessionBoxOui.isChecked()) {
				getEchantillon().setConformeCession(true);
			} else if (conformeCessionBoxNon.isChecked()) {
				getEchantillon().setConformeCession(false);
			} else {
				getEchantillon().setConformeCession(null);
			}
			if (getEchantillon().getConformeCession() == null) {
				setSelectedNonConformiteCession(null);
			} else if (getEchantillon().getConformeCession()) {
				setSelectedNonConformiteCession(null);
			}
			
			// Gestion du collaborateur
			String selectedNomAndPremon = this.collabBox.getValue()
				.toUpperCase();
			this.collabBox.setValue(selectedNomAndPremon);
			int ind = getNomsAndPrenoms().indexOf(selectedNomAndPremon);
			if (ind > -1) {
				setSelectedCollaborateur(getCollaborateurs().get(ind));
			} else {
				setSelectedCollaborateur(null);
			}
			
			// la quantité et le volume restants sont égaux aux valeurs
			// intiales
			getEchantillon().setQuantite(getEchantillon().getQuantiteInit());
			
			getEchantillon()
				.setObjetStatut(findStatutForTKStockableObject(getEchantillon()));
			ObjetStatut statut = ManagerLocator.getObjetStatutManager()
				.findByStatutLikeManager("NON STOCKE", true).get(0);
			
			prepareCrAnapath();
			
			prepareCodes();
			
			String lateralite = null;
			// lateralite
			if (getSelectedLateralite() != null 
							&& !getSelectedLateralite().equals("")) {
				lateralite = getSelectedLateralite();
			}
			
			// prepare les listes de valeurs à manipuler
			getObjectTabController()
				.getFicheAnnotation().populateValeursActionLists(true, false);
			// clone liste valeurs annotations
			List<AnnotationValeur> clonesValeurs = 
										new ArrayList<AnnotationValeur>();
						
			for (int i = 0; i < getObjectTabController()
				.getFicheAnnotation()
						.getValeursToCreateOrUpdate().size(); i++) {
				
				AnnotationValeur val = getObjectTabController()
						.getFicheAnnotation()
						.getValeursToCreateOrUpdate().get(i);
				// retire les annotations fichier car créés en batches ultérieurement
				if (val.getChampAnnotation().getDataType().getDataTypeId() == 8) {
					if (val.getFichier() != null && val.getStream() != null) {
						FileBatch batch = new FileBatch();
						batch.setChamp(val.getChampAnnotation());
						batch.setFile(val.getFichier());
						batch.setStream(val.getStream());
						batches.add(batch);
					}
				} else {
					clonesValeurs.add(getObjectTabController()
						.getFicheAnnotation()
								.getValeursToCreateOrUpdate().get(i).clone());
				}
			}
				
			// gestion des bornes pour la création
			int first = 0;
			int last = 0;
			if (numNombres.isChecked()) {
				first = premierCode;
				last = dernierCode;
			} else {
				first = lettres.indexOf(premiereLettre.toUpperCase());
				last = lettres.indexOf(derniereLettre.toUpperCase());
			}
			
			// Création de tous les nouveaux échantillons
			for (int i = first; i <= last; i++) {
				
				// création du code échantillon en fct de celui du prlvt et
				// du numéro saisi
				StringBuffer sb = new StringBuffer();
				
				// 2.0.10.6 VIROBIOTEC création codes sans prefixe
				if (getCodePrefixe() != null 
									&& !getCodePrefixe().trim().equals("")) {
					sb.append(getCodePrefixe());
					//sb.append(".");
					sb.append(separatorBox.getValue() != null ? separatorBox.getValue() : "");
				}
				if (numNombres.isChecked()) {
					sb.append(i);
				} else {
					sb.append(lettres.get(i));
				}
				
				usedCodesEchantillons.add(sb.toString());
				
				Echantillon newEchantillon = new Echantillon();
				newEchantillon.setBanque(getBanque());
				newEchantillon.setPrelevement(getParentObject());
				newEchantillon.setCollaborateur(getSelectedCollaborateur());
				newEchantillon.setCode(sb.toString());
				newEchantillon.setDateStock(getEchantillon().getDateStock());
				newEchantillon.setEchantillonType(getSelectedType());
				newEchantillon.setQuantite(getEchantillon().getQuantite());
				newEchantillon.setQuantiteInit(getEchantillon()
														.getQuantiteInit());
				newEchantillon.setQuantiteUnite(getSelectedQuantiteUnite());
				newEchantillon.setDelaiCgl(getEchantillon().getDelaiCgl());
				newEchantillon.setEchanQualite(getSelectedQualite());
				newEchantillon.setTumoral(getEchantillon().getTumoral());
				newEchantillon.setModePrepa(getSelectedPrepa());
				newEchantillon.setCrAnapath(getCrAnapath() != null ? 
										getCrAnapath().cloneNoId() : null);
				newEchantillon.setAnapathStream(i == first ? getAnapathStream() : null);
				newEchantillon.setSterile(getEchantillon().getSterile());
				newEchantillon.setObjetStatut(statut);
				newEchantillon.setLateralite(lateralite);
				newEchantillon.setConformeTraitement(getEchantillon()
						.getConformeTraitement());
				newEchantillon.setConformeCession(getEchantillon()
						.getConformeCession());
				
				echantillons.add(newEchantillon);
				addedEchantillons.add(newEchantillon);
				EchantillonDTO deco = 
					new EchantillonDTO(newEchantillon);
				
				deco.setCodesOrgsToCreateOrEdit(CodeAssigneDecorator
						.undecorateListe(getCodesOrganeController()
						.getObjToCreateOrEdit()));
				//deco.setCodeOrganeToExport(getCodeOrganeToExport());
				deco.setCodesLesToCreateOrEdit(CodeAssigneDecorator
						.undecorateListe(getCodesMorphoController()
						.getObjToCreateOrEdit()));
				//deco.setCodeLesToExport(getCodeLesToExport());
				
				deco.setValeursToCreateOrUpdate(clonesValeurs);
				
				deco.setNonConformiteTraitements(
						findSelectedNonConformitesTraitement());
				deco.setNonConformiteCessions(
						findSelectedNonConformitesCession());
				
//				deco.setAnapatStream(getAnapathStream());
				echantillonsDecorated.add(deco);
				
				// file batches
				for (FileBatch batch : batches) {
					if (!batch.isCompleted()) {
						batch.getObjs().add(newEchantillon);
					}
				}
			}
			
			for (FileBatch batch : batches) {
				batch.setCompleted(true);
			}
			
			ListModel<EchantillonDTO> list = 
				new ListModelList<EchantillonDTO>(echantillonsDecorated);
			echantillonsList.setModel(list);
			
			getEchanDecoRenderer().setUsedCodes(usedCodesEchantillons);
			
			/*String id = stockageEchantillons.getUuid();
			String idTop = panelChildrenWithScroll.getUuid();
			Clients.evalJavaScript("document.getElementById('" + idTop + "')" 
					+ ".scrollTop = document.getElementById('" + id + "')" 
					+ ".offsetTop;");	*/
			Clients.scrollIntoView(stockageEchantillons);
			
			clearForm(true);
			
			// active le lien vers le stockage
			if (!echantillons.isEmpty()) {
				//stockageEchantillons.setSclass("formLink");
				stockageEchantillons.setDisabled(false);
			}
		} catch (ValidationException ve) {
			Messagebox.show(handleExceptionMessage(ve), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} 

	}
	
	/**
	 * Méthode supprimant un des échantillons que l'utilisateur
	 * souhaitait créer.
	 * @param event : clic sur le lien deleteEchan dans la liste
	 * d'échantillons.
	 */
	public void onDeleteDeco$rows(ForwardEvent event) {
//		EchantillonDecorator2 deco = (EchantillonDecorator2) 
//			AbstractListeController2
//				.getBindingData((ForwardEvent) event, false);
		
		EchantillonDTO deco = (EchantillonDTO) 
								event.getOrigin().getData(); 
		
		Echantillon deletedEchantillon = deco.getEchantillon();
		
		if (addedEchantillons.contains(deletedEchantillon)) {
			addedEchantillons.remove(deletedEchantillon);
			echantillons.remove(deletedEchantillon);
			echantillonsDecorated.remove(deco);
			usedCodesEchantillons.remove(deletedEchantillon.getCode());
			
			// on enlève l'association entre l'échantillon et son
			// emplacement dans la hashtable
			if (echansEmpl.containsKey(deletedEchantillon)) {
				echansEmpl.remove(deletedEchantillon);
			}
			
			ListModel<EchantillonDTO> list = 
				new ListModelList<EchantillonDTO>(echantillonsDecorated);
			echantillonsList.setModel(list);
			
			// echantillon retiré des fileBatches
			for (FileBatch batch : batches) {
				batch.getObjs().remove(deletedEchantillon);
			}
			
			deletedEchantillon = null;
			deco = null;
			
			
		}
		
		// desactive le lien vers le stockage
		if (echantillons.isEmpty()) {
			//stockageEchantillons.setSclass("formLabel");
			stockageEchantillons.setDisabled(true);
		}
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * codePrefixeEchan. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$codePrefixeEchan() {
		codePrefixeEchan.setValue(codePrefixeEchan.getValue()
											.toUpperCase().trim());
	}
	
	/**
	 * Méthode appelée lors du clic sur le bouton stockageEchantillons.
	 */
	public void onClick$stockageEchantillons() {
		if (!stockageEchantillons.isDisabled()) {
			// on récupère tous les échantillons NON STOCKE
			List<Echantillon> echansToStock = new ArrayList<Echantillon>();
			for (int i = 0; i < echantillons.size(); i++) {
				if (echantillons.get(i).getObjetStatut()
						.getStatut().equals("NON STOCKE")) {
					echansToStock.add(echantillons.get(i));
				}
			}
			
			// si des emplacements ont déja ete définis pour des échantillons
			// ils sont réservés
			Iterator<Emplacement> emplsIt = echansEmpl.values().iterator();
			List<Emplacement> reserves = new ArrayList<Emplacement>();
			while (emplsIt.hasNext()) {
				reserves.add(emplsIt.next());
			}
			
			StockageController tabController = StockageController
				.backToMe(getMainWindow(), page);
			getMainWindow().blockAllPanelsExceptOne("stockageTab");
			tabController.clearAllPage();
			tabController.switchToStockerMode(
					echansToStock, null, Path.getPath(self),
					"onGetResultsFromStockage", reserves);
		}
	}
	
	/**
	 * Méthode qui récupère les emplacements choisis pour les
	 * échantillons.
	 * @param e Event contenant les emplacements.
	 */
	@SuppressWarnings("unchecked")
	public void onGetResultsFromStockage(Event e) {
		// les emplacements sont contenus dans une hashtable mappant
		// un échantillon avec son emplacement
		Hashtable<TKStockableObject, Emplacement> results = 
			(Hashtable<TKStockableObject, Emplacement>) e.getData();
		
		updateDecoList(results);
	}
	
	/**
	 * Met à jour la liste d'échantillon décorées
	 * @since 2.0.13.2
	 * @param table echantillon - emplacements
	 */
	private void updateDecoList(Map<TKStockableObject, Emplacement> empls) {
		ObjetStatut stocke = ManagerLocator.getObjetStatutManager()
				.findByStatutLikeManager("STOCKE", true).get(0);
	
		Set<TKStockableObject> echans = (Set<TKStockableObject>) empls.keySet();
		Iterator<TKStockableObject> it = echans.iterator();
		Emplacement emp;
		// pour chaque échantillon qui vient d'être stocké
		while (it.hasNext()) {
			TKStockableObject tmp = it.next();
			EchantillonDTO deco = new EchantillonDTO((Echantillon) tmp);
			// on récupère son decorator correspondant dans la liste
			if (echantillonsDecorated.contains(deco)) {
				EchantillonDTO decoUp = echantillonsDecorated
					.get(echantillonsDecorated.indexOf(deco));
				
				emp = empls.get(tmp);
				
				// since 2.0.13.2 empl peux être null
				// pour nettoyer echan stockage en erreur
				if (emp != null) {
					// MAJ du stockage de cet échantillon
					decoUp.setAdrlTmp(emp.getAdrl());
					decoUp.getEchantillon().setObjetStatut(stocke);
				}
			}
			
			// ajout du couple dans la hashtable
			if (!echansEmpl.containsKey(tmp)) {
				echansEmpl.put(tmp, empls.get(tmp));
			}
		}
		
		// MAJ de la liste
		getBinder().loadAttribute(
					self.getFellow("echantillonsList"), "model");		
	}
	
	public void onClick$changeNumerotation() {
		changeNumerotation.setVisible(false);
		choixNumerotation.setVisible(true);
	}
	
	public void onCheck$numNombres() {
		premierCodeBoxEchan.setVisible(true);
		dernierCodeBoxEchan.setVisible(true);
		premiereLettreBoxEchan.setVisible(false);
		derniereLettreBoxEchan.setVisible(false);
		Clients.clearWrongValue(premiereLettreBoxEchan);
		premiereLettreBoxEchan.setConstraint("");
		premiereLettreBoxEchan.setValue(null);
		premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
		Clients.clearWrongValue(derniereLettreBoxEchan);
		derniereLettreBoxEchan.setConstraint("");
		derniereLettreBoxEchan.setValue(null);
		derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
		changeNumerotation.setVisible(true);
		choixNumerotation.setVisible(false);
		changeNumerotation.setImage("/images/icones/smallNumber.png");
	}
	
	public void onCheck$numLettres() {
		premierCodeBoxEchan.setVisible(false);
		dernierCodeBoxEchan.setVisible(false);
		premiereLettreBoxEchan.setVisible(true);
		derniereLettreBoxEchan.setVisible(true);
		Clients.clearWrongValue(premierCodeBoxEchan);
		premierCodeBoxEchan.setConstraint("");
		premierCodeBoxEchan.setValue(null);
		premierCodeBoxEchan.setConstraint(cttPremierCode);
		Clients.clearWrongValue(dernierCodeBoxEchan);
		dernierCodeBoxEchan.setConstraint("");
		dernierCodeBoxEchan.setValue(null);
		dernierCodeBoxEchan.setConstraint(cttDernierCode);
		changeNumerotation.setVisible(true);
		choixNumerotation.setVisible(false);
		changeNumerotation.setImage("/images/icones/smallLetter.png");
	}
	
	/**
	 * Cette méthode va vider le formulaire une fois que l'utilisateur
	 * aura cliqué sur le bouton addEchantillons.
	 */
	@SuppressWarnings("unchecked")
	public void clearForm(boolean setParentStatic) {
		setEchantillon(new Echantillon());
		
		premierCode = null;
		dernierCode = null;
		premiereLettre = null;
		derniereLettre = null;
		
		setQuantite(null);
		setQuantiteInit(null);

		setHeureDelai(null);
		setMinDelai(null);
		
		setSelectedLateralite(null);
		
		if (getTypes().size() > 0) {
			setSelectedType(getTypes().get(0));
		} else {
			setSelectedType(null);
		}
		typesBoxEchan.clearSelection();
		((ListModelList<Object>) typesBoxEchan.getModel())
			.clearSelection();
		
		setSelectedQuantiteUnite(null);
		quaniteUnitesBoxEchan.clearSelection();
		((ListModelList<Object>) quaniteUnitesBoxEchan.getModel())
			.clearSelection();
		
		// setSelectedCollaborateur(null);
		
		setSelectedQualite(null);
		qualitesBoxEchan.clearSelection();
		((ListModelList<Object>) qualitesBoxEchan.getModel())
			.clearSelection();
		
		setSelectedPrepa(null);
		prepasBox.clearSelection();
		((ListModelList<Object>) prepasBox.getModel())
			.clearSelection();
		
		tumoraleBox.setChecked(false);
		
		crAnapathNomBox.setValue(null);
		setCrAnapath(new Fichier());
		setAnapathStream(null);
		showDeleteAndFileNameBox(false);
		
		if (setParentStatic) {
			if (selectParentRowEchan.isVisible()) {
				removeSelectParentMode();
				if (getParentObject() == null) {
					prelRow.setVisible(false);
					unknownPrelRow.setVisible(true);
				}
			}
		} else {
			setCodePrefixe(null);
		}
		
		sterileBox.setChecked(getParentObject() != null 
				&& getParentObject().getSterile() != null
				&& getParentObject().getSterile());
		
		dateStockCalBox.setHasChanged(false);
		
		getCodesOrganeController().setObjs(new ArrayList<SmallObjDecorator>());
		getCodesOrganeController().reloadGrid();
		getCodesMorphoController().setObjs(new ArrayList<SmallObjDecorator>());
		getCodesMorphoController().reloadGrid();
		
		if (getObjectTabController().canUpdateAnnotation()) {
			getObjectTabController().getFicheAnnotation()
													.clearValeursLists(true);
			getObjectTabController().getFicheAnnotation()
													.updateAnnotationValues();
			getObjectTabController().getFicheAnnotation()
									.switchToStaticOrEditMode(false, false);
			//getObjectTabController()
			//.getFicheAnnotation().showButtonsBar(false);
		}
		
		setSelectedNonConformiteTraitement(null);
		setSelectedNonConformiteCession(null);
		conformeTraitementBoxOui.setChecked(false);
		conformeTraitementBoxNon.setChecked(false);
		conformeTraitementBox.setVisible(false);
		conformeCessionBoxOui.setChecked(false);
		conformeCessionBoxNon.setChecked(false);
		conformeCessionBox.setVisible(false);
		((Selectable<NonConformite>) nonConformitesCessionBox.getModel())
			.clearSelection();
		((Selectable<NonConformite>) nonConformitesTraitementBox.getModel())
			.clearSelection();
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void initEditableMode() {
		
		super.initEditableMode();

		if (getParentObject() != null) { 
			if (getParentObject().getCode() != null) {
				setCodePrefixe(getParentObject().getCode());
			}
			if (getParentObject().getOperateur() != null
					&& getCollaborateurs()
						.contains(getParentObject().getOperateur())) {
				setSelectedCollaborateur(getParentObject().getOperateur());
				collabBox.setValue(getSelectedCollaborateur().getNomAndPrenom());
			} 
		}
		
		// active le lien vers le stockage
		if (!echantillons.isEmpty()) {
			stockageEchantillons.setDisabled(false);
		}
		
		connaissancesBoxEchan
			.setSelectedItem(connaissancesBoxEchan.getItemAtIndex(1));
		
	}
	
	/**
	 * Recherche des doublons pour le permier et dernier codes saisis.
	 * @param premier Premier code des échanstillons à créer.
	 * @param dernier Dernier code des échanstillons à créer.
	 * @return True s'il y a des doublons, false sinon.
	 */
	public List<String> findDoublons(Integer premier, Integer dernier) {
		
		List<String> doublons = new ArrayList<String>();
		for (int i = premier; i <= dernier; i++) {
			StringBuffer sb = new StringBuffer();
			if (getCodePrefixe() != null 
					&& !getCodePrefixe().trim().equals("")) {
				sb.append(getCodePrefixe());
				// sb.append(".");
				sb.append(separatorBox.getValue() != null 
						? separatorBox.getValue() : "");
			}
			// sb.append(codePrefixeEchan.getValue());
			// sb.append(".");
			sb.append(i);
			
			if (usedCodesEchantillons.contains(sb.toString())) {
				doublons.add(String.valueOf(i));
			}
		}
		
		return doublons;
	}
	
	/**
	 * Recherche des doublons pour la permiere et derniere letres saisies.
	 * @return True s'il y a des doublons, false sinon.
	 */
	public List<String> findDoublonsForLetters(String premier, String dernier) {
		
		List<String> doublons = new ArrayList<String>();
		for (int i = lettres.indexOf(premier); 
			i <= lettres.indexOf(dernier); i++) {
			StringBuffer sb = new StringBuffer();
			if (getCodePrefixe() != null 
					&& !getCodePrefixe().trim().equals("")) {
				sb.append(getCodePrefixe());
				// sb.append(".");
				sb.append(separatorBox.getValue() != null 
						? separatorBox.getValue() : "");

			}
			// sb.append(codePrefixeEchan.getValue());
			// sb.append(".");
			sb.append(lettres.get(i));
			
			if (usedCodesEchantillons.contains(sb.toString())) {
				doublons.add(lettres.get(i));
			}
		}
		
		return doublons;
	}
	
	/**
	 * Méthode initialisant les objets associés.
	 */
	public void initAssociations() {
		if (getParentObject() != null 
				&& getParentObject().getBanque() != null) {
			setBanque(getParentObject().getBanque());
		} else {
			setBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
		}
		
		if (addedEchantillons.size() == 0) {
			// Init des échantillons
			if (getParentObject() != null 
						&& getParentObject().getPrelevementId() != null) {
				echantillons = ManagerLocator.getEchantillonManager()
						.findByPrelevementManager(getParentObject());
				
				usedCodesEchantillons = ManagerLocator.getEchantillonManager()
									.findAllCodesForBanqueManager(getBanque());
				
			} else {
				usedCodesEchantillons.clear();
				echantillons.clear();
			}
			
			echantillonsDecorated = ManagerLocator.getEchantillonDTOManager()
				.decorateListeManager(echantillons);
			ListModel<EchantillonDTO> list = 
				new ListModelList<EchantillonDTO>(echantillonsDecorated);
			echantillonsList.setModel(list);
			
			if (echantillons.size() > 0) {
				stockageEchantillons.setDisabled(false);
			} else {
				stockageEchantillons.setDisabled(true);
			}
		}
	}
	
//	/**
//	 * Recherche le statut de l'échantillon en fonction de la valeurs du
//	 * champ QUANTITE.
//	 * @return Un ObjetStatut : STOCKE ou EPUISE.
//	 */
//	public ObjetStatut findStatutForEchantillon() {
//		ObjetStatut stocke = ManagerLocator.getObjetStatutManager()
//			.findByStatutLikeManager("STOCKE", true).get(0);
//		ObjetStatut epuise = ManagerLocator.getObjetStatutManager()
//			.findByStatutLikeManager("EPUISE", true).get(0);
//		
//		if (stocke != null && epuise != null) {
//			Float zero = (float) 0.0;
//			// si la qté est non null
//			if (getEchantillon().getQuantite() != null) {
//				
//				// si sa valeur est égale a 0 => epuisé
//				if (getEchantillon().getQuantite().equals(zero)) {
//					return epuise;
//				} else {
//					return stocke;
//				}
//				
//			} else {
//				return stocke;
//			}
//		} else {
//			return null;
//		}
//	}
	
	/*********************************************************/
	/********************** ACCESSEURS. **********************/
	/*********************************************************/
		
	public Prelevement getPrelevement() {
		return getParentObject();
	}
	
	public String[] getConnaissances() {
		return connaissances;
	}

	public void setConnaissances(String[] c) {
		this.connaissances = c;
	}

	public boolean isSelectParent() {
		return selectParent;
	}

	public void setSelectParent(boolean parent) {
		this.selectParent = parent;
	}

	public Maladie getMaladie() {
		return maladie;
	}

	public void setMaladie(Maladie mal) {
		this.maladie = mal;
	}

	public List<Echantillon> getEchantillons() {
		return echantillons;
	}

	public void setEchantillons(List<Echantillon> echans) {
		this.echantillons = echans;
	}

	public List<Object> getAddedEchantillons() {
		return addedEchantillons;
	}

	public void setAddedEchantillons(List<Object> added) {
		this.addedEchantillons = added;
	}

	public List<EchantillonDTO> getEchantillonsDecorated() {
		return echantillonsDecorated;
	}

	public void setEchantillonsDecorated(
			List<EchantillonDTO> decorated) {
		this.echantillonsDecorated = decorated;
	}

	public EchantillonDecoratorRowRenderer getEchanDecoRenderer() {
		return echanDecoRenderer;
	}

	public List<String> getUsedCodesEchantillons() {
		return usedCodesEchantillons;
	}

	public void setUsedCodesEchantillons(List<String> usedCodes) {
		this.usedCodesEchantillons = usedCodes;
	}

	
	public Integer getPremierCode() {
		return premierCode;
	}

	public void setPremierCode(Integer code) {
		this.premierCode = code;
	}

	public Integer getDernierCode() {
		return dernierCode;
	}

	public void setDernierCode(Integer code) {
		this.dernierCode = code;
	}
	
	public Map<? extends TKStockableObject, Emplacement> getEchansEmpl() {
		return echansEmpl;
	}

//	public void setEchansEmpl(Hashtable<TKStockableObject, Emplacement> e) {
//		this.echansEmpl = e;
//	}

	/*************************************************************************/
	/************************** VALIDATION ***********************************/
	/*************************************************************************/

	public Constraint getCttPremierCode() {
		return cttPremierCode;
	}

	public void setCttPremierCode(Constraint ctt) {
		this.cttPremierCode = ctt;
	}

	public Constraint getCttDernierCode() {
		return cttDernierCode;
	}

	public void setCttDernierCode(Constraint ctt) {
		this.cttDernierCode = ctt;
	}
	
	/**
	 * 
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstPremierCode implements Constraint {
		
		public void validate(Component comp, Object value) {
			// on ne prend en compte cette contrainte que si
			// l'utilisateur veut utiliser des chiffres en
			// numérotation
			if (numNombres.isChecked()) {
				// on récupère la valeur dans premierCodeBoxEchan
				premierCode = (Integer) value;
				
				// si une valeur est saisie dans le champ premierCodeBoxEchan
				if (premierCode != null) {
					// si le premier code est négatif
					if (premierCode <= 0) {
						throw new WrongValueException(
						comp, Labels.getLabel(
						"ficheMultiProdDerive.error.code.sup.zero"));
					}
					// Si le dernierCode est null (1ère édition de la page)
					// on va récupérer la valeur du champ dernierCodeBoxEchan
					if (dernierCode == null) {
						// on enlève la contrainte de dernierCodeBoxEchan pour
						// pouvoir récupérer sa valeur
						dernierCodeBoxEchan.setConstraint("");
						dernierCodeBoxEchan.clearErrorMessage();
						dernierCode = dernierCodeBoxEchan.getValue();
						// on remet la contrainte
						dernierCodeBoxEchan.setConstraint(cttDernierCode);
					}
					
					// si une valeur est saisie dans le champ dernierCodeBoxEchan
					if (dernierCode != null) {
						// si le dernier code est < au premier
						if (dernierCode < premierCode) {
							throw new WrongValueException(
								comp, "Le premier code saisi ne peut pas être "
								+  "supérieur au dernier.");
						} else {
							
							// sinon on enlève toutes les erreurs affichées
							Integer tmp = dernierCode;
							Clients.clearWrongValue(dernierCodeBoxEchan);
							dernierCodeBoxEchan.setConstraint("");
							dernierCodeBoxEchan.setValue(tmp);
							dernierCodeBoxEchan.setConstraint(cttDernierCode);
							
							List<String> doublons = 
								findDoublons(premierCode, dernierCode);
							// si des doublons existent pour les valeurs saisies
							if (doublons.size() > 0) {
								StringBuffer sb = new StringBuffer();
								if (doublons.size() == 1) {
									sb.append("Echantillon déjà enregistré " 
											+ "pour le numéro : {");
									sb.append(doublons.get(0));
								} else {
									sb.append(
										"Echantillons déjà enregistrés " 
											+ "pour les numéros : {");
									for (int i = 0; i < doublons.size(); i++) {
										sb.append(doublons.get(i));
										if (i + 1 < doublons.size()) {
											sb.append(", ");
										}
									}
								}
								sb.append(
									"}. Veuillez modifier les numéros saisis.");
								throw new WrongValueException(
								comp, sb.toString());
							} else {
								// sinon on enlève toutes les erreurs affichées
								tmp = dernierCode;
								Clients.clearWrongValue(dernierCodeBoxEchan);
								dernierCodeBoxEchan.setConstraint("");
								dernierCodeBoxEchan.setValue(tmp);
								dernierCodeBoxEchan.setConstraint(cttDernierCode);
							}
						}
					} else {
						// sinon on enlève toutes les erreurs affichées
						Integer tmp = dernierCode;
						Clients.clearWrongValue(dernierCodeBoxEchan);
						dernierCodeBoxEchan.setConstraint("");
						dernierCodeBoxEchan.setValue(tmp);
						dernierCodeBoxEchan.setConstraint(cttDernierCode);
					}
				} else {
					throw new WrongValueException(
						comp, "Champ vide non autorisé. " 
						+ "Vous devez spécifier une valeur.");
				}
			} else {
				Clients.clearWrongValue(premierCodeBoxEchan);
				premierCodeBoxEchan.setConstraint("");
				premierCodeBoxEchan.setValue(null);
				premierCodeBoxEchan.setConstraint(cttPremierCode);
				Clients.clearWrongValue(dernierCodeBoxEchan);
				dernierCodeBoxEchan.setConstraint("");
				dernierCodeBoxEchan.setValue(null);
				dernierCodeBoxEchan.setConstraint(cttDernierCode);
			}
		}
	}
	private Constraint cttPremierCode = new ConstPremierCode();
	
	/**
	 * 
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstDernierCode implements Constraint {
		
		public void validate(Component comp, Object value) {
			// on ne prend en compte cette contrainte que si
			// l'utilisateur veut utiliser des chiffres en
			// numérotation
			if (numNombres.isChecked()) {
				// on récupère la valeur dans dernierCodeBoxEchan
				dernierCode = (Integer) value;
				
				// si une valeur est saisie dans le champ dernierCodeBoxEchan
				if (dernierCode != null) {
					// si le dernier code est négatif
					if (dernierCode <= 0) {
						throw new WrongValueException(
						comp, Labels.getLabel(
						"ficheMultiProdDerive.error.code.sup.zero"));
					}
					// Si le premierCode est null (1ère édition de la page)
					// on va récupérer la valeur du champ premierCodeBoxEchan
					if (premierCode == null) {
						// on enlève la contrainte de premierCodeBoxEchan pour
						// pouvoir récupérer sa valeur
						premierCodeBoxEchan.setConstraint("");
						premierCodeBoxEchan.clearErrorMessage();
						premierCode = premierCodeBoxEchan.getValue();
						// on remet la contrainte
						premierCodeBoxEchan.setConstraint(cttPremierCode);
					}
					
					// si une valeur est saisie dans le champ premierCodeBoxEchan
					if (premierCode != null) {
						// si le dernier code est < au premier
						if (dernierCode < premierCode) {
							throw new WrongValueException(
								comp, "Le premier code saisi ne peut pas être "
								+  "supérieur au dernier.");
						} else {
							
							// sinon on enlève toutes les erreurs affichées
							Integer tmp = premierCode;
							Clients.clearWrongValue(premierCodeBoxEchan);
							premierCodeBoxEchan.setConstraint("");
							premierCodeBoxEchan.setValue(tmp);
							premierCodeBoxEchan.setConstraint(cttPremierCode);
					
							List<String> doublons = 
								findDoublons(premierCode, dernierCode);
							// si des doublons existent pour les valeurs saisies
							if (doublons.size() > 0) {
								StringBuffer sb = new StringBuffer();
								if (doublons.size() == 1) {
									sb.append("Echantillon déjà enregistré " 
											+ "pour le numéro : {");
									sb.append(doublons.get(0));
								} else {
									sb.append(
										"Echantillons déjà enregistrés " 
											+ "pour les numéros : {");
									for (int i = 0; i < doublons.size(); i++) {
										sb.append(doublons.get(i));
										if (i + 1 < doublons.size()) {
											sb.append(", ");
										}
									}
								}
								sb.append(
									"}. Veuillez modifier les numéros saisis.");
								throw new WrongValueException(
								comp, sb.toString());
							} else {
								// sinon on enlève toutes les erreurs affichées
								tmp = premierCode;
								Clients.clearWrongValue(premierCodeBoxEchan);
								premierCodeBoxEchan.setConstraint("");
								premierCodeBoxEchan.setValue(tmp);
								premierCodeBoxEchan.setConstraint(cttPremierCode);
							}
						}
					} else {
						// sinon on enlève toutes les erreurs affichées
						Integer tmp = premierCode;
						Clients.clearWrongValue(premierCodeBoxEchan);
						premierCodeBoxEchan.setConstraint("");
						premierCodeBoxEchan.setValue(tmp);
						premierCodeBoxEchan.setConstraint(cttPremierCode);
					}
				} else {
					throw new WrongValueException(
						comp, "Champ vide non autorisé. " 
						+ "Vous devez spécifier une valeur.");
				}
			} else {
				Clients.clearWrongValue(premierCodeBoxEchan);
				premierCodeBoxEchan.setConstraint("");
				premierCodeBoxEchan.setValue(null);
				premierCodeBoxEchan.setConstraint(cttPremierCode);
				Clients.clearWrongValue(dernierCodeBoxEchan);
				dernierCodeBoxEchan.setConstraint("");
				dernierCodeBoxEchan.setValue(null);
				dernierCodeBoxEchan.setConstraint(cttDernierCode);
			}
		}
	}
	private Constraint cttDernierCode = new ConstDernierCode();
	
	/**
	 * 
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstPremiereLettre implements Constraint {
		
		public void validate(Component comp, Object value) {
			// on ne prend en compte cette contrainte que si
			// l'utilisateur veut utiliser des lettres en
			// numérotation
			if (numLettres.isChecked()) {
				// on récupère la valeur dans premiereLettreBoxEchan
				premiereLettre = (String) value;
				
				// si une valeur est saisie
				if (premiereLettre != null && !premiereLettre.equals("")) {
					// si le premier code est invalide
					if (!lettres.contains(premiereLettre.toUpperCase())) {
						throw new WrongValueException(
						comp, Labels.getLabel(
						"ficheMultiEchantillons.lettre.invalide"));
					}
					// Si la derniereLettre est null (1ère édition de la page)
					// on va récupérer la valeur du champ derniereLettreBoxEchan
					if (derniereLettre == null || derniereLettre.equals("")) {
						// on enlève la contrainte de 
						// derniereLettreBoxEchan pour
						// pouvoir récupérer sa valeur
						derniereLettreBoxEchan.setConstraint("");
						derniereLettreBoxEchan.clearErrorMessage();
						derniereLettre = derniereLettreBoxEchan.getValue();
						// on remet la contrainte
						derniereLettreBoxEchan.setConstraint(
								cttDerniereLettre);
					}
					
					// si une valeur est saisie dans le champ 
					// derniereLettreBoxEchan
					if (derniereLettre != null && !derniereLettre.equals("")) {
						// si la derniere est avant la premiere
						if (lettres.indexOf(derniereLettre.toUpperCase()) 
								< lettres.indexOf(premiereLettre.toUpperCase())) {
							throw new WrongValueException(
								comp, "La première lettre saisie ne "
								+ "peut pas être alphabétiquement "
								+ "après la dernière.");
						} else {
							
							// sinon on enlève toutes les erreurs affichées
							String tmp = derniereLettre;
							Clients.clearWrongValue(derniereLettreBoxEchan);
							derniereLettreBoxEchan.setConstraint("");
							derniereLettreBoxEchan.setValue(tmp);
							derniereLettreBoxEchan.setConstraint(
									cttDerniereLettre);
							
							List<String> doublons = 
								findDoublonsForLetters(
										premiereLettre.toUpperCase(), 
										derniereLettre.toUpperCase());
							// si des doublons existent pour les valeurs saisies
							if (doublons.size() > 0) {
								StringBuffer sb = new StringBuffer();
								if (doublons.size() == 1) {
									sb.append("Echantillon déjà enregistré " 
											+ "pour la lettre : {");
									sb.append(doublons.get(0));
								} else {
									sb.append(
										"Echantillons déjà enregistrés " 
											+ "pour les lettres : {");
									for (int i = 0; i < doublons.size(); i++) {
										sb.append(doublons.get(i));
										if (i + 1 < doublons.size()) {
											sb.append(", ");
										}
									}
								}
								sb.append(
									"}. Veuillez modifier les lettres saisies.");
								throw new WrongValueException(
								comp, sb.toString());
							} else {
								// sinon on enlève toutes les erreurs affichées
								tmp = derniereLettre;
								Clients.clearWrongValue(derniereLettreBoxEchan);
								derniereLettreBoxEchan.setConstraint("");
								derniereLettreBoxEchan.setValue(tmp);
								derniereLettreBoxEchan.setConstraint(
										cttDerniereLettre);
							}
						}
					} else {
						// sinon on enlève toutes les erreurs affichées
						String tmp = derniereLettre;
						Clients.clearWrongValue(derniereLettreBoxEchan);
						derniereLettreBoxEchan.setConstraint("");
						derniereLettreBoxEchan.setValue(tmp);
						derniereLettreBoxEchan.setConstraint(
								cttDerniereLettre);
					}
				} else {
					throw new WrongValueException(
						comp, "Champ vide non autorisé. " 
						+ "Vous devez spécifier une valeur.");
				}
			} else {
				Clients.clearWrongValue(premiereLettreBoxEchan);
				premiereLettreBoxEchan.setConstraint("");
				premiereLettreBoxEchan.setValue(null);
				premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
				Clients.clearWrongValue(derniereLettreBoxEchan);
				derniereLettreBoxEchan.setConstraint("");
				derniereLettreBoxEchan.setValue(null);
				derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
			}
		}
	}
	private Constraint cttPremiereLettre = new ConstPremiereLettre();
	
	/**
	 * 
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstDerniereLettre implements Constraint {
		
		public void validate(Component comp, Object value) {
			// on ne prend en compte cette contrainte que si
			// l'utilisateur veut utiliser des lettres en
			// numérotation
			if (numLettres.isChecked()) {
				// on récupère la valeur dans derniereLettreBoxEchan
				derniereLettre = (String) value;
				
				// si une valeur est saisie
				if (derniereLettre != null && !derniereLettre.equals("")) {
					// si la derniere lettre n'est pas valide
					if (!lettres.contains(derniereLettre.toUpperCase())) {
						throw new WrongValueException(
						comp, Labels.getLabel(
						"ficheMultiEchantillons.lettre.invalide"));
					}
					// Si la premiere lettre est null (1ère édition de la page)
					// on va récupérer la valeur 
					// du champ premiereLettreBoxEchan
					if (premiereLettre == null || premiereLettre.equals("")) {
						// on enlève la contrainte pour
						// pouvoir récupérer sa valeur
						premiereLettreBoxEchan.setConstraint("");
						premiereLettreBoxEchan.clearErrorMessage();
						premiereLettre = premiereLettreBoxEchan.getValue();
						// on remet la contrainte
						premiereLettreBoxEchan.setConstraint(
								cttPremiereLettre);
					}
					
					// si une valeur est saisie dans le champ 
					// premiereLettreBoxEchan
					if (premiereLettre != null && !premiereLettre.equals("")) {
						// si le dernier code est < au premier
						// si la derniere est avant la premiere
						if (!lettres.contains(premiereLettre.toUpperCase())
								|| lettres.indexOf(derniereLettre.toUpperCase()) 
								< lettres.indexOf(premiereLettre.toUpperCase())) {
							throw new WrongValueException(
								comp, "La première lettre saisie ne "
								+ "peut pas être alphabétiquement "
								+ "après la dernière.");
						} else {
							// sinon on enlève toutes les erreurs affichées
							String tmp = premiereLettre;
							Clients.clearWrongValue(premiereLettreBoxEchan);
							premiereLettreBoxEchan.setConstraint("");
							premiereLettreBoxEchan.setValue(tmp);
							premiereLettreBoxEchan.setConstraint(
									cttPremiereLettre);
							
							List<String> doublons = 
								findDoublonsForLetters(
										premiereLettre.toUpperCase(), 
										derniereLettre.toUpperCase());
							// si des doublons existent pour les valeurs saisies
							if (doublons.size() > 0) {
								StringBuffer sb = new StringBuffer();
								if (doublons.size() == 1) {
									sb.append("Echantillon déjà enregistré " 
											+ "pour la lettre : {");
									sb.append(doublons.get(0));
								} else {
									sb.append(
										"Echantillons déjà enregistrés " 
											+ "pour les lettres : {");
									for (int i = 0; i < doublons.size(); i++) {
										sb.append(doublons.get(i));
										if (i + 1 < doublons.size()) {
											sb.append(", ");
										}
									}
								}
								sb.append(
									"}. Veuillez modifier les lettres saisies.");
								throw new WrongValueException(
								comp, sb.toString());
							} else {
								// sinon on enlève toutes les erreurs affichées
								tmp = premiereLettre;
								Clients.clearWrongValue(premiereLettreBoxEchan);
								premiereLettreBoxEchan.setConstraint("");
								premiereLettreBoxEchan.setValue(tmp);
								premiereLettreBoxEchan.setConstraint(
										cttPremiereLettre);
							}
						}
					} else {
						// sinon on enlève toutes les erreurs affichées
						String tmp = premiereLettre;
						Clients.clearWrongValue(premiereLettreBoxEchan);
						premiereLettreBoxEchan.setConstraint("");
						premiereLettreBoxEchan.setValue(tmp);
						premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
					}
				} else {
					throw new WrongValueException(
						comp, "Champ vide non autorisé. " 
						+ "Vous devez spécifier une valeur.");
				}
			} else {
				Clients.clearWrongValue(premiereLettreBoxEchan);
				premiereLettreBoxEchan.setConstraint("");
				premiereLettreBoxEchan.setValue(null);
				premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
				Clients.clearWrongValue(derniereLettreBoxEchan);
				derniereLettreBoxEchan.setConstraint("");
				derniereLettreBoxEchan.setValue(null);
				derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
			}
		}
	}
	private Constraint cttDerniereLettre = new ConstDerniereLettre();

	@Override
	public void clearConstraints() {
		//super.clearConstraints();
		Clients.clearWrongValue(codesParentBoxEchan);
		Clients.clearWrongValue(premierCodeBoxEchan);
		Clients.clearWrongValue(dernierCodeBoxEchan);
		Clients.clearWrongValue(quantiteInitBoxEchan);
		Clients.clearWrongValue(dateStockCalBox);
		Clients.clearWrongValue(sterileBox);	
		Clients.clearWrongValue(premiereLettreBoxEchan);
		Clients.clearWrongValue(derniereLettreBoxEchan);
	}
	
	/**
	 * Applique la validation sur la sterilite.
	 */
	public void onCheck$sterileBox() {
		Clients.clearWrongValue(sterileBox);
		//validateSterilite(sterileBox.isChecked());
	}
	
	/**
	 * Valide l'application de la sterilite au niveau echantillon.
	 * @param value
	 */
	private void validateSterilite(boolean value) {
		Errors errs = null;
		//String field = "";
		
		if (value) { 
			//field = "sterile";
			// test objet
			Echantillon testEchan = new Echantillon();
			testEchan.setSterile(true);
			Prelevement prel = null;
			if (isPrelevementProcedure) {
				prel = getParentObject();
			} else {
				prel = getPrelevementFromSelect();
			}
			if (prel != null) {
				if (getLaboInters() != null
						&& !getLaboInters().isEmpty()) {
					prel.setLaboInters(new HashSet<LaboInter>(getLaboInters()));
				} else if (prel.getPrelevementId() != null) {
					prel
						.setLaboInters(new HashSet<LaboInter>(ManagerLocator
							.getPrelevementManager()
							.getLaboIntersWithOrderManager(prel)));
				} 
				testEchan.setPrelevement(prel);
				
				errs = ManagerLocator.getEchantillonValidator()
									.checkSteriliteAntecedence(testEchan);
				
				prel.setLaboInters(null);
			}
		}
			
		if (errs != null && errs.hasErrors()) {			
//			throw new WrongValueException(
//				sterileBox, handleErrors(errs, field));
			List<Errors> errors = new ArrayList<Errors>();
			errors.add(errs);
			throw new ValidationException(errors);
		}			
	}

	/*************************************************************************/
	/************************** PROCEDURE PRELEVEMENT*************************/
	/*************************************************************************/	
	/**
	 * Change mode de la fiche en mode create depuis la procedure
	 * de  creation d'un prelevement.
	 */
	public void switchToEditMode(Prelevement prel) {
		
		// passage prelevement
		setParentObject(prel);
			
		setIsCreate(false);
		switchButtons();
		
		Clients.scrollIntoView(formGrid.getColumns());
		
		// Initialisation du mode (listes, valeurs...)
		initEditableMode();
		initQuantiteAndVolume();
		initAssociations();
	
		if (!getDroitOnAction("Collaborateur", "Consultation")) {
			operateurAideSaisieEchan.setVisible(false);
		}	
		
		setFocusOnElement();
		
		getBinder().loadComponent(self);
	}
	
	/*************************************************************************/
	/************************** PARENT    ************************************/
	/*************************************************************************/
	// Objets pour la sélection du parent
	private List<String> codesParent = new ArrayList<String>();
	private CustomSimpleListModel dictCodesModel;
	
	private String selectedCodeParent;
	private boolean selectParent = false;
	
	// Mode de sélection du parent
	private Row selectParentRowEchan;
	private Row prelRow;
	private Row unknownPrelRow;
	private Combobox codesParentBoxEchan;
	private Label codeParentLabelEchan;
	private Label requiredCodeParentEchan;
	private Listbox connaissancesBoxEchan;
	
	 // Variable contenant le code prlvt saisi par l'utilisateur.
	private String codeTmp;
	
	public List<String> getCodesParent() {
		return codesParent;
	}

	public void setCodesParent(List<String> codes) {
		this.codesParent = codes;
	}

	public CustomSimpleListModel getDictCodesModel() {
		return dictCodesModel;
	}

	public void setDictCodesModel(CustomSimpleListModel dictCodes) {
		this.dictCodesModel = dictCodes;
	}

	public String getSelectedCodeParent() {
		return selectedCodeParent;
	}

	public void setSelectedCodeParent(String selectedCode) {
		this.selectedCodeParent = selectedCode;
	}
	
	/**
	 * Cache le mode de sélection du prlvt parent.
	 */
	public void removeSelectParentMode() {
		selectParent = false;
		
		// on cache les éléments.
		selectParentRowEchan.setVisible(false);
		prelRow.setVisible(true);
		//codesParentBoxEchan.setVisible(false);
		//codeParentLabelEchan.setVisible(false);
		//requiredCodeParentEchan.setVisible(false);
		
		// on réinitialise les listes de codes.
		// codesParent.clear();
		// dictCodesModel = new SimpleListModel(codesParent);
		// codesParentBoxEchan.setConstraint("");
		// codesParentBoxEchan.clearErrorMessage();
		// codesParentBoxEchan.setValue("");
		// codesParentBoxEchan.setModel(dictCodesModel);
		// codesParentBoxEchan.setConstraint(cttCodeParentEchan);
		
	}
	
	/**
	 * Lors de la création d'un échantillon, si son prlvt parent n'est
	 * pas fourni, on propose une sélection de celui-ci.
	 * Cette méthode permet l'affichage de cette sélection.
	 */
	public void addSelectParentMode() {
		selectParent = true;
		// setParentObject(new Prelevement());
		setParentObject(null);
		
		// affichage des éléments
		selectParentRowEchan.setVisible(true);
		connaissancesBoxEchan.setSelectedIndex(0);
		prelRow.setVisible(false);
		//codesParentBoxEchan.setVisible(true);
		//codeParentLabelEchan.setVisible(true);
		//requiredCodeParentEchan.setVisible(true);
		
		// on récupère la liste des codes des prlvts
		codesParent = ManagerLocator.getPrelevementManager().
		findAllCodesForBanqueManager(SessionUtils
							.getSelectedBanques(sessionScope).get(0));
		// ces codes sont placés dans un dictionnaire pour permettre
		// le filtre lorsque l'utilisateur saisit le code
		dictCodesModel = new CustomSimpleListModel(codesParent.toArray());
		codesParentBoxEchan.setModel(dictCodesModel);
	}
	
	/**
	 * Sélection d'un code de prélèvement.
	 * @param event Event : sélection dans la combobox codesParentBoxEchan.
	 * @throws Exception
	 */
	/*public void onSelect$codesParentBoxEchan(Event event) throws Exception {
		if (codesParentBoxEchan.getSelectedItem() != null) {
			setCodePrefixe(codesParentBoxEchan.getSelectedItem().getLabel());
		}
		dateStockCalBox.setHasChanged(false);
	}*/
	
	/**
	 * Mise à jour de la variable codeTmp à chaque modification
	 * de l'utilisateur.
	 * @param event Event : modification dans la combobox codesParentBoxEchan.
	 * @throws Exception
	 */
	/*public void onChanging$codesParentBoxEchan(
			InputEvent event) throws Exception {
		codeTmp = event.getValue();
	}*/
	
	/**
	 * Mise à jour de la valeur sélectionné dans la combobox lorsque
	 * l'utilisateur clique à l'extérieur de celle-ci.
	 * @param event Event : clique à l'extérieur de la combobox 
	 * codesParentBoxEchan.
	 * @throws Exception
	 */
	public void onBlur$codesParentBoxEchan(Event event) throws Exception {
		// on enlève la contrainte de la combobox
		codesParentBoxEchan.setConstraint("");
		codesParentBoxEchan.clearErrorMessage();
		// si aucun élément n'est sélectionné, la valeur de la box correspond 
		// à ce qu'a tapé l'utilisateur.
		if (codesParentBoxEchan.getSelectedItem() == null) {
			codesParentBoxEchan.setValue(codeTmp);
		} else {
			// on valide la sélection
			cttCodeParentEchan.validate(codesParentBoxEchan, null);
			
			setCodePrefixe(codesParentBoxEchan.getSelectedItem().getLabel());
			
			// on récupère le prlvt en fonction de son code
			setParentObject(ManagerLocator.getPrelevementManager()
				.findByCodeOrNumLaboLikeWithBanqueManager(getCodePrefixe(), 
						SessionUtils.getSelectedBanques(sessionScope).get(0), 
																true).get(0));
			
			calculDelaiCgl();
			clearConstraints();
			
			initAssociations();
			
			sterileBox.setChecked(initSterileOrNot());
			codePrefixeEchan.setValue(getCodePrefixe());
		}
		
		setPremierCode(null);
		setPremiereLettre(null);
		setDernierCode(null);
		setDerniereLettre(null);
		
		// on ré-active la contrainte
		//codesParentBoxEchan.setModel(dictCodesModel);
		//codesParentBoxEchan.setConstraint(cttCodeParentEchan);
		cttCodeParentEchan.validate(codesParentBoxEchan, null);
	}
	
	public void onSelect$connaissancesBoxEchan() {		
		int ind = connaissancesBoxEchan.getSelectedIndex();
		clearForm(false);
		if (ind > 0) {
			setParentObject(null);
			codeParentLabelEchan.setVisible(false);
			requiredCodeParentEchan.setVisible(false);
			codesParentBoxEchan.setVisible(false);
			
			codesParentBoxEchan.setConstraint("");
			codesParentBoxEchan.clearErrorMessage();
			codesParentBoxEchan.setValue("");
			connaissancesBoxEchan.setSelectedIndex(1);
		} else {
			codeParentLabelEchan.setVisible(true);
			requiredCodeParentEchan.setVisible(true);
			codesParentBoxEchan.setVisible(true);
			
			codesParentBoxEchan.setConstraint("");
			codesParentBoxEchan.clearErrorMessage();
			codesParentBoxEchan.setValue("");
			// codesParentBoxEchan.setConstraint(cttCodeParentEchan);
			connaissancesBoxEchan.setSelectedIndex(0);
		}
		
		calculDelaiCgl();
		clearConstraints();
		
		initAssociations();
		
		dateStockCalBox.setHasChanged(false);
	}
	
	/**
	 * Clic sur le bouton générant un code.
	 */
	public void onClick$numerotation() {
//		if (getCodePrefixe() == null || getCodePrefixe().equals("")
//			|| 
		if (getParentObject() != null 
				&& !getCodePrefixe().equals(getParentObject().getCode())) {
			setCodePrefixe(generateCodeAndUpdateNumerotation());
		}
	}
	
	/*********************************************************/
	/********************** VALIDATION ***********************/
	/*********************************************************/
	/**
	 * Contrainte vérifiant que la combobox pour le code du parent n'est
	 * pas nulle et que son contenu se trouve bien dans la liste.
	 * @author Pierre Ventadour.
	 *
	 */
	public class ConstCodeParentEchan implements Constraint {
		/**
		 * Méthode de validation du champ dateTransfoBox.
		 */
		public void validate(Component comp, Object value) {
			codesParentBoxEchan.setConstraint("");
			codesParentBoxEchan.clearErrorMessage();
			String code = "";
			if (codesParentBoxEchan.getSelectedItem() != null) {
				code = codesParentBoxEchan.getSelectedItem().getLabel();
				//codesParentBoxEchan.setValue(code);
			} else {
				code = codesParentBoxEchan.getValue();
			}
			// codesParentBoxEchan.setConstraint(cttCodeParentEchan);
			
			if (code.length() == 0) {
				throw new WrongValueException(
					comp, "Champ vide non autorisé. Vous devez " 
					+ "spécifier un code présent dans la liste.");
			} else if (!codesParent.contains(code)) {
				throw new WrongValueException(
					comp, "Valeur non autorisée. Vous devez spécifier " 
					+ "une valeur présente dans la liste.");
			}
		}
	}
	
	private Constraint cttCodeParentEchan = new ConstCodeParentEchan();
	
	public Constraint getCttCodeParentEchan() {
		return cttCodeParentEchan;
	}
	
	/**
	 * Applique la validation sur la date.
	 */
	public void onBlur$dateStockCalBox() {
		Datebox box = (Datebox) dateStockCalBox
			.getFirstChild().getFirstChild();
		boolean badDateFormat = false;
		if (box.getErrorMessage() != null
				&& box.getErrorMessage().contains(
						box.getFormat())) {
			badDateFormat = true;
		} else {
			Calendar calValue = Calendar.getInstance();
			if (box.getValue() != null) {
				calValue.setTime(box.getValue());
				if (calValue != null && !calValue.equals("")) {
					if (calValue.get(Calendar.YEAR) > 9999) {
						badDateFormat = true;
					}
				}
			}
		}
		if (!badDateFormat) {
			// recupere le prelevement parent au besoin
			if (getParentObject() == null 
					&& connaissancesBoxEchan.isVisible()) {
				setParentObject(getPrelevementFromSelect());
			}
			if (getParentObject() != null) {
				getEchantillon().setPrelevement((Prelevement) 
						getParentObject());
				if (!dateStockCalBox.isHasChanged() 
						&& dateStockCalBox.getValue() == null) {
						dateStockCalBox.setValue(ObjectTypesFormatters
								.getDateWithoutHoursAndMins(getParentObject()
										.getDatePrelevement()));
						getEchantillon().setDateStock(
								dateStockCalBox.getValue());
				} else {
					dateStockCalBox.clearErrorMessage(
							dateStockCalBox.getValue());
					validateCoherenceDate(dateStockCalBox, 
									dateStockCalBox.getValue());
				}
			}
			getEchantillon().setDateStock(dateStockCalBox.getValue());
			calculDelaiCgl();
			dateStockCalBox.setHasChanged(true);
		} else {
			throw new WrongValueException(
					dateStockCalBox, 
					Labels.getLabel("validation.invalid.date"));
		}
	}
	
	/**
	 * Indique si le champ stérile doit être true ou false 
	 * par défaut dans le cas de procédure d'enregistrement 
	 * d'un prélèvement.
	 * @return true/false.
	 */
	private boolean initSterileOrNot() {		
		if (getParentObject() == null ||
			getParentObject().getSterile() == null 
				|| !getParentObject().getSterile()) {
			return false;
		}
		
		
		Collection<LaboInter> labs = null;
		// aucun nouveau labo créé
		if (getLaboInters() == null || getLaboInters().isEmpty()) {
			if (getParentObject().getPrelevementId() != null) {
				labs = ManagerLocator
						.getPrelevementManager()
							.getLaboIntersWithOrderManager(getParentObject());
			} else {
				labs = new ArrayList<LaboInter>();
			}
		} else {
			labs = getLaboInters();
		}
		
		Iterator<LaboInter> it = labs.iterator();
		while (it.hasNext()) {
			if (!it.next().getSterile()) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/*************************************************************************/
	/************************** INTERFACAGES *********************************/
	/*************************************************************************/
	public void initFormFromDossierExterne() {
		ResultatInjection res = SessionUtils.getDossierExterneInjection(sessionScope);
		// getObjectTabController().setDossierExterne(
		//		res.getDossierExterne());
		
		if (res != null) {
		
			Echantillon newObj = null;
			if (res.getEchantillon() != null) {
				newObj = res.getEchantillon().clone();
				newObj.setBanque((Banque) sessionScope.get("Banque"));
				newObj.setPrelevement(getParentObject());
				setEchantillon(newObj);
				setSelectedType(newObj.getEchantillonType());
				setSelectedPrepa(newObj.getModePrepa());
				setSelectedLateralite(newObj.getLateralite());
				setSelectedQualite(newObj.getEchanQualite());
				setSelectedCollaborateur(newObj.getCollaborateur());
				setSelectedQuantiteUnite(newObj.getQuantiteUnite());
			}
			
			if (getObjectTabController() != null 
					&& getObjectTabController().getFicheAnnotation() != null
					&& res.getAnnosEchantillon() != null) {
				getObjectTabController().getFicheAnnotation()
											.setAnnotationValues(
													res.getAnnosEchantillon());
			}	
			
		//	if (getObjectTabController().canUpdateAnnotation()) {
		//		getObjectTabController().getFicheAnnotation()
		//			.switchToStaticOrEditMode(false, false);
		//	}
			
			// codes
			getCodesOrganeController().addCodesFromInjection(res.getCodesOrgane());
			getCodesMorphoController().addCodesFromInjection(res.getCodesMorpho());
			
			// cr anapath
			if (res.getCrAnapath() != null) {
				setAnapathStream(res.getStream());
				crAnapathNomBox.setValue(res.getCrAnapath().getNom());
				showDeleteAndFileNameBox(true);
				getCrAnapathConstraint()
					.validate(crAnapathNomBox, res.getCrAnapath().getNom());
			}
		}
	}
	
	@Override
	public void setLaboInters(List<LaboInter> labos) {
		super.setLaboInters(labos);
		if (isPrelevementProcedure) {
			sterileBox.setChecked(initSterileOrNot());
		}
	}

	public String getPremiereLettre() {
		return premiereLettre;
	}

	public void setPremiereLettre(String l) {
		this.premiereLettre = l;
	}

	public String getDerniereLettre() {
		return derniereLettre;
	}

	public void setDerniereLettre(String l) {
		this.derniereLettre = l;
	}

	public Constraint getCttPremiereLettre() {
		return cttPremiereLettre;
	}

	public void setCttPremiereLettre(Constraint c) {
		this.cttPremiereLettre = c;
	}

	public Constraint getCttDerniereLettre() {
		return cttDerniereLettre;
	}

	public void setCttDerniereLettre(Constraint c) {
		this.cttDerniereLettre = c;
	}

	/**
	 * Applique les codes obtenus depuis le scan dans l'ordre aux codes échantillons 
	 * en attente de création dans la liste de travail.
	 * @param sT scanTerminale
	 */
	public void applyTKObjectsCodesFromScan(ScanTerminale sT) {
		if (!getAddedEchantillons().isEmpty() 
								&& !sT.getScanTubes().isEmpty()) {
			int i = 0;
//			for (ScanTube tube : sT.getScanTubes()) {
//				if (i < getAddedEchantillons().size()) {
//					((Echantillon) getAddedEchantillons().get(i)).setCode(tube.getCode());
//					i++;
//				} else {
//					break;
//				}
//			}
//			getBinder().loadAttribute(echantillonsList, "model");
			Textbox tb;
			for (EchantillonDTO deco : getEchantillonsDecorated()) {
				if (deco.isNew() && deco.getAdrlTmp() == null) {
					if (i < sT.getScanTubes().size()) {
						tb = (Textbox) echantillonsList.getRows().getChildren()
							.get(getEchantillonsDecorated().indexOf(deco))
							.getFirstChild().getNextSibling();
						tb.setValue(sT.getScanTubes().get(i).getCode());
						Events.postEvent("onChange", tb, null);
						i++;
					} else {
						break;
					}
				}
			}
		}
		
	}	
}
