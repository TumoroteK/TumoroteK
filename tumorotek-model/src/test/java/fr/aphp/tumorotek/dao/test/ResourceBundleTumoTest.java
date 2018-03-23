package fr.aphp.tumorotek.dao.test;

import java.util.ResourceBundle;

import fr.aphp.tumorotek.model.bundles.ResourceBundleTumo;

public class ResourceBundleTumoTest extends AbstractDaoTest
{

   /** Bean. */
   private ResourceBundleTumo resourceBundleTumo;

   public void setResourceBundleTumo(final ResourceBundleTumo r){
      this.resourceBundleTumo = r;
   }

   public ResourceBundleTumoTest(){

   }

   public void testDoesResourceBundleExists(){
      assertTrue(resourceBundleTumo.doesResourceBundleExists("tumorotek.properties"));
      assertFalse(resourceBundleTumo.doesResourceBundleExists("blabla"));
      assertFalse(resourceBundleTumo.doesResourceBundleExists(null));
   }

   public void testGetResourceBundle(){
      ResourceBundle bundle = resourceBundleTumo.getResourceBundle("tumorotek.properties");
      assertTrue(bundle.getString("DBMS").equals("MySQL"));

      bundle = resourceBundleTumo.getResourceBundle("blabla");
      assertNull(bundle);

      bundle = resourceBundleTumo.getResourceBundle(null);
      assertNull(bundle);
   }

}
