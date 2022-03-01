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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * @author Gille Chapelot
 * @version 2.3
 *
 */
@Entity
@Table(name = "CHAMP_DELEGUE")
@GenericGenerator(name="seqGenerator", strategy="increment")
@AttributeOverrides({@AttributeOverride(name="id", column=@Column(name="CHAMP_DELEGUE_ID"))})
//@NamedQueries({@NamedQuery(name = "ChampDelegue.findByEntiteAndContexte",
//query = "FROM ChampDelegue cd WHERE cd.entite=?1 AND cd.contexte=?2"),
//	@NamedQuery(name = "ChampDelegue.findByNomAndEntiteAndContexte",
//	query = "FROM ChampDelegue cd WHERE cd.nom=?1 AND cd.entite=?2 AND cd.contexte=?3")})
public class ChampDelegue extends AbstractTKChamp
{

	private Entite entite;
	private EContexte contexte;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITE_ID", nullable = false)
	public Entite getEntite(){
		return entite;
	}

	public void setEntite(Entite entite){
		this.entite = entite;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CONTEXTE", nullable = false)
	public EContexte getContexte(){
		return contexte;
	}

	public void setContexte(EContexte contexte){
		this.contexte = contexte;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contexte == null) ? 0 : contexte.hashCode());
		result = prime * result + ((entite == null) ? 0 : entite.hashCode());
		result = prime * result + ((this.getNom() == null) ? 0 : this.getNom().hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		ChampDelegue other = (ChampDelegue) obj;
		if(contexte != other.contexte)
			return false;
		if(entite == null){
			if(other.entite != null)
				return false;
		}else if(!entite.equals(other.entite))
			return false;
		if(this.getNom() == null){
			if(other.getNom() != null)
				return false;
		}else if(!this.getNom().equals(other.getNom()))
			return false;
		return true;
	}

	@Transient
	public String getILNLabelForChampDelegue(final EContexte ctx){

		if (ctx != null && getEntite() != null && getNom() != null) {		  
			final StringBuffer iProperty = new StringBuffer()
					.append("Champ.")
					.append(getEntite().getNom())
					.append(".")
					.append(ctx)
					.append(".")
					.append(getNom().replaceAll("Id$", ""));

			// on ajoute la valeur du champ
			return iProperty.toString();
		}
		return null;
	}
}
