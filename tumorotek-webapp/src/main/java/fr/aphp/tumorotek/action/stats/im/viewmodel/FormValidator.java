package fr.aphp.tumorotek.action.stats.im.viewmodel;

import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class FormValidator extends AbstractValidator
{

   @Override
   public void validate(final ValidationContext ctx){
      //all the bean properties
      final Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

      validateName(ctx, (String) beanProps.get("nom").getValue());
      validateDescription(ctx, (String) beanProps.get("description").getValue());
   }

   private void validateName(final ValidationContext ctx, final String name){
      if(name == null){
         this.addInvalidMessage(ctx, "nom", "Veuillez indiquer un nom pour votre Modèle!");
      }
   }

   private void validateDescription(final ValidationContext ctx, final String desc){
      if(desc == null){
         this.addInvalidMessage(ctx, "description", "Courte description!");
      }
   }
}
