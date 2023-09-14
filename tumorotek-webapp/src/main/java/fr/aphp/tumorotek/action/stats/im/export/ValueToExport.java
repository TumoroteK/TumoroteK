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
package fr.aphp.tumorotek.action.stats.im.export;

/**
 * Class de transport des données renvoyées par les procédures de
 * calculs des indicateurs. Les instances sont de cette classe sont produites
 * par liste, pour chaque indicateur.
 * Date:  12/08/2015
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class ValueToExport
{
   // private String unitTemp;
   // private String year;
   private Number value;

   private Number valuePourcentage;

   private Integer subDivId;

   private Integer banqueId;
   /*
   	public ValueToExport(Number v,
   			Integer b,
   			Integer s) {
   		this.value = "nb : " + v;
   		this.banqueId = b;
   		this.subDivId = s;
   	}*/

   public ValueToExport(final Number v, final Number vpour, final Integer b, final Integer s){
      this.value = v;
      this.valuePourcentage = vpour;
      this.banqueId = b;
      this.subDivId = s;
   }

   //	public String getYear() {
   //		return year;
   //	}
   //
   //	public void setYear(String year) {
   //		this.year = year;
   //	}

   public Number getValue(){
      return value;
   }

   public void setValue(final Number value){
      this.value = value;
   }

   public Number getValuePourcentage(){
      return valuePourcentage;
   }

   public void setValuePourcentage(final Number v){
      this.valuePourcentage = v;
   }

   public Integer getSubDivId(){
      return subDivId;
   }

   public void setSubDivId(final Integer s){
      this.subDivId = s;
   }

   public Integer getBanqueId(){
      return banqueId;
   }

   public void setBanqueId(final Integer i){
      this.banqueId = i;
   }

   //	public String getUnitTemp() {
   //		return unitTemp;
   //	}
   //
   //	public void setUnitTemp(String unitTemp) {
   //		this.unitTemp = unitTemp;
   //	}

   @Override
   public String toString(){
      return "ValueToExport banqueId: " + banqueId + ", subDivId : " + subDivId + ", value : " + value;
   }

}
