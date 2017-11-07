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

import fr.aphp.tumorotek.decorator.CritereDecorator;
import fr.aphp.tumorotek.model.io.export.Critere;

/**
 * Classe abstraite représentant un noeud d'un arbre RequeteModel.
 * Classe créée le 09/02/10.
 * 
 * @author GOUSSEAU Maxime.
 * @version 2.0.
 *
 */
public class CritereNode extends ExportNode {
	
private Critere critere;
	
	public CritereNode(Critere serv, GroupementNode p) {
		this.critere = serv;
		this.parent = p;
	}

	public Critere getCritere() {
		return critere;
	}

	public void setCritere(Critere s) {
		this.critere = s;
	}
	
	/**
	 * Ce n'est pas une feuille.
	 */
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	 @Override
	 public boolean equals(Object obj) {
		 boolean retour = false;
		 if (obj instanceof CritereNode) {
			 return ((CritereNode) obj).getCritere().equals(this.getCritere());
		 }
		 return retour;
	 }
	 
	 @Override
	 public String toString() {
		return new CritereDecorator(getCritere()).getLabel();
	}
	 
	 @Override
	public String getWidth() {
		return 30 * (getParentCount() - 1) + "px";
	}

	@Override
	public String getSclass() {
		return null;
	}

}
