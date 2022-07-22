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
package fr.aphp.tumorotek.model.qualite;

import java.util.HashSet;
import java.util.Set;

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

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table CONFORMITE_TYPE.
 * Classe créée le 08/11/11.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "CONFORMITE_TYPE")
@NamedQueries(value = {@NamedQuery(name = "ConformiteType.findByEntiteAndType",
   query = "SELECT c FROM ConformiteType c " + "WHERE c.conformiteType like ?1 " + "AND c.entite = ?2")})
public class ConformiteType implements java.io.Serializable
{

   private static final long serialVersionUID = -8531615889506970231L;

   private Integer conformiteTypeId;

   private String conformiteType;

   private Entite entite;

   private Set<NonConformite> nonConformites = new HashSet<>();

   public ConformiteType(){
      super();
   }

   @Id
   @Column(name = "CONFORMITE_TYPE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getConformiteTypeId(){
      return conformiteTypeId;
   }

   public void setConformiteTypeId(final Integer id){
      this.conformiteTypeId = id;
   }

   @Column(name = "CONFORMITE_TYPE", nullable = false, length = 50)
   public String getConformiteType(){
      return conformiteType;
   }

   public void setConformiteType(final String cType){
      this.conformiteType = cType;
   }

   @OneToMany(mappedBy = "conformiteType")
   public Set<NonConformite> getNonConformites(){
      return nonConformites;
   }

   public void setNonConformites(final Set<NonConformite> n){
      this.nonConformites = n;
   }

   @ManyToOne()
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite entite){
      this.entite = entite;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ConformiteType test = (ConformiteType) obj;
      return ((this.conformiteType == test.conformiteType
         || (this.conformiteType != null && this.conformiteType.equals(test.conformiteType)))
         && (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashType = 0;
      int hashEntite = 0;

      if(this.conformiteType != null){
         hashType = this.conformiteType.hashCode();
      }
      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }

      hash = 7 * hash + hashType;
      hash = 7 * hash + hashEntite;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.conformiteType != null){
         return "{" + this.conformiteType + "}";
      }
      return "{Empty ConformiteType}";
   }

}
