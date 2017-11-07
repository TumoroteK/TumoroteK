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
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.qualite.NonConformite;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une
 * non conformité. 
 * Classe créée le 25/09/12. 
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.8
 *
 */
public class ModificationMultipleNonConformites 
	extends AbstractModificationMultipleComponent {

	private static final long serialVersionUID = 3551763682958457361L;
	
	/**
	 * Components.
	 */
	private Checkbox conformeCheckOne;
	private Checkbox nonconformeCheckOne;
	private Listbox nonConformitesBoxOne;
	private Div nonConformeDivOne;
	private Checkbox conformeCheckErase;
	private Checkbox nonconformeCheckErase;
	private Listbox nonConformitesBoxErase;
	private Div nonConformeDivErase;
	
	private List<NonConformite> nonConformites = new ArrayList<NonConformite>();
	private Set<Listitem> selectedNonConformitesItem = new HashSet<Listitem>();
	private Set<Listitem> selectedNonConformitesEraseItem = new HashSet<Listitem>();
		
	public List<NonConformite> getNonConformites() {
		return nonConformites;
	}

	public Set<Listitem> getSelectedNonConformitesItem() {
		return selectedNonConformitesItem;
	}

	public void setSelectedNonConformitesItem(Set<Listitem> sels) {
		this.selectedNonConformitesItem = sels;
	}
	
	public Set<Listitem> getSelectedNonConformitesEraseItem() {
		return selectedNonConformitesEraseItem;
	}

	public void setSelectedNonConformitesEraseItem(Set<Listitem> sels) {
		this.selectedNonConformitesEraseItem = sels;
	}

	private Div eraseDiv;
	
	@Override
	public void setConstraintsToBoxes(Constraint constr) {
	}
	
	/**
	 * Surcharge de la méthode init pour déclencher la récupération 
	 * des non conformités.
	 * Surcharge de champNameThesaurus pour paramétrer le type de 
	 * non conformité.
	 */
	public void init(String pathToPage, String methodToCall,
			List<? extends Object> objs, String label, String champToEdit,
			List<? extends Object> allValuesThesaurus,
			String champNameThesaurus, String entiteNom) {
		
		// copie la liste pour eviter la modification de la liste originale
		this.nonConformites.clear();
		Iterator<? extends Object> iT = allValuesThesaurus.iterator();
		while (iT.hasNext()) {
			this.nonConformites.add((NonConformite) iT.next());
		}
		setChampThesaurus(champNameThesaurus);
		super.init(pathToPage, methodToCall, objs, label, champToEdit, 
											entiteNom, null, false);
	}
	
	@Override
	public String formatLocalObject(Object obj) {
		if (obj != null) {
			ConformitePack pack = (ConformitePack) obj;
			if (pack.getNonConforme() != null) {
				String out;
				if (pack.getNonConforme()) {
					out = "non conforme";
					if (pack.getNonConformites() != null) {
						out = out + ": ";
						Iterator<NonConformite> ncIt = pack.getNonConformites().iterator();
						while (ncIt.hasNext()) {
							 out = out + ncIt.next().getNom();
							 if (ncIt.hasNext()) {
									out = out + ", ";
							}
						}
						
					}
				} else {
					out = "conforme";
				}
				return out;
			}
		} 
		return null;
	}
	
	@Override
	public void setEraserBoxeVisible(boolean visible) {
		eraseDiv.setVisible(visible);
	}
	
	@Override
	public Object extractValueFromEraserBox() {
		ConformitePack cPack = new ConformitePack();
		if (conformeCheckErase.isChecked()) {
			cPack.setNonConforme(false);
		} else if (nonconformeCheckErase.isChecked()) {
			cPack.setNonConforme(true);
			if (nonConformitesBoxErase.getSelectedItems().size() > 0) {
				cPack.getNonConformites().clear();
				cPack.getNonConformites()
					.addAll(getValuesFromSelectedItems(nonConformitesBoxErase));
			} 
		}		
		return cPack;
	}

	
	@Override
	public AnnotateDataBinder getBinder() {
		return ((AnnotateDataBinder) self
				.getParent()
				.getAttributeOrFellow("modificationNonConformites", true));
	}
	
	@Override
	public void passValueToEraserBox() {
		conformeCheckErase.setChecked(false);
		nonconformeCheckErase.setChecked(false);
		nonConformitesBoxErase.clearSelection();
		nonConformeDivErase.setVisible(false);
		if (getSelectedValue() != null) {
			ConformitePack pck = (ConformitePack) getSelectedValue();
			if (pck.getNonConforme() != null) {
				conformeCheckErase.setChecked(!pck.getNonConforme());
				nonconformeCheckErase.setChecked(pck.getNonConforme());
				nonConformeDivErase.setVisible(pck.getNonConforme());
			} else {
				nonConformeDivErase.setVisible(false);
			}
			if (!pck.getNonConformites().isEmpty()) {
				Iterator<Listitem> it = nonConformitesBoxErase.getItems().iterator();
				Listitem li;
				while (it.hasNext()) {
					li = it.next();
					if (pck.getNonConformites()
										.contains((NonConformite) li.getValue())) {
						li.setSelected(true);
					}
				}
			}
		} 
		nonConformitesBoxErase.invalidate();
		// getBinder().loadAttribute(nonConformitesBoxErase, "selectedItems");
	}

	@Override
	public void passNullToEraserBox() {
		conformeCheckErase.setChecked(false);
		nonconformeCheckErase.setChecked(false);
		nonConformitesBoxErase.clearSelection();
		getBinder().loadAttribute(nonConformitesBoxErase, "selectedItems");
	}
	
	public void onCheck$conformeCheckOne() {
		nonconformeCheckOne.setChecked(false);
		if (conformeCheckOne.isChecked()) {
			nonConformeDivOne.setVisible(false);
		}
	}
	
	public void onCheck$nonconformeCheckOne() {
		conformeCheckOne.setChecked(false);
		if (nonconformeCheckOne.isChecked()) {			
			nonConformeDivOne.setVisible(true);
		} else {
			nonConformeDivOne.setVisible(false);
		}
	}
	
	public void onCheck$conformeCheckErase() {
		if (conformeCheckErase.isChecked()) {
			nonconformeCheckErase.setChecked(false);
			nonConformeDivErase.setVisible(false);
		}
	}
	
	public void onCheck$nonconformeCheckErase() {
		if (nonconformeCheckErase.isChecked()) {
			conformeCheckErase.setChecked(false);
			nonConformeDivErase.setVisible(true);
		} else {
			nonConformeDivErase.setVisible(false);
		}
	}

	@Override
	public Object extractValueFromMultiBox() {
		ConformitePack cPack = new ConformitePack();
		if (conformeCheckOne.isChecked()) {
			cPack.setNonConforme(false);
		} else if (nonconformeCheckOne.isChecked()) {
			cPack.setNonConforme(true);
			if (nonConformitesBoxOne.getSelectedItems().size() > 0) {
				cPack.getNonConformites().clear();
				cPack.getNonConformites()
					.addAll(getValuesFromSelectedItems(nonConformitesBoxOne));
			} 
		}			
		return cPack;
	}
	
	private List<NonConformite> getValuesFromSelectedItems(Listbox box) {
		List<NonConformite> values = new ArrayList<NonConformite>();
		Iterator<Listitem> itsOr = box.getSelectedItems().iterator();
		while (itsOr.hasNext()) {
			values.add(nonConformites
					.get(box.getIndexOfItem(itsOr.next())));
		}
		return values;
	}
	
//	/**
//	 * Prépare la liste de selectedItemps à partir de la liste de 
//	 * non conformites.
//	 * @param box
//	 * @param ncfs List<NonConformite>
//	 * @return Set<Listitem> selectedItems
//	 */
//	private List<Listitem> getSelectedItemsFromValues(Listbox box, 
//			List<NonConformite> selNcfs) {
//		List<Listitem> values = new ArrayList<Listitem>();
//		Iterator<NonConformite> it = selNcfs.iterator();
//		while (it.hasNext()) {
//			values.add(box.getItemAtIndex(nonConformites
//					.indexOf(it.next())));
//		}
//		return values;
//	}
	
	
	
	@Override
	public void extractValuesFromObjects() {
		setHasNulls(false);
		// pour chaque objet à modifier
		// on extrait le booléen et la liste de non conformites
		for (int i = 0; i < getListObjets().size(); i++) {
			try {
				// on extrait le booleen
				Boolean conforme = (Boolean) PropertyUtils.getSimpleProperty(
						getListObjets().get(i), getChamp());
				
				// on recupere les non conformites
				List<NonConformite> nonConfs = ManagerLocator.getNonConformiteManager()
					.getFromObjetNonConformes(ManagerLocator
					.getObjetNonConformeManager()
					.findByObjetAndTypeManager(getListObjets().get(i), getChampThesaurus()));
				
				ConformitePack cPack = new ConformitePack();
				cPack.setConforme(conforme);
				cPack.setNonConformites(nonConfs);
				
				if (!cPack.isEmpty()) {
					if (!getValues().contains(cPack)) {					
						getValues().add(cPack);
						getStringValues().add(formatLocalObject(cPack));
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
	}
	
	@Override
	public void setUniqueValueToMultiBox() {
		setNewValue(((ConformitePack) getOldUniqueValue()).clone());
	}
	
	@Override
	public void initComponentsInWindow() {
		super.initComponentsInWindow();
		// init des composants
		if ((ConformitePack) getNewValue() != null 
			&& ((ConformitePack) getNewValue())
									.getNonConforme() != null) {
			nonconformeCheckOne.setChecked(((ConformitePack) 
					getNewValue()).getNonConforme());
			conformeCheckOne.setChecked(((ConformitePack) 
					getNewValue()).getConforme());
			
			nonConformeDivOne.setVisible(((ConformitePack) 
					getNewValue()).getNonConforme());
		}
		
		// dessine la liste
		Listitem li;
		for (int i = 0; i < nonConformites.size(); i++) {
			li = new Listitem();
			//li.setLabel(((Item) allValues.get(i)).getLabel());
			li.setLabel(nonConformites.get(i).getNom());
			li.setValue(nonConformites.get(i));
			if ((ConformitePack) 
					getNewValue() != null 
				&& ((ConformitePack) getNewValue()).getNonConformites()
							.contains(nonConformites.get(i))) {
					li.setSelected(true);
			}
			li.setParent(nonConformitesBoxOne);
		}
	}
}
