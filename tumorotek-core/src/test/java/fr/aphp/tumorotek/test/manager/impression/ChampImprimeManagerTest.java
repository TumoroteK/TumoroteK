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
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.impression.ChampImprimeManager;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.ChampImprimePK;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ChampImprimeManager.
 * Classe créée le 26/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ChampImprimeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ChampImprimeManager champImprimeManager;
   @Autowired
   private TemplateDao templateDao;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private BlocImpressionDao blocImpressionDao;

   public ChampImprimeManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<ChampImprime> liste = champImprimeManager.findAllObjectsManager();
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByIdManager().
    */
   @Test
   public void testFindByIdManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final ChampEntite ce = champEntiteDao.findById(54);
      final BlocImpression bi = blocImpressionDao.findById(5);
      ChampImprimePK pk = new ChampImprimePK(t1, ce, bi);

      ChampImprime bt = champImprimeManager.findByIdManager(pk);
      assertNotNull(bt);

      pk = new ChampImprimePK(t2, ce, bi);
      bt = champImprimeManager.findByIdManager(pk);
      assertNull(bt);

      bt = champImprimeManager.findByIdManager(null);
      assertNull(bt);
   }

   /**
    * Test l'appel de la méthode findByExcludedPKManager().
    */
   @Test
   public void testFindByExcludedPKManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final ChampEntite ce = champEntiteDao.findById(54);
      final BlocImpression bi = blocImpressionDao.findById(5);
      ChampImprimePK pk = new ChampImprimePK(t1, ce, bi);

      List<ChampImprime> liste = champImprimeManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 3);

      pk = new ChampImprimePK(t2, ce, bi);
      liste = champImprimeManager.findByExcludedPKManager(pk);
      assertTrue(liste.size() == 4);

      liste = champImprimeManager.findByExcludedPKManager(null);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByTemplateManager().
    */
   @Test
   public void testFindByTemplateManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);

      List<ChampImprime> liste = champImprimeManager.findByTemplateManager(t1);
      assertTrue(liste.size() == 4);

      liste = champImprimeManager.findByTemplateManager(t2);
      assertTrue(liste.size() == 0);

      liste = champImprimeManager.findByTemplateManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTemplateAndBlocManager().
    */
   @Test
   public void testFindByTemplateAndBlocManager(){
      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final BlocImpression bi5 = blocImpressionDao.findById(5);
      final BlocImpression bi1 = blocImpressionDao.findById(1);

      List<ChampImprime> liste = champImprimeManager.findByTemplateAndBlocManager(t1, bi5);
      assertTrue(liste.size() == 4);

      liste = champImprimeManager.findByTemplateAndBlocManager(t2, bi5);
      assertTrue(liste.size() == 0);

      liste = champImprimeManager.findByTemplateAndBlocManager(t1, bi1);
      assertTrue(liste.size() == 0);

      liste = champImprimeManager.findByTemplateAndBlocManager(null, bi5);
      assertTrue(liste.size() == 0);

      liste = champImprimeManager.findByTemplateAndBlocManager(t1, null);
      assertTrue(liste.size() == 0);

      liste = champImprimeManager.findByTemplateAndBlocManager(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la méthode findDoublonManager.
    */
   @Test
   public void testFindDoublonManager(){

      final Template t1 = templateDao.findById(1);
      final Template t2 = templateDao.findById(2);
      final ChampEntite ce1 = champEntiteDao.findById(54);
      final ChampEntite ce2 = champEntiteDao.findById(2);
      final BlocImpression bi5 = blocImpressionDao.findById(5);
      final BlocImpression bi1 = blocImpressionDao.findById(1);

      assertTrue(champImprimeManager.findDoublonManager(t1, ce1, bi5));
      assertFalse(champImprimeManager.findDoublonManager(t2, ce1, bi5));
      assertFalse(champImprimeManager.findDoublonManager(t1, ce2, bi5));
      assertFalse(champImprimeManager.findDoublonManager(t1, ce1, bi1));

      assertFalse(champImprimeManager.findDoublonManager(null, ce1, bi5));
      assertFalse(champImprimeManager.findDoublonManager(t1, null, bi5));
      assertFalse(champImprimeManager.findDoublonManager(t1, ce1, null));
      assertFalse(champImprimeManager.findDoublonManager(null, null, null));

   }

   /**
    * Test la méthode validateObjectManager.
    */
   @Test
   public void testValidateObjectManager(){
      final Template t1 = templateDao.findById(1);
      final ChampEntite ce1 = champEntiteDao.findById(54);
      final BlocImpression bi5 = blocImpressionDao.findById(5);

      Boolean catched = false;
      // on test l'insertion avec le template null
      try{
         champImprimeManager.validateObjectManager(null, ce1, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec le champentite null
      try{
         champImprimeManager.validateObjectManager(t1, null, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec le bloc null
      try{
         champImprimeManager.validateObjectManager(t1, ce1, null);
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
         champImprimeManager.validateObjectManager(t1, ce1, bi5);
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
      final ChampEntite ce54 = champEntiteDao.findById(54);
      final ChampEntite ce2 = champEntiteDao.findById(2);
      final BlocImpression bi5 = blocImpressionDao.findById(5);

      final ChampImprime ci = new ChampImprime();
      ci.setOrdre(5);

      Boolean catched = false;
      // on test l'insertion avec le champentite null
      try{
         champImprimeManager.saveManager(ci, t1, null, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      catched = false;

      // on test l'insertion avec le template null
      try{
         champImprimeManager.saveManager(ci, null, ce54, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      catched = false;

      // on test l'insertion avec le bloc null
      try{
         champImprimeManager.saveManager(ci, t1, ce54, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         champImprimeManager.saveManager(ci, t1, ce54, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      catched = false;

      // test d'une insertion valide
      champImprimeManager.saveManager(ci, t1, ce2, bi5);
      final ChampImprimePK pk = ci.getPk();
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 5);

      final ChampImprime ciTest = champImprimeManager.findByIdManager(pk);
      assertNotNull(ciTest);
      assertTrue(ciTest.getTemplate().equals(t1));
      assertTrue(ciTest.getChampEntite().equals(ce2));
      assertTrue(ciTest.getBlocImpression().equals(bi5));
      assertTrue(ciTest.getOrdre() == 5);

      // MAJ
      try{
         champImprimeManager.saveManager(ciTest, t1, null, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 5);
      catched = false;

      // on test l'insertion avec le template null
      try{
         champImprimeManager.saveManager(ciTest, null, ce54, bi5);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 5);
      catched = false;

      // on test l'insertion avec le bloc null
      try{
         champImprimeManager.saveManager(ciTest, t1, ce54, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 5);
      catched = false;

      // MAJ valide
      ciTest.setOrdre(10);
      champImprimeManager.saveManager(ciTest, t1, ce2, bi5);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 5);

      final ChampImprime ciTest2 = champImprimeManager.findByIdManager(pk);
      assertNotNull(ciTest2);
      assertTrue(ciTest2.getTemplate().equals(t1));
      assertTrue(ciTest2.getChampEntite().equals(ce2));
      assertTrue(ciTest2.getBlocImpression().equals(bi5));
      assertTrue(ciTest2.getOrdre() == 10);

      // suppression du profilUtilisateur ajouté
      champImprimeManager.deleteByIdManager(ciTest);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);

      // suppression d'un profilUtilisateur null
      champImprimeManager.deleteByIdManager(null);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);

      // suppression d'un profilUtilisateur inexistant
      champImprimeManager.deleteByIdManager(ciTest);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
   }

}
