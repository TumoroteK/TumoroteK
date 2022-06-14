/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.echantillon.EchantillonRowRenderer;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 * Gatsbi controller regroupant les fonctionalités de modification dynamique de
 * l'interface spécifique aux échantillons
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class GatsbiControllerEchantillon {
	
	public static void addColumnForChpId(Integer chpId, Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		switch (chpId) {
		case 53: // collaborateur
			drawOperateurColumn(grid);
			break;
		case 54:
			// "code" toujours rendu par défaut
			break;
		case 55:
			drawObjetStatutColumn(grid);
			break;
		case 56: // date stock
			drawDateStockColumn(grid);
			break;
		case 57: // emplacement
			drawEmplacementColumn(grid);
			break;
		case 58: // type
			drawEchantillonTypeColumn(grid);
			break;
		case 60: // lateralite
			drawLateraliteColumn(grid);
			break;
		case 61: // quantite
			drawQuantiteColumn(grid);
			break;
		case 67: // delai cgl
			drawDelaiCglColumn(grid);
			break;
		case 68: // qualite
			drawQualiteColumn(grid);
			break;
		case 69: // tumoral
			drawTumoralColumn(grid);
			break;
		case 70: // preparation
			drawModePrepaColumn(grid);
			break;
		case 72: // sterile
			drawSterileColumn(grid);
			break;
		case 229: // codes organes
			drawCodesOrganeColumn(grid);
			break;
		case 230: // codes lesionnels
			drawCodesLesionnelColumn(grid);
			break;	
		case 243: // conforme traitement -> rendu sous la forme d'une icône
			break;
		case 244: // conforme cession -> rendu sous la forme d'une icône
			break;
		case 255: // cr anapath
			drawCrAnapathColumn(grid);
			break;
		default:
			break;
		}
	}


	// code, colonne toujours affichée
	public static Column drawCodeColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "general.code", null, null, null, "auto(code)", true);
	}

	public static Column drawBanqueColumn(Grid grid, boolean visible)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Entite.Banque", null, null, null, "auto(code)", visible);
	}

	// patient, colonne toujours affichée
	public static Column drawPatientColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "prelevement.patient", null, null, null, "auto(maladie.patient.nom)",
				true);
	}

	// nb dérivés toujours affichée
	public static Column drawNbDerivesColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Column nbProdDerivesColumn = GatsbiController.addColumn(grid, "derives.nb", null, null, null, "auto", true);
		nbProdDerivesColumn.setId("nbProdDerivesColumn");
		return nbProdDerivesColumn;
	}

	// nb cessions
	public static Column drawNbCessionsColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Column nbCessionsColumn = GatsbiController.addColumn(grid, "cession.nb", null, null, null, "auto", true);
		nbCessionsColumn.setId("nbCessionsColumn");
		return nbCessionsColumn;
	}

	// collaborateur
	public static Column drawOperateurColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.Collaborateur", null, null, null,
				"auto(collaborateur.nomAndPrenom)", true);
	}

	public static Column drawObjetStatutColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.ObjetStatut", null, null, null,
				"auto(objetStatut.nom)", true);
	}

	public static Column drawDateStockColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.DateStock", null, null, null, "auto(dateStock)",
				true);
	}

	public static Column drawEmplacementColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.Emplacement", null, null, null,
				"auto(emplacement.emplacementId)", true);
	}

	public static Column drawEchantillonTypeColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		return GatsbiController.addColumn(grid, "Champ.Echantillon.EchantillonType.Type", null, null, null,
				"auto(echantillonType.nom)", true);
	}

	public static Column drawLateraliteColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.Lateralite", null, null, null, "auto(lateralite)",
				true);
	}

	public static Column drawQuantiteColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "ficheEchantillon.quantiteLabel", null, null, null, "auto(quantite)",
				true);
	}

	public static Column drawDelaiCglColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.DelaiCgl", null, null, null, "auto(delaiCgl)", true);
	}

	public static Column drawQualiteColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.EchanQualite", null, null, null,
				"auto(echanQualite.nom)", true);
	}

	public static Column drawTumoralColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.Tumoral", null, null, null, "auto(tumoral)", true);
	}

	public static Column drawModePrepaColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.ModePrepa", null, null, null, "auto(modePrepa.nom)",
				true);
	}

	public static Column drawSterileColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.Sterile", null, null, null, "auto(sterile)", true);
	}

	public static Column drawCodesOrganeColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "Champ.Echantillon.Organe", null, null, null, "auto", true);
	}

	public static Column drawCodesLesionnelColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "ficheEchantillon.codeLesionelLabel", null, null, null, "auto", true);
	}

	public static Column drawCrAnapathColumn(Grid grid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return GatsbiController.addColumn(grid, "ficheEchantillon.crAnapathLabel", null, null, null,
				"auto(crAnapath.nom)", true);
	}
	
	/**
	 * Détermine la largeur de la colonnes des îcones à partir d'une largeur de base, 
	 * en fonction des champs d'informations affichés par gatsbi.
	 * Null si la largeur finale = 0.
	 * @param baseWidth
	 * @param contexte
	 * @return largeur sous la forme 00px
	 */
	public static String getIconesColWidthFrom(int baseWidth, Contexte contexte) {
		if (contexte.isChampIdVisible(249)) { // risque
			baseWidth += 20;
		}
		if (contexte.isChampIdVisible(243)) { // non conf traitement
			baseWidth += 20;
		}
		if (contexte.isChampIdVisible(244)) { // non conf cession
			baseWidth += 20;
		}
		
		if (baseWidth == 0) {
			return null;
		}
		
		return String.valueOf(baseWidth).concat("px");
	}
	
	/**
	 * Applique la methode de rendering correspondant au champEntité id passé en
	 * paramètre pour un objet Echantillon
	 * @param chpId
	 * @param row
	 * @param echantillon
	 * @param anonyme true/false
	 * @param access stockage true/false
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public static void applyEchantillonChpRender(Integer chpId, Row row, Echantillon echan, 
			boolean anonyme, boolean accessStockage) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {

		switch (chpId) {
		case 53: // collaborateur
			TKSelectObjectRenderer.renderCollaborateurProperty(row, echan, "collaborateur");
			break;
		case 54:
			// "code" toujours rendu par défaut
			break;
		case 55:
			EchantillonRowRenderer.renderObjetStatut(row, echan);
			break;
		case 56: // date prelevement
			TKSelectObjectRenderer.renderDateProperty(row, echan, "dateStock");
			break;
		case 57: // emplacement
			EchantillonRowRenderer.renderEmplacement(row, echan, anonyme, accessStockage);
			break;
		case 58: // type
			TKSelectObjectRenderer.renderThesObjectProperty(row, echan, "echantillonType");
			break;
		case 60: // lateralite
			EchantillonRowRenderer.renderLateralite(row, echan);
			break;
		case 61: // quantite
			EchantillonRowRenderer.renderQuantite(row, echan);
			break;
		case 67: // delai cgl
			EchantillonRowRenderer.renderDelaiCgl(row, echan);
			break;
		case 68: // qualite
			TKSelectObjectRenderer.renderThesObjectProperty(row, echan, "echanQualite");
			break;
		case 69: // tumoral
			TKSelectObjectRenderer.renderBoolProperty(row, echan, "tumoral");
			break;
		case 70: // preparation
			TKSelectObjectRenderer.renderThesObjectProperty(row, echan, "modePrepa");
			break;
		case 72: // sterile
			TKSelectObjectRenderer.renderBoolProperty(row, echan, "sterile");
			break;
		case 229: // codes organes
			EchantillonRowRenderer.renderCodeAssignes(row, 
				ManagerLocator.getCodeAssigneManager()
					.findCodesOrganeByEchantillonManager(echan));
			break;
		case 230: // codes lesionnels
			EchantillonRowRenderer.renderCodeAssignes(row, 
				ManagerLocator.getCodeAssigneManager()
					.findCodesMorphoByEchantillonManager(echan));
			break;	
		case 243: // conforme traitement -> rendu sous la forme d'une icône
			break;
		case 244: // conforme cession -> rendu sous la forme d'une icône
			break;
		case 255: // cr anapath
			EchantillonRowRenderer.renderCrAnapath(row, echan, anonyme);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Applique la methode de rendering correspondant au champEntité id passé en
	 * paramètre pour un EchantillonDTO (decorator)
	 * @param chpId
	 * @param row
	 * @param echantillonDTO 
	 * @param anonyme true/false
	 * @param access stockage true/false	 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	public static void applyEchantillonDecoratorChpRender(Integer chpId, Row row, EchantillonDTO deco, 
			boolean anonyme, boolean accessStockage) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {

		switch (chpId) {
		case 53: // collaborateur
			TKSelectObjectRenderer.renderAlphanumPropertyAsStringNoFormat(row, deco, "collaborateurNomAndPrenom");
			break;
		case 54:
			// "code" toujours rendu par défaut
			break;
		case 55:
			EchantillonRowRenderer.renderObjetStatut(row, deco.getEchantillon());
			break;
		case 56: // date prelevement
			TKSelectObjectRenderer.renderDateProperty(row, deco, "dateStockage");
			break;
		case 57: // emplacement
			EchantillonRowRenderer.renderAlphanumPropertyAsStringNoFormat(row, deco, "emplacementAdrlinMulti");
			break;
		case 58: // type
			TKSelectObjectRenderer.renderThesObjectProperty(row, deco.getEchantillon(), "echantillonType");
			break;
		case 60: // lateralite
			EchantillonRowRenderer.renderLateralite(row, deco.getEchantillon());
			break;
		case 61: // quantite
			EchantillonRowRenderer.renderAlphanumPropertyAsStringNoFormat(row, deco, "quantite");
			break;
		case 67: // delai cgl
			EchantillonRowRenderer.renderDelaiCgl(row, deco.getEchantillon());
			break;
		case 68: // qualite
			TKSelectObjectRenderer.renderThesObjectProperty(row, deco.getEchantillon(), "echanQualite");
			break;
		case 69: // tumoral
			TKSelectObjectRenderer.renderBoolProperty(row, deco.getEchantillon(), "tumoral");
			break;
		case 70: // preparation
			TKSelectObjectRenderer.renderThesObjectProperty(row, deco.getEchantillon(), "modePrepa");
			break;
		case 72: // sterile
			TKSelectObjectRenderer.renderBoolProperty(row, deco.getEchantillon(), "sterile");
			break;
		case 229: // codes organes
			EchantillonRowRenderer.renderCodeAssignes(row, deco.getCodesOrgsToCreateOrEdit()); 
			break;
		case 230: // codes lesionnels
			EchantillonRowRenderer.renderCodeAssignes(row, deco.getCodesLesToCreateOrEdit()); 
			break;	
		case 243: // conforme traitement -> rendu sous la forme d'une icône
			break;
		case 244: // conforme cession -> rendu sous la forme d'une icône
			break;
		case 255: // cr anapath
			EchantillonRowRenderer.renderCrAnapath(row, deco.getEchantillon(), true); // non clickable
			break;
		default:
			break;
		}
	}
}