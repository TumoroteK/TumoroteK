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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.stockage.*;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.*;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

import static org.junit.Assert.*;

/**
 *
 * Classe de test pour le manager ConteneurManager.
 * Classe créée le 23/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ConteneurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private ConteneurTypeDao conteneurTypeDao;
   @Autowired
   private ServiceDao serviceDao;
   @Autowired
   private EnceinteTypeDao enceinteTypeDao;
   @Autowired
   private TerminaleTypeDao terminaleTypeDao;
   @Autowired
   private TerminaleNumerotationDao terminaleNumerotationDao;
   @Autowired
   private EnceinteManager enceinteManager;
   @Autowired
   private TerminaleManager terminaleManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private RetourManager retourManager;
   @Autowired
   private IncidentManager incidentManager;

   public ConteneurManagerTest(){}

   @Test
   public void testFindById(){
      Conteneur c = conteneurManager.findByIdManager(1);
      assertNotNull(c);
      assertEquals("Congélateur 1", c.getNom());

      c = conteneurManager.findByIdManager(4);
      assertNotNull(c);

      final Conteneur cNull = conteneurManager.findByIdManager(10);
      assertNull(cNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Conteneur> list = conteneurManager.findAllObjectsManager();
      assertEquals(4, list.size());
   }

   /**
    * Test la méthode findByBanqueWithOrderManager.
    */
   @Test
   public void testFindByBanqueWithOrderManager(){
      final Banque b1 = banqueDao.findById(1);
      List<Conteneur> list = conteneurManager.findByBanqueWithOrderManager(b1);
      assertEquals(3, list.size());
      assertEquals("Congélateur 1", list.get(0).getNom());

      final Banque b2 = banqueDao.findById(2);
      list = conteneurManager.findByBanqueWithOrderManager(b2);
      assertEquals(4, list.size());

      final Banque newB = new Banque();
      list = conteneurManager.findByBanqueWithOrderManager(newB);
      assertEquals(0, list.size());

      list = conteneurManager.findByBanqueWithOrderManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode findByBanqueAndCodeManager.
    */
   @Test
   public void testFindByBanqueAndCodeManager(){
      final Banque b1 = banqueDao.findById(1);
      List<Conteneur> list = conteneurManager.findByBanqueAndCodeManager(b1, "CC1");
      assertEquals(1, list.size());
      assertEquals("Congélateur 1", list.get(0).getNom());

      list = conteneurManager.findByBanqueAndCodeManager(b1, "dcsc");
      assertEquals(0, list.size());

      final Banque b2 = banqueDao.findById(2);
      list = conteneurManager.findByBanqueAndCodeManager(b2, "CC1");
      assertEquals(1, list.size());

      final Banque newB = new Banque();
      list = conteneurManager.findByBanqueAndCodeManager(newB, "CC1");
      assertEquals(0, list.size());

      list = conteneurManager.findByBanqueAndCodeManager(null, "CC1");
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode findByBanquesWithOrderManager.
    */
   @Test
   public void testFindByBanquesWithOrderManager(){
      List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      banques.add(b1);
      banques.add(b2);
      List<Conteneur> list = conteneurManager.findByBanquesWithOrderManager(banques);
      assertEquals(4, list.size());
      assertEquals("Congélateur 1", list.get(0).getNom());

      banques = new ArrayList<>();
      banques.add(b1);
      banques.add(new Banque());
      banques.add(null);
      list = conteneurManager.findByBanquesWithOrderManager(banques);
      assertEquals(3, list.size());

      list = conteneurManager.findByBanquesWithOrderManager(new ArrayList<Banque>());
      assertEquals(0, list.size());

      list = conteneurManager.findByBanquesWithOrderManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode findByPlateformeWithOrderManager.
    */
   @Test
   public void testFindByPlateformeWithOrderManager(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<Conteneur> list = conteneurManager.findByPlateformeOrigWithOrderManager(p1);
      assertEquals(3, list.size());
      assertEquals("CC1", list.get(0).getCode());

      final Plateforme p3 = plateformeDao.findById(3);
      list = conteneurManager.findByPlateformeOrigWithOrderManager(p3);
      assertEquals(0, list.size());

      final Plateforme newP = new Plateforme();
      list = conteneurManager.findByPlateformeOrigWithOrderManager(newP);
      assertEquals(0, list.size());

      list = conteneurManager.findByPlateformeOrigWithOrderManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode getBanquesManager.
    */
   @Test
   public void testGetBanquesManager(){

      final Conteneur c1 = conteneurManager.findByIdManager(1);
      Set<Banque> set = conteneurManager.getBanquesManager(c1);
      assertEquals(4, set.size());

      final Conteneur c2 = conteneurManager.findByIdManager(2);
      set = conteneurManager.getBanquesManager(c2);
      assertEquals(2, set.size());

      final Conteneur newC = new Conteneur();
      set = conteneurManager.getBanquesManager(newC);
      assertEquals(0, set.size());

      set = conteneurManager.getBanquesManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode getEnceintesManager.
    */
   @Test
   public void testGetEnceintesManager(){

      final Conteneur c1 = conteneurManager.findByIdManager(1);
      Set<Enceinte> set = conteneurManager.getEnceintesManager(c1);
      assertEquals(2, set.size());

      final Conteneur c2 = conteneurManager.findByIdManager(2);
      set = conteneurManager.getEnceintesManager(c2);
      assertEquals(0, set.size());

      final Conteneur newC = new Conteneur();
      set = conteneurManager.getEnceintesManager(newC);
      assertEquals(0, set.size());

      set = conteneurManager.getEnceintesManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode getAllTerminalesInArborescenceManager.
    */
   @Test
   public void testGetAllTerminalesInArborescenceManager(){

      final Conteneur c1 = conteneurManager.findByIdManager(1);
      List<Terminale> list = conteneurManager.getAllTerminalesInArborescenceManager(c1);
      assertEquals(6, list.size());

      final Conteneur c2 = conteneurManager.findByIdManager(2);
      list = conteneurManager.getAllTerminalesInArborescenceManager(c2);
      assertEquals(0, list.size());

      final Conteneur newC = new Conteneur();
      list = conteneurManager.getAllTerminalesInArborescenceManager(newC);
      assertEquals(0, list.size());

      list = conteneurManager.getAllTerminalesInArborescenceManager(null);
      assertEquals(0, list.size());
   }

   /**
    * Test la méthode getIncidentsManager.
    */
   @Test
   public void testGetIncidentsManager(){

      final Conteneur c1 = conteneurManager.findByIdManager(1);
      Set<Incident> set = conteneurManager.getIncidentsManager(c1);
      assertEquals(2, set.size());

      final Conteneur c2 = conteneurManager.findByIdManager(2);
      set = conteneurManager.getIncidentsManager(c2);
      assertEquals(1, set.size());

      final Conteneur c3 = conteneurManager.findByIdManager(3);
      set = conteneurManager.getIncidentsManager(c3);
      assertEquals(0, set.size());

      final Conteneur newC = new Conteneur();
      set = conteneurManager.getIncidentsManager(newC);
      assertEquals(0, set.size());

      set = conteneurManager.getIncidentsManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode getPlateformesManager.
    */
   @Test
   public void testGetConteneurPlateformesManager(){

      final Conteneur c1 = conteneurManager.findByIdManager(1);
      Set<ConteneurPlateforme> set = conteneurManager.getConteneurPlateformesManager(c1);
      assertEquals(2, set.size());

      final Conteneur c2 = conteneurManager.findByIdManager(2);
      set = conteneurManager.getConteneurPlateformesManager(c2);
      assertEquals(1, set.size());

      final Conteneur newC = new Conteneur();
      set = conteneurManager.getConteneurPlateformesManager(newC);
      assertEquals(0, set.size());

      set = conteneurManager.getConteneurPlateformesManager(null);
      assertEquals(0, set.size());
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String code1 = "C999";
      final String code2 = "TTT";
      final String nom1 = "Congélateur 999";
      final String nom2 = "RAAAA";

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);
      banques.add(b2);

      final Conteneur c1 = new Conteneur();
      c1.setCode(code1);
      c1.setNom(nom1);
      c1.setPlateformeOrig(plateformeDao.findById(2));
      assertTrue(conteneurManager.findDoublonManager(c1, banques));

      c1.setCode(code2);
      assertFalse(conteneurManager.findDoublonManager(c1, banques));

      c1.setCode(code1);
      c1.setNom(nom2);
      assertTrue(conteneurManager.findDoublonManager(c1, banques));

      c1.setCode(code1);
      c1.setNom(nom1);
      banques = new ArrayList<>();
      banques.add(b1);
      assertFalse(conteneurManager.findDoublonManager(c1, banques));

      banques = new ArrayList<>();
      banques.add(b1);
      banques.add(b2);
      final Conteneur c2 = conteneurManager.findByIdManager(2);
      assertFalse(conteneurManager.findDoublonManager(c2, banques));

      c2.setCode(code1);
      c2.setNom(nom1);
      assertFalse(conteneurManager.findDoublonManager(c2, banques));

      c2.setPlateformeOrig(plateformeDao.findById(2));
      assertTrue(conteneurManager.findDoublonManager(c2, banques));

      assertFalse(conteneurManager.findDoublonManager(null, banques));
      assertFalse(conteneurManager.findDoublonManager(c2, null));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Conteneur c1 = conteneurManager.findByIdManager(1);
      assertTrue(conteneurManager.isUsedObjectManager(c1));

      final Conteneur c2 = conteneurManager.findByIdManager(2);
      assertFalse(conteneurManager.isUsedObjectManager(c2));

      final Conteneur newC = new Conteneur();
      assertFalse(conteneurManager.isUsedObjectManager(newC));

      assertFalse(conteneurManager.isUsedObjectManager(null));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      saveManagerTest();
      saveManagerTest();
      deleteByIdManagerTest();
   }

   /**
    * Teste la methode saveManager. 
    * @throws ParseException 
    */
   @Test
   public void saveManagerTest() throws ParseException{

      final ConteneurType type = conteneurTypeDao.findById(1);
      final Service serv = serviceDao.findById(1);
      final Banque bank1 = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(bank1);
      banks.add(bank2);
      final Utilisateur u = utilisateurDao.findById(1);

      final Conteneur c1 = new Conteneur();
      c1.setCode("CR1");

      Boolean catched = false;
      // on test l'insertion avec le service null
      try{
         conteneurManager.saveManager(c1, null, null, null, null, u, plateformeDao.findById(1));
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      catched = false;
      // on test l'insertion avec une pf Origine nulle
      try{
         conteneurManager.saveManager(c1, null, serv, null, null, u, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      // on test l'insertion d'un doublon
      c1.setCode("CC1");
      c1.setNom("Congélateur 1");
      try{
         conteneurManager.saveManager(c1, null, serv, banks, null, u, plateformeDao.findById(1));
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      // Test de la validation lors de la création
      c1.setNom("CR1");
      try{
         validationInsert(c1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      // On test une insertion valide avec les assos non obligatoires à null
      c1.setCode("CR");
      c1.setNom("Congel");
      c1.setTemp((float) -50.0);
      c1.setPiece("PIECE");
      c1.setNbrEnc(3);
      c1.setNbrNiv(3);
      c1.setDescription("DESC");
      c1.setArchive(false);
      conteneurManager.saveManager(c1, null, serv, null, null, u, plateformeDao.findById(1));
      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      assertEquals(1, getOperationManager().findByObjectManager(c1).size());
      final int id = c1.getConteneurId();
      // Vérification
      final Conteneur cTest = conteneurManager.findByIdManager(id);
      assertNotNull(cTest);
      assertNotNull(cTest.getService());
      assertNull(cTest.getConteneurType());
      assertEquals(3, (int) cTest.getNbrEnc());
      assertEquals(3, (int) cTest.getNbrNiv());
      assertEquals("Congel", cTest.getNom());
      assertEquals("CR", cTest.getCode());
      assertEquals("PIECE", cTest.getPiece());
      assertEquals("DESC", cTest.getDescription());
      assertEquals(0, conteneurManager.getBanquesManager(cTest).size());
      assertEquals(0, conteneurManager.getConteneurPlateformesManager(cTest).size());
      assertEquals(1, (int) cTest.getPlateformeOrig().getPlateformeId());

      // On test une insertion valide avec les assos non obligatoires
      final Conteneur c2 = new Conteneur();
      c2.setCode("CD");
      c2.setNom("TEST");
      final List<Plateforme> pfs = new ArrayList<>();
      pfs.add(pf1);
      pfs.add(pf2);
      conteneurManager.saveManager(c2, type, serv, banks, pfs, u, plateformeDao.findById(2));
      assertEquals(6, conteneurManager.findAllObjectsManager().size());
      assertEquals(1, getOperationManager().findByObjectManager(c2).size());
      final int id2 = c2.getConteneurId();
      // Vérification
      final Conteneur cTest2 = conteneurManager.findByIdManager(id2);
      assertNotNull(cTest2);
      assertNotNull(cTest2.getService());
      assertNotNull(cTest2.getConteneurType());
      assertEquals("CD", cTest2.getCode());
      assertEquals(2, conteneurManager.getBanquesManager(cTest2).size());
      assertEquals(2, conteneurManager.getConteneurPlateformesManager(cTest2).size());

      // Suppression
      conteneurManager.deleteByIdManager(cTest, null, u);
      conteneurManager.deleteByIdManager(cTest2, null, u);
      assertEquals(0, getOperationManager().findByObjectManager(cTest).size());
      assertEquals(0, getOperationManager().findByObjectManager(cTest2).size());
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest);
      fs.add(cTest2);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode saveManager. 
    * @throws ParseException 
    */
   @Test
   public void saveManagerTest() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final ConteneurType type = conteneurTypeDao.findById(1);
      final Service serv = serviceDao.findById(1);
      final Banque bank1 = banqueDao.findById(1);
      final Banque bank2 = banqueDao.findById(2);
      final Banque bank3 = banqueDao.findById(3);
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      List<Banque> banks = new ArrayList<>();
      banks.add(bank1);
      banks.add(bank2);

      final Conteneur c = new Conteneur();
      c.setCode("CR");
      c.setNom("TEST UP");
      conteneurManager.saveManager(c, null, serv, null, null, u, pf1);
      assertEquals(1, getOperationManager().findByObjectManager(c).size());
      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      final Integer id = c.getConteneurId();

      Boolean catched = false;
      final Conteneur cUp1 = conteneurManager.findByIdManager(id);
      // on test l'update avec le service null
      try{
         conteneurManager.saveManager(cUp1, null, null, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(5, conteneurManager.findAllObjectsManager().size());

      // on test l'update d'un doublon
      cUp1.setCode("CC1");
      cUp1.setNom("Congélateur 1");
      try{
         conteneurManager.saveManager(cUp1, null, serv, banks, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertEquals(5, conteneurManager.findAllObjectsManager().size());

      // Test de la validation lors de l'update
      cUp1.setNom("CR1");
      try{
         validationUpdate(cUp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertEquals(5, conteneurManager.findAllObjectsManager().size());

      // on teste un doublon sur les incidents
      cUp1.setCode("CR1");
      cUp1.setNom("Congel");
      cUp1.setTemp((float) -50.0);
      cUp1.setPiece("PIECE");
      cUp1.setNbrEnc(3);
      cUp1.setNbrNiv(3);
      cUp1.setDescription("DESC");
      cUp1.setArchive(false);
      List<Incident> incs = new ArrayList<>();
      final Incident inc1 = new Incident();
      inc1.setNom("TEST");
      final Incident inc2 = new Incident();
      inc2.setNom("TEST");
      incs.add(inc1);
      incs.add(inc2);
      catched = false;
      try{
         conteneurManager.saveManager(cUp1, null, serv, null, null, incs, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On test un update valide avec les assos non obligatoires à null
      incs = new ArrayList<>();
      final Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("08/11/2009");
      inc1.setDate(d1);
      incs.add(inc1);
      List<Plateforme> pfs = new ArrayList<>();
      pfs.add(pf1);
      pfs.add(pf2);
      conteneurManager.saveManager(cUp1, null, serv, banks, pfs, incs, u);
      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      assertEquals(2, getOperationManager().findByObjectManager(cUp1).size());
      // Vérification
      final Conteneur cTest = conteneurManager.findByIdManager(id);
      assertNotNull(cTest);
      assertNotNull(cTest.getService());
      assertNull(cTest.getConteneurType());
      assertEquals(3, (int) cTest.getNbrEnc());
      assertEquals(3, (int) cTest.getNbrNiv());
      assertEquals("Congel", cTest.getNom());
      assertEquals("CR1", cTest.getCode());
      assertEquals("PIECE", cTest.getPiece());
      assertEquals("DESC", cTest.getDescription());
      assertEquals(2, conteneurManager.getBanquesManager(cTest).size());
      assertEquals(2, conteneurManager.getConteneurPlateformesManager(cTest).size());
      assertEquals(1, conteneurManager.getIncidentsManager(cTest).size());

      // On test un update valide avec les assos non obligatoires
      final Conteneur cUp2 = conteneurManager.findByIdManager(id);
      cUp2.setCode("CD");
      banks = new ArrayList<>();
      banks.add(bank3);
      pfs = new ArrayList<>();
      conteneurManager.saveManager(cUp2, type, serv, banks, pfs, null, u);
      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      assertEquals(3, getOperationManager().findByObjectManager(cUp2).size());
      // Vérification
      final Conteneur cTest2 = conteneurManager.findByIdManager(id);
      assertNotNull(cTest2);
      assertNotNull(cTest2.getService());
      assertNotNull(cTest2.getConteneurType());
      assertEquals("CD", cTest2.getCode());
      assertEquals(1, conteneurManager.getBanquesManager(cTest2).size());
      assertEquals(0, conteneurManager.getConteneurPlateformesManager(cTest2).size());
      final Iterator<Banque> it = conteneurManager.getBanquesManager(cTest2).iterator();
      assertEquals("BANQUE3", it.next().getNom());

      // Suppression
      conteneurManager.deleteByIdManager(cTest2, null, u);
      assertEquals(0, getOperationManager().findByObjectManager(cTest2).size());
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(5, incidentManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest2);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la methode deleteByIdManager. 
    */
   @Test
   public void deleteByIdManagerTest(){
      final Utilisateur u = utilisateurDao.findById(1);
      // test de la suppression d'un objet null
      conteneurManager.deleteByIdManager(null, null, null);
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      // test de la suppression d'un objet utilisé
      final Conteneur c1 = conteneurManager.findByIdManager(1);
      boolean catched = false;
      try{
         conteneurManager.deleteByIdManager(c1, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertEquals(4, conteneurManager.findAllObjectsManager().size());

      // création d'un nouveau conteneur pour tester la délétion
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
      final List<Integer> positions = new ArrayList<>();
      positions.add(null);
      positions.add(null);
      positions.add(null);
      positions.add(null);
      try{
         conteneurManager.createAllArborescenceManager(c, enceintes, term, positions, banques, plateformes, null, false, u, pf1);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      assertEquals(33, enceinteManager.findAllObjectsManager().size());
      assertEquals(96, terminaleManager.findAllObjectsManager().size());

      final List<Enceinte> encs = enceinteManager.findAllEnceinteByConteneurManager(c);
      assertEquals(26, encs.size());

      final List<Terminale> terminales = new ArrayList<>();
      final Iterator<Enceinte> encIt = encs.iterator();
      while(encIt.hasNext()){
         terminales.addAll(terminaleManager.findByEnceinteWithOrderManager(encIt.next()));
      }
      assertEquals(90, terminales.size());

      // suppression du conteneur
      final Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
      conteneurManager.deleteByIdManager(cTest, null, u);
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   /**
    * Test la validation d'un conteneur lors de sa création.
    * @param conteneur Conteneur à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Conteneur conteneur) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Service serv = serviceDao.findById(1);
      boolean catchedInsert = false;

      // On teste une insertion avec un attribut code non valide
      String[] emptyValues = new String[] {null, "", "  ", "¤¤%$$*d", createOverLength(5)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            conteneur.setCode(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, u, plateformeDao.findById(1));
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      conteneur.setCode("CR");

      // On teste une insertion avec un attribut nom non valide
      emptyValues = new String[] {null, "", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            conteneur.setNom(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, u, plateformeDao.findById(1));
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      conteneur.setNom(null);

      // On teste une insertion avec un attribut piece non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤¤", createOverLength(10)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            conteneur.setPiece(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, u, plateformeDao.findById(1));
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      conteneur.setPiece(null);

      // On teste une insertion avec un attribut nbrNiv non valide
      catchedInsert = false;
      try{
         conteneur.setNbrNiv(-1);
         conteneurManager.saveManager(conteneur, null, serv, null, null, u, plateformeDao.findById(1));
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      conteneur.setNbrNiv(null);

      // On teste une insertion avec un attribut nbrEnc non valide
      catchedInsert = false;
      try{
         conteneur.setNbrEnc(-1);
         conteneurManager.saveManager(conteneur, null, serv, null, null, u, plateformeDao.findById(1));
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      assertTrue(catchedInsert);
      conteneur.setNbrEnc(null);

      // On teste une insertion avec un attribut description non valide
      emptyValues = new String[] {"", "  ", createOverLength(250)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            conteneur.setDescription(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, u, plateformeDao.findById(1));
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      conteneur.setDescription(null);
   }

   /**
    * Test la validation d'un conteneur lors de son update.
    * @param conteneur Conteneur à tester.
    * @throws ParseException 
    */
   private void validationUpdate(final Conteneur conteneur) throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Service serv = serviceDao.findById(1);
      boolean catched = false;

      // On teste un update avec un attribut code non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$*d¤¤", createOverLength(5)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            conteneur.setCode(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      conteneur.setCode("CR");

      // On teste un update avec un attribut nom non valide
      emptyValues = new String[] {null, "", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            conteneur.setNom(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      conteneur.setNom(null);

      // On teste un update avec un attribut piece non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤¤", createOverLength(10)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            conteneur.setPiece(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      conteneur.setPiece(null);

      // On teste un update avec un attribut nbrNiv non valide
      catched = false;
      try{
         conteneur.setNbrNiv(-1);
         conteneurManager.saveManager(conteneur, null, serv, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      conteneur.setNbrNiv(null);

      // On teste un update avec un attribut nbrEnc non valide
      catched = false;
      try{
         conteneur.setNbrEnc(-1);
         conteneurManager.saveManager(conteneur, null, serv, null, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      conteneur.setNbrEnc(null);

      // On teste un update avec un attribut description non valide
      emptyValues = new String[] {"", "  ", createOverLength(250)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            conteneur.setDescription(emptyValues[i]);
            conteneurManager.saveManager(conteneur, null, serv, null, null, null, u);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      conteneur.setDescription(null);
   }

   /**
    * Teste la methode createAllArborescenceManager. 
    * @throws ParseException 
    */
   @Test
   public void testCreateAllArborescenceManager() throws ParseException{
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

      // test avec des éléments nulls ou de mauvais positions
      try{
         conteneurManager.createAllArborescenceManager(null, enceintes, term, goodPositions, banques, plateformes, null, false, u,
            pf1);
         conteneurManager.createAllArborescenceManager(c, null, term, goodPositions, banques, plateformes, null, false, u, pf1);
         conteneurManager.createAllArborescenceManager(c, enceintes, null, goodPositions, banques, plateformes, null, false, u,
            pf1);
         conteneurManager.createAllArborescenceManager(c, enceintes, term, null, banques, plateformes, null, false, u, pf1);
         conteneurManager.createAllArborescenceManager(c, enceintes, term, badPositions, banques, plateformes, null, false, u,
            pf1);
      }catch(final Exception e){
         e.printStackTrace();
      }
      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      // test avec exception sur le conteneur
      c.setService(null);
      boolean catched = false;
      try{
         conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, plateformes, null, false, u,
            pf1);
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
      c.setService(serv);

      // test avec excpetion sur la derniere enceinte
      e3.setEnceinteType(null);
      try{
         conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, plateformes, null, false, u,
            pf1);
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
         conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, plateformes, null, false, u,
            pf1);
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
      term.setTerminaleType(tType);

      try{
         conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, plateformes, null, false, u,
            pf1);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      assertEquals(33, enceinteManager.findAllObjectsManager().size());
      assertEquals(96, terminaleManager.findAllObjectsManager().size());

      Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
      assertEquals("C2", cTest.getCode());
      assertEquals(2, conteneurManager.getEnceintesManager(cTest).size());

      final Enceinte eTest1 = conteneurManager.getEnceintesManager(cTest).iterator().next();
      assertEquals("R11", eTest1.getNom());
      assertEquals(3, enceinteManager.getEnceintesManager(eTest1).size());

      final Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1).iterator().next();
      assertEquals("T1", eTest2.getNom());
      assertEquals(3, enceinteManager.getEnceintesManager(eTest2).size());

      final Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2).iterator().next();
      assertEquals("C25", eTest3.getNom());
      assertEquals(0, enceinteManager.getEnceintesManager(eTest3).size());
      assertEquals(5, enceinteManager.getTerminalesManager(eTest3).size());

      final Terminale tTest = enceinteManager.getTerminalesManager(eTest3).iterator().next();
      assertEquals("BT1", tTest.getNom());

      // ajoute emplacement pour casser cascade
      final Emplacement emp = new Emplacement();
      emp.setPosition(55);
      emp.setObjetId(1);
      emp.setAdrl("ADRL");
      emplacementManager.saveManager(emp, terminaleManager.findByEnceinteWithOrderManager(eTest3).get(4),
         entiteDao.findById(3));
      assertEquals(8, emplacementManager.findAllObjectsManager().size());
      assertFalse(emp.getVide());

      catched = false;
      try{
         conteneurManager.deleteByIdManager(cTest, null, u);
      }catch(final ObjectUsedException oe){
         catched = true;
         assertEquals("conteneur.deletion.isUsed", oe.getKey());
         assertFalse(oe.isCascadable());
      }
      assertTrue(catched);

      final List<Enceinte> encs = enceinteManager.findAllEnceinteByConteneurManager(c);
      assertEquals(26, encs.size());
      final List<Terminale> terminales = new ArrayList<>();
      final Iterator<Enceinte> encIt = encs.iterator();
      while(encIt.hasNext()){
         terminales.addAll(terminaleManager.findByEnceinteWithOrderManager(encIt.next()));
      }
      assertEquals(90, terminales.size());

      // si emplacement vide mais associé à évènement de stockage -> archivage
      emp.setObjetId(null);
      emp.setEntite(null);
      emplacementManager.saveManager(emp, emp.getTerminale(), null);
      assertTrue(emp.getVide());

      final Retour r = new Retour();
      /*Champs obligatoires*/
      final Echantillon ec2 = echantillonDao.findById(2);
      r.setTempMoyenne(new Float(22.0));
      r.setDateSortie(Calendar.getInstance());

      retourManager.createOrsaveManager(r, ec2, emp, null, null, null, null, u, "creation");

      assertEquals(1, retourManager.getRetoursForObjectManager(ec2).size());

      conteneurManager.deleteByIdManager(cTest, null, u);
      // vérifie archivage du seul conteneur
      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      cTest = conteneurManager.findByIdManager(c.getConteneurId());
      assertTrue(cTest.getArchive());
      assertTrue(conteneurManager.getEnceintesManager(cTest).isEmpty());
      assertTrue(conteneurManager.getAllTerminalesInArborescenceManager(cTest).isEmpty());

      //suppr retour pour permettre cascade
      retourManager.deleteByIdManager(retourManager.getRetoursForObjectManager(ec2).get(0));
      assertTrue(retourManager.getRetoursForObjectManager(ec2).isEmpty());

      conteneurManager.deleteByIdManager(cTest, null, u);

      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);

   }

   @Test
   public void testCreateConteneurAPaillette(){
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

      try{
         conteneurManager.createAllArborescenceManager(c, enceintes, term, goodPositions, banques, plateformes, 16, true, u, pf1);
      }catch(final Exception e){
         e.printStackTrace();
      }

      assertEquals(5, conteneurManager.findAllObjectsManager().size());
      assertEquals(33, enceinteManager.findAllObjectsManager().size());
      assertEquals(96, terminaleManager.findAllObjectsManager().size());

      final Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
      assertEquals("C2", cTest.getCode());
      assertEquals(2, conteneurManager.getEnceintesManager(cTest).size());

      final Enceinte eTest1 = conteneurManager.getEnceintesManager(cTest).iterator().next();
      assertEquals("R11", eTest1.getNom());
      assertEquals(3, enceinteManager.getEnceintesManager(eTest1).size());

      final Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1).iterator().next();
      assertEquals("T1", eTest2.getNom());
      assertEquals(3, enceinteManager.getEnceintesManager(eTest2).size());

      final Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2).iterator().next();
      assertEquals("C25", eTest3.getNom());
      assertEquals(0, enceinteManager.getEnceintesManager(eTest3).size());
      assertEquals(5, enceinteManager.getTerminalesManager(eTest3).size());

      final Terminale tTest = enceinteManager.getTerminalesManager(eTest3).iterator().next();
      assertEquals("TRANSPARENT1", tTest.getNom());

      final List<Enceinte> encs = enceinteManager.findAllEnceinteByConteneurManager(c);
      assertEquals(26, encs.size());

      final List<Terminale> terminales = new ArrayList<>();
      for(Enceinte enc : encs){
         terminales.addAll(terminaleManager.findByEnceinteWithOrderManager(enc));
      }
      assertEquals(90, terminales.size());

      conteneurManager.deleteByIdManager(cTest, null, u);

      assertEquals(4, conteneurManager.findAllObjectsManager().size());
      assertEquals(7, enceinteManager.findAllObjectsManager().size());
      assertEquals(6, terminaleManager.findAllObjectsManager().size());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cTest);
      fs.addAll(encs);
      fs.addAll(terminales);
      cleanUpFantomes(fs);
   }

   @Test
   public void testGetContainingEnceinteManager(){
      Conteneur c = conteneurManager.findByIdManager(1);
      List<Enceinte> enceintes;
      enceintes = conteneurManager.getContainingEnceinteManager(c);
      assertEquals(7, enceintes.size());

      c = null;
      enceintes = conteneurManager.getContainingEnceinteManager(c);
      assertTrue(enceintes.isEmpty());
   }

   @Test
   public void testRemoveBanqueFromConteneurAndEnceintes(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Conteneur c = conteneurManager.findByIdManager(1);
      final Set<Banque> banksC = conteneurManager.getBanquesManager(c);
      assertEquals(4, banksC.size());
      final Enceinte e5 = enceinteManager.findByIdManager(5);
      final Set<Banque> banske5 = enceinteManager.getBanquesManager(e5);
      assertEquals(2, banske5.size());
      final Enceinte e7 = enceinteManager.findByIdManager(7);
      final Set<Banque> banske7 = enceinteManager.getBanquesManager(e7);
      assertEquals(1, banske7.size());
      final Banque b = banqueDao.findById(2);

      conteneurManager.removeBanqueFromContAndEncManager(c, b);
      assertEquals(3, conteneurManager.getBanquesManager(c).size());
      assertEquals(1, enceinteManager.getBanquesManager(e5).size());
      assertEquals(0, enceinteManager.getBanquesManager(e7).size());

      // remise en place 
      conteneurManager.saveManager(c, c.getConteneurType(), c.getService(), new ArrayList<>(banksC), null, null, u);
      enceinteManager.saveManager(e5, e5.getEnceinteType(), e5.getConteneur(), e5.getEnceintePere(), e5.getEntite(),
         new ArrayList<>(banske5), null, null, u, null);
      enceinteManager.saveManager(e7, e7.getEnceinteType(), e7.getConteneur(), e7.getEnceintePere(), e7.getEntite(),
         new ArrayList<>(banske7), null, null, u, null);

      assertEquals(4, conteneurManager.getBanquesManager(c).size());
      assertEquals(2, enceinteManager.getBanquesManager(e5).size());
      assertEquals(1, enceinteManager.getBanquesManager(e7).size());
      List<Operation> ops = getOperationManager().findByObjectManager(c);
      for(Operation op2 : ops){
         getOperationManager().deleteByIdManager(op2);
      }
      ops = getOperationManager().findByObjectManager(e5);
      for(Operation op1 : ops){
         getOperationManager().deleteByIdManager(op1);
      }
      ops = getOperationManager().findByObjectManager(e7);
      for(Operation op : ops){
         getOperationManager().deleteByIdManager(op);
      }
      assertEquals(19, getOperationManager().findAllObjectsManager().size());
   }

   @Test
   public void testFindConteneurFromEmplacementManager(){
      final Emplacement e1 = emplacementManager.findByIdManager(1);
      assertEquals(conteneurManager.findFromEmplacementManager(e1), conteneurManager.findByIdManager(1));
      final Emplacement e6 = emplacementManager.findByIdManager(6);
      assertEquals(conteneurManager.findFromEmplacementManager(e6), conteneurManager.findByIdManager(1));
      assertNull(conteneurManager.findFromEmplacementManager(null));
   }

   @Test
   public void testFindByPartageManager(){
      List<Conteneur> conts = conteneurManager.findByPartageManager(plateformeDao.findById(1), true);
      assertEquals(1, conts.size());
      conts = conteneurManager.findByPartageManager(null, true);
      assertTrue(conts.isEmpty());
      conts = conteneurManager.findByPartageManager(plateformeDao.findById(1), null);
      assertTrue(conts.isEmpty());
   }

   @Test
   public void testFindTempForEmplacementIdManager(){
      assertEquals(conteneurManager.findTempForEmplacementManager(emplacementManager.findByIdManager(2)), new Float(-75.0));
      assertEquals(conteneurManager.findTempForEmplacementManager(emplacementManager.findByIdManager(5)), new Float(-75.0));
      assertNull(conteneurManager.findTempForEmplacementManager(emplacementManager.findByIdManager(11)));
      assertNull(conteneurManager.findTempForEmplacementManager(null));
   }
}
