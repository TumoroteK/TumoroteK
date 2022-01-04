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
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine CodeDossier. Interface créée le
 * 19/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Repository
public interface CodeDossierDao extends CrudRepository<CodeDossier, Integer> {

	/**
	 * Recherche les dossiers dont le nom est like celui passé en paramètre.
	 * 
	 * @param nom    pour lequel on recherche des dossiers de codes utilisateurs.
	 * @param banque
	 * @return Liste de CodeDossier.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.nom like ?1 AND c.banque = ?2")
	List<CodeDossier> findByNomLike(String code, Banque bank);

	/**
	 * Recherche tous les dossiers contenus dans un dossier.
	 * 
	 * @param codeDossier
	 * @return Liste de CodeDossier.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.dossierParent = ?1")
	List<CodeDossier> findByCodeDossierParent(CodeDossier codeDossier);

	/**
	 * Recherche tous les dossiers des Codeutilisateur à la racine pour une banque
	 * donnée.
	 * 
	 * @param banque
	 * @return Liste de CodeDossier.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.dossierParent is null AND c.banque = ?1 AND c.codeSelect = 0")
	List<CodeDossier> findByRootCodeDossierUtilisateur(Banque bank);

	/**
	 * Recherche tous les dossiers des CodeSelect à la racine pour une banque et un
	 * utilisateur donnés.
	 * 
	 * @param utilisateur
	 * @param banque
	 * @return Liste de CodeDossier.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.dossierParent is null AND c.banque = ?2 AND c.utilisateur = ?1 "
			+ "AND c.codeSelect = 1")
	List<CodeDossier> findByRootCodeDossierSelect(Utilisateur u, Banque bank);

	/**
	 * Recherche les dossiers de codes ajoutés au favoris pour l'utilisateur et la
	 * banque passées en paramètres.
	 * 
	 * @param l'utilisateur pour lequel on recherche des dossiers.
	 * @param la            banque
	 * @return une liste de CodeDossiers.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.utilisateur = ?1 AND c.banque = ?2 AND c.codeSelect = 1")
	List<CodeDossier> findBySelectUtilisateurAndBanque(Utilisateur u, Banque b);

	/**
	 * Recherche les dossiers de codes utilisateurs pour l'utilisateur et la banque
	 * passées en paramètres.
	 * 
	 * @param l'utilisateur pour lequel on recherche des dossiers.
	 * @param la            banque
	 * @return une liste de CodeDossiers.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.utilisateur = ?1 AND c.banque = ?2 AND c.codeSelect = 0")
	List<CodeDossier> findByUtilisateurAndBanque(Utilisateur u, Banque b);

	/**
	 * Recherche tous les dossiers sauf celui dont l'id est passé en paramètre.
	 * 
	 * @param codeDossierId Identifiant du dossier que l'on souhaite exclure de la
	 *                      liste retournée.
	 * @return une liste de CodeDossiers.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.codeDossierId != ?1")
	List<CodeDossier> findByExcludedId(Integer codeDossierId);

	/**
	 * Recherche tous les dossiers de codes favoris ou utilisateur définis pour une
	 * banque à la racine de l'arborescence.
	 * 
	 * @param banque Banque
	 * @return une liste de CodeDossier.
	 */
	@Query("SELECT c FROM CodeDossier c WHERE c.dossierParent is null AND c.banque = ?1 " + "AND c.codeSelect = ?2")
	List<CodeDossier> findByRootDossierBanque(Banque bank, Boolean select);
}
