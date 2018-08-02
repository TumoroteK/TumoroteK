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
package fr.aphp.tumorotek.dao.contexte;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;

/**
 * Interface pour le DAO du bean de domaine Etablissement.
 *
 * Date: 09/09/2009
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EtablissementDao extends GenericDaoJpa<Etablissement, Integer>
{

   /**
    * Recherche tous les établissements ordonnés par nom.
    * @return une liste ordonnée d'établissements.
    */
   List<Etablissement> findByOrder();

   /**
    * Recherche les établissements dont le nom est égal au paramètre.
    * @param nom pour lequel on recherche des établissements.
    * @return une liste d'établissements.
    */
   List<Etablissement> findByNom(String nom);

   /**
    * Recherche les établissements dont le finess est égal au paramètre.
    * @param finess pour lequel on recherche des établissements.
    * @return une liste d'établissements.
    */
   List<Etablissement> findByFiness(String finess);

   /**
    * Recherche les établissements qui sont locaux.
    * @param local .
    * @return une liste d'établissements.
    */
   List<Etablissement> findByLocal(boolean local);

   /**
    * Recherche les établissements archivés.
    * @param archive .
    * @return une liste d'établissements.
    */
   List<Etablissement> findByArchiveWithOrder(boolean archive);

   /**
    * Recherche les établissements dont les coordonnées 
    * sont passées en paramètre.
    * @param coordonnee pour lesquelles on recherche un établissement.
    * @return une liste d'établissements.
    */
   List<Etablissement> findByCoordonnee(Coordonnee coordonnee);

   /**
    * Recherche les établissements dont la ville
    * est passée en paramètre.
    * @param ville Ville pour laquelle on recherche des établissements.
    * @return une liste d'établissements.
    */
   List<Etablissement> findByVille(String ville);

   /**
    * Recherche les établissements dont la catégorie est passée en paramètre.
    * @param categorie pour laquelle on recherche des établissements.
    * @return une liste d'établissements.
    */
   List<Etablissement> findByCategorie(Categorie categorie);

   /**
    * Recherche l'établissement dont l'identifiant passé en paramètre.
    * Les associations avec les tables CATEGORIE et COORDONNEE seront chargées
    * gpar l'intermédiaire d'un fetch.
    * @param etablissementId est l'identifiant de l'établissement recherché.
    * @return un établissement.
    */
   List<Etablissement> findByIdWithFetch(Integer etablissementId);

   /**
    * Recherche l'établisseent associé au service dont l'id est
    * passé en paramètre.
    * @param serviceId est l'id du service pour lequel on recherche
    * les établissements.
    * @return une liste d'établissements (de taille 1).
    */
   List<Etablissement> findByServiceId(Integer serviceId);

   /**
    * Recherche l'établisseent associé au collaborateur dont l'id est
    * passé en paramètre.
    * @param collaborateurId est l'id du collaborateur pour lequel on recherche
    * les établissements.
    * @return une liste d'établissements (de taille 1).
    */
   List<Etablissement> findByCollaborateurId(Integer collaborateurId);

   /**
    * Recherche tous les établissements sauf celui dont l'id est passé 
    * en paramètre.
    * @param etablissementId Identifiant de l'établissement que l'on souhaite
    * exclure de la liste retournée.
    * @return une liste d'Etablissements.
    */
   List<Etablissement> findByExcludedId(Integer etablissementId);

   /**
    * Recherche le nombre de services de l'etablissement est passé 
    * en paramètre.
    * @param etablissementId Identifiant de l'établissement.
    * @return une liste sercice.
    */
   List<Long> findCountByServiceIdManager(Etablissement eta);
}
