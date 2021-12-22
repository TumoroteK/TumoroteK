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
package fr.aphp.tumorotek.dao.contexte;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 * Interface pour le DAO du bean de domaine Transporteur.
 *
 * Date: 09/09/2009
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Repository
public interface TransporteurDao extends CrudRepository<Transporteur, Integer> {
	/**
	 * Recherche les transporteurs dont le nom est égal au paramètre.
	 * 
	 * @param nom pour lequel on recherche des transporteurs.
	 * @return une liste de transporteurs.
	 */
	@Query("SELECT t FROM Transporteur t WHERE t.nom = ?1")
	List<Transporteur> findByNom(String nom);

	/**
	 * Recherche les transporteurs dont le nom du contact est donné en paramètre.
	 * 
	 * @param contactNom du contact pour lequel on recherche des transporteurs.
	 * @return une liste de transporteurs.
	 */
	@Query("SELECT t FROM Transporteur t WHERE t.contactNom = ?1")
	List<Transporteur> findByContactNom(String contactNom);

	/**
	 * Recherche les transporteurs archivés.
	 * 
	 * @param archive .
	 * @return une liste de transporteurs.
	 */
	@Query("SELECT t FROM Transporteur t WHERE t.archive = ?1 ORDER BY t.nom")
	List<Transporteur> findByArchive(boolean archive);

	/**
	 * Recherche les transporteurs dont les coordonnées sont passées en paramètre.
	 * 
	 * @param coordonnee pour lesquelles on recherche des transporteurs.
	 * @return une liste transporteurs.
	 */
	@Query("SELECT t FROM Transporteur t WHERE t.coordonnee = ?1")
	List<Transporteur> findByCoordonnee(Coordonnee coordonnee);

	/**
	 * Recherche le transporteur dont l'identifiant est passé en paramètre.
	 * L'association avec la table COORDONNEE sera chargée par l'intermédiaire d'un
	 * fetch.
	 * 
	 * @param transporteurId est l'identifiant du transporteur recherché.
	 * @return un transporteur.
	 */
	@Query("SELECT t FROM Transporteur t LEFT JOIN FETCH t.coordonnee WHERE t.transporteurId = ?1")
	List<Transporteur> findByIdWithFetch(Integer transporteurId);

	/**
	 * Recherche tous les Transporteurs ordonnés.
	 * 
	 * @return Liste ordonnée de Transporteurs.
	 */
	@Query("SELECT t FROM Transporteur t ORDER BY t.nom")
	List<Transporteur> findByOrder();

	/**
	 * Recherche tous les transporteurs sauf celui dont l'id est passé en paramètre.
	 * 
	 * @param transporteurId Identifiant du transporteur que l'on souhaite exclure
	 *                       de la liste retournée.
	 * @return une liste de transporteurs.
	 */
	@Query("SELECT t FROM Transporteur t WHERE t.transporteurId != ?1")
	List<Transporteur> findByExcludedId(Integer transporteurId);

}
