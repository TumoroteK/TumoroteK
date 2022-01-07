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

import java.util.ArrayList;
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



import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.dao.interfacage.LogicielDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Logiciel;

/**
 *
 * Classe de test pour le DAO EmetteurDao et le bean du domaine Emetteur.
 *
 * @author Pierre Ventadour.
 * @version 04/10/2011
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class EmetteurDaoTest extends AbstractDaoTest
{


   @Autowired
 EmetteurDao emetteurDao;

   @Autowired
 LogicielDao logicielDao;

   public EmetteurDaoTest(){

   }

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-interfacages-test-mysql.xml"};
   }

   @Test
public void setEmetteurDao(final EmetteurDao eDao){
      this.emetteurDao = eDao;
   }

   @Test
public void setLogicielDao(final LogicielDao lDao){
      this.logicielDao = lDao;
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   @Test
public void testFindByOrder(){
      final List<Emetteur> liste = emetteurDao.findByOrder();
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getIdentification().equals("Apix Anapath"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAll(){
      final List<Emetteur> liste = IterableUtils.toList(emetteurDao.findAll());
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'appel de la méthode findByLogiciel().
    */
   @Test
public void testFindByLogiciel(){
      final Logiciel l1 = logicielDao.findById(1);
      List<Emetteur> liste = emetteurDao.findByLogiciel(l1);
      assertTrue(liste.size() == 2);

      final Logiciel l2 = logicielDao.findById(2);
      liste = emetteurDao.findByLogiciel(l2);
      assertTrue(liste.size() == 0);

      liste = emetteurDao.findByLogiciel(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByLogicielAndIdentification().
    */
   @Test
public void testFindByLogicielAndIdentification(){
      final Logiciel l1 = logicielDao.findById(1);
      List<Emetteur> liste = emetteurDao.findByLogicielAndIdentification(l1, "Apix Anapath");
      assertTrue(liste.size() == 1);

      liste = emetteurDao.findByLogicielAndIdentification(l1, "Apix");
      assertTrue(liste.size() == 0);

      final Logiciel l2 = logicielDao.findById(2);
      liste = emetteurDao.findByLogicielAndIdentification(l2, "Apix Anapath");
      assertTrue(liste.size() == 0);

      liste = emetteurDao.findByLogicielAndIdentification(null, "Apix Anapath");
      assertTrue(liste.size() == 0);

      liste = emetteurDao.findByLogicielAndIdentification(l1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIdentificationAndService().
    */
   @Test
public void testFindByIdentificationAndService(){
      List<Emetteur> liste = emetteurDao.findByIdentificationAndService("Apix Anapath", "ANAP");
      assertTrue(liste.size() == 1);

      liste = emetteurDao.findByIdentificationAndService("Apix", "ANAP");
      assertTrue(liste.size() == 0);

      liste = emetteurDao.findByIdentificationAndService("Apix Anapath", "AN");
      assertTrue(liste.size() == 0);

      liste = emetteurDao.findByIdentificationAndService(null, "ANAP");
      assertTrue(liste.size() == 0);

      liste = emetteurDao.findByIdentificationAndService("Apix Anapath", null);
      assertTrue(liste.size() == 0);
   }

   @Test
public void testFindByIdInList(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      List<Emetteur> liste = emetteurDao.findByIdInList(ids);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getIdentification().equals("Glims Hémato"));

      ids.add(2);
      liste = emetteurDao.findByIdInList(ids);
      assertTrue(liste.size() == 2);
      assertTrue(liste.get(0).getIdentification().equals("Apix Anapath"));

      liste = emetteurDao.findByIdInList(null);
      assertTrue(liste.size() == 0);
   }

   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final Emetteur e1 = new Emetteur();
      final Logiciel log = logicielDao.findById(1);
      e1.setLogiciel(log);
      e1.setIdentification("ID");

      // Test de l'insertion
      emetteurDao.save(e1);
      assertEquals(new Integer(4), e1.getEmetteurId());

      final Emetteur e2 = emetteurDao.findById(new Integer(4));
      // Vérification des données entrées dans la base
      assertNotNull(e2);
      assertNotNull(e2.getLogiciel());
      assertTrue(e2.getIdentification().equals("ID"));
      assertNull(e2.getService());
      assertNull(e2.getObservations());

      // Test de la mise à jour
      e2.setIdentification("ID2");
      e2.setService("SERV");
      e2.setObservations("OBS");
      emetteurDao.save(e2);
      assertTrue(emetteurDao.findById(new Integer(4)).getIdentification().equals("ID2"));
      assertTrue(emetteurDao.findById(new Integer(4)).getService().equals("SERV"));
      assertTrue(emetteurDao.findById(new Integer(4)).getObservations().equals("OBS"));

      // Test de la délétion
      emetteurDao.deleteById(new Integer(4));
      assertFalse(emetteurDao.findById(new Integer(4)).isPresent());
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String nom = "id1";
      final String nom2 = "id2";
      final Logiciel log1 = logicielDao.findById(1);
      final Logiciel log2 = logicielDao.findById(2);
      final Emetteur e1 = new Emetteur();
      final Emetteur e2 = new Emetteur();
      e1.setIdentification(nom);
      e1.setLogiciel(log1);
      e2.setIdentification(nom);
      e2.setLogiciel(log1);

      // L'objet 1 n'est pas égal à null
      assertFalse(e1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(e1.equals(e1));
      // 2 objets sont égaux entre eux
      assertTrue(e1.equals(e2));
      assertTrue(e2.equals(e1));

      e1.setLogiciel(null);
      e1.setIdentification(null);
      e2.setLogiciel(null);
      e2.setIdentification(null);
      assertTrue(e1.equals(e2));
      e2.setIdentification(nom);
      assertFalse(e1.equals(e2));
      e1.setIdentification(nom);
      assertTrue(e1.equals(e2));
      e2.setIdentification(nom2);
      assertFalse(e1.equals(e2));
      e2.setIdentification(null);
      assertFalse(e1.equals(e2));
      e2.setLogiciel(log1);
      assertFalse(e1.equals(e2));

      e1.setLogiciel(log1);
      e1.setIdentification(null);
      e2.setIdentification(null);
      e2.setLogiciel(log1);
      assertTrue(e1.equals(e2));
      e2.setLogiciel(log2);
      assertFalse(e1.equals(e2));
      e2.setIdentification(nom);
      assertFalse(e1.equals(e2));

      // Vérification de la différenciation de 2 objets
      e1.setIdentification(nom);
      assertFalse(e1.equals(e2));
      e2.setIdentification(nom2);
      e2.setLogiciel(log1);
      assertFalse(e1.equals(e2));

      final Categorie c3 = new Categorie();
      assertFalse(e1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String nom = "id1";
      final String nom2 = "id2";
      final Logiciel log1 = logicielDao.findById(1);
      final Logiciel log2 = logicielDao.findById(2);
      final Emetteur e1 = new Emetteur();
      final Emetteur e2 = new Emetteur();
      //null
      assertTrue(e1.hashCode() == e2.hashCode());

      //Nom
      e2.setIdentification(nom);
      assertFalse(e1.hashCode() == e2.hashCode());
      e1.setIdentification(nom2);
      assertFalse(e1.hashCode() == e2.hashCode());
      e1.setIdentification(nom);
      assertTrue(e1.hashCode() == e2.hashCode());

      //ProtocoleType
      e2.setLogiciel(log1);
      assertFalse(e1.hashCode() == e2.hashCode());
      e1.setLogiciel(log2);
      assertFalse(e1.hashCode() == e2.hashCode());
      e1.setLogiciel(log1);
      assertTrue(e1.hashCode() == e2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = e1.hashCode();
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());
      assertTrue(hash == e1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final Emetteur e1 = emetteurDao.findById(1);
      assertTrue(e1.toString().equals("{" + e1.getIdentification() + ", " + e1.getLogiciel().getNom() + "(Logiciel)}"));

      final Emetteur e2 = new Emetteur();
      assertTrue(e2.toString().equals("{Empty Emetteur}"));
   }

}
