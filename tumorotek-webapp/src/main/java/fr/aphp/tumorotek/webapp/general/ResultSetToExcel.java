/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
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
 *
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
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.webapp.general;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.utilisateur.ProfilExport;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.webapp.general.export.Export;

/**
 * Formate les données issues de la BDD sous forme de ResultSet afin de créer un
 * Fichier Excel des Exports exploitable par les Users
 *
 * @author jhusson
 * @version 2.2.3-rc1
 */
public class ResultSetToExcel
{

	protected static Log log = LogFactory.getLog(ResultSetToExcel.class);

	private static final String DETAILS_LABO_INTER = "details labo inter";
	private static final String LABO_INTER_SHEET = "Labo_inter_List";
	private static final String DETAILS_RETOUR = "details evts stock";
	private static final String RETOUR_ECHAN_SHEET = "Evts_echan_List";
	private static final String RETOUR_DERIVE_SHEET = "Evts_derive_List";

	// private static final int LIMIT_AUTOSIZE = 10000;
	public static final String DATE_FORMAT = "yyyyMMddHHmm";

	// ecart entre colonnes permettant de retrouver l'id du prel à partir de la colonne labo_inter
	private int index_prel_id = 26;
	// ecart entre colonnes permettant de retrouver l'id de l'echan/derive à partir de la colonne evts_stock
	private int index_echan_id = 25;
	private int index_derive_id = 28;

	protected SXSSFWorkbook workbook;
	protected SXSSFSheet sheet;
	protected XSSFFont boldFont;
	private ResultSet resultSet;
	private FormatType[] formatTypes;

	private Export export;
	private boolean hasPrelevement;
	
	// @since 2.2.3-genno anonyme -> 3 etats possibles (nominatif, anonyme, anonymestock)
	private ProfilExport profilExport;
	
	private ArrayList<Integer> anonymeColumn = new ArrayList<>();
	private final ArrayList<Integer> labo_interColumn = new ArrayList<>();
	private final ArrayList<Integer> multipleValuesColumn = new ArrayList<>();
	private ResultSet resultLaboInter;
	private ResultSet retourEchanRs;
	private int retourE_interColumn = -1;
	private ResultSet retourDeriveRs;
	private int retourD_interColumn = -1;

	private XSSFCellStyle dateStyle;
	private XSSFCellStyle linkStyle;
	private XSSFCellStyle boldStyle;
	private CreationHelper createHelper;

	private Export updateThread;

	private boolean cessionExport = false;

	public ResultSetToExcel(final SXSSFWorkbook wb, final Export export, final ResultSet resultSet, final ResultSet rs2,
			final ResultSet erRs, final ResultSet drRs, final ProfilExport pE, final boolean hasP, final String sheetName,
			final boolean isCession){
		workbook = wb;
		this.export = export;
		this.resultSet = resultSet;

		sheet = (SXSSFSheet) wb.createSheet(sheetName);
		boldFont = (XSSFFont) workbook.createFont();
		boldFont.setBold(true);
		// format = workbook.createDataFormat();
		this.profilExport = pE;
		hasPrelevement = hasP;
		resultLaboInter = rs2;
		retourEchanRs = erRs;
		retourDeriveRs = drRs;

		cessionExport = isCession;

		setsEcartColsFromContexte(export);

		sheet.trackAllColumnsForAutoSizing();

		boldStyle = (XSSFCellStyle) workbook.createCellStyle();
		boldStyle.setFont(boldFont);

		dateStyle = (XSSFCellStyle) workbook.createCellStyle();
		dateStyle.setDataFormat(((XSSFDataFormat) workbook.createDataFormat()).getFormat("dd/MM/yyyy hh:mm"));

		// cell style for hyperlinks
		// by default hyperlinks are blue and underlined
		createHelper = workbook.getCreationHelper();
		linkStyle = (XSSFCellStyle) workbook.createCellStyle();
		final Font hlink_font = workbook.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_font.setFontHeightInPoints((short) 12);
		linkStyle.setFont(hlink_font);

	}

	/**
	 * Défini les valeurs des variables définissant les écarts de colonnes 
	 * entre les ids de domaine et les hyperliens, en fonction du contexte 
	 * de la collection.
	 * @param export, car contient une ref vers le contexte
	 * @since 2.2.1
	 */
	private void setsEcartColsFromContexte(Export export) {
		if (export != null && export.getCtx() != null) {
			switch(export.getCtx()) {
			case SEROLOGIE:
				index_prel_id = 28;
				index_echan_id = 21;
				// index_derive_id = 28;
				break;
			default:
				index_prel_id = 26;
				index_echan_id = 25;
				index_derive_id = 28;
			}
		}
	}

	public ResultSetToExcel(final ResultSet resultSet, final String sheetName){
		workbook = new SXSSFWorkbook(100);
		this.resultSet = resultSet;
		sheet = (SXSSFSheet) workbook.createSheet(sheetName);
	}

	public ResultSetToExcel(final String sheetName){
		workbook = new SXSSFWorkbook(100);
		sheet = (SXSSFSheet) workbook.createSheet(sheetName);
	}

	public ResultSetToExcel(final Export export, final ResultSet resultSet, final ProfilExport pE){
		setExport(export);
		setResultSet(resultSet);
		setProfilExport(pE);
	}

	public void generate() throws Exception{

		int currentRow = 0;
		int numCols = 0;
		final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		Row row = sheet.createRow(currentRow);
		numCols = resultSetMetaData.getColumnCount();
		formatTypes = new FormatType[numCols];

		if(profilExport != null && !profilExport.equals(ProfilExport.NOMINATIF)){
			anonymeColumn.clear();
		}

		if(hasPrelevement){
			labo_interColumn.clear();
		}

		String entite = "Patient";
		boolean isAnno = false;

		for(int i = 0; i < numCols; i++){
			// title = alias ssi export catalogue
			String title =
					(export.getExportType() == 1) ? resultSetMetaData.getColumnName(i + 1) : resultSetMetaData.getColumnLabel(i + 1);
					if(export.getMapCorrespondanceAnnotationName() != null
							&& export.getMapCorrespondanceAnnotationName().containsKey(title)){
						title = export.getMapCorrespondanceAnnotationName().get(title);
						isAnno = true;
					}else{
						isAnno = false;
					}
					
					// Anonyme
					// @since 2.2.3-genno anonyme -> 3 etats possibles (nominatif, anonyme, anonymestock)
					if(profilExport != null && !profilExport.equals(ProfilExport.NOMINATIF)) {
						// ces traits seront toujours masqués
						if(title.equalsIgnoreCase("patient_nda")
								|| title.equalsIgnoreCase("patient_id") || title.equalsIgnoreCase("nip") || title.equalsIgnoreCase("nom_naissance")
								|| title.equalsIgnoreCase("nom") || title.equalsIgnoreCase("prenom") || title.equalsIgnoreCase("date_naissance")){
							anonymeColumn.add(i);
						} else if (title.equalsIgnoreCase("emplacement") && profilExport.equals(ProfilExport.ANONYME)) { // anonyme strict (sans accès stockage)
							anonymeColumn.add(i);
						}
					}
					// Data labo Inter
					if(hasPrelevement){
						if(title.equalsIgnoreCase("labo_inter")){
							labo_interColumn.add(i);
						}
					}

					// liste retour
					if(retourEchanRs != null){
						if(title.equalsIgnoreCase("evts_stock_e")){
							retourE_interColumn = i;
						}
					}

					if(retourDeriveRs != null){
						if(title.equalsIgnoreCase("evts_stock_d")){
							retourD_interColumn = i;
						}
					}

					// liste deroulante
					if(title.equalsIgnoreCase("echantillons") || title.equalsIgnoreCase("produits_derives")){
						if(multipleValuesColumn == null){
							multipleValuesColumn.clear();
						}
						multipleValuesColumn.add(i);
					}
					final Class<?> _class = Class.forName(resultSetMetaData.getColumnClassName(i + 1));
					final int precision = resultSetMetaData.getPrecision(i + 1);
					formatTypes[i] = getFormatType(_class, precision);

					if(title.equals("PATIENT_ID")){
						entite = "Patient";
					}else if(title.equals("MALADIE_ID")){
						entite = "Maladie";
					}else if(title.equals("PRELEVEMENT_ID")){
						entite = "Prelevement";
					}else if(title.equals("ECHANTILLON_ID")){
						entite = "Echantillon";
					}else if(title.equals("PROD_DERIVE_ID")){
						entite = "ProdDerive";
					}else if(title.equals("CESSION_ID")){
						entite = "Cession";
					}

					writeCell(row, i, isAnno ? title : labelPrintTitle(title, entite), formatTypes[i], boldStyle);
		}

		// BIOCAP export ligne champs TK
		if(export.getExportType() == ConfigManager.BIOCAP_EXPORT){
			writeTKcolNames();
			currentRow++;
		}

		currentRow++;
		// Write report rows
		while(resultSet.next()){
			row = sheet.createRow(currentRow++);
			for(int i = 0; i < numCols; i++){
				final Object value = resultSet.getObject(i + 1);
				writeCell(row, i, value, formatTypes[i]);
			}
			if(currentRow % 10 == 0){
				if(getUpdateThread() != null){
					if(!getUpdateThread().isInterrupted()){
						getUpdateThread().setExportDetails(null, currentRow, getUpdateThread().getTotal(), "progressbar.recherche.rows",
								null, null);
					}else{
						resultSet.close();
						throw new InterruptedException();
					}
				}
			}
		}
		resultSet.close();

		if(hasPrelevement && resultLaboInter != null){
			createLaboInterSheet();
		}

		if(retourEchanRs != null){
			createRetourSheet(3);
		}

		if(retourDeriveRs != null){
			createRetourSheet(8);
		}

		// Autosize columns
		// if (currentRow < LIMIT_AUTOSIZE) {
		sheet.trackAllColumnsForAutoSizing();
		for(int i = 0; i < numCols; i++){
			sheet.autoSizeColumn((short) i);
		}
		// }
	}

	private void writeTKcolNames(){
		final Row row = sheet.createRow(1);
		final String[] tkcols = new String[] {"PATIENT.PATIENT_ID", "PATIENT.NIP", "PATIENT.NOM", "PATIENT.PRENOM",
				"PATIENT.DATE_NAISSANCE", "PATIENT.SEXE", "PRELEVEMENT.STATUT JURIDIQUE", "PATIENT.ETAT",
				"PRELEVEMENT.ETABLISSEMENT PRELEVEUR", "ETABLISSEMENT.FINESS", "PRELEVEMENT.SERVICE PRELEVEUR", "PRELEVEMENT.CODE",
				"PRELEVEMENT.DATE PRELEVEMENT", "PRELEVEMENT.DATE PRELEVEMENT", "PRELEVEMENT.DATE ARRIVEE", "PRELEVEMENT.NATURE",
				"PRELEVEMENT.TYPE", "", "ECHANTILLON.CODES ORGANES", "ECHANTILLON.CODES LESIONNELS", "", "ECHANTILLON.CODES ORGANES",
				"ECHANTILLON.CODES LESIONNELS", "ANNOTATION_PREL.Taille tumeur : cT", "ANNOTATION_PREL.Envahissement ganglionnaire : CN",
				"ANNOTATION_PREL.Version pTNM", "ANNOTATION_PREL.Taille tumeur : pT", "ANNOTATION_PREL.Envahissement ganglionnaire : pN",
				"ANNOTATION_PREL.pM", "ANNOTATION_PREL.022 TYPE EVENT", "ECHANTILLON.CODE", "ECHANTILLON.TUMORAL ou ECHANTILLON.TYPE",
				"CONTENEUR.TEMPERATURE", "ECHANTILLON.TYPE", "ECHANTILLON.MODE PREPARATION", "ECHANTILLON.DELAI CONGELATION",
				"ECHANTILLON.DATE_STOCKAGE", "ECHANTILLON.DATE_STOCKAGE", "ANNOTATION_ECH.032 Controle tissu", "", "",
				"ANNOTATION_ECH.035 Pourcentage cellules tumorales", "PRODUIT_DERIVE.TYPE", "", "PRODUIT_DERIVE.TYPE", "",
				"PRODUIT_DERIVE.TYPE", "CONTENEUR.SERVICE.ETABLISSEMENT.NOM", "CONTENEUR.SERVICE.ETABLISSEMENT.FINESS", "", "", "", "",
				"", "ANNOTATION_ECH.053 ADN constitutionnel", "", "", "ANNOTATION_PREL.CR standardise interroegable",
				"ANNOTATION_PREL.Donnees cliniques", "ANNOTATION_PREL.Inclusion protocole thérapeutique",
				"ANNOTATION_PREL.Inclusion projet recherche", "ECHANTILLON.STATUT", "", "", "", "", "PRELEVEMENT.NUMERO_LABO"};
		for(int i = 0; i < tkcols.length; i++){
			writeCell(row, i, tkcols[i], FormatType.TEXT);
		}
	}

	protected FormatType getFormatType(final Class<?> _class, final int precision){
		if(_class == Boolean.class){
			return FormatType.BOOL;
		}else if(_class == Date.class || _class == Timestamp.class){
			return FormatType.DATE;
		}else if(_class == Integer.class || _class == Float.class || _class == Long.class || _class == BigDecimal.class
				|| Number.class.isAssignableFrom(_class)){
			if(precision == 1){ // Oracle boolean hack
				return FormatType.BOOL;
			}
			return FormatType.NUMERIC;
		}else{
			return FormatType.TEXT;
		}
	}

	protected void writeCell(final Row row, final int col, final Object value, final FormatType formatType){
		writeCell(row, col, value, formatType, null);
	}

	protected void writeCell(final Row row, final int col, final Object value, final FormatType formatType,
			final XSSFCellStyle st){
		writeCell(row, col, value, formatType, null, st);
	}

	protected void writeCell(final Row row, final int col, final Object value, final FormatType formatType, final Short bgColor,
			final XSSFCellStyle st){

		if(value != null){
			final Cell cell = row.createCell(col);
			// CellStyle style = workbook.createCellStyle();

			if(st != null){
				cell.setCellStyle(st);
			}

			if(profilExport != null && !profilExport.equals(ProfilExport.NOMINATIF) 
						&& this.anonymeColumn.contains(col) && row.getRowNum() != 0){
				cell.setCellType(CellType.STRING);
				cell.setCellValue("****");
			}else{
				if(formatType.equals(FormatType.BOOL) && row.getRowNum() != 0){
					cell.setCellType(CellType.BOOLEAN);
					if(value instanceof BigDecimal){

						if(((BigDecimal) value).compareTo(BigDecimal.ZERO) > 0){
							cell.setCellValue(ObjectTypesFormatters.booleanLitteralFormatter(true));
						}else{
							cell.setCellValue(ObjectTypesFormatters.booleanLitteralFormatter(false));
						}
					}else{
						cell.setCellValue(ObjectTypesFormatters.booleanLitteralFormatter((Boolean) value));
					}
				}else if(formatType.equals(FormatType.DATE) && row.getRowNum() != 0){
					cell.setCellStyle(dateStyle);
					cell.setCellValue((java.util.Date) value);
				}else if(formatType.equals(FormatType.NUMERIC) && row.getRowNum() != 0){
					cell.setCellType(CellType.NUMERIC);
					if(value instanceof Integer){
						cell.setCellValue((Integer) value);
					}else if(value instanceof Float){
						cell.setCellValue((Float) value);
					}else if(value instanceof Double){
						cell.setCellValue((Double) value);
					}else if(value instanceof BigDecimal){
						cell.setCellValue(((BigDecimal) value).doubleValue());
					}else{
						cell.setCellValue((Double) value);
						// cell.setCellType(Cell.CELL_TYPE_BLANK);
					}
				}else{
					cell.setCellType(CellType.STRING);
					cell.setCellValue(value.toString());
				}
			}

			if(hasPrelevement){
				if(labo_interColumn.contains(col) && row.getRowNum() != 0){
					if(!value.toString().contentEquals("0")){
						cell.setCellType(CellType.STRING);
						createHyperlink("id_", cell, col, row, DETAILS_LABO_INTER, index_prel_id);
					} // else {
					//	cell.setCellType(Cell.CELL_TYPE_BLANK);
					// cell.setCellValue("");
					//}
				}
			}
			if(col == retourE_interColumn && row.getRowNum() != 0){
				if(!value.toString().contentEquals("0")){
					cell.setCellType(CellType.STRING);
					createHyperlink("id_re_", cell, col, row, DETAILS_RETOUR,
							index_echan_id + (cessionExport && retourD_interColumn == -1 ? 2 : 0));
					// n'applique le +2 en cas de cessionExport que si c'est un export Echantillon
					// et donc que le +2 n'a pas déja été appliqué pour le dérivé.
				}
			}
			if(col == retourD_interColumn && row.getRowNum() != 0){
				if(!value.toString().contentEquals("0")){
					cell.setCellType(CellType.STRING);
					createHyperlink("id_rd_", cell, col, row, DETAILS_RETOUR, index_derive_id + (cessionExport ? 2 : 0));
				}
			}
			//		if (bgColor != null) {
			//			CellStyle titleStyle = workbook.createCellStyle();
			//
			//			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			//			titleStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
			//			titleStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
			//			titleStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			//			titleStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
			//			cell.setCellStyle(titleStyle);
			// HSSFCellUtil.setCellStyleProperty(cell, workbook,
			// FILL_FOREGROUND_COLOR, bgColor);
			// HSSFCellUtil.setCellStyleProperty(cell, workbook, FILL_PATTERN,
			// HSSFCellStyle.SOLID_FOREGROUND);
			//		}
		}
	}

	private void createLaboInterSheet() throws SQLException, ClassNotFoundException, InterruptedException{
		if(resultLaboInter.isBeforeFirst()){
			// create a target sheet and cell
			final SXSSFSheet sheet2 = workbook.createSheet(LABO_INTER_SHEET);
			int currentRow = 0;
			int numCols = 0;
			String title = "";
			final ResultSetMetaData resultSetMetaData = resultLaboInter.getMetaData();
			Row row = sheet2.createRow(currentRow);
			numCols = resultSetMetaData.getColumnCount();
			formatTypes = new FormatType[numCols];
			for(int i = 0; i < numCols; i++){
				title = resultSetMetaData.getColumnName(i + 1);
				final Class<?> _class = Class.forName(resultSetMetaData.getColumnClassName(i + 1));
				final int precision = resultSetMetaData.getPrecision(i + 1);
				formatTypes[i] = getFormatType(_class, precision);
				writeCell(row, i, labelPrintTitle(title, "LaboInter"), formatTypes[i], boldStyle);
			}
			currentRow++;
			// Write report rows
			final int[] cellRange = new int[2];
			cellRange[0] = currentRow + 1;
			Number prev_prelId = null;
			Number prelId = null;
			String reference;
			XSSFName namedCell;
			while(resultLaboInter.next()){
				row = sheet2.createRow(currentRow++);
				for(int i = 0; i < numCols; i++){
					final Object value = resultLaboInter.getObject(i + 1);
					if(i == 0){
						prelId = (Number) value;
					}
					writeCell(row, i, value, formatTypes[i]);
				}
				// ecris la reference sur le cells range du prelevement precedent
				if(prev_prelId != null && null != prelId && !prelId.equals(prev_prelId)){
					cellRange[1] = currentRow - 1;
					namedCell = (XSSFName) workbook.createName();
					namedCell.setNameName("id_" + prev_prelId);
					if(cellRange[1] > cellRange[0]){
						reference = LABO_INTER_SHEET + "!A" + cellRange[0] + ":L" + cellRange[1];
					}else{
						reference = LABO_INTER_SHEET + "!A" + cellRange[0] + ":L" + cellRange[0];
					}
					namedCell.setRefersToFormula(reference);
					// commence un autre prelevement
					cellRange[0] = currentRow;
				}
				prev_prelId = prelId;

				//			if (getUpdateThread() != null) {
				//				if (getUpdateThread().isInterrupted()) {
				//					throw new InterruptedException();
				//				}
				//			}
			}
			// ecris le dernier lien
			cellRange[1] = currentRow;
			namedCell = (XSSFName) workbook.createName();
			namedCell.setNameName("id_" + prelId);
			if(cellRange[1] > cellRange[0]){
				reference = LABO_INTER_SHEET + "!A" + cellRange[0] + ":L" + cellRange[1];
			}else{
				reference = LABO_INTER_SHEET + "!A" + cellRange[0] + ":L" + cellRange[0];
			}
			namedCell.setRefersToFormula(reference);
			resultLaboInter.close();
			// Autosize columns
			sheet2.trackAllColumnsForAutoSizing();
			for(int i = 0; i < numCols; i++){
				sheet2.autoSizeColumn(i);
			}
		}
	}

	private void createRetourSheet(final int entiteId) throws SQLException, ClassNotFoundException, InterruptedException{
		// create a target sheet and cell
		final String sheetName = entiteId == 3 ? RETOUR_ECHAN_SHEET : RETOUR_DERIVE_SHEET;
		final ResultSet rs = entiteId == 3 ? retourEchanRs : retourDeriveRs;
		final String suff = entiteId == 3 ? "id_re_" : "id_rd_";

		if(rs.isBeforeFirst()){
			final Sheet sheet2 = workbook.createSheet(sheetName);
			int currentRow = 0;
			int numCols = 0;
			String title = "";

			final ResultSetMetaData resultSetMetaData = rs.getMetaData();
			Row row = sheet2.createRow(currentRow);
			numCols = resultSetMetaData.getColumnCount();
			formatTypes = new FormatType[numCols];

			for(int i = 0; i < numCols; i++){
				title = resultSetMetaData.getColumnName(i + 1);
				final Class<?> _class = Class.forName(resultSetMetaData.getColumnClassName(i + 1));
				final int precision = resultSetMetaData.getPrecision(i + 1);
				formatTypes[i] = getFormatType(_class, precision);
				writeCell(row, i, labelPrintTitle(title, "Retour"), formatTypes[i], boldStyle);
			}

			currentRow++;
			// Write report rows
			final int[] cellRange = new int[2];
			cellRange[0] = currentRow + 1;
			Number prev_objId = null;
			Number objId = null;
			String reference;
			Name namedCell;
			while(rs.next()){
				row = sheet2.createRow(currentRow++);
				for(int i = 0; i < numCols; i++){
					final Object value = rs.getObject(i + 1);
					if(i == 0){
						objId = (Number) value;
					}
					writeCell(row, i, value, formatTypes[i]);
				}
				// ecris la reference sur le cells range de l'objet precedent
				if(prev_objId != null && null != objId && !objId.equals(prev_objId)){
					cellRange[1] = currentRow - 1;
					namedCell = workbook.createName();
					namedCell.setNameName(suff + prev_objId);
					if(cellRange[1] > cellRange[0]){
						reference = sheetName + "!A" + cellRange[0] + ":M" + cellRange[1];
					}else{
						reference = sheetName + "!A" + cellRange[0] + ":M" + cellRange[0];
					}
					namedCell.setRefersToFormula(reference);
					// commence un autre obj
					cellRange[0] = currentRow;
				}
				prev_objId = objId;

			}
			// ecris le dernier lien
			cellRange[1] = currentRow;
			namedCell = workbook.createName();
			namedCell.setNameName(suff + objId);
			if(cellRange[1] > cellRange[0]){
				reference = sheetName + "!A" + cellRange[0] + ":M" + cellRange[1];
			}else{
				reference = sheetName + "!A" + cellRange[0] + ":M" + cellRange[0];
			}
			namedCell.setRefersToFormula(reference);

			// Autosize columns
			((SXSSFSheet)sheet2).trackAllColumnsForAutoSizing();
			for(int i = 0; i < numCols; i++){
				sheet2.autoSizeColumn((short) i);
			}
		}
		rs.close();
	}

	private void createHyperlink(final String suff, final Cell cell, final int col, final Row row, final String text,
			final int idEcartCol){
		cell.setCellValue(text);
		final org.apache.poi.ss.usermodel.Hyperlink link2 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);

		link2.setAddress(suff + String.valueOf((int) (row.getCell(col - idEcartCol).getNumericCellValue())));
		cell.setHyperlink(link2);
	}

	// private void createValueBox(Row row, Row row2, int col, int col2) {
	// DVConstraint dvConstraint = DVConstraint
	// .createExplicitListConstraint(new String[] { "DA37B1.1",
	// "DA37B2.1", "DA37B3.1" });
	// CellRangeAddressList addressList = new CellRangeAddressList(
	// row.getRowNum(), row2.getRowNum(), col, col2);
	// DataValidation dataValidation = new HSSFDataValidation(addressList,
	// dvConstraint);
	// dataValidation.setSuppressDropDownArrow(true);
	// sheet.addValidationData(dataValidation);
	// }

	public enum FormatType
	{
		TEXT, BOOL, DATE, NUMERIC
	}

	protected static String stringToBool(final String s){
		if(s.equals("1")){
			return "OUI";
		}
		if(s.equals("0")){
			return "NON";
		}
		return s;
	}

	//	/**
	//	 * Gère le download d'un fichier d'export xls.
	//	 * 
	//	 * @param wb
	//	 *            workbook
	//	 * @param fileName
	//	 */
	//	public static void downloadExportFileXls(XSSFWorkbook wb, String fileName) {
	//		ByteArrayOutputStream out = null;
	//		try {
	//			out = new ByteArrayOutputStream();
	//			wb.write(out);
	//			if (out.size() > 0) {
	//				AMedia media = new AMedia(fileName, "xls",
	//						ConfigManager.OFFICE_EXCEL_MIME_TYPE, out.toByteArray());
	//				Filedownload.save(media);
	//			}
	//		} catch (FileNotFoundException e) {
	//			log.error(e);
	//		} catch (Exception e) {
	//			log.error(e);
	//		} finally {
	//			if (out != null) {
	//				try {
	//					out.close();
	//				} catch (IOException e) {
	//					out = null;
	//				}
	//			}
	//		}
	//	}

	public void setUpdateThread(final Export u){
		this.updateThread = u;
	}

	public Export getUpdateThread(){
		return updateThread;
	}

	public SXSSFWorkbook getWorkbook(){
		return workbook;
	}

	private String labelPrintTitle(final String title, final String entite){

		// skip project normalized exports
		if(export.getExportType() > 1){
			return title;
		}

		final StringBuffer label = new StringBuffer();
		if(!entite.equals("LaboInter")){
			label.append(title.substring(0, 1));
		}else{
			label.append(title.substring(0, 1).toLowerCase());
		}
		if(title.contains("_")){
			label.append(title.substring(1, title.indexOf("_")).toLowerCase());
			label.append(title.substring(title.indexOf("_") + 1, title.indexOf("_") + 2));
			final String endS = title.substring(title.indexOf("_") + 2);
			if(endS.contains("_")){
				label.append(endS.substring(0, endS.indexOf("_")).toLowerCase());
				label.append(endS.substring(endS.indexOf("_") + 1, endS.indexOf("_") + 2));
				label.append(endS.substring(endS.indexOf("_") + 2).toLowerCase());
			}else{
				label.append(endS.toLowerCase());
			}

		}else{
			label.append(title.substring(1).toLowerCase());
		}

		String out;
		if(!entite.equals("LaboInter")){
			out = Labels.getLabel("Champ." + entite + "." + label.toString());
		}else{
			out = Labels.getLabel("laboInter." + label.toString());
		}
		if(out != null){
			return out;
		}

		if(entite.equals("Patient")){
			if(title.equals("MEDECIN_PATIENT")){
				return Labels.getLabel("patient.medecins");
			}else if(title.equals("CODE_ORGANE")){
				return Labels.getLabel("Champ.Echantillon.Organe");
			}else if(title.equals("NOMBRE_PRELEVEMENT")){
				return Labels.getLabel("patient.nbPrelevements");
			}
		}else if(entite.equals("Prelevement")){
			if(title.equals("CODE_ORGANE")){
				return Labels.getLabel("Champ.Echantillon.Organe");
			}else if(title.equals("PROTOCOLES")){
				return Labels.getLabel("Champ.Prelevement.SEROLOGIE.Protocoles");
			}else if(title.equals("COMPLEMENT_DIAG")){
				return Labels.getLabel("Champ.Prelevement.SEROLOGIE.Libelle");
			}else if(title.equals("ETABLISSEMENT")){
				return Labels.getLabel("prelevement.etablissement");
			}else if(title.equals("RAISON_NC_TRAITEMENT")){
				return Labels.getLabel("thesaurus.liste.nonConformite.arrivee");
			}else if(title.equals("LABO_INTER")){
				return Labels.getLabel("fichePrelevement.laboInters");
			}else if(title.equals("AGE_PREL")){
				return Labels.getLabel("prelevement.age");
			}else if(title.equals("ECHAN_TOTAL")){
				return Labels.getLabel("prelevement.nb.total.echantillons");
			}else if(title.equals("ECHAN_RESTANT")){
				return Labels.getLabel("prelevement.nb.echantillons.restants");
			}else if(title.equals("ECHAN_STOCKE")){
				return Labels.getLabel("prelevement.nbEchantillons") + " " + Labels.getLabel("prelevement.echantillons.stockes");
			}else if(title.equals("NOMBRE_DERIVES")){
				return Labels.getLabel("prelevement.nbProdDerives");
			}
		}else if(entite.equals("Maladie")){
			if(title.equals("MEDECIN_MALADIE")){
				return Labels.getLabel("patient.medecins");
			}else if(title.equals("CODE_MALADIE")){
				return Labels.getLabel("Champ.Maladie.Code");
			}
		}else if(entite.equals("Echantillon")){
			if(title.equals("TEMP_STOCK")){
				return Labels.getLabel("conteneur.temp");
			}else if(title.equals("RAISON_NC_TRAITEMENT")){
				return Labels.getLabel("thesaurus.liste.nonConformite.traitement.echan");
			}else if(title.equals("RAISON_NC_CESSION")){
				return Labels.getLabel("thesaurus.liste.nonConformite.cession.echan");
			}else if(title.equals("NOMBRE_DERIVES")){
				return Labels.getLabel("derives.nb");
			}else if(title.equals("EVTS_STOCK_E")){
				return Labels.getLabel("general.export.events");
			}else if(title.equals("QUANTITE_DEMANDEE")){
				return Labels.getLabel("Champ.Cession.QuantiteDemandee");
			}else if(title.equals("QUANTITE_UNITE_ID")){
				return Labels.getLabel("Champ.Cession.QuantiteDemandeeUnite");
			}
		}else if(entite.equals("ProdDerive")){
			if(title.equals("TEMP_STOCK")){
				return Labels.getLabel("conteneur.temp");
			}else if(title.equals("QUANTITE_UTILISEE")){
				return Labels.getLabel("ficheProdDerive.transformation.quantitevolume");
			}else if(title.equals("QUANTITE_UTILISEE_UNITE")){
				return Labels.getLabel("ficheProdDerive.transformation.unite");
			}else if(title.equals("RAISON_NC_TRAITEMENT")){
				return Labels.getLabel("thesaurus.liste.nonConformite.traitement.derive");
			}else if(title.equals("RAISON_NC_CESSION")){
				return Labels.getLabel("thesaurus.liste.nonConformite.cession.derive");
			}else if(title.equals("NOMBRE_DERIVES")){
				return Labels.getLabel("derives.nb");
			}else if(title.equals("EVTS_STOCK_D")){
				return Labels.getLabel("general.export.events");
			}else if(title.equals("PARENT_DERIVE_ID")){
				return Labels.getLabel("general.export.parent.deriveId");
			}else if(title.equals("QUANTITE_DEMANDEE")){
				return Labels.getLabel("Champ.Cession.QuantiteDemandee");
			}else if(title.equals("QUANTITE_UNITE_ID")){
				return Labels.getLabel("Champ.Cession.QuantiteDemandeeUnite");
			}
		}else if(entite.equals("Cession")){
			if(title.equals("ETABLISSEMENT")){
				return Labels.getLabel("cession.etablissement");
			}else if(title.equals("ECHANTILLONS")){
				return Labels.getLabel("cession.echantillons");
			}else if(title.equals("PRODUITS_DERIVES")){
				return Labels.getLabel("cession.prodDerive");
			}else if(title.equals("NB_ECHANTILLONS")){
				return Labels.getLabel("echantillons.nb");
			}else if(title.equals("NB_DERIVES")){
				return Labels.getLabel("derives.nb");
			}else if(title.equals("QUANTITE_UNITE_ID")){
				return Labels.getLabel("cederObjet.unite");
			}
		}else if(entite.equals("Retour")){
			if(title.equals("ECHANTILLON_ID")){
				return Labels.getLabel("Champ.Echantillon.EchantillonId");
			}else if(title.equals("PROD_DERIVE_ID")){
				return Labels.getLabel("Champ.ProdDerive.ProdDeriveId");
			}else if(title.equals("CODE_E")){
				return Labels.getLabel("Champ.Echantillon.Code");
			}else if(title.equals("CODE_D")){
				return Labels.getLabel("Champ.ProdDerive.Code");
			}else if(title.equals("RAISON")){
				return Labels.getLabel("general.export.raison");
			}
		}else if(entite.equals("LaboInter")){
			if(title.equals("TEMPERATURE_TRANSPORT")){
				return Labels.getLabel("laboInter.temperature.transport");
			}else if(title.equals("TEMPERATURE_CONSERVATION")){
				return Labels.getLabel("laboInter.temperature.conservation");
			}else if(title.equals("CODE")){
				return Labels.getLabel("Champ.Prelevement.Code");
			}else if(title.equals("PRELEVEMENT_ID")){
				return Labels.getLabel("Champ.Prelevement.PrelevementId");
			}
		}

		return title;
	}

	public Export getExport(){
		return export;
	}

	public void setExport(final Export e){
		this.export = e;
	}

	public ProfilExport getProfilExport(){
		return profilExport;
	}

	public void setProfilExport(final ProfilExport ie){
		this.profilExport = ie;
	}

	public ArrayList<Integer> getAnonymeColumn(){
		return anonymeColumn;
	}

	public void setAnonymeColumn(final ArrayList<Integer> ac){
		this.anonymeColumn = ac;
	}

	public ResultSet getResultSet(){
		return resultSet;
	}

	public void setResultSet(final ResultSet rs){
		this.resultSet = rs;
	}

	public FormatType[] getFormatTypes(){
		return formatTypes;
	}

	public void setFormatTypes(final FormatType[] ft){
		this.formatTypes = ft;
	}
}
