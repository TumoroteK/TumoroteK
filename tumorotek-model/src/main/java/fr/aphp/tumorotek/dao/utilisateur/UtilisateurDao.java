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
package fr.aphp.tumorotek.dao.utilisateur;

import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine Utilisateur.
 *
 * @author Pierre Ventadour
 * @version 10/09/2009
 *
 */
public interface UtilisateurDao extends GenericDaoJpa<Utilisateur, Integer>
{

   /**
    * Recherche les utilisateurs en les ordonnants par login.
    * @return une liste d'utilisateurs ordonnée.
    */
   List<Utilisateur> findByOrder();

   /**
    * Recherche les utilisateurs dont le login est égal au paramètre.
    * @param login Login pour lequel on recherche des utilisateurs.
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByLogin(String login);

   /**
    * Recherche les utilisateurs dont le login est égal au paramètre.
    * @param login Login pour lequel on recherche des utilisateurs.
    * @param boolean archive ou non
    * @param pfs plateformes d'origine
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByLoginAndArchive(String login, boolean archive, List<Plateforme> pfs);

   /**
    * Recherche les utilisateurs archivés.
    * @param archive True ou false.
    * @param pfs plateformes d'origine
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByArchive(boolean archive, List<Plateforme> pfs);

   /**
    * Recherche les utilisateurs dont le dn_ldap est égal au paramètre.
    * @param dnLdap Login dans l'annuaire LDAP.
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findBydnLdap(String dnLdap);

   /**
    * Recherche les utilisateurs dont l'email est égal au paramètre.
    * @param email Email de l'utilisateur.
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByEmail(String email);

   /**
    * Recherche les utilisateurs dont le timeout est passé en paramètre.
    * @param timeOut Date.
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByTimeOut(Date timeOut);

   /**
    * Recherche les utilisateurs non archivés dont le timeout avant celui 
    * passé en paramètre.
    * @param timeOut Date.
    * @return une liste d'utilisateurs.
    * @version 2.0.13
    */
   List<Utilisateur> findByTimeOutBefore(Date timeOut);

   /**
    * Recherche les utilisateurs dont le timeout après celui 
    * passé en paramètre.
    * @param timeOut Date.
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByTimeOutAfter(Date timeOut);

   /**
    * Recherche les utilisateurs dont le collaborateur est passé en paramètre.
    * @param Collaborateur pour lequel on recherche des utilisateurs.
    * @return une liste de utilisateurs.
    */
   List<Utilisateur> findByCollaborateur(Collaborateur collaborateur);

   /**
    * Recherche les utilisateurs dont une réservation est passée en paramètre.
    * @param reservationId Clé primaire de la réservation pour laquelle on 
    * recherche des utilisateurs.
    * @return une liste de utilisateurs.
    */
   List<Utilisateur> findByReservationId(Integer reservationId);

   /**
    * Recherche l'utilisateur dont l'identifiant passé en paramètre.
    * L'association avec la table COLLABORATEUR sera chargée par 
    * l'intermédiaire d'un fetch.
    * @param utilisateurId Identifiant de l'utilisateur recherché.
    * @return un utilisateur.
    */
   List<Utilisateur> findByIdWithFetch(Integer utilisateurId);

   /**
    * Recherche les utilisateurs dont l'identifiant est différent
    * de celui passé en paramètre.
    * @param utilisateurId Identifiant de l'utilisateur à exclure.
    * @return un utilisateur.
    */
   List<Utilisateur> findByExcludedId(Integer utilisateurId);

   /**
    * Recherche les utilisateurs archivés.
    * @param archive True ou false.
    * @param pfs plateformes d'origine
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByOrderWithArchive(boolean archive, List<Plateforme> pfs);

   /**
    * Recherche les utilisateurs dont le login et le mdp sont égaux 
    * aux paramètres.
    * @param login Login pour lequel on recherche des utilisateurs.
    * @param password Mdp pour lequel on recherche des utilisateurs.
    * @return une liste d'utilisateurs.
    */
   List<Utilisateur> findByLoginPassAndArchive(String login, String password, boolean archive);

}
