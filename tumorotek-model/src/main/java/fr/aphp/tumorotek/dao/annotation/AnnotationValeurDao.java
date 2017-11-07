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
package fr.aphp.tumorotek.dao.annotation;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Interface pour le DAO du bean de domaine AnnotationValeur.
 * Interface créée le 01/02/10.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.12
 *
 */
public interface AnnotationValeurDao 
					extends GenericDaoJpa<AnnotationValeur, Integer> {
	
	
	/**
	 * Recherche les valeurs assignées a l'objet pour le champ spécifiés 
	 * en paramètre.
	 * @param champAnnotation
	 * @param objetId
	 * @return Liste de AnnotationDefaut.
	 */
	List<AnnotationValeur> findByChampAndObjetId(ChampAnnotation champ, 
														Integer objetId);
	
	/**
	 * Recherche toutes les valeurs pour le champ et l'objet 
	 * sauf celle dont l'id est passé en paramètre.
	 * @param champAnnotation
	 * @param objetId
	 * @param annotationValeurId Identifiant de la valeur que l'on souhaite
	 * exclure de la liste retournée.
	 * @return Liste de AnnotationValeur.
	 */
	List<AnnotationValeur> findByExcludedId(ChampAnnotation champ, 
								Integer objetId, Integer annotationValeurId);
	
	/**
	 * Recherche toutes les valeurs spécifiées pour une table d'annotations
	 * et pour une banque. 
	 * Cette requete a pour objectif de retourner toutes les valeurs qui seront
	 * supprimées avec la suppression de la référence de ta table vers la
	 * banque.
	 * @param table TableAnnotation
	 * @param banque Banque
	 * @return Liste de AnnotationValeur.
	 */
	List<AnnotationValeur> findByTableAndBanque(TableAnnotation table, 
															Banque bank);
	
	/**
	 * Recherche toutes les valeurs d'annotations pour un objet donné en passant
	 * l'id de objet et son entité.
	 * @param objetId
	 * @param Entité ent
	 * @return Liste de AnnotationValeur.
	 */
	List<AnnotationValeur> findByObjectIdAndEntite(Integer objetId, Entite ent);
	
	/**
	 * Compte les valeurs d'annotations referencant l'item passé en paramètre.
	 * @param item
	 * @return compte
	 */
	List<Long> findCountByItem(Item item);
	
	/**
	 * Compte les valeurs d'annotations renseignée pour une banque et une 
	 * table annotation passées en paramètres.
	 * @param tab TableAnnotation
	 * @param banque Banque
	 * @return compte
	 */
	List<Long> findCountByTableAnnotationBanque(TableAnnotation tab, Banque b);
}
