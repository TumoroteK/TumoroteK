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
package fr.aphp.tumorotek.model.stockage;

import java.io.Serializable;
import java.util.Calendar;
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

import fr.aphp.tumorotek.model.cession.Retour;

/**
 *
 * Objet persistant mappant la table INCIDENT.
 * @since 2.0.10 ajout de references vers ENCEINTE et TERMINALE
 * Classe créée le 11/09/09, modifiée le 11/10/2013
 *
 * @author Maxime Gousseau
 * @version 2.0.10
 *
 */
@Entity
@Table(name = "INCIDENT")
@NamedQueries(value = {@NamedQuery(name = "Incident.findByNom", query = "SELECT i FROM Incident i WHERE i.nom = ?1"),
   @NamedQuery(name = "Incident.findByDate", query = "SELECT i FROM Incident i WHERE i.date = ?1"),
   @NamedQuery(name = "Incident.findByDescription", query = "SELECT i FROM Incident i WHERE i.description = ?1"),
   @NamedQuery(name = "Incident.findByConteneurOrderByDate",
      query = "SELECT i FROM Incident i " + "WHERE i.conteneur = ?1 " + "ORDER BY i.date"),
   @NamedQuery(name = "Incident.findByEnceinte",
      query = "SELECT i FROM Incident i " + "WHERE i.enceinte = ?1 " + "ORDER BY i.date"),
   @NamedQuery(name = "Incident.findByTerminale",
      query = "SELECT i FROM Incident i " + "WHERE i.terminale = ?1 " + "ORDER BY i.date"),
   @NamedQuery(name = "Incident.findDoublon", query = "SELECT i FROM Incident i WHERE i.nom = ?1" + " AND i.date = ?2"),
   @NamedQuery(name = "Incident.findByExcludedIdAndConteneur",
      query = "SELECT i FROM Incident i " + "WHERE i.incidentId != ?1 " + "AND i.conteneur = ?2"),
   @NamedQuery(name = "Incident.findByExcludedIdAndEnceinte",
      query = "SELECT i FROM Incident i " + "WHERE i.incidentId != ?1 " + "AND i.enceinte = ?2"),
   @NamedQuery(name = "Incident.findByExcludedIdAndTerminale",
      query = "SELECT i FROM Incident i " + "WHERE i.incidentId != ?1 " + "AND i.terminale = ?2"),})
public class Incident implements Serializable
{

   private static final long serialVersionUID = -4770356034470081995L;

   private Integer incidentId;
   private String nom;
   private Date date;
   private String description;

   private Conteneur conteneur;
   private Enceinte enceinte;
   private Terminale terminale;

   private Set<Retour> retours = new HashSet<>();

   /** Constructeur par défaut. */
   public Incident(){}

   @Id
   @Column(name = "INCIDENT_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getIncidentId(){
      return this.incidentId;
   }

   public void setIncidentId(final Integer id){
      this.incidentId = id;
   }

   @Column(name = "NOM", nullable = false)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "DATE_", nullable = false)
   public Date getDate(){
      if(date != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(date);
         return cal.getTime();
      }else{
         return null;
      }
   }

   public void setDate(final Date d){
      if(d != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(d);
         this.date = cal.getTime();
      }else{
         this.date = null;
      }
   }

   @Column(name = "DESCRIPTION", nullable = true)
   //Lob
   public String getDescription(){
      return this.description;
   }

   public void setDescription(final String desc){
      this.description = desc;
   }

   @ManyToOne
   @JoinColumn(name = "CONTENEUR_ID", nullable = true)
   public Conteneur getConteneur(){
      return this.conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   @ManyToOne
   @JoinColumn(name = "ENCEINTE_ID", nullable = true)
   public Enceinte getEnceinte(){
      return enceinte;
   }

   public void setEnceinte(final Enceinte enceinte){
      this.enceinte = enceinte;
   }

   @ManyToOne
   @JoinColumn(name = "TERMINALE_ID", nullable = true)
   public Terminale getTerminale(){
      return terminale;
   }

   public void setTerminale(final Terminale terminale){
      this.terminale = terminale;
   }

   @OneToMany(mappedBy = "incident", cascade = {CascadeType.REMOVE})
   public Set<Retour> getRetours(){
      return this.retours;
   }

   public void setRetours(final Set<Retour> rets){
      this.retours = rets;
   }

   /**
    * 2 incidents sont considérés comme égaux s'ils ont l même
    * nom et la même date.
    * @param obj est l'incident à tester.
    * @return true si les incidents sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Incident test = (Incident) obj;
      return ((this.conteneur == test.conteneur || (this.conteneur != null && this.conteneur.equals(test.conteneur)))
         && (this.enceinte == test.enceinte || (this.enceinte != null && this.enceinte.equals(test.enceinte)))
         && (this.terminale == test.terminale || (this.terminale != null && this.terminale.equals(test.terminale)))
         && (this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.date == test.date || (this.date != null && this.date.equals(test.date))));
   }

   /**
    * Le hashcode est calculé sur les attributs nom et date.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashConteneur = 0;
      int hashEnceinte = 0;
      int hashTerminale = 0;
      int hashNom = 0;
      int hashDate = 0;

      if(this.conteneur != null){
         hashConteneur = this.conteneur.hashCode();
      }
      if(this.enceinte != null){
         hashEnceinte = this.enceinte.hashCode();
      }
      if(this.terminale != null){
         hashTerminale = this.terminale.hashCode();
      }
      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.date != null){
         hashDate = this.date.hashCode();
      }

      hash = 31 * hash + hashConteneur;
      hash = 31 * hash + hashEnceinte;
      hash = 31 * hash + hashTerminale;
      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashDate;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null || this.date != null){
         return "{" + this.nom + " " + this.date + "}";
      }else{
         return "{Empty Incident}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone echantillon
    */
   @Override
   public Incident clone(){
      final Incident clone = new Incident();

      clone.setIncidentId(this.incidentId);
      clone.setNom(this.nom);
      clone.setDate(this.date);
      clone.setDescription(this.description);
      clone.setConteneur(this.conteneur);
      clone.setEnceinte(getEnceinte());
      clone.setTerminale(getTerminale());

      return clone;

   }
}
