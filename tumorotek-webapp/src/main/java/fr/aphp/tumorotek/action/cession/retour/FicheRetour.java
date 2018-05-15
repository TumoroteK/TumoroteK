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
package fr.aphp.tumorotek.action.cession.retour;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.CessionConstraints;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Temperature;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheRetour extends AbstractFicheCombineController
{

   //private Log log = LogFactory.getLog(FicheRetour.class);

   private static final long serialVersionUID = 6300875937416491348L;

   private boolean dateRetourOblig = false;

   private Window fwinRetour;

   // Labels.
   private Label codeObjetLabel;
   // private Label entiteOperationLabel;
   // private Label entiteCodeLabel;
   private Label dateSortieLabel;
   private Label dateRetourLabel;
   private Label tempMoyenneLabel;
   private Label sterileLabel;
   private Label collaborateurLabel;
   private Label observationsLabel;
   private Label emplacementLabel;

   // Editable components : mode d'édition ou de création.
   private Label dateSortieRequired;
   private Label dateRetourRequired;
   private CalendarBox dateSortieCalBox;
   private CalendarBox dateRetourCalBox;
   private Decimalbox tempMoyenneBox;
   private Label tempMoyenneRequired;
   private Checkbox sterileBox;
   private Checkbox impactBox;
   private Combobox collaborateurBox;
   private Textbox observationsBox;
   private Textbox emplacementBox;
   private Listbox temperatureListBox;
   private Listbox objectsBox;
   private Row rowSelectionTitle;
   private Row rowSelection;
   private Row rowEmplacement;

   // Objets Principaux.
   private Retour retour = new Retour();
   private TKStockableObject tkObject;
   private final ListModelList<TKStockableObject> objects = new ListModelList<>();
   // private Set<Listitem> selectedObjectsItem = new HashSet<Listitem>();
   private HashMap<TKStockableObject, Emplacement> oldEmplacements = new HashMap<>();
   private Entite entiteOperation = null;
   private Cession cession = null;
   private Transformation transformation = null;
   private Incident incident = null;
   private String oldEmplacementAdrl = null;
   private Conteneur conteneur = null;

   private List<Collaborateur> collaborateurs;
   private Collaborateur selectedCollaborateur;
   private List<String> nomsAndPrenoms = new ArrayList<>();

   private List<Temperature> temperatures = new ArrayList<>();
   private Temperature selectedTemperature;

   private boolean insertOk = true;
   private boolean isAdmin = false;

   public List<TKStockableObject> getObjects(){
      return objects;
   }

   public void setObjects(final List<TKStockableObject> objs){
      if(objs != null){
         this.objects.addAll(objs);
      }
      Collections.sort(getObjects(), new TKStockableObject.CodeComparator(false));
   }

   public void setCession(final Cession ces){
      this.cession = ces;
      // les évènements de stockage ne peuvent être incomplets 
      // que si la cession est attente. 
      final boolean validee = ces != null && !ces.getCessionStatut().getStatut().equals("EN ATTENTE");
      dateRetourOblig = dateRetourOblig || validee;
      dateRetourCalBox.setDisabled(!validee);
   }

   public void setTransformation(final Transformation tr){
      this.transformation = tr;
      dateRetourOblig = (dateRetourOblig || tr != null);
   }

   public void setIncident(final Incident ic){
      this.incident = ic;
      dateRetourOblig = (dateRetourOblig || ic != null);
   }

   //	public void setOldEmplacement(Emplacement e) {
   //		this.oldEmplacement = e;
   //	}

   /**
    * Pré-remplie le calendarBox de la date de Sortie.
    * Pré-remplie le calendarBox de la date de retour, avec la date 
    * actuelle
    * @param Date in
    */
   public void setInitDateSortie(final Date in){
      if(in != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(in);
         retour.setDateSortie(cal);
         //getBinder().loadAttribute(dateSortieCalBox, "value");
         getBinder().loadComponent(dateSortieCalBox);

         if(cession != null && !cession.getCessionStatut().getStatut().equals("EN ATTENTE")){
            Calendar out = Utils.getCurrentSystemCalendar();
            if(out.before(cal)){
               out = cal;
            }
            //	Calendar out = (Calendar) cal.clone();
            //	out.set(Calendar.HOUR_OF_DAY, 0);
            //	out.set(Calendar.HOUR, 0);
            //	out.set(Calendar.MINUTE, 0);
            retour.setDateRetour(out);
            getBinder().loadComponent(dateRetourCalBox);
         }
      }
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      addNewC = new Button();
      deleteC = new Button();
      editC = new Button();

      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.retour");

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.dateSortieLabel, this.dateRetourLabel, this.tempMoyenneLabel,
         this.sterileLabel, this.collaborateurLabel, this.observationsLabel, this.emplacementLabel,});

      setObjBoxsComponents(
         new Component[] {this.dateSortieCalBox, this.dateRetourCalBox, this.tempMoyenneBox, this.collaborateurBox,
            this.sterileBox, this.observationsBox, this.temperatureListBox, this.emplacementBox, this.impactBox});

      setRequiredMarks(new Component[] {dateSortieRequired, tempMoyenneRequired});

      if(sessionScope.containsKey("AdminPF")){
         isAdmin = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         isAdmin = (Boolean) sessionScope.get("Admin");
      }

      drawActionsForRetours();

      winPanel.setHeight("360px");

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.retour = (Retour) obj;

      // date sortie = current si nouveau retour
      if(retour.getDateSortie() == null){
         retour.setDateSortie(Calendar.getInstance());
      }

      dateRetourCalBox.setHasChanged(retour.getDateRetour() != null);

      if(this.cession != null){
         retour.setCession(cession);
      }else if(this.transformation != null){
         retour.setTransformation(transformation);
      }else if(this.incident != null){
         retour.setIncident(incident);
      }
      // else if (this.conteneur != null) {
      //	retour.setConteneur(conteneur);
      //}

      if(tkObject == null){
         tkObject = ManagerLocator.getRetourManager().getObjetFromRetourManager(retour);
      }

      if(retour.getCession() != null){
         this.entiteOperation = ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0);
         this.cession = retour.getCession();
      }else if(retour.getTransformation() != null){
         this.entiteOperation = ManagerLocator.getEntiteManager().findByNomManager("Transformation").get(0);
         this.transformation = retour.getTransformation();
      }else if(retour.getIncident() != null){
         this.entiteOperation = ManagerLocator.getEntiteManager().findByNomManager("Incident").get(0);
         this.incident = retour.getIncident();
      }
      // else if (retour.getConteneur() != null) {
      //	this.entiteOperation = ManagerLocator.getEntiteManager()
      //						.findByNomManager("Conteneur").get(0);
      //this.conteneur = retour.getConteneur();
      // }

      if(retour.getOldEmplacementAdrl() != null){
         setOldEmplacementAdrl(retour.getOldEmplacementAdrl());
      }

      super.setObject(retour);
   }

   @Override
   public void cloneObject(){
      setClone(this.retour.clone());
   }

   @Override
   public Retour getObject(){
      return this.retour;
   }

   public void setTkObject(final TKStockableObject tk){
      this.tkObject = tk;
      if(retour.getRetourId() == null){ // creation
         if(tk instanceof Echantillon){
            setOldEmplacementAdrl(ManagerLocator.getEchantillonManager().getEmplacementAdrlManager((Echantillon) tk));
         }else if(tk instanceof ProdDerive){
            setOldEmplacementAdrl(ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager((ProdDerive) tk));
         }
      }
      getBinder().loadAttribute(codeObjetLabel, "value");
      getBinder().loadAttribute(emplacementBox, "value");
   }

   @Override
   public void setNewObject(){
      setObject(new Retour());
      super.setNewObject();
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   public void switchToCreateMode(final String obs){

      super.switchToCreateMode();

      retour.setObservations(obs);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      if(getObjects() != null && getObjects().size() > 0){
         rowEmplacement.setVisible(false);
         if(getObjects().size() <= 100 && getObjects().size() > 1){
            rowSelection.setVisible(true);
            rowSelectionTitle.setVisible(true);
            //				for (int i = 0; i < objectsBox.getItems().size(); i++) {
            //					selectedObjectsItem.add((Listitem) 
            //							objectsBox.getItems().get(i));
            //				}
            objectsBox.setModel(objects);
            ((Selectable<TKStockableObject>) objectsBox.getModel()).setMultiple(true);
            ((Selectable<TKStockableObject>) objectsBox.getModel()).setSelection(getObjects());

         }else{

            ((Window) fwinRetour.getParent().getParent()).setHeight("380px");
         }
         //			else {
         //				ListModel<TKStockableObject> list = 
         //						new ListModelList<TKStockableObject>(
         //						new ArrayList<TKStockableObject>());
         //				objectsBox.setModel(list);
         //			}
      }

      // if (tkObject != null && oldEmplacementsAdrl != null
      //		&& oldEmplacementsAdrl.containsKey(tkObject)) {
      //	retour.setOldEmplacementAdrl(oldEmplacementsAdrl.get(tkObject));
      //}
      dateRetourRequired.setVisible(dateRetourOblig);
      if(dateRetourOblig){
         dateRetourCalBox.setConstraint("no empty");
      }else{
         dateRetourCalBox.setConstraint(null);
      }

      disableIfNotAdmin();

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){

      super.switchToEditMode();
      initEditableMode();

      disableIfNotAdmin();

      getBinder().loadComponent(self);
   }

   /**
    * Si l'utilisateur n'est pas admin, tous les champs de formulaire 
    * sont disabled sauf date de retour
    */
   private void disableIfNotAdmin(){
      if(!isAdmin){
         //dateSortieCalBox.setDisabled(true);
         //collaborateurBox.setDisabled(true);
         //sterileBox.setDisabled(true);
         //observationsBox.setDisabled(true); 
         //temperatureListBox.setDisabled(true);
      }
   }

   @Override
   public void setFocusOnElement(){
      dateSortieCalBox.setFocus(true);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){

      setEmptyToNulls();
      insertOk = true;

      if(tkObject != null){
         ManagerLocator.getRetourManager().createOrUpdateObjectManager(retour, tkObject, null, selectedCollaborateur, cession,
            transformation, incident, SessionUtils.getLoggedUser(sessionScope), "creation");
      }else{ // creation liste
         final List<TKStockableObject> objToUpdate = new ArrayList<>();
         if(getObjects().size() <= 100 && getObjects().size() > 1){
            objToUpdate.addAll(findSelectedObjList());
         }else{
            objToUpdate.addAll(getObjects());
         }
         insertOk = ManagerLocator.getRetourManager().createRetourHugeListManager(objToUpdate, oldEmplacements, retour,
            selectedCollaborateur, cession, transformation, incident, SessionUtils.getLoggedUser(sessionScope));

         // si l'insertion s'est passée sans erreurs
         // refresh statuts objets dans les listes
         // si ENCOURS ou IMPACT
         if(insertOk && (retour.getDateRetour() == null || (retour.getImpact() != null && retour.getImpact())
            || (retour.getSterile() != null && !retour.getSterile())) && getObjects().size() < 100){

            final List<TKdataObject> derives = new ArrayList<>();
            final List<TKdataObject> echans = new ArrayList<>();

            for(int i = 0; i < getObjects().size(); i++){
               if(getObjects().get(i) instanceof Echantillon){
                  // on vérifie que l'on retrouve bien la 
                  // page contenant la liste
                  // des échantillons
                  // refresh object
                  echans.add(ManagerLocator.getEchantillonManager().findByIdManager(getObjects().get(i).listableObjectId()));
               }else if(getObjects().get(i) instanceof ProdDerive){
                  // on vérifie que l'on retrouve bien la 
                  // page contenant la liste des dérives

                  // refresh object
                  derives.add(ManagerLocator.getProdDeriveManager().findByIdManager(getObjects().get(i).listableObjectId()));
               }
            }
            // update de l'échantillon dans la liste
            if(!echans.isEmpty() && getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")
               && getEchantillonController() != null && getEchantillonController().getListe() != null){
               getEchantillonController().getListe().updateMultiObjectsGridListInPlace(echans);
            }
            // update de l'échantillon dans la liste
            if(!derives.isEmpty() && getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")
               && getProdDeriveController() != null && getProdDeriveController().getListe() != null){
               getProdDeriveController().getListe().updateMultiObjectsGridListInPlace(derives);
            }
         }
      }
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getRetourManager().removeObjectManager(getObject());
   }

   @Override
   public void setEmptyToNulls(){

      // Gestion du collaborateur
      final String selectedNomAndPremon = collaborateurBox.getValue().toUpperCase();
      collaborateurBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      if(this.retour.getObservations().equals("")){
         this.retour.setObservations(null);
      }
   }

   @Override
   public void updateObject(){

      setEmptyToNulls();

      ManagerLocator.getRetourManager().createOrUpdateObjectManager(retour, tkObject, null, selectedCollaborateur, cession,
         transformation, incident, SessionUtils.getLoggedUser(sessionScope), "modification");
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes.
    */
   public void initEditableMode(){
      // init des collaborateurs
      nomsAndPrenoms = new ArrayList<>();
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
      }
      if(this.retour.getCollaborateur() != null){
         selectedCollaborateur = this.retour.getCollaborateur();
      }

      if(selectedCollaborateur != null && collaborateurs.contains(selectedCollaborateur)){
         collaborateurBox.setValue(selectedCollaborateur.getNomAndPrenom());
      }else if(selectedCollaborateur != null){
         collaborateurs.add(selectedCollaborateur);
         nomsAndPrenoms.add(selectedCollaborateur.getNomAndPrenom());
      }else{
         collaborateurBox.setValue("");
         selectedCollaborateur = null;
      }

      collaborateurBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
      if(selectedCollaborateur != null){
         collaborateurBox.setValue(selectedCollaborateur.getNomAndPrenom());
      }

      temperatures = ManagerLocator.getTemperatureManager().findAllObjectsManager();
      temperatures.add(0, null);
      selectedTemperature = null;

      getBinder().loadComponent(collaborateurBox);

      // objectsBox.setModel(new ListModelList<TKStockableObject>(objects));
   }

   @Override
   public void onClick$validateC(){
      try{
         updateObject();
         cloneObject();

         // update si vient de la liste
         if(getListeRetour() != null){
            getListeRetour().updateObjectGridListFromOtherPage(getObject(), true);
         }

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));

      }catch(final RuntimeException re){
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void onClick$revertC(){
      // les dates sont passées lors des validations
      // implique recup des valeurs du clone
      retour.setDateSortie(((Retour) getClone()).getDateSortie());
      retour.setDateRetour(((Retour) getClone()).getDateRetour());
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$createC(){
      Clients.showBusy(null);
      Events.echoEvent("onLaterCreate", self, null);

   }

   @Override
   public void onLaterCreate(){
      try{
         validateCoherenceDate(dateSortieCalBox, dateSortieCalBox.getValue());
         validateCoherenceDate(dateRetourCalBox, dateRetourCalBox.getValue());
         createNewObject();

         Clients.clearBusy();

         // si l'insertion s'est passée sans erreurs
         if(insertOk){
            // ajout retour à la liste
            if(getListeRetour() != null){
               getListeRetour().addToObjectList(getObject());
            }else{
               // information
               Messagebox.show(Labels.getLabel("message.retour.create.done"), Labels.getLabel("message.retour.create.title"),
                  Messagebox.OK, Messagebox.INFORMATION);
            }

            // fermeture de la fenêtre
            Events.postEvent(new Event("onClose", self.getRoot()));
         }else{
            Messagebox.show(Labels.getLabel("message.retour.error"), "Error", Messagebox.OK, Messagebox.ERROR);
         }

      }catch(final RuntimeException re){
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void onClick$cancelC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(dateSortieCalBox);
      Clients.clearWrongValue(dateRetourCalBox);
      Clients.clearWrongValue(observationsBox);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForRetours(){
      drawActionsButtons("Stockage");
   }

   /**
    * Retourne les échantillons sélectionnées.
    * @return
    */

   private List<TKStockableObject> findSelectedObjList(){
      if(getObjects().size() <= 100){
         final List<TKStockableObject> list = new ArrayList<>();
         list.addAll(((Selectable<TKStockableObject>) objectsBox.getModel()).getSelection());
         return list;
      }
      return getObjects();
   }

   public void onClick$cocherTous(){
      ((Selectable<TKStockableObject>) objectsBox.getModel()).setSelection(objects);
   }

   public void onClick$decocherTous(){
      //		selectedObjectsItem = new HashSet<Listitem>();
      //		getBinder().loadComponent(objectsBox);
      ((Selectable<TKStockableObject>) objectsBox.getModel()).clearSelection();
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/
   public Retour getRetour(){
      return retour;
   }

   public void setRetour(final Retour r){
      setObject(r);
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur conteneur){
      this.conteneur = conteneur;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public List<String> getNomsAndPrenoms(){
      return nomsAndPrenoms;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selected){
      this.selectedCollaborateur = selected;
   }

   public List<Temperature> getTemperatures(){
      return temperatures;
   }

   public void setTemperatures(final List<Temperature> temps){
      this.temperatures = temps;
   }

   public Temperature getSelectedTemperature(){
      return selectedTemperature;
   }

   public void setSelectedTemperature(final Temperature selected){
      this.selectedTemperature = selected;
   }

   /*********************************************************/
   /********************** FORMATTERS. **********************/
   /*********************************************************/
   public String getCodeObjet(){
      if(tkObject != null){
         return tkObject.getPhantomData();
      }else if(!getObjects().isEmpty()){
         String codes = getObjects().get(0).getPhantomData();
         if(getObjects().size() > 1){
            codes = codes + ", " + getObjects().get(1).getPhantomData() + " ...";
         }
         return codes;
      }
      return null;
   }

   public String getEntiteOperation(){
      if(entiteOperation != null){
         if(!entiteOperation.getNom().equals("Emplacement")){
            return entiteOperation.getNom();
         }
         return Labels.getLabel("ficheRetour.deplacement");
      }
      return null;
   }

   public String getEntiteCode(){
      String entiteCode = null;
      if(entiteOperation != null){
         if(cession != null){
            entiteCode = cession.getNumero();
         }else if(incident != null){
            entiteCode = incident.getNom();
         }
         //			else if (oldEmplacement != null) {
         //				entiteCode = ManagerLocator.getEmplacementManager()
         //					.getAdrlManager(oldEmplacement);
         //			} 
      }
      return entiteCode;
   }

   public String getSClassCollaborateur(){
      return ObjectTypesFormatters.sClassCollaborateur(this.retour.getCollaborateur());
   }

   public String getDateSortieFormatted(){
      return ObjectTypesFormatters.dateRenderer2(this.retour.getDateSortie());

   }

   public String getDateRetourFormatted(){
      return ObjectTypesFormatters.dateRenderer2(this.retour.getDateRetour());
   }

   public String getSterileFormatted(){
      return ObjectTypesFormatters.booleanLitteralFormatter(this.retour.getSterile());
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(retour.equals(new Retour()));
   }

   public void onBlur$dateSortieCalBox(){
      dateSortieCalBox.clearErrorMessage(dateSortieCalBox.getValue());
      validateCoherenceDate(dateSortieCalBox, dateSortieCalBox.getValue());
      dateSortieCalBox.setValue(retour.getDateSortie());
   }

   public void onBlur$dateRetourCalBox(){
      if(dateRetourCalBox.getValue() == null && !dateRetourCalBox.isHasChanged()){
         final Calendar current = Calendar.getInstance();
         dateRetourCalBox.setValue(current);

      }else{
         dateRetourCalBox.clearErrorMessage(dateRetourCalBox.getValue());
         validateCoherenceDate(dateRetourCalBox, dateRetourCalBox.getValue());
      }
      retour.setDateRetour(dateRetourCalBox.getValue());
      dateRetourCalBox.setHasChanged(true);
   }

   private void validateCoherenceDate(final Component comp, final Object value){
      if(comp.getId().equals("dateSortieCalBox")){
         if(value == null || value.equals("")){
            throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
         }
         retour.setDateSortie(((CalendarBox) comp).getValue());
         if(retour.getDateRetour() != null){
            if(!ValidationUtilities.checkWithDate(retour.getDateSortie(), "dateSortie", retour.getDateRetour(), "date",
               "validation", "DateRetour", null, true)){
               throw new WrongValueException(comp, Labels.getLabel("date" + ".validation.supDateRetour"));
            }
         }
      }else if(comp.getId().equals("dateRetourCalBox")){
         retour.setDateRetour(((CalendarBox) comp).getValue());
         if(retour.getDateRetour() != null && retour.getDateSortie() != null
            && !ValidationUtilities.checkWithDate(retour.getDateRetour(), "dateRetour", retour.getDateSortie(), "date",
               "validation", "DateSortie", null, false)){
            throw new WrongValueException(comp, Labels.getLabel("date" + ".validation.infDateSortie"));
         }
      }
   }

   public ConstText getObsConstraint(){
      return CessionConstraints.getDescrConstraint();
   }

   /**
    * Méthode appelée lors de la sélection d'une température dans la
    * liste temperatureListBox. Met à jour la valeur dans la decimalBox
    * @param event Select 
    */
   public void onSelect$temperatureListBox(final Event event){

      if(selectedTemperature != null){
         tempMoyenneBox.setValue(new BigDecimal(selectedTemperature.getTemperature()));
      }
   }

   public HashMap<TKStockableObject, Emplacement> getOldEmplacements(){
      return oldEmplacements;
   }

   public void setOldEmplacements(final HashMap<TKStockableObject, Emplacement> o){
      this.oldEmplacements = o;
      dateRetourOblig = (dateRetourOblig || o != null);
   }

   //	public Set<Listitem> getSelectedObjectsItem() {
   //		return selectedObjectsItem;
   //	}
   //
   //	public void setSelectedObjectsItem(Set<Listitem> s) {
   //		this.selectedObjectsItem = s;
   //	}

   public boolean isInsertOk(){
      return insertOk;
   }

   public void setInsertOk(final boolean ok){
      this.insertOk = ok;
   }

   public String getOldEmplacementAdrl(){
      return oldEmplacementAdrl;
   }

   public void setOldEmplacementAdrl(final String a){
      this.oldEmplacementAdrl = a;
   }

   public String getImpactFormatted(){
      if((retour != null && retour.getImpact() != null && retour.getImpact()) || impactBox.isVisible()){
         return Labels.getLabel("Champ.Retour.Impact");
      }
      return Labels.getLabel("Champ.Retour.Impact.neg");
   }

   public String getImpactClass(){
      if(retour != null && retour.getImpact() != null && retour.getImpact()){
         if(!impactBox.isVisible()){
            return "requiredMark";
         }
      }else if(!impactBox.isVisible()){
         return "formArchiveValue";
      }
      return "formValueItalics";
   }

   public Boolean getDotVisible(){
      return retour != null && retour.getImpact() != null && retour.getImpact() && !impactBox.isVisible();
   }
}
