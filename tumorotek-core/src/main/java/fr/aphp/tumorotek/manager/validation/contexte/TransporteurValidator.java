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
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 * Validator pour le bean domaine Transporteur (de contexte).
 * Classe creee le 18/11/10.
 * 
 * Regles de validation:
 *  - tous les champs doivent avoir une synthaxe valide.
 * 
 * @author Pierre VENTADOUR
 * @version 2.0
 */
public class TransporteurValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return Transporteur.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// nom non vide
		ValidationUtils
			.rejectIfEmptyOrWhitespace(
			errors, "nom", "transporteur.nom.empty");
		
		// contactNom non vide
		ValidationUtils
			.rejectIfEmptyOrWhitespace(
			errors, "contactNom", "transporteur.contactNom.empty");
		
		Transporteur transporteur = (Transporteur) target;
		// nom valide
		if (transporteur.getNom() != null) {
			if (transporteur.getNom()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("nom", 
						"transporteur.nom.empty");
			}
			if (!transporteur.getNom()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("nom", "transporteur.nom.illegal");
			}
			if (transporteur.getNom().length() > 50) {
				errors.rejectValue("nom", "transporteur.nom.tooLong");
			}
		}
		
		// contactNom valide
		if (transporteur.getContactNom() != null) {
			if (transporteur.getContactNom()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("contactNom", 
						"transporteur.contactNom.empty");
			}
			if (!transporteur.getContactNom()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("contactNom", 
						"transporteur.contactNom.illegal");
			}
			if (transporteur.getContactNom().length() > 50) {
				errors.rejectValue("contactNom", 
						"transporteur.contactNom.tooLong");
			}
		}
		
		// contactPrenom valide
		if (transporteur.getContactPrenom() != null) {
			if (transporteur.getContactPrenom()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("contactPrenom", 
						"transporteur.contactPrenom.empty");
			}
			if (!transporteur.getContactPrenom()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("contactPrenom", 
						"transporteur.contactPrenom.illegal");
			}
			if (transporteur.getContactPrenom().length() > 50) {
				errors.rejectValue("contactPrenom", 
						"transporteur.contactPrenom.tooLong");
			}
		}
		
		// contactTel valide
		if (transporteur.getContactTel() != null) {
			if (transporteur.getContactTel()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("contactTel", 
						"transporteur.contactTel.empty");
			}
			if (!transporteur.getContactTel()
					.matches(ValidationUtilities.CODEREGEXP)) {
				errors.rejectValue("contactTel", 
						"transporteur.contactTel.illegal");
			}
			if (transporteur.getContactTel().length() > 15) {
				errors.rejectValue("contactTel", 
						"transporteur.contactTel.tooLong");
			}
		}
		
		// contactFax valide
		if (transporteur.getContactFax() != null) {
			if (transporteur.getContactFax()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("contactFax", 
						"transporteur.contactFax.empty");
			}
			if (!transporteur.getContactFax()
					.matches(ValidationUtilities.CODEREGEXP)) {
				errors.rejectValue("contactFax", 
						"transporteur.contactFax.illegal");
			}
			if (transporteur.getContactFax().length() > 15) {
				errors.rejectValue("contactFax", 
						"transporteur.contactFax.tooLong");
			}
		}
		
		// contactMail valide
		if (transporteur.getContactMail() != null) {
			if (transporteur.getContactMail()
					.matches(ValidationUtilities.ONLYSPACESREGEXP)) {
				errors.rejectValue("contactMail", 
						"transporteur.contactMail.empty");
			}
			if (!transporteur.getContactMail()
					.matches(ValidationUtilities.MAILREGEXP)) {
				errors.rejectValue("contactMail", 
						"transporteur.contactMail.illegal");
			}
			if (transporteur.getContactMail().length() > 100) {
				errors.rejectValue("contactMail", 
						"transporteur.contactMail.tooLong");
			}
		}
	}
	
}
