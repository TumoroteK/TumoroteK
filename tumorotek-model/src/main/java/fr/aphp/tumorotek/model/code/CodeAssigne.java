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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 * 
 * Objet persistant mappant la table CODE_ASSIGNE.
 * Classe créée le 17/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "CODE_ASSIGNE")
@NamedQueries(value = {@NamedQuery(name = "CodeAssigne.findByCodeLike",
			query = "SELECT c FROM CodeAssigne c WHERE c.code like ?1"),
		@NamedQuery(name = "CodeAssigne.findByCodeAndEchantillon",
			query = "SELECT c FROM CodeAssigne c WHERE c.code like ?1 "
					+ " AND c.echantillon = ?2"),
		@NamedQuery(name = "CodeAssigne.findByLibelleLike",
			query = "SELECT c FROM CodeAssigne c WHERE c.libelle like ?1"),
		@NamedQuery(name = "CodeAssigne.findCodesMorphoByEchantillon", 
			query = "SELECT c FROM CodeAssigne c " 
						+ "WHERE c.echantillon = ?1 AND c.isMorpho = 1 "
						+ "ORDER BY c.ordre"),
		@NamedQuery(name = "CodeAssigne.findCodesOrganeByEchantillon", 
			query = "SELECT c FROM CodeAssigne c " 
					+ "WHERE c.echantillon = ?1 AND c.isOrgane = 1 "
					+ "ORDER BY c.ordre"),
		@NamedQuery(name = "CodeAssigne.findByExcludedId", 
			query = "SELECT c FROM CodeAssigne c "
					+ "WHERE c.codeAssigneId != ?1 and c.code = ?2 "
					+ "AND c.echantillon = ?3"),
		@NamedQuery(name = "CodeAssigne.findCodesLesExportedByPrelevement", 
				query = "SELECT c FROM CodeAssigne c "
					+ "WHERE c.echantillon.prelevement = ?1 "
					+ "AND c.export = 1 AND c.isMorpho = 1"
					+ "ORDER BY c.echantillon.echantillonId"), 
		@NamedQuery(name = "CodeAssigne.findCodesOrgExportedByPrelevement", 
				query = "SELECT c FROM CodeAssigne c "
					+ "WHERE c.echantillon.prelevement = ?1 "
					+ "AND c.export = 1 AND c.isOrgane = 1"
					+ "ORDER BY c.echantillon.echantillonId"),
		@NamedQuery(name = "CodeAssigne.findCodesOrgExportedByPatient", 
				query = "SELECT c FROM CodeAssigne c "
					+ "WHERE c.echantillon.prelevement.maladie.patient = ?1 "
					+ "AND c.export = 1 AND c.isOrgane = 1"
					+ "ORDER BY c.echantillon.echantillonId")
})
public class CodeAssigne implements CodeCommon, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer codeAssigneId;
	private String code;
	private String libelle;
	private Boolean isMorpho;
	private Integer codeRefId;
	private Boolean isOrgane;
	private TableCodage tableCodage;
	private Echantillon echantillon;
	private Integer ordre = 1;
	private Boolean export = false;
	
	//private Echantillon echanExpOrg;
	//private Echantillon echanExpLes;

	/** Constructeur par défaut. */
	public CodeAssigne() {		
	}

	@Id
	@Column(name = "CODE_ASSIGNE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	// @GenericGenerator(name = "autoincrement", strategy = "increment")
	@GenericGenerator(name = "autoincrement", strategy = "native", 
		parameters = {@Parameter(name="sequence", value="codeAssigneSeq")})
	public Integer getCodeAssigneId() {
		return this.codeAssigneId;
	}

	public void setCodeAssigneId(Integer id) {
		this.codeAssigneId = id;
	}

	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String c) {
		this.code = c;
	}

	@Column(name = "LIBELLE", nullable = true, length = 300)
	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String lib) {
		this.libelle = lib;
	}

	@Column(name = "IS_MORPHO", nullable = true)
	public Boolean getIsMorpho() {
		return this.isMorpho;
	}

	public void setIsMorpho(Boolean morpho) {
		this.isMorpho = morpho;
	}
	
	@Column(name = "IS_ORGANE", nullable = false)
	public Boolean getIsOrgane() {
		return isOrgane;
	}

	public void setIsOrgane(Boolean isO) {
		this.isOrgane = isO;
	}

	@Column(name = "CODE_REF_ID", nullable = true)
	public Integer getCodeRefId() {
		return this.codeRefId;
	}

	public void setCodeRefId(Integer codeRef) {
		this.codeRefId = codeRef;
	}

	@ManyToOne
	@JoinColumn(name = "TABLE_CODAGE_ID", nullable = true)
	public TableCodage getTableCodage() {
		return this.tableCodage;
	}

	public void setTableCodage(TableCodage c) {
		this.tableCodage = c;
	}

	@ManyToOne
	@JoinColumn(name = "ECHANTILLON_ID", nullable = false)
	public Echantillon getEchantillon() {
		return this.echantillon;
	}

	public void setEchantillon(Echantillon echan) {
		this.echantillon = echan;
	}
	
	@Column(name = "ORDRE", nullable = false, length = 3)
	public Integer getOrdre() {
		return ordre;
	}

	public void setOrdre(Integer o) {
		this.ordre = o;
	}

	@Column(name = "EXPORT", nullable = false)
	public Boolean getExport() {
		return export;
	}

	public void setExport(Boolean b) {
		this.export = b;
	}

	/**
	 * 2 codes sont considérés comme égaux s'ils ont les mêmes
	 * code venant de la meme table de codage et la meme reference
	 * vers l'échantillon.
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
		CodeAssigne test = (CodeAssigne) obj;
		return ((this.echantillon == test.echantillon 
					|| (this.echantillon != null 
							&& this.echantillon.equals(test.echantillon)))
				&& (this.code == test.code 
						|| (this.code != null 
								&& this.code.equals(test.code))) 
				&& (this.libelle == test.libelle 
						|| (this.libelle != null 
								&& this.libelle.equals(test.libelle)))
				&& (this.codeRefId == test.codeRefId 
						|| (this.codeRefId != null 
								&& this.codeRefId.equals(test.codeRefId))) 
				&& (this.tableCodage == test.tableCodage 
						|| (this.tableCodage != null 
						&& this.tableCodage.equals(test.tableCodage)))
				&& (this.isOrgane == test.isOrgane 
						|| (this.isOrgane != null 
								&& this.isOrgane.equals(test.isOrgane)))
				&& (this.isMorpho == test.isMorpho
						|| (this.isMorpho != null 
								&& this.isMorpho.equals(test.isMorpho)))
			);
	}

	/**
	 * Le hashcode est calculé sur les attributs code, table
	 * codage et echantillon.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashCode = 0;
		int hashLibelle = 0;
		int hashCodeRef = 0;
		int hashEchantillon = 0;
		int hashTable = 0;
		int hashIsOrgane = 0;
		int hashIsMorpho = 0;
		
		if (this.code != null) {
			hashCode = this.code.hashCode();
		}
		if (this.libelle != null) {
			hashLibelle = this.libelle.hashCode();
		}
		if (this.echantillon != null) {
			hashEchantillon = this.echantillon.hashCode();
		}
		if (this.codeRefId != null) {
			hashCodeRef = this.codeRefId.hashCode();
		}
		if (this.tableCodage != null) {
			hashTable = this.tableCodage.hashCode();
		}
		if (this.isOrgane != null) {
			hashIsOrgane = this.isOrgane.hashCode();
		}
		if (this.isMorpho != null) {
			hashIsOrgane = this.isMorpho.hashCode();
		}
		
		hash = 31 * hash + hashCode;
		hash = 31 * hash + hashLibelle;
		hash = 31 * hash + hashEchantillon;
		hash = 31 * hash + hashCodeRef;
		hash = 31 * hash + hashTable;
		hash = 31 * hash + hashIsMorpho;
		hash = 31 * hash + hashIsOrgane;
		
		return hash;
		
	}
	
	@Override
	public String toString() {
		if (this.code != null) {
			if (isOrgane) {
				return "{CodeOrgane: " + this.code + "}";
			} else if (isMorpho != null && isMorpho) {
				return "{CodeMorpho: " + this.code + "}";
			} else {
				return "{CodeDiag: " + this.code + "}";
			}
		} else {
			return "{Empty CodeAssigne}";
		}
	}
	
	@Override
	public CodeAssigne clone() {
		CodeAssigne clone = new CodeAssigne();
		clone.setCodeAssigneId(getCodeAssigneId());
		clone.setEchantillon(getEchantillon());
		clone.setCode(getCode());
		clone.setLibelle(getLibelle());
		clone.setIsOrgane(getIsOrgane());
		clone.setIsMorpho(getIsMorpho());
		clone.setCodeRefId(getCodeRefId());
		clone.setTableCodage(getTableCodage());
		//clone.setEchanExpLes(getEchanExpLes());
		//clone.setEchanExpOrg(getEchanExpOrg());
		clone.setOrdre(getOrdre());
		clone.setExport(getExport());
		return clone;
	}

	@Override
	@Transient
	public Integer getCodeId() {
		return getCodeAssigneId();
	}

	@Override
	public void setCodeId(Integer id) {
		this.codeAssigneId = id;		
	}
		
	@Override
	@Transient
	public CodeSelect getCodeSelect() {
		return null;
	}

	@Override
	public void setCodeSelect(CodeSelect s) {	
	}
	
	
//	@OneToOne(mappedBy = "codeOrganeExport", optional = true)
//	public Echantillon getEchanExpOrg() {
//		return echanExpOrg;
//	}
//	
//	public void setEchanExpOrg(Echantillon e) {
//		this.echanExpOrg = e;
//	}
//	
//	@OneToOne(mappedBy = "codeLesExport", optional = true)
//	public Echantillon getEchanExpLes() {
//		return echanExpLes;
//	}
//	
//	public void setEchanExpLes(Echantillon e) {
//		this.echanExpLes = e;
//	}
}
