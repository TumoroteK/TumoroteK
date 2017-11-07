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
package fr.aphp.tumorotek.model.stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 * Objet persistant mappant la table Subdivision. Classe créée le
 * 25/07/2015.
 * 
 * @author Marc DESCHAMPS
 * @version 2.0.12
 * 
 */

@Entity
@Table(name = "SUBDIVISION")
@NamedQueries(value = {
	@NamedQuery(name = "Subdivision.findByModele", 
	query = "SELECT distinct s FROM Subdivision s JOIN s.indicateurs i "
		+ "JOIN i.sModeleIndicateurs m "
		+ "WHERE m.pk.sModele = ?1")
})
public class Subdivision implements Serializable {


	private static final long serialVersionUID = 73708959544594320L;
	private Integer subdivisionId;
	private String nom;
	private List<Indicateur> indicateurs = new ArrayList<Indicateur>();
	private ChampEntite champEntite;
	
	public Subdivision() {
		super();
	}
	
	
	@Id
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	@Column(name = "SUBDIVISION_ID", unique = true, nullable = false)
	public Integer getSubdivisionId() {
		return subdivisionId;
	}

	public void setSubdivisionId(Integer _id) {
		this.subdivisionId = _id;
	}

	
	@Column(name = "NOM", nullable = false)
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@OneToMany(mappedBy = "subdivision")
	public List<Indicateur> getIndicateurs() {
		return indicateurs;
	}

	public void setIndicateurs(List<Indicateur> indicateurs) {
		this.indicateurs = indicateurs;
	}
	
	@OneToOne
	@JoinColumn(name = "CHAMP_ENTITE_ID", nullable = true)
	public ChampEntite getChampEntite() {
		return champEntite;
	}

	public void setChampEntite(ChampEntite chpE) {
		this.champEntite = chpE;
	}


	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || (!(obj instanceof Subdivision))) {
			return false;
		}
		Subdivision sub = (Subdivision) obj;
		return (this.nom == sub.nom || (this.nom != null && this.nom
				.equals(sub.nom)));
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = 31 * hashCode + ((nom == null) ? 0 : nom.hashCode());
		return hashCode;
	}
	
	

}
