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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstEmail;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche d'un collaborateur.
 * Controller créé le 17/12/2009.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FicheCollaborateur extends AbstractFicheCombineController
{

   private final Logger log = LoggerFactory.getLogger(FicheCollaborateur.class);

   private static final long serialVersionUID = -5288212654515739964L;

   private Button addCoordonnee;

   private Menubar menuBar;

   // Labels
   private Label titreLabel;

   private Label nomLabel;

   private Label prenomLabel;

   private Label specialiteLabel;

   private Label etablissementLabel;

   private Grid servicesList;

   private Group groupServicesCollab;

   private Group groupCoordonneesCollab;

   private Grid coordonneesGrid;

   private Label archiveLabel;

   // Editable components : mode d'édition ou de création.
   private Label nomRequired;

   private Textbox nomBox;

   private Textbox prenomBox;

   private Listbox etabsBox;

   private Listbox titresBox;

   private Listbox specialitesBox;

   private Grid servicesListEdit;

   private Row rowServicesTitle;

   private Row rowAddService;

   private Combobox addServiceBox;

   private Grid coordonneesGridEdit;

   private Textbox adresseBox;

   private Textbox cpBox;

   private Textbox villeBox;

   private Textbox paysBox;

   private Textbox telBox;

   private Textbox faxBox;

   private Textbox mailBox;

   private Checkbox archiveBox;

   // Objets Principaux.
   private Collaborateur collaborateur;

   private final List<String> servicesAndEtabs = new ArrayList<>();

   private Service service;

   // Associations.
   private Titre selectedTitre;

   private Specialite selectedSpecialite;

   private Etablissement selectedEtablissement;

   private List<Titre> titres = new ArrayList<>();

   private List<Specialite> specialites = new ArrayList<>();

   private List<Etablissement> etablissements = new ArrayList<>();

   private final List<Service> services = new ArrayList<>();

   private final List<Coordonnee> coordonnees = new ArrayList<>();

   private List<Coordonnee> coordonneesToDelete = new ArrayList<>();

   private final List<Coordonnee> copyCoords = new ArrayList<>();

   private List<Service> allServices = new ArrayList<>();

   // Variables formulaire.
   private String serviceNom;

   private boolean createMode = false;

   private String servicesGroupHeader = Labels.getLabel("collaborateur.services");

   private String coordonneesGroupHeader = Labels.getLabel("coordonnee.group.infos");

   private String mode;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.collaborateur");
      setCascadable(false);
      setDeletable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.titreLabel, this.nomLabel, this.prenomLabel, this.specialiteLabel,
         this.etablissementLabel, this.servicesList, this.coordonneesGrid, this.archiveLabel, this.menuBar});

      setObjBoxsComponents(new Component[] {this.nomBox, this.prenomBox, this.etabsBox, this.titresBox, this.specialitesBox,
         this.servicesListEdit, this.rowAddService, this.rowServicesTitle, this.coordonneesGridEdit, this.archiveBox});

      setRequiredMarks(new Component[] {this.nomRequired});
      this.groupServicesCollab.setOpen(false);

      editC.setVisible(false);
      deleteC.setVisible(false);
      mode = "modification";

      drawActionsForCollaborateur();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.collaborateur = (Collaborateur) obj;

      services.clear();
      coordonnees.clear();

      if(collaborateur.getCollaborateurId() != null){
         services.addAll(ManagerLocator.getCollaborateurManager().getServicesManager(collaborateur));
         coordonnees.addAll(ManagerLocator.getCollaborateurManager().getCoordonneesManager(collaborateur));
      }

      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("collaborateur.services"));
      sb.append(" (");
      sb.append(services.size());
      sb.append(")");
      servicesGroupHeader = sb.toString();

      sb = new StringBuffer();
      sb.append(Labels.getLabel("coordonnee.group.infos"));
      sb.append(" (");
      sb.append(coordonnees.size());
      sb.append(")");
      coordonneesGroupHeader = sb.toString();

      super.setObject(collaborateur);
   }

   @Override
   public void cloneObject(){
      setClone(this.collaborateur.clone());
      copyCoords.clear();
      for(int i = 0; i < coordonnees.size(); i++){
         copyCoords.add(coordonnees.get(i).clone());
      }
   }

   @Override
   public void revertObject(){
      super.revertObject();
      setCoordonnees(getCopyCoords());
   }

   public void setCoordonnees(final List<Coordonnee> coords){
      this.coordonnees.clear();
      this.coordonnees.addAll(coords);
   }

   @Override
   public Collaborateur getObject(){
      return this.collaborateur;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public CollaborationsController getObjectTabController(){
      return (CollaborationsController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Collaborateur());
      super.setNewObject();
   }

   public void switchToCreateMode(final Service serv){
      this.service = serv;
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterNew", self, null);
   }

   public void onLaterNew(){
      switchToCreateMode();
      // ferme wait message
      Clients.clearBusy();
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      createMode = true;

      services.clear();
      coordonnees.clear();
      coordonneesToDelete.clear();

      servicesGroupHeader = Labels.getLabel("collaborateur.services");
      coordonneesGroupHeader = Labels.getLabel("coordonnee.group.infos");

      if(service != null){
         services.add(service);
         this.collaborateur.setEtablissement(service.getEtablissement());

         final StringBuffer sb = new StringBuffer();
         sb.append(Labels.getLabel("collaborateur.services"));
         sb.append(" (");
         sb.append(services.size());
         sb.append(")");
         servicesGroupHeader = sb.toString();
         this.groupServicesCollab.setLabel(servicesGroupHeader);
      }

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      final Coordonnee newCoord = new Coordonnee();
      if(selectedEtablissement != null && selectedEtablissement.getCoordonnee() != null){
         // on crée une coordonnée par défaut avec celle de l'établissement
         newCoord.setAdresse(selectedEtablissement.getCoordonnee().getAdresse());
         newCoord.setCp(selectedEtablissement.getCoordonnee().getCp());
         newCoord.setVille(selectedEtablissement.getCoordonnee().getVille());
         newCoord.setPays(selectedEtablissement.getCoordonnee().getPays());
         newCoord.setTel(selectedEtablissement.getCoordonnee().getTel());
         newCoord.setFax(selectedEtablissement.getCoordonnee().getFax());
         newCoord.setMail(selectedEtablissement.getCoordonnee().getMail());
      }
      coordonnees.add(newCoord);
      // maj de la liste des coordonnées
      final ListModel<Coordonnee> list = new ListModelList<>(coordonnees);
      coordonneesGridEdit.setModel(list);
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("coordonnee.group.infos"));
      sb.append(" (");
      sb.append(coordonnees.size());
      sb.append(")");
      coordonneesGroupHeader = sb.toString();
      this.groupCoordonneesCollab.setLabel(coordonneesGroupHeader);

      addCoordonnee.setVisible(true);
      this.groupServicesCollab.setOpen(true);

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode statique.
    * Modifie le membre modeFiche.
    */
   public void switchToStaticMode(final String modeFiche){
      this.mode = modeFiche;
      switchToStaticMode();

      if(this.collaborateur.equals(new Collaborateur())){
         menuBar.setVisible(false);
      }
   }

   @Override
   public void switchToStaticMode(){

      createMode = false;

      super.switchToStaticMode(this.collaborateur.equals(new Collaborateur()));

      if(this.mode.equals("modification")){
         switchToModificationMode(this.collaborateur.equals(new Collaborateur()));
      }else if(this.mode.equals("select")){
         switchToSelectMode();
      }else if(this.mode.equals("details")){
         switchToDetailMode();
      }

      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("collaborateur.services"));
      sb.append(" (");
      sb.append(services.size());
      sb.append(")");
      servicesGroupHeader = sb.toString();
      this.groupServicesCollab.setLabel(servicesGroupHeader);
      this.groupServicesCollab.setOpen(false);

      sb = new StringBuffer();
      sb.append(Labels.getLabel("coordonnee.group.infos"));
      sb.append(" (");
      sb.append(coordonnees.size());
      sb.append(")");
      coordonneesGroupHeader = sb.toString();
      this.groupCoordonneesCollab.setLabel(coordonneesGroupHeader);

      addCoordonnee.setVisible(false);

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   @Override
   public void switchToEditMode(){
      Clients.showBusy(null);
      Events.echoEvent("onLaterSwitch", self, null);
   }

   public void onLaterSwitch(){
      createMode = false;

      super.switchToEditMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();
      coordonneesToDelete = new ArrayList<>();

      addCoordonnee.setVisible(true);
      this.groupServicesCollab.setOpen(true);

      getBinder().loadComponent(self);

      Clients.clearBusy();
   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   public void switchToModificationMode(final boolean isNew){
      addNewC.setVisible(true);
      editC.setVisible(!isNew);
      deleteC.setVisible(!isNew);
   }

   public void switchToSelectMode(){
      addNewC.setVisible(true);
      editC.setVisible(false);
      deleteC.setVisible(false);
   }

   public void switchToDetailMode(){
      addNewC.setVisible(false);
      editC.setVisible(false);
      deleteC.setVisible(false);
   }

   @Override
   public void clearData(){
      this.collaborateur = new Collaborateur();
      services.clear();
      coordonnees.clear();
      copyCoords.clear();
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){
      setEmptyToNulls();
      setFieldsToUpperCase();
      this.collaborateur.setArchive(archiveBox.isChecked());

      ManagerLocator.getCollaborateurManager().createObjectManager(collaborateur, selectedTitre, selectedEtablissement,
         selectedSpecialite, services, coordonnees, SessionUtils.getLoggedUser(sessionScope));

      if(getListeCollaborations() != null){
         getListeCollaborations().updateTree();
         getListeCollaborations().updateCollabsWithoutService();
         getListeCollaborations().openCollaborateurInTreeOrList(this.collaborateur, false, true);
      }
   }

   @Override
   public void updateObject(){

      setEmptyToNulls();
      setFieldsToUpperCase();
      this.collaborateur.setArchive(archiveBox.isChecked());

      ManagerLocator.getCollaborateurManager().updateObjectManager(collaborateur, selectedTitre, selectedEtablissement,
         selectedSpecialite, services, coordonnees, SessionUtils.getLoggedUser(sessionScope), true);

      // si aucune erreur ne s'est produite lors de la sauvegarde
      for(int i = 0; i < coordonneesToDelete.size(); i++){
         final Coordonnee coord = coordonneesToDelete.get(i);

         if(!ManagerLocator.getCoordonneeManager().isUsedByOtherObjectManager(coord, collaborateur)){
            ManagerLocator.getCoordonneeManager().removeObjectManager(coord);
         }
      }

      if(getListeCollaborations() != null){
         getListeCollaborations().updateTree();
         getListeCollaborations().openCollaborateurInTreeOrList(this.collaborateur, false, true);
         getListeCollaborations().updateCollabsWithoutService();
      }
   }

   @Override
   public boolean prepareDeleteObject(){
      final boolean isReferenced = ManagerLocator.getCollaborateurManager().isReferencedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      setDeletable(true);
      if(isReferenced){
         setDeleteMessage(Labels.getLabel("collaborateur.deletion.isReferencedCascade"));
         if(getObject().getArchive()){ // deja archivé
            setDeletable(false);
            setDeleteMessage(Labels.getLabel("collaborateur.deletion.isAlreadyArchived"));
         }
      }
      setFantomable(!isReferenced);

      return isReferenced;
   }

   @Override
   public void removeObject(final String comments){
      if(isFantomable()){ // suppression possible
         ManagerLocator.getCollaborateurManager().removeObjectManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope));
      }else{ // archivage
         getObject().setArchive(true);
         ManagerLocator.getCollaborateurManager().updateObjectManager(getObject(), getObject().getTitre(),
            getObject().getEtablissement(), getObject().getSpecialite(), null, null, SessionUtils.getLoggedUser(sessionScope),
            false);
      }
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         removeObject((String) event.getData());

         if(getListeCollaborations() != null){
            getListeCollaborations().updateTree();
            getListeCollaborations().updateCollabsWithoutService();
         }

         if(isFantomable()){
            // clear form
            clearData();
         }else{
            if(getListeCollaborations() != null){
               getListeCollaborations().openCollaborateurInTreeOrList(getObject(), false, true);
            }
         }

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeCollaborations
    */
   private ListeCollaborations getListeCollaborations(){
      return getObjectTabController().getListeCollaborations();
   }

   @Override
   public void onClick$addNewC(){
      this.switchToCreateMode(null);
   }

   @Override
   public void onLaterCancel(){
      clearData();
      Clients.clearBusy();
   }

   @Override
   public void onClick$createC(){
      Clients.showBusy(Labels.getLabel("ficheCollaborateur.modification.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      clearConstraints();
      try{
         createNewObject();
         cloneObject();
         switchToStaticMode();
         disableToolBar(false);
      }catch(final RuntimeException re){
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
      Clients.clearBusy();
   }

   @Override
   public void onClick$editC(){
      if(this.collaborateur != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){

      super.onClick$validateC();
      clearConstraints();
   }

   /**
    * Cette méthode va supprimer un service de la liste.
    * @param event Clic sur une image deleteService.
    */
   public void onClick$deleteService(final Event event){
      // on récupère le service que l'utilisateur veut
      // suppimer
      final Service serv = (Service) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on enlève le service de la liste et on la met à jour
      services.remove(serv);
      final ListModel<Service> list = new ListModelList<>(services);
      servicesListEdit.setModel(list);

   }

   /**
    * Méthode appelée lors du clic sur le bouton addCoordonnee. Elle
    * va créer une nouvelle coordonnée et l'ajouter à la liste.
    */
   public void onClick$addCoordonnee(){
      final Coordonnee newCoord = new Coordonnee();

      coordonnees.add(newCoord);

      // maj de la liste des coordonnées
      final ListModel<Coordonnee> list = new ListModelList<>(coordonnees);
      coordonneesGridEdit.setModel(list);

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("coordonnee.group.infos"));
      sb.append(" (");
      sb.append(coordonnees.size());
      sb.append(")");
      coordonneesGroupHeader = sb.toString();
      this.groupCoordonneesCollab.setLabel(coordonneesGroupHeader);
   }

   /**
    * Cette méthode va supprimer une coordonnée de la liste.
    * @param event Clic sur une image deleteCoordonnee.
    */
   public void onClick$deleteCoordonnee(final Event event){
      // on demande confirmation à l'utilisateur
      // de supprimer la coordonnée
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message",
            new String[] {Labels.getLabel("message.deletion.coordonnee")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
         // on récupère la coordonnée que l'utilisateur veut
         // suppimer
         final Coordonnee coord = (Coordonnee) AbstractListeController2.getBindingData((ForwardEvent) event, false);
         // on enlève la coord de la liste et on la met à jour
         coordonnees.remove(coord);
         final ListModel<Coordonnee> list = new ListModelList<>(coordonnees);
         coordonneesGridEdit.setModel(list);

         // si la coord existait dans la BDD on l'ajoute à la
         // liste des coords à supprimer (elle ne sera délété que
         // lors de la sauvegarde finale)
         if(coord.getCoordonneeId() != null){
            coordonneesToDelete.add(coord);
         }
      }
   }

   /**
    * Cette méthode ajoute un service à la liste lors du
    * clic sur le bouton addService.
    */
   public void onClick$addService(){
      final String selectedServiceNom = this.addServiceBox.getValue().toUpperCase();
      this.addServiceBox.setValue(selectedServiceNom);

      final int ind = servicesAndEtabs.indexOf(selectedServiceNom);

      if(ind > -1){
         final Service serv = allServices.get(ind);

         if(!services.contains(serv)){
            services.add(serv);
            final ListModel<Service> list = new ListModelList<>(services);
            servicesListEdit.setModel(list);

            final StringBuffer sb = new StringBuffer();
            sb.append(Labels.getLabel("collaborateur.services"));
            sb.append(" (");
            sb.append(services.size());
            sb.append(")");
            servicesGroupHeader = sb.toString();
            this.groupServicesCollab.setLabel(servicesGroupHeader);

            if(selectedEtablissement == null){
               selectedEtablissement = serv.getEtablissement();
            }
         }
         addServiceBox.setValue(null);
      }else{
         throw new WrongValueException(addServiceBox, Labels.getLabel("collaborateur.error.ajout.service"));
      }
   }

   /**
    * Cette méthode descend la barre de scroll au niveau du groupe
    * groupCoordonneesCollab.
    */
   public void onOpen$groupCoordonneesCollab(){
      final String id = groupCoordonneesCollab.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Cette méthode descend la barre de scroll au niveau du groupe
    * groupServicesCollab.
    */
   public void onOpen$groupServicesCollab(){
      final String id = groupServicesCollab.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBox(){
      nomBox.setValue(nomBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * prenomBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$prenomBox(){
      prenomBox.setValue(prenomBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * villeBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$villeBox(final Event event){
      final Coordonnee coord = (Coordonnee) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      coord.setVille(coord.getVille().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * paysBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$paysBox(final Event event){
      final Coordonnee coord = (Coordonnee) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      coord.setPays(coord.getPays().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.collaborateur.getNom() != null){
         this.collaborateur.setNom(this.collaborateur.getNom().toUpperCase().trim());
      }

      if(this.collaborateur.getPrenom() != null){
         this.collaborateur.setPrenom(this.collaborateur.getPrenom().toUpperCase().trim());
      }

      for(int i = 0; i < coordonnees.size(); i++){
         if(coordonnees.get(i).getVille() != null){
            coordonnees.get(i).setVille(coordonnees.get(i).getVille().toUpperCase().trim());
         }
         if(coordonnees.get(i).getPays() != null){
            coordonnees.get(i).setPays(coordonnees.get(i).getPays().toUpperCase().trim());
         }
      }

   }

   /**
    * Méthode appelée lors de la sélection d'un établissement : en mode de
    * création, la coordonnée du service sera alors pré-remplie.
    */
   public void onSelect$etabsBox(){
      if(createMode && coordonnees.size() > 0){
         final Coordonnee coord = coordonnees.get(0);
         if(selectedEtablissement != null && selectedEtablissement.getCoordonnee() != null){
            coord.setAdresse(selectedEtablissement.getCoordonnee().getAdresse());
            coord.setCp(selectedEtablissement.getCoordonnee().getCp());
            coord.setVille(selectedEtablissement.getCoordonnee().getVille());
            coord.setPays(selectedEtablissement.getCoordonnee().getPays());
            coord.setTel(selectedEtablissement.getCoordonnee().getTel());
            coord.setFax(selectedEtablissement.getCoordonnee().getFax());
            coord.setMail(selectedEtablissement.getCoordonnee().getMail());
         }else{
            coord.setAdresse(null);
            coord.setCp(null);
            coord.setVille(null);
            coord.setPays(null);
            coord.setTel(null);
            coord.setFax(null);
            coord.setMail(null);
         }
         // maj de la liste des coordonnées
         final ListModel<Coordonnee> list = new ListModelList<>(coordonnees);
         coordonneesGridEdit.setModel(list);
      }
   }

   @Override
   public void setEmptyToNulls(){

      if(this.collaborateur.getPrenom().equals("")){
         this.collaborateur.setPrenom(null);
      }
      final List<Coordonnee> coords = new ArrayList<>();
      coords.addAll(coordonnees);
      for(int i = 0; i < coords.size(); i++){
         final Coordonnee coordonnee = coords.get(i);

         if(coordonnee.getAdresse().equals("")){
            coordonnee.setAdresse(null);
         }
         if(coordonnee.getCp().equals("")){
            coordonnee.setCp(null);
         }
         if(coordonnee.getVille().equals("")){
            coordonnee.setVille(null);
         }
         if(coordonnee.getPays().equals("")){
            coordonnee.setPays(null);
         }
         if(coordonnee.getTel().equals("")){
            coordonnee.setTel(null);
         }
         if(coordonnee.getFax().equals("")){
            coordonnee.setFax(null);
         }
         if(coordonnee.getMail().equals("")){
            coordonnee.setMail(null);
         }

         // supprime null coor
         if(coordonnee.getAdresse() == null && coordonnee.getCp() == null && coordonnee.getVille() == null
            && coordonnee.getPays() == null && coordonnee.getTel() == null && coordonnee.getFax() == null
            && coordonnee.getMail() == null){
            coordonnees.remove(coordonnee);
         }
      }
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){

      etablissements = ManagerLocator.getEtablissementManager().findAllObjectsWithOrderManager();
      etablissements.add(0, null);
      selectedEtablissement = this.collaborateur.getEtablissement();

      titres = ManagerLocator.getTitreManager().findAllObjectsManager();
      titres.add(0, null);
      selectedTitre = this.collaborateur.getTitre();

      specialites = ManagerLocator.getSpecialiteManager().findAllObjectsManager();
      specialites.add(0, null);
      selectedSpecialite = this.collaborateur.getSpecialite();

      servicesAndEtabs.clear();
      allServices = ManagerLocator.getServiceManager().findAllObjectsWithOrderManager();
      for(int i = 0; i < allServices.size(); i++){
         final Service tmp = allServices.get(i);
         final StringBuffer sb = new StringBuffer();
         sb.append(tmp.getNom());
         sb.append(" {");
         sb.append(tmp.getEtablissement().getNom());
         sb.append("}");
         servicesAndEtabs.add(sb.toString());
      }
      addServiceBox.setModel(new CustomSimpleListModel(servicesAndEtabs));

      if(this.collaborateur.getArchive() != null){
         archiveBox.setChecked(this.collaborateur.getArchive());
      }else{
         archiveBox.setChecked(false);
      }

   }
   //
   //	public void revertCoordonnees() {
   //		coordonnees = new ArrayList<Coordonnee>();
   //
   //		for (int i = 0; i < copyCoords.size(); i++) {
   //			Coordonnee coordonnee = new Coordonnee();
   //			Coordonnee copyCoord = copyCoords.get(i);
   //
   //			coordonnee.setCoordonneeId(copyCoord.getCoordonneeId());
   //			coordonnee.setAdresse(copyCoord.getAdresse());
   //			coordonnee.setCp(copyCoord.getCp());
   //			coordonnee.setVille(copyCoord.getVille());
   //			coordonnee.setPays(copyCoord.getPays());
   //			coordonnee.setTel(copyCoord.getTel());
   //			coordonnee.setFax(copyCoord.getFax());
   //			coordonnee.setMail(copyCoord.getMail());
   //			coordonnee.setCollaborateurs(copyCoord.getCollaborateurs());
   //
   //			coordonnees.add(coordonnee);
   //		}
   //	}

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(prenomBox);
      Clients.clearWrongValue(adresseBox);
      Clients.clearWrongValue(cpBox);
      Clients.clearWrongValue(villeBox);
      Clients.clearWrongValue(paysBox);
      Clients.clearWrongValue(telBox);
      Clients.clearWrongValue(faxBox);
      Clients.clearWrongValue(mailBox);
      addServiceBox.setValue(null);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForCollaborateur(){
      drawActionsButtons("Collaborateur");
   }

   /**
    * Méthode pour l'impression de la fiche d'un collaborateur.
    */
   public void onClick$print(){
      if(this.collaborateur != null){
         // création du document XML contenant les données à imprimer
         final Document document = ManagerLocator.getXmlUtils().createJDomDocument();
         final Element root = document.getRootElement();
         final Element pageXML = ManagerLocator.getXmlUtils().addPage(root, null);
         ManagerLocator.getXmlUtils().addHautDePage(root, Labels.getLabel("ficheCollaborateur.panel.title"), false, null);
         ManagerLocator.getXmlUtils().addBasDePage(root, "");

         // ajout des infos principales
         addInfosGeneralesToPrint(pageXML);
         // ajout de toutes les coordonnées
         for(int i = 0; i < coordonnees.size(); i++){
            final StringBuffer sb = new StringBuffer(Labels.getLabel("coordonnee.group.infos"));
            sb.append(" n°");
            sb.append(String.valueOf(i + 1));
            addCoordonneesToPrint(pageXML, coordonnees.get(i), sb.toString());
         }
         // ajout des services
         addServicesToPrint(pageXML);

         // Transformation du document en fichier
         byte[] dl = null;
         try{
            dl = ManagerLocator.getXmlUtils().creerPdf(document);
         }catch(final Exception e){
            log.error("An error occurred: {}", e.toString()); 
         }

         // envoie du fichier à imprimer à l'utilisateur
         if(dl != null){
            Filedownload.save(dl, "application/pdf", "collaborateur.pdf");
            dl = null;
         }
      }
   }

   /**
    * Ajout les infos générales à imprimer.
    * @param page
    */
   public void addInfosGeneralesToPrint(final Element page){
      String tmp = "";
      final CoupleValeur cpVide = new CoupleValeur("", "");
      // Etablissement
      if(collaborateur.getEtablissement() != null){
         tmp = collaborateur.getEtablissement().getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("Champ.Collaborateur.Etalissement"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cpVide});
      // Titre
      if(collaborateur.getTitre() != null){
         tmp = collaborateur.getTitre().getTitre();
      }else{
         tmp = "";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("Champ.Collaborateur.Titre"), tmp);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp2, cpVide});
      // Nom
      if(collaborateur.getNom() != null){
         tmp = collaborateur.getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("Champ.Collaborateur.Nom"), tmp);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp3, cpVide});
      // Prénom
      if(collaborateur.getPrenom() != null){
         tmp = collaborateur.getPrenom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("Champ.Collaborateur.Prenom"), tmp);
      final LigneParagraphe li4 = new LigneParagraphe("", new CoupleValeur[] {cp4, cpVide});
      // Spécialité
      if(collaborateur.getSpecialite() != null){
         tmp = collaborateur.getSpecialite().getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("Champ.Collaborateur.Specialite"), tmp);
      final LigneParagraphe li5 = new LigneParagraphe("", new CoupleValeur[] {cp5, cpVide});
      // Inactif
      if(collaborateur.getArchive() != null){
         tmp = ObjectTypesFormatters.booleanLitteralFormatter(this.collaborateur.getArchive());
      }else{
         tmp = "";
      }
      final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("collaborateur.archive"), tmp);
      final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp6, cpVide});
      final Paragraphe par1 = new Paragraphe(null, new LigneParagraphe[] {li1, li2, li3, li4, li5, li6}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les coordonnées à imprimer.
    * @param page
    */
   public void addCoordonneesToPrint(final Element page, final Coordonnee coordonnee, final String titre){

      if(coordonnee != null){
         String tmp = "";
         final CoupleValeur cpVide = new CoupleValeur("", "");
         // Coordonnées adresse
         if(coordonnee.getAdresse() != null){
            tmp = coordonnee.getAdresse();
         }else{
            tmp = "";
         }
         final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("coordonnee.adresse"), tmp);
         final LigneParagraphe li5 = new LigneParagraphe("", new CoupleValeur[] {cp5, cpVide});
         // Coordonnées code postal
         if(coordonnee.getCp() != null){
            tmp = coordonnee.getCp();
         }else{
            tmp = "";
         }
         final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("coordonnee.cp"), tmp);
         final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp6, cpVide});
         // Coordonnées ville
         if(coordonnee.getVille() != null){
            tmp = coordonnee.getVille();
         }else{
            tmp = "";
         }
         final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("coordonnee.ville"), tmp);
         final LigneParagraphe li7 = new LigneParagraphe("", new CoupleValeur[] {cp7, cpVide});
         // Coordonnées pays
         if(coordonnee.getPays() != null){
            tmp = coordonnee.getPays();
         }else{
            tmp = "";
         }
         final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("coordonnee.pays"), tmp);
         final LigneParagraphe li8 = new LigneParagraphe("", new CoupleValeur[] {cp8, cpVide});
         // Coordonnées tel
         if(coordonnee.getTel() != null){
            tmp = coordonnee.getTel();
         }else{
            tmp = "";
         }
         final CoupleValeur cp9 = new CoupleValeur(Labels.getLabel("coordonnee.telephone"), tmp);
         final LigneParagraphe li9 = new LigneParagraphe("", new CoupleValeur[] {cp9, cpVide});
         // Coordonnées fax
         if(coordonnee.getFax() != null){
            tmp = coordonnee.getFax();
         }else{
            tmp = "";
         }
         final CoupleValeur cp10 = new CoupleValeur(Labels.getLabel("coordonnee.fax"), tmp);
         final LigneParagraphe li10 = new LigneParagraphe("", new CoupleValeur[] {cp10, cpVide});
         // Coordonnées mail
         if(coordonnee.getMail() != null){
            tmp = coordonnee.getMail();
         }else{
            tmp = "";
         }
         final CoupleValeur cp11 = new CoupleValeur(Labels.getLabel("coordonnee.mail"), tmp);
         final LigneParagraphe li11 = new LigneParagraphe("", new CoupleValeur[] {cp11, cpVide});
         final Paragraphe par2 =
            new Paragraphe(titre, new LigneParagraphe[] {li5, li6, li7, li8, li9, li10, li11}, null, null, null);
         ManagerLocator.getXmlUtils().addParagraphe(page, par2);
      }
   }

   /**
    * Ajout les services à imprimer.
    * @param page
    */
   public void addServicesToPrint(final Element page){
      // Services
      final EnteteListe entetes =
         new EnteteListe(new String[] {Labels.getLabel("service.nom"), Labels.getLabel("service.etablissement")});
      final LigneListe[] liste = new LigneListe[services.size()];
      for(int i = 0; i < services.size(); i++){
         final String[] valeurs = new String[2];
         if(services.get(i).getNom() != null){
            valeurs[0] = services.get(i).getNom();
         }else{
            valeurs[0] = "-";
         }
         if(services.get(i).getEtablissement() != null && services.get(i).getEtablissement().getNom() != null){
            valeurs[1] = services.get(i).getEtablissement().getNom();
         }else{
            valeurs[1] = "-";
         }
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      final ListeElement listeSites = new ListeElement(null, entetes, liste);
      // ajout du paragraphe
      final StringBuffer titre = new StringBuffer();
      titre.append(Labels.getLabel("collaborateur.services"));
      titre.append(" (");
      titre.append(services.size());
      titre.append(")");
      final Paragraphe par = new Paragraphe(titre.toString(), null, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   public void onClick$serviceNom(final Event event){
      final Service serv = (Service) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(getListeCollaborations() != null){
         getListeCollaborations().openService(serv, true, true);
      }
   }

   /*************************************************/
   /**               ACCESSEURS                    **/
   /*************************************************/

   public Collaborateur getCollaborateur(){
      return collaborateur;
   }

   public void setCollaborateur(final Collaborateur c){
      setObject(c);
   }

   public Titre getSelectedTitre(){
      return selectedTitre;
   }

   public void setSelectedTitre(final Titre selected){
      this.selectedTitre = selected;
   }

   public Specialite getSelectedSpecialite(){
      return selectedSpecialite;
   }

   public void setSelectedSpecialite(final Specialite selected){
      this.selectedSpecialite = selected;
   }

   public List<Titre> getTitres(){
      return titres;
   }

   public List<Specialite> getSpecialites(){
      return specialites;
   }

   public Etablissement getSelectedEtablissement(){
      return selectedEtablissement;
   }

   public void setSelectedEtablissement(final Etablissement selected){
      this.selectedEtablissement = selected;
   }

   public List<Etablissement> getEtablissements(){
      return etablissements;
   }

   public List<Service> getServices(){
      return services;
   }

   public List<Coordonnee> getCoordonnees(){
      return coordonnees;
   }

   public String getServicesGroupHeader(){
      return servicesGroupHeader;
   }

   public String getCoordonneesGroupHeader(){
      return coordonneesGroupHeader;
   }

   public List<Coordonnee> getCopyCoords(){
      return copyCoords;
   }

   public List<String> getServicesAndEtabs(){
      return servicesAndEtabs;
   }

   public List<Service> getAllServices(){
      return allServices;
   }

   public List<Coordonnee> getCoordonneesToDelete(){
      return coordonneesToDelete;
   }

   public String getMode(){
      return mode;
   }

   public void setMode(final String m){
      this.mode = m;
   }

   /**
    * Formate la valeur du champ Archive.
    * @return Oui ou non.
    */
   public String getArchiveFormated(){

      if(this.collaborateur != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.collaborateur.getArchive());
      }
      return "";
   }

   public String getServiceNom(){
      return serviceNom;
   }

   public void setServiceNom(final String serviceN){
      this.serviceNom = serviceN;
   }

   public boolean isCreateMode(){
      return createMode;
   }

   public void setCreateMode(final boolean createM){
      this.createMode = createM;
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/
   public ConstWord getNomCollabConstraint(){
      return ContexteConstraints.getNomCollabConstraint();
   }

   public ConstWord getPrenomCollabConstraint(){
      return ContexteConstraints.getPrenomCollabConstraint();
   }

   public ConstText getAddrConstraint(){
      return ContexteConstraints.getAddrConstraint();
   }

   public ConstWord getVillePaysConstraint(){
      return ContexteConstraints.getVillePaysConstraint();
   }

   public ConstCode getCpConstraint(){
      return ContexteConstraints.getCpConstraint();
   }

   public ConstCode getTelFaxConstraint(){
      return ContexteConstraints.getTefFaxConstraint();
   }

   public ConstEmail getEmailConstraint(){
      return ContexteConstraints.getEmailConstraint();
   }

   @Override
   public String getDeleteWaitLabel(){
      if(isFantomable()){
         return Labels.getLabel("deletion.general.wait");
      }
      return Labels.getLabel("archivage.general.wait");
   }
}