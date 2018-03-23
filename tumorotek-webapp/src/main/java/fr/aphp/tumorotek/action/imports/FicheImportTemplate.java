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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.EntiteDecorator;
import fr.aphp.tumorotek.decorator.I3listBoxItemRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.ErrorsInImportException;
import fr.aphp.tumorotek.manager.io.imports.ImportError;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheImportTemplate extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(FicheImportTemplate.class);

   private static final long serialVersionUID = 3330210719922132352L;

   // Labels components
   private Label nomLabel;
   private Label descriptionLabel;
   private Grid colonnesGrid;
   private Group groupHistorique;
   private Grid historiquesGrid;
   private Button exporterHeader;

   // Editable components
   private Label nomRequired;
   private Textbox nomBox;
   private Textbox descriptionBox;
   private Grid colonnesGridEdit;
   private Row helpAddColumnRow;
   private Row rowAddChamp;
   private Listbox champsBox;
   private Listbox entitesBox;
   private Button importer;

   private Checkbox updateTemplateCheckbox;

   // subderive
   private Button addNewSubderiveC;
   private Listbox subderiveParentListbox;
   private Label subderiveParentLabel;

   private Div entitesAssocieesImport;
   private Group groupEntites;

   // Objets Principaux.
   private ImportTemplate importTemplate;
   private Set<EntiteDecorator> entites = new HashSet<>();
   private EntiteDecorator selectedEntite;
   private List<EntiteDecorator> entitesAssociees = new ArrayList<>();
   private List<ImportColonne> importColonnes = new ArrayList<>();
   private List<ImportColonne> importColonnesToRemove = new ArrayList<>();
   private List<ImportColonneDecorator> importColonnesDecorator = new ArrayList<>();
   private ImportColonneRowRenderer colonnesRenderer = new ImportColonneRowRenderer();
   private List<ImportChampDecorator> champs = new ArrayList<>();
   private ImportChampDecorator selectedChamp;
   private List<ImportHistorique> historiques = new ArrayList<>();
   private Integer nbHistorique;
   private ImportHistoriqueRowRenderer historiquesRenderer = new ImportHistoriqueRowRenderer();
   //	private BufferedInputStream fileInputStream;
   private Workbook uploadedWb;
   //	private InputStream fileInputStreamCorrectif;

   private Event currentEvent;

   // isSubDerive pour appliquer un mode éditable 
   // restreint aux imports subderive (colonnes entêtes parent dérivé, 
   // liste entité réduite à dérivé)
   private Boolean isSubderive = false;

   private static I3listBoxItemRenderer entiteRenderer = new I3listBoxItemRenderer("nom");

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.importTemplate");
      setCascadable(false);
      setFantomable(false);
      setDeletable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.nomLabel, this.descriptionLabel, this.colonnesGrid, this.groupHistorique,
         this.historiquesGrid, this.importer, this.exporterHeader, this.subderiveParentLabel});

      setObjBoxsComponents(new Component[] {this.nomBox, this.descriptionBox, this.colonnesGridEdit, this.helpAddColumnRow,
         this.rowAddChamp, this.updateTemplateCheckbox, this.subderiveParentListbox});

      setRequiredMarks(new Component[] {this.nomRequired});

      drawActionsForImport();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      importer.setVisible(false);
      exporterHeader.setVisible(false);

      Executions.createComponents("/zuls/imports/EntitesAssocieesImport.zul", entitesAssocieesImport, null);
      getEntitesAssocieesImport().setGroupHeader(groupEntites);
      getEntitesAssocieesImport().setPathToRespond(Path.getPath(self));

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.importTemplate = (ImportTemplate) obj;

      // entites.clear();
      nbHistorique = 0;
      historiques.clear();

      if(this.importTemplate != null && this.importTemplate.getImportTemplateId() != null){
         entites.clear();
         entites
            .addAll(EntiteDecorator.decorateListe(ManagerLocator.getImportTemplateManager().getEntiteManager(importTemplate)));

         importColonnes.clear();
         // subderive
         isSubderive = importTemplate.getDeriveParentEntite() != null;
         if(isSubderive){
            importColonnes.addAll(makeSubderiveHeaderCols(importTemplate));
         }

         importColonnes.addAll(ManagerLocator.getImportColonneManager().findByImportTemplateManager(importTemplate));
         nbHistorique = ManagerLocator.getImportHistoriqueManager().findByTemplateWithOrderManager(importTemplate).size();
      }

      entitesAssociees.clear();
      final Iterator<EntiteDecorator> it = entites.iterator();
      while(it.hasNext()){
         entitesAssociees.add(it.next());
      }
      getEntitesAssocieesImport().setObjects(entitesAssociees);

      super.setObject(importTemplate);
   }

   @Override
   public void cloneObject(){
      setClone(this.importTemplate.clone());
   }

   @Override
   public ImportTemplate getObject(){
      return this.importTemplate;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public ImportController getObjectTabController(){
      return (ImportController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      final ImportTemplate it = new ImportTemplate();
      it.setBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      importColonnes.clear();
      importColonnesDecorator.clear();
      // entitesAssociees.clear();
      entites.clear();

      if(isSubderive){
         final Entite deriveE = ManagerLocator.getEntiteManager().findByIdManager(8);
         entites.add(new EntiteDecorator(deriveE));
         it.setDeriveParentEntite(ManagerLocator.getEntiteManager().findByIdManager(8));

         importColonnes.addAll(makeSubderiveHeaderCols(it));
         // ajout des colonnes obligatoires derives
         // on récupère les champs obligatoires que l'on va ajouter
         final List<ChampEntite> ces =
            ManagerLocator.getChampEntiteManager().findByEntiteImportAndIsNullableManager(deriveE, true, false);

         for(int i = ces.size() - 1; i > -1; i--){
            final ImportColonne ic = new ImportColonne();
            ic.setImportTemplate(it);
            ic.setChamp(new Champ(ces.get(i)));
            ic.setImportTemplate(it);
            importColonnes.add(ic);
         }

      }

      setObject(it);

      super.setNewObject();
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.importTemplate.equals(new ImportTemplate()));

      if(this.importTemplate != null && this.importTemplate.getIsEditable() != null && !this.importTemplate.getIsEditable()){
         editC.setVisible(false);
         deleteC.setVisible(false);
      }

      if(this.importTemplate != null && this.importTemplate.getImportTemplateId() == null){
         importer.setVisible(false);
         exporterHeader.setVisible(false);
      }

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("importHistorique.title"));
      sb.append(" (");
      sb.append(nbHistorique);
      sb.append(")");
      groupHistorique.setLabel(sb.toString());
      groupHistorique.setOpen(false);

      getEntitesAssocieesImport().switchToStaticMode();

      this.addNewSubderiveC.setVisible(true);

      getBinder().loadComponent(self);
   }

   @Override
   public void disableToolBar(final boolean b){
      super.disableToolBar(b);
      addNewSubderiveC.setDisabled(b || !isCanNew());
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      addNewSubderiveC.setVisible(false);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      getEntitesAssocieesImport().switchToCreateMode();

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      super.switchToEditMode();

      addNewSubderiveC.setVisible(false);

      getEntitesAssocieesImport().switchToEditMode(true);

      getBinder().loadComponent(self);

   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }

   @Override
   public void onClick$addNewC(){
      isSubderive = false;
      switchToCreateMode();
   }

   @Override
   public void onClick$editC(){
      if(this.importTemplate != null){
         if(this.importTemplate.getDeriveParentEntite() == null){
            switchToEditMode();
         }else{ // subderive edit mode
            switchToEditModeSubderive();
         }
      }
   }

   @Override
   public void onClick$cancelC(){
      isSubderive = false;
      clearData();
   }

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void clearData(){
      importColonnesDecorator.clear();
      importColonnes.clear();
      entites.clear();
      clearConstraints();
      super.clearData();
   }

   @Override
   public void onClick$createC(){
      Clients.showBusy(Labels.getLabel("importTemplate.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         setObject(this.importTemplate);
         switchToStaticMode();
         disableToolBar(false);

         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void createNewObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      // si subderive import, retire les 3 premières colonnes entêtes
      if(isSubderive){
         importColonnesDecorator.remove(0);
         importColonnesDecorator.remove(0);
         importColonnesDecorator.remove(0);
      }

      final List<ImportColonne> ics = ImportColonneDecorator.extractListe(importColonnesDecorator);
      for(int i = 0; i < ics.size(); i++){
         ics.get(i).setOrdre(i + 1);
      }

      // update de l'objet
      ManagerLocator.getImportTemplateManager().createObjectManager(importTemplate, importTemplate.getBanque(),
         EntiteDecorator.extractListe(entitesAssociees), ics);

      if(getListeImportTemplate() != null){
         // ajout de l'utilisateur à la liste
         getListeImportTemplate().addToObjectList(importTemplate);
      }

   }

   @Override
   public void onClick$validateC(){
      Clients.showBusy(Labels.getLabel("importTemplate.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
         setObject(this.importTemplate);
         switchToStaticMode();

         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void updateObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      // si subderive import, retire les 3 premières colonnes entêtes
      if(this.importTemplate.getDeriveParentEntite() != null){
         importColonnesDecorator.remove(0);
         importColonnesDecorator.remove(0);
         importColonnesDecorator.remove(0);
      }

      final List<ImportColonne> ics = ImportColonneDecorator.extractListe(importColonnesDecorator);
      for(int i = 0; i < ics.size(); i++){
         ics.get(i).setOrdre(i + 1);
      }

      // update de l'objet
      ManagerLocator.getImportTemplateManager().updateObjectManager(importTemplate, importTemplate.getBanque(),
         EntiteDecorator.extractListe(entitesAssociees), ics, importColonnesToRemove);

      if(getListeImportTemplate() != null){
         getListeImportTemplate().updateObjectGridList(this.importTemplate);
      }
   }

   @Override
   public boolean prepareDeleteObject(){
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getImportTemplateManager().removeObjectManager(importTemplate);
   }

   @Override
   public void setEmptyToNulls(){
      if(this.importTemplate.getDescription().equals("")){
         this.importTemplate.setDescription(null);
      }
      this.importTemplate.setIsEditable(true);
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeImportTemplate
    */
   private ListeImportTemplate getListeImportTemplate(){
      return getObjectTabController().getListe();
   }

   public void onGetAddedObject(final Event e){
      if(e.getData() != null){
         final EntiteDecorator entiteDeco = (EntiteDecorator) e.getData();
         if(!entitesAssociees.contains(entiteDeco)){
            entitesAssociees.add(entiteDeco);
         }

         // on récupère les champs obligatoires que l'on va ajouter
         List<ChampEntite> ces =
            ManagerLocator.getChampEntiteManager().findByEntiteImportAndIsNullableManager(entiteDeco.getEntite(), true, false);

         for(int i = 0; i < ces.size(); i++){
            final ImportColonne ic = new ImportColonne();
            ic.setImportTemplate(importTemplate);
            ic.setChamp(new Champ(ces.get(i)));
            importColonnesDecorator.add(new ImportColonneDecorator(ic));
         }

         if(entitesAssociees.size() == 1){
            selectedEntite = entiteDeco;

            final List<Champ> tmp = new ArrayList<>();
            ces = ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(selectedEntite.getEntite(), true);
            final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
               .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplate.getBanque());
            final List<ChampAnnotation> cas = new ArrayList<>();
            for(int i = 0; i < tas.size(); i++){
               cas.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
            }

            for(int i = 0; i < ces.size(); i++){
               tmp.add(new Champ(ces.get(i)));
            }
            for(int i = 0; i < cas.size(); i++){
               tmp.add(new Champ(cas.get(i)));
            }

            for(int i = 0; i < importColonnesDecorator.size(); i++){
               if(tmp.contains(importColonnesDecorator.get(i).getColonne().getChamp())){
                  tmp.remove(importColonnesDecorator.get(i).getColonne().getChamp());
               }
            }
            champs = ImportChampDecorator.decorateListe(tmp);
            champs.add(0, null);

            final ListModel<ImportChampDecorator> list = new ListModelList<>(champs);
            champsBox.setModel(list);
         }

         getBinder().loadAttribute(colonnesGridEdit, "model");
         getBinder().loadComponent(colonnesGridEdit);
         getBinder().loadComponent(entitesBox);
      }
   }

   public void onGetRemovedObject(final Event e){
      if(e.getData() != null){
         final EntiteDecorator entiteDeco = (EntiteDecorator) e.getData();
         if(entitesAssociees.contains(entiteDeco)){
            entitesAssociees.remove(entiteDeco);
         }

         if(selectedEntite != null && selectedEntite.equals(entiteDeco)){
            if(entitesAssociees.size() > 0){
               selectedEntite = entitesAssociees.get(0);
            }else{
               selectedEntite = null;
            }

            if(selectedEntite != null){
               final List<Champ> tmp = new ArrayList<>();
               final List<ChampEntite> ces =
                  ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(selectedEntite.getEntite(), true);
               final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
                  .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplate.getBanque());
               final List<ChampAnnotation> cas = new ArrayList<>();
               for(int i = 0; i < tas.size(); i++){
                  cas.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
               }

               for(int i = 0; i < ces.size(); i++){
                  tmp.add(new Champ(ces.get(i)));
               }
               for(int i = 0; i < cas.size(); i++){
                  tmp.add(new Champ(cas.get(i)));
               }

               for(int i = 0; i < importColonnesDecorator.size(); i++){
                  if(tmp.contains(importColonnesDecorator.get(i).getColonne().getChamp())){
                     tmp.remove(importColonnesDecorator.get(i).getColonne().getChamp());
                  }
               }
               champs = ImportChampDecorator.decorateListe(tmp);
               champs.add(0, null);
            }else{
               champs = new ArrayList<>();
            }
            final ListModel<ImportChampDecorator> list = new ListModelList<>(champs);
            champsBox.setModel(list);
         }

         final List<Champ> tmp = new ArrayList<>();
         final List<ChampEntite> ces =
            ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(entiteDeco.getEntite(), true);
         final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(entiteDeco.getEntite(), importTemplate.getBanque());
         final List<ChampAnnotation> cas = new ArrayList<>();
         for(int i = 0; i < tas.size(); i++){
            cas.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
         }

         for(int i = 0; i < ces.size(); i++){
            tmp.add(new Champ(ces.get(i)));
         }
         for(int i = 0; i < cas.size(); i++){
            tmp.add(new Champ(cas.get(i)));
         }

         final List<Integer> indexes = new ArrayList<>();
         for(int i = 0; i < importColonnesDecorator.size(); i++){
            if(tmp.contains(importColonnesDecorator.get(i).getColonne().getChamp())){
               indexes.add(i);
            }
         }
         for(int i = 0; i < indexes.size(); i++){
            final ImportColonneDecorator icd = importColonnesDecorator.get(indexes.get(i) - i);
            importColonnesDecorator.remove(indexes.get(i).intValue() - i);

            if(icd.getColonne().getImportColonneId() != null){
               importColonnesToRemove.add(icd.getColonne());
            }
         }
         champs = ImportChampDecorator.decorateListe(tmp);
      }

      getBinder().loadAttribute(colonnesGridEdit, "model");
      getBinder().loadComponent(colonnesGridEdit);
      getBinder().loadComponent(entitesBox);
   }

   /**
    * Monte le champ dans l'ordre de la liste.
    * @param event
    */
   public void onClick$upChamp(final Event event){
      upObject((ImportColonneDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false));
      getBinder().loadAttribute(colonnesGridEdit, "model");

      Clients.scrollIntoView(colonnesGridEdit);
   }

   /**
    * Descend le champ dans l'ordre de la liste.
    * @param event
    */
   public void onClick$downChamp(final Event event){
      final ImportColonneDecorator colDeco =
         (ImportColonneDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      final int tabIndex = importColonnesDecorator.indexOf(colDeco);
      if(tabIndex + 1 < importColonnesDecorator.size()){
         upObject(importColonnesDecorator.get(tabIndex + 1));
      }
      getBinder().loadAttribute(colonnesGridEdit, "model");

      Clients.scrollIntoView(colonnesGridEdit);
   }

   /**
    * Effectue l'operation de mouvements des objets au sein de la liste.
    * @param objet a monter d'un cran
    */
   private void upObject(final ImportColonneDecorator obj){
      final int tabIndex = importColonnesDecorator.indexOf(obj);
      ImportColonneDecorator supObjectInList = null;
      final int limit = isSubderive ? 2 : -1;
      if(tabIndex - 1 > limit){
         supObjectInList = importColonnesDecorator.get(tabIndex - 1);
         importColonnesDecorator.set(tabIndex, supObjectInList);
         importColonnesDecorator.set(tabIndex - 1, obj);
      }
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForImport(){
      boolean admin = false;
      if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin PF => boutons cliquables
      if(admin){
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
      }
      setCanNew(true);
      setCanEdit(true);
      setCanDelete(true);
   }

   /**
    * Prépare le formulaire, en peuplant les listes de choix, 
    * les composants, en fonction de l'objet ImportTemplate.
    * @version 2.0.12
    */
   public void initEditableMode(){

      importColonnesDecorator.clear();

      importColonnesDecorator.addAll(ImportColonneDecorator.decorateListe(importColonnes, isSubderive));

      if(entitesAssociees.size() > 0){
         selectedEntite = entitesAssociees.get(0);
      }else{
         // seule entite derive est utile
         if(isSubderive){
            entitesAssociees.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByIdManager(8)));
            selectedEntite = entitesAssociees.get(0);
         }else{
            selectedEntite = null;
         }
      }
      if(selectedEntite != null){
         final List<Champ> tmp = new ArrayList<>();
         final List<ChampEntite> ces =
            ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(selectedEntite.getEntite(), true);
         final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplate.getBanque());
         final List<ChampAnnotation> cas = new ArrayList<>();
         for(int i = 0; i < tas.size(); i++){
            cas.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
         }

         for(int i = 0; i < ces.size(); i++){
            tmp.add(new Champ(ces.get(i)));
         }
         for(int i = 0; i < cas.size(); i++){
            tmp.add(new Champ(cas.get(i)));
         }

         for(int i = 0; i < importColonnesDecorator.size(); i++){
            if(tmp.contains(importColonnesDecorator.get(i).getColonne().getChamp())){
               tmp.remove(importColonnesDecorator.get(i).getColonne().getChamp());
            }
         }
         champs = ImportChampDecorator.decorateListe(tmp);
      }else{
         champs = new ArrayList<>();
      }
      champs.add(0, null);

      final ListModel<ImportChampDecorator> list = new ListModelList<>(champs);
      champsBox.setModel(list);

      importColonnesToRemove = new ArrayList<>();
   }

   public void onClick$addChamp(){
      if(selectedChamp != null){
         if(champs.contains(selectedChamp)){
            champs.remove(selectedChamp);
         }
         champsBox.invalidate();
         final ListModel<ImportChampDecorator> list = new ListModelList<>(champs);
         champsBox.setModel(list);

         final ImportColonne ic = new ImportColonne();
         ic.setChamp(selectedChamp.getChamp());
         ic.setImportTemplate(importTemplate);
         final ImportColonneDecorator icd = new ImportColonneDecorator(ic);
         importColonnesDecorator.add(icd);
         selectedChamp = null;

         getBinder().loadComponent(colonnesGridEdit);
         Clients.scrollIntoView(colonnesGridEdit);
      }
   }

   /**
    * Supprime un champ de la liste.
    * @param event
    */
   public void onClick$deleteChamp(final Event event){
      // confirmation
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message",
            new String[] {Labels.getLabel("message.deletion.importColonne")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

         final ImportColonneDecorator icd =
            (ImportColonneDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

         importColonnesDecorator.remove(icd);

         Entite e = null;
         if(icd.getColonne().getChamp().getChampEntite() != null){
            e = icd.getColonne().getChamp().getChampEntite().getEntite();
         }else if(icd.getColonne().getChamp().getChampAnnotation() != null){
            e = icd.getColonne().getChamp().getChampAnnotation().getTableAnnotation().getEntite();
         }

         if(e != null && e.equals(selectedEntite)){
            champs.add(new ImportChampDecorator(icd.getColonne().getChamp()));
            final ListModel<ImportChampDecorator> list = new ListModelList<>(champs);
            champsBox.setModel(list);
         }

         if(icd.getColonne().getImportColonneId() != null){
            importColonnesToRemove.add(icd.getColonne());
         }
      }

      getBinder().loadAttribute(colonnesGridEdit, "model");
   }

   public void onOpen$groupHistorique(){
      if(historiques.isEmpty() && importTemplate.getImportTemplateId() != null){
         historiques = ManagerLocator.getImportHistoriqueManager().findByTemplateWithOrderManager(importTemplate);
      }
      getBinder().loadAttribute(historiquesGrid, "model");
      Clients.scrollIntoView(historiquesGrid);
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(descriptionBox);
   }

   /**
    * Filtre les champs par entités.
    * @param event Event : seléction sur la liste entitesBox.
    * @throws Exception
    */
   public void onSelect$entitesBox(final Event event) throws Exception{
      if(selectedEntite != null){
         final List<Champ> tmp = new ArrayList<>();
         final List<ChampEntite> ces =
            ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(selectedEntite.getEntite(), true);
         final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplate.getBanque());
         final List<ChampAnnotation> cas = new ArrayList<>();
         for(int i = 0; i < tas.size(); i++){
            cas.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
         }

         for(int i = 0; i < ces.size(); i++){
            tmp.add(new Champ(ces.get(i)));
         }
         for(int i = 0; i < cas.size(); i++){
            tmp.add(new Champ(cas.get(i)));
         }

         for(int i = 0; i < importColonnesDecorator.size(); i++){
            if(tmp.contains(importColonnesDecorator.get(i).getColonne().getChamp())){
               tmp.remove(importColonnesDecorator.get(i).getColonne().getChamp());
            }
         }
         champs = ImportChampDecorator.decorateListe(tmp);
      }else{
         champs = new ArrayList<>();
      }
      champs.add(0, null);

      final ListModel<ImportChampDecorator> list = new ListModelList<>(champs);
      champsBox.setModel(list);

      if(!champs.contains(selectedChamp)){
         selectedChamp = null;
      }
      champsBox.setSelectedIndex(champs.indexOf(selectedChamp));
   }

   /**
    * Sélectionne le champsBox.
    * @param event Event : seléction sur la liste champsBox.
    * @throws Exception
    */
   public void onSelect$champsBox(final Event event) throws Exception{
      final int ind = champsBox.getSelectedIndex();
      selectedChamp = champs.get(ind);
   }

   /**
    * Affichage des patients importés.
    * @param event
    */
   public void onClickNbPatients(final Event event){
      if(event.getData() != null){
         currentEvent = event;
         Clients.showBusy(Labels.getLabel("importHistorique.affichage.patients"));
         Events.echoEvent("onLaterAffichagePatients", self, null);
      }
   }

   /**
    * Méthode gérant l'affichage des patients.
    */
   public void onLaterAffichagePatients(){
      if(currentEvent != null && currentEvent.getData() != null){
         final ImportHistorique hist = (ImportHistorique) currentEvent.getData();
         final Entite ePatient = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
         // on récupère les importations
         final List<Importation> importations =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, ePatient);

         // on récupère les patients
         final List<Patient> patients = new ArrayList<>();
         for(int i = 0; i < importations.size(); i++){
            patients.add((Patient) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(importations.get(i).getEntite(), importations.get(i).getObjetId()));
         }

         // on met a jour la liste des patients
         final PatientController tabController = (PatientController) PatientController.backToMe(getMainWindow(), page);
         tabController.getListe().updateListContent(patients);
         tabController.switchToOnlyListeMode();
      }
      Clients.clearBusy();
   }

   /**
    * Affichage des prlvts importés.
    * @param event
    */
   public void onClickNbPrelevements(final Event event){
      if(event.getData() != null){
         currentEvent = event;
         Clients.showBusy(Labels.getLabel("importHistorique.affichage.prelevements"));
         Events.echoEvent("onLaterAffichagePrelevements", self, null);
      }
   }

   /**
    * Méthode gérant l'affichage des prlvts.
    */
   public void onLaterAffichagePrelevements(){
      if(currentEvent != null && currentEvent.getData() != null){
         final ImportHistorique hist = (ImportHistorique) currentEvent.getData();
         final List<Prelevement> prelevements =
            ManagerLocator.getImportHistoriqueManager().findPrelevementByImportHistoriqueManager(hist);
         //
         //			Entite ePrlvt = ManagerLocator.getEntiteManager()
         //				.findByNomManager("Prelevement").get(0);
         //			// on récupère les importations
         //			List<Importation> importations = ManagerLocator
         //				.getImportHistoriqueManager()
         //				.findImportationsByHistoriqueAndEntiteManager(
         //						hist, ePrlvt);
         //			
         //			// on récupère les prlvts
         //			List<Prelevement> prelevements = new ArrayList<Prelevement>();
         //			for (int i = 0; i < importations.size(); i++) {
         //				prelevements.add((Prelevement) ManagerLocator.getEntiteManager()
         //						.findObjectByEntiteAndIdManager(
         //								importations.get(i).getEntite(), 
         //								importations.get(i).getObjetId()));
         //			}

         // on met a jour la liste des prlvts
         final PrelevementController tabController =
            (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);
         tabController.getListe().updateListContent(prelevements);
         tabController.switchToOnlyListeMode();
      }
      Clients.clearBusy();
   }

   /**
    * Affichage des echantillons importés.
    * @param event
    */
   public void onClickNbEchantillons(final Event event){
      if(event.getData() != null){
         currentEvent = event;
         Clients.showBusy(Labels.getLabel("importHistorique.affichage.echantillons"));
         Events.echoEvent("onLaterAffichageEchantillons", self, null);
      }
   }

   /**
    * Méthode gérant l'affichage des echantillons.
    */
   public void onLaterAffichageEchantillons(){
      if(currentEvent != null && currentEvent.getData() != null){
         final ImportHistorique hist = (ImportHistorique) currentEvent.getData();
         final Entite eEchan = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
         // on récupère les importations
         final List<Importation> importations =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, eEchan);

         // on récupère les echantillons
         final List<Echantillon> echantillons = new ArrayList<>();
         for(int i = 0; i < importations.size(); i++){
            echantillons.add((Echantillon) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(importations.get(i).getEntite(), importations.get(i).getObjetId()));
         }

         // on met a jour la liste des echantillons
         final EchantillonController tabController =
            (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);
         tabController.getListe().updateListContent(echantillons);
         tabController.switchToOnlyListeMode();
      }
      Clients.clearBusy();
   }

   /**
    * Affichage des prodDerives importés.
    * @param event
    */
   public void onClickNbDerives(final Event event){
      if(event.getData() != null){
         currentEvent = event;
         Clients.showBusy(Labels.getLabel("importHistorique.affichage.prodDerives"));
         Events.echoEvent("onLaterAffichageProdDerives", self, null);
      }
   }

   /**
    * Méthode gérant l'affichage des prodDerives.
    */
   public void onLaterAffichageProdDerives(){
      if(currentEvent != null && currentEvent.getData() != null){
         final ImportHistorique hist = (ImportHistorique) currentEvent.getData();
         final Entite eDerive = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
         // on récupère les importations
         final List<Importation> importations =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, eDerive);

         // on récupère les prodDerives
         final List<ProdDerive> prodDerives = new ArrayList<>();
         for(int i = 0; i < importations.size(); i++){
            prodDerives.add((ProdDerive) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(importations.get(i).getEntite(), importations.get(i).getObjetId()));
         }

         // on met a jour la liste des prodDerives
         final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);
         tabController.getListe().updateListContent(prodDerives);
         tabController.switchToOnlyListeMode();
      }
      Clients.clearBusy();
   }

   public void onClick$importer(){
      Media[] medias;
      // fileInputStream = null;
      // fileInputStreamCorrectif = null;
      medias = Fileupload.get(ObjectTypesFormatters.getLabel("general.upload.limit", new String[] {String.valueOf(10000)}),
         Labels.getLabel("importTemplate.file.upload.title"), 1, 10000, true);
      if(medias != null && medias.length > 0){
         BufferedInputStream fileInputStream = new BufferedInputStream(medias[0].getStreamData());

         if(fileInputStream != null){

            try{
               uploadedWb = WorkbookFactory.create(fileInputStream);
               final List<Sheet> sheets = new ArrayList<>();
               for(int i = 0; i < uploadedWb.getNumberOfSheets(); i++){
                  if(uploadedWb.getSheetAt(i).getLastRowNum() > 0){
                     sheets.add(uploadedWb.getSheetAt(i));
                  }
               }

               if(sheets.size() > 1){
                  // choose sheets modale
                  final HashMap<String, Object> map = new HashMap<>();
                  map.put("sheets", sheets);
                  map.put("parent", self);

                  final Window dialog = (Window) Executions.createComponents("/zuls/imports/ChooseSheetWindow.zul", self, map);
                  dialog.doModal();
               }else{ // sheet = 1
                  Clients.showBusy(Labels.getLabel("importTemplate.wait.import"));
                  Events.echoEvent("onLaterImport", self, null);
               }
            }catch(final IOException ex){
               log.error(ex);
               ex.printStackTrace();
               Clients.clearBusy();
            }catch(final InvalidFormatException ev){
               ev.printStackTrace();
               Clients.clearBusy();
            }finally{
               if(fileInputStream != null){
                  try{
                     fileInputStream.close();
                  }catch(final Exception e){
                     fileInputStream = null;
                  }
               }
            }
         }
      }
   }

   public void onLaterImport(final Event e){
      Boolean ok = false;
      String sheetName = "";
      // if (fileInputStream != null) {
      if(uploadedWb != null){
         ImportHistorique historique = null;
         List<ImportError> errors = new ArrayList<>();
         // Workbook wb = null;
         sheetName = e.getData() == null ? uploadedWb.getSheetAt(0).getSheetName() : (String) e.getData();
         try{
            if(importTemplate.getDeriveParentEntite() == null){
               historique = ManagerLocator.getImportManager().importFileManager(this.importTemplate,
                  SessionUtils.getLoggedUser(sessionScope),
                  e.getData() == null ? uploadedWb.getSheetAt(0) : uploadedWb.getSheet((String) e.getData()));
            }else{ // import sub derive
               historique = ManagerLocator.getImportManager().importSubDeriveFileManager(this.importTemplate,
                  SessionUtils.getLoggedUser(sessionScope),
                  e.getData() == null ? uploadedWb.getSheetAt(0) : uploadedWb.getSheet((String) e.getData()), null);
            }

            // Maj de la fiche
            setObject(this.importTemplate);
            switchToStaticMode();
            ok = true;
         }catch(final RuntimeException re){
            errors = ((ErrorsInImportException) re).getErrors();
            ok = false;
            //			} catch (IOException ex) {
            //				log.error(e);
            //				ex.printStackTrace();
            //			} catch (InvalidFormatException ev) {
            //				ev.printStackTrace();
            //			} finally {
            //				uploadedWb = null;
            //				fileInputStream = null;
            //				if (fileInputStream != null) {
            //					try { fileInputStream.close(); } catch (Exception e) {
            //						fileInputStream = null; 
            //					}
            //				} 
         }

         // si import s'est déroulé correctement
         if(ok){
            try{
               // Prelevements
               final List<TKAnnotableObject> tkObjs = new ArrayList<>();
               tkObjs.addAll(ManagerLocator.getImportHistoriqueManager().findPrelevementByImportHistoriqueManager(historique));
               // Recepteurs
               for(final Recepteur recept : SessionUtils.getRecepteursInterfacages(sessionScope)){
                  ManagerLocator.getSenderFactory().sendMessages(recept, tkObjs, null);
               }
            }catch(final Exception ex){
               Clients.clearBusy();
               throw new RuntimeException(ex);
            }
         }

         Clients.clearBusy();
         // si la modale resultats n'est pas initialisée
         if(openResultatsImportWindow(page, ok, historique, errors, null, uploadedWb, sheetName, self)){
            ((ResultatsImportModale) page.getFellow("resultsImportWindow").getFirstChild().getFellow("fwinResultatsImportModale")
               .getAttributeOrFellow("fwinResultatsImportModale$composer", true)).update(ok, sheetName, historique, errors);
         }
      }
      //		if (fileInputStreamCorrectif != null) {
      //			try { fileInputStreamCorrectif.close(); } catch (Exception e) {
      //				fileInputStreamCorrectif = null; 
      //			}
      //		}

      Clients.clearBusy();
   }

   public void onClick$exporterHeader(){
      final HSSFWorkbook wb = new HSSFWorkbook();
      wb.createSheet("import");
      final HSSFSheet sheet = wb.getSheetAt(0);

      final List<String> tmp = new ArrayList<>();
      for(int i = 0; i < importColonnes.size(); i++){
         tmp.add(importColonnes.get(i).getNom());
      }
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 0, tmp);
      ByteArrayOutputStream out = null;

      out = new ByteArrayOutputStream();
      try{
         wb.write(out);
         Filedownload.save(out.toByteArray(), "application/vnd.ms-excel", "import.xls");
      }catch(final IOException e){
         log.error(e);
      }finally{
         if(out != null){
            try{
               out.close();
            }catch(final Exception e){
               out = null;
            }
         }
      }
   }

   public void onOpen$groupEntites(){
      if(groupEntites.isOpen()){
         getEntitesAssocieesImport().updateComponent();
      }
   }

   /*********************************************************/
   /********************** SUB DERIVE ************************/
   /*********************************************************/

   public List<Entite> subderiveParentEntites = new ArrayList<>();
   {
      subderiveParentEntites.add(ManagerLocator.getEntiteManager().findByIdManager(2));
      subderiveParentEntites.add(ManagerLocator.getEntiteManager().findByIdManager(3));
      subderiveParentEntites.add(ManagerLocator.getEntiteManager().findByIdManager(8));
   }

   public Boolean getSubderive(){
      return isSubderive;
   }

   public Boolean getNonSubderive(){
      return !isSubderive;
   }

   public String getDeriveParentEntiteNom(){
      if(importTemplate != null && importTemplate.getDeriveParentEntite() != null
         && importTemplate.getDeriveParentEntite().getNom() != null){
         return Labels.getLabel("Entite." + importTemplate.getDeriveParentEntite().getNom()).toUpperCase();
      }
      return "";
   }

   public List<Entite> getSubderiveParentEntites(){
      return subderiveParentEntites;
   }

   public void onClick$addNewSubderiveC(){
      isSubderive = true;
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterAddNewSubderive", self, null);
   }

   public void onLaterAddNewSubderive(){
      switchToCreateModeSubderive();
      // ferme wait message
      Clients.clearBusy();
   }

   public void switchToCreateModeSubderive(){

      super.switchToCreateMode();

      addNewSubderiveC.setVisible(false);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      // getEntitesAssocieesImport().switchToCreateMode();

      getBinder().loadComponent(self);
   }

   public void switchToEditModeSubderive(){
      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      super.switchToEditMode();

      addNewSubderiveC.setVisible(false);

      // getEntitesAssocieesImport().switchToEditMode(true);

      getBinder().loadComponent(self);

   }

   private List<ImportColonne> makeSubderiveHeaderCols(final ImportTemplate it){
      final List<ImportColonne> ics = new ArrayList<>();
      final ImportColonne icCodeParent = new ImportColonne();
      icCodeParent.setNom("code.parent");
      icCodeParent.setOrdre(0);
      icCodeParent.setImportTemplate(it);
      ics.add(icCodeParent);
      final ImportColonne icTrQte = new ImportColonne();
      icTrQte.setNom("qte.transf");
      icTrQte.setOrdre(0);
      icTrQte.setImportTemplate(it);
      ics.add(icTrQte);
      final ImportColonne icDs = new ImportColonne();
      icDs.setNom("evt.date");
      icDs.setOrdre(0);
      icDs.setImportTemplate(it);
      ics.add(icDs);
      return ics;
   }

   public I3listBoxItemRenderer getEntiteRenderer(){
      return entiteRenderer;
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public ImportTemplate getImportTemplate(){
      return importTemplate;
   }

   public void setImportTemplate(final ImportTemplate iTemplate){
      this.importTemplate = iTemplate;
   }

   public Set<EntiteDecorator> getEntites(){
      return entites;
   }

   public void setEntites(final Set<EntiteDecorator> e){
      this.entites = e;
   }

   public List<ImportColonne> getImportColonnes(){
      return importColonnes;
   }

   public void setImportColonnes(final List<ImportColonne> iColonnes){
      this.importColonnes = iColonnes;
   }

   public String getEntitesFormatted(){
      final StringBuffer sb = new StringBuffer();
      final Iterator<EntiteDecorator> it = entites.iterator();

      while(it.hasNext()){
         sb.append(it.next().getLabel());
         if(it.hasNext()){
            sb.append(", ");
         }
      }
      return sb.toString();
   }

   public ImportColonneRowRenderer getColonnesRenderer(){
      return colonnesRenderer;
   }

   public void setColonnesRenderer(final ImportColonneRowRenderer cRenderer){
      this.colonnesRenderer = cRenderer;
   }

   public ConstWord getNomConstraint(){
      return ImportConstraints.getNomConstraint();
   }

   public ConstText getDescriptionConstraint(){
      return ImportConstraints.getDescriptionConstraint();
   }

   public List<EntiteDecorator> getEntitesAssociees(){
      return entitesAssociees;
   }

   public void setEntitesAssociees(final List<EntiteDecorator> e){
      this.entitesAssociees = e;
   }

   /**
    * Renvoie le controller associe au composant permettant la gestion 
    * des associations one-to-many avec les banques. 
    */
   public EntitesAssocieesImport getEntitesAssocieesImport(){
      return (EntitesAssocieesImport) self.getFellow("entitesAssocieesImport").getFellow("winEntitesAssocieesImport")
         .getAttributeOrFellow("winEntitesAssocieesImport$composer", true);
   }

   public List<ImportColonneDecorator> getImportColonnesDecorator(){
      return importColonnesDecorator;
   }

   public void setImportColonnesDecorator(final List<ImportColonneDecorator> i){
      this.importColonnesDecorator = i;
   }

   public EntiteDecorator getSelectedEntite(){
      return selectedEntite;
   }

   public void setSelectedEntite(final EntiteDecorator sEntite){
      this.selectedEntite = sEntite;
   }

   public List<ImportChampDecorator> getChamps(){
      return champs;
   }

   public void setChamps(final List<ImportChampDecorator> c){
      this.champs = c;
   }

   public ImportChampDecorator getSelectedChamp(){
      return selectedChamp;
   }

   public void setSelectedChamp(final ImportChampDecorator selectedC){
      this.selectedChamp = selectedC;
   }

   public List<ImportColonne> getImportColonnesToRemove(){
      return importColonnesToRemove;
   }

   public void setImportColonnesToRemove(final List<ImportColonne> iToRemove){
      this.importColonnesToRemove = iToRemove;
   }

   public List<ImportHistorique> getHistoriques(){
      return historiques;
   }

   public void setHistoriques(final List<ImportHistorique> h){
      this.historiques = h;
   }

   public Integer getNbHistorique(){
      return nbHistorique;
   }

   public void setNbHistorique(final Integer nb){
      this.nbHistorique = nb;
   }

   public ImportHistoriqueRowRenderer getHistoriquesRenderer(){
      return historiquesRenderer;
   }

   public void setHistoriquesRenderer(final ImportHistoriqueRowRenderer hRenderer){
      this.historiquesRenderer = hRenderer;
   }

   public Event getCurrentEvent(){
      return currentEvent;
   }

   public void setCurrentEvent(final Event current){
      this.currentEvent = current;
   }

   public Boolean getUpdateTemplateLabelVisible(){
      return updateTemplateCheckbox.isVisible() || (getObject() != null && getObject().getIsUpdate());
   }

   public String getTitle(){
      if(!isSubderive){
         return Labels.getLabel("importTemplate.fiche.title");
      }else{
         return Labels.getLabel("importTemplate.subderive.fiche.title");
      }
   }
}
