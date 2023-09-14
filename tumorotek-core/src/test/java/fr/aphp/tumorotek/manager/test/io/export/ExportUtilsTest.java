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
package fr.aphp.tumorotek.manager.test.io.export;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.export.ExportUtils;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ExportUtilsTest extends AbstractManagerTest4
{

   public ExportUtilsTest(){}

   @Autowired
   private ExportUtils exportUtils;

   @Autowired
   private MaladieManager maladieManager;

   @Autowired
   private PrelevementManager prelevementManager;

   @Autowired
   private EchantillonManager echantillonManager;

   @Autowired
   private ProdDeriveManager prodDeriveManager;

   @Autowired
   private CessionManager cessionManager;

   @Autowired
   private TableAnnotationManager tableAnnotationManager;

   @Autowired
   private UtilisateurManager utilisateurManager;

   @Autowired
   private OperationManager operationManager;

   @Test
   public void testCreateExcellWorkBook(){
      final HSSFWorkbook wb = exportUtils.createExcellWorkBook("test");
      assertNotNull(wb);
      assertTrue(wb.getNumberOfSheets() == 1);
      assertTrue(wb.getSheetName(0).equals("test"));
   }

   @Test
   public void testAddMaladieData(){
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final HSSFWorkbook wb = exportUtils.createExcellWorkBook("test");
      HSSFSheet sheet = wb.getSheetAt(0);
      final Maladie m1 = maladieManager.findByLibelleLikeManager("Non precise", true).get(0);

      final List<String> columns = new ArrayList<>();
      columns.add("Id Maladie");
      columns.add("Libellé");
      columns.add("Code");
      columns.add("Date début");
      columns.add("Date diagnostic");
      columns.add("Médecin maladie 1");
      columns.add("Médecin maladie 2");
      columns.add("Médecin maladie 3");
      columns.add("Id Patient");
      columns.add("NIP");
      columns.add("Nom patronymique");
      columns.add("Nom");
      columns.add("Prénom");
      columns.add("Date de naissance");
      columns.add("Sexe");
      columns.add("Ville de naissance");
      columns.add("Pays de naissance");
      columns.add("Etat");
      columns.add("Date état");
      columns.add("Date décès");
      columns.add("Médecin patient 1");
      columns.add("Médecin patient 2");
      columns.add("Médecin patient 3");
      columns.add("Organe");
      columns.add("Nb prelevements");
      columns.add("date et heure de saisie du patient");
      columns.add("Utilisateur ayant saisi le patient");
      columns.add("Alphanum1");
      columns.add("File1");
      sheet = exportUtils.addDataToRow(sheet, 0, 0, columns);

      final TableAnnotation ta = tableAnnotationManager.findByNomLikeManager("TABLE_PAT1", true).get(0);
      final Iterator<ChampAnnotation> it = tableAnnotationManager.getChampAnnotationsManager(ta).iterator();
      final List<ChampAnnotation> listeCas = new ArrayList<>();
      while(it.hasNext()){
         listeCas.add(it.next());
      }

      final HSSFRow row = sheet.createRow(1);
      final Utilisateur u = utilisateurManager.findByIdManager(1);
      exportUtils.addMaladieData(row, wb, 0, m1, listeCas, false, u);
      // On écrit le workbook dans le flux sortant
      try{
         wb.write(out);

         final byte[] data = out.toByteArray();
         final OutputStream output = new FileOutputStream("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");

         output.write(data);

         output.close();
      }catch(final IOException e){
         e.printStackTrace();
      }

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");
      assertTrue(file.length() > 0);
      file.delete();

      assertTrue(operationManager.findAllObjectsManager().size() == 20);
      final List<Operation> ops = operationManager.findByObjectManager(m1.getPatient());
      operationManager.removeObjectManager(ops.get(ops.size() - 1));
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
   }

   @Test
   public void testAddPrelevementData(){
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final HSSFWorkbook wb = exportUtils.createExcellWorkBook("test");
      HSSFSheet sheet = wb.getSheetAt(0);
      final Prelevement p1 = prelevementManager.findByIdManager(1);

      final List<String> columns = new ArrayList<>();
      columns.add("Id Prlvt");
      columns.add("Collection");
      columns.add("Code");
      columns.add("N° Labo");
      columns.add("Nature");
      columns.add("Date prlvt");
      columns.add("Type");
      columns.add("Stérile");
      columns.add("Conforme à l'arrivée");
      columns.add("Non conformité");
      columns.add("Etablissement");
      columns.add("Service");
      columns.add("Preleveur");
      columns.add("Type de conditionnement");
      columns.add("Nb conditionnements");
      columns.add("Milieu");
      columns.add("Statut juridique");
      columns.add("Date statut");
      columns.add("Date depart");
      columns.add("Transporteur");
      columns.add("Température");
      columns.add("Date arrivée");
      columns.add("Opérateur");
      columns.add("Quantité");
      columns.add("N° de dossier");
      columns.add("Diagnostic");
      columns.add("Nb échantillons total");
      columns.add("Nb échantillons restants");
      columns.add("Nb échantillons stockés");
      columns.add("Age au prélèvement");
      columns.add("Nb produits dérivés");
      columns.add("date et heure de saisie du prlvt");
      columns.add("Utilisateur ayant saisi le prlvt");
      columns.add("Alphanum2");
      columns.add("Bool1");
      columns.add("Link1");
      sheet = exportUtils.addDataToRow(sheet, 0, 0, columns);

      final TableAnnotation ta = tableAnnotationManager.findByNomLikeManager("TABLE_PREL1", true).get(0);
      final Iterator<ChampAnnotation> it = tableAnnotationManager.getChampAnnotationsManager(ta).iterator();
      final List<ChampAnnotation> listeCas = new ArrayList<>();
      while(it.hasNext()){
         listeCas.add(it.next());
      }

      final HSSFRow row = sheet.createRow(1);
      final Utilisateur u = utilisateurManager.findByIdManager(1);
      exportUtils.addPrelevementData(row, wb, 0, p1, listeCas, false, u);
      // On écrit le workbook dans le flux sortant
      try{
         wb.write(out);

         final byte[] data = out.toByteArray();
         final OutputStream output = new FileOutputStream("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");

         output.write(data);

         output.close();
      }catch(final IOException e){
         e.printStackTrace();
      }

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");
      assertTrue(file.length() > 0);
      file.delete();

      assertTrue(operationManager.findAllObjectsManager().size() == 20);
      final List<Operation> ops = operationManager.findByObjectManager(p1);
      operationManager.removeObjectManager(ops.get(ops.size() - 1));
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
   }

   @Test
   public void testAddEchantillonData(){
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final HSSFWorkbook wb = exportUtils.createExcellWorkBook("test");
      HSSFSheet sheet = wb.getSheetAt(0);
      final Echantillon e1 = echantillonManager.findByIdManager(1);

      final List<String> columns = new ArrayList<>();
      columns.add("Id Echan");
      columns.add("Collection");
      columns.add("Code");
      columns.add("Type");
      columns.add("Quantité");
      columns.add("Quantité Init");
      columns.add("Date stkg");
      columns.add("Délai");
      columns.add("Opérateur");
      columns.add("Emplacement");
      columns.add("Statut");
      columns.add("Qualité");
      columns.add("Mode");
      columns.add("Stérile");
      columns.add("Conforme Traitement");
      columns.add("Raison");
      columns.add("Conforme cession");
      columns.add("Raison");
      columns.add("Tumoral");
      columns.add("Latéralité");
      columns.add("Code organe 1");
      columns.add("Code organe 2");
      columns.add("Code organe 3");
      columns.add("Code lésionnel 1");
      columns.add("Code lésionnel 2");
      columns.add("Code lésionnel 3");
      columns.add("Code lésionnel 4");
      columns.add("Code lésionnel 5");
      columns.add("Nb produits dérivés");
      columns.add("date et heure de saisie de l'échan");
      columns.add("Utilisateur ayant saisi l'échan");
      columns.add("Bool2");
      columns.add("Date1");
      columns.add("Num1");
      columns.add("Texte1");
      columns.add("Thes1");
      sheet = exportUtils.addDataToRow(sheet, 0, 0, columns);

      final TableAnnotation ta = tableAnnotationManager.findByNomLikeManager("INCA_ECHAN1", true).get(0);
      final Iterator<ChampAnnotation> it = tableAnnotationManager.getChampAnnotationsManager(ta).iterator();
      final List<ChampAnnotation> listeCas = new ArrayList<>();
      while(it.hasNext()){
         listeCas.add(it.next());
      }

      final HSSFRow row = sheet.createRow(1);
      final Utilisateur u = utilisateurManager.findByIdManager(1);
      exportUtils.addEchantillonData(row, wb, 0, e1, listeCas, false, u);
      // On écrit le workbook dans le flux sortant
      try{
         wb.write(out);

         final byte[] data = out.toByteArray();
         final OutputStream output = new FileOutputStream("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");

         output.write(data);

         output.close();
      }catch(final IOException e){
         e.printStackTrace();
      }

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");
      assertTrue(file.length() > 0);
      file.delete();

      assertTrue(operationManager.findAllObjectsManager().size() == 20);
      final List<Operation> ops = operationManager.findByObjectManager(e1);
      operationManager.removeObjectManager(ops.get(ops.size() - 1));
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
   }

   @Test
   public void testAddProdDeriveData(){
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final HSSFWorkbook wb = exportUtils.createExcellWorkBook("test");
      HSSFSheet sheet = wb.getSheetAt(0);
      final ProdDerive d1 = prodDeriveManager.findByIdManager(1);

      final List<String> columns = new ArrayList<>();
      columns.add("Id Echan");
      columns.add("Collection");
      columns.add("Code");
      columns.add("Type");
      columns.add("Date transfo");
      columns.add("Quantite transfo");
      columns.add("N° labo");
      columns.add("Volume");
      columns.add("Volume init");
      columns.add("Concentration");
      columns.add("Quantité");
      columns.add("Quantité init");
      columns.add("Date stockage");
      columns.add("Preparation");
      columns.add("Qualite");
      columns.add("Opérateur");
      columns.add("Emplacement");
      columns.add("Statut");
      columns.add("Conforme Traitement");
      columns.add("Raison");
      columns.add("Conforme cession");
      columns.add("Raison");
      columns.add("Nb Dérivés");
      columns.add("date et heure de saisie du dérivé");
      columns.add("Utilisateur ayant saisi le dérivé");
      columns.add("AlphanumDerive");
      columns.add("BoolDerive");
      sheet = exportUtils.addDataToRow(sheet, 0, 0, columns);

      final TableAnnotation ta = tableAnnotationManager.findByNomLikeManager("TABLE_DERIVE1", true).get(0);
      final Iterator<ChampAnnotation> it = tableAnnotationManager.getChampAnnotationsManager(ta).iterator();
      final List<ChampAnnotation> listeCas = new ArrayList<>();
      while(it.hasNext()){
         listeCas.add(it.next());
      }

      final HSSFRow row = sheet.createRow(1);
      final Utilisateur u = utilisateurManager.findByIdManager(1);
      exportUtils.addProdDeriveData(row, wb, 0, d1, listeCas, false, u);
      // On écrit le workbook dans le flux sortant
      try{
         wb.write(out);

         final byte[] data = out.toByteArray();
         final OutputStream output = new FileOutputStream("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");

         output.write(data);

         output.close();
      }catch(final IOException e){
         e.printStackTrace();
      }

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");
      assertTrue(file.length() > 0);
      file.delete();

      assertTrue(operationManager.findAllObjectsManager().size() == 20);
      final List<Operation> ops = operationManager.findByObjectManager(d1);
      operationManager.removeObjectManager(ops.get(ops.size() - 1));
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
   }

   @Test
   public void testAddCessionData(){
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final HSSFWorkbook wb = exportUtils.createExcellWorkBook("test");
      HSSFSheet sheet = wb.getSheetAt(0);
      final Cession c1 = cessionManager.findByIdManager(2);

      final List<String> columns = new ArrayList<>();
      columns.add("Id Cession");
      columns.add("Collection");
      columns.add("Numéro");
      columns.add("Type");
      columns.add("Echantillons");
      columns.add("Nb Echantillons");
      columns.add("Dérivés");
      columns.add("Nb dérivés");
      columns.add("Demandeur");
      columns.add("Date demande");
      columns.add("Contrat");
      columns.add("Etude titre");
      columns.add("Examen");
      columns.add("Motif");
      columns.add("Description");
      columns.add("Etablissement");
      columns.add("Service");
      columns.add("Destinataire");
      columns.add("Date validation");
      columns.add("Date destruction");
      columns.add("Statut");
      columns.add("Opérateur");
      columns.add("Stérile");
      columns.add("Date départ");
      columns.add("Date arrivée");
      columns.add("Transporteur");
      columns.add("Températeur");
      columns.add("Observations");
      columns.add("date et heure de saisie de la cession");
      columns.add("Utilisateur ayant saisi la cession");
      sheet = exportUtils.addDataToRow(sheet, 0, 0, columns);

      final HSSFRow row = sheet.createRow(1);
      final Utilisateur u = utilisateurManager.findByIdManager(1);
      exportUtils.addCessionData(row, wb, 0, c1, null, u);
      // On écrit le workbook dans le flux sortant
      try{
         wb.write(out);

         final byte[] data = out.toByteArray();
         final OutputStream output = new FileOutputStream("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");

         output.write(data);

         output.close();
      }catch(final IOException e){
         e.printStackTrace();
      }

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/export/test.xls");
      assertTrue(file.length() > 0);
      file.delete();

      assertTrue(operationManager.findAllObjectsManager().size() == 20);
      final List<Operation> ops = operationManager.findByObjectManager(c1);
      operationManager.removeObjectManager(ops.get(ops.size() - 1));
      assertTrue(operationManager.findAllObjectsManager().size() == 19);
   }

   @Test
   public void testDateRenderer() throws ParseException{
      final Calendar date = Calendar.getInstance();
      date.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 14:57:01"));
      assertTrue(exportUtils.dateRenderer(date).equals("17/09/2006 14:57"));

      date.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/2006 00:00:00"));
      assertTrue(exportUtils.dateRenderer(date).equals("17/09/2006"));

      assertTrue(exportUtils.dateRenderer(date.getTime()).equals("17/09/2006"));

      assertNull(exportUtils.dateRenderer(null));
   }

   @Test
   public void testBooleanLitteralFormatter(){
      assertTrue(exportUtils.booleanLitteralFormatter(true).equals("Oui"));
      assertTrue(exportUtils.booleanLitteralFormatter(false).equals("Non"));
      assertNull(exportUtils.booleanLitteralFormatter(null));
   }

   @Test
   public void testAddDataToRowCsv(){
      StringWriter sw = null;
      BufferedWriter bw = null;

      final List<String> values = new ArrayList<>();
      values.add("1");
      values.add("");
      values.add("3");
      values.add(null);
      values.add("aa");

      try{
         sw = new StringWriter();
         bw = new BufferedWriter(sw);

         exportUtils.addDataToRowCSV(bw, 3, values, "|", "|\n");

         bw.flush();

         assertTrue(sw.getBuffer().toString().equals("|||1||3||aa|\n"));

         bw.close();
         sw.close();
      }catch(final IOException ioe){
         try{
            bw.close();
         }catch(final Exception e){
            bw = null;
         }
         try{
            sw.close();
         }catch(final Exception e){
            sw = null;
         }
      }
   }

   /**
    * teste la regexp qui suppr les 7 caractères problématiques 
    * dans la création des noms des workbook sheets 
    * @since 2.2.1 TK-255
    * @see https://www.accountingweb.com/technology/excel/seven-characters-you-cant-use-in-worksheet-names
    */
   @Test
   public void testXlsxSheetNameInfamousChars(){
      String sheetname = "\\/*[]:?";
      assertTrue(sheetname.replaceAll(ValidationUtilities.SHEETNAME_INFAMOUSCHARS, "").isEmpty());
   }
}
