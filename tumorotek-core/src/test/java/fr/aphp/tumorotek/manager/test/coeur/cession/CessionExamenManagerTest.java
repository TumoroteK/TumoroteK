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
import fr.aphp.tumorotek.manager.coeur.cession.CessionExamenManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager CessionExamenManager.
 * Classe créée le 27/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CessionExamenManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CessionExamenManager cessionExamenManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public CessionExamenManagerTest(){}

   @Test
   public void testFindById(){
      final CessionExamen ce1 = cessionExamenManager.findByIdManager(1);
      assertNotNull(ce1);
      assertTrue(ce1.getExamen().equals("EXAMEN 1"));

      final CessionExamen ceNull = cessionExamenManager.findByIdManager(10);
      assertNull(ceNull);
   }

   /**
    * Test la méthode findByExamenLikeManager.
    */
   @Test
   public void testFindByExamenLikeExactManager(){
      List<CessionExamen> list = cessionExamenManager.findByExamenLikeManager("EXAMEN 1", true);
      assertTrue(list.size() == 1);

      list = cessionExamenManager.findByExamenLikeManager("EXAMEN", true);
      assertTrue(list.size() == 0);

      list = cessionExamenManager.findByExamenLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByExamenLikeManager.
    */
   @Test
   public void testFindByExamenLikeManager(){
      List<CessionExamen> list = cessionExamenManager.findByExamenLikeManager("EXAMEN 1", false);
      assertTrue(list.size() == 1);

      list = cessionExamenManager.findByExamenLikeManager("EXAMEN", false);
      assertTrue(list.size() == 2);

      list = cessionExamenManager.findByExamenLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final CessionExamen c1 = cessionExamenManager.findByIdManager(1);
      assertFalse(cessionExamenManager.findDoublonManager(c1));
      c1.setExamen("EXAMEN 2");
      c1.setExamenEn("EXAMEN_EN 2");
      assertTrue(cessionExamenManager.findDoublonManager(c1));
      final CessionExamen c2 = new CessionExamen();
      c2.setExamen(c1.getExamen());
      c2.setExamenEn(c1.getExamenEn());
      c2.setPlateforme(c1.getPlateforme());
      assertTrue(c2.equals(c1));
      assertTrue(cessionExamenManager.findDoublonManager(c2));
      c2.setExamen("ROOO");
      assertFalse(cessionExamenManager.findDoublonManager(c2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final CessionExamen c1 = cessionExamenManager.findByIdManager(1);
      assertTrue(cessionExamenManager.isUsedObjectManager(c1));
      //Enregistrement n'est pas reference
      final CessionExamen c2 = cessionExamenManager.findByIdManager(2);
      assertFalse(cessionExamenManager.isUsedObjectManager(c2));
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
      final CessionExamen c1 = new CessionExamen();
      c1.setExamen("RAAAAA");
      c1.setPlateforme(plateformeDao.findById(1));
      cessionExamenManager.createObjectManager(c1);
      assertTrue((cessionExamenManager.findByExamenLikeManager("RAAAAA", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final CessionExamen c1Bis = new CessionExamen();
      c1Bis.setExamen("RAAAAA");
      try{
         cessionExamenManager.createObjectManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      c1Bis.setPlateforme(plateformeDao.findById(1));
      try{
         cessionExamenManager.createObjectManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((cessionExamenManager.findByExamenLikeManager("RAAAAA", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "=¤¤¤12.K", createOverLength(200), null};
      final CessionExamen c2 = new CessionExamen();
      c2.setPlateforme(plateformeDao.findById(1));
      for(int i = 0; i < emptyValues.length; i++){
         try{
            c2.setExamen(emptyValues[i]);
            cessionExamenManager.createObjectManager(c2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(cessionExamenManager.findByOrderManager(c1.getPlateforme()).size() == 4);
         }
      }
   }

   /**
    * Teste la methode updateObjectManager. 
    */
   @Test
   public void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final CessionExamen c1 = (cessionExamenManager.findByExamenLikeManager("RAAAAA", true)).get(0);
      c1.setExamen("TEST");
      cessionExamenManager.updateObjectManager(c1);
      assertTrue((cessionExamenManager.findByExamenLikeManager("TEST", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         c1.setExamen("EXAMEN 1");
         c1.setExamenEn("EXAMEN_EN 1");
         cessionExamenManager.updateObjectManager(c1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((cessionExamenManager.findByExamenLikeManager("EXAMEN 1", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$_¤¤", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         try{
            c1.setExamen(emptyValues[i]);
            cessionExamenManager.updateObjectManager(c1);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            assertTrue((cessionExamenManager.findByExamenLikeManager("EXAMEN 1", true)).size() == 1);
         }
      }
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   @Test
   public void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final CessionExamen c1 = (cessionExamenManager.findByExamenLikeManager("TEST", true)).get(0);
      cessionExamenManager.removeObjectManager(c1);
      assertTrue((cessionExamenManager.findByExamenLikeManager("TEST", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			CessionExamen c2 = (cessionExamenManager
      //					.findByExamenLikeManager("EXAMEN 1", true)).get(0);
      //			cessionExamenManager.removeObjectManager(c2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((cessionExamenManager.findByExamenLikeManager("EXAMEN 1", true)).size() > 0);
      //null remove
      cessionExamenManager.removeObjectManager(null);
      assertTrue(cessionExamenManager.findByOrderManager(c1.getPlateforme()).size() == 3);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = cessionExamenManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(1).getNom().equals("EXAMEN 1"));
      pf = plateformeDao.findById(2);
      list = cessionExamenManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = cessionExamenManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
