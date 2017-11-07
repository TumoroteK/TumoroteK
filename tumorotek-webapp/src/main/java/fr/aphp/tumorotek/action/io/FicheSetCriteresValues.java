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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.export.CritereNode;
import fr.aphp.tumorotek.webapp.tree.export.ExportNode;
import fr.aphp.tumorotek.webapp.tree.export.GroupementNode;

/**
 * Classe permettant à l'utilisateur de saisir les valeurs des critères
 * et d'exécuter la requête.
 * @author pierre
 *
 */
public class FicheSetCriteresValues extends GenericForwardComposer<Component> {
	
	private Log log = LogFactory.getLog(FicheSetCriteresValues.class);

	private static final long serialVersionUID = 6152666419852522833L;
	
	private Panel winPanel;
	private Component parent;
	private Row rowOneCollection;
	private Row rowToutesCollections;
	private Row rowSelectionCollections;
	private Label labelOneCollection;
	private Label labelToutesCollections;
	private Listbox banquesBox;
	private Html presentationLabel;
	
	private List<Banque> banques = new ArrayList<Banque>();
	private List<Banque> availableBanques = new ArrayList<Banque>();
	private Recherche recherche;
	private AnnotateDataBinder binder;
	private Grid exportNodesGrid;
	private List<ExportNode> exportNodes = new ArrayList<ExportNode>();
	private Hashtable<Integer, Object> criteresValues = 
		new Hashtable<Integer, Object>();
	private Set<Listitem> selectedBanquesItem = new HashSet<Listitem>();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		binder = new AnnotateDataBinder(comp);	
		binder.loadComponent(comp);
		
		if (winPanel != null) {
			winPanel.setHeight("465px");
		}
	}
	
	public void init(List<Banque> bks, Component p,
			Recherche r) {
		parent = p;
		banques = bks;
		recherche = r;
		
		// création de l'arbre de la requête
		GroupementNode node = GroupementNode
			.convertFromGroupement(recherche.getRequete()
					.getGroupementRacine(), null);
		exportNodes = node.getExportNodeList();
		this.exportNodesGrid.setModel(new SimpleListModel(exportNodes));
		
		// init des banques disponibles
		if (banques.size() == 1) {
			Utilisateur user = SessionUtils
				.getLoggedUser(sessionScope);
			Plateforme pf = SessionUtils.getPlateforme(sessionScope);
			availableBanques = ManagerLocator.getUtilisateurManager()
				.getAvailableBanquesByPlateformeManager(
					user, pf);
			availableBanques.remove(banques.get(0));
			
			rowToutesCollections.setVisible(false);
			rowOneCollection.setVisible(true);
			rowSelectionCollections.setVisible(true);
			labelOneCollection.setValue(ObjectTypesFormatters.getLabel(
					"execution.recherche.liste.banques.1", 
					new String[] {banques.get(0).getNom()}));
		} else {
			rowToutesCollections.setVisible(true);
			rowOneCollection.setVisible(false);
			rowSelectionCollections.setVisible(false);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < banques.size(); i++) {
				sb.append(banques.get(i).getNom());
				if (i < banques.size() - 1) {
					sb.append(", ");
				}
			}
			labelToutesCollections.setValue(ObjectTypesFormatters.getLabel(
					"execution.recherche.liste.banques.2", 
					new String[] {sb.toString()}));
		}
		
		StringBuffer bksNom = new StringBuffer();
		List<Banque> tmp = ManagerLocator.getRechercheManager()
			.findBanquesManager(recherche);
		if (tmp.size() > 1) {
			bksNom.append(Labels.getLabel("execution.recherche.banques"));
		} else {
			bksNom.append(Labels.getLabel("execution.recherche.banque"));
		}
		bksNom.append(" ");
		for (int i = 0; i < tmp.size(); i++) {
			bksNom.append(tmp.get(i).getNom());
			
			if (i + 1 < tmp.size()) {
				bksNom.append(", ");
			}
		}
		presentationLabel.setContent(ObjectTypesFormatters.getLabel(
				"execution.recherche.presentation", 
				new String[]{recherche.getIntitule(),
						bksNom.toString()}));
		
		getBinder().loadComponent(banquesBox);
	}
	
	public void onClick$cancel() {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	/**
	 * Lance la recherche.
	 */
	public void onClick$execute() {
		boolean ok = true;
		
		// pour chaque noeud de la requete
		for (int i = 0; i < exportNodes.size(); i++) {
			// si c'est un critere
			if (exportNodes.get(i) instanceof CritereNode) {
				// on récupère le critère et la valeur saisie
				Critere crit = ((CritereNode) exportNodes.get(i)).getCritere();
				Object value = null;
				
				if (crit.getOperateur().equals("is null")) {
					value = "";
				} else {
					value = exportNodes.get(i).getCritereValue();
				}
				
				if (value == null) {
					ok = false;
				} else {
					if (crit.getOperateur().equals("like")
							|| crit.getOperateur().equals("not like")) {
						String tmp = (String) value;
						if (!tmp.endsWith("%")) {
							tmp = tmp + "%";
						}
						if (!tmp.startsWith("%")) {
							tmp = "%" + tmp;
						}
						value = tmp;
					}
					criteresValues.put(crit.getCritereId(), value);
				}
			}
		}
		
		// si certaines valeurs ne sont pas saisies => erreur!!
		if (!ok) {
			Messagebox.show(
				Labels.getLabel("critere.value.null"), 
				"Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			Clients.showBusy(Labels.getLabel(
					"recherche.execution.encours"));
			Events.echoEvent("onLaterExecuteSearch", self, null);
		}
	}
	
	/**
	 * Execution de la recherche.
	 */
	public void onLaterExecuteSearch() {
		banques.addAll(findSelectedBanques());
		
		String jdbcDialect = null;
		try {
			jdbcDialect = SessionUtils.getDbms();
		} catch (NamingException e) {
			jdbcDialect = "inconnu";
		}
		
		List<Object> objets = ManagerLocator.getTraitementRequeteManager()
			.traitementRequeteManager(recherche.getRequete(), 
				banques, criteresValues, jdbcDialect);
		
		// on renvoie les résultats
		Events.postEvent("onGetSearchResults", getParent(), objets);
		// ferme wait message
		Clients.clearBusy();
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	/**
	 * Retourne les banques sélectionnées.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Banque> findSelectedBanques() {
		List<Banque> bks = new ArrayList<Banque>();
		Iterator<Listitem> its = banquesBox.getSelectedItems().iterator();
		while (its.hasNext()) {
			bks.add(availableBanques.get(
					banquesBox.getItems().indexOf(its.next())));
		}
		return bks;
	}
	
	/****************************************************/
	/************     GETTERS et SETTERS     ************/
	/****************************************************/

	public Panel getWinPanel() {
		return winPanel;
	}
	public void setWinPanel(Panel p) {
		this.winPanel = p;
	}
	public Component getParent() {
		return parent;
	}
	public void setParent(Component p) {
		this.parent = p;
	}
	public List<Banque> getBanques() {
		return banques;
	}
	public void setBanques(List<Banque> b) {
		this.banques = b;
	}
	public Recherche getRecherche() {
		return recherche;
	}
	public void setRecherche(Recherche r) {
		this.recherche = r;
	}

	public List<ExportNode> getExportNodes() {
		return exportNodes;
	}

	public void setExportNodes(List<ExportNode> e) {
		this.exportNodes = e;
	}

	public Hashtable<Integer, Object> getCriteresValues() {
		return criteresValues;
	}

	public void setCriteresValues(Hashtable<Integer, Object> cValues) {
		this.criteresValues = cValues;
	}

	public List<Banque> getAvailableBanques() {
		return availableBanques;
	}

	public void setAvailableBanques(List<Banque> bks) {
		this.availableBanques = bks;
	}

	public Set<Listitem> getSelectedBanquesItem() {
		return selectedBanquesItem;
	}

	public void setSelectedBanquesItem(Set<Listitem> selectedItem) {
		this.selectedBanquesItem = selectedItem;
	}

	public AnnotateDataBinder getBinder() {
		return binder;
	}

	public void setBinder(AnnotateDataBinder b) {
		this.binder = b;
	}
}
