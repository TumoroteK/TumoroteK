package fr.aphp.tumorotek.manager.validation.coeur.cession.retour;

import java.util.Calendar;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.Retour;

/**
 * Validator pour le bean domaine Retour.<br>
 * Classe creee le 01/04/11<br>
 * <br>
 * Regles de validation:<br>
 * 	- la date de sorte doit être antérieure à la date de retour<br>
 *  - la date de sortie doit être postérieure à la date de stockage<br>
 *  - la date de sortie et la date de retour ne doivent pas être inclus dans un autre
 *  retour ou inclure un autre retour
 * 	- le champ observations doit etre non vide, valide litteralement et 
 * 		de taille inferieure à 250<br>
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0.10
 */
public interface RetourValidator extends Validator {

	/**
	 * Vérifie la cohérence de la date de sortie. 
	 * @param retour
	 * @return Errors
	 */
	public abstract Errors checkDateSortieCoherence(Retour retour);
	
	/**
	 * Vérifie la cohérence de la date de retour. 
	 * @param retour
	 * @return Errors
	 */
	public abstract Errors checkDateRetourCoherence(Retour retour);
	
	/**
	 * Trouve pour l'objet passé en paramètre un (ou plusieurs) retours qui 
	 * inclus dans son intervalle de dates la date passée en paramètre.
	 * @param cal Date 
	 * @param obj TKStockableObject
	 * @param exclId id du retour à exclure (si modification du retour, il sortira 
	 * automatiquement dans la requête)
	 * @return liste retours
	 * @since 2.0.10
	 */
	List<Retour> findByObjDatesManager(Calendar cal, TKStockableObject obj, 
			Integer exclId);
	
	/**
	 * Trouve pour l'objet passé en paramètre un (ou plusieurs) retours qui 
	 * sont inclus dans l'intervalle de dates composés par les deux dates 
	 * passées en paramètres.
	 * @param cal1 limite inf
	 * @param cal2 limite sup
	 * @param obj TKStockableObjetc
	 * @param exclId id du retour à exclure (si modification du retour, il sortira 
	 * automatiquement dans la requête)
	 * @return liste retours
	 * @since 2.0.10
	 */
	List<Retour> findByObjInsideDatesManager(Calendar cal1, Calendar cal2, 
				TKStockableObject obj, Integer exclId);

}