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
package fr.aphp.tumorotek.model.systeme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table TEMPERATURE.
 * Classe créée le 07/07/2010.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "TEMPERATURE")
//@NamedQueries(value = {
//   @NamedQuery(name = "Temperature.findByExcludedId", query = "SELECT t FROM Temperature t " + "WHERE t.temperatureId != ?1")})
public class Temperature implements Serializable
{

   private static final long serialVersionUID = 7889751373831810664L;

   private Integer temperatureId;
   private Float temperature;

   /** Constructeur par défaut. */
   public Temperature(){}

   @Id
   @Column(name = "TEMPERATURE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTemperatureId(){
      return temperatureId;
   }

   public void setTemperatureId(final Integer id){
      this.temperatureId = id;
   }

   @Column(name = "TEMPERATURE", nullable = false)
   public Float getTemperature(){
      return temperature;
   }

   public void setTemperature(final Float temp){
      this.temperature = temp;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Temperature test = (Temperature) obj;
      return ((this.temperature == test.temperature || (this.temperature != null && this.temperature.equals(test.temperature))));
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashTemperature = 0;

      if(this.temperature != null){
         hashTemperature = this.temperature.hashCode();
      }

      hash = 31 * hash + hashTemperature;

      return hash;
   }

   @Override
   public String toString(){
      return "{" + this.temperature + "}";
   }

}
