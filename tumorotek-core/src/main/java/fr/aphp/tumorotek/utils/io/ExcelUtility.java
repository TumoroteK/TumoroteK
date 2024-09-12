package fr.aphp.tumorotek.utils.io;

import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

public class ExcelUtility {

    /**
     * Adds a footer to the given sheet with specified left and right strings.
     *
     * @param sheet        The sheet where the footer will be added.
     * @param leftString   The string to set on the left side of the footer.
     * @param centerString The string to set on the center.
     * @param rightString  The string to set on the right side of the footer.
     */
    public static void addFooter(Sheet sheet, String leftString, String centerString, String rightString) {
        Footer footer = sheet.getFooter();
        footer.setLeft(leftString);
        footer.setCenter(centerString);
        footer.setRight(rightString);
    }


    /**
     * Writes a string value to a specified cell in the sheet.
     *
     * @param sheet    The sheet where the cell is located.
     * @param rowIndex The row index (zero-based) where the cell is located.
     * @param colIndex The column index (zero-based) where the cell is located.
     * @param value    The string value to write to the cell.
     * @return The cell where the value was written.
     */
    public static Cell writeToCell(Sheet sheet, int rowIndex, int colIndex, String value) {


        // Retrieve the row, create it if it does not exist
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        // Retrieve the cell, create it if it does not exist
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        // Set the value of the cell
        cell.setCellValue(value);

        return cell;
    }

    /**
     * Writes a string value to a specified cell in the sheet with bold formatting.
     *
     * @param sheet    The sheet where the cell is located.
     * @param rowIndex The row index (zero-based) where the cell is located.
     * @param colIndex The column index (zero-based) where the cell is located.
     * @param value    The string value to write to the cell.
     * @return The cell where the value was written.
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


    public static Cell writeToCellWithBold(Sheet sheet, int rowIndex, int colIndex, String value, boolean isBold) {
        if (isBold) {
            return writeToCellWithBold(sheet, rowIndex, colIndex, value);
        } else {
            return writeToCell(sheet, rowIndex, colIndex, value);
        }
    }

    /**
     * Merges cells in a rectangular region and sets the cell value and style.
     *
     * @param sheet     The sheet where cells will be merged.
     * @param startRow  The starting row index (zero-based) for the merge.
     * @param endRow    The ending row index (zero-based) for the merge.
     * @param startCol  The starting column index (zero-based) for the merge.
     * @param endCol    The ending column index (zero-based) for the merge.
     * @param cellValue The value to set in the merged cell.
     * @return The cell that was merged and styled.
     */
    public static Cell mergeCells(Sheet sheet, int startRow, int endRow, int startCol, int endCol, String cellValue) {


        if (startRow == endRow && startCol == endCol) {

            return writeToCell(sheet, startRow, startCol, cellValue);

        }
        // Use writeToCell to handle row and cell creation and set the value
        Cell cell = writeToCell(sheet, startRow, startCol, cellValue);

        // Create and apply cell style to center the text
        Workbook wb = sheet.getWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Merge the cells
        CellRangeAddress region = new CellRangeAddress(startRow, endRow, startCol, endCol);
        sheet.addMergedRegion(region);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    /**
     * Creates a cell with specified alignment and value.
     *
     * @param sheet       The sheet where the cell will be created.
     * @param rowIndex    The row index (zero-based) where the cell will be created.
     * @param columnIndex The column index (zero-based) where the cell will be created.
     * @param halign      The horizontal alignment for the cell.
     * @param valign      The vertical alignment for the cell.
     * @param value       The value to set in the cell.
     */
    private static void createCellWithAlign(Sheet sheet, int rowIndex, int columnIndex, HorizontalAlignment halign, VerticalAlignment valign, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        Workbook wb = sheet.getWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
    }

    /**
     * Applies a hex color to the left border of a cell.
     *
     * @param cell     The cell to which the color will be applied.
     * @param hexColor The hex color code (e.g., "#FF5733").
     * @throws IllegalArgumentException if the hexColor is not in the format "#RRGGBB".
     */
    public static void applyLeftBorderColor(Cell cell, String hexColor) {
        if (hexColor != null && hexColor.matches("^#[0-9A-Fa-f]{6}$")) {
            // Convert hex to RGB
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
     * Auto-sizes all columns in the sheet.
     *
     * @param sheet The sheet where the columns will be auto-sized.
     */
    public static void autoSize(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        for (int colNum = 0; colNum < lastRowNum; colNum++)
            sheet.autoSizeColumn(colNum);
    }

    /**
     * Applies border styles to a range of cells with optional custom left border color.
     *
     * @param sheet    The sheet where the border styles will be applied.
     * @param startRow The starting row index (zero-based) for the border.
     * @param endRow   The ending row index (zero-based) for the border.
     * @param startCol The starting column index (zero-based) for the border.
     * @param endCol   The ending column index (zero-based) for the border.
     * @param hexColor The hex color code for the left border (e.g., "#FF5733").
     */
    public static void applyTableBorderStyleOnMerge(Sheet sheet, int startRow, int endRow, int startCol, int endCol, String hexColor) {
        Workbook workbook = sheet.getWorkbook();
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

        XSSFColor blackColor = new XSSFColor(java.awt.Color.BLACK);
        XSSFColor leftBorderColor = blackColor;
        BorderStyle leftBorderStyle = BorderStyle.THIN;

        if (hexColor != null && hexColor.matches("^#[0-9A-Fa-f]{6}$")) {
            // Convert hex to RGB
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

            leftBorderColor = new XSSFColor(new java.awt.Color(red, green, blue));
            leftBorderStyle = BorderStyle.THICK;
        }

        // Apply border style
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(blackColor);

        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(blackColor);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(blackColor);

        cellStyle.setBorderLeft(leftBorderStyle);
        cellStyle.setLeftBorderColor(leftBorderColor);

        // Center text alignment
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Apply the cell style to each cell in the range
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
     * Writes two strings to a cell with the first string in normal font and the second string in italic.
     *
     * @param sheet       The sheet where the cell is located.
     * @param normalText  The text to be written in normal font.
     * @param italicText  The text to be written in italic font.
     * @param columnIndex The column index of the cell.
     * @param rowIndex    The row index of the cell.
     */
    public static Cell writeToCellWithHalfItalic(Sheet sheet, int rowIndex, int columnIndex, String normalText, String italicText) {
        Workbook workbook = sheet.getWorkbook();
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }

        // Create fonts
        Font normalFont = workbook.createFont();
        normalFont.setFontHeightInPoints((short) 11);

        Font italicFont = workbook.createFont();
        italicFont.setFontHeightInPoints((short) 11);
        italicFont.setItalic(true);


        // Create RichTextString
        RichTextString richTextString = new XSSFRichTextString(normalText + italicText);
        richTextString.applyFont(0, normalText.length(), normalFont);
        richTextString.applyFont(normalText.length(), normalText.length() + italicText.length(), italicFont); // Apply italic font to the second part

        // Set the RichTextString to the cell
        cell.setCellValue(richTextString);
        return cell;
    }

    /**
     * Applies border styles to a single cell with optional custom left border color.
     *
     * @param cell     The cell to which the border styles will be applied.
     * @param hexColor The hex color code for the left border (e.g., "#FF5733").
     */
    public static void applyTableBorderStyle(Cell cell, String hexColor, boolean isToCenter) {
        Workbook workbook = cell.getSheet().getWorkbook();
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // Default colors and border styles
        XSSFColor blackColor = new XSSFColor(java.awt.Color.BLACK);
        XSSFColor leftBorderColor = blackColor;
        BorderStyle leftBorderStyle = BorderStyle.THIN;

        // If a valid hex color is provided, use it for the left border
        if (hexColor != null && hexColor.matches("^#[0-9A-Fa-f]{6}$")) {
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

            // Create a new XSSFColor object for the custom border color
            leftBorderColor = new XSSFColor(new java.awt.Color(red, green, blue));
            leftBorderStyle = BorderStyle.THICK;
        }

        // Apply border styles
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(blackColor);

        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(blackColor);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(blackColor);

        cellStyle.setBorderLeft(leftBorderStyle);
        cellStyle.setLeftBorderColor(leftBorderColor);

        // Center text alignment
        if (isToCenter) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }


        // Apply the style to the cell
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


    public static XSSFSheet createSheet(XSSFWorkbook workbook, String sheetName) {
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

