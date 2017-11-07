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
package fr.aphp.tumorotek.manager.validation.code;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.CodeCommon;

/**
 * Validator pour les beans de domaine héritant CodeCommon.<br>
 * Classe creee le 21/05/10<br>
 * <br>
 * Regles de validation:<br>
 * 	- le code nom doit etre non vide, non null, et valide litteralement et 
 * 		de taille inferieure à 25<br>
 * - le libelle nom doit etre null ou non vide, et valide litteralement et 
 * 		de taille inferieure à 150<br>
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class CodeCommonValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CodeCommon.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errs) {
		
		CodeCommon code = (CodeCommon) obj; 	

		//code non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errs, "code", 
											"code.code.empty");
		//code valide
		if (code.getCode() != null) {
			if (!code.getCode()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errs.rejectValue("code", "code.code.illegal");
			}
			if (code.getCode().length() > 50) {
				errs.rejectValue("code", "code.code.tooLong");
			}
		}
		
		//libelle valide
		if (code.getLibelle() != null) {
			ValidationUtils
			.rejectIfEmptyOrWhitespace(errs, "libelle", 
										"code.libelle.empty");
			if (!code.getLibelle()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errs.rejectValue("libelle", "code.libelle.illegal");
			}
			if (code.getLibelle().length() > 300) {
				errs.rejectValue("libelle", "code.libelle.tooLong");
			}
		}
		
		if (code instanceof CodeAssigne) {
			if (((CodeAssigne) code).getIsMorpho() != null 
					&& ((CodeAssigne) code).getIsOrgane() != null) {
				if (((CodeAssigne) code).getIsMorpho()
					&& ((CodeAssigne) code).getIsOrgane()) {
						errs.rejectValue("isOrgane", 
											"code.bothIsOrganeAndIsMorpho");
				}
			}
			// table codage non required mais implique codeRefId
//			if (!((((CodeAssigne) code).getTableCodage() != null 
//					&& ((CodeAssigne) code).getCodeRefId() != null)
//				|| (((CodeAssigne) code).getTableCodage() == null 
//						&& ((CodeAssigne) code).getCodeRefId() == null))) {
//				errs.rejectValue("codeRefId", "code.codeRefIDOrTableEmpty");
//			}
		}
	}	
}
