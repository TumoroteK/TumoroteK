package fr.aphp.tumorotek.action.administration;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;

import fr.aphp.tumorotek.manager.validation.systeme.ParametreValeurSpecifiqueValidator;
import fr.aphp.tumorotek.param.EParametreType;

/**
 * Classe qui implémente une contrainte de validation pour une valeur de paramètre spécifique de type String.
 * pour les autres types, le contrôle n'est pas nécessaire car le composant est spécifique (intbox, decimalbox, radiogroup ...)
 * Cette classe permet de valider qu'une valeur donnée correspond à un type spécifique.
 */
public class ConstParametreValeur implements Constraint
{

   private String typeValue = EParametreType.STRING.getType();
   
   /**
    * Valide la valeur donnée par rapport au type de valeur spécifié.
    * La validation est effectuée en convertissant la valeur en chaîne de caractères et
    * en vérifiant si elle correspond au type de valeur attendu.
    *
    * @param component Le composant auquel la valeur est associée (généralement un champ de formulaire).
    * @param value La valeur à valider.
    * @throws WrongValueException Si la valeur ne respecte pas la contrainte.
    */
   @Override
   public void validate(Component component, Object value) throws WrongValueException{
      String erreurDetectee = ParametreValeurSpecifiqueValidator.checkValeur((String)value, typeValue);


      if(erreurDetectee != null) {
         throw new WrongValueException(component, Labels.getLabel(erreurDetectee));
      }
      
   }
}
