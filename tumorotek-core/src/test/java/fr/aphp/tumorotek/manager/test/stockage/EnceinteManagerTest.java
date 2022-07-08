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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.EnceinteSizeException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.stockage.*;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.*;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

import static org.junit.Assert.*;

/**
 *
 * Classe de test pour le manager EnceinteManager.
 * Classe créée le 22/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.2.1
 *
 */
public class EnceinteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EnceinteManager enceinteManager;

   @Autowired
   private ConteneurDao conteneurDao;

   @Autowired
   private EnceinteTypeDao enceinteTypeDao;

   @Autowired
   private BanqueDao banqueDao;

   @Autowired
   private EntiteDao entiteDao;

   @Autowired
   private TerminaleTypeDao terminaleTypeDao;

   @Autowired
   private TerminaleNumerotationDao terminaleNumerotationDao;

   @Autowired
   private TerminaleManager terminaleManager;

   @Autowired
   private ConteneurManager conteneurManager;

   @Autowired
   private UtilisateurDao utilisateurDao;

   @Autowired
   private OperationTypeManager operationTypeManager;

   @Autowired
   private CheckPositionManager checkPositionManager;

   @Autowired
   private CouleurDao couleurDao;

   @Autowired
   private IncidentManager incidentManager;

   public EnceinteManagerTest(){}

   @Test
   public void testFindById(){
      Enceinte e = enceinteManager.findByIdManager(1);
      assertNotNull(e);
      assertEquals("R1", e.getNom());

      e = enceinteManager.findByIdManager(5);
      assertNotNull(e);

      final Enceinte eNull = enceinteManager.findByIdManager(10);
      assertNull(eNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Enceinte> list = enceinteManager.findAllObjectsManager();
      assertEquals(7, list.size());
   }

   /**
    * Test la méthode findByConteneurWithOrderManager.
    */
   @Test
   public void testFindByConteneurWithOrderManager(){
      final Conteneur c1 = conteneurDao.findById(1);
      List<Enceinte> list = enceinteManager.findByConteneurWithOrderManager(c1);
      assertEquals(2, list.size());
      assertEquals("R1", list.get(0).getNom());

      final Conteneur c2 = conteneurDao.findById(2);
      list = enceinteManager.findByConteneurWithOrderManager(c2);
      assertEquals(0, list.size());

      list = enceinteManager.findByConteneurWithOrderManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode findByEnceintePereWithOrderManager.
    */
   @Test
   public void testFindByEnceintePereWithOrderManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<Enceinte> list = enceinteManager.findByEnceintePereWithOrderManager(e1);
      assertEquals(3, list.size());
      assertEquals("T1", list.get(0).getNom());

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.findByEnceintePereWithOrderManager(e2);
      assertEquals(2, list.size());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.findByEnceintePereWithOrderManager(e3);
      assertEquals(0, list.size());

      list = enceinteManager.findByEnceintePereWithOrderManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode findByConteneurAndNomManager.
    */
   @Test
   public void testFindByConteneurAndNomManager(){
      final Conteneur c1 = conteneurDao.findById(1);
      List<Enceinte> list = enceinteManager.findByConteneurAndNomManager(c1, "R1");
      assertEquals(1, list.size());
      assertEquals("R1", list.get(0).getNom());

      list = enceinteManager.findByConteneurAndNomManager(c1, "Rqsdqsd1");
      assertEquals(0, list.size());

      final Conteneur c2 = conteneurDao.findById(2);
      list = enceinteManager.findByConteneurAndNomManager(c2, "R1");
      assertEquals(0, list.size());

      list = enceinteManager.findByConteneurAndNomManager(null, "R1");
      assertEquals(0, list.size());

      list = enceinteManager.findByConteneurAndNomManager(c1, null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode findByEnceintePereAndNomManager.
    */
   @Test
   public void testFindByEnceintePereAndNomManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<Enceinte> list = enceinteManager.findByEnceintePereAndNomManager(e1, "T1");
      assertEquals(1, list.size());
      assertEquals("T1", list.get(0).getNom());

      list = enceinteManager.findByEnceintePereAndNomManager(e1, "jhq");
      assertEquals(0, list.size());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.findByEnceintePereAndNomManager(e3, "T1");
      assertEquals(0, list.size());

      list = enceinteManager.findByEnceintePereAndNomManager(null, "T1");
      assertEquals(0, list.size());

      list = enceinteManager.findByEnceintePereAndNomManager(e1, null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode usedNomsExceptOneManager.
    */
   @Test
   public void testUsedNomsExceptOneManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<String> list = enceinteManager.usedNomsExceptOneManager(e1);
      assertEquals(1, list.size());
      assertEquals("R2", list.get(0));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.usedNomsExceptOneManager(e2);
      assertEquals(1, list.size());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.usedNomsExceptOneManager(e3);
      assertEquals(2, list.size());
      assertEquals("T2", list.get(0));

      list = enceinteManager.usedNomsExceptOneManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode getBanquesManager.
    */
   @Test
   public void testGetBanquesManager(){

      final Enceinte e5 = enceinteManager.findByIdManager(5);
      Set<Banque> set = enceinteManager.getBanquesManager(e5);
      assertEquals(2, set.size());

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      set = enceinteManager.getBanquesManager(e7);
      assertEquals(1, set.size());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      set = enceinteManager.getBanquesManager(e3);
      assertEquals(0, set.size());

      final Enceinte newE = new Enceinte();
      set = enceinteManager.getBanquesManager(newE);
      assertEquals(0, set.size());

      set = enceinteManager.getBanquesManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode getEnceintesManager.
    */
   @Test
   public void testGetEnceintesManager(){

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Set<Enceinte> set = enceinteManager.getEnceintesManager(e1);
      assertEquals(3, set.size());
      final Iterator<Enceinte> it = set.iterator();
      assertEquals(1, (int) it.next().getPosition());

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      set = enceinteManager.getEnceintesManager(e2);
      assertEquals(2, set.size());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      set = enceinteManager.getEnceintesManager(e3);
      assertEquals(0, set.size());

      final Enceinte newE = new Enceinte();
      set = enceinteManager.getEnceintesManager(newE);
      assertEquals(0, set.size());

      set = enceinteManager.getEnceintesManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode getTerminalesManager.
    */
   @Test
   public void testGetTerminalesManager(){

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      Set<Terminale> set = enceinteManager.getTerminalesManager(e3);
      assertEquals(3, set.size());
      final Iterator<Terminale> it = set.iterator();
      assertEquals("BT1", it.next().getNom());

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      set = enceinteManager.getTerminalesManager(e7);
      assertEquals(1, set.size());

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      set = enceinteManager.getTerminalesManager(e4);
      assertEquals(0, set.size());

      final Enceinte newE = new Enceinte();
      set = enceinteManager.getTerminalesManager(newE);
      assertEquals(0, set.size());

      set = enceinteManager.getTerminalesManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode getTerminalesManager.
    */
   @Test
   public void testGetAllTerminalesInArborescenceManager(){

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      List<Terminale> list = enceinteManager.getAllTerminalesInArborescenceManager(e3);
      assertEquals(3, list.size());
      assertEquals("BT1", list.get(0).getNom());

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e7);
      assertEquals(1, list.size());

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e4);
      assertEquals(0, list.size());

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e1);
      assertEquals(4, list.size());

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e2);
      assertEquals(2, list.size());

      final Enceinte newE = new Enceinte();
      list = enceinteManager.getAllTerminalesInArborescenceManager(newE);
      assertEquals(0, list.size());

      list = enceinteManager.getAllTerminalesInArborescenceManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode checkEnceinteInEnceintePereLimitesManager.
    */
   @Test
   public void testCheckEnceinteInEnceintePereLimitesManager(){

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      assertTrue(enceinteManager.checkEnceinteInEnceintePereLimitesManager(e3));

      e3.setPosition(1000);
      assertFalse(enceinteManager.checkEnceinteInEnceintePereLimitesManager(e3));

      final Enceinte newE = new Enceinte();
      assertFalse(enceinteManager.checkEnceinteInEnceintePereLimitesManager(newE));

      newE.setPosition(5);
      assertFalse(enceinteManager.checkEnceinteInEnceintePereLimitesManager(newE));

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      newE.setEnceintePere(e1);
      newE.setPosition(null);
      assertFalse(enceinteManager.checkEnceinteInEnceintePereLimitesManager(newE));

      newE.setPosition(5);
      assertTrue(enceinteManager.checkEnceinteInEnceintePereLimitesManager(newE));

      newE.setPosition(500);
      assertFalse(enceinteManager.checkEnceinteInEnceintePereLimitesManager(newE));

      assertFalse(enceinteManager.checkEnceinteInEnceintePereLimitesManager(null));

   }

   /**
    * Test la méthode checkEnceinteInConteneurLimitesManager.
    */
   @Test
   public void testCheckEnceinteInConteneurLimitesManager(){

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertTrue(enceinteManager.checkEnceinteInConteneurLimitesManager(e1));

      e1.setPosition(1000);
      assertFalse(enceinteManager.checkEnceinteInConteneurLimitesManager(e1));

      final Enceinte newE = new Enceinte();
      assertFalse(enceinteManager.checkEnceinteInConteneurLimitesManager(newE));

      newE.setPosition(3);
      assertFalse(enceinteManager.checkEnceinteInConteneurLimitesManager(newE));

      final Conteneur c1 = conteneurDao.findById(1);
      newE.setConteneur(c1);
      newE.setPosition(null);
      assertFalse(enceinteManager.checkEnceinteInConteneurLimitesManager(newE));

      newE.setPosition(3);
      assertTrue(enceinteManager.checkEnceinteInConteneurLimitesManager(newE));

      newE.setPosition(500);
      assertFalse(enceinteManager.checkEnceinteInConteneurLimitesManager(newE));

      assertFalse(enceinteManager.checkEnceinteInConteneurLimitesManager(null));

   }

   /**
    * Test la méthode checkPositionLibreInEnceinte.
    */
   @Test
   public void testCheckPositionLibreInEnceinte(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e1, 1, null, 10));
      assertTrue(checkPositionManager.checkPositionLibreInEnceinteManager(e1, 1, null, 3));
      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e1, 1, 1, null));
      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e1, 1, null, null));
      assertTrue(checkPositionManager.checkPositionLibreInEnceinteManager(e1, 10, null, null));

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e3, 1, 10, null));
      assertTrue(checkPositionManager.checkPositionLibreInEnceinteManager(e3, 10, 1, null));
      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e3, 1, null, 1));
      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e3, 1, null, null));

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      assertTrue(checkPositionManager.checkPositionLibreInEnceinteManager(e4, 1, null, null));

      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(null, 1, 1, 1));

      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(e3, null, null, null));

      assertFalse(checkPositionManager.checkPositionLibreInEnceinteManager(null, null, null, null));
   }

   /**
    * Test la méthode checkPositionLibreInConteneurManager.
    */
   @Test
   public void testCheckPositionLibreInConteneurManager(){
      final Conteneur c1 = conteneurDao.findById(1);
      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(c1, 1, 10));

      assertTrue(checkPositionManager.checkPositionLibreInConteneurManager(c1, 1, 1));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(c1, 1, 2));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(c1, 1, null));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(c1, null, 1));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(null, 1, 1));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(null, 1, null));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(c1, null, null));

      assertFalse(checkPositionManager.checkPositionLibreInConteneurManager(null, null, null));
   }

   /**
    * Test la méthode checkLastEnceinte.
    */
   @Test
   public void testCheckLastEnceinte(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertFalse(enceinteManager.checkLastEnceinte(e1));

      final Enceinte e2 = enceinteManager.findByIdManager(1);
      assertFalse(enceinteManager.checkLastEnceinte(e2));

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      assertTrue(enceinteManager.checkLastEnceinte(e3));

      final Enceinte e6 = enceinteManager.findByIdManager(6);
      assertTrue(enceinteManager.checkLastEnceinte(e6));

      final Enceinte eNew = new Enceinte();
      assertFalse(enceinteManager.checkLastEnceinte(eNew));

      assertFalse(enceinteManager.checkLastEnceinte(null));
   }

   /**
    * Test la méthode getLevelEnceinte.
    */
   @Test
   public void testGetLevelEnceinte(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertEquals(1, (int) enceinteManager.getLevelEnceinte(e1));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      assertEquals(1, (int) enceinteManager.getLevelEnceinte(e2));

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      assertEquals(2, (int) enceinteManager.getLevelEnceinte(e3));

      final Enceinte e6 = enceinteManager.findByIdManager(6);
      assertEquals(2, (int) enceinteManager.getLevelEnceinte(e6));

      final Enceinte eNew = new Enceinte();
      assertEquals(1, (int) enceinteManager.getLevelEnceinte(eNew));

      assertEquals(0, (int) enceinteManager.getLevelEnceinte(null));
   }

   /**
    * Test la méthode getNumberEmplacementsLibres.
    */
   @Test
   public void testGetNumberEmplacementsLibres(){

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Long nb = enceinteManager.getNumberEmplacementsLibres(e1);
      assertEquals(397, (long) nb);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      nb = enceinteManager.getNumberEmplacementsLibres(e2);
      assertEquals(148, (long) nb);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      nb = enceinteManager.getNumberEmplacementsLibres(e3);
      assertEquals(297, (long) nb);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      nb = enceinteManager.getNumberEmplacementsLibres(e4);
      assertEquals(0, (long) nb);

      final Enceinte newE = new Enceinte();
      nb = enceinteManager.getNumberEmplacementsLibres(newE);
      assertEquals(0, (long) nb);

      nb = enceinteManager.getNumberEmplacementsLibres(null);
      assertEquals(0, (long) nb);
   }

   @Test
   public void testGetNumberEmplacements(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Long nb = enceinteManager.getNbEmplacementsLibresByPS(e1);
      assertEquals(397, (long) nb);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      nb = enceinteManager.getNbEmplacementsLibresByPS(e2);
      assertEquals(148, (long) nb);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      nb = enceinteManager.getNbEmplacementsLibresByPS(e3);
      assertEquals(297, (long) nb);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      nb = enceinteManager.getNbEmplacementsLibresByPS(e4);
      assertEquals(0, (long) nb);

      final Enceinte newE = new Enceinte();
      nb = enceinteManager.getNbEmplacementsLibresByPS(newE);
      assertEquals(0, (long) nb);

      nb = enceinteManager.getNbEmplacementsLibresByPS(null);
      assertEquals(0, (long) nb);
   }

   @Test
   public void testGetNumberEmplacementsOccupes(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Long nb = enceinteManager.getNbEmplacementsOccupesByPS(e1);
      assertEquals(3, (long) nb);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      nb = enceinteManager.getNbEmplacementsOccupesByPS(e2);
      assertEquals(0, (long) nb);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      nb = enceinteManager.getNbEmplacementsOccupesByPS(e3);
      assertEquals(3, (long) nb);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      nb = enceinteManager.getNbEmplacementsOccupesByPS(e4);
      assertEquals(0, (long) nb);

      final Enceinte newE = new Enceinte();
      nb = enceinteManager.getNbEmplacementsOccupesByPS(newE);
      assertEquals(0, (long) nb);

      nb = enceinteManager.getNbEmplacementsOccupesByPS(null);
      assertEquals(0, (long) nb);
   }

   @Test
   public void testGetObjetIdsByEntiteByPS(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<Integer> list = new ArrayList<>();
      list = enceinteManager.getObjetIdsByEntiteByPS(e1, entiteDao.findById(3));
      assertEquals(1, list.size());

      list = enceinteManager.getObjetIdsByEntiteByPS(e1, entiteDao.findById(8));
      assertEquals(2, list.size());

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.getObjetIdsByEntiteByPS(e2, entiteDao.findById(3));
      assertEquals(0, list.size());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.getObjetIdsByEntiteByPS(e3, entiteDao.findById(3));
      assertEquals(1, list.size());

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      list = enceinteManager.getObjetIdsByEntiteByPS(e4, entiteDao.findById(3));
      assertEquals(0, list.size());

      final Enceinte newE = new Enceinte();
      list = enceinteManager.getObjetIdsByEntiteByPS(newE, entiteDao.findById(3));
      assertEquals(0, list.size());

      list = enceinteManager.getObjetIdsByEntiteByPS(null, entiteDao.findById(3));
      assertEquals(0, list.size());

      list = enceinteManager.getObjetIdsByEntiteByPS(e3, new Entite());
      assertEquals(0, list.size());

      list = enceinteManager.getObjetIdsByEntiteByPS(e3, null);
      assertEquals(0, list.size());
   }

   /**
    * Teste la méthode getAdrlManager.
    */
   @Test
   public void testGetAdrlManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      String path = enceinteManager.getAdrlManager(e1);
      assertEquals("CC1", path);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      path = enceinteManager.getAdrlManager(e2);
      assertEquals("CC1", path);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      path = enceinteManager.getAdrlManager(e3);
      assertEquals("CC1.R1", path);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      path = enceinteManager.getAdrlManager(e4);
      assertEquals("CC1.R1", path);

      final Enceinte newE = new Enceinte();
      path = enceinteManager.getAdrlManager(newE);
      assertEquals("", path);

      path = enceinteManager.getAdrlManager(null);
      assertEquals("", path);
   }

   /**
    * Teste la méthode getConteneurManager.
    */
   @Test
   public void testGetConteneurManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Conteneur cont = enceinteManager.getConteneurManager(e1);
      assertEquals(1, (int) cont.getConteneurId());

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      cont = enceinteManager.getConteneurManager(e2);
      assertEquals(1, (int) cont.getConteneurId());

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      cont = enceinteManager.getConteneurManager(e3);
      assertEquals(1, (int) cont.getConteneurId());

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      cont = enceinteManager.getConteneurManager(e4);
      assertEquals(1, (int) cont.getConteneurId());

      final Enceinte newE = new Enceinte();
      cont = enceinteManager.getConteneurManager(newE);
      assertNull(cont);

      cont = enceinteManager.getConteneurManager(null);
      assertNull(cont);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      String nom1 = "R1";
      String nom2 = "R10";
      final Conteneur c1 = conteneurDao.findById(1);
      final Conteneur c2 = conteneurDao.findById(2);

      Enceinte e1 = new Enceinte();
      assertFalse(enceinteManager.findDoublonManager(e1));
      e1.setNom(nom1);
      e1.setConteneur(c1);
      assertTrue(enceinteManager.findDoublonManager(e1));
      e1.setNom(nom2);
      assertFalse(enceinteManager.findDoublonManager(e1));
      e1.setNom(nom1);
      e1.setConteneur(c2);
      assertFalse(enceinteManager.findDoublonManager(e1));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      assertFalse(enceinteManager.findDoublonManager(e2));
      e2.setNom(nom1);
      assertTrue(enceinteManager.findDoublonManager(e2));
      e2.setConteneur(c2);
      assertFalse(enceinteManager.findDoublonManager(e2));

      nom1 = "T1";
      nom2 = "T10";

      final Enceinte e3 = new Enceinte();
      assertFalse(enceinteManager.findDoublonManager(e3));
      e1 = enceinteManager.findByIdManager(1);
      e3.setNom(nom1);
      e3.setEnceintePere(e1);
      assertTrue(enceinteManager.findDoublonManager(e3));
      e3.setNom(nom2);
      assertFalse(enceinteManager.findDoublonManager(e3));
      e3.setNom(nom1);
      e3.setEnceintePere(e2);
      assertFalse(enceinteManager.findDoublonManager(e3));

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      assertFalse(enceinteManager.findDoublonManager(e4));
      e4.setNom(nom1);
      assertTrue(enceinteManager.findDoublonManager(e4));
      e4.setEnceintePere(e2);
      assertFalse(enceinteManager.findDoublonManager(e4));

      assertFalse(enceinteManager.findDoublonManager(null));

   }

   /**
    * Test la méthode findDoublonWithoutOneEnceinteManager.
    */
   @Test
   public void testFindDoublonWithoutOneEnceinteManager(){

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      final Enceinte e2 = enceinteManager.findByIdManager(2);
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e1, e2));

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      final Enceinte e6 = enceinteManager.findByIdManager(6);
      final Enceinte e7 = enceinteManager.findByIdManager(7);

      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, e6));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e6, e3));

      e3.setEnceintePere(e7.getEnceintePere());
      assertTrue(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, e7));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, e6));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e6, e3));

      e3.setEnceintePere(null);
      e3.setConteneur(e1.getConteneur());
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, e1));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, e2));

      e3.setConteneur(null);
      e3.setEnceintePere(e1);
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, e7));

      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(null, e7));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, null));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(null, null));

      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(e3, new Enceinte()));
      assertFalse(enceinteManager.findDoublonWithoutTwoEnceintesManager(new Enceinte(), e7));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertTrue(enceinteManager.isUsedObjectManager(e1));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      assertFalse(enceinteManager.isUsedObjectManager(e2));

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      assertTrue(enceinteManager.isUsedObjectManager(e3));

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      assertFalse(enceinteManager.isUsedObjectManager(e4));

      final Enceinte e5 = enceinteManager.findByIdManager(5);
      assertFalse(enceinteManager.isUsedObjectManager(e5));

      final Enceinte e6 = enceinteManager.findByIdManager(6);
      assertFalse(enceinteManager.isUsedObjectManager(e6));

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      assertFalse(enceinteManager.isUsedObjectManager(e7));

      final Enceinte newE = new Enceinte();
      assertFalse(enceinteManager.isUsedObjectManager(newE));

      assertFalse(enceinteManager.isUsedObjectManager(null));
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

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   private void createObjectManagerTest() throws ParseException{

      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte enc = enceinteManager.findByIdManager(1);
      final Conteneur cont = conteneurDao.findById(1);
      final EnceinteType type = enceinteTypeDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Banque bank1 = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(bank1);
      banks.add(bank2);

      final Enceinte e1 = new Enceinte();
      e1.setNbPlaces(6);
      e1.setNom("R3");
      e1.setPosition(3);

      Boolean catched = false;
      // on test l'insertion avec l'enceinteType nulle
      try{
         enceinteManager.createObjectManager(e1, null, cont, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test l'insertion avec d'un conteneur et d'une enceintePere
      // non nulls
      try{
         enceinteManager.createObjectManager(e1, type, cont, enc, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test l'insertion avec d'un conteneur et d'une enceintePere
      // nulls
      try{
         enceinteManager.createObjectManager(e1, type, null, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test l'insertion d'un doublon dans un conteneur
      e1.setNom("R1");
      try{
         enceinteManager.createObjectManager(e1, type, cont, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test l'insertion d'un doublon dans une enceinte
      e1.setNom("T1");
      try{
         enceinteManager.createObjectManager(e1, type, null, enc, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la validation de la position dans un conteneur
      e1.setNom("T10");
      e1.setPosition(10);
      try{
         enceinteManager.createObjectManager(e1, type, cont, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la validation de la position dans une enceinte
      e1.setNom("R10");
      try{
         enceinteManager.createObjectManager(e1, type, null, enc, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on vérifie que la position est libre dans un conteneur
      e1.setNom("T10");
      e1.setPosition(1);
      try{
         enceinteManager.createObjectManager(e1, type, cont, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on vérifie que la position est libre dans une enceinte
      e1.setNom("R10");
      e1.setPosition(1);
      try{
         enceinteManager.createObjectManager(e1, type, null, enc, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // Test de la validation lors de la création
      e1.setNom("T3");
      e1.setPosition(3);
      try{
         validationInsert(e1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test l'insertion avec incoherence sur banque
      final Conteneur c4 = conteneurDao.findById(4);
      e1.setNom("R3");
      e1.setPosition(3);
      try{
         enceinteManager.createObjectManager(e1, type, c4, null, null, banks, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("IncoherenceException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // On test une insertion valide dans un conteneur avec les 
      // assos non obligatoires à null
      e1.setNom("R3");
      e1.setPosition(3);
      e1.setAlias("ALIAS");
      e1.setNbPlaces(6);
      enceinteManager.createObjectManager(e1, type, cont, null, null, null, null, u);
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(1, getOperationManager().findByObjectManager(e1).size());
      final int id = e1.getEnceinteId();
      // Vérification
      final Enceinte eTest = enceinteManager.findByIdManager(id);
      assertNotNull(eTest);
      assertNotNull(eTest.getConteneur());
      assertNotNull(eTest.getEnceinteType());
      assertNull(eTest.getEnceintePere());
      assertEquals(3, (int) eTest.getPosition());
      assertEquals("R3", eTest.getNom());
      assertEquals("ALIAS", eTest.getAlias());
      assertEquals(6, (int) eTest.getNbPlaces());
      assertEquals(0, enceinteManager.getBanquesManager(eTest).size());

      // On test une insertion valide dans une enceinte avec 
      // toutes les assos
      final Enceinte e2 = new Enceinte();
      e2.setNom("T3");
      e2.setPosition(3);
      e2.setAlias("ALIAS3");
      e2.setNbPlaces(5);

      enceinteManager.createObjectManager(e2, type, null, enc, ent, banks, couleurDao.findById(7), u);
      assertEquals(9, enceinteManager.findAllObjectsManager().size());
      assertEquals(1, getOperationManager().findByObjectManager(e2).size());
      final int id2 = e2.getEnceinteId();
      // Vérification
      final Enceinte eTest2 = enceinteManager.findByIdManager(id2);
      assertNotNull(eTest2);
      assertNotNull(eTest2.getEnceintePere());
      assertNotNull(eTest2.getEnceinteType());
      assertNotNull(eTest2.getEntite());
      assertNotNull(eTest2.getCouleur());
      assertNull(eTest2.getConteneur());
      assertEquals(3, (int) eTest2.getPosition());
      assertEquals("T3", eTest2.getNom());
      assertEquals("ALIAS3", eTest2.getAlias());
      assertEquals(5, (int) eTest2.getNbPlaces());
      assertEquals(2, enceinteManager.getBanquesManager(eTest2).size());

      // Suppression
      enceinteManager.removeObjectManager(eTest, null, u);
      enceinteManager.removeObjectManager(eTest2, null, u);
      assertEquals(0, getOperationManager().findByObjectManager(eTest).size());
      assertEquals(0, getOperationManager().findByObjectManager(eTest2).size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(eTest);
      fs.add(eTest2);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode updateObjectManager. 
    * @throws ParseException 
    */
   private void updateObjectManagerTest() throws ParseException{

      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte enc = enceinteManager.findByIdManager(1);
      final Conteneur cont = conteneurDao.findById(1);
      final EnceinteType type = enceinteTypeDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Banque bank1 = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final Banque bank3 = banqueDao.findById(3);
      List<Banque> banks = new ArrayList<>();
      banks.add(bank1);
      banks.add(bank2);

      final Enceinte en = new Enceinte();
      en.setNbPlaces(3);
      en.setNom("TEST3");
      en.setPosition(3);
      enceinteManager.createObjectManager(en, type, null, enc, null, null, null, u);
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(1, getOperationManager().findByObjectManager(en).size());
      final Integer id = en.getEnceinteId();

      final Enceinte eUp1 = enceinteManager.findByIdManager(id);
      Boolean catched = false;
      // on test l'update avec l'enceinteType nulle
      try{
         enceinteManager.updateObjectManager(eUp1, null, cont, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test l'update avec d'un conteneur et d'une enceintePere
      // non nulls
      try{
         enceinteManager.updateObjectManager(eUp1, type, cont, enc, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test l'update avec d'un conteneur et d'une enceintePere
      // nulls
      try{
         enceinteManager.updateObjectManager(eUp1, type, null, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test l'update d'un doublon dans un conteneur
      eUp1.setNom("R1");
      try{
         enceinteManager.updateObjectManager(eUp1, type, cont, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test l'update d'un doublon dans une enceinte
      eUp1.setNom("T1");
      try{
         enceinteManager.updateObjectManager(eUp1, type, null, enc, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test la validation de la position dans un conteneur
      eUp1.setNom("T10");
      eUp1.setPosition(10);
      try{
         enceinteManager.updateObjectManager(eUp1, type, cont, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test la validation de la position dans une enceinte
      eUp1.setNom("R10");
      try{
         enceinteManager.updateObjectManager(eUp1, type, null, enc, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on vérifie que la position est libre dans un conteneur
      eUp1.setNom("T10");
      eUp1.setPosition(1);
      try{
         enceinteManager.updateObjectManager(eUp1, type, cont, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on vérifie que la position est libre dans une enceinte
      eUp1.setNom("R10");
      eUp1.setPosition(1);
      try{
         enceinteManager.updateObjectManager(eUp1, type, null, enc, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // Test de la validation lors de l'update
      eUp1.setNom("T3");
      eUp1.setPosition(3);
      try{
         validationUpdate(eUp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test la modification avec incoherence sur banque
      final Conteneur c4 = conteneurDao.findById(4);
      eUp1.setNom("R3");
      eUp1.setPosition(3);
      try{
         enceinteManager.updateObjectManager(eUp1, type, c4, null, null, banks, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("IncoherenceException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());

      // on test la modification avec incident incoherent
      final List<Incident> incs = new ArrayList<>();
      Incident inc1 = new Incident();
      inc1.setNom("TEST");
      inc1.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("08/11/2012"));
      final Incident inc2 = new Incident();
      inc2.setNom("¤¤$$ùù%%%");
      inc2.setDate(new SimpleDateFormat("dd/MM/yyyy").parse("09/11/2012"));
      incs.add(inc1);
      incs.add(inc2);

      eUp1.setNom("R3");
      eUp1.setPosition(3);
      eUp1.setAlias("ALIAS3");
      eUp1.setNbPlaces(6);
      catched = false;
      try{
         enceinteManager.updateObjectManager(eUp1, type, cont, null, null, banks, null, incs, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(5, incidentManager.findAllObjectsManager().size());

      incs.remove(1);

      // On test un update valide dans un conteneur avec les 
      // assos non obligatoires à null
      enceinteManager.updateObjectManager(eUp1, type, cont, null, null, banks, null, incs, u, null);
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(2, getOperationManager().findByObjectManager(eUp1).size());
      // Vérification
      final Enceinte eTest = enceinteManager.findByIdManager(id);
      assertNotNull(eTest);
      assertNotNull(eTest.getConteneur());
      assertNotNull(eTest.getEnceinteType());
      assertNull(eTest.getEnceintePere());
      assertEquals(3, (int) eTest.getPosition());
      assertEquals("R3", eTest.getNom());
      assertEquals("ALIAS3", eTest.getAlias());
      assertEquals(6, (int) eTest.getNbPlaces());
      assertEquals(2, enceinteManager.getBanquesManager(eTest).size());

      inc1 = incidentManager.findByEnceinteManager(eTest).get(0);
      assertEquals("TEST", inc1.getNom());
      assertNull(inc1.getDescription());

      // On test un update valide dans une enceinte avec 
      // toutes les assos
      final Enceinte eUp2 = enceinteManager.findByIdManager(id);
      eUp2.setNom("T3");
      eUp2.setPosition(3);
      eUp2.setAlias("ALIAS3");
      eUp2.setNbPlaces(5);
      banks = new ArrayList<>();
      banks.add(bank3);
      final List<OperationType> opTypes = new ArrayList<>();
      opTypes.add(operationTypeManager.findByIdManager(1));
      opTypes.add(operationTypeManager.findByIdManager(2));

      incs.clear();
      inc1.setDescription("incident pour enceinte update");
      incs.add(inc1);

      enceinteManager.updateObjectManager(eUp2, type, null, enc, ent, banks, couleurDao.findById(5), incs, u, opTypes);
      assertEquals(5, getOperationManager().findByObjectManager(eUp2).size());
      // Vérification
      final Enceinte eTest2 = enceinteManager.findByIdManager(id);
      assertNotNull(eTest2);
      assertNotNull(eTest2.getEnceintePere());
      assertNotNull(eTest2.getEnceinteType());
      assertNotNull(eTest2.getEntite());
      assertNotNull(eTest2.getCouleur());
      assertNull(eTest2.getConteneur());
      assertEquals(3, (int) eTest2.getPosition());
      assertEquals("T3", eTest2.getNom());
      assertEquals("ALIAS3", eTest2.getAlias());
      assertEquals(5, (int) eTest2.getNbPlaces());
      assertEquals(1, enceinteManager.getBanquesManager(eTest2).size());
      final Iterator<Banque> it = enceinteManager.getBanquesManager(eTest2).iterator();
      assertEquals("BANQUE3", it.next().getNom());

      inc1 = incidentManager.findByEnceinteManager(eTest2).get(0);
      assertEquals("TEST", inc1.getNom());
      assertEquals("incident pour enceinte update", inc1.getDescription());

      // Suppression
      enceinteManager.removeObjectManager(eTest2, null, u);
      assertEquals(0, getOperationManager().findByObjectManager(eTest2).size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(5, incidentManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(eTest2);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   private void removeObjectManagerTest(){
      final Utilisateur u = utilisateurDao.findById(1);
      // test de la suppression d'un objet null
      enceinteManager.removeObjectManager(null, null, null);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // test de la suppression d'un objet utilisé
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      boolean catched = false;
      try{
         enceinteManager.removeObjectManager(e1, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            assertEquals("enceinte.deletion.isUsed", ((ObjectUsedException) e).getKey());
            catched = true;
         }
      }
      assertTrue(catched);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // creation d'une 1ère enceinte
      final Enceinte newE = new Enceinte();
      final Conteneur cont = conteneurDao.findById(2);
      final EnceinteType type = enceinteTypeDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      banks.add(b2);
      newE.setNom("R1");
      newE.setNbPlaces(5);
      newE.setPosition(1);
      enceinteManager.createObjectManager(newE, type, cont, null, null, banks, null, u);
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      final Integer id = newE.getEnceinteId();

      // création de 5 enceintes pour celle-ci
      final Enceinte supE = enceinteManager.findByIdManager(id);
      final Enceinte newE2 = new Enceinte();
      newE2.setNom("T");
      newE2.setNbPlaces(5);
      newE2.setEnceinteType(type);
      final List<Enceinte> encs = enceinteManager.createMultiObjetcsForEnceinteManager(supE, newE2, 5, null, u);
      assertEquals(13, enceinteManager.findAllObjectsManager().size());

      // création de 5 terminales pour la 1ère enceinte
      final TerminaleType tType = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Terminale newTs = new Terminale();
      newTs.setTerminaleNumerotation(num);
      newTs.setTerminaleType(tType);
      newTs.setNom("BT");
      newTs.setAlias("ALIAS");
      terminaleManager.createMultiObjetcsManager(encs.get(0), newTs, 5, null, u);
      assertEquals(11, terminaleManager.findAllObjectsManager().size());

      final List<Enceinte> enceintes = enceinteManager.findByEnceintePereWithOrderManager(supE);

      final List<Terminale> terms = new ArrayList<>();
      for(int i = 0; i < enceintes.size(); i++){
         terms.addAll(terminaleManager.findByEnceinteWithOrderManager(enceintes.get(i)));
      }

      enceinteManager.removeObjectManager(supE, null, u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(supE);
      fs.addAll(enceintes);
      fs.addAll(terms);
      cleanUpFantomes(fs);
   }

   /**
    * Test la validation d'une Enceinte lors de sa création.
    * @param enceinte Enceinte à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Enceinte enceinte) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur cont = conteneurDao.findById(1);
      final EnceinteType type = enceinteTypeDao.findById(1);
      boolean catchedInsert = false;

      // On teste une insertion avec un attribut position non valide
      catchedInsert = false;
      try{
         enceinte.setPosition(-1);
         enceinteManager.createObjectManager(enceinte, type, cont, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      enceinte.setPosition(3);

      // On teste une insertion avec un attribut nom non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            enceinte.setNom(emptyValues[i]);
            enceinteManager.createObjectManager(enceinte, type, cont, null, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      enceinte.setNom("T3");

      // On teste une insertion avec un attribut alias non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            enceinte.setAlias(emptyValues[i]);
            enceinteManager.createObjectManager(enceinte, type, cont, null, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      enceinte.setAlias(null);

      // On teste une insertion avec un attribut nbPlaces non valide
      final Integer[] nbPlacesValues = new Integer[] {null, -1};
      for(int i = 0; i < nbPlacesValues.length; i++){
         catchedInsert = false;
         try{
            enceinte.setNbPlaces(nbPlacesValues[i]);
            enceinteManager.createObjectManager(enceinte, type, cont, null, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      enceinte.setNbPlaces(6);
   }

   /**
    * Test la validation d'une Enceinte lors de son update.
    * @param enceinte Enceinte à tester.
    * @throws ParseException 
    */
   private void validationUpdate(Enceinte enceinte) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur cont = conteneurDao.findById(1);
      final EnceinteType type = enceinteTypeDao.findById(1);
      boolean catched = false;

      // On teste une insertion avec un attribut position non valide
      catched = false;
      try{
         enceinte.setPosition(-1);
         enceinteManager.updateObjectManager(enceinte, type, cont, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      enceinte.setPosition(3);

      // On teste une insertion avec un attribut nom non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$¤¤*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            enceinte.setNom(emptyValues[i]);
            enceinteManager.updateObjectManager(enceinte, type, cont, null, null, null, null, null, u, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      enceinte.setNom("T3");

      // On teste une insertion avec un attribut alias non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            enceinte.setAlias(emptyValues[i]);
            enceinteManager.updateObjectManager(enceinte, type, cont, null, null, null, null, null, u, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      enceinte.setAlias(null);

      // On teste une insertion avec un attribut nbPlaces non valide
      final Integer[] nbPlacesValues = new Integer[] {null, -1};
      for(int i = 0; i < nbPlacesValues.length; i++){
         catched = false;
         try{
            enceinte.setNbPlaces(nbPlacesValues[i]);
            enceinteManager.updateObjectManager(enceinte, type, cont, null, null, null, null, null, u, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      enceinte.setNbPlaces(6);

      // On teste une insertion avec un doublon incident
      enceinte = enceinteManager.findByIdManager(1);
      final List<Incident> incs = incidentManager.findByEnceinteManager(enceinte);
      final Incident inc1 = new Incident();
      inc1.setNom(incs.get(0).getNom());
      inc1.setDate(incs.get(0).getDate());
      incs.add(inc1);
      catched = false;
      try{
         enceinteManager.updateObjectManager(enceinte, type, cont, null, null, null, null, incs, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

      // On teste une insertion avec un doublon in list
      incs.clear();
      inc1.setNom("TEST");
      final Incident inc2 = inc1.clone();
      incs.add(inc1);
      incs.add(inc2);

      try{
         enceinteManager.updateObjectManager(enceinte, type, cont, null, null, null, null, incs, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);

   }

   /**
    * Test de la méthode createMultiObjetcsForConteneurManager().
    */
   @Test
   public void testCreateMultiObjetcsForConteneurManager(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur cont1 = conteneurDao.findById(1);
      final Conteneur cont2 = conteneurDao.findById(2);
      final EnceinteType type = enceinteTypeDao.findById(1);
      final Entite ent = entiteDao.findById(3);

      final Enceinte newEs = new Enceinte();
      newEs.setEnceinteType(type);
      newEs.setEntite(ent);
      newEs.setNom("R");
      newEs.setAlias("ALIAS");
      newEs.setNbPlaces(5);

      // Test de la création avec l'enceinte à null
      enceinteManager.createMultiObjetcsForConteneurManager(cont2, null, 2, null, u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      Boolean catched = false;
      // on test la création avec le conteneur nulle
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(null, newEs, 2, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la création avec un nombre trop grand
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, 5, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la création avec un nombre null
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, null, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la création avec un nombre négatif
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, -10, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la génération d'une exception
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(cont1, newEs, 2, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on teste une création multiple valide avec la premiere position
      // à 125
      final List<Enceinte> list = enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, 2, 125, u);
      assertEquals(2, list.size());
      assertEquals(9, enceinteManager.findAllObjectsManager().size());
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getConteneur());
      assertNull(list.get(0).getEnceintePere());
      assertNotNull(list.get(0).getEnceinteType());
      assertNotNull(list.get(0).getEntite());
      assertEquals(1, (int) list.get(0).getPosition());
      assertEquals("ALIAS1", list.get(0).getAlias());
      assertEquals(5, (int) list.get(0).getNbPlaces());
      assertFalse(list.get(0).getArchive());
      assertEquals("R125", list.get(0).getNom());
      assertEquals("R126", list.get(1).getNom());

      for(int i = 0; i < list.size(); i++){
         enceinteManager.removeObjectManager(list.get(i), null, u);
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on teste une création multiple valide avec la premiere position
      // à null
      final List<Enceinte> list2 = enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, 2, null, u);
      assertEquals(2, list2.size());
      assertEquals(9, enceinteManager.findAllObjectsManager().size());
      assertNotNull(list2.get(0));
      assertNotNull(list2.get(0).getConteneur());
      assertNull(list2.get(0).getEnceintePere());
      assertNotNull(list2.get(0).getEnceinteType());
      assertNotNull(list2.get(0).getEntite());
      assertEquals(1, (int) list2.get(0).getPosition());
      assertEquals("ALIAS1", list2.get(0).getAlias());
      assertEquals(5, (int) list2.get(0).getNbPlaces());
      assertFalse(list2.get(0).getArchive());
      assertEquals("R1", list2.get(0).getNom());
      assertEquals("R2", list2.get(1).getNom());

      for(int i = 0; i < list2.size(); i++){
         enceinteManager.removeObjectManager(list2.get(i), null, u);
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      fs.addAll(list2);
      cleanUpFantomes(fs);
   }

   /**
    * Test de la méthode createMultiObjetcsForConteneurManager().
    */
   @Test
   public void testCreateMultiObjetcsForEnceinteManager(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte e3 = enceinteManager.findByIdManager(3);
      final Enceinte e4 = enceinteManager.findByIdManager(4);
      final EnceinteType type = enceinteTypeDao.findById(1);
      final Entite ent = entiteDao.findById(3);

      final Enceinte newEs = new Enceinte();
      newEs.setEnceinteType(type);
      newEs.setEntite(ent);
      newEs.setNom("T");
      newEs.setAlias("ALIAS");
      newEs.setNbPlaces(5);

      // Test de la création avec l'enceinte à null
      enceinteManager.createMultiObjetcsForEnceinteManager(e4, null, 2, null, u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      Boolean catched = false;
      // on test la création avec le conteneur nulle
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(null, newEs, 2, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la création avec un nombre trop grand
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, 15, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la création avec un nombre null
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, null, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la création avec un nombre négatif
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, -10, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on test la génération d'une exception
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(e3, newEs, 2, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on teste une création multiple valide avec la 1re pos à 58
      final List<Enceinte> list = enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, 5, 58, u);
      assertEquals(5, list.size());
      assertEquals(12, enceinteManager.findAllObjectsManager().size());
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getEnceintePere());
      assertNull(list.get(0).getConteneur());
      assertNotNull(list.get(0).getEnceinteType());
      assertNotNull(list.get(0).getEntite());
      assertEquals(1, (int) list.get(0).getPosition());
      assertEquals("ALIAS1", list.get(0).getAlias());
      assertEquals(5, (int) list.get(0).getNbPlaces());
      assertFalse(list.get(0).getArchive());
      assertEquals("T58", list.get(0).getNom());
      assertEquals("T59", list.get(1).getNom());
      assertEquals("T62", list.get(4).getNom());

      for(int i = 0; i < list.size(); i++){
         enceinteManager.removeObjectManager(list.get(i), null, u);
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      // on teste une création multiple valide avec la 1re pos à null
      final List<Enceinte> list2 = enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, 5, null, u);
      assertEquals(5, list2.size());
      assertEquals(12, enceinteManager.findAllObjectsManager().size());
      assertNotNull(list2.get(0));
      assertNotNull(list2.get(0).getEnceintePere());
      assertNull(list2.get(0).getConteneur());
      assertNotNull(list2.get(0).getEnceinteType());
      assertNotNull(list2.get(0).getEntite());
      assertEquals(1, (int) list2.get(0).getPosition());
      assertEquals("ALIAS1", list2.get(0).getAlias());
      assertEquals(5, (int) list2.get(0).getNbPlaces());
      assertFalse(list2.get(0).getArchive());
      assertEquals("T1", list2.get(0).getNom());
      assertEquals("T2", list2.get(1).getNom());
      assertEquals("T5", list2.get(4).getNom());

      for(int i = 0; i < list2.size(); i++){
         enceinteManager.removeObjectManager(list2.get(i), null, u);
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      fs.addAll(list2);
      cleanUpFantomes(fs);
   }

   /**
    * Test de la méthode echangerDeuxEnceintesManager().
    */
   @Test
   public void testEchangerDeuxEnceintesManager(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte e3 = enceinteManager.findByIdManager(3);
      final Enceinte e6 = enceinteManager.findByIdManager(6);
      final Enceinte e7 = enceinteManager.findByIdManager(7);

      final Conteneur c3 = e3.getConteneur();
      final Enceinte ep3 = e3.getEnceintePere();
      final Integer pos3 = e3.getPosition();

      final Conteneur c6 = e6.getConteneur();
      final Enceinte ep6 = e6.getEnceintePere();
      final Integer pos6 = e6.getPosition();

      final Conteneur c7 = e7.getConteneur();
      final Enceinte ep7 = e7.getEnceintePere();
      final Integer pos7 = e7.getPosition();

      enceinteManager.echangerDeuxEnceintesManager(e3, null, u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      Enceinte eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep3);
      assertEquals(eTest3.getPosition(), pos3);

      enceinteManager.echangerDeuxEnceintesManager(e3, new Enceinte(), u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep3);
      assertEquals(eTest3.getPosition(), pos3);

      enceinteManager.echangerDeuxEnceintesManager(null, e3, u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep3);
      assertEquals(eTest3.getPosition(), pos3);

      enceinteManager.echangerDeuxEnceintesManager(new Enceinte(), e3, u);
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep3);
      assertEquals(eTest3.getPosition(), pos3);

      // on teste les doublons
      e3.setConteneur(c7);
      e3.setEnceintePere(ep7);
      e3.setPosition(pos7);
      e7.setConteneur(c3);
      e7.setEnceintePere(ep3);
      e7.setPosition(pos3);
      boolean catched = false;
      try{
         enceinteManager.echangerDeuxEnceintesManager(e3, e7, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      try{
         enceinteManager.echangerDeuxEnceintesManager(e7, e3, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on teste un déplacement valide
      e3.setConteneur(c6);
      e3.setEnceintePere(ep6);
      e3.setPosition(pos6);
      e6.setConteneur(c3);
      e6.setEnceintePere(ep3);
      e6.setPosition(pos3);
      enceinteManager.echangerDeuxEnceintesManager(e3, e6, u);
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep6);
      assertEquals(eTest3.getPosition(), pos6);
      Enceinte eTest6 = enceinteManager.findByIdManager(6);
      assertNull(eTest6.getConteneur());
      assertEquals(eTest6.getEnceintePere(), ep3);
      assertEquals(eTest6.getPosition(), pos3);
      assertEquals(1, getOperationManager().findByObjectManager(eTest3).size());
      assertEquals(1, getOperationManager().findByObjectManager(eTest6).size());

      // on refait l'échange dans l'autre sens
      e3.setConteneur(c3);
      e3.setEnceintePere(ep3);
      e3.setPosition(pos3);
      e6.setConteneur(c6);
      e6.setEnceintePere(ep6);
      e6.setPosition(pos6);
      enceinteManager.echangerDeuxEnceintesManager(e3, e6, u);
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep3);
      assertEquals(eTest3.getPosition(), pos3);
      eTest6 = enceinteManager.findByIdManager(6);
      assertNull(eTest6.getConteneur());
      assertEquals(eTest6.getEnceintePere(), ep6);
      assertEquals(eTest6.getPosition(), pos6);
      assertEquals(2, getOperationManager().findByObjectManager(eTest3).size());
      assertEquals(2, getOperationManager().findByObjectManager(eTest6).size());

      // on teste l'échange de 2 enceintes cote a cote
      final Enceinte e4 = enceinteManager.findByIdManager(4);
      final Conteneur c4 = e4.getConteneur();
      final Enceinte ep4 = e4.getEnceintePere();
      final Integer pos4 = e4.getPosition();

      e3.setConteneur(c4);
      e3.setEnceintePere(ep4);
      e3.setPosition(pos4);
      e4.setConteneur(c3);
      e4.setEnceintePere(ep3);
      e4.setPosition(pos3);
      enceinteManager.echangerDeuxEnceintesManager(e3, e4, u);
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep4);
      assertEquals(eTest3.getPosition(), pos4);
      Enceinte eTest4 = enceinteManager.findByIdManager(4);
      assertNull(eTest4.getConteneur());
      assertEquals(eTest4.getEnceintePere(), ep3);
      assertEquals(eTest4.getPosition(), pos3);

      // on refait l'échange dans l'autre sens
      e3.setConteneur(c3);
      e3.setEnceintePere(ep3);
      e3.setPosition(pos3);
      e4.setConteneur(c4);
      e4.setEnceintePere(ep4);
      e4.setPosition(pos4);
      enceinteManager.echangerDeuxEnceintesManager(e3, e4, u);
      eTest3 = enceinteManager.findByIdManager(3);
      assertNull(eTest3.getConteneur());
      assertEquals(eTest3.getEnceintePere(), ep3);
      assertEquals(eTest3.getPosition(), pos3);
      eTest4 = enceinteManager.findByIdManager(4);
      assertNull(eTest4.getConteneur());
      assertEquals(eTest4.getEnceintePere(), ep4);
      assertEquals(eTest4.getPosition(), pos4);

      List<Operation> ops = getOperationManager().findByObjectManager(eTest3);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      ops = getOperationManager().findByObjectManager(eTest6);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      ops = getOperationManager().findByObjectManager(eTest4);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      assertEquals(19, getOperationManager().findAllObjectsManager().size());
   }

   /**
    * Teste la methode createAllArborescenceManager avec des terminales
    * directement. 
    * @throws ParseException 
    */
   @Test
   public void testCreateAllTerminalesManager() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur c = conteneurDao.findById(2);
      final Enceinte eParent = enceinteManager.findByIdManager(3);
      final Banque bank1 = banqueDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(bank1);
      final List<Enceinte> enceintes = new ArrayList<>();

      final EnceinteType eType1 = enceinteTypeDao.findById(6);

      final TerminaleType tType = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);

      // 1ère Enceinte à créer
      final Enceinte e1 = new Enceinte();
      e1.setEnceinteType(eType1);
      e1.setNom("R");
      e1.setNbPlaces(5);
      e1.setConteneur(c);
      e1.setEnceintePere(eParent);
      e1.setPosition(3);

      // Terminale
      final Terminale term = new Terminale();
      term.setTerminaleType(tType);
      term.setNom("BT");
      term.setTerminaleNumerotation(num);

      // 1ères positions
      final List<Integer> goodPositions = new ArrayList<>();
      goodPositions.add(10);
      final List<Integer> badPositions = new ArrayList<>();

      // test avec des éléments nulls ou de mauvais positions
      try{
         enceinteManager.createAllArborescenceManager(null, enceintes, term, goodPositions, banques, u);
         enceinteManager.createAllArborescenceManager(e1, null, term, goodPositions, banques, u);
         enceinteManager.createAllArborescenceManager(e1, enceintes, null, goodPositions, banques, u);
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, null, banques, u);
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, badPositions, banques, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      // test avec exception sur l'enceinte
      boolean catched = false;
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      e1.setConteneur(null);

      // test avec excpetion sur la terminale
      term.setTerminaleType(null);
      catched = false;
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      term.setTerminaleType(tType);

      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(11, terminaleManager.findAllObjectsManager().size());

      final Set<Enceinte> eTests = enceinteManager.getEnceintesManager(eParent);
      assertEquals(1, eTests.size());

      final Terminale tTest = enceinteManager.getTerminalesManager(e1).iterator().next();
      assertEquals("BT10", tTest.getNom());

      final List<Terminale> terms = terminaleManager.findByEnceinteWithOrderManager(e1);

      enceinteManager.removeObjectManager(e1, null, u);
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e1);
      fs.addAll(terms);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode createAllArborescenceManager avec des enceintes et
    * des terminales. 
    * @throws ParseException 
    */
   @Test
   public void testCreateAllArborescenceManager() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur c = conteneurDao.findById(2);
      final Enceinte eParent = enceinteManager.findByIdManager(3);
      final Banque bank1 = banqueDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(bank1);
      final List<Enceinte> enceintes = new ArrayList<>();

      final EnceinteType eType1 = enceinteTypeDao.findById(6);
      final EnceinteType eType2 = enceinteTypeDao.findById(2);
      final EnceinteType eType3 = enceinteTypeDao.findById(1);

      final TerminaleType tType = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);

      // 1ère Enceinte à créer
      final Enceinte e1 = new Enceinte();
      e1.setEnceinteType(eType1);
      e1.setNom("R");
      e1.setNbPlaces(2);
      e1.setConteneur(c);
      e1.setEnceintePere(eParent);
      e1.setPosition(1);

      // Enceinte 2eme niveau
      final Enceinte e2 = new Enceinte();
      e2.setEnceinteType(eType2);
      e2.setNom("T");
      e2.setNbPlaces(3);

      // Enceinte 3eme niveau
      final Enceinte e3 = new Enceinte();
      e3.setEnceinteType(eType3);
      e3.setNom("C");
      e3.setNbPlaces(5);

      enceintes.add(e2);
      enceintes.add(e3);

      // Terminale
      final Terminale term = new Terminale();
      term.setTerminaleType(tType);
      term.setNom("BT");
      term.setTerminaleNumerotation(num);

      // 1ères positions
      final List<Integer> goodPositions = new ArrayList<>();
      goodPositions.add(10);
      goodPositions.add(null);
      goodPositions.add(25);
      final List<Integer> badPositions = new ArrayList<>();

      // test avec des éléments nulls ou de mauvais positions
      try{
         enceinteManager.createAllArborescenceManager(null, enceintes, term, goodPositions, banques, u);
         enceinteManager.createAllArborescenceManager(e1, null, term, goodPositions, banques, u);
         enceinteManager.createAllArborescenceManager(e1, enceintes, null, goodPositions, banques, u);
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, null, banques, u);
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, badPositions, banques, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      // test avec exception sur l'enceinte
      boolean catched = false;
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      e1.setEnceintePere(null);

      // test avec excpetion sur la derniere enceinte
      e3.setEnceinteType(null);
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      e3.setEnceinteType(eType3);

      // test avec excpetion sur la terminale
      term.setTerminaleType(null);
      catched = false;
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      term.setTerminaleType(tType);

      // creation valide
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertEquals(16, enceinteManager.findAllObjectsManager().size());
      assertEquals(36, terminaleManager.findAllObjectsManager().size());

      final Enceinte eTest1 = conteneurManager.getEnceintesManager(c).iterator().next();
      assertEquals("R", eTest1.getNom());
      assertEquals(2, enceinteManager.getEnceintesManager(eTest1).size());

      final Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1).iterator().next();
      assertEquals("T10", eTest2.getNom());
      assertEquals(3, enceinteManager.getEnceintesManager(eTest2).size());

      final Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2).iterator().next();
      assertEquals("C1", eTest3.getNom());
      assertEquals(0, enceinteManager.getEnceintesManager(eTest3).size());
      assertEquals(5, enceinteManager.getTerminalesManager(eTest3).size());

      final Terminale tTest = enceinteManager.getTerminalesManager(eTest3).iterator().next();
      assertEquals("BT25", tTest.getNom());

      final List<Enceinte> dep = new ArrayList<>();
      dep.add(e1);
      final List<Enceinte> encs = new ArrayList<>();
      enceinteManager.findEnceinteRecursiveManager(dep, encs);

      final List<Terminale> terms = new ArrayList<>();
      for(int i = 0; i < encs.size(); i++){
         terms.addAll(terminaleManager.findByEnceinteWithOrderManager(encs.get(i)));
      }

      enceinteManager.removeObjectManager(e1, null, u);
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(e1);
      fs.addAll(encs);
      fs.addAll(terms);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode updatewithCreateAllArborescenceManager
    * avec des enceintes et des terminales. 
    * @throws ParseException 
    */
   @Test
   public void testUpdatewithCreateAllArborescenceManager() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur c = conteneurDao.findById(2);
      final Enceinte eParent = enceinteManager.findByIdManager(3);
      final Banque bank1 = banqueDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(bank1);
      final List<Enceinte> enceintes = new ArrayList<>();

      final EnceinteType eType1 = enceinteTypeDao.findById(6);
      final EnceinteType eType2 = enceinteTypeDao.findById(2);
      final EnceinteType eType3 = enceinteTypeDao.findById(1);

      final TerminaleType tType = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);

      // 1ère Enceinte à créer
      final Enceinte e = new Enceinte();
      e.setEnceinteType(eType1);
      e.setNom("R");
      e.setNbPlaces(2);
      e.setConteneur(c);
      e.setPosition(1);
      enceinteManager.createObjectManager(e, eType1, c, null, null, banques, couleurDao.findById(2), u);
      final Enceinte eUp = enceinteManager.findByIdManager(e.getEnceinteId());
      eUp.setNom("RUP1");
      eUp.setEnceintePere(eParent);

      // Enceinte 2eme niveau
      final Enceinte e2 = new Enceinte();
      e2.setEnceinteType(eType2);
      e2.setNom("T");
      e2.setNbPlaces(3);

      // Enceinte 3eme niveau
      final Enceinte e3 = new Enceinte();
      e3.setEnceinteType(eType3);
      e3.setNom("C");
      e3.setNbPlaces(5);

      enceintes.add(e2);
      enceintes.add(e3);

      // Terminale
      final Terminale term = new Terminale();
      term.setTerminaleType(tType);
      term.setNom("BT");
      term.setTerminaleNumerotation(num);

      // 1ères positions
      final List<Integer> goodPositions = new ArrayList<>();
      goodPositions.add(10);
      goodPositions.add(null);
      goodPositions.add(25);
      final List<Integer> badPositions = new ArrayList<>();

      // test avec des éléments nulls ou de mauvais positions
      try{
         enceinteManager.updatewithCreateAllArborescenceManager(null, enceintes, term, goodPositions, banques, u);
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, null, term, goodPositions, banques, u);
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, null, goodPositions, banques, u);
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, null, banques, u);
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, badPositions, banques, u);
      }catch(final Exception exp){
         exp.printStackTrace();
      }
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      // test avec exception sur l'enceinte
      boolean catched = false;
      try{
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, goodPositions, banques, u);
      }catch(final Exception exp){
         if(exp.getClass().getSimpleName().equals("InvalidParentException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      eUp.setEnceintePere(null);

      // test avec excpetion sur la derniere enceinte
      e3.setEnceinteType(null);
      try{
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, goodPositions, banques, u);
      }catch(final Exception exp){
         if(exp.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      e3.setEnceinteType(eType3);

      // test avec excpetion sur la terminale
      term.setTerminaleType(null);
      catched = false;
      try{
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, goodPositions, banques, u);
      }catch(final Exception exp){
         if(exp.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());
      term.setTerminaleType(tType);

      // creation valide
      try{
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, goodPositions, banques, u);
      }catch(final Exception exp){
         exp.printStackTrace();
      }

      assertEquals(16, enceinteManager.findAllObjectsManager().size());
      assertEquals(36, terminaleManager.findAllObjectsManager().size());

      final Enceinte eTest1 = conteneurManager.getEnceintesManager(c).iterator().next();
      assertEquals("RUP1", eTest1.getNom());
      assertNotNull(eTest1.getCouleur());
      assertEquals(2, enceinteManager.getEnceintesManager(eTest1).size());

      final Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1).iterator().next();
      assertEquals("T10", eTest2.getNom());
      assertEquals(3, enceinteManager.getEnceintesManager(eTest2).size());

      final Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2).iterator().next();
      assertEquals("C1", eTest3.getNom());
      assertEquals(0, enceinteManager.getEnceintesManager(eTest3).size());
      assertEquals(5, enceinteManager.getTerminalesManager(eTest3).size());

      final Terminale tTest = enceinteManager.getTerminalesManager(eTest3).iterator().next();
      assertEquals("BT25", tTest.getNom());

      final List<Enceinte> dep = new ArrayList<>();
      dep.add(eUp);
      final List<Enceinte> encs = new ArrayList<>();
      enceinteManager.findEnceinteRecursiveManager(dep, encs);

      final List<Terminale> terms = new ArrayList<>();
      for(int i = 0; i < encs.size(); i++){
         terms.addAll(terminaleManager.findByEnceinteWithOrderManager(encs.get(i)));
      }

      enceinteManager.removeObjectManager(eUp, null, u);
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(eUp);
      fs.addAll(encs);
      fs.addAll(terms);
      cleanUpFantomes(fs);
   }

   @Test
   public void getConteneurParentManager(){
      final Enceinte e5 = enceinteManager.findByIdManager(6);
      assertEquals(enceinteManager.getConteneurParent(e5), conteneurDao.findById(1));
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertEquals(enceinteManager.getConteneurParent(e1), conteneurDao.findById(1));
      final Enceinte enull = new Enceinte();
      assertNull(enceinteManager.getConteneurParent(enull));
   }

   @Test
   public void testUpdateTailleEnceinte(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte enc = enceinteManager.findByIdManager(1);
      final EnceinteType type = enceinteTypeDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Banque bank1 = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(bank1);
      banks.add(bank2);

      // toutes les assos
      Enceinte e1 = new Enceinte();
      e1.setNom("T3");
      e1.setPosition(3);
      e1.setAlias("ALIAS3");
      e1.setNbPlaces(5);

      enceinteManager.createObjectManager(e1, type, null, enc, ent, banks, null, u);
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(1, getOperationManager().findByObjectManager(e1).size());
      final int id1 = e1.getEnceinteId();
      // Vérification
      final Enceinte eTest1 = enceinteManager.findByIdManager(id1);
      assertNotNull(eTest1);
      assertNotNull(eTest1.getEnceintePere());
      assertNotNull(eTest1.getEnceinteType());
      assertNotNull(eTest1.getEntite());
      assertNull(eTest1.getConteneur());
      assertEquals(3, (int) eTest1.getPosition());
      assertEquals("T3", eTest1.getNom());
      assertEquals("ALIAS3", eTest1.getAlias());
      assertEquals(5, (int) eTest1.getNbPlaces());
      assertEquals(2, enceinteManager.getBanquesManager(eTest1).size());

      // update de la taille
      final Enceinte up = enceinteManager.findByIdManager(id1);
      Enceinte eTest2 = enceinteManager.updateTailleEnceinteManager(up, 5, u);
      assertEquals(8, enceinteManager.findAllObjectsManager().size());
      assertEquals(2, getOperationManager().findByObjectManager(eTest2).size());
      assertEquals(10, (int) eTest2.getNbPlaces());
      assertEquals(eTest2.getEnceintePere(), eTest1.getEnceintePere());
      assertEquals(eTest2.getEnceinteType(), eTest1.getEnceinteType());
      assertEquals(eTest2.getEntite(), eTest1.getEntite());
      assertNull(eTest2.getConteneur());
      assertEquals(eTest2.getPosition(), eTest1.getPosition());
      assertEquals(eTest2.getNom(), eTest1.getNom());
      assertEquals(eTest2.getAlias(), eTest1.getAlias());
      assertEquals(2, enceinteManager.getBanquesManager(eTest2).size());

      // enceintes filles
      final Enceinte e2 = new Enceinte();
      e2.setNom("T3-1");
      e2.setPosition(1);
      e2.setAlias("T3_1");
      e2.setNbPlaces(5);

      enceinteManager.createObjectManager(e2, type, null, eTest2, ent, banks, null, u);

      final Enceinte e3 = new Enceinte();
      e3.setNom("T3-4");
      e3.setPosition(4);
      e3.setAlias("T3_4");
      e3.setNbPlaces(5);

      enceinteManager.createObjectManager(e3, type, null, eTest2, ent, banks, null, u);

      final Enceinte e4 = new Enceinte();
      e4.setNom("T3-8");
      e4.setPosition(8);
      e4.setAlias("T3_8");
      e4.setNbPlaces(5);

      enceinteManager.createObjectManager(e4, type, null, eTest2, ent, banks, null, u);

      assertEquals(3, enceinteManager.getEnceintesManager(eTest2).size());

      // pas besoin de repositionner les enceintes
      enceinteManager.updateTailleEnceinteManager(eTest2, -2, u);
      eTest2 = enceinteManager.findByIdManager(id1);
      final Set<Enceinte> enceintes = enceinteManager.getEnceintesManager(eTest2);
      int i = 1;
      for(final Enceinte ec : enceintes){
         if(i == 1){
            assertEquals("T3-1", ec.getNom());
            assertEquals(1, (int) ec.getPosition());
         }else if(i == 2){
            assertEquals("T3-4", ec.getNom());
            assertEquals(4, (int) ec.getPosition());
         }else if(i == 3){
            assertEquals("T3-8", ec.getNom());
            assertEquals(8, (int) ec.getPosition());
         }
         i++;
      }

      // repositionement des enceintes
      enceintes.clear();
      enceinteManager.updateTailleEnceinteManager(eTest2, -4, u);
      eTest2 = enceinteManager.findByIdManager(id1);
      enceintes.addAll(enceinteManager.getEnceintesManager(eTest2));
      i = 1;
      for(final Enceinte ec : enceintes){
         if(i == 1){
            assertEquals("T3-1", ec.getNom());
            assertEquals(1, (int) ec.getPosition());
         }else if(i == 2){
            assertEquals("T3-4", ec.getNom());
            assertEquals(2, (int) ec.getPosition());
         }else if(i == 3){
            assertEquals("T3-8", ec.getNom());
            assertEquals(3, (int) ec.getPosition());
         }
         i++;
      }

      // mauv;ais paramétrage
      Enceinte bad = enceinteManager.findByIdManager(id1);
      enceinteManager.updateTailleEnceinteManager(null, 5, u);
      assertEquals(11, enceinteManager.findAllObjectsManager().size());
      assertEquals(4, getOperationManager().findByObjectManager(bad).size());
      bad = enceinteManager.findByIdManager(id1);
      assertEquals(4, (int) bad.getNbPlaces());

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, null, u);
      assertEquals(11, enceinteManager.findAllObjectsManager().size());
      assertEquals(4, getOperationManager().findByObjectManager(bad).size());
      assertEquals(4, (int) bad.getNbPlaces());

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, 5, null);
      assertEquals(11, enceinteManager.findAllObjectsManager().size());
      assertEquals(4, getOperationManager().findByObjectManager(bad).size());
      assertEquals(4, (int) bad.getNbPlaces());

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, 0, u);
      assertEquals(11, enceinteManager.findAllObjectsManager().size());
      assertEquals(4, getOperationManager().findByObjectManager(bad).size());
      assertEquals(4, (int) bad.getNbPlaces());

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, -1, u);
      assertEquals(11, enceinteManager.findAllObjectsManager().size());
      assertEquals(5, getOperationManager().findByObjectManager(bad).size());
      assertEquals(3, (int) bad.getNbPlaces());

      // < 0
      boolean catched = false;
      try{
         enceinteManager.updateTailleEnceinteManager(bad, -4, u);
      }catch(final EnceinteSizeException e){
         catched = true;
         assertEquals(e.getEnceinte(), bad);
         assertEquals(3, (int) e.getNbPlaces());
      }
      assertTrue(catched);

      // occupation = 3 enceintes / 6 places
      e1 = enceinteManager.findByIdManager(1);
      catched = false;
      try{
         enceinteManager.updateTailleEnceinteManager(e1, -4, u);
      }catch(final EnceinteSizeException e){
         catched = true;
         assertEquals(e.getEnceinte(), e1);
         assertEquals(4, (int) e.getNbPlaces());
      }
      assertTrue(catched);

      // occupation = 1 Terminale / 5 places
      e1 = enceinteManager.findByIdManager(5);
      catched = false;
      try{
         enceinteManager.updateTailleEnceinteManager(e1, -5, u);
      }catch(final EnceinteSizeException e){
         catched = true;
         assertEquals(e.getEnceinte(), e1);
         assertEquals(1, (int) e.getNbPlaces());
      }
      assertTrue(catched);

      // repositionnement des terminales
      Enceinte ect1 = enceintes.iterator().next();
      final Integer idEct1 = ect1.getEnceinteId();
      enceinteManager.updateTailleEnceinteManager(ect1, 5, u);
      ect1 = enceinteManager.findByIdManager(idEct1);
      assertEquals(10, (int) ect1.getNbPlaces());

      // terminales filles
      final Terminale t1 = new Terminale();
      t1.setNom("BT1-1");
      t1.setPosition(3);
      final TerminaleType typeT = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);

      terminaleManager.createObjectManager(t1, ect1, typeT, null, null, num, null, u);

      final Terminale t2 = new Terminale();
      t2.setNom("BT2-6");
      t2.setPosition(6);

      terminaleManager.createObjectManager(t2, ect1, typeT, null, null, num, null, u);

      final Terminale t3 = new Terminale();
      t3.setNom("BT3-9");
      t3.setPosition(9);

      terminaleManager.createObjectManager(t3, ect1, typeT, null, null, num, null, u);

      assertEquals(3, enceinteManager.getTerminalesManager(ect1).size());

      // pas besoin de repositionner les terminales
      enceinteManager.updateTailleEnceinteManager(ect1, -1, u);
      ect1 = enceinteManager.findByIdManager(idEct1);
      final Set<Terminale> terminales = enceinteManager.getTerminalesManager(ect1);
      i = 1;
      for(final Terminale tm : terminales){
         if(i == 1){
            assertEquals("BT1-1", tm.getNom());
            assertEquals(3, (int) tm.getPosition());
         }else if(i == 2){
            assertEquals("BT2-6", tm.getNom());
            assertEquals(6, (int) tm.getPosition());
         }else if(i == 3){
            assertEquals("BT3-9", tm.getNom());
            assertEquals(9, (int) tm.getPosition());
         }
         i++;
      }

      // repositionement des terminales
      terminales.clear();
      enceinteManager.updateTailleEnceinteManager(ect1, -1, u);
      ect1 = enceinteManager.findByIdManager(idEct1);
      terminales.addAll(enceinteManager.getTerminalesManager(ect1));
      i = 1;
      for(final Terminale tm : terminales){
         if(i == 1){
            assertEquals("BT1-1", tm.getNom());
            assertEquals(1, (int) tm.getPosition());
         }else if(i == 2){
            assertEquals("BT2-6", tm.getNom());
            assertEquals(2, (int) tm.getPosition());
         }else if(i == 3){
            assertEquals("BT3-9", tm.getNom());
            assertEquals(3, (int) tm.getPosition());
         }
         i++;
      }

      // Suppression

      enceinteManager.removeObjectManager(eTest2, null, u);

      assertEquals(0, getOperationManager().findByObjectManager(eTest2).size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(eTest2);
      fs.addAll(enceintes);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   // @since 2.2.1
   @Test
   public void testGetDistinctBanquesFromTkObjectsManager(){
      List<Banque> banks = enceinteManager.getDistinctBanquesFromTkObjectsManager(enceinteManager.findByIdManager(1));

      // enceinte premier niveau -> contient la terminale
      assertTrue(banks.size() == 1);
      assertTrue(banks.contains(banqueDao.findById(1)));

      // enceinte deuxième niveau -> contient la terminale
      banks = enceinteManager.getDistinctBanquesFromTkObjectsManager(enceinteManager.findByIdManager(3));
      assertTrue(banks.size() == 1);
      assertTrue(banks.contains(banqueDao.findById(1)));

      banks = enceinteManager.getDistinctBanquesFromTkObjectsManager(enceinteManager.findByIdManager(2));
      assertTrue(banks.isEmpty());

      banks = enceinteManager.getDistinctBanquesFromTkObjectsManager(null);
      assertTrue(banks.isEmpty());
   }
}
