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
package fr.aphp.tumorotek.dao.io.imports;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine ImportColonne.
 *
 * Date: 24/01/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.3
 */
@Repository
public interface ImportColonneDao extends CrudRepository<ImportColonne, Integer> {

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate.
	 * 
	 * @param importTemplate Template.
	 * @return Liste d'ImportColonnes.
	 */
	@Query("SELECT i FROM ImportColonne i " + "WHERE i.importTemplate = ?1 ORDER BY i.ordre")
	List<ImportColonne> findByTemplateWithOrder(ImportTemplate importTemplate);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate et de l'entité.
	 * 
	 * @param importTemplate Template.
	 * @param entite         Entité.
	 * @return Liste d'ImportColonnes.
	 */
	@Query("SELECT i FROM ImportColonne i "
			+ "WHERE i.importTemplate = ?1 AND i.champ.champEntite.entite = ?2 ORDER BY i.ordre")
	List<ImportColonne> findByTemplateAndEntite(ImportTemplate importTemplate, Entite entite);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate et de l'entité pour les
	 * champs delegues contextuels.
	 * 
	 * @param importTemplate Template.
	 * @param entite         Entité.
	 * @return Liste d'ImportColonnes.
	 * @since 2.2.1
	 */
	@Query("SELECT i FROM ImportColonne i "
			+ "WHERE i.importTemplate = ?1 AND i.champ.champDelegue.entite = ?2 ORDER BY i.ordre")
	List<ImportColonne> findByTemplateAndEntiteDelegue(ImportTemplate importTemplate, Entite entite);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate et du datatype.
	 * 
	 * @param importTemplate Template.
	 * @param dataType       DataType.
	 * @return Liste d'ImportColonnes.
	 */
	@Query("SELECT i FROM ImportColonne i "
			+ "WHERE i.importTemplate = ?1 AND i.champ.champEntite.dataType = ?2 ORDER BY i.ordre")
	List<ImportColonne> findByTemplateAndDataType(ImportTemplate importTemplate, DataType dataType);

	/**
	 * Recherche les ImportColonnes dont l'id est différent de celui passé en
	 * paramètres.
	 * 
	 * @param excludedId     Id à exclure.
	 * @param importTemplate Template.
	 * @return Liste d'ImportColonne.
	 */
	@Query("SELECT i FROM ImportColonne i " + "WHERE i.importColonneId != ?1 AND i.importTemplate = ?2")
	List<ImportColonne> findByExcludedIdWithTemplate(Integer excludedId, ImportTemplate importTemplate);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate qui sont des thésuarus.
	 * 
	 * @param importTemplate Template.
	 * @return Liste d'ImportColonnes.
	 */
	@Query("SELECT i FROM ImportColonne i "
			+ "WHERE i.importTemplate = ?1 AND i.champ.champEntite.queryChamp is not null " + "ORDER BY i.ordre")
	List<ImportColonne> findByTemplateAndThesaurus(ImportTemplate importTemplate);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate et de l'entité (pour celles
	 * liées à un ChampAnnotation).
	 * 
	 * @param importTemplate Template.
	 * @param entite         Entité.
	 * @return Liste d'ImportColonnes.
	 */
	@Query("SELECT i FROM ImportColonne i "
			+ "WHERE i.importTemplate = ?1 AND i.champ.champAnnotation.tableAnnotation.entite = ?2 "
			+ "ORDER BY i.ordre")
	List<ImportColonne> findByTemplateAndAnnotationEntite(ImportTemplate importTemplate, Entite entite);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate et du datatype (pour celles
	 * liées à un ChampAnnotation).
	 * 
	 * @param importTemplate Template.
	 * @param dataType       DataType.
	 * @return Liste d'ImportColonnes.
	 */
	@Query("SELECT i FROM ImportColonne i "
			+ "WHERE i.importTemplate = ?1 AND i.champ.champAnnotation.dataType = ?2 ORDER BY i.ordre")
	List<ImportColonne> findByTemplateAndAnnotationDatatype(ImportTemplate importTemplate, DataType dataType);

	/**
	 * Recherche les ImportColonnes de l'ImportTemplate.
	 * 
	 * @param importTemplate Template.
	 * @return Liste de noms d'ImportColonnes.
	 */
	@Query("SELECT i.nom FROM ImportColonne i " + "WHERE i.importTemplate = ?1 ORDER BY i.ordre")
	List<String> findByTemplateWithOrderSelectNom(ImportTemplate importTemplate);

	/**
	 * Recherche les ImportColonnes dont l'id est différent de celui passé en
	 * paramètres.
	 * 
	 * @param excludedId     Id à exclure.
	 * @param importTemplate Template.
	 * @return Liste de noms d'ImportColonne.
	 */
	@Query("SELECT i.nom FROM ImportColonne i " + "WHERE i.importColonneId != ?1 AND i.importTemplate = ?2")
	List<String> findByExcludedIdWithTemplateSelectNom(Integer excludedId, ImportTemplate importTemplate);
}
