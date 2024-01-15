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
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Collaborateur.
 * Interface créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface CollaborateurManager
{

   /**
    * Recherche un Collaborateur dont l'identifiant est passé en paramètre.
    * @param collaborateurId Identifiant du Collaborateur que l'on recherche.
    * @return Un Collaborateur.
    */
   Collaborateur findByIdManager(Integer collaborateurId);

   /**
    * Recherche tous les collaborateur présents dans la base.
    * @return Liste de Collaborateur.
    */
   List<Collaborateur> findAllObjectsManager();

   /**
    * Recherche tous les collaborateur présents dans la base.
    * @return Liste ordonnée de Collaborateur.
    */
   List<Collaborateur> findAllObjectsWithOrderManager();

   /**
    * Recherche tous les collaborateur présents dans la base.
    * @return Liste ordonnée de Collaborateur.
    */
   List<Collaborateur> findAllActiveObjectsWithOrderManager();

   /**
    * Recherche tous les collaborateurs présents dans la base qui
    * n'ont pas de service.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findAllObjectsWithoutService();

   /**
    * Recherche tous les collaborateurs présents dans la base qui
    * n'ont pas de service.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findAllActiveObjectsWithoutService();

   /**
    * Recherche les services liés au collaborateur passé en paramètre.
    * @param collaborateur Collaborateur pour lequel on recherche des
    * services.
    * @return Liste de Services.
    */
   Set<Service> getServicesManager(Collaborateur collaborateur);

   /**
    * Recherche les collaborateurs liés au service passé en paramètre
    * et qui sont archivés ou non.
    * @param service Service pour lequel on recherche des collaborateurs.
    * @param archive True si le collaborateur doit etre archivé.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findByServicesAndArchiveManager(Service service, boolean archive);
   
   /**
    * filtre les collaborateurs passés en paramètre pour ne garder que ceux du service voulu
    * @param service
    * @param collaborateurs
    * @return
    */
   List<Collaborateur> filterCollaborateursByServiceWithOrder(final Service service, final List<Collaborateur> collaborateurs);

   /**
    * Recherche les coordonnées liées au collaborateur passé en paramètre.
    * @param collaborateur Collaborateur pour lequel on recherche des
    * services.
    * @return Liste de Coordonnee.
    */
   Set<Coordonnee> getCoordonneesManager(Collaborateur collaborateur);

   /**
    * Recherche une liste de Collaborateurs dont le nom commence comme
    * celui passé en paramètre.
    * @param nom Nom pour lequel on recherche des Collaborateurs.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findByNomLikeManager(String nom, boolean exactMatch);

   /**
    * Recherche une liste de Collaborateurs dont le nom contient
    * celui passé en paramètre.
    * @param nom Nom pour lequel on recherche des Collaborateurs.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findByNomLikeBothSideManager(String nom);

   /**
    * Recherche une liste de Collaborateurs dont le prénom commence comme
    * celui passé en paramètre.
    * @param prenom Prénom pour lequel on recherche des Collaborateurs.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findByPrenomLikeManager(String prenom, boolean exactMatch);

   /**
    * Recherche une liste de Collaborateurs dont la spécialité est
    * passée en paramètre.
    * @param specialite Specialite pour laquelle on recherche des collabs.
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findBySpecialiteManager(Specialite specialite);

   /**
    * Recherche une liste de Collaborateurs dont le service est
    * passée en paramètre.
    * @param sercice Service pour lequel on recherche des collabs.
    * @return Liste de Collaborateurs.
    */
   Long findCountByServicedIdManager(Service service);

   /**
    * Recherche une liste de Collaborateurs dont l'etablissement est
    * passée en paramètre.
    * @param sercice Service pour lequel on recherche des collabs.
    * @return Liste de Collaborateurs.
    */
   Long findCountByEtablissementManager(Etablissement etab);

   /**
    * Recherche les doublons du Collaborateur passé en paramètre.
    * @param collaborateur Collaborateur pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Collaborateur collaborateur);

   /**
    * Test si un collaborateur est archivable : tous ses services, sauf celui
    * en paramètre, sont archivés.
    * @param collaborateur Collaborateur.
    * @param service Service.
    * @return True Si le collab peut être archivé.
    */
   Boolean isArchivableManager(Collaborateur collaborateur, Service service);

   /**
    * Persist une instance de Collaborateur dans la base de données.
    * @param collaborateur Nouvelle instance de l'objet à créer.
    * @param titre Titre associé au collaborateur.
    * @param etablissement Etablissement associé.
    * @param specialite Specialite du collaborateur.
    * @param services Liste des services associés au collaborateur.
    * @param coordonnees Liste de coord associées au collaborateur.
    */
   void createObjectManager(Collaborateur collaborateur, Titre titre, Etablissement etablissement, Specialite specialite,
      List<Service> services, List<Coordonnee> coordonnees, Utilisateur utilisateur);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param collaborateur Objet à mettre à jour dans la base.
    * @param titre Titre associé au collaborateur.
    * @param etablissement Etablissement associé.
    * @param specialite Specialite du collaborateur.
    * @param services Liste des services associés au collaborateur.
    * @param coordonnees Liste de coord associées au collaborateur.
    * @param doValidation Si false, ne fait pas la validation des
    * champs du collaborateur.
    * @throws javax.xml.bind.ValidationException
    */
   void updateObjectManager(Collaborateur collaborateur, Titre titre, Etablissement etablissement, Specialite specialite,
      List<Service> services, List<Coordonnee> coordonnees, Utilisateur utilisateur, boolean doValidation);

   /**
    * Supprime un Collaborateur de la base de données.
    * @param collab Collaborateur à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @throws ObjectReferencedException si collaborateur est référencé par des
    * objets.
    */
   void removeObjectManager(Collaborateur collab, String comments, Utilisateur user);

   /**
    * Verifie si le collaborateur est référencé par d'autres objets du système
    * et donc ne peut être supprimé (peut être inactivé en attendant).
    * @param collab Collaborateur
    * @return boolean true si le collaborateur est référencé
    * par au moins un objet.
    */
   boolean isReferencedObjectManager(Collaborateur et);

   /**
    * Supprime un collaborateyr de la base de données
    * en terminale d'une cascade
    * initié par au niveau du service ou de l'établissement.
    * Si le collaborateur appartient à plusieurs services, ne supprime que
    * que la référence du service cascadant.
    * @param collaborateur Collaborateur à supprimer de la base de données.
    * @param service Service cascadant
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void removeObjectCascadeManager(Collaborateur collab, Service service, String comments, Utilisateur user);

   /**
    * Recherche tous les collaborateurs présents dans la base qui
    * n'ont pas de service pour un établissement donnée.
    * @param etablissement
    * @return Liste de Collaborateurs.
    */
   List<Collaborateur> findByEtablissementNoServiceManager(Etablissement etab);

   /**
    * Fusionne deux Collaborateurs enregistrés dans le système.
    * Le premier collaborateur passé en paramètre est celui qui sera conservé
    * dans le système et qui recevra les associations attribuées au
    * collaborateur passif passé en deuxième paramètre.
    * Association à récupérer:<br>
    * 		-Banque
    *		-Collaborateur_coordonnee
    *		-Contrat
    *		-Echantillon
    *		-Labo_Inter
    *		-Maladie_Medecin
    *		-Patient_Medecin
    *		-Plateforme
    *		-Prod_Derive
    *		-Retour
    *		-Service_Collaborateur
    *		-Utilisateur
    *		-Cession (Demandeur_ID)
    *		-Prelevement (Preleveur_ID)
    * @param cActif celui qui restera
    * @param cPassif celui qui sera supprimé
    */
   void fusionCollaborateurManager(int idActif, int idPassif, String comments, Utilisateur user);

   /**
    * Recherche une liste de Collaborateur dont la ville contient
    * le nom passé en paramètre.
    * @param ville Ville pour laquelle on recherche des Collaborateurs.
    * @return Liste de Collaborateur.
    */
   List<Collaborateur> findByVilleLikeManager(String ville);

}
