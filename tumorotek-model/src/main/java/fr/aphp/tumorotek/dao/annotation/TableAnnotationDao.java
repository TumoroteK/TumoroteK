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
package fr.aphp.tumorotek.dao.annotation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine TableAnnotation. Interface créée le
 * 29/01/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Repository
public interface TableAnnotationDao extends CrudRepository<TableAnnotation, Integer> {

	/**
	 * Recherche les tables dont le nom est 'like' le paramètre. Les tables
	 * retournées sont triées par nom.
	 * 
	 * @param nom Nom des tables recherchés.
	 * @return Liste de TableAnnotation.
	 */
	@Query("SELECT t FROM TableAnnotation t WHERE t.nom like ?1 ORDER BY t.nom")
	List<TableAnnotation> findByNom(String nom);

	/**
	 * Recherche les tables d'une banque donnée assignées pour une entité. Les
	 * tables retournés sont triés par leur ordre assigné dans la banque.
	 * 
	 * @param entite à laquelle la table est assignée.
	 * @param banque
	 * @return Liste de TableAnnotation.
	 */
	@Query("SELECT t FROM TableAnnotation t JOIN t.tableAnnotationBanques b WHERE t.entite = ?1 AND b.pk.banque = ?2"
			+ " ORDER BY b.ordre")
	List<TableAnnotation> findByEntiteAndBanque(Entite entite, Banque banque);

	/**
	 * Recherche les tables d'une banque donnée assignées pour une entité. Les
	 * tables retournés sont triés par leur ordre assigné dans la banque.
	 * 
	 * @param entite à laquelle la table est assignée.
	 * @param banque
	 * @return Liste de TableAnnotation.
	 */
	@Query("SELECT t FROM TableAnnotation t JOIN t.tableAnnotationBanques b WHERE t.entite = ?1 AND b.pk.banque = ?2"
			+ " AND t.catalogue.nom like ?3 ORDER BY b.ordre")
	List<TableAnnotation> findByEntiteBanqueAndCatalogue(Entite entite, Banque banque, String catalogue);

	/**
	 * Trouve l'ordre max spécifié a une table pour une entite et une banque passées
	 * en paramètres.
	 * 
	 * @param bank
	 * @param entite
	 * @return liste contenant le max Ordre tableAnnotation.ordre
	 */
	@Query("SELECT max(b.ordre) FROM TableAnnotationBanque b JOIN b.pk.tableAnnotation t"
			+ " WHERE t.entite = ?1 AND b.pk.banque = ?2")
	List<Integer> findMaxOrdreForBanqueAndEntite(Entite entite, Banque bank);

	/**
	 * Recherche toutes les tables sauf celle dont l'id est passé en paramètre.
	 * 
	 * @param tableAnnotationId Identifiant de la table que l'on souhaite exclure de
	 *                          la liste retournée.
	 * @return une liste de TableAnnotation.
	 */
	@Query("SELECT t FROM TableAnnotation t WHERE t.tableAnnotationId != ?1")
	List<TableAnnotation> findByExcludedId(Integer champAnnotationId);

	/**
	 * Recherche les tables par l'entite à laquelle elles sont attribuées pour une
	 * plateforme.
	 * 
	 * @param e          Entite
	 * @param Plateforme pf
	 * @return une liste de TableAnnotation ordonnée par leur nom.
	 */
	@Query("SELECT t FROM TableAnnotation t WHERE t.entite = ?1 AND t.plateforme = ?2 AND t.catalogue is null "
			+ "ORDER BY t.nom")
	List<TableAnnotation> findByEntiteAndPlateforme(Entite e, Plateforme pf);

	/**
	 * Recherche les tables associées à au moins une banque appartenant à la
	 * plateforme.
	 * 
	 * @param pf
	 * @return une liste de TableAnnotation ordonnée par leur nom
	 */
	@Query("SELECT t from TableAnnotation t WHERE t.plateforme = ?1 order by t.nom")
	List<TableAnnotation> findByPlateforme(Plateforme pf);

	/**
	 * Recherche les tables assignées aux banques présentes dans la liste.
	 * 
	 * @param banks
	 * @return une liste de TableAnnotation ordonnée par leur nom.
	 */
	@Query("SELECT distinct t from TableAnnotation t "
			+ "JOIN t.tableAnnotationBanques tabs WHERE tabs.pk.banque in (?1) " + "ORDER BY t.entite.entiteId, t.nom")
	List<TableAnnotation> findByBanques(List<Banque> banks);

	/**
	 * Recherche les tables associées au catalogue passé en paramètre.
	 * 
	 * @param catas
	 * @return une liste de TableAnnotation.
	 */
	@Query("SELECT t from TableAnnotation t WHERE t.catalogue in (?1) "
			+ "ORDER BY t.entite.entiteId, t.tableAnnotationId")
	List<TableAnnotation> findByCatalogues(List<Catalogue> catas);

	/**
	 * Recherche les tables associées au catalogue passé en paramètre et contenant
	 * au moins un champ annotation dont editable par l'utilisateur.
	 * 
	 * @param catalogue
	 * @return une liste de TableAnnotation.
	 */
	@Query("SELECT distinct t FROM TableAnnotation t "
			+ "JOIN t.champAnnotations c WHERE t.catalogue = ?1 AND c.edit = 1 ORDER BY t.entite.entiteId")
	List<TableAnnotation> findByCatalogueAndChpEdit(Catalogue cat);

}
