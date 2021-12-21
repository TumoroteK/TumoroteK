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

import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;

/**
 *
 * Interface pour le DAO du bean de domaine ImportHistorique.
 *
 * Date: 09/02/2011
 *
 * @author Pierre VENTADOUR
 * @version 2.3
 */
@Repository
public interface ImportHistoriqueDao extends CrudRepository<ImportHistorique, Integer> {

	/**
	 * Recherche les ImportHistoriques d'un ImportTemplate ordonnés par date. Ordre
	 * descendant.
	 * 
	 * @param importTemplate ImportTemplate.
	 * @return Liste d'ImportHistoriques.
	 */
	@Query("SELECT i FROM ImportHistorique i " + "WHERE i.importTemplate = ?1 ORDER BY i.date desc")
	List<ImportHistorique> findByTemplateWithOrder(ImportTemplate importTemplate);

	/**
	 * Recherche les ImportHistoriques dont l'id est différent de celui passé en
	 * paramètres.
	 * 
	 * @param excludedId Id à exclure.
	 * @return Liste d'ImportHistoriques.
	 */
	@Query("SELECT i FROM ImportHistorique i " + "WHERE i.importHistoriqueId != ?1")
	List<ImportHistorique> findByExcludedId(Integer excludedId);

	/**
	 * Recherche les prélevements importés pour une opération d'import dont
	 * l'historique est passé en paramètre.
	 * 
	 * @param hist ImportHistorique
	 * @return liste de Prelevement
	 * @since 2.0.10.3
	 */
	@Query("SELECT p FROM Prelevement p, Importation i " + " WHERE i.objetId = p.prelevementId"
			+ " AND i.entite.entiteId = 2" + " AND i.importHistorique = ?1" + " ORDER BY p.prelevementId")
	List<Prelevement> findPrelevementByImportHistorique(ImportHistorique hist);

}
