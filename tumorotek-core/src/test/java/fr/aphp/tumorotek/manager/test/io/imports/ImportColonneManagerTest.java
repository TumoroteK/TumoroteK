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
package fr.aphp.tumorotek.manager.test.io.imports;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le manager ImportColonneManager.
 * Classe créée le 25/01/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0.13.2
 *
 */
public class ImportColonneManagerTest extends AbstractManagerTest
{

   /** Bean Manager. */
   private ImportColonneManager importColonneManager;
   private ImportTemplateDao importTemplateDao;
   private EntiteDao entiteDao;
   private DataTypeDao dataTypeDao;
   private ChampManager champManager;
   private ChampEntiteDao champEntiteDao;

   public ImportColonneManagerTest(){

   }

   public void setImportColonneManager(final ImportColonneManager iManager){
      this.importColonneManager = iManager;
   }

   public void setImportTemplateDao(final ImportTemplateDao iDao){
      this.importTemplateDao = iDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setDataTypeDao(final DataTypeDao dDao){
      this.dataTypeDao = dDao;
   }

   public void setChampManager(final ChampManager cManager){
      this.champManager = cManager;
   }

   public void setChampEntiteDao(final ChampEntiteDao cDao){
      this.champEntiteDao = cDao;
   }

   /**
    * Test la méthode findById.
    */
   public void testFindById(){
      final ImportColonne temp = importColonneManager.findByIdManager(1);
      assertNotNull(temp);

      final ImportColonne tempNull = importColonneManager.findByIdManager(1000);
      assertNull(tempNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   public void testFindAll(){
      final List<ImportColonne> list = importColonneManager.findAllObjectsManager();
      assertTrue(list.size() == 103);
   }

   /**
    * Test la méthode findByImportTemplateManager.
    */
   public void testFindByImportTemplateManager(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);

      List<ImportColonne> list = importColonneManager.findByImportTemplateManager(it1);
      assertTrue(list.size() == 31);

      list = importColonneManager.findByImportTemplateManager(it2);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByImportTemplateManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTemplateAndDataTypeManager.
    */
   public void testFindByTemplateAndDataTypeManager(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);
      final DataType d1 = dataTypeDao.findById(1);
      final DataType d7 = dataTypeDao.findById(7);

      List<ImportColonne> list = importColonneManager.findByTemplateAndDataTypeManager(it1, d1);
      assertTrue(list.size() == 12);
      list = importColonneManager.findByTemplateAndDataTypeManager(it1, d7);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndDataTypeManager(it2, d1);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndDataTypeManager(null, d1);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndDataTypeManager(it2, null);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndDataTypeManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTemplateAndEntiteManager.
    */
   public void testFindByTemplateAndEntiteManager(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);
      final Entite e1 = entiteDao.findById(1);
      final Entite e10 = entiteDao.findById(10);

      List<ImportColonne> list = importColonneManager.findByTemplateAndEntiteManager(it1, e1);
      assertTrue(list.size() == 6);
      list = importColonneManager.findByTemplateAndEntiteManager(it1, e10);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndEntiteManager(it2, e1);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndEntiteManager(null, e1);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndEntiteManager(it1, null);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndEntiteManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTemplateAndAnnotationEntiteManager.
    */
   public void testFindByTemplateAndAnnotationEntiteManager(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);
      final Entite e2 = entiteDao.findById(2);
      final Entite e10 = entiteDao.findById(10);

      List<ImportColonne> list = importColonneManager.findByTemplateAndAnnotationEntiteManager(it1, e2);
      assertTrue(list.size() == 5);
      list = importColonneManager.findByTemplateAndAnnotationEntiteManager(it1, e10);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndAnnotationEntiteManager(it2, e2);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndAnnotationEntiteManager(null, e2);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndAnnotationEntiteManager(it1, null);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndAnnotationEntiteManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTemplateAndThesaurusManager.
    */
   public void testFindByTemplateAndThesaurusManager(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);

      List<ImportColonne> list = importColonneManager.findByTemplateAndThesaurusManager(it1);
      assertTrue(list.size() == 8);

      list = importColonneManager.findByTemplateAndThesaurusManager(it2);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndThesaurusManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByTemplateAndAnnotationThesaurusManager.
    */
   public void testFindByTemplateAndAnnotationThesaurusManager(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);

      List<ImportColonne> list = importColonneManager.findByTemplateAndAnnotationThesaurusManager(it1);
      assertTrue(list.size() == 2);

      list = importColonneManager.findByTemplateAndAnnotationThesaurusManager(it2);
      assertTrue(list.size() == 0);

      list = importColonneManager.findByTemplateAndAnnotationThesaurusManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   public void testFindDoublon(){
      final ImportTemplate it1 = importTemplateDao.findById(1);
      final ImportTemplate it2 = importTemplateDao.findById(2);

      final ImportColonne ic1 = new ImportColonne();
      ic1.setNom("COLONNE");
      ic1.setImportTemplate(it2);
      assertFalse(importColonneManager.findDoublonManager(ic1));
      ic1.setImportTemplate(it1);
      assertFalse(importColonneManager.findDoublonManager(ic1));

      ic1.setNom("Nip patient");
      assertTrue(importColonneManager.findDoublonManager(ic1));
      ic1.setImportTemplate(it2);
      assertFalse(importColonneManager.findDoublonManager(ic1));

      final ImportColonne ic2 = importColonneManager.findByIdManager(1);
      assertFalse(importColonneManager.findDoublonManager(ic2));

      ic2.setNom("Nom patient");
      assertTrue(importColonneManager.findDoublonManager(ic2));

      assertFalse(importColonneManager.findDoublonManager(null));
   }

   /**
    * Test de la méthode findDoublonInListManager().
    */
   public void testFindDoublonInListManager(){
      final ImportColonne ic1 = importColonneManager.findByIdManager(1);
      final ImportColonne ic2 = importColonneManager.findByIdManager(2);
      final ImportColonne ic3 = importColonneManager.findByIdManager(3);
      final ImportColonne ic4 = importColonneManager.findByIdManager(4);
      final ImportColonne ic5 = importColonneManager.findByIdManager(5);
      List<ImportColonne> colonnes = new ArrayList<>();
      colonnes.add(ic1);
      colonnes.add(ic2);
      colonnes.add(ic3);
      colonnes.add(ic4);
      colonnes.add(ic5);
      assertFalse(importColonneManager.findDoublonInListManager(colonnes));

      colonnes.add(ic5);
      assertTrue(importColonneManager.findDoublonInListManager(colonnes));

      colonnes = new ArrayList<>();
      colonnes.add(ic1);
      colonnes.add(ic2);
      colonnes.add(ic3);
      colonnes.add(ic1);
      assertTrue(importColonneManager.findDoublonInListManager(colonnes));

      colonnes = new ArrayList<>();
      colonnes.add(ic1);
      colonnes.add(ic2);
      colonnes.add(ic3);
      final ImportColonne icTmp = new ImportColonne();
      icTmp.setImportTemplate(ic1.getImportTemplate());
      icTmp.setNom(ic1.getNom());
      colonnes.add(icTmp);
      assertTrue(importColonneManager.findDoublonInListManager(colonnes));

      colonnes = new ArrayList<>();
      colonnes.add(ic1);
      colonnes.add(icTmp);
      colonnes.add(ic2);
      colonnes.add(ic3);
      assertTrue(importColonneManager.findDoublonInListManager(colonnes));

      colonnes = new ArrayList<>();
      assertFalse(importColonneManager.findDoublonInListManager(colonnes));

      assertFalse(importColonneManager.findDoublonInListManager(null));
   }

   /**
    * Test de la méthode validateObjectManager().
    */
   public void testValidateObjectManager(){
      final ImportTemplate it = importTemplateDao.findById(1);
      final Champ c1 = champManager.findByIdManager(1);
      final ImportColonne ic = new ImportColonne();
      ic.setNom("TEST");
      ic.setOrdre(1);
      ic.setImportTemplate(it);

      Boolean catched = false;
      // on test avec l'importtemplate null
      try{
         importColonneManager.validateObjectManager(ic, null, c1, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // on test avec le champ null
      try{
         importColonneManager.validateObjectManager(ic, it, null, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;

      // Test de la validation lors de la création
      // On teste une insertion avec un attribut nom non valide
      final String[] emptyValues = new String[] {null, "", "  ", "¢¢kjs", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            ic.setNom(emptyValues[i]);
            importColonneManager.validateObjectManager(ic, it, c1, "creation");
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }

      // on test l'insertion d'un doublon
      ic.setNom("Nip patient");
      try{
         importColonneManager.validateObjectManager(ic, it, c1, "creation");
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
   }

   public void testCrud(){
      createObjectManager();
      updateObjectManager();
   }

   public void createObjectManager(){
      final ImportTemplate it = importTemplateDao.findById(1);
      final Champ c2 = new Champ();
      final ChampEntite ce = champEntiteDao.findById(2);
      final ImportColonne ic1 = new ImportColonne();
      ic1.setNom("TEST");
      ic1.setOrdre(12);

      final int tots = importColonneManager.findAllObjectsManager().size();
      final int cTots = champManager.findAllObjectsManager().size();

      // on vérifie qu'une exception est bien lancée
      Boolean catched = false;
      // on test avec l'importtemplate null
      try{
         importColonneManager.createObjectManager(ic1, null, c2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots);

      // insertion avec un nouveau champ
      final ImportColonne ic2 = new ImportColonne();
      ic2.setNom("AUTRE");
      ic2.setOrdre(12);
      ic2.setImportTemplate(it);
      c2.setChampEntite(ce);
      importColonneManager.createObjectManager(ic2, it, c2);
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots + 1);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 1);
      final Integer idIc2 = ic2.getImportColonneId();

      final ImportColonne icTest2 = importColonneManager.findByIdManager(idIc2);
      assertNotNull(icTest2);
      assertTrue(icTest2.getImportTemplate().equals(it));
      assertTrue(icTest2.getChamp().equals(c2));
      assertTrue(icTest2.getNom().equals("AUTRE"));
      assertTrue(icTest2.getOrdre() == 12);

      importColonneManager.removeObjectManager(icTest2);
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots);
      assertTrue(champManager.findAllObjectsManager().size() == cTots);
   }

   public void updateObjectManager(){
      final ImportTemplate it = importTemplateDao.findById(1);
      final Champ c1 = new Champ();
      final ChampEntite ce = champEntiteDao.findById(2);
      final ChampEntite ce3 = champEntiteDao.findById(3);
      final ChampEntite ce4 = champEntiteDao.findById(4);
      final ImportColonne ic1 = new ImportColonne();
      ic1.setNom("TEST");
      ic1.setOrdre(12);
      ic1.setImportTemplate(it);
      c1.setChampEntite(ce);

      final int tots = importColonneManager.findAllObjectsManager().size();
      final int cTots = champManager.findAllObjectsManager().size();

      importColonneManager.createObjectManager(ic1, it, c1);
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots + 1);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 1);
      final Integer idIc1 = ic1.getImportColonneId();

      final ImportColonne icUp = importColonneManager.findByIdManager(idIc1);
      // on vérifie qu'une exception est bien lancée
      Boolean catched = false;
      // on test avec l'importtemplate null
      try{
         importColonneManager.updateObjectManager(icUp, null, c1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots + 1);

      // update avec un nouveau champ
      icUp.setNom("AUTRE");
      icUp.setImportTemplate(it);
      final Champ c2 = new Champ();
      c2.setChampEntite(ce3);
      importColonneManager.updateObjectManager(icUp, it, c2);
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots + 1);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);

      final ImportColonne icTest = importColonneManager.findByIdManager(idIc1);
      assertNotNull(icTest);
      assertTrue(icTest.getImportTemplate().equals(it));
      assertTrue(icTest.getChamp().equals(c2));
      assertTrue(icTest.getChamp().getChampEntite().equals(ce3));
      assertTrue(icTest.getNom().equals("AUTRE"));
      assertTrue(icTest.getOrdre() == 12);

      // update avec un champ modifié
      c2.setChampEntite(ce4);
      importColonneManager.updateObjectManager(icUp, it, c2);
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots + 1);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
      final ImportColonne icTest2 = importColonneManager.findByIdManager(idIc1);
      assertNotNull(icTest2);
      assertTrue(icTest2.getImportTemplate().equals(it));
      assertTrue(icTest2.getChamp().equals(c2));
      assertTrue(icTest2.getChamp().getChampEntite().equals(ce4));
      assertTrue(icTest2.getNom().equals("AUTRE"));
      assertTrue(icTest2.getOrdre() == 12);

      importColonneManager.removeObjectManager(icTest2);
      assertTrue(importColonneManager.findAllObjectsManager().size() == tots);
      champManager.removeObjectManager(c1);
      assertTrue(champManager.findAllObjectsManager().size() == cTots);
   }

}
