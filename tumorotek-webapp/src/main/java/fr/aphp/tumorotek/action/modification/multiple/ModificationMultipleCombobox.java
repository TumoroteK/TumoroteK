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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une
 * Combobox.
 * Classe créée le 25/03/11. 
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ModificationMultipleCombobox 
	extends AbstractModificationMultipleComponent {

	private static final long serialVersionUID = 3551763682958457361L;

	private Combobox eraseListBox;
	private Combobox oneValueListBox;
	private Boolean isObligatoire;
	

	// Toutes les valeurs possibles du champ
	private List<Object> allValues = new ArrayList<Object>();
	// Valeurs String correspondantes
	private List<String> allStringValues = new ArrayList<String>();
	// valeur string correspondante
	private String selectedStringValue = null;
	
	@Override
	public Object extractValueFromEraserBox() {
		// si une valeur selectionnée
		if (eraseListBox.getSelectedIndex() > -1) {
			return allValues.get(eraseListBox.getSelectedIndex());
		} else {
			if (!allValues.isEmpty()) {
				return allValues.get(0);
			} 
			return null;
		}
	}

	@Override
	public Object extractValueFromMultiBox() {
		// si une valeur selectionnée
		if (oneValueListBox.getValue() != null && !oneValueListBox.getValue().equals("")) {
			return allValues.get(allStringValues.indexOf(oneValueListBox.getValue()));
			//return allValues.get(oneValueListBox.getSelectedIndex());
		} else {
			return null;
		}
	}

	@Override
	public void setConstraintsToBoxes(Constraint constr) {
	}

	@Override
	public void setEraserBoxeVisible(boolean visible) {
		eraseListBox.setVisible(visible);
	}
	
	@Override
	public void passValueToEraserBox() {
		eraseListBox
			.setSelectedItem(findItemForValue(multiListBox
									.getSelectedItem().getValue()));	
	}
	
	@Override
	public void passNullToEraserBox() {
		eraseListBox.setSelectedItem(null);	
	}
	
	@SuppressWarnings("unchecked")
	private Comboitem findItemForValue(Object value) {
		Iterator<Comboitem> its = eraseListBox.getItems().iterator();
		Comboitem item;
		while (its.hasNext()) {
			item = its.next();
			if (item.getValue() != null) {
				if (item.getValue().equals(value)) {
					return item;
				}
			} else if (value == null) {
				return item;
			}
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
	}
	
	@Override
	public void extractValuesFromObjects() {
		
		super.extractValuesFromObjects();
		
		// pour chaque objet du thésaurus, on va extraire la valeur du
		// champ à afficher
		for (int i = 0; i < allValues.size(); i++) {
			try {
				String stringTmp = null;
				
				if (allValues.get(i) != null) {
					stringTmp = (String) 
						PropertyUtils.getSimpleProperty(allValues.get(i), 
													getChampThesaurus());	
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
		if ((isObligatoire == null || !isObligatoire) 
				&& !allValues.isEmpty()) {
			allValues.add(0, null);
			allStringValues.add(0, "---");
		}		
	}
	
	@Override
	public String formatLocalObject(Object obj) {
		if (getIsCombined() && obj != null 
				&& obj.equals("system.tk.unknownExistingValue")) {
			return Labels.getLabel("system.tk.unknownExistingValue");
		}
		String out = null;
		try {
			if (obj != null) {
				out = (String) PropertyUtils
							.getSimpleProperty(obj, getChampThesaurus());
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
	
	/**
	 * Initialise les composants de la fenêtre.
	 */
	@Override
	public void initComponentsInWindow() {
		super.initComponentsInWindow();
		
		if (isObligatoire != null && isObligatoire 
					&& getStringValues().contains("")) {
			int empty = getValues().indexOf("");
			getValues().remove(empty);
			getStringValues().remove(empty);
			// selectionne le premier item de la liste
			setSelectedValue(getValues().get(0));
		}
		
		if (getValues().size() == 1) {
			selectedStringValue = getStringValues().get(0);
		}
	}
	
	@Override
	public AnnotateDataBinder getBinder() {
		return ((AnnotateDataBinder) self
				.getParent()
				.getAttributeOrFellow("modificationCombobox", true));
	}

	public List<Object> getAllValues() {
		return allValues;
	}

	public List<String> getAllStringValues() {
		return allStringValues;
	}

	public String getSelectedStringValue() {
		return selectedStringValue;
	}
	
	public void setSelectedStringValue(String sel) {
		this.selectedStringValue = sel;
	}

	@Override
	public Boolean isObligatoire() {
		return isObligatoire;
	}
}
