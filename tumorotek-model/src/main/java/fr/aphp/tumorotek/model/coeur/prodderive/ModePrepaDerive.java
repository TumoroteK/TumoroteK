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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;

/**
 *
 * Objet persistant mappant la table MODE_PREPA_DERIVE. Classe créée le
 * 05/01/2011.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "MODE_PREPA_DERIVE")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "MODE_PREPA_DERIVE_ID")) })
@GenericGenerator(name = "autoincrement", strategy = "increment")
//@NamedQueries(
//   value = {@NamedQuery(name = "ModePrepaDerive.findByNom", query = "SELECT m FROM ModePrepaDerive m WHERE m.nom like ?1"),
//      @NamedQuery(name = "ModePrepaDerive.findByExcludedId", query = "SELECT m FROM ModePrepaDerive m " + "WHERE m.id != ?1"),
//      @NamedQuery(name = "ModePrepaDerive.findByPfOrder",
//         query = "SELECT m FROM ModePrepaDerive m " + "WHERE m.plateforme = ?1 ORDER BY m.nom"),
//   @NamedQuery(name = "ModePrepaDerive.findByOrder",
//      query = "SELECT m FROM ModePrepaDerive m ORDER BY m.nom")})
public class ModePrepaDerive extends AbstractPfDependantThesaurusObject implements Serializable {
	private static final long serialVersionUID = 5348645345465465L;

	private String nomEn;
	private Set<ProdDerive> prodDerives;

	/** Constructeur par défaut. */
	public ModePrepaDerive() {
		prodDerives = new HashSet<>();
	}

	/**
	 * @deprecated Utiliser {@link #getId()}
	 * @return
	 */
	@Deprecated
	@Transient
	public Integer getModePrepaDeriveId() {
		return this.getId();
	}

	/**
	 * @deprecated Utiliser {@link #setId(Integer)}
	 * @param mId
	 */
	@Deprecated
	public void setModePrepaDeriveId(final Integer mId) {
		this.setId(mId);
	}

	@Column(name = "NOM_EN", nullable = true, length = 25)
	public String getNomEn() {
		return nomEn;
	}

	public void setNomEn(final String e) {
		this.nomEn = e;
	}

	@OneToMany(mappedBy = "modePrepaDerive")
	public Set<ProdDerive> getProdDerives() {
		return prodDerives;
	}

	public void setProdDerives(final Set<ProdDerive> p) {
		this.prodDerives = p;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.getNom() != null) {
			return "{" + this.getNom() + "}";
		} else {
			return "{Empty ModePrepaDerive}";
		}
	}

}
