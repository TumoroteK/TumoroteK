package fr.aphp.tumorotek.param;

/**
 * Énumération représentant les paramètres par défaut pour chaque plateforme de TK.
 * Ces paramètres sont accessibles sous l'onglet administrateur - paramètres.
 * Si l'utilisateur modifie les valeurs par défaut via l'interface utilisateur, les modifications sont enregistrées
 * dans la table PARAMETRE_VALEUR_SPECIFIQUE.
 *
 * Chaque élément de l'énumération est défini par un code, une valeur, un type et le groupe auquel il appartient.
 * /!\ La valeur ne peut pas être null. Il faut utiliser "" dans ce cas.
 * <p>//TK-435 : un seul paramètre dépendant de la plateforme est nécessaire lors de la mise en oeuvre
 * mais le mécanisme est prévu pour en gérer d'autres sans aucune modification au niveau de l'écran Administration/Paramètres.
 * L'ajout d'un paramètre consiste donc :
 * - à définir une nouvelle entrée pour l'enum en définissant notamment la valeur par défaut.
 * - mettre en oeuvre son utilisation dans l'application
 * </p>
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




public enum EParametreValeurParDefaut {

   // Paramètre quantité utilisé obligatoire lors de la création du dérivé
   DERIVE_QTE_OBLIGATOIRE("DERIVE_QTE_OBLIGATOIRE", "true", EParametreType.BOOLEAN, "DERIVE"),

   // Paramètre pour le nombre maximal de conteneurs que l'utilisateur peut choisir pour la génération du plan.
   STOCKAGE_NB_MAX_CONTENEUR_A_IMPRIMER("STOCKAGE_NB_MAX_CONTENEUR_A_IMPRIMER", "25", EParametreType.INTEGER, "STOCKAGE");

   private final String code;
   private final String valeur;
   private final String type;
   private final String groupe;

   EParametreValeurParDefaut(String code, String valeur, EParametreType typeEnum, String groupe) {
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
      EParametreValeurParDefaut paramEnum = findByCode(code);
      return (paramEnum != null) ? paramEnum.getValeur() : null;
   }

   /**
    * Vérifie si une valeur donnée est la valeur par défaut pour un code de paramètre donné.
    *
    * @param code le code du paramètre
    * @param value la valeur à vérifier
    * @return true si la valeur donnée est la valeur par défaut, sinon false
    */
   public static boolean isDefaultValue(String code, String value) {
      String defaultValue = getDefaultValeurByCode(code);
      return (defaultValue != null) && defaultValue.equals(value);
   }

   public String getCode() {
      return code;
   }

   public String getValeur() {
      return valeur;
   }

   public String getType() {
      return type;
   }

   public String getGroupe() {
      return groupe;
   }
}
