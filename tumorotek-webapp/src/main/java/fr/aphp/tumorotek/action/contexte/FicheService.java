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
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
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
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche d'un service.
 * Controller créé le 17/12/2009.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FicheService extends AbstractFicheCombineController
{

   private final Logger log = LoggerFactory.getLogger(FicheService.class);

   private static final long serialVersionUID = -5842411632241759671L;

   private Group groupCollabsActifsService;

   private Group groupCollabsInactifsService;

   private Button newCollaborateur;

   private Menubar menuBar;

   // Labels
   private Label nomLabel;

   private Label etablissementLabel;

   private Label adresseLabel;

   private Label cpLabel;

   private Label villeLabel;

   private Label paysLabel;

   private Label telLabel;

   private Label faxLabel;

   private Label mailLabel;

   private Grid collabsActifsList;

   private Grid collabsInactifsList;

   private Label archiveLabel;

   // Editable components : mode d'édition ou de création.
   private Label etabRequired;

   private Label nomRequired;

   private Textbox nomBox;

   private Textbox adresseBox;

   private Textbox cpBox;

   private Textbox villeBox;

   private Textbox paysBox;

   private Textbox telBox;

   private Textbox faxBox;

   private Textbox mailBox;

   private Listbox etabsBox;

   private Grid collabsListEdit;

   private Row rowCollabsTitle;

   private Row rowAddCollab;

   private Combobox addCollabBox;

   private Checkbox archiveBox;

   private Group groupCollabsEditService;

   // Objets Principaux.
   private Service service;

   private Coordonnee coordonnee;

   private Coordonnee copyCoord;

   private Etablissement etablissement;

   // Associations.
   private List<Etablissement> etablissements = new ArrayList<>();

   private Etablissement selectedEtablissement;

   private List<Collaborateur> collaborateursActifs = new ArrayList<>();

   private List<Collaborateur> collaborateursInactifs = new ArrayList<>();

   private List<Collaborateur> collaborateursEdit = new ArrayList<>();

   private List<Collaborateur> allCollaborateurs = new ArrayList<>();

   private final List<String> nomsAndPrenoms = new ArrayList<>();

   // Variables formulaire.
   private String collabsActifsGroupHeader = Labels.getLabel("service.collaborateurs.actifs");

   private String collabsInactifsGroupHeader = Labels.getLabel("service.collaborateurs.inactifs");

   private String mode;

   private boolean createMode = false;

   private boolean cascadeArchive = false;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.service");
      setDeletable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(
         new Component[] {this.nomLabel, this.etablissementLabel, this.adresseLabel, this.cpLabel, this.villeLabel,
            this.paysLabel, this.telLabel, this.faxLabel, this.mailLabel, this.collabsActifsList, this.collabsInactifsList,
            this.archiveLabel, this.menuBar, this.groupCollabsActifsService, this.groupCollabsInactifsService});

      setObjBoxsComponents(new Component[] {this.nomBox, this.adresseBox, this.cpBox, this.villeBox, this.paysBox, this.telBox,
         this.faxBox, this.mailBox, this.etabsBox, this.collabsListEdit, this.rowCollabsTitle, this.rowAddCollab,
         this.addCollabBox, this.archiveBox, this.groupCollabsEditService});

      setRequiredMarks(new Component[] {this.etabRequired, this.nomRequired});

      groupCollabsActifsService.setOpen(false);
      groupCollabsInactifsService.setOpen(false);
      mode = "modification";

      drawActionsForService();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.service = (Service) obj;

      if(this.service.getCoordonnee() != null){
         this.coordonnee = this.service.getCoordonnee();
      }else{
         this.coordonnee = new Coordonnee();
      }

      collaborateursEdit.clear();
      collaborateursActifs.clear();
      collaborateursInactifs.clear();
      if(service.getServiceId() != null){
         collaborateursEdit.addAll(ManagerLocator.getServiceManager().getCollaborateursWithOrderManager(service));
         collaborateursActifs.addAll(ManagerLocator.getCollaborateurManager().findByServicesAndArchiveManager(service, false));
         collaborateursInactifs.addAll(ManagerLocator.getCollaborateurManager().findByServicesAndArchiveManager(service, true));
      }

      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("service.collaborateurs.actifs"));
      sb.append(" (");
      sb.append(collaborateursActifs.size());
      sb.append(")");
      collabsActifsGroupHeader = sb.toString();

      sb = new StringBuffer();
      sb.append(Labels.getLabel("service.collaborateurs.inactifs"));
      sb.append(" (");
      sb.append(collaborateursInactifs.size());
      sb.append(")");
      collabsInactifsGroupHeader = sb.toString();

      super.setObject(service);
   }

   @Override
   public void cloneObject(){
      setClone(this.service.clone());
      setCopyCoord(coordonnee.clone());
   }

   @Override
   public Service getObject(){
      return this.service;
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
      setObject(new Service());
      this.coordonnee = new Coordonnee();
      super.setNewObject();
   }

   /* Methode comportementales */
   /**
    * Change le mode de la fiche en creation.
    * @param parent Objet dont est issu l'échantillon.
    */
   public void switchToCreateMode(final Etablissement etab){
      this.etablissement = etab;
      switchToCreateMode();
   }

   @Override
   public void switchToCreateMode(){
      createMode = true;

      super.switchToCreateMode();

      collaborateursEdit = new ArrayList<>();
      collabsActifsGroupHeader = Labels.getLabel("service.collaborateurs.actifs");
      collabsInactifsGroupHeader = Labels.getLabel("service.collaborateurs.inactifs");

      if(etablissement != null){
         this.service.setEtablissement(etablissement);
      }

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      if(null != selectedEtablissement && selectedEtablissement.getCoordonnee() != null){
         this.coordonnee.setAdresse(selectedEtablissement.getCoordonnee().getAdresse());
         this.coordonnee.setCp(selectedEtablissement.getCoordonnee().getCp());
         this.coordonnee.setVille(selectedEtablissement.getCoordonnee().getVille());
         this.coordonnee.setPays(selectedEtablissement.getCoordonnee().getPays());
         this.coordonnee.setTel(selectedEtablissement.getCoordonnee().getTel());
         this.coordonnee.setFax(selectedEtablissement.getCoordonnee().getFax());
         this.coordonnee.setMail(selectedEtablissement.getCoordonnee().getMail());
      }

      groupCollabsActifsService.setOpen(true);
      groupCollabsInactifsService.setOpen(true);
      newCollaborateur.setVisible(false);

      getBinder().loadAll();
   }

   /**
    * Change mode de la fiche en mode statique.
    * Modifie le membre modeFiche.
    */
   public void switchToStaticMode(final String modeFiche){
      this.mode = modeFiche;
      switchToStaticMode();
   }

   @Override
   public void switchToStaticMode(){
      createMode = false;

      super.switchToStaticMode(this.service.equals(new Service()));

      if(this.service.equals(new Service())){
         this.menuBar.setVisible(false);
      }

      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("service.collaborateurs.actifs"));
      sb.append(" (");
      sb.append(collaborateursActifs.size());
      sb.append(")");
      collabsActifsGroupHeader = sb.toString();
      groupCollabsActifsService.setOpen(false);
      groupCollabsActifsService.setLabel(collabsActifsGroupHeader);

      sb = new StringBuffer();
      sb.append(Labels.getLabel("service.collaborateurs.inactifs"));
      sb.append(" (");
      sb.append(collaborateursInactifs.size());
      sb.append(")");
      collabsInactifsGroupHeader = sb.toString();
      groupCollabsInactifsService.setOpen(false);
      groupCollabsInactifsService.setLabel(collabsInactifsGroupHeader);
      newCollaborateur.setVisible(true);

      if(this.mode.equals("modification")){
         switchToModificationMode(this.service.equals(new Service()));
      }else if(this.mode.equals("select")){
         switchToSelectMode(this.service.equals(new Service()));
      }else if(this.mode.equals("details")){
         switchToDetailMode();
      }

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   @Override
   public void switchToEditMode(){
      // Initialisation du mode (listes, valeurs...)
      createMode = false;

      super.switchToEditMode();

      initEditableMode();

      groupCollabsActifsService.setOpen(true);
      groupCollabsInactifsService.setOpen(true);
      newCollaborateur.setVisible(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   public void switchToModificationMode(final boolean isNew){
      addNewC.setVisible(true);
      editC.setVisible(!isNew);
      deleteC.setVisible(!isNew);
      newCollaborateur.setVisible(!isNew);
   }

   public void switchToSelectMode(final boolean isNew){
      addNewC.setVisible(true);
      editC.setVisible(false);
      deleteC.setVisible(false);
      newCollaborateur.setVisible(!isNew);
   }

   public void switchToDetailMode(){
      addNewC.setVisible(false);
      editC.setVisible(false);
      deleteC.setVisible(false);
      newCollaborateur.setVisible(false);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){

      setEmptyToNulls();
      setFieldsToUpperCase();

      this.service.setArchive(archiveBox.isChecked());

      ManagerLocator.getServiceManager().createObjectManager(service, coordonnee, selectedEtablissement, collaborateursEdit,
         SessionUtils.getLoggedUser(sessionScope), cascadeArchive);

      if(getListeCollaborations() != null){
         getListeCollaborations().updateTree();
         getListeCollaborations().openService(service, false, true);
         getListeCollaborations().updateCollabsWithoutService();
      }
   }

   @Override
   public void updateObject(){
      setEmptyToNulls();
      setFieldsToUpperCase();

      this.service.setArchive(archiveBox.isChecked());

      ManagerLocator.getServiceManager().updateObjectManager(service, coordonnee, selectedEtablissement, collaborateursEdit,
         SessionUtils.getLoggedUser(sessionScope), cascadeArchive, true);

      if(getListeCollaborations() != null){
         getListeCollaborations().updateTree();
         getListeCollaborations().openService(this.service, false, true);
         getListeCollaborations().updateCollabsWithoutService();
      }
   }

   @Override
   public void setEmptyToNulls(){

      if(this.coordonnee.getAdresse().equals("")){
         this.coordonnee.setAdresse(null);
      }
      if(this.coordonnee.getCp().equals("")){
         this.coordonnee.setCp(null);
      }
      if(this.coordonnee.getVille().equals("")){
         this.coordonnee.setVille(null);
      }
      if(this.coordonnee.getPays().equals("")){
         this.coordonnee.setPays(null);
      }
      if(this.coordonnee.getTel().equals("")){
         this.coordonnee.setTel(null);
      }
      if(this.coordonnee.getFax().equals("")){
         this.coordonnee.setFax(null);
      }
      if(this.coordonnee.getMail().equals("")){
         this.coordonnee.setMail(null);
      }

   }

   @Override
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getServiceManager().isUsedObjectManager(getObject());
      final boolean isReferenced = ManagerLocator.getServiceManager().isReferencedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      setDeletable(true);
      if(isUsed){
         setDeleteMessage(Labels.getLabel("service.deletion.isUsedCascade"));
      }
      if(isReferenced){
         setDeleteMessage(Labels.getLabel("service.deletion.isReferencedCascade"));
         if(getObject().getArchive()){ // deja archivé
            setDeletable(false);
            setDeleteMessage(Labels.getLabel("service.deletion.isAlreadyArchived"));
         }
      }
      setFantomable(!isReferenced);
      setCascadable(isUsed);
      return isReferenced;
   }

   @Override
   public void removeObject(final String comments){
      if(isCascadable()){
         if(isFantomable()){ // suppression possible
            ManagerLocator.getServiceManager().removeObjectCascadeManager(getObject(), comments,
               SessionUtils.getLoggedUser(sessionScope));
         }else{ // archivage
            getObject().setArchive(true);
            ManagerLocator.getServiceManager().updateObjectManager(getObject(), getObject().getCoordonnee(),
               getObject().getEtablissement(), null, SessionUtils.getLoggedUser(sessionScope), true, false);
         }
      }else{
         ManagerLocator.getServiceManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope));
      }
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         removeObject((String) event.getData());

         if(getListeCollaborations() != null){
            getListeCollaborations().updateTree();
         }

         if(isFantomable()){
            // clear form
            clearData();
         }else{
            if(getListeCollaborations() != null){
               getListeCollaborations().openService(getObject(), false, true);
               getListeCollaborations().updateCollabsWithoutService();
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
   public void onLaterAddNew(){
      this.switchToCreateMode(null);
      // ferme wait message
      Clients.clearBusy();
   }

   @Override
   public void onLaterCancel(){
      clearData();
      Clients.clearBusy();
   }

   @Override
   public void onLaterCreate(){
      clearConstraints();
      super.onLaterCreate();
      //Clients.clearBusy();
   }

   @Override
   public void onLaterValidate(){
      clearConstraints();
      super.onLaterValidate();
      //Clients.clearBusy();
   }

   /**
    * Cette méthode va supprimer un Collaborateur de la liste.
    * @param event Clic sur une image deleteCollab.
    */
   public void onClick$deleteCollab(final Event event){
      // on récupère le collab que l'utilisateur veut
      // suppimer
      final Collaborateur collab = (Collaborateur) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on enlève le collab de la liste et on la met à jour
      collaborateursEdit.remove(collab);
      final ListModel<Collaborateur> list = new ListModelList<>(collaborateursEdit);
      collabsListEdit.setModel(list);

   }

   /**
    * Cette méthode ajoute un collaborateur à la liste lors du
    * clic sur le bouton addCollaborateur.
    */
   public void onClick$addCollaborateur(){
      final String selectedNomAndPremon = this.addCollabBox.getValue().toUpperCase();
      addCollabBox.setValue(selectedNomAndPremon);

      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);

      if(ind > -1){
         final Collaborateur collab = allCollaborateurs.get(ind);

         if(!collaborateursEdit.contains(collab)){
            collaborateursEdit.add(collab);
            final ListModel<Collaborateur> list = new ListModelList<>(collaborateursEdit);
            collabsListEdit.setModel(list);
         }

         addCollabBox.setValue(null);
      }else{
         throw new WrongValueException(addCollabBox, Labels.getLabel("service.error.ajout.collaborateur"));
      }

   }

   /**
    * Cette méthode renvoie l'utilisateur vers la page FicheCollaborateur
    * en mode création. Méthode appellée lors du clic sur le bouton
    * newCollaborateur.
    */
   public void onClick$newCollaborateur(){
      getObjectTabController().switchToFicheCollaborateurCreationMode(this.service);
   }

   /**
    * Cette méthode descend la barre de scroll au niveau du groupe
    * groupCollabsActifsService.
    */
   public void onOpen$groupCollabsActifsService(){
      final String id = groupCollabsActifsService.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Cette méthode descend la barre de scroll au niveau du groupe
    * groupCollabsService.
    */
   public void onOpen$groupCollabsInactifsService(){
      final String id = groupCollabsInactifsService.getUuid();
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
    * villeBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$villeBox(final Event event){
      villeBox.setValue(villeBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * paysBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$paysBox(final Event event){
      paysBox.setValue(paysBox.getValue().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.service.getNom() != null){
         this.service.setNom(this.service.getNom().toUpperCase().trim());
      }

      if(this.coordonnee.getVille() != null){
         this.coordonnee.setVille(this.coordonnee.getVille().toUpperCase().trim());
      }

      if(this.coordonnee.getPays() != null){
         this.coordonnee.setPays(this.coordonnee.getPays().toUpperCase().trim());
      }
   }

   /**
    * Méthode appelée lors de la sélection d'un établissement : en mode de
    * création, la coordonnée du service sera alors pré-remplie.
    */
   public void onSelect$etabsBox(){
      if(createMode && selectedEtablissement != null && selectedEtablissement.getCoordonnee() != null){
         this.coordonnee.setAdresse(selectedEtablissement.getCoordonnee().getAdresse());
         this.coordonnee.setCp(selectedEtablissement.getCoordonnee().getCp());
         this.coordonnee.setVille(selectedEtablissement.getCoordonnee().getVille());
         this.coordonnee.setPays(selectedEtablissement.getCoordonnee().getPays());
         this.coordonnee.setTel(selectedEtablissement.getCoordonnee().getTel());
         this.coordonnee.setFax(selectedEtablissement.getCoordonnee().getFax());
         this.coordonnee.setMail(selectedEtablissement.getCoordonnee().getMail());
      }
   }

   /**
    * Check sur la checkbox archiveBox.
    */
   public void onCheck$archiveBox(){
      // on demande confirmation à l'utilisateur
      // de l'activation/inactivation
      if(archiveBox.isChecked()){
         if(Messagebox.show(Labels.getLabel("message.archive.message.service"), Labels.getLabel("message.archive.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            cascadeArchive = true;
         }else{
            cascadeArchive = false;
            archiveBox.setChecked(false);
         }
      }else{
         if(Messagebox.show(Labels.getLabel("message.not.archive.message.service"),
            Labels.getLabel("messagenot.not.archive.title"), Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION) == Messagebox.YES){
            cascadeArchive = true;
         }else{
            cascadeArchive = false;
            archiveBox.setChecked(true);
         }
      }
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){
      etablissements = ManagerLocator.getEtablissementManager().findAllObjectsWithOrderManager();
      if(null != etablissements && !etablissements.isEmpty()){
         if(this.service.getEtablissement() != null){
            selectedEtablissement = this.service.getEtablissement();
         }else{
            selectedEtablissement = etablissements.get(0);
         }
      }

      nomsAndPrenoms.clear();
      allCollaborateurs = ManagerLocator.getCollaborateurManager().findAllObjectsWithOrderManager();
      for(int i = 0; i < allCollaborateurs.size(); i++){
         nomsAndPrenoms.add(allCollaborateurs.get(i).getNomAndPrenom());
      }
      addCollabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      archiveBox.setChecked(this.service.getArchive());

      cascadeArchive = false;
   }

   //	public void revertService() {
   //		service.setServiceId(copyService.getServiceId());
   //		service.setCoordonnee(copyService.getCoordonnee());
   //		service.setEtablissement(copyService.getEtablissement());
   //		service.setNom(copyService.getNom());
   //		service.setArchive(copyService.getArchive());
   //		service.setCollaborateurs(copyService.getCollaborateurs());
   //	}
   //
   //	public void revertCoordonnee() {
   //		coordonnee.setCoordonneeId(copyCoord.getCoordonneeId());
   //		coordonnee.setAdresse(copyCoord.getAdresse());
   //		coordonnee.setCp(copyCoord.getCp());
   //		coordonnee.setVille(copyCoord.getVille());
   //		coordonnee.setPays(copyCoord.getPays());
   //		coordonnee.setTel(copyCoord.getTel());
   //		coordonnee.setFax(copyCoord.getFax());
   //		coordonnee.setMail(copyCoord.getMail());
   //	}

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(adresseBox);
      Clients.clearWrongValue(cpBox);
      Clients.clearWrongValue(villeBox);
      Clients.clearWrongValue(paysBox);
      Clients.clearWrongValue(telBox);
      Clients.clearWrongValue(faxBox);
      Clients.clearWrongValue(mailBox);
      addCollabBox.setValue(null);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForService(){
      drawActionsButtons("Collaborateur");
      newCollaborateur.setDisabled(!drawActionOnOneButton("Collaborateur", "Creation"));
   }

   /**
    * Méthode pour l'impression de la fiche d'un établissement.
    */
   public void onClick$print(){
      if(this.service != null){
         // création du document XML contenant les données à imprimer
         final Document document = ManagerLocator.getXmlUtils().createJDomDocument();
         final Element root = document.getRootElement();
         final Element pageXML = ManagerLocator.getXmlUtils().addPage(root, null);
         ManagerLocator.getXmlUtils().addHautDePage(root, Labels.getLabel("ficheService.panel.title"), false, null);
         ManagerLocator.getXmlUtils().addBasDePage(root, "");

         addInfosGeneralesToPrint(pageXML);
         addCoordonneesToPrint(pageXML);
         addCollaborateursActifsToPrint(pageXML);
         addCollaborateursInactifsToPrint(pageXML);

         // Transformation du document en fichier
         byte[] dl = null;
         try{
            dl = ManagerLocator.getXmlUtils().creerPdf(document);
         }catch(final Exception e){
            log.error("An error occurred: {}", e.toString()); 
         }

         final StringBuffer sb = new StringBuffer();
         sb.append(service.getNom().trim());
         sb.append(".pdf");
         // envoie du fichier à imprimer à l'utilisateur
         if(dl != null){
            Filedownload.save(dl, "application/pdf", sb.toString());
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
      // établissement
      if(service.getEtablissement() != null){
         tmp = service.getEtablissement().getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("service.etablissement"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cpVide});
      // nom
      if(service.getNom() != null){
         tmp = service.getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("service.nom"), tmp);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp2, cpVide});
      // Inactif
      if(service.getArchive() != null){
         tmp = ObjectTypesFormatters.booleanLitteralFormatter(this.service.getArchive());
      }else{
         tmp = "";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("service.archive"), tmp);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp3, cpVide});
      final Paragraphe par1 = new Paragraphe(null, new LigneParagraphe[] {li1, li2, li3}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les coordonnées à imprimer.
    * @param page
    */
   public void addCoordonneesToPrint(final Element page){

      if(this.service.getCoordonnee() != null){
         String tmp = "";
         final CoupleValeur cpVide = new CoupleValeur("", "");
         // Coordonnées adresse
         if(service.getCoordonnee().getAdresse() != null){
            tmp = service.getCoordonnee().getAdresse();
         }else{
            tmp = "";
         }
         final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("coordonnee.adresse"), tmp);
         final LigneParagraphe li5 = new LigneParagraphe("", new CoupleValeur[] {cp5, cpVide});
         // Coordonnées code postal
         if(service.getCoordonnee().getCp() != null){
            tmp = service.getCoordonnee().getCp();
         }else{
            tmp = "";
         }
         final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("coordonnee.cp"), tmp);
         final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp6, cpVide});
         // Coordonnées ville
         if(service.getCoordonnee().getVille() != null){
            tmp = service.getCoordonnee().getVille();
         }else{
            tmp = "";
         }
         final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("coordonnee.ville"), tmp);
         final LigneParagraphe li7 = new LigneParagraphe("", new CoupleValeur[] {cp7, cpVide});
         // Coordonnées pays
         if(service.getCoordonnee().getPays() != null){
            tmp = service.getCoordonnee().getPays();
         }else{
            tmp = "";
         }
         final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("coordonnee.pays"), tmp);
         final LigneParagraphe li8 = new LigneParagraphe("", new CoupleValeur[] {cp8, cpVide});
         // Coordonnées tel
         if(service.getCoordonnee().getTel() != null){
            tmp = service.getCoordonnee().getTel();
         }else{
            tmp = "";
         }
         final CoupleValeur cp9 = new CoupleValeur(Labels.getLabel("coordonnee.telephone"), tmp);
         final LigneParagraphe li9 = new LigneParagraphe("", new CoupleValeur[] {cp9, cpVide});
         // Coordonnées fax
         if(service.getCoordonnee().getFax() != null){
            tmp = service.getCoordonnee().getFax();
         }else{
            tmp = "";
         }
         final CoupleValeur cp10 = new CoupleValeur(Labels.getLabel("coordonnee.fax"), tmp);
         final LigneParagraphe li10 = new LigneParagraphe("", new CoupleValeur[] {cp10, cpVide});
         // Coordonnées mail
         if(service.getCoordonnee().getMail() != null){
            tmp = service.getCoordonnee().getMail();
         }else{
            tmp = "";
         }
         final CoupleValeur cp11 = new CoupleValeur(Labels.getLabel("coordonnee.mail"), tmp);
         final LigneParagraphe li11 = new LigneParagraphe("", new CoupleValeur[] {cp11, cpVide});
         final Paragraphe par2 = new Paragraphe(Labels.getLabel("coordonnee.group.infos"),
            new LigneParagraphe[] {li5, li6, li7, li8, li9, li10, li11}, null, null, null);
         ManagerLocator.getXmlUtils().addParagraphe(page, par2);
      }
   }

   /**
    * Ajout les services à imprimer.
    * @param page
    */
   public void addCollaborateursActifsToPrint(final Element page){
      // Services
      final EnteteListe entetes = new EnteteListe(new String[] {Labels.getLabel("collaborateur.nom"),
         Labels.getLabel("collaborateur.prenom"), Labels.getLabel("collaborateur.specialite")});
      final LigneListe[] liste = new LigneListe[collaborateursActifs.size()];
      for(int i = 0; i < collaborateursActifs.size(); i++){
         final String[] valeurs = new String[3];
         // nom
         if(collaborateursActifs.get(i).getNom() != null){
            valeurs[0] = collaborateursActifs.get(i).getNom();
         }else{
            valeurs[0] = "-";
         }
         // prenom
         if(collaborateursActifs.get(i).getPrenom() != null){
            valeurs[1] = collaborateursActifs.get(i).getPrenom();
         }else{
            valeurs[1] = "-";
         }
         // specialite
         if(collaborateursActifs.get(i).getSpecialite() != null){
            valeurs[2] = collaborateursActifs.get(i).getSpecialite().getNom();
         }else{
            valeurs[2] = "-";
         }
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      final ListeElement listeSites = new ListeElement(null, entetes, liste);
      // ajout du paragraphe
      final StringBuffer titre = new StringBuffer();
      titre.append(Labels.getLabel("service.collaborateurs.actifs"));
      titre.append(" (");
      titre.append(collaborateursActifs.size());
      titre.append(")");
      final Paragraphe par = new Paragraphe(titre.toString(), null, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   /**
    * Ajout les services à imprimer.
    * @param page
    */
   public void addCollaborateursInactifsToPrint(final Element page){
      // Services
      final EnteteListe entetes = new EnteteListe(new String[] {Labels.getLabel("collaborateur.nom"),
         Labels.getLabel("collaborateur.prenom"), Labels.getLabel("collaborateur.specialite")});
      final LigneListe[] liste = new LigneListe[collaborateursInactifs.size()];
      for(int i = 0; i < collaborateursInactifs.size(); i++){
         final String[] valeurs = new String[3];
         // nom
         if(collaborateursInactifs.get(i).getNom() != null){
            valeurs[0] = collaborateursInactifs.get(i).getNom();
         }else{
            valeurs[0] = "-";
         }
         // prenom
         if(collaborateursInactifs.get(i).getPrenom() != null){
            valeurs[1] = collaborateursInactifs.get(i).getPrenom();
         }else{
            valeurs[1] = "-";
         }
         // specialite
         if(collaborateursInactifs.get(i).getSpecialite() != null){
            valeurs[2] = collaborateursInactifs.get(i).getSpecialite().getNom();
         }else{
            valeurs[2] = "-";
         }
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      final ListeElement listeSites = new ListeElement(null, entetes, liste);
      // ajout du paragraphe
      final StringBuffer titre = new StringBuffer();
      titre.append(Labels.getLabel("service.collaborateurs.inactifs"));
      titre.append(" (");
      titre.append(collaborateursInactifs.size());
      titre.append(")");
      final Paragraphe par = new Paragraphe(titre.toString(), null, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   public String isArchiveCollab(final Object obj){
      final Boolean arch = (Boolean) obj;

      return ObjectTypesFormatters.booleanLitteralFormatter(arch);
   }

   public void onClick$collabActifNom(final Event event){
      final Collaborateur collab = (Collaborateur) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(getListeCollaborations() != null){
         getListeCollaborations().openCollaborateurInTree(collab, true, true);
      }
   }

   public void onClick$collabInactifNom(final Event event){
      final Collaborateur collab = (Collaborateur) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(getListeCollaborations() != null){
         getListeCollaborations().openCollaborateurInTree(collab, true, true);
      }
   }

   /*************************************************/
   /**               ACCESSEURS                    **/
   /*************************************************/

   public Service getService(){
      return service;
   }

   public void setService(final Service s){
      setObject(s);
   }

   public Coordonnee getCoordonnee(){
      return coordonnee;
   }

   public void setCoordonnee(final Coordonnee coord){
      this.coordonnee = coord;
   }

   public Coordonnee getCopyCoord(){
      return copyCoord;
   }

   public void setCopyCoord(final Coordonnee copy){
      this.copyCoord = copy;
   }

   public List<Etablissement> getEtablissements(){
      return etablissements;
   }

   public Etablissement getSelectedEtablissement(){
      return selectedEtablissement;
   }

   public void setSelectedEtablissement(final Etablissement selected){
      this.selectedEtablissement = selected;
   }

   public String getCollabsActifsGroupHeader(){
      return collabsActifsGroupHeader;
   }

   public String getCollabsInactifsGroupHeader(){
      return collabsInactifsGroupHeader;
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

      if(this.service != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.service.getArchive());
      }else{
         return "";
      }
   }

   public boolean isCreateMode(){
      return createMode;
   }

   public void setCreateMode(final boolean createM){
      this.createMode = createM;
   }

   public boolean isCascadeArchive(){
      return cascadeArchive;
   }

   public void setCascadeArchive(final boolean cascadeA){
      this.cascadeArchive = cascadeA;
   }

   public Constraint getNomConstraint(){
      return ContexteConstraints.getNomConstraint();
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

   public List<Collaborateur> getCollaborateursActifs(){
      return collaborateursActifs;
   }

   public void setCollaborateursActifs(final List<Collaborateur> collaborateurs){
      this.collaborateursActifs = collaborateurs;
   }

   public List<Collaborateur> getCollaborateursInactifs(){
      return collaborateursInactifs;
   }

   public void setCollaborateursInactifs(final List<Collaborateur> collaborateurs){
      this.collaborateursInactifs = collaborateurs;
   }

   public List<Collaborateur> getCollaborateursEdit(){
      return collaborateursEdit;
   }

   public void setCollaborateursEdit(final List<Collaborateur> collaborateurs){
      this.collaborateursEdit = collaborateurs;
   }

   @Override
   public String getDeleteWaitLabel(){
      if(isFantomable()){
         return Labels.getLabel("deletion.general.wait");
      }else{
         return Labels.getLabel("archivage.general.wait");
      }
   }
}