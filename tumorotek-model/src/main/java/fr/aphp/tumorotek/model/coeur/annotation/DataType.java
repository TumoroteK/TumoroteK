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
package fr.aphp.tumorotek.model.coeur.annotation;

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
 * Objet persistant mappant la table DATA_TYPE.<br>
 *
 * Date: 09/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.0
 *
 */
@Entity
@Table(name = "DATA_TYPE")
//@NamedQueries(value = {@NamedQuery(name = "DataType.findByType", query = "SELECT d FROM DataType d WHERE d.type = ?1"),
//   @NamedQuery(name = "DataType.findByTypes", query = "SELECT d FROM DataType d WHERE d.type in ?1")})
public class DataType implements Serializable { /*
												 * TODO Pourquoi ne pas simplement utiliser un ENUM, plutôt que de faire
												 * des appels en base non pertinents ? De plus ces datatype ne sont
												 * absolument pas modifiables. Cela éviterai également les erreurs lors
												 * des tests equals (cf. "text" vs "texte") + on connaitrait exactement
												 * les types disponibles et utilisés.
												 */

	private static final long serialVersionUID = 4677080789317418835L;

	private Integer dataTypeId;
	private String type;
	private Set<ChampAnnotation> champAnnotations = new HashSet<>();

	/** Constructeur par défaut. */
	public DataType() {
	}

	@Id
	@Column(name = "DATA_TYPE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getDataTypeId() {
		return this.dataTypeId;
	}

	public void setDataTypeId(final Integer id) {
		this.dataTypeId = id;
	}

	@Column(name = "TYPE", nullable = false, length = 10)
	public String getType() {
		return this.type;
	}

	public void setType(final String t) {
		this.type = t;
	}

	@OneToMany(mappedBy = "dataType")
	public Set<ChampAnnotation> getChampAnnotations() {
		return this.champAnnotations;
	}

	public void setChampAnnotations(final Set<ChampAnnotation> chps) {
		this.champAnnotations = chps;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.type != null) {
			return "{" + this.type + "}";
		}
		return "{Empty DataType}";
	}

	/**
	 * 2 data-types sont consideres comme egaux si ils ont le même type.
	 * 
	 * @param obj est le data-type à tester.
	 * @return true si les data-types sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}

		final DataType test = (DataType) obj;

		return ((this.type == test.type || (this.type != null && this.type.equals(test.type))));
	}

	/**
	 * Le hashcode est calculé sur le type.
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
}
