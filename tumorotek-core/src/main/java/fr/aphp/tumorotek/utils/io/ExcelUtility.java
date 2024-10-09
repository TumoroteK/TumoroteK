package fr.aphp.tumorotek.utils.io;

import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;


/**
 * Classe utilitaire pour la manipulation de feuilles Excel à l'aide de la bibliothèque Apache POI.
 * Cette classe fournit une série de méthodes statiques pour faciliter les opérations sur
 * les cellules, les lignes et les feuilles d'un document Excel.
 *
 */
public class ExcelUtility {

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
    public static Cell getOrCreateCell(Sheet sheet, int rowIndex, int colIndex) {
        // Valider les paramètres d'entrée
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet cannot be null.");
        }
        if (rowIndex < 0 || colIndex < 0) {
            throw new IllegalArgumentException("Row index and column index must be non-negative.");
        }

        // Retrieve the existing row or create it if it does not exist
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        // Retrieve the existing cell or create it if it does not exist
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        return cell;
    }

    /**
     * Ajoute un pied de page à la feuille donnée avec les chaînes spécifiées.
     *
     * @param sheet        La feuille où le pied de page sera ajouté.
     * @param leftString   La chaîne à définir sur le côté gauche du pied de page.
     * @param centerString La chaîne à définir au centre.
     * @param rightString  La chaîne à définir sur le côté droit du pied de page.
     */
    public static void addFooter(Sheet sheet, String leftString, String centerString, String rightString) {
        Footer footer = sheet.getFooter();
        if (leftString != null) {
            footer.setLeft(leftString);
        }
        if (centerString != null) {
            footer.setCenter(centerString);
        }
        if (rightString != null) {
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
    public static Cell writeToCell(Sheet sheet, int rowIndex, int colIndex, String value) {
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
    public static Cell writeToCellWithBold(Sheet sheet, int rowIndex, int colIndex, String value) {

        // Retrieve the cell, create it if it does not exist
        Cell cell = writeToCell(sheet, rowIndex, colIndex, value);

        // Get the workbook from the sheet
        Workbook workbook = sheet.getWorkbook();

        // Create a new cell style with bold font
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true); // Set the font to bold
        cellStyle.setFont(font);

        // Apply the style to the cell
        cell.setCellStyle(cellStyle);

        return cell;
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
    public static Cell writeToCellWithOptionalBold(Sheet sheet, int rowIndex, int colIndex, String value, boolean isBold) {
        if (isBold) {
            return writeToCellWithBold(sheet, rowIndex, colIndex, value);
        } else {
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
    public static Cell mergeCells(Sheet sheet, int startRow, int endRow, int startCol, int endCol, String cellValue) {


        if (startRow == endRow && startCol == endCol) {

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
     * Applique une couleur hexadécimale à la bordure gauche d'une cellule.
     *
     * @param cell     La cellule à laquelle la couleur sera appliquée.
     * @param hexColor Le code couleur hexadécimal (par exemple, "#FF5733").
     * @throws IllegalArgumentException si le hexColor n'est pas au format "#RRGGBB".
     */
    public static void applyLeftBorderColor(Cell cell, String hexColor) {
        if (hexColor != null && hexColor.matches(VALID_HEX_COLOR_PATTERN)) {
            // Convertir hex en RGB
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

            // Create a new XSSFColor object
            XSSFColor xssfColor = new XSSFColor(new java.awt.Color(red, green, blue));

            // Create a new cell style
            Workbook workbook = cell.getSheet().getWorkbook();
            XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setLeftBorderColor(xssfColor);

            // Apply the cell style to the cell
            cell.setCellStyle(cellStyle);
        }
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
    public static void applyTableBorderStyleOnMerge(Sheet sheet, int startRow, int endRow, int startCol, int endCol, String hexColor) {
        Workbook workbook = sheet.getWorkbook();
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

        XSSFColor blackColor = new XSSFColor(java.awt.Color.BLACK);
        XSSFColor leftBorderColor = blackColor;
        BorderStyle leftBorderStyle = BorderStyle.THIN;

        if (hexColor != null && hexColor.matches(VALID_HEX_COLOR_PATTERN)) {
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
        for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }

            for (int colNum = startCol; colNum <= endCol; colNum++) {
                Cell cell = row.getCell(colNum);
                if (cell == null) {
                    cell = row.createCell(colNum);
                }
                cell.setCellStyle(cellStyle);
            }
        }
    }

    /**
     * Écrit deux chaînes dans une cellule fournie avec la première chaîne en police normale et la seconde en italique.
     *
     * @param cell        La cellule dans laquelle écrire.
     * @param normalText  Le texte à écrire en police normale.
     * @param italicText  Le texte à écrire en police italique.
     */
    public static void writeToCellWithHalfItalic(Cell cell, String normalText, String italicText) {

        if (cell == null) {
            throw new NullPointerException("Cell cannot be null");
        }
        if (normalText == null) {
            throw new NullPointerException("Normal text cannot be null");
        }
        if (italicText == null) {
            throw new NullPointerException("Italic text cannot be null");
        }
        Workbook workbook = cell.getSheet().getWorkbook();

        // Créer les polices
        Font normalFont = workbook.createFont();
        normalFont.setFontHeightInPoints((short) 11);

        Font italicFont = workbook.createFont();
        italicFont.setFontHeightInPoints((short) 11);
        italicFont.setItalic(true);

        // Créer RichTextString
        RichTextString richTextString = new XSSFRichTextString(normalText + italicText);
        richTextString.applyFont(0, normalText.length(), normalFont);
        richTextString.applyFont(normalText.length(), normalText.length() + italicText.length(), italicFont); // Apply italic font to the second part

        // Définir le RichTextString dans la cellule
        cell.setCellValue(richTextString);
    }



    /**
     * Applique des styles de bordure à une cellule unique avec une couleur de bordure gauche personnalisée en option.
     *
     * @param cell     La cellule à laquelle les styles de bordure seront appliqués.
     * @param hexColor Le code couleur hexadécimal pour la bordure gauche (ex : "#FF5733").
     * @param isToCenter Indique si le texte doit être centré.
     */
    public static void applyTableBorderStyle(Cell cell, String hexColor, boolean isToCenter) {
        Workbook workbook = cell.getSheet().getWorkbook();
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // Couleurs par défaut et styles de bordure
        XSSFColor blackColor = new XSSFColor(java.awt.Color.BLACK);
        XSSFColor leftBorderColor = blackColor;
        BorderStyle leftBorderStyle = BorderStyle.THIN;

        // Si une couleur hexadécimale valide est fournie, l'utiliser pour la bordure gauche
        if (hexColor != null && hexColor.matches(VALID_HEX_COLOR_PATTERN)) {
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

            // Créer un nouvel objet XSSFColor pour la couleur de bordure personnalisée
            leftBorderColor = new XSSFColor(new java.awt.Color(red, green, blue));
            leftBorderStyle = BorderStyle.THICK;
        }

        // Appliquer les styles de bordure
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(blackColor);

        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(blackColor);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(blackColor);

        cellStyle.setBorderLeft(leftBorderStyle);
        cellStyle.setLeftBorderColor(leftBorderColor);

        // Centrer l'alignement du texte
        if (isToCenter) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }


        // Appliquer le style à la cellule
        cell.setCellStyle(cellStyle);
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
     * @throws IllegalArgumentException Si le nom résultant est vide
     *                                  après le traitement des caractères interdits.
     */
    public static String getSafeSheetName(String input) {
        if (input == null) {
            return "Sheet";  // Renvoie "Sheet" pour une entrée nulle
        }

        // Remplacement des caractères interdits par des caractères sûrs
        String safeName = input.replace("/", "-")
                .replace("\\", "-")
                .replace("?", "-")
                .replace("*", "-")
                .replace("[", "(")
                .replace("]", ")");

        // Tronque à 31 caractères si nécessaire
        if (safeName.length() > 31) {
            safeName = safeName.substring(0, 32);
        }

        // Vérifie si le nom est vide après traitement
        if (safeName.isEmpty()) {
            throw new IllegalArgumentException("Le nom de la feuille ne peut pas être vide après traitement.");
        }

        return safeName;
    }


    public static Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(getSafeSheetName(sheetName));
    }

    public static void applyAlignment(Cell cell, AlignmentType alignmentType) {
        CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();

        switch (alignmentType) {
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

