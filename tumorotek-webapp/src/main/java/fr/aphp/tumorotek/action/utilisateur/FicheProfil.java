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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.AdministrationController;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.webapp.general.ExportUtils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.2.3-rc1
 * @author Mathieu BARTHELEMY
 *
 */
public class FicheProfil extends AbstractFicheCombineController
{

   private static final Logger log = LoggerFactory.getLogger(FicheProfil.class);

   private static final long serialVersionUID = 5480071795760704640L;

   // Static Components pour le mode static.
   private Label nomLabel;

   private Label anonymeLabel;

   private Label accesAdministrationLabel;

   private Label exportLabel;

   private Menubar menuBar;

   // composants pour les droits sur les objets
   private Component[] droitLabelsComponents;

   private Label consultationPatientLabel;

   private Label creationPatientLabel;

   private Label modificationPatientLabel;

   private Label suppressionPatientLabel;

   private Label consultationPrelevementLabel;

   private Label creationPrelevementLabel;

   private Label modificationPrelevementLabel;

   private Label suppressionPrelevementLabel;

   private Label consultationEchantillonLabel;

   private Label creationEchantillonLabel;

   private Label modificationEchantillonLabel;

   private Label suppressionEchantillonLabel;

   private Label consultationProdDeriveLabel;

   private Label creationProdDeriveLabel;

   private Label modificationProdDeriveLabel;

   private Label suppressionProdDeriveLabel;

   private Label consultationCessionLabel;

   private Label creationCessionLabel;

   private Label modificationCessionLabel;

   private Label suppressionCessionLabel;

   private Label consultationStockageLabel;

   private Label creationStockageLabel;

   private Label modificationStockageLabel;

   private Label suppressionStockageLabel;

   private Label consultationAnnotationLabel;

   private Label creationAnnotationLabel;

   private Label modificationAnnotationLabel;

   private Label suppressionAnnotationLabel;

   private Label consultationCollaborateurLabel;

   private Label creationCollaborateurLabel;

   private Label modificationCollaborateurLabel;

   private Label suppressionCollaborateurLabel;

   private Label consultationRequeteLabel;

   private Label creationRequeteLabel;

   private Label modificationRequeteLabel;

   private Label suppressionRequeteLabel;

   private Label archiveLabel;

   // Editable components : mode d'édition ou de création.
   private Label nomRequired;

   private Textbox nomBox;

   private Checkbox archiveBox;

   private Checkbox anonymeBox;

   private Checkbox accesAdministrationBox;

   private Listbox exportListBox;

   private Label exportAttentionLabel;

   // composants pour les droits sur les objets
   private Component[] droitBoxComponents;

   private Row rowCocher;

   private Checkbox consultationPatientBox;

   private Checkbox creationPatientBox;

   private Checkbox modificationPatientBox;

   private Checkbox suppressionPatientBox;

   private Checkbox modifMultipliePatientBox;

   private Checkbox annotationPatientBox;

   private Checkbox consultationPrelevementBox;

   private Checkbox creationPrelevementBox;

   private Checkbox modificationPrelevementBox;

   private Checkbox suppressionPrelevementBox;

   private Checkbox modifMultipliePrelevementBox;

   private Checkbox annotationPrelevementBox;

   private Checkbox consultationEchantillonBox;

   private Checkbox creationEchantillonBox;

   private Checkbox modificationEchantillonBox;

   private Checkbox suppressionEchantillonBox;

   private Checkbox modifMultiplieEchantillonBox;

   private Checkbox annotationEchantillonBox;

   private Checkbox consultationProdDeriveBox;

   private Checkbox creationProdDeriveBox;

   private Checkbox modificationProdDeriveBox;

   private Checkbox suppressionProdDeriveBox;

   private Checkbox modifMultiplieProdDeriveBox;

   private Checkbox annotationProdDeriveBox;

   private Checkbox consultationCessionBox;

   private Checkbox creationCessionBox;

   private Checkbox modificationCessionBox;

   private Checkbox suppressionCessionBox;

   private Checkbox modifMultiplieCessionBox;

   private Checkbox annotationCessionBox;

   private Checkbox consultationStockageBox;

   private Checkbox creationStockageBox;

   private Checkbox modificationStockageBox;

   private Checkbox suppressionStockageBox;

   private Checkbox consultationCollaborateurBox;

   private Checkbox creationCollaborateurBox;

   private Checkbox modificationCollaborateurBox;

   private Checkbox suppressionCollaborateurBox;

   private Checkbox consultationRequeteBox;

   private Checkbox creationRequeteBox;

   private Checkbox modificationRequeteBox;

   private Checkbox suppressionRequeteBox;

   // utilisateurs
   // @since 2.1
   private Group groupProfilUtilisateurs;

   private Grid gridProfilUtilisateur;

   private Checkbox utilisateursArchiveBox;

   // Objets Principaux.
   private Profil profil;

   private Hashtable<Checkbox, List<Checkbox>> rowsCheckBoxs = new Hashtable<>();

   private Hashtable<Checkbox, List<Checkbox>> annonymesRowsCheckBoxs = new Hashtable<>();

   // Associations.
   private java.util.Hashtable<String, Entite> entites = new java.util.Hashtable<>();

   private java.util.Hashtable<String, OperationType> operationTypes = new java.util.Hashtable<>();

   private List<DroitObjet> droitsObjets = new ArrayList<>();

   // @since 2.2.3-rc1 utilisation Enum avec ajout anonyme + stock
   private ProfilExport selectedExport = ProfilExport.ANONYMESTOCK; // defaut

   // utilisateurs
   // @since 2.1
   private final List<ProfilUtilisateur> profilUtilisateurs = new ArrayList<>();

   private final ProfilUtilisateurRowRenderer profilUtilisateurRowRenderer = new ProfilUtilisateurRowRenderer(true);

   /**
    * @version 2.1
    * @param comp
    * @throws Exception
    */
   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.profil");
      setCascadable(false);
      setFantomable(false);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.nomLabel, this.anonymeLabel, this.accesAdministrationLabel, this.exportLabel,
         this.menuBar, this.archiveLabel});

      setObjBoxsComponents(new Component[] {this.nomBox, this.anonymeBox, this.accesAdministrationBox, this.rowCocher,
         this.exportListBox, this.exportAttentionLabel, this.archiveBox});

      setRequiredMarks(new Component[] {this.nomRequired});

      this.droitLabelsComponents = new Component[] {this.consultationPatientLabel, this.creationPatientLabel,
         this.modificationPatientLabel, this.suppressionPatientLabel, this.consultationPrelevementLabel,
         this.creationPrelevementLabel, this.modificationPrelevementLabel, this.suppressionPrelevementLabel,
         this.consultationEchantillonLabel, this.creationEchantillonLabel, this.modificationEchantillonLabel,
         this.suppressionEchantillonLabel, this.consultationProdDeriveLabel, this.creationProdDeriveLabel,
         this.modificationProdDeriveLabel, this.suppressionProdDeriveLabel, this.consultationCessionLabel,
         this.creationCessionLabel, this.modificationCessionLabel, this.suppressionCessionLabel, this.consultationStockageLabel,
         this.creationStockageLabel, this.modificationStockageLabel, this.suppressionStockageLabel,
         this.consultationAnnotationLabel, this.creationAnnotationLabel, this.modificationAnnotationLabel,
         this.suppressionAnnotationLabel, this.consultationCollaborateurLabel, this.creationCollaborateurLabel,
         this.modificationCollaborateurLabel, this.suppressionCollaborateurLabel, this.consultationRequeteLabel,
         this.creationRequeteLabel, this.modificationRequeteLabel, this.suppressionRequeteLabel};

      this.droitBoxComponents = new Component[] {this.consultationPatientBox, this.creationPatientBox,
         this.modificationPatientBox, this.suppressionPatientBox, this.modifMultipliePatientBox, this.annotationPatientBox,
         this.consultationPrelevementBox, this.creationPrelevementBox, this.modificationPrelevementBox,
         this.suppressionPrelevementBox, this.modifMultipliePrelevementBox, this.annotationPrelevementBox,
         this.consultationEchantillonBox, this.creationEchantillonBox, this.modificationEchantillonBox,
         this.suppressionEchantillonBox, this.modifMultiplieEchantillonBox, this.annotationEchantillonBox,
         this.consultationProdDeriveBox, this.creationProdDeriveBox, this.modificationProdDeriveBox,
         this.suppressionProdDeriveBox, this.modifMultiplieProdDeriveBox, this.annotationProdDeriveBox,
         this.consultationCessionBox, this.creationCessionBox, this.modificationCessionBox, this.suppressionCessionBox,
         this.modifMultiplieCessionBox, this.annotationCessionBox, this.consultationStockageBox, this.creationStockageBox,
         this.modificationStockageBox, this.suppressionStockageBox, this.consultationCollaborateurBox,
         this.creationCollaborateurBox, this.modificationCollaborateurBox, this.suppressionCollaborateurBox,
         this.consultationRequeteBox, this.creationRequeteBox, this.modificationRequeteBox, this.suppressionRequeteBox};

      drawActionsForProfil();

      initHashTables();

      menuBar.setVisible(false);

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();

   }

   @Override
   public void setObject(final TKdataObject obj){
      this.profil = (Profil) obj;

      droitsObjets.clear();

      if(profil != null && profil.getProfilId() != null){
         droitsObjets = ManagerLocator.getDroitObjetManager().findByProfilManager(profil);

         // gestion de l'export
         if(this.profil.getAdmin() != null && this.profil.getAdmin()){
            selectedExport = ProfilExport.NOMINATIF;
         }else if(this.profil.getProfilExport() != null){
            selectedExport = ExportUtils.getProfilExportFromValue(this.profil.getProfilExport());
         }else{
            selectedExport = ProfilExport.NO;
         }
      }else{
         selectedExport = ProfilExport.NO;
      }

      initAssociations();

      super.setObject(profil);
   }

   /**
    * @since 2.1
    */
   private void initAssociations(){
      profilUtilisateurs.clear();
      if(profil.getProfilId() != null){
         utilisateursArchiveBox.setChecked(true);
         profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByProfilManager(profil, false));
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.profil.clone());
   }

   @Override
   public Profil getObject(){
      return this.profil;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public ProfilController getObjectTabController(){
      return (ProfilController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Profil());
      super.setNewObject();
   }

   /**
    * @version 2.1
    */
   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      for(int i = 0; i < droitBoxComponents.length; i++){
         ((Checkbox) droitBoxComponents[i]).setDisabled(false);
      }

      groupProfilUtilisateurs.setVisible(false);
      gridProfilUtilisateur.setVisible(false);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();
      initDroitsCheckboxs();

      initRowsCheckBoxs();

      getBinder().loadComponent(self);

   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.profil.equals(new Profil()));

      groupProfilUtilisateurs.setVisible(true);
      gridProfilUtilisateur.setVisible(true);

      if(this.profil != null && this.profil.getAdmin() != null && this.profil.getAdmin()){
         editC.setVisible(false);
         deleteC.setVisible(false);
      }

      for(int i = 0; i < droitBoxComponents.length; i++){
         ((Checkbox) droitBoxComponents[i]).setDisabled(true);
      }

      if(this.profil.getProfilId() == null){
         menuBar.setVisible(false);
      }

      //initDroitsLabels();
      initDroitsCheckboxs();

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      // Initialisation du mode (listes, valeurs...)
      initEditableMode();
      initDroitsCheckboxs();

      for(int i = 0; i < droitBoxComponents.length; i++){
         ((Checkbox) droitBoxComponents[i]).setDisabled(false);
      }

      super.switchToEditMode();

      if(!this.profil.getAnonyme()){
         initRowsCheckBoxs();
         accesAdministrationBox.setDisabled(false);
      }else{
         initRowsCheckBoxs();
         final List<Checkbox> checkBoxToEnable = new ArrayList<>();
         for(int i = 0; i < droitBoxComponents.length; i++){
            // on extrait l'operation gérée par le label : son nom
            // contenu dans le custom-attribute "operation"
            final OperationType type = operationTypes.get(droitBoxComponents[i].getAttribute("operation"));
            final Entite entite = entites.get(droitBoxComponents[i].getAttribute("entite"));

            if(!entite.getNom().equals("Stockage") && !entite.getNom().equals("Requete")
               && !entite.getNom().equals("Collaborateur")){

               if(type.getNom().equals("Consultation")){
                  final Checkbox box = (Checkbox) droitBoxComponents[i];
                  box.setDisabled(false);

                  if(annonymesRowsCheckBoxs.containsKey(box)){
                     final List<Checkbox> boxs = annonymesRowsCheckBoxs.get(box);

                     for(int j = 0; j < boxs.size(); j++){
                        if(box.isChecked()){
                           checkBoxToEnable.add(boxs.get(j));
                        }
                     }
                  }
               }else{
                  ((Checkbox) droitBoxComponents[i]).setDisabled(true);
               }
            }else{
               ((Checkbox) droitBoxComponents[i]).setDisabled(true);
               ((Checkbox) droitBoxComponents[i]).setChecked(false);
            }
         }

         for(int i = 0; i < checkBoxToEnable.size(); i++){
            checkBoxToEnable.get(i).setDisabled(false);
         }
         accesAdministrationBox.setDisabled(true);
      }

      getBinder().loadComponent(self);

   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   /**
    * @version 2.1
    */
   @Override
   public void createNewObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      this.profil.setAnonyme(anonymeBox.isChecked());
      this.profil.setAdmin(false);
      this.profil.setAccesAdministration(accesAdministrationBox.isChecked());

      // on extrait les droits cochés
      final List<DroitObjet> newDroits = extractDroitObjets();

      // update de l'objet
      ManagerLocator.getProfilManager().createObjectManager(profil, newDroits, SessionUtils.getLoggedUser(sessionScope),
         SessionUtils.getCurrentPlateforme());

      setDroitsObjets(newDroits);

      if(getListeProfil() != null){
         // ajout de l'utilisateur à la liste
         getListeProfil().addToObjectList(profil);
      }

   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeProfil
    */
   private ListeProfil getListeProfil(){
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
      Clients.showBusy(Labels.getLabel("profil.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
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
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getProfilManager().isUsedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isUsed){
         setDeleteMessage(Labels.getLabel("profil.deletion.isUsed"));
      }
      setDeletable(!isUsed);
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getProfilManager().removeObjectManager(getObject());
   }

   //	@Override
   //	public void onLaterDelete(Event event) {
   //		try {
   //			removeObject((String) event.getData());
   //
   //			// on vérifie que l'on retrouve bien la page
   //			// contenant la liste des profils
   //			if (getListeProfil() != null) {
   //				// on enlève le profil de la liste
   //				getListeProfil().
   //					removeFromObjectList(this.profil);
   //			}
   //
   //			// maj de l'utilisateur sélectionné
   //			if (getListeUtilisateur() != null) {
   //				getListeUtilisateur().updateCurrentObject();
   //			}
   //			// clear form
   //			clearData();
   //		} catch (RuntimeException re) {
   //			// ferme wait message
   //			Clients.showBusy(null, false);
   //			try {
   //				Messagebox.show(handleExceptionMessage(re),
   //						"Error", Messagebox.OK, Messagebox.ERROR);
   //			} catch (InterruptedException e) {
    //        log.error(e.getMessage(), e);
   //			}
   //		} finally {
   //			// ferme wait message
   //			Clients.showBusy(null, false);
   //		}
   //	}

   @Override
   public void onClick$editC(){
      if(this.profil != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){
      Clients.showBusy(Labels.getLabel("profil.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
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
   public void setEmptyToNulls(){}

   @Override
   public void updateObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      this.profil.setAnonyme(anonymeBox.isChecked());
      this.profil.setAdmin(false);
      this.profil.setAccesAdministration(accesAdministrationBox.isChecked());

      // on extrait les droits cochés
      final List<DroitObjet> newDroits = extractDroitObjets();

      // update de l'objet
      ManagerLocator.getProfilManager().updateObjectManager(profil, newDroits, SessionUtils.getLoggedUser(sessionScope));

      setDroitsObjets(newDroits);

      if(getListeProfil() != null){
         // ajout de l'utilisateur à la liste
         getListeProfil().updateObjectGridList(this.profil);
      }
   }

   /**
    * Clic sur le lien cocherTous : coche toutes les checkboxs.
    */
   public void onClick$cocherTous(){
      for(int i = 0; i < droitBoxComponents.length; i++){
         ((Checkbox) droitBoxComponents[i]).setChecked(true);
         ((Checkbox) droitBoxComponents[i]).setDisabled(false);
      }
   }

   /**
    * Clic sur le lien decocherTous : décoche toutes les checkboxs.
    */
   public void onClick$decocherTous(){
      for(int i = 0; i < droitBoxComponents.length; i++){
         ((Checkbox) droitBoxComponents[i]).setChecked(false);

         if(!rowsCheckBoxs.containsKey(droitBoxComponents[i])){
            ((Checkbox) droitBoxComponents[i]).setDisabled(true);
         }
      }
   }

   /**
    * Sélection de l'export.
    * @param event Event : sélection sur la liste exportListBox.
    * @throws Exception
    */
   public void onSelect$exportListBox(final Event event) throws Exception{
      if(exportListBox.getSelectedItem() != null){
         selectedExport = exportListBox.getSelectedItem().getValue();
      }
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){
      if(this.profil.getAnonyme() != null){
         anonymeBox.setChecked(this.profil.getAnonyme());
      }else{
         anonymeBox.setChecked(false);
      }

      if(this.profil.getAccesAdministration() != null){
         accesAdministrationBox.setChecked(this.profil.getAccesAdministration());
      }else{
         accesAdministrationBox.setChecked(false);
      }

      initAssociations();

   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(anonymeBox);
      Clients.clearWrongValue(accesAdministrationBox);
   }

   /**
    * Init le contenu des hashtables.
    */
   public void initHashTables(){
      entites = new java.util.Hashtable<>();
      // entité Patient
      entites.put("Patient", ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0));
      // entité Prelevement
      entites.put("Prelevement", ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0));
      // entité Echantillon
      entites.put("Echantillon", ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
      // entité ProdDerive
      entites.put("ProdDerive", ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
      // entité Cession
      entites.put("Cession", ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0));
      // entité Stockage
      entites.put("Stockage", ManagerLocator.getEntiteManager().findByNomManager("Stockage").get(0));
      // entité Collaborateur
      entites.put("Collaborateur", ManagerLocator.getEntiteManager().findByNomManager("Collaborateur").get(0));
      // entité Requete
      entites.put("Requete", ManagerLocator.getEntiteManager().findByNomManager("Requete").get(0));

      operationTypes = new java.util.Hashtable<>();
      // operation Consultation
      operationTypes.put("Consultation",
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("Consultation", true).get(0));
      // operation Creation
      operationTypes.put("Creation", ManagerLocator.getOperationTypeManager().findByNomLikeManager("Creation", true).get(0));
      // operation Modification
      operationTypes.put("Modification",
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("Modification", true).get(0));
      // operation Archivage
      operationTypes.put("Archivage", ManagerLocator.getOperationTypeManager().findByNomLikeManager("Archivage", true).get(0));
      // operation ModifMultiple
      operationTypes.put("ModifMultiple",
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("ModifMultiple", true).get(0));
      // operation Annotation
      operationTypes.put("Annotation", ManagerLocator.getOperationTypeManager().findByNomLikeManager("Annotation", true).get(0));
      // operation Import
      operationTypes.put("Import", ManagerLocator.getOperationTypeManager().findByNomLikeManager("Import", true).get(0));
      // operation Export
      operationTypes.put("Export", ManagerLocator.getOperationTypeManager().findByNomLikeManager("Export", true).get(0));
      // operation ExportAnonyme
      operationTypes.put("ExportAnonyme",
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("ExportAnonyme", true).get(0));
   }

   /**
    * Init la valeur des labels contenant les droits.
    */
   public void initDroitsLabels(){
      // pour tous les labels pour les droits
      for(int i = 0; i < droitLabelsComponents.length; i++){
         // si ce n'est pas un nouveau profil
         if(!this.profil.equals(new Profil())){
            // on extrait l'entité gérée par le label : son nom
            // contenu dans le custom-attribute "entite"
            final Entite entite = entites.get(droitLabelsComponents[i].getAttribute("entite"));
            // on extrait l'operation gérée par le label : son nom
            // contenu dans le custom-attribute "operation"
            final OperationType type = operationTypes.get(droitLabelsComponents[i].getAttribute("operation"));

            // on construit un DroitObjet à partir de l'entité et
            // de l'operation que l'on vient d'extraire
            final DroitObjet droit = new DroitObjet();
            droit.setProfil(profil);
            droit.setEntite(entite);
            droit.setOperationType(type);

            // si ce DroitObjet se trouve dans la base, on affiche Oui,
            // sinon on affiche Non
            ((Label) droitLabelsComponents[i])
               .setValue(ObjectTypesFormatters.booleanLitteralFormatter(droitsObjets.contains(droit)));
         }else{
            ((Label) droitLabelsComponents[i]).setValue("");
         }
      }

   }

   /**
    * Init les checkboxs contenant les droits.
    */
   public void initDroitsCheckboxs(){
      // pour toutes les checkboxs pour les droits
      for(int i = 0; i < droitBoxComponents.length; i++){
         // si ce n'est pas un nouveau profil
         if(!this.profil.equals(new Profil())){
            // si le compte est admin, tout est checké
            if(this.profil.getAdmin() != null && this.profil.getAdmin()){
               ((Checkbox) droitBoxComponents[i]).setChecked(true);
            }else{
               // on extrait l'entité gérée par le label : son nom
               // contenu dans le custom-attribute "entite"
               final Entite entite = entites.get(droitBoxComponents[i].getAttribute("entite"));
               // on extrait l'operation gérée par le label : son nom
               // contenu dans le custom-attribute "operation"
               final OperationType type = operationTypes.get(droitBoxComponents[i].getAttribute("operation"));

               // on construit un DroitObjet à partir de l'entité et
               // de l'operation que l'on vient d'extraire
               final DroitObjet droit = new DroitObjet();
               droit.setProfil(profil);
               droit.setEntite(entite);
               droit.setOperationType(type);

               // si ce DroitObjet se trouve dans la base, on coche la
               // checkbox, sinonn non
               ((Checkbox) droitBoxComponents[i]).setChecked(droitsObjets.contains(droit));
            }
         }else{
            ((Checkbox) droitBoxComponents[i]).setChecked(false);
         }
      }

   }

   public List<DroitObjet> extractDroitObjets(){
      final List<DroitObjet> newDroits = new ArrayList<>();
      selectedExport = exportListBox.getSelectedItem().getValue();

      // pour toutes les checkboxs pour les droits
      for(int i = 0; i < droitBoxComponents.length; i++){
         final Checkbox check = (Checkbox) droitBoxComponents[i];

         if(check.isChecked()){
            // on extrait l'entité gérée par le label : son nom
            // contenu dans le custom-attribute "entite"
            final Entite entite = entites.get(check.getAttribute("entite"));
            // on extrait l'operation gérée par le label : son nom
            // contenu dans le custom-attribute "operation"
            final OperationType type = operationTypes.get(check.getAttribute("operation"));

            // on construit un DroitObjet à partir de l'entité et
            // de l'operation que l'on vient d'extraire
            final DroitObjet droit = new DroitObjet();
            droit.setProfil(profil);
            droit.setEntite(entite);
            droit.setOperationType(type);

            newDroits.add(droit);
         }
      }

      // gestion de l'export
      this.profil.setProfilExport(selectedExport.getValue());

      return newDroits;
   }

   public void initRowsCheckBoxs(){
      List<Checkbox> list = new ArrayList<>();

      // ligne des patients
      list.add(creationPatientBox);
      list.add(modificationPatientBox);
      list.add(suppressionPatientBox);
      list.add(modifMultipliePatientBox);
      list.add(annotationPatientBox);
      rowsCheckBoxs.put(consultationPatientBox, list);
      if(!consultationPatientBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des prelevements
      list = new ArrayList<>();
      list.add(creationPrelevementBox);
      list.add(modificationPrelevementBox);
      list.add(suppressionPrelevementBox);
      list.add(modifMultipliePrelevementBox);
      list.add(annotationPrelevementBox);
      rowsCheckBoxs.put(consultationPrelevementBox, list);
      if(!consultationPrelevementBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des echantillons
      list = new ArrayList<>();
      list.add(creationEchantillonBox);
      list.add(modificationEchantillonBox);
      list.add(suppressionEchantillonBox);
      list.add(modifMultiplieEchantillonBox);
      list.add(annotationEchantillonBox);
      rowsCheckBoxs.put(consultationEchantillonBox, list);
      if(!consultationEchantillonBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des ProdDerive
      list = new ArrayList<>();
      list.add(creationProdDeriveBox);
      list.add(modificationProdDeriveBox);
      list.add(suppressionProdDeriveBox);
      list.add(modifMultiplieProdDeriveBox);
      list.add(annotationProdDeriveBox);
      rowsCheckBoxs.put(consultationProdDeriveBox, list);
      if(!consultationProdDeriveBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des Cession
      list = new ArrayList<>();
      list.add(creationCessionBox);
      list.add(modificationCessionBox);
      list.add(suppressionCessionBox);
      list.add(modifMultiplieCessionBox);
      list.add(annotationCessionBox);
      rowsCheckBoxs.put(consultationCessionBox, list);
      if(!consultationCessionBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des Stockage
      list = new ArrayList<>();
      list.add(creationStockageBox);
      list.add(modificationStockageBox);
      list.add(suppressionStockageBox);
      rowsCheckBoxs.put(consultationStockageBox, list);
      if(!consultationStockageBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des Collaborateur
      list = new ArrayList<>();
      list.add(creationCollaborateurBox);
      list.add(modificationCollaborateurBox);
      list.add(suppressionCollaborateurBox);
      rowsCheckBoxs.put(consultationCollaborateurBox, list);
      if(!consultationCollaborateurBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // ligne des Recherche
      list = new ArrayList<>();
      list.add(creationRequeteBox);
      list.add(modificationRequeteBox);
      list.add(suppressionRequeteBox);
      rowsCheckBoxs.put(consultationRequeteBox, list);
      if(!consultationRequeteBox.isChecked()){
         for(int i = 0; i < list.size(); i++){
            list.get(i).setDisabled(true);
         }
      }

      // gestion de l'annonymisation
      list = new ArrayList<>();
      annonymesRowsCheckBoxs.put(consultationPatientBox, new ArrayList<>(list));

      list = new ArrayList<>();
      list.add(modificationPrelevementBox);
      list.add(annotationPrelevementBox);
      annonymesRowsCheckBoxs.put(consultationPrelevementBox, new ArrayList<>(list));

      list = new ArrayList<>();
      list.add(modificationEchantillonBox);
      list.add(annotationEchantillonBox);
      annonymesRowsCheckBoxs.put(consultationEchantillonBox, new ArrayList<>(list));

      list = new ArrayList<>();
      list.add(modificationProdDeriveBox);
      list.add(annotationProdDeriveBox);
      annonymesRowsCheckBoxs.put(consultationProdDeriveBox, new ArrayList<>(list));

      list = new ArrayList<>();
      list.add(modificationCessionBox);
      list.add(annotationCessionBox);
      annonymesRowsCheckBoxs.put(consultationCessionBox, new ArrayList<>(list));
   }

   public void onCheckItem(final ForwardEvent e){
      final Event first = e.getOrigin();
      final Checkbox box = (Checkbox) first.getTarget();

      if(rowsCheckBoxs.containsKey(box)){

         List<Checkbox> boxs = new ArrayList<>();
         if(!anonymeBox.isChecked()){
            boxs = rowsCheckBoxs.get(box);
         }else{
            boxs = annonymesRowsCheckBoxs.get(box);
         }

         for(int i = 0; i < boxs.size(); i++){
            if(box.isChecked()){
               boxs.get(i).setDisabled(false);
            }else{
               boxs.get(i).setChecked(false);
               boxs.get(i).setDisabled(true);
            }
         }
      }
   }

   /**
    * Clic sur la CheckBox gérant l'anonymisation des patients.
    */
   public void onCheck$anonymeBox(){
      final List<Checkbox> checkBoxToEnable = new ArrayList<>();
      // on parcourt toutes les checkboxs
      for(int i = 0; i < droitBoxComponents.length; i++){

         // on extrait l'operation gérée par le label : son nom
         // contenu dans le custom-attribute "operation"
         final OperationType type = operationTypes.get(droitBoxComponents[i].getAttribute("operation"));

         if(anonymeBox.isChecked()){
            // on extrait l'entité gérée par le label : son nom
            // contenu dans le custom-attribute "entite"
            final Entite entite = entites.get(droitBoxComponents[i].getAttribute("entite"));

            // si anonyme, seule la consultation est autorisée
            // sauf pour le stockage, la recherche et les
            // collaborations
            if(!entite.getNom().equals("Stockage") && !entite.getNom().equals("Requete")
               && !entite.getNom().equals("Collaborateur")){

               if(type.getNom().equals("Consultation")){
                  final Checkbox box = (Checkbox) droitBoxComponents[i];
                  box.setDisabled(false);

                  if(annonymesRowsCheckBoxs.containsKey(box)){
                     final List<Checkbox> boxs = annonymesRowsCheckBoxs.get(box);

                     for(int j = 0; j < boxs.size(); j++){
                        if(box.isChecked()){
                           checkBoxToEnable.add(boxs.get(j));
                        }
                     }

                     final List<Checkbox> boxsBis = rowsCheckBoxs.get(box);
                     for(int j = 0; j < boxsBis.size(); j++){
                        if(!checkBoxToEnable.contains(boxsBis.get(j))){
                           boxsBis.get(j).setChecked(false);
                        }
                     }
                  }
               }else{
                  ((Checkbox) droitBoxComponents[i]).setDisabled(true);
               }
            }else{
               ((Checkbox) droitBoxComponents[i]).setDisabled(true);
               ((Checkbox) droitBoxComponents[i]).setChecked(false);
            }
         }else{
            if(type.getNom().equals("Consultation")){
               final Checkbox box = (Checkbox) droitBoxComponents[i];
               box.setDisabled(false);

               if(rowsCheckBoxs.containsKey(box)){
                  final List<Checkbox> boxs = rowsCheckBoxs.get(box);

                  for(int j = 0; j < boxs.size(); j++){
                     if(box.isChecked()){
                        boxs.get(j).setDisabled(false);
                     }else{
                        boxs.get(j).setChecked(false);
                        boxs.get(j).setDisabled(true);
                     }
                  }
               }

            }
         }
      }

      for(int i = 0; i < checkBoxToEnable.size(); i++){
         checkBoxToEnable.get(i).setDisabled(false);
      }

      // si anonyme on bloque l'import et l'accès à l'administration
      if(anonymeBox.isChecked()){
         accesAdministrationBox.setChecked(false);
         accesAdministrationBox.setDisabled(true);
      }else{
         accesAdministrationBox.setDisabled(false);
      }
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForProfil(){
      boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }

      // si l'utilisateur est admin PF => boutons cliquables
      if(admin){
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
         setCanSeeHistorique(true);
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
         setCanSeeHistorique(false);
      }
   }

   @Override
   public void disableToolBar(final boolean b){
      if(sessionScope.containsKey("Admin") && (Boolean) sessionScope.get("Admin")){
         super.disableToolBar(false);
      }else{
         super.disableToolBar(true);
      }
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public Profil getProfil(){
      return profil;
   }

   public void setProfil(final Profil p){
      setObject(p);
   }

   /**
    * Formate la valeur du champ anonyme.
    * @return Oui ou non.
    */
   public String getAnonymeFormated(){

      if(this.profil != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.profil.getAnonyme());
      }
      return "";
   }

   /**
    * Formate la valeur du champ accesAdministration.
    * @return Oui ou non.
    */
   public String getAccesAdministrationFormated(){

      if(this.profil != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.profil.getAccesAdministration());
      }
      return "";
   }

   /**
    * Formate la valeur du champ import.
    * @return Oui ou non.
    */
   public String getImportFormated(){

      boolean canImport = false;

      if(this.profil != null && this.profil.getProfilId() != null){
         if(this.profil.getAdmin() != null && this.profil.getAdmin()){
            canImport = this.profil.getAdmin();
         }else if(!profil.getAccesAdministration()){
            canImport = false;
         }else{
            canImport = hasAllCreationRights(getObject());
         }
      }

      return ObjectTypesFormatters.booleanLitteralFormatter(canImport);
   }

   /**
    * Formate la valeur du champ export.
    * @return Oui ou non.
    */
   public String getExportFormated(){

      if(this.profil != null && this.profil.getProfilId() != null){
         if(this.profil.getAdmin() != null && this.profil.getAdmin()){
            return ObjectTypesFormatters.booleanLitteralFormatter(true);
         }
         final OperationType exportOp = operationTypes.get("Export");
         final List<DroitObjet> list = ManagerLocator.getDroitObjetManager().findByProfilOperationManager(profil, exportOp);

         return ObjectTypesFormatters.booleanLitteralFormatter(list.size() == 6);
      }
      return "";
   }

   public java.util.Hashtable<String, Entite> getEntites(){
      return entites;
   }

   public void setEntites(final java.util.Hashtable<String, Entite> e){
      this.entites = e;
   }

   public java.util.Hashtable<String, OperationType> getOperationTypes(){
      return operationTypes;
   }

   public void setOperationTypes(final java.util.Hashtable<String, OperationType> oTypes){
      this.operationTypes = oTypes;
   }

   public List<DroitObjet> getDroitsObjets(){
      return droitsObjets;
   }

   public void setDroitsObjets(final List<DroitObjet> dObjets){
      this.droitsObjets = dObjets;
   }

   public Hashtable<Checkbox, List<Checkbox>> getRowsCheckBoxs(){
      return rowsCheckBoxs;
   }

   public void setRowsCheckBoxs(final Hashtable<Checkbox, List<Checkbox>> r){
      this.rowsCheckBoxs = r;
   }

   public ConstWord getNomConstraint(){
      return UtilisateurConstraints.getNomConstraint();
   }

   /**
    * @since 2.2.3-rc1
    * @return le libellé ILN en fonction du profil d'export
    */
   public String getSelectedExportTranslated(){
      return translateProfilExportEnum(selectedExport);
   }

   private String translateProfilExportEnum(final ProfilExport _p){
      if(_p != null){
         switch(_p.getValue()){
            case 0:
               return Labels.getLabel("profil.droit.export.non");

            case 1:
               return Labels.getLabel("profil.droit.export.anonyme");

            case 3:
               return Labels.getLabel("profil.droit.export.anonymestock");
            case 2:
               return Labels.getLabel("profil.droit.export.nominatif");
         }
      }
      return "undefined";
   }

   public ListitemRenderer<ProfilExport> getProfilExportRenderer(){
      return (li, profilExport, index) -> {
         li.setLabel(translateProfilExportEnum(profilExport));
         li.setValue(profilExport);
      };
   }

   public List<ProfilExport> getProfilExportsModel(){
      return Arrays.asList(ProfilExport.values());
   }

   public ProfilExport getSelectedExport(){
      return this.selectedExport;
   }

   public void setSelectedExport(final ProfilExport _p){
      this.selectedExport = _p;
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }

   public Hashtable<Checkbox, List<Checkbox>> getAnnonymesRowsCheckBoxs(){
      return annonymesRowsCheckBoxs;
   }

   public void setAnnonymesRowsCheckBoxs(final Hashtable<Checkbox, List<Checkbox>> a){
      this.annonymesRowsCheckBoxs = a;
   }

   /**
    * Formate la valeur du champ Archive.
    * @return Oui ou non.
    * @since 2.1
    */
   public String getArchiveFormated(){

      if(this.profil != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.profil.isArchive());
      }
      return "";
   }

   public List<ProfilUtilisateur> getProfilUtilisateurs(){
      return profilUtilisateurs;
   }

   public ProfilUtilisateurRowRenderer getProfilUtilisateurRowRenderer(){
      return profilUtilisateurRowRenderer;
   }

   /**
    * @since 2.1
    */
   public void onCheck$utilisateursArchiveBox(){
      profilUtilisateurs.clear();
      // display non-archived users only
      if(profil != null && profil.getProfilId() != null){
         if(utilisateursArchiveBox.isChecked()){
            profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByProfilManager(profil, false));
         }else{ // display all of them
            profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByProfilManager(profil, null));
         }
      }
   }

   /**
    * Traite onClick hyperlien nom utilisateur
    * @param event
    * @since 2.1
    */
   public void onClickProfilUtilisateur(final Event event){
      final ProfilUtilisateur pf = (ProfilUtilisateur) event.getData();
      getAdministrationController().selectUtilisateurInController(pf.getUtilisateur());
   }

   /**
    * Traite onClick hyperlien nom banque
    * @param event
    * @since 2.1
    */
   public void onClickProfilUtilisateurBanque(final Event event){
      final ProfilUtilisateur pf = (ProfilUtilisateur) event.getData();
      getAdministrationController().selectBanqueInController(pf.getBanque());
   }

   public AdministrationController getAdministrationController(){
      return (AdministrationController) self.getParent().getParent().getParent().getParent().getParent().getParent().getParent()
         .getParent()
         //.getParent()
         .getAttributeOrFellow("winAdministration$composer", true);
   }
}
