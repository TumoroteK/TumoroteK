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
package fr.aphp.tumorotek.action.listmodel;

import java.util.ArrayList;
import java.util.List;


import fr.aphp.tumorotek.action.io.RechercheUtils;
import fr.aphp.tumorotek.action.io.ResultatRow;
import fr.aphp.tumorotek.model.io.export.Affichage;

/**
 * ListModel qui permette de charger le contenu d'une page de resultats 
 * renvoyés par une recherche complexe. La demande d'une page affiche 
 * à partir de la liste d'objets la matrice affichable.
 * @see http://books.zkoss.org/wiki/Small_Talks/2009/July/Handling_huge_data_using_ZK
 * Date: 15/02/2013
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 */
public class GridResultsModel extends ObjectPagingModel {

	public GridResultsModel(int startPageNumber, int pageSize,
			List<Object> objs, Affichage aff) {
		super(startPageNumber, pageSize, objs, null, aff);
	}


	private static final long serialVersionUID = 6613208067174831719L;
		

	@Override
	protected List<? extends Object> getPageData(int itemStartNumber, 
									int pageSize) {
		List<List<Object>> matriceObjets = 
								new ArrayList<List<Object>>();
		
		int end = itemStartNumber + pageSize;
		if (end >= getObjets().size()) {
			end = getObjets().size();
		}
		
		for (int i = itemStartNumber; i < end; i ++) {
			matriceObjets
				.add(RechercheUtils
				.getListeObjetsCorrespondants(getObjets().get(i), 
						getAffichage(), null));
		}

		List<List<Object>> matriceAffichable = 
								new ArrayList<List<Object>>();
		
		/** On charge la matrice affichable. */
		RechercheUtils.loadMatriceAffichable(matriceObjets, 
								matriceAffichable, getAffichage());
		
		// On supprime les doublons de la liste
		for (int i = 0; i < matriceAffichable.size(); i++) {
			List<Object> lo = matriceAffichable.get(i);
			for (int j = i + 1; j < matriceAffichable.size(); j++) {
				List<Object> loTemp = matriceAffichable.get(j);
				if (lo.size() == loTemp.size()) {
					boolean egale = true;
					for (int k = 0; k < lo.size(); k++) {
						if ((lo.get(k) == null && loTemp.get(k) != null)
								|| (lo.get(k) != null && !lo.get(k).equals(
										loTemp.get(k)))) {
							egale = false;
							break;
						}
					}
					if (egale) {
						matriceAffichable.remove(j);
						matriceObjets.remove(j);
						j--;
					}
				}
			}
		}
		/** On trie les matrices. */
		// sortMatrices();
		
		List<ResultatRow> rows = new ArrayList<ResultatRow>();
		for (int i = 0; i < matriceAffichable.size(); i++) {
			rows.add(new ResultatRow(matriceAffichable.get(i),
					matriceObjets.get(i)));
		}
		return rows;
	}
	
	
	
	

}
