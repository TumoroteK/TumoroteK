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
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Embedded Id pour la table TABLE_ANNOTATION_BANQUE. Classe créée le 29/01/10.
 *
 * @author Mathieu BARTHELEMY
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 *      hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class TableAnnotationBanquePK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Banque banque;
	private TableAnnotation tableAnnotation;

	/** Constructeur par défaut. */
	public TableAnnotationBanquePK() {
	}

	public TableAnnotationBanquePK(final Banque b, final TableAnnotation t) {
		this.banque = b;
		this.tableAnnotation = t;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Banque.class)
	public Banque getBanque() {
		return this.banque;
	}

	public void setBanque(final Banque b) {
		this.banque = b;
	}

	@ManyToOne(fetch = FetchType.EAGER,targetEntity = TableAnnotation.class)
	public TableAnnotation getTableAnnotation() {
		return this.tableAnnotation;
	}

	public void setTableAnnotation(final TableAnnotation t) {
		this.tableAnnotation = t;
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
		if ((obj == null) || !(obj instanceof TableAnnotationBanquePK)) {
			return false;
		}		
		final TableAnnotationBanquePK test = (TableAnnotationBanquePK) obj;
		return Objects.equals(banque, test.getBanque())
			&& Objects.equals(tableAnnotation, test.getTableAnnotation());
	}

	/**
	 * Le hashcode est calculé sur les clés.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		// int hash = 7;
		int hashBanque = 0;
		int hashTableAnnotation = 0;

		if (this.banque != null) {
			hashBanque = this.banque.hashCode();
		}
		if (this.tableAnnotation != null) {
			hashTableAnnotation = this.tableAnnotation.hashCode();
		}

		return hashBanque + hashTableAnnotation;
	}

}
