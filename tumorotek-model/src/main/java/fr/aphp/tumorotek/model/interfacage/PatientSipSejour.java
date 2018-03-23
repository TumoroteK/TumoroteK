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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table PATIENT_SIP_SEJOUR enregistrant les numéros
 * de visite/sejour envoyés pour un PATIENT temporaire.
 * Classe créée le 14/11/12.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.9
 *
 */
@Entity
@Table(name = "PATIENT_SIP_SEJOUR")
public class PatientSipSejour
{

   private Integer patientSipSejourId;
   private String numero;
   private Date dateSejour;
   private PatientSip patientSip;

   /** Constructeur par défaut. */
   public PatientSipSejour(){}

   @Id
   @Column(name = "PATIENT_SIP_SEJOUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getPatientSipSejourId(){
      return this.patientSipSejourId;
   }

   public void setPatientSipSejourId(final Integer id){
      this.patientSipSejourId = id;
   }

   @Column(name = "NUMERO", nullable = false, length = 20)
   public String getNumero(){
      return this.numero;
   }

   public void setNumero(final String n){
      this.numero = n;
   }

   @Column(name = "DATE_SEJOUR", nullable = true)
   public Date getDateSejour(){
      return dateSejour;
   }

   public void setDateSejour(final Date date){
      this.dateSejour = date;
   }

   @ManyToOne(cascade = {CascadeType.REFRESH})
   @JoinColumn(name = "PATIENT_SIP_ID", nullable = false)
   public PatientSip getPatientSip(){
      return this.patientSip;
   }

   public void setPatientSip(final PatientSip p){
      this.patientSip = p;
   }

   @Override
   public PatientSipSejour clone(){
      final PatientSipSejour clone = new PatientSipSejour();
      clone.setPatientSipSejourId(getPatientSipSejourId());
      clone.setNumero(getNumero());
      clone.setDateSejour(getDateSejour());
      clone.setPatientSip(getPatientSip());
      return clone;
   }

   /**
    * 2 sejour sont considérés comme égaux s'ils ont les mêmes numéros.
    * @param obj est le patient sip à tester.
    * @return true si les patients sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final PatientSipSejour test = (PatientSipSejour) obj;
      return (this.getNumero() != null && getNumero().equals(test.getNumero())) || this.getNumero() == test.getNumero();
   }

   /**
    * Le hashcode est calculé sur l'attribut numero.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNumero = 0;

      if(getNumero() != null){
         hashNumero = this.getNumero().hashCode();
      }

      hash = 7 * hash + hashNumero;

      return hash;
   }

   @Override
   public String toString(){
      if(this.getNumero() != null){
         return "{" + this.getNumero() + "}";
      }else{
         return "{Empty PatientSipSejour}";
      }
   }
}
