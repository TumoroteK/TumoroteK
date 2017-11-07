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
package fr.aphp.tumorotek.manager.validation.coeur.cession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Validator pour le bean domaine Cession.<br>
 * Classe creee le 01/02/10<br>
 * <br>
 * Regles de validation:<br>
 * 	- le champ numero doit etre non non null et positif<br>
 * 	- le champ observations doit etre non vide, valide litteralement et 
 * 		de taille inferieure à 250<br>
 * 
 * @author Pierre VENTADOUR.
 * @version 2.0
 */
public class CessionValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Cession.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Cession cession = (Cession) target;
		
		//numero non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "numero", 
											"cession.numero.empty");
		//numero valide
		if (cession.getNumero() != null) {
			if (!cession.getNumero()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("numero", "cession.numero.illegal");
			}
			if (cession.getNumero().length() > 100) {
				errors.rejectValue("numero", "cession.numero.tooLong");
			}
		}
		
		// observations valide
		if (cession.getObservations() != null) {
			ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "observations", 
									"cession.observations.empty");
			
//			if (cession.getObservations().length() > 250) {
//				errors.rejectValue(
//						"observations", "cession.observations.tooLong");
//			}
		}
		
		// description valide
		if (cession.getDescription() != null) {
			ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "description", 
									"cession.description.empty");
			
//			if (cession.getDescription().length() > 250) {
//				errors.rejectValue(
//						"description", "cession.description.tooLong");
//			}
		}
		
		//titre valide
		if (cession.getEtudeTitre() != null) {
			if (cession.getEtudeTitre()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("etudeTitre", "cession.etudeTitre.empty");
			}
			if (!cession.getEtudeTitre()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("etudeTitre", "cession.etudeTitre.illegal");
			}
			if (cession.getEtudeTitre().length() > 100) {
				errors.rejectValue("etudeTitre", "cession.etudeTitre.tooLong");
			}
		}
		
		//date cohérences
		errors.addAllErrors(checkDemandeDateCoherence(cession));
		errors.addAllErrors(checkValidationDateCoherence(cession));
	}

	
	/**
	 * Vérifie la cohérence de la date de demande de la cession avec les
	 * dates de validation, depart, arrivee, destruction. 
	 * @param maladie
	 * @return Errors
	 */
	public static Errors checkDemandeDateCoherence(Cession cession) {		
		
		BindException errs = new BindException(cession, 
					"fr.aphp.tumorotek.model.cession.Cession");
		
		if (cession.getDemandeDate() != null) {
			if (cession.getValidationDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getDemandeDate(), "demandeDate", 
							cession.getValidationDate(), "date", 
							"validation", "ValidationDate", errs, true);
			} else if (cession.getDepartDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getDemandeDate(), "demandeDate", 
						cession.getDepartDate(), "date", "validation",
											"DepartDateCession", errs, true);
			} else if (cession.getArriveeDate() != null) { 
				ValidationUtilities
					.checkWithDate(cession.getDemandeDate(), "demandeDate", 
						cession.getArriveeDate(), "date", "validation",
											"ArriveeDateCession", errs, true);
			} else if (!cession.getDemandeDate()
						.before(Utils.getCurrentSystemDate())) {
				errs.rejectValue("demandeDate", 
					"date.validation.supDateActuelle");
			}
		}
		return errs;
	}
	
	/**
	 * Vérifie la cohérence de la date de validation de la cession avec les
	 * dates de depart, demande, arrivee. 
	 * @param maladie
	 * @return Errors
	 */
	public static Errors checkValidationDateCoherence(Cession cession) {		
		
		BindException errs = new BindException(cession, 
					"fr.aphp.tumorotek.model.cession.Cession");
		
		if (cession.getValidationDate() != null) {
			// limites inf
			if (cession.getDemandeDate() != null) {
				ValidationUtilities
					.checkWithDate(cession
								.getValidationDate(), "validationDate", 
						cession.getDemandeDate(), "date", "validation",
													"DemandeDate", errs, false);
			} 
			// limites sup
			if (cession.getDepartDate() != null) {
				ValidationUtilities
					.checkWithDate(cession
								.getValidationDate(), "validationDate", 
						cession.getDepartDate(), "date", "validation",
											"DepartDateCession", errs, true);
			} else if (cession.getArriveeDate() != null) { 
				ValidationUtilities
					.checkWithDate(cession
								.getValidationDate(), "validationDate", 
						cession.getArriveeDate(), "date", "validation",
										"ArriveeDateCession", errs, true);
			} else if (!cession.getValidationDate()
							.before(Utils.getCurrentSystemDate())) {
				errs.rejectValue("validationDate", 
					"date.validation.supDateActuelle");
			}
		}
		return errs;
	}
	
	/**
	 * Vérifie la cohérence de la date de départ de la cession avec les
	 * dates de validation, demande, arrivee. 
	 * @param maladie
	 * @return Errors
	 */
	public static Errors checkDepartDateCoherence(Cession cession) {		
		
		BindException errs = new BindException(cession, 
					"fr.aphp.tumorotek.model.cession.Cession");
		
		if (cession.getDepartDate() != null) {
			// limites inf
			if (cession.getValidationDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getDepartDate(), "departDate", 
						cession.getValidationDate(), "date", "validation",
														"ValidationDate", 
															errs, false);
			} else if (cession.getDemandeDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getDepartDate(), "departDate",
						cession.getDemandeDate(), "date", "validation",
												"DemandeDate", errs, false);
			}
			// limites sup
			if (cession.getArriveeDate() != null) { 
				ValidationUtilities
					.checkWithDate(cession.getDepartDate(), "departDate", 
						cession.getArriveeDate(), "date", "validation",
										"ArriveeDateCession", errs, true);
			} else if (!cession.getDepartDate().getTime()
							.before(Utils.getCurrentSystemDate())) {
				errs.rejectValue("departDate", 
									"date.validation.supDateActuelle");
			}
		}
		return errs;
	}
	
	/**
	 * Vérifie la cohérence de la date d'Arrivee de la cession avec les
	 * dates de validation, demande, depart. 
	 * @param maladie
	 * @return Errors
	 */
	public static Errors checkArriveeDateCoherence(Cession cession) {		
		
		BindException errs = new BindException(cession, 
					"fr.aphp.tumorotek.model.cession.Cession");
		
		if (cession.getArriveeDate() != null) {
			// limites inf
			if (cession.getDepartDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getArriveeDate(), "arriveeDate", 
						cession.getDepartDate(), "date", "validation",
										"DepartDateCession", errs, false);
			} else if (cession.getValidationDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getArriveeDate(), "arriveeDate", 
						cession.getValidationDate(), "date", "validation",
														"ValidationDate", 
														errs, false);
			} else if (cession.getDemandeDate() != null) {
				ValidationUtilities
					.checkWithDate(cession.getArriveeDate(), "arriveeDate", 
						cession.getDemandeDate(), "date", "validation",
										"DemandeDate", errs, false);
			}
			
			// limites sup
			if (!cession.getArriveeDate().getTime()
					.before(Utils.getCurrentSystemDate())) {
				errs.rejectValue("arriveeDate", 
								"date.validation.supDateActuelle");
			}
		}
		return errs;
	}
	
	/**
	 * Vérifie la cohérence de la date de destruction de la cession avec les
	 * dates de validation, demande, depart, arrivee. 
	 * @param maladie
	 * @return Errors
	 */
	public static Errors checkDestructionDateCoherence(Cession cession) {		
		
		BindException errs = new BindException(cession, 
					"fr.aphp.tumorotek.model.cession.Cession");
		
		if (cession.getDestructionDate() != null) {
			// limites inf
			if (cession.getValidationDate() != null) {
				ValidationUtilities
					.checkWithDate(cession
								.getDestructionDate(), "destructionDate", 
						cession.getValidationDate(), "date", 
						"validation", "ValidationDate", errs, false);
			} else if (cession.getDemandeDate() != null) {
				ValidationUtilities
					.checkWithDate(cession
								.getDestructionDate(), "destructionDate", 
						cession.getDemandeDate(), "date", "validation", 
													"DemandeDate", errs, false);
			}
			
			// limites sup
			if (!cession.getDestructionDate().getTime()
					.before(Utils.getCurrentSystemDate())) {
				errs.rejectValue("destructionDate", 
								"date.validation.supDateActuelle");
			}
		}
		return errs;
	}
}
