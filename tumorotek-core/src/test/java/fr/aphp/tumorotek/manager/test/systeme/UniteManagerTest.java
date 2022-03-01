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
package fr.aphp.tumorotek.manager.test.systeme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.aphp.tumorotek.manager.systeme.UniteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.test.Config;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 *
 * Classe de test pour le manager UniteManager.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class UniteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private UniteManager uniteManager;

   public UniteManagerTest(){}

   @Test
   public void testFindById(){
      final Unite unite = uniteManager.findByIdManager(1);
      assertNotNull(unite);
      assertTrue(unite.getNom().equals("FRAGMENTS"));

      final Unite uniteNull = uniteManager.findByIdManager(25);
      assertNull(uniteNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Unite> list = uniteManager.findAllObjectsManager();
      assertTrue(list.size() == 14);
   }

   /**
    * Test la méthode findByUniteLikeManager.
    */
   @Test
   public void testFindByUniteLikeExactManager(){
      List<Unite> list = uniteManager.findByUniteLikeManager("COUPES", true);
      assertTrue(list.size() == 1);

      list = uniteManager.findByUniteLikeManager("COU", true);
      assertTrue(list.size() == 0);

      list = uniteManager.findByUniteLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByUniteLikeManager(){
      List<Unite> list = uniteManager.findByUniteLikeManager("COUPES", false);
      assertTrue(list.size() == 1);

      list = uniteManager.findByUniteLikeManager("COU", false);
      assertTrue(list.size() == 1);

      list = uniteManager.findByUniteLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeExactManager(){
      List<Unite> list = uniteManager.findByTypeLikeManager("masse", true);
      assertTrue(list.size() == 4);

      list = uniteManager.findByTypeLikeManager("ma", true);
      assertTrue(list.size() == 0);

      list = uniteManager.findByTypeLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeManager(){
      List<Unite> list = uniteManager.findByTypeLikeManager("masse", false);
      assertTrue(list.size() == 4);

      list = uniteManager.findByTypeLikeManager("mas", false);
      assertTrue(list.size() == 4);

      list = uniteManager.findByTypeLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode isUsedObject.
    */
   @Test
   public void testIsUsed(){
      final Unite unite1 = uniteManager.findByIdManager(1);
      assertNotNull(unite1);
      assertTrue(uniteManager.isUsedObjectManager(unite1));

      final Unite unite2 = uniteManager.findByIdManager(3);
      assertNotNull(unite2);
      assertFalse(uniteManager.isUsedObjectManager(unite2));
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Unite unite = new Unite();
      unite.setUnite("COUPES");
      unite.setType("discret");
      assertTrue(uniteManager.findDoublonManager(unite));

      unite.setType(null);
      assertFalse(uniteManager.findDoublonManager(unite));
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   // @Transactional(transactionManager = "transactionManager")
   public void testCrud(){
      // Test de l'insertion
      final Unite unite1 = new Unite();
      unite1.setUnite("COUPES");
      unite1.setType("discret");
      Boolean catchedInsert = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      try{
         uniteManager.createObjectManager(unite1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      // On teste une insertion avec des attributs non valides
      final String[] uniteValues = new String[] {"", "  ", null, "hf¤u_$$|(", createOverLength(30), "GOOD"};
      final String[] typeValues = new String[] {"", " ", null, "*/$$¤..ç=", createOverLength(15), "GOOD"};
      for(int i = 0; i < uniteValues.length; i++){
         for(int j = 0; j < typeValues.length; j++){
            catchedInsert = false;
            try{
               unite1.setUnite(uniteValues[i]);
               unite1.setType(typeValues[j]);
               if(i != 5 || j != 5){ //car creation valide
                  uniteManager.createObjectManager(unite1);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedInsert = true;
               }
            }
            if(i != 5 || j != 5){
               assertTrue(catchedInsert);
            }
         }
      }
      assertTrue(uniteManager.findAllObjectsManager().size() == 14);
      // On test une insertion valide
      unite1.setUnite("COUPES");
      unite1.setType("poids");
      uniteManager.createObjectManager(unite1);
      assertTrue(unite1.getId() == 15);

      // Test de la mise à jour
      final Unite unite2 = uniteManager.findByIdManager(15);
      final String typeUpdated1 = "volume";
      final String typeUpdated2 = "discret";
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      unite2.setType(typeUpdated2);
      Boolean catchedUpdate = false;
      try{
         uniteManager.updateObjectManager(unite2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      // On teste une modif avec les attributs non valides
      for(int i = 0; i < uniteValues.length; i++){
         for(int j = 0; j < typeValues.length; j++){
            catchedUpdate = false;
            try{
               unite2.setUnite(uniteValues[i]);
               unite2.setType(typeValues[j]);
               if(i != 5 || j != 5){ //car creation valide
                  uniteManager.updateObjectManager(unite2);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedUpdate = true;
               }
            }
            if(i != 5 || j != 5){
               assertTrue(catchedUpdate);
            }
         }
      }
      // On teste une mise à jour valide
      unite2.setType(typeUpdated1);
      uniteManager.updateObjectManager(unite2);
      final Unite unite3 = uniteManager.findByIdManager(15);
      assertTrue(unite3.getType().equals(typeUpdated1));

      // Test de la suppression
      final Unite unite4 = uniteManager.findByIdManager(15);
      uniteManager.removeObjectManager(unite4);
      assertNull(uniteManager.findByIdManager(15));
      // On test la suppression d'un objet utilisé
      final Unite unite5 = uniteManager.findByIdManager(1);
      Boolean catchedDelete = false;
      try{
         uniteManager.removeObjectManager(unite5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catchedDelete = true;
         }
      }
      assertTrue(catchedDelete);
   }

}
