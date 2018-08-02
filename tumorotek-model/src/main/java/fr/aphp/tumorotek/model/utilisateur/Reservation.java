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

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;

/**
 *
 * Objet persistant mappant la table RESERVATION.
 * Classe créée le 10/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "RESERVATION")
@NamedQueries(value = {@NamedQuery(name = "Reservation.findByDebut", query = "SELECT r FROM Reservation r WHERE r.debut = ?1"),
   @NamedQuery(name = "Reservation.findByFin", query = "SELECT r FROM Reservation r WHERE r.fin = ?1"),
   @NamedQuery(name = "Reservation.findByDebutAfterDate", query = "SELECT r FROM Reservation r WHERE r.debut > ?1"),
   @NamedQuery(name = "Reservation.findByFinBeforeDate", query = "SELECT r FROM Reservation r WHERE r.fin < ?1"),
   @NamedQuery(name = "Reservation.findByDateBetweenDebutFin",
      query = "SELECT r FROM Reservation r " + "WHERE r.debut < ?1 and r.fin > ?1"),
   @NamedQuery(name = "Reservation.findByUtilisateur", query = "SELECT r FROM Reservation r " + "WHERE r.utilisateur= ?1"),
   @NamedQuery(name = "Reservation.findDoublon",
      query = "SELECT r FROM Reservation r" + " WHERE r.debut = ?1 AND r.fin = ?2" + " AND r.utilisateur= ?3"),
   @NamedQuery(name = "Reservation.findByIdWithFetch",
      query = "SELECT r FROM Reservation r LEFT JOIN FETCH " + "r.utilisateur WHERE r.reservationId = ?1")})
public class Reservation implements Serializable
{

   private static final long serialVersionUID = 8743121543120154L;

   private Integer reservationId;
   private Date debut;
   private Date fin;

   private Utilisateur utilisateur;

   private Set<Echantillon> echantillons = new HashSet<>();
   private Set<ProdDerive> prodDerives = new HashSet<>();

   /** Constructeur par défaut. */
   public Reservation(){}

   /**
    * Constructeur avec paramètres.
    * @param id .
    * @param d .
    * @param f .
    */
   public Reservation(final Integer id, final Date d, final Date f){
      this.reservationId = id;
      if(d != null){
         this.debut = new Date(d.getTime());
      }else{
         this.debut = null;
      }
      if(f != null){
         this.fin = new Date(f.getTime());
      }else{
         this.fin = null;
      }
   }

   @Id
   @Column(name = "RESERVATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getReservationId(){
      return reservationId;
   }

   public void setReservationId(final Integer id){
      this.reservationId = id;
   }

   @Column(name = "DEBUT", nullable = true)
   public Date getDebut(){
      if(debut != null){
         return new Date(debut.getTime());
      }else{
         return null;
      }
   }

   public void setDebut(final Date d){
      if(d != null){
         this.debut = new Date(d.getTime());
      }else{
         this.debut = null;
      }
   }

   @Column(name = "FIN", nullable = true)
   public Date getFin(){
      if(fin != null){
         return new Date(fin.getTime());
      }else{
         return null;
      }
   }

   public void setFin(final Date f){
      if(f != null){
         this.fin = new Date(f.getTime());
      }else{
         this.fin = null;
      }
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "UTILISATEUR_ID", nullable = false)
   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   @OneToMany(mappedBy = "reservation")
   public Set<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final Set<Echantillon> echants){
      this.echantillons = echants;
   }

   @OneToMany(mappedBy = "reservation")
   public Set<ProdDerive> getProdDerives(){
      return prodDerives;
   }

   public void setProdDerives(final Set<ProdDerive> prods){
      this.prodDerives = prods;
   }

   /**
    * 2 réservations sont considérées comme égales si elles ont la même date
    * de début, la même date de fin et la meme reference vers utilisateur.
    * @param obj est la réservation à tester.
    * @return true si les réservations sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Reservation test = (Reservation) obj;
      return ((this.debut == test.debut || (this.debut != null && this.debut.equals(test.debut)))
         && (this.fin == test.fin || (this.fin != null && this.fin.equals(test.fin)))
         && (this.utilisateur == test.utilisateur || (this.utilisateur != null && this.utilisateur.equals(test.utilisateur))));
   }

   /**
    * Le hashcode est calculé sur les attributs debut et fin
    * et sur la reference vers utilisateur.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashDeb = 0;
      int hashFin = 0;
      int hashUtilisateur = 0;

      if(this.debut != null){
         hashDeb = this.debut.hashCode();
      }
      if(this.fin != null){
         hashFin = this.fin.hashCode();
      }
      if(this.utilisateur != null){
         hashUtilisateur = this.utilisateur.hashCode();
      }

      hash = 7 * hash + hashDeb;
      hash = 7 * hash + hashFin;
      hash = 7 * hash + hashUtilisateur;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.debut != null && this.fin != null){
         return "{" + this.debut + " - " + this.fin + "}";
      }else{
         return "{Empty Reservation}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Cession.
    */
   @Override
   public Reservation clone(){
      final Reservation clone = new Reservation();

      clone.setReservationId(this.reservationId);
      clone.setDebut(this.debut);
      clone.setFin(this.fin);
      clone.setUtilisateur(this.utilisateur);
      clone.setEchantillons(this.echantillons);
      clone.setProdDerives(this.prodDerives);

      return clone;
   }
}
