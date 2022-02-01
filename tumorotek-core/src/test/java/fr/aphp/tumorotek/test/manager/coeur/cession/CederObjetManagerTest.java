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
package fr.aphp.tumorotek.test.manager.coeur.cession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.model.coeur.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.cession.CederObjetPK;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager CederObjetManager.
 * Classe créée le 29/01/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CederObjetManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CederObjetManager cederObjetManager;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private UniteDao uniteDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private ProdDeriveDao prodDeriveDao;
   @Autowired
   private PatientDao patientDao;

   public CederObjetManagerTest(){

   }

   @Test
   public void testFindById(){
      final Cession c1 = cessionDao.findById(1);
      final Entite e1 = entiteDao.findById(3);
      final CederObjetPK pk1 = new CederObjetPK(c1, e1, 1);

      final CederObjet co1 = cederObjetManager.findByIdManager(pk1);
      assertNotNull(co1);

      final Cession c2 = cessionDao.findById(3);
      final Entite e2 = entiteDao.findById(5);
      final CederObjetPK pk2 = new CederObjetPK(c2, e2, 2);
      final CederObjet co2 = cederObjetManager.findByIdManager(pk2);
      assertNull(co2);

      final CederObjet co3 = cederObjetManager.findByIdManager(null);
      assertNull(co3);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<CederObjet> list = cederObjetManager.findAllObjectsManager();
      assertTrue(list.size() == 6);
   }

   /**
    * Test la méthode findByExcludedPKManager.
    */
   @Test
   public void testFindByExcludedPKManager(){
      final Cession c1 = cessionDao.findById(1);
      final Entite e1 = entiteDao.findById(3);
      final CederObjetPK pk1 = new CederObjetPK(c1, e1, 1);

      List<CederObjet> list = cederObjetManager.findByExcludedPKManager(pk1);
      assertTrue(list.size() == 5);

      final Cession c2 = cessionDao.findById(3);
      final Entite e2 = entiteDao.findById(5);
      final CederObjetPK pk2 = new CederObjetPK(c2, e2, 2);
      list = cederObjetManager.findByExcludedPKManager(pk2);
      assertTrue(list.size() == 6);

      list = cederObjetManager.findByExcludedPKManager(null);
      assertTrue(list.size() == 6);
   }

   /**
    * Test la méthode findByEntiteManager.
    */
   @Test
   public void testFindByEntiteManager(){
      Entite e1 = entiteDao.findByNom("Echantillon").get(0);
      List<CederObjet> list = cederObjetManager.findByEntiteManager(e1);
      assertTrue(list.size() == 4);

      e1 = entiteDao.findByNom("Patient").get(0);
      list = cederObjetManager.findByEntiteManager(e1);
      assertTrue(list.size() == 0);

      list = cederObjetManager.findByEntiteManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByEntiteManager.
    */
   @Test
   public void testGetAllEchantillonsCederObjetsManager(){

      final List<CederObjet> list = cederObjetManager.getAllEchantillonsCederObjetsManager();
      assertTrue(list.size() == 4);

   }

   /**
    * Test la méthode getAllProdDerivesCederObjetsManager.
    */
   @Test
   public void testGetAllProdDerivesCederObjetsManager(){

      final List<CederObjet> list = cederObjetManager.getAllProdDerivesCederObjetsManager();
      assertTrue(list.size() == 2);

   }

   /**
    * Test la méthode findByObjetManager.
    */
   @Test
   public void testFindByObjetManager(){

      final Echantillon echan1 = echantillonDao.findById(1);
      List<CederObjet> list = cederObjetManager.findByObjetManager(echan1);
      assertTrue(list.size() == 2);

      final Echantillon echan2 = echantillonDao.findById(4);
      list = cederObjetManager.findByObjetManager(echan2);
      assertTrue(list.size() == 0);

      final ProdDerive derive1 = prodDeriveDao.findById(1);
      list = cederObjetManager.findByObjetManager(derive1);
      assertTrue(list.size() == 1);

      final ProdDerive derive2 = prodDeriveDao.findById(2);
      list = cederObjetManager.findByObjetManager(derive2);
      assertTrue(list.size() == 0);

      final Patient pat = patientDao.findById(1);
      list = cederObjetManager.findByObjetManager(pat);
      assertTrue(list.size() == 0);

      list = cederObjetManager.findByObjetManager(null);
      assertTrue(list.size() == 0);

   }

   /**
    * Test la méthode getAllCessionsByStatutAndObjetManager.
    */
   @Test
   public void testGetAllCessionsByStatutAndObjetManager(){

      final Echantillon echan1 = echantillonDao.findById(1);
      List<Cession> list = cederObjetManager.getAllCessionsByStatutAndObjetManager("VALIDEE", echan1);
      assertTrue(list.size() == 1);

      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("REFUSEE", echan1);
      assertTrue(list.size() == 1);

      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("EN ATTENTE", echan1);
      assertTrue(list.size() == 0);

      final Echantillon echan4 = echantillonDao.findById(4);
      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("VALIDEE", echan4);
      assertTrue(list.size() == 0);

      final ProdDerive derive1 = prodDeriveDao.findById(1);
      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("VALIDEE", derive1);
      assertTrue(list.size() == 1);

      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("REFUSEE", derive1);
      assertTrue(list.size() == 0);

      final Patient pat = patientDao.findById(1);
      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("VALIDEE", pat);
      assertTrue(list.size() == 0);

      list = cederObjetManager.getAllCessionsByStatutAndObjetManager(null, null);
      assertTrue(list.size() == 0);

      list = cederObjetManager.getAllCessionsByStatutAndObjetManager(null, echan1);
      assertTrue(list.size() == 0);

      list = cederObjetManager.getAllCessionsByStatutAndObjetManager("VALIDEE", null);
      assertTrue(list.size() == 0);

   }

   /**
    * Test la méthode findByCessionEntiteManager.
    */
   @Test
   public void testFindByCessionEntiteManager(){
      Entite e1 = entiteDao.findByNom("Echantillon").get(0);
      final Cession c2 = cessionDao.findById(2);
      List<CederObjet> list = cederObjetManager.findByCessionEntiteManager(c2, e1);
      assertTrue(list.size() == 3);

      e1 = entiteDao.findByNom("ProdDerive").get(0);
      list = cederObjetManager.findByCessionEntiteManager(c2, e1);
      assertTrue(list.size() == 0);

      list = cederObjetManager.findByCessionEntiteManager(c2, null);
      assertTrue(list.size() == 0);

      list = cederObjetManager.findByCessionEntiteManager(null, e1);
      assertTrue(list.size() == 0);

      list = cederObjetManager.findByCessionEntiteManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode getEchantillonsCedesByCessionManager.
    */
   @Test
   public void testGetEchantillonsCedesByCessionManager(){

      final Cession c2 = cessionDao.findById(2);
      List<CederObjet> list = cederObjetManager.getEchantillonsCedesByCessionManager(c2);
      assertTrue(list.size() == 3);

      final Cession c4 = cessionDao.findById(4);
      list = cederObjetManager.getEchantillonsCedesByCessionManager(c4);
      assertTrue(list.size() == 0);

      list = cederObjetManager.getEchantillonsCedesByCessionManager(null);
      assertTrue(list.size() == 0);

   }

   /**
    * Test la méthode getProdDerivesCedesByCessionManager.
    */
   @Test
   public void testGetProdDerivesCedesByCessionManager(){

      final Cession c2 = cessionDao.findById(2);
      List<CederObjet> list = cederObjetManager.getProdDerivesCedesByCessionManager(c2);
      assertTrue(list.size() == 0);

      final Cession c4 = cessionDao.findById(4);
      list = cederObjetManager.getProdDerivesCedesByCessionManager(c4);
      assertTrue(list.size() == 1);

      list = cederObjetManager.getProdDerivesCedesByCessionManager(null);
      assertTrue(list.size() == 0);

   }

   /**
    * Test la méthode findDoublonManager.
    */
   @Test
   public void testFindDoublonManager(){
      final Entite e1 = entiteDao.findByNom("Echantillon").get(0);
      final Cession c1 = cessionDao.findById(1);
      final CederObjetPK pk1 = new CederObjetPK(c1, e1, 1);
      final CederObjet co1 = new CederObjet();
      co1.setPk(pk1);
      assertTrue(cederObjetManager.findDoublonManager(co1));

      co1.setObjetId(2);
      assertFalse(cederObjetManager.findDoublonManager(co1));

      assertFalse(cederObjetManager.findDoublonManager(null));
   }

   /**
    * Test la méthode isValidPK.
    */
   @Test
   public void testIsValidPK(){
      // Test avec echantillon (valide puis non valide)
      Entite e1 = entiteDao.findByNom("Echantillon").get(0);
      final Cession c1 = cessionDao.findById(1);
      final CederObjetPK pk1 = new CederObjetPK(c1, e1, 1);
      final CederObjet co1 = new CederObjet();
      co1.setPk(pk1);
      assertTrue(cederObjetManager.isValidPK(co1));

      co1.setObjetId(10);
      assertFalse(cederObjetManager.isValidPK(co1));

      // Test avec ProdDerive (valide puis non valide)
      e1 = entiteDao.findByNom("ProdDerive").get(0);
      co1.setEntite(e1);
      co1.setObjetId(3);
      assertTrue(cederObjetManager.isValidPK(co1));

      // Test avec Patient
      e1 = entiteDao.findByNom("Patient").get(0);
      co1.setEntite(e1);
      assertFalse(cederObjetManager.isValidPK(co1));

      co1.setPk(null);
      assertFalse(cederObjetManager.isValidPK(co1));

      assertFalse(cederObjetManager.isValidPK(null));
   }

   /**
    * Teste la methode validateObjectManager. 
    */
   @Test
   public void testValidateObjectManager(){

      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);

      final CederObjet ced1 = new CederObjet();

      Boolean catched = false;
      // on test l'insertion avec la cession nulle
      try{
         cederObjetManager.validateObjectManager(ced1, null, entite, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'insertion avec l'entité nulle
      try{
         cederObjetManager.validateObjectManager(ced1, cession, null, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      ced1.setObjetId(1);
      try{
         cederObjetManager.validateObjectManager(ced1, cession, entite, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On teste l'insertion d'une PK non valide pour vérifier que 
      // l'exception est lancée
      ced1.setObjetId(10);
      try{
         cederObjetManager.validateObjectManager(ced1, cession, entite, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPKException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // Test de la validation lors de la création
      validate(ced1);

   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    */
   @Test
   public void testCrud(){
      saveManagerTest();
      saveManagerTest();
      removeObjectManagerTest();
   }

   /**
    * Teste la methode saveManager. 
    */
   @Test
   public void saveManagerTest(){

      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);
      final Unite qteUnite = uniteDao.findById(5);
      final float qte = (float) 10.0;

      final CederObjet ced1 = new CederObjet();

      Boolean catched = false;
      // on test l'insertion avec la cession nulle
      try{
         cederObjetManager.saveManager(ced1, null, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
      catched = false;

      // on test l'insertion avec l'entité nulle
      try{
         cederObjetManager.saveManager(ced1, cession, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
      catched = false;

      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      ced1.setObjetId(1);
      try{
         cederObjetManager.saveManager(ced1, cession, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
      catched = false;

      // On teste l'insertion d'une PK non valide pour vérifier que 
      // l'exception est lancée
      ced1.setObjetId(10);
      try{
         cederObjetManager.saveManager(ced1, cession, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPKException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
      catched = false;

      // Test de la validation lors de la création
      validationInsert(ced1);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);

      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      ced1.setObjetId(2);
      cederObjetManager.saveManager(ced1, cession, entite, null);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 7);
      final CederObjetPK pk1 = ced1.getPk();

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies
      final CederObjet ced2 = new CederObjet();
      ced2.setObjetId(4);
      ced2.setQuantite(qte);
      cederObjetManager.saveManager(ced2, cession, entite, qteUnite);
      final CederObjetPK pk2 = ced2.getPk();
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 8);
      // On vérifie que toutes associations ont étées enregistrées
      final CederObjet cedTest = cederObjetManager.findByIdManager(pk2);
      assertNotNull(cedTest);
      assertNotNull(cedTest.getCession());
      assertNotNull(cedTest.getEntite());
      assertNotNull(cedTest.getQuantiteUnite());
      // On test les attributs
      assertTrue(cedTest.getObjetId() == 4);
      assertTrue(cedTest.getQuantite() == qte);

      // Suppression
      final CederObjet cedToRemove1 = cederObjetManager.findByIdManager(pk1);
      final CederObjet cedToRemove2 = cederObjetManager.findByIdManager(pk2);
      cederObjetManager.removeObjectManager(cedToRemove1);
      cederObjetManager.removeObjectManager(cedToRemove2);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);

   }

   /**
    * Teste la methode saveManager. 
    */
   @Test
   public void saveManagerTest(){

      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);
      final Unite qteUnite = uniteDao.findById(5);
      final float qte = (float) 10.0;

      // Insertion de départ
      final CederObjet ced = new CederObjet();
      ced.setObjetId(2);
      cederObjetManager.saveManager(ced, cession, entite, null);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 7);
      final CederObjetPK pk = ced.getPk();

      Boolean catched = false;
      final CederObjet cedUp1 = cederObjetManager.findByIdManager(pk);
      assertNotNull(cedUp1);
      // on test l'update avec la cession nulle
      try{
         cederObjetManager.saveManager(cedUp1, null, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test l'update avec l'entité nulle
      try{
         cederObjetManager.saveManager(cedUp1, cession, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On teste l'update d'une PK non valide pour vérifier que 
      // l'exception est lancée
      cedUp1.setObjetId(10);
      try{
         cederObjetManager.saveManager(cedUp1, cession, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("InvalidPKException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // On teste l'update d'une PK inconnu pour vérifier que 
      // l'exception est lancée
      cedUp1.setObjetId(4);
      try{
         cederObjetManager.saveManager(cedUp1, cession, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("PKNotFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // Test de la validation lors de la création
      validationUpdate(cedUp1);

      // on teste une modif valide avec les associations 
      // non obigatoires
      cedUp1.setQuantite(qte);
      cederObjetManager.saveManager(cedUp1, cession, entite, qteUnite);
      // On vérifie que toutes associations ont étées enregistrées
      final CederObjet cedTest = cederObjetManager.findByIdManager(pk);
      assertNotNull(cedTest);
      assertNotNull(cedTest.getCession());
      assertNotNull(cedTest.getEntite());
      assertNotNull(cedTest.getQuantiteUnite());
      // On test les attributs
      assertTrue(cedTest.getObjetId() == 2);
      assertTrue(cedTest.getQuantite() == qte);

      // Suppression
      final CederObjet cedToRemove = cederObjetManager.findByIdManager(pk);
      cederObjetManager.removeObjectManager(cedToRemove);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   @Test
   public void removeObjectManagerTest(){
      // test de la suppression d'un objet null
      cederObjetManager.removeObjectManager(null);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);

      // test de la suppression d'un objet non présent dans la base
      final Cession c1 = cessionDao.findById(1);
      final Entite e1 = entiteDao.findById(3);
      final CederObjetPK pk1 = new CederObjetPK(c1, e1, 4);
      final CederObjet ced = new CederObjet();
      ced.setPk(pk1);
      cederObjetManager.removeObjectManager(ced);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);

      // test de la suppression avec PK invalide
      final CederObjetPK pk2 = new CederObjetPK(c1, null, 4);
      final CederObjet ced2 = new CederObjet();
      ced.setPk(pk2);
      cederObjetManager.removeObjectManager(ced2);
      assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
   }

   /**
    * Test la validation d'un CederObjet.
    * @param ce CederObjet à tester.
    */
   private void validate(final CederObjet ce){
      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);

      boolean catchedInsert = false;
      // On teste une insertion avec un attribut objetId non valide
      final Integer[] ids = new Integer[] {null, -1};
      for(int i = 0; i < ids.length; i++){
         catchedInsert = false;
         try{
            ce.setObjetId(ids[i]);
            cederObjetManager.validateObjectManager(ce, cession, entite, null, "creation");
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      ce.setObjetId(2);

      // On teste une insertion avec un attribut quantité non valide
      ce.setQuantite((float) -10);
      catchedInsert = false;
      try{
         cederObjetManager.validateObjectManager(ce, cession, entite, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      ce.setQuantite(null);

   }

   /**
    * Test la validation d'un CederObjet lors de sa création.
    * @param ce CederObjet à tester.
    */
   private void validationInsert(final CederObjet ce){
      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);

      boolean catchedInsert = false;
      // On teste une insertion avec un attribut objetId non valide
      final Integer[] ids = new Integer[] {null, -1};
      for(int i = 0; i < ids.length; i++){
         catchedInsert = false;
         try{
            ce.setObjetId(ids[i]);
            cederObjetManager.saveManager(ce, cession, entite, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      ce.setObjetId(2);

      // On teste une insertion avec un attribut quantité non valide
      ce.setQuantite((float) -10);
      catchedInsert = false;
      try{
         cederObjetManager.saveManager(ce, cession, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedInsert = true;
         }
      }
      ce.setQuantite(null);

   }

   /**
    * Test la validation d'un CederObjet lors de l'update.
    * @param ce CederObjet à tester.
    */
   private void validationUpdate(final CederObjet ce){
      final Cession cession = cessionDao.findById(1);
      final Entite entite = entiteDao.findById(3);

      boolean catched = false;
      // On teste un update avec un attribut objetId non valide
      final Integer[] ids = new Integer[] {null, -1};
      for(int i = 0; i < ids.length; i++){
         catched = false;
         try{
            ce.setObjetId(ids[i]);
            cederObjetManager.saveManager(ce, cession, entite, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      ce.setObjetId(2);

      // On teste un update avec un attribut quantité non valide
      ce.setQuantite((float) -10);
      catched = false;
      try{
         cederObjetManager.saveManager(ce, cession, entite, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      ce.setQuantite(null);

   }

   @Test
   public void testCountObjectCessedManager(){
      final Entite e1 = entiteDao.findById(3);
      final Entite e2 = entiteDao.findById(2);
      final Entite e8 = entiteDao.findById(8);
      final Cession c1 = cessionDao.findById(1);

      Long count = cederObjetManager.findObjectsCessedCountManager(c1, e1);
      assertTrue(count == 1);

      count = cederObjetManager.findObjectsCessedCountManager(c1, e8);
      assertTrue(count == 1);

      count = cederObjetManager.findObjectsCessedCountManager(c1, e2);
      assertTrue(count == 0);

      count = cederObjetManager.findObjectsCessedCountManager(null, null);
      assertTrue(count == 0);
   }

   @Test
   public void testFindCodesByCessionEntiteManager(){
      final Entite e1 = entiteDao.findById(3);
      final Entite e2 = entiteDao.findById(2);
      final Entite e8 = entiteDao.findById(8);
      final Cession c1 = cessionDao.findById(1);

      List<String> codes = cederObjetManager.findCodesByCessionEntiteManager(c1, e1);
      assertTrue(codes.size() == 1);
      assertTrue(codes.contains("PTRA.1"));

      codes = cederObjetManager.findCodesByCessionEntiteManager(c1, e8);
      assertTrue(codes.contains("EHT.1.1"));

      codes = cederObjetManager.findCodesByCessionEntiteManager(c1, null);
      assertNull(codes);

      codes = cederObjetManager.findCodesByCessionEntiteManager(c1, e2);
      assertNull(codes);

      codes = cederObjetManager.findCodesByCessionEntiteManager(null, e8);
      assertTrue(codes.isEmpty());
   }

}
