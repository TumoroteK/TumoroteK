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
package fr.aphp.tumorotek.manager.test.coeur.prelevement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConditTypeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager ConditTypeManager.
 * Classe créée le 02/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class ConditTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ConditTypeManager conditTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ConditTypeManagerTest(){}

   /**
    * Test la methode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeManager(){
      //teste une recherche exactMatch
      List<ConditType> conditTypes = conditTypeManager.findByTypeLikeManager("TUBE", true);
      assertTrue(conditTypes.size() == 1);
      //teste une recherche non exactMatch
      conditTypes = conditTypeManager.findByTypeLikeManager("P", false);
      assertTrue(conditTypes.size() == 1);
      assertTrue(conditTypes.get(0).getNom().equals("POUDRIER"));
      //teste une recherche infructueuse
      conditTypes = conditTypeManager.findByTypeLikeManager("V", false);
      assertTrue(conditTypes.size() == 0);
      //null recherche
      conditTypes = conditTypeManager.findByTypeLikeManager(null, false);
      assertTrue(conditTypes.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final ConditType ct1 = (conditTypeManager.findByTypeLikeManager("TUBE", true)).get(0);
      assertFalse(conditTypeManager.findDoublonManager(ct1));
      final ConditType ct2 = new ConditType();
      ct2.setNom(ct1.getNom());
      ct2.setPlateforme(ct1.getPlateforme());
      assertTrue(ct2.equals(ct1));
      assertTrue(conditTypeManager.findDoublonManager(ct2));
      ct1.setNom("POUDRIER");
      ct1.setPlateforme(plateformeDao.findById(2));
      assertTrue(conditTypeManager.findDoublonManager(ct1));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final ConditType ct1 = (conditTypeManager.findByTypeLikeManager("TUBE", true)).get(0);
      assertTrue(conditTypeManager.isUsedObjectManager(ct1));
      //Enregistrement n'est pas reference
      final ConditType ct2 = (conditTypeManager.findByTypeLikeManager("POUDRIER", true)).get(0);
      assertFalse(conditTypeManager.isUsedObjectManager(ct2));
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
      final ConditType ct1 = new ConditType();
      ct1.setNom("PILLULIER 1.-");
      ct1.setPlateforme(plateformeDao.findById(1));
      conditTypeManager.createObjectManager(ct1);
      assertTrue((conditTypeManager.findByTypeLikeManager("PILLULIER 1.-", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final ConditType ct1Bis = new ConditType();
      ct1Bis.setNom("PILLULIER 1.-");
      try{
         conditTypeManager.createObjectManager(ct1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      ct1Bis.setPlateforme(ct1.getPlateforme());
      assertTrue(catched);
      try{
         conditTypeManager.createObjectManager(ct1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((conditTypeManager.findByTypeLikeManager("PILLULIER 1.-", true)).size() == 1);

      //validation test
      final String[] emptyValues = new String[] {"", "  ", "¢€€lou{", createOverLength(200), null};
      final ConditType ct2 = new ConditType();
      ct2.setPlateforme(ct1.getPlateforme());
      for(int i = 0; i < emptyValues.length; i++){
         try{
            ct2.setNom(emptyValues[i]);
            conditTypeManager.createObjectManager(ct2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(conditTypeManager.findByOrderManager(ct1.getPlateforme()).size() == 2);
         }
      }
   }

   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final ConditType ct1 = (conditTypeManager.findByTypeLikeManager("PILLULIER 1.-", true)).get(0);
      ct1.setNom("EPPEN-DORF");
      conditTypeManager.updateObjectManager(ct1);
      assertTrue((conditTypeManager.findByTypeLikeManager("EPPEN-DORF", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         ct1.setNom("POUDRIER");
         ct1.setPlateforme(plateformeDao.findById(2));
         conditTypeManager.updateObjectManager(ct1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((conditTypeManager.findByTypeLikeManager("POUDRIER", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "¢¢$$anti^", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         try{
            ct1.setNom(emptyValues[i]);
            conditTypeManager.updateObjectManager(ct1);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            assertTrue((conditTypeManager.findByTypeLikeManager("EPPEN-DORF", true)).size() == 1);
         }
      }
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final ConditType ct1 = (conditTypeManager.findByTypeLikeManager("EPPEN-DORF", true)).get(0);
      conditTypeManager.removeObjectManager(ct1);
      assertTrue((conditTypeManager.findByTypeLikeManager("EPPEN-DORF", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			ConditType ct2 = (conditTypeManager
      //					.findByTypeLikeManager("TUBE", true)).get(0);
      //			conditTypeManager.removeObjectManager(ct2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((conditTypeManager.findByTypeLikeManager("TUBE", true)).size() > 0);
      //null remove
      conditTypeManager.removeObjectManager(null);
      assertTrue(conditTypeManager.findByOrderManager(ct1.getPlateforme()).size() == 1);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = conditTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("TUBE"));
      pf = plateformeDao.findById(2);
      list = conditTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = conditTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
