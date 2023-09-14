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

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 *
 * Embedded Id pour la table PATIENT_MEDECIN.
 * Classe créée le 29/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class PatientMedecinPK implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Collaborateur collaborateur;

   private Patient patient;

   /** Constructeur par défaut. */
   public PatientMedecinPK(){}

   public PatientMedecinPK(final Collaborateur c, final Patient p){
      this.collaborateur = c;
      this.patient = p;
   }

   @ManyToOne(targetEntity = Collaborateur.class)
   public Collaborateur getCollaborateur(){
      return this.collaborateur;
   }

   public void setCollaborateur(final Collaborateur c){
      this.collaborateur = c;
   }

   @ManyToOne(targetEntity = Patient.class)
   public Patient getPatient(){
      return patient;
   }

   public void setPatient(final Patient p){
      this.patient = p;
   }

   /**
    * 2 PKs sont considérés comme égales si elles sont composees
    * des mêmes clés.
    * @param obj est la PK à tester.
    * @return true si les PK sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final PatientMedecinPK test = (PatientMedecinPK) obj;
      return (((this.collaborateur != null && (this.collaborateur.equals(test.collaborateur))
         || this.collaborateur == test.collaborateur)
         && ((this.patient != null && this.patient.equals(test.patient)) || this.patient == test.patient)));
   }

   /**
    * Le hashcode est calculé sur les clés.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      //int hash = 7;
      int hashCollaborateur = 0;
      int hashPatient = 0;

      if(this.collaborateur != null){
         hashCollaborateur = this.collaborateur.hashCode();
      }
      if(this.patient != null){
         hashPatient = this.patient.hashCode();
      }

      //hash = 7 * hash + hashPatient1;
      //hash = 7 * hash + hashPatient2;

      return hashCollaborateur + hashPatient;
   }

}
