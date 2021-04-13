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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * Objet persistant mappant la table PATIENT_LIEN.
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
@Table(name = "PATIENT_LIEN")
@AssociationOverrides({
   @AssociationOverride(name = "pk.patient1",
      joinColumns = @JoinColumn(name = "PATIENT1_ID", referencedColumnName = "PATIENT_ID")),
   @AssociationOverride(name = "pk.patient2",
      joinColumns = @JoinColumn(name = "PATIENT2_ID", referencedColumnName = "PATIENT_ID"))})
@AttributeOverride(column = @Column(name = "LIEN_FAMILIAL_ID"), name = "lienFamilial")
public class PatientLien implements Serializable
{

   private static final long serialVersionUID = 1L;

   private LienFamilial lienFamilial;
   private PatientLienPK pk = new PatientLienPK();

   /** Constructeur par défaut. */
   public PatientLien(){}

   @Override
   public String toString(){
      if(this.lienFamilial != null && this.getPatient1() != null && this.getPatient2() != null){
         return "{" + this.getPatient1() + " - " + this.lienFamilial + " - " + this.getPatient2() + "}";
      }
      return "{Empty PatientLien}";
   }

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "patient1", column = @Column(name = "PATIENT1_ID")),
      @AttributeOverride(name = "patient2", column = @Column(name = "PATIENT2_ID"))})
   public PatientLienPK getPk(){
      return pk;
   }

   public void setPk(final PatientLienPK ppk){
      this.pk = ppk;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "LIEN_FAMILIAL_ID", nullable = false)
   public LienFamilial getLienFamilial(){
      return this.lienFamilial;
   }

   public void setLienFamilial(final LienFamilial l1){
      this.lienFamilial = l1;
   }

   @Transient
   public Patient getPatient1(){
      return this.getPk().getPatient1();
   }

   public void setPatient1(final Patient pat1){
      this.getPk().setPatient1(pat1);
   }

   @Transient
   public Patient getPatient2(){
      return this.getPk().getPatient2();
   }

   public void setPatient2(final Patient pat2){
      this.getPk().setPatient2(pat2);
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
      final PatientLien test = (PatientLien) obj;
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
