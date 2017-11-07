package fr.aphp.tumorotek.manager.validation.interfacage;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;

public class DossierExterneValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return DossierExterne.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		DossierExterne dossierExterne = (DossierExterne) target; 	

		// numéro non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "identificationDossier", 
						"dossierExterne.identificationDossier.empty");
		//numero valide
		if (dossierExterne.getIdentificationDossier() != null) {
			if (!dossierExterne.getIdentificationDossier()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("identificationDossier", 
						"dossierExterne.identificationDossier.illegal");
			}
			if (dossierExterne.getIdentificationDossier().length() > 100) {
				errors.rejectValue("identificationDossier", 
						"dossierExterne.identificationDossier.tooLong");
			}
		}
	}

}
