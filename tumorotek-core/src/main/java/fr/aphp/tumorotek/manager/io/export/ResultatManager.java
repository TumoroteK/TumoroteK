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

import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;

/**
 *
 * Implémentation du manager du bean de domaine Résultat.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface ResultatManager
{

   /**
    * Recherche un Résultat dont l'identifiant est passé en paramètre.
    * @param idResultat Identifiant du Résultat que l'on recherche.
    * @return un Résultat.
    */
   Resultat findByIdManager(Integer idResultat);

   /**
    * Recherche tous les Résultats présents dans la BDD.
    * @return Liste de Résultats.
    */
   List<Resultat> findAllObjectsManager();

   /**
    * Copie un Résultat en BDD.
    * @param resultat Résultat à copier.
    * @return le Résultat copié.
    */
   Resultat copyResultatManager(Resultat resultat, Affichage affichage);

   /**
    * Créé un Résultat en BDD.
    * @param resultat Résultat à créer.
    * @param affichage Affichage du Résultat.
    * @param champ Champ du Résultat.
    */
   void createObjectManager(Resultat resultat, Affichage affichage, Champ champ);

   /**
    * Met à jour un Résultat en BDD.
    * @param resultat Résultat à mettre à jour.
    * @param affichage Affichage du Résultat.
    * @param champ Champ du Résultat.
    */
   void updateObjectManager(Resultat resultat, Affichage affichage, Champ champ);

   /**
    * Supprime un Résultat.
    * @param groupement Résultat à supprimer.
    */
   void removeObjectManager(Resultat resultat);

   /**
    * Recherche la liste des Résultats de l'Affichage passé en paramètre.
    * @param affichage Affichage dont ont souhaite obtenir les Résultats.
    * @return la liste des Résultats d'un Affichage
    */
   List<Resultat> findByAffichageManager(Affichage affichage);
}