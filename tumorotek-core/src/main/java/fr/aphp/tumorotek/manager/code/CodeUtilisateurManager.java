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
import java.util.Set;

import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeUtilisateur.
 * Interface créée le 21/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface CodeUtilisateurManager
{

   /**
    * Recherche toutes les instances de codes présentes dans la codification.
    * @return List contenant les codes.
    */
   List<CodeUtilisateur> findAllObjectsManager();

   /**
    * Recherche les codes dont le code est like celui passé
    * en paramètre.
    * @param code Code pour lequel on recherche des codes.
    * @param boolean exactMatch
    * @param liste de banques
    * @return Liste de codes.
    */
   List<CodeUtilisateur> findByCodeLikeManager(String code, boolean exactMatch, List<Banque> bank);

   /**
    * Recherche les codes dont le libellé est like celui passé en
    * paramètre.
    * @param libelle Description du code que l'on recherche.
    * @param boolean exactMatch
    * @param liste de banque
    * @return une liste de codes.
    */
   List<CodeUtilisateur> findByLibelleLikeManager(String libelle, boolean exactMatch, List<Banque> bank);

   /**
    * Recherche les codes du Dossier passé en paramètre.
    * @param dossier dont on veut les codes contenus.
    * @return une liste de codes CodeUtilisateur.
    */
   List<CodeUtilisateur> findByCodeDossierManager(CodeDossier parent);

   /**
    * Recherche les codes non contenus dans un dossier.
    * @param banque
    * @return une liste de codes CodeUtilisateur.
    */
   List<CodeUtilisateur> findByRootDossierManager(Banque bank);

   /**
    * Recherche les codes heritant d'un code parent.
    * @param codeUtilisateur parent
    * @return une liste de codes CodeUtilisateur.
    */
   List<CodeUtilisateur> findByCodeParentManager(CodeUtilisateur parent);

   /**
    * Recherche les codes pour un utilisateur et une banque.
    * @param utilisateur Utilisateur
    * @param banque Banque 
    * @return une liste de codes CodeUtilisateur.
    */
   List<CodeUtilisateur> findByUtilisateurAndBanqueManager(Utilisateur u, Banque b);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id 
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param table CodeUtilisateur dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(CodeUtilisateur code);

   /**
    * Enregsitre ou modifie un code utilisateur. 
    * @param code
    * @param dos
    * @param banque
    * @param utilisateur
    * @param code parent
    * @param transcodes
    * @param String operation creation/modification
    */
   void createOrUpdateManager(CodeUtilisateur code, CodeDossier dos, Banque bank, Utilisateur utilisateur, CodeUtilisateur parent,
      Set<CodeCommon> transcodes, String operation);

   /**
    * Supprime un objet de la base de données ainsi que tous les codes 
    * dont il est le parent en cascade.
    * @param code CodeUtilisateur à supprimer de la base de données.
    */
   void removeObjectManager(CodeUtilisateur code);

   /**
    * Renvoie les codes utilisateurs héritant du code parent.
    * @param code parent.
    * @return
    */
   Set<CodeUtilisateur> getCodesUtilisateurManager(CodeUtilisateur code);

   /**
    * Recherche les codes Adicap, Cim ou Cimo issus du transcodage du
    * code Utilisateur passé en paramètre.
    * @param code utilisateur qui sera transcodé
    * @param tables de codage auxquelles doivent appartenir les transcodes
    * @return Liste de codes implemantant CodeCommon
    */
   Set<CodeCommon> getTranscodesManager(CodeUtilisateur code, List<TableCodage> tables);

   /**
    * Recherche les codes utilisateurs issus du transcodage du code 
    * passé en paramètre. 
    * @param code
    * @param liste de banques auxquelles appartiennent codes utilisateurs.
    * @return liste de codes utilisateurs.
    */
   List<CodeUtilisateur> findByTranscodageManager(CodeCommon code, List<Banque> banks);

   /**
    * Renvoie le code utilisateur correspondant au code id passé en paramètre.
    * @param codeId
    * @return codeUtilisateur
    */
   CodeUtilisateur findByIdManager(Integer codeId);
}
