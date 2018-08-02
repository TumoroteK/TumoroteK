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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.io.export.Champ;

/**
 * Objet persistant mappant la table CHAMP_CALCULE
 * Type de champ d'annotation permettant le calcul entre deux champs
 * ou entre un champ et une valeur saisie
 * Classe créée le 19/02/2018
 * 
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 * 
 */
@Entity
@Table(name = "CHAMP_CALCULE")
@NamedQueries(value = {@NamedQuery(name = "ChampCalcule.findByChampAnnotation",
   query = "SELECT c FROM ChampCalcule c WHERE c.champAnnotation = ?1"),})
public class ChampCalcule implements Serializable
{

   private static final long serialVersionUID = 1L;

   /**
    * id du champ calcule
    */
   private Integer champCalculeId;
   /**
    * Champ annotation auquel est relie le champ calculé
    */
   private ChampAnnotation champAnnotation;
   /**
    * Premier champ pour le calcul
    */
   private Champ champ1;
   /**
    * Deuxième champ pour le calcul
    */
   private Champ champ2;
   /**
    * Si le calcul se fait avec une saisie utilisateur (à la place de champ2)
    * TODO ChampCalcule - Renommer plus explicitement comme "valeurSaisie" ?
    */
   private String valeur;
   /**
    * Opération a effectuer entre les deux champs/valeur
    * TODO ChampCalcule - Enum ?
    */
   private String operateur;
   /**
    * DataType que retourne le champCalculé
    */
   private DataType dataType;

   /** Constructeur par défaut. */
   public ChampCalcule(){}

   /**
    * id du champ calcule
    * @return id du champ calcule
    */
   @Id
   @Column(name = "CHAMP_CALCULE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getChampCalculeId(){
      return this.champCalculeId;
   }

   /**
    * id du champ calcule
    * @param id id du champ calcule
    */
   public void setChampCalculeId(Integer id){
      this.champCalculeId = id;
   }

   /**
    * Champ annotation auquel est relie le champ calculé
    * @return Champ annotation auquel est relie le champ calculé
    */
   @OneToOne
   @JoinColumn(name = "CHAMP_ANNOTATION_ID", unique = true)
   public ChampAnnotation getChampAnnotation(){
      return this.champAnnotation;
   }

   /**
    * Champ annotation auquel est relie le champ calculé
    * @param chp Champ annotation auquel est relie le champ calculé
    */
   public void setChampAnnotation(ChampAnnotation chp){
      this.champAnnotation = chp;
   }

   /**
    * Premier champ pour le calcul
    * @return Premier champ pour le calcul
    */
   @ManyToOne(cascade = {CascadeType.REMOVE})
   @JoinColumn(name = "CHAMP1_ID")
   public Champ getChamp1(){
      return champ1;
   }

   /**
    * Premier champ pour le calcul
    * @param champ1 Premier champ pour le calcul
    */
   public void setChamp1(Champ champ1){
      this.champ1 = champ1;
   }

   /**
    * Deuxième champ pour le calcul
    * @return Deuxième champ pour le calcul
    */
   @ManyToOne(cascade = {CascadeType.REMOVE})
   @JoinColumn(name = "CHAMP2_ID", nullable = true)
   public Champ getChamp2(){
      return champ2;
   }

   /**
    * Deuxième champ pour le calcul
    * @param champ2 Deuxième champ pour le calcul
    */
   public void setChamp2(Champ champ2){
      this.champ2 = champ2;
   }

   /**
    * Si le calcul se fait avec une saisie utilisateur (à la place de champ2)
    * @return Si le calcul se fait avec une saisie utilisateur (à la place de champ2)
    */
   @Column(name = "VALEUR")
   public String getValeur(){
      return valeur;
   }

   /**
    * Si le calcul se fait avec une saisie utilisateur (à la place de champ2)
    * @param valeur Si le calcul se fait avec une saisie utilisateur (à la place de champ2)
    */
   public void setValeur(String valeur){
      this.valeur = valeur;
   }

   /**
    * Opération a effectuer entre les deux champs/valeur
    * @return Opération a effectuer entre les deux champs/valeur
    */
   @Column(name = "OPERATEUR", nullable = false)
   public String getOperateur(){
      return operateur;
   }

   /**
    * Opération a effectuer entre les deux champs/valeur
    * @param operateur Opération a effectuer entre les deux champs/valeur
    */
   public void setOperateur(String operateur){
      this.operateur = operateur;
   }

   /**
    * DataType que retourne le champCalculé
    * @return DataType que retourne le champCalculé
    */
   @ManyToOne
   @JoinColumn(name = "DATA_TYPE_ID", nullable = false)
   public DataType getDataType(){
      return dataType;
   }

   /**
    * DataType que retourne le champCalculé
    * @param dataType DataType que doit retourner le champCalculé
    */
   public void setDataType(DataType dataType){
      this.dataType = dataType;
   }

   /**
    * 2 items sont consideres comme egaux si ils ont le même label 
    * et la même reference vers le champ annotation.
    * @param obj est l'item à tester.
    * @return true si les items sont égaux.
    */
   @Override
   public boolean equals(Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      ChampCalcule test = (ChampCalcule) obj;
      if(this.champCalculeId != null && this.champCalculeId.equals(test.champCalculeId)){
         return true;
      }

      return ((this.champAnnotation == test.champAnnotation
         || (this.champAnnotation != null && this.champAnnotation.equals(test.champAnnotation))));
   }

   /**
    * 
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hasChampId = 0;
      int hasChamp1 = 0;
      int hasChamp2 = 0;
      int hasOperateur = 0;
      int hasValeur = 0;
      int hasDataType= 0;

      if(this.champCalculeId != null){
         return 31 * hash + this.champCalculeId.hashCode();
      }

      if(this.champAnnotation != null){
         hasChampId = this.champAnnotation.hashCode();
      }

      if(this.champ1 != null){
         hasChamp1 = this.champ1.hashCode();
      }

      if(this.champ2 != null){
         hasChamp2 = this.champ2.hashCode();
      }

      if(this.operateur != null){
         hasOperateur = this.operateur.hashCode();
      }

      if(this.valeur != null){
         hasValeur = this.valeur.hashCode();
      }
      
      if(this.dataType != null){
         hasDataType = this.dataType.hashCode();
      }

      hash = 31 * hash + hasChampId;
      hash = 31 * hash + hasChamp1;
      hash = 31 * hash + hasChamp2;
      hash = 31 * hash + hasOperateur;
      hash = 31 * hash + hasValeur;
      hash = 31 * hash + hasDataType;

      return hash;
   }

   @Override
   public String toString(){
      if(this.champAnnotation != null){
         return "{Champ Calculé: " + this.champAnnotation.getNom() + "}";
      }
      return "{Empty Item}";
   }

   /**
    * Clone un item (deep copy).
    */
   public ChampCalcule clone(){
      ChampCalcule clone = new ChampCalcule();
      clone.setChampCalculeId(this.champCalculeId);
      clone.setChampAnnotation(this.champAnnotation);
      clone.setChamp1(this.champ1);
      clone.setChamp2(this.champ2);
      clone.setOperateur(this.operateur);
      clone.setValeur(this.valeur);
      clone.setDataType(this.dataType);
      return clone;
   }

}
