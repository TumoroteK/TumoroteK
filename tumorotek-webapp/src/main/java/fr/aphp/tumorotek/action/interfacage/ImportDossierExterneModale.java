package fr.aphp.tumorotek.action.interfacage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ImportDossierExterneModale extends AbstractFicheCombineController
{

   private static final long serialVersionUID = -9000002907510692460L;

   private Listbox echantillonsBox;

   private Checkbox checkAllValeursPatient;

   private Grid patientValeursGrid;

   private Checkbox checkAllValeursMaladie;

   private Grid maladieValeursGrid;

   private Checkbox checkAllValeursPrelevement;

   private Grid prelevementValeursGrid;

   private Checkbox checkAllValeursEchantillon;

   private Grid echantillonValeursGrid;

   private String path;

   private Prelevement prelevement;

   private Patient patient;

   private Maladie maladie;

   private DossierExterne dossierExterne;

   private List<Echantillon> echantillons = new ArrayList<>();

   private List<BlocExterne> blocExternes = new ArrayList<>();

   private List<ValeurExterneDecorator> patientValeurExternes = new ArrayList<>();

   private List<ValeurExterneDecorator> maladieValeurExternes = new ArrayList<>();

   private List<ValeurExterneDecorator> prelevementValeurExternes = new ArrayList<>();

   private List<ValeurExterneDecorator> echantillonValeurExternes = new ArrayList<>();

   private ValeurExterneRowRenderer valeurExterneRowRenderer = new ValeurExterneRowRenderer();

   private Set<Listitem> selectedEchantillonsItem = new HashSet<>();

   // warning no echantillon selected
   // Nathalie DUFAY
   // @since 2.1
   boolean checkEchans = true;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      // reference vers des boutons non affichés
      editC = new Button();
      validateC = new Button();
      createC = new Button();
      revertC = new Button();
      deleteC = new Button();
      cancelC = new Button();
      addNewC = new Button();

      super.doAfterCompose(comp);

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 10 + "px");
      }

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      getBinder().loadAll();
   }

   /**
    * Méthode intialisant le composant.
    * @param pathToPage Chemin vers la page qui demande une modif.
    */
   public void init(final String pathToPage, final Prelevement prlvt, final DossierExterne dossier){
      this.path = pathToPage;
      this.prelevement = prlvt;
      this.dossierExterne = dossier;

      initLists();

      getBinder().loadComponent(self);
   }

   /**
    * Initialise les listes de valeurs externes.
    */
   public void initLists(){
      blocExternes = ManagerLocator.getBlocExterneManager().findByDossierExterneManager(dossierExterne);

      if(prelevement.getMaladie() != null){
         maladie = prelevement.getMaladie().clone();
      }
      if(maladie != null && maladie.getPatient() != null){
         patient = maladie.getPatient().clone();
      }
      echantillons = ManagerLocator.getEchantillonManager().findByPrelevementManager(prelevement);

      for(int i = 0; i < blocExternes.size(); i++){
         // PATIENT
         if(blocExternes.get(i).getEntiteId() == 1 && patient != null){
            patientValeurExternes = ValeurExterneDecorator.decorateListe(
               ManagerLocator.getValeurExterneManager().findByBlocExterneManager(blocExternes.get(i)), patient, true);
         }
         // MALADIE
         if(blocExternes.get(i).getEntiteId() == 7 && maladie != null){
            maladieValeurExternes = ValeurExterneDecorator.decorateListe(
               ManagerLocator.getValeurExterneManager().findByBlocExterneManager(blocExternes.get(i)), maladie, true);
         }
         // PRELEVEMENT
         if(blocExternes.get(i).getEntiteId() == 2 && prelevement != null){
            prelevementValeurExternes = ValeurExterneDecorator.decorateListe(
               ManagerLocator.getValeurExterneManager().findByBlocExterneManager(blocExternes.get(i)), prelevement, true);
         }
         // ECHANTILLON
         if(blocExternes.get(i).getEntiteId() == 3 && echantillons.size() > 0){
            echantillonValeurExternes = ValeurExterneDecorator.decorateListeEchantillons(
               ManagerLocator.getValeurExterneManager().findByBlocExterneManager(blocExternes.get(i)), null, true, echantillons,
               SessionUtils.getSelectedBanques(sessionScope), getTablesForBanques());
         }
      }

      // ajout opération consultation
      ManagerLocator.getConsultationIntfManager().createObjectManager(dossierExterne.getIdentificationDossier(),
         Calendar.getInstance(), dossierExterne.getEmetteur().getIdentification(), SessionUtils.getLoggedUser(sessionScope));

   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$importer(){

      checkEchans = true;

      if(!echantillons.isEmpty() && echantillonsBox.getSelectedItems().isEmpty()){
         Messagebox.show(Labels.getLabel("import.dossierExternes.echans.warning"), "Warning", Messagebox.OK | Messagebox.CANCEL,
            Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener<Event>()
            {
               @Override
               public void onEvent(final Event e){
                  if(Messagebox.ON_OK.equals(e.getName())){
                     checkEchans = true;
                  }else if(Messagebox.ON_CANCEL.equals(e.getName())){
                     checkEchans = false;
                  }
               }
            });
      }

      if(checkEchans){
         Clients.showBusy(null);
         Events.echoEvent("onLaterImport", self, null);
      }
   }

   public void onLaterImport(){

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("importDossierExterneTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);

      try{
         final List<Echantillon> selectedEchantillons = findSelectedEchantillons();

         final List<AnnotationValeur> patientAnnoValeurs = new ArrayList<>();
         final List<AnnotationValeur> patientAnnoValeursToDelete = new ArrayList<>();
         boolean upPatient = false;
         boolean upMaladie = false;
         final List<AnnotationValeur> prelevementAnnoValeurs = new ArrayList<>();
         final List<AnnotationValeur> prelevementAnnoValeursToDelete = new ArrayList<>();
         boolean upPrelevement = false;
         final List<AnnotationValeur> echantillonsAnnoValeurs = new ArrayList<>();
         final List<AnnotationValeur> echantillonsAnnoValeursToDelete = new ArrayList<>();
         final List<CodeAssigne> codesToCreate = new ArrayList<>();
         final List<CodeAssigne> codesToDelete = new ArrayList<>();
         Fichier crAnapath = null;
         InputStream anapathStream = null;

         boolean upEchantillons = false;
         // Injection dans le patient
         for(int i = 0; i < patientValeurExternes.size(); i++){
            // si la valeur doit être importée
            if(patientValeurExternes.get(i).isImporter()){
               upPatient = true;
               ManagerLocator.getInjectionManager().injectValeurExterneInObject(patient,
                  SessionUtils.getSelectedBanques(sessionScope).get(0), patientValeurExternes.get(i).getValeurExterne(),
                  patientAnnoValeurs);
            }
         }
         // Injection dans la maladie
         for(int i = 0; i < maladieValeurExternes.size(); i++){
            // si la valeur doit être importée
            if(maladieValeurExternes.get(i).isImporter()){
               upMaladie = true;
               ManagerLocator.getInjectionManager().injectValeurExterneInObject(maladie,
                  SessionUtils.getSelectedBanques(sessionScope).get(0), maladieValeurExternes.get(i).getValeurExterne(),
                  new ArrayList<AnnotationValeur>());
            }
         }
         // Injection dans le prelevement
         for(int i = 0; i < prelevementValeurExternes.size(); i++){
            // si la valeur doit être importée
            if(prelevementValeurExternes.get(i).isImporter()){
               upPrelevement = true;
               ManagerLocator.getInjectionManager().injectValeurExterneInObject(prelevement,
                  SessionUtils.getSelectedBanques(sessionScope).get(0), prelevementValeurExternes.get(i).getValeurExterne(),
                  prelevementAnnoValeurs);
            }
         }
         // Injection dans les échantillons sélectionnés
         for(int k = 0; k < selectedEchantillons.size(); k++){
            final Echantillon echantillon = selectedEchantillons.get(k);

            int ordreOrgane = 1;
            int ordreMorpho = 1;

            for(int i = 0; i < echantillonValeurExternes.size(); i++){
               // si la valeur doit être importée
               if(echantillonValeurExternes.get(i).isImporter()){
                  final List<AnnotationValeur> avs = new ArrayList<>();
                  upEchantillons = true;
                  boolean isCode = false;
                  boolean isCR = false;
                  ChampEntite ce = null;
                  if(echantillonValeurExternes.get(i).getValeurExterne().getChampEntiteId() != null){
                     ce = ManagerLocator.getChampEntiteManager()
                        .findByIdManager(echantillonValeurExternes.get(i).getValeurExterne().getChampEntiteId());

                     if(ce.getNom().equals("CodeOrganes") || ce.getNom().equals("CodeMorphos")){
                        isCode = true;
                     }else if(ce.getNom().equals("CrAnapath")){
                        isCR = true;
                     }

                  }

                  if(!isCode && !isCR){
                     ManagerLocator.getInjectionManager().injectValeurExterneInObject(echantillon,
                        SessionUtils.getSelectedBanques(sessionScope).get(0), echantillonValeurExternes.get(i).getValeurExterne(),
                        avs);

                     for(int j = 0; j < avs.size(); j++){
                        avs.get(j).setObjetId(echantillon.getEchantillonId());
                        echantillonsAnnoValeurs.add(avs.get(j));
                     }
                  }else if(isCode && echantillonValeurExternes.get(i).getCode() != null){
                     // } else if (isCode) {
                     //	String[] codes = echantillonValeurExternes.get(i)
                     //		.getValeurExterne().getValeur().split(";");

                     //	for (int j = 0; j < codes.length; j++) {
                     final CodeAssigne codeAs = new CodeAssigne();
                     codeAs.setCodeRefId(echantillonValeurExternes.get(i).getCode().getCodeId());
                     codeAs.setCode(echantillonValeurExternes.get(i).getCode().getCode());
                     codeAs.setLibelle(echantillonValeurExternes.get(i).getCode().getLibelle());
                     codeAs.setTableCodage(null);
                     codeAs.setIsMorpho(ce.getNom().equals("CodeMorphos"));
                     codeAs.setIsOrgane(ce.getNom().equals("CodeOrganes"));
                     if(ce.getNom().equals("CodeMorphos")){
                        codeAs.setOrdre(ordreMorpho);
                        if(ordreMorpho == 1){
                           codeAs.setExport(true);
                        }
                        ++ordreMorpho;
                     }else{
                        codeAs.setOrdre(ordreOrgane);
                        if(ordreOrgane == 1){
                           codeAs.setExport(true);
                        }
                        ++ordreOrgane;
                     }

                     if(!codesToCreate.contains(codeAs)){
                        codesToCreate.add(codeAs);
                     }
                     // }

                     List<CodeAssigne> tmp = new ArrayList<>();
                     // recherche des codes à supprimer
                     if(ce.getNom().equals("CodeMorphos")){
                        tmp = ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echantillon);
                     }else{
                        tmp = ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echantillon);
                     }
                     for(int j = 0; j < tmp.size(); j++){
                        if(!codesToDelete.contains(tmp.get(j))){
                           codesToDelete.add(tmp.get(j));
                        }
                     }
                  }else if(isCR){
                     crAnapath = new Fichier();
                     crAnapath.setNom(echantillonValeurExternes.get(i).getValeurExterne().getValeur());
                     crAnapath.setMimeType("application/pdf");
                     anapathStream = new ByteArrayInputStream(echantillonValeurExternes.get(i).getValeurExterne().getContenu());
                  }
               }
            }
         }

         // sauvegarde du patient si un import a été fait
         if(upPatient){
            // on récupère les listes non impactées par cet update
            final List<Collaborateur> medecins = new ArrayList<>(ManagerLocator.getPatientManager().getMedecinsManager(patient));
            final List<PatientLien> liens = new ArrayList<>(ManagerLocator.getPatientManager().getPatientLiensManager(patient));

            // on recherche les annotations à supprimer (celle maj)
            for(int i = 0; i < patientAnnoValeurs.size(); i++){
               patientAnnoValeursToDelete.addAll(ManagerLocator.getAnnotationValeurManager()
                  .findByChampAndObjetManager(patientAnnoValeurs.get(i).getChampAnnotation(), patient));
            }

            // update du patient
            ManagerLocator.getPatientManager().createOrUpdateObjectManager(patient, null, medecins, liens, patientAnnoValeurs,
               patientAnnoValeursToDelete, filesCreated, filesToDelete, SessionUtils.getLoggedUser(sessionScope), "modification",
               SessionUtils.getSystemBaseDir(), false);
         }

         if(maladie != null){
            maladie.setPatient(patient);
            // sauvegarde de la maladie si un import a été fait
            if(upMaladie){
               // on récupère les liste non impactées par cet update
               final List<Collaborateur> medecins =
                  new ArrayList<>(ManagerLocator.getMaladieManager().getCollaborateursManager(this.maladie));

               ManagerLocator.getMaladieManager().createOrUpdateObjectManager(this.maladie, null, medecins,
                  SessionUtils.getLoggedUser(sessionScope), "modification");
            }
         }

         prelevement.setMaladie(maladie);
         // sauvegarde du prlvt si un import a été fait
         if(upPrelevement){
            // on récupère les listes non impactées par cet update
            final List<LaboInter> laboInters = ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(prelevement);

            // on recherche les annotations à supprimer (celle maj)
            for(int i = 0; i < prelevementAnnoValeurs.size(); i++){
               prelevementAnnoValeursToDelete.addAll(ManagerLocator.getAnnotationValeurManager()
                  .findByChampAndObjetManager(prelevementAnnoValeurs.get(i).getChampAnnotation(), prelevement));
            }

            ManagerLocator.getPrelevementManager().updateObjectManager(prelevement, prelevement.getBanque(),
               prelevement.getNature(), maladie, prelevement.getConsentType(), prelevement.getPreleveur(),
               prelevement.getServicePreleveur(), prelevement.getPrelevementType(), prelevement.getConditType(),
               prelevement.getConditMilieu(), prelevement.getTransporteur(), prelevement.getOperateur(),
               prelevement.getQuantiteUnite(), laboInters, prelevementAnnoValeurs, prelevementAnnoValeursToDelete, filesCreated,
               filesToDelete, SessionUtils.getLoggedUser(sessionScope), null, true, SessionUtils.getSystemBaseDir(), false);
         }

         for(int i = 0; i < selectedEchantillons.size(); i++){
            selectedEchantillons.get(i).setPrelevement(prelevement);
         }
         // sauvegarde des échantillons si un import a été fait
         if(upEchantillons){

            for(int k = 0; k < selectedEchantillons.size(); k++){
               final Echantillon ech = selectedEchantillons.get(k);

               // on recherche les annotations à supprimer (celle maj)
               for(int i = 0; i < echantillonsAnnoValeurs.size(); i++){
                  final List<AnnotationValeur> annos = ManagerLocator.getAnnotationValeurManager()
                     .findByChampAndObjetManager(echantillonsAnnoValeurs.get(i).getChampAnnotation(), ech);

                  for(int j = 0; j < annos.size(); j++){
                     if(!echantillonsAnnoValeursToDelete.contains(annos.get(j))){
                        echantillonsAnnoValeursToDelete.add(annos.get(j));
                     }
                  }
               }
            }

            ManagerLocator.getEchantillonManager().updateMultipleObjectsManager(selectedEchantillons, selectedEchantillons,
               codesToCreate, codesToDelete, crAnapath, anapathStream, false, echantillonsAnnoValeurs,
               echantillonsAnnoValeursToDelete, null, null, SessionUtils.getLoggedUser(sessionScope),
               SessionUtils.getSystemBaseDir());
         }

         // suppression du dossier
         ManagerLocator.getDossierExterneManager().removeObjectManager(dossierExterne);

         ManagerLocator.getTxManager().commit(status);

         for(final File f : filesToDelete){
            f.delete();
         }

         // si le chemin d'accès à la page est correcte
         if(Path.getComponent(path) != null){
            // on envoie un event à cette page avec
            // le prelevement maj
            Events.postEvent(new Event("onGetPrelevementUpdatedFromInterfacage", Path.getComponent(path), prelevement));
         }

      }catch(final RuntimeException re){
         ManagerLocator.getTxManager().rollback(status);

         for(final File f : filesCreated){
            f.delete();
         }

         // ferme wait message
         Clients.clearBusy();
         log.error(re);
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
      // ferme wait message
      Clients.clearBusy();
   }

   /**
    * Suppression du dossier externe.
    */
   public void onClick$delete(){
      // on demande confirmation à l'utilisateur
      // de supprimer l'échantillon
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel("message.deletion.dossier")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

         ManagerLocator.getDossierExterneManager().removeObjectManager(dossierExterne);

         // si le chemin d'accès à la page est correcte
         if(Path.getComponent(path) != null){
            // on envoie un event à cette page avec
            // le prelevement maj
            Events.postEvent(new Event("onGetPrelevementUpdatedFromInterfacage", Path.getComponent(path), prelevement));
         }

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));

      }
   }

   /**
    * Clique sur la checkbox d'un bloc.
    * @param event
    */
   public void onCheckValeurExterne(final ForwardEvent event){
      // on récupère la checkbox associé à l'event
      final Checkbox box = (Checkbox) event.getOrigin().getTarget();
      // on récupère la valeur externe associé à l'event
      final ValeurExterneDecorator deco = (ValeurExterneDecorator) event.getData();

      if(box.isChecked()){
         deco.setImporter(true);
      }else{
         deco.setImporter(false);
      }
      coloriseRow((Row) box.getParent(), box.isChecked());
   }

   /**
    * Colore une ligne en fonction de si elle est à importer ou non.
    * @param row Row à colorier.
    * @param active True si la ligne doit être imprimée.
    */
   public void coloriseRow(final Row row, final Boolean active){
      if(active){
         row.setStyle(null);
      }else{
         row.setStyle("background-color : #E5E5E5;");
      }
      getBinder().loadComponent(row);
   }

   /**
    * Retourne les échantillons sélectionnées.
    * @return
    */
   private List<Echantillon> findSelectedEchantillons(){
      final List<Echantillon> echans = new ArrayList<>();
      final Iterator<Listitem> its = echantillonsBox.getSelectedItems().iterator();
      while(its.hasNext()){
         echans.add(echantillons.get(echantillonsBox.getItems().indexOf(its.next())));
      }
      return echans;
   }

   /**
    * Coche ou décoche toutes les valeurs à importer pour le patient.
    */
   public void onCheck$checkAllValeursPatient(){
      for(int i = 0; i < patientValeurExternes.size(); i++){
         patientValeurExternes.get(i).setImporter(checkAllValeursPatient.isChecked());
      }
      getBinder().loadComponent(patientValeursGrid);
   }

   /**
    * Coche ou décoche toutes les valeurs à importer pour la maladie.
    */
   public void onCheck$checkAllValeursMaladie(){
      for(int i = 0; i < maladieValeurExternes.size(); i++){
         maladieValeurExternes.get(i).setImporter(checkAllValeursMaladie.isChecked());
      }
      getBinder().loadComponent(maladieValeursGrid);
   }

   /**
    * Coche ou décoche toutes les valeurs à importer pour le prelevement.
    */
   public void onCheck$checkAllValeursPrelevement(){
      for(int i = 0; i < prelevementValeurExternes.size(); i++){
         prelevementValeurExternes.get(i).setImporter(checkAllValeursPrelevement.isChecked());
      }
      getBinder().loadComponent(prelevementValeursGrid);
   }

   /**
    * Coche ou décoche toutes les valeurs à importer pour les echantillons.
    */
   public void onCheck$checkAllValeursEchantillon(){
      for(int i = 0; i < echantillonValeurExternes.size(); i++){
         echantillonValeurExternes.get(i).setImporter(checkAllValeursEchantillon.isChecked());
      }
      getBinder().loadComponent(echantillonValeursGrid);
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){

   }

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   public Prelevement getPrelevement(){
      return prelevement;
   }

   public void setPrelevement(final Prelevement p){
      this.prelevement = p;
   }

   public DossierExterne getDossierExterne(){
      return dossierExterne;
   }

   public void setDossierExterne(final DossierExterne d){
      this.dossierExterne = d;
   }

   public List<ValeurExterneDecorator> getPatientValeurExternes(){
      return patientValeurExternes;
   }

   public void setPatientValeurExternes(final List<ValeurExterneDecorator> p){
      this.patientValeurExternes = p;
   }

   public ValeurExterneRowRenderer getValeurExterneRowRenderer(){
      return valeurExterneRowRenderer;
   }

   public void setValeurExterneRowRenderer(final ValeurExterneRowRenderer v){
      this.valeurExterneRowRenderer = v;
   }

   public List<BlocExterne> getBlocExternes(){
      return blocExternes;
   }

   public void setBlocExternes(final List<BlocExterne> b){
      this.blocExternes = b;
   }

   public Patient getPatient(){
      return patient;
   }

   public void setPatient(final Patient p){
      this.patient = p;
   }

   public Maladie getMaladie(){
      return maladie;
   }

   public void setMaladie(final Maladie m){
      this.maladie = m;
   }

   public List<ValeurExterneDecorator> getMaladieValeurExternes(){
      return maladieValeurExternes;
   }

   public void setMaladieValeurExternes(final List<ValeurExterneDecorator> m){
      this.maladieValeurExternes = m;
   }

   public List<ValeurExterneDecorator> getPrelevementValeurExternes(){
      return prelevementValeurExternes;
   }

   public void setPrelevementValeurExternes(final List<ValeurExterneDecorator> p){
      this.prelevementValeurExternes = p;
   }

   public List<ValeurExterneDecorator> getEchantillonValeurExternes(){
      return echantillonValeurExternes;
   }

   public void setEchantillonValeurExternes(final List<ValeurExterneDecorator> e){
      this.echantillonValeurExternes = e;
   }

   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> e){
      this.echantillons = e;
   }

   public Set<Listitem> getSelectedEchantillonsItem(){
      return selectedEchantillonsItem;
   }

   public void setSelectedEchantillonsItem(final Set<Listitem> s){
      this.selectedEchantillonsItem = s;
   }

}
