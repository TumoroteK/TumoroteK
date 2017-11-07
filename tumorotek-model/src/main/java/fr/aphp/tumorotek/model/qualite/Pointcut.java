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
package fr.aphp.tumorotek.model.qualite;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Objet persistant mappant la table POINTCUT.
 * Enregistrement de point d'insertion (AOP like) 
 * d'execution de critere de conformites.
 * 
 * Date: 17/09/2009
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 * 
 */
@Entity
@Table(name = "POINTCUT")
@NamedQueries(value = {@NamedQuery(name = "Pointcut.findByNom", 
			query = "SELECT p FROM Pointcut p WHERE p.nom = ?1") })
public class Pointcut implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer pointcutId;
	private String nom;
	
	/** Constructeur par défaut. */
	public Pointcut() {	
	}
	
	@Id
	@Column(name = "POINTCUT_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getPointcutId() {
		return pointcutId;
	}
	
	public void setPointcutId(Integer id) {
		this.pointcutId = id;
	}
	
	@Column(name = "NOM", nullable = false, length = 25)
	public String getNom() {
		return nom;
	}
	
	public void setNom(String n) {
		this.nom = n;
	}
	
	/**
	 * 2 pointcuts sont consideres comme egaux si ils ont le même nom.
	 * @param obj est le pointcut à tester.
	 * @return true si les pointcuts sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		Pointcut test = (Pointcut) obj;
		if (this.pointcutId != null) { //l'objet a id assigne
			return this.pointcutId.equals(test.pointcutId); 
		} else { //l'objet a ete nouvellement cree
			return ((this.nom == test.nom || (this.nom != null 
					&& this.nom.equals(test.nom)))
				);
		}
	}

	/**
	 * Le hashcode est calculé sur le nom.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashType = 0;
		
		if (this.nom != null) {
			hashType = this.nom.hashCode();
		}
	
		hash = 31 * hash + hashType;
		
		return hash;
		
	}

}
