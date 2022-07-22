/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (14/07/2014)
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
package fr.aphp.tumorotek.model.ui;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table UI_COMP_VALUE.
 * Classe créée le 16/07/14.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.11
 *
 */
@Entity
@Table(name = "UI_COMP_VALUE")
public class UiCompValue implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer uiCompValueId;

   private UiRequete uiRequete;

   private String idComponent;

   private String componentClass;

   private Integer indexValue;

   private String textValue;

   private Boolean checkValue;

   private Calendar calendarValue;

   public UiCompValue(){}

   public UiCompValue(final Integer id, final UiRequete req, final String idC, final String cC, final Integer iV, final String tV,
      final Boolean kV, final Calendar cV){
      setUiCompValueId(id);
      setUiRequete(req);
      setIdComponent(idC);
      setComponentClass(cC);
      setIndexValue(iV);
      setTextValue(tV);
      setCheckValue(kV);
      setCalendarValue(cV);
   }

   @Id
   @Column(name = "UI_COMP_VALUE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getUiCompValueId(){
      return uiCompValueId;
   }

   public void setUiCompValueId(final Integer i){
      this.uiCompValueId = i;
   }

   @ManyToOne
   @JoinColumn(name = "UI_REQUETE_ID", nullable = false)
   public UiRequete getUiRequete(){
      return uiRequete;
   }

   public void setUiRequete(final UiRequete u){
      this.uiRequete = u;
   }

   @Column(name = "ID_COMPONENT", nullable = false, length = 50)
   public String getIdComponent(){
      return idComponent;
   }

   public void setIdComponent(final String i){
      this.idComponent = i;
   }

   @Column(name = "COMPONENT_CLASS", nullable = false, length = 50)
   public String getComponentClass(){
      return componentClass;
   }

   public void setComponentClass(final String c){
      this.componentClass = c;
   }

   @Column(name = "INDEX_VALUE", nullable = true)
   public Integer getIndexValue(){
      return indexValue;
   }

   public void setIndexValue(final Integer x){
      this.indexValue = x;
   }

   @Column(name = "TEXT_VALUE", nullable = true, length = 300)
   public String getTextValue(){
      return textValue;
   }

   public void setTextValue(final String x){
      this.textValue = x;
   }

   @Column(name = "CHECK_VALUE", nullable = true)
   public Boolean getCheckValue(){
      return checkValue;
   }

   public void setCheckValue(final Boolean k){
      this.checkValue = k;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "CALENDAR_VALUE", nullable = true)
   public Calendar getCalendarValue(){
      if(calendarValue != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(calendarValue.getTime());
         return cal;
      }else{
         return null;
      }
   }

   public void setCalendarValue(final Calendar cal){
      if(cal != null){
         this.calendarValue = Calendar.getInstance();
         this.calendarValue.setTime(cal.getTime());
      }else{
         this.calendarValue = null;
      }
   }

   /**
    * 2 uiCompValues sont considérées comme égales si elles appartiennent
    * à la même uiRequete et si elles concerne le même composant id
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
      final UiCompValue test = (UiCompValue) obj;
      return ((this.uiRequete == test.uiRequete || (this.uiRequete != null && this.uiRequete.equals(test.uiRequete)))
         && (this.idComponent == test.idComponent || (this.idComponent != null && this.idComponent.equals(test.idComponent))));
   }

   /**
    * Le hashcode est calculé sur les attributs définissant l'égalité.
    * i.e. uiRequete et idComponent.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashUiRequete = 0;
      int hashIdComponent = 0;

      if(this.uiRequete != null){
         hashUiRequete = this.uiRequete.hashCode();
      }

      if(this.idComponent != null){
         hashIdComponent = this.idComponent.hashCode();
      }

      hash = 31 * hash + hashUiRequete;
      hash = 31 * hash + hashIdComponent;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.uiRequete != null && this.idComponent != null){
         return "{" + this.uiRequete + ", " + this.idComponent + "}";
      }else{
         return "{Empty Unite}";
      }
   }
}
