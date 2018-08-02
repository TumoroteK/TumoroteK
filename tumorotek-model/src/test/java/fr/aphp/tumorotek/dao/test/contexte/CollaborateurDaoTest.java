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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.SpecialiteDao;
import fr.aphp.tumorotek.dao.contexte.TitreDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.PopulateBeanForTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;

/**
 *
 * Classe de test pour le DAO CollaborateurDao et le bean
 * du domaine Collaborateur.
 *
 * @author Pierre Ventadour
 * @version 09/09/2009
 *
 */
public class CollaborateurDaoTest extends AbstractDaoTest
{

   /** Bean Dao CollaborateurDao. */
   private CollaborateurDao collaborateurDao;
   /** Bean Dao EtablissementDao. */
   private EtablissementDao etablissementDao;
   /** Bean Dao CoordonneeDao. */
   private CoordonneeDao coordonneeDao;
   /** Bean Dao SpecialiteDao. */
   private SpecialiteDao specialiteDao;
   /** Bean Dao TitreDao. */
   private TitreDao titreDao;
   /** Bean Dao ServiceDao. */
   private ServiceDao serviceDao;
   /** valeur du nom pour la maj. */
   private final String updatedNom = "Coll mis a jour";

   /** Constructeur. */
   public CollaborateurDaoTest(){}

   /**
    * Setter du bean Dao CollaborateurDao.
    * @param cDao est le bean Dao.
    */
   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   /**
    * Setter du bean Dao EtablissementDao.
    * @param eDao est le bean Dao.
    */
   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   /**
    * Setter du bean Dao CoordonneeDao.
    * @param cDao est le bean Dao.
    */
   public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   /**
    * Setter du bean Dao SpecialiteDao.
    * @param sDao est le bean Dao.
    */
   public void setSpecialiteDao(final SpecialiteDao sDao){
      this.specialiteDao = sDao;
   }

   /**
    * Setter du bean Dao TitreDao.
    * @param tDao est le bean Dao.
    */
   public void setTitreDao(final TitreDao tDao){
      this.titreDao = tDao;
   }

   /**
    * Setter du bean Dao ServiceDao.
    * @param sDao est le bean Dao.
    */
   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllCollaborateurs(){
      final List<Collaborateur> collaborateurs = collaborateurDao.findAll();
      assertTrue(collaborateurs.size() == 6);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<Collaborateur> collaborateurs = collaborateurDao.findByOrder();
      assertTrue(collaborateurs.size() == 6);
      assertTrue(collaborateurs.get(0).getNom().equals("DUFAY"));
      assertTrue(collaborateurs.get(5).getNom().equals("XIE"));
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByNom("VIAL");
      assertTrue(collaborateurs.size() == 1);
      assertTrue(collaborateurs.get(0).getNom().equals("VIAL"));
      collaborateurs = collaborateurDao.findByNom("VI");
      assertTrue(collaborateurs.size() == 0);

      collaborateurs = collaborateurDao.findByNom("VI%");
      assertTrue(collaborateurs.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByPrenom().
    */
   public void testFindByPrenom(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByPrenom("PIERRE");
      assertTrue(collaborateurs.size() == 1);
      assertTrue(collaborateurs.get(0).getPrenom().equals("PIERRE"));
      collaborateurs = collaborateurDao.findByPrenom("TEST P");
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByArchive().
    */
   public void testFindByArchive(){
      final List<Collaborateur> collaborateurs = collaborateurDao.findByArchive(false);
      assertTrue(collaborateurs.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByExcludedId(1);
      assertTrue(collaborateurs.size() == 5);

      collaborateurs = collaborateurDao.findByExcludedId(10);
      assertTrue(collaborateurs.size() == 6);
   }

   /**
    * Test l'appel de la méthode findByEtablissement().
    */
   public void testFindByEtablissement(){
      final Etablissement etab1 = etablissementDao.findById(3);
      List<Collaborateur> collaborateurs = collaborateurDao.findByEtablissement(etab1);
      assertTrue(collaborateurs.size() == 2);
      final Etablissement etab2 = etablissementDao.findById(2);
      collaborateurs = collaborateurDao.findByEtablissement(etab2);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEtablissementWithOrder().
    */
   public void testFindByEtablissementWithOrder(){
      final Etablissement etab1 = etablissementDao.findById(1);
      List<Collaborateur> collaborateurs = collaborateurDao.findByEtablissementWithOrder(etab1);
      assertTrue(collaborateurs.size() == 3);
      assertTrue(collaborateurs.get(0).getNom().equals("DUFAY"));
      assertTrue(collaborateurs.get(1).getNom().equals("VIAL"));

      final Etablissement etab2 = etablissementDao.findById(2);
      collaborateurs = collaborateurDao.findByEtablissementWithOrder(etab2);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEtablissementArchiveWithOrder().
    */
   public void testFindByEtablissementArchiveWithOrder(){
      final Etablissement etab1 = etablissementDao.findById(1);
      List<Collaborateur> collaborateurs = collaborateurDao.findByEtablissementArchiveWithOrder(etab1, false);
      assertTrue(collaborateurs.size() == 2);
      assertTrue(collaborateurs.get(0).getNom().equals("DUFAY"));
      assertTrue(collaborateurs.get(1).getNom().equals("VIAL"));

      collaborateurs = collaborateurDao.findByEtablissementArchiveWithOrder(etab1, true);
      assertTrue(collaborateurs.size() == 1);

      final Etablissement etab2 = etablissementDao.findById(2);
      collaborateurs = collaborateurDao.findByEtablissementArchiveWithOrder(etab2, true);
      assertTrue(collaborateurs.size() == 0);

      final Etablissement etab3 = etablissementDao.findById(3);
      collaborateurs = collaborateurDao.findByEtablissementArchiveWithOrder(etab3, true);
      assertTrue(collaborateurs.size() == 0);

      collaborateurs = collaborateurDao.findByEtablissementArchiveWithOrder(etab3, false);
      assertTrue(collaborateurs.size() == 2);
   }

   /**
    * Test l'appel de la méthode findBySpecialite().
    */
   public void testFindBySpecialite(){
      Specialite s = specialiteDao.findById(1);
      List<Collaborateur> collaborateurs = collaborateurDao.findBySpecialite(s);
      assertTrue(collaborateurs.size() == 3);
      s = specialiteDao.findById(6);
      collaborateurs = collaborateurDao.findBySpecialite(s);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTitre().
    */
   public void testFindByTitre(){
      Titre t = titreDao.findById(2);
      List<Collaborateur> collaborateurs = collaborateurDao.findByTitre(t);
      assertTrue(collaborateurs.size() == 1);
      t = titreDao.findById(6);
      collaborateurs = collaborateurDao.findByTitre(t);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByServiceId().
    */
   public void testFindByServiceId(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByServiceId(1);
      assertTrue(collaborateurs.size() == 2);
      collaborateurs = collaborateurDao.findByServiceId(2);
      assertTrue(collaborateurs.size() == 1);
      collaborateurs = collaborateurDao.findByServiceId(4);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByServiceIdWithOrder().
    */
   public void testFindByServiceIdWithOrder(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByServiceIdWithOrder(1);
      assertTrue(collaborateurs.size() == 2);
      assertTrue(collaborateurs.get(0).getNom().equals("DUFAY"));
      assertTrue(collaborateurs.get(1).getNom().equals("VIAL"));

      collaborateurs = collaborateurDao.findByServiceId(2);
      assertTrue(collaborateurs.size() == 1);

      collaborateurs = collaborateurDao.findByServiceId(4);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByServiceIdArchiveWithOrder().
    */
   public void testFindByServiceIdArchiveWithOrder(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByServiceIdArchiveWithOrder(1, false);
      assertTrue(collaborateurs.size() == 2);
      assertTrue(collaborateurs.get(0).getNom().equals("DUFAY"));
      assertTrue(collaborateurs.get(1).getNom().equals("VIAL"));

      collaborateurs = collaborateurDao.findByServiceIdArchiveWithOrder(1, true);
      assertTrue(collaborateurs.size() == 0);

      collaborateurs = collaborateurDao.findByServiceIdArchiveWithOrder(2, false);
      assertTrue(collaborateurs.size() == 0);

      collaborateurs = collaborateurDao.findByServiceIdArchiveWithOrder(2, true);
      assertTrue(collaborateurs.size() == 1);

      collaborateurs = collaborateurDao.findByServiceIdArchiveWithOrder(3, false);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCollaborateurWithoutService().
    */
   public void testFindByCollaborateurWithoutService(){
      final List<Collaborateur> collaborateurs = collaborateurDao.findByCollaborateurWithoutService();
      assertTrue(collaborateurs.size() == 3);
   }

   public void testFindByEtablissementNoService(){
      Etablissement e = etablissementDao.findById(3);
      List<Collaborateur> collaborateurs = collaborateurDao.findByEtablissementNoService(e);
      assertTrue(collaborateurs.size() == 2);

      e = etablissementDao.findById(1);
      collaborateurs = collaborateurDao.findByEtablissementNoService(e);
      assertTrue(collaborateurs.size() == 0);

      collaborateurs = collaborateurDao.findByEtablissementNoService(null);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode 
    * findByCollaborateurWithoutServiceAndArchive().
    */
   public void testFindByCollaborateurWithoutServiceAndArchive(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByCollaborateurWithoutServiceAndArchive(false);
      assertTrue(collaborateurs.size() == 2);

      collaborateurs = collaborateurDao.findByCollaborateurWithoutServiceAndArchive(true);
      assertTrue(collaborateurs.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByPlateformeId().
    */
   public void testFindByPlateformeId(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByPlateformeId(1);
      assertTrue(collaborateurs.size() == 1);
      collaborateurs = collaborateurDao.findByPlateformeId(3);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueId().
    */
   public void testFindByBanqueId(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByBanqueId(1);
      assertTrue(collaborateurs.size() == 1);
      collaborateurs = collaborateurDao.findByBanqueId(5);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByUtilisateurId().
    */
   public void testFindByUtilisateurId(){
      List<Collaborateur> collaborateurs = collaborateurDao.findByUtilisateurId(1);
      assertTrue(collaborateurs.size() == 1);
      collaborateurs = collaborateurDao.findByUtilisateurId(5);
      assertTrue(collaborateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIdWithFetch().
    */
   public void testFindByIdWithFetch(){
      final List<Collaborateur> collaborateurs = collaborateurDao.findByIdWithFetch(5);
      final Collaborateur collaborateur = collaborateurs.get(0);
      assertNotNull(collaborateur);
      assertTrue(collaborateur.getCoordonnees().iterator().next().getCoordonneeId() == 3);
      assertTrue(collaborateur.getEtablissement().getEtablissementId() == 3);
      assertTrue(collaborateur.getSpecialite().getSpecialiteId() == 2);
      assertTrue(collaborateur.getTitre().getTitreId() == 4);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un collaborateur.
    * @throws Exception lance une exception en cas d'erreur sur les données. 
    */
   @Rollback(false)
   public void testCrudCollaborateur() throws Exception{

      final Collaborateur c = new Collaborateur();
      final Etablissement etab = etablissementDao.findById(1);
      final Coordonnee co = new Coordonnee();
      co.setAdresse("1 ru cELaide");
      final Set<Coordonnee> coords = new HashSet<>();
      coords.add(co);
      final Titre titre = titreDao.findById(4);
      final Specialite spe = specialiteDao.findById(5);
      final Service serv = serviceDao.findById(1);
      final Service serv2 = serviceDao.findById(2);

      // on remplit le nouveau collaborateur avec les données du fichier
      // "collaborateur.properties"
      try{
         PopulateBeanForTest.populateBean(c, "collaborateur");
      }catch(final Exception exc){
         System.out.println(exc.getMessage());
      }
      c.setCoordonnees(coords);
      c.setEtablissement(etab);
      c.setSpecialite(spe);
      c.setTitre(titre);
      c.getServices().add(serv);
      // Test de l'insertion
      collaborateurDao.createObject(c);
      assertEquals(new Integer(7), c.getCollaborateurId());
      assertTrue(coordonneeDao.findAll().size() == 6);

      // Test de la mise à jour
      final Collaborateur c2 = collaborateurDao.findById(new Integer(7));
      // Vérification des données entrées dans la base
      assertNotNull(c2);
      assertTrue(c2.getCoordonnees().iterator().hasNext());
      assertNotNull(c2.getEtablissement());
      assertNotNull(c2.getSpecialite());
      assertNotNull(c2.getTitre());
      //assertTrue(c2.getServices().iterator().hasNext());
      c2.setNom(updatedNom);
      c2.getServices().add(serv2);
      collaborateurDao.updateObject(c2);
      assertTrue(collaborateurDao.findById(new Integer(7)).getNom().equals(updatedNom));
      assertTrue(collaborateurDao.findById(new Integer(7)).getServices().size() == 2);
      // Test de la délétion
      final Coordonnee coord = c2.getCoordonnees().iterator().next();
      c2.getCoordonnees().remove(coord);
      coordonneeDao.removeObject(coord.getCoordonneeId());
      assertTrue(coordonneeDao.findAll().size() == 5);
      collaborateurDao.removeObject(c2.getCollaborateurId());
      testFindByExcludedId();
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String nom = "Coll1";
      final String nom2 = "Coll2";
      final String prenom = "Pre1";
      final String prenom2 = "Pre2";
      final Specialite s1 = specialiteDao.findById(1);
      final Specialite s2 = specialiteDao.findById(2);
      final Collaborateur c1 = new Collaborateur();
      final Collaborateur c2 = new Collaborateur();

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));

      /*null*/
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Nom*/
      c2.setNom(nom);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setNom(nom2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setNom(nom);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      /*Prenom*/
      c2.setPrenom(prenom);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setPrenom(prenom2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setPrenom(prenom);
      assertTrue(c1.equals(c2));

      /*Specialite*/
      c2.setSpecialite(s1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setSpecialite(s2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c1.setSpecialite(s1);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      final Categorie c3 = new Categorie();
      assertFalse(c1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String nom = "Coll1";
      final String nom2 = "Coll2";
      final String prenom = "Pre1";
      final String prenom2 = "Pre2";
      final Specialite s1 = specialiteDao.findById(1);
      final Specialite s2 = specialiteDao.findById(2);
      final Collaborateur c1 = new Collaborateur();
      final Collaborateur c2 = new Collaborateur();

      /*null*/
      assertTrue(c1.hashCode() == c2.hashCode());

      /*Nom*/
      c2.setNom(nom);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setNom(nom2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setNom(nom);
      assertTrue(c1.hashCode() == c2.hashCode());

      /*Prenom*/
      c2.setPrenom(prenom);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setPrenom(prenom2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setPrenom(prenom);
      assertTrue(c1.hashCode() == c2.hashCode());

      /*Specialite*/
      c2.setSpecialite(s1);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setSpecialite(s2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setSpecialite(s1);
      assertTrue(c1.hashCode() == c2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = c1.hashCode();
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
   }

   /**
    * test toString().
    */
   public void testToString(){
      final Collaborateur c1 = collaborateurDao.findById(1);
      assertTrue(c1.toString().equals("{" + c1.getNom() + "}"));

      final Collaborateur c2 = new Collaborateur();
      assertTrue(c2.toString().equals("{Empty Collaborateur}"));
   }

   /**
    * test méthod nomAndPrenom().
    */
   public void testNomAndPrenom(){
      final Collaborateur c1 = collaborateurDao.findById(1);
      assertTrue(c1.getNomAndPrenom().equals(c1.getNom() + " " + c1.getPrenom()));

      final Collaborateur c4 = collaborateurDao.findById(4);
      assertTrue(c4.getNomAndPrenom().equals(c4.getNom()));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Collaborateur c1 = collaborateurDao.findById(1);
      final Collaborateur c2 = c1.clone();
      assertTrue(c1.equals(c2));

      if(c1.getCollaborateurId() != null){
         assertTrue(c1.getCollaborateurId() == c2.getCollaborateurId());
      }else{
         assertNull(c2.getCollaborateurId());
      }

      if(c1.getEtablissement() != null){
         assertTrue(c1.getEtablissement().equals(c2.getEtablissement()));
      }else{
         assertNull(c2.getEtablissement());
      }

      if(c1.getSpecialite() != null){
         assertTrue(c1.getSpecialite().equals(c2.getSpecialite()));
      }else{
         assertNull(c2.getSpecialite());
      }

      if(c1.getNom() != null){
         assertTrue(c1.getNom().equals(c2.getNom()));
      }else{
         assertNull(c2.getNom());
      }

      if(c1.getPrenom() != null){
         assertTrue(c1.getPrenom().equals(c2.getPrenom()));
      }else{
         assertNull(c2.getPrenom());
      }

      if(c1.getTitre() != null){
         assertTrue(c1.getTitre().equals(c2.getTitre()));
      }else{
         assertNull(c2.getTitre());
      }

      assertTrue(c1.getArchive() == c2.getArchive());
   }
}
