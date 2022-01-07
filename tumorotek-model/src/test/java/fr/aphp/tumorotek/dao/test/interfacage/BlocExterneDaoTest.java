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
package fr.aphp.tumorotek.dao.test.interfacage;

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



import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;

/**
 *
 * Classe de test pour le DAO BlocExterneDao
 * et le bean du domaine BlocExterne.
 *
 * @author Pierre Ventadour.
 * @version 05/10/2011
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class BlocExterneDaoTest extends AbstractDaoTest
{


   @Autowired
 BlocExterneDao blocExterneDao;

   @Autowired
 DossierExterneDao dossierExterneDao;

   public BlocExterneDaoTest(){

   }

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-interfacages-test-mysql.xml"};
   }

   @Test
public void setBlocExterneDao(final BlocExterneDao bDao){
      this.blocExterneDao = bDao;
   }

   @Test
public void setDossierExterneDao(final DossierExterneDao dDao){
      this.dossierExterneDao = dDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAll(){
      final List<BlocExterne> liste = IterableUtils.toList(blocExterneDao.findAll());
      assertTrue(liste.size() >= 9);
   }

   /**
    * Test l'appel de la méthode findByDossierExterne().
    */
   @Test
public void testFindByDossierExterne(){
      final DossierExterne d1 = dossierExterneDao.findById(1);
      List<BlocExterne> liste = blocExterneDao.findByDossierExterne(d1);
      assertTrue(liste.size() == 4);

      final DossierExterne d2 = dossierExterneDao.findById(2);
      liste = blocExterneDao.findByDossierExterne(d2);
      assertTrue(liste.size() == 0);

      liste = blocExterneDao.findByDossierExterne(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByDossierExterneAndEntite().
    */
   @Test
public void testFindByDossierExterneAndEntite(){
      final DossierExterne d1 = dossierExterneDao.findById(1);
      List<BlocExterne> liste = blocExterneDao.findByDossierExterneAndEntite(d1, 3);
      assertTrue(liste.size() == 2);

      liste = blocExterneDao.findByDossierExterneAndEntite(d1, 8);
      assertTrue(liste.size() == 0);

      final DossierExterne d2 = dossierExterneDao.findById(2);
      liste = blocExterneDao.findByDossierExterneAndEntite(d2, 3);
      assertTrue(liste.size() == 0);

      liste = blocExterneDao.findByDossierExterneAndEntite(null, 3);
      assertTrue(liste.size() == 0);

      liste = blocExterneDao.findByDossierExterneAndEntite(d1, null);
      assertTrue(liste.size() == 0);
   }

   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final BlocExterne b1 = new BlocExterne();
      final DossierExterne d1 = dossierExterneDao.findById(1);
      b1.setDossierExterne(d1);
      b1.setEntiteId(1);
      b1.setOrdre(2);

      final Integer id = IterableUtils.toList(blocExterneDao.findAll()).size() + 1;
      // Test de l'insertion
      blocExterneDao.save(b1);
      assertEquals(new Integer(id), b1.getBlocExterneId());

      final BlocExterne b2 = blocExterneDao.findById(new Integer(id));
      // Vérification des données entrées dans la base
      assertNotNull(b2);
      assertNotNull(b2.getDossierExterne());
      assertTrue(b2.getEntiteId() == 1);
      assertTrue(b2.getOrdre() == 2);

      // Test de la mise à jour
      b2.setEntiteId(3);
      b2.setOrdre(5);
      blocExterneDao.save(b2);
      assertTrue(blocExterneDao.findById(new Integer(id)).getEntiteId() == 3);
      assertTrue(blocExterneDao.findById(new Integer(id)).getOrdre() == 5);

      // Test de la délétion
      blocExterneDao.deleteById(new Integer(id));
      assertFalse(blocExterneDao.findById(new Integer(id)).isPresent());
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final DossierExterne d1 = dossierExterneDao.findById(1);
      final DossierExterne d2 = dossierExterneDao.findById(2);
      final Integer e1 = 1;
      final Integer e2 = 2;
      final BlocExterne b1 = new BlocExterne();
      final BlocExterne b2 = new BlocExterne();

      // L'objet 1 n'est pas égal à null
      assertFalse(b1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(b1.equals(b1));

      /*null*/
      assertTrue(b1.equals(b2));
      assertTrue(b2.equals(b1));

      /*ordre*/
      b2.setOrdre(o1);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setOrdre(o2);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setOrdre(o1);
      assertTrue(b1.equals(b2));
      assertTrue(b2.equals(b1));

      /*entite*/
      b2.setEntiteId(e1);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setEntiteId(e2);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setEntiteId(e1);
      assertTrue(b1.equals(b2));

      b2.setDossierExterne(d1);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setDossierExterne(d2);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setDossierExterne(d1);
      assertTrue(b1.equals(b2));
      assertTrue(b2.equals(b1));

      final Categorie c3 = new Categorie();
      assertFalse(b1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final DossierExterne d1 = dossierExterneDao.findById(1);
      final DossierExterne d2 = dossierExterneDao.findById(2);
      final Integer e1 = 1;
      final Integer e2 = 2;
      final BlocExterne b1 = new BlocExterne();
      final BlocExterne b2 = new BlocExterne();

      /*null*/
      assertTrue(b1.hashCode() == b2.hashCode());

      /*Ordre*/
      b2.setOrdre(o1);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setOrdre(o2);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setOrdre(o1);
      assertTrue(b1.hashCode() == b2.hashCode());

      b2.setEntiteId(e1);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setEntiteId(e2);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setEntiteId(e1);
      assertTrue(b1.hashCode() == b2.hashCode());

      b2.setDossierExterne(d1);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setDossierExterne(d2);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setDossierExterne(d1);
      assertTrue(b1.hashCode() == b2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = b1.hashCode();
      assertTrue(hash == b1.hashCode());
      assertTrue(hash == b1.hashCode());
      assertTrue(hash == b1.hashCode());
      assertTrue(hash == b1.hashCode());
   }

   /**
    * test toString().
    */
   @Test
public void testToString(){
      final BlocExterne b1 = blocExterneDao.findById(1);
      assertTrue(b1.toString().equals("{" + b1.getOrdre() + ", " + b1.getEntiteId() + "(Entite) "
         + b1.getDossierExterne().getIdentificationDossier() + "(DossierExterne)}"));

      final BlocExterne b2 = new BlocExterne();
      assertTrue(b2.toString().equals("{Empty BlocExterne}"));
   }

}
