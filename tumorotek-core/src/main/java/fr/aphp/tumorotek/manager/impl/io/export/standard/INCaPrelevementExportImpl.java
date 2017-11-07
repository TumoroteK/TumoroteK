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
import java.text.DateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.io.export.standard.IncaPrelevementExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;

public class INCaPrelevementExportImpl implements IncaPrelevementExport {
		
	private BanqueManager banqueManager;
	
	public void setBanqueManager(BanqueManager bManager) {
		this.banqueManager = bManager;
	}

	public BanqueManager getBanqueManager() {
		return banqueManager;
	}
	

	@Override
	public String getCentreStockage(Echantillon echantillon) {
		String finess = null;
		if (echantillon.getBanque().getProprietaire() != null
				&& echantillon.getBanque()
					.getProprietaire().getEtablissement() != null) {
			finess = echantillon.getBanque()
				.getProprietaire().getEtablissement().getFiness();
		}
		if (finess != null) {
			return finess;
		} else {
			throw new ItemException(2, 
					"FINESS manquant pour le centre de stockage");
		}
	}

	@Override
	public String getCodePrelevement(Prelevement prelevement) {
		return prelevement.getCode();
	}

	@Override
	public String getDatePrelevement(Prelevement prelevement, DateFormat df) {
		if (prelevement.getDatePrelevement() != null) {
			return df.format(prelevement.getDatePrelevement().getTime());
		} else {
			throw new ItemException(2, "Date prelevement manquante");
		}
	}

	@Override
	public String getModePrelevement(Prelevement prelevement) {
		String type = null;
		if (prelevement.getPrelevementType() != null) {
			Pattern p = Pattern.compile("([BCPNOL9]) : .+");
			Matcher m = p.matcher(prelevement
							.getPrelevementType().getType());
			boolean b = m.matches();
			if (!b) {
				throw new ItemException(3, 
						"Valeur inattendue " + prelevement
						.getPrelevementType().getType());
			} else {
				type = m.group(1);
			}
		}
		if (type == null) {
			throw new ItemException(2, "Type prelevement manquant");
		}
		
		return type;
	}


	@Override
	public String getClassif(Banque bank) {

		String classifs = "";
		
		List<BanqueTableCodage> bcts = getBanqueManager()
			.getBanqueTableCodageByBanqueManager(bank);
		
		for (int i = 0; i < bcts.size(); i++) {
			if (bcts.get(i).getTableCodage().getNom().matches("CIM[^O].*")) {
				classifs = classifs + "C";
			} else if (bcts.get(i).getTableCodage()
								.getNom().matches("ADICAP")) {
				classifs = classifs + "A";
			} 
		}
		
		if (classifs.equals("")) {
			throw new ItemException(2, 
					"Aucune classification specifiee pour la banque " 
						+ bank.getNom());
		}
		
		return classifs;
	}

	@Override
	public String getCodeOrgane(Connection con, 
								Echantillon echan, 
								String codif) {
		String codifId = "1"; // ADICAP
		if (codif != null) {
			if (codif.equals("C")) {
				codifId = "2";
			}
			return ExportCatalogueManagerImpl.fetchItemAsString(con, 
					"SELECT CODE FROM CODE_ASSIGNE "
				+ "WHERE CODE_ASSIGNE.ECHANTILLON_ID=" 
				+ echan.getEchantillonId().toString()
				+ " AND CODE_ASSIGNE.IS_ORGANE=1"
				+ " AND CODE_ASSIGNE.TABLE_CODAGE_ID=" + codifId
				+ " ORDER BY CODE_ASSIGNE.EXPORT DESC," 
					+ "CODE_ASSIGNE.CODE_ASSIGNE_ID", 
				"code organe manquant", true, null, null);
		} else {
			return ExportCatalogueManagerImpl.fetchItemAsString(con, 
					"SELECT CODE FROM CODE_ASSIGNE "
				+ "WHERE CODE_ASSIGNE.ECHANTILLON_ID=" 
				+ echan.getEchantillonId().toString()
				+ " AND CODE_ASSIGNE.IS_ORGANE=1"
				+ " ORDER BY CODE_ASSIGNE.EXPORT DESC, "
					+ "CODE_ASSIGNE.CODE_ASSIGNE_ID", 
				"code organe manquant", true, null, null);
		}
		
		
	}
	
	@Override
	public String getTypeLesionnel(Connection con, Echantillon echan,
														String codif) {
			String codifId = "1"; // ADICAP
		if (codif != null) {
			if (codif.equals("C")) {
				codifId = "3";
			}
			return ExportCatalogueManagerImpl.fetchItemAsString(con, 
					"SELECT CODE_ASSIGNE.CODE FROM CODE_ASSIGNE "
					+ "WHERE CODE_ASSIGNE.ECHANTILLON_ID=" 
					+ echan.getEchantillonId().toString()
					+ " AND CODE_ASSIGNE.IS_MORPHO=1"
					+ " AND CODE_ASSIGNE.TABLE_CODAGE_ID=" + codifId
					+ " ORDER BY CODE_ASSIGNE.EXPORT DESC, "
						+ "CODE_ASSIGNE.CODE_ASSIGNE_ID", 
				"Type lesionnel manquant", true, null, null);
		} else {
			return ExportCatalogueManagerImpl.fetchItemAsString(con, 
					"SELECT CODE_ASSIGNE.CODE FROM CODE_ASSIGNE "
					+ "WHERE CODE_ASSIGNE.ECHANTILLON_ID=" 
					+ echan.getEchantillonId().toString()
					+ " AND CODE_ASSIGNE.IS_MORPHO=1"
					+ " ORDER BY CODE_ASSIGNE.EXPORT DESC, "
						+ "CODE_ASSIGNE.CODE_ASSIGNE_ID", 
				"Type lesionnel manquant", true, null, null);
		}
		
	}
	
	

	@Override
	public String getTypeEvent(Connection con, Prelevement prelevement) {
		return ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, "
			+ "CHAMP_ANNOTATION, TABLE_ANNOTATION " 
			+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
			+ prelevement.getPrelevementId().toString() 
			+ " AND CHAMP_ANNOTATION.NOM like '022%' "
			+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
					+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
			+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
				+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
			+ "AND TABLE_ANNOTATION.ENTITE_ID=2", 
				null, true, "[1234569]\\s?: .*", "9 : INCONNU"); 
	}

	@Override
	public String getVersionPTNM(Connection con, Prelevement prelevement) {
		return ExportCatalogueManagerImpl.fetchItemAsString(con,
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR,"
		+ "CHAMP_ANNOTATION, TABLE_ANNOTATION "
		+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
		+ prelevement.getPrelevementId().toString() 
		+ " AND CHAMP_ANNOTATION.NOM like '023%' "
		+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
		+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
			+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
		+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
			+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
		+ "AND TABLE_ANNOTATION.ENTITE_ID=2", 
			"Version pTNM manquante", true, "([4567X]).*", null);
	}

	@Override
	public String getTailleTumeurPT(Connection con, Prelevement prelevement) {	
		String res = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR,"
			+ " CHAMP_ANNOTATION, TABLE_ANNOTATION" 
			+ " WHERE ANNOTATION_VALEUR.OBJET_ID="
			+ prelevement.getPrelevementId().toString()
			+ " AND CHAMP_ANNOTATION.NOM like '024%' "
			+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID"
					+ "=ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
			+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=2", 
					"pT manquante", true, "([a-zA-Z0-9]*):?\\s?.*", null);
		if (res.length() > 3) {
			return res.substring(0, 3);
		} else {
			return res;
		}
	}

	@Override
	public String getEnvGangPN(Connection con, Prelevement prelevement) {
		String res = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR,"
			+ " CHAMP_ANNOTATION, TABLE_ANNOTATION" 
			+ " WHERE ANNOTATION_VALEUR.OBJET_ID="
			+ prelevement.getPrelevementId().toString()
			+ " AND CHAMP_ANNOTATION.NOM like '025%' "
			+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID"
					+ "=ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
			+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=2", 
					"pN manquant", true, "([a-zA-Z0-9]*):?\\s?.*", null);
		if (res.length() > 3) {
			return res.substring(0, 3);
		} else {
			return res;
		}
	}

	@Override
	public String getExtMetastaticPM(Connection con, Prelevement prelevement) {
		String res = ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR,"
			+ " CHAMP_ANNOTATION, TABLE_ANNOTATION" 
			+ " WHERE ANNOTATION_VALEUR.OBJET_ID="
			+ prelevement.getPrelevementId().toString()
			+ " AND CHAMP_ANNOTATION.NOM like "
				+ "'026 : Extension m%tastatique : pM%' "
			+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID"
					+ "=ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
			+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
					+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
				+ "AND TABLE_ANNOTATION.ENTITE_ID=2", 
					"pM manquant", true, "([a-zA-Z0-9]*):?\\s?.*", null);
		
		if (res.length() > 3) {
			return res.substring(0, 3);
		} else {
			return res;
		}
	}
}
