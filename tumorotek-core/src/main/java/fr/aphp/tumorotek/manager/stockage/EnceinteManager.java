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
package fr.aphp.tumorotek.manager.stockage;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

import java.util.List;
import java.util.Set;

/**
 *
 * Interface pour le manager du bean de domaine Enceinte.
 * Interface créée le 22/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.2.1
 *
 */
public interface EnceinteManager
{

   /**
    * Recherche une Enceinte dont l'identifiant est passé en paramètre.
    * @param enceinteId Identifiant de l'Enceinte que l'on recherche.
    * @return Une Enceinte.
    */
   Enceinte findByIdManager(Integer enceinteId);

   /**
    * Recherche toutes les Enceintes présentes dans la base.
    * @return Liste d'Enceintes.
    */
   List<Enceinte> findAllObjectsManager();

   /**
    * Recherche toutes les Enceintes d'une enceinte.
    * @param enceintePere Enceinte père des Enceintes que l'on recherche.
    * @return Liste d'Enceinte.
    */
   List<Enceinte> findByEnceintePereWithOrderManager(Enceinte enceintePere);

   /**
    * Recherche toutes les Enceintes d'un conteneur.
    * @param conteneur Conteneur des Enceintes que l'on recherche.
    * @return Liste d'Enceintes.
    */
   List<Enceinte> findByConteneurWithOrderManager(Conteneur conteneur);

   /**
    * Cette méthode renvoie tous les noms des enceintes qui se trouvent
    * au même niveau que celle passée en paramètre.
    * @param enceinte Enceinte pour la recherche.
    * @return Une liste des noms des enceintes.
    */
   List<String> usedNomsExceptOneManager(Enceinte enceinte);

   /**
    * Retourne toutes les terminales d'une enceinte.
    * @param enceinte Enceinte.
    * @return Liste d'emplacements.
    */
   Set<Terminale> getTerminalesManager(Enceinte enceinte);

   /**
    * Retourne toutes les terminales d'une enceinte en
    * parcourant toutes les enceintes filles de cette
    * enceinte.
    * @param enceinte Enceinte.
    * @return Liste de Terminales.
    */
   List<Terminale> getAllTerminalesInArborescenceManager(Enceinte enceinte);

   /**
    * Retourne tous les Enceintes d'une enceinte père.
    * @param enceintePere Enceinte père.
    * @return Liste d'Enceintes.
    */
   Set<Enceinte> getEnceintesManager(Enceinte enceintePere);

   /**
    * Retourne toutes les banques d'une enceinte.
    * @param enceinte Enceinte.
    * @return Liste de banques.
    */
   Set<Banque> getBanquesManager(Enceinte enceinte);

   /**
    * Récupère le level de l'enceinte dans l'arborescence.
    * @param enceinte Enceinte pour laquelle on veut le level
    * @return Level de l'enceinte.
    */
   Integer getLevelEnceinte(Enceinte enceinte);

   /**
    * Test si l'Enceinte se trouve dans les limites de l'enceinte père.
    * @param enceinte Enceinte à tester.
    * @return True si l'Enceinte est valide.
    */
   Boolean checkEnceinteInEnceintePereLimitesManager(Enceinte enceinte);

   /**
    * Test si l'Enceinte se trouve dans les limites du conteneur.
    * @param enceinte Enceinte à tester.
    * @return True si l'Enceinte est valide.
    */
   Boolean checkEnceinteInConteneurLimitesManager(Enceinte enceinte);

   /**
    * Teste si l'enceinte passée en paramètre est la dernière de
    * l'arborescence (ses filles sont des terminales).
    * @param enceinte Enceinte à tester.
    * @return True si c'est la dernière enceinte.
    */
   Boolean checkLastEnceinte(Enceinte enceinte);

   /**
    * Test si une position d'une Enceinte est libre. Les ids
    * en paramètre permettent d'exclure des éléments de la
    * recherche (pour l'update par ex).
    * @param enceinte Enceinte à tester.
    * @param position Position à tester.
    * @param terminaleId Id de la terminale à exclure.
    * @param enceinteId Id de l'enceinte à exclure.
    * @return True si la position est libre.
    */
   //Boolean checkPositionLibreInEnceinteManager(Enceinte enceinte,
   //		Integer position, Integer terminaleId,
   //		Integer enceinteId);

   /**
    * Test si une position d'un conteneur est libre. Les ids
    * en paramètre permettent d'exclure des éléments de la
    * recherche (pour l'update par ex).
    * @param conteneur Conteneur à tester.
    * @param position Position à tester.
    * @param enceinteId Id de l'enceinte à exclure.
    * @return True si la position est libre.
    */
   //Boolean checkPositionLibreInConteneurManager(Conteneur conteneur,
   //		Integer position, Integer enceinteId);

   /**
    * Compte le nombre d'emplacements libres dans l'enceinte.
    * @param enceinte Enceinte.
    * @return Le nombre d'emplacements libre.
    */
   Long getNumberEmplacementsLibres(Enceinte enceinte);

   /**
    * Recherche les doublons de l'Enceinte passée en paramètre.
    * @param enceinte Enceinte pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Enceinte enceinte);

   /**
    * Recherche les doublons de l'Enceinte passée en paramètre en
    * excluant une enceinte de cette recherche.
    * @param enceinte Enceinte pour laquelle on cherche des doublons.
    * @param enceinteDestination Enceinte à exclure.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonWithoutTwoEnceintesManager(Enceinte enceinte, Enceinte enceinteDestination);

   /**
    * Teste si l'Enceinte passée en paramètre est utilisée par
    * d'autres objets.
    * @param enceinte Enceinte que l'on test.
    * @return True si l'objet est utilisé.
    */
   Boolean isUsedObjectManager(Enceinte enceinte);

   /**
    * Persist une instance d'Enceinte dans la base de données.
    * @param enceinte Nouvelle instance de l'objet à créer.
    * @param enceinteType Type de l'Enceinte.
    * @param conteneur Conteneur de l'Enceinte.
    * @param enceintePere Enceinte père de l'Enceinte.
    * @param entite Entite de l'Enceinte.
    * @param banques Banques de l'Enceinte.
    */
   void createObjectManager(Enceinte enceinte, EnceinteType enceinteType, Conteneur conteneur, Enceinte enceintePere,
      Entite entite, List<Banque> banques, Couleur couleur, Utilisateur utilisateur);

   /**
    * Crée toute l'arborescence d'un conteneur : dès enceintes filles
    * jusqu'aux enceintes terminales.
    * @param conteneur Conteneur de l'arborescence.
    * @param enceintes Liste d'enceinte. Chaque objet représente un
    * squelette de subdivision dans l'arborescence.
    * @param terminale Représente le format des enceintes terminales.
    * @param Numéros des 1ères positions de chaque élément.
    * @param banques Banques liées au conteneur.
    * @param plateformes Plateformes liées au conteneur.
    */
   void createAllArborescenceManager(Enceinte enceinte, List<Enceinte> enceintes, Terminale terminale,
      List<Integer> firstPositions, List<Banque> banques, Utilisateur utilisateur) throws Exception;

   /**
    * Crée un ensemble d'enceintes pour un conteneur.
    * @param conteneur Conteneur des Enceintes.
    * @param enceinte Enceinte.
    * @param number Nombre d'Enceintes à créer.
    * @param firstPosition Index de la première enceinte.
    */
   List<Enceinte> createMultiObjetcsForConteneurManager(Conteneur conteneur, Enceinte enceinte, Integer number,
      Integer firstPosition, Utilisateur utilisateur);

   /**
    * Crée un ensemble d'enceintes pour une enceinte père.
    * @param enceintePere Enceinte pere des Enceintes.
    * @param enceinte Enceinte.
    * @param number Nombre d'Enceintes à créer.
    * @param firstPosition Index de la première enceinte.
    */
   List<Enceinte> createMultiObjetcsForEnceinteManager(Enceinte enceintePere, Enceinte enceinte, Integer number,
      Integer firstPosition, Utilisateur utilisateur);

   /**
    * Persist une instance d'Enceinte dans la base de données.
    * @param enceinte Nouvelle instance de l'objet à créer.
    * @param enceinteType Type de l'Enceinte.
    * @param conteneur Conteneur de l'Enceinte.
    * @param enceintePere Enceinte père de l'Enceinte.
    * @param entite Entite de l'Enceinte.
    * @param banques Banques de l'Enceinte.
    * @param couleur
    * @param incidents
    * @param utilisateurs
    * @param operations
    * @version 2.0.10
    */
   void updateObjectManager(Enceinte enceinte, EnceinteType enceinteType, Conteneur conteneur, Enceinte enceintePere,
      Entite entite, List<Banque> banques, Couleur couleur, List<Incident> incidents, Utilisateur utilisateur,
      List<OperationType> operations);

   /**
    * Crée toute l'arborescence d'un conteneur : dès enceintes filles
    * jusqu'aux enceintes terminales.
    * @param enceinte Enceinte à update.
    * @param enceintes Liste d'enceinte. Chaque objet représente un
    * squelette de subdivision dans l'arborescence.
    * @param terminale Représente le format des enceintes terminales.
    * @param Numéros des 1ères positions de chaque élément.
    * @param banques Banques liées au conteneur.
    * @param plateformes Plateformes liées au conteneur.
    */
   void updatewithCreateAllArborescenceManager(Enceinte enceinte, List<Enceinte> enceintes, Terminale terminale,
      List<Integer> firstPositions, List<Banque> banques, Utilisateur utilisateur) throws Exception;

   /**
    * Supprime une Enceinte de la base de données.
    * @param enceinte Enceinte à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void removeObjectManager(Enceinte enceinte, String comments, Utilisateur user);

   /**
    * Compte le nombre d'emplacements libres dans l'enceinte en
    * utilisant des PreparedStatement.
    * @param enceinte Enceinte.
    * @return Le nombre d'emplacements libre.
    */
   Long getNbEmplacementsLibresByPS(Enceinte enceinte);

   /**
    * Compte le nombre d'emplacements occupes dans l'enceinte en
    * utilisant des PreparedStatement.
    * @param enceinte Enceinte.
    * @return Le nombre d'emplacements libre.
    */
   Long getNbEmplacementsOccupesByPS(Enceinte enceinte);

   /**
    * Récupère la liste des identifiants des objets présents dans
    * une enceinte en fct de leur entité.
    * @param enceinte Enceinte.
    * @param entite Entite.
    * @return Liste des ids des objets dans l'enceinte.
    */
   List<Integer> getObjetIdsByEntiteByPS(Enceinte enceinte, Entite entite);

   /**
    * Crée l'Adrl de l'enceinte passée en paramètre.
    * @param enceinte Enceinte pour laquelle on souhaite
    * créer l'Adrl.
    * @return Adrl de l'enceinte.
    */
   String getAdrlManager(Enceinte enceinte);

   /**
    * Récupère le conteneur de l'enceinte.
    * @param enceinte Enceinte pour laquelle on souhaite
    * récupérer le conteneur.
    * @return Conteneur de l'enceinte.
    */
   Conteneur getConteneurManager(Enceinte enceinte);

   /**
    * Cette méthode échange les positions de 2 enceintes.
    * @param enceinte1 Enceinte.
    * @param enceinte2 Enceinte.
    */
   void echangerDeuxEnceintesManager(Enceinte enceinte1, Enceinte enceinte2, Utilisateur utilisateur);

   /**
    * Trouve le conteneur contenant l'enceinte en remontant toute
    * l'arborescence au besoin.
    * @param enceinte
    * @return Conteneur parent.
    */
   Conteneur getConteneurParent(Enceinte enceinte);

   /**
    * Trouve de manière récursive toutes les enceintes contenues
    * dans un conteneur à tous les niveaux.
    * @param Conteneur
    * @return liste d'enceintes
    */
   List<Enceinte> findAllEnceinteByConteneurManager(Conteneur c);

   /**
    * Peuple la liste passée en deuxième paramètre avec toutes
    * les enceintes descendantes de celles passées dans la liste
    * en premier paramètre.
    * @param encs
    * @param coll
    */
   void findEnceinteRecursiveManager(List<Enceinte> encs, List<Enceinte> coll);

   /**
    * Recherche les Enceintes d'une enceinte dont le nom est passé en
    * paramètre.
    * @param enceintePere Enceinte père des Enceintes que l'on recherche.
    * @param nom Nom de l'enceinte.
    * @return Liste d'Enceinte.
    */
   List<Enceinte> findByEnceintePereAndNomManager(Enceinte enceintePere, String nom);

   /**
    * Recherche les Enceintes d'un conteneur dont le nom est
    * passé en paramètre.
    * @param conteneur Conteneur des Enceintes que l'on recherche.
    * @param nom Nom de l'enceinte.
    * @return Liste d'Enceintes.
    */
   List<Enceinte> findByConteneurAndNomManager(Conteneur conteneur, String nom);

   /**
    * Méthode qui augmente la taille du contenu d'une enceinte.
    * @param enceinte Enceinte à agrandir.
    * @param nbPlaces Nb de places supplémentaires.
    * @param utilisateur Utilisateur.
    * @return Enceinte maj.
    */
   Enceinte updateTailleEnceinteManager(Enceinte enceinte, Integer nbPlaces, Utilisateur utilisateur);

   /**
    * Collecte les banques distinctes représentées par le contenu de l'ensemble des
    * terminales composant l'enceinte
    * @param term
    * @return liste de banques
    * @since 2.2.1
    */
   List<Banque> getDistinctBanquesFromTkObjectsManager(Enceinte enc);


   /**
    * Calcule le nombre total de places dans l'instance 'Enceinte' spécifiée et dans ses sous-enseintes de manière récursive.
    *
    * @param enceinte l'instance 'Enceinte' pour laquelle calculer le nombre total de places
    * @return le nombre total de places, y compris celles des sous-enseintes
    */
    Integer calculateTotalNbPlaces(Enceinte enceinte);

}
