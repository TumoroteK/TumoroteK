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
package fr.aphp.tumorotek.model.stockage;

import java.io.Serializable;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.contexte.Plateforme;


/**
 * 
 * Objet persistant mappant la table d'association améliorée CONTENEUR_PLATEFORME.
 * Classe créée le  02/12/2013.
 * 
 * clef composite
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/*%20
 * 		hibernate-annotations-the-many-to-many-association-with-composite-key/
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 * 
 */
@Entity
@Table(name = "CONTENEUR_PLATEFORME")
@AssociationOverrides({
	@AssociationOverride(name = "pk.conteneur", 
				joinColumns = @JoinColumn(name = "CONTENEUR_ID", 
							referencedColumnName = "CONTENEUR_ID")),
	@AssociationOverride(name = "pk.plateforme", 
			joinColumns = @JoinColumn(name = "PLATEFORME_ID",
							referencedColumnName = "PLATEFORME_ID"))
    })
public class ConteneurPlateforme implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Boolean partage = false;
	private ConteneurPlateformePK pk = new ConteneurPlateformePK();

	/** Constructeur par défaut. */
	public ConteneurPlateforme() {
	}
	
	public ConteneurPlateforme(Conteneur c, Plateforme p) {
		pk.setConteneur(c);
		pk.setPlateforme(p);
	}

	@Override
	public String toString() {
		if (this.getConteneur() != null && this.getPlateforme() != null) {
			return "{" + this.getConteneur() + " - " 
									+ this.getPlateforme() + "}";
		}
		return "{Empty ConteneurPlateforme}";
	}
	
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "conteneur", 
				column = @Column(name = "CONTENEUR_ID")),
		@AttributeOverride(name = "plateforme", 
				column = @Column(name = "PLATEFORME_ID")) })
	public ConteneurPlateformePK getPk() {
		return pk;
	}
	 
	public void setPk(ConteneurPlateformePK pmk) {
		this.pk = pmk;
	}

	@Column(name = "PARTAGE", nullable = false)
	public Boolean getPartage() {
		return this.partage;
	}

	public void setPartage(Boolean b) {
		this.partage = b;
	}

	@Transient
	public Conteneur getConteneur() {
		return this.pk.getConteneur();
	}

	public void setConteneur(Conteneur c) {
		this.pk.setConteneur(c);
	}

	@Transient
	public Plateforme getPlateforme() {
		return this.pk.getPlateforme();
	}

	public void setPlateforme(Plateforme p) {
		this.pk.setPlateforme(p);
	}

	/**
	 * 2 liens sont considérés comme égaux s'ils ont la même pk.
	 * @param obj est le lien à tester.
	 * @return true si les liens sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		ConteneurPlateforme test = (ConteneurPlateforme) obj;
		return (this.pk != null 
				&& (this.pk == test.pk || this.pk.equals(test.pk)));
	}

	/**
	 * Le hashcode est calculé sur la pk.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashPk = 0;
		
		if (this.pk != null) {
			hashPk = this.pk.hashCode();
		}
		
		hash = 7 * hash + hashPk;
		
		return hash;		
	}
}
