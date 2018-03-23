package fr.aphp.tumorotek.model.impression;

import java.util.ArrayList;
import java.util.List;

/**
 * Types de Templates pour l'impression
 *
 * @author Answald Bournique
 *
 */
public enum ETemplateType
{

   BLOC("BLOC"), DOC("DOC");

   private String type = "";

   ETemplateType(final String type){
      this.type = type;
   }

   public String getType(){
      return type;
   }

   @Override
   public String toString(){
      return type;
   }

   /**
    * Liste tous les types
    * @return liste de tous les types
    */
   public static List<String> getTypeList(){
      final List<String> typeList = new ArrayList<>();
      for(final ETemplateType templateType : ETemplateType.values()){
         typeList.add(templateType.getType());
      }
      return typeList;
   }

}
