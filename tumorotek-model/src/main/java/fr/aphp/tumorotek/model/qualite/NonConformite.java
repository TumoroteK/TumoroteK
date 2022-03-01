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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;

/**
 *
 * Objet persistant mappant la table NON_CONFORMITE.
 * Classe créée le 08/11/11.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "NON_CONFORMITE")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "NON_CONFORMITE_ID"))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
//@NamedQueries(
//   value = {@NamedQuery(name = "NonConformite.findByPfOrder", query = "FROM NonConformite n WHERE n.plateforme=?1 ORDER BY n.nom"),
//      @NamedQuery(name = "NonConformite.findByOrder", query = "FROM NonConformite n ORDER BY n.nom"),
//      @NamedQuery(name = "NonConformite.findByTypeAndPf",
//         query = "SELECT n FROM NonConformite n " + "WHERE n.conformiteType = ?1 " + "AND n.plateforme = ?2 " + "ORDER BY n.nom"),
//      @NamedQuery(name = "NonConformite.findByTypePfAndNom",
//         query = "SELECT n FROM NonConformite n " + "WHERE n.conformiteType = ?1 " + "AND n.plateforme = ?2 "
//            + "AND n.nom like ?3 " + "ORDER BY n.nom"),
//      @NamedQuery(name = "NonConformite.findByExcludedId", query = "SELECT n FROM NonConformite n " + "WHERE n.id != ?1")})
public class NonConformite extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = -6139596888096490682L;

   private ConformiteType conformiteType;
   private Set<ObjetNonConforme> objetNonConformes = new HashSet<>();

   public NonConformite(){
      super();
   }

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getNonConformiteId(){
      return this.getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @param id
    */
   @Deprecated
   public void setNonConformiteId(final Integer id){
      this.setId(id);
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "CONFORMITE_TYPE_ID", nullable = false)
   public ConformiteType getConformiteType(){
      return conformiteType;
   }

   public void setConformiteType(final ConformiteType c){
      this.conformiteType = c;
   }


   @OneToMany(mappedBy = "nonConformite")
   public Set<ObjetNonConforme> getObjetNonConformes(){
      return objetNonConformes;
   }

   public void setObjetNonConformes(final Set<ObjetNonConforme> o){
      this.objetNonConformes = o;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final NonConformite test = (NonConformite) obj;
      return ((this.getNom() == test.getNom() || (this.getNom() != null && this.getNom().equals(test.getNom())))
         && (this.conformiteType == test.conformiteType
            || (this.conformiteType != null && this.conformiteType.equals(test.conformiteType)))
         && (this.getPlateforme() == test.getPlateforme() || (this.getPlateforme() != null && this.getPlateforme().equals(test.getPlateforme()))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashType = 0;
      int hashPlateforme = 0;

      if(this.getNom() != null){
         hashNom = this.getNom().hashCode();
      }
      if(this.conformiteType != null){
         hashType = this.conformiteType.hashCode();
      }
      if(this.getPlateforme() != null){
         hashPlateforme = this.getPlateforme().hashCode();
      }

      hash = 7 * hash + hashNom;
      hash = 8 * hash + hashType;
      hash = 8 * hash + hashPlateforme;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + ", " + conformiteType.getConformiteType() + "(ConformiteType), " + getPlateforme().getNom()
            + "(Plateforme)}";
      }
      return "{Empty NonConformite}";
   }

   @Override
   public NonConformite clone(){
      final NonConformite clone = new NonConformite();

      clone.setId(this.getId());
      clone.setNom(this.getNom());
      clone.setConformiteType(this.conformiteType);
      clone.setPlateforme(this.getPlateforme());

      return clone;
   }

}
