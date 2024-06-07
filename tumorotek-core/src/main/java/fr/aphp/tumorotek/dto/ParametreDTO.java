package fr.aphp.tumorotek.dto;

import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;

import java.util.Objects;

public class ParametreDTO
{

   private String code;
   private String valeur;

   private String type;

   private String groupe;

   public ParametreDTO(String code, String valeur, String type, String groupe){
      this.code = code;
      this.valeur = valeur;
      this.type = type;
      this.groupe = groupe;
   }

   public String getCode(){
      return code;
   }

   public void setCode(String code){
      this.code = code;
   }

   public String getValeur(){
      return valeur;
   }

   public void setValeur(String valeur){
      this.valeur = valeur;
   }

   public String getType(){
      return type;
   }

   public void setType(String type){
      this.type = type;
   }

   public String getGroupe(){
      return groupe;
   }

   public void setGroupe(String groupe){
      this.groupe = groupe;
   }

   /**
    * Cette méthode retourne la clé d'internationalisation pour le code du paramètre.
    * Si le code n'est pas nul, elle préfixe "params." au code.
    * Si le code est nul, elle retourne le code lui-même, qui sera donc null.
    *
    * Pour que cela fonctionne, le code retourné doit avoir une clé correspondante dans le fichier de propriétés de traduction.
    *
    * @return la clé d'internationalisation pour le code du paramètre
    */

   public String getI18nKey(){
      if (code != null){
         return new StringBuilder("params.").append(code).toString();
      }
      return code;
   }
   public static ParametreDTO mapFromEntity(ParametreValeurSpecifique entity) {
      return new ParametreDTO(entity.getCode(),entity.getValeur(),
                                       entity.getType() , entity.getGroupe());
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ParametreDTO that = (ParametreDTO) o;
      return Objects.equals(code, that.code);
   }

   @Override
   public int hashCode() {
      return Objects.hash(code);
   }

}

