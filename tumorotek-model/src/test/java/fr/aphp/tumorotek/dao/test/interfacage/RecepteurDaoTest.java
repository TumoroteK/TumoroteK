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
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.interfacage.LogicielDao;
import fr.aphp.tumorotek.dao.interfacage.RecepteurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.Logiciel;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

/**
 *
 * Classe de test pour le DAO RecepteurDao et le bean du domaine Recepteur.
 * Date 08/10/2014
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0.10.3
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class RecepteurDaoTest extends AbstractDaoTest
{

   private RecepteurDao recepteurDao;
   private LogicielDao logicielDao;

   public RecepteurDaoTest(){

   }

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-interfacages-test-mysql.xml"};
   }

   public void setRecepteurDao(final RecepteurDao eDao){
      this.recepteurDao = eDao;
   }

   public void setLogicielDao(final LogicielDao lDao){
      this.logicielDao = lDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<Recepteur> liste = recepteurDao.findAll();
      assertTrue(liste.size() == 2);
   }

   public void testFindByLogicielAndIdentification(){
      final Logiciel l2 = logicielDao.findById(2);
      List<Recepteur> liste = recepteurDao.findByLogicielAndIdentification(l2, "DIAMIC-ACK");
      assertTrue(liste.size() == 1);

      liste = recepteurDao.findByLogicielAndIdentification(l2, "Apix");
      assertTrue(liste.size() == 0);

      final Logiciel l3 = logicielDao.findById(3);
      liste = recepteurDao.findByLogicielAndIdentification(l3, "DIAMIC-ACK");
      assertTrue(liste.size() == 0);

      liste = recepteurDao.findByLogicielAndIdentification(l3, "DME");
      assertTrue(liste.size() == 1);

      liste = recepteurDao.findByLogicielAndIdentification(null, "DME");
      assertTrue(liste.size() == 0);

      liste = recepteurDao.findByLogicielAndIdentification(l3, null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByIdInList(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      List<Recepteur> liste = recepteurDao.findByIdInList(ids);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getIdentification().equals("DIAMIC-ACK"));

      ids.add(2);
      liste = recepteurDao.findByIdInList(ids);
      assertTrue(liste.size() == 2);
      assertTrue(liste.get(0).getIdentification().equals("DIAMIC-ACK"));
      assertTrue(liste.get(1).getIdentification().equals("DME"));

      liste = recepteurDao.findByIdInList(null);
      assertTrue(liste.size() == 0);
   }

   @Rollback(false)
   public void testCrud() throws Exception{

      final Recepteur e1 = new Recepteur();
      final Logiciel log = logicielDao.findById(1);
      e1.setLogiciel(log);
      e1.setIdentification("ID");

      // Test de l'insertion
      recepteurDao.createObject(e1);
      assertNotNull(e1.getRecepteurId());

      final Recepteur e2 = recepteurDao.findById(e1.getRecepteurId());
      // Vérification des données entrées dans la base
      assertNotNull(e2);
      assertNotNull(e2.getLogiciel());
      assertTrue(e2.getIdentification().equals("ID"));
      assertNull(e2.getObservations());

      // Test de la mise à jour
      e2.setIdentification("ID2");
      e2.setObservations("OBS");
      e2.setLogiciel(logicielDao.findById(3));
      recepteurDao.updateObject(e2);
      assertTrue(recepteurDao.findById(e1.getRecepteurId()).getIdentification().equals("ID2"));
      assertTrue(recepteurDao.findById(e1.getRecepteurId()).getObservations().equals("OBS"));
      assertTrue(recepteurDao.findById(e1.getRecepteurId()).getLogiciel().getLogicielId() == 3);

      // Test de la délétion
      recepteurDao.removeObject(e1.getRecepteurId());
      assertNull(recepteurDao.findById(e1.getRecepteurId()));
   }

   public void testEquals(){
      final String nom = "id1";
      final String nom2 = "id2";
      final Logiciel log1 = logicielDao.findById(1);
      final Logiciel log2 = logicielDao.findById(2);
      final Recepteur e1 = new Recepteur();
      final Recepteur e2 = new Recepteur();
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

   public void testHashCode(){
      final String nom = "id1";
      final String nom2 = "id2";
      final Logiciel log1 = logicielDao.findById(1);
      final Logiciel log2 = logicielDao.findById(2);
      final Recepteur e1 = new Recepteur();
      final Recepteur e2 = new Recepteur();
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
   public void testToString(){
      final Recepteur e1 = recepteurDao.findById(1);
      assertTrue(e1.toString().equals("{" + e1.getIdentification() + ", " + e1.getLogiciel().getNom() + "(Logiciel)}"));

      final Recepteur e2 = new Recepteur();
      assertTrue(e2.toString().equals("{Empty Recepteur}"));
   }

}
