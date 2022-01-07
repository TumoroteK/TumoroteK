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
package fr.aphp.tumorotek.dao.test.cession;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.cession.CessionExamenDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.Config;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO CessionExamenDao et le bean
 * du domaine CessionExamen.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class}) 
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class CessionExamenDaoTest extends AbstractDaoTest
{

   @Autowired
   @Autowired
 CessionExamenDao cessionExamenDao;
   
   @Autowired
   @Autowired
 PlateformeDao plateformeDao;

   /** valeur du nom pour la maj. */
   @Autowired
 final String updatedExamen = "Mis a jour";

   /** Constructeur. */
   public CessionExamenDaoTest(){

   }

   @Test
public void setCessionExamenDao(final CessionExamenDao cDao){
      this.cessionExamenDao = cDao;
   }

   @Test
public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllCessionExamens(){
      final List<CessionExamen> qualites = IterableUtils.toList(cessionExamenDao.findAll());
      assertTrue(qualites.size() == 4);
   }

   @Test
public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = cessionExamenDao.findByPfOrder(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("ANALYSE 1"));
      pf = plateformeDao.findById(2);
      list = cessionExamenDao.findByPfOrder(pf);
      assertTrue(list.size() == 1);
      list = cessionExamenDao.findByPfOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExamen().
    */
   @Test
public void testFindByExamen(){
      List<CessionExamen> liste = cessionExamenDao.findByExamen("ANALYSE 1");
      assertTrue(liste.size() == 1);

      liste = cessionExamenDao.findByExamen("ANALY");
      assertTrue(liste.size() == 0);

      liste = cessionExamenDao.findByExamen("ANALY%");
      assertTrue(liste.size() == 2);

      liste = cessionExamenDao.findByExamen(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByExamenEn().
    */
   @Test
public void testFindByExamenEn(){
      List<CessionExamen> liste = cessionExamenDao.findByExamenEn("ANALYSE_EN 1");
      assertTrue(liste.size() == 1);

      liste = cessionExamenDao.findByExamenEn("ANALY");
      assertTrue(liste.size() == 0);

      liste = cessionExamenDao.findByExamenEn("ANALY%");
      assertTrue(liste.size() == 2);

      liste = cessionExamenDao.findByExamenEn(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<CessionExamen> liste = cessionExamenDao.findByExcludedId(1);
      assertTrue(liste.size() == 3);
      final CessionExamen exam = liste.get(0);
      assertNotNull(exam);
      assertTrue(exam.getId() == 2);

      liste = cessionExamenDao.findByExcludedId(15);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrudCessionExamen() throws Exception{

      final CessionExamen ce = new CessionExamen();

      ce.setNom("TEST");
      ce.setPlateforme(plateformeDao.findById(1));
      // Test de l'insertion
      cessionExamenDao.save(ce);
      assertEquals(new Integer(5), ce.getId());

      // Test de la mise à jour
      final CessionExamen ce2 = cessionExamenDao.findById(new Integer(5));
      assertNotNull(ce2);
      assertTrue(ce2.getNom().equals("TEST"));
      ce2.setNom(updatedExamen);
      cessionExamenDao.save(ce2);
      assertTrue(cessionExamenDao.findById(new Integer(5)).getNom().equals(updatedExamen));

      // Test de la délétion
      cessionExamenDao.deleteById(new Integer(5));
      assertFalse(cessionExamenDao.findById(new Integer(5)).isPresent());

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String exam = "EXAM";
      final String exam2 = "EXAM2";
      final CessionExamen ce1 = new CessionExamen();
      ce1.setNom(exam);
      final CessionExamen ce2 = new CessionExamen();
      ce2.setNom(exam);

      // L'objet 1 n'est pas égal à null
      assertFalse(ce1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ce1.equals(ce1));
      // 2 objets sont égaux entre eux
      assertTrue(ce1.equals(ce2));
      assertTrue(ce2.equals(ce1));

      // Vérification de la différenciation de 2 objets
      ce2.setNom(exam2);
      assertFalse(ce1.equals(ce2));
      assertFalse(ce2.equals(ce1));

      ce2.setNom(null);
      assertFalse(ce1.equals(ce2));
      assertFalse(ce2.equals(ce1));

      ce1.setNom(null);
      assertTrue(ce1.equals(ce2));
      ce2.setNom(exam);
      assertFalse(ce1.equals(ce2));

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      ce1.setNom(ce2.getNom());
      ce1.setPlateforme(pf1);
      ce2.setPlateforme(pf1);
      assertTrue(ce1.equals(ce2));
      ce2.setPlateforme(pf2);
      assertFalse(ce1.equals(ce2));

      final Categorie c = new Categorie();
      assertFalse(ce1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String exam = "Exam";
      final CessionExamen ce1 = new CessionExamen();
      ce1.setNom(exam);
      final CessionExamen ce2 = new CessionExamen();
      ce2.setNom(exam);
      final CessionExamen ce3 = new CessionExamen();
      ce3.setNom(null);
      ce3.setExamenEn(null);
      assertTrue(ce3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      ce1.setPlateforme(pf1);
      ce2.setPlateforme(pf1);
      ce3.setPlateforme(pf2);

      final int hash = ce1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(ce1.hashCode() == ce2.hashCode());
      assertFalse(ce1.hashCode() == ce3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == ce1.hashCode());
      assertTrue(hash == ce1.hashCode());
      assertTrue(hash == ce1.hashCode());
      assertTrue(hash == ce1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final CessionExamen ce1 = cessionExamenDao.findById(1);
      assertTrue(ce1.toString().equals("{" + ce1.getNom() + "}"));

      final CessionExamen ce2 = new CessionExamen();
      assertTrue(ce2.toString().equals("{Empty CessionExamen}"));
   }

}
