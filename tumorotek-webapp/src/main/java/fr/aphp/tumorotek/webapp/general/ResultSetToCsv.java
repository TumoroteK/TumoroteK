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

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.webapp.general.export.Export;

/**
 * Formate les données issues de la BDD sous forme de ResultSet afin de créer un
 * Fichier Excel des Exports exploitable par les Users
 * 
 * @author jhusson
 * @version 2.0
 */
public class ResultSetToCsv extends ResultSetToExcel {
	
	private static final NumberFormat decimalFormat = new DecimalFormat("####.##");
	private static final SimpleDateFormat dateFormat = 
			new SimpleDateFormat("dd/MM/yyyy hh:mm");
	
	private OutputStream outStream;
	
	private String cellSeparator;
		
	public ResultSetToCsv(OutputStream oStr, Export export, 
			ResultSet resultSet, 
			boolean isExportAnonyme,
			String cellSep) {
		super(export,resultSet,isExportAnonyme);
		this.outStream = oStr;
		this.cellSeparator = cellSep != null ? cellSep : "\t";
		setUpdateThread(getExport());
	}

	public void generate() throws Exception {
		int currentRow = 0;
		int numCols = 0;
		ResultSetMetaData resultSetMetaData = getResultSet().getMetaData();
		numCols = resultSetMetaData.getColumnCount();
		setFormatTypes(new FormatType[numCols]);

		if (isExportAnonyme()) {
			getAnonymeColumn().clear();
		}
		
		// String entite = "Patient";

		for (int i = 0; i < numCols; i++) {
			String title = resultSetMetaData.getColumnName(i + 1);
			if (getExport().getMapCorrespondanceAnnotationName() != null
					&& getExport().getMapCorrespondanceAnnotationName()
							.containsKey(title)) {
				title = getExport().getMapCorrespondanceAnnotationName().get(
						title);
			} 
			// Anonyme
			if (isExportAnonyme()) {
				if (title.equalsIgnoreCase("emplacement")
						|| title.equalsIgnoreCase("patient_nda")
						|| title.equalsIgnoreCase("patient_id")
						|| title.equalsIgnoreCase("nip")
						|| title.equalsIgnoreCase("nom_naissance")
						|| title.equalsIgnoreCase("nom")
						|| title.equalsIgnoreCase("prenom")
						|| title.equalsIgnoreCase("date_naissance")) {
					getAnonymeColumn().add(i);
				}
			}
			
			Class<?> _class = Class.forName(resultSetMetaData
					.getColumnClassName(i + 1));
			int precision = resultSetMetaData.getPrecision(i + 1);
			getFormatTypes()[i] = getFormatType(_class, precision);
			
//			if (title.equals("PATIENT_ID")) {
//				entite = "Patient";
//			} else if (title.equals("MALADIE_ID")) {
//				entite = "Maladie";
//			} else if (title.equals("PRELEVEMENT_ID")) {
//				entite = "Prelevement";
//			} else if (title.equals("ECHANTILLON_ID")) {
//				entite = "Echantillon";
//			} else if (title.equals("PROD_DERIVE_ID")) {
//				entite = "ProdDerive";
//			} else if (title.equals("CESSION_ID")) {
//				entite = "Cession";
//			}
			
			// writeCell(row, i, isAnno ? title : labelPrintTitle(title, entite), 
			//								formatTypes[i], boldStyle);
		}

		// currentRow++;
		// Write report rows
		while (getResultSet().next()) {
			for (int i = 0; i < numCols; i++) {
				Object value = getResultSet().getObject(i + 1);
				outStream.write(writeCsvCell(value, getFormatTypes()[i], i).getBytes());
			}
			outStream.write("\n".getBytes());
			currentRow ++;
			
			if (currentRow % 10 == 0) {
				if (getUpdateThread() != null) {
					if (!getUpdateThread().isInterrupted()) {
						getUpdateThread().setExportDetails(null, currentRow, 
								getUpdateThread().getTotal(), "progressbar.recherche.rows", 
								null, null);
					} else {
						getResultSet().close();
						throw new InterruptedException();
					}
				}
			}
		}
		getResultSet().close();
	}

	private String writeCsvCell(Object value, FormatType formatType, int col) {
		
		String cell = "";
	
		if (value != null) {
	
			if (formatType.equals(FormatType.BOOL)) {
				if (value instanceof BigDecimal) {
					
					if (((BigDecimal) value).compareTo(BigDecimal.ZERO) > 0) {
						cell = ObjectTypesFormatters
								.booleanLitteralFormatter(true);
					} else {
						cell = ObjectTypesFormatters
								.booleanLitteralFormatter(false);
					}
				} else {
					cell = ObjectTypesFormatters
						.booleanLitteralFormatter((Boolean) value);
				}
			} else if (formatType.equals(FormatType.DATE)) {
				cell = dateFormat.format((java.util.Date) value);
			} else if (formatType.equals(FormatType.NUMERIC)) {
				if (value instanceof Integer) {
					cell = ((Integer) value).toString();
				} else if (value instanceof Float) {
					cell = decimalFormat.format((Float) value);
				} else if (value instanceof Double) {
					cell = decimalFormat.format((Double) value);
				} else if (value instanceof BigDecimal) {
					cell = decimalFormat.format(((BigDecimal) value).doubleValue());
				} else {
					cell = decimalFormat.format((Double) value);
				}
			} else {
				cell = value.toString();
			}
	
			if (isExportAnonyme()) {
				if (this.getAnonymeColumn().contains(col)) {
					cell = "****";
				}
			}
		}
		return cell + cellSeparator;
	}
}
