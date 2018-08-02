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

import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine Retour.
 * Interface créée le 25/01/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface RetourDao extends GenericDaoJpa<Retour, Integer>
{

   /**
    * Recherche tous les retour sauf celui dont l'id est passé 
    * en paramètre, l'objet et l'entite
    * @param retourId Identifiant du retour que l'on souhaite
    * exclure de la liste retournée.
    * @return une liste de Retour.
    * @since 2.0.10
    */
   List<Retour> findByExcludedId(Integer retourId, Integer objectId, Entite entite);

   /**
    * Recherche les Retours dont l'objet sur lequel ils sont
    * appliqués est passé en paramètre sous sa fome découplée
    * objetId et Entite.
    * @param objectId id de l'objet.
    * @return list Retours.
    */
   List<Retour> findByObject(Integer objectId, Entite entite);

   /**
    * Renvoie l'id max de la table retour.
    * @return
    */
   List<Integer> findByMaxId();

   /**
    * Recherche les retours pour un objet donné dont l'intervalle de dates 
    * composé par DateSortie - DateRetour contient la date passée en paramètre.
    * Cette méthode sera appelée lors du contrôle de cohérence de dates.
    * @param dt date 
    * @param objId Id de l'objet
    * @param e Entite (Echantillon ou ProdDerive)
    * @param rId id du retour en cours de modification (afin de l'exclure du resultat)
    * @return Liste de retours
    * @since 2.0.10
    */
   List<Retour> findByObjDates(Calendar dt, Integer objId, Entite e, Integer rId);

   /**
    * Recherche les retours pour un objet donné dont l'intervalle de dates 
    * composé par DateSortie - DateRetour est inclu dans l'intervalle de dates 
    * composé par les dates passées en paramètres.
    * Cette méthode sera appelée lors du contrôle de cohérence de dates.
    * @param dt date limite inf
    * @param dt2 date limite sup
    * @param objId Id de l'objet
    * @param e Entite (Echantillon ou ProdDerive)
    * @param rId id du retour en cours de modification (afin de l'exclure du resultat)
    * @return Liste de retours
    * @since 2.0.10
    */
   List<Retour> findByObjInsideDates(Calendar dt, Calendar dt2, Integer objId, Entite e, Integer rId);

   /**
    * Recherche tous les objets ids dont les retours formant l'intervalle de dates 
    * composé par DateSortie - DateRetour contient la date passée en paramètre.
    * Cette méthode sera appelée lors du contrôle de cohérence de dates pour 
    * optimiser l'insertion en batch mode.
    * @param dt
    * @param e
    * @return
    * @since 2.0.10
    */
   List<Integer> findObjIdsByDatesAndEntite(Calendar dt, Entite e);

   /**
    * Recherche objets ids dont les retours formant l'intervalle de dates 
    * composé par DateSortie - DateRetour est inclu dans l'intervalle de dates 
    * composé par les dates passées en paramètres.
    * Cette méthode sera appelée lors du contrôle de cohérence de dates pour 
    * optimiser l'insertion en batch mode.
    * @param dt date limite inf
    * @param dt2 date limite sup
    * @param e Entite (Echantillon ou ProdDerive)
    * @return List<Integer> ids
    * @since 2.0.10
    */
   List<Integer> findObjIdsInsideDatesEntite(Calendar dt, Calendar dt2, Entite es);

   /**
    * Recherche les Retours dont les objets sont passés en paramètres
    * sous la forme découplée objetId et Entite et dont la date de retour est nulle.
    * @param liste objectId id des objets.
    * @return list Retours.
    * @since 2.0.10
    */
   List<Retour> findByObjectsDateRetourEmpty(List<Integer> objectIds, Entite entite);

   /**
    * Recherche les Retours pour l'objet sous la forme découplée objetId et Entite 
    * et pour une valeur d'impact sur qualité true/false.
    * @param objectId
    * @param entite
    * @param impact
    * @return list Retours.
    * @since 2.0.10	 
    * */
   List<Retour> findByObjectAndImpact(Integer objectId, Entite entite, Boolean impact);

   /**
    * Recherche les Retours dont le Collaborateur est passé en param 
    * @param Collaborateur
    * @return list Retours.
    * */
   List<Retour> findByCollaborateur(Collaborateur collaborateur);

}
