package fr.aphp.tumorotek.dto;

import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;

import java.util.Objects;

public class ParametreDTO
{

   private String code;
   private String valeur;

   private String type;

   private String groupe;

   public ParametreDTO(){
   }
   
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

   @Override
   public ParametreDTO clone(){
      return new ParametreDTO(this.code, this.valeur, this.type, this.groupe);
   }
}

