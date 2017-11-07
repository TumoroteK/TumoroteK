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
package fr.aphp.tumorotek.manager.validation;

import java.util.List;

import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;

public interface CoherenceDateManager {

	public abstract Object[] findPostRefDateForPatientManager(Patient patient);

	public abstract Object[] findPostRefDateInEchantillonsManager(
			Prelevement prelevement);

	public abstract Object[] findAntRefDateInPrelevementManager(
			Prelevement prelevement, boolean skipToDatePrel);

	@SuppressWarnings("unchecked")
	public abstract Object[] findPostRefDateInLabosManager(List<LaboInter> labos);

	/**
	 * Parcoure les dates des objets précédant un dérivé jusqu'a la date 
	 * de naissance du patient, pour trouver la date de référence la plus 
	 * récente.
	 * Permet la récursivite sur les dérivés de dérivés.
	 * @param dérivé
	 * @paral object parent
	 * @return object[] couple (date reference, code qui sera utilisé pour l'
	 * internationalisation du message d'erreur)
	 */
	public abstract Object[] findAntRefDateForDeriveManager(ProdDerive derive);

	public abstract Object[] findPostRefDateInDerivesManager(Object parent);

}