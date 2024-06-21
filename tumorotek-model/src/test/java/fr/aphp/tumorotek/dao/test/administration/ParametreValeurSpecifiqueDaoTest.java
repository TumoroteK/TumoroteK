package fr.aphp.tumorotek.dao.test.administration;

import fr.aphp.tumorotek.dao.administration.ParametreValeurSpecifiqueDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.config.ParametreValeurSpecifique;

import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class ParametreValeurSpecifiqueDaoTest extends AbstractDaoTest
{

   private ParametreValeurSpecifiqueDao parametreValeurSpecifiqueDao;

   public void setParametreDao(ParametreValeurSpecifiqueDao parametreValeurSpecifiqueDao){
      this.parametreValeurSpecifiqueDao = parametreValeurSpecifiqueDao;
   }

   /**
    * Teste la méthode {@link ParametreValeurSpecifiqueDao#findByPlateformeIdAndCode(Integer, String)}.
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

      parametreValeurSpecifiqueDao.createObject(newParametreValeurSpecifique);

      List<ParametreValeurSpecifique> result = parametreValeurSpecifiqueDao.findByPlateformeIdAndCode(plateformID, code);
      assertNotNull(result);
      assertEquals(1, result.size());
      String codeError = "welcame.message";
      assertEquals(code, result.get(0).getCode());
      assertNotEquals(codeError, result.get(0).getCode());

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

      parametreValeurSpecifiqueDao.createObject(newParametreValeurSpecifique);
      parametreValeurSpecifiqueDao.createObject(newParametreValeurSpecifique2);
      List<ParametreValeurSpecifique> allParameters = parametreValeurSpecifiqueDao.findAllByPlateformeId(platformId);
      assertNotNull(allParameters);
      assertEquals(2, allParameters.size());
   }
}


