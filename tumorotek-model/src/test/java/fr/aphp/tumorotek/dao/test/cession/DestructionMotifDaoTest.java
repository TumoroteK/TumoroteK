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

import org.springframework.test.annotation.Rollback;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.test.Config;



import fr.aphp.tumorotek.dao.cession.DestructionMotifDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO DestructionMotifDao et le bean
 * du domaine DestructionMotif.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class DestructionMotifDaoTest extends AbstractDaoTest
{


   @Autowired
 DestructionMotifDao destructionMotifDao;
   @Autowired
 PlateformeDao plateformeDao;

   @Autowired
 final String updatedMotif = "Mis a jour";

   /** Constructeur. */
   public DestructionMotifDaoTest(){

   }

   @Test
public void setDestructionMotifDao(final DestructionMotifDao dDao){
      this.destructionMotifDao = dDao;
   }

   @Test
public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllDestructionMotifs(){
      final List<DestructionMotif> liste = IterableUtils.toList(destructionMotifDao.findAll());
      assertTrue(liste.size() == 3);
   }

   @Test
public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = destructionMotifDao.findByPfOrder(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("INUTILISABLE"));
      pf = plateformeDao.findById(2);
      list = destructionMotifDao.findByPfOrder(pf);
      assertTrue(list.size() == 1);
      list = destructionMotifDao.findByPfOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByMotif().
    */
   @Test
public void testFindByMotif(){
      List<DestructionMotif> liste = destructionMotifDao.findByMotif("INUTILISABLE");
      assertTrue(liste.size() == 1);

      liste = destructionMotifDao.findByMotif("INUTIL");
      assertTrue(liste.size() == 0);

      liste = destructionMotifDao.findByMotif("INUTIL%");
      assertTrue(liste.size() == 1);

      liste = destructionMotifDao.findByMotif(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<DestructionMotif> liste = destructionMotifDao.findByExcludedId(1);
      assertTrue(liste.size() == 2);
      final DestructionMotif motif = liste.get(0);
      assertNotNull(motif);
      assertTrue(motif.getDestructionMotifId() == 2);

      liste = destructionMotifDao.findByExcludedId(15);
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un DestructionMotif.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrudDestructionMotif() throws Exception{

      final DestructionMotif de = new DestructionMotif();
      de.setPlateforme(plateformeDao.findById(1));
      de.setMotif("TEST");
      // Test de l'insertion
      destructionMotifDao.save(de);
      assertEquals(new Integer(4), de.getDestructionMotifId());

      // Test de la mise à jour
      final DestructionMotif de2 = destructionMotifDao.findById(new Integer(4));
      assertNotNull(de2);
      assertTrue(de2.getMotif().equals("TEST"));
      de2.setMotif(updatedMotif);
      destructionMotifDao.save(de2);
      assertTrue(destructionMotifDao.findById(new Integer(4)).getMotif().equals(updatedMotif));

      // Test de la délétion
      destructionMotifDao.deleteById(new Integer(4));
      assertFalse(destructionMotifDao.findById(new Integer(4)).isPresent());

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String motif = "MOTIF";
      final String motif2 = "MOTIF2";
      final DestructionMotif de1 = new DestructionMotif();
      de1.setMotif(motif);
      final DestructionMotif de2 = new DestructionMotif();
      de2.setMotif(motif);

      // L'objet 1 n'est pas égal à null
      assertFalse(de1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(de1.equals(de1));
      // 2 objets sont égaux entre eux
      assertTrue(de1.equals(de2));
      assertTrue(de2.equals(de1));

      // Vérification de la différenciation de 2 objets
      de2.setMotif(motif2);
      assertFalse(de1.equals(de2));
      assertFalse(de2.equals(de1));

      de2.setMotif(null);
      assertFalse(de1.equals(de2));
      assertFalse(de2.equals(de1));

      de1.setMotif(null);
      assertTrue(de1.equals(de2));
      de2.setMotif(motif);
      assertFalse(de1.equals(de2));

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      de1.setMotif(de2.getMotif());
      de1.setPlateforme(pf1);
      de2.setPlateforme(pf1);
      assertTrue(de1.equals(de2));
      de2.setPlateforme(pf2);
      assertFalse(de1.equals(de2));

      final Categorie c = new Categorie();
      assertFalse(de1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String motif = "MOTIF";
      final DestructionMotif de1 = new DestructionMotif();
      de1.setMotif(motif);
      final DestructionMotif de2 = new DestructionMotif();
      de2.setMotif(motif);
      final DestructionMotif de3 = new DestructionMotif();
      de3.setMotif(null);
      assertTrue(de3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      de1.setPlateforme(pf1);
      de2.setPlateforme(pf1);
      de3.setPlateforme(pf2);

      final int hash = de1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(de1.hashCode() == de2.hashCode());
      assertFalse(de1.hashCode() == de3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == de1.hashCode());
      assertTrue(hash == de1.hashCode());
      assertTrue(hash == de1.hashCode());
      assertTrue(hash == de1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final DestructionMotif de1 = destructionMotifDao.findById(1);
      assertTrue(de1.toString().equals("{" + de1.getMotif() + "}"));

      final DestructionMotif de2 = new DestructionMotif();
      assertTrue(de2.toString().equals("{Empty DestructionMotif}"));
   }

}
