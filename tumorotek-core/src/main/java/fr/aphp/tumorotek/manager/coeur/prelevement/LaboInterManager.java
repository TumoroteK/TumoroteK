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
package fr.aphp.tumorotek.manager.coeur.prelevement;

import java.util.List;

import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Interface pour le manager du bean de domaine LaboInter.<br>
 * Interface créée le 05/10/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un labo intermediaire (controle de doublons)<br>
 * 	- Modifier un labo intermediaire (controle de doublons)<br>
 * 	- Afficher toutes les labos<br>
 * 	- Afficher avec un filtre sur le labo<br>
 * 	- Supprimer un labo<br>
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface LaboInterManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param labo LaboInter a creer
    * @param prelevement Prelevement associe (non null)
    * @param service Service associe
    * @param collaborateur Collaborateur associe
    * @param transporteur Transporteur associe
    */
   void createObjectManager(LaboInter labo, Prelevement prelevement, Service service, Collaborateur collaborateur,
      Transporteur transporteur, boolean doValidation);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param objet Objet à mettre à jour dans la base.
    * @param prelevement Prelevement associe (non null)
    * @param service Service associe
    * @param collaborateur Collaborateur associe
    * @param transporteur Transporteur associe
    */
   void updateObjectManager(LaboInter obj, Prelevement prelevement, Service service, Collaborateur collaborateur,
      Transporteur transporteur, boolean doValidation);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id 
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param labo LaboInter dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(LaboInter o);

   /**
    * Supprime un objet de la base de données.
    * @param labo LaboInter à supprimer de la base de données.
    */
   void removeObjectManager(LaboInter obj);

   /**
    * Recherche toutes les instances de LaboInter présentes dans la base.
    * @return List contenant les LaboInter.
    */
   List<LaboInter> findAllObjectsManager();

   /**
    * Recherche tous les labos intermediaires associes au service
    * passe en parametre.
    * @param service Service 
    * @return Liste de LaboInter.
    */
   List<LaboInter> findByServiceManager(Service service);

   /**
    * Recherche tous les labos intermediaires associes au collaborateur
    * passe en parametre.
    * @param collaborateur Collaborateur 
    * @return Liste de LaboInter.
    */
   List<LaboInter> findByCollaborateurManager(Collaborateur collaborateur);

   /**
    * Recherche tous les labos intermediaires associes au transporteur
    * passe en parametre.
    * @param transporteur Transporteur
    * @return Liste de LaboInter.
    */
   List<LaboInter> findByTransporteurManager(Transporteur transporteur);

   /**
    * Recherche tous les labos intermediaires d'un prelevement
    * passe en parametre et les renvoie dans l'ordre.
    * @param prelevement Prelevement
    * @return Liste de LaboInter.
    */
   List<LaboInter> findByPrelevementWithOrder(Prelevement prelevement);

}
