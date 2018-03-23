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
package fr.aphp.tumorotek.manager.interfacage;

import java.util.List;

import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Interface pour le manager du bean de domaine ValeurExterne.
 * Interface créée le 05/10/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ValeurExterneManager
{

   /**
    * Recherche une ValeurExterne dont l'identifiant est passé en paramètre.
    * @param valeurExterneId Identifiant de la valeur que l'on recherche.
    * @return Une ValeurExterne.
    */
   ValeurExterne findByIdManager(Integer valeurExterneId);

   /**
    * Recherche toutes les ValeurExternes présentes dans la base, ordonnés
    * par nom.
    * @return Liste de ValeurExternes.
    */
   List<ValeurExterne> findAllObjectsManager();

   /**
    * Recherche toutes les ValeurExternes d'un Bloc.
    * @param blocExterne BlocExterne.
    * @return Liste de ValeurExternes.
    */
   List<ValeurExterne> findByBlocExterneManager(BlocExterne blocExterne);

   /**
    * Retourne l'objet ChampEntite correspondant à l'id se trouvant
    * dans la table.
    * @param valeurExterne Valeur dont on veut le ChampEntite.
    * @return Un ChampEntite.
    */
   ChampEntite getChampEntiteManager(ValeurExterne valeurExterne);

   /**
    * Retourne l'objet ChampAnnotation correspondant à l'id se trouvant
    * dans la table.
    * @param valeurExterne Valeur dont on veut le ChampAnnotation.
    * @return Un ChampAnnotation.
    */
   ChampAnnotation getChampAnnotationManager(ValeurExterne valeurExterne);

   /**
    * Recherche les doublons d'une ValeurExterne.
    * @param valeurExterne Valeur dont on cherche des doublons.
    * @return True s'il y a un doublon.
    */
   boolean findDoublonManager(ValeurExterne valeurExterne);

   /**
    * Vérifie que la valeur externe est valide.
    * @param valeurExterne Valeur Externe à tester.
    * @param blocExterne bloc de la valeur externe.
    */
   void validateValeurExterneManager(ValeurExterne valeurExterne, BlocExterne blocExterne);

   /**
    * Crée une valeur externe.
    * @param valeurExterne Valeur Externe à créer.
    * @param blocExterne bloc de la valeur externe.
    */
   void createObjectManager(ValeurExterne valeurExterne, BlocExterne blocExterne);

   /**
    * Supprime une valeur externe.
    * @param valeurExterne Valeur Externe à supprimer.
    */
   void removeObjectManager(ValeurExterne valeurExterne);
}
