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
package fr.aphp.tumorotek.manager.test.coeur.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.FichierDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.AnnotationCommonValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Classe de test pour le manager AnnotationValeurManager.
 * Classe créée le 09/02/10.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class AnnotationValeurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private AnnotationValeurManager annotationValeurManager;
   @Autowired
   private AnnotationValeurDao annotationValeurDao;
   @Autowired
   private ChampAnnotationDao champAnnotationDao;
   @Autowired
   private PatientDao patientDao;
   @Autowired
   private PrelevementDao prelevementDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private TableAnnotationDao tableAnnotationDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private AnnotationCommonValidator annotationCommonValidator;
   @Autowired
   private TableAnnotationManager tableAnnotationManager;
   @Autowired
   private FichierDao fichierDao;
   @Autowired
   private ItemDao itemDao;
   @Autowired
   private OperationTypeDao operationTypeDao;
   @Autowired
   @Qualifier("dataSource")
   private DataSource dataSource;

   // @Rule
   // public Timeout globalTimeout =  new Timeout(10000);

   public AnnotationValeurManagerTest(){}

   /**
    * Test la méthode findAllObjectsManager.
    */
   @Test
   public void testFindAllObjectsManager(){
      final List<AnnotationValeur> valeurs = annotationValeurManager.findAllObjectsManager();
      assertTrue(valeurs.size() == 12);
   }

   @Test
   public void testFindByChampAndObjetManager(){
      ChampAnnotation c = champAnnotationDao.findById(1);
      final Patient p1 = patientDao.findById(1);
      List<AnnotationValeur> valeurs = annotationValeurManager.findByChampAndObjetManager(c, p1);
      assertTrue(valeurs.size() == 1);
      c = champAnnotationDao.findById(4);
      valeurs = annotationValeurManager.findByChampAndObjetManager(c, p1);
      assertTrue(valeurs.size() == 0);
      final Echantillon e = echantillonDao.findById(1);
      valeurs = annotationValeurManager.findByChampAndObjetManager(c, e);
      assertTrue(valeurs.size() == 1);
      valeurs = annotationValeurManager.findByChampAndObjetManager(c, null);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurManager.findByChampAndObjetManager(null, p1);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurManager.findByChampAndObjetManager(null, null);
      assertTrue(valeurs.size() == 0);
   }

   @Test
   public void testFindByTableAndBanqueManager(){
      TableAnnotation t = tableAnnotationDao.findById(3);
      Banque b = banqueDao.findById(1);
      List<AnnotationValeur> valeurs = annotationValeurManager.findByTableAndBanqueManager(t, b);
      assertTrue(valeurs.size() == 3);
      b = banqueDao.findById(2);
      valeurs = annotationValeurManager.findByTableAndBanqueManager(t, b);
      assertTrue(valeurs.size() == 2);
      t = tableAnnotationDao.findById(1);
      valeurs = annotationValeurManager.findByTableAndBanqueManager(t, b);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurManager.findByTableAndBanqueManager(t, null);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurManager.findByTableAndBanqueManager(null, b);
      assertTrue(valeurs.size() == 0);
      valeurs = annotationValeurManager.findByTableAndBanqueManager(null, null);
      assertTrue(valeurs.size() == 0);
   }

   @Test
   public void testFindByObjectManager(){
      List<AnnotationValeur> vals = annotationValeurManager.findByObjectManager(echantillonDao.findById(1));
      assertTrue(vals.size() == 6);

      vals = annotationValeurManager.findByObjectManager(patientDao.findById(1));
      assertTrue(vals.size() == 1);

      vals = annotationValeurManager.findByObjectManager(echantillonDao.findById(3));
      assertTrue(vals.size() == 0);

      vals = annotationValeurManager.findByObjectManager(null);
      assertTrue(vals.size() == 0);
   }

   @Test
   public void testFindDoublonManager(){
      //Cree le doublon
      final AnnotationValeur av1 = annotationValeurDao.findById(8);
      final AnnotationValeur av2 = new AnnotationValeur();
      av2.setObjetId(av1.getObjetId());
      av2.setChampAnnotation(av1.getChampAnnotation());
      av2.setItem(av1.getItem());
      av2.setBanque(av1.getBanque());
      assertTrue(av2.equals(av1));
      assertTrue(annotationValeurManager.findDoublonManager(av2));
   }

   @Test
   public void testCRUD(){
      createObjectManagerTest();
      updateObjectManagerTest();
      removeObjectManagerTest();
   }

   public void createObjectManagerTest(){

      final AnnotationValeur valeur = new AnnotationValeur();
      final Patient pat = patientDao.findById(1);
      final Prelevement prel = prelevementDao.findById(1);
      final ChampAnnotation c = champAnnotationDao.findById(2);
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(2);

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      //insertion
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, null, null, null, null, u, null, null, filesCreated,
            filesToDelete);
      }catch(final NullPointerException ne){
         assertTrue(ne.getMessage().equals("operation cannot be " + "set to null for createorUpdateMethod"));
      }
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, null, null, null, null, u, "op", null, filesCreated,
            filesToDelete);
      }catch(final RequiredObjectIsNullException re){
         assertTrue(re.getRequiredObject().equals("ChampAnnotation"));
      }
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, null, null, null, u, "op", null, filesCreated,
            filesToDelete);
      }catch(final RequiredObjectIsNullException re){
         assertTrue(re.getRequiredObject().equals("TKAnnotableObject"));
      }
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, null, null, u, "op", null, filesCreated,
            filesToDelete);
      }catch(final RequiredObjectIsNullException re){
         assertTrue(re.getRequiredObject().equals("Banque"));
      }
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, b, null, u, "op", null, filesCreated, filesToDelete);
      }catch(final RuntimeException rte){
         assertTrue(true);
      }
      valeur.setAlphanum("loos#¤¤");
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, prel, b, null, u, "creation", null, filesCreated,
            filesToDelete);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.alphanum.illegal"));
      }
      valeur.setAlphanum("winner");
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, prel, b, null, u, "op", null, filesCreated,
            filesToDelete);
      }catch(final IllegalArgumentException ie){
         assertTrue(ie.getMessage().equals("Operation must match " + "'creation/modification' values"));
      }
      annotationValeurManager.createOrUpdateObjectManager(valeur, c, prel, b, null, u, "creation", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(valeur).size() == 1);
      assertTrue(getOperationManager().findByObjectManager(valeur).get(0).getOperationType().getNom().equals("Creation"));

      //Insertion d'un doublon engendrant une exception
      final AnnotationValeur v2 = new AnnotationValeur();
      v2.setAlphanum("doublon");
      boolean catched = false;
      try{
         annotationValeurManager.createOrUpdateObjectManager(v2, c, prel, b, null, u, "creation", "/tmp/", filesCreated,
            filesToDelete);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).get(0).getAlphanum().equals("winner"));
      assertTrue(getOperationManager().findByObjectManager(valeur).size() == 1);
   }

   public void updateObjectManagerTest(){
      final Prelevement prel = prelevementDao.findById(1);
      final ChampAnnotation c = champAnnotationDao.findById(2);
      final AnnotationValeur valeur = annotationValeurManager.findByChampAndObjetManager(c, prel).get(0);
      valeur.setAlphanum("new Alpha");
      final Utilisateur u = utilisateurDao.findById(2);

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      annotationValeurManager.createOrUpdateObjectManager(valeur, null, null, null, null, u, "modification", "/tmp/",
         filesCreated, filesToDelete);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).size() == 1);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).get(0).getAlphanum().equals("new Alpha"));
      assertTrue(getOperationManager().findByObjectManager(valeur).size() == 2);
      assertTrue(getOperationManager()
         .findByObjetIdEntiteAndOpeTypeManager(valeur, operationTypeDao.findByNom("Modification").get(0)).size() == 1);
   }

   /**
    * Teste la methode removeObjectManager. 
    */
   public void removeObjectManagerTest(){
      final List<File> filesToDelete = new ArrayList<>();
      final Prelevement prel = prelevementDao.findById(1);
      final ChampAnnotation c = champAnnotationDao.findById(2);
      final AnnotationValeur valeur = annotationValeurManager.findByChampAndObjetManager(c, prel).get(0);
      annotationValeurManager.removeObjectManager(valeur, filesToDelete);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, prel).size() == 0);
      assertTrue(getOperationManager().findByObjectManager(valeur).size() == 0);
      annotationValeurManager.removeObjectManager(null, filesToDelete);
      //verifie que l'etat des tables modifies est revenu identique
      assertEquals(19, getOperationManager().findAllObjectsManager().size());
      testFindAllObjectsManager();
   }

   @Test
   public void testAnnotationCommonValidation() throws ParseException{
      final AnnotationValeur v = new AnnotationValeur();

      // champAnnotation null
      v.setAlphanum("");
      try{
         BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.alphanum.empty"));
      }

      v.setAlphanum(createOverLength(100));
      try{
         BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.alphanum.tooLong"));
      }

      v.setAlphanum(null);
      v.setTexte("");
      try{
         BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.texte.empty"));
      }
      v.setTexte("Somewhere over the rainbow!");
      BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});

      // champAnnotation alphanum
      ChampAnnotation chp = champAnnotationDao.findById(1);
      v.setChampAnnotation(chp);
      v.setAlphanum("##'");
      try{
         BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.alphanum.illegal"));
      }
      v.setAlphanum("ok");
      BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});

      // champAnnotation num
      chp = champAnnotationDao.findById(7);
      v.setChampAnnotation(chp);
      v.setAlphanum("a");
      try{
         BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.num.illegal"));
      }

      // champAnnotation hyperlien
      chp = champAnnotationDao.findById(15);
      v.setChampAnnotation(chp);
      v.setAlphanum("http://zz&é@&((");
      try{
         BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().get(0).getFieldError().getCode().equals("anno.hyperlien.illegal"));
      }

      v.setAlphanum("125");
      BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
      v.setAlphanum("system.tk.unknownExistingValue");
      BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});

      // champAnnotation thes
      chp = champAnnotationDao.findById(12);
      v.setChampAnnotation(chp);
      v.setAlphanum("##");
      BeanValidator.validateObject(v, new Validator[] {annotationCommonValidator});
   }

   /**
    * Teste la cascade de deletion d'une reference vers banques 
    * vers les valeurs d'annotations.
    */
   @Test
   public void testRemoveValeurWithBanqueRelation(){
      final TableAnnotation t1 = tableAnnotationDao.findById(1);
      final ChampAnnotation alpha = champAnnotationDao.findById(1);
      final ChampAnnotation file1 = champAnnotationDao.findById(13);
      final Patient p1 = patientDao.findById(1);
      final Patient p2 = patientDao.findById(2);

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      // ajout reference table 1 vers banque 2
      List<Banque> banks = new ArrayList<>(tableAnnotationManager.getBanquesManager(t1));
      final Banque b2 = banqueDao.findById(2);
      banks.add(b2);
      final Utilisateur u = utilisateurDao.findById(2);
      tableAnnotationManager.createOrUpdateObjectManager(t1, null, null, null, banks, null, u, "modification", "/tmp/", null);
      assertTrue(tableAnnotationManager.getBanquesManager(t1).size() == 2);

      // creation de 4 valeurs annotations
      final AnnotationValeur p1alpha = new AnnotationValeur();
      p1alpha.setAlphanum("p1lapha");
      annotationValeurManager.createOrUpdateObjectManager(p1alpha, alpha, p1, b2, null, u, "creation", "/tmp/", filesCreated,
         filesToDelete);

      final AnnotationValeur p2alpha = new AnnotationValeur();
      p2alpha.setAlphanum("p2lapha");
      annotationValeurManager.createOrUpdateObjectManager(p2alpha, alpha, p2, b2, null, u, "creation", "/tmp/", filesCreated,
         filesToDelete);

      final AnnotationValeur p1file1 = new AnnotationValeur();
      p1file1.setAlphanum("p1file1");
      annotationValeurManager.createOrUpdateObjectManager(p1file1, file1, p1, b2, null, u, "creation", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(filesCreated.isEmpty());

      final AnnotationValeur p2file1 = new AnnotationValeur();
      p2file1.setAlphanum("p2file1");
      annotationValeurManager.createOrUpdateObjectManager(p2file1, file1, p2, b2, null, u, "creation", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(filesCreated.isEmpty());

      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 16);

      // suppression de la reference entre la table 1 vers la banque 2
      banks = new ArrayList<>(tableAnnotationManager.getBanquesManager(t1));
      banks.remove(b2);
      tableAnnotationManager.createOrUpdateObjectManager(t1, null, null, null, banks, null, u, "modification", "/tmp/", null);
      assertTrue(tableAnnotationManager.getBanquesManager(t1).size() == 1);
      assertTrue(annotationValeurManager.findByTableAndBanqueManager(t1, b2).size() == 0);

      //verifie que l'etat des tables modifies est revenu identique
      testFindAllObjectsManager();

      // supprime les deux operations de modifications pour la table
      CreateOrUpdateUtilities.removeAssociateOperations(t1, getOperationManager());
      assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
   }

   @Test
   public void testStoreFileAnno(){
      final AnnotationValeur valeur = new AnnotationValeur();
      final Patient pat = patientDao.findById(2);
      final ChampAnnotation c = champAnnotationDao.findById(13);
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(2);

      final Integer totFichier = fichierDao.findAll().size();

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      // creation architecture fichiers
      final File baseDir = new File(Utils.writeAnnoFilePath("/tmp/", b, c, null));
      baseDir.mkdirs();

      Fichier f = new Fichier();
      String tmp = "abcdefghijklmnopqrstuvwxyz";
      byte[] byteArray = tmp.getBytes();
      ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
      f.setNom("*$$$!&&");
      valeur.setStream(bais);
      // teste validation Excpetion -> pas de fichier cree	
      boolean catched = false;
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, b, f, u, "creation", "/tmp/", filesCreated,
            filesToDelete);
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, c, f)).exists());

      f.setNom("fichierTest");
      // teste mauvais path	
      catched = false;
      try{
         annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, b, f, u, "creation", "rm *", filesCreated,
            filesToDelete);
      }catch(final RuntimeException re){
         catched = true;
      }
      assertTrue(catched);
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, c, f)).exists());

      assertTrue(filesCreated.isEmpty());
      assertTrue(fichierDao.findAll().size() == totFichier);
      assertTrue(baseDir.listFiles().length == 0);

      f.setPath(null);

      annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, b, f, u, "creation", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, pat).size() == 1);
      assertTrue(fichierDao.findById(f.getFichierId()).getNom().equals("fichierTest"));
      assertTrue(annotationValeurManager.findByChampAndObjetManager(c, pat).get(0).getFichier().equals(f));
      final String path = f.getPath();
      assertTrue(new File(path).length() == 26);
      assertTrue(f.getPath().equals(Utils.writeAnnoFilePath("/tmp/", b, c, f) + "_" + f.getFichierId()));
      assertTrue(fichierDao.findAll().size() == totFichier + 1);
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);
      assertTrue(baseDir.listFiles().length == 1);

      // modification du nom fichier
      f = valeur.getFichier().clone();
      f.setNom("heroes");
      valeur.setStream(null);
      annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, b, f, u, "modification", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(fichierDao.findById(f.getFichierId()).getNom().equals("heroes"));
      assertTrue(fichierDao.findAll().size() == totFichier + 1);
      assertTrue(f.getPath().equals(path));
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 0);
      assertTrue(baseDir.listFiles().length == 1);
      filesCreated.clear();

      // modification du contenu
      f = valeur.getFichier().clone();
      f.setNom("curtis_mayfield");
      tmp = "curtis mayfield";
      byteArray = tmp.getBytes();
      bais = new ByteArrayInputStream(byteArray);
      valeur.setStream(bais);
      annotationValeurManager.createOrUpdateObjectManager(valeur, c, pat, b, f, u, "modification", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(fichierDao.findAll().size() == totFichier + 1);
      f = valeur.getFichier();
      assertTrue(f.getNom().equals("curtis_mayfield"));
      assertTrue(new File(f.getPath()).length() == 15);
      assertFalse(f.getPath().equals(path));
      assertTrue(filesCreated.size() == 1);
      assertTrue(filesToDelete.size() == 1);
      for(final File fl : filesToDelete){
         fl.delete();
      }
      assertTrue(baseDir.listFiles().length == 1);

      // nettoyage
      annotationValeurManager.removeObjectManager(valeur, null);
      testFindAllObjectsManager();
      assertTrue(fichierDao.findAll().size() == totFichier);
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, c, f)).exists());
      assertTrue(baseDir.listFiles().length == 0);
      // nettoyage architecture 
      try{
         FileUtils.deleteDirectory(new File("/tmp/pt_1"));
         Thread.sleep(500);
         assertFalse(new File("/tmp/pt_1").exists());
      }catch(final IOException e){
         assertFalse(true);
      }catch(final InterruptedException e){
         assertFalse(true);
         // e.printStackTrace();
      }
      assertFalse(new File("/tmp/pt_1").exists());
   }

   /**
    * Teste la creation et la suppression de liste d'AnnotationValeur.
    */
   @Test
   public void testCreateAnnotationValeurListManager(){

      final Echantillon obj = echantillonDao.findById(4);
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(1);

      List<AnnotationValeur> valeurs = new ArrayList<>();
      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      final AnnotationValeur bool2 = new AnnotationValeur();
      bool2.setChampAnnotation(champAnnotationDao.findById(4));
      bool2.setBool(true);
      bool2.setBanque(b);
      valeurs.add(bool2);
      final AnnotationValeur num1 = new AnnotationValeur();
      num1.setAlphanum("##&&$");
      num1.setChampAnnotation(champAnnotationDao.findById(7));
      num1.setBanque(b);
      valeurs.add(num1);
      final AnnotationValeur thes1 = new AnnotationValeur();
      thes1.setChampAnnotation(champAnnotationDao.findById(12));
      thes1.setItem(itemDao.findById(4));
      thes1.setBanque(b);
      valeurs.add(thes1);

      // teste la creation avec rollback
      try{
         annotationValeurManager.createAnnotationValeurListManager(valeurs, obj, u, "creation", "/tmp/", filesCreated,
            filesToDelete);
      }catch(final ValidationException ve){
         testFindAllObjectsManager();
      }
      // creation réussie
      num1.setAlphanum("12");
      valeurs = annotationValeurManager.createAnnotationValeurListManager(valeurs, obj, u, "creation", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 15);
      assertTrue(filesCreated.isEmpty());
      assertTrue(filesToDelete.isEmpty());

      // update -> implique recuperation AnnotationValeur avec son Id!
      //		AnnotationValeur boolUp = annotationValeurManager
      //				.findByChampAndObjetManager(champAnnotationDao.findById(4), obj)
      //														.get(0);
      //		boolUp.setBool(false);
      //		AnnotationValeur numUp = annotationValeurManager
      //		.findByChampAndObjetManager(champAnnotationDao.findById(7), obj)
      //												.get(0);
      //		numUp.setAlphanum("13");
      //		valeurs.clear();
      //		valeurs.add(boolUp); valeurs.add(numUp);
      valeurs.get(0).setBool(false);
      valeurs.get(1).setAlphanum("13");
      annotationValeurManager.createAnnotationValeurListManager(valeurs, obj, u, "modification", "/tmp/", filesCreated,
         filesToDelete);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == 15);
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(4), obj).get(0).getBool());
      assertFalse(annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), obj).get(0).getAlphanum()
         .equals("13.0"));
      assertTrue(filesCreated.isEmpty());
      assertTrue(filesToDelete.isEmpty());

      // clean up
      //		valeurs.add(annotationValeurManager
      //			.findByChampAndObjetManager(champAnnotationDao.findById(12), obj)
      //				.get(0));
      valeurs.add(thes1);
      annotationValeurManager.removeAnnotationValeurListManager(valeurs, null);
      final List<Operation> ops = getOperationManager().findByObjectManager(obj);
      for(int i = 0; i < ops.size(); i++){
         if(ops.get(i).getOperationType().getNom().equals("Annotation")){
            getOperationManager().removeObjectManager(ops.get(i));
         }
      }
   }

   @Test
   public void testFindByIdManager(){
      final AnnotationValeur val1 = annotationValeurManager.findByIdManager(1);
      assertNotNull(val1);
      assertTrue(val1.getAlphanum().equals("AlphanumValue1"));

      final AnnotationValeur valNull = annotationValeurManager.findByIdManager(20);
      assertNull(valNull);
   }

   @Test
   public void testGetValueForAnnotationValeur(){
      // alphanum
      final AnnotationValeur val1 = annotationValeurManager.findByIdManager(1);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val1).equals(val1.getAlphanum()));

      // boolean
      final AnnotationValeur val4 = annotationValeurManager.findByIdManager(4);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val4).equals(val4.getBool()));

      // date
      final AnnotationValeur val5 = annotationValeurManager.findByIdManager(5);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val5).equals(val5.getDate()));

      // texte
      final AnnotationValeur val7 = annotationValeurManager.findByIdManager(7);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val7).equals(val7.getTexte()));

      // thes
      final AnnotationValeur val8 = annotationValeurManager.findByIdManager(8);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val8).equals(val8.getItem()));

      // thes2
      final AnnotationValeur val9 = annotationValeurManager.findByIdManager(9);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val9).equals(val9.getItem()));

      // file
      final AnnotationValeur val10 = annotationValeurManager.findByIdManager(10);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val10).equals(val10.getFichier()));

      // link
      final AnnotationValeur val11 = annotationValeurManager.findByIdManager(11);
      assertTrue(annotationValeurManager.getValueForAnnotationValeur(val11).equals(val11.getAlphanum()));

      assertNull(annotationValeurManager.getValueForAnnotationValeur(new AnnotationValeur()));

      assertNull(annotationValeurManager.getValueForAnnotationValeur(null));
   }

   @Test
   public void testPrepareAnnotationValeurListJDBCManager() throws SQLException{

      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      final EchantillonJdbcSuite suite = new EchantillonJdbcSuite();

      try{
         conn = DataSourceUtils.getConnection(dataSource);
         stmt = conn.createStatement();
         rs = stmt.executeQuery("select max(annotation_valeur_id)" + " from ANNOTATION_VALEUR");
         rs.first();
         final Integer maxAnnoId = rs.getInt(1);
         suite.setMaxAnnotationValeurId(maxAnnoId);

         final String sql = "insert into ANNOTATION_VALEUR (ANNOTATION_VALEUR_ID, " + "CHAMP_ANNOTATION_ID, OBJET_ID, BANQUE_ID, "
            + "ALPHANUM, BOOL, ANNO_DATE, " + "TEXTE, ITEM_ID) " + "values (?,?,?,?,?,?,?,?,?)";
         suite.setPstmtAnno(conn.prepareStatement(sql));

         final String sql2 = "insert into OPERATION (UTILISATEUR_ID, " + "OBJET_ID, ENTITE_ID, OPERATION_TYPE_ID, " + "DATE_, V1)"
            + "values (?,?,?,?,?,?)";
         suite.setPstmtOp(conn.prepareStatement(sql2));

         final Echantillon obj = echantillonDao.findById(4);
         final Banque b = banqueDao.findById(1);
         final Utilisateur u = utilisateurDao.findById(1);

         final List<AnnotationValeur> valeurs = new ArrayList<>();

         final AnnotationValeur bool2 = new AnnotationValeur();
         bool2.setChampAnnotation(champAnnotationDao.findById(4));
         bool2.setBool(true);
         bool2.setBanque(b);
         valeurs.add(bool2);
         final AnnotationValeur num1 = new AnnotationValeur();
         num1.setAlphanum("##&&$");
         num1.setChampAnnotation(champAnnotationDao.findById(7));
         num1.setBanque(b);
         valeurs.add(num1);
         final AnnotationValeur thes1 = new AnnotationValeur();
         thes1.setChampAnnotation(champAnnotationDao.findById(12));
         thes1.setItem(itemDao.findById(4));
         thes1.setBanque(b);
         valeurs.add(thes1);
         final AnnotationValeur texte1 = new AnnotationValeur();
         texte1.setChampAnnotation(champAnnotationDao.findById(9));
         texte1.setTexte("bob l'éponge le film");
         texte1.setBanque(b);
         valeurs.add(texte1);
         final AnnotationValeur date1 = new AnnotationValeur();
         date1.setChampAnnotation(champAnnotationDao.findById(5));
         final Calendar cal = Calendar.getInstance();
         cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("12/08/2014"));
         date1.setDate(cal);
         date1.setBanque(b);
         valeurs.add(date1);

         // teste la creation avec rollback
         boolean catched = false;
         try{
            annotationValeurManager.prepareAnnotationValeurListJDBCManager(suite, valeurs, obj, u);
         }catch(final ValidationException ve){
            catched = true;
         }
         assertTrue(catched);

         num1.setAlphanum("12");
         // teste la creation avec doublon
         catched = false;
         try{
            annotationValeurManager.prepareAnnotationValeurListJDBCManager(suite, valeurs, obj, u);
         }catch(final DoublonFoundException de){
            catched = true;
         }
         assertTrue(catched);

         testFindAllObjectsManager();

         // id n'a pas changé
         assertTrue(suite.getMaxAnnotationValeurId().equals(maxAnnoId));

         suite.getPstmtAnno().clearBatch();
         suite.getPstmtOp().clearBatch();

         date1.setChampAnnotation(champAnnotationDao.findById(6));
         // creation réussie
         annotationValeurManager.prepareAnnotationValeurListJDBCManager(suite, valeurs, obj, u);
         suite.getPstmtAnno().executeBatch();
         suite.getPstmtOp().executeBatch();

         // id a changé
         assertTrue(suite.getMaxAnnotationValeurId().equals(maxAnnoId + 5));

         // asserts
         assertTrue(annotationValeurManager.findByObjectManager(obj).size() == 7);

         final AnnotationValeur av1 =
            annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(4), obj).get(0);
         assertTrue(av1.getBool());
         assertTrue(getOperationManager().findByObjectManager(av1).get(0).getOperationType().getNom().equals("Creation"));

         final AnnotationValeur av2 =
            annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(7), obj).get(0);
         assertTrue(av2.getAlphanum().equals("12"));
         assertTrue(getOperationManager().findByObjectManager(av2).get(0).getOperationType().getNom().equals("Creation"));

         final AnnotationValeur av3 =
            annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(12), obj).get(0);
         assertTrue(av3.getItem().equals(itemDao.findById(4)));
         assertTrue(getOperationManager().findByObjectManager(av3).get(0).getOperationType().getNom().equals("Creation"));

         final AnnotationValeur av4 =
            annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(9), obj).get(0);
         assertTrue(av4.getTexte().equals("bob l'éponge le film"));
         assertTrue(getOperationManager().findByObjectManager(av4).get(0).getOperationType().getNom().equals("Creation"));

         final AnnotationValeur av5 =
            annotationValeurManager.findByChampAndObjetManager(champAnnotationDao.findById(6), obj).get(0);
         assertTrue(av5.getDate().equals(cal));
         assertTrue(getOperationManager().findByObjectManager(av5).get(0).getOperationType().getNom().equals("Creation"));

         // clean up
         final List<AnnotationValeur> avs = new ArrayList<>();
         avs.add(av1);
         avs.add(av2);
         avs.add(av3);
         avs.add(av4);
         avs.add(av5);
         annotationValeurManager.removeAnnotationValeurListManager(avs, null);
         final List<Operation> ops = getOperationManager().findByObjectManager(obj);
         for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).getOperationType().getNom().equals("Annotation")){
               getOperationManager().removeObjectManager(ops.get(i));
            }
         }

         cleanUpFantomes(null);
      }catch(final Exception e){
         // e.printStackTrace();
      }finally{
         if(null != stmt)
            stmt.close();
         if(null != rs)
            rs.close();
         if(null != conn)
            conn.close();
         suite.closePs();
      }
   }

   // @Test(timeout = 600)
   public void testCreateFileBatchForTKObjectsManager(){

      final List<TKAnnotableObject> objs = new ArrayList<>();
      objs.add(patientDao.findById(2));
      objs.add(patientDao.findById(1));
      objs.add(patientDao.findById(3));
      final ChampAnnotation c = champAnnotationDao.findById(13);
      final Banque b = banqueDao.findById(1);
      final Utilisateur u = utilisateurDao.findById(2);

      final Integer totFichier = fichierDao.findAll().size();
      final Integer totValeurs = annotationValeurDao.findAll().size();

      final List<File> filesCreated = new ArrayList<>();

      // creation architecture fichiers
      final File baseDir = new File(Utils.writeAnnoFilePath("/tmp/", b, c, null));
      baseDir.mkdirs();

      final Fichier f = new Fichier();
      final String tmp = "abcdefghijklmnopqrstuvwxyz";
      final byte[] byteArray = tmp.getBytes();
      final InputStream bais = new ByteArrayInputStream(byteArray);
      f.setNom("*$$$!&&");
      // teste validation Excpetion -> pas de fichier cree	
      boolean catched = false;
      try{
         annotationValeurManager.createFileBatchForTKObjectsManager(objs, f, bais, c, b, u, "/tmp/", filesCreated);
      }catch(final ValidationException e){
         catched = true;
      }
      assertTrue(catched);
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, c, f)).exists());
      assertTrue(filesCreated.isEmpty());
      assertTrue(annotationValeurDao.findAll().size() == totValeurs);
      assertTrue(fichierDao.findAll().size() == totFichier);

      f.setNom("fichierTest");
      // teste mauvais path	
      catched = false;
      try{
         annotationValeurManager.createFileBatchForTKObjectsManager(objs, f, bais, c, b, u, "/rm *", filesCreated);
      }catch(final RuntimeException re){
         catched = true;
      }
      assertTrue(catched);
      assertFalse(new File(Utils.writeAnnoFilePath("/tmp/", b, c, f)).exists());

      assertTrue(filesCreated.isEmpty());
      assertTrue(fichierDao.findAll().size() == totFichier);
      assertTrue(annotationValeurDao.findAll().size() == totValeurs);
      assertTrue(baseDir.listFiles().length == 0);

      f.setPath(null);

      // null tests
      annotationValeurManager.createFileBatchForTKObjectsManager(null, f, bais, c, b, u, "/tmp/", filesCreated);
      annotationValeurManager.createFileBatchForTKObjectsManager(objs, null, bais, c, b, u, "/tmp/", filesCreated);
      annotationValeurManager.createFileBatchForTKObjectsManager(objs, f, null, c, b, u, "/tmp/", filesCreated);

      assertTrue(filesCreated.isEmpty());
      assertTrue(fichierDao.findAll().size() == totFichier);
      assertTrue(annotationValeurDao.findAll().size() == totValeurs);
      assertTrue(baseDir.listFiles().length == 0);

      annotationValeurManager.createFileBatchForTKObjectsManager(objs, f, bais, c, b, u, "/tmp/", filesCreated);

      assertTrue(filesCreated.size() == 1);
      assertTrue(fichierDao.findAll().size() == totFichier + 3);
      assertTrue(annotationValeurDao.findAll().size() == totValeurs + 3);
      assertTrue(baseDir.listFiles().length == 1);

      final Fichier f1 = annotationValeurManager.findByChampAndObjetManager(c, patientDao.findById(1)).get(0).getFichier();
      final Fichier f2 = annotationValeurManager.findByChampAndObjetManager(c, patientDao.findById(2)).get(0).getFichier();
      final Fichier f3 = annotationValeurManager.findByChampAndObjetManager(c, patientDao.findById(3)).get(0).getFichier();

      assertTrue(f1.getNom().equals("fichierTest"));
      assertTrue(f2.getNom().equals("fichierTest"));
      assertTrue(f3.getNom().equals("fichierTest"));
      assertTrue(f1.getPath().equals(f2.getPath()) && f2.getPath().equals(f3.getPath()));
      assertTrue(f1.getMimeType().equals(f2.getMimeType()) && f2.getMimeType().equals(f3.getMimeType()));

      assertTrue(f1.getPath().equals(Utils.writeAnnoFilePath("/tmp/", b, c, f) + "_" + f2.getFichierId()));
      assertTrue(f2.getPath().equals(Utils.writeAnnoFilePath("/tmp/", b, c, f) + "_" + f2.getFichierId()));
      assertTrue(f3.getPath().equals(Utils.writeAnnoFilePath("/tmp/", b, c, f) + "_" + f2.getFichierId()));

      // nettoyage
      annotationValeurManager
         .removeObjectManager(annotationValeurManager.findByChampAndObjetManager(c, patientDao.findById(1)).get(0), null);
      assertTrue(fichierDao.findAll().size() == totFichier + 2);
      assertTrue(annotationValeurDao.findAll().size() == totValeurs + 2);
      assertTrue(baseDir.listFiles().length == 1);
      assertTrue(new File(f1.getPath()).exists());

      annotationValeurManager
         .removeObjectManager(annotationValeurManager.findByChampAndObjetManager(c, patientDao.findById(2)).get(0), null);
      assertTrue(fichierDao.findAll().size() == totFichier + 1);
      assertTrue(annotationValeurDao.findAll().size() == totValeurs + 1);
      assertTrue(baseDir.listFiles().length == 1);
      assertTrue(new File(f2.getPath()).exists());

      annotationValeurManager
         .removeObjectManager(annotationValeurManager.findByChampAndObjetManager(c, patientDao.findById(3)).get(0), null);
      assertTrue(fichierDao.findAll().size() == totFichier);
      assertTrue(annotationValeurDao.findAll().size() == totValeurs);
      assertTrue(baseDir.listFiles().length == 0);
      assertFalse(new File(f3.getPath()).exists());
      assertFalse(new File(f1.getPath()).exists());

      testFindAllObjectsManager();

      // nettoyage architecture 
      try{
         Runtime.getRuntime().exec("rm -rf /tmp/pt_1");
         Thread.sleep(500);
         assertFalse(new File("/tmp/pt_1").exists());
      }catch(final IOException e){
         assertFalse(true);
      }catch(final InterruptedException e){
         assertFalse(true);
         //e.printStackTrace();
      }
      assertFalse(new File("/tmp/pt_1").exists());
   }

   @Test
   public void testFindCountByTableAnnotationBanqueManager(){
      final TableAnnotation t3 = tableAnnotationDao.findById(3);
      final Banque b1 = banqueDao.findById(1);
      Long count = annotationValeurManager.findCountByTableAnnotationBanqueManager(t3, b1);
      assertTrue(count == 3);
      final Banque b2 = banqueDao.findById(2);
      count = annotationValeurManager.findCountByTableAnnotationBanqueManager(t3, b2);
      assertTrue(count == 2);
      final TableAnnotation t5 = tableAnnotationDao.findById(5);
      count = annotationValeurManager.findCountByTableAnnotationBanqueManager(t5, b2);
      assertTrue(count == 0);
      count = annotationValeurManager.findCountByTableAnnotationBanqueManager(t5, b1);
      assertTrue(count == 3);
      count = annotationValeurManager.findCountByTableAnnotationBanqueManager(t5, null);
      assertNull(count);
      count = annotationValeurManager.findCountByTableAnnotationBanqueManager(null, b1);
      assertNull(count);

   }
}
