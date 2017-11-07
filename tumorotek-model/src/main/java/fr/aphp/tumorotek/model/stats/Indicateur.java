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
package fr.aphp.tumorotek.model.stats;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Objet persistant mappant la table STATS_STATEMENT. Classe créée le
 * 12/03/2013.
 * 
 * @author Marc DESCHAMPS
 * @version 2.0.12
 * 
 */

@Entity
@Table(name = "STATS_INDICATEUR")
@NamedQueries(value = {
		@NamedQuery(name = "Indicateur.findBySModele", 
			query = "SELECT i FROM Indicateur i JOIN "
			+ "i.sModeleIndicateurs s WHERE s.pk.sModele = ?1 ORDER BY s.ordre"),
		@NamedQuery(name = "Indicateur.findNullSubvivisionIndicateurs", 
			query = "SELECT i FROM Indicateur i WHERE i.subdivision is null "
			+ " ORDER BY i.indicateurId"),
		@NamedQuery(name = "Indicateur.findByExcludedId", 
			query = "SELECT i FROM Indicateur i WHERE i.indicateurId != ?1"),
		@NamedQuery(name = "Indicateur.findByEntite", 
			query = "SELECT i FROM Indicateur i WHERE i.entite = ?1"),
		@NamedQuery(name = "Indicateur.findBySubdivision", 
			query = "SELECT i FROM Indicateur i WHERE i.subdivision = ?1") })
public class Indicateur implements Serializable, Comparable<Object> {

	private static final long serialVersionUID = 1184655618045474785L;
	private Integer indicateurId;
	private String nom;
	private String callingProcedure;
	private String description;
	private Entite entite;
	private Subdivision subdivision;
//	private String nature;
	private Set<SModeleIndicateur> sModeleIndicateurs = 
			new HashSet<SModeleIndicateur>();

	public Indicateur() {
		super();
	}

	public Indicateur(String nom, String desc, String call) {
		super();
		this.nom = nom;
		this.callingProcedure = call;
		this.description = desc;
	}

	@Id
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	@Column(name = "STATS_INDICATEUR_ID", unique = true, nullable = false)
	public Integer getIndicateurId() {
		return indicateurId;
	}

	public void setIndicateurId(Integer _id) {
		this.indicateurId = _id;
	}

	@Column(name = "NOM", nullable = false, length = 200)
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	@OneToMany(mappedBy = "pk.indicateur")
	public Set<SModeleIndicateur> getsModeleIndicateurs() {
		return sModeleIndicateurs;
	}

	public void setsModeleIndicateurs(Set<SModeleIndicateur> sModeleIndicateurs) {
		this.sModeleIndicateurs = sModeleIndicateurs;
	}

	@Column(name = "CALLING_PROCEDURE", nullable = false, length = 100)
	public String getCallingProcedure() {
		return callingProcedure;
	}

	public void setCallingProcedure(String callingProcedure) {
		this.callingProcedure = callingProcedure;
	}

	@Column(name = "DESCRIPTION", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*@Column(name = "NATURE", nullable = false)
	public String getNature() {
		return nature;
	}

	public void setNature(String n) {
		this.nature = n;
	}*/
	
	@ManyToOne
	@JoinColumn(name = "ENTITE_ID", nullable = true)
	public Entite getEntite() {
		return this.entite;
	}

	public void setEntite(Entite entite) {
		this.entite = entite;
	}

	
	@ManyToOne
	@JoinColumn(name = "SUBDIVISION_ID", nullable = true)
	public Subdivision getSubdivision() {
		return this.subdivision;
	}

	public void setSubdivision(Subdivision s) {
		this.subdivision= s;
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 * 
	 */
	@Override
	public String toString() {
		String resultat = "";
		if (this.nom != null) {
			return resultat += this.nom;
		} else {
			return resultat += "{Empty Requete}";
		}
	}

	public Indicateur clone() {
		Indicateur clone = new Indicateur();
		clone.setNom(this.nom);
		clone.setDescription(this.description);
		clone.setEntite(this.entite);
		return clone;
	}

	/**
	 * 2 requetes sont considérées comme égales si tous leurs attributs sont
	 * égaux. 2 requete egales ne peuvent pas avoir le même ordre.
	 * 
	 * @param obj
	 *            est le statement à tester.
	 * @return true si les statement sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (!(obj instanceof Indicateur))) {
			return false;
		}
		Indicateur st = (Indicateur) obj;

		
		return (this.nom == st.nom || (this.nom != null && this.nom
				.equals(st.nom)))
			&& (this.entite == st.entite || (this.entite != null && this.entite
					.equals(st.entite)));
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = 31 * hashCode + ((nom == null) ? 0 : nom.hashCode());
		hashCode = 31
				* hashCode
				+ ((entite == null) ? 0 : entite.hashCode());
		return hashCode;
	}

	@Override
	public int compareTo(Object o) {
		return this.getNom().compareTo(((Indicateur) o).getNom());
	}

}
