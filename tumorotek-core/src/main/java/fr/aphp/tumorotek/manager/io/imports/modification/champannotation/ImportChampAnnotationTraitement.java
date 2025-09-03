/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.io.imports.modification.champannotation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import fr.aphp.tumorotek.manager.exception.AbstractImportCellScopeException;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.EAnnotationValeurInfoColonne;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.ImportChampAnnotationInfo;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;

/**
 * traitement d'import qui correspond à un champ d'annotation (c'est-à-dire à une colonne de fichier importé dans le cas d'un import de type 
 * "modification des annotations" d'un objet TK)
 * les classes filles permettent de définir les spécificités liées au type du champ d'annotation
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public interface ImportChampAnnotationTraitement
{
   /**
    * retourne l'enum EAnnotationValeurColonneForData correspondant à la colonne associée au traitement
    * @return
    */
   EAnnotationValeurInfoColonne getAnnotationValeurInfoColonne();
   
   /**
    * lit le contenu d'une cellule du fichier d'import (au format excel) associée à un objetId, en prenant en compte éventuellement un FormulaEvaluator
    * @param cell cellule dont le contenu doit être lu
    * @param formulaEvaluator
    * @param objetId l'id de l'objet TK associé à la valeur lue
    * @numRow numéro de la ligne de la cellule
    * @throws AbstractImportCellScopeException
    */
   void readCellForAnnotationValeur(Cell cell, FormulaEvaluator formulaEvaluator, Integer objetId, int numRow) throws AbstractImportCellScopeException;
   
   /**
    * constuit la requête de select pour récupérer les valeurs existantes pour le champ d'annotation à traiter
    * @param preparedStatement
    * @throws SQLException
    */
   void prepareSelectCall(PreparedStatement preparedStatement) throws SQLException;
   
   /**
    * lit le resultSet de recherche des valeurs existantes ({@link #prepareSelectCall(PreparedStatement))} 
    * @param selectResultSet resultSet de la requête de sélection dans ANNOTATION_VALEUR des valeurs existantes pour un champ d'annotation.
    * @throws SQLException
    */
   void manageSelectResultSet(ResultSet selectResultSet) throws SQLException;
   
   /**
    * constuit les requêtes d'insert et d'update pour modifier les valeurs du champ d'annotation concerné par le traitement
    * @param preparedStatement
    * @throws SQLException
    */
   void prepareInsertAndUpdateCall(PreparedStatement pstForInsert, PreparedStatement pstForUpdate, Integer banqueId) throws SQLException;
   
   /**
    * retourne l'index de la colonne dans le fichier. Commence à 0
    */
   int getIndexColonneInFile();
   
   /**
    * retourne l'importColonne associé au traitement
    */
   ImportColonne getImportColonne();
   
   /**
    * retourne la requête SQL de recherche des valeurs existantes
    */
   String getSqlSelectRequete();

   /**
    * retourne la requête SQL d'insert d'une valeur pour le champ d'annotation concerné
    */
   String getSqlInsertRequete();
   
   /**
    * retourne la requête SQL d'update d'une valeur pour le champ d'annotation concerné
    */
   String getSqlUpdateRequete();
   
   /**
    * retourne true si aucun objet TK n'est à traiter pour ce champ d'annotation
    * @return
    */
   boolean isEmpty();
   
   /**
    * supprime tous les objets TK associés à ce traitement
    * @return
    */
   void clear();

   /**
    * retourne la liste des données nécessaires pour la mise à jour des champs
    * @return
    */
   List<ImportChampAnnotationInfo> getListImportInfo();
   
   /**
    * retourne la liste des objetId sous forme de set pour supprimer les doublons
    * @return
    */
   Set<Integer> retrieveObjetIdTraite();
   
   /**
    * convertit au format String la valeur passée en paramètre.
    * Le type de la valeur à convertir est celui de la colonne associé au traitement 
    * utilisé notamment pour afficher à l'utilisateur la valeur présente en base de données
    * @param valeur
    * @return
    */
   String convertValeurAsString(Object valeur);
}
