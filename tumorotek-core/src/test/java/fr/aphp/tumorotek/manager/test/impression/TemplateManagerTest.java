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
package fr.aphp.tumorotek.manager.test.impression;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static fr.aphp.tumorotek.model.impression.ETemplateType.BLOC;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.impression.BlocImpressionTemplateManager;
import fr.aphp.tumorotek.manager.impression.ChampImprimeManager;
import fr.aphp.tumorotek.manager.impression.TableAnnotationTemplateManager;
import fr.aphp.tumorotek.manager.impression.TemplateManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le manager TemplateManager.
 * Classe créée le 26/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TemplateManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TemplateManager templateManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private BlocImpressionDao blocImpressionDao;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private BlocImpressionTemplateManager blocImpressionTemplateManager;
   @Autowired
   private ChampImprimeManager champImprimeManager;
   @Autowired
   private TableAnnotationManager tableAnnotationManager;
   @Autowired
   private TableAnnotationTemplateManager tableAnnotationTemplateManager;

   public TemplateManagerTest(){

   }

   @Test
   public void testFindById(){
      final Template temp = templateManager.findByIdManager(1);
      assertNotNull(temp);

      final Template tempNull = templateManager.findByIdManager(10);
      assertNull(tempNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Template> list = templateManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   /**
    * Test la méthode findByBanqueManager.
    */
   @Test
   public void testFindByBanqueManager(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);

      List<Template> list = templateManager.findByBanqueManager(b1);
      assertTrue(list.size() == 2);

      list = templateManager.findByBanqueManager(b3);
      assertTrue(list.size() == 0);

      list = templateManager.findByBanqueManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByBanqueEntiteManager.
    */
   @Test
   public void testFindByBanqueEntiteManager(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      final Entite e1 = entiteDao.findById(1);
      final Entite e3 = entiteDao.findById(3);

      List<Template> list = templateManager.findByBanqueEntiteManager(b1, e1);
      assertTrue(list.size() == 1);

      list = templateManager.findByBanqueEntiteManager(b3, e1);
      assertTrue(list.size() == 0);

      list = templateManager.findByBanqueEntiteManager(b1, e3);
      assertTrue(list.size() == 0);

      list = templateManager.findByBanqueEntiteManager(b3, null);
      assertTrue(list.size() == 0);

      list = templateManager.findByBanqueEntiteManager(null, e1);
      assertTrue(list.size() == 0);

      list = templateManager.findByBanqueEntiteManager(null, null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      final Template t1 = new Template();
      t1.setType(BLOC);
      t1.setNom("Fiche prélèvement");
      assertFalse(templateManager.findDoublonManager(t1));
      t1.setNom("AUTRE");
      t1.setBanque(b1);
      assertFalse(templateManager.findDoublonManager(t1));

      t1.setNom("Fiche prélèvement");
      assertTrue(templateManager.findDoublonManager(t1));
      t1.setBanque(b3);
      assertFalse(templateManager.findDoublonManager(t1));

      final Template t2 = templateManager.findByIdManager(2);
      assertFalse(templateManager.findDoublonManager(t2));

      t2.setNom("Fiche prélèvement");
      assertTrue(templateManager.findDoublonManager(t2));

      assertFalse(templateManager.findDoublonManager(null));
   }

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   @Test
   public void testCreateObjectManager() throws ParseException{

      final Template t1 = new Template();
      final Banque b1 = banqueDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final BlocImpression bi1 = blocImpressionDao.findById(1);
      final BlocImpression bi2 = blocImpressionDao.findById(2);
      final ChampEntite ce1 = champEntiteDao.findById(1);
      final ChampEntite ce2 = champEntiteDao.findById(2);
      List<BlocImpressionTemplate> blocs = new ArrayList<>();
      List<ChampImprime> champs = new ArrayList<>();
      t1.setType(BLOC);
      t1.setNom("Template de test");
      final TableAnnotation ta1 = tableAnnotationManager.findByNomLikeManager("TABLE_PAT1", true).get(0);
      final TableAnnotation ta2 = tableAnnotationManager.findByNomLikeManager("TABLE_PREL1", true).get(0);
      List<TableAnnotationTemplate> tables = new ArrayList<>();

      Boolean catched = false;
      // on test l'insertion avec la banque null
      try{
         templateManager.createObjectManager(t1, null, e2, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 3);

      // on test l'insertion avec l'entité null
      try{
         templateManager.createObjectManager(t1, b1, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 3);

      // on test l'insertion d'un doublon
      t1.setNom("Fiche prélèvement");
      try{
         templateManager.createObjectManager(t1, b1, e2, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 3);

      // Test de la validation lors de la création
      t1.setNom("Template de test");
      try{
         validationInsert(t1);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(templateManager.findAllObjectsManager().size() == 3);

      // on teste la validation des BlocImpressionTemplate
      final BlocImpressionTemplate bit1 = new BlocImpressionTemplate();
      bit1.setTemplate(t1);
      bit1.setBlocImpression(bi1);
      final BlocImpressionTemplate bit2 = new BlocImpressionTemplate();
      bit2.setTemplate(t1);
      bit2.setBlocImpression(null);
      blocs.add(bit1);
      blocs.add(bit2);
      catched = false;
      try{
         templateManager.createObjectManager(t1, b1, e2, blocs, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 3);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);

      // on teste la validation des ChampImprimes
      final ChampImprime ci1 = new ChampImprime();
      ci1.setTemplate(t1);
      ci1.setChampEntite(ce1);
      ci1.setBlocImpression(bi1);
      final ChampImprime ci2 = new ChampImprime();
      ci2.setTemplate(t1);
      ci2.setChampEntite(null);
      ci2.setBlocImpression(bi1);
      champs.add(ci1);
      champs.add(ci2);
      catched = false;
      try{
         templateManager.createObjectManager(t1, b1, e2, null, champs, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 3);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);

      // on teste la validation des TableAnnotationTemplate
      TableAnnotationTemplate tat1 = new TableAnnotationTemplate();
      tat1.setTemplate(t1);
      tat1.setTableAnnotation(ta1);
      tat1.setOrdre(3);
      TableAnnotationTemplate tat2 = new TableAnnotationTemplate();
      tat2.setTemplate(t1);
      tat2.setTableAnnotation(null);
      tat2.setOrdre(4);
      tables.add(tat1);
      tables.add(tat2);
      catched = false;
      try{
         templateManager.createObjectManager(t1, b1, e2, null, null, tables);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 3);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);

      // insertion valide avec les assos à null
      t1.setNom("Template de test");
      t1.setDescription("DESC");
      t1.setEnTete("En Tete");
      t1.setPiedPage("Pied");
      templateManager.createObjectManager(t1, b1, e2, null, null, null);
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      final Integer idT1 = t1.getTemplateId();

      // Vérification
      final Template tTest = templateManager.findByIdManager(idT1);
      assertNotNull(tTest);
      assertTrue(tTest.getNom().equals("Template de test"));
      assertTrue(tTest.getDescription().equals("DESC"));
      assertTrue(tTest.getEnTete().equals("En Tete"));
      assertTrue(tTest.getPiedPage().equals("Pied"));
      assertNotNull(tTest.getEntite());
      assertNotNull(tTest.getBanque());

      // insertion valide avec les assos
      final Template t2 = new Template();
      t2.setType(BLOC);
      t2.setNom("Fiche imperssion complète");
      bit1.setTemplate(t2);
      bit1.setBlocImpression(bi1);
      bit1.setOrdre(1);
      bit2.setTemplate(t2);
      bit2.setBlocImpression(bi2);
      bit2.setOrdre(2);
      blocs = new ArrayList<>();
      blocs.add(bit1);
      blocs.add(bit2);

      ci1.setTemplate(t2);
      ci1.setChampEntite(ce1);
      ci1.setOrdre(1);
      ci1.setBlocImpression(bi1);
      ci2.setTemplate(t2);
      ci2.setChampEntite(ce2);
      ci2.setOrdre(2);
      ci2.setBlocImpression(bi1);
      champs = new ArrayList<>();
      champs.add(ci1);
      champs.add(ci2);
      tables = new ArrayList<>();
      tat1 = new TableAnnotationTemplate();
      tat1.setTemplate(t2);
      tat1.setTableAnnotation(ta1);
      tat1.setOrdre(3);
      tat2 = new TableAnnotationTemplate();
      tat2.setTemplate(t2);
      tat2.setTableAnnotation(ta2);
      tat2.setOrdre(4);
      tables.add(tat1);
      tables.add(tat2);

      templateManager.createObjectManager(t2, b1, e2, blocs, champs, tables);
      assertTrue(templateManager.findAllObjectsManager().size() == 5);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 6);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 6);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 3);
      final Integer idT2 = t2.getTemplateId();

      // Vérification
      final Template tTest2 = templateManager.findByIdManager(idT2);
      assertNotNull(tTest2);
      assertTrue(tTest2.getNom().equals("Fiche imperssion complète"));
      assertNull(tTest2.getDescription());
      assertNull(tTest2.getEnTete());
      assertNull(tTest2.getPiedPage());
      assertNotNull(tTest.getEntite());
      assertNotNull(tTest.getBanque());
      assertTrue(blocImpressionTemplateManager.findByTemplateManager(tTest2).size() == 2);
      assertTrue(champImprimeManager.findByTemplateManager(tTest2).size() == 2);
      assertTrue(tableAnnotationTemplateManager.findByTemplateManager(tTest2).size() == 2);

      templateManager.removeObjectManager(tTest);
      templateManager.removeObjectManager(tTest2);
      assertTrue(templateManager.findAllObjectsManager().size() == 3);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);
   }

   /**
    * Teste la methode createObjectManager. 
    * @throws ParseException 
    */
   @Test
   public void testUpdateObjectManager(){
      final Template t = new Template();
      final Banque b1 = banqueDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final BlocImpression bi1 = blocImpressionDao.findById(1);
      final BlocImpression bi2 = blocImpressionDao.findById(2);
      final BlocImpression bi3 = blocImpressionDao.findById(3);
      final ChampEntite ce1 = champEntiteDao.findById(1);
      final ChampEntite ce2 = champEntiteDao.findById(2);
      final ChampEntite ce3 = champEntiteDao.findById(3);

      List<BlocImpressionTemplate> blocs = new ArrayList<>();
      List<BlocImpressionTemplate> blocsToCreate = new ArrayList<>();
      List<ChampImprime> champs = new ArrayList<>();
      List<ChampImprime> champsToCreate = new ArrayList<>();

      t.setType(BLOC);
      t.setNom("Template de test");
      final TableAnnotation ta1 = tableAnnotationManager.findByNomLikeManager("TABLE_PAT1", true).get(0);
      final TableAnnotation ta2 = tableAnnotationManager.findByNomLikeManager("TABLE_PREL1", true).get(0);
      final TableAnnotation ta3 = tableAnnotationManager.findByNomLikeManager("INCA_ECHAN1", true).get(0);
      List<TableAnnotationTemplate> tables = new ArrayList<>();
      List<TableAnnotationTemplate> tablesToCreate = new ArrayList<>();

      final BlocImpressionTemplate bit1 = new BlocImpressionTemplate();
      bit1.setTemplate(t);
      bit1.setBlocImpression(bi1);
      bit1.setOrdre(1);
      final BlocImpressionTemplate bit2 = new BlocImpressionTemplate();
      bit2.setTemplate(t);
      bit2.setBlocImpression(bi2);
      bit2.setOrdre(2);
      blocs.add(bit1);
      blocs.add(bit2);
      final ChampImprime ci1 = new ChampImprime();
      ci1.setTemplate(t);
      ci1.setChampEntite(ce1);
      ci1.setBlocImpression(bi1);
      ci1.setOrdre(1);
      final ChampImprime ci2 = new ChampImprime();
      ci2.setTemplate(t);
      ci2.setChampEntite(ce2);
      ci2.setBlocImpression(bi1);
      ci2.setOrdre(2);
      champs.add(ci1);
      champs.add(ci2);
      final TableAnnotationTemplate tat1 = new TableAnnotationTemplate();
      tat1.setTemplate(t);
      tat1.setTableAnnotation(ta1);
      tat1.setOrdre(1);
      final TableAnnotationTemplate tat2 = new TableAnnotationTemplate();
      tat2.setTemplate(t);
      tat2.setTableAnnotation(ta2);
      tat2.setOrdre(2);
      tables.add(tat1);
      tables.add(tat2);

      templateManager.createObjectManager(t, b1, e2, blocs, champs, tables);
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 6);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 6);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 3);
      final Integer idT = t.getTemplateId();

      final Template tUp = templateManager.findByIdManager(idT);
      Boolean catched = false;
      // on test l'insertion avec la banque null
      try{
         templateManager.updateObjectManager(tUp, null, e2, null, null, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 4);

      // on test l'insertion avec l'entité null
      try{
         templateManager.updateObjectManager(tUp, b1, null, null, null, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 4);

      // on test l'insertion d'un doublon
      tUp.setNom("Fiche patient");
      try{
         templateManager.updateObjectManager(tUp, b1, e2, null, null, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 4);

      // Test de la validation lors de la création
      t.setNom("Template autre");
      try{
         validationUpdate(tUp);
      }catch(final ParseException e){
         e.printStackTrace();
      }
      assertTrue(templateManager.findAllObjectsManager().size() == 4);

      // on teste la validation des BlocImpressionTemplate
      final BlocImpressionTemplate bit3 = new BlocImpressionTemplate();
      bit3.setTemplate(tUp);
      bit3.setBlocImpression(null);
      blocsToCreate.add(bit3);
      catched = false;
      try{
         templateManager.updateObjectManager(tUp, b1, e2, blocs, blocsToCreate, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 6);

      // on teste la validation des ChampImprimes
      final ChampImprime ci3 = new ChampImprime();
      ci3.setTemplate(tUp);
      ci3.setChampEntite(null);
      ci3.setBlocImpression(bi1);
      champsToCreate.add(ci3);
      catched = false;
      try{
         templateManager.updateObjectManager(tUp, b1, e2, null, null, champs, champsToCreate, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 6);

      // on teste la validation des TableAnnotationTemplate
      final TableAnnotationTemplate tat3 = new TableAnnotationTemplate();
      tat3.setTemplate(tUp);
      tat3.setTableAnnotation(null);
      tat3.setOrdre(4);
      tablesToCreate.add(tat3);
      catched = false;
      try{
         templateManager.updateObjectManager(tUp, b1, e2, null, null, null, null, tables, tablesToCreate);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 3);

      // on test un update en modifiant uniquement le nom
      tUp.setNom("BLABLABLA");
      templateManager.updateObjectManager(tUp, b1, e2, null, null, null, null, null, null);
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 3);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 6);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 6);

      final Template tUp2 = templateManager.findByIdManager(idT);
      assertTrue(tUp2.getNom().equals("BLABLABLA"));
      // on test l'ajout denouveaux éléments.
      bit3.setTemplate(tUp2);
      bit3.setBlocImpression(bi3);
      bit3.setOrdre(3);
      blocs = blocImpressionTemplateManager.findByTemplateManager(tUp2);
      blocsToCreate = new ArrayList<>();
      blocsToCreate.add(bit3);
      ci3.setTemplate(tUp2);
      ci3.setChampEntite(ce3);
      ci3.setBlocImpression(bi1);
      ci3.setOrdre(3);
      champs = champImprimeManager.findByTemplateManager(tUp2);
      champsToCreate = new ArrayList<>();
      champsToCreate.add(ci3);
      tat3.setTemplate(tUp2);
      tat3.setTableAnnotation(ta3);
      tat3.setOrdre(4);
      tables = tableAnnotationTemplateManager.findByTemplateManager(tUp2);
      tablesToCreate = new ArrayList<>();
      tablesToCreate.add(tat3);
      templateManager.updateObjectManager(tUp2, b1, e2, blocs, blocsToCreate, champs, champsToCreate, tables, tablesToCreate);
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 4);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 7);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 7);

      // on test la suppression de certaines assos
      templateManager.updateObjectManager(tUp2, b1, e2, blocs, null, champs, null, tables, null);
      assertTrue(templateManager.findAllObjectsManager().size() == 4);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 3);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 6);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 6);

      templateManager.removeObjectManager(tUp2);
      assertTrue(templateManager.findAllObjectsManager().size() == 3);
      assertTrue(blocImpressionTemplateManager.findAllObjectsManager().size() == 4);
      assertTrue(champImprimeManager.findAllObjectsManager().size() == 4);
      assertTrue(tableAnnotationTemplateManager.findAllObjectsManager().size() == 1);
   }

   /**
    * Test la validation d'un template lors de sa création.
    * @param template à tester.
    * @throws ParseException 
    */
   private void validationInsert(final Template template) throws ParseException{

      final Banque b1 = banqueDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      boolean catchedInsert = false;

      // On teste une insertion avec un attribut nom non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$$¤¤*d", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setNom(emptyValues[i]);
            templateManager.createObjectManager(template, b1, e2, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setNom("TEST");

      // On teste une insertion avec un attribut description non valide
      emptyValues = new String[] {"", "  ", createOverLength(250)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setDescription(emptyValues[i]);
            templateManager.createObjectManager(template, b1, e2, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setDescription("TEST");

      // On teste une insertion avec un attribut encopass non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setEnTete(emptyValues[i]);
            templateManager.createObjectManager(template, b1, e2, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setEnTete(null);

      // On teste une insertion avec un attribut dnldap non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setPiedPage(emptyValues[i]);
            templateManager.createObjectManager(template, b1, e2, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setPiedPage(null);

   }

   /**
    * Test la validation d'un template lors de sa création.
    * @param template à tester.
    * @throws ParseException 
    */
   private void validationUpdate(final Template template) throws ParseException{

      final Banque b1 = banqueDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      boolean catchedInsert = false;

      // On teste un update avec un attribut nom non valide
      String[] emptyValues = new String[] {null, "", "  ", "%$¤¤$*d", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setNom(emptyValues[i]);
            templateManager.updateObjectManager(template, b1, e2, null, null, null, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setNom("TEST");

      // On teste une insertion avec un attribut description non valide
      emptyValues = new String[] {"", "  ", createOverLength(250)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setDescription(emptyValues[i]);
            templateManager.updateObjectManager(template, b1, e2, null, null, null, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setDescription("TEST");

      // On teste une insertion avec un attribut encopass non valide
      emptyValues = new String[] {"", "  ", "%$$*gd¤¤", createOverLength(50)};
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setEnTete(emptyValues[i]);
            templateManager.updateObjectManager(template, b1, e2, null, null, null, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setEnTete(null);

      // On teste une insertion avec un attribut dnldap non valide
      for(int i = 0; i < emptyValues.length; i++){
         catchedInsert = false;
         try{
            template.setPiedPage(emptyValues[i]);
            templateManager.updateObjectManager(template, b1, e2, null, null, null, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catchedInsert = true;
            }
         }
         assertTrue(catchedInsert);
      }
      template.setPiedPage(null);

   }

}
