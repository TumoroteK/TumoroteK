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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.ContratController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Abstract classe regroupant les comportements partagés par les fiches 
 * utilisées pour l'affichage statique des données de l'application.
 * Date: 24/07/2010
 * 
 * Gere les droits, les actions communes 'new', 'edit', 'delete', 
 * mise à jour des annotations et déclare abstract les methodes 
 * portant sur la manipulation de l'objet (backing-bean).
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public abstract class AbstractFicheStaticController 
										extends AbstractFicheController {

	private static final long serialVersionUID = 8935058921588644322L;

	//Composants
	protected Grid formGrid;
	protected Button edit;
	protected Button delete;
	protected Button addNew;
	protected Menuitem print;
	protected Menuitem historique;
	
	// Variable de droits.
	private boolean canEdit;
	private boolean canDelete;
	private boolean canNew;
	private boolean canSeeHistorique;
	private boolean isAnonyme;
	private boolean isAdmin;
	
	private String deletionMessage;

	public String getDeletionMessage() {
		return deletionMessage;
	}

	public void setDeletionMessage(String delMessage) {
		this.deletionMessage = delMessage;
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		applyDroitsOnFiche();
	}
	
	/**
	 * Passe la fiche associee en formulaire vide.
	 */
	public void onClick$addNew() {
		getObjectTabController().switchToCreateMode(null);
	}
	
	/**
	 * Passe la fiche en formulaire editable.
	 * Active les boutons validate et revert.	
	 */
	public void onClick$edit() {
		getObjectTabController().switchToEditMode(getObject());
	}
	
	/**
	 * Supprime l'objet de la base.
	 * Retire l'objet de la liste.
	 */
	public void onClick$delete() {
		if (getObject() != null) {
			
			prepareDeleteObject();
		
			openDeleteWindow(page, getDeleteMessage(), isCascadable(), 
						isFantomable(), isDeletable(), self, false);
//			
//			// on fait apparaître un fenêtre demandant à l'utilisateur 
//			// s'il est sûr de vouloir supprimer l'objet.
//			try {
//				if (Messagebox.show(ObjectTypesFormatters.getLabel(
//						"message.deletion.message", 
//						new String[]{Labels
//							.getLabel(getDeletionMessage())}), 
//						Labels.getLabel("message.deletion.title"), 
//						Messagebox.YES | Messagebox.NO, 
//						Messagebox.QUESTION) == Messagebox.YES) {
//				
//					// suppression du patient
//					removeObject();
//
//					if (getObjectTabController().getListe() != null) {
//						// on enlève l'échantillon de la liste
//						getObjectTabController().getListe().
//							removeFromObjectList(getObject());
//					}
//					getObjectTabController().clearStaticFiche();
//					getObjectTabController().switchToOnlyListeMode();
//				}
//			} catch (InterruptedException e) {
//				log.error(e);
//			}	
		}
	}
	
	/**
	 * Recoit l'evenement envoyé depuis la modale pour 
	 * commander la suppression. L'evenement contient en data 
	 * les commentaires liés à la suppression.
	 * @param event
	 */
	public void onDeleteTriggered(Event event) {
		Clients.showBusy(Labels.getLabel("deletion.general.wait"));
		String comments = null;
		if (event.getData() != null) {
			comments = (String) event.getData();
		}
		Events.echoEvent("onLaterDelete", self, comments);
	}
	
	public void onLaterDelete(Event event) {
		try {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(((TKdataObject) getObject()).listableObjectId());
			
			Map<Entite, List<Integer>> children = getObjectTabController()
							.getChildrenObjectsIds(ids);
						
			Map<Entite, List<Integer>> parents = getObjectTabController()
					.getParentsObjectsIds(ids);
			
			removeObject((String) event.getData());
			if (getObjectTabController() != null) {
				if (getObjectTabController().getListe() != null) {
					// on enlève l'objet de la liste
					getObjectTabController().getListe().
						removeObjectAndUpdateList(getObject());
				}
				getObjectTabController().clearStaticFiche();
				getObjectTabController().switchToOnlyListeMode();
			}
			
//			// update de l'objet parent
//			if (!getObjectTabController()
//					.getReferencingObjectControllers().isEmpty()
//												&& parent != null) {
//				for (int i = 0; i < getObjectTabController()
//							.getReferencingObjectControllers().size(); i++) {
//					if (getObjectTabController()
//							.getReferencingObjectControllers()
//												.get(i).getListe() != null) {
//						getObjectTabController()
//							.getReferencingObjectControllers().get(i).getListe()
//						.updateObjectGridListFromOtherPage(parent, true);
//					}
//				}
//			}
			
			// update de la liste des parents
			getObjectTabController().updateParentsReferences(parents);
			
			// update de la liste des enfants
			getObjectTabController().updateChildrenReferences(children, true);
			
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} finally {
			// ferme wait message
			Clients.clearBusy();
		}
	}
	
	
	
	@Override
	public void setObject(TKdataObject obj) {
		super.setObject(obj);
		disableToolBar(false);
	}
	
	@Override
	public void setNewObject() {
		edit.setDisabled(true);
		delete.setDisabled(true);
		print.setDisabled(true);
		historique.setDisabled(true);
	}
	

	public void reloadObject() {
		if (getObject() != null) {
			TKdataObject obj = getObjectTabController().loadById(getObject().listableObjectId());
			if (obj != null) {
				setObject(obj);
			} else {
				setNewObject();
			}
		}
	}
	
	/**
	 * Méthode qui ferme la fiche : seule la liste sera visible.
	 * @param event onClose de la fiche.
	 */
	public void onClose$winPanel(Event event) {
		Events.getRealOrigin((ForwardEvent) event).stopPropagation();	
		getObjectTabController().clearStaticFiche();
		getObjectTabController().switchToOnlyListeMode();
	}
	
	/**
	 * Gèle ou dégèle les boutons de la toolbar de la fiche statique.
	 * Cascade vers les boutons de la liste et de la fiche annotation.
	 * @param b
	 */
	public void disableToolBar(boolean b) {
		edit.setDisabled(b || !isCanEdit());
		delete.setDisabled(b || !isCanDelete());
		addNew.setDisabled(b || !isCanNew());
		print.setDisabled(b);
		historique.setDisabled(b || !isCanSeeHistorique());
		getObjectTabController().getListe().disableToolBar(b);
		if (getObjectTabController().getFicheAnnotation() != null) {
			getObjectTabController().getFicheAnnotation().disableEdit(b);
		}
	}
	
	public void onClick$print() {
		StringBuffer sb = new StringBuffer();
		if (getObject().getClass().getSimpleName().equals("Patient")) {
			sb.append(Labels.getLabel("impression.print.patient"));
			sb.append(" ");
			sb.append(((Patient) getObject()).getNom());
		} else if (getObject().getClass().getSimpleName()
				.equals("Prelevement")) {
			sb.append(Labels.getLabel("impression.print.prelevement"));
			sb.append(" ");
			sb.append(((Prelevement) getObject()).getCode());
		} else if (getObject().getClass().getSimpleName()
				.equals("Echantillon")) {
			sb.append(Labels.getLabel("impression.print.echantillon"));
			sb.append(" ");
			sb.append(((Echantillon) getObject()).getCode());
		} else if (getObject().getClass().getSimpleName()
				.equals("ProdDerive")) {
			sb.append(Labels.getLabel("impression.print.prodDerive"));
			sb.append(" ");
			sb.append(((ProdDerive) getObject()).getCode());
		} else if (getObject().getClass().getSimpleName()
				.equals("Cession")) {
			sb.append(Labels.getLabel("impression.print.cession"));
			sb.append(" ");
			sb.append(((Cession) getObject()).getNumero());
		}
		
		openImpressionWindow(page, getObject(), 
				sb.toString(), isAnonyme(), isCanSeeHistorique());
	}
	
	
	/*************************************************************************/	
	/************************** ABSTRACTS ************************************/	
	/*************************************************************************/	
	/**
	 * Prepare la méthode de suppression de l'objet. Verifie si ce dernier 
	 * est utilisé ou référencé.
	 */
	public abstract void prepareDeleteObject();
	/**
	 * Appelle la méthode de suppression de l'objet en base de données.
	 * @param commentaires liés à la suppression.
	 */
	public abstract void removeObject(String comments);
	
	/*************************************************************************/
	/************************** DROITS ***************************************/
	/*************************************************************************/	
	public boolean isCanEdit() {
		return canEdit;
	}
	
	public void setCanEdit(boolean cEdit) {
		this.canEdit = cEdit;
	}

	public boolean isCanDelete() {
		return canDelete;
	}
	
	public void setCanDelete(boolean cDelete) {
		this.canDelete = cDelete;
	}

	public boolean isCanNew() {
		return canNew;
	}
	
	public void setCanNew(boolean cNew) {
		this.canNew = cNew;
	}
	
	public boolean isCanSeeHistorique() {
		return canSeeHistorique;
	}

	public void setCanSeeHistorique(boolean cHistorique) {
		this.canSeeHistorique = cHistorique;
	}

	/**
	 * Rend les boutons d'actions cliquables, rend les liens accessibles en 
	 * fonction des droits.
	 */
	public void applyDroitsOnFiche() {
		// application aux buttons
		addNew.setDisabled(!canNew);
		edit.setDisabled(!canEdit);
		delete.setDisabled(!canDelete);
		historique.setDisabled(!canSeeHistorique);
		
		if (sessionScope.containsKey("Anonyme")
				&& (Boolean) sessionScope.get("Anonyme")) {
			isAnonyme = true;
		} else {
			isAnonyme = false;
		}
	}
	
	/**
	 * Rend les boutons edit, addNew et delete cliquable en fonction
	 * des droits de l'utilisateur.
	 * @param nomEntite Entite (ex.:ProdDerive).
	 */
	@SuppressWarnings("unchecked")
	public void drawActionsButtons(String nomEntite) {
		Boolean admin = false;
		if (sessionScope.containsKey("AdminPF")) {
			admin = (Boolean) sessionScope.get("AdminPF");
		} else if  (sessionScope.containsKey("Admin")) {
			admin = (Boolean) sessionScope.get("Admin");
		}
		
		// si l'utilisateur est admin => boutons cliquables
		if (admin) {
			setCanNew(true);
			setCanEdit(true);
			setCanDelete(true);
			setCanSeeHistorique(true);
		} else {
			setCanSeeHistorique(false);
			// on extrait les OperationTypes de la base
			OperationType creation = ManagerLocator
				.getOperationTypeManager()
				.findByNomLikeManager("Creation", true).get(0);
			OperationType modification = ManagerLocator
				.getOperationTypeManager()
				.findByNomLikeManager("Modification", true).get(0);
			OperationType archivage = ManagerLocator
				.getOperationTypeManager()
				.findByNomLikeManager("Archivage", true).get(0);
			
			Hashtable<String, List<OperationType>> droits = 
				new Hashtable<String, List<OperationType>>();
			
			if (sessionScope.containsKey("Droits")) {
				// on extrait les droits de l'utilisateur
				droits = (Hashtable<String, List<OperationType>>) 
					sessionScope.get("Droits");
				
				List<OperationType> ops = droits.get(nomEntite);
				setCanNew(ops.contains(creation));
				setCanEdit(ops.contains(modification));
				setCanDelete(ops.contains(archivage));
			}
		}
	}
	
	
	/************************ action methods. **************************/
	
	public static void openFicheContratWindow(Page page, Contrat contrat) {
		
		
		// nouvelle fenêtre
		final Window win = new Window();
		win.setVisible(false);
		win.setId("aideContratWindow");
		win.setPage(page);
		win.setMaximizable(true);
		win.setSizable(true);
		win.setTitle("  ");
		win.setBorder("normal");
		win.setWidth("700px");
		win.setHeight("500px");
		win.setClosable(true);
		
		final HtmlMacroComponent ua = contratComponent(win, page, contrat);
				
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

		} catch (SuspendNotAllowedException e) { log.error(e);
		}
	}
	
	private static HtmlMacroComponent contratComponent(Window win, Page page, 
													Contrat contrat) {
		// HtmlMacroComponent contenu dans la fenêtre : il correspond
		// au composant des collaborations.
		HtmlMacroComponent ua;
		ua = (HtmlMacroComponent)
			page.getComponentDefinition("contrat", false)
											.newInstance(page, null);
		ua.setParent(win);
		ua.setId("openFicheContrat");
		ua.applyProperties();
		ua.afterCompose();
		
		((ContratController) ua.getFellow("winContrat")
				.getAttributeOrFellow("winContrat$composer", true))
				.switchToDetailMode(contrat);
		
		return ua;
	}
	
	public boolean isAnonyme() {
		return isAnonyme;
	}

	public void setAnonyme(boolean isAno) {
		this.isAnonyme = isAno;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isA) {
		this.isAdmin = isA;
	}
	
//	/**
//	 * Ouvre la modale contenant le formulaire permettant d'enregistrer 
//	 * ou de modifier un retour.
//	 */
//	public void openListRetourModale(TKAnnotableObject obj) {
//		if (!isBlockModal()) {
//			
//			setBlockModal(true);
//			
//			// nouvelle fenêtre
//			final Window win = new Window();
//			win.setVisible(false);
//			win.setId("lwinRetourModal");
//			win.setPage(page);
//			win.setPosition("center,top");
//			win.setHeight("350px");
//			win.setWidth("650px");
//			win.setMaximizable(true);
//			win.setSizable(true);
//			
//			win.setTitle(Labels.getLabel("listeRetour.title"));
//			win.setBorder("normal");
//			win.setClosable(true);
//
//			final HtmlMacroComponent ua = (HtmlMacroComponent)
//				 page.getComponentDefinition("listeRetoursModale", false)
//											.newInstance(page, null);
//			
//			ua.setParent(win);
//			ua.setId("lRetourModalMacro");
//			ua.applyProperties();
//			ua.afterCompose();
//			ua.setVisible(false);
//			
//			((ListeRetour) ua
//					.getFellow("lwinRetour")
//					.getAttributeOrFellow("lwinRetour$composer", true))
//						.setEmbedded(false);
//			
//			((ListeRetour) ua
//				.getFellow("lwinRetour")
//				.getAttributeOrFellow("lwinRetour$composer", true))
//					.setObject(obj);
//				
//			win.addEventListener("onTimed", new EventListener() {
//				public void onEvent(Event event) throws Exception {
//					ua.setVisible(true);
//				}
//			});
//			Timer timer = new Timer();
//			timer.setDelay(200);
//			timer.setRepeats(false);
//			timer.addForward("onTimer", timer.getParent(), "onTimed");
//			win.appendChild(timer);
//			timer.start();
//			try {
//				win.onModal();
//				setBlockModal(false);
//
//			} catch (SuspendNotAllowedException e) {
//				log.error(e);
//			} catch (InterruptedException e) {
//				log.error(e);
//			}
//		}
//	}
	
	public boolean getTtesCollections() {
		return SessionUtils
				.getSelectedBanques(sessionScope).size() > 1;
	}
}
