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
package fr.aphp.tumorotek.model.qualite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Objet persistant mappant la table FANTOME.
 * Enregistrement d'un objet 'Fantome' qui, associé 
 * à une opération permet de tracer la suppression 
 * d'un objet du système.
 * 
 * Date: 15/12/2010
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 * 
 */
@Entity
@Table(name = "FANTOME")
@NamedQueries(value = {@NamedQuery(name = "Fantome.findByNom", 
			query = "SELECT f FROM Fantome f WHERE f.nom like ?1")
})
public class Fantome {
	
	private Integer fantomeId;
	private String nom;
	private String commentaires;
	private Entite entite;
	
	public Fantome() {
	}
	
	public String toString() {
		if (this.nom != null) {
			return "{Fantome : " + this.nom + "}";
		} else {
			return "{Empty Operation}";
		}
	}

	@Id
	@Column(name = "FANTOME_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getFantomeId() {
		return fantomeId;
	}

	public void setFantomeId(Integer fId) {
		this.fantomeId = fId;
	}

	@Column(name = "NOM", nullable = false, length = 100)
	public String getNom() {
		return nom;
	}

	public void setNom(String n) {
		this.nom = n;
	}

	@Column(name = "COMMENTAIRES", nullable = true)
	public String getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(String c) {
		this.commentaires = c;
	}
	
	@ManyToOne
	@JoinColumn(name = "ENTITE_ID", nullable = false)
	public Entite getEntite() {
		return this.entite;
	}

	public void setEntite(Entite en) {
		this.entite = en;
	}
	
	/**
	 * 2 operations sont considerees egales s'ils sont de meme type
	 * et si elles sont effectue par le meme utilisateur 
	 * sur le meme objet a la meme date.
	 * @param obj est l'operation à tester.
	 * @return true si les operations sont egales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		Fantome test = (Fantome) obj;
		return (this.nom == test.nom 
					|| (this.nom != null 
					&& this.nom.equals(test.nom))) 
			&& (this.entite == test.entite || (this.entite != null 
							&& this.entite.equals(test.entite)));
	}
		
	/**
	 * Le hashcode est calculé sur l'attribut nom et la
	 * description.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashNom = 0;
		int hashEntite = 0;
		
		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.entite != null) {
			hashEntite = this.entite.hashCode();
		}
		
		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashEntite;
		
		return hash;
		
	}

}
