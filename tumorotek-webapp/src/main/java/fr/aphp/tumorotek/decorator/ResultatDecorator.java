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

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Resultat;

/**
 * Classe 'Decorateur' qui reprend les attributs de Entité.
 * pour les afficher dans la liste associée.
 * Decorator créé le 26/05/2010.
 *
 * @version 2.0
 * @author GOUSSEAU Maxime
 *
 */
public class ResultatDecorator implements Comparable {
	
	private Resultat resultat;
	
	public ResultatDecorator(Resultat r) {
		this.resultat = r;
	}

	public Resultat getResultat() {
		return resultat;
	}

	public void setResultat(Resultat r) {
		this.resultat = r;
	}
	
	public String getNomColonne() {
		return this.resultat.getNomColonne();
	}
	
	public String getChamp() {
		return new ChampDecorator(resultat.getChamp()).getLabelLong();		
	}
	
	/**
	 * Decore une liste d'Resultats.
	 * @param resultats
	 * @return Resultats décorés.
	 */
	public static List<ResultatDecorator> 
								decorateListe(List<Resultat> resultats) {
		List<ResultatDecorator> liste = 
									new ArrayList<ResultatDecorator>();
		Iterator<Resultat> it = resultats.iterator();
		while (it.hasNext()) {
			liste.add(new ResultatDecorator(it.next()));
		}
		return liste;
	}
	
	/**
	 * Extrait les Contrats d'une liste de Decorator.
	 * @param Contrats
	 * @return Contrats décorés.
	 */
	public static List<Resultat> extractListe(List<ResultatDecorator> resultats) {
		List<Resultat> liste = 
									new ArrayList<Resultat>();
		Iterator<ResultatDecorator> it = resultats.iterator();
		while (it.hasNext()) {
			liste.add(it.next().getResultat());
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
		
		ResultatDecorator deco = (ResultatDecorator) obj;
		return this.getResultat().equals(deco.getResultat());
		
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashResultat = 0;
		
		if (this.resultat != null) {
			hashResultat = this.resultat.hashCode();
		}
		
		hash = 7 * hash + hashResultat;

		return hash;
	}
	
	@Override
	public int compareTo(Object object) {
		return compareTo((ResultatDecorator) object);
	}
	
	private int compareTo(ResultatDecorator cd) {
		return this.getResultat().compareTo(cd.getResultat());
	}
	
	public boolean getVisibleLabel() {
		return (this.resultat.getResultatId() != null);
	}
	
	public boolean getVisibleTextbox() {
		return (this.resultat.getResultatId() == null);
	}

}
