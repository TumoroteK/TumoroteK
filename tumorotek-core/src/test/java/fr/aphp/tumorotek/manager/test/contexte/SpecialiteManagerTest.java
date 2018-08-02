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
package fr.aphp.tumorotek.manager.test.contexte;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Specialite;

/**
 *
 * Classe de test pour le manager SpecialiteManager.
 * Classe créée le 12/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class SpecialiteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private SpecialiteManager specialiteManager;

   public SpecialiteManagerTest(){}

   @Test
   public void testFindById(){
      final Specialite spec1 = specialiteManager.findByIdManager(2);
      assertNotNull(spec1);
      assertTrue(spec1.getNom().equals("DERMATOLOGIE"));

      final Specialite specNull = specialiteManager.findByIdManager(10);
      assertNull(specNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Specialite> list = specialiteManager.findAllObjectsManager();
      assertTrue(list.size() == 5);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeExactManager(){
      List<Specialite> list = specialiteManager.findByNomLikeManager("DERMATOLOGIE", true);
      assertTrue(list.size() == 1);

      list = specialiteManager.findByNomLikeManager("DERM", true);
      assertTrue(list.size() == 0);

      list = specialiteManager.findByNomLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      List<Specialite> list = specialiteManager.findByNomLikeManager("DERMATOLOGIE", false);
      assertTrue(list.size() == 1);

      list = specialiteManager.findByNomLikeManager("DERM", false);
      assertTrue(list.size() == 1);

      list = specialiteManager.findByNomLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final Specialite s1 = specialiteManager.findByIdManager(1);
      assertFalse(specialiteManager.findDoublonManager(s1));
      s1.setNom("BIOCHIMIE");
      assertTrue(specialiteManager.findDoublonManager(s1));
      final Specialite s2 = new Specialite();
      s2.setNom(s1.getNom());
      assertTrue(s2.equals(s1));
      assertTrue(specialiteManager.findDoublonManager(s2));
      s2.setNom("ROOO");
      assertFalse(specialiteManager.findDoublonManager(s2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final Specialite s1 = specialiteManager.findByIdManager(1);
      assertTrue(specialiteManager.isUsedObjectManager(s1));
      //Enregistrement n'est pas reference
      final Specialite s2 = specialiteManager.findByIdManager(4);
      assertFalse(specialiteManager.isUsedObjectManager(s2));
   }

   /**
    * Teste les methodes CRUD. 
    */
   @Test
   public void testCRUD(){
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest(){
      //Insertion nouvel enregistrement
      final Specialite s1 = new Specialite();
      s1.setNom("NEW");
      specialiteManager.createObjectManager(s1);
      assertTrue(specialiteManager.findAllObjectsManager().size() == 6);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final Specialite s1Bis = new Specialite();
      s1Bis.setNom("NEW");
      try{
         specialiteManager.createObjectManager(s1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(specialiteManager.findAllObjectsManager().size() == 6);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "¤¤1$$2.K", createOverLength(200), null};
      final Specialite s2 = new Specialite();
      for(int i = 0; i < emptyValues.length; i++){
         try{
            s2.setNom(emptyValues[i]);
            specialiteManager.createObjectManager(s2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(specialiteManager.findAllObjectsManager().size() == 6);
         }
      }
   }

   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final Specialite s1 = specialiteManager.findByIdManager(6);
      s1.setNom("NEW BIS");
      specialiteManager.updateObjectManager(s1);
      final Specialite s1Bis = specialiteManager.findByIdManager(6);
      assertTrue(s1Bis.getNom().equals("NEW BIS"));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         s1Bis.setNom("BIOCHIMIE");
         specialiteManager.updateObjectManager(s1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$$¤_", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            s1Bis.setNom(emptyValues[i]);
            specialiteManager.updateObjectManager(s1Bis);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            catched = true;
         }
         assertTrue(catched);
      }
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final Specialite s1 = specialiteManager.findByIdManager(6);
      specialiteManager.removeObjectManager(s1);
      assertTrue(specialiteManager.findAllObjectsManager().size() == 5);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			Specialite s2 = specialiteManager.findByIdManager(1);
      //			specialiteManager.removeObjectManager(s2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue(specialiteManager.findAllObjectsManager().size() == 5);
      //null remove
      specialiteManager.removeObjectManager(null);
      assertTrue(specialiteManager.findAllObjectsManager().size() == 5);
   }

}
