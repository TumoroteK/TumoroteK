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
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;

/**
 *
 * Interface pour le DAO du bean de domaine Adicap.
 * Interface créée le 22/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface AdicapDao extends GenericDaoJpa<Adicap, Integer>
{

   /**
    * Recherche les codes Adicap dont le code est like celui passé
    * en paramètre.
    * @param code Code pour lequel on recherche des codes Adicap.
    * @return Liste de codes Adicap.
    */
   List<Adicap> findByCodeLike(String code);

   /**
    * Recherche les codes Adicap dont le libellé est like celui passé en
    * paramètre.
    * @param libelle Description du code Adicap que l'on recherche.
    * @return une liste de codes Adicap.
    */
   List<Adicap> findByLibelleLike(String libelle);

   /**
    * Recherche les codes Adicap dont le groupe est passé en paramètre.
    * Ne renvoie que les codes de premier niveau (cad adicapParent = null)
    * @param groupe contenant les codes.
    * @return une liste de codes Adicap.
    */
   List<Adicap> findByAdicapGroupeNullParent(AdicapGroupe groupe);

   /**
    * Recherche les codes Adicap dont le groupe est passé en paramètre.
    * @param groupe contenant les codes.
    * @param codeOrLibelle
    * @return une liste de codes Adicap.
    */
   List<Adicap> findByAdicapGroupeAndCodeOrLibelle(AdicapGroupe groupe, String codeOrLibelle);

   /**
    * Recherche les codes Adicap dont la morpho est passée en paramètre.
    * @param isMorpho Vrai ou faux.
    * @return une liste de codes Adicap.
    */
   List<Adicap> findByMorpho(Boolean isMorpho);

   /**
    * Recherche les enfants du code Adicap passé en paramètre.
    * @param parent code Adicap pour lequel on
    * recherche les enfants.
    * @return une liste de codes Adicap.
    */
   List<Adicap> findByAdicapParentAndCodeOrLibelle(Adicap parent, String codeOrLibelle);

   /**
    * Recherche les codes ADICAP dans un dictionnaire par son code
    * ou son libellé LIKE.
    * @param grp Dico ADICAP
    * @param codeOrLibelle
    * @return liste de codes Adicap
    */
   List<Adicap> findByDicoAndCodeOrLibelle(AdicapGroupe grp, String codeOrLibelle);
}
