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

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;

/**
 * 
 * Interface pour le DAO du bean de domaine Enceinte.
 * Interface créée le 18/03/10.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EnceinteDao extends GenericDaoJpa<Enceinte, Integer> {
	
	/**
	 * Recherche toutes les Enceintes d'un conteneur ordonnées.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @return Liste ordonnée d'Enceintes.
	 */
	List<Enceinte> findByConteneurWithOrder(Conteneur conteneur);
	
	/**
	 * Recherche toutes les Enceintes filles d'une Enceinte.
	 * @param enceinte Enceinte mère des Enceintes recherchées.
	 * @return Liste ordonnée d'Enceintes.
	 */
	List<Enceinte> findByEnceintePereWithOrder(Enceinte enceinte);
	
	/**
	 * Recherche tous les noms d'Enceintes d'un conteneur.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @return Liste ordonnée des noms d'Enceintes.
	 */
	List<String> findByConteneurSelectNom(Conteneur conteneur);
	
	/**
	 * Recherche tous les noms d'Enceintes filles d'une Enceinte.
	 * @param enceinte Enceinte mère des Enceintes recherchées.
	 * @return Liste ordonnée de noms d'Enceintes.
	 */
	List<String> findByEnceintePereSelectNom(Enceinte enceinte);
	
	/**
	 * Recherche le nombre d'Enceintes filles d'une Enceinte.
	 * @param enceinte Enceinte mère des Enceintes recherchées.
	 * @return Nombre d'Enceintes filles.
	 */
	List<Long> findNumberEnceinteFilles(Enceinte enceinte);
	
	/**
	 * Recherche toutes les enceintes d'une Enceinte et pour une
	 * position donnée.
	 * @param enceintePere Enceinte père des enceintes recherchées.
	 * @param position Position pour laquelle on recherche une enceinte.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByEnceintePereAndPosition(Enceinte enceintePere,
			Integer position);
	
	/**
	 * Recherche toutes les enceintes d'une Enceinte pour une
	 * position donnée, sauf celle dont d'id est en paramètre.
	 * @param enceintePere Enceinte père des enceintes recherchées.
	 * @param position Position pour laquelle on recherche une enceinte.
	 * @param enceinteId Id de l'enceinte à exclure.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByEnceintePereAndPositionExcludedId(
			Enceinte enceintePere,
			Integer position, Integer enceinteId);
	
	/**
	 * Recherche toutes les enceintes d'un conteneur our une
	 * position donnée.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @param position Position pour laquelle on recherche une enceinte.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByConteneurAndPosition(Conteneur conteneur,
			Integer position);
	
	/**
	 * Recherche toutes les enceintes d'un Conteneur pour une
	 * position donnée, sauf celle dont d'id est en paramètre.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @param position Position pour laquelle on recherche une enceinte.
	 * @param enceinteId Id de l'enceinte à exclure.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByConteneurAndPositionExcludedId(
			Conteneur conteneur,
			Integer position, Integer enceinteId);
	
	/**
	 * Recherche les Enceintes d'un conteneur,
	 * sauf celle dont l'identifiant est en paramètre.
	 * @param enceinteId Identifiant de l'enceinte à exclure.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByExcludedIdWithConteneur(Integer enceinteId,
			Conteneur conteneur);
	
	/**
	 * Recherche les Enceintes d'un conteneur,
	 * sauf celles dont les identifiants sont en paramètre.
	 * @param enceinteId Identifiant d'une enceinte à exclure.
	 * @param enceinteId2 Identifiant d'une enceinte à exclure.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByTwoExcludedIdsWithConteneur(Integer enceinteId,
			Integer enceinteId2, Conteneur conteneur);
	
	/**
	 * Recherche les Enceintes d'une enceinte mère,
	 * sauf celle dont l'identifiants est en paramètre.
	 * @param enceinteId Identifiant d'une enceinte à exclure.
	 * @param enceinte Enceinte mère des Enceintes recherchées.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByExcludedIdWithEnceinte(Integer enceinteId,
			Enceinte enceinte);
	
	/**
	 * Recherche les Enceintes d'une enceinte mère,
	 * sauf celles dont les identifiants sont en paramètre.
	 * @param enceinteId Identifiant d'une enceinte à exclure.
	 * @param enceinteId2 Identifiant d'une enceinte à exclure.
	 * @param enceinte Enceinte mère des Enceintes recherchées.
	 * @return Liste d'Enceintes.
	 */
	List<Enceinte> findByTwoExcludedIdsWithEnceinte(Integer enceinteId,
			Integer enceinteId2, Enceinte enceinte);

	/**
	 * Recherche les Enceintes d'un conteneur en fct de leur
	 * nom.
	 * @param conteneur Conteneur des enceintes recherchées.
	 * @param nom Nom de l'enceinte.
	 * @return Liste ordonnée d'Enceintes.
	 */
	List<Enceinte> findByConteneurAndNom(Conteneur conteneur,
			String nom);
	
	/**
	 * Recherche les Enceintes filles d'une Enceinte en fct de
	 * leur nom.
	 * @param enceinte Enceinte mère des Enceintes recherchées.
	 * @param nom Nom de l'enceinte.
	 * @return Liste ordonnée d'Enceintes.
	 */
	List<Enceinte> findByEnceintePereAndNom(Enceinte enceinte,
			String nom);
}
