package fr.aphp.tumorotek.manager.test.administration;

import fr.aphp.tumorotek.dao.administration.ParametreValeurSpecifiqueDao;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@Transactional
public class ParametresManagerTest extends AbstractManagerTest4 {

   @Autowired
   private ParametresManager parametresManager;

   @Autowired
   private ParametreValeurSpecifiqueDao parametreValeurSpecifiqueDao;

   private static final Random random = new Random();

   String code = "DERIVE_QTE_OBLIGATOIRE";

   String valueFromDb = "value_from_db";

   @Before
   public void insertOneParametre(){
      Integer plateformeId = 1;
      // Crée un paramètre avec une valeur initiale dans la base de données
      ParametreValeurSpecifique parametre = new ParametreValeurSpecifique();
      parametre.setCode(code);
      parametre.setValeur(valueFromDb);
      parametre.setPlateformeId(plateformeId);
      parametreValeurSpecifiqueDao.createObject(parametre);

   }

   /**
    * Teste le scénario où la mise à jour de la valeur d'un paramètre, qui n'a jamais été modifiée auparavant
    * (n'existe pas dans la base de données).
    */

   @Test
   public void testUpdateValeurNoExistsInDB(){
      // Arrange
      Integer plateformId = 11244;
      String nouvelleValeur = "value_from_user_input";

      // Vérifie que le paramètre n'existe pas initialement dans la base de données
      ParametreValeurSpecifique parametre = parametresManager.findParametresByPlateformeIdAndCode(plateformId, code);
      assertNull(parametre);

      // Act
      parametresManager.updateValeur(plateformId, code, nouvelleValeur);

      // Assert
      // Vérifie que le paramètre a été créé avec la nouvelle valeur
      ParametreValeurSpecifique parametreMaj = parametresManager.findParametresByPlateformeIdAndCode(plateformId, code);
      assertNotNull(parametreMaj);
      assertEquals(nouvelleValeur, parametreMaj.getValeur());

   }

   /**
    * Teste le scénario où la mise à jour de la valeur d'un paramètre, qui a été modifiée auparavant
    * (existe dans la base de données).
    */
   @Test
   public void testUpdateValeurExistsInDB(){
      Integer plateformeId = 1;
      // Arrange
      String nouvelleValeur = "NOUVELLE_VALEUR";

      // Vérifie que le paramètre existe initialement dans la base de données
      ParametreValeurSpecifique parametreDeLaBase = parametresManager.findParametresByPlateformeIdAndCode(plateformeId, code);
      assertNotNull(parametreDeLaBase);
      assertEquals(parametreDeLaBase.getValeur(), valueFromDb);

      // Act
      parametresManager.updateValeur(plateformeId, code, nouvelleValeur);

      // Assert
      // Récupère l'objet mis à jour depuis la base de données et vérifie ses propriétés
      ParametreValeurSpecifique parametreMaj = parametresManager.findParametresByPlateformeIdAndCode(plateformeId, code);
      assertNotNull(parametreMaj);
      assertEquals(nouvelleValeur, parametreMaj.getValeur());

   }

   /***
    *
    */
   @Test
   public void updateInvalidCode(){
      try{
         parametresManager.updateValeur(5, "non_existing_code", "value");
         // La ligne ci-dessus devrait générer une exception
         fail("Expected IllegalArgumentException was not thrown");
      }catch(IllegalArgumentException e){
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
      List<ParametreDTO> result = parametresManager.findParametresByPlateformeId(plateformeId);

      // Assert
      assertNotNull(result);
      assertNotNull(result);
      // Le nombre de paramètres par défaut est égal au nombre d'éléments d'énumération + 2 (message d'accueil + logo),
      int totalParametres = EParametreValeurParDefaut.values().length + 2;

      assertEquals(totalParametres, result.size());

      // Verify that findParametresByPlateformeIdAndCode was called for each parameter code
      for(EParametreValeurParDefaut param : EParametreValeurParDefaut.values()){
         // Find the corresponding ParametreDTO in the result list
         ParametreDTO parametreDTO =
            result.stream().filter(dto -> dto.getCode().equals(param.getCode())).findFirst().orElse(null);

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
      List<ParametreDTO> result = parametresManager.findParametresByPlateformeId(plateformeId);

      // Assert
      assertNotNull(result);
      // Le nombre de paramètres par défaut est égal au nombre d'éléments d'énumération
      int totalParametres = EParametreValeurParDefaut.values().length;

      assertEquals(totalParametres, result.size());

      // Verify that findParametresByPlateformeIdAndCode was called for each parameter code
      for(EParametreValeurParDefaut param : EParametreValeurParDefaut.values()){
         // Find the corresponding ParametreDTO in the result list
         ParametreDTO parametreDTO =
            result.stream().filter(dto -> dto.getCode().equals(param.getCode())).findFirst().orElse(null);

         // Verify that the ParametreDTO exists
         assertNotNull(parametreDTO);

         // Assert that the ParametreDTO has the correct values
         assertEquals(param.getCode(), parametreDTO.getCode());
         if(param.getCode().equals(code)){
            assertNotEquals(param.getValeur(), parametreDTO.getValeur());

         }

      }
   }

}
