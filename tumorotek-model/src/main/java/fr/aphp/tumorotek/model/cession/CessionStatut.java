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
package fr.aphp.tumorotek.model.cession;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table CESSION_STATUT.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "CESSION_STATUT")
@NamedQueries(
   value = {@NamedQuery(name = "CessionStatut.findByStatut", query = "SELECT c FROM CessionStatut c " + "WHERE c.statut like ?1"),
      @NamedQuery(name = "CessionStatut.findByOrder", query = "SELECT c FROM CessionStatut c " + "ORDER BY c.statut")})
public class CessionStatut implements Serializable
{

   private static final long serialVersionUID = -1503913295196145334L;

   private Integer cessionStatutId;
   private String statut;

   private Set<Cession> cessions = new HashSet<>();

   /** Constructeur par défaut. */
   public CessionStatut(){}

   @Id
   @Column(name = "CESSION_STATUT_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCessionStatutId(){
      return this.cessionStatutId;
   }

   public void setCessionStatutId(final Integer id){
      this.cessionStatutId = id;
   }

   @Column(name = "STATUT", nullable = false, length = 15)
   public String getStatut(){
      return this.statut;
   }

   public void setStatut(final String s){
      this.statut = s;
   }

   @OneToMany(mappedBy = "cessionStatut")
   public Set<Cession> getCessions(){
      return this.cessions;
   }

   public void setCessions(final Set<Cession> cess){
      this.cessions = cess;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même statut.
    * @param obj est l'objet à tester.
    * @return true si les objets sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final CessionStatut test = (CessionStatut) obj;
      if(this.statut == null){
         return (test.statut == null);
      }
      return (this.statut.equals(test.statut));
   }

   /**
    * Le hashcode est calculé sur l'attribut statut.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashStatut = 0;

      if(this.statut != null){
         hashStatut = this.statut.hashCode();
      }

      hash = 31 * hash + hashStatut;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.statut != null){
         return "{" + this.statut + "}";
      }
      return "{Empty CessionStatut}";
   }
}
