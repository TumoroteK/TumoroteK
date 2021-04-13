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
package fr.aphp.tumorotek.model.io.export;

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

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table REQUETE.
 * Classe créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@Entity
@Table(name = "REQUETE")
@NamedQueries(
   value = {@NamedQuery(name = "Requete.findByUtilisateur", query = "SELECT r FROM Requete r WHERE " + "r.createur = ?1"),
      @NamedQuery(name = "Requete.findByBanque",
         query = "SELECT r FROM Requete r WHERE " + "r.banque = ?1 " + "ORDER BY r.intitule"),
      @NamedQuery(name = "Requete.findByBanqueInList",
         query = "SELECT r FROM Requete r WHERE " + "r.banque in (?1) " + "ORDER BY r.intitule"),
      @NamedQuery(name = "Requete.findByIntitule", query = "SELECT r FROM Requete r " + "WHERE r.intitule like ?1"),
      @NamedQuery(name = "Requete.findByIntituleUtilisateur",
         query = "SELECT r FROM Requete r " + "WHERE r.intitule like ?1 " + "AND r.createur = ?2"),
      @NamedQuery(name = "Requete.findByExcludedId", query = "SELECT r FROM Requete r " + "WHERE r.requeteId != ?1")})
public class Requete implements TKdataObject, Comparable<Requete>
{

   private Integer requeteId;
   private String intitule;
   private Utilisateur createur;
   private Groupement groupementRacine;
   private Banque banque;

   private Set<Recherche> recherches = new HashSet<>();

   public Requete(){
      super();
   }

   public Requete(final String i, final Groupement gRacine){
      super();
      this.intitule = i;
      this.groupementRacine = gRacine;
   }

   public Requete(final String i, final Utilisateur cr, final Groupement gRacine){
      super();
      this.intitule = i;
      this.createur = cr;
      this.groupementRacine = gRacine;
   }

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "REQUETE_ID", unique = true, nullable = false)
   public Integer getRequeteId(){
      return requeteId;
   }

   public void setRequeteId(final Integer rId){
      this.requeteId = rId;
   }

   @Column(name = "INTITULE", nullable = false)
   public String getIntitule(){
      return intitule;
   }

   public void setIntitule(final String i){
      this.intitule = i;
   }

   @OneToOne
   @JoinColumn(name = "CREATEUR_ID")
   public Utilisateur getCreateur(){
      return createur;
   }

   public void setCreateur(final Utilisateur cr){
      this.createur = cr;
   }

   @OneToOne
   @JoinColumn(name = "GROUPEMENT_RACINE_ID")
   public Groupement getGroupementRacine(){
      return groupementRacine;
   }

   public void setGroupementRacine(final Groupement gRacine){
      this.groupementRacine = gRacine;
   }

   @ManyToOne(optional = false)
   @JoinColumn(name = "BANQUE_ID")
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   @OneToMany(mappedBy = "requete", cascade = CascadeType.REMOVE)
   public Set<Recherche> getRecherches(){
      return recherches;
   }

   public void setRecherches(final Set<Recherche> recherches){
      this.recherches = recherches;
   }

   /**
    * 2 requetes sont considérées comme égales si elles ont le même intitule
    * et le même createur.
    * @param obj est la requete à tester.
    * @return true si les requetes sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Requete test = (Requete) obj;
      if(this.createur == null){
         if(test.createur == null){
            if(this.intitule == null){
               return (test.intitule == null);
            }else{
               return (this.intitule.equals(test.intitule));
            }
         }else{
            return false;
         }
      }else if(this.createur.equals(test.createur)){
         if(this.intitule == null){
            return (test.intitule == null);
         }else{
            return (this.intitule.equals(test.intitule));
         }
      }else{
         return false;
      }
   }

   /**
    * Le hashcode est calculé sur les attributs intitule et createur.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashIntitule = 0;
      int hashCreateur = 0;

      if(this.createur != null){
         hashCreateur = this.createur.hashCode();
      }
      if(this.intitule != null){
         hashIntitule = this.intitule.hashCode();
      }

      hash = 31 * hash + hashCreateur;
      hash = 31 * hash + hashIntitule;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.intitule != null){
         return "{" + this.intitule + "}";
      }else{
         return "{Empty Requete}";
      }
   }

   @Override
   public Requete clone(){
      final Requete clone = new Requete();
      clone.setCreateur(this.createur);
      clone.setGroupementRacine(this.groupementRacine);
      clone.setIntitule(this.intitule);
      clone.setRequeteId(this.requeteId);
      clone.setBanque(this.banque);
      return clone;
   }

   @Override
   public int compareTo(final Requete obj){
      return this.getIntitule().compareTo(obj.getIntitule());
   }

   @Override
   public Integer listableObjectId(){
      return getRequeteId();
   }
}
