package fr.aphp.tumorotek.test.utils.io;

import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import fr.aphp.tumorotek.utils.io.ExcelUtility;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class ExcelUtilityTest {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Before
    public void setUp() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("TestSheet");
    }


    /**
     getSafeSheetName: Test de la méthode avec des caractères interdits:
     */
    @Test
    public void testSafeSheetNameWithForbiddenCharacters() {
        String input = "Sheet/Name\\With?Forbidden*Characters[]";
        String expected = "Sheet-Name-With-Forbidden-Charac";
        assertEquals(expected, ExcelUtility.getSafeSheetName(input));
    }

    /**
     getSafeSheetName: Test de la troncature du nom de feuille
     */
    @Test
    public void testSafeSheetNameTruncation() {
        String input = "ThisIsAVeryLongSheetNameThatExceedsThirtyOneCharacters";
        String expected = "ThisIsAVeryLongSheetNameThatExce";
        assertEquals(expected, ExcelUtility.getSafeSheetName(input));
    }

    /**
     getSafeSheetName: Test avec une entrée nulle:
     */
    @Test
    public void testSafeSheetNameWithNullInput() {
        // Test with null input
        String expected = "Sheet";
        assertEquals(expected, ExcelUtility.getSafeSheetName(null));
    }

    /**
     getSafeSheetName: Test avec une entrée vide:
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSafeSheetNameWithEmptyInput() {
        ExcelUtility.getSafeSheetName("");
    }


    @Test
    public void testAddFooter() {
        ExcelUtility.addFooter(sheet, "Left Footer", "Center Footer", "Right Footer");
        Footer footer = sheet.getFooter();
        assertEquals("Left Footer", footer.getLeft());
        assertEquals("Center Footer", footer.getCenter());
        assertEquals("Right Footer", footer.getRight());
    }

    @Test
    public void testAddFooterWithNull() {
        ExcelUtility.addFooter(sheet, "Left Footer", null, null);
        Footer footer = sheet.getFooter();
        assertEquals("Left Footer", footer.getLeft());
    }

    @Test
    public void testWriteToCell() {
        Cell cell = ExcelUtility.writeToCell(sheet, 0, 0, "Test Value");
        assertEquals("Test Value", cell.getStringCellValue());
        assertEquals(0, cell.getRowIndex());
        assertEquals(0, cell.getColumnIndex());
    }



    @Test
    public void testMergeCells() {
        Cell cell = ExcelUtility.mergeCells(sheet, 0, 1, 0, 1, "Merged Value");
        assertEquals("Merged Value", cell.getStringCellValue());
        assertTrue(sheet.getMergedRegions().contains(new CellRangeAddress(0, 1, 0, 1)));
    }

    @Test
    public void testApplyLeftBorderColor() {
        Cell cell = ExcelUtility.writeToCell(sheet, 0, 0, "Value");
        ExcelUtility.applyLeftBorderColor(cell, "#FF5733");
        assertEquals(BorderStyle.THICK, cell.getCellStyle().getBorderLeftEnum());
    }


    @Test
    public void testApplyTableBorderStyleOnMerge() {
        ExcelUtility.applyTableBorderStyleOnMerge(sheet, 0, 1, 0, 1, "#FF5733");
        // Check border styles; however, deeper inspection would require checking cell styles.
        Cell cell = sheet.getRow(0).getCell(0);
        assertNotNull(cell.getCellStyle());
    }



    @Test
    public void testApplyTableBorderStyle() {
        Cell cell = ExcelUtility.writeToCell(sheet, 0, 0, "Value");
        ExcelUtility.applyTableBorderStyle(cell, "#FF5733", true);
        assertEquals(BorderStyle.THICK, cell.getCellStyle().getBorderLeftEnum());
    }


    @Test
    public void testCreateSheet() {
        Sheet createdSheet = ExcelUtility.createSheet(workbook, "NewSheet");
        assertNotNull(createdSheet);
        assertEquals("NewSheet", createdSheet.getSheetName());
    }

    @Test
    public void testApplyAlignment() {
        Cell cell = ExcelUtility.writeToCell(sheet, 0, 0, "Value");
        ExcelUtility.applyAlignment(cell, AlignmentType.CENTER);
        assertEquals(HorizontalAlignment.CENTER, cell.getCellStyle().getAlignmentEnum());
    }
}
