package fr.aphp.tumorotek.dao.test;

import java.util.ResourceBundle;

import org.junit.Test;

import fr.aphp.tumorotek.model.bundles.ResourceBundleSip;

public class ResourceBundleSipTest extends AbstractDaoTest
{

   /** Bean. */
 ResourceBundleSip resourceBundleSip;

public void setResourceBundleSip(final ResourceBundleSip r){
      this.resourceBundleSip = r;
   }

   public ResourceBundleSipTest(){

   }

   @Test
public void testDoesResourceBundleExists(){
      assertTrue(resourceBundleSip.doesResourceBundleExists("serveur_Identites.properties"));
      assertFalse(resourceBundleSip.doesResourceBundleExists("blabla"));
      assertFalse(resourceBundleSip.doesResourceBundleExists(null));
   }

   @Test
public void testGetResourceBundle(){
      ResourceBundle bundle = resourceBundleSip.getResourceBundle("serveur_Identites.properties");
      assertTrue(bundle.getString("DBMS").equals("ORACLE"));

      bundle = resourceBundleSip.getResourceBundle("blabla");
      assertNull(bundle);

      bundle = resourceBundleSip.getResourceBundle(null);
      assertNull(bundle);
   }

}
