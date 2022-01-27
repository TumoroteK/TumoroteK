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
package fr.aphp.tumorotek.model.coeur.cession;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Embedded Id pour la table CEDER_OBJET. Classe créée le 22/09/09.
 *
 * @author Mathieu BARTHELEMY
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 *      hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class CederObjetPK implements Serializable {

	private static final long serialVersionUID = 4888881853949416299L;

	private Cession cession;
	private Entite entite;
	private Integer objetId;

	/** Constructeur par défaut. */
	public CederObjetPK() {
	}

	public CederObjetPK(final Cession cess, final Entite ent, final Integer id) {
		this.cession = cess;
		this.entite = ent;
		this.objetId = id;
	}

	@ManyToOne(targetEntity = Cession.class)
	public Cession getCession() {
		return cession;
	}

	public void setCession(final Cession cess) {
		this.cession = cess;
	}

	@ManyToOne(targetEntity = Entite.class)
	public Entite getEntite() {
		return entite;
	}

	public void setEntite(final Entite en) {
		this.entite = en;
	}

	@Column(name = "OBJET_ID", nullable = false)
	public Integer getObjetId() {
		return objetId;
	}

	public void setObjetId(final Integer objId) {
		this.objetId = objId;
	}

	/**
	 * 2 PKs sont considérés comme égales si elles sont composees des mêmes clés.
	 * 
	 * @param obj est la PK à tester.
	 * @return true si les PK sont egales.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		final CederObjetPK test = (CederObjetPK) obj;
		return ((this.cession == test.cession || (this.cession != null && this.cession.equals(test.cession)))
				&& (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite)))
				&& (this.objetId == test.objetId || (this.objetId != null && this.objetId.equals(test.objetId))));
	}

	/**
	 * Le hashcode est calculé sur les clés.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashCession = 0;
		int hashEntite = 0;
		int hashObjet = 0;

		if (this.cession != null) {
			hashCession = this.cession.hashCode();
		}
		if (this.entite != null) {
			hashEntite = this.entite.hashCode();
		}
		if (this.objetId != null) {
			hashObjet = this.objetId.hashCode();
		}

		hash = 7 * hash + hashCession;
		hash = 7 * hash + hashEntite;
		hash = 7 * hash + hashObjet;

		return hash;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.entite != null && this.cession != null && this.objetId != null) {
			return "{" + cession.toString() + " (Cession), " + entite.toString() + " (Entite), " + objetId
					+ " (ObjetId)}";
		}
		return "{Empty CederObjetPK}";
	}

}
