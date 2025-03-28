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
package fr.aphp.tumorotek.dao.stockage;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;

import java.util.List;

/**
 *
 * Interface pour le DAO du bean de domaine Conteneur.
 * Interface créée le 17/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.0.13
 *
 */
public interface ConteneurDao extends GenericDaoJpa<Conteneur, Integer>
{

   /**
    * Recherche tous les Conteneurs d'une banque ordonnées.
    * @param banqueId Identifiant de la Banque des Conteneurs
    * recherchés.
    * @return Liste ordonnée de Conteneurs.
    */
   List<Conteneur> findByBanqueIdWithOrder(Integer banqueId);

   /**
    * Recherche des Conteneurs qui ont des identifiants dans la liste
    * passée en paramètre.
    * @param listIds liste des identifiants de Conteneur que l'on recherche.
    * @return Liste de Conteneurs.
    */
   List<Conteneur> findByIdList(List<Integer> listIds);


   /**
    * Recherche tous les Conteneurs d'une banque en fct de son
    * code.
    * @param banqueId Identifiant de la Banque des Conteneurs
    * recherchés.
    * @param code Code du conteneur.
    * @return Liste ordonnée de Conteneurs.
    */
   List<Conteneur> findByBanqueIdAndCode(Integer banqueId, String code);

   /**
    * Recherche tous les Conteneurs initialement crée par une plateforme.
    * @param plateforme.
    * @return Liste ordonnée de Conteneurs.
    */
   List<Conteneur> findByPlateformeOrigWithOrder(Plateforme orig);

   /**
    * Recherche tous les conteneurs qui sont accessibles à partir
    * d'une plateforme, et si ils sont actuellement déja assignés en partage ou pas.
    * @param Plateforme pf
    * @param partage true/false
    * @return Liste de conteneurs.
    */
   List<Conteneur> findByPartage(Plateforme pf, Boolean partage);

   /**
    * Recherche tous les conteneurs qui sont accessibles à partir
    * d'un service.
    * @param Service serv
    * @return Liste de conteneurs.
    */
   List<Conteneur> findByService(Service serv);

   /**
    * Recherche la température de stockage correspondant à un emplacement.
    * @since 2.0.13
    * @param Integer emplacementId
    * @return liste Float
    */
   List<Float> findTempForEmplacementId(Integer emplacementId);

   /**
    * Recherche une liste de conteneurs ayant le code spécifié en paramètre et appartenant à une plateforme donnée,
    * tout en excluant un conteneur spécifique identifié par son ID.
    * Cette méthode est utilisée pour détecter d'éventuels doublons de code lors de la modification
    * d'un conteneur, en tenant compte de la distinction par plateforme introduite en version 2.
    *
    * @param code        Le code du conteneur recherché.
    * @param plateforme  La plateforme sur laquelle faire la recherche.
    * @param conteneurId L'identifiant du conteneur à exclure de la recherche.
    * @return            La liste des conteneurs correspondant aux critères spécifiés.
    */
   List<Conteneur> findByCodeAndPlateformeExcludedId(String code, Plateforme plateforme, Integer conteneurId);

   /**
    * Recherche les conteneurs ayant le code spécifié en paramètre et appartenant à une plateforme donnée.
    * Cette méthode est utilisée pour détecter d'éventuels doublons lors de la création
    * d'un conteneur, en tenant compte de la distinction par plateforme introduite en version 2.
    * NB : normalement cette méthode ne doit ramener qu'un seul élément maximum mais vu qu'en version 1
    * le contrôle de doublons était à la collection, il pourrait rester sur la plateforme, plusieurs conteneurs avec le même code.
    * Par conséquent, par sécurité, la méthode renvoie une liste.
    *
    * @param code        Le code du conteneur recherché.
    * @param plateforme  La plateforme sur laquelle faire la recherche.
    * @return            La liste des conteneurs correspondants aux critères spécifiés.
    */
   List<Conteneur> findByCodeAndPlateforme(String code, Plateforme plateforme);

   /**
    * Recherche une liste de conteneurs ayant le nom spécifié en paramètre et appartenant à une plateforme donnée.
    *
    * @param nom         Le nom des conteneurs recherchés.
    * @param plateforme  La plateforme sur laquelle faire la recherche.
    * @return            La liste des conteneurs correspondant aux critères spécifiés.
    */
   List<Conteneur> findByNomAndPlateforme(String nom, Plateforme plateforme);

   /**
    * Recherche une liste de conteneurs ayant le nom spécifié en paramètre et appartenant à une plateforme donnée,
    * tout en excluant un conteneur spécifique identifié par son ID.
    * Cette méthode est utilisée pour détecter d'éventuels doublons de nom lors de la modification
    * d'un conteneur.
    *
    * @param nom         Le nom du conteneur recherché.
    * @param plateforme  La plateforme à laquelle appartient le conteneur.
    * @param id          L'identifiant du conteneur à exclure de la recherche.
    * @return            Une liste de conteneurs correspondants aux critères spécifiés.
    */
   List<Conteneur> findByNomAndPlateformeExcludedId(String nom, Plateforme plateforme, Integer id);

}
