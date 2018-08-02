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
package fr.aphp.tumorotek.dao.systeme;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 *
 * Interface pour le DAO du bean de domaine CouleurEntiteType.
 *
 * Date: 29/04/2010
 *
 * @author Pierre Ventadour
 * @version 2.0
 */
public interface CouleurEntiteTypeDao extends GenericDaoJpa<CouleurEntiteType, Integer>
{

   /**
    * Recherche les couleurs définies pour une banque.
    * @param banque Banque pour laquelle on cherche des couleurs.
    * @return Liste de CouleurEntiteType.
    */
   List<CouleurEntiteType> findByBanque(Banque banque);

   /**
    * Recherche les couleurs pour les EchantillonType d'une banque.
    * @param banque Banque pour laquelle on cherche des couleurs.
    * @return Liste de CouleurEntiteType.
    */
   List<CouleurEntiteType> findByBanqueAllEchanType(Banque banque);

   /**
    * Recherche les couleurs pour les ProdType d'une banque.
    * @param banque Banque pour laquelle on cherche des couleurs.
    * @return Liste de CouleurEntiteType.
    */
   List<CouleurEntiteType> findByBanqueAllProdType(Banque banque);

   /**
    * Recherche les CouleurEntiteType sauf celle dont l'id est en paramètre.
    * @param id Identifiant de la CouleurEntiteType à exclure
    * @return Liste de CouleurEntiteType.
    */
   List<CouleurEntiteType> findByExcludedId(Integer id);

}
