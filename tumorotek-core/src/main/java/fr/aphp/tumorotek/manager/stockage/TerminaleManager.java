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

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Terminale.
 * Interface créée le 19/03/10.
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public interface TerminaleManager
{

   /**
    * Recherche une Terminale dont l'identifiant est passé en paramètre.
    * @param terminaleId Identifiant de la Terminale que l'on recherche.
    * @return Une Terminale.
    */
   Terminale findByIdManager(Integer terminaleId);

   /**
    * Recherche toutes les Terminales présentes dans la base.
    * @return Liste de Terminale.
    */
   List<Terminale> findAllObjectsManager();

   /**
    * Recherche toutes les Terminales d'une enceinte.
    * @param enceinte Enceinte des Terminales que l'on recherche.
    * @return Liste de Terminales.
    */
   List<Terminale> findByEnceinteWithOrderManager(Enceinte enceinte);

   /**
    * Recherche les Terminales d'une enceinte dont le nom est passé
    * en paramètre.
    * @param enceinte Enceinte des Terminales que l'on recherche.
    * @param nom Nom de la terminaele.
    * @return Liste de Terminales.
    */
   List<Terminale> findByEnceinteAndNomManager(Enceinte enceinte, String nom);

   /**
    * Retourne tous les emplacements d'une enceinte terminale.
    * @param terminale Enceinte terminale.
    * @return Liste d'emplacements.
    */
   Set<Emplacement> getEmplacementsManager(Terminale terminale);

   /**
    * Test si la Terminale se trouve dans les limites de l'enceinte.
    * @param terminale Terminale à tester.
    * @return True si la Terminale est valide.
    */
   Boolean checkTerminaleInEnceinteLimitesManager(Terminale terminale);

   /**
    * Compte le nombre d'emplacements libres dans la terminales.
    * @param terminale Terminale.
    * @return Le nombre d'emplacements libres.
    */
   Long getNumberEmplacementsLibresManager(Terminale terminale);

   /**
    * Compte le nombre d'emplacements occupes dans la terminales.
    * @param terminale Terminale.
    * @return Le nombre d'emplacements occupes.
    */
   Long getNumberEmplacementsOccupesManager(Terminale terminale);

   /**
    * Recherche les doublons de la Terminale passée en paramètre.
    * @param terminale Terminale pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Terminale terminale);

   /**
    * Recherche les doublons de la terminale passée en paramètre en
    * excluant cette terminale et une autre de cette recherche.
    * @param terminale Terminale pour laquelle on cherche des doublons.
    * @param terminaleDestination Terminale à exclure.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonWithoutTwoTerminalesManager(Terminale terminale, Terminale terminaleDestination);

   /**
    * Teste si la Terminale passée en paramètre est utilisée par 
    * d'autres objets.
    * @param terminale Terminale que l'on test.
    * @return True si l'objet est utilisé.
    */
   Boolean isUsedObjectManager(Terminale terminale);

   /**
    * Cette méthode recherche les codes des lignes d'une terminale.
    * @param terminale Terminale pour laquelle on recherche les
    * lignes.
    * @return Une liste de codes de lignes.
    */
   List<String> getCodesLignesManager(Terminale terminale);

   /**
    * Cette méthode recherche les codes des colonnes pour une
    * ligne d'une terminale.
    * @param terminale Terminale pour laquelle on recherche les
    * colonnes.
    * @param numLigne Numéro de la ligne pour laquelle on
    * recherche les codes des colonnes.
    * @return Une liste de codes de colonnes.
    */
   List<String> getCodesColonnesManager(Terminale terminale, Integer numLigne);

   /**
    * Cette méthode récupère tous les échantillons contenus dans
    * une enceinte terminale.
    * @param terminale Terminale pour laquelle on souhaite extraire
    * les échantillons.
    * @return Liste d'échantillons.
    */
   List<Echantillon> getEchantillonsManager(Terminale terminale);

   /**
    * Cette méthode récupère tous les dérivés contenus dans
    * une enceinte terminale.
    * @param terminale Terminale pour laquelle on souhaite extraire
    * les dérivés.
    * @return Liste de dérivés.
    */
   List<ProdDerive> getProdDerivesManager(Terminale terminale);

   /**
    * Méthode qui récupère la liste des parents d'une terminale (jusqu'au
    * conteneur).
    * @param terminale Boite pour laquelle on recherche les parents.
    * @return Liste de parents, le 1er élément de la liste est le parent
    * le plus haut (le conteneur par ex.)
    */
   List<Object> getListOfParentsManager(Terminale terminale);

   /**
    * Persist une instance de Terminale dans la base de données.
    * @param terminale Nouvelle instance de l'objet à créer.
    * @param enceinte Enceinte de la Terminale.
    * @param terminaleType TerminaleType de la Terminale.
    * @param banque Banque de la Terminale.
    * @param entite Entite de la Terminale.
    * @param terminaleNumerotation Numerotation de la Terminale.
    * @param couleur Couleur à appliquer à la boite.
    */
   void createObjectManager(Terminale terminale, Enceinte enceinte, TerminaleType terminaleType, Banque banque, Entite entite,
      TerminaleNumerotation terminaleNumerotation, Couleur couleur, Utilisateur utilisateur);

   /**
    * Crée un ensemble de terminales pour une enceinte.
    * @param enceinte Enceinte des Terminales.
    * @param terminale terminaleque l'on va persister.
    * @param number Nombre de terminales à créer.
    * @param firstPosition Index de la première terminale.
    */
   List<Terminale> createMultiObjetcsManager(Enceinte enceinte, Terminale terminale, Integer number, Integer firstPosition,
      Utilisateur utilisateur);

   /**
    * Crée un ensemble de terminales de type visotube 
    * pour une enceinte.
    * @param enceinte Enceinte des Terminales.
    * @param terminale terminaleque l'on va persister.
    * @param number Nombre de terminales à créer.
    * @param firstPosition Index de la première terminale.
    * @param nameFromColor Si true, le nom de la terminale sera
    * @param size contenance du visotube 16 ou 12
    * @param utilisateur 
    * créé à partir de sa couleur.
    * @version 2.0.10
    */
   List<Terminale> createMultiVisotubesManager(Enceinte enceinte, Terminale terminale, Integer number, Integer firstPosition,
      boolean nameFromColor, Integer size, Utilisateur utilisateur);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param terminale Instance de l'objet à persister.
    * @param enceinte Enceinte de la Terminale.
    * @param terminaleType TerminaleType de la Terminale.
    * @param banque Banque de la Terminale.
    * @param entite Entite de la Terminale.
    * @param terminaleNumerotation Numerotation de la Terminale.
    * @param couleur Couleur à appliquer à la boite.
    * @param incidents
    * @param utilisateur
    * @param operations
    * @version 2.0.10
    */
   void updateObjectManager(Terminale terminale, Enceinte enceinte, TerminaleType terminaleType, Banque banque, Entite entite,
      TerminaleNumerotation terminaleNumerotation, Couleur couleur, List<Incident> incidents, Utilisateur utilisateur,
      List<OperationType> operations);

   /**
    * Supprime une Terminale de la base de données.
    * @param terminale Terminale à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void removeObjectManager(Terminale terminale, String comments, Utilisateur user);

   /**
    * Cette méthode échange les positions de 2 Terminales.
    * @param terminale1 Terminale.
    * @param terminale2 Terminale.
    */
   void echangerDeuxTerminalesManager(Terminale terminale1, Terminale terminale2, Utilisateur utilisateur);

   /**
    * Modifie la numérotation d'une liste de terminales.
    * @param terminales Terminales dont on souhaite modifier
    * la numérotation.
    * @param numrotation Numérotation des terminales.
    * @param utilisateur Utilisateur.
    */
   void updateNumerotationForMultiTerminales(List<Terminale> terminales, TerminaleNumerotation numrotation,
      Utilisateur utilisateur);

   /**
    * Extrait d'une terminale une Map contenant les TKStockableObjects et leurs
    * emplacements respectifs.
    * L'objectif est d'optimiser la récupération de ces informations à partir du 
    * système de stockage car elles sont indispensables à la création des 
    * évènements de stockage lors de déplacements/incidents.
    * @version 2.0.10
    * @param term
    * @return Map<TKStockableObject, Emplacement>
    */
   Map<TKStockableObject, Emplacement> getTkObjectsAndEmplacementsManager(Terminale term);

   /**
    * Renvoie les ids des terminales dont le nom/alias correspond au critère passé en paramètre, 
    * et appartenant à l'arborescence spécifiée par l'enceinte et/ou le conteneur.
    * Conteneur et enceinte peuvent être nulls, ne limitant pas à la requête. Si l'enceinte est passée en 
    * paramètre, le paramètre conteneur est pris en compte, et la fonction renvoie null si l'enceinte 
    * n'appartient pas au conteneur.
    * @param nom nom/alias LIKE des Terminale
    * @param enc Enceinte parente
    * @param List<Conteneur> conteneurs racines accessibles
    * @return List<Integer> ids Terminale
    * @since 2.1
    */
   List<Integer> findTerminaleIdsFromNomManager(String nom, Enceinte enc, List<Conteneur> cont);

   /**
    * Collecte les banques distinctes représentées par le contenu de la boite
    * @param term
    * @return liste de banques
    * @since 2.2.1
    */
   List<Banque> getDistinctBanquesFromTkObjectsManager(Terminale term);

   /**
    * Recherche les terminales dont l'alias égale la valeur passée en paramètre
    * @param alias
    * @return liste terminale
    * @since 2.2.2-diamic
    */
   List<Terminale> findByAliasManager(String _a);

   /**
    * Renvoie une trace emplacement de tous les échantillons/dérivés contenus 
    * dans une terminale.
    * Voir https://tumorotek.myjetbrains.com/youtrack/issue/TK-291
    * @param term
    * @return liste de trace echantillon/stockage
    * @since 2.2.3-genno
    */
   List<OldEmplTrace> getTkObjectsEmplacementTracesManager(Terminale term);
}
