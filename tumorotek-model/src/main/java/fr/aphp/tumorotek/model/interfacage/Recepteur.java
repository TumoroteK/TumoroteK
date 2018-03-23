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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table RECEPTEUR.
 * Classe créée le 08/10/2014.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10.3
 *
 */
@Entity
@Table(name = "RECEPTEUR")
@NamedQueries(value = {
   @NamedQuery(name = "Recepteur.findByLogicielAndIdentification",
      query = "SELECT r FROM Recepteur r " + "WHERE r.logiciel = ?1 " + "AND r.identification like ?2 "
         + "ORDER BY r.identification"),
   @NamedQuery(name = "Recepteur.findByIdInList",
      query = "SELECT r FROM Recepteur r " + "WHERE r.recepteurId in (?1) " + "ORDER BY r.identification")})
public class Recepteur implements java.io.Serializable
{

   private static final long serialVersionUID = -4548757508672995006L;

   private Integer recepteurId;
   private String identification;
   private String observations;
   private Logiciel logiciel;
   private Integer envoiNum;

   public Recepteur(){
      super();
   }

   @Id
   @Column(name = "RECEPTEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getRecepteurId(){
      return recepteurId;
   }

   public void setRecepteurId(final Integer e){
      this.recepteurId = e;
   }

   @Column(name = "IDENTIFICATION", nullable = false, length = 50)
   public String getIdentification(){
      return identification;
   }

   public void setIdentification(final String i){
      this.identification = i;
   }

   @Column(name = "OBSERVATIONS", nullable = true)
   public String getObservations(){
      return observations;
   }

   public void setObservations(final String o){
      this.observations = o;
   }

   @Column(name = "ENVOI_NUM", nullable = true)
   public Integer getEnvoiNum(){
      return envoiNum;
   }

   public void setEnvoiNum(final Integer o){
      this.envoiNum = o;
   }

   @ManyToOne()
   @JoinColumn(name = "LOGICIEL_ID", nullable = false)
   public Logiciel getLogiciel(){
      return logiciel;
   }

   public void setLogiciel(final Logiciel l){
      this.logiciel = l;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Recepteur test = (Recepteur) obj;
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

   @Override
   public String toString(){
      if(this.identification != null){
         return "{" + this.identification + ", " + logiciel.getNom() + "(Logiciel)}";
      }else{
         return "{Empty Recepteur}";
      }
   }

}
