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
package fr.aphp.tumorotek.model.coeur.cession;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table CESSION_TYPE. Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "CESSION_TYPE")
//@NamedQueries(value = {@NamedQuery(name = "CessionType.findByType", query = "SELECT c FROM CessionType c WHERE c.type like ?1"),
//   @NamedQuery(name = "CessionType.findByOrder", query = "SELECT c FROM CessionType c " + "ORDER BY c.type")})
public class CessionType implements Serializable {

	private static final long serialVersionUID = 8525570559901326749L;

	private Integer cessionTypeId;
	private String type;

	private Set<Cession> cessions = new HashSet<>();

	/** Constructeur par défaut. */
	public CessionType() {
	}

	@Id
	@Column(name = "CESSION_TYPE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getCessionTypeId() {
		return this.cessionTypeId;
	}

	public void setCessionTypeId(final Integer id) {
		this.cessionTypeId = id;
	}

	@Column(name = "TYPE", nullable = false, length = 15)
	public String getType() {
		return this.type;
	}

	public void setType(final String t) {
		this.type = t;
	}

	@OneToMany(mappedBy = "cessionType")
	public Set<Cession> getCessions() {
		return this.cessions;
	}

	public void setCessions(final Set<Cession> cess) {
		this.cessions = cess;
	}

	/**
	 * 2 objets sont considérés comme égaux s'ils ont le même type.
	 * 
	 * @param obj est l'objet à tester.
	 * @return true si les objets sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		final CessionType test = (CessionType) obj;
		if (this.type == null) {
			return (test.type == null);
		}
		return (this.type.equals(test.type));
	}

	/**
	 * Le hashcode est calculé sur l'attribut type.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashType = 0;

		if (this.type != null) {
			hashType = this.type.hashCode();
		}

		hash = 31 * hash + hashType;

		return hash;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.type != null) {
			return "{" + this.type + "}";
		}
		return "{Empty CessionType}";
	}

}
