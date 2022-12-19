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
package fr.aphp.tumorotek.model.coeur.patient.gatsbi;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Objet persistant mappant la table PATIENT_IDENTIFIANT.
 * Classe créée le 05/09/2022.
 * Modifiée par Mathieu pour créer la clef composite
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/*%20
 * 		hibernate-annotations-the-many-to-many-association-with-composite-key/
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
@Entity
@Table(name = "PATIENT_IDENTIFIANT")
@AssociationOverrides({
   @AssociationOverride(name = "pk.banque",
      joinColumns = @JoinColumn(name = "BANQUE_ID", referencedColumnName = "BANQUE_ID")),
   @AssociationOverride(name = "pk.patient",
      joinColumns = @JoinColumn(name = "PATIENT_ID", referencedColumnName = "PATIENT_ID"))})
public class PatientIdentifiant implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String identifiant;

   private PatientIdentifiantPK pk = new PatientIdentifiantPK();

   public PatientIdentifiant(){}
   
   public PatientIdentifiant(Patient p, Banque b){
      pk.setPatient(p);
      pk.setBanque(b);
   }
   
   public PatientIdentifiant(Patient p, Banque b, String _id){
      pk.setPatient(p);
      pk.setBanque(b);
      this.identifiant = _id;
   }

   @Override
   public String toString(){
      if(this.getPatient() != null && this.getBanque() != null){
         return "{" + this.getPatient() + " - " + this.getBanque() + "}";
      }
      return "{Empty PatientIdentifiant}";
   }

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "collaborateur", column = @Column(name = "COLLABORATEUR_ID")),
      @AttributeOverride(name = "patient", column = @Column(name = "PATIENT_ID"))})
   public PatientIdentifiantPK getPk(){
      return pk;
   }

   public void setPk(final PatientIdentifiantPK pmk){
      this.pk = pmk;
   }

   @Column(name = "IDENTIFIANT", nullable = false)
   public String getIdentifiant(){
      return this.identifiant;
   }

   public void setIdentifiant(final String _i){
      this.identifiant = _i;
   }

   @Transient
   public Banque getBanque(){
      return this.pk.getBanque();
   }

   public void setBanque(final Banque _b){
      this.pk.setBanque(_b);
   }

   @Transient
   public Patient getPatient(){
      return this.pk.getPatient();
   }

   public void setPatient(final Patient pat){
      this.pk.setPatient(pat);
   }

   /**
    * 2 identifiants sont considérés comme égaux s'ils ont la même pk.
    * @param obj est le lien à tester.
    * @return true si les liens sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final PatientIdentifiant test = (PatientIdentifiant) obj;
      return (this.pk != null && (this.pk == test.pk || this.pk.equals(test.pk)));
   }

   /**
    * Le hashcode est calculé sur la pk.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashPk = 0;

      if(this.pk != null){
         hashPk = this.pk.hashCode();
      }

      hash = 7 * hash + hashPk;

      return hash;
   }

   @Transient
   public boolean isEmpty(){
      return identifiant == null;
   }
}
