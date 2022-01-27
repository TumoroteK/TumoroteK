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
package fr.aphp.tumorotek.test.manager.coeur.cession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.cession.DestructionMotifManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager DestructionMotifManager.
 * Classe créée le 27/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class DestructionMotifManagerTest extends AbstractManagerTest4
{

   @Autowired
   private DestructionMotifManager destructionMotifManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public DestructionMotifManagerTest(){}

   @Test
   public void testFindById(){
      final DestructionMotif dm1 = destructionMotifManager.findByIdManager(1);
      assertNotNull(dm1);
      assertTrue(dm1.getMotif().equals("TUBE ILLISIBLE"));

      final DestructionMotif dmNull = destructionMotifManager.findByIdManager(10);
      assertNull(dmNull);
   }

   /**
    * Test la méthode findByMotifLikeManager.
    */
   @Test
   public void testFindByMotifLikeExactManager(){
      List<DestructionMotif> list = destructionMotifManager.findByMotifLikeManager("INUTILISABLE", true);
      assertTrue(list.size() == 1);

      list = destructionMotifManager.findByMotifLikeManager("INUTIL", true);
      assertTrue(list.size() == 0);

      list = destructionMotifManager.findByMotifLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByMotifLikeManager.
    */
   @Test
   public void testFindByMotifLikeManager(){
      List<DestructionMotif> list = destructionMotifManager.findByMotifLikeManager("INUTILISABLE", false);
      assertTrue(list.size() == 1);

      list = destructionMotifManager.findByMotifLikeManager("INUTIL", false);
      assertTrue(list.size() == 1);

      list = destructionMotifManager.findByMotifLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final DestructionMotif d1 = (destructionMotifManager.findByMotifLikeManager("TUBE ILLISIBLE", true)).get(0);
      assertFalse(destructionMotifManager.findDoublonManager(d1));
      d1.setMotif("INUTILISABLE");
      assertTrue(destructionMotifManager.findDoublonManager(d1));
      final DestructionMotif d2 = new DestructionMotif();
      d2.setMotif(d1.getMotif());
      d2.setPlateforme(d1.getPlateforme());
      assertTrue(d2.equals(d1));
      assertTrue(destructionMotifManager.findDoublonManager(d2));
      d2.setMotif("ROOO");
      assertFalse(destructionMotifManager.findDoublonManager(d2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final DestructionMotif d1 = (destructionMotifManager.findByMotifLikeManager("TUBE ILLISIBLE", true)).get(0);
      assertTrue(destructionMotifManager.isUsedObjectManager(d1));
      //Enregistrement n'est pas reference
      final DestructionMotif d2 = (destructionMotifManager.findByMotifLikeManager("INUTILISABLE", true)).get(0);
      assertFalse(destructionMotifManager.isUsedObjectManager(d2));
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

   @Test
   public void saveManagerTest(){
      //Insertion nouvel enregistrement
      final DestructionMotif d1 = new DestructionMotif();
      d1.setMotif("PAS BON");
      d1.setPlateforme(plateformeDao.findById(1));
      destructionMotifManager.saveManager(d1);
      assertTrue((destructionMotifManager.findByMotifLikeManager("PAS BON", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final DestructionMotif d1Bis = new DestructionMotif();
      d1Bis.setMotif("PAS BON");
      try{
         destructionMotifManager.saveManager(d1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      d1Bis.setPlateforme(plateformeDao.findById(1));
      catched = false;
      try{
         destructionMotifManager.saveManager(d1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((destructionMotifManager.findByMotifLikeManager("PAS BON", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "¤¤12.K", createOverLength(200), null};
      final DestructionMotif d2 = new DestructionMotif();
      d2.setPlateforme(plateformeDao.findById(1));
      for(int i = 0; i < emptyValues.length; i++){
         try{
            d2.setMotif(emptyValues[i]);
            destructionMotifManager.saveManager(d2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(destructionMotifManager.findByOrderManager(d1.getPlateforme()).size() == 3);
         }
      }
   }

   /**
    * Teste la methode saveManager. 
    */
   @Test
   public void saveManagerTest(){
      //Modification d'un enregistrement
      final DestructionMotif d1 = (destructionMotifManager.findByMotifLikeManager("PAS BON", true)).get(0);
      d1.setMotif("NON");
      destructionMotifManager.saveManager(d1);
      assertTrue((destructionMotifManager.findByMotifLikeManager("NON", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         d1.setMotif("INUTILISABLE");
         destructionMotifManager.saveManager(d1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((destructionMotifManager.findByMotifLikeManager("INUTILISABLE", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$_¤¤", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            d1.setMotif(emptyValues[i]);
            destructionMotifManager.saveManager(d1);
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
   @Test
   public void deleteByIdManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final DestructionMotif d1 = (destructionMotifManager.findByMotifLikeManager("NON", true)).get(0);
      destructionMotifManager.deleteByIdManager(d1);
      assertTrue((destructionMotifManager.findByMotifLikeManager("NON", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			DestructionMotif d2 = (destructionMotifManager
      //					.findByMotifLikeManager("TUBE ILLISIBLE", true))
      //					.get(0);
      //			destructionMotifManager.deleteByIdManager(d2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((destructionMotifManager.findByMotifLikeManager("TUBE ILLISIBLE", true)).size() > 0);
      //null remove
      destructionMotifManager.deleteByIdManager(null);
      assertTrue(destructionMotifManager.findByOrderManager(d1.getPlateforme()).size() == 2);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = destructionMotifManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(1).getNom().equals("TUBE ILLISIBLE"));
      pf = plateformeDao.findById(2);
      list = destructionMotifManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = destructionMotifManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
