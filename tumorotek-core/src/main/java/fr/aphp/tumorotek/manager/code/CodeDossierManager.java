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
package fr.aphp.tumorotek.manager.code;

import java.util.List;

import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeDossier.
 * Interface créée le 06/06/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface CodeDossierManager
{

   /**
    * Recherche toutes les dossiers.
    * @return List contenant les dossiers.
    */
   List<CodeDossier> findAllCodeDossiersManager();

   /**
    * Recherche le dossier dont le nom est like celui passé
    * en paramètre.
    * @param nom Nom pour lequel on recherche un dossier.
    * @param boolean exactMatch
    * @param banque
    * @return List contenant les codeDossiers.
    */
   List<CodeDossier> findByNomLikeManager(String nom, boolean exactMatch, Banque bank);

   /**
    * Recherche les dossier du Dossier passé en paramètre.
    * @param dossier dont on veut les dossiers contenus.
    * @return une liste de codeDossiers.
    */
   List<CodeDossier> findByCodeDossierParentManager(CodeDossier parent);

   /**
    * Recherche les dossiers de CodeUtilisateur a la racine.
    * @param banque
    * @return une liste de codeDossiers.
    */
   List<CodeDossier> findByRootCodeDossierUtilisateurManager(Banque bank);

   /**
    * Recherche les dossiers de CodeSelect a la racine pour un utilisateur
    * et une banque donnée.
    * @param utilisateur
    * @param banque
    * @return une liste de codeDossiers.
    */
   List<CodeDossier> findByRootCodeDossierSelectManager(Utilisateur u, Banque bank);

   /**
    * Recherche les dossiers de codes utilisateurs
    * pour l'utilisateur et la banque passées en paramètres.
    * @param l'utilisateur pour lequel on recherche des dossiers.
    * @param la banque
    * @return une liste de CodeDossiers.
    */
   List<CodeDossier> findByUtilisateurAndBanqueManager(Utilisateur u, Banque b);

   /**
    * Recherche les dossiers de codes ajoutés au favoris
    * pour l'utilisateur et la banque passées en paramètres.
    * @param l'utilisateur pour lequel on recherche des dossiers.
    * @param la banque
    * @return une liste de CodeDossiers.
    */
   List<CodeDossier> findBySelectUtilisateurAndBanqueManager(Utilisateur u, Banque b);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param table CodeUtilisateur dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(CodeDossier dos);

   /**
    * Enregsitre ou modifie un dossier.
    * @param dos
    * @param dossier parent
    * @param banque
    * @param utilisateur
    * @param code parent
    * @param String operation creation/modification
    */
   void createOrUpdateManager(CodeDossier dos, CodeDossier parent, Banque bank, Utilisateur utilisateur, String operation);

   /**
    * Supprime un dossier de la base de données ainsi que tous les codes
    * et sous-dossiers dont il est parent en cascade.
    * @param code CodeDossier à supprimer de la base de données.
    */
   void removeObjectManager(CodeDossier dos);

   /**
    * Recherche tous les dossiers de codes favoris ou utilisateur définis
    * pour une banque à la racine de l'arborescence.
    * @param banque Banque
    * @return une liste de CodeDossier.
    */
   List<CodeDossier> findByRootDossierBanqueManager(Banque bank, Boolean select);
}
