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
package fr.aphp.tumorotek.action.modification.multiple;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;

//import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
//import fr.aphp.tumorotek.model.coeur.annotation.Item;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une
 * Listbox à choix multiples.
 * Classe créée le 11/03/09. Modifiée par Mathieu pour utiliser
 * factorisation AbstractModificationMultipleComponent.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ModificationMultipleMultiListbox 
	extends AbstractModificationMultipleComponent {

	private static final long serialVersionUID = 3551763682958457361L;

	/**
	 * Components.
	 */
	private Listbox eraseMultiListBox;
	private Listbox oneValueMultiListBox;
	private Boolean isObligatoire;
	
	private Integer listLength = 15;
	

	// Toutes les valeurs possibles
	private List<Object> allValues = new ArrayList<Object>();
	// Valeurs String correspondantes
	private List<String> allStringValues = new ArrayList<String>();
	
	@Override
	public Object extractValueFromEraserBox() {
		Set<Object> res = null;
		// si une valeur selectionnée
		if (eraseMultiListBox.getSelectedItems().size() > 0) {
			res = new HashSet<Object>();
			res.addAll(getValuesFromSelectedItems(eraseMultiListBox));
		} 
		return res;
	}
	
	@Override
	public Object extractValueFromMultiBox() {
		Set<Object> res = null;
		// si une valeur selectionnée
		if (oneValueMultiListBox.getSelectedItems().size() > 0) {
			res = new HashSet<Object>();
			res.addAll(getValuesFromSelectedItems(oneValueMultiListBox));
		} 
		return res;
	}
	
	private List<Object> getValuesFromSelectedItems(Listbox box) {
		List<Object> values = new ArrayList<Object>();
		Iterator<Listitem> itsOr = box.getSelectedItems().iterator();
		while (itsOr.hasNext()) {
			values.add(allValues.get(box.getIndexOfItem(itsOr.next())));
		}
		return values;
	}

	@Override
	public void setConstraintsToBoxes(Constraint constr) {
	}

	@Override
	public void setEraserBoxeVisible(boolean visible) {
		eraseMultiListBox.setVisible(visible);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void passValueToEraserBox() {
		Set<Listitem> selIts = new HashSet<Listitem>();
		eraseMultiListBox.clearSelection();
		if (multiListBox.getSelectedIndex() > 0) {
			Iterator<Object> valuesIt = ((Set<Object>) 
					getValues().get(multiListBox
							.getSelectedIndex())).iterator();
			
			while (valuesIt.hasNext()) {
				selIts.add(findItemForValue(valuesIt.next()));	
			}
		}
		eraseMultiListBox.setSelectedItems(selIts);
	}
	
	@Override
	public void passNullToEraserBox() {
		eraseMultiListBox.clearSelection();
	}
	
	private Listitem findItemForValue(Object value) {
		Iterator<Listitem> its = eraseMultiListBox.getItems().iterator();
		Listitem item;
		String stringVal;
		try {
			while (its.hasNext()) {
				item = its.next();
				if (item.getValue() != null) {
					stringVal = (String) PropertyUtils
						.getSimpleProperty(value, getChampThesaurus());
					if (item.getValue().equals(stringVal)) {
						return item;
					}
				} else if (value == null) {
					return item;
				}
			}
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		} catch (NoSuchMethodException e) {
			log.error(e);
		}
		return null;
	}
	
	/**
	 * Ecrase la methode héritée init pour gérer les values thesaurus.
	 * @param pathToPage
	 * @param methodToCall
	 * @param objs
	 * @param label
	 * @param champToEdit
	 * @param allValuesThesaurus
	 * @param champNameThesaurus
	 * @param entiteNom
	 * @param constr
	 * @param isCombined
	 * @param isObligatoire true si chp annotation obligatoire
	 */
	public void init(String pathToPage, String methodToCall,
			List< ? extends Object> objs, String label, 
			String champToEdit, List<Object> allValuesThesaurus,
			String champNameThesaurus, String entiteNom, Constraint constr,
			Boolean isCombined, Boolean isOblig) {
		
		// copie la liste pour eviter la modification de la liste originale
		this.allValues.clear();
		this.allValues.addAll(allValuesThesaurus);
		setChampThesaurus(champNameThesaurus);
		super.init(pathToPage, methodToCall, objs, label, champToEdit, 
											entiteNom, constr, isCombined);
		isObligatoire = isOblig;
		
		for (int i = 0; i < allValues.size(); i++) {
			//if (((Item) allValues.get(i)).getLabel().length() > listLength) {
			try {
				String val = (String) PropertyUtils
					.getSimpleProperty(allValues.get(i), getChampThesaurus());
				if (val.length() > listLength) {
					listLength = val.length();
				}
			} catch (IllegalAccessException e) {
				log.error(e);
			} catch (InvocationTargetException e) {
				log.error(e);
			} catch (NoSuchMethodException e) {
				log.error(e);
			}
		}
		
		if (isOblig) {
			oneValueMultiListBox
					.addEventListener("onSelect", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					if (((Listbox) oneValueMultiListBox)
							.getSelectedItems().size() == 0 
							&& (!getIsCombined() || !combine.isChecked())) {
						throw new WrongValueException(oneValueMultiListBox, 
								Labels.getLabel("anno.thes.empty"));
					} else {
						Clients.clearWrongValue((Listbox) oneValueMultiListBox);
					}
				}
			});
			eraseMultiListBox.addEventListener("onSelect", new EventListener<Event>() {
				public void onEvent(Event event) throws Exception {
					if (((Listbox) eraseMultiListBox)
							.getSelectedItems().size() == 0 
						&& (!getIsCombined() || !eraseCombine.isChecked())) {
						throw new WrongValueException(eraseMultiListBox, Labels
								.getLabel("anno.thes.empty"));
					} else {
						Clients.clearWrongValue((Listbox) eraseMultiListBox);
					}
				}
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void extractValuesFromObjects() {
		
		// pour chaque objet à modifier
		// on extrait la valeur actuelle du champ à modifier
		// toutes ces valeurs sont placées dans la liste values
		setHasNulls(false);
		for (int i = 0; i < getListObjets().size(); i++) {
			try {
				List< ? extends Object> vals;
				// annotation valeurs
				if (getListObjets().get(i) instanceof List<?>) { 
					 vals = (List<? extends Object>) getListObjets().get(i);
				} else {
					vals = new ArrayList<Object>((Set<? extends Object>) 
						PropertyUtils.getSimpleProperty(getListObjets().get(i), 
																getChamp())); 
				}
				Object tmp = null;
				Set<Object> its = new HashSet<Object>();
				for (int j = 0; j < vals.size(); j++) {
					tmp = null;
					if (vals.get(j) instanceof AnnotationValeur) {
						tmp = PropertyUtils.getSimpleProperty(
							vals.get(j), getChamp());
					} else {
						tmp = vals.get(j);
					}
					if (tmp != null && !tmp.equals("")) {
						its.add(tmp);
					} else  if (tmp == null) {
						if (getIsCombined()) {
							tmp = PropertyUtils.getSimpleProperty(
										vals.get(j), "alphanum");
						}
					}
				}
	
				if (!its.isEmpty()) {
					Object formatted = formatValue(its);
					if (!getValues().contains(formatted)) {					
						getValues().add(formatted);
						getStringValues().add(formatLocalObject(formatted));
					}
				} else if (tmp != null
						&& tmp.equals("system.tk.unknownExistingValue")) {
					if (!getValues().contains(tmp)) {					
						getValues().add(tmp);
						getStringValues().add(formatLocalObject(tmp));
					}
				} else {
					setHasNulls(true);
				}
			} catch (IllegalAccessException e) {
				log.error(e);
			} catch (InvocationTargetException e) {
				log.error(e);
			} catch (NoSuchMethodException e) {
				log.error(e);
			}
		}
		
		// pour chaque objet du thésaurus, on va extraire la valeur du
		// champ à afficher
		for (int i = 0; i < allValues.size(); i++) {
			try {
				String stringTmp = null;
				
				if (allValues.get(i) != null) {
					stringTmp = (String) PropertyUtils.getSimpleProperty(
							allValues.get(i), getChampThesaurus());	
					allStringValues.add(stringTmp);					
				} 
			} catch (IllegalAccessException e) {
				log.error(e);
			} catch (InvocationTargetException e) {
				log.error(e);
			} catch (NoSuchMethodException e) {
				log.error(e);
			}
		}	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String formatLocalObject(Object obj) {
		if (getIsCombined()) {
			if (obj != null
					&& obj.equals("system.tk.unknownExistingValue")) {
				return Labels.getLabel("system.tk.unknownExistingValue");
			}
		}
		String out = null;
		try {
			if (obj != null) {
				StringBuilder bld = new StringBuilder();
				Iterator<Object> objsIt = ((Set<Object>) obj).iterator();
				while (objsIt.hasNext()) {
					bld.append((String) PropertyUtils
						.getSimpleProperty(objsIt.next(), getChampThesaurus()));
					// connecteur ' - '
					if (objsIt.hasNext()) {
						bld.append(" - ");
					}
				}
				out = bld.toString();
			}
		} catch (Exception e) {
			log.error(e);
		}
		return out;			
	}
	
	@Override
	public Object formatValue(Object obj) {
		return obj;
	}
	
	@Override
	public void initComponentsInWindow() {
		super.initComponentsInWindow();
		
		if (getValues().size() < 2) {
			drawOneValueMultiListBox();
		}
	}
	
	/**
	 * Cree la liste Listitem avec selectedItems.
	 * @param its
	 */
	@SuppressWarnings("unchecked")
	private void drawOneValueMultiListBox() {
		Set<Object> its = null;
		if (getValues().get(0) != null 
				&& !getValues().get(0)
						.equals("system.tk.unknownExistingValue")) {
			its = (Set<Object>) getValues().get(0);
		}
		Listitem li;
		try {
			for (int i = 0; i < allValues.size(); i++) {
				li = new Listitem();
				//li.setLabel(((Item) allValues.get(i)).getLabel());
				li.setLabel((String) PropertyUtils
					.getSimpleProperty(allValues.get(i), getChampThesaurus()));
				li.setValue(allValues.get(i));
				// selection
				if (its != null && its.contains(allValues.get(i))) {
					li.setSelected(true);
				}
				li.setParent(oneValueMultiListBox);
			}
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		} catch (NoSuchMethodException e) {
			log.error(e);
		}
		oneValueMultiListBox.setWidth(getListLength());
	}
	
	public String getListLength() {
		return (String.valueOf(listLength * 10) + "px");
	}

	
	@Override
	public AnnotateDataBinder getBinder() {
		return ((AnnotateDataBinder) self
				.getParent()
				.getAttributeOrFellow("modificationMultiListbox", true));
	}

	public List<Object> getAllValues() {
		return allValues;
	}

	public List<String> getAllStringValues() {
		return allStringValues;
	}

	@Override
	public Boolean isObligatoire() {
		return isObligatoire;
	}
	
	@Override
	public void onClick$validate() {

		if (isObligatoire()) {
			if (rowOneValue.isVisible()) {	
				if (((Listbox) oneValueMultiListBox)
						.getSelectedItems().size() == 0
					&& (!getIsCombined() || !combine.isChecked())) {
					throw new WrongValueException(oneValueMultiListBox, 
							Labels.getLabel("anno.thes.empty"));
				}
			} else if (!multiListBox.isDisabled()) {
				if (((Listbox) eraseMultiListBox)
							.getSelectedItems().size() == 0
					&& (!getIsCombined() || !eraseCombine.isChecked())) {
						throw new WrongValueException(eraseMultiListBox, Labels
								.getLabel("anno.thes.empty"));
				} 
			}
		}
		
		super.onClick$validate();
	}
}
