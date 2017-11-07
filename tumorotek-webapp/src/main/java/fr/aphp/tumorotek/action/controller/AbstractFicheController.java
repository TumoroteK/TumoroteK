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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.historique.FicheHistoriqueModale;
import fr.aphp.tumorotek.action.imports.ResultatsImportModale;
import fr.aphp.tumorotek.action.impression.FicheBonLivraisonModale;
import fr.aphp.tumorotek.action.impression.FicheTemplateModale;
import fr.aphp.tumorotek.action.interfacage.SelectDossierExterneModale;
import fr.aphp.tumorotek.action.modification
				.multiple.ModificationMultipleCheckbox;
import fr.aphp.tumorotek.action.modification
				.multiple.ModificationMultipleCodeAssigne;
import fr.aphp.tumorotek.action.modification
				.multiple.ModificationMultipleCombobox;
import fr.aphp.tumorotek.action.modification.multiple.ModificationMultipleNonConformites;
import fr.aphp.tumorotek.action.modification
				.multiple.ModificationMultipleDatebox;
import fr.aphp.tumorotek.action.modification
				.multiple.ModificationMultipleDoublebox;
import fr.aphp.tumorotek.action.modification.multiple.ModificationMultipleFile;
import fr.aphp.tumorotek.action.modification
					.multiple.ModificationMultipleListbox;
import fr.aphp.tumorotek.action.modification
					.multiple.ModificationMultipleModal;
import fr.aphp.tumorotek.action.modification
					.multiple.ModificationMultipleMultiListbox;
import fr.aphp.tumorotek.action.modification
				.multiple.ModificationMultipleQuantification;
import fr.aphp.tumorotek.action.modification
					.multiple.ModificationMultipleTextbox;
import fr.aphp.tumorotek.action.stockage.ChangeNumerotationModale;
import fr.aphp.tumorotek.action.stockage.ChangeTailleEnceinteModale;
import fr.aphp.tumorotek.action.thesaurus.FicheChampThesaurus;
import fr.aphp.tumorotek.decorator.CederObjetDecorator;
import fr.aphp.tumorotek.manager.io.imports.ImportError;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Niveau d'abstraction intermédiaire définissant les méthodes abstraites 
 * partagées par les deux types de fiches (statique et dynamique).
 * Date: 26/07/2010
 *
 * @author mathieu BARTHELEMY
 * @version 2.0
 *
 */
public abstract class AbstractFicheController extends AbstractController {
	
	private static final long serialVersionUID = 1L;
	
	private boolean cascadable;
	private boolean fantomable;
	private boolean deletable;
	private String deleteMessage;
	
	private AbstractObjectTabController objectTabController;
	
	public AbstractObjectTabController getObjectTabController() {
		return objectTabController;
	}
	
	public void setObjectTabController(AbstractObjectTabController 
															controller) {
		objectTabController = controller;
	}
	
	public boolean isCascadable() {
		return cascadable;
	}

	public boolean isFantomable() {
		return fantomable;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setCascadable(boolean c) {
		this.cascadable = c;
	}

	public void setFantomable(boolean f) {
		this.fantomable = f;
	}

	public void setDeletable(boolean d) {
		this.deletable = d;
	}

	public String getDeleteMessage() {
		return deleteMessage;
	}

	public void setDeleteMessage(String dM) {
		this.deleteMessage = dM;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setBinder(new AnnotateDataBinder(comp));
		
		if (!getMainWindow().isScreenUpdated()) {
			updateApplicationScreenSize();
		}
		if (winPanel != null) {
			winPanel.setHeight(getMainWindow().getPanelHeight() + "px");
		}
	}
	
	/**
	 * Methode d'assignation de l'objet peuplant la fiche (backing-bean).
	 * Passe l'objet à la fiche annotation.
	 */
	public void setObject(TKdataObject obj) {
		if (getObjectTabController() != null 
					&& getObjectTabController().getFicheAnnotation() != null) {
			getObjectTabController().getFicheAnnotation()
											.setObj((TKAnnotableObject) obj);
		}	
		getBinder().loadAll();
	}
	
	public void onClick$historique() {
		openHistoriqueWindow(page, getObject());
	}
	
	/*************************************************************************/	
	/************************** ABSTRACTS ************************************/	
	/*************************************************************************/	
	public abstract void setParentObject(TKdataObject obj);	
	public abstract TKdataObject getParentObject();
		
	/**
	 * Methode d'assignation d'un objet vide peuplant la fiche (backing-bean).
	 */
	public abstract void setNewObject();
	
	/**
	 * Retourne l'objet peuplant la fiche (backing-bean).
	 * @return objet
	 */
	public abstract TKdataObject getObject();
	
	/*************************************************************************/
	/****************** MODIFICATION MULTIPLES *******************************/
	/*************************************************************************/

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur un lien pour 
	 * réaliser une modification multiple.
	 * @param page dans laquelle inclure la modale
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param allValuesThesaurus Toutes les valeurs possibles que
	 * peut prendre le champ à modifier.
	 * @param champNameThesaurus Nom du champ pour l'affichage des
	 * valeurs du thésaurus.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean isCombined true si champAnnotation combine
	 * @param Boolean true si formatage upperCase onBlur pour textbox
	 * @param isObligatoire true si chp annotation obligatoire
	 */
	public void openModificationMultipleWindow(Page page, 
			String pathToPage, String methodToCall,
			String typeModal, List< ? extends Object> objs, String label, 
			String champToEdit, List<Object> allValuesThesaurus,
			String champThesaurus, String entiteNom, Constraint constr,
			Boolean isCombined, 
			Boolean upperCaseOnBlur, Boolean isOblig) {
		 if (!isBlockModal()) {
			
			setBlockModal(true);
			
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("modificationMultipleWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels
						.getLabel("general.actions.modification.multiple"));
			win.setBorder("normal");
			win.setWidth("800px");
			win.setHeight("230px");
			win.setClosable(true);
			
			if (typeModal.equals("Textbox")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleTextbox(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, entiteNom, constr, 
										isCombined, false, upperCaseOnBlur);
				
			} else if (typeModal.equals("Listbox")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleListbox(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, allValuesThesaurus, 
							champThesaurus, entiteNom, constr, 
							isCombined, isOblig);
	
			} else if (typeModal.equals("Combobox")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleCombobox(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, allValuesThesaurus, 
							champThesaurus, entiteNom, constr, 
							isCombined, isOblig);
	
			} else if (typeModal.equals("Datebox")) {
				final HtmlMacroComponent ua =
					populateModificationMultipleDatebox(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, entiteNom, constr, isCombined);
				
				((ModificationMultipleDatebox) ua
						.getFellow("winModificationMultipleDatebox")
						.getAttributeOrFellow("winModificationMultipleDatebox$composer", 
																		true))
									.setCalendar(false);
				
			} else if (typeModal.equals("Calendarbox")) {
				final HtmlMacroComponent ua =
				populateModificationMultipleDatebox(win, page, 
						pathToPage, methodToCall,
						objs, label, 
						champToEdit, entiteNom, constr, isCombined);
				
				((ModificationMultipleDatebox) ua
					.getFellow("winModificationMultipleDatebox")
					.getAttributeOrFellow("winModificationMultipleDatebox$composer", 
																	true))
							.setCalendar(true);
			
			}  else if (typeModal.equals("Quantification")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleQuantification(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit,
							allValuesThesaurus, champThesaurus, entiteNom, 
							null);
	
			} else if (typeModal.equals("Checkbox")) {
				//final HtmlMacroComponent ua = 
					populateModificationMultipleCheckbox(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, entiteNom, constr);
				
			} else if (typeModal.equals("Doublebox")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleDoublebox(win, page, 
						pathToPage, methodToCall,
						objs, label, 
						champToEdit, entiteNom, constr, isCombined);		
			} else if (typeModal.equals("Intbox")) {
				final HtmlMacroComponent ua =
					populateModificationMultipleDoublebox(win, page, 
						pathToPage, methodToCall,
						objs, label, 
						champToEdit, entiteNom, constr, isCombined);
				
				((ModificationMultipleDoublebox) ua
						.getFellow("winModificationMultipleDoublebox")
					.getAttributeOrFellow("winModificationMultipleDoublebox$composer", 
																		true))
						.setInteger(true);
			} else if (typeModal.equals("Floatbox")) {
				final HtmlMacroComponent ua =
					populateModificationMultipleDoublebox(win, page, 
						pathToPage, methodToCall,
						objs, label, 
						champToEdit, entiteNom, constr, isCombined);
				
				((ModificationMultipleDoublebox) ua
						.getFellow("winModificationMultipleDoublebox")
					.getAttributeOrFellow("winModificationMultipleDoublebox$composer", 
																		true))
						.setFloat(true);
			} else if (typeModal.equals("BigTextbox")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleTextbox(win, page, 
						pathToPage, methodToCall,
						objs, label, 
						champToEdit, entiteNom, constr, 
									isCombined, true, upperCaseOnBlur);		
			} else if (typeModal.equals("MultiListbox")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleMultiListbox(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, allValuesThesaurus, 
							champThesaurus, entiteNom, constr, 
							isCombined, isOblig);
			} else if (typeModal.equals("Modal")) {
				//final HtmlMacroComponent ua =
					populateModificationMultipleModal(win, page, 
							pathToPage, methodToCall,
							objs, label, 
							champToEdit, entiteNom, constr, isCombined);
			} else if (typeModal.equals("CodesBox")) {
				populateModificationMultipleCodeAssigne(win, page, 
							pathToPage, methodToCall,
							objs, label, champToEdit);
				
			} else if (typeModal.equals("Filebox")) {
				populateModificationMultipleFile(win, page, 
								pathToPage, methodToCall,
								objs, label, champToEdit, entiteNom);
					
			} else if (typeModal.equals("Conformitebox")) {				
				populateModificationMultipleNonConformites(win, page, 
							pathToPage, methodToCall,
								objs, label, champToEdit,
							allValuesThesaurus,
							champThesaurus, entiteNom);
					
			}
			
			// ua.setVisible(false);
			win.getFirstChild().setVisible(false);
	
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					win.getFirstChild().setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
	
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		}
	}

	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un Textbox.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 * @param Boolean true si champAnnotation Texte
	 * @param Boolean true si formatage upperCase onBlur
	 */
	private static HtmlMacroComponent populateModificationMultipleTextbox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, String entiteNom, Constraint constr,
			Boolean isCombined, Boolean isTextArea,
			Boolean upperCaseOnBlur) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
			page.getComponentDefinition("modificationMultipleTextbox", false)
			.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleTextbox");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleTextbox) ua
				.getFellow("winModificationMultipleTextbox")
				.getAttributeOrFellow("winModificationMultipleTextbox$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						entiteNom, constr, isCombined);
		
		// dessine un textArea au besoin
		if (isTextArea) {
			((ModificationMultipleTextbox) ua
				.getFellow("winModificationMultipleTextbox")
				.getAttributeOrFellow("winModificationMultipleTextbox$composer", true))
					.setTextAreaBoxes();
			win.setHeight("200px");
		}
		
		((ModificationMultipleTextbox) ua
			.getFellow("winModificationMultipleTextbox")
				.getAttributeOrFellow("winModificationMultipleTextbox$composer", true))
					.setUpperCaseOnBlur(upperCaseOnBlur);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un Listbox (ou d'un booleen).
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param allValuesThesaurus Toutes les valeurs possibles que
	 * peut prendre le champ à modifier.
	 * @param champNameThesaurus Nom du champ pour l'affichage des
	 * valeurs du thésaurus.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 * @param isObligatoire true si chp annotation obligatoire
	 */
	private static HtmlMacroComponent populateModificationMultipleListbox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, List<Object> allValuesThesaurus,
			String champThesaurus, String entiteNom, Constraint constr,
			Boolean isCombined, Boolean isOblig) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleListbox", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleListbox");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleListbox) ua
				.getFellow("winModificationMultipleListbox")
				.getAttributeOrFellow("winModificationMultipleListbox$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit, 
						allValuesThesaurus, 
						champThesaurus, entiteNom, constr, isCombined, isOblig);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un Combobox.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param allValuesThesaurus Toutes les valeurs possibles que
	 * peut prendre le champ à modifier.
	 * @param champNameThesaurus Nom du champ pour l'affichage des
	 * valeurs du thésaurus.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 * @param isObligatoire true si chp annotation obligatoire
	 */
	private static HtmlMacroComponent populateModificationMultipleCombobox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, List<Object> allValuesThesaurus,
			String champThesaurus, String entiteNom, Constraint constr,
			Boolean isCombined, Boolean isOblig) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleCombobox", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleCombobox");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleCombobox) ua
				.getFellow("winModificationMultipleCombobox")
				.getAttributeOrFellow("winModificationMultipleCombobox$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit, 
						allValuesThesaurus, 
						champThesaurus, entiteNom, constr, isCombined, isOblig);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un Datebox.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 */
	private static HtmlMacroComponent populateModificationMultipleDatebox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, String entiteNom, Constraint constr,
			Boolean isCombined) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleDatebox", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleDatebox");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleDatebox) ua
				.getFellow("winModificationMultipleDatebox")
				.getAttributeOrFellow("winModificationMultipleDatebox$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						entiteNom, constr, isCombined);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'une quantification.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param allValuesThesaurus Toutes les valeurs possibles que
	 * peut prendre le champ à modifier.
	 * @param champNameThesaurus Nom du champ pour l'affichage des
	 * valeurs du thésaurus.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 */
	private static HtmlMacroComponent 
		populateModificationMultipleQuantification(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, List<Object> allValuesThesaurus,
			String champThesaurus, String entiteNom, Constraint constr
			) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleQuantification", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleQuantification");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleQuantification) ua
				.getFellow("winModificationMultipleQuantification")
				.getAttributeOrFellow("winModificationMultipleQuantification$composer"
						, true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						allValuesThesaurus, champThesaurus, entiteNom, constr, 
																		null);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un Checkbox.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 */
	private static HtmlMacroComponent populateModificationMultipleCheckbox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, String entiteNom, Constraint constr) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleCheckbox", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleCheckbox");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleCheckbox) ua
				.getFellow("winModificationMultipleCheckbox")
				.getAttributeOrFellow("winModificationMultipleCheckbox$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						entiteNom, constr, false);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un Doublebox.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 */
	private static HtmlMacroComponent populateModificationMultipleDoublebox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, String entiteNom, Constraint constr,
			Boolean isCombined) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleDoublebox", false)
													.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleDoublebox");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleDoublebox) ua
				.getFellow("winModificationMultipleDoublebox")
				.getAttributeOrFellow("winModificationMultipleDoublebox$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						entiteNom, constr, isCombined);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'une liste à choix multiples.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param allValuesThesaurus Toutes les valeurs possibles que
	 * peut prendre le champ à modifier.
	 * @param champNameThesaurus Nom du champ pour l'affichage des
	 * valeurs du thésaurus.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 */
	private static HtmlMacroComponent populateModificationMultipleMultiListbox(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, List<Object> allValuesThesaurus,
			String champThesaurus, String entiteNom, Constraint constr,
			Boolean isCombined, Boolean isOblig) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleMultiListbox", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultiplMultiListbox");
		ua.applyProperties();
		ua.afterCompose();
		//win.setWidth("100%");
		
		((ModificationMultipleMultiListbox) ua
				.getFellow("winModificationMultipleMultiListbox")
				.getAttributeOrFellow("winModificationMultipleMultiListbox$composer", 
																		true))
				.init(pathToPage, methodToCall, objs, label, champToEdit, 
						allValuesThesaurus, 
						champThesaurus, entiteNom, constr, isCombined, isOblig);
		
		int height = 200 + (allValuesThesaurus.size() * 20);
		win.setHeight(String.valueOf(height) + "px");
		((Panel) ua
			.getFellow("winModificationMultipleMultiListbox")
				.getFirstChild()).setHeight(String.valueOf(height - 75) + "px");
		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple d'un champ renseignable par une modal.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 */
	private static HtmlMacroComponent populateModificationMultipleModal(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, String entiteNom, Constraint constr,
			Boolean isCombined) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleModal", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleModal");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleModal) ua
				.getFellow("winModificationMultipleModal")
				.getAttributeOrFellow("winModificationMultipleModal$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						entiteNom, constr, isCombined);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple des codes assignes organes ou lesionnels.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param champToEdit Champ de l'entité à modifier.
	 */
	private static HtmlMacroComponent populateModificationMultipleCodeAssigne(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, String champToEdit) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleCodeAssigne", false)
													.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleCodeAssigne");
		win.setHeight("300px");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleCodeAssigne) ua
			.getFellow("winModificationMultipleCodeAssigne")
			.getAttributeOrFellow("winModificationMultipleCodeAssigne$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						null, null, false);

		return ua;
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple des d'un lien vers un fichier enregistré dans le système.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param champToEdit Champ de l'entité à modifier.
	 */
	private static HtmlMacroComponent 
					populateModificationMultipleFile(Window win, Page page,
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, String champToEdit, 
			String entiteNom) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleFile", false)
													.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleFile");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleFile) ua
			.getFellow("winModificationMultipleFile")
			.getAttributeOrFellow("winModificationMultipleFile$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						entiteNom, null, false);

		return ua;
		
	}
	
	/**
	 * Méthode appelée pour créer le composant contenant la modification
	 * multiple des non conformites.
	 * @param win Window contenant le composant.
	 * @param page Page contenant la définition du composant.
	 * @param pathToPage Chemin vers la page qui demande une modif.
	 * @param methodToCall Méthode à appeler
	 * @param objs Liste des objets à modifier
	 * @param label Code pour label du champ dans .properties 
	 * internationalisation.
	 * @param entiteToEdit Nom de l'entité à modifier. 
	 * @param champToEdit Champ de l'entité à modifier.
	 * @param allValuesThesaurus Toutes les valeurs possibles que
	 * peut prendre le champ à modifier.
	 * @param champNameThesaurus Nom du champ pour l'affichage des
	 * valeurs du thésaurus.
	 * @param ent nom de l'entite a afficher dans l'intitulé
	 * @param Constraint à appliquer
	 * @param Boolean true si champAnnotation combine
	 */
	private static HtmlMacroComponent populateModificationMultipleNonConformites(
			Window win, Page page, 
			String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, List<? extends Object> allValuesThesaurus,
			String champThesaurus, String entiteNom) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant de la modif multiple.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("modificationMultipleNonConformites", false)
													.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openModificationMultipleNonConformite");
		ua.applyProperties();
		ua.afterCompose();
		
		((ModificationMultipleNonConformites) ua
			.getFellow("winModificationMultipleNonConformites")
			.getAttributeOrFellow("winModificationMultipleNonConformites$composer", true))
				.init(pathToPage, methodToCall, objs, label, champToEdit,
						allValuesThesaurus, champThesaurus, entiteNom);

		return ua;
		
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * pour imprimer la page. Cette méthode va créer une nouvelle
	 * fenêtre.
	 * @param page dans laquelle inclure la modale
	 * @param objToPrint Objet à imprimer.
	 */
	public void openImpressionWindow(Page page, Object objToPrint,
			String title, Boolean anonyme, Boolean canHistorique) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("impressionWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(title);
			win.setBorder("normal");
			win.setWidth("650px");
			int height = getMainWindow().getPanelHeight() + 30;
			win.setHeight(height + "px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = populateImpressionModal(
					win, page, objToPrint, anonyme, canHistorique);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent populateImpressionModal(
			Window win, Page page,
			Object objToPrint, Boolean anonyme,
			Boolean canHistorique) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("ficheTemplateModale", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openImpressionModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((FicheTemplateModale) ua.getFellow("fwinTemplateModale")
				.getAttributeOrFellow("fwinTemplateModale$composer", true))
				.initFicheModale(objToPrint, anonyme, canHistorique);
		
		return ua;
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * pour modifier un champ d'un thésaurus.
	 * @param page dans laquelle inclure la modale
	 * @param objToPrint Objet à imprimer.
	 */
	public void openChampThesaurusWindow(Page page, 
			String path, Object value, Constraint cons,
			Constraint cons2, 
			String titlePage, boolean createMode) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("champThesaurusWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setBorder("normal");
			win.setWidth("550px");
			win.setHeight("175px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = populateChampThesaurus(win, 
					page, path, value, cons, cons2, titlePage, createMode);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent populateChampThesaurus(
			Window win, Page page,
			String path, Object value, Constraint cons,
			Constraint cons2, 
			String titlePage, boolean createMode) {
		// HtmlMacroComponent contenu dans la fenêtre
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("ficheChampThesaurus", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openChampThesaurus");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((FicheChampThesaurus) ua.getFellow("fwinChampThesaurus")
				.getAttributeOrFellow("fwinChampThesaurus$composer", true))
				.init(path, value, cons, cons2, titlePage, createMode);
		
		return ua;
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * pour voir l'historique. Cette méthode va créer une nouvelle
	 * fenêtre.
	 * @param page dans laquelle inclure la modale
	 * @param historiqueObject Objet pour lequel on veut l'historique.
	 */
	public void openHistoriqueWindow(Page page, TKdataObject historiqueObject) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("historiqueWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("general.historique"));
			win.setBorder("normal");
			win.setWidth("670px");
			int height = getMainWindow().getPanelHeight() + 30;
			win.setHeight(height + "px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = populateHistoriqueModal(
					win, page, historiqueObject);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent populateHistoriqueModal(
			Window win, Page page,
			TKdataObject historiqueObj) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("ficheHistoriqueModale", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openHistoriqueModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((FicheHistoriqueModale) ua.getFellow("fwinHistoriqueModale")
				.getAttributeOrFellow("fwinHistoriqueModale$composer", true))
				.setObject(historiqueObj);
		
		return ua;
	}
	

	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * pour voir le bon de livraison. Cette méthode va créer une nouvelle
	 * fenêtre.
	 * @param page dans laquelle inclure la modale
	 * @param historiqueObject Objet pour lequel on veut l'historique.
	 */
	public void openBonLivraisonWindow(Page page, Cession cession) {
			// List<CederObjetDecorator> echans,
			// List<CederObjetDecorator> derives) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("bonLivraisonWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("general.bon.livraison"));
			win.setBorder("normal");
			win.setWidth("650px");
			win.setHeight("600px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = populateBonLivraisonModal(
					win, page, cession); 
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent populateBonLivraisonModal(
			Window win, Page page,
			Cession cession) {
			// List<CederObjetDecorator> echans,
			// List<CederObjetDecorator> derives) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("ficheBonLivraisonModale", false)
		.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openBonLivraisonModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((FicheBonLivraisonModale) ua.getFellow("fwinBonLivraisonModale")
				.getAttributeOrFellow("fwinBonLivraisonModale$composer", true))
				.init(cession); //, echans, derives);
		
		return ua;
	}
	
	/**
	 * PopUp window appelée pour afficher les résultats de l'import.
	 * @param page
	 * @param importOk
	 * @param histo
	 * @param errs
	 * @param is
	 * @param wb
	 * @param currSheetName
	 * @version 2.0.10.6
	 */
	public boolean openResultatsImportWindow(Page page, 
			Boolean importOk, ImportHistorique histo,
			List<ImportError> errs, InputStream is, Workbook wb, 
			String currSheetName, Component parent) {
		
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("resultsImportWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("message.import.title"));
			win.setBorder("normal");
			win.setWidth("420px");
			int height = 340;
			win.setHeight(String.valueOf(height) + "px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua;
			ua = (HtmlMacroComponent)
					page.getComponentDefinition("resultatsImportModale", false)
												.newInstance(page, null);
			ua.setParent(win);
			ua.setId("resultatsImportModaleComponent");
			ua.applyProperties();
			ua.afterCompose(); 
			
			((ResultatsImportModale) ua
					.getFellow("fwinResultatsImportModale")
					.getAttributeOrFellow("fwinResultatsImportModale$composer", true))
					.init(importOk, histo, errs, is, wb, currSheetName, height, parent);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
		 
		 return isBlockModal();
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu item
	 * pour changer la numérotations de terminales.
	 */
	public void openChangeNumerotationModaleWindow(Object parent) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("changeNumerotationWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels
					.getLabel("terminale.numerotation.edit"));
			win.setBorder("normal");
			win.setWidth("500px");
			//win.setHeight("600px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = 
				populateChangeNumerotationModal(parent, win, page, 
													getMainWindow());
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent 
		populateChangeNumerotationModal(Object parent, Window win, 
												Page page, MainWindow main) {
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("changeNumerotationModale", false)
												.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openchangeNumerotationModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((ChangeNumerotationModale) ua.getFellow("fwinChangeNumerotationModale")
				.getAttributeOrFellow(
						"fwinChangeNumerotationModale$composer", true))
				.init(parent, main);
		
		return ua;
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le menu item
	 * pour changer la taille d'une enceinte.
	 */
	public void openChangeTailleEnceinteModaleWindow(
			Component parent, Enceinte obj) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("changeTailleEnceinteWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels
					.getLabel("enceinte.taille.edit"));
			win.setBorder("normal");
			win.setWidth("500px");
			//win.setHeight("600px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = 
				populateChangeTailleEnceinteModal(parent, win, page, obj);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent 
		populateChangeTailleEnceinteModal(Component parent, Window win, 
												Page page, Enceinte obj) {
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("ficheChangeTailleEnceinteModale", false)
												.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openChangeTailleEnceinteModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((ChangeTailleEnceinteModale) ua
				.getFellow("fwinChangeTailleEnceinteModale")
				.getAttributeOrFellow(
					"fwinChangeTailleEnceinteModale$composer", true))
				.init(parent, obj);
		
		return ua;
	}
	
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur le lien
	 * pour voir recherché les dossiers existants pour l'interfacage
	 * avec d'autres logiciels.
	 */
	public void openSelectDossierExterneWindow(Page page, 
			String path, String critere,
			boolean isEdit,
			Prelevement prlvt) {
		 if (!isBlockModal()) {
				
			 setBlockModal(true);
		
			// nouvelle fenêtre
			final Window win = new Window();
			win.setVisible(false);
			win.setId("selectDossierExterneWindow");
			win.setPage(page);
			win.setMaximizable(true);
			win.setSizable(true);
			win.setTitle(Labels.getLabel("select.dossierExternes.title"));
			win.setBorder("normal");
			win.setWidth("650px");
			int height = 470;
			win.setHeight(height + "px");
			win.setClosable(true);
			
			final HtmlMacroComponent ua = populateSelectDossierExterneModal(
					win, page, path, critere, isEdit, prlvt);
			ua.setVisible(false);
			
			win.addEventListener("onTimed", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					//progress.detach();
					ua.setVisible(true);
				}
			});
			
			Timer timer = new Timer();
			timer.setDelay(500);
			timer.setRepeats(false);
			timer.addForward("onTimer", timer.getParent(), "onTimed");
			win.appendChild(timer);
			timer.start();
			
			try {
				win.onModal();
				setBlockModal(false);
	
			} catch (SuspendNotAllowedException e) { log.error(e);
			}
		 }
	}
	
	private static HtmlMacroComponent populateSelectDossierExterneModal(
			Window win, Page page,
			String path, String critere,
			boolean isEdit,
			Prelevement prlvt) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
		page.getComponentDefinition("ficheSelectDossierExterneModale", false)
			.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openSelectDossierExterneModale");
		ua.applyProperties();
		ua.afterCompose(); 
		
		((SelectDossierExterneModale) 
				ua.getFellow("fwinSelectDossierExterneModale")
				.getAttributeOrFellow(
						"fwinSelectDossierExterneModale$composer", true))
				.init(path, critere, isEdit, prlvt);
		
		return ua;
	}
	
	/**** cession revert obj unit methods *****/
	/**** @since 2.1 *****/
	
	/**
	 * Revert one cessed echantillon from its decorator
	 * Code refactoring so that method can be used for single objet revert in cession edit
	 * @since 2.1.1
	 * @param deco
	 */
	protected void revertEchantillon(Object deco) {
		Echantillon echanToUpdate = ((CederObjetDecorator) deco).getEchantillon();
		
		if (echanToUpdate.getQuantite() != null) { 
			if (((CederObjetDecorator) deco).getCederObjet().getQuantite() != null 
					&& echanToUpdate.getQuantiteInit() != null) { // check qte Init @since 2.1.1
				echanToUpdate.setQuantite(((CederObjetDecorator) deco).getQuantiteMax());
			} else {
				echanToUpdate.setQuantite(null);
			}
		}

		// maj du statut
		ObjetStatut statut = null;
		// ObjetStatut oldStatut = echanToUpdate.getObjetStatut();
		statut = findStatutForTKStockableObject(echanToUpdate);
		if (statut.getStatut().equals("ENCOURS")) {
			List<Integer> objsId = new ArrayList<Integer>();
			objsId.add(echanToUpdate.getEchantillonId());
			statut = ManagerLocator.getRetourManager()
					.findByObjectDateRetourEmptyManager(objsId, 
				ManagerLocator.getEntiteManager().findByIdManager(3))
				.get(0).getObjetStatut();
		}

		List<OperationType> ops = new ArrayList<OperationType>();

		// Update echantillon
		Prelevement prlvt = ManagerLocator.getEchantillonManager()
				.getPrelevementManager(echanToUpdate);
		ManagerLocator.getEchantillonManager().updateObjectManager(
				echanToUpdate, echanToUpdate.getBanque(), prlvt,
				echanToUpdate.getCollaborateur(), statut,
				echanToUpdate.getEmplacement(),
				echanToUpdate.getEchantillonType(), null, null,
				echanToUpdate.getQuantiteUnite(),
				echanToUpdate.getEchanQualite(),
				echanToUpdate.getModePrepa(), 
//				echanToUpdate.getCrAnapath(), null, 
				echanToUpdate.getReservation(), null, null, null, null,
				SessionUtils.getLoggedUser(sessionScope), false, ops, null);
		
	}
	
	/**
	 * Revert one cessed prodDErive from its decorator
	 * Code refactoring so that method can be used for single objet revert in cession edit
	 * @since 2.1.1
	 * @param deco
	 */
	protected void revertProdDerive(Object deco) {
		ProdDerive deriveToUpdate = 
				((CederObjetDecorator) deco).getProdDerive();				

		if (((CederObjetDecorator) deco).getSelectedUnite() != null) { 
			if (((CederObjetDecorator) deco).getSelectedUnite().getType().equals("volume")) { // restore from volume
				if (deriveToUpdate.getVolume() != null) { 
					if (deriveToUpdate.getVolumeInit() != null) { // check volumeInit @since 2.1.1
						deriveToUpdate.setVolume(((CederObjetDecorator) deco)
														.getQuantiteMax());
					} else {
						deriveToUpdate.setVolume(null);
					}
					deriveToUpdate.setQuantite(
						calculerQuantiteRestante(
						deriveToUpdate, deriveToUpdate.getVolume()));
				}
			} else { // restore from quantite
				if (deriveToUpdate.getQuantite() != null) {
					if (deriveToUpdate.getQuantiteInit() != null) { // check qteInit @since 2.1.1
						deriveToUpdate.setQuantite(((CederObjetDecorator) deco)
														.getQuantiteMax());
					} else {
						deriveToUpdate.setQuantite(null);
					}
					deriveToUpdate
					.setVolume(calculerVolumeRestant(deriveToUpdate,
						deriveToUpdate.getQuantite()));
				}
			}
		} else { // @since 2.1.1 pas d'unité... 
			if (deriveToUpdate.getVolume() != null) { 
				if (deriveToUpdate.getVolumeInit() != null) {
					deriveToUpdate.setVolume(((CederObjetDecorator) deco)
													.getQuantiteMax());
				} else {
					deriveToUpdate.setVolume(null);
				}
				deriveToUpdate.setQuantite(
					calculerQuantiteRestante(
					deriveToUpdate, deriveToUpdate.getVolume()));
			}
			if (deriveToUpdate.getQuantite() != null) {
				if (deriveToUpdate.getQuantiteInit() != null) {
					deriveToUpdate.setQuantite(((CederObjetDecorator) deco)
													.getQuantiteMax());
				} else {
					deriveToUpdate.setQuantite(null);
				}
				deriveToUpdate
				.setVolume(calculerVolumeRestant(deriveToUpdate,
					deriveToUpdate.getQuantite()));
			}
		}
	
		// maj du statut
		// maj du statut
		ObjetStatut statut = null;
		// ObjetStatut oldStatut = echanToUpdate.getObjetStatut();
		statut = findStatutForTKStockableObject(deriveToUpdate);
		if (statut.getStatut().equals("ENCOURS")) {
			List<Integer> objsId = new ArrayList<Integer>();
			objsId.add(deriveToUpdate.getProdDeriveId());
			statut = ManagerLocator.getRetourManager()
					.findByObjectDateRetourEmptyManager(objsId, 
				ManagerLocator.getEntiteManager().findByIdManager(8))
				.get(0).getObjetStatut();
		}
		
		List<OperationType> ops = new ArrayList<OperationType>();
		
		// update du dérvié
		ManagerLocator.getProdDeriveManager().updateObjectManager(
				deriveToUpdate, deriveToUpdate.getBanque(),
				deriveToUpdate.getProdType(), statut,
				deriveToUpdate.getCollaborateur(),
				deriveToUpdate.getEmplacement(),
				deriveToUpdate.getVolumeUnite(),
				deriveToUpdate.getConcUnite(),
				deriveToUpdate.getQuantiteUnite(),
				deriveToUpdate.getModePrepaDerive(),
				deriveToUpdate.getProdQualite(),
				deriveToUpdate.getTransformation(), 
				null, null, null, null,
				deriveToUpdate.getReservation(),
				SessionUtils.getLoggedUser(sessionScope), false, ops, null);
			
	}

}
