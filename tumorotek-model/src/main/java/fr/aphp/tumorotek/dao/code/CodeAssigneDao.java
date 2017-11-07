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
package fr.aphp.tumorotek.dao.code;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * 
 * Interface pour le DAO du bean de domaine CodeAssigne.
 * Interface créée le 21/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface CodeAssigneDao extends GenericDaoJpa<CodeAssigne, Integer> {
	
	/**
	 * Recherche les codes assignes dont le code like celui passe en
	 *  paramètre.
	 * @param code Code pour lequel on recherche des codes assignes.
	 * @return une liste de codes assignes.
	 */
	List<CodeAssigne> findByCodeLike(String code);
	
	/**
	 * Recherche les codes assignes dont le libelle like celui passe en
	 *  paramètre.
	 * @param code libelle pour lequel on recherche des codes assignes.
	 * @return une liste de codes assignes.
	 */
	List<CodeAssigne> findByLibelleLike(String code);
	
	/**
	 * Recherche les codes qui sont assignes comme type lésionnel/morpho 
	 * pour l'échantillon.
	 * @param echantillon.
	 * @return une liste de codes assignes.
	 */
	List<CodeAssigne> findCodesMorphoByEchantillon(Echantillon echan);
	
	/**
	 * Recherche les codes qui sont assignes pour definir l'organes 
	 * dont est issu l'échantillon.
	 * @param echantillon.
	 * @return une liste de codes assignes.
	 */
	List<CodeAssigne> findCodesOrganeByEchantillon(Echantillon echan);
	
	/**
	 * Recherche tous les codes assignes sauf celui dont l'id
	 *  est passé en paramètres.
	 * @param codeAssigneId Identifiant du code que l'on souhaite
	 * exclure de la liste retournée.
	 * @param code pour filtrer le nombre de codes retournés
	 * @param echantillon pour filtrer le nombre de codes retournés
	 * @return une liste de CodeAssignes.
	 */
	List<CodeAssigne> findByExcludedId(Integer codeAssigneId, 
										String code, Echantillon echan);
	
	/**
	 * Recherche les codes lésionels assignes exportes pour 
	 * chacun des echantillons issus du prélèvement passé en paramètre.
	 * Ordonne les codes suivant id des échantillons.
	 * @param prel Prelevement
	 * @return une liste de CodeAssignes.
	 */
	List<CodeAssigne> findCodesLesExportedByPrelevement(Prelevement prel);
	
	/**
	 * Recherche les codes organes assignes exportes pour 
	 * chacun des echantillons issus du prélèvement passé en paramètre.
	 * Ordonne les codes suivant id des échantillons.
	 * @param prel Prelevement
	 * @return une liste de CodeAssignes.
	 */
	List<CodeAssigne> findCodesOrgExportedByPrelevement(Prelevement prel);
	
	/**
	 * Recherche les codes organes assignes exportes pour 
	 * chacun des echantillons issus du patient passé en paramètre.
	 * Ordonne les codes suivant id des échantillons.
	 * @param pat Patient
	 * @return une liste de CodeAssignes.
	 */
	List<CodeAssigne> findCodesOrgExportedByPatient(Patient pat);
	
	/**
	 * Recherche tous les codes assignes pour un code et un echantillon. 
	 * Permet une recherche de doublons efficace.
	 * @param code pour filtrer le nombre de codes retournés
	 * @param echantillon pour filtrer le nombre de codes retournés
	 * @return une liste de CodeAssignes.
	 */
	List<CodeAssigne> findByCodeAndEchantillon(String code, Echantillon echan);
}
