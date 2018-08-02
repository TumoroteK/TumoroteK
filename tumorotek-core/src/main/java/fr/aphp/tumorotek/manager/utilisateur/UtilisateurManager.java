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
package fr.aphp.tumorotek.manager.utilisateur;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Utilisateur.
 * Interface créée le 04/11/09.
 *
 * @since 2.0.10 Ajout du paramètre plateformes dans les
 * requêtes pour créer et filtrer les comptes utilisateur en fonction de
 * leur origine
 *
 * @since 2.0.13 AJout méthode d'archive une liste utilisateurs dont le tiemout a expiré et
 * logs INFO ces opérations
 *
 * @since 2.1 ajout toutes les pfs getPlateformesAvailableManager au superAdmin
 *
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface UtilisateurManager
{

   /**
    * Recherche un Utilisateur dont l'identifiant est passé en paramètre.
    * @param utilisateurId Identifiant de l'Utilisateur que l'on recherche.
    * @return Un Utilisateur.
    */
   Utilisateur findByIdManager(Integer utilisateurId);

   /**
    * Recherche un Utilisateur dont le login est passé en paramètre.
    * @param login Login de l'Utilisateur que l'on recherche.
    * @return Un Utilisateur.
    */
   List<Utilisateur> findByLoginManager(String login);

   /**
    * Recherche un Utilisateur dont le login est passé en paramètre.
    * @param login Login de l'Utilisateur que l'on recherche.
    * @param pfs liste des plateformes d'origine d'un utilisateur.
    * @return Un Utilisateur.
    */
   List<Utilisateur> findByLoginAndArchiveManager(String login, boolean archive, List<Plateforme> pfs);

   /**
    * Recherche tous les Utilisateur présents dans la base.
    * @return Liste de Utilisateur.
    */
   List<Utilisateur> findAllObjectsManager();

   /**
    * Recherche les utilisateurs actifs ou non.
    * @param archive True si l'utilisateur n'est pas actif.
    * @param pfs liste des plateformes d'origine d'un utilisateur.
    * @return Liste d'utilisateurs.
    */
   List<Utilisateur> findByArchiveManager(boolean archive, List<Plateforme> pfs);
   
   /**
    * Recherche les utilisateurs actifs ou non.
    * @param archive True si l'utilisateur n'est pas actif.
    * @param pfs liste des plateformes d'origine d'un utilisateur.
    * @param includeSuperAdmin inclus les super-utilisateurs si <i>true</i>
    * @return Liste d'utilisateurs.
    */
   List<Utilisateur> findByArchiveManager(boolean archive, List<Plateforme> pfs, Boolean includeSuperAdmin);

   /**
    * Recherche les utilisateurs par login, mdp et archive.
    * @param login
    * @param pass
    * @param archive True si l'utilisateur n'est pas actif.
    * @return Liste d'utilisateurs.
    */
   List<Utilisateur> findByLoginPasswordAndArchiveManager(String login, String pass, boolean archive);

   /**
    * Recherche les doublons de l'Utilisateur passé en paramètre.
    * @param utilisateur Un Utilisateur pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Utilisateur utilisateur);

   /**
    * Teste si l'utilisateur est utilisé : au moins une opération lui est
    * associée.
    * @param utilisateur Utilisateur à tester.
    * @return True s'il est utilisé.
    */
   Boolean isUsedObjectManager(Utilisateur utilisateur);

   /**
    * Récupère la liste des plateformes pour lesquels l'utilisateur est
    * admin.
    * @param utilisateur Utilisateur.
    * @return Set de plateformes.
    */
   Set<Plateforme> getPlateformesManager(Utilisateur utilisateur);

   /**
    * Retourne toutes les banques auxquelles l'utilisateur a accès.
    * @param utilisateur Utilisateur.
    * @return Liste de banques.
    */
   List<Banque> getAvailableBanquesManager(Utilisateur utilisateur);

   /**
    * Retourne toutes les banques auxquelles l'utilisateur a accès
    * en tant qu'administrateur.
    * @param utilisateur Utilisateur.
    * @return Liste de banques.
    */
   List<Banque> getAvailableBanquesAsAdminManager(Utilisateur utilisateur);

   /**
    * Retourne toutes les plateformes auxquelles l'utilisateur a accès.
    * @param utilisateur Utilisateur.
    * @return Liste de platformes.
    * @version 2.1
    */
   List<Plateforme> getAvailablePlateformesManager(Utilisateur utilisateur);

   /**
    * Retourne toutes les banques auxquelles l'utilisateur a accès
    * en fonction d'une plateforme.
    * @param utilisateur Utilisateur.
    * @param plateforme Plateforme.
    * @return Liste de banques.
    */
   List<Banque> getAvailableBanquesByPlateformeManager(Utilisateur utilisateur, Plateforme plateforme);

   /**
    * Persist une instance d'Utilisateur dans la base de données.
    * @param utilisateur Nouvelle instance de l'objet à créer.
    * @param collaborateur Collaborateur
    * @param profils Liste de ProfilUtilisateur.
    * @param plateformes Liste de Plateforme.
    * @param admin Utilisateur créant le nouveau compte.
    * @param plateforme origine du nouveau compte.
    */
   void createObjectManager(Utilisateur utilisateur, Collaborateur collaborateur, List<ProfilUtilisateur> profils,
      List<Plateforme> plateformes, Utilisateur admin, Plateforme origine);

   /**
    * Persist une instance d'Utilisateur dans la base de données.
    * @param utilisateur Instance de l'objet à maj.
    * @param collaborateur Collaborateur
    * @param profils Liste de ProfilUtilisateur.
    * @param plateformes Liste de Plateforme.
    * @param admin Utilisateur modifiant le compte.
    */
   void updateObjectManager(Utilisateur utilisateur, Collaborateur collaborateur, List<ProfilUtilisateur> profils,
      List<Plateforme> plateformes, Utilisateur admin, OperationType oType);

   /**
    * Modifie en base le mot de passe de l'utilisateur et augmente
    * la durée de validité de son MDP.
    * @param utilisateur User dont on veut changer le pwd.
    * @param newPwd Nouveau mot de passe.
    * @param nbMois Nombre de mois à ajouter à la validité du mdp.
    * @param admin User réalisant l'opréation.
    * @return L'utilisateur mis à jour.
    */
   Utilisateur updatePasswordManager(Utilisateur utilisateur, String newPwd, Integer nbMois, Utilisateur admin);

   /**
    * Archive l'utilisateur passé en paramètres.
    * @param utilisateur Utilisateur à archiver.
    * @param admin User réalisant l'opréation.
    */
   void archiveUtilisateurManager(Utilisateur utilisateur, Utilisateur admin);

   /**
    * Supprime un Utilisateur de la base de données.
    * @param utilisateur Utilisateur à supprimer de la base de données.
    */
   void removeObjectManager(Utilisateur utilisateur);

   /**
    * Archive les utilisateurs pas encore archivés, dont le timeout a expiré.
    * Enregistre une trace dans la table OPERATION et log INFO.
    * Cette méthode sera appelée au niveau de la webapp par le Spring Scheduler, 
    * toutes les nuits à 00:00 heures. 
    * @since 2.0.13
    * @param admin Utilisateur associée à la trace de l'archivage dans OPERATION
    */
   void archiveScheduledUtilisateursManager(Utilisateur admin);

}
