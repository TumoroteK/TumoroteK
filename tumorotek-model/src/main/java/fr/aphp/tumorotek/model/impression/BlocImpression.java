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
package fr.aphp.tumorotek.model.impression;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table BLOC_IMPRESSION. Classe créée le
 * 21/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.3
 *
 */
@Entity
@Table(name = "BLOC_IMPRESSION")
//@NamedQueries(value = {@NamedQuery(name = "BlocImpression.findByEntite",
//   query = "SELECT b FROM BlocImpression b WHERE b.entite = ?1 " + "ORDER BY b.ordre")})
public class BlocImpression implements Serializable {

	private static final long serialVersionUID = -401351001870593573L;

	private Integer blocImpressionId;
	private String nom;
	private Integer ordre;
	private Boolean isListe;
	private Boolean imprime = true;

	private Entite entite;

	private Set<ChampEntiteBloc> champEntiteBlocs = new HashSet<>();
	private Set<BlocImpressionTemplate> blocImpressionTemplates = new HashSet<>();
	private Set<ChampImprime> champImprimes = new HashSet<>();

	public BlocImpression() {

	}

	@Id
	@Column(name = "BLOC_IMPRESSION_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getBlocImpressionId() {
		return blocImpressionId;
	}

	public void setBlocImpressionId(final Integer id) {
		this.blocImpressionId = id;
	}

	@Column(name = "NOM", nullable = false)
	public String getNom() {
		return nom;
	}

	public void setNom(final String n) {
		this.nom = n;
	}

	@Column(name = "ORDRE", nullable = false)
	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(final Integer o) {
		this.ordre = o;
	}

	@Column(name = "IS_LISTE", nullable = false)
	public Boolean getIsListe() {
		return isListe;
	}

	public void setIsListe(final Boolean isL) {
		this.isListe = isL;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITE_ID", nullable = false)
	public Entite getEntite() {
		return entite;
	}

	public void setEntite(final Entite e) {
		this.entite = e;
	}

	@OneToMany(mappedBy = "pk.blocImpression")
	public Set<ChampEntiteBloc> getChampEntiteBlocs() {
		return champEntiteBlocs;
	}

	public void setChampEntiteBlocs(final Set<ChampEntiteBloc> champs) {
		this.champEntiteBlocs = champs;
	}

	@OneToMany(mappedBy = "pk.blocImpression")
	public Set<BlocImpressionTemplate> getBlocImpressionTemplates() {
		return blocImpressionTemplates;
	}

	public void setBlocImpressionTemplates(final Set<BlocImpressionTemplate> templates) {
		this.blocImpressionTemplates = templates;
	}

	@OneToMany(mappedBy = "pk.blocImpression")
	public Set<ChampImprime> getChampImprimes() {
		return champImprimes;
	}

	public void setChampImprimes(final Set<ChampImprime> champIs) {
		this.champImprimes = champIs;
	}

	@Transient
	public Boolean getImprime() {
		return imprime;
	}

	public void setImprime(final Boolean imp) {
		this.imprime = imp;
	}

	/**
	 * 2 blocs sont considérés comme égaux s'ils ont le même nom et la même entité.
	 * 
	 * @param obj est le bloc à tester.
	 * @return true si les blocs sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof BlocImpression)) {
			return false;
		}
		final BlocImpression test = (BlocImpression) obj;
		return Objects.equals(nom, test.getNom()) && Objects.equals(entite, test.getEntite());
	}

	/**
	 * Le hashcode est calculé sur les attributs nom et banque.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashNom = 0;
		int hashEntite = 0;

		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.entite != null) {
			hashEntite = this.entite.hashCode();
		}

		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashEntite;

		return hash;

	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.nom != null) {
			return "{" + this.nom + "}";
		}
		return "{Empty BlocImpression}";
	}

	/**
	 * Cree un clone de l'objet.
	 * 
	 * @return clone BlocImpression.
	 */
	@Override
	public BlocImpression clone() {
		final BlocImpression clone = new BlocImpression();

		clone.setBlocImpressionId(this.blocImpressionId);
		clone.setNom(this.nom);
		clone.setEntite(this.entite);
		clone.setOrdre(this.ordre);
		clone.setIsListe(this.isListe);

		return clone;
	}

}
