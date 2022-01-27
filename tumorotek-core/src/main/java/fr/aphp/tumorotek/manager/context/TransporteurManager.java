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

import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public interface TransporteurManager
{

   /**
    * Recherche un Transporteur dont l'identifiant est passé en paramètre.
    * @param transporteurId Identifiant du Transporteur que l'on recherche.
    * @return Un Transporteur.
    */
   Transporteur findByIdManager(Integer transporteurId);

   /**
    * Recherche tous les Transporteur présents dans la base.
    * @return Liste de Transporteur.
    */
   List<Transporteur> findAllObjectsManager();

   /**
    * Recherche les doublons du Transporteur passé en paramètre.
    * @param transporteur Transporteur pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Transporteur transporteur);

   /**
    * Persist une instance de Transporteur dans la base de données.
    * @param transporteur Nouvelle instance de l'objet à créer.
    * @param coordonnee Coord associée au Transporteur.
    */
   void saveManager(Transporteur transporteur, Coordonnee coordonnee, Utilisateur utilisateur);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param transporteur Objet à mettre à jour dans la base.
    * @param coordonnee Coord associée au Transporteur.
    * @throws javax.xml.bind.ValidationException 
    */
   void saveManager(Transporteur transporteur, Coordonnee coordonnee, Utilisateur utilisateur);

   /**
    * Supprime un Transporteur de la base de données.
    * @param transporteur Transporteur à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @throws ObjectReferencedException si transporteur est référencé par des
    * objets.
    */
   void deleteByIdManager(Transporteur transporteur, String comments, Utilisateur user);

   /**
    * Verifie si le transporteur est référencé par d'autres objets du système
    * et donc ne peut être supprimé.
    * @param transp Transporteur
    * @return boolean true si le transporteur est référencé 
    * par au moins un objet.
    */
   boolean isReferencedObjectManager(Transporteur tr);

   /**
    * Trouve les transporteurs qui ne sont inactivés.
    * @return liste de transporteurs.
    */
   List<Transporteur> findAllActiveManager();

}
