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
import fr.aphp.tumorotek.manager.coeur.prelevement.ConditMilieuManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le manager ConditMilieuManager.
 * Classe créée le 02/10/09.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class ConditMilieuManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ConditMilieuManager conditMilieuManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ConditMilieuManagerTest(){}

   /**
    * Test la methode findByMilieuLikeManager.
    */
   @Test
   public void testFindByMilieuLikeManager(){
      //teste une recherche exactMatch
      List<ConditMilieu> conditMilieux = conditMilieuManager.findByMilieuLikeManager("SEC", true);
      assertTrue(conditMilieux.size() == 1);
      //teste une recherche non exactMatch
      conditMilieux = conditMilieuManager.findByMilieuLikeManager("HEP", false);
      assertTrue(conditMilieux.size() == 1);
      assertTrue(conditMilieux.get(0).getNom().equals("HEPARINE"));
      //teste une recherche infructueuse
      conditMilieux = conditMilieuManager.findByMilieuLikeManager("TRIZOL", false);
      assertTrue(conditMilieux.size() == 0);
      //null recherche
      conditMilieux = conditMilieuManager.findByMilieuLikeManager(null, false);
      assertTrue(conditMilieux.size() == 0);
   }

   /**
    * Teste la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final ConditMilieu cm1 = (conditMilieuManager.findByMilieuLikeManager("SEC", true)).get(0);
      assertFalse(conditMilieuManager.findDoublonManager(cm1));
      final ConditMilieu cm2 = new ConditMilieu();
      cm2.setNom(cm1.getNom());
      cm2.setPlateforme(cm1.getPlateforme());
      assertTrue(cm2.equals(cm1));
      assertTrue(conditMilieuManager.findDoublonManager(cm2));
      cm1.setNom("HEPARINE");
      cm1.setPlateforme(plateformeDao.findById(2));
      assertTrue(conditMilieuManager.findDoublonManager(cm1));
   }

   /**
    * Teste la méthode isUsedObject.
    */
   @Test
   public void testIsUsedObject(){
      //Enregistrement est reference
      final ConditMilieu cm1 = (conditMilieuManager.findByMilieuLikeManager("SEC", true)).get(0);
      assertTrue(conditMilieuManager.isUsedObjectManager(cm1));
      //Enregistrement n'est pas reference
      final ConditMilieu cm2 = (conditMilieuManager.findByMilieuLikeManager("HEPARINE", true)).get(0);
      assertFalse(conditMilieuManager.isUsedObjectManager(cm2));
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
      final ConditMilieu cm1 = new ConditMilieu();
      cm1.setNom("EDTA-");
      cm1.setPlateforme(plateformeDao.findById(1));
      conditMilieuManager.createObjectManager(cm1);
      assertTrue((conditMilieuManager.findByMilieuLikeManager("EDTA-", true)).size() == 1);
      //Insertion d'un doublon engendrant une exception
      Boolean catched = false;
      final ConditMilieu cm1Bis = new ConditMilieu();
      cm1Bis.setNom("EDTA-");
      try{
         conditMilieuManager.createObjectManager(cm1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      cm1Bis.setPlateforme(cm1.getPlateforme());
      catched = false;
      try{
         conditMilieuManager.createObjectManager(cm1Bis);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((conditMilieuManager.findByMilieuLikeManager("EDTA-", true)).size() == 1);

      //validation test
      final String[] emptyValues = new String[] {"", "  ", "lalo¤¤$", createOverLength(200), null};
      final ConditMilieu cm2 = new ConditMilieu();
      cm2.setPlateforme(cm1.getPlateforme());
      for(int i = 0; i < emptyValues.length; i++){
         try{
            cm2.setNom(emptyValues[i]);
            conditMilieuManager.createObjectManager(cm2);
         }catch(final ValidationException e){
            //verifie qu'aucune ligne n'a ete ajoutee
            assertTrue(conditMilieuManager.findByOrderManager(cm1.getPlateforme()).size() == 2);
         }
      }
   }

   private void updateObjectManagerTest(){
      //Modification d'un enregistrement
      final ConditMilieu cm1 = (conditMilieuManager.findByMilieuLikeManager("EDTA-", true)).get(0);
      cm1.setNom("Triz.zol +");
      conditMilieuManager.updateObjectManager(cm1);
      assertTrue((conditMilieuManager.findByMilieuLikeManager("Triz.zol +", true)).size() == 1);
      //Modification en un doublon engendrant une exception
      Boolean catched = false;
      try{
         cm1.setNom("SEC");
         conditMilieuManager.updateObjectManager(cm1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue((conditMilieuManager.findByMilieuLikeManager("HEPARINE", true)).size() == 1);

      //Validation test
      final String[] emptyValues = new String[] {"", "  ", "$$lk#¤¤", createOverLength(200), null};
      for(int i = 0; i < emptyValues.length; i++){
         try{
            cm1.setNom(emptyValues[i]);
            conditMilieuManager.updateObjectManager(cm1);
         }catch(final ValidationException e){
            //verifie que l'enregistrement n'a pas ete modifie
            assertTrue((conditMilieuManager.findByMilieuLikeManager("Triz.zol +", true)).size() == 1);
         }
      }
   }

   private void removeObjectManagerTest(){
      //Suppression de l'enregistrement precedemment insere
      final ConditMilieu cm1 = (conditMilieuManager.findByMilieuLikeManager("Triz.zol +", true)).get(0);
      conditMilieuManager.removeObjectManager(cm1);
      assertTrue((conditMilieuManager.findByMilieuLikeManager("Triz.zol +", true)).size() == 0);
      //Suppression engrendrant une exception
      //		Boolean catched = false;
      //		try {
      //			ConditMilieu cm2 = (conditMilieuManager
      //					.findByMilieuLikeManager("SEC", true)).get(0);
      //			conditMilieuManager.removeObjectManager(cm2);
      //		} catch (Exception e) {
      //			if (e.getClass().getSimpleName().equals(
      //					"ObjectUsedException")) {
      //				catched = true;
      //			}
      //		}
      //		assertTrue(catched);
      assertTrue((conditMilieuManager.findByMilieuLikeManager("SEC", true)).size() > 0);
      //null remove
      conditMilieuManager.removeObjectManager(null);
      assertTrue(conditMilieuManager.findByOrderManager(cm1.getPlateforme()).size() == 1);
   }

   @Test
   public void testFindByOrderManager(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = conditMilieuManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("SEC"));
      pf = plateformeDao.findById(2);
      list = conditMilieuManager.findByOrderManager(pf);
      assertTrue(list.size() == 1);
      list = conditMilieuManager.findByOrderManager(null);
      assertTrue(list.size() == 0);
   }
}
