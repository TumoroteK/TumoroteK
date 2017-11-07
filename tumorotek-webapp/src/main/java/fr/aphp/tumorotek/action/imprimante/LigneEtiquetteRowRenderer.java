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
package fr.aphp.tumorotek.action.imprimante;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Box;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Champ;

/** 
 * LigneEtiquetteRowRenderer affiche dans le Row
 * les membres d'une ligne d'étiquette.
 * 
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 15/06/2011
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class LigneEtiquetteRowRenderer implements RowRenderer {
	
	public LigneEtiquetteRowRenderer() {
		
	}

	@Override
	public void render(Row row, Object data, int index) throws Exception {
		LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) data;
		LigneEtiquette le = deco.getLigneEtiquette();
		
		// image edit
		Div divEdit = new Div();
		divEdit.setAlign("center");
		divEdit.setParent(row);
		Image edit = new Image();
		edit.setSrc("/images/icones/edit.png");
		edit.setWidth("12px");
		edit.setHeight("12px");
		edit.setStyle("cursor:pointer");
		edit.addForward(null, edit.getParent(), "onClickEditLigne", deco);
		edit.setParent(divEdit);
		
		// si la ligne est un code-barres
		if (le.getIsBarcode() != null
				&& le.getIsBarcode()) {
			Box box = new Box();
			box.setOrient("horizontal");
			Image img = new Image();
			img.setSrc("/images/icones/codeBarres.png");
			img.setParent(box);
			box.setParent(row);
			formateChampsForLigne(deco, row, box).setParent(box);
			row.setSpans("1,2,1,1,1");
			row.setAlign("center");
		} else {
			String italicStyle = "font-style : italic;";
			String boldStyle = "font-weight : bold;";
			
			// s'il y a une entete
			if (le.getEntete() != null) {
				Label enteteLabel = new Label(le.getEntete());
				if (le.getStyle().equals("BOLD")) {
					enteteLabel.setStyle(boldStyle);
				} else if (le.getStyle().equals("ITALIC")) {
					enteteLabel.setStyle(italicStyle);
				}
				enteteLabel.setParent(row);
			} else {
				row.setSpans("1,2,1,1,1");
				row.setAlign("center");
			}
			
			// contenu
			if (le.getContenu() != null) {
				Label contenuLabel = new Label(le.getContenu());
				if (le.getStyle().equals("BOLD")) {
					contenuLabel.setStyle(boldStyle);
				} else if (le.getStyle().equals("ITALIC")) {
					contenuLabel.setStyle(italicStyle);
				}
				contenuLabel.setParent(row);
			} else {
				formateChampsForLigne(deco, row, row).setParent(row);
			}
		}
		
		// div up
		Div up = new Div();
		up.setWidth("12px");
		up.setHeight("12px");
		up.setSclass("upArrow");
		up.setStyle("cursor:pointer");
		up.addForward("onClick", up.getParent(), "onClickUpLigne", deco);
		up.setParent(row);
		
		// div down
		Div down = new Div();
		down.setWidth("12px");
		down.setHeight("12px");
		down.setSclass("downArrow");
		down.setStyle("cursor:pointer");
		down.addForward("onClick", down.getParent(), 
				"onClickDownLigne", deco);
		down.setParent(row);
		
		// div delete
		Div divDelete = new Div();
		divDelete.setAlign("center");
		divDelete.setParent(row);
		Div delete = new Div();
		delete.setWidth("12px");
		delete.setHeight("12px");
		delete.setSclass("gridDelete");
		delete.setStyle("cursor:pointer");
		delete.addForward(null, delete.getParent(), 
				"onClickDeleteLigne", deco);
		delete.setParent(divDelete);
	}
	
	public Component formateChampsForLigneOld(LigneEtiquetteDecorator deco,
			Row row, Component parent) {
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < deco.getChamps().size(); i++) {
			Champ chp = deco.getChamps().get(i).getChamp();
			StringBuffer sb = new StringBuffer();
			if (chp.getChampAnnotation() != null) {
				sb.append(chp.getChampAnnotation().getNom());
				sb.append(" [");
				sb.append(deco.getChamps().get(i)
						.getEntite().getNom());
				sb.append("]");
			} else if (chp.getChampEntite() != null) {
				sb.append(ObjectTypesFormatters
						.getLabelForChampEntite(chp.getChampEntite()));
				sb.append(" [");
				sb.append(deco.getChamps().get(i)
						.getEntite().getNom());
				sb.append("]");
			}
			values.add(sb.toString());
		}
		
		if (values.size() > 0) {
			String italicStyle = "font-style : italic;";
			String boldStyle = "font-weight : bold;";
			Label champsLabel = new Label(values.get(0));
			if (deco.getLigneEtiquette().getStyle() != null) {
				if (deco.getLigneEtiquette().getStyle()
						.equals("BOLD")) {
					champsLabel.setStyle(boldStyle);
				} else if (deco.getLigneEtiquette()
						.getStyle().equals("ITALIC")) {
					champsLabel.setStyle(italicStyle);
				}
			}
			if (values.size() > 1) {
				
				Hbox labelAndLinkBox = new Hbox();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				Popup malPopUp = new Popup();
				malPopUp.setParent(labelAndLinkBox);
				
				Label lab;
				Vbox popupVbox = new Vbox();
				String labStr;
				for (int i = 0; i < values.size(); i++) {
					labStr = values.get(i);
					lab = new Label(labStr);
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}
				
				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(champsLabel);
				labelAndLinkBox.appendChild(moreLabel);
				return labelAndLinkBox;
			} else {
				return champsLabel;
			}
		} else {
			return new Label();
		}
	}
	
	public Component formateChampsForLigne(LigneEtiquetteDecorator deco,
			Row row, Component parent) {
		List<String> valuesEchans = new ArrayList<String>();
		List<String> valuesDerives = new ArrayList<String>();
		for (int i = 0; i < deco.getChamps().size(); i++) {
			Champ chp = deco.getChamps().get(i).getChamp();
			StringBuffer sb = new StringBuffer();
			if (chp.getChampEntite() != null) {
				sb.append(ObjectTypesFormatters
						.getLabelForChampEntite(
								chp.getChampEntite()));				
				if (deco.getChamps().get(i).getEntite()
						.getNom().equals("Echantillon")) {
					valuesEchans.add(sb.toString());
				} else {
					valuesDerives.add(sb.toString());
				}
			}
		}
		
		if (valuesEchans.size() + valuesDerives.size() > 0) {
			String italicStyle = "font-style : italic;";
			String boldStyle = "font-weight : bold;";
			Label champsLabel;
			StringBuffer sbTmp = new StringBuffer();
			if (valuesEchans.size() > 0) {
				sbTmp.append(valuesEchans.get(0));
				sbTmp.append(" [Echantillon]");
				champsLabel = new Label(sbTmp.toString());
			} else {
				sbTmp.append(valuesDerives.get(0));
				sbTmp.append(" [ProdDerive]");
				champsLabel = new Label(sbTmp.toString());
			}
			if (deco.getLigneEtiquette().getStyle() != null) {
				if (deco.getLigneEtiquette().getStyle()
						.equals("BOLD")) {
					champsLabel.setStyle(boldStyle);
				} else if (deco.getLigneEtiquette()
						.getStyle().equals("ITALIC")) {
					champsLabel.setStyle(italicStyle);
				}
			}
			if (valuesEchans.size() + valuesDerives.size() > 1) {
				
				Hbox labelAndLinkBox = new Hbox();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				Popup malPopUp = new Popup();
				malPopUp.setParent(labelAndLinkBox);
				
				// affichage des données des échantillons
				Label lab;
				Vbox popupVbox = new Vbox();
				StringBuffer labStr = new StringBuffer();
				for (int i = 0; i < valuesEchans.size(); i++) {
					labStr.append(valuesEchans.get(i));
					if (i + 1 < valuesEchans.size()) {
						labStr.append(", ");
					} else {
						labStr.append(" [Echantillon]");
					}
				}
				if (!labStr.toString().equals("")) {
					lab = new Label(labStr.toString());
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}
				
				// affichage des données des dérivés
				labStr = new StringBuffer();
				for (int i = 0; i < valuesDerives.size(); i++) {
					labStr.append(valuesDerives.get(i));
					if (i + 1 < valuesDerives.size()) {
						labStr.append(", ");
					} else {
						labStr.append(" [ProdDerive]");
					}
				}
				if (!labStr.toString().equals("")) {
					lab = new Label(labStr.toString());
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}
				
				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(champsLabel);
				labelAndLinkBox.appendChild(moreLabel);
				return labelAndLinkBox;
			} else {
				return champsLabel;
			}
		} else {
			return new Label();
		}
	}

}
