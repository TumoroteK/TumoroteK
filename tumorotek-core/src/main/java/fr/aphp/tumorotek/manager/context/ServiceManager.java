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
package fr.aphp.tumorotek.manager.context;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le manager du bean de domaine Service.
 * Interface créée le 01/10/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ServiceManager {
	
	/**
	 * Recherche un Service dont l'identifiant est passé en paramètre.
	 * @param serviceId Identifiant du Service que l'on recherche.
	 * @return Un Service.
	 */
	Service findByIdManager(Integer serviceId);
	
	/**
	 * Recherche tous les Services présents dans la base.
	 * @return Liste de Services.
	 */
	List<Service> findAllObjectsManager();
	
	/**
	 * Recherche tous les Services présents dans la base.
	 * @return Liste ordonnée de Services.
	 */
	List<Service> findAllObjectsWithOrderManager();
	
	/**
	 * Recherche tous les Services présents dans la base.
	 * @return Liste ordonnée de Services.
	 */
	List<Service> findAllActiveObjectsWithOrderManager();
	
	/**
	 * Compte les services affectés à un établissement passé en param.
	 * @return Liste de Service.
	 */
	Long findCountByEtablissementIdManager(Etablissement etablissement);
	
	/**
	 * Recherche les collaborateurs liés au service passé en paramètre.
	 * @param service Service pour lequel on recherche des
	 * collaborateurs.
	 * @return Liste de Collaborateurs.
	 */
	Set<Collaborateur> getCollaborateursManager(Service service);
	
	/**
	 * Recherche les collaborateurs liés au service passé en paramètre.
	 * @param service Service pour lequel on recherche des
	 * collaborateurs.
	 * @return Liste ordonnée de Collaborateurs.
	 */
	List<Collaborateur> getCollaborateursWithOrderManager(Service service);
	
	/**
	 * Recherche les collaborateurs non archivés liés au service passé 
	 * en paramètre.
	 * @param service Service pour lequel on recherche des
	 * collaborateurs.
	 * @return Liste ordonnée de Collaborateurs.
	 */
	List<Collaborateur> getActiveCollaborateursWithOrderManager(
			Service service);
	
	/**
	 * Recherche une liste de Services dont le nom commence comme
	 * celui passé en paramètre.
	 * @param nom Nom pour lequel on recherche des Services.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste de Services.
	 */
	List<Service> findByNomLikeManager(String nom, boolean exactMatch);
	
	/**
	 * Recherche une liste de Services dont le nom contient
	 * celui passé en paramètre.
	 * @param nom Nom pour lequel on recherche des Services.
	 * @return Liste de Services.
	 */
	List<Service> findByNomLikeBothSideManager(String nom);
	
	/**
	 * Recherche les doublons du Service passé en paramètre.
	 * @param service Service pour lequel on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonManager(Service service);
	
	/**
	 * Persist une instance de Service dans la base de données.
	 * @param service Nouvelle instance de l'objet à créer.
	 * @param coordonnee Coordonnee associée au service.
	 * @param etablissement Etablissement associé.
	 * @param collaborateurs Collaborateurs associés au service.
	 * @param cascadeArchive Si true, cascade l'archivage sur les collabs.
	 */
	void createObjectManager(Service service, Coordonnee coordonnee,
			Etablissement etablissement, List<Collaborateur> collaborateurs,
			Utilisateur utilisateur,
			boolean cascadeArchive);
	
	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * @param service Objet à mettre à jour dans la base.
	 * @param coordonnee Coordonnee associée au service.
	 * @param etablissement Etablissement associé.
	 * @param collaborateurs Collaborateurs associés au service.
	 * @param cascadeArchive Si true, cascade l'archivage sur les collabs.
	 * @param doValidation Si false, ne fait pas la validation des
	 * champs du collaborateur.
	 */
	void updateObjectManager(Service service, Coordonnee coordonnee,
			Etablissement etablissement, List<Collaborateur> collaborateurs,
			Utilisateur utilisateur,
			boolean cascadeArchive, boolean doValidation);
	
	/**
	 * Verifie si le service contient des 
	 * collborateurs. 
	 * @param srv Service
	 * @return boolean true si le service a des enfants.
	 */
	boolean isUsedObjectManager(Service srv);

	/**
	 * Verifie si le service est référencé par d'autres objets 
	 * (banques, prelevements, laboInters, cessions, contrats, conteneurs)
	 * et donc ne peut être supprimé (peut être inactivé en attendant).
	 * @param src Service
	 * @return boolean true si le service est référencé par au moins un objet.
	 */
	boolean isReferencedObjectManager(Service srv);
	
	/**
	 * Supprime un Service de la base de données.
	 * @param service Service à supprimer de la base de données.
	 * @param comments commentaires liés à la suppression
	 * @param Utilisateur réalisant la suppression.
	 * @throws ObjectUsedException si le service reference des derives
	 * ou est référencé par des cessions.
	 * @throws ObjectReferencedException si service est référencé par des
	 * objets.
	 */
	void removeObjectManager(Service service, String comments,
														Utilisateur user);

	/**
	 * Supprime un objet de la base de données et en cascade tous les objets 
	 * dont il est le parent, deletion cascadant à leur tour sur les objets 
	 * en descendant la hierarchie. 
	 * @param service Service à supprimer de la base de données.
	 * @param comments commentaires liés à la suppression
	 * @param Utilisateur réalisant la suppression.
	 */
	void removeObjectCascadeManager(Service srv, String comments,
														Utilisateur user);
	
	/**
	 * Fusionne deux Services enregistrés dans le système.
	 * Le premier service passé en paramètre est celui qui sera conservé 
	 * dans le système et qui recevra les associations attribuées au 
	 * service passif passé en deuxième paramètre.
	 * Association à récupérer:<br>
	 * 		-Banque
  	 *		-Cession
  	 *		-Conteneur
	 *		-Contrat
	 *		-Labo_Inter
	 *		-Prelevement (Preleveur_ID)
	 *		-Service_Collaborateur
	 * @param idActif celui qui restera
	 * @param idPassif celui qui sera supprimé
	 */
	void fusionServiceManager(int idActif, int idPassif, String comments,
			Utilisateur user);
	
	/**
	 * Recherche une liste de Service dont la ville contient
	 * le nom passé en paramètre.
	 * @param nom de la Ville pour laquelle on recherche des Services.
	 * @return Liste de Service.
	 */
	List<Service> findByVilleLikeManager(String ville);
}
