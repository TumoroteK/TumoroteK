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

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Objet persistant mappant la table CODE_SELECT.
 * Classe créée le 17/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "CODE_SELECT")
@NamedQueries(value = {
		@NamedQuery(name = "CodeSelect.findByUtilisateurAndBanque",
			query = "SELECT c FROM CodeSelect c " 
				+ "WHERE c.utilisateur = ?1 AND c.banque = ?2"),
		@NamedQuery(name = "CodeSelect.findByBanque",
				query = "SELECT c FROM CodeSelect c " 
					+ "WHERE c.banque = ?1 " 
					+ "ORDER BY c.codeSelectId"),
		@NamedQuery(name = "CodeSelect.findByCodeDossier",
			query = "SELECT c FROM CodeSelect c WHERE c.codeDossier = ?1 "
				+ "ORDER BY c.codeSelectId"),
		@NamedQuery(name = "CodeSelect.findByRootDossier",
			query = "SELECT c FROM CodeSelect c WHERE c.codeDossier is null "
				+ "AND c.utilisateur= ?1 AND c.banque = ?2 " 
				+ "ORDER BY c.codeSelectId"),
		@NamedQuery(name = "CodeSelect.findByRootDossierAndBanque",
				query = "SELECT c FROM CodeSelect c WHERE c.codeDossier is null "
					+ "AND c.banque = ?1 " 
					+ "ORDER BY c.codeSelectId"),
		@NamedQuery(name = "CodeSelect.findByExcludedId", 
			query = "SELECT c FROM CodeSelect c "
				+ "WHERE c.codeSelectId != ?1")
				})
public class CodeSelect implements Serializable {

	private static final long serialVersionUID = 478456654635153L;
	
	private Integer codeSelectId;
	private Integer codeId;	
	private TableCodage tableCodage;
	private Utilisateur utilisateur;
	private Banque banque;
	private CodeDossier codeDossier;


	/** Constructeur par défaut. */
	public CodeSelect() {		
	}
	
	public String toString() {
		return "{CodeSelect: "
			+ this.tableCodage.getNom() + "." + this.codeId + "}";
	}

	@Id
	@Column(name = "CODE_SELECT_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getCodeSelectId() {
		return this.codeSelectId;
	}

	public void setCodeSelectId(Integer id) {
		this.codeSelectId = id;
	}

	@Column(name = "CODE_ID", nullable = false)
	public Integer getCodeId() {
		return this.codeId;
	}

	public void setCodeId(Integer code) {
		this.codeId = code;
	}

	@ManyToOne
	@JoinColumn(name = "TABLE_CODAGE_ID", nullable = false)
	public TableCodage getTableCodage() {
		return this.tableCodage;
	}

	public void setTableCodage(TableCodage table) {
		this.tableCodage = table;
	}

	@ManyToOne
	@JoinColumn(name = "UTILISATEUR_ID", nullable = false)
	public Utilisateur getUtilisateur() {
		return this.utilisateur;
	}

	public void setUtilisateur(Utilisateur util) {
		this.utilisateur = util;
	}

	@ManyToOne
	@JoinColumn(name = "BANQUE_ID", nullable = false)
	public Banque getBanque() {
		return this.banque;
	}

	public void setBanque(Banque bank) {
		this.banque = bank;
	}
	
	@ManyToOne
	@JoinColumn(name = "CODE_DOSSIER_ID", nullable = true)
	public CodeDossier getCodeDossier() {
		return codeDossier;
	}

	public void setCodeDossier(CodeDossier dos) {
		this.codeDossier = dos;
	}

	/**
	 * 2 codes select sont considérés comme égaux s'ils ont les mêmes
	 * attributs.
	 * @param obj est le code à tester.
	 * @return true si les codes sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		CodeSelect test = (CodeSelect) obj;
		return ((this.codeId == test.codeId || (this.codeId != null 
					&& this.codeId.equals(test.codeId))) 
			&& (this.banque == test.banque || (this.banque != null 
					&& this.banque.equals(test.banque))) 
			&& (this.tableCodage == test.tableCodage 
					|| (this.tableCodage != null 
					&& this.tableCodage.equals(test.tableCodage))) 
			&& (this.utilisateur == test.utilisateur 
					|| (this.utilisateur != null 
					&& this.utilisateur.equals(test.utilisateur))));
	}

	/**
	 * Le hashcode est calculé sur tous les attributs.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashCode = 0;
		int hashBanque = 0;
		int hashTable = 0;
		int hashUtilisateur = 0;
		
		if (this.codeId != null) {
			hashCode = this.codeId.hashCode();
		}
		if (this.banque != null) {
			hashBanque = this.banque.hashCode();
		}
		if (this.tableCodage != null) {
			hashTable = this.tableCodage.hashCode();
		}
		if (this.utilisateur != null) {
			hashUtilisateur = this.utilisateur.hashCode();
		}
		
		hash = 7 * hash + hashCode;
		hash = 7 * hash + hashBanque;
		hash = 7 * hash + hashTable;
		hash = 7 * hash + hashUtilisateur;
		
		return hash;
	}
}
