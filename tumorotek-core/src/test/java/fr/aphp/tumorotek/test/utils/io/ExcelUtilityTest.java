package fr.aphp.tumorotek.test.utils.io;

import fr.aphp.tumorotek.manager.exception.ExcelWriteException;
import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import fr.aphp.tumorotek.utils.io.ExcelUtility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Classe de test pour la classe ExcelUtility.
 * Elle contient des tests unitaires pour vérifier le bon fonctionnement des méthodes utilitaires
 * utilisées pour manipuler des feuilles Excel.
 */
public class ExcelUtilityTest {

   /**
    * Le classeur Excel utilisé pour les tests.
    */
   private XSSFWorkbook workbook;

   /**
    * La feuille Excel sur laquelle les tests sont effectués.
    */
   private XSSFSheet sheet;

   /**
    * Configuration avant chaque test. Initialise un nouveau classeur et une feuille nommée "TestSheet".
    */
   @Before
   public void setUp(){
      workbook = new XSSFWorkbook();
      sheet = workbook.createSheet("TestSheet");
   }

   /**
    * Teste la méthode getOrCreateCell pour vérifier qu'elle ne modifie pas les cellules adjacentes.
    */
   @Test
   public void testGetOrCreateCell_doesNotEraseAdjacentCell(){
      // Crée un nouveau classeur et une nouvelle feuille
      Workbook workbook = new XSSFWorkbook();
      Sheet sheet = workbook.createSheet("Test Sheet");

      // Définit les indices de ligne et de colonne pour les tests
      int rowIndex = 0;
      int initialColIndex = 0;
      int adjacentColIndex = 1;

      // Écrit une valeur initiale dans la cellule à (ligne, colonne)
      Cell initialCell = ExcelUtility.getOrCreateCell(sheet, rowIndex, initialColIndex);
      initialCell.setCellValue("Initial Value");

      // Écrit une valeur différente dans la cellule adjacente (ligne, colonne + 1)
      Cell adjacentCell = ExcelUtility.getOrCreateCell(sheet, rowIndex, adjacentColIndex);
      adjacentCell.setCellValue("Adjacent Value");

      // Vérifie que la valeur dans la cellule initiale (ligne, colonne) est toujours intacte
      Cell retrievedInitialCell = sheet.getRow(rowIndex).getCell(initialColIndex);
      assertNotNull(retrievedInitialCell);
      assertEquals("Initial Value", retrievedInitialCell.getStringCellValue());

      // Vérifie que la cellule adjacente (ligne, colonne + 1) contient la bonne valeur
      Cell retrievedAdjacentCell = sheet.getRow(rowIndex).getCell(adjacentColIndex);
      assertNotNull(retrievedAdjacentCell);
      assertEquals("Adjacent Value", retrievedAdjacentCell.getStringCellValue());
   }

   /**
    * Teste la méthode getSafeSheetName avec des caractères interdits dans le nom de la feuille.
    */
   @Test
   public void testSafeSheetNameWithForbiddenCharacters(){
      String input = "Sheet/Name\\With?Forbidden*Characters[]";
      String expected = "Sheet-Name-With-Forbidden-Charac";
      assertEquals(expected, ExcelUtility.getSafeSheetName(input));
   }

   /**
    * Teste la troncature du nom de la feuille lorsque celui-ci dépasse la longueur maximale.
    */
   @Test
   public void testSafeSheetNameTruncation(){
      String input = "ThisIsAVeryLongSheetNameThatExceedsThirtyOneCharacters";
      String expected = "ThisIsAVeryLongSheetNameThatExce";
      assertEquals(expected, ExcelUtility.getSafeSheetName(input));
   }

   /**
    * Teste la méthode getSafeSheetName avec une entrée nulle pour s'assurer qu'elle retourne un nom par défaut.
    */
   @Test
   public void testSafeSheetNameWithNullInput(){
      String expected = "Sheet";
      assertEquals(expected, ExcelUtility.getSafeSheetName(null));
   }

   /**
    * Teste la méthode getSafeSheetName avec une entrée vide et attend une exception IllegalArgumentException.
    */
   @Test(expected = ExcelWriteException.class)
   public void testSafeSheetNameWithEmptyInput(){
      ExcelUtility.getSafeSheetName("");
   }

   /**
    * Teste la méthode addFooter pour vérifier l'ajout d'un pied de page avec les valeurs spécifiées.
    */
   @Test
   public void testAddFooter(){
      ExcelUtility.addFooter(sheet, "Left Footer", "Center Footer", "Right Footer");
      Footer footer = sheet.getFooter();
      assertEquals("Left Footer", footer.getLeft());
      assertEquals("Center Footer", footer.getCenter());
      assertEquals("Right Footer", footer.getRight());
   }

   /**
    * Teste la méthode addFooter avec des valeurs nulles pour certains éléments du pied de page.
    */
   @Test
   public void testAddFooterWithNull(){
      ExcelUtility.addFooter(sheet, "Left Footer", null, null);
      Footer footer = sheet.getFooter();
      assertEquals("Left Footer", footer.getLeft());
   }

   /**
    * Teste la méthode writeToCell pour vérifier l'écriture correcte d'une valeur dans une cellule.
    */
   @Test
   public void testWriteToCell(){
      Cell cell = ExcelUtility.writeToCell(sheet, 0, 0, "Test Value");
      assertEquals("Test Value", cell.getStringCellValue());
      assertEquals(0, cell.getRowIndex());
      assertEquals(0, cell.getColumnIndex());
   }

   /**
    * Teste la méthode mergeCells pour vérifier la fusion de cellules et l'écriture d'une valeur fusionnée.
    */
   @Test
   public void testMergeCells(){
      Cell cell = ExcelUtility.mergeCells(sheet, 0, 1, 0, 1, "Merged Value");
      assertEquals("Merged Value", cell.getStringCellValue());
      assertTrue(sheet.getMergedRegions().contains(new CellRangeAddress(0, 1, 0, 1)));
   }

   /**
    * Teste l'application du style de bordure sur une plage de cellules fusionnées.
    */
   @Test
   public void testApplyTableBorderStyleOnMerge(){
      ExcelUtility.applyTableBorderStyleOnMerge(sheet, 0, 1, 0, 1, "#FF5733");
      // Vérifie les styles de bordure; une inspection plus approfondie nécessite de vérifier les styles des cellules.
      Cell cell = sheet.getRow(0).getCell(0);
      assertNotNull(cell.getCellStyle());
   }

   /**
    * Teste la création d'une nouvelle feuille dans le classeur.
    */
   @Test
   public void testCreateSheet(){
      Sheet createdSheet = ExcelUtility.createSheet(workbook, "NewSheet");
      assertNotNull(createdSheet);
      assertEquals("NewSheet", createdSheet.getSheetName());
   }

   /**
    * Teste l'application de l'alignement horizontal sur une cellule.
    */
   @Test
   public void testApplyAlignment(){
      Cell cell = ExcelUtility.writeToCell(sheet, 0, 0, "Value");
      ExcelUtility.applyAlignment(cell, AlignmentType.CENTER);
      assertEquals(HorizontalAlignment.CENTER, cell.getCellStyle().getAlignmentEnum());
   }
}
