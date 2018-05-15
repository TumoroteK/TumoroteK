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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.stats.im.export.ValueToExport;
import fr.aphp.tumorotek.action.stats.im.model.StatResultsRow;
import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.Subdivision;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Abstract ViewModel pour les onglets du module Indicateurs qui propose la liste
 * des modèles enregistrés pour la plateforme.
 * Date: 12/05/2015
 *
 * @author Julien HUSSON, Marc DESCHAMPS, Mathieu BARTHELEMY
 * @version  2.1
 *
 */
public abstract class AbstractListGridVM
{

   protected List<String> valueList;
   protected Window win;
   protected List<SModele> modelList;
   protected Grid gridView;
   protected Boolean isGridCreated = false;

   private SModele selectedModel;
   private final List<Banque> gridBanques = new ArrayList<>();
   private final List<Indicateur> gridIndicateurs = new ArrayList<>();
   private TKThesaurusObject subdivManager = null;
   private Subdivision gridSubdivision;

   private final Map<Integer, String> subdivMap = new HashMap<>();

   private final List<StatResultsRow> rows = new ArrayList<>();

   private boolean inEdition = false;

   @Init
   public void initData(@ContextParam(ContextType.COMPONENT) final Grid grid){
      this.gridView = grid;
   }

   public void setSelectedModel(final SModele im){
      this.selectedModel = im;
   }

   public SModele getSelectedModel(){
      return selectedModel;
   }

   @Command
   @NotifyChange({"selectedModel", "rows"})
   public void onClickModel(){
      initIndicateursAndBanques();
      initDataGrid(null);
      updateGridUI(win);
   }

   public void updateGridUI(final Component parent){
      if(!isGridCreated){
         // cree si grid si Indicateurs et Banques
         if(!getGridIndicateurs().isEmpty() || !getGridBanques().isEmpty()){
            gridView = (Grid) Executions.createComponents("/zuls/stats/im/gridModel.zul", parent.getFellow("gridzul"), null);
            isGridCreated = true;
         }
      }else{
         parent.removeChild(gridView);
         gridView.detach();
         isGridCreated = false;
         updateGridUI(parent);
      }
   }

   public void initIndicateursAndBanques(){
      getGridBanques().clear();
      getGridIndicateurs().clear();

      getGridIndicateurs().addAll(ManagerLocator.getIndicateurManager().findBySModeleManager(selectedModel));
      getGridBanques().addAll(ManagerLocator.getSModeleManager().getBanquesManager(selectedModel));
      setGridSubdivision(selectedModel.getSubdivision());
      Collections.sort(getGridBanques());
   }

   /**
    * Met à jour tous les objets de la data grid
    * @param dataMap Map (peut être nulle)
    */
   public void initDataGrid(final Map<Indicateur, ArrayList<ValueToExport>> dataMap){

      // valeurs subdivision
      // setSubdivision(ManagerLocator.getSModeleManager().getSubdivisionManager(getSelectedModel()));
      mapSubdivisionListeValeurs();

      // Rows
      populateRowValues(dataMap);
   }

   /**
    * Peuple la liste de rows qui permettront de remplir la dataGrid. Si 
    * la dataMap est null, seules les deux premières colonnes Banque et Subdiv sont remplies.
    * 
    * @param dataMap
    */
   private void populateRowValues(final Map<Indicateur, ArrayList<ValueToExport>> dataMap){

      getRows().clear();

      if(getGridSubdivision() != null){
         boolean firstForBank;
         for(final Banque bank : getGridBanques()){
            firstForBank = true;
            for(final Map.Entry<Integer, String> entry : getSubdivMap().entrySet()){
               final StatResultsRow row = new StatResultsRow(bank, entry.getKey(), entry.getValue(), firstForBank);
               row.setRowspan(getSubdivMap().size());
               getRows().add(row);
               firstForBank = false;
            }
         }
      }else{ // counts tot
         for(final Banque bank : getGridBanques()){
            final StatResultsRow row = new StatResultsRow(bank, 0, null, true);
            row.setRowspan(1);
            getRows().add(row);
         }
      }

      if(dataMap != null){
         for(final Indicateur idc : getGridIndicateurs()){
            Banque b = null;
            Integer bankId;
            final Integer previousBankId = 0;
            for(final ValueToExport o : dataMap.get(idc)){
               bankId = o.getBanqueId();
               // recherche la banque si besoin
               if(!previousBankId.equals(bankId)){
                  b = ManagerLocator.getBanqueManager().findByIdManager(bankId);
               }
               // la row est forcément présente sinon problème dans le code!
               final StatResultsRow row = getRows().get(getRows().indexOf(new StatResultsRow(b, o.getSubDivId(), null, false)));

               row.getValues().add(o.getValue());

               if(o.getValuePourcentage() == null){
                  row.getValuesPourcentage().add(o.getValue());
               }else{
                  row.getValuesPourcentage().add(o.getValuePourcentage());
               }
            }
         }
      }
   }

   public TKThesaurusObject getSubdivManager(){
      return subdivManager;
   }

   public void setSubdivManager(final TKThesaurusObject subdivManager){
      this.subdivManager = subdivManager;
   }

   public List<Indicateur> getGridIndicateurs(){
      return gridIndicateurs;
   }

   public void setGridIndicateurs(final List<Indicateur> ics){
      this.gridIndicateurs.clear();
      this.gridIndicateurs.addAll(ics);
   }

   public void mapSubdivisionListeValeurs(){

      getSubdivMap().clear();

      if(getGridSubdivision() != null){
         TKThesaurusManager<?> manager = null;
         if(getGridSubdivision().getChampEntite().getEntite().getNom().equals("Nature")){
            manager = ManagerLocator.getNatureManager();
         }else if(getGridSubdivision().getChampEntite().getEntite().getNom().equals("PrelevementType")){
            manager = ManagerLocator.getPrelevementTypeManager();
         }else if(getGridSubdivision().getChampEntite().getEntite().getNom().equals("EchantillonType")){
            manager = ManagerLocator.getEchantillonTypeManager();
         }else if(getGridSubdivision().getChampEntite().getEntite().getNom().equals("ProdType")){
            manager = ManagerLocator.getProdTypeManager();
         }

         // si c'est un thes de non conformité
         if(manager != null){
            for(final TKThesaurusObject thObj : manager.findByOrderManager(SessionUtils.getCurrentPlateforme())){
               getSubdivMap().put(thObj.getId(), thObj.getNom());
            }
         }
      }
   }

   public TKThesaurusObject getTKObj(final String typeThesaurus, final Integer id){
      TKThesaurusManager<?> thesManager = null;
      if(typeThesaurus.equals("Nature")){
         thesManager = ManagerLocator.getNatureManager();
      }else if(typeThesaurus.equals("PrelevementType")){
         thesManager = ManagerLocator.getPrelevementTypeManager();
      }else if(typeThesaurus.equals("EchantillonType")){
         thesManager = ManagerLocator.getEchantillonTypeManager();
      }else if(typeThesaurus.equals("EchanQualite")){
         thesManager = ManagerLocator.getEchanQualiteManager();
      }else if(typeThesaurus.equals("ProdType")){
         thesManager = ManagerLocator.getProdTypeManager();
      }else if(typeThesaurus.equals("ProdQualite")){
         thesManager = ManagerLocator.getProdQualiteManager();
      }else if(typeThesaurus.equals("ConditType")){
         thesManager = ManagerLocator.getConditTypeManager();
      }else if(typeThesaurus.equals("ConditMilieu")){
         thesManager = ManagerLocator.getConditMilieuManager();
      }else if(typeThesaurus.equals("ConsentType")){
         thesManager = ManagerLocator.getConsentTypeManager();
      }else if(typeThesaurus.equals("Risque")){
         thesManager = ManagerLocator.getRisqueManager();
      }else if(typeThesaurus.equals("ModePrepa")){
         thesManager = ManagerLocator.getModePrepaManager();
      }else if(typeThesaurus.equals("ModePrepaDerive")){
         thesManager = ManagerLocator.getModePrepaDeriveManager();
      }else if(typeThesaurus.equals("CessionExamen")){
         thesManager = ManagerLocator.getCessionExamenManager();
      }else if(typeThesaurus.equals("DestructionMotif")){
         thesManager = ManagerLocator.getDestructionMotifManager();
      }else if(typeThesaurus.equals("ProtocoleType")){
         thesManager = ManagerLocator.getProtocoleTypeManager();
      }else if(typeThesaurus.equals("ConteneurType")){
         thesManager = ManagerLocator.getConteneurTypeManager();
      }else if(typeThesaurus.equals("EnceinteType")){
         thesManager = ManagerLocator.getEnceinteTypeManager();
      }else if(typeThesaurus.equals("Protocole")){
         thesManager = ManagerLocator.getProtocoleManager();
      }

      if(thesManager != null){
         return thesManager.findByIdManager(id);
      }

      return null;
   }

   public List<Banque> getGridBanques(){
      return gridBanques;
   }

   public void setGridBanques(final List<Banque> bs){
      this.gridBanques.clear();
      this.gridBanques.addAll(bs);
   }

   public List<StatResultsRow> getRows(){
      return rows;
   }

   public Map<Integer, String> getSubdivMap(){
      return subdivMap;
   }

   public Subdivision getGridSubdivision(){
      return gridSubdivision;
   }

   public void setGridSubdivision(final Subdivision s){
      this.gridSubdivision = s;
   }

   public Boolean getIsSubdivised(){
      return getGridSubdivision() != null;
   }

   public boolean isInEdition(){
      return inEdition;
   }

   public void setInEdition(final boolean inEdition){
      this.inEdition = inEdition;
   }

   protected void init(){
      modelList = ManagerLocator.getSModeleManager().findByPlateformeManager(SessionUtils.getCurrentPlateforme());
   }

   public List<SModele> getModelList(){
      return modelList;
   }

   public void setModelList(List<SModele> modelList){
      this.modelList = modelList;
   }
}
