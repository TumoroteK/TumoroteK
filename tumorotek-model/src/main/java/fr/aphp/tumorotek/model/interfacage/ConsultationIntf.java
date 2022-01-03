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

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Objet persistant mappant la table CONSULTATION.
 * Enregistrement des consultations de dossiers externes par un utilisateur
 * par souci de tracabilite.
 *
 * Date: 22/02/2016
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Entity
@Table(name = "CONSULTATION_INTF")
//@NamedQueries(
//   value = {
//      @NamedQuery(name = "ConsultationIntf.findByUtilisateurInDates",
//         query = "SELECT c FROM ConsultationIntf c "
//            + "WHERE c.utilisateur = ?1 and c.date >= ?2 AND c.date <= ?3 order by c.date"),
//      @NamedQuery(name = "ConsultationIntf.findByEmetteurInDates",
//         query = "SELECT c FROM ConsultationIntf c "
//            + "WHERE c.emetteurIdent like ?1 and c.date >= ?2 AND c.date <= ?3 order by c.date"),
//      @NamedQuery(name = "ConsultationIntf.findByUtilisateurEmetteurInDates", query = "SELECT c FROM ConsultationIntf c "
//         + "WHERE c.utilisateur = ?1 and c.emetteurIdent like ?2 and c.date >= ?3 AND c.date <= ?4 order by c.date"),})
public class ConsultationIntf implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer consultationIntfId;
   private Calendar date;
   private String identification;
   private Utilisateur utilisateur;
   private String emetteurIdent;

   /** Constructeur par défaut. */
   public ConsultationIntf(){}

   @Override
   public String toString(){
      if(this.emetteurIdent != null && this.utilisateur != null){
         return "{" + this.emetteurIdent + this.identification + ": " + this.utilisateur.getLogin() + " }";
      }
      return "{Empty Consultation}";
   }

   @Id
   @Column(name = "CONSULTATION_INTF_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getConsultationIntfId(){
      return this.consultationIntfId;
   }

   public void setConsultationIntfId(final Integer id){
      this.consultationIntfId = id;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "DATE_", nullable = false)
   public Calendar getDate(){
      if(date != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(date.getTime());
         return cal;
      }
      return null;
   }

   public void setDate(final Calendar d){
      if(d != null){
         this.date = Calendar.getInstance();
         this.date.setTime(d.getTime());
      }else{
         this.date = null;
      }
   }

   @Column(name = "IDENTIFICATION", nullable = false, length = 100)
   public String getIdentification(){
      return this.identification;
   }

   public void setIdentification(final String _s){
      this.identification = _s;
   }

   @Column(name = "EMETTEUR_IDENT", nullable = false, length = 100)
   public String getEmetteurIdent(){
      return this.emetteurIdent;
   }

   public void setEmetteurIdent(final String _id){
      this.emetteurIdent = _id;
   }

   @ManyToOne
   @JoinColumn(name = "UTILISATEUR_ID", nullable = true)
   public Utilisateur getUtilisateur(){
      return this.utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   /**
    * 2 consultations sont considerees egales si elles portent 
    * sur la même identification
    * et si elles sont effectue par le meme utilisateur 
    * sur le meme objet a la meme date.
    * @param obj est la consultation à tester.
    * @return true si les consultations sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ConsultationIntf test = (ConsultationIntf) obj;
      return ((this.identification == test.identification
         || (this.identification != null && this.identification.equals(test.identification)))
         && (this.utilisateur == test.utilisateur || (this.utilisateur != null && this.utilisateur.equals(test.utilisateur)))
         && (this.emetteurIdent == test.emetteurIdent
            || (this.emetteurIdent != null && this.emetteurIdent.equals(test.emetteurIdent)))
         && (this.date == test.date || (this.date != null && this.date.equals(test.date))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom et la
    * description.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashIdentification = 0;
      int hashUtilisateur = 0;
      int hashEmetteur = 0;
      int hashDate = 0;

      if(this.identification != null){
         hashIdentification = this.identification.hashCode();
      }
      if(this.utilisateur != null){
         hashUtilisateur = this.utilisateur.hashCode();
      }
      if(this.emetteurIdent != null){
         hashEmetteur = this.emetteurIdent.hashCode();
      }
      if(this.date != null){
         hashDate = this.date.hashCode();
      }

      hash = 31 * hash + hashIdentification;
      hash = 31 * hash + hashUtilisateur;
      hash = 31 * hash + hashEmetteur;
      hash = 31 * hash + hashDate;

      return hash;

   }
}
