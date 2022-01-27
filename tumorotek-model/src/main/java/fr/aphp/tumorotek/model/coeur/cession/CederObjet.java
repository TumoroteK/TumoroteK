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
import java.util.List;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utils.Utils;

/**
 *
 * Objet persistant mappant la table CEDER_OBJET. Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 *      hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.3
 *
 */
@Entity
@Table(name = "CEDER_OBJET")
@AssociationOverrides({
		@AssociationOverride(name = "pk.cession", joinColumns = @JoinColumn(name = "CESSION_ID", referencedColumnName = "CESSION_ID")),
		@AssociationOverride(name = "pk.entite", joinColumns = @JoinColumn(name = "ENTITE_ID", referencedColumnName = "ENTITE_ID")) })
@AttributeOverride(column = @Column(name = "OBJET_ID"), name = "objetId")
//@NamedQueries(
//   value = {@NamedQuery(name = "CederObjet.findByExcludedPK", query = "SELECT c FROM CederObjet c " + "WHERE c.pk != ?1"),
//      @NamedQuery(name = "CederObjet.findByEntite", query = "SELECT c FROM CederObjet c " + "WHERE c.pk.entite = ?1"),
//      @NamedQuery(name = "CederObjet.findByEntiteObjet",
//         query = "SELECT c FROM CederObjet c " + "WHERE c.pk.entite = ?1 AND c.pk.objetId = ?2"),
//      @NamedQuery(name = "CederObjet.findByEntiteObjetStatut",
//      query = "SELECT c FROM CederObjet c " + "WHERE c.pk.entite = ?1 AND c.pk.objetId = ?2 AND c.statut = ?3"),
//      @NamedQuery(name = "CederObjet.findByObjetId", query = "SELECT c FROM CederObjet c " + "WHERE c.pk.objetId = ?1"),
//      @NamedQuery(name = "CederObjet.findByCessionEntite",
//         query = "SELECT c FROM CederObjet c " + "WHERE c.pk.cession = ?1 AND c.pk.entite = ?2"),
//      @NamedQuery(name = "CederObjet.findObjectsCessedCount",
//         query = "SELECT count(c.pk.objetId) FROM CederObjet c " + "WHERE c.pk.cession = ?1 and c.pk.entite = ?2"),
//      @NamedQuery(name = "CederObjet.findCodesEchantillonByCession",
//         query = "SELECT e.code FROM CederObjet c, Echantillon e " + "WHERE c.pk.objetId = e.echantillonId "
//            + "AND c.pk.cession = ?1 AND c.pk.entite.entiteId = 3 " + "ORDER BY e.code"),
//      @NamedQuery(name = "CederObjet.findCodesDeriveByCession",
//         query = "SELECT e.code FROM CederObjet c, ProdDerive e " + "WHERE c.pk.objetId = e.prodDeriveId "
//            + "AND c.pk.cession = ?1 AND c.pk.entite.entiteId = 8 " + "ORDER BY e.code"),
//      @NamedQuery(name = "CederObjet.findCountObjCession",
//         query = "SELECT count(c.pk.cession) FROM CederObjet c " + "WHERE c.pk.objetId = ?1 AND c.pk.entite = ?2"),})
public class CederObjet implements Serializable, TKdataObject {

	private static final long serialVersionUID = -4882326831163602398L;

	private Float quantite;
	private Unite quantiteUnite;
	private ECederObjetStatut statut;
	private List<ProdDerive> produitRetourList;

	/** Constructeur par défaut. */
	public CederObjet() {
	}

	private CederObjetPK pk = new CederObjetPK();

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "cession", column = @Column(name = "CESSION_ID")),
			@AttributeOverride(name = "entite", column = @Column(name = "ENTITE_ID")),
			@AttributeOverride(name = "objetId", column = @Column(name = "OBJET_ID")) })
	public CederObjetPK getPk() {
		return pk;
	}

	public void setPk(final CederObjetPK cpk) {
		this.pk = cpk;
	}

	@Transient
	public Integer getObjetId() {
		return this.pk.getObjetId();
	}

	public void setObjetId(final Integer id) {
		this.pk.setObjetId(id);
	}

	@Column(name = "QUANTITE", nullable = true)
	public Float getQuantite() {
		return Utils.floor(this.quantite, 3);
	}

	public void setQuantite(final Float q) {
		this.quantite = Utils.floor(q, 3);
	}

	@Transient
	public Entite getEntite() {
		return this.pk.getEntite();
	}

	public void setEntite(final Entite e) {
		this.pk.setEntite(e);
	}

	@ManyToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "QUANTITE_UNITE_ID", nullable = true)
	public Unite getQuantiteUnite() {
		return this.quantiteUnite;
	}

	public void setQuantiteUnite(final Unite qu) {
		this.quantiteUnite = qu;
	}

	@Column(name = "STATUT")
	@Enumerated(EnumType.STRING)
	public ECederObjetStatut getStatut() {
		return statut;
	}

	public void setStatut(ECederObjetStatut statut) {
		this.statut = statut;
	}

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "CEDER_OBJET_PROD_DERIVE", joinColumns = {
			@JoinColumn(name = "CESSION_ID", referencedColumnName = "CESSION_ID"),
			@JoinColumn(name = "ENTITE_ID", referencedColumnName = "ENTITE_ID"),
			@JoinColumn(name = "OBJET_ID", referencedColumnName = "OBJET_ID") }, inverseJoinColumns = @JoinColumn(name = "PROD_DERIVE_ID"))
	public List<ProdDerive> getProduitRetourList() {
		return produitRetourList;
	}

	public void setProduitRetourList(List<ProdDerive> produitRetour) {
		this.produitRetourList = produitRetour;
	}

	@Transient
	public Cession getCession() {
		return this.pk.getCession();
	}

	public void setCession(final Cession c) {
		this.pk.setCession(c);
	}

	/**
	 * 2 operation de ceder Objet sont consideres egales si elles ont la même PK et
	 * le même couple volume-volume_id ou quantite-quantite_id.
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
		final CederObjet test = (CederObjet) obj;
		return ((this.pk == test.pk || (this.pk != null && this.pk.equals(test.pk))));
	}

	/**
	 * Le hashcode est calculé sur les attributs cession, entite et objetId.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashPk = 0;

		if (this.pk != null) {
			hashPk = this.pk.hashCode();
		}

		hash = 7 * hash + hashPk;

		return hash;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.pk != null) {
			return "{" + this.pk.toString() + "}";
		}
		return "{Empty CederObjet}";
	}

	/**
	 * Cree un clone de l'objet.
	 * 
	 * @return clone CederObjet.
	 */
	@Override
	public CederObjet clone() {
		final CederObjet clone = new CederObjet();

		clone.setPk(this.getPk());
		clone.setQuantite(this.getQuantite());
		clone.setQuantiteUnite(this.getQuantiteUnite());

		return clone;
	}

	@Override
	public Integer listableObjectId() {
		return null;
	}
}
