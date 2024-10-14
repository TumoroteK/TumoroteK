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
package fr.aphp.tumorotek.interfacage.scan;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import fr.aphp.tumorotek.manager.interfacage.scan.ScanTerminaleManager;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;

/**
 * Utilises POI pour convertir le fichier Excel produit par le scanner de boite
 * Thermofischer® VisionMate, en une liste
 */
public class ExcelVMateParser
{

   private final static Log log = LogFactory.getLog(ExcelVMateParser.class);

   private ScanTerminaleManager scanTerminaleManager;

   public void setScanTerminaleManager(final ScanTerminaleManager _s){
      this.scanTerminaleManager = _s;
   }

   public void handleVMateScan(@Body final InputStream body){
      scanTerminaleManager.createObjectManager(processExcelVMate(body), null);
   }

   public ScanTerminale processExcelVMate(final InputStream body){

      final ScanTerminale sT = new ScanTerminale();
      final Map<String, Integer> colsPosition = new HashMap<>();

      String terminaleName = null;
      Date dateScan = null;
      final DateFormat df = new SimpleDateFormat("YYYYMMDD HH:mm:ss");
      try( final HSSFWorkbook workbook = new HSSFWorkbook(body);){
         final HSSFSheet sheet = workbook.getSheetAt(0);
         boolean headersFound = false;
         int colNum;

         int termWidth = 1;
         int termHeight = 0;
         boolean firstLigneDone = false;

         String previousRow = null;

         for(final Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();){
            final HSSFRow row = (HSSFRow) rit.next();
            HSSFCell cell;
            if(!headersFound){ // // headers
               headersFound = true;
               colNum = 0;
               for(final Iterator<Cell> cit = row.cellIterator(); cit.hasNext(); ++colNum){
                  cell = (HSSFCell) cit.next();
                  colsPosition.put(cell.getStringCellValue(), new Integer(colNum));
               }
            }else{

               // terminale name
               if(terminaleName == null){
                  cell = row.getCell(colsPosition.get("RackID").intValue());
                  cell.setCellType(CellType.STRING);
                  terminaleName = cell.getStringCellValue();
               }
               // date scan
               if(dateScan == null){
                  String parsedDate;
                  cell = row.getCell(colsPosition.get("Date").intValue());
                  cell.setCellType(CellType.STRING);
                  parsedDate = cell.getStringCellValue();
                  cell = row.getCell(colsPosition.get("Time").intValue());
                  cell.setCellType(CellType.STRING);
                  dateScan = df.parse(parsedDate + " " + cell.getStringCellValue());
               }

               // scanTube
               final ScanTube tube = new ScanTube();
               cell = row.getCell(colsPosition.get("TubeCode").intValue());
               cell.setCellType(CellType.STRING);
               tube.setCode(cell.getStringCellValue());
               // code || No Tube -> null
               if(tube.getCode().equals("No Tube")){
                  tube.setCode(null);
               }
               // cell
               cell = row.getCell(colsPosition.get("LocationCell").intValue());
               cell.setCellType(CellType.STRING);
               tube.setCellule(cell.getStringCellValue());
               // row
               cell = row.getCell(colsPosition.get("LocationRow").intValue());
               cell.setCellType(CellType.STRING);
               tube.setLigne(cell.getStringCellValue());
               // col
               cell = row.getCell(colsPosition.get("LocationColumn").intValue());
               cell.setCellType(CellType.STRING);
               tube.setColonne(cell.getStringCellValue());

               if(tube.getLigne() != previousRow){
                  termHeight++;
                  if(previousRow != null){ // first line parsed
                     firstLigneDone = true;
                  }
                  previousRow = tube.getLigne();
               }else if(!firstLigneDone){
                  termWidth++;
               }

               tube.setScanTerminale(sT);
               sT.getScanTubes().add(tube);
            }
         }
         sT.setName(terminaleName);
         final Calendar c = Calendar.getInstance();
         c.setTime(dateScan);
         sT.setDateScan(c);
         sT.setHeight(termHeight);
         sT.setWidth(termWidth);

      }catch(final Exception e){
         log.error("Error parsing VisionMate xls", e);
         throw new RuntimeException("Error parsing VisionMate xls", e);
      }
      return sT;
   }
}
