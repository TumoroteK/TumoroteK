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
package fr.aphp.tumorotek.model.imprimante;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * Objet persistant mappant la table IMPRIMANTE_API.
 * Classe créée le 14/12/2010.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "IMPRIMANTE_API")
@NamedQueries(value = {@NamedQuery(name = "ImprimanteApi.findByOrder",
			query = "SELECT i FROM ImprimanteApi i ORDER BY i.nom") })
public class ImprimanteApi implements Serializable {

	private static final long serialVersionUID = -1697659518925168511L;

	private Integer imprimanteApiId;
	private String nom;
	
	private Set<Imprimante> imprimantes = new HashSet<Imprimante>();
	
	public ImprimanteApi() {
		super();
	}
	
	@Id
	@Column(name = "IMPRIMANTE_API_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getImprimanteApiId() {
		return this.imprimanteApiId;
	}

	public void setImprimanteApiId(Integer id) {
		this.imprimanteApiId = id;
	}

	@Column(name = "NOM", nullable = false, length = 50)
	public String getNom() {
		return this.nom;
	}

	public void setNom(String n) {
		this.nom = n;
	}

	@OneToMany(mappedBy = "imprimanteApi")
	public Set<Imprimante> getImprimantes() {
		return imprimantes;
	}

	public void setImprimantes(Set<Imprimante> i) {
		this.imprimantes = i;
	}
	
	/**
	 * 2 unités sont considérées comme égales si elles ont le même type
	 * et la même unité.
	 * @param obj est l'unité à tester.
	 * @return true si les unités sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		ImprimanteApi test = (ImprimanteApi) obj;
		return ((this.nom == test.nom
				|| (this.nom != null 
						&& this.nom.equals(test.nom))) 
		);
	}

	/**
	 * Le hashcode est calculé sur les attributs unite et type.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashNom = 0;
		
		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		
		hash = 31 * hash + hashNom;
		
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
			return "{Empty ImprimanteApi}";
		}
	}
}
