/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2016)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Column;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.Subdivision;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Julien Husson
 * @author Marc Deschamps
 * @author Mathieu Barthelemy
 * @version 2.2.0
 * @since 2.1.0
 *
 */
public class CreateModel extends AbstractListGridVM
{

   private Window win;
   private List<Subdivision> subdivisions;
   private Subdivision selectedSubdivision;

   // list existing model
   //   private List<SModele> modelList;
   private SModele selectedExistingModel;

   private ListModelList<Indicateur> indicateursModel;
   private List<Indicateur> selectedIndicateurs = new ArrayList<>();

   private ListModelList<Banque> banquesModel;
   private List<Banque> selectedBanques = new ArrayList<>();

   @Wire("#msgPopup")
   Popup popup;
   @Wire("#msg")
   Label msg;

   @Init
   public void initData(@ContextParam(ContextType.COMPONENT) final Window win){
      this.win = win;

      modelList = ManagerLocator.getSModeleManager().findByPlateformeManager(SessionUtils.getCurrentPlateforme());
      modelList.add(0, null);

      indicateursModel =
         new ListModelList<>(ManagerLocator.getIndicateurManager().findNullSubdivisionIndicateursManager());

      banquesModel = new ListModelList<>(
         ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(SessionUtils.getCurrentPlateforme(), null));

      banquesModel.setMultiple(true);

      subdivisions = ManagerLocator.getSubdivisionManager().findAllObjectsManager();
      subdivisions.add(0, null);
   }

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   /********************* Commands *****************************************/
   @Command
   @NotifyChange({"selectedExistingModel", "indicateursModel", "inEdition"})
   public void onChangeSelectedExistingModel(){
      clearSelections();

      // switch consult mode
      setInEdition(false);

      if(selectedExistingModel != null){
         selectedBanques.addAll(ManagerLocator.getSModeleManager().getBanquesManager(selectedExistingModel));

         banquesModel.setSelection(selectedBanques);

         selectedSubdivision = selectedExistingModel.getSubdivision();
         resetIndicateurs();

         selectedIndicateurs.addAll(ManagerLocator.getIndicateurManager().findBySModeleManager(selectedExistingModel));
         indicateursModel.setSelection(selectedIndicateurs);
      }else{
         resetIndicateurs();
      }
      // Collections.sort(selectedIndicateurs);
      Collections.sort(selectedBanques);

      updatePreviewGrid(true);
   }

   @Command
   @NotifyChange({"selectedExistingModel", "modelList", "inEdition"})
   public void onDeleteModel(){
      setInEdition(false);
      ManagerLocator.getSModeleManager().removeObjectManager(selectedExistingModel);
      modelList.remove(selectedExistingModel);

      selectedExistingModel = null;
      clearSelections();
      resetIndicateurs();

      updatePreviewGrid(false);
   }

   @Command
   @NotifyChange({"selectedExistingModel", "inEdition"})
   public void onNewModel(){
      setInEdition(true);

      selectedExistingModel = new SModele();

      clearSelections();
      resetIndicateurs();

      updatePreviewGrid(false);
   }

   @Command
   @NotifyChange({"inEdition"})
   public void onEditModel(){
      setInEdition(true);
   }

   @Command
   @NotifyChange({"selectedExistingModel", "inEdition"})
   public void onCancelModel(){

      if(selectedExistingModel == null){

      }else{
         setInEdition(false);
      }
   }

   @Command
   @NotifyChange({"selectedExistingModel", "modelList", "inEdition"})
   public void onSaveModel(){

      selectedIndicateurs.clear();
      selectedIndicateurs.addAll(getGridIndicateurs());
      selectedBanques.clear();
      selectedBanques.addAll(banquesModel.getSelection());

      if(selectedIndicateurs.isEmpty()){
         Clients.showNotification(Labels.getLabel("stats.smodel.warn.noindicateurs"), "warning", win, "middle_center", 2000);
      }else if(selectedBanques.isEmpty()){
         Clients.showNotification(Labels.getLabel("stats.smodel.warn.nobanques"), "warning", win, "middle_center", 2000);
      }else{
         selectedExistingModel.setSubdivision(selectedSubdivision);
         // update
         if(selectedExistingModel.getSmodeleId() != null){

            selectedExistingModel = ManagerLocator.getSModeleManager().updateObjectManager(selectedExistingModel,
               selectedExistingModel.getPlateforme(), getGridIndicateurs(), selectedBanques);

            int pos = modelList.indexOf(selectedExistingModel);
            modelList.remove(selectedExistingModel);
            modelList.add(pos, selectedExistingModel);

            //				Clients.showNotification(selectedExistingModel.getNom()
            //						+ "Modèle modifié avec succés !!", "info", win,
            //						"center", 2000);
         }else{ // create
            ManagerLocator.getSModeleManager().createObjectManager(selectedExistingModel, SessionUtils.getCurrentPlateforme(),
               getGridIndicateurs(), selectedBanques);
            // selectedModele = ManagerLocator.getSModeleManager()
            //		.findByIdManager(selectedModele.getSmodeleId());

            //      modelList.add(selectedExistingModel);
            Collections.sort(modelList);

            //				Clients.showNotification(selectedExistingModel.getNom()
            //						+ "Modèle crée avec succés !!", "info", win,
            //						"middle_center", 2000);
         }
         setInEdition(false);
         //	updatePreviewGrid();
      }
      init();
   }

   /****************************** Grid handling ***********************/

   @Command("changeGridIndicateurOrder")
   @GlobalCommand("changeGridIndicateurOrder")
   public void changeGridIndicateurOrder(@BindingParam("dropEvt") final DropEvent e,
      @BindingParam("indic") final Indicateur indic){
      final Column dragged = (Column) e.getDragged();
      final Column target = (Column) e.getTarget();
      if(!gridView.getColumns().insertBefore(dragged, target)){
         gridView.getColumns().insertBefore(target, dragged);
      }
      final List<Component> child = gridView.getColumns().getChildren();
      // rectificatif pos col Banque 
      final int pos = child.indexOf(dragged) - 2;

      getGridIndicateurs().remove(indic);
      if(pos < getGridIndicateurs().size()){
         getGridIndicateurs().add(pos, indic);
      }else{ // last pos
         getGridIndicateurs().add(indic);
      }

   }

   private void updatePreviewGrid(final Boolean orderedIndics){
      if(orderedIndics != null){
         getGridIndicateurs().clear();
         if(orderedIndics){
            getGridIndicateurs().addAll(selectedIndicateurs);
         }else{
            getGridIndicateurs().addAll(indicateursModel.getSelection());
         }
      }
      getGridBanques().clear();
      getGridBanques().addAll(banquesModel.getSelection());
      setGridSubdivision(selectedSubdivision);
      initDataGrid(null);
      updateGridUI(win);
   }

   @Command
   @NotifyChange("selectedIndicateurs")
   public void onSelectIndicateur(@BindingParam("item") final Indicateur idc){
      // getGridIndicateurs().clear();
      // getGridIndicateurs().addAll(indicateursModel.getSelection());
      if(!getGridIndicateurs().contains(idc)){
         getGridIndicateurs().add(idc);
      }else{
         getGridIndicateurs().remove(idc);
      }

      updatePreviewGrid(null);
   }

   @Command
   @NotifyChange("selectedIndicateurs")
   public void onSelectAllIndicateurs(){
      // getGridIndicateurs().clear();
      // getGridIndicateurs().addAll(indicateursModel.getSelection());
      if(!getGridIndicateurs().containsAll(indicateursModel)){
         for(final Indicateur indic : indicateursModel){
            if(!getGridIndicateurs().contains(indic)){
               getGridIndicateurs().add(indic);
            }
         }
      }else{
         getGridIndicateurs().clear();
      }

      updatePreviewGrid(null);
   }

   @Command
   public void onSelectBanque(){
      getGridBanques().clear();
      getGridBanques().addAll(banquesModel.getSelection());

      updatePreviewGrid(null);
   }

   @Command
   @NotifyChange({"indicateursModel"})
   public void onChangeSubdivision(@BindingParam("item") final Subdivision subd){
      selectedIndicateurs.clear();

      resetIndicateurs();

      updatePreviewGrid(false);
   }

   private void resetIndicateurs(){
      indicateursModel.clear();
      if(selectedSubdivision != null){
         indicateursModel.addAll(ManagerLocator.getIndicateurManager().findBySubdivisionManager(selectedSubdivision));
      }else{
         indicateursModel.addAll(ManagerLocator.getIndicateurManager().findNullSubdivisionIndicateursManager());
      }
   }

   private void clearSelections(){
      banquesModel.clearSelection();
      indicateursModel.clearSelection();
      selectedBanques.clear();
      selectedIndicateurs.clear();
      selectedSubdivision = null;
   }

   @Command
   public void OnMouseOverIndicateurList(@BindingParam("target") final Component target,
      @BindingParam("item") final Indicateur idc){
      msg.setValue(idc.getDescription());
      popup.open(target, "after_end");
   }

   @Command
   public void onMouseOutIndicateurList(){
      popup.close();
   }

   //	private void showNotify(String msg, Component ref) {
   //		Clients.showNotification(msg, "info", ref, "center", 2000);
   //	}

   public List<Subdivision> getSubdivisions(){
      return subdivisions;
   }

   public ListModel<Indicateur> getIndicateursModel(){
      return indicateursModel;
   }

   public ListModel<Banque> getBanquesModel(){
      return banquesModel;
   }

   public List<Banque> getSelectedBanques(){
      return selectedBanques;
   }

   public void setSelectedBanques(final List<Banque> s){
      this.selectedBanques = s;
   }

   public List<Indicateur> getSelectedIndicateurs(){
      return selectedIndicateurs;
   }

   public void setSelectedIndicateurs(final List<Indicateur> selectedIndicateurs){
      this.selectedIndicateurs = selectedIndicateurs;
   }

   //   public List<SModele> getExistingSModels(){
   //      return modelList;
   //   }

   public Subdivision getSelectedSubdivision(){
      return selectedSubdivision;
   }

   public void setSelectedSubdivision(final Subdivision selectedSubdivision){
      this.selectedSubdivision = selectedSubdivision;
   }

   //   public void setExistingSModels(ListModelList<SModele> existingSModel){
   //      this.modelList = existingSModel;
   //   }

   public SModele getSelectedExistingModel(){
      return selectedExistingModel;
   }

   public void setSelectedExistingModel(final SModele selectedExistingModel){
      this.selectedExistingModel = selectedExistingModel;
   }
}
