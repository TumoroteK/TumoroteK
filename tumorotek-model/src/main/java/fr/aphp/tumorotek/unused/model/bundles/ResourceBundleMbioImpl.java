package fr.aphp.tumorotek.unused.model.bundles;

public class ResourceBundleMbioImpl implements ResourceBundleMbio
{

   private String mbioPath;

   public void setMbioPath(final String s){
      this.mbioPath = s;
   }

   @Override
   public String getMbioConfDirectory(){
      if(mbioPath != null){
         return mbioPath;
      }else{
         return "";
      }
   }

}
