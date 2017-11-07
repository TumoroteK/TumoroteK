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
package fr.aphp.tumorotek.manager.coeur.prelevement;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;


/**
 * 
 * Interface pour le validator du bean de domaine Prelevement.
 * Interface créée le 14/12/10, contrairement aux autres validators,
 * pour beneficier injection dependances (ref vers prelevementManager)
 * 
 * @author Mathieu Barthelemy
 * @version 2.0
 *
 */
public interface PrelevementValidator extends Validator {
	
	/**
	 * Vérifie la cohérence de la date de prélèvement avec la date de 
	 * naissance et la date actuelle.
	 * @param prelevement
	 * @return errors
	 */
	Errors checkDatePrelevementCoherence(Prelevement prelevement);
	
	/**
	 * Vérifie la cohérence de la date de consentement avec la date de 
	 * naissance et la date actuelle.
	 * @param prelevement
	 * @return errors
	 */
	Errors checkDateConsentCoherence(Prelevement prelevement);
		
	
	/**
	 * Parcoure les dates de l'objet prelevement jusqu'a la date de naissance du
	 * patient, pour trouver la date de référence la plus récente.
	 * @param prelevement
	 * @param skipToDatePrel ne cherche pas la date reference 
	 * dans le cheminement du prélèvement, commence directement 
	 * par la date de prelevement
	 * @return object[] couple (date reference, code qui sera utilisé pour l'
	 * internationalisation du message d'erreur)
	 */
	//Object[] findAntRefDateInPrelevement(Prelevement prelevement, 
	//												boolean skipToDatePrel);
	
	/**
	 * Parcoure les dates des objets issus d'un prelevement jusqu'a la date 
	 * actuelle, pour trouver la date de référence la moins 
	 * récente.
	 * Permet la récursivite sur les dérivés de dérivés.
	 * @param prelevement
	 * @return object[] couple (date reference, code qui sera utilisé pour l'
	 * internationalisation du message d'erreur)
	 */
	//Object[] findPostRefDateInEchantillons(Prelevement prelevement);
	
	/**
	 * Vérifie la cohérence de la date de départ avec la date de 
	 * prélèvement ou avec la date de naissance, avec la date d'arrivee
	 * ou la date actuelle.
	 * @param prelevement
	 * @return errors
	 */
	Errors checkDateDepartCoherence(Prelevement prelevement);
	
	/**
	 * Vérifie la cohérence de la date d'arrivee avec la date de 
	 * depart ou la date de prelevement ou la date de naissance, et avec 
	 * la date actuelle.
	 * @param prelevement
	 * @return errors
	 */
	Errors checkDateArriveeCoherence(Prelevement prelevement);

	/**
	 * Parcoure les labos dans l'ordre chronologique pour 
	 * trouver la première date qui servira 
	 * de référence.
	 * @param labos
	 * @return object[] couple (date reference, code qui sera utilisé pour l'
	 * internationalisation du message d'erreur)
	 */
	//Object[] findPostRefDateInLabos(List<LaboInter> labos);
}
