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
package fr.aphp.tumorotek.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Interface du Generic DAO.
 * @param <T> est la classe de l'objet.
 * @param <PK> est sa clé primaire.
 * 
 * @author Pierre Ventadour
 * @version 09/09/2009
 * 
 */
public interface GenericDaoJpa <T, PK extends Serializable> {
	
	/** 
	 * Persist une instance d'objet dans la base de données.
	 * @param newInstance est  une instance de l'objet à créer.
	 */
	void createObject(T newInstance);
	
	T mergeObject(T o);

	/** 
	 *   Retrouve un objet qui était persistant dans la base de données en
	 *   utilisant sa clé primaire.
	 *   @param id est la clé primaire de l'objet.
	 *   @return l'objet.
	 */
	T findById(PK id);

	/**
	 * Renvoie tous les objets d'une certaine table présents dans la base
	 * de données.
	 * @return tous les objets d'une table.
	 */
	List<T> findAll();

	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * @param transientObject est l'objet à mettre à jour dans la base
	 * de données. 
	 */
	void updateObject(T transientObject);

	/**  
	 * Supprime un objet de la base de données.
	 * @param id est la clé primaire de l'objet à surrpimer. 
	 */
	void removeObject(PK id);

}
