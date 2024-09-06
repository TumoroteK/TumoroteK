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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
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
import fr.aphp.tumorotek.manager.io.imports.IncompatibiliteEntreImportTemplateEtBanqueResult;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateStatutPartage;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.ItemForErrorResult;
import fr.aphp.tumorotek.utils.MessagesUtils;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheImportTemplate extends AbstractFicheCombineController
{

   private final Logger log = LoggerFactory.getLogger(FicheImportTemplate.class);

   private static final long serialVersionUID = 3330210719922132352L;
   
   // Labels components
   private Label nomLabel;

   private Label descriptionLabel;

   private Grid colonnesGrid;

   private Group groupHistorique;

   private Grid historiquesGrid;

   //TK-537
   private Menubar menuBarActions;
   private Menuitem menuImporter;
   private Menuitem menuExporterHeader;
   private Menuitem menuAddNewFrom;
   private Menuitem menuPartager;
   private Menuitem menuSupprimerPartage;
   private Menuitem menuArchiver;
   private Menuitem menuReactiver;
   //fin TK-537

   // Editable components
   private Label nomRequired;

   private Textbox nomBox;

   private Textbox descriptionBox;

   private Grid colonnesGridEdit;

   private Row helpAddColumnRow;
   private Row rowAddChamp;

   private Listbox champsBox;

   private Listbox entitesBox;

   // subderive
   private Button addNewSubderiveC;

   private Listbox subderiveParentListbox;

   private Label subderiveParentLabel;
   // fin subderive

   private Button copyEmptyFields;
   
   private Div entitesAssocieesImport;

   private Group groupEntites;

   // Objets Principaux.
   //TK-537
   private ImportTemplateDecorator importTemplateDecorator;
   
   private Set<EntiteDecorator> entites = new HashSet<>();

   private EntiteDecorator selectedEntite;

   private List<EntiteDecorator> entitesAssociees = new ArrayList<>();

   private List<ImportColonne> importColonnes = new ArrayList<>();

   private List<ImportColonne> importColonnesToRemove = new ArrayList<>();

   private List<ImportColonneDecorator> importColonnesDecorator = new ArrayList<>();

   private ImportColonneRowRenderer colonnesRenderer;

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

   //TK-537
   private IncompatibiliteEntreImportTemplateEtBanqueResult incompatibiliteTemplateBanque;
   private Row rowAlerteExecution;
   private Label labelAlerteExecutionError;
   private Label labelAlerteExecutionDetailChamp;
   private Label labelAlerteExecutionToDo;
   private Div iconePartage;
   private Row rowAlerteArchive;
   private Row rowModifiedAfterLastExecution;
   //fin TK-537
   
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
         this.historiquesGrid, this.subderiveParentLabel});//

//      setObjBoxsComponents(new Component[] {this.nomBox, this.descriptionBox, this.colonnesGridEdit, this.helpAddColumnRow,
//         this.rowAddChamp, this.updateTemplateCheckbox, this.subderiveParentListbox});
      //TK-537 en attendant TK-538 : import en modification des annotations
      setObjBoxsComponents(new Component[] {this.nomBox, this.descriptionBox, this.colonnesGridEdit, this.helpAddColumnRow,
         this.rowAddChamp, this.subderiveParentListbox});
      
      setRequiredMarks(new Component[] {this.nomRequired});

      drawActionsForImport();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      copyEmptyFields.setVisible(false);

      Executions.createComponents("/zuls/imports/EntitesAssocieesImport.zul", entitesAssocieesImport, null);
      getEntitesAssocieesImport().setGroupHeader(groupEntites);
      getEntitesAssocieesImport().setPathToRespond(Path.getPath(self));

      //TK-537 : partage des modèles :
      getEntitesAssocieesImport().setGatsbi(isGatsbiTemplate());
      colonnesRenderer = new ImportColonneRowRenderer(retrieveImportTemplateContexte(), retrieveImportTemplateBanque().getEtude());

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.importTemplateDecorator = (ImportTemplateDecorator) obj;

      nbHistorique = 0;
      historiques.clear();

      //TK-537 : modèle partagé
      getEntitesAssocieesImport().setGatsbi(isGatsbiTemplate());
      colonnesRenderer.initContexte(retrieveImportTemplateContexte(), retrieveImportTemplateBanque().getEtude());
      
      //TK-537 : modèle partagé
      reinitControleCoherence();
      checkHasBeenModifiedAfterLastExecution();
      checkCoherenceTemplateWithCurrentBanqueIfNecessary();
      manageAffichageBoutons();
      //fin TK-537

      if(importTemplateDecorator != null && importTemplateDecorator.getImportTemplateId() != null){
         entites.clear();
         entites
            .addAll(EntiteDecorator.decorateListe(ManagerLocator.getImportTemplateManager().getEntiteManager(importTemplateDecorator.getImportTemplate()), 
               isGatsbiTemplate()));

         importColonnes.clear();
         // subderive
         isSubderive = importTemplateDecorator.getDeriveParentEntite() != null;
         if(isSubderive){
            importColonnes.addAll(makeSubderiveHeaderCols(importTemplateDecorator.getImportTemplate()));
         }

         importColonnes.addAll(ManagerLocator.getImportColonneManager().findByImportTemplateManager(importTemplateDecorator.getImportTemplate()));
         if(importTemplateDecorator.getImportTemplateId() != null){
            historiques = ManagerLocator.getImportHistoriqueManager().findByTemplateIdAndImportBanqueIdWithOrderManager(
               importTemplateDecorator.getImportTemplateId(), SessionUtils.getCurrentBanque(sessionScope).getBanqueId());
            if(historiques != null) {
               nbHistorique = historiques.size();
            }
         }
      }

      entitesAssociees.clear();
      final Iterator<EntiteDecorator> it = entites.iterator();
      while(it.hasNext()){
         entitesAssociees.add(it.next());
      }
      getEntitesAssocieesImport().setObjects(entitesAssociees);
      
      // TK-537 : l'appel du super.setObject() pose problème car il empêche que les boutons
      // Modifier et Supprimer soit "disabled" dans le cas où aucun objet n'est sélectionné
      // En effet elle passe false à disableToolBar sans regarder si l'objet transmis contient un id
      // Pour contourner ce problème, avant il y avait un super.setNewObject(); appelé dans setNewObject()
      // pour forcer le disabled à true sur Modifier et Supprimer dans le cas où aucun modèle n'est sélectionné
      // pour faire les choses proprement et gérer les boutons Modifier et Supprimer avec les autres dans manageAffichageBoutons, 
      // suppression de l'appel à super.setObject()
      //super.setObject(importTemplateDecorator);
      superSetObject();
   }

   @Override
   public void cloneObject(){
      setClone(this.importTemplateDecorator.clone());
   }

   @Override
   public ImportTemplateDecorator getObject(){
      return this.importTemplateDecorator;
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
         // @since 2.3.0-gatsbi surcharge
         final List<ChampEntite> ces = GatsbiController.findByEntiteImportAndIsNullableManager(deriveE, true, false, SessionUtils.getCurrentBanque(sessionScope));

         for(int i = ces.size() - 1; i > -1; i--){
            final ImportColonne ic = new ImportColonne();
            ic.setImportTemplate(it);
            ic.setChamp(new Champ(ces.get(i)));
            ic.setImportTemplate(it);
            importColonnes.add(ic);
         }

      }

      setObject(new ImportTemplateDecorator(it, false, false));

   }
  
   
   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.importTemplateDecorator.equals(new ImportTemplateDecorator()));
      this.addNewSubderiveC.setVisible(true);
      addNewSubderiveC.setDisabled(!isCanNew());
      //TK-537 : la méthode super.switchToStaticMode gère l'affichage des boutons Modifier et Supprimer
      //sans prendre en compte le partage de modèle => gestion de la désactivation des boutons
      editC.setDisabled(isEditCDisabled());
      deleteC.setDisabled(isDeleteCDisabled());
      
      copyEmptyFields.setVisible(false);

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("importHistorique.title"));
      sb.append(" (");
      sb.append(nbHistorique);
      sb.append(")");
      groupHistorique.setLabel(sb.toString());
      groupHistorique.setOpen(false);

      getEntitesAssocieesImport().switchToStaticMode();

      getBinder().loadComponent(self);
   }

   @Override
   public void disableToolBar(final boolean b){
      super.disableToolBar(b);
      addNewSubderiveC.setDisabled(b || !isCanNew());
   }

   @Override
   public void switchToCreateMode(){
      switchToCreateMode(false);
   }

   //fromExisting à true indique une création à partir du template courant
   protected void switchToCreateMode(boolean fromExisting){

      //super.switchToCreateMode crée un nouvel objet et le rattache avec la méthode setNewObject de cette classe
      //or setNewObject appelle setObject (toujours de cette classe) qui réinitialise les listes.
      //c'est trop tôt dans le cas d'une création "à partir de" car on doit s'appuyer sur les éléments 
      //chargés par le setObject précédent
      //=> on ne fait pas ce setNewObject dans ce cas :
      if(fromExisting) {
         switchToCreateModeWithoutReinitObject();
         showCopyFieldsButton();
      }
      else {
         super.switchToCreateMode();
      }

      addNewSubderiveC.setVisible(false);

      // Initialisation du mode edition (listes, valeurs...)
      //cette méthode initialise notamment les colonnes => traitement différent en fonction
      //du cas création "classique" ou création "à partir de". 
      //Dans le cas "à partir de", cette méthode prendra en charge l'instanciation de importTemplateDecorator
      //non fait par super.switchToCreateMode
      initEditableMode(fromExisting);

      //dans le cas de l'import dérivé, on ne peut pas ajouter d'entité
      if(!isSubderive) {
         if(fromExisting) {
            getEntitesAssocieesImport().switchToEditMode(true);
         }
         else {
            getEntitesAssocieesImport().switchToCreateMode();
         }
      }
      
      getBinder().loadComponent(self);

   }

   @Override
   public void switchToEditMode(){
      // Initialisation du mode édition (listes, valeurs...)
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
   
   public void onClick$menuAddNewFrom(){
      //TK-537 : on garde la valeur courante pour isSubderive car elle a été alimentée par le setObject => on ne fait rien pour ce cas ici.
      switchToCreateMode(true);
   }
   
   @Override
   public void onClick$editC(){
      //TK-537 
      //on ne peut cliquer sur le bouton que si on est dans la collection propriétaire du modèle
      //et pour la collection propriétaire, si le template est partagé, on regarde le nombre de collections utilisant ce modèle
      if(this.importTemplateDecorator != null){
         if(importTemplateDecorator.isPartage()) {
            Banque banqueTemplate = importTemplateDecorator.getBanque();
            if(importTemplateDecorator.isNotInCurrentCollection()) {
               //ouverture d'une modale info .... cas impossible normalement
               String messageBody = ObjectTypesFormatters.getLabel("importTemplate.modale.edit.collectionIncorrecte",
                new String[] { banqueTemplate.getNom() });
               MessagesUtils.openInfoModal(Labels.getLabel("importTemplate.modale.edit.templateUsed.titre"), messageBody);
               
            }
            else {
               //la méthode doOnclickEditC ne sera appelée que si l'utilisateur confirme dans le cas où le modèle est partagé et utilisé par d'autres collection
               checkUtilisationDuTemplateAndAskConfirmationBeforeDoing(banqueTemplate, "importTemplate.modale.edit.templateUsed.titre", () -> doOnclickEditC());
            }
         }
         else {//le modèle n'est pas partagé
            doOnclickEditC();
         }
      }
   }

   private String buildMessageListantLesBanquesUtilisantUnModele(List<String> listBanqueUtilisantModele, String keyLabelFor2ndPartOfMessage) {
      String messageListantLesBanquesUtilisantUnModele = null;
      int nbBanque = listBanqueUtilisantModele.size();
      if(nbBanque <= 10) {
         String nomsBanqueAsString = listBanqueUtilisantModele.stream().collect(Collectors.joining(", "));
         messageListantLesBanquesUtilisantUnModele = ObjectTypesFormatters.getLabel("importTemplate.modale.edit.templateUsed",
            new String[] { nomsBanqueAsString });
       }
       else {
          String nomsBanqueAsString =listBanqueUtilisantModele.subList(0, 10).stream().collect(Collectors.joining(", "));
          messageListantLesBanquesUtilisantUnModele = ObjectTypesFormatters.getLabel("importTemplate.modale.edit.templateUsed.plusDeDix",
             new String[] { String.valueOf(nbBanque), new StringBuilder(nomsBanqueAsString).append("...").toString() });
       }
      
      String retourChariot = System.getProperty("line.separator");
      
      //ajout de 2 retours à la ligne pour bien aérer
      return new StringBuilder(messageListantLesBanquesUtilisantUnModele).append(retourChariot).append(retourChariot).
         append(Labels.getLabel(keyLabelFor2ndPartOfMessage)).toString();
   }
   
   private void doOnclickEditC() {
      if(this.importTemplateDecorator.getDeriveParentEntite() == null){
         switchToEditMode();
      }else{ // subderive edit mode
         switchToEditModeSubderive();
      }
      //on cache l'éventuelle alerte sur la dernière date d'exécution
      rowModifiedAfterLastExecution.setVisible(false);
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
   public void onClick$deleteC(){
      if(getObject() != null){
         checkUtilisationDuTemplateBeforeDoing(importTemplateDecorator.getBanque(), "importTemplate.modale.delete.templateUsed.titre",
                                                     () -> super.onClick$deleteC() , true);
      }
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

   public void onClick$copyEmptyFields() throws Exception{
      for(ImportColonneDecorator importColonneDecorator : importColonnesDecorator){
         //        si null , l'utilisateur n'a pas rempli l'entrée
         if(importColonneDecorator.getColonne().getNom() == null){
            String champName = importColonneDecorator.getChamp();
            log.debug("l'entrée a été remplie avec la chaîne {}", champName);
            importColonneDecorator.getColonne().setNom(champName);
         }
      }
      copyEmptyFields.setDisabled(true);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         setObject(this.importTemplateDecorator);
         switchToStaticMode();
         refreshListesModele();

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
      ManagerLocator.getImportTemplateManager().createObjectManager(importTemplateDecorator.getImportTemplate(), importTemplateDecorator.getBanque(),
         EntiteDecorator.extractListe(entitesAssociees), ics, SessionUtils.getLoggedUser(sessionScope));

      if(getListeImportTemplate() != null){
         // ajout du importTemplateDecorator à la liste
         getListeImportTemplate().addToObjectList(importTemplateDecorator);
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
         setObject(this.importTemplateDecorator);
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
      if(this.importTemplateDecorator.getDeriveParentEntite() != null){
         importColonnesDecorator.remove(0);
         importColonnesDecorator.remove(0);
         importColonnesDecorator.remove(0);
      }

      final List<ImportColonne> ics = ImportColonneDecorator.extractListe(importColonnesDecorator);
      for(int i = 0; i < ics.size(); i++){
         ics.get(i).setOrdre(i + 1);
      }

      // update de l'objet
      ManagerLocator.getImportTemplateManager().updateObjectManager(importTemplateDecorator.getImportTemplate(), importTemplateDecorator.getBanque(),
         EntiteDecorator.extractListe(entitesAssociees), ics, importColonnesToRemove, SessionUtils.getLoggedUser(sessionScope));

      if(getListeImportTemplate() != null){
         getListeImportTemplate().updateObjectGridList(importTemplateDecorator);
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
      ManagerLocator.getImportTemplateManager().removeObjectManager(importTemplateDecorator.getImportTemplate());
   }

   @Override
   public void setEmptyToNulls(){
      if(this.importTemplateDecorator.getDescription().equals("")){
         this.importTemplateDecorator.setDescription(null);
      }
      this.importTemplateDecorator.getImportTemplate().setIsEditable(true);//A SUPPRIMER
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

   //méthode qui : 
   //- initialise le tableau des colonnes de l'import avec les champs obligatoires après ajout d'une entité (EntitesAssocieesImport.java)
   //- si c'est la première entité ajoutée, sélection de celle-ci dans la liste "Entité" du bloc "Colonnes présentes dans le fichier"
   // et chargement des champs ajoutables au modèle pour cette entité.
   //NB : après la sélection des entités suivantes, la liste Entité du bloc "Colonnes présentes ..." reste à sa valeur courante
   //=> c'est lors de la sélection de l'entité dans ce bloc que les champs non obligatoire de la nouvelle entité sélectionnée sont récupérés
   //via onSelect$entitesBox (appel de getAndDecorateNullableChampForEntite())
   public void onGetAddedObject(final Event e){
      if(e.getData() != null){
         final EntiteDecorator entiteDeco = (EntiteDecorator) e.getData();
         if(!entitesAssociees.contains(entiteDeco)){
            entitesAssociees.add(entiteDeco);
         }

         // on récupère les champs obligatoires que l'on va ajouter
         final List<ChampEntite> ces =
            GatsbiController.findByEntiteImportAndIsNullableManager(entiteDeco.getEntite(), true, false, retrieveImportTemplateBanque());

         /*
          * @since 2.3.0 les labels des lignes Maladie deviennent Visite 
          * pour les contextes Gatsbi
          * (ne concerne pas les champ entites non obligatoires
          */
         EContexte templateContexte = retrieveImportTemplateContexte();
         for(int i = 0; i < ces.size(); i++){
            final ImportColonne ic = new ImportColonne();
            ic.setImportTemplate(importTemplateDecorator.getImportTemplate());
            ic.setChamp(new Champ(ces.get(i)));
            importColonnesDecorator.add(new ImportColonneDecorator(ic, templateContexte));
         }

         if(entitesAssociees.size() == 1){
            selectedEntite = entiteDeco;

            getAndDecorateNullableChampForEntite();
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
                  GatsbiController.findByEntiteImportAndIsNullableManager(selectedEntite.getEntite(), true, true, retrieveImportTemplateBanque());
               final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
                  .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplateDecorator.getBanque());
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
            GatsbiController.findByEntiteImportAndIsNullableManager(entiteDeco.getEntite(), true, null, retrieveImportTemplateBanque());
         final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(entiteDeco.getEntite(), importTemplateDecorator.getBanque());
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
      //si on est dans ce menu, c'est qu'on a le droit de tout faire :
      //TK-537 : une gestion plus fine sera appliquée lors de la sélection d'un template partagé
      //méthode manageAffichageBoutons()
      setCanNew(true);
      setCanEdit(true);
      setCanDelete(true);
      setCanSeeHistorique(true);
   }

   //TK-548 - copie d'un modèle à partir d'un existant
   public void initEditableMode(boolean fromCurrentTemplate){
      if(fromCurrentTemplate) {
         initEditableModeFromCurrentTemplate();
      }
      else {
         initEditableMode();
      }
   }
   
   /**
    * Prépare le formulaire, en peuplant les listes de choix,
    * les composants, en fonction de l'objet ImportTemplate.
    * @version 2.0.12
    */
   public void initEditableMode(){

      importColonnesDecorator.clear();

      importColonnesDecorator.addAll(GatsbiController.decorateImportColonnes(importColonnes, SessionUtils.getCurrentContexte(), isSubderive));

      manageEntitesAssocieesForEditMode();
      
      manageImportChampsForEditMode();
      
      importColonnesToRemove = new ArrayList<>();
       
   }

   /**
    * Prépare le formulaire, en peuplant les listes de choix,
    * les composants, en fonction de l'objet ImportTemplate.
    * @version 2.3.1 (TK-53
    */
   public void initEditableModeFromCurrentTemplate(){

      final ImportTemplate it = new ImportTemplate();
      it.setBanque(SessionUtils.getCurrentBanque(sessionScope));
      //si modèle dérivé, on récupère le parent du modèle "from" :
      if(isSubderive) {
         it.setDeriveParentEntite(importTemplateDecorator.getDeriveParentEntite());
      }
      this.importTemplateDecorator = new ImportTemplateDecorator(it, false, false);
      //TK-537 : au lieu d'appeler ces 2 méthodes (la 2e écrase l'affichage des boutons fait fait la 1ere), 
      //appel d'une méthode qui fait juste ce qu'il faut sans gérer l'affichage des boutons fait dans manageAffichageBoutons
      //super.setObject(importTemplateDecorator);
      //super.setNewObject();
      superSetObject();
      
      List<ImportColonneDecorator> listImportColonneDecoratorForNewTemplate = initImportColonneWithExisting();
      
      importColonnesDecorator = listImportColonneDecoratorForNewTemplate;
      
      manageEntitesAssocieesForEditMode();
      
      manageImportChampsForEditMode();
      
      importColonnesToRemove = new ArrayList<>();
      
      //TK-537
      //une fois l'initialisation des colonnes faites à partir du modèle précédent, nettoyage de l'objet lié au contrôle de cohérence du modèle précédent :
      reinitControleCoherence();
      
   }

   //Copie les ImportColonneDecorator courants et supprime les incohérences
   private List<ImportColonneDecorator> initImportColonneWithExisting(){
      List<ImportColonne> listImportColonneAExclure = new ArrayList<ImportColonne>();
      //List<ImportColonne> listImportColonneForNewTemplate = new ArrayList<ImportColonne>();
      List<ImportColonneDecorator> listImportColonneDecoratorForNewTemplate = new ArrayList<ImportColonneDecorator>();
      if (incompatibiliteTemplateBanque != null) {
         listImportColonneAExclure = incompatibiliteTemplateBanque.retrieveAllImportColonnesKO();
      }
      EContexte banqueContexte = SessionUtils.getCurrentContexte();
      
      //Génération des imports colonnes du nouveau modèle :
      //1 - Création de nouveaux importColonne pour chaque import colonne du modèle de référence compatible avec la banque :
      List<ImportColonne> listImportColonneForNewTemplate = new ArrayList<ImportColonne>();
      for(ImportColonne importColonne : importColonnes) {
         if(!listImportColonneAExclure.contains(importColonne)) {
            listImportColonneForNewTemplate.add(createNewImportColonneFromExisting(importColonne));
 //           ImportColonneDecorator importColonneDecoratorForNewTemplate = new ImportColonneDecorator(createNewFromExisting(importColonne));
 //           importColonneDecoratorForNewTemplate.setImportTemplateContexte(banqueContexte);
 //           listImportColonneDecoratorForNewTemplate.add(importColonneDecoratorForNewTemplate);
         }
      }
      //tri de la liste par ordre :
      Collections.sort(listImportColonneForNewTemplate, Comparator.comparing(ImportColonne::getOrdre));
      //récupération de la dernière valeur de l'ordre
      int lastOrdre = listImportColonneForNewTemplate.get(listImportColonneForNewTemplate.size()-1).getOrdre();;
      
      //2 - Gestion des champs obligatoires non présents dans le modèle => il faut les ajouter au nouveau modèle
      //on les met à la fin comme c'est fait actuellement lorsqu'on modifie un modèle
      //NB : le côté obligatoire ou non est porté par le imporColonneDecorator et non par importColonne 
      //=> pas de gestion spécifique à ce niveau pour les colonnes présentes dans le modèle mais non obligatoires et obligatoires pour la banque 
      if(incompatibiliteTemplateBanque != null && incompatibiliteTemplateBanque.getListChampObligatoirePourBanqueMaisNonPresentDansModele() != null) {
         List<ChampEntite> listChampObligatoireManquant = incompatibiliteTemplateBanque.getListChampObligatoirePourBanqueMaisNonPresentDansModele();
         int nbChampObligatoireManquant = listChampObligatoireManquant.size();
         for(int i=0; i< nbChampObligatoireManquant; i++) {
            listImportColonneForNewTemplate.add(createNewImportColonneFromChampEntite(listChampObligatoireManquant.get(i), lastOrdre+1+i));
         }
      }
      
      //création des decorators
      return GatsbiController.decorateImportColonnes(listImportColonneForNewTemplate, banqueContexte, isSubderive);
   }

   private void manageEntitesAssocieesForEditMode(){
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
   }

   private void manageImportChampsForEditMode(){
      if(selectedEntite != null){
         final List<Champ> tmp = new ArrayList<>();
         final List<ChampEntite> ces =
            GatsbiController.findByEntiteImportAndIsNullableManager(selectedEntite.getEntite(), true, true, importTemplateDecorator.getBanque());
         final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplateDecorator.getBanque());
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
         ic.setImportTemplate(importTemplateDecorator.getImportTemplate());
         final ImportColonneDecorator icd = new ImportColonneDecorator(ic, SessionUtils.getCurrentContexte());
         importColonnesDecorator.add(icd);
         selectedChamp = null;
         showCopyFieldsButton();
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
      //le champ historiques a été chargé au moment du setObject (besoin du nombre d'historiques) => on rafraîchit juste le composant
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

      getAndDecorateNullableChampForEntite();

      if(!champs.contains(selectedChamp)){
         selectedChamp = null;
      }
      champsBox.setSelectedIndex(champs.indexOf(selectedChamp));
   }

   /**
    * Collecte tous les champs non obligatoires pouvant être
    * ajoutés à un import pour l'entité selectionnée par l'utilisateur, et les décore :
    * @since 2.2.1 ajout champ delegue contextuels
    */
   private void getAndDecorateNullableChampForEntite(){

      if(selectedEntite != null){
         final List<Champ> tmp = new ArrayList<>();
         final List<ChampEntite> ces = GatsbiController.findByEntiteImportAndIsNullableManager(selectedEntite.getEntite(), true, true, retrieveImportTemplateBanque());
         
         final List<ChampDelegue> dels = ManagerLocator.getChampDelegueManager()
            .findByEntiteAndContexte(selectedEntite.getEntite(), SessionUtils.getCurrentContexte());

         final List<TableAnnotation> tas = ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(selectedEntite.getEntite(), importTemplateDecorator.getBanque());
         final List<ChampAnnotation> cas = new ArrayList<>();
         for(int i = 0; i < tas.size(); i++){
            cas.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
         }

         for(int i = 0; i < ces.size(); i++){
            tmp.add(new Champ(ces.get(i)));
         }

         for(int i = 0; i < dels.size(); i++){
            tmp.add(new Champ(dels.get(i)));
         }

         // les champs calculés ne sont pas concernés par l'import
         for(int i = 0; i < cas.size(); i++){
            if(!"calcule".equals(cas.get(i).getDataType().getType())){
               tmp.add(new Champ(cas.get(i)));
            }
         }

         // retire les champs déja associés
         for(int i = 0; i < importColonnesDecorator.size(); i++){
            if(tmp.contains(importColonnesDecorator.get(i).getColonne().getChamp())){
               tmp.remove(importColonnesDecorator.get(i).getColonne().getChamp());
            }
         }

         champs.clear();
         champs.addAll(ImportChampDecorator.decorateListe(tmp));
         champs.add(0, null);

         // update model
         champsBox.setModel(new ListModelList<>(champs));
      }
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
    * surcharge de la méthode pour utiliser l'importTemplate et non le decorator ..
    */
   @Override
   public void onClick$historique(){
      openHistoriqueWindow(page, getObject().getImportTemplate());
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

   public void onClick$menuImporter(){

      Media[] medias;
      // fileInputStream = null;
      // fileInputStreamCorrectif = null;
      medias = Fileupload.get(ObjectTypesFormatters.getLabel("general.upload.limit", new String[] {String.valueOf(10000)}),
         Labels.getLabel("importTemplate.file.upload.title"), 1, 10000, true);
      if(medias != null && medias.length > 0){
         BufferedInputStream fileInputStream = new BufferedInputStream(medias[0].getStreamData());

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
            log.error(ex.getMessage(), ex);
            Clients.clearBusy();
         }catch(final InvalidFormatException ev){
            log.error(ev.getMessage(), ev);
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

            GatsbiController.enrichesBanqueWithEtudeContextes(importTemplateDecorator.getBanque(), sessionScope);
            
            if(importTemplateDecorator.getDeriveParentEntite() == null){
               historique = ManagerLocator.getImportManager().importFileManager(this.importTemplateDecorator.getImportTemplate(),
                  SessionUtils.getLoggedUser(sessionScope), SessionUtils.getCurrentBanque(sessionScope),
                  e.getData() == null ? uploadedWb.getSheetAt(0) : uploadedWb.getSheet((String) e.getData()));
            }else{ // import sub derive
               historique = ManagerLocator.getImportManager().importSubDeriveFileManager(this.importTemplateDecorator.getImportTemplate(),
                  SessionUtils.getLoggedUser(sessionScope), SessionUtils.getCurrentBanque(sessionScope),
                  e.getData() == null ? uploadedWb.getSheetAt(0) : uploadedWb.getSheet((String) e.getData()), null);
            }

            // Maj de la fiche
            setObject(this.importTemplateDecorator);
            switchToStaticMode();
            if(importTemplateDecorator.isFromNotUsedTemplates()) {
               refreshListesModele();
            }
            ok = true;
         }catch(final RuntimeException re){
            errors = ((ErrorsInImportException) re).getErrors();
            ok = false;
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

      Clients.clearBusy();
   }

   public void onClick$menuExporterHeader(){
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
         log.error(e.getMessage(), e); 
      }finally{
         try{
            out.close();
            wb.close();
         }catch(final Exception e){
            out = null;
         }
      }
   }

   public void onOpen$groupEntites(){
      if(groupEntites.isOpen()){
         getEntitesAssocieesImport().updateComponent();
      }
   }
   
   public void onClick$menuPartager(){
      if(importTemplateDecorator != null) {
         try {
            ManagerLocator.getImportTemplateManager().updateStatutPartage(
               importTemplateDecorator.getImportTemplateId(), EImportTemplateStatutPartage.PARTAGE_ENCOURS, SessionUtils.getLoggedUser(sessionScope));
            importTemplateDecorator.setStatutPartageCode(EImportTemplateStatutPartage.PARTAGE_ENCOURS.getImportTemplateStatutPartageCode());

            changeActionsVisibilityForNewPartageVisibility(false);
            
            MessagesUtils.openInfoModal(
               Labels.getLabel("importTemplate.partage.modale.title"), 
               ObjectTypesFormatters.getLabel("importTemplate.partage.modale.succes", new String[] { importTemplateDecorator.getNom() }));
         }
         catch(IllegalArgumentException ie) {
            MessagesUtils.openErrorModal(
               Labels.getLabel("importTemplate.partage.modale.title"), 
               ObjectTypesFormatters.getLabel("importTemplate.partage.modale.echec", new String[] { importTemplateDecorator.getNom() }));

         }
      }
      else {
         log.warn("l'utilisateur a pu cliquer sur le bouton 'Partager' alors qu'aucun modèle n'était affiché");
      }
   }
   
   public void onClick$menuSupprimerPartage(){
      if(importTemplateDecorator != null) {
         Banque banqueTemplate = importTemplateDecorator.getBanque();
         checkUtilisationDuTemplateBeforeDoing(banqueTemplate, "importTemplate.suppressionPartage.modale.title", () -> doSupprimerPartage() , true);
      }
      else {
         log.warn("l'utilisateur a pu cliquer sur le bouton 'Supprimer le partage' alors qu'aucun modèle n'était affiché");
      }
   }
   
   private void doSupprimerPartage() {
      try {
         ManagerLocator.getImportTemplateManager().updateStatutPartage(
            importTemplateDecorator.getImportTemplateId(), EImportTemplateStatutPartage.PARTAGE_SUPPRIME, SessionUtils.getLoggedUser(sessionScope));
         importTemplateDecorator.setStatutPartageCode(EImportTemplateStatutPartage.PARTAGE_SUPPRIME.getImportTemplateStatutPartageCode());
         
         changeActionsVisibilityForNewPartageVisibility(true);           
         
         MessagesUtils.openInfoModal(
            Labels.getLabel("importTemplate.suppressionPartage.modale.title"), 
            ObjectTypesFormatters.getLabel("importTemplate.suppressionPartage.modale.succes", new String[] { importTemplateDecorator.getNom() }));
         
      }
      catch(IllegalArgumentException ie) {
         MessagesUtils.openErrorModal(
            Labels.getLabel("importTemplate.suppressionPartage.modale.title"), 
            ObjectTypesFormatters.getLabel("importTemplate.suppressionPartage.modale.echec", new String[] { importTemplateDecorator.getNom() }));

      }
   }
   
   public void onClick$menuArchiver(){
      if(importTemplateDecorator != null) {
         Banque banqueTemplate = importTemplateDecorator.getBanque();
         checkUtilisationDuTemplateAndAskConfirmationBeforeDoing(banqueTemplate, "importTemplate.archivage.modale.title", () -> doArchiver());
      }
      else {
         log.warn("l'utilisateur a pu cliquer sur le bouton 'Archiver' alors qu'aucun modèle n'était affiché");
      }
   }
   
   private void doArchiver() {
      ManagerLocator.getImportTemplateManager().updateArchive(importTemplateDecorator.getImportTemplateId(), true, SessionUtils.getLoggedUser(sessionScope));
      importTemplateDecorator.setArchive(true);
      
      changeActionsVisibilityForNewArchiveVisibility(false);
      
      MessagesUtils.openInfoModal(
         Labels.getLabel("importTemplate.archivage.modale.title"), 
         ObjectTypesFormatters.getLabel("importTemplate.archivage.modale.succes", new String[] { importTemplateDecorator.getNom() }));
      
   }
   
   public void onClick$menuReactiver(){
      if(importTemplateDecorator != null) {
         ManagerLocator.getImportTemplateManager().updateArchive(importTemplateDecorator.getImportTemplateId(), false, SessionUtils.getLoggedUser(sessionScope));
         importTemplateDecorator.setArchive(false);

         changeActionsVisibilityForNewArchiveVisibility(true);
         
      }
      else {
         log.warn("l'utilisateur a pu cliquer sur le bouton 'Réactiver' alors qu'aucun modèle n'était affiché");
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
      if(importTemplateDecorator != null && importTemplateDecorator.getDeriveParentEntite() != null
         && importTemplateDecorator.getDeriveParentEntite().getNom() != null){
         return Labels.getLabel("Entite." + importTemplateDecorator.getDeriveParentEntite().getNom()).toUpperCase();
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
      // Rend le bouton copyEmptyFields cliquable et visible
      showCopyFieldsButton();
      // ferme wait message
      Clients.clearBusy();
   }

   /**
    *
    * Active le bouton de copie des champs vides en le rendant cliquable et visible.
    */
   public void showCopyFieldsButton(){
      copyEmptyFields.setDisabled(false);
      copyEmptyFields.setVisible(true);
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

   public ImportTemplateDecorator getImportTemplateDecorator(){
      return importTemplateDecorator;
   }

   public void setImportTemplateDecorator(final ImportTemplateDecorator iTemplateDecorator){
      this.importTemplateDecorator = iTemplateDecorator;
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

   public String getTitle(){
      if(!isSubderive){
         return Labels.getLabel("importTemplate.fiche.title");
      }
      return Labels.getLabel("importTemplate.subderive.fiche.title");
   }

   private void refreshListesModele () {          
      getObjectTabController().getListe().initObjectsBox();
      getObjectTabController().getListe().setCurrentObject(importTemplateDecorator);
      
      getObjectTabController().initZoneImportTemplatePartageNonUtilise();
   }
   
   //TK-537 : vérifie la cohérence entre les champs du modèle et le contexte de la banque courante.et la collection courante (le résultat sera stocké pour adapter ultérieurement les actions possibles) 
   //Si il y a des incompatibilités, affiche les messages correspondant en haut de la fiche du modèle
   private void checkCoherenceTemplateWithCurrentBanqueIfNecessary() {
      //si l'utilisateur a choisi un modèle partagé qu'il n'a jamais utilisé ou si le modèle n'appartient pas à la collection courante et a été modifié depuis sa dernière utilisation
      if(importTemplateDecorator != null && 
         (importTemplateDecorator.isFromNotUsedTemplates() || 
            (importTemplateDecorator.isNotInCurrentCollection() && rowModifiedAfterLastExecution.isVisible())) ) {
         
         //avant de faire le contrôle, il faut enrichir la banque du modèle dans le cas d'un modèle Gatsbi
         GatsbiController.enrichesBanqueWithEtudeContextes(importTemplateDecorator.getBanque(), sessionScope);
         
         incompatibiliteTemplateBanque = ManagerLocator.getCompatibiliteEntreImportTemplateEtBanqueValidator().validate(
            importTemplateDecorator.getImportTemplate(), SessionUtils.getCurrentBanque(sessionScope));
         if(incompatibiliteTemplateBanque != null) {
            //gestion du message à afficher (on n'en affiche qu'un seul, on privilégie celui sur les champs
            //par rapport à celui sur les annotations :
            if(incompatibiliteTemplateBanque.getChampDelegueItem() != null) {
               populateAlerteInfo(incompatibiliteTemplateBanque.getChampDelegueItem(), retrieveImportTemplateContexte());
            }
            else if(incompatibiliteTemplateBanque.getChampItem() != null) {
               //NB : dans la majorité des cas, on sera sur un template Gatsbi mais on peut aussi remonter une erreur ici
               //dans le cas d'un modèle anapat' et un contexte sérologie si le modèle contient les champs spécifique à l'anapat' !
               populateAlerteInfo(incompatibiliteTemplateBanque.getChampItem(), retrieveImportTemplateContexte());
            }
            else if(incompatibiliteTemplateBanque.getTableAnnotationItem() != null) {
               populateAlerteInfoForAnnotation(incompatibiliteTemplateBanque.getTableAnnotationItem());
            }
            else if(incompatibiliteTemplateBanque.getObligatoireItem() != null) {
               populateAlerteInfoForEntite(incompatibiliteTemplateBanque.getObligatoireItem());
            }
            rowAlerteExecution.setVisible(true);
         }
      }
   }
   
   //TK-537 : Vérifie si le modèle a été modifié depuis la dernière exécution par la collection courante (que le modèle soit partagé ou non cette information étant intéressante dans les 2 cas) 
   //Si c'est le cas, affiche un message en haut de la fiche du modèle pour en informer l'utilisateur
   private void checkHasBeenModifiedAfterLastExecution() {
      boolean hasBeenModifiedAfterLastExecution = false;
      
      if(importTemplateDecorator != null && importTemplateDecorator.getImportTemplateId() != null){
         //regarde si le modèle a été modifié depuis la dernière exécution pour la banque courante pour afficher un message d'alerte
         hasBeenModifiedAfterLastExecution = ManagerLocator.getImportTemplateManager().hasBeenModifiedAfterLastExecution(
                           importTemplateDecorator.getImportTemplate(), SessionUtils.getCurrentBanque(sessionScope));
      }

      rowModifiedAfterLastExecution.setVisible(hasBeenModifiedAfterLastExecution);
   }

   //TK-537 : Vérifie si le modèle est utilisé par d'autres collections. Si oui, un message informera l'utilisateur et lui demandera s'il veut continuer avant de lancer l'action
   //Si le modèle n'est pas utilisé, l'action toDo sera effectuée.
   private void checkUtilisationDuTemplateAndAskConfirmationBeforeDoing(Banque banqueTemplate, String keyLabelForTitreModale, Runnable toDo) {
      checkUtilisationDuTemplateBeforeDoing(banqueTemplate, keyLabelForTitreModale, toDo, false);
   }

   //TK-537 : Vérifie si le modèle est utilisé par d'autres collections. 
   //Si oui : 
   // - si actionImpossibleIfUtilisation vaut true, l'action ne sera pas effectuée et une popup s'ouvrira (avec le titre correspondant à keyLabelForTitreModale passé en paramètre)
   //pour en informer l'utilisateur
   // - si actionImpossibleIfUtilisation vaut false, une popup s'ouvrira (avec le titre correspondant à keyLabelForTitreModale passé en paramètre) pour informer l'utilisateur 
   //et lui demandera s'il veut continuer. Si il confirme, l'action toDo sera lancée
   //Si le modèle n'est pas utilisé, l'action toDo sera effectuée.
   private void checkUtilisationDuTemplateBeforeDoing(Banque banqueTemplate, String keyLabelForTitreModale, Runnable toDo, boolean actionImpossibleIfUtilisation) {
      //contrôle de l'utilisation du modèle par les autres collections
      List<String> listBanqueUtilisantModele = ManagerLocator.getImportHistoriqueManager().
         findNomBanqueUtilisantUnTemplatePartage(importTemplateDecorator.getImportTemplateId(), banqueTemplate.getBanqueId());
      int nbBanque = listBanqueUtilisantModele.size();
      if( nbBanque > 0) {
         String messageHeader = Labels.getLabel(keyLabelForTitreModale);
         if(actionImpossibleIfUtilisation) {
            //Construction des messages personnalisés en fonction du nombre récupéré :
            String messageBody = buildMessageListantLesBanquesUtilisantUnModele(
               listBanqueUtilisantModele, "importTemplate.modale.action.impossible");
            MessagesUtils.openInfoModal(messageHeader, messageBody);
         }
         else {
            //Construction des messages personnalisés en fonction du nombre récupéré :
            String messageBody = buildMessageListantLesBanquesUtilisantUnModele(
               listBanqueUtilisantModele, "importTemplate.modale.edit.continuer");
            boolean isOk = MessagesUtils.openQuestionModal(messageHeader, messageBody);
            if(isOk) {
               toDo.run();
            }
         }
      }
      else {//le modèle n'est pas utilisé
         toDo.run();
      }
   }
   
   
   //TK-537 : un modèle peut être utilisé par une autre banque. Si la banque nécessaire est celle du template mais que l'on est en mode création (template null),
   //on va alors récupérer celle de la session... 
   private Banque retrieveImportTemplateBanque() {
      Banque banque = null;
      if(importTemplateDecorator == null || importTemplateDecorator.getImportTemplate() == null || importTemplateDecorator.getBanque() == null) {
         banque = SessionUtils.getCurrentBanque(sessionScope);
      }
      else {
         banque = importTemplateDecorator.getBanque();
      }
      
      return banque; 
   }

   private boolean isGatsbiTemplate() {
      return retrieveImportTemplateBanque().getEtude() != null;
   }
   
   private EContexte retrieveImportTemplateContexte() {
      return EContexte.findByNom(retrieveImportTemplateBanque().getContexte().getNom());
   }
   
   private void reinitControleCoherence(){
      incompatibiliteTemplateBanque = null;
      if(rowAlerteExecution != null) {
         rowAlerteExecution.setVisible(false);  
      }
      if(labelAlerteExecutionError != null) {
         labelAlerteExecutionError.setValue("");
      }
      if(labelAlerteExecutionDetailChamp != null) {
         labelAlerteExecutionDetailChamp.setValue("");
      }
      if(labelAlerteExecutionToDo != null) {
         labelAlerteExecutionToDo.setValue("");
      }
   }
   
   private void populateAlerteInfo(ItemForErrorResult<ImportColonne> itemForErrorResult, EContexte templateContexte) {
      labelAlerteExecutionError.setValue(Labels.getLabel(itemForErrorResult.getErreurKeyI18n()));
      labelAlerteExecutionToDo.setValue(Labels.getLabel(itemForErrorResult.getTodoKeyI18n()));
      
      labelAlerteExecutionDetailChamp.setValue(itemForErrorResult.getListDataInError().stream()
         .map(importColonne -> ImportUtils.extractChamp(importColonne, templateContexte)).collect(Collectors.joining (", ")));
   }
   
   private void populateAlerteInfoForAnnotation(ItemForErrorResult<TableAnnotation> itemForErrorResult) {
      labelAlerteExecutionError.setValue(Labels.getLabel(itemForErrorResult.getErreurKeyI18n()));
      labelAlerteExecutionToDo.setValue(Labels.getLabel(itemForErrorResult.getTodoKeyI18n()));
      
      labelAlerteExecutionDetailChamp.setValue(itemForErrorResult.getListDataInError().stream()
         .map(tableAnnotation -> tableAnnotation.getNom()).collect(Collectors.joining (", ")));
   }
   
   private void populateAlerteInfoForEntite(ItemForErrorResult<Entite> itemForErrorResult) {
      labelAlerteExecutionError.setValue(Labels.getLabel(itemForErrorResult.getErreurKeyI18n()));
      labelAlerteExecutionToDo.setValue(Labels.getLabel(itemForErrorResult.getTodoKeyI18n()));
      
      labelAlerteExecutionDetailChamp.setValue(itemForErrorResult.getListDataInError().stream()
         .map(entite -> entite.getNom()).collect(Collectors.joining (", ")));
   }
   
   //TK-548 : création d'un modèle à partir d'un autre
   //Création des colonnes du nouveau modèle à partir de celles compatibles passées en paramètres
   private ImportColonne createNewImportColonneFromExisting(ImportColonne existing) {
      ImportColonne newImportColonne = new ImportColonne();
      newImportColonne.setImportTemplate(importTemplateDecorator.getImportTemplate());
      Champ newChamp = null;
      // /!\ dans le cas des dérivés, pour les colonnes du parent du dérivé, les importColonnes sont construits
      //"manuellement" (méthode makeSubderiveHeaderCols()) donc getChamp() est null ....
      if(existing.getChamp() != null) {
         if(existing.getChamp().getChampEntite() != null) {
            newChamp = new Champ(existing.getChamp().getChampEntite());
         }
         else if(existing.getChamp().getChampDelegue() != null) {
            newChamp = new Champ(existing.getChamp().getChampDelegue());
         }
         else if(existing.getChamp().getChampAnnotation() != null) {
            newChamp = new Champ(existing.getChamp().getChampAnnotation());
         }
         newImportColonne.setChamp(newChamp);
      }
      newImportColonne.setNom(existing.getNom());
      newImportColonne.setOrdre(existing.getOrdre());
      
      return newImportColonne;
   }

   //TK-548 : création d'un modèle à partir d'un autre
   //création d'une colonne à partir d'un champ
   private ImportColonne createNewImportColonneFromChampEntite(ChampEntite champEntite, int ordre) {
      ImportColonne newImportColonne = new ImportColonne();
      newImportColonne.setImportTemplate(importTemplateDecorator.getImportTemplate());
      newImportColonne.setChamp(new Champ(champEntite));
      newImportColonne.setOrdre(ordre);
      
      return newImportColonne;
   }
   
   
   private void  manageAffichageBoutons() {
      boolean importTemplateNonNull = isImportTemplateNonNull();
      boolean templateArchive = importTemplateNonNull && importTemplateDecorator.isArchive();
      boolean appartientBanqueCourante = appartientBanqueCourante();
      menuBarActions.setVisible(importTemplateNonNull);
      // /!\ si menuBar est visible, les 2 boutons menuImporter et menuExporterHeader sont toujours affichés mais "disabled" lorsque l'utilisation n'est pas pertinente.
      menuImporter.setDisabled(menuBarActions.isVisible() && (templateArchive || incompatibiliteTemplateBanque != null));
      menuExporterHeader.setDisabled(menuBarActions.isVisible() && (templateArchive || incompatibiliteTemplateBanque != null));
      menuAddNewFrom.setVisible(menuBarActions.isVisible());
      boolean templatePartage = importTemplateNonNull && importTemplateDecorator.isPartage();
      menuPartager.setVisible(menuBarActions.isVisible() && appartientBanqueCourante && !templateArchive && !templatePartage);
      menuSupprimerPartage.setVisible(menuBarActions.isVisible() && appartientBanqueCourante && templatePartage);
      menuArchiver.setVisible(menuBarActions.isVisible() && appartientBanqueCourante && !templateArchive);
      menuReactiver.setVisible(menuBarActions.isVisible() && appartientBanqueCourante && templateArchive);
      historique.setVisible(menuBarActions.isVisible() && appartientBanqueCourante && isCanSeeHistorique());
      editC.setDisabled(isEditCDisabled());
      deleteC.setDisabled(isDeleteCDisabled());      
   }
   
   //TK-537 : met à jour la visibilité / accessibilité aux boutons du menu action suite à un changement de valeur du partage.
   //Le paramètre indique la nouvelle visibilité du menu Partager
   private void changeActionsVisibilityForNewPartageVisibility(boolean partagerVisible) {
      menuPartager.setVisible(partagerVisible);
      menuSupprimerPartage.setVisible(!partagerVisible);
      getBinder().loadComponent(menuPartager);
      getBinder().loadComponent(menuSupprimerPartage);  
      getBinder().loadComponent(iconePartage);
      refreshListImportTemplateDecorator();
   }
   
   //TK-537 : met à jour la visibilité / accessibilité aux boutons du menu action suite à un changement de valeur du champ archive. 
   //Le paramètre indique la nouvelle visibilité du menu Archiver
   private void changeActionsVisibilityForNewArchiveVisibility(boolean archiverVisible) {
      menuArchiver.setVisible(archiverVisible);
      menuReactiver.setVisible(!archiverVisible);
      //l'archivage a beaucoup d'impact sur le menu action => on rafraichit tout :
      refreshAffichageBoutons();
      getBinder().loadComponent(rowAlerteArchive);
      refreshListImportTemplateDecorator();
   }
   
   private void refreshListImportTemplateDecorator() {
      ListeImportTemplate listeImportTemplate = getObjectTabController().getListe();
      listeImportTemplate.updateObjectGridList(importTemplateDecorator);
      listeImportTemplate.getBinder().loadAttribute(listeImportTemplate.getObjectsListGrid(), "model");
   }
   
   private void refreshAffichageBoutons() {
      manageAffichageBoutons();
      List<Component> listItemAction = menuBarActions.getChildren().get(0).getChildren().get(0).getChildren();
      
      getBinder().loadComponent(menuBarActions);
      for(Component menuItem : listItemAction) {
         getBinder().loadComponent(menuItem);
      }
   }

   //TK-537 : complète la méthode standard qui gère l'affichage des boutons Modifier et supprimer 
   //pour cacher le menu Action
   @Override
   protected void switchButtonsToCreateOrEdit(final boolean isCreate){
      super.switchButtonsToCreateOrEdit(isCreate);
      menuBarActions.setVisible(false);
   }
   
   //TK-537
   //la méthode standard super.setObject pose problème dans l'affichage des boutons car elle force le disabled à false pour
   //l'affichage des boutons Modifier et Supprimer sans regarder si un objet est sélectionné
   //=> ça oblige à faire un super.setNewObject() - qui force le disabled à true - dans la méthode setNewObject() :-(
   //pour gérer proprement l'affichage de tous les boutons dans une seule méthode manageAffichageBoutons, surcharge de la
   //méthode super pour ne pas faire le disableToolbar
   private void superSetObject() {
      cloneObject();
      getBinder().loadComponent(self);
   }
   
   //TK-537 : partage de modèle
   private boolean isEditCDisabled() {
      return (editC.isVisible() && 
                (!isImportTemplateNonNull() || !isCanEdit() || !appartientBanqueCourante() || importTemplateDecorator.isArchive()));
   }
   
   private boolean isDeleteCDisabled() {
      return (deleteC.isVisible() && (!isImportTemplateNonNull() || !isCanDelete() || !appartientBanqueCourante()) );
   }
   
   private boolean isImportTemplateNonNull() {
      return importTemplateDecorator != null && importTemplateDecorator.getImportTemplateId() != null;
   }
   
   private boolean appartientBanqueCourante() {
      return isImportTemplateNonNull() && importTemplateDecorator.getImportTemplate().getBanque().equals(SessionUtils.getCurrentBanque(sessionScope));
   }
   //fin TK-537
}
