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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Objet persistant mappant la table COLLABORATEUR.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "COLLABORATEUR")
//@NamedQueries(value = {@NamedQuery(name = "Collaborateur.findByOrder", query = "SELECT c FROM Collaborateur c ORDER BY c.nom"),
//   @NamedQuery(name = "Collaborateur.findByNom", query = "SELECT c FROM Collaborateur c WHERE c.nom like ?1"),
//   @NamedQuery(name = "Collaborateur.findByPrenom", query = "SELECT c FROM Collaborateur c WHERE c.prenom like ?1"),
//   @NamedQuery(name = "Collaborateur.findByArchive",
//      query = "SELECT c FROM Collaborateur c WHERE c.archive " + "= ?1 ORDER BY c.nom, c.prenom"),
//   @NamedQuery(name = "Collaborateur.findByExcludedId",
//      query = "SELECT c FROM Collaborateur c " + "WHERE c.collaborateurId != ?1"),
//   @NamedQuery(name = "Collaborateur.findCountByServiceId",
//      query = "SELECT count(c) FROM Collaborateur c " + "left join c.services s " + "WHERE s.serviceId = ?1"),
//   //		@NamedQuery(name = "Collaborateur.findByCoordonnee", 
//   //				query = "SELECT c FROM Collaborateur c " 
//   //					+ "WHERE c.coordonnee = ?1"),
//   @NamedQuery(name = "Collaborateur.findByEtablissement",
//      query = "SELECT c FROM Collaborateur c " + "WHERE c.etablissement = ?1"),
//   @NamedQuery(name = "Collaborateur.findByEtablissementWithOrder",
//      query = "SELECT c FROM Collaborateur c " + "WHERE c.etablissement = ?1 " + "ORDER BY c.nom, c.prenom"),
//   @NamedQuery(name = "Collaborateur.findByEtablissementArchiveWithOrder",
//      query = "SELECT c FROM Collaborateur c " + "WHERE c.etablissement = ?1 " + "AND c.archive = ?2 "
//         + "ORDER BY c.nom, c.prenom"),
//   @NamedQuery(name = "Collaborateur.findBySpecialite", query = "SELECT c FROM Collaborateur c " + "WHERE c.specialite = ?1"),
//   @NamedQuery(name = "Collaborateur.findByTitre", query = "SELECT c FROM Collaborateur c " + "WHERE c.titre = ?1"),
//   @NamedQuery(name = "Collaborateur.findByServiceIdWithOrder",
//      query = "SELECT c FROM Collaborateur c " + "left join c.services s " + "WHERE s.serviceId = ?1 "
//         + "ORDER BY c.nom, c.prenom"),
//   @NamedQuery(name = "Collaborateur.findByServiceIdArchiveWithOrder",
//      query = "SELECT c FROM Collaborateur c " + "left join c.services s " + "WHERE s.serviceId = ?1 " + "AND c.archive = ?2 "
//         + "ORDER BY c.nom, c.prenom"),
//   @NamedQuery(name = "Collaborateur.findByServiceId",
//      query = "SELECT c FROM Collaborateur c " + "left join c.services s " + "WHERE s.serviceId = ?1"),
//   @NamedQuery(name = "Collaborateur.findByPlateformeId",
//      query = "SELECT c FROM Collaborateur c " + "left join c.plateformes p " + "WHERE p.plateformeId = ?1"),
//   @NamedQuery(name = "Collaborateur.findByBanqueId",
//      query = "SELECT c FROM Collaborateur c " + "left join c.banques b " + "WHERE b.banqueId = ?1"),
//   @NamedQuery(name = "Collaborateur.findByUtilisateurId",
//      query = "SELECT c FROM Collaborateur c " + "left join c.utilisateurs u " + "WHERE u.utilisateurId = ?1"),
//   @NamedQuery(name = "Collaborateur.findByCollaborateurWithoutService",
//      query = "SELECT c FROM Collaborateur c " + "left JOIN c.services s WHERE s is null"),
//   @NamedQuery(name = "Collaborateur.findByEtablissementNoService",
//      query = "SELECT c FROM Collaborateur c " + "left JOIN c.services s WHERE s is null " + "AND c.etablissement = ?1"),
//   @NamedQuery(name = "Collaborateur.findByCollaborateurWithoutServiceAndArchive",
//      query = "SELECT c FROM Collaborateur c " + "left JOIN c.services s WHERE s is null " + "AND c.archive = ?1"),
//   @NamedQuery(name = "Collaborateur.findByIdWithFetch",
//      query = "SELECT c FROM Collaborateur c LEFT JOIN FETCH " + "c.etablissement LEFT JOIN FETCH c.coordonnees "
//         + "LEFT JOIN FETCH c.specialite LEFT JOIN FETCH c.titre " + "WHERE c.collaborateurId = ?1"),
//   @NamedQuery(name = "Collaborateur.findCountByEtablissement",
//      query = "SELECT count(c) FROM Collaborateur c " + "WHERE c.etablissement = (?1)"),
//   /*	@NamedQuery(name = "Collaborateur.findByVilleLikeManager", 
//   				query = "SELECT c FROM Collaborateur c " 
//   					+ "WHERE c.coordonnee.ville like ?1 " 
//   					+ "ORDER BY c.nom")*/
//   @NamedQuery(name = "Collaborateur.findByVille",
//      query = "SELECT c FROM Collaborateur c " + "left join c.coordonnees s " + "WHERE s.ville like ?1 " + "ORDER BY c.nom")})
public class Collaborateur implements TKdataObject, TKFantomableObject, java.io.Serializable
{

   private static final long serialVersionUID = 8764349963544695L;

   private Integer collaborateurId;
   private String nom;
   private String prenom;
   private Boolean archive = false;

   private Etablissement etablissement;
   private Specialite specialite;
   private Titre titre;

   private Set<Coordonnee> coordonnees = new HashSet<>();
   private Set<Service> services = new HashSet<>();
   private Set<Plateforme> plateformes = new HashSet<>();
   private Set<Banque> banques = new HashSet<>();
   private Set<Banque> contactBanques = new HashSet<>();
   private Set<Utilisateur> utilisateurs = new HashSet<>();
   private Set<Maladie> maladies = new HashSet<>();
   private Set<Prelevement> prelevementsPreleveur = new HashSet<>();
   private Set<Prelevement> prelevementsOperateur = new HashSet<>();
   private Set<Echantillon> echantillons = new HashSet<>();
   private Set<ProdDerive> prodDerives = new HashSet<>();
   private Set<Cession> cessionDestinataires = new HashSet<>();
   private Set<Cession> cessionDemandeurs = new HashSet<>();
   private Set<Cession> cessionExecutants = new HashSet<>();
   private Set<Contrat> contrats = new HashSet<>();
   private Set<LaboInter> laboInters = new HashSet<>();
   private Set<PatientMedecin> patientMedecins = new HashSet<>();
   private Set<Retour> retours = new HashSet<>();

   /** Constructeur. */
   public Collaborateur(){}

   //	/**
   //	 * Constructeur avec paramètres.
   //	 * @param id .
   //	 * @param n .
   //	 * @param p .
   //	 * @param init .
   //	 * @param arch .
   //	 */
   //	public Collaborateur(Integer id, String n, String p, String init,
   //			boolean arch) {
   //		this.collaborateurId = id;
   //		this.nom = n;
   //		this.prenom = p;
   //		this.initiales = init;
   //		this.archive = arch;
   //	}

   @Id
   @Column(name = "COLLABORATEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCollaborateurId(){
      return collaborateurId;
   }

   public void setCollaborateurId(final Integer cId){
      this.collaborateurId = cId;
   }

   @Column(name = "NOM", nullable = false, length = 30)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "PRENOM", nullable = true, length = 30)
   public String getPrenom(){
      return prenom;
   }

   public void setPrenom(final String p){
      this.prenom = p;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   @ManyToOne
   @JoinColumn(name = "ETABLISSEMENT_ID", nullable = true)
   public Etablissement getEtablissement(){
      return etablissement;
   }

   public void setEtablissement(final Etablissement e){
      this.etablissement = e;
   }

   @ManyToMany(targetEntity = Coordonnee.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(name = "COLLABORATEUR_COORDONNEE", joinColumns = @JoinColumn(name = "COLLABORATEUR_ID"),
      inverseJoinColumns = @JoinColumn(name = "COORDONNEE_ID"))
   @OrderBy(value = "coordonneeId")
   public Set<Coordonnee> getCoordonnees(){
      return coordonnees;
   }

   public void setCoordonnees(final Set<Coordonnee> cs){
      this.coordonnees = cs;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "SPECIALITE_ID", nullable = true)
   public Specialite getSpecialite(){
      return specialite;
   }

   public void setSpecialite(final Specialite s){
      this.specialite = s;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "TITRE_ID", nullable = true)
   public Titre getTitre(){
      return titre;
   }

   public void setTitre(final Titre t){
      this.titre = t;
   }

   @ManyToMany(mappedBy = "collaborateurs", targetEntity = Service.class)
   @javax.persistence.OrderBy("nom")
   public Set<Service> getServices(){
      return services;
   }

   public void setServices(final Set<Service> newServices){
      this.services = newServices;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<Plateforme> getPlateformes(){
      return plateformes;
   }

   public void setPlateformes(final Set<Plateforme> newPlateformes){
      this.plateformes = newPlateformes;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final Set<Banque> newBanques){
      this.banques = newBanques;
   }

   @OneToMany(mappedBy = "contact")
   public Set<Banque> getContactBanques(){
      return contactBanques;
   }

   public void setContactBanques(final Set<Banque> cB){
      this.contactBanques = cB;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<Utilisateur> getUtilisateurs(){
      return utilisateurs;
   }

   public void setUtilisateurs(final Set<Utilisateur> newUtilisateurs){
      this.utilisateurs = newUtilisateurs;
   }

   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "collaborateurs", targetEntity = Maladie.class)
   public Set<Maladie> getMaladies(){
      return maladies;
   }

   public void setMaladies(final Set<Maladie> mals){
      this.maladies = mals;
   }

   @OneToMany(mappedBy = "preleveur")
   public Set<Prelevement> getPrelevementsPreleveur(){
      return prelevementsPreleveur;
   }

   public void setPrelevementsPreleveur(final Set<Prelevement> prelevs){
      this.prelevementsPreleveur = prelevs;
   }

   @OneToMany(mappedBy = "operateur")
   public Set<Prelevement> getPrelevementsOperateur(){
      return prelevementsOperateur;
   }

   public void setPrelevementsOperateur(final Set<Prelevement> prelevs){
      this.prelevementsOperateur = prelevs;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final Set<Echantillon> echants){
      this.echantillons = echants;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<ProdDerive> getProdDerives(){
      return prodDerives;
   }

   public void setProdDerives(final Set<ProdDerive> prods){
      this.prodDerives = prods;
   }

   @OneToMany(mappedBy = "destinataire")
   public Set<Cession> getCessionDestinataires(){
      return cessionDestinataires;
   }

   public void setCessionDestinataires(final Set<Cession> cessions){
      this.cessionDestinataires = cessions;
   }

   @OneToMany(mappedBy = "demandeur")
   public Set<Cession> getCessionDemandeurs(){
      return cessionDemandeurs;
   }

   public void setCessionDemandeurs(final Set<Cession> cessions){
      this.cessionDemandeurs = cessions;
   }

   @OneToMany(mappedBy = "executant")
   public Set<Cession> getCessionExecutants(){
      return cessionExecutants;
   }

   public void setCessionExecutants(final Set<Cession> cessions){
      this.cessionExecutants = cessions;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<Contrat> getContrats(){
      return this.contrats;
   }

   public void setContrats(final Set<Contrat> conts){
      this.contrats = conts;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<LaboInter> getLaboInters(){
      return laboInters;
   }

   public void setLaboInters(final Set<LaboInter> labos){
      this.laboInters = labos;
   }

   @OneToMany(mappedBy = "pk.collaborateur")
   public Set<PatientMedecin> getPatientMedecins(){
      return this.patientMedecins;
   }

   public void setPatientMedecins(final Set<PatientMedecin> pMs){
      this.patientMedecins = pMs;
   }

   @OneToMany(mappedBy = "collaborateur")
   public Set<Retour> getRetours(){
      return this.retours;
   }

   public void setRetours(final Set<Retour> rets){
      this.retours = rets;
   }

   /**
    * 2 collaborateurs sont considérés comme égaux s'ils ont les mêmes
    * noms, prénoms et specialite.
    * @param obj est le collaborateur à tester.
    * @return true si les collaborateurs sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Collaborateur test = (Collaborateur) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.prenom == test.prenom || (this.prenom != null && this.prenom.equals(test.prenom)))
         && (this.specialite == test.specialite || (this.specialite != null && this.specialite.equals(test.specialite))));
   }

   /**
    * Le hashcode est calculé sur les attributs nom et prénom
    * et specialite.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashPrenom = 0;
      int hashSpec = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.prenom != null){
         hashPrenom = this.prenom.hashCode();
      }
      if(this.specialite != null){
         hashSpec = this.specialite.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashPrenom;
      hash = 31 * hash + hashSpec;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }
      return "{Empty Collaborateur}";
   }

   /**
    * Méthode renvoyant la concaténation du nom et du prénom.
    * (utilisée dans l'interface)
    */
   @Transient
   public String getNomAndPrenom(){
      final StringBuffer sb = new StringBuffer();
      if(this.nom != null){
         sb.append(this.nom);
      }
      if(this.nom != null && this.prenom != null){
         sb.append(" ");
      }
      if(this.prenom != null){
         sb.append(this.prenom);
      }
      return sb.toString();
   }

   public String nomAndPrenom(){
      return getNomAndPrenom();
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Collaborateur.
    */
   @Override
   public Collaborateur clone(){
      final Collaborateur clone = new Collaborateur();
      clone.setCollaborateurId(this.collaborateurId);
      clone.setEtablissement(this.getEtablissement());
      clone.setSpecialite(this.getSpecialite());
      clone.setNom(this.getNom());
      clone.setPrenom(this.getPrenom());
      clone.setTitre(this.getTitre());
      clone.setArchive(this.getArchive());
      clone.setServices(this.getServices());
      clone.setCoordonnees(this.getCoordonnees());

      return clone;
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   @Override
   public String entiteNom(){
      return "Collaborateur";
   }

   @Override
   public Integer listableObjectId(){
      return getCollaborateurId();
   }

}
