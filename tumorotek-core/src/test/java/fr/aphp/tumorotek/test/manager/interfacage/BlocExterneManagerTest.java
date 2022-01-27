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
package fr.aphp.tumorotek.test.manager.interfacage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager BlocExterne.
 * Classe créée le 07/10/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class BlocExterneManagerTest extends AbstractManagerTest4
{

   @Autowired
   private BlocExterneManager blocExterneManager;
   @Autowired
   private DossierExterneDao dossierExterneDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private ValeurExterneManager valeurExterneManager;

   // 	@SuppressWarnings("deprecation")
   public BlocExterneManagerTest(){
      // pour eviter l'autowiring vers fantomeDao
      //		this.setDependencyCheck(false);
   }

   //	@Override
   //	protected String[] getConfigLocations() {
   //        return new String[]{ "applicationContextManagerBase.xml",
   //       		 "applicationContextDaoBase.xml" };
   //    }

   @Test
   public void testFindById(){
      final BlocExterne m = blocExterneManager.findByIdManager(1);
      assertNotNull(m);

      final BlocExterne mNull = blocExterneManager.findByIdManager(100);
      assertNull(mNull);
   }

   @Test
   public void testFindAll(){
      final List<BlocExterne> list = blocExterneManager.findAllObjectsManager();
      assertTrue(list.size() >= 6);
   }

   @Test
   public void testFindByDossierExterneManager(){
      final DossierExterne d1 = dossierExterneDao.findById(1);
      final DossierExterne d2 = dossierExterneDao.findById(2);

      List<BlocExterne> list = blocExterneManager.findByDossierExterneManager(d1);
      assertTrue(list.size() == 4);

      list = blocExterneManager.findByDossierExterneManager(d2);
      assertTrue(list.size() == 0);

      list = blocExterneManager.findByDossierExterneManager(null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByDossierExterneAndEntiteManager(){
      final DossierExterne d1 = dossierExterneDao.findById(1);
      final DossierExterne d2 = dossierExterneDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e5 = entiteDao.findById(5);

      List<BlocExterne> list = blocExterneManager.findByDossierExterneAndEntiteManager(d1, e1);
      assertTrue(list.size() == 1);

      list = blocExterneManager.findByDossierExterneAndEntiteManager(d1, e5);
      assertTrue(list.size() == 0);

      list = blocExterneManager.findByDossierExterneAndEntiteManager(d2, e1);
      assertTrue(list.size() == 0);

      list = blocExterneManager.findByDossierExterneAndEntiteManager(d2, e5);
      assertTrue(list.size() == 0);

      list = blocExterneManager.findByDossierExterneAndEntiteManager(null, e1);
      assertTrue(list.size() == 0);

      list = blocExterneManager.findByDossierExterneAndEntiteManager(d1, null);
      assertTrue(list.size() == 0);

      list = blocExterneManager.findByDossierExterneAndEntiteManager(null, null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testGetEntiteManager(){
      final BlocExterne b1 = blocExterneManager.findByIdManager(1);
      Entite e = blocExterneManager.getEntiteManager(b1);
      assertNotNull(e);
      assertTrue(e.getNom().equals("Patient"));

      final BlocExterne b3 = blocExterneManager.findByIdManager(3);
      e = blocExterneManager.getEntiteManager(b3);
      assertNotNull(e);
      assertTrue(e.getNom().equals("Echantillon"));

      e = blocExterneManager.getEntiteManager(null);
      assertNull(e);
   }

   @Test
   public void testFindDoublon(){
      final DossierExterne de1 = dossierExterneDao.findById(1);
      final DossierExterne de2 = dossierExterneDao.findById(2);

      final BlocExterne be = new BlocExterne();
      be.setDossierExterne(de1);
      be.setEntiteId(1);
      be.setOrdre(1);
      assertTrue(blocExterneManager.findDoublonManager(be));

      be.setDossierExterne(de2);
      assertFalse(blocExterneManager.findDoublonManager(be));
      be.setDossierExterne(de1);

      be.setEntiteId(5);
      assertFalse(blocExterneManager.findDoublonManager(be));
      be.setEntiteId(1);

      be.setOrdre(10);
      assertFalse(blocExterneManager.findDoublonManager(be));
      be.setOrdre(1);
   }

   @Test
   public void testValidateValeurExterneManager(){
      final DossierExterne de1 = dossierExterneDao.findById(1);
      final BlocExterne be = new BlocExterne();

      be.setEntiteId(1);
      be.setOrdre(1);
      boolean catched = false;
      // on test l'insertion avec un dossier null
      try{
         blocExterneManager.validateBlocExterneManager(be, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec une entité null
      be.setEntiteId(null);
      try{
         blocExterneManager.validateBlocExterneManager(be, de1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test si l'entité n'existe pas
      be.setEntiteId(500);
      try{
         blocExterneManager.validateBlocExterneManager(be, de1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test avec l'ordre null
      be.setEntiteId(1);
      be.setOrdre(null);
      try{
         blocExterneManager.validateBlocExterneManager(be, null);
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
      final DossierExterne de = dossierExterneDao.findById(2);
      final BlocExterne be1 = new BlocExterne();
      be1.setEntiteId(1);
      be1.setOrdre(1);

      final Integer nb = blocExterneManager.findAllObjectsManager().size();
      final Integer nbV = valeurExterneManager.findAllObjectsManager().size();
      boolean catched = false;
      // on test l'insertion avec un dossier null
      try{
         blocExterneManager.saveManager(be1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nb);

      // on test une insertion valide mais sans valeurs
      blocExterneManager.saveManager(be1, de, null);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nb + 1);
      final Integer id1 = be1.getBlocExterneId();

      final BlocExterne bTest = blocExterneManager.findByIdManager(id1);
      assertNotNull(bTest);
      assertTrue(bTest.getDossierExterne().equals(de));
      assertTrue(bTest.getEntiteId().equals(1));
      assertTrue(bTest.getOrdre().equals(1));

      // on teste une instertion avec un doublon
      final BlocExterne be2 = new BlocExterne();
      be2.setEntiteId(1);
      be2.setOrdre(1);
      blocExterneManager.saveManager(be2, de, null);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nb + 1);
      final Integer id2 = be2.getBlocExterneId();
      assertTrue(!id1.equals(id2));

      final BlocExterne bTest2 = blocExterneManager.findByIdManager(id2);
      assertNotNull(bTest2);
      assertTrue(bTest2.getDossierExterne().equals(de));
      assertTrue(bTest2.getEntiteId().equals(1));
      assertTrue(bTest2.getOrdre().equals(1));

      // on test une insertion avec des valeurs
      final BlocExterne be3 = new BlocExterne();
      be3.setEntiteId(2);
      be3.setOrdre(2);
      final ValeurExterne ve1 = new ValeurExterne();
      ve1.setChampEntiteId(23);
      ve1.setValeur("TEST");
      final ValeurExterne ve2 = new ValeurExterne();
      ve2.setChampAnnotationId(1);
      ve2.setValeur("ANNO");
      List<ValeurExterne> valeurs = new ArrayList<>();
      valeurs.add(ve1);
      valeurs.add(ve2);
      blocExterneManager.saveManager(be3, de, valeurs);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nb + 2);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV + 2);
      final Integer id3 = be3.getBlocExterneId();

      final BlocExterne bTest3 = blocExterneManager.findByIdManager(id3);
      assertNotNull(bTest3);
      assertTrue(bTest3.getDossierExterne().equals(de));
      assertTrue(bTest3.getEntiteId().equals(2));
      assertTrue(bTest3.getOrdre().equals(2));
      valeurs = valeurExterneManager.findByBlocExterneManager(bTest3);
      assertTrue(valeurs.size() == 2);
      assertTrue(valeurs.get(0).getChampEntiteId().equals(23));
      assertTrue(valeurs.get(0).getValeur().equals("TEST"));
      assertTrue(valeurs.get(1).getChampAnnotationId().equals(1));
      assertTrue(valeurs.get(1).getValeur().equals("ANNO"));

      // suppression des blocs et des valeurs
      blocExterneManager.deleteByIdManager(bTest2);
      blocExterneManager.deleteByIdManager(bTest3);
      blocExterneManager.deleteByIdManager(new BlocExterne());
      blocExterneManager.deleteByIdManager(null);
      assertTrue(blocExterneManager.findAllObjectsManager().size() == nb);
      assertTrue(valeurExterneManager.findAllObjectsManager().size() == nbV);
   }

}
