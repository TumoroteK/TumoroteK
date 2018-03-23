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
package fr.aphp.tumorotek.dao.imprimante;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.Imprimante;

/**
 *
 * Interface pour le DAO du bean de domaine Imprimante.
 *
 * @author Pierre Ventadour
 * @version 18/03/2011
 *
 */
public interface ImprimanteDao extends GenericDaoJpa<Imprimante, Integer>
{

   /**
    * Recherche les Imprimante d'une plateforme.
    * @param plateforme Plateforme.
    * @return Une liste d'Imprimantes.
    */
   List<Imprimante> findByPlateforme(Plateforme plateforme);

   /**
    * Recherche les Imprimantes d'une plateforme en fct de son nom.
    * @param nom nom de l'imprimante.
    * @param plateforme Plateforme.
    * @return Une liste d'Imprimantes.
    */
   List<Imprimante> findByNomAndPlateforme(String nom, Plateforme plateforme);

   /**
    * Recherche les noms d'Imprimantes d'une plateforme.
    * @param plateforme Plateforme.
    * @return Une liste de noms.
    */
   List<String> findByPlateformeSelectNom(Plateforme plateforme);

   /**
    * Recherche les Imprimantes dont l'identifiant est différent
    * de celui passé en paramètre.
    * @param utilisateurId Identifiant de l'Imprimante à exclure.
    * @return Une liste d'Imprimantes.
    */
   List<Imprimante> findByExcludedId(Integer imprimanteId);

}
