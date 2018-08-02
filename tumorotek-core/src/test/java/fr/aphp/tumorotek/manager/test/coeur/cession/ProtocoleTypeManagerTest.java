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
package fr.aphp.tumorotek.manager.test.coeur.cession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.cession.ProtocoleTypeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager ProtocoleTypeManager.
 * Classe créée le 27/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ProtocoleTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ProtocoleTypeManager protocoleTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ProtocoleTypeManagerTest(){}

   @Test
   public void testFindById(){
      final ProtocoleType pt1 = protocoleTypeManager.findByIdManager(1);
      assertNotNull(pt1);
      assertTrue(pt1.getType().equals("RECHERCHE"));

      final ProtocoleType ptNull = protocoleTypeManager.findByIdManager(10);
      assertNull(ptNull);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeExactManager(){
      List<ProtocoleType> list = protocoleTypeManager.findByTypeLikeManager("RECHERCHE", true);
      assertTrue(list.size() == 1);

      list = protocoleTypeManager.findByTypeLikeManager("RECHER", true);
      assertTrue(list.size() == 0);

      list = protocoleTypeManager.findByTypeLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeManager(){
      List<ProtocoleType> list = protocoleTypeManager.findByTypeLikeManager("RECHERCHE", false);
      assertTrue(list.size() == 1);

      list = protocoleTypeManager.findByTypeLikeManager("RECHER", false);
      assertTrue(list.size() == 1);

      list = protocoleTypeManager.findByTypeLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final ProtocoleType p1 = (protocoleTypeManager.findByTypeLikeManager("RECHERCHE", true)).get(0);
      assertFalse(protocoleTypeManager.findDoublonManager(p1));
      p1.setType("THERAPEUTIQUE");
      assertTrue(protocoleTypeManager.findDoublonManager(p1));
      final ProtocoleType p2 = new ProtocoleType();
      p2.setType(p1.getType());
      p2.setPlateforme(p1.getPlateforme());
      assertTrue(p2.equals(p1));
      assertTrue(protocoleTypeManager.findDoublonManager(p2));
      p2.setType("ROOO");
      assertFalse(protocoleTypeManager.findDoublonManager(p2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final ProtocoleType p1 = (protocoleTypeManager.findByTypeLikeManager("RECHERCHE", true)).get(0);
      assertTrue(protocoleTypeManager.isUsedObjectManager(p1));
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

   @Test
   public void createObjectManagerTest(){
      //Insertion nouvel enregistrement
      final ProtocoleType p1 = new ProtocoleType();
      p1.setType("TEST");
      p1.setPlateforme(plateformeDao.findById(1));
      protocoleTypeManager.createObjectManager(p1);
      assertTrue((protocoleTypeManager.findByTypeLikeManager("TEST", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final ProtocoleType p1Bis = new ProtocoleType();
      p1Bis.setType("TEST");
      try{
         protocoleTypeManager.createObjectManager(p1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      p1Bis.setPlateforme(plateformeDao.findById(1));
      try{
         protocoleTypeManager.createObjectManager(p1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((protocoleTypeManager.findByTypeLikeManager("TEST", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "™|¤$12.K", createOverLength(200), null};
      final ProtocoleType p2 = new ProtocoleType();
      p2.setPlateforme(plateformeDao.findById(1));
      for(int i = 0; i < emptyValues.length; i++){
         try{
            p2.setType(emptyValues[i]);
            protocoleTypeManager.createObjectManager(p2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(protocoleTypeManager.findByOrderManager(p1.getPlateforme()).size() == 3);
         }
      }
   }

   /**
    * Teste la methode updateObjectManager. 
    */
   @Test
   public void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final ProtocoleType p1 = (protocoleTypeManager.findByTypeLikeManager("TEST", true)).get(0);
      p1.setType("TEST2");
      protocoleTypeManager.updateObjectManager(p1);
      assertTrue((protocoleTypeManager.findByTypeLikeManager("TEST2", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         p1.setType("RECHERCHE");
         protocoleTypeManager.updateObjectManager(p1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((protocoleTypeManager.findByTypeLikeManager("RECHERCHE", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$_¤¤", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            p1.setType(emptyValues[i]);
            protocoleTypeManager.updateObjectManager(p1);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            catched = true;
         }
         assertTrue(catched);
      }
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   @Test
   public void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final ProtocoleType p1 = (protocoleTypeManager.findByTypeLikeManager("TEST2", true)).get(0);
      protocoleTypeManager.removeObjectManager(p1);
      assertTrue((protocoleTypeManager.findByTypeLikeManager("TEST2", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			ProtocoleType p2 = (protocoleTypeManager
      //				.findByTypeLikeManager("RECHERCHE", true)).get(0);
      //			protocoleTypeManager.removeObjectManager(p2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((protocoleTypeManager.findByTypeLikeManager("RECHERCHE", true)).size() > 0);
      //null remove
      protocoleTypeManager.removeObjectManager(null);
      assertTrue(protocoleTypeManager.findByOrderManager(p1.getPlateforme()).size() == 2);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = protocoleTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(1).getNom().equals("THERAPEUTIQUE"));
      pf = plateformeDao.findById(2);
      list = protocoleTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 0);
      list = protocoleTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
