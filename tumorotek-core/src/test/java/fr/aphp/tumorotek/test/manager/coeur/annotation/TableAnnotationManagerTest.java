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
package fr.aphp.tumorotek.test.manager.coeur.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.AnnotationDefautDao;
import fr.aphp.tumorotek.dao.annotation.CatalogueDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationBanqueDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.TableAnnotationValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanquePK;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager TableAnnotationManager.
 * Classe créée le 03/02/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class TableAnnotationManagerTest extends AbstractManagerTest4
{

   /* Managers injectes par Spring*/
   @Autowired
   private TableAnnotationManager tableAnnotationManager;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private CatalogueDao catalogueDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private TableAnnotationBanqueDao tableAnnotationBanqueDao;
   @Autowired
   private TableAnnotationValidator tableAnnotationValidator;
   @Autowired
   private DataTypeDao dataTypeDao;
   @Autowired
   private ChampAnnotationDao champAnnotationDao;
   @Autowired
   private ChampAnnotationManager champAnnotationManager;
   @Autowired
   private AnnotationDefautDao annotationDefautDao;
   @Autowired
   private ItemDao itemDao;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private OperationTypeDao operationTypeDao;

   public TableAnnotationManagerTest(){}

   @Test
   public void testFindAllObjectsManager(){
      final List<TableAnnotation> tables = tableAnnotationManager.findAllObjectsManager();
      assertTrue(tables.size() == 11);
   }

   /**
    * Test la methode findByNomLikeManager.
    */
   @Test
   public void testFindByNomLikeManager(){
      //teste une recherche exactMatch
      List<TableAnnotation> tables = tableAnnotationManager.findByNomLikeManager("TABLE_PAT1", true);
      assertTrue(tables.size() == 1);
      //teste une recherche non exactMatch
      tables = tableAnnotationManager.findByNomLikeManager("TAB", false);
      assertTrue(tables.size() == 5);
      //teste une recherche infructueuse
      tables = tableAnnotationManager.findByNomLikeManager("LUX", true);
      assertTrue(tables.size() == 0);
      //null recherche
      tables = tableAnnotationManager.findByNomLikeManager(null, false);
      assertTrue(tables.size() == 0);
   }

   @Test
   public void testFindByEntiteAndBanqueManager(){
      Entite e = entiteDao.findByNom("Echantillon").get(0);
      Banque b = banqueDao.findById(1);
      List<TableAnnotation> tables = tableAnnotationManager.findByEntiteAndBanqueManager(e, b);
      assertTrue(tables.size() == 4);
      e = entiteDao.findByNom("Patient").get(0);
      tables = tableAnnotationManager.findByEntiteAndBanqueManager(e, b);
      assertTrue(tables.size() == 2);
      b = banqueDao.findById(2);
      tables = tableAnnotationManager.findByEntiteAndBanqueManager(e, b);
      assertTrue(tables.size() == 0);
      tables = tableAnnotationManager.findByEntiteAndBanqueManager(e, null);
      assertTrue(tables.size() == 0);
      tables = tableAnnotationManager.findByEntiteAndBanqueManager(null, b);
      assertTrue(tables.size() == 0);
      tables = tableAnnotationManager.findByEntiteAndBanqueManager(null, null);
      assertTrue(tables.size() == 0);
   }

   @Test
   public void testFindByEntiteBanqueAndCatalogueManager(){
      Entite e = entiteDao.findByNom("Echantillon").get(0);
      Banque b = banqueDao.findById(1);
      List<TableAnnotation> tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(e, b, "INCa");
      assertTrue(tables.size() == 2);
      e = entiteDao.findByNom("Patient").get(0);
      tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(e, b, "INCa");
      assertTrue(tables.size() == 1);
      tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(e, b, "tmp");
      assertTrue(tables.size() == 0);
      b = banqueDao.findById(2);
      tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(e, b, "INCa");
      assertTrue(tables.size() == 0);
      tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(e, null, "INCa");
      assertTrue(tables.size() == 0);
      tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(null, b, "INCa");
      assertTrue(tables.size() == 0);
      tables = tableAnnotationManager.findByEntiteBanqueAndCatalogueManager(null, null, null);
      assertTrue(tables.size() == 0);
   }

   @Test
   public void testFindByEntiteManager(){
      Entite e = entiteDao.findByNom("Prelevement").get(0);
      final Plateforme pf1 = plateformeDao.findById(1);
      List<TableAnnotation> tabs = tableAnnotationManager.findByEntiteAndPlateformeManager(e, pf1);
      assertTrue(tabs.size() == 1);
      e = entiteDao.findByNom("Cession").get(0);
      tabs = tableAnnotationManager.findByEntiteAndPlateformeManager(e, pf1);
      assertTrue(tabs.size() == 0);
      tabs = tableAnnotationManager.findByEntiteAndPlateformeManager(e, pf1);
      assertTrue(tabs.size() == 0);
   }

   @Test
   public void testFindDoublon(){
      //Cree le doublon
      final TableAnnotation t1 = tableAnnotationManager.findByNomLikeManager("TABLE_PAT1", true).get(0);
      final TableAnnotation t2 = new TableAnnotation();
      t2.setNom(t1.getNom());
      t2.setEntite(t1.getEntite());
      t2.setPlateforme(t1.getPlateforme());
      assertTrue(t2.equals(t1));
      assertTrue(tableAnnotationManager.findDoublonManager(t2));
   }

   @Test
   public void testGetBanquesManager(){
      TableAnnotation t = tableAnnotationManager.findByNomLikeManager("INCA_ECHAN1", true).get(0);
      assertTrue(tableAnnotationManager.getBanquesManager(t).size() == 3);
      t = new TableAnnotation();
      assertTrue(tableAnnotationManager.getBanquesManager(t).size() == 0);
   }

   @Test
   public void testGetTableAnnotationBanquesManager(){
      TableAnnotation t = tableAnnotationManager.findByNomLikeManager("INCA_ECHAN1", true).get(0);
      assertTrue(tableAnnotationManager.getTableAnnotationBanquesManager(t).size() == 3);
      t = new TableAnnotation();
      assertTrue(tableAnnotationManager.getTableAnnotationBanquesManager(t).size() == 0);
   }

   @Test
   public void testGetChampAnnotationsManager(){
      TableAnnotation t = tableAnnotationManager.findByNomLikeManager("INCA_ECHAN1", true).get(0);
      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).size() == 5);
      t = new TableAnnotation();
      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).size() == 0);
   }

   @Test
   public void testCRUD(){
      saveManagerTest();
      saveManagerTest();
      savewWithThesaurusChampManagerTest();
      //	moveTableOrderUpManager();
      //	moveTableOrderDownManager();
      //	moveTableOrderUpTopManager();
      //	moveTableOrderDownBottomManager();
      deleteByIdManagerTest();
   }

   /**
    * Teste la methode saveManager. 
    * @throws ParseException 
    */
   private void saveManagerTest(){

      final TableAnnotation table = new TableAnnotation();
      /*Champs obligatoires*/
      table.setNom("TABLE_TEST");
      table.setDescription("testTab descr");
      final Entite e = entiteDao.findByNom("Patient").get(0);
      // references vers banques
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      banks.add(b2);
      final Utilisateur u = utilisateurDao.findById(2);
      final ChampAnnotation c1 = new ChampAnnotation();
      c1.setNom("alpha_table_test");
      c1.setDataType(dataTypeDao.findById(1));
      c1.setOrdre(1);
      //c1.setTableAnnotation(table);
      final ChampAnnotation c2 = new ChampAnnotation();
      c2.setNom("thes_table_test¢¢¢€}}");
      c2.setDataType(dataTypeDao.findById(7));
      c2.setOrdre(2);
      final Item i1 = new Item();
      i1.setLabel("it1");
      i1.setChampAnnotation(c2);
      final Item i2 = new Item();
      i2.setLabel("it2");
      i2.setChampAnnotation(c2);
      final Set<Item> items = new HashSet<>();
      items.add(i1);
      items.add(i2);
      c2.setItems(items);
      //c2.setTableAnnotation(table);
      final List<ChampAnnotation> champs = new ArrayList<>();
      champs.add(c1);
      champs.add(c2);
      final Plateforme pf2 = plateformeDao.findById(2);

      //required Entite
      try{
         tableAnnotationManager.createOrsaveManager(table, null, null, null, banks, null, u, "creation", null, null);
      }catch(final RequiredObjectIsNullException re){
         assertTrue(true);
      }
      //operation invalide
      try{
         tableAnnotationManager.createOrsaveManager(table, e, null, null, banks, null, u, null, null, null);
      }catch(final NullPointerException ne){
         assertTrue(ne.getMessage().equals("operation cannot be " + "set to null for createorUpdateMethod"));
      }
      try{
         tableAnnotationManager.createOrsaveManager(table, e, null, null, banks, null, u, "", null, null);
      }catch(final IllegalArgumentException ie){
         assertTrue(ie.getMessage().equals("Operation must match " + "'creation/modification' values"));
      }
      // champs invalide -> rollback
      try{
         tableAnnotationManager.createOrsaveManager(table, e, null, champs, banks, null, u, "creation", null, null);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("champAnnotation.nom.illegal"));
      }
      assertTrue(table.getTableAnnotationId() == null);
      assertTrue(c1.getId() == null);
      assertTrue(table.getChampAnnotations().size() == 0);
      assertTrue(c2.getItems().size() == 2);
      testFindAllObjectsManager();

      c2.setNom("thes_table_test");
      tableAnnotationManager.createOrsaveManager(table, e, null, champs, banks, null, u, "creation", "/tmp/", pf2);
      assertTrue((tableAnnotationManager.findByNomLikeManager("TABLE_TEST", true)).size() == 1);
      assertTrue((tableAnnotationManager.getBanquesManager(table)).size() == 2);
      assertTrue(IterableUtils.toList(itemDao.findAll()).size() == 89);
      assertTrue(tableAnnotationBanqueDao.findById(new TableAnnotationBanquePK(b2, table)).getOrdre().equals(1));
      assertTrue(tableAnnotationManager.getChampAnnotationsManager(table).size() == 2);
      assertTrue(getOperationManager().findByObjectManager(table).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(table).get(0).getOperationType().getNom().equals("Creation"));

      //Insertion d'un doublon engendrant une exception
      final TableAnnotation t2 = new TableAnnotation();
      t2.setNom(table.getNom());
      t2.setEntite(table.getEntite());
      t2.setPlateforme(table.getPlateforme());
      assertTrue(table.equals(t2));
      Boolean catched = false;
      try{
         tableAnnotationManager.createOrsaveManager(t2, null, null, null, null, null, u, "creation", null, pf2);
      }catch(final Exception ex){
         if(ex.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(tableAnnotationManager.findByNomLikeManager("TABLE_TEST", false).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(table).size() == 1);
   }

   /**
    * Teste la methode saveManager. 
    * @throws ParseException 
    */
   private void saveManagerTest(){
      final TableAnnotation t = tableAnnotationManager.findByNomLikeManager("TABLE_TEST", false).get(0);
      t.setNom("NEWTABLE");
      t.setDescription("Voici la nouvelle table");
      // Catalogue c = catalogueDao.findById(1);
      // references vers banques
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      final Banque b4 = banqueDao.findById(4);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b3);
      banks.add(b1);
      banks.add(b4);
      final Utilisateur u = utilisateurDao.findById(2);
      // modification c1
      final ChampAnnotation c1 = tableAnnotationManager.getChampAnnotationsManager(t).iterator().next();
      assertTrue(c1.getNom().equals("alpha_table_test"));
      c1.setNom("alpha2_table_test");
      final AnnotationDefaut defaut = new AnnotationDefaut();
      defaut.setAlphanum("defaut_test");
      defaut.setObligatoire(false);
      defaut.setChampAnnotation(c1);
      final Set<AnnotationDefaut> defauts = new HashSet<>();
      defauts.add(defaut);
      c1.setAnnotationDefauts(defauts);
      // nouveau champ
      final ChampAnnotation c3 = new ChampAnnotation();
      final AnnotationDefaut defaut2 = new AnnotationDefaut();
      defaut2.setAlphanum("12345");
      defaut2.setChampAnnotation(c3);
      c3.setNom("num_table_test");
      c3.setDataType(dataTypeDao.findById(5));
      c3.setOrdre(3);
      c3.setTableAnnotation(t);
      final List<ChampAnnotation> champs = new ArrayList<>();
      champs.add(c3);
      champs.add(c1);

      //Modification d'un champ entrainant validationException
      boolean catched = false;
      c1.setNom("$$&¤¤&");
      //champs.remove(1);
      try{
         tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, null, u, "modification", null, null);
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);

      c1.setNom("alpha2_table_test");
      c1.setAnnotationDefauts(defauts);
      tableAnnotationManager.createOrsaveManager(t, null, null, champs, banks, null, u, "modification", null, null);
      //		assertTrue((tableAnnotationManager
      //				.findByNomLikeManager("NEWTABLE", true)).get(0)
      //					.getCatalogue().equals(c));
      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).size() == 3);
      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).iterator().next().getNom().equals("alpha2_table_test"));
      assertTrue(getOperationManager().findByObjectManager(champAnnotationDao.findByNom("alpha2_table_test").get(0)).size() == 2);
      assertTrue(IterableUtils.toList(annotationDefautDao.findAll()).size() == 12);
      assertTrue(getOperationManager().findByObjectManager(champAnnotationDao.findByNom("thes_table_test").get(0)).size() == 1);
      assertTrue((tableAnnotationManager.getBanquesManager(t)).size() == 3);
      assertTrue(tableAnnotationBanqueDao.findById(new TableAnnotationBanquePK(b1, t)).getOrdre().equals(1));
      assertTrue(getOperationManager().findByObjectManager(t).size() == 2);

      assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(t, operationTypeDao.findByNom("Modification").get(0))
         .size() == 1);
      tableAnnotationManager.createOrsaveManager(t, null, null, null, null, null, u, "modification", null, null);
      assertTrue(tableAnnotationManager.getBanquesManager(t).size() == 3);
      tableAnnotationManager.createOrsaveManager(t, null, null, null, new ArrayList<Banque>(), null, u, "modification",
         null, null);
      assertTrue(tableAnnotationManager.getBanquesManager(t).size() == 0);
      // pour tester la cascade plus loin lors du testRemove
      tableAnnotationManager.createOrsaveManager(t, null, null, null, banks, null, u, "modification", null, null);
      assertTrue(tableAnnotationManager.getBanquesManager(t).size() == 3);
      //Modification en un doublon engendrant une exception
      catched = false;
      try{
         t.setNom("TABLE_ECHAN1");
         final Entite e = entiteDao.findByNom("Echantillon").get(0);
         t.setPlateforme(plateformeDao.findById(1));
         tableAnnotationManager.createOrsaveManager(t, e, null, null, null, null, u, "modification", null, null);
      }catch(final DoublonFoundException e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(tableAnnotationManager.findByNomLikeManager("INCA_ECHAN1", true).size() == 1);
      t.setPlateforme(plateformeDao.findById(2));

      // la cascade de deletion d'une reference vers banques vers les valeurs
      // d'annotations est testée dans AnnotationValeurManagerTest
   }

   private void savewWithThesaurusChampManagerTest(){
      final TableAnnotation t = tableAnnotationManager.findByNomLikeManager("NEWTABLE", false).get(0);
      final Utilisateur u = utilisateurDao.findById(2);
      // nouveau champ
      final ChampAnnotation c3 = new ChampAnnotation();
      c3.setNom("thes_test");
      c3.setDataType(dataTypeDao.findById(6));
      c3.setOrdre(4);
      c3.setTableAnnotation(t);
      final List<ChampAnnotation> champs = new ArrayList<>();
      champs.add(c3);

      Set<Item> its = new LinkedHashSet<>();
      final Item i1 = new Item();
      i1.setLabel("label1");
      i1.setChampAnnotation(c3);
      final Item i2 = new Item();
      i2.setLabel("label2");
      i2.setValeur("val2");
      i2.setChampAnnotation(c3);
      final Item i3 = new Item();
      i3.setLabel("label3");
      i3.setValeur("val3");
      i3.setChampAnnotation(c3);
      its.add(i1);
      its.add(i2);
      its.add(i3);
      c3.setItems(its);

      tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, null, u, "modification", null, null);

      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).size() == 4);

      assertTrue(champAnnotationManager.getItemsManager(c3, null).size() == 3);
      assertTrue((tableAnnotationManager.getBanquesManager(t)).size() == 3);

      // cast pour ne plus utiliser PersistentSet 
      // -> LazyInitialisationException ds TableAnnotationManager
      its = new LinkedHashSet<>();
      //its.addAll(champAnnotationManager.getItemsManager(c3));
      its.addAll(c3.getItems());
      new ArrayList<>(its).get(0).setLabel("newLabel1");
      new ArrayList<>(its).get(1).setValeur("newValeur2");
      new ArrayList<>(its).get(2).setLabel("newLabel3");
      new ArrayList<>(its).get(2).setValeur("newValeur3");
      c3.setItems(its);

      tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, null, u, "modification", null, null);

      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c3, null)).get(0).getLabel().equals("label2"));
      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c3, null)).get(1).getLabel().equals("newLabel1"));
      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c3, null)).get(2).getLabel().equals("newLabel3"));
      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c3, null)).get(2).getValeur().equals("newValeur3"));
   }

   //	@Test
   //	public void moveTableOrderUpManager() {
   //		TableAnnotation t = tableAnnotationManager
   //								.findByNomLikeManager("NEWTABLE", false).get(0);
   //		Set<TableAnnotationBanque> tabs = 
   //					tableAnnotationManager.getTableAnnotationBanquesManager(t);
   //		TableAnnotationBanque tab = null;
   //		Banque b1 = banqueDao.findById(1);
   //		Banque b3 = banqueDao.findById(3);
   //		Banque b4 = banqueDao.findById(4);
   //		// verifie l'ordre
   //		Iterator<TableAnnotationBanque> it = tabs.iterator();
   //		while (it.hasNext()) {
   //			tab = (TableAnnotationBanque) it.next();
   //			if (tab.getBanque().equals(b1)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			} else if (tab.getBanque().equals(b3)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			} else if (tab.getBanque().equals(b4)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			}			
   //		}
   //		t.setTableAnnotationBanques(tabs);
   //		
   //		// up la b1
   //		tableAnnotationManager.moveTableOrderUpDownManager(t, b1, true);
   //		it = t.getTableAnnotationBanques().iterator();
   //		while (it.hasNext()) {
   //			tab = (TableAnnotationBanque) it.next();
   //			if (tab.getBanque().equals(b1)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			} else if (tab.getBanque().equals(b3)) {
   //				assertTrue(tab.getOrdre().equals(2));
   //			} else if (tab.getBanque().equals(b4)) {
   //				assertTrue(tab.getOrdre().equals(3));
   //			}
   //		}
   //	}
   //	
   //	@Test
   //	public void moveTableOrderDownManager() {
   //		TableAnnotation t = tableAnnotationManager
   //								.findByNomLikeManager("NEWTABLE", false).get(0);
   //		Set<TableAnnotationBanque> tabs = 
   //					tableAnnotationManager.getTableAnnotationBanquesManager(t);
   //		TableAnnotationBanque tab = null;
   //		Banque b1 = banqueDao.findById(1);
   //		Banque b3 = banqueDao.findById(3);
   //		Banque b4 = banqueDao.findById(4);
   //
   //		t.setTableAnnotationBanques(tabs);
   //		
   //		// down la b1
   //		tableAnnotationManager.moveTableOrderUpDownManager(t, b1, false);
   //		Iterator<TableAnnotationBanque> it = tabs.iterator();
   //		it = t.getTableAnnotationBanques().iterator();
   //		while (it.hasNext()) {
   //			tab = (TableAnnotationBanque) it.next();
   //			if (tab.getBanque().equals(b1)) {
   //				assertTrue(tab.getOrdre().equals(3));
   //			} else if (tab.getBanque().equals(b3)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			} else if (tab.getBanque().equals(b4)) {
   //				assertTrue(tab.getOrdre().equals(2));
   //			}
   //		}
   //	}
   //		
   //	@Test
   //	public void moveTableOrderUpTopManager() {
   //		TableAnnotation t = tableAnnotationManager
   //								.findByNomLikeManager("NEWTABLE", false).get(0);
   //		Set<TableAnnotationBanque> tabs = 
   //					tableAnnotationManager.getTableAnnotationBanquesManager(t);
   //		TableAnnotationBanque tab = null;
   //		Banque b1 = banqueDao.findById(1);
   //		Banque b3 = banqueDao.findById(3);
   //		Banque b4 = banqueDao.findById(4);
   //		t.setTableAnnotationBanques(tabs);
   //		
   //		// up la b3
   //		tableAnnotationManager.moveTableOrderUpDownManager(t, b3, true);
   //		Iterator<TableAnnotationBanque> it = 
   //									t.getTableAnnotationBanques().iterator();
   //		while (it.hasNext()) {
   //			tab = (TableAnnotationBanque) it.next();
   //			if (tab.getBanque().equals(b1)) {
   //				assertTrue(tab.getOrdre().equals(2));
   //			} else if (tab.getBanque().equals(b3)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			} else if (tab.getBanque().equals(b4)) {
   //				assertTrue(tab.getOrdre().equals(3));
   //			}
   //		}
   //	}
   //		
   //	@Test
   //	public void moveTableOrderDownBottomManager() {
   //		TableAnnotation t = tableAnnotationManager
   //								.findByNomLikeManager("NEWTABLE", false).get(0);
   //		Set<TableAnnotationBanque> tabs = 
   //					tableAnnotationManager.getTableAnnotationBanquesManager(t);
   //		TableAnnotationBanque tab = null;
   //		Banque b1 = banqueDao.findById(1);
   //		Banque b3 = banqueDao.findById(3);
   //		Banque b4 = banqueDao.findById(4);
   //		t.setTableAnnotationBanques(tabs);
   //		
   //		// up la b3
   //		tableAnnotationManager.moveTableOrderUpDownManager(t, b4, false);
   //		Iterator<TableAnnotationBanque> it = 
   //									t.getTableAnnotationBanques().iterator();
   //		while (it.hasNext()) {
   //			tab = (TableAnnotationBanque) it.next();
   //			if (tab.getBanque().equals(b1)) {
   //				assertTrue(tab.getOrdre().equals(2));
   //			} else if (tab.getBanque().equals(b3)) {
   //				assertTrue(tab.getOrdre().equals(1));
   //			} else if (tab.getBanque().equals(b4)) {
   //				assertTrue(tab.getOrdre().equals(3));
   //			}
   //		}
   //	}

   /**
    * Teste la methode deleteByIdManager. 
    */
   private void deleteByIdManagerTest(){
      final TableAnnotation t = tableAnnotationManager.findByNomLikeManager("NEWTABLE", true).get(0);
      final Set<ChampAnnotation> chps = tableAnnotationManager.getChampAnnotationsManager(t);
      tableAnnotationManager.deleteByIdManager(t, null, utilisateurDao.findById(1), "/tmp/");
      assertTrue(tableAnnotationManager.findByNomLikeManager("NEWTABLE", true).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(t).size() == 0);
      //verification de la suppression cascade des associations
      assertNull(tableAnnotationBanqueDao.findById(new TableAnnotationBanquePK(banqueDao.findById(1), t)));
      tableAnnotationManager.deleteByIdManager(null, null, null, null);
      //verifie que l'etat des tables modifies est revenu identique
      assertTrue(IterableUtils.toList(champAnnotationDao.findAll()).size() == 48);
      assertTrue(IterableUtils.toList(annotationDefautDao.findAll()).size() == 11);
      assertTrue(IterableUtils.toList(tableAnnotationBanqueDao.findAll()).size() == 11);
      testFindAllObjectsManager();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(t);
      fs.addAll(chps);
      cleanUpFantomes(fs);
   }

   @Test
   public void testTableValidation(){
      final TableAnnotation t = new TableAnnotation();

      // nom 
      try{
         BeanValidator.validateObject(t, new Validator[] {tableAnnotationValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("tableAnnotation.nom.empty"));
      }
      t.setNom("");
      try{
         BeanValidator.validateObject(t, new Validator[] {tableAnnotationValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("tableAnnotation.nom.empty"));
      }
      t.setNom("$$###''");
      try{
         BeanValidator.validateObject(t, new Validator[] {tableAnnotationValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("tableAnnotation.nom.illegal"));
      }
      t.setNom(createOverLength(50));
      try{
         BeanValidator.validateObject(t, new Validator[] {tableAnnotationValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("tableAnnotation.nom.tooLong"));
      }
   }

   @Test
   public void testUpdateChampOrdersManager(){
      final TableAnnotation t = new TableAnnotation();
      t.setNom("NEWTABLE");
      final Entite e = entiteDao.findByNom("Patient").get(0);
      final Utilisateur u = utilisateurDao.findById(2);
      // Champs
      final ChampAnnotation c1 = new ChampAnnotation();
      c1.setNom("c1");
      c1.setDataType(dataTypeDao.findById(5));
      c1.setOrdre(1);
      c1.setTableAnnotation(t);
      final ChampAnnotation c2 = new ChampAnnotation();
      c2.setNom("c2");
      c2.setDataType(dataTypeDao.findById(2));
      c2.setOrdre(1);
      c2.setTableAnnotation(t);
      final ChampAnnotation c3 = new ChampAnnotation();
      c3.setNom("c3");
      c3.setDataType(dataTypeDao.findById(1));
      c3.setOrdre(1);
      c3.setTableAnnotation(t);
      final ChampAnnotation c4 = new ChampAnnotation();
      c4.setNom("c4");
      c4.setDataType(dataTypeDao.findById(8));
      c4.setOrdre(1);
      c4.setTableAnnotation(t);
      final List<ChampAnnotation> champs = new ArrayList<>();
      champs.add(c1);
      champs.add(c2);
      champs.add(c3);
      champs.add(c4);
      final Plateforme pf2 = plateformeDao.findById(2);

      // file system error
      final List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));
      try{
         tableAnnotationManager.createOrsaveManager(t, e, null, champs, banks, null, u, "creation", "zaz", pf2);
      }catch(final RuntimeException re){
         assertTrue(re.getMessage().equals("error.filesystem.access"));
      }
      tableAnnotationManager.createOrsaveManager(t, e, null, champs, null, null, u, "creation", "/tmp/", pf2);
      assertTrue((tableAnnotationManager.findByNomLikeManager("NEWTABLE", true)).size() == 1);
      final TableAnnotation t2 = tableAnnotationManager.findByNomLikeManager("NEWTABLE", true).get(0);

      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t2).size() == 4);
      assertEquals(24, getOperationManager().findAllObjectsManager().size());

      // deplace les champs*
      final List<ChampAnnotation> champs2 = new ArrayList<>();
      champs2.add(c4);
      champs2.add(c1);
      champs2.add(c3);
      champs2.add(c2);

      tableAnnotationManager.updateChampOrdersManager(champs2);

      assertTrue(getOperationManager().findAllObjectsManager().size() == 24);

      final TableAnnotation t3 = tableAnnotationManager.findByNomLikeManager("NEWTABLE", true).get(0);

      final List<ChampAnnotation> champsOrdered =
         new ArrayList<>(tableAnnotationManager.getChampAnnotationsManager(t3));
      assertTrue(champsOrdered.get(0).equals(c4));
      assertTrue(champsOrdered.get(1).equals(c1));
      assertTrue(champsOrdered.get(2).equals(c3));
      assertTrue(champsOrdered.get(3).equals(c2));

      // clean up
      tableAnnotationManager.deleteByIdManager(t3, null, u, "/tmp/");
      //verifie que l'etat des tables modifies est revenu identique
      assertTrue(champAnnotationManager.findAllObjectsManager().size() == 48);
      testFindAllObjectsManager();

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(t);
      fs.addAll(champsOrdered);
      cleanUpFantomes(fs);
   }

   @Test
   public void testFindByBanquesManager(){
      List<Banque> banks = new ArrayList<>();
      banks.add(banqueDao.findById(1));
      List<TableAnnotation> tabs = tableAnnotationManager.findByBanquesManager(banks, true);
      assertTrue(tabs.size() == 9);
      tabs = tableAnnotationManager.findByBanquesManager(banks, false);
      assertTrue(tabs.size() == 5);
      banks.add(banqueDao.findById(2));
      tabs = tableAnnotationManager.findByBanquesManager(banks, true);
      assertTrue(tabs.size() == 9);
      tabs = tableAnnotationManager.findByBanquesManager(banks, false);
      assertTrue(tabs.size() == 5);
      banks.clear();
      tabs = tableAnnotationManager.findByBanquesManager(banks, true);
      assertTrue(tabs.size() == 0);
      tabs = tableAnnotationManager.findByBanquesManager(banks, false);
      assertTrue(tabs.size() == 0);
      banks = null;
      tabs = tableAnnotationManager.findByBanquesManager(banks, true);
      assertTrue(tabs.size() == 0);
      tabs = tableAnnotationManager.findByBanquesManager(banks, false);
      assertTrue(tabs.size() == 0);
   }

   @Test
   public void testFindByCataloguesManager(){
      final Catalogue c = catalogueDao.findById(1);
      final List<Catalogue> catas = new ArrayList<>();
      catas.add(c);
      List<TableAnnotation> tabs = tableAnnotationManager.findByCataloguesManager(catas);
      assertTrue(tabs.size() == 4);
      catas.clear();
      tabs = tableAnnotationManager.findByCataloguesManager(catas);
      assertTrue(tabs.size() == 0);
      tabs = tableAnnotationManager.findByCataloguesManager(null);
      assertTrue(tabs.size() == 0);
   }

   @Test
   public void testFindByCatalogueAndChpEditManager(){
      final Catalogue c = catalogueDao.findById(1);
      List<TableAnnotation> tabs = tableAnnotationManager.findByCatalogueAndChpEditManager(c);
      assertTrue(tabs.size() == 3);
      assertTrue(tabs.get(0).getEntite().getNom().equals("Patient"));

      tabs = tableAnnotationManager.findByCatalogueAndChpEditManager(null);
      assertTrue(tabs.isEmpty());
   }

   @Test
   public void savewWithThesaurusCatalogue(){
      final TableAnnotation t = tableAnnotationManager.findByNomLikeManager("INCa-Patient", false).get(0);
      final Utilisateur u = utilisateurDao.findById(2);
      // champ catalogue INCa 057
      final ChampAnnotation c = champAnnotationDao.findById(21);

      final Banque b1 = banqueDao.findById(1);
      final Plateforme p1 = b1.getPlateforme();

      final Set<Item> its = new LinkedHashSet<>();
      final Item i1 = new Item();
      i1.setLabel("protocole1");
      i1.setChampAnnotation(c);
      final Item i2 = new Item();
      i2.setLabel("protocole2");
      i2.setChampAnnotation(c);
      i1.setPlateforme(p1);
      i2.setPlateforme(p1);
      its.add(i1);
      its.add(i2);
      c.setItems(its);

      final List<ChampAnnotation> champs = new ArrayList<>();
      champs.add(c);

      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).size() == 8);

      tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, b1, u, "modification", null, null);

      assertTrue(tableAnnotationManager.getChampAnnotationsManager(t).size() == 8);
      assertTrue(champAnnotationManager.getItemsManager(c, b1).size() == 2);

      final Banque b4 = banqueDao.findById(4);
      final Plateforme p2 = b4.getPlateforme();

      // cast pour ne plus utiliser PersistentSet 
      // -> LazyInitialisationException ds TableAnnotationManager
      // its = new LinkedHashSet<Item>();
      final Set<Item> its2 = new LinkedHashSet<>();
      final Item i3 = new Item();
      i3.setLabel("protocole3");
      i3.setChampAnnotation(c);
      final Item i4 = new Item();
      i4.setLabel("protocole4");
      i4.setChampAnnotation(c);
      final Item i5 = new Item();
      i5.setLabel("protocole5");
      i5.setChampAnnotation(c);
      i3.setPlateforme(p2);
      i4.setPlateforme(p2);
      i5.setPlateforme(p2);
      its2.add(i3);
      its2.add(i4);
      its2.add(i5);
      c.setItems(its2);

      tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, b4, u, "modification", null, null);

      assertTrue(champAnnotationManager.getItemsManager(c, b1).size() == 2);

      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c, b4)).get(0).getLabel().equals("protocole3"));
      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c, b4)).get(1).getLabel().equals("protocole4"));
      assertTrue(new ArrayList<>(champAnnotationManager.getItemsManager(c, b4)).get(2).getLabel().equals("protocole5"));

      // suppression des items pour la pf2
      c.setItems(new HashSet<Item>());
      tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, b4, u, "modification", null, null);

      assertTrue(champAnnotationManager.getItemsManager(c, b1).size() == 2);
      assertTrue(champAnnotationManager.getItemsManager(c, b4).size() == 0);

      // suppression des items pour la pf1
      c.setItems(new HashSet<Item>());
      tableAnnotationManager.createOrsaveManager(t, null, null, champs, null, b1, u, "modification", null, null);

      assertTrue(champAnnotationManager.getItemsManager(c, b1).size() == 0);

      testFindAllObjectsManager();
      assertTrue(IterableUtils.toList(itemDao.findAll()).size() == 87);

      // clean up operations
      Iterator<Operation> opsIt = getOperationManager().findByObjectManager(t).iterator();
      while(opsIt.hasNext()){
         getOperationManager().deleteByIdManager(opsIt.next());
      }
      opsIt = getOperationManager().findByObjectManager(c).iterator();
      while(opsIt.hasNext()){
         getOperationManager().deleteByIdManager(opsIt.next());
      }
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   @Test
   public void testUpdateTableFichiers() throws IOException{
      final TableAnnotation t = tableAnnotationManager.findByNomLikeManager("TABLE_PAT1", true).get(0);
      final List<Banque> banks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banks.add(b1);
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      final Banque b3 = banqueDao.findById(3);
      banks.add(b3);
      final Utilisateur u = utilisateurDao.findById(1);

      if(new File("/tmp/pt_1").exists()){
         // nettoyage
         try{
            FileUtils.deleteDirectory(new File("/tmp/pt_1"));
            Thread.sleep(500);
            assertFalse(new File("/tmp/pt_1").exists());
         }catch(final IOException e){
            assertFalse(true);
         }catch(final InterruptedException e){
            assertFalse(true);
            e.printStackTrace();
         }
      }

      tableAnnotationManager.createOrsaveManager(t, null, null, null, banks, null, u, "modification", "/tmp/", null);

      assertTrue(new File("/tmp/pt_1").list().length == 2);
      assertTrue(new File("/tmp/pt_1/coll_2/anno").list().length == 1);
      assertTrue(new File("/tmp/pt_1/coll_2/anno/chp_13").isDirectory());
      assertTrue(new File("/tmp/pt_1/coll_3/anno").list().length == 1);
      assertTrue(new File("/tmp/pt_1/coll_3/anno/chp_13").isDirectory());
      assertFalse(new File("/tmp/pt_1/coll_1").exists());

      // ajout deux fichiers
      new File("/tmp/pt_1/coll_2/anno/chp_13/guns").createNewFile();
      new File("/tmp/pt_1/coll_3/anno/chp_13/roses").createNewFile();
      assertTrue(new File("/tmp/pt_1/coll_2/anno/chp_13").list().length == 1);
      assertTrue(new File("/tmp/pt_1/coll_3/anno/chp_13").list().length == 1);

      // suprpession des associations
      banks.remove(b2);
      banks.remove(b3);
      tableAnnotationManager.createOrsaveManager(t, null, null, null, banks, null, u, "modification", "/tmp/", null);

      assertTrue(new File("/tmp/pt_1").list().length == 2);
      assertTrue(new File("/tmp/pt_1/coll_2/anno").list().length == 0);
      assertFalse(new File("/tmp/pt_1/coll_2/anno/chp_1").exists());
      assertTrue(new File("/tmp/pt_1/coll_3/anno").list().length == 0);
      assertFalse(new File("/tmp/pt_1/coll_3/anno/chp_1").exists());

      // clean up
      // nettoyage
      try{
         FileUtils.deleteDirectory(new File("/tmp/pt_1"));
         Thread.sleep(500);
         assertFalse(new File("/tmp/pt_1").exists());
      }catch(final IOException e){
         assertFalse(true);
      }catch(final InterruptedException e){
         assertFalse(true);
         e.printStackTrace();
      }

      final Iterator<Operation> ops = getOperationManager().findByObjectManager(t).iterator();

      while(ops.hasNext()){
         getOperationManager().deleteByIdManager(ops.next());
      }
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   @Test
   public void testFindByPlateformeManager(){
      Plateforme pf = plateformeDao.findById(1);
      assertTrue(tableAnnotationManager.findByPlateformeManager(pf).size() == 5);
      pf = plateformeDao.findById(2);
      assertTrue(tableAnnotationManager.findByPlateformeManager(pf).isEmpty());
   }
}
