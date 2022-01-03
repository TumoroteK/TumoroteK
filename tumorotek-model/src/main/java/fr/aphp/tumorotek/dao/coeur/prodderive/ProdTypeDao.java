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
package fr.aphp.tumorotek.dao.coeur.prodderive;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.dao.PfDependantTKThesaurusDao;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Interface pour le DAO du bean de domaine ProdType. Interface créée le
 * 25/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Repository
public interface ProdTypeDao extends CrudRepository<ProdType, Integer>, PfDependantTKThesaurusDao<ProdType> {

	/**
	 * Recherche les types de produits dérivés dont la valeur est égale au
	 * paramètre.
	 * 
	 * @param type Type du produit dérivé.
	 * @return une liste de types.
	 */
	@Query("SELECT p FROM ProdType p WHERE p.nom like ?1")
	List<ProdType> findByType(String type);

	/**
	 * Recherche le type qui est lié au produit dérivé passé en paramètre.
	 * 
	 * @param prodDeriveId Identifiant du produit dérivé pour lequel on recherche un
	 *                     type.
	 * @return Liste de type.
	 */
	@Query("SELECT p FROM ProdType p left join p.prodDerives d WHERE d.prodDeriveId = ?1")
	List<ProdType> findByProdDeriveId(Integer prodDeriveId);

	/**
	 * Recherche tous les ProdTypes sauf celui dont l'identifiant est passé en
	 * paramètre.
	 * 
	 * @param id Identifiant du ProdType à exclure.
	 * @return Liste de ProdTypes.
	 */
	@Query("SELECT p FROM ProdType p WHERE p.id != ?1")
	List<ProdType> findByExcludedId(Integer id);

	@Override
	@Query("SELECT p FROM ProdType p WHERE p.plateforme = ?1 ORDER BY p.nom")
	List<ProdType> findByPfOrder(Plateforme pf);

	@Override
	@Query("SELECT p FROM ProdType p ORDER BY p.nom")
	List<ProdType> findByOrder();
}
