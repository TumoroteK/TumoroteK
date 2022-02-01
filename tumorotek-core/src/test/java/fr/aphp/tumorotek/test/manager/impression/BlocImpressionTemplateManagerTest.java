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
package fr.aphp.tumorotek.test.manager.impression;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.manager.impression.BlocImpressionTemplateManager;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager BlocImpressionTemplateManager.
 * Classe créée le 23/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class BlocImpressionTemplateManagerTest extends AbstractManagerTest4
{

   @Autowired
   private BlocImpressionTemplateManager blocImpressionTemplateManager;
   @Autowired
   private TemplateDao templateDao;
   @Autowired
   private BlocImpressionDao blocImpressionDao;

   public BlocImpressionTemplateManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<BlocImpressionTemplate> liste = blocImpressionTemplateManager.findAllObjectsManager();
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByIdManager().
    */
   @Test
   public void testFindByIdManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final BlocImpression b1 = blocImpressionDao.findById(1);
      BlocImpressionTemplatePK pk = new BlocImpressionTemplatePK(t1, b1);

      BlocImpressionTemplate bt = blocImpressionTemplateManager.findByIdManager(pk);
      assertNotNull(bt);

      pk = new BlocImpressionTemplatePK(t2, b1);
      bt = blocImpressionTemplateManager.findByIdManager(pk);
      assertNull(bt);

      bt = blocImpressionTemplateManager.findByIdManager(null);
      assertNull(bt);
   }

   /**
    * Test l'appel de la méthode findByExcludedPKManager().
    */
   @Test
   public void testFindByExcludedPKManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final BlocImpression b1 = blocImpressionDao.findById(1);
      BlocImpressionTemplatePK pk = new BlocImpressionTemplatePK(t1, b1);

      List<BlocImpressionTemplate> liste = blocImpressionTemplateManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 3);

      pk = new BlocImpressionTemplatePK(t2, b1);
      liste = blocImpressionTemplateManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 4);

      liste = blocImpressionTemplateManager.findByExcludedPKManager(null);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByTemplateManager().
    */
   @Test
   public void testFindByTemplateManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);

      List<BlocImpressionTemplate> liste = blocImpressionTemplateManager.findByTemplateManager(t1);
      assertTrue(liste.size() == 4);

      liste = blocImpressionTemplateManager.findByTemplateManager(t2);
      assertTrue(liste.size() == 0);

      liste = blocImpressionTemplateManager.findByTemplateManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la méthode findDoublonManager.
    */
   @Test
   public void testFindDoublonManager(){

      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final BlocImpression b1 = blocImpressionDao.findById(1);
      final BlocImpression b6 = blocImpressionDao.findById(6);

      assertTrue(blocImpressionTemplateManager.findDoublonManager(t1, b1));
      assertFalse(blocImpressionTemplateManager.findDoublonManager(t2, b1));
      assertFalse(blocImpressionTemplateManager.findDoublonManager(t1, b6));

      assertFalse(blocImpressionTemplateManager.findDoublonManager(null, b1));
      assertFalse(blocImpressionTemplateManager.findDoublonManager(t1, null));
      assertFalse(blocImpressionTemplateManager.findDoublonManager(null, null));

   }

   /**
    * Test la méthode validateObjectManager.
    */
   @Test
   public void testValidateObjectManager(){
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(1);

      Boolean catched = false;
      // on test l'insertion avec le template null
      try{
         blocImpressionTemplateManager.validateObjectManager(null, b1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec le bloc null
      try{
         blocImpressionTemplateManager.validateObjectManager(t1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         blocImpressionTemplateManager.validateObjectManager(t1, b1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
   }

   /**
    * Test la méthode CRUD.
    */
   @Test
   public void testCrud(){
      final Template t1 = templateDao.findById(1);
      final BlocImpression b1 = blocImpressionDao.findById(1);
      final BlocImpression b4 = blocImpressionDao.findById(4);

      final BlocImpressionTemplate bit1 = new BlocImpressionTemplate();
      bit1.setOrdre(5);

      Boolean catched = false;
      // on test l'insertion avec le blocimpression null
      try{
         blocImpressionTemplateManager.saveManager(bit1, t1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
      catched = false;

      // on test l'insertion avec le template null
      try{
         blocImpressionTemplateManager.saveManager(bit1, null, b1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         blocImpressionTemplateManager.saveManager(bit1, t1, b1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
      catched = false;

      // test d'une insertion valide
      blocImpressionTemplateManager.saveManager(bit1, t1, b4);
      final BlocImpressionTemplatePK pk = bit1.getPk();
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 5);

      final BlocImpressionTemplate bitTest = blocImpressionTemplateManager.findByIdManager(pk);
      assertNotNull(bitTest);
      assertTrue(bitTest.getTemplate().equals(t1));
      assertTrue(bitTest.getBlocImpression().equals(b4));
      assertTrue(bitTest.getOrdre() == 5);

      // test maj
      try{
         blocImpressionTemplateManager.saveManager(bitTest, t1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 5);
      catched = false;

      try{
         blocImpressionTemplateManager.saveManager(bitTest, null, b1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 5);
      catched = false;

      // maj valide
      bitTest.setOrdre(10);
      blocImpressionTemplateManager.saveManager(bitTest, bitTest.getTemplate(), bitTest.getBlocImpression());
      final BlocImpressionTemplate bitTest2 = blocImpressionTemplateManager.findByIdManager(pk);
      assertNotNull(bitTest2);
      assertTrue(bitTest2.getTemplate().equals(t1));
      assertTrue(bitTest2.getBlocImpression().equals(b4));
      assertTrue(bitTest2.getOrdre() == 10);

      // suppression du BlocImpressionTemplate ajouté
      blocImpressionTemplateManager.removeObjectManager(bitTest);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);

      // suppression d'un BlocImpressionTemplate null
      blocImpressionTemplateManager.removeObjectManager(null);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);

      // suppression d'un BlocImpressionTemplate inexistant
      blocImpressionTemplateManager.removeObjectManager(bitTest);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
   }
}
