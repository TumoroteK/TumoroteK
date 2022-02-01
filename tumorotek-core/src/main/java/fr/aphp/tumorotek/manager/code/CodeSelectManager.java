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
package fr.aphp.tumorotek.manager.code;

import java.util.List;

import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeSelect. Interface créée le
 * 02/07/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface CodeSelectManager {

	/**
	 * Recherche toutes les instances de codes favoris.
	 * 
	 * @return List contenant les codes.
	 */
	public List<CodeSelect> findAllObjectsManager();

	/**
	 * Recherche les codes référencés par les CodeSelects pour utilisateur et la
	 * banque passés en paramètres et dont le code est like celui passé en
	 * paramètre.
	 * 
	 * @param code
	 * @param exactMatch
	 * @param l'utilisateur pour lequel on recherche des CodeSelects.
	 * @param la            banque pour laquelle on recherche des CodeSelects.
	 * @return une liste de CodeCommon.
	 */
	public List<CodeCommon> findByCodeOrLibelleLikeManager(String codeOrLibelle, final boolean exactMatch,
			final Utilisateur u, final Banque b);

	/**
	 * Recherche les codes du Dossier passé en paramètre.
	 * 
	 * @param dossier dont on veut les codes contenus.
	 * @return une liste de codes CodeSelect.
	 */
	public List<CodeSelect> findByCodeDossierManager(final CodeDossier parent);

	/**
	 * Recherche les codes référencés par les codes favoris du Dossier passé en
	 * paramètre.
	 * 
	 * @param dossier dont on veut les codes référencés contenus.
	 * @return une liste de codes Common.
	 */
	public List<CodeCommon> findCodesFromSelectByDossierManager(final CodeDossier parent);

	/**
	 * Recherche les codes referencés par les codes favoris qui sont non contenus
	 * dans un dossier pour un utilisateur et une banque donnée.
	 * 
	 * @param utilisateur
	 * @param banque
	 * @return une liste de codes CodeCommon.
	 */
	public List<CodeCommon> findByRootDossierManager(final Utilisateur u, final Banque bank);
	/**
	 * Recherche les codes pour un utilisateur et une banque.
	 * 
	 * @param utilisateur Utilisateur
	 * @param banque      Banque
	 * @return une liste de codes CodeSelect.
	 */
	public List<CodeSelect> findByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b);
	
	/**
	 * Recherche les codes référencés par les favoris pour un utilisateur et une
	 * banque.
	 * 
	 * @param utilisateur Utilisateur
	 * @param banque      Banque
	 * @return une liste de codes CodeSelect.
	 */
	public List<CodeCommon> findCodesFromSelectByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b);

	/**
	 * Cherche les doublons en se basant sur la methode equals() surchargee par les
	 * entites. Si l'objet est modifie donc a un id attribue par le SGBD, ce dernier
	 * est retire de la liste findAll.
	 * 
	 * @param table CodeUtilisateur dont on cherche la presence dans la base
	 * @return true/false
	 */
	public boolean findDoublonManager(final CodeSelect code);

	/**
	 * Enregsitre ou modifie un code favori. La modification ne peut porter que sur
	 * le dossier parent (cad deplacement).
	 * 
	 * @param code
	 * @param dos
	 * @param banque
	 * @param utilisateur
	 * @param String      operation creation/modification
	 */
	public void createOrUpdateManager(final CodeSelect code, final CodeDossier dos, final Banque bank,
			final Utilisateur utilisateur, final String operation);

	/**
	 * Supprime un code favori de la base de données.
	 * 
	 * @param code CodeUtilisateur à supprimer de la base de données.
	 */
	public void removeObjectManager(final CodeSelect code);

	/**
	 * Recherche tous les codes favoris définis pour une banque à la racine de
	 * l'arborescence.
	 * 
	 * @param banque Banque
	 * @return une liste de CodeSelects.
	 */
	public List<CodeCommon> findByRootDossierAndBanqueManager(final Banque bank);
}
