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
package fr.aphp.tumorotek.dao.test.code;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO CodeOossierDao et le
 * bean du domaine CodeDossier.
 * Classe de test créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CodeDossierDaoTest extends AbstractDaoTest
{

   @Autowired
 CodeDossierDao codeDossierDao;
   @Autowired
 UtilisateurDao utilisateurDao;
   @Autowired
 BanqueDao banqueDao;

   public CodeDossierDaoTest(){}

   @Test
public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   @Test
public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   @Test
public void setCodeDossierDao(final CodeDossierDao cDosDao){
      this.codeDossierDao = cDosDao;
   }

   @Test
public void testReadAllCodeDossiers(){
      final List<CodeDossier> codes = IterableUtils.toList(codeDossierDao.findAll());
      assertTrue(codes.size() == 4);
   }

   @Test
public void testFindByNomLike(){
      final Banque b = banqueDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findByNomLike("DossierU%", b);
      assertTrue(doss.size() == 2);
      doss = codeDossierDao.findByNomLike("code4", b);
      assertTrue(doss.size() == 0);
   }

   @Test
public void findByCodeDossierParent(){
      CodeDossier dos = codeDossierDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findByCodeDossierParent(dos);
      assertTrue(doss.size() == 1);
      dos = codeDossierDao.findById(3);
      doss = codeDossierDao.findByCodeDossierParent(dos);
      assertTrue(doss.size() == 0);
   }

   @Test
public void findByRootCodeDossierUtilisateur(){
      final Banque b = banqueDao.findById(1);
      final List<CodeDossier> doss = codeDossierDao.findByRootCodeDossierUtilisateur(b);
      assertTrue(doss.size() == 2);
   }

   @Test
public void findByRootCodeDossierSelect(){
      final Banque b = banqueDao.findById(1);
      Utilisateur u = utilisateurDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findByRootCodeDossierSelect(u, b);
      assertTrue(doss.size() == 1);
      assertTrue(doss.get(0).getCodeDossierId() == 3);
      u = utilisateurDao.findById(2);
      doss = codeDossierDao.findByRootCodeDossierSelect(u, b);
      assertTrue(doss.size() == 0);
   }

   @Test
public void testFindBySelectUtilisateurAndBanque(){
      Utilisateur u = utilisateurDao.findById(1);
      final Banque b = banqueDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findBySelectUtilisateurAndBanque(u, b);
      assertTrue(doss.size() == 1);
      u = utilisateurDao.findById(2);
      doss = codeDossierDao.findBySelectUtilisateurAndBanque(u, b);
      assertTrue(doss.size() == 0);
   }

   @Test
public void testFindByUtilisateurAndBanque(){
      Utilisateur u = utilisateurDao.findById(1);
      Banque b = banqueDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findByUtilisateurAndBanque(u, b);
      assertTrue(doss.size() == 2);
      u = utilisateurDao.findById(2);
      doss = codeDossierDao.findByUtilisateurAndBanque(u, b);
      assertTrue(doss.size() == 1);
      b = banqueDao.findById(2);
      doss = codeDossierDao.findByUtilisateurAndBanque(u, b);
      assertTrue(doss.size() == 0);
   }

   @Test
public void findByExcludedId(){
      final CodeDossier c = codeDossierDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findByExcludedId(c.getCodeDossierId());
      assertTrue(doss.size() == 3);
      doss = codeDossierDao.findByExcludedId(8);
      assertTrue(doss.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un dossier.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   @Test
public void testCrud() throws Exception{
      final CodeDossier c = new CodeDossier();

      final Utilisateur u = utilisateurDao.findById(1);
      final Banque b = banqueDao.findById(1);
      final CodeDossier dos = codeDossierDao.findById(2);
      c.setUtilisateur(u);
      c.setBanque(b);
      c.setNom("new dossier");
      c.setDossierParent(dos);
      c.setCodeSelect(true);
      // Test de l'insertion
      codeDossierDao.save(c);
      assertEquals(new Integer(5), c.getCodeDossierId());

      // Test de la mise à jour
      final CodeDossier c2 = codeDossierDao.findById(new Integer(5));
      assertNotNull(c2);
      assertTrue(c2.getNom().equals("new dossier"));
      assertTrue(c2.getCodeSelect());
      assertNotNull(c2.getBanque());
      assertNotNull(c2.getUtilisateur());
      assertNotNull(c2.getDossierParent().equals(dos));
      c2.setNom("update");
      c2.setDossierParent(null);
      codeDossierDao.save(c2);
      assertTrue(codeDossierDao.findById(new Integer(5)).getNom() == "update");

      // Test de la délétion
      codeDossierDao.deleteById(new Integer(5));
      assertFalse(codeDossierDao.findById(new Integer(5)).isPresent());

   }

   /**
    * Test des méthodes surchargées "equals" et hashcode.
    */
   @Test
public void testEqualsAndHashCode(){
      final CodeDossier c1 = new CodeDossier();
      final CodeDossier c2 = new CodeDossier();
      assertFalse(c1.equals(null));
      assertNotNull(c2);
      assertTrue(c1.equals(c1));
      assertTrue(c1.equals(c2));
      assertTrue(c1.hashCode() == c2.hashCode());

      final String s1 = "nom1";
      final String s2 = "nom2";
      final String s3 = new String("nom2");

      c1.setNom(s1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setNom(s2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setNom(s2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setNom(s3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final Utilisateur u3 = new Utilisateur();
      u3.setLogin(u2.getLogin());
      assertFalse(u1.equals(u2));
      assertFalse(u1.hashCode() == u2.hashCode());
      assertTrue(u2.equals(u3));
      c1.setUtilisateur(u1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setUtilisateur(u2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setUtilisateur(u3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setUtilisateur(u2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = new Banque();
      b3.setNom(b2.getNom());
      b3.setPlateforme(b2.getPlateforme());
      assertFalse(b1.equals(b2));
      assertFalse(b1.hashCode() == u2.hashCode());
      assertTrue(b2.equals(b3));
      c1.setBanque(b1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setBanque(b2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setBanque(b3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setBanque(b2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      // dummy
      final Categorie c = new Categorie();
      assertFalse(c1.equals(c));
   }

   @Test
public void testToString(){
      CodeDossier c1 = codeDossierDao.findById(1);
      assertTrue(c1.toString().equals("{CodeDossier: " + c1.getNom() + "}"));

      c1 = new CodeDossier();
      assertTrue(c1.toString().equals("{Empty CodeDossier}"));
   }

   @Test
public void testClone(){
      final CodeDossier c1 = codeDossierDao.findById(1);
      c1.setDossierParent(codeDossierDao.findById(2)); // pour eviter null
      final CodeDossier clone = c1.clone();
      assertTrue(c1.equals(clone));
      assertTrue(c1.hashCode() == clone.hashCode());
      assertEquals(c1.getCodeDossierId(), clone.getCodeDossierId());
      assertEquals(c1.getNom(), clone.getNom());
      assertEquals(c1.getDescription(), clone.getDescription());
      assertEquals(c1.getUtilisateur(), clone.getUtilisateur());
      assertEquals(c1.getBanque(), clone.getBanque());
      assertEquals(c1.getDossierParent(), clone.getDossierParent());
      assertEquals(c1.getCodeSelect(), clone.getCodeSelect());
   }

   @Test
public void testFindByRootDossierBanque(){
      Banque b = banqueDao.findById(1);
      List<CodeDossier> doss = codeDossierDao.findByRootDossierBanque(b, true);
      assertTrue(doss.size() == 1);
      assertTrue(doss.get(0).getCodeDossierId() == 3);
      doss = codeDossierDao.findByRootDossierBanque(b, false);
      assertTrue(doss.size() == 2);
      doss = codeDossierDao.findByRootDossierBanque(b, null);
      assertTrue(doss.size() == 0);
      b = banqueDao.findById(2);
      doss = codeDossierDao.findByRootDossierBanque(b, false);
      assertTrue(doss.size() == 0);
      doss = codeDossierDao.findByRootDossierBanque(null, false);
      assertTrue(doss.size() == 0);
      doss = codeDossierDao.findByRootDossierBanque(b, false);
      assertTrue(doss.size() == 0);
   }

}
