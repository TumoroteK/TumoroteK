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
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.utilisateur.DroitObjetManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ProfilManager.
 * Classe créée le 20/05/10.
 *
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
public class ProfilManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ProfilManager profilManager;
   @Autowired
   private EntiteManager entiteManager;
   @Autowired
   private OperationTypeDao operationTypeDao;
   @Autowired
   private DroitObjetManager droitObjetManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private OperationManager operationManager;
   @Autowired
   private PlateformeDao plateformeDao;

   public ProfilManagerTest(){}

   @Test
   public void testFindById(){
      final Profil p = profilManager.findByIdManager(1);
      assertNotNull(p);
      assertTrue(p.getNom().equals("CONSULTATION"));
      assertFalse(p.getAccesAdministration());

      final Profil pNull = profilManager.findByIdManager(10);
      assertNull(pNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Profil> list = profilManager.findAllObjectsManager();
      assertTrue(list.size() == 5);
   }

   /**
    * Test la méthode findDoublon.
    * @version 2.1
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "CONSULTATION";
      final String nom2 = "GESTION";

      final Profil p1 = new Profil();
      p1.setNom(nom1);
      assertFalse(profilManager.findDoublonManager(p1));
      p1.setPlateforme(plateformeDao.findById(1));
      assertTrue(profilManager.findDoublonManager(p1));

      p1.setNom(nom2);
      assertFalse(profilManager.findDoublonManager(p1));

      final Profil p2 = profilManager.findByIdManager(2);
      assertFalse(profilManager.findDoublonManager(p2));

      p2.setNom(nom1);
      assertTrue(profilManager.findDoublonManager(p2));

      assertFalse(profilManager.findDoublonManager(null));
   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Profil p1 = profilManager.findByIdManager(1);
      assertTrue(profilManager.isUsedObjectManager(p1));

      final Profil p3 = profilManager.findByIdManager(3);
      assertFalse(profilManager.isUsedObjectManager(p3));

      assertFalse(profilManager.isUsedObjectManager(new Profil()));
      assertFalse(profilManager.isUsedObjectManager(null));
   }

   /**
    * Test le CRUD d'un Profil.
    * @throws ParseException 
    * @version 2.1
    */
   @Test
   public void testCrud() throws ParseException{
      saveManagerTest();
      saveManagerTest();
      deleteByIdManagerTest();
   }

   private void saveManagerTest() throws ParseException{

      final Utilisateur admin = utilisateurDao.findById(1);
      final OperationType type1 = operationTypeDao.findById(1);
      final Entite ent1 = entiteManager.findByIdManager(1);
      final Entite ent2 = entiteManager.findByIdManager(2);
      List<DroitObjet> droits = new ArrayList<>();
      final Plateforme pf1 = plateformeDao.findById(1);
      final Profil p1 = new Profil();

      p1.setNom("NEWPROFIL");

      boolean catched = false;

      // null plateforme
      try{
         profilManager.saveManager(p1, null, admin, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion d'un doublon
      p1.setNom("CONSULTATION");
      try{
         profilManager.saveManager(p1, null, admin, pf1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(profilManager.findAllObjectsManager().size() == 5);

      // on teste la validation lors de la création
      final String[] emptyValues = new String[] {null, "", "  ", "%$$*d", createOverLength(100)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            p1.setNom(emptyValues[i]);
            profilManager.saveManager(p1, null, admin, pf1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      p1.setNom("NEW");
      assertTrue(profilManager.findAllObjectsManager().size() == 5);

      // validation du chanmp admin
      try{
         p1.setAdmin(null);
         profilManager.saveManager(p1, null, admin, pf1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      p1.setAdmin(false);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);

      // validation du chanmp accesAdministration
      try{
         p1.setAccesAdministration(null);
         profilManager.saveManager(p1, null, admin, pf1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      p1.setAccesAdministration(false);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);

      // on teste la validation des DroitObjets
      final DroitObjet do1 = new DroitObjet();
      do1.setProfil(p1);
      do1.setEntite(ent1);
      do1.setOperationType(type1);
      final DroitObjet do2 = new DroitObjet();
      do2.setProfil(p1);
      do2.setEntite(ent2);
      do2.setOperationType(null);
      droits.add(do1);
      droits.add(do2);
      catched = false;
      try{
         profilManager.saveManager(p1, droits, admin, pf1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(profilManager.findAllObjectsManager().size() == 5);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);

      // insertion valide avec les droits à null
      profilManager.saveManager(p1, null, admin, pf1);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      assertTrue(operationManager.findByObjectManager(p1).size() == 1);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf1, false).size() == 4);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf1, false).contains(p1));
      final Integer idP1 = p1.getProfilId();

      final Profil pTest = profilManager.findByIdManager(idP1);
      assertNotNull(pTest);
      assertTrue(pTest.getNom().equals("NEW"));
      assertTrue(pTest.getPlateforme().equals(pf1));
      assertTrue(droitObjetManager.findByProfilManager(pTest).size() == 0);
      assertFalse(pTest.getAccesAdministration());

      // insertion valide avec les droits
      do2.setOperationType(type1);
      droits = new ArrayList<>();
      droits.add(do1);
      droits.add(do2);
      final Profil p2 = new Profil();
      p2.setNom("HEY");
      p2.setAdmin(false);
      p2.setAnonyme(true);
      p2.setAccesAdministration(true);
      p2.setArchive(true);
      profilManager.saveManager(p2, droits, admin, pf1);
      assertTrue(profilManager.findAllObjectsManager().size() == 7);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 11);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf1, true).size() == 2);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf1, true).contains(p2));
      final Integer idP2 = p2.getProfilId();
      assertTrue(operationManager.findByObjectManager(p2).size() == 1);

      final Profil pTest2 = profilManager.findByIdManager(idP2);
      assertNotNull(pTest2);
      assertTrue(pTest2.getNom().equals("HEY"));
      assertTrue(pTest2.getAccesAdministration());
      assertTrue(pTest2.isArchive());
      assertTrue(droitObjetManager.findByProfilManager(pTest2).size() == 2);

      // suppression des profils et des droits
      profilManager.deleteByIdManager(pTest);
      profilManager.deleteByIdManager(pTest2);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      assertTrue(operationManager.findByObjectManager(pTest).size() == 0);
      assertTrue(operationManager.findByObjectManager(pTest2).size() == 0);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf1, false).size() == 3);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf1, true).size() == 1);
   }

   private void saveManagerTest() throws ParseException{

      final Utilisateur admin = utilisateurDao.findById(1);
      final OperationType type1 = operationTypeDao.findById(1);
      final Entite ent1 = entiteManager.findByIdManager(1);
      final Entite ent2 = entiteManager.findByIdManager(2);
      List<DroitObjet> droits = new ArrayList<>();
      final Plateforme pf2 = plateformeDao.findById(2);

      final Profil p = new Profil();
      p.setNom("NEW");
      p.setAdmin(false);
      p.setAccesAdministration(true);
      final DroitObjet do1 = new DroitObjet();
      do1.setProfil(p);
      do1.setEntite(ent1);
      do1.setOperationType(type1);
      droits.add(do1);
      profilManager.saveManager(p, droits, admin, pf2);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf2, false).size() == 2);
      final Integer idP = p.getProfilId();

      final Profil pUp = profilManager.findByIdManager(idP);
      boolean catched = false;
      // on test l'insertion d'un doublon
      pUp.setNom("UTILISATEUR");
      try{
         profilManager.saveManager(pUp, droits, admin);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);

      // on teste la validation lors de la création
      final String[] emptyValues = new String[] {null, "", "  ", "%$$*d¤¤", createOverLength(100)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            pUp.setNom(emptyValues[i]);
            profilManager.saveManager(pUp, droits, admin);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      pUp.setNom("NEW");
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);

      // validation du chanmp admin
      try{
         pUp.setAdmin(null);
         profilManager.saveManager(pUp, null, admin);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      pUp.setAdmin(false);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);

      // validation du chanmp accesAdministration
      try{
         pUp.setAccesAdministration(null);
         profilManager.saveManager(pUp, null, admin);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      pUp.setAccesAdministration(false);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);

      // on teste la validation des DroitObjets
      final DroitObjet do2 = new DroitObjet();
      do2.setProfil(pUp);
      do2.setEntite(ent2);
      do2.setOperationType(null);
      droits = new ArrayList<>();
      droits.add(do1);
      droits.add(do2);
      catched = false;
      try{
         profilManager.saveManager(pUp, droits, admin);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);

      // update valide avec deux droits
      do2.setOperationType(type1);
      droits = new ArrayList<>();
      droits.add(do1);
      droits.add(do2);
      profilManager.saveManager(pUp, droits, admin);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 11);
      assertTrue(operationManager.findByObjectManager(pUp).size() == 2);

      final Profil pTest = profilManager.findByIdManager(idP);
      assertNotNull(pTest);
      assertTrue(pTest.getNom().equals("NEW"));
      assertTrue(droitObjetManager.findByProfilManager(pTest).size() == 2);

      // insertion valide avec un seul droits
      droits = new ArrayList<>();
      droits.add(do1);
      profilManager.saveManager(pUp, droits, admin);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 10);
      assertTrue(operationManager.findByObjectManager(pUp).size() == 3);

      final Profil pTest2 = profilManager.findByIdManager(idP);
      assertNotNull(pTest2);
      assertTrue(droitObjetManager.findByProfilManager(pTest2).size() == 1);
      assertTrue(droitObjetManager.findByProfilManager(pTest2).get(0).equals(do1));

      // insertion valide avec les droits nulls
      pUp.setArchive(true);
      profilManager.saveManager(pUp, new ArrayList<DroitObjet>(), admin);
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      assertTrue(operationManager.findByObjectManager(pUp).size() == 4);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf2, false).size() == 1);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf2, true).size() == 1);

      final Profil pTest3 = profilManager.findByIdManager(idP);
      assertNotNull(pTest3);
      assertTrue(pTest3.isArchive());
      assertTrue(droitObjetManager.findByProfilManager(pTest2).size() == 0);

      // suppression
      profilManager.deleteByIdManager(pTest3);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);
      assertTrue(droitObjetManager.findAllObjectsManager().size() == 9);
      assertTrue(operationManager.findByObjectManager(pTest3).size() == 0);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf2, false).size() == 1);
      assertTrue(profilManager.findByPlateformeAndArchiveManager(pf2, true).size() == 0);
   }

   private void deleteByIdManagerTest(){
      final Utilisateur admin = utilisateurDao.findById(1);
      // test de la suppression d'un objet null
      profilManager.deleteByIdManager(null);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);

      // test de la suppression d'un objet non utilisé
      final Profil p = new Profil();
      p.setNom("TEST");
      p.setAdmin(false);
      p.setAccesAdministration(true);
      profilManager.saveManager(p, null, admin, plateformeDao.findById(1));
      assertTrue(profilManager.findAllObjectsManager().size() == 6);
      final Integer id = p.getProfilId();

      final Profil pTest = profilManager.findByIdManager(id);
      profilManager.deleteByIdManager(pTest);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);
      assertTrue(operationManager.findByObjectManager(pTest).size() == 0);

      // test de la non suppression d'un profil utilisé
      final Profil p4 = profilManager.findByIdManager(4);
      boolean catched = false;
      try{
         profilManager.deleteByIdManager(p4);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ObjectUsedException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(profilManager.findAllObjectsManager().size() == 5);
   }

   /**
    * @since 2.1
    */
   @Test
   public void testFindByPlateformeAndArchiveManager(){
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);

      List<Profil> prfs = profilManager.findByPlateformeAndArchiveManager(pf1, false);
      assertTrue(prfs.size() == 3);
      assertTrue(prfs.get(2).getProfilId() == 2);

      prfs = profilManager.findByPlateformeAndArchiveManager(pf1, true);
      assertTrue(prfs.size() == 1);
      assertTrue(prfs.get(0).getProfilId() == 3);

      prfs = profilManager.findByPlateformeAndArchiveManager(pf1, null);
      assertTrue(prfs.size() == 4);
      assertTrue(prfs.get(0).getProfilId() == 4);
      assertTrue(prfs.get(1).getProfilId() == 1);
      assertTrue(prfs.get(2).getProfilId() == 3);
      assertTrue(prfs.get(3).getProfilId() == 2);

      prfs = profilManager.findByPlateformeAndArchiveManager(pf2, false);
      assertTrue(prfs.size() == 1);
      assertTrue(prfs.get(0).getProfilId() == 5);

      prfs = profilManager.findByPlateformeAndArchiveManager(pf2, true);
      assertTrue(prfs.size() == 0);

      prfs = profilManager.findByPlateformeAndArchiveManager(null, null);
      assertTrue(prfs.size() == 0);
   }

}
