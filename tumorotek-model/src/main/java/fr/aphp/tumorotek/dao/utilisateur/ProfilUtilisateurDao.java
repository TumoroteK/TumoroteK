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

import java.util.List;
import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilByEtudeCount;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateurPK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine ProfilUtilisateur.
 * Interface créée le 18/05/2010.
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 * 
 * @version 2.3.0-gatsbi
 *
 */
public interface ProfilUtilisateurDao extends GenericDaoJpa<ProfilUtilisateur, ProfilUtilisateurPK>
{

   /**
    * Recherche les ProfilUtilisateurs sauf celui dont la clé 
    * primaire est passée en paramètre.
    * @param pk ProfilUtilisateurPK.
    * @return une liste de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByExcludedPK(ProfilUtilisateurPK pk);

   /**
    * Recherche les ProfilUtilisateurs dont le Profil est égal au 
    * paramètre.
    * @param profil Profil des ProfilUtilisateurs recherchés.
    * @return une liste de ProfilUtilisateurs.
    * @version 2.1
    */
   List<ProfilUtilisateur> findByProfil(Profil profil, boolean archive);

   /**
    * Recherche les ProfilUtilisateurs dont l'Utilisateur est égal au 
    * paramètre.
    * @param utilisateur Utilisateur des ProfilUtilisateurs recherchés.
    * @param filtre statut archivé des banques
    * @return une liste de ProfilUtilisateurs.
    * @version 2.1
    */
   List<ProfilUtilisateur> findByUtilisateur(Utilisateur utilisateur, boolean archive);

   /**
    * Recherche les ProfilUtilisateurs dont la Banque est égale au 
    * paramètre.
    * @param banque Banque des ProfilUtilisateurs recherchés.
    * @param Boolean utilisateur archivé oui/non 
    * @return une liste de ProfilUtilisateurs.
    * @version 2.1
    */
   List<ProfilUtilisateur> findByBanque(Banque banque, Boolean archive);

   /**
    * Recherche tous les ProfilUtilisateurs pour un couple de valeurs Profil
    * et utilisateur.
    * @param utilisateur Utilisateur des ProfilUtilisateurs recherchés.
    * @param profil Profil des ProfilUtilisateurs recherchés.
    * @return Liste ordonnée de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByUtilisateurProfil(Utilisateur utilisateur, Profil profil);

   /**
    * Recherche tous les ProfilUtilisateurs pour un couple de valeurs Banque
    * et utilisateur.
    * @param utilisateur Utilisateur des ProfilUtilisateurs recherchés.
    * @param banque Banque des ProfilUtilisateurs recherchés.
    * @return Liste ordonnée de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByUtilisateurBanque(Utilisateur utilisateur, Banque banque);

   /**
    * Recherche tous les ProfilUtilisateurs pour un couple de valeurs Profil
    * et Banque.
    * @param banque Banque des ProfilUtilisateurs recherchés.
    * @param profil Profil des ProfilUtilisateurs recherchés.
    * @return Liste ordonnée de ProfilUtilisateurs.
    */
   List<ProfilUtilisateur> findByBanqueProfil(Banque banque, Profil profil);
   
   /**
    * Compte pour un utilisateur le nombre de profils différents qui lui sont attribués 
    * pour accéder aux banques (de contexte non GATSBI) pour une plateforme donnée.
    * Cette méthode permet de visualiser rapidement, si le nombre de profils est supérieur à 1,
    * si l'utilisateur pourra accéder en mode 'toutes collections'.
    * @param u utilisateur
    * @param p plateforme
    * @return nombre de profils d'accès distincts
    * @since 2.2.4.1
    */
   List<Long> findCountDistinctProfilForUserAndPlateformeGroupedByContexte(Utilisateur u, Plateforme p);
   
   /**
    * Compte pour un utilisateur le nombre de profils différents qui lui sont attribués 
    * pour accéder aux banques d'une même étude (donc de contexte GATSBI) pour une plateforme donnée.
    * Cette méthode permet de visualiser rapidement, si le nombre de profils est supérieur à 1,
    * si l'utilisateur pourra accéder en mode 'toutes collections' pour chacune de ces études
    * @param u utilisateur
    * @param p plateforme
    * @return nombre de profils d'accès distincts
    * @since 2.3.0-gatsbi
    */
   List<ProfilByEtudeCount> findCountDistinctProfilForUserAndPlateformeGroupedByEtude(Utilisateur u, Plateforme p);
}
