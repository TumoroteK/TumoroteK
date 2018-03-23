package fr.aphp.tumorotek.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextDaoBase-test-h2.xml"})
public class AbstractInMemoryTests
{
   @BeforeClass
   public static void init(){
      cleanDB();
   }

   /**
    * Supprime le dossier /db situé dans le target pour effacer la base de données H2 avant de lancer un nouveau test unitaire
    */
   public static void cleanDB(){
      // Directory path.
      final String directoryPath = "target/db";

      //Deleting the directory recursively using FileUtils.
      try{
         FileUtils.deleteDirectory(new File(directoryPath));
      }catch(final IOException e){
         e.printStackTrace();
      }
   }
}
