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
package fr.aphp.tumorotek.model.contexte;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * Objet persistant mappant la table TRANSPORTEUR. Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "TRANSPORTEUR")
//@NamedQueries(value = {@NamedQuery(name = "Transporteur.findByNom", query = "SELECT t FROM Transporteur t WHERE t.nom = ?1"),
//   @NamedQuery(name = "Transporteur.findByContactNom", query = "SELECT t FROM Transporteur t WHERE t.contactNom = ?1"),
//   @NamedQuery(name = "Transporteur.findByArchive",
//      query = "SELECT t FROM Transporteur t WHERE t.archive = ?1" + " ORDER BY t.nom"),
//   @NamedQuery(name = "Transporteur.findByCoordonnee", query = "SELECT t FROM Transporteur t " + "WHERE t.coordonnee = ?1"),
//   @NamedQuery(name = "Transporteur.findByIdWithFetch",
//      query = "SELECT t FROM Transporteur t LEFT JOIN FETCH " + "t.coordonnee WHERE t.transporteurId = ?1"),
//   @NamedQuery(name = "Transporteur.findByExcludedId", query = "SELECT t FROM Transporteur t " + "WHERE t.transporteurId != ?1"),
//   @NamedQuery(name = "Transporteur.findByOrder", query = "SELECT t FROM Transporteur t ORDER BY t.nom")})
public class Transporteur implements TKFantomableObject, TKdataObject, Serializable {

	private static final long serialVersionUID = 3254564866454L;

	private Integer transporteurId;
	private String nom;
	private String contactNom;
	private String contactPrenom;
	private String contactTel;
	private String contactFax;
	private String contactMail;
	private Boolean archive = false;

	private Coordonnee coordonnee;

	private Set<Prelevement> prelevements = new HashSet<>();
	private Set<Cession> cessions = new HashSet<>();
	private Set<LaboInter> laboInters = new HashSet<>();

	/** Constructeur. */
	public Transporteur() {
	}

	// /**
	// * Constructeur avec paramètres.
	// * @param id .
	// * @param n .
	// * @param cNom .
	// * @param cPrenom .
	// * @param cTel .
	// * @param cFax .
	// * @param cMail .
	// * @param arch .
	// */
	// public Transporteur(Integer id, String n, String cNom,
	// String cPrenom, String cTel, String cFax,
	// String cMail, boolean arch) {
	// this.transporteurId = id;
	// this.nom = n;
	// this.contactNom = cNom;
	// this.contactPrenom = cPrenom;
	// this.contactTel = cTel;
	// this.contactFax = cFax;
	// this.contactMail = cMail;
	// this.archive = arch;
	// }

	@Id
	@Column(name = "TRANSPORTEUR_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getTransporteurId() {
		return transporteurId;
	}

	public void setTransporteurId(final Integer tId) {
		this.transporteurId = tId;
	}

	@Column(name = "NOM", nullable = false, length = 50)
	public String getNom() {
		return nom;
	}

	public void setNom(final String n) {
		this.nom = n;
	}

	@Column(name = "CONTACT_NOM", nullable = false, length = 50)
	public String getContactNom() {
		return contactNom;
	}

	public void setContactNom(final String cNom) {
		this.contactNom = cNom;
	}

	@Column(name = "CONTACT_PRENOM", nullable = true, length = 50)
	public String getContactPrenom() {
		return contactPrenom;
	}

	public void setContactPrenom(final String cPrenom) {
		this.contactPrenom = cPrenom;
	}

	@Column(name = "CONTACT_TEL", nullable = true, length = 15)
	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(final String cTel) {
		this.contactTel = cTel;
	}

	@Column(name = "CONTACT_FAX", nullable = true, length = 15)
	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(final String cFax) {
		this.contactFax = cFax;
	}

	@Column(name = "CONTACT_MAIL", nullable = true, length = 100)
	public String getContactMail() {
		return contactMail;
	}

	public void setContactMail(final String cMail) {
		this.contactMail = cMail;
	}

	@Column(name = "ARCHIVE", nullable = false)
	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(final Boolean arch) {
		this.archive = arch;
	}

	// @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,
	// CascadeType.MERGE })
	// @JoinColumn(name = "COORDONNEE_ID", nullable = true)
	@OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "COORDONNEE_ID", nullable = true)
	public Coordonnee getCoordonnee() {
		return coordonnee;
	}

	public void setCoordonnee(final Coordonnee c) {
		this.coordonnee = c;
	}

	@OneToMany(mappedBy = "transporteur")
	public Set<Prelevement> getPrelevements() {
		return prelevements;
	}

	public void setPrelevements(final Set<Prelevement> prelevs) {
		this.prelevements = prelevs;
	}

	@OneToMany(mappedBy = "transporteur")
	public Set<Cession> getCessions() {
		return cessions;
	}

	public void setCessions(final Set<Cession> cess) {
		this.cessions = cess;
	}

	@OneToMany(mappedBy = "transporteur")
	public Set<LaboInter> getLaboInters() {
		return laboInters;
	}

	public void setLaboInters(final Set<LaboInter> labos) {
		this.laboInters = labos;
	}

	/**
	 * 2 transporteurs sont considérés comme égaux s'ils ont les même attributs.
	 * 
	 * @param obj est le transporteur à tester.
	 * @return true si les transporteurs sont égaux.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof Transporteur)) {
			return false;
		}
		final Transporteur test = (Transporteur) obj;
		return Objects.equals(nom, test.getNom()) && Objects.equals(contactNom, test.getContactNom())
				&& Objects.equals(contactPrenom, test.getContactPrenom())
				&& Objects.equals(contactTel, test.getContactTel()) && Objects.equals(contactFax, test.getContactFax())
				&& Objects.equals(contactMail, test.getContactMail());
	}

	/**
	 * Le hashcode est calculé sur tous les attributs.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashNom = 0;
		int hashCNom = 0;
		int hashCPrenom = 0;
		int hashCTel = 0;
		int hashCFax = 0;
		int hashCMail = 0;

		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.contactNom != null) {
			hashCNom = this.contactNom.hashCode();
		}
		if (this.contactPrenom != null) {
			hashCPrenom = this.contactPrenom.hashCode();
		}
		if (this.contactTel != null) {
			hashCTel = this.contactTel.hashCode();
		}
		if (this.contactFax != null) {
			hashCFax = this.contactFax.hashCode();
		}
		if (this.contactMail != null) {
			hashCMail = this.contactMail.hashCode();
		}

		hash = 7 * hash + hashNom;
		hash = 7 * hash + hashCNom;
		hash = 7 * hash + hashCPrenom;
		hash = 7 * hash + hashCTel;
		hash = 7 * hash + hashCFax;
		hash = 7 * hash + hashCMail;

		return hash;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		return "{" + this.nom + ", " + this.contactNom + ", " + this.contactPrenom + ", " + this.contactTel + ", "
				+ this.contactFax + ", " + this.contactMail + "}";
	}

	/**
	 * Cree un clone de l'objet.
	 * 
	 * @return clone Collaborateur.
	 */
	@Override
	public Transporteur clone() {
		final Transporteur clone = new Transporteur();

		clone.setTransporteurId(this.transporteurId);
		clone.setNom(this.nom);
		clone.setContactNom(this.contactNom);
		clone.setContactPrenom(this.contactPrenom);
		clone.setContactTel(this.contactTel);
		clone.setContactFax(this.contactFax);
		clone.setContactMail(this.contactMail);
		clone.setArchive(this.archive);
		clone.setCoordonnee(this.coordonnee);

		return clone;
	}

	@Override
	public Integer listableObjectId() {
		return getTransporteurId();
	}

	@Override
	@Transient
	public String getPhantomData() {
		return getNom();
	}

	@Override
	public String entiteNom() {
		return "Transporteur";
	}

}
