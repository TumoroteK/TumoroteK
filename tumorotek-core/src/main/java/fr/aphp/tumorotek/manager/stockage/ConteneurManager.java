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
import java.util.Set;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le manager du bean de domaine Conteneur.
 * Interface créée le 23/03/10.
 * 
 * @author Pierre Ventadour
 * @version 2.0.10
 *
 */
public interface ConteneurManager {
	
	/**
	 * Recherche un Conteneur dont l'identifiant est passé en paramètre.
	 * @param conteneurId Identifiant du Conteneur que l'on recherche.
	 * @return Un Conteneur.
	 */
	Conteneur findByIdManager(Integer conteneurId);
	
	/**
	 * Recherche tous les Conteneurs présents dans la base.
	 * @return Liste de Conteneurs.
	 */
	List<Conteneur> findAllObjectsManager();
	
	/**
	 * Recherche tous les Conteneurs d'une banque.
	 * @param banque Banque des Conteneurs que l'on recherche.
	 * @return Liste de Conteneur.
	 */
	List<Conteneur> findByBanqueWithOrderManager(Banque banque);
	
	/**
	 * Recherche tous les Conteneurs d'une banque dont le code
	 * est passé en paramètre.
	 * @param banque Banque des Conteneurs que l'on recherche.
	 * @param code Code du conteneur.
	 * @return Liste de Conteneur.
	 */
	List<Conteneur> findByBanqueAndCodeManager(Banque banque,
			String code);
	
	/**
	 * Recherche tous les Conteneurs d'une liste banque.
	 * @param banques Liste de Banques des Conteneurs que l'on recherche.
	 * @return Liste de Conteneurs.
	 */
	List<Conteneur> findByBanquesWithOrderManager(List<Banque> banques);
	
	/**
	 * Recherche tous les Conteneurs initialement créés par une plateforme.
	 * @param plateforme Plateforme des Conteneurs que l'on recherche.
	 * @return Liste de Conteneurs.
	 */
	List<Conteneur> findByPlateformeOrigWithOrderManager(Plateforme plateforme);
	
	/**
	 * Retourne tous les incidents d'un conteneur.
	 * @param conteneur Conteneur.
	 * @return Set d'incidents.
	 */
	Set<Incident> getIncidentsManager(Conteneur conteneur);
	
	/**
	 * Retourne toutes les Enceintes d'un Conteneur.
	 * @param conteneur Conteneur.
	 * @return Set d'Enceintes.
	 */
	Set<Enceinte> getEnceintesManager(Conteneur conteneur);
	
	/**
	 * Retourne toutes les terminales du conteneur en
	 * parcourant toutes ses enceintes.
	 * @param conteneur Conteneur.
	 * @return Liste de Terminales.
	 */
	List<Terminale> getAllTerminalesInArborescenceManager(
			Conteneur conteneur);
	
	/**
	 * Retourne toutes les banques d'un Conteneur.
	 * @param conteneur Conteneur.
	 * @return Set de banques.
	 */
	Set<Banque> getBanquesManager(Conteneur conteneur);
	
	/**
	 * Retourne toutes les plateformes d'un Conteneur.
	 * @param conteneur Conteneur.
	 * @return Set de ConteneurPlateformes.
	 */
	Set<ConteneurPlateforme> getConteneurPlateformesManager(Conteneur conteneur);
	
	/**
	 * Recherche les doublons du Conteneur passé en paramètre.
	 * @param conteneur Conteneur pour lequel on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonManager(Conteneur conteneur,
			List<Banque> banques);
	
	/**
	 * Teste si le Conteneur passé en paramètre est utilisé par 
	 * d'autres objets.
	 * @param conteneur Conteneur que l'on test.
	 * @return True si l'objet est utilisé.
	 */
	Boolean isUsedObjectManager(Conteneur conteneur);
	
	/**
	 * Persist une instance de Conteneur dans la base de données.
	 * @param conteneur Nouvelle instance de l'objet à créer.
	 * @param conteneurType Type du Conteneur.
	 * @param service Service du Conteneur.
	 * @param banques Banques du Conteneur.
	 * @param plateformes Plateformes du Conteneur.
	 * @param Utilisateur 
	 * @param pf Plateforme origine
	 * @version 2.0.10
	 */
	void createObjectManager(Conteneur conteneur, 
			ConteneurType conteneurType,
			Service service,
			List<Banque> banques,
			List<Plateforme> plateformes,
			Utilisateur utilisateur, 
			Plateforme pfOrig);
	
	/**
	 * Persist une instance de Conteneur dans la base de données.
	 * @param conteneur Nouvelle instance de l'objet à modifier.
	 * @param conteneurType Type du Conteneur.
	 * @param service Service du Conteneur.
	 * @param banques Banques du Conteneur.
	 * @param plateformes Plateformes du Conteneur.
	 * @param incidents Incident du Conteneur.
	 */
	void updateObjectManager(Conteneur conteneur, 
			ConteneurType conteneurType,
			Service service,
			List<Banque> banques,
			List<Plateforme> plateformes,
			List<Incident> incidents,
			Utilisateur utilisateur); 
	
	/**
	 * Supprime un Conteneur de la base de données.
	 * @param conteneur Conteneur à supprimer de la base de données.
	 * @param comments commentaires liés à la suppression
	 * @param Utilisateur réalisant la suppression.
	 * 
	 * @since 2.0.10 si le conteneur fait reference à des évènements de 
	 * stockage, il est automatiquement archivé.
	 */
	void removeObjectManager(Conteneur conteneur, String comments, 
													Utilisateur user);
	
	/**
	 * Crée toute l'arborescence d'un conteneur : dès enceintes filles
	 * jusqu'aux enceintes terminales.
	 * @param conteneur Conteneur de l'arborescence.
	 * @param enceintes Liste d'enceinte. Chaque objet représente un
	 * squelette de subdivision dans l'arborescence.
	 * @param terminale Représente le format des enceintes terminales.
	 * @param Numéros des 1ères positions de chaque élément.
	 * @param banques Banques liées au conteneur.
	 * @param plateformes Plateformes liées au conteneur.
	 * @param contenance du nombre de paillettes (si non null -> conteneur paillettes)
	 * @param nameFromColor si True, le nom des terminales aura pour
	 * préfixe leur couleur.
	 * @param utilisateur
	 * @param Plateforme origine
	 * @since 2.0.10
	 */
	void createAllArborescenceManager(Conteneur conteneur,
			List<Enceinte> enceintes,
			Terminale terminale,
			List<Integer> firstPositions,
			List<Banque> banques,
			List<Plateforme> plateformes,
			Integer sizePaillettes,
			boolean nameFromColor,
			Utilisateur utilisateur, 
			Plateforme pfOrig);

	/**
	 * Recupere une liste d'enceintes contenues par le conteneur.
	 * @param conteneur
	 * @return liste d'enceintes
	 */
	List<Enceinte> getContainingEnceinteManager(Conteneur conteneur);

	/**
	 * Supprime l'association manytomany entre le conteneur et la 
	 * banque ainsi que toute 
	 * reservation associant une enceinte appartenant au conteneur et cette 
	 * même banque.
	 * @param conteneur
	 * @param banque
	 */
	void removeBanqueFromContAndEncManager(Conteneur conteneur, 
															Banque banque);

	/**
	 * Trouve le conteneur à l'origine de 
	 * l'emplacement passé en paramètres.
	 * @param empl Emplacement
	 * @return Conteneur
	 */
	Conteneur findFromEmplacementManager(Emplacement empl);

	/**
	 * Vérifies si des évènements de stockage sont associés à ce 
	 * conteneur.
	 * @param conteneur
	 * @return true si oui.
	 */
	boolean hasRetoursManager(Conteneur conteneur);
	
	/**
	 * Recherche tous les conteneurs qui sont accessibles à partir 
	 * d'une plateforme, et si ils sont actuellement déja assignés en partage ou pas.
	 * @param Plateforme pf
	 * @param partage true/false
	 * @return Liste de conteneurs.
	 */
	List<Conteneur> findByPartageManager(Plateforme pf, Boolean partage);

	/**
	 * Recherche la température de stockage correspondant à un emplacement.
	 * Renvoie null si aucune température ou emplacement null.
	 * @since 2.0.13
	 * @param Emplacement
	 * @return Float
	 */
	Float findTempForEmplacementManager(Emplacement emplacement);
}
