package fr.aphp.tumorotek.utils.io;

import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

/**
 * Classe utilitaire, surcouche de POI, pour définir des opérations pouvant être nécessaires 
 * dans plusieurs cas de mise en oeuvre de fichiers excel.
 *
 */
public class ExcelUtility
{
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

}

