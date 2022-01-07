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
package fr.aphp.tumorotek.dao.test.coeur.annotation;

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO ItemDao et le
 * bean du domaine Item.
 * Classe de test créée le 01/02/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ItemDaoTest extends AbstractDaoTest
{

   /** Beans Dao. */
   @Autowired
 ItemDao itemDao;
   @Autowired
 ChampAnnotationDao champAnnotationDao;
   @Autowired
 DataTypeDao dataTypeDao;
   @Autowired
 TableAnnotationDao tableAnnotationDao;
   @Autowired
 PlateformeDao plateformeDao;

   /**
    * Constructeur.
    */
   public ItemDaoTest(){}

   @Test
public void setItemDao(final ItemDao iDao){
      this.itemDao = iDao;
   }

   @Test
public void setChampAnnotationDao(final ChampAnnotationDao cDao){
      this.champAnnotationDao = cDao;
   }

   @Test
public void setTableAnnotationDao(final TableAnnotationDao taDao){
      this.tableAnnotationDao = taDao;
   }

   @Test
public void setDataTypeDao(final DataTypeDao dtDao){
      this.dataTypeDao = dtDao;
   }

   @Test
public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test la méthode toString.
    * @throws ParseException 
    */
   @Test
public void testToString() throws ParseException{
      final Item i1 = itemDao.findById(1);
      assertTrue(i1.toString().equals("{Thesaurus Item: Thes1.item1-1}"));

      final Item i2 = new Item();
      assertTrue(i2.toString().equals("{Empty Item}"));
      i2.setChampAnnotation(champAnnotationDao.findById(11));
      assertTrue(i2.toString().equals("{Empty Item}"));
      i2.setChampAnnotation(null);
      i2.setLabel("testItem");
      assertTrue(i2.toString().equals("{Empty Item}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllItems(){
      final List<Item> items = IterableUtils.toList(itemDao.findAll());
      assertTrue(items.size() == 87);
   }

   @Test
public void testFindByChamp(){
      List<Item> items = itemDao.findByChamp(champAnnotationDao.findById(12));
      assertTrue(items.size() == 3);
      items = itemDao.findByChamp(champAnnotationDao.findById(1));
      assertTrue(items.size() == 0);
      items = itemDao.findByChamp(null);
      assertTrue(items.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      final Item i1 = itemDao.findById(1);
      List<Item> items = itemDao.findByExcludedId(1);
      assertTrue(items.size() == 86);
      assertFalse(items.contains(i1));
      items = itemDao.findByExcludedId(99);
      assertTrue(items.size() == 87);
      items = itemDao.findByExcludedId(null);
      assertTrue(items.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un Item.
    */
   @Rollback(false)
   @Test
public void testCrudItem(){

      // creation d'un thes avec cascade sur items
      final ChampAnnotation thes = new ChampAnnotation();
      thes.setNom("testThes");
      thes.setDataType(dataTypeDao.findByType("thesaurus").get(0));
      thes.setTableAnnotation(tableAnnotationDao.findById(1));
      thes.setCombine(false);
      thes.setOrdre(1);

      // Test de l'insertion a partir du champ
      champAnnotationDao.save(thes);
      assertNotNull(thes.getId());

      final Integer maxId = thes.getId();

      // Test de l'insertion
      final ChampAnnotation thes2 = champAnnotationDao.findById(maxId);
      assertNotNull(thes2);

      // creation item supplementaire
      final Item i3 = new Item();
      i3.setLabel("new");
      i3.setValeur("newVal");
      i3.setChampAnnotation(thes2);
      itemDao.save(i3);
      assertTrue(i3.getItemId() == 88);
      assertTrue(itemDao.findByChamp(thes2).size() == 1);
      assertTrue(itemDao.findById(88).getLabel().equals("new"));
      assertTrue(itemDao.findById(88).getValeur().equals("newVal"));

      // update
      i3.setLabel("staple");
      i3.setValeur(null);

      itemDao.save(i3);
      assertTrue(itemDao.findByChamp(thes2).size() == 1);
      assertTrue(itemDao.findById(88).getLabel().equals("staple"));
      assertFalse(itemDao.findById(88).getValeur().isPresent());

      // Test de la délétion
      itemDao.deleteById(new Integer(88));
      assertFalse(itemDao.findById(new Integer(88)).isPresent());
      // cascade
      champAnnotationDao.deleteById(maxId);
      testReadAllItems();
      assertTrue(IterableUtils.toList(champAnnotationDao.findAll()).size() == 48);
   }

   /**
    * Test des méthodes surchargées "equals" et hashcode.
    */
   @Test
public void testEqualsAndHashCode(){
      final Item i1 = new Item();
      final Item i2 = new Item();
      assertFalse(i1.equals(null));
      assertNotNull(i2);
      assertTrue(i1.equals(i1));
      assertTrue(i1.equals(i2));
      assertTrue(i1.hashCode() == i2.hashCode());

      final String s1 = "label1";
      final String s2 = "label2";
      final String s3 = new String("label2");

      i1.setLabel(s1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      assertTrue(i1.hashCode() != i2.hashCode());
      i2.setLabel(s2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      assertTrue(i1.hashCode() != i2.hashCode());
      i1.setLabel(s2);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));
      assertTrue(i1.hashCode() == i2.hashCode());
      i1.setLabel(s3);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));
      assertTrue(i1.hashCode() == i2.hashCode());

      final ChampAnnotation t1 = champAnnotationDao.findById(1);
      final ChampAnnotation t2 = champAnnotationDao.findById(2);
      final ChampAnnotation t3 = new ChampAnnotation();
      t3.setNom(t2.getNom());
      t3.setTableAnnotation(t2.getTableAnnotation());
      assertFalse(t1.equals(t2));
      assertFalse(t1.hashCode() == t2.hashCode());
      assertTrue(t2.equals(t3));
      i1.setChampAnnotation(t1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      assertTrue(i1.hashCode() != i2.hashCode());
      i2.setChampAnnotation(t2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      assertTrue(i1.hashCode() != i2.hashCode());
      i1.setChampAnnotation(t3);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));
      assertTrue(i1.hashCode() == i2.hashCode());
      i1.setChampAnnotation(t2);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));
      assertTrue(i1.hashCode() == i2.hashCode());

      final Plateforme pf1 = new Plateforme();
      pf1.setNom("PF1");
      final Plateforme pf2 = new Plateforme();
      pf2.setNom("PF2");
      final Plateforme pf3 = new Plateforme();
      pf3.setNom("PF2");
      assertFalse(pf1.equals(pf2));
      assertFalse(pf1.hashCode() == pf2.hashCode());
      assertTrue(pf2.equals(pf3));
      i1.setPlateforme(pf1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      assertTrue(i1.hashCode() != i2.hashCode());
      i2.setPlateforme(pf2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      assertTrue(i1.hashCode() != i2.hashCode());
      i1.setPlateforme(pf3);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));
      assertTrue(i1.hashCode() == i2.hashCode());
      i1.setPlateforme(pf2);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));
      assertTrue(i1.hashCode() == i2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(i1.equals(c));
   }

   @Test
public void testClone(){
      final Item i = itemDao.findById(1);
      final Item i2 = i.clone();
      assertTrue(i.equals(i2));
      if(i.getItemId() != null){
         assertTrue(i.getItemId().equals(i2.getItemId()));
      }else{
         assertNull(i2.getItemId());
      }
      if(i.getLabel() != null){
         assertTrue(i.getLabel().equals(i2.getLabel()));
      }else{
         assertNull(i2.getLabel());
      }
      if(i.getValeur() != null){
         assertTrue(i.getValeur().equals(i2.getValeur()));
      }else{
         assertNull(i2.getValeur());
      }
      if(i.getChampAnnotation() != null){
         assertTrue(i.getChampAnnotation().equals(i2.getChampAnnotation()));
      }else{
         assertNull(i.getChampAnnotation());
      }
      if(i.getAnnotationValeurs() != null){
         assertTrue(i.getAnnotationValeurs().equals(i2.getAnnotationValeurs()));
      }else{
         assertNull(i2.getAnnotationValeurs());
      }
      if(i.getAnnotationDefauts() != null){
         assertTrue(i.getAnnotationDefauts().equals(i2.getAnnotationDefauts()));
      }else{
         assertNull(i2.getAnnotationDefauts());
      }
   }

   @Test
public void testFindByChampAndPlateforme(){
      Plateforme pf = plateformeDao.findById(1);
      ChampAnnotation c = champAnnotationDao.findById(38);
      assertTrue(itemDao.findByChampAndPlateforme(c, pf).isEmpty());

      c = champAnnotationDao.findById(11);
      assertTrue(itemDao.findByChampAndPlateforme(c, pf).size() == 2);

      pf = plateformeDao.findById(2);
      assertTrue(itemDao.findByChampAndPlateforme(c, pf).size() == 1);
   }
}
