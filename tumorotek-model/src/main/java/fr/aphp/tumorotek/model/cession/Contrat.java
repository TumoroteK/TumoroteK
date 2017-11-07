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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;



/**
 * 
 * Objet persistant mappant la table Contrat.
 * Classe créée le 03/03/10.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 * 
 */
@Entity
@Table(name = "CONTRAT")
@NamedQueries(value = {@NamedQuery(name = "Contrat.findByNumero", 
			query = "SELECT m FROM Contrat m WHERE m.numero like ?1"),
		@NamedQuery(name = "Contrat.findByPlateforme", 
			query = "SELECT c FROM Contrat c " 
				+ "WHERE c.plateforme = ?1 ORDER BY c.numero"),
		@NamedQuery(name = "Contrat.findByOrder", 
			query = "SELECT m FROM Contrat m " 
				+ "ORDER BY m.numero"),
		@NamedQuery(name = "Contrat.findByExcludedId", 
			query = "SELECT m FROM Contrat m WHERE m.contratId != ?1"),
		@NamedQuery(name = "Contrat.findByCollaborateur", 
			query = "SELECT m FROM Contrat m WHERE m.collaborateur = ?1"),
		@NamedQuery(name = "Contrat.findByService", 
			query = "SELECT m FROM Contrat m WHERE m.service = ?1"),
		@NamedQuery(name = "Contrat.findByEtablissement",
			query = "SELECT m FROM Contrat m WHERE m.etablissement = ?1")
})
public class Contrat implements TKdataObject, TKFantomableObject, Serializable {
	
	private static final long serialVersionUID = -1166135996929457768L;
	
	private Integer contratId;
	private String numero;
	private Date dateDemandeCession;
	private Date dateValidation;
	private Date dateDemandeRedaction;
	private Date dateEnvoiContrat;
	private Date dateSignature;
	private String titreProjet;
	private String description;
	private Float montant;

	private Plateforme plateforme;
	private ProtocoleType protocoleType;
	private Service service;
	private Collaborateur collaborateur;
	private Etablissement etablissement;
	
	private Set<Cession> cessions = new HashSet<Cession>();

	/** Constructeur par défaut. */
	public Contrat() {
		super();
		cessions = new HashSet<Cession>();
	}

	@Id
	@Column(name = "CONTRAT_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getContratId() {
		return this.contratId;
	}

	public void setContratId(Integer id) {
		this.contratId = id;
	}

	@Column(name = "NUMERO", nullable = false, length = 50)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String num) {
		this.numero = num;
	}

	@Column(name = "DATE_DEMANDE_CESSION", nullable = true)
	public Date getDateDemandeCession() {
		if (dateDemandeCession != null) {
			return new Date(dateDemandeCession.getTime());
		} else {
			return null;
		}
	}

	public void setDateDemandeCession(Date date) {
		if (date != null) {
			this.dateDemandeCession = new Date(date.getTime());
		} else {
			this.dateDemandeCession = null;
		}
	}

	@Column(name = "DATE_VALIDATION", nullable = true)
	public Date getDateValidation() {
		if (dateValidation != null) {
			return new Date(dateValidation.getTime());
		} else {
			return null;
		}
	}

	public void setDateValidation(Date date) {
		if (date != null) {
			this.dateValidation = new Date(date.getTime());
		} else {
			this.dateValidation = null;
		}
	}

	@Column(name = "DATE_DEMANDE_REDACTION", nullable = true)
	public Date getDateDemandeRedaction() {
		if (dateDemandeRedaction != null) {
			return new Date(dateDemandeRedaction.getTime());
		} else {
			return null;
		}
	}

	public void setDateDemandeRedaction(Date date) {
		if (date != null) {
			this.dateDemandeRedaction = new Date(date.getTime());
		} else {
			this.dateDemandeRedaction = null;
		}
	}

	@Column(name = "DATE_ENVOI_CONTRAT", nullable = true)
	public Date getDateEnvoiContrat() {
		if (dateEnvoiContrat != null) {
			return new Date(dateEnvoiContrat.getTime());
		} else {
			return null;
		}
	}

	public void setDateEnvoiContrat(Date date) {
		if (date != null) {
			this.dateEnvoiContrat = new Date(date.getTime());
		} else {
			this.dateEnvoiContrat = null;
		}
	}

	@Column(name = "DATE_SIGNATURE", nullable = true)
	public Date getDateSignature() {
		if (dateSignature != null) {
			return new Date(dateSignature.getTime());
		} else {
			return null;
		}
	}

	public void setDateSignature(Date date) {
		if (date != null) {
			this.dateSignature = new Date(date.getTime());
		} else {
			this.dateSignature = null;
		}
	}
	
	@Column(name = "TITRE_PROJET", nullable = true, length = 100)
	public String getTitreProjet() {
		return this.titreProjet;
	}

	public void setTitreProjet(String titre) {
		this.titreProjet = titre;
	}

	@Column(name = "DESCRIPTION", nullable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}
	
	@ManyToOne
	@JoinColumn(name = "PLATEFORME_ID", nullable = false)
	public Plateforme getPlateforme() {
		return plateforme;
	}

	public void setPlateforme(Plateforme p) {
		this.plateforme = p;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PROTOCOLE_TYPE_ID", nullable = true)
	public ProtocoleType getProtocoleType() {
		return this.protocoleType;
	}

	public void setProtocoleType(ProtocoleType protocole) {
		this.protocoleType = protocole;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "SERVICE_ID", nullable = true)
	public Service getService() {
		return this.service;
	}

	public void setService(Service serv) {
		this.service = serv;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
	public Collaborateur getCollaborateur() {
		return this.collaborateur;
	}

	public void setCollaborateur(Collaborateur collab) {
		this.collaborateur = collab;
	}
	
	@Column(name = "MONTANT", nullable = true)
	public Float getMontant() {
		return montant;
	}

	public void setMontant(Float m) {
		this.montant = m;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "ETABLISSEMENT_ID", nullable = true)
	public Etablissement getEtablissement() {
		return etablissement;
	}

	public void setEtablissement(Etablissement e) {
		this.etablissement = e;
	}

	@OneToMany(mappedBy = "contrat")
	public Set<Cession> getCessions() {
		return this.cessions;
	}

	public void setCessions(Set<Cession> cess) {
		this.cessions = cess;
	}

	/**
	 * 2 contrats sont considérés comme égaux s'ils ont le même numéro.
	 * @param obj est le Contrat à tester.
	 * @return true si les Contrats sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		Contrat test = (Contrat) obj;
		return ((this.numero == test.numero || (this.numero != null 
						&& this.numero.equals(test.numero))) 
				&& (this.plateforme == test.plateforme 
						|| (this.plateforme != null 
						&& this.plateforme.equals(test.plateforme)))
				);
	}

	/**
	 * Le hashcode est calculé sur l'attribut numero.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashNom = 0;
		int hashPf = 0;
		
		if (this.numero != null) {
			hashNom = this.numero.hashCode();
		}
		if (this.plateforme != null) {
			hashPf = this.plateforme.hashCode();
		}
		

		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashPf;
		
		return hash;
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.numero != null && this.plateforme != null) {
			return "{" + this.numero + ", " 
			+ plateforme.getNom() + "(Plateforme)}";
		} else {
			return "{Empty Contrat}";
		}
	}
	
	/**
	 * Cree un clone de l'objet.
	 * @return clone echantillon
	 */
	public Contrat clone() {
		Contrat clone = new Contrat();
		
		clone.setContratId(this.getContratId());
		clone.setPlateforme(this.getPlateforme());
		clone.setNumero(this.getNumero());
		clone.setDateDemandeCession(this.getDateDemandeCession());
		clone.setDateValidation(this.getDateValidation());
		clone.setDateDemandeRedaction(this.getDateDemandeRedaction());
		clone.setDateEnvoiContrat(this.getDateEnvoiContrat());
		clone.setDateSignature(this.getDateSignature());
		clone.setTitreProjet(this.getTitreProjet());
		clone.setCollaborateur(this.getCollaborateur());
		clone.setService(this.getService());
		clone.setProtocoleType(this.getProtocoleType());
		clone.setDescription(this.getDescription());
		clone.setEtablissement(this.getEtablissement());
		clone.setMontant(this.getMontant());
		
		return clone;
	}

	@Override
	public String entiteNom() {
		return "Contrat";
	}

	@Override
	@Transient
	public String getPhantomData() {
		return getNumero();
	}

	@Override
	public Integer listableObjectId() {
		return getContratId();
	}
}
