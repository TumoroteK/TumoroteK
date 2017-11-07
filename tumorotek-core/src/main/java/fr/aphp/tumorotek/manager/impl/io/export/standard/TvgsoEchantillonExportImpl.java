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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.io.export.standard.TvgsoEchantillonExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

public class TvgsoEchantillonExportImpl extends INCaEchantillonExportImpl 
									implements TvgsoEchantillonExport {
	
	private Log log = LogFactory.getLog(TvgsoEchantillonExport.class);
	
	@Override
	public String getIsTumoral(Echantillon echantillon) {
		
		String isTumo = "";
		try {
			isTumo = super.getIsTumoral(echantillon);
		} catch (ItemException e) {
			log.debug("TVGSO specs -> tumoral non renseigne");
			isTumo = "";
		}
		
		if (isTumo.equals("") || isTumo.equals("N")) {
			String type = echantillon.getEchantillonType().getType();
			if (type.matches("[A-Z]{1}T.* :.*")) {
				return "O";
			} else {
				return "N";
			}
		}
		
		if (isTumo.equals("")) {
			isTumo = "N";
		}
		return isTumo;
	}
	
	@Override
	public String getModeConservation(Echantillon echantillon) {
		
		String modeC = null;
	
		modeC = super.getModeConservation(echantillon);
		if (modeC.equals("5")) {
			log.debug("TVGSO specs -> mode conservation < 5");
			return "";
		}
		
		return modeC;
	}
	
	@Override
	public String getModePreparation(Connection con, 
						Echantillon echantillon) {
		String modeP = "";
		try {
			modeP = super.getModePreparation(echantillon);
		} catch (ItemException ie) {
			log.debug("TVGSO specs -> mode prepa non obligatoire");
		}
		
		if (modeP.equals("")) {
			return ExportCatalogueManagerImpl.fetchItemAsString(con, 
				"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, "
			+ "CHAMP_ANNOTATION, TABLE_ANNOTATION "
			+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
			+ echantillon.getEchantillonId().toString()
			+ " AND CHAMP_ANNOTATION.NOM like '030%' "
			+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
				+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
			+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
				+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
			+ "AND TABLE_ANNOTATION.ENTITE_ID=3", 
				null, true, "([0-9]).*", "");
		} 
		return modeP;
	}
	
	@Override
	public String getControles(Connection con, Echantillon echantillon) {
		
		return ExportCatalogueManagerImpl.fetchItemAsString(con,
					"SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR,"
			+ "CHAMP_ANNOTATION, TABLE_ANNOTATION "
			+ "WHERE ANNOTATION_VALEUR.OBJET_ID="
			+ echantillon.getEchantillonId().toString()
			+ " AND CHAMP_ANNOTATION.NOM like '032%' "
			+ "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
			+ "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
				+ "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
			+ " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
				+ "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
			+ "AND TABLE_ANNOTATION.ENTITE_ID=3", 
				null, true, "([0-9])\\s?:.*", "");
	}
	
	@Override
	public String getPourcentageCellulesTumorales(Connection con,
			Echantillon echantillon) {
		String pourcentage = "";
		try {
			pourcentage = 
				super.getPourcentageCellulesTumorales(con, echantillon);
		} catch (ItemException ie) {
			if (ie.getSeverity() == 3) {
				throw ie;
			}
		}
	
		return pourcentage;
	}
	
	@Override
	public String getRessourceBiolAssociee(Echantillon echantillon,
			String resType) {
		
		String res = super
				.getRessourceBiolAssociee(echantillon, resType);
		
		if (res.equals("O")) {
			return "1";
		} 
		return res;
	}
	
	@Override
	public String getADNconstitutionnel(Connection con, 
										Echantillon echantillon) {
		String res = super.getADNconstitutionnel(con, echantillon, "%");
		if (res.equals("O")) {
			return "1";
		} 
		return res;
	}
}
