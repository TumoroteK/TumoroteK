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
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.cession.Retour;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utils.Utils;

/**
 *
 * Objet persistant mappant la table TRANSFORMATION. Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "TRANSFORMATION")
//@NamedQueries(
//   value = {@NamedQuery(name = "Transformation.findByObjetId", query = "SELECT t FROM Transformation t WHERE t.objetId = ?1"),
//      @NamedQuery(name = "Transformation.findByExcludedId",
//         query = "SELECT t FROM Transformation t " + "WHERE t.transformationId != ?1"),
//      @NamedQuery(name = "Transformation.findByEntite", query = "SELECT t FROM Transformation t " + "WHERE t.entite = ?1"),
//      @NamedQuery(name = "Transformation.findByEntiteObjet",
//         query = "SELECT t FROM Transformation t " + "WHERE t.entite = ?1 AND t.objetId = ?2"),
//      @NamedQuery(name = "Transformation.findByQuantite", query = "SELECT t FROM Transformation t WHERE t.quantite = ?1"),
//      @NamedQuery(name = "Transformation.findByQuantiteUnite",
//         query = "SELECT t FROM Transformation t " + "WHERE t.quantiteUnite = ?1"),
//      @NamedQuery(name = "Transformation.findByProdDeriveId",
//         query = "SELECT t FROM Transformation t " + "left join t.prodDerives p " + "WHERE p.prodDeriveId = ?1")})
public class Transformation implements Serializable {

	private static final long serialVersionUID = 127047103050760932L;

	private Integer transformationId;
	private Integer objetId;
	private Entite entite;
	private Float quantite;
	private Unite quantiteUnite;
	// private Float volume;
	// private Unite volumeUnite;

	private Set<Retour> retours;
	private Set<ProdDerive> prodDerives;

	/** Constructeur par défaut. */
	public Transformation() {
		super();
		retours = new HashSet<>();
		prodDerives = new HashSet<>();
	}

	@Id
	@Column(name = "TRANSFORMATION_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getTransformationId() {
		return this.transformationId;
	}

	public void setTransformationId(final Integer id) {
		this.transformationId = id;
	}

	@Column(name = "OBJET_ID", nullable = false)
	public Integer getObjetId() {
		return this.objetId;
	}

	public void setObjetId(final Integer id) {
		this.objetId = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinColumn(name = "ENTITE_ID", nullable = false)
	public Entite getEntite() {
		return this.entite;
	}

	public void setEntite(final Entite ent) {
		this.entite = ent;
	}

	@Column(name = "QUANTITE", nullable = true)
	public Float getQuantite() {
		return Utils.floor(this.quantite, 3);
	}

	public void setQuantite(final Float quant) {
		this.quantite = Utils.floor(quant, 3);
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "QUANTITE_UNITE_ID", nullable = true)
	public Unite getQuantiteUnite() {
		return this.quantiteUnite;
	}

	public void setQuantiteUnite(final Unite qu) {
		this.quantiteUnite = qu;
	}

	/*
	 * @Column(name = "VOLUME", nullable = true) public Float getVolume() { return
	 * this.volume; }
	 * 
	 * public void setVolume(Float vol) { this.volume = vol; }
	 * 
	 * @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,
	 * CascadeType.MERGE })
	 * 
	 * @JoinColumn(name = "VOLUME_UNITE_ID", nullable = true) public Unite
	 * getVolumeUnite() { return this.volumeUnite; }
	 * 
	 * public void setVolumeUnite(Unite volu) { this.volumeUnite = volu; }
	 */

	@OneToMany(mappedBy = "transformation")
	public Set<Retour> getRetours() {
		return retours;
	}

	public void setRetours(final Set<Retour> rets) {
		this.retours = rets;
	}

	@OneToMany(mappedBy = "transformation")
	@OrderBy("prodDeriveId")
	public Set<ProdDerive> getProdDerives() {
		return prodDerives;
	}

	public void setProdDerives(final Set<ProdDerive> prodDers) {
		this.prodDerives = prodDers;
	}

	/**
	 * 2 transformations sont considérées comme égales si elles ont les mêmes
	 * quantités et volumes.
	 * 
	 * @param obj est la transformation à tester.
	 * @return true si les transformations sont égales.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof Transformation)) {
			return false;
		}
		final Transformation test = (Transformation) obj;
		return Objects.equals(objetId, test.getObjetId()) 
				&& Objects.equals(quantite, test.getQuantite())
				&& Objects.equals(quantiteUnite, test.getQuantiteUnite()) 
				&& Objects.equals(entite, test.getEntite());
	}

	/**
	 * Le hashcode est calculé sur les attributs quantite et volume.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashObjet = 0;
		int hashEntite = 0;
		int hashQuantite = 0;
		int hashQuantiteUnite = 0;

		if (this.objetId != null) {
			hashObjet = this.objetId.hashCode();
		}
		if (this.entite != null) {
			hashEntite = this.entite.hashCode();
		}
		if (this.quantite != null) {
			hashQuantite = this.quantite.hashCode();
		}
		if (this.quantiteUnite != null) {
			hashQuantiteUnite = this.quantiteUnite.hashCode();
		}
		/*
		 * if (this.volume != null) { hashVolume = this.volume.hashCode(); } if
		 * (this.volumeUnite != null) { hashVolumeUnite = this.volumeUnite.hashCode(); }
		 */

		hash = 7 * hash + hashObjet;
		hash = 7 * hash + hashEntite;
		hash = 7 * hash + hashQuantite;
		hash = 7 * hash + hashQuantiteUnite;
		/*
		 * hash = 7 * hash + hashVolume; hash = 7 * hash + hashVolumeUnite;
		 */

		return hash;

	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.entite != null && this.objetId != null) {
			return "{" + this.objetId + ", " + this.entite.toString() + "(Entite)}";
		}
		return "{Empty transformation}";
	}

	/**
	 * Cree un clone de l'objet.
	 * 
	 * @return clone Transformation.
	 */
	@Override
	public Transformation clone() {
		final Transformation clone = new Transformation();

		clone.setTransformationId(this.getTransformationId());
		clone.setObjetId(this.getObjetId());
		clone.setEntite(this.getEntite());
		clone.setQuantite(this.getQuantite());
		clone.setQuantiteUnite(this.getQuantiteUnite());
		/*
		 * clone.setVolume(this.getVolume());
		 * clone.setVolumeUnite(this.getVolumeUnite());
		 */

		return clone;
	}

	@PreRemove
	private void nullifyRetours() {
		for (final Retour r : getRetours()) {
			r.setTransformation(null);
		}
	}
}
