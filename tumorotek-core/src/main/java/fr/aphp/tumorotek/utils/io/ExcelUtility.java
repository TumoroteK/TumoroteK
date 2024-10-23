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

   //CHT : on est sur une classe utilitaire : c'est à l'appelant de maîtriser les créations sinon ça complique la compréhension et le débogage
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
        //CHT : pourquoi ne pas renvoyer une IllegalArgumentException également ici ?
        //Faut-il faire planter ou juste ne pas créer ces cellules ? 
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

   //CHT : c'est simple d'écrire dans une cellule : cette surcouche complique la compréhension
   //la création des cellules doit être maîtrisée par l'appelant et non par une classe utilitaire.
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

   //CHT : Fait trop de chose pour une classe utilitaire. 
   //risque de bug car si la cellule qu'on passe a déjà un style tout est écrasé
   //mettre en gras est assez simple donc cette méthode n'est pas nécessaire
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
      //CHT : utiliser un cache pour éviter de créer une nouvelle Font à chaque fois
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

   //CHT : fusionner des cellules c'est 2 lignes de code => cette surcouche amène de la complexité dans la compréhension
   //car finalement, il y a plus de lignes de code pour ajouter la valeur...
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
      cellStyle.setAlignment(HorizontalAlignment.CENTER);//CHT : si classe utilitaire, on ne peut pas forcer arbitrairement l'alignement ou alors il faut l'indiquer dans le nom de la méthode
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//CHT : si classe utilitaire, on ne peut pas forcer arbitrairement l'alignement ou alors il faut l'indiquer dans le nom de la méthode

      // Fusionner les cellules
      CellRangeAddress region = new CellRangeAddress(startRow, endRow, startCol, endCol);
      sheet.addMergedRegion(region);
      cell.setCellStyle(cellStyle);

      return cell;
   }


   //CHT : apparemment pas utilisée
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

      //CHT : une méthode existe pour faire cela : cf commentaire au niveau de createColorFromHex()
      if(hexColor != null && hexColor.matches(VALID_HEX_COLOR_PATTERN)){
         // Convertir hex en RGB
         int red = Integer.parseInt(hexColor.substring(1, 3), 16);//CHT : aurait dû s'appuyer sur la méthode spécifique createColorFromHex()
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

   //CHT : 
   //pourquoi indiquer (par défaut 11) sur fontHeight vu que le code prend toujours la valeur en param
   //méthode assez spécifique à DocumentWithDataAsTableExcelProducer donc plutôt à mettre dans DocumentWithDataAsTableExcelProducer
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
      //CHT : Pourquoi faire planter si cell est null ? 
      //De plus les autres méthodes writeToCell crée la cellule donc ça peut créer une confusion dans l'utilisation
      if (cell == null) {
         throw new ExcelWriteException("Cell cannot be null.");
      }
      //CHT : pour normalText, italicText : une solution moins impactante serait que 
      //si normalText est null, on écrit rien en normal et si italic est null on écrit rien en italique
      //en loggant un warning
      if (normalText == null || normalText.isEmpty()) {
         throw new ExcelWriteException("Normal text cannot be null or empty.");
      }
      if (italicText == null || italicText.isEmpty()) {
         throw new ExcelWriteException("Italic text cannot be null or empty.");
      }
      //CHT try catch inutile car aucune méthode ne lance une Exception qui n'est pas une RuntimeException
      //=> elle sera gérée par les appelants
      try {
         Workbook workbook = cell.getSheet().getWorkbook();

         Font normalFont = workbook.createFont();
         normalFont.setFontHeightInPoints(fontHeight);

         Font italicFont = workbook.createFont();
         italicFont.setFontHeightInPoints(fontHeight);
         italicFont.setItalic(true);

         RichTextString richText = new XSSFRichTextString(normalText + " " + italicText);

         richText.applyFont(0, normalText.length(), normalFont);


         richText.applyFont(normalText.length() + 1, richText.length(), italicFont);//CHT le +1 fait que l'espace n'a pas de font donc potentiellement une taille différente des autres caractères...

         cell.setCellValue(richText);

      } catch (Exception e) {
         throw new ExcelWriteException("Error while writing to the cell.", e);
      }
   }

   //CHT : trop compliqué toute la mécanique mise en place pour la gestion des bordures
   //Appliquer un border est simple (2 lignes de code donc pas besoin de mettre cette surcouche sur l'api)
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

      //CHT : cette création d'un nouveau style et son application à cell écrasent le style précédemment défini => peut générer un bug
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

   //CHT : plutôt que de tester les valeurs possibles avec une regex, s'appuyer sur NumberFormatException
   //MAIS un méthode existe dans POI pour faire ça : XSSFColor xSSFColor = new XSSFColor(); xSSFColor.setARGBHex(hexCode.substring(1));
   //idéalement utiliser une map de cache pour ne pas recréer la couleur à chaque fois. clé : hexCode - valeur XSSFColor
   public static XSSFColor createColorFromHex(String color){
//      // Create XSSFColor if a valid hex color code is provided
//      if(color != null && color.matches("^#[0-9a-fA-F]{6}$")){//CHT Défini en constante donc VALID_HEX_COLOR_PATTERN à utiliser
//         int red = Integer.parseInt(color.substring(1, 3), 16);
//         int green = Integer.parseInt(color.substring(3, 5), 16);
//         int blue = Integer.parseInt(color.substring(5, 7), 16);
//         return new XSSFColor(new java.awt.Color(red, green, blue));
//      }
//      return new XSSFColor();
      
      final XSSFColor BLACK__XSSF_COLOR = new XSSFColor(new java.awt.Color(0, 0, 0));
      if(color != null) {
         try {
            int red = Integer.parseInt(color.substring(1, 3), 16);
            int green = Integer.parseInt(color.substring(3, 5), 16);
            int blue = Integer.parseInt(color.substring(5, 7), 16);
            return new XSSFColor(new java.awt.Color(red, green, blue));  
         }
         catch (NumberFormatException e) {
            //trace de debug !
            return BLACK__XSSF_COLOR;
         }
      }
      return BLACK__XSSF_COLOR;
   }

   //CHT : cette méthode existe déjà dans l'API : WorkbookUtil.createSafeSheetName(input)
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

   //CHT : à revoir pour utiliser le code standard workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
   //Par contre, il faut gérer le cas d'un doublon qui peut arriver notamment si le nom est tronqué.
   //Le plus simple est de catcher l'IllegalArgumentException renvoyée dans ce cas et appeler l'onglet "(i)" avec i le n° de feuille (nb sheet)
   //tester sheetName non null et créer aussi avec onglet n°i si null
   public static Sheet createSheet(Workbook workbook, String sheetName){
      return workbook.createSheet(getSafeSheetName(sheetName));
   }

   //CHT : cette méthode écrase le style éventuellement précédemment défini sur la cellule ...
   //il faudait plutôt récupérer le style existant sur la cellule et appliqué l'alignement
   //mais dans l'absolu, il faut gérer toutes les caractéristiques du style d'une cellule au même endroit
   //ou alors récupérer le CellStyle de la cell en param pour le compléter à chaque fois... 
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

   //CHT : trop compliqué toute la mécanique mise en place pour la gestion des bordures
   //et normalement si une classe est public, elle doit être dans son propre fichier
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

