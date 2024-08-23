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
//      La conversion en `String` assure que toutes les valeurs sont traitées uniformément par la validation, évitant ainsi les erreurs de type
      String stringValue = value != null ? value.toString() : "";
      String erreurDetectee = ParametreValeurSpecifiqueValidator.checkValeur(stringValue, typeValue);


      if(erreurDetectee != null) {
         throw new WrongValueException(component, Labels.getLabel(erreurDetectee));
      }
      
   }
}
