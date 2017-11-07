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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.ListUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Group;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.West;
import org.zkoss.zul.ext.Selectable;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.NumberInputElement;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.recherche.historique.SearchHistory;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.AnnotationItemRenderer;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * Abstract classe contenant methodes recherches avancees.
 * 
 * @author Pierre VENTADOUR
 * @version 2.0.13
 * 
 *          modifiée Septembre 2012 Julien Husson
 * 
 */
public abstract class AbstractFicheRechercheAvancee extends
		AbstractFicheCombineController {

	private static final long serialVersionUID = 1L;

	// private Log log = LogFactory.getLog(AbstractFicheRechercheAvancee.class);

	protected Rows rechercheRows;
	// Groupes
	protected Group groupAnnotations;

	/**
	 * Objets principaux.
	 */
	protected Entite entiteToSearch;
	protected String pathToRespond;
	protected boolean oneValueEntered = false;
	protected boolean otherQuery = false;
	protected boolean searchForProdDerives = false;
	protected List<Object> resultats = new ArrayList<Object>();
	protected List<Integer> resultatsIds = new ArrayList<Integer>();
	// protected List<String>
	protected boolean annotationAlreadyOpen = false;
	protected Champ parent1ToQueryProdDerive;
	protected Champ parent2ToQueryProdDerive;

	protected List<Critere> criteresStandards = new ArrayList<Critere>();
	protected List<Object> valeursStandards = new ArrayList<Object>();
	protected List<Critere> criteresDerives1 = new ArrayList<Critere>();
	protected List<Object> valeursDerives1 = new ArrayList<Object>();
	protected List<Critere> criteresDerives2 = new ArrayList<Critere>();
	protected List<Object> valeursDerives2 = new ArrayList<Object>();

	protected List<String> operateursDecimaux = new ArrayList<String>();
	protected List<String> operateursDates = new ArrayList<String>();

	private List<RechercheCompValues> usedComponents = new ArrayList<RechercheCompValues>();
	private List<RechercheCompValues> usedAnnoComponents = new ArrayList<RechercheCompValues>();

	/**
	 * Listes de components.
	 */
	protected Component[] objPatientComponents;
	protected Component[] objMaladieComponents;
	protected Component[] objPrelevementComponents;
	protected Component[] objPrelevementContextComponents;
	protected Component[] objEchantillonComponents;
	protected Component[] objProdDeriveComponents;
	protected Component[] objAnonymeComponents;
	// objects recherche cession
	protected Component[] objCessionComponents;
	// objects recherche INCa
	protected List<Component> objAnnotationsComponent = new ArrayList<Component>();
	// objects operateurs
	protected Component[] objOperateurs;
	protected List<Component> annoOperateurs = new ArrayList<Component>();

	// private String catalogueExport = null;

	/**
	 * Search history
	 * 
	 */
	protected SearchHistory searchHistory;
	protected List<SearchHistory> searchHistoryList;
	protected ListModelList<SearchHistory> itemSearchHistoryListbox;
	protected West panelSearchHistorique;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		if (winPanel != null) {
			winPanel.setHeight(getMainWindow().getPanelHeight() - 10 + "px");
			((Borderlayout) winPanel.getFellow("bLayout"))
				.setHeight(getMainWindow().getPanelHeight() - 40 + "px");
		}
	}

	/**
	 * Exécute les requêtes avec des critères sur les champs des annotations.
	 * @since 2.0.10 application des opérateurs numériques aux champs annotation type numérique
	 */
	@SuppressWarnings("unchecked")
	public void executeQueriesForAnnotations() {
		
		RechercheCompValues rcv;
		
		// pour chaque champ interrogeable pour les annotations
		for (int i = 0; i < objAnnotationsComponent.size(); i++) {
			
			rcv = new RechercheCompValues();
			
			// si c'est un textbox
			if (objAnnotationsComponent.get(i).getClass().getSimpleName()
					.equals("Textbox")) {
				Textbox current = (Textbox) objAnnotationsComponent.get(i);
				// si une valeur a été saisie
				if (current.getValue() != null
						&& !current.getValue().equals("")) {
					// exécution de la requête
					executeSimpleQueryForTextbox(current, null, null,
							oneValueEntered, false);

					oneValueEntered = true;
					rcv.setCompClass(Textbox.class);
					rcv.setCompId(current.getId());
					rcv.setTextValue(current.getText());
				}
			} else if (objAnnotationsComponent.get(i).getClass().getSimpleName()
					.equals("Listbox")) {
				Listbox current = (Listbox) objAnnotationsComponent.get(i);
				if (!current.isMultiple()) {
					// si une valeur a été saisie
					if ((current.getModel() != null && 
							!((Selectable<Object>) current.getModel()).isSelectionEmpty())
						|| current.getSelectedIndex() > 0) {
						if (!current.hasAttribute("fileBox")) {
							// exécution de la requête
							executeSimpleQueryForListbox(current, null, null,
									oneValueEntered, null);
							rcv.setCompClass(Listbox.class);
							rcv.setCompId(current.getId());
							rcv.setSelectedIndexValue(current.getSelectedIndex());
							if (current.getModel() != null) {
								rcv.getSelectedValues()
									.addAll(((Selectable<Object>) current.getModel()).getSelection());
							}
						} else {
							rcv = executeFileQuery(current, getEntiteToSearch(), 
								(Boolean) current.getSelectedItem().getValue());
						}
	
						oneValueEntered = true;
					}
				} else { // thesaurusM since 2.0.13
					if (!((Selectable<Object>) 
							((Listbox) current).getModel()).getSelection().isEmpty()) {
					
						// exécution de la requête
						List<Object> items = new ArrayList<Object>();
						for (Object it : ((Selectable<Object>) 
								((Listbox) current).getModel()).getSelection()) {
							items.add((Item) it);
						}
						
						boolean cumulative = ((Checkbox) current.getNextSibling()).isChecked();
						executeListQuery((String) current.getAttribute("chpId"), 
								"ChampAnnotation", entiteToSearch, null,
								oneValueEntered, items, cumulative);
						
						rcv.setCompClass(Listbox.class);
						rcv.setCompId(current.getId());
						rcv.getSelectedValues().addAll(items);
						rcv.setCumulative(cumulative);
						getUsedComponents().add(rcv);
						
						oneValueEntered = true;
					}
				}
			} else if (objAnnotationsComponent.get(i).getClass().getSimpleName()
					.equals("Datebox")) {
				Datebox current = (Datebox) objAnnotationsComponent.get(i);
				// si une valeur a été saisie
				if (current.getValue() != null 
						&& ((Listbox) current.getPreviousSibling())
						.getSelectedIndex() > -1) {

					String operateur = getOperateursDecimaux()
							.get(((Listbox) current.getPreviousSibling())
									.getSelectedIndex());
					
					executeSimpleQueryForDatebox(current, null, null,
							oneValueEntered, operateur, true);

					oneValueEntered = true;
					rcv.setCompClass(Datebox.class);
					rcv.setCompId(current.getId());
					rcv.setTextValue(current.getText());
				}
			} else if (objAnnotationsComponent.get(i).getClass().getSimpleName()
					.equals("CalendarBox")) {
				CalendarBox current = (CalendarBox) objAnnotationsComponent.get(i);
				// si une valeur a été saisie
				if (current.getValue() != null 
						&& ((Listbox) current.getPreviousSibling())
						.getSelectedIndex() > -1) {

					String operateur = getOperateursDecimaux()
							.get(((Listbox) current.getPreviousSibling())
									.getSelectedIndex());
					
					executeSimpleQueryForCalendarbox(current, null, null,
							oneValueEntered, operateur);

					oneValueEntered = true;
					rcv.setCompClass(CalendarBox.class);
					rcv.setCompId(current.getId());
					rcv.setCalendarValue(current.getValue());
				}
			} else if (objAnnotationsComponent.get(i).getClass().getSimpleName()
					.equals("Decimalbox")) {
				Decimalbox current = (Decimalbox) objAnnotationsComponent.get(i);
				// si une valeur a été saisie
				if (current.getValue() != null 
						&& ((Listbox) current.getPreviousSibling())
						.getSelectedIndex() > -1) {
					
					String operateur = getOperateursDecimaux()
							.get(((Listbox) current.getPreviousSibling()).getSelectedIndex());

					// exécution de la requête
					executeSimpleQueryForDecimalbox(current, null, null,
							oneValueEntered, operateur, true);

					oneValueEntered = true;
					rcv.setCompClass(Decimalbox.class);
					rcv.setCompId(current.getId());
					rcv.setTextValue(current.getText());

				}
			}
			
			if (rcv.getCompId() != null) {
				getUsedAnnoComponents().add(rcv);
			}
		}
	}

	/**
	 * Crée le critère pour la requête à exécuter.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent
	 *            Parent pour accéder à l'objet à requeter.
	 * @param operateur
	 *            Opérateur de la requête.
	 * @return Critere de la requête.
	 */
	public Critere createCritereForQuery(Component current, Champ parent,
			String operateur) {
		Champ champ = null;
		// si le component correspond à une annotation
		if (current.getAttribute("champ") != null) {
			ChampAnnotation anno = (ChampAnnotation) current
					.getAttribute("champ");
			champ = new Champ();
			champ.setChampAnnotation(anno);
			if (parent != null) {
				champ.setChampParent(parent);
			}
		} else {
			// on récup l'attribut et l'entité du composant pour extraire
			// le ChampEntite correspondant
			String attribut = (String) current.getAttribute("attribut");
			String nomEntite = (String) current.getAttribute("entite");
			Entite entite = ManagerLocator.getEntiteManager()
					.findByNomManager(nomEntite).get(0);
			ChampEntite champEntite = ManagerLocator.getChampEntiteManager()
					.findByEntiteAndNomManager(entite, attribut).get(0);

			// création du champ avec ajout du parent si nécessaire
			champ = new Champ(champEntite);
			if (parent != null) {
				champ.setChampParent(parent);
			}
		}

		return new Critere(champ, operateur, null);
	}

	/**
	 * Exécute une requête simple : sur un code, une listbox simple, un
	 * decimalbox....
	 * 
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param first
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param critere
	 *            Critère de la requête.
	 * @param value
	 *            Valeur du critère.
	 * @return La liste de résultats mise à jour.
	 */
	public void executeSimpleQuery(boolean firstQuery, Critere critere,
			Object value) {
		criteresStandards.add(critere);
		valeursStandards.add(value);
	}

	/**
	 * Exécute une requête simple sur un textbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @return
	 */
	public void executeSimpleQueryForTextbox(Textbox current, Champ parent1,
			Champ parent2, boolean firstQuery, boolean contain) {
		String value = "";
		if (contain) {
			value = "%" + current.getValue() + "%";
		} else {
			value = current.getValue() + "%";
		}

		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, "like");

			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, value);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, value, "like", parent1,
					parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Textbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un listbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param prop
	 *            pour extraire un membre de l'objet a passer en critère au lieu
	 *            de l'objet lui même.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void executeSimpleQueryForListbox(Listbox current, Champ parent1,
			Champ parent2, boolean firstQuery, String prop) {

		Object obj = null;
		// on récupère l'objet sélectionné
		if (current.getListModel() != null) {
//			obj = current.getListModel().getElementAt(
//					current.getSelectedIndex());
			obj = ((Selectable<Object>) current.getListModel()).getSelection().iterator().next();
		} else {
			obj = current.getSelectedItem().getValue();
		}
		
		if (current.getId().matches(".*BoolBox")) {
			obj = new Boolean((String) obj);
		}

		if (prop != null) {
			try {
				obj = PropertyUtils.getSimpleProperty(obj, prop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, "=");
			// exécution de lam2 requête
			executeSimpleQuery(firstQuery, critere, obj);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, obj, "=", parent1,
					parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Listbox.class);
		rcv.setCompId(current.getId());
		rcv.getSelectedValues().add(obj);
		rcv.setSelectedIndexValue(current.getSelectedIndex());
		getUsedComponents().add(rcv);
	}
	
	/**
	 * Exécute une requête simple sur un listbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param prop
	 *            pour extraire un membre de l'objet a passer en critère au lieu
	 *            de l'objet lui même.
	 * @return
	 */
	public void executeSimpleQueryForCombobox(Combobox current, Champ parent1,
			Champ parent2, boolean firstQuery, String prop) {

		Object obj = null;
		// on récupère l'objet sélectionné
		if (current.getModel() != null) {
			obj = current.getModel().getElementAt(
					current.getSelectedIndex());
		} else {
			obj = current.getSelectedItem().getValue();
		}
		
		if (current.getId().matches(".*BoolBox")) {
			obj = new Boolean((String) obj);
		}

		if (prop != null) {
			try {
				obj = PropertyUtils.getSimpleProperty(obj, prop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, "=");
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, obj);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, obj, "=", parent1,
					parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Combobox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un listbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @return
	 */
	public void executeSimpleQueryForListboxWithSpecialValue(Listbox current,
			Champ parent1, Champ parent2, Object value, boolean firstQuery) {

		if (!searchForProdDerives) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, "=");
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, value);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, value, "=", parent1,
					parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Listbox.class);
		rcv.setCompId(current.getId());
		rcv.setSelectedIndexValue(current.getSelectedIndex());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un decimalbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param operateur
	 *            Operateur de la requête.
	 * @return
	 */
	public void executeSimpleQueryForDecimalbox(Decimalbox current,
			Champ parent1, Champ parent2, boolean firstQuery, String operateur, boolean isBigDecimal) {
		// on récupère la valeur saisie
		BigDecimal val = current.getValue();
		Number obj;
		if (!isBigDecimal) {
			obj = current.getValue().floatValue();
		} else {
			obj = val;
		}
		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, operateur);
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, obj);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, obj, operateur,
					parent1, parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Decimalbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un intbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param operateur
	 *            Operateur de la requête.
	 * @return
	 */
	public void executeSimpleQueryForIntbox(Intbox current, Champ parent1,
			Champ parent2, boolean firstQuery, String operateur) {
		// on récupère la valeur saisie
		Integer obj = current.getValue();
		if (!searchForProdDerives) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, operateur);
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, obj);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, obj, operateur,
					parent1, parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Intbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un decimalbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param operateur
	 *            Operateur de la requête.
	 * @since 2.0.10 permet de faire une requête exacte et avec les opérateurs numériques.
	 * @version 2.0.10
	 * @return
	 */
	public void executeSimpleQueryForDoublebox(NumberInputElement current,
			Champ parent1, Champ parent2, boolean firstQuery, String operateur) {
		// on récupère la valeur saisie
		// Float obj = current.getText().floatValue();
		// String val = String.valueOf(obj);
		String val = current.getText();
		// if (val.contains(".")) {
		// 	val = val.substring(0, val.indexOf("."));
		//	val = val.concat("%");
		// }
		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, operateur);
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, val);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, val, operateur,
					parent1, parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Doublebox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un datebox.
	 * 
	 * @param current Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param operateur
	 *            Operateur de la requête.
	 * @return
	 */
	public void executeSimpleQueryForDatebox(Datebox current, Champ parent1,
			Champ parent2, boolean firstQuery, String operateur, boolean isCalendar) {
		// on récupère la valeur saisie
		Date tmp = current.getValue();
		Object obj = null;

		if (isCalendar) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(tmp);
			obj = cal;
		} else {
			obj = tmp;
		}
		
		

		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, operateur);
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, obj);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, obj, operateur,
					parent1, parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Datebox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}
	
	/**
	 * Exécute une requête simple sur un CalendarBox. Se comporte comme une date 
	 * si les heures et les minutes ne sont pas renseignées
	 * 
	 * @param current Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param operateur
	 *            Operateur de la requête.
	 * @return
	 */
	public void executeSimpleQueryForCalendarbox(CalendarBox current, Champ parent1,
			Champ parent2, boolean firstQuery, String operateur) {
		// on récupère la valeur saisie
		Calendar value = current.getValue();
		
		// Grenoble's Gabriel report SEPAGES date interval sup
		// considère la date YYYY-MM-DD 00:00:00 comme limite sup 
		// exclusive (la veille) car prend en compte les heures:minutes
		if (operateur.equals("<=") && value.get(Calendar.HOUR) == 0 
				&& value.get(Calendar.MINUTE) == 0 
				&& value.get(Calendar.SECOND) == 0) {
			value.add(Calendar.DATE, 1);
		}

		if (!searchForProdDerives || current.getId().matches("annoBox.*")) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, operateur);
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, value);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, value, operateur,
					parent1, parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(CalendarBox.class);
		rcv.setCompId(current.getId());
		rcv.setCalendarValue(current.getValue());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un textbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param parent1
	 *            1er Parent pour accéder à l'objet à requeter.
	 * @param parent2
	 *            2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @return
	 */
	public void executeSimpleQueryForCheckbox(Checkbox current, Champ parent1,
			Champ parent2, boolean firstQuery) {
		boolean value = current.isChecked();

		if (!searchForProdDerives) {
			// création du critère
			Critere critere = createCritereForQuery(current, parent1, "=");

			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, value);
		} else {
			// requête spéciale pour les dérivés
			prepareAndExecuteQueyForDerives(firstQuery, value, "=", parent1,
					parent2, current);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Checkbox.class);
		rcv.setCompId(current.getId());
		rcv.setCheckedValue(current.isChecked());
		getUsedComponents().add(rcv);
	}

	/**
	 * Prépare la requête permettant d'interroger les produits dérivés.
	 * 
	 * @param first
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param value
	 *            Valeur du critère.
	 * @param operateur
	 *            Opérateur de la requête.
	 * @param parent1
	 *            1er parent possible.
	 * @param parent2
	 *            2eme parent possible.
	 * @param current
	 *            Component courant.
	 */
	public void prepareAndExecuteQueyForDerives(boolean firstQuery,
			Object value, String operateur, Champ parent1, Champ parent2,
			Component current) {
		Critere critere1 = null;
		Critere critere2 = null;

		// création des 2 critères
		critere1 = createCritereForQuery(current, parent1, operateur);

		if (parent2 != null) {
			critere2 = createCritereForQuery(current, parent2, operateur);
		}

		// execution de la requête
		executeSimpleQueryForDerives(firstQuery, critere1, critere2, value);
	}

	/**
	 * Exécute une requête simple sur un textbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 */
	public void executeDeriveQueryForTextbox(Textbox current, boolean firstQuery) {

		String value = current.getValue() + "%";
		if (!searchForProdDerives) {
			// création des 2 critères
			Critere critere1 = createCritereForQuery(current,
					parent1ToQueryProdDerive, "like");

			Critere critere2 = null;
			if (parent2ToQueryProdDerive != null) {
				critere2 = createCritereForQuery(current,
						parent2ToQueryProdDerive, "like");
			}

			executeSimpleQueryForDerives(firstQuery, critere1, critere2, value);

		} else {
			// création du critère
			Critere critere = createCritereForQuery(current, null, "like");

			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, value);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Textbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple sur un listbox (pour un dérivé).
	 * 
	 * @param current
	 *            Component courant.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void executeDeriveQueryForListbox(Listbox current, boolean firstQuery) {

		Object obj = null;
		// on récupère l'objet sélectionné
		if (current.getListModel() != null) {
//			obj = current.getListModel().getElementAt(
//					current.getSelectedIndex());
			obj = ((Selectable<Object>) current.getListModel()).getSelection().iterator().next();
		} else {
			obj = current.getSelectedItem().getValue();
		}
		
		if (current.getId().matches(".*BoolBox")) {
			obj = new Boolean((String) obj);
		}
		
		if (!searchForProdDerives) {
			// création des 2 critères
			Critere critere1 = createCritereForQuery(current,
					parent1ToQueryProdDerive, "=");

			Critere critere2 = null;
			if (parent2ToQueryProdDerive != null) {
				critere2 = createCritereForQuery(current,
						parent2ToQueryProdDerive, "=");
			}

			executeSimpleQueryForDerives(firstQuery, critere1, critere2, obj);

		} else {
			// création du critère
			Critere critere = createCritereForQuery(current, null, "=");
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, obj);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Listbox.class);
		rcv.setCompId(current.getId());
		rcv.setSelectedIndexValue(current.getSelectedIndex());
		rcv.getSelectedValues().add(obj);
		getUsedComponents().add(rcv);
	}
	
	/**
	 * Exécute une requête simple sur un decimalbox.
	 * 
	 * @param attribut membre a rechercher OU id champAnnotation
	 * @param entite a rechercher OU ChampAnnotation
	 * @param parent1 1er Parent pour accéder à l'objet à requeter.
	 * @param parent2 2eme Parent pour accéder à l'objet à requeter.
	 * @param resultats Liste de résultats actuellement trouvés.
	 * @param firstQuery True si c'est la 1ère requête que l'on exécute.
	 * @param operateur Operateur de la requête.
	 * @param cumulative boolean si true chaque élément de la liste devient un critère de recherche 
	 * combiné aux autres par AND, OR si false.
	 * @version 2.0.13
	 */
	@SuppressWarnings("unchecked")
	public void executeListQuery(String attribut, String nomEntite,
			Entite fromEntite, Champ parent1, boolean firstQuery, List<Object> objs, 
			boolean cumulative) {
		
		Champ champ = null;
		if (!nomEntite.equals("ChampAnnotation")) {

			Entite entite = ManagerLocator.getEntiteManager()
					.findByNomManager(nomEntite).get(0);
			ChampEntite champEntite = ManagerLocator.getChampEntiteManager()
					.findByEntiteAndNomManager(entite, attribut).get(0);
	
			// création du champ avec ajout du parent si nécessaire
			champ = new Champ(champEntite);
			if (parent1 != null) {
				champ.setChampParent(parent1);
			}
		} else {
			ChampAnnotation chpA = ManagerLocator
					.getChampAnnotationManager()
					.findByIdManager(Integer.valueOf(attribut));
			champ = new Champ(chpA);
		}

		Critere critere = new Critere(champ, "=", null);

		// exécution de la requête
		// on récupère la ou les banques sélectionnée(s)
		List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);

		// requete ids 
		List<Integer> ids = ManagerLocator.getTraitementQueryManager()
				.findObjetByCritereInListWithBanquesManager(critere,
						banques, objs, cumulative);
		
		// target Entite
		if (fromEntite != null && entiteToSearch != null && 
							!fromEntite.equals(entiteToSearch)) {
			ids = ManagerLocator.getCorrespondanceIdManager()
					.findTargetIdsFromIdsManager(ids, fromEntite, 
							entiteToSearch, banques, true);
		}

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if (!otherQuery) {
			resultatsIds = ids;
		} else {
			resultatsIds = ListUtils.intersection(resultatsIds, ids);
		}
		otherQuery = true;
	}

	/**
	 * Exécute une requête simple sur un decimalbox.
	 * 
	 * @param current
	 *            Component courant.
	 * @param firstQuery
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param operateur
	 *            Operateur de la requête.
	 * @return
	 */
	public void executeDeriveQueryForDecimalbox(Decimalbox current,
			boolean firstQuery, String operateur) {

		// on récupère la valeur saisie
		Float obj = current.getValue().floatValue();
		if (!searchForProdDerives) {
			// création des 2 critères
			Critere critere1 = createCritereForQuery(current,
					parent1ToQueryProdDerive, operateur);

			Critere critere2 = null;
			if (parent2ToQueryProdDerive != null) {
				critere2 = createCritereForQuery(current,
						parent2ToQueryProdDerive, operateur);
			}

			executeSimpleQueryForDerives(firstQuery, critere1, critere2, obj);

		} else {
			// création du critère
			Critere critere = createCritereForQuery(current, null, operateur);
			// exécution de la requête
			executeSimpleQuery(firstQuery, critere, obj);
		}
		
		// ajout du composant à l'historique
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Decimalbox.class);
		rcv.setCompId(current.getId());
		rcv.setTextValue(current.getText());
		getUsedComponents().add(rcv);
	}

	/**
	 * Exécute une requête simple pour les dérivés : sur un code, une listbox
	 * simple, un decimalbox....
	 * 
	 * @param resultats
	 *            Liste de résultats actuellement trouvés.
	 * @param first
	 *            True si c'est la 1ère requête que l'on exécute.
	 * @param critere1
	 *            1er Critère d'accès aux dérivés.
	 * @param critere2
	 *            2ème Critère d'accès aux dérivés.
	 * @param value
	 *            Valeur du critère.
	 * @return La liste de résultats mise à jour.
	 */
	public void executeSimpleQueryForDerives(boolean firstQuery,
			Critere critere1, Critere critere2, Object value) {
		criteresDerives1.add(critere1);
		criteresDerives2.add(critere2);
		valeursDerives1.add(value);
		valeursDerives2.add(value);
	}
	
	@SuppressWarnings("unchecked")
	public RechercheCompValues executeFileQuery(Listbox current, Entite target, Boolean empty) {

		// on récupère la ou les banques sélectionnée(s)
		List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);
		
		Champ fileChp = new Champ();
		if (current.getAttribute("champ") != null) {
			ChampAnnotation anno = (ChampAnnotation) current
					.getAttribute("champ");
			fileChp.setChampAnnotation(anno);
		} else {
			String attribut = (String) current.getAttribute("attribut");
			String nomEntite = (String) current.getAttribute("entite");
			Entite entite = ManagerLocator.getEntiteManager()
					.findByNomManager(nomEntite).get(0);
			ChampEntite champEntite = ManagerLocator.getChampEntiteManager()
					.findByEntiteAndNomManager(entite, attribut).get(0);
			fileChp.setChampEntite(champEntite);
		}

		// si c'est la 1ère requête les résultats vont directement
		// dans la liste
		if (!otherQuery) {
			resultatsIds = ManagerLocator.getTraitementQueryManager()
					.findFileUploadedManager(fileChp, target, banques, empty);
		} else {
			// sinon on fait une intersection des résultats de la
			// requête avec ceux déjà trouvés
			List<Integer> objects = ManagerLocator.getTraitementQueryManager()
					.findFileUploadedManager(fileChp, target, banques, empty);

			resultatsIds = ListUtils.intersection(resultatsIds, objects);
		}
		otherQuery = true;
		RechercheCompValues rcv = new RechercheCompValues();
		rcv.setCompClass(Listbox.class);
		rcv.setCompId(current.getId());
		rcv.setSelectedIndexValue(current.getSelectedIndex());
		getUsedComponents().add(rcv);
		return rcv;
	}

	/*************************************************************************/
	/********************* GESTION DES RESULTATS *****************************/
	/*************************************************************************/

	public void onDoCancel() {
		setOneValueEntered(false);
		otherQuery = false;
		setCriteresStandards(new ArrayList<Critere>());
		setCriteresDerives1(new ArrayList<Critere>());
		setCriteresDerives2(new ArrayList<Critere>());
		setValeursStandards(new ArrayList<Object>());
		setValeursDerives1(new ArrayList<Object>());
		setValeursDerives2(new ArrayList<Object>());
	}

	public void onDoExport(Event e) {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
		
		getObjectTabController().getListe().setResultatsIds(getResultatsIds());
		
		if (e.getData() == null) {
			getObjectTabController().getListe().onLaterExport(false);
		} else if (e.getData().equals("TVGSO")) {
			getObjectTabController().getListe().onDoExportTVGSO(false);
		} else if (e.getData().equals("TVGSOcsv")) {
			getObjectTabController().getListe().onDoExportTVGSO(true);
		} else if (e.getData().equals("INCa")) {
			getObjectTabController().getListe().onDoExportINCa();
		} else if (e.getData().equals("BIOCAP")) {
			getObjectTabController().getListe().onDoExportBIOCAP();
		}
	}

	public void onShowResults() {
		Clients.showBusy(Labels.getLabel("recherche.avancee.affichage.wait"));
		Events.echoEvent("onLaterShowResults", self, null);
	}
	
	/**
	 * Evenement relayant la sélection d'un trop grand nombre de 
	 * résultats (envoyé depuis ResultatsModale)
	 */
	public void onDoSelect(Event e) {
		Clients.showBusy(Labels.getLabel("cession.select.wait"));
		Events.echoEvent("onLaterCessionSelect", self, null);
	}
	
	public void onLaterCessionSelect() {
		getObjectTabController().getListe().setResultatsIds(getResultatsIds());

		getObjectTabController().getListe().onSelectFromResultatModale();
		
		Clients.clearBusy();
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	public void onDoBatchDelete(Event e) {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
		
		// Event eIds = new Event("onDeleteIdsFromModaleEvent", 
		//		getObjectTabController().getListe().getSelfComponent(), 
		//		getResultatsIds());
		
		// getObjectTabController().getListe().deleteIdsFromModaleEvent(eIds);
		Events.echoEvent("onDeleteIdsFromModaleEvent", 
				getObjectTabController().getListe().getSelfComponent(), 
				getResultatsIds());
	}
	
	/**
	 * Evenement relayant l'envoi vers une nouvelle cession 
	 * d'un trop grand nombre de résultats (envoyé depuis ResultatsModale)
	 */
	public void onDoNewCession(Event e) {
		Clients.showBusy(Labels.getLabel("cession.select.wait"));
		Events.echoEvent("onLaterNewCession", self, null);
	}
	
	public void onLaterNewCession() {
		getObjectTabController().getListe().setResultatsIds(getResultatsIds());

		getObjectTabController().getListe().onNewCessionFromResultatModale();
		
		Clients.clearBusy();
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}


	public void onLaterShowResults() {
		List<Integer> ids = new ArrayList<Integer>();
		if (resultatsIds.size() > 500) {
			Collections.reverse(resultatsIds);
			ids = resultatsIds.subList(0, 500);
		} else {
			ids = resultatsIds;
		}

		resultats = new ArrayList<Object>();
		if (entiteToSearch.getNom().equals("Patient")) {
			resultats.addAll(ManagerLocator.getPatientManager()
					.findByIdsInListManager(ids));
		} else if (entiteToSearch.getNom().equals("Prelevement")) {
			resultats.addAll(ManagerLocator.getPrelevementManager()
					.findByIdsInListManager(ids));
		} else if (entiteToSearch.getNom().equals("Echantillon")) {
			resultats.addAll(ManagerLocator.getEchantillonManager()
					.findByIdsInListManager(ids));
		} else if (entiteToSearch.getNom().equals("ProdDerive")) {
			resultats.addAll(ManagerLocator.getProdDeriveManager()
					.findByIdsInListManager(ids));
		} else if (entiteToSearch.getNom().equals("Cession")) {
			resultats.addAll(ManagerLocator.getCessionManager()
					.findByIdsInListManager(ids));
		}

		// si l'utilisateur n'a pas validé sa recherche, on ne ferme
		// pas la fenêtre
		// si le chemin d'accès à la page est correcte
		if (Path.getComponent(pathToRespond) != null) {
			Events.postEvent(new Event("onGetObjectFromResearch", Path
					.getComponent(pathToRespond), resultats));
		}
		
		Clients.clearBusy();
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}

	/*************************************************************************/
	/************************** ABSTRACTS ************************************/
	/*************************************************************************/

	public abstract void onClick$find();

	public abstract void onOpen$groupAnnotations();
	
	public abstract List<String> getOpenedGroups();
	
	public abstract void setGroupPatientsOpened(Boolean g);

	public abstract void setGroupMaladiesOpened(Boolean g);

	public abstract void setGroupPrelevementsOpened(Boolean g);

	public abstract void setGroupEchantillonsOpened(Boolean g);

	public abstract void setGroupProdDerivesOpened(Boolean g);


	/************************************************************/
	/***************** Affichage des annotations. ***************/
	/************************************************************/

	/**
	 * Dessine les annotations.
	 * @version 2.0.10.2 
	 */
	public void drawAnnotationPanelContent(Banque bankUsedToDrawChamps) {

		if (bankUsedToDrawChamps != null) {
			// recupere les tables pour dessiner chaque groupe
			List<TableAnnotation> tabs = ManagerLocator
					.getTableAnnotationManager().findByEntiteAndBanqueManager(
							this.entiteToSearch, bankUsedToDrawChamps);

			Row annoTable = null;

			// pour chaque groupe
			for (int i = 0; i < tabs.size(); i++) {
				// on dessine le titre
				annoTable = drawTableAnnotationGroup(tabs.get(i));
				// 2.0.10.2 attention tables partagées toutes collections
				if (rechercheRows.getFellowIfAny(annoTable.getId()) == null) {
					rechercheRows.appendChild(annoTable);
	
					// dessine les champs
					Iterator<ChampAnnotation> champsIt = ManagerLocator
							.getTableAnnotationManager()
							.getChampAnnotationsManager(tabs.get(i)).iterator();
					// pour chaque
					while (champsIt.hasNext()) {
						Row newRow = new Row();
						Cell cellLabel = new Cell();
						ChampAnnotation champ = champsIt.next();
						// label du champ
						Label champLabel = new Label(champ.getNom());
						champLabel.setSclass("formLabel");
						cellLabel.appendChild(champLabel);
						cellLabel.setColspan(1);
						newRow.appendChild(cellLabel);
						
						// dessine le champ
						Cell cell = new Cell();
						cell.appendChild(drawAnnotation(champ));
						cell.setColspan(3);
						newRow.appendChild(cell);
						rechercheRows.appendChild(newRow);
					}
				}
			}
		}
	}

	/**
	 * Dessine les annotations.
	 */
	public void drawINCaAnnotationsContent(Banque bankUsedToDrawChamps,
			Entite entite, Component elt) {

		if (bankUsedToDrawChamps != null) {
			// recupere les tables pour dessiner chaque groupe
			List<TableAnnotation> tabs = ManagerLocator
					.getTableAnnotationManager()
					.findByEntiteBanqueAndCatalogueManager(entite,
							bankUsedToDrawChamps, "INCa");

			// pour chaque groupe
			for (int i = 0; i < tabs.size(); i++) {

				// dessine les champs
				Iterator<ChampAnnotation> champsIt = ManagerLocator
						.getTableAnnotationManager()
						.getChampAnnotationsManager(tabs.get(i)).iterator();
				// pour chaque
				Row newRow = new Row();
				int cpt = 0;
				boolean spans = true;
				while (champsIt.hasNext()) {
					ChampAnnotation champ = champsIt.next();
					++cpt;
					if (cpt == 1) {
						if (!champsIt.hasNext()) {
							newRow = new Row();
							spans = true;
						} else {
							newRow = new Row();
							spans = false;
						}
					}
					// label du champ
					Cell cellLabel = new Cell();
					Label champLabel = new Label(champ.getNom());
					champLabel.setSclass("formLabel");
					cellLabel.appendChild(champLabel);
					cellLabel.setColspan(1);
					newRow.appendChild(cellLabel);
					// dessine le champ
					Cell cellInput = new Cell();
					cellInput.appendChild(drawAnnotation(champ));
					cellInput.setColspan(spans ? 3 : 1);
					newRow.appendChild(cellInput);


					if (cpt == 2) {
						cpt = 0;
						if (elt != null) {
							rechercheRows.insertBefore(newRow, elt);
						} else {
							rechercheRows.appendChild(newRow);
						}
					} else if (!champsIt.hasNext()) {
						if (elt != null) {
							rechercheRows.insertBefore(newRow, elt);
						} else {
							rechercheRows.appendChild(newRow);
						}
					}
				}
			}
		}
	}

	/**
	 * Dessine un groupe pour une table annotation. Dessine les champs à
	 * l'intérieur du groupe. Cette méthodes est appelée lors de la mise en
	 * place de la page lorsque la banque a été selectionnée.
	 * 
	 * @param table
	 */
	private Row drawTableAnnotationGroup(TableAnnotation table) {
		Row tableRow = new Row();
		tableRow.setId("table" + table.getTableAnnotationId());
		Cell cell = new Cell();
		cell.setColspan(5);
		Vbox vbox = new Vbox();
		vbox.setParent(cell);
		Label label = new Label(table.getNom());
		label.setSclass("formSubTitle");
		label.setParent(vbox);
		Image img = new Image();
		img.setSrc("/images/pixelble.gif");
		img.setStyle("background-repeat: repeat-x;");
		img.setParent(vbox);

		tableRow.appendChild(cell);
		return tableRow;
	}

	/**
	 * Dessine un composant ChampAnnotation afin de l'assigner dans une Row sous
	 * le groupe TableAnnotation. Cette méthodes est appelée lors de la mise en
	 * place de la page lorsque la banque a été selectionnée.
	 * 
	 * @param champ
	 * @return Component
	 * @version 2.0.13
	 */
	private Component drawAnnotation(ChampAnnotation champ) {
		Component box = null;
		if (champ != null) {
			DataType dtype = champ.getDataType();

			if ("alphanum".equals(dtype.getType())
					|| "hyperlien".equals(dtype.getType())) {
				box = createTextbox(champ);
			} else if ("boolean".equals(dtype.getType())) {
				box = createCheckbox(champ);
			} else if ("date".equals(dtype.getType())) {
				box = createDatebox(champ);
			} else if ("datetime".equals(dtype.getType())) {
				box = createCalendarbox(champ);
			} else if ("num".equals(dtype.getType())) {
				box = createDoublebox(champ);
			} else if ("texte".equals(dtype.getType())) {
				box = createTextboxForText(champ);
			} else if (dtype.getType().matches("thesaurus")) {
				box = createThesaurusBox(champ, false);
			} else if (dtype.getType().matches("thesaurusM")) {
				box = createThesaurusBox(champ, true);
			} else if ("fichier".equals(dtype.getType())) {
				box = createFilebox(champ);
			}
			
			// div -> contient box + opérateurs
			if (!(box instanceof Div)) {
				box.setId("annoBox" + champ.getChampAnnotationId());
				objAnnotationsComponent.add(box);
			} else {
				if (!box.hasAttribute("thesM")) {
					box.getLastChild()
						.setId("annoBox" + champ.getChampAnnotationId());
					objAnnotationsComponent.add(box.getLastChild());
					annoOperateurs.add(box.getFirstChild());
				} else { // thesaurusM
					box.getFirstChild().setAttribute("chpId", 
							champ.getChampAnnotationId().toString());
					box.getFirstChild()
						.setId("annoBox" + champ.getChampAnnotationId());
					objAnnotationsComponent.add(box.getFirstChild());
				}
			}
		}

		return box;
	}

	/**
	 * Dessine la TextBox.
	 */
	private Textbox createTextbox(ChampAnnotation champ) {
		Textbox box = new Textbox();
		((Textbox) box).setCols(15);

		box.setAttribute("champ", champ);
		box.setAttribute("entite", champ.getTableAnnotation().getEntite()
				.getNom());

		return box;
	}

	/**
	 * Dessine la checkbox sous la forme d'une liste oui/non/vide
	 */
	private Listbox createCheckbox(ChampAnnotation champ) {
		Listbox box = new Listbox();

		box.setAttribute("champ", champ);
		box.setAttribute("entite", 
				champ.getTableAnnotation().getEntite()
											.getNom());
		box.setMold("select");	
		box.setRows(1);
		box.appendChild(new Listitem("", null));
		box.appendChild(new Listitem(Labels
				.getLabel("annotation.boolean.oui"), true));
		box.appendChild(new Listitem(Labels
				.getLabel("annotation.boolean.non"), false));
		return box;
	}

	/**
	 * Dessine la dateBox.
	 */
	private Div createDatebox(ChampAnnotation champ) {
		Div divBox = new Div();
		
		// operateurs
		Listbox opBox = new Listbox();
		opBox.setId("opannoBox" + champ.getChampAnnotationId());
		opBox.setMold("select");
		opBox.setRows(1);
		opBox.setModel(new ListModelList<String>(getOperateursDecimaux()));
		divBox.appendChild(opBox);
		
		Datebox box = new Datebox();
		((Datebox) box).setFormat(Labels.getLabel("validation.date.format.simple"));
		((Datebox) box).setCols(10);

		box.setAttribute("champ", champ);
		box.setAttribute("entite", champ.getTableAnnotation().getEntite()
				.getNom());
		
		divBox.appendChild(box);

		return divBox;
	}
	
	/**
	 * Dessine la CalendarBox.
	 */
	private Div createCalendarbox(ChampAnnotation champ) {
		Div divBox = new Div();
		
		// operateurs
		Listbox opBox = new Listbox();
		opBox.setId("opannoBox" + champ.getChampAnnotationId());
		opBox.setMold("select");
		opBox.setRows(1);
		opBox.setModel(new ListModelList<String>(getOperateursDecimaux()));
		divBox.appendChild(opBox);
		
		CalendarBox box = (CalendarBox) getPage()
				.getComponentDefinition("calendarbox", false)
				.newInstance(getPage(), "fr.aphp.tumorotek.component.CalendarBox");

		box.applyProperties();
		((CalendarBox) box).afterCompose();

		box.setAttribute("champ", champ);
		box.setAttribute("entite", champ.getTableAnnotation().getEntite()
				.getNom());
		
		divBox.appendChild(box);

		return divBox;
	}

	/**
	 * Dessine la DooubleBox.
	 */
	private Div createDoublebox(ChampAnnotation champ) {
		Div divBox = new Div();
		
		// operateurs
		Listbox opBox = new Listbox();
		opBox.setId("opannoBox" + champ.getChampAnnotationId());
		opBox.setMold("select");
		opBox.setRows(1);
		opBox.setModel(new ListModelList<String>(getOperateursDecimaux()));
		divBox.appendChild(opBox);
		
		Decimalbox box = new Decimalbox();
		box.setFormat("0.###");
		box.setLocale("en");
		box.setScale(3);

		box.setAttribute("champ", champ);
		box.setAttribute("entite", champ.getTableAnnotation().getEntite()
				.getNom());
		
		divBox.appendChild(box);

		return divBox;
	}

	/**
	 * Dessine le thesaurus sous la forme d'un Listbox.
	 * @version 2.0.13
	 */
	private Component createThesaurusBox(ChampAnnotation champ, boolean multiple) {
		// Items si thesaurus
		Set<Item> its = ManagerLocator.getChampAnnotationManager()
				.getItemsManager(champ,
						SessionUtils.getSelectedBanques(sessionScope).get(0));
//		Iterator<Item> itemItor = null;
		// recupere les items
//		itemItor = its.iterator();
		Integer maxLength = ManagerLocator.getChampAnnotationManager()
				.findMaxItemLength(its);

		if (maxLength < 15) {
			maxLength = 15;
		}

	//	Component comboIt = null;

		Listbox box = new Listbox();
		box.setAttribute("champ", champ);
		box.setAttribute("entite", champ.getTableAnnotation().getEntite()
				.getNom());
		box.setMultiple(multiple);
		if (!multiple) {
			box.setWidth(String.valueOf(maxLength * 10) + "px");
			box.setMold("select");
			box.setRows(1);
		} else {
			box.setCheckmark(true);
			box.setHflex("1");
			box.setRows(3);
		}
		// box.setStyle("width:150px;");
		
		box.setItemRenderer(new AnnotationItemRenderer());
		
		ListModelList<Item> itemsModel = new ListModelList<Item>(its);
		if (!multiple) {
			itemsModel.add(0, null);
		}
		box.setModel(itemsModel);
		
//		// ajout d'un item vide
//		comboIt = new Listitem();
//		((Listitem) comboIt).setLabel(null);
//		((Listitem) comboIt).setValue(null);
//		comboIt.setParent(box);
//		// ajoute items au thesaurus
//		if (itemItor != null) {
//			Item it;
//			while (itemItor.hasNext()) {
//				it = itemItor.next();
//				comboIt = new Listitem();
//				((Listitem) comboIt).setLabel(it.getLabel());
//				((Listitem) comboIt).setValue(it);
//				comboIt.setParent(box);
//			}
//		}
		
		// since 2.0.13
		// multiple box + cumulative
		if (multiple) {
			itemsModel.setMultiple(true);
			Div divBox = new Div();
			divBox.setAttribute("thesM", true);
			divBox.appendChild(box);
			divBox.appendChild(new Checkbox(Labels.getLabel("general.search.cumulative")));
			return divBox;
		}

		return box;
	}

	/**
	 * Dessine la TextBox avec contrainte et valeur par défaut pour un champ
	 * texte.
	 */
	private Textbox createTextboxForText(ChampAnnotation champ) {
		Textbox box = new Textbox();
		box.setRows(3);
		box.setCols(15);

		box.setAttribute("champ", champ);
		box.setAttribute("entite", champ.getTableAnnotation().getEntite()
				.getNom());

		return box;
	}
	
	/**
	 * Dessine la filebox sous la forme d'une liste telecharge/a/vide
	 */
	private Listbox createFilebox(ChampAnnotation champ) {
		Listbox box = new Listbox();

		box.setAttribute("champ", champ);
		box.setAttribute("entite", 
				champ.getTableAnnotation().getEntite()
											.getNom());
		box.setAttribute("fileBox", true);
		box.setMold("select");	
		box.setRows(1);
		box.appendChild(new Listitem("", null));
		box.appendChild(new Listitem(Labels
				.getLabel("recherche.fichier.exist"), false));
		box.appendChild(new Listitem(Labels
				.getLabel("recherche.fichier.absent"), true));
		return box;
	}

	

	/*************************************************************************/
	/************************** GETTERS - SETTERS ****************************/
	/*************************************************************************/

	public Entite getEntiteToSearch() {
		return entiteToSearch;
	}

	public void setEntiteToSearch(Entite e) {
		this.entiteToSearch = e;
	}

	public String getPathToRespond() {
		return pathToRespond;
	}

	public void setPathToRespond(String p) {
		this.pathToRespond = p;
	}

	public boolean isOneValueEntered() {
		return oneValueEntered;
	}

	public void setOneValueEntered(boolean o) {
		this.oneValueEntered = o;
	}

	public boolean isOtherQuery() {
		return otherQuery;
	}

	public void setOtherQuery(boolean o) {
		this.otherQuery = o;
	}

	public List<Object> getResultats() {
		return resultats;
	}

	public void setResultats(List<Object> r) {
		this.resultats = r;
	}

	public List<Integer> getResultatsIds() {
		return resultatsIds;
	}

	public void setResultatsIds(List<Integer> ids) {
		this.resultatsIds = ids;
	}

	public boolean isAnnotationAlreadyOpen() {
		return annotationAlreadyOpen;
	}

	public void setAnnotationAlreadyOpen(boolean a) {
		this.annotationAlreadyOpen = a;
	}

	public List<Critere> getCriteresStandards() {
		return criteresStandards;
	}

	public void setCriteresStandards(List<Critere> c) {
		this.criteresStandards = c;
	}

	public List<Object> getValeursStandards() {
		return valeursStandards;
	}

	public void setValeursStandards(List<Object> v) {
		this.valeursStandards = v;
	}

	public List<String> getOperateursDecimaux() {
		return operateursDecimaux;
	}

	public void setOperateursDecimaux(List<String> o) {
		this.operateursDecimaux = o;
	}

	public List<String> getOperateursDates() {
		return operateursDates;
	}

	public void setOperateursDates(List<String> o) {
		this.operateursDates = o;
	}

	public ListModelList<SearchHistory> getItemSearchHistoryListbox() {
		return itemSearchHistoryListbox;
	}

	public void setItemSearchHistoryListbox(
			ListModelList<SearchHistory> itemSearchHistoryListbox) {
		this.itemSearchHistoryListbox = itemSearchHistoryListbox;
	}

	public List<Component> getObjAnnotationsComponent() {
		return objAnnotationsComponent;
	}

	public void setObjAnnotationsComponent(List<Component> o) {
		this.objAnnotationsComponent = o;
	}

	public boolean isSearchForProdDerives() {
		return searchForProdDerives;
	}

	public void setSearchForProdDerives(boolean s) {
		this.searchForProdDerives = s;
	}

	public Champ getParent1ToQueryProdDerive() {
		return parent1ToQueryProdDerive;
	}

	public void setParent1ToQueryProdDerive(Champ p) {
		this.parent1ToQueryProdDerive = p;
	}

	public Champ getParent2ToQueryProdDerive() {
		return parent2ToQueryProdDerive;
	}

	public void setParent2ToQueryProdDerive(Champ p) {
		this.parent2ToQueryProdDerive = p;
	}

	public List<Critere> getCriteresDerives1() {
		return criteresDerives1;
	}

	public void setCriteresDerives1(List<Critere> c) {
		this.criteresDerives1 = c;
	}

	public List<Object> getValeursDerives1() {
		return valeursDerives1;
	}

	public void setValeursDerives1(List<Object> v) {
		this.valeursDerives1 = v;
	}

	public List<Critere> getCriteresDerives2() {
		return criteresDerives2;
	}

	public void setCriteresDerives2(List<Critere> c) {
		this.criteresDerives2 = c;
	}

	public List<Object> getValeursDerives2() {
		return valeursDerives2;
	}

	public void setValeursDerives2(List<Object> v) {
		this.valeursDerives2 = v;
	}

	/*--------------- SEARCH HISTORY IMPLEMENTATION ----------*/

	/**
	 * Create the listbox history get save history in sessionscope filter by
	 * entite name.
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void createSearchHistoryListbox(String entiteNom) {
		if (sessionScope.containsKey("SearchHistorySession")) {
			searchHistoryList = (LinkedList<SearchHistory>) sessionScope
					.get("SearchHistorySession");
		}

		itemSearchHistoryListbox = new ListModelList<SearchHistory>();

		for (SearchHistory searchHistory : searchHistoryList) {
			if (searchHistory.getType().contentEquals(entiteNom)) {
				itemSearchHistoryListbox.add(0, searchHistory);
				panelSearchHistorique.setOpen(true);
			}
		}
	}

	/**
	 * Créé l'historique de recherche et l'ajoute à la liste
	 * 
	 * Ajoute les composants d'une recherche à l'historique
	 */

	protected void createSearchHistory(String entiteNom) {

		searchHistory = new SearchHistory();
//		List<Component[]> listSearchHistoryComponents = null;

//		if (this.entiteToSearch.getNom().equals("Cession")) {
//			listSearchHistoryComponents = Arrays.asList(objCessionComponents,
//					objOperateurs);
//		} else {
//
//			listSearchHistoryComponents = Arrays
//					.asList(objPatientComponents,
//							objMaladieComponents,
//							objPrelevementComponents,
//							objEchantillonComponents,
//							objOperateurs,
//							objProdDeriveComponents,
//							objPrelevementContextComponents != null ? objPrelevementContextComponents
//									: new Component[0]); // si l'obj est non nul
//															// on l'utilise
//															// sinon on insère
//															// un array vide
//		}		
		

		// save component
		//searchHistory.setComponents(listSearchHistoryComponents);
		searchHistory.getListSearchHistoryComponent().addAll(getUsedComponents());
		searchHistory.getListSearchHistoryComponent().addAll(clonedUsedOperators(Arrays.asList(objOperateurs)));
		searchHistory.getListAnnotationsComponent().addAll(getUsedAnnoComponents());
		searchHistory.getListSearchHistoryComponent().addAll(clonedUsedOperators(annoOperateurs));
		searchHistory.setType(entiteNom);
		searchHistory.setInfo(Utils.getCurrentSystemTime(), resultatsIds.size());
		
		// openedgroups
		searchHistory.getOpenedGroupsIds().addAll(getOpenedGroups());
		
		// add current search history to sessionScope
		searchHistoryList.add(searchHistory);
		getItemSearchHistoryListbox().add(0, searchHistory);
	}

	/*--------------- SEARCH HISTORY BOX EVENT  --------------*/
	private Collection<RechercheCompValues> clonedUsedOperators(List<Component> ops) {
		List<RechercheCompValues> clonedOperators = new ArrayList<RechercheCompValues>();
		for (Component cpt : ops) {
			if (cpt != null && ((Listbox) cpt).getSelectedIndex() > -1) {
				RechercheCompValues rcv = new RechercheCompValues();
				rcv.setCompClass(Listbox.class);
				rcv.setCompId(cpt.getId());
				rcv.setSelectedIndexValue(((Listbox) cpt).getSelectedIndex());
				clonedOperators.add(rcv);
			}
		}
		return clonedOperators;
	}

	public void onSelect$searchHistoryListbox(SelectEvent<Listitem, ?> event) {

		SearchHistory sH = getItemSearchHistoryListbox()
						.getSelection().iterator().next();
		
		// putSearchHistoryValues(sH, false);
		
		if (!sH.getListAnnotationsComponent().isEmpty()) {
			
			if (groupAnnotations != null) {
				groupAnnotations.setOpen(true);
				onOpen$groupAnnotations();
			}

		} 
//		else { // nettoie le bloc
//			if (groupAnnotations != null) {
//				groupAnnotations.setOpen(false);
//				setAnnotationAlreadyOpen(false);
//				Component r = groupAnnotations.getNextSibling();
//				List<Row> rowsToDetach = new ArrayList<Row>();
//				while (r != null) {
//					rowsToDetach.add((Row) r);
//					r = r.getNextSibling();
//				}
//				for (Row row : rowsToDetach) {
//					row.detach();
//				}
//			}
//		}
		
		putSearchHistoryValues(sH);

	}

	@SuppressWarnings("unchecked")
	private void putSearchHistoryValues(SearchHistory sH) {
		
		// tous les composants à renseigner ou effacer à partir de l'historique
		List<Component> allComps = new ArrayList<Component>();
		if (this.entiteToSearch.getNom().equals("Cession")) {
			allComps.addAll(Arrays.asList(objCessionComponents));
			allComps.addAll(Arrays.asList(objOperateurs));
		} else {
			allComps.addAll(Arrays.asList(objPatientComponents));
			allComps.addAll(Arrays.asList(objMaladieComponents));
			allComps.addAll(Arrays.asList(objPrelevementComponents));
			allComps.addAll(Arrays.asList(objEchantillonComponents));
			allComps.addAll(Arrays.asList(objProdDeriveComponents));
			allComps.addAll(Arrays.asList(objOperateurs));

			allComps.addAll(objPrelevementContextComponents != null ? 
					Arrays.asList(objPrelevementContextComponents) : new ArrayList<Component>()); 
		}	
		allComps.addAll(objAnnotationsComponent);
		allComps.addAll(annoOperateurs);
		
		for (Component comp : allComps) {
			if (comp != null && rechercheRows.hasFellow(comp.getId())) {
				Component target = rechercheRows.getFellow(comp.getId(), true);
				RechercheCompValues used = extractComponentFromListById(sH, comp.getId());
				if (used != null) { // le composant a été utilisé dans la recherche
					if (target instanceof Listbox) {
						// if (!((Listbox) target).isMultiple()) {
							if (used.getSelectedIndexValue() != null && used.getSelectedIndexValue() != -1) {
								((Listbox) target).setSelectedIndex(used.getSelectedIndexValue());
							} else {
								((Listbox) target).clearSelection();
							}
						//} else { // multiple listbox
						if (((Listbox) target).getModel() != null)  {
							((Selectable<Object>) ((Listbox) target).getModel())
								.clearSelection();
							for (Object obj : used.getSelectedValues()) {
								((Selectable<Object>) ((Listbox) target).getModel())
									.addToSelection(obj);
							}
							if (((Listbox) target).isMultiple()) {
								((Checkbox) target.getNextSibling()).setChecked(used.isCumulative());
							}
						}
					} else if (target instanceof InputElement) {
						if (used.getTextValue() != null && !used.getTextValue().equals("")) {
							((InputElement) target).setText(used.getTextValue());
						} else {
							((InputElement) target).setRawValue(null);
						}
					} else if (target instanceof Checkbox) {
						if (used.getCheckedValue() != null && used.getCheckedValue()) {
							((Checkbox) target).setChecked(true);
						}
					} else if (target instanceof CalendarBox) {
						if (used.getCalendarValue() != null) {
							((CalendarBox) target).setValue(used.getCalendarValue());
						} else {
							((CalendarBox) target).setValue(null);
						}
					} else if (target instanceof Decimalbox) {
						if (used.getTextValue() != null && !used.getTextValue().equals("")) {
							((Decimalbox) target).setText(used.getTextValue());
						} else {
							((Decimalbox) target).setRawValue(null);
						}
					}
				} else { // le component n'a pas été utilisé -> nettoyage
					if (target instanceof Listbox) {
						if (!((Listbox) target).isMultiple()) {
							((Listbox) target).clearSelection();
						} else {
							((Selectable<Object>) ((Listbox) target).getModel())
							.clearSelection();
							((Checkbox) target.getNextSibling()).setChecked(false);
						}
					} else if (target instanceof InputElement) {
						((InputElement) target).setRawValue(null);
					} else if (target instanceof Checkbox) {
						((Checkbox) target).setChecked(false);
					} else if (target instanceof CalendarBox) {
						((CalendarBox) target).setValue(null);
					} 
				}
			} 
		}
		
		// open groups
		for (String groupId : sH.getOpenedGroupsIds()) {
			if (groupId != null && rechercheRows.hasFellow(groupId)) {
				((Group) rechercheRows.getFellow(groupId, true)).setOpen(true);
				
				if (groupId.equals("groupPatients")) {
					setGroupPatientsOpened(true);
				} else if (groupId.equals("groupMaladies")) {
					setGroupMaladiesOpened(true);
				} else if (groupId.equals("groupPrelevements")) {
					setGroupPrelevementsOpened(true);
				} else if (groupId.equals("groupEchantillons")) {
					setGroupEchantillonsOpened(true);
				} else if (groupId.equals("groupProdDerives")) {
					setGroupProdDerivesOpened(true);
				}
			}
		}
	}

	
	private RechercheCompValues extractComponentFromListById(SearchHistory sH, String compId) {		
		if (sH != null && compId != null) {
//			if (!annos) {
				if (sH.getListSearchHistoryComponent() != null) {
					for (RechercheCompValues rcv : sH.getListSearchHistoryComponent()) {
						if (rcv != null && compId.equals(rcv.getCompId())) {
							return rcv;
						}
					}
				}
//			} else {
//				if (sH.getListAnnotationsComponent() != null) {
//					for (RechercheCompValues rcv : sH.getListAnnotationsComponent() ) {
//						if (rcv != null && compId.equals(rcv.getCompId())) {
//							return rcv;
//						}
//					}
//				}
//			}
		}	
		return null;
	}

	public List<RechercheCompValues> getUsedComponents() {
		return usedComponents;
	}

	public void setUsedComponents(List<RechercheCompValues> u) {
		this.usedComponents = u;
	}

	public List<RechercheCompValues> getUsedAnnoComponents() {
		return usedAnnoComponents;
	}

	public void setUsedAnnoComponents(List<RechercheCompValues> u) {
		this.usedAnnoComponents = u;
	}
}
