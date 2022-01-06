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
package fr.aphp.tumorotek.dao.test.ui;


import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.ui.UiRequeteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.ui.UiCompValue;
import fr.aphp.tumorotek.model.ui.UiRequete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Date: 21/07/2014
 * @author Mathieu BARTHELEMY
 * @version 2.11
 *
 * Fonctionalité non terminée... seul le modele implémenté
 */

//@RunWith(SpringRunner.class)
//@ContextConfiguration(locations = {"classpath:applicationContextInterceptor.xml", "classpath:spring-jpa-test-mysql.xml", "classpath:applicationContextDao.xml" }) 
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
// @RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class UiRequeteDaoTest extends AbstractDaoTest {

   @Autowired
 UiRequeteDao uiRequeteDao;
   @Autowired
 EntiteDao entiteDao;
   @Autowired
 UtilisateurDao utilisateurDao;

   public UiRequeteDaoTest(){

   }

   @Test
public void setUiRequeteDao(final UiRequeteDao u){
      this.uiRequeteDao = u;
   }

   @Test
public void setEntiteDao(final EntiteDao e){
      this.entiteDao = e;
   }

   @Test
public void setUtilisateurDao(final UtilisateurDao t){
      this.utilisateurDao = t;
   }

   @Test
   @Test
public void testFindById(){
      UiRequete r = uiRequeteDao.findById(1);
      assertNotNull(r);

      r = uiRequeteDao.findById(100);
      assertNull(r);
   }

   @Test
public void testFindAll(){
      final List<UiRequete> liste = IterableUtils.toList(uiRequeteDao.findAll());
      assertTrue(liste.size() == 4);
   }

   @Test
public void testFindByUtilisateurAndEntite(){
      List<UiRequete> reqs = uiRequeteDao.findByUtilisateurAndEntite(utilisateurDao.findById(1), entiteDao.findById(2));
      assertTrue(reqs.size() == 3);
      assertTrue(reqs.get(0).getOrdre() == 1);
      assertTrue(reqs.get(2).getOrdre() == 3);

      reqs = uiRequeteDao.findByUtilisateurAndEntite(utilisateurDao.findById(1), entiteDao.findById(3));
      assertTrue(reqs.isEmpty());

      reqs = uiRequeteDao.findByUtilisateurAndEntite(utilisateurDao.findById(5), entiteDao.findById(3));
      assertTrue(reqs.size() == 1);

      reqs = uiRequeteDao.findByUtilisateurAndEntite(utilisateurDao.findById(5), null);
      assertTrue(reqs.isEmpty());

      reqs = uiRequeteDao.findByUtilisateurAndEntite(null, entiteDao.findById(2));
      assertTrue(reqs.isEmpty());
   }

   @Test
public void testFindByNomUtilisateurAndEntite(){
      List<UiRequete> reqs =
         uiRequeteDao.findByNomUtilisateurAndEntite(utilisateurDao.findById(1), entiteDao.findById(2), "REQ1");
      assertTrue(reqs.size() == 1);

      reqs = uiRequeteDao.findByNomUtilisateurAndEntite(utilisateurDao.findById(1), entiteDao.findById(2), "REQ2");
      assertTrue(reqs.size() == 1);

      reqs = uiRequeteDao.findByNomUtilisateurAndEntite(utilisateurDao.findById(1), entiteDao.findById(2), "DAVID");
      assertTrue(reqs.isEmpty());

      reqs = uiRequeteDao.findByNomUtilisateurAndEntite(utilisateurDao.findById(1), entiteDao.findById(2), null);
      assertTrue(reqs.isEmpty());

      reqs = uiRequeteDao.findByNomUtilisateurAndEntite(null, entiteDao.findById(2), "REQ2");
      assertTrue(reqs.isEmpty());

      reqs = uiRequeteDao.findByNomUtilisateurAndEntite(utilisateurDao.findById(1), null, "REQ2");
      assertTrue(reqs.isEmpty());
   }

   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final UiRequete r1 = new UiRequete(null, utilisateurDao.findById(2), entiteDao.findById(8), "TESTREQ", 2);

      final UiCompValue u1 = new UiCompValue(null, r1, "codeDeriveBox", "textbox", null, "PTRA.12", null, null);
      r1.getUiCompValues().add(u1);
      final UiCompValue u2 = new UiCompValue(null, r1, "annoDeriveBox123", "listbox", 3, null, null, null);
      r1.getUiCompValues().add(u2);

      // Test de l'insertion
      uiRequeteDao.save(r1);
      final Integer idR1 = r1.getUiRequeteId();
      assertNotNull(idR1);

      final UiRequete r2 = uiRequeteDao.findById(idR1);
      // Vérification des données entrées dans la base
      assertTrue(r2.getUtilisateur().getUtilisateurId() == 2);
      assertTrue(r2.getEntite().getEntiteId() == 8);
      assertTrue(r2.getNom().equals("TESTREQ"));
      assertTrue(r2.getOrdre() == 2);
      assertTrue(r2.getUiCompValues().size() == 2);
      Iterator<UiCompValue> it = r2.getUiCompValues().iterator();
      final UiCompValue v1 = it.next();
      assertTrue(v1.getUiCompValueId() == 6);
      assertTrue(v1.getUiRequete().getNom().equals("TESTREQ"));
      assertTrue(v1.getIdComponent().equals("codeDeriveBox"));
      assertTrue(v1.getComponentClass().equals("textbox"));
      assertNull(v1.getIndexValue());
      assertNull(v1.getCalendarValue());
      assertNull(v1.getCheckValue());
      assertTrue(v1.getTextValue().equals("PTRA.12"));
      UiCompValue v2 = it.next();

      // Test de la mise à jour
      r2.setOrdre(1);
      r2.setNom("TESTREQ22");
      final UiCompValue u3 = new UiCompValue(null, r2, "sexeMFBox", "checkbox", null, null, true, null);
      r2.getUiCompValues().clear();
      r2.getUiCompValues().add(v2);
      r2.getUiCompValues().add(u3);

      uiRequeteDao.save(r2);

      final UiRequete r3 = uiRequeteDao.findById(idR1);
      assertTrue(r3.getUtilisateur().getUtilisateurId() == 2);
      assertTrue(r3.getEntite().getEntiteId() == 8);
      assertTrue(r3.getNom().equals("TESTREQ22"));
      assertTrue(r3.getOrdre() == 1);
      assertTrue(r3.getUiCompValues().size() == 2);
      it = r3.getUiCompValues().iterator();
      v2 = it.next();
      assertTrue(v2.getUiCompValueId() == 7);
      assertTrue(v2.getUiRequete().getNom().equals("TESTREQ22"));
      assertTrue(v2.getIdComponent().equals("annoDeriveBox123"));
      assertTrue(v2.getComponentClass().equals("listbox"));
      assertTrue(v2.getIndexValue() == 3);
      assertNull(v2.getCalendarValue());
      assertNull(v2.getCheckValue());
      assertNull(v2.getTextValue());
      final UiCompValue v3 = it.next();
      assertTrue(v3.getUiCompValueId() == 8);
      assertTrue(v3.getUiRequete().getNom().equals("TESTREQ22"));
      assertTrue(v3.getIdComponent().equals("sexeMFBox"));
      assertTrue(v3.getComponentClass().equals("checkbox"));
      assertNull(v3.getIndexValue());
      assertNull(v3.getCalendarValue());
      assertTrue(v3.getCheckValue());
      assertNull(v3.getTextValue());

      // Test de la délétion
      uiRequeteDao.deleteById(idR1);
      assertNull(uiRequeteDao.findById(idR1));

      testFindAll();
   }

   @Test
public void testEquals(){
      final String n1 = "nom1";
      final String n2 = "nom2";
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final UiRequete r1 = new UiRequete();
      final UiRequete r2 = new UiRequete();

      // nulls
      assertTrue(r1.equals(r1));
      // 2 objets sont égaux entre eux
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));

      r1.setNom(n1);
      r1.setUtilisateur(u1);
      r1.setEntite(e1);
      r2.setNom(n1);
      r2.setUtilisateur(u1);
      r2.setEntite(e1);

      // L'objet 1 n'est pas égal à null
      assertFalse(r1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(r1.equals(r1));
      // 2 objets sont égaux entre eux
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));

      // nom
      r1.setUtilisateur(null);
      r1.setEntite(null);
      r2.setUtilisateur(null);
      r2.setEntite(null);
      assertTrue(r1.equals(r2));
      r2.setNom(n2);
      assertFalse(r1.equals(r2));
      r1.setNom(n2);
      assertTrue(r1.equals(r2));
      r2.setNom(n1);
      assertFalse(r1.equals(r2));
      r1.setNom("nom1");
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      r2.setNom(null);
      assertFalse(r1.equals(r2));

      // Utilisateur
      r1.setNom(null);
      r2.setNom(null);
      r1.setUtilisateur(u1);
      r2.setUtilisateur(u1);
      assertTrue(r1.equals(r2));
      r2.setUtilisateur(u2);
      assertFalse(r1.equals(r2));
      r1.setUtilisateur(u2);
      assertTrue(r1.equals(r2));
      r2.setUtilisateur(u1);
      assertFalse(r1.equals(r2));
      final Utilisateur u3 = new Utilisateur();
      u3.setLogin("USER1");
      r1.setUtilisateur(u3);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      r2.setUtilisateur(null);
      assertFalse(r1.equals(r2));

      // Entite
      r1.setUtilisateur(null);
      r1.setEntite(e1);
      r2.setEntite(e1);
      assertTrue(r1.equals(r2));
      r2.setEntite(e2);
      assertFalse(r1.equals(r2));
      r1.setEntite(e2);
      assertTrue(r1.equals(r2));
      r2.setEntite(e1);
      assertFalse(r1.equals(r2));
      final Entite e3 = new Entite();
      e3.setNom("Patient");
      r1.setEntite(e3);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      r2.setEntite(null);
      assertFalse(r1.equals(r2));

      final Categorie c3 = new Categorie();
      assertFalse(r1.equals(c3));
   }

   @Test
public void testHashCode(){
      final String n1 = "nom1";
      final String n2 = "nom2";
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final UiRequete r1 = new UiRequete();
      final UiRequete r2 = new UiRequete();

      // nulls
      assertTrue(r1.hashCode() == r1.hashCode());
      // 2 objets sont égaux entre eux
      assertTrue(r1.hashCode() == r2.hashCode());

      r1.setNom(n1);
      r1.setUtilisateur(u1);
      r1.setEntite(e1);
      r2.setNom(n1);
      r2.setUtilisateur(u1);
      r2.setEntite(e1);

      // L'objet 1 est égale à lui même
      assertTrue(r1.hashCode() == r1.hashCode());
      // 2 objets sont égaux entre eux
      assertTrue(r1.hashCode() == r2.hashCode());

      // nom
      r1.setUtilisateur(null);
      r1.setEntite(null);
      r2.setUtilisateur(null);
      r2.setEntite(null);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setNom(n2);
      assertFalse(r1.hashCode() == r2.hashCode());
      r1.setNom(n2);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setNom(n1);
      assertFalse(r1.hashCode() == r2.hashCode());
      r1.setNom("nom1");
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setNom(null);
      assertFalse(r1.hashCode() == r2.hashCode());

      // Utilisateur
      r1.setNom(null);
      r2.setNom(null);
      r1.setUtilisateur(u1);
      r2.setUtilisateur(u1);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setUtilisateur(u2);
      assertFalse(r1.hashCode() == r2.hashCode());
      r1.setUtilisateur(u2);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setUtilisateur(u1);
      assertFalse(r1.hashCode() == r2.hashCode());
      final Utilisateur u3 = new Utilisateur();
      u3.setLogin("USER1");
      r1.setUtilisateur(u3);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setUtilisateur(null);
      assertFalse(r1.hashCode() == r2.hashCode());

      // Entite
      r1.setUtilisateur(null);
      r1.setEntite(e1);
      r2.setEntite(e1);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setEntite(e2);
      assertFalse(r1.hashCode() == r2.hashCode());
      r1.setEntite(e2);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setEntite(e1);
      assertFalse(r1.hashCode() == r2.hashCode());
      final Entite e3 = new Entite();
      e3.setNom("Patient");
      r1.setEntite(e3);
      assertTrue(r1.hashCode() == r2.hashCode());
      r2.setEntite(null);
      assertFalse(r1.hashCode() == r2.hashCode());
   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
  //    assertTrue(uiRequeteDao.findById(1).toString().equals("{REQ1, Prelevement}"));
      assertTrue(new UiRequete().toString().equals("{Empty UiRequete}"));
   }
}
