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
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.cession.CessionStatutDao;
import fr.aphp.tumorotek.dao.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.cession.ContratDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.LaboInterDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.EtablissementManager;
import fr.aphp.tumorotek.manager.context.ServiceManager;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.context.TitreManager;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager ServiceManager.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ServiceManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ServiceManager serviceManager;
   @Autowired
   private EtablissementManager etablissementManager;
   @Autowired
   private EtablissementDao etablissementDao;
   @Autowired
   private CoordonneeManager coordonneeManager;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private SpecialiteManager specialiteManager;
   @Autowired
   private TitreManager titreManager;
   @Autowired
   private CollaborateurManager collaborateurManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private PrelevementDao prelevementDao;
   @Autowired
   private LaboInterDao laboInterDao;
   @Autowired
   private BanqueManager banqueManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private ContratManager contratManager;
   @Autowired
   private ContratDao contratDao;
   @Autowired
   private ContexteDao contexteDao;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   private CessionTypeDao cessionTypeDao;
   @Autowired
   private CessionStatutDao cessionStatutDao;
   @Autowired
   private CessionManager cessionManager;
   @Autowired
   private NatureDao natureDao;
   @Autowired
   private MaladieDao maladieDao;
   @Autowired
   private ConsentTypeDao consentTypeDao;
   @Autowired
   private ConteneurDao conteneurDao;
   @Autowired
   private ConteneurManager conteneurManager;
   @Autowired
   private ConteneurTypeDao conteneurTypeDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private OperationTypeDao operationTypeDao;
   @Autowired
   private OperationDao operationDao;

   public ServiceManagerTest(){}

   @Test
   public void testFindById(){
      final Service service = serviceManager.findByIdManager(1);
      assertNotNull(service);
      assertTrue(service.getNom().equals("ANAPATH"));

      final Service serviceNull = serviceManager.findByIdManager(5);
      assertNull(serviceNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Service> list = serviceManager.findAllObjectsManager();
      assertTrue(list.size() == 4);
   }

   /**
    * Test la méthode findAllObjectsWithOrderManager.
    */
   @Test
   public void testFindAllObjectsWithOrderManager(){
      final List<Service> services = serviceManager.findAllObjectsWithOrderManager();
      assertTrue(services.size() == 4);
      assertTrue(services.get(1).getNom().equals("ANAPATH"));
      assertTrue(services.get(2).getNom().equals("CELLULO"));
      assertTrue(services.get(3).getNom().equals("HEMATO"));
      assertTrue(services.get(0).getNom().equals("ANAPATH SURESNES"));
   }

   /**
    * Test la méthode findAllActiveObjectsWithOrderManager.
    */
   @Test
   public void testFindAllActiveObjectsWithOrderManager(){
      final List<Service> list = serviceManager.findAllActiveObjectsWithOrderManager();
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("ANAPATH"));
      assertTrue(list.get(1).getNom().equals("ANAPATH SURESNES"));
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeExactManager(){
      List<Service> list = serviceManager.findByNomLikeManager("ANAPATH SURESNES", true);
      assertTrue(list.size() == 1);

      list = serviceManager.findByNomLikeManager("ANA", true);
      assertTrue(list.size() == 0);

      list = serviceManager.findByNomLikeManager("", true);
      assertTrue(list.size() == 0);

      list = serviceManager.findByNomLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      List<Service> list = serviceManager.findByNomLikeManager("ANAPATH SURESNES", false);
      assertTrue(list.size() == 1);

      list = serviceManager.findByNomLikeManager("ANA", false);
      assertTrue(list.size() == 2);

      list = serviceManager.findByNomLikeManager("", false);
      assertTrue(list.size() == 4);

      list = serviceManager.findByNomLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeBothSideManager.
    */
   @Test
   public void testFindByNomLikeBothSideManager(){
      List<Service> list = serviceManager.findByNomLikeBothSideManager("ANAPATH SURESNES");
      assertTrue(list.size() == 1);

      list = serviceManager.findByNomLikeBothSideManager("APATH");
      assertTrue(list.size() == 2);

      list = serviceManager.findByNomLikeBothSideManager("");
      assertTrue(list.size() == 4);

      list = serviceManager.findByNomLikeBothSideManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode getCollaborateursManager.
    */
   @Test
   public void testGetCollaborateursManager(){
      final Service service1 = serviceManager.findByIdManager(1);
      assertNotNull(service1);
      Set<Collaborateur> collabs = serviceManager.getCollaborateursManager(service1);
      assertTrue(collabs.size() == 2);

      final Service service2 = serviceManager.findByIdManager(4);
      assertNotNull(service2);
      collabs = serviceManager.getCollaborateursManager(service2);
      assertTrue(collabs.size() == 0);
   }

   /**
    * Test la méthode getCollaborateursWithOrderManager.
    */
   @Test
   public void testGetCollaborateursWithOrderManager(){
      final Service service1 = serviceManager.findByIdManager(1);
      assertNotNull(service1);
      List<Collaborateur> collabs = serviceManager.getCollaborateursWithOrderManager(service1);
      assertTrue(collabs.size() == 2);
      assertTrue(collabs.get(0).getNom().equals("DUFAY"));
      assertTrue(collabs.get(1).getNom().equals("VIAL"));

      final Service service2 = serviceManager.findByIdManager(4);
      assertNotNull(service2);
      collabs = serviceManager.getCollaborateursWithOrderManager(service2);
      assertTrue(collabs.size() == 0);

      final Service service3 = new Service();
      collabs = serviceManager.getCollaborateursWithOrderManager(service3);

      collabs = serviceManager.getCollaborateursWithOrderManager(null);
   }

   /**
    * Test la méthode getActiveCollaborateursWithOrderManager.
    */
   @Test
   public void testGetActiveCollaborateursWithOrderManager(){
      final Service service1 = serviceManager.findByIdManager(1);
      assertNotNull(service1);
      List<Collaborateur> collabs = serviceManager.getActiveCollaborateursWithOrderManager(service1);
      assertTrue(collabs.size() == 2);
      assertTrue(collabs.get(0).getNom().equals("DUFAY"));
      assertTrue(collabs.get(1).getNom().equals("VIAL"));

      final Service service2 = serviceManager.findByIdManager(3);
      assertNotNull(service2);
      collabs = serviceManager.getActiveCollaborateursWithOrderManager(service2);
      assertTrue(collabs.size() == 0);

      final Service service3 = new Service();
      collabs = serviceManager.getActiveCollaborateursWithOrderManager(service3);

      collabs = serviceManager.getActiveCollaborateursWithOrderManager(null);

      final Service service4 = serviceManager.findByIdManager(2);
      assertNotNull(service4);
      collabs = serviceManager.getActiveCollaborateursWithOrderManager(service4);
      assertTrue(collabs.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "ANAPATH";
      final String nom2 = "AUTRE";
      final Etablissement etab1 = etablissementManager.findByIdManager(1);
      final Etablissement etab2 = etablissementManager.findByIdManager(2);

      final Service serv1 = new Service();
      serv1.setNom(nom1);
      serv1.setEtablissement(etab1);
      assertTrue(serviceManager.findDoublonManager(serv1));

      serv1.setNom(nom2);
      assertFalse(serviceManager.findDoublonManager(serv1));

      serv1.setNom(nom1);
      serv1.setEtablissement(etab2);
      assertFalse(serviceManager.findDoublonManager(serv1));

      serv1.setNom(nom2);
      assertFalse(serviceManager.findDoublonManager(serv1));

      final Service serv2 = serviceManager.findByIdManager(2);
      assertFalse(serviceManager.findDoublonManager(serv2));

      serv2.setNom(nom1);
      assertTrue(serviceManager.findDoublonManager(serv2));

      serv2.setNom(nom2);
      serv2.setEtablissement(etab2);
      assertFalse(serviceManager.findDoublonManager(serv2));

   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      // Récupération des objets associés à un Etablissement
      final Etablissement etab = etablissementManager.findByIdManager(1);
      final Collaborateur collab1 = collaborateurDao.findById(1).orElse(null);
      final Collaborateur collab2 = collaborateurDao.findById(2).orElse(null);
      final Collaborateur collab3 = collaborateurDao.findById(3).orElse(null);
      List<Collaborateur> collabs = new ArrayList<>();
      Set<Collaborateur> collabsTest = new HashSet<>();

      // Insertion
      final Service serv1 = new Service();
      serv1.setNom("ANAPATH");
      Boolean catched = false;
      Coordonnee coordExcp = new Coordonnee();
      // on test l'insertion avec l'etab null
      try{
         serviceManager.createObjectManager(serv1, coordExcp, null, null, u, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      catched = false;
      coordExcp = new Coordonnee();
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         serviceManager.createObjectManager(serv1, coordExcp, etab, null, u, false);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);

      // Test de la validation lors de la création
      validationInsert(serv1);
      assertTrue(serviceManager.findAllObjectsManager().size() == 4);
      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      serv1.setNom("TEST");
      serviceManager.createObjectManager(serv1, null, etab, null, u, false);
      final int idS1 = serv1.getServiceId();
      assertTrue(getOperationManager().findByObjectManager(serv1).size() == 1);

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies
      Service serv2 = new Service();
      serv2.setNom("TEST AN OTHER");
      collabs.add(collab1);
      collabs.add(collab2);
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul service
      final Coordonnee newCoord1 = new Coordonnee();
      newCoord1.setAdresse("TMP");
      serviceManager.createObjectManager(serv2, newCoord1, etab, collabs, u, false);
      int id = newCoord1.getCoordonneeId();
      final int idS2 = serv2.getServiceId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(serv2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      Service testInsert = serviceManager.findByIdManager(idS2);
      assertNotNull(testInsert.getEtablissement());
      assertNotNull(testInsert.getCoordonnee());
      assertTrue(testInsert.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert.getNom().length() > 0);
      collabsTest = serviceManager.getCollaborateursManager(testInsert);
      assertTrue(collabsTest.size() == 2);

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies
      serv2 = new Service();
      serv2.setNom("TEST AN OTHER2");
      collabs.add(collab1);
      collabs.add(collab2);
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul service
      final Coordonnee newCoordbis = new Coordonnee();
      newCoordbis.setAdresse("TMP");
      coordonneeManager.createObjectManager(newCoordbis, null);
      final int id2 = newCoordbis.getCoordonneeId();
      serviceManager.createObjectManager(serv2, newCoordbis, etab, collabs, u, false);
      final int idS3 = serv2.getServiceId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(serv2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert = serviceManager.findByIdManager(idS3);
      assertNotNull(testInsert.getEtablissement());
      assertNotNull(testInsert.getCoordonnee());
      assertTrue(testInsert.getCoordonnee().getCoordonneeId() == id2);
      // On test les attributs
      assertTrue(testInsert.getNom().length() > 0);
      collabsTest = serviceManager.getCollaborateursManager(testInsert);
      assertTrue(collabsTest.size() == 2);

      // Suppression
      final Service serv3 = serviceManager.findByIdManager(idS1);
      serviceManager.removeObjectManager(serv3, null, u);
      assertTrue(getOperationManager().findByObjectManager(serv3).size() == 0);
      final Service serv4 = serviceManager.findByIdManager(idS2);
      serviceManager.removeObjectCascadeManager(serv4, null, u);
      assertTrue(getOperationManager().findByObjectManager(serv4).size() == 0);
      final Service serv4b = serviceManager.findByIdManager(idS3);
      serviceManager.removeObjectCascadeManager(serv4b, null, u);
      assertTrue(getOperationManager().findByObjectManager(serv4b).size() == 0);
      assertNull(coordonneeManager.findByIdManager(id));
      assertNull(coordonneeManager.findByIdManager(id2));

      // Update
      final String nomUpdated2 = "ANAPATH";
      final Service serv5 = new Service();
      serv5.setNom("CANCER");
      serviceManager.createObjectManager(serv5, null, etab, null, u, false);
      final int idS4 = serv5.getServiceId();
      assertTrue(getOperationManager().findByObjectManager(serv5).size() == 1);

      Boolean catchedUpdate = false;
      coordExcp = new Coordonnee();
      coordExcp.setAdresse("Excep");
      // on test l'insertion avec l'etab null
      final Service serv6 = serviceManager.findByIdManager(idS4);
      try{
         serviceManager.updateObjectManager(serv6, coordExcp, null, null, u, false, true);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      catched = false;
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      serv6.setNom(nomUpdated2);
      coordExcp = new Coordonnee();
      coordExcp.setAdresse("Excep");
      try{
         serviceManager.updateObjectManager(serv6, coordExcp, etab, null, u, false, true);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
      catchedUpdate = false;

      // On test la validation lors d'un update
      final Service serv7 = serviceManager.findByIdManager(idS4);
      validationUpdate(serv7);

      // On teste une mise à jour valide avec les assos a null mais
      // sans la validation
      serv7.setNom("*****$#¤¤@-_");
      serviceManager.updateObjectManager(serv7, null, etab, null, u, false, false);
      final Service serv8 = serviceManager.findByIdManager(idS4);
      assertTrue(serv8.getNom().equals("*****$#¤¤@-_"));
      assertTrue(getOperationManager().findByObjectManager(serv8).size() == 2);

      // On teste une mise à jour valide avec les assos (avec une nouvelle
      // coordonnée)
      Service serv9 = serviceManager.findByIdManager(idS4);
      serv9.setNom("CANCA");
      collabs = new ArrayList<>();
      collabs.add(collab1);
      collabs.add(collab2);
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul service
      final Coordonnee newCoord2 = new Coordonnee();
      newCoord2.setAdresse("TMP");
      serviceManager.updateObjectManager(serv9, newCoord2, etab, collabs, u, false, true);
      id = newCoord2.getCoordonneeId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(serv9).size() == 3);

      // On vérifie que toutes associations ont étées enregistrées
      Service testInsert2 = serviceManager.findByIdManager(idS4);
      assertNotNull(testInsert2.getEtablissement());
      assertNotNull(testInsert2.getCoordonnee());
      assertTrue(testInsert2.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert2.getNom().length() > 0);
      assertTrue(serviceManager.getCollaborateursManager(testInsert2).size() == 2);

      // On teste une mise à jour valide avec les assos avec une
      // coordonnée deja existante
      serv9 = serviceManager.findByIdManager(idS4);
      serv9.setNom("CANCA");
      collabs = new ArrayList<>();
      collabs.add(collab1);
      collabs.add(collab2);
      // Création de la nouvelle coordonnée : une coordonnée ne peut
      // etre associée qu'à un seul service
      final Coordonnee newCoord3 = coordonneeManager.findByIdManager(id);
      newCoord3.setAdresse("TEST");
      serviceManager.updateObjectManager(serv9, newCoord3, etab, collabs, u, false, true);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 6);
      assertTrue(getOperationManager().findByObjectManager(serv9).size() == 4);

      // On vérifie que toutes associations ont étées enregistrées
      testInsert2 = serviceManager.findByIdManager(idS4);
      assertNotNull(testInsert2.getEtablissement());
      assertNotNull(testInsert2.getCoordonnee());
      assertTrue(testInsert2.getCoordonnee().getAdresse().equals("TEST"));
      assertTrue(testInsert2.getCoordonnee().getCoordonneeId() == id);
      // On test les attributs
      assertTrue(testInsert2.getNom().length() > 0);
      assertTrue(serviceManager.getCollaborateursManager(testInsert2).size() == 2);

      // On test la modif d'une liste de collabs
      final Service serv10 = serviceManager.findByIdManager(idS4);
      collabs = new ArrayList<>();
      collabs.add(collab3);
      serviceManager.updateObjectManager(serv10, newCoord2, etab, collabs, u, false, true);
      assertTrue(getOperationManager().findByObjectManager(serv10).size() == 5);
      // On vérifie que les associations de services ont étée modifiées
      final Service testInsert3 = serviceManager.findByIdManager(idS4);
      collabsTest = serviceManager.getCollaborateursManager(testInsert3);
      assertTrue(collabsTest.size() == 1);
      final Iterator<Collaborateur> it = collabsTest.iterator();
      while(it.hasNext()){
         final Collaborateur tmp = it.next();
         assertTrue(tmp.getCollaborateurId() == 3);
      }

      final Service serv11 = serviceManager.findByIdManager(idS4);
      serviceManager.removeObjectCascadeManager(serv11, null, u);
      assertNull(serviceManager.findByIdManager(idS4));
      assertNull(coordonneeManager.findByIdManager(id));
      assertTrue(getOperationManager().findByObjectManager(serv11).size() == 0);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(serv3);
      fs.add(serv4);
      fs.add(serv4b);
      fs.add(serv11);
      cleanUpFantomes(fs);
   }

   /**
    * Test la validation d'un service lors de sa création.
    * @param service Service à tester.
    */
   private void validationInsert(final Service service){
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      final Etablissement etab = etablissementManager.findByIdManager(1);
      boolean catchedInsert = false;
      final String[] nomValues = new String[] {"", "  ", null, "l#¤¤$d%", createOverLength(100), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         catchedInsert = false;
         try{
            final Coordonnee coord = new Coordonnee();
            coord.setAdresse("test");
            service.setNom(nomValues[i]);
            if(i != 5){ //car creation valide
               serviceManager.createObjectManager(service, coord, etab, null, u, false);
            }
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
               assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
            }
         }
         if(i != 5){
            assertTrue(catchedInsert);
         }
      }
      // On teste une insertion avec un attribut nom vide

   }

   /**
    * Test la validation d'un service lors de sa modif.
    * @param service Service à tester.
    */
   private void validationUpdate(final Service service){
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      final Etablissement etab = etablissementManager.findByIdManager(1);
      boolean catchedUpdate = false;
      final String[] nomValues = new String[] {"", "  ", null, "l#$$¤¤jd%", createOverLength(100), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         catchedUpdate = false;
         try{
            final Coordonnee coord = new Coordonnee();
            coord.setAdresse("test");
            service.setNom(nomValues[i]);
            if(i != 5){ //car creation valide
               serviceManager.updateObjectManager(service, coord, etab, null, u, false, true);
            }
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedUpdate = true;
               assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);
            }
         }
         if(i != 5){
            assertTrue(catchedUpdate);
         }
      }
      // On teste une insertion avec un attribut nom vide

   }

   @Test
   public void testArchivage(){
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      // récup des objets associés
      final Service s1 = serviceManager.findByIdManager(1);
      final Service s3 = serviceManager.findByIdManager(3);
      final Etablissement etab = etablissementManager.findByIdManager(1);
      final Specialite spec = specialiteManager.findByIdManager(1);
      final Titre titre = titreManager.findByIdManager(1);

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

      // création d'un service archivé
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      final Service newS = new Service();
      newS.setNom("NOMS");
      newS.setArchive(true);
      final List<Collaborateur> collabs = new ArrayList<>();
      collabs.add(c1);
      collabs.add(c2);
      serviceManager.createObjectManager(newS, null, etab, collabs, u, true);
      final int idS1 = newS.getServiceId();

      // vérif : le service et le cooab c2 doivent etre archivés
      final Service sTest1 = serviceManager.findByIdManager(idS1);
      assertNotNull(sTest1);
      assertTrue(sTest1.getArchive());
      assertTrue(serviceManager.getCollaborateursManager(sTest1).size() == 2);
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      assertFalse(c1.getArchive());
      assertTrue(c2.getArchive());
      assertTrue(collaborateurManager.getCoordonneesManager(c2).size() == 1);
      assertTrue(collaborateurManager.getServicesManager(c2).size() == 2);

      sTest1.setArchive(false);
      serviceManager.updateObjectManager(sTest1, null, etab, collabs, u, true, true);

      // desarchivage
      final Service sTest2 = serviceManager.findByIdManager(idS1);
      assertNotNull(sTest2);
      assertFalse(sTest2.getArchive());
      c1 = collaborateurManager.findByIdManager(idColl1);
      assertFalse(c1.getArchive());
      c2 = collaborateurManager.findByIdManager(idColl2);
      assertFalse(c2.getArchive());

      serviceManager.removeObjectCascadeManager(sTest2, null, u);
      c1 = collaborateurManager.findByIdManager(idColl1);
      c2 = collaborateurManager.findByIdManager(idColl2);
      collaborateurManager.removeObjectManager(c2, null, u);
      collaborateurManager.removeObjectManager(c1, null, u);
      coordonneeManager.removeObjectManager(coord);

      assertTrue(collaborateurManager.findAllObjectsManager().size() == 6);
      assertTrue(serviceManager.findAllObjectsManager().size() == 4);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 5);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(sTest2);
      fs.addAll(collabs);
      cleanUpFantomes(fs);
   }

   @Test
   public void testIsObjectReferencedManager(){
      Service s = serviceManager.findByIdManager(1);
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      boolean catched = false;
      try{
         serviceManager.removeObjectManager(s, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("service.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      s = serviceManager.findByIdManager(2);
      try{
         serviceManager.removeObjectManager(s, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("service.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      s = serviceManager.findByIdManager(3);
      try{
         serviceManager.removeObjectManager(s, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("service.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      s = serviceManager.findByIdManager(4);
      try{
         serviceManager.removeObjectManager(s, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("service.deletion.isReferencedCascade"));
      }
      assertTrue(catched);
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
      final Utilisateur u = utilisateurDao.findById(1).orElse(null);
      final Etablissement etab = etablissementManager.findByIdManager(1);
      //		Specialite spec = specialiteManager.findByIdManager(1);
      //		Titre titre = titreManager.findByIdManager(1);

      final Service newS = new Service();
      newS.setNom("NOMS");
      serviceManager.createObjectManager(newS, null, etab, null, u, false);
      final int idS1 = newS.getServiceId();

      final Service sTest1 = serviceManager.findByIdManager(idS1);
      assertNotNull(sTest1);
      final List<Service> servs = new ArrayList<>();
      servs.add(sTest1);

      Collaborateur c1 = new Collaborateur();
      c1.setNom("NOM1");
      collaborateurManager.createObjectManager(c1, null, null, null, servs, null, u);
      Collaborateur c2 = new Collaborateur();
      c2.setNom("NOM2");
      collaborateurManager.createObjectManager(c2, null, null, null, servs, null, u);
      final List<Collaborateur> collabs = new ArrayList<>();
      collabs.add(c1);
      collabs.add(c2);

      assertTrue(serviceManager.getCollaborateursManager(sTest1).size() == 2);
      c1 = collaborateurManager.findByIdManager(c1.getCollaborateurId());
      c2 = collaborateurManager.findByIdManager(c2.getCollaborateurId());
      assertTrue(collaborateurManager.getServicesManager(c1).size() == 1);
      assertTrue(collaborateurManager.getServicesManager(c2).size() == 1);

      //referencement de collab2
      final Prelevement p = new Prelevement();
      final Prelevement p1 = prelevementManager.findByIdManager(1);
      p.setCode("PrelPourService");
      prelevementManager.createObjectManager(p, p1.getBanque(), p1.getNature(), null, p1.getConsentType(), c2, null, null, null,
         null, null, null, null, null, null, null, u, false, "/tmp/", false);

      boolean catched = false;
      try{
         serviceManager.removeObjectCascadeManager(sTest1, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      prelevementManager.updateObjectManager(p, p.getBanque(), p.getNature(), null, p.getConsentType(), null, null, null, null,
         null, null, null, null, null, null, null, null, null, u, null, false, "/tmp/", false);

      serviceManager.removeObjectCascadeManager(sTest1, null, u);

      assertTrue(collaborateurManager.findAllObjectsManager().size() == 6);
      assertTrue(serviceManager.findAllObjectsManager().size() == 4);

      prelevementManager.removeObjectManager(p, null, u, null);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 5);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(sTest1);
      fs.addAll(collabs);
      fs.add(p);
      cleanUpFantomes(fs);
   }

   private void setUpFusionTest(){
      final Service sA = new Service();
      sA.setNom("ACTIF");
      final Service sP = new Service();
      sP.setNom("PASSIF");

      serviceManager.createObjectManager(sA, null, etablissementDao.findById(3), null, utilisateurDao.findById(1), false).orElse(null);

      serviceManager.createObjectManager(sP, null, etablissementDao.findById(2), null, utilisateurDao.findById(1), false).orElse(null);

      assertFalse(serviceManager.findByNomLikeManager("ACTIF", true).isEmpty());
      assertFalse(serviceManager.findByNomLikeManager("PASSIF", true).isEmpty());
   }

   private void cleanAfterFusion(final List<TKFantomableObject> fs){
      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      serviceManager.removeObjectManager(actif, null, utilisateurDao.findById(1)).orElse(null);
      fs.add(actif);
      cleanUpFantomes(fs);
      testFindAll();
   }

   @Rollback(false)
   @Test
   public void testFusionServiceManagerVide() throws ParseException{

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service sPVide = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      // nulls
      serviceManager.fusionServiceManager(actif.getServiceId(), 0, null, null);
      serviceManager.fusionServiceManager(0, sPVide.getServiceId(), null, null);

      assertFalse(serviceManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertFalse(serviceManager.findByNomLikeManager("ACTIF", true).isEmpty());

      serviceManager.fusionServiceManager(actif.getServiceId(), sPVide.getServiceId(), "fusion test vide", u1);

      assertTrue(serviceManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(operationDao.findByObjetIdEntiteAndOperationType(actif.getServiceId(), entiteDao.findByNom("Service").get(0),
         operationTypeDao.findByNom("Fusion").get(0)).size() == 1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(sPVide);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionServices(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test vide", u1);

      assertTrue(serviceManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(getFantomeDao().findByNom("PASSIF").get(0).getCommentaires()
         .equals("fusion id: " + actif.getServiceId().toString() + " .test vide"));

      assertTrue(operationDao.findByObjetIdEntiteAndOperationType(actif.getServiceId(), entiteDao.findByNom("Service").get(0),
         operationTypeDao.findByNom("Fusion").get(0)).size() == 1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionServicesWithBanques(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      final Banque b = new Banque();
      b.setNom("BANK");
      b.setArchive(false);
      banqueManager.createOrUpdateObjectManager(b, plateformeDao.findById(1), contexteDao.findById(1).orElse(null), passif, null, null, null,
         null, null, null, null, null, null, null, null, null, u1, null, "creation", "/tmp/");

      final Banque b2 = new Banque();
      b2.setNom("BANK2");
      b2.setArchive(false);
      banqueManager.createOrUpdateObjectManager(b2, plateformeDao.findById(1), contexteDao.findById(1).orElse(null), passif, null, null, null,
         null, null, null, null, null, null, null, null, null, u1, null, "creation", "/tmp/");

      assertTrue(banqueDao.findByProprietaire(passif).size() == 2);
      assertTrue(banqueDao.findByProprietaire(actif).isEmpty());
      assertTrue(banqueDao.findByProprietaire(passif).get(0).getNom().equals("BANK"));
      assertTrue(banqueDao.findByProprietaire(passif).get(1).getNom().equals("BANK2"));

      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test banque", u1);

      assertTrue(banqueDao.findByProprietaire(passif).isEmpty());
      assertTrue(banqueDao.findByProprietaire(actif).size() == 2);
      assertTrue(banqueDao.findByProprietaire(actif).get(0).getNom().equals("BANK"));
      assertTrue(banqueDao.findByProprietaire(actif).get(1).getNom().equals("BANK2"));

      banqueManager.removeObjectManager(banqueDao.findByNom("BANK").get(0), null, u1, "/tmp/", true);
      assertTrue(banqueDao.findByNom("BANK").isEmpty());
      banqueManager.removeObjectManager(banqueDao.findByNom("BANK2").get(0), null, u1, "/tmp/", true);
      assertTrue(banqueDao.findByNom("BANK2").isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(b);
      fs.add(b2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionServicesContrats(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      Contrat contrat = new Contrat();
      contrat.setNumero("CONTRAT");
      contratManager.createObjectManager(contrat, null, passif, null, null, plateformeDao.findById(1), u1).orElse(null);

      Contrat contrat2 = new Contrat();
      contrat2.setNumero("CONTRAT2");
      contratManager.createObjectManager(contrat2, null, passif, null, null, plateformeDao.findById(1), u1).orElse(null);

      assertFalse(contratDao.findByNumero("CONTRAT").isEmpty());
      assertFalse(contratDao.findByNumero("CONTRAT2").isEmpty());
      assertTrue(contratDao.findByService(passif).size() == 2);
      assertTrue(contratDao.findByService(actif).isEmpty());
      assertTrue(contratDao.findByService(passif).get(0).getNumero().equals("CONTRAT"));
      assertTrue(contratDao.findByService(passif).get(1).getNumero().equals("CONTRAT2"));

      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test contrat", u1);

      assertTrue(contratDao.findByService(passif).isEmpty());
      assertTrue(contratDao.findByService(actif).size() == 2);
      assertTrue(contratDao.findByService(actif).get(0).getNumero().equals("CONTRAT"));
      assertTrue(contratDao.findByService(actif).get(1).getNumero().equals("CONTRAT2"));

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
   public void testFusionServicesCoreObjects(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      Prelevement prelevement = new Prelevement();
      prelevement.setArchive(false);
      prelevement.setCode("PRELEVEMENTEST");

      final LaboInter laboInter = new LaboInter();
      laboInter.setOrdre(1);
      laboInter.setService(passif);
      laboInter.setDateArrivee(Calendar.getInstance());
      final List<LaboInter> labs = new ArrayList<>();
      labs.add(laboInter);

      prelevementManager.createObjectManager(prelevement, banqueDao.findById(1), natureDao.findById(1), maladieDao.findById(1).orElse(null),
         consentTypeDao.findById(1).orElse(null), null, passif, null, null, null, null, null, null, labs, null, null, u1, false, "/tmp/",
         false);

      Prelevement prelevement2 = new Prelevement();
      prelevement2.setArchive(false);
      prelevement2.setCode("PRELEVEMENTEST2");

      final LaboInter laboInter2 = new LaboInter();
      laboInter2.setOrdre(1);
      laboInter2.setService(passif);
      laboInter2.setDateArrivee(Calendar.getInstance());
      final List<LaboInter> labs2 = new ArrayList<>();
      labs2.add(laboInter2);

      prelevementManager.createObjectManager(prelevement2, banqueDao.findById(1), natureDao.findById(1), maladieDao.findById(1).orElse(null),
         consentTypeDao.findById(1).orElse(null), null, passif, null, null, null, null, null, null, labs2, null, null, u1, false, "/tmp/",
         false);

      assertTrue(prelevementDao.findByService(passif).size() == 2);
      assertTrue(prelevementDao.findByService(passif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST")));
      assertTrue(prelevementDao.findByService(passif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST2")));
      assertTrue(prelevementDao.findByService(actif).isEmpty());
      assertTrue(laboInterDao.findByService(passif).size() == 2);
      assertTrue(laboInterDao.findByService(passif).contains(laboInter));
      assertTrue(laboInterDao.findByService(passif).contains(laboInter2));
      assertTrue(laboInterDao.findByService(actif).isEmpty());

      // fusion
      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test prel et labo", u1);

      assertTrue(prelevementDao.findByService(actif).size() == 2);
      assertTrue(prelevementDao.findByService(actif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST")));
      assertTrue(prelevementDao.findByService(actif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST2")));
      assertTrue(prelevementDao.findByService(passif).isEmpty());
      assertTrue(laboInterDao.findByService(actif).size() == 2);
      assertTrue(laboInterDao.findByService(actif).contains(laboInter));
      assertTrue(laboInterDao.findByService(actif).contains(laboInter2));
      assertTrue(laboInterDao.findByService(passif).isEmpty());

      // clean up
      prelevement = prelevementManager.findByCodeLikeManager("PRELEVEMENTEST", true).get(0);
      prelevementManager.removeObjectCascadeManager(prelevement, "remove prel Preleveur fusion", u1, null);
      assertTrue(prelevementDao.findByCode("PRELEVEMENTEST").isEmpty());
      prelevement2 = prelevementManager.findByCodeLikeManager("PRELEVEMENTEST2", true).get(0);
      prelevementManager.removeObjectCascadeManager(prelevement2, "remove prel Preleveur fusion", u1, null);
      assertTrue(prelevementDao.findByCode("PRELEVEMENTEST2").isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(prelevement);
      fs.add(prelevement2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionServicesCession(){
      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      Cession cess = new Cession();
      cess.setNumero("100");
      cess.setArchive(false);

      cessionManager.createObjectManager(cess, banqueDao.findById(1), cessionTypeDao.findById(1).orElse(null), null, null, null, passif, null,
         cessionStatutDao.findById(1), null, null, null, null, null, u1, null, "/tmp/").orElse(null);

      Cession cess2 = new Cession();
      cess2.setNumero("1002");
      cess2.setArchive(false);

      cessionManager.createObjectManager(cess2, banqueDao.findById(1), cessionTypeDao.findById(1).orElse(null), null, null, null, passif, null,
         cessionStatutDao.findById(1), null, null, null, null, null, u1, null, "/tmp/").orElse(null);

      assertTrue(cessionDao.findByNumero("100").get(0).getServiceDest().equals(passif));
      assertTrue(cessionDao.findByNumero("1002").get(0).getServiceDest().equals(passif));
      assertTrue(cessionDao.findByServiceDest(passif).size() == 2);
      assertTrue(cessionDao.findByServiceDest(actif).isEmpty());

      // fusion
      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test cession", u1);

      cess = cessionManager.findByNumeroLikeManager("100", true).get(0);
      cess2 = cessionManager.findByNumeroLikeManager("1002", true).get(0);

      assertTrue(cess.getServiceDest().equals(actif));
      assertTrue(cess2.getServiceDest().equals(actif));
      assertTrue(cessionDao.findByServiceDest(passif).isEmpty());
      assertTrue(cessionDao.findByServiceDest(actif).size() == 2);
      assertTrue(cessionDao.findByServiceDest(actif).contains(cess));
      assertTrue(cessionDao.findByServiceDest(actif).contains(cess2));

      cessionManager.removeObjectManager(cess, "remove fusion", u1, null);
      assertTrue(cessionManager.findByNumeroLikeManager("100", true).isEmpty());
      cessionManager.removeObjectManager(cess2, "remove fusion", u1, null);
      assertTrue(cessionManager.findByNumeroLikeManager("1002", true).isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(cess);
      fs.add(cess2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionServicesConteneur(){
      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      Conteneur conteneur = new Conteneur();

      conteneur.setCode("TestF");
      conteneur.setNom("TestNomFusion");
      conteneur.setArchive(false);

      conteneurManager.createObjectManager(conteneur, conteneurTypeDao.findById(1).orElse(null), passif, null, null, u1,
         plateformeDao.findById(1)).orElse(null);

      Conteneur conteneur2 = new Conteneur();

      conteneur2.setCode("Test2");
      conteneur2.setNom("TestNomFusion2");
      conteneur2.setArchive(false);

      conteneurManager.createObjectManager(conteneur2, conteneurTypeDao.findById(1).orElse(null), passif, null, null, u1,
         plateformeDao.findById(1)).orElse(null);

      assertTrue(conteneurDao.findByService(passif).size() == 2);
      assertTrue(conteneurDao.findByService(actif).isEmpty());
      assertTrue(conteneurDao.findByService(passif).contains(conteneur));
      assertTrue(conteneurDao.findByService(passif).contains(conteneur2));

      // fusion
      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test conteneur", u1);

      assertTrue(conteneurDao.findByService(actif).size() == 2);
      assertTrue(conteneurDao.findByService(passif).isEmpty());
      assertTrue(conteneurDao.findByService(actif).contains(conteneur));
      assertTrue(conteneurDao.findByService(actif).contains(conteneur2));

      conteneur = conteneurDao.findByService(actif).get(0);
      conteneur2 = conteneurDao.findByService(actif).get(1);

      conteneurManager.removeObjectManager(conteneur, null, u1);
      assertTrue(conteneurDao.findById(conteneur.getConteneurId()) == null).orElse(null);
      conteneurManager.removeObjectManager(conteneur2, null, u1);
      assertTrue(conteneurDao.findById(conteneur2.getConteneurId()) == null).orElse(null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(conteneur);
      fs.add(conteneur2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionServicesCollaborateur(){
      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1).orElse(null);

      final Service actif = serviceManager.findByNomLikeManager("ACTIF", true).get(0);
      final Service passif = serviceManager.findByNomLikeManager("PASSIF", true).get(0);

      final Collaborateur collPassif = new Collaborateur();
      collPassif.setNom("COLLFUSIONPASSIF");
      final Collaborateur collActif = new Collaborateur();
      collActif.setNom("COLLFUSIONACTIF");
      final Collaborateur collCommun = collaborateurDao.findById(3).orElse(null);

      final List<Service> servicesPassifSet = new ArrayList<>();
      servicesPassifSet.add(passif);
      final List<Service> servicesActifSet = new ArrayList<>();
      servicesActifSet.add(actif);
      final Set<Service> servicesX2Set = collaborateurManager.getServicesManager(collCommun);
      servicesX2Set.add(passif);
      servicesX2Set.add(actif);

      collaborateurManager.createObjectManager(collPassif, null, null, null, servicesPassifSet, new ArrayList<Coordonnee>(), u1);
      collaborateurManager.createObjectManager(collActif, null, null, null, servicesActifSet, new ArrayList<Coordonnee>(), u1);
      collaborateurManager.updateObjectManager(collCommun, collCommun.getTitre(), collCommun.getEtablissement(),
         collCommun.getSpecialite(), new ArrayList<>(servicesX2Set),
         new ArrayList<>(collaborateurManager.getCoordonneesManager(collCommun)), u1, false);

      assertTrue(serviceManager.getCollaborateursManager(actif).size() == 2);
      assertTrue(serviceManager.getCollaborateursManager(actif).contains(collActif));
      assertTrue(serviceManager.getCollaborateursManager(actif).contains(collCommun));
      assertTrue(serviceManager.getCollaborateursManager(passif).size() == 2);
      assertTrue(serviceManager.getCollaborateursManager(passif).contains(collPassif));
      assertTrue(serviceManager.getCollaborateursManager(passif).contains(collCommun));
      assertTrue(collaborateurManager.getServicesManager(collCommun).size() == 4);
      assertTrue(collaborateurManager.getServicesManager(collCommun).contains(passif));
      assertTrue(collaborateurManager.getServicesManager(collCommun).contains(actif));

      // fusion
      serviceManager.fusionServiceManager(actif.getServiceId(), passif.getServiceId(), "test collabs", u1);

      assertTrue(serviceManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(serviceManager.getCollaborateursManager(actif).size() == 3);
      assertTrue(serviceManager.getCollaborateursManager(actif).contains(collActif));
      assertTrue(serviceManager.getCollaborateursManager(actif).contains(collPassif));
      assertTrue(serviceManager.getCollaborateursManager(actif).contains(collCommun));
      assertTrue(collaborateurManager.getServicesManager(collCommun).size() == 3);
      assertFalse(collaborateurManager.getServicesManager(collCommun).contains(passif));
      assertTrue(collaborateurManager.getServicesManager(collCommun).contains(actif));

      // clean up
      collaborateurManager.removeObjectManager(collaborateurDao.findByNom("COLLFUSIONACTIF").get(0), null, u1);
      collaborateurManager.removeObjectManager(collaborateurDao.findByNom("COLLFUSIONPASSIF").get(0), null, u1);

      servicesX2Set.remove(actif);
      servicesX2Set.remove(passif);

      collaborateurManager.updateObjectManager(collCommun, collCommun.getTitre(), collCommun.getEtablissement(),
         collCommun.getSpecialite(), new ArrayList<>(servicesX2Set),
         new ArrayList<>(collaborateurManager.getCoordonneesManager(collCommun)), u1, false);
      assertTrue(collaborateurManager.getServicesManager(collCommun).size() == 2);

      getOperationManager().removeObjectManager(operationDao
         .findByObjetIdAndEntite(collCommun.getCollaborateurId(), entiteDao.findByNom("Collaborateur").get(0)).get(0));
      getOperationManager().removeObjectManager(operationDao
         .findByObjetIdAndEntite(collCommun.getCollaborateurId(), entiteDao.findByNom("Collaborateur").get(0)).get(0));

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(collActif);
      fs.add(collPassif);
      cleanAfterFusion(fs);
   }
}
