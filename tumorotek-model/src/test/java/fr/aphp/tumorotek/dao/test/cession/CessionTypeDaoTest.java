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

import fr.aphp.tumorotek.dao.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO CessionTypeDao et le bean
 * du domaine CessionType.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class CessionTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CessionTypeDao cessionTypeDao;
   /** valeur du nom pour la maj. */
   private final String updatedType = "Mis a jour";

   /** Constructeur. */
   public CessionTypeDaoTest(){

   }

   public void setCessionTypeDao(final CessionTypeDao cDao){
      this.cessionTypeDao = cDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllCessionTypes(){
      final List<CessionType> liste = cessionTypeDao.findAll();
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<CessionType> liste = cessionTypeDao.findByOrder();
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getType().equals("Destruction"));
   }

   /**
    * Test l'appel de la méthode findByExamen().
    */
   public void testFindByExamen(){
      List<CessionType> liste = cessionTypeDao.findByType("SANITAIRE");
      assertTrue(liste.size() == 1);

      liste = cessionTypeDao.findByType("SAN");
      assertTrue(liste.size() == 0);

      liste = cessionTypeDao.findByType("SAN%");
      assertTrue(liste.size() == 1);

      liste = cessionTypeDao.findByType(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un CessionType.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrudCessionType() throws Exception{

      final CessionType ct = new CessionType();

      ct.setType("TEST");
      // Test de l'insertion
      cessionTypeDao.createObject(ct);
      assertEquals(new Integer(4), ct.getCessionTypeId());

      // Test de la mise à jour
      final CessionType ct2 = cessionTypeDao.findById(new Integer(4));
      assertNotNull(ct2);
      assertTrue(ct2.getType().equals("TEST"));
      ct2.setType(updatedType);
      cessionTypeDao.updateObject(ct2);
      assertTrue(cessionTypeDao.findById(new Integer(4)).getType().equals(updatedType));

      // Test de la délétion
      cessionTypeDao.removeObject(new Integer(4));
      assertNull(cessionTypeDao.findById(new Integer(4)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String type = "TYPE";
      final String type2 = "TYPE2";
      final CessionType ct1 = new CessionType();
      ct1.setType(type);
      final CessionType ct2 = new CessionType();
      ct2.setType(type);

      // L'objet 1 n'est pas égal à null
      assertFalse(ct1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ct1.equals(ct1));
      // 2 objets sont égaux entre eux
      assertTrue(ct1.equals(ct2));
      assertTrue(ct2.equals(ct1));

      // Vérification de la différenciation de 2 objets
      ct2.setType(type2);
      assertFalse(ct1.equals(ct2));
      assertFalse(ct2.equals(ct1));

      ct2.setType(null);
      assertFalse(ct1.equals(ct2));
      assertFalse(ct2.equals(ct1));

      ct1.setType(null);
      assertTrue(ct1.equals(ct2));
      ct2.setType(type);
      assertFalse(ct1.equals(ct2));

      final Categorie c = new Categorie();
      assertFalse(ct1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String type = "TYPE";
      final CessionType ct1 = new CessionType();
      ct1.setType(type);
      final CessionType ct2 = new CessionType();
      ct2.setType(type);
      final CessionType ct3 = new CessionType();
      ct3.setType(null);
      assertTrue(ct3.hashCode() > 0);

      final int hash = ct1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(ct1.hashCode() == ct2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == ct1.hashCode());
      assertTrue(hash == ct1.hashCode());
      assertTrue(hash == ct1.hashCode());
      assertTrue(hash == ct1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final CessionType ct1 = cessionTypeDao.findById(1);
      assertTrue(ct1.toString().equals("{" + ct1.getType() + "}"));

      final CessionType ct2 = new CessionType();
      assertTrue(ct2.toString().equals("{Empty CessionType}"));
   }

}
