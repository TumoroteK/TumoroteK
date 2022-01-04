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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Objet persistant mappant la table CIM_LIBELLE. Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "CIM_LIBELLE")
//@NamedQueries(
//   value = {@NamedQuery(name = "CimLibelle.findByLibelleLike", query = "SELECT c FROM CimLibelle c WHERE c.libelle like ?1")})
public class CimLibelle implements Serializable {

	private static final long serialVersionUID = -5425665238563681260L;

	private Integer lid;
	private String source;
	private String valid;
	private String libelle;

	private CimMaster cimMaster;

	/** Constrcteur par défaut. */
	public CimLibelle() {
	}

	@Override
	public String toString() {
		return "{CimLibelle: " + this.libelle + "}";
	}

	@Id
	@Column(name = "LID", unique = true, nullable = false)
	public Integer getLid() {
		return this.lid;
	}

	public void setLid(final Integer id) {
		this.lid = id;
	}

	@Column(name = "SOURCE", nullable = true, length = 2)
	public String getSource() {
		return this.source;
	}

	public void setSource(final String sour) {
		this.source = sour;
	}

	@Column(name = "VALID", nullable = true, length = 1)
	public String getValid() {
		return this.valid;
	}

	public void setValid(final String val) {
		this.valid = val;
	}

	@Column(name = "LIBELLE", nullable = true)
	// @Lob
	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(final String lib) {
		this.libelle = lib;
	}

	@ManyToOne
	@JoinColumn(name = "SID", nullable = true)
	public CimMaster getCimMaster() {
		return this.cimMaster;
	}

	public void setCimMaster(final CimMaster s) {
		this.cimMaster = s;
	}

	/**
	 * 2 CIM sont considérés comme égaux s'ils ont le même LID.
	 * 
	 * @param obj est le libelle CIM à tester.
	 * @return true si les libelle CIMs sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		final CimLibelle test = (CimLibelle) obj;
		if (this.lid != null) {
			return this.lid.equals(test.lid);
		} // impossible
		return false;
	}

	/**
	 * Le hashcode est calculé sur le LID.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashLID = 0;

		if (this.lid != null) {
			hashLID = this.lid.hashCode();
		}

		hash = 31 * hash + hashLID;

		return hash;
	}
}
