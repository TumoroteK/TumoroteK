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
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.cession.CessionStatutDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.CessionStatut;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO CessionStatutDao et le bean
 * du domaine CessionStatut.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class CessionStatutDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CessionStatutDao cessionStatutDao;
   /** valeur du nom pour la maj. */
   private final String updatedStatut = "Mis a jour";

   /** Constructeur. */
   public CessionStatutDaoTest(){

   }

   public void setCessionStatutDao(final CessionStatutDao cDao){
      this.cessionStatutDao = cDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllCessionStatuts(){
      final List<CessionStatut> liste = cessionStatutDao.findAll();
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<CessionStatut> list = cessionStatutDao.findByOrder();
      assertTrue(list.size() == 3);
      assertTrue(list.get(1).getStatut().equals("REFUSEE"));
   }

   /**
    * Test l'appel de la méthode findByStatut().
    */
   public void testFindByStatut(){
      List<CessionStatut> liste = cessionStatutDao.findByStatut("VALIDEE");
      assertTrue(liste.size() == 1);

      liste = cessionStatutDao.findByStatut("VAL");
      assertTrue(liste.size() == 0);

      liste = cessionStatutDao.findByStatut("VAL%");
      assertTrue(liste.size() == 1);

      liste = cessionStatutDao.findByStatut(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrudCessionStatut() throws Exception{

      final CessionStatut cs = new CessionStatut();

      cs.setStatut("TEST");
      // Test de l'insertion
      cessionStatutDao.createObject(cs);
      assertEquals(new Integer(4), cs.getCessionStatutId());

      // Test de la mise à jour
      final CessionStatut cs2 = cessionStatutDao.findById(new Integer(4));
      assertNotNull(cs2);
      assertTrue(cs2.getStatut().equals("TEST"));
      cs2.setStatut(updatedStatut);
      cessionStatutDao.updateObject(cs2);
      assertTrue(cessionStatutDao.findById(new Integer(4)).getStatut().equals(updatedStatut));

      // Test de la délétion
      cessionStatutDao.removeObject(new Integer(4));
      assertNull(cessionStatutDao.findById(new Integer(4)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String statut = "STATUT";
      final String statut2 = "STATUT2";
      final CessionStatut cs1 = new CessionStatut();
      cs1.setStatut(statut);
      final CessionStatut cs2 = new CessionStatut();
      cs2.setStatut(statut);

      // L'objet 1 n'est pas égal à null
      assertFalse(cs1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(cs1.equals(cs1));
      // 2 objets sont égaux entre eux
      assertTrue(cs1.equals(cs2));
      assertTrue(cs2.equals(cs1));

      // Vérification de la différenciation de 2 objets
      cs2.setStatut(statut2);
      assertFalse(cs1.equals(cs2));
      assertFalse(cs2.equals(cs1));

      cs2.setStatut(null);
      assertFalse(cs1.equals(cs2));
      assertFalse(cs2.equals(cs1));

      cs1.setStatut(null);
      assertTrue(cs1.equals(cs2));
      cs2.setStatut(statut2);
      assertFalse(cs1.equals(cs2));

      final Categorie c = new Categorie();
      assertFalse(cs1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String statut = "STATUT";
      final CessionStatut cs1 = new CessionStatut();
      cs1.setStatut(statut);
      final CessionStatut cs2 = new CessionStatut();
      cs2.setStatut(statut);
      final CessionStatut cs3 = new CessionStatut();
      cs3.setStatut(null);
      assertTrue(cs3.hashCode() > 0);

      final int hash = cs1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(cs1.hashCode() == cs2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == cs1.hashCode());
      assertTrue(hash == cs1.hashCode());
      assertTrue(hash == cs1.hashCode());
      assertTrue(hash == cs1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final CessionStatut cs1 = cessionStatutDao.findById(1);
      assertTrue(cs1.toString().equals("{" + cs1.getStatut() + "}"));

      final CessionStatut cs2 = new CessionStatut();
      assertTrue(cs2.toString().equals("{Empty CessionStatut}"));
   }

}
