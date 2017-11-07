package fr.aphp.tumorotek.action.stats.im.export;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModeleIndicateur;
import fr.aphp.tumorotek.webapp.general.ResultSetToExcel;

public class ExportToExcel extends ResultSetToExcel {

	protected static Log log = LogFactory.getLog(ExportToExcel.class);

	private Map<Indicateur, ArrayList<ValueToExport>> dataMap;
	private List<GroupExcel> groupList;
	private SModele modele;
	private List<String> years;
	private String intervalType;
	private List<String> subValues;
	private Map<String, ArrayList<String>> mapPeriodByYear;
	private List<Sheet> sheetList;

	private int lastRow;
	private int firstRow;

	private int currentRow;

	public ExportToExcel(SModele modele, List<String> years,
			String intervalType,
			Map<String, ArrayList<String>> mapPeriodByYear,
			Map<Indicateur, ArrayList<ValueToExport>> dataMap,
			List<String> subValues, String sheetName) {
		
		super(sheetName);

		sheetList = new ArrayList<Sheet>();
		this.dataMap = dataMap;
		this.modele = modele;
		this.years = years;
		this.intervalType = intervalType;
		this.mapPeriodByYear = mapPeriodByYear;
		this.subValues = subValues;

		if (modele.getSubdivision() != null) {
			groupList = new ArrayList<GroupExcel>();
		}
		
		// Crée les cellules headers
		initHeaders();
		// Crée les cellules qui contiennent les valeurs numériques
		createCell();
	}

	private void initHeaders() {

		XSSFCellStyle cs = (XSSFCellStyle) workbook.createCellStyle();
		cs.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
		cs.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cs.setBorderBottom((short) 1);
		cs.setBorderTop((short) 1);
		cs.setAlignment(CellStyle.ALIGN_JUSTIFY);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font f = workbook.createFont();
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		f.setFontHeightInPoints((short) 8);
		cs.setFont(f);
		currentRow = 0;
		int i = 0;
		for (String year : years) {
			for (String period : mapPeriodByYear.get(year)) {
				sheet = (SXSSFSheet) workbook.createSheet(year
						+ (period != "" ? "-" + period : ""));
				Row rowStmt = sheet.createRow(0);
				rowStmt.setHeightInPoints(50);
				writeCell(rowStmt, i,
						"collection ou projet (type de prélèvement)", null,
						(XSSFFont) boldFont, cs);
				i++;
				if (modele.getSubdivision() != null) {
					writeCell(rowStmt, i, "Subdivisions", null, (XSSFFont) boldFont, cs);
					i++;
				}

				for (SModeleIndicateur s : modele.getSModeleIndicateurs()) {
					writeCell(rowStmt, i, s.getIndicateur().getNom(), null, (XSSFFont) boldFont, cs);
					i++;
				}
				i = 0;
				sheet.createFreezePane(0, 1, 0, 1);
				sheetList.add(sheet);
			}
		}
	}

	private void createCell() {
		XSSFCellStyle cs = (XSSFCellStyle) workbook.createCellStyle();
		cs.setFillBackgroundColor(new XSSFColor(Color.getHSBColor(194, 16, 100)));
		cs.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		Font f = workbook.createFont();
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		f.setFontHeightInPoints((short) 12);
		cs.setFont(f);

		Row rowValue;
		Row collectionRow;
		int rowCountCollection;
		int currentCol = 1;
		String period = "";
		String year = "";

		for (Sheet sh : sheetList) {
			//?probleme avec ligne du dessous?
			String tmp[] = sh.getSheetName().split("-");
			//System.out.println("test");
			//ne passe pas là
			if (tmp != null && tmp.length == 2) {
			//pas d'affectation à year, pourquoi? (voir plus haut)
				year = tmp[0];
				period = tmp[1];
			} else {
				year = sh.getSheetName();
			}
			currentRow = 4;
			rowValue = sh.createRow(currentRow);
			rowCountCollection = 3;
			for (Banque collection : ManagerLocator.getSModeleManager()
					.getBanquesManager(modele)) {
				firstRow = currentRow;
				collectionRow = sh.createRow(rowCountCollection);
				// System.out.println("Collection > " + collection
				// + "/ row collection :" + rowCountCollection);
				writeCell(collectionRow, 0, collection.getNom(), null, boldFont, null);

				if (modele.getSubdivision() != null) {
					writeSubValues(sh, collection, rowValue, currentCol, year,
							period);
				} else {
					writeValues(collection, collectionRow, currentCol, "",
							year, period);
				}

				rowCountCollection = currentRow;
				collectionRow = sh.createRow(rowCountCollection);
				lastRow = currentRow - 1;
				rowValue = sh.createRow(1 + currentRow++);
				if (modele.getSubdivision() != null) {
					groupList.add(new GroupExcel(firstRow, lastRow));
				}
			}
			if (modele.getSubdivision() != null) {
				groupRow(sh);
				groupList.clear();
			}
		}
	}

	private void writeValues(Banque collection, Row rowValue, int currentCol,
			String subValue, String year, String period) {
		for (SModeleIndicateur s : modele.getSModeleIndicateurs()) {

		/*	if (intervalType.contentEquals("year")) {
				createDataSheetByYear(collection, rowValue, currentCol,
						subValue, s, year);
			} else if (intervalType.contentEquals("month")) {
				createDataSheetByMonth(collection, rowValue, currentCol,
						period, subValue, s, year);
			}
			// TODO
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

	private void writeSubValues(Sheet sh, Banque collection, Row rowValue,
			int currentCol, String year, String period) {
		//total subvalue
//		writeCell(rowValue, 1, "TOTAL COLLECTION", null, boldFont);
//		currentRow++;
		for (String subValue : subValues) {
			 System.out.println("subvalues " + subValue
			 + " / row Subvalue :" + currentRow + " / "
			 + rowValue.getRowNum());
			writeCell(rowValue, 1, subValue, null, boldFont, null);
			writeValues(collection, rowValue, currentCol, subValue, year,
					period);
			currentCol = 2;
			rowValue = sh.createRow(1 + currentRow++);
			 System.err.println("1\\ " + rowValue.getRowNum() + " / "
			 + currentRow);
		}
	}
/*
	private void createDataSheetByYear(Banque collection, Row row,
			int currentCol, String subValue, Statement s, String year) {

		for (ValueToExport v : dataMap.get(s.getCallingProcedure())) {

			if (v.getUnitTemp().contentEquals(year)
					&& v.getCollection() == collection.getBanqueId()
					&& v.getSubValue().contentEquals(subValue)) {
				System.err.println("value rowCount : " + currentRow
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
				// System.err.println("value rowCount : " + currentRow
				// + " / col : " + currentCol +" / " + year + " / " + period);
				
				
				writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
						boldFont, null);
			}
		}
	}
	*/
	private void createDataSheetByTrimester(Banque collection, Row row,
			int currentCol, String period, int subValue, Indicateur s,
			String year) {

		for (ValueToExport v : dataMap.get(s.getCallingProcedure())) {
			// if (v.getUnitTemp().contentEquals(year+"-"+period)
			//		&& v.getCollection() == collection.getBanqueId()
			//		&& v.getSubValue() == subValue) {

			//	writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
			//			boldFont, null);
			// }
		}
	}
	
	private void createDataSheetByMonth(Banque collection, Row row,
			int currentCol, String period, int subValue, Indicateur s,
			String year) {
		
		//pour test a enlever!!!!!
		String periodTemp;
		switch (period) {
		case "Jan" : periodTemp = "01";
		break;
		case "FeB" : periodTemp = "02";
		break;
		case "Mar" : periodTemp = "03";
		break;
		case "Apr" : periodTemp = "04";
		break;
		case "May" : periodTemp = "05";
		break;
		case "Jun" : periodTemp = "06";
		break;
		case "Jul" : periodTemp = "07";
		break;
		case "Aug" : periodTemp = "08";
		break;
		case "Sep" : periodTemp = "09";
		break;
		case "Oct" : periodTemp = "10";
		break;
		case "Nov" : periodTemp = "11";
		break;
		case "Dec" : periodTemp = "12";
		default : periodTemp = "00";
		}
		//
		
		for (ValueToExport v : dataMap.get(s.getCallingProcedure())) {
			// if (v.getUnitTemp().contentEquals(year+"-"+periodTemp)
			//		//&& v.getUnitTemp().contains(period)
			//		&& v.getCollection() == collection.getBanqueId()
			//		&& v.getSubValue() == subValue) {	
				
			//	writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
			//			boldFont, null);
			// }
		}
	}
	
	private void createDataSheetByWeek(Banque collection, Row row,
			int currentCol, String period, int subValue, Indicateur s,
			String year) {
		System.out.println("Passe par ici (week) 2 ????");
				
		for (ValueToExport v : dataMap.get(s.getCallingProcedure())) {
//			if (v.getUnitTemp().contentEquals(year+"-"+period.substring(1))
//					&& v.getCollection() == collection.getBanqueId()
//					&& v.getSubValue() == subValue) {
//
//				// System.err.println("value rowCount : " + currentRow
//				// + " / col : " + currentCol +" / " + year + " / " + period);
//				writeCell(row, currentCol, String.valueOf(v.getValue()), FormatType.NUMERIC,
//						boldFont, null);
//			}
		}
	}
	
	

	private void writeCell(Row row, int col, String value, Object object,
			XSSFFont boldFont, XSSFCellStyle style) {
		writeCell(row, col, value, null, null, boldFont, style);

	}

	private void groupRow(Sheet sh) {
		for (GroupExcel g : groupList) {
//			System.out.println(g.getFirstRow() + " / " + g.getLastRow());
//			System.out.println("----------");
			sh.groupRow(g.getFirstRow(), g.getLastRow());
		}
	}

	public void generate() throws Exception {
		for (Sheet sh : sheetList) {
			try {
				sh.setHorizontallyCenter(true);
				sh.setVerticallyCenter(true);
			} catch (Exception ex) {
				System.out.println("Erreur generate");
				System.out.println(ex);
				log.error(ex);
			} finally {
				// Autosize columns
				for (int i = 0; i < 30; i++) {
					sh.autoSizeColumn((short) i);
				}
			}
		}
	}

	protected void writeCell(Row row, int col, Object value,
			FormatType formatType, Short bgColor, Font font) {
		Cell cell = row.createCell(col);
		if (value == null) {
			return;
		}
		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

		if (font != null) {
			System.out.println("style font");
			style.setFont(font);
			font.setBoldweight((short) 1);
			font.setItalic(true);
			cell.setCellStyle(style);
		}

		XSSFDataFormat format = (XSSFDataFormat) workbook.createDataFormat();
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		cellStyle.setDataFormat(format.getFormat("dd/MM/yy h:mm"));

		CreationHelper createHelper = workbook.getCreationHelper();

		if (formatType != null) {
			if (formatType.equals(FormatType.BOOL) && row.getRowNum() != 0) {
				cell.setCellValue(stringToBool(value.toString()));
			} else if (formatType.equals(FormatType.DATE)
					&& row.getRowNum() != 0) {
				style.setDataFormat(createHelper.createDataFormat().getFormat(
						"dd/MM/yyyy hh:mm:ss"));
				cell.setCellStyle(style);
				cell.setCellValue((java.util.Date) value);
			} else if (formatType.equals(FormatType.DATE)
					&& row.getRowNum() != 0) {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(value.toString());
			} else if (formatType.equals(FormatType.NUMERIC)) {
				cell.setCellValue((int) value);
			} else {
				cell.setCellValue(value.toString());
			}
		} else {
			cell.setCellStyle(style);
			cell.setCellValue(value.toString());
		}

		if (bgColor != null) {
			XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
			titleStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
			titleStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			titleStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
			cell.setCellStyle(titleStyle);
		}
	}

	protected void writeCell(Row row, int col, Object value,
			FormatType formatType, Short bgColor, XSSFFont font,
			XSSFCellStyle style) {
		Cell cell = row.createCell(col);
		if (value == null) {
			return;
		}

		if (font != null) {
			System.out.println("style font");
			style.setFont(font);
			font.setBold(true);
			font.setItalic(true);
			cell.setCellStyle(style);
		}

		cell.setCellStyle(style);
		cell.setCellValue(value.toString());

		if (bgColor != null) {
			XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
			titleStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
			titleStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			titleStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
			cell.setCellStyle(titleStyle);
		}
	}

	class GroupExcel {
		private int firstRow = 0;
		private int lastRow = 0;

		public GroupExcel(int a, int b) {
			this.setFirstRow(a);
			this.setLastRow(b);
		}

		public int getFirstRow() {
			return firstRow;
		}

		public void setFirstRow(int firstRow) {
			this.firstRow = firstRow;
		}

		public int getLastRow() {
			return lastRow;
		}

		public void setLastRow(int lastRow) {
			this.lastRow = lastRow;
		}
	}
}
