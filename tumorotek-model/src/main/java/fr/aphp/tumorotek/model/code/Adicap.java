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
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * Objet persistant mappant la table ADICAP. Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "ADICAP")
//@NamedQueries(value = {@NamedQuery(name = "Adicap.findByCodeLike", query = "SELECT a FROM Adicap a WHERE a.code like ?1"),
//   @NamedQuery(name = "Adicap.findByLibelleLike", query = "SELECT a FROM Adicap a WHERE a.libelle like ?1"),
//   @NamedQuery(name = "Adicap.findByAdicapGroupeNullParent",
//      query = "SELECT a FROM Adicap a WHERE a.adicapGroupe = ?1" + " AND a.adicapParent is null"),
//   @NamedQuery(name = "Adicap.findByAdicapGroupeAndCodeOrLibelle",
//      query = "SELECT a FROM Adicap a WHERE a.adicapGroupe = ?1 " + "AND (a.code like ?2 OR a.libelle like ?2)"),
//   @NamedQuery(name = "Adicap.findByMorpho", query = "SELECT a FROM Adicap a WHERE a.morpho = ?1"),
//   @NamedQuery(name = "Adicap.findByAdicapParentAndCodeOrLibelle",
//      query = "SELECT a FROM Adicap a " + "WHERE a.adicapParent = ?1 " + "AND (a.code like ?2 OR a.libelle like ?2)"),
//   @NamedQuery(name = "Adicap.findByDicoAndCodeOrLibelle",
//      query = "SELECT a FROM Adicap a WHERE a.adicapGroupe = ?1 " + " AND (a.code like ?2 OR a.libelle like ?2)")})
public class Adicap implements CodeCommon, Serializable {

	private static final long serialVersionUID = -8442052205622516619L;

	private Integer adicapId;
	private String code;
	private String libelle;
	private AdicapGroupe adicapGroupe;
	private Boolean morpho;
	private Adicap adicapParent;

	private Set<Adicap> adicaps = new HashSet<>();
	private Set<CimoMorpho> cimoMorphos = new HashSet<>();
	private Set<CimMaster> cimMasters = new HashSet<>();

	private CodeSelect codeSelect;

	/** Constructeur par défaut. */
	public Adicap() {
	}

	@Override
	public String toString() {
		return "{Adicap: " + this.code + "}";
	}

	@Id
	@Column(name = "ADICAP_ID", unique = true, nullable = false)
	public Integer getAdicapId() {
		return this.adicapId;
	}

	public void setAdicapId(final Integer id) {
		this.adicapId = id;
	}

	@Override
	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	@Override
	public void setCode(final String c) {
		this.code = c;
	}

	@Override
	@Column(name = "LIBELLE", nullable = false, length = 255)
	public String getLibelle() {
		return this.libelle;
	}

	@Override
	public void setLibelle(final String lib) {
		this.libelle = lib;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADICAP_GROUPE_ID")
	public AdicapGroupe getAdicapGroupe() {
		return this.adicapGroupe;
	}

	public void setAdicapGroupe(final AdicapGroupe parent) {
		this.adicapGroupe = parent;
	}

	@Column(name = "MORPHO", nullable = true)
	public Boolean getMorpho() {
		return this.morpho;
	}

	public void setMorpho(final Boolean morph) {
		this.morpho = morph;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADICAP_PARENT_ID", nullable = true)
	public Adicap getAdicapParent() {
		return this.adicapParent;
	}

	public void setAdicapParent(final Adicap topo) {
		this.adicapParent = topo;
	}

	@OneToMany(mappedBy = "adicapParent")
	@OrderBy("adicapId")
	public Set<Adicap> getAdicaps() {
		return this.adicaps;
	}

	public void setAdicaps(final Set<Adicap> adis) {
		this.adicaps = adis;
	}

	@ManyToMany(targetEntity = CimoMorpho.class)
	@JoinTable(name = "ADICAPCIMO_MORPHO", joinColumns = @JoinColumn(name = "ADICAP_ID"), inverseJoinColumns = @JoinColumn(name = "CIMO_MORPHO_ID"))
	public Set<CimoMorpho> getCimoMorphos() {
		return this.cimoMorphos;
	}

	public void setCimoMorphos(final Set<CimoMorpho> cimoMorphs) {
		this.cimoMorphos = cimoMorphs;
	}

	@ManyToMany(mappedBy = "adicaps", targetEntity = CimMaster.class)
	public Set<CimMaster> getCimMasters() {
		return this.cimMasters;
	}

	public void setCimMasters(final Set<CimMaster> cims) {
		this.cimMasters = cims;
	}

	/**
	 * 2 adicaps sont considérés comme égaux s'ils ont le ID. Les codes ne seront
	 * pas enregistres par l'interface mais directement dans la base de donnees donc
	 * impossibilite de ne pas avoir ID assigne de maniere unique.
	 * 
	 * @param obj est l'adicap à tester.
	 * @return true si les adicaps sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		// instanceOf est utilise plutot que != a cause des proxys
		// JPA qui sont crées par lors du fetch par
		// la relation manyToAny
		if ((obj == null) || !(obj instanceof Adicap)) {
			return false;
		}
		final Adicap test = (Adicap) obj;	
		return Objects.equals(adicapId, test.getAdicapId());
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

		if (this.adicapId != null) {
			hashID = this.adicapId.hashCode();
		}

		hash = 31 * hash + hashID;

		return hash;
	}

	@Override
	public Adicap clone() {
		return this;
	}

	@Override
	@Transient
	public Integer getCodeId() {
		return getAdicapId();
	}

	@Override
	public void setCodeId(final Integer id) {
		this.adicapId = id;
	}

	@Override
	@Transient
	public CodeSelect getCodeSelect() {
		return codeSelect;
	}

	@Override
	public void setCodeSelect(final CodeSelect s) {
		codeSelect = s;
	}
}
