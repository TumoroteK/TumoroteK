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
package fr.aphp.tumorotek.manager.qualite;

import java.sql.SQLException;
import java.util.List;

import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;

/**
 *
 * Interface pour le manager du bean de domaine ObjetNonConforme.
 * Interface créée le 09/11/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0.10.6
 *
 */
public interface ObjetNonConformeManager
{

   /**
    * Recherche un ObjetNonConforme dont l'identifiant est passé en paramètre.
    * @param objetNonConformeId Identifiant du ObjetNonConforme
    * que l'on recherche.
    * @return Un ObjetNonConforme.
    */
   ObjetNonConforme findByIdManager(Integer objetNonConformeId);

   /**
    * Recherche tous les ObjetNonConformes présents dans la base.
    * @return Liste de ObjetNonConformes.
    */
   List<ObjetNonConforme> findAllObjectsManager();

   /**
    * Recherche les ObjetNonConformes d'un objet.
    * @param obj Objet dont on recherche les ObjetNonConformes.
    * @return Liste d'ObjetNonConformes.
    */
   List<ObjetNonConforme> findByObjetManager(Object obj);

   /**
    * Recherche les ObjetNonConformes d'un objet pour un type
    * de conformité donné.
    * @param obj Objet dont on recherche les ObjetNonConformes.
    * @param type Type de la conformité.
    * @return Liste d'ObjetNonConformes.
    */
   List<ObjetNonConforme> findByObjetAndTypeManager(Object obj, Object type);

   /**
    * Crée ou update la non conformité de l'objet passé en
    * paramètres.
    * @param obj Objet non conforme.
    * @param nonConformite Non conformité.
    */
   void createUpdateOrRemoveObjectManager(Object obj, NonConformite nonConformite, String type);

   /**
    * Crée ou update les non conformités de l'objet passé en
    * paramètres.
    * @param obj Objet non conforme.
    * @param nonConformites Non conformités.
    */
   void createUpdateOrRemoveListObjectManager(Object obj, List<NonConformite> nonConformites, String type);

   /**
    * Supprime un objet de la base de données.
    * @param objetNonConforme Objet à supprimer de la base de données.
    */
   void removeObjectManager(ObjetNonConforme objetNonConforme);

   /**
    * Renvoie tous les ids objets qui référencent au moins une des
    * non conformites passées dans la liste en paramètre.
    * Teste si la liste est vide ou null.
    * @param noconfs
    * @return liste integer
    * @since 2.0.10
    */
   List<Integer> findObjetIdsByNonConformitesManager(List<NonConformite> nocfs);

   /**
    * Préparations des batch statements pour full JDBC inserts pour l'objet passé en paramètre.
    * @param jdbcSuite contenant les ids et statements permettant
    * la creation des objets en full JDBC
    * @param obj TKAnnotableObjects
    * @param nonConformites
    * @since 2.0.10.6
    */
   void prepareListJDBCManager(EchantillonJdbcSuite jdbcSuite, TKAnnotableObject tkObj, List<NonConformite> nonConformites)
      throws SQLException;

}
