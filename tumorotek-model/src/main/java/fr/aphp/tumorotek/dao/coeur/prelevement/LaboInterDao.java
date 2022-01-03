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
package fr.aphp.tumorotek.dao.coeur.prelevement;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 * Interface pour le DAO du bean de domaine LaboInter.
 *
 * Date: 28/09/2009
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Repository
public interface LaboInterDao extends CrudRepository<LaboInter, Integer> {

	/**
	 * Recherche tous les labo intermediaires sauf celui dont l'id est passé en
	 * paramètre.
	 * 
	 * @param laboInterId Identifiant du labo que l'on souhaite exclure de la liste
	 *                    retournée.
	 * @return une liste de LaboInter.
	 */
	@Query("SELECT l FROM LaboInter l WHERE l.laboInterId != ?1")
	List<LaboInter> findByExcludedId(Integer laboInterId);

	/**
	 * Recherche les labos intermediaires par leur transporteur.
	 * 
	 * @param transporteur Transporteur.
	 * @return Liste des labos intermediaires.
	 */
	@Query("SELECT l FROM LaboInter l WHERE l.transporteur = ?1")
	List<LaboInter> findByTransporteur(Transporteur transporteur);

	/**
	 * Recherche les labos intermediaires par leur service.
	 * 
	 * @param service Service.
	 * @return Liste des labos intermediaires.
	 */
	@Query("SELECT l FROM LaboInter l WHERE l.service = ?1")
	List<LaboInter> findByService(Service service);

	/**
	 * Recherche les labos intermediaires par leur collaborateur.
	 * 
	 * @param collaborateur Collaborateur.
	 * @return Liste des labos intermediaires.
	 */
	@Query("SELECT l FROM LaboInter l WHERE l.collaborateur = ?1")
	List<LaboInter> findByCollaborateur(Collaborateur collaborateur);

	/**
	 * Recherche les labos intermediaires par leur prelevement. Ces labos seront
	 * ordonnés par ordre.
	 * 
	 * @param prelevement Prelevement.
	 * @return Liste ordonnée de labos intermediaires.
	 */
	@Query("SELECT l FROM LaboInter l WHERE l.prelevement = ?1 ORDER BY l.ordre")
	List<LaboInter> findByPrelevementWithOrder(Prelevement prelevement);

	/**
	 * Recherche les ordres des labos intermediaires par leur prelevement.
	 * 
	 * @param prelevement Prelevement.
	 * @return Liste d'ordres de labos intermediaires.
	 */
	@Query("SELECT l.ordre FROM LaboInter l WHERE l.prelevement = ?1 ")
	List<Integer> findByPrelevementWithOnlyOrder(Prelevement prelevement);

	/**
	 * Recherche les ordres des labos intermediaires par leur prelevement. Ne
	 * retourne pas l'ordre du labo dont l'id est passé en paramètre.
	 * 
	 * @param prelevement Prelevement.
	 * @param laboInterId Identifiant du labo à ignorer.
	 * @return Liste d'ordres de labos intermediaires.
	 */
	@Query("SELECT l.ordre FROM LaboInter l WHERE l.prelevement = ?1 AND l.laboInterId != ?2")
	List<Integer> findByPrelevementWithOnlyOrderAndExcludedId(Prelevement prelevement, Integer laboInterId);
}
