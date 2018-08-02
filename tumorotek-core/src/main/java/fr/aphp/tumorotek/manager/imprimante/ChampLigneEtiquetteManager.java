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

import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le manager du bean de domaine ChampLigneEtiquette.
 * Interface créée le 08/07/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ChampLigneEtiquetteManager
{

   /**
    * Recherche un ChampLigneEtiquette dont l'identifiant 
    * est passé en paramètre.
    * @param champLigneEtiquetteId Id que l'on recherche.
    * @return Un ChampLigneEtiquette.
    */
   ChampLigneEtiquette findByIdManager(Integer champLigneEtiquetteId);

   /**
    * Recherche tous les ChampLigneEtiquettes présents dans la base.
    * @return Liste de ChampLigneEtiquettes.
    */
   List<ChampLigneEtiquette> findAllObjectsManager();

   /**
    * Recherche un ChampLigneEtiquette dont la ligne est passée en paramètre.
    * @param ligneEtiquette LigneEtiquette.
    * @return Une liste de ChampLigneEtiquettes.
    */
   List<ChampLigneEtiquette> findByLigneEtiquetteManager(LigneEtiquette ligneEtiquette);

   /**
    * Recherche les ChampLigneEtiquettes de la ligne et de l'entité.
    * @param ligneEtiquette LigneEtiquette.
    * @param entite Entité.
    * @return Liste de ChampLigneEtiquettes.
    */
   List<ChampLigneEtiquette> findByLigneEtiquetteAndEntiteManager(LigneEtiquette ligneEtiquette, Entite entite);

   /**
    * Valide une instance de ChampLigneEtiquette dans la base de données.
    * @param importColonne Nouvelle instance de l'objet à créer.
    * @param template ImportTemplate.
    * @param champ Champ.
    */
   void validateObjectManager(ChampLigneEtiquette champLigneEtiquette, LigneEtiquette ligneEtiquette, Entite entite, Champ champ,
      String operation);

   /**
    * Persist une instance de ChampLigneEtiquette dans la base de données.
    * @param champLigneEtiquette Nouvelle instance de l'objet à créer.
    * @param ligneEtiquette LigneEtiquette.
    * @param entite Entite.
    * @param champ Champ.
    */
   void createObjectManager(ChampLigneEtiquette champLigneEtiquette, LigneEtiquette ligneEtiquette, Entite entite, Champ champ);

   /**
    * Persist une instance de ChampLigneEtiquette dans la base de données.
    * @param champLigneEtiquette Instance de l'objet à maj.
    * @param ligneEtiquette LigneEtiquette.
    * @param entite Entite.
    * @param champ Champ.
    */
   void updateObjectManager(ChampLigneEtiquette champLigneEtiquette, LigneEtiquette ligneEtiquette, Entite entite, Champ champ);

   /**
    * Supprime un ChampLigneEtiquette de la base de données.
    * @param champLigneEtiquette ChampLigneEtiquette à 
    * supprimer de la base de données.
    */
   void removeObjectManager(ChampLigneEtiquette champLigneEtiquette);

}
