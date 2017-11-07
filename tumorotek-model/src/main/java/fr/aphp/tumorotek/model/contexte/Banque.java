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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanque;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.stats.SModele;

import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.model.systeme.Numerotation;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;

/**
 * 
 * Objet persistant mappant la table BANQUE.
 * Classe créée le 09/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 * 
 */
@Entity
@Table(name = "BANQUE")
@NamedQueries(value = {@NamedQuery(name = "Banque.findByNom",
			query = "SELECT b FROM Banque b WHERE b.nom = ?1"),
		@NamedQuery(name = "Banque.findByIdentification", 
			query = "SELECT b FROM Banque b WHERE b.identification = ?1"),
		@NamedQuery(name = "Banque.findByAutoriseCrossPatient", 
				query = "SELECT b FROM Banque b " 
					+ "WHERE b.autoriseCrossPatient = ?1"), 
		@NamedQuery(name = "Banque.findByArchive", 
				query = "SELECT b FROM Banque b WHERE b.archive = ?1"),
		@NamedQuery(name = "Banque.findByCollaborateur", 
				query = "SELECT b FROM Banque b " 
					+ "WHERE b.collaborateur = ?1"),
		@NamedQuery(name = "Banque.findByProprietaire", 
				query = "SELECT b FROM Banque b " 
					+ "WHERE b.proprietaire = ?1"),
//		@NamedQuery(name = "Banque.findByServiceId", 
//				query = "SELECT b FROM Banque b " 
//					+ "WHERE b.services.serviceId = ?1"),
		@NamedQuery(name = "Banque.findByPlateformeAndArchive", 
				query = "SELECT b FROM Banque b " 
					+ "WHERE b.plateforme = ?1 AND b.archive = ?2 "
					+ "ORDER BY b.nom"),
		@NamedQuery(name = "Banque.findByIdWithFetch", 
				query = "SELECT b FROM Banque b LEFT JOIN FETCH " 
					+ "b.collaborateur LEFT JOIN FETCH b.proprietaire " 
					+ "LEFT JOIN FETCH b.plateforme WHERE b.banqueId = ?1"),
		@NamedQuery(name = "Banque.findByOrder", 
				query = "SELECT b FROM Banque b ORDER BY b.nom"),
		@NamedQuery(name = "Banque.findByRechercheId", 
				query = "SELECT b FROM Banque b "
					+ "left join b.recherches r "
					+ "WHERE r.rechercheId = ?1"),
		@NamedQuery(name = "Banque.findContexteCatalogues", 
				query = "SELECT b.contexte.catalogues FROM Banque b " 
							+ "WHERE b.banqueId = ?1"),
		@NamedQuery(name = "Banque.findByEntiteConsultByUtilisateur", 
				query = "SELECT b FROM Banque b "
						+ "join b.profilUtilisateurs as profilU "
						+ "join profilU.pk.profil as profil "
						+ "join profil.droitObjets as droit "
						+ "WHERE b.plateforme = ?3 AND "
						+ "profilU.pk.utilisateur = ?1 "
						+ "AND droit.pk.entite = ?2 "
						+ "AND droit.pk.operationType.nom = 'Consultation' "
						+ "AND b.archive = 0"
						),
		@NamedQuery(name = "Banque.findByEntiteModifByUtilisateur", 
				query = "SELECT b FROM Banque b "
						+ "join b.profilUtilisateurs as profilU "
						+ "join profilU.pk.profil as profil "
						+ "join profil.droitObjets as droit "
						+ "WHERE b.plateforme = ?3 AND "
						+ "profilU.pk.utilisateur = ?1 "
						+ "AND droit.pk.entite = ?2 "
						+ "AND droit.pk.operationType.nom = 'Modification' " 
						+ "AND b.archive = 0 "
						+ "ORDER by b.nom"
						),
		@NamedQuery(name = "Banque.findByUtilisateurIsAdmin", 
				query = "SELECT distinct b FROM Banque b "
						+ "left join b.profilUtilisateurs as profilU "
						+ "left join profilU.pk.profil as profil "
						+ "join b.plateforme.utilisateurs as us "
						+ "WHERE b.plateforme = ?2 "
						+ "AND b.archive = 0 "
						+ "AND ((profilU.pk.utilisateur = ?1 "
						+ "AND profil.admin=1) "
						+ "OR us = ?1) " 
						+ "ORDER BY b.nom"),
		@NamedQuery(name = "Banque.findByUtilisateurAndPF", 
				query = "SELECT distinct b FROM Banque b "
						+ "left join b.profilUtilisateurs as profilU "
						+ "left join profilU.pk.profil as profil "
						+ "join b.plateforme.utilisateurs as us "
						+ "WHERE b.plateforme = ?2 " 
						+ "AND b.archive = 0 "
						+ "AND (profilU.pk.utilisateur = ?1 OR us = ?1) " 
						+ "ORDER BY b.nom"),
		@NamedQuery(name = "Banque.findByProfilUtilisateur", 
				query = "SELECT b FROM Banque b "
						+ "join b.profilUtilisateurs as profilU "
						+ "join profilU.pk.profil as profil "
						+ "WHERE profilU.pk.utilisateur = ?1 "
						+ "ORDER BY b.nom"
						),
		@NamedQuery(name = "Banque.findByExcludedId", 
				query = "SELECT b FROM Banque b " 
					+ "WHERE b.banqueId != ?1"),
		@NamedQuery(name = "Banque.findByTableAnnotation", 
				query = "SELECT b FROM Banque b " 
					+ "JOIN b.tableAnnotationBanques t "
					+ "WHERE t.pk.tableAnnotation = ?1")
})
public class Banque implements TKFantomableObject, TKdataObject, 
							java.io.Serializable, Comparable<Object> {

	private static final long serialVersionUID = 33315464531548613L;

	private Integer banqueId;
	private String nom;
	private String identification;
	private String description;
	private Boolean autoriseCrossPatient;
	private Boolean archive = false;
	private Boolean defMaladies = true;
	private String defautMaladie;
	private String defautMaladieCode;
	
	private Collaborateur collaborateur;
	private Collaborateur contact;
	private Service proprietaire;
	private Plateforme plateforme;
	private Contexte contexte;
	private Couleur echantillonCouleur;
	private Couleur prodDeriveCouleur;

	//private Set<Service> services = new HashSet<Service>();
	private Set<Conteneur> conteneurs = new HashSet<Conteneur>();
	private Set<Enceinte> enceintes = new HashSet<Enceinte>();
	private Set<Terminale> terminales = new HashSet<Terminale>();
	private Set<Prelevement> prelevements = new HashSet<Prelevement>();
	private Set<Cession> cessions = new HashSet<Cession>();
	private Set<CodeSelect> codeSelects = new HashSet<CodeSelect>();
	private Set<CodeUtilisateur> codeUtilisateurs = 
		new HashSet<CodeUtilisateur>();
	private Set<ProfilUtilisateur> profilUtilisateurs = 
		new HashSet<ProfilUtilisateur>();
	private Set<Echantillon> echantillons = new HashSet<Echantillon>();
	private Set<ProdDerive> prodDerives = new HashSet<ProdDerive>();
	private Set<TableAnnotationBanque> tableAnnotationBanques = 
									new HashSet<TableAnnotationBanque>();
	private Set<Recherche> recherches = new HashSet<Recherche>();
	private Set<CouleurEntiteType> couleurEntiteTypes = 
		new HashSet<CouleurEntiteType>();
	//private Set<TableCodage> tablesCodage = new HashSet<TableCodage>();
	private Set<BanqueTableCodage> banqueTableCodages = 
									new HashSet<BanqueTableCodage>();
	private Set<ImportTemplate> importTemplate = 
		new HashSet<ImportTemplate>();
	private Set<AffectationImprimante> affectationImprimantes = 
		new HashSet<AffectationImprimante>();
	private Set<Affichage> affichages = new HashSet<Affichage>();
	private Set<Requete> requetes = new HashSet<Requete>();
	private Set<AnnotationDefaut> annotationDefauts = 
								new HashSet<AnnotationDefaut>();
	private Set<Numerotation> numerotations = 
								new HashSet<Numerotation>();
	private Set<Template> templates = new HashSet<Template>();
	private Set<Catalogue> catalogues = new HashSet<Catalogue>();
	private Set<SModele> sModeles = new HashSet<SModele>();

	/** Constructeur par défaut. */
	public Banque() {
	}

	@Id
	@Column(name = "BANQUE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getBanqueId() {
		return banqueId;
	}

	public void setBanqueId(Integer bId) {
		this.banqueId = bId;
	}

	@Column(name = "NOM", nullable = false, length = 100)
	public String getNom() {
		return nom;
	}

	public void setNom(String n) {
		this.nom = n;
	}

	@Column(name = "IDENTIFICATION", nullable = true, length = 50)
	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identif) {
		this.identification = identif;
	}

	@Column(name = "DESCRIPTION", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	@Column(name = "AUTORISE_CROSS_PATIENT", nullable = true)
	public Boolean getAutoriseCrossPatient() {
		return autoriseCrossPatient;
	}

	public void setAutoriseCrossPatient(Boolean aut) {
		this.autoriseCrossPatient = aut;
	}

	@Column(name = "ARCHIVE", nullable = false)
	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean arch) {
		this.archive = arch;
	}
	
	@Column(name = "DEFMALADIES", nullable = false)
	public Boolean getDefMaladies() {
		return defMaladies;
	}

	public void setDefMaladies(Boolean dm) {
		this.defMaladies = dm;
	}

	@Column(name = "DEFAUT_MALADIE", nullable = true, length = 250)
	public String getDefautMaladie() {
		return defautMaladie;
	}

	public void setDefautMaladie(String defMaladie) {
		this.defautMaladie = defMaladie;
	}

	@Column(name = "DEFAUT_MALADIE_CODE", nullable = true, length = 50)
	public String getDefautMaladieCode() {
		return defautMaladieCode;
	}

	public void setDefautMaladieCode(String d) {
		this.defautMaladieCode = d;
	}

	@ManyToOne
	@JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
	public Collaborateur getCollaborateur() {
		return collaborateur;
	}

	public void setCollaborateur(Collaborateur c) {
		this.collaborateur = c;
	}

	@ManyToOne
	@JoinColumn(name = "CONTACT_ID", nullable = true)
	public Collaborateur getContact() {
		return contact;
	}

	public void setContact(Collaborateur c) {
		this.contact = c;
	}

	@ManyToOne
	@JoinColumn(name = "PROPRIETAIRE_ID", nullable = true)
	public Service getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(Service s) {
		this.proprietaire = s;
	}

	@ManyToOne
	@JoinColumn(name = "PLATEFORME_ID", nullable = false)
	public Plateforme getPlateforme() {
		return plateforme;
	}

	public void setPlateforme(Plateforme p) {
		this.plateforme = p;
	}

	@ManyToOne
	@JoinColumn(name = "CONTEXTE_ID", nullable = false)
	public Contexte getContexte() {
		return contexte;
	}

	public void setContexte(Contexte c) {
		this.contexte = c;
	}

	@ManyToOne()
	@JoinColumn(name = "ECHANTILLON_COULEUR_ID", nullable = true)
	public Couleur getEchantillonCouleur() {
		return echantillonCouleur;
	}

	public void setEchantillonCouleur(Couleur eCouleur) {
		this.echantillonCouleur = eCouleur;
	}

	@ManyToOne()
	@JoinColumn(name = "PROD_DERIVE_COULEUR_ID", nullable = true)
	public Couleur getProdDeriveCouleur() {
		return prodDeriveCouleur;
	}

	public void setProdDeriveCouleur(Couleur pCouleur) {
		this.prodDeriveCouleur = pCouleur;
	}

//	@ManyToMany(
//		cascade = {CascadeType.PERSIST, CascadeType.MERGE },
//        mappedBy = "banques",
//        targetEntity = Service.class
//	)
//	public Set<Service> getServices() {
//		return services;
//	}
//
//	public void setServices(Set<Service> newServices) {
//		this.services = newServices;
//	}

	@ManyToMany(
			targetEntity = Conteneur.class
	)
    @JoinTable(
    		name = "CONTENEUR_BANQUE",
            joinColumns = @JoinColumn(name = "BANQUE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CONTENEUR_ID")
    )
	public Set<Conteneur> getConteneurs() {
		return conteneurs;
	}

	public void setConteneurs(Set<Conteneur> cont) {
		this.conteneurs = cont;
	}

	@ManyToMany(
			targetEntity = Enceinte.class
	)
    @JoinTable(
    		name = "ENCEINTE_BANQUE",
            joinColumns = @JoinColumn(name = "BANQUE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ENCEINTE_ID")
    )
	public Set<Enceinte> getEnceintes() {
		return enceintes;
	}

	public void setEnceintes(Set<Enceinte> enc) {
		this.enceintes = enc;
	}

	@OneToMany(mappedBy = "banque")
	public Set<Terminale> getTerminales() {
		return terminales;
	}

	public void setTerminales(Set<Terminale> terms) {
		this.terminales = terms;
	}

	@OneToMany(mappedBy = "banque")
	public Set<Prelevement> getPrelevements() {
		return prelevements;
	}

	public void setPrelevements(Set<Prelevement> prelevs) {
		this.prelevements = prelevs;
	}

	@OneToMany(mappedBy = "banque")
	public Set<Cession> getCessions() {
		return cessions;
	}

	public void setCessions(Set<Cession> cess) {
		this.cessions = cess;
	}

	@OneToMany(mappedBy = "banque")
	public Set<CodeSelect> getCodeSelects() {
		return codeSelects;
	}

	public void setCodeSelects(Set<CodeSelect> codes) {
		this.codeSelects = codes;
	}
	
	@OneToMany(mappedBy = "banque")
	public Set<CodeUtilisateur> getCodeUtilisateurs() {
		return codeUtilisateurs;
	}

	public void setCodeUtilisateurs(Set<CodeUtilisateur> codes) {
		this.codeUtilisateurs = codes;
	}

	@OneToMany(mappedBy = "pk.banque", cascade = { CascadeType.REMOVE })
	public Set<ProfilUtilisateur> getProfilUtilisateurs() {
		return profilUtilisateurs;
	}

	public void setProfilUtilisateurs(Set<ProfilUtilisateur> profils) {
		this.profilUtilisateurs = profils;
	}

	@OneToMany(mappedBy = "banque")
	public Set<Echantillon> getEchantillons() {
		return echantillons;
	}

	public void setEchantillons(Set<Echantillon> e) {
		this.echantillons = e;
	}

	@OneToMany(mappedBy = "banque")
	public Set<ProdDerive> getProdDerives() {
		return prodDerives;
	}

	public void setProdDerives(Set<ProdDerive> pDerives) {
		this.prodDerives = pDerives;
	}
	
	@OneToMany(mappedBy = "pk.banque", cascade = { CascadeType.REMOVE })
	public Set<TableAnnotationBanque> getTableAnnotationBanques() {
		return this.tableAnnotationBanques;
	}

	public void setTableAnnotationBanques(Set<TableAnnotationBanque> tabs) {
		this.tableAnnotationBanques = tabs;
	}
	
	@ManyToMany(
	        mappedBy = "banques",
	        targetEntity = Recherche.class
		)
	@javax.persistence.OrderBy("intitule")
	public Set<Recherche> getRecherches() {
		return recherches;
	}

	public void setRecherches(Set<Recherche> newRecherches) {
		this.recherches = newRecherches;
	}

	@OneToMany(mappedBy = "banque")
	public Set<ImportTemplate> getImportTemplate() {
		return importTemplate;
	}

	public void setImportTemplate(Set<ImportTemplate> it) {
		this.importTemplate = it;
	}

	@OneToMany(mappedBy = "banque", cascade = { CascadeType.REMOVE })
	public Set<CouleurEntiteType> getCouleurEntiteTypes() {
		return couleurEntiteTypes;
	}

	public void setCouleurEntiteTypes(Set<CouleurEntiteType> cTypes) {
		this.couleurEntiteTypes = cTypes;
	}

//	@ManyToMany(
//			targetEntity = TableCodage.class
//	)
//    @JoinTable(
//    		name = "BANQUE_TABLE_CODAGE",
//            joinColumns = @JoinColumn(name = "BANQUE_ID"),
//            inverseJoinColumns = @JoinColumn(name = "TABLE_CODAGE_ID")
//    )
//	public Set<TableCodage> getTablesCodage() {
//		return tablesCodage;
//	}
//
//	public void setTablesCodage(Set<TableCodage> tC) {
//		this.tablesCodage = tC;
//	}
	
	@OneToMany(mappedBy = "pk.banque", cascade = { CascadeType.REMOVE })
	public Set<BanqueTableCodage> getBanqueTableCodages() {
		return banqueTableCodages;
	}

	public void setBanqueTableCodages(Set<BanqueTableCodage> btcs) {
		this.banqueTableCodages = btcs;
	}
	
	@OneToMany(mappedBy = "pk.banque", cascade = { CascadeType.REMOVE })
	public Set<AffectationImprimante> getAffectationImprimantes() {
		return affectationImprimantes;
	}

	public void setAffectationImprimantes(
			Set<AffectationImprimante> aImprimantes) {
		this.affectationImprimantes = aImprimantes;
	}

	@OneToMany(mappedBy = "banque", 
			cascade = CascadeType.REMOVE)
	public Set<Affichage> getAffichages() {
		return affichages;
	}

	public void setAffichages(Set<Affichage> a) {
		this.affichages = a;
	}

	@OneToMany(mappedBy = "banque", 
			cascade = CascadeType.REMOVE)
	public Set<Requete> getRequetes() {
		return requetes;
	}

	public void setRequetes(Set<Requete> r) {
		this.requetes = r;
	}

	@OneToMany(mappedBy = "banque", cascade = CascadeType.REMOVE)
	public Set<AnnotationDefaut> getAnnotationDefauts() {
		return annotationDefauts;
	}

	public void setAnnotationDefauts(Set<AnnotationDefaut> as) {
		this.annotationDefauts = as;
	}
	
	@OneToMany(mappedBy = "banque", cascade = CascadeType.REMOVE)
	public Set<Numerotation> getNumerotations() {
		return numerotations;
	}

	public void setNumerotations(Set<Numerotation> nums) {
		this.numerotations = nums;
	}

	@OneToMany(mappedBy = "banque")
	public Set<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(Set<Template> tps) {
		this.templates = tps;
	}

	@ManyToMany(targetEntity = Catalogue.class, fetch = FetchType.EAGER)
	@JoinTable(name = "BANQUE_CATALOGUE", 
		joinColumns = @JoinColumn(name = "BANQUE_ID"), 
		inverseJoinColumns = @JoinColumn(name = "CATALOGUE_ID"))
	public Set<Catalogue> getCatalogues() {
		return catalogues;
	}

	public void setCatalogues(Set<Catalogue> cats) {
		this.catalogues = cats;
	}
	
	@ManyToMany(
	        mappedBy = "banques",
	        targetEntity = SModele.class
	)
	public Set<SModele> getSModeles() {
		return sModeles;
	}

	public void setSModeles(Set<SModele> sim) {
		this.sModeles = sim;
	}


	/**
	 * 2 banques sont considérées comme égales si elles ont le même nom
	 * et la même reference vers la plateforme.
	 * @param obj est la banque à tester.
	 * @return true si les banques sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		Banque test = (Banque) obj;
		return ((this.nom == test.nom || (this.nom != null 
						&& this.nom.equals(test.nom))) 
				&& (this.plateforme == test.plateforme 
						|| (this.plateforme != null 
						&& this.plateforme.equals(test.plateforme)))
				);
	}

	/**
	 * Le hashcode est calculé sur l'attribut nom et la reference
	 * vers la plateforme.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		int hashNom = 0;
		int hashPf = 0;
		
		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.plateforme != null) {
			hashPf = this.plateforme.hashCode();
		}
		

		hash = 31 * hash + hashNom;
		hash = 31 * hash + hashPf;
		
		return hash;
	}
	
	public Banque clone() {
		Banque clone = new Banque();
		
		clone.setBanqueId(this.banqueId);
		clone.setCollaborateur(this.collaborateur);
		clone.setNom(this.nom);
		clone.setIdentification(this.identification);
		clone.setDescription(this.description);
		clone.setProprietaire(this.proprietaire);
		clone.setContact(this.contact);
		clone.setAutoriseCrossPatient(this.autoriseCrossPatient);
		clone.setArchive(this.archive);
		clone.setDefMaladies(this.defMaladies);
		clone.setDefautMaladie(this.getDefautMaladie());
		clone.setDefautMaladieCode(this.getDefautMaladieCode());
		clone.setContexte(this.contexte);
		clone.setPlateforme(this.plateforme);
		clone.setEchantillonCouleur(getEchantillonCouleur());
		clone.setProdDeriveCouleur(getProdDeriveCouleur());
		clone.setConteneurs(getConteneurs());
		clone.setBanqueTableCodages(getBanqueTableCodages());
		clone.setAffectationImprimantes(this.affectationImprimantes);
		clone.setAffichages(this.affichages);
		clone.setRequetes(this.requetes);
		clone.setAnnotationDefauts(getAnnotationDefauts());
		clone.setNumerotations(getNumerotations());
		clone.setTemplates(getTemplates());
		clone.setCatalogues(getCatalogues());
		clone.setSModeles(getSModeles());
		
		
		return clone;
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		return "{" + this.nom + ", " + plateforme.getNom() + "(Plateforme)}";	
	}

	@Override
	public int compareTo(Object arg0) {
		return this.getNom().compareToIgnoreCase(((Banque) arg0).getNom());
	}

	@Override
	@Transient
	public Integer listableObjectId() {
		return getBanqueId();
	}

	@Override
	public String entiteNom() {
		return "Banque";
	}

	@Override
	@Transient
	public String getPhantomData() {
		return getNom();
	}
}
