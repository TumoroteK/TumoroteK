package fr.aphp.tumorotek.manager.validation.systeme;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.param.EParametreType;

public class ParametreValeurSpecifiqueValidator implements Validator
{

   @Override
   public boolean supports(Class<?> clazz){
      return ParametreDTO.class.equals(clazz);
   }

   @Override
   public void validate(Object target, Errors errors){//target = String !!!!
      ParametreDTO parametre = (ParametreDTO)target;
      String typeValeur = parametre.getType();
      String valeur = parametre.getValeur();
      
      String erreurDetectee = checkValeur(valeur, typeValeur);
      if(erreurDetectee != null) {
         errors.reject("valeur", erreurDetectee);
      }
   }
   
   /**
    * vérifie la valeur transmise en fonction du type transmis
    * @param valeur
    * @param typeValeur
    * @return la clé i18n du message à afficher si une erreur est détectée sinon null
    */
   public static String checkValeur(String valeur, String typeValeur) {
      if(typeValeur.equals(EParametreType.INTEGER.getType())) {
         try {
            Integer.valueOf(valeur);
         }
         catch (NumberFormatException e) {
            return "params.valeur.notEntier";
         }
      }
      else if(typeValeur.equals(EParametreType.FLOAT.getType())) {
         try {
            Float.valueOf(valeur);
         }
         catch (NumberFormatException e) {
            return "params.valeur.notNumber";
         }
      }
      else {
         if(valeur.length() > 255) {
            return "params.valeur.tooLong";
         }
      }

      return null;
   }

}
