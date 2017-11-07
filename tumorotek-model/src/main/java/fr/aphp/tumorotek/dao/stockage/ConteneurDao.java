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
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * 
 * Interface pour le DAO du bean de domaine Conteneur.
 * Interface créée le 17/03/10.
 * 
 * @author Pierre Ventadour
 * @version 2.0.13
 *
 */
public interface ConteneurDao extends GenericDaoJpa<Conteneur, Integer> {
	
	/**
	 * Recherche tous les Conteneurs d'une banque ordonnées.
	 * @param banqueId Identifiant de la Banque des Conteneurs 
	 * recherchés.
	 * @return Liste ordonnée de Conteneurs.
	 */
	List<Conteneur> findByBanqueIdWithOrder(Integer banqueId);
	
	/**
	 * Recherche tous les Conteneurs d'une banque en fct de son
	 * code.
	 * @param banqueId Identifiant de la Banque des Conteneurs 
	 * recherchés.
	 * @param code Code du conteneur.
	 * @return Liste ordonnée de Conteneurs.
	 */
	List<Conteneur> findByBanqueIdAndCode(Integer banqueId,
			String code);
	
	/**
	 * Recherche tous les Conteneurs d'une banque ordonnées sauf
	 * celui dont l'ID est passé en paramètre.
	 * @param banqueId Identifiant de la Banque des Conteneurs 
	 * recherchés.
	 * @param conteneurId Id du conteneur à exclure.
	 * @return Liste ordonnée de Conteneurs.
	 */
	List<Conteneur> findByBanqueIdWithExcludedId(
			Integer banqueId, Integer conteneurId);
	
	/**
	 * Recherche tous les Conteneurs initialement crée par une plateforme.
	 * @param plateforme.
	 * @return Liste ordonnée de Conteneurs.
	 */
	List<Conteneur> findByPlateformeOrigWithOrder(Plateforme orig);
	
	/**
	 * Recherche les Conteneurs sauf celui dont l'identifiant 
	 * est en paramètre.
	 * @param id Identifiant du Conteneur à exclure.
	 * @return Liste de Conteneurs.
	 */
	List<Conteneur> findByExcludedId(Integer conteneurId);

	/**
	 * Recherche tous les conteneurs qui sont accessibles à partir 
	 * d'une plateforme, et si ils sont actuellement déja assignés en partage ou pas.
	 * @param Plateforme pf
	 * @param partage true/false
	 * @return Liste de conteneurs.
	 */
	List<Conteneur> findByPartage(Plateforme pf, Boolean partage);
	
	/**
	 * Recherche tous les conteneurs qui sont accessibles à partir 
	 * d'un service.
	 * @param Service serv
	 * @return Liste de conteneurs.
	 */
	List<Conteneur> findByService(Service serv);
	
	/**
	 * Recherche la température de stockage correspondant à un emplacement.
	 * @since 2.0.13
	 * @param Integer emplacementId
	 * @return liste Float
	 */
	List<Float> findTempForEmplacementId(Integer emplacementId);
}
