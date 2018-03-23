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
package fr.aphp.tumorotek.model.coeur.prelevement;

import java.io.Serializable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Objet persistant mappant la table NATURE.
 * Classe créée le 14/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "NATURE")
@NamedQueries(value = {@NamedQuery(name = "Nature.findByNature", query = "SELECT n FROM Nature n WHERE n.nature like ?1"),
   @NamedQuery(name = "Nature.findByExcludedId", query = "SELECT n FROM Nature n " + "WHERE n.natureId != ?1"),
   @NamedQuery(name = "Nature.findByOrder", query = "SELECT n FROM Nature n " + "WHERE n.plateforme = ?1 ORDER BY n.nature")})
public class Nature implements Serializable, TKThesaurusObject
{

   private static final long serialVersionUID = 8513939510881684683L;

   private Integer natureId;
   private String nature;
   private Plateforme plateforme;

   private Set<Prelevement> prelevements = new HashSet<>();

   /** Constructeur par défaut. */
   public Nature(){}

   @Override
   public String toString(){
      if(this.nature != null){
         return "{" + this.nature + "}";
      }else{
         return "{Empty Nature}";
      }
   }

   @Id
   @Column(name = "NATURE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getNatureId(){
      return this.natureId;
   }

   public void setNatureId(final Integer id){
      this.natureId = id;
   }

   @Column(name = "NATURE", nullable = false, length = 200)
   public String getNature(){
      return this.nature;
   }

   public void setNature(final String nat){
      this.nature = nat;
   }

   @OneToMany(mappedBy = "nature")
   public Set<Prelevement> getPrelevements(){
      return this.prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevs){
      this.prelevements = prelevs;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   @Override
   public void setPlateforme(final Plateforme pf){
      this.plateforme = pf;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont la même nature.
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
      final Nature test = (Nature) obj;
      return ((this.nature == test.nature || (this.nature != null && this.nature.equals(test.nature)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nature.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNature = 0;
      int hashPF = 0;

      if(this.nature != null){
         hashNature = this.nature.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashNature;
      hash = 31 * hash + hashPF;

      return hash;

   }

   /**
    * Cree un clone de l'objet.
    * @return clone Utilisateur.
    */
   @Override
   public Nature clone(){
      final Nature clone = new Nature();

      clone.setNatureId(this.natureId);
      clone.setNature(this.nature);
      clone.setPlateforme(getPlateforme());

      return clone;

   }

   @Override
   @Transient
   public String getNom(){
      return getNature();
   }

   @Override
   @Transient
   public Integer getId(){
      return getNatureId();
   }
}
