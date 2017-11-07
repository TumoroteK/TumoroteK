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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Objet persistant mappant la table COORDONNEE.
 * Classe créée le 09/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 * 
 */
@Entity
@Table(name = "COORDONNEE")
@NamedQueries(value = {@NamedQuery(name = "Coordonnee.findByAdresse", 
			query = "SELECT c FROM Coordonnee c WHERE c.adresse = ?1"), 
		@NamedQuery(name = "Coordonnee.findByCp", 
			query = "SELECT c FROM Coordonnee c WHERE c.cp = ?1"),
		@NamedQuery(name = "Coordonnee.findByVille", 
			query = "SELECT c FROM Coordonnee c WHERE c.ville = ?1"),
		@NamedQuery(name = "Coordonnee.findByPays", 
			query = "SELECT c FROM Coordonnee c WHERE c.pays = ?1"),
//		@NamedQuery(name = "Coordonnee.findByEtablissement", 
//			query = "SELECT c FROM Coordonnee c WHERE c.etablissement = ?1"),
//		@NamedQuery(name = "Coordonnee.findByService", 
//			query = "SELECT c FROM Coordonnee c WHERE " 
//				+ "c.service = ?1"),
//		@NamedQuery(name = "Coordonnee.findByTransporteur", 
//			query = "SELECT c FROM Coordonnee c WHERE " 
//				+ "c.transporteur = ?1"),
		@NamedQuery(name = "Coordonnee.findByCollaborateurId", 
			query = "SELECT c FROM Coordonnee c " 
				+ "left join c.collaborateurs o "
				+ "WHERE o.collaborateurId = ?1"),
		@NamedQuery(name = "Coordonnee.findByCollaborateurIdAndExcludedId", 
			query = "SELECT c FROM Coordonnee c "
				+ "left join c.collaborateurs o "
				+ "WHERE o.collaborateurId = ?1 " 
				+ "AND c.coordonneeId != ?2"),
		@NamedQuery(name = "Coordonnee.findByExcludedId", 
			query = "SELECT c FROM Coordonnee c " 
				+ "WHERE c.coordonneeId != ?1")
				})
public class Coordonnee implements java.io.Serializable {
	
	private static final long serialVersionUID = 54687944563647L;
	
	private Integer coordonneeId;
	private String adresse;
	private String cp;
	private String ville;
	private String pays;
	private String tel;
	private String fax;
	private String mail;
	
	//private Set<Etablissement> etablissements = new HashSet<Etablissement>();
	private Etablissement etablissement;
	//private Set<Service> services = new HashSet<Service>();
	private Service service; 
	//private Set<Transporteur> transporteurs = new HashSet<Transporteur>();
	private Transporteur transporteur;
	
	private Set<Collaborateur> collaborateurs = new HashSet<Collaborateur>();
	
	/**
	 * Constructeur par défaut.
	 */
	public Coordonnee() {
	}
	
//	/**
//	 * Constructeur avec paramètres.
//	 * @param id est l'identifiant de l'objet dans la base de données.
//	 * @param adr .
//	 * @param newCp .
//	 * @param newVille .
//	 * @param newPays .
//	 * @param newTel .
//	 * @param newFax .
//	 * @param newMail .
//	 */
//	public Coordonnee(Integer id, String adr, String newCp, String newVille, 
//			String newPays, String newTel, String newFax, String newMail) {
//		this.coordonneeId = id;
//		this.adresse = adr;
//		this.cp = newCp;
//		this.ville = newVille;
//		this.pays = newPays;
//		this.tel = newTel;
//		this.fax = newFax;
//		this.mail = newMail;
//	}
	

	@Id
	@Column(name = "COORDONNEE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getCoordonneeId() {
		return coordonneeId;
	}

	public void setCoordonneeId(Integer cId) {
		this.coordonneeId = cId;
	}

	@Column(name = "ADRESSE", nullable = true, length = 250)
	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adr) {
		this.adresse = adr;
	}

	@Column(name = "CP", nullable = true, length = 10)
	public String getCp() {
		return cp;
	}

	public void setCp(String newCp) {
		this.cp = newCp;
	}

	@Column(name = "VILLE", nullable = true, length = 100)
	public String getVille() {
		return ville;
	}

	public void setVille(String newVille) {
		this.ville = newVille;
	}

	@Column(name = "PAYS", nullable = true, length = 100)
	public String getPays() {
		return pays;
	}

	public void setPays(String newPays) {
		this.pays = newPays;
	}

	@Column(name = "TEL", nullable = true, length = 15)
	public String getTel() {
		return tel;
	}

	public void setTel(String newTel) {
		this.tel = newTel;
	}

	@Column(name = "FAX", nullable = true, length = 15)
	public String getFax() {
		return fax;
	}

	public void setFax(String newFax) {
		this.fax = newFax;
	}

	@Column(name = "MAIL", nullable = true, length = 100)
	public String getMail() {
		return mail;
	}

	public void setMail(String newMail) {
		this.mail = newMail;
	}
	
//	@OneToMany(mappedBy = "coordonnee")
//	public Set<Etablissement> getEtablissements() {
//		return etablissements;
//	}
//
//	public void setEtablissements(Set<Etablissement> etabs) {
//		this.etablissements = etabs;
//	}
    @OneToOne(mappedBy = "coordonnee", targetEntity = Etablissement.class)
	public Etablissement getEtablissement() {
		return etablissement;
	}

	public void setEtablissement(Etablissement etab) {
		this.etablissement = etab;
	}

//	@OneToMany(mappedBy = "coordonnee")
//	public Set<Service> getServices() {
//		return services;
//	}
//
//	public void setServices(Set<Service> serv) {
//		this.services = serv;
//	}

	@OneToOne(mappedBy = "coordonnee", targetEntity = Service.class)
	public Service getService() {
		return service;
	}

	public void setService(Service serv) {
		this.service = serv;
	}
	
//	@OneToMany(mappedBy = "coordonnee")
//	public Set<Transporteur> getTransporteurs() {
//		return transporteurs;
//	}
//
//	public void setTransporteurs(Set <Transporteur> transp) {
//		this.transporteurs = transp;
//	}
	
	@OneToOne(mappedBy = "coordonnee", targetEntity = Transporteur.class)
	public Transporteur getTransporteur() {
		return transporteur;
	}

	public void setTransporteur(Transporteur transp) {
		this.transporteur = transp;
	}

	@ManyToMany(
			targetEntity = Collaborateur.class
	)
    @JoinTable(
    		name = "COLLABORATEUR_COORDONNEE",
            joinColumns = @JoinColumn(name = "COORDONNEE_ID"),
            inverseJoinColumns = @JoinColumn(name = "COLLABORATEUR_ID")
    )
	public Set<Collaborateur> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(Set<Collaborateur> newCollaborateurs) {
		this.collaborateurs = newCollaborateurs;
	}
	
	/**
	 * 2 coordonnées sont considérées comme égales si elles ont la même adresse,
	 * le même cp, la même ville, le même pays, le même tel, le même fax et le
	 * même mail.
	 * @param obj est la coordonnée à tester.
	 * @return true si les coordonnées sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		Coordonnee test = (Coordonnee) obj;
		// 2 coordonnees sont egales si toutes leurs valeurs le sont
		return ((this.adresse == test.adresse || (this.adresse != null 
						&& this.adresse.equals(test.adresse))) 
				&& (this.cp == test.cp || (this.cp != null 
						&& this.cp.equals(test.cp))) 
				&& (this.ville == test.ville || (this.ville != null 
						&& this.ville.equals(test.ville))) 
				&& (this.pays == test.pays || (this.pays != null 
						&& this.pays.equals(test.pays))) 
				&& (this.tel == test.tel || (this.tel != null 
						&& this.tel.equals(test.tel))) 
				&& (this.fax == test.fax || (this.fax != null 
						&& this.fax.equals(test.fax))) 
				&& (this.mail == test.mail || (this.mail != null 
						&& this.mail.equals(test.mail))));
	}

	/**
	 * Le hashcode est calculé sur tous les attributs.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashAdresse = 0;
		int hashCp = 0;
		int hashVille = 0;
		int hashPays = 0;
		int hashTel = 0;
		int hashFax = 0;
		int hashMail = 0;
		
		if (this.adresse != null) {
			hashAdresse = this.adresse.hashCode();
		}
		if (this.cp != null) {
			hashCp = this.cp.hashCode();
		}
		if (this.ville != null) {
			hashVille = this.ville.hashCode();
		}
		if (this.pays != null) {
			hashPays = this.pays.hashCode();
		}
		if (this.tel != null) {
			hashTel = this.tel.hashCode();
		}
		if (this.fax != null) {
			hashFax = this.fax.hashCode();
		}
		if (this.mail != null) {
			hashMail = this.mail.hashCode();
		}
		
		hash = 7 * hash + hashAdresse;
		hash = 7 * hash + hashCp;
		hash = 7 * hash + hashVille;
		hash = 7 * hash + hashPays;
		hash = 7 * hash + hashTel;
		hash = 7 * hash + hashFax;
		hash = 7 * hash + hashMail;
		
		return hash;
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		return "{" + this.adresse + ", " + this.cp + ", " + this.ville 
		+ ", " + this.pays + ", " + this.tel + ", " + this.fax 
		+ ", " + this.mail + "}";	
	}
	
	/**
	 * Cree un clone de l'objet.
	 * @return clone Coordonnee.
	 */
	public Coordonnee clone() {
		Coordonnee clone = new Coordonnee();
		
		clone.setCoordonneeId(this.getCoordonneeId());
		clone.setAdresse(this.getAdresse());
		clone.setCp(this.getCp());
		clone.setVille(this.getVille());
		clone.setPays(this.getPays());
		clone.setTel(this.getTel());
		clone.setFax(this.getFax());
		clone.setMail(this.getMail());
		clone.setCollaborateurs(this.getCollaborateurs());
		
		return clone;
	}

}
