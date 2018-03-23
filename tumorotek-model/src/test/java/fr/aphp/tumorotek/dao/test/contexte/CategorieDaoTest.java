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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.CategorieDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO CategorieDao et le bean du domaine Categorie.
 * Classe de test créée le 08/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class CategorieDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CategorieDao categorieDao;

   /** Valeur du nom pour la maj. */
   private final String updatedNom = "Cat mis a jour";

   /**
    * Constructeur.
    */
   public CategorieDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param cDao est le bean Dao.
    */
   public void setCategorieDaoJpa(final CategorieDao cDao){
      this.categorieDao = cDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllCategories(){
      final List<Categorie> categories = categorieDao.findAll();
      assertTrue(categories.size() == 2);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<Categorie> list = categorieDao.findByOrder();
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("CAT1"));
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){
      List<Categorie> categories = categorieDao.findByNom("CAT1");
      assertTrue(categories.size() == 1);
      categories = categorieDao.findByNom("CAT5");
      assertTrue(categories.size() == 0);
      categories = categorieDao.findByNom("CA%");
      assertTrue(categories.size() == 2);
   }

   /**
    * Test l'appel de la méthode findByEtablissementId().
    */
   public void testFindByEtablissementId(){
      List<Categorie> categories = categorieDao.findByEtablissementId(1);
      assertTrue(categories.size() == 1);
      categories = categorieDao.findByEtablissementId(4);
      assertTrue(categories.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<Categorie> liste = categorieDao.findByExcludedId(1);
      assertTrue(liste.size() == 1);
      final Categorie cat = liste.get(0);
      assertNotNull(cat);
      assertTrue(cat.getCategorieId() == 2);

      liste = categorieDao.findByExcludedId(15);
      assertTrue(liste.size() == 2);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une catégorie.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudCategorie() throws Exception{
      final Categorie c = new Categorie();

      c.setNom("CAT3");
      // Test de l'insertion
      categorieDao.createObject(c);
      assertEquals(new Integer(3), c.getCategorieId());

      // Test de la mise à jour
      final Categorie c2 = categorieDao.findById(new Integer(3));
      assertNotNull(c2);
      assertTrue(c2.getNom().equals("CAT3"));
      c2.setNom(updatedNom);
      categorieDao.updateObject(c2);
      assertTrue(categorieDao.findById(new Integer(3)).getNom().equals(updatedNom));

      // Test de la délétion
      categorieDao.removeObject(new Integer(3));
      assertNull(categorieDao.findById(new Integer(3)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String nom = "Categorie";
      final String nom2 = "Categorie2";
      final Categorie c1 = new Categorie();
      c1.setNom(nom);
      final Categorie c2 = new Categorie();
      c2.setNom(nom);

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));
      // 2 objets sont égaux entre eux
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      // Vérification de la différenciation de 2 objets
      c2.setNom(nom2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      c2.setNom(null);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      c1.setNom(null);
      assertTrue(c1.equals(c2));
      c2.setNom(nom);
      assertFalse(c1.equals(c2));

      final Banque b = new Banque();
      assertFalse(c1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String nom = "Categorie";
      final Categorie c1 = new Categorie();
      c1.setCategorieId(1);
      c1.setNom(nom);
      final Categorie c2 = new Categorie();
      c2.setCategorieId(1);
      c2.setNom(nom);
      final Categorie c3 = new Categorie();
      c3.setCategorieId(1);
      c3.setNom(null);
      assertTrue(c3.hashCode() > 0);

      final int hash = c1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(c1.hashCode() == c2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());

   }

}
