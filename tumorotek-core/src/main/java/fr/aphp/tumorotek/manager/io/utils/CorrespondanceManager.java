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

import fr.aphp.tumorotek.model.io.export.Affichage;

public interface CorrespondanceManager {
	
	/**
	 * Recupere une liste d'Entites depuis une autre liste d'Entites (d'un autre
	 * typage).	 * 
	 * @param sources
	 *            Liste d'Entites sources.
	 * @param cible
	 *            Nom de la classe paramètre de la liste destinatrice.
	 * @throws IllegalArgumentException
	 *             si le paramètre de la liste d'Entites ou le paramètre "cible"
	 *             n'est pas égale à Patient, Maladie, Prelevement, Echantillon
	 *             ou ProdDerive
	 */
	List<Object> recupereEntitesViaDAutres(List<Object> sources, String cible);
	
	/**
	 * Recupere si besoin une liste d'Entites pour l'Affichage.	 * 
	 * @param liste
	 *            Liste d'Entites sources.
	 * @param affichage
	 *            Affichage d'objets.
	 * @throws IllegalArgumentException
	 *             si le paramètre de la liste d'Entites ou le paramètre "cible"
	 *             n'est pas égale à Patient, Maladie, Prelevement, Echantillon
	 *             ou ProdDerive
	 */
	List<Object> recupereEntitesPourAffichageManager(List<Object> liste, 
			Affichage affichage);
}
