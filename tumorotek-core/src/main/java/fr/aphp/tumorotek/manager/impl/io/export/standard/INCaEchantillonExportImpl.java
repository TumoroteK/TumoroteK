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
package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.io.export.standard.IncaEchantillonExport;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * Classe regroupant les methodes récupérant les items 
 * du bloc INFORMATIONS ECHANTILLON spécifié par l'export INCa/TVGSO.
 * Date: 15/09/2011
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class INCaEchantillonExportImpl implements IncaEchantillonExport {
	
	private Log log = LogFactory.getLog(IncaEchantillonExport.class);
	
	private ConteneurManager conteneurManager;
	private EchantillonManager echantillonManager;

	public void setConteneurManager(ConteneurManager cManager) {
		this.conteneurManager = cManager;
	}
	
	public ConteneurManager getConteneurManager() {
		return conteneurManager;
	}

	public void setEchantillonManager(EchantillonManager eManager) {
		this.echantillonManager = eManager;
	}	

	public EchantillonManager getEchantillonManager() {
		return echantillonManager;
	}

	@Override
	public String getIsTumoral(Echantillon echantillon) {
		
		if (echantillon.getTumoral() != null) {
			if (echantillon.getTumoral()) {
				return "O";
			} else {
				return "N";
			}
		} else {
			throw new ItemException(2, "Tumoral/non tumoral manquant");
		}
	}

	@Override
	public String getModeConservation(Echantillon echantillon) {
		
		if (echantillon.getEmplacement() != null) {
			Conteneur cont = getConteneurManager()
				.findFromEmplacementManager(echantillonManager
							.getEmplacementManager(echantillon));
			if (cont.getTemp() != null) {
				if (cont.getTemp() == -196) {
					return "4";
				} else if (cont.getTemp() == -140) {
					return "3";
				} else if (cont.getTemp() == -80) {
					return "2";
				} else if (cont.getTemp() == -20) {
					return "1";
				} 
			}
		} else {
			throw new ItemException(2, "Mode conservation manquant");
		}
		return "5";
	}
	
	public String getEchantillonType(Echantillon echantillon) {
		if (echantillon.getEchantillonType().getType()
									.matches(".*[Tt][Ii][Ss][Ss][Uu].*")) {
			return "T";
		} else if (echantillon.getEchantillonType()
				.getType().matches(".*[Cc][Ee][Ll][Ll][Uu][Ll][Ee][Ss].*")) {
			return "C";
		}
		return "9";
	}

	@Override
	public String getModePreparation(Echantillon echantillon) {
		if (echantillon.getModePrepa() != null) {			
			if (echantillon.getModePrepa()
					.getNom().matches(".*[Dd][Mm][Ss][Oo].*")) {
				return "1";
			} else if (echantillon.getModePrepa()
					.getNom().matches(".*[Cc][Uu][Ll][Oo][Tt].*")) {
				return "2";
			} else if (echantillon.getModePrepa()
					.getNom().matches(".*[Tt][Ii][Ss][Ss][Uu].*")) {
				return "3";
			} else {
				return "9";
			}
		} else {
			throw new ItemException(2, "Mode préparation manquant");
		}
	}

	@Override
	public String getDelaiCongelation(Echantillon echantillon) {
		if (echantillon.getDelaiCgl() != null 
							&& echantillon.getDelaiCgl() > 0) {
			 if (echantillon.getDelaiCgl() < 30) { 
				return "1"; 
			} else if (echantillon.getDelaiCgl() >= 30) { 
				return "2"; 
			}
		} 
		return "9";
	}

	@Override
	public String getControles(Connection con, Echantillon echantillon) {
		String controle = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, "
					+ "TABLE_ANNOTATION, CHAMP_ANNOTATION "
				+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
				+ echantillon.getEchantillonId().toString()
				+ " AND CHAMP_ANNOTATION.NOM like '032%' "
				+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
					+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
					+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=3", 
				"Controle sur tissus manquant", true, null, null);
		if (controle.matches(".*[Cc][Oo][Uu][Pp][Ee].*")) {
			return "1";
		} else if (controle.matches(".*[Bb][Ll][Oo][Cc].*")) {
			return "2";
		} else if (controle
				.matches(".*[Ee][Mm][Pp][Rr][Ee][Ii][Nn][Tt][Ee].*")) {
			return "3";
		} else if (controle.matches(".*[Cc][Mm][Ff].*")) {
			return "4";
		} else if (controle.matches(".*[Ss][Oo][Rr][Tt][Ii][Ee].*")) {
			return "5";
		} else {
			return "9";
		}
	}

	@Override
	public String getPourcentageCellulesTumorales(Connection con,
			Echantillon echantillon) {

		String pourcentage = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.ALPHANUM FROM "
				+ "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
				+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
				+ echantillon.getEchantillonId().toString()
				+ " AND CHAMP_ANNOTATION.NOM like '035%' "
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
						+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
				+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=3", 
						null, true, null, ""); 
		
		if (!pourcentage.equals("")) {
			try {
				if (Float.valueOf(pourcentage) >= 0 
								&& Float.valueOf(pourcentage) <= 100) {
					return pourcentage;
				} else {
					throw new ItemException(3, 
								"Valeur inattendue " + pourcentage);
				}
			} catch (Exception e) {
				throw new ItemException(3, "Valeur inattendue " + pourcentage);
			}
		} else {
			throw new ItemException(2, 
							"Pourcentage cellules tumorales manquant");
		}
	}

	@Override
	public String getProdTypeAssocie(Connection con, Echantillon echantillon, 
																String regexp) {
		Statement s = null;
		ResultSet rs  = null;
		
		String prodType = "";
		
		try {
			s = con.createStatement();
			rs = s.executeQuery("SELECT DISTINCT(PROD_TYPE.TYPE) "
					+ "FROM PROD_DERIVE, PROD_TYPE, TRANSFORMATION "
					+ "WHERE TRANSFORMATION.TRANSFORMATION_ID="
					+ "PROD_DERIVE.TRANSFORMATION_ID "
					+ "AND TRANSFORMATION.OBJET_ID=" 
					+ echantillon.getEchantillonId().toString()
					+ " AND TRANSFORMATION.ENTITE_ID=3"
					+ " AND PROD_TYPE.PROD_TYPE_ID=PROD_DERIVE.PROD_TYPE_ID");
			while (rs.next()) {
				prodType = rs.getString(1);
				if (prodType != null) {
					if (prodType.matches(regexp)) {
						return "1";
					}
				}
			}
			rs.close();
			s.close();
		} catch (SQLException sqle) {
			log.error(sqle);
		} finally {
			if (s != null) {
				try { s.close(); } catch (SQLException e) { s = null; } 
			}
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { rs = null; }
			}	
		}
		return "N";
	}

	@Override
	public String getRessourceBiolAssociee(Echantillon echantillon,
			String resType) {
		
		boolean res = getEchantillonManager()
				.itemINCa50To53Manager(echantillon, resType);
		
		if (res) {
			return "O";
		} else {
			return "N";
		}
	}

	@Override
	public String getADNconstitutionnel(Connection con, 
					Echantillon echantillon, String consentSQLRegexp) {
		String res = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ANNOTATION_VALEUR.BOOL FROM ANNOTATION_VALEUR, "
				+ "CHAMP_ANNOTATION, TABLE_ANNOTATION, "
				+ "ECHANTILLON, PRELEVEMENT, CONSENT_TYPE "
				+ "WHERE ANNOTATION_VALEUR.OBJET_ID=" 
					+ echantillon.getEchantillonId().toString()
				+ " AND CHAMP_ANNOTATION.NOM like '053%' "
				+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
					+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
				+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=3 "
				+ "AND ECHANTILLON.ECHANTILLON_ID="
					+ echantillon.getEchantillonId().toString()
				+ " AND ECHANTILLON.PRELEVEMENT_ID=PRELEVEMENT.PRELEVEMENT_ID "
				+ "AND PRELEVEMENT.CONSENT_TYPE_ID="
										+ "CONSENT_TYPE.CONSENT_TYPE_ID " 
				+ "AND CONSENT_TYPE.TYPE like '" + consentSQLRegexp  + "'",
														null, true, null, "");
		// boolean false getString -> "0"
		if (res.equals("1")) {
			return "O";
		} else if (res.equals("0")) {
			return "N";
		}
		return res;
	}
}
