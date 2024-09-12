/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.impl.io.production;

import fr.aphp.tumorotek.dto.OutputStreamData;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.io.document.DocumentFooter;
import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsTable;
import fr.aphp.tumorotek.manager.io.document.LabelValue;
import fr.aphp.tumorotek.manager.io.document.StylingAttributes;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.utils.io.ExcelUtility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * La classe implémente l'interface {@link DocumentProducer}
 * et fournit la fonctionnalité pour produire des documents Excel à partir d'une liste d'objets {@link DocumentWithDataAsTable}.
 *
 * <p>Le principal objectif de cette classe est de faciliter la génération de documents Excel à partir des données encapsulées
 * dans les objets {@link DocumentWithDataAsTable}. Elle est conçue pour être utilisée dans des scénarios où les données
 * doivent être exportées ou transformées en format de feuille de calcul.</p>
 *
 * <p><b>Exemple d'utilisation :</b></p>
 * <pre>{@code
 * List<DocumentWithDataAsTable> data = ...; // Préparez vos données
 * OutputStreamData output = new OutputStreamData();
 * output.setFileName("toto");
 * DocumentWithDataAsTableExcelProducer producer = new DocumentWithDataAsTableExcelProducer();
 * producer.produce(data, output);
 * }</pre>
 */

public class DocumentWithDataAsTableExcelProducer implements DocumentProducer {

    private static final int START_ROW = 0;
    private static final int START_COLUMN = 0;


    /**
     * Produit des documents au format Excel et les écrit dans un flux de sortie.
     *
     * @param listDocumentWithDataAsTable Liste des documents à produire.
     * @param outputStreamData            Données du flux de sortie où les documents seront écrits.
     */
    @Override
    public void produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable, OutputStreamData outputStreamData) {

        setupOutputStreamData(outputStreamData);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            for (DocumentWithDataAsTable document : listDocumentWithDataAsTable) {
                XSSFSheet sheet = ExcelUtility.createSheet(workbook, document.getDocumentName());
                int rowNum = START_ROW;

                // Écrire le context

                for (LabelValue labelValue : document.getContext().getListLabelValue()) {
                    writeLabelValue(sheet, rowNum, START_COLUMN, labelValue);
                    rowNum++;

                }

                //    Ecrire le data: use the method
                // get create DocumenyWithDataAsArry and call getData
//                it will give you a list of Cell rows (inside)
//                loop the list of cell rows and for eac cell row write the cell row. how?
//                write a method the gets CellRow loop over it and for each datacell call writeDataCellWithStyle()

                // Écrire le pied de page
                DocumentFooter documentFooter = document.getFooter();
                ExcelUtility.addFooter(sheet, documentFooter.getLeftData(), documentFooter.getCenterData(), documentFooter.getRightData());
            }

            workbook.write(byteArrayOutputStream);

            outputStreamData.setOutputStream(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du document Excel", e);

        }
    }

    public static void writeLabelValue(Sheet sheet, int startRow, int startColumn, LabelValue labelValue) {
        String key = labelValue.getLabel();
        String value = labelValue.getValue();
        ExcelUtility.writeToCellWithBold(sheet, startRow, startColumn, key, labelValue.isLabelInBold());
        ExcelUtility.writeToCellWithBold(sheet, startRow, startColumn, value, labelValue.isValueInBold());

    }

    private void setupOutputStreamData(OutputStreamData outputStreamData) {
        outputStreamData.setContentType(ConfigManager.OFFICE_OPENXML_MIME_TYPE);
        outputStreamData.setFormat(ConfigManager.EXCEL_XLSX_FILETYPE);

        // Other setup tasks related to workbook creation
    }


    private void writeDataCellWithStyle(XSSFSheet sheet, int rowNum, int colNum, DataCell dataCell) {

        CellContent cellContent = dataCell.getCellContent();
        StylingAttributes stylingAttributes = dataCell.getStylingAttributes();

        String text = cellContent.getText();
        String complement = cellContent.getComplement();
        String stringToWrite = text + " " + complement;
        Cell cell = ExcelUtility.writeToCell(sheet, rowNum, colNum, stringToWrite);

        applyStyles(sheet, rowNum, colNum, stylingAttributes, text, complement, stringToWrite, cell);
    }

    private static void applyStyles(XSSFSheet sheet, int rowNum, int colNum, StylingAttributes stylingAttributes, String text, String complement, String stringToWrite, Cell cell) {
        // Apply styling attributes
        if (stylingAttributes != null) {
            // Set border left color
            if (stylingAttributes.getBorderLeftColor() != null) {
                ExcelUtility.applyLeftBorderColor(cell, stylingAttributes.getBorderLeftColor());
            }
            // Apply border if required
            if (stylingAttributes.isWithBorder()) {
                // Code to apply border on cell
                ExcelUtility.applyTableBorderStyle(cell, null, false);
                // Example: cellStyle.setBorderBottom(BorderStyle.THIN);
            }
            // Set alignment type
            if (stylingAttributes.getAlignmentType() != null) {
                ExcelUtility.applyAlignment(cell, stylingAttributes.getAlignmentType());

            }
            if (stylingAttributes.getColspan() > 1) {
                int colSpan = stylingAttributes.getColspan();
                // Code to set colspan on cell
                ExcelUtility.mergeCells(sheet, rowNum, rowNum, colNum, colNum + colSpan, stringToWrite);
                // Example: sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, colNum, colNum + stylingAttributes.getColspan() - 1));
            }

            // Apply italic formatting if needed
            if (stylingAttributes.isFirstTextInItalic()) {
                // Code to apply italic formatting on cell content
                ExcelUtility.writeToCellWithHalfItalic(sheet, rowNum, colNum, text, complement);
                // Example: fontStyle.setItalic(true);
            }


        }

    }

}
