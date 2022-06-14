/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.validation.coeur.echantillon.gatsbi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;

import fr.aphp.tumorotek.manager.validation.RequiredValueValidator;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 * Gatsbi validor appliquant de manière dynamique une validation sur les 
 * champs obligatoires définis par un contexte.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class EchantillonGatsbiValidator extends RequiredValueValidator {
	
	private List<CodeAssigne> codes = new ArrayList<CodeAssigne>();
	
	// permet de retirer la validation obligatoire du chmp crAnapath (id=255)
	// uniquement pour l'import fichier excel
	private boolean isImport = false;

	public EchantillonGatsbiValidator(String _e, List<Integer> _f, List<CodeAssigne> _c, boolean _i) {
		super(_e, _f);
		
		if (_c != null) {
			codes.addAll(_c);
		}
		this.isImport = _i;
	}

	@Override
	public boolean supports(final Class<?> clazz){
		return Echantillon.class.equals(clazz);
	}

	@Override
	protected void initChpIdNameMap() {
		chpIdNameMap.put(53, "collaborateur");
		// chpIdNameMap.put(54, "code"); 
		// chpIdNameMap.put(58, "echantillonType"); // validé par défaut dans checkrequired
		// TODO vérifier avec CORINNE si besoin validation fonctionnelle ici
		chpIdNameMap.put(61, "quantite");
		chpIdNameMap.put(62, "quantiteInit"); // logiquement rempli si quantite remplie
		chpIdNameMap.put(63, "quantiteUnite"); // logiquement rempli si quantite remplie
		chpIdNameMap.put(70, "modePrepa");
		// sterile id=72 (boolean) ne peut être obligatoire
		chpIdNameMap.put(56, "dateStock");
		chpIdNameMap.put(67, "delaiCgl");
		chpIdNameMap.put(53, "collaborateur");
		// chpIdNameMap.put(55, "statut"); // validé par défaut dans checkrequired
		// emplacement id=57 ne peut être obligatoire
		chpIdNameMap.put(68, "echanQualite");
		chpIdNameMap.put(243, "conformeTraitement");
		chpIdNameMap.put(244, "conformeCession");
		if (!isImport) {
			chpIdNameMap.put(255, "crAnapath");
		}
		// tumoral id=69 (boolean) ne peut être obligatoire
		chpIdNameMap.put(229, "codeOrganes");
		chpIdNameMap.put(60, "lateralite");
		chpIdNameMap.put(230, "codeMorphos");
	}
	
	@Override
	protected void initFunctionalValidationMap() {

		validations.put(255, (TKdataObject e, Errors r) -> 
			(isImport || ((Echantillon) e).getAnapathStream() != null 
				|| ((Echantillon) e).getCrAnapath() != null));
		
		validations.put(229, (TKdataObject e, Errors r) -> 
			codes.stream().anyMatch(c -> c.getIsOrgane()));
		validations.put(230, (TKdataObject e, Errors r) -> 
			codes.stream().anyMatch(c -> c.getIsMorpho()));
	}

	/**
	 * Echantillon impose la transformation des noms de certaines propriétés/champs.
	 */
	@Override
	protected String correctFieldNameIfNeeded(String n) {
		if (n != null && Arrays.asList("codeOrganes", "codeMorphos").contains(n)) {
			return "codesAssignes";
		}
		return n;
	}
}