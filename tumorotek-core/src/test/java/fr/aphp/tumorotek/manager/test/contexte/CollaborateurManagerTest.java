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
import java.text.SimpleDateFormat;
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
import fr.aphp.tumorotek.dao.cession.RetourDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.LaboInterDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.qualite.OperationDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.CollaborateurManager;
import fr.aphp.tumorotek.manager.context.CoordonneeManager;
import fr.aphp.tumorotek.manager.context.EtablissementManager;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.context.ServiceManager;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.context.TitreManager;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager CollaborateurManager.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CollaborateurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private CollaborateurManager collaborateurManager;

   @Autowired
   private SpecialiteManager specialiteManager;

   @Autowired
   private EtablissementManager etablissementManager;

   @Autowired
   private TitreManager titreManager;

   @Autowired
   private ServiceManager serviceManager;

   @Autowired
   private CoordonneeManager coordonneeManager;

   @Autowired
   private UtilisateurDao utilisateurDao;

   @Autowired
   private ServiceDao serviceDao;

   @Autowired
   private UtilisateurManager utilisateurManager;

   @Autowired
   private CollaborateurDao collaborateurDao;

   @Autowired
   private BanqueManager banqueManager;

   @Autowired
   private BanqueDao banqueDao;

   @Autowired
   private PlateformeDao plateformeDao;

   @Autowired
   private ContexteDao contexteDao;

   @Autowired
   private CoordonneeDao coordonneeDao;

   @Autowired
   private ContratManager contratManager;

   @Autowired
   private ContratDao contratDao;

   @Autowired
   private EchantillonManager echantillonManager;

   @Autowired
   private EchantillonDao echantillonDao;

   @Autowired
   private EchantillonTypeDao echantillonTypeDao;

   @Autowired
   private ObjetStatutDao objetStatutDao;

   @Autowired
   private LaboInterDao laboInterDao;

   @Autowired
   private PrelevementManager prelevementManager;

   @Autowired
   private PrelevementDao prelevementDao;

   @Autowired
   private MaladieManager maladieManager;

   @Autowired
   private MaladieDao maladieDao;

   @Autowired
   private PatientManager patientManager;

   @Autowired
   private PatientDao patientDao;

   @Autowired
   private PlateformeManager plateformeManager;

   @Autowired
   private ProdDeriveManager produitDeriveManager;

   @Autowired
   private ProdDeriveDao prodDeriveDao;

   @Autowired
   private ProdTypeDao produitTypeDao;

   @Autowired
   private RetourDao retourDao;

   @Autowired
   private RetourManager retourManager;

   @Autowired
   private EntiteDao entiteDao;

   @Autowired
   private OperationDao operationDao;

   @Autowired
   private CessionDao cessionDao;

   @Autowired
   private CessionManager cessionManager;

   @Autowired
   private CessionTypeDao cessionTypeDao;

   @Autowired
   private CessionStatutDao cessionStatutDao;

   @Autowired
   private NatureDao natureDao;

   @Autowired
   private ConsentTypeDao consentTypeDao;

   @Autowired
   private OperationTypeDao operationTypeDao;

   public CollaborateurManagerTest(){}

   @Test
   public void testFindById(){
      final Collaborateur collab = collaborateurManager.findByIdManager(1);
      assertNotNull(collab);
      assertTrue(collab.getNom().equals("VIAL"));

      final Collaborateur collabNull = collaborateurManager.findByIdManager(7);
      assertNull(collabNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Collaborateur> list = collaborateurManager.findAllObjectsManager();
      assertTrue(list.size() == 6);
   }

   /**
    * Test la méthode findAllObjectsWithOrderManager.
    */
   @Test
   public void testFindAllObjectsWithOrderManager(){
      final List<Collaborateur> list = collaborateurManager.findAllObjectsWithOrderManager();
      assertTrue(list.size() == 6);
      assertTrue(list.get(0).getNom().equals("DUFAY"));
      assertTrue(list.get(5).getNom().equals("XIE"));
   }

   /**
    * Test la méthode findAllActiveObjectsWithOrderManager.
    */
   @Test
   public void testFindAllActiveObjectsWithOrderManager(){
      final List<Collaborateur> list = collaborateurManager.findAllActiveObjectsWithOrderManager();
      assertTrue(list.size() == 4);
      assertTrue(list.get(0).getNom().equals("DUFAY"));
      assertTrue(list.get(3).getNom().equals("VIAL"));
   }

   /**
    * Test la méthode findAllObjectsWithoutService.
    */
   @Test
   public void testFindAllObjectsWithoutService(){
      final List<Collaborateur> list = collaborateurManager.findAllObjectsWithoutService();
      assertTrue(list.size() == 3);
      assertTrue(collaborateurManager.getServicesManager(list.get(0)).size() == 0);
   }

   @Test
   public void testFindByEtablissementNoServiceManager(){
      Etablissement e = etablissementManager.findByIdManager(4);
      List<Collaborateur> collaborateurs = collaborateurManager.findByEtablissementNoServiceManager(e);
      assertTrue(collaborateurs.size() == 1);

      e = etablissementManager.findByIdManager(2);
      collaborateurs = collaborateurManager.findByEtablissementNoServiceManager(e);
      assertTrue(collaborateurs.size() == 0);

      collaborateurs = collaborateurManager.findByEtablissementNoServiceManager(null);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test la méthode findAllActiveObjectsWithoutService.
    */
   @Test
   public void testFindAllActiveObjectsWithoutService(){
      final List<Collaborateur> list = collaborateurManager.findAllActiveObjectsWithoutService();
      assertTrue(list.size() == 2);
      assertTrue(collaborateurManager.getServicesManager(list.get(0)).size() == 0);
   }

   /**
    * Test la méthode getServicesManager.
    */
   @Test
   public void testGetServicesManager(){
      final Collaborateur collab1 = collaborateurManager.findByIdManager(1);
      assertNotNull(collab1);
      Set<Service> services = collaborateurManager.getServicesManager(collab1);
      assertTrue(services.size() == 1);

      final Collaborateur collab2 = collaborateurManager.findByIdManager(4);
      assertNotNull(collab2);
      services = collaborateurManager.getServicesManager(collab2);
      assertTrue(services.size() == 0);

      final Collaborateur collabNull = null;
      services = collaborateurManager.getServicesManager(collabNull);
      assertTrue(services.size() == 0);
   }

   /**
    * Test la méthode getCoordonneesManager.
    */
   @Test
   public void testGetCoordonneesManager(){
      final Collaborateur collab1 = collaborateurManager.findByIdManager(1);
      assertNotNull(collab1);
      Set<Coordonnee> coordonnees = collaborateurManager.getCoordonneesManager(collab1);
      assertTrue(coordonnees.size() == 2);

      final Collaborateur collab2 = collaborateurManager.findByIdManager(4);
      assertNotNull(collab2);
      coordonnees = collaborateurManager.getCoordonneesManager(collab2);
      assertTrue(coordonnees.size() == 1);

      final Collaborateur collabNull = null;
      coordonnees = collaborateurManager.getCoordonneesManager(collabNull);
      assertTrue(coordonnees.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeExactManager(){
      List<Collaborateur> list = collaborateurManager.findByNomLikeManager("VIAL", true);
      assertTrue(list.size() == 1);

      list = collaborateurManager.findByNomLikeManager("VIA", true);
      assertTrue(list.size() == 0);

      list = collaborateurManager.findByNomLikeManager("", true);
      assertTrue(list.size() == 0);

      list = collaborateurManager.findByNomLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      List<Collaborateur> list = collaborateurManager.findByNomLikeManager("VIAL", false);
      assertTrue(list.size() == 1);

      list = collaborateurManager.findByNomLikeManager("V", false);
      assertTrue(list.size() == 2);

      list = collaborateurManager.findByNomLikeManager("", false);
      assertTrue(list.size() == 6);

      list = collaborateurManager.findByNomLikeManager(null, false);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomLikeBothSideManager.
    */
   @Test
   public void testFindByNomLikeBothSideManager(){
      List<Collaborateur> list = collaborateurManager.findByNomLikeBothSideManager("AL");
      assertTrue(list.size() == 1);

      list = collaborateurManager.findByNomLikeBothSideManager("V");
      assertTrue(list.size() == 2);

      list = collaborateurManager.findByNomLikeBothSideManager("");
      assertTrue(list.size() == 6);

      list = collaborateurManager.findByNomLikeBothSideManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByPrenomLikeManager.
    */
   @Test
   public void testFindByPrenomLikeExactManager(){
      List<Collaborateur> list = collaborateurManager.findByPrenomLikeManager("CHRISTOPHE", true);
      assertTrue(list.size() == 2);

      list = collaborateurManager.findByPrenomLikeManager("CHRIS", true);
      assertTrue(list.size() == 0);

      list = collaborateurManager.findByPrenomLikeManager("", true);
      assertTrue(list.size() == 0);

      list = collaborateurManager.findByPrenomLikeManager(null, true);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findBySpecialiteManager.
    */
   @Test
   public void testFindBySpecialiteManager(){
      final Specialite spec1 = specialiteManager.findByIdManager(1);
      List<Collaborateur> list = collaborateurManager.findBySpecialiteManager(spec1);
      assertNotNull(list);
      assertTrue(list.size() == 3);

      final Specialite spec4 = specialiteManager.findByIdManager(4);
      list = collaborateurManager.findBySpecialiteManager(spec4);
      assertNotNull(list);
      assertTrue(list.size() == 0);

      list = collaborateurManager.findBySpecialiteManager(null);
      assertNotNull(list);
      assertTrue(list.size() == 0);
   }

   /**
    * findByServicesAndArchiveManager.
    */
   @Test
   public void testFindByServicesAndArchiveManager(){
      final Service s1 = serviceManager.findByIdManager(1);
      List<Collaborateur> liste = collaborateurManager.findByServicesAndArchiveManager(s1, false);
      assertTrue(liste.size() == 2);
      liste = collaborateurManager.findByServicesAndArchiveManager(s1, true);
      assertTrue(liste.size() == 0);

      final Service s2 = serviceManager.findByIdManager(2);
      liste = collaborateurManager.findByServicesAndArchiveManager(s2, false);
      assertTrue(liste.size() == 0);
      liste = collaborateurManager.findByServicesAndArchiveManager(s2, true);
      assertTrue(liste.size() == 1);

      liste = collaborateurManager.findByServicesAndArchiveManager(new Service(), false);

      liste = collaborateurManager.findByServicesAndArchiveManager(null, false);
   }

   /**
    * Test la méthode findByCodeLikeManager.
    */
   @Test
   public void testFindByPrenomLikeManager(){
      List<Collaborateur> list = collaborateurManager.findByPrenomLikeManager("CHRISTOPHE", false);
      assertTrue(list.size() == 2);

      list = collaborateurManager.findByPrenomLikeManager("CHRIS", false);
      assertTrue(list.size() == 2);

      list = collaborateurManager.findByPrenomLikeManager("", false);
      assertTrue(list.size() == 5);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){
      final Specialite spe1 = specialiteManager.findByIdManager(1);
      final Specialite spe2 = specialiteManager.findByIdManager(2);
      final String nom1 = "VIAL";
      final String nom2 = "VRAI";
      final String prenom1 = "CHRISTOPHE";
      final String prenom2 = "PRE";
      final Collaborateur collab = new Collaborateur();
      collab.setSpecialite(spe1);
      collab.setNom(nom1);
      collab.setPrenom(prenom1);
      assertTrue(collaborateurManager.findDoublonManager(collab));

      collab.setSpecialite(spe2);
      assertFalse(collaborateurManager.findDoublonManager(collab));

      collab.setSpecialite(spe1);
      collab.setNom(nom2);
      assertFalse(collaborateurManager.findDoublonManager(collab));

      collab.setNom(nom1);
      collab.setPrenom(prenom2);
      assertFalse(collaborateurManager.findDoublonManager(collab));

      final Collaborateur collab2 = collaborateurManager.findByIdManager(1);
      assertFalse(collaborateurManager.findDoublonManager(collab2));

      collab2.setNom("MOREL");
      collab2.setPrenom("CHRISTOPHE");
      collab2.setSpecialite(spe2);
      assertTrue(collaborateurManager.findDoublonManager(collab2));
   }

   /**
    * Test de la méthode isArchivableManager().
    */
   @Test
   public void testIsArchivableManager(){
      final Collaborateur c1 = collaborateurManager.findByIdManager(1);
      final Service s1 = serviceManager.findByIdManager(1);
      final Service s2 = serviceManager.findByIdManager(2);
      final Service s3 = serviceManager.findByIdManager(3);

      assertTrue(collaborateurManager.isArchivableManager(c1, s1));
      assertFalse(collaborateurManager.isArchivableManager(c1, s2));
      assertFalse(collaborateurManager.isArchivableManager(c1, null));

      final Collaborateur c3 = collaborateurManager.findByIdManager(3);
      assertFalse(collaborateurManager.isArchivableManager(c3, s1));
      assertTrue(collaborateurManager.isArchivableManager(c3, s2));
      assertFalse(collaborateurManager.isArchivableManager(c1, s3));

      assertTrue(collaborateurManager.isArchivableManager(null, s3));
      assertTrue(collaborateurManager.isArchivableManager(null, null));

      final Collaborateur c4 = collaborateurManager.findByIdManager(4);
      assertTrue(collaborateurManager.isArchivableManager(c4, null));
   }

   /**
    * Test d'un CRUD.
    */
   @Test
   public void testCrud(){
      final Utilisateur u = utilisateurDao.findById(1);
      // Récupération des objets associés à un Collaborateur
      final Etablissement etab = etablissementManager.findByIdManager(1);
      final Specialite spec = specialiteManager.findByIdManager(1);
      final Titre titre = titreManager.findByIdManager(1);
      final Service service = serviceManager.findByIdManager(1);
      final Service service2 = serviceManager.findByIdManager(2);
      final Service service3 = serviceManager.findByIdManager(3);
      List<Service> services = new ArrayList<>();
      Set<Service> servicesTest = new HashSet<>();
      final Coordonnee coord1 = coordonneeManager.findByIdManager(1);
      final Coordonnee coord2 = coordonneeManager.findByIdManager(2);
      // Coordonnee coord3 = coordonneeManager.findByIdManager(3);
      List<Coordonnee> coords = new ArrayList<>();
      Set<Coordonnee> coordsTest = new HashSet<>();

      // Insertion
      final Collaborateur collab1 = new Collaborateur();
      collab1.setNom("VIAL");
      collab1.setPrenom("CHRISTOPHE");
      collab1.setSpecialite(spec);
      Boolean catched = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancée
      try{
         collaborateurManager.createObjectManager(collab1, null, null, spec, null, null, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      // Test de la validation lors de la création
      validationInsert(collab1);
      assertTrue(collaborateurManager.findAllObjectsManager().size() == 6);
      // on teste une insertion valide avec les associations 
      // non obigatoires nulles
      collab1.setNom("JU-JA.");
      collab1.setPrenom("Jean Eric 4.");
      collaborateurManager.createObjectManager(collab1, null, null, null, null, null, u);
      final int idC1 = collab1.getCollaborateurId();
      assertTrue(getOperationManager().findByObjectManager(collab1).size() == 1);

      // on teste une insertion valide avec les associations 
      // non obigatoires remplies
      final Collaborateur collab2 = new Collaborateur();
      collab2.setNom("JU-JA. 89");
      collab2.setPrenom("Pierre");
      services.add(service);
      services.add(service2);
      coords.add(coord1);
      coords.add(coord2);
      final Coordonnee newCoord = new Coordonnee();
      newCoord.setAdresse("TMP");
      final Coordonnee newCoord2 = new Coordonnee();
      newCoord2.setAdresse("TMP");
      coordonneeManager.createObjectManager(newCoord2, null);
      int id2 = newCoord2.getCoordonneeId();
      coords.add(newCoord);
      coords.add(newCoord2);
      // On teste l'insertion d'un doublon de coordonnée pour vérifier 
      // que l'exception est lancée
      catched = false;
      try{
         collaborateurManager.createObjectManager(collab2, titre, etab, spec, services, coords, u);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      // Création avec des coordonnées valides
      newCoord2.setAdresse("TEST");
      coords = new ArrayList<>();
      //coords.add(coord1);
      //coords.add(coord2);
      coords.add(newCoord);
      coords.add(newCoord2);
      collaborateurManager.createObjectManager(collab2, titre, etab, spec, services, coords, u);
      final int idC2 = collab2.getCollaborateurId();
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(collab2).size() == 1);

      // On vérifie que toutes associations ont étées enregistrées
      final Collaborateur testInsert = collaborateurManager.findByIdManager(idC2);
      assertNotNull(testInsert.getEtablissement());
      assertNotNull(testInsert.getSpecialite());
      assertNotNull(testInsert.getTitre());
      // On test les attributs
      assertTrue(testInsert.getNom().length() > 0);
      assertTrue(testInsert.getPrenom().length() > 0);
      // On vérifie les associations avec les services
      servicesTest = collaborateurManager.getServicesManager(testInsert);
      Iterator<Service> it = servicesTest.iterator();
      assertTrue(servicesTest.size() == 2);
      assertTrue(collaborateurManager.getCoordonneesManager(testInsert).size() == 2);
      // on vérifie 1 coord updaté lors de la création du collab
      final Coordonnee testCoord = coordonneeManager.findByIdManager(id2);
      assertTrue(testCoord.getAdresse().equals("TEST"));
      assertTrue(coordonneeManager.getCollaborateursManager(testCoord).size() == 1);

      // Suppression
      final Collaborateur collab3 = collaborateurManager.findByIdManager(idC1);
      collaborateurManager.removeObjectManager(collab3, null, u);
      final Collaborateur collab4 = collaborateurManager.findByIdManager(idC2);
      collaborateurManager.removeObjectManager(collab4, null, u);
      coordonneeManager.removeObjectManager(newCoord);
      coordonneeManager.removeObjectManager(newCoord2);
      assertTrue(getOperationManager().findByObjectManager(collab3).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(collab4).size() == 0);

      // Update
      final String nomUpdated2 = "VIAL";
      final Collaborateur collab5 = new Collaborateur();
      collab5.setNom("COLLAB7");
      collaborateurManager.createObjectManager(collab5, null, null, spec, null, null, u);
      final int idC3 = collab5.getCollaborateurId();
      assertTrue(getOperationManager().findByObjectManager(collab5).size() == 1);

      Boolean catchedUpdate = false;
      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      final Collaborateur collab6 = collaborateurManager.findByIdManager(idC3);
      collab6.setNom(nomUpdated2);
      collab6.setPrenom("CHRISTOPHE");
      try{
         collaborateurManager.updateObjectManager(collab6, null, null, spec, null, null, u, true);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;
      // On test la validation lors d'un update
      final Collaborateur collab7 = collaborateurManager.findByIdManager(idC3);
      validationUpdate(collab7);
      // On teste une mise à jour valide avec les assos a null mais sans
      // la validation
      collab7.setNom("BAR");
      collab7.setPrenom("*#%$$");
      collaborateurManager.updateObjectManager(collab7, null, null, spec, null, null, u, false);
      final Collaborateur collab8 = collaborateurManager.findByIdManager(idC3);
      assertTrue(collab8.getNom().equals("BAR"));
      assertTrue(collab8.getPrenom().equals("*#%$$"));
      assertTrue(getOperationManager().findByObjectManager(collab7).size() == 2);

      // On teste une mise à jour valide avec les assos
      final Collaborateur collab9 = collaborateurManager.findByIdManager(idC3);
      collab9.setNom("BARTH");
      collab9.setPrenom("Mathieu");
      services = new ArrayList<>();
      services.add(service);
      services.add(service2);
      coords = new ArrayList<>();
      coords.add(coord1);
      coords.add(coord2);
      // on crée 2 coords égales
      final Coordonnee newCoordUp = new Coordonnee();
      newCoordUp.setAdresse("TMP");
      final Coordonnee newCoordUp2 = new Coordonnee();
      newCoordUp2.setAdresse("TMP");
      coordonneeManager.createObjectManager(newCoordUp2, null);
      id2 = newCoordUp2.getCoordonneeId();
      coords.add(newCoordUp);
      coords.add(newCoordUp2);
      // On teste l'update d'un doublon de coordonnée pour vérifier 
      // que l'exception est lancée
      catchedUpdate = false;
      try{
         collaborateurManager.updateObjectManager(collab9, titre, etab, spec, services, coords, u, true);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      catchedUpdate = false;
      // Création avec des coordonnées valides
      newCoordUp2.setAdresse("TEST");
      coords = new ArrayList<>();
      coords.add(coord1);
      coords.add(coord2);
      coords.add(newCoordUp);
      coords.add(newCoordUp2);
      collaborateurManager.updateObjectManager(collab9, titre, etab, spec, services, coords, u, true);
      assertTrue(coordonneeManager.findAllObjectsManager().size() == 7);
      assertTrue(getOperationManager().findByObjectManager(collab9).size() == 3);

      // On vérifie que toutes associations ont étées enregistrées
      final Collaborateur testInsert2 = collaborateurManager.findByIdManager(idC3);
      assertNotNull(testInsert2.getEtablissement());
      assertNotNull(testInsert2.getSpecialite());
      assertNotNull(testInsert2.getTitre());
      assertTrue(collaborateurManager.getServicesManager(testInsert2).size() == 2);
      // On test les attributs
      assertTrue(testInsert2.getNom().length() > 0);
      assertTrue(testInsert2.getPrenom().length() > 0);
      assertTrue(collaborateurManager.getCoordonneesManager(testInsert2).size() == 4);

      // On test la modif d'une liste de services et de coordonnées
      final Collaborateur collab10 = collaborateurManager.findByIdManager(idC3);
      services = new ArrayList<>();
      services.add(service3);
      coords = new ArrayList<>();
      //coords.add(coord2);
      //coords.add(coord3);
      collaborateurManager.updateObjectManager(collab10, titre, etab, spec, services, coords, u, true);
      assertTrue(getOperationManager().findByObjectManager(collab10).size() == 4);
      // On vérifie que les associations de services ont étée modifiées
      final Collaborateur testInsert3 = collaborateurManager.findByIdManager(idC3);
      servicesTest = collaborateurManager.getServicesManager(testInsert3);
      assertTrue(servicesTest.size() == 1);
      it = servicesTest.iterator();
      while(it.hasNext()){
         final Service tmp = it.next();
         assertTrue(tmp.getServiceId() == 3);
      }
      // On vérifie que les associations de coords ont étée modifiées
      coordsTest = collaborateurManager.getCoordonneesManager(testInsert3);
      assertTrue(coordsTest.size() == 0);

      final Collaborateur collab11 = collaborateurManager.findByIdManager(idC3);
      collaborateurManager.removeObjectManager(collab11, null, u);
      assertNull(collaborateurManager.findByIdManager(idC3));
      coordonneeManager.removeObjectManager(newCoordUp);
      coordonneeManager.removeObjectManager(newCoordUp2);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(collab3);
      fs.add(collab4);
      fs.add(collab11);
      cleanUpFantomes(fs);
   }

   /**
    * Test la validation d'un collaborateur lors de sa création.
    * @param collaborateur Collaborateur à tester.
    */
   private void validationInsert(final Collaborateur collaborateur){
      final Utilisateur u = utilisateurDao.findById(1);
      boolean catchedInsert = false;
      final String[] nomValues = new String[] {"", "  ", null, "l#¤¤$d%", createOverLength(30), "GOOD"};
      final String[] prenomValues = new String[] {"", " ", "l#¤¤$d%", createOverLength(30), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < prenomValues.length; j++){
            catchedInsert = false;
            try{
               collaborateur.setNom(nomValues[i]);
               collaborateur.setPrenom(prenomValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  collaborateurManager.createObjectManager(collaborateur, null, null, null, null, null, u);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedInsert = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedInsert);
            }
         }
      }
      // On teste une insertion avec un attribut nom vide

   }

   /**
    * Test la validation d'un collaborateur lors de sa modif.
    * @param collaborateur Collaborateur à tester.
    */
   private void validationUpdate(final Collaborateur collaborateur){
      final Utilisateur u = utilisateurDao.findById(1);
      boolean catchedUpdate = false;
      final String[] nomValues = new String[] {"", "  ", null, "l#¤¤$d%", createOverLength(30), "GOOD"};
      final String[] prenomValues = new String[] {"", " ", "l#¤¤$d%", createOverLength(30), "GOOD"};
      for(int i = 0; i < nomValues.length; i++){
         for(int j = 0; j < prenomValues.length; j++){
            catchedUpdate = false;
            try{
               collaborateur.setNom(nomValues[i]);
               collaborateur.setPrenom(prenomValues[j]);
               if(i != 5 || j != 4){ //car creation valide
                  collaborateurManager.updateObjectManager(collaborateur, null, null, null, null, null, u, true);
               }
            }catch(final Exception e){
               if(e.getClass().getSimpleName().equals("ValidationException")){
                  catchedUpdate = true;
               }
            }
            if(i != 5 || j != 4){
               assertTrue(catchedUpdate);
            }
         }
      }
   }

   @Test
   public void testIsObjectReferencedManager(){
      Collaborateur c = collaborateurManager.findByIdManager(1);
      final Utilisateur u = utilisateurDao.findById(1);
      boolean catched = false;
      try{
         collaborateurManager.removeObjectManager(c, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      c = collaborateurManager.findByIdManager(2);
      try{
         collaborateurManager.removeObjectManager(c, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      c = collaborateurManager.findByIdManager(3);
      try{
         collaborateurManager.removeObjectManager(c, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      c = collaborateurManager.findByIdManager(4);
      try{
         collaborateurManager.removeObjectManager(c, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      c = collaborateurManager.findByIdManager(5);
      try{
         collaborateurManager.removeObjectManager(c, null, u);
      }catch(final ObjectReferencedException ore){
         catched = true;
         assertTrue(ore.getKey().equals("collaborateur.deletion.isReferencedCascade"));
      }
      assertTrue(catched);

      catched = false;
      c = collaborateurManager.findByIdManager(6);
      assertFalse(collaborateurManager.isReferencedObjectManager(c));
   }

   private void setUpFusionTest(){
      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur cA = new Collaborateur();
      cA.setNom("ACTIF");
      final Collaborateur cP = new Collaborateur();
      cP.setNom("PASSIF");

      collaborateurManager.createObjectManager(cA, null, null, null, null, null, u1);
      collaborateurManager.createObjectManager(cP, null, null, null, null, null, u1);

      assertFalse(collaborateurManager.findByNomLikeManager("ACTIF", true).isEmpty());
      assertFalse(collaborateurManager.findByNomLikeManager("PASSIF", true).isEmpty());
   }

   private void cleanAfterFusion(final List<TKFantomableObject> fs){
      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      collaborateurManager.removeObjectManager(actif, null, utilisateurDao.findById(1));
      fs.add(actif);
      cleanUpFantomes(fs);
      assertTrue(collaborateurManager.findAllObjectsManager().size() == 6);
   }

   @Rollback(false)
   @Test
   public void testFusionCollaborateurManagerVide() throws ParseException{

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur cPVide = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      // nulls
      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), 0, null, null);
      collaborateurManager.fusionCollaborateurManager(0, cPVide.getCollaborateurId(), null, null);

      assertFalse(collaborateurManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertFalse(collaborateurManager.findByNomLikeManager("ACTIF", true).isEmpty());

      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), cPVide.getCollaborateurId(), "fusion test vide",
         u1);

      assertTrue(collaborateurManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(operationDao.findByObjetIdEntiteAndOperationType(actif.getCollaborateurId(),
         entiteDao.findByNom("Collaborateur").get(0), operationTypeDao.findByNom("Fusion").get(0)).size() == 1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(cPVide);
      cleanAfterFusion(fs);

   }

   @Test
   public void testFusionCollaborateurs(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passifVide = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passifVide.getCollaborateurId(), "test vide",
         u1);

      assertTrue(collaborateurManager.findByNomLikeManager("PASSIF", true).isEmpty());
      assertTrue(getFantomeDao().findByNom("PASSIF").get(0).getCommentaires()
         .equals("fusion id: " + actif.getCollaborateurId().toString() + " .test vide"));

      assertTrue(operationDao.findByObjetIdEntiteAndOperationType(actif.getCollaborateurId(),
         entiteDao.findByNom("Collaborateur").get(0), operationTypeDao.findByNom("Fusion").get(0)).size() == 1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passifVide);
      cleanAfterFusion(fs);

   }

   @Test
   public void testFusionCollaborateursWithBanques(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      final Banque b = new Banque();
      b.setNom("BANK");
      b.setArchive(false);
      banqueManager.createOrUpdateObjectManager(b, plateformeDao.findById(1), contexteDao.findById(1), null, passif, passif, null,
         null, null, null, null, null, null, null, null, null, u1, null, "creation", "/tmp/");

      final Banque b2 = new Banque();
      b2.setNom("BANK2");
      b2.setArchive(false);
      banqueManager.createOrUpdateObjectManager(b2, plateformeDao.findById(1), contexteDao.findById(1), null, passif, passif,
         null, null, null, null, null, null, null, null, null, null, u1, null, "creation", "/tmp/");

      assertTrue(banqueDao.findByCollaborateur(passif).size() == 2);
      assertTrue(banqueDao.findByCollaborateur(actif).isEmpty());
      assertTrue(banqueDao.findByCollaborateur(passif).get(0).getContact().equals(passif));
      assertTrue(banqueDao.findByCollaborateur(passif).get(1).getContact().equals(passif));

      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test banque", u1);

      assertTrue(banqueDao.findByCollaborateur(passif).isEmpty());
      assertTrue(banqueDao.findByCollaborateur(actif).size() == 2);
      assertTrue(banqueDao.findByCollaborateur(actif).get(0).getContact().equals(actif));
      assertTrue(banqueDao.findByCollaborateur(actif).get(1).getContact().equals(actif));

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
   public void testFusionCollaborateursContrats(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      Contrat contrat = new Contrat();
      contrat.setNumero("CONTRAT");
      contratManager.createObjectManager(contrat, passif, null, null, null, plateformeDao.findById(1), u1);
      Contrat contrat2 = new Contrat();
      contrat2.setNumero("CONTRAT2");
      contratManager.createObjectManager(contrat2, passif, null, null, null, plateformeDao.findById(1), u1);
      assertTrue(contratDao.findByCollaborateur(passif).size() == 2);
      assertTrue(contratDao.findByCollaborateur(actif).isEmpty());
      assertTrue(contratDao.findByNumero("CONTRAT").get(0).getCollaborateur().equals(passif));
      assertTrue(contratDao.findByNumero("CONTRAT2").get(0).getCollaborateur().equals(passif));

      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test contrat",
         u1);

      assertTrue(contratDao.findByCollaborateur(passif).isEmpty());
      assertTrue(contratDao.findByCollaborateur(actif).size() == 2);
      assertTrue(contratDao.findByNumero("CONTRAT").get(0).getCollaborateur().equals(actif));
      assertTrue(contratDao.findByNumero("CONTRAT2").get(0).getCollaborateur().equals(actif));

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
   public void testFusionCollaborateursCoordonnees(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      final Coordonnee coordonneeA = new Coordonnee();
      coordonneeA.setAdresse("addresse Active");
      coordonneeA.setPays("FRANCE");

      final Coordonnee coordonneeP = new Coordonnee();
      coordonneeP.setAdresse("addresse Passive");
      coordonneeP.setPays("FRANCE");

      final Coordonnee coordonneesCommune = coordonneeDao.findById(5);

      final List<Collaborateur> collaborateurActifList = new ArrayList<>();
      collaborateurActifList.add(actif);
      final List<Collaborateur> collaborateurPassifList = new ArrayList<>();
      collaborateurPassifList.add(passif);
      final List<Collaborateur> collaborateurX2List = new ArrayList<>();
      collaborateurX2List.add(actif);
      collaborateurX2List.add(passif);
      collaborateurX2List.add(collaborateurDao.findById(6)); // VENTADOUR

      coordonneeManager.createObjectManager(coordonneeA, collaborateurActifList);
      coordonneeManager.createObjectManager(coordonneeP, collaborateurPassifList);
      coordonneeManager.updateObjectManager(coordonneesCommune, collaborateurX2List, false);

      assertFalse(coordonneeDao.findByAdresse("addresse Active").isEmpty());
      assertFalse(coordonneeDao.findByAdresse("addresse Passive").isEmpty());

      assertTrue(coordonneeDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 2);
      assertTrue(coordonneeDao.findByCollaborateurId(passif.getCollaborateurId()).size() == 2);

      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test coords", u1);

      assertTrue(coordonneeDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 3);
      assertTrue(coordonneeDao.findByCollaborateurId(passif.getCollaborateurId()).isEmpty());

      assertTrue(coordonneeDao.findByCollaborateurId(actif.getCollaborateurId())
         .contains(coordonneeDao.findByAdresse("addresse Active").get(0)));
      assertTrue(coordonneeDao.findByCollaborateurId(actif.getCollaborateurId())
         .contains(coordonneeDao.findByAdresse("addresse Passive").get(0)));
      assertTrue(coordonneeDao.findByCollaborateurId(actif.getCollaborateurId()).contains(coordonneeDao.findById(5)));

      //clean up

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      cleanAfterFusion(fs);

      assertTrue(coordonneeDao.findByAdresse("addresse Active").isEmpty());
      assertTrue(coordonneeDao.findByAdresse("addresse Passive").isEmpty());

   }

   @Test
   public void testFusionCollaborateursServices(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      final Service s1 = new Service();
      s1.setNom("S1");
      final Service s2 = new Service();
      s2.setNom("S2");
      final Service s3 = new Service();
      s3.setNom("S3");

      final List<Collaborateur> s1colList = new ArrayList<>();
      s1colList.add(actif);
      final List<Collaborateur> s2colList = new ArrayList<>();
      s2colList.add(passif);
      final List<Collaborateur> s3colList = new ArrayList<>();
      s3colList.add(actif);
      s3colList.add(passif);

      serviceManager.createObjectManager(s1, null, etablissementManager.findByIdManager(1), s1colList, u1, false);
      serviceManager.createObjectManager(s2, null, etablissementManager.findByIdManager(1), s2colList, u1, false);
      serviceManager.createObjectManager(s3, null, etablissementManager.findByIdManager(1), s3colList, u1, false);

      assertFalse(serviceDao.findByCollaborateurId(passif.getCollaborateurId()).isEmpty());
      assertTrue(serviceDao.findByCollaborateurId(passif.getCollaborateurId()).size() == 2);
      assertTrue(serviceDao.findByCollaborateurId(passif.getCollaborateurId()).contains(s2));
      assertTrue(serviceDao.findByCollaborateurId(passif.getCollaborateurId()).contains(s3));
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 2);
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).contains(s1));
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).contains(s3));

      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test services",
         u1);

      assertTrue(serviceDao.findByCollaborateurId(passif.getCollaborateurId()).isEmpty());
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 3);
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).contains(s1));
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).contains(s2));
      assertTrue(serviceDao.findByCollaborateurId(actif.getCollaborateurId()).contains(s3));

      // clean collaborateurs from service
      s1.getCollaborateurs().clear();
      s2.getCollaborateurs().clear();
      s3.getCollaborateurs().clear();
      serviceManager.updateObjectManager(s1, null, etablissementManager.findByIdManager(1), new ArrayList<Collaborateur>(), u1,
         false, false);
      serviceManager.updateObjectManager(s2, null, etablissementManager.findByIdManager(1), new ArrayList<Collaborateur>(), u1,
         false, false);
      serviceManager.updateObjectManager(s3, null, etablissementManager.findByIdManager(1), new ArrayList<Collaborateur>(), u1,
         false, false);

      serviceManager.removeObjectCascadeManager(s1, null, u1);
      serviceManager.removeObjectCascadeManager(s2, null, u1);
      serviceManager.removeObjectCascadeManager(s3, null, u1);

      assertTrue(serviceDao.findByEtablissement(etablissementManager.findByIdManager(1)).size() == 3);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(s1);
      fs.add(s2);
      fs.add(s3);
      cleanAfterFusion(fs);

   }

   @Test
   public void testFusionCollaborateursCoreObjects(){

      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      Prelevement prelevement = new Prelevement();
      prelevement.setArchive(false);
      prelevement.setCode("PRELEVEMENTEST");

      final LaboInter laboInter = new LaboInter();
      laboInter.setOrdre(1);
      laboInter.setCollaborateur(passif);
      laboInter.setDateArrivee(Calendar.getInstance());
      final List<LaboInter> labs = new ArrayList<>();
      labs.add(laboInter);

      prelevementManager.createObjectManager(prelevement, banqueDao.findById(1), natureDao.findById(1), maladieDao.findById(1),
         consentTypeDao.findById(1), passif, serviceDao.findById(1), null, null, null, null, passif, null, labs, null, null, u1,
         false, "/tmp/", false);

      Prelevement prelevement2 = new Prelevement();
      prelevement2.setArchive(false);
      prelevement2.setCode("PRELEVEMENTEST2");

      final LaboInter laboInter2 = new LaboInter();
      laboInter2.setOrdre(1);
      laboInter2.setCollaborateur(passif);
      laboInter2.setDateArrivee(Calendar.getInstance());
      final List<LaboInter> labs2 = new ArrayList<>();
      labs2.add(laboInter2);

      prelevementManager.createObjectManager(prelevement2, banqueDao.findById(1), natureDao.findById(1), maladieDao.findById(1),
         consentTypeDao.findById(1), passif, serviceDao.findById(1), null, null, null, null, passif, null, labs2, null, null, u1,
         false, "/tmp/", false);

      assertTrue(prelevementDao.findByPreleveur(passif).size() == 2);
      assertTrue(prelevementDao.findByPreleveur(passif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST")));
      assertTrue(prelevementDao.findByPreleveur(passif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST2")));
      assertTrue(prelevementDao.findByOperateur(passif).size() == 2);
      assertTrue(prelevementDao.findByOperateur(passif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST")));
      assertTrue(prelevementDao.findByOperateur(passif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST2")));
      assertTrue(prelevementDao.findByOperateur(actif).isEmpty());
      assertTrue(prelevementDao.findByPreleveur(actif).isEmpty());
      assertTrue(laboInterDao.findByCollaborateur(passif).size() == 2);
      assertTrue(laboInterDao.findByCollaborateur(actif).isEmpty());

      Echantillon echantillon = new Echantillon();
      echantillon.setCode("ECHANTILLONTEST");
      echantillon.setArchive(true);
      echantillon.setObjetStatut(objetStatutDao.findById(1));

      echantillonManager.createObjectManager(echantillon, banqueDao.findById(1),
         prelevementDao.findByCode("PRELEVEMENTEST").get(0), passif, null, null, echantillonTypeDao.findById(1), null, null, null,
         null, null, null, u1, false, "/tmp/", false);

      assertFalse(echantillonDao.findByCode("ECHANTILLONTEST").isEmpty());

      Echantillon echantillon2 = new Echantillon();
      echantillon2.setCode("ECHANTILLONTEST2");
      echantillon2.setArchive(true);
      echantillon2.setObjetStatut(objetStatutDao.findById(1));

      echantillonManager.createObjectManager(echantillon2, banqueDao.findById(1),
         prelevementDao.findByCode("PRELEVEMENTEST").get(0), passif, null, null, echantillonTypeDao.findById(1), null, null, null,
         null, null, null, u1, false, "/tmp/", false);

      assertFalse(echantillonDao.findByCode("ECHANTILLONTEST2").isEmpty());

      assertTrue(echantillonDao.findByCollaborateur(passif).size() == 2);
      assertTrue(echantillonDao.findByCollaborateur(actif).isEmpty());

      final Retour retour = new Retour();
      retour.setObjetId(echantillonDao.findByCode("ECHANTILLONTEST").get(0).getEchantillonId());

      final Calendar dateSortie = Calendar.getInstance();
      final Calendar dateRetour = Calendar.getInstance();

      try{
         dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2011 12:10:25"));
         dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("02/01/2012 16:47:10"));
      }catch(final ParseException e){
         e.printStackTrace();
      }

      retour.setDateRetour(dateRetour);
      retour.setDateSortie(dateSortie);

      retour.setTempMoyenne(retourDao.findById(1).getTempMoyenne());
      retour.setEntite(entiteDao.findById(3));
      retourManager.createOrUpdateObjectManager(retour, null, null, passif, null, null, null, u1, "creation");

      final Retour retour2 = new Retour();
      retour2.setObjetId(echantillonDao.findByCode("ECHANTILLONTEST2").get(0).getEchantillonId());
      retour2.setDateRetour(dateRetour);
      retour2.setDateSortie(dateSortie);
      retour2.setTempMoyenne(retourDao.findById(1).getTempMoyenne());
      retour2.setEntite(entiteDao.findById(3));
      retourManager.createOrUpdateObjectManager(retour2, null, null, passif, null, null, null, u1, "creation");

      assertTrue(retourDao.findByCollaborateur(passif).size() == 2);
      assertTrue(retourDao.findByCollaborateur(actif).isEmpty());

      ProdDerive prodDerive = new ProdDerive();
      prodDerive.setCode("PRODUITDERIVETEST");
      prodDerive.setArchive(false);
      prodDerive.setObjetStatut(objetStatutDao.findById(1));

      produitDeriveManager.createObjectManager(prodDerive, banqueDao.findById(1), produitTypeDao.findById(1), null, passif, null,
         null, null, null, null, null, null, null, null, u1, false, "/tmp/", false);

      ProdDerive prodDerive2 = new ProdDerive();
      prodDerive2.setCode("PRODUITDERIVETEST2");
      prodDerive2.setArchive(false);
      prodDerive2.setObjetStatut(objetStatutDao.findById(1));

      produitDeriveManager.createObjectManager(prodDerive2, banqueDao.findById(1), produitTypeDao.findById(1), null, passif, null,
         null, null, null, null, null, null, null, null, u1, false, "/tmp/", false);

      assertTrue(prodDeriveDao.findByCollaborateur(passif).size() == 2);
      assertTrue(prodDeriveDao.findByCollaborateur(actif).isEmpty());

      // fusion
      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test services",
         u1);

      assertTrue(echantillonDao.findByCollaborateur(passif).isEmpty());
      assertTrue(echantillonDao.findByCollaborateur(actif).size() == 2);
      assertTrue(echantillonDao.findByCollaborateur(actif).containsAll(echantillonDao.findByCode("ECHANTILLONTEST")));
      assertTrue(echantillonDao.findByCollaborateur(actif).containsAll(echantillonDao.findByCode("ECHANTILLONTEST2")));
      assertTrue(prelevementDao.findByPreleveur(actif).size() == 2);
      assertTrue(prelevementDao.findByPreleveur(actif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST")));
      assertTrue(prelevementDao.findByPreleveur(actif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST2")));
      assertTrue(prelevementDao.findByOperateur(actif).size() == 2);
      assertTrue(prelevementDao.findByOperateur(actif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST")));
      assertTrue(prelevementDao.findByOperateur(actif).containsAll(prelevementDao.findByCode("PRELEVEMENTEST2")));
      assertTrue(prelevementDao.findByOperateur(passif).isEmpty());
      assertTrue(prelevementDao.findByPreleveur(passif).isEmpty());
      assertTrue(laboInterDao.findByCollaborateur(actif).size() == 2);
      assertTrue(laboInterDao.findByCollaborateur(actif).contains(laboInter));
      assertTrue(laboInterDao.findByCollaborateur(actif).contains(laboInter2));
      assertTrue(laboInterDao.findByCollaborateur(passif).isEmpty());
      assertTrue(retourDao.findByCollaborateur(actif).size() == 2);
      assertTrue(retourDao.findByCollaborateur(actif).contains(retour));
      assertTrue(retourDao.findByCollaborateur(actif).contains(retour2));
      assertTrue(retourDao.findByCollaborateur(passif).isEmpty());
      assertTrue(prodDeriveDao.findByCollaborateur(passif).isEmpty());
      assertTrue(prodDeriveDao.findByCollaborateur(actif).size() == 2);
      assertTrue(prodDeriveDao.findByCollaborateur(actif).containsAll(prodDeriveDao.findByCode("PRODUITDERIVETEST")));
      assertTrue(prodDeriveDao.findByCollaborateur(actif).containsAll(prodDeriveDao.findByCode("PRODUITDERIVETEST2")));

      // clean up
      prelevement = prelevementManager.findByCodeLikeManager("PRELEVEMENTEST", true).get(0);
      echantillon = echantillonDao.findByCode("ECHANTILLONTEST").get(0);
      echantillon2 = echantillonDao.findByCode("ECHANTILLONTEST2").get(0);
      prelevementManager.removeObjectCascadeManager(prelevement, "remove prel Preleveur fusion", u1, null);
      assertTrue(prelevementDao.findByCode("PRELEVEMENTEST").isEmpty());
      assertTrue(echantillonDao.findByCode("ECHANTILLONTEST").isEmpty());
      assertTrue(echantillonDao.findByCode("ECHANTILLONTEST2").isEmpty());
      prodDerive = prodDeriveDao.findByCode("PRODUITDERIVETEST").get(0);
      produitDeriveManager.removeObjectManager(prodDerive, null, u1, null);
      assertTrue(prodDeriveDao.findByCode("PRODUITDERIVETEST").isEmpty());
      prelevement2 = prelevementManager.findByCodeLikeManager("PRELEVEMENTEST2", true).get(0);
      prelevementManager.removeObjectCascadeManager(prelevement2, "remove prel Preleveur fusion", u1, null);
      prodDerive2 = prodDeriveDao.findByCode("PRODUITDERIVETEST2").get(0);
      produitDeriveManager.removeObjectManager(prodDerive2, null, u1, null);
      assertTrue(prodDeriveDao.findByCode("PRODUITDERIVETEST2").isEmpty());

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(prelevement);
      fs.add(prelevement2);
      fs.add(echantillon);
      fs.add(echantillon2);
      fs.add(prodDerive);
      fs.add(prodDerive2);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionCollaborateurCession(){
      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      Cession cess = new Cession();
      cess.setNumero("100");
      cess.setArchive(false);

      // cess.setDemandeur(collaborateurDao.findById(1));
      // cess.setExecutant(collaborateurDao.findById(1));

      cessionManager.createObjectManager(cess, banqueDao.findById(1), cessionTypeDao.findById(1), null, null, passif, null,
         passif, cessionStatutDao.findById(1), passif, null, null, null, null, u1, null, "/tmp/");

      Cession cess2 = new Cession();
      cess2.setNumero("1002");
      cess2.setArchive(false);

      // cess2.setDemandeur(collaborateurDao.findById(1));
      // cess2.setExecutant(collaborateurDao.findById(1));

      cessionManager.createObjectManager(cess2, banqueDao.findById(1), cessionTypeDao.findById(1), null, null, passif, null,
         passif, cessionStatutDao.findById(1), passif, null, null, null, null, u1, null, "/tmp/");

      assertTrue(cessionDao.findByNumero("100").get(0).getDestinataire().equals(passif));
      assertTrue(cessionDao.findByNumero("100").get(0).getDemandeur().equals(passif));
      assertTrue(cessionDao.findByNumero("100").get(0).getExecutant().equals(passif));
      assertTrue(cessionDao.findByNumero("1002").get(0).getDestinataire().equals(passif));
      assertTrue(cessionDao.findByNumero("1002").get(0).getDemandeur().equals(passif));
      assertTrue(cessionDao.findByNumero("1002").get(0).getExecutant().equals(passif));

      // fusion
      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test cession",
         u1);

      cess = cessionManager.findByNumeroLikeManager("100", true).get(0);
      assertTrue(cess.getDestinataire().equals(actif));
      assertTrue(cess.getDemandeur().equals(actif));
      assertTrue(cess.getExecutant().equals(actif));

      cess2 = cessionManager.findByNumeroLikeManager("1002", true).get(0);
      assertTrue(cess2.getDestinataire().equals(actif));
      assertTrue(cess2.getDemandeur().equals(actif));
      assertTrue(cess2.getExecutant().equals(actif));

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
   public void testFusionCollaborateurPlateformeAndUtilisateur(){
      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      final Plateforme plateF = plateformeDao.findById(1);
      plateformeManager.updateObjectManager(plateF, passif, null, null, u1);
      final Plateforme plateF2 = plateformeDao.findById(2);
      plateformeManager.updateObjectManager(plateF2, passif, null, null, u1);

      assertTrue(plateformeDao.findByCollaborateur(passif).size() == 2);
      assertTrue(plateformeDao.findByCollaborateur(actif).isEmpty());

      Utilisateur utilisateur = new Utilisateur();
      utilisateur.setLogin("USER6");
      utilisateur.setPassword("pass");
      utilisateur.setArchive(false);
      utilisateur.setSuperAdmin(false);

      utilisateurManager.createObjectManager(utilisateur, passif, null, null, u1, plateformeDao.findById(1));

      Utilisateur utilisateur2 = new Utilisateur();
      utilisateur2.setLogin("USER62");
      utilisateur2.setPassword("pass");
      utilisateur2.setArchive(false);
      utilisateur2.setSuperAdmin(false);

      utilisateurManager.createObjectManager(utilisateur2, passif, null, null, u1, plateformeDao.findById(1));

      assertTrue(utilisateurDao.findByCollaborateur(passif).size() == 2);
      assertTrue(utilisateurDao.findByCollaborateur(actif).isEmpty());

      // fusion
      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(), "test pf and user",
         u1);

      assertTrue(plateformeDao.findByCollaborateur(passif).isEmpty());
      assertTrue(plateformeDao.findByCollaborateur(actif).size() == 2);
      assertTrue(plateformeDao.findByCollaborateur(actif).contains(plateF));
      assertTrue(plateformeDao.findByCollaborateur(actif).contains(plateF2));
      assertTrue(utilisateurDao.findByCollaborateur(passif).isEmpty());
      assertTrue(utilisateurDao.findByCollaborateur(actif).size() == 2);
      assertTrue(utilisateurDao.findByCollaborateur(actif).contains(utilisateur));
      assertTrue(utilisateurDao.findByCollaborateur(actif).contains(utilisateur2));

      utilisateur = utilisateurDao.findByLogin("USER6").get(0);
      utilisateurManager.removeObjectManager(utilisateur);
      assertTrue(utilisateurDao.findByLogin("USER6").isEmpty());
      utilisateur2 = utilisateurDao.findByLogin("USER62").get(0);
      utilisateurManager.removeObjectManager(utilisateur2);
      assertTrue(utilisateurDao.findByLogin("USER62").isEmpty());

      // plateF = plateformeDao.findByCollaborateur(actif).get(0);
      // plateF.setCollaborateur(collaborateurDao.findById(1));
      plateformeManager.updateObjectManager(plateF, collaborateurDao.findById(1), null, null, u1);
      // plateF2.setCollaborateur(collaborateurDao.findById(4));
      plateformeManager.updateObjectManager(plateF2, collaborateurDao.findById(4), null, null, u1);

      for(final Operation op : getOperationManager().findByObjectManager(plateF)){
         getOperationManager().removeObjectManager(op);
      }
      for(final Operation op : getOperationManager().findByObjectManager(plateF2)){
         getOperationManager().removeObjectManager(op);
      }

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      cleanAfterFusion(fs);
   }

   @Test
   public void testFusionCollaborateurPatientMaladieMedecin() throws ParseException{
      setUpFusionTest();

      final Utilisateur u1 = utilisateurDao.findById(1);

      final Collaborateur actif = collaborateurManager.findByNomLikeManager("ACTIF", true).get(0);
      final Collaborateur passif = collaborateurManager.findByNomLikeManager("PASSIF", true).get(0);

      Patient patient = new Patient();
      patient.setNom("PATTEST");
      patient.setPrenom("Aixie");
      patient.setSexe("M");
      patient.setPatientEtat("V");
      patient.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("21/03/1980"));

      final List<Collaborateur> collaborateurPassifList = new ArrayList<>();
      collaborateurPassifList.add(passif);

      patientManager.createOrUpdateObjectManager(patient, null, collaborateurPassifList, null, null, null, null, null, u1,
         "creation", "/tmp/", false);

      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST").get(0)).size() == 1);

      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST").get(0)).get(0).equals(passif));

      Patient patient2 = new Patient();
      patient2.setNom("PATTEST2");
      patient2.setPrenom("Aixie");
      patient2.setSexe("M");
      patient2.setPatientEtat("V");
      patient2.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy").parse("21/03/1980"));

      patientManager.createOrUpdateObjectManager(patient2, null, collaborateurPassifList, null, null, null, null, null, u1,
         "creation", "/tmp/", false);

      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST2").get(0)).size() == 1);

      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST2").get(0)).get(0).equals(passif));

      Maladie maladie = new Maladie();
      maladie.setLibelle("MALADIETEST");
      maladie.setSystemeDefaut(false);

      maladieManager.createOrUpdateObjectManager(maladie, patientDao.findByNom("PATTEST").get(0), collaborateurPassifList, u1,
         "creation");

      assertFalse(maladieDao.findByLibelle("MALADIETEST").isEmpty());
      assertFalse(maladieDao.findByCollaborateurId(passif.getCollaborateurId()).isEmpty());
      assertTrue(maladieDao.findByCollaborateurId(actif.getCollaborateurId()).isEmpty());

      final Maladie maladie2 = new Maladie();
      maladie2.setLibelle("MALADIETEST2");
      maladie2.setSystemeDefaut(false);

      final List<Collaborateur> allCollabs = new ArrayList<>();
      allCollabs.add(passif);
      allCollabs.add(actif);

      maladieManager.createOrUpdateObjectManager(maladie2, patientDao.findByNom("PATTEST2").get(0), allCollabs, u1, "creation");

      assertFalse(maladieDao.findByLibelle("MALADIETEST2").isEmpty());
      assertTrue(maladieDao.findByCollaborateurId(passif.getCollaborateurId()).size() == 2);
      assertTrue(maladieDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 1);

      final Maladie maladie3 = new Maladie();
      maladie3.setLibelle("MALADIEACTIF");
      maladie3.setSystemeDefaut(false);

      // new Collab
      final Collaborateur newCol = new Collaborateur();
      newCol.setNom("NEWCOL");
      collaborateurManager.createObjectManager(newCol, null, null, null, null, null, u1);
      assertFalse(collaborateurManager.findByNomLikeManager("NEWCOL", true).isEmpty());

      final List<Collaborateur> listActif = new ArrayList<>();
      listActif.add(passif);
      listActif.add(newCol);

      maladieManager.createOrUpdateObjectManager(maladie3, patientDao.findByNom("PATTEST2").get(0), listActif, u1, "creation");

      assertFalse(maladieDao.findByLibelle("MALADIEACTIF").isEmpty());
      assertTrue(maladieDao.findByCollaborateurId(passif.getCollaborateurId()).size() == 3);
      assertTrue(maladieDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 1);
      assertTrue(maladieDao.findByCollaborateurId(newCol.getCollaborateurId()).size() == 1);

      // fusion
      collaborateurManager.fusionCollaborateurManager(actif.getCollaborateurId(), passif.getCollaborateurId(),
         "patient and maladie medecins", u1);

      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST").get(0)).size() == 1);
      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST").get(0)).get(0).equals(actif));
      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST2").get(0)).size() == 1);
      assertTrue(patientManager.getMedecinsManager(patientDao.findByNom("PATTEST2").get(0)).get(0).equals(actif));

      assertTrue(maladieDao.findByCollaborateurId(passif.getCollaborateurId()).isEmpty());
      assertTrue(maladieDao.findByCollaborateurId(actif.getCollaborateurId()).size() == 3);
      assertTrue(maladieDao.findByCollaborateurId(newCol.getCollaborateurId()).size() == 1);

      //clean up
      patient = patientDao.findByNom("PATTEST").get(0);
      maladie = maladieDao.findByCollaborateurId(actif.getCollaborateurId()).get(0);
      patientManager.removeObjectManager(patient, "remove fusion", u1, null);
      patient2 = patientDao.findByNom("PATTEST2").get(0);
      patientManager.removeObjectManager(patient2, "remove fusion", u1, null);

      collaborateurManager.removeObjectManager(newCol, null, u1);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(passif);
      fs.add(newCol);
      fs.add(patient);
      fs.add(maladie);
      fs.add(patient2);
      fs.add(maladie2);
      fs.add(maladie3);
      cleanAfterFusion(fs);
   }
}
