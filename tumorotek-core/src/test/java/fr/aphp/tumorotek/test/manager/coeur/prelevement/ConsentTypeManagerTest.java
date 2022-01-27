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
package fr.aphp.tumorotek.test.manager.coeur.prelevement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.ConsentTypeManager;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ConsentTypeManager.
 * Classe créée le 05/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class ConsentTypeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ConsentTypeManager consentTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ConsentTypeManagerTest(){}

   /**
    * Test la methode findByTypeLikeManager.
    */
   @Test
   public void testFindByTypeLikeManager(){
      //teste une recherche exactMatch
      List<ConsentType> consentTypes = consentTypeManager.findByTypeLikeManager("EN ATTENTE", true);
      assertTrue(consentTypes.size() == 1);
      //teste une recherche non exactMatch
      consentTypes = consentTypeManager.findByTypeLikeManager("RECH", false);
      assertTrue(consentTypes.size() == 1);
      assertTrue(consentTypes.get(0).getType().equals("RECHERCHE"));
      //teste une recherche infructueuse
      consentTypes = consentTypeManager.findByTypeLikeManager("ACCORD", false);
      assertTrue(consentTypes.size() == 0);
      //null recherche
      consentTypes = consentTypeManager.findByTypeLikeManager(null, false);
      assertTrue(consentTypes.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final ConsentType ct1 = (consentTypeManager.findByTypeLikeManager("RECHERCHE", true)).get(0);
      assertFalse(consentTypeManager.findDoublonManager(ct1));
      final ConsentType ct2 = new ConsentType();
      ct2.setType(ct1.getType());
      ct2.setPlateforme(ct1.getPlateforme());
      assertTrue(ct2.equals(ct1));
      assertTrue(consentTypeManager.findDoublonManager(ct2));
      ct1.setType("DECEDE");
      ct1.setPlateforme(plateformeDao.findById(1));
      assertTrue(consentTypeManager.findDoublonManager(ct1));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final ConsentType ct1 = (consentTypeManager.findByTypeLikeManager("EN ATTENTE", true)).get(0);
      assertTrue(consentTypeManager.isUsedObjectManager(ct1));
      //Enregistrement n'est pas reference
      final ConsentType ct2 = (consentTypeManager.findByTypeLikeManager("RECHERCHE", true)).get(0);
      assertFalse(consentTypeManager.isUsedObjectManager(ct2));
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

   /**
    * Teste la methode saveManager. 
    */
   private void saveManagerTest(){
      //Insertion nouvel enregistrement
      final ConsentType ct1 = new ConsentType();
      ct1.setType("GENETIQUE 1.0");
      ct1.setPlateforme(plateformeDao.findById(1));
      consentTypeManager.saveManager(ct1);
      assertTrue((consentTypeManager.findByTypeLikeManager("GENETIQUE 1.0", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final ConsentType ct1Bis = new ConsentType();
      ct1Bis.setType("GENETIQUE 1.0");
      try{
         consentTypeManager.saveManager(ct1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      ct1Bis.setPlateforme(ct1.getPlateforme());
      assertTrue(catched);
      try{
         consentTypeManager.saveManager(ct1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((consentTypeManager.findByTypeLikeManager("GENETIQUE 1.0", true)).size() == 1);

      //validation test
      final String[] emptyValues = new String[] {"", "  ", "$$¢\\uy", createOverLength(200), null};
      final ConsentType ct2 = new ConsentType();
      ct2.setPlateforme(ct1.getPlateforme());
      for(int i = 0; i < emptyValues.length; i++){
         try{
            ct2.setType(emptyValues[i]);
            consentTypeManager.saveManager(ct2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(consentTypeManager.findByOrderManager(ct1.getPlateforme()).size() == 3);
         }
      }
   }

   private void saveManagerTest(){
      //Modification d'un enregistrement
      final ConsentType ct1 = (consentTypeManager.findByTypeLikeManager("GENETIQUE 1.0", true)).get(0);
      ct1.setType("ADN -");
      consentTypeManager.saveManager(ct1);
      assertTrue((consentTypeManager.findByTypeLikeManager("ADN -", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         ct1.setType("EN ATTENTE");
         consentTypeManager.saveManager(ct1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((consentTypeManager.findByTypeLikeManager("EN ATTENTE", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "¤¤$$[plk]", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         try{
            ct1.setType(emptyValues[i]);
            consentTypeManager.saveManager(ct1);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            assertTrue((consentTypeManager.findByTypeLikeManager("ADN -", true)).size() == 1);
         }
      }
   }

   private void deleteByIdManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final ConsentType ct1 = (consentTypeManager.findByTypeLikeManager("ADN -", true)).get(0);
      consentTypeManager.deleteByIdManager(ct1);
      assertTrue((consentTypeManager.findByTypeLikeManager("ADN -", true)).size() == 0);
      //Suppression engrendrant une exception
      Boolean catched = false;
      try{
         final ConsentType ct2 = (consentTypeManager.findByTypeLikeManager("EN ATTENTE", true)).get(0);
         consentTypeManager.deleteByIdManager(ct2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((consentTypeManager.findByTypeLikeManager("EN ATTENTE", true)).size() > 0);
      //null remove
      consentTypeManager.deleteByIdManager(null);
      assertTrue(consentTypeManager.findByOrderManager(ct1.getPlateforme()).size() == 2);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = consentTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("DECEDE"));
      pf = plateformeDao.findById(2);
      list = consentTypeManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = consentTypeManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
