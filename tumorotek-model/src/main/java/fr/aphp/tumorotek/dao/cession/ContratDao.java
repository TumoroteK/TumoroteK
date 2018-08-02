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
package fr.aphp.tumorotek.dao.cession;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 *
 * Interface pour le DAO du bean de domaine Contrat.
 * Interface créée le 25/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ContratDao extends GenericDaoJpa<Contrat, Integer>
{

   /**
    * Recherche les Contrats dont le numéro est égal au paramètre.
    * @param numero Numéro du Contrat recherché.
    * @return une liste de Contrats.
    */
   List<Contrat> findByNumero(String numero);

   /**
    * Recherche tous les Contrats ordonnés.
    * @return Liste ordonnée de Contrats.
    */
   List<Contrat> findByOrder();

   /**
    * Recherche tous les Contrats sauf celui dont 
    * l'identifiant est passé en paramètre.
    * @param mtaId Identifiant du Contrat que l'on souhaite exclure.
    * @return une liste de Contrats.
    */
   List<Contrat> findByExcludedId(Integer contratId);

   /**
    * Recherche tous les Contrats de la plateforme passée en params.
    * @param plateforme Plateforme des contrats recherchés.
    * @return une liste de Contrats.
    */
   List<Contrat> findByPlateforme(Plateforme plateforme);

   /**
    * Recherche tous les Contrats ayant le Collaboratuer passé en params.
    * @param coollaborateur Collaborateur des contrats recherchés.
    * @return une liste de Contrats.
    */
   List<Contrat> findByCollaborateur(Collaborateur collaborateur);

   /**
    * Recherche tous les Contrats ayant le Service passé en params.
    * @param service Service des contrats recherchés.
    * @return une liste de Contrats.
    */
   List<Contrat> findByService(Service service);

   /**
    * Recherche tous les Contrats ayant l'etablissement passé en params.
    * @param service Service des contrats recherchés.
    * @return une liste de Contrats.
    */
   List<Contrat> findByEtablissement(Etablissement etablissement);
}
