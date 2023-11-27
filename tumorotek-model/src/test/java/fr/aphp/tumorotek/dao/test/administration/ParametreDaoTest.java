package fr.aphp.tumorotek.dao.test.administration;

import fr.aphp.tumorotek.dao.administration.ParametreDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class ParametreDaoTest extends AbstractDaoTest
{

   private ParametreDao parametreDao;

   public void setParametreDao(ParametreDao parametreDao){
      this.parametreDao = parametreDao;
   }

   /**
    * Teste la méthode {@link ParametreDao#findByPlateformeIdAndCode(Integer, String)}.
    *
    * Ce test vérifie que le DAO récupère correctement un ParametreValeurSpecifique par son plateformeId et son code.
    * Il crée un nouveau ParametreValeurSpecifique, l'enregistre en utilisant le DAO, puis le récupère et vérifie que
    * la liste retournée contient exactement un élément correspondant au plateformeId et au code fournis.
    */
   public void testFindByPlateformeIdAndCode(){
      Integer plateformID = 1;
      String code = "welcome.message";
      ParametreValeurSpecifique newParametreValeurSpecifique = new ParametreValeurSpecifique();
      newParametreValeurSpecifique.setPlateformeId(plateformID);
      newParametreValeurSpecifique.setCode(code);

      parametreDao.createObject(newParametreValeurSpecifique);

      List<ParametreValeurSpecifique> result = parametreDao.findByPlateformeIdAndCode(plateformID, code);
      assertNotNull(result);
      assertEquals(1, result.size());
      String codeError = "welcame.message";
      assertEquals(code, result.get(0).getCode());
      assertNotEquals(codeError, result.get(0).getCode());

   }

   /**
    * Teste la contrainte d'unicité pour la combinaison de code et plateformeId.
    *
    * Ce test garantit que toute tentative d'insertion de plusieurs instances de ParametreValeurSpecifique avec la même
    * combinaison de code et plateformeId entraîne une {@link DataIntegrityViolationException}, plus
    * spécifiquement une {@link ConstraintViolationException}. En effet, la combinaison de code et de
    * plateformeId doit être unique.
    */
   public void testDuplicate(){

      Integer plateformID = 1;
      String code = "welcome.message";

      try{
         for(int i = 0; i < 3; i++){
            ParametreValeurSpecifique newParametreValeurSpecifique = new ParametreValeurSpecifique();
            newParametreValeurSpecifique.setPlateformeId(plateformID);
            newParametreValeurSpecifique.setCode(code);

            parametreDao.createObject(newParametreValeurSpecifique);
         }

         // If no exception is thrown, fail the test
         fail("Expected DataIntegrityViolationException not thrown");
      }catch(DataIntegrityViolationException e){
         // Expected exception, do further assertions if needed
         assertTrue(e.getCause() instanceof ConstraintViolationException);
      }
   }

   public void testFindAllByPlateformeId(){
      Integer platformId = 1;
      String code = "welcome.message";
      String code2 = "message";
      ParametreValeurSpecifique newParametreValeurSpecifique = new ParametreValeurSpecifique();
      newParametreValeurSpecifique.setPlateformeId(platformId);
      newParametreValeurSpecifique.setCode(code);
      ParametreValeurSpecifique newParametreValeurSpecifique2 = new ParametreValeurSpecifique();
      newParametreValeurSpecifique2.setPlateformeId(platformId);
      newParametreValeurSpecifique2.setCode(code2);

      parametreDao.createObject(newParametreValeurSpecifique);
      parametreDao.createObject(newParametreValeurSpecifique2);
      List<ParametreValeurSpecifique> allParameters = parametreDao.findAllByPlateformeId(platformId);
      assertNotNull(allParameters);
      assertEquals(2, allParameters.size());
   }
}


