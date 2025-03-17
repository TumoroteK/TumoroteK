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
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Requête.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface RequeteManager
{

   /**
    * Recherche une Requête dont l'identifiant est passé en paramètre.
    * @param id Identifiant de la Requête que l'on recherche.
    * @return une Requête.
    */
   Requete findByIdManager(Integer id);

   /**
    * Recherche toutes les Requêtes présentes dans la BDD.
    * @return Liste de Requêtes.
    */
   List<Requete> findAllObjectsManager();

   /**
    * Renomme une Requête (change son intitulé).
    * @param requete Requête à renommer.
    * @param intitule nouvel intitulé de la Requête.
    */
   void renameRequeteManager(Requete requete, String intitule);


   /**
    * Créé une nouvelle Requête en BDD.
    * @param requete Requête à créer.
    * @param createur Utilisateur qui créé la Requête.
    */
   void createObjectManager(Requete requete, Groupement groupement, Utilisateur createur, Banque banque);

   /**
    * Met à jour une Requête en BDD.
    * @param requete Requête à mettre à jour.
    * @param createur Utilisateur qui met à jour la Requête.
    */
   void updateObjectManager(Requete requete, Groupement groupement, Utilisateur createur);

   /**
    * Supprime une Requête en BDD.
    * @param requete Requête à supprimer
    */
   void removeObjectManager(Requete requete);

   /**
    * Recherche les Requêtes dont la Banque est passée en
    * paramètre.
    * @param banque Banque qui à créé les Requêtes recherchées.
    * @return la liste de toutes les Requêtes de la Banque.
    */
   List<Requete> findByBanqueManager(Banque banque);

   /**
    * Recherche les Requetes pour les Banques passées en
    * paramètre.
    * @param banques Liste de Banques des Requetes recherchés.
    * @return la liste de tous les Requetes des Banques.
    */
   List<Requete> findByBanqueInLIstManager(List<Banque> banques);

   /**
    * Méthode qui vérifie que la Requete n'est pas utilisée.
    * @param requete Requete.
    * @return True si la Requete est associée à une recherche.
    */
   Boolean isUsedObjectManager(Requete requete);


   /**
    * Recherche les Requêtes par intitulé dans une plateforme spécifique.
    *
    * @param intitule L'intitulé de la Requête à rechercher
    * @param plateforme La plateforme dans laquelle rechercher
    * @return la liste des Requêtes correspondantes
    */
   List<Requete> findByIntituleInPlateformeManager(String intitule, Plateforme plateforme);

   /**
    * Vérifie si une requête avec le même intitulé existe déjà dans la plateforme donnée.
    * Si la requête passée en paramètre existe déjà en base de données (champ requeteId valorisé) - cas de la modification de l'intitulé - 
    * l'id de la requête trouvée en base de données sera comparé avec celui de la requête en paramètre
    * pour ne renvoyer true que si les 2 sont différents.
    *
    * @param requete La requête de laquelle sera récupéré l'intitulé pour vérifier l'existence d'un doublon.
    * @param plateforme La plateforme dans laquelle vérifier l'unicité de l'intitulé.
    * @return true / false.
    */
   boolean isDoublonIntituleInPlateformeManager(final Requete requete, Plateforme plateforme);

}
