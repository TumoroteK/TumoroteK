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
package fr.aphp.tumorotek.model.systeme;

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

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Objet persistant mappant la table NUMEROTATION.
 * Enregistrement des templates de numerotation automatique
 * des codes. Ces codes sont composes d'une formule
 * avec un chiffre incremente d'une certaine longueur, eventuellement
 * zero-filled.
 * 
 * Date: 15/09/2009
 * 
 * @author Mathieu Barthelemy
 * @version 2.0
 * 
 */
@Entity
@Table(name = "NUMEROTATION")
@NamedQueries(value = {@NamedQuery(name = "Numerotation.findByEntite", 
					query = "SELECT n FROM Numerotation n"
						+ " WHERE n.entite = ?1"),
			@NamedQuery(name = "Numerotation.findByBanques", 
				query = "SELECT n FROM Numerotation n"
					+ " WHERE n.banque in (?1)" 
					+ " ORDER BY n.banque, n.entite"),
			@NamedQuery(name = "Numerotation.findByBanqueAndEntite", 
				query = "SELECT n FROM Numerotation n"
					+ " WHERE n.banque = ?1 AND n.entite = ?2"),
			@NamedQuery(name = "Numerotation.findByBanqueSelectEntite", 
				query = "SELECT n.entite FROM Numerotation n"
					+ " WHERE n.banque = ?1"),
			@NamedQuery(name = "Numerotation.findByExcludedId", 
				query = "SELECT n FROM Numerotation n"
					+ " WHERE n.numerotationId != ?1") })
public class Numerotation implements TKdataObject, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer numerotationId;
	private String codeFormula;
	private Integer currentIncrement;
	private Integer startIncrement;
	private Integer nbChiffres;
	private Boolean zeroFill;
	private Entite entite;
	private Banque banque;

	/** Constructeur par défaut. */
	public Numerotation() {
	}
	
	@Id
	@Column(name = "NUMEROTATION_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getNumerotationId() {
		return this.numerotationId;
	}

	public void setNumerotationId(Integer id) {
		this.numerotationId = id;
	}
	
	@Column(name = "CODE_FORMULA", nullable = false, length = 25)
	public String getCodeFormula() {
		return this.codeFormula;
	}

	public void setCodeFormula(String formula) {
		this.codeFormula = formula;
	}
	
	@Column(name = "CURRENT_INCREMENT", nullable = false, length = 5)
	public Integer getCurrentIncrement() {
		return this.currentIncrement;
	}

	public void setCurrentIncrement(Integer incr) {
		this.currentIncrement = incr;
	}
	
	@Column(name = "START_INCREMENT", nullable = true, length = 5)
	public Integer getStartIncrement() {
		return this.startIncrement;
	}

	public void setStartIncrement(Integer incr) {
		this.startIncrement = incr;
	}
	
	@Column(name = "NB_CHIFFRES", nullable = true, length = 2)
	public Integer getNbChiffres() {
		return this.nbChiffres;
	}

	public void setNbChiffres(Integer nb) {
		this.nbChiffres = nb;
	}
	
	@Column(name = "ZERO_FILL", nullable = true)
	public Boolean getZeroFill() {
		return this.zeroFill;
	}

	public void setZeroFill(Boolean zero) {
		this.zeroFill = zero;
	}
	
	@ManyToOne
	@JoinColumn(name = "ENTITE_ID", nullable = false)
	public Entite getEntite() {
		return this.entite;
	}

	public void setEntite(Entite en) {
		this.entite = en;
	}
	
	@ManyToOne
	@JoinColumn(name = "BANQUE_ID", nullable = false)
	public Banque getBanque() {
		return this.banque;
	}

	public void setBanque(Banque b) {
		this.banque = b;
	}
	
	/**
	 * Deux numerotations sont considerees comme egales
	 * si elles partagent la meme formule et si elles
	 * sont associees a la meme entite. Les numerotations
	 * sont partagees par le systeme qui bloquera l'enregistrement
	 * de doublons.
	 * @param obj est la numerotation à tester.
	 * @return true si les numerotations sont egales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		Numerotation test = (Numerotation) obj;
		return ((this.banque == test.banque
				|| (this.banque != null 
						&& this.banque.equals(test.banque))) 
				&& (this.entite == test.entite 
						|| (this.entite != null 
						&& this.entite.equals(test.entite))) 
						);
	}

	/**
	 * Le hashcode est calculé sur la formule et la reference vers 
	 * l'entite.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashBanque = 0;
		int hashEntite = 0;
		
		if (this.banque != null) {
			hashBanque = this.banque.hashCode();
		}
		if (this.entite != null) {
			hashEntite = this.entite.hashCode();
		}
		
		hash = 31 * hash + hashBanque;
		hash = 31 * hash + hashEntite;
		
		return hash;
	}

	@Override
	public String toString() {
		if (this.codeFormula != null) {
			return "{" + this.codeFormula + "}";
		} else {
			return "{Empty Numerotation}";
		}
	}
	
	public Numerotation clone() {
		Numerotation clone = new Numerotation();
		clone.setNumerotationId(this.numerotationId);
		clone.setBanque(this.banque);
		clone.setEntite(this.entite);
		clone.setCodeFormula(this.codeFormula);
		clone.setCurrentIncrement(this.currentIncrement);
		clone.setStartIncrement(this.startIncrement);
		clone.setNbChiffres(this.nbChiffres);
		clone.setZeroFill(this.zeroFill);
		return clone;
	}

	@Override
	public Integer listableObjectId() {
		return getNumerotationId();
	}

}

