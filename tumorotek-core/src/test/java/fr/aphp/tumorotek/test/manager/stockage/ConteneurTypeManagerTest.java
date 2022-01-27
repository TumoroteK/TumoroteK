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
package fr.aphp.tumorotek.test.manager.stockage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.stockage.ConteneurTypeManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ConteneurTypeManager.
 * Classe créée le 17/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ConteneurTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ConteneurTypeManager conteneurTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ConteneurTypeManagerTest(){}

   @Test
   public void testFindById(){
      final ConteneurType ct1 = conteneurTypeManager.findByIdManager(1);
      assertNotNull(ct1);
      assertTrue(ct1.getNom().equals("CONGELATEUR"));

      final ConteneurType csNull = conteneurTypeManager.findByIdManager(10);
      assertNull(csNull);
   }

   //	/**
   //	 * Test la méthode findAllObjects.
   //	 */
   //	@Test
   //	public void testFindAll() {
   //		List<ConteneurType> list = conteneurTypeManager.findAllObjectsManager();
   //		assertTrue(list.size() == 3);
   //		assertTrue(list.get(1).getNom().equals("CRYOCONSERVATEUR"));
   //	}

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final ConteneurType c1 = conteneurTypeManager.findByIdManager(1);
      assertFalse(conteneurTypeManager.findDoublonManager(c1));
      c1.setNom("CRYOCONSERVATEUR");
      assertTrue(conteneurTypeManager.findDoublonManager(c1));
      final ConteneurType c2 = new ConteneurType();
      c2.setNom(c1.getNom());
      c2.setPlateforme(c1.getPlateforme());
      assertTrue(c2.equals(c1));
      assertTrue(conteneurTypeManager.findDoublonManager(c2));
      c2.setNom("ROOO");
      assertFalse(conteneurTypeManager.findDoublonManager(c2));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObjectManager(){
      //Enregistrement est reference
      final ConteneurType c1 = conteneurTypeManager.findByIdManager(1);
      assertTrue(conteneurTypeManager.isUsedObjectManager(c1));
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
      final ConteneurType c1 = new ConteneurType();
      c1.setNom("NEW");
      c1.setPlateforme(plateformeDao.findById(1));
      conteneurTypeManager.saveManager(c1);
      assertTrue(conteneurTypeManager.findByOrderManager(c1.getPlateforme()).size() == 4);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final ConteneurType c1Bis = new ConteneurType();
      c1Bis.setNom("NEW");
      try{
         conteneurTypeManager.saveManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      c1Bis.setPlateforme(c1.getPlateforme());
      assertTrue(catched);
      catched = false;
      try{
         conteneurTypeManager.saveManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(conteneurTypeManager.findByOrderManager(c1.getPlateforme()).size() == 4);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "=¤¤12$$.K", createOverLength(200), null};
      final ConteneurType c2 = new ConteneurType();
      c2.setPlateforme(plateformeDao.findById(1));
      for(int i = 0; i < emptyValues.length; i++){
         try{
            c2.setNom(emptyValues[i]);
            conteneurTypeManager.saveManager(c2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(conteneurTypeManager.findByOrderManager(c1.getPlateforme()).size() == 4);
         }
      }
   }

   private void saveManagerTest(){
      //Modification d'un enregistrement
      final ConteneurType c1 = conteneurTypeManager.findByIdManager(4);
      c1.setNom("NEW BIS");
      conteneurTypeManager.saveManager(c1);
      final ConteneurType c1Bis = conteneurTypeManager.findByIdManager(4);
      assertTrue(c1Bis.getNom().equals("NEW BIS"));
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         c1Bis.setNom("CONGELATEUR");
         conteneurTypeManager.saveManager(c1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "plk$¤¤$_", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            c1Bis.setNom(emptyValues[i]);
            conteneurTypeManager.saveManager(c1Bis);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            catched = true;
         }
         assertTrue(catched);
      }
   }

   private void deleteByIdManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final ConteneurType c1 = conteneurTypeManager.findByIdManager(4);
      conteneurTypeManager.deleteByIdManager(c1);
      assertTrue(conteneurTypeManager.findByOrderManager(c1.getPlateforme()).size() == 3);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			ConteneurType c2 = conteneurTypeManager.findByIdManager(1);
      //			conteneurTypeManager.deleteByIdManager(c2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      //null remove
      conteneurTypeManager.deleteByIdManager(null);
      assertTrue(conteneurTypeManager.findByOrderManager(c1.getPlateforme()).size() == 3);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = conteneurTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(1).getNom().equals("CRYOCONSERVATEUR"));
      pf = plateformeDao.findById(2);
      list = conteneurTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 0);
      list = conteneurTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }

}
