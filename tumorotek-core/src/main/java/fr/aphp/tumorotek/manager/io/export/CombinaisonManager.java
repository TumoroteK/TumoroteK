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

import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Combinaison;

/**
 *
 * Interface pour le manager du bean de domaine Combinaison.
 * Interface créée le 02/11/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface CombinaisonManager
{

   /**
    * Copie une combinaison en BDD.
    * @param combinaison Combinaison à copier.
    * @return la Combinaison copiée
    */
   Combinaison copyCombinaisonManager(Combinaison combinaison);

   /**
    * Créé une nouvelle Combinaison en BDD.
    * @param combinaison Combinaison à créer.
    * @param champ1 Premier champ de la Combinaison.
    * @param champ2 Deuxième champ de la Combinaison.
    */
   void saveManager(Combinaison combinaison, Champ champ1, Champ champ2);

   /**
    * Met à jour une Combinaison en BDD.
    * @param combinaison Combinaison à mettre à jour.
    * @param champ1 Premier champ de la Combinaison.
    * @param champ2 Deuxième champ de la Combinaison.
    */
   void saveManager(Combinaison combinaison, Champ champ1, Champ champ2);

   /**
    * Supprime une Combinaison en BDD.
    * @param combinaison Combinaison à supprimer.
    */
   void deleteByIdManager(Combinaison combinaison);

   /**
    * Recherche une Combinaison dont l'identifiant est passé en paramètre.
    * @param idCombinaison Identifiant de la Combinaison que l'on recherche.
    * @return une Combinaison.
    */
   Combinaison findByIdManager(Integer idCombinaison);

   /**
    * Recherche toutes les Combinaisons présentes dans la BDD.
    * @return Liste d'Affichages.
    */
   List<Combinaison> findAllObjectsManager();

}
