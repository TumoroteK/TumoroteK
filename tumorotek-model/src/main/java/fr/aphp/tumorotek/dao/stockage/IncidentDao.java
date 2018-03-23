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
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

public interface IncidentDao extends GenericDaoJpa<Incident, Integer>
{

   /**
    * Recherche tous les incidents d'un conteneur ordonnés par date.
    * @param conteneur
    * @return Liste ordonnée des incidents d'un conteneur.
    */
   List<Incident> findByConteneurOrderByDate(Conteneur conteneur);

   /**
    * Recherche tous les incidents d'une enceinte ordonnés par date.
    * @param enceinte
    * @return Liste ordonnée des incidents d'une enceinte.
    * @since 2.0.10
    */
   List<Incident> findByEnceinte(Enceinte enceinte);

   /**
    * Recherche tous les incidents d'une terminale ordonnés par date.
    * @param terminale
    * @return Liste ordonnée des incidents d'une terminale.
    * @since 2.0.10
    */
   List<Incident> findByTerminale(Terminale terminale);

   /**
    * Recherche tous les Incidents d'un conteneur sauf celui dont 
    * l'identifiant est passé en paramètre.
    * @param incidentId Identifiant de l'incident que l'on souhaite 
    * exclure.
    * @param conteneur Conteneur des incidents recherchés.
    * @return une liste d'Incidents.
    */
   List<Incident> findByExcludedIdAndConteneur(Integer incidentId, Conteneur conteneur);

   /**
    * Recherche tous les Incidents d'une enceinte sauf celui dont 
    * l'identifiant est passé en paramètre.
    * @param incidentId Identifiant de l'incident que l'on souhaite 
    * exclure.
    * @param enceinte des incidents recherchés.
    * @return une liste d'Incidents.
    * @since 2.0.10
    */
   List<Incident> findByExcludedIdAndEnceinte(Integer incidentId, Enceinte enceinte);

   /**
    * Recherche tous les Incidents d'une terminale sauf celui dont 
    * l'identifiant est passé en paramètre.
    * @param incidentId Identifiant de l'incident que l'on souhaite 
    * exclure.
    * @param terminale des incidents recherchés.
    * @return une liste d'Incidents.
    * @since 2.0.10
    */
   List<Incident> findByExcludedIdAndTerminale(Integer incidentId, Terminale terminale);

}
