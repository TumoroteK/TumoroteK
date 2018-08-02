package fr.aphp.tumorotek.dao.test;

import java.util.ResourceBundle;

import fr.aphp.tumorotek.model.bundles.ResourceBundleTumo;

public class ResourceBundleTumoTest extends AbstractDaoTest //FIXME Class de test non utilisée/executée ?
{

   /** Bean. */
   private ResourceBundleTumo resourceBundleTumo;

   public void setResourceBundleTumo(final ResourceBundleTumo r){
      this.resourceBundleTumo = r;
   }

   public ResourceBundleTumoTest(){

   }

   public void testDoesResourceBundleExists(){
      assertTrue(resourceBundleTumo.doesResourceBundleExists("tumorotek.properties")); //FIXME Aucun path spécifié ????
      assertFalse(resourceBundleTumo.doesResourceBundleExists("blabla"));
      assertFalse(resourceBundleTumo.doesResourceBundleExists(null));
   }

   public void testGetResourceBundle(){
      ResourceBundle bundle = resourceBundleTumo.getResourceBundle("tumorotek.properties");
      assertTrue(bundle.getString("DBMS").equals("MySQL")); //FIXME False

      bundle = resourceBundleTumo.getResourceBundle("blabla");
      assertNull(bundle);

      bundle = resourceBundleTumo.getResourceBundle(null);
      assertNull(bundle);
   }

}
