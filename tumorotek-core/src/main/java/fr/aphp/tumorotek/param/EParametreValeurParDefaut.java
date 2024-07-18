package fr.aphp.tumorotek.param;

/**
 * Énumération représentant les paramètres par défaut pour chaque platforme de TK
 * Ces paramètres sont accessibles sous l'onglet administrateur - paramètres.
 * Si l'utilisateur modifie les valeurs par défaut via l'interface utilisateur, les modifications sont enregistrées
 * dans la table PARAMETRE_VALEUR_SPECIFIQUE.
 *
 * Chaque élément de l'énumération est défini par un code, une valeur, un type et le groupe auquel il appartient.
 * /!\ La valeur ne peut pas être null
 *
 * Exemple d'utilisation :
 * <pre>
 * {@code
 * EParametreValeurParDefaut.DERIVE_QTE_OBLIGATOIRE.getCode();
 * EParametreValeurParDefaut.DERIVE_QTE_OBLIGATOIRE.getValeur();
 * EParametreValeurParDefaut.DERIVE_QTE_OBLIGATOIRE.getType();
 * EParametreValeurParDefaut.DERIVE_QTE_OBLIGATOIRE.getGroupe();
 * }
 * </pre>
 */
public enum EParametreValeurParDefaut
{
   //TK-435 : un seul paramètre dépendant de la plateforme est nécessaire lors de la mise en oeuvre
   //mais le mécanisme est prévu pour en gérer d'autres sans aucune modification au niveau de l'écran Administration/Paramètres.
   //L'ajout d'un paramètre consiste donc :
   // - à définir une nouvelle entrée pour l'enum en définissant notamment la valeur par défaut.
   //   mais ATTENTION, celle-ci ne peut pas être null. Il faut utiliser "" dans ce cas.
   // - mettre en oeuvre son utilisation dans l'application
   
   // paramètre quantité utilisé obligatoire lors de la création du dérivé :
   DERIVE_QTE_OBLIGATOIRE("DERIVE_QTE_OBLIGATOIRE", "true", EParametreType.BOOLEAN, "DERIVE");

   private String code;

   private String valeur;

   private String type;

   private String groupe;

   EParametreValeurParDefaut(String code, String valeur, EParametreType typeEnum, String groupe){
      this.code = code;
      this.valeur = valeur;
      this.type = typeEnum.getType();
      this.groupe = groupe;
   }

   public static EParametreValeurParDefaut findByCode(String code) {
      for (EParametreValeurParDefaut paramEnum : values()) {
         if (paramEnum.getCode().equals(code)) {
            return paramEnum;
         }
      }
      return null;
   }

   /**
    * Récupère la valeur par défaut associée à un code de paramètre donné.
    *
    * @param code le code du paramètre
    * @return la valeur par défaut associée au code du paramètre, ou null si le code est introuvable
    */
   public static String getDefaultValeurByCode(String code) {
      // Trouver l'énumération correspondant au code donné
      EParametreValeurParDefaut paramEnum = findByCode(code);
      // Si une correspondance est trouvée, retourner la valeur par défaut
      if (paramEnum != null) {
         return paramEnum.getValeur();
      }
      // Si aucune correspondance n'est trouvée, retourner null
      return null;
   }


   /**
    * Vérifie si une valeur donnée est la valeur par défaut pour un code de paramètre donné.
    *
    * @param code le code du paramètre
    * @param value la valeur à vérifier
    * @return true si la valeur donnée est la valeur par défaut, sinon false
    */
   public static boolean isDefaultValue(String code, String value) {
      // Récupérer la valeur par défaut pour le code de paramètre donné
      String defaultValue = getDefaultValeurByCode(code);
      // Vérifier si la valeur par défaut n'est pas null et égale à la valeur donnée
      return defaultValue != null && defaultValue.equals(value);
   }
   public String getCode(){
      return code;
   }

   public String getValeur(){
      return valeur;
   }

   public String getType(){
      return type;
   }

   public String getGroupe(){
      return groupe;
   }
}
