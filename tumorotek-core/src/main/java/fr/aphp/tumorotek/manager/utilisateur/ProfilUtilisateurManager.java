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
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateurPK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine ProfilUtilisateur.
 * Interface créée le 19/05/2010.
 *
 * @author Pierre Ventadour
 * @version TK-305
 *
 */
public interface ProfilUtilisateurManager
{

   /**
    * Recherche un ProfilUtilisateur dont l'identifiant est 
    * passé en paramètre.
    * @param pk Identifiant du ProfilUtilisateur que l'on recherche.
    * @return Un ProfilUtilisateur.
    */
   ProfilUtilisateur findByIdManager(ProfilUtilisateurPK pk);

   /**
    * Recherche tous les ProfilUtilisateurs présents dans la base.
    * @return Liste de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findAllObjectsManager();

   /**
    * Recherche les ProfilUtilisateurs sauf celui dont la clé 
    * primaire est passée en paramètre.
    * @param pk ProfilUtilisateurPK.
    * @return Liste de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByExcludedPKManager(ProfilUtilisateurPK pk);

   /**
    * Recherche les ProfilUtilisateurs dont le Profil est égal au 
    * paramètre, en filtrant sur les utilisateurs archivés. Renvoie tous 
    * les profils utilisateurs si archive = null;
    * @param profil Profil des ProfilUtilisateurs recherchés.
    * @param archive utilisateur
    * @return une liste de ProfilUtilisateurs.
    * @version 2.1
    */
   List<ProfilUtilisateur> findByProfilManager(Profil profil, Boolean archive);

   /**
    * Recherche les ProfilUtilisateurs dont l'Utilisateur est égal au 
    * paramètre.
    * @param utilisateur Utilisateur des ProfilUtilisateurs recherchés.
    * @param archive filtre statut archivé banque
    * @return une liste de ProfilUtilisateurs.
    * @version 2.1
    */
   List<ProfilUtilisateur> findByUtilisateurManager(Utilisateur utilisateur, Boolean archive);

   /**
    * Recherche les ProfilUtilisateurs dont la Banque est égale au 
    * paramètre.
    * @param banque Banque des ProfilUtilisateurs recherchés.
    * @param Boolean utilisateur archivé
    * @return une liste de ProfilUtilisateurs.
    * @version 2.1
    */
   List<ProfilUtilisateur> findByBanqueManager(Banque banque, Boolean archive);

   /**
    * Recherche tous les ProfilUtilisateurs pour un couple de valeurs Profil
    * et utilisateur.
    * @param utilisateur Utilisateur des ProfilUtilisateurs recherchés.
    * @param profil Profil des ProfilUtilisateurs recherchés.
    * @return Liste ordonnée de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByUtilisateurProfilManager(Utilisateur utilisateur, Profil profil);

   /**
    * Recherche tous les ProfilUtilisateurs pour un couple de valeurs Banque
    * et utilisateur.
    * @param utilisateur Utilisateur des ProfilUtilisateurs recherchés.
    * @param banque Banque des ProfilUtilisateurs recherchés.
    * @return Liste ordonnée de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByUtilisateurBanqueManager(Utilisateur utilisateur, Banque banque);

   /**
    * Recherche tous les ProfilUtilisateurs pour un couple de valeurs Profil
    * et Banque.
    * @param banque Banque des ProfilUtilisateurs recherchés.
    * @param profil Profil des ProfilUtilisateurs recherchés.
    * @return Liste ordonnée de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByBanqueProfilManager(Banque banque, Profil profil);

   /**
    * Recherche les doublons pour un ProfilUtilisateur.
    * @param utilisateur Utilisateur du ProfilUtilisateur.
    * @param banque Banque  du ProfilUtilisateur.
    * @param profil Profil du ProfilUtilisateur.
    * @return True si le ProfilUtilisateur existe déjà.
    */
   Boolean findDoublonManager(Utilisateur utilisateur, Banque banque, Profil profil);

   /**
    * Valide l'objet formé par les associations en paramètres.
    * @param utilisateur Utilisateur du ProfilUtilisateur.
    * @param banque Banque  du ProfilUtilisateur.
    * @param profil Profil du ProfilUtilisateur.
    */
   void validateObjectManager(Utilisateur utilisateur, Banque banque, Profil profil);

   /**
    * Persist une instance de ProfilUtilisateur dans la base de données.
    * @param profilUtilisateur ProfilUtilisateur à créer.
    * @param utilisateur Utilisateur du ProfilUtilisateur.
    * @param banque Banque  du ProfilUtilisateur.
    * @param profil Profil du ProfilUtilisateur.
    */
   void saveManager(ProfilUtilisateur profilUtilisateur, Utilisateur utilisateur, Banque banque, Profil profil);

   /**
    * Supprime un ProfilUtilisateur de la base de données.
    * @param profilUtilisateur ProfilUtilisateur à supprimer 
    * de la base de données.
    */
   void deleteByIdManager(ProfilUtilisateur profilUtilisateur);
   
   /**
    * Compte pour un utilisateur le nombre de profils différents qui lui sont attribués 
    * pour accéder aux banques de chaque contexte, pour une plateforme donnée.
    * Cette méthode permet de visualiser rapidement, si le nombre de profils est supérieur à 1,
    * si l'utilisateur pourra accéder en mode 'toutes collections'.
    * @param u utilisateur
    * @param p plateforme
    * @return nombre de profils d'accès distincts
    * @since TK-305
    */
   Long countDistinctProfilForUserAndPlateformeGroupedByContexteManager(Utilisateur u, Plateforme p);
}