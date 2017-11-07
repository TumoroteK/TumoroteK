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
package fr.aphp.tumorotek.action;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;

/**
 * Classe récupérée sur le forum de Zk qui extends la classe SimpleListModel.
 * Elle permet de rendre les Combobox non case-sensitive.
 * 
 * @author Pierre Ventadour.
 *
 */
public class CustomSimpleListModel extends SimpleListModel {

	private static final long serialVersionUID = 7061490413290014532L;

	public CustomSimpleListModel(List data) {
        super(data);
    }
	
	public CustomSimpleListModel(Object[] data) {
        super(data);
    }

	public ListModel getSubModel(Object value, int nRows) {
        final String idx = value == null ? "" : objectToString(value);
        if (nRows < 0) {
            nRows = 20;
        }
        final LinkedList data = new LinkedList();
        for (int i = 0; i < getSize(); i++) {
            if (idx.equals("")
                    || entryMatchesText(getElementAt(i).toString(), idx)) {
                data.add(getElementAt(i));
                if (--nRows <= 0) {
                    break; // done
                }
            }
        }
        return new CustomSimpleListModel(data);
    }

	public boolean entryMatchesText(String entry, String text) {
    	return entry.toLowerCase().startsWith(text.toLowerCase());
	}

}
