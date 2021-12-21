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
package fr.aphp.tumorotek.dao.stockage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine Emplacement. Interface créée le
 * 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Repository
public interface EmplacementDao extends CrudRepository<Emplacement, Integer> {

	/**
	 * Recherche tous les Emplacements d'une terminale.
	 * 
	 * @param terminale Terminale des emplacements recherchés.
	 * @return Liste ordonnée d'Emplacements.
	 */
	@Query("SELECT e FROM Emplacement e " + "WHERE e.terminale = ?1 " + "ORDER BY e.position")
	List<Emplacement> findByTerminaleWithOrder(Terminale terminale);

	/**
	 * Recherche tous les Emplacements d'une terminale, vide ou non.
	 * 
	 * @param terminale Terminale des emplacements recherchés.
	 * @param vide      True ou False.
	 * @return Liste ordonnée d'Emplacements.
	 */
	@Query("SELECT e FROM Emplacement e " + "WHERE e.terminale = ?1 " + "AND e.vide = ?2 "
			+ "ORDER BY e.position, e.entite.entiteId, e.objetId")
	List<Emplacement> findByTerminaleAndVide(Terminale terminale, boolean vide);

	/**
	 * Compte tous les Emplacements d'une terminale, vide ou non.
	 * 
	 * @param terminale Terminale des emplacements recherchés.
	 * @param vide      True ou False.
	 * @return Nombre d'Emplacements.
	 */
	@Query("SELECT count(e) FROM Emplacement e " + "WHERE e.terminale = ?1 " + "AND e.vide = ?2")
	List<Long> findByCountTerminaleAndVide(Terminale terminale, boolean vide);

	/**
	 * Recherche tous les Enceintes pour un couple objetId/Entité.
	 * 
	 * @param objetId Identifiant de l'objet.
	 * @param entite  Entité de l'objet.
	 * @return Liste d'Emplacements.
	 */
	@Query("SELECT e FROM Emplacement e " + "WHERE e.objetId = ?1 " + "AND e.entite = ?2")
	List<Emplacement> findByObjetIdEntite(Integer objetId, Entite entite);

	/**
	 * Recherche tous les Emplacements d'une Terminale et pour une position donnée.
	 * 
	 * @param terminale Terminale des emplacements recherchés.
	 * @param position  Position pour laquelle on recherche un emplacement.
	 * @return Liste d'Emplacements.
	 */
	@Query("SELECT e FROM Emplacement e " + "WHERE e.terminale = ?1 " + "AND e.position = ?2")
	List<Emplacement> findByTerminaleAndPosition(Terminale terminale, Integer position);

	/**
	 * Recherche les Emplacements d'une terminale, sauf celui dont l'identifiant est
	 * en paramètre.
	 * 
	 * @param emplacementId Identifiant de l'emplacement à exclure.
	 * @param terminale     Terminale des emplacements recherchés.
	 * @return Liste d'Emplacements.
	 */
	@Query("SELECT e FROM Emplacement e " + "WHERE e.emplacementId != ?1 " + "AND e.terminale = ?2")
	List<Emplacement> findByExcludedIdTerminale(Integer emplacementId, Terminale terminale);
}
