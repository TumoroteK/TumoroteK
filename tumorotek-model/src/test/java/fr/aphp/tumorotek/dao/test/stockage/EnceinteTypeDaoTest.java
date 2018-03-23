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
package fr.aphp.tumorotek.dao.test.stockage;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

public class EnceinteTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private EnceinteTypeDao enceinteTypeDao;
   private PlateformeDao plateformeDao;

   private final String updatedType = "Mis a jour";

   /** Constructeur. */
   public EnceinteTypeDaoTest(){

   }

   public void setEnceinteTypeDao(final EnceinteTypeDao eDao){
      this.enceinteTypeDao = eDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAlls(){
      final List<EnceinteType> liste = enceinteTypeDao.findAll();
      assertTrue(liste.size() == 9);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = enceinteTypeDao.findByOrder(pf);
      assertTrue(list.size() == 8);
      assertTrue(list.get(0).getNom().equals("BOITE"));
      pf = plateformeDao.findById(2);
      list = enceinteTypeDao.findByOrder(pf);
      assertTrue(list.size() == 1);
      list = enceinteTypeDao.findByOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByOrderExceptBoite().
    */
   public void testFindByOrderExceptBoite(){
      final Plateforme pf1 = plateformeDao.findById(1);
      List<EnceinteType> list = enceinteTypeDao.findByOrderExceptBoite(pf1);
      assertTrue(list.size() == 7);
      assertTrue(list.get(0).getType().equals("CANISTER"));
      assertTrue(list.get(0).getPrefixe().equals("CAN"));
      list = enceinteTypeDao.findByOrderExceptBoite(plateformeDao.findById(2));
      assertTrue(list.size() == 1);
      list = enceinteTypeDao.findByOrderExceptBoite(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<EnceinteType> liste = enceinteTypeDao.findByExcludedId(1);
      assertTrue(liste.size() == 8);
      final EnceinteType type = liste.get(0);
      assertNotNull(type);
      assertTrue(type.getEnceinteTypeId() == 2);

      liste = enceinteTypeDao.findByExcludedId(15);
      assertTrue(liste.size() == 9);
   }

   /**
    * Test l'appel de la méthode findByTypeAndPf().
    */
   public void testFindByTypeAndPf(){
      List<EnceinteType> liste = enceinteTypeDao.findByTypeAndPf("CANISTER", plateformeDao.findById(1));
      assertTrue(liste.size() == 1);

      liste = enceinteTypeDao.findByTypeAndPf("CANISTER", plateformeDao.findById(2));
      assertTrue(liste.size() == 0);

      liste = enceinteTypeDao.findByTypeAndPf("CAN", plateformeDao.findById(1));
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final EnceinteType et = new EnceinteType();
      et.setPlateforme(plateformeDao.findById(1));
      et.setType("Type");
      et.setPrefixe("Ty");
      // Test de l'insertion
      enceinteTypeDao.createObject(et);
      assertEquals(new Integer(10), et.getEnceinteTypeId());

      // Test de la mise à jour
      final EnceinteType et2 = enceinteTypeDao.findById(new Integer(10));
      assertNotNull(et2);
      assertTrue(et2.getType().equals("Type"));
      et2.setType(updatedType);
      enceinteTypeDao.updateObject(et2);
      assertTrue(enceinteTypeDao.findById(new Integer(10)).getType().equals(updatedType));

      // Test de la délétion
      enceinteTypeDao.removeObject(new Integer(10));
      assertNull(enceinteTypeDao.findById(new Integer(10)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String type = "TYPE";
      final String type2 = "TYPE2";
      final EnceinteType et1 = new EnceinteType();
      et1.setType(type);
      final EnceinteType et2 = new EnceinteType();
      et2.setType(type);

      // L'objet 1 n'est pas égal à null
      assertFalse(et1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(et1.equals(et1));
      // 2 objets sont égaux entre eux
      assertTrue(et1.equals(et2));
      assertTrue(et2.equals(et1));

      // Vérification de la différenciation de 2 objets
      et2.setType(type2);
      assertFalse(et1.equals(et2));
      assertFalse(et2.equals(et1));

      et2.setType(null);
      assertFalse(et1.equals(et2));
      assertFalse(et2.equals(et1));

      et1.setType(null);
      assertTrue(et1.equals(et2));
      et2.setType(type2);
      assertFalse(et1.equals(et2));

      //plateforme
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      et1.setType(et2.getType());
      et1.setPlateforme(pf1);
      et2.setPlateforme(pf1);
      assertTrue(et1.equals(et2));
      et2.setPlateforme(pf2);
      assertFalse(et1.equals(et2));

      final Categorie c = new Categorie();
      assertFalse(et1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String type = "TYPE";
      final EnceinteType et1 = new EnceinteType();
      et1.setType(type);
      final EnceinteType et2 = new EnceinteType();
      et2.setType(type);
      final EnceinteType ct3 = new EnceinteType();
      ct3.setType(type);
      assertTrue(ct3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      et1.setPlateforme(pf1);
      et2.setPlateforme(pf1);
      ct3.setPlateforme(pf2);

      final int hash = et1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(et1.hashCode() == et2.hashCode());
      assertFalse(et1.hashCode() == ct3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == et1.hashCode());
      assertTrue(hash == et1.hashCode());
      assertTrue(hash == et1.hashCode());
      assertTrue(hash == et1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final EnceinteType et1 = enceinteTypeDao.findById(1);
      assertTrue(et1.toString().equals("{" + et1.getType() + "}"));

      final EnceinteType et2 = new EnceinteType();
      assertTrue(et2.toString().equals("{Empty EnceinteType}"));
   }

}
