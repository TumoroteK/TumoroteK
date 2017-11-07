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
package fr.aphp.tumorotek.manager.validation.stockage;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.stockage.Conteneur;

public class ConteneurValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return Conteneur.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Conteneur conteneur = (Conteneur) target; 	

		// code non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "code", 
											"conteneur.code.empty");
		//code valide
		if (conteneur.getCode() != null) {
			if (!conteneur.getCode()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("code", "conteneur.code.illegal");
			}
			if (conteneur.getCode().length() > 5) {
				errors.rejectValue("code", "conteneur.code.tooLong");
			}
		}
		
		// code non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "nom", 
											"conteneur.nom.empty");
		
		//nom valide
		if (conteneur.getNom() != null) {
			if (!conteneur.getNom()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("nom", "conteneur.nom.illegal");
			}
			if (conteneur.getNom().length() > 50) {
				errors.rejectValue("nom", "conteneur.nom.tooLong");
			}
		}
		
		//pièce valide
		if (conteneur.getPiece() != null) {
			if (conteneur.getPiece()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("piece", "conteneur.piece.empty");
			}
			if (!conteneur.getPiece()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("piece", "conteneur.piece.illegal");
			}
			if (conteneur.getPiece().length() > 10) {
				errors.rejectValue("piece", "conteneur.piece.tooLong");
			}
		}
		
		//nbrNiv valide
		if (conteneur.getNbrNiv() != null) {
			if (conteneur.getNbrNiv() < 2) {
				errors.rejectValue("nbrNiv", "conteneur.nbrNiv.illegal");
			}
		}
		
		//nbrEnc valide
		if (conteneur.getNbrEnc() != null) {
			if (conteneur.getNbrEnc() < 0) {
				errors.rejectValue("nbrEnc", "conteneur.nbrEnc.illegal");
			}
		}
		
		//description valide
		if (conteneur.getDescription() != null) {
			if (conteneur.getDescription()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("description", 
						"conteneur.description.empty");
			}
			if (conteneur.getDescription().length() > 250) {
				errors.rejectValue("description", 
						"conteneur.description.tooLong");
			}
		}
		
		
	}
}
