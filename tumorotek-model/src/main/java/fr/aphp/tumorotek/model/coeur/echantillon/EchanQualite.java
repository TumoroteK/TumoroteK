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
package fr.aphp.tumorotek.model.coeur.echantillon;

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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Objet persistant mappant la table ECHAN_QUALITE.
 * Classe créée le 10/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "ECHAN_QUALITE")
@NamedQueries(value = {@NamedQuery(name = "EchanQualite.findByQualite", 
			query = "SELECT e FROM EchanQualite e " 
				+ "WHERE e.echanQualite like ?1"),
		@NamedQuery(name = "EchanQualite.findByEchantillonId", 
			query = "SELECT e FROM EchanQualite e " 
				+ "left join e.echantillons h "
				+ "WHERE h.echantillonId = ?1"),
		@NamedQuery(name = "EchanQualite.findByExcludedId", 
			query = "SELECT e FROM EchanQualite e " 
				+ "WHERE e.echanQualiteId != ?1"),
		@NamedQuery(name = "EchanQualite.findByOrder", 
			query = "SELECT e FROM EchanQualite e "
				+ "WHERE e.plateforme = ?1 ORDER BY e.echanQualite") })
public class EchanQualite implements java.io.Serializable, TKThesaurusObject {
	
	private static final long serialVersionUID = 768431365534341L;
	
	private Integer echanQualiteId;
	private String echanQualite;
	private Plateforme plateforme;
	
	private Set<Echantillon> echantillons;
	
	/** Constructeur par défaut. */
	public EchanQualite() {
		echantillons = new HashSet<Echantillon>();
	}
	
	/**
	 * Constructeur avec paramètres.
	 * @param id .
	 * @param q .
	 */
	public EchanQualite(Integer id, String q) {
		this.echanQualiteId = id;
		this.echanQualite = q;
	}


	@Id
	@Column(name = "ECHAN_QUALITE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getEchanQualiteId() {
		return echanQualiteId;
	}

	public void setEchanQualiteId(Integer eId) {
		this.echanQualiteId = eId;
	}

	@Column(name = "ECHAN_QUALITE", nullable = false, length = 200)
	public String getEchanQualite() {
		return echanQualite;
	}

	public void setEchanQualite(String q) {
		this.echanQualite = q;
	}
	
	@OneToMany(mappedBy = "echanQualite")
	public Set<Echantillon> getEchantillons() {
		return echantillons;
	}

	public void setEchantillons(Set<Echantillon> echants) {
		this.echantillons = echants;
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
		EchanQualite test = (EchanQualite) obj;
		return ((this.echanQualite == test.echanQualite 
					|| (this.echanQualite != null  
						&& this.echanQualite.equals(test.echanQualite))) 
				&& (this.plateforme == test.plateforme 
						|| (this.plateforme != null 
						&& this.plateforme.equals(test.plateforme)))
		);
	}

	/**
	 * Le hashcode est calculé sur les attributs echanQualite et plateforme.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashQualite = 0;
		int hashPF = 0;
		
		if (this.echanQualite != null) {
			hashQualite = this.echanQualite.hashCode();
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
		if (this.echanQualite != null) {
			return "{" + this.echanQualite + "}";
		} else {
			return "{Empty EchanQualite}";
		}
	}

	@Override
	@Transient
	public String getNom() {
		return getEchanQualite();
	}

	@Override
	@Transient
	public Integer getId() {
		return getEchanQualiteId();
	}

}
