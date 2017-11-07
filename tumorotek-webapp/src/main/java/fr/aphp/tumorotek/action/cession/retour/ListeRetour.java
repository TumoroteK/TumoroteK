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
package fr.aphp.tumorotek.action.cession.retour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.ContratDecorator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.Retour;

public class ListeRetour extends AbstractListeController2 {
	
	//private Log log = LogFactory.getLog(ListeRetour.class);

	
	private static final long serialVersionUID = 2480945046066137749L;

	private TKStockableObject object;
	private List<TKdataObject> listObjects = 
									new ArrayList<TKdataObject>();
	private Button addNew;
	private Column editCol;
	private Column deleteCol;
	private Window lwinRetour;
	
	public Window getLwinRetour() {
		return lwinRetour;
	}

	public void setLwinRetour(Window lw) {
		this.lwinRetour = lw;
	}

	private boolean isEmbedded = true;
	private boolean canEditObject = false;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {	
		modificationItem = new Menuitem();
		super.doAfterCompose(comp);
				
		drawActionsForRetours();
		
		listPanel.setHeight("100%");
	}
	
	@Override
	public List<TKdataObject> getListObjects() {
		return this.listObjects;
	}
	
	@Override
	public void setListObjects(List< ? extends TKdataObject> objs) {
		this.listObjects.clear();
		this.listObjects.addAll(objs);
	}
	
	@Override
	public void addToListObjects(TKdataObject obj, Integer pos) {
		if (pos != null) { 
			getListObjects().add(pos.intValue(), (RetourDecorator) obj);
		} else {
			getListObjects().add((RetourDecorator) obj);
		}
	}
	
	@Override
	public void removeObjectFromList(TKdataObject obj) {
		getListObjects().remove((RetourDecorator) obj);
	}
	
	@Override
	public void setSelectedObjects(List< ? extends TKdataObject> objs) {
	}
	
	@Override
	public List<Contrat> getSelectedObjects() {
		return null;
	}
	
	public boolean isEmbedded() {
		return isEmbedded;
	}

	public void setEmbedded(boolean isE) {
		this.isEmbedded = isE;
		if (!isE) {
			listPanel.setHeight("315px");
		}
	}

	public TKStockableObject getObject() {
		return object;
	}

	public void setObject(TKStockableObject o) {
		this.object = o;
		initObjectsBox();
	}

	@Override
	public void addToSelectedObjects(TKdataObject obj) {
	}
	
	@Override
	public void removeFromSelectedObjects(TKdataObject obj) {
	}
	
	@Override
	public TKSelectObjectRenderer getListObjectsRenderer() {
		return null;
	}
	
	@Override
	public void passSelectedToList() {	
	}
	
	@Override
	public void passListToSelected() {	
	}
	
	@Override
	public void initObjectsBox() {

		listObjects.clear();
		
		List<Retour> rets = ManagerLocator.getRetourManager()
						.getRetoursForObjectManager(object);
		
		listObjects.addAll(RetourDecorator.decorateListe(rets, 
				object != null ? object.getObjetStatut() : null));
		
		setCurrentRow(null);
		setCurrentObject(null);

		getBinder().loadAttribute(
				self.getFellow("objectsListGrid"), "model");
		

	}
	
	/**
	 * Ouvre la modale en mode details.
	 * @param event
	 */
	public void onClick$viewObj(Event event) throws IOException {
		openRetourFormModale(((RetourDecorator) 
				AbstractListeController2.getBindingData((ForwardEvent) event, 
												false)).getRetour(), false);	
	}

	@Override
	public void onClick$addNew(Event event) throws Exception {
		// ouvre la modale
		openRetourFormModale(null, true);
	}
	
	/**
	 * Ouvre la modale en mode edition.
	 * @param event
	 */
	public void onClick$editObj(Event event) throws IOException {
		
		Retour retour = ((RetourDecorator) 
				AbstractListeController2.getBindingData((ForwardEvent) event, 
						false)).getRetour();
		
		if (getObject() != null && getObject().getObjetStatut() != null 
				&& (!getObject().getObjetStatut().getStatut().equals("ENCOURS") 
				|| retour.getDateRetour() == null)) {
			openRetourFormModale(retour, true);
		
			Events.postEvent("onClickUpdateSorties",
				self.getParent().getParent(), getObject());
		}
	}
	
	/**
	 * Supprime un élément de la liste.
	 * @param event
	 */
	public void onClick$deleteObj(Event event) {
		// confirmation
		if (Messagebox.show(ObjectTypesFormatters.getLabel(
				"message.deletion.message", 
				new String[]{Labels.getLabel("message.deletion.retour")}),
		Labels.getLabel("message.deletion.title"), 
		Messagebox.YES | Messagebox.NO, 
		Messagebox.QUESTION) == Messagebox.YES) {
			
			RetourDecorator deco = (RetourDecorator) 
					AbstractListeController2
						.getBindingData((ForwardEvent) event, false);
			
				
			ManagerLocator.getRetourManager()
							.removeObjectManager(deco.getRetour());
			
			setObject(ManagerLocator.getRetourManager()
					.getObjetFromRetourManager(deco.getRetour()));
						
			getListObjects().remove(deco);
			
			getBinder().loadAttribute(
					self.getFellow("objectsListGrid"), "model");
			
			Events.postEvent("onClickUpdateSorties",
					self.getParent().getParent(), getObject());
		}
	}
	
	/**
	 * Mets à jour l'objet sélectionné de la liste.
	 * @param objet
	 */
	@Override
	public void updateObjectGridList(Object obj) {
		// l'objet passé en paramètre est cloné
		ContratDecorator edit = 
			new ContratDecorator(((Contrat) obj).clone());
				
		// on vérifie que la liste a bien un objet sélectionné
		if (getCurrentObject() != null) {
			// si l'objet édité est dans la liste, il est forcément
			// sélectionné.
			// On vérifie donc que l'objet sélectionné a le meme id
			// que celui édité
			Integer idSelected = ((ContratDecorator) 
					getCurrentObject()).getContrat().getContratId();
			Integer idUpdated = edit.getContrat().getContratId();
			if (idSelected.equals(idUpdated)) {
				int ind = getListObjects().indexOf(getCurrentObject());
				// si c'est le cas, maj de la liste par 
				// suppression/insertion
				if (ind > -1) {
					getListObjects().remove(ind);
					addToListObjects(edit, new Integer(ind));
					
					getBinder().loadAttribute(objectsListGrid, "model");
					
					// on re-sélctionne la liste contenant l'obj
					Rows rows = objectsListGrid.getRows();
					List<Component> comps = rows.getChildren();
					selectRow((Row) comps.get(ind), edit);
				}
			}
		}
	}

	@Override
	public boolean updateObjectGridListFromOtherPage(Object obj, boolean select) {
		boolean updated = false;
		
		// l'objet passé en paramètre est cloné
		RetourDecorator edit = new RetourDecorator((Retour) obj, getObject().getObjetStatut());
		
		// si la liste contient l'objet updaté
		if (getListObjects().contains(edit)) {
			
			// déselection de la liste courante
			deselectRow();
			
			// on récupère l'objet et on le met à jour par
			// suppression/insertion dans la liste
			int ind = getListObjects().indexOf(edit);
			getListObjects().remove(ind);
			addToListObjects(edit, new Integer(ind));
			
			// maj de la grille
			getBinder().loadAttribute(objectsListGrid, "model");
			
			// on récupère toutes les lignes de la grille et on
			// sélectionne celle qui contient l'obj updaté
			Rows rows = objectsListGrid.getRows();
			List<Component> comps = rows.getChildren();
			selectRow((Row) comps.get(ind), edit);
			
			// on affiche la page contenant l'objet
			objectsListGrid.getPaginal().setActivePage(
					getPageNumberForObject(getCurrentObject()));
			
			// on passe l'objet à la fiche			
			updated = true;
		}	
		
		
		return updated;
	}
	
	/**
	 * Ajoute un evenement a la liste
	 * @param Retour
	 */
	@Override
	public void addToObjectList(Object newObj) {
		// L'objet inséré est un clone de celui du formulaire
		// afin d'éviter des effets de bord lors de la modif
		// du formulaire
		RetourDecorator newRetour = new 
						RetourDecorator(((Retour) newObj).clone(), getObject().getObjetStatut());
		this.listObjects.add(newRetour);
		
		if (!objectsListGrid.isVisible()) {
			objectsListGrid.setVisible(true);
		}
		
		// update de la liste et rafraichissement taille window
		lwinRetour.invalidate();
		getBinder().loadAttribute(
				self.getFellow("objectsListGrid"), "model");
		
		// on récupère toutes les lignes de la grille et on
		// sélectionne celle du nouvel objet
		Rows rows = objectsListGrid.getRows();
		List<Component> comps = rows.getChildren();
		selectRow((Row) comps.get(
				this.listObjects.indexOf(newRetour)), newRetour);
		
		// on affiche la page sur laquelle se trouve l'objet
		objectsListGrid.getPaginal().setActivePage(
				getPageNumberForObject(getCurrentObject()));
				
		Events.postEvent("onClickUpdateSorties",
					self.getParent().getParent(), getObject());
	}

	@Override
	public void onClick$find() {
	}

	@Override
	public void updateMultiObjectsGridListFromOtherPage(List<TKdataObject> 
														objects) {
	}

	@Override
	public List<Integer> doFindObjects() {
		return null;
	}

	@Override
	public List< ? extends TKdataObject> extractObjectsFromIds(List<Integer> ids) {
		return null;
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForRetours() {
		editCol.setVisible(false);
		deleteCol.setVisible(false);
		addNew.setDisabled(true);
		
		// si l'utilisateur est admin, il peut modifier les retours
		boolean isAdmin = false;
		if (sessionScope.containsKey("AdminPF")) {
			isAdmin = (Boolean) sessionScope.get("AdminPF");
		} else if (sessionScope.containsKey("Admin")) {
			isAdmin = (Boolean) sessionScope.get("Admin");
		}
		
		canEditObject = isAdmin;

		addNew.setVisible(canEditObject);
		editCol.setVisible(canEditObject);
		deleteCol.setVisible(canEditObject);
	}
	
	public boolean getDisableAddNew() {
		if (canEditObject) {
			return getObject() == null
				|| getObject().getObjetStatut() == null
				|| getObject().getObjetStatut().getStatut().equals("ENCOURS")
				|| getObject().getObjetStatut().getStatut().equals("EPUISE");
				// || getObject().getObjetStatut().getStatut().equals("RESERVE");
		}
		return true;
	}
	
	/**
	 * Wrapper pour la methode d'ouverture de la modale formulaire 
	 * d'une sortie temporaire du le système de stockage.
	 * @param retour
	 * @param b
	 * @see AbstractController.openRetourFormModale
	 */
	private void openRetourFormModale(Retour retour, boolean b) {
		Hashtable<TKStockableObject, String> oldEmps = 
			new Hashtable<TKStockableObject, String>();
		oldEmps.put(getObject(), ObjectTypesFormatters.getEmplacementAdrl(
				getObject()));
		
		openRetourFormModale(retour, b, this, getObject(), 
							null, null, null, null, 
							null, null, null, null);
	}

	@Override
	public List<? extends TKdataObject> extractLastObjectsCreated() {
		return null;
	}

	public Grid getObjectsListGrid() {
		return objectsListGrid;
	}
	
	public Integer getAddedDelais() {
		Integer sum = 0;
		for (int i = 0; i < listObjects.size(); i++) {
			if (((RetourDecorator) listObjects.get(i)).getDelaiInMins() != null) {
				sum = sum + ((RetourDecorator) listObjects.get(i)).getDelaiInMins();
			}
		}
		if (sum > 0) {
			return sum;
		} else {
			return null;
		}
		
	}
}
