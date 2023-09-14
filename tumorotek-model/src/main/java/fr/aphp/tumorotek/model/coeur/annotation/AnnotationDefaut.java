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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Objet persistant mappant la table ANNOTATION_DEFAUT.
 *
 * Date: 16/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.0
 *
 */
@Entity
@Table(name = "ANNOTATION_DEFAUT")
@NamedQueries(value = {
   @NamedQuery(name = "AnnotationDefaut.findByChamp",
      query = "SELECT a FROM AnnotationDefaut a WHERE " + "a.champAnnotation = ?1 ORDER BY a.annotationDefautId"),
   @NamedQuery(name = "AnnotationDefaut.findByExcludedId",
      query = "SELECT a FROM AnnotationDefaut a WHERE " + "a.annotationDefautId != ?1")})
public class AnnotationDefaut extends AnnotationCommon implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer annotationDefautId;

   private String alphanum;

   private String texte;

   private Calendar date;

   private Boolean bool;

   private Boolean obligatoire;

   private Item item;

   private ChampAnnotation champAnnotation;

   private Banque banque;

   /** Constructeur par défaut. */
   public AnnotationDefaut(){}

   @Id
   @Column(name = "ANNOTATION_DEFAUT_ID")
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getAnnotationDefautId(){
      return this.annotationDefautId;
   }

   public void setAnnotationDefautId(final Integer id){
      this.annotationDefautId = id;
   }

   @Override
   @Column(name = "ALPHANUM", nullable = true, length = 100)
   public String getAlphanum(){
      return this.alphanum;
   }

   @Override
   public void setAlphanum(final String alpha){
      this.alphanum = alpha;
   }

   @Override
   //Lob
   @Column(name = "TEXTE", nullable = true)
   public String getTexte(){
      return this.texte;
   }

   @Override
   public void setTexte(final String tex){
      this.texte = tex;
   }

   @Override
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "ANNO_DATE", nullable = true)
   public Calendar getDate(){
      if(date != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(date.getTime());
         return cal;
      }
      return null;
   }

   @Override
   public void setDate(final Calendar cal){
      if(cal != null){
         this.date = Calendar.getInstance();
         this.date.setTime(cal.getTime());
      }else{
         this.date = null;
      }
   }

   @Override
   @Column(name = "BOOL", nullable = true)
   public Boolean getBool(){
      return this.bool;
   }

   @Override
   public void setBool(final Boolean b){
      this.bool = b;
   }

   @Column(name = "OBLIGATOIRE", nullable = false)
   public Boolean getObligatoire(){
      return this.obligatoire;
   }

   public void setObligatoire(final Boolean obl){
      this.obligatoire = obl;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = true)
   public Banque getBanque(){
      return banque;
   }

   @Override
   public void setBanque(final Banque bank){
      this.banque = bank;
   }

   /**
    * Override methode de AnnotationValeur car la cascade differe.
    * En effet, les valeurs par defaut peuvent être enregistrees
    * en même temps que les items, contrairement aux valeurs qui sont
    * assignees aux objets ultérieurement.
    */
   @Override
   @ManyToOne
   @JoinColumn(name = "ITEM_ID", nullable = true)
   public Item getItem(){
      return this.item;
   }

   @Override
   public void setItem(final Item it){
      this.item = it;
   }

   /**
    * Override methode de AnnotationValeur car la cascade differe.
    * En effet, les valeurs par defaut peuvent être enregistrees
    * en même temps que les champs, contrairement aux valeurs qui sont
    * assignees aux objets ultérieurement.
    */
   @Override
   @ManyToOne
   @JoinColumn(name = "CHAMP_ANNOTATION_ID", nullable = false)
   public ChampAnnotation getChampAnnotation(){
      return this.champAnnotation;
   }

   @Override
   public void setChampAnnotation(final ChampAnnotation chpA){
      this.champAnnotation = chpA;
   }

   /**
    * 2 valeurs sont considerees comme egales si ils ont les mêmes references
    * vers le champ annotation auxqelles et la banque sont attribuees.
    * La propriete item intervient pour
    * differencier les valeurs defaut lors d'une selection multiple.
    * @param obj est la valeur defaut à tester.
    * @return true si les valeur defaut sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final AnnotationDefaut test = (AnnotationDefaut) obj;

      if(this.annotationDefautId != null && this.annotationDefautId.equals(test.annotationDefautId)){
         return true;
      }

      return ((this.item == test.item || (this.item != null && this.item.equals(test.item)))
         && (this.champAnnotation == test.champAnnotation
            || (this.champAnnotation != null && this.champAnnotation.equals(test.champAnnotation)))
         && (this.banque == test.banque || (this.banque != null && this.banque.equals(test.banque))));
   }

   /**
    * Le hashcode est calculé sur les references vers le
    * champ annotation, l'item auxquels la valeur est attribuee.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashChpId = 0;
      int hashItem = 0;
      int hashBanque = 0;

      if(this.annotationDefautId != null){
         return 31 * hash + this.annotationDefautId.hashCode();
      }

      if(this.champAnnotation != null){
         hashChpId = this.champAnnotation.hashCode();
      }
      if(this.item != null){
         hashItem = this.item.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }

      hash = 31 * hash + hashChpId;
      hash = 31 * hash + hashItem;
      hash = 31 * hash + hashBanque;

      return hash;
   }

   @Override
   public String toString(){
      String output = "{Empty AnnotationDefaut}";

      if(this.champAnnotation != null){
         output = "{Defaut: " + this.champAnnotation.getNom() + ".";
         if(this.alphanum != null){
            output = output + this.alphanum;
         }else if(this.bool != null){
            output = output + bool.toString();
         }else if(this.date != null){
            output = output + new SimpleDateFormat("dd/mm/yyyy hh:MM:ss").format(this.date.getTime());
         }else if(this.texte != null){
            if(texte.length() > 5){
               output = output + texte.substring(0, 5) + "...";
            }else{
               output = output + texte;
            }
         }else if(this.item != null){
            output = output + item.getLabel();
         }else{
            return "{Empty AnnotationDefaut}";
         }
         output = output + "}";
      }
      return output;
   }

   /**
    * Cree un clone d'une valeur d'annotation par defaut.
    * @return clone
    */
   @Override
   public AnnotationDefaut clone(){
      final AnnotationDefaut clone = new AnnotationDefaut();
      clone.setAnnotationDefautId(this.getAnnotationDefautId());
      clone.setChampAnnotation(this.getChampAnnotation());
      clone.setAlphanum(this.getAlphanum());
      clone.setBool(this.getBool());
      clone.setDate(this.getDate());
      clone.setTexte(this.getTexte());
      clone.setItem(this.getItem());
      clone.setBanque(this.getBanque());
      clone.setObligatoire(this.getObligatoire());
      return clone;
   }

}
