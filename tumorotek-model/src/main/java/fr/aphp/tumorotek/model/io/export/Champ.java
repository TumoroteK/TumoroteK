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
package fr.aphp.tumorotek.model.io.export;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Objet persistant mappant la table CHAMP.
 * Classe créée le 23/10/09.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 * 
 */
@Entity
@Table(name = "CHAMP")
@NamedQueries(value = { @NamedQuery(name = "Champ.findEnfants",
		query = "SELECT c FROM Champ c WHERE champParent = ?1") })
public class Champ implements Comparable<Champ> {
	
	private Integer champId;
    private ChampEntite champEntite;
    private ChampAnnotation champAnnotation;
    private Champ champParent;
    
    private Set<ChampLigneEtiquette> champLigneEtiquettes = 
		new HashSet<ChampLigneEtiquette>();
    
    public Champ() {
		super();
	}
    
    public Champ(ChampEntite chEnt) {
		super();
		this.champEntite = chEnt;
	}
    
    public Champ(ChampAnnotation chAnno) {
		super();
		this.champAnnotation = chAnno;
	}
    
    @Id
    @GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	@Column(name = "CHAMP_ID", unique = true, nullable = false)
	public Integer getChampId() {
		return champId;
	}
	public void setChampId(Integer chId) {
		this.champId = chId;
	}
	
	@ManyToOne
	@JoinColumn(name = "CHAMP_ENTITE_ID")
	public ChampEntite getChampEntite() {
		return champEntite;
	}
	public void setChampEntite(ChampEntite sousEnt) {
		this.champEntite = sousEnt;
	}
	
	@ManyToOne
	@JoinColumn(name = "CHAMP_ANNOTATION_ID")
	public ChampAnnotation getChampAnnotation() {
		return champAnnotation;
	}
	public void setChampAnnotation(ChampAnnotation champAnno) {
		this.champAnnotation = champAnno;
	}
	
	@OneToOne
    @JoinColumn(name = "CHAMP_PARENT_ID")
	public Champ getChampParent() {
		return champParent;
	}
	public void setChampParent(Champ champPar) {
		this.champParent = champPar;
	}
	
	@OneToMany(mappedBy = "champ", 
			cascade = { CascadeType.REMOVE })
	public Set<ChampLigneEtiquette> getChampLigneEtiquettes() {
		return champLigneEtiquettes;
	}

	public void setChampLigneEtiquettes(
			Set<ChampLigneEtiquette> c) {
		this.champLigneEtiquettes = c;
	}
	
	/**
	 * 2 champs sont considérés comme égaux s'ils ont la même champ-entité
	 * et le même champ-annotation.
	 * @param obj est le champ à tester.
	 * @return true si les champs sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		Champ test = (Champ) obj;
		if (this.champAnnotation == null) {
			if (test.champAnnotation == null) {
				if (this.champEntite == null) {
					return test.champEntite == null;
				} else {
					return this.champEntite.equals(test.champEntite);
				}				
			} else {
				return false;
			}
		} else if (this.champAnnotation.equals(test.champAnnotation)) {
			if (this.champEntite == null) {
				return test.champEntite == null;
			} else {
				return this.champEntite.equals(test.champEntite);
			}
		} else {
			return false;
		}
	}

	/**
	 * Le hashcode est calculé sur les attributs entité et sous-entité.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashChampAnnotation = 0;
		int hashChampEntite = 0;
		
		if (this.champAnnotation != null) {
			hashChampAnnotation = this.champAnnotation.hashCode();
		}
		if (this.champEntite != null) {
			hashChampEntite = this.champEntite.hashCode();
		}
		
		hash = 31 * hash + hashChampAnnotation;
		hash = 31 * hash + hashChampEntite;

		return hash;

	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.champAnnotation != null) {
			return this.champAnnotation.getTableAnnotation().getEntite()
					.getNom() + " " + this.getChampAnnotation().getNom();
		} else if (this.champEntite != null) {
			String champEntiteNom = this.champEntite.getNom();
			if (this.getChampEntite().getNom().matches("^[a-zA-Z]+Id$")) {
				champEntiteNom = champEntiteNom.substring(0, champEntiteNom
						.length() - 2);
			}
			if (this.champParent != null) {
				String champParentNom = this.champParent.getChampEntite().getNom().substring(0, this.champParent.getChampEntite().getNom()
						.length() - 2);
				return this.champParent.getChampEntite().getEntite().getNom()
						+ "." + champParentNom + "." + champEntiteNom;
			} else {
				return this.getChampEntite().getEntite().getNom() + "."
						+ champEntiteNom;
			}					
		}
		return "{Empty Champ}";
	}
	
	public String nom() {
		String retour = null;
		if (this.champAnnotation != null) {
			retour = this.champAnnotation.getNom();
		} else if (this.champEntite != null) {
			retour = this.champEntite.getNom();
			if (retour.matches("^[a-zA-Z]+Id$")) {
				retour = retour.substring(0, retour
						.length() - 2);
			}
		}
		return retour;
	}
	
	public DataType dataType() {
		DataType retour = null;
		if (this.champAnnotation != null) {
			retour = this.champAnnotation.getDataType();
		} else if (this.champEntite != null) {
			retour = this.champEntite.getDataType();
		}
		return retour;
	}
	
	public Entite entite() {
		Entite retour = null;
		if (this.champAnnotation != null) {
			retour = this.champAnnotation.getTableAnnotation().getEntite();
		} else if (this.champEntite != null) {
			retour = this.champEntite.getEntite();
		}
		return retour;
	}

	@Override
	public int compareTo(Champ champ) {
		return this.nom().toLowerCase().compareTo(champ.nom().toLowerCase());
	}

	public Champ copy() {
		Champ copy = new Champ();
		if (this.getChampAnnotation() != null) {
			copy.setChampAnnotation(this.getChampAnnotation());
		}
		if (this.getChampEntite() != null) {
			copy.setChampEntite(this.getChampEntite());
		}
		if (this.getChampParent() != null) {
			copy.setChampParent(this.getChampParent().copy());
		}
		return copy;		
	}
}
