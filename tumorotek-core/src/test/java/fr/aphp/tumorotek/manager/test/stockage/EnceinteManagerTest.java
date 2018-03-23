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
import fr.aphp.tumorotek.manager.stockage.CheckPositionManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager EnceinteManager.
 * Classe créée le 22/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
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
      assertTrue(e.getNom().equals("R1"));

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
      assertTrue(list.size() == 7);
   }

   /**
    * Test la méthode findByConteneurWithOrderManager.
    */
   @Test
   public void testFindByConteneurWithOrderManager(){
      final Conteneur c1 = conteneurDao.findById(1);
      List<Enceinte> list = enceinteManager.findByConteneurWithOrderManager(c1);
      assertEquals(2, list.size());
      assertTrue(list.get(0).getNom().equals("R1"));

      final Conteneur c2 = conteneurDao.findById(2);
      list = enceinteManager.findByConteneurWithOrderManager(c2);
      assertTrue(list.size() == 0);

      list = enceinteManager.findByConteneurWithOrderManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByEnceintePereWithOrderManager.
    */
   @Test
   public void testFindByEnceintePereWithOrderManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<Enceinte> list = enceinteManager.findByEnceintePereWithOrderManager(e1);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("T1"));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.findByEnceintePereWithOrderManager(e2);
      assertTrue(list.size() == 2);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.findByEnceintePereWithOrderManager(e3);
      assertTrue(list.size() == 0);

      list = enceinteManager.findByEnceintePereWithOrderManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByConteneurAndNomManager.
    */
   @Test
   public void testFindByConteneurAndNomManager(){
      final Conteneur c1 = conteneurDao.findById(1);
      List<Enceinte> list = enceinteManager.findByConteneurAndNomManager(c1, "R1");
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("R1"));

      list = enceinteManager.findByConteneurAndNomManager(c1, "Rqsdqsd1");
      assertTrue(list.size() == 0);

      final Conteneur c2 = conteneurDao.findById(2);
      list = enceinteManager.findByConteneurAndNomManager(c2, "R1");
      assertTrue(list.size() == 0);

      list = enceinteManager.findByConteneurAndNomManager(null, "R1");
      assertTrue(list.size() == 0);

      list = enceinteManager.findByConteneurAndNomManager(c1, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByEnceintePereAndNomManager.
    */
   @Test
   public void testFindByEnceintePereAndNomManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<Enceinte> list = enceinteManager.findByEnceintePereAndNomManager(e1, "T1");
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("T1"));

      list = enceinteManager.findByEnceintePereAndNomManager(e1, "jhq");
      assertTrue(list.size() == 0);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.findByEnceintePereAndNomManager(e3, "T1");
      assertTrue(list.size() == 0);

      list = enceinteManager.findByEnceintePereAndNomManager(null, "T1");
      assertTrue(list.size() == 0);

      list = enceinteManager.findByEnceintePereAndNomManager(e1, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode usedNomsExceptOneManager.
    */
   @Test
   public void testUsedNomsExceptOneManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<String> list = enceinteManager.usedNomsExceptOneManager(e1);
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).equals("R2"));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.usedNomsExceptOneManager(e2);
      assertTrue(list.size() == 1);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.usedNomsExceptOneManager(e3);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).equals("T2"));

      list = enceinteManager.usedNomsExceptOneManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode getBanquesManager.
    */
   @Test
   public void testGetBanquesManager(){

      final Enceinte e5 = enceinteManager.findByIdManager(5);
      Set<Banque> set = enceinteManager.getBanquesManager(e5);
      assertTrue(set.size() == 2);

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      set = enceinteManager.getBanquesManager(e7);
      assertTrue(set.size() == 1);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      set = enceinteManager.getBanquesManager(e3);
      assertTrue(set.size() == 0);

      final Enceinte newE = new Enceinte();
      set = enceinteManager.getBanquesManager(newE);
      assertTrue(set.size() == 0);

      set = enceinteManager.getBanquesManager(null);
      assertTrue(set.size() == 0);
   }

   /**
    * Test la méthode getEnceintesManager.
    */
   @Test
   public void testGetEnceintesManager(){

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Set<Enceinte> set = enceinteManager.getEnceintesManager(e1);
      assertTrue(set.size() == 3);
      final Iterator<Enceinte> it = set.iterator();
      assertTrue(it.next().getPosition() == 1);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      set = enceinteManager.getEnceintesManager(e2);
      assertTrue(set.size() == 2);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      set = enceinteManager.getEnceintesManager(e3);
      assertTrue(set.size() == 0);

      final Enceinte newE = new Enceinte();
      set = enceinteManager.getEnceintesManager(newE);
      assertTrue(set.size() == 0);

      set = enceinteManager.getEnceintesManager(null);
      assertTrue(set.size() == 0);
   }

   /**
    * Test la méthode getTerminalesManager.
    */
   @Test
   public void testGetTerminalesManager(){

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      Set<Terminale> set = enceinteManager.getTerminalesManager(e3);
      assertTrue(set.size() == 3);
      final Iterator<Terminale> it = set.iterator();
      assertTrue(it.next().getNom().equals("BT1"));

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      set = enceinteManager.getTerminalesManager(e7);
      assertTrue(set.size() == 1);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      set = enceinteManager.getTerminalesManager(e4);
      assertTrue(set.size() == 0);

      final Enceinte newE = new Enceinte();
      set = enceinteManager.getTerminalesManager(newE);
      assertTrue(set.size() == 0);

      set = enceinteManager.getTerminalesManager(null);
      assertTrue(set.size() == 0);
   }

   /**
    * Test la méthode getTerminalesManager.
    */
   @Test
   public void testGetAllTerminalesInArborescenceManager(){

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      List<Terminale> list = enceinteManager.getAllTerminalesInArborescenceManager(e3);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("BT1"));

      final Enceinte e7 = enceinteManager.findByIdManager(7);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e7);
      assertTrue(list.size() == 1);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e4);
      assertTrue(list.size() == 0);

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e1);
      assertTrue(list.size() == 4);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.getAllTerminalesInArborescenceManager(e2);
      assertTrue(list.size() == 2);

      final Enceinte newE = new Enceinte();
      list = enceinteManager.getAllTerminalesInArborescenceManager(newE);
      assertTrue(list.size() == 0);

      list = enceinteManager.getAllTerminalesInArborescenceManager(null);
      assertTrue(list.size() == 0);
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
      assertTrue(enceinteManager.getLevelEnceinte(e1) == 1);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      assertTrue(enceinteManager.getLevelEnceinte(e2) == 1);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      assertTrue(enceinteManager.getLevelEnceinte(e3) == 2);

      final Enceinte e6 = enceinteManager.findByIdManager(6);
      assertTrue(enceinteManager.getLevelEnceinte(e6) == 2);

      final Enceinte eNew = new Enceinte();
      assertTrue(enceinteManager.getLevelEnceinte(eNew) == 1);

      assertTrue(enceinteManager.getLevelEnceinte(null) == 0);
   }

   /**
    * Test la méthode getNumberEmplacementsLibres.
    */
   @Test
   public void testGetNumberEmplacementsLibres(){

      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Long nb = enceinteManager.getNumberEmplacementsLibres(e1);
      assertTrue(nb == 397);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      nb = enceinteManager.getNumberEmplacementsLibres(e2);
      assertTrue(nb == 148);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      nb = enceinteManager.getNumberEmplacementsLibres(e3);
      assertTrue(nb == 297);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      nb = enceinteManager.getNumberEmplacementsLibres(e4);
      assertTrue(nb == 0);

      final Enceinte newE = new Enceinte();
      nb = enceinteManager.getNumberEmplacementsLibres(newE);
      assertTrue(nb == 0);

      nb = enceinteManager.getNumberEmplacementsLibres(null);
      assertTrue(nb == 0);
   }

   @Test
   public void testGetNumberEmplacements(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Long nb = enceinteManager.getNbEmplacementsLibresByPS(e1);
      assertTrue(nb == 397);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      nb = enceinteManager.getNbEmplacementsLibresByPS(e2);
      assertTrue(nb == 148);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      nb = enceinteManager.getNbEmplacementsLibresByPS(e3);
      assertTrue(nb == 297);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      nb = enceinteManager.getNbEmplacementsLibresByPS(e4);
      assertTrue(nb == 0);

      final Enceinte newE = new Enceinte();
      nb = enceinteManager.getNbEmplacementsLibresByPS(newE);
      assertTrue(nb == 0);

      nb = enceinteManager.getNbEmplacementsLibresByPS(null);
      assertTrue(nb == 0);
   }

   @Test
   public void testGetNumberEmplacementsOccupes(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Long nb = enceinteManager.getNbEmplacementsOccupesByPS(e1);
      assertTrue(nb == 3);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      nb = enceinteManager.getNbEmplacementsOccupesByPS(e2);
      assertTrue(nb == 0);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      nb = enceinteManager.getNbEmplacementsOccupesByPS(e3);
      assertTrue(nb == 3);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      nb = enceinteManager.getNbEmplacementsOccupesByPS(e4);
      assertTrue(nb == 0);

      final Enceinte newE = new Enceinte();
      nb = enceinteManager.getNbEmplacementsOccupesByPS(newE);
      assertTrue(nb == 0);

      nb = enceinteManager.getNbEmplacementsOccupesByPS(null);
      assertTrue(nb == 0);
   }

   @Test
   public void testGetObjetIdsByEntiteByPS(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      List<Integer> list = new ArrayList<>();
      list = enceinteManager.getObjetIdsByEntiteByPS(e1, entiteDao.findById(3));
      assertTrue(list.size() == 1);

      list = enceinteManager.getObjetIdsByEntiteByPS(e1, entiteDao.findById(8));
      assertTrue(list.size() == 2);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      list = enceinteManager.getObjetIdsByEntiteByPS(e2, entiteDao.findById(3));
      assertTrue(list.size() == 0);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      list = enceinteManager.getObjetIdsByEntiteByPS(e3, entiteDao.findById(3));
      assertTrue(list.size() == 1);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      list = enceinteManager.getObjetIdsByEntiteByPS(e4, entiteDao.findById(3));
      assertTrue(list.size() == 0);

      final Enceinte newE = new Enceinte();
      list = enceinteManager.getObjetIdsByEntiteByPS(newE, entiteDao.findById(3));
      assertTrue(list.size() == 0);

      list = enceinteManager.getObjetIdsByEntiteByPS(null, entiteDao.findById(3));
      assertTrue(list.size() == 0);

      list = enceinteManager.getObjetIdsByEntiteByPS(e3, new Entite());
      assertTrue(list.size() == 0);

      list = enceinteManager.getObjetIdsByEntiteByPS(e3, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Teste la méthode getAdrlManager.
    */
   @Test
   public void testGetAdrlManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      String path = enceinteManager.getAdrlManager(e1);
      assertTrue(path.equals("CC1"));

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      path = enceinteManager.getAdrlManager(e2);
      assertTrue(path.equals("CC1"));

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      path = enceinteManager.getAdrlManager(e3);
      assertTrue(path.equals("CC1.R1"));

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      path = enceinteManager.getAdrlManager(e4);
      assertTrue(path.equals("CC1.R1"));

      final Enceinte newE = new Enceinte();
      path = enceinteManager.getAdrlManager(newE);
      assertTrue(path.equals(""));

      path = enceinteManager.getAdrlManager(null);
      assertTrue(path.equals(""));
   }

   /**
    * Teste la méthode getConteneurManager.
    */
   @Test
   public void testGetConteneurManager(){
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      Conteneur cont = enceinteManager.getConteneurManager(e1);
      assertTrue(cont.getConteneurId() == 1);

      final Enceinte e2 = enceinteManager.findByIdManager(2);
      cont = enceinteManager.getConteneurManager(e2);
      assertTrue(cont.getConteneurId() == 1);

      final Enceinte e3 = enceinteManager.findByIdManager(3);
      cont = enceinteManager.getConteneurManager(e3);
      assertTrue(cont.getConteneurId() == 1);

      final Enceinte e4 = enceinteManager.findByIdManager(4);
      cont = enceinteManager.getConteneurManager(e4);
      assertTrue(cont.getConteneurId() == 1);

      final Enceinte newE = new Enceinte();
      cont = enceinteManager.getConteneurManager(newE);
      assertTrue(cont == null);

      cont = enceinteManager.getConteneurManager(null);
      assertTrue(cont == null);
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // Test de la validation lors de la création
      e1.setNom("T3");
      e1.setPosition(3);
      try{
         validationInsert(e1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // On test une insertion valide dans un conteneur avec les 
      // assos non obligatoires à null
      e1.setNom("R3");
      e1.setPosition(3);
      e1.setAlias("ALIAS");
      e1.setNbPlaces(6);
      enceinteManager.createObjectManager(e1, type, cont, null, null, null, null, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(getOperationManager().findByObjectManager(e1).size() == 1);
      final int id = e1.getEnceinteId();
      // Vérification
      final Enceinte eTest = enceinteManager.findByIdManager(id);
      assertNotNull(eTest);
      assertNotNull(eTest.getConteneur());
      assertNotNull(eTest.getEnceinteType());
      assertNull(eTest.getEnceintePere());
      assertTrue(eTest.getPosition() == 3);
      assertTrue(eTest.getNom().equals("R3"));
      assertTrue(eTest.getAlias().equals("ALIAS"));
      assertTrue(eTest.getNbPlaces() == 6);
      assertTrue(enceinteManager.getBanquesManager(eTest).size() == 0);

      // On test une insertion valide dans une enceinte avec 
      // toutes les assos
      final Enceinte e2 = new Enceinte();
      e2.setNom("T3");
      e2.setPosition(3);
      e2.setAlias("ALIAS3");
      e2.setNbPlaces(5);

      enceinteManager.createObjectManager(e2, type, null, enc, ent, banks, couleurDao.findById(7), u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 9);
      assertTrue(getOperationManager().findByObjectManager(e2).size() == 1);
      final int id2 = e2.getEnceinteId();
      // Vérification
      final Enceinte eTest2 = enceinteManager.findByIdManager(id2);
      assertNotNull(eTest2);
      assertNotNull(eTest2.getEnceintePere());
      assertNotNull(eTest2.getEnceinteType());
      assertNotNull(eTest2.getEntite());
      assertNotNull(eTest2.getCouleur());
      assertNull(eTest2.getConteneur());
      assertTrue(eTest2.getPosition() == 3);
      assertTrue(eTest2.getNom().equals("T3"));
      assertTrue(eTest2.getAlias().equals("ALIAS3"));
      assertTrue(eTest2.getNbPlaces() == 5);
      assertTrue(enceinteManager.getBanquesManager(eTest2).size() == 2);

      // Suppression
      enceinteManager.removeObjectManager(eTest, null, u);
      enceinteManager.removeObjectManager(eTest2, null, u);
      assertTrue(getOperationManager().findByObjectManager(eTest).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(eTest2).size() == 0);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(getOperationManager().findByObjectManager(en).size() == 1);
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

      // Test de la validation lors de l'update
      eUp1.setNom("T3");
      eUp1.setPosition(3);
      try{
         validationUpdate(eUp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

      incs.remove(1);

      // On test un update valide dans un conteneur avec les 
      // assos non obligatoires à null
      enceinteManager.updateObjectManager(eUp1, type, cont, null, null, banks, null, incs, u, null);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(getOperationManager().findByObjectManager(eUp1).size() == 2);
      // Vérification
      final Enceinte eTest = enceinteManager.findByIdManager(id);
      assertNotNull(eTest);
      assertNotNull(eTest.getConteneur());
      assertNotNull(eTest.getEnceinteType());
      assertNull(eTest.getEnceintePere());
      assertTrue(eTest.getPosition() == 3);
      assertTrue(eTest.getNom().equals("R3"));
      assertTrue(eTest.getAlias().equals("ALIAS3"));
      assertTrue(eTest.getNbPlaces() == 6);
      assertTrue(enceinteManager.getBanquesManager(eTest).size() == 2);

      inc1 = incidentManager.findByEnceinteManager(eTest).get(0);
      assertTrue(inc1.getNom().equals("TEST"));
      assertTrue(inc1.getDescription() == null);

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
      assertTrue(getOperationManager().findByObjectManager(eUp2).size() == 5);
      // Vérification
      final Enceinte eTest2 = enceinteManager.findByIdManager(id);
      assertNotNull(eTest2);
      assertNotNull(eTest2.getEnceintePere());
      assertNotNull(eTest2.getEnceinteType());
      assertNotNull(eTest2.getEntite());
      assertNotNull(eTest2.getCouleur());
      assertNull(eTest2.getConteneur());
      assertTrue(eTest2.getPosition() == 3);
      assertTrue(eTest2.getNom().equals("T3"));
      assertTrue(eTest2.getAlias().equals("ALIAS3"));
      assertTrue(eTest2.getNbPlaces() == 5);
      assertTrue(enceinteManager.getBanquesManager(eTest2).size() == 1);
      final Iterator<Banque> it = enceinteManager.getBanquesManager(eTest2).iterator();
      assertTrue(it.next().getNom().equals("BANQUE3"));

      inc1 = incidentManager.findByEnceinteManager(eTest2).get(0);
      assertTrue(inc1.getNom().equals("TEST"));
      assertTrue(inc1.getDescription().equals("incident pour enceinte update"));

      // Suppression
      enceinteManager.removeObjectManager(eTest2, null, u);
      assertTrue(getOperationManager().findByObjectManager(eTest2).size() == 0);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // test de la suppression d'un objet utilisé
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      boolean catched = false;
      try{
         enceinteManager.removeObjectManager(e1, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            assertTrue(((ObjectUsedException) e).getKey().equals("enceinte.deletion.isUsed"));
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      final Integer id = newE.getEnceinteId();

      // création de 5 enceintes pour celle-ci
      final Enceinte supE = enceinteManager.findByIdManager(id);
      final Enceinte newE2 = new Enceinte();
      newE2.setNom("T");
      newE2.setNbPlaces(5);
      newE2.setEnceinteType(type);
      final List<Enceinte> encs = enceinteManager.createMultiObjetcsForEnceinteManager(supE, newE2, 5, null, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 13);

      // création de 5 terminales pour la 1ère enceinte
      final TerminaleType tType = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Terminale newTs = new Terminale();
      newTs.setTerminaleNumerotation(num);
      newTs.setTerminaleType(tType);
      newTs.setNom("BT");
      newTs.setAlias("ALIAS");
      terminaleManager.createMultiObjetcsManager(encs.get(0), newTs, 5, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 11);

      final List<Enceinte> enceintes = enceinteManager.findByEnceintePereWithOrderManager(supE);

      final List<Terminale> terms = new ArrayList<>();
      for(int i = 0; i < enceintes.size(); i++){
         terms.addAll(terminaleManager.findByEnceinteWithOrderManager(enceintes.get(i)));
      }

      enceinteManager.removeObjectManager(supE, null, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on test la création avec un nombre null
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, null, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on test la création avec un nombre négatif
      try{
         enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, -10, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on teste une création multiple valide avec la premiere position
      // à 125
      final List<Enceinte> list = enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, 2, 125, u);
      assertTrue(list.size() == 2);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 9);
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getConteneur());
      assertNull(list.get(0).getEnceintePere());
      assertNotNull(list.get(0).getEnceinteType());
      assertNotNull(list.get(0).getEntite());
      assertTrue(list.get(0).getPosition() == 1);
      assertTrue(list.get(0).getAlias().equals("ALIAS1"));
      assertTrue(list.get(0).getNbPlaces() == 5);
      assertFalse(list.get(0).getArchive());
      assertTrue(list.get(0).getNom().equals("R125"));
      assertTrue(list.get(1).getNom().equals("R126"));

      for(int i = 0; i < list.size(); i++){
         enceinteManager.removeObjectManager(list.get(i), null, u);
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on teste une création multiple valide avec la premiere position
      // à null
      final List<Enceinte> list2 = enceinteManager.createMultiObjetcsForConteneurManager(cont2, newEs, 2, null, u);
      assertTrue(list2.size() == 2);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 9);
      assertNotNull(list2.get(0));
      assertNotNull(list2.get(0).getConteneur());
      assertNull(list2.get(0).getEnceintePere());
      assertNotNull(list2.get(0).getEnceinteType());
      assertNotNull(list2.get(0).getEntite());
      assertTrue(list2.get(0).getPosition() == 1);
      assertTrue(list2.get(0).getAlias().equals("ALIAS1"));
      assertTrue(list2.get(0).getNbPlaces() == 5);
      assertFalse(list2.get(0).getArchive());
      assertTrue(list2.get(0).getNom().equals("R1"));
      assertTrue(list2.get(1).getNom().equals("R2"));

      for(int i = 0; i < list2.size(); i++){
         enceinteManager.removeObjectManager(list2.get(i), null, u);
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on test la création avec un nombre null
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, null, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on test la création avec un nombre négatif
      try{
         enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, -10, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on teste une création multiple valide avec la 1re pos à 58
      final List<Enceinte> list = enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, 5, 58, u);
      assertTrue(list.size() == 5);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 12);
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getEnceintePere());
      assertNull(list.get(0).getConteneur());
      assertNotNull(list.get(0).getEnceinteType());
      assertNotNull(list.get(0).getEntite());
      assertTrue(list.get(0).getPosition() == 1);
      assertTrue(list.get(0).getAlias().equals("ALIAS1"));
      assertTrue(list.get(0).getNbPlaces() == 5);
      assertFalse(list.get(0).getArchive());
      assertTrue(list.get(0).getNom().equals("T58"));
      assertTrue(list.get(1).getNom().equals("T59"));
      assertTrue(list.get(4).getNom().equals("T62"));

      for(int i = 0; i < list.size(); i++){
         enceinteManager.removeObjectManager(list.get(i), null, u);
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      // on teste une création multiple valide avec la 1re pos à null
      final List<Enceinte> list2 = enceinteManager.createMultiObjetcsForEnceinteManager(e4, newEs, 5, null, u);
      assertTrue(list2.size() == 5);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 12);
      assertNotNull(list2.get(0));
      assertNotNull(list2.get(0).getEnceintePere());
      assertNull(list2.get(0).getConteneur());
      assertNotNull(list2.get(0).getEnceinteType());
      assertNotNull(list2.get(0).getEntite());
      assertTrue(list2.get(0).getPosition() == 1);
      assertTrue(list2.get(0).getAlias().equals("ALIAS1"));
      assertTrue(list2.get(0).getNbPlaces() == 5);
      assertFalse(list2.get(0).getArchive());
      assertTrue(list2.get(0).getNom().equals("T1"));
      assertTrue(list2.get(1).getNom().equals("T2"));
      assertTrue(list2.get(4).getNom().equals("T5"));

      for(int i = 0; i < list2.size(); i++){
         enceinteManager.removeObjectManager(list2.get(i), null, u);
      }
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      Enceinte eTest3 = enceinteManager.findByIdManager(3);
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep3));
      assertTrue(eTest3.getPosition().equals(pos3));

      enceinteManager.echangerDeuxEnceintesManager(e3, new Enceinte(), u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      eTest3 = enceinteManager.findByIdManager(3);
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep3));
      assertTrue(eTest3.getPosition().equals(pos3));

      enceinteManager.echangerDeuxEnceintesManager(null, e3, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      eTest3 = enceinteManager.findByIdManager(3);
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep3));
      assertTrue(eTest3.getPosition().equals(pos3));

      enceinteManager.echangerDeuxEnceintesManager(new Enceinte(), e3, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      eTest3 = enceinteManager.findByIdManager(3);
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep3));
      assertTrue(eTest3.getPosition().equals(pos3));

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
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep6));
      assertTrue(eTest3.getPosition().equals(pos6));
      Enceinte eTest6 = enceinteManager.findByIdManager(6);
      assertTrue(eTest6.getConteneur() == null);
      assertTrue(eTest6.getEnceintePere().equals(ep3));
      assertTrue(eTest6.getPosition().equals(pos3));
      assertTrue(getOperationManager().findByObjectManager(eTest3).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(eTest6).size() == 1);

      // on refait l'échange dans l'autre sens
      e3.setConteneur(c3);
      e3.setEnceintePere(ep3);
      e3.setPosition(pos3);
      e6.setConteneur(c6);
      e6.setEnceintePere(ep6);
      e6.setPosition(pos6);
      enceinteManager.echangerDeuxEnceintesManager(e3, e6, u);
      eTest3 = enceinteManager.findByIdManager(3);
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep3));
      assertTrue(eTest3.getPosition().equals(pos3));
      eTest6 = enceinteManager.findByIdManager(6);
      assertTrue(eTest6.getConteneur() == null);
      assertTrue(eTest6.getEnceintePere().equals(ep6));
      assertTrue(eTest6.getPosition().equals(pos6));
      assertTrue(getOperationManager().findByObjectManager(eTest3).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(eTest6).size() == 2);

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
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep4));
      assertTrue(eTest3.getPosition().equals(pos4));
      Enceinte eTest4 = enceinteManager.findByIdManager(4);
      assertTrue(eTest4.getConteneur() == null);
      assertTrue(eTest4.getEnceintePere().equals(ep3));
      assertTrue(eTest4.getPosition().equals(pos3));

      // on refait l'échange dans l'autre sens
      e3.setConteneur(c3);
      e3.setEnceintePere(ep3);
      e3.setPosition(pos3);
      e4.setConteneur(c4);
      e4.setEnceintePere(ep4);
      e4.setPosition(pos4);
      enceinteManager.echangerDeuxEnceintesManager(e3, e4, u);
      eTest3 = enceinteManager.findByIdManager(3);
      assertTrue(eTest3.getConteneur() == null);
      assertTrue(eTest3.getEnceintePere().equals(ep3));
      assertTrue(eTest3.getPosition().equals(pos3));
      eTest4 = enceinteManager.findByIdManager(4);
      assertTrue(eTest4.getConteneur() == null);
      assertTrue(eTest4.getEnceintePere().equals(ep4));
      assertTrue(eTest4.getPosition().equals(pos4));

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
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      term.setTerminaleType(tType);

      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 11);

      final Set<Enceinte> eTests = enceinteManager.getEnceintesManager(eParent);
      assertTrue(eTests.size() == 1);

      final Terminale tTest = enceinteManager.getTerminalesManager(e1).iterator().next();
      assertTrue(tTest.getNom().equals("BT10"));

      final List<Terminale> terms = terminaleManager.findByEnceinteWithOrderManager(e1);

      enceinteManager.removeObjectManager(e1, null, u);
      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
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
      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      term.setTerminaleType(tType);

      // creation valide
      try{
         enceinteManager.createAllArborescenceManager(e1, enceintes, term, goodPositions, banques, u);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertTrue(enceinteManager.findAllObjectsManager().size() == 16);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 36);

      final Enceinte eTest1 = conteneurManager.getEnceintesManager(c).iterator().next();
      assertTrue(eTest1.getNom().equals("R"));
      assertTrue(enceinteManager.getEnceintesManager(eTest1).size() == 2);

      final Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1).iterator().next();
      assertTrue(eTest2.getNom().equals("T10"));
      assertTrue(enceinteManager.getEnceintesManager(eTest2).size() == 3);

      final Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2).iterator().next();
      assertTrue(eTest3.getNom().equals("C1"));
      assertTrue(enceinteManager.getEnceintesManager(eTest3).size() == 0);
      assertTrue(enceinteManager.getTerminalesManager(eTest3).size() == 5);

      final Terminale tTest = enceinteManager.getTerminalesManager(eTest3).iterator().next();
      assertTrue(tTest.getNom().equals("BT25"));

      final List<Enceinte> dep = new ArrayList<>();
      dep.add(e1);
      final List<Enceinte> encs = new ArrayList<>();
      enceinteManager.findEnceinteRecursiveManager(dep, encs);

      final List<Terminale> terms = new ArrayList<>();
      for(int i = 0; i < encs.size(); i++){
         terms.addAll(terminaleManager.findByEnceinteWithOrderManager(encs.get(i)));
      }

      enceinteManager.removeObjectManager(e1, null, u);
      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
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
      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      term.setTerminaleType(tType);

      // creation valide
      try{
         enceinteManager.updatewithCreateAllArborescenceManager(eUp, enceintes, term, goodPositions, banques, u);
      }catch(final Exception exp){
         exp.printStackTrace();
      }

      assertTrue(enceinteManager.findAllObjectsManager().size() == 16);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 36);

      final Enceinte eTest1 = conteneurManager.getEnceintesManager(c).iterator().next();
      assertTrue(eTest1.getNom().equals("RUP1"));
      assertNotNull(eTest1.getCouleur());
      assertTrue(enceinteManager.getEnceintesManager(eTest1).size() == 2);

      final Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1).iterator().next();
      assertTrue(eTest2.getNom().equals("T10"));
      assertTrue(enceinteManager.getEnceintesManager(eTest2).size() == 3);

      final Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2).iterator().next();
      assertTrue(eTest3.getNom().equals("C1"));
      assertTrue(enceinteManager.getEnceintesManager(eTest3).size() == 0);
      assertTrue(enceinteManager.getTerminalesManager(eTest3).size() == 5);

      final Terminale tTest = enceinteManager.getTerminalesManager(eTest3).iterator().next();
      assertTrue(tTest.getNom().equals("BT25"));

      final List<Enceinte> dep = new ArrayList<>();
      dep.add(eUp);
      final List<Enceinte> encs = new ArrayList<>();
      enceinteManager.findEnceinteRecursiveManager(dep, encs);

      final List<Terminale> terms = new ArrayList<>();
      for(int i = 0; i < encs.size(); i++){
         terms.addAll(terminaleManager.findByEnceinteWithOrderManager(encs.get(i)));
      }

      enceinteManager.removeObjectManager(eUp, null, u);
      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(eUp);
      fs.addAll(encs);
      fs.addAll(terms);
      cleanUpFantomes(fs);
   }

   @Test
   public void getConteneurParentManager(){
      final Enceinte e5 = enceinteManager.findByIdManager(6);
      assertTrue(enceinteManager.getConteneurParent(e5).equals(conteneurDao.findById(1)));
      final Enceinte e1 = enceinteManager.findByIdManager(1);
      assertTrue(enceinteManager.getConteneurParent(e1).equals(conteneurDao.findById(1)));
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
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(getOperationManager().findByObjectManager(e1).size() == 1);
      final int id1 = e1.getEnceinteId();
      // Vérification
      final Enceinte eTest1 = enceinteManager.findByIdManager(id1);
      assertNotNull(eTest1);
      assertNotNull(eTest1.getEnceintePere());
      assertNotNull(eTest1.getEnceinteType());
      assertNotNull(eTest1.getEntite());
      assertNull(eTest1.getConteneur());
      assertTrue(eTest1.getPosition() == 3);
      assertTrue(eTest1.getNom().equals("T3"));
      assertTrue(eTest1.getAlias().equals("ALIAS3"));
      assertTrue(eTest1.getNbPlaces() == 5);
      assertTrue(enceinteManager.getBanquesManager(eTest1).size() == 2);

      // update de la taille
      final Enceinte up = enceinteManager.findByIdManager(id1);
      Enceinte eTest2 = enceinteManager.updateTailleEnceinteManager(up, 5, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 8);
      assertTrue(getOperationManager().findByObjectManager(eTest2).size() == 2);
      assertTrue(eTest2.getNbPlaces() == 10);
      assertTrue(eTest2.getEnceintePere().equals(eTest1.getEnceintePere()));
      assertTrue(eTest2.getEnceinteType().equals(eTest1.getEnceinteType()));
      assertTrue(eTest2.getEntite().equals(eTest1.getEntite()));
      assertNull(eTest2.getConteneur());
      assertTrue(eTest2.getPosition().equals(eTest1.getPosition()));
      assertTrue(eTest2.getNom().equals(eTest1.getNom()));
      assertTrue(eTest2.getAlias().equals(eTest1.getAlias()));
      assertTrue(enceinteManager.getBanquesManager(eTest2).size() == 2);

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

      assertTrue(enceinteManager.getEnceintesManager(eTest2).size() == 3);

      // pas besoin de repositionner les enceintes
      enceinteManager.updateTailleEnceinteManager(eTest2, -2, u);
      eTest2 = enceinteManager.findByIdManager(id1);
      final Set<Enceinte> enceintes = enceinteManager.getEnceintesManager(eTest2);
      int i = 1;
      for(final Enceinte ec : enceintes){
         if(i == 1){
            assertTrue(ec.getNom().equals("T3-1"));
            assertTrue(ec.getPosition() == 1);
         }else if(i == 2){
            assertTrue(ec.getNom().equals("T3-4"));
            assertTrue(ec.getPosition() == 4);
         }else if(i == 3){
            assertTrue(ec.getNom().equals("T3-8"));
            assertTrue(ec.getPosition() == 8);
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
            assertTrue(ec.getNom().equals("T3-1"));
            assertTrue(ec.getPosition() == 1);
         }else if(i == 2){
            assertTrue(ec.getNom().equals("T3-4"));
            assertTrue(ec.getPosition() == 2);
         }else if(i == 3){
            assertTrue(ec.getNom().equals("T3-8"));
            assertTrue(ec.getPosition() == 3);
         }
         i++;
      }

      // mauv;ais paramétrage
      Enceinte bad = enceinteManager.findByIdManager(id1);
      enceinteManager.updateTailleEnceinteManager(null, 5, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 11);
      assertTrue(getOperationManager().findByObjectManager(bad).size() == 4);
      bad = enceinteManager.findByIdManager(id1);
      assertTrue(bad.getNbPlaces() == 4);

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, null, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 11);
      assertTrue(getOperationManager().findByObjectManager(bad).size() == 4);
      assertTrue(bad.getNbPlaces() == 4);

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, 5, null);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 11);
      assertTrue(getOperationManager().findByObjectManager(bad).size() == 4);
      assertTrue(bad.getNbPlaces() == 4);

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, 0, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 11);
      assertTrue(getOperationManager().findByObjectManager(bad).size() == 4);
      assertTrue(bad.getNbPlaces() == 4);

      bad = enceinteManager.findByIdManager(id1);
      bad = enceinteManager.updateTailleEnceinteManager(bad, -1, u);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 11);
      assertTrue(getOperationManager().findByObjectManager(bad).size() == 5);
      assertTrue(bad.getNbPlaces() == 3);

      // < 0
      boolean catched = false;
      try{
         enceinteManager.updateTailleEnceinteManager(bad, -4, u);
      }catch(final EnceinteSizeException e){
         catched = true;
         assertTrue(e.getEnceinte().equals(bad));
         assertTrue(e.getNbPlaces().equals(3));
      }
      assertTrue(catched);

      // occupation = 3 enceintes / 6 places
      e1 = enceinteManager.findByIdManager(1);
      catched = false;
      try{
         enceinteManager.updateTailleEnceinteManager(e1, -4, u);
      }catch(final EnceinteSizeException e){
         catched = true;
         assertTrue(e.getEnceinte().equals(e1));
         assertTrue(e.getNbPlaces().equals(4));
      }
      assertTrue(catched);

      // occupation = 1 Terminale / 5 places
      e1 = enceinteManager.findByIdManager(5);
      catched = false;
      try{
         enceinteManager.updateTailleEnceinteManager(e1, -5, u);
      }catch(final EnceinteSizeException e){
         catched = true;
         assertTrue(e.getEnceinte().equals(e1));
         assertTrue(e.getNbPlaces().equals(1));
      }
      assertTrue(catched);

      // repositionnement des terminales
      Enceinte ect1 = enceintes.iterator().next();
      final Integer idEct1 = ect1.getEnceinteId();
      enceinteManager.updateTailleEnceinteManager(ect1, 5, u);
      ect1 = enceinteManager.findByIdManager(idEct1);
      assertTrue(ect1.getNbPlaces() == 10);

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

      assertTrue(enceinteManager.getTerminalesManager(ect1).size() == 3);

      // pas besoin de repositionner les terminales
      enceinteManager.updateTailleEnceinteManager(ect1, -1, u);
      ect1 = enceinteManager.findByIdManager(idEct1);
      final Set<Terminale> terminales = enceinteManager.getTerminalesManager(ect1);
      i = 1;
      for(final Terminale tm : terminales){
         if(i == 1){
            assertTrue(tm.getNom().equals("BT1-1"));
            assertTrue(tm.getPosition() == 3);
         }else if(i == 2){
            assertTrue(tm.getNom().equals("BT2-6"));
            assertTrue(tm.getPosition() == 6);
         }else if(i == 3){
            assertTrue(tm.getNom().equals("BT3-9"));
            assertTrue(tm.getPosition() == 9);
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
            assertTrue(tm.getNom().equals("BT1-1"));
            assertTrue(tm.getPosition() == 1);
         }else if(i == 2){
            assertTrue(tm.getNom().equals("BT2-6"));
            assertTrue(tm.getPosition() == 2);
         }else if(i == 3){
            assertTrue(tm.getNom().equals("BT3-9"));
            assertTrue(tm.getPosition() == 3);
         }
         i++;
      }

      // Suppression

      enceinteManager.removeObjectManager(eTest2, null, u);

      assertTrue(getOperationManager().findByObjectManager(eTest2).size() == 0);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(eTest2);
      fs.addAll(enceintes);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }
}
