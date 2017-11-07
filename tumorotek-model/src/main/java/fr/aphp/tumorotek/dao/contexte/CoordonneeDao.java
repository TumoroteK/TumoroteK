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
import fr.aphp.tumorotek.model.contexte.Coordonnee;

/**
 * 
 * Interface pour le DAO du bean de domaine Coordonne.
 * 
 * Date: 09/09/2009
 * 
 * @author Pierre Ventadour
 * @version 2.0
 */
public interface CoordonneeDao extends GenericDaoJpa<Coordonnee, Integer> {
	
	/**
	 * Recherche les coordonnees dont l'adresse est égale au paramètre.
	 * @param adresse pour laquelle on recherche des coordonnees.
	 * @return une liste de coordonnees.
	 */
	List<Coordonnee> findByAdresse(String adresse);
	
	/**
	 * Recherche les coordonnees dont le code postal est égal au paramètre.
	 * @param cp pour lequel on recherche des coordonnees.
	 * @return une liste de coordonnees.
	 */
	List<Coordonnee> findByCp(String cp);
	
	/**
	 * Recherche les coordonnees dont la ville est égale au paramètre.
	 * @param ville pour laquelle on recherche des coordonnees.
	 * @return une liste de coordonnees.
	 */
	List<Coordonnee> findByVille(String ville);
	
	/**
	 * Recherche les coordonnees dont le pays est égal au paramètre.
	 * @param pays pour lequel on recherche des coordonnees.
	 * @return une liste de coordonnees.
	 */
	List<Coordonnee> findByPays(String pays);
	
//	/**
//	 * Recherche la coordonnee associée à l'établissement
//	 * passé en paramètre.
//	 * @param l'établissement dont on recherche les coordonnées.
//	 * @return une liste de coordonnees (de taille 1).
//	 */
//	List<Coordonnee> findByEtablissement(Etablissement etab);
//	
//	/**
//	 * Recherche la coordonnee associée au service 
//	 * passé en paramètre.
//	 * @param service pour lequel dont on recherche les coordonnées.
//	 * @return une liste de coordonnees (de taille 1).
//	 */
//	List<Coordonnee> findByService(Service serv);
//	
//	/**
//	 * Recherche la coordonnee associée au transporteur
//	 * passé en paramètre.
//	 * @param transporteur pour lequel dont on recherche les coordonnées.
//	 * @return une liste de coordonnees (de taille 1).
//	 */
//	List<Coordonnee> findByTransporteur(Transporteur transp);
//	
	/**
	 * Recherche la coordonnee associée au collaborateur dont l'id est
	 * passé en paramètre.
	 * @param collaborateurId est l'id du collaborateur pour lequel on recherche
	 * les coordonnées.
	 * @return une liste de coordonnees (de taille 1).
	 */
	List<Coordonnee> findByCollaborateurId(Integer collaborateurId);
	
	/**
	 * Recherche les coordonnees associées au collaborateur (dont l'id est
	 * passé en paramètre) et dont l'id est different de celui passé en
	 * paramètre.
	 * @param collaborateurId est l'id du collaborateur pour lequel on recherche
	 * les coordonnées.
	 * @param coordonneeId Identifiant de la coordonnée à exclure.
	 * @return une liste de coordonnees (de taille 1).
	 */
	List<Coordonnee> findByCollaborateurIdAndExcludedId(
			Integer collaborateurId, Integer coordonneeId);
	
	/**
	 * Recherche toutes les coordonnees sauf celles dont l'id est passé 
	 * en paramètre.
	 * @param coordonneeId Identifiant de la Coordonnee que l'on souhaite
	 * exclure de la liste retournée.
	 * @return une liste de Coordonnees.
	 */
	List<Coordonnee> findByExcludedId(Integer coordonneeId);

}
