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
package fr.aphp.tumorotek.action.interfacage;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

/**
 * @version 2.0.13
 * @author Mathieu BARTHELEMY
 *
 */
public class ValeurExterneRowRenderer implements RowRenderer<ValeurExterneDecorator> {

	@Override
	public void render(Row row, ValeurExterneDecorator data, int index) throws Exception {
		ValeurExterneDecorator deco = data;
		
		// Nom du champ
		Label nomLabel = new Label(deco.getNomChamp());
		nomLabel.setStyle("font-weight: bold;");
		nomLabel.setParent(row);
		
		// Ancienne valeur
		Label oldLabel = new Label(deco.getOldValue());
		oldLabel.setParent(row);
		
		// Image
		Image img = new Image();
		img.setSrc("/images/icones/back.png");
		img.setParent(row);
		
		// Nouvelle valeur
		Label newLabel = new Label(deco.getNewValue());
		newLabel.setParent(row);
		
		// check box d'import
		Checkbox check = new Checkbox();
		check.setChecked(deco.isImporter());
		check.addForward("onCheck", 
				check.getParent(), 
				"onCheckValeurExterne", deco);
		check.setParent(row);
		if (deco.isImporter()) {
			row.setStyle(null);
		} else {
			row.setStyle("background-color : #E5E5E5;");
		}
	}

}
