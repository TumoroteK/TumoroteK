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
package fr.aphp.tumorotek.action.controller;

import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.CancelFromModalException;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.cession.DateRetourCompleteModale;
import fr.aphp.tumorotek.action.cession.FicheCessionEdit;
import fr.aphp.tumorotek.action.cession.retour.FicheRetour;
import fr.aphp.tumorotek.action.cession.retour.ListeRetour;
import fr.aphp.tumorotek.action.code.CodesController;
import fr.aphp.tumorotek.action.contexte.CollaborationsController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.etiquettes.FicheEtiquetteDynModale;
import fr.aphp.tumorotek.action.etiquettes.FicheEtiquetteModale;
import fr.aphp.tumorotek.action.impression.FicheHeadersModale;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.ConsentTypeUsedTable;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.action.recherche.ResultatsModale;
import fr.aphp.tumorotek.action.stockage.FicheAddIncident;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.action.utilisateur.FichePasswordModale;
import fr.aphp.tumorotek.action.utilisateur.ProfilExport;
import fr.aphp.tumorotek.component.ProgressBarComponent;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DeriveBatchSaveException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ExistingAnnotationValuesException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.StringEtiquetteOverSizeException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.exception.TransformationQuantiteOverDemandException;
import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.DeleteModale;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.2.1-IRELEC
 */
public abstract class AbstractController extends GenericForwardComposer<Component>
{
	protected static Log log = LogFactory.getLog(AbstractController.class);

	private static final long serialVersionUID = -3799945305452822008L;

	private boolean blockModal = false;

	// Panel contenant la fiche
	protected Panel winPanel;
	// Panelchildren contenant le scroll de la fiche
	protected Panelchildren panelChildrenWithScroll;

	private AnnotateDataBinder binder;
	// ProgressBar component
	private HtmlMacroComponent progressBar;
	private Window winProgressBar;
	private boolean isAnonyme;

	public AnnotateDataBinder getBinder(){
		return binder;
	}

	public void setBinder(final AnnotateDataBinder b){
		this.binder = b;
	}

	public boolean isBlockModal(){
		return blockModal;
	}

	public void setBlockModal(final boolean b){
		this.blockModal = b;
	}

	public boolean isAnonyme(){
		return isAnonyme;
	}

	public void setAnonyme(final boolean isAno){
		this.isAnonyme = isAno;
	}

	/**
	 * Mets à jour les variables contenant la taille de l'écran dans la page
	 * MainWindow.
	 */
	public void updateApplicationScreenSize(){
		// on récupère la taille de l'écran
		if(sessionScope.containsKey("screenHeight")){
			final Integer screenHeight = (Integer) sessionScope.get("screenHeight");
			getMainWindow().setScreenHeight(screenHeight);
		}
		if(sessionScope.containsKey("screenWidth")){
			final Integer screenWidth = (Integer) sessionScope.get("screenWidth");
			getMainWindow().setScreenWidth(screenWidth);
		}
		getMainWindow().setScreenUpdated(true);
	}

	public MainWindow getMainWindow(){
		return (MainWindow) page.getFellow("mainWin").getAttributeOrFellow("mainWin$composer", true);
	}

	/*************************************************************************/
	/************************** DROITS ***************************************/
	/*************************************************************************/
	/**
	 * Renvoie un boolean pour indiquer si un bouton cliquable en fonction des
	 * droits de l'utilisateur.
	 * 
	 * @param nomEntite
	 *            Entite (ex.:ProdDerive).
	 * @param nomOperation
	 *            Type d'operation du bouton.
	 */

	public boolean drawActionOnOneButton(final String nomEntite, final String nomOperation){
		Boolean admin = false;
		if(sessionScope.containsKey("Admin")){
			admin = (Boolean) sessionScope.get("Admin");
		}

		// si l'utilisateur est admin => bouton cliquable
		if(admin){
			return true;
		}
		// on extrait l'OperationType de la base
		final OperationType operation = ManagerLocator.getOperationTypeManager().findByNomLikeManager(nomOperation, true).get(0);

		Hashtable<String, List<OperationType>> droits = new Hashtable<>();

		if(sessionScope.containsKey("Droits")){
			// on extrait les droits de l'utilisateur
			droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

			final List<OperationType> ops = droits.get(nomEntite);
			return (ops.contains(operation));
		}
		return false;
	}

	/**
	 * Test si une action est réalisable en fonction des droits de
	 * l'utilisateur.
	 * 
	 * @param nomEntite
	 *            Entite (ex.:ProdDerive).
	 * @param nomOperation
	 *            Type d'operation du bouton.
	 */

	public boolean getDroitOnAction(final String nomEntite, final String nomOperation){
		Boolean admin = false;
		if(sessionScope.containsKey("Admin")){
			admin = (Boolean) sessionScope.get("Admin");
		}

		// si l'utilisateur est admin => bouton cliquable
		if(admin){
			return true;
		}
		// on extrait l'OperationType de la base
		final OperationType opeation = ManagerLocator.getOperationTypeManager().findByNomLikeManager(nomOperation, true).get(0);

		Hashtable<String, List<OperationType>> droits = new Hashtable<>();

		if(sessionScope.containsKey("Droits")){
			// on extrait les droits de l'utilisateur
			droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

			final List<OperationType> ops = droits.get(nomEntite);
			return ops.contains(opeation);
		}
		return false;

	}

	/*************************************************************************/
	/************************** MODALES ***************************************/
	/*************************************************************************/

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * pour voir recherché les patients existants lors de la
	 * création d'un nouveau prélèvement.
	 * @param page dans laquelle inclure la modale
	 * @param path Chemin vers la page ayant appelée cette modale.
	 * @param critere Critere de recherche des patients.
	 * @param patient à exclure en cas de fusion
	 */
	public void openSelectPatientWindow(final String path, final String returnMethode, final Boolean isFusionPatients,
			final String critere, final Patient patAExclure){

		final HashMap<String, Object> map = new HashMap<>();
		map.put("path", path);
		map.put("methode", returnMethode);
		map.put("isFusion", isFusionPatients);
		map.put("critere", critere);
		map.put("patAExclure", patAExclure);

		final Window window = (Window) Executions.createComponents("/zuls/prelevement/SelectPatientModale.zul", null, map);
		window.doModal();
	}

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * operateurAideSaisieEchan. Cette méthode va créer une nouvelle fenêtre
	 * contenant l'aide pour la sélection d'un collaborateur.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param title
	 *            code pour titre dans .properties internationalisation
	 * @param mode
	 *            select ou detaillé
	 * @param obj
	 *            dont on veut afficher les details
	 * @param entite
	 *            Nom de l'entite pour laquelle on peut selectionner une
	 *            instance
	 * @param list
	 *            des objects a ouvrir pour pre-selection
	 * @param path
	 *            vers le composant parent declenchant la modal.
	 * @param oldSelection
	 *            Liste des objets qui étaient sélectionnés.
	 */
	public void openCollaborationsWindow(final Page page, final String title, final String mode, final Object obj,
			final String entite, final List<Object> list, final String path, final List<Object> oldSelection){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("aideWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel(title));
			win.setBorder("normal");
			win.setWidth("800px");
			final int height = getMainWindow().getPanelHeight() + 35;
			win.setHeight(height + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua = populateModal(win, page, mode, obj, entite, list, path, oldSelection);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					// progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				blockModal = false;

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	private static HtmlMacroComponent populateModal(final Window win, final Page page, final String mode, final Object obj,
			final String entite, final List<Object> list, final String path, final List<Object> oldSelection){
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent) page.getComponentDefinition("collaborations", false).newInstance(page, null);
		ua.setParent(win);
		ua.setId("openCollaborations");
		ua.applyProperties();
		ua.afterCompose();

		if("select".equals(mode)){
			((CollaborationsController) ua.getFellow("winCollaborations").getAttributeOrFellow("winCollaborations$composer", true))
			.switchToSelectMode(entite, path, list, oldSelection);
		}else if("details".equals(mode)){
			((CollaborationsController) ua.getFellow("winCollaborations").getAttributeOrFellow("winCollaborations$composer", true))
			.switchToDetailMode();
			((CollaborationsController) ua.getFellow("winCollaborations").getAttributeOrFellow("winCollaborations$composer", true))
			.switchToFicheCollaborateurMode((Collaborateur) obj);
			((CollaborationsController) ua.getFellow("winCollaborations").getAttributeOrFellow("winCollaborations$composer", true))
			.getListeCollaborations().openCollaborateurInTree((Collaborateur) obj, true, true);
			((CollaborationsController) ua.getFellow("winCollaborations").getAttributeOrFellow("winCollaborations$composer", true))
			.switchToOnlyFicheMode();
		}
		return ua;
	}

	/**
	 * Ouvre la modale assistant codes en lui spécifiant l'objet les paramètres
	 * morpho et organe. Si code diagnostic maladie, passe un paramètre boolean
	 * isDiagnostic a true.
	 * 
	 * @param page
	 * @param path
	 * @param isMorpho
	 * @param isOrgane
	 * @param isDiagnostic
	 * @param toCims
	 *            pour afficher catégories CIMS
	 */
	public void openCodesModal(final Page page, final String path, final boolean isMorpho, final boolean isOrgane,
			final boolean isDiagnostic, final boolean toCims){

		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("winCodesModal");
			win.setPage(page);
			win.setPosition("left,top");
			win.setHeight((getMainWindow().getWindowAvailableHeight()) + "px");
			win.setWidth((getMainWindow().getWindowAvailableWidth()) + "px");
			win.setMaximizable(true);
			win.setSizable(true);

			String[] titleParam = new String[] {};
			if(isOrgane){
				titleParam = new String[] {Labels.getLabel("codes.modal.title.organe")};
			}else if(isMorpho){
				titleParam = new String[] {Labels.getLabel("codes.modal.title.morpho")};
			}else if(isDiagnostic){
				titleParam = new String[] {Labels.getLabel("codes.modal.title.diagno")};
			}else{
				titleParam = new String[] {Labels.getLabel("codes.modal.title.general")};
			}
			win.setTitle(ObjectTypesFormatters.getLabel("code.component.title", titleParam));
			win.setBorder("normal");
			win.setClosable(true);

			final HtmlMacroComponent ua =
					(HtmlMacroComponent) page.getComponentDefinition("codesModale", false).newInstance(page, null);

			ua.setParent(win);
			ua.setId("codesModalMacro");
			ua.applyProperties();
			ua.afterCompose();
			ua.setVisible(false);

			((CodesController) ua.getFellow("winCodes").getAttributeOrFellow("winCodes$composer", true)).init(path, isOrgane,
					isMorpho, isDiagnostic, toCims);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					ua.setVisible(true);
				}
			});
			final Timer timer = new Timer();
			timer.setDelay(200);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			try{
				win.onModal();
				blockModal = false;

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	// /**
	// * Méthode appelée lorsque l'utilisateur clique sur un lien pour
	// * sélectionner plusieurs échantillons ou dérivés. Une fenêtre modal
	// * souvrira pour permettre cette sélection.
	// * @param page dans laquelle inclure la modale
	// * @param title code pour titre dans .properties internationalisation
	// * @param entite Nom de l'entite pour laquelle on peut selectionner
	// * une instance
	// * @param path vers le composant parent declenchant la modal.
	// */
	// public static void openEchantillonsOrDerivesWindow(Page page, String
	// title,
	// String entite, String path) {
	//
	// // nouvelle fenêtre
	// final Window win = new Window();
	// win.setVisible(false);
	// win.setId("selectEchantillonsWindow");
	// win.setPage(page);
	// win.setMaximizable(true);
	// win.setSizable(true);
	// win.setTitle(Labels.getLabel(title));
	// win.setBorder("normal");
	// win.setWidth("700px");
	// win.setHeight("600px");
	// win.setClosable(true);
	//
	// if (entite.equals("Echantillon")) {
	// final HtmlMacroComponent ua =
	// populateEchantillonsModal(win, page, path);
	// ua.setVisible(false);
	//
	// win.addEventListener("onTimed", new EventListener<Event>() {
	// public void onEvent(Event event) throws Exception {
	// //progress.detach();
	// ua.setVisible(true);
	// }
	// });
	// } else if (entite.equals("ProdDerive")) {
	// final HtmlMacroComponent ua =
	// populateProdDerivesModal(win, page, path);
	// ua.setVisible(false);
	//
	// win.addEventListener("onTimed", new EventListener<Event>() {
	// public void onEvent(Event event) throws Exception {
	// //progress.detach();
	// ua.setVisible(true);
	// }
	// });
	// }
	//
	// Timer timer = new Timer();
	// timer.setDelay(500);
	// timer.setRepeats(false);
	// timer.addForward("onTimer", timer.getParent(), "onTimed");
	// win.appendChild(timer);
	// timer.start();
	//
	// try {
	// win.onModal();
	//
	// } catch (SuspendNotAllowedException e) { log.error(e);
	// } catch (InterruptedException e) { log.error(e); }
	// }
	//
	// /**
	// * Méthode appelée pour créer le composant contenant la liste des
	// * échantillons.
	// * @param win Window contenant le composant.
	// * @param page Page contenant la définition du composant.
	// * @param path Chemin vers la page qui demande une sélection.
	// * @return Un HtmlMacroComponent contenant la liste des échantillons.
	// */
	// private static HtmlMacroComponent populateEchantillonsModal(
	// Window win, Page page, String path) {
	// // HtmlMacroComponent contenu dans la fenêtre : il correspond
	// // au composant des collaborations.
	// HtmlMacroComponent ua;
	// ua = (HtmlMacroComponent)
	// page.getComponentDefinition("echantillon", false)
	// .newInstance(page, null);
	// ua.setParent(win);
	// ua.setId("openEchantillons");
	// ua.applyProperties();
	// ua.afterCompose();
	//
	// ((EchantillonController) ua.getFellow("winEchantillon")
	// .getAttributeOrFellow("winEchantillon$composer", true))
	// .switchToSelectMode(path);
	//
	// return ua;
	// }
	//
	// /**
	// * Méthode appelée pour créer le composant contenant la liste des
	// * dérivés.
	// * @param win Window contenant le composant.
	// * @param page Page contenant la définition du composant.
	// * @param path Chemin vers la page qui demande une sélection.
	// * @return Un HtmlMacroComponent contenant la liste des dérivés.
	// */
	// private static HtmlMacroComponent populateProdDerivesModal(
	// Window win, Page page, String path) {
	// // HtmlMacroComponent contenu dans la fenêtre : il correspond
	// // au composant des collaborations.
	// HtmlMacroComponent ua;
	// ua = (HtmlMacroComponent)
	// page.getComponentDefinition("prodDerive", false)
	// .newInstance(page, null);
	// ua.setParent(win);
	// ua.setId("openProdDerives");
	// ua.applyProperties();
	// ua.afterCompose();
	//
	// ((ProdDeriveController) ua.getFellow("winProdDerive")
	// .getAttributeOrFellow("winProdDerive$composer", true))
	// .switchToSelectMode(path);
	//
	// return ua;
	// }

	/**
	 * Génère le message qui sera affiché dans la fenêtre d'erreurs.
	 * @throws WrongValueException 
	 */
	public static String handleExceptionMessage(final Exception ex) throws WrongValueException{
		StringBuilder message = new StringBuilder(Labels.getLabel("validation.exception.inconnu"));
		if(ex instanceof ValidationException){
			message = new StringBuilder(Labels.getLabel("validation.error"));
			message.append("\n");
			final Iterator<Errors> errs = (((ValidationException) ex).getErrors()).iterator();
			String errCode;
			while(errs.hasNext()){
				errCode = errs.next().getFieldError().getCode();
				if(Labels.getLabel(errCode) != null){
					message.append(Labels.getLabel(errCode));
				}else{
					message.append("TK erreur label: ").append(errCode);
				}

				message.append("\n");
			}
		}else if(ex instanceof EmplacementDoublonFoundException){
			return ObjectTypesFormatters.getLabel(ex.getMessage(),
					new String[] {((EmplacementDoublonFoundException) ex).getTerminale().getNom(),
							(((EmplacementDoublonFoundException) ex).getPosition()).toString()});
		}else if(ex instanceof DoublonFoundException){
			message = new StringBuilder(ObjectTypesFormatters.getLabel("validation.doublon",
					new String[] {((DoublonFoundException) ex).getEntite(), ((DoublonFoundException) ex).getOperation()}));
		}else if(ex instanceof RequiredObjectIsNullException){
			message =
					new StringBuilder(ObjectTypesFormatters.getLabel("validation.requiredObject",
							new String[] {((RequiredObjectIsNullException) ex).getEntite(),
									((RequiredObjectIsNullException) ex).getRequiredObject(),
									((RequiredObjectIsNullException) ex).getOperation()}));
		}else if(ex instanceof ObjectUsedException){
			message = new StringBuilder(Labels.getLabel(((ObjectUsedException) ex).getKey()));
		}else if(ex instanceof ObjectReferencedException){
			message = new StringBuilder(Labels.getLabel(((ObjectReferencedException) ex).getKey()));
		}else if(ex instanceof WrongValueException){
			// try {
			throw (WrongValueException) ex;
			//} catch (Exception e) {
			//	log.error(e);
			//}
		}else if(ex instanceof PrinterException){
			message = new StringBuilder(Labels.getLabel("validation.erreur.impression"));
			if(ex instanceof StringEtiquetteOverSizeException){
				message.append("\n").append(Labels.getLabel("etiquette.overwidth.error"));
			}else{
				message.append("\n").append(Labels.getLabel("validation.erreur.imprimante.non.detectee"));
			}
			message.append(ex.getMessage());

		}else if(ex instanceof DataIntegrityViolationException){
			message = new StringBuilder(Labels.getLabel("error.data.invalid.usage"));
			if(((DataIntegrityViolationException) ex).getRootCause() != null
					&& ((DataIntegrityViolationException) ex).getRootCause().getMessage() != null){
				return message + "\n" + ((DataIntegrityViolationException) ex).getRootCause().getMessage();
			}
		}else if(ex instanceof CancelFromModalException){
			message = new StringBuilder(Labels.getLabel("general.action.cancelled"));
		}else if(ex instanceof DeriveBatchSaveException){
			return handleExceptionMessage(((DeriveBatchSaveException) ex).getTargetExeption());
		}else if(ex instanceof ExistingAnnotationValuesException){
			return ObjectTypesFormatters.getLabel("annotation.table.delete.existing.values",
					new String[] {((ExistingAnnotationValuesException) ex).getBanque().getNom(),
							((ExistingAnnotationValuesException) ex).getTable().getNom()});
		}else if(ex instanceof TransformationQuantiteOverDemandException){
			message = new StringBuilder(ObjectTypesFormatters.getLabel(ex.getMessage(),
					new String[] {((TransformationQuantiteOverDemandException) ex).getQteDemandee().toString(),
							((TransformationQuantiteOverDemandException) ex).getQteRestante().toString()}));
		}else{
			if(null == ex.getMessage()){
				message = new StringBuilder(ex.toString());
			}else if(null != Labels.getLabel(ex.getMessage())){
				message = new StringBuilder(Labels.getLabel(ex.getMessage()));
			}else{
				message = new StringBuilder(ex.getMessage());
			}
			log.error(ex.getMessage());
			log.debug(ex);
		}
//		// aucun message n'a pu être généré -> exception inattendue
//		if(message == null){
//			message = new StringBuilder(ex.getClass().getSimpleName() + " : " + ex.getMessage());
//			log.error(ex.getMessage());
//			log.debug(ex);
//		}

		// si l'exception possède des infos sur l'objet qui l'a
		// généré, on écrit ces informations dans le message
		if(ex instanceof TKException){
			if(message.toString().contains("{1}")){
				message = new StringBuilder(ObjectTypesFormatters.getLabel(ex.getMessage(),
						new String[] {((TKException) ex).getIdentificationObjetException()}));
			}else if(((TKException) ex).getEntiteObjetException() != null
					&& ((TKException) ex).getIdentificationObjetException() != null){
				final StringBuffer sb = new StringBuffer();
				sb.append(((TKException) ex).getEntiteObjetException());
				sb.append(" : ");
				sb.append(((TKException) ex).getIdentificationObjetException());
				sb.append(". ");

				message = new StringBuilder(sb.toString()
						+ (Labels.getLabel(ex.getMessage()) != null ? Labels.getLabel(ex.getMessage()) : message.toString()));
			}
		}

		return message.toString();
	}

	/**
	 * Rend un label anonyme.
	 * 
	 * @param label
	 *            Label à rendre anonyme.
	 * @param details
	 *            Si true, un lien détail apparaitra.
	 */
	public static void makeLabelAnonyme(final Label label, final boolean details){
		if(details){
			label.setSclass("formAnonymeLink");
			label.setValue(getAnonymeLink());
		}else{
			label.setSclass("formAnonymeBlock");
			label.setValue(getAnonymeString());
		}
		label.setPre(true);

	}

	public static String getAnonymeString(){
		return "                                 ";
	}

	public static String getCouleurString(){
		return "           ";
	}

	public static String getAnonymeLink(){
		final StringBuffer sb = new StringBuffer("          ");
		sb.append(Labels.getLabel("general.details"));
		sb.append("          ");
		return sb.toString();
	}

	/**
	 * Trouve la liste de table de codifications utilisables pour le transcodage
	 * a partir de la liste de banques spécifiée par l'interface en
	 * consultation.
	 * 
	 * @return liste de TableCodage.
	 */
	public List<TableCodage> getTablesForBanques(){
		final Set<TableCodage> tables = new HashSet<>();
		final List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
		List<BanqueTableCodage> btcs;
		for(int i = 0; i < banks.size(); i++){
			btcs = ManagerLocator.getBanqueManager().getBanqueTableCodageByBanqueManager(banks.get(i));
			for(int j = 0; j < btcs.size(); j++){
				tables.add(btcs.get(j).getTableCodage());
			}
		}
		return new ArrayList<>(tables);
	}

	/**
	 * Gère le download d'un fichier d'export.
	 */
	public static void downloadExportFile(final HSSFWorkbook wb, final String fileName){
		ByteArrayOutputStream out = null;

		try{
			out = new ByteArrayOutputStream();
			wb.write(out);

			final AMedia media = new AMedia(fileName, "xls", "application/vnd.ms-excel", out.toByteArray());
			Filedownload.save(media);
		}catch(final Exception e){
			log.error(e);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(final IOException e){
					out = null;
				}
			}
		}
	}

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien pour imprimer la
	 * page. Cette méthode va créer une nouvelle fenêtre.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param objToPrint
	 *            Objet à imprimer.
	 */
	public void openEtiquetteWindow(final Page page, final Modele modele, final Imprimante imp){
		if(!isBlockModal()){
			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("impressionWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("etiquette.modale.titre"));
			win.setBorder("normal");
			win.setWidth("500px");
			win.setHeight("450px");
			win.setClosable(true);

			final HtmlMacroComponent ua = populateEtiquetteModal(win, page, modele, imp);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					// progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	private static HtmlMacroComponent populateEtiquetteModal(final Window win, final Page page, Modele modele,
			final Imprimante imp){
		boolean isDefault = true;
		if(modele != null && modele.getIsDefault() != null){
			isDefault = modele.getIsDefault();
		}
		if(imp != null && imp.getImprimanteApi().getNom().equals("mbio")){
			modele = null;
			isDefault = true;
		}

		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		if(isDefault){
			ua = (HtmlMacroComponent) page.getComponentDefinition("ficheEtiquetteModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("openEtiquetteModale");
			ua.applyProperties();
			ua.afterCompose();
			((FicheEtiquetteModale) ua.getFellow("fwinEtiquetteModale").getAttributeOrFellow("fwinEtiquetteModale$composer", true))
			.init(modele, imp, null, null, null);
		}else{
			ua = (HtmlMacroComponent) page.getComponentDefinition("ficheEtiquetteDynModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("openEtiquetteDyModale");
			ua.applyProperties();
			ua.afterCompose();
			((FicheEtiquetteDynModale) ua.getFellow("fwinEtiquetteDynModale").getAttributeOrFellow("fwinEtiquetteDynModale$composer",
					true)).init(modele, imp, null, null, null);
		}

		return ua;
	}

	/**
	 * Recherche le statut de l'échantillon en fonction de la valeurs du champ
	 * QUANTITE.
	 * 
	 * @return Un ObjetStatut : STOCKE ou EPUISE.
	 */
	public ObjetStatut findStatutForTKStockableObject(final TKStockableObject tkobj){
		final ObjetStatut nonStocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0);
		//		ObjetStatut encours = ManagerLocator.getObjetStatutManager()
		//				.findByStatutLikeManager("ENCOURS", true).get(0);

		//		if (tkobj.getObjetStatut() != null
		//				&& tkobj.getObjetStatut().equals(nonStocke)) {
		//			return nonStocke;
		//		} else {

		final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);
		ObjetStatut epuise = null;

		final List<Cession> cessionsEnAttente =
				ManagerLocator.getCederObjetManager().getAllCessionsByStatutAndObjetManager("EN ATTENTE", tkobj);

		if(cessionsEnAttente.size() == 0){
			epuise = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("EPUISE", true).get(0);
			if(tkobj.getObjetStatut() != null && tkobj.getObjetStatut().getStatut().equals("DETRUIT")){
				epuise = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("DETRUIT", true).get(0);
			}
		}else{
			epuise = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("RESERVE", true).get(0);
		}

		// if (stocke != null && epuise != null) {
		final Float zero = (float) 0.0;
		// si la qté est non null
		if(tkobj.getQuantite() != null){

			// si sa valeur est égale a 0 => epuisé
			if(tkobj.getQuantite().equals(zero)){
				return epuise;
			}
			if(tkobj.getObjetStatut() != null && !tkobj.getObjetStatut().getStatut().equals("EPUISE")
					&& !tkobj.getObjetStatut().getStatut().equals("RESERVE") && !tkobj.getObjetStatut().getStatut().equals("DETRUIT")){
				return tkobj.getObjetStatut();
			}else if(tkobj.getEmplacement() != null){
				return stocke;
			}else{
				return nonStocke;
			}

		}else if(tkobj instanceof ProdDerive && ((ProdDerive) tkobj).getVolume() != null){
			// si sa valeur est égale a 0 => epuisé
			if(((ProdDerive) tkobj).getVolume().equals(zero)){
				return epuise;
			}
			if(tkobj.getObjetStatut() != null && !tkobj.getObjetStatut().getStatut().equals("EPUISE")
					&& !tkobj.getObjetStatut().getStatut().equals("RESERVE") && !tkobj.getObjetStatut().getStatut().equals("DETRUIT")){
				return tkobj.getObjetStatut();
			}else if(tkobj.getEmplacement() != null){
				return stocke;
			}else{
				return nonStocke;
			}
		}else{
			if(tkobj.getEmplacement() != null){
				return stocke;
			}
			return nonStocke;
		}
		//			else {
		//				return stocke;
		//			}
		//		} 
		//		} else {
		//			return null;
		//		}

		// return tkobj.getObjetStatut();
		// }
	}

	//	/**
	//	 * Recherche le statut du produit dérivé en fonction des valeurs des champs
	//	 * QUANTITE et VOLUME.
	//	 * 
	//	 * @param parent
	//	 *            ProdDerive parent pour lequel on cherche le statut.
	//	 * @return Un ObjetStatut : STOCKE ou EPUISE.
	//	 */
	//	public ObjetStatut findStatutForProdDerive(ProdDerive parent) {
	//		ObjetStatut nonStocke = ManagerLocator.getObjetStatutManager()
	//				.findByStatutLikeManager("NON STOCKE", true).get(0);
	//
	//		if (parent.getObjetStatut() != null
	//				&& parent.getObjetStatut().equals(nonStocke)) {
	//			return nonStocke;
	//		} else {
	//
	//			ObjetStatut stocke = ManagerLocator.getObjetStatutManager()
	//					.findByStatutLikeManager("STOCKE", true).get(0);
	//			ObjetStatut epuise = null;
	//
	//			List<Cession> cessionsEnAttente = ManagerLocator
	//					.getCederObjetManager()
	//					.getAllCessionsByStatutAndObjetManager("EN ATTENTE", parent);
	//
	//			if (cessionsEnAttente.size() == 0) {
	//				epuise = ManagerLocator.getObjetStatutManager()
	//						.findByStatutLikeManager("EPUISE", true).get(0);
	//			} else {
	//				epuise = ManagerLocator.getObjetStatutManager()
	//						.findByStatutLikeManager("RESERVE", true).get(0);
	//			}
	//
	//			if (stocke != null && epuise != null) {
	//				Float zero = (float) 0.0;
	//				// si la qté est non null
	//				if (parent.getQuantite() != null) {
	//
	//					// si sa valeur est égale a 0 => epuisé
	//					if (parent.getQuantite().equals(zero)) {
	//						return epuise;
	//					} else {
	//						if (parent.getObjetStatut() != null
	//								&& parent.getObjetStatut().getStatut()
	//										.equals("EPUISE")) {
	//							Iterator<Retour> rets = ManagerLocator.getRetourManager()
	//									.getRetoursForObjectManager(parent).iterator();
	//							Retour r;
	//							while (rets.hasNext()) {
	//								r = rets.next();
	//								if (r.getDateRetour() == null) {
	//									r.getObjetStatut();
	//								}
	//							}
	//							return nonStocke;
	//						} else {
	//							return stocke;
	//						}
	//					}
	//
	//				} else if (parent.getVolume() != null) {
	//					// si sa valeur est égale a 0 => epuisé
	//					if (parent.getVolume().equals(zero)) {
	//						return epuise;
	//					} else {
	//						if (parent.getObjetStatut() != null
	//								&& parent.getObjetStatut().getStatut()
	//										.equals("EPUISE")) {
	//							Iterator<Retour> rets = ManagerLocator.getRetourManager()
	//									.getRetoursForObjectManager(parent).iterator();
	//							while (rets.hasNext()) {
	//								if (rets.next().getDateRetour() == null) {
	//									return ManagerLocator.getObjetStatutManager()
	//										.findByStatutLikeManager("ENCOURS", true).get(0);
	//								}
	//							}
	//							return nonStocke;
	//						} else {
	//							return stocke;
	//						}
	//					}
	//				} else {
	//					return stocke;
	//				}
	//			} else {
	//				return null;
	//			}
	//		}
	//	}

	public static Float calculerVolumeRestant(final ProdDerive derive, final Float qte){
		Float vol = (float) 0.0;

		// maj du volume
		if(derive.getQuantiteInit() != null && !derive.getQuantiteInit().equals(new Float(0.0))){
			if(derive.getVolumeInit() != null){
				final Float rapport = qte / derive.getQuantiteInit();
				vol = derive.getVolumeInit() * rapport;
			}else{
				return null;
			}
		}else{
			vol = qte;
		}

		return vol;
	}

	public static Float calculerQuantiteRestante(final ProdDerive derive, final Float vol){
		Float quantite = (float) 0.0;

		if(derive.getVolumeInit() != null && !derive.getVolumeInit().equals(new Float(0.0))){
			if(derive.getQuantiteInit() != null){
				final Float rapport = vol / derive.getVolumeInit();
				quantite = derive.getQuantiteInit() * rapport;
			}else{
				return null;
			}
		}else{
			quantite = vol;
		}

		return quantite;
	}

	/**
	 * PopUp window appelée pour remplissage des headers.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param comp
	 *            parent ayant demandé la fenêtre.
	 */
	public void openHeadersWindow(final Page page, final Component comp){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("resultsWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("impression.headers.title"));
			win.setBorder("normal");
			win.setWidth("410px");
			final int height = 175;
			win.setHeight(String.valueOf(height) + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent) page.getComponentDefinition("ficheHeadersModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("ficheHeadersModaleComponent");
			ua.applyProperties();
			ua.afterCompose();

			((FicheHeadersModale) ua.getFellow("fwinHeadersModale").getAttributeOrFellow("fwinHeadersModale$composer", true))
			.setParent(comp);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					// progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	/**
	 * Forwarded Event. Sélectionne l'emplacement ppour l'afficher dans la liste
	 * stockage.
	 * 
	 * @param event
	 *            forwardé depuis le label emplacement cliquable (event.getData
	 *            contient l'emplacement).
	 */
	public void onClickObjectEmplacement(final Event event){

		if(event.getData() != null){

			final StockageController tabController = StockageController.backToMe(getMainWindow(), page);

			if(tabController != null && tabController.getListeStockages() != null){
				tabController.getListeStockages().openTerminaleFromEmplacement((Emplacement) event.getData());
			}
		}
	}

	/**
	 * Demande à l'utilisateur s'il souhaire créer des retours pour un
	 * évèvenement de stokage. Retourne la réponse de l'utlisateur.
	 * 
	 * @return
	 * @version 2.2.3-genno fix TK-291
	 */
	public boolean askForRetoursCreation(final Integer nbObjs, final Cession cession, final Transformation transformation,
			final List<OldEmplTrace> emplacements, final Incident incident, final OperationType opType){
		String texte = null;
		String countObjs = null;
		String objetDescr = null;
		boolean okForRetour = false;

		if(nbObjs > 1){
			countObjs = ObjectTypesFormatters.getLabel("message.retour.create.nobjets", new String[] {String.valueOf(nbObjs)});
		}else{ // objs.size() == 1
			countObjs = Labels.getLabel("message.retour.create.unobjet");
		}

		if(cession != null){
			objetDescr = Labels.getLabel("Entite.Cession") + " " + cession.getNumero();
			texte = ObjectTypesFormatters.getLabel("message.retour.create.list",
					new String[] {countObjs, opType.getNom().toLowerCase(), objetDescr});
			if(cession.getCessionStatut().getStatut().equals("EN ATTENTE")){
				texte = texte + "\n\n" + Labels.getLabel("message.retour.create.incomplete");
			}
		}else if(transformation != null){
			texte = Labels.getLabel("message.retour.create.transfo");
		}else if(incident != null){
			objetDescr = Labels.getLabel("Entite.Incident") + " " + incident.getNom();
			texte =
					ObjectTypesFormatters.getLabel("message.retour.create.list", new String[] {countObjs, opType.getNom(), objetDescr});
		}else if(emplacements != null){
			texte = ObjectTypesFormatters.getLabel("message.retour.create.deplacement", new String[] {countObjs});
		}

		// confirmation
		if(Messagebox.show(texte, Labels.getLabel("message.retour.create.title"), Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.YES){

			okForRetour = true;

		}

		return okForRetour;
	}

	/**
	 * Ouvre une boite de dialogue pour demander la creation de sorties
	 * temporaires associés à l'opération pour la liste d'objet qui a été
	 * manipulée.
	 * @param objs
	 * @param oldEmplacements table ancien emplacements
	 * @param date
	 * @param cession
	 * @param transformation
	 * @param emplacement
	 * @param conteneur
	 * @param incident
	 * @param opType
	 * @param observation
	 * @param collaborateur
	 * @version 2.2.3-genno fix TK-291
	 */
	public void proposeRetoursCreation(final List<TKStockableObject> objs,
			final List<OldEmplTrace> oldEmplacements, final Date date, final Cession cession,
			final Transformation transformation, final Incident incident, final OperationType opType, final String observation,
			final Collaborateur operateur){

		if(objs != null && !objs.isEmpty() && opType != null){
			if(askForRetoursCreation(objs.size(), cession, transformation, oldEmplacements, incident, opType)){
				openRetourFormModale(null, true, null, null, objs, oldEmplacements, cession, transformation, incident, date,
						observation, operateur);
			}
		}
	}

	/**
	 * Ouvre la modale contenant le formulaire permettant d'enregistrer ou de
	 * modifier un évènement de stockage.
	 * @param retour
	 * @param edit
	 * @param listRetour
	 * @param tkObject
	 * @param objs
	 * @param oldEmplacements table des anciens emplacements (déplacements)
	 * @param cession
	 * @param transformation
	 * @param incident
	 * @param emplacement
	 * @param dateSortie
	 * @param observation
	 * @param Collaborateur operateur
	 * @version 2.2.3-genno
	 */
	public void openRetourFormModale(final Retour retour, final boolean edit, final ListeRetour listRetour,
			final TKStockableObject tkObject, final List<TKStockableObject> objs,
			final List<OldEmplTrace> oldEmplacements, final Cession cession, final Transformation transformation,
			final Incident incident, final Date dateSortie, final String observation, final Collaborateur operateur){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("winRetourModal");
			win.setPage(page);
			win.setPosition("left,top");
			win.setHeight(objs != null ? "600px" : "380px");
			win.setWidth("700px");
			win.setMaximizable(true);
			win.setSizable(true);

			if(!edit){
				win.setTitle(Labels.getLabel("general.details"));
			}else{
				if(retour != null){
					win.setTitle(Labels.getLabel("general.create"));
				}else{
					win.setTitle(Labels.getLabel("general.edit"));
				}
			}
			win.setBorder("normal");
			win.setClosable(true);

			final HtmlMacroComponent ua =
					(HtmlMacroComponent) page.getComponentDefinition("retourModale", false).newInstance(page, null);

			ua.setParent(win);
			ua.setId("retourModalMacro");
			ua.applyProperties();
			ua.afterCompose();
			ua.setVisible(false);

			// passe la reference du controller de la liste à
			// la fiche
			if(listRetour != null){
				((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
				.setListeRetour(listRetour);
			}
			if(retour != null){ // modification
				((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true)).setObject(retour);
			}

			// tkObject
			((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true)).setTkObject(tkObject);

			if(!edit){
				((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true)).switchToStaticMode();
			}else{
				if(retour == null){
					if(objs != null){ // creation multiple
						((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true)).setObjects(objs);
						if(cession != null){
							((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
							.setCession(cession);
						}else if(transformation != null){
							((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
							.setTransformation(transformation);
						}else if(incident != null){
							((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
							.setIncident(incident);
						}
						//						else if (emplacement != null) {
						//							((FicheRetour) ua.getFellow("fwinRetour")
						//									.getAttributeOrFellow(
						//											"fwinRetour$composer", true))
						//									.setOldEmplacement(emplacement);
						//						}
					}
					((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
					.setSelectedCollaborateur(operateur);
					((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
					.setOldEmplacements(oldEmplacements);
					((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
					.switchToCreateMode(observation);

					((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true))
					.setInitDateSortie(dateSortie);

				}else{
					((FicheRetour) ua.getFellow("fwinRetour").getAttributeOrFellow("fwinRetour$composer", true)).switchToEditMode();
				}
			}

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					ua.setVisible(true);
				}
			});
			final Timer timer = new Timer();
			timer.setDelay(200);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	/**
	 * PopUp window appelée pour remplissage des headers.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param comp
	 *            parent ayant demandé la fenêtre.
	 */
	public void openRechercheBiocapWindow(final Page page, final Component parent, final AbstractObjectTabController controller){

		if(!isBlockModal()){

			setBlockModal(true);

			final HashMap<String, Object> map = new HashMap<>();
			map.put("parent", parent);
			map.put("controller", controller);

			if(!parent.hasFellow("fwinBiocapModale")){
				Executions.createComponents("/zuls/recherche/FicheRechercheBiocapModale.zul", parent, map);
			}

			setBlockModal(false);
		}

		//			setBlockModal(true);

		// nouvelle fenêtre
		//			final Window win = new Window();
		//			win.setVisible(false);
		//			win.setId("rechercheBiocapWindow");
		//			win.setPage(page);
		//			win.setMaximizable(true);
		//			win.setSizable(true);
		//			win.setTitle(Labels.getLabel("recherche.biocap.title"));
		//			win.setBorder("normal");
		//			win.setWidth("600px");
		//			int height = 400;
		//			win.setHeight(String.valueOf(height) + "px");
		//			win.setClosable(true);
		//
		//			final HtmlMacroComponent ua;
		//			ua = (HtmlMacroComponent) page.getComponentDefinition(
		//					"rechercheBiocapModale", false).newInstance(page, null);
		//			ua.setParent(win);
		//			ua.setId("ficheRechercheBiocapModaleComponent");
		//			ua.applyProperties();
		//			ua.afterCompose();
		//
		//			((FicheRechercheBiocapModale) ua.getFellow("fwinBiocapModale")
		//					.getAttributeOrFellow("fwinBiocapModale$composer", true))
		//					.initModale(comp);
		//			ua.setVisible(false);
		//
		//			win.addEventListener("onTimed", new EventListener<Event>() {
		//				public void onEvent(Event event) throws Exception {
		//					// progress.detach();
		//					ua.setVisible(true);
		//				}
		//			});
		//
		//			Timer timer = new Timer();
		//			timer.setDelay(500);
		//			timer.setRepeats(false);
		//			timer.addForward("onTimer", timer.getParent(), "onTimed");
		//			win.appendChild(timer);
		//			timer.start();
		//
		//			try {
		//				win.onModal();
		//				setBlockModal(false);
		//
		//			} catch (SuspendNotAllowedException e) {
		//				log.error(e);
		//			}
		//		}
	}

	/**
	 * PopUp window appelée pour modifier le pwd d'un utilisateur.
	 * 
	 * @param page
	 *            dans laquelle inclure la modale
	 * @param comp
	 *            parent ayant demandé la fenêtre.
	 * @param user
	 *            Utilisateur à modifier.
	 */
	public void openPasswordWindow(final Page page, final Component comp, final Utilisateur user){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("passwordWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("utilisateur.update.password"));
			win.setBorder("normal");
			win.setWidth("450px");
			final int height = 210;
			win.setHeight(String.valueOf(height) + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent) page.getComponentDefinition("fichePasswordModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("fichePasswordModaleComponent");
			ua.applyProperties();
			ua.afterCompose();

			((FichePasswordModale) ua.getFellow("fwinPasswordModale").getAttributeOrFellow("fwinPasswordModale$composer", true))
			.initModale(comp, user);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					// progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	/**
	 * Retourne le controller de la fiche d'un échantillon.
	 * 
	 * @param event
	 * @return
	 */
	public EchantillonController getEchantillonController(){
		if(getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){
			return ((EchantillonController) getMainWindow().getMainTabbox().getTabpanels().getFellow("echantillonPanel")
					.getFellow("winEchantillon").getAttributeOrFellow("winEchantillon$composer", true));
		}
		return null;
	}

	/**
	 * Retourne le controller de la fiche d'un dérivé.
	 * 
	 * @param event
	 * @return
	 */
	public ProdDeriveController getProdDeriveController(){
		if(getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")){
			return ((ProdDeriveController) getMainWindow().getMainTabbox().getTabpanels().getFellow("derivePanel")
					.getFellow("winProdDerive").getAttributeOrFellow("winProdDerive$composer", true));
		}
		return null;
	}

	/**
	 * Modale window appelée pour choisir l'imprimante et le modèle d'impression.
	 * @param pf Plateforme d'appartenance des imprimantes et des modèles.
	 * @param list TKStockableObject à imprimer
	 * @param affectation éventuelle affectation pour pré-remplir la liste
	 * @since 2.0.13
	 * @param lignes étiquettes en cours d'édition pour modèle paramétrable
	 */
	public void openImprimanteModeleModale(final Plateforme pf, final List<? extends TKStockableObject> objs,
			final AffectationImprimante affectation, final List<LigneEtiquette> lignes){
		if(!isBlockModal()){

			setBlockModal(true);

			final HashMap<String, Object> map = new HashMap<>();
			map.put("pf", pf);
			map.put("objs", objs);
			map.put("affectation", affectation);
			map.put("lignes", lignes);

			Executions.createComponents("/zuls/imprimante/ImprimanteModeleModale.zul", null, map);

			setBlockModal(false);
		}
	}

	/**
	 * PopUp window appelée pour modifier ajouter un incident au conteneur.
	 * @param page dans laquelle inclure la modale
	 * @param comp parent ayant demandé la fenêtre.
	 * @param conteneur Conteneur.
	 * @param enceinte
	 * @param terminale 
	 * @version 2.0.10
	 */
	public void openAddIncidentWindow(final Page page, final Component comp, final Conteneur conteneur, final Enceinte enceinte,
			final Terminale terminale){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("addIncidentWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("conteneur.add.incident"));
			win.setBorder("normal");
			win.setHeight("260px");
			win.setWidth("650px");
			win.setClosable(true);

			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent) page.getComponentDefinition("ficheAddIncidentModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("ficheAddIncidentModaleComponent");
			ua.applyProperties();
			ua.afterCompose();

			((FicheAddIncident) ua.getFellow("fwinAddIncident").getAttributeOrFellow("fwinAddIncident$composer", true))
			.init(conteneur, comp, enceinte, terminale);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					// progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	/**
	 * Clic sur le bouton générant un code.
	 */
	public void onClick$numerotation(){}

	/**
	 * PopUp window appelée pour afficher la progress bar.
	 * 
	 */

	public HtmlMacroComponent callProgressBar(){

		winProgressBar = new Window();
		winProgressBar.setId("progressBarWindow");
		winProgressBar.setPage(page);
		winProgressBar.setBorder("normal");
		winProgressBar.setWidth("350px");
		winProgressBar.setHeight("55px");
		winProgressBar.setPosition("center");
		winProgressBar.doHighlighted();
		// win.setStyle(".z-window-popup .z-window-popup-hm {background: transparent}");

		progressBar = (HtmlMacroComponent) page.getComponentDefinition("progressBar", false).newInstance(page, null);
		progressBar.setParent(winProgressBar);
		progressBar.setId("progressBarComponent");
		progressBar.applyProperties();
		progressBar.afterCompose();

		return progressBar;

	}

	public ProgressBarComponent getBar(){
		return ((ProgressBarComponent) progressBar.getFellow("progressPanel").getAttributeOrFellow("progressPanel$composer", true));
	}

	public void closeProgressBarComponent(){
		winProgressBar.getParent().detach();
	}

	/**
	 * L'export est anonyme+stock par défaut, 
	 * Si profil anonyme, conserve ce profi
	 * Verifie ensuite si adminstrateur -> NOMINATIF, sinon
	 * utilise le profilExport attribué au profil.
	 *  
	 * @version 2.2.3-rc1
	 * @since 2.2.1 export anonyme par défaut
	 * @return ProfilExport
	 */
	public ProfilExport getProfilExport(){		
		// admin PF -> export nominatif possible
		if (sessionScope.containsKey("AdminPF") && (Boolean) sessionScope.get("AdminPF")) {
			return ProfilExport.NOMINATIF;
		}

		if(sessionScope.containsKey("Export")){
			return (ProfilExport) sessionScope.get("Export");
		}
		
		return ProfilExport.ANONYMESTOCK;
	}

	/**
	 * Modale warning informant qu'au moins un objet a un statut de stockage 
	 * incohérent avec la création d'évènements de stockage.
	 * @param objs 
	 * @param operation (TRANSFORMATION|CESSION|DEPLACEMENT|INCIDENT).
	 */
	public void openObjetStatutWarnModale(final List<TKStockableObject> objs, final String operation){
		if(!isBlockModal()){

			setBlockModal(true);

			final HashMap<String, Object> map = new HashMap<>();
			map.put("objs", objs);
			map.put("operation", operation);
			map.put("banque", SessionUtils.getSelectedBanques(sessionScope).size() == 1
					? SessionUtils.getSelectedBanques(sessionScope).get(0) : null);
			map.put("main", getMainWindow());

			Executions.createComponents("/zuls/cession/retour/ObjetStatutWarnModale.zul", null, map);

			setBlockModal(false);
		}
	}

	/**
	 * Filtre les objets dont le statut de stockage est incompatible avec la création 
	 * d'autres évènements de stockage (ENCOURS, EPUISE, RESERVE)
	 * @param objs liste de TKStockableObjets à filtrer
	 * @param operation (TRANSFORMATION|CESSION|DEPLACEMENT|INCIDENT).
	 * @return true si des objets incompatibles bloquent la suite des actions 
	 * associées à la création d'évènements de stockage (transformations, cessions, 
	 * déplacement, incidents).
	 */
	public boolean getObjStatutIncompatibleForRetour(final List<? extends Object> objs, final String operation){
		final List<TKStockableObject> encours = new ArrayList<>();
		final Iterator<? extends Object> itS = objs.iterator();
		final List<Integer> objIds = new ArrayList<>();
		final List<Retour> rets = new ArrayList<>();
		TKStockableObject obj;
		while(itS.hasNext()){
			obj = (TKStockableObject) itS.next();
			if(obj.getObjetStatut().getStatut().equals("EPUISE") || (obj.getObjetStatut().getStatut().equals("RESERVE")
					&& operation != null && (operation.equals("CESSION") || operation.equals("TRANSFORMATION")))){
				encours.add(obj);
				// statut ENCOURS sauf si demande création dérivés
			}else if(obj.getObjetStatut().getStatut().equals("ENCOURS")){
				if(operation != null && !operation.equals("TRANSFORMATION")){
					encours.add(obj);
				}else{ // recupere les ids si ENCOURS pour vérifier si cession partielle
					rets.clear();
					objIds.clear();
					objIds.add(obj.listableObjectId());
					if(obj.entiteNom().equals("Echantillon")){
						rets.addAll(ManagerLocator.getRetourManager().findByObjectDateRetourEmptyManager(objIds,
								ManagerLocator.getEntiteManager().findByIdManager(3)));
					}else{
						rets.addAll(ManagerLocator.getRetourManager().findByObjectDateRetourEmptyManager(objIds,
								ManagerLocator.getEntiteManager().findByIdManager(8)));
					}
				}
				// un seul retour attendu au max
				retLoop: for(final Retour retour : rets){
					if(retour.getCession() != null){
						encours.add(obj);
						break retLoop;
					}
				}
			}
		}

		if(!encours.isEmpty()){
			openObjetStatutWarnModale(encours, operation);
		}
		return !encours.isEmpty();
	}

	/**
	 * Ouvre la modale permettant la rcomplétion d'évènements de stockage 
	 * incomplets
	 * @param AbstractListController controller qui ouvre la modale et attend en 
	 * retour la liste des TKStockableObject à mettre à jour.
	 * @param liste des TKStockableObject à mettre à jour.
	 * @since 2.0.10
	 */
	public void openDateRetourModale(final AbstractListeController2 liste, final List<TKStockableObject> objs){

		if(!isBlockModal()){

			setBlockModal(true);

			final HashMap<String, Object> map = new HashMap<>();
			map.put("controller", liste.getObjectTabController());
			map.put("objs", objs);

			Executions.createComponents("/zuls/cession/retour/DateRetourModale.zul", null, map);

			setBlockModal(false);
		}

	}

	//	/**
	//	 * PopUp window appelée pour confirmation de la recherche.
	//	 * 
	//	 * @param page
	//	 *            dans laquelle inclure la modale
	//	 * @param message
	//	 *            affiché à l'utilisateur.
	//	 * @param boolean cascadable si possibilité de cascader la délétion ou
	//	 *        l'archivage
	//	 * @param deletable
	//	 *            si la deletion est possible.
	//	 * @param controller
	//	 *            parent ayant demandé la délétion.
	//	 */
	//	public void openResultatsWindow(Page page, Integer results, Component comp) {
	//		if (!isBlockModal()) {
	//
	//			setBlockModal(true);
	//
	//			// nouvelle fenêtre
	//			final Window win = new Window();
	//			win.setVisible(false);
	//			win.setId("resultsWindow");
	//			win.setPage(page);
	//			win.setMaximizable(true);
	//			win.setSizable(true);
	//			win.setTitle(Labels.getLabel("message.research.title"));
	//			win.setBorder("normal");
	//			win.setWidth("400px");
	//			int height = 190;
	//			win.setHeight(String.valueOf(height) + "px");
	//			win.setClosable(true);
	//
	//			final HtmlMacroComponent ua;
	//			ua = (HtmlMacroComponent) page.getComponentDefinition(
	//					"resultatsModale", false).newInstance(page, null);
	//			ua.setParent(win);
	//			ua.setId("resultatsModaleComponent");
	//			ua.applyProperties();
	//			ua.afterCompose();
	//
	//			((ResultatsModale) ua.getFellow("fwinResultatsModale")
	//					.getAttributeOrFellow("fwinResultatsModale$composer", true))
	//					.init(results, comp, getEntiteNom(), 
	//							getObjectTabController());
	//			ua.setVisible(false);
	//
	//			win.addEventListener("onTimed", new EventListener<Event>() {
	//				public void onEvent(Event event) throws Exception {
	//					// progress.detach();
	//					ua.setVisible(true);
	//				}
	//			});
	//
	//			Timer timer = new Timer();
	//			timer.setDelay(500);
	//			timer.setRepeats(false);
	//			timer.addForward("onTimer", timer.getParent(), "onTimed");
	//			win.appendChild(timer);
	//			timer.start();
	//
	//			try {
	//				win.onModal();
	//				setBlockModal(false);
	//
	//			} catch (SuspendNotAllowedException e) {
	//				log.error(e);
	//			}
	//		}
	//	}

	/**
	 * PopUp window appelée pour confirmation de la recherche.
	 */
	public void openResultatsWindow(final Page page, final List<Integer> results, final Component comp, final String entite,
			final AbstractObjectTabController controller){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("resultsWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("message.research.title"));
			win.setBorder("normal");
			win.setWidth("400px");
			win.addForward("onClose", comp, "onDoCancel");
			final int height = 200;
			win.setHeight(String.valueOf(height) + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent) page.getComponentDefinition("resultatsModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("resultatsModaleComponent");
			ua.applyProperties();
			ua.afterCompose();

			((ResultatsModale) ua.getFellow("fwinResultatsModale").getAttributeOrFellow("fwinResultatsModale$composer", true))
			.init(results, comp, entite, controller);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					// progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	/**
	 * Ouvre la modale permettant la restriction des tables d'annotations 
	 * concernées par l'export.
	 * @param AbstractListController controller qui ouvre la modale et attend en 
	 * retour la liste des ids de tables annotations à exporter
	 * @param parent si la modale est ouverte la ResultatsModale
	 * @param entite concernées par l'export
	 * @since 2.0.10
	 */
	public void openRestrictTablesModale(final AbstractListeController2 liste, final Component parent, final Entite e){

		if(!isBlockModal()){

			setBlockModal(true);

			final HashMap<String, Object> map = new HashMap<>();
			map.put("controller", liste);
			map.put("parent", parent);
			map.put("entite", e);

			Executions.createComponents("/zuls/export/RestrictTablesModale.zul", null, map);

			setBlockModal(false);
		}

	}

	/**
	 * Méthode générique de vérification des authorisations lors de l'utilisation 
	 * de liens vers des ressources sur d'autres onglets/collections/pfs
	 * Renvoie true si l'utilisateur loggé a le droit d'accéder à la ressource 
	 * demandée. Sinon, affiche un messageBox warning et return false. 
	 * @param Obj dont les informations sont demandées
	 * @param boolean display warning si la methode doit afficher un message 
	 * d'avertissement
	 * @return 
	 */
	public Boolean getDroitListableObjectConsultation(final TKAnnotableObject obj, final boolean displayWarning){
		final boolean acces = getDroitEntiteObjectConsultation(obj.getBanque(), obj.entiteNom());
		if(acces){
			return true;
		}else if(displayWarning){
			Messagebox.show(Labels.getLabel("error.resource.notallowed"), Labels.getLabel("error.resource.notallowed.title"),
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

		return false;
	}

	public Hashtable<String, Boolean> getDroitsConsultation(){
		return getMainWindow().getDroitsConsultation();
	}

	/**
	 * Traite l'authorisation a une resource (= onglet) d'une entite précise pour une 
	 * banque donnée
	 * @param banque
	 * @param entiteNom
	 * @return boolean
	 */
	public Boolean getDroitEntiteObjectConsultation(final Banque banque, final String entiteNom){

		// si on reste dans banque courante
		// utilise getDroitsConsultation
		if(SessionUtils.getSelectedBanques(sessionScope).contains(banque) && getDroitsConsultation().get(entiteNom) != null
				&& getDroitsConsultation().get(entiteNom)){
			return true;
		}

		boolean acces = false;

		final Utilisateur user = SessionUtils.getLoggedUser(sessionScope);

		Set<Plateforme> pfs = new HashSet<>();
		if(user.isSuperAdmin()){
			return true;
		}

		pfs = ManagerLocator.getUtilisateurManager().getPlateformesManager(user);
		if(pfs.contains(banque.getPlateforme())){
			acces = true;
		}else{
			// on récupère le profil du user pour la banque
			// sélectionnée
			final List<ProfilUtilisateur> profils =
					ManagerLocator.getProfilUtilisateurManager().findByUtilisateurBanqueManager(user, banque);

			if(profils.size() > 0){
				final Profil profil = profils.get(0).getProfil();
				// si l'utilisateur est admin pour la banque
				if(profil.getAdmin()){
					acces = true;
				}else{
					final List<OperationType> operations =
							ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, entiteNom);
					final OperationType opeation =
							ManagerLocator.getOperationTypeManager().findByNomLikeManager("Consultation", true).get(0);
					if(operations.contains(opeation)){
						acces = true;
					}
				}
			}
		}

		return acces;
	}

	/**
	 * Affiche un objet à partir d'un lien affiché sur un onglet différent 
	 * de l'onglet correspondant à l'entite de l'objet.
	 * Applique les vérifications sur les authorisations de l'utilisateur d'accéder 
	 * à cet autre onglet et applique éventuellement le changement de banque. 
	 * @param event
	 */
	public void displayObjectData(final TKAnnotableObject tkObj){

		if(tkObj != null){ // un seul element a afficher

			if(getDroitListableObjectConsultation(tkObj, true)){
				// change la banque au besoin
				if(!SessionUtils.getSelectedBanques(sessionScope).contains(tkObj.getBanque())){
					getMainWindow().updateSelectedBanque(tkObj.getBanque());
				}
				final AbstractObjectTabController controller = backToControllerFromEntiteNom(tkObj.entiteNom());
				controller.switchToFicheStaticMode(tkObj);

			}
		}
	}

	/**
	 * Affiche une liste d'objets à partir d'un lien affiché sur un onglet différent 
	 * de l'onglet correspondant à l'entite des objets de la liste.
	 * Applique les vérifications sur les authorisations de l'utilisateur d'accéder 
	 * à cet autre onglet et applique éventuellement le changement vers l'affichage 
	 * toutes collections si possible. 
	 * @param event
	 */
	public void displayObjectsListData(final List<TKAnnotableObject> tkObjList){

		if(tkObjList != null && !tkObjList.isEmpty()){
			final String entiteNom = tkObjList.get(0).entiteNom();
			final List<Banque> availBanques = new ArrayList<>();
			for(final Banque banque : getMainWindow().getBanques()){
				if(banque.getBanqueId() != null && getDroitEntiteObjectConsultation(banque, entiteNom)){
					availBanques.add(banque);
				}
			}

			boolean warn = false;
			final List<TKAnnotableObject> objs = new ArrayList<>();
			final Set<Banque> objBanks = new HashSet<>();

			for(int i = 0; i < tkObjList.size(); i++){
				// n'ajoute à la liste que les objets accessibles
				if(availBanques.contains(tkObjList.get(i).getBanque())){
					objBanks.add(tkObjList.get(i).getBanque());
					objs.add(tkObjList.get(i));
				}else{
					warn = true;
				}
			}

			try{
				if(!objs.isEmpty()){
					final Iterator<Banque> itBk = objBanks.iterator();
					Banque bq;
					// passe en toutes collections si besoin
					if(objBanks.size() == 1){
						bq = itBk.next();
						// si une seule banque mais pas banque courante
						if(!SessionUtils.getSelectedBanques(sessionScope).contains(bq)){
							getMainWindow().updateSelectedBanque(bq);
						}
					}else{ // toutes collections pour cet utilisateur
						getMainWindow().updateSelectedBanqueToutesColl();
					}

					final AbstractObjectTabController tabController = backToControllerFromEntiteNom(entiteNom);

					tabController.getListe().updateMultiObjectsGridListFromOtherPage(new ArrayList<TKdataObject>(objs));
					//setListObjects(echans);
					tabController.getListe().setCurrentObject(null);
					tabController.clearStaticFiche();
					tabController.switchToListeMode();
				}

				// affiche un warning si des objets ont été retirés
				if(warn){
					Messagebox.show(Labels.getLabel("warn.resource.notallowed"), Labels.getLabel("error.resource.notallowed.title"),
							Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}catch(final Exception e){
				Messagebox.show(Labels.getLabel("error.toutesColl.notallowed"), Labels.getLabel("error.resource.notallowed.title"),
						Messagebox.OK, Messagebox.EXCLAMATION);

			}
		}
	}

	/**
	 * Selectionne le tab et renvoie le controller à partir du nom de l'entite 
	 * recherchée.
	 * @param nom
	 * @return AbstractObjectTabController
	 * @version 2.0.10
	 */
	public AbstractObjectTabController backToControllerFromEntiteNom(final String nom){
		AbstractObjectTabController controller = null;
		switch(nom){
		case "Patient":
			controller = PatientController.backToMe(getMainWindow(), page);
			break;
		case "Prelevement":
			controller = PrelevementController.backToMe(getMainWindow(), page);
			break;
		case "Echantillon":
			controller = EchantillonController.backToMe(getMainWindow(), page);
			break;
		case "ProdDerive":
			controller = ProdDeriveController.backToMe(getMainWindow(), page);
			break;
		case "Cession":
			controller = CessionController.backToMe(getMainWindow(), page);
			break;
		default:
			break;
		}

		return controller;
	}

	public Boolean hasAllCreationRights(final Profil profil){

		final OperationType creation = ManagerLocator.getOperationTypeManager().findByNomLikeManager("creation", true).get(0);
		final OperationType annotation = ManagerLocator.getOperationTypeManager().findByNomLikeManager("annotation", true).get(0);

		final List<Entite> entites = new ArrayList<>();
		entites.add(ManagerLocator.getEntiteManager().findByIdManager(1));
		entites.add(ManagerLocator.getEntiteManager().findByIdManager(2));
		entites.add(ManagerLocator.getEntiteManager().findByIdManager(3));
		entites.add(ManagerLocator.getEntiteManager().findByIdManager(8));

		if(profil != null){
			return ManagerLocator.getDroitObjetManager().hasProfilOperationOnEntitesManager(profil, creation, entites)
					&& ManagerLocator.getDroitObjetManager().hasProfilOperationOnEntitesManager(profil, annotation, entites);
		}

		return false;

	}

	/**
	 * Modale window appelée pour obliger l'utilisateur à renseigner une date
	 * de fin pour les évènements de stockage des échantillons et/ou dérivés qui 
	 * ont cédés partiellement et pour lesquels ces évènements de stockage ont 
	 * été produits à la création de la cession (statut EN ATTENTE).
	 * @param Map clef = Code Echantillon, valeur = date de sortie formattée evt incomplet
	 * @param Map clef = Code Dérivé, valeur = date de sortie formattéeevt incomplet
	 * @param Calendar date de pré-remplissage des CalendarBox 
	 * (ie date de départ de la cession)
	 * @param FicheCessionEdit controller auquel renvoyer les Calendar saisis
	 * @param parent Component qui commande l'ouverture de la modale
	 */
	public void openDateRetourCompleteModale(final Map<String, String> echansRetourIncompletes,
			final Map<String, String> derivesRetourIncompletes, final Calendar dateInit, final FicheCessionEdit controller,
			final Component parent){
		if(!isBlockModal()){

			setBlockModal(true);

			//			HashMap <String, Object> map = new HashMap<String, Object>();
			//		        map.put("cal1", cal1);
			//		        map.put("cal2", cal2);
			//		        map.put("controller", controller);

			//		    Executions.createComponents("/zuls/cession/DateRetourComplete.zul", parent, map);

			final HtmlMacroComponent ua =
					(HtmlMacroComponent) page.getComponentDefinition("dateRetourCompleteModale", false).newInstance(page, null);

			ua.setParent(parent);
			ua.setId("dateRetourCompleteModale");
			ua.applyProperties();
			ua.afterCompose();

			((DateRetourCompleteModale) ua.getFellow("fwinDateRetourComplete")
					.getAttributeOrFellow("fwinDateRetourComplete$composer", true)).init(echansRetourIncompletes,
							derivesRetourIncompletes, dateInit, controller);

			((Window) ua.getFellow("fwinDateRetourComplete")).doModal();

			setBlockModal(false);
		}
	}

	/**
	 * PopUp window appelée pour confirmation de la suppression d'un objet. 
	 * Cette popUp peut contenir une demande de confirmation simple avec 
	 * éventuellement un textbox permettant de rentrer le commentaire 
	 * de suppression pour les objets fantomables, un avertissement 
	 * d'impossibilité de suppression ou la possibilité de cascader la 
	 * suppression aux objets enfants.
	 * @param page dans laquelle inclure la modale
	 * @param message affiché à l'utilisateur.
	 * @param boolean cascadable si possibilité de cascader la délétion
	 * ou l'archivage
	 * @param deletable si la deletion est possible.
	 * @param controller parent ayant demandé la délétion.
	 * @param archive si la deletion devient archivage.
	 */
	public void openDeleteWindow(final Page page, final String message, final boolean casc, final boolean fantomable,
			final boolean del, final Component comp, final boolean archive){
		if(!isBlockModal()){

			setBlockModal(true);

			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("deleteWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("message.deletion.title"));
			win.setBorder("normal");
			win.setWidth("500px");
			int height = 200;
			if(fantomable){
				height = height + 100;
			}
			win.setHeight(String.valueOf(height) + "px");
			win.setClosable(true);

			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent) page.getComponentDefinition("deleteModale", false).newInstance(page, null);
			ua.setParent(win);
			ua.setId("deleteModaleComponent");
			ua.applyProperties();
			ua.afterCompose();

			((DeleteModale) ua.getFellow("fwinDeleteModale").getAttributeOrFellow("fwinDeleteModale$composer", true)).init(message,
					casc, fantomable, del, comp, archive);
			ua.setVisible(false);

			win.addEventListener("onTimed", new EventListener<Event>()
			{
				@Override
				public void onEvent(final Event event) throws Exception{
					//progress.detach();
					ua.setVisible(true);
				}
			});

			final Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();

			try{
				win.onModal();
				setBlockModal(false);

			}catch(final SuspendNotAllowedException e){
				log.error(e);
			}
		}
	}

	public Component getSelfComponent(){
		return self;
	}

	/**
	 * Modale window appelée pour ouvrir la popup affichant les 
	 * prélèvements consultables par l'utilisateur 
	 * et les consent type utilisés pour le patient auquel on ajoute un 
	 * nouveau prélèvement.
	 * @param ConsentTypeUsedTable table des liste de prélèvements par consentType
	 * @since 2.0.13
	 */
	public void openConsentTypeUsed(final ConsentTypeUsedTable table){
		if(!isBlockModal()){

			setBlockModal(true);

			final HashMap<String, Object> map = new HashMap<>();
			map.put("table", table);

			Executions.createComponents("/zuls/prelevement/ConsentTypeUsedPopup.zul", null, map);

			setBlockModal(false);
		}
	}
	
	/**
	 * Modale window appelée pour avertir que l'export doit être 
	 * anonymisé.
	 * Permet de commander un export nominatif, mais le bouton est 
	 * moins visible. Sinon l'export demandé est anonyme + stock.
	 * @return Le ProfilExport si anonymisation demandée
	 * @since 2.2.3-rc1
	 */
	public ProfilExport openExportAnonymeModale(){
		
		ProfilExport pExport = ProfilExport.ANONYMESTOCK;
		
		Window win = (Window) Executions
				.createComponents("/zuls/export/ExportAnonymeModale.zul", null, null);
		
		win.doModal();
		
		// EXPORT NOMINATIF uniquement si export modale renvoie anonymize = false
		if (win != null && win.hasAttribute("anonymize") 
				&& !((Boolean) win.getAttribute("anonymize")).booleanValue()) {
			pExport = ProfilExport.NOMINATIF; 
		}
		
		
		return pExport;
	}
	
	/************************* INTERFACAGES ***********************************/
	/**************************************************************************/
	/**
	 * Méthode d'envoi d'une liste d'objets stockables vers une modale permettant 
	 * à l'utilisateur de positionner les objets sur un portoir de transport 
	 * transitoire avec un système de stockage automatisé.
	 * @param tkObjs liste d'objets stockables
	 * @since 2.2.1-IRELEC
	 * @version 2.2.1-IRELEC
	 */
	public void postStorageData(List<? extends TKStockableObject> tkObjs, boolean destock) {
				
		if (!SessionUtils.getRecepteursInterfacages(sessionScope).isEmpty() && tkObjs != null) {
			
			Collections.sort(tkObjs, new TKStockableObject.CodeComparator(true));
			
			// only in stock obj
			List<TKStockableObject> inStock = new ArrayList<TKStockableObject>();
			for (TKStockableObject tkO : tkObjs) {
				if (tkO.getEmplacement() != null) {
					inStock.add(tkO);
				}
			}
			
			if (!inStock.isEmpty()) {
				Map<String, Object> args = new HashMap<String, Object>();
				args.put("objs", tkObjs);
				args.put("destockageMode", destock);
				Window window = (Window) Executions.createComponents("/zuls/stockage/BoiteTransfertModale.zul", 
						getMainWindow().getSelfComponent(), args);
				window.doModal();
			}
		}
	}
	
	/**
	 * Le bouton Storage robot item est visible:
	 *  - si le recepteur IRELEC est  configuré
	 *  - si l'utilisateur est au moins Admin PF
	 * @return true si le bouton doit être affiché
	 */
	public boolean storageRobotItemVisible() {
		if (sessionScope.containsKey("AdminPF")) {
			for (fr.aphp.tumorotek.model.interfacage.Recepteur recept : 
							SessionUtils.getRecepteursInterfacages(sessionScope)) {
				if (recept.getLogiciel().getNom().equals("IRELEC")) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Renvoie true si la plateforme passée en paramètre (origine d'un conteneur/user..) 
	 * correspond à la PF courante ou si l'utilisateur est admin de cette plateforme 
	 * ou super administrateur.
	 * @param pOrig Plateforme à tester
	 * @return boolean
	 */
	public boolean isCurrentPFOrUserAtLeastAdminfPF(Plateforme pOrig) {
		boolean enable = pOrig.equals(SessionUtils.getCurrentPlateforme())
			|| SessionUtils.getLoggedUser(sessionScope).isSuperAdmin()
			|| ManagerLocator.getUtilisateurManager()
					.getPlateformesManager(SessionUtils.getLoggedUser(sessionScope)).contains(pOrig);
		return enable;
	}
}