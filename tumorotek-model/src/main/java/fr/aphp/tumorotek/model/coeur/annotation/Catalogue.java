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
package fr.aphp.tumorotek.model.coeur.annotation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Contexte;

/**
 * Objet persistant mappant la table CATALOGUE.
 * 
 * Date: 18/03/2010
 * 
 * @author Mathieu Barthelemy
 * @version 2.0
 * 
 */
@Entity
@Table(name = "CATALOGUE")
@NamedQueries(value = {@NamedQuery(name = "Catalogue.findNoms", 
				query = "SELECT c.nom FROM Catalogue c order by c.nom"),
		@NamedQuery(name = "Catalogue.findByAssignedBanque", 
				query = "SELECT b.catalogues FROM Banque b " 
					+ "WHERE b = ?1")
})
public class Catalogue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer catalogueId;
	private String nom;
	private String description;
	private String icone;

	private Set<TableAnnotation> tableAnnotations = 
								new HashSet<TableAnnotation>();
	private Set<Contexte> contextes = new HashSet<Contexte>();
	private Set<Banque> banques = new HashSet<Banque>();
	
	/** Constructeur par défaut. */
	public Catalogue() {
	}
	
	@Id
	@Column(name = "CATALOGUE_ID" , unique = true, nullable = false)
	public Integer getCatalogueId() {
		return catalogueId;
	}
	
	public void setCatalogueId(Integer catId) {
		this.catalogueId = catId;
	}
	
	@Column(name = "NOM", nullable = false, length = 25)
	public String getNom() {
		return nom;
	}
	
	public void setNom(String n) {
		this.nom = n;
	}
	
	@Column(name = "DESCRIPTION", nullable = true, length = 250)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String descr) {
		this.description = descr;
	}
	
	@Column(name = "ICONE", nullable = true, length = 100)
	public String getIcone() {
		return this.icone;
	}
	
	public void setIcone(String path) {
		this.icone = path;
	}
	
	@ManyToMany(
			targetEntity = Contexte.class
	)
    @JoinTable(
    		name = "CATALOGUE_CONTEXTE",
            joinColumns = @JoinColumn(name = "CATALOGUE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CONTEXTE_ID")
    )
	public Set<Contexte> getContextes() {
		return contextes;
	}

	public void setContextes(Set<Contexte> conts) {
		this.contextes = conts;
	}
	
	@OneToMany(mappedBy = "catalogue")
	public Set<TableAnnotation> getTableAnnotations() {
		return tableAnnotations;
	}

	public void setTableAnnotations(Set<TableAnnotation> tabs) {
		this.tableAnnotations = tabs;
	}
	
	@ManyToMany(mappedBy = "catalogues", targetEntity = Banque.class)
	public Set<Banque> getBanques() {
		return banques;
	}

	public void setBanques(Set<Banque> b) {
		this.banques = b;
	}

	/**
	 * 2 catalogues sont consideres comme egaux si ils ont le même nom.
	 * @param obj est le catalogue à tester.
	 * @return true si les catalogues sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		Catalogue test = (Catalogue) obj;

		return ((this.nom == test.nom 
					|| (this.nom != null 
							&& this.nom.equals(test.nom))) 
		);
	}

	/**
	 * Le hashcode est calculé sur le nom.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashNom = 0;
		
		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
	
		hash = 31 * hash + hashNom;
		
		return hash;
	}
	
	@Override
	public String toString() {
		if (this.nom != null) {
			return "{" + this.nom + "}";
		} else {
			return "{Empty Catalogue}";
		}
	}
}
