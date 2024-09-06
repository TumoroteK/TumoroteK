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
package fr.aphp.tumorotek.dao.qualite;

import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Interface pour le DAO du bean de domaine Operation.
 *
 * Date: 28/09/2009
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface OperationDao extends GenericDaoJpa<Operation, Integer>
{

   /**
    * Recherche toutes les operations sauf celles dont l'id est passé
    * en paramètre.
    * @param OperationId Identifiant de l'operation que l'on souhaite
    * exclure de la liste retournée.
    * @return une liste de Operation.
    */
   List<Operation> findByExcludedId(Integer operationId);

   /**
    * Recherche les operations par objetId et entite.
    * @param objetId Integer.
    * @param entite Entite
    * @return Liste des Operation.
    */
   List<Operation> findByObjetIdAndEntite(Integer objetId, Entite entite);

   /**
    * Recherche les operations par objetId et entite pour afficher
    * l'historique : les opérations de type login et logout sont
    * exclues de cette recherche.
    * @param objetId Integer.
    * @param entite Entite
    * @return Liste des Operation.
    */
   List<Operation> findByObjetIdAndEntiteForHistorique(Integer objetId, Entite entite);

   /**
    * Recherche les operations par leur utilisateur.
    * @param utilisateur Utilisateur.
    * @return Liste des Operation.
    */
   List<Operation> findByUtilisateur(Utilisateur utilisateur);

   /**
    * Recherche les operations par objetId, entite et operationType.
    * @param objetId Integer.
    * @param entite Entite.
    * @param operationType OperationType.
    * @return Liste des Operation.
    */
   List<Operation> findByObjetIdEntiteAndOperationType(Integer objetId, Entite entite, OperationType operationType);
   
   /**
    * Recherche les opérations faites à une date.
    * @param date date recherchée.
    * @return Liste d'opérations.
    */
   List<Operation> findByDate(Calendar date);

   /**
    * Recherche les opérations faites après une date.
    * @param date date recherchée.
    * @return Liste d'opérations.
    */
   List<Operation> findByAfterDate(Calendar date);

   /**
    * Recherche les opérations faites avant une date.
    * @param date date recherchée.
    * @return Liste d'opérations.
    */
   List<Operation> findByBeforeDate(Calendar date);

   /**
    * Recherche les opérations faites entre deux dates.
    * @param date date recherchée.
    * @return Liste d'opérations.
    */
   List<Operation> findByBetweenDates(Calendar date1, Calendar date2);

}
