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
package fr.aphp.tumorotek.model.utilisateur;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Objet persistant mappant la table PROFIL.
 * Classe créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
@Entity
@Table(name = "PROFIL")
@NamedQueries(value = {@NamedQuery(name = "Profil.findByOrder", query = "SELECT p FROM Profil p ORDER BY p.nom"),
   @NamedQuery(name = "Profil.findByNom", query = "SELECT p FROM Profil p WHERE p.nom like ?1"),
   @NamedQuery(name = "Profil.findByAnonyme", query = "SELECT p FROM Profil p " + "WHERE p.anonyme = ?1"),
   @NamedQuery(name = "Profil.findByDoublon", query = "SELECT p FROM Profil p " + "WHERE p.nom = ?1 AND p.anonyme = ?2"),
   @NamedQuery(name = "Profil.findByExcludedId", query = "SELECT p FROM Profil p " + "WHERE p.profilId != ?1"),
   @NamedQuery(name = "Profil.findByPlateformeAndArchive",
      query = "SELECT p FROM Profil p " + "WHERE p.plateforme = ?1 AND p.archive = ?2 " + "ORDER BY p.nom")})
public class Profil implements TKdataObject, Serializable, Comparable<Profil>
{

   private Integer profilId;

   private String nom;

   private Boolean anonyme;

   private Boolean admin;

   private Boolean accesAdministration;

   private Integer profilExport;

   // @since 2.1
   private Boolean archive = false;

   private Plateforme plateforme;

   private Set<DroitObjet> droitObjets = new HashSet<>();

   private Set<ProfilUtilisateur> profilUtilisateurs = new HashSet<>();

   private static final long serialVersionUID = 77874545646151L;

   /** Constructeur par défaut. */
   public Profil(){}

   @Id
   @Column(name = "PROFIL_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getProfilId(){
      return this.profilId;
   }

   public void setProfilId(final Integer id){
      this.profilId = id;
   }

   @Column(name = "NOM", nullable = false, length = 100)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "ANONYME", nullable = true)
   public Boolean getAnonyme(){
      return this.anonyme;
   }

   @Column(name = "ADMIN", nullable = false)
   public Boolean getAdmin(){
      return this.admin;
   }

   public void setAdmin(final Boolean a){
      this.admin = a;
   }

   public void setAnonyme(final Boolean ano){
      this.anonyme = ano;
   }

   @Column(name = "ACCES_ADMINISTRATION", nullable = false)
   public Boolean getAccesAdministration(){
      return this.accesAdministration;
   }

   public void setAccesAdministration(final Boolean aAdministration){
      this.accesAdministration = aAdministration;
   }

   @OneToMany(mappedBy = "pk.profil")
   public Set<DroitObjet> getDroitObjets(){
      return this.droitObjets;
   }

   public void setDroitObjets(final Set<DroitObjet> droits){
      this.droitObjets = droits;
   }

   @OneToMany(mappedBy = "pk.profil")
   public Set<ProfilUtilisateur> getProfilUtilisateurs(){
      return this.profilUtilisateurs;
   }

   public void setProfilUtilisateurs(final Set<ProfilUtilisateur> profils){
      this.profilUtilisateurs = profils;
   }

   @Column(name = "PROFIL_EXPORT", nullable = true)
   public Integer getProfilExport(){
      return profilExport;
   }

   public void setProfilExport(final Integer pExport){
      this.profilExport = pExport;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public boolean isArchive(){
      return archive;
   }

   public void setArchive(final boolean arch){
      this.archive = arch;
   }

   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme pf){
      this.plateforme = pf;
   }

   /**
    * 2 profils sont considérés comme égaux s'ils ont les mêmes
    * noms et appartiennent à la même plateforme.
    * @param obj est le profil à tester.
    * @return true si les profils sont égaux.
    * @version 2.1
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Profil test = (Profil) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur les attributs nom et plateforme.
    * @return la valeur du hashcode.
    * @version 2.1
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashPF = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashPF;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }else{
         return "{Empty Profil}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Profil.
    * @version 2.1
    */
   @Override
   public Profil clone(){
      final Profil clone = new Profil();

      clone.setProfilId(this.profilId);
      clone.setNom(this.nom);
      clone.setAnonyme(this.anonyme);
      clone.setAdmin(this.admin);
      clone.setAccesAdministration(this.accesAdministration);
      clone.setDroitObjets(this.droitObjets);
      clone.setProfilUtilisateurs(this.profilUtilisateurs);
      clone.setProfilExport(this.profilExport);
      clone.setArchive(this.archive);
      clone.setPlateforme(this.plateforme);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return getProfilId();
   }

   @Override
   public int compareTo(final Profil o){
      return this.getNom().compareToIgnoreCase(o.getNom());
   }
}
