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

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Interface pour le DAO du bean de domaine Terminale.
 * Interface créée le 18/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.2.2-diamic
 *
 */
public interface TerminaleDao extends GenericDaoJpa<Terminale, Integer>
{

   /**
    * Recherche toutes les Terminales d'une Enceinte.
    * @param enceinte Enceinte mère des Terminales recherchées.
    * @return Liste ordonnée de Terminales.
    */
   List<Terminale> findByEnceinteWithOrder(Enceinte enceinte);

   /**
    * Recherche le nombre de Terminales d'une Enceinte.
    * @param enceinte Enceinte mère des Terminales recherchées.
    * @return Nombre de Terminales filles.
    */
   List<Long> findNumberTerminalesForEnceinte(Enceinte enceinte);

   /**
    * Recherche toutes les Terminales d'une Enceinte pour une
    * position donnée sauf celle dont l'identifiant est en
    * paramètre.
    * @param Enceinte enceinte des terminales recherchées.
    * @param position Position pour laquelle on recherche une terminale.
    * @param terminaleId Id de la terminale à exclure.
    * @return Liste de Terminales.
    */
   List<Terminale> findByEnceinteAndPositionExcludedId(Enceinte enceinte, Integer position, Integer terminaleId);

   /**
    * Recherche toutes les Terminales d'une Enceinte et pour une
    * position donnée.
    * @param Enceinte enceinte des terminales recherchées.
    * @param position Position pour laquelle on recherche une terminale.
    * @return Liste de Terminales.
    */
   List<Terminale> findByEnceinteAndPosition(Enceinte enceinte, Integer position);

   /**
    * Recherche toutes les Terminales d'une Enceinte pour un
    * nom donné.
    * @param Enceinte enceinte des terminales recherchées.
    * @param nom Nom pour leaquel on recherche une terminale.
    * @return Liste de Terminales.
    */
   List<Terminale> findByEnceinteAndNom(Enceinte enceinte, String nom);

   /**
    * Recherche les Terminales d'une enceinte mère,
    * sauf celle dont l'identifiant est en paramètre.
    * @param terminaleId Identifiant de la Terminale à exclure.
    * @param enceinte Enceinte mère des Terminales recherchées.
    * @return Liste de Terminale.
    */
   List<Terminale> findByExcludedIdEnceinte(Integer terminaleId, Enceinte enceinte);

   /**
    * Recherche les terminales d'une enceinte,
    * sauf celles dont les identifiants sont en paramètre.
    * @param terminaleId Identifiant d'une terminale à exclure.
    * @param terminaleId2 Identifiant d'une terminale à exclure.
    * @param enceinte Enceinte mère des terminales recherchées.
    * @return Liste de terminales.
    */
   List<Terminale> findByTwoExcludedIdsWithEnceinte(Integer terminaleId, Integer terminaleId2, Enceinte enceinte);
   
   /**
    * Recherche les terminales dont l'alias égale la valeur passée en paramètre
    * @param alias
    * @return liste terminale
    * @since 2.2.2-diamic
    */
   List<Terminale> findByAlias(String alias);
}
