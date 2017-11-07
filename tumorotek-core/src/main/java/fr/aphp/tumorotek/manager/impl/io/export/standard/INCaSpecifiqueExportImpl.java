package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.sql.Connection;

import fr.aphp.tumorotek.manager.io.export.standard.IncaSpecifiqueExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;

public class INCaSpecifiqueExportImpl implements IncaSpecifiqueExport {

	@Override
	public String getQuestAntTabac(Connection con, 
									Patient patient, Banque bank) {
		String bool =  ExportCatalogueManagerImpl.fetchItemAsString(con, 
			"SELECT ANNOTATION_VALEUR.BOOL FROM ANNOTATION_VALEUR, "
				+ "CHAMP_ANNOTATION, TABLE_ANNOTATION" 
				+ " WHERE ANNOTATION_VALEUR.OBJET_ID=" 
				+ patient.getPatientId().toString()
				+ " AND CHAMP_ANNOTATION.NOM like '069%' " 
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
					+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
				+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=1 "
				+ "AND ANNOTATION_VALEUR.BANQUE_ID="
								+ bank.getBanqueId().toString(),  
				null, true, null, "");
		
		if (bool.equals("0")) {
			bool = "N";
		} else if (bool.equals("1")) {
			bool = "O";
		}
		return bool;
	}	

	@Override
	public String getQuestFamilial(Connection con, 
									Patient patient, Banque bank) {
		String bool =  ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.BOOL FROM ANNOTATION_VALEUR, "
					+ "CHAMP_ANNOTATION, TABLE_ANNOTATION" 
					+ " WHERE ANNOTATION_VALEUR.OBJET_ID=" 
					+ patient.getPatientId().toString()
					+ " AND CHAMP_ANNOTATION.NOM like '070%' " 
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
					+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
						+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
					+ "AND TABLE_ANNOTATION.ENTITE_ID=1 "
					+ "AND ANNOTATION_VALEUR.BANQUE_ID="
									+ bank.getBanqueId().toString(),  
					null, true, null, "");
			
			if (bool.equals("0")) {
				bool = "N";
			} else if (bool.equals("1")) {
				bool = "O";
			}
			return bool;
	}

	@Override
	public String getQuestPro(Connection con, 
									Patient patient, Banque bank) {
		String bool =  ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.BOOL FROM ANNOTATION_VALEUR, "
					+ "CHAMP_ANNOTATION, TABLE_ANNOTATION" 
					+ " WHERE ANNOTATION_VALEUR.OBJET_ID=" 
					+ patient.getPatientId().toString()
					+ " AND CHAMP_ANNOTATION.NOM like '071%' " 
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
					+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
						+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
					+ "AND TABLE_ANNOTATION.ENTITE_ID=1 "
					+ "AND ANNOTATION_VALEUR.BANQUE_ID="
									+ bank.getBanqueId().toString(),  
					null, true, null, "");
			
			if (bool.equals("0")) {
				bool = "N";
			} else if (bool.equals("1")) {
				bool = "O";
			}
			return bool;
	}

	@Override
	public String getRadioNaif(Connection con, 
										Echantillon echantillon) {
		String bool = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.BOOL FROM "
				+ "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
				+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
				+ echantillon.getEchantillonId().toString()
				+ " AND CHAMP_ANNOTATION.NOM like '072%' "
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
				+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=3", 
						null, true, null, ""); 
		
		if (bool.equals("0")) {
			bool = "N";
		} else if (bool.equals("1")) {
			bool = "O";
		}
		return bool;
	}

	@Override
	public String getChimioNaif(Connection con, Echantillon echantillon) {
		String bool = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.BOOL FROM "
				+ "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
				+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
				+ echantillon.getEchantillonId().toString()
				+ " AND CHAMP_ANNOTATION.NOM like '073%' "
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
				+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=3", 
						null, true, null, ""); 
		
		if (bool.equals("0")) {
			bool = "N";
		} else if (bool.equals("1")) {
			bool = "O";
		}
		return bool;
	}

	@Override
	public String getStatutTabac(Connection con, Patient patient, 
													Banque bank) {
		return ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, "
					+ "CHAMP_ANNOTATION, TABLE_ANNOTATION" 
					+ " WHERE ANNOTATION_VALEUR.OBJET_ID=" 
					+ patient.getPatientId().toString()
					+ " AND CHAMP_ANNOTATION.NOM like '074%' " 
					+ " AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID AND "
				+ "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
					+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
						+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
					+ "AND TABLE_ANNOTATION.ENTITE_ID=1 "
					+ "AND ANNOTATION_VALEUR.BANQUE_ID="
									+ bank.getBanqueId().toString(),  
					null, true, "([0-3]) : .+", "");
	}

	@Override
	public String getNPA(Connection con, Patient patient, 
													Banque bank) {
		return ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.TEXTE FROM ANNOTATION_VALEUR, "
					+ "CHAMP_ANNOTATION, TABLE_ANNOTATION" 
					+ " WHERE ANNOTATION_VALEUR.OBJET_ID=" 
					+ patient.getPatientId().toString()
					+ " AND CHAMP_ANNOTATION.NOM like '075%' " 
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
					+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
						+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
					+ "AND TABLE_ANNOTATION.ENTITE_ID=1 "
					+ "AND ANNOTATION_VALEUR.BANQUE_ID="
									+ bank.getBanqueId().toString(),  
					null, true, null, "");
	}

}
