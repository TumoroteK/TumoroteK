package fr.aphp.tumorotek.action.administration;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;

import fr.aphp.tumorotek.manager.validation.systeme.ParametreValeurSpecifiqueValidator;

public class ConstParametreValeur implements Constraint
{

   private String typeValue;
   
   public ConstParametreValeur(String typeValue) {
      this.typeValue = typeValue;
   }
   
   @Override
   public void validate(Component component, Object value) throws WrongValueException{
      String erreurDetectee = ParametreValeurSpecifiqueValidator.checkValeur((String)value, typeValue);
      
      if(erreurDetectee != null) {
         throw new WrongValueException(component, Labels.getLabel(erreurDetectee));
      }
      
   }
}
