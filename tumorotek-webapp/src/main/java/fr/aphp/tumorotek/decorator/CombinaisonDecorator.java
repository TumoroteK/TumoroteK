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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.model.io.export.Combinaison;

/**
 * Classe 'Decorateur' qui reprend les attributs de Entité.
 * pour les afficher dans la liste associée.
 * Decorator créé le 26/05/2010.
 *
 * @version 2.0
 * @author GOUSSEAU Maxime
 *
 */
public class CombinaisonDecorator implements Comparable {
	
	private Combinaison combinaison;
	
	public CombinaisonDecorator(Combinaison c) {
		this.combinaison = c;
	}

	public Combinaison getCombinaison() {
		return combinaison;
	}

	public void setCombinaison(Combinaison c) {
		this.combinaison = c;
	}
	
	public String getLabel() {
		return new ChampDecorator(combinaison.getChamp1()).getLabelLong() + " "
				+ combinaison.getOperateur() + " "
				+ new ChampDecorator(combinaison.getChamp2()).getLabelLong();
	}
	
	/**
	 * Decore une liste d'Combinaisons.
	 * @param combinaisons
	 * @return Combinaisons décorés.
	 */
	public static List<CombinaisonDecorator> 
								decorateListe(List<Combinaison> combinaisons) {
		List<CombinaisonDecorator> liste = 
									new ArrayList<CombinaisonDecorator>();
		Iterator<Combinaison> it = combinaisons.iterator();
		while (it.hasNext()) {
			liste.add(new CombinaisonDecorator(it.next()));
		}
		return liste;
	}
	
	/**
	 * Extrait les Contrats d'une liste de Decorator.
	 * @param Contrats
	 * @return Contrats décorés.
	 */
	public static List<Combinaison> extractListe(List<CombinaisonDecorator> combinaisons) {
		List<Combinaison> liste = 
									new ArrayList<Combinaison>();
		Iterator<CombinaisonDecorator> it = combinaisons.iterator();
		while (it.hasNext()) {
			liste.add(it.next().getCombinaison());
		}
		return liste;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		CombinaisonDecorator deco = (CombinaisonDecorator) obj;
		return this.getCombinaison().equals(deco.getCombinaison());
		
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashCombinaison = 0;
		
		if (this.combinaison != null) {
			hashCombinaison = this.combinaison.hashCode();
		}
		
		hash = 7 * hash + hashCombinaison;

		return hash;
	}
	
	@Override
	public int compareTo(Object object) {
		return compareTo((CombinaisonDecorator) object);
	}
	
	private int compareTo(CombinaisonDecorator cd) {
		return this.getLabel().compareTo(cd.getLabel());
	}

}
