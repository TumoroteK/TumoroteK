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
package fr.aphp.tumorotek.manager.validation.contexte;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.contexte.Coordonnee;

/**
 * Validator pour le bean domaine Coordonnee (de contexte).
 * Classe creee le 05/01/10.
 * 
 * Regles de validation:
 *  - tous les champs doivent avoir une synthaxe valide.
 * 
 * @author Pierre VENTADOUR
 * @version 2.0
 */
public class CoordonneeValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return Coordonnee.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Coordonnee coord = (Coordonnee) target;
		// adresse valide
		if (coord.getAdresse() != null) {
			if (coord.getAdresse()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("adresse", "coordonnee.adresse.empty");
			}
			if (coord.getAdresse().length() > 250) {
				errors.rejectValue("adresse", "coordonnee.adresse.tooLong");
			}
		}
		
		// cp valide
		if (coord.getCp() != null) {
			if (coord.getCp()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("cp", "coordonnee.cp.empty");
			}
			if (!coord.getCp()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("cp", "coordonnee.cp.illegal");
			}
			if (coord.getCp().length() > 10) {
				errors.rejectValue("cp", "coordonnee.cp.tooLong");
			}
		}
		
		// ville valide
		if (coord.getVille() != null) {
			if (coord.getVille()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("ville", "coordonnee.ville.empty");
			}
			if (!coord.getVille()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("ville", "coordonnee.ville.illegal");
			}
			if (coord.getVille().length() > 20) {
				errors.rejectValue("ville", "coordonnee.ville.tooLong");
			}
		}
		
		// pays valide
		if (coord.getPays() != null) {
			if (coord.getPays()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("pays", "coordonnee.pays.empty");
			}
			if (!coord.getPays()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("pays", "coordonnee.pays.illegal");
			}
			if (coord.getPays().length() > 20) {
				errors.rejectValue("pays", "coordonnee.pays.tooLong");
			}
		}
		
		// tel valide
		if (coord.getTel() != null) {
			if (coord.getTel()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("tel", "coordonnee.tel.empty");
			}
			if (!coord.getTel()
					.matches(ValidationUtilities.CODEREGEXP)) {
				errors.rejectValue("tel", "coordonnee.tel.illegal");
			}
			if (coord.getTel().length() > 15) {
				errors.rejectValue("tel", "coordonnee.tel.tooLong");
			}
		}
		
		// fax valide
		if (coord.getFax() != null) {
			if (coord.getFax()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("fax", "coordonnee.fax.empty");
			}
			if (!coord.getFax()
					.matches(ValidationUtilities.CODEREGEXP)) {
				errors.rejectValue("fax", "coordonnee.fax.illegal");
			}
			if (coord.getFax().length() > 15) {
				errors.rejectValue("fax", "coordonnee.fax.tooLong");
			}
		}
		
		// mail valide
		if (coord.getMail() != null) {
			if (coord.getMail()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("mail", "coordonnee.mail.empty");
			}
			if (!coord.getMail()
					.matches(ValidationUtilities.MAILREGEXP)) {
				errors.rejectValue("mail", "coordonnee.mail.illegal");
			}
			if (coord.getMail().length() > 100) {
				errors.rejectValue("mail", "coordonnee.mail.tooLong");
			}
		}
		
	}

}
