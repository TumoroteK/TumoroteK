package fr.aphp.tumorotek.param;

/**
 * Énumération représentant les paramètres par défaut dans chaque platform de TK
 * Ces paramètres sont accessibles sous l'onglet administrateur - paramètres.
 * Si l'utilisateur modifie les valeurs par défaut via l'interface utilisateur, les modifications sont enregistrées
 * dans la table PARAMETRE_VALEUR_SPECIFIQUE.
 *
 * Chaque élément de l'énumération est défini par un code, une valeur par défaut, un type et un groupe auquel il appartient.
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
   // paramètre quantité utilisé obligatoire lors de la création du dérivé :
   DERIVE_QTE_OBLIGATOIRE("params.derives.obligatoire", "true", "boolean", "derive");

   private String code;

   private String valeur;

   private String type;

   private String groupe;

   EParametreValeurParDefaut(String code, String valeur, String type, String groupe){
      this.code = code;
      this.valeur = valeur;
      this.type = type;
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
