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
package fr.aphp.tumorotek.manager.systeme;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.model.TKFileSettableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 *
 * Interface pour le manager du bean de domaine Fichier.
 * Interface créée le 23/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.2.0
 *
 */
public interface FichierManager
{

   /**
    * Recherche un Fichier dont l'identifiant est passé en paramètre.
    * @param FichierId Identifiant du path que l'on recherche.
    * @return Un Fichier.
    */
   Fichier findByIdManager(Integer fichierId);

   /**
    * Recherche tous les Fichiers présents dans la base.
    * @return Liste de Fichiers.
    */
   List<Fichier> findAllObjectsManager();

   /**
    * Recherche tous les Fichiers dont la valeur commence
    * comme celle passée en paramètre.
    * @param Fichier Fichier que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Fichier.
    */
   List<Fichier> findByPathLikeManager(String path, boolean exactMatch);

   /**
    * Recherche les doublons du Fichier passé en paramètre.
    * @param path Un Fichier pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Fichier path);

   /**
    * Test si un Fichier est lié à des échantillons.
    * @param path Fichier que l'on souhaite tester.
    * @return Vrai si le path est utilisé.
    */
   Boolean isUsedObjectManager(Fichier path);

   /**
    * Test le path d'un Fichier est partagé par plusieurs fichiers et 
    * donc référencé par plusieurs échantillons ou annotations.
    * @param path Fichier que l'on souhaite tester.
    * @return Vrai si le nb de refs est sup à 1.
    */
   Boolean isPathSharedManager(Fichier path);

   /**
    * Persist une instance de Fichier dans la base de données.
    * Cree un fichier dans le systeme.
    * @param path Nouvelle instance de l'objet à créer.
    * @param stream InputStream contenant le fichier
    * @param liste des fichiers qui ont été crées sur le disque
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   void createObjectManager(Fichier path, InputStream stream, List<File> filesCreated);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * Modifie ou non le contenu du fichier dans le systeme.
    * @param fichier Objet à mettre à jour dans la base.
    * @param stream InputStream contenant le fichier
    * @param liste des fichiers qui ont été crées sur le disque
    * @param liste des fichiers qui seront supprimés du disque
    * @return 
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   Fichier updateObjectManager(Fichier path, InputStream stream, List<File> filesCreated, List<File> filesToDelete);

   /**
    * Supprime un Fichier de la base de données.
    * Supprime ou non le fichier dans le systeme
    * @param path Fichier à supprimer de la base de données.
    * @param liste des fichiers qui seront supprimés du disque
    * @throws DoublonFoundException Lance une exception si l'objet
    * est utilisé par des échantillons.
    */
   void removeObjectManager(Fichier path, List<File> filesToDelete);

   /**
    * Cree le fichier par l'utilisation de flux au path spécifié.
    * @param fis InputStream
    * @param path
    * @param Liste des fichiers créées sur disque par opération
    * @return boolean true si creation fichier reussie
    */
   boolean storeFile(InputStream fis, String path, List<File> filesCreated);

   /**
    * Cree, modifie ou suppr la reference vers le fichier pour le TKFileSettableObject 
    * passé en paramètre.
    * Si un stream est fourni, réalise les opérations de création/modification dans le 
    * file system en ajoutant dans la liste filesCreated les objets File créés et 
    * dans la liste filesToDelete les objets File qui seront à supprimés.
    * La liste filesToDelete est parcouru en fin de méthode (quand aucun rollback n'est
    * possible). La liste filesCreated est utilisé lors d'un rollback pour supprimer 
    * les fichiers du file system.
    * @param obj TKFileSettableObject
    * @param fileRef Fichier
    * @param stream contenu à écrire dans le file system
    * @param pathBase path utilisé pour le file system
    * @param filesCreated
    * @param filesToDelete
    */
   void createOrUpdateFileForObject(TKFileSettableObject obj, Fichier fileRef, InputStream stream, String pathBase,
      List<File> filesCreated, List<File> filesToDelete);
   
	/**
	 * [Correctif bug TK-155]
	 * Change le fichier de collection, lors du changement de collection d'un objet et de 
	 * ses annotations.
	 * Met à jour le path et déplace physiquement le fichier dans le système de stockage. 
	 * @param file Fichier
	 * @param dest Collection de destination
	 * @param liste de déplacements (unique) de fichiers à programmer 
	 * @since 2.2.0
	 */
	void switchBanqueManager(Fichier file, Banque dest, Set<MvFichier> filesToMove);

}
