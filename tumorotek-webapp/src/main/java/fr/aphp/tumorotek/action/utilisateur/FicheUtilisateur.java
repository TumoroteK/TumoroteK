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
package fr.aphp.tumorotek.action.utilisateur;

import static fr.aphp.tumorotek.param.TkParam.LDAP_AUTHENTICATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
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
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.AdministrationController;
import fr.aphp.tumorotek.action.constraints.ConstDateLimit;
import fr.aphp.tumorotek.action.constraints.ConstEmail;
import fr.aphp.tumorotek.action.constraints.ConstPassword;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.imprimante.AffectationCollectionsModale;
import fr.aphp.tumorotek.action.imprimante.AffectationDecorator;
import fr.aphp.tumorotek.action.imprimante.AffectationRowRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.validation.utilisateur.UtilisateurValidator;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 *
 */
public class FicheUtilisateur extends AbstractFicheCombineController
{

   private final Logger log = LoggerFactory.getLogger(FicheUtilisateur.class);

   private static final long serialVersionUID = 6626506501770467670L;

   // Static Components pour le mode static.
   private Label loginLabel;

   private Label passwordLabel;

   private Label emailLabel;

   private Label collaborateurLabel;

   private Label archiveLabel;

   private Label timeoutLabel;

   private Row rowPfsAdmin;

   private Menubar menuBar;

   private Group affectationsGroup;

   private Row affectationsRow;

   private Grid affectationsGrid;

   private Button addAffectations;

   private Button editPassword;

   // Editable components : mode d'édition ou de création.
   private Label loginRequired;

   private Label passwordRequired;

   private Textbox loginBox;

   private Textbox passwordBox;

   private Textbox emailBox;

   private Checkbox archiveBox;

   // private Row timeoutRow;
   private Datebox timeoutBox;

   // private Row timeoutHelpRow;
   private Row passwordRow;

   private Row rowConfirmationPassword;

   private Textbox confirmPasswordBox;

   private Combobox collabBox;

   private Label collabAideSaisieUser;

   private Column deleteRoleColumn;

   private Row rowAddRoleTitle;

   private Row rowAddRole;

   private Grid rolesGrid;

   //  private Listbox collectionsBox;
   private Listbox profilsBox;

   private Label authLdapLabel;

   private Checkbox authLdapCheckbox;

   // @since 2.1
   private Checkbox banquesArchiveBox;

   // composant pour le mode admin de collection
   private Component[] objAdminCollectionComponents;

   // component pour le mode modif de son propre compte
   private Component[] objModifPersoComponents;

   private Component[] labelModifPersoComponents;

   // Objets Principaux.
   private Utilisateur user;

   // Associations.
   private List<ProfilUtilisateur> profilUtilisateurs = new ArrayList<>();

   private List<ProfilUtilisateur> profilUtilisateursOtherBanques = new ArrayList<>();

   private final List<Plateforme> plateformes = new ArrayList<>();

   private List<Collaborateur> collaborateurs = new ArrayList<>();

   private Collaborateur selectedCollaborateur;

   private List<String> nomsAndPrenoms = new ArrayList<>();

   private ListModelList<Banque> banques;

   private List<Banque> selectedBanques = new ArrayList<>();

   private final List<Profil> profils = new ArrayList<>();

   private Profil selectedProfil;

   private Hashtable<Banque, ProfilUtilisateur> banquesDefined = new Hashtable<>();

   private List<AffectationDecorator> affectationDecorators = new ArrayList<>();

   private static AffectationRowRenderer affectationRenderer;

   // Variables formulaire.
   private String plateformesFormated = "";

   private Integer nbMoisMdp = null;

   private Date defaultTimeoutDate;

   private boolean isAdminPF = false;

   private boolean isAdmin = false;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.utilisateur");
      setFantomable(false);
      setDeletable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.loginLabel, this.authLdapLabel, this.passwordLabel, this.emailLabel,
         this.collaborateurLabel, this.archiveLabel, this.timeoutLabel, this.rowPfsAdmin, this.menuBar, this.affectationsGroup,
         this.affectationsRow, this.editPassword});

      setObjBoxsComponents(new Component[] {this.loginBox, this.authLdapCheckbox, this.passwordBox, this.rowConfirmationPassword,
         this.emailBox, this.archiveBox, this.timeoutBox, this.collabBox, this.collabAideSaisieUser, this.deleteRoleColumn,
         this.rowAddRole, this.rowAddRoleTitle});

      objAdminCollectionComponents = new Component[] {this.deleteRoleColumn, this.rowAddRole, this.rowAddRoleTitle};

      // Initialisation des listes de composants
      labelModifPersoComponents = new Component[] {this.loginLabel, this.passwordLabel, this.emailLabel, this.collaborateurLabel};

      objModifPersoComponents = new Component[] {this.loginBox, this.passwordBox, this.rowConfirmationPassword, this.emailBox,
         this.collabBox, this.collabAideSaisieUser};

      setRequiredMarks(new Component[] {this.loginRequired, this.passwordRequired});

      affectationRenderer = new AffectationRowRenderer(SessionUtils.getPlateforme(sessionScope));
      drawActionsForUtilisateur();
      menuBar.setVisible(false);
      affectationsGroup.setOpen(false);

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      nbMoisMdp = ObjectTypesFormatters.getNbMoisMdp();

      if(nbMoisMdp != null){
         final Calendar cal = Calendar.getInstance();
         cal.add(Calendar.MONTH, nbMoisMdp);
         defaultTimeoutDate = cal.getTime();
      }

      getBinder().loadAll();

   }

   @Override
   public void setObject(final TKdataObject obj){
      this.user = (Utilisateur) obj;

      // un utilisateur même nom admin
      // peut modifier sa fiche
      if(!isAdmin && !isAdminPF){
         if(this.user.equals(SessionUtils.getLoggedUser(sessionScope))){
            setCanEdit(true);
         }else{
            setCanEdit(false);
         }
      }

      authLdapCheckbox.setChecked(user.isLdap());
      passwordRow.setVisible(!user.isLdap());
      // timeoutRow.setVisible(!user.isLdap());
      // timeoutHelpRow.setVisible(!user.isLdap());

      profilUtilisateurs.clear();
      profilUtilisateursOtherBanques.clear();
      plateformes.clear();
      plateformesFormated = "";

      if(user.getUtilisateurId() != null){
         // on récup les banques avec des droits d'admin
         final List<Banque> availableBanques =
            ManagerLocator.getUtilisateurManager().getAvailableBanquesAsAdminManager(SessionUtils.getLoggedUser(sessionScope));
         // on récup tous les profils
         final List<ProfilUtilisateur> tmp = ManagerLocator.getProfilUtilisateurManager().findByUtilisateurManager(user, false);
         // on va afficher seulement les profils pour lesquels l'utilisateur
         // a des droits d'admin sur la banque
         for(int i = 0; i < tmp.size(); i++){
            if(availableBanques.contains(tmp.get(i).getBanque())){
               profilUtilisateurs.add(tmp.get(i));
            }else{
               profilUtilisateursOtherBanques.add(tmp.get(i));
            }
         }

         plateformes.addAll(ManagerLocator.getUtilisateurManager().getPlateformesManager(user));

         if(this.user.getCollaborateur() != null && collaborateurs.contains(this.user.getCollaborateur())){
            selectedCollaborateur = this.user.getCollaborateur();
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
         }else if(this.user.getCollaborateur() != null){
            collaborateurs.add(this.user.getCollaborateur());
            selectedCollaborateur = this.user.getCollaborateur();
            nomsAndPrenoms.add(this.user.getCollaborateur().getNomAndPrenom());
            collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
         }else{
            collabBox.setValue("");
            selectedCollaborateur = null;
         }

         initAffectations();
      }

      final StringBuffer sb = new StringBuffer();
      if(plateformes.size() > 0){
         for(int i = 0; i < plateformes.size(); i++){
            sb.append(plateformes.get(i).getNom());
            if(i + 1 < plateformes.size()){
               sb.append(", ");
            }
         }
      }else{
         sb.append(Labels.getLabel("utilisateur.plateformes.aucune"));
      }
      plateformesFormated = sb.toString();

      super.setObject(user);

      // desactive le bouton d'édition de mot de passe
      // si le compte est vide, lié à un compté ldap
      // ou si l'utilisateur courant n'as pas le droit d'édition
      editPassword.setDisabled(!isCanEdit() || user.isLdap() || user.getUtilisateurId() == null);

      // TK-252
      // toolbar active pour les comptes non superadmin
      // obligé
      if(!this.user.isSuperAdmin()){
         disableToolBar(false);
      }else{ // superadmin -> non modifiable, que par lui
         disableToolBar(!this.user.equals(SessionUtils.getLoggedUser(sessionScope)));
         editPassword.setDisabled(!this.user.equals(SessionUtils.getLoggedUser(sessionScope)));
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.user.clone());
   }

   @Override
   public Utilisateur getObject(){
      return this.user;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public UtilisateurController getObjectTabController(){
      return (UtilisateurController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Utilisateur());
      super.setNewObject();
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();
      collabBox.setValue(null);

      final boolean ldapAuthActivated = isLdapAuthActivated();

      authLdapCheckbox.setChecked(ldapAuthActivated);
      passwordRow.setVisible(!ldapAuthActivated);

      displayPasswordEdition(!ldapAuthActivated);

      // lors de la création, on fixe par défaut la date de
      // desactivation du MDP à 6 mois
      if(defaultTimeoutDate != null){
         this.user.setTimeOut(defaultTimeoutDate);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.user.equals(new Utilisateur()));

      if(this.user.getUtilisateurId() == null){
         menuBar.setVisible(false);
      }

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   @Override
   public void switchToEditMode(){

      if(isAdminPF){
         super.switchToEditMode();
      }else{
         validateC.setVisible(true);
         revertC.setVisible(true);
         createC.setVisible(false);
         cancelC.setVisible(false);
         addNewC.setVisible(false);
         editC.setVisible(false);
         deleteC.setVisible(false);
         if(isAdmin){
            for(int i = 0; i < objAdminCollectionComponents.length; i++){
               objAdminCollectionComponents[i].setVisible(true);
            }
         }

         if(user.equals(SessionUtils.getLoggedUser(sessionScope))){
            for(int i = 0; i < objModifPersoComponents.length; i++){
               objModifPersoComponents[i].setVisible(true);
            }
            for(int i = 0; i < labelModifPersoComponents.length; i++){
               labelModifPersoComponents[i].setVisible(false);
            }
         }
      }

      passwordRow.setVisible(!getObject().isLdap());

      displayPasswordEdition(false);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      getBinder().loadComponent(self);
   }

   @Override
   public void setFocusOnElement(){
      loginBox.setFocus(true);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){

      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      this.user.setArchive(archiveBox.isChecked());
      this.user.setSuperAdmin(false);

      // Gestion du collaborateur
      final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
      this.collabBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      // on fusionne les 2 listes de profils
      final List<ProfilUtilisateur> tmp = new ArrayList<>();
      tmp.addAll(profilUtilisateurs);
      tmp.addAll(profilUtilisateursOtherBanques);

      user.setLdap(authLdapCheckbox.isChecked());

      if(!user.isLdap()){
         // encodage du password
         if(passwordBox.getValue() != null && !passwordBox.getValue().trim().isEmpty()){
            user.setPassword(ObjectTypesFormatters.getEncodedPassword(passwordBox.getValue()));
         }
      }else{
         user.setPassword(null);
      }

      // update de l'objet
      ManagerLocator.getUtilisateurManager().createObjectManager(user, selectedCollaborateur, tmp, null,
         SessionUtils.getLoggedUser(sessionScope), SessionUtils.getPlateforme(sessionScope));
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeContrat
    */
   private ListeUtilisateur getListeUtilisateur(){
      return getObjectTabController().getListe();
   }

   @Override
   public void onClick$addNewC(){
      switchToCreateMode();
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$createC(){
      if(!authLdapCheckbox.isChecked() && !confirmPasswordBox.getValue().equals(passwordBox.getValue())){
         throw new WrongValueException(confirmPasswordBox, Labels.getLabel("utilisateur.bad.password"));
      }

      Clients.showBusy(Labels.getLabel("utilisateur.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         setObject(this.user);
         switchToStaticMode();
         disableToolBar(false);

         if(getListeUtilisateur() != null){
            // ajout de l'utilisateur à la liste
            getListeUtilisateur().addToObjectList(this.user);
         }
         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getUtilisateurManager().isUsedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isUsed){
         setDeleteMessage(Labels.getLabel("utilisateur.suppression.impossible"));
      }
      // propose l'archivage par ce booleen dans removeObject
      setCascadable(isUsed);
      return isUsed;
   }

   @Override
   public void removeObject(final String comments){
      if(!isCascadable()){
         ManagerLocator.getUtilisateurManager().removeObjectManager(getObject());
      }else{
         // opération d'archivage
         final OperationType oType = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Archivage", true).get(0);
         getObject().setArchive(true);
         // on fusionne les 2 listes de profils
         final List<ProfilUtilisateur> tmp = new ArrayList<>();
         tmp.addAll(profilUtilisateurs);
         tmp.addAll(profilUtilisateursOtherBanques);
         // update de l'objet
         ManagerLocator.getUtilisateurManager().updateObjectManager(user, selectedCollaborateur, tmp, null,
            SessionUtils.getLoggedUser(sessionScope), oType);
      }
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         removeObject((String) event.getData());

         if(!isCascadable()){ // suppression
            if(getListeUtilisateur() != null){
               // on enlève le contrat de la liste
               getListeUtilisateur().removeObjectAndUpdateList(this.user);
            }
            // clear form
            clearData();
         }else{ //archivage
            cloneObject();
            switchToStaticMode();

            if(getListeUtilisateur() != null){
               // ajout de l'utilisateur à la liste
               getListeUtilisateur().updateObjectGridList(this.user);
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

   public void onClick$delete2C(){
      if(this.user != null){
         // si l'utilisateur n'a jamais été utilisé : pas d'historique
         if(!ManagerLocator.getUtilisateurManager().isUsedObjectManager(user)){
            if(Messagebox.show(
               ObjectTypesFormatters.getLabel("message.deletion.message",
                  new String[] {Labels.getLabel("message.deletion.utilisateur")}),
               Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

               // suppression du contrat
               ManagerLocator.getUtilisateurManager().removeObjectManager(user);

               // on vérifie que l'on retrouve bien la page
               // contenant la liste des contrats
               if(getListeUtilisateur() != null){
                  // on enlève le contrat de la liste
                  getListeUtilisateur().removeObjectFromList(this.user);
               }
               // clear form
               clearData();
            }
         }else{
            if(Messagebox.show(Labels.getLabel("utilisateur.suppression.impossible"),
               Labels.getLabel("utilisateur.archiver.title"), Messagebox.YES | Messagebox.NO,
               Messagebox.QUESTION) == Messagebox.YES){

               // opération d'archivage
               final OperationType oType =
                  ManagerLocator.getOperationTypeManager().findByNomLikeManager("Archivage", true).get(0);
               this.user.setArchive(true);
               // on fusionne les 2 listes de profils
               final List<ProfilUtilisateur> tmp = new ArrayList<>();
               tmp.addAll(profilUtilisateurs);
               tmp.addAll(profilUtilisateursOtherBanques);
               // update de l'objet
               ManagerLocator.getUtilisateurManager().updateObjectManager(user, selectedCollaborateur, tmp, null,
                  SessionUtils.getLoggedUser(sessionScope), oType);
               cloneObject();
               switchToStaticMode();

               if(getListeUtilisateur() != null){
                  // ajout de l'utilisateur à la liste
                  getListeUtilisateur().updateObjectGridList(this.user);
               }
            }
         }
      }
   }

   //	@Override
   //	public void onClick$editC() {
   //		if (this.user != null) {
   //			switchToEditMode();
   //		}
   //	}

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){

      if(!authLdapCheckbox.isChecked() && rowConfirmationPassword.isVisible()
         && !confirmPasswordBox.getValue().equals(passwordBox.getValue())){
         throw new WrongValueException(confirmPasswordBox, Labels.getLabel("utilisateur.bad.password"));
      }

      Clients.showBusy(Labels.getLabel("utilisateur.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
         switchToStaticMode();

         if(getListeUtilisateur() != null){
            // ajout de l'utilisateur à la liste
            getListeUtilisateur().updateObjectGridList(this.user);
         }
         // ferme wait message
         Clients.clearBusy();

         initAffectations();
         getBinder().loadComponent(affectationsGrid);
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Méthode appelée après modification du mdp. On va alors
    * mettre à jour la fiche et la liste de utilisateurs.
    * @param event
    */
   public void onGetPasswordUpdated(final Event event){
      if(event.getData() != null){
         final Utilisateur u = (Utilisateur) event.getData();

         // maj de la fiche
         setObject(u);

         // maj de la liste
         if(getListeUtilisateur() != null){
            // ajout de l'utilisateur à la liste
            getListeUtilisateur().updateObjectGridList(this.user);
         }
      }
   }

   @Override
   public void setEmptyToNulls(){
      if(this.user.getEmail() != null && this.user.getEmail().trim().equals("")){
         this.user.setEmail(null);
      }
      //    if(this.user.getPassword().equals("")){
      //        this.user.setPassword(null);
      //    }
   }

   @Override
   public void updateObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      this.user.setArchive(archiveBox.isChecked());

      OperationType oType = null;
      // on teste si la modification est un archivage ou une
      // restauration
      if(user.isArchive()){
         if(!((Utilisateur) getClone()).isArchive()){
            oType = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Archivage", true).get(0);
         }else{
            oType = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Modification", true).get(0);
         }
      }else{
         if(((Utilisateur) getClone()).isArchive()){
            oType = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Restauration", true).get(0);
         }else{
            oType = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Modification", true).get(0);
         }
      }

      // encrypt only if new password
      if(passwordBox.isVisible()){
         user.setPassword(ObjectTypesFormatters.getEncodedPassword(passwordBox.getValue()));
      }

      user.setLdap(authLdapCheckbox.isChecked());

      // Gestion du collaborateur
      final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
      this.collabBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      // on fusionne les 2 listes de profils
      final List<ProfilUtilisateur> tmp = new ArrayList<>();
      tmp.addAll(profilUtilisateurs);
      tmp.addAll(profilUtilisateursOtherBanques);

      // update de l'objet
      ManagerLocator.getUtilisateurManager().updateObjectManager(user, selectedCollaborateur, tmp, null,
         SessionUtils.getLoggedUser(sessionScope), oType);
   }

   /**
    * Clic sur le bouton addRoleButton pour ajouter un role à
    * l'utilisateur.
    * @version 2.1
    */

   public void onClick$addRoleButton(){

      // if no profile defined in PF -> warning
      if(selectedProfil != null){

         selectedBanques.clear();
         // selectedBanques.addAll(((Selectable<Banque>) collectionsBox.getModel()).getSelection());
         selectedBanques.addAll(banques.getSelection());

         for(final Banque bq : selectedBanques){
            final ProfilUtilisateur newRole = new ProfilUtilisateur();
            newRole.setBanque(bq);
            newRole.setProfil(selectedProfil);
            newRole.setUtilisateur(user);

            if(!profilUtilisateurs.contains(newRole)){

               // si une banque a déja été définie, on modifie le
               // précédent profil
               if(banquesDefined.containsKey(bq)){
                  final ProfilUtilisateur pu = banquesDefined.get(bq);
                  pu.setProfil(selectedProfil);
                  banquesDefined.remove(bq);
                  banquesDefined.put(bq, pu);
               }else{
                  profilUtilisateurs.add(newRole);
                  banquesDefined.put(bq, newRole);
               }
            }
         }
         final ListModel<ProfilUtilisateur> list = new ListModelList<>(profilUtilisateurs);
         rolesGrid.setModel(list);
      }else{ // warn empty profiles
         throw new WrongValueException(profilsBox, Labels.getLabel("ficheUtilisateur.emptyRoles.warning"));
      }
   }

   /**
    * Clic sur le bouton deleteRole pour supprimer un role à
    * l'utilisateur.
    * @version 2.1
    */
   public void onClick$deleteRole(final Event event){
      //@since 2.1 template rendering
      final ProfilUtilisateur role = (ProfilUtilisateur) event.getData();
      // AbstractListeController2
      //	.getBindingData((ForwardEvent) event, false);

      // l'utilisateur ne peut modifier que les autorisations
      // pour lesquelles il a un droit d'administration sur
      // la banque
      if(banques.contains(role.getBanque())){
         if(profilUtilisateurs.contains(role)){
            profilUtilisateurs.remove(role);
            final ListModel<ProfilUtilisateur> list = new ListModelList<>(profilUtilisateurs);
            rolesGrid.setModel(list);
            if(banquesDefined.containsKey(role.getBanque())){
               banquesDefined.remove(role.getBanque());
            }
         }
      }else{
         Messagebox.show(Labels.getLabel("utilisateur.suppression.role.impossible"), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * collabAideSaisieUser. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection d'un collaborateur.
    */
   public void onClick$collabAideSaisieUser(){
      // on récupère le collaborateur actuellement sélectionné
      // pour l'afficher dans la modale
      final List<Object> old = new ArrayList<>();
      if(selectedCollaborateur != null){
         old.add(selectedCollaborateur);
      }

      // ouvre la modale
      openCollaborationsWindow(page, "general.recherche", "select", null, "Collaborateur", null, Path.getPath(self), old);

   }

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand
    * l'utilisateur sélectionne un collaborateur.
    * @param e Event contenant le collaborateur sélectionné.
    */
   public void onGetObjectFromSelection(final Event e){

      // les collaborateurs peuvent être modifiés dans la fenêtre
      // d'aide => maj de ceux-ci
      nomsAndPrenoms = new ArrayList<>();
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
      }

      if(this.user.getCollaborateur() != null && !collaborateurs.contains(this.user.getCollaborateur())){
         collaborateurs.add(this.user.getCollaborateur());
         nomsAndPrenoms.add(this.user.getCollaborateur().getNomAndPrenom());
      }
      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      // si un collaborateur a été sélectionné
      if(e.getData() != null){
         final Collaborateur coll = (Collaborateur) e.getData();
         if(nomsAndPrenoms.contains(coll.getNomAndPrenom())){
            final int ind = nomsAndPrenoms.indexOf(coll.getNomAndPrenom());
            selectedCollaborateur = collaborateurs.get(ind);
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
         }
      }
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){

      // init des collaborateurs
      // nomsAndPrenoms .clear();

      if(collaborateurs.isEmpty()){
         collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
         for(int i = 0; i < collaborateurs.size(); i++){
            nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
         }
      }
      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      // reset profiles
      profils.clear();
      // @since 2.1 plateforme + archive
      profils
         .addAll(ManagerLocator.getProfilManager().findByPlateformeAndArchiveManager(SessionUtils.getCurrentPlateforme(), false));
      if(!profils.isEmpty()){
         selectedProfil = profils.get(0);
      }

      archiveBox.setChecked(this.user.isArchive());

      selectedBanques.clear();
      banques = new ListModelList<>(
         ManagerLocator.getUtilisateurManager().getAvailableBanquesAsAdminManager(SessionUtils.getLoggedUser(sessionScope)));
      // ((Selectable<Banque>) collectionsBox.getModel())
      // .setSelection(ncfCess);
      // if (banques.size() > 0) {
      //	selectedBanque = banques.get(0);
      //}

      banquesDefined.clear();
      for(int i = 0; i < profilUtilisateurs.size(); i++){
         banquesDefined.put(profilUtilisateurs.get(i).getBanque(), profilUtilisateurs.get(i));
      }
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(loginBox);
      Clients.clearWrongValue(passwordBox);
      Clients.clearWrongValue(emailBox);
      Clients.clearWrongValue(archiveBox);
      Clients.clearWrongValue(timeoutBox);
      Clients.clearWrongValue(confirmPasswordBox);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForUtilisateur(){
      if(sessionScope.containsKey("AdminPF")){
         isAdminPF = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         isAdmin = (Boolean) sessionScope.get("Admin");
      }

      affectationRenderer.setCanEdit(false);
      addAffectations.setVisible(false);
      if(isAdminPF){
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
         setCanSeeHistorique(true);
         affectationRenderer.setCanEdit(true);
         addAffectations.setVisible(true);
      }else if(isAdmin){
         setCanNew(false);
         setCanEdit(true);
         setCanDelete(false);
         setCanSeeHistorique(false);
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
         setCanSeeHistorique(false);
      }

      //		List<String> entites = new ArrayList<String>();
      //		entites.add("Collaborateur");
      //		setDroitsConsultation(drawConsultationLinks(entites));
      // si pas le droit d'accès aux dérivés, on cache le lien
      if(!getDroitsConsultation().get("Collaborateur")){
         collaborateurLabel.setSclass(null);
      }else{
         collaborateurLabel.setSclass("formLink");
      }

      editPassword.setDisabled(true);
   }

   /**
    * Affiche la fiche d'un medecin referent.
    */
   public void onClick$collaborateurLabel(){
      if(getDroitsConsultation().get("Collaborateur") && this.user.getCollaborateur() != null){

         // ouvre la modale
         openCollaborationsWindow(page, "context.modal.collaborateur", "details", this.user.getCollaborateur(), null, null, null,
            null);
      }
   }

   public void onClick$editPassword(){
      openPasswordWindow(page, self, user);
   }

   /**********************************************************************/
   /*********************** Gestion des affectations**********************/
   /**********************************************************************/

   public void initAffectations(){
      affectationDecorators = new ArrayList<>();
      if(this.user.getUtilisateurId() != null){
         // gestion des imprimantes
         // on extrait les banques accessibles pour la pf actuelle
         final List<Banque> bks =
            ManagerLocator.getBanqueManager().findByUtilisateurAndPFManager(user, SessionUtils.getPlateforme(sessionScope));

         // pour chaque banque, on crée un AffectationDecorator
         for(int j = 0; j < bks.size(); j++){
            final AffectationDecorator aff = new AffectationDecorator(user, bks.get(j), j == 0, j == bks.size() - 1, bks.size());
            affectationDecorators.add(aff);
         }
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur souhaite modifier une
    * affectation.
    * @param event
    */
   public void onClickEditAffectation(final ForwardEvent event){
      final AffectationDecorator aff = (AffectationDecorator) event.getData();
      // on passe le décorator en éditable et on recharge la grid
      aff.setEdit(true);
      getBinder().loadComponent(affectationsGrid);
   }

   /**
    * Méthode appelée lorsque l'utilisateur valide une modif sur une
    * affectation.
    * @param event
    */

   public void onClickValidateAffectation(final ForwardEvent event){
      // on récupère les donneés : la listbox contenant les imprimantes,
      // celle contenant es modèles et le decorator d'affectation
      final List<Object> datas = new ArrayList<>();
      datas.addAll((List<?>) event.getData());
      final Listbox liImp = (Listbox) datas.get(0);
      final Listbox liMod = (Listbox) datas.get(1);
      final AffectationDecorator aff = (AffectationDecorator) datas.get(2);

      // on récupère l'imprimante sélectionnée
      Imprimante selectedImprimante = null;
      if(liImp.getSelectedItem().getValue() != null){
         selectedImprimante = ManagerLocator.getImprimanteManager()
            .findByNomAndPlateformeManager((String) liImp.getSelectedItem().getValue(), SessionUtils.getPlateforme(sessionScope))
            .get(0);
      }

      // on récupère le modèle sélectionné
      Modele selectedModele = null;
      if(liMod.getSelectedItem().getValue() != null){
         selectedModele = ManagerLocator.getModeleManager()
            .findByNomAndPlateformeManager((String) liMod.getSelectedItem().getValue(), SessionUtils.getPlateforme(sessionScope))
            .get(0);
      }

      try{
         AffectationImprimante ai = null;
         // on cherche si une affectation existait déjà
         final List<AffectationImprimante> liste =
            ManagerLocator.getAffectationImprimanteManager().findByBanqueUtilisateurManager(aff.getBanque(), user);
         if(liste.size() > 0){
            ai = liste.get(0);
         }else{
            ai = new AffectationImprimante();
         }

         // on sauvegarde l'affectation
         ManagerLocator.getAffectationImprimanteManager().createObjectManager(new AffectationImprimante(), user, aff.getBanque(),
            selectedImprimante, selectedModele);

         // l'imprimante faisant partie de la PK, si celle ci a changé
         // et qu'une affectation existait, on la supprime
         if(ai.getImprimante() != null && !ai.getImprimante().equals(selectedImprimante)){
            ManagerLocator.getAffectationImprimanteManager().removeObjectManager(ai);
         }

         // Maj de la grid
         aff.setEdit(false);
         getBinder().loadComponent(affectationsGrid);
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur souhaite supprimer
    * une affectation d'imrpiamnte.
    * @param event
    */
   public void onClickDeleteAffectation(final ForwardEvent event){
      final AffectationDecorator aff = (AffectationDecorator) event.getData();

      AffectationImprimante ai = null;
      // on cherche si une affectation existait
      final List<AffectationImprimante> liste =
         ManagerLocator.getAffectationImprimanteManager().findByBanqueUtilisateurManager(aff.getBanque(), user);
      if(liste.size() > 0){
         ai = liste.get(0);
      }

      // si une affectation était définie
      if(ai != null){
         // confirmation
         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.affectationImprimante")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            // suppression
            ManagerLocator.getAffectationImprimanteManager().removeObjectManager(ai);
            // maj
            getBinder().loadComponent(affectationsGrid);
         }
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur annule une modif sur une
    * affectation.
    * @param event
    */
   public void onClickCancelAffectation(final ForwardEvent event){
      final AffectationDecorator aff = (AffectationDecorator) event.getData();
      // on repasse le decirator en non editable et on recharge
      // la grid
      aff.setEdit(false);
      getBinder().loadComponent(affectationsGrid);
   }

   public void onClick$addAffectations(){
      openAffectationCollectionsWindow();
   }

   /**
    * PopUp window appelée permettre la définition des
    * imprimantes pour chaque collection.
    */
   public void openAffectationCollectionsWindow(){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("affectationCollectionsWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("utilisateur.affectations.modale.title"));
         win.setBorder("normal");
         win.setWidth("500px");
         final int height = 260;
         win.setHeight(String.valueOf(height) + "px");
         win.setClosable(true);

         final HtmlMacroComponent ua;
         ua = (HtmlMacroComponent) page.getComponentDefinition("affectationCollectionsModale", false).newInstance(page, null);
         ua.setParent(win);
         ua.setId("affectationCollectionsModaleComponent");
         ua.applyProperties();
         ua.afterCompose();

         final List<Banque> bks =
            ManagerLocator.getBanqueManager().findByUtilisateurAndPFManager(user, SessionUtils.getPlateforme(sessionScope));
         ((AffectationCollectionsModale) ua.getFellow("fwinAffectationCollectionsModale")
            .getAttributeOrFellow("fwinAffectationCollectionsModale$composer", true)).init(bks, self, user,
               SessionUtils.getPlateforme(sessionScope));
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               //progress.detach();
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e.getMessage(), e); 
         }
      }
   }

   /**
    * Méthode appelée pour mettre à jour les affectations
    * d'imprimantes.
    * @param e
    */
   public void onGetAffectationsDone(final Event e){
      getBinder().loadComponent(affectationsGrid);
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public Utilisateur getUser(){
      return user;
   }

   public void setUser(final Utilisateur u){
      setObject(u);
   }

   public List<ProfilUtilisateur> getProfilUtilisateurs(){
      return profilUtilisateurs;
   }

   public void setProfilUtilisateurs(final List<ProfilUtilisateur> p){
      this.profilUtilisateurs = p;
   }

   /**
    * Formate la valeur du champ Archive.
    * @return Oui ou non.
    */
   public String getArchiveFormated(){

      if(this.user != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.user.isArchive());
      }
      return "";
   }

   /**
    * Formate la valeur du champ super.
    * @return Oui ou non.
    */
   public String getSuperFormated(){

      if(this.user != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.user.isSuperAdmin());
      }
      return "";
   }

   /**
    * Formate la date de création de l'utilisateur.
    * @return Date de création formatée.
    */
   public String getDateCreationFormated(){
      if(this.user != null && !this.user.equals(new Utilisateur())){

         final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(user);

         if(date != null){
            return ObjectTypesFormatters.dateRenderer2(date);
         }
         return null;
      }
      return null;
   }

   /**
    * Formate la date de stockage de l'échantillon.
    * @return Date de stockage formatée.
    */
   public String getTimeoutFormated(){
      if(this.user != null){
         return ObjectTypesFormatters.dateRenderer2(this.user.getTimeOut());
      }
      return null;
   }

   /**
    * Formate le message d'explication sur la durée de validité du MDP.
    * @return
    */
   public String getTimeoutLabelFormated(){
      String value = "";

      if(nbMoisMdp != null && nbMoisMdp > 0){
         value = ObjectTypesFormatters.getLabel("utilisateur.timeout.help", new String[] {String.valueOf(nbMoisMdp)});
      }

      return value;
   }

   /**
    * Formate la valeur du champ Authentification LDAP.
    * @return Oui ou non.
    */
   public String getLdapFormated(){

      String ftdIsLdap = "";

      if(this.user != null){
         ftdIsLdap = ObjectTypesFormatters.booleanLitteralFormatter(this.user.isLdap());
      }

      return ftdIsLdap;

   }

   public String getSClassOperateur(){
      if(this.user != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.user.getCollaborateur());
      }
      return null;
   }

   public List<Plateforme> getPlateformes(){
      return plateformes;
   }

   public String getPlateformesFormated(){
      return plateformesFormated;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selectedC){
      this.selectedCollaborateur = selectedC;
   }

   public List<String> getNomsAndPrenoms(){
      return nomsAndPrenoms;
   }

   public ListModel<Banque> getBanques(){
      return banques;
   }

   public List<Banque> getSelectedBanques(){
      return selectedBanques;
   }

   public void setSelectedBanques(final List<Banque> selected){
      this.selectedBanques = selected;
   }

   public List<Profil> getProfils(){
      return profils;
   }

   public Profil getSelectedProfil(){
      return selectedProfil;
   }

   public void setSelectedProfil(final Profil selected){
      this.selectedProfil = selected;
   }

   public Hashtable<Banque, ProfilUtilisateur> getBanquesDefined(){
      return banquesDefined;
   }

   public void setBanquesDefined(final Hashtable<Banque, ProfilUtilisateur> bDefined){
      this.banquesDefined = bDefined;
   }

   public ConstWord getLoginConstraint(){
      return UtilisateurConstraints.getNomConstraint();
   }

   public ConstWord getLdapConstraint(){
      return UtilisateurConstraints.getLdapConstraint();
   }

   public ConstEmail getEmailConstraint(){
      return UtilisateurConstraints.getEmailConstraint();
   }

   public ConstPassword getPasswordConstraint(){
      return UtilisateurConstraints.getPasswordConstraint();
   }

   public ConstDateLimit getDateConstraint(){
      return UtilisateurConstraints.getDateConstraint();
   }

   private void validateCoherenceDate(final Component comp, final Object value){
      final Date dateValue = (Date) value;
      Errors errs = null;
      final String field = "timeOut";

      if(dateValue == null || dateValue.equals("")){
         // la contrainte est retiree
         //((Datebox) comp).setConstraint("");
         ((Datebox) comp).clearErrorMessage(true);
         ((Datebox) comp).setValue(null);
         if(comp.getId().equals("timeoutBox")){
            this.user.setTimeOut(dateValue);
         }
      }else{
         if(comp.getId().equals("timeoutBox")){
            this.user.setTimeOut(dateValue);
            errs = UtilisateurValidator.checkDateDesactCoherence(user);
         }

         // Si la date n'est pas vide, on applique la contrainte
         if(errs != null && errs.hasErrors()){
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   public void onBlur$timeoutBox(){
      boolean badDateFormat = false;
      if(timeoutBox.getErrorMessage() != null){
         badDateFormat = true;
      }
      if(!badDateFormat){
         timeoutBox.clearErrorMessage(true);
         validateCoherenceDate(timeoutBox, timeoutBox.getValue());
      }
   }

   /**
    * Traite onClick hyperlien nom banque
    * @param event
    * @since 2.1
    */
   public void onClick$banqueNom(final Event event){
      final ProfilUtilisateur pf = (ProfilUtilisateur) event.getData();
      getAdministrationController().selectBanqueInController(pf.getBanque());
   }

   /**
    * Traite onClick hyperlien nom profil
    * @param event
    * @since 2.1
    */
   public void onClick$profilNom(final Event event){
      final ProfilUtilisateur pf = (ProfilUtilisateur) event.getData();
      getAdministrationController().selectProfilInController(pf.getProfil());
   }

   /**
    * @since 2.1
    */
   public void onCheck$banquesArchiveBox(){
      profilUtilisateurs.clear();
      // display non-archived users only
      if(user != null && user.getUtilisateurId() != null){
         if(banquesArchiveBox.isChecked()){
            profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByUtilisateurManager(user, false));
         }else{ // display all of them
            profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByUtilisateurManager(user, null));
         }
      }
   }

   public void onCheck$authLdapCheckbox(){

      //       final boolean createMode = (null == user.getUtilisateurId());
      final boolean ldapUser = authLdapCheckbox.isChecked();

      //Affichage/masquage des lignes du formulaires selon que l'authentification
      //LDAP est activée ou non pour l'utilisateur
      passwordRow.setVisible(!ldapUser);
      // timeoutRow.setVisible(!ldapUser);
      // timeoutHelpRow.setVisible(!ldapUser);

      displayPasswordEdition(getObject().getPassword() == null && !ldapUser);

      //Si création de compte ou bascule d'un utilisateur LDAP existant en non LDAP
      //      if(createMode || user.isLdap()){
      //
      //         passwordLabel.setVisible(false);
      //         passwordBox.setVisible(!ldapUser);
      //         rowConfirmationPassword.setVisible(!ldapUser);
      //         if(user.getTimeOut() == null) {
      //            user.setTimeOut(defaultTimeoutDate);
      //         }
      //
      //         //Suppression/activation des contraintes sur le mot de passe selon que l'authentification
      //         //LDAP est activée ou non pour l'utilisateur
      //         if(ldapUser){
      //            Clients.clearWrongValue(Arrays.asList(passwordBox, confirmPasswordBox));
      //            passwordBox.clearErrorMessage();
      //            confirmPasswordBox.clearErrorMessage();
      //            passwordBox.setConstraint("");
      //            confirmPasswordBox.setConstraint("");
      //         }else{
      //            passwordBox.setConstraint(getPasswordConstraint());
      //            confirmPasswordBox.setConstraint("no empty");
      //         }
      //     }
   }

   @Override
   public void setFieldsToUpperCase(){}

   public boolean isAdminPF(){
      return isAdminPF;
   }

   public void setAdminPF(final boolean isA){
      this.isAdminPF = isA;
   }

   @Override
   public boolean isAdmin(){
      return isAdmin;
   }

   @Override
   public void setAdmin(final boolean isA){
      this.isAdmin = isA;
   }

   public List<ProfilUtilisateur> getProfilUtilisateursOtherBanques(){
      return profilUtilisateursOtherBanques;
   }

   public void setProfilUtilisateursOtherBanques(final List<ProfilUtilisateur> pOtherBanques){
      this.profilUtilisateursOtherBanques = pOtherBanques;
   }

   @Override
   public String getDeleteWaitLabel(){
      if(!isCascadable()){
         return Labels.getLabel("deletion.general.wait");
      }
      return Labels.getLabel("archivage.general.wait");
   }

   public List<AffectationDecorator> getAffectationDecorators(){
      return affectationDecorators;
   }

   public void setAffectationDecorators(final List<AffectationDecorator> a){
      this.affectationDecorators = a;
   }

   public static AffectationRowRenderer getAffectationRenderer(){
      return affectationRenderer;
   }

   public static void setAffectationRenderer(final AffectationRowRenderer a){
      FicheUtilisateur.affectationRenderer = a;
   }

   public Integer getNbMoisMdp(){
      return nbMoisMdp;
   }

   public void setNbMoisMdp(final Integer nb){
      this.nbMoisMdp = nb;
   }

   public boolean isLdapAuthActivated(){
      return Boolean.valueOf(LDAP_AUTHENTICATION.getValue());
   }

   public boolean isAuthLdapRowVisible(){
      return isLdapAuthActivated() || (user != null && user.isLdap());
   }

   /**
    *
    * @since 2.1
    */
   public AdministrationController getAdministrationController(){
      return (AdministrationController) self.getParent().getParent().getParent().getParent().getParent().getParent().getParent()
         .getParent()
         //.getParent()
         .getAttributeOrFellow("winAdministration$composer", true);
   }

   /**
    * Affiche ou non les champs dédiés à l'initialisation d'un mot de passe
    * (et sa confirmation) pour un compte à authentification locale
    * @param display
    * @since 2.2.1
    */
   private void displayPasswordEdition(final boolean display){
      if(display){

         passwordLabel.setVisible(false);
         passwordBox.setVisible(true);
         rowConfirmationPassword.setVisible(true);

         // on ajoute les contraintes sur les
         // mots de passe
         passwordBox.setMaxlength(20);
         passwordBox.setConstraint(getPasswordConstraint());
         confirmPasswordBox.setMaxlength(20);
         confirmPasswordBox.setConstraint("no empty");
      }else{
         passwordLabel.setVisible(true);
         passwordBox.setVisible(false);
         rowConfirmationPassword.setVisible(false);

         Clients.clearWrongValue(Arrays.asList(passwordBox, confirmPasswordBox));
         passwordBox.clearErrorMessage();
         confirmPasswordBox.clearErrorMessage();

         passwordBox.setConstraint("");
         confirmPasswordBox.setConstraint("");
      }
   }

}