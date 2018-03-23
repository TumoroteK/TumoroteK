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
package fr.aphp.tumorotek.dao.coeur.echantillon;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.dao.TKThesaurusDao;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;

/**
 *
 * Interface pour le DAO du bean de domaine ModePrepa.
 * Interface créée le 10/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ModePrepaDao extends GenericDaoJpa<ModePrepa, Integer>, TKThesaurusDao
{

   /**
    * Recherche les mode de préparation dont le nom est égal au paramètre.
    * @param nom Nom pour lequel on recherche des modes de préparation.
    * @return une liste de modes de préparation.
    */
   List<ModePrepa> findByNom(String nom);

   /**
    * Recherche les mode de préparation dont le nom est égal au paramètre.
    * @param nomEn Nom anglais pour lequel on recherche des modes 
    * de préparation.
    * @return une liste de modes de préparation.
    */
   List<ModePrepa> findByNomEn(String nomEn);

   /**
    * Recherche le mode de préparation qui est lié à l'échantillon passé 
    * en paramètre. 
    * @param echantillonId Identifiant de l'échantillon pour lequel 
    * on recherche un mode de préparation.
    * @return une liste de ModePrepas.
    */
   List<ModePrepa> findByEchantillonId(Integer echantillonId);

   /**
    * Recherche tous les ModePrepas sauf celui dont
    * l'identifiant est passé en paramètre.
    * @param id Identifiant du ModePrepa à exclure.
    * @return Liste de ModePrepas.
    */
   List<ModePrepa> findByExcludedId(Integer id);

}
