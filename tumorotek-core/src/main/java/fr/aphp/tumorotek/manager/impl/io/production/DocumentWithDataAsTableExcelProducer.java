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
import fr.aphp.tumorotek.manager.io.production.DocumentProducerResult;
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
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
 * et permet de produire un fichier Excel constitué de plusieurs feuilles
 * contenant chacune un tableau de données, avec éventuellement quelques lignes de contexte au dessus du tableau.
 * Le contenu de chaque feuille est défini par un {@link DocumentWithDataAsTable}.
 * 
 * Cette classe n'expose qu'une seule méthode produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable)
 *
 * <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public class DocumentWithDataAsTableExcelProducer implements DocumentProducer
{

   private static final Logger logger = LoggerFactory.getLogger(DocumentWithDataAsTableExcelProducer.class);

   //Constante de type XSSFColor qui correspond à la couleur "Noir".
   private static final XSSFColor BLACK_XSSF_COLOR = new XSSFColor(new java.awt.Color(0, 0, 0));

   // Map servant de cache pour faire le lien entre un code hexadecimal de TK et le XSSFColor associé
   private final Map<String, XSSFColor> colorCacheMap = new HashMap<>();

   // Map servant de cache pour les fonts à utiliser
   // EnumMap est utilisé ici pour une meilleure performance et efficacité mémoire dans le cas de clés de type enum.
   private final EnumMap<EDocumentExcelFont, Font> fontCacheMap = new EnumMap<>(EDocumentExcelFont.class);

   /**
    * Produit un document Excel à partir d'une liste de documents contenant des données sous forme de tableau.
    *
    * <p>Cette méthode génère un fichier Excel XLSX en créant une feuille distincte pour chaque document 
    * de la liste fournie. Pour chaque document, elle :</p>
    * <ul>
    *   <li>Crée une nouvelle feuille avec le nom du document</li>
    *   <li>Configure la largeur par défaut des colonnes</li>
    *   <li>Écrit le contexte du document sous la forme de plusieurs lignes contenant un libellé et une valeur</li>
    *   <li>Saute une ligne</li>
    *   <li>Écrit les données sous la forme d'un tableau</li>
    *   <li>Ajoute un pied de page</li>
    * </ul>
    *
    * @param listDocumentWithDataAsTable Liste des documents à traiter
    * @return Un objet DocumentProducerResult contenant les éléments constituant le fichier Excel à générer
    * @throws IOException En cas d'erreur lors de l'écriture du fichier
    */
   @Override
   public DocumentProducerResult produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable)
      throws IOException{
      // Utilisation de try-with-resources pour gérer automatiquement la fermeture des ressources
      try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         Workbook workbook = new XSSFWorkbook() ){ // Création d'un nouveau classeur Ex
         
         //nettoyage des caches avant de gérer un nouveau workbook pour éviter une erreur lors de l'ouverture du fichier.
         colorCacheMap.clear();
         fontCacheMap.clear();
         
         // Parcourt chaque document de la liste fournie
         for(DocumentWithDataAsTable document : listDocumentWithDataAsTable){

            String sheetName = document.getDocumentName(); // Récupère le nom de la feuille
            Sheet sheet = ExcelUtility.createSheet(workbook, sheetName);
            // Gère la largeur de colonne par défaut
            if (document.getColumnWidth() > 0) {
               sheet.setDefaultColumnWidth(document.getColumnWidth());
            }

            // Écrit le contexte du document dans la feuille afin que les utilisateurs 
            // sachent à quoi correspondent les données qui seront ensuite affichées sous forme de tableau
            writeDocumentContext(sheet, document.getContext(), document.isWithLeftMargin());

            // Écrit les données du document dans la feuille, ce qui constitue l'essentiel du contenu
            writeDocumentData(sheet, document.getData(), document.isWithLeftMargin());

            // Ajoute un pied de page à la feuille pour fournir des informations supplémentaires ou des références
            ExcelUtility.addFooter(sheet, document.getFooter().getLeftData(), document.getFooter().getCenterData(),
               document.getFooter().getRightData());
         }

         // Écrit le contenu complet du classeur dans le flux de sortie pour être utilisé ultérieurement
         workbook.write(outputStream);

         return new DocumentProducerResult(ConfigManager.EXCEL_XLSX_FILETYPE, ConfigManager.OFFICE_OPENXML_MIME_TYPE, outputStream);
      }catch(IOException e){
         // Log l'erreur avec un message utile
         logger.error("Error writing the Excel file: {}", e.getMessage(), e);
         // Relance l'exception après enregistrement dans les logs pour une gestion appropriée en amont
         throw e;
      }
   }

   /**
    * Écrit le contexte du document en haut de la feuille Excel. Permet d'indiquer à
    * l'utilisateur à quoi correspondent les données du document
    *
    * <p>Cette méthode parcourt la liste des paires label-valeur du contexte.
    * chaque paire est écrite sur une nouvelle ligne, avec :</p>
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
    * @param withLeftMargin ajoute une colonne vide en première position pour simuler une marge (peut être utile par exemple 
    * dans le cas des plans de conteneur pour que la couleur des éléments dans la première colonne soit bien visible)
    * 
    */
   private void writeDocumentContext(Sheet sheet, DocumentContext documentContext, boolean withLeftMargin){
      // Vérifie si la feuille et le contexte du document ne sont pas nuls avant de procéder.
      if(sheet != null && documentContext != null){
         // Récupère la liste des LabelValue à partir du contexte du document.
         List<LabelValue> labelValues = documentContext.getListLabelValue();
         // Assure que la liste des LabelValue n'est pas nulle pour éviter NullPointerException.
         if(labelValues != null){
            // Initialisation des indices de ligne et colonne pour l'écriture dans la feuille.
            int rowIndex = 0; // on commence sur la 1ere ligne du document
            int colIndexLabel = (withLeftMargin ? 1 : 0); // Colonne pour le label.
            
            // Crée un style de cellule en gras pour les labels ou valeurs qui nécessitent ce formatage.
            CellStyle boldCellStyle = sheet.getWorkbook().createCellStyle();
            boldCellStyle.setFont(retrieveFont(EDocumentExcelFont.DEFAULT_BOLD, sheet.getWorkbook()));

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
    * Écrit les données sous forme de tableau dans une feuille Excel.
    *
    * <p>Cette méthode prend les données structurées d'un DataAsTable et les transcrit
    * dans la feuille Excel fournie. L'écriture commence à la première ligne disponible
    * après le contenu existant dans la feuille.</p>
    *
    * @param sheet La feuille Excel où écrire les données
    * @param dataAsTable L'objet contenant les données à écrire sous forme de tableau
    * @param withLeftMargin ajoute une colonne vide en première position pour simuler une marge (peut être utile par exemple 
    * dans le cas des plans de conteneur pour que la couleur des éléments dans la première colonne soit bien visible)
    */
   private void writeDocumentData(Sheet sheet, DataAsTable dataAsTable, boolean withLeftMargin){
      // Vérifiez si la feuille (sheet) et le tableau de données (dataAsTable) ne sont pas nuls.
      if(sheet != null && dataAsTable != null){
         // Obtenez le nombre de lignes physiques dans la feuille,
         // cela nous permet de commencer à écrire à la suite des données existantes.
         int rowIndex = sheet.getPhysicalNumberOfRows();
         // Parcourez chaque ligne de cellules dans le tableau de données.
         // Chaque CellRow représente une ligne à écrire dans la feuille.
         for(CellRow cellRow : dataAsTable.getListCellRow()){
            // Écrivez la ligne actuelle (cellRow) dans la feuille à l'index spécifié (rowIndex).
            writeCellRow(sheet, rowIndex, cellRow, withLeftMargin);
            // Incrémentez l'index de ligne pour passer à la prochaine ligne disponible
            // afin d'éviter d'écraser les données précédemment écrites.
            rowIndex++;
         }
      }else{
         // Si soit la feuille soit le tableau de données est nul,
         // loggez un avertissement pour indiquer que l'opération d'écriture ne peut pas être effectuée.
         logger.error("Échec de l'opération d'écriture des données : la feuille et le tableau de données ne doivent pas être nulles. Or sheet : {}, dataAsTable : {}",
            (sheet == null ? "null" : sheet), (dataAsTable == null ? "null" : dataAsTable));
         throw new IllegalArgumentException("La feuille et le tableau de données ne doivent pas être nulles.");
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
    * @param withLeftMargin ajoute une colonne vide en première position pour simuler une marge (peut être utile par exemple 
    * dans le cas des plans de conteneur pour que la couleur des éléments dans la première colonne soit bien visible)
    */
   private void writeCellRow(Sheet sheet, int rowIndex, CellRow cellRow, boolean withLeftMargin) {
      // Vérifie que cellRow et sa liste de cellules ne sont pas nulles
      if(cellRow != null && cellRow.getListDataCell() != null) {
          // Crée une nouvelle ligne dans la feuille Excel à l'index spécifié
          Row row = sheet.createRow(rowIndex);
          //currentColumn commence à 0 ou à 1 selon la demande d'avoir une marge à gauche ou non
          int currentColumn = (withLeftMargin ? 1 : 0);

          // Parcourt chaque cellule de données dans la ligne
          for(DataCell dataCell : cellRow.getListDataCell()) {
              if(dataCell != null) {
                  Cell cell = row.createCell(currentColumn);
                  CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                  // Applique le style à la cellule
                  cell.setCellStyle(cellStyle);
                  // Applique les styles spécifiques (alignement, bordures, etc.)
                  applyStyle(dataCell, cellStyle);

                  // Gestion du colspan (fusion de cellules) si nécessaire
                  if(dataCell.getColspan() > 1) {
                      // Calcule l'index de la dernière colonne pour la fusion
                      int lastColIndex = currentColumn + dataCell.getColspan() - 1;

                      // Pour appliquer un style à la cellule résultat de la fusion,
                      // il faut soit que toutes les cellules fusionnées aient le même style
                      // soit passer par RegionUtil. 
                      // Ca paraît plus simple d'un point de vue code d'appliquer le style sur toutes les cellules
                      // => Création des autres cellules de la plage de fusion
                      for(int i = currentColumn + 1; i <= lastColIndex; i++) {
                          // Crée une cellule supplémentaire
                          Cell cellAFusionner = row.createCell(i);
                          cellAFusionner.setCellStyle(cellStyle);
                      }
                      // Fusionne les cellules dans la plage spécifiée
                      CellRangeAddress region = new CellRangeAddress(
                          rowIndex, rowIndex, currentColumn, lastColIndex);
                      sheet.addMergedRegion(region); // Ajoute la région fusionnée à la feuille Excel
                  }

                  // Récupère le contenu de la cellule
                  CellContent content = dataCell.getCellContent();
                  // Si le contenu existe, configure le retour à la ligne si nécessaire
                  if(content != null) {
                      cellStyle.setWrapText(content.isComplementOnAnotherLine());
                  }
                  // Applique le contenu à la cellule principale
                  applyCellContent(cell, content);

                  // Incrémente la position de la colonne en tenant compte du colspan
                  currentColumn += dataCell.getColspan();
              } else {
                  // Si la cellule est nulle, crée quand même une cellule vide et incrémente la position
                  row.createCell(currentColumn++);
              }
          }
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
   private void applyCellContent(Cell cell, CellContent content) {
       // Récupération du Workbook pour accéder aux fonctionnalités de formatage
       Workbook wb = cell.getSheet().getWorkbook();
       String contentAsString = content.buildContentValue();

       // Gestion spéciale pour le texte en italique
       if(content.isComplementInItalic()) {
           // Utilisation de RichTextString pour permettre différents styles dans la même cellule
           RichTextString richText = new XSSFRichTextString(contentAsString);
           // Applique la police par défaut épaisseur normale au texte principal
           richText.applyFont(0, content.getText().length(), retrieveFont(EDocumentExcelFont.DEFAULT_NORMAL, wb));
           // Applique l'italique au texte complémentaire
           // Le texte complémentaire commence après le texte principal
           richText.applyFont(content.getText().length(), richText.length(), retrieveFont(EDocumentExcelFont.DEFAULT_ITALIC, wb));
           cell.setCellValue(richText);
       } else {
           // Si pas d'italique nécessaire, applique le texte directement
           cell.setCellValue(contentAsString);
       }
       
       //Gestion de l'éventuel retour à la ligne :
       CellStyle style = cell.getCellStyle();
       style.setWrapText(contentAsString.contains(System.getProperty("line.separator")));
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
    * @param dataCell L'objet métier contenant les informations liées à une cellule contenant une donnée
    * @param cellStyle L'objet POI à configurer pour rendre en excel le style défini dans l'objet métier dataCell
    */
   private void applyStyle(DataCell dataCell, CellStyle cellStyle){
      if(dataCell != null && cellStyle != null){
         //par défaut, on force l'alignement vertical à TOP pour que le début du texte ne soit pas caché 
         //dans le cas où la cellule ne serait pas assez haute 
         cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
         
         // Récupération du type d'alignement défini dans la cellule de données
         AlignmentType alignmentType = dataCell.getAlignmentType();
         // Application du type d'alignement au style de cellule
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
         
         // Vérification si la cellule doit avoir des bordures visibles
         if(dataCell.isWithBorder()){
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
         }
         // Vérifie s'il y a un code couleur hexadécimal pour la bordure gauche
         if(dataCell.getHexaColorCodeForLeftBorder() != null){
            // Définit une bordure épaisse pour la bordure gauche afin d'accentuer cette partie
            cellStyle.setBorderLeft(BorderStyle.THICK);
            ((XSSFCellStyle) cellStyle).setLeftBorderColor(
               // Récupère et applique la couleur correspondante à partir du code hexadécimal fourni
               retrieveXSSFColorFromHex(dataCell.getHexaColorCodeForLeftBorder()));
         }
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
   private XSSFColor retrieveXSSFColorFromHex(String hexCode){
      // Vérification initiale du code hexadécimal pour éviter le traitement de valeurs nulles
      if(hexCode != null){
         // Tentative de récupération de la couleur depuis le cache
         // L'utilisation du cache améliore significativement les performances en évitant
         // de recréer les mêmes couleurs plusieurs fois
         XSSFColor result = colorCacheMap.get(hexCode);

         // Si la couleur n'existe pas encore dans le cache, nous devons la créer
         if(result == null){
            try{
               // Création d'une nouvelle instance de XSSFColor
               // Cette approche est plus coûteuse en ressources, d'où l'importance du cache
               result = new XSSFColor();

               // Conversion du code hexadécimal en format compatible avec XSSFColor
               // Le substring(1) retire le caractère '#' du début du code hexadécimal
               result.setARGBHex(hexCode.substring(1));

               // Stockage de la nouvelle couleur dans le cache pour une utilisation future
               // Optimisation cruciale pour les documents contenant beaucoup de cellules colorées
               colorCacheMap.put(hexCode, result);

            }catch(IllegalArgumentException e){
               // IllegalArgumentException peut être lancée par setARGBHex si le code hexa transmis est incorrect.
               // Dans ce cas, le traitement se poursuit en prenant le noir et on trace l'erreur
               logger.error("Invalid hex color code: {}", hexCode, e);
 
               return BLACK_XSSF_COLOR;
            }
         }
         // Retour de la couleur, qu'elle soit nouvelle ou récupérée du cache
         return result;
      }
      // Retour de la couleur noire, valeur par défaut si le code hexadécimal est null pour ne pas bloquer le traitement
      return BLACK_XSSF_COLOR;
   }

   /** Enum des polices (font) utilisées pour le contenu cellules Excel. 
    * <ul>
    *   <li>DEFAULT_NORMAL : Police standard</li>
    *   <li>DEFAULT_BOLD : Police en gras (mais taille et couleur par défaut) </li>
    *   <li>DEFAULT_ITALIC : Police en italique (mais taille et couleur par défaut)</li>
    * </ul>
    * */
   //NB : dans le futur, des polices avoir une couleur ou une taille particulière pourraient être ajoutées
   private enum EDocumentExcelFont
   {
      DEFAULT_NORMAL,
      DEFAULT_BOLD,
      DEFAULT_ITALIC;
   }

   /**
    * Récupère ou crée une police Excel selon le type spécifié.
    *
    * <p>Cette méthode utilise un cache pour optimiser les performances en évitant
    * de recréer les mêmes polices. Les types de police disponibles sont définies dans {@link EDocumentExcelFont}
    *
    * @param eDocumentExcelFont Le type de police souhaité
    * @param wb Le classeur Excel pour créer la police si nécessaire
    * @return La police configurée selon le type demandé
    */
   private Font retrieveFont(EDocumentExcelFont eDocumentExcelFont, Workbook wb){
      // Vérifie si la police associée à l'excelFontStyle existe déjà dans le cache.
      Font font = fontCacheMap.get(eDocumentExcelFont);

      // Si la police n'est pas encore dans le cache, nous devons en créer une nouvelle.
      if(font == null){
         // Crée une nouvelle instance de Font à partir du classeur fourni.
         font = wb.createFont();
         // Selon le type de police spécifié par excelFontStyle,
         // nous appliquons les attributs appropriés à cette nouvelle instance de Font.
         switch(eDocumentExcelFont){
            case DEFAULT_NORMAL:
               //on ne fait rien
               break;
            case DEFAULT_BOLD:
               font.setBold(true);
               break;
            case DEFAULT_ITALIC:
               font.setItalic(true);
               break;
         }

         // Une fois que nous avons configuré la nouvelle instance de Font,
         // nous l'ajoutons au cache pour que les appels futurs puissent réutiliser
         // cette même instance plutôt que d'en créer une nouvelle.
         fontCacheMap.put(eDocumentExcelFont, font);
      }
      // Retourne la police récupérée ou nouvellement créée pour être utilisée par l'appelant.
      return font;
   }
}
