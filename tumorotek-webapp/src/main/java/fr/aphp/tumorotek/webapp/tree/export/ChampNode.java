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

import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un champ que l'utilisateur peut afficher dans
 * une requête.
 * Classe créée le 07/04/2011.
 * 
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public class ChampNode extends TumoTreeNode {

	private Champ champ;
	
	public ChampNode(Champ c) {
		this.champ = c;
	}

	public Champ getChamp() {
		return champ;
	}

	public void setChamp(Champ c) {
		this.champ = c;
	}

	@Override
	public void readChildren() {
	}
	
	/**
	 * C'est une feuille.
	 */
	@Override
	public boolean isLeaf() {
		
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		ChampNode node = (ChampNode) obj;
		return this.getChamp().equals(node.getChamp());
		
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashChamp = 0;
		
		if (this.champ != null) {
			hashChamp = this.champ.hashCode();
		}
		
		hash = 7 * hash + hashChamp;

		return hash;
	}

}
