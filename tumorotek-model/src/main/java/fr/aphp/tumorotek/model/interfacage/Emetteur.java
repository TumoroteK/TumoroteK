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
package fr.aphp.tumorotek.model.interfacage;

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

/**
 *
 * Objet persistant mappant la table LOGICIEL.
 * Classe créée le 01/10/11.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "EMETTEUR")
@NamedQueries(value = {@NamedQuery(name = "Emetteur.findByOrder", query = "SELECT e FROM Emetteur e ORDER BY e.identification"),
   @NamedQuery(name = "Emetteur.findByLogiciel",
      query = "SELECT e FROM Emetteur e " + "WHERE e.logiciel = ?1 " + "ORDER BY e.identification"),
   @NamedQuery(name = "Emetteur.findByLogicielAndIdentification",
      query = "SELECT e FROM Emetteur e " + "WHERE e.logiciel = ?1 " + "AND e.identification like ?2 "
         + "ORDER BY e.identification"),
   @NamedQuery(name = "Emetteur.findByIdInList",
      query = "SELECT e FROM Emetteur e " + "WHERE e.emetteurId in (?1) " + "ORDER BY e.identification"),
   @NamedQuery(name = "Emetteur.findByIdentificationAndService",
      query = "SELECT e FROM Emetteur e " + "WHERE e.identification = ?1 " + "AND e.service = ?2")})
public class Emetteur implements java.io.Serializable
{

   private static final long serialVersionUID = -4548757508672995006L;

   private Integer emetteurId;
   private String identification;
   private String service;
   private String observations;
   private Logiciel logiciel;

   private Set<DossierExterne> dossierExternes = new HashSet<>();

   public Emetteur(){
      super();
   }

   @Id
   @Column(name = "EMETTEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getEmetteurId(){
      return emetteurId;
   }

   public void setEmetteurId(final Integer e){
      this.emetteurId = e;
   }

   @Column(name = "IDENTIFICATION", nullable = false, length = 50)
   public String getIdentification(){
      return identification;
   }

   public void setIdentification(final String i){
      this.identification = i;
   }

   @Column(name = "SERVICE", nullable = true, length = 50)
   public String getService(){
      return service;
   }

   public void setService(final String s){
      this.service = s;
   }

   @Column(name = "OBSERVATIONS", nullable = true)
   public String getObservations(){
      return observations;
   }

   public void setObservations(final String o){
      this.observations = o;
   }

   @ManyToOne()
   @JoinColumn(name = "LOGICIEL_ID", nullable = false)
   public Logiciel getLogiciel(){
      return logiciel;
   }

   public void setLogiciel(final Logiciel l){
      this.logiciel = l;
   }

   @OneToMany(mappedBy = "emetteur", cascade = CascadeType.REMOVE)
   public Set<DossierExterne> getDossierExternes(){
      return dossierExternes;
   }

   public void setDossierExternes(final Set<DossierExterne> d){
      this.dossierExternes = d;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Emetteur test = (Emetteur) obj;
      return ((this.identification == test.identification
         || (this.identification != null && this.identification.equals(test.identification)))
         && (this.logiciel == test.logiciel || (this.logiciel != null && this.logiciel.equals(test.logiciel))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashId = 0;
      int hashLog = 0;

      if(this.identification != null){
         hashId = this.identification.hashCode();
      }
      if(this.logiciel != null){
         hashLog = this.logiciel.hashCode();
      }

      hash = 7 * hash + hashId;
      hash = 31 * hash + hashLog;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.identification != null){
         return "{" + this.identification + ", " + logiciel.getNom() + "(Logiciel)}";
      }else{
         return "{Empty Emetteur}";
      }
   }

}
