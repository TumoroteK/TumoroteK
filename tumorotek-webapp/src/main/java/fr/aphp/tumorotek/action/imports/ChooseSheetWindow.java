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
package fr.aphp.tumorotek.action.imports;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Window;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10.6
 *
 */
public class ChooseSheetWindow
{

   @Wire("#fwinChooseSheetWindow")
   private Window fwinChooseSheetWindow;

   @Wire("#buttonsBox")
   private Hlayout buttonsLayout;

   private Component parent;

   private final List<TabFileSheet> sheets = new ArrayList<>();

   private TabFileSheet selectedSheet = null;

   private Boolean embed = false;

   private String border = "normal";

   private String winTitle = Labels.getLabel("import.choose.sheet.title");

   private String mainLabel = Labels.getLabel("import.choose.sheet");

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   @Init
   public void init(@ExecutionArgParam("selSheets") final List<TabFileSheet> selTbs,
      @ExecutionArgParam("sheets") final List<Sheet> shs, @ExecutionArgParam("parent") final Component p,
      @ExecutionArgParam("embedded") final Boolean emb){

      parent = p;
      if(emb != null){
         embed = emb;
      }

      // transformation list sheets from workbook
      if(shs != null){
         TabFileSheet tfs;
         for(final Sheet sheet : shs){
            tfs = new TabFileSheet(sheet.getSheetName(), sheet.getPhysicalNumberOfRows() - 1, false);
            sheets.add(tfs);
         }
         updateSelSheets(selTbs);
      }

      if(emb != null && emb){
         doEmbedded();
      }
   }

   @GlobalCommand
   @NotifyChange({"sheets", "selectedSheet"})
   public void update(@BindingParam("selSheets") final List<TabFileSheet> selTbs){
      updateSelSheets(selTbs);
   }

   private void updateSelSheets(final List<TabFileSheet> selTbs){
      // disable
      if(selTbs != null){
         for(final TabFileSheet tf : selTbs){
            if(sheets.contains(tf)){
               sheets.get(sheets.indexOf(tf)).setDisabled(true);
            }
         }
      }

      if(!sheets.isEmpty()){
         for(final TabFileSheet sheet : sheets){
            if(!sheet.getDisabled()){
               setSelectedSheet(sheet);
               break;
            }
         }
      }
   }

   @Command
   public void importSheet(){

      if(selectedSheet != null){
         Clients.showBusy(Labels.getLabel("importTemplate.wait.import"));
         if(!embed){
            Events.echoEvent("onClose", fwinChooseSheetWindow, null);
         }
         Events.echoEvent("onLaterImport", parent, getSelectedSheet().getName());
      }
   }

   @Command
   public void close(){
      if(embed){
         Events.postEvent("onCloseFromChooseSheet", parent, null);
      }else{
         Events.postEvent("onClose", fwinChooseSheetWindow, null);
      }
   }

   /**********************************************************/
   /****************** GETTERS - SETTERS *********************/
   /**********************************************************/
   public List<TabFileSheet> getSheets(){
      return sheets;
   }

   public TabFileSheet getSelectedSheet(){
      return selectedSheet;
   }

   public void setSelectedSheet(final TabFileSheet ts){
      selectedSheet = ts;
   }

   public void doEmbedded(){
      winTitle = "";
      border = "none";
      mainLabel = Labels.getLabel("import.choose.sheet.title.embed");
   }

   public String getBorder(){
      return border;
   }

   public String getWinTitle(){
      return winTitle;
   }

   public String getMainLabel(){
      return mainLabel;
   }
}
