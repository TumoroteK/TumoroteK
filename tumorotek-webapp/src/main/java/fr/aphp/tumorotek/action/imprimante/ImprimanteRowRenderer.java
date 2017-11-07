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

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.imprimante.Imprimante;

/** 
 * ImprimanteRowRenderer affiche dans le Row
 * les membres d'Imprimante sous forme de labels.
 * 
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 21/03/2011
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class ImprimanteRowRenderer implements RowRenderer {
	
	public ImprimanteRowRenderer() {
		
	}

	@Override
	public void render(Row row, Object data, int index) throws Exception {
		Imprimante imp = (Imprimante) data;
		
		// nom
		Label nomLabel = new Label(imp.getNom());
		nomLabel.addForward(null, nomLabel.getParent(), 
					"onClickImprimante", imp);
		nomLabel.setClass("formLink");
		nomLabel.setParent(row);
		
		// abscisse
		if (imp.getAbscisse() != null) {
			new Label(String.valueOf(imp.getAbscisse())).setParent(row);
		} else {
			new Label(null).setParent(row);
		}
		
		// ordonnee
		if (imp.getOrdonnee() != null) {
			new Label(String.valueOf(imp.getOrdonnee())).setParent(row);
		} else {
			new Label(null).setParent(row);
		}
		
		// largeur
		if (imp.getLargeur() != null) {
			new Label(String.valueOf(imp.getLargeur())).setParent(row);
		} else {
			new Label(null).setParent(row);
		}
		
		// longueur
		if (imp.getLongueur() != null) {
			new Label(String.valueOf(imp.getLongueur())).setParent(row);
		} else {
			new Label(null).setParent(row);
		}
		
		// orientation
		if (imp.getOrientation() != null) {
			String tmp = "";
			if (imp.getOrientation() == 1) {
				tmp = Labels.getLabel("Champ.Imprimante.Orientation.portrait");
			} else {
				tmp = Labels.getLabel("Champ.Imprimante.Orientation.paysage");
			}
			new Label(String.valueOf(tmp)).setParent(row);
		} else {
			new Label(null).setParent(row);
		}
		
		// api
		if (imp.getImprimanteApi() != null) {
			new Label(String.valueOf(imp.getImprimanteApi().getNom()))
				.setParent(row);
		} else {
			new Label(null).setParent(row);
		}
		
		// delete
		Div divDelete = new Div();
		divDelete.setWidth("25px");
		divDelete.setAlign("center");
		Div divImage = new Div();
		divImage.setWidth("12px");
		divImage.setHeight("12px");
		divImage.setSclass("gridDelete");
		divImage.addForward("onClick", divDelete.getParent(), 
				"onClickDeleteImprimante", imp);
		divImage.setParent(divDelete);
		divDelete.setParent(row);
	}

}
