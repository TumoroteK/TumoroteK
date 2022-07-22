package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * @version 2.2.1-IRELEC
 * @author Mathieu BARTHELEMY
 *
 */
public abstract class AbstractFicheCombineStockageController extends AbstractFicheCombineController
{

	private static final long serialVersionUID = 1L;

	private Object stockageObj;

	private List<Incident> incidents = new ArrayList<>();
	private List<Incident> incidentsToRemove = new ArrayList<>();
	private List<IncidentDecorator> incidentsDecorated = new ArrayList<>();

	private String incidentsGroupHeader = Labels.getLabel("conteneur.incidents");

	protected Grid incidentsList;
	protected Grid incidentsListEdit;
	protected Group groupIncidents;

	protected Menuitem addIncidentItem;

	@Override
	public void doAfterCompose(final Component comp) throws Exception{
		super.doAfterCompose(comp);

		if(groupIncidents != null){
			groupIncidents.setOpen(false);
		}
	}

	public void initIncidents(final List<Incident> ics){

		getIncidents().clear();
		getIncidentsDecorated().clear();

		getIncidents().addAll(ics);

		setIncidentsDecorated(IncidentDecorator.decorateListe(getIncidents()));
		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("conteneur.incidents"));
		sb.append(" (");
		sb.append(getIncidentsDecorated().size());
		sb.append(")");
		incidentsGroupHeader = sb.toString();
	}

	public void clearIncidents(){
		getIncidents().clear();
		getIncidentsDecorated().clear();
		getIncidentsToRemove().clear();
		incidentsGroupHeader = Labels.getLabel("conteneur.incidents");
	}

	@Override
	public void setEmptyToNulls(){
		for(int i = 0; i < getIncidents().size(); i++){
			if(getIncidents().get(i).getDescription() != null && getIncidents().get(i).getDescription().equals("")){
				getIncidents().get(i).setDescription(null);
			}
		}
	}

	@Override
	public void updateObject(){
		for(int i = 0; i < getIncidentsToRemove().size(); i++){
			ManagerLocator.getIncidentManager().removeObjectManager(getIncidentsToRemove().get(i));
		}
	}

	/**
	 * Méthode appelée lors du clic sur le bouton addIncident. Elle
	 * va créer un nouvel incident et l'ajouter à la liste.
	 */
	public void onClick$addIncident(){

		final Incident ind = new Incident();

		if(getStockageObj() instanceof Conteneur){
			ind.setConteneur((Conteneur) getStockageObj());
		}else if(getStockageObj() instanceof Enceinte){
			ind.setEnceinte((Enceinte) getStockageObj());
		}else if(getStockageObj() instanceof Terminale){
			ind.setTerminale((Terminale) getStockageObj());
		}

		getIncidents().add(ind);

		// maj de la liste des labos
		final ListModel<Incident> list = new ListModelList<>(getIncidents());
		incidentsListEdit.setModel(list);

		final StringBuffer sb = new StringBuffer();
		sb.append(Labels.getLabel("conteneur.incidents"));
		sb.append(" (");
		sb.append(getIncidents().size());
		sb.append(")");
		incidentsGroupHeader = sb.toString();
		groupIncidents.setLabel(incidentsGroupHeader);

		//		String id = groupIncidents.getUuid();
		//		String idTop = panelChildrenWithScroll.getUuid();
		//		Clients.evalJavaScript("document.getElementById('" + idTop + "')" 
		//				+ ".scrollTop = document.getElementById('" + id + "')" 
		//				+ ".offsetTop;");
		Clients.scrollIntoView(groupIncidents);

	}

	public abstract void onClick$addIncidentItem();

	/**
	 * Méthode appelée après ajout d'un nouvel incident.
	 * @param event
	 */
	public void onGetAddedIncident(final Event event){
		if(event.getData() != null){
			getIncidents().add((Incident) event.getData());
			setIncidentsDecorated(IncidentDecorator.decorateListe(getIncidents()));

			// maj de la liste des labos
			final ListModel<IncidentDecorator> list = new ListModelList<>(getIncidentsDecorated());
			incidentsList.setModel(list);

			final ListModel<Incident> list2 = new ListModelList<>(getIncidents());
			incidentsListEdit.setModel(list2);
			getBinder().loadComponent(incidentsListEdit);

			final StringBuffer sb = new StringBuffer();
			sb.append(Labels.getLabel("conteneur.incidents"));
			sb.append(" (");
			sb.append(getIncidents().size());
			sb.append(")");
			incidentsGroupHeader = sb.toString();
			groupIncidents.setLabel(incidentsGroupHeader);
			groupIncidents.setOpen(true);

			//			String id = groupIncidents.getUuid();
			//			String idTop = panelChildrenWithScroll.getUuid();
			//			Clients.evalJavaScript("document.getElementById('" + idTop + "')" 
			//					+ ".scrollTop = document.getElementById('" + id + "')" 
			//					+ ".offsetTop;");
			Clients.scrollIntoView(groupIncidents);
		}
	}

	/**
	 * Cette méthode va supprimer un LaboInter de la liste.
	 * @param event Clic sur une image deleteIncident.
	 */
	public void onClick$deleteIncident(final Event event){
		// on demande confirmation à l'utilisateur
		// de supprimer l'incident
		if(Messagebox.show(
				ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel("message.deletion.incident")}),
				Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
			// on récupère l'incident que l'utilisateur veut
			// suppimer
			final Incident ind = (Incident) getBindingData((ForwardEvent) event);
			// on enlève le bao de la liste et on la met à jour
			getIncidents().remove(ind);

			final ListModel<Incident> list = new ListModelList<>(getIncidents());
			incidentsListEdit.setModel(list);

			// si l'incident existait dans la BDD on l'ajoute à la
			// liste des incidents à supprimer (il ne sera délété que
			// lors de la sauvegarde finale)
			if(ind.getIncidentId() != null){
				getIncidentsToRemove().add(ind);
			}
		}
	}

	/**
	 * Cette méthode va retourner l'incident contenu dans la ligne
	 * courante de la grid.
	 * @param event Event sur la grille contenant la liste des Incidents.
	 * @return Un Incident.
	 */
	public Object getBindingData(final ForwardEvent event){
		Component target = event.getOrigin().getTarget();
		try{
			while(!(target instanceof Row || target instanceof Listitem)){
				target = target.getParent();
			}
			final Map<?, ?> map = (Map<?, ?>) target.getAttribute("zkplus.databind.TEMPLATEMAP");
			return map.get(target.getAttribute("zkplus.databind.VARNAME"));
		}catch(final NullPointerException e){
			return null;
		}
	}

	/**
	 * @since 2.2.1 shortcut to prevent disableToolbar(false) from 
	 * AbstractFicheCombineStockageController.setObject
	 */
	@Override
	public void setObject(final TKdataObject obj){
		cloneObject();
		getBinder().loadComponent(self);
	}

	//TK-314
    /**
    * active / désactive les boutons édition/modification/ajout incident en fonction : 
    * - des droits de l'utilisateur
    * - de l'autorisation sur le conteneur passé en paramètre : la désactivation est forcée quelque soit les droits de l'utilisateur si :
    *    - le conteneur passé en paramètre n'appartient pas à la PF courante 
    *    - le stockage y est restreint à l'administrateur  
    *    - et l'utilisateur connecté n'est pas admin PF de la plateforme d'origine du conteneur
    * Convient aux spec Grenoble IRELEC.
    * @param conteneur Conteneur concerné
    * @return true si la désactivation est forcée
    * @since 2.2.1-IRELEC
    */
   public boolean disableActionsForForeignPlateformeConteneur(Conteneur conteneur) {
      boolean disable = !isAccessibleConteneurForCurrentPlateform(conteneur);
       
      if (editC !=null) {
         editC.setDisabled(disable || !isCanEdit());
      }
      if (deleteC != null) {
         deleteC.setDisabled(disable || !isCanDelete());
      }
      if (addIncidentItem != null) {
         addIncidentItem.setDisabled(disable || !isCanEdit());
      }   
    
      return disable;
   }	
	
	public List<Incident> getIncidents(){
		return incidents;
	}

	public void setIncidents(final List<Incident> is){
		this.incidents = is;
	}

	public List<Incident> getIncidentsToRemove(){
		return incidentsToRemove;
	}

	public void setIncidentsToRemove(final List<Incident> itr){
		this.incidentsToRemove = itr;
	}

	public List<IncidentDecorator> getIncidentsDecorated(){
		return incidentsDecorated;
	}

	public void setIncidentsDecorated(final List<IncidentDecorator> icd){
		this.incidentsDecorated = icd;
	}

	public Object getStockageObj(){
		return stockageObj;
	}

	public void setStockageObj(final Object s){
		this.stockageObj = s;
	}

	public String getIncidentsGroupHeader(){
		return incidentsGroupHeader;
	}
}
