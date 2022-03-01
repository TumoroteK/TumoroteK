package fr.aphp.tumorotek.manager.qualite;

import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public interface OperationManagerOld {

	void createObjectManager(Operation operation, Utilisateur utilisateur, OperationType operationType, Object obj);

	List<Operation> findAllObjectsManager();

	List<Operation> findByUtilisateurManager(Utilisateur utilisateur);

	boolean findDoublonManager(Operation operation);

	List<Operation> findByObjectManager(Object obj);

	List<Operation> findByObjectForHistoriqueManager(Object obj);

	List<Operation> findByObjetIdEntiteAndOpeTypeManager(Object obj, OperationType oType);

	void removeObjectManager(Operation operation);

	/**
	    * Recherche la date de création de l'objet passé en paramètres.
	    * @param obj Un Object pour lequel on cherche la date de
	    * creation.
	    * @return Date de création de l'objet.
	    */
	Calendar findDateCreationManager(Object obj);

	Operation findOperationCreationManager(Object obj);

	List<Operation> findByDateOperationManager(Calendar date);

	List<Operation> findAfterDateOperationManager(Calendar date);

	List<Operation> findBeforeDateOperationManager(Calendar date);

	List<Operation> findBetweenDatesOperationManager(Calendar date1, Calendar date2);

	void createPhantomManager(TKFantomableObject obj, String comments, Utilisateur user);

	void removeAssociateOperationsManager(Object obj, String comments, Utilisateur user);

	List<Operation> findByMultiCriteresManager(String operateurDate1, Calendar date1, String operateurDate2,
			Calendar date2, OperationType operationType, List<Utilisateur> users, boolean showLogin);

	Operation findLastByUtilisateurAndTypeManager(OperationType operationType, Utilisateur user, int pos);

	void batchSaveManager(List<Integer> objsId, Utilisateur u, OperationType oT, Calendar cal, Entite e);

}