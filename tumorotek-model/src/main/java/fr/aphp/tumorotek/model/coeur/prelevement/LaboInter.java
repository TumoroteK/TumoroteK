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
package fr.aphp.tumorotek.model.coeur.prelevement;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Objet persistant mappant la table LABO_INTER. Classe créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "LABO_INTER")
//@NamedQueries(
//   value = {@NamedQuery(name = "LaboInter.findByExcludedId", query = "SELECT l FROM LaboInter l " + "WHERE l.laboInterId != ?1"),
//      //		@NamedQuery(name = "LaboInter.findByOrdre",
//      //			query = "SELECT l FROM LaboInter l WHERE l.ordre = ?1"),
//      //		@NamedQuery(name = "LaboInter.findByDateArrivee", 
//      //			query = "SELECT l FROM LaboInter l WHERE l.dateArrivee = ?1"),
//      //		@NamedQuery(name = "LaboInter.findByDateDepart", 
//      //				query = "SELECT l FROM LaboInter l " 
//      //					+ "WHERE l.dateDepart = ?1"), 
//      //		@NamedQuery(name = "LaboInter.findByConservTemp", 
//      //				query = "SELECT l FROM LaboInter l WHERE l.conservTemp = ?1"),
//      //		@NamedQuery(name = "LaboInter.findByTransportTemp", 
//      //				query = "SELECT l FROM LaboInter l " 
//      //					+ "WHERE l.transportTemp = ?1"),
//      //		@NamedQuery(name = "LaboInter.findBySterile", 
//      //				query = "SELECT l FROM LaboInter l " 
//      //					+ "WHERE l.sterile = ?1"),
//      @NamedQuery(name = "LaboInter.findByService", query = "SELECT l FROM LaboInter l " + "WHERE l.service = ?1"),
//      @NamedQuery(name = "LaboInter.findByTransporteur", query = "SELECT l FROM LaboInter l " + "WHERE l.transporteur = ?1"),
//      @NamedQuery(name = "LaboInter.findByCollaborateur", query = "SELECT l FROM LaboInter l " + "WHERE l.collaborateur = ?1"),
//      @NamedQuery(name = "LaboInter.findByPrelevementWithOrder",
//         query = "SELECT l FROM LaboInter l " + "WHERE l.prelevement = ?1 " + "ORDER BY l.ordre"),
//      @NamedQuery(name = "LaboInter.findByPrelevementWithOnlyOrder",
//         query = "SELECT l.ordre FROM LaboInter l " + "WHERE l.prelevement = ?1 "),
//      @NamedQuery(name = "LaboInter.findByPrelevementWithOnlyOrderAndExcludedId",
//         query = "SELECT l.ordre FROM LaboInter l " + "WHERE l.prelevement = ?1 " + "AND l.laboInterId != ?2")
//   //		@NamedQuery(name = "LaboInter.findByProprietaire", 
//   //				query = "SELECT l FROM LaboInter l " 
//   //					+ "WHERE l.prelevement = ?1"),
//   //		@NamedQuery(name = "LaboInter.findByIdWithFetch", 
//   //				query = "SELECT l FROM LaboInter l LEFT JOIN FETCH " 
//   //					+ "l.collaborateur LEFT JOIN FETCH l.service " 
//   //					+ "LEFT JOIN FETCH l.transporteur LEFT JOIN FETCH " 
//   //					+ "l.prelevement WHERE l.laboInterId = ?1"),
//   })
public class LaboInter implements Serializable {

	private static final long serialVersionUID = 548676135486746L;

	private Integer laboInterId;
	private Integer ordre;
	private Calendar dateArrivee;
	private Float conservTemp;
	private Boolean sterile;
	private Boolean congelation;
	private Float transportTemp;
	private Calendar dateDepart;

	private Prelevement prelevement;
	private Collaborateur collaborateur;
	private Transporteur transporteur;
	private Service service;

	/** Constructeur par défaut. */
	public LaboInter() {
	}

	@Override
	public String toString() {
		if (this.prelevement != null && this.ordre != null) {
			return "{" + this.prelevement.getCode() + ", " + this.ordre.toString() + "}";
		} else {
			return "{Empty LaboInter}";
		}
	}

	@Id
	@Column(name = "LABO_INTER_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getLaboInterId() {
		return this.laboInterId;
	}

	public void setLaboInterId(final Integer id) {
		this.laboInterId = id;
	}

	@Column(name = "ORDRE", nullable = false)
	public Integer getOrdre() {
		return this.ordre;
	}

	public void setOrdre(final Integer o) {
		this.ordre = o;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_ARRIVEE", nullable = true)
	public Calendar getDateArrivee() {
		if (dateArrivee != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(dateArrivee.getTime());
			return cal;
		} else {
			return null;
		}
	}

	public void setDateArrivee(final Calendar cal) {
		if (cal != null) {
			this.dateArrivee = Calendar.getInstance();
			this.dateArrivee.setTime(cal.getTime());
		} else {
			this.dateArrivee = null;
		}
	}

	@Column(name = "CONSERV_TEMP", nullable = true)
	public Float getConservTemp() {
		return this.conservTemp;
	}

	public void setConservTemp(final Float temp) {
		this.conservTemp = temp;
	}

	@Column(name = "STERILE", nullable = true)
	public Boolean getSterile() {
		return this.sterile;
	}

	public void setSterile(final Boolean ster) {
		this.sterile = ster;
	}

	@Column(name = "CONGELATION", nullable = true)
	public Boolean getCongelation() {
		return congelation;
	}

	public void setCongelation(final Boolean c) {
		this.congelation = c;
	}

	@Column(name = "TRANSPORT_TEMP", nullable = true)
	public Float getTransportTemp() {
		return this.transportTemp;
	}

	public void setTransportTemp(final Float temp) {
		this.transportTemp = temp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_DEPART", nullable = true)
	public Calendar getDateDepart() {
		if (dateDepart != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(dateDepart.getTime());
			return cal;
		} else {
			return null;
		}
	}

	public void setDateDepart(final Calendar cal) {
		if (cal != null) {
			this.dateDepart = Calendar.getInstance();
			this.dateDepart.setTime(cal.getTime());
		} else {
			this.dateDepart = null;
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRELEVEMENT_ID", nullable = false)
	public Prelevement getPrelevement() {
		return this.prelevement;
	}

	public void setPrelevement(final Prelevement prelev) {
		this.prelevement = prelev;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
	public Collaborateur getCollaborateur() {
		return this.collaborateur;
	}

	public void setCollaborateur(final Collaborateur collab) {
		this.collaborateur = collab;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinColumn(name = "TRANSPORTEUR_ID", nullable = true)
	public Transporteur getTransporteur() {
		return this.transporteur;
	}

	public void setTransporteur(final Transporteur transport) {
		this.transporteur = transport;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinColumn(name = "SERVICE_ID", nullable = true)
	public Service getService() {
		return this.service;
	}

	public void setService(final Service serv) {
		this.service = serv;
	}

	/**
	 * 2 labos intermedaires sont considérés comme égaux s'ils ont le même ordre et
	 * si ils associes au même prélèvement.
	 * 
	 * @param obj est le labo à tester.
	 * @return true si les labos sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof LaboInter)) {
			return false;
		}
		final LaboInter test = (LaboInter) obj;
		return Objects.equals(prelevement, test.getPrelevement()) && Objects.equals(ordre, test.getOrdre());
	}

	/**
	 * Le hashcode est calculé sur les attributs ordre et prelevement.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashOrdre = 0;
		int hashPrelevement = 0;
		if (this.ordre != null) {
			hashOrdre = this.ordre.hashCode();
		}
		if (this.prelevement != null) {
			hashPrelevement = this.prelevement.hashCode();
		}
		hash = 31 * hash + hashOrdre;
		hash = 31 * hash + hashPrelevement;

		return hash;

	}
}
