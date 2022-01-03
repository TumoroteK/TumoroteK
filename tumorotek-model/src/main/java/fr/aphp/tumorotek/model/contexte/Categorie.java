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
package fr.aphp.tumorotek.model.contexte;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractThesaurusObject;

/**
 *
 * Objet persistant mappant la table CATEGORIE.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "CATEGORIE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "CATEGORIE_ID"))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
//@NamedQueries(value = {@NamedQuery(name = "Categorie.findByNom", query = "SELECT c FROM Categorie c WHERE c.nom like ?1"),
//   @NamedQuery(name = "Categorie.findByEtablissementId",
//      query = "SELECT c FROM Categorie c " + "left join c.etablissements e " + "WHERE e.etablissementId = ?1"),
//   @NamedQuery(name = "Categorie.findByExcludedId", query = "SELECT c FROM Categorie c " + "WHERE c.id != ?1"),
//   @NamedQuery(name = "Categorie.findByOrder", query = "SELECT c FROM Categorie c ORDER BY c.nom")})
public class Categorie  extends AbstractThesaurusObject implements Serializable
{

   private static final long serialVersionUID = 86784231547511654L;

   private Set<Etablissement> etablissements = new HashSet<>();

   /**
    * Constructeur par défaut.
    */
   public Categorie(){}
   
   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getCategorieId(){
      return getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @param cId
    */
   @Deprecated
   public void setCategorieId(final Integer cId){
      this.setId(cId);
   }

   @OneToMany(mappedBy = "categorie")
   public Set<Etablissement> getEtablissements(){
      return etablissements;
   }

   public void setEtablissements(final Set<Etablissement> newEtablissements){
      this.etablissements = newEtablissements;
   }

   /**
    * 2 catégories sont considérées comme égales si elles ont le même nom.
    * @param obj est la catégorie à tester.
    * @return true si les catégories sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Categorie test = (Categorie) obj;
      return ((this.getNom() == test.getNom() || (this.getNom() != null && this.getNom().equals(test.getNom()))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;

      if(this.getNom() != null){
         hashNom = this.getNom().hashCode();
      }

      hash = 31 * hash + hashNom;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      return "{" + this.getNom() + "}";
   }

}
