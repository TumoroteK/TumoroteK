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
package fr.aphp.tumorotek.manager.systeme;

import java.util.List;

import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * 
 * Interface pour le manager du bean de domaine Unite.
 * Interface créée le 01/10/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface UniteManager {
	
	/**
	 * Recherche une unité dont l'identifiant est passé en paramètre.
	 * @param uniteId Identifiant de l'unité que l'on recherche.
	 * @return Une Unite.
	 */
	Unite findByIdManager(Integer uniteId);
	
	/**
	 * Recherche toutes les unités présentes dans la base.
	 * @return Liste d'Unite.
	 */
	List<Unite> findAllObjectsManager();
	
	/**
	 * Recherche toutes les unités dont l'unite commence comme celle 
	 * passée en paramètre.
	 * @param unite Unite que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste d'Unite
	 */
	List<Unite> findByUniteLikeManager(String unite, boolean exactMatch);
	
	/**
	 * Recherche toutes les unités dont le type commence comme celui 
	 * passé en paramètre.
	 * @param type Type que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste d'Unite
	 */
	List<Unite> findByTypeLikeManager(String type, boolean exactMatch);
	
	/**
	 * Recherche les doublons de l'Unite passé en paramètre.
	 * @param unite Unite pour laquelle on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonManager(Unite unite);
	
	/**
	 * Test si une unité est liée à des objets de la base.
	 * @param unite Unite que l'on souhaite tester.
	 * @return Vrai si l'Unite est utilisée.
	 */
	Boolean isUsedObjectManager(Unite unite);
	
	/**
	 * Persist une instance d'Unite dans la base de données.
	 * @param unite Nouvelle instance de l'objet à créer.
	 * @throws DoublonFoundException Lance une exception si un doublon de
	 * l'objet à créer se trouve déjà dans la base.
	 */
	void createObjectManager(Unite unite);
	
	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * @param unite Objet à mettre à jour dans la base.
	 * @throws DoublonFoundException Lance une exception si un doublon de
	 * l'objet à créer se trouve déjà dans la base.
	 */
	void updateObjectManager(Unite unite);
	
	/**
	 * Supprime une Unite de la base de données.
	 * @param unite Unite à supprimer de la base de données.
	 * @throws DoublonFoundException Lance une exception si l'objet
	 * est utilisé par des échantillons.
	 */
	void removeObjectManager(Unite unite);

}
