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
package fr.aphp.tumorotek.dao.utilisateur;

import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.utilisateur.Reservation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine Reservation.
 *
 * @author Pierre Ventadour
 * @version 10/09/2009
 *
 */
public interface ReservationDao extends GenericDaoJpa<Reservation, Integer>
{

   /**
    * Recherche les reservations dont le début est passé en paramètre.
    * @param debut Date de début.
    * @return une liste de reservations.
    */
   List<Reservation> findByDebut(Date debut);

   /**
    * Recherche les reservations dont la fin est passée en paramètre.
    * @param fin Date de fin.
    * @return une liste de reservations.
    */
   List<Reservation> findByFin(Date fin);

   /**
    * Recherche les reservations dont le debut est apres celui 
    * passé en paramètre.
    * @param debut Date de début.
    * @return une liste de reservations.
    */
   List<Reservation> findByDebutAfterDate(Date debut);

   /**
    * Recherche les reservations dont la fin est avant 
    * celle passée en paramètre.
    * @param fin Date de fin.
    * @return une liste de reservations.
    */
   List<Reservation> findByFinBeforeDate(Date fin);

   /**
    * Recherche les reservations dont la date passée en paramètre est entre
    * le début et la fin.
    * @param date Date testée.
    * @return une liste de reservations.
    */
   List<Reservation> findByDateBetweenDebutFin(Date date);

   /**
    * Recherche les reservations dont l'utilisateur est passé en paramètre.
    * @param utilisateur pour lequel on recherche des reservations.
    * @return une liste de reservations.
    */
   List<Reservation> findByUtilisateur(Utilisateur u);

   /**
    * Recherche la reservation dont l'identifiant passé en paramètre.
    * L'association avec la table UTILISATEUR sera chargée par 
    * l'intermédiaire d'un fetch.
    * @param l'utilisateur de la reservation recherchée.
    * @return une reservation.
    */
   List<Reservation> findByIdWithFetch(Integer reservationId);

}
