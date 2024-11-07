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

import org.apache.commons.io.output.ByteArrayOutputStream;
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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.EnumMap;
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

   private static final Logger logger = LoggerFactory.getLogger(DocumentWithDataAsTableExcelProducer.class);

   private static final XSSFColor BLACK_XSSF_COLOR = new XSSFColor(new java.awt.Color(0, 0, 0));

   // Map servant de cache pour faire le lien entre un code hexadecimal de TK et le XSSFColor associé
   private final Map<String, XSSFColor> colorCacheMap = new HashMap<>();

   // Map servant de cache pour les fonts à utiliser
   // EnumMap est utilisé ici pour une meilleure performance et efficacité mémoire avec des clés enum.
   private final EnumMap<ExcelFontStyle, Font> fontCacheMap = new EnumMap<>(ExcelFontStyle.class);


    /**
    * Produit un document Excel à partir d'une liste de documents contenant des données tabulaires.
    * 
    * <p>Cette méthode génère un fichier Excel XLSX en créant une feuille distincte pour chaque document 
    * de la liste fournie. Pour chaque document, elle :</p>
    * <ul>
    *   <li>Crée une nouvelle feuille avec le nom du document</li>
    *   <li>Configure la largeur par défaut des colonnes</li>
    *   <li>Écrit le contexte du document</li>
    *   <li>Écrit les données tabulaires</li>
    *   <li>Ajoute un pied de page</li>
    * </ul>
    *
    * @param listDocumentWithDataAsTable Liste des documents à traiter
    * @param defaultColumnWidth Largeur par défaut des colonnes (en unités de caractères)
    * @return Un objet DocumentProducerResult contenant le fichier Excel généré
    * @throws IOException En cas d'erreur lors de l'écriture du fichier
    */
   @Override
   public DocumentProducerResult produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable, int defaultColumnWidth)
      throws IOException{
      // Initialisation du résultat à renvoyer
      DocumentProducerResult result = new DocumentProducerResult();
      result.setFormat(ConfigManager.EXCEL_XLSX_FILETYPE);     // Définit le format du fichier comme XLSX (Excel)
      // Définit le type MIME pour un fichier Excel au format OpenXML
      result.setContentType(ConfigManager.OFFICE_OPENXML_MIME_TYPE);

      // Utilisation de try-with-resources pour gérer automatiquement la fermeture des ressources
      try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           Workbook workbook = new XSSFWorkbook() ){ // Création d'un nouveau classeur Ex
         // Parcourt chaque document dans la liste fournie
         for(DocumentWithDataAsTable document : listDocumentWithDataAsTable){

            String sheetName = document.getDocumentName(); // Récupère le nom de la feuille

            // Crée une nouvelle feuille dans le classeur avec le nom spécifié
            Sheet sheet = ExcelUtility.createSheet(workbook, sheetName);

            // Définit la largeur par défaut des colonnes pour une meilleure lisibilité
            sheet.setDefaultColumnWidth(defaultColumnWidth);

            // Écrit le contexte du document dans la feuille afin que les utilisateurs aient un aperçu des informations du contexte
            writeDocumentContext(sheet, document.getContext());

            // Écrit les données du document dans la feuille, ce qui constitue l'essentiel du contenu
            writeDocumentData(sheet, document.getData());

            // Ajoute un pied de page à la feuille pour fournir des informations supplémentaires ou des références
            ExcelUtility.addFooter(sheet, document.getFooter().getLeftData(),
                                          document.getFooter().getCenterData(),
                                          document.getFooter().getRightData());
         }

         // Écrit le contenu complet du classeur dans le flux de sortie pour être utilisé ultérieurement
         workbook.write(outputStream);

         // Définit le flux de sortie dans le résultat afin qu'il puisse être récupéré après l'exécution
         result.setOutputStream(outputStream);

      }catch(IOException e){
         // Log l'erreur avec un message utile
         logger.error("Error writing the Excel file: {}", e.getMessage(), e);
         // Relance l'exception après enregistrement dans les logs pour une gestion appropriée en amont
         throw e;
      }

      return result; // Retourne le résultat final contenant toutes les données produites sous forme d'Excel

   }

   /**
    * Écrit le contexte du document dans une feuille Excel.
    * 
    * <p>Cette méthode parcourt la liste des paires label-valeur du contexte et les écrit
    * dans la feuille Excel. Chaque paire est écrite sur une nouvelle ligne, avec :</p>
    * <ul>
    *   <li>Le label dans la première colonne</li>
    *   <li>La valeur dans la deuxième colonne</li>
    * </ul>
    * 
    * <p>Le formatage en gras est appliqué selon les propriétés isLabelInBold() et isValueInBold()
    * de chaque LabelValue.</p>
    *
    * @param sheet La feuille Excel où écrire le contexte
    * @param documentContext Le contexte du document contenant les paires label-valeur
    */
   private void writeDocumentContext(Sheet sheet, DocumentContext documentContext){
      // Vérifie si la feuille et le contexte du document ne sont pas nuls avant de procéder.
      if(sheet != null && documentContext != null){
         // Récupère la liste des LabelValue à partir du contexte du document.
         List<LabelValue> labelValues = documentContext.getListLabelValue();
         // Assure que la liste des LabelValue n'est pas nulle pour éviter NullPointerException.
         if(labelValues != null){
            // Initialisation des indices de ligne et colonne pour l'écriture dans la feuille.
            int rowIndex = 0; // on commence sur la 1ere ligne du document
            int colIndexLabel = 0;  // Colonne pour le label.

            // Crée un style de cellule en gras pour les labels ou valeurs qui nécessitent ce formatage.
            CellStyle boldCellStyle = sheet.getWorkbook().createCellStyle();
            boldCellStyle.setFont(getFont(ExcelFontStyle.BOLD, sheet.getWorkbook()));

            // Parcourt chaque LabelValue dans la liste fournie par le DocumentContext.
            for(LabelValue labelValue : labelValues){
               // Crée une nouvelle ligne dans la feuille pour chaque paire label-valeur.
               Row row = sheet.createRow(rowIndex);

               // Crée une cellule pour le label au sein de cette ligne, utilisant l'index de colonne approprié.
               Cell cellLabel = row.createCell(colIndexLabel);

               // Applique le style gras au label si spécifié par l'objet LabelValue.
               if(labelValue.isLabelInBold()){
                  cellLabel.setCellStyle(boldCellStyle);
               }
               // Définit la valeur du label dans la cellule créée précédemment.
               cellLabel.setCellValue(labelValue.getLabel());
               // Crée une cellule adjacente pour la valeur correspondante au label actuel.
               Cell cellValue = row.createCell(colIndexLabel + 1);
               // Applique le style gras à la valeur si spécifié par l'objet LabelValue.
               if(labelValue.isValueInBold()){
                  cellValue.setCellStyle(boldCellStyle);
               }
               // Définit la valeur correspondante dans cette cellule adjacente.
               cellValue.setCellValue(labelValue.getValue());
               // Passe à la ligne suivante après avoir écrit une paire complète (label et valeur).
               rowIndex++;
            }
            // Ajoute une ligne vide après avoir écrit toutes les paires label-valeur,
            // permettant ainsi d'améliorer l'esthétique et lisibilité du tableau final
            sheet.createRow(rowIndex);
         }
      }
   }

   /**
    * Écrit les données tabulaires dans une feuille Excel.
    * 
    * <p>Cette méthode prend les données structurées d'un DataAsTable et les transcrit
    * dans la feuille Excel fournie. L'écriture commence à la première ligne disponible
    * après le contenu existant dans la feuille.</p>
    *
    * @param sheet La feuille Excel où écrire les données
    * @param dataAsTable L'objet contenant les données tabulaires à écrire
    * @throws IllegalArgumentException Si sheet ou dataAsTable est null
    */
   private void writeDocumentData(Sheet sheet, DataAsTable dataAsTable){
      // Vérifiez si la feuille (sheet) et le tableau de données (dataAsTable) ne sont pas nuls.
      if(sheet != null && dataAsTable != null){
         // Obtenez le nombre de lignes physiques dans la feuille,
         // cela nous permet de commencer à écrire à la suite des données existantes.
         int rowIndex = sheet.getPhysicalNumberOfRows();
         // Parcourez chaque ligne de cellules dans le tableau de données.
         // Chaque CellRow représente une ligne à écrire dans la feuille.
         for(CellRow cellRow : dataAsTable.getListCellRow()){
            // Écrivez la ligne actuelle (cellRow) dans la feuille à l'index spécifié (rowIndex).
            writeCellRow(sheet, rowIndex, cellRow);
            // Incrémentez l'index de ligne pour passer à la prochaine ligne disponible
            // afin d'éviter d'écraser les données précédemment écrites.
            rowIndex++;
         }
      }else{
         // Si soit la feuille soit le tableau de données est nul,
         // loggez un avertissement pour indiquer que l'opération d'écriture ne peut pas être effectuée.
         logger.warn("Échec de l'opération d'écriture : la feuille ou le tableau de données est nul.");
         throw new IllegalArgumentException("La feuille ou le tableau de données ne doit pas être nul.");
      }

   }

   /**
    * Écrit une ligne de données dans une feuille Excel.
    * 
    * <p>Cette méthode crée une nouvelle ligne dans la feuille et y ajoute les cellules
    * selon les spécifications de l'objet CellRow fourni.</p>
    *
    * @param sheet La feuille Excel où écrire la ligne
    * @param rowIndex L'index de la ligne à créer
    * @param cellRow L'objet contenant les données de la ligne à écrire
    */
   private void writeCellRow(Sheet sheet, int rowIndex, CellRow cellRow) {
      if (cellRow != null && cellRow.getListDataCell() != null) {
          Row row = sheet.createRow(rowIndex);
          int currentColumn = 0; // Track the current column position
          
          for (DataCell dataCell : cellRow.getListDataCell()) {
              writeCellRowToExcel(row, dataCell, rowIndex, currentColumn);
              
              // Increment by colspan or 1 if no colspan
             if (dataCell != null ){
                currentColumn += Math.max(dataCell.getColspan(), 1);
                System.out.println("Next column position will be: " + currentColumn);
             }

          }
      }
   }

   private void writeCellRowToExcel(Row row, DataCell dataCell, int rowIndex, int colIndex) {
      if (row == null) {
          logger.warn("Row is null at rowIndex {}", rowIndex);
          return;
      }

      Sheet sheet = row.getSheet();
      
      if (dataCell != null) {
          System.out.println("\ntrying to write datacell to excel: " + dataCell.toString());
          System.out.println(" at column: " + colIndex);

          Cell mainCell = row.createCell(colIndex);
          CellStyle mainCellStyle = sheet.getWorkbook().createCellStyle();
          mainCell.setCellStyle(mainCellStyle);
          applyStyle(dataCell, mainCellStyle);

          if (dataCell.getColspan() > 1) {
              int lastColIndex = colIndex + dataCell.getColspan() - 1;
              System.out.println(" merging to column: " + lastColIndex);
              
              // Create and style all cells in the merge range
              for (int i = colIndex + 1; i <= lastColIndex; i++) {
                  Cell cell = row.createCell(i);
                  CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                  if (dataCell.isWithBorder()) {
                      cellStyle.setBorderBottom(BorderStyle.THIN);
                      cellStyle.setBorderTop(BorderStyle.THIN);
                      cellStyle.setBorderRight(BorderStyle.THIN);
                      cellStyle.setBorderLeft(BorderStyle.THIN);
                  }
                  
                  // Apply special left border if specified
                  if (dataCell.getHexaColorCodeForLeftBorder() != null) {
                      cellStyle.setBorderLeft(BorderStyle.THICK);
                      ((XSSFCellStyle) cellStyle).setLeftBorderColor(
                          retrieveXSSFColorFromHex(dataCell.getHexaColorCodeForLeftBorder())
                      );
                  }
                  
                  cell.setCellStyle(cellStyle);
              }

              try {
                  CellRangeAddress region = new CellRangeAddress(
                      rowIndex, rowIndex, colIndex, lastColIndex);
                  sheet.addMergedRegion(region);
              } catch (IllegalArgumentException e) {
                  logger.error("Failed to merge: row={}, cols={}-{}: {}", 
                      rowIndex, colIndex, lastColIndex, e.getMessage());
              }
          }

          CellContent content = dataCell.getCellContent();
          if (content != null) {
              mainCellStyle.setWrapText(content.isComplementOnAnotherLine());
          }

          applyCellContent(mainCell, content);
      }
   }

   /**
    * Applique le contenu à une cellule Excel avec le formatage de texte approprié.
    * 
    * <p>Cette méthode gère le formatage du texte, notamment :</p>
    * <ul>
    *   <li>L'application de l'italique sur les parties complémentaires du texte</li>
    *   <li>La gestion du texte enrichi (RichTextString)</li>
    * </ul>
    *
    * @param cell La cellule Excel à remplir
    * @param content Le contenu à appliquer à la cellule
    */
   private void applyCellContent(Cell cell, CellContent content){
      System.out.println("applyCellContent to  " + content.getText());
      if (cell != null && content != null) {
          Workbook wb = cell.getSheet().getWorkbook();
          String contentAsString = content.buildContentValue();

          if (content.isComplementInItalic()) {
              RichTextString richText = new XSSFRichTextString(contentAsString);
              richText.applyFont(0, content.getText().length(), getFont(ExcelFontStyle.NORMAL, wb));
              richText.applyFont(content.getText().length(), richText.length(), getFont(ExcelFontStyle.ITALIC, wb));
              cell.setCellValue(richText);
          } else {
              cell.setCellValue(contentAsString);
          }
      } else {
          logger.warn("Cell or content is null when applying content");
      }
   }

    /**
    * Applique les styles visuels à une cellule Excel.
    * 
    * <p>Cette méthode configure :</p>
    * <ul>
    *   <li>L'alignement horizontal du contenu</li>
    *   <li>Les bordures de la cellule</li>
    *   <li>Les couleurs des bordures spécifiques</li>
    * </ul>
    *
    * @param dataCell L'objet contenant les spécifications de style
    * @param cellStyle Le style de cellule Excel à configurer
    */
   private void applyStyle(DataCell dataCell, CellStyle cellStyle) {
      if(dataCell != null && cellStyle != null) {
         // Récupération du type d'alignement défini dans la cellule de données
          AlignmentType alignmentType = dataCell.getAlignmentType();
         // Application du type d'alignement au style de cellule
          switch(alignmentType) {
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
         // Vérification si la cellule doit avoir des bordures visibles
          if(dataCell.isWithBorder()) {
              cellStyle.setBorderBottom(BorderStyle.THIN);
              cellStyle.setBorderTop(BorderStyle.THIN);
              cellStyle.setBorderRight(BorderStyle.THIN);
              cellStyle.setBorderLeft(BorderStyle.THIN);
          }
         // Vérifie s'il y a un code couleur hexadécimal pour la bordure gauche
          if(dataCell.getHexaColorCodeForLeftBorder() != null) {
             // Définit une bordure épaisse pour la bordure gauche afin d'accentuer cette partie
              cellStyle.setBorderLeft(BorderStyle.THICK);
              ((XSSFCellStyle) cellStyle).setLeftBorderColor(
                 // Récupère et applique la couleur correspondante à partir du code hexadécimal fourni
                  retrieveXSSFColorFromHex(dataCell.getHexaColorCodeForLeftBorder())
              );
          }
      } else {
         // Enregistre un avertissement si l'un des paramètres est nul pour éviter des erreurs lors du traitement ultérieur.
         logger.warn("DataCell ou CellStyle est nul lors de l'application du style.");
      }
   }

   /**
    * Récupère ou crée une couleur XSSFColor à partir d'un code hexadécimal.
    * 
    * <p>Cette méthode utilise un cache pour optimiser les performances en évitant
    * de recréer les mêmes couleurs. Si le code hexadécimal est invalide ou null,
    * retourne la couleur noire par défaut.</p>
    *
    * @param hexCode Le code hexadécimal de la couleur (format "#RRGGBB")
    * @return La couleur XSSFColor correspondante
    */
   private XSSFColor retrieveXSSFColorFromHex(String hexCode) {
      // Vérification initiale du code hexadécimal pour éviter le traitement de valeurs nulles
      if(hexCode != null) {
          // Tentative de récupération de la couleur depuis le cache
          // L'utilisation du cache améliore significativement les performances en évitant
          // de recréer les mêmes couleurs plusieurs fois
          XSSFColor result = colorCacheMap.get(hexCode);
          
          // Si la couleur n'existe pas encore dans le cache, nous devons la créer
          if(result == null) {
              try {
                  // Création d'une nouvelle instance de XSSFColor
                  // Cette approche est plus coûteuse en ressources, d'où l'importance du cache
                  result = new XSSFColor();
                  
                  // Conversion du code hexadécimal en format compatible avec XSSFColor
                  // Le substring(1) retire le caractère '#' du début du code hexadécimal
                  result.setARGBHex(hexCode.substring(1));
                  
                  // Stockage de la nouvelle couleur dans le cache pour une utilisation future
                  // Optimisation cruciale pour les documents contenant beaucoup de cellules colorées
                  colorCacheMap.put(hexCode, result);
                  
              } catch(IllegalArgumentException e) {
                  // Gestion appropriée des erreurs pour les codes hexadécimaux invalides
                  // La journalisation aide au débogage tout en maintenant la stabilité du programme
                  logger.error("Invalid hex color code: {}", hexCode, e);
                  // Retour d'une couleur par défaut (noir) pour assurer la continuité du programme
                  return BLACK_XSSF_COLOR;
              }
          }
          // Retour de la couleur, qu'elle soit nouvelle ou récupérée du cache
          return result;
      }
      // Retour de la couleur noire par défaut si le code hexadécimal est null
      // Cela assure que la méthode renvoie toujours une valeur valide
      return BLACK_XSSF_COLOR;
   }

   /** Enum représentant différents types typographiques utilisés lors de l'application des polices aux cellules Excel.*/

   private enum ExcelFontStyle
   {
      NORMAL, BOLD, ITALIC
   }

   /**
    * Récupère ou crée une police Excel selon le type spécifié.
    * 
    * <p>Cette méthode utilise un cache pour optimiser les performances en évitant
    * de recréer les mêmes polices. Les types de police disponibles sont :</p>
    * <ul>
    *   <li>DEFAULT_NORMAL : Police standard</li>
    *   <li>DEFAULT_BOLD : Police en gras</li>
    *   <li>DEFAULT_ITALIC : Police en italique</li>
    * </ul>
    *
    * @param excelFontStyle Le type de police souhaité
    * @param wb Le classeur Excel pour créer la police si nécessaire
    * @return La police configurée selon le type demandé
    */
   private Font getFont(ExcelFontStyle excelFontStyle, Workbook wb){
      // Vérifie si la police associée à l'excelFontStyle existe déjà dans le cache.
      Font font = fontCacheMap.get(excelFontStyle);

      // Si la police n'est pas encore dans le cache, nous devons en créer une nouvelle.
      if(font == null){
         // Crée une nouvelle instance de Font à partir du classeur fourni.
         font = wb.createFont();
         // Selon le type de police spécifié par excelFontStyle,
         // nous appliquons les attributs appropriés à cette nouvelle instance de Font.
         switch(excelFontStyle){
            case NORMAL:
               //on ne fait rien
               break;
            case BOLD:
               font.setBold(true);
               break;
            case ITALIC:
               font.setItalic(true);
               break;
         }

         // Une fois que nous avons configuré la nouvelle instance de Font,
         // nous l'ajoutons au cache pour que les appels futurs puissent réutiliser
         // cette même instance plutôt que d'en créer une nouvelle.
         fontCacheMap.put(excelFontStyle, font);
      }
      // Retourne la police récupérée ou nouvellement créée pour être utilisée par l'appelant.
      return font;
   }
}
