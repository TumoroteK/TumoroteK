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
package fr.aphp.tumorotek.action.stats.im.viewmodel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.stats.im.ExecuteModele;
import fr.aphp.tumorotek.action.stats.im.export.ValueToExport;
import fr.aphp.tumorotek.action.stats.im.model.StatResultsRow;
import fr.aphp.tumorotek.model.stats.Indicateur;

/**
 * ViewModel pour onglet de génération de rapports du module Indicateurs.
 * Contient la liste des modèles et la grid 'Aperçu' pour afficher les résultats.
 * Date: 12/05/2015
 *
 * @author Julien HUSSON, Marc DESCHAMPS, Mathieu BARTHELEMY
 * @version  2.1
 *
 */
public class EditModel extends AbstractListGridVM
{

   protected static Log log = LogFactory.getLog(EditModel.class);

   private Date dateDebut;
   private Date dateFin;
   private ExecuteModele executeModel;

   boolean percentDisplay = false;

   private String switchPercentClass = "appLink percent";

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireEventListeners(view, this);
   }

   @Init
   public void initData(@ContextParam(ContextType.COMPONENT) final Window win){
      this.win = win;
      super.init();
   }

   @Command
   @NotifyChange({"onUpdateModel", "rows", "modelList"})
   public void onUpdateModel(@ContextParam(ContextType.VIEW) Component view){
      init();
   }

   private final Map<Indicateur, ArrayList<ValueToExport>> dataMap = new HashMap<>();

   @Command
   @NotifyChange("selectedModel")
   public void generateReport(@BindingParam("date1") java.util.Date date1, @BindingParam("date2") java.util.Date date2){

      dataMap.clear();
      if(date1 == null){
         date1 = new Date(0);
      }
      if(date2 == null){
         date2 = new Date();
      }

      if(getSelectedModel() != null){
         executeModel = new ExecuteModele(getSelectedModel(), date1, date2, dataMap);

         Events.echoEvent("onLaterGenerate", win, null);
         Clients.showBusy(Labels.getLabel("stats.report.busy"));
      }
      // else {		
      //	// REMPLACER par bouton inactivé
      //	Messagebox.show("Sélectionnez un modèle !", "Error", Messagebox.OK,
      //															Messagebox.ERROR);
      // }
   }

   public Boolean getPercentDisplay(){
      return percentDisplay;
   }

   public void setPercentDisplay(final Boolean _p){
      this.percentDisplay = _p;
   }

   @Command
   @NotifyChange({"percentDisplay", "switchPercentClass"})
   public void switchDisplay(){

      percentDisplay = !percentDisplay;

      if(!percentDisplay){
         switchPercentClass = "appLink percent";
      }else{
         switchPercentClass = "appLink percentSwitch";
      }

      Clients.showBusy(Labels.getLabel("stats.report.busy"));
      Events.echoEvent("updateGridUI(win);", win, null);
      Clients.clearBusy();
   }

   @Listen("onLaterGenerate=#win")
   public void onLaterGenerate(){

      initIndicateursAndBanques();
      try{
         executeModel.start();
      }catch(final Exception e){
         org.zkoss.zul.Messagebox.show(e.getMessage(), "Error", org.zkoss.zul.Messagebox.OK, org.zkoss.zul.Messagebox.ERROR);
      }

      initDataGrid(dataMap);
      updateGridUI(win);

      Clients.clearBusy();
   }

   public Validator getDateValidator(){
      return new AbstractValidator()
      {
         @Override
         public void validate(final ValidationContext ctx){
            final Date dateDebut = (Date) ctx.getProperty().getValue();// the main
            // property
            final Date dateFin = (Date) ctx.getProperties("dateFin")[0].getValue();

            if(!isValidDate(dateDebut, dateFin)){
               addInvalidMessage(ctx, "dates", Labels.getLabel("stats.report.dates.period.illegal"));
            }
         }

         public boolean isValidDate(Date d1, Date d2){
            if(d1 == null){
               d1 = new Date(0);
            }
            ;
            if(d2 == null){
               d2 = new Date();
            }

            final Calendar cal1 = Calendar.getInstance();
            final Calendar cal2 = Calendar.getInstance();

            cal1.setTime(d1);
            cal2.setTime(d2);

            return cal2.getTimeInMillis() - cal1.getTimeInMillis() >= 0;
         }

      };
   }

   public Date getDateDebut(){
      return dateDebut;
   }

   public void setDateDebut(final Date dateDebut){
      this.dateDebut = dateDebut;
   }

   public Date getDateFin(){
      return dateFin;
   }

   public void setDateFin(final Date dateFin){
      this.dateFin = dateFin;
   }

   public String getSwitchPercentClass(){
      return switchPercentClass;
   }

   public void setSwitchPercentClass(final String _s){
      this.switchPercentClass = _s;
   }

   @Command
   public void downloadDataAsExcel(){

      // créé et remplit le workbook
      int currentRow = 0;
      int currColl = 0;
      final HSSFWorkbook wb = new HSSFWorkbook();
      final HSSFSheet sheet = wb.createSheet("data");

      final HSSFCellStyle intTotStyle = wb.createCellStyle();
      final HSSFDataFormat format = wb.createDataFormat();
      intTotStyle.setDataFormat(format.getFormat("00"));

      final HSSFCellStyle nbStyle = wb.createCellStyle();
      nbStyle.setDataFormat(format.getFormat("00"));

      final List<CellRangeAddress> spans = new ArrayList<>();

      HSSFRow row;
      Cell cell;

      // header
      row = sheet.createRow(currentRow);
      currentRow++;
      cell = row.createCell(currColl);
      cell.setCellType(Cell.CELL_TYPE_STRING);
      cell.setCellValue(Labels.getLabel("Entite.Banque"));
      currColl++;
      if(getIsSubdivised()){
         cell = row.createCell(currColl);
         cell.setCellType(Cell.CELL_TYPE_STRING);
         cell.setCellValue(Labels.getLabel("Champ.SModele.Subdivision"));
         currColl++;
      }
      for(final Indicateur indic : getGridIndicateurs()){
         cell = row.createCell(currColl);
         cell.setCellType(Cell.CELL_TYPE_STRING);
         cell.setCellValue(Labels.getLabel("Indicateur." + indic.getNom()));
         currColl++;
      }

      // data
      int i = 0;
      for(final StatResultsRow data : getRows()){
         row = sheet.createRow(currentRow);
         currentRow++;
         currColl = 0;
         // banque
         cell = row.createCell(currColl);
         cell.setCellType(Cell.CELL_TYPE_STRING);
         cell.setCellValue(data.getBanque().getNom());
         currColl++;
         // subdivision
         if(getIsSubdivised()){
            cell = row.createCell(currColl);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(data.getSubDivNom());
            currColl++;
         }
         // datum
         if(!percentDisplay){
            for(final Number n : data.getValues()){
               cell = row.createCell(currColl);
               cell.setCellType(Cell.CELL_TYPE_NUMERIC);
               cell.setCellValue(n.doubleValue());
               cell.setCellStyle(intTotStyle);
               currColl++;
            }
         }else{ // percents
            for(final Number n : data.getValuesPourcentage()){
               cell = row.createCell(currColl);
               cell.setCellType(Cell.CELL_TYPE_NUMERIC);
               cell.setCellValue(n.doubleValue());
               cell.setCellStyle(nbStyle);
               currColl++;
            }
         }

         // spans
         if(data.getFirstSubdivForBanque() && data.getRowspan() > 1){
            final CellRangeAddress spanAddr = new CellRangeAddress(i + 1, i + data.getRowspan(), 0, 0);
            spans.add(spanAddr);
         }

         // last row
         i++;
      }

      // apply rowspans
      for(final CellRangeAddress span : spans){
         sheet.addMergedRegion(span);
      }

      // downloadm2
      final ByteArrayOutputStream outStr = new ByteArrayOutputStream();
      try{
         wb.write(outStr);
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyysyMMddHHmm").format(cal.getTime());
         final AMedia media =
            new AMedia(getSelectedModel().getNom() + "_" + date, "xls", "application/vnd.ms-excel", outStr.toByteArray());
         Filedownload.save(media);
      }catch(final IOException e){
         e.printStackTrace();
      }
   }
}
