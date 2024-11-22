package fr.aphp.tumorotek.utils.io;

import fr.aphp.tumorotek.manager.exception.ExcelWriteException;
import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * Classe utilitaire pour la manipulation de feuilles Excel à l'aide de la bibliothèque Apache POI.
 * Cette classe fournit une série de méthodes statiques pour faciliter les opérations sur
 * les cellules, les lignes et les feuilles d'un document Excel.
 *
 */
public class ExcelUtility
{


   private ExcelUtility(){
      throw new IllegalStateException("Utility class");
   }

   public static Sheet createSheet(Workbook workbook, String sheetName){
      String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
      return workbook.createSheet(safeSheetName);
   }

   /**
    * Récupère une cellule existante ou en crée une nouvelle dans la feuille spécifiée aux indices de ligne et de colonne donnés.
    *
    * @param sheet    La feuille Excel où la cellule sera créée/récupérée.
    * @param rowIndex L'indice de ligne (base zéro) de la cellule.
    * @param colIndex L'indice de colonne (base zéro) de la cellule.
    * @return Un objet Cell à l'emplacement spécifié ; jamais null.
    * @throws ExcelWriteException si la feuille est nulle ou si les indices sont négatifs.
    */
   public static Cell getOrCreateCell(Sheet sheet, int rowIndex, int colIndex) throws ExcelWriteException{
      // Valider les paramètres d'entrée
      if(sheet == null){
         throw new ExcelWriteException("Sheet cannot be null.");
      }
      if(rowIndex < 0 || colIndex < 0){
         throw new ExcelWriteException("Row index and column index must be non-negative.");
      }

      // Récupérer la ligne existante ou la créer si elle n'existe pas
      Row row = sheet.getRow(rowIndex);
      if(row == null){
         row = sheet.createRow(rowIndex); // Crée une nouvelle ligne si elle n'existe pas
      }

      // Récupérer la cellule existante ou la créer si elle n'existe pas
      Cell cell = row.getCell(colIndex);
      if(cell == null){
         cell = row.createCell(colIndex); // Crée une nouvelle cellule si elle n'existe pas
      }

      return cell;  // Retourne la cellule récupérée ou nouvellement créée
   }

   /**
    * Ajoute un pied de page à la feuille donnée avec les chaînes spécifiées.
    *
    * @param sheet        La feuille où le pied de page sera ajouté.
    * @param leftString   La chaîne à définir sur le côté gauche du pied de page.
    * @param centerString La chaîne à définir au centre.
    * @param rightString  La chaîne à définir sur le côté droit du pied de page.
    */
   public static void addFooter(Sheet sheet, String leftString, String centerString, String rightString){
      Footer footer = sheet.getFooter();
      if(leftString != null){
         footer.setLeft(leftString);
      }
      if(centerString != null){
         footer.setCenter(centerString);
      }
      if(rightString != null){
         footer.setRight(rightString);
      }
   }

   /**
    * Écrit une valeur de chaîne dans une cellule spécifiée de la feuille.
    *
    * @param sheet    La feuille où la cellule est située.
    * @param rowIndex L'indice de ligne (base zéro) où se trouve la cellule.
    * @param colIndex L'indice de colonne (base zéro) où se trouve la cellule.
    * @param value    La valeur de chaîne à écrire dans la cellule.
    * @return La cellule où la valeur a été écrite.
    */
   public static Cell writeToCell(Sheet sheet, int rowIndex, int colIndex, String value) throws ExcelWriteException{
      // Récupérer ou créer la cellule
      Cell cell = getOrCreateCell(sheet, rowIndex, colIndex);

      // Définir la valeur de la cellule
      String valueToWrite = (value != null) ? value : "";
      cell.setCellValue(valueToWrite);

      return cell;
   }





   public static void applyAlignment(Cell cell, AlignmentType alignmentType){
      CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();

      switch(alignmentType){
         case LEFT:
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            break;
         case CENTER:
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            break;
         case RIGHT:
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
            break;
      }

      cell.setCellStyle(cellStyle);
   }



}

