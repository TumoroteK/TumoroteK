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
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationCommon;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;

/**
 * Validator pour le valeurs de contenu commun des beans AnnotationDefaut 
 * et AnnotationValeur.<br>
 * Applique la validation sur les champs ALPHANUM, TEXTE.
 * Classe creee le 06/02/10<br>
 * <br>
 * Regles de validation:<br>
 * - la valeur alphanum doit etre null ou non vide, et valide litteralement et 
 * 		de taille inferieure à 100<br>
 * - la valeur texte doit etre null ou non vide
 * 
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class AnnotationCommonValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AnnotationValeur.class.equals(clazz)
		 	|| AnnotationDefaut.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errs) {
		
		AnnotationCommon anno = (AnnotationCommon) obj; 	
		
//		// valide le contenu
//		DataType dtype = anno.getChampAnnotation().getDataType();
//		
//		if ("alphanum".equals(dtype.getType()) 
//				|| "num".equals(dtype.getType())
//				|| "fichier".equals(dtype.getType()) 
//				|| "hyperlien".equals(dtype.getType())) {
//			if (anno.getAlphanum() == null) {
//				errs.rejectValue("alphanum", "anno.alphanum.empty");
//			}
//		} else if ("boolean".equals(dtype.getType())) {
//			if (anno.getBool() == null) {
//				errs.rejectValue("bool", "anno.bool.empty");
//			}
//		} else if ("date".equals(dtype.getType())) {
//			if (anno.getDate() == null) {
//				errs.rejectValue("date", "anno.date.empty");
//			}
//		} else if ("texte".equals(dtype.getType())) {
//			if (anno.getTexte() == null) {
//				errs.rejectValue("texte", "anno.texte.empty");
//			}
//		} else if (dtype.getType().matches("thesaurusM?")) {
//			if (anno.getItem() == null) {
//				errs.rejectValue("item", "anno.item.empty");
//			}
//		} else {
//			errs.reject("anno.dataType.inconnu");
//		}
		
		
		//alphanum valide
		if (anno.getAlphanum() != null) {
			ValidationUtils
			.rejectIfEmptyOrWhitespace(errs, "alphanum", 
										"anno.alphanum.empty");
			if (anno.getAlphanum().length() > 100) {
				errs.rejectValue("alphanum", "anno.alphanum.tooLong");
			}
			if (anno.getChampAnnotation() != null) {
				if (anno.getChampAnnotation().getDataType().getType()
							.equals("alphanum")) {				
					if (!anno.getAlphanum()
							.matches(ValidationUtilities.MOTREGEXP)) {
						errs.rejectValue("alphanum", "anno.alphanum.illegal");
					}
				} else if (anno.getChampAnnotation().getDataType().getType()
						.equals("num")) {
					if (!anno.getAlphanum().matches("-?[0-9]+(\\.|,)?[0-9]*E?[0-9]*") 
							&& !anno.getAlphanum()
									.equals("system.tk.unknownExistingValue")) {
						errs.rejectValue("alphanum", "anno.num.illegal");
					}
				} else if (anno.getChampAnnotation().getDataType().getType()
						.equals("hyperlien")) {
					if (!anno.getAlphanum()
									.matches(ValidationUtilities.HTTPREGEXP) 
							&& !anno.getAlphanum()
									.equals("system.tk.unknownExistingValue")) {
						errs.rejectValue("alphanum", "anno.hyperlien.illegal");
					}
				}
			}				
		}
		
		//texte valide
		if (anno.getTexte() != null) {
			ValidationUtils
				.rejectIfEmptyOrWhitespace(errs, "texte", "anno.texte.empty");
		}
		
	}	
}