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
package fr.aphp.tumorotek.action.recherche;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.zk.ui.Component;

/**
 * Contient les informations caractérisant l'utilisation d'un champ de
 * recherche, informations permettant de pré-remplir le champ lors d'une
 * nouvelle recherche à partir d'un formulaire vide.
 * Date: 17/06/2014.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.13
 *
 */
public class RechercheCompValues
{

   private Class<? extends Component> compClass;

   private String compId;

   private String groupId;

   private Integer selectedIndexValue;

   private Object selectedObject;

   private String textValue;

   private Boolean checkedValue;

   private Calendar calendarValue;

   // @since 2.0.13
   private final List<Object> selectedValues = new ArrayList<>();

   private boolean cumulative = false;

   public Class<? extends Component> getCompClass(){
      return compClass;
   }

   public void setCompClass(final Class<? extends Component> c){
      this.compClass = c;
   }

   public String getCompId(){
      return compId;
   }

   public void setCompId(final String c){
      this.compId = c;
   }

   public String getGroupId(){
      return groupId;
   }

   public void setGroupId(final String g){
      this.groupId = g;
   }

   public Integer getSelectedIndexValue(){
      return selectedIndexValue;
   }

   public void setSelectedIndexValue(final Integer s){
      this.selectedIndexValue = s;
   }

   public Object getSelectedObject(){
      return selectedObject;
   }

   public void setSelectedObject(final Object s){
      this.selectedObject = s;
   }

   public String getTextValue(){
      return textValue;
   }

   public void setTextValue(final String t){
      this.textValue = t;
   }

   public Boolean getCheckedValue(){
      return checkedValue;
   }

   public void setCheckedValue(final Boolean c){
      this.checkedValue = c;
   }

   public Calendar getCalendarValue(){
      return calendarValue;
   }

   public void setCalendarValue(final Calendar c){
      this.calendarValue = c;
   }

   public List<Object> getSelectedValues(){
      return selectedValues;
   }

   public boolean isCumulative(){
      return cumulative;
   }

   public void setCumulative(final boolean c){
      this.cumulative = c;
   }
}
