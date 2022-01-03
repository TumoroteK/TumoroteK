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
package fr.aphp.tumorotek.model.stockage;

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

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;

/**
 *
 * Objet persistant mappant la table CONTENEUR_TYPE.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "CONTENEUR_TYPE_ID")),
   @AttributeOverride(name = "nom", column = @Column(name = "TYPE", nullable = false, length = 200))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@Table(name = "CONTENEUR_TYPE")
//@NamedQueries(value = {@NamedQuery(name = "ConteneurType.findByType", query = "SELECT c FROM ConteneurType c WHERE c.nom = ?1"),
//   @NamedQuery(name = "ConteneurType.findByExcludedId", query = "SELECT c FROM ConteneurType c " + "WHERE c.id != ?1"),
//   @NamedQuery(name = "ConteneurType.findByPfOrder",
//      query = "SELECT c FROM ConteneurType c " + "WHERE c.plateforme = ?1 ORDER BY c.nom"),
//   @NamedQuery(name = "ConteneurType.findByOrder",
//   query = "SELECT c FROM ConteneurType c ORDER BY c.nom")})
public class ConteneurType extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = 1968660248660817618L;

   private Set<Conteneur> conteneurs = new HashSet<>();

   public ConteneurType(){}

   /**
    * @deprecated Utiliser {@link #getId()}
    * @return
    */
   @Deprecated
   @Transient
   public Integer getConteneurTypeId(){
      return this.getId();
   }

   /**
    * @deprecated Utiliser {@link #setId(Integer)}
    * @return
    */
   @Deprecated
   public void setConteneurTypeId(final Integer id){
      this.setId(id);
   }

   /**
    * @deprecated Utiliser {@link #getNom()}
    */
   @Deprecated
   @Transient
   public String getType(){
      return this.getNom();
   }

   /**
    * @deprecated Utiliser {@link #setNom()}
    */
   @Deprecated
   public void setType(final String t){
      this.setNom(t);
   }

   @OneToMany(mappedBy = "conteneurType")
   public Set<Conteneur> getConteneurs(){
      return this.conteneurs;
   }

   public void setConteneurs(final Set<Conteneur> conts){
      this.conteneurs = conts;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même type et
    * la même plateforme.
    * @param obj est l'objet à tester.
    * @return true si les objets sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ConteneurType test = (ConteneurType) obj;
      return ((this.getNom() == test.getNom() || (this.getNom() != null && this.getNom().equals(test.getNom())))
         && (this.getPlateforme() == test.getPlateforme()
            || (this.getPlateforme() != null && this.getPlateforme().equals(test.getPlateforme()))));
   }

   /**
    * Le hashcode est calculé sur les attributs type et plateforme.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashType = 0;
      int hashPF = 0;

      if(this.getNom() != null){
         hashType = this.getNom().hashCode();
      }
      if(this.getPlateforme() != null){
         hashPF = this.getPlateforme().hashCode();
      }

      hash = 31 * hash + hashType;
      hash = 31 * hash + hashPF;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }
      return "{Empty ConteneurType}";
   }

}
