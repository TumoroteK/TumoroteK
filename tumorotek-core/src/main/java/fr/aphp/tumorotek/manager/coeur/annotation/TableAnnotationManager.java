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

import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanque;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine TableAnnotation.<br>
 * Interface créée le 02/02/10.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer une table (controle de doublons)<br>
 * 	- Modifier une table (controle de doublons)<br>
 * 	- Afficher toutes les tables pour une entité et une banque<br>
 * 	- Afficher toutes les tables pour une entité
 * 		assignable par un utilisateur<br>
 *  - monter ou descendre une table dans l'ordre d'apparition pour une
 *  	entité et une banque<br>
 * 	- Supprimer une table
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface TableAnnotationManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param table TableAnnotation à creer
    * @param entite 
    * @param catalogue
    * @param champs List des champs à créer ou a modifier
    * @param banques liste de Banques
    * @param banque courante
    * @param utilisateur Utilisateur realisant la creation
    * @param operation String creation / modification
    * @param base directory pour créer dossier sytème associé.
    */
   void createOrsaveManager(TableAnnotation table, Entite entite, Catalogue catalogue, List<ChampAnnotation> champs,
      List<Banque> banques, Banque current, Utilisateur utilisateur, String operation, String baseDir, Plateforme pf);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id 
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param table TableAnnotation dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(TableAnnotation table);

   //	/**
   //	 * Verifie avant la suppression que d'autres objets ne referencent
   //	 * pas cet objet. 
   //	 * @param table TableAnnotation a supprimer de la base de donnees.
   //	 * @return true/false
   //	 */
   //	boolean isUsedObjectManager(Patient patient);

   /**
    * Supprime un objet de la base de données.
    * @param table TableAnnotation à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param base directory pour effacer dossier sytème associé.
    */
   void deleteByIdManager(TableAnnotation table, String comments, Utilisateur usr, String baseDir);

   /**
    * Recherche les champs liés à la table passée en paramètres.
    * @param table TableAnnotation pour laquelle on recherche les champs.
    * @return Liste de ChampAnnotation.
    */
   Set<ChampAnnotation> getChampAnnotationsManager(TableAnnotation table);

   /**
    * Recherche les banques auxquelles la table passée en paramètres est 
    * assignée.
    * @param table TableAnnotation pour laquelle on recherche les banques.
    * @return Liste de Banque.
    */
   Set<Banque> getBanquesManager(TableAnnotation table);

   /**
    * Recherche les references TableAnnotationBanques pour lesquelles la
    * table est passée en paramètre.
    * @param table TableAnnotation pour laquelle on recherche les TABs.
    * @return Liste de TableAnnotationBanques.
    */
   Set<TableAnnotationBanque> getTableAnnotationBanquesManager(TableAnnotation table);

   /**
    * Recherche toutes les instances de TableAnnotation présentes dans la base.
    * @return List contenant les TableAnnotation.
    */
   List<TableAnnotation> findAllObjectsManager();

   /**
    * Recherche toutes les tables dont le nom est egal ou 'like' 
    * celui passé en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de TableAnnotation.
    */
   List<TableAnnotation> findByNomLikeManager(String nom, boolean exactMatch);

   /**
    * Recherche toutes les tables dont l'entite et la banque sont passées en 
    * en parametres.
    * @param Entite entite
    * @param Banque bank
    * @return Liste de TableAnnotation.
    */
   List<TableAnnotation> findByEntiteAndBanqueManager(Entite entite, Banque bank);

   /**
    * Recherche toutes les tables dont l'entite, la banque et le
    * catalogue sont passées en 
    * en parametres.
    * @param Entite entite
    * @param Banque bank
    * @param catalogue Nom du catalogue.
    * @return Liste de TableAnnotation.
    */
   List<TableAnnotation> findByEntiteBanqueAndCatalogueManager(Entite entite, Banque bank, String catalogue);

   /**
    * Recherche toutes les tables assignables pour une entité 
    * et une plateforme.
    * @param Entite entite
    * @param Plateforme pf
    * @return Liste de TableAnnotation.
    */
   List<TableAnnotation> findByEntiteAndPlateformeManager(Entite entite, Plateforme pf);

   //	/**
   //	 * Monte ou descend l'ordre d'une table pour une banque spécifiée 
   //	 * d'une unité. Modifie les TableAnnotationBanque a l'intérieur de leur
   //	 * liste.
   //	 * @param liste TableAnnotationBanque 
   //	 * @param boolean specifiant up si true
   //	 */
   //	void moveTableOrderUpDownManager(TableAnnotation table, Banque banque, 
   //																boolean up);

   /**
    * Cette méthode met à jour les associations entre une table et
    * une liste de champAnnotation à créer ou a modifier.
    * @param table pour lequel on veut mettre à jour
    * les champs.
    * @param champs Liste des ChampAnnotation que l'on créer ou modifier.
    * @param utilisateur realisant les operations creation/modification
    * @param banque courante
    * @param base directory pour créer dossier sytème associé.
    */
   void createOrUpdateChampsManager(TableAnnotation tab, List<ChampAnnotation> champs, Utilisateur usr, Banque current,
      String baseDir);

   /**
    * Applique l'ordre d'appartion des champs à partir de la liste 
    * ordonnée des champs. Ne passe pas par ChampAnnotationManager 
    * pour ne pas enregistrer de modifications associées dans la base.
    * Remplace moveTableOrderUpDownManager.
    * @param chps liste ordonnée des champs.
    */
   void updateChampOrdersManager(List<ChampAnnotation> chps);

   /**
    * Recherche les tables assignées aux banques présentes dans la liste.
    * @param banks
    * @param boolean catalogue false, retire toutes les tables catalogues
    * @return une liste de TableAnnotation ordonnée par leur nom.
    * @version 2.0.10
    */
   List<TableAnnotation> findByBanquesManager(List<Banque> banks, boolean catalogue);

   /**
    * Recherche les tables associées au catalogue passé en paramètre.
    * @param catas
    * @return une liste de TableAnnotation.
    */
   List<TableAnnotation> findByCataloguesManager(List<Catalogue> catas);

   /**
    * Recherche les tables associées au catalogue passé en paramètre 
    * et contenant au moins un champ annotation dont editable 
    * par l'utilisateur.
    * @param catalogue
    * @return une liste de TableAnnotation.
    */
   List<TableAnnotation> findByCatalogueAndChpEditManager(Catalogue cat);

   /**
    * Recherche les tables d'annotations pour la plateforme spécifiée.
    * @param pf
    * @return liste de TableAnnotation
    */
   List<TableAnnotation> findByPlateformeManager(Plateforme pf);

   /**
    * Renvoie l'objet persistant à partir de son id
    * @param id
    * @return TableAnnnotation
    */
   TableAnnotation findByIdManager(Integer id);
}
