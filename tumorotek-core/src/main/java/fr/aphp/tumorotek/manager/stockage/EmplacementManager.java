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

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Emplacement.
 * Interface créée le 02/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.2.2-diamic
 *
 */
public interface EmplacementManager
{

   /**
    * Recherche un Emplacement dont l'identifiant est passé en paramètre.
    * @param emplacementId Identifiant de l'Emplacement que l'on recherche.
    * @return Un Emplacement.
    */
   Emplacement findByIdManager(Integer emplacementId);

   /**
    * Recherche tous les Emplacements présents dans la base.
    * @return Liste d'Emplacements.
    */
   List<Emplacement> findAllObjectsManager();

   /**
    * Recherche tous les Emplacements d'une enceinte terminale.
    * @param terminale Terminale des Emplacements que l'on recherche.
    * @return Liste d'Emplacements.
    */
   List<Emplacement> findByTerminaleWithOrder(Terminale terminale);

   /**
    * Recherche tous les Emplacements d'une terminale, vides ou non.
    * @param terminale Terminale des emplacements recherchés.
    * @param vide True ou False.
    * @return Liste ordonnée d'Emplacements.
    */
   List<Emplacement> findByTerminaleAndVideManager(Terminale terminale, boolean vide);

   /**
    * Recherche tous les Emplacements d'une terminale, pour une
    * position donnée.
    * @param terminale Terminale des emplacements recherchés.
    * @param position Position recherchée.
    * @return Liste ordonnée d'Emplacements.
    */
   List<Emplacement> findByTerminaleAndPosition(Terminale terminale, Integer position);

   /**
    * Renoie l'emplacement qui se trouve à l'adresse fournie.
    * @param adrl Adresse de l'emplacement (Ex:C1.R1.T1.B1.15).
    * @return Emplacement.
    */
   Emplacement findByEmplacementAdrlManager(String adrl, Banque banque);

   /**
    * Test si l'emplacement se trouve dans les limites de la boite.
    * @param emplacement Emplacement à tester.
    * @return True si l'Emplacement est valide.
    */
   Boolean checkEmplacementInTerminale(Emplacement emplacement);

   /**
    * cette méthode calcule la valeur de la position pour les coordonnées
    * passeés en paramètres.
    * @param Terminale de l'emplacement.
    * @param numLigne Numéro de la ligne.
    * @param numColonne Numéro de la colonne.
    * @return La position.
    */
   Integer getPositionByCoordonnees(Terminale terminale, Integer numLigne, Integer numColonne);

   /**
    * Recherche les doublons de l'Emplacement passé en paramètre.
    * @param emplacement Emplacement pour lequel on cherche des doublons.
    * @return Emplacement déja présent s'il existe, null sinon.
    */
   Emplacement findDoublonManager(Emplacement emplacement);

   /**
    * Teste si l'Emplacement passé en paramètre est utilisé par 
    * d'autres objets.
    * @param emplacement Emplacement que l'on test.
    * @return True si l'objet est utilisé.
    */
   Boolean isUsedObjectManager(Emplacement emplacement);

   /**
    * Récupère le conteneur de l'emplacement passé en paramètre.
    * @param emplacement Emplacement pour lequel on souhaite
    * créer l'Adrl.
    * @return Conteneur de l'emplacement.
    */
   Conteneur getConteneurManager(Emplacement emplacement);

   /**
    * Crée l'Adrl de l'emplacement passé en paramètre.
    * @param emplacement Emplacement pour lequel on souhaite
    * créer l'Adrl.
    * @param flag positions si true renvoie adrl composée positions
    * (utilisable uniquement pour Emplacement deja en base, car resultat 
    * renvoyé par function SQL)
    * @return Adrl de l'emplacement.
    */
   String getAdrlManager(Emplacement emplacement, boolean positions);

   /**
    * Crée l'Adrl de la terminale passée en paramètre.
    * @param terminale Terminale pour laquelle on souhaite
    * créer l'Adrl.
    * @return Adrl de la Terminale.
    */
   String getTerminaleAdrlManager(Terminale terminale);

   /**
    * Renvoie la liste des noms des objets contenus dans les emplacements.
    * @param emplacements Liste d'emplacement dont on veut le nom.
    * @return Liste de noms.
    */
   List<String> getNomsForEmplacementsManager(List<Emplacement> emplacements);

   /**
    * Renvoie la liste des types des objets contenus dans les emplacements.
    * @param emplacements Liste d'emplacement dont on veut le nom.
    * @return Liste de types.
    */
   List<String> getTypesForEmplacementsManager(List<Emplacement> emplacements);

   /**
    * Renvoie la numérotation formattée d'une position en fonction d'une
    * terminale.
    * @param position Position de l'emplacement.
    * @param terminale Terminale de l'emplacement.
    * @return La numérotation formatté.
    */
   String getNumerotationByPositionAndTerminaleManager(Integer position, Terminale terminale);

   /**
    * Persist une instance d'Emplacement dans la base de données.
    * @param emplacement Nouvelle instance de l'objet à créer.
    * @param terminale Terminale de l'Emplacement.
    * @param entite Entite de l'objet se trouvant a cet Emplacement.
    */
   void saveManager(Emplacement emplacement, Terminale terminale, Entite entite);

   /**
    * Crée un ensemble d'emplacements pour une enceinte terminale.
    * @param terminale terminale pour laquelle on va créer des
    * emplacements.
    * @param number Nombre d'emplacement à créer.
    * @return La liste de emplacements créés.
    */
   List<Emplacement> createMultiObjetcsManager(Terminale terminale, Integer number);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param emplacement Nouvelle instance de l'objet à persister.
    * @param terminale Terminale de l'Emplacement.
    * @param entite Entite de l'objet se trouvant a cet Emplacement.
    */
   void saveManager(Emplacement emplacement, Terminale terminale, Entite entite);

   /**
    * Supprime un Emplacement de la base de données.
    * @param emplacement Emplacement à supprimer de la base de données.
    */
   void deleteByIdManager(Emplacement emplacement);

   /**
    * Cette méthode va changer l'adresse de plusieurs emplacements.
    * @param emplacements Liste des emplacements à déplacer.
    */
   void deplacerMultiEmplacementsManager(List<Emplacement> emplacements, Utilisateur utilisateur);

   /**
    * Passe tous les tests de validation sur une liste d'emplacements.
    * @param emplacements Liste d'emplacements à tester.
    * @param true si validation avant deplacements
    */
   void validateMultiEmplacementsManager(List<Emplacement> emplacements, boolean isDeplacement);

   /**
    * Sauvegarde (par création ou update) une liste d'emplacements.
    * @param emplacements Liste d'emplacements à sauver.
    */
   void saveMultiEmplacementsManager(List<Emplacement> emplacements);

   /**
    * Renvoie l'éventuel objet de l'entité et placé à l'emplacement passés en 
    * paramètre.
    * @param empl
    * @param String entiteNom
    * @return Echantillon
    * @since 2.0.10
    */
   List<TKStockableObject> findObjByEmplacementManager(Emplacement empl, String entiteNom);

   Integer getPositionByAdrl(Terminale terminale, String adrlPos);

   /**
    * Trouve un emplacement depuis un object stockable.
    * @param tk object
    * @return emplacement ou null 
    */
   Emplacement findByTKStockableObjectManager(TKStockableObject tkObj);

   /**
    * Trouve un emplacement depuis son adrl, accessible depuis une banque.
    * @param adrl
    * @param banque
    * @return emplacement
    * @since 2.2.2-diamic
    */
   Emplacement findByAdrlCallableManager(String adrl, Banque b);

}
