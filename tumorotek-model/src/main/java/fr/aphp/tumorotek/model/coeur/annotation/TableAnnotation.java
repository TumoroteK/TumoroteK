/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.model.coeur.annotation;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Objet persistant mappant la table TABLE_ANNOTATION.
 * <p>
 * Date: 09/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.0
 */
@Entity
@Table(name = "TABLE_ANNOTATION")
@NamedQueries(value = {
		@NamedQuery(name = "TableAnnotation.findByNom", query = "SELECT t FROM TableAnnotation t WHERE t.nom like ?1"
				+ " ORDER BY t.nom"),
		@NamedQuery(name = "TableAnnotation.findByEntiteAndBanque", query = "SELECT t FROM TableAnnotation t"
				+ " JOIN t.tableAnnotationBanques b" + " WHERE t.entite = ?1 AND b.pk.banque = ?2"
				+ " ORDER BY b.ordre"),
		@NamedQuery(name = "TableAnnotation.findByEntiteBanqueAndCatalogue", query = "SELECT t FROM TableAnnotation t"
				+ " JOIN t.tableAnnotationBanques b" + " WHERE t.entite = ?1 AND b.pk.banque = ?2"
				+ " AND t.catalogue.nom like ?3" + " ORDER BY b.ordre"),
		@NamedQuery(name = "TableAnnotation.findByEntiteAndPlateforme", query = "SELECT t FROM TableAnnotation t"
				+ " WHERE t.entite = ?1 AND t.plateforme = ?2 " + "AND t.catalogue is null " + "ORDER BY t.nom"),
		@NamedQuery(name = "TableAnnotation"
				+ ".findMaxOrdreForBanqueAndEntite", query = "SELECT max(b.ordre) FROM TableAnnotationBanque b"
						+ " JOIN b.pk.tableAnnotation t" + " WHERE t.entite = ?1 AND b.pk.banque = ?2"),
		// @NamedQuery(name = "TableAnnotation.findDoublon",
		// query = "SELECT t FROM TableAnnotation t WHERE t.nom = ?1 "
		// + "AND t.entite = ?2")
		@NamedQuery(name = "TableAnnotation.findByExcludedId", query = "SELECT t FROM TableAnnotation t WHERE "
				+ "t.tableAnnotationId != ?1"),
		@NamedQuery(name = "TableAnnotation.findByPlateforme", query = "SELECT t from TableAnnotation t "
				+ "WHERE t.plateforme = ?1 " + "order by t.nom"),
		@NamedQuery(name = "TableAnnotation.findByCatalogues", query = "SELECT t from TableAnnotation t "
				+ "WHERE t.catalogue in (?1) " + "ORDER BY t.entite.entiteId, t.tableAnnotationId"),
		@NamedQuery(name = "TableAnnotation.findByBanques", query = "SELECT distinct t from TableAnnotation t "
				+ "JOIN t.tableAnnotationBanques tabs " + "WHERE tabs.pk.banque in (?1) "
				// + "AND t.catalogue is null "
				+ "ORDER BY t.entite.entiteId, t.nom"),
		@NamedQuery(name = "TableAnnotation.findByCatalogueAndChpEdit", query = "SELECT distinct t FROM TableAnnotation t "
				+ "JOIN t.champAnnotations c " + "WHERE t.catalogue = ?1 " + "AND c.edit = 1 "
				+ "ORDER BY t.entite.entiteId") })
public class TableAnnotation implements TKFantomableObject, TKdataObject, Serializable {
	private static final long serialVersionUID = 1L;

	private Integer tableAnnotationId;
	private String nom;
	private String description;
	private Entite entite;
	private Catalogue catalogue;
	private Plateforme plateforme;

	private Set<ChampAnnotation> champAnnotations = new HashSet<>();
	private Set<TableAnnotationBanque> tableAnnotationBanques = new HashSet<>();
	private Set<TableAnnotationTemplate> tableAnnotationTemplates = new HashSet<>();

	/**
	 * Constructeur par défaut.
	 */
	public TableAnnotation() {
	}

	@Id
	@Column(name = "TABLE_ANNOTATION_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getTableAnnotationId() {
		return this.tableAnnotationId;
	}

	public void setTableAnnotationId(final Integer id) {
		this.tableAnnotationId = id;
	}

	@Column(name = "NOM", nullable = false, length = 50)
	public String getNom() {
		return this.nom;
	}

	public void setNom(final String n) {
		this.nom = n;
	}

	// @Lob
	@Column(name = "DESCRIPTION", nullable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String descr) {
		this.description = descr;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITE_ID", nullable = false)
	public Entite getEntite() {
		return this.entite;
	}

	public void setEntite(final Entite en) {
		this.entite = en;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATALOGUE_ID", nullable = true)
	public Catalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(final Catalogue cata) {
		this.catalogue = cata;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLATEFORME_ID", nullable = true)
	public Plateforme getPlateforme() {
		return plateforme;
	}

	public void setPlateforme(final Plateforme pf) {
		this.plateforme = pf;
	}

	@OneToMany(mappedBy = "tableAnnotation")
	@OrderBy("ordre")
	public Set<ChampAnnotation> getChampAnnotations() {
		return this.champAnnotations;
	}

	public void setChampAnnotations(final Set<ChampAnnotation> chps) {
		this.champAnnotations = chps;
	}

	@OneToMany(mappedBy = "pk.tableAnnotation", cascade = { CascadeType.REMOVE })
	@OrderBy("ordre")
	public Set<TableAnnotationBanque> getTableAnnotationBanques() {
		return this.tableAnnotationBanques;
	}

	public void setTableAnnotationBanques(final Set<TableAnnotationBanque> tabs) {
		this.tableAnnotationBanques = tabs;
	}

	@OneToMany(mappedBy = "pk.tableAnnotation", cascade = { CascadeType.REMOVE })
	public Set<TableAnnotationTemplate> getTableAnnotationTemplates() {
		return tableAnnotationTemplates;
	}

	public void setTableAnnotationTemplates(final Set<TableAnnotationTemplate> tables) {
		this.tableAnnotationTemplates = tables;
	}

	/**
	 * 2 tables sont considerees comme egales si ils ont le même nom et la même
	 * reference vers l'entite. Les tables annotations sont partagees par le systeme
	 * donc ce dernier bloquera l'enregistrement de deux tables ayant le même nom
	 * pour la même entité pour une même plateforme.
	 *
	 * @param obj est la table à tester.
	 * @return true si les tables sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof TableAnnotation)) {
			return false;
		}		
		final TableAnnotation test = (TableAnnotation) obj;
		return Objects.equals(getNom(), test.getNom())
			&& Objects.equals(entite, test.getEntite())
			&& Objects.equals(plateforme, test.getPlateforme());
	}

	/**
	 * Le hashcode est calculé sur le nom de la table et l'id de l'entite à laquelle
	 * la table est attribuee..
	 *
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashNom = 0;
		int hashEntite = 0;
		int hashPF = 0;

		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.entite != null) {
			hashEntite = this.entite.hashCode();
		}
		if (this.plateforme != null) {
			hashPF = this.plateforme.hashCode();
		}

		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashEntite;
		hash = 31 * hash + hashPF;

		return hash;
	}

	@Override
	public String toString() {
		if (this.entite != null && this.nom != null) {
			return "{" + this.entite.getNom() + "." + this.nom + "}";
		}
		return "{Empty TableAnnotation}";
	}

	/**
	 * Cree un clone de l'objet.
	 *
	 * @return clone TableAnnotation
	 */
	@Override
	public TableAnnotation clone() {
		final TableAnnotation clone = new TableAnnotation();
		clone.setTableAnnotationId(this.tableAnnotationId);
		clone.setNom(this.nom);
		clone.setDescription(this.description);
		clone.setCatalogue(this.catalogue);
		clone.setEntite(this.entite);
		clone.setChampAnnotations(this.getChampAnnotations());
		clone.setPlateforme(getPlateforme());

		return clone;
	}

	@Override
	@Transient
	public Integer listableObjectId() {
		return getTableAnnotationId();
	}

	@Override
	public String entiteNom() {
		return "TableAnnotation";
	}

	@Override
	@Transient
	public String getPhantomData() {
		return getNom();
	}
}
