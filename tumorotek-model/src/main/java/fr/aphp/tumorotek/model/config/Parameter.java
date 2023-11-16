package fr.aphp.tumorotek.model.config;

public class Parameter
{
   private String code;
   private String value;
   private String type;

   public Parameter(String code, String value, String type){
      this.code = code;
      this.value = value;
      this.type = type;
   }

   public String getCode(){
      return code;
   }

   public void setCode(String key){
      this.code = key;
   }

   public String getValue(){
      return value;
   }

   public void setValue(String value){
      this.value = value;
   }

   public String getType(){
      return type;
   }

   public void setType(String type){
      this.type = type;
   }
}
