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
package fr.aphp.tumorotek.manager.io.utils;

import java.util.List;

import fr.aphp.tumorotek.manager.io.TKAnnotableObjectDuo;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectPropertyDuo;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
* Manager proposant les méthodes de traitement appliquées sur un 
* de TKAnnotableObjects: comparaison, merge.
* Date: 15/06/2015
*
* @author Mathieu BARTHELEMY
* @version 2.0.12
*/
public interface TKAnnotableDuoManager {

	/**
	 * Compare pour la liste de champ entite et de champ d'annotations passées en 
	 * paramètres les deux objets TKAnnotableObject composant le duo.
	 * Renvoie une liste de duos composés par les valeurs pour un champ 
	 * dont la comparaison a montré une divergence.
	 * @param duo TKAnnotableObjectDuo
	 * @param chpEntites
	 * @param chpAnnos
	 * @return liste TKAnnotableObjectPropertyDuo
	 */
	List<TKAnnotableObjectPropertyDuo> compareObjectsDuoManager(TKAnnotableObjectDuo duo, 
							List<ChampEntite> chpEntites, List<ChampAnnotation> chpAnnos);

	/**
	 * Applique pour chaque duo de valeurs contenu dans la liste 
	 * de TKAnnotableObjectPropertyDuo passée en paramètre, donc pour chaque champ 
	 * entité ou annotation réprésenté par ces duos de valeurs, l'écrasement de la valeur
	 * de l'objet existant (secondObj) par la valeur de l'objet nouvellement composé 
	 * (firstObject) 
	 * @param duo TKAnnotableObjectDuo
	 * @param liste TKAnnotableObjectPropertyDuo
	 * @return true si il y a eu au moins une modification appliquée.
	 */
	Boolean mergeDuoObjectsManager(TKAnnotableObjectDuo duo, 
										List<TKAnnotableObjectPropertyDuo> propDuos);

}
