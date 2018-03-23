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
package fr.aphp.tumorotek.manager.ui;

import java.util.List;

import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.ui.UiCompValue;
import fr.aphp.tumorotek.model.ui.UiRequete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine UiRequete.
 * Interface créée le 223/07/2014.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.11
 *
 */
public interface UiRequeteManager
{

   /**
    * Recherche une Requête dont l'id est passé en paramètre.
    * @param id
    * @return requete.
    */
   UiRequete findByIdManager(Integer fichierId);

   /**
    * Recherche les requetes enregistrées par un utilisateur pour un 
    * onglet (entite)
    * @param u Utilisateur
    * @param e Entite
    * @return liste de UiRequetes
    */
   List<UiRequete> findByUtilisateurAndEntiteManager(Utilisateur u, Entite e);

   /**
    * Recherche les doublons de la requête passée en paramètre.
    * @param uiRequete.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(UiRequete req);

   /**
    * Persist une instance de UiRequete dans la base de données.
    * Cree une requete dans le systeme.
    * @param String nom de la nouvelle requête à créer.
    * @param utilisateur 
    * @param entite
    * @param ordre
    * @param  liste UiCompValues
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    * @throws validationException si nom illegal
    * @throws RequiredObjectIsNullException si ut, et ou ordre sont nulls
    */
   void createObjectManager(String nom, Utilisateur ut, Entite et, Integer ordre, List<UiCompValue> vals);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * Remplace toutes les UiCompValues par les values passées en paramètres.
    * @param requete
    * @param  liste UiCompValues
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   void mergeObjectManager(UiRequete requete, List<UiCompValue> vals);

   /**
    * Supprime une UiRequete de la base de données.
    * @param requete à supprimer de la base de données.
    */
   void removeObjectManager(UiRequete requete);
}
