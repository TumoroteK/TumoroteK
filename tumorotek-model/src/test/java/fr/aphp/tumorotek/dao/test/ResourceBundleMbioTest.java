package fr.aphp.tumorotek.dao.test;

import fr.aphp.tumorotek.model.bundles.ResourceBundleMbio;

public class ResourceBundleMbioTest extends AbstractDaoTest //FIXME non lancé dans maven surefire ?
{

   /** Bean. */
   private ResourceBundleMbio resourceBundleMbio;

   public void setResourceBundleMbio(final ResourceBundleMbio rMbio){
      this.resourceBundleMbio = rMbio;
   }

   public ResourceBundleMbioTest(){

   }

   public void testDoesResourceBundleExists(){
      assertNotNull(resourceBundleMbio.getMbioConfDirectory());
      assertTrue(resourceBundleMbio.getMbioConfDirectory().equals("/home/pierre/apache-tomcat-6.0.18/conf/mbio/")); //FIXME False
   }

}
