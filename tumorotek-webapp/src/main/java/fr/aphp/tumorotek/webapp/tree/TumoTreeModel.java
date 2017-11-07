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
package fr.aphp.tumorotek.webapp.tree;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

/**
 * Classe gérant les trees qui utilisés dans TK. Elle reprend les
 * méthodes de base de la classe AbstractTreeModel.
 * Classe créée le 16/12/09.
 * 
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public class TumoTreeModel extends AbstractTreeModel<TumoTreeNode> {

	private static final long serialVersionUID = -7475931891386110982L;

	/**
	 * Constructeur de l'arbre.
	 * @param rootNode Premier noeud de l'arbre(pas affiché).
	 */
	public TumoTreeModel(TumoTreeNode rootNode) {
		super(rootNode);
	}

	//@Override
	/**
	 * Renvoie l'enfant d'un item placé la position arg1. 
	 */
	public TumoTreeNode getChild(TumoTreeNode arg0, int arg1) {
		return ((TumoTreeNode) arg0).getChild(arg1);
	}

	//@Override
	/**
	 * Renvoie le nombre d'enfant d'un noeud de l'arbre.
	 */
	public int getChildCount(TumoTreeNode arg0) {
		return ((TumoTreeNode) arg0).getChildCount();
	}

	//@Override
	/**
	 * Renvoie si un noeud est une feuille ou non.
	 */
	public boolean isLeaf(TumoTreeNode arg0) {
		if (arg0 == null) {
			return true;
		}

		return ((TumoTreeNode) arg0).isLeaf();
	}
	
	/**
	 * Surcharge de la methode pour restreindre la creation du path
	 * à partir de la racine du conteneur
	 */
	public int[] getPathFromNode(TumoTreeNode child, TumoTreeNode cNode, 
													int cPath) {
		List<Integer> path = new ArrayList<Integer>();
		conteneurSearch(path, cNode, child);

		final int[] ipath = new int[path.size() + 1];
		ipath[0] = cPath;
		for (int j = 0; j < path.size(); j++) {
			ipath[j + 1] = path.get(j);
		}
		return ipath;
	}
	
	private boolean conteneurSearch(List<Integer> path, TumoTreeNode node, 
													TumoTreeNode target){
		if (node.equals(target))
			return true;

		for (int i = 0, size = node.getChildCount(); i< size; i++)
			if (conteneurSearch(path, getChild(node, i), target)) {
				path.add(0, new Integer(i));
				return true;
			}
		return false;
	}
}

