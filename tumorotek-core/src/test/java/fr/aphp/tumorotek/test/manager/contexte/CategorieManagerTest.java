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
package fr.aphp.tumorotek.test.manager.contexte;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.context.CategorieManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager CategorieManager.
 * Classe créée le 06/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CategorieManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CategorieManager categorieManager;

   public CategorieManagerTest(){}

   @Test
   public void testFindById(){
      final Categorie cat1 = categorieManager.findByIdManager(1);
      assertNotNull(cat1);
      assertTrue(cat1.getNom().equals("CAT1"));

      final Categorie catNull = categorieManager.findByIdManager(10);
      assertNull(catNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Categorie> list = categorieManager.findAllObjectsManager();
      assertTrue(list.size() == 2);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeExactManager(){
      List<Categorie> list = categorieManager.findByNomLikeManager("CAT1", true);
      assertTrue(list.size() == 1);

      list = categorieManager.findByNomLikeManager("CAT", true);
      assertTrue(list.size() == 0);

      list = categorieManager.findByNomLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      List<Categorie> list = categorieManager.findByNomLikeManager("CAT1", false);
      assertTrue(list.size() == 1);

      list = categorieManager.findByNomLikeManager("CAT", false);
      assertTrue(list.size() == 2);

      list = categorieManager.findByNomLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final Categorie c1 = categorieManager.findByIdManager(1);
      assertFalse(categorieManager.findDoublonManager(c1));
      c1.setNom("CAT2");
      assertTrue(categorieManager.findDoublonManager(c1));
      final Categorie c2 = new Categorie();
      c2.setNom(c1.getNom());
      assertTrue(c2.equals(c1));
      assertTrue(categorieManager.findDoublonManager(c2));
      c2.setNom("ROOO");
      assertFalse(categorieManager.findDoublonManager(c2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final Categorie c1 = categorieManager.findByIdManager(1);
      assertTrue(categorieManager.isUsedObjectManager(c1));
   }

   /**
    * Teste les methodes CRUD. 
    */
   @Test
   public void testCRUD(){
      saveManagerTest();
      saveManagerTest();
      deleteByIdManagerTest();
   }

   private void saveManagerTest(){
      //Insertion nouvel enregistrement
      final Categorie c1 = new Categorie();
      c1.setNom("NEW");
      categorieManager.saveManager(c1);
      assertTrue(categorieManager.findAllObjectsManager().size() == 3);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final Categorie c1Bis = new Categorie();
      c1Bis.setNom("NEW");
      try{
         categorieManager.saveManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(categorieManager.findAllObjectsManager().size() == 3);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "=12$$€", createOverLength(50), null};
      final Categorie c2 = new Categorie();
      for(int i = 0; i < emptyValues.length; i++){
         try{
            c2.setNom(emptyValues[i]);
            categorieManager.saveManager(c2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(categorieManager.findAllObjectsManager().size() == 3);
         }
      }
   }

   /**
    * Teste la methode saveManager. 
    */
   private void saveManagerTest(){
      //Modification d'un enregistrement
      final Categorie c1 = categorieManager.findByIdManager(3);
      c1.setNom("NEW BIS");
      categorieManager.saveManager(c1);
      final Categorie c1Bis = categorieManager.findByIdManager(3);
      assertTrue(c1Bis.getNom().equals("NEW BIS"));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         c1Bis.setNom("CAT1");
         categorieManager.saveManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$_¤¤", createOverLength(50), null};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            c1Bis.setNom(emptyValues[i]);
            categorieManager.saveManager(c1Bis);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            catched = true;
         }
         assertTrue(catched);
      }
   }

   /**
    * Teste la methode deleteByIdManager. 
    */
   private void deleteByIdManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Categorie c1 = categorieManager.findByIdManager(3);
      categorieManager.deleteByIdManager(c1);
      assertTrue(categorieManager.findAllObjectsManager().size() == 2);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			Categorie c2 = categorieManager.findByIdManager(1);
      //			categorieManager.deleteByIdManager(c2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue(categorieManager.findAllObjectsManager().size() == 2);
      //null remove
      categorieManager.deleteByIdManager(null);
      assertTrue(categorieManager.findAllObjectsManager().size() == 2);
   }
}
