/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.model.contexte.gatsbi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Objet persistant mappant la table GASTBY_ETUDE.
 * Classe créée le 12/07/2021.
 *
 * 
 * @version 2.3.0-gatsbi
 *
 */
@Entity
@Immutable
@Table(name = "GATSBY_ETUDE")
@NamedQueries(value = {
	@NamedQuery(name = "Etude.findByPfOrder", 
		query = "SELECT e FROM Etude e " + "WHERE e.plateforme = ?1 ORDER BY e.titre"),
})
public class Etude implements Serializable {

	private static final long serialVersionUID = 86784231547511654L;

	private Integer etudeId;
	

	private Plateforme plateforme;
	private String titre;
	private String acronyme;
	private List<Contexte> contextes = new ArrayList<Contexte>();

	public Etude(){}
	
	@Id
	@Column(name = "GATSBY_ETUDE_ID", unique = true, nullable = false)
	public Integer getEtudeId() {
		return etudeId;
	}
	
	public void setEtudeId(Integer etudeId) {
		this.etudeId = etudeId;
	}

	@ManyToOne
	@JoinColumn(name = "PLATEFORME_ID")
	public Plateforme getPlateforme() {
		return plateforme;
	}
	
	public void setPlateforme(Plateforme plateforme) {
		this.plateforme = plateforme;
	}

	@Column(name = "TITRE")
	public String getTitre() {
		return titre;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Column(name = "ACRONYME")
	public String getAcronyme() {
		return acronyme;
	}
	
	public void setAcronyme(String acronyme) {
		this.acronyme = acronyme;
	}

	@Transient
	public List<Contexte> getContextes() {
		return contextes;
	}

	@Transient
	public void addToContextes(Contexte c) {
		if (!this.contextes.contains(c)) {
			contextes.add(c);
		}
	}

	/**
	 * 2 etudes sont considérées comme égales si elles ont le même titre.
	 * @param obj est l'étude à tester.
	 * @return true si les études sont égales.
	 */
	@Override
	public boolean equals(final Object obj){

		if(this == obj){
			return true;
		}
		if((obj == null) || obj.getClass() != this.getClass()){
			return false;
		}
		final Etude test = (Etude) obj;
		return Objects.equals(getTitre(), test.getTitre());
	}

	/**
	 * Le hashcode est calculé sur l'attribut titre.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode(){

		int hash = 7;
		int hashTitre = 0;

		if(this.getTitre() != null){
			hashTitre = this.getTitre().hashCode();
		}

		hash = 31 * hash + hashTitre;

		return hash;

	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString(){
		return "{" + this.getTitre() + "}";
	}
	
	@Transient
	public Contexte getContexteForEntite(Integer entiteId) {
		for (Contexte c : contextes) {
			if (c.getContexteType().getEntiteId().equals(entiteId)) {
				return c;
			}
		}
		return null;
	}
}