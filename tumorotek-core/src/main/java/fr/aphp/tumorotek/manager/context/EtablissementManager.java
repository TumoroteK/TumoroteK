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

import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Banque.
 * Interface créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EtablissementManager
{

   /**
    * Recherche un Etablissement dont l'identifiant est passé en paramètre.
    * @param etablissementId Identifiant du Etablissement que l'on recherche.
    * @return Un Etablissement.
    */
   Etablissement findByIdManager(Integer etablissementId);

   /**
    * Recherche tous les Etablissements présents dans la base.
    * @return Liste d'Etablissement.
    */
   List<Etablissement> findAllObjectsManager();

   /**
    * Recherche tous les Etablissements présents dans la base.
    * @return Liste ordonnée d'Etablissement.
    */
   List<Etablissement> findAllObjectsWithOrderManager();

   /**
    * Recherche tous les Etablissements actifs présents dans la base.
    * @return Liste ordonnée d'Etablissement.
    */
   List<Etablissement> findAllActiveObjectsWithOrderManager();

   /**
    * Recherche les services liés à l'établissement passé en paramètre.
    * @param etablissement Etablissement pour lequel on recherche des
    * services.
    * @return Liste de Services.
    */
   Set<Service> getServicesManager(Etablissement etablissement);

   /**
    * Recherche les services liés à l'établissement passé en paramètre.
    * @param etablissement Etablissement pour lequel on recherche des
    * services.
    * @return Liste ordonnéev de Services.
    */
   List<Service> getServicesWithOrderManager(Etablissement etablissement);

   /**
    * Recherche les services actifs liés à l'établissement passé en
    * paramètre.
    * @param etablissement Etablissement pour lequel on recherche des
    * services.
    * @return Liste ordonnéev de Services.
    */
   List<Service> getActiveServicesWithOrderManager(Etablissement etablissement);

   /**
    * Recherche les collaborateurs liés à l'établissement passé en paramètre.
    * @param etablissement Etablissement pour lequel on recherche des
    * collaborateurs.
    * @return Liste de Collaborateurs.
    */
   Set<Collaborateur> getCollaborateursManager(Etablissement etablissement);

   /**
    * Recherche les collaborateurs liés à l'établissement passé en paramètre.
    * @param etablissement Etablissement pour lequel on recherche des
    * collaborateurs.
    * @return Liste ordonnée de Collaborateurs.
    */
   List<Collaborateur> getCollaborateursWithOrderManager(Etablissement etablissement);

   /**
    * Recherche les collaborateurs non archivés liés à l'établissement
    * passé en paramètre.
    * @param etablissement Etablissement pour lequel on recherche des
    * collaborateurs.
    * @return Liste ordonnée de Collaborateurs.
    */
   List<Collaborateur> getActiveCollaborateursWithOrderManager(Etablissement etablissement);

   /**
    * Recherche une liste d'Etablissement dont le nom commence comme
    * celui passé en paramètre.
    * @param nom Nom pour lequel on recherche des Etablissements.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Etablissements.
    */
   List<Etablissement> findByNomLikeManager(String nom, boolean exactMatch);

   /**
    * Recherche une liste d'Etablissement dont le nom contient
    * celui passé en paramètre.
    * @param nom Nom pour lequel on recherche des Etablissements.
    * @return Liste d'Etablissements.
    */
   List<Etablissement> findByNomLikeBothSideManager(String nom);

   /**
    * Recherche une liste d'Etablissement dont la ville contient
    * le nom passé en paramètre.
    * @param ville Ville pour laquelle on recherche des Etablissements.
    * @return Liste d'Etablissements.
    */
   List<Etablissement> findByVilleLikeManager(String ville);

   /**
    * Recherche une liste d'Etablissement dont le finess commence comme
    * celui passé en paramètre.
    * @param finess Finess pour lequel on recherche des Etablissements.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Etablissements.
    */
   List<Etablissement> findByFinessLikeManager(String finess, boolean exactMatch);

   /**
    * Recherche les doublons de l'Etablissement passé en paramètre.
    * @param etablissement Etablissement pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Etablissement etablissement);

   /**
    * Persist une instance de Etablissement dans la base de données.
    * @param etablissement Nouvelle instance de l'objet à créer.
    * @param coordonnee Coordonnee associée à l'Etablissement.
    * @param categorie Categorie associée.
    */
   void createObjectManager(Etablissement etablissement, Coordonnee coordonnee, Categorie categorie, Utilisateur utilisateur);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param etablissement Objet à mettre à jour dans la base.
    * @param coordonnee Coordonnee associée à l'Etablissement.
    * @param categorie Categorie associée.
    * @param cascadeArchive Si true, cascade l'archivage sur les services.
    */
   void updateObjectManager(Etablissement etablissement, Coordonnee coordonnee, Categorie categorie, Utilisateur utilisateur,
      boolean cascadeArchive);

   /**
    * Supprime un Etablissement de la base de données.
    * @param etablissement Etablissement à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @throws ObjectUsedException si etablissement reference des services
    * ou des collaborateurs isolés.
    * @throws ObjectReferencedException si etablissement est référencé par des
    * contrats.
    */
   void removeObjectManager(Etablissement etablissement, String comments, Utilisateur user);

   /**
    * Verifie si l'etablissement contient des services ou des
    * collborateurs.
    * @param et Etablisement
    * @return boolean true si l'etablissement a des enfants.
    */
   boolean isUsedObjectManager(Etablissement et);

   /**
    * Verifie si l'etablissement est référencé par d'autres objets
    * (contrats)
    * et donc ne peut être supprimé (peut être inactivé en attendant).
    * @param et Etablisement
    * @return boolean true si l'établissement est référencé
    * par au moins un objet.
    */
   boolean isReferencedObjectManager(Etablissement et);

   /**
    * Supprime un objet de la base de données et en cascade tous les objets
    * dont il est le parent, deletion cascadant à leur tour sur les objets
    * en descendant la hierarchie.
    * @param etablissement Etablissement à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void removeObjectCascadeManager(Etablissement etablissement, String comments, Utilisateur user);

   /**
    * Fusionne deux Etablissements enregistrés dans le système.
    * Le premier etablissement passé en paramètre est celui qui sera conservé
    * dans le système et qui recevra les associations attribuées au
    * etablissement passif passé en deuxième paramètre.
    * Association à récupérer:<br>
    *		-Collaborateur
    *		-Contrat
    *		-Service
    * @param idActif celui qui restera
    * @param idPassif celui qui sera supprimé
    */
   void fusionEtablissementManager(int idActif, int idPassif, String comments, Utilisateur user);

}
