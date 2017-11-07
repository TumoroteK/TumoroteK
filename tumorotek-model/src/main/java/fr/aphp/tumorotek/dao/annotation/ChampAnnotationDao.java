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
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Interface pour le DAO du bean de domaine ChampAnnotation.
 * Interface créée le 28/01/10.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface ChampAnnotationDao 
					extends GenericDaoJpa<ChampAnnotation, Integer> {
	
	
	/**
	 * Recherche les champs dont le nom est 'like' le paramètre.
	 * Les champs retournés sont triés par nom.
	 * @param nom Nom des champs recherchés.
	 * @return Liste de ChampAnnotation.
	 */
	List<ChampAnnotation> findByNom(String nom);
	
	/**
	 * Recherche les champs dont la table est passée en paramètre.
	 * Les champs retournés sont triés par leur ordre.
	 * @param table TableAnnotation à laquelle les champs appartiennent.
	 * @return Liste de ChampAnnotation.
	 */
	List<ChampAnnotation> findByTable(TableAnnotation table);
	
	/**
	 * Recherche tous les champs sauf celui dont l'id est passé 
	 * en paramètre.
	 * @param champAnnotationId Identifiant du champ que l'on souhaite
	 * exclure de la liste retournée.
	 * @return une liste de Champs.
	 */
	List<ChampAnnotation> findByExcludedId(Integer champAnnotationId);
	
	/**
	 * Recherche tous les champs qui sont editables par l'utilisateur 
	 * pour la table passée en paramètre.
	 * @param tableAnnotation
	 * @return une liste de Champs.
	 */
	List<ChampAnnotation> findByEditByCatalogue(TableAnnotation tab);
	
	/**
	 * Trouve les champs d'une table en fonction de leur types.
	 * @param table annotation
	 * @param data type recherché
	 * @return liste de champ annotation du type demandé.
	 */
	List<ChampAnnotation> findByTableAndType(TableAnnotation table, 
															DataType type);
	
	/**
	 * Recherche les critères associés à un champ annotation.
	 * @param champ annotation
	 * @return une liste de critères.
	 */
	List<Critere> findCriteresByChampAnnotation(ChampAnnotation chp);
	
	/**
	 * Recherche les résultats associés à un champ annotation.
	 * @param champ annotation
	 * @return une liste de résultats.
	 */
	List<Resultat> findResultatsByChampAnnotation(ChampAnnotation chp);
	
	/**
	 * Recherche les ChampLigneEtiquettes associées à un champ annotation.
	 * @param champ annotation
	 * @return une liste de ChampLigneEtiquettes.
	 */
	List<ChampLigneEtiquette> 
			findChpLEtiquetteByChampAnnotation(ChampAnnotation chp);
	
	/**
	 * Recherche les colonnes associées à un champ annotation.
	 * @param champ annotation
	 * @return une liste d'ImportColonne.
	 */
	List<ImportColonne> 
		findImportColonnesByChampAnnotation(ChampAnnotation chp);
	
	/**
	 * Recherche tous les champs annotations associés à un template 
	 * d'importation pour une entité.
	 * @param template
	 * @param entite
	 * @return List ChampAnnotation
	 * @since 2.0.12
	 */
	List<ChampAnnotation> findByImportTemplateAndEntite(ImportTemplate ip,
			Entite e);

}
