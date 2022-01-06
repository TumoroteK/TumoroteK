package fr.aphp.tumorotek.dao.test;

import java.util.ResourceBundle;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.model.bundles.ResourceBundleTumo;

public class ResourceBundleTumoTest extends AbstractDaoTest //FIXME Class de test non utilisée/executée ?
{

   /** Bean. */
   @Autowired
 ResourceBundleTumo resourceBundleTumo;


   public ResourceBundleTumoTest(){

   }

   @Test
public void testDoesResourceBundleExists(){
      assertTrue(resourceBundleTumo.doesResourceBundleExists("tumorotek.properties"));
      assertFalse(resourceBundleTumo.doesResourceBundleExists("blabla"));
      assertFalse(resourceBundleTumo.doesResourceBundleExists(null));
   }

   @Test
public void testGetResourceBundle(){
      ResourceBundle bundle = resourceBundleTumo.getResourceBundle("tumorotek.properties");
      assertTrue(bundle.getString("tk.filesystem").equals("/home/mathieu/TKtest/"));

      bundle = resourceBundleTumo.getResourceBundle("blabla");
      assertNull(bundle);

      bundle = resourceBundleTumo.getResourceBundle(null);
      assertNull(bundle);
   }

}
