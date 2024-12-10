package fr.aphp.tumorotek.test.utils.io;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import fr.aphp.tumorotek.utils.io.ExcelUtility;

/**
 * Classe de test pour la classe ExcelUtility.
 * Elle contient des tests unitaires pour vérifier le bon fonctionnement des méthodes utilitaires
 * utilisées pour manipuler des feuilles Excel.
 */
public class ExcelUtilityTest
{

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
    * Teste la création d'une nouvelle feuille dans le classeur.
    */
   @Test
   public void testCreateSheet(){
      Sheet createdSheet = ExcelUtility.createSheet(workbook, "NewSheet");
      assertNotNull(createdSheet);
      assertEquals("NewSheet", createdSheet.getSheetName());
   }
}
