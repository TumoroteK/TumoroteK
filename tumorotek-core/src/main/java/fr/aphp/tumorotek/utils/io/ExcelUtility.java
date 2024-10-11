package fr.aphp.tumorotek.utils.io;

import fr.aphp.tumorotek.manager.exception.ExcelWriteException;
import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

/**
 * Classe utilitaire pour la manipulation de feuilles Excel à l'aide de la bibliothèque Apache POI.
 * Cette classe fournit une série de méthodes statiques pour faciliter les opérations sur
 * les cellules, les lignes et les feuilles d'un document Excel.
 *
 */
public class ExcelUtility
{

   private static final String VALID_HEX_COLOR_PATTERN = "^#[0-9A-Fa-f]{6}$";

   private ExcelUtility(){
      throw new IllegalStateException("Utility class");
   }

   /**
    * Récupère une cellule existante ou en crée une nouvelle dans la feuille spécifiée aux indices de ligne et de colonne donnés.
    *
    * @param sheet    La feuille Excel où la cellule sera créée/récupérée.
    * @param rowIndex L'indice de ligne (base zéro) de la cellule.
    * @param colIndex L'indice de colonne (base zéro) de la cellule.
    * @return Un objet Cell à l'emplacement spécifié ; jamais null.
    * @throws IllegalArgumentException si la feuille est nulle ou si les indices sont négatifs.
    */
   public static Cell getOrCreateCell(Sheet sheet, int rowIndex, int colIndex){
      // Valider les paramètres d'entrée
      if(sheet == null){
         throw new IllegalArgumentException("Sheet cannot be null.");
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
   public static Cell writeToCell(Sheet sheet, int rowIndex, int colIndex, String value){
      // Récupérer ou créer la cellule
      Cell cell = getOrCreateCell(sheet, rowIndex, colIndex);

      // Définir la valeur de la cellule
      String valueToWrite = (value != null) ? value : "";
      cell.setCellValue(valueToWrite);

      return cell;
   }

   /**
    * Écrit une valeur de chaîne dans une cellule spécifiée de la feuille avec un formatage en gras.
    *
    * @param sheet    La feuille où la cellule est située.
    * @param rowIndex L'indice de ligne (base zéro) où se trouve la cellule.
    * @param colIndex L'indice de colonne (base zéro) où se trouve la cellule.
    * @param value    La valeur de chaîne à écrire dans la cellule.
    * @return La cellule où la valeur a été écrite.
    */
   public static Cell writeToCellWithBold(Sheet sheet, int rowIndex, int colIndex, String value){

      // Récupérer la cellule, la créer si elle n'existe pas
      Cell cell = writeToCell(sheet, rowIndex, colIndex, value);

      // Obtenir le classeur à partir de la feuille
      Workbook workbook = sheet.getWorkbook(); // Récupère le classeur associé à la feuille

      // Créer un nouveau style de cellule avec une police en gras
      CellStyle cellStyle = workbook.createCellStyle(); // Crée un nouveau style de cellule
      Font font = workbook.createFont(); // Crée une nouvelle police
      font.setBold(true); // Définit la police en gras
      cellStyle.setFont(font); // Applique la police au style de cellule

      // Appliquer le style à la cellule
      cell.setCellStyle(cellStyle);

      return cell; // Retourne la cellule spécifiée après l'écriture et l'application du style
   }

   /**
    * Écrit une valeur de chaîne dans une cellule spécifiée de la feuille avec un formatage en gras optionnel.
    *
    * @param sheet    La feuille où la cellule est située.
    * @param rowIndex L'indice de ligne (base zéro) où se trouve la cellule.
    * @param colIndex L'indice de colonne (base zéro) où se trouve la cellule.
    * @param value    La valeur de chaîne à écrire dans la cellule.
    * @param isBold   Indique si le texte doit être en gras ou non.
    * @return La cellule où la valeur a été écrite.
    */
   public static Cell writeToCellWithOptionalBold(Sheet sheet, int rowIndex, int colIndex, String value, boolean isBold){
      if(isBold){
         return writeToCellWithBold(sheet, rowIndex, colIndex, value);
      }else{
         return writeToCell(sheet, rowIndex, colIndex, value);
      }
   }

   /**
    * Fusionne des cellules dans une région rectangulaire et définit la valeur de la cellule ainsi que le style.
    *
    * @param sheet     La feuille où les cellules seront fusionnées.
    * @param startRow  L'indice de ligne de départ (base zéro) pour la fusion.
    * @param endRow    L'indice de ligne de fin (base zéro) pour la fusion.
    * @param startCol  L'indice de colonne de départ (base zéro) pour la fusion.
    * @param endCol    L'indice de colonne de fin (base zéro) pour la fusion.
    * @param cellValue La valeur à définir dans la cellule fusionnée.
    * @return La cellule qui a été fusionnée et stylisée.
    */
   public static Cell mergeCells(Sheet sheet, int startRow, int endRow, int startCol, int endCol, String cellValue){

      if(startRow == endRow && startCol == endCol){

         return writeToCell(sheet, startRow, startCol, cellValue);

      }
      // Utiliser writeToCell pour gérer la création de lignes et de cellules et définir la valeur
      Cell cell = writeToCell(sheet, startRow, startCol, cellValue);

      // Créer et appliquer un style de cellule pour centrer le texte
      Workbook wb = sheet.getWorkbook();
      CellStyle cellStyle = wb.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

      // Fusionner les cellules
      CellRangeAddress region = new CellRangeAddress(startRow, endRow, startCol, endCol);
      sheet.addMergedRegion(region);
      cell.setCellStyle(cellStyle);

      return cell;
   }



   /**
    * Applique des styles de bordure à une plage de cellules avec une couleur de bordure gauche personnalisée en option.
    *
    * @param sheet    La feuille où les styles de bordure seront appliqués.
    * @param startRow L'indice de la ligne de départ (basé sur zéro) pour la bordure.
    * @param endRow   L'indice de la ligne de fin (basé sur zéro) pour la bordure.
    * @param startCol L'indice de la colonne de départ (basé sur zéro) pour la bordure.
    * @param endCol   L'indice de la colonne de fin (basé sur zéro) pour la bordure.
    * @param hexColor Le code couleur hexadécimal pour la bordure gauche (ex : "#FF5733").
    */
   public static void applyTableBorderStyleOnMerge(Sheet sheet, int startRow, int endRow, int startCol, int endCol,
      String hexColor){
      Workbook workbook = sheet.getWorkbook();
      XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

      XSSFColor blackColor = new XSSFColor(java.awt.Color.BLACK);
      XSSFColor leftBorderColor = blackColor;
      BorderStyle leftBorderStyle = BorderStyle.THIN;

      if(hexColor != null && hexColor.matches(VALID_HEX_COLOR_PATTERN)){
         // Convertir hex en RGB
         int red = Integer.parseInt(hexColor.substring(1, 3), 16);
         int green = Integer.parseInt(hexColor.substring(3, 5), 16);
         int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

         leftBorderColor = new XSSFColor(new java.awt.Color(red, green, blue));
         leftBorderStyle = BorderStyle.THICK;
      }

      // Appliquer le style de bordure
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setTopBorderColor(blackColor);

      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setRightBorderColor(blackColor);

      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBottomBorderColor(blackColor);

      cellStyle.setBorderLeft(leftBorderStyle);
      cellStyle.setLeftBorderColor(leftBorderColor);

      // Centrer l'alignement du texte
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

      // Appliquer le style de cellule à chaque cellule de la plage
      for(int rowNum = startRow; rowNum <= endRow; rowNum++){
         Row row = sheet.getRow(rowNum);
         if(row == null){
            row = sheet.createRow(rowNum);
         }

         for(int colNum = startCol; colNum <= endCol; colNum++){
            Cell cell = row.getCell(colNum);
            if(cell == null){
               cell = row.createCell(colNum);
            }
            cell.setCellStyle(cellStyle);
         }
      }
   }


   /**
    * Écrit deux chaînes de caractères dans une cellule fournie, avec la première chaîne
    * en police normale et la deuxième chaîne en italique. Un espace est toujours inclus entre les deux chaînes.
    *
    * @param cell La cellule où le texte sera écrit.
    * @param normalText Le texte à écrire en police normale.
    * @param italicText Le texte à écrire en italique.
    * @param fontHeight La hauteur des polices en points (par défaut, 11).
    * @throws ExcelWriteException si un argument est nul ou vide.
    */
   public static void writeToCellWithHalfItalic(Cell cell, String normalText, String italicText, short fontHeight) {
      if (cell == null) {
         throw new ExcelWriteException("Cell cannot be null.");
      }
      if (normalText == null || normalText.isEmpty()) {
         throw new ExcelWriteException("Normal text cannot be null or empty.");
      }
      if (italicText == null || italicText.isEmpty()) {
         throw new ExcelWriteException("Italic text cannot be null or empty.");
      }

      try {
         Workbook workbook = cell.getSheet().getWorkbook();

         Font normalFont = workbook.createFont();
         normalFont.setFontHeightInPoints(fontHeight);

         Font italicFont = workbook.createFont();
         italicFont.setFontHeightInPoints(fontHeight);
         italicFont.setItalic(true);

         RichTextString richText = new XSSFRichTextString(normalText + " " + italicText);

         richText.applyFont(0, normalText.length(), normalFont);


         richText.applyFont(normalText.length() + 1, richText.length(), italicFont);

         cell.setCellValue(richText);

      } catch (Exception e) {
         throw new ExcelWriteException("Error while writing to the cell.", e);
      }
   }

   /**
    * Applique les styles de bordure spécifiés à une cellule donnée.
    *
    * @param cell      La cellule à laquelle appliquer les bordures.
    * @param borders   La liste des propriétés de bordure à appliquer.
    * @param isToCenter Indique si le texte de la cellule doit être centré.
    *
    * @throws ExcelWriteException Si la cellule ou la liste de bordures est nulle ou vide.
    */
   public static void applyCustomBordersAndCenterContent(Cell cell, List<BorderProperties> borders, boolean isToCenter) {
      // Valider les paramètres d'entrée
      if (cell == null || borders == null || borders.isEmpty()) {
         // Vérifie que la cellule et les bordures ne sont pas nulles ou vides
         throw new ExcelWriteException("Cell and borders must not be null or empty");
      }

      Workbook workbook = cell.getSheet().getWorkbook(); // Obtenir le classeur de la cellule

      CellStyle cellStyle = workbook.createCellStyle(); // Créer un nouveau style de cellule
      for (BorderProperties border : borders) { // Parcourir chaque propriété de bordure
         XSSFColor customColor = createColorFromHex(border.getColor()); // Créer une couleur à partir du code hexadécimal

         // Appliquer la bordure en fonction de la direction spécifiée
         switch (border.getBorderDirection()) {
            case LEFT:
               cellStyle.setBorderLeft(border.getStyle()); // Appliquer le style de bordure gauche
               ((XSSFCellStyle) cellStyle).setLeftBorderColor(customColor); // Appliquer la couleur de bordure gauche
               break;
            case RIGHT:
               cellStyle.setBorderRight(border.getStyle()); // Appliquer le style de bordure droite
               ((XSSFCellStyle) cellStyle).setRightBorderColor(customColor); // Appliquer la couleur de bordure droite
               break;
            case TOP:
               cellStyle.setBorderTop(border.getStyle()); // Appliquer le style de bordure supérieure
               ((XSSFCellStyle) cellStyle).setTopBorderColor(customColor); // Appliquer la couleur de bordure supérieure
               break;
            case BOTTOM:
               cellStyle.setBorderBottom(border.getStyle()); // Appliquer le style de bordure inférieure
               ((XSSFCellStyle) cellStyle).setBottomBorderColor(customColor); // Appliquer la couleur de bordure inférieure
               break;
         }
      }

      // Appliquer les styles à la cellule spécifiée
      cell.setCellValue(cell.getStringCellValue());  // S'assurer qu'elle conserve sa valeur
      cell.setCellStyle(cellStyle); // Appliquer le style de cellule

      // Centrer le texte si spécifié
      if (isToCenter) {
         CellUtil.setAlignment(cell, HorizontalAlignment.CENTER); // Centrer l'alignement horizontal
         CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER); // Centrer l'alignement vertical
      }
   }


   private static XSSFColor createColorFromHex(String color){
      // Create XSSFColor if a valid hex color code is provided
      if(color != null && color.matches("^#[0-9a-fA-F]{6}$")){
         int red = Integer.parseInt(color.substring(1, 3), 16);
         int green = Integer.parseInt(color.substring(3, 5), 16);
         int blue = Integer.parseInt(color.substring(5, 7), 16);
         return new XSSFColor(new java.awt.Color(red, green, blue));
      }
      return new XSSFColor();

   }

   /**
    * Renvoie une chaîne de caractères conforme aux restrictions de nom de fichier
    * dans certaines applications. Remplace les caractères interdits et tronque
    * le nom à une longueur maximale de 31 caractères.
    *
    * @param input Le nom initial fourni par l'utilisateur.
    *              Si null, le nom par défaut "DefaultName" est utilisé.
    * @return Un nom sécurisé, avec les caractères interdits remplacés par des
    * caractères sûrs et une longueur maximale de 31 caractères.
    * @throws ExcelWriteException Si le nom résultant est vide
    *                                  après le traitement des caractères interdits.
    */
   public static String getSafeSheetName(String input){
      if(input == null){
         return "Sheet";  // Renvoie "Sheet" pour une entrée nulle
      }

      // Remplacement des caractères interdits par des caractères sûrs
      String safeName =
         input.replace("/", "-").replace("\\", "-").replace("?", "-").replace("*", "-").replace("[", "(").replace("]", ")");

      // Tronque à 31 caractères si nécessaire
      if(safeName.length() > 31){
         safeName = safeName.substring(0, 32);
      }

      // Vérifie si le nom est vide après traitement
      if(safeName.isEmpty()){
         throw new ExcelWriteException("Le nom de la feuille ne peut pas être vide après traitement.");
      }

      return safeName;
   }

   public static Sheet createSheet(Workbook workbook, String sheetName){
      return workbook.createSheet(getSafeSheetName(sheetName));
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

   /**
    * Représente les propriétés d'une bordure pour une cellule dans une feuille Excel.
    */
   public static class BorderProperties
   {
      private final BorderStyle style;

      private final String color;  // Code couleur hexadécimal de la bordure

      private final BorderDirection borderDirection;  // left, right, top , bottom

      public BorderProperties(BorderStyle style, String color, BorderDirection borderDirection){
         this.style = style;
         this.color = color;
         this.borderDirection = borderDirection;
      }

      public BorderStyle getStyle(){
         return style;
      }

      public String getColor(){
         return color;
      }

      public BorderDirection getBorderDirection(){
         return borderDirection;
      }
   }

   /**
    * Enumération représentant les directions possibles pour appliquer des bordures.
    */
   public enum BorderDirection
   {
      LEFT("left"), RIGHT("right"), TOP("top"), BOTTOM("bottom");

      private final String direction;

      BorderDirection(String direction){
         this.direction = direction;
      }

      @Override
      public String toString(){
         return direction;
      }
   }

}

