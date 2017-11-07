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
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 * 
 * Interface pour le DAO du bean de domaine Service.
 * 
 * Date: 09/09/2009
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ServiceDao extends GenericDaoJpa<Service, Integer> {
	
	/**
	 * Recherche tous les services ordonnés par nom.
	 * @return une liste ordonnée de services.
	 */
	List<Service> findByOrder();
	
	/**
	 * Recherche les services dont le nom est égal au paramètre.
	 * @param nom pour lequel on recherche des services.
	 * @return une liste de services.
	 */
	List<Service> findByNom(String nom);
	
	/**
	 * Recherche les services archivés.
	 * @param archive .
	 * @return une liste de services.
	 */
	List<Service> findByArchiveWithOrder(boolean archive);
	
	/**
	 * Recherche les services dont les coordonnées sont passées en paramètre.
	 * @param coordonnee pour lesquelles on recherche des services.
	 * @return une liste de services.
	 */
	List<Service> findByCoordonnee(Coordonnee coordonnee);
	
	/**
	 * Recherche les services dont l'établissement est passé en paramètre.
	 * @param etablissement pour lequel on recherche des services.
	 * @return une liste de services.
	 */
	List<Service> findByEtablissement(Etablissement etablissement);
	
	/**
	 * Recherche les services dont l'établissement est passé en paramètre.
	 * Ces services sont renvoyés ordonnés.
	 * @param etablissement pour lequel on recherche des services.
	 * @return une liste ordonnée de services.
	 */
	List<Service> findByEtablissementWithOrder(Etablissement etablissement);
	
	/**
	 * Recherche les services archivés ou non dont l'établissement est passé 
	 * en paramètre. Ces services sont renvoyés ordonnés.
	 * @param etablissement pour lequel on recherche des services.
	 * @param archive True : renvoie les services archivés.
	 * @return une liste ordonnée de services.
	 */
	List<Service> findByEtablissementArchiveWithOrder(
			Etablissement etablissement, boolean archive);
	
	/**
	 * Recherche les services dont un collaborateur est passé en paramètre.
	 * @param collaborateurId pour lequel on recherche des services.
	 * @return une liste de services.
	 */
	List<Service> findByCollaborateurId(Integer collaborateurId);
	
	/**
	 * Recherche les services dont un collaborateur est passé en paramètre
	 * et qui sont archivés ou non.
	 * @param collaborateurId pour lequel on recherche des services.
	 * @param archive True : renvoie les services archivés.
	 * @return une liste de services.
	 */
	List<Service> findByCollaborateurIdAndArchive(Integer collaborateurId, 
			boolean archive);
	
	/**
	 * Recherche les services dont une banque est passée en paramètre.
	 * @param banqueId pour laquelle on recherche des services.
	 * @return une liste de services.
	 */
	List<Service> findByBanquePossedeesId(Integer banqueId);
	
	/**
	 * Recherche le services dont l'identifiant passé en paramètre.
	 * Les associations avec les tables ETABLISSEMENT et 
	 * COORDONNEE seront chargées
	 * par l'intermédiaire d'un fetch.
	 * @param serviceId est l'identifiant dU service recherché.
	 * @return un service.
	 */
	List<Service> findByIdWithFetch(Integer serviceId);
	
	/**
	 * Recherche tous les services sauf celui dont l'id est passé 
	 * en paramètre.
	 * @param serviceId Identifiant du Service que l'on souhaite
	 * exclure de la liste retournée.
	 * @return une liste de Services.
	 */
	List<Service> findByExcludedId(Integer serviceId);
	
	/**
	 * Compte tous les services rattachés à l'établissement dont l'id est passé
	 * en paramètre.
	 * @param etablissementId Identifiant de l'établissement.
	 * @return une liste de Services.
	 */
	List<Long> findCountByEtablissementId(Integer etabissementId);
	
	/**
	 * Recherche les Services dont la ville
	 * est passée en paramètre.
	 * @param ville Ville pour laquelle on recherche des services.
	 * @return une liste de Services.
	 */
	List<Service> findByVille(String ville);
}
