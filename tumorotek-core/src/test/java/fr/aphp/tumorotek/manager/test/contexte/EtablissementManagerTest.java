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
package fr.aphp.tumorotek.manager.test.contexte;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.cession.ContratDao;
import fr.aphp.tumorotek.dao.contexte.CategorieDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.EtablissementManager;
import fr.aphp.tumorotek.manager.context.ServiceManager;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.context.TitreManager;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager EtablissementManagerTest.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EtablissementManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EtablissementManager etablissementManager;
   @Autowired
   private CoordonneeManager coordonneeManager;
   @Autowired
   private CategorieDao categorieDao;
   @Autowired
   private ServiceManager serviceManager;
   @Autowired
   private SpecialiteManager specialiteManager;
   @Autowired
   private TitreManager titreManager;
   @Autowired
   private CollaborateurManager collaborateurManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private CessionManager cessionManager;
   @Autowired
   private ContratManager contratManager;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private ContratDao contratDao;
   @Autowired
   private OperationDao operationDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private OperationTypeDao operationTypeDao;

   public EtablissementManagerTest(){}

   @Test
   public void testFindById(){
      final Etablissement etab = etablissementManager.findByIdManager(1);
      assertNotNull(etab);
      assertTrue(etab.getNom().equals("SAINT LOUIS"));

      final Etablissement etabNull = etablissementManager.findByIdManager(5);
      assertNull(etabNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Etablissement> list = etablissementManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   /**
    * Test la méthode findAllObjectsWithOrderManager.
    */
   @Test
   public void testFindAllObjectsWithOrderManager(){
      final List<Etablissement> list = etablissementManager.findAllObjectsWithOrderManager();
      assertTrue(list.size() == 4);
      assertTrue(list.get(0).getNom().equals("BEAUVAIS CH"));
      assertTrue(list.get(3).getNom().equals("SAINT LOUIS"));
   }

   /**
    * Test la méthode findAllActiveObjectsWithOrderManager.
    */
   @Test
   public void testFindAllActiveObjectsWithOrderManager(){
      final List<Etablissement> list = etablissementManager.findAllActiveObjectsWithOrderManager();
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("BEAUVAIS CH"));
      assertTrue(list.get(2).getNom().equals("SAINT LOUIS"));
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeExactManager(){
      List<Etablissement> list = etablissementManager.findByNomLikeManager("SAINT LOUIS", true);
      assertTrue(list.size() == 1);

      list = etablissementManager.findByNomLikeManager("SAINT", true);
      assertTrue(list.size() == 0);

      list = etablissementManager.findByNomLikeManager("", true);
      assertTrue(list.size() == 0);

      list = etablissementManager.findByNomLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      List<Etablissement> list = etablissementManager.findByNomLikeManager("SAINT LOUIS", false);
      assertTrue(list.size() == 1);

      list = etablissementManager.findByNomLikeManager("SAINT", false);
      assertTrue(list.size() == 1);

      list = etablissementManager.findByNomLikeManager("", false);
      assertTrue(list.size() == 4);

      list = etablissementManager.findByNomLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeBothSideManager.
    */
   @Test
   public void testFindByNomLikeBothSideManager(){
      List<Etablissement> list = etablissementManager.findByNomLikeBothSideManager("SAINT LOUIS");
      assertTrue(list.size() == 1);

      list = etablissementManager.findByNomLikeBothSideManager("LOU");
      assertTrue(list.size() == 1);

      list = etablissementManager.findByNomLikeBothSideManager("");
      assertTrue(list.size() == 4);

      list = etablissementManager.findByNomLikeBothSideManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByVilleLikeManager.
    */
   @Test
   public void testFindByVilleLikeManager(){
      List<Etablissement> list = etablissementManager.findByVilleLikeManager("PARIS");
      assertTrue(list.size() == 1);

      list = etablissementManager.findByVilleLikeManager("ES");
      assertTrue(list.size() == 2);

      list = etablissementManager.findByVilleLikeManager("");
      assertTrue(list.size() == 4);

      list = etablissementManager.findByVilleLikeManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByFinessLikeManager.
    */
   @Test
   public void testFindByFinessLikeExactManager(){
      List<Etablissement> list = etablissementManager.findByFinessLikeManager("1111", true);
      assertTrue(list.size() == 1);

      list = etablissementManager.findByFinessLikeManager("11", true);
      assertTrue(list.size() == 0);

      list = etablissementManager.findByFinessLikeManager("", true);
      assertTrue(list.size() == 0);

      list = etablissementManager.findByFinessLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByFinessLikeManager.
    */
   @Test
   public void testFindByFinessLikeManager(){
      List<Etablissement> list = etablissementManager.findByFinessLikeManager("1111", false);
      assertTrue(list.size() == 1);

      list = etablissementManager.findByFinessLikeManager("11", false);
      assertTrue(list.size() == 1);

      list = etablissementManager.findByFinessLikeManager("", false);
      assertTrue(list.size() == 4);

      list = etablissementManager.findByFinessLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode getServicesWithOrderManager.
    */
   @Test
   public void testGetServicesWithOrderManager(){
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      List<Service> services = etablissementManager.getServicesWithOrderManager(etab1);
      assertTrue(services.size() == 3);
      assertTrue(services.get(0).getNom().equals("ANAPATH"));
      assertTrue(services.get(1).getNom().equals("CELLULO"));

      final Etablissement etab2 = etablissementManager.findByIdManager(3);
      services = etablissementManager.getServicesWithOrderManager(etab2);
      assertTrue(services.size() == 0);

      services = etablissementManager.getServicesWithOrderManager(null);
      assertTrue(services.size() == 0);
   }

   /**
    * Test la méthode getActiveServicesWithOrderManager.
    */
   @Test
   public void testGetActiveServicesWithOrderManager(){
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      List<Service> services = etablissementManager.getActiveServicesWithOrderManager(etab1);
      assertTrue(services.size() == 2);
      assertTrue(services.get(0).getNom().equals("ANAPATH"));
      assertTrue(services.get(1).getNom().equals("HEMATO"));

      final Etablissement etab2 = etablissementManager.findByIdManager(3);
      services = etablissementManager.getActiveServicesWithOrderManager(etab2);
      assertTrue(services.size() == 0);

      services = etablissementManager.getActiveServicesWithOrderManager(null);
      assertTrue(services.size() == 0);
   }

   /**
    * Test la méthode getServicesManager.
    */
   @Test
   public void testGetServicesManager(){
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      Set<Service> services = etablissementManager.getServicesManager(etab1);
      assertTrue(services.size() == 3);

      final Etablissement etab2 = etablissementManager.findByIdManager(3);
      services = etablissementManager.getServicesManager(etab2);
      assertTrue(services.size() == 0);
   }

   /**
    * Test la méthode getCollaborateursManager.
    */
   @Test
   public void testGetCollaborateursManager(){
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      Set<Collaborateur> collabs = etablissementManager.getCollaborateursManager(etab1);
      assertTrue(collabs.size() == 3);

      final Etablissement etab2 = etablissementManager.findByIdManager(2);
      collabs = etablissementManager.getCollaborateursManager(etab2);
      assertTrue(collabs.size() == 0);
   }

   /**
    * Test la méthode getCollaborateursWithOrderManager.
    */
   @Test
   public void testGetCollaborateursWithOrderManager(){
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      List<Collaborateur> collabs = etablissementManager.getCollaborateursWithOrderManager(etab1);
      assertTrue(collabs.size() == 3);
      assertTrue(collabs.get(0).getNom().equals("DUFAY"));
      assertTrue(collabs.get(1).getNom().equals("VIAL"));

      final Etablissement etab2 = etablissementManager.findByIdManager(2);
      collabs = etablissementManager.getCollaborateursWithOrderManager(etab2);
      assertTrue(collabs.size() == 0);

      collabs = etablissementManager.getCollaborateursWithOrderManager(null);
      assertTrue(collabs.size() == 0);
   }

   /**
    * Test la méthode getActiveCollaborateursWithOrderManager.
    */
   @Test
   public void testGetActiveCollaborateursWithOrderManager(){
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      List<Collaborateur> collabs = etablissementManager.getActiveCollaborateursWithOrderManager(etab1);
      assertTrue(collabs.size() == 2);
      assertTrue(collabs.get(0).getNom().equals("DUFAY"));
      assertTrue(collabs.get(1).getNom().equals("VIAL"));

      final Etablissement etab2 = etablissementManager.findByIdManager(2);
      collabs = etablissementManager.getActiveCollaborateursWithOrderManager(etab2);
      assertTrue(collabs.size() == 0);

      final Etablissement etab4 = etablissementManager.findByIdManager(4);
      collabs = etablissementManager.getActiveCollaborateursWithOrderManager(etab4);
      assertTrue(collabs.size() == 0);

      collabs = etablissementManager.getActiveCollaborateursWithOrderManager(null);
      assertTrue(collabs.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "SAINT LOUIS";
      final String nom2 = "SAINT";
      final String finess1 = "1111";
      final String finess2 = "1010";

      final Etablissement etab1 = new Etablissement();
      etab1.setNom(nom1);
      etab1.setFiness(finess1);
      assertTrue(etablissementManager.findDoublonManager(etab1));

      etab1.setNom(nom2);
      assertFalse(etablissementManager.findDoublonManager(etab1));

      etab1.setNom(nom1);
      etab1.setFiness(finess2);
      assertFalse(etablissementManager.findDoublonManager(etab1));

      final Etablissement etab2 = etablissementManager.findByIdManager(2);
      assertFalse(etablissementManager.findDoublonManager(etab2));

      etab2.setNom(nom1);
      assertFalse(etablissementManager.findDoublonManager(etab2));

      etab2.setFiness(finess1);
      assertTrue(etablissementManager.findDoublonManager(etab2));

   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      final Utilisateur u = utilisateurDao.findById(1);
      // Récupération des objets associés à un Etablissement
      final Categorie cat = categorieDao.findById(1);

      // Insertion
      final Etablissement etab1 = new Etablissement();
      etab1.setNom("SAINT LOUIS");
      etab1.setFiness("1111");
      Boolean catched = false;
      Coordonnee coordExcp = new Coordonnee();
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         etablissementManager.createObjectManager(etab1, coordExcp, cat, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);

      // Test de la validation lors de la création
      validationInsert(etab1);
      assertTrue(etablissementManager.findAllObjectsManager().size() == 4);
      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      etab1.setNom("CLERMONT");
      etab1.setFiness("1010");
      etablissementManager.createObjectManager(etab1, null, null, u);
      final int idE1 = etab1.getEtablissementId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(etab1).size() == 1);

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies et une nouvelle coordonnée
      Etablissement etab2 = new Etablissement();
      etab2.setNom("LYON");
      etab2.setFiness("51459");
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul établissement
      final Coordonnee newCoord1 = new Coordonnee();
      newCoord1.setAdresse("TMP");
      etablissementManager.createObjectManager(etab2, newCoord1, cat, u);
      int id = newCoord1.getCoordonneeId();
      final int idE2 = etab2.getEtablissementId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(etab2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      Etablissement testInsert = etablissementManager.findByIdManager(idE2);
      assertNotNull(testInsert.getCategorie());
      assertNotNull(testInsert.getCoordonnee());
      assertTrue(testInsert.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert.getNom().length() > 0);
      assertTrue(testInsert.getFiness().length() > 0);

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies et une nouvelle coordonnée
      etab2 = new Etablissement();
      etab2.setNom("LYON2");
      etab2.setFiness("51459bis");
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul établissement
      final Coordonnee newCoordbis = new Coordonnee();
      newCoordbis.setAdresse("TMP");
      coordonneeManager.createObjectManager(newCoordbis, null);
      final int id2 = newCoordbis.getCoordonneeId();
      etablissementManager.createObjectManager(etab2, newCoordbis, cat, u);
      final int idE3 = etab2.getEtablissementId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(etab2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert = etablissementManager.findByIdManager(idE3);
      assertNotNull(testInsert.getCategorie());
      assertNotNull(testInsert.getCoordonnee());
      assertTrue(testInsert.getCoordonnee().getCoordonneeId() == id2);
      // On test les attributs
      assertTrue(testInsert.getNom().length() > 0);
      assertTrue(testInsert.getFiness().length() > 0);

      // Suppression
      final Etablissement etab3 = etablissementManager.findByIdManager(idE1);
      etablissementManager.removeObjectManager(etab3, null, u);
      assertTrue(getOperationManager().findByObjectManager(etab3).size() == 0);
      final Etablissement etab4 = etablissementManager.findByIdManager(idE2);
      etablissementManager.removeObjectManager(etab4, null, u);
      assertTrue(getOperationManager().findByObjectManager(etab4).size() == 0);
      final Etablissement etab4b = etablissementManager.findByIdManager(idE3);
      etablissementManager.removeObjectManager(etab4b, null, u);
      assertTrue(getOperationManager().findByObjectManager(etab4b).size() == 0);
      assertNull(coordonneeManager.findByIdManager(id));
      assertNull(coordonneeManager.findByIdManager(id2));

      // Update
      final String nomUpdated2 = "SAINT LOUIS";
      final String finessUpdated2 = "1111";
      final Etablissement etab5 = new Etablissement();
      etab5.setNom("NANCY");
      etablissementManager.createObjectManager(etab5, null, null, u);
      final int idE4 = etab5.getEtablissementId();
      assertTrue(getOperationManager().findByObjectManager(etab5).size() == 1);

      Boolean catchedUpdate = false;
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée et que la coordonnée n'est pas créée
      final Etablissement etab6 = etablissementManager.findByIdManager(idE4);
      etab6.setNom(nomUpdated2);
      etab6.setFiness(finessUpdated2);
      coordExcp = new Coordonnee();
      coordExcp.setAdresse("Excep");
      try{
         etablissementManager.updateObjectManager(etab6, coordExcp, null, u, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      catchedUpdate = false;

      // on vérifie que l'exception est lancée sur la validation 
      // de la coordonnée
      etab6.setNom("GRENOBLE");
      coordExcp.setAdresse("         ");
      try{
         etablissementManager.updateObjectManager(etab6, coordExcp, null, u, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      catchedUpdate = false;

      // On test la validation lors d'un update
      final Etablissement etab7 = etablissementManager.findByIdManager(idE4);
      validationUpdate(etab7);

      // On teste une mise à jour valide avec les assos a null
      etab7.setNom("GRENOBLE");
      etab7.setFiness("54864");
      etablissementManager.updateObjectManager(etab7, null, null, u, false);
      final Etablissement etab8 = etablissementManager.findByIdManager(idE4);
      assertTrue(etab8.getNom().equals("GRENOBLE"));
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      assertTrue(getOperationManager().findByObjectManager(etab7).size() == 2);

      // On teste une mise à jour valide avec les assos et une nouvelle
      // coordonnée
      final Etablissement etab9 = etablissementManager.findByIdManager(idE4);
      etab9.setNom("TOULON");
      etab9.setFiness("1515");
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul établissement
      final Coordonnee newCoord2 = new Coordonnee();
      newCoord2.setAdresse("TMP");
      etablissementManager.updateObjectManager(etab9, newCoord2, cat, u, false);
      id = newCoord2.getCoordonneeId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(etab9).size() == 3);

      // On vérifie que toutes associations ont étées enregistrées
      final Etablissement testInsert2 = etablissementManager.findByIdManager(idE4);
      assertNotNull(testInsert2.getCategorie());
      assertNotNull(testInsert2.getCoordonnee());
      assertTrue(testInsert2.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert2.getNom().length() > 0);
      assertTrue(testInsert2.getFiness().length() > 0);

      // On teste une mise à jour valide avec les assos et une
      // coordonnée existante
      final Etablissement etab10 = etablissementManager.findByIdManager(idE4);
      etab10.setNom("TOULON2");
      etab10.setFiness("3030");
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul établissement
      final Coordonnee newCoord3 = coordonneeManager.findByIdManager(id);
      newCoord3.setAdresse("TMP2");
      etablissementManager.updateObjectManager(etab10, newCoord3, cat, u, false);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(etab10).size() == 4);

      // On vérifie que toutes associations ont étées enregistrées
      final Etablissement testInsert3 = etablissementManager.findByIdManager(idE4);
      assertNotNull(testInsert3.getCategorie());
      assertNotNull(testInsert3.getCoordonnee());
      assertTrue(testInsert3.getCoordonnee().getCoordonneeId() == id);
      assertTrue(testInsert3.getCoordonnee().getAdresse().equals("TMP2"));
      // On test les attributs
      assertTrue(testInsert3.getNom().equals("TOULON2"));
      assertTrue(testInsert3.getFiness().length() > 0);

      final Etablissement etab11 = etablissementManager.findByIdManager(idE4);
      etablissementManager.removeObjectManager(etab11, null, u);
      assertNull(etablissementManager.findByIdManager(idE4));
      assertNull(coordonneeManager.findByIdManager(id));
      assertTrue(getOperationManager().findByObjectManager(etab11).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(etab3);
      fs.add(etab4);
      fs.add(etab4b);
      fs.add(etab11);
      cleanUpFantomes(fs);

   }

   /**
    * Test la validation d'un établissement lors de sa création.
    * @param etablissement Etablissement à tester.
    */
   private void validationInsert(final Etablissement etablissement){
      final Utilisateur u = utilisateurDao.findById(1);
      boolean catchedInsert = false;
      final String[] nomValues = new String[] {"", "  ", null, "l#$$¤¤jd%", createOverLength(100), "GOOD"};
      final String[] finessValues = new String[] {"", " ", "%*k¤¤$$js", createOverLength(20), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < finessValues.length; j++){
            catchedInsert = false;
            try{
               etablissement.setNom(nomValues[i]);
               etablissement.setFiness(finessValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  final Coordonnee coord = new Coordonnee();
                  coord.setAdresse("test");
                  etablissementManager.createObjectManager(etablissement, coord, null, u);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedInsert = true;
                  assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedInsert);
            }
         }
      }
   }

   /**
    * Test la validation d'un établissement lors de sa modif.
    * @param etablissement Etablissement à tester.
    */
   private void validationUpdate(final Etablissement etablissement){
      final Utilisateur u = utilisateurDao.findById(1);
      boolean catchedUpdate = false;
      final String[] nomValues = new String[] {"", "  ", null, "l#$¤¤jd%", createOverLength(100), "GOOD"};
      final String[] finessValues = new String[] {"", " ", "%*k$¤¤js", createOverLength(20), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < finessValues.length; j++){
            catchedUpdate = false;
            try{
               etablissement.setNom(nomValues[i]);
               etablissement.setFiness(finessValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  final Coordonnee coord = new Coordonnee();
                  coord.setAdresse("test");
                  etablissementManager.updateObjectManager(etablissement, coord, null, u, false);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedUpdate = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedUpdate);
               assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
            }
         }
      }
   }

   @Test
   public void testArchivable(){
      final Utilisateur u = utilisateurDao.findById(1);
      // récup des objets associés
      final Service s1 = serviceManager.findByIdManager(1);
      final Service s3 = serviceManager.findByIdManager(3);
      final Specialite spec = specialiteManager.findByIdManager(1);
      final Titre titre = titreManager.findByIdManager(1);
      final Categorie cat = categorieDao.findById(1);

      // creation d'un établissement
      Etablissement etab = new Etablissement();
      etab.setNom("ETAB");
      etablissementManager.createObjectManager(etab, null, cat, u);
      final int idE = etab.getEtablissementId();

      // création d'une coordonnée
      final List<Coordonnee> coords = new ArrayList<>();
      final Coordonnee coord = new Coordonnee();
      coord.setAdresse("ADRESSE");
      coordonneeManager.createObjectManager(coord, null);
      coords.add(coord);

      // création d'un collab avec un service non archivé
      List<Service> services = new ArrayList<>();
      services.add(s1);
      Collaborateur c1 = new Collaborateur();
      c1.setNom("NOM1");
      collaborateurManager.createObjectManager(c1, titre, etab, spec, services, null, u);
      final int idColl1 = c1.getCollaborateurId();

      // maj du collaborateur pour que son prénom soit invalide
      // afin de vérifier que la validation ne se fait pas lors de
      // la cascade de l'archivage
      c1 = collaborateurManager.findByIdManager(idColl1);
      c1.setPrenom("      ");
      collaborateurManager.updateObjectManager(c1, titre, etab, spec, services, null, u, false);

      // création d'un collab avec un service archivé
      services = new ArrayList<>();
      services.add(s3);
      Collaborateur c2 = new Collaborateur();
      c2.setNom("NOM2");
      collaborateurManager.createObjectManager(c2, titre, etab, spec, services, coords, u);
      final int idColl2 = c2.getCollaborateurId();

      // création d'un service avec les 2 collabs
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      Service newS = new Service();
      newS.setNom("NOMS");
      final List<Collaborateur> collabs = new ArrayList<>();
      collabs.add(c1);
      collabs.add(c2);
      serviceManager.createObjectManager(newS, null, etab, collabs, u, false);
      final int idS1 = newS.getServiceId();

      // collaborateur isolé
      Collaborateur c4 = new Collaborateur();
      c4.setNom("NOM4");
      collaborateurManager.createObjectManager(c4, null, etab, null, null, null, u);

      assertTrue(collaborateurManager.findByEtablissementNoServiceManager(etab).size() == 1);

      // maj du service pour que son nom soit invalide
      // afin de vérifier que la validation ne se fait pas lors de
      // la cascade de l'archivage
      newS = serviceManager.findByIdManager(idS1);
      newS.setNom("          ");
      serviceManager.updateObjectManager(newS, null, etab, collabs, u, false, false);

      // création d'un service sans collabs
      final Service newS2 = new Service();
      newS2.setNom("NOMS2");
      serviceManager.createObjectManager(newS2, null, etab, null, u, false);
      final int idS2 = newS2.getServiceId();

      // archivage de l'établissement
      etab = etablissementManager.findByIdManager(idE);
      etab.setArchive(true);
      etablissementManager.updateObjectManager(etab, null, cat, u, true);

      // vérif : l'étab, les services et le collab c2 doivent etre archivés
      etab = etablissementManager.findByIdManager(idE);
      assertTrue(etab.getArchive());
      Service sTest1 = serviceManager.findByIdManager(idS1);
      assertNotNull(sTest1);
      assertTrue(sTest1.getArchive());
      assertTrue(serviceManager.getCollaborateursManager(sTest1).size() == 2);
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      assertFalse(c1.getArchive());
      assertTrue(c2.getArchive());
      assertTrue(collaborateurManager.getCoordonneesManager(c2).size() == 1);
      assertTrue(collaborateurManager.getServicesManager(c2).size() == 2);
      Service sTest2 = serviceManager.findByIdManager(idS2);
      assertNotNull(sTest2);
      assertTrue(sTest2.getArchive());

      assertTrue(collaborateurManager.findByEtablissementNoServiceManager(etab).size() == 1);
      c4 = collaborateurManager.findByEtablissementNoServiceManager(etab).get(0);
      assertTrue(c4.getArchive());

      // desarchivage de l'établissement
      etab = etablissementManager.findByIdManager(idE);
      etab.setArchive(false);
      etablissementManager.updateObjectManager(etab, null, cat, u, true);
      // vérif : l'étab, les services et le collab c2 doivent etre archivés
      etab = etablissementManager.findByIdManager(idE);
      assertFalse(etab.getArchive());
      sTest1 = serviceManager.findByIdManager(idS1);
      assertNotNull(sTest1);
      assertFalse(sTest1.getArchive());
      assertTrue(serviceManager.getCollaborateursManager(sTest1).size() == 2);
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      assertFalse(c1.getArchive());
      assertFalse(c2.getArchive());
      assertTrue(collaborateurManager.getCoordonneesManager(c2).size() == 1);
      assertTrue(collaborateurManager.getServicesManager(c2).size() == 2);
      sTest2 = serviceManager.findByIdManager(idS2);
      assertNotNull(sTest2);
      assertFalse(sTest2.getArchive());

      etablissementManager.removeObjectCascadeManager(etab, null, u);
      //serviceManager.removeObjectManager(sTest2, null, u);
      //serviceManager.removeObjectManager(sTest1, null, u);
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      collaborateurManager.removeObjectManager(c2, null, u);
      collaborateurManager.removeObjectManager(c1, null, u);
      coordonneeManager.removeObjectManager(coord);

      assertTrue(etablissementManager.findAllObjectsManager().size() == 4);
      assertTrue(collaborateurManager.findAllObjectsManager().size() == 6);
      assertTrue(serviceManager.findAllObjectsManager().size() == 4);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(etab);
      fs.add(sTest1);
      fs.add(sTest2);
      fs.addAll(collabs);
      fs.add(c4);
      cleanUpFantomes(fs);
   }

   /**
    * Teste la deletion en cascade depuis une service vers les collaborateurs.
    * La cascade est bloquée dans un premier temps car 
    * le collaborateur est référencé.
    * Le test de la deletion en cascade de collaborateur non referencé mais 
    * appartenant à plusieurs services est réalisé dans testIsArchive.
    */
   @Test
   public void testDeleteCascade(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Categorie cat = categorieDao.findById(1);
      final Plateforme pf = plateformeDao.findById(1);
      // creation d'un établissement
      final Etablissement etab = new Etablissement();
      etab.setNom("ETAB");
      etablissementManager.createObjectManager(etab, null, cat, u);
      // services
      final Service newS = new Service();
      newS.setNom("NOMS");
      serviceManager.createObjectManager(newS, null, etab, null, u, false);
      final int idS1 = newS.getServiceId();
      final Service newS2 = new Service();
      newS2.setNom("NOMS2");
      serviceManager.createObjectManager(newS2, null, etab, null, u, false);
      final int idS2 = newS2.getServiceId();
      final Service newS3 = new Service();
      newS3.setNom("NOMS3");
      serviceManager.createObjectManager(newS3, null, etab, null, u, false);
      final int idS3 = newS3.getServiceId();

      final Service sTest1 = serviceManager.findByIdManager(idS1);
      assertNotNull(sTest1);
      final Service sTest2 = serviceManager.findByIdManager(idS2);
      assertNotNull(sTest2);
      final Service sTest3 = serviceManager.findByIdManager(idS3);
      assertNotNull(sTest3);

      final List<Service> servs1 = new ArrayList<>();
      servs1.add(sTest1);
      final List<Service> servs2 = new ArrayList<>();
      servs2.add(sTest2);

      Collaborateur c1 = new Collaborateur();
      c1.setNom("NOM1");
      collaborateurManager.createObjectManager(c1, null, etab, null, servs1, null, u);
      Collaborateur c2 = new Collaborateur();
      c2.setNom("NOM2");
      collaborateurManager.createObjectManager(c2, null, etab, null, servs1, null, u);
      final List<Collaborateur> collabs = new ArrayList<>();
      collabs.add(c1);
      collabs.add(c2);
      final Collaborateur c3 = new Collaborateur();
      c3.setNom("NOM3");
      collaborateurManager.createObjectManager(c3, null, etab, null, servs2, null, u);
      final Collaborateur c4 = new Collaborateur();
      c4.setNom("NOM4");
      collaborateurManager.createObjectManager(c4, null, etab, null, null, null, u);

      assertTrue(serviceManager.getCollaborateursManager(sTest1).size() == 2);
      c1 = collaborateurManager.findByIdManager(c1.getCollaborateurId());
      c2 = collaborateurManager.findByIdManager(c2.getCollaborateurId());
      assertTrue(collaborateurManager.getServicesManager(c1).size() == 1);
      assertTrue(collaborateurManager.getServicesManager(c2).size() == 1);
      assertTrue(serviceManager.getCollaborateursManager(sTest2).size() == 1);
      assertTrue(serviceManager.getCollaborateursManager(sTest3).size() == 0);
      assertTrue(etablissementManager.getCollaborateursManager(etab).size() == 4);

      //referencement de collab2
      final Cession c = new Cession();
      final Cession ces1 = cessionManager.findByIdManager(1);
      c.setNumero("666");
      cessionManager.createObjectManager(c, ces1.getBanque(), ces1.getCessionType(), null, null, null, null, c2,
         ces1.getCessionStatut(), null, null, null, null, null, u, null, null);

      boolean catched = false;
      try{
         etablissementManager.removeObjectCascadeManager(etab, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      assertTrue(collaborateurManager.findAllObjectsManager().size() == 10);
      assertTrue(serviceManager.findAllObjectsManager().size() == 7);
      assertTrue(etablissementManager.findAllObjectsManager().size() == 5);

      cessionManager.updateObjectManager(c, c.getBanque(), c.getCessionType(), null, null, null, null, null, c.getCessionStatut(),
         null, null, null, null, null, null, null, u, null, null);

      //referencement de sTest2
      final Contrat ct = new Contrat();
      //Contrat ct1 = contratManager.findByIdManager(1);
      ct.setNumero("12");
      contratManager.createObjectManager(ct, null, sTest2, null, null, pf, u);

      catched = false;
      try{
         etablissementManager.removeObjectCascadeManager(etab, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("service.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      assertTrue(collaborateurManager.findAllObjectsManager().size() == 10);
      assertTrue(serviceManager.findAllObjectsManager().size() == 7);
      assertTrue(etablissementManager.findAllObjectsManager().size() == 5);

      //referencement de c4
      contratManager.updateObjectManager(ct, c4, null, null, null, pf, u);

      catched = false;
      try{
         etablissementManager.removeObjectCascadeManager(etab, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      assertTrue(collaborateurManager.findAllObjectsManager().size() == 10);
      assertTrue(serviceManager.findAllObjectsManager().size() == 7);
      assertTrue(etablissementManager.findAllObjectsManager().size() == 5);

      // dereferencement c4
      contratManager.updateObjectManager(ct, null, null, null, null, pf, u);

      etablissementManager.removeObjectCascadeManager(etab, null, u);

      assertTrue(collaborateurManager.findAllObjectsManager().size() == 6);
      assertTrue(serviceManager.findAllObjectsManager().size() == 4);
      testFindAll();

      cessionManager.removeObjectManager(c, null, u, null);
      assertTrue(cessionManager.findAllObjectsManager().size() == 4);
      contratManager.removeObjectManager(ct, null, u);
      assertTrue(contratManager.findAllObjectsManager().size() == 4);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(etab);
      fs.add(sTest1);
      fs.add(sTest2);
      fs.add(sTest3);
      fs.addAll(collabs);
      fs.add(c3);
      fs.add(c4);
      fs.add(c);
      fs.add(ct);
      cleanUpFantomes(fs);
   }

   @Test
   public void testIsReferencedObject(){
      boolean catched = false;
      final Utilisateur u = utilisateurDao.findById(1);

      Etablissement e = etablissementManager.findByIdManager(1);
      try{
         etablissementManager.removeObjectManager(e, null, u);
      }catch(final ObjectReferencedException oe){
         assertTrue(oe.getKey().equals("etablissement.deletion.isReferencedCascade"));
         assertTrue(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      e = etablissementManager.findByIdManager(2);
      try{
         etablissementManager.removeObjectManager(e, null, u);
      }catch(final ObjectReferencedException oe){
         assertTrue(oe.getKey().equals("etablissement.deletion.isReferencedCascade"));
         assertTrue(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      e = etablissementManager.findByIdManager(3);
      try{
         etablissementManager.removeObjectManager(e, null, u);
      }catch(final ObjectReferencedException oe){
         assertTrue(oe.getKey().equals("etablissement.deletion.isReferencedCascade"));
         assertTrue(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);

      catched = false;
      e = etablissementManager.findByIdManager(4);
      try{
         etablissementManager.removeObjectManager(e, null, u);
      }catch(final ObjectUsedException oe){
         assertTrue(oe.getKey().equals("etablissement.deletion.isUsedCascade"));
         assertTrue(oe.isCascadable());
         catched = true;
      }
      assertTrue(catched);
   }

   private void setUpFusionTest(){
      final Etablissement eA = new Etablissement();
      eA.setNom("ACTIF");
      final Etablissement eP = new Etablissement();
      eP.setNom("PASSIF");

      etablissementManager.createObjectManager(eA, null, null, utilisateurDao.findById(1));

      etablissementManager.createObjectManager(eP, null, null, utilisateurDao.findById(1));

      assertFalse(etablissementManager.findByNomLikeManager("ACTIF", true).isEmpty());
      assertFalse(etablissementManager.findByNomLikeManager("PASSIF", true).isEmpty());
   }

   private void cleanAfterFusion(final List<TKFantomableObject> fs){
      final Etablissement actif = etablissementManager.findByNomLikeManager("ACTIF", true).get(0);
      etablissementManager.removeObjectManager(actif, null, utilisateurDao.findById(1));
      fs.add(actif);
      cleanUpFantomes(fs);
      testFindAll();
   }

   @Rollback(false)
   @Test
   public void testFusionEtablissementVide() throws ParseException{

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Etablissement actif = etablissementManager.findByNomLikeManager("ACTIF", true).get(0);
      final Etablissement ePVide = etablissementManager.findByNomLikeManager("PASSIF", true).get(0);

      // nulls
      etablissementManager.fusionEtablissementManager(actif.getEtablissementId(), 0, null, null);
      etablissementManager.fusionEtablissementManager(0, ePVide.getEtablissementId(), null, null);

      assertFalse(etablissementManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertFalse(etablissementManager.findByNomLikeManager("ACTIF", true).isEmpty());

      etablissementManager.fusionEtablissementManager(actif.getEtablissementId(), ePVide.getEtablissementId(), "fusion test vide",
         u1);

      assertTrue(etablissementManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(operationDao.findByObjetIdEntiteAndOperationType(actif.getEtablissementId(),
         entiteDao.findByNom("Etablissement").get(0), operationTypeDao.findByNom("Fusion").get(0)).size() == 1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(ePVide);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionEtablisement(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Etablissement actif = etablissementManager.findByNomLikeManager("ACTIF", true).get(0);
      final Etablissement passif = etablissementManager.findByNomLikeManager("PASSIF", true).get(0);

      etablissementManager.fusionEtablissementManager(actif.getEtablissementId(), passif.getEtablissementId(), "test vide", u1);

      assertTrue(etablissementManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(getFantomeDao().findByNom("PASSIF").get(0).getCommentaires()
         .equals("fusion id: " + actif.getEtablissementId().toString() + " .test vide"));

      assertTrue(operationDao.findByObjetIdEntiteAndOperationType(actif.getEtablissementId(),
         entiteDao.findByNom("Etablissement").get(0), operationTypeDao.findByNom("Fusion").get(0)).size() == 1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionEtablissementsContrats(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Etablissement actif = etablissementManager.findByNomLikeManager("ACTIF", true).get(0);
      final Etablissement passif = etablissementManager.findByNomLikeManager("PASSIF", true).get(0);

      Contrat contrat = new Contrat();
      contrat.setNumero("CONTRAT");
      contratManager.createObjectManager(contrat, null, null, passif, null, plateformeDao.findById(1), u1);

      Contrat contrat2 = new Contrat();
      contrat2.setNumero("CONTRAT2");
      contratManager.createObjectManager(contrat2, null, null, passif, null, plateformeDao.findById(1), u1);

      assertFalse(contratDao.findByNumero("CONTRAT").isEmpty());
      assertFalse(contratDao.findByNumero("CONTRAT2").isEmpty());
      assertTrue(contratDao.findByEtablissement(passif).size() == 2);
      assertTrue(contratDao.findByEtablissement(actif).isEmpty());
      assertTrue(contratDao.findByEtablissement(passif).get(0).getNumero().equals("CONTRAT"));
      assertTrue(contratDao.findByEtablissement(passif).get(1).getNumero().equals("CONTRAT2"));

      etablissementManager.fusionEtablissementManager(actif.getEtablissementId(), passif.getEtablissementId(), "test contrat",
         u1);

      assertTrue(contratDao.findByEtablissement(passif).isEmpty());
      assertTrue(contratDao.findByEtablissement(actif).size() == 2);
      assertTrue(contratDao.findByEtablissement(actif).get(0).getNumero().equals("CONTRAT"));
      assertTrue(contratDao.findByEtablissement(actif).get(1).getNumero().equals("CONTRAT2"));

      // clean up
      contrat = contratDao.findByNumero("CONTRAT").get(0);
      contratManager.removeObjectManager(contrat, "removeTest", u1);
      assertTrue(contratDao.findByNumero("CONTRAT").isEmpty());
      contrat2 = contratDao.findByNumero("CONTRAT2").get(0);
      contratManager.removeObjectManager(contrat2, "removeTest", u1);
      assertTrue(contratDao.findByNumero("CONTRAT2").isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(contrat);
      fs.add(contrat2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionEtablissementsCollaborateursNoService(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Etablissement actif = etablissementManager.findByNomLikeManager("ACTIF", true).get(0);
      final Etablissement passif = etablissementManager.findByNomLikeManager("PASSIF", true).get(0);

      Collaborateur collaborateur = new Collaborateur();
      collaborateur.setNom("NOM1");
      collaborateur.setPrenom("PRENOM1");
      collaborateurManager.createObjectManager(collaborateur, null, passif, null, null, null, u1);

      Collaborateur collaborateur2 = new Collaborateur();
      collaborateur2.setNom("NOM2");
      collaborateur2.setPrenom("PRENOM2");
      collaborateurManager.createObjectManager(collaborateur2, null, passif, null, null, null, u1);

      assertFalse(collaborateurDao.findByNom("NOM1").isEmpty());
      assertFalse(collaborateurDao.findByNom("NOM2").isEmpty());
      assertTrue(collaborateurDao.findByEtablissement(passif).size() == 2);
      assertTrue(collaborateurDao.findByEtablissement(actif).isEmpty());
      assertTrue(collaborateurDao.findByEtablissement(passif).contains(collaborateur));
      assertTrue(collaborateurDao.findByEtablissement(passif).contains(collaborateur2));

      etablissementManager.fusionEtablissementManager(actif.getEtablissementId(), passif.getEtablissementId(), "test contrat",
         u1);

      assertTrue(collaborateurDao.findByEtablissement(passif).isEmpty());
      assertTrue(collaborateurDao.findByEtablissement(actif).size() == 2);
      assertTrue(collaborateurDao.findByEtablissement(actif).contains(collaborateur));
      assertTrue(collaborateurDao.findByEtablissement(actif).contains(collaborateur2));

      // clean up
      collaborateur = collaborateurDao.findByNom("NOM1").get(0);
      collaborateurManager.removeObjectManager(collaborateur, "removeTest", u1);
      assertTrue(collaborateurDao.findByNom("NOM1").isEmpty());
      collaborateur2 = collaborateurDao.findByNom("NOM2").get(0);
      collaborateurManager.removeObjectManager(collaborateur2, "removeTest", u1);
      assertTrue(collaborateurDao.findByNom("NOM2").isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(collaborateur);
      fs.add(collaborateur2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionEtablissementsService(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Etablissement actif = etablissementManager.findByNomLikeManager("ACTIF", true).get(0);
      final Etablissement passif = etablissementManager.findByNomLikeManager("PASSIF", true).get(0);

      Service service1 = new Service();
      service1.setNom("SERVICE1");
      serviceManager.createObjectManager(service1, null, passif, null, u1, false);
      final List<Service> srvs = new ArrayList<>();
      srvs.add(service1);

      Collaborateur collaborateur = new Collaborateur();
      collaborateur.setNom("NOM1");
      collaborateur.setPrenom("PRENOM1");
      collaborateurManager.createObjectManager(collaborateur, null, passif, null, srvs, null, u1);

      service1 = serviceManager.findByNomLikeManager("SERVICE1", true).get(0);

      Service service2 = new Service();
      service2.setNom("SERVICE2");
      serviceManager.createObjectManager(service2, null, passif, null, u1, false);
      final List<Service> srvs2 = new ArrayList<>();
      srvs2.add(service1);
      srvs2.add(service2);

      Collaborateur collaborateur2 = new Collaborateur();
      collaborateur2.setNom("NOM2");
      collaborateur2.setPrenom("PRENOM2");
      collaborateurManager.createObjectManager(collaborateur2, null, passif, null, srvs2, null, u1);

      Service service3 = new Service();
      service3.setNom("SERVICE1");
      serviceManager.createObjectManager(service3, null, actif, null, u1, false);
      final List<Service> srvs3 = new ArrayList<>();
      srvs3.add(service3);

      Collaborateur collaborateur3 = new Collaborateur();
      collaborateur3.setNom("NOM3");
      collaborateur3.setPrenom("PRENOM3");
      collaborateurManager.createObjectManager(collaborateur3, null, passif, null, srvs3, null, u1);

      assertTrue(serviceManager.findByNomLikeManager("SERVICE1", true).size() == 2);
      assertTrue(serviceManager.findByNomLikeManager("SERVICE2", true).size() == 1);
      assertTrue(etablissementManager.getServicesWithOrderManager(actif).size() == 1);
      assertTrue(collaborateurDao
         .findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(0).getServiceId()).size() == 1);
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(0).getServiceId())
            .contains(collaborateur3));
      assertTrue(etablissementManager.getServicesWithOrderManager(passif).size() == 2);
      assertTrue(collaborateurDao
         .findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(passif).get(0).getServiceId()).size() == 2);
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(passif).get(0).getServiceId())
            .contains(collaborateur));
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(passif).get(0).getServiceId())
            .contains(collaborateur2));
      assertTrue(collaborateurDao
         .findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(passif).get(1).getServiceId()).size() == 1);
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(passif).get(1).getServiceId())
            .contains(collaborateur2));

      etablissementManager.fusionEtablissementManager(actif.getEtablissementId(), passif.getEtablissementId(), "test services",
         u1);

      assertTrue(etablissementManager.getServicesWithOrderManager(passif).isEmpty());
      assertTrue(etablissementManager.getServicesWithOrderManager(actif).size() == 2);
      assertTrue(collaborateurDao
         .findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(0).getServiceId()).size() == 3);
      assertTrue(etablissementManager.getServicesWithOrderManager(actif).get(0).getNom().equals("SERVICE1"));
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(0).getServiceId())
            .contains(collaborateur));
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(0).getServiceId())
            .contains(collaborateur2));
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(0).getServiceId())
            .contains(collaborateur3));
      assertTrue(collaborateurDao
         .findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(1).getServiceId()).size() == 1);
      assertTrue(etablissementManager.getServicesWithOrderManager(actif).get(1).getNom().equals("SERVICE2"));
      assertTrue(collaborateurDao
         .findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(1).getServiceId()).size() == 1);
      assertTrue(
         collaborateurDao.findByServiceIdWithOrder(etablissementManager.getServicesWithOrderManager(actif).get(1).getServiceId())
            .contains(collaborateur2));

      // clean up
      collaborateur = collaborateurDao.findByNom("NOM1").get(0);
      collaborateur2 = collaborateurDao.findByNom("NOM2").get(0);
      collaborateur3 = collaborateurDao.findByNom("NOM3").get(0);

      service2 = etablissementManager.getServicesWithOrderManager(actif).get(0);
      service3 = etablissementManager.getServicesWithOrderManager(actif).get(1);
      serviceManager.removeObjectCascadeManager(service2, null, u1);
      serviceManager.removeObjectCascadeManager(service3, null, u1);
      assertTrue(serviceManager.findByNomLikeManager("SERVICE1", true).isEmpty());
      assertTrue(serviceManager.findByNomLikeManager("SERVICE2", true).isEmpty());
      assertTrue(collaborateurDao.findByNom("NOM1").isEmpty());
      assertTrue(collaborateurDao.findByNom("NOM2").isEmpty());
      assertTrue(collaborateurDao.findByNom("NOM3").isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(collaborateur);
      fs.add(collaborateur2);
      fs.add(collaborateur3);
      fs.add(service1);
      fs.add(service2);
      fs.add(service3);
      cleanAfterFusion(fs);
   }
}
