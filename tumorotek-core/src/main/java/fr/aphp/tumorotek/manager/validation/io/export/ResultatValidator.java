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
package fr.aphp.tumorotek.manager.validation.io.export;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.io.export.Resultat;

/**
 * Validator pour le bean domaine Resultat.
 * Classe créée le 05/11/09
 * 
 * Regles de validation:
 * 	- le champ nomColonne doit être non vide, non null
 *  - le champ tri doit être non null
 *  - le champ ordreTri doit être positif et non null
 *  - le champ position doit être positif et non null
 *  - le champ champ doit être non null
 *  - le champ affichage doit être non null
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 */
public class ResultatValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Resultat.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errs) {
		//nomColonne non vide
		ValidationUtils.rejectIfEmptyOrWhitespace(errs, "nomColonne",
				"resultat.nomColonne.empty");
		
		Resultat resultat = (Resultat) obj;
		//nomColonne valide
		if (resultat.getNomColonne() != null) {
			if (!resultat.getNomColonne()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errs.rejectValue("nomColonne", "resultat.nomColonne.illegal");
			}
			if (resultat.getNomColonne().length() > 40) {
				errs.rejectValue("nomColonne", "resultat.nomColonne.tooLong");
			}
		}
		
		//tri valide
		if (resultat.getTri() == null) {
			errs.rejectValue("tri", "resultat.tri.empty");
		}
		
		//ordreTri valide
		if (resultat.getOrdreTri() != null) {
			if (resultat.getOrdreTri() <= 0) {
				errs.rejectValue("ordreTri",
						"resultat.ordreTri.negativeOrZero");
			}
		} else {
			errs.rejectValue("ordreTri", "resultat.ordreTri.empty");
		}
		
		//position valide
		if (resultat.getPosition() != null) {
			if (resultat.getPosition() <= 0) {
				errs.rejectValue("position",
						"resultat.position.negativeOrZero");
			}
		} else {
			errs.rejectValue("position", "resultat.position.empty");
		}
		
		
		//champ valide
		if (resultat.getChamp() == null) {
			errs.rejectValue("champ", "resultat.champ.empty");
		}
		
		//affichage valide
		if (resultat.getAffichage() == null) {
			errs.rejectValue("affichage", "resultat.affichage.empty");
		}
	}
}