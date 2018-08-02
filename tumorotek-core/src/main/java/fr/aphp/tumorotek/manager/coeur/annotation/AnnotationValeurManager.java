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

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine AnnotationValeur.<br>
 * Interface créée le 06/02/10.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer une valeur (controle de doublons)<br>
 * 	- Modifier une valeur (controle de doublons)<br>
 * 	- Retourner les valeurs par champ et par objet<br>
 * 	- Retourner toutes les valeurs pour une table et une banque<br>
 * 	- Supprimer une valeur
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.0
 * @since 2.0.12
 */
public interface AnnotationValeurManager
{

   void createObject(AnnotationValeur annoVal);

   void updateObject(AnnotationValeur annoVal);

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param valeur AnnotationValeur
    * @param champ ChampAnnotation
    * @param Object objet pour lequel est spécifié la valeur d'annotation 
    * @param banque
    * @param fichier
    * @param utilisateur Utilisateur realisant la creation
    * @param operation String creation / modification
    * @param base directory pour enregistrer l'objet fichier associé 
    * avec le bon path dans le système de fichier associé.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    */
   void createOrUpdateObjectManager(AnnotationValeur valeur, ChampAnnotation champ, TKAnnotableObject obj, Banque banque,
      Fichier fichier, Utilisateur utilisateur, String operation, String baseDir, List<File> filesCreated,
      List<File> filesToDelete);

   /**
    * Enregistre une liste de valeurs d'annotations.
    * Les attributs, champ et fichier doivent être contenus dans 
    * les objets valeurs.
    * Utilise la deep copy de la liste originelle pour mettre
    * l'objet en etat transient (utile lors creation simultanée d'objets
    * avec une seule fiche annotation)
    * @param Liste de valeurs AnnotationValeur
    * @param Object objet pour lequel est spécifié la valeur d'annotation 
    * @param utilisateur Utilisateur realisant la creation
    * @param operation String creation / modification
    * @param base directory pour enregistrer l'objet fichier associé 
    * avec le bon path dans le système de fichier associé.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @return liste AnnotationValeurs updatée.
    */
   List<AnnotationValeur> createAnnotationValeurListManager(List<AnnotationValeur> valeurs, TKAnnotableObject obj,
      Utilisateur utilisateur, String operation, String baseDir, List<File> filesCreated, List<File> filesToDelete);

   /**
    * Cherche toutes les valeurs d'annotations pour un objet. 
    * Methode utile lors de la cascade de suppression.
    * @param obj TKAnnotableObject
    * @return liste AnnotationValeurs.
    */
   List<AnnotationValeur> findByObjectManager(TKAnnotableObject obj);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id 
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param table AnnotationValeur dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(AnnotationValeur valeur);

   /**
    * Supprime un objet de la base de données.
    * @param table AnnotationValeur à supprimer de la base de données.
    * @param filesToDelete liste de fichier à supprimer
    */
   void removeObjectManager(AnnotationValeur valeur, List<File> filesToDelete);

   /**
    * Recherche toutes les instances AnnotationValeur présentes dans la base.
    * @return List contenant les AnnotationValeur.
    */
   List<AnnotationValeur> findAllObjectsManager();

   /**
    * Recherche toutes les valeurs spécifiée pour un champs  et
    * pour un objet passés en parametres.
    * @param ChampAnnotation champ
    * @param Object obj
    * @return Liste de AnnotationValeur.
    */
   List<AnnotationValeur> findByChampAndObjetManager(ChampAnnotation champ, TKAnnotableObject obj);

   /**
    * Recherche toutes les valeurs spécifiée pour un champs  et
    * pour un objet passés en parametres (spécifique pour l'export afin de récupérer les annotationValeur de champsCalcule enregistrées exprès, si discardCalcule = false)
    * @param champ ChampAnnotation
    * @param obj Objet
    * @param discardCalcule si true, ne pas calculer mais chercher une valeur enregistrée en base (annotationValeur)
    * @return Liste de AnnotationValeur.
    * @since 2.2.0
    */
   List<AnnotationValeur> findByChampAndObjetManager(ChampAnnotation champ, TKAnnotableObject obj, Boolean discardCalcule);

   /**
    * Recherche toutes les valeurs pour les champs appartenant à la table et
    * spécifiées pour la banque passées en parametres.
    * @param TableAnnotation table
    * @param Banque bank
    * @return Liste de AnnotationValeur.
    */
   List<AnnotationValeur> findByTableAndBanqueManager(TableAnnotation table, Banque bank);

   /**
    * Supprime une liste de valeurs d'annotation.
    * @param valeurs
    * @param liste des fichiers à supprimer. La suppression se fera systématiquement 
    * dans la méthode parente.
    */
   void removeAnnotationValeurListManager(List<AnnotationValeur> valeurs, List<File> filesToDelete);

   /**
    * renvoie la valeur remplie dans l'objet AnnotationValeur.
    * @param valeur AnnotationValeur.
    * @return Valeur remplie.
    */
   Object getValueForAnnotationValeur(AnnotationValeur valeur);

   AnnotationValeur findByIdManager(Integer annotationValeurId);

   /**
    * Migre les valeurs d'annotation lors de la migration d'un objet 
    * d'une banque à une autre.
    * Update les valeurs qui sont attribuées à des champs appartenant aux 
    * tables partagées par les banques de départ et d'arrivée. Supprime les
    * autres.
    * @param obj annotable qui est migré
    * @param bank banque d'arrivée
    * @param liste des fichiers à supprimer. La suppression se fera systématiquement 
    * dans la méthode parente.
    * @param liste de déplacements (uniques) de fichiers à programmer [Correctif bug TK-155]
    */
	void switchBanqueManager(TKAnnotableObject obj, Banque bank, 
						List<File> filesToDelete, Set<MvFichier> filesToMove);
			

   /**
    * Prépare les batchs statements pour full JDBC inserts d'une liste de valeurs annotations
    * Les validations sont identiques à celles appliquées par la création en 
    * JPA.
    * Méthode développée suite à problèmatique d'import > 500 échantillons (projet 
    * ESTEBAN CHU Tours). Le temps moyen de l'enregistrement d'un échantillon est 
    * divisé par 4 (200 à 50 msec).
    * @param jdbcSuite contenant les ids et statements permettant 
    * la creation des objets en full JDBC
    * @param valeurs
    * @param tkAnnotableObj
    * @param utilisateur
    * @version 2.0.10.6
    */
   void prepareAnnotationValeurListJDBCManager(EchantillonJdbcSuite jdbcSuite, List<AnnotationValeur> valeurs,
      TKAnnotableObject obj, Utilisateur utilisateur) throws SQLException;

   /**
    * Enregistre un fichier pour un batch de TKAnnotableObject. Une AnnotationValeur et 
    * un Fichier sont bien créés pour chaque obj, mais les Fichiers partagent le même 
    * path et MimeType. Le stream n'est utilisé pour ne créé qu'un seul fichier sur le 
    * disque. 
    * @param objs List<TKAnnotableObjects>
    * @param file Fichier contient le nom du fichier
    * @param stream InputStream contenu du fichier
    * @param champ ChampAnnotation
    * @param banque Banque
    * @param u Utilisateur tracabilité
    * @param baseDir base directory pour enreg fichier sur disque
    * @param filesCreated List<File> fichiers créés utile pour rollback
    * @version 2.0.12
    */
   void createFileBatchForTKObjectsManager(List<? extends TKAnnotableObject> objs, Fichier file, InputStream stream,
      ChampAnnotation champ, Banque banque, Utilisateur u, String baseDir, List<File> filesCreated);

   /**
    * Compte les valeurs d'annotations renseignée pour une banque et une 
    * table annotation passées en paramètres.
    * @param tab TableAnnotation
    * @param banque Banque
    * @return compte
    */
   Long findCountByTableAnnotationBanqueManager(TableAnnotation tab, Banque b);
}
