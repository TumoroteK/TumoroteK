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
package fr.aphp.tumorotek.manager.test.stockage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementImportConteneurException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager EmplacementManager.
 * Classe créée le 19/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EmplacementManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private TerminaleDao terminaleDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private EntiteManager entiteManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private ServiceDao serviceDao;
   @Autowired
   private PlateformeDao plateformeDao;

   public EmplacementManagerTest(){}

   @Test
   public void testFindById(){
      Emplacement e = emplacementManager.findByIdManager(1);
      assertNotNull(e);
      assertTrue(e.getAdrl().equals("CC1.R1.T1.BT1.A-A"));

      e = emplacementManager.findByIdManager(5);
      assertNotNull(e);

      final Emplacement eNull = emplacementManager.findByIdManager(10);
      assertNull(eNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Emplacement> list = emplacementManager.findAllObjectsManager();
      assertEquals(7, list.size());
   }

   /**
    * Test la méthode findByTerminaleAndVide.
    */
   @Test
   public void testFindByTerminaleAndVide(){
      final Terminale t1 = terminaleDao.findById(1);

      List<Emplacement> list = emplacementManager.findByTerminaleAndVideManager(t1, true);
      assertEquals(2, list.size());
      assertTrue(list.get(0).getAdrl().equals("CC1.R1.T1.BT1.A-J"));

      list = emplacementManager.findByTerminaleAndVideManager(t1, false);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getAdrl().equals("CC1.R1.T1.BT1.A-A"));

      final Terminale t2 = terminaleDao.findById(3);
      list = emplacementManager.findByTerminaleAndVideManager(t2, true);
      assertTrue(list.size() == 0);

      list = emplacementManager.findByTerminaleAndVideManager(t2, false);
      assertTrue(list.size() == 0);

      list = emplacementManager.findByTerminaleAndVideManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTerminaleWithOrder.
    */
   @Test
   public void testFindByTerminaleWithOrder(){
      final Terminale t1 = terminaleDao.findById(1);

      List<Emplacement> list = emplacementManager.findByTerminaleWithOrder(t1);
      assertEquals(5, list.size());
      assertTrue(list.get(0).getAdrl().equals("CC1.R1.T1.BT1.A-A"));

      final Terminale t2 = terminaleDao.findById(6);
      list = emplacementManager.findByTerminaleWithOrder(t2);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getAdrl().equals("CC1.R2.T6.BT1.A-1"));

      final Terminale t3 = terminaleDao.findById(3);
      list = emplacementManager.findByTerminaleWithOrder(t3);
      assertTrue(list.size() == 0);

      list = emplacementManager.findByTerminaleWithOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTerminaleAndPosition.
    */
   @Test
   public void testFindByTerminaleAndPosition(){
      final Terminale t1 = terminaleDao.findById(1);

      List<Emplacement> list = emplacementManager.findByTerminaleAndPosition(t1, 1);
      assertTrue(list.size() == 1);

      final Terminale t2 = terminaleDao.findById(2);
      list = emplacementManager.findByTerminaleAndPosition(t2, 1);
      assertTrue(list.size() == 0);

      list = emplacementManager.findByTerminaleAndPosition(t1, null);
      assertTrue(list.size() == 0);

      list = emplacementManager.findByTerminaleAndPosition(null, 1);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByEmplacementAdrlManager.
    */
   @Test
   public void testFindByEmplacementAdrlManager(){
      final Banque b1 = banqueDao.findById(1);
      Emplacement empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.A-A", b1);
      assertTrue(empl.getEmplacementId() == 1);
      assertTrue(emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.1", b1).getEmplacementId() == 1);
      assertNull(emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.A-1", b1));

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.A-J", b1);
      assertTrue(empl.getEmplacementId() == 4);
      assertTrue(emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.10", b1).getEmplacementId() == 4);

      // out of bounds
      assertNull(emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.A-K", b1));
      assertNull(emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.415", b1));

      // a creer
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.C-D", b1);
      assertNull(empl.getEmplacementId());
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.29", b1);
      assertNull(empl.getEmplacementId());

      // autre numerotation
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.A-2", b1);
      assertTrue(empl.getEmplacementId() == 7);
      assertTrue(emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.2", b1).getEmplacementId() == 7);
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.A-1", b1);
      assertTrue(empl.getEmplacementId() == 6);
      assertTrue(emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.1", b1).getEmplacementId() == 6);

      // out of bounds
      assertNull(emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.K-2", b1));
      assertNull(emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.114", b1));

      // a creer
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.C-2", b1);
      assertNull(empl.getEmplacementId());
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R2.T6.BT1.31", b1);
      assertNull(empl.getEmplacementId());

      // dummy tests
      empl = emplacementManager.findByEmplacementAdrlManager("CC1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.BT1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.BT1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CT1.R1.T1.BT1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.X1.T1.BT1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.X1.BT1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.B1.1", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.1A", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT33.A-A", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager("", b1);
      assertNull(empl);

      empl = emplacementManager.findByEmplacementAdrlManager(null, b1);
      assertNull(empl);

      // conteneurs ambigus
      // On test une insertion valide avec les assos non obligatoires
      final Conteneur c2 = new Conteneur();
      c2.setCode("CC1");
      c2.setNom("TEST");
      final List<Plateforme> pfs = new ArrayList<>();
      pfs.add(plateformeDao.findById(1));
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      conteneurManager.createObjectManager(c2, null, serviceDao.findById(1), banks, pfs, utilisateurDao.findById(1),
         plateformeDao.findById(2));
      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      boolean catched = false;
      try{
         emplacementManager.findByEmplacementAdrlManager("CC1.R1.T1.BT1.89", b1);
      }catch(final EmplacementImportConteneurException ie){
         catched = true;
      }
      assertTrue(catched);

      // clean up
      final Conteneur cTest2 = conteneurManager.findByIdManager(c2.getConteneurId());
      assertNotNull(cTest2);
      conteneurManager.removeObjectManager(cTest2, null, utilisateurDao.findById(1));
      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest2);
      cleanUpFantomes(fs);
   }

   /**
    * Test la méthode checkEmplacementInTerminale.
    */
   @Test
   public void testCheckEmplacementInTerminale(){

      final Emplacement e1 = emplacementManager.findByIdManager(1);
      assertTrue(emplacementManager.checkEmplacementInTerminale(e1));

      e1.setPosition(1000);
      assertFalse(emplacementManager.checkEmplacementInTerminale(e1));

      final Emplacement newE = new Emplacement();
      assertFalse(emplacementManager.checkEmplacementInTerminale(newE));

      newE.setPosition(5);
      assertFalse(emplacementManager.checkEmplacementInTerminale(newE));

      final Terminale t1 = terminaleDao.findById(1);
      newE.setTerminale(t1);
      assertTrue(emplacementManager.checkEmplacementInTerminale(newE));

      newE.setPosition(500);
      assertFalse(emplacementManager.checkEmplacementInTerminale(newE));

      assertFalse(emplacementManager.checkEmplacementInTerminale(null));

   }

   /**
    * Test de la méthode getNumerotationByPositionAndTerminale().
    */
   @Test
   public void testGetNumerotationByPositionAndTerminaleManager(){
      final Terminale t1 = terminaleDao.findById(1);
      String num = emplacementManager.getNumerotationByPositionAndTerminaleManager(1, t1);
      assertTrue(num.equals("A-A"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(5, t1);
      assertTrue(num.equals("A-E"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(10, t1);
      assertTrue(num.equals("A-J"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(11, t1);
      assertTrue(num.equals("B-A"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(20, t1);
      assertTrue(num.equals("B-J"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(23, t1);
      assertTrue(num.equals("C-C"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(59, t1);
      assertTrue(num.equals("F-I"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(100, t1);
      assertTrue(num.equals("J-J"));

      final Terminale t2 = terminaleDao.findById(2);
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(1, t2);
      assertTrue(num.equals("1"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(5, t2);
      assertTrue(num.equals("5"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(10, t2);
      assertTrue(num.equals("10"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(11, t2);
      assertTrue(num.equals("11"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(20, t2);
      assertTrue(num.equals("20"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(23, t2);
      assertTrue(num.equals("23"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(59, t2);
      assertTrue(num.equals("59"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(100, t2);
      assertTrue(num.equals("100"));

      final Terminale t4 = terminaleDao.findById(4);
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(1, t4);
      assertTrue(num.equals("1-1"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(5, t4);
      assertTrue(num.equals("1-5"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(10, t4);
      assertTrue(num.equals("1-10"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(11, t4);
      assertTrue(num.equals("2-1"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(23, t4);
      assertTrue(num.equals("3-3"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(59, t4);
      assertTrue(num.equals("6-9"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(100, t4);
      assertTrue(num.equals("10-10"));

      final Terminale t5 = terminaleDao.findById(5);
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(1, t5);
      assertTrue(num.equals("1-A"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(5, t5);
      assertTrue(num.equals("2-C"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(10, t5);
      assertTrue(num.equals("4-A"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(11, t5);
      assertTrue(num.equals("4-B"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(23, t5);
      assertTrue(num.equals("6-C"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(59, t5);
      assertTrue(num.equals("10-E"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(67, t5);
      assertTrue(num.equals("11-D"));

      final Terminale t6 = terminaleDao.findById(6);
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(1, t6);
      assertTrue(num.equals("A-1"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(5, t6);
      assertTrue(num.equals("A-5"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(10, t6);
      assertTrue(num.equals("B-1"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(11, t6);
      assertTrue(num.equals("B-2"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(23, t6);
      assertTrue(num.equals("C-5"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(59, t6);
      assertTrue(num.equals("G-5"));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(81, t6);
      assertTrue(num.equals("I-9"));

      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(0, t1);
      assertTrue(num.equals(""));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(155, t1);
      assertTrue(num.equals(""));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(15, null);
      assertTrue(num.equals(""));
      num = emplacementManager.getNumerotationByPositionAndTerminaleManager(15, new Terminale());
      assertTrue(num.equals(""));
   }

   /**
    * Test de la méthode getPositionByCoordonnees().
    */
   @Test
   public void testGetPositionByCoordonnees(){
      final Terminale t1 = terminaleDao.findById(1);
      Integer pos = emplacementManager.getPositionByCoordonnees(t1, 1, 1);
      assertTrue(pos.equals(1));
      pos = emplacementManager.getPositionByCoordonnees(t1, 1, 5);
      assertTrue(pos.equals(5));
      pos = emplacementManager.getPositionByCoordonnees(t1, 1, 10);
      assertTrue(pos.equals(10));
      pos = emplacementManager.getPositionByCoordonnees(t1, 2, 1);
      assertTrue(pos.equals(11));
      pos = emplacementManager.getPositionByCoordonnees(t1, 3, 3);
      assertTrue(pos.equals(23));
      pos = emplacementManager.getPositionByCoordonnees(t1, 6, 9);
      assertTrue(pos.equals(59));
      pos = emplacementManager.getPositionByCoordonnees(t1, 10, 10);
      assertTrue(pos.equals(100));
      pos = emplacementManager.getPositionByCoordonnees(t1, 11, 10);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t1, 10, 11);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t1, 11, 11);
      assertTrue(pos.equals(0));

      final Terminale t5 = terminaleDao.findById(5);
      pos = emplacementManager.getPositionByCoordonnees(t5, 1, 1);
      assertTrue(pos.equals(1));
      pos = emplacementManager.getPositionByCoordonnees(t5, 2, 3);
      assertTrue(pos.equals(5));
      pos = emplacementManager.getPositionByCoordonnees(t5, 4, 1);
      assertTrue(pos.equals(10));
      pos = emplacementManager.getPositionByCoordonnees(t5, 4, 2);
      assertTrue(pos.equals(11));
      pos = emplacementManager.getPositionByCoordonnees(t5, 6, 3);
      assertTrue(pos.equals(23));
      pos = emplacementManager.getPositionByCoordonnees(t5, 10, 5);
      assertTrue(pos.equals(59));
      pos = emplacementManager.getPositionByCoordonnees(t5, 12, 4);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t5, 5, 7);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t5, 1, 3);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t5, 8, 10);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t5, 12, 15);
      assertTrue(pos.equals(0));

      final Terminale t6 = terminaleDao.findById(6);
      pos = emplacementManager.getPositionByCoordonnees(t6, 1, 1);
      assertTrue(pos.equals(1));
      pos = emplacementManager.getPositionByCoordonnees(t6, 1, 5);
      assertTrue(pos.equals(5));
      pos = emplacementManager.getPositionByCoordonnees(t6, 1, 9);
      assertTrue(pos.equals(9));
      pos = emplacementManager.getPositionByCoordonnees(t6, 2, 1);
      assertTrue(pos.equals(10));
      pos = emplacementManager.getPositionByCoordonnees(t6, 3, 5);
      assertTrue(pos.equals(23));
      pos = emplacementManager.getPositionByCoordonnees(t6, 7, 5);
      assertTrue(pos.equals(59));
      pos = emplacementManager.getPositionByCoordonnees(t6, 9, 9);
      assertTrue(pos.equals(81));
      pos = emplacementManager.getPositionByCoordonnees(t6, 10, 9);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t6, 9, 10);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t6, 10, 10);
      assertTrue(pos.equals(0));

      pos = emplacementManager.getPositionByCoordonnees(null, 1, 1);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(new Terminale(), 1, 1);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t1, null, 1);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t1, 0, 1);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t1, 1, null);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(t1, 1, 0);
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByCoordonnees(null, null, null);
      assertTrue(pos.equals(0));
   }

   @Test
   public void testGetPositionByAdrl(){

      // CAR-CAR carré 10-10
      final Terminale t1 = terminaleDao.findById(1);
      Integer pos = emplacementManager.getPositionByAdrl(t1, "A-A");
      assertTrue(pos.equals(1));
      pos = emplacementManager.getPositionByAdrl(t1, "E-F");
      assertTrue(pos.equals(46));
      pos = emplacementManager.getPositionByAdrl(t1, "J-J");
      assertTrue(pos.equals(100));
      // out 
      pos = emplacementManager.getPositionByAdrl(t1, "K-B");
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByAdrl(t1, "A-Y");
      assertTrue(pos.equals(0));
      // incoh
      pos = emplacementManager.getPositionByAdrl(t1, "A-10");
      assertTrue(pos.equals(0));
      // pos		
      pos = emplacementManager.getPositionByAdrl(t1, "10");
      assertTrue(pos.equals(10));
      boolean catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t1, "A");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);

      // POS carré 10-10
      final Terminale t2 = terminaleDao.findById(2);
      pos = emplacementManager.getPositionByAdrl(t2, "1");
      assertTrue(pos.equals(1));
      pos = emplacementManager.getPositionByAdrl(t2, "42");
      assertTrue(pos.equals(42));
      pos = emplacementManager.getPositionByAdrl(t2, "99");
      assertTrue(pos.equals(99));
      // out 
      pos = emplacementManager.getPositionByAdrl(t2, "112");
      assertTrue(pos.equals(0));
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t2, "A");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t2, "A-10");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t2, "1-2");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);

      // NUM-NUM carré 10-10
      final Terminale t4 = terminaleDao.findById(4);
      pos = emplacementManager.getPositionByAdrl(t4, "1-1");
      assertTrue(pos.equals(1));
      pos = emplacementManager.getPositionByAdrl(t4, "5-6");
      assertTrue(pos.equals(46));
      pos = emplacementManager.getPositionByAdrl(t4, "10-10");
      assertTrue(pos.equals(100));
      // out 
      pos = emplacementManager.getPositionByAdrl(t4, "11-2");
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByAdrl(t4, "1-22");
      assertTrue(pos.equals(0));
      // pos		
      pos = emplacementManager.getPositionByAdrl(t4, "44");
      assertTrue(pos.equals(44));
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t4, "A-10");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t4, "B");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);

      // CAR-NUM carré 9-9
      final Terminale t6 = terminaleDao.findById(6);
      pos = emplacementManager.getPositionByAdrl(t6, "B-2");
      assertTrue(pos.equals(11));
      pos = emplacementManager.getPositionByAdrl(t6, "F-9");
      assertTrue(pos.equals(54));
      pos = emplacementManager.getPositionByAdrl(t6, "I-9");
      assertTrue(pos.equals(81));
      pos = emplacementManager.getPositionByAdrl(t6, "I-10");
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByAdrl(t6, "M-1");
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByAdrl(t6, "2-2");
      assertTrue(pos.equals(0));
      // pos		
      pos = emplacementManager.getPositionByAdrl(t6, "12");
      assertTrue(pos.equals(12));
      // pos out
      pos = emplacementManager.getPositionByAdrl(t6, "100");
      assertTrue(pos.equals(0));
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t6, "A-A");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t4, "C");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);

      // NUM-CAR triangle 67
      final Terminale t5 = terminaleDao.findById(5);
      pos = emplacementManager.getPositionByAdrl(t5, "2-C");
      assertTrue(pos.equals(5));
      pos = emplacementManager.getPositionByAdrl(t5, "6-B");
      assertTrue(pos.equals(22));
      pos = emplacementManager.getPositionByAdrl(t5, "10-I");
      assertTrue(pos.equals(0));
      pos = emplacementManager.getPositionByAdrl(t5, "2-10");
      assertTrue(pos.equals(0));
      // out
      pos = emplacementManager.getPositionByAdrl(t5, "2-I");
      assertTrue(pos.equals(0));
      // pos		
      pos = emplacementManager.getPositionByAdrl(t5, "24");
      assertTrue(pos.equals(24));
      // pos out
      pos = emplacementManager.getPositionByAdrl(t5, "100");
      assertTrue(pos.equals(0));

      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t5, "B-B");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t5, "F");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      // out of scheme
      catched = false;
      try{
         pos = emplacementManager.getPositionByAdrl(t5, "12-A");
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final Integer position1 = 1;
      final Integer position2 = 5;
      final Terminale t1 = terminaleDao.findById(1);
      final Terminale t2 = terminaleDao.findById(2);

      final Emplacement e1 = new Emplacement();
      e1.setPosition(position1);
      e1.setTerminale(t1);
      assertNotNull(emplacementManager.findDoublonManager(e1));

      e1.setPosition(position2);
      assertNull(emplacementManager.findDoublonManager(e1));

      e1.setPosition(position1);
      e1.setTerminale(t2);
      assertNull(emplacementManager.findDoublonManager(e1));

      final Emplacement e2 = emplacementManager.findByIdManager(2);
      assertNull(emplacementManager.findDoublonManager(e2));

      e2.setPosition(position1);
      assertNotNull(emplacementManager.findDoublonManager(e2));

      e2.setTerminale(t2);
      assertNull(emplacementManager.findDoublonManager(e2));

      assertNull(emplacementManager.findDoublonManager(null));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      Emplacement e = emplacementManager.findByIdManager(2);
      assertTrue(emplacementManager.isUsedObjectManager(e));

      e = emplacementManager.findByIdManager(5);
      assertFalse(emplacementManager.isUsedObjectManager(e));

      assertFalse(emplacementManager.isUsedObjectManager(null));
   }

   /**
    * Test la méthode getConteneurForEmplacement.
    */
   @Test
   public void testGetConteneurManager(){

      final Emplacement e1 = emplacementManager.findByIdManager(1);
      Conteneur cont1 = emplacementManager.getConteneurManager(e1);
      assertNotNull(cont1);
      assertTrue(cont1.getCode().equals("CC1"));

      final Emplacement newE = new Emplacement();
      assertNull(emplacementManager.getConteneurManager(newE));

      final Terminale t1 = terminaleDao.findById(1);
      newE.setTerminale(t1);
      cont1 = emplacementManager.getConteneurManager(newE);
      assertNotNull(cont1);
      assertTrue(cont1.getCode().equals("CC1"));

      assertNull(emplacementManager.getConteneurManager(null));

   }

   /**
    * Test la méthode getTerminaleAdrlManager.
    */
   @Test
   public void testGetTerminaleAdrlManager(){

      final Terminale t1 = terminaleDao.findById(1);
      String adrl = emplacementManager.getTerminaleAdrlManager(t1);
      assertNotNull(adrl);
      assertTrue(adrl.equals("CC1.R1.T1.BT1"));

      final Terminale t2 = terminaleDao.findById(6);
      adrl = emplacementManager.getTerminaleAdrlManager(t2);
      assertNotNull(adrl);
      assertTrue(adrl.equals("CC1.R2.T6.BT1"));

      adrl = emplacementManager.getTerminaleAdrlManager(null);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));

   }

   /**
    * Test la méthode getAdrlManager.
    */
   @Test
   public void testGetAdrlManager(){

      final Emplacement e1 = emplacementManager.findByIdManager(1);
      String adrl = emplacementManager.getAdrlManager(e1, false);
      assertNotNull(adrl);
      assertTrue(adrl.equals("CC1.R1.T1.BT1.A-A"));

      adrl = emplacementManager.getAdrlManager(e1, true);
      assertNotNull(adrl);
      assertTrue(adrl.equals("CC1.1.1.1.1"));

      final Emplacement newE = new Emplacement();
      adrl = emplacementManager.getAdrlManager(newE, false);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));

      adrl = emplacementManager.getAdrlManager(newE, true);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));

      final Terminale t1 = terminaleDao.findById(1);
      newE.setTerminale(t1);
      adrl = emplacementManager.getAdrlManager(newE, false);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));

      adrl = emplacementManager.getAdrlManager(newE, true);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));

      newE.setPosition(15);
      adrl = emplacementManager.getAdrlManager(newE, false);
      assertNotNull(adrl);
      assertTrue(adrl.equals("CC1.R1.T1.BT1.B-E"));

      adrl = emplacementManager.getAdrlManager(newE, true);
      assertNotNull(adrl);
      assertTrue(adrl.equals("CC1.R1.T1.BT1.B-E"));

      adrl = emplacementManager.getAdrlManager(null, false);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));

      adrl = emplacementManager.getAdrlManager(null, true);
      assertNotNull(adrl);
      assertTrue(adrl.equals(""));
   }

   @Test
   public void testGetNomsForEmplacementsManager(){
      final Emplacement e1 = emplacementManager.findByIdManager(1);
      final Emplacement e2 = emplacementManager.findByIdManager(2);
      final Emplacement e3 = emplacementManager.findByIdManager(3);
      final Emplacement e5 = emplacementManager.findByIdManager(5);
      final Emplacement e6 = emplacementManager.findByIdManager(6);

      List<Emplacement> emplList = new ArrayList<>();
      List<String> results = new ArrayList<>();

      emplList.add(e1);
      emplList.add(e2);
      emplList.add(e3);
      emplList.add(e5);
      results = emplacementManager.getNomsForEmplacementsManager(emplList);
      assertTrue(results.size() == 4);
      assertTrue(results.get(0).equals("PTRA.1.1"));
      assertTrue(results.get(2).equals("PTRA.2"));
      assertTrue(results.get(3).equals(""));

      emplList = new ArrayList<>();
      emplList.add(e6);
      emplList.add(e5);
      results = emplacementManager.getNomsForEmplacementsManager(emplList);
      assertTrue(results.size() == 2);
      assertTrue(results.get(0).equals(""));

      emplList.add(new Emplacement());
      results = emplacementManager.getNomsForEmplacementsManager(emplList);
      assertTrue(results.size() == 3);
      assertTrue(results.get(0).equals(""));

      results = emplacementManager.getNomsForEmplacementsManager(null);
      assertTrue(results.size() == 0);

   }

   @Test
   public void testGetTypesForEmplacementsManager(){
      final Emplacement e1 = emplacementManager.findByIdManager(1);
      final Emplacement e2 = emplacementManager.findByIdManager(2);
      final Emplacement e3 = emplacementManager.findByIdManager(3);
      final Emplacement e5 = emplacementManager.findByIdManager(5);
      final Emplacement e6 = emplacementManager.findByIdManager(6);

      List<Emplacement> emplList = new ArrayList<>();
      List<String> results = new ArrayList<>();

      emplList.add(e1);
      emplList.add(e2);
      emplList.add(e3);
      emplList.add(e5);
      results = emplacementManager.getTypesForEmplacementsManager(emplList);
      assertTrue(results.size() == 4);
      assertTrue(results.get(0).equals("ADN"));
      assertTrue(results.get(2).equals("CELLULES"));
      assertTrue(results.get(3).equals(""));

      emplList = new ArrayList<>();
      emplList.add(e6);
      emplList.add(e5);
      results = emplacementManager.getTypesForEmplacementsManager(emplList);
      assertTrue(results.size() == 2);
      assertTrue(results.get(0).equals(""));

      emplList.add(new Emplacement());
      results = emplacementManager.getTypesForEmplacementsManager(emplList);
      assertTrue(results.size() == 3);
      assertTrue(results.get(0).equals(""));

      results = emplacementManager.getTypesForEmplacementsManager(null);
      assertTrue(results.size() == 0);

   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   private void createObjectManagerTest() throws ParseException{

      final Terminale term = terminaleDao.findById(1);
      final Entite ent = entiteDao.findById(3);

      final Emplacement emp1 = new Emplacement();
      emp1.setPosition(55);

      Boolean catched = false;
      // on test l'insertion avec la terminale nulle
      try{
         emplacementManager.createObjectManager(emp1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, emplacementManager.findAllObjectsManager().size());

      // on test l'insertion d'un doublon
      emp1.setPosition(1);
      try{
         emplacementManager.createObjectManager(emp1, term, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EmplacementDoublonFoundException")){
            assertTrue(((EmplacementDoublonFoundException) e).getTerminale().equals(term));
            assertTrue(((EmplacementDoublonFoundException) e).getPosition().equals(1));
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on test la validation de la position
      emp1.setPosition(155);
      try{
         emplacementManager.createObjectManager(emp1, term, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on test la validation du couple entite/objetId
      emp1.setPosition(55);
      emp1.setEntite(ent);
      emp1.setObjetId(98);
      try{
         emplacementManager.createObjectManager(emp1, term, ent);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      emp1.setEntite(null);
      emp1.setObjetId(null);
      // Test de la validation lors de la création
      try{
         validationInsert(emp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // On test une insertion valide avec les assos non obligatoires à null
      emp1.setTerminale(term);
      emp1.setPosition(55);
      emp1.setVide(true);
      emp1.setObjetId(null);
      emp1.setAdrl("ADRL");
      emplacementManager.createObjectManager(emp1, term, null);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);
      final int id = emp1.getEmplacementId();
      // Vérification
      final Emplacement eTest = emplacementManager.findByIdManager(id);
      assertNotNull(eTest);
      assertNotNull(eTest.getTerminale());
      assertTrue(eTest.getPosition() == 55);
      assertTrue(eTest.getVide());
      assertTrue(eTest.getAdrl().equals("ADRL"));

      // On test une insertion valide avec toutes les assos
      final Emplacement emp2 = new Emplacement();
      emp2.setTerminale(term);
      emp2.setPosition(56);
      emp2.setVide(true);
      emp2.setObjetId(3);
      emp2.setEntite(ent);
      emp2.setAdrl("ADRL");
      emplacementManager.createObjectManager(emp2, term, ent);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 9);
      final int id2 = emp2.getEmplacementId();
      // Vérification
      final Emplacement eTest2 = emplacementManager.findByIdManager(id2);
      assertNotNull(eTest2);
      assertNotNull(eTest2.getTerminale());
      assertNotNull(eTest2.getEntite());
      assertTrue(eTest2.getPosition() == 56);
      assertTrue(eTest2.getObjetId() == 3);
      assertFalse(eTest2.getVide());
      assertTrue(eTest2.getAdrl().equals("ADRL"));

      // Suppression
      emplacementManager.removeObjectManager(eTest);
      emplacementManager.removeObjectManager(eTest2);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
   }

   private void updateObjectManagerTest() throws ParseException{

      final Terminale term = terminaleDao.findById(1);
      final Entite ent = entiteDao.findById(3);

      final Emplacement emp = new Emplacement();
      emp.setPosition(55);
      emplacementManager.createObjectManager(emp, term, null);
      final Integer id1 = emp.getEmplacementId();

      Boolean catched = false;
      final Emplacement empUp1 = emplacementManager.findByIdManager(id1);
      // on test l'update avec la terminale nulle
      try{
         emplacementManager.updateObjectManager(empUp1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);

      // on test l'update d'un doublon
      empUp1.setPosition(1);
      try{
         emplacementManager.updateObjectManager(empUp1, term, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EmplacementDoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);

      // on test la validation de la position
      empUp1.setPosition(102);
      try{
         emplacementManager.updateObjectManager(empUp1, term, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);

      // on test la validation du couple entite/objetId
      empUp1.setPosition(75);
      empUp1.setEntite(ent);
      empUp1.setObjetId(15);
      try{
         emplacementManager.updateObjectManager(empUp1, term, ent);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);

      empUp1.setEntite(null);
      empUp1.setObjetId(null);
      // Test de la validation lors de la création
      try{
         validationUpdate(empUp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);

      // On test un update valide avec les assos non obligatoires à null
      empUp1.setTerminale(term);
      empUp1.setPosition(75);
      empUp1.setObjetId(null);
      empUp1.setAdrl("ADRL");
      emplacementManager.updateObjectManager(empUp1, term, null);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);
      // Vérification
      final Emplacement eTest = emplacementManager.findByIdManager(id1);
      assertNotNull(eTest);
      assertNotNull(eTest.getTerminale());
      assertTrue(eTest.getPosition() == 75);
      assertTrue(eTest.getVide());
      assertTrue(eTest.getAdrl().equals("ADRL"));

      // On test une insertion valide avec toutes les assos
      final Emplacement empUp2 = emplacementManager.findByIdManager(id1);
      empUp2.setTerminale(term);
      empUp2.setPosition(76);
      empUp2.setVide(true);
      empUp2.setObjetId(1);
      empUp2.setEntite(ent);
      empUp2.setAdrl("ADRL");
      emplacementManager.updateObjectManager(empUp2, term, ent);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);
      // Vérification
      final Emplacement eTest2 = emplacementManager.findByIdManager(id1);
      assertNotNull(eTest2);
      assertNotNull(eTest2.getTerminale());
      assertNotNull(eTest2.getEntite());
      assertTrue(eTest2.getPosition() == 76);
      assertTrue(eTest2.getObjetId() == 1);
      assertFalse(eTest2.getVide());
      assertTrue(eTest2.getAdrl().equals("ADRL"));

      // Suppression
      emplacementManager.removeObjectManager(eTest);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
   }

   private void removeObjectManagerTest(){
      // test de la suppression d'un objet null
      emplacementManager.removeObjectManager(null);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // test de la suppression d'un objet utilisé
      final Emplacement emp1 = emplacementManager.findByIdManager(1);
      boolean catched = false;
      try{
         emplacementManager.removeObjectManager(emp1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
   }

   /**
    * Test de la méthode createMultiObjetcsManager().
    */
   @Test
   public void testCreateMultiObjetcsManager(){
      final Terminale t1 = terminaleDao.findById(1);
      final Terminale t2 = terminaleDao.findById(2);

      Boolean catched = false;
      // on test la création avec la terminale nulle
      try{
         emplacementManager.createMultiObjetcsManager(null, 15);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, emplacementManager.findAllObjectsManager().size());

      // on test la création avec un nombre trop grand
      try{
         emplacementManager.createMultiObjetcsManager(t2, 155);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on test la création avec un nombre null
      try{
         emplacementManager.createMultiObjetcsManager(t2, null);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on test la création avec un nombre négatif
      try{
         emplacementManager.createMultiObjetcsManager(t2, -10);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on test la création multiple avec des doublons
      try{
         emplacementManager.createMultiObjetcsManager(t1, 100);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EmplacementDoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on teste une création multiple valide
      final List<Emplacement> list = emplacementManager.createMultiObjetcsManager(t2, 10);
      assertTrue(list.size() == 10);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 17);
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getTerminale());
      assertNull(list.get(0).getEntite());
      assertTrue(list.get(0).getPosition() == 1);
      assertNull(list.get(0).getObjetId());
      assertTrue(list.get(0).getVide());
      assertTrue(list.get(0).getAdrl().equals("CC1.R1.T1.BT2.1"));
      assertNull(list.get(0).getAdrp());

      for(int i = 0; i < list.size(); i++){
         emplacementManager.removeObjectManager(list.get(i));
      }
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
   }

   /**
    * Test la validation d'un Emplacement lors de sa création.
    * @param emplacement Emplacement à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Emplacement emplacement) throws ParseException{

      final Terminale term = terminaleDao.findById(1);
      boolean catchedInsert = false;
      // On teste une insertion avec un attribut position non valide
      catchedInsert = false;
      try{
         emplacement.setTerminale(term);
         emplacement.setPosition(-1);
         emplacementManager.createObjectManager(emplacement, term, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      emplacement.setPosition(55);

      // On teste une insertion avec un attribut adrl non valide
      final String[] emptyValues = new String[] {"", "  ", "%$$¤¤*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            emplacement.setTerminale(term);
            emplacement.setAdrl(emptyValues[i]);
            emplacementManager.createObjectManager(emplacement, term, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      emplacement.setAdrl(null);
   }

   /**
    * Test la validation d'un Emplacement lors de son update.
    * @param emplacement Emplacement à tester.
    * @throws ParseException 
    */
   private void validationUpdate(final Emplacement emplacement) throws ParseException{

      final Terminale term = terminaleDao.findById(1);
      boolean catched = false;
      // On teste un update avec un attribut position non valide
      catched = false;
      try{
         emplacement.setTerminale(term);
         emplacement.setPosition(-1);
         emplacementManager.updateObjectManager(emplacement, term, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      emplacement.setPosition(55);

      // On teste un update avec un attribut adrl non valide
      final String[] emptyValues = new String[] {"", "  ", "%$$*¤¤gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            emplacement.setTerminale(term);
            emplacement.setAdrl(emptyValues[i]);
            emplacementManager.updateObjectManager(emplacement, term, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      emplacement.setAdrl(null);
   }

   /**
    * Test de la méthode deplacerMultiEmplacementsManager().
    */
   @Test
   public void testDeplacerMultiEmplacementsManager(){
      final Utilisateur u = utilisateurDao.findById(4);
      // on fait des tests avec une liste mal remplie
      emplacementManager.deplacerMultiEmplacementsManager(null, u);
      assertEquals(7, emplacementManager.findAllObjectsManager().size());
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);

      List<Emplacement> list = new ArrayList<>();
      emplacementManager.deplacerMultiEmplacementsManager(list, u);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);

      list.add(new Emplacement());
      list.add(null);
      boolean catched = false;
      try{
         emplacementManager.deplacerMultiEmplacementsManager(list, u);
      }catch(final RequiredObjectIsNullException rex){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);

      // on teste avec une nouvelle position invalide
      list = new ArrayList<>();
      Emplacement e1 = emplacementManager.findByIdManager(1);
      e1.setPosition(155);
      list.add(e1);
      catched = false;
      try{
         emplacementManager.deplacerMultiEmplacementsManager(list, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
      Emplacement e1Test = emplacementManager.findByIdManager(1);
      assertTrue(e1Test.getPosition().equals(1));

      // on fait un test valide
      e1 = emplacementManager.findByIdManager(1);
      final Integer pos1 = e1.getPosition();
      final Emplacement e2 = emplacementManager.findByIdManager(2);
      final Integer pos2 = e2.getPosition();
      final Emplacement e3 = emplacementManager.findByIdManager(3);
      final Integer pos3 = e3.getPosition();
      e1.setPosition(pos2);
      e2.setPosition(pos1);
      e3.setPosition(25);
      list = new ArrayList<>();
      list.add(e1);
      list.add(e2);
      list.add(e3);
      emplacementManager.deplacerMultiEmplacementsManager(list, u);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
      e1Test = emplacementManager.findByIdManager(1);
      assertTrue(e1Test.getPosition().equals(pos2));
      Emplacement e2Test = emplacementManager.findByIdManager(2);
      assertTrue(e2Test.getPosition().equals(pos1));
      Emplacement e3Test = emplacementManager.findByIdManager(3);
      assertTrue(e3Test.getPosition().equals(25));
      assertTrue(getOperationManager().findAllObjectsManager().size() == 22);
      final Object o1 = entiteManager.findObjectByEntiteAndIdManager(e1.getEntite(), e1.getObjetId());
      assertTrue(getOperationManager().findByObjectManager(o1).size() == 3);
      final Object o2 = entiteManager.findObjectByEntiteAndIdManager(e2.getEntite(), e2.getObjetId());
      assertTrue(getOperationManager().findByObjectManager(o2).size() == 2);
      final Object o3 = entiteManager.findObjectByEntiteAndIdManager(e3.getEntite(), e3.getObjetId());
      assertTrue(getOperationManager().findByObjectManager(o3).size() == 2);

      // on revert le test
      e1.setPosition(pos1);
      e2.setPosition(pos2);
      e3.setPosition(pos3);
      list = new ArrayList<>();
      list.add(e1);
      list.add(e2);
      list.add(e3);
      emplacementManager.deplacerMultiEmplacementsManager(list, u);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
      e1Test = emplacementManager.findByIdManager(1);
      assertTrue(e1Test.getPosition().equals(pos1));
      e2Test = emplacementManager.findByIdManager(2);
      assertTrue(e2Test.getPosition().equals(pos2));
      e3Test = emplacementManager.findByIdManager(3);
      assertTrue(e3Test.getPosition().equals(pos3));
      assertTrue(getOperationManager().findAllObjectsManager().size() == 25);

      // supression des opérations créées
      final List<Operation> ops = getOperationManager().findByUtilisateurManager(u);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   /**
    * Test de la méthode validateMultiEmplacements().
    */
   @Test
   public void testValidateMultiEmplacements(){
      final Emplacement e1 = emplacementManager.findByIdManager(1);
      final Emplacement e2 = emplacementManager.findByIdManager(2);
      final Emplacement eError = new Emplacement();
      final Terminale t = terminaleDao.findById(1);
      final Entite entite = entiteDao.findById(3);
      final List<Emplacement> liste = new ArrayList<>();
      liste.add(e1);
      liste.add(e2);
      liste.add(eError);
      liste.add(null);

      emplacementManager.validateMultiEmplacementsManager(null);

      Boolean catched = false;
      // on test la validation avec la terminale nulle
      try{
         emplacementManager.validateMultiEmplacementsManager(liste);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, emplacementManager.findAllObjectsManager().size());

      // on test la validation de la position
      eError.setTerminale(t);
      eError.setPosition(155);
      try{
         emplacementManager.validateMultiEmplacementsManager(liste);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      // on test la validation du couple entite/objetId
      eError.setPosition(55);
      eError.setEntite(entite);
      eError.setObjetId(98);
      try{
         emplacementManager.validateMultiEmplacementsManager(liste);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EntiteObjectIdNotExistException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
      eError.setEntite(null);
      eError.setObjetId(null);

      // on test l'insertion d'un doublon
      eError.setPosition(1);
      try{
         emplacementManager.validateMultiEmplacementsManager(liste);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("EmplacementDoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      eError.setPosition(55);
      eError.setAdrl("%$*g¤d");
      // Test de la validation lors de la création
      try{
         emplacementManager.validateMultiEmplacementsManager(liste);
      }catch(final ValidationException ve){
         catched = true;
      }
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
   }

   /**
    * Test de la méthode saveMultiEmplacements().
    */
   @Test
   public void testSaveMultiEmplacements(){
      final Emplacement e1 = emplacementManager.findByIdManager(1);
      final Emplacement e2 = emplacementManager.findByIdManager(2);
      final Emplacement eNew = new Emplacement();
      final Emplacement e4 = emplacementManager.findByIdManager(4);
      e4.setPosition(15);
      final Terminale t = terminaleDao.findById(1);
      final Entite entite = entiteDao.findById(3);
      final List<Emplacement> liste = new ArrayList<>();
      liste.add(e1);
      liste.add(e2);
      liste.add(eNew);
      liste.add(e4);
      liste.add(null);

      emplacementManager.saveMultiEmplacementsManager(null);

      Boolean catched = false;
      // on test la validation avec la terminale nulle
      try{
         emplacementManager.saveMultiEmplacementsManager(liste);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, emplacementManager.findAllObjectsManager().size());
      Emplacement eTest4 = emplacementManager.findByIdManager(4);
      assertTrue(eTest4.getPosition().equals(10));

      eNew.setTerminale(t);
      eNew.setEntite(entite);
      eNew.setObjetId(1);
      eNew.setPosition(20);
      eNew.setVide(false);
      emplacementManager.saveMultiEmplacementsManager(liste);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 8);
      eTest4 = emplacementManager.findByIdManager(4);
      assertTrue(eTest4.getPosition().equals(15));

      final Integer idEmp = eNew.getEmplacementId();
      final Emplacement eTestNew = emplacementManager.findByIdManager(idEmp);
      assertTrue(eTestNew.getPosition().equals(20));

      eTest4.setPosition(10);
      emplacementManager.updateObjectManager(eTest4, eTest4.getTerminale(), eTest4.getEntite());
      eTest4 = emplacementManager.findByIdManager(4);
      assertTrue(eTest4.getPosition().equals(10));

      emplacementManager.removeObjectManager(eTestNew);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
   }

}
