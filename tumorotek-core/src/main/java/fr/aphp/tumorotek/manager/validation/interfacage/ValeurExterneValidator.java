package fr.aphp.tumorotek.manager.validation.interfacage;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

public class ValeurExterneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ValeurExterne.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValeurExterne valeur = (ValeurExterne) target; 	

		//valeur valide
		if (valeur.getValeur() != null) {
			ValidationUtils
				.rejectIfEmptyOrWhitespace(errors, "valeur", 
										"valeurExterne.valeur.empty");
			
			if (!valeur.getValeur()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("valeur", 
						"valeurExterne.valeur.illegal");
			}
			if (valeur.getValeur().length() > 250) {
				errors.rejectValue("valeur", 
						"valeurExterne.valeur.tooLong");
			}
		}
	}

}
