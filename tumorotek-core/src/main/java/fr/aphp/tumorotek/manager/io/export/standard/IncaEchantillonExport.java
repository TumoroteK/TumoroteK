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
package fr.aphp.tumorotek.manager.io.export.standard;

import java.sql.Connection;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 * Classe regroupant les methodes récupérant les items 
 * du bloc INFORMATIONS ECHANTILLON spécifié par l'export INCa/TVGSO.
 * Date: 15/09/2011
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface IncaEchantillonExport {
	
	/**
	 * Type Echantillon TUMORAL ou non. OBLIGATOIRE.
	 * Obtenu directement dans le champ Tumoral.
	 * @param echantillon
	 * @return O ou N
	 */
	String getIsTumoral(Echantillon echantillon);

	/**
	 * Mode de conservation. Encoder selon specifications.
	 * OBLIGATOIRE.
	 * @param echantillon
	 * @return 1, 2, 3, 4 ou 5
	 */
	String getModeConservation(Echantillon echantillon);
	
	/**
	 * Type de l'échantillon. Encoder selon specifications.
	 * OBLIGATOIRE.
	 * @param echantillon
	 * @return C, T ou 9
	 */
	String getEchantillonType(Echantillon echantillon);


	/**
	 * Mode de preparation. 
	 * OBLIGATOIRE
	 * @param echantillon
	 * @return un chiffre 1, 2, 3, 9 ou ""
	 */
	String getModePreparation(Echantillon echantillon);

	/**
	 * Delai de congelation.
	 * OBLIGATOIRE. 9 par défaut.
	 * @param echantillon
	 * @return 1, 2, 9.
	 */
	String getDelaiCongelation(Echantillon echantillon);

	/**
	 * Contrôles sur tissus. Annotation 032/044
	 * OBLIGATOIRE.
	 * @param echantillon
	 * @param connection
	 * @return 1, 2, 3, 4, 5 , 9.
	 */
	String getControles(Connection con, Echantillon echantillon);
	
	/**
	 * Pourcentage de cellules tumorales. 
	 * OBLIGATOIRE.
	 * @param con
	 * @param echantillon
	 * @return 0-100.
	 */
	String getPourcentageCellulesTumorales(Connection con, 
											Echantillon echantillon);

	/**
	 * Type de prod dérivé associé à l'échantillon.
	 * @param con
	 * @param echantillon
	 * @param le type recherché.
	 * @return 1 ou N
	 */
	String getProdTypeAssocie(Connection con, 
								Echantillon echantillon, String regexp);
	
	/**
	 * Ressources biologiques associées à l'échantillon.
	 * @see EchantillonManager.itemINCa50to53manager
	 * @param echantillon
	 * @param resType
	 * @return O ou N
	 */
	String getRessourceBiolAssociee(Echantillon echantillon, String resType);


	/**
	 * ADN constitutionnel. 
	 * Applique Regexp sur le consentement associé au prélèvement.
	 * @param con
	 * @param echantillon
	 * @param regexp consentement eclairé
	 * @return O, N ou ""
	 */
	String getADNconstitutionnel(Connection con, Echantillon echantillon, 
													String consentSQLRegexp);
	
}