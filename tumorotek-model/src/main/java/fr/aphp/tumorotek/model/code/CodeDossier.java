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
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table CODE_DOSSIER. Enregistrement des dossiers
 * permettant d'organiser les codes personnels aux utilisateur et les 'favoris'.
 *
 * Classe créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Entity
@Table(name = "CODE_DOSSIER")
//@NamedQueries(
//		value = {
//				@NamedQuery(name = "CodeDossier.findByNomLike",
//						query = "SELECT c FROM CodeDossier c WHERE c.nom like ?1 " + "AND c.banque = ?2"),
//				@NamedQuery(name = "CodeDossier.findByCodeDossierParent",
//				query = "SELECT c FROM CodeDossier c " + "WHERE c.dossierParent = ?1"),
//				@NamedQuery(name = "CodeDossier.findByRootCodeDossierUtilisateur",
//				query = "SELECT c FROM CodeDossier c " + "WHERE c.dossierParent is null " + "AND c.banque = ?1 AND c.codeSelect = 0"),
//				@NamedQuery(name = "CodeDossier.findByRootCodeDossierSelect",
//				query = "SELECT c FROM CodeDossier c " + "WHERE c.dossierParent is null " + "AND c.banque = ?2 AND c.utilisateur = ?1 "
//						+ "AND c.codeSelect = 1"),
//				@NamedQuery(name = "CodeDossier.findBySelectUtilisateurAndBanque",
//				query = "SELECT c FROM CodeDossier c " + "WHERE c.utilisateur = ?1 AND c.banque = ?2 " + "AND c.codeSelect = 1"),
//				@NamedQuery(name = "CodeDossier.findByUtilisateurAndBanque",
//				query = "SELECT c FROM CodeDossier c " + "WHERE c.utilisateur = ?1 AND c.banque = ?2 " + "AND c.codeSelect = 0"),
//				@NamedQuery(name = "CodeDossier.findByRootDossierBanque",
//				query = "SELECT c FROM CodeDossier c " + "WHERE c.dossierParent is null " + "AND c.banque = ?1 "
//						+ "AND c.codeSelect = ?2"),
//				@NamedQuery(name = "CodeDossier.findByExcludedId", query = "SELECT c FROM CodeDossier c " + "WHERE c.codeDossierId != ?1")})
public class CodeDossier implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codeDossierId;
	private String nom;
	private String description;
	private Boolean codeSelect;
	private CodeDossier dossierParent;
	private Utilisateur utilisateur;
	private Banque banque;

	public CodeDossier() {
	}

	@Id
	@Column(name = "CODE_DOSSIER_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getCodeDossierId() {
		return codeDossierId;
	}

	public void setCodeDossierId(final Integer cDosId) {
		this.codeDossierId = cDosId;
	}

	@Column(name = "NOM", nullable = false, length = 25)
	public String getNom() {
		return nom;
	}

	public void setNom(final String n) {
		this.nom = n;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return description;
	}

	public void setDescription(final String descr) {
		this.description = descr;
	}

	@Column(name = "CODESELECT", nullable = false)
	public Boolean getCodeSelect() {
		return codeSelect;
	}

	public void setCodeSelect(final Boolean codeSel) {
		this.codeSelect = codeSel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOSSIER_PARENT_ID", nullable = true)
	public CodeDossier getDossierParent() {
		return dossierParent;
	}

	public void setDossierParent(final CodeDossier dosParent) {
		this.dossierParent = dosParent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTILISATEUR_ID", nullable = true)
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(final Utilisateur u) {
		this.utilisateur = u;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANQUE_ID", nullable = true)
	public Banque getBanque() {
		return banque;
	}

	public void setBanque(final Banque bank) {
		this.banque = bank;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		// instanceOf est utilise plutot que != a cause des proxys
		// JPA qui sont crées par lors du fetch par
		// la relation manyToAny
		if ((obj == null) || !(obj instanceof CodeDossier)) {
			return false;
		}
		final CodeDossier test = (CodeDossier) obj;
		return Objects.equals(nom, test.getNom())
			&& Objects.equals(utilisateur, test.getUtilisateur())
			&& Objects.equals(banque, test.getBanque());
	}

	@Override
	public int hashCode() {

		int hash = 7;
		int hashNom = 0;
		int hashBanque = 0;
		int hashUtilisateur = 0;

		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.banque != null) {
			hashBanque = this.banque.hashCode();
		}
		if (this.utilisateur != null) {
			hashUtilisateur = this.utilisateur.hashCode();
		}

		hash = 7 * hash + hashNom;
		hash = 7 * hash + hashBanque;
		hash = 7 * hash + hashUtilisateur;

		return hash;
	}

	@Override
	public String toString() {
		if (this.nom != null) {
			return "{CodeDossier: " + this.nom + "}";
		}
		return "{Empty CodeDossier}";
	}

	@Override
	public CodeDossier clone() {
		final CodeDossier clone = new CodeDossier();
		clone.setCodeDossierId(this.getCodeDossierId());
		clone.setNom(this.getNom());
		clone.setCodeSelect(this.getCodeSelect());
		clone.setDescription(this.getDescription());
		clone.setDossierParent(this.getDossierParent());
		clone.setUtilisateur(this.getUtilisateur());
		clone.setBanque(this.getBanque());

		return clone;
	}
}
