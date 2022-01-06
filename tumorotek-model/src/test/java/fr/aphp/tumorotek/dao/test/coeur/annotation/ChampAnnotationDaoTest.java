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
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;

/**
 *
 * Classe de test pour le DAO ChampAnnotationDao et le
 * bean du domaine ChampAnnotation.
 * Classe de test créée le 28/01/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ChampAnnotationDaoTest extends AbstractDaoTest
{

   @Autowired
 ChampAnnotationDao champAnnotationDao;
   @Autowired
 TableAnnotationDao tableAnnotationDao;
   @Autowired
 DataTypeDao dataTypeDao;
   @Autowired
 EntiteDao entiteDao;
   @Autowired
 ImportTemplateDao importTemplateDao;

   public ChampAnnotationDaoTest(){}

   @Test
public void setChampAnnotationDao(final ChampAnnotationDao cDao){
      this.champAnnotationDao = cDao;
   }

   @Test
public void setDataTypeDao(final DataTypeDao dtDao){
      this.dataTypeDao = dtDao;
   }

   @Test
public void setTableAnnotationDao(final TableAnnotationDao tDao){
      this.tableAnnotationDao = tDao;
   }

   @Test
public void setEntiteDao(final EntiteDao e){
      this.entiteDao = e;
   }

   @Test
public void setImportTemplateDao(final ImportTemplateDao i){
      this.importTemplateDao = i;
   }

   /**
    * Test la méthode toString.
    * @throws ParseException 
    */
   @Test
public void testToString() throws ParseException{
      final ChampAnnotation c1 = champAnnotationDao.findById(1);
      assertTrue(c1.toString().equals("{ChampAnnotation: TABLE_PAT1.Alphanum1}"));

      final ChampAnnotation c2 = new ChampAnnotation();
      assertTrue(c2.toString().equals("{Empty ChampAnnotation}"));
      c2.setTableAnnotation(tableAnnotationDao.findById(1));
      assertTrue(c2.toString().equals("{Empty ChampAnnotation}"));
      c2.setTableAnnotation(null);
      c2.setNom("testChamp");
      assertTrue(c2.toString().equals("{Empty ChampAnnotation}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllChamps(){
      final List<ChampAnnotation> champs = IterableUtils.toList(champAnnotationDao.findAll());
      assertTrue(champs.size() == 48);
   }

   @Test
public void testFindByNom(){
      List<ChampAnnotation> champs = champAnnotationDao.findByNom("Bool%");
      assertTrue(champs.size() == 3);
      champs = champAnnotationDao.findByNom("Test%");
      assertTrue(champs.size() == 0);
      champs = champAnnotationDao.findByNom(null);
      assertTrue(champs.size() == 0);
   }

   @Test
public void testFindByTable(){
      TableAnnotation t = tableAnnotationDao.findById(3);
      List<ChampAnnotation> champs = champAnnotationDao.findByTable(t);
      assertTrue(champs.size() == 5);
      // verfie l'ordre
      assertTrue(champs.get(0).equals(champAnnotationDao.findById(4)));
      assertTrue(champs.get(1).equals(champAnnotationDao.findById(5)));
      assertTrue(champs.get(2).equals(champAnnotationDao.findById(7)));
      assertTrue(champs.get(3).equals(champAnnotationDao.findById(9)));
      assertTrue(champs.get(4).equals(champAnnotationDao.findById(11)));
      t = tableAnnotationDao.findById(1);
      champs = champAnnotationDao.findByTable(t);
      assertTrue(champs.size() == 2);
      assertTrue(champs.get(1).equals(champAnnotationDao.findById(13)));
      t = tableAnnotationDao.findById(6);
      champs = champAnnotationDao.findByTable(t);
      assertTrue(champs.size() == 2);
      champs = champAnnotationDao.findByTable(null);
      assertTrue(champs.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      final ChampAnnotation c1 = champAnnotationDao.findById(1);
      List<ChampAnnotation> champs = champAnnotationDao.findByExcludedId(1);
      assertTrue(champs.size() == 47);
      assertFalse(champs.contains(c1));
      champs = champAnnotationDao.findByExcludedId(56);
      assertTrue(champs.size() == 48);
      champs = champAnnotationDao.findByExcludedId(null);
      assertTrue(champs.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un ChampAnnotation.
    */
   @Rollback(false)
   @Test
public void testCrudChampAnnotation(){

      final ChampAnnotation champ = new ChampAnnotation();
      champ.setNom("testAnno");
      champ.setDataType(dataTypeDao.findByType("fichier").get(0));
      champ.setTableAnnotation(tableAnnotationDao.findById(6));
      champ.setCombine(true);
      champ.setOrdre(3);
      // items pour thesaurus sont testés dans ItemDaoTest
      // ajouter valeurs par défaut testées dans AnnotationDefautDaoTest

      // Test de l'insertion
      champAnnotationDao.save(champ);
      assertNotNull(champ.getId());

      final Integer maxId = champ.getId();

      // Test de la mise à jour
      final ChampAnnotation champ2 = champAnnotationDao.findById(maxId);
      assertNotNull(champ2);
      assertTrue(champ2.getNom().equals("testAnno"));
      assertTrue(champ2.getDataType().equals(dataTypeDao.findByType("fichier").get(0)));
      assertTrue(champ2.getTableAnnotation().equals(tableAnnotationDao.findById(6)));
      assertTrue(champ2.getCombine());
      assertTrue(champ2.getOrdre() == 3);

      // update
      champ2.setNom("testAnno2");
      champ2.setOrdre(4);
      champ2.setCombine(false);

      champAnnotationDao.save(champ2);

      assertTrue(champAnnotationDao.findById(maxId).getNom().equals("testAnno2"));
      assertTrue(champAnnotationDao.findById(maxId).getOrdre() == 4);
      assertFalse(champAnnotationDao.findById(maxId).getCombine());

      // Test de la délétion
      champAnnotationDao.deleteById(maxId);
      assertNull(champAnnotationDao.findById(maxId));
      testReadAllChamps();
   }

   /**
    * Test des méthodes surchargées "equals" et hashcode.
    */
   @Test
public void testEqualsAndHashCode(){
      final ChampAnnotation c1 = new ChampAnnotation();
      final ChampAnnotation c2 = new ChampAnnotation();
      assertFalse(c1.equals(null));
      assertNotNull(c2);
      assertTrue(c1.equals(c1));
      assertTrue(c1.equals(c2));
      assertTrue(c1.hashCode() == c2.hashCode());

      final String s1 = "nom1";
      final String s2 = "nom2";
      final String s3 = new String("nom2");

      c1.setNom(s1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setNom(s2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setNom(s2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setNom(s3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      final TableAnnotation t1 = tableAnnotationDao.findById(1);
      final TableAnnotation t2 = tableAnnotationDao.findById(2);
      final TableAnnotation t3 = new TableAnnotation();
      t3.setNom(t2.getNom());
      t3.setEntite(t2.getEntite());
      t3.setPlateforme(t2.getPlateforme());
      assertFalse(t1.equals(t2));
      assertFalse(t1.hashCode() == t2.hashCode());
      assertTrue(t2.equals(t3));
      c1.setTableAnnotation(t1);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c2.setTableAnnotation(t2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      assertTrue(c1.hashCode() != c2.hashCode());
      c1.setTableAnnotation(t3);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());
      c1.setTableAnnotation(t2);
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));
      assertTrue(c1.hashCode() == c2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(c1.equals(c));
   }

   @Test
public void testClone(){
      final ChampAnnotation c = champAnnotationDao.findById(1);
      final ChampAnnotation c2 = c.clone();
      assertTrue(c.equals(c2));
      if(c.getId() != null){
         assertTrue(c.getId().equals(c2.getId()));
      }else{
         assertNull(c2.getId());
      }
      if(c.getNom() != null){
         assertTrue(c.getNom().equals(c2.getNom()));
      }else{
         assertNull(c2.getNom());
      }
      if(c.getDataType() != null){
         assertTrue(c.getDataType().equals(c2.getDataType()));
      }else{
         assertNull(c2.getDataType());
      }
      if(c.getOrdre() != null){
         assertTrue(c.getOrdre().equals(c2.getOrdre()));
      }else{
         assertNull(c2.getOrdre());

      }
      if(c.getCombine() != null){
         assertTrue(c.getCombine().equals(c2.getCombine()));
      }else{
         assertNull(c2.getCombine());

      }
      if(c.getTableAnnotation() != null){
         assertTrue(c.getTableAnnotation().equals(c2.getTableAnnotation()));
      }else{
         assertNull(c2.getTableAnnotation());
      }
      if(c.getAnnotationDefauts() != null){
         assertTrue(c.getAnnotationDefauts().equals(c2.getAnnotationDefauts()));
      }else{
         assertNull(c2.getAnnotationDefauts());
      }
      if(c.getAnnotationValeurs() != null){
         assertTrue(c.getAnnotationValeurs().equals(c2.getAnnotationValeurs()));
      }else{
         assertNull(c2.getAnnotationValeurs());
      }
      if(c.getItems() != null){
         assertTrue(c.getItems().equals(c2.getItems()));
      }else{
         assertNull(c2.getItems());
      }
      assertTrue(c.getEdit().equals(c2.getEdit()));
   }

   @Test
public void testFindByEditByCatalogue(){
      TableAnnotation t = tableAnnotationDao.findById(1);
      List<ChampAnnotation> chps = champAnnotationDao.findByEditByCatalogue(t);
      assertTrue(chps.size() == 2);

      t = tableAnnotationDao.findById(3);
      chps = champAnnotationDao.findByEditByCatalogue(t);
      assertTrue(chps.size() == 2);
      assertTrue(chps.get(0).getNom().equals("Texte1"));
      assertTrue(chps.get(1).getNom().equals("Thes1"));
   }

   @Test
public void testFindChampsFichiersByTable(){
      final TableAnnotation t = tableAnnotationDao.findById(7);
      DataType dt = dataTypeDao.findById(7);
      List<ChampAnnotation> chps = champAnnotationDao.findByTableAndType(t, dt);
      assertTrue(chps.size() == 3);
      chps = champAnnotationDao.findByTableAndType(null, dt);
      assertTrue(chps.size() == 0);
      chps = champAnnotationDao.findByTableAndType(t, null);
      assertTrue(chps.size() == 0);
      dt = dataTypeDao.findById(3);
      chps = champAnnotationDao.findByTableAndType(t, dt);
      assertTrue(chps.size() == 0);
   }

   @Test
public void testFindCriteresByChampAnnotation(){
      final ChampAnnotation c1 = champAnnotationDao.findById(1);
      List<Critere> res = champAnnotationDao.findCriteresByChampAnnotation(c1);
      assertTrue(res.isEmpty());
      res = champAnnotationDao.findCriteresByChampAnnotation(null);
      assertTrue(res.isEmpty());
   }

   @Test
public void testFindResultatsByChampAnnotation(){
      final ChampAnnotation c1 = champAnnotationDao.findById(1);
      List<Resultat> res = champAnnotationDao.findResultatsByChampAnnotation(c1);
      assertTrue(res.isEmpty());
      res = champAnnotationDao.findResultatsByChampAnnotation(null);
      assertTrue(res.isEmpty());
   }

   @Test
public void testFindImportColonnesByChampAnnotation(){
      ChampAnnotation c = champAnnotationDao.findById(1);
      List<ImportColonne> res = champAnnotationDao.findImportColonnesByChampAnnotation(c);
      assertTrue(res.isEmpty());
      c = champAnnotationDao.findById(2);
      res = champAnnotationDao.findImportColonnesByChampAnnotation(c);
      assertTrue(res.size() == 1);
      res = champAnnotationDao.findImportColonnesByChampAnnotation(null);
      assertTrue(res.isEmpty());
   }

   @Test
public void testFindByChpLEtiquetteChampAnnotation(){
      final ChampAnnotation c1 = champAnnotationDao.findById(1);
      List<ChampLigneEtiquette> res = champAnnotationDao.findChpLEtiquetteByChampAnnotation(c1);
      assertTrue(res.isEmpty());
      res = champAnnotationDao.findChpLEtiquetteByChampAnnotation(null);
      assertTrue(res.isEmpty());
   }

   @Test
public void testfindByImportTemplateAndEntite(){
      List<ChampAnnotation> chpA =
         champAnnotationDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), entiteDao.findById(2));
      assertTrue(chpA.size() == 5);
      assertTrue(chpA.contains(champAnnotationDao.findById(2)));
      assertTrue(chpA.contains(champAnnotationDao.findById(3)));
      assertTrue(chpA.contains(champAnnotationDao.findById(15)));
      assertTrue(chpA.contains(champAnnotationDao.findById(27)));
      assertTrue(chpA.contains(champAnnotationDao.findById(39)));
      chpA = champAnnotationDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), entiteDao.findById(1));
      assertTrue(chpA.size() == 0);
      chpA = champAnnotationDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), entiteDao.findById(3));
      assertTrue(chpA.size() == 1);
      assertTrue(chpA.contains(champAnnotationDao.findById(12)));
      chpA = champAnnotationDao.findByImportTemplateAndEntite(importTemplateDao.findById(2), entiteDao.findById(2));
      assertTrue(chpA.size() == 0);
      chpA = champAnnotationDao.findByImportTemplateAndEntite(importTemplateDao.findById(1), null);
      assertTrue(chpA.size() == 0);
      chpA = champAnnotationDao.findByImportTemplateAndEntite(null, entiteDao.findById(3));
      assertTrue(chpA.size() == 0);
   }
}
