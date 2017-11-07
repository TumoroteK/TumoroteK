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

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 * 
 * Interface pour le DAO du bean de domaine Categorie.
 * 
 * Date: 09/09/2009
 * 
 * @author Pierre Ventadour
 * @version 2.0
 */
public interface CategorieDao extends GenericDaoJpa<Categorie, Integer> {
	
	/**
	 * Recherche les catégories dont le nom est égal au paramètre.
	 * @param nom pour lequel on recherche des catégories.
	 * @return une liste de catégories.
	 */
	List<Categorie> findByNom(String nom);
	
	/**
	 * Recherche les catégories dont l'établissement est égal au paramètre.
	 * @param etabId est la clé primaire de l'établissement pour lequel on 
	 * recherche des catégories.
	 * @return une liste de catégories.
	 */
	List<Categorie> findByEtablissementId(Integer etabId);
	
	/**
	 * Recherche toutes les Categories ordonnées.
	 * @return Liste ordonnée de Categories.
	 */
	List<Categorie> findByOrder();
	
	/**
	 * Recherche toutes les Categories sauf celle dont
	 * l'identifiant est passé en paramètre.
	 * @param id Identifiant de la Categorie à exclure.
	 * @return Liste de Categories.
	 */
	List<Categorie> findByExcludedId(Integer id);

}
