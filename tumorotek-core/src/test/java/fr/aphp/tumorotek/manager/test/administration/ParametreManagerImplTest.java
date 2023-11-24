package fr.aphp.tumorotek.manager.test.administration;

import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.impl.administration.ParametresManagerImpl;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.test.inmemory.AbstractInMemoryManagerTest;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametreManagerImplTest extends AbstractInMemoryManagerTest
{

   private static ParametresManagerImpl parametreManager;




   @Test
   public void testGetParametresByPlateforme(){

      // Mock data
      //      Integer idPlateforme = 1;
      //      ParametreValeurSpecifique parametreFromDB = new ParametreValeurSpecifique();
      //      parametreFromDB.setCode("derives.obligatoire");
      //      parametreFromDB.setValeur("false");
      //      parametreFromDB.setType("boolean");
      //      parametreFromDB.setGroupe("derive");
      //
      //      // Set up the ParametreDAOMock to return the mock data
      //      // (You may need to implement a mock similar to the example)
      //
      //      // Test the method
      //      List<ParametreDTO> result = parametreManager.findParametresByPlateformeId(idPlateforme);
      //
      //      // Verify the result
      //      assertNotNull(result);
      //      assertEquals(1, result.size());
      //      ParametreDTO resultDTO = result.get(0);
      //      assertEquals("derives.obligatoire", resultDTO.getCode());
      //      assertEquals("false", resultDTO.getValeur());
      //      assertEquals("boolean", resultDTO.getType());
      //      assertEquals("derive", resultDTO.getGroupe());
   }

   @Test
   public void testSaveParametreToDB(){
      // Mock data
      ParametreDTO parametreDTO = new ParametreDTO("derives.obligatoire", "false", "boolean", "derive");
      Integer plateformeId = 4;

      // Test the method

      // Verify that the ParametreDAO method is called with the correct parameter
      ParametreValeurSpecifique
         savedParametreValeurSpecifique = parametreManager.findParametresByPlateformeIdAndCode(plateformeId, "derives.obligatoire");
      assertNotNull(savedParametreValeurSpecifique);
      assertEquals("false", savedParametreValeurSpecifique.getValeur());
      assertEquals("boolean", savedParametreValeurSpecifique.getType());
      assertEquals("derive", savedParametreValeurSpecifique.getGroupe());
   }

   @Test
   public void testModifyParameter(){
      // Mock data
      //      Integer plateformID = 1;
      //      String code = "derives.obligatoire";
      //      String newValue = "true";
      //      ParametreValeurSpecifique parametreFromDB = new ParametreValeurSpecifique();
      //      parametreFromDB.setCode(code);
      //      parametreFromDB.setValeur("false");
      //      parametreFromDB.setType("boolean");
      //      parametreFromDB.setGroupe("derive");
      //
      //      // Set up the ParametreDAOMock to return the mock data
      //      // (You may need to implement a mock similar to the example)
      //
      //      // Test the method
      //      parametreManager.modifyParameter(plateformID, code, newValue);
      //
      //      // Verify that the ParametreDAO method is called with the correct parameter
      //      ParametreValeurSpecifique modifiedParametre = parametreDao.findByPlateformeIdAndCode(plateformID, code);
      //      assertNotNull(modifiedParametre);
      //      assertEquals("true", modifiedParametre.getValeur());
      //      assertEquals("boolean", modifiedParametre.getType());
      //      assertEquals("derive", modifiedParametre.getGroupe());
   }
}
