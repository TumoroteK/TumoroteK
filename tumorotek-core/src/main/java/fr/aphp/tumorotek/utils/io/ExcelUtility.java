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
package fr.aphp.tumorotek.utils.io;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;

import fr.aphp.tumorotek.manager.exception.FormulaException;

/**
 * Classe utilitaire, surcouche de POI, pour définir des opérations pouvant être nécessaires 
 * dans plusieurs cas de mise en oeuvre de fichiers excel.
 *
 */
public class ExcelUtility
{
   
   public static final short  POI_DATA_FORMAT__GENERAL_INDEX = 0;//index pour le format standard
   public static final String POI_DATA_FORMAT__DATE_HEURE = "m/d/yy h:mm";
   public static final String POI_DATA_FORMAT__JOUR = "m/d/yy";
   
   
   private ExcelUtility(){
      throw new IllegalStateException("Utility class");
   }

   /**
    * Ajoute une feuille excel au "workbook POI" passé en paramètre avec le nom spécifié. 
    * La méthode POI @{WorkbookUtil.createSafeSheetName} est appliqué sur le nom passé en paramètre
    * afin de gérer les caractères non autorisés (remplacés par " ") 
    * ou un nombre de caractères supérieur à 31 (nom tronqué dans ce cas) 
    * 
    * @param workbook : objet auquel sera ajouté la feuille excel (sheet)
    * @param sheetName : nom de la feuille excel à utiliser. Il sera adapté aux contraintes d'excel
    * @return
    */
   public static Sheet createSheet(Workbook workbook, String sheetName){
      String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
      return workbook.createSheet(safeSheetName);
   }

   /**
    * Ajoute un pied de page à la feuille donnée avec les chaînes spécifiées.
    *
    * @param sheet        La feuille où le pied de page sera ajouté.
    * @param leftString   La chaîne à définir sur le côté gauche du pied de page.
    * @param centerString La chaîne à définir au centre.
    * @param rightString  La chaîne à définir sur le côté droit du pied de page.
    */
   public static void addFooter(Sheet sheet, String leftString, String centerString, String rightString){
      Footer footer = sheet.getFooter();
      if(leftString != null){
         footer.setLeft(leftString);
      }
      if(centerString != null){
         footer.setCenter(centerString);
      }
      if(rightString != null){
         footer.setRight(rightString);
      }
   }
   
   public static CellStyle defineStyle(Workbook workbook, EPoiColor poiColor, boolean bold) {
      CellStyle styleForColor = workbook.createCellStyle();
      Font fontRouge = workbook.createFont();
      if(styleForColor instanceof HSSFCellStyle){
         fontRouge.setColor(poiColor.getColorForHSSFCellStyle().getIndex());
      }else{
         ((XSSFFont) fontRouge).setColor(poiColor.getColorForXSSFStyle());
      }
      fontRouge.setBold(bold);
      styleForColor.setFont(fontRouge);
      
      return styleForColor;
   }

   public static boolean isEmpty(Cell cell) {
      return cell == null || cell.getCellTypeEnum() == CellType.BLANK;
   }
   
   //par défaut, le format date n'est pas accepté (car c'est généralement utilisé pour lire des clés fonctionnelles)
   public static final String readAlphanumContent(Cell cell) throws ExcelIllegalContentTypeException {
      return readAlphanumContent(cell, null, false);
   }

   //TODO : idéalement message à internationaliser : mais un peu compliqué car il faudrait avoir la locale à ce niveau car les textes ci-dessous sont passés en paramètre d'un autre message internationalisé lui
   public static final String readAlphanumContent(Cell cell, FormulaEvaluator formulaEvaluator, boolean acceptDate) throws ExcelIllegalContentTypeException {
      String result = null;
      if(!isEmpty(cell)) {
         //type de la donnée dans la cellule ou de la valeur provenant de l'évaluation
         CellType type = cell.getCellTypeEnum();
         CellValue formuleEvaluee = evaluateFormulaIfNecessary(cell, formulaEvaluator);
         
         if(formuleEvaluee != null) {
            type = formuleEvaluee.getCellTypeEnum();
         }
         
         final String formatAttendu = "Chaîne de caractères";//il faudrait internationalisé mais un peu compliqué car passé en paramètre d'un autre message internationalisé lui...
         
         //gestion des 2 types attendus : STRING et NUMERIC
         switch(type){
            case STRING:
               result = formuleEvaluee == null ? cell.getStringCellValue().trim() : formuleEvaluee.getStringValue().trim();
               break;
   
            case NUMERIC:
               //dans cette méthode, on n'accepte pas les dates 
               if(DateUtil.isCellDateFormatted(cell)) {
                  if(acceptDate) {
                     Date date = formuleEvaluee == null ? cell.getDateCellValue() : new Date((long)formuleEvaluee.getNumberValue());
                     Calendar calendar = Calendar.getInstance();
                     calendar.setTime(date);
                     if(calendar.get(Calendar.HOUR_OF_DAY) > 0 || calendar.get(Calendar.MINUTE) > 0){
                        final DateFormat dateFormatAvecHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
                        result = dateFormatAvecHeure.format(date);
                     }else{
                        final DateFormat dateFormatSansHeure = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                        result = dateFormatSansHeure.format(date);
                     }
                  }
                  else {
                     throw new ExcelIllegalContentTypeException(formatAttendu);
                  }
               }
               else {
                  //pour qu'un code ne contenant que des chiffres soit bien écrit (sans "exponentiel"), comparaison des valeurs double et long
                  final double valueAsDouble = formuleEvaluee == null ? cell.getNumericCellValue() : formuleEvaluee.getNumberValue();
                  final long valueAsLong = (long) valueAsDouble;
   
                  if(valueAsLong == valueAsDouble){
                     result = String.valueOf(valueAsLong);
                  }else{
                     result = String.valueOf(valueAsDouble);
                  }
               }
               break;            
               
            default:
               throw new ExcelIllegalContentTypeException(formatAttendu);
         }
      }
      
      return result;
   }
   
   private static CellValue evaluateFormulaIfNecessary(Cell cell, FormulaEvaluator formulaEvaluator) {
      CellValue formuleEvaluee = null;
      
      //gestion d'une éventuelle formule : évaluation de la valeur puis exploitation comme si il ne s'agissait pas
      //d'une formule en s'appuyant sur le type associée à l'évaluation
      if(cell != null && cell.getCellTypeEnum() == CellType.FORMULA) {
         if (formulaEvaluator == null) {
            throw new FormulaException(cell.getCellFormula().toString());
         }
         formuleEvaluee = formulaEvaluator.evaluate(cell);
      }
      
      return formuleEvaluee;
   }
   
   public static final Date readDateContent(Cell cell, FormulaEvaluator formulaEvaluator) throws ExcelIllegalContentTypeException {

      final String formatAttendu = "Date";//il faudrait internationalisé mais un peu compliqué car passé en paramètre d'un autre message internationalisé lui...
      
      if(!isEmpty(cell)) {
         CellType type = cell.getCellTypeEnum();
         CellValue formuleEvaluee = evaluateFormulaIfNecessary(cell, formulaEvaluator);
         if(formuleEvaluee != null) {
            type = formuleEvaluee.getCellTypeEnum();
         }
         
         if(type == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return formuleEvaluee == null ? cell.getDateCellValue() : new Date((long)formuleEvaluee.getNumberValue());
         }
         else {
            throw new ExcelIllegalContentTypeException(formatAttendu);
         }
      }
      
      return null;
   }

   public static final Number readNumericContent(Cell cell, FormulaEvaluator formulaEvaluator) throws ExcelIllegalContentTypeException {

      final String formatAttendu = "Nombre";//il faudrait internationalisé mais un peu compliqué car passé en paramètre d'un autre message internationalisé lui...
      
      if(!isEmpty(cell)) {
         CellType type = cell.getCellTypeEnum();
         CellValue formuleEvaluee = evaluateFormulaIfNecessary(cell, formulaEvaluator);
         if(formuleEvaluee != null) {
            type = formuleEvaluee.getCellTypeEnum();
         }
         
         if(type == CellType.NUMERIC && !DateUtil.isCellDateFormatted(cell)) {
            return formuleEvaluee == null ? cell.getNumericCellValue() : formuleEvaluee.getNumberValue();
         }
         else {
            throw new ExcelIllegalContentTypeException(formatAttendu);
         }
      }
      
      return null;
   }   
   
   public static final Boolean readBooleanContent(Cell cell, FormulaEvaluator formulaEvaluator) throws ExcelIllegalContentTypeException {

      if(!isEmpty(cell)) {
         CellType type = cell.getCellTypeEnum();
         CellValue formuleEvaluee = evaluateFormulaIfNecessary(cell, formulaEvaluator);
         if(formuleEvaluee != null) {
            type = formuleEvaluee.getCellTypeEnum();
         }
         
         final String formatAttendu = "0 pour NON ou 1 pour OUI";//il faudrait internationalisé mais un peu compliqué car passé en paramètre d'un autre message internationalisé lui...
         
         switch(type){
            case BOOLEAN:
               return formuleEvaluee == null ? cell.getBooleanCellValue() : formuleEvaluee.getBooleanValue();
            
            case NUMERIC:
               long valueAsLong = (long)(formuleEvaluee == null ? cell.getNumericCellValue() : formuleEvaluee.getNumberValue());
               if(valueAsLong == 0) {
                  return false;
               }
               else if(valueAsLong == 1) {
                  return true;
               }
               else {
                  throw new ExcelIllegalContentTypeException(formatAttendu);
               }
            
            //NB : on gère également le OUI / NON case insensitive...
            case STRING:
               String valueAsString = (formuleEvaluee == null ? cell.getStringCellValue() : formuleEvaluee.getStringValue());
               if("NON".equalsIgnoreCase(valueAsString)) {
                  return false;
               }
               else if("OUI".equalsIgnoreCase(valueAsString)) {
                  return true;
               }
               else {
                  throw new ExcelIllegalContentTypeException(formatAttendu);
               }
              
            default:
               throw new ExcelIllegalContentTypeException(formatAttendu);
         }
      }
      
      return null;
   }
   
}

