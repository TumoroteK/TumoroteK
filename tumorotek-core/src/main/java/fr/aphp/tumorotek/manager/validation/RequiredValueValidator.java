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
package fr.aphp.tumorotek.manager.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Gatsbi validor appliquant de manière dynamique une validation sur les 
 * champs obligatoires définis par un contexte.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class RequiredValueValidator implements Validator {
	
	private final Log log = LogFactory.getLog(RequiredValueValidator.class);
	
	protected Map<Integer, String> chpIdNameMap = new HashMap<Integer, String>();
	private String eNom;
	private List<Integer> requiredFieldChpIds = new ArrayList<Integer>();
	
	public RequiredValueValidator(String _e, List<Integer> _f) {
		super();
		this.eNom = _e != null ? _e.toLowerCase() : null;
		requiredFieldChpIds.addAll(_f);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object target, Errors errs) {
		for (String fName : translateChpIdToFieldName()) {
			try {
				if (!PropertyUtils.getPropertyType(target, fName).isInstance(Collection.class)) { // not collection
				     ValidationUtils.rejectIfEmptyOrWhitespace(errs, fName, eNom + "." + fName + ".empty");
				} else if (((Collection<?>) PropertyUtils.getProperty(target, fName)).isEmpty()) {
					errs.rejectValue(fName, eNom + "." + fName + ".empty");
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				log.warn(e.getMessage());
				log.debug(e);
			}
		}
	}
	
	// surchargée par les validators spécifiques par entite
	protected void initChpIdNameMap() {
	}
	
	/**
	 * Transforme le chpId en nom de la propriété de l'objet 
	 * dont la presénce obligatoire d'une valeur sera vérifiée;
	 * @return liste des noms des propriétés obligatoires.
	 */
	public List<String> translateChpIdToFieldName() {
		List<String> fNames = new ArrayList<String>();
		for(Integer chpId : requiredFieldChpIds) {
			fNames.add(chpIdNameMap.get(chpId));
		}
		return fNames;
	}
	
//	"codeLaboDiv",
//	"ndaDiv",
//	"datePrelDiv",
//	"typeDiv",
//	"sterileDiv",
//	"risquesDiv",
//	"etabPreleveurDiv",
//	"servicePreleveurDiv",
//	"preleveurDiv",
//	"conditTypeDiv",
//	"conditNbrDiv",
//	"conditMilieuDiv",
//
//	"consentDateDiv",
//	"congPrelDiv",
//	"dateDepartDiv",
//	"transporteurDiv",
//	"tempTranspDiv",
//	"congPrelDiv",
//	"dateArriveeDiv",
//	"operateurDiv",
//	"quantiteDiv",
//	"conformeArriveeDiv",
//	"congBiothequeDiv",

}
