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
package fr.aphp.tumorotek.manager.systeme;

import java.util.List;

import fr.aphp.tumorotek.model.systeme.Temperature;

/**
 *
 * Interface pour le manager du bean de domaine Temperature.
 * Interface créée le 07/07/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface TemperatureManager
{

   /**
    * Recherche une Temperature dont l'identifiant est passé en paramètre.
    * @param temperatureId Identifiant de la Temperature que l'on recherche.
    * @return Une Temperature.
    */
   Temperature findByIdManager(Integer temperatureId);

   /**
    * Recherche toutes les Temperatures présentes dans la base.
    * @return Liste de Temperatures.
    */
   List<Temperature> findAllObjectsManager();

   /**
    * Recherche les doublons de la Temperature passée en paramètre.
    * @param temperature Temperature pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Temperature temperature);

   /**
    * Persist une instance de Temperature dans la base de données.
    * @param temperature Nouvelle instance de l'objet à créer.
    */
   void createObjectManager(Temperature temperature);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param temperature Objet à mettre à jour dans la base.
    */
   void updateObjectManager(Temperature temperature);

   /**
    * Supprime une Temperature de la base de données.
    * @param temperature Temperature à supprimer de la base de données.
    */
   void removeObjectManager(Temperature temperature);

}
