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
package fr.aphp.tumorotek.model.coeur.prelevement;

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
 * Objet persistant mappant la table CONDIT_MILIEU. Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "CONDIT_MILIEU")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "CONDIT_MILIEU_ID")),
		@AttributeOverride(name = "nom", column = @Column(name = "MILIEU", nullable = false, length = 200)) })
@GenericGenerator(name = "autoincrement", strategy = "increment")
//@NamedQueries(
//   value = {@NamedQuery(name = "ConditMilieu.findByMilieu", query = "SELECT c FROM ConditMilieu c WHERE c.nom like ?1"),
//      @NamedQuery(name = "ConditMilieu.findByExcludedId", query = "SELECT c FROM ConditMilieu c " + "WHERE c.id != ?1"),
//      @NamedQuery(name = "ConditMilieu.findByPfOrder",
//         query = "SELECT c FROM ConditMilieu c " + "WHERE c.plateforme = ?1 ORDER BY c.nom"),
//      @NamedQuery(name = "ConditMilieu.findByOrder",
//         query = "FROM ConditMilieu c ORDER BY c.nom")})

public class ConditMilieu extends AbstractPfDependantThesaurusObject implements Serializable {

	private static final long serialVersionUID = -1948372046053283715L;

	private Set<Prelevement> prelevements = new HashSet<>();

	/** Constructeur par défaut. */
	public ConditMilieu() {
	}

	/**
	 * @deprecated Utiliser {@link #getId()}
	 * @return
	 */
	@Deprecated
	@Transient
	public Integer getConditMilieuId() {
		return this.getId();
	}

	/**
	 * @deprecated Utiliser {@link #setId(Integer)}
	 * @return
	 */
	@Deprecated
	public void setConditMilieuId(final Integer id) {
		this.setId(id);
	}

	/**
	 * @deprecated Utiliser {@link #getNom()}
	 * @return
	 */
	@Deprecated
	@Transient
	public String getMilieu() {
		return this.getNom();
	}

	/**
	 * @deprecated Utiliser {@link #setNom(String)}
	 * @param mil
	 */
	@Deprecated
	public void setMilieu(final String mil) {
		this.setNom(mil);
	}

	@OneToMany(mappedBy = "conditMilieu")
	public Set<Prelevement> getPrelevements() {
		return this.prelevements;
	}

	public void setPrelevements(final Set<Prelevement> prelevs) {
		this.prelevements = prelevs;
	}

	@Override
	public String toString() {
		if (this.getNom() != null) {
			return "{" + this.getNom() + "}";
		} else {
			return "{Empty ConditMilieu}";
		}
	}

}
