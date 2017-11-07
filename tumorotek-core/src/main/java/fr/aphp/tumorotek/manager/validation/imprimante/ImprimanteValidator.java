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
package fr.aphp.tumorotek.manager.validation.imprimante;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.imprimante.Imprimante;

public class ImprimanteValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Imprimante.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Imprimante imprimante = (Imprimante) target; 	

		// nom non null
		ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "nom", 
									"imprimante.nom.empty");
		//nom valide
		if (imprimante.getNom() != null) {
			if (!imprimante.getNom()
					.matches(ValidationUtilities.MOTREGEXP)) {
				errors.rejectValue("nom", "imprimante.nom.illegal");
			}
			if (imprimante.getNom().length() > 50) {
				errors.rejectValue("nom", "imprimante.nom.tooLong");
			}
		}
		
		//abscisse valide
		if (imprimante.getAbscisse() == null) {
			errors.rejectValue("abscisse", "imprimante.abscisse.empty");
		}
		//ordonnee valide
		if (imprimante.getOrdonnee() == null) {
			errors.rejectValue("ordonnee", "imprimante.ordonnee.empty");
		}
		//largeur valide
		if (imprimante.getLargeur() == null) {
			errors.rejectValue("largeur", "imprimante.largeur.empty");
		}
		//longueur valide
		if (imprimante.getLongueur() == null) {
			errors.rejectValue("longueur", "imprimante.longueur.empty");
		}
		//orientation valide
		if (imprimante.getOrientation() == null) {
			errors.rejectValue("orientation", "imprimante.orientation.empty");
		}
		
		//ip valide
		if (imprimante.getAdresseIp() != null) {
			if (!imprimante.getAdresseIp()
					.matches(ValidationUtilities.IPREGEXP)) {
				errors.rejectValue("adresseIp", "imprimante.adresseIp.illegal");
			}
		}
		
		//port valide
		if (imprimante.getPort() != null) {
			if (imprimante.getPort() < 1 ||imprimante.getPort() > 65535) {
				errors.rejectValue("port", "imprimante.port.illegal");
			}
		}
	}

}
