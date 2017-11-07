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
package fr.aphp.tumorotek.manager.imprimante;

import java.util.List;

import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

/**
 * 
 * Interface pour le manager du bean de domaine LigneEtiquette.
 * Interface créée le 09/06/2011.
 * 
 * @author Pierre Ventadour 
 * @version 2.0
 *
 */
public interface LigneEtiquetteManager {
	
	/**
	 * Recherche une LigneEtiquette dont l'identifiant est 
	 * passé en paramètre.
	 * @param ligneEtiquetteId Id de la ligne que l'on recherche.
	 * @return Une LigneEtiquette.
	 */
	LigneEtiquette findByIdManager(Integer ligneEtiquetteId);
	
	/**
	 * Recherche toutes les LigneEtiquettes présentes dans la base.
	 * @return Liste de LigneEtiquettes.
	 */
	List<LigneEtiquette> findAllObjectsManager();
	
	/**
	 * Recherche les LigneEtiquettes dont le modèle est passé en paramètre.
	 * @param modele Modele des lignes que l'on recherche.
	 * @return Une liste de LigneEtiquettes.
	 */
	List<LigneEtiquette> findByModeleManager(Modele modele);
	
	/**
	 * Valide une instance de LigneEtiquette dans la base de données.
	 * @param ligneEtiquette Nouvelle instance à valider.
	 * @param modele Modele.
	 * @param champLigneEtiquettes ChampLigneEtiquettes.
	 */
	void validateObjectManager(LigneEtiquette ligneEtiquette,
			Modele modele,
			List<ChampLigneEtiquette> champLigneEtiquettes,
			String operation);
	
	/**
	 * Persist une instance de LigneEtiquette dans la base de données.
	 * @param ligneEtiquette Nouvelle instance de l'objet à créer.
	 * @param modele Modele.
	 * @param champLigneEtiquettes ChampLigneEtiquettes.
	 */
	void createObjectManager(LigneEtiquette ligneEtiquette, 
			Modele modele,
			List<ChampLigneEtiquette> champLigneEtiquettes);
	
	/**
	 * Persist une instance de LigneEtiquette dans la base de données.
	 * @param ligneEtiquette Instance de l'objet à maj.
	 * @param modele Modele.
	 * @param champLigneEtiquettestoCreate ChampLigneEtiquettes 
	 * à persister.
	 * @param champLigneEtiquettesToremove ChampLigneEtiquettes 
	 * à supprimer.
	 */
	void updateObjectManager(LigneEtiquette ligneEtiquette, 
			Modele modele,
			List<ChampLigneEtiquette> champLigneEtiquettesToCreate,
			List<ChampLigneEtiquette> champLigneEtiquettesToremove);
	
	/**
	 * Supprime une LigneEtiquette de la base de données.
	 * @param ligneEtiquette Ligne à supprimer de la base de données.
	 */
	void removeObjectManager(LigneEtiquette ligneEtiquette);

}
