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
package fr.aphp.tumorotek.model.coeur.patient;

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

import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 *
 * Objet persistant mappant la table PATIENT_MEDECIN.
 * Classe créée le 14/09/09.
 * Modifiée par Mathieu pour créer la clef composite
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/*%20
 * 		hibernate-annotations-the-many-to-many-association-with-composite-key/
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "PATIENT_MEDECIN")
/*@NamedQueries(value = {@NamedQuery(name = "PatientMedecin.findByCollaborateur",
query = "SELECT p FROM PatientMedecin p "
	+ "WHERE p.collaborateur = ?1")})*/
@AssociationOverrides({
   @AssociationOverride(name = "pk.collaborateur",
      joinColumns = @JoinColumn(name = "COLLABORATEUR_ID", referencedColumnName = "COLLABORATEUR_ID")),
   @AssociationOverride(name = "pk.patient",
      joinColumns = @JoinColumn(name = "PATIENT_ID", referencedColumnName = "PATIENT_ID"))})
//@AttributeOverride(column =
//	@Column(name = "ORDRE"), name = "ordre")
public class PatientMedecin implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer ordre;
   private PatientMedecinPK pk = new PatientMedecinPK();

   /** Constructeur par défaut. */
   public PatientMedecin(){}

   @Override
   public String toString(){
      if(this.getPatient() != null && this.getCollaborateur() != null){
         return "{" + this.getPatient() + " - " + this.getCollaborateur() + "}";
      }
      return "{Empty PatientMedecin}";
   }

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "collaborateur", column = @Column(name = "COLLABORATEUR_ID")),
      @AttributeOverride(name = "patient", column = @Column(name = "PATIENT_ID"))})
   public PatientMedecinPK getPk(){
      return pk;
   }

   public void setPk(final PatientMedecinPK pmk){
      this.pk = pmk;
   }

   //@JoinColumn(name = "ORDRE", nullable = false)
   @Column(name = "ORDRE", nullable = false)
   public Integer getOrdre(){
      return this.ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   @Transient
   public Collaborateur getCollaborateur(){
      return this.pk.getCollaborateur();
   }

   public void setCollaborateur(final Collaborateur coll){
      this.pk.setCollaborateur(coll);
   }

   @Transient
   public Patient getPatient(){
      return this.pk.getPatient();
   }

   public void setPatient(final Patient pat){
      this.pk.setPatient(pat);
   }

   /**
    * 2 liens sont considérés comme égaux s'ils ont la même pk.
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
      final PatientMedecin test = (PatientMedecin) obj;
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
}
