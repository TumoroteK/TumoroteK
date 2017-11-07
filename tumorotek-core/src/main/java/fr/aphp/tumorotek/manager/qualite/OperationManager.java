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

import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le manager du bean de domaine Operation.
 * Interface créée le 14/10/09.
 * 
 * Actions:
 * 	- Enregistrer une operation a partir d'un Objet (controle de doublons)
 *  - Afficher toutes les Operations
 *  - Afficher les operations pour un objet
 *  - Afficher les operations pour un utilisateur
 *  - Supprimer une operation
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public interface OperationManager {
	
	/**
	 * Persiste une instance afin de l'enregistrer dans la base de données.
	 * @param operation Operation a creer
	 * @param utilisateur Utilisateur associe (non null)
	 * @param operationType OperationType associee (non null)
	 * @param entite Entite associee (non null)
	 */
	void createObjectManager(Operation operation, 
							Utilisateur utilisateur,
							OperationType operationType,
							Object obj);
	
	/**
	 * Recherche toutes les instances de Operation présentes dans la base.
	 * @return List contenant les Operation.
	 */
	List<Operation> findAllObjectsManager();
	
	/**
	 * Cherche les doublons en se basant sur la methode equals()
	 * surchargee par les entites. 
	 * @param objet Operation dont on cherche la presence dans la base
	 * @return true/false
	 */
	boolean findDoublonManager(Operation operation);
	
	/**
	 * Supprime un objet de la base de données.
	 * @param objet Operation à supprimer de la base de données.
	 */
	void removeObjectManager(Operation operation);
	
	/**
	 * Recherche tous les Operations associes a un utilisateur
	 * passe en parametre.
	 * @param utilisateur Utilisateur
	 * @return Liste de Operation.
	 */
	List<Operation> findByUtilisateurManager(Utilisateur utilisateur);
	
	/**
	 * Recherche tous les Operations associes a un Object de domaine
	 * passe en parametre.
	 * @param obj Objet de domaine
	 * @return Liste de Operation.
	 */
	List<Operation> findByObjectManager(Object obj);
	
	/**
	 * Recherche tous les Operations associes a un Object de domaine
	 * passe en parametre afin d'afficher sopn historique : les 
	 * opérations de type login et logout sont exclues.
	 * @param obj Objet de domaine
	 * @return Liste de Operation.
	 */
	List<Operation> findByObjectForHistoriqueManager(Object obj);
	

	/**
	 * Recherche la date de création de l'objet passé en paramètres.
	 * @param obj Un Object pour lequel on cherche la date de
	 * creation.
	 * @return Date de création de l'objet.
	 */
	Calendar findDateCreationManager(Object obj);
	
	/**
	 * Recherche l'opération de création d'un objet.
	 * @param obj Objet pour lequel on recherche l'op de création.
	 * @return Operation.
	 */
	Operation findOperationCreationManager(Object obj);
	
	/**
	 * Recherche les opérations produites à une date.
	 * @param date Date.
	 * @return Liste d'opérations
	 */
	List<Operation> findByDateOperationManager(Calendar date);
	
	/**
	 * Recherche les opérations produites après une date.
	 * @param date Date.
	 * @return Liste d'opérations
	 */
	List<Operation> findAfterDateOperationManager(Calendar date);
	
	/**
	 * Recherche les opérations produites avant une date.
	 * @param date Date.
	 * @return Liste d'opérations
	 */
	List<Operation> findBeforeDateOperationManager(Calendar date);
	
	/**
	 * Recherche les opérations produites entre deux dates.
	 * @param date1 Première date.
	 * @param date2 Deuxième date.
	 * @return Liste d'Operations.
	 */
	List<Operation> findBetweenDatesOperationManager(Calendar date1,
			Calendar date2);

	/**
	 * Crée un objet phantom et l'opération de suppression 
	 * qui lui est associée.
	 * @param obj
	 * @param comments Commentaires associé à l'objet.
	 * @param utilisateur rélaisant la suppression.
	 */
	void createPhantomManager(TKFantomableObject obj, String comments, 
													Utilisateur user);

	void removeAssociateOperationsManager(Object obj, 
									String comments, Utilisateur user);
	
	/**
	 * Recherche les opérations en fonctions de divers critères.
	 * @param operateurDate1
	 * @param date1
	 * @param operateurDate2
	 * @param date2
	 * @param operationType
	 * @param List<Utilisateur> users
	 * @param showLogin Si true, affichera les opréations de connexion.
	 * @return
	 */
	List<Operation> findByMultiCriteresManager(String operateurDate1,
			Calendar date1, 
			String operateurDate2, Calendar date2, 
			OperationType operationType, List<Utilisateur> users,
			boolean showLogin);

	/**
	 * Trouve les operations d'un type donné pour l'objet 
	 * passé en paramètres.
	 * @param obj
	 * @param oType
	 * @return une liste d'operations
	 */
	List<Operation> findByObjetIdEntiteAndOpeTypeManager(Object obj,
			OperationType oType);

	/**
	 * Trouve une opération effectuée par un utilisateur pour un type 
	 * donnée. Utilise le positionnement dans la liste parmi les opérations 
	 * renvoyées. Ex: 2 avec un ordre DESC sur date renvoie l'avant dernière.
	 * @param operationType
	 * @param user
	 * @param pos 
	 * @return la dernière Operation
	 */
	Operation findLastByUtilisateurAndTypeManager(OperationType operationType,
			Utilisateur user, int pos);

	/**
	 * Sauve en batch mode une liste d'operations de type identique pour une liste d'objets 
	 * identifiés par leurs ids et leur entite.
	 * Utilise jdbc dans une JPA transaction pour plus de rapidité grace 
	 * au JPATransactionManager
	 * @see http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/orm/jpa/JpaTransactionManager.html
	 * @param objsId ids des objets
	 * @param u Utilisateur réalisant l'opération
	 * @param oT type opération
	 * @param cal date opération
	 * @param e entité des objets
	 * @since 2.0.10
	 */
	void batchSaveManager(List<Integer> objsId, Utilisateur u,
			OperationType oT, Calendar cal, Entite e);
	
	
}

