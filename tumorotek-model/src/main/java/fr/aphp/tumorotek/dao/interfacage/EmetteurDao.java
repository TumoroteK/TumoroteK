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
package fr.aphp.tumorotek.dao.interfacage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Logiciel;

/**
 *
 * Interface pour le DAO du bean de domaine EMETTEUR. Interface créée le
 * 04/10/11.
 *
 * @author Pierre VENTADOUR
 * @version 2.3
 *
 */
@Repository
public interface EmetteurDao extends CrudRepository<Emetteur, Integer> {

	/**
	 * Recherche les Emetteurs ordonnés par identification.
	 * 
	 * @return Une liste d'Emetteurs.
	 */
	@Query("SELECT e FROM Emetteur e ORDER BY e.identification")
	List<Emetteur> findByOrder();

	/**
	 * Recherche les Emetteurs d'un logiciel ordonnés par identification.
	 * 
	 * @param logiciel Logiciel.
	 * @return Une liste d'Emetteurs.
	 */
	@Query("SELECT e FROM Emetteur e WHERE e.logiciel = ?1 ORDER BY e.identification")
	List<Emetteur> findByLogiciel(Logiciel logiciel);

	/**
	 * Recherche les Emetteurs d'un logiciel par identification.
	 * 
	 * @param logiciel       Logiciel des emetteurs recherchés.
	 * @param identification Identification des emetteurs recherchés.
	 * @return Une liste d'Emetteurs.
	 */
	@Query("SELECT e FROM Emetteur e WHERE e.logiciel = ?1 AND e.identification like ?2 " + "ORDER BY e.identification")
	List<Emetteur> findByLogicielAndIdentification(Logiciel logiciel, String identification);

	/**
	 * Recherche les Emetteurs par identification et service.
	 * 
	 * @param identification Identification des emetteurs recherchés.
	 * @param service        Service des emetteurs recherchés.
	 * @return Une liste d'Emetteurs.
	 */
	@Query("SELECT e FROM Emetteur e WHERE e.identification = ?1 AND e.service = ?2")
	List<Emetteur> findByIdentificationAndService(String identification, String service);

	/**
	 * Recherche les Emetteurs dont les identifiants sont passés en paramètre.
	 * 
	 * @param ids Liste d'identifiants.
	 * @return Liste d'Emetteurs.
	 */
	@Query("SELECT e FROM Emetteur e WHERE e.emetteurId in (?1) ORDER BY e.identification")
	List<Emetteur> findByIdInList(List<Integer> ids);
}
