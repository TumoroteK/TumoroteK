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
package fr.aphp.tumorotek.model.coeur.annotation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * Objet persistant mappant la table ITEM.
 *
 * Date: 15/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.0
 *
 */
@Entity
@Table(name = "ITEM")
@NamedQueries(value = {
   @NamedQuery(name = "Item.findByChamp", query = "SELECT i FROM Item i WHERE i.champAnnotation = ?1" + " ORDER BY i.label"),
   @NamedQuery(name = "Item.findByChampAndPlateforme",
      query = "SELECT i FROM Item i WHERE i.champAnnotation = ?1 " + "AND (i.plateforme = ?2 OR i.plateforme is null) "
         + "ORDER BY i.itemId"),
   //			@NamedQuery(name = "Item.findDoublon", 
   //				query = "SELECT i FROM Item i WHERE i.label = ?1" 
   //					+ " AND i.champAnnotation = ?2") 
   @NamedQuery(name = "Item.findByExcludedId", query = "SELECT i FROM Item i WHERE i.itemId != ?1")})
public class Item implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer itemId;
   private String label;
   private String valeur;
   private ChampAnnotation champAnnotation;
   private Plateforme plateforme;

   private Set<AnnotationDefaut> annotationDefauts = new HashSet<>();
   private Set<AnnotationValeur> annotationValeurs = new HashSet<>();

   /** Constructeur par défaut. */
   public Item(){}

   @Id
   @Column(name = "ITEM_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getItemId(){
      return this.itemId;
   }

   public void setItemId(final Integer id){
      this.itemId = id;
   }

   @Column(name = "LABEL", nullable = false, length = 100)
   public String getLabel(){
      return this.label;
   }

   public void setLabel(final String l){
      this.label = l;
   }

   @Column(name = "VALEUR", nullable = true, length = 100)
   public String getValeur(){
      return this.valeur;
   }

   public void setValeur(final String v){
      this.valeur = v;
   }

   @ManyToOne
   @JoinColumn(name = "CHAMP_ANNOTATION_ID", nullable = false)
   public ChampAnnotation getChampAnnotation(){
      return this.champAnnotation;
   }

   public void setChampAnnotation(final ChampAnnotation chp){
      this.champAnnotation = chp;
   }

   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = true)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme pf){
      this.plateforme = pf;
   }

   @OneToMany(mappedBy = "item", cascade = {CascadeType.REMOVE})
   public Set<AnnotationDefaut> getAnnotationDefauts(){
      return this.annotationDefauts;
   }

   public void setAnnotationDefauts(final Set<AnnotationDefaut> defauts){
      this.annotationDefauts = defauts;
   }

   @OneToMany(mappedBy = "item", cascade = {CascadeType.ALL})
   public Set<AnnotationValeur> getAnnotationValeurs(){
      return this.annotationValeurs;
   }

   public void setAnnotationValeurs(final Set<AnnotationValeur> valeurs){
      this.annotationValeurs = valeurs;
   }

   /**
    * 2 items sont consideres comme egaux si ils ont le même label 
    * et la même reference vers le champ annotation.
    * @param obj est l'item à tester.
    * @return true si les items sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final Item test = (Item) obj;
      if(this.itemId != null && this.itemId.equals(test.itemId)){
         return true;
      }

      return ((this.label == test.label || (this.label != null && this.label.equals(test.label)))
         && (this.champAnnotation == test.champAnnotation
            || (this.champAnnotation != null && this.champAnnotation.equals(test.champAnnotation)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur le label et l'id du champ_annotation.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashLabel = 0;
      int hasChampId = 0;
      int hasPF = 0;

      if(this.itemId != null){
         return 31 * hash + this.itemId.hashCode();
      }

      if(this.label != null){
         hashLabel = this.label.hashCode();
      }
      if(this.champAnnotation != null){
         hasChampId = this.champAnnotation.hashCode();
      }
      if(this.plateforme != null){
         hasPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashLabel;
      hash = 31 * hash + hasChampId;
      hash = 31 * hash + hasPF;

      return hash;
   }

   @Override
   public String toString(){
      if(this.champAnnotation != null && this.label != null){
         return "{Thesaurus Item: " + this.champAnnotation.getNom() + "." + this.label + "}";
      }else{
         return "{Empty Item}";
      }
   }

   /**
    * Clone un item (deep copy).
    */
   @Override
   public Item clone(){
      final Item clone = new Item();
      clone.setItemId(this.itemId);
      clone.setLabel(this.label);
      clone.setValeur(this.valeur);
      clone.setChampAnnotation(this.champAnnotation);
      clone.setAnnotationDefauts(this.annotationDefauts);
      clone.setAnnotationValeurs(this.annotationValeurs);
      clone.setPlateforme(getPlateforme());
      return clone;
   }

   //	@Override
   //	public String getPhantomDatat() {
   //		return this.champAnnotation.getNom() + "." + this.label;
   //	}

}
