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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.io.export.AbstractTKChamp;
import fr.aphp.tumorotek.model.io.export.Champ;

/**
 * Objet persistant mappant la table CHAMP_ANNOTATION.
 *
 * Date: 09/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.0
 *
 */
@Entity
@Table(name = "CHAMP_ANNOTATION")
@GenericGenerator(name = "seqGenerator", strategy = "increment")
@AttributeOverride(name = "id", column = @Column(name = "CHAMP_ANNOTATION_ID", unique = true, nullable = false))
//@NamedQueries(value = {
//   @NamedQuery(name = "ChampAnnotation.findByNom",
//      query = "SELECT c FROM ChampAnnotation c WHERE c.nom like ?1" + " ORDER BY c.nom"),
//   @NamedQuery(name = "ChampAnnotation.findByTable",
//      query = "SELECT c FROM ChampAnnotation c " + "WHERE c.tableAnnotation = ?1 ORDER BY c.ordre"),
//   @NamedQuery(name = "ChampAnnotation.findByTableAndType",
//      query = "SELECT c FROM ChampAnnotation c " + "WHERE c.tableAnnotation = ?1 " + "ANd c.dataType = ?2 ORDER BY c.ordre"),
//   @NamedQuery(name = "ChampAnnotation.findByExcludedId",
//      query = "SELECT c FROM ChampAnnotation c WHERE " + "c.id != ?1"),
//   @NamedQuery(name = "ChampAnnotation.findByEditByCatalogue",
//      query = "SELECT c FROM ChampAnnotation c " + "WHERE c.edit = 1 " + "AND c.tableAnnotation = ?1 " + "ORDER BY c.ordre"),
//   @NamedQuery(name = "ChampAnnotation.findImportColonnesByChampAnnotation",
//      query = "SELECT i FROM ImportColonne i WHERE " + "i.champ.champAnnotation = ?1"),
//   @NamedQuery(name = "ChampAnnotation.findChpLEtiquetteByChampAnnotation",
//      query = "SELECT c FROM ChampLigneEtiquette c WHERE " + "c.champ.champAnnotation = ?1"),
//   @NamedQuery(name = "ChampAnnotation.findCriteresByChampAnnotation",
//      query = "SELECT c FROM Critere c WHERE " + "c.champ.champAnnotation = ?1"),
//   @NamedQuery(name = "ChampAnnotation.findResultatsByChampAnnotation",
//      query = "SELECT r FROM Resultat r WHERE " + "r.champ.champAnnotation = ?1"),
//   @NamedQuery(name = "ChampAnnotation.findByImportTemplateAndEntite", query = "SELECT c.champAnnotation FROM ImportColonne i "
//      + "JOIN i.champ c WHERE i.importTemplate = ?1 " + "AND c.champAnnotation.tableAnnotation.entite = ?2"),
//   @NamedQuery(name = "ChampAnnotation.findByTableAndDataType",
//   query = "SELECT c FROM ChampAnnotation c " + "WHERE c.tableAnnotation = ?1 AND c.dataType in ?2 ORDER BY c.ordre")})
public class ChampAnnotation extends AbstractTKChamp implements TKFantomableObject, Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean combine;
	private Integer ordre;
	private Boolean edit = true;
	private TableAnnotation tableAnnotation;
	private ChampCalcule champCalcule;

	private Set<AnnotationDefaut> annotationDefauts = new HashSet<>();
	private Set<Item> items = new HashSet<>();
	private Set<AnnotationValeur> annotationValeurs = new HashSet<>();
	private Set<Champ> champs = new HashSet<>();

	/** Constructeur par défaut. */
	public ChampAnnotation() {
	}

	/**
	 * @deprecated Utiliser {@link #getId()}
	 */
	@Transient
	@Deprecated
	public Integer getChampAnnotationId() {
		return this.getId();
	}

	/**
	 * @deprecated Utiliser {@link #setId(Integer)}
	 */
	@Deprecated
	public void setChampAnnotationId(final Integer id) {
		this.setId(id);
	}

	@Column(name = "COMBINE", nullable = true)
	public Boolean getCombine() {
		return this.combine;
	}

	public void setCombine(final Boolean b) {
		this.combine = b;
	}

	@Column(name = "ORDRE", nullable = false, length = 3)
	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(final Integer o) {
		this.ordre = o;
	}

	@Column(name = "EDIT", nullable = true)
	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(final Boolean e) {
		this.edit = e;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_ANNOTATION_ID", nullable = false)
	public TableAnnotation getTableAnnotation() {
		return this.tableAnnotation;
	}

	public void setTableAnnotation(final TableAnnotation table) {
		this.tableAnnotation = table;
	}

	@OneToOne(mappedBy = "champAnnotation", cascade = { CascadeType.REMOVE })
	public ChampCalcule getChampCalcule() {
		return this.champCalcule;
	}

	public void setChampCalcule(ChampCalcule champCalcule) {
		this.champCalcule = champCalcule;
	}

	@OneToMany(mappedBy = "champAnnotation", cascade = { CascadeType.REMOVE })
	public Set<AnnotationDefaut> getAnnotationDefauts() {
		return this.annotationDefauts;
	}

	public void setAnnotationDefauts(final Set<AnnotationDefaut> defauts) {
		this.annotationDefauts = defauts;
	}

	@OneToMany(mappedBy = "champAnnotation", cascade = { CascadeType.REMOVE })
	@OrderBy("label")
	public Set<Item> getItems() {
		return this.items;
	}

	public void setItems(final Set<Item> its) {
		this.items = its;
	}

	@OneToMany(mappedBy = "champAnnotation", cascade = { CascadeType.REMOVE })
	public Set<AnnotationValeur> getAnnotationValeurs() {
		return this.annotationValeurs;
	}

	public void setAnnotationValeurs(final Set<AnnotationValeur> valeurs) {
		this.annotationValeurs = valeurs;
	}

	@OneToMany(mappedBy = "champAnnotation", cascade = { CascadeType.REMOVE })
	public Set<Champ> getChamps() {
		return champs;
	}

	public void setChamps(final Set<Champ> cs) {
		this.champs = cs;
	}

	/**
	 * 2 champs sont consideres comme egaux si ils ont le même nom et la même
	 * reference vers la table d'annotation à laquelle ils appartiennent. Le systeme
	 * bloquera l'enregistrement de deux champ de même nom (quelque soit leur type)
	 * dans une même table.
	 * 
	 * @param obj est le champ à tester.
	 * @return true si les champs sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}

		final ChampAnnotation test = (ChampAnnotation) obj;
		return ((this.getNom() == test.getNom() || (this.getNom() != null && this.getNom().equals(test.getNom())))
				&& (this.tableAnnotation == test.tableAnnotation
						|| (this.tableAnnotation != null && this.tableAnnotation.equals(test.tableAnnotation))));
	}

	/**
	 * Le hashcode est calculé sur le nom du champ_annotation et sur la reference
	 * vers la table d'annotation.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashNom = 0;
		int hasTableId = 0;

		if (this.getNom() != null) {
			hashNom = this.getNom().hashCode();
		}
		if (this.tableAnnotation != null) {
			hasTableId = this.tableAnnotation.hashCode();
		}

		hash = 31 * hash + hashNom;
		hash = 31 * hash + hasTableId;

		return hash;
	}

	@Override
	public String toString() {
		if (this.tableAnnotation != null && this.getNom() != null) {
			return "{ChampAnnotation: " + this.tableAnnotation.getNom() + "." + this.getNom() + "}";
		}

		return "{Empty ChampAnnotation}";
	}

	/**
	 * Cree un clone de l'objet.
	 * 
	 * @return clone ChampAnnotation
	 */
	@Override
	public ChampAnnotation clone() {
		final ChampAnnotation clone = new ChampAnnotation();
		clone.setId(this.getId());
		clone.setNom(this.getNom());
		clone.setDataType(this.getDataType());
		clone.setOrdre(this.ordre);
		clone.setCombine(this.combine);
		clone.setEdit(getEdit());
		clone.setTableAnnotation(this.tableAnnotation);
		clone.setAnnotationDefauts(this.annotationDefauts);
		clone.setAnnotationValeurs(this.annotationValeurs);
		clone.setItems(this.items);
		clone.setChampCalcule(this.champCalcule);
		return clone;
	}

	@Override
	public String entiteNom() {
		return "ChampAnnotation";
	}

	@Override
	@Transient
	public String getPhantomData() {
		return getNom();
	}

}
