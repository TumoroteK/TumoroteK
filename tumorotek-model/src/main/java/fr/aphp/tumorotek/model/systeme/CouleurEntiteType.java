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

import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * 
 * Objet persistant mappant la table COULEUR_ENTITE_TYPE.
 * Classe créée le 29/04/10.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "COULEUR_ENTITE_TYPE")
@NamedQueries(value = {@NamedQuery(name = "CouleurEntiteType.findByBanque",
			query = "SELECT c FROM CouleurEntiteType c WHERE c.banque like ?1"),
		@NamedQuery(name = "CouleurEntiteType.findByBanqueAllEchanType",
			query = "SELECT c FROM CouleurEntiteType c " 
				+ "WHERE c.banque like ?1 AND c.prodType is null"),
		@NamedQuery(name = "CouleurEntiteType.findByBanqueAllProdType",
			query = "SELECT c FROM CouleurEntiteType c " 
				+ "WHERE c.banque like ?1 AND c.echantillonType is null"),
		@NamedQuery(name = "CouleurEntiteType.findByExcludedId",
			query = "SELECT c FROM CouleurEntiteType c " 
				+ "WHERE c.couleurEntiteTypeId != ?1") })
public class CouleurEntiteType implements Serializable {

	private static final long serialVersionUID = 2235148194681273455L;
	
	private Integer couleurEntiteTypeId;
	
	private Couleur couleur;
	private Banque banque;
	private EchantillonType echantillonType;
	private ProdType prodType;
	
	/** Constructeur par défaut. */
	public CouleurEntiteType() {
	
	}

	@Id
	@Column(name = "COULEUR_ENTITE_TYPE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getCouleurEntiteTypeId() {
		return couleurEntiteTypeId;
	}

	public void setCouleurEntiteTypeId(Integer cId) {
		this.couleurEntiteTypeId = cId;
	}

	@ManyToOne()
	@JoinColumn(name = "COULEUR_ID", nullable = false)
	public Couleur getCouleur() {
		return couleur;
	}

	public void setCouleur(Couleur c) {
		this.couleur = c;
	}

	@ManyToOne()
	@JoinColumn(name = "BANQUE_ID", nullable = false)
	public Banque getBanque() {
		return banque;
	}

	public void setBanque(Banque b) {
		this.banque = b;
	}

	@ManyToOne()
	@JoinColumn(name = "ECHANTILLON_TYPE_ID", nullable = true)
	public EchantillonType getEchantillonType() {
		return echantillonType;
	}

	public void setEchantillonType(EchantillonType eType) {
		this.echantillonType = eType;
	}

	@ManyToOne()
	@JoinColumn(name = "PROD_TYPE_ID", nullable = true)
	public ProdType getProdType() {
		return prodType;
	}

	public void setProdType(ProdType pType) {
		this.prodType = pType;
	}
	
	/**
	 * 2 couleursentites sont considérées comme égales si elles ont les
	 * mêmes attributs.
	 * @param obj.
	 * @return true si elles sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		CouleurEntiteType test = (CouleurEntiteType) obj;
		// 2 CouleurEntiteType sont egales si toutes leurs valeurs le sont
		return ((this.banque == test.banque || (this.banque != null 
						&& this.banque.equals(test.banque))) 
				&& (this.echantillonType == test.echantillonType 
						|| (this.echantillonType != null 
						&& this.echantillonType.equals(test.echantillonType))) 
				&& (this.prodType == test.prodType || (this.prodType != null 
						&& this.prodType.equals(test.prodType))));
	}

	/**
	 * Le hashcode est calculé sur tous les attributs.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashBanque = 0;
		int hashEchantillonType = 0;
		int hashProdType = 0;
		
		if (this.banque != null) {
			hashBanque = this.banque.hashCode();
		}
		if (this.echantillonType != null) {
			hashEchantillonType = this.echantillonType.hashCode();
		}
		if (this.prodType != null) {
			hashProdType = this.prodType.hashCode();
		}
		
		hash = 3 * hash + hashBanque;
		hash = 3 * hash + hashEchantillonType;
		hash = 3 * hash + hashProdType;
		
		return hash;
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.couleur != null && this.banque != null) {
		return "{" + this.couleur + ", " + this.banque + ", " 
		+ this.echantillonType 
		+ ", " + this.prodType + "}";
		} else {
			return "{Empty CouleurEntiteType}";
		}
	}
	
	/**
	 * Cree un clone de l'objet.
	 * @return clone Coordonnee.
	 */
	public CouleurEntiteType clone() {
		CouleurEntiteType clone = new CouleurEntiteType();
		
		clone.setCouleurEntiteTypeId(this.couleurEntiteTypeId);
		clone.setCouleur(this.couleur);
		clone.setBanque(this.banque);
		clone.setEchantillonType(this.echantillonType);
		clone.setProdType(this.prodType);
		
		return clone;
	}
}
