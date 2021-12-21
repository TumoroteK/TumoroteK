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
package fr.aphp.tumorotek.model.io.imports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.io.export.Champ;

/**
 *
 * Objet persistant mappant la table IMPORT_COLONNE. Classe créée le 24/01/11.
 *
 * @author Pierre VENTADOUR
 * @version 2.3
 *
 */
@Entity
@Table(name = "IMPORT_COLONNE")
//@NamedQueries(value = {
//		@NamedQuery(name = "ImportColonne.findByTemplateWithOrder", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateWithOrderSelectNom", query = "SELECT i.nom FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateAndEntite", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 AND i.champ.champEntite.entite = ?2 ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateAndEntiteDelegue", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 AND i.champ.champDelegue.entite = ?2 ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateAndAnnotationEntite", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 " + "AND i.champ.champAnnotation.tableAnnotation.entite = ?2 "
//				+ "ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateAndDataType", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 " + "AND i.champ.champEntite.dataType = ?2 " + "ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateAndThesaurus", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 " + "AND i.champ.champEntite.queryChamp is not null "
//				+ "ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByTemplateAndAnnotationDatatype", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importTemplate = ?1 " + "AND i.champ.champAnnotation.dataType = ?2 " + "ORDER BY i.ordre"),
//		@NamedQuery(name = "ImportColonne.findByExcludedIdWithTemplate", query = "SELECT i FROM ImportColonne i "
//				+ "WHERE i.importColonneId != ?1 " + "AND i.importTemplate = ?2"),
//		@NamedQuery(name = "ImportColonne"
//				+ ".findByExcludedIdWithTemplateSelectNom", query = "SELECT i.nom FROM ImportColonne i "
//						+ "WHERE i.importColonneId != ?1 " + "AND i.importTemplate = ?2") })
public class ImportColonne implements java.io.Serializable {

	private static final long serialVersionUID = -1118923775227753390L;

	private Integer importColonneId;
	private ImportTemplate importTemplate;
	private Champ champ;
	private String nom;
	private Integer ordre;

	public ImportColonne() {

	}

	@Id
	@Column(name = "IMPORT_COLONNE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getImportColonneId() {
		return importColonneId;
	}

	public void setImportColonneId(final Integer id) {
		this.importColonneId = id;
	}

	@ManyToOne
	@JoinColumn(name = "IMPORT_TEMPLATE_ID", nullable = false)
	public ImportTemplate getImportTemplate() {
		return importTemplate;
	}

	public void setImportTemplate(final ImportTemplate i) {
		this.importTemplate = i;
	}

	@ManyToOne
	@JoinColumn(name = "CHAMP_ID", nullable = false)
	public Champ getChamp() {
		return champ;
	}

	public void setChamp(final Champ c) {
		this.champ = c;
	}

	@Column(name = "NOM", nullable = false, length = 50)
	public String getNom() {
		return nom;
	}

	public void setNom(final String n) {
		this.nom = n;
	}

	@Column(name = "ORDRE", nullable = true)
	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(final Integer o) {
		this.ordre = o;
	}

	/**
	 * 2 colonnes sont considérées comme égales si elles ont le même nom et la même
	 * reference vers le template.
	 * 
	 * @param obj
	 *            à tester.
	 * @return true si les objs sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		final ImportColonne test = (ImportColonne) obj;
		return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
				&& (this.importTemplate == test.importTemplate
						|| (this.importTemplate != null && this.importTemplate.equals(test.importTemplate)))
				&& (this.champ == test.champ || (this.champ != null && this.champ.equals(test.champ))));
	}

	/**
	 * Le hashcode est calculé sur l'attribut nom et la reference vers le template.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashNom = 0;
		int hashChamp = 0;
		int hashImport = 0;

		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.champ != null) {
			hashChamp = this.champ.hashCode();
		}
		if (this.importTemplate != null) {
			hashImport = this.importTemplate.hashCode();
		}

		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashChamp;
		hash = 31 * hash + hashImport;

		return hash;
	}

	@Override
	public ImportColonne clone() {
		final ImportColonne clone = new ImportColonne();

		clone.setImportColonneId(this.importColonneId);
		clone.setImportTemplate(this.importTemplate);
		clone.setChamp(this.champ);
		clone.setNom(this.nom);
		clone.setOrdre(this.ordre);

		return clone;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.nom != null && this.importTemplate != null) {
			return "{" + this.nom + ", " + importTemplate.getNom() + "(ImportTemplate)}";
		} else {
			return "{Empty ImportColonne}";
		}
	}

}
