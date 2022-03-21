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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table TRANSCODE_UTILISATEUR. Classe créée le
 * 22/06/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@Entity
@Table(name = "TRANSCODE_UTILISATEUR")
public class TranscodeUtilisateur implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer transcodeUtilisateurId;
	private CodeUtilisateur codeUtilisateur;
	private Integer codeId;
	private TableCodage tableCodage;

	/** Constructeur par défaut. */
	public TranscodeUtilisateur() {
	}

	@Id
	@Column(name = "TRANSCODE_UTILISATEUR_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getTranscodeUtilisateurId() {
		return this.transcodeUtilisateurId;
	}

	public void setTranscodeUtilisateurId(final Integer id) {
		this.transcodeUtilisateurId = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODE_UTILISATEUR_ID", nullable = false)
	public CodeUtilisateur getCodeUtilisateur() {
		return codeUtilisateur;
	}

	public void setCodeUtilisateur(final CodeUtilisateur codeU) {
		this.codeUtilisateur = codeU;
	}

	@Column(name = "CODE_ID", nullable = false)
	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(final Integer c) {
		this.codeId = c;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TABLE_CODAGE_ID", nullable = false)
	public TableCodage getTableCodage() {
		return this.tableCodage;
	}

	public void setTableCodage(final TableCodage c) {
		this.tableCodage = c;
	}

	/**
	 * 2 transcodes sont considérés comme égaux s'ils ont les mêmes valeurs de clefs
	 * etrangères.
	 * 
	 * @param obj est le code à tester.
	 * @return true si les codes sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		// instanceOf est utilise plutot que != a cause des proxys
		// JPA qui sont crées par lors du fetch par
		// la relation manyToAny
		if ((obj == null) || !(obj instanceof TranscodeUtilisateur)) {
			return false;
		}
		final TranscodeUtilisateur test = (TranscodeUtilisateur) obj;
		return Objects.equals(codeId, test.getCodeId())
			&& Objects.equals(codeUtilisateur, test.getCodeUtilisateur()) 
			&& Objects.equals(tableCodage, test.getTableCodage());
	}

	/**
	 * Le hashcode est calculé sur les attributs code, table codage et echantillon.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashCode = 0;
		int hashCodeUtilisateur = 0;
		int hashTable = 0;

		if (this.codeId != null) {
			hashCode = this.codeId.hashCode();
		}
		if (this.codeUtilisateur != null) {
			hashCodeUtilisateur = this.codeUtilisateur.hashCode();
		}
		if (this.tableCodage != null) {
			hashTable = this.tableCodage.hashCode();
		}

		hash = 31 * hash + hashCode;
		hash = 31 * hash + hashCodeUtilisateur;
		hash = 31 * hash + hashTable;

		return hash;

	}
}
