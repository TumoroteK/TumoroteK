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
package fr.aphp.tumorotek.model.cession;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table RETOUR. Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "RETOUR")
//@NamedQueries(
//   value = {
//      @NamedQuery(name = "Retour.findByObjetId",
//         query = "SELECT r FROM Retour r WHERE r.objetId = ?1 " + "ORDER BY r.dateSortie"),
//      @NamedQuery(name = "Retour.findByObject",
//         query = "SELECT r FROM Retour r WHERE r.objetId = ?1" + " AND r.entite = ?2 " + "ORDER BY r.dateSortie"),
//      @NamedQuery(name = "Retour.findByObjectAndImpact",
//         query = "SELECT r FROM Retour r WHERE r.objetId = ?1" + " AND r.entite = ?2 AND r.impact = ?3 "
//            + "ORDER BY r.dateSortie"),
//      @NamedQuery(name = "Retour.findByObjectsDateRetourEmpty",
//         query = "SELECT r FROM Retour r WHERE " + " r.entite = ?2 AND r.dateRetour is null" + " AND r.objetId in (?1)"),
//      @NamedQuery(name = "Retour.findByExcludedId",
//         query = "SELECT r FROM Retour r WHERE r.retourId != ?1 " + " AND r.objetId = ?2" + " AND r.entite = ?3"),
//      @NamedQuery(name = "Retour.findByMaxId", query = "SELECT max(r.retourId) FROM Retour r"),
//      @NamedQuery(name = "Retour.findByObjDates",
//         query = "SELECT r FROM Retour r where r.dateSortie <= ?1 "
//            + "AND r.dateRetour >= ?1 AND r.objetId = ?2 and r.entite = ?3 " + "AND r.retourId <> ?4"),
//      @NamedQuery(name = "Retour.findObjIdsByDatesAndEntite",
//         query = "SELECT distinct r.objetId FROM Retour r where r.dateSortie <= ?1 "
//            + "AND r.dateRetour >= ?1 AND r.entite = ?2"),
//      @NamedQuery(name = "Retour.findByObjInsideDates",
//         query = "SELECT r FROM Retour r where r.dateSortie >= ?1 "
//            + "AND r.dateRetour <= ?2 AND r.objetId = ?3 and r.entite = ?4 " + "AND r.retourId <> ?5"),
//      @NamedQuery(name = "Retour.findObjIdsInsideDatesEntite",
//         query = "SELECT distinct r.objetId FROM Retour r where r.dateSortie >= ?1 "
//            + "AND r.dateRetour <= ?2 AND r.entite = ?3"),
//      @NamedQuery(name = "Retour.findByCollaborateur", query = "SELECT r FROM Retour r where r.collaborateur = ?1 ")})
public class Retour implements TKdataObject, Serializable {

	private Integer retourId;
	private Integer objetId;
	private Calendar dateSortie;
	private Calendar dateRetour;
	private Float tempMoyenne;
	private Boolean sterile;
	private Boolean impact;
	private String observations;
	private String oldEmplacementAdrl;

	private Cession cession;
	private Entite entite;
	private Transformation transformation;
	private Collaborateur collaborateur;
	private Conteneur conteneur;
	private Incident incident;
	private ObjetStatut objetStatut;

	private TKStockableObject tkObject;

	private static final long serialVersionUID = 1L;

	/** Constructeur par défaut. */
	public Retour() {
	}

	@Override
	public String toString() {
		if (entite != null && dateSortie != null) {
			return "{" + entite.getNom() + ":" + objetId + " "
					+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateSortie.getTime()) + "}";
		}
		return "{Empty Retour}";
	}

	@Id
	@Column(name = "RETOUR_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "native", parameters = {
			@Parameter(name = "sequence", value = "retourSeq") })
	public Integer getRetourId() {
		return this.retourId;
	}

	public void setRetourId(final Integer id) {
		this.retourId = id;
	}

	@Column(name = "OBJET_ID", nullable = false)
	public Integer getObjetId() {
		return this.objetId;
	}

	public void setObjetId(final Integer id) {
		this.objetId = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_SORTIE", nullable = false)
	public Calendar getDateSortie() {
		if (dateSortie != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(dateSortie.getTime());
			return cal;
		}
		return null;
	}

	public void setDateSortie(final Calendar cal) {
		if (cal != null) {
			this.dateSortie = Calendar.getInstance();
			this.dateSortie.setTime(cal.getTime());
		} else {
			this.dateSortie = null;
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_RETOUR", nullable = true)
	public Calendar getDateRetour() {
		if (dateRetour != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(dateRetour.getTime());
			return cal;
		}
		return null;
	}

	public void setDateRetour(final Calendar cal) {
		if (cal != null) {
			this.dateRetour = Calendar.getInstance();
			this.dateRetour.setTime(cal.getTime());
		} else {
			this.dateRetour = null;
		}
	}

	@Column(name = "TEMP_MOYENNE", nullable = false)
	public Float getTempMoyenne() {
		return this.tempMoyenne;
	}

	public void setTempMoyenne(final Float temp) {
		this.tempMoyenne = temp;
	}

	@Column(name = "STERILE", nullable = true)
	public Boolean getSterile() {
		return this.sterile;
	}

	public void setSterile(final Boolean s) {
		this.sterile = s;
	}

	@Column(name = "OBSERVATIONS", nullable = true)
	// @Lob
	public String getObservations() {
		return this.observations;
	}

	@Column(name = "IMPACT", nullable = true)
	public Boolean getImpact() {
		return impact;
	}

	public void setImpact(final Boolean impact) {
		this.impact = impact;
	}

	public void setObservations(final String obs) {
		this.observations = obs;
	}

	@Column(name = "OLD_EMPLACEMENT_ADRL", nullable = true, length = 100)
	public String getOldEmplacementAdrl() {
		return oldEmplacementAdrl;
	}

	public void setOldEmplacementAdrl(final String oAdrl) {
		this.oldEmplacementAdrl = oAdrl;
	}

	@ManyToOne
	@JoinColumn(name = "CESSION_ID", nullable = true)
	public Cession getCession() {
		return this.cession;
	}

	public void setCession(final Cession cess) {
		this.cession = cess;
	}

	@ManyToOne
	@JoinColumn(name = "ENTITE_ID", nullable = true)
	public Entite getEntite() {
		return this.entite;
	}

	public void setEntite(final Entite e) {
		this.entite = e;
	}

	@ManyToOne
	@JoinColumn(name = "TRANSFORMATION_ID", nullable = true)
	public Transformation getTransformation() {
		return this.transformation;
	}

	public void setTransformation(final Transformation trans) {
		this.transformation = trans;
	}

	@ManyToOne
	@JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
	public Collaborateur getCollaborateur() {
		return this.collaborateur;
	}

	public void setCollaborateur(final Collaborateur coll) {
		this.collaborateur = coll;
	}

	@ManyToOne
	@JoinColumn(name = "CONTENEUR_ID", nullable = true)
	public Conteneur getConteneur() {
		return this.conteneur;
	}

	public void setConteneur(final Conteneur cu) {
		this.conteneur = cu;
	}

	@ManyToOne
	@JoinColumn(name = "INCIDENT_ID", nullable = true)
	public Incident getIncident() {
		return this.incident;
	}

	public void setIncident(final Incident inc) {
		this.incident = inc;
	}

	@ManyToOne
	@JoinColumn(name = "OBJET_STATUT_ID", nullable = true)
	public ObjetStatut getObjetStatut() {
		return objetStatut;
	}

	public void setObjetStatut(final ObjetStatut o) {
		this.objetStatut = o;
	}

	@Transient
	public TKStockableObject getTkObject() {
		return tkObject;
	}

	public void setTkObject(final TKStockableObject t) {
		this.tkObject = t;
	}

	/**
	 * 2 objets sont considérés comme égaux s'ils ont les même dates et le même
	 * couple objet-entite id.
	 * 
	 * @param obj est la coordonnée à tester.
	 * @return true si les coordonnées sont égales.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		final Retour test = (Retour) obj;

		return ((this.dateRetour == test.dateRetour
				|| (this.dateRetour != null && this.dateRetour.equals(test.dateRetour)))
				&& (this.dateSortie == test.dateSortie
						|| (this.dateSortie != null && this.dateSortie.equals(test.dateSortie)))
				&& (this.objetId == test.objetId || (this.objetId != null && this.objetId.equals(test.objetId)))
				&& (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite))));

	}

	/**
	 * Le hashcode est calculé sur tous les dates et objetid.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashRetour = 0;
		int hashSortie = 0;
		int hashObjet = 0;
		final int hashEntite = 0;

		if (this.dateRetour != null) {
			hashRetour = this.dateRetour.hashCode();
		}
		if (this.dateSortie != null) {
			hashSortie = this.dateSortie.hashCode();
		}
		if (this.objetId != null) {
			hashObjet = this.objetId.hashCode();
		}
		if (this.entite != null) {
			hashObjet = this.entite.hashCode();
		}

		hash = 7 * hash + hashRetour;
		hash = 7 * hash + hashSortie;
		hash = 7 * hash + hashObjet;
		hash = 7 * hash + hashEntite;

		return hash;
	}

	@Override
	public Retour clone() {
		final Retour clone = new Retour();
		clone.setRetourId(getRetourId());
		clone.setEntite(getEntite());
		clone.setObjetId(getObjetId());
		clone.setDateSortie(getDateSortie());
		clone.setDateRetour(getDateRetour());
		clone.setTempMoyenne(getTempMoyenne());
		clone.setSterile(getSterile());
		clone.setObservations(getObservations());
		clone.setOldEmplacementAdrl(getOldEmplacementAdrl());
		clone.setCollaborateur(getCollaborateur());
		clone.setCession(getCession());
		clone.setTransformation(getTransformation());
		clone.setConteneur(getConteneur());
		clone.setIncident(getIncident());
		clone.setObjetStatut(getObjetStatut());
		clone.setTkObject(getTkObject());
		clone.setImpact(getImpact());
		return clone;
	}

	@Override
	public Integer listableObjectId() {
		return getRetourId();
	}

}
