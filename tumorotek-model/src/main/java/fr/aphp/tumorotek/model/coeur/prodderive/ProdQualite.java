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
package fr.aphp.tumorotek.model.coeur.prodderive;

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
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Objet persistant mappant la table PROD_QUALITE.
 * Classe créée le 14/09/09.
 * 
 * @author Maxime Gousseau
 * @version 2.0
 * 
 */
@Entity
@Table(name = "PROD_QUALITE")
@NamedQueries(value = {@NamedQuery(name = "ProdQualite.findByProdQualite", 
			query = "SELECT p FROM ProdQualite p WHERE p.prodQualite like ?1"),
		@NamedQuery(name = "ProdQualite.findByProdDeriveId", 
			query = "SELECT p FROM ProdQualite p " 
				+ "left join p.prodDerives d " 
				+ "WHERE d.prodDeriveId = ?1"),
		@NamedQuery(name = "ProdQualite.findByExcludedId", 
			query = "SELECT p FROM ProdQualite p " 
				+ "WHERE p.prodQualiteId != ?1"),
		@NamedQuery(name = "ProdQualite.findByOrder", 
			query = "SELECT p FROM ProdQualite p "
				+ "WHERE p.plateforme = ?1 ORDER BY p.prodQualite") })
public class ProdQualite implements Serializable, TKThesaurusObject {
	
	private static final long serialVersionUID = -4115872442854900525L;
	
	private Integer prodQualiteId;
	private String prodQualite;
	private Plateforme plateforme;
	
	private Set<ProdDerive> prodDerives;
	
	/** Constructeur par défaut. */
	public ProdQualite() {
		super();
		prodDerives = new HashSet<ProdDerive>();
	}

	@Id
	@Column(name = "PROD_QUALITE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getProdQualiteId() {
		return this.prodQualiteId;
	}

	public void setProdQualiteId(Integer id) {
		this.prodQualiteId = id;
	}

	@Column(name = "PROD_QUALITE", nullable = false, length = 200)
	public String getProdQualite() {
		return this.prodQualite;
	}

	public void setProdQualite(String qualite) {
		this.prodQualite = qualite;
	}

	@OneToMany(mappedBy = "prodQualite")
	public Set<ProdDerive> getProdDerives() {
		return prodDerives;
	}

	public void setProdDerives(Set<ProdDerive> prodDs) {
		this.prodDerives = prodDs;
	}
	
	@Override
	@ManyToOne
	@JoinColumn(name = "PLATEFORME_ID", nullable = false)
	public Plateforme getPlateforme() {
		return plateforme;
	}

	@Override
	public void setPlateforme(Plateforme pf) {
		this.plateforme = pf;
	}

	/**
	 * 2 objets sont considérés comme égaux s'ils ont la même qualité et 
	 * la même plateforme.
	 * @param obj est l'objet à tester.
	 * @return true si les objets sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		ProdQualite test = (ProdQualite) obj;
		return ((this.prodQualite == test.prodQualite 
				|| (this.prodQualite != null  
					&& this.prodQualite.equals(test.prodQualite))) 
			&& (this.plateforme == test.plateforme 
					|| (this.plateforme != null 
					&& this.plateforme.equals(test.plateforme)))
	);
	}

	/**
	 * Le hashcode est calculé sur les attributs prodQualite et plateforme.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashQualite = 0;
		int hashPF = 0;
		
		if (this.prodQualite != null) {
			hashQualite = this.prodQualite.hashCode();
		}
		if (this.plateforme != null) {
			hashPF = this.plateforme.hashCode();
		}
		
		hash = 31 * hash + hashQualite;
		hash = 31 * hash + hashPF;
		
		return hash;
		
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.prodQualite != null) {
			return "{" + this.prodQualite + "}";
		} else {
			return "{Empty ProdQualite}";
		}
	}
	
	@Override
	@Transient
	public String getNom() {
		return getProdQualite();
	}

	@Override
	@Transient
	public Integer getId() {
		return getProdQualiteId();
	}
}
