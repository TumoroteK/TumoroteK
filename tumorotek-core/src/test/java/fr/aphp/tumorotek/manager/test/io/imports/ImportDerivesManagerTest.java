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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.exception.*;
import fr.aphp.tumorotek.manager.io.imports.*;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

import static org.junit.Assert.*;

public class ImportDerivesManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ImportTemplateManager importTemplateManager;
   @Autowired
   private ImportColonneManager importColonneManager;
   @Autowired
   private ImportHistoriqueManager importHistoriqueManager;
   @Autowired
   private ImportManager importManager;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private PatientManager patientManager;
   @Autowired
   private MaladieManager maladieManager;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private ChampAnnotationManager champAnnotationManager;
   @Autowired
   private AnnotationValeurManager annotationValeurManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private EmplacementManager emplacementManager;
   @Autowired
   private ObjetStatutDao objetStatutDao;
   @Autowired
   private NonConformiteDao nonConformiteDao;
   @Autowired
   private ObjetNonConformeDao objetNonConformeDao;
   @Autowired
   @Qualifier("dataSource")
   private DataSource dataSource;
   @Autowired
   private TransformationManager transformationManager;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private OperationTypeDao operationTypeDao;
   @Autowired
   private RetourManager retourManager;

   // private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

   public ImportDerivesManagerTest(){}

   @Test
   public void testImportDerivesBatchesFile() throws IOException{
      final ImportTemplate template = importTemplateManager.findByIdManager(1);
      final Utilisateur u = utilisateurDao.findById(1);
      ImportHistorique ih = null;

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/test/io/imports/importDerivesBatches.xls");

      template.setIsUpdate(false);

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file)){
         ih = importManager.importFileManager(template, u, fis);
      }catch(final FileNotFoundException e){
         e.printStackTrace();
      }catch(final RuntimeException re){
         errors = ((ErrorsInImportException) re).getErrors();
         errors.get(0).getException().printStackTrace();
      }

      assertTrue(errors.size() == 0);
      assertTrue(patientManager.findAllObjectsManager().size() == 6);
      assertTrue(patientManager.findByNomLikeManager("MEUNIER", true).size() == 1);
      final Patient pat = patientManager.findByNomLikeManager("MEUNIER", true).get(0);
      assertTrue(maladieManager.findAllObjectsManager().size() == 7);
      assertTrue(maladieManager.findByLibelleLikeManager("TUMEUR", true).size() == 1);
      final Maladie mal = maladieManager.findByLibelleLikeManager("TUMEUR", true).get(0);
      assertTrue(mal.getPatient().equals(pat));
      assertTrue(prelevementManager.findAllObjectsManager().size() == 6);
      assertTrue(prelevementManager.findByCodeLikeManager("PRLVT456", true).size() == 1);
      final Prelevement prel = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
      assertTrue(prel.getMaladie().equals(mal));
      assertTrue(echantillonManager.findAllObjectsManager().size() == 5);
      assertTrue(echantillonManager.findByCodeLikeManager("PRLVT456.1", true).size() == 1);
      final Echantillon ech = echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0);
      assertTrue(echantillonManager.getPrelevementManager(ech).equals(prel));
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 13);
      assertTrue(transformationManager.findAllObjectsManager().size() == 7);
      final Transformation t1 = transformationManager.findByEntiteObjetManager(entiteDao.findById(3),
         echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0).getEchantillonId()).get(0);
      assertTrue(prodDeriveManager.findByTransformationManager(t1).size() == 6);
      final Transformation t2 = transformationManager.findByEntiteObjetManager(entiteDao.findById(3),
         echantillonManager.findByCodeLikeManager("PTRA.2", true).get(0).getEchantillonId()).get(0);
      assertTrue(prodDeriveManager.findByTransformationManager(t2).size() == 3);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 15);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 4);
      assertNotNull(ih);
      assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ih).size() == 12);

      // restaure le statut du template
      template.setIsUpdate(true);
      importTemplateManager.updateObjectManager(template, template.getBanque(), null, null, null);

      // Removes
      importHistoriqueManager.removeObjectManager(ih);
      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);

      // clean up
      final List<TKFantomableObject> fs = new ArrayList<>();

      final List<ProdDerive> derives = prodDeriveManager.findByCodeLikeManager("BATCH", false);
      fs.addAll(derives);
      for(final ProdDerive prodDerive : derives){
         prodDeriveManager.removeObjectManager(prodDerive, null, u, null);
      }

      transformationManager.removeObjectManager(t2, null, u);

      final Emplacement e10 = echantillonManager.getEmplacementManager(ech);
      assertTrue(e10 != null);
      assertTrue(e10.getPosition() == 21);

      //	e10.setVide(true); e10.setObjetId(null);
      //	emplacementManager.updateObjectManager(e10, e10.getTerminale(), null);
      //	echantillonManager.updateObjectManager(ech, ech.getBanque(), ech.getPrelevement(), 
      //			null, ech.getObjetStatut(), null, ech.getEchantillonType(), null, null, null, qualite, preparation, reserv, listAnnoToCreateOrUpdate, listAnnoToDelete, filesCreated, filesToDelete, utilisateur, doValidation, operations, baseDir)
      echantillonManager.removeObjectCascadeManager(ech, null, u, null);
      fs.add(ech);

      prelevementManager.removeObjectCascadeManager(prel, null, u, null);
      fs.add(prel);
      maladieManager.removeObjectManager(mal, null, u);
      fs.add(mal);

      fs.add(pat);
      patientManager.removeObjectManager(pat, null, u, null);

      assertTrue(patientManager.findAllObjectsManager().size() == 5);
      assertTrue(maladieManager.findAllObjectsManager().size() == 6);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

      cleanUpFantomes(fs);
   }

   @Test
   public void testImportDerivesBatchesFileErrors(){
      final ImportTemplate template = importTemplateManager.findByIdManager(1);
      final Utilisateur u = utilisateurDao.findById(1);

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/test/io/imports/importDerivesBatches.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         importManager.importFileManager(template, u, wb.getSheetAt(1));
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
         errors.get(0).getException().printStackTrace();
      }

      assertTrue(errors.size() == 4);
      assertTrue(errors.get(0).getNbRow() == 9);
      assertTrue(errors.get(0).getException() instanceof WrongImportValueException);
      assertTrue(errors.get(1).getNbRow() == 1);
      assertTrue(errors.get(1).getException() instanceof ObjectStatutException);
      assertTrue(((ObjectStatutException) errors.get(1).getException()).getEntite().equals("Echantillon"));
      assertTrue(errors.get(2).getNbRow() == 6);
      assertTrue(errors.get(2).getException() instanceof ValidationException);
      assertTrue(((ValidationException) errors.get(2).getException()).getErrors().get(0).getFieldError().getCode()
         .equals("date.validation.supDateActuelle"));
      assertTrue(errors.get(3).getNbRow() == 11);
      assertTrue(errors.get(3).getException() instanceof ValidationException);
      assertTrue(((ValidationException) errors.get(3).getException()).getErrors().get(0).getFieldError().getCode()
         .equals("prodDerive.code.illegal"));

      assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
      assertTrue(patientManager.findAllObjectsManager().size() == 5);
      assertTrue(maladieManager.findAllObjectsManager().size() == 6);
      assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
      assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
      assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

   }

   @Test
   public void testImportSubDeriveForPrelevement(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite prelEntite = entiteDao.findById(2);
      final Entite deriveEntite = entiteDao.findById(8);
      final ImportTemplate itTest2 = setDeriveImportTemplateUp(prelEntite, banqueDao.findById(2));

      Prelevement p3 = prelevementManager.findByIdManager(3);
      Prelevement newPrel = new Prelevement();
      newPrel.setCode("newprel");
      newPrel.setQuantite(new Float(100.0));
      prelevementManager.createObjectManager(newPrel, itTest2.getBanque(), p3.getNature(), p3.getMaladie(), p3.getConsentType(),
         null, null, null, null, null, null, null, null, null, null, null, u, false, null, false);
      assertTrue(prelevementManager.findByCodeLikeManager("newprel", true).get(0).getQuantite().equals(new Float(100.0)));

      ImportHistorique ih = null;
      final Integer totDerives = prodDeriveManager.findAllObjectsManager().size();
      final Integer totOncf = objetNonConformeDao.findAll().size();
      final Integer totAnno = annotationValeurManager.findAllObjectsManager().size();

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/test/io/imports/importSubDerives.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         ih = importManager.importSubDeriveFileManager(itTest2, u, wb.getSheetAt(0), null);
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
         // errors.get(3).getException().printStackTrace();
      }

      assertTrue(errors.size() == 0);
      assertNotNull(ih);
      assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ih).size() == 12);

      // verif derives + annos + ncsf
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives + 12);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf + 10);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 3);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 3);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 4);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno + 16);

      // update quantite
      p3 = prelevementManager.findByIdManager(3);
      assertTrue(p3.getQuantite().equals(new Float(30)));

      final List<TKFantomableObject> fs = new ArrayList<>();

      // transformations
      final List<Transformation> trPrel3 = transformationManager.findByEntiteObjetManager(prelEntite, 3);
      assertTrue(trPrel3.size() == 3);
      // t1
      final Transformation t1 = trPrel3.get(0);
      assertTrue(t1.getQuantite().equals(new Float(12.0)));
      assertTrue(t1.getQuantiteUnite().equals(p3.getQuantiteUnite()));
      final List<ProdDerive> t1Derives = prodDeriveManager.findByTransformationManager(t1);
      assertTrue(t1Derives.size() == 3);
      // derive 1
      assertTrue(t1Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(0).getDateTransformation() != null);
      assertTrue(t1Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t1Derives.get(0).getConformeTraitement());
      assertNull(t1Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      List<AnnotationValeur> vals = annotationValeurManager.findByObjectManager(t1Derives.get(0));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("1"));
      assertTrue(vals.get(1).getBool());
      // derive2
      assertTrue(t1Derives.get(1).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(1).getDateTransformation() != null);
      assertTrue(t1Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertFalse(t1Derives.get(1).getConformeTraitement());
      assertFalse(t1Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(1).getProdDeriveId(), deriveEntite).size() == 4);
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(1));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("2"));
      assertTrue(vals.get(1).getBool());
      // derive3
      assertTrue(t1Derives.get(2).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(2).getDateTransformation() != null);
      assertTrue(t1Derives.get(2).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t1Derives.get(2).getConformeTraitement());
      assertNull(t1Derives.get(2).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(2).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(2));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getBool());

      fs.addAll(t1Derives);

      // t2
      final Transformation t2 = trPrel3.get(1);
      assertTrue(t2.getQuantite().equals(new Float(58.0)));
      assertTrue(t2.getQuantiteUnite().equals(p3.getQuantiteUnite()));
      final List<ProdDerive> t2Derives = prodDeriveManager.findByTransformationManager(t2);
      assertTrue(t2Derives.size() == 2);
      // derive10
      assertTrue(t2Derives.get(1).getProdType().getNom().equals("PROTEINE"));
      assertTrue(t2Derives.get(1).getDateTransformation() == null);
      assertTrue(t2Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t2Derives.get(1).getConformeTraitement());
      assertFalse(t2Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t2Derives.get(1).getProdDeriveId(), deriveEntite).size() == 2);
      vals = annotationValeurManager.findByObjectManager(t2Derives.get(1));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getAlphanum().equals("10"));

      fs.addAll(t2Derives);

      // t3
      final Transformation t3 = trPrel3.get(2);
      assertTrue(t3.getQuantite() == null);
      assertTrue(t3.getQuantiteUnite().equals(p3.getQuantiteUnite()));
      final List<ProdDerive> t3Derives = prodDeriveManager.findByTransformationManager(t3);
      assertTrue(t3Derives.size() == 2);
      // derive12
      assertTrue(t3Derives.get(1).getProdType().getNom().equals("ARN"));
      assertTrue(t3Derives.get(1).getDateTransformation() == null);
      assertTrue(t3Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertFalse(t3Derives.get(1).getConformeTraitement());
      assertNull(t3Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t3Derives.get(1).getProdDeriveId(), deriveEntite).size() == 1);
      vals = annotationValeurManager.findByObjectManager(t3Derives.get(1));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getAlphanum().equals("12"));

      fs.addAll(t3Derives);

      // update quantite newprel
      newPrel = prelevementManager.findByIdManager(newPrel.getPrelevementId());
      assertTrue(newPrel.getQuantite().equals(new Float(0)));
      fs.add(newPrel);

      // transformations
      final List<Transformation> trNewPrel =
         transformationManager.findByEntiteObjetManager(prelEntite, newPrel.getPrelevementId());
      assertTrue(trNewPrel.size() == 2);
      // t4
      final Transformation t4 = trNewPrel.get(0);
      assertTrue(t4.getQuantite().equals(new Float(25.0)));
      assertTrue(t4.getQuantiteUnite() == null);
      final List<ProdDerive> t4Derives = prodDeriveManager.findByTransformationManager(t4);
      assertTrue(t4Derives.size() == 2);
      // derive 6
      assertTrue(t4Derives.get(1).getProdType().getNom().equals("ARN"));
      assertTrue(t4Derives.get(1).getDateTransformation() != null);
      assertTrue(t4Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t4Derives.get(1).getConformeTraitement());
      assertNull(t4Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t4Derives.get(1).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t4Derives.get(1));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("5"));
      assertFalse(vals.get(1).getBool());

      fs.addAll(t4Derives);

      // t5
      final Transformation t5 = trNewPrel.get(1);
      assertTrue(t5.getQuantite().equals(new Float(75.0)));
      assertTrue(t5.getQuantiteUnite() == null);
      final List<ProdDerive> t5Derives = prodDeriveManager.findByTransformationManager(t5);
      assertTrue(t5Derives.size() == 3);
      // derive 7
      assertTrue(t5Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t5Derives.get(0).getDateTransformation() == null);
      assertTrue(t5Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t5Derives.get(0).getConformeTraitement());
      assertNull(t5Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t5Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t5Derives.get(0));
      assertTrue(vals.size() == 1);
      assertFalse(vals.get(0).getBool());

      fs.addAll(t5Derives);

      // clean up
      p3.setQuantite(new Float(100.0));
      prelevementManager.updateObjectManager(p3, p3.getBanque(), p3.getNature(), p3.getMaladie(), p3.getConsentType(),
         p3.getPreleveur(), p3.getServicePreleveur(), p3.getPrelevementType(), p3.getConditType(), p3.getConditMilieu(),
         p3.getTransporteur(), p3.getOperateur(), p3.getQuantiteUnite(), null, null, null, null, null, u, null, false, null,
         false);

      final List<Operation> ops = getOperationManager().findByObjetIdEntiteAndOpeTypeManager(p3, operationTypeDao.findById(5));
      assertTrue(ops.size() == 1);
      getOperationManager().removeObjectManager(ops.get(0));

      prelevementManager.removeObjectCascadeManager(newPrel, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t1, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t2, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t3, null, u, null);

      importTemplateManager.removeObjectManager(itTest2);

      cleanUpFantomes(fs);

      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 0);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno);
   }

   @Test
   public void testImportSubDeriveForPrelevementErrors(){
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite prelEntite = entiteDao.findById(2);
      final ImportTemplate itTest2 = setDeriveImportTemplateUp(prelEntite, banqueDao.findById(2));

      Prelevement p3 = prelevementManager.findByIdManager(3);
      final Prelevement newPrel = new Prelevement();
      newPrel.setCode("newprel");
      newPrel.setQuantite(new Float(100.0));
      prelevementManager.createObjectManager(newPrel, itTest2.getBanque(), p3.getNature(), p3.getMaladie(), p3.getConsentType(),
         null, null, null, null, null, null, null, p3.getQuantiteUnite(), null, null, null, u, false, null, false);
      assertTrue(prelevementManager.findByCodeLikeManager("newprel", true).get(0).getQuantite().equals(new Float(100.0)));

      ImportHistorique ih = null;
      final Integer totDerives = prodDeriveManager.findAllObjectsManager().size();
      final Integer totOncf = objetNonConformeDao.findAll().size();
      final Integer totAnno = annotationValeurManager.findAllObjectsManager().size();

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/importSubDerives.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         ih = importManager.importSubDeriveFileManager(itTest2, u, wb.getSheetAt(1), null);
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
         // errors.get(3).getException().printStackTrace();
      }

      assertTrue(errors.size() == 11);
      assertNull(ih);

      // verif derives + annos + ncsf
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 0);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno);

      // no update quantite
      p3 = prelevementManager.findByIdManager(3);
      assertTrue(p3.getQuantite().equals(new Float(100)));

      // errors durant extraction données depuis Excel
      int i = 0;
      ImportError e = errors.get(i);
      assertTrue(e.getNbRow() == 1);
      assertTrue(e.getException() instanceof DeriveImportParentNotFoundException);
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getColonne().getNom().equals("code.parent"));
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getValeurAttendue().equals("PRLVT333"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 2);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("qte.transf"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("Float"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 4);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("Type du dérivé"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("ProdType"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 5);
      assertTrue(e.getException() instanceof DeriveImportParentNotFoundException);
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getColonne().getNom().equals("code.parent"));
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getValeurAttendue() == null);
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 6);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("evt.date"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("Calendar"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 7);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("Noconf cession"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("ConformeCession.Raison"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 11);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("Boolderive"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("Boolean"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 12);
      assertTrue(e.getException() instanceof UsedPositionException);
      assertTrue(((UsedPositionException) e.getException()).getEntite().equals("ProdDerive"));
      assertTrue(((UsedPositionException) e.getException()).getOperation().equals("Import"));
      assertTrue(((UsedPositionException) e.getException()).getPosition().equals(1));
      // errors survenues lors batch save derive
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 3);
      assertTrue(e.getException() instanceof ValidationException);
      assertTrue(
         ((ValidationException) e.getException()).getErrors().get(0).getFieldError().getCode().equals("prodDerive.code.illegal"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 8);
      assertTrue(e.getException() instanceof TransformationQuantiteOverDemandException);
      assertTrue(((TransformationQuantiteOverDemandException) e.getException()).getQteDemandee().equals(new Float(500)));
      assertTrue(((TransformationQuantiteOverDemandException) e.getException()).getQteRestante().equals(new Float(100)));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 10);
      assertTrue(e.getException() instanceof ValidationException);
      assertTrue(
         ((ValidationException) e.getException()).getErrors().get(0).getFieldError().getCode().equals("anno.alphanum.illegal"));

      importTemplateManager.removeObjectManager(itTest2);

      prelevementManager.removeObjectCascadeManager(newPrel, null, u, null);

      final List<TKFantomableObject> fs = new ArrayList<>();
      fs.add(newPrel);
      cleanUpFantomes(fs);
   }

   @Test
   public void testImportSubDeriveForEchantillon() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite echEntite = entiteDao.findById(3);
      final Entite deriveEntite = entiteDao.findById(8);
      final ImportTemplate itTest2 = setDeriveImportTemplateUp(echEntite, banqueDao.findById(1));

      final Integer totDerives = prodDeriveManager.findAllObjectsManager().size();
      final Integer totOncf = objetNonConformeDao.findAll().size();
      final Integer totAnno = annotationValeurManager.findAllObjectsManager().size();
      final Integer totRetour = retourManager.findAllObjectsManager().size();

      Echantillon e2 = echantillonManager.findByIdManager(2);
      Echantillon newEch = new Echantillon();
      newEch.setCode("NEWECH");
      newEch.setQuantite(new Float(100.0));
      Emplacement e4 = emplacementManager.findByIdManager(4);
      echantillonManager.createObjectManager(newEch, itTest2.getBanque(), e2.getPrelevement(), null, objetStatutDao.findById(1),
         e4, e2.getEchantillonType(), null, null, null, null, null, null, u, false, null, false);
      assertTrue(echantillonManager.findByCodeLikeManager("NEWECH", true).get(0).getQuantite().equals(new Float(100.0)));
      e4.setVide(false);
      e4.setObjetId(newEch.getEchantillonId());
      emplacementManager.updateObjectManager(e4, e4.getTerminale(), echEntite);
      assertTrue(echantillonManager.getEmplacementManager(newEch).equals(e4));
      assertTrue(emplacementManager.findObjByEmplacementManager(e4, "Echantillon").get(0).equals(newEch));

      // retour incomplete
      final Calendar dateS = Calendar.getInstance();
      dateS.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10"));
      final Retour incomp1 = new Retour();
      incomp1.setTempMoyenne(new Float(20));
      incomp1.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp1, newEch, emplacementManager.findByIdManager(4), null, null, null, null, u,
         "creation");
      assertTrue(echantillonManager.findByIdManager(newEch.getEchantillonId()).getObjetStatut().getStatut().equals("ENCOURS"));
      final Retour incomp2 = new Retour();
      incomp2.setTempMoyenne(new Float(22));
      incomp2.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp2, e2, emplacementManager.findByIdManager(3), null, null, null, null, u,
         "creation");
      assertTrue(echantillonManager.findByIdManager(e2.getEchantillonId()).getObjetStatut().getStatut().equals("ENCOURS"));

      ImportHistorique ih = null;

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/importSubDerives.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         ih = importManager.importSubDeriveFileManager(itTest2, u, wb.getSheetAt(2), "OBS TEST");
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
      }

      assertTrue(errors.size() == 0);
      assertNotNull(ih);
      assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ih).size() == 12);

      // verif derives + annos + ncsf
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives + 12);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf + 10);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 3);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 3);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 4);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno + 16);

      // update quantite + retour + echantillon in base
      e2 = echantillonManager.findByIdManager(2);
      assertTrue(e2.getQuantite().equals(new Float(1)));
      assertTrue(e2.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 1);
      final Retour complete1 = retourManager.getRetoursForObjectManager(e2).get(0);
      assertTrue(complete1.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      assertTrue(complete1.getDateRetour().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:30")));
      assertTrue(complete1.getTempMoyenne().equals(new Float(22)));

      final List<TKFantomableObject> fs = new ArrayList<>();

      // transformations
      final List<Transformation> trEch2 = transformationManager.findByEntiteObjetManager(echEntite, 2);
      assertTrue(trEch2.size() == 3);
      // t1 = complete Retour
      final Transformation t1 = trEch2.get(0);
      assertTrue(t1.getQuantite().equals(new Float(12.0)));
      assertTrue(t1.getQuantiteUnite().getNom().equals("FRAGMENTS"));
      final List<ProdDerive> t1Derives = prodDeriveManager.findByTransformationManager(t1);
      assertTrue(t1Derives.size() == 3);
      assertTrue(complete1.getTransformation().equals(t1));
      // derive 1
      assertTrue(t1Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(0).getDateTransformation() != null);
      assertTrue(t1Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t1Derives.get(0).getConformeTraitement());
      assertNull(t1Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      List<AnnotationValeur> vals = annotationValeurManager.findByObjectManager(t1Derives.get(0));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("1"));
      assertTrue(vals.get(1).getBool());
      // derive2
      assertTrue(t1Derives.get(1).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(1).getDateTransformation() != null);
      assertTrue(t1Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertFalse(t1Derives.get(1).getConformeTraitement());
      assertFalse(t1Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(1).getProdDeriveId(), deriveEntite).size() == 4);
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(1));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("2"));
      assertTrue(vals.get(1).getBool());
      // derive3
      assertTrue(t1Derives.get(2).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(2).getDateTransformation() != null);
      assertTrue(t1Derives.get(2).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t1Derives.get(2).getConformeTraitement());
      assertNull(t1Derives.get(2).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(2).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(2));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getBool());

      fs.addAll(t1Derives);

      // t3
      final Transformation t3 = trEch2.get(1);
      assertTrue(t3.getQuantite() == null);
      assertTrue(t3.getQuantiteUnite().equals(e2.getQuantiteUnite()));
      final List<ProdDerive> t3Derives = prodDeriveManager.findByTransformationManager(t3);
      assertTrue(t3Derives.size() == 2);
      // derive10
      assertTrue(t3Derives.get(1).getProdType().getNom().equals("PROTEINE"));
      assertTrue(t3Derives.get(1).getDateTransformation() != null);
      assertTrue(t3Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t3Derives.get(1).getConformeTraitement());
      assertFalse(t3Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t3Derives.get(1).getProdDeriveId(), deriveEntite).size() == 2);
      vals = annotationValeurManager.findByObjectManager(t3Derives.get(1));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getAlphanum().equals("10"));

      fs.addAll(t3Derives);

      // t4
      final Transformation t4 = trEch2.get(2);
      assertTrue(t4.getQuantite().equals(new Float(12)));
      assertTrue(t4.getQuantiteUnite().equals(e2.getQuantiteUnite()));
      final List<ProdDerive> t4Derives = prodDeriveManager.findByTransformationManager(t4);
      assertTrue(t4Derives.size() == 2);
      // derive12
      assertTrue(t4Derives.get(1).getProdType().getNom().equals("ARN"));
      assertTrue(t4Derives.get(1).getDateTransformation() == null);
      assertTrue(t4Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertFalse(t4Derives.get(1).getConformeTraitement());
      assertNull(t4Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t4Derives.get(1).getProdDeriveId(), deriveEntite).size() == 1);
      vals = annotationValeurManager.findByObjectManager(t4Derives.get(1));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getAlphanum().equals("12"));

      fs.addAll(t4Derives);

      // update quantite + retour + newech
      newEch = echantillonManager.findByIdManager(newEch.getEchantillonId());
      assertTrue(newEch.getQuantite().equals(new Float(0)));
      assertTrue(newEch.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(newEch.getEmplacement() == null);
      e4 = emplacementManager.findByIdManager(4);
      assertTrue(e4.getVide());
      assertTrue(e4.getObjetId() == null);
      assertTrue(e4.getEntite() == null);
      assertTrue(retourManager.getRetoursForObjectManager(newEch).size() == 2);
      final Retour complete2 = retourManager.getRetoursForObjectManager(newEch).get(0);
      assertTrue(complete2.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      assertTrue(complete2.getDateRetour().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:33")));
      assertTrue(complete2.getTempMoyenne().equals(new Float(20)));
      final Retour epuisement = retourManager.getRetoursForObjectManager(newEch).get(1);
      assertTrue(epuisement.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:40")));
      assertTrue(epuisement.getDateRetour() == null);
      assertTrue(epuisement.getConteneur().getConteneurId().equals(1));
      assertTrue(epuisement.getObservations().contains("OBS TEST"));
      assertTrue(epuisement.getTempMoyenne().equals(new Float(20)));

      // transformations
      final List<Transformation> trNewEch = transformationManager.findByEntiteObjetManager(echEntite, newEch.getEchantillonId());
      assertTrue(trNewEch.size() == 3);

      // t2
      final Transformation t2 = trNewEch.get(0);
      assertTrue(t2.getQuantite().equals(new Float(25.0)));
      assertTrue(t2.getQuantiteUnite() == null);
      final List<ProdDerive> t2Derives = prodDeriveManager.findByTransformationManager(t2);
      assertTrue(t2Derives.size() == 2);
      assertTrue(complete2.getTransformation().equals(t2));
      // derive 6
      assertTrue(t2Derives.get(1).getProdType().getNom().equals("ARN"));
      assertTrue(t2Derives.get(1).getDateTransformation() != null);
      assertTrue(t2Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t2Derives.get(1).getConformeTraitement());
      assertNull(t2Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t2Derives.get(1).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t2Derives.get(1));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("5"));
      assertFalse(vals.get(1).getBool());

      fs.addAll(t2Derives);

      // t5
      final Transformation t5 = trNewEch.get(1);
      assertTrue(t5.getQuantite().equals(new Float(10.0)));
      assertTrue(t5.getQuantiteUnite() == null);
      final List<ProdDerive> t5Derives = prodDeriveManager.findByTransformationManager(t5);
      assertTrue(t5Derives.size() == 1);
      // derive 7
      assertTrue(t5Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t5Derives.get(0).getDateTransformation() == null);
      assertTrue(t5Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t5Derives.get(0).getConformeTraitement());
      assertNull(t5Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t5Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t5Derives.get(0));
      assertTrue(vals.size() == 1);
      assertFalse(vals.get(0).getBool());

      fs.addAll(t5Derives);

      // t6
      final Transformation t6 = trNewEch.get(2);
      assertTrue(t6.getQuantite().equals(new Float(65.0)));
      assertTrue(t6.getQuantiteUnite() == null);
      final List<ProdDerive> t6Derives = prodDeriveManager.findByTransformationManager(t6);
      assertTrue(t6Derives.size() == 2);
      assertTrue(epuisement.getTransformation().equals(t6));
      // derive 7
      assertTrue(t6Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t6Derives.get(0).getDateTransformation() != null);
      assertTrue(t6Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t6Derives.get(0).getConformeTraitement());
      assertNull(t6Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t6Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t6Derives.get(0));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("7"));
      assertFalse(vals.get(1).getBool());

      fs.addAll(t6Derives);

      // clean up
      e2.setQuantite(new Float(25.0));
      echantillonManager.updateObjectManager(e2, e2.getBanque(), e2.getPrelevement(), e2.getCollaborateur(), e2.getObjetStatut(),
         e2.getEmplacement(), e2.getEchantillonType(), null, null, e2.getQuantiteUnite(), e2.getEchanQualite(), e2.getModePrepa(),
         null, null, null, null, u, false, null, null);
      final List<Operation> ops = getOperationManager().findByObjetIdEntiteAndOpeTypeManager(e2, operationTypeDao.findById(5));
      assertTrue(ops.size() == 1);
      getOperationManager().removeObjectManager(ops.get(0));

      retourManager.removeObjectManager(complete1);

      fs.add(newEch);
      echantillonManager.removeObjectCascadeManager(newEch, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t1, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t3, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t4, null, u, null);

      importTemplateManager.removeObjectManager(itTest2);

      cleanUpFantomes(fs);

      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 0);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno);
      assertTrue(retourManager.findAllObjectsManager().size() == totRetour);
   }

   @Test
   public void testImportSubDeriveForEchantillonErrors() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite echEntite = entiteDao.findById(3);
      final ImportTemplate itTest2 = setDeriveImportTemplateUp(echEntite, banqueDao.findById(1));

      final Integer totDerives = prodDeriveManager.findAllObjectsManager().size();
      final Integer totOncf = objetNonConformeDao.findAll().size();
      final Integer totAnno = annotationValeurManager.findAllObjectsManager().size();
      final Integer totRetour = retourManager.findAllObjectsManager().size();

      Echantillon e2 = echantillonManager.findByIdManager(2);
      Echantillon newEch = new Echantillon();
      newEch.setCode("NEWECH");
      newEch.setQuantite(new Float(100.0));
      Emplacement e4 = emplacementManager.findByIdManager(4);
      echantillonManager.createObjectManager(newEch, itTest2.getBanque(), e2.getPrelevement(), null, objetStatutDao.findById(1),
         e4, e2.getEchantillonType(), null, e2.getQuantiteUnite(), null, null, null, null, u, false, null, false);
      assertTrue(echantillonManager.findByCodeLikeManager("NEWECH", true).get(0).getQuantite().equals(new Float(100.0)));
      e4.setVide(false);
      e4.setObjetId(newEch.getEchantillonId());
      emplacementManager.updateObjectManager(e4, e4.getTerminale(), echEntite);
      assertTrue(echantillonManager.getEmplacementManager(newEch).equals(e4));
      assertTrue(emplacementManager.findObjByEmplacementManager(e4, "Echantillon").get(0).equals(newEch));

      // retour incomplete
      final Calendar dateS = Calendar.getInstance();
      dateS.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10"));
      final Retour incomp1 = new Retour();
      incomp1.setTempMoyenne(new Float(20));
      incomp1.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp1, newEch, emplacementManager.findByIdManager(4), null, null, null, null, u,
         "creation");
      assertTrue(echantillonManager.findByIdManager(newEch.getEchantillonId()).getObjetStatut().getStatut().equals("ENCOURS"));
      final Retour incomp2 = new Retour();
      incomp2.setTempMoyenne(new Float(22));
      incomp2.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp2, e2, emplacementManager.findByIdManager(3), null, null, null, null, u,
         "creation");
      assertTrue(echantillonManager.findByIdManager(e2.getEchantillonId()).getObjetStatut().getStatut().equals("ENCOURS"));

      ImportHistorique ih = null;

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/importSubDerives.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         ih = importManager.importSubDeriveFileManager(itTest2, u, wb.getSheetAt(3), "OBS TEST");
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
      }

      assertTrue(errors.size() == 5);
      assertNull(ih);

      // verif derives + annos + ncsf
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 0);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno);
      assertTrue(retourManager.findAllObjectsManager().size() == totRetour + 2);

      // echantillon in base + retour intacts
      e2 = echantillonManager.findByIdManager(2);
      assertTrue(e2.getQuantite().equals(new Float(25)));
      assertTrue(e2.getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(retourManager.getRetoursForObjectManager(e2).size() == 1);
      final Retour complete1 = retourManager.getRetoursForObjectManager(e2).get(0);
      assertTrue(complete1.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      assertTrue(complete1.getDateRetour() == null);
      assertTrue(complete1.getTempMoyenne().equals(new Float(22)));

      final List<TKFantomableObject> fs = new ArrayList<>();

      // transformations
      final List<Transformation> trEch2 = transformationManager.findByEntiteObjetManager(echEntite, 2);
      assertTrue(trEch2.size() == 0);

      // update quantite + retour + newech
      newEch = echantillonManager.findByIdManager(newEch.getEchantillonId());
      assertTrue(newEch.getQuantite().equals(new Float(100)));
      assertTrue(newEch.getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(newEch.getEmplacement() != null);
      e4 = emplacementManager.findByIdManager(4);
      assertFalse(e4.getVide());
      assertTrue(e4.getObjetId().equals(newEch.getEchantillonId()));
      assertTrue(e4.getEntite().equals(echEntite));
      assertTrue(retourManager.getRetoursForObjectManager(newEch).size() == 1);
      final Retour complete2 = retourManager.getRetoursForObjectManager(newEch).get(0);
      assertTrue(complete2.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      assertTrue(complete2.getDateRetour() == null);
      assertTrue(complete2.getTempMoyenne().equals(new Float(20)));

      // transformations
      final List<Transformation> trNewEch = transformationManager.findByEntiteObjetManager(echEntite, newEch.getEchantillonId());
      assertTrue(trNewEch.size() == 0);

      int i = 0;
      // erreur survenant lors du parsing Excel
      ImportError e = errors.get(i);
      assertTrue(e.getNbRow() == 1);
      assertTrue(e.getException() instanceof DeriveImportParentNotFoundException);
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getColonne().getNom().equals("code.parent"));
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getValeurAttendue().equals("PTRA.2254"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 7);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("evt.date"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("Calendar"));
      // erreur lancée par manager createDerives
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 2);
      assertTrue(e.getException() instanceof ValidationException);
      assertTrue(((ValidationException) e.getException()).getErrors().get(0).getFieldError().getCode()
         .equals("date.validation.supDateRetour"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 6);
      assertTrue(e.getException() instanceof ObjectStatutException);
      assertTrue(((ObjectStatutException) e.getException()).getOperation().equals("transformation"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 9);
      assertTrue(e.getException() instanceof TransformationQuantiteOverDemandException);
      assertTrue(((TransformationQuantiteOverDemandException) e.getException()).getQteDemandee().equals(new Float(40)));
      // les deux premiers transf ont ampute la qte avant exception
      assertTrue(((TransformationQuantiteOverDemandException) e.getException()).getQteRestante().equals(new Float(1)));

      // clean up
      retourManager.removeObjectManager(complete1);
      e2 = echantillonManager.findByIdManager(2);
      assertTrue(e2.getObjetStatut().getStatut().equals("STOCKE"));

      e4.setObjetId(null);
      e4.setEntite(null);
      e4.setVide(true);
      emplacementManager.updateObjectManager(e4, e4.getTerminale(), null);

      echantillonManager.updateObjectManager(newEch, newEch.getBanque(), newEch.getPrelevement(), null, newEch.getObjetStatut(),
         null, newEch.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, false, null, null);

      fs.add(newEch);
      echantillonManager.removeObjectCascadeManager(newEch, null, u, null);

      importTemplateManager.removeObjectManager(itTest2);

      cleanUpFantomes(fs);

      assertTrue(retourManager.findAllObjectsManager().size() == totRetour);
   }

   @Test
   public void testImportSubDeriveForDerive() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite deriveEntite = entiteDao.findById(8);
      final ImportTemplate itTest2 = setDeriveImportTemplateUp(deriveEntite, banqueDao.findById(1));

      final Integer totDerives = prodDeriveManager.findAllObjectsManager().size();
      final Integer totOncf = objetNonConformeDao.findAll().size();
      final Integer totAnno = annotationValeurManager.findAllObjectsManager().size();
      final Integer totRetour = retourManager.findAllObjectsManager().size();

      ProdDerive p2 = prodDeriveManager.findByIdManager(2);
      ProdDerive newDerive = new ProdDerive();
      newDerive.setCode("NEWDER");
      newDerive.setQuantite(new Float(100.0));
      Emplacement e4 = emplacementManager.findByIdManager(4);
      prodDeriveManager.createObjectManager(newDerive, itTest2.getBanque(), p2.getProdType(), objetStatutDao.findById(1), null,
         e4, null, null, null, null, null, null, null, null, u, false, null, false);
      assertTrue(prodDeriveManager.findByCodeLikeManager("NEWDER", true).get(0).getQuantite().equals(new Float(100.0)));
      e4.setVide(false);
      e4.setObjetId(newDerive.getProdDeriveId());
      emplacementManager.updateObjectManager(e4, e4.getTerminale(), deriveEntite);
      assertTrue(prodDeriveManager.getEmplacementManager(newDerive).equals(e4));
      assertTrue(emplacementManager.findObjByEmplacementManager(e4, "ProdDerive").get(0).equals(newDerive));

      // retour incomplete
      final Calendar dateS = Calendar.getInstance();
      dateS.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10"));
      final Retour incomp1 = new Retour();
      incomp1.setTempMoyenne(new Float(20));
      incomp1.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp1, newDerive, emplacementManager.findByIdManager(4), null, null, null, null,
         u, "creation");
      assertTrue(prodDeriveManager.findByIdManager(newDerive.getProdDeriveId()).getObjetStatut().getStatut().equals("ENCOURS"));
      final Retour incomp2 = new Retour();
      incomp2.setTempMoyenne(new Float(22));
      incomp2.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp2, p2, emplacementManager.findByIdManager(3), null, null, null, null, u,
         "creation");
      assertTrue(prodDeriveManager.findByIdManager(p2.getProdDeriveId()).getObjetStatut().getStatut().equals("ENCOURS"));

      ImportHistorique ih = null;

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/importSubDerives.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         ih = importManager.importSubDeriveFileManager(itTest2, u, wb.getSheetAt(4), "OBS SUB DERIVES TEST");
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
      }

      assertTrue(errors.size() == 0);
      assertNotNull(ih);
      assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ih).size() == 12);

      // verif derives + annos + ncsf
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives + 12 + 1);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf + 10);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == (2 + 1));
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 3);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == (3 + 1));
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 2);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno + 16);

      // update quantite + retour + echantillon in base
      p2 = prodDeriveManager.findByIdManager(2);
      assertTrue(p2.getQuantite() == null);
      assertTrue(p2.getObjetStatut().getStatut().equals("STOCKE"));
      assertTrue(retourManager.getRetoursForObjectManager(p2).size() == 1);
      final Retour complete1 = retourManager.getRetoursForObjectManager(p2).get(0);
      assertTrue(complete1.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      // date retour = current date au moment de l'import
      assertTrue(complete1.getDateRetour().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
         && complete1.getDateRetour().get(Calendar.HOUR) == Calendar.getInstance().get(Calendar.HOUR)
         && (complete1.getDateRetour().get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE)
            || (complete1.getDateRetour().get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE) + 1)));
      assertTrue(complete1.getTempMoyenne().equals(new Float(22)));

      final List<TKFantomableObject> fs = new ArrayList<>();

      // transformations
      final List<Transformation> trEch2 = transformationManager.findByEntiteObjetManager(deriveEntite, 2);
      assertTrue(trEch2.size() == 2);
      // t1 = complete Retour
      final Transformation t1 = trEch2.get(0);
      assertTrue(t1.getQuantite().equals(new Float(12.0)));
      assertTrue(t1.getQuantiteUnite().equals(p2.getQuantiteUnite()));
      final List<ProdDerive> t1Derives = prodDeriveManager.findByTransformationManager(t1);
      assertTrue(t1Derives.size() == 5);
      assertTrue(complete1.getTransformation().equals(t1));
      // derive 1
      assertTrue(t1Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(0).getDateTransformation() != null);
      assertTrue(t1Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t1Derives.get(0).getConformeTraitement());
      assertNull(t1Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      List<AnnotationValeur> vals = annotationValeurManager.findByObjectManager(t1Derives.get(0));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("1"));
      assertTrue(vals.get(1).getBool());
      // derive2
      assertTrue(t1Derives.get(1).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(1).getDateTransformation() != null);
      assertTrue(t1Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertFalse(t1Derives.get(1).getConformeTraitement());
      assertFalse(t1Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(1).getProdDeriveId(), deriveEntite).size() == 4);
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(1));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("2"));
      assertTrue(vals.get(1).getBool());
      // derive3
      assertTrue(t1Derives.get(2).getProdType().getNom().equals("ADN"));
      assertTrue(t1Derives.get(2).getDateTransformation() != null);
      assertTrue(t1Derives.get(2).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t1Derives.get(2).getConformeTraitement());
      assertNull(t1Derives.get(2).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(2).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(2));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getBool());
      // derive12
      assertTrue(t1Derives.get(4).getProdType().getNom().equals("ARN"));
      assertTrue(t1Derives.get(4).getDateTransformation() == null);
      assertTrue(t1Derives.get(4).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertFalse(t1Derives.get(4).getConformeTraitement());
      assertNull(t1Derives.get(4).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t1Derives.get(4).getProdDeriveId(), deriveEntite).size() == 1);
      vals = annotationValeurManager.findByObjectManager(t1Derives.get(4));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getAlphanum().equals("12"));

      fs.addAll(t1Derives);

      // t3
      final Transformation t3 = trEch2.get(1);
      assertTrue(t3.getQuantite() == null);
      assertTrue(t3.getQuantiteUnite().equals(p2.getQuantiteUnite()));
      final List<ProdDerive> t3Derives = prodDeriveManager.findByTransformationManager(t3);
      assertTrue(t3Derives.size() == 2);
      // derive10
      assertTrue(t3Derives.get(1).getProdType().getNom().equals("PROTEINE"));
      assertTrue(t3Derives.get(1).getDateTransformation() != null);
      assertTrue(t3Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t3Derives.get(1).getConformeTraitement());
      assertFalse(t3Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t3Derives.get(1).getProdDeriveId(), deriveEntite).size() == 2);
      vals = annotationValeurManager.findByObjectManager(t3Derives.get(1));
      assertTrue(vals.size() == 1);
      assertTrue(vals.get(0).getAlphanum().equals("10"));

      fs.addAll(t3Derives);

      // update quantite + retour + newech
      newDerive = prodDeriveManager.findByIdManager(newDerive.getProdDeriveId());
      assertTrue(newDerive.getQuantite().equals(new Float(0)));
      assertTrue(newDerive.getObjetStatut().getStatut().equals("EPUISE"));
      assertTrue(newDerive.getEmplacement() == null);
      e4 = emplacementManager.findByIdManager(4);
      assertTrue(e4.getVide());
      assertTrue(e4.getObjetId() == null);
      assertTrue(e4.getEntite() == null);
      assertTrue(retourManager.getRetoursForObjectManager(newDerive).size() == 2);
      final Retour complete2 = retourManager.getRetoursForObjectManager(newDerive).get(0);
      assertTrue(complete2.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      // date retour = current date au moment de l'enregistrement
      assertTrue(complete2.getDateRetour().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:37")));
      assertTrue(complete2.getTempMoyenne().equals(new Float(20)));
      final Retour epuisement = retourManager.getRetoursForObjectManager(newDerive).get(1);
      // date retour = current date au moment de l'epuisement
      assertTrue(epuisement.getDateSortie().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
         && epuisement.getDateSortie().get(Calendar.HOUR) == Calendar.getInstance().get(Calendar.HOUR)
         && (epuisement.getDateSortie().get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE)
            || (epuisement.getDateSortie().get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE) + 1)));
      assertTrue(epuisement.getDateRetour() == null);
      assertTrue(epuisement.getConteneur().getConteneurId().equals(1));
      assertTrue(epuisement.getObservations().contains("OBS SUB DERIVES TEST"));
      assertTrue(epuisement.getTempMoyenne().equals(new Float(20)));

      // transformations
      final List<Transformation> trNewEch =
         transformationManager.findByEntiteObjetManager(deriveEntite, newDerive.getProdDeriveId());
      assertTrue(trNewEch.size() == 3);

      // t2
      final Transformation t2 = trNewEch.get(0);
      assertTrue(t2.getQuantite().equals(new Float(25.0)));
      assertTrue(t2.getQuantiteUnite() == null);
      final List<ProdDerive> t2Derives = prodDeriveManager.findByTransformationManager(t2);
      assertTrue(t2Derives.size() == 2);
      assertTrue(complete2.getTransformation().equals(t2));
      // derive 5
      assertTrue(t2Derives.get(1).getProdType().getNom().equals("ARN"));
      assertTrue(t2Derives.get(1).getDateTransformation() != null);
      assertTrue(t2Derives.get(1).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t2Derives.get(1).getConformeTraitement());
      assertNull(t2Derives.get(1).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t2Derives.get(1).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t2Derives.get(1));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("5"));
      assertFalse(vals.get(1).getBool());

      fs.addAll(t2Derives);

      // t5
      final Transformation t5 = trNewEch.get(1);
      assertTrue(t5.getQuantite().equals(new Float(10.0)));
      assertTrue(t5.getQuantiteUnite() == null);
      final List<ProdDerive> t5Derives = prodDeriveManager.findByTransformationManager(t5);
      assertTrue(t5Derives.size() == 1);
      // derive 7
      assertTrue(t5Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t5Derives.get(0).getDateTransformation() == null);
      assertTrue(t5Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t5Derives.get(0).getConformeTraitement());
      assertNull(t5Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t5Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t5Derives.get(0));
      assertTrue(vals.size() == 1);
      assertFalse(vals.get(0).getBool());

      fs.addAll(t5Derives);

      // t6
      final Transformation t6 = trNewEch.get(2);
      assertTrue(t6.getQuantite().equals(new Float(65.0)));
      assertTrue(t6.getQuantiteUnite() == null);
      final List<ProdDerive> t6Derives = prodDeriveManager.findByTransformationManager(t6);
      assertTrue(t6Derives.size() == 2);
      assertTrue(epuisement.getTransformation().equals(t6));
      // derive 7
      assertTrue(t6Derives.get(0).getProdType().getNom().equals("ADN"));
      assertTrue(t6Derives.get(0).getDateTransformation() != null);
      assertTrue(t6Derives.get(0).getObjetStatut().getStatut().equals("NON STOCKE"));
      assertNull(t6Derives.get(0).getConformeTraitement());
      assertNull(t6Derives.get(0).getConformeCession());
      assertTrue(objetNonConformeDao.findByObjetAndEntite(t6Derives.get(0).getProdDeriveId(), deriveEntite).isEmpty());
      vals = annotationValeurManager.findByObjectManager(t6Derives.get(0));
      assertTrue(vals.size() == 2);
      assertTrue(vals.get(0).getAlphanum().equals("7"));
      assertFalse(vals.get(1).getBool());

      fs.addAll(t6Derives);

      // clean up
      assertTrue(p2.getQuantite() == null);

      retourManager.removeObjectManager(complete1);

      fs.add(newDerive);
      prodDeriveManager.removeObjectCascadeManager(newDerive, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t1, null, u, null);
      prodDeriveManager.removeObjectCascadeManager(t3, null, u, null);

      importTemplateManager.removeObjectManager(itTest2);

      cleanUpFantomes(fs);

      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 0);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno);
      assertTrue(retourManager.findAllObjectsManager().size() == totRetour);
   }

   @Test
   public void testImportSubDeriveForDeriveErrors() throws ParseException{
      final Utilisateur u = utilisateurDao.findById(1);
      final Entite deriveEntite = entiteDao.findById(8);
      final ImportTemplate itTest2 = setDeriveImportTemplateUp(deriveEntite, banqueDao.findById(1));

      final Integer totDerives = prodDeriveManager.findAllObjectsManager().size();
      final Integer totOncf = objetNonConformeDao.findAll().size();
      final Integer totAnno = annotationValeurManager.findAllObjectsManager().size();
      final Integer totRetour = retourManager.findAllObjectsManager().size();

      ProdDerive p2 = prodDeriveManager.findByIdManager(2);
      ProdDerive newDerive = new ProdDerive();
      newDerive.setCode("NEWDER");
      newDerive.setQuantite(new Float(100.0));
      Emplacement e4 = emplacementManager.findByIdManager(4);
      prodDeriveManager.createObjectManager(newDerive, itTest2.getBanque(), p2.getProdType(), objetStatutDao.findById(1), null,
         e4, null, null, null, null, null, null, null, null, u, false, null, false);
      assertTrue(prodDeriveManager.findByCodeLikeManager("NEWDER", true).get(0).getQuantite().equals(new Float(100.0)));
      e4.setVide(false);
      e4.setObjetId(newDerive.getProdDeriveId());
      emplacementManager.updateObjectManager(e4, e4.getTerminale(), deriveEntite);
      assertTrue(prodDeriveManager.getEmplacementManager(newDerive).equals(e4));
      assertTrue(emplacementManager.findObjByEmplacementManager(e4, "ProdDerive").get(0).equals(newDerive));

      // retour incomplete
      final Calendar dateS = Calendar.getInstance();
      dateS.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10"));
      final Retour incomp1 = new Retour();
      incomp1.setTempMoyenne(new Float(20));
      incomp1.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp1, newDerive, emplacementManager.findByIdManager(4), null, null, null, null,
         u, "creation");
      assertTrue(prodDeriveManager.findByIdManager(newDerive.getProdDeriveId()).getObjetStatut().getStatut().equals("ENCOURS"));
      final Retour incomp2 = new Retour();
      incomp2.setTempMoyenne(new Float(22));
      incomp2.setDateSortie(dateS);
      retourManager.createOrUpdateObjectManager(incomp2, p2, emplacementManager.findByIdManager(3), null, null, null, null, u,
         "creation");
      assertTrue(prodDeriveManager.findByIdManager(p2.getProdDeriveId()).getObjetStatut().getStatut().equals("ENCOURS"));

      ImportHistorique ih = null;

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/importSubDerives.xls");

      List<ImportError> errors = new ArrayList<>();
      try(FileInputStream fis = new FileInputStream(file);){
         final Workbook wb = WorkbookFactory.create(fis);
         ih = importManager.importSubDeriveFileManager(itTest2, u, wb.getSheetAt(5), "OBS DERIVE TEST");
      }catch(final Exception re){
         errors = ((ErrorsInImportException) re).getErrors();
      }

      assertTrue(errors.size() == 6);
      assertNull(ih);

      // verif derives + annos + ncsf
      assertTrue(prodDeriveManager.findAllObjectsManager().size() == totDerives + 1);
      assertTrue(objetNonConformeDao.findAll().size() == totOncf);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(8)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(9)).size() == 0);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(10)).size() == 1);
      assertTrue(objetNonConformeDao.findByNonConformite(nonConformiteDao.findById(11)).size() == 0);
      assertTrue(annotationValeurManager.findAllObjectsManager().size() == totAnno);
      assertTrue(retourManager.findAllObjectsManager().size() == totRetour + 2);

      // derive in base + retour intacts
      p2 = prodDeriveManager.findByIdManager(2);
      assertTrue(p2.getQuantite() == null);
      assertTrue(p2.getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(retourManager.getRetoursForObjectManager(p2).size() == 1);
      final Retour complete1 = retourManager.getRetoursForObjectManager(p2).get(0);
      assertTrue(complete1.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      assertTrue(complete1.getDateRetour() == null);
      assertTrue(complete1.getTempMoyenne().equals(new Float(22)));

      final List<TKFantomableObject> fs = new ArrayList<>();

      // transformations
      final List<Transformation> trEch2 = transformationManager.findByEntiteObjetManager(deriveEntite, 2);
      assertTrue(trEch2.size() == 0);

      // update quantite + retour + newech
      newDerive = prodDeriveManager.findByIdManager(newDerive.getProdDeriveId());
      assertTrue(newDerive.getQuantite().equals(new Float(100)));
      assertTrue(newDerive.getObjetStatut().getStatut().equals("ENCOURS"));
      assertTrue(newDerive.getEmplacement() != null);
      e4 = emplacementManager.findByIdManager(4);
      assertFalse(e4.getVide());
      assertTrue(e4.getObjetId().equals(newDerive.getProdDeriveId()));
      assertTrue(e4.getEntite().equals(deriveEntite));
      assertTrue(retourManager.getRetoursForObjectManager(newDerive).size() == 1);
      final Retour complete2 = retourManager.getRetoursForObjectManager(newDerive).get(0);
      assertTrue(complete2.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/03/2015 10:10")));
      assertTrue(complete2.getDateRetour() == null);
      assertTrue(complete2.getTempMoyenne().equals(new Float(20)));

      // transformations
      final List<Transformation> trNewEch =
         transformationManager.findByEntiteObjetManager(deriveEntite, newDerive.getProdDeriveId());
      assertTrue(trNewEch.size() == 0);

      int i = 0;
      // erreur survenant lors du parsing Excel
      ImportError e = errors.get(i);
      assertTrue(e.getNbRow() == 1);
      assertTrue(e.getException() instanceof DeriveImportParentNotFoundException);
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getColonne().getNom().equals("code.parent"));
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getValeurAttendue().equals("PTRA.1.ZZ"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 7);
      assertTrue(e.getException() instanceof WrongImportValueException);
      assertTrue(((WrongImportValueException) e.getException()).getColonne().getNom().equals("evt.date"));
      assertTrue(((WrongImportValueException) e.getException()).getValeurAttendue().equals("Calendar"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 9);
      assertTrue(e.getException() instanceof DeriveImportParentNotFoundException);
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getColonne().getNom().equals("code.parent"));
      assertTrue(((DeriveImportParentNotFoundException) e.getException()).getValeurAttendue() == null);
      // erreur lancée par manager createDerives		
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 2);
      assertTrue(e.getException() instanceof ValidationException);
      assertTrue(((ValidationException) e.getException()).getErrors().get(0).getFieldError().getCode()
         .equals("date.validation.supDateRetour"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 6);
      assertTrue(e.getException() instanceof ObjectStatutException);
      assertTrue(((ObjectStatutException) e.getException()).getOperation().equals("transformation"));
      i++;
      e = errors.get(i);
      assertTrue(e.getNbRow() == 11);
      assertTrue(e.getException() instanceof TransformationQuantiteOverDemandException);
      assertTrue(((TransformationQuantiteOverDemandException) e.getException()).getQteDemandee().equals(new Float(67)));
      // les deux premiers transf ont ampute la qte avant exception
      assertTrue(((TransformationQuantiteOverDemandException) e.getException()).getQteRestante().equals(new Float(65)));

      // clean up
      retourManager.removeObjectManager(complete1);
      p2 = prodDeriveManager.findByIdManager(2);
      assertTrue(p2.getObjetStatut().getStatut().equals("STOCKE"));

      e4.setObjetId(null);
      e4.setEntite(null);
      e4.setVide(true);
      emplacementManager.updateObjectManager(e4, e4.getTerminale(), null);

      prodDeriveManager.updateObjectManager(newDerive, newDerive.getBanque(), newDerive.getProdType(), newDerive.getObjetStatut(),
         null, null, null, null, null, null, null, null, null, null, null, null, u, false, null, null);

      fs.add(newDerive);
      prodDeriveManager.removeObjectCascadeManager(newDerive, null, u, null);

      importTemplateManager.removeObjectManager(itTest2);

      cleanUpFantomes(fs);

      assertTrue(retourManager.findAllObjectsManager().size() == totRetour);
   }

   private ImportTemplate setDeriveImportTemplateUp(final Entite parentEntite, final Banque b){
      // creation du filtre
      final ImportTemplate itDerive = new ImportTemplate();
      itDerive.setNom("Import SubDerive");

      // itDerive.setDeriveParentEntite(entiteDao.findByNom("ProdDerive").get(0));
      final List<Entite> entites = new ArrayList<>();
      entites.add(entiteDao.findByNom("ProdDerive").get(0));
      final List<ImportColonne> colonnes = new ArrayList<>();

      final Champ ch1 = new Champ();
      ch1.setChampEntite(champEntiteDao.findById(79)); // code
      final ImportColonne ic1 = new ImportColonne();
      ic1.setNom("Code dérivé");
      ic1.setOrdre(1);
      ic1.setChamp(ch1);
      ic1.setImportTemplate(itDerive);
      final Champ ch2 = new Champ();
      final ImportColonne ic2 = new ImportColonne();
      ch2.setChampEntite(champEntiteDao.findById(78)); // type
      ic2.setNom("Type du dérivé");
      ic2.setOrdre(2);
      ic2.setChamp(ch2);
      ic2.setImportTemplate(itDerive);
      final Champ ch3 = new Champ();
      ch3.setChampEntite(champEntiteDao.findById(95)); // date transfo
      final ImportColonne ic3 = new ImportColonne();
      ic3.setNom("Date de transformation");
      ic3.setOrdre(3);
      ic3.setChamp(ch3);
      ic3.setImportTemplate(itDerive);
      final Champ ch4 = new Champ();
      final ImportColonne ic4 = new ImportColonne();
      ch4.setChampEntite(champEntiteDao.findById(263)); // conforme traitement raison
      ic4.setNom("Noconf trait");
      ic4.setOrdre(4);
      ic4.setChamp(ch4);
      ic4.setImportTemplate(itDerive);
      final Champ ch5 = new Champ();
      final ImportColonne ic5 = new ImportColonne();
      ch5.setChampEntite(champEntiteDao.findById(264)); // conforme cession raison
      ic5.setNom("Noconf cession");
      ic5.setOrdre(5);
      ic5.setChamp(ch5);
      ic5.setImportTemplate(itDerive);
      final Champ ch6 = new Champ();
      final ImportColonne ic6 = new ImportColonne();
      ch6.setChampAnnotation(champAnnotationManager.findByIdManager(17)); // Alphanumderive
      ic6.setNom("Alphanumderive");
      ic6.setOrdre(6);
      ic6.setChamp(ch6);
      ic6.setImportTemplate(itDerive);
      final Champ ch7 = new Champ();
      final ImportColonne ic7 = new ImportColonne();
      ch7.setChampAnnotation(champAnnotationManager.findByIdManager(18)); // Boolderive
      ic7.setNom("Boolderive");
      ic7.setOrdre(7);
      ic7.setChamp(ch7);
      ic7.setImportTemplate(itDerive);
      final Champ ch8 = new Champ();
      final ImportColonne ic8 = new ImportColonne();
      ch8.setChampEntite(champEntiteDao.findById(87)); // Emplacement
      ic8.setNom("Emplacement");
      ic8.setOrdre(8);
      ic8.setChamp(ch8);
      ic8.setImportTemplate(itDerive);
      colonnes.add(ic1);
      colonnes.add(ic2);
      colonnes.add(ic3);
      colonnes.add(ic4);
      colonnes.add(ic5);
      colonnes.add(ic6);
      colonnes.add(ic7);
      colonnes.add(ic8);

      itDerive.setDeriveParentEntite(parentEntite);

      itDerive.setBanque(b);
      if(importTemplateManager.findDoublonManager(itDerive)){
         ImportTemplate toDelete = null;
         for(final ImportTemplate tpl : importTemplateManager.findByBanqueManager(b)){
            if(tpl.getNom().equals("Import SubDerive")){
               toDelete = tpl;
               break;
            }
         }
         importTemplateManager.removeObjectManager(toDelete);
      }

      importTemplateManager.createObjectManager(itDerive, b, entites, colonnes);
      final Integer idT2 = itDerive.getImportTemplateId();
      // Vérification de la creation du filtre
      final ImportTemplate itTest2 = importTemplateManager.findByIdManager(idT2);
      assertNotNull(itTest2);
      assertTrue(itTest2.getNom().equals("Import SubDerive"));
      assertTrue(itTest2.getBanque().equals(b));
      assertTrue(importTemplateManager.getEntiteManager(itTest2).size() == 1);
      assertTrue(importColonneManager.findByImportTemplateManager(itTest2).size() == 8);

      return itTest2;
   }
}
