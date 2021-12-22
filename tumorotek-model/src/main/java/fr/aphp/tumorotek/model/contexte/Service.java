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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * Objet persistant mappant la table SERVICE.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "SERVICE")
//@NamedQueries(value = {@NamedQuery(name = "Service.findByNom", query = "SELECT s FROM Service s WHERE s.nom like ?1"),
//   @NamedQuery(name = "Service.findByOrder", query = "SELECT s FROM Service s ORDER BY s.etablissement.nom, s.nom"),
//   @NamedQuery(name = "Service.findByArchiveWithOrder",
//      query = "SELECT s FROM Service s WHERE s.archive = ?1 " + "ORDER BY s.nom"),
//   @NamedQuery(name = "Service.findByCoordonnee", query = "SELECT s FROM Service s " + "WHERE s.coordonnee = ?1"),
//   @NamedQuery(name = "Service.findByEtablissement", query = "SELECT s FROM Service s " + "WHERE s.etablissement = ?1"),
//   @NamedQuery(name = "Service.findByEtablissementWithOrder",
//      query = "SELECT s FROM Service s " + "WHERE s.etablissement = ?1 " + "ORDER BY s.nom"),
//   @NamedQuery(name = "Service.findByEtablissementArchiveWithOrder",
//      query = "SELECT s FROM Service s " + "WHERE s.etablissement = ?1 " + "AND s.archive =?2 " + "ORDER BY s.nom"),
//   @NamedQuery(name = "Service.findByCollaborateurId",
//      query = "SELECT s FROM Service s " + "join s.collaborateurs c " + "WHERE c.collaborateurId = ?1"),
//   @NamedQuery(name = "Service.findByCollaborateurIdAndArchive",
//      query = "SELECT s FROM Service s " + "left join s.collaborateurs c " + "WHERE c.collaborateurId = ?1 "
//         + "AND s.archive =?2"),
//   @NamedQuery(name = "Service.findCountByEtablissementId",
//      query = "SELECT count(s) FROM Service s " + "left join s.etablissement e " + "WHERE e.etablissementId = (?1)"),
//   //		@NamedQuery(name = "Service.findByBanqueId", 
//   //				query = "SELECT s FROM Service s " 
//   //					+ "WHERE s.banques.banqueId = ?1"),
//   @NamedQuery(name = "Service.findByBanquePossedeesId",
//      query = "SELECT s FROM Service s " + "left join s.banquesPossedees b " + "WHERE b.banqueId = ?1"),
//   @NamedQuery(name = "Service.findByIdWithFetch",
//      query = "SELECT s FROM Service s LEFT JOIN FETCH " + "s.etablissement LEFT JOIN FETCH s.coordonnee "
//         + "WHERE s.serviceId = ?1"),
//   @NamedQuery(name = "Service.findByExcludedId", query = "SELECT s FROM Service s " + "WHERE s.serviceId != ?1"), @NamedQuery(
//      name = "Service.findByVille", query = "SELECT s FROM Service s " + "WHERE s.coordonnee.ville like ?1 " + "ORDER BY s.nom")})
public class Service implements TKdataObject, TKFantomableObject, java.io.Serializable
{

   private static final long serialVersionUID = 54864135646414L;

   private Integer serviceId;
   private String nom;
   private Boolean archive = false;

   private Etablissement etablissement;
   private Coordonnee coordonnee;

   private Set<Collaborateur> collaborateurs = new HashSet<>();
   //private Set<Banque> banques = new HashSet<Banque>();
   private Set<Banque> banquesPossedees = new HashSet<>();
   private Set<Conteneur> conteneurs = new HashSet<>();
   private Set<Prelevement> prelevements = new HashSet<>();
   private Set<Cession> cessions = new HashSet<>();
   private Set<Contrat> contrats = new HashSet<>();
   private Set<LaboInter> laboInters = new HashSet<>();

   /** Constructeur par défaut. */
   public Service(){}

   /**
    * Constructeur avec paramètres.
    * @param id .
    * @param n .
    * @param descr .
    * @param arch .
    */
   public Service(final Integer id, final String n, final String descr, final boolean arch){
      this.serviceId = id;
      this.nom = n;
      this.archive = arch;
   }

   @Id
   @Column(name = "SERVICE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getServiceId(){
      return serviceId;
   }

   public void setServiceId(final Integer sId){
      this.serviceId = sId;
   }

   @Column(name = "NOM", nullable = false, length = 100)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   @ManyToOne
   @JoinColumn(name = "ETABLISSEMENT_ID", nullable = false)
   public Etablissement getEtablissement(){
      return etablissement;
   }

   public void setEtablissement(final Etablissement e){
      this.etablissement = e;
   }

   @OneToOne(optional = true, cascade = CascadeType.ALL)
   @JoinColumn(name = "COORDONNEE_ID", nullable = true)
   public Coordonnee getCoordonnee(){
      return coordonnee;
   }

   public void setCoordonnee(final Coordonnee c){
      this.coordonnee = c;
   }

   @ManyToMany(targetEntity = Collaborateur.class, cascade = {})
   @JoinTable(name = "SERVICE_COLLABORATEUR", joinColumns = @JoinColumn(name = "SERVICE_ID"),
      inverseJoinColumns = @JoinColumn(name = "COLLABORATEUR_ID"))
   @javax.persistence.OrderBy("nom, prenom")
   public Set<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final Set<Collaborateur> collabs){
      this.collaborateurs = collabs;
   }

   //	@ManyToMany(
   //			targetEntity = Banque.class,
   //	        cascade = {CascadeType.PERSIST, CascadeType.MERGE }
   //	)
   //    @JoinTable(
   //    		name = "BANQUE_STOCKAGE",
   //            joinColumns = @JoinColumn(name = "SERVICE_ID"),
   //            inverseJoinColumns = @JoinColumn(name = "BANQUE_ID")
   //    )
   //	public Set<Banque> getBanques() {
   //		return banques;
   //	}
   //
   //	public void setBanques(Set<Banque> banks) {
   //		this.banques = banks;
   //	}

   @OneToMany(mappedBy = "proprietaire")
   public Set<Banque> getBanquesPossedees(){
      return banquesPossedees;
   }

   public void setBanquesPossedees(final Set<Banque> banquesPoss){
      this.banquesPossedees = banquesPoss;
   }

   @OneToMany(mappedBy = "service")
   public Set<Conteneur> getConteneurs(){
      return conteneurs;
   }

   public void setConteneurs(final Set<Conteneur> conts){
      this.conteneurs = conts;
   }

   @OneToMany(mappedBy = "servicePreleveur")
   public Set<Prelevement> getPrelevements(){
      return prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevs){
      this.prelevements = prelevs;
   }

   @OneToMany(mappedBy = "serviceDest")
   public Set<Cession> getCessions(){
      return cessions;
   }

   public void setCessions(final Set<Cession> cess){
      this.cessions = cess;
   }

   @OneToMany(mappedBy = "service")
   public Set<Contrat> getContrats(){
      return this.contrats;
   }

   public void setContrats(final Set<Contrat> conts){
      this.contrats = conts;
   }

   @OneToMany(mappedBy = "service")
   public Set<LaboInter> getLaboInters(){
      return laboInters;
   }

   public void setLaboInters(final Set<LaboInter> labos){
      this.laboInters = labos;
   }

   /**
    * 2 services sont considérés comme égaux s'ils ont le même nom et
    * la même reference vers etablissement.
    * @param obj est le service à tester.
    * @return true si les services sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Service test = (Service) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.etablissement == test.etablissement
            || (this.etablissement != null && this.etablissement.equals(test.etablissement))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom et la
    * reference vers etablissement.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashEtab = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.etablissement != null){
         hashEtab = this.etablissement.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashEtab;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      return "{" + this.nom + "}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Service.
    */
   @Override
   public Service clone(){
      final Service clone = new Service();

      clone.setServiceId(this.getServiceId());
      clone.setCoordonnee(this.getCoordonnee());
      clone.setEtablissement(this.getEtablissement());
      clone.setNom(this.getNom());
      clone.setArchive(this.getArchive());
      clone.setCollaborateurs(this.getCollaborateurs());

      return clone;
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   @Override
   public String entiteNom(){
      return "Service";
   }

   @Override
   public Integer listableObjectId(){
      return getServiceId();
   }
}
