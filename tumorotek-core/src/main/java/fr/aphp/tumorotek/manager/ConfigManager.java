package fr.aphp.tumorotek.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 2.1
 * @author Mathieu BARTHELEMY
 *
 */
public class ConfigManager {

	public static final String ENTETE_PRELEVEMENT = "Prel:";
	public static final String ENTETE_TUBE = "Tube:";
	public static final String ENTETE_TYPE = "Type:";
	public static final String ENTETE_PATIENT = "Patient:";
	public static final String ENTETE_DATE_CONGELATION = "Date Cong:";
	public static final String ENTETE_QUANTITE = "Quantite:";

	public static final String G2D_FONT_FAMILY = "DejaVu Serif";
	public static final String BOLD_FONT_STYLE = "DejaVu Serif";

	// ------------ DATABASE -------------
	public static final String DB_ORACLE = "oracle";
	public static final String DB_MYSQL = "mysql";
	// ------------ EXPORT ---------------
	public static final short ENTITE_ID_PATIENT = 1;
	public static final short ENTITE_ID_PRELEVEMENT = 2;
	public static final short ENTITE_ID_ECHANTILLON = 3;
	public static final short ENTITE_ID_DERIVE = 8;
	public static final short ENTITE_ID_CESSION = 5;

	public static final short DEFAULT_EXPORT = 1;
	public static final short INCA_EXPORT = 2;
	public static final short TVGSO_EXPORT = 3;
	public static final short BIOCAP_EXPORT = 4;
	public static final short BIOBANQUES_EXPORT = 5; //@since 2.1

	// BIOCAP
	// public static final String BIOCAP_XSLT_FILE = "/xslt/biocap_xslt.xsl";
	// public static final String BIOCAP_OUT_FILE = "biocap_export.xlsx";
	// public static final String BIOCAP_OUT_FILE = "biocap_export.xlsx";

	public static final String[] aSheetName = { "Patient List",
			"Prelevement List", "Echantillon List", "Derive List",
			"Cession List" };
	public static final String OFFICE_OPENXML_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String OFFICE_EXCEL_MIME_TYPE = "application/vnd.ms-excel";
	public static final String UNICODE_CHARSET = "UTF-8";

	// STATS INDICATEURS
	public static final String SUBDIVISION_TYPE_THESAURUS = "Thesaurus";

	public static final Map<String, String> translateMonthMap;
	static {
		translateMonthMap = new HashMap<String, String>();
		translateMonthMap.put("Jan", "J");
		translateMonthMap.put("Feb", "F");
		translateMonthMap.put("Mar", "M");
		translateMonthMap.put("Apr", "A");
		translateMonthMap.put("May", "M");
		translateMonthMap.put("Jun", "J");
		translateMonthMap.put("Jul", "J");
		translateMonthMap.put("Aug", "A");
		translateMonthMap.put("Sep", "S");
		translateMonthMap.put("Oct", "O");
		translateMonthMap.put("Nov", "N");
		translateMonthMap.put("Dec", "D");
	}

	public static final Map<String, Short> entityMap;
	static {
		entityMap = new HashMap<String, Short>();
		entityMap.put("pat", ENTITE_ID_PATIENT);
		entityMap.put("prel", ENTITE_ID_PRELEVEMENT);
		entityMap.put("echan", ENTITE_ID_ECHANTILLON);
		entityMap.put("pd", ENTITE_ID_DERIVE);
		entityMap.put("cession", ENTITE_ID_CESSION);
	}



	public static final List<String> abbrMonthMap = Arrays.asList("Jan", "Feb",
			"Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
	
	//mettre à jour
	public static List<String> abbrWeekMap = Arrays.asList(
			"w01", "w02","w03", "w04", "w05", "w06", "w07", "w08", "w09",
			"w10", "w11", "w12","w13", "w14", "w15", "w16", "w17", "w18", "w19",
			"w20", "w21", "w22","w23", "w24", "w25", "w26", "w27", "w28", "w29",
			"w30", "w31", "w32","w33", "w34", "w35", "w36", "w37", "w38", "w39",
			"w40", "w41", "w42","w43", "w44", "w45", "w46", "w47", "w48", "w49",
			"w50", "w51", "w52");

	public static final List<String> abbrSemesterMap = Arrays.asList("Semestre1", "Semestre2");
			
	public static final List<String> abbrTrimesterMap = Arrays.asList("Trimestre1", "Trimestre2",
			"Trimestre3", "Trimestre4");
}
