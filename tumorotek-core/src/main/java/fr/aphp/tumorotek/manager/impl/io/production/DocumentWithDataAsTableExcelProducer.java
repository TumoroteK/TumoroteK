/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.impl.io.production;

import fr.aphp.tumorotek.dto.DocumentProducerResult;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.exception.ExcelWriteException;
import fr.aphp.tumorotek.manager.io.document.DataAsTable;
import fr.aphp.tumorotek.manager.io.document.DocumentContext;
import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsTable;
import fr.aphp.tumorotek.manager.io.document.LabelValue;
import fr.aphp.tumorotek.manager.io.document.detail.table.AlignmentType;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellContent;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.io.document.detail.table.DataCell;
import fr.aphp.tumorotek.manager.io.production.DocumentProducer;
import fr.aphp.tumorotek.utils.io.ExcelUtility;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.afp.modca.MapCodedFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe implémente l'interface {@link DocumentProducer}
 * et fournit la fonctionnalité pour produire des documents Excel à partir d'une liste d'objets {@link DocumentWithDataAsTable}.
 *
 * <p>Le principal objectif de cette classe est de faciliter la génération de documents Excel à partir des données encapsulées
 * dans les objets {@link DocumentWithDataAsTable}. Elle est conçue pour être utilisée dans des scénarios où les données
 * doivent être exportées ou transformées en format de feuille de calcul.</p>
 *
 * <p><b>Exemple d'utilisation :</b></p>
 * <pre>{@code
 * List<DocumentWithDataAsTable> data = ...; // Préparez vos données
 * OutputStreamData output = new OutputStreamData();
 * output.setFileName("toto");
 * DocumentWithDataAsTableExcelProducer producer = new DocumentWithDataAsTableExcelProducer();
 * producer.produce(data, output);
 * }</pre>
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public class DocumentWithDataAsTableExcelProducer implements DocumentProducer
{

   private static final Logger log = LoggerFactory.getLogger(DocumentWithDataAsTableExcelProducer.class);

   //CHT : utilisation de XSSFColor BLACK_COLOR = new XSSFColor(new java.awt.Color(0, 0, 0)); dans ExcelUtility
   // Couleur noire en hexadécimal pour les bordures.
   private static final String BLACK_HEX_CODE = "#000000";
   
   private static final XSSFColor BLACK__XSSF_COLOR = new XSSFColor(new java.awt.Color(0, 0, 0));

   // Indice de la première colonne.
   private static final int FIRST_COLUMN_INDEX = 0;
   
   //CHT : 
   private static final short FONT_SIZE__11 = 11;
   
   //CHT : map servant de cache pour faire le lien entre un code hexadecimal de TK et le XSSFColor associé 
   private Map<String, XSSFColor> mapColor = new HashMap<String, XSSFColor>();
 //CHT : map servant de cache pour les fonts à utiliser
   private Map<EDocumentExcelFont, Font> mapFont = new HashMap<EDocumentExcelFont, Font>();

   /**
    * Produit un document Excel à partir d'une liste de DocumentWithDataAsTable.
    *
    * @param listDocumentWithDataAsTable La liste des documents avec données sous forme de table.
    * @return DocumentProducerResult Le résultat du document produit, contenant le format et le flux de sortie.
    * @throws IOException En cas d'erreur lors de la création du fichier Excel.
    */
   //passer en paramètre l'index de colonne (et de ligne) où commencer à écrire ?
   @Override
   public DocumentProducerResult produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable) throws IOException{
      // Initialisation du résultat à renvoyer
      DocumentProducerResult result = new DocumentProducerResult();
      result.setFormat(ConfigManager.EXCEL_XLSX_FILETYPE); // Définit le format du fichier
      result.setContentType(ConfigManager.OFFICE_OPENXML_MIME_TYPE); // Définit le type MIME

      // Utilisation de try-with-resources pour gérer automatiquement la fermeture des ressources
      try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         Workbook workbook = new XSSFWorkbook() ){ // utiliser cette classe de XSSFWorkbook fait marcher l'italique
         /*SXSSFWorkbook workbook = new SXSSFWorkbook() ){  // Optimized for large data sets

         // Optimise l'utilisation de la mémoire pour les grands ensembles de données
         workbook.setCompressTempFiles(true);//CHT : pas nécessaire dans notre cas - peut causer des pénalités de performance
*/
         for(DocumentWithDataAsTable document : listDocumentWithDataAsTable){
            String sheetName = document.getDocumentName(); // Récupère le nom de la feuille
            Sheet sheet = ExcelUtility.createSheet(workbook, sheetName); // Crée une nouvelle feuille
            //!!!!!!!!!!!!!!!!!!!!!!! CHT : à passer en paramètre car pourrait dépendre du document (=> ajouter un param à la méthode) : 25 = nombre de caractères
            sheet.setDefaultColumnWidth(25);
            
            if(document.getContext() != null) {
               // Écrit le contexte du document dans la feuille
               //writeDocumentContext(sheet, document.getContext().getListLabelValue());//il faudrait mieux passer tout le contexte en paramètre
               writeDocumentContext(sheet, document.getContext());
            }
            
            // Écrit les données du document dans la feuille
            writeDocumentData(sheet, document.getData());

            // Ajoute un pied de page à la feuille
            ExcelUtility.addFooter(sheet, document.getFooter().getLeftData(), document.getFooter().getCenterData(),
               document.getFooter().getRightData());
         }

         workbook.write(outputStream); // Écrit le classeur dans le flux de sortie

         result.setOutputStream(outputStream); // Définit le flux de sortie dans le résultat

      }  // Auto-close resources when done
      //CHT : ces blocs catchs sont inutiles vu que les exceptions sont renvoyées.
      //C'est la classe qui gère l'exception qui trace l'erreur.
      catch(ExcelWriteException e){
         log.error("Error occurred while writing the excel file: {}", e.getMessage(), e);
         throw e; // Relance l'exception en cas d'erreur
      }catch(Exception e){
         log.error("Unexpected error: {}", e.getMessage());
         throw e;
      }

      return result; // Retourne le résultat du produit documentaire créé

   }

   //CHT : Il faudrait mieux que cette méthode prenne un documentContext et non uniquement la liste de labelValues
   //c'est plus cohérent avec le nom de la méthode et plus évolutif si DocumentContext a d'autres attributs
   /**
    * Écrit le contexte du document dans une feuille donnée.
    *
    * @param sheet La feuille où écrire les informations (non nulle).
    * @param labelValues Liste des paires label-valeur à écrire (non nulle).
    */
   private void writeDocumentContext(Sheet sheet, List<LabelValue> labelValues){
      if(sheet != null && labelValues != null) {
         int rowIndex = 0; // Indice initial pour les lignes
         for(LabelValue labelValue : labelValues){
            //CHT : il faut descendre dans 4 méthodes pour comprendre comment sont écrits ces objets
            //alors que ce qu'il faut faire est relativement simple
            ExcelUtility.writeToCellWithOptionalBold(sheet, rowIndex, FIRST_COLUMN_INDEX, labelValue.getLabel(),
               labelValue.isLabelInBold());
            ExcelUtility.writeToCellWithOptionalBold(sheet, rowIndex, FIRST_COLUMN_INDEX + 1, labelValue.getValue(),
               labelValue.isValueInBold());
            rowIndex++; // Incrémente l'indice après chaque écriture
         }
   
         // Écrit une ligne vide après les labels et valeurs  pour séparer la section Contexte de la section Données.
         int emptyRow = rowIndex + 1;
         ExcelUtility.writeToCell(sheet, emptyRow, FIRST_COLUMN_INDEX, "");
      }
   }

   private void writeDocumentContext(Sheet sheet, DocumentContext documentContext){
      if(sheet != null && documentContext != null) {
         List<LabelValue> labelValues = documentContext.getListLabelValue();
         if(labelValues != null) {
            //initialisation
            int rowIndex = 0; // on commence sur la 1ere ligne du document
            int colIndexLabel = 0;
//            Font boldFont = sheet.getWorkbook().createFont();
//            boldFont.setBold(true);
            CellStyle styleBold = sheet.getWorkbook().createCellStyle();
            styleBold.setFont(getFont(EDocumentExcelFont.DEFAULT_BOLD, sheet.getWorkbook()));
            //gestion des labelValue
            for(LabelValue labelValue : labelValues){
               //Le LabelValue est écrit sur 1 ligne dans 2 cellules
               Row row = sheet.createRow(rowIndex);
               Cell cellLabel = row.createCell(colIndexLabel);
               if(labelValue.isLabelInBold()) {
                  cellLabel.setCellStyle(styleBold);
               }
               cellLabel.setCellValue(labelValue.getLabel());
               Cell cellValue = row.createCell(colIndexLabel+1);
               if(labelValue.isValueInBold()) {
                  cellValue.setCellStyle(styleBold);
               }
               cellValue.setCellValue(labelValue.getValue());
               rowIndex++;
            }
            
            // Écrit une ligne vide après les labels et valeurs  pour séparer la section Contexte de la section Données.
            sheet.createRow(rowIndex);
         }
      }
   }
   
   /**
    * Écrit les données du tableau dans une feuille donnée.
    *
    * @param sheet La feuille où écrire les données.
    * @param data Les données sous forme de tableau à écrire.
    */
   private void writeDocumentData(Sheet sheet, DataAsTable data){
      if(sheet != null && data != null) {
         int rowIndex = sheet.getPhysicalNumberOfRows(); // Start where last written ended
         for(CellRow cellRow : data.getListCellRow()){
            //writeCellRow(sheet, rowIndex, cellRow);
            writeCellRowCHT(sheet, rowIndex, cellRow);
            rowIndex++;
         }
      }

   }
   
   //CHT : cette méthode ne semble pas gérer le retour à la ligne
   //par ailleurs, bien que le découpage semble bien définir les différentes étapes (bonne intention)
   //la mise en oeuvre apparaît compliquée à comprendre car des choses essentielles sont encapsulées dans des méthodes et même sous méthodes
   //de ExcelUtility comme "créer une Row" et "créer une Cell", responsabilité première de cette méthode. Ces 2 appels 
   //devraient donc être les premières lignes de la méthode pour ensuite appliquer les caractéristiques de DataCell sur Cell
   /**
    * Écrit une ligne complète composée des cellules spécifiées dans une feuille donnée.
    *
    * @param sheet La feuille où écrire la ligne.
    * @param rowIndex L'indice de ligne où commencer l'écriture.
    * @param cellRow La ligne contenant des cellules à écrire.
    *
    *  <p>
    *  Cette méthode parcourt chaque DataCell dans le CellRow et écrit les données dans la feuille.
    *  Elle gère également la fusion des cellules lorsque cela est spécifié dans les propriétés de la DataCell.
    *  Le but est de s'assurer que les données sont formatées et alignées correctement dans la feuille,
    *  en respectant les styles de texte et de bordure fournis.
    *  </p>
    *
    */
   private void writeCellRow(Sheet sheet, int rowIndex, CellRow cellRow){
      //CHT : on est en train de créer une Row donc ça surprend de ne pas voir Row row = sheet.createRow(rowIndex); en début de traitement
      //de même pour la création de la Cellule qui devrait être faite avant d'appliquer les différentes caractéristiques
      //Or elle est faite à 2 endroits selon qu'il y ait un colspan ou pas...
      //Cacher les appels de base peut compliquer la compréhension et l'éventuel débogage
   
      
      //CHT : il vaut mieux sortir cellRow.getListDataCell().size() dans une variable pour appeler qu'une seule fois ces méthodes
      for(int colIndex = 0; colIndex < cellRow.getListDataCell().size(); colIndex++){
         DataCell dataCell = cellRow.getListDataCell().get(colIndex);
         CellContent content = dataCell.getCellContent();

         // Gérer la fusion des cellules si la DataCell spécifie une largeur de colonne supérieure à 1.
         if(dataCell.getColspan() > 1){
            int endColIndex = colIndex + dataCell.getColspan() - 1;
            // Fusionner les cellules de la colonne de début à la colonne de fin.
            ExcelUtility.mergeCells(sheet, rowIndex, rowIndex, colIndex, endColIndex, content.getText());

            // Sauter les colonnes fusionnées pour éviter de traiter ces cellules à nouveau.
            colIndex += (dataCell.getColspan() - 1);
            //CHT : pourquoi faire un continue. Le besoin semble être un if / else.
            continue; // Passer à l'itération suivante après la fusion
         }

         //CHT : On sait qu'on veut créer la cellule donc row.createCell(colIndex) suffisait
         // Créer ou obtenir la cellule existante à l'emplacement spécifié.
         Cell cell = ExcelUtility.getOrCreateCell(sheet, rowIndex, colIndex);

         // Appliquer l'alignement spécifié dans la DataCell à la cellule.
         ExcelUtility.applyAlignment(cell, dataCell.getAlignmentType());

         // Appliquer le style de texte et d'italique à la cellule.
         applyTextAndItalicStyleToCell(cell, dataCell);

         // Appliquer les bordures définies dans la DataCell à la cellule.
         applyBorders(cell, dataCell);

      }
   }

   private void writeCellRowCHT(Sheet sheet, int rowIndex, CellRow cellRow){
      int nbCell =  cellRow.getListDataCell().size();
      
      //il faut :
      //- créer la row :
      Row row = sheet.createRow(rowIndex);
      //boucler pour créer les cellules
      
      for(int colIndex = 0; colIndex < nbCell; colIndex++){
         //Mettre tout dans une méthosz addCellToRow(Row row);
         addCellToRow(row, cellRow, rowIndex, colIndex);
      }
   }

   //CHT :
   private void addCellToRow(Row row, CellRow cellRow, int rowIndex, int colIndex) {
      Cell cell = row.createCell(colIndex);
      Sheet sheet = row.getSheet();
      
      DataCell dataCell = cellRow.getListDataCell().get(colIndex);

      if(dataCell!= null) {
         //application du style
         CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
         cell.setCellStyle(cellStyle);
         applyStyle(dataCell, cellStyle);//hexaColorCodeForLeftBorder + withBorder + alignment
         CellContent content = dataCell.getCellContent();
         if(content != null) {
            cellStyle.setWrapText(content.isComplementOnAnotherLine());
         }
         
         //prise en compte du colspan :
         if(dataCell.getColspan() > 1) {
            int endColIndex = colIndex + dataCell.getColspan() - 1;
            CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, colIndex, endColIndex);
            sheet.addMergedRegion(region);
            // Sauter les colonnes fusionnées pour éviter de traiter ces cellules à nouveau.
            colIndex += (dataCell.getColspan() - 1); 
         }
         
         //Gestion du contenu
         applyContent(cell, content);
      }
   }

   //simplification de applyTextAndItalicStyleToCell
   private void applyContent(Cell cell, CellContent content){
      if(cell != null) {
         Workbook wb = cell.getSheet().getWorkbook();
         String contentAsString = content.buildContentValue();
         //Font normalFont = wb.createFont();//CHT : à mettre dans un cache
         
         //normalFont.setFontHeightInPoints(FONT_SIZE__11);
         if(content.isComplementInItalic()) {
            //Font italicFont = wb.createFont();//CHT : à mettre dans un cache
            //italicFont.setFontHeightInPoints(FONT_SIZE__11);
            //italicFont.setItalic(true);
            RichTextString richText = new XSSFRichTextString(contentAsString);
            richText.applyFont(0, content.getText().length(), getFont(EDocumentExcelFont.DEFAULT_NORMAL, wb));
            richText.applyFont(content.getText().length(), richText.length(), getFont(EDocumentExcelFont.DEFAULT_ITALIC, wb));
            cell.setCellValue(richText);
         }
         else {
            cell.setCellValue(contentAsString);
         }
      }
   }

   private void applyStyle(DataCell dataCell, CellStyle cellStyle) {
      if(dataCell != null && cellStyle != null) {
         //alignement
         AlignmentType alignmentType = dataCell.getAlignmentType();
         switch(alignmentType){
            case CENTER:
               cellStyle.setAlignment(HorizontalAlignment.CENTER);
               break;
            case LEFT:
               cellStyle.setAlignment(HorizontalAlignment.LEFT);
               break;
            case RIGHT:
               cellStyle.setAlignment(HorizontalAlignment.RIGHT);
               break;
         }
         //bordure
         if(dataCell.isWithBorder()) {//on met du noir simple sur tous les côtés. Sera surchargé si besoin d'un border left particulier
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            //((XSSFCellStyle)cellStyle).setLeftBorderColor(BLACK__XSSF_COLOR);
         }
         //si couleur côté gauche, surcharge :
         if(dataCell.getHexaColorCodeForLeftBorder() != null) {
            cellStyle.setBorderLeft(BorderStyle.THICK);
            ((XSSFCellStyle)cellStyle).setLeftBorderColor(retrieveXSSFColorFromHex(dataCell.getHexaColorCodeForLeftBorder()));
         }
      }
   }
   
   

   /**
    * Applique les styles de texte et d'italique à la cellule spécifiée.
    *
    * @param cell     La cellule à laquelle les styles seront appliqués.
    * @param dataCell L'objet DataCell contenant les informations de style.
    *
    */
   private void applyTextAndItalicStyleToCell(Cell cell, DataCell dataCell){
      // Récupérer le contenu de la cellule depuis l'objet DataCell
      CellContent content = dataCell.getCellContent();

      // Extraire le texte normal et le texte en italique du contenu
      String normalText = (content.getText() == null ? "":content.getText());//CHT : pour être cohérent avec ce qui est désormais fait dans CellContent
      //CHT : variable à renommer en complement. Si isComplementInItalic() est à false le texte n'est pas en italique donc le nom de variable est trompeur
      String italicText = content.getComplement();
      
      // Vérifier si le complément doit être affiché en italique
      if (content.isComplementInItalic() && italicText!= null && !italicText.isEmpty()) {
         // Écrire dans la cellule avec une partie en italique
         //CHT : String.format() n'est pas fait pour faire de la concaténation
         //les parenthèses n'ont pas à être mises ici mais avant : il faut prendre le texte tel quel comme c'est fait plus bas
         //sinon on a une règle métier dans une classe qui ne doit pas en avoir
         String alias = String.format("(%s)", italicText);
         ExcelUtility.writeToCellWithHalfItalic(cell, normalText, alias , (short) 11);//CHT : 11 à mettre en constante mais pourquoi dans ce cas on gère une taille alors que ce n'est pas le cas dans les autres méthodes ?
      } else {
         // Écrire seulement le texte normal dans la cellule
         ExcelUtility.writeToCell(cell.getSheet(), cell.getRowIndex(), cell.getColumnIndex(), normalText);
         if (italicText != null) {
            // Si un texte complémentaire existe, l'ajouter après le texte normal
            String fullText = normalText + " " + italicText;
            //CHT : pourquoi on ne gère pas de taille de font dans ce cas (puisqu'on en gère un dans le cas précédent) ?
            ExcelUtility.writeToCell(cell.getSheet(), cell.getRowIndex(), cell.getColumnIndex(), fullText);
         }
      }
   }

   //CHT : l'utilisation de la mécanique des Border est trop compliquée. Il faut appeler directement l'api
   /**
    * Applique les bordures à la cellule spécifiée selon les propriétés définies dans DataCell.
    *
    * @param cell La cellule à laquelle les bordures seront appliquées.
    *             Doit être une instance valide de Cell.
    * @param dataCell L'objet DataCell contenant des informations sur les bordures.
    *                 Doit être une instance valide de DataCell avec des propriétés de bordure appropriées.
    */
   private void applyBorders(Cell cell, DataCell dataCell) {
      // Liste pour stocker les propriétés des bordures à appliquer
      List<ExcelUtility.BorderProperties> borderPropertiesList = new ArrayList<>();

      // Récupérer la couleur hexadécimale pour la bordure gauche
      String leftBorderHexaColor = dataCell.getHexaColorCodeForLeftBorder();

      // Ajouter toutes les bordures si spécifié par DataCell
      if (dataCell.isWithBorder()) {
         // Définir couleur par défaut si aucune couleur n'est spécifiée pour la gauche
         String leftBorderColor = (leftBorderHexaColor != null ? leftBorderHexaColor : BLACK_HEX_CODE);

         // Ajouter chaque côté avec une épaisseur et couleur déterminée
         borderPropertiesList.add(new ExcelUtility.BorderProperties(BorderStyle.THICK, leftBorderColor, ExcelUtility.BorderDirection.LEFT));
         borderPropertiesList.add(new ExcelUtility.BorderProperties(BorderStyle.THICK, BLACK_HEX_CODE, ExcelUtility.BorderDirection.RIGHT));
         borderPropertiesList.add(new ExcelUtility.BorderProperties(BorderStyle.THICK, BLACK_HEX_CODE, ExcelUtility.BorderDirection.TOP));
         borderPropertiesList.add(new ExcelUtility.BorderProperties(BorderStyle.THICK, BLACK_HEX_CODE, ExcelUtility.BorderDirection.BOTTOM));

         // Appliquer tous les styles de bordure accumulés à la cellule
         ExcelUtility.applyCustomBordersAndCenterContent(cell, borderPropertiesList, false);
      }

      // Appliquer uniquement la bordure gauche si elle est spécifiée mais pas d'autres bords
      if (leftBorderHexaColor != null && !dataCell.isWithBorder()) {
         // Ajouter seulement la propriété pour la bordure gauche avec sa couleur spécifique

         borderPropertiesList.add(new ExcelUtility.BorderProperties(BorderStyle.THICK,leftBorderHexaColor,
            ExcelUtility.BorderDirection.LEFT));

         // Appliquer uniquement cette configuration à la cellule
         ExcelUtility.applyCustomBordersAndCenterContent(cell, borderPropertiesList, false);
      }
   }

   //CHT :
   private XSSFColor retrieveXSSFColorFromHex(String hexCode) {
      if(hexCode != null) {
         XSSFColor result = mapColor.get(hexCode);
         if(result == null) {
            try {
               result = new XSSFColor();
               //il faut retirer le caractère # en première position
               result.setARGBHex(hexCode.substring(1));
               mapColor.put(hexCode, result);
            }
            catch (IllegalArgumentException e) {//exception pouvant être lancée par setARGBHex
               //log.debug();
               return BLACK__XSSF_COLOR;
            }
         }
         return result;
      }
      return BLACK__XSSF_COLOR;
   }
   
   private enum EDocumentExcelFont {
      DEFAULT_NORMAL,
      DEFAULT_BOLD,
      DEFAULT_ITALIC;
   }

   private Font getFont(EDocumentExcelFont eDocumentExcelFont, Workbook wb) {
      Font font = mapFont.get(eDocumentExcelFont);
      if(font == null) {
         font = wb.createFont();
         switch(eDocumentExcelFont){
            case DEFAULT_NORMAL :
               //on ne fait rien
               break;
            case DEFAULT_BOLD :
               font.setBold(true);
               break;
            case DEFAULT_ITALIC :  
               font.setItalic(true);
               break;
         }
         
         mapFont.put(eDocumentExcelFont, font);
      }
      
      return font;
   }
}
