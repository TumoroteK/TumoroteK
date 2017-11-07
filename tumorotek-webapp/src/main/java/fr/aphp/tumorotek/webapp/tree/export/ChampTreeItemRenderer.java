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
package fr.aphp.tumorotek.webapp.tree.export;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import fr.aphp.tumorotek.model.io.export.ChampEntite;


/**
 * Classe gérant l'affichage des informations dans un arbre de type 
 * TumoTreeModel contenant les champs affichages.
 * @author pierre
 *
 */
public class ChampTreeItemRenderer implements TreeitemRenderer<Object> {
	
	@Override
	public void render(Treeitem item, Object data, int index) throws Exception {
		item.setValue(data);

		// Construct treecells
		Treecell tcNamn = new Treecell(data.toString());
		Treerow tr = null;

		if (item.getTreerow() == null) {
			tr = new Treerow();
			tr.setParent(item);
		} else {
			tr = item.getTreerow();
			tr.getChildren().clear();
		}
		// Attach treecells to treerow
		tcNamn.setParent(tr);

		String nom = null;

		// si le noeud est une entité
		if (data instanceof EntiteNode) {
			EntiteNode node = (EntiteNode) data;
			nom = node.getEntite().getNom();
			
			Label nomLabel = new Label();
			nomLabel.setValue(nom);
			nomLabel.setParent(tcNamn);
			nomLabel.setStyle("font-weight:bold;color:#003399;");
			// il ne peut pas etre sélectionné
			item.setCheckable(false);
		} else if (data instanceof ChampNode) {
			ChampNode node = (ChampNode) data;
			if (node.getChamp().getChampEntite() != null) {
				nom = getLabelForChampEntite(node.getChamp().getChampEntite());
			} else if (node.getChamp().getChampAnnotation() != null) {
				nom = node.getChamp().getChampAnnotation().getNom();
			}
			
			Label nomLabel = new Label();
			nomLabel.setValue(nom);
			nomLabel.setParent(tcNamn);
		}
		
		item.setLabel(null);
		item.setOpen(false);
		
		// gère l'ouverture d'un Treeitem lors du clic sur le
		// nom de celui-ci
		tcNamn.addEventListener("onClick", new EventListener<Event>() {
			public void onEvent(Event event) throws Exception {
				Treeitem item = (Treeitem) event
					.getTarget().getParent().getParent();
				
				if (item.isOpen()) {
					item.setOpen(false);
				} else {
					item.setOpen(true);
				}
			}
		});
	}
	
	/**
	 * Méthode permettant d'afficher le nom d'un champ.
	 * @param c
	 * @return
	 */
	public String getLabelForChampEntite(ChampEntite c) {
		StringBuffer iProperty = new StringBuffer();
		iProperty.append("Champ.");
		iProperty.append(c.getEntite().getNom());
		iProperty.append(".");
		
		String champOk = "";
		// si le nom du champ finit par "Id", on le retire
		if (c.getNom().endsWith("Id")) {
			champOk = c.getNom().substring(
					0, c.getNom().length() - 2);
		} else {
			champOk = c.getNom();
		}
		iProperty.append(champOk);
		
		// on ajoute la valeur du champ
		return Labels.getLabel(iProperty.toString());
	}

}
