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

/**
 *
 * Implémentation du manager du bean de domaine Champ.
 * Classe créée le 05/05/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface ChampManager
{

   /**
    * Recherche un Champ dont l'identifiant est passé en paramètre.
    * @param champId Identifiant du Champ que l'on recherche.
    * @return un Champ.
    */
   Champ findByIdManager(Integer champId);

   /**
    * Recherche tous les Champs présents dans la BDD.
    * @return Liste de Champs.
    */
   List<Champ> findAllObjectsManager();

   /**
    * Créé un Champ en BDD.
    * @param champ Champ à créer.
    * @param parent Champ parent de la reqûete.
    */
   void createObjectManager(Champ champ, Champ parent);
   
   /**
    * Créé un Champ en BDD.
    * @param champ Champ à créer.
    */
   void createObjectManager(Champ champ);

   /**
    * Met à jour un Champ en BDD.
    * @param champ Champ à mettre à jour.
    * @param parent Champ parent du Champ à mettre à jour.
    */
   void updateObjectManager(Champ champ, Champ parent);
   
   /**
    * Met à jour un Champ en BDD.
    * @param champ Champ à mettre à jour.
    */
   void updateObjectManager(Champ champ);

   /**
    * Supprime un Champ et son parent d'abord.
    * @param champ Champ à supprimer.
    */
   void removeObjectManager(Champ champ);

   /**
    * Copie un Champ et son parent d'abord.
    * @param champ Champ à copier.
    */
   Champ copyChampManager(Champ champ);

   /**
    * Chercher les Champs enfants du Champ passé en paramètre.
    * @param champ Champ dont on souhaite obtenir la liste d'enfants.
    * @return liste des enfants (Champs) d'un Champ.
    */
   List<Champ> findEnfantsManager(Champ champ);

   /**
    * Renvoie la valeur (en String) du champ pour l'objet.
    * @param champ Champ à extraire.
    * @param obj Objet.
    * @param boolean format String
    * @return Valeur du champ (en String).
    */
   <T> Object getValueForObjectManager(Champ champ, T obj, boolean prettyFormat);

}
