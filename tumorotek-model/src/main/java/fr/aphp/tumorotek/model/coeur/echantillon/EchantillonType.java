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
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 * 
 * Objet persistant mappant la table ECHANTILLON_TYPE.
 * Classe créée le 10/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "ECHANTILLON_TYPE")
@NamedQueries(value = {@NamedQuery(name = "EchantillonType.findByType", 
			query = "SELECT e FROM EchantillonType e WHERE e.type like ?1"),
		@NamedQuery(name = "EchantillonType.findByIncaCat", 
			query = "SELECT e FROM EchantillonType e " 
				+ "WHERE e.incaCat like ?1"),
		@NamedQuery(name = "EchantillonType.findByEchantillonId", 
			query = "SELECT e FROM EchantillonType e "
				+ "left join e.echantillons h "
				+ "WHERE h.echantillonId = ?1"),
		@NamedQuery(name = "EchantillonType.findByExcludedId", 
			query = "SELECT e FROM EchantillonType e " 
				+ "WHERE e.echantillonTypeId != ?1"),
		@NamedQuery(name = "EchantillonType.findByOrder", 
			query = "SELECT e FROM EchantillonType e "
				+ "WHERE e.plateforme = ?1 ORDER BY e.type") })
public class EchantillonType implements java.io.Serializable, 
												TKThesaurusObject {
	
	private static final long serialVersionUID = 47864535434464543L;
	
	private Integer echantillonTypeId;
	private String type;
	private String incaCat;
	private Plateforme plateforme;
	
	private Set<Echantillon> echantillons;
	private Set<CouleurEntiteType> couleurEntiteTypes = 
		new HashSet<CouleurEntiteType>();
	
	/** Constructeur. */
	public EchantillonType() {
		echantillons = new HashSet<Echantillon>();
	}
	
	/**
	 * Constructeur avec paramètres.
	 * @param id .
	 * @param t .
	 * @param inca .
	 */
	public EchantillonType(Integer id, String t, String inca) {
		this.echantillonTypeId = id;
		this.type = t;
		this.incaCat = inca;
	}


	@Id
	@Column(name = "ECHANTILLON_TYPE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getEchantillonTypeId() {
		return echantillonTypeId;
	}

	public void setEchantillonTypeId(Integer eId) {
		this.echantillonTypeId = eId;
	}

	@Column(name = "TYPE", nullable = false, length = 200)
	public String getType() {
		return type;
	}

	public void setType(String t) {
		this.type = t;
	}

	@Column(name = "INCA_CAT", nullable = true, length = 10)
	public String getIncaCat() {
		return incaCat;
	}

	public void setIncaCat(String inca) {
		this.incaCat = inca;
	}
	
	@OneToMany(mappedBy = "echantillonType")
	public Set<Echantillon> getEchantillons() {
		return echantillons;
	}

	public void setEchantillons(Set<Echantillon> echants) {
		this.echantillons = echants;
	}

	@OneToMany(mappedBy = "echantillonType")
	public Set<CouleurEntiteType> getCouleurEntiteTypes() {
		return couleurEntiteTypes;
	}

	public void setCouleurEntiteTypes(Set<CouleurEntiteType> cTypes) {
		this.couleurEntiteTypes = cTypes;
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
	 * 2 objets sont considérés comme égaux s'ils ont le même type et 
	 * la même catégorie inca.
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
		EchantillonType test = (EchantillonType) obj;
		return ((this.type == test.type || (this.type != null 
				&& this.type.equals(test.type)))
				&& (this.incaCat == test.incaCat || (this.incaCat != null 
						&& this.incaCat.equals(test.incaCat)))
				&& (this.plateforme == test.plateforme 
						|| (this.plateforme != null 
						&& this.plateforme.equals(test.plateforme)))); 
	}

	/**
	 * Le hashcode est calculé sur les attributs type et incaCat.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashType = 0;
		int hashIncaCat = 0;
		int hashPF = 0;
		
		if (this.type != null) {
			hashType = this.type.hashCode();
		}
		if (this.incaCat != null) {
			hashIncaCat = this.incaCat.hashCode();
		}
		if (this.plateforme != null) {
			hashPF = this.plateforme.hashCode();
		}
		
		hash = 31 * hash + hashType;
		hash = 31 * hash + hashIncaCat;
		hash = 31 * hash + hashPF;
		
		return hash;
		
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.type != null) {
			return "{" + this.type + "}";
		} else {
			return "{Empty EchantillonType}";
		}
	}

	@Override
	@Transient
	public String getNom() {
		return getType();
	}

	@Override
	@Transient
	public Integer getId() {
		return getEchantillonTypeId();
	}

}
