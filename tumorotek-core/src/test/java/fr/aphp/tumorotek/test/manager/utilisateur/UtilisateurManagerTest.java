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
package fr.aphp.tumorotek.test.manager.utilisateur;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilUtilisateurManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager UtilisateurManager.
 * Classe créée le 04/11/09.
 *
 * @author Pierre Ventadour.
 * @version 2.2.1
 *
 */
public class UtilisateurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private UtilisateurManager utilisateurManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private ProfilDao profilDao;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private ProfilUtilisateurManager profilUtilisateurManager;
   @Autowired
   private PlateformeManager plateformeManager;
   @Autowired
   private OperationManager operationManager;
   @Autowired
   private CodeSelectManager codeSelectManager;
   @Autowired
   private CodeUtilisateurManager codeUtilisateurManager;
   @Autowired
   private OperationTypeDao operationTypeDao;

   /** Constructeur par défaut. */
   public UtilisateurManagerTest(){}

   @Test
   public void testFindById(){
      final Utilisateur utilisateur = utilisateurManager.findByIdManager(1);
      assertNotNull(utilisateur);

      final Utilisateur utilisateurNull = utilisateurManager.findByIdManager(10);
      assertNull(utilisateurNull);
   }

   /**
    * Test la méthode findByLoginManager.
    */
   @Test
   public void testFindByLoginManager(){
      List<Utilisateur> list = utilisateurManager.findByLoginManager("USER1");
      assertTrue(list.size() == 1);

      list = utilisateurManager.findByLoginManager("USER2");
      assertTrue(list.size() == 1);

      list = utilisateurManager.findByLoginManager("USER");
      assertTrue(list.size() == 0);

      list = utilisateurManager.findByLoginManager("");
      assertTrue(list.size() == 0);

      list = utilisateurManager.findByLoginManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByLoginAndArchvieManager.
    */
   @Test
   public void testFindByLoginAndArchvieManager(){
      final List<Plateforme> pfs = plateformeManager.findAllObjectsManager();
      List<Utilisateur> list = utilisateurManager.findByLoginAndArchiveManager("USER1", false, pfs);
      assertTrue(list.size() == 1);

      list = utilisateurManager.findByLoginAndArchiveManager("USER1", true, pfs);
      assertTrue(list.size() == 0);

      list = utilisateurManager.findByLoginAndArchiveManager("USER", true, pfs);
      assertTrue(list.size() == 0);

      list = utilisateurManager.findByLoginAndArchiveManager("", false, pfs);
      assertTrue(list.size() == 0);

      list = utilisateurManager.findByLoginAndArchiveManager(null, false, pfs);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByArchiveManager.
    */
   @Test
   public void testFindByArchiveManager(){
      final List<Plateforme> pfs = plateformeManager.findAllObjectsManager();

      List<Utilisateur> list;
      boolean listContainsSuperAdmin;

      //Test excluant les super-administrateurs

      ////Test recherche utilisateurs archivés
      list = utilisateurManager.findByArchiveManager(true, pfs, false);
      listContainsSuperAdmin = list.stream().anyMatch(Utilisateur::isSuperAdmin);
      assertTrue(list.size() == 1 && !listContainsSuperAdmin);

      ////Test recherche utilisateurs non-archivés
      list = utilisateurManager.findByArchiveManager(false, pfs, false);
      listContainsSuperAdmin = list.stream().anyMatch(Utilisateur::isSuperAdmin);
      assertTrue(list.size() == 3 && !listContainsSuperAdmin);

      //Test incluant les super-administrateurs

      ////Test recherche utilisateurs archivés
      list = utilisateurManager.findByArchiveManager(true, pfs, true);
      assertTrue(list.size() == 1);

      ////Test recherche utilisateurs non-archivés
      list = utilisateurManager.findByArchiveManager(false, pfs, true);
      assertTrue(list.size() == 4);

   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Utilisateur> list = utilisateurManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   /**
    * Test la méthode getPlateformes.
    */
   @Test
   public void testGetPlateformes(){
      final Utilisateur u1 = utilisateurManager.findByIdManager(1);
      Set<Plateforme> pfs = utilisateurManager.getPlateformesManager(u1);
      assertTrue(pfs.size() == 0);

      final Utilisateur u5 = utilisateurManager.findByIdManager(5);
      pfs = utilisateurManager.getPlateformesManager(u5);
      assertTrue(pfs.size() == 1);

      pfs = utilisateurManager.getPlateformesManager(null);
      assertTrue(pfs.size() == 0);
   }

   /**
    * Test la méthode getAvailableBanquesManager.
    * @version 2.1
    */
   @Test
   public void testGetAvailableBanquesManager(){
      final Utilisateur u1 = utilisateurManager.findByIdManager(1);
      List<Banque> bks = utilisateurManager.getAvailableBanquesManager(u1, false);
      // @since 2.1 Banque2 archive
      assertTrue(bks.size() == 2);
      // @since 2.2.1 keepArchived
      bks = utilisateurManager.getAvailableBanquesManager(u1, true);
      assertTrue(bks.size() == 3);

      final Utilisateur u2 = utilisateurManager.findByIdManager(2);
      bks = utilisateurManager.getAvailableBanquesManager(u2, false);
      // @since 2.1 Banque2 archive
      assertTrue(bks.size() == 2);
      // @since 2.2.1 keepArchived
      bks = utilisateurManager.getAvailableBanquesManager(u2, true);
      assertTrue(bks.size() == 3);

      final Utilisateur u4 = utilisateurManager.findByIdManager(4);
      bks = utilisateurManager.getAvailableBanquesManager(u4, false);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesManager(u4, true);
      assertTrue(bks.size() == 0);

      final Utilisateur u5 = utilisateurManager.findByIdManager(5);
      bks = utilisateurManager.getAvailableBanquesManager(u5, false);
      // @since 2.1 Banque2 archive
      assertTrue(bks.size() == 3);
      // @since 2.2.1 keepArchived
      bks = utilisateurManager.getAvailableBanquesManager(u5, true);
      assertTrue(bks.size() == 4);

      bks = utilisateurManager.getAvailableBanquesManager(null, true);
      assertTrue(bks.size() == 0);
   }

   /**
    * Test la méthode getAvailableBanquesAsAdminManager.
    * @version 2.1
    */
   @Test
   public void testGetAvailableBanquesAsAdminManager(){
      final Utilisateur u1 = utilisateurManager.findByIdManager(1);
      List<Banque> bks = utilisateurManager.getAvailableBanquesAsAdminManager(u1);
      assertTrue(bks.size() == 2);

      final Utilisateur u2 = utilisateurManager.findByIdManager(2);
      bks = utilisateurManager.getAvailableBanquesAsAdminManager(u2);
      assertTrue(bks.size() == 0);

      final Utilisateur u4 = utilisateurManager.findByIdManager(4);
      bks = utilisateurManager.getAvailableBanquesAsAdminManager(u4);
      assertTrue(bks.size() == 0);

      final Utilisateur u5 = utilisateurManager.findByIdManager(5);
      bks = utilisateurManager.getAvailableBanquesAsAdminManager(u5);
      // @since 2.1 retire banque2 archive
      assertTrue(bks.size() == 3);

      bks = utilisateurManager.getAvailableBanquesAsAdminManager(null);
      assertTrue(bks.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "USER1";
      final String nom2 = "USER";

      final Utilisateur u1 = new Utilisateur();
      u1.setLogin(nom1);
      assertTrue(utilisateurManager.findDoublonManager(u1));

      u1.setLogin(nom2);
      assertFalse(utilisateurManager.findDoublonManager(u1));

      final Utilisateur u2 = utilisateurManager.findByIdManager(2);
      assertFalse(utilisateurManager.findDoublonManager(u2));

      u2.setLogin(nom1);
      assertTrue(utilisateurManager.findDoublonManager(u2));

      assertFalse(utilisateurManager.findDoublonManager(null));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Utilisateur u1 = utilisateurManager.findByIdManager(1);
      assertTrue(utilisateurManager.isUsedObjectManager(u1));

      final Utilisateur u3 = utilisateurManager.findByIdManager(3);
      assertFalse(utilisateurManager.isUsedObjectManager(u3));

      assertFalse(utilisateurManager.isUsedObjectManager(new Utilisateur()));
      assertFalse(utilisateurManager.isUsedObjectManager(null));
   }

   @Test
   public void testFindByLoginPasswordAndArchiveManager(){
      List<Utilisateur> liste =
         utilisateurManager.findByLoginPasswordAndArchiveManager("USER5", "b383bb08bd750d8ef04d034ad648a208", false);
      assertTrue(liste.size() == 1);

      liste = utilisateurManager.findByLoginPasswordAndArchiveManager("USER6", "b383bb08bd750d8ef04d034ad648a208", false);
      assertTrue(liste.size() == 0);

      liste = utilisateurManager.findByLoginPasswordAndArchiveManager("USER5", "b383bb08bd750d8ef04d034ad64dvs8a208", false);
      assertTrue(liste.size() == 0);

      liste = utilisateurManager.findByLoginPasswordAndArchiveManager("USER5", "b383bb08bd750d8ef04d034ad648a208", true);
      assertTrue(liste.size() == 0);

      liste = utilisateurManager.findByLoginPasswordAndArchiveManager(null, "b383bb08bd750d8ef04d034ad648a208", false);
      assertTrue(liste.size() == 0);

      liste = utilisateurManager.findByLoginPasswordAndArchiveManager("USER5", null, false);
      assertTrue(liste.size() == 0);

      liste = utilisateurManager.findByLoginPasswordAndArchiveManager(null, null, false);
      assertTrue(liste.size() == 0);
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

      final Utilisateur admin = utilisateurManager.findByIdManager(1);
      final Utilisateur u1 = new Utilisateur();
      final Profil p1 = profilDao.findById(1);
      final Profil p2 = profilDao.findById(2);
      final Banque b1 = banqueDao.findById(1);
      final Collaborateur c = collaborateurDao.findById(1);
      List<ProfilUtilisateur> profils = new ArrayList<>();
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);
      final List<Plateforme> plateformes = new ArrayList<>();

      boolean catched = false;
      // on test l'insertion d'un doublon
      u1.setLogin("USER1");
      u1.setPassword("PASS");
      try{
         utilisateurManager.saveManager(u1, null, null, null, admin, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);

      // Test de la validation lors de la création
      u1.setLogin("PIERRE");
      try{
         validationInsert(u1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);

      // on teste la validation des ProfilUtilisateurs
      final ProfilUtilisateur pu1 = new ProfilUtilisateur();
      pu1.setBanque(b1);
      pu1.setProfil(p1);
      final ProfilUtilisateur pu2 = new ProfilUtilisateur();
      pu2.setBanque(b1);
      pu2.setProfil(null);
      profils.add(pu1);
      profils.add(pu2);
      catched = false;
      try{
         utilisateurManager.saveManager(u1, null, profils, null, admin, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);

      // insertion valide avec les assos à null
      u1.setLogin("PIERRE");
      utilisateurManager.saveManager(u1, null, null, null, admin, plateformeManager.findByIdManager(1));
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      final Integer idU1 = u1.getUtilisateurId();
      assertTrue(operationManager.findByObjectManager(u1).size() == 1);

      final Utilisateur uTest = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest);
      assertTrue(uTest.getLogin().equals("PIERRE"));
      assertNull(uTest.getCollaborateur());
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest, null).size() == 0);
      assertTrue(utilisateurManager.getPlateformesManager(uTest).size() == 0);

      // insertion valide avec les assos
      pu2.setProfil(p2);
      profils = new ArrayList<>();
      profils.add(pu1);
      profils.add(pu2);
      plateformes.add(pf1);
      plateformes.add(pf2);
      final Utilisateur u2 = new Utilisateur();
      u2.setLogin("MATHIEU");
      u2.setPassword("PASS MAT");
      utilisateurManager.saveManager(u2, c, profils, plateformes, admin, null);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 6);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 10);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      final Integer idU2 = u2.getUtilisateurId();
      assertTrue(operationManager.findByObjectManager(u2).size() == 1);

      final Utilisateur uTest2 = utilisateurManager.findByIdManager(idU2);
      assertNotNull(uTest2);
      assertNotNull(uTest2.getCollaborateur());
      assertTrue(uTest2.getLogin().equals("MATHIEU"));
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest2, null).size() == 2);
      assertTrue(utilisateurManager.getPlateformesManager(uTest2).size() == 2);

      // suppression des utilisateurs
      utilisateurManager.removeObjectManager(uTest);
      utilisateurManager.removeObjectManager(uTest2);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(operationManager.findByObjectManager(uTest).size() == 0);
      assertTrue(operationManager.findByObjectManager(uTest2).size() == 0);

   }

   private void saveManagerTest() throws ParseException{

      final Utilisateur admin = utilisateurManager.findByIdManager(1);
      final Utilisateur u = new Utilisateur();
      final Profil p1 = profilDao.findById(1);
      final Profil p2 = profilDao.findById(2);
      final Banque b1 = banqueDao.findById(1);
      final Collaborateur c = collaborateurDao.findById(1);
      List<ProfilUtilisateur> profils = new ArrayList<>();
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);
      List<Plateforme> plateformes = new ArrayList<>();
      final OperationType oType = operationTypeDao.findById(5);

      u.setLogin("MAXIME");
      u.setPassword("PASS");
      final ProfilUtilisateur pu1 = new ProfilUtilisateur();
      pu1.setBanque(b1);
      pu1.setProfil(p1);
      profils.add(pu1);
      plateformes.add(pf1);
      utilisateurManager.saveManager(u, null, profils, plateformes, admin, plateformeManager.findByIdManager(1));
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 9);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      final Integer idUp1 = u.getUtilisateurId();

      final Utilisateur uUp1 = utilisateurManager.findByIdManager(idUp1);
      boolean catched = false;
      // on test l'update d'un doublon
      uUp1.setLogin("USER1");
      try{
         utilisateurManager.saveManager(uUp1, null, null, null, admin, oType);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);

      // Test de la validation lors de la création
      uUp1.setLogin("MAXIME");
      try{
         validationUpdate(uUp1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);

      // on teste la validation des ProfilUtilisateurs
      final ProfilUtilisateur pu2 = new ProfilUtilisateur();
      pu2.setBanque(b1);
      pu2.setProfil(null);
      profils = new ArrayList<>();
      profils.add(pu1);
      profils.add(pu2);
      catched = false;
      try{
         utilisateurManager.saveManager(uUp1, null, profils, null, admin, oType);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 9);

      // insertion valide avec de nouvelles assos
      uUp1.setLogin("HEYO");
      pu2.setProfil(p2);
      profils = new ArrayList<>();
      profils.add(pu2);
      plateformes = new ArrayList<>();
      plateformes.add(pf2);
      utilisateurManager.saveManager(uUp1, null, profils, plateformes, admin, oType);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 9);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      assertTrue(operationManager.findByObjectManager(uUp1).size() == 2);

      final Utilisateur uTest = utilisateurManager.findByIdManager(idUp1);
      assertNotNull(uTest);
      assertTrue(uTest.getLogin().equals("HEYO"));
      assertNull(uTest.getCollaborateur());
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest, null).size() == 1);
      assertTrue(utilisateurManager.getPlateformesManager(uTest).size() == 1);
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest, null).get(0).equals(pu2));
      final Iterator<Plateforme> it = utilisateurManager.getPlateformesManager(uTest).iterator();
      assertTrue(it.next().equals(pf2));

      // insertion valide avec les assos vides
      profils = new ArrayList<>();
      plateformes = new ArrayList<>();
      uTest.setLogin("MATHIEU");
      uTest.setPassword("PASS MAT");
      utilisateurManager.saveManager(uTest, c, profils, plateformes, admin, oType);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      assertTrue(operationManager.findByObjectManager(uTest).size() == 3);

      final Utilisateur uTest2 = utilisateurManager.findByIdManager(idUp1);
      assertNotNull(uTest2);
      assertNotNull(uTest2.getCollaborateur());
      assertTrue(uTest2.getLogin().equals("MATHIEU"));
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest2, null).size() == 0);
      assertTrue(utilisateurManager.getPlateformesManager(uTest2).size() == 0);

      // suppression des utilisateurs
      utilisateurManager.removeObjectManager(uTest2);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(operationManager.findByObjectManager(uTest2).size() == 0);

   }

   @Test
   public void testUpdatePasswordManager(){
      final Utilisateur admin = utilisateurManager.findByIdManager(5);
      final Utilisateur u1 = new Utilisateur();
      final Profil p1 = profilDao.findById(1);
      final Profil p2 = profilDao.findById(2);
      final Banque b1 = banqueDao.findById(1);
      final Collaborateur c = collaborateurDao.findById(1);
      List<ProfilUtilisateur> profils = new ArrayList<>();
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);
      final List<Plateforme> plateformes = new ArrayList<>();

      // insertion valide avec les assos
      final ProfilUtilisateur pu1 = new ProfilUtilisateur();
      pu1.setBanque(b1);
      pu1.setProfil(p1);
      final ProfilUtilisateur pu2 = new ProfilUtilisateur();
      pu2.setBanque(b1);
      pu2.setProfil(p2);
      profils = new ArrayList<>();
      profils.add(pu1);
      profils.add(pu2);
      plateformes.add(pf1);
      plateformes.add(pf2);
      u1.setLogin("MATHIEU");
      u1.setPassword("PASS MAT");
      final Calendar cal = Calendar.getInstance();
      u1.setTimeOut(cal.getTime());
      utilisateurManager.saveManager(u1, c, profils, plateformes, admin, plateformeManager.findByIdManager(2));
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 10);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      final Integer idU1 = u1.getUtilisateurId();
      assertTrue(operationManager.findByObjectManager(u1).size() == 1);

      final Utilisateur uTest = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest);
      assertNotNull(uTest.getCollaborateur());
      assertTrue(uTest.getLogin().equals("MATHIEU"));
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest, null).size() == 2);
      assertTrue(utilisateurManager.getPlateformesManager(uTest).size() == 2);

      // modif mot de passe avec même pwd
      utilisateurManager.updatePasswordManager(uTest, "PASS MAT", 7, admin);
      assertTrue(operationManager.findByObjectManager(uTest).size() == 1);

      // modif mot de passe avec pwd null
      final Utilisateur uTest3 = utilisateurManager.updatePasswordManager(uTest, null, 7, admin);
      assertTrue(operationManager.findByObjectManager(uTest).size() == 1);

      // modif valide avec Timeout
      utilisateurManager.updatePasswordManager(uTest, "NEW PASS", 7, admin);
      final Utilisateur uTest2 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest2);
      assertNotNull(uTest2.getCollaborateur());
      assertTrue(uTest2.getLogin().equals("MATHIEU"));
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest2, null).size() == 2);
      assertTrue(utilisateurManager.getPlateformesManager(uTest2).size() == 2);
      assertTrue(uTest2.getPassword().equals("NEW PASS"));
      assertTrue(operationManager.findByObjectManager(uTest2).size() == 2);
      assertTrue(uTest2.equals(uTest3));
      assertTrue(uTest2.getPassword().equals(uTest3.getPassword()));
      final Calendar calTest = Calendar.getInstance();
      calTest.add(Calendar.MONTH, 7);
      final Calendar calTest2 = Calendar.getInstance();
      calTest2.setTime(uTest2.getTimeOut());
      assertTrue(calTest.get(Calendar.YEAR) == calTest2.get(Calendar.YEAR));
      assertTrue(calTest.get(Calendar.MONTH) == calTest2.get(Calendar.MONTH));
      assertTrue(calTest.get(Calendar.DAY_OF_MONTH) == calTest2.get(Calendar.DAY_OF_MONTH));

      // modif valide sans mois
      calTest.setTime(uTest2.getTimeOut());
      utilisateurManager.updatePasswordManager(uTest2, "NEW PASS2", null, admin);
      final Utilisateur uTest4 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest4);
      calTest2.setTime(uTest4.getTimeOut());
      assertTrue(calTest.equals(calTest2));

      // modif valide avec mois à 0
      calTest.setTime(uTest4.getTimeOut());
      utilisateurManager.updatePasswordManager(uTest4, "NEW PASS3", 0, admin);
      final Utilisateur uTest5 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest5);
      calTest2.setTime(uTest5.getTimeOut());
      assertTrue(calTest.equals(calTest2));

      // modif valide sans Timeout
      uTest5.setTimeOut(null);
      utilisateurManager.updatePasswordManager(uTest5, "NEW PASS5", 7, admin);
      final Utilisateur uTest6 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest6);
      assertNull(uTest6.getTimeOut());

      // suppression de l'utilisateur
      utilisateurManager.removeObjectManager(uTest2);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(operationManager.findByObjectManager(uTest2).size() == 0);
   }

   @Test
   public void testArchivageUtilisateurManager(){
      final Utilisateur admin = utilisateurManager.findByIdManager(5);
      final Utilisateur u1 = new Utilisateur();
      final Profil p1 = profilDao.findById(1);
      final Profil p2 = profilDao.findById(2);
      final Banque b1 = banqueDao.findById(1);
      final Collaborateur c = collaborateurDao.findById(1);
      List<ProfilUtilisateur> profils = new ArrayList<>();
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);
      final List<Plateforme> plateformes = new ArrayList<>();

      // insertion valide avec les assos
      final ProfilUtilisateur pu1 = new ProfilUtilisateur();
      pu1.setBanque(b1);
      pu1.setProfil(p1);
      final ProfilUtilisateur pu2 = new ProfilUtilisateur();
      pu2.setBanque(b1);
      pu2.setProfil(p2);
      profils = new ArrayList<>();
      profils.add(pu1);
      profils.add(pu2);
      plateformes.add(pf1);
      plateformes.add(pf2);
      u1.setLogin("MATHIEU");
      u1.setPassword("PASS MAT");
      final Calendar cal = Calendar.getInstance();
      u1.setTimeOut(cal.getTime());
      u1.setArchive(false);
      utilisateurManager.saveManager(u1, c, profils, plateformes, admin, plateformeManager.findByIdManager(2));
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 10);
      assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
      final Integer idU1 = u1.getUtilisateurId();
      assertTrue(operationManager.findByObjectManager(u1).size() == 1);

      final Utilisateur uTest = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest);
      assertNotNull(uTest.getCollaborateur());
      assertTrue(uTest.getLogin().equals("MATHIEU"));
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest, null).size() == 2);
      assertTrue(utilisateurManager.getPlateformesManager(uTest).size() == 2);

      // archivage non valide
      utilisateurManager.archiveUtilisateurManager(uTest, null);
      final Utilisateur uTest2 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest2);
      assertFalse(uTest2.isArchive());
      utilisateurManager.archiveUtilisateurManager(null, admin);
      final Utilisateur uTest3 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest3);
      assertFalse(uTest3.isArchive());

      // archivage valide
      utilisateurManager.archiveUtilisateurManager(uTest, admin);
      final Utilisateur uTest4 = utilisateurManager.findByIdManager(idU1);
      assertNotNull(uTest4);
      assertNotNull(uTest4.getCollaborateur());
      assertTrue(uTest4.getLogin().equals("MATHIEU"));
      assertTrue(profilUtilisateurManager.findByUtilisateurManager(uTest4, null).size() == 2);
      assertTrue(utilisateurManager.getPlateformesManager(uTest4).size() == 2);
      assertTrue(uTest4.getPassword().equals("PASS MAT"));
      assertTrue(uTest4.isArchive());
      assertTrue(operationManager.findByObjectManager(uTest4).size() == 2);
      assertTrue(operationManager.findByObjectManager(uTest4).get(1).getOperationType().getNom().equals("Archivage"));

      // suppression de l'utilisateur
      utilisateurManager.removeObjectManager(uTest4);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
      assertTrue(profilUtilisateurManager.findAllObjectsManager().size() == 8);
      assertTrue(operationManager.findByObjectManager(uTest4).size() == 0);
   }

   /**
    * Test la validation d'un utilisateur lors de sa création.
    * @param utilisateur à tester.
    * @throws ParseException
    */
   private void validationInsert(final Utilisateur utilisateur) throws ParseException{

      boolean catchedInsert = false;

      final Utilisateur admin = utilisateurManager.findByIdManager(1);
      // On teste une insertion avec un attribut admin non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$*gd¤¤", createOverLength(100)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            utilisateur.setLogin(emptyValues[i]);
            utilisateurManager.saveManager(utilisateur, null, null, null, admin, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      utilisateur.setLogin("PIERRE");

      // On teste une insertion avec un attribut password non valide
      final String[] emptyValues2 = new String[] {null, "", "  ", createOverLength(100)};
      for(int i = 0; i < emptyValues2.length; i++){
         catchedInsert = false;
         try{
            utilisateur.setPassword(emptyValues2[i]);
            utilisateurManager.saveManager(utilisateur, null, null, null, admin, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      utilisateur.setPassword("PASS");

      // On teste une insertion avec un attribut email non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            utilisateur.setEmail(emptyValues[i]);
            utilisateurManager.saveManager(utilisateur, null, null, null, admin, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      utilisateur.setEmail(null);

   }

   /**
    * Test la validation d'un utilisateur lors de son update.
    * @param utilisateur à tester.
    * @throws ParseException
    */
   private void validationUpdate(final Utilisateur utilisateur) throws ParseException{
      final OperationType oType = operationTypeDao.findById(5);
      boolean catched = false;
      final Utilisateur admin = utilisateurManager.findByIdManager(1);
      // On teste une insertion avec un attribut admin non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$*gd¤¤", createOverLength(100)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            utilisateur.setLogin(emptyValues[i]);
            utilisateurManager.saveManager(utilisateur, null, null, null, admin, oType);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      utilisateur.setLogin("PIERRE");

      // On teste un update avec un attribut password non valide
      final String[] emptyValues2 = new String[] {null, "", "  ", createOverLength(100)};
      for(int i = 0; i < emptyValues2.length; i++){
         catched = false;
         try{
            utilisateur.setPassword(emptyValues2[i]);
            utilisateurManager.saveManager(utilisateur, null, null, null, admin, oType);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      utilisateur.setPassword("PASS");

      // On teste un update avec un attribut email non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            utilisateur.setEmail(emptyValues[i]);
            utilisateurManager.saveManager(utilisateur, null, null, null, admin, oType);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      utilisateur.setEmail(null);

   }

   @Test
   public void testDateDesactivationCoherence() throws ParseException{
      /*Utilisateur u = new Utilisateur();

      // null validation
      u.setTimeOut(null);
      Errors errs = UtilisateurValidator.checkDateDesactCoherence(u);
      assertTrue(errs.getAllErrors().size() == 0);

      // limites inf
      u.setTimeOut(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2009"));
      errs = UtilisateurValidator.checkDateDesactCoherence(u);
      assertEquals("date.validation.infDateActuelle",
      							errs.getFieldError().getCode());*/
   }

   private void removeObjectManagerTest(){
      final Utilisateur admin = utilisateurManager.findByIdManager(1);
      // test de la suppression d'un objet null
      utilisateurManager.removeObjectManager(null);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);

      // test de la suppression d'un objet non utilisé
      final Utilisateur u = new Utilisateur();
      u.setLogin("TEST");
      u.setPassword("PASS");
      utilisateurManager.saveManager(u, null, null, null, admin, plateformeManager.findByIdManager(1));
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 5);
      final Integer id = u.getUtilisateurId();

      //		// cree code Select pour deletion en cascade
      //		Banque b = banqueDao.findById(1);
      //		CodeSelect codeSel = new CodeSelect();
      //		codeSel.setCodeId(1);
      //		codeSel.setTableCodage(tableCodageDao.findById(1));
      //		codeSelectManager
      //			.createOrUpdateManager(codeSel, null, b, u, "creation");
      //		// cree code Utilisateur
      //		CodeUtilisateur codeU = new CodeUtilisateur();
      //		codeU.setCode("codeCas");
      //		codeU.setLibelle("libCasc");
      //		codeUtilisateurManager
      //			.createOrUpdateManager(codeU, null, b, u,
      //											null, null, "creation");
      //		assertTrue(codeSelectManager.findAllObjectsManager().size() == 6);
      //		assertTrue(codeUtilisateurManager.findAllObjectsManager().size() == 7);

      final Utilisateur uTest = utilisateurManager.findByIdManager(id);
      utilisateurManager.removeObjectManager(uTest);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
      assertTrue(operationManager.findByObjectManager(uTest).size() == 0);

      assertTrue(codeSelectManager.findAllObjectsManager().size() == 5);
      assertTrue(codeUtilisateurManager.findAllObjectsManager().size() == 6);

      // test de la non suppression d'un profil utilisé
      boolean catched = false;
      try{
         utilisateurManager.removeObjectManager(admin);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(utilisateurManager.findAllObjectsManager().size() == 4);
   }

   @Test
   /**
    * @version 2.1
    */
   public void testGetAvailablePlateformesManager(){
      final Utilisateur u1 = utilisateurManager.findByIdManager(1);
      List<Plateforme> pfs = utilisateurManager.getAvailablePlateformesManager(u1);
      // @since 2.1 PF2 ne sort pas car la seule banque 2 est archivée
      assertTrue(pfs.size() == 1);

      final Utilisateur u2 = utilisateurManager.findByIdManager(2);
      pfs = utilisateurManager.getAvailablePlateformesManager(u2);
      assertTrue(pfs.size() == 1);

      final Utilisateur u3 = utilisateurManager.findByIdManager(3);
      pfs = utilisateurManager.getAvailablePlateformesManager(u3);
      assertTrue(pfs.size() == 1);

      final Utilisateur u4 = utilisateurManager.findByIdManager(4);
      pfs = utilisateurManager.getAvailablePlateformesManager(u4);
      assertTrue(pfs.size() == 0);

      final Utilisateur u5 = utilisateurManager.findByIdManager(5);
      pfs = utilisateurManager.getAvailablePlateformesManager(u5);
      assertTrue(pfs.size() == 2);

      pfs = utilisateurManager.getAvailablePlateformesManager(null);
      assertTrue(pfs.size() == 0);
   }

   @Test
   public void testGetAvailableBanquesByPlateformeManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      final Utilisateur u1 = utilisateurManager.findByIdManager(1);
      List<Banque> bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u1, pf1, false);
      assertTrue(bks.size() == 2);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u1, pf1, true);
      assertTrue(bks.size() == 2);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u1, pf2, false);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u1, pf2, true);
      assertTrue(bks.size() == 1); // since 2.2.1 keepArchive

      final Utilisateur u3 = utilisateurManager.findByIdManager(3);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u3, pf1, false);
      assertTrue(bks.size() == 2);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u3, pf1, true);
      assertTrue(bks.size() == 2);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u3, pf2, false);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u3, pf2, true);
      assertTrue(bks.size() == 0);

      final Utilisateur u4 = utilisateurManager.findByIdManager(4);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u4, pf1, false);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u4, pf1, true);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u4, pf2, false);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u4, pf2, true);
      assertTrue(bks.size() == 0);

      final Utilisateur u5 = utilisateurManager.findByIdManager(5);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u5, pf1, false);
      assertTrue(bks.size() == 3);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u5, pf1, true);
      assertTrue(bks.size() == 3);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u5, pf2, false);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u5, pf2, true);
      assertTrue(bks.size() == 1);

      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(null, pf2, true);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(u5, null, true);
      assertTrue(bks.size() == 0);
      bks = utilisateurManager.getAvailableBanquesByPlateformeManager(null, null, true);
      assertTrue(bks.size() == 0);
   }

   @Test
   public void testArchiveScheduledUtilisateursManager(){
      final Utilisateur admin = utilisateurManager.findByIdManager(5);
      // Profil p1 = profilDao.findById(1);
      // Profil p2 = profilDao.findById(2);
      // Banque b1 = banqueDao.findById(1);
      final Collaborateur c = collaborateurDao.findById(1);

      final Utilisateur uNew1 = new Utilisateur();
      uNew1.setLogin("EXPIR1");
      uNew1.setPassword("EXPIR1");
      final Calendar cal = Calendar.getInstance();
      uNew1.setTimeOut(cal.getTime());
      uNew1.setArchive(false);
      utilisateurManager.saveManager(uNew1, c, null, null, admin, plateformeManager.findByIdManager(2));

      final Utilisateur uNew2 = new Utilisateur();
      uNew2.setLogin("EXPIR2");
      uNew2.setPassword("EXPIR2");
      uNew2.setTimeOut(cal.getTime());
      uNew2.setArchive(false);
      utilisateurManager.saveManager(uNew2, c, null, null, admin, plateformeManager.findByIdManager(2));

      final List<Utilisateur> toArchiveNow = utilisateurDao.findByTimeOutBefore(Calendar.getInstance().getTime());

      assertTrue(toArchiveNow.size() == 4);
      assertTrue(toArchiveNow.contains(utilisateurDao.findById(2)));
      assertTrue(toArchiveNow.contains(utilisateurDao.findById(4)));
      assertTrue(toArchiveNow.contains(uNew1));
      assertTrue(toArchiveNow.contains(uNew2));

      // null
      utilisateurManager.archiveScheduledUtilisateursManager(null);

      assertTrue(utilisateurDao.findByTimeOutBefore(Calendar.getInstance().getTime()).size() == 4);

      // ok
      utilisateurManager.archiveScheduledUtilisateursManager(admin);
      assertTrue(utilisateurDao.findByTimeOutBefore(Calendar.getInstance().getTime()).isEmpty());

      // restore
      final Utilisateur u2 = utilisateurDao.findById(2);
      u2.setArchive(false);
      utilisateurManager.saveManager(u2, u2.getCollaborateur(), null, null, admin, operationTypeDao.findById(5));
      // suppr 2 dernieres ops
      operationManager
         .removeObjectManager(operationManager.findByObjectManager(u2).get(operationManager.findByObjectManager(u2).size() - 1));
      operationManager
         .removeObjectManager(operationManager.findByObjectManager(u2).get(operationManager.findByObjectManager(u2).size() - 1));
      final Utilisateur u4 = utilisateurDao.findById(4);
      u4.setArchive(false);
      utilisateurManager.saveManager(u4, u4.getCollaborateur(), null, null, admin, operationTypeDao.findById(5));
      // suppr 2 derniere ops
      operationManager
         .removeObjectManager(operationManager.findByObjectManager(u4).get(operationManager.findByObjectManager(u4).size() - 1));
      operationManager
         .removeObjectManager(operationManager.findByObjectManager(u4).get(operationManager.findByObjectManager(u4).size() - 1));

      // clean up
      utilisateurManager.removeObjectManager(uNew1);
      utilisateurManager.removeObjectManager(uNew2);

      cleanUpFantomes(null);
   }
   
   /**
    * @since 2.2.1
    */
   @Test
   public void testFindSuperAndArchiveManager(){
      List<Utilisateur> utilisateurs =
         utilisateurManager.findBySuperAndArchiveManager(true, true);
      assertTrue(utilisateurs.isEmpty());

      utilisateurs =  utilisateurManager.findBySuperAndArchiveManager(false, true);
      assertTrue(utilisateurs.size() == 1);
      assertTrue(utilisateurs.get(0).getUtilisateurId() == 5);

      utilisateurs = utilisateurManager.findBySuperAndArchiveManager(true, false);
      assertTrue(utilisateurs.size() == 1);
      assertTrue(utilisateurs.get(0).getUtilisateurId() == 3);
      
      utilisateurs = utilisateurManager.findBySuperAndArchiveManager(false, false);
      assertTrue(utilisateurs.size() == 3);
   }

}
