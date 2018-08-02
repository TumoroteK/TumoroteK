package fr.aphp.tumorotek.manager.validation.workflow;

import fr.aphp.tumorotek.model.validation.NiveauValidation;

public interface NiveauValidationManager
{

   NiveauValidation findCriticiteLevelOk();
   
   NiveauValidation findCriticiteLevelUndefined();
   
}
