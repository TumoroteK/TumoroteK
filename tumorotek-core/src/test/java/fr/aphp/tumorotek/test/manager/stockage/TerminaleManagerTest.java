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
package fr.aphp.tumorotek.test.manager.stockage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.stockage.IncidentManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager TerminaleManager.
 * Classe créée le 19/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.2.1
 *
 */
public class TerminaleManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TerminaleManager terminaleManager;
   @Autowired
   private EnceinteDao enceinteDao;
   @Autowired
   private TerminaleTypeDao terminaleTypeDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private TerminaleNumerotationDao terminaleNumerotationDao;
   @Autowired
   private ConteneurDao conteneurDao;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private OperationTypeManager operationTypeManager;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private EnceinteManager enceinteManager;
   @Autowired
   private ConteneurTypeDao conteneurTypeDao;
   @Autowired
   private EnceinteTypeDao enceinteTypeDao;
   @Autowired
   private ServiceDao serviceDao;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private CouleurDao couleurDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private ProdDeriveDao prodDeriveDao;
   @Autowired
   private IncidentManager incidentManager;

   public TerminaleManagerTest(){}

   @Test
   public void testFindById(){
      Terminale t = terminaleManager.findByIdManager(1);
      assertNotNull(t);
      assertTrue(t.getNom().equals("BT1"));

      t = terminaleManager.findByIdManager(5);
      assertNotNull(t);

      final Terminale tNull = terminaleManager.findByIdManager(10);
      assertNull(tNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Terminale> list = terminaleManager.findAllObjectsManager();
      assertTrue(list.size() == 6);
   }

   /**
    * Test la méthode findByEnceinteWithOrder.
    */
   @Test
   public void testFindByEnceinteWithOrder(){
      final Enceinte e3 = enceinteDao.findById(3);

      List<Terminale> list = terminaleManager.findByEnceinteWithOrderManager(e3);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("BT1"));

      final Enceinte e5 = enceinteDao.findById(5);
      list = terminaleManager.findByEnceinteWithOrderManager(e5);
      assertTrue(list.size() == 1);

      final Enceinte e1 = enceinteDao.findById(1);
      list = terminaleManager.findByEnceinteWithOrderManager(e1);
      assertTrue(list.size() == 0);

      list = terminaleManager.findByEnceinteWithOrderManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByEnceinteAndNomManager.
    */
   @Test
   public void testFindByEnceinteAndNomManager(){
      final Enceinte e3 = enceinteDao.findById(3);

      List<Terminale> list = terminaleManager.findByEnceinteAndNomManager(e3, "BT1");
      assertTrue(list.size() == 1);
      assertTrue(list.get(0).getNom().equals("BT1"));

      list = terminaleManager.findByEnceinteAndNomManager(e3, "jivoqsji");
      assertTrue(list.size() == 0);

      final Enceinte e1 = enceinteDao.findById(1);
      list = terminaleManager.findByEnceinteAndNomManager(e1, "BT1");
      assertTrue(list.size() == 0);

      list = terminaleManager.findByEnceinteAndNomManager(null, "BT1");
      assertTrue(list.size() == 0);

      list = terminaleManager.findByEnceinteAndNomManager(e3, null);
      assertTrue(list.size() == 0);
   }
   
   @Test
   public void testFindByAliasManager(){

      List<Terminale> list = terminaleManager.findByAliasManager("ALIAS BT1");
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getTerminaleId().equals(1));
      assertTrue(list.get(1).getTerminaleId().equals(4));
      assertTrue(list.get(2).getTerminaleId().equals(6));

      list = terminaleManager.findByAliasManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode checkTerminaleInEnceinteLimites.
    */
   @Test
   public void testCheckTerminaleInEnceinteLimites(){

      final Terminale t1 = terminaleManager.findByIdManager(1);
      assertTrue(terminaleManager.checkTerminaleInEnceinteLimitesManager(t1));

      t1.setPosition(1000);
      assertFalse(terminaleManager.checkTerminaleInEnceinteLimitesManager(t1));

      final Terminale newT = new Terminale();
      assertFalse(terminaleManager.checkTerminaleInEnceinteLimitesManager(newT));

      newT.setPosition(5);
      assertFalse(terminaleManager.checkTerminaleInEnceinteLimitesManager(newT));

      final Enceinte e1 = enceinteDao.findById(1);
      newT.setEnceinte(e1);
      newT.setPosition(null);
      assertFalse(terminaleManager.checkTerminaleInEnceinteLimitesManager(newT));

      newT.setPosition(5);
      assertTrue(terminaleManager.checkTerminaleInEnceinteLimitesManager(newT));

      newT.setPosition(500);
      assertFalse(terminaleManager.checkTerminaleInEnceinteLimitesManager(newT));

      assertFalse(terminaleManager.checkTerminaleInEnceinteLimitesManager(null));

   }

   /**
    * Test la méthode getEmplacementsManager.
    */
   @Test
   public void testGetEmplacementsManager(){

      final Terminale t1 = terminaleManager.findByIdManager(1);
      Set<Emplacement> set = terminaleManager.getEmplacementsManager(t1);
      assertTrue(set.size() == 5);
      final Iterator<Emplacement> it = set.iterator();
      assertTrue(it.next().getPosition() == 1);

      final Terminale t6 = terminaleManager.findByIdManager(6);
      set = terminaleManager.getEmplacementsManager(t6);
      assertTrue(set.size() == 2);

      final Terminale t3 = terminaleManager.findByIdManager(3);
      set = terminaleManager.getEmplacementsManager(t3);
      assertTrue(set.size() == 0);

      final Terminale newT = new Terminale();
      set = terminaleManager.getEmplacementsManager(newT);
      assertTrue(set.size() == 0);

      set = terminaleManager.getEmplacementsManager(null);
      assertTrue(set.size() == 0);
   }

   /**
    * Test la méthode getNumberEmplacementsLibres.
    */
   @Test
   public void testGetNumberEmplacementsLibres(){

      final Terminale t1 = terminaleManager.findByIdManager(1);
      Long nb = terminaleManager.getNumberEmplacementsLibresManager(t1);
      assertTrue(nb == 97);

      final Terminale t6 = terminaleManager.findByIdManager(6);
      nb = terminaleManager.getNumberEmplacementsLibresManager(t6);
      assertTrue(nb == 81);

      final Terminale t3 = terminaleManager.findByIdManager(3);
      nb = terminaleManager.getNumberEmplacementsLibresManager(t3);
      assertTrue(nb == 100);

      final Terminale newT = new Terminale();
      nb = terminaleManager.getNumberEmplacementsLibresManager(newT);
      assertTrue(nb == 0);

      nb = terminaleManager.getNumberEmplacementsLibresManager(null);
      assertTrue(nb == 0);
   }

   /**
    * Test la méthode getNumberEmplacementsOccupes.
    */
   @Test
   public void testGetNumberEmplacementsOccupes(){

      final Terminale t1 = terminaleManager.findByIdManager(1);
      Long nb = terminaleManager.getNumberEmplacementsOccupesManager(t1);
      assertTrue(nb == 3);

      final Terminale t6 = terminaleManager.findByIdManager(6);
      nb = terminaleManager.getNumberEmplacementsOccupesManager(t6);
      assertTrue(nb == 0);

      final Terminale t3 = terminaleManager.findByIdManager(3);
      nb = terminaleManager.getNumberEmplacementsOccupesManager(t3);
      assertTrue(nb == 0);

      final Terminale newT = new Terminale();
      nb = terminaleManager.getNumberEmplacementsOccupesManager(newT);
      assertTrue(nb == 0);

      nb = terminaleManager.getNumberEmplacementsOccupesManager(null);
      assertTrue(nb == 0);
   }

   /**
    * Test de la méthode getCodesLignesManager().
    */
   @Test
   public void testGetCodesLignesManager(){
      final Terminale t1 = terminaleManager.findByIdManager(1);
      List<String> list = terminaleManager.getCodesLignesManager(t1);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(4).equals("E"));
      assertTrue(list.get(9).equals("J"));

      final Terminale t2 = terminaleManager.findByIdManager(2);
      list = terminaleManager.getCodesLignesManager(t2);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));

      final Terminale t4 = terminaleManager.findByIdManager(4);
      list = terminaleManager.getCodesLignesManager(t4);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));

      final Terminale t5 = terminaleManager.findByIdManager(5);
      list = terminaleManager.getCodesLignesManager(t5);
      assertTrue(list.size() == 11);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(10).equals("11"));

      final Terminale t6 = terminaleManager.findByIdManager(6);
      list = terminaleManager.getCodesLignesManager(t6);
      assertTrue(list.size() == 9);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(4).equals("E"));
      assertTrue(list.get(8).equals("I"));

      list = terminaleManager.getCodesLignesManager(null);
      assertTrue(list.size() == 0);

      list = terminaleManager.getCodesLignesManager(new Terminale());
      assertTrue(list.size() == 0);
   }

   /**
    * Test de la méthode getCodesColonnesManager().
    */
   @Test
   public void testGetCodesColonnesManager(){
      final Terminale t1 = terminaleManager.findByIdManager(1);
      List<String> list = terminaleManager.getCodesColonnesManager(t1, 1);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(4).equals("E"));
      assertTrue(list.get(9).equals("J"));
      list = terminaleManager.getCodesColonnesManager(t1, 5);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(4).equals("E"));
      assertTrue(list.get(9).equals("J"));
      list = terminaleManager.getCodesColonnesManager(t1, 10);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(4).equals("E"));
      assertTrue(list.get(9).equals("J"));

      final Terminale t2 = terminaleManager.findByIdManager(2);
      list = terminaleManager.getCodesColonnesManager(t2, 1);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));
      list = terminaleManager.getCodesColonnesManager(t2, 5);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));
      list = terminaleManager.getCodesColonnesManager(t2, 10);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));

      final Terminale t4 = terminaleManager.findByIdManager(4);
      list = terminaleManager.getCodesColonnesManager(t4, 1);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));
      list = terminaleManager.getCodesColonnesManager(t4, 5);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));
      list = terminaleManager.getCodesColonnesManager(t4, 10);
      assertTrue(list.size() == 10);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(9).equals("10"));

      final Terminale t5 = terminaleManager.findByIdManager(5);
      list = terminaleManager.getCodesColonnesManager(t5, 1);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(1).equals("B"));
      list = terminaleManager.getCodesColonnesManager(t5, 5);
      assertTrue(list.size() == 6);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(5).equals("F"));
      list = terminaleManager.getCodesColonnesManager(t5, 11);
      assertTrue(list.size() == 4);
      assertTrue(list.get(0).equals("A"));
      assertTrue(list.get(3).equals("D"));

      final Terminale t6 = terminaleManager.findByIdManager(6);
      list = terminaleManager.getCodesColonnesManager(t6, 1);
      assertTrue(list.size() == 9);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(8).equals("9"));
      list = terminaleManager.getCodesColonnesManager(t6, 5);
      assertTrue(list.size() == 9);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(8).equals("9"));
      list = terminaleManager.getCodesColonnesManager(t6, 9);
      assertTrue(list.size() == 9);
      assertTrue(list.get(0).equals("1"));
      assertTrue(list.get(4).equals("5"));
      assertTrue(list.get(8).equals("9"));

      list = terminaleManager.getCodesColonnesManager(null, 5);
      assertTrue(list.size() == 0);
      list = terminaleManager.getCodesColonnesManager(t1, null);
      assertTrue(list.size() == 0);
      list = terminaleManager.getCodesColonnesManager(null, null);
      assertTrue(list.size() == 0);
      list = terminaleManager.getCodesColonnesManager(t1, 0);
      assertTrue(list.size() == 0);

      list = terminaleManager.getCodesColonnesManager(new Terminale(), 1);
      assertTrue(list.size() == 0);
   }

   /**
    * Test de la méthode getEchantillonsManager().
    */
   @Test
   public void testGetEchantillonsManager(){
      final Terminale t1 = terminaleManager.findByIdManager(1);
      List<Echantillon> liste = terminaleManager.getEchantillonsManager(t1);
      assertTrue(liste.size() == 1);

      final Terminale t2 = terminaleManager.findByIdManager(2);
      liste = terminaleManager.getEchantillonsManager(t2);
      assertTrue(liste.size() == 0);

      liste = terminaleManager.getEchantillonsManager(new Terminale());
      assertTrue(liste.size() == 0);

      liste = terminaleManager.getEchantillonsManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test de la méthode getProdDerivesManager().
    */
   @Test
   public void testGetProdDerivesManager(){
      final Terminale t1 = terminaleManager.findByIdManager(1);
      List<ProdDerive> liste = terminaleManager.getProdDerivesManager(t1);
      assertTrue(liste.size() == 2);

      final Terminale t3 = terminaleManager.findByIdManager(3);
      liste = terminaleManager.getProdDerivesManager(t3);
      assertTrue(liste.size() == 0);

      liste = terminaleManager.getProdDerivesManager(new Terminale());
      assertTrue(liste.size() == 0);

      liste = terminaleManager.getProdDerivesManager(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "BT1";
      final String nom2 = "BT10";
      final Enceinte e1 = enceinteDao.findById(3);
      final Enceinte e2 = enceinteDao.findById(2);

      final Terminale t1 = new Terminale();
      t1.setNom(nom1);
      t1.setEnceinte(e1);
      assertTrue(terminaleManager.findDoublonManager(t1));

      t1.setNom(nom2);
      assertFalse(terminaleManager.findDoublonManager(t1));

      t1.setNom(nom1);
      t1.setEnceinte(e2);
      assertFalse(terminaleManager.findDoublonManager(t1));

      final Terminale t2 = terminaleManager.findByIdManager(2);
      assertFalse(terminaleManager.findDoublonManager(t2));

      t2.setNom(nom1);
      assertTrue(terminaleManager.findDoublonManager(t2));

      t2.setEnceinte(e2);
      assertFalse(terminaleManager.findDoublonManager(t2));

      assertFalse(terminaleManager.findDoublonManager(null));

   }

   /**
    * Test la méthode findDoublonWithoutTwoTerminalesManager.
    */
   @Test
   public void testFindDoublonWithoutTwoTerminalesManager(){

      final Terminale t1 = terminaleManager.findByIdManager(1);
      final Terminale t2 = terminaleManager.findByIdManager(2);
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t1, t2));

      final Terminale t4 = terminaleManager.findByIdManager(4);
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t1, t4));
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t4, t1));

      t4.setEnceinte(t1.getEnceinte());
      assertTrue(terminaleManager.findDoublonWithoutTwoTerminalesManager(t4, t2));
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t1, t4));
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t4, t1));

      final Enceinte e = enceinteDao.findById(5);
      t4.setEnceinte(e);
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t4, t2));

      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(null, t4));
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t2, null));
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(null, null));

      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(t2, new Terminale()));
      assertFalse(terminaleManager.findDoublonWithoutTwoTerminalesManager(new Terminale(), t4));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Terminale t1 = terminaleManager.findByIdManager(1);
      assertTrue(terminaleManager.isUsedObjectManager(t1));

      final Terminale t6 = terminaleManager.findByIdManager(6);
      assertFalse(terminaleManager.isUsedObjectManager(t6));

      final Terminale t2 = terminaleManager.findByIdManager(2);
      assertFalse(terminaleManager.isUsedObjectManager(t2));

      final Terminale newT = new Terminale();
      assertFalse(terminaleManager.isUsedObjectManager(newT));

      assertFalse(terminaleManager.isUsedObjectManager(null));
   }

   /**
    * Test de la méthode getListOfParentsManager().
    */
   @Test
   public void testGetListOfParentsManager(){
      final Terminale t1 = terminaleManager.findByIdManager(1);
      List<Object> results = terminaleManager.getListOfParentsManager(t1);
      assertTrue(results.size() == 3);
      assertTrue(results.get(0).equals(conteneurDao.findById(1)));
      assertTrue(results.get(1).equals(enceinteDao.findById(1)));
      assertTrue(results.get(2).equals(enceinteDao.findById(3)));

      final Terminale t5 = terminaleManager.findByIdManager(5);
      results = terminaleManager.getListOfParentsManager(t5);
      assertTrue(results.size() == 3);
      assertTrue(results.get(0).equals(conteneurDao.findById(1)));
      assertTrue(results.get(1).equals(enceinteDao.findById(2)));
      assertTrue(results.get(2).equals(enceinteDao.findById(6)));

      results = terminaleManager.getListOfParentsManager(null);
      assertTrue(results.size() == 0);
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      saveManagerTest();
      saveManagerTest();
      removeObjectManagerTest();
   }

   private void saveManagerTest() throws ParseException{

      final Enceinte enc = enceinteDao.findById(3);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Banque banque = banqueDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Utilisateur u = utilisateurDao.findById(1);

      final Terminale t1 = new Terminale();
      t1.setNom("BT3");
      t1.setPosition(3);

      Boolean catched = false;
      // on test l'insertion avec l'enceinte nulle
      try{
         terminaleManager.saveManager(t1, null, type, null, null, num, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test l'insertion avec la terminaleType nulle
      try{
         terminaleManager.saveManager(t1, enc, null, null, null, num, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test l'insertion avec la terminalenumerotation nulle
      try{
         terminaleManager.saveManager(t1, enc, type, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test l'insertion d'un doublon
      t1.setNom("BT1");
      try{
         terminaleManager.saveManager(t1, enc, type, null, null, num, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la validation de la position
      t1.setNom("BT50");
      t1.setPosition(50);
      try{
         terminaleManager.saveManager(t1, enc, type, null, null, num, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on vérifie que la position est libre
      t1.setNom("BT50");
      t1.setPosition(2);
      try{
         terminaleManager.saveManager(t1, enc, type, null, null, num, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // Test de la validation lors de la création
      t1.setNom("BT3");
      t1.setPosition(3);
      try{
         validationInsert(t1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // On test une insertion valide avec les assos non obligatoires à null
      t1.setNom("BT3");
      t1.setPosition(3);
      t1.setAlias("ALIAS");
      terminaleManager.saveManager(t1, enc, type, null, null, num, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(t1).size() == 1);
      final int id = t1.getTerminaleId();
      // Vérification
      final Terminale tTest = terminaleManager.findByIdManager(id);
      assertNotNull(tTest);
      assertNotNull(tTest.getEnceinte());
      assertNotNull(tTest.getTerminaleNumerotation());
      assertNotNull(tTest.getTerminaleType());
      assertTrue(tTest.getPosition() == 3);
      assertTrue(tTest.getNom().equals("BT3"));
      assertTrue(tTest.getAlias().equals("ALIAS"));

      // On test une insertion valide avec toutes les assos
      final Terminale t2 = new Terminale();
      t2.setNom("BT4");
      t2.setPosition(4);
      t2.setAlias("ALIAS4");
      final Couleur coul = couleurDao.findById(6);
      terminaleManager.saveManager(t2, enc, type, banque, ent, num, coul, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 8);
      assertTrue(getOperationManager().findByObjectManager(t2).size() == 1);
      final int id2 = t2.getTerminaleId();
      // Vérification
      final Terminale tTest2 = terminaleManager.findByIdManager(id2);
      assertNotNull(tTest2);
      assertNotNull(tTest2.getEnceinte());
      assertNotNull(tTest2.getTerminaleNumerotation());
      assertNotNull(tTest2.getTerminaleType());
      assertNotNull(tTest2.getBanque());
      assertNotNull(tTest2.getEntite());
      assertNotNull(tTest2.getCouleur());
      assertTrue(tTest2.getPosition() == 4);
      assertTrue(tTest2.getNom().equals("BT4"));
      assertTrue(tTest2.getAlias().equals("ALIAS4"));

      // Suppression
      terminaleManager.removeObjectManager(tTest, null, u);
      terminaleManager.removeObjectManager(tTest2, null, u);
      assertTrue(getOperationManager().findByObjectManager(tTest).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(tTest2).size() == 0);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(tTest);
      fs.add(tTest2);
      cleanUpFantomes(fs);
   }

   private void saveManagerTest() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte enc = enceinteDao.findById(3);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Banque banque = banqueDao.findById(1);
      final Entite ent = entiteDao.findById(3);

      final Terminale t = new Terminale();
      t.setNom("BT3");
      t.setPosition(3);
      terminaleManager.saveManager(t, enc, type, null, ent, num, null, u);
      final Integer id = t.getTerminaleId();
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(t).size() == 1);

      final Terminale tUp1 = terminaleManager.findByIdManager(id);
      Boolean catched = false;
      // on test l'update avec l'enceinte nulle
      try{
         terminaleManager.saveManager(tUp1, null, type, null, null, num, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      // on test l'update avec la terminaleType nulle
      try{
         terminaleManager.saveManager(tUp1, enc, null, null, null, num, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      // on test l'update avec la terminalenumerotation nulle
      try{
         terminaleManager.saveManager(tUp1, enc, type, null, null, null, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      // on test l'update d'un doublon
      tUp1.setNom("BT1");
      try{
         terminaleManager.saveManager(tUp1, enc, type, null, null, num, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      // on test la validation de la position
      tUp1.setNom("BT50");
      tUp1.setPosition(50);
      try{
         terminaleManager.saveManager(tUp1, enc, type, null, null, num, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      // on vérifie que la position est libre
      tUp1.setNom("BT50");
      tUp1.setPosition(2);
      try{
         terminaleManager.saveManager(tUp1, enc, type, null, null, num, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      // Test de la validation lors de l'update
      tUp1.setNom("BT3");
      tUp1.setPosition(3);
      try{
         validationUpdate(tUp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);

      catched = false;
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

      tUp1.setNom("BTUP3");
      tUp1.setPosition(3);
      tUp1.setAlias("ALIAS UP");

      try{
         terminaleManager.saveManager(tUp1, enc, type, null, null, num, null, incs, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

      incs.remove(1);

      terminaleManager.saveManager(tUp1, enc, type, null, null, num, null, incs, u, null);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(tUp1).size() == 2);
      // Vérification
      final Terminale tTest = terminaleManager.findByIdManager(id);
      assertNotNull(tTest);
      assertNotNull(tTest.getEnceinte());
      assertNotNull(tTest.getTerminaleNumerotation());
      assertNotNull(tTest.getTerminaleType());
      assertTrue(tTest.getPosition() == 3);
      assertTrue(tTest.getNom().equals("BTUP3"));
      assertTrue(tTest.getAlias().equals("ALIAS UP"));

      inc1 = incidentManager.findByTerminaleManager(tTest).get(0);
      assertTrue(inc1.getNom().equals("TEST"));
      assertTrue(inc1.getDescription() == null);

      incs.clear();
      inc1.setDescription("incident pour enceinte update");
      incs.add(inc1);

      // On test un update valide avec toutes les assos
      final Terminale tUp2 = terminaleManager.findByIdManager(id);
      tUp2.setNom("BTUP4");
      tUp2.setPosition(4);
      tUp2.setAlias("ALIASUP4");
      final List<OperationType> opTypes = new ArrayList<>();
      opTypes.add(operationTypeManager.findByIdManager(1));
      opTypes.add(operationTypeManager.findByIdManager(2));
      terminaleManager.saveManager(tUp2, enc, type, banque, ent, num, couleurDao.findById(8), incs, u, opTypes);
      assertTrue(getOperationManager().findByObjectManager(tUp2).size() == 5);
      // Vérification
      final Terminale tTest2 = terminaleManager.findByIdManager(id);
      assertNotNull(tTest2);
      assertNotNull(tTest2.getEnceinte());
      assertNotNull(tTest2.getTerminaleNumerotation());
      assertNotNull(tTest2.getTerminaleType());
      assertNotNull(tTest2.getBanque());
      assertNotNull(tTest2.getEntite());
      assertNotNull(tTest2.getCouleur());
      assertTrue(tTest2.getPosition() == 4);
      assertTrue(tTest2.getNom().equals("BTUP4"));
      assertTrue(tTest2.getAlias().equals("ALIASUP4"));

      inc1 = incidentManager.findByTerminaleManager(tTest2).get(0);
      assertTrue(inc1.getNom().equals("TEST"));
      assertTrue(inc1.getDescription().equals("incident pour enceinte update"));

      // Suppression
      terminaleManager.removeObjectManager(tTest2, null, u);
      assertTrue(getOperationManager().findByObjectManager(tTest2).size() == 0);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      assertTrue(incidentManager.findAllObjectsManager().size() == 5);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(tTest2);
      cleanUpFantomes(fs);
   }

   private void removeObjectManagerTest(){
      final Utilisateur u = utilisateurDao.findById(1);
      // test de la suppression d'un objet null
      terminaleManager.removeObjectManager(null, null, null);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // test de la suppression d'un objet utilisé
      final Terminale t1 = terminaleManager.findByIdManager(1);
      boolean catched = false;
      try{
         terminaleManager.removeObjectManager(t1, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // Test de la suppression en cascade des emplacements
      final Enceinte enc = enceinteDao.findById(3);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Terminale newT = new Terminale();
      newT.setNom("BT3");
      newT.setPosition(3);
      terminaleManager.saveManager(newT, enc, type, null, null, num, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 7);
      final Integer id = newT.getTerminaleId();

      final Terminale tToRemove = terminaleManager.findByIdManager(id);
      emplacementManager.createMultiObjetcsManager(tToRemove, 10);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 17);

      terminaleManager.removeObjectManager(tToRemove, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(tToRemove);
      cleanUpFantomes(fs);
   }

   /**
    * Test de la méthode createMultiObjetcsManager().
    */
   @Test
   public void testCreateMultiObjetcsManager(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte e3 = enceinteDao.findById(3);
      final Enceinte e4 = enceinteDao.findById(4);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Banque banque = banqueDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Terminale newTs = new Terminale();
      newTs.setTerminaleNumerotation(num);
      newTs.setTerminaleType(type);
      newTs.setBanque(banque);
      newTs.setEntite(ent);
      newTs.setNom("BT");
      newTs.setAlias("ALIAS");

      // Test de la création avec la terminale à null
      terminaleManager.createMultiObjetcsManager(e4, null, 5, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      Boolean catched = false;
      // on test la création avec l'enceinte nulle
      try{
         terminaleManager.createMultiObjetcsManager(null, newTs, 5, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la création avec un nombre trop grand
      try{
         terminaleManager.createMultiObjetcsManager(e4, newTs, 155, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la création avec un nombre null
      try{
         terminaleManager.createMultiObjetcsManager(e4, newTs, null, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la création avec un nombre négatif
      try{
         terminaleManager.createMultiObjetcsManager(e4, newTs, -10, null, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la génération d'une exception
      try{
         terminaleManager.createMultiObjetcsManager(e3, newTs, 2, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on teste une création multiple valide avec une 1ère position
      // à 50
      final List<Terminale> list = terminaleManager.createMultiObjetcsManager(e4, newTs, 5, 50, u);
      assertTrue(list.size() == 5);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 11);
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getEnceinte());
      assertNotNull(list.get(0).getTerminaleNumerotation());
      assertNotNull(list.get(0).getTerminaleType());
      assertNotNull(list.get(0).getBanque());
      assertNotNull(list.get(0).getEntite());
      assertTrue(list.get(0).getPosition() == 1);
      assertTrue(list.get(0).getAlias().equals("ALIAS1"));
      assertFalse(list.get(0).getArchive());
      assertTrue(list.get(0).getNom().equals("BT50"));
      assertTrue(list.get(1).getNom().equals("BT51"));
      assertTrue(list.get(4).getNom().equals("BT54"));

      for(int i = 0; i < list.size(); i++){
         terminaleManager.removeObjectManager(list.get(i), null, u);
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on teste une création multiple valide avec une 1ère position
      // à null
      final List<Terminale> list2 = terminaleManager.createMultiObjetcsManager(e4, newTs, 5, null, u);
      assertTrue(list2.size() == 5);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 11);
      assertNotNull(list2.get(0));
      assertTrue(list2.get(0).getNom().equals("BT1"));
      assertTrue(list2.get(1).getNom().equals("BT2"));
      assertTrue(list2.get(4).getNom().equals("BT5"));

      for(int i = 0; i < list2.size(); i++){
         terminaleManager.removeObjectManager(list2.get(i), null, u);
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      fs.addAll(list2);
      cleanUpFantomes(fs);

   }

   /**
    * Test la validation d'une Terminale lors de sa création.
    * @param terminale Terminale à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Terminale terminale) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte enc = enceinteDao.findById(3);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      boolean catchedInsert = false;

      // On teste une insertion avec un attribut position non valide
      catchedInsert = false;
      try{
         terminale.setPosition(-1);
         terminaleManager.saveManager(terminale, enc, type, null, null, num, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      terminale.setPosition(3);

      // On teste une insertion avec un attribut nom non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$¤¤$*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            terminale.setNom(emptyValues[i]);
            terminaleManager.saveManager(terminale, enc, type, null, null, num, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      terminale.setNom("BT3");

      // On teste une insertion avec un attribut alias non valide
      emptyValues = new String[] {"", "  ", "%$$*¤¤gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            terminale.setAlias(emptyValues[i]);
            terminaleManager.saveManager(terminale, enc, type, null, null, num, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      terminale.setAlias(null);
   }

   /**
    * Test la validation d'une Terminale lors de son update.
    * @param terminale Terminale à tester.
    * @throws ParseException 
    */
   private void validationUpdate(Terminale terminale) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte enc = enceinteDao.findById(3);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      boolean catchedInsert = false;

      // On teste un update avec un attribut position non valide
      catchedInsert = false;
      try{
         terminale.setPosition(-1);
         terminaleManager.saveManager(terminale, enc, type, null, null, num, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      terminale.setPosition(3);

      // On teste un update avec un attribut nom non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$¤*gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            terminale.setNom(emptyValues[i]);
            terminaleManager.saveManager(terminale, enc, type, null, null, num, null, null, u, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      terminale.setNom("BT3");

      // On teste un update avec un attribut alias non valide
      emptyValues = new String[] {"", "  ", "%$$*¤gd", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            terminale.setAlias(emptyValues[i]);
            terminaleManager.saveManager(terminale, enc, type, null, null, num, null, null, u, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      terminale.setAlias(null);

      // On teste une insertion avec un doublon incident
      terminale = terminaleManager.findByIdManager(3);
      final List<Incident> incs = incidentManager.findByTerminaleManager(terminale);
      final Incident inc1 = new Incident();
      inc1.setNom(incs.get(0).getNom());
      inc1.setDate(incs.get(0).getDate());
      incs.add(inc1);
      boolean catched = false;
      try{
         terminaleManager.saveManager(terminale, enc, type, null, null, num, null, incs, u, null);
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
         terminaleManager.saveManager(terminale, enc, type, null, null, num, null, incs, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
   }

   /**
    * Test de la méthode echangerDeuxTerminalesManager().
    */
   @Test
   public void testEchangerDeuxTerminalesManager(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Terminale t1 = terminaleManager.findByIdManager(1);
      final Terminale t2 = terminaleManager.findByIdManager(2);
      final Terminale t4 = terminaleManager.findByIdManager(4);

      final Enceinte ep1 = t1.getEnceinte();
      final Integer pos1 = t1.getPosition();

      final Enceinte ep2 = t2.getEnceinte();
      final Integer pos2 = t2.getPosition();

      final Enceinte ep4 = t4.getEnceinte();
      final Integer pos4 = t4.getPosition();

      terminaleManager.echangerDeuxTerminalesManager(t4, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      Terminale tTest4 = terminaleManager.findByIdManager(4);
      assertTrue(tTest4.getEnceinte().equals(ep4));
      assertTrue(tTest4.getPosition().equals(pos4));

      terminaleManager.echangerDeuxTerminalesManager(t4, new Terminale(), u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      tTest4 = terminaleManager.findByIdManager(4);
      assertTrue(tTest4.getEnceinte().equals(ep4));
      assertTrue(tTest4.getPosition().equals(pos4));

      terminaleManager.echangerDeuxTerminalesManager(null, t4, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      tTest4 = terminaleManager.findByIdManager(4);
      assertTrue(tTest4.getEnceinte().equals(ep4));
      assertTrue(tTest4.getPosition().equals(pos4));

      terminaleManager.echangerDeuxTerminalesManager(new Terminale(), t4, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
      tTest4 = terminaleManager.findByIdManager(4);
      assertTrue(tTest4.getEnceinte().equals(ep4));
      assertTrue(tTest4.getPosition().equals(pos4));

      // on teste les doublons
      t2.setEnceinte(ep4);
      t2.setPosition(pos4);
      t4.setEnceinte(ep2);
      t4.setPosition(pos2);

      boolean catched = false;
      try{
         terminaleManager.echangerDeuxTerminalesManager(t2, t4, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      try{
         terminaleManager.echangerDeuxTerminalesManager(t4, t2, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on teste un déplacement valide
      t1.setEnceinte(ep4);
      t1.setPosition(pos4);
      t2.setEnceinte(ep2);
      t2.setPosition(pos2);
      t4.setEnceinte(ep1);
      t4.setPosition(pos1);
      terminaleManager.echangerDeuxTerminalesManager(t4, t1, u);
      tTest4 = terminaleManager.findByIdManager(4);
      assertTrue(tTest4.getEnceinte().equals(ep1));
      assertTrue(tTest4.getPosition().equals(pos1));
      Terminale tTest1 = terminaleManager.findByIdManager(1);
      assertTrue(tTest1.getEnceinte().equals(ep4));
      assertTrue(tTest1.getPosition().equals(pos4));
      assertTrue(getOperationManager().findByObjectManager(tTest4).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(tTest1).size() == 1);

      // on refait l'échange dans l'autre sens
      t1.setEnceinte(ep1);
      t1.setPosition(pos1);
      t4.setEnceinte(ep4);
      t4.setPosition(pos4);
      terminaleManager.echangerDeuxTerminalesManager(t4, t1, u);
      tTest4 = terminaleManager.findByIdManager(4);
      assertTrue(tTest4.getEnceinte().equals(ep4));
      assertTrue(tTest4.getPosition().equals(pos4));
      tTest1 = terminaleManager.findByIdManager(1);
      assertTrue(tTest1.getEnceinte().equals(ep1));
      assertTrue(tTest1.getPosition().equals(pos1));
      assertTrue(getOperationManager().findByObjectManager(tTest4).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(tTest1).size() == 2);

      // on teste l'échange de 2 enceintes cote a cote		
      t1.setEnceinte(ep2);
      t1.setPosition(pos2);
      t2.setEnceinte(ep1);
      t2.setPosition(pos1);
      terminaleManager.echangerDeuxTerminalesManager(t1, t2, u);
      Terminale tTest2 = terminaleManager.findByIdManager(2);
      assertTrue(tTest2.getEnceinte().equals(ep1));
      assertTrue(tTest2.getPosition().equals(pos1));
      tTest1 = terminaleManager.findByIdManager(1);
      assertTrue(tTest1.getEnceinte().equals(ep2));
      assertTrue(tTest1.getPosition().equals(pos2));

      // on refait l'échange dans l'autre sens
      t1.setEnceinte(ep1);
      t1.setPosition(pos1);
      t2.setEnceinte(ep2);
      t2.setPosition(pos2);
      terminaleManager.echangerDeuxTerminalesManager(t1, t2, u);
      tTest2 = terminaleManager.findByIdManager(2);
      assertTrue(tTest2.getEnceinte().equals(ep2));
      assertTrue(tTest2.getPosition().equals(pos2));
      tTest1 = terminaleManager.findByIdManager(1);
      assertTrue(tTest1.getEnceinte().equals(ep1));
      assertTrue(tTest1.getPosition().equals(pos1));

      List<Operation> ops = getOperationManager().findByObjectManager(tTest2);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      ops = getOperationManager().findByObjectManager(tTest1);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      ops = getOperationManager().findByObjectManager(tTest4);
      for(int i = 0; i < ops.size(); i++){
         getOperationManager().removeObjectManager(ops.get(i));
      }
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   @Test
   public void testUpdateNumerotationForMultiTerminales(){
      // Création d'une nouvelle arborescence
      final Utilisateur u = utilisateurDao.findById(1);
      final ConteneurType type = conteneurTypeDao.findById(1);
      final Service serv = serviceDao.findById(1);
      final Banque bank1 = banqueDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(bank1);
      final Plateforme pf1 = plateformeDao.findById(1);
      final List<Plateforme> plateformes = new ArrayList<>();
      plateformes.add(pf1);

      final EnceinteType eType1 = enceinteTypeDao.findById(6);
      final EnceinteType eType2 = enceinteTypeDao.findById(2);
      final EnceinteType eType3 = enceinteTypeDao.findById(1);
      final List<Enceinte> enceintes = new ArrayList<>();

      final TerminaleType tType = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);

      // conteneur
      final Conteneur c = new Conteneur();
      c.setCode("C2");
      c.setNom("Congélateur N2");
      c.setTemp((float) -50);
      c.setNbrEnc(2);
      c.setNbrNiv(4);
      c.setConteneurType(type);
      c.setService(serv);
      c.setArchive(false);

      // Enceinte 1er niveau
      final Enceinte e1 = new Enceinte();
      e1.setEnceinteType(eType1);
      e1.setNom("R");
      e1.setNbPlaces(3);

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

      enceintes.add(e1);
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
      goodPositions.add(null);
      final List<Integer> badPositions = new ArrayList<>();
      badPositions.add(1);

      try{
         conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, plateformes, null, false, u,
            bank1.getPlateforme());
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 96);

      final Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
      // Récupération de la liste de terminales
      final List<Terminale> termsToUp = conteneurManager.getAllTerminalesInArborescenceManager(cTest);
      final TerminaleNumerotation num2 = terminaleNumerotationDao.findById(2);
      // modif des numérotations
      terminaleManager.updateNumerotationForMultiTerminales(termsToUp, num2, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 96);

      // Vérification
      final List<Terminale> termsToTest = conteneurManager.getAllTerminalesInArborescenceManager(cTest);
      for(int i = 0; i < termsToTest.size(); i++){
         assertTrue(termsToTest.get(i).getTerminaleNumerotation().equals(num2));
         assertTrue(termsToTest.get(i).getEnceinte().equals(termsToUp.get(i).getEnceinte()));
         assertTrue(termsToTest.get(i).getTerminaleType().equals(termsToUp.get(i).getTerminaleType()));
         assertTrue(termsToTest.get(i).getNom().equals(termsToUp.get(i).getNom()));
         assertTrue(termsToTest.get(i).getPosition().equals(termsToUp.get(i).getPosition()));
      }

      // Suppression
      final List<Enceinte> encs = enceinteManager.findAllEnceinteByConteneurManager(c);
      assertTrue(encs.size() == 26);

      final List<Terminale> terminales = new ArrayList<>();
      final Iterator<Enceinte> encIt = encs.iterator();
      while(encIt.hasNext()){
         terminales.addAll(terminaleManager.findByEnceinteWithOrderManager(encIt.next()));
      }
      assertTrue(terminales.size() == 90);

      conteneurManager.removeObjectManager(cTest, null, u);

      assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
      assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   /**
    * Test de la méthode createMultiVisotubesManager().
    */
   @Test
   public void testCreateMultiVisotubesManager(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Enceinte e3 = enceinteDao.findById(3);
      final Enceinte e4 = enceinteDao.findById(4);
      final TerminaleType type = terminaleTypeDao.findById(1);
      final TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
      final Banque banque = banqueDao.findById(1);
      final Entite ent = entiteDao.findById(3);
      final Terminale newTs = new Terminale();
      newTs.setTerminaleNumerotation(num);
      newTs.setTerminaleType(type);
      newTs.setBanque(banque);
      newTs.setEntite(ent);
      newTs.setNom("VIS");
      newTs.setAlias("ALIAS");

      // Test de la création avec la terminale à null
      terminaleManager.createMultiVisotubesManager(e4, null, 5, null, false, 16, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // teste visotube sans taille
      terminaleManager.createMultiVisotubesManager(e4, null, 5, null, false, null, u);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // teste visotube taille introuvable

      Boolean catched = false;
      // on test la création avec l'enceinte nulle
      try{
         terminaleManager.createMultiVisotubesManager(null, newTs, 5, null, false, 16, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la création avec un nombre trop grand
      try{
         terminaleManager.createMultiVisotubesManager(e4, newTs, 155, null, false, 10, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la création avec un nombre null
      try{
         terminaleManager.createMultiVisotubesManager(e4, newTs, null, null, false, 16, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la création avec un nombre négatif
      try{
         terminaleManager.createMultiVisotubesManager(e4, newTs, -10, null, false, 16, u);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on test la génération d'une exception
      try{
         terminaleManager.createMultiVisotubesManager(e3, newTs, 2, null, false, 16, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("UsedPositionException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      catched = false;
      // on test la création avec l'enceinte nulle
      try{
         terminaleManager.createMultiVisotubesManager(e4, newTs, 5, null, false, 22, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on teste une création multiple valide avec une 1ère position
      // à 50
      final List<Terminale> list = terminaleManager.createMultiVisotubesManager(e4, newTs, 5, 50, false, 16, u);
      assertTrue(list.size() == 5);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 11);
      assertNotNull(list.get(0));
      assertNotNull(list.get(0).getEnceinte());
      assertNotNull(list.get(0).getTerminaleNumerotation());
      assertNotNull(list.get(0).getTerminaleType());
      assertNotNull(list.get(0).getBanque());
      assertNotNull(list.get(0).getEntite());
      assertTrue(list.get(0).getPosition() == 1);
      assertTrue(list.get(0).getAlias().equals("ALIAS1"));
      assertFalse(list.get(0).getArchive());
      assertTrue(list.get(0).getNom().equals("VIS50"));
      assertTrue(list.get(1).getNom().equals("VIS51"));
      assertTrue(list.get(4).getNom().equals("VIS54"));

      // on vérifie que le premier est bien visotube rond trasparent
      assertTrue(list.get(0).getTerminaleType().getType().equals("VISOTUBE_16_ROND"));
      assertTrue(list.get(0).getCouleur().getCouleur().equals("TRANSPARENT"));

      // on vérifie que les autres sont bien des visotubes traingulaires
      assertTrue(list.get(1).getTerminaleType().getType().equals("VISOTUBE_16_TRI"));
      assertTrue(list.get(1).getCouleur().getCouleur().equals("NOIR"));
      assertTrue(list.get(2).getTerminaleType().getType().equals("VISOTUBE_16_TRI"));
      assertTrue(list.get(2).getCouleur().getCouleur().equals("MARRON"));
      assertTrue(list.get(3).getTerminaleType().getType().equals("VISOTUBE_16_TRI"));
      assertTrue(list.get(3).getCouleur().getCouleur().equals("ROUGE"));

      for(int i = 0; i < list.size(); i++){
         terminaleManager.removeObjectManager(list.get(i), null, u);
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      // on teste une création multiple valide avec une 1ère position
      // à null et plus de visotubes que de couleurs
      final List<Terminale> list2 = terminaleManager.createMultiVisotubesManager(e4, newTs, 5, null, true, 16, u);
      assertTrue(list2.size() == 5);
      assertTrue(terminaleManager.findAllObjectsManager().size() == 11);
      assertNotNull(list2.get(0));
      assertTrue(list2.get(0).getNom().equals("TRANSPARENT1"));
      assertTrue(list2.get(1).getNom().equals("NOIR2"));
      assertTrue(list2.get(4).getNom().equals("VERT5"));

      for(int i = 0; i < list2.size(); i++){
         terminaleManager.removeObjectManager(list2.get(i), null, u);
      }
      assertTrue(terminaleManager.findAllObjectsManager().size() == 6);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.addAll(list);
      fs.addAll(list2);
      cleanUpFantomes(fs);
   }

   @Test
   public void testGetTkObjectsAndEmplacementsManager(){
      Map<TKStockableObject, Emplacement> emps =
         terminaleManager.getTkObjectsAndEmplacementsManager(terminaleManager.findByIdManager(1));
      assertTrue(emps.size() == 3);
      assertTrue(emps.containsKey(echantillonDao.findById(2)));
      assertTrue(emps.get(echantillonDao.findById(2)).getEmplacementId().equals(3));
      assertTrue(emps.containsKey(prodDeriveDao.findById(1)));
      assertTrue(emps.get(prodDeriveDao.findById(1)).getEmplacementId().equals(1));
      assertTrue(emps.containsKey(prodDeriveDao.findById(2)));
      assertTrue(emps.get(prodDeriveDao.findById(2)).getEmplacementId().equals(2));

      emps = terminaleManager.getTkObjectsAndEmplacementsManager(terminaleManager.findByIdManager(2));
      assertTrue(emps.isEmpty());

      emps = terminaleManager.getTkObjectsAndEmplacementsManager(terminaleManager.findByIdManager(2));
      assertTrue(emps.isEmpty());
   }

   // @since 2.1
   @Test
   public void testFindTerminaleIdsFromNomManager(){
      final List<Integer> ids = terminaleManager.findTerminaleIdsFromNomManager(null, null, null);
      assertTrue(ids.isEmpty());

      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT1", null, null));
      assertTrue(ids.isEmpty());

      final List<Conteneur> conts = new ArrayList<>();

      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT1", null, conts));
      assertTrue(ids.isEmpty());

      conts.addAll(IterableUtils.toList(conteneurDao.findAll()));
      // nom find
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT1", null, conts));
      assertTrue(ids.size() == 4);
      assertTrue(ids.contains(1));
      assertTrue(ids.contains(4));
      assertTrue(ids.contains(5));
      assertTrue(ids.contains(6));

      // alias find
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("ALIAS BT1", null, conts));
      assertTrue(ids.size() == 3);
      assertTrue(ids.contains(1));
      assertTrue(ids.contains(4));
      assertTrue(ids.contains(6));

      // match find
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("ALIAS BT%", null, conts));
      assertTrue(ids.size() == 5);
      assertTrue(ids.contains(1));
      assertTrue(ids.contains(2));
      assertTrue(ids.contains(3));
      assertTrue(ids.contains(4));
      assertTrue(ids.contains(6));

      // conteneur restrict
      // toutes les boites sont dans le conteneur 1!
      conts.remove(conteneurDao.findById(1));
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("ALIAS BT%", null, conts));
      assertTrue(ids.isEmpty());

      conts.clear();
      conts.add(conteneurDao.findById(1));
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT2", null, conts));
      assertTrue(ids.size() == 1);
      assertTrue(ids.contains(2));

      conts.clear();
      conts.add(conteneurDao.findById(2));
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT2", null, conts));
      assertTrue(ids.isEmpty());

      ids.clear();
      conts.clear();
      conts.addAll(IterableUtils.toList(conteneurDao.findAll()));
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT2", null, conts));
      assertTrue(ids.size() == 1);
      assertTrue(ids.contains(2));

      // enceinte restrict
      conts.clear();
      conts.add(conteneurDao.findById(1));
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT1", enceinteDao.findById(1), conts));
      assertTrue(ids.size() == 2);
      assertTrue(ids.contains(1));
      assertTrue(ids.contains(4));
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT1", enceinteDao.findById(3), conts));
      assertTrue(ids.size() == 1);
      assertTrue(ids.contains(1));
      // conteneur incoherent -> empty
      conts.clear();
      conts.add(conteneurDao.findById(2));
      ids.clear();
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT1", enceinteDao.findById(3), conts));
      assertTrue(ids.isEmpty());
      ids.addAll(terminaleManager.findTerminaleIdsFromNomManager("BT2", enceinteDao.findById(4), conts));
      assertTrue(ids.isEmpty());
   }
   
   // @since 2.2.1
   @Test
   public void testGetDistinctBanquesFromTkObjectsManager() {
	   List<Banque> banks = terminaleManager.getDistinctBanquesFromTkObjectsManager(terminaleManager.findByIdManager(1));
	   assertTrue(banks.size() == 1);
	   assertTrue(banks.contains(banqueDao.findById(1)));
	   
	   banks = terminaleManager.getDistinctBanquesFromTkObjectsManager(terminaleManager.findByIdManager(2));
	   assertTrue(banks.isEmpty());

	   banks = terminaleManager.getDistinctBanquesFromTkObjectsManager(null);
	   assertTrue(banks.isEmpty());
   }
}
