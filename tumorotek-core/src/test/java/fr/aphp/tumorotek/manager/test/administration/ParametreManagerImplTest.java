package fr.aphp.tumorotek.manager.test.administration;

import fr.aphp.tumorotek.dao.administration.ParametreDao;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.impl.administration.ParametresManagerImpl;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ParametreManagerImplTest extends AbstractManagerTest
{

   @Autowired
   private ParametresManagerImpl parametreManager;

   @Autowired
   private ParametreDao parametreDao;

   String code = "derives.obligatoire";

   String valueFromDb = "value_from_db";


   @Before
   public void insertOneParametre(){
      Integer plateformeId = 1;
      // Crée un paramètre avec une valeur initiale dans la base de données
      ParametreValeurSpecifique parametre = new ParametreValeurSpecifique();
      parametre.setCode(code);
      parametre.setValeur(valueFromDb);
      parametre.setPlateformeId(plateformeId);
      parametreDao.createObject(parametre);

   }


   /**
    * Teste le scénario où la mise à jour de la valeur d'un paramètre, qui n'a jamais été modifiée auparavant
    * (n'existe pas dans la base de données).
    */

   @Test
   public void testUpdateValeurNoExistsInDB(){
      // Arrange
      Integer plateformId = 2;
      String nouvelleValeur = "value_from_user";

      // Vérifie que le paramètre n'existe pas initialement dans la base de données
      ParametreValeurSpecifique parametre = parametreManager.findParametresByPlateformeIdAndCode(plateformId, code);
      assertNull(parametre);

      // Act
      parametreManager.updateValeur(plateformId, code, nouvelleValeur);

      // Assert
      // Vérifie que le paramètre a été créé avec la nouvelle valeur
      ParametreValeurSpecifique parametreMaj = parametreManager.findParametresByPlateformeIdAndCode(plateformId, code);
      assertNotNull(parametreMaj);
      assertEquals(nouvelleValeur, parametreMaj.getValeur());
   }

   /**
    * Teste le scénario où la mise à jour de la valeur d'un paramètre, qui a été modifiée auparavant
    * (existe dans la base de données).
    */
   @Test
   public void testUpdateValeurExistsInDB(){
      // Arrange
      Integer plateformeId = 1;
      String nouvelleValeur = "NOUVELLE_VALEUR";

      // Vérifie que le paramètre existe initialement dans la base de données
      ParametreValeurSpecifique parametreDeLaBase = parametreManager.findParametresByPlateformeIdAndCode(plateformeId, code);
      assertNotNull(parametreDeLaBase);
      assertEquals(parametreDeLaBase.getValeur(), valueFromDb);

      // Act
      parametreManager.updateValeur(plateformeId, code, nouvelleValeur);

      // Assert
      // Récupère l'objet mis à jour depuis la base de données et vérifie ses propriétés
      ParametreValeurSpecifique parametreMaj = parametreManager.findParametresByPlateformeIdAndCode(plateformeId, code);
      assertNotNull(parametreMaj);
      assertEquals(nouvelleValeur, parametreMaj.getValeur());
   }

   /***
    *
    */
   @Test
   public void updateInvalidCode() {
      try {
         parametreManager.updateValeur(5, "non_existing_code", "value");
         // La ligne ci-dessus devrait générer une exception
         fail("Expected IllegalArgumentException was not thrown");
      } catch (IllegalArgumentException e) {
         // Vérifier le message de l'exception
         assertEquals("Invalid code: non_existing_code", e.getMessage());
      }
   }

   /**
    * Teste le scénario où l'on cherche à récupérer les paramètres d'une plateforme qui utilise juste les valeurs par défaut.
    */

   @Test
   public void testgetDefaultParametresByPlateformeId(){
      Integer plateformeId = 3;
      // Act
      Set<ParametreDTO> result = parametreManager.getParametresByPlateformeId(plateformeId);

      // Assert
      assertNotNull(result);
      assertEquals(EParametreValeurParDefaut.values().length, result.size());

      // Verify that findParametresByPlateformeIdAndCode was called for each parameter code
      for(EParametreValeurParDefaut param : EParametreValeurParDefaut.values()){
         // Find the corresponding ParametreDTO in the result list
         ParametreDTO parametreDTO = result.stream().filter(dto -> dto.getCode().equals(param.getCode())).findFirst().orElse(null);

         // Verify that the ParametreDTO exists
         assertNotNull(parametreDTO);

         // Assert that the ParametreDTO has the correct values
         assertEquals(param.getCode(), parametreDTO.getCode());
         assertEquals(param.getValeur(), parametreDTO.getValeur());
         // Add more assertions for other properties if needed

      }
   }

   /**
    * Teste le scénario où l'on cherche à récupérer les paramètres d'une plateforme qui utilise juste les valeurs par défaut.
    */

   @Test
   public void testgetParametresByPlateformeId(){
      Integer plateformeId = 1;
      // Act
      Set<ParametreDTO> result = parametreManager.getParametresByPlateformeId(plateformeId);

      // Assert
      assertNotNull(result);
      assertEquals(EParametreValeurParDefaut.values().length, result.size());

      // Verify that findParametresByPlateformeIdAndCode was called for each parameter code
      for(EParametreValeurParDefaut param : EParametreValeurParDefaut.values()){
         // Find the corresponding ParametreDTO in the result list
         ParametreDTO parametreDTO = result.stream().filter(dto -> dto.getCode().equals(param.getCode())).findFirst().orElse(null);

         // Verify that the ParametreDTO exists
         assertNotNull(parametreDTO);

         // Assert that the ParametreDTO has the correct values
         assertEquals(param.getCode(), parametreDTO.getCode());
         if (param.getCode().equals(code)){
            assertNotEquals(param.getValeur(), parametreDTO.getValeur());

         }

      }
   }
}
