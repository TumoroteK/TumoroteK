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

import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Interface pour le Manager du bean de domaine TableCodage.
 * Interface créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface TableCodageManager
{

   /**
    * Recherche toutes les instances de TableCodage.
    * @return List contenant les tables de codage.
    */
   List<TableCodage> findAllObjectsManager();

   /**
    * Recherche la table de codage dont le nom est like celui passé
    * en paramètre.
    * @param nom Nom pour lequel on recherche une table.
    * @return List contenant les tables de codage.
    */
   List<TableCodage> findByNomManager(String nom);

   /**
    * Recherche un code partir de la table et du code de l'Id 
    * spécifiés en paramètres.
    * @param ID du code
    * @param TableCodage
    * @return le code
    */
   //CodeCommon findCodeByTableCodageAndId(Integer codeId, TableCodage table);

   /**
    * Renvoie la table de codage associée au code common passé en paramètre.
    * @param code
    * @return table de codage.
    */
   //TableCodage getTableCodageFromCodeCommon(CodeCommon code);

   //	/**
   //	 * Recupere les banques associées à la codification passée en paramètre.
   //	 * @param tab
   //	 * @return Liste de banques.
   //	 */
   //	Set<Banque> getBanquesManager(TableCodage tab);

   /**
    * Trouve tous les codes au travers des tables de transcodage pour le code 
    * et sa table de codage passés en paramètres.
    * @param code dont on cherche les transcodes
    * @param tables auxquelles peuvent appartenir les transcodes.
    * @param liste de banque (pour les codes utilisateur)
    * @return
    */
   List<CodeCommon> transcodeManager(CodeCommon code, List<TableCodage> tables, List<Banque> banks);

   /**
    * Trouve tous les codes au travers des tables de transcodage pour la chaine
    * de caractere passées en paramètres. Trouve pour chaque table le code dont
    * le code ou le libellé est égal à la chaine de caractère 
    * passée en paramètre et trouve tous les transcodes 
    * pour les autres tables. 
    * @param string chaine de caractères à matcher sur la base de codes.
    * @param liste de table de codifications.
    * @param liste de banque (pour les codes utilisateur)
    * @param exactMatch true si recherche exacte a partir code ou libelle
    * @return
    */
   List<CodeCommon> findCodesAndTranscodesFromStringManager(String codeorLib, List<TableCodage> tables, List<Banque> banks,
      boolean exactMatch);

   /**
    * Transforme une liste de CodeCommon en la liste de codes équivalente.
    * @param codes
    * @return liste de codes String
    */
   List<String> getListCodesFromCodeCommon(List<CodeCommon> codes);

   /**
    * Transforme une liste de CodeCommon en la liste de libelles équivalente.
    * @param codes
    * @return liste de libelles String
    */
   List<String> getListLibellesFromCodeCommon(List<CodeCommon> codes);
}
