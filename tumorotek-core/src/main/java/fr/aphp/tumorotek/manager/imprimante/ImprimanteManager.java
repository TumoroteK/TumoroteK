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
package fr.aphp.tumorotek.manager.imprimante;

import java.util.List;

import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.ImprimanteApi;

/**
 *
 * Interface pour le manager du bean de domaine Imprimante.
 * Interface créée le 21/03/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ImprimanteManager
{

   /**
    * Recherche une Imprimante dont l'identifiant est passé en paramètre.
    * @param imprimanteId Identifiant de l'Imprimante que l'on recherche.
    * @return Une Imprimante.
    */
   Imprimante findByIdManager(Integer imprimanteId);

   /**
    * Recherche toutes les Imprimantes présents dans la base, ordonnés
    * par nom.
    * @return Liste de Imprimantes.
    */
   List<Imprimante> findAllObjectsManager();

   /**
    * Recherche toutes les Imprimantes présentes d'une Plateforme, ordonnés
    * par nom.
    * @param plateforme Plateforme.
    * @return Liste de Imprimantes.
    */
   List<Imprimante> findByPlateformeManager(Plateforme plateforme);

   /**
    * Recherche toutes les Imprimantes présentes d'une Plateforme, en
    * fonction de leur nom.
    * @param nom Nom de l'imprimante.
    * @param plateforme Plateforme.
    * @return Liste de Imprimantes.
    */
   List<Imprimante> findByNomAndPlateformeManager(String nom, Plateforme plateforme);

   /**
    * Recherche tous les noms d'Imprimantes présentes d'une Plateforme, 
    * ordonnés par nom.
    * @param plateforme Plateforme.
    * @return Liste de noms d'Imprimantes.
    */
   List<String> findByPlateformeSelectNomManager(Plateforme plateforme);

   /**
    * Recherche les doublons de l'Imprimante passé en paramètre.
    * @param imprimante Une Imprimante pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Imprimante imprimante);

   /**
    * Test si une Imprimante est lié à des utilisateurs.
    * @param imprimante Imprimante que l'on souhaite tester.
    * @return Vrai si l'Imprimante est utilisée.
    */
   Boolean isUsedObjectManager(Imprimante imprimante);

   /**
    * Persist une instance d'Imprimante dans la base de données.
    * @param imprimante Nouvelle instance de l'objet à créer.
    * @param plateforme Plateforme de l'Imprimante.
    * @param imprimanteApi ImprimanteApi de l'Imprimante.
    */
   void saveManager(Imprimante imprimante, Plateforme plateforme, ImprimanteApi imprimanteApi);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param imprimante Objet à mettre à jour dans la base.
    * @param plateforme Plateforme de l'Imprimante.
    * @param imprimanteApi ImprimanteApi de l'Imprimante.
    */
   void saveManager(Imprimante imprimante, Plateforme plateforme, ImprimanteApi imprimanteApi);

   /**
    * Supprime une Imprimante de la base de données.
    * @param imprimante Imprimante à supprimer de la base de données.
    */
   void deleteByIdManager(Imprimante imprimante);

}
