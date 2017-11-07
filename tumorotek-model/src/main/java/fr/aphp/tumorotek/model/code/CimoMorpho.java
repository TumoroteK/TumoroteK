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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * Objet persistant mappant la table CIMO_MORPHO.
 * Classe créée le 11/09/09.
 * 
 * @author Maxime Gousseau
 * @version 2.0
 * 
 */
@Entity
@Table(name = "CIMO_MORPHO")
@NamedQueries(value = {@NamedQuery(name = "CimoMorpho.findByCodeLike", 
			query = "SELECT c FROM CimoMorpho c WHERE c.code like ?1"),
		@NamedQuery(name = "CimoMorpho.findByLibelleLike", 
			query = "SELECT c FROM CimoMorpho c WHERE c.libelle like ?1"),
		@NamedQuery(name = "CimoMorpho.findByCimRefLike", 
			query = "SELECT c FROM CimoMorpho c WHERE c.cimRef like ?1")
			})
public class CimoMorpho implements CodeCommon, Serializable {

	private static final long serialVersionUID = 3570913607410757679L;
	
	private Integer cimoMorphoId;
	private String code;
	private String libelle;
	private String cimRef;
	
	private Set<Adicap> adicaps = new HashSet<Adicap>();
	
	private CodeSelect codeSelect;

	/** Constructeur par défaut. */
	public CimoMorpho() {
	}
	
	public String toString() {
		return "{CimoMorpho: " + this.code + "}";
	}


	@Id
	@Column(name = "CIMO_MORPHO_ID", unique = true, nullable = false)
	public Integer getCimoMorphoId() {
		return this.cimoMorphoId;
	}

	public void setCimoMorphoId(Integer id) {
		this.cimoMorphoId = id;
	}

	@Override
	@Column(name = "CODE", nullable = false, length = 10)
	public String getCode() {
		return this.code;
	}
	
	@Override
	public void setCode(String c) {
		this.code = c;
	}
	
	@Override
	@Column(name = "LIBELLE", nullable = false, length = 100)
	public String getLibelle() {
		return this.libelle;
	}
	
	@Override
	public void setLibelle(String lib) {
		this.libelle = lib;
	}

	@Column(name = "CIM_REF", nullable = true, length = 50)
	public String getCimRef() {
		return this.cimRef;
	}

	public void setCimRef(String cim) {
		this.cimRef = cim;
	}

	@ManyToMany(
	        mappedBy = "cimoMorphos",
	        targetEntity = Adicap.class
	)
	public Set<Adicap> getAdicaps() {
		return this.adicaps;
	}

	public void setAdicaps(Set<Adicap> adis) {
		this.adicaps = adis;
	}

	/**
	 * 2 CIMO sont considérés comme égaux s'ils ont le même ID.
	 * @param obj est le CIMO à tester.
	 * @return true si les CIMOs sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		// instanceOf est utilise plutot que != a cause des instances
		// CimoMorpho$$EnhancerByCGLIB qui sont crées par lors du fetch par 
		// la relation manyToAny
		if ((obj == null) || !(obj instanceof CimoMorpho)) {
			return false;
		}
		CimoMorpho test = (CimoMorpho) obj;
		if (this.getCimoMorphoId() != null) {
			// utilisation get car CimoMoprho$$EnhancerByCGLIB proxy
			return this.getCimoMorphoId().equals(test.getCimoMorphoId()); 
		} else { //impossible
			return false;
		}
	}

	/**
	 * Le hashcode est calculé sur l'id.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashID = 0;
		
		if (this.cimoMorphoId != null) {
			hashID = this.cimoMorphoId.hashCode();
		}
		
		hash = 31 * hash + hashID;
		
		return hash;
	}
	
	@Override
	public CimoMorpho clone() {
		return this;
	}

	@Override
	@Transient
	public Integer getCodeId() {
		return getCimoMorphoId();
	}

	@Override
	public void setCodeId(Integer id) {
		this.cimoMorphoId = id;
	}
	
	@Override
	@Transient
	public CodeSelect getCodeSelect() {
		return codeSelect;
	}

	@Override
	public void setCodeSelect(CodeSelect s) {
		codeSelect = s;		
	}
}
