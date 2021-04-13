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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table LIEN_FAMILIAL.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "LIEN_FAMILIAL")
@NamedQueries(value = {@NamedQuery(name = "LienFamilial.findByNom", query = "SELECT l FROM LienFamilial l WHERE l.nom like ?1"),
   //		@NamedQuery(name = "LienFamilial.findByReciproque", 
   //			query = "SELECT l FROM LienFamilial l WHERE l.reciproque = ?1"),
   //		@NamedQuery(name = "LienFamilial.findByAscendant", 
   //			query = "SELECT l FROM LienFamilial l WHERE l.ascendant = ?1")
   @NamedQuery(name = "LienFamilial.findByExcludedId", query = "SELECT l FROM LienFamilial l " + "WHERE l.lienFamilialId != ?1")})
public class LienFamilial implements Serializable
{

   private static final long serialVersionUID = 1819985582518503182L;

   private Integer lienFamilialId;
   private String nom;
   private LienFamilial reciproque;
   private Boolean ascendant;

   private Set<PatientLien> patientLiens = new HashSet<>();
   //private Set<PatientLien> patientLiens2 = new HashSet<PatientLien>();

   /** Constructeur par défaut. */
   public LienFamilial(){}

   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }else{
         return "{Empty LienFamilial}";
      }
   }

   @Id
   @Column(name = "LIEN_FAMILIAL_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getLienFamilialId(){
      return this.lienFamilialId;
   }

   public void setLienFamilialId(final Integer id){
      this.lienFamilialId = id;
   }

   @Column(name = "NOM", nullable = false, length = 20)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @OneToOne(cascade = {CascadeType.ALL})
   @JoinColumn(name = "RECIPROQUE_ID", nullable = true)
   public LienFamilial getReciproque(){
      return this.reciproque;
   }

   public void setReciproque(final LienFamilial rec){
      this.reciproque = rec;
   }

   @Column(name = "ASCENDANT", nullable = true)
   public Boolean getAscendant(){
      return this.ascendant;
   }

   public void setAscendant(final Boolean asc){
      this.ascendant = asc;
   }

   @OneToMany(mappedBy = "lienFamilial")
   public Set<PatientLien> getPatientLiens(){
      return this.patientLiens;
   }

   public void setPatientLiens(final Set<PatientLien> patientLs){
      this.patientLiens = patientLs;
   }

   //	@OneToMany(mappedBy = "lien2")
   //	public Set<PatientLien> getPatientLiens2() {
   //		return this.patientLiens2;
   //	}
   //
   //	public void setPatientLiens2(Set<PatientLien> patientLs2) {
   //		this.patientLiens2 = patientLs2;
   //	}

   /**
    * 2 liens sont considérés comme égaux s'ils ont le même nom.
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
      final LienFamilial test = (LienFamilial) obj;
      return ((this.nom != null && this.nom.equals(test.nom)) || this.nom == test.nom);
   }

   /**
    * Le hashcode est calculé sur l'attribut nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }

      hash = 31 * hash + hashNom;

      return hash;

   }
}
