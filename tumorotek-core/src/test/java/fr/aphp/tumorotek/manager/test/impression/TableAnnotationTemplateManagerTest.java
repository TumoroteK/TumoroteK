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
package fr.aphp.tumorotek.manager.test.impression;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.manager.impression.TableAnnotationTemplateManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

/**
 *
 * Classe de test pour le manager TableAnnotationTemplateManager.
 * Classe créée le 02/08/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TableAnnotationTemplateManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TableAnnotationTemplateManager tableAnnotationTemplateManager;

   @Autowired
   private TemplateDao templateDao;

   @Autowired
   private TableAnnotationDao tableAnnotationDao;

   public TableAnnotationTemplateManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<TableAnnotationTemplate> liste = tableAnnotationTemplateManager.findAllObjectsManager();
      assertTrue(liste.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByIdManager().
    */
   @Test
   public void testFindByIdManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final TableAnnotation ta1 = tableAnnotationDao.findById(2);
      TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(t1, ta1);

      TableAnnotationTemplate tat = tableAnnotationTemplateManager.findByIdManager(pk);
      assertNotNull(tat);

      pk = new TableAnnotationTemplatePK(t2, ta1);
      tat = tableAnnotationTemplateManager.findByIdManager(pk);
      assertNull(tat);

      tat = tableAnnotationTemplateManager.findByIdManager(null);
      assertNull(tat);
   }

   /**
    * Test l'appel de la méthode findByExcludedPKManager().
    */
   @Test
   public void testFindByExcludedPKManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final TableAnnotation ta1 = tableAnnotationDao.findById(2);
      TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(t1, ta1);

      List<TableAnnotationTemplate> liste = tableAnnotationTemplateManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 0);

      pk = new TableAnnotationTemplatePK(t2, ta1);
      liste = tableAnnotationTemplateManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 1);

      liste = tableAnnotationTemplateManager.findByExcludedPKManager(null);
      assertTrue(liste.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByTemplateManager().
    */
   @Test
   public void testFindByTemplateManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);

      List<TableAnnotationTemplate> liste = tableAnnotationTemplateManager.findByTemplateManager(t1);
      assertTrue(liste.size() == 1);

      liste = tableAnnotationTemplateManager.findByTemplateManager(t2);
      assertTrue(liste.size() == 0);

      liste = tableAnnotationTemplateManager.findByTemplateManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la méthode findDoublonManager.
    */
   @Test
   public void testFindDoublonManager(){

      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final TableAnnotation ta2 = tableAnnotationDao.findById(2);
      final TableAnnotation ta3 = tableAnnotationDao.findById(3);

      assertTrue(tableAnnotationTemplateManager.findDoublonManager(t1, ta2));
      assertFalse(tableAnnotationTemplateManager.findDoublonManager(t2, ta2));
      assertFalse(tableAnnotationTemplateManager.findDoublonManager(t1, ta3));

      assertFalse(tableAnnotationTemplateManager.findDoublonManager(null, ta2));
      assertFalse(tableAnnotationTemplateManager.findDoublonManager(t1, null));
      assertFalse(tableAnnotationTemplateManager.findDoublonManager(null, null));

   }

   /**
    * Test la méthode validateObjectManager.
    */
   @Test
   public void testValidateObjectManager(){
      final Template t1 = templateDao.findById(1);
      final TableAnnotation ta2 = tableAnnotationDao.findById(2);

      Boolean catched = false;
      // on test l'insertion avec le template null
      try{
         tableAnnotationTemplateManager.validateObjectManager(null, ta2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec la tableannotation null
      try{
         tableAnnotationTemplateManager.validateObjectManager(t1, null);
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
         tableAnnotationTemplateManager.validateObjectManager(t1, ta2);
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
      final TableAnnotation ta2 = tableAnnotationDao.findById(2);
      final TableAnnotation ta3 = tableAnnotationDao.findById(3);

      final TableAnnotationTemplate tat1 = new TableAnnotationTemplate();
      tat1.setOrdre(6);

      Boolean catched = false;
      // on test l'insertion avec la tableannotation null
      try{
         tableAnnotationTemplateManager.createObjectManager(tat1, t1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);
      catched = false;

      // on test l'insertion avec le template null
      try{
         tableAnnotationTemplateManager.createObjectManager(tat1, null, ta2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         tableAnnotationTemplateManager.createObjectManager(tat1, t1, ta2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);
      catched = false;

      // test d'une insertion valide
      tableAnnotationTemplateManager.createObjectManager(tat1, t1, ta3);
      final TableAnnotationTemplatePK pk = tat1.getPk();
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 2);

      final TableAnnotationTemplate tatTest = tableAnnotationTemplateManager.findByIdManager(pk);
      assertNotNull(tatTest);
      assertTrue(tatTest.getTemplate().equals(t1));
      assertTrue(tatTest.getTableAnnotation().equals(ta3));
      assertTrue(tatTest.getOrdre() == 6);

      // MAJ
      try{
         tableAnnotationTemplateManager.updateObjectManager(tatTest, t1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 2);
      catched = false;

      try{
         tableAnnotationTemplateManager.updateObjectManager(tatTest, null, ta2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 2);
      catched = false;

      // Maj valide
      tatTest.setOrdre(10);
      tableAnnotationTemplateManager.updateObjectManager(tatTest, t1, ta3);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 2);

      final TableAnnotationTemplate tatTest2 = tableAnnotationTemplateManager.findByIdManager(pk);
      assertNotNull(tatTest2);
      assertTrue(tatTest2.getTemplate().equals(t1));
      assertTrue(tatTest2.getTableAnnotation().equals(ta3));
      assertTrue(tatTest2.getOrdre() == 10);

      // suppression du profilUtilisateur ajouté
      tableAnnotationTemplateManager.removeObjectManager(tatTest);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);

      // suppression d'un profilUtilisateur null
      tableAnnotationTemplateManager.removeObjectManager(null);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);

      // suppression d'un profilUtilisateur inexistant
      tableAnnotationTemplateManager.removeObjectManager(tatTest);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);
   }

}
