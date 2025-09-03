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
package fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.traitement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManagerFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.exception.AbstractImportCellScopeException;
import fr.aphp.tumorotek.manager.exception.ImportDefaultWrongImportValueException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueForThesaurusException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueForThesaurusMException;
import fr.aphp.tumorotek.manager.impl.io.imports.modification.champannotation.ImportChampAnnotationInfo;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.utils.io.ExcelIllegalContentTypeException;

/**
 * traitement d'import qui correspond à un champ d'annotation dont la valeur est stockée dans le champ ITEM_ID de la table ANNOTATION_VALEUR
 * mais pour un champ de type ThesaurusM (plusieurs ITEM_ID possibles)
 * dans ce cas, l'utilisateur ne peut qu'ajouter une nouvelle valeur. Si une valeur existante n'est pas reprise dans le fichier d'import, elle ne
 * sera pas supprimée (sécurité pour éviter de faire une suppression non souhaitée)
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportChampAnnotationTraitementForThesaurusM extends ImportChampAnnotationTraitementForThesaurus
{
   private final Logger log = LoggerFactory.getLogger(ImportChampAnnotationTraitementForThesaurus.class);
   
   public ImportChampAnnotationTraitementForThesaurusM(Integer champAnnotationId, int indexColonne, ImportColonne importColonne, EntityManagerFactory entityManagerFactory) {
      super(champAnnotationId, indexColonne, importColonne, entityManagerFactory);
   }

   
   /**
    * surcharge de la méthode par défaut car dans ce cas, le contenu d'une cellule alimente plusieurs objets dans listImportInfo
    */
   @Override
   public void readCellForAnnotationValeur(Cell cell, FormulaEvaluator formulaEvaluator, Integer objetId, int numRow) throws AbstractImportCellScopeException {
      
      String cellContent = null;
      try {
         cellContent = (String)getImportChampAnnotationReader().read(cell, formulaEvaluator);
      }
      catch (ExcelIllegalContentTypeException excelIllegalContentTypeException) {
         throw new ImportDefaultWrongImportValueException(getImportColonne(), excelIllegalContentTypeException.getTypeAttendu());
      }
      
      if(cellContent != null) {
         String[] tabValeur = ((String) cellContent).split(";");
         int nbValeur = tabValeur.length;
         List<String> listValeurNonAutorisee = new ArrayList<String>();
         for(int i = 0; i < nbValeur; i++){
            try {
               Object itemIdForValeur = convertCellContent(tabValeur[i]);
               getListImportInfo().add(new ImportChampAnnotationInfo(getChampAnnotationId(), objetId, itemIdForValeur, numRow));
            }
            catch(WrongImportValueForThesaurusException wrongImportValueForThesaurusException) {
               listValeurNonAutorisee.add(tabValeur[i]);
            }
         }

         if(!listValeurNonAutorisee.isEmpty()) {
            throw new WrongImportValueForThesaurusMException(getImportColonne(), listValeurNonAutorisee, getMapValeursAutoriseesByLibelle().keySet());
         }

      }

   }
   
   @Override
   public void manageSelectResultSet(ResultSet resultSet) throws SQLException {
      if(resultSet != null) {
         while(resultSet.next()) {
            Integer champAnnotationId = resultSet.getInt(2);
            Integer objetId = resultSet.getInt(3);
            Integer itemId = resultSet.getInt(4);
            
            //dans le cas du traitement de thesaurusM, listImportInfo peut contenir plusieurs éléments pour le même objet TK
            //un par valeur de thesaurus à affecter à l'objet TK
            //si la modification que l'utilisateur souhaite faire est l'ajout d'une valeur pour ce champ et qu'il a transmis cette nouvelle valeur
            //ainsi que les valeurs déjà présentes, il faut supprimer ces ImportChampAnnotationInfo qui existent déjà en base
            //A noter que le traitement ne supprimera jamais une valeur de thésaurus déjà présente en base : pour les thesaurusM, 
            //l'utilisateur ne peut qu'ajouter une nouvelle valeur (cf prepareInsertAndUpdateCall())
            getListImportInfo().removeIf(
               importInfo -> importInfo.getChampAnnotationId().equals(champAnnotationId) && importInfo.getObjetId().equals(objetId) && ((Integer)importInfo.getNewValeur()).equals(itemId));
         }
      }
   }
   
   //Pour le thesaurusM, on ne fait que de l'insert.
   //En effet, il s'agit d'un lien 1-n. En supposant que l'utilisateur transmet à chaque fois toutes les valeurs,
   //l'update correspond à une suppression des valeurs non présentes dans le fichier et une insertion des valeurs non présentes en base de données
   //Mais il se peut que l'utilisateur ne transmette que les ajouts donc pas sécurité, on ne fera que les insertions pour les valeurs non présentes en base
   //======== L'import en mise à jour sur un champ d'annotation de type thesaurusM ne permet donc de faire que des ajouts de valeurs ==========
   //
   @Override
   public void prepareInsertAndUpdateCall(PreparedStatement pstForInsert, PreparedStatement pstForUpdate, Integer banqueId) throws SQLException {
      for(ImportChampAnnotationInfo importInfo : getListImportInfo()) {
         prepareInsertCallForThisImportChampAnnotationInfo(importInfo, pstForInsert, banqueId);
      }
   }

   //dans le cas des champs d'annotation de type theusaurusM, il peut y avoir plusieurs ImportChampAnnotationInfo pour un même objet TK et un même champ d'annotation
   //cette méthode permet de filter pour ne ramener que le 1er élément. Cela permet ensuite de ne récupérer qu'une fois l'objetId et ne pas alourdir inutilement les requêtes de select
   @Override
   protected Stream<ImportChampAnnotationInfo> getListImportInfoWithoutDoublon() {
      return getListImportInfo().stream().distinct();
   }
   
}
