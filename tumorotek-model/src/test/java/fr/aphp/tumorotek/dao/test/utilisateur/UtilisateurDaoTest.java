/***
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
package fr.aphp.tumorotek.dao.test.utilisateur;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.PopulateBeanForTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO UtilisateurDao et le bean du domaine Utilisateur.
 *
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class UtilisateurDaoTest extends AbstractDaoTest
{

   private UtilisateurDao utilisateurDao;
   private CollaborateurDao collaborateurDao;
   private PlateformeDao plateformeDao;

   /** valeur du login pour la maj. */
   private final String updatedLogin = "Login mis a jour";

   /** Constructeur. */
   public UtilisateurDaoTest(){

   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllUtilisateurs(){
      final List<Utilisateur> utilisateurs = utilisateurDao.findAll();
      assertTrue(utilisateurs.size() == 5);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<Utilisateur> utilisateurs = utilisateurDao.findByOrder();
      assertTrue(utilisateurs.size() == 4);
      assertTrue(utilisateurs.get(0).getLogin().equals("USER1"));
   }

   /**
    * Test l'appel de la méthode findByLogin().
    */
   public void testFindByLogin(){
      List<Utilisateur> utilisateurs = utilisateurDao.findByLogin("USER1");
      assertTrue(utilisateurs.size() == 1);
      utilisateurs = utilisateurDao.findByLogin("USER");
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByLoginAndArchive().
    */
   public void testFindByLoginAndArchive(){
      final List<Plateforme> pfs = new ArrayList<>();
      pfs.add(plateformeDao.findById(1));
      List<Utilisateur> utilisateurs = utilisateurDao.findByLoginAndArchive("USER1", false, pfs);
      assertTrue(utilisateurs.size() == 1);

      utilisateurs = utilisateurDao.findByLoginAndArchive("USER1", true, pfs);
      assertTrue(utilisateurs.size() == 0);

      utilisateurs = utilisateurDao.findByLoginAndArchive("USER", true, pfs);
      assertTrue(utilisateurs.size() == 0);

      pfs.clear();
      pfs.add(plateformeDao.findById(2));
      utilisateurs = utilisateurDao.findByLoginAndArchive("USER1", false, pfs);
      assertTrue(utilisateurs.size() == 0);
      utilisateurs = utilisateurDao.findByLoginAndArchive("USER4", false, pfs);
      assertTrue(utilisateurs.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByArchive().
    */
   public void testFindByArchive(){
      final List<Plateforme> pfs = new ArrayList<>();
      pfs.add(plateformeDao.findById(1));
      List<Utilisateur> utilisateurs = utilisateurDao.findByArchive(false, pfs);
      assertTrue(utilisateurs.size() == 2);
      pfs.add(plateformeDao.findById(2));
      utilisateurs = utilisateurDao.findByArchive(false, pfs);
      assertTrue(utilisateurs.size() == 3);
   }

   /**
    * Test l'appel de la méthode findByOrderWithArchive().
    */
   public void testFindByOrderWithArchive(){
      final List<Plateforme> pfs = new ArrayList<>();
      pfs.add(plateformeDao.findById(1));
      List<Utilisateur> utilisateurs = utilisateurDao.findByOrderWithArchive(false, pfs);
      assertTrue(utilisateurs.size() == 2);
      pfs.add(plateformeDao.findById(2));
      utilisateurs = utilisateurDao.findByArchive(false, pfs);
      assertTrue(utilisateurs.size() == 3);

      utilisateurs = utilisateurDao.findByOrderWithArchive(true, pfs);
      assertTrue(utilisateurs.size() == 1);
   }

   /**
    * Test l'appel de la méthode findBydnLdap().
    */
   public void testFindBydnLdap(){
      List<Utilisateur> utilisateurs = utilisateurDao.findBydnLdap("LDAP3");
      assertTrue(utilisateurs.size() == 1);
      utilisateurs = utilisateurDao.findBydnLdap("LDAP");
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEmail().
    */
   public void testFindByEmail(){
      List<Utilisateur> utilisateurs = utilisateurDao.findByEmail("mail2@yahoo.fr");
      assertTrue(utilisateurs.size() == 1);
      utilisateurs = utilisateurDao.findByEmail("mail@mail.fr");
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTimeOut().
    * @throws Exception Lance une exception en cas d'erreur.
    */
   public void testFindByTimeOut() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2025");
      List<Utilisateur> utilisateurs = utilisateurDao.findByTimeOut(search);
      assertTrue(utilisateurs.size() == 2);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("05/11/2050");
      utilisateurs = utilisateurDao.findByTimeOut(search);
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByTimeOutBefore().
    * @throws Exception Lance une exception en cas d'erreur.
    */
   public void testFindByTimeOutBefore() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("21/06/2011");
      List<Utilisateur> utilisateurs = utilisateurDao.findByTimeOutBefore(search);
      assertTrue(utilisateurs.size() == 1);
      assertTrue(utilisateurs.get(0).getLogin().equals("USER2"));
      search = new SimpleDateFormat("dd/MM/yyyy").parse("12/03/1999");
      utilisateurs = utilisateurDao.findByTimeOutBefore(search);
      assertTrue(utilisateurs.size() == 0);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2050");
      utilisateurs = utilisateurDao.findByTimeOutBefore(search);
      assertTrue(utilisateurs.size() == 4);
      assertFalse(utilisateurs.contains(utilisateurDao.findById(3)));
   }

   /**
    * Test l'appel de la méthode findByTimeOutAfter().
    * @throws Exception Lance une exception en cas d'erreur.
    */
   public void testFindByTimeOutAfter() throws Exception{
      Date search = new SimpleDateFormat("dd/MM/yyyy").parse("21/06/2011");
      List<Utilisateur> utilisateurs = utilisateurDao.findByTimeOutAfter(search);
      assertTrue(utilisateurs.size() == 3);
      search = new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2050");
      utilisateurs = utilisateurDao.findByTimeOutAfter(search);
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCollaborateurId().
    */
   public void testFindByCollaborateur(){
      final Collaborateur c = collaborateurDao.findById(1);
      List<Utilisateur> utilisateurs = utilisateurDao.findByCollaborateur(c);
      assertTrue(utilisateurs.size() == 1);
      final Collaborateur c2 = collaborateurDao.findById(4);
      utilisateurs = utilisateurDao.findByCollaborateur(c2);
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByReservationId().
    */
   public void testFindByReservationId(){
      List<Utilisateur> utilisateurs = utilisateurDao.findByReservationId(1);
      assertTrue(utilisateurs.size() == 1);
      utilisateurs = utilisateurDao.findByReservationId(5);
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIdWithFetch().
    */
   public void testFindByIdWithFetch(){
      List<Utilisateur> utilisateurs = utilisateurDao.findByIdWithFetch(3);
      Utilisateur utilisateur = utilisateurs.get(0);
      assertNotNull(utilisateur);
      assertTrue(utilisateur.getCollaborateur().getCollaborateurId() == 3);

      utilisateurs = utilisateurDao.findByIdWithFetch(5);
      utilisateur = utilisateurs.get(0);
      assertNotNull(utilisateur);
      assertNull(utilisateur.getCollaborateur());
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<Utilisateur> utilisateurs = utilisateurDao.findByExcludedId(1);
      assertTrue(utilisateurs.size() == 4);
      final Utilisateur utilisateur = utilisateurs.get(0);
      assertNotNull(utilisateur);
      assertTrue(utilisateur.getUtilisateurId() == 2);

      utilisateurs = utilisateurDao.findByExcludedId(15);
      assertTrue(utilisateurs.size() == 5);
   }

   public void testFindByLoginPassAndArchive(){
      List<Utilisateur> utilisateurs =
         utilisateurDao.findByLoginPassAndArchive("USER5", "b383bb08bd750d8ef04d034ad648a208", false);
      assertTrue(utilisateurs.size() == 1);

      utilisateurs = utilisateurDao.findByLoginPassAndArchive("USER6", "b383bb08bd750d8ef04d034ad648a208", false);
      assertTrue(utilisateurs.size() == 0);

      utilisateurs = utilisateurDao.findByLoginPassAndArchive("USER5", "b383bb08bd750d8ef04d034ad648a208qcqsc", false);
      assertTrue(utilisateurs.size() == 0);

      utilisateurs = utilisateurDao.findByLoginPassAndArchive("USER5", "b383bb08bd750d8ef04d034ad648a208", true);
      assertTrue(utilisateurs.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un utilisateur.
    * @throws Exception Lance une exception en cas d'erreur.
    */
   public void testCrudUtilisateur() throws Exception{

      final Utilisateur u = new Utilisateur();
      final Collaborateur c = collaborateurDao.findById(4);
      // on remplit le nouvel utilisateur avec les données du fichier
      // "utilisateur.properties"
      try{
         PopulateBeanForTest.populateBean(u, "utilisateur");
      }catch(final Exception exc){
         System.out.println(exc.getMessage());
      }
      final Date dateTest = new SimpleDateFormat("dd/MM/yyyy").parse("25/11/2011");
      u.setTimeOut(dateTest);
      final Plateforme pf1 = plateformeDao.findById(1);
      u.setPlateformeOrig(pf1);
      u.setCollaborateur(c);

      // Test de l'insertion
      utilisateurDao.createObject(u);
      final Integer id = u.getUtilisateurId();

      // Test de la mise à jour
      final Utilisateur u2 = utilisateurDao.findById(id);
      assertNotNull(u2);
      assertTrue(u2.getLogin().equals("new user"));
      assertTrue(u2.getPassword().equals("new pass"));
      assertTrue(u2.getEncodedPassword().equals("encoded"));
      assertTrue(u2.getDnLdap().equals("ladp"));
      assertTrue(u2.getEmail().equals("mail@gmail.com"));
      assertTrue(u2.getTimeOut().getTime() == dateTest.getTime());
      assertTrue(u2.getPlateformeOrig().equals(pf1));
      assertNotNull(u2.getCollaborateur());
      u2.setLogin(updatedLogin);
      u2.setTimeOut(null);
      utilisateurDao.updateObject(u2);
      assertTrue(utilisateurDao.findById(id).getLogin().equals(updatedLogin));

      // Test de la délétion
      utilisateurDao.removeObject(id);
      assertNull(utilisateurDao.findById(id));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String login = "login";
      final String login2 = "login2";
      final Utilisateur u1 = new Utilisateur();
      u1.setLogin(login);
      final Utilisateur u2 = new Utilisateur();
      u2.setLogin(login);

      // L'objet 1 n'est pas égal à null
      assertFalse(u1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(u1.equals(u1));
      // 2 objets sont égaux entre eux
      assertTrue(u1.equals(u2));
      assertTrue(u2.equals(u1));

      u1.setLogin(null);
      u2.setLogin(null);
      assertTrue(u1.equals(u2));

      u2.setLogin(login);
      assertFalse(u1.equals(u2));

      u1.setLogin(login);
      u2.setLogin(login2);
      assertFalse(u1.equals(u2));

      final Categorie c = new Categorie();
      assertFalse(u1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws Exception Lance une exception.
    */
   public void testHashCode() throws Exception{
      final String login = "login";
      final Utilisateur u1 = new Utilisateur();
      u1.setLogin(login);
      final Utilisateur u2 = new Utilisateur();
      u2.setLogin(login);
      final Utilisateur u3 = new Utilisateur();

      assertTrue(u3.hashCode() > 0);

      final int hash = u1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(u1.hashCode() == u2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == u1.hashCode());
      assertTrue(hash == u1.hashCode());
      assertTrue(hash == u1.hashCode());
      assertTrue(hash == u1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Utilisateur u1 = utilisateurDao.findById(1);
      assertTrue(u1.toString().equals("{" + u1.getLogin() + "}"));

      final Utilisateur u2 = new Utilisateur();
      assertTrue(u2.toString().equals("{Empty Utilisateur}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Utilisateur u1 = utilisateurDao.findById(1);
      Utilisateur u2 = new Utilisateur();
      u2 = u1.clone();

      assertTrue(u1.equals(u2));

      if(u1.getUtilisateurId() != null){
         assertTrue(u1.getUtilisateurId() == u2.getUtilisateurId());
      }else{
         assertNull(u2.getUtilisateurId());
      }

      if(u1.getLogin() != null){
         assertTrue(u1.getLogin().equals(u2.getLogin()));
      }else{
         assertNull(u2.getLogin());
      }

      if(u1.getPassword() != null){
         assertTrue(u1.getPassword().equals(u2.getPassword()));
      }else{
         assertNull(u2.getPassword());
      }

      assertTrue(u1.isArchive() == u2.isArchive());

      if(u1.getEncodedPassword() != null){
         assertTrue(u1.getEncodedPassword().equals(u2.getEncodedPassword()));
      }else{
         assertNull(u2.getEncodedPassword());
      }

      if(u1.getDnLdap() != null){
         assertTrue(u1.getDnLdap().equals(u2.getDnLdap()));
      }else{
         assertNull(u2.getDnLdap());
      }

      if(u1.getEmail() != null){
         assertTrue(u1.getEmail().equals(u2.getEmail()));
      }else{
         assertNull(u2.getEmail());
      }

      if(u1.getTimeOut() != null){
         assertTrue(u1.getTimeOut().equals(u2.getTimeOut()));
      }else{
         assertNull(u2.getTimeOut());
      }

      if(u1.getCollaborateur() != null){
         assertTrue(u1.getCollaborateur().equals(u2.getCollaborateur()));
      }else{
         assertNull(u2.getCollaborateur());
      }

      assertTrue(u1.isSuperAdmin() == u2.isSuperAdmin());

      if(u1.getCodeSelects() != null){
         assertTrue(u1.getCodeSelects().equals(u2.getCodeSelects()));
      }else{
         assertNull(u2.getCodeSelects());
      }

      if(u1.getCodeUtilisateurs() != null){
         assertTrue(u1.getCodeUtilisateurs().equals(u2.getCodeUtilisateurs()));
      }else{
         assertNull(u2.getCodeUtilisateurs());
      }

      if(u1.getProfilUtilisateurs() != null){
         assertTrue(u1.getProfilUtilisateurs().equals(u2.getProfilUtilisateurs()));
      }else{
         assertNull(u2.getProfilUtilisateurs());
      }

      if(u1.getReservations() != null){
         assertTrue(u1.getReservations().equals(u2.getReservations()));
      }else{
         assertNull(u2.getReservations());
      }

      if(u1.getPlateformes() != null){
         assertTrue(u1.getPlateformes().equals(u2.getPlateformes()));
      }else{
         assertNull(u2.getPlateformes());
      }

      if(u1.getAffectationImprimantes() != null){
         assertTrue(u1.getAffectationImprimantes().equals(u2.getAffectationImprimantes()));
      }else{
         assertNull(u2.getAffectationImprimantes());
      }
      assertTrue(u1.getPlateformeOrig().equals(u2.getPlateformeOrig()));

   }

}
