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
package fr.aphp.tumorotek.manager.coeur.annotation;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le Manager du bean de domaine ChampAnnotation.
 * Interface créée le 02/02/10.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface ChampAnnotationManager {
	
	/**
	 * Recherche un ChampAnnotation dont l'identifiant est passé en paramètre.
	 * @param champAnnotationId Identifiant du ChampAnnotation 
	 * que l'on recherche.
	 * @return Un ChampAnnotation.
	 */
	ChampAnnotation findByIdManager(Integer champAnnotationId);
	
	/**
	 * Recherche les champs dont le nom est 'like' le paramètre.
	 * Les champs retournés sont triés par nom.
	 * @param nom Nom des champs recherchés.
	 * @return Liste de ChampAnnotation.
	 */
	List<ChampAnnotation> findByNomManager(String nom);
	
	/**
	 * Recherche les champs dont la table est passée en paramètre.
	 * Les champs retournés sont triés par leur ordre.
	 * @param table TableAnnotation à laquelle les champs appartiennent.
	 * @return Liste de ChampAnnotation.
	 */
	List<ChampAnnotation> findByTableManager(TableAnnotation table);
	
	/**
	 * Récupère la valeur d'un champAnnotation d'une entité.
	 * @param objet Objet lié au champAnnotation.
	 * @param champAnnotation Champ dont on veut connaître la valeur.
	 * @return valeur d'une annotation
	 */
	String findAnnotationValeurManager(Object objet,
			ChampAnnotation champAnnotation);
	
	/**
	 * Récupère les AnnotationValeur d'un ChampAnnotation.
	 * @param champAnnotation ChampAnnotation dont on souhaite obtenir les
	 * AnnotationValeur.
	 * @return Liste des AnnotationValeur du ChampAnnotation.
	 */
	List<AnnotationValeur> findAnnotationValeurByChampAnnotationManager(
			ChampAnnotation ca);
	
	/**
	 * Récupère les ChampAnnotation d'une Entite.
	 * @param entite Entite dont on souhaite obtenir les champs annotation.
	 * @return La liste des champs annotation de l'entité.
	 */
	List<ChampAnnotation> findByEntiteManager(Entite e);
	
	/**
	 * Persiste une instance afin de l'enregistrer dans la base de données.
	 * @param champ ChampAnnotation à creer
	 * @param table TableAnnotation
	 * @param dataType
	 * @param items liste de Items
	 * @param defauts liste d'annotation defaut
	 * @apram banque Banque courante (pour les items de champs de catalogue)
	 * @param operation String creation / modification
	 * @param base directory pour créer le dossier sytème associé.
	 */
	void createOrUpdateObjectManager(ChampAnnotation champ,
								TableAnnotation table,
								DataType dataType,
								List<Item> items,
								List<AnnotationDefaut> defauts,
								Utilisateur utilisateur,
								Banque banque,
								String operation,
								String baseDir
								);
	
	/**
	 * Cherche les doublons en se basant sur la methode equals()
	 * surchargee par les entites. Si l'objet est modifie donc a un id 
	 * attribue par le SGBD, ce dernier est retire de la liste findAll.
	 * @param champ ChampAnnotation dont on cherche la presence dans la base
	 * @return true/false
	 */
	boolean findDoublonManager(ChampAnnotation champ);
	
	/**
	 * Supprime un objet de la base de données.
	 * @param champ ChampAnnotation à supprimer de la base de données.
	 * @param comments commentaires liés à la suppression
	 * @param Utilisateur réalisant la suppression.
	 * @param base directory pour effacer le dossier sytème associé.
	 */
	void removeObjectManager(ChampAnnotation champ, String comments, 
										Utilisateur usr, String baseDir);
	
	/**
	 * Recherche les valeurs liées au champ passé en paramètres.
	 * @param champs pour lequel on recherche les valeurs.
	 * @return Liste de AnnotationValeur.
	 */
	Set<AnnotationValeur> getAnnotationValeursManager(ChampAnnotation champ);
	
	/**
	 * Recherche les valeurs défauts liées au champ passé en paramètres.
	 * @param champs pour lequel on recherche les valeurs.
	 * @return Liste de AnnotationDefaut.
	 */
	Set<AnnotationDefaut> getAnnotationDefautsManager(ChampAnnotation champ);
	
	/**
	 * Recherche les items liés au champ thesaurus passé en paramètres.
	 * @param champs pour lequel on recherche les items.
	 * @param banque pour laquelle on recherche les items associé à 
	 * ce champ de catalogue.
	 * @return Liste de Item.
	 */
	Set<Item> getItemsManager(ChampAnnotation champ, Banque banque);
	
	/**
	 * Recherche toutes les instances de ChampAnnotation présentes dans la base.
	 * @return List contenant les ChampAnnotation.
	 */
	List<ChampAnnotation> findAllObjectsManager();
	
	/**
	 * Recherche toutes les champs dont le nom est egal ou 'like' 
	 * celui passé en parametre.
	 * @param nom
	 * @param boolean exactMatch
	 * @return Liste de ChampAnnotation.
	 */
	List<ChampAnnotation> findByNomLikeManager(String nom, boolean exactMatch);
		
	/**
	 * Monte ou descend l'ordre d'un champ au sein d'une table. 
	 * @param champ ChampAnnotationBanque
	 * @param boolean specifiant up si true
	 */
	void moveChampOrderUpDownManager(ChampAnnotation champ, boolean up);
	
	/**
	 * Calcule la taille du plus long label pour les Items associés à ce 
	 * champ.
	 * @param liste d'items
	 * @return taille du label
	 */
	Integer findMaxItemLength(Set<Item> items);
	
	/**
	 * Cree ou supprime l'arborescence de fichier qui accueillera 
	 * les annotations de type fichier. Si l'arborescence existe deja, 
	 * cree uniquement le dossier final. 
	 * Supprime uniquement le dossier du champ qui recueille les fichiers 
	 * et son contenu.
	 * @param base directory pour créer dossier sytème associé au champ 
	 * annotation de type fichier.
	 * @param champ
	 * @param boolean indiquant deletion ou non.
	 * @param liste de banques pour lesquels il faut créer un dossier qui
	 * recevra les valeurs d'annotations.
	 */
	void createOrDeleteFileDirectoryManager(String baseDir, 
											ChampAnnotation chp, 
											boolean delete, 
											List<Banque> banques);
	
	/**
	 * Renvoie les banques spécifées pour la table à laquelle appartient
	 * le champ passé en paramètre.
	 * @param chp
	 * @return liste de Banques
	 */
	List<Banque> getBanquesFromTableManager(ChampAnnotation chp);
	
	/**
	 * Renvoie tous les champs de la table la table à laquelle appartient
	 * le champ passé en paramètre.
	 * @param chp
	 * @return liste de ChampAnnotation
	 */
	Set<ChampAnnotation> getAllChampsFromTableManager(ChampAnnotation chp);
	
	/**
	 * Recherche tous les champs qui sont editables par l'utilisateur 
	 * pour la table passée en paramètre.
	 * @param tableAnnotation
	 * @return une liste de Champs.
	 */
	List<ChampAnnotation> findByEditByCatalogueManager(TableAnnotation table);
	
	/**
	 * Verifie si un item du thesaurus est utilisé au moins une fois 
	 * par une valeur d'annotation. Si c'est le cas l'item 
	 * ne peut être supprimé.
	 * @param item
	 * @return true si l'item utilisé.
	 */
	boolean isUsedItemManager(Item item);
	
	/**
	 * Vérifie si le champ annotation est référencée à un objet 
	 * CHAMP vers d'autres objets (critère, résultat, import, étiquette).
	 * @param ChampAnnotation c
	 * @return liste d'objets qui référencent le champ annotation
	 */
	List<? extends Object> isUsedObjectManager(ChampAnnotation c);
	
	/**
	 * Trouve tous les champs de type fichier pour la table passée en 
	 * paramètre.
	 * @param table annotation
	 * @return liste de champ annotation type fichier.
	 */
	List<ChampAnnotation> 
			findChampsFichiersByTableManager(TableAnnotation table);
	
	/**
	 * Recherche tous les champs annotations associés à un template 
	 * d'importation pour une entité.
	 * @param template
	 * @param entite
	 * @return List ChampAnnotation
	 * @since 2.0.12
	 */
	List<ChampAnnotation> findByImportTemplateAndEntiteManager(ImportTemplate ip,
			Entite e);
}
