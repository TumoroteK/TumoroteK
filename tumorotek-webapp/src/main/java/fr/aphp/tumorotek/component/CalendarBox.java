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
package fr.aphp.tumorotek.component;

import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Timebox;

/**
 * Box permettant enregistrement et modification d'une
 * date complète portée par un java Calendar.
 * @see http://docs.zkoss.org/wiki/Macro_Component
 *
 * Date: 01/07/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CalendarBox extends HtmlMacroComponent
{

   private static final long serialVersionUID = 1L;

   private Calendar cal = null;

   private Date date = null;

   private Date dateShort = null;

   private String dateConstraint = null;

   // flag indiquant si le Calendarbox a été modifié
   private boolean hasChanged = false;

   @Override
   public void afterCompose(){
      super.afterCompose();

      ((Timebox) getFirstChild().getLastChild()).setButtonVisible(false);
      ((Datebox) getFirstChild().getFirstChild()).addForward("onBlur", this, "onBlur");
      ((Timebox) getFirstChild().getLastChild()).addForward("onBlur", this, "onBlurTimebox");
   }

   /**
    * Assigne les valeurs aux boxes en dissociant les différentes
    * composantes du Calendar.
    * @param maladie
    */
   public void setValue(final Calendar c){

      cal = c;

      if(getFirstChild() != null){ // vérifie que le composant a été crée
         int hours = 0;
         int minutes = 0;
         if(cal != null){
            hours = cal.get(Calendar.HOUR_OF_DAY);
            minutes = cal.get(Calendar.MINUTE);
            date = cal.getTime();
            dateShort = new Date(cal.getTimeInMillis() - hours - minutes);
         }else{
            /*cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1);
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date = cal.getTime();*/
            date = null;
            dateShort = null;
         }
         if(hours != 0 || minutes != 0){
            ((Timebox) getFirstChild().getLastChild()).setValue(date);
         }else{
            ((Timebox) getFirstChild().getLastChild()).setValue(null);
         }
         ((Datebox) getFirstChild().getFirstChild()).setValue(dateShort);
      }
   }

   /**
    * Recupere les Dates dans les boxes pour créer un Calendar.
    * @return calendar
    */
   public Calendar getValue(){
      cal = Calendar.getInstance();
      if(((Datebox) getFirstChild().getFirstChild()).getValue() != null){
         final Calendar timeCal = Calendar.getInstance();
         timeCal.setTime(((Datebox) getFirstChild().getFirstChild()).getValue());
         cal.set(Calendar.DAY_OF_MONTH, timeCal.get(Calendar.DAY_OF_MONTH));
         cal.set(Calendar.MONTH, timeCal.get(Calendar.MONTH));
         cal.set(Calendar.YEAR, timeCal.get(Calendar.YEAR));
      }else if(dateConstraint != null && dateConstraint.equals("no empty")){
         throw new WrongValueException(this, "Date obligatoire");
      }else{
         return null;
      }

      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      if(((Timebox) getFirstChild().getLastChild()).getValue() != null){
         final Calendar timeCal = Calendar.getInstance();
         timeCal.setTime(((Timebox) getFirstChild().getLastChild()).getValue());
         cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
         cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
      }else{
         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 0);
      }

      return cal;
   }

   public void onBlurTimebox(){
      final Calendar timeCal = Calendar.getInstance();
      if(((Timebox) getFirstChild().getLastChild()).getValue() != null){
         timeCal.setTime(((Timebox) getFirstChild().getLastChild()).getValue());
      }else{
         timeCal.set(Calendar.HOUR_OF_DAY, 0);
         timeCal.set(Calendar.MINUTE, 0);
      }

      if(timeCal.get(Calendar.HOUR_OF_DAY) == 0 && timeCal.get(Calendar.MINUTE) == 0){
         ((Timebox) getFirstChild().getLastChild()).setValue(null);
      }

      Events.postEvent("onBlur", this, null);
   }

   public boolean isHasChanged(){
      return hasChanged;
   }

   public void setHasChanged(final boolean h){
      this.hasChanged = h;
   }

   public void clearErrorMessage(final Calendar value){
      Clients.clearWrongValue(this);
   }

   /**
    * Lors du focus sur la date, si aucune heure n'est spécifiée dans le
    * TimeBox, on le remplit à 00:00. Ceci est utilisé pour IE8, afin que
    * le curseur se positionne au début de la TimeBox lors de l'utilisation
    * des tabulations.
    */
   public void onFocus$dateBox(){
      if(((Timebox) getFirstChild().getLastChild()).getValue() == null){
         final Calendar calTmp = Calendar.getInstance();
         calTmp.set(Calendar.YEAR, 1);
         calTmp.set(Calendar.MONTH, 1);
         calTmp.set(Calendar.DAY_OF_MONTH, 1);
         calTmp.set(Calendar.HOUR_OF_DAY, 0);
         calTmp.set(Calendar.MINUTE, 0);
         calTmp.set(Calendar.SECOND, 0);
         calTmp.set(Calendar.MILLISECOND, 0);
         ((Timebox) getFirstChild().getLastChild()).setValue(calTmp.getTime());
      }
   }

   public void setConstraint(final String cst){
      this.dateConstraint = cst;
   }

   public void setDisabled(final boolean b){
      ((Timebox) getFirstChild().getLastChild()).setDisabled(b);
      ((Datebox) getFirstChild().getFirstChild()).setDisabled(b);

   }

   public Timebox getTimeBox(){
      return ((Timebox) getFirstChild().getLastChild());
   }
}
