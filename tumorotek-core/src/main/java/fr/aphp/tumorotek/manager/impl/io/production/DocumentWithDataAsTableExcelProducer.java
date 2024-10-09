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

import fr.aphp.tumorotek.dto.DocumentProducerResult;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.io.document.DataAsTable;
import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsTable;
import fr.aphp.tumorotek.manager.io.document.LabelValue;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.utils.io.ExcelUtility;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public class DocumentWithDataAsTableExcelProducer implements DocumentProducer
{

   private static final Logger log = LoggerFactory.getLogger(DocumentWithDataAsTableExcelProducer.class);

   private static final int START_COLUMN = 0;

   @Override
   public DocumentProducerResult produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable) throws IOException{
      DocumentProducerResult result = new DocumentProducerResult();
      result.setFormat(ConfigManager.EXCEL_XLSX_FILETYPE);
      result.setContentType(ConfigManager.OFFICE_OPENXML_MIME_TYPE);


      // Use try-with-resources to ensure the resources are closed properly
      try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         SXSSFWorkbook workbook = new SXSSFWorkbook() ){  // Optimized for large data sets

         workbook.setCompressTempFiles(true);  // Optional: compress temp files for better performance

         for(DocumentWithDataAsTable document : listDocumentWithDataAsTable){
            String sheetName = document.getDocumentName();
            Sheet sheet = ExcelUtility.createSheet(workbook, sheetName);

            // Write document context
            writeDocumentContext(sheet, document.getContext().getListLabelValue());


            // Write document data
            writeDocumentData(sheet, document.getData());

            // Write footer
            ExcelUtility.addFooter(sheet, document.getFooter().getLeftData(), document.getFooter().getCenterData(),
               document.getFooter().getRightData());
         }

         // Write workbook directly to output stream
         workbook.write(outputStream);

         result.setOutputStream(outputStream);

      }  // Auto-close resources when done

      return result;
   }

   private void writeDocumentContext(Sheet sheet, List<LabelValue> labelValues){
      int rowIndex = 0;
      for(LabelValue labelValue : labelValues){
         ExcelUtility.writeToCellWithOptionalBold(sheet, rowIndex, START_COLUMN, labelValue.getLabel(),
            labelValue.isLabelInBold());
         ExcelUtility.writeToCellWithOptionalBold(sheet, rowIndex, START_COLUMN + 1, labelValue.getValue(),
            labelValue.isValueInBold());
         rowIndex++;
      }
      int emptyRow = rowIndex + 1;
      ExcelUtility.writeToCell(sheet, emptyRow, START_COLUMN, "");

   }

   private void writeDocumentData(Sheet sheet, DataAsTable data){
      int rowIndex = sheet.getLastRowNum(); // Start where last written ended
      for(CellRow cellRow : data.getListCellRow()){
         writeCellRow(sheet, rowIndex, cellRow);
         rowIndex++;

      }

   }

   private void writeCellRow(Sheet sheet, int rowIndex, CellRow cellrow){
      int colIndex = 0;
      for(DataCell dataCell : cellrow.getListDataCell()){
         Cell cell = ExcelUtility.getOrCreateCell(sheet, rowIndex, colIndex);

         CellContent content = dataCell.getCellContent();

         String textToWrite = buildText(content);



         // Handle colspan by skipping cells accordingly
         if(dataCell.getColspan() > 1){
            cell = ExcelUtility.mergeCells(sheet, rowIndex, rowIndex, colIndex, colIndex + dataCell.getColspan(), textToWrite);
         }

         // Apply border if required
         if(dataCell.isWithBorder()){
            ExcelUtility.applyTableBorderStyle(cell, dataCell.getHexaColorCodeForLeftBorder(), true);
         }

         // Write text with optional italic complement

         if(content.isComplementInItalic() && content.getComplement() != null){
            ExcelUtility.writeToCellWithHalfItalic(cell, content.getText(), content.getComplement());
         }else{
            cell.setCellValue(textToWrite);
            ExcelUtility.applyAlignment(cell, dataCell.getAlignmentType());
            colIndex++;
         }

      }
   }

   private String buildText(CellContent content) {
      StringBuilder sb = new StringBuilder(content.getText());

      // Handle complement text
      if (!content.isComplementOnAnotherLine()) {
         sb.append(" ").append(content.getComplement() != null ? content.getComplement() : "");
      }

      return sb.toString();
   }




}
