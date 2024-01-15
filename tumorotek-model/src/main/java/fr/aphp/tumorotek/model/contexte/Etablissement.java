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
import fr.aphp.tumorotek.model.cession.Contrat;

/**
 * Objet persistant mappant la table ETABLISSEMENT.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "ETABLISSEMENT")
@NamedQueries(value = {@NamedQuery(name = "Etablissement.findByOrder", query = "SELECT e FROM Etablissement e ORDER BY e.nom"),
   @NamedQuery(name = "Etablissement.findByNom", query = "SELECT e FROM Etablissement e WHERE e.nom like ?1 " + "ORDER BY e.nom"),
   @NamedQuery(name = "Etablissement.findByFiness", query = "SELECT e FROM Etablissement e WHERE e.finess like ?1"),
   @NamedQuery(name = "Etablissement.findByLocal", query = "SELECT e FROM Etablissement e WHERE e.local = ?1"),
   @NamedQuery(name = "Etablissement.findByArchiveWithOrder",
      query = "SELECT e FROM Etablissement e WHERE e.archive = ?1 " + "ORDER BY e.nom"),
   @NamedQuery(name = "Etablissement.findByCoordonnee", query = "SELECT e FROM Etablissement e " + "WHERE e.coordonnee = ?1"),
   @NamedQuery(name = "Etablissement.findByVille",
      query = "SELECT e FROM Etablissement e " + "WHERE e.coordonnee.ville like ?1 " + "ORDER BY e.nom"),
   @NamedQuery(name = "Etablissement.findByCategorie", query = "SELECT e FROM Etablissement e " + "WHERE e.categorie = ?1"),
   @NamedQuery(name = "Etablissement.findByIdWithFetch",
      query = "SELECT e FROM Etablissement e LEFT JOIN FETCH " + "e.categorie LEFT JOIN FETCH e.coordonnee "
         + "WHERE e.etablissementId = ?1"),
   @NamedQuery(name = "Etablissement.findByServiceId",
      query = "SELECT e FROM Etablissement e " + "left join e.services s " + "WHERE s.serviceId = ?1"),
   //findByCollaborateurId ne semble pas utilisée ...
   @NamedQuery(name = "Etablissement.findByCollaborateurId",
      query = "SELECT e FROM Etablissement e " + "left join e.collaborateurs c " + "WHERE c.collaborateurId = ?1"),
   @NamedQuery(name = "Etablissement.findByExcludedId",
      query = "SELECT e FROM Etablissement e " + "WHERE e.etablissementId != ?1")})
public class Etablissement implements TKdataObject, TKFantomableObject, java.io.Serializable
{

   private static final long serialVersionUID = 1235484325153L;

   private Integer etablissementId;

   private String nom;

   private String finess;

   private boolean local;

   private Boolean archive = false;

   private Coordonnee coordonnee;

   private Categorie categorie;

   private Set<Service> services = new HashSet<>();

   private Set<Collaborateur> collaborateurs = new HashSet<>();

   private Set<Contrat> contrats = new HashSet<>();

   /**
    * Constructeur par défaut.
    */
   public Etablissement(){}

   @Id
   @Column(name = "ETABLISSEMENT_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getEtablissementId(){
      return etablissementId;
   }

   public void setEtablissementId(final Integer eId){
      this.etablissementId = eId;
   }

   @Column(name = "NOM", nullable = false, length = 100)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "FINESS", nullable = true, length = 20)
   public String getFiness(){
      return finess;
   }

   public void setFiness(final String fin){
      this.finess = fin;
   }

   @Column(name = "LOCAL", nullable = true)
   public boolean isLocal(){
      return local;
   }

   public void setLocal(final boolean loc){
      this.local = loc;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public Boolean getArchive(){
      return archive;
   }

   public void setArchive(final Boolean arch){
      this.archive = arch;
   }

   //@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
   //@JoinColumn(name = "COORDONNEE_ID", nullable = true)
   @OneToOne(optional = true, cascade = CascadeType.ALL)
   @JoinColumn(name = "COORDONNEE_ID", nullable = true)
   public Coordonnee getCoordonnee(){
      return coordonnee;
   }

   public void setCoordonnee(final Coordonnee c){
      this.coordonnee = c;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "CATEGORIE_ID", nullable = true)
   public Categorie getCategorie(){
      return categorie;
   }

   public void setCategorie(final Categorie c){
      this.categorie = c;
   }

   @OneToMany(mappedBy = "etablissement")
   @javax.persistence.OrderBy("nom")
   public Set<Service> getServices(){
      return services;
   }

   public void setServices(final Set<Service> newServices){
      this.services = newServices;
   }

   @OneToMany(mappedBy = "etablissement")
   @javax.persistence.OrderBy("nom, prenom")
   public Set<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final Set<Collaborateur> newCollaborateurs){
      this.collaborateurs = newCollaborateurs;
   }

   @OneToMany(mappedBy = "etablissement")
   public Set<Contrat> getContrats(){
      return contrats;
   }

   public void setContrats(final Set<Contrat> c){
      this.contrats = c;
   }

   /**
    * 2 établissements sont considérés comme égaux s'ils ont le même nom et
    * le même finess.
    * @param obj est l'établissement à tester.
    * @return true si les établissements sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Etablissement test = (Etablissement) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.finess == test.finess || (this.finess != null && this.finess.equals(test.finess))));
   }

   /**
    * Le hashcode est calculé sur les attributs nom et finess.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashNom = 0;
      int hashFiness = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.finess != null){
         hashFiness = this.finess.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashFiness;

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
      return "{Empty Etablissement}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Etablissement.
    */
   @Override
   public Etablissement clone(){
      final Etablissement clone = new Etablissement();

      clone.setEtablissementId(this.getEtablissementId());
      clone.setCoordonnee(this.getCoordonnee());
      clone.setCategorie(this.getCategorie());
      clone.setNom(this.getNom());
      clone.setFiness(this.getFiness());
      clone.setLocal(this.isLocal());
      clone.setArchive(this.getArchive());
      clone.setServices(this.getServices());
      clone.setCollaborateurs(this.getCollaborateurs());
      clone.setContrats(this.getContrats());

      return clone;
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   @Override
   public String entiteNom(){
      return "Etablissement";
   }

   @Override
   public Integer listableObjectId(){
      return getEtablissementId();
   }
}
