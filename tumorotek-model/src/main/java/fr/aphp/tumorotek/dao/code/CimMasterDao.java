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
package fr.aphp.tumorotek.dao.code;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.code.CimMaster;

/**
 *
 * Interface pour le DAO du bean de domaine CimoMaster.
 * Interface créée le 21/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface CimMasterDao extends GenericDaoJpa<CimMaster, Integer>
{

   /**
    * Recherche les codes CimMasters dont le code est like celui passé
    * en paramètre.
    * @param code Code pour lequel on recherche des codes CimMasters.
    * @return une liste de codes CimMasters.
    */
   List<CimMaster> findByCodeLike(String code);

   /**
    * Recherche les codes CimMasters dont le libellé est like celui passé en
    * paramètre.
    * @param libelle Description du code Cim que l'on recherche.
    * @return une liste de codes Cim.
    */
   List<CimMaster> findByLibelleLike(String libelle);

   /**
    * Recherche les codes CimMasters dont le level égale celui passé
    * en paramètre. Permet de récupérer les codes de niveau 1.
    * @param level Level pour lequel on recupere les codes.
    * @return une liste de codes CimMasters.
    */
   List<CimMaster> findByLevel(Integer level);
}
