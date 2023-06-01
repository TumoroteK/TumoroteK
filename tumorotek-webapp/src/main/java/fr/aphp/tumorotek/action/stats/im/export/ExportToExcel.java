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
package fr.aphp.tumorotek.action.stats.im.export;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.SModeleIndicateur;
import fr.aphp.tumorotek.webapp.general.ResultSetToExcel;

public class ExportToExcel extends ResultSetToExcel
{

   //TODO Mega Refactoring Necessaire

   protected static Logger log = LoggerFactory.getLogger(ExportToExcel.class);

   private final Map<Indicateur, ArrayList<ValueToExport>> dataMap;

   private List<GroupExcel> groupList;

   private final SModele modele;

   private final List<String> years;

   //TODO Jamais utilisé ?
   private final String intervalType;

   private final List<String> subValues;

   private final Map<String, ArrayList<String>> mapPeriodByYear;

   private final List<Sheet> sheetList;

   private int lastRow;

   private int firstRow;

   private int currentRow;

   public ExportToExcel(final SModele modele, final List<String> years, final String intervalType,
      final Map<String, ArrayList<String>> mapPeriodByYear, final Map<Indicateur, ArrayList<ValueToExport>> dataMap,
      final List<String> subValues, final String sheetName){

      super(sheetName);

      sheetList = new ArrayList<>();
      this.dataMap = dataMap;
      this.modele = modele;
      this.years = years;
      this.intervalType = intervalType;
      this.mapPeriodByYear = mapPeriodByYear;
      this.subValues = subValues;

      if(modele.getSubdivision() != null){
         groupList = new ArrayList<>();
      }

      // Crée les cellules headers
      initHeaders();
      // Crée les cellules qui contiennent les valeurs numériques
      createCell();
   }

   private void initHeaders(){

      final XSSFCellStyle cs = (XSSFCellStyle) workbook.createCellStyle();
      cs.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
      cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cs.setBorderBottom(BorderStyle.THIN);
      cs.setBorderTop(BorderStyle.THIN);
      cs.setAlignment(HorizontalAlignment.JUSTIFY);
      cs.setVerticalAlignment(VerticalAlignment.CENTER);
      final Font f = workbook.createFont();
      f.setBold(true);
      f.setFontHeightInPoints((short) 8);
      cs.setFont(f);
      currentRow = 0;
      int i = 0;
      for(final String year : years){
         for(final String period : mapPeriodByYear.get(year)){
            sheet = workbook.createSheet(year + (period != "" ? "-" + period : ""));
            final Row rowStmt = sheet.createRow(0);
            rowStmt.setHeightInPoints(50);
            writeCell(rowStmt, i, "collection ou projet (type de prélèvement)", null, boldFont, cs);
            i++;
            if(modele.getSubdivision() != null){
               writeCell(rowStmt, i, "Subdivisions", null, boldFont, cs);
               i++;
            }

            for(final SModeleIndicateur s : modele.getSModeleIndicateurs()){
               writeCell(rowStmt, i, s.getIndicateur().getNom(), null, boldFont, cs);
               i++;
            }
            i = 0;
            sheet.createFreezePane(0, 1, 0, 1);
            sheetList.add(sheet);
         }
      }
   }

   private void createCell(){
      final XSSFCellStyle cs = (XSSFCellStyle) workbook.createCellStyle();
      cs.setFillBackgroundColor(new XSSFColor(Color.getHSBColor(194, 16, 100)));
      cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      final Font f = workbook.createFont();
      f.setBold(true);
      f.setFontHeightInPoints((short) 12);
      cs.setFont(f);

      Row rowValue;
      Row collectionRow;
      int rowCountCollection;
      final int currentCol = 1;
      String period = "";
      String year = "";

      for(final Sheet sh : sheetList){
         //?probleme avec ligne du dessous?
         final String tmp[] = sh.getSheetName().split("-");
         //ne passe pas là
         if(tmp != null && tmp.length == 2){
            //pas d'affectation à year, pourquoi? (voir plus haut)
            year = tmp[0];
            period = tmp[1];
         }else{
            year = sh.getSheetName();
         }
         currentRow = 4;
         rowValue = sh.createRow(currentRow);
         rowCountCollection = 3;
         for(final Banque collection : ManagerLocator.getSModeleManager().getBanquesManager(modele)){
            firstRow = currentRow;
            collectionRow = sh.createRow(rowCountCollection);
            // + "/ row collection :" + rowCountCollection);
            writeCell(collectionRow, 0, collection.getNom(), null, boldFont, null);

            if(modele.getSubdivision() != null){
               writeSubValues(sh, collection, rowValue, currentCol, year, period);
            }else{
               writeValues(collection, collectionRow, currentCol, "", year, period);
            }

            rowCountCollection = currentRow;
            collectionRow = sh.createRow(rowCountCollection);
            lastRow = currentRow - 1;
            rowValue = sh.createRow(1 + currentRow++);
            if(modele.getSubdivision() != null){
               groupList.add(new GroupExcel(firstRow, lastRow));
            }
         }
         if(modele.getSubdivision() != null){
            groupRow(sh);
            groupList.clear();
         }
      }
   }

   /**
    * TODO A quoi sert cela ?
    * @param collection
    * @param rowValue
    * @param currentCol
    * @param subValue
    * @param year
    * @param period
    */
   private void writeValues(final Banque collection, final Row rowValue, int currentCol, final String subValue, final String year,
      final String period){
      for(final SModeleIndicateur s : modele.getSModeleIndicateurs()){

         /*	if (intervalType.contentEquals("year")) {
         		createDataSheetByYear(collection, rowValue, currentCol,
         				subValue, s, year);
         	} else if (intervalType.contentEquals("month")) {
         		createDataSheetByMonth(collection, rowValue, currentCol,
         				period, subValue, s, year);
         	}
         	else if (intervalType.contentEquals("week")) {
         		createDataSheetByWeek(collection, rowValue, currentCol,
         				period, subValue, s, year);
         	} else if (intervalType.contentEquals("trimestre")) {
         		createDataSheetByTrimester(collection, rowValue, currentCol,
         				period, subValue, s, year);
         	} else if (intervalType.contentEquals("semestre")) {
         		createDataSheetBySemester(collection, rowValue, currentCol,
         				period, subValue, s, year);
         	}*/
         currentCol++;
      }
   }

   private void writeSubValues(final Sheet sh, final Banque collection, Row rowValue, int currentCol, final String year,
      final String period){
      //total subvalue
      //		writeCell(rowValue, 1, "TOTAL COLLECTION", null, boldFont);
      //		currentRow++;
      for(final String subValue : subValues){
         writeCell(rowValue, 1, subValue, null, boldFont, null);
         writeValues(collection, rowValue, currentCol, subValue, year, period);
         currentCol = 2;
         rowValue = sh.createRow(1 + currentRow++);
      }
   }

   /*
   	private void createDataSheetByYear(Banque collection, Row row,
   			int currentCol, String subValue, Statement s, String year) {

   		for (ValueToExport v : dataMap.get(s.getCallingProcedure())) {

   			if (v.getUnitTemp().contentEquals(year)
   					&& v.getCollection() == collection.getBanqueId()
   					&& v.getSubValue().contentEquals(subValue)) {
   				 + " / col : " + currentCol);
   				writeCell(row, currentCol,
   						String.valueOf(v.getValue()),
   						null, boldFont, null);
   			}
   		}
   		currentCol++;
   	}

   	private void createDataSheetBySemester(Banque collection, Row row,
   			int currentCol, String period, String subValue, Statement s,
   			String year) {

   		for (ValueToExport v : dataMap.get(s.getCallingProcedure())) {
   			if (v.getYear().contentEquals(year)
   					&& v.getSemestre().contentEquals(period)
   					&& v.getCollection() == collection.getBanqueId()
   					&& v.getSubValue().contentEquals(subValue)) {
   				// + " / col : " + currentCol +" / " + year + " / " + period);


   				writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
   						boldFont, null);
   			}
   		}
   	}
   	*/

   /**
    * TODO A quoi sert cela ?
    * @param collection
    * @param row
    * @param currentCol
    * @param period
    * @param subValue
    * @param s
    * @param year
    */
   //   private void createDataSheetByTrimester(final Banque collection, final Row row, final int currentCol, final String period,
   //      final int subValue, final Indicateur s, final String year){
   //
   //      for(final ValueToExport v : dataMap.get(s.getCallingProcedure())){
   //         // if (v.getUnitTemp().contentEquals(year+"-"+period)
   //         //		&& v.getCollection() == collection.getBanqueId()
   //         //		&& v.getSubValue() == subValue) {
   //
   //         //	writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
   //         //			boldFont, null);
   //         // }
   //      }
   //   }

   /**
    * TODO A quoi sert cela ?
    * @param collection
    * @param row
    * @param currentCol
    * @param period
    * @param subValue
    * @param s
    * @param year
    */
   //   private void createDataSheetByMonth(final Banque collection, final Row row, final int currentCol, final String period,
   //      final int subValue, final Indicateur s, final String year){
   //
   //      switch(period){
   //         case "Jan":
   //            break;
   //         case "FeB":
   //            break;
   //         case "Mar":
   //            break;
   //         case "Apr":
   //            break;
   //         case "May":
   //            break;
   //         case "Jun":
   //            break;
   //         case "Jul":
   //            break;
   //         case "Aug":
   //            break;
   //         case "Sep":
   //            break;
   //         case "Oct":
   //            break;
   //         case "Nov":
   //            break;
   //         case "Dec":
   //         default:
   //      }
   //      //
   //
   //      for(final ValueToExport v : dataMap.get(s.getCallingProcedure())){
   //         // if (v.getUnitTemp().contentEquals(year+"-"+periodTemp)
   //         //		//&& v.getUnitTemp().contains(period)
   //         //		&& v.getCollection() == collection.getBanqueId()
   //         //		&& v.getSubValue() == subValue) {
   //
   //         //	writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
   //         //			boldFont, null);
   //         // }
   //      }
   //   }

   /**
    * TODO A quoi sert cela ?
    * @param collection
    * @param row
    * @param currentCol
    * @param period
    * @param subValue
    * @param s
    * @param year
    */
   //   private void createDataSheetByWeek(final Banque collection, final Row row, final int currentCol, final String period,
   //      final int subValue, final Indicateur s, final String year){
   //
   //      for(final ValueToExport v : dataMap.get(s.getCallingProcedure())){
   //         //			if (v.getUnitTemp().contentEquals(year+"-"+period.substring(1))
   //         //					&& v.getCollection() == collection.getBanqueId()
   //         //					&& v.getSubValue() == subValue) {
   //         //
   //         //				// + " / col : " + currentCol +" / " + year + " / " + period);
   //         //				writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
   //         //						boldFont, null);
   //         //			}
   //      }
   //   }

   /**
    *
    * @param row
    * @param col
    * @param value
    * @param object
    * @param boldFont
    * @param style
    */
   private void writeCell(final Row row, final int col, final String value, final Object object, final XSSFFont boldFont,
      final XSSFCellStyle style){
      writeCell(row, col, value, null, null, boldFont, style);

   }

   private void groupRow(final Sheet sh){
      for(final GroupExcel g : groupList){
         sh.groupRow(g.getFirstRow(), g.getLastRow());
      }
   }

   @Override
   public void generate() throws Exception{
      for(final Sheet sh : sheetList){
         try{
            sh.setHorizontallyCenter(true);
            sh.setVerticallyCenter(true);
         }catch(final Exception ex){
            System.out.println("Erreur generate");
            System.out.println(ex);
            log.error(ex.getMessage(), ex);
         }finally{
            // Autosize columns
            ((SXSSFSheet) sh).trackAllColumnsForAutoSizing();
            for(int i = 0; i < 30; i++){
               sh.autoSizeColumn((short) i);
            }
         }
      }
   }

   protected void writeCell(final Row row, final int col, final Object value, final FormatType formatType, final Short bgColor,
      final Font font){
      final Cell cell = row.createCell(col);
      if(value == null){
         return;
      }
      final XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

      if(font != null){
         style.setFont(font);
         font.setBold(true);
         font.setItalic(true);
         cell.setCellStyle(style);
      }

      final XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();
      final XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
      cellStyle.setDataFormat(format.getFormat("dd/MM/yy h:mm"));

      final CreationHelper createHelper = workbook.getCreationHelper();

      if(formatType != null){
         if(formatType.equals(FormatType.BOOL) && row.getRowNum() != 0){
            cell.setCellValue(stringToBool(value.toString()));
         }else if(formatType.equals(FormatType.DATE) && row.getRowNum() != 0){
            style.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm:ss"));
            cell.setCellStyle(style);
            cell.setCellValue((java.util.Date) value);
         }else if(formatType.equals(FormatType.DATE) && row.getRowNum() != 0){
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(value.toString());
         }else if(formatType.equals(FormatType.NUMERIC)){
            cell.setCellValue((int) value);
         }else{
            cell.setCellValue(value.toString());
         }
      }else{
         cell.setCellStyle(style);
         cell.setCellValue(value.toString());
      }

      if(bgColor != null){
         final XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();

         titleStyle.setAlignment(HorizontalAlignment.CENTER);
         titleStyle.setBorderBottom(BorderStyle.MEDIUM);
         titleStyle.setBorderLeft(BorderStyle.MEDIUM);
         titleStyle.setBorderRight(BorderStyle.MEDIUM);
         titleStyle.setBorderTop(BorderStyle.MEDIUM);
         cell.setCellStyle(titleStyle);
      }
   }

   protected void writeCell(final Row row, final int col, final Object value, final FormatType formatType, final Short bgColor,
      final XSSFFont font, final XSSFCellStyle style){
      final Cell cell = row.createCell(col);
      if(value == null){
         return;
      }

      if(font != null){
         style.setFont(font);
         font.setBold(true);
         font.setItalic(true);
         cell.setCellStyle(style);
      }

      cell.setCellStyle(style);
      cell.setCellValue(value.toString());

      if(bgColor != null){
         final XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();

         titleStyle.setAlignment(HorizontalAlignment.CENTER);
         titleStyle.setBorderBottom(BorderStyle.MEDIUM);
         titleStyle.setBorderLeft(BorderStyle.MEDIUM);
         titleStyle.setBorderRight(BorderStyle.MEDIUM);
         titleStyle.setBorderTop(BorderStyle.MEDIUM);
         cell.setCellStyle(titleStyle);
      }
   }

   class GroupExcel
   {
      private int firstRow = 0;

      private int lastRow = 0;

      public GroupExcel(final int a, final int b){
         this.setFirstRow(a);
         this.setLastRow(b);
      }

      public int getFirstRow(){
         return firstRow;
      }

      public void setFirstRow(final int firstRow){
         this.firstRow = firstRow;
      }

      public int getLastRow(){
         return lastRow;
      }

      public void setLastRow(final int lastRow){
         this.lastRow = lastRow;
      }
   }
}
