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
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Affichage.
 * Interface créée le 02/11/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface AffichageManager
{

   /**
    * Recherche un Affichage dont l'identifiant est passé en paramètre.
    * @param id Identifiant de l'Affichage que l'on recherche.
    * @return un Affichage.
    */
   Affichage findByIdManager(Integer id);

   /**
    * Recherche tous les Affichages présents dans la BDD.
    * @return Liste d'Affichages.
    */
   List<Affichage> findAllObjectsManager();

   /**
    * Renomme un Affichage (change son intitulé).
    * @param affichage Affichage à renommer.
    * @param intitule nouvel intitulé de l'Affichage.
    */
   void renameAffichageManager(Affichage affichage, String intitule);

   /**
    * Créé un nouvel Affichage en BDD.
    * @param affichage Affichage à créer.
    * @param resultats Liste de Resultats à associer.
    * @param createur Utilisateur qui créé l'Affichage.
    */
   void createObjectManager(Affichage affichage, List<Resultat> resultats, Utilisateur createur, Banque banque);

   /**
    * Met à jour un Affichage en BDD.
    * @param affichage Affichage à mettre à jour.
    * @param resultats Liste de Resultats à associer.
    */
   void updateObjectManager(Affichage affichage, List<Resultat> resultats, List<Resultat> resultatsToRemove);

   /**
    * Supprimme un Affichage en BDD.
    * @param affichage Affichage à supprimer.
    */
   void removeObjectManager(Affichage affichage);

   /**
    * Recherche les Affichages dont la Banque est passée en
    * paramètre.
    * @param banque Banque qui à créé les Affichages recherchés.
    * @return la liste de tous les Affichages de la Banque.
    */
   List<Affichage> findByBanqueManager(Banque banque);

   /**
    * Recherche les Affichages pour les Banques passées en
    * paramètre.
    * @param banques Liste de Banques des Affichages recherchés.
    * @return la liste de tous les Affichages des Banques.
    */
   List<Affichage> findByBanqueInLIstManager(List<Banque> banques);

   /**
    * Méthode qui vérifie que l'affichage n'est pas utilisé.
    * @param affichage Affichage.
    * @return True si l'affichage est associé à une recherche.
    */
   Boolean isUsedObjectManager(Affichage affichage);

   /**
    * Recherche les Affichages par intitulé dans une plateforme spécifique.
    *
    * @param intitule L'intitulé de l'Affichage à rechercher
    * @param plateforme La plateforme dans laquelle rechercher
    * @return la liste des Affichages correspondants
    */
   List<Affichage> findByIntituleInPlateformeManager(String intitule, Plateforme plateforme);

   /**
    * Vérifie si un affichage avec le même intitulé existe déjà dans la plateforme donnée.
    * Si l'affichage passé en paramètre existe déjà en base de données (champ affichageId valorisé) - cas de la modification de l'intitulé - 
    * l'id de l'affichage trouvé en base de données sera comparé avec celui de l'affichage en paramètre
    * pour ne renvoyer true que si les 2 sont différents.
    *
    * @param affichage L'affichage duquel sera récupérer l'intitulé pour vérifier l'existence d'un doublon.
    * @param plateforme La plateforme à prendre en compte pour le contrôle de l'existence du doublon
    * @return true / false.
    */
   boolean isDoublonIntituleInPlateformeManager(Affichage affichage, Plateforme plateforme);
}
