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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.PopulateBeanForTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 *
 * Classe de test pour le DAO ServiceDao et le bean du domaine Service.
 *
 * Date: 09/09/2009
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ServiceDaoTest extends AbstractDaoTest
{

   /** Bean Dao ServiceDao. */
   @Autowired
 ServiceDao serviceDao;
   /** Bean Dao EtablissementDao. */
   @Autowired
 EtablissementDao etablissementDao;
   /** Bean Dao CollaborateurDao. */
   @Autowired
 CollaborateurDao collaborateurDao;
   /** Bean Dao CoordonneeDao. */
   @Autowired
 CoordonneeDao coordonneeDao;
   /** valeur du nom pour la maj. */
   @Autowired
 final String updatedNom = "Service mis a jour";

   /** Constructeur. */
   public ServiceDaoTest(){

   }

   /**
    * Setter du bean Dao ServiceDao.
    * @param sDao est le bean Dao.
    */
   @Test
public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   /**
    * Setter du bean Dao EtablissementDao.
    * @param eDao est le bean Dao.
    */
   @Test
public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   /**
    * Setter du bean Dao CoordonneeDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   /**
    * Setter du bean Dao CollaborateurDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllServices(){
      final List<Service> services = IterableUtils.toList(serviceDao.findAll());
      assertTrue(services.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   @Test
public void testFindByOrder(){
      final List<Service> services = serviceDao.findByOrder();
      assertTrue(services.size() == 4);
      assertTrue(services.get(1).getNom().equals("ANAPATH"));
      assertTrue(services.get(2).getNom().equals("CELLULO"));
      assertTrue(services.get(3).getNom().equals("HEMATO"));
      assertTrue(services.get(0).getNom().equals("ANAPATH SURESNES"));
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   @Test
public void testFindByNom(){
      List<Service> services = serviceDao.findByNom("HEMATO");
      assertTrue(services.size() == 1);
      services = serviceDao.findByNom("TEST");
      assertTrue(services.size() == 0);
      services = serviceDao.findByNom("ANA%");
      assertTrue(services.size() == 2);
   }

   /**
    * Test l'appel de la méthode findByArchiveWithOrder().
    */
   @Test
public void testFindByArchiveWithOrder(){
      List<Service> services = serviceDao.findByArchiveWithOrder(false);
      assertTrue(services.size() == 3);
      assertTrue(services.get(0).getNom().equals("ANAPATH"));

      services = serviceDao.findByArchiveWithOrder(true);
      assertTrue(services.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByCoordonnee().
    */
   @Test
public void testFindByCoordonnee(){
      Coordonnee c = coordonneeDao.findById(1);
      List<Service> services = serviceDao.findByCoordonnee(c);
      assertTrue(services.size() == 1);
      c = coordonneeDao.findById(5);
      services = serviceDao.findByCoordonnee(c);
      assertTrue(services.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEtablissement().
    */
   @Test
public void testFindByEtablissement(){
      Etablissement e = etablissementDao.findById(2);
      List<Service> services = serviceDao.findByEtablissement(e);
      assertTrue(services.size() == 1);
      e = etablissementDao.findById(4);
      services = serviceDao.findByEtablissement(e);
      assertTrue(services.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEtablissementWithOrder().
    */
   @Test
public void testFindByEtablissementWithOrder(){
      Etablissement e = etablissementDao.findById(1);
      List<Service> services = serviceDao.findByEtablissementWithOrder(e);
      assertTrue(services.size() == 3);
      assertTrue(services.get(0).getNom().equals("ANAPATH"));
      assertTrue(services.get(1).getNom().equals("CELLULO"));
      e = etablissementDao.findById(4);
      services = serviceDao.findByEtablissementWithOrder(e);
      assertTrue(services.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEtablissementArchiveWithOrder().
    */
   @Test
public void testFindByEtablissementArchiveWithOrder(){
      Etablissement e = etablissementDao.findById(1);
      List<Service> services = serviceDao.findByEtablissementArchiveWithOrder(e, false);
      assertTrue(services.size() == 2);
      assertTrue(services.get(0).getNom().equals("ANAPATH"));
      assertTrue(services.get(1).getNom().equals("HEMATO"));

      services = serviceDao.findByEtablissementArchiveWithOrder(e, true);
      assertTrue(services.size() == 1);

      e = etablissementDao.findById(4);
      services = serviceDao.findByEtablissementWithOrder(e);
      assertTrue(services.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCollaborateurId().
    */
   @Test
public void testFindByCollaborateurId(){
      List<Service> services = serviceDao.findByCollaborateurId(1);
      assertTrue(services.size() == 1);
      services = serviceDao.findByCollaborateurId(3);
      assertTrue(services.size() == 2);
      services = serviceDao.findByCollaborateurId(5);
      assertTrue(services.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCollaborateurIdAndArchive().
    */
   @Test
public void testFindByCollaborateurIdAndArchived(){
      List<Service> services = serviceDao.findByCollaborateurIdAndArchive(1, false);
      assertTrue(services.size() == 1);

      services = serviceDao.findByCollaborateurIdAndArchive(1, true);
      assertTrue(services.size() == 0);

      services = serviceDao.findByCollaborateurIdAndArchive(3, false);
      assertTrue(services.size() == 1);

      services = serviceDao.findByCollaborateurIdAndArchive(3, true);
      assertTrue(services.size() == 1);

      services = serviceDao.findByCollaborateurIdAndArchive(5, true);
      assertTrue(services.size() == 0);

      services = serviceDao.findByCollaborateurIdAndArchive(5, false);
      assertTrue(services.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanquePossedeesId().
    */
   @Test
public void testFindByBanquePossedeesId(){
      List<Service> services = serviceDao.findByBanquePossedeesId(1);
      assertTrue(services.size() == 1);
      services = serviceDao.findByBanquePossedeesId(5);
      assertTrue(services.size() == 0);
      services = serviceDao.findByBanquePossedeesId(3);
      assertTrue(services.size() == 1);
      assertTrue(services.get(0).getServiceId() == 2);
   }

   /**
    * Test l'appel de la méthode findByIdWithFetch().
    */
   @Test
public void testFindByIdWithFetch(){
      final List<Service> services = serviceDao.findByIdWithFetch(1);
      final Service service = services.get(0);
      assertNotNull(service);
      assertTrue(service.getCoordonnee().getCoordonneeId() == 1);
      assertTrue(service.getEtablissement().getEtablissementId() == 1);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<Service> list = serviceDao.findByExcludedId(1);
      assertTrue(list.size() == 3);

      list = serviceDao.findByExcludedId(10);
      assertTrue(list.size() == 4);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un service.
    * @throws Exception lance une exception en cas d'erreur sur les données.
    */
   @Rollback(false)
   @Test
public void testCrudService() throws Exception{

      final Service s = new Service();
      final Etablissement e = etablissementDao.findById(2);
      final Collaborateur c = collaborateurDao.findById(1);
      //Coordonnee c = coordonneeDao.findById(6);
      //Coordonnee c = new Coordonnee();

      // on remplit le nouveau service avec les données du fichier
      // "service.properties"
      try{
         PopulateBeanForTest.populateBean(s, "service");
         //PopulateBeanForTest.populateBean(c, "coordonnee");
      }catch(final Exception exc){
         System.out.println(exc.getMessage());
      }
      //s.setCoordonnee(c);
      s.setEtablissement(e);
      s.getCollaborateurs().add(c);
      // Test de l'insertion
      serviceDao.save(s);
      assertEquals(new Integer(5), s.getServiceId());

      // Test de la mise à jour
      final Service s2 = serviceDao.findById(new Integer(5));
      assertNotNull(s2);
      assertTrue(s2.getNom().equals("Service1"));
      assertTrue(s2.getCollaborateurs().size() > 0);
      //assertNotNull(s2.getCoordonnee());
      assertNotNull(s2.getEtablissement());
      s2.setNom(updatedNom);
      serviceDao.save(s2);
      assertTrue(serviceDao.findById(new Integer(5)).getNom().equals(updatedNom));

      // Test de la délétion
      serviceDao.deleteById(new Integer(5));
      assertFalse(serviceDao.findById(new Integer(5)).isPresent());

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String nom = "Service1";
      final String nom2 = "Service2";
      final Etablissement e1 = etablissementDao.findById(1);
      final Etablissement e2 = etablissementDao.findById(2);
      final Service s1 = new Service();
      Service s2 = new Service();

      // L'objet 1 n'est pas égal à null
      assertFalse(s1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(s1.equals(s1));

      /*null*/
      assertTrue(s1.equals(s2));
      assertTrue(s2.equals(s1));

      /*Nom*/
      s2.setNom(nom);
      assertFalse(s1.equals(s2));
      assertFalse(s2.equals(s1));
      s1.setNom(nom2);
      assertFalse(s1.equals(s2));
      assertFalse(s2.equals(s1));
      s1.setNom(nom);
      assertTrue(s1.equals(s2));
      assertTrue(s2.equals(s1));

      /*Etablissement*/
      s2.setEtablissement(e1);
      assertFalse(s1.equals(s2));
      assertFalse(s2.equals(s1));
      s1.setEtablissement(e2);
      assertFalse(s1.equals(s2));
      assertFalse(s2.equals(s1));
      s1.setEtablissement(e1);
      assertTrue(s1.equals(s2));
      assertTrue(s2.equals(s1));

      final Categorie c3 = new Categorie();
      assertFalse(s1.equals(c3));

      //teste doublons
      s2 = serviceDao.findById(2);
      s1.setNom(s2.getNom());
      s1.setEtablissement(s2.getEtablissement());
      assertTrue(IterableUtils.toList(serviceDao.findAll()).contains(s1));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String nom = "Service1";
      final String nom2 = "Service2";
      final Etablissement e1 = etablissementDao.findById(1);
      final Etablissement e2 = etablissementDao.findById(2);
      final Service s1 = new Service();
      final Service s2 = new Service();

      /*null*/
      assertTrue(s1.hashCode() == s2.hashCode());

      /*Nom*/
      s2.setNom(nom);
      assertFalse(s1.hashCode() == s2.hashCode());
      s1.setNom(nom2);
      assertFalse(s1.hashCode() == s2.hashCode());
      s1.setNom(nom);
      assertTrue(s1.hashCode() == s2.hashCode());

      /*Specialite*/
      s2.setEtablissement(e1);
      assertFalse(s1.hashCode() == s2.hashCode());
      s1.setEtablissement(e2);
      assertFalse(s1.hashCode() == s2.hashCode());
      s1.setEtablissement(e1);
      assertTrue(s1.hashCode() == s2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = s1.hashCode();
      assertTrue(hash == s1.hashCode());
      assertTrue(hash == s1.hashCode());
      assertTrue(hash == s1.hashCode());
      assertTrue(hash == s1.hashCode());
   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final Service s1 = serviceDao.findById(1);
      final Service s2 = s1.clone();
      assertTrue(s1.equals(s2));

      if(s1.getServiceId() != null){
         assertTrue(s1.getServiceId() == s2.getServiceId());
      }else{
         assertNull(s2.getServiceId());
      }

      if(s1.getCoordonnee() != null){
         assertTrue(s1.getCoordonnee().equals(s2.getCoordonnee()));
      }else{
         assertNull(s2.getCoordonnee());
      }

      if(s1.getEtablissement() != null){
         assertTrue(s1.getEtablissement().equals(s2.getEtablissement()));
      }else{
         assertNull(s2.getEtablissement());
      }

      if(s1.getNom() != null){
         assertTrue(s1.getNom().equals(s2.getNom()));
      }else{
         assertNull(s2.getNom());
      }

      assertTrue(s1.getArchive() == s2.getArchive());
   }
}
