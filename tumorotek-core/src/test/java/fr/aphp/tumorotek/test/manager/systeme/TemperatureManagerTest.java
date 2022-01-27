/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.test.manager.systeme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.systeme.TemperatureManager;
import fr.aphp.tumorotek.model.systeme.Temperature;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager TemperatureManager.
 * Classe créée le 07/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TemperatureManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TemperatureManager temperatureManager;

   public TemperatureManagerTest(){}

   /**
    * Test la méthode findById.
    */
   @Test
   public void testFindById(){
      final Temperature temp = temperatureManager.findByIdManager(1);
      assertNotNull(temp);
      assertTrue(temp.getTemperature().equals(new Float(20)));
      final Temperature tempNull = temperatureManager.findByIdManager(10);
      assertNull(tempNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Temperature> list = temperatureManager.findAllObjectsManager();
      assertTrue(list.size() == 5);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Temperature temp = new Temperature();
      final Float temp1 = new Float(20);
      final Float temp2 = new Float(50);

      temp.setTemperature(temp1);
      assertTrue(temperatureManager.findDoublonManager(temp));

      temp.setTemperature(temp2);
      assertFalse(temperatureManager.findDoublonManager(temp));

      final Temperature tempBis = temperatureManager.findByIdManager(2);
      assertFalse(temperatureManager.findDoublonManager(tempBis));

      tempBis.setTemperature(temp1);
      assertTrue(temperatureManager.findDoublonManager(tempBis));

      assertFalse(temperatureManager.findDoublonManager(null));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      saveManagerTest();
      saveManagerTest();
      deleteByIdManagerTest();
   }

   private void saveManagerTest() throws ParseException{

      final Temperature tempNew1 = new Temperature();
      final Float temp = new Float(50);
      final Float tempD = new Float(20);

      Boolean catched = false;
      // on test l'insertion d'un doublon
      tempNew1.setTemperature(tempD);
      try{
         temperatureManager.saveManager(tempNew1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(temperatureManager.findAllObjectsManager().size() == 5);

      // Test de la validation lors de la création
      try{
         tempNew1.setTemperature(null);
         temperatureManager.saveManager(tempNew1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 5);

      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      tempNew1.setTemperature(temp);
      temperatureManager.saveManager(tempNew1);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 6);
      final int id = tempNew1.getTemperatureId();

      // Vérification
      final Temperature tempTest = temperatureManager.findByIdManager(id);
      assertNotNull(tempTest);
      assertTrue(tempTest.getTemperature().equals(temp));

      // Suppression
      temperatureManager.deleteByIdManager(tempTest);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 5);
   }

   private void saveManagerTest() throws ParseException{

      final Temperature tempNew = new Temperature();
      final Float temp = new Float(50);
      final Float temp2 = new Float(50);
      final Float tempD = new Float(20);

      tempNew.setTemperature(temp);
      temperatureManager.saveManager(tempNew);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 6);
      final int id = tempNew.getTemperatureId();

      final Temperature tempUp1 = temperatureManager.findByIdManager(id);
      Boolean catched = false;
      // on test l'insertion d'un doublon
      tempUp1.setTemperature(tempD);
      try{
         temperatureManager.saveManager(tempUp1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(temperatureManager.findAllObjectsManager().size() == 6);

      // Test de la validation lors de la modif
      try{
         tempUp1.setTemperature(null);
         temperatureManager.saveManager(tempUp1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 6);

      // on teste une modif valide
      tempUp1.setTemperature(temp2);
      temperatureManager.saveManager(tempUp1);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 6);

      // Vérification
      final Temperature tempTest = temperatureManager.findByIdManager(id);
      assertNotNull(tempTest);
      assertTrue(tempTest.getTemperature().equals(temp2));

      // Suppression
      temperatureManager.deleteByIdManager(tempTest);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 5);
   }

   private void deleteByIdManagerTest(){
      // test de la suppression d'un objet null
      temperatureManager.deleteByIdManager(null);
      assertTrue(temperatureManager.findAllObjectsManager().size() == 5);
   }
}
