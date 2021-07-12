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
package fr.aphp.tumorotek.manager.validation.coeur.prelevement.gastbi;

import java.util.List;
import fr.aphp.tumorotek.manager.validation.RequiredValueValidator;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * Gatsbi validor appliquant de manière dynamique une validation sur les 
 * champs obligatoires définis par un contexte.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class PrelevementGatsbiValidator extends RequiredValueValidator {
	

	public PrelevementGatsbiValidator(String _e, List<Integer> _f) {
		super(_e, _f);		
	}

	@Override
	public boolean supports(final Class<?> clazz){
		return Prelevement.class.equals(clazz);
	}


	@Override
	protected void initChpIdNameMap() {
		// chpIdNameMap.put(23, "code"); 
		chpIdNameMap.put(45, "numeroLabo");
		// chpIdNameMap.put(24, "nature"); // validé par défaut dans checkrequired
		chpIdNameMap.put(44, "patientNda");
		chpIdNameMap.put(30, "datePrelevement");
		chpIdNameMap.put(31, "prelevementType");
		// sterile (boolean) ne peut être obligatoire
		chpIdNameMap.put(249, "risques");
		// etab preleveur non persisté
		chpIdNameMap.put(29, "servicePreleveur");
		chpIdNameMap.put(28, "preleveur");
		chpIdNameMap.put(32, "conditType");
		chpIdNameMap.put(34, "conditNbr");
		chpIdNameMap.put(33, "conditMilieu");
		// chpIdNameMap.put(26, "consentType"); // validé par défaut dans checkrequired
		chpIdNameMap.put(27, "consentDate");
		// congPrel (boolean) ne peut être obligatoire
		chpIdNameMap.put(35, "dateDepart");
		chpIdNameMap.put(36, "transporteur");
		chpIdNameMap.put(37, "transportTemp");
		chpIdNameMap.put(38, "dateArrivee");
		chpIdNameMap.put(39, "operateur");
		chpIdNameMap.put(40, "quantite");
		// congBiotheque (boolean) ne peut être obligatoire
		chpIdNameMap.put(256, "conformeArrivee");
	}
}