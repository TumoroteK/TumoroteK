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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.exception.BadFileFormatException;
import fr.aphp.tumorotek.manager.exception.ErrorsInImportException;
import fr.aphp.tumorotek.manager.exception.FormulaException;
import fr.aphp.tumorotek.manager.exception.HeaderException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.impl.io.imports.DerivesImportBatches;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectDuo;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.io.imports.ImportError;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.io.imports.ImportManager;
import fr.aphp.tumorotek.manager.io.imports.ImportProperties;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ImportManagerTest extends AbstractManagerTest4
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
	private NatureDao natureDao;
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
	private ItemDao itemDao;
	@Autowired
	private AnnotationValeurManager annotationValeurManager;
	@Autowired
	private EchantillonManager echantillonManager;
	@Autowired
	private ProdDeriveManager prodDeriveManager;
	@Autowired
	private EmplacementManager emplacementManager;
	@Autowired
	private CodeAssigneManager codeAssigneManager;
	@Autowired
	private ObjetStatutDao objetStatutDao;
	@Autowired
	private NonConformiteDao nonConformiteDao;
	@Autowired
	private ObjetNonConformeDao objetNonConformeDao;
	@Autowired
	private ConformiteTypeDao conformiteTypeDao;
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	@Autowired
	private TransformationDao transformationDao;

	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public ImportManagerTest(){}

	/**
	 * Test de la méthode getCellContent().
	 */
	@Test
	public void testGetCellContent(){
		try{
			// HSSFWorkbook xls test
			File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/testCellContents.xls");
			FileInputStream fis = new FileInputStream(file);
			Workbook wb = new HSSFWorkbook(fis);
			getCellContentsTypeTest(wb);
			fis.close();
			// XSSFWorkbook xlsx test
			file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/testCellContents.xlsx");
			fis = new FileInputStream(file);
			wb = new XSSFWorkbook(fis);
			getCellContentsTypeTest(wb);
			fis.close();
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	private void getCellContentsTypeTest(final Workbook wb){

		Sheet sheet;
		Iterator<Row> rit;
		Row row = null;

		sheet = wb.getSheetAt(0);

		final FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		/*parcourt les lignes*/
		rit = sheet.rowIterator();
		row = rit.next();
		row = rit.next();

		assertTrue(importManager.getCellContent(row.getCell(0), false, null).equals("MEUNIER"));
		assertTrue(importManager.getCellContent(row.getCell(0), false, evaluator).equals("MEUNIER"));
		assertTrue(importManager.getCellContent(row.getCell(0), true, evaluator).equals("MEUNIER"));
		assertTrue(importManager.getCellContent(row.getCell(1), true, evaluator).equals("11/10/2009"));
		assertTrue(importManager.getCellContent(row.getCell(2), true, evaluator).equals("11/10/2009 10:00"));
		assertTrue(importManager.getCellContent(row.getCell(2), true, null).equals("11/10/2009 10:00"));
		assertTrue(importManager.getCellContent(row.getCell(3), true, null).equals("08/10/1983"));
		assertTrue(importManager.getCellContent(row.getCell(4), true, evaluator).equals("08/10/1983 11:30"));
		assertTrue(importManager.getCellContent(row.getCell(5), false, null).equals("1"));
		assertTrue(importManager.getCellContent(row.getCell(5), false, evaluator).equals("1"));
		assertTrue(importManager.getCellContent(row.getCell(6), false, null).equals("1"));
		assertTrue(importManager.getCellContent(row.getCell(6), false, evaluator).equals("1"));
		assertTrue(importManager.getCellContent(row.getCell(7), false, null).equals("1569"));
		assertTrue(importManager.getCellContent(row.getCell(7), false, evaluator).equals("1569"));
		assertTrue(importManager.getCellContent(row.getCell(8), false, null).equals("14.5"));
		assertTrue(importManager.getCellContent(row.getCell(8), false, evaluator).equals("14.5"));
		assertTrue(importManager.getCellContent(row.getCell(9), false, null).equals("11"));
		assertTrue(importManager.getCellContent(row.getCell(9), false, evaluator).equals("11"));
		assertTrue(importManager.getCellContent(row.getCell(10), false, null).equals("300004638"));
		assertTrue(importManager.getCellContent(row.getCell(10), false, evaluator).equals("300004638"));
		boolean catched = false;
		try{
			importManager.getCellContent(row.getCell(11), false, null);
		}catch(final FormulaException fe){
			catched = true;
			assertTrue(fe.getMessage().matches("import.formula.error: CONCATENATE.*"));
		}
		assertTrue(catched);
		assertTrue(importManager.getCellContent(row.getCell(11), false, evaluator).equals("MEUNIER-1"));
		// blank
		assertTrue(importManager.getCellContent(row.getCell(12), false, evaluator).equals(""));
		assertTrue(importManager.getCellContent(row.getCell(12), false, null).equals(""));
		// blank formula
		assertTrue(importManager.getCellContent(row.getCell(13), false, evaluator).equals(""));
		catched = false;
		try{
			importManager.getCellContent(row.getCell(13), false, null);
		}catch(final FormulaException fe){
			catched = true;
			assertTrue(fe.getMessage().matches("import.formula.error: IF.*"));
		}
		assertTrue(catched);
		// num formula
		assertTrue(importManager.getCellContent(row.getCell(14), false, evaluator).equals("2"));
		catched = false;
		try{
			importManager.getCellContent(row.getCell(14), false, null);
		}catch(final FormulaException fe){
			catched = true;
			assertTrue(fe.getMessage().matches("import.formula.error: SUM.*"));
		}
		assertTrue(catched);
		// bool formula
		assertTrue(importManager.getCellContent(row.getCell(15), false, evaluator).equals("1"));
		catched = false;
		try{
			importManager.getCellContent(row.getCell(15), false, null);
		}catch(final FormulaException fe){
			catched = true;
			assertTrue(fe.getMessage().matches("import.formula.error: TRUE.*"));
		}
		assertTrue(catched);
		// date formula
		assertTrue(importManager.getCellContent(row.getCell(16), true, evaluator).equals("03/05/2006"));
		catched = false;
		try{
			importManager.getCellContent(row.getCell(16), false, null);
		}catch(final FormulaException fe){
			catched = true;
			assertTrue(fe.getMessage().matches("import.formula.error: DATE.*"));
		}
		assertTrue(catched);
	}

	/**
	 * Test de la méthode initColumnsHeadersManager().
	 */
	@Test
	public void testInitColumnsHeadersManager(){
		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		Hashtable<String, Integer> headers = new Hashtable<>();
		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			headers = importManager.initColumnsHeadersManager(row);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}

		assertTrue(headers.size() == 31);
		assertTrue(headers.get("Nip patient") == 0);
		assertTrue(headers.get("Date naissance") == 4);
		assertTrue(headers.get("Nature") == 8);
		assertTrue(headers.get("Bool1") == 12);

		/*********************************************************/
		/***** Test avec un fichier où une colonne n'a pas header*/
		/*********************************************************/
		file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/badHeaderImport.xls");

		Boolean catched = false;
		headers = new Hashtable<>();
		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			headers = importManager.initColumnsHeadersManager(row);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}catch(final HeaderException he){
			catched = true;
			assertTrue(he.getCol() == 3);
		}
		assertTrue(catched);

	}

	/**
	 * Test de la méthode initImportColonnesManager().
	 */
	@Test
	public void testInitImportColonnesManager(){
		File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");
		final ImportTemplate it = importTemplateManager.findByIdManager(1);
		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		Hashtable<String, Integer> headers = new Hashtable<>();
		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			headers = importManager.initColumnsHeadersManager(row);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}

		Hashtable<Entite, List<ImportColonne>> entitesColonnes = importManager.initImportColonnesManager(headers, it);

		assertTrue(entitesColonnes.size() == 5);
		final Entite e1 = entiteDao.findById(1);
		List<ImportColonne> cols = entitesColonnes.get(e1);
		assertTrue(cols.size() == 6);
		assertTrue(cols.get(0).getNom().equals("Nip patient"));

		final Entite e2 = entiteDao.findById(2);
		cols = entitesColonnes.get(e2);
		assertTrue(cols.size() == 10);
		assertTrue(cols.get(0).getNom().equals("Code prlvt"));
		assertTrue(cols.get(8).getNom().equals("version cTNM"));

		assertTrue(importManager.initImportColonnesManager(null, it).size() == 0);

		assertTrue(importManager.initImportColonnesManager(headers, null).size() == 0);

		assertTrue(importManager.initImportColonnesManager(null, null).size() == 0);

		file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/badFormatImport.xls");

		/*********************************************************/
		/***** Test avec un fichier où il manque une colonne *****/
		/*********************************************************/
		headers = new Hashtable<>();
		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			headers = importManager.initColumnsHeadersManager(row);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}

		Boolean catched = false;
		try{
			entitesColonnes = importManager.initImportColonnesManager(headers, it);
		}catch(final BadFileFormatException bdfe){
			catched = true;
		}
		assertTrue(catched);
	}

	/**
	 * Test de la méthode extractValuesForOneThesaurus().
	 */
	@Test
	public void testExtractValuesForOneThesaurus(){
		final ImportTemplate it = importTemplateManager.findByIdManager(1);
		final ChampEntite ceNature = champEntiteDao.findById(111);
		Hashtable<String, Object> values = importManager.extractValuesForOneThesaurus(ceNature, it.getBanque());
		assertTrue(values.size() == 3);
		final Nature nat = (Nature) values.get("TISSU");
		assertNotNull(nat);
		assertTrue(nat.getNom().equals("TISSU"));

		final ChampEntite ceCollab = champEntiteDao.findById(199);
		values = importManager.extractValuesForOneThesaurus(ceCollab, it.getBanque());
		assertTrue(values.size() == 6);
		final Collaborateur c = (Collaborateur) values.get("VIAL CHRISTOPHE");
		assertNotNull(c);
		assertTrue(c.getNom().equals("VIAL"));

		final ChampEntite ceService = champEntiteDao.findById(194);
		values = importManager.extractValuesForOneThesaurus(ceService, it.getBanque());
		assertTrue(values.size() == 4);
		final Service s = (Service) values.get("SAINT LOUIS - ANAPATH");
		assertNotNull(s);
		assertTrue(s.getNom().equals("ANAPATH"));

		assertTrue(importManager.extractValuesForOneThesaurus(null, it.getBanque()).size() == 0);

		final ChampEntite confArrivee = champEntiteDao.findById(257);
		values = importManager.extractValuesForOneThesaurus(confArrivee, it.getBanque());
		assertTrue(values.size() == 2);

		assertTrue(values.contains(nonConformiteDao.findById(1)));
		assertTrue(values.contains(nonConformiteDao.findById(2)));

		final ChampEntite confCessDerive = champEntiteDao.findById(264);
		values = importManager.extractValuesForOneThesaurus(confCessDerive, it.getBanque());
		assertTrue(values.size() == 2);
		assertTrue(values.contains(nonConformiteDao.findById(10)));
		assertTrue(values.contains(nonConformiteDao.findById(11)));
	}

	/**
	 * Test de la méthode extractValuesForOneAnnotationThesaurus().
	 */
	@Test
	public void testExtractValuesForOneAnnotationThesaurus(){
		final Banque b1 = importTemplateManager.findByIdManager(1).getBanque();
		final ChampAnnotation ca = champAnnotationManager.findByNomManager("009 : version cTNM").get(0);
		Hashtable<String, Object> values = importManager.extractValuesForOneAnnotationThesaurus(ca, b1);
		assertTrue(values.size() == 5);
		final Item it = (Item) values.get("X");
		assertNotNull(it);
		assertTrue(it.getLabel().equals("X"));

		// thes multiple
		final ChampAnnotation ca2 = champAnnotationManager.findByNomManager("Thes2").get(0);
		values = importManager.extractValuesForOneAnnotationThesaurus(ca2, b1);
		assertTrue(values.size() == 3);
		assertNotNull(values.get("item1-2"));
		assertNotNull(values.get("item2-2"));
		assertNotNull(values.get("item3-2-max"));

		assertTrue(importManager.extractValuesForOneAnnotationThesaurus(null, b1).size() == 0);

		assertTrue(importManager.extractValuesForOneAnnotationThesaurus(ca, null).size() == 0);

		assertTrue(importManager.extractValuesForOneAnnotationThesaurus(null, null).size() == 0);
	}

	/**
	 * Test de la méthode generateThesaurusHashtable().
	 */
	@Test
	public void testGenerateThesaurusHashtable(){
		Hashtable<Object, Hashtable<String, Object>> thesaurus = new Hashtable<>();
		final ImportTemplate it = importTemplateManager.findByIdManager(1);
		thesaurus = importManager.generateThesaurusHashtable(it);
		assertTrue(thesaurus.size() == 8);
		assertTrue(thesaurus.containsKey(entiteDao.findByNom("Nature").get(0)));
		assertTrue(thesaurus.containsKey(entiteDao.findByNom("ConsentType").get(0)));
		assertTrue(thesaurus.containsKey(entiteDao.findByNom("Collaborateur").get(0)));
		assertTrue(thesaurus.get(entiteDao.findByNom("ConsentType").get(0)).size() == 2);
		assertTrue(thesaurus.get(entiteDao.findByNom("Risque").get(0)).size() == 2);

		thesaurus = importManager.generateThesaurusHashtable(null);
		assertTrue(thesaurus.size() == 0);
	}

	/**
	 * Test de la méthode generateAnnotationsThesaurusHashtable().
	 */
	@Test
	public void testGenerateAnnotationsThesaurusHashtable(){
		Hashtable<ChampAnnotation, Hashtable<String, Object>> thesaurus =
				new Hashtable<>();
		final ImportTemplate it = importTemplateManager.findByIdManager(1);
		thesaurus = importManager.generateAnnotationsThesaurusHashtable(it);
		assertTrue(thesaurus.size() == 2);
		assertTrue(thesaurus.containsKey(champAnnotationManager.findByNomManager("009 : version cTNM").get(0)));
		assertTrue(thesaurus.get(champAnnotationManager.findByNomManager("009 : version cTNM").get(0)).size() == 5);

		assertTrue(thesaurus.containsKey(champAnnotationManager.findByNomManager("Thes2").get(0)));
		assertTrue(thesaurus.get(champAnnotationManager.findByNomManager("Thes2").get(0)).size() == 3);

		thesaurus = importManager.generateAnnotationsThesaurusHashtable(null);
		assertTrue(thesaurus.size() == 0);
	}

	/**
	 * Test de la méthode setPropertyValueForObject().
	 */
	@Test
	public void testSetPropertyValueForObject(){
		final Prelevement prlvt = new Prelevement();

		// test sur un string
		final ImportColonne col = new ImportColonne();
		col.setNom("TEST");
		ChampEntite ce = champEntiteDao.findById(23);
		importManager.setPropertyValueForObject("CODE", ce, prlvt, col);
		assertTrue(prlvt.getCode().equals("CODE"));

		// test sur un integer
		ce = champEntiteDao.findById(34);
		importManager.setPropertyValueForObject("5", ce, prlvt, col);
		assertTrue(prlvt.getConditNbr() == 5);
		Boolean catched = false;
		try{
			importManager.setPropertyValueForObject("tcg", ce, prlvt, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test sur un float
		ce = champEntiteDao.findById(40);
		importManager.setPropertyValueForObject("5.5", ce, prlvt, col);
		assertTrue(prlvt.getQuantite() == 5.5);
		try{
			importManager.setPropertyValueForObject("tcg", ce, prlvt, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test sur un boolean
		ce = champEntiteDao.findById(47);
		importManager.setPropertyValueForObject("1", ce, prlvt, col);
		assertTrue(prlvt.getSterile());
		try{
			importManager.setPropertyValueForObject("tcg", ce, prlvt, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test sur une date
		ce = champEntiteDao.findById(27);
		importManager.setPropertyValueForObject("10/11/2005", ce, prlvt, col);
		assertTrue(format.format(prlvt.getConsentDate()).equals("2005-11-10"));
		try{
			importManager.setPropertyValueForObject("tcg", ce, prlvt, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test sur un calendar
		ce = champEntiteDao.findById(30);
		importManager.setPropertyValueForObject("10/11/2005 10:00", ce, prlvt, col);
		assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2005-11-10"));
		importManager.setPropertyValueForObject("10/11/2005", ce, prlvt, col);
		assertTrue(format.format(prlvt.getDatePrelevement().getTime()).equals("2005-11-10"));
		try{
			importManager.setPropertyValueForObject("tcg", ce, prlvt, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test avec un thesaurus
		final Nature n = natureDao.findById(1);
		ce = champEntiteDao.findById(24);
		importManager.setPropertyValueForObject(n, ce, prlvt, col);
		assertTrue(prlvt.getNature().equals(n));

		// test avec un emplacement
		final Emplacement empl = emplacementManager.findByIdManager(1);
		final Echantillon e = new Echantillon();
		ce = champEntiteDao.findById(57);
		importManager.setPropertyValueForObject(empl, ce, e, col);
		assertNotNull(e.getEmplacement());

		// test avec un null
		ce = champEntiteDao.findById(23);
		importManager.setPropertyValueForObject(null, ce, prlvt, col);
		assertNull(prlvt.getCode());
	}

	/**
	 * Test de la méthode setPropertyValueForAnnotationValeur().
	 */
	@Test
	public void testSetPropertyValueForAnnotationValeur(){
		final ImportColonne col = new ImportColonne();
		col.setNom("TEST");
		AnnotationValeur av = new AnnotationValeur();
		// test sur un string
		ChampAnnotation ca = champAnnotationManager.findByNomManager("Alphanum1").get(0);
		importManager.setPropertyValueForAnnotationValeur("CODE", ca, av, col);
		assertTrue(av.getAlphanum().equals("CODE"));

		// test sur un integer
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("Num1").get(0);
		importManager.setPropertyValueForAnnotationValeur("50.5", ca, av, col);
		assertTrue(av.getAlphanum().equals("50.5"));

		// test sur un boolean
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("Bool1").get(0);
		importManager.setPropertyValueForAnnotationValeur("1", ca, av, col);
		assertTrue(av.getBool());
		Boolean catched = false;
		try{
			importManager.setPropertyValueForAnnotationValeur("sfd", ca, av, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test sur un calendar
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("Date1").get(0);
		importManager.setPropertyValueForAnnotationValeur("10/11/2005 10:00", ca, av, col);
		assertTrue(format.format(av.getDate().getTime()).equals("2005-11-10"));
		importManager.setPropertyValueForAnnotationValeur("10/11/2005", ca, av, col);
		assertTrue(format.format(av.getDate().getTime()).equals("2005-11-10"));
		try{
			importManager.setPropertyValueForAnnotationValeur("qsc", ca, av, col);
		}catch(final WrongImportValueException wve){
			catched = true;
		}
		assertTrue(catched);
		catched = false;

		// test avec un text
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("Texte1").get(0);
		importManager.setPropertyValueForAnnotationValeur("hhdhdhd", ca, av, col);
		assertTrue(av.getTexte().equals("hhdhdhd"));

		// test avec un hyperlien
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("Link1").get(0);
		importManager.setPropertyValueForAnnotationValeur("www.google.fr", ca, av, col);
		assertTrue(av.getAlphanum().equals("www.google.fr"));

		// test avec un thesaurus
		Item it = itemDao.findById(12);
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("009 : version cTNM").get(0);
		importManager.setPropertyValueForAnnotationValeur(it, ca, av, col);
		assertTrue(av.getItem().equals(it));

		// test avec un thesaurusM
		it = itemDao.findById(5);
		av = new AnnotationValeur();
		ca = champAnnotationManager.findByNomManager("Thes2").get(0);
		importManager.setPropertyValueForAnnotationValeur(it, ca, av, col);
		assertTrue(av.getItem().equals(it));
	}

	/**
	 * Test de la méthode setPropertyForImportColonne().
	 */
	@Test
	public void testSetPropertyForImportColonne(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/badImport.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur le patient
			final Patient pat = new Patient();
			ImportColonne col = importColonneManager.findByIdManager(1);
			importManager.setPropertyForImportColonne(pat, col, row, properties);
			assertTrue(pat.getNip().equals("152115453"));
			col = importColonneManager.findByIdManager(4);
			importManager.setPropertyForImportColonne(pat, col, row, properties);
			assertTrue(pat.getNip().equals("152115453"));
			assertTrue(format.format(pat.getDateNaissance()).equals("1983-07-25"));

			// test sur le prélèvement
			final Prelevement prlvt = new Prelevement();
			col = importColonneManager.findByIdManager(6);
			importManager.setPropertyForImportColonne(prlvt, col, row, properties);
			assertTrue(prlvt.getCode().equals("PRLVT456"));
			col = importColonneManager.findByIdManager(7);
			importManager.setPropertyForImportColonne(prlvt, col, row, properties);
			assertTrue(prlvt.getNature().getNom().equals("SANG"));
			col = importColonneManager.findByIdManager(9);
			importManager.setPropertyForImportColonne(prlvt, col, row, properties);
			assertTrue(prlvt.getPreleveur().getNom().equals("VIAL"));

			// test sur l'echantillon
			final Echantillon echan = new Echantillon();
			col = importColonneManager.findByIdManager(26);
			importManager.setPropertyForImportColonne(echan, col, row, properties);
			assertNotNull(echan.getEmplacement());
			assertNotNull(echan.getEmplacement().getEmplacementId());
			assertTrue(echan.getEmplacement().getPosition() == 10);

			// on se place sur la 3eme ligne
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(26);
			importManager.setPropertyForImportColonne(echan, col, row, properties);
			assertNull(echan.getEmplacement());

			// on se place sur la 4eme ligne
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(26);
			importManager.setPropertyForImportColonne(echan, col, row, properties);
			assertNotNull(echan.getEmplacement());
			assertNull(echan.getEmplacement().getEmplacementId());
			assertTrue(echan.getEmplacement().getPosition() == 12);

			// on se place sur la 7 eme ligne pour vérifier 
			// les exceptions
			row = (HSSFRow) rit.next();
			row = (HSSFRow) rit.next();
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(4);
			Boolean catched = false;
			try{
				importManager.setPropertyForImportColonne(pat, col, row, properties);
			}catch(final WrongImportValueException wve){
				catched = true;
			}
			assertTrue(catched);
			catched = false;

			col = importColonneManager.findByIdManager(8);
			try{
				importManager.setPropertyForImportColonne(pat, col, row, properties);
			}catch(final WrongImportValueException wve){
				catched = true;
			}
			assertTrue(catched);
			catched = false;

			col = importColonneManager.findByIdManager(26);
			try{
				importManager.setPropertyForImportColonne(echan, col, row, properties);
			}catch(final WrongImportValueException wve){
				catched = true;
			}
			assertTrue(catched);
			catched = false;

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setCodeAssigneForEchantillon().
	 */
	@Test
	public void testSetCodeAssigneForEchantillon(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1ere ligne
			final Echantillon echan = new Echantillon();
			//ImportColonne col = importColonneManager.findByIdManager(98);
			importManager.setCodeAssigneForEchantillon(echan, importColonneManager.findByIdManager(98), row, properties);
			importManager.setCodeAssigneForEchantillon(echan, importColonneManager.findByIdManager(99), row, properties);
			final Iterator<CodeAssigne> echanCodesAssignesIterator = echan.getCodesAssignes().iterator();
			CodeAssigne echanCodesAssignes = echanCodesAssignesIterator.next();

			//assertNotNull(echan.getCodeOrganeExport());
			assertTrue(echanCodesAssignes.getCode().equals("MOELLE OSSEUSE"));
			assertTrue(echanCodesAssignes.getLibelle().equals("grave"));
			assertTrue(echanCodesAssignes.getIsOrgane());
			assertFalse(echanCodesAssignes.getIsMorpho());
			//col = importColonneManager.findByIdManager(99);
			echanCodesAssignes = echanCodesAssignesIterator.next();
			// assertNotNull(echan.getCodeLesExport());
			assertEquals("LAM3", echanCodesAssignes.getCode());
			assertTrue(echanCodesAssignes.getLibelle().equals("Maladie"));
			assertTrue(echanCodesAssignes.getIsMorpho());
			assertFalse(echanCodesAssignes.getIsOrgane());

			// test sur la 2ème ligne
			row = (HSSFRow) rit.next();
			final Echantillon echan2 = new Echantillon();
			ImportColonne col = importColonneManager.findByIdManager(98);
			importManager.setCodeAssigneForEchantillon(echan2, col, row, properties);
			// assertNull(echan2.getCodeOrganeExport());
			assertTrue(echan2.getCodesAssignes().isEmpty());
			col = importColonneManager.findByIdManager(99);
			importManager.setCodeAssigneForEchantillon(echan2, col, row, properties);
			// assertNull(echan2.getCodeLesExport());
			assertTrue(echan2.getCodesAssignes().isEmpty());

			// test sur la 3ème ligne
			row = (HSSFRow) rit.next();
			final Echantillon echan3 = new Echantillon();
			col = importColonneManager.findByIdManager(98);
			importManager.setCodeAssigneForEchantillon(echan3, col, row, properties);
			//assertNotNull(echan3.getCodeOrganeExport());
			assertTrue(echan3.getCodesAssignes().iterator().next().getCode().equals("SANG SAI"));
			assertNull(echan3.getCodesAssignes().iterator().next().getLibelle());
			col = importColonneManager.findByIdManager(99);
			importManager.setCodeAssigneForEchantillon(echan3, col, row, properties);
			//assertNotNull(echan3.getCodeLesExport());
			assertTrue(echan3.getCodesAssignes().iterator().next().getCode().equals("rien"));
			assertTrue(echan3.getCodesAssignes().iterator().next().getLibelle().equals("rien"));

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setRisquesForPrelevement().
	 */
	@Test
	public void testSetRisquesForPrelevement(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1ere ligne
			final Prelevement prlvt = new Prelevement();
			ImportColonne col = importColonneManager.findByIdManager(100);
			importManager.setRisquesForPrelevement(prlvt, col, row, properties);
			final Iterator<Risque> it = prlvt.getRisques().iterator();
			final Risque r = it.next();
			assertTrue(r.getNom().equals("HIV"));
			final Risque r2 = it.next();
			assertEquals("GRIPPE A", r2.getNom());

			// test sur la 2ème ligne
			row = (HSSFRow) rit.next();
			final Prelevement prlvt2 = new Prelevement();
			col = importColonneManager.findByIdManager(100);
			importManager.setRisquesForPrelevement(prlvt2, col, row, properties);
			// assertNull(echan2.getCodeOrganeExport());
			assertTrue(prlvt2.getRisques().isEmpty());

			// test sur la 3ème ligne
			row = (HSSFRow) rit.next();
			final Prelevement prlvt3 = new Prelevement();
			col = importColonneManager.findByIdManager(100);
			importManager.setRisquesForPrelevement(prlvt3, col, row, properties);
			//assertNotNull(echan3.getCodeOrganeExport());
			assertTrue(prlvt3.getRisques().iterator().next().getNom().equals("HIV"));

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setNonConformites().
	 */
	@Test
	public void testSetNonConformites(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1ere ligne
			final Echantillon echan = new Echantillon();
			echan.setCode("1");
			ImportColonne col = importColonneManager.findByIdManager(102);
			final Map<TKAnnotableObject, List<NonConformite>> ncfsEchanTrait = new HashMap<>();
			importManager.setNonConformites(echan, col, row, properties, ncfsEchanTrait);
			assertTrue(ncfsEchanTrait.size() == 1);
			assertTrue(ncfsEchanTrait.get(echan).isEmpty());

			// test sur la 2ème ligne
			row = (HSSFRow) rit.next();
			final Echantillon echan2 = new Echantillon();
			echan2.setCode("2");
			col = importColonneManager.findByIdManager(102);
			importManager.setNonConformites(echan2, col, row, properties, ncfsEchanTrait);
			assertTrue(ncfsEchanTrait.size() == 2);
			assertTrue(ncfsEchanTrait.get(echan2).isEmpty());

			// test sur la 3ème ligne
			row = (HSSFRow) rit.next();
			final Echantillon echan3 = new Echantillon();
			echan3.setCode("3");
			col = importColonneManager.findByIdManager(102);
			importManager.setNonConformites(echan3, col, row, properties, ncfsEchanTrait);
			assertTrue(ncfsEchanTrait.size() == 3);
			assertTrue(ncfsEchanTrait.get(echan3).size() == 2);
			assertTrue(ncfsEchanTrait.get(echan3).contains(nonConformiteDao.findById(4)));
			assertTrue(ncfsEchanTrait.get(echan3).contains(nonConformiteDao.findById(5)));

			// test sur la 4ème ligne
			row = (HSSFRow) rit.next();
			final Echantillon echan4 = new Echantillon();
			echan4.setCode("4");
			col = importColonneManager.findByIdManager(102);
			importManager.setNonConformites(echan4, col, row, properties, ncfsEchanTrait);
			assertTrue(ncfsEchanTrait.size() == 4);
			assertTrue(ncfsEchanTrait.get(echan4).size() == 1);
			assertTrue(ncfsEchanTrait.get(echan4).contains(nonConformiteDao.findById(5)));

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setPropertyForAnnotationColonne().
	 */
	@Test
	public void testSetPropertyForAnnotationColonne(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/badImport.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));
			properties.setImportTemplate(template);

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1ere ligne
			ImportColonne col = importColonneManager.findByIdManager(10);
			List<AnnotationValeur> avs = importManager.setPropertyForAnnotationColonne(col, row, properties);
			assertTrue(avs.size() == 1);
			assertTrue(avs.get(0).getAlphanum().equals("hfj"));
			col = importColonneManager.findByIdManager(11);
			avs = importManager.setPropertyForAnnotationColonne(col, row, properties);
			assertTrue(avs.size() == 1);
			assertTrue(avs.get(0).getBool());
			col = importColonneManager.findByIdManager(17);
			avs = importManager.setPropertyForAnnotationColonne(col, row, properties);
			assertTrue(avs.size() == 1);
			assertTrue(avs.get(0).getAlphanum().equals("www.google.fr"));
			col = importColonneManager.findByIdManager(18);
			avs = importManager.setPropertyForAnnotationColonne(col, row, properties);
			assertTrue(avs.size() == 1);
			assertTrue(avs.get(0).getItem().equals(itemDao.findById(12)));
			// since 2.0.13.2 thesM
			col = importColonneManager.findByIdManager(103);
			avs = importManager.setPropertyForAnnotationColonne(col, row, properties);
			assertTrue(avs.size() == 1);
			assertTrue(avs.get(0).getItem().equals(itemDao.findById(5)));

			// test sur la 3eme ligne
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(17);
			assertTrue(importManager.setPropertyForAnnotationColonne(col, row, properties).isEmpty());

			// since 2.0.13.2 thesM multiple values
			// test sur la 4eme ligne
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(103);
			avs = importManager.setPropertyForAnnotationColonne(col, row, properties);
			assertTrue(avs.size() == 2);
			assertTrue(avs.get(0).getItem().equals(itemDao.findById(5)));
			assertTrue(avs.get(1).getItem().equals(itemDao.findById(4)));

			// test sur la 5eme ligne
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(18);
			Boolean catched = false;
			try{
				importManager.setPropertyForAnnotationColonne(col, row, properties);
			}catch(final WrongImportValueException wve){
				catched = true;
			}
			assertTrue(catched);
			catched = false;

			// test sur la 7eme ligne
			row = (HSSFRow) rit.next();
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(11);
			try{
				importManager.setPropertyForAnnotationColonne(col, row, properties);
			}catch(final WrongImportValueException wve){
				catched = true;
			}
			assertTrue(catched);
			catched = false;

			// since 2.0.13.2 thesM multiple values
			// test sur la 10eme ligne
			row = (HSSFRow) rit.next();
			row = (HSSFRow) rit.next();
			col = importColonneManager.findByIdManager(103);
			try{
				importManager.setPropertyForAnnotationColonne(col, row, properties);
			}catch(final WrongImportValueException wve){
				catched = true;
			}
			assertTrue(catched);
			catched = false;

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	@Test
	public void testSetAllPropertiesForPatient(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur le 1er patient
			TKAnnotableObjectDuo patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat = (Patient) patDuo.getFirstObj();
			assertTrue(pat.getNip().equals("152115453"));
			assertTrue(pat.getNom().equals("MEUNIER"));
			assertTrue(pat.getPrenom().equals("FRANCOIS"));
			assertTrue(pat.getSexe().equals("M"));
			assertTrue(format.format(pat.getDateNaissance()).equals("1983-07-25"));
			assertNull(pat.getPatientId());
			final Entite ePatient = entiteDao.findById(1);
			assertFalse(properties.getAnnotationsEntite().containsKey(ePatient));

			// test sur le 2eme patient
			row = (HSSFRow) rit.next();
			patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat2 = (Patient) patDuo.getSecondObj();
			assertTrue(pat2.equals(patDuo.getFirstObj()));
			assertTrue(pat2.getNip().equals("12"));
			assertTrue(pat2.getNom().equals("MAYER"));
			assertTrue(pat2.getPrenom().equals("SUZAN"));
			assertNotNull(pat2.getDateNaissance());
			assertNotNull(pat2.getPatientId());
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setAllPropertiesForMaladie().
	 */
	@Test
	public void testSetAllPropertiesForMaladie(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1er ligne
			TKAnnotableObjectDuo patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat = (Patient) patDuo.getFirstObj();
			final Maladie mal = importManager.setAllPropertiesForMaladie(row, properties, pat);
			assertTrue(mal.getLibelle().equals("TUMEUR"));
			assertTrue(mal.getPatient().equals(pat));
			assertNull(mal.getMaladieId());
			assertNull(mal.getDateDiagnostic());

			// test sur la 2eme ligne
			row = (HSSFRow) rit.next();
			patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat2 = (Patient) patDuo.getSecondObj(); // patiente Mayer existe
			final Maladie mal2 = importManager.setAllPropertiesForMaladie(row, properties, pat2);
			assertTrue(mal2.getLibelle().equals("Non precise"));
			assertTrue(mal2.getPatient().equals(pat2));
			assertNotNull(mal2.getMaladieId());
			assertNull(mal2.getDateDiagnostic());

			// test avec patient null
			final Maladie mal3 = importManager.setAllPropertiesForMaladie(row, properties, null);
			assertNull(mal3.getPatient());

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setAllPropertiesForPrelevement().
	 */
	@Test
	public void testSetAllPropertiesForPrelevement(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));
			properties.setImportTemplate(template);

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// Entite ePrlvt = entiteDao.findById(2);
			// test sur la 1er ligne
			TKAnnotableObjectDuo patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat = (Patient) patDuo.getFirstObj();
			final Maladie mal = importManager.setAllPropertiesForMaladie(row, properties, pat);
			TKAnnotableObjectDuo duo = importManager.setAllPropertiesForPrelevement(row, properties, mal);
			final Prelevement prlvt = (Prelevement) duo.getFirstObj();
			assertTrue(prlvt.getCode().equals("PRLVT456"));
			assertTrue(prlvt.getNature().getNom().equals("SANG"));
			assertTrue(prlvt.getPreleveur().getNom().equals("VIAL"));
			assertTrue(prlvt.getMaladie().equals(mal));
			assertNull(prlvt.getPrelevementId());
			// assertTrue(properties.getAnnotationsEntite()
			//		.get(ePrlvt).size() == 5);
			assertTrue(duo.getFirstAnnoVals().size() == 5);
			assertTrue(duo.getFirstNoConfs().isEmpty());
			assertTrue(duo.getSecondAnnoVals().isEmpty());
			assertTrue(duo.getSecondNoConfs().isEmpty());

			// test sur la 2eme ligne
			row = (HSSFRow) rit.next();
			patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat2 = (Patient) patDuo.getFirstObj();
			final Maladie mal2 = importManager.setAllPropertiesForMaladie(row, properties, pat2);
			duo = importManager.setAllPropertiesForPrelevement(row, properties, mal2);
			final Prelevement prlvt2 = (Prelevement) duo.getFirstObj();
			assertTrue(prlvt2.getCode().equals("PRLVT4"));
			assertTrue(prlvt2.getNature().getNom().equals("LIQUIDE D'ASCITE"));
			assertTrue(prlvt2.getPreleveur().getNom().equals("VIAL"));
			assertTrue(prlvt2.getMaladie().equals(mal2));
			assertNull(prlvt2.getPrelevementId());
			//	assertTrue(properties.getAnnotationsEntite().get(ePrlvt).size() == 5);
			assertTrue(duo.getFirstAnnoVals().size() == 4);
			assertTrue(duo.getFirstNoConfs().isEmpty());
			assertTrue(duo.getSecondAnnoVals().isEmpty());
			assertTrue(duo.getSecondNoConfs().isEmpty());
			// assertTrue(properties.getAnnotationsEntite().get(ePrlvt).get(2).isEmpty());

			// test avec patient null
			row = (HSSFRow) rit.next();
			final Prelevement prlvt3 =
					(Prelevement) importManager.setAllPropertiesForPrelevement(row, properties, null).getFirstObj();
			assertNull(prlvt3.getMaladie());

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setAllPropertiesForEchantillon().
	 */
	@Test
	public void testSetAllPropertiesForEchantillon(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));
			properties.setImportTemplate(template);

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1er ligne
			TKAnnotableObjectDuo patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat = (Patient) patDuo.getFirstObj();
			final Maladie mal = importManager.setAllPropertiesForMaladie(row, properties, pat);
			final Prelevement prlvt = (Prelevement) importManager.setAllPropertiesForPrelevement(row, properties, mal).getFirstObj();
			final Echantillon echan = importManager.setAllPropertiesForEchantillon(row, properties, prlvt);
			assertTrue(echan.getCode().equals("PRLVT456.1"));
			assertTrue(echan.getEchantillonType().getNom().equals("CELLULES"));
			assertTrue(format.format(echan.getDateStock().getTime()).equals("2010-11-25"));
			assertTrue(echan.getPrelevement().equals(prlvt));
			assertNull(echan.getEchantillonId());

			assertTrue(echan.getCodesAssignes().size() == 2);
			final Iterator<CodeAssigne> codesIt = echan.getCodesAssignes().iterator();
			CodeAssigne c;
			while(codesIt.hasNext()){
				c = codesIt.next();
				if(c.getIsOrgane()){
					assertTrue(c.getCode().equals("MOELLE OSSEUSE"));
					assertTrue(c.getLibelle().equals("grave"));
				}else{
					assertTrue(c.getCode().equals("LAM3"));
					assertTrue(c.getLibelle().equals("Maladie"));
				}
			}

			// test sur la 2eme ligne
			row = (HSSFRow) rit.next();
			patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat2 = (Patient) patDuo.getFirstObj();
			final Maladie mal2 = importManager.setAllPropertiesForMaladie(row, properties, pat2);
			final Prelevement prlvt2 =
					(Prelevement) importManager.setAllPropertiesForPrelevement(row, properties, mal2).getSecondObj();
			final Echantillon echan2 = importManager.setAllPropertiesForEchantillon(row, properties, prlvt2);
			assertTrue(echan2.getCode().equals("PTRA.1"));
			assertTrue(echan2.getEchantillonType().getNom().equals("CELLULES"));
			assertNotNull(echan2.getPrelevement().getPrelevementId());
			assertNotNull(echan2.getEchantillonId());

			// test avec prelevement null
			row = (HSSFRow) rit.next();
			final Echantillon echan3 = importManager.setAllPropertiesForEchantillon(row, properties, null);
			assertNull(echan3.getPrelevement());

			// test sur la 4eme ligne
			row = (HSSFRow) rit.next();
			final Echantillon echan4 = importManager.setAllPropertiesForEchantillon(row, properties, prlvt2);
			assertTrue(echan4.getCodesAssignes().isEmpty());

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode setAllPropertiesForProdDerive().
	 */
	@Test
	public void testSetAllPropertiesForProdDerive(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));
			properties.setImportTemplate(template);

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1er ligne
			TKAnnotableObjectDuo patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat = (Patient) patDuo.getFirstObj();
			final Maladie mal = importManager.setAllPropertiesForMaladie(row, properties, pat);
			final Prelevement prlvt = (Prelevement) importManager.setAllPropertiesForPrelevement(row, properties, mal).getFirstObj();
			importManager.setAllPropertiesForEchantillon(row, properties, prlvt);
			final ProdDerive derive = importManager.setAllPropertiesForProdDerive(row, properties);
			assertTrue(derive.getCode().equals("PRLVT456.1.1"));
			assertTrue(derive.getProdType().getNom().equals("ARN"));
			assertTrue(format.format(derive.getDateTransformation().getTime()).equals("2010-11-25"));
			assertNull(derive.getProdDeriveId());

			// test sur la 2eme ligne
			row = (HSSFRow) rit.next();
			patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat2 = (Patient) patDuo.getFirstObj();
			final Maladie mal2 = importManager.setAllPropertiesForMaladie(row, properties, pat2);
			final Prelevement prlvt2 =
					(Prelevement) importManager.setAllPropertiesForPrelevement(row, properties, mal2).getFirstObj();
			importManager.setAllPropertiesForEchantillon(row, properties, prlvt2);
			final ProdDerive derive2 = importManager.setAllPropertiesForProdDerive(row, properties);
			assertTrue(derive2.getCode().equals("PTRA.1.1"));
			assertTrue(derive2.getProdType().getNom().equals("ADN"));
			assertNotNull(derive2.getProdDeriveId());

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode saveObjectsManager().
	 */
	@Test
	public void testSaveObjectsManager(){
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());
		final Utilisateur u = utilisateurDao.findById(1);
		final List<Importation> importations = new ArrayList<>();
		final List<DerivesImportBatches> derivesImportBatches = new ArrayList<>();

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));
			properties.setImportTemplate(template);

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1er ligne
			final TKAnnotableObjectDuo patDuo = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat = (Patient) patDuo.getFirstObj();
			final Maladie mal = importManager.setAllPropertiesForMaladie(row, properties, pat);
			final TKAnnotableObjectDuo prlvtDuo = importManager.setAllPropertiesForPrelevement(row, properties, mal);
			final Prelevement prlvt = (Prelevement) prlvtDuo.getFirstObj();
			final Echantillon echan = importManager.setAllPropertiesForEchantillon(row, properties, prlvt);
			final ProdDerive derive = importManager.setAllPropertiesForProdDerive(row, properties);
			List<Object> objs = new ArrayList<>();
			objs.add(patDuo);
			objs.add(mal);
			objs.add(prlvtDuo);
			objs.add(echan);
			objs.add(derive);
			importManager.saveObjectsRowManager(row, objs, importations, u, properties, null, derivesImportBatches);
			assertTrue(patientManager.findAllObjectsManager().size() == 6);
			assertTrue(maladieManager.findAllObjectsManager().size() == 7);
			assertTrue(prelevementManager.findAllObjectsManager().size() == 6);
			assertTrue(echantillonManager.findAllObjectsManager().size() == 5);
			assertTrue(annotationValeurManager.findByObjectManager(prlvt).size() == 5);
			assertTrue(annotationValeurManager.findByObjectManager(pat).size() == 0);
			assertTrue(annotationValeurManager.findByObjectManager(echan).size() == 1);
			assertNotNull(echan.getEmplacement());
			assertTrue(!echan.getEmplacement().getVide());
			assertTrue(echan.getObjetStatut().getStatut().equals("STOCKE"));
			assertTrue(echan.getCodesAssignes().size() == 2);
			final Iterator<CodeAssigne> codesIt = echan.getCodesAssignes().iterator();
			CodeAssigne c;
			while(codesIt.hasNext()){
				c = codesIt.next();
				if(c.getIsOrgane()){
					assertTrue(c.getCode().equals("MOELLE OSSEUSE"));
					assertTrue(c.getLibelle().equals("grave"));
				}else{
					assertTrue(c.getCode().equals("LAM3"));
					assertTrue(c.getLibelle().equals("Maladie"));
				}
			}
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 7);
			// vérif des risques
			final Prelevement prlvtTest = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
			assertTrue(prelevementManager.getRisquesManager(prlvtTest).size() == 2);

			// verif non conformites
			final Echantillon echan1 = echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0);
			assertTrue(objetNonConformeDao
					.findByObjetEntiteAndType(echan1.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2)).isEmpty());
			assertTrue(importManager.getNcfsPrelevement() == null);
			assertTrue(importManager.getNcfsEchanCess() == null);

			//  verif derive batch
			assertNotNull(derive.getEmplacement());
			assertTrue(derive.getObjetStatut().getStatut().equals("STOCKE"));
			assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
			assertTrue(derivesImportBatches.size() == 1);
			assertTrue(derivesImportBatches.get(0).getDerives().size() == 1);
			assertTrue(derivesImportBatches.get(0).getDerives().contains(derive));
			assertTrue(derivesImportBatches.get(0).getParent().equals(echan1));
			assertTrue(derivesImportBatches.get(0).getTransfoQte() == null);
			assertTrue(derivesImportBatches.get(0).getTransfoQteUnite() == null);
			assertTrue(derivesImportBatches.get(0).getDateSortie() == null);
			assertTrue(derivesImportBatches.get(0).getObservations() == null);
			assertTrue(derivesImportBatches.get(0).getEmplacements().size() == 1);
			assertTrue(derivesImportBatches.get(0).getEmplacements().get(derive).equals(derive.getEmplacement()));
			assertTrue(derivesImportBatches.get(0).getAnnoVals().isEmpty());
			assertTrue(derivesImportBatches.get(0).getNcfsTrait().isEmpty());
			assertTrue(derivesImportBatches.get(0).getNcfsCess().isEmpty());

			// assertTrue(importations.size() == 4); // derive non importe
			assertTrue(importations.size() == 3);
			assertNotNull(importations.get(0).getObjetId());
			assertNotNull(importations.get(0).getEntite());
			importations.clear();

			// test sur la 2eme ligne
			row = (HSSFRow) rit.next();
			final TKAnnotableObjectDuo patDuo2 = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat2 = (Patient) patDuo2.getSecondObj(); // patiente MAYER existe
			final Maladie mal2 = importManager.setAllPropertiesForMaladie(row, properties, pat2);
			final TKAnnotableObjectDuo prlvtDuo2 = importManager.setAllPropertiesForPrelevement(row, properties, mal2);
			final Prelevement prlvt2 = (Prelevement) prlvtDuo2.getFirstObj();
			final Echantillon echan2 = importManager.setAllPropertiesForEchantillon(row, properties, prlvt2);
			final ProdDerive derive2 = importManager.setAllPropertiesForProdDerive(row, properties);
			objs = new ArrayList<>();
			objs.add(patDuo2);
			objs.add(mal2);
			objs.add(prlvtDuo2);
			objs.add(echan2);
			objs.add(derive2);
			importManager.saveObjectsRowManager(row, objs, importations, u, properties, null, derivesImportBatches);
			assertTrue(patientManager.findAllObjectsManager().size() == 6);
			assertTrue(maladieManager.findAllObjectsManager().size() == 7);
			assertTrue(prelevementManager.findAllObjectsManager().size() == 7);
			assertTrue(echantillonManager.findAllObjectsManager().size() == 5);
			assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 7);

			// verif non conformites
			assertEquals(null, importManager.getNcfsDeriveTrait());
			assertTrue(importManager.getNcfsDeriveCess() == null);
			assertTrue(importations.size() == 1);

			assertTrue(derivesImportBatches.size() == 1);

			// test sur la 3eme ligne
			row = (HSSFRow) rit.next();
			final TKAnnotableObjectDuo patDuo3 = importManager.setAllPropertiesForPatient(row, properties);
			final Patient pat3 = (Patient) patDuo3.getFirstObj();
			final Maladie mal3 = importManager.setAllPropertiesForMaladie(row, properties, pat3);
			final TKAnnotableObjectDuo prlvtDuo3 = importManager.setAllPropertiesForPrelevement(row, properties, mal3);
			final Prelevement prlvt3 = (Prelevement) prlvtDuo3.getFirstObj();
			final Echantillon echan3 = importManager.setAllPropertiesForEchantillon(row, properties, prlvt3);
			final ProdDerive derive3 = importManager.setAllPropertiesForProdDerive(row, properties);
			objs = new ArrayList<>();
			objs.add(patDuo3);
			objs.add(mal3);
			objs.add(prlvtDuo3);
			objs.add(echan3);
			objs.add(derive3);
			importManager.saveObjectsRowManager(row, objs, importations, u, properties, null, derivesImportBatches);
			assertTrue(patientManager.findAllObjectsManager().size() == 7);
			assertTrue(maladieManager.findAllObjectsManager().size() == 8);
			assertTrue(prelevementManager.findAllObjectsManager().size() == 8);
			assertTrue(echantillonManager.findAllObjectsManager().size() == 6);
			assertNotNull(echan.getEmplacement());
			assertTrue(echan.getObjetStatut().getStatut().equals("STOCKE"));
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 9);

			// verif non conformites
			final Echantillon echan3b = echantillonManager.findByCodeLikeManager("PRLVT5612.1", true).get(0);
			assertTrue(objetNonConformeDao
					.findByObjetEntiteAndType(echan3b.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2))
					.size() == 2);

			assertTrue(importManager.getNcfsPrelevement() == null);
			assertTrue(importManager.getNcfsEchanCess() == null);

			//  verif derive batch
			assertNotNull(derive3.getEmplacement());
			assertTrue(derive3.getObjetStatut().getStatut().equals("STOCKE"));
			assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
			assertTrue(derivesImportBatches.size() == 2);
			assertTrue(derivesImportBatches.get(1).getDerives().size() == 1);
			assertTrue(derivesImportBatches.get(1).getDerives().contains(derive3));
			assertTrue(derivesImportBatches.get(1).getParent().equals(echan3));
			assertTrue(derivesImportBatches.get(1).getTransfoQte() == null);
			assertTrue(derivesImportBatches.get(1).getTransfoQteUnite() == null);
			assertTrue(derivesImportBatches.get(1).getDateSortie() == null);
			assertTrue(derivesImportBatches.get(1).getObservations() == null);
			assertTrue(derivesImportBatches.get(1).getEmplacements().size() == 1);
			assertTrue(derivesImportBatches.get(1).getEmplacements().get(derive3).equals(derive3.getEmplacement()));
			assertTrue(derivesImportBatches.get(1).getAnnoVals().isEmpty());
			assertTrue(derivesImportBatches.get(1).getNcfsTrait().isEmpty());
			assertTrue(derivesImportBatches.get(1).getNcfsCess().isEmpty());

			assertTrue(importations.size() == 4);
			assertNotNull(importations.get(0).getObjetId());
			assertNotNull(importations.get(0).getEntite());
			importations.clear();

			// save 2 derives 2 batches
			final List<ImportError> errors = new ArrayList<>();
			importManager.saveDeriveBatchesManager(derivesImportBatches, properties, importations, u, null, errors);
			assertTrue(prodDeriveManager.findAllObjectsManager().size() == 6);
			assertTrue(importations.size() == 2);

			derivesImportBatches.clear();

			final Emplacement empl1 = echan.getEmplacement();
			empl1.setEntite(null);
			empl1.setObjetId(null);
			empl1.setVide(true);
			emplacementManager.updateObjectManager(empl1, empl1.getTerminale(), null);
			final Emplacement empl2 = echan3.getEmplacement();
			empl2.setEntite(null);
			empl2.setObjetId(null);
			empl2.setVide(true);
			emplacementManager.updateObjectManager(empl2, empl2.getTerminale(), null);
			final Emplacement empl3 = derive.getEmplacement();
			empl3.setEntite(null);
			empl3.setObjetId(null);
			empl3.setVide(true);
			emplacementManager.updateObjectManager(empl3, empl3.getTerminale(), null);
			final Emplacement empl4 = derive3.getEmplacement();
			empl4.setEntite(null);
			empl4.setObjetId(null);
			empl4.setVide(true);
			emplacementManager.updateObjectManager(empl4, empl4.getTerminale(), null);
			prodDeriveManager.removeObjectCascadeManager(derive, null, u, null);
			echan.setEmplacement(null);
			echan.setObjetStatut(objetStatutDao.findById(4)); // non stocke
			echantillonManager.updateObjectManager(echan, properties.getBanque(), prlvt, null, null, null,
					echan.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, true, null, null);
			echantillonManager.removeObjectCascadeManager(echan, null, u, null);
			prelevementManager.removeObjectManager(prlvt, null, u, null);
			maladieManager.removeObjectManager(mal, null, u);
			patientManager.removeObjectManager(pat, null, u, null);
			prodDeriveManager.removeObjectCascadeManager(derive3, null, u, null);
			echantillonManager.removeObjectCascadeManager(echan3, null, u, null);
			prelevementManager.removeObjectManager(prlvt3, null, u, null);
			prelevementManager.removeObjectManager(prlvt2, null, u, null);
			maladieManager.removeObjectManager(mal3, null, u);
			patientManager.removeObjectManager(pat3, null, u, null);

			assertTrue(patientManager.findAllObjectsManager().size() == 5);
			assertTrue(maladieManager.findAllObjectsManager().size() == 6);
			assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
			assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
			assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
			assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 5);

			final List<TKFantomableObject> fs = new ArrayList<>();
			fs.add(derive3);
			fs.add(echan3);
			fs.add(prlvt3);
			fs.add(prlvt2);
			fs.add(pat3);
			fs.add(mal3);
			fs.add(derive);
			fs.add(echan);
			fs.add(prlvt);
			fs.add(pat);
			fs.add(mal);
			cleanUpFantomes(fs);

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Test de la méthode importFile().
	 */
	@Test
	public void testImportXlsFile(){
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		final Utilisateur u = utilisateurDao.findById(1);
		ImportHistorique ih = null;
		ImportHistorique ih2 = null;

		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// File file = new File("/home/mathieu/Documents/tumorotek/esteban/esteban.xlsx");

		List<ImportError> errors = new ArrayList<>();
		try( FileInputStream fis = new FileInputStream(file);){
			ih = importManager.importFileManager(template, u, fis);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			errors = ((ErrorsInImportException) re).getErrors();
			errors.get(0).getException().printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertTrue(errors.size() == 0);
		assertTrue(patientManager.findAllObjectsManager().size() == 8);
		assertTrue(maladieManager.findAllObjectsManager().size() == 9);
		assertTrue(prelevementManager.findAllObjectsManager().size() == 9);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 7);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 7);
		assertTrue(transformationDao.findAll().size() == 8);
		assertTrue(emplacementManager.findAllObjectsManager().size() == 10);
		assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 4);
		assertNotNull(ih);
		assertEquals(13, importHistoriqueManager.findImportationsByHistoriqueManager(ih).size());

		// imports identique = 0 importations + pas historique
		try( FileInputStream fis = new FileInputStream(file);){
			ih2 = importManager.importFileManager(template, u, fis);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			errors = ((ErrorsInImportException) re).getErrors();
			re.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}
		assertTrue(errors.size() == 0);
		assertTrue(patientManager.findAllObjectsManager().size() == 8);
		assertTrue(maladieManager.findAllObjectsManager().size() == 9);
		assertTrue(prelevementManager.findAllObjectsManager().size() == 9);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 7);
		assertTrue(transformationDao.findAll().size() == 8);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 7);
		assertTrue(emplacementManager.findAllObjectsManager().size() == 10);
		assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 4);
		assertNull(ih2);

		// Removes
		importHistoriqueManager.removeObjectManager(ih);
		assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
		final ProdDerive derive1 = prodDeriveManager.findByCodeLikeManager("PRLVT456.1.1", true).get(0);
		final Emplacement empl1 = prodDeriveManager.getEmplacementManager(derive1);
		empl1.setEntite(null);
		empl1.setObjetId(null);
		empl1.setVide(true);
		emplacementManager.updateObjectManager(empl1, empl1.getTerminale(), null);
		prodDeriveManager.removeObjectCascadeManager(derive1, null, u, null);
		final ProdDerive derive2 = prodDeriveManager.findByCodeLikeManager("PRLVT5612.1.2", true).get(0);
		final Emplacement empl2 = prodDeriveManager.getEmplacementManager(derive2);
		empl2.setEntite(null);
		empl2.setObjetId(null);
		empl2.setVide(true);
		emplacementManager.updateObjectManager(empl2, empl1.getTerminale(), null);
		prodDeriveManager.removeObjectCascadeManager(derive2, null, u, null);
		final ProdDerive derive3 = prodDeriveManager.findByCodeLikeManager("PRLVT98.5.1", true).get(0);
		prodDeriveManager.removeObjectCascadeManager(derive3, null, u, null);
		final Echantillon echan1 = echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0);
		final Emplacement empl3 = echantillonManager.getEmplacementManager(echan1);
		empl3.setEntite(null);
		empl3.setObjetId(null);
		empl3.setVide(true);
		emplacementManager.updateObjectManager(empl3, empl1.getTerminale(), null);
		echan1.setEmplacement(null);
		echan1.setObjetStatut(objetStatutDao.findById(4)); // non stocke
		echantillonManager.updateObjectManager(echan1, echan1.getBanque(), echan1.getPrelevement(), null, null, null,
				echan1.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, true, null, null);
		echantillonManager.removeObjectCascadeManager(echan1, null, u, null);
		final Echantillon echan2 = echantillonManager.findByCodeLikeManager("PRLVT5612.1", true).get(0);
		final Emplacement empl4 = echantillonManager.getEmplacementManager(echan2);
		empl4.setEntite(null);
		empl4.setObjetId(null);
		empl4.setVide(true);
		emplacementManager.updateObjectManager(empl4, empl1.getTerminale(), null);
		echantillonManager.removeObjectCascadeManager(echan2, null, u, null);
		final Echantillon echan3 = echantillonManager.findByCodeLikeManager("PRLVT98.5", true).get(0);
		echantillonManager.removeObjectCascadeManager(echan3, null, u, null);
		final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
		prelevementManager.removeObjectManager(prlvt1, null, u, null);
		final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("PRLVT5612", true).get(0);
		prelevementManager.removeObjectManager(prlvt2, null, u, null);
		final Prelevement prlvt3 = prelevementManager.findByCodeLikeManager("PRLVT98", true).get(0);
		prelevementManager.removeObjectManager(prlvt3, null, u, null);
		final Prelevement prlvt4 = prelevementManager.findByCodeLikeManager("PRLVT4", true).get(0);
		prelevementManager.removeObjectManager(prlvt4, null, u, null);
		final Maladie mal1 = maladieManager.findByLibelleLikeManager("TUMEUR", true).get(0);
		maladieManager.removeObjectManager(mal1, null, u);
		final Maladie mal2 = maladieManager.findByLibelleLikeManager("CANCER", true).get(0);
		maladieManager.removeObjectManager(mal2, null, u);
		final Maladie mal3 = maladieManager.findByLibelleLikeManager("VIEILLESSE", true).get(0);
		maladieManager.removeObjectManager(mal3, null, u);
		final Patient pat1 = patientManager.findByNomLikeManager("MEUNIER", true).get(0);
		patientManager.removeObjectManager(pat1, null, u, null);
		final Patient pat2 = patientManager.findByNomLikeManager("MULLER", true).get(0);
		patientManager.removeObjectManager(pat2, null, u, null);
		final Patient pat3 = patientManager.findByNomLikeManager("NONO", true).get(0);
		patientManager.removeObjectManager(pat3, null, u, null);

		assertTrue(patientManager.findAllObjectsManager().size() == 5);
		assertTrue(maladieManager.findAllObjectsManager().size() == 6);
		assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
		assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

		final List<TKFantomableObject> fs = new ArrayList<>();
		fs.add(derive1);
		fs.add(derive2);
		fs.add(derive3);
		fs.add(echan1);
		fs.add(echan2);
		fs.add(echan3);
		fs.add(prlvt1);
		fs.add(prlvt2);
		fs.add(prlvt3);
		fs.add(prlvt4);
		fs.add(pat1);
		fs.add(pat2);
		fs.add(pat3);
		fs.add(mal1);
		fs.add(mal2);
		fs.add(mal3);
		cleanUpFantomes(fs);
	}

	@Test
	public void testImportXlsxFile(){
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		final Utilisateur u = utilisateurDao.findById(1);
		ImportHistorique ih = null;

		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xlsx");

		List<ImportError> errors = new ArrayList<>();
		try( FileInputStream fis = new FileInputStream(file);){
			ih = importManager.importFileManager(template, u, fis);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			errors = ((ErrorsInImportException) re).getErrors();
			re.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertTrue(errors.size() == 0);
		assertTrue(patientManager.findAllObjectsManager().size() == 8);
		assertTrue(maladieManager.findAllObjectsManager().size() == 9);
		assertTrue(prelevementManager.findAllObjectsManager().size() == 9);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 7);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 7);
		assertTrue(emplacementManager.findAllObjectsManager().size() == 10);
		assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 4);
		assertNotNull(ih);
		assertEquals(13, importHistoriqueManager.findImportationsByHistoriqueManager(ih).size());

		// Removes
		importHistoriqueManager.removeObjectManager(ih);
		assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
		final ProdDerive derive1 = prodDeriveManager.findByCodeLikeManager("PRLVT456.1.1", true).get(0);
		final Emplacement empl1 = prodDeriveManager.getEmplacementManager(derive1);
		empl1.setEntite(null);
		empl1.setObjetId(null);
		empl1.setVide(true);
		emplacementManager.updateObjectManager(empl1, empl1.getTerminale(), null);
		prodDeriveManager.removeObjectCascadeManager(derive1, null, u, null);
		final ProdDerive derive2 = prodDeriveManager.findByCodeLikeManager("PRLVT5612.1.2", true).get(0);
		final Emplacement empl2 = prodDeriveManager.getEmplacementManager(derive2);
		empl2.setEntite(null);
		empl2.setObjetId(null);
		empl2.setVide(true);
		emplacementManager.updateObjectManager(empl2, empl1.getTerminale(), null);
		prodDeriveManager.removeObjectCascadeManager(derive2, null, u, null);
		final ProdDerive derive3 = prodDeriveManager.findByCodeLikeManager("PRLVT98.5.1", true).get(0);
		prodDeriveManager.removeObjectCascadeManager(derive3, null, u, null);
		final Echantillon echan1 = echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0);
		final Emplacement empl3 = echantillonManager.getEmplacementManager(echan1);
		empl3.setEntite(null);
		empl3.setObjetId(null);
		empl3.setVide(true);
		emplacementManager.updateObjectManager(empl3, empl1.getTerminale(), null);
		echan1.setEmplacement(null);
		echan1.setObjetStatut(objetStatutDao.findById(4)); // non stocke
		echantillonManager.updateObjectManager(echan1, echan1.getBanque(), echan1.getPrelevement(), null, null, null,
				echan1.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, true, null, null);
		echantillonManager.removeObjectCascadeManager(echan1, null, u, null);
		final Echantillon echan2 = echantillonManager.findByCodeLikeManager("PRLVT5612.1", true).get(0);
		final Emplacement empl4 = echantillonManager.getEmplacementManager(echan2);
		empl4.setEntite(null);
		empl4.setObjetId(null);
		empl4.setVide(true);
		emplacementManager.updateObjectManager(empl4, empl1.getTerminale(), null);
		echantillonManager.removeObjectCascadeManager(echan2, null, u, null);
		final Echantillon echan3 = echantillonManager.findByCodeLikeManager("PRLVT98.5", true).get(0);
		echantillonManager.removeObjectCascadeManager(echan3, null, u, null);
		final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
		prelevementManager.removeObjectManager(prlvt1, null, u, null);
		final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("PRLVT5612", true).get(0);
		prelevementManager.removeObjectManager(prlvt2, null, u, null);
		final Prelevement prlvt3 = prelevementManager.findByCodeLikeManager("PRLVT98", true).get(0);
		prelevementManager.removeObjectManager(prlvt3, null, u, null);
		final Prelevement prlvt4 = prelevementManager.findByCodeLikeManager("PRLVT4", true).get(0);
		prelevementManager.removeObjectManager(prlvt4, null, u, null);
		final Maladie mal1 = maladieManager.findByLibelleLikeManager("TUMEUR", true).get(0);
		maladieManager.removeObjectManager(mal1, null, u);
		final Maladie mal2 = maladieManager.findByLibelleLikeManager("CANCER", true).get(0);
		maladieManager.removeObjectManager(mal2, null, u);
		final Maladie mal3 = maladieManager.findByLibelleLikeManager("VIEILLESSE", true).get(0);
		maladieManager.removeObjectManager(mal3, null, u);
		final Patient pat1 = patientManager.findByNomLikeManager("MEUNIER", true).get(0);
		patientManager.removeObjectManager(pat1, null, u, null);
		final Patient pat2 = patientManager.findByNomLikeManager("MULLER", true).get(0);
		patientManager.removeObjectManager(pat2, null, u, null);
		final Patient pat3 = patientManager.findByNomLikeManager("NONO", true).get(0);
		patientManager.removeObjectManager(pat3, null, u, null);

		assertTrue(patientManager.findAllObjectsManager().size() == 5);
		assertTrue(maladieManager.findAllObjectsManager().size() == 6);
		assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
		assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

		final List<TKFantomableObject> fs = new ArrayList<>();
		fs.add(derive1);
		fs.add(derive2);
		fs.add(derive3);
		fs.add(echan1);
		fs.add(echan2);
		fs.add(echan3);
		fs.add(prlvt1);
		fs.add(prlvt2);
		fs.add(prlvt3);
		fs.add(prlvt4);
		fs.add(pat1);
		fs.add(pat2);
		fs.add(pat3);
		fs.add(mal1);
		fs.add(mal2);
		fs.add(mal3);
		cleanUpFantomes(fs);
	}

	/**
	 * Test de la méthode importFile().
	 */
	@Test
	public void testImportFileWithErrors(){
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		final Utilisateur u = utilisateurDao.findById(1);
		ImportHistorique ih = null;

		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/badImport.xls");

		List<ImportError> errors = new ArrayList<>();
		try( FileInputStream fis = new FileInputStream(file);){
			ih = importManager.importFileManager(template, u, fis);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			errors = ((ErrorsInImportException) re).getErrors();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertNull(ih);
		assertEquals(8, errors.size());
		assertTrue(patientManager.findAllObjectsManager().size() == 5);
		assertEquals(6, maladieManager.findAllObjectsManager().size());
		assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
	}

	@Test
	public void testImportFileWithErrorsBis(){
		final ImportTemplate importTemplate = importTemplateManager.findByIdManager(1);
		final Utilisateur utilisateur = utilisateurDao.findById(1);

		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/badImport.xls");

		final ImportProperties properties = new ImportProperties();
		properties.setBanque(importTemplate.getBanque());
		final List<ImportError> errors = new ArrayList<>();
		final List<Importation> importations = new ArrayList<>();

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;

		// ouvre fichier xls
		try( FileInputStream fis = new FileInputStream(file);){
			wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			// init de toutes les hashtables
			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties
			.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), importTemplate));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(importTemplate));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(importTemplate));
			properties.setImportTemplate(importTemplate);

			final Entite ePatient = entiteDao.findById(1);
			final Entite eMaladie = entiteDao.findById(7);
			final Entite ePrlvt = entiteDao.findById(2);
			final Entite eEchan = entiteDao.findById(3);
			final Entite eDerive = entiteDao.findById(8);

			while(rit.hasNext()){
				row = (HSSFRow) rit.next();
				properties.setAnnotationsEntite(new Hashtable<Entite, List<AnnotationValeur>>());
				List<Object> objectsToSave = new ArrayList<>();
				Patient patient = null;
				Maladie maladie = null;
				Prelevement prlvt = null;
				Echantillon echantillon = null;
				ProdDerive derive = null;
				try{
					if(properties.getColonnesForEntites().containsKey(ePatient)){
						patient = (Patient) importManager.setAllPropertiesForPatient(row, properties).getFirstObj();
						objectsToSave.add(patient);
					}
					if(properties.getColonnesForEntites().containsKey(eMaladie)){
						maladie = importManager.setAllPropertiesForMaladie(row, properties, patient);
						objectsToSave.add(maladie);
					}
					if(properties.getColonnesForEntites().containsKey(ePrlvt)){
						prlvt = (Prelevement) importManager.setAllPropertiesForPrelevement(row, properties, maladie).getFirstObj();
						objectsToSave.add(prlvt);
					}
					if(properties.getColonnesForEntites().containsKey(eEchan)){
						echantillon = importManager.setAllPropertiesForEchantillon(row, properties, prlvt);
						objectsToSave.add(echantillon);
					}
					if(properties.getColonnesForEntites().containsKey(eDerive)){
						derive = importManager.setAllPropertiesForProdDerive(row, properties);
						objectsToSave.add(derive);
					}
				}catch(final WrongImportValueException wve){
					final ImportError error = new ImportError();
					error.setException(wve);
					error.setRow(row);
					error.setNbRow(row.getRowNum());
					errors.add(error);
					objectsToSave = new ArrayList<>();
				}
				try{
					if(objectsToSave.size() > 0){
						importManager.saveObjectsRowManager(row, objectsToSave, importations, utilisateur, properties, null, null);
					}
				}catch(final RuntimeException re){
					final ImportError error = new ImportError();
					error.setException(re);
					error.setRow(row);
					error.setNbRow(row.getRowNum());
					errors.add(error);
				}
			}
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}catch(final BadFileFormatException bdfe){
			final ImportError error = new ImportError();
			error.setException(bdfe);
			error.setRow(row);
			error.setNbRow(row.getRowNum());
			errors.add(error);
		}

		assertEquals(8, errors.size());
		assertTrue(patientManager.findAllObjectsManager().size() == 5);
		assertEquals(6, maladieManager.findAllObjectsManager().size());
		assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
	}

	/**
	 * @since 2.1 test xlsx extraction
	 */
	@Test
	public void testExtractListOfStringFromExcelFile(){
		File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/test_extraction.xls");

		List<String> liste = new ArrayList<>();
		try( FileInputStream fis = new FileInputStream(file);){
			liste = importManager.extractListOfStringFromExcelFile(fis, true);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			re.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertTrue(liste.size() == 11);
		assertTrue(liste.get(0).equals("PRLVT1"));
		assertTrue(liste.get(3).equals("PRLVT1.1"));
		assertTrue(liste.get(8).equals("25.4"));
		assertTrue(liste.get(10).equals("145478453"));

		try( FileInputStream fis = new FileInputStream(file);){
			liste = importManager.extractListOfStringFromExcelFile(fis, false);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			re.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertTrue(liste.size() == 12);
		assertTrue(liste.get(1).equals("PRLVT1"));
		assertTrue(liste.get(4).equals("PRLVT1.1"));
		assertNull(liste.get(0));
		assertTrue(liste.get(9).equals("25.4"));
		assertTrue(liste.get(11).equals("145478453"));

		// xlsx same file / same test results
		file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/test_extraction.xlsx");

		liste.clear();
		try( FileInputStream fis = new FileInputStream(file);){
			liste = importManager.extractListOfStringFromExcelFile(fis, true);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			re.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertTrue(liste.size() == 11);
		assertTrue(liste.get(0).equals("PRLVT1"));
		assertTrue(liste.get(3).equals("PRLVT1.1"));
		assertTrue(liste.get(8).equals("25.4"));
		assertTrue(liste.get(10).equals("145478453"));

		try( FileInputStream fis = new FileInputStream(file);){
			liste = importManager.extractListOfStringFromExcelFile(fis, false);
		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final RuntimeException re){
			re.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}

		assertTrue(liste.size() == 12);
		assertTrue(liste.get(1).equals("PRLVT1"));
		assertTrue(liste.get(4).equals("PRLVT1.1"));
		assertNull(liste.get(0));
		assertTrue(liste.get(9).equals("25.4"));
		assertTrue(liste.get(11).equals("145478453"));
	}

	/**
	 * Test de la méthode saveObjectsManager() sur l'enregsistrement 
	 * JDBC des échantillons.
	 * @throws SQLException 
	 * @throws CannotGetJdbcConnectionException 
	 */
	@Test
	public void testSaveEchantillonsJDBCManager() throws CannotGetJdbcConnectionException, SQLException{
		final ImportProperties properties = new ImportProperties();
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		properties.setBanque(template.getBanque());
		final Utilisateur u = utilisateurDao.findById(1);
		final List<Importation> importations = new ArrayList<>();

		HSSFWorkbook wb;
		HSSFSheet sheet;
		Iterator<Row> rit;
		HSSFRow row = null;
		final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xls");

		// ouvre fichier xls
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		final EchantillonJdbcSuite jdbcSuite = new EchantillonJdbcSuite();

		try{
			conn = DataSourceUtils.getConnection(dataSource);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select max(echantillon_id) from ECHANTILLON");
			if(rs.next()){
				jdbcSuite.setMaxEchantillonId(rs.getInt(1));
			}else{
				jdbcSuite.setMaxEchantillonId(1);
			}
			// Integer maxEchId = jdbcSuite.getMaxEchantillonId();
			rs.close();
			rs = stmt.executeQuery("select max(annotation_valeur_id) from ANNOTATION_VALEUR");
			if(rs.next()){
				jdbcSuite.setMaxAnnotationValeurId(rs.getInt(1));
			}else{
				jdbcSuite.setMaxAnnotationValeurId(1);
			}
			// Integer maxAnnoId = jdbcSuite.getMaxAnnotationValeurId();
			rs.close();
			rs = stmt.executeQuery("select max(code_assigne_id) from CODE_ASSIGNE");
			if(rs.next()){
				jdbcSuite.setMaxCodeAssigneId(rs.getInt(1));
			}else{
				jdbcSuite.setMaxCodeAssigneId(1);
			}
			jdbcSuite.setMaxCodeAssigneId(rs.getInt(1));
			// Integer maxCdId = jdbcSuite.getMaxCodeAssigneId();
			rs.close();
			rs = stmt.executeQuery("select max(objet_non_conforme_id) from OBJET_NON_CONFORME");
			if(rs.next()){
				jdbcSuite.setMaxObjetNonConformeId(rs.getInt(1));
			}else{
				jdbcSuite.setMaxObjetNonConformeId(1);
			}
			// Integer maxOncId = jdbcSuite.getMaxObjetNonConformeId();
			rs.close();

			final String sql = "insert into ECHANTILLON (ECHANTILLON_ID, " + "BANQUE_ID, ECHANTILLON_TYPE_ID, OBJET_STATUT_ID, "
					+ "PRELEVEMENT_ID, COLLABORATEUR_ID, QUANTITE_UNITE_ID, " + "ECHAN_QUALITE_ID, MODE_PREPA_ID, EMPLACEMENT_ID, "
					+ "CODE, DATE_STOCK, QUANTITE, QUANTITE_INIT, " + "LATERALITE, DELAI_CGL, " + "TUMORAL, STERILE, "
					+ "CONFORME_TRAITEMENT, CONFORME_CESSION, " + "ETAT_INCOMPLET, ARCHIVE) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			jdbcSuite.setPstmt(conn.prepareStatement(sql));

			final String sql2 = "insert into OPERATION (UTILISATEUR_ID, " + "OBJET_ID, ENTITE_ID, OPERATION_TYPE_ID, " + "DATE_, V1)"
					+ "values (?,?,?,?,?,?)";
			jdbcSuite.setPstmtOp(conn.prepareStatement(sql2));

			final String sql3 = "insert into CODE_ASSIGNE (CODE_ASSIGNE_ID, ECHANTILLON_ID, "
					+ "CODE, LIBELLE, CODE_REF_ID, TABLE_CODAGE_ID, IS_ORGANE, " + "IS_MORPHO, ORDRE, EXPORT) "
					+ "values (?,?,?,?,?,?,?,?,?,?)";
			jdbcSuite.setPstmtCd(conn.prepareStatement(sql3));

			final String sql4 =
					"insert into ANNOTATION_VALEUR (ANNOTATION_VALEUR_ID, " + "CHAMP_ANNOTATION_ID, OBJET_ID, BANQUE_ID, "
							+ "ALPHANUM, BOOL, ANNO_DATE, " + "TEXTE, ITEM_ID) " + "values (?,?,?,?,?,?,?,?,?)";
			jdbcSuite.setPstmtAnno(conn.prepareStatement(sql4));

			final String sql5 = "insert into OBJET_NON_CONFORME (OBJET_NON_CONFORME_ID, "
					+ "OBJET_ID, ENTITE_ID, NON_CONFORMITE_ID) " + "values (?,?,?,?)";
			jdbcSuite.setPstmtNc(conn.prepareStatement(sql5));
			FileInputStream fis = new FileInputStream(file);
			wb = new HSSFWorkbook(fis);
			fis.close();
			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = (HSSFRow) rit.next();

			properties.setColonnesHeaders(importManager.initColumnsHeadersManager(row));
			properties.setColonnesForEntites(importManager.initImportColonnesManager(properties.getColonnesHeaders(), template));
			properties.setThesaurusValues(importManager.generateThesaurusHashtable(template));
			properties.setAnnotationThesaurusValues(importManager.generateAnnotationsThesaurusHashtable(template));

			// on se place sur la 2eme ligne
			row = (HSSFRow) rit.next();

			// test sur la 1er ligne
			final Echantillon echan = importManager.setAllPropertiesForEchantillon(row, properties, null);
			List<Object> objs = new ArrayList<>();
			objs.add(echan);
			importManager.saveObjectsRowManager(row, objs, importations, u, properties, jdbcSuite, null);

			// test sur la 3eme ligne
			row = (HSSFRow) rit.next();
			row = (HSSFRow) rit.next();
			final Echantillon echan3 = importManager.setAllPropertiesForEchantillon(row, properties, null);
			objs = new ArrayList<>();
			objs.add(echan3);
			importManager.saveObjectsRowManager(row, objs, importations, u, properties, jdbcSuite, null);
			jdbcSuite.executeBatches();
			jdbcSuite.clearBatches();

			assertTrue(echantillonManager.findAllObjectsManager().size() == 6);
			assertTrue(echan3.getEmplacement().getPosition() == 12);
			assertTrue(echan3.getObjetStatut().getStatut().equals("STOCKE"));
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 9);

			assertTrue(echantillonManager.findAllObjectsManager().size() == 6);
			assertTrue(echan.getEmplacement().getPosition() == 10);
			assertTrue(echan.getObjetStatut().getStatut().equals("STOCKE"));

			// verif non conformites
			final Echantillon echan1 = echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0);
			assertTrue(annotationValeurManager.findByObjectManager(echan1).size() == 1);
			assertTrue(objetNonConformeDao
					.findByObjetEntiteAndType(echan1.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2)).isEmpty());
			assertTrue(importManager.getNcfsPrelevement() == null);
			assertTrue(importManager.getNcfsEchanCess() == null);
			// codes assignes 
			assertTrue(codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).size() == 1);
			final CodeAssigne org = codeAssigneManager.findCodesOrganeByEchantillonManager(echan1).get(0);
			assertTrue(org.getCode().equals("MOELLE OSSEUSE"));
			assertTrue(org.getLibelle().equals("grave"));
			assertTrue(codeAssigneManager.findCodesMorphoByEchantillonManager(echan1).size() == 1);
			final CodeAssigne morph = codeAssigneManager.findCodesMorphoByEchantillonManager(echan1).get(0);
			assertTrue(morph.getCode().equals("LAM3"));
			assertTrue(morph.getLibelle().equals("Maladie"));
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 9);

			// verif non conformites
			final Echantillon echan3b = echantillonManager.findByCodeLikeManager("PRLVT5612.1", true).get(0);
			assertTrue(objetNonConformeDao
					.findByObjetEntiteAndType(echan3b.getEchantillonId(), entiteDao.findById(3), conformiteTypeDao.findById(2))
					.size() == 2);

			assertTrue(importManager.getNcfsPrelevement() == null);
			assertTrue(importManager.getNcfsEchanCess() == null);

			final Emplacement empl1 = echantillonManager.getEmplacementManager(echan1);
			empl1.setEntite(null);
			empl1.setObjetId(null);
			empl1.setVide(true);
			emplacementManager.updateObjectManager(empl1, empl1.getTerminale(), null);
			echantillonManager.updateObjectManager(echan1, properties.getBanque(), null, null, null, null,
					echan1.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, false, null, null);

			echantillonManager.removeObjectCascadeManager(echan1, null, u, null);
			echantillonManager.removeObjectCascadeManager(echan3b, null, u, null);

			assertTrue(patientManager.findAllObjectsManager().size() == 5);
			assertTrue(maladieManager.findAllObjectsManager().size() == 6);
			assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
			assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
			assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
			assertTrue(emplacementManager.findAllObjectsManager().size() == 7);
			assertTrue(codeAssigneManager.findAllObjectsManager().size() == 5);

			final List<TKFantomableObject> fs = new ArrayList<>();
			fs.add(echan3b);
			fs.add(echan1);
			cleanUpFantomes(fs);

		}catch(final FileNotFoundException e){
			e.printStackTrace();
		}catch(final IOException e){
			e.printStackTrace();
		}finally{
			if(null != stmt){
				stmt.close();
			}
			jdbcSuite.closePs();
			if(null != conn){
				conn.close();
			}
		}
	}

	/**
	 * Teste la modification d'un patient + prélèvement + annotations 
	 * à partir de l'import d'un fichier.
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	@Test
	public void testUpdateByImportManager() throws InvalidFormatException, IOException{

		// imports des prélèvements à modifier		
		final ImportTemplate template = importTemplateManager.findByIdManager(1);
		final Utilisateur u = utilisateurDao.findById(1);
		ImportHistorique ih = null;

		FileInputStream fileStr = null;
		try{
			fileStr = new FileInputStream(new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/io/imports/import1.xlsx"));
		}catch(final FileNotFoundException e1){
			e1.printStackTrace();
		}

		final Workbook wb = WorkbookFactory.create(fileStr);

		List<ImportError> errors = new ArrayList<>();

		if(null != fileStr){
			fileStr.close();
		}

		try{
			ih = importManager.importFileManager(template, u, wb.getSheetAt(0));
		}catch(final RuntimeException re){
			errors = ((ErrorsInImportException) re).getErrors();
			re.printStackTrace();
		}

		assertTrue(errors.size() == 0);
		assertNotNull(ih);
		assertEquals(13, importHistoriqueManager.findImportationsByHistoriqueManager(ih).size());
		for(final Importation imp : importHistoriqueManager.findImportationsByHistoriqueManager(ih)){
			assertFalse(imp.getIsUpdate());
		}

		// MEUNIER	FRANCOIS
		Patient patImp1 = patientManager.findByNipLikeManager("152115453", true).get(0);
		assertTrue(patImp1.getPatientEtat().equals("V"));

		// PRLVT456
		Prelevement prelImp1 = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
		assertTrue(prelImp1.getNature().getNom().equals("SANG"));
		assertTrue(prelImp1.getConsentType().getNom().equals("EN ATTENTE"));
		assertTrue(prelImp1.getPreleveur().getNom().equals("VIAL"));
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp1)
				.get(0).getAlphanum().equals("hfj"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp1).get(0).getBool());
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp1)
				.get(0).getAlphanum().equals("www.google.fr"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp1)
				.get(0).getItem().getLabel().equals("X"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0),
						prelImp1)
				.get(0).getTexte().equals("Test"));
		assertTrue(echantillonManager.findByPrelevementManager(prelImp1).size() == 1);

		// PRLVT4
		Prelevement prelImp2 = prelevementManager.findByCodeLikeManager("PRLVT4", true).get(0);
		assertTrue(prelImp2.getNature().getNom().equals("LIQUIDE D'ASCITE"));
		assertTrue(prelImp2.getConsentType().getNom().equals("DECEDE"));
		assertTrue(prelImp2.getPreleveur().getNom().equals("VIAL"));
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp2)
				.get(0).getAlphanum().equals("fd"));
		assertFalse(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp2).get(0).getBool());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp2).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp2)
				.get(0).getItem().getLabel().equals("5"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0),
						prelImp2)
				.get(0).getTexte().equals("Test"));
		assertTrue(echantillonManager.findByPrelevementManager(prelImp2).size() == 0);

		// PRLVT5612
		Prelevement prelImp3 = prelevementManager.findByCodeLikeManager("PRLVT5612", true).get(0);
		assertTrue(prelImp3.getNature().getNom().equals("SANG"));
		assertTrue(prelImp3.getConsentType().getNom().equals("DECEDE"));
		assertTrue(prelImp3.getPreleveur().getNom().equals("XIE"));
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp3)
				.get(0).getAlphanum().equals("lnnll"));
		assertFalse(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp3).get(0).getBool());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp3).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp3)
				.get(0).getItem().getLabel().equals("4"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0),
						prelImp3)
				.get(0).getTexte().equals("raioahj"));
		assertTrue(echantillonManager.findByPrelevementManager(prelImp3).size() == 1);

		// MARTINE NONO
		Patient patImp4 = patientManager.findByNomLikeManager("NONO", true).get(0);
		assertTrue(patImp4.getNip().equals("64543134"));

		// PRLVT98
		Prelevement prelImp4 = prelevementManager.findByCodeLikeManager("PRLVT98", true).get(0);
		assertTrue(prelImp4.getNature().getNom().equals("TISSU"));
		assertTrue(prelImp4.getConsentType().getNom().equals("DECEDE"));
		assertTrue(prelImp4.getPreleveur().getNom().equals("VENTADOUR"));
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp4)
				.get(0).getAlphanum().equals("ijsij"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp4).get(0).getBool());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp4).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp4)
				.get(0).getItem().getLabel().equals("X"));
		assertTrue(annotationValeurManager.findByChampAndObjetManager(
				champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0), prelImp4).isEmpty());
		assertTrue(echantillonManager.findByPrelevementManager(prelImp4).size() == 1);

		// modifies les prélèvements importés
		ImportHistorique ih2 = null;
		errors.clear();
		try{
			ih2 = importManager.importFileManager(template, u, wb.getSheetAt(1));
		}catch(final RuntimeException re){
			errors = ((ErrorsInImportException) re).getErrors();
			re.printStackTrace();
		}
		assertTrue(errors.size() == 0);
		assertNotNull(ih2);
		assertTrue(importHistoriqueManager.findImportationsByHistoriqueManager(ih2).size() == 6);
		assertTrue(importHistoriqueManager.findImportationsByHistoriqueAndEntiteManager(ih2, entiteDao.findById(1)).size() == 2);
		for(final Importation imp : importHistoriqueManager.findImportationsByHistoriqueManager(ih2)){
			assertTrue(imp.getIsUpdate());
		}

		// FRANCOIS MEUNIER
		patImp1 = patientManager.findByNipLikeManager("152115453", true).get(0);
		assertTrue(patImp1.getPatientEtat().equals("D"));

		// PRLVT456
		prelImp1 = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
		// assertTrue(prelImp1.getNature().getNom().equals("TISSU"));
		// assertTrue(prelImp1.getConsentType().getNom().equals("DECEDE"));
		// assertTrue(prelImp1.getPreleveur().getNom().equals("VENTADOUR"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp1).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp1).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp1).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp1)
				.isEmpty());
		assertTrue(annotationValeurManager.findByChampAndObjetManager(
				champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0), prelImp1).isEmpty());
		assertTrue(echantillonManager.findByPrelevementManager(prelImp1).size() == 1);

		// PRLVT4
		prelImp2 = prelevementManager.findByCodeLikeManager("PRLVT4", true).get(0);
		assertTrue(prelImp2.getNature().getNom().equals("LIQUIDE D'ASCITE"));
		assertTrue(prelImp2.getConsentType().getNom().equals("DECEDE"));
		assertTrue(prelImp2.getPreleveur().getNom().equals("VIAL"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp2).size() == 1);
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp2)
				.get(0).getAlphanum().equals("up1"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp2).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp2).get(0).getBool());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp2).size() == 1);
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp2)
				.get(0).getAlphanum().equals("link1"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp2)
				.size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp2)
				.get(0).getItem().getLabel().equals("X"));
		assertTrue(annotationValeurManager.findByChampAndObjetManager(
				champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0), prelImp2).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0),
						prelImp2)
				.get(0).getTexte().equals("up2"));
		assertTrue(echantillonManager.findByPrelevementManager(prelImp2).size() == 0);

		// PRLVT5612
		prelImp3 = prelevementManager.findByCodeLikeManager("PRLVT5612", true).get(0);
		assertTrue(prelImp3.getNature().getNom().equals("SANG"));
		// assertTrue(prelImp3.getConsentType().getNom().equals("EN ATTENTE"));
		assertTrue(prelImp3.getPreleveur().getNom().equals("XIE"));
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp3)
				.get(0).getAlphanum().equals("lnnll"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp3).get(0).getBool());
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp3)
				.get(0).getAlphanum().equals("link2"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp3)
				.isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0),
						prelImp3)
				.get(0).getTexte().equals("raioahj"));
		assertTrue(echantillonManager.findByPrelevementManager(prelImp3).size() == 1);

		// MARTINE NONO
		patImp4 = patientManager.findByNomLikeManager("NONO", true).get(0);
		assertTrue(patImp4.getNip().equals("55667"));

		// PRLVT98
		prelImp4 = prelevementManager.findByCodeLikeManager("PRLVT98", true).get(0);
		assertTrue(prelImp4.getNature().getNom().equals("TISSU"));
		assertTrue(prelImp4.getConsentType().getNom().equals("DECEDE"));
		// assertTrue(prelImp4.getPreleveur().getNom().equals("XIE"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Alphanum2").get(0), prelImp4).isEmpty());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Bool1").get(0), prelImp4).get(0).getBool());
		assertTrue(
				annotationValeurManager.findByChampAndObjetManager(champAnnotationManager.findByNomManager("Link1").get(0), prelImp4)
				.get(0).getAlphanum().equals("link3"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomLikeManager("009 : version cTNM", false).get(0), prelImp4)
				.get(0).getItem().getLabel().equals("X"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationManager.findByNomManager("065 : Champs spécifique du type cancer").get(0),
						prelImp4)
				.get(0).getTexte().equals("up3"));
		assertTrue(echantillonManager.findByPrelevementManager(prelImp4).size() == 1);

		// Removes
		importHistoriqueManager.removeObjectManager(ih);
		importHistoriqueManager.removeObjectManager(ih2);
		assertTrue(importHistoriqueManager.findAllObjectsManager().size() == 3);
		final ProdDerive derive1 = prodDeriveManager.findByCodeLikeManager("PRLVT456.1.1", true).get(0);
		final Emplacement empl1 = prodDeriveManager.getEmplacementManager(derive1);
		empl1.setEntite(null);
		empl1.setObjetId(null);
		empl1.setVide(true);
		emplacementManager.updateObjectManager(empl1, empl1.getTerminale(), null);
		prodDeriveManager.removeObjectCascadeManager(derive1, null, u, null);
		final ProdDerive derive2 = prodDeriveManager.findByCodeLikeManager("PRLVT5612.1.2", true).get(0);
		final Emplacement empl2 = prodDeriveManager.getEmplacementManager(derive2);
		empl2.setEntite(null);
		empl2.setObjetId(null);
		empl2.setVide(true);
		emplacementManager.updateObjectManager(empl2, empl1.getTerminale(), null);
		prodDeriveManager.removeObjectCascadeManager(derive2, null, u, null);
		final ProdDerive derive3 = prodDeriveManager.findByCodeLikeManager("PRLVT98.5.1", true).get(0);
		prodDeriveManager.removeObjectCascadeManager(derive3, null, u, null);
		final Echantillon echan1 = echantillonManager.findByCodeLikeManager("PRLVT456.1", true).get(0);
		final Emplacement empl3 = echantillonManager.getEmplacementManager(echan1);
		empl3.setEntite(null);
		empl3.setObjetId(null);
		empl3.setVide(true);
		emplacementManager.updateObjectManager(empl3, empl1.getTerminale(), null);
		echan1.setEmplacement(null);
		echan1.setObjetStatut(objetStatutDao.findById(4)); // non stocke
		echantillonManager.updateObjectManager(echan1, echan1.getBanque(), echan1.getPrelevement(), null, null, null,
				echan1.getEchantillonType(), null, null, null, null, null, null, null, null, null, u, true, null, null);
		echantillonManager.removeObjectCascadeManager(echan1, null, u, null);
		final Echantillon echan2 = echantillonManager.findByCodeLikeManager("PRLVT5612.1", true).get(0);
		final Emplacement empl4 = echantillonManager.getEmplacementManager(echan2);
		empl4.setEntite(null);
		empl4.setObjetId(null);
		empl4.setVide(true);
		emplacementManager.updateObjectManager(empl4, empl1.getTerminale(), null);
		echantillonManager.removeObjectCascadeManager(echan2, null, u, null);
		final Echantillon echan3 = echantillonManager.findByCodeLikeManager("PRLVT98.5", true).get(0);
		echantillonManager.removeObjectCascadeManager(echan3, null, u, null);
		final Prelevement prlvt1 = prelevementManager.findByCodeLikeManager("PRLVT456", true).get(0);
		prelevementManager.removeObjectManager(prlvt1, null, u, null);
		final Prelevement prlvt2 = prelevementManager.findByCodeLikeManager("PRLVT5612", true).get(0);
		prelevementManager.removeObjectManager(prlvt2, null, u, null);
		final Prelevement prlvt3 = prelevementManager.findByCodeLikeManager("PRLVT98", true).get(0);
		prelevementManager.removeObjectManager(prlvt3, null, u, null);
		final Prelevement prlvt4 = prelevementManager.findByCodeLikeManager("PRLVT4", true).get(0);
		prelevementManager.removeObjectManager(prlvt4, null, u, null);
		final Maladie mal1 = maladieManager.findByLibelleLikeManager("TUMEUR", true).get(0);
		maladieManager.removeObjectManager(mal1, null, u);
		final Maladie mal2 = maladieManager.findByLibelleLikeManager("CANCER", true).get(0);
		maladieManager.removeObjectManager(mal2, null, u);
		final Maladie mal3 = maladieManager.findByLibelleLikeManager("VIEILLESSE", true).get(0);
		maladieManager.removeObjectManager(mal3, null, u);
		final Patient pat1 = patientManager.findByNomLikeManager("MEUNIER", true).get(0);
		patientManager.removeObjectManager(pat1, null, u, null);
		final Patient pat2 = patientManager.findByNomLikeManager("MULLER", true).get(0);
		patientManager.removeObjectManager(pat2, null, u, null);
		final Patient pat3 = patientManager.findByNomLikeManager("NONO", true).get(0);
		patientManager.removeObjectManager(pat3, null, u, null);

		assertTrue(patientManager.findAllObjectsManager().size() == 5);
		assertTrue(maladieManager.findAllObjectsManager().size() == 6);
		assertTrue(prelevementManager.findAllObjectsManager().size() == 5);
		assertTrue(echantillonManager.findAllObjectsManager().size() == 4);
		assertTrue(prodDeriveManager.findAllObjectsManager().size() == 4);
		assertTrue(emplacementManager.findAllObjectsManager().size() == 7);

		final List<TKFantomableObject> fs = new ArrayList<>();
		fs.add(derive1);
		fs.add(derive2);
		fs.add(derive3);
		fs.add(echan1);
		fs.add(echan2);
		fs.add(echan3);
		fs.add(prlvt1);
		fs.add(prlvt2);
		fs.add(prlvt3);
		fs.add(prlvt4);
		fs.add(pat1);
		fs.add(pat2);
		fs.add(pat3);
		fs.add(mal1);
		fs.add(mal2);
		fs.add(mal3);
		cleanUpFantomes(fs);
	}
}
