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
package fr.aphp.tumorotek.dao.test.io.export;

import java.util.Iterator;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO ChampDao et le
 * bean du domaine Champ.
 * Classe de test créée le 25/11/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class ChampEntiteDaoTest extends AbstractDaoTest
{

   private EntiteDao entiteDao;
   private ChampEntiteDao champEntiteDao;
   private DataTypeDao dataTypeDao;
   private ImportTemplateDao importTemplateDao;

   public ChampEntiteDaoTest(){}

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setChampEntiteDao(final ChampEntiteDao ceDao){
      this.champEntiteDao = ceDao;
   }

   public void setDataTypeDao(final DataTypeDao dtDao){
      this.dataTypeDao = dtDao;
   }

   public void setImportTemplateDao(final ImportTemplateDao itDao){
      this.importTemplateDao = itDao;
   }

   public void testFindByEntiteAndNom(){
      final Entite e1 = entiteDao.findById(1);
      List<ChampEntite> liste = champEntiteDao.findByEntiteAndNom(e1, "Nom");
      assertTrue(liste.size() == 1);

      liste = champEntiteDao.findByEntiteAndNom(e1, "aaaaaaaaa");
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteAndNom(null, "aaaaaaaaa");
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteAndNom(e1, "");
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteAndNom(e1, null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByEntiteAndImport(){
      final Entite e1 = entiteDao.findById(1);
      List<ChampEntite> liste = champEntiteDao.findByEntiteAndImport(e1, false);
      assertTrue(liste.size() == 5);

      liste = champEntiteDao.findByEntiteAndImport(e1, true);
      assertTrue(liste.size() == 11);

      liste = champEntiteDao.findByEntiteAndImport(null, true);
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteAndImport(e1, null);
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteAndImport(null, null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByEntiteImportObligatoire(){
      final Entite e1 = entiteDao.findById(1);
      List<ChampEntite> liste = champEntiteDao.findByEntiteImportObligatoire(e1, true, false);
      assertTrue(liste.size() == 5);

      liste = champEntiteDao.findByEntiteImportObligatoire(e1, true, true);
      assertTrue(liste.size() == 6);

      liste = champEntiteDao.findByEntiteImportObligatoire(null, true, true);
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteImportObligatoire(e1, null, true);
      assertTrue(liste.size() == 0);

      liste = champEntiteDao.findByEntiteImportObligatoire(e1, true, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
   * d'un champ.
   * @throws Exception lance une exception en cas de problème lors du CRUD.
   */
   @Rollback(false)
   public void testCrudChamp() throws Exception{
      final boolean nullable = true;
      final int entiteId = 3;
      final int dataTypeId = 3;
      final boolean unique = true;
      final String valeurDefaut = "valeurDefaut";
      final String nom = "nom";

      final Entite entite = this.entiteDao.findById(entiteId);
      final DataType dataType = this.dataTypeDao.findById(dataTypeId);

      final ChampEntite c = new ChampEntite();
      c.setEntite(entite);
      c.setNullable(nullable);
      c.setDataType(dataType);
      c.setUnique(unique);
      c.setValeurDefaut(valeurDefaut);
      c.setNom(nom);
      c.setCanImport(false);

      // Test de l'insertion
      Integer idObject = new Integer(-1);
      this.champEntiteDao.createObject(c);
      final List<ChampEntite> champEntites = this.champEntiteDao.findAll();
      final Iterator<ChampEntite> itChampEntites = champEntites.iterator();
      boolean found = false;
      while(itChampEntites.hasNext()){
         final ChampEntite temp = itChampEntites.next();
         if(temp.equals(c)){
            found = true;
            idObject = temp.getId();
            break;
         }
      }
      assertTrue(found);

      // Test de la mise à jour
      final ChampEntite c2 = this.champEntiteDao.findById(idObject);
      assertNotNull(c2);
      assertNotNull(c2.getEntite());
      assertTrue(c2.getEntite().equals(entite));
      assertNotNull(c2.isNullable());
      assertTrue(c2.isNullable().equals(nullable));
      assertNotNull(c2.getDataType());
      assertTrue(c2.getDataType().equals(dataType));
      assertNotNull(c2.isUnique());
      assertTrue(c2.isUnique().equals(unique));
      if(c2.getValeurDefaut() != null){
         assertTrue(c2.getValeurDefaut().equals(valeurDefaut));
      }else{
         assertNull(valeurDefaut);
      }
      assertNotNull(c2.getNom());
      assertTrue(c2.getNom().equals(nom));

      final boolean updatedNullable = false;
      final int updatedEntiteId = 2;
      final int updatedDataTypeId = 2;
      final boolean updatedUnique = false;
      final String updatedValeurDefaut = "valeurDefaut2";
      final String updatedNom = "nom2";

      final Entite updatedEntite = this.entiteDao.findById(updatedEntiteId);
      final DataType updatedDataType = this.dataTypeDao.findById(updatedDataTypeId);

      c2.setEntite(updatedEntite);
      c2.setNullable(updatedNullable);
      c2.setDataType(updatedDataType);
      c2.setUnique(updatedUnique);
      c2.setValeurDefaut(updatedValeurDefaut);
      c2.setNom(updatedNom);

      this.champEntiteDao.updateObject(c2);
      assertNotNull(this.champEntiteDao.findById(idObject).getEntite());
      assertTrue(this.champEntiteDao.findById(idObject).getEntite().equals(updatedEntite));
      assertNotNull(this.champEntiteDao.findById(idObject).isNullable());
      assertTrue(this.champEntiteDao.findById(idObject).isNullable().equals(updatedNullable));
      assertNotNull(this.champEntiteDao.findById(idObject).getDataType());
      assertTrue(this.champEntiteDao.findById(idObject).getDataType().equals(updatedDataType));
      assertNotNull(this.champEntiteDao.findById(idObject).isUnique());
      assertTrue(this.champEntiteDao.findById(idObject).isUnique().equals(updatedUnique));
      if(this.champEntiteDao.findById(idObject).getValeurDefaut() != null){
         assertTrue(this.champEntiteDao.findById(idObject).getValeurDefaut().equals(updatedValeurDefaut));
      }else{
         assertNull(updatedValeurDefaut);
      }
      assertNotNull(this.champEntiteDao.findById(idObject).getNom());
      assertTrue(this.champEntiteDao.findById(idObject).getNom().equals(updatedNom));
      // Test de la délétion
      this.champEntiteDao.removeObject(idObject);
      assertNull(this.champEntiteDao.findById(idObject));
   }

   /**
    * test toString().
    */
   public void testToString(){
      final ChampEntite c1 = champEntiteDao.findById(1);
      assertTrue(c1.toString().equals("{" + c1.getNom() + "}"));

      final ChampEntite c2 = new ChampEntite();
      assertTrue(c2.toString().equals("{Empty ChampEntite}"));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      //On boucle sur les 4 possibilités
      for(int i = 0; i < Math.pow(2, 2); i++){
         final ChampEntite champ1 = new ChampEntite();
         final ChampEntite champ2 = new ChampEntite();
         Entite entite = null;
         if(i >= 2){
            entite = entiteDao.findById(2);
         }
         champ1.setEntite(entite);
         champ2.setEntite(entite);
         String nom = null;
         final int toTest = i % 2;
         if(toTest > 0){
            nom = "Nom";
         }
         champ1.setNom(nom);
         champ2.setNom(nom);
         //On compare les 2 champs
         assertTrue(champ1.equals(champ2));
      }
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      //On boucle sur les 4 possibilités
      for(int i = 0; i < Math.pow(2, 2); i++){
         final ChampEntite champ = new ChampEntite();
         int hash = 7;
         Entite entite = null;
         int hashEntite = 0;
         if(i >= 2){
            entite = entiteDao.findById(3);
            hashEntite = entite.hashCode();
         }
         final int toTest = i % 2;
         String nom = null;
         int hashNom = 0;
         if(toTest > 0){
            nom = "Test";
            hashNom = nom.hashCode();
         }
         hash = 31 * hash + hashEntite;
         hash = 31 * hash + hashNom;
         champ.setEntite(entite);
         champ.setNom(nom);
         //On vérifie que le hashCode est bon
         assertTrue(champ.hashCode() == hash);
         assertTrue(champ.hashCode() == hash);
         assertTrue(champ.hashCode() == hash);
      }
   }

   public void testFindByImportTemplate(){
      List<ChampEntite> chpE = champEntiteDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), entiteDao.findById(1));
      assertTrue(chpE.size() == 6);
      assertTrue(chpE.contains(champEntiteDao.findById(2)));
      assertTrue(chpE.contains(champEntiteDao.findById(3)));
      assertTrue(chpE.contains(champEntiteDao.findById(5)));
      assertTrue(chpE.contains(champEntiteDao.findById(6)));
      assertTrue(chpE.contains(champEntiteDao.findById(7)));
      assertTrue(chpE.contains(champEntiteDao.findById(10)));
      chpE = champEntiteDao.findByImportTemplateAndEntite(importTemplateDao.findById(3), entiteDao.findById(1));
      assertTrue(chpE.size() == 3);
      assertTrue(chpE.contains(champEntiteDao.findById(2)));
      assertTrue(chpE.contains(champEntiteDao.findById(3)));
      assertTrue(chpE.contains(champEntiteDao.findById(5)));
      chpE = champEntiteDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), entiteDao.findById(3));
      assertTrue(chpE.size() == 8);
      chpE = champEntiteDao.findByImportTemplateAndEntite(importTemplateDao.findById(2), entiteDao.findById(3));
      assertTrue(chpE.size() == 0);
      chpE = champEntiteDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), entiteDao.findById(5));
      assertTrue(chpE.size() == 0);
      chpE = champEntiteDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), null);
      assertTrue(chpE.size() == 0);
      chpE = champEntiteDao.findByImportTemplateAndEntite(null, entiteDao.findById(3));
      assertTrue(chpE.size() == 0);
   }

}
