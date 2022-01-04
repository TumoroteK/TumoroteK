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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine CodeSelect. Interface créée le
 * 18/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Repository
public interface CodeSelectDao extends CrudRepository<CodeSelect, Integer> {

	/**
	 * Recherche les CodeSelects dont l'utilisateur et la banque sont passés en
	 * paramètres.
	 * 
	 * @param l'utilisateur pour lequel on recherche des CodeSelects.
	 * @param la            banque pour laquelle on recherche des CodeSelects.
	 * @return une liste de CodeSelects.
	 */
	@Query("SELECT c FROM CodeSelect c WHERE c.utilisateur = ?1 AND c.banque = ?2")
	List<CodeSelect> findByUtilisateurAndBanque(Utilisateur u, Banque b);

	/**
	 * Recherche les CodeSelects dont la banque est est passée en paramètre.
	 * 
	 * @param la banque pour laquelle on recherche des CodeSelects.
	 * @return une liste de CodeSelects.
	 */
	@Query("SELECT c FROM CodeSelect c WHERE c.banque = ?1 ORDER BY c.codeSelectId")
	List<CodeSelect> findByBanque(Banque b);

	/**
	 * Recherche tous les codes favoris contenu dans un dossier.
	 * 
	 * @param codeDossier
	 * @return une liste de CodeSelects.
	 */
	@Query("SELECT c FROM CodeSelect c WHERE c.codeDossier = ?1 ORDER BY c.codeSelectId")
	List<CodeSelect> findByCodeDossier(CodeDossier codeDossier);

	/**
	 * Recherche tous les codes favoris non contenu dans un dossier pour la banque
	 * spécifiée. Par defaut un utilisateur enregistre des favoris pour une banque
	 * donnée.
	 * 
	 * @param utilisateur
	 * @param banque
	 * @return une liste de CodeSelects.
	 */
	@Query("SELECT c FROM CodeSelect c WHERE c.codeDossier is null AND c.utilisateur= ?1 AND c.banque = ?2 "
			+ "ORDER BY c.codeSelectId")
	List<CodeSelect> findByRootDossier(Utilisateur u, Banque bank);

	/**
	 * Recherche tous les codes favoris sauf celui dont l'id est passé en paramètre.
	 * 
	 * @param codeSelectId Identifiant du code que l'on souhaite exclure de la liste
	 *                     retournée.
	 * @return une liste de CodeSelects.
	 */
	@Query("SELECT c FROM CodeSelect c WHERE c.codeSelectId != ?1")
	List<CodeSelect> findByExcludedId(Integer codeSelectId);

	/**
	 * Recherche tous les codes favoris définis pour une banque à la racine de
	 * l'arborescence.
	 * 
	 * @param banque Banque
	 * @return une liste de CodeSelects.
	 */
	@Query("SELECT c FROM CodeSelect c WHERE c.codeDossier is null AND c.banque = ?1 ORDER BY c.codeSelectId")
	List<CodeSelect> findByRootDossierAndBanque(Banque bank);
}
