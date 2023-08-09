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
package fr.aphp.tumorotek.manager.coeur.cession;

import java.util.List;

import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Retour.<br>
 * Interface créée le 25/01/10.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un Retour (controle de doublons)<br>
 * 	- Modifier un Retour (controle de doublons)<br>
 * 	- Lister tous les Retours<br>
 *  - Lister tous les Retours pour un objet<br>
 *  - Récupérer les objets associés<br>
 * 	- Supprimer un Retour
 *
 * Modifiée le 14/05/2013 pour remplacer la trace de old emplacement
 * par la trace du conteneur obligatoire à partir de l'emplacement
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public interface RetourManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * Utilise l'emplacement directement référencé par l'objet ou celui passé en
    * paramètre (dans le cas d'un déplacement) pour enregistrer
    * l'adresse logique et le conteneur associé à l'évènement de stockage.
    * @param retour Retour a creer
    * @param objet Object echantillon ou dérivé sur lequel porte le Retour
    * @param emplacement précédent (ne concerne donc que les mouvements)
    * @param collaborateur enregistrant le Retour
    * @param cession associée au Retour
    * @param transformation associée au Retour
    * @param incident associée au Retour
    * @param utilisateur Utilisateur realisant la creation
    * @param operation String creation / modification
    */
   void createOrUpdateObjectManager(Retour retour, TKStockableObject objet, Emplacement emplacement, Collaborateur collborateur,
      Cession cession, Transformation transformation, Incident incident, Utilisateur utilisateur, String operation);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param patient Retour dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(Retour retour);

   /**
    * Recherche toutes les instances de Retour présentes dans la base.
    * @return List contenant les Retour.
    */
   List<Retour> findAllObjectsManager();

   /**
    * Supprime un objet Retour de la base de données.
    * @param retour Retour à supprimer de la base de données.
    */
   void removeObjectManager(Retour retour);

   /**
    * Recherche tous les Retour enregistrés pour un objet Echantillon
    * ou Dérivé passé en paramètre.
    * @param objet Object (Echantillon ou Dérivé)
    * @return liste de Retour
    */
   List<Retour> getRetoursForObjectManager(TKStockableObject objet);

   /**
    * Cree à partir d'un retour une liste de retour identiques
    * (avec les mêmes informations) pour chaque objet echantillon/dérivé.
    * Si la liste des emplacements précédents est null, les emplacements
    * actuels référencés directement par les objets sont utilisés.
    * passé dans la liste.
    * @param objects
    * @param table Emplacements + conteneur avt dpct associés aux objets
    * @param retour base pour créer pour tous les autres.
    * @param collaborateur
    * @param cession
    * @param transformation
    * @param incident
    * @param utilisateur
    * @version 2.2.3-genno
    * @deprecated
    */
   @Deprecated
   void createRetourListManager(List<TKStockableObject> objects, List<OldEmplTrace> oldEmpAdrls, Retour retour,
      Collaborateur collaborateur, Cession cession, Transformation transformation, Incident incident, Utilisateur utilisateur);

   /**
    * Cree à partir d'un retour une liste de retour identiques
    * (avec les mêmes informations) pour chaque objet echantillon/dérivé
    * passé dans la liste.
    * Si la liste des emplacements précédents est null, les emplacements
    * actuels référencés directement par les objets sont utilisés.
    * passé dans la liste.
    * @param objects
    * @param table Emplacements + conteneur avt dpct associés aux objets
    * @param retour base pour créer pour tous les autres.
    * @param collaborateur
    * @param cession
    * @param transformation
    * @param incident
    * @param utilisateur
    * @return True si l'insert s'est bien passé.
    * @version 2.2.3-genno
    */
   boolean createRetourHugeListManager(List<TKStockableObject> objects, List<OldEmplTrace> oldEmpAdrls, Retour retour,
      Collaborateur collaborateur, Cession cession, Transformation transformation, Incident incident, Utilisateur utilisateur);
   /*
   void createRetourHugeListByTxManager(List<TKStockableObject> objects,
   		Hashtable<TKStockableObject, String> oldEmpAdrls,
   		Retour retour,
   		Collaborateur collaborateur, Cession cession,
   		Transformation transformation, Emplacement emplacement,
   		Incident incident, Utilisateur utilisateur);
   */

   /**
    * Trouve le TKStockableObject auquel est attribué ce retour.
    * @param retour
    * @return TKStockableObject Echantillon ou ProdDerive
    */
   TKStockableObject getObjetFromRetourManager(Retour retour);

   /**
    * Recherche les Retours dont les objets sont passés en paramètres sous
    * la fome découplée objetId et Entite et dont la date de retour est nulle.
    * [Envoie une exception si plusieurs évènements incomplets pour un objet.
    * Un seul attendu ou aucun -- commenté dans le code]
    * @param liste objectIds ids des objets.
    * @return Retour ou null
    * @since 2.0.10
    */
   List<Retour> findByObjectDateRetourEmptyManager(List<Integer> objIds, Entite e);

   /**
    * Modification multiple d'une liste d'évènments de stockage.
    * Tous les champs à l'exception de l'objet et de son emplacement de stockage
    * peuvent être modifiés.
    * Méthode appelée notamment pour compléter la date de retour de plusieurs
    * évènements incomplets.
    * @param retours
    * @param collaborateur
    * @param cession
    * @param transformation
    * @param incident
    * @param utilisateur
    */
   void updateMultipleObjectManager(List<Retour> retours, Collaborateur collaborateur, Cession cession,
      Transformation transformation, Incident incident, Utilisateur utilisateur);

   /**
    * Recherche les Retours pour l'objet
    * et pour une valeur d'impact sur qualité true/false.
    * @param TKStockableObject
    * @param impact
    * @return list Retours.
    * @since 2.0.10
    */
   List<Retour> findByObjectAndImpactManager(TKStockableObject obj, Boolean impact);

}
