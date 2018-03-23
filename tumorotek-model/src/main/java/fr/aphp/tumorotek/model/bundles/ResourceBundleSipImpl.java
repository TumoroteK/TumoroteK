package fr.aphp.tumorotek.model.bundles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResourceBundleSipImpl implements ResourceBundleSip
{

   private final Log log = LogFactory.getLog(ResourceBundleSip.class);

   private String sipPath;

   public void setSipPath(final String s){
      this.sipPath = s;
   }

   @Override
   public boolean doesResourceBundleExists(final String baseName){
      boolean exist = true;

      if(baseName != null){
         InputStreamReader reader = null;
         FileInputStream fis = null;
         final File file = new File(sipPath, baseName);
         if(file.isFile()){ // Also checks for existance
            try{
               fis = new FileInputStream(file);
               reader = new InputStreamReader(fis, Charset.forName("UTF-8"));
               reader.close();
               fis.close();
            }catch(final FileNotFoundException e){
               exist = false;
            }catch(final IOException e){
               log.error(e);
            }finally{
               try{
                  reader.close();
               }catch(final IOException e){
                  reader = null;
               }
               try{
                  fis.close();
               }catch(final IOException e){
                  fis = null;
               }
            }
         }else{
            exist = false;
         }
      }else{
         exist = false;
      }

      return exist;
   }

   @Override
   public ResourceBundle getResourceBundle(final String baseName){
      InputStreamReader reader = null;
      FileInputStream fis = null;
      ResourceBundle bundle = null;

      if(baseName != null){
         final File file = new File(sipPath, baseName);
         if(file.isFile()){ // Also checks for existance
            try{
               fis = new FileInputStream(file);
               reader = new InputStreamReader(fis, Charset.forName("UTF-8"));
               bundle = new PropertyResourceBundle(reader);
            }catch(final FileNotFoundException e){
               log.error(e);
            }catch(final IOException e){
               log.error(e);
            }finally{
               try{
                  reader.close();
               }catch(final IOException e){
                  reader = null;
               }
               try{
                  fis.close();
               }catch(final IOException e){
                  fis = null;
               }
            }
         }
      }

      return bundle;
   }
}
