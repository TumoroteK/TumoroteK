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
package fr.aphp.tumorotek.model.code;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * Objet persistant mappant la table ADICAP_GROUPE. Classe créée le 02/06/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Entity
@Table(name = "ADICAP_GROUPE")
//@NamedQueries(value = {
//   @NamedQuery(name = "AdicapGroupe.findDictionnaires", query = "SELECT a FROM AdicapGroupe a WHERE a.groupeParent is null")})
public class AdicapGroupe implements Serializable {

	private static final long serialVersionUID = -8442052205622516619L;

	private Integer adicapGroupeId;
	private String nom;
	private AdicapGroupe groupeParent;

	private Set<AdicapGroupe> adicapGroupes = new HashSet<>();
	private Set<Adicap> adicaps = new HashSet<>();

	/** Constructeur par défaut. */
	public AdicapGroupe() {
	}

	@Override
	public String toString() {
		return "{AdicapGroupe: " + this.nom + "}";
	}

	@Id
	@Column(name = "ADICAP_GROUPE_ID", unique = true, nullable = false)
	public Integer getAdicapGroupeId() {
		return this.adicapGroupeId;
	}

	public void setAdicapGroupeId(final Integer id) {
		this.adicapGroupeId = id;
	}

	@Column(name = "NOM", nullable = false, length = 200)
	public String getNom() {
		return this.nom;
	}

	public void setNom(final String n) {
		this.nom = n;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPE_PARENT_ID", nullable = true)
	public AdicapGroupe getGroupeParent() {
		return this.groupeParent;
	}

	public void setGroupeParent(final AdicapGroupe parent) {
		this.groupeParent = parent;
	}

	@OneToMany(mappedBy = "groupeParent")
	@OrderBy("adicapGroupeId")
	public Set<AdicapGroupe> getAdicapGroupes() {
		return this.adicapGroupes;
	}

	public void setAdicapGroupes(final Set<AdicapGroupe> adis) {
		this.adicapGroupes = adis;
	}

	@OneToMany(mappedBy = "adicapGroupe")
	@OrderBy("adicapId")
	public Set<Adicap> getAdicaps() {
		return this.adicaps;
	}

	public void setAdicaps(final Set<Adicap> adis) {
		this.adicaps = adis;
	}

	/**
	 * 2 groupe adicaps sont considérés comme égaux s'ils ont le même ID. Les
	 * groupes ne seront pas enregistres par l'interface mais directement dans la
	 * base de donnees donc impossibilite de ne pas avoir ID assigne de maniere
	 * unique.
	 * 
	 * @param obj est le groupe adicap à tester.
	 * @return true si les groupes adicaps sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		final AdicapGroupe test = (AdicapGroupe) obj;
		if (this.adicapGroupeId != null) {
			return this.adicapGroupeId.equals(test.adicapGroupeId);
		}
		return false;
	}

	/**
	 * Le hashcode est calculé sur l'ID.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashID = 0;

		if (this.adicapGroupeId != null) {
			hashID = this.adicapGroupeId.hashCode();
		}

		hash = 31 * hash + hashID;

		return hash;
	}
}
