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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Objet persistant mappant la table MODE_PREPA_DERIVE.
 * Classe créée le 05/01/2011.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "MODE_PREPA_DERIVE")
@NamedQueries(value = {@NamedQuery(name = "ModePrepaDerive.findByNom", 
			query = "SELECT m FROM ModePrepaDerive m WHERE m.nom like ?1"),
		@NamedQuery(name = "ModePrepaDerive.findByExcludedId", 
			query = "SELECT m FROM ModePrepaDerive m " 
				+ "WHERE m.modePrepaDeriveId != ?1"),
		@NamedQuery(name = "ModePrepaDerive.findByOrder", 
			query = "SELECT m FROM ModePrepaDerive m "
				+ "WHERE m.plateforme = ?1 ORDER BY m.nom") })
public class ModePrepaDerive implements Serializable, TKThesaurusObject {
	private static final long serialVersionUID = 5348645345465465L;
	
	private Integer modePrepaDeriveId;
	private String nom;
	private String nomEn;
	private Plateforme plateforme;
	
	private Set<ProdDerive> prodDerives;
	
	/** Constructeur par défaut. */
	public ModePrepaDerive() {
		prodDerives = new HashSet<ProdDerive>();
	}
	

	@Id
	@Column(name = "MODE_PREPA_DERIVE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getModePrepaDeriveId() {
		return modePrepaDeriveId;
	}

	public void setModePrepaDeriveId(Integer mId) {
		this.modePrepaDeriveId = mId;
	}

	@Column(name = "NOM", nullable = false, length = 200)
	public String getNom() {
		return nom;
	}

	public void setNom(String n) {
		this.nom = n;
	}

	@Column(name = "NOM_EN", nullable = true, length = 25)
	public String getNomEn() {
		return nomEn;
	}

	public void setNomEn(String e) {
		this.nomEn = e;
	}
	
	@OneToMany(mappedBy = "modePrepaDerive")
	public Set<ProdDerive> getProdDerives() {
		return prodDerives;
	}


	public void setProdDerives(Set<ProdDerive> p) {
		this.prodDerives = p;
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
	 * 2 préparations sont considérées comme égales si elles ont le même nom 
	 * et la même plateforme.
	 * @param obj est la préparation à tester.
	 * @return true si les préparations sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		ModePrepaDerive test = (ModePrepaDerive) obj;
		return ((this.nom == test.nom || (this.nom != null 
				&& this.nom.equals(test.nom))) 
				&& (this.plateforme == test.plateforme 
						|| (this.plateforme != null 
						&& this.plateforme.equals(test.plateforme))));
	}

	/**
	 * Le hashcode est calculé sur les attributs nom
	 * et plateforme.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashNom = 0;
		int hashPF = 0;
		
		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.plateforme != null) {
			hashPF = this.plateforme.hashCode();
		}
		
		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashPF;
		
		return hash;
		
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.nom != null) {
			return "{" + this.nom + "}";
		} else {
			return "{Empty ModePrepaDerive}";
		}
	}


	@Override
	@Transient
	public Integer getId() {
		return getModePrepaDeriveId();
	}
}
