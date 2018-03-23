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
package fr.aphp.tumorotek.manager.io.export;

import java.util.List;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Recherche.
 * Classe créée le 25/02/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface RechercheManager
{

   /**
    * Recherche une Recherche dont l'identifiant est passé en paramètre.
    * @param id Identifiant de la Recherche que l'on recherche.
    * @return une Recherche.
    */
   Recherche findByIdManager(Integer id);

   /**
    * Recherche toutes les Recherches présentes dans la BDD.
    * @return Liste de Recherches.
    */
   List<Recherche> findAllObjectsManager();

   /**
    * Renomme une Recherche (change son intitulé).
    * @param recherche Recherche à renommer.
    * @param intitule nouvel intitulé de la Recherche.
    */
   void renameRechercheManager(Recherche recherche, String intitule);

   //	/**
   //	 * Copie une Recherche en BDD.
   //	 * @param recherche Recherche à copier.
   //	 * @param copieur Utilisateur qui copie la Recherche.
   //	 * @return la Recherche copiée.
   //	 */
   //	Recherche copyRechercheManager(Recherche recherche, Utilisateur copieur,
   //			Banque banque);

   /**
    * Créé une nouvelle Recherche en BDD.
    * @param recherche Recherche à créer.
    * @param affichage Affichage à associer.
    * @param requete Requete à associer.
    * @param banques Liste de Banque à associer.
    * @param createur Utilisateur qui créé la Recherche.
    */
   void createObjectManager(Recherche recherche, Affichage affichage, Requete requete, List<Banque> banques, Utilisateur createur,
      Banque banque);

   /**
    * Met à jour une Recherche en BDD.
    * @param recherche Recherche à mettre à jour.
    * @param affichage Affichage à associer.
    * @param requete Requete à associer.
    * @param banques Liste de Banque à associer.
    * @param createur Utilisateur qui met à jour la Recherche.
    */
   void updateObjectManager(Recherche recherche, Affichage affichage, Requete requete, List<Banque> banques, Utilisateur createur,
      Banque banque);

   /**
    * Supprime une Recherche en BDD.
    * @param recherche Recherche à supprimer
    */
   void removeObjectManager(Recherche recherche);

   /**
    * Recherche les Recherches dont l'utilisateur créateur est passé en
    * paramètre. 
    * @param util Utilisateur qui à créé les Recherches recherchées.
    * @return la liste de toutes les Recherches de l'Utilisateur.
    */
   List<Recherche> findByUtilisateurManager(Utilisateur util);

   /**
    * Recherche les Recherches dont l'intitulé est passé en paramètre.
    * 
    * @param intitule Intitulé des Recherches recherchées.
    * @return la liste de toutes les Recherches de l'intitulé.
    */
   List<Recherche> findByIntituleManager(String intitule);

   /**
    * Recherche les Recherches dont l'intitulé et l'utilisateur
    * sont passés en paramètre. 
    * @param intitilé des Recherches recherchés.
    * @param util Utilisateur qui à créé les Recherches recherchés.
    * @return la liste de toutes les Recherches de l'intitulé.
    */
   List<Recherche> findByIntituleAndUtilisateurManager(String intitule, Utilisateur util);

   /**
    * Recherche les Recherches dont la Requête est passée en paramètre.
    * 
    * @param requete Requete des Recherches recherchées.
    * @return la liste de toutes les Recherches comprenant la Requête.
    */
   List<Recherche> findByRequeteManager(Requete requete);

   /**
    * Recherche les Recherches dont l'Affichage est passé en paramètre.
    * 
    * @param affichage Affichage des Recherches recherchées.
    * @return la liste de toutes les Recherches comprenant l'Affichage.
    */
   List<Recherche> findByAffichageManager(Affichage affichage);

   /**
    * Recherche les doublons d'un Affichage passé en paramètre.
    * @param affichage un Affichage pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Recherche recherche);

   /**
    * Méthode qui permet de vérifier que 2 Recherches sont des copies.
    * @param r Recherche première Recherche à vérifier.
    * @param copie deuxième Recherche à vérifier.
    * @return true si les 2 Recherches sont des copies, false sinon.
    */
   Boolean isCopyManager(Recherche r, Recherche copie);

   /**
    * Récupère les banques d'une Recherche.
    * @param recherche Recherche dont on souhaite récupérer les banques
    * @return liste de banques de la recherche
    */
   List<Banque> findBanquesManager(Recherche recherche);

   /**
    * Recherche les Recherches dont la banque est passé en paramètre.
    * @param banque Banque des Recherches recherchées.
    * @return la liste de toutes les Recherches comprenant la Banque.
    */
   List<Recherche> findByBanqueManager(Banque banque);

   /**
    * Recherche les Recherches pour les Banques passées en
    * paramètre. 
    * @param banques Liste de Banques des Recherches recherchés.
    * @return la liste de tous les Recherches des Banques.
    */
   List<Recherche> findByBanqueInLIstManager(List<Banque> banques);
}
