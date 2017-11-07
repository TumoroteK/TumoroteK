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
package fr.aphp.tumorotek.manager.stockage;

import java.util.List;

import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * 
 * Interface pour le manager du bean de domaine Incident.
 * Interface créée le 17/03/10.
 * 
 * @author Pierre Ventadour
 * @version 2.0.10
 *
 */
public interface IncidentManager {
	
	/**
	 * Recherche un Incident dont l'identifiant est passé en paramètre.
	 * @param incidentId Identifiant du Incident que l'on recherche.
	 * @return Un Incident.
	 */
	Incident findByIdManager(Integer incidentId);
	
	/**
	 * Recherche tous les Incidents présents dans la base.
	 * @return Liste d'Incidents.
	 */
	List<Incident> findAllObjectsManager();
	
	/**
	 * Recherche tous les Incidents (ordonnés par date) d'un conteneur.
	 * @param conteneur Conteneur pour lequel on recherche des incidents.
	 * @return Liste d'Incidents.
	 */
	List<Incident> findAllObjectsByConteneurManager(Conteneur conteneur);
	
	/**
	 * Recherche les doublons de l'Incident passé en paramètre.
	 * @param incident Incident pour lequel on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonManager(Incident incident);
	
	/**
	 * Recherche les doublons dans une liste d'Incidents.
	 * @param incidents Liste d'incidents.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonInListManager(List<Incident> incidents);
	
	/**
	 * Persist une instance d'Incident dans la base de données.
	 * @param incident Nouvelle instance de l'objet à créer.
	 * @param conteneur Conteneur de l'incident.
	 * @param conteneur Enceinte de l'incident.
	 * @param conteneur Terminale de l'incident.
	 * @version 2.0.10
	 */
	void createObjectManager(Incident incident, Conteneur conteneur, 
			Enceinte enceinte, Terminale terminale);
	
	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * @param incident Objet à persister.
	 * @param conteneur Conteneur de l'incident.
	 * @param conteneur Enceinte de l'incident.
	 * @param conteneur Terminale de l'incident.
	 * @version 2.0.10
	 */
	void updateObjectManager(Incident incident, Conteneur conteneur,
			Enceinte enceinte, Terminale terminale);
	
	/**
	 * Supprime un Incident de la base de données.
	 * @param incident Incident à supprimer de la base de données.
	 */
	void removeObjectManager(Incident incident);

	/**
	 * Recherche tous les Incidents (ordonnés par date) d'une enceinte.
	 * @param enceinte
	 * @return liste incidents
	 * @since 2.0.10
	 */
	List<Incident> findByEnceinteManager(Enceinte enceinte);

	/**
	 * Recherche tous les Incidents (ordonnés par date) d'une terminale.
	 * @param terminale
	 * @return liste incidents
	 * @since 2.0.10
	 */
	List<Incident> findByTerminaleManager(Terminale terminale);

}
