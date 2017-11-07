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
package fr.aphp.tumorotek.webapp.tree.stockage;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Space;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * Classe gérant l'affichage des informations dans un arbre de type 
 * TumoTreeModel contenant les stockages.
 * @author Pierre Ventadour
 *
 */
public class StockageTreeItemRenderer implements TreeitemRenderer {
	
	private static String imageSrc = "/images/icones/boites/pastille";

	@Override
	public void render(Treeitem item, Object data, int index) throws Exception {
		item.setValue(data);
		
		item.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) throws Exception {
			}
		});

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
		
		if (data instanceof ConteneurNode) {
			ConteneurNode node = (ConteneurNode) data;
			
			// on affiche le nom du conteneur
			org.zkoss.zul.Label nomLabel = new org.zkoss.zul.Label();
			nomLabel.setValue(node.getLibelle());
			nomLabel.setParent(tcNamn);
			nomLabel.setStyle("font-weight:bold;");
			
		} else if (data instanceof EnceinteNode) {
			EnceinteNode node = (EnceinteNode) data;
			
			if (!node.isVide()) {
				// affichage de la couleur de l'enceinte
				if (node.getEnceinte().getCouleur() != null) {
					Space spaceBefore = new Space();
					spaceBefore.setWidth("2px");
					spaceBefore.setHeight("1px");
					spaceBefore.setParent(tcNamn);
					Image img = new Image();
					StringBuffer src = new StringBuffer();
					src.append(imageSrc);
					src.append(node.getEnceinte().getCouleur().getCouleur());
					src.append(".png");
					img.setSrc(src.toString());
					img.setParent(tcNamn);
					Space spaceAfter = new Space();
					spaceAfter.setWidth("5px");
					spaceAfter.setHeight("1px");
					spaceAfter.setParent(tcNamn);
				}
				
				// nom de l'enceinte
				org.zkoss.zul.Label nomLabel = new org.zkoss.zul.Label();
				nomLabel.setValue(node.getEnceinte().getNom());
				nomLabel.setParent(tcNamn);
				
				// alias
				if (node.getEnceinte().getAlias() != null 
					&& !node.getEnceinte().getAlias().equals("")) {
					StringBuffer sb = new StringBuffer();
					sb.append(" ");
					sb.append(node.getEnceinte().getAlias());
					org.zkoss.zul.Label aliasLabel = new org.zkoss.zul.Label();
					aliasLabel.setValue(sb.toString());
					aliasLabel.setStyle("color:#07811C;font-style:italic;");
					aliasLabel.setParent(tcNamn);
				}
				
				// nb d'emplacements libres
				StringBuffer sb = new StringBuffer();
				sb.append(" [");
				sb.append(node.getNbEmplacementsLibres());
				if (node.getPourcentage() != null) {
					sb.append(" - ");
					sb.append(ObjectTypesFormatters.floor(
							node.getPourcentage(), 1));
					sb.append("%");
				}
				sb.append("]");
				org.zkoss.zul.Label numLabel = new org.zkoss.zul.Label();
				numLabel.setValue(sb.toString());
				numLabel.setParent(tcNamn);
			} else {
				org.zkoss.zul.Label videLabel = new org.zkoss.zul.Label();
				videLabel.setValue(
						Labels.getLabel("stockage.position.libre"));
				videLabel.setStyle("font-style:italic;color:#7F7F7F;");
				videLabel.setParent(tcNamn);
			}
			
			if (!node.childrenAvailable()) {
				tcNamn.setStyle("color:#cf3708;");
			}
		} else if (data instanceof TerminaleNode) {
			TerminaleNode node = (TerminaleNode) data;
			
			if (!node.isVide()) {
				// affichage de la couleur de la boite
				if (node.getTerminale().getCouleur() != null) {
					Space spaceBefore = new Space();
					spaceBefore.setWidth("2px");
					spaceBefore.setHeight("1px");
					spaceBefore.setParent(tcNamn);
					Image img = new Image();
					StringBuffer src = new StringBuffer();
					src.append(imageSrc);
					src.append(node.getTerminale().getCouleur().getCouleur());
					src.append(".png");
					img.setSrc(src.toString());
					img.setParent(tcNamn);
					Space spaceAfter = new Space();
					spaceAfter.setWidth("5px");
					spaceAfter.setHeight("1px");
					spaceAfter.setParent(tcNamn);
				}
				
				// nom de la terminale
				org.zkoss.zul.Label nomLabel = new org.zkoss.zul.Label();
				nomLabel.setValue(node.getTerminale().getNom());
				nomLabel.setParent(tcNamn);
				
				// alias
				if (node.getTerminale().getAlias() != null 
					&& !node.getTerminale().getAlias().equals("")) {
					StringBuffer sb = new StringBuffer();
					sb.append(" ");
					sb.append(node.getTerminale().getAlias());
					org.zkoss.zul.Label aliasLabel = new org.zkoss.zul.Label();
					aliasLabel.setValue(sb.toString());
					aliasLabel.setStyle("color:#07811C;font-style:italic;");
					aliasLabel.setParent(tcNamn);
				}
				
				// nb d'emplacements libres et % de remplissage
				StringBuffer sb = new StringBuffer();
				sb.append(" [");
				Long nbLibres = ManagerLocator.getTerminaleManager()
					.getNumberEmplacementsLibresManager(node.getTerminale());
				Long nbOccupes = ManagerLocator.getTerminaleManager()
					.getNumberEmplacementsOccupesManager(node.getTerminale());
				sb.append(nbLibres);
				Long total = nbLibres + nbOccupes;
				if (total != 0) {
					Float puiss = nbOccupes.floatValue() * 100;
					Float pourcentage = puiss / total.floatValue();
					sb.append(" - ");
					sb.append(ObjectTypesFormatters.floor(pourcentage, 1));
					sb.append("%");
				}
				sb.append("]");
				org.zkoss.zul.Label numLabel = new org.zkoss.zul.Label();
				numLabel.setValue(sb.toString());
				numLabel.setParent(tcNamn);
			} else {
				org.zkoss.zul.Label videLabel = new org.zkoss.zul.Label();
				videLabel.setValue(
						Labels.getLabel("stockage.position.libre"));
				videLabel.setStyle("font-style:italic;color:#7F7F7F;");
				videLabel.setParent(tcNamn);
			}
			
			if (!node.availableTerminale()) {
				tcNamn.setStyle("color:#cf3708;");
			}
		}
		
		item.setLabel(null);		
		item.setOpen(false);
		
		// gère l'ouverture d'un Treeitem lors du clic sur le
		// nom de celui-ci
		tcNamn.addEventListener("onClick", new EventListener() {
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

}
