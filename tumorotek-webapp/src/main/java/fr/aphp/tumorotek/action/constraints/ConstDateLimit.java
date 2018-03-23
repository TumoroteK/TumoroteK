package fr.aphp.tumorotek.action.constraints;

import java.util.Calendar;
import java.util.Date;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;

/**
 * Contrainte vérifiant que la date entrée sans l'assistant
 * est valide et insérable en base.
 * Date: 21/09/2011
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 */
public class ConstDateLimit implements Constraint
{

   private Boolean nullable = false;

   public Boolean getNullable(){
      return nullable;
   }

   public void setNullable(final Boolean n){
      this.nullable = n;
   }

   @Override
   public void validate(final Component comp, final Object value){
      validateDate(comp, value);
   }

   /**
    * Applique la validation sur la date.
    * @param comp
    * @param value
    */
   private void validateDate(final Component comp, final Object value){
      boolean vide = false;
      try{
         if(value != null){
            if(value instanceof Date){
               final Calendar calValue = Calendar.getInstance();
               calValue.setTime((Date) value);
               // on vérifie que la date ne dépasse pas 9999
               // pour pouvoir être enregistrée en base
               if(!calValue.equals("")){
                  if(calValue.get(Calendar.YEAR) > 9999){
                     throw new WrongValueException(comp, Labels.getLabel("validation.invalid.date"));
                  }
               }else{
                  vide = true;
               }
            }else if(value instanceof Calendar){
               final Calendar calValue = (Calendar) value;
               if(!calValue.equals("")){
                  // on vérifie que la date ne dépasse pas 9999
                  // pour pouvoir être enregistrée en base
                  if(calValue.get(Calendar.YEAR) > 9999){
                     throw new WrongValueException(comp, Labels.getLabel("validation.invalid.date"));
                  }
               }else{
                  vide = true;
               }
            }

            if(vide && !nullable){
               throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
            }
         }else{
            if(!nullable){
               throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
            }else if(comp instanceof Datebox){
               // la contrainte est retiree
               ((Datebox) comp).setConstraint("");
               ((Datebox) comp).clearErrorMessage(true);
               ((Datebox) comp).setValue(null);
               // on remet la contrainte
               ((Datebox) comp).setConstraint(this);
            }
         }
      }catch(final WrongValueException e){
         Clients.scrollIntoView(e.getComponent());
         throw (e);
      }
   }

}
