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

/**
 *
 * Interface pour le manager du bean de domaine Coordonnee.
 * Interface créée le 05/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface CoordonneeManager
{

   /**
    * Recherche une Coordonnee dont l'identifiant est passé en paramètre.
    * @param coordonneeId Identifiant de la Coordonnee que l'on recherche.
    * @return Une Coordonnee.
    */
   Coordonnee findByIdManager(Integer coordonneeId);

   /**
    * Recherche toutes les Coordonnees présentes dans la base.
    * @return Liste de Coordonnees.
    */
   List<Coordonnee> findAllObjectsManager();

   /**
    * Recherche les Collaborateurs liés à la Coordonnee passeé
    * en paramètre.
    * @param coordonnee Coordonnee pour laquelle on recherche des
    * collaborateurs.
    * @return Liste de Collaborateurs.
    */
   Set<Collaborateur> getCollaborateursManager(Coordonnee coordonnee);

   /**
    * Recherche les doublons de la coordonnée pour le collaborateur passé
    *  en paramètre.
    * @param collaborateur Collaborateur pour lequel on cherche des doublons.
    * @param coordonnee Coordonnee pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonForCollaborateurManager(Coordonnee coordonnee, Collaborateur collaborateur);

   /**
    * Recherche les doublons dans une liste de coordonnées.
    * @param coordonnees Liste de coordonnées.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonInListManager(List<Coordonnee> coordonnees);

   /**
    * Persist une instance de Coordonnee dans la base de données.
    * @param coordonnee Nouvelle instance de l'objet à créer.
    * @param collaborateurs Liste de collaborateurs associés à la
    * coordonnée.
    */
   void saveManager(Coordonnee coordonnee, List<Collaborateur> collaborateurs);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param coordonnee Objet à mettre à jour dans la base.
    * @param collaborateurs Liste de collaborateurs associés à la
    * coordonnée.
    * @param doValidation Si true, exécute la validation de la
    * coordonnée.
    */
   void saveManager(Coordonnee coordonnee, List<Collaborateur> collaborateurs, boolean doValidation);

   /**
    * Supprime une Coordonnee de la base de données.
    * @param coordonnee Coordonnee à supprimer de la base de données.
    */
   void deleteByIdManager(Coordonnee coordonnee);

   /**
    * Test si la coordonnée est utilisée par d'autres objets que le
    * collaborateur passé en paramètre.
    * @param coordonnee Coordonnée à tester.
    * @param collaborateur Collaborateur pour lequel on teste la coord.
    * @return True si la coord estutilisée par d'autres objets.
    */
   Boolean isUsedByOtherObjectManager(Coordonnee coordonnee, Collaborateur collaborateur);

}
