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
package fr.aphp.tumorotek.manager.test.imprimante;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.imprimante.LigneEtiquetteDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le manager ChampLigneEtiquetteManager.
 * Classe créée le 08/06/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0.13.2
 *
 */
public class ChampLigneEtiquetteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ChampLigneEtiquetteManager champLigneEtiquetteManager;
   @Autowired
   private LigneEtiquetteDao ligneEtiquetteDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private ChampManager champManager;
   @Autowired
   private ChampEntiteDao champEntiteDao;

   public ChampLigneEtiquetteManagerTest(){

   }

   @Test
   public void testFindById(){
      final ChampLigneEtiquette cle = champLigneEtiquetteManager.findByIdManager(1);
      assertNotNull(cle);

      final ChampLigneEtiquette cleNull = champLigneEtiquetteManager.findByIdManager(1000);
      assertNull(cleNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<ChampLigneEtiquette> list = champLigneEtiquetteManager.findAllObjectsManager();
      assertTrue(list.size() == 10);
   }

   /**
    * Test la méthode findByLigneEtiquetteManager.
    */
   @Test
   public void testFindByLigneEtiquetteManager(){
      final LigneEtiquette li1 = ligneEtiquetteDao.findById(1);
      final LigneEtiquette li7 = ligneEtiquetteDao.findById(7);

      List<ChampLigneEtiquette> list = champLigneEtiquetteManager.findByLigneEtiquetteManager(li1);
      assertTrue(list.size() == 2);

      list = champLigneEtiquetteManager.findByLigneEtiquetteManager(li7);
      assertTrue(list.size() == 0);

      list = champLigneEtiquetteManager.findByLigneEtiquetteManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByLigneEtiquetteAndEntiteManager.
    */
   @Test
   public void testFindByLigneEtiquetteAndEntiteManager(){
      final LigneEtiquette li1 = ligneEtiquetteDao.findById(1);
      final LigneEtiquette li7 = ligneEtiquetteDao.findById(7);
      final Entite e3 = entiteDao.findById(3);
      final Entite e4 = entiteDao.findById(4);

      List<ChampLigneEtiquette> list = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(li1, e3);
      assertTrue(list.size() == 1);

      list = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(li1, e4);
      assertTrue(list.size() == 0);

      list = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(li7, e3);
      assertTrue(list.size() == 0);

      list = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(null, e3);
      assertTrue(list.size() == 0);

      list = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(li7, null);
      assertTrue(list.size() == 0);

      list = champLigneEtiquetteManager.findByLigneEtiquetteAndEntiteManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test de la méthode validateObjectManager().
    */
   @Test
   public void testValidateObjectManager(){
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      final Champ c1 = champManager.findByIdManager(1);
      final Entite e3 = entiteDao.findById(4);
      final ChampLigneEtiquette cle = new ChampLigneEtiquette();
      cle.setOrdre(1);
      cle.setExpReg("TEST");

      Boolean catched = false;
      // on test avec l'importtemplate null
      try{
         champLigneEtiquetteManager.validateObjectManager(cle, null, e3, c1, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test avec le champ null
      try{
         champLigneEtiquetteManager.validateObjectManager(cle, le1, null, c1, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test avec le champ null
      try{
         champLigneEtiquetteManager.validateObjectManager(cle, le1, e3, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test avec le champ null
      cle.setOrdre(null);
      try{
         champLigneEtiquetteManager.validateObjectManager(cle, le1, e3, c1, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
   }

   @Test
   public void testCrud(){
      createObjectManager();
      updateObjectManager();
   }

   @Test
   public void createObjectManager(){
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      final Entite e3 = entiteDao.findById(3);
      final Champ champ = new Champ();
      final ChampEntite ce = champEntiteDao.findById(1);

      final ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
      cle1.setOrdre(2);
      cle1.setExpReg("EXP");

      final int cTots = champManager.findAllObjectsManager().size();

      // on vérifie qu'une exception est bien lancée
      Boolean catched = false;
      // on test avec la ligne null
      try{
         champLigneEtiquetteManager.createObjectManager(cle1, null, e3, champ);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 10);
      assertTrue(champManager.findAllObjectsManager().size() == cTots);

      // insertion avec un nouveau champ
      champ.setChampEntite(ce);
      champLigneEtiquetteManager.createObjectManager(cle1, le1, e3, champ);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 11);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 1);
      final Integer idCle1 = cle1.getChampLigneEtiquetteId();

      final ChampLigneEtiquette cle2 = champLigneEtiquetteManager.findByIdManager(idCle1);
      assertNotNull(cle2);
      assertTrue(cle2.getLigneEtiquette().equals(le1));
      assertTrue(cle2.getChamp().equals(champ));
      assertTrue(cle2.getEntite().equals(e3));
      assertTrue(cle2.getExpReg().equals("EXP"));
      assertTrue(cle2.getOrdre() == 2);

      champLigneEtiquetteManager.removeObjectManager(cle2);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 10);
      assertTrue(champManager.findAllObjectsManager().size() == cTots);
   }

   @Test
   public void updateObjectManager(){
      final LigneEtiquette le1 = ligneEtiquetteDao.findById(1);
      final Entite e3 = entiteDao.findById(3);
      final Champ c1 = new Champ();

      final int cTots = champManager.findAllObjectsManager().size();

      final ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
      cle1.setOrdre(2);
      cle1.setExpReg("EXP");
      final ChampEntite ce2 = champEntiteDao.findById(2);
      final ChampEntite ce3 = champEntiteDao.findById(3);
      final ChampEntite ce4 = champEntiteDao.findById(4);
      c1.setChampEntite(ce2);
      champLigneEtiquetteManager.createObjectManager(cle1, le1, e3, c1);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 11);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 1);
      final Integer idCle1 = cle1.getChampLigneEtiquetteId();

      final ChampLigneEtiquette cleUp = champLigneEtiquetteManager.findByIdManager(idCle1);
      // on vérifie qu'une exception est bien lancée
      Boolean catched = false;
      // on test avec la ligne null
      try{
         champLigneEtiquetteManager.updateObjectManager(cleUp, null, e3, c1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 11);

      // update avec un nouveau champ
      cleUp.setOrdre(3);
      cleUp.setExpReg("UP");
      final Champ c2 = new Champ();
      c2.setChampEntite(ce3);
      champLigneEtiquetteManager.updateObjectManager(cleUp, le1, e3, c2);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 11);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);

      final ChampLigneEtiquette cleTest = champLigneEtiquetteManager.findByIdManager(idCle1);
      assertNotNull(cleTest);
      assertTrue(cleTest.getLigneEtiquette().equals(le1));
      assertTrue(cleTest.getChamp().equals(c2));
      assertTrue(cleTest.getEntite().equals(e3));
      assertTrue(cleTest.getChamp().getChampEntite().equals(ce3));
      assertTrue(cleTest.getExpReg().equals("UP"));
      assertTrue(cleTest.getOrdre() == 3);

      // update avec un champ modifié
      c2.setChampEntite(ce4);
      champLigneEtiquetteManager.updateObjectManager(cleTest, le1, e3, c2);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 11);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
      final ChampLigneEtiquette cleTest2 = champLigneEtiquetteManager.findByIdManager(idCle1);
      assertNotNull(cleTest2);
      assertTrue(cleTest2.getLigneEtiquette().equals(le1));
      assertTrue(cleTest2.getChamp().equals(c2));
      assertTrue(cleTest2.getChamp().getChampEntite().equals(ce4));

      champLigneEtiquetteManager.removeObjectManager(cleTest2);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 10);
      champManager.removeObjectManager(c1);
      assertTrue(champManager.findAllObjectsManager().size() == cTots);
   }
}
