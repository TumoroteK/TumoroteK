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
package fr.aphp.tumorotek.manager.validation.coeur.annotation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;

/**
 * Validator pour le bean domaine ChampAnnotation.<br>
 * Classe creee le 05/02/10<br>
 * <br>
 * Regles de validation:<br>
 * 	- le champ nom doit etre non vide, non null, et valide litteralement et 
 * 		de taille inferieure à 50<br>
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class ChampAnnotationValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return ChampAnnotation.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errs) {
		
		ChampAnnotation champ = (ChampAnnotation) obj; 	

		//nom non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errs, "nom", 
											"champAnnotation.nom.empty");
		//nom valide
		if (champ.getNom() != null) {
			if (!champ.getNom()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errs.rejectValue("nom", "champAnnotation.nom.illegal");
			}
			if (champ.getNom().length() > 100) {
				errs.rejectValue("nom", "champAnnotation.nom.tooLong");
			}
		}
			
		// verifie Boolean, File et combine
		if (champ.getDataType() != null) {
			if ((champ.getDataType().getType().equals("boolean") 
					|| champ.getDataType().getType().equals("fichier"))  
					&& (champ.getCombine() != null && champ.getCombine())) {
				errs.rejectValue("combine", 
							"champAnnotation.combine.notWithbooleanOrFichier");
			}
		}	
	}	
}