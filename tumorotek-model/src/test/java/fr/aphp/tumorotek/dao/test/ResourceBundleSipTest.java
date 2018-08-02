package fr.aphp.tumorotek.dao.test;

import java.util.ResourceBundle;

import fr.aphp.tumorotek.model.bundles.ResourceBundleSip;

public class ResourceBundleSipTest extends AbstractDaoTest //FIXME Test jamais executé / utilisé ?
{

   /** Bean. */
   private ResourceBundleSip resourceBundleSip;

   public void setResourceBundleSip(final ResourceBundleSip r){
      this.resourceBundleSip = r;
   }

   public ResourceBundleSipTest(){

   }

   public void testDoesResourceBundleExists(){
      assertTrue(resourceBundleSip.doesResourceBundleExists("serveur_Identites.properties")); //FIXME Aucun Path spécifié ??
      assertFalse(resourceBundleSip.doesResourceBundleExists("blabla"));
      assertFalse(resourceBundleSip.doesResourceBundleExists(null));
   }

   public void testGetResourceBundle(){
      ResourceBundle bundle = resourceBundleSip.getResourceBundle("serveur_Identites.properties");
      assertTrue(bundle.getString("DBMS").equals("ORACLE")); //FIXME False

      bundle = resourceBundleSip.getResourceBundle("blabla");
      assertNull(bundle);

      bundle = resourceBundleSip.getResourceBundle(null);
      assertNull(bundle);
   }

}
