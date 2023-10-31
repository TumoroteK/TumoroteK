/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.prelevement;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstInt;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.FichePatientStatic;
import fr.aphp.tumorotek.action.patient.PatientConstraints;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.PrelevementDecorator2;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.utils.MessagesUtils;
/**
 * Controller gérant la fiche en édition d'un prélèvement.
 * Controller créé le 23/06/2010.
 *
 * @author Pierre Ventadour
 * @version 2.3.0-gatsbi
 */
public class FichePrelevementEdit extends AbstractFicheEditController
{

   protected Logger log = LoggerFactory.getLogger(FichePrelevementEdit.class);

   protected static final long serialVersionUID = 2627927168895414292L;

   protected Textbox codeBoxPrlvt;

   protected Textbox numLaboBoxPrlvt;

   protected CalendarBox datePrelCalBox;

   protected Datebox dateConsentBoxPrlvt;

   protected boolean dateConsentBoxChanged;

   protected Listbox etabsBoxPrlvt;

   protected Listbox servicesBoxPrlvt;

   protected Listbox collaborateursBoxPrlvt;

   protected Checkbox sterileBoxPrlvt;

   protected Button next;

   protected Listbox naturesBoxPrlvt;

   protected Listbox consentTypesBoxPrlvt;

   protected Listbox risquesBox;

   protected Button interfacage;

   protected ResumePatient resumePatient;

   protected Textbox ndaBox;

   // gatsbi overrides
   // protected Group groupPatient;
   protected HtmlBasedComponent groupPatient;

   // Objets Principaux.
   protected Prelevement prelevement = new Prelevement();

   protected Maladie maladie;

   // Associations.
   protected List<Nature> natures = new ArrayList<>();

   protected Nature selectedNature;

   protected List<PrelevementType> modes = new ArrayList<>();

   protected PrelevementType selectedMode;

   protected List<ConditType> conditTypes = new ArrayList<>();

   protected ConditType selectedConditType;

   protected List<ConditMilieu> conditMilieus = new ArrayList<>();

   protected ConditMilieu selectedConditMilieu;

   protected List<ConsentType> consentTypes = new ArrayList<>();

   protected ConsentType selectedConsentType;

   protected List<Etablissement> etablissements = new ArrayList<>();

   protected Etablissement selectedEtablissement;

   protected List<Service> services = new ArrayList<>();

   protected Service selectedService;

   protected List<Collaborateur> collaborateurs = new ArrayList<>();

   protected Collaborateur selectedCollaborateur;

   protected List<LaboInter> oldLabos = new ArrayList<>();

   protected List<Risque> risques = new ArrayList<>();

   protected boolean isPatientAccessible = true;

   protected Div refPatientDiv;

   //protected Boolean banqueOrgane;
   // boolean conditionnant l'affichage dans le group Patient
   protected boolean maladieEmbedded = false;

   protected boolean patientEmbedded = false;

   protected boolean sterileInit;

   protected boolean isAnonyme = false;

   protected Window referenceur;

   protected boolean isPatientMaladieStatic = false;

   protected ConsentTypeUsedTable consentTypeUsed = new ConsentTypeUsedTable();

   public void setMaladie(final Maladie m){
      this.maladie = m;
   }

   public Maladie getMaladie(){
      return this.maladie;
   }

   public boolean isPatientAccessible(){
      return isPatientAccessible;
   }

   public void setPatientAccessible(final boolean isAccessible){
      this.isPatientAccessible = isAccessible;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // Initialisation du mode (listes, valeurs...)
      initLists();

      if(sessionScope.containsKey("Anonyme") && (Boolean) sessionScope.get("Anonyme")){
         isAnonyme = true;
      }else{
         isAnonyme = false;
      }

      isPatientAccessible = getDroitOnAction("Patient", "Consultation");

      // gatsbi overrides
      resumePatient = initResumePatient();
   }

   // gatsbi surcharge cette méthode
   // car le component group patient n'est pas
   // de même type group VS Groupbox
   protected ResumePatient initResumePatient(){
      return new ResumePatient(groupPatient, null, null);
   }

   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();

      setInterfacageButtonVisible();

      getObjectTabController().setCodeUpdated(false);
      getObjectTabController().setOldCode(null);

      // affiche le composant de référencement vers le patient
      this.groupPatient.setClass("z-group");
      initCollaborations();

      if(getMaladie() != null){
         // ResumePatient resume = drawResumePatientComponent();
         resumePatient.setVisible(true);
         resumePatient.setAnonyme(isAnonyme());
         resumePatient.setMaladie(getMaladie());
         resumePatient.setPrelevement(getObject());
         resumePatient.setPatientAccessible(false);
         resumePatient.hideMaladieRows(SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope)));
         resumePatient.setNdaBoxVisible(true);

         ndaBox = resumePatient.getNdaBox();

         // prepare la liste de risques
         if(getParentObject() != null){
            selectRisques(ManagerLocator.getRisqueManager().findByPatientAndPlateformeManager((Patient) getParentObject(),
               SessionUtils.getPlateforme(sessionScope)));
         }

      }else{
         resumePatient.setVisible(false);
         referenceur = (Window) Executions.createComponents(getRefPatientCompPath(), refPatientDiv, null);
         // initialize le referenceur
         ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true))
            .initialize(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefMaladies());
      }
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();

      if(getMaladie() != null){
         resumePatient.setVisible(true);
         resumePatient.setAnonyme(isAnonyme());
         resumePatient.setPrelevement(getObject());
         resumePatient.setMaladie(getMaladie());
         resumePatient.setPatientAccessible(false);
         resumePatient.hideMaladieRows(SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope)));
         resumePatient.setNdaBoxVisible(true);
         enablePatientGroup(true);
      }else{
         resumePatient.setVisible(false);
         enablePatientGroup(false);
      }

      initSelectedInLists();
      initCollaborations();

      //this.isPatientMaladieStatic = true;

      // enregistre si le prelevement etait sterile avant update
      sterileInit = (this.prelevement.getSterile() != null && this.prelevement.getSterile());

      getObjectTabController().setCodeUpdated(false);
      getObjectTabController().setOldCode(null);
   }

   /**
    * Gatsbi surcharge cette méthode
    */
   protected void enablePatientGroup(final boolean b){
      if(b){ // enable
         this.groupPatient.setClass("z-group");
      }else{
         this.groupPatient.setClass("z-group-dsd");
      }
   }

   /**
    * Initialisation du contenu des listes déroulantes.
    */

   public void initLists(){

      natures = ManagerLocator.getNatureManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      natures.add(0, null);
      selectedNature = natures.get(0);

      modes = ManagerLocator.getPrelevementTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      modes.add(0, null);

      conditTypes = ManagerLocator.getConditTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      conditTypes.add(0, null);

      conditMilieus = ManagerLocator.getConditMilieuManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      conditMilieus.add(0, null);

      consentTypes = ManagerLocator.getConsentTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      if(consentTypes.size() > 1){
         consentTypes.add(0, null);
      }
      if(!consentTypes.isEmpty()){
         selectedConsentType = consentTypes.get(0);
      }

      risques.addAll(ManagerLocator.getRisqueManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));

      //initCollabrorations();
      dateConsentBoxChanged = false;
   }

   /**
    * Initialisation des valeurs selected pour chaque liste.
    */
   public void initSelectedInLists(){

      if(this.prelevement.getNature() != null){
         selectedNature = this.prelevement.getNature();
      }else if(!natures.isEmpty()){
         selectedNature = natures.get(0);
      }

      selectedMode = this.prelevement.getPrelevementType();
      selectedConditType = this.prelevement.getConditType();
      selectedConditMilieu = this.prelevement.getConditMilieu();

      if(this.prelevement.getConsentType() != null){
         selectedConsentType = this.prelevement.getConsentType();
      }else{
         final List<ConsentType> tmps = ManagerLocator.getConsentTypeManager().findByTypeLikeManager("EN ATTENTE", false);
         // @since 2.3.0-gatsbi init selected in list EN ATTENTE si hors gatsbi
         if(tmps.size() > 0 && !GatsbiController.isInGatsbiContexte(SessionUtils.getCurrentBanque(sessionScope))){
            selectedConsentType = tmps.get(0);
         }else if(!consentTypes.isEmpty()){
            selectedConsentType = consentTypes.get(0);
         }
      }

      // sauf dans le cas ou create another prelevement
      final List<Risque> sels = new ArrayList<>();
      if(getObject().getPrelevementId() != null){
         getObject().setRisques(ManagerLocator.getPrelevementManager().getRisquesManager(getObject()));
      }
      sels.addAll(getObject().getRisques());

      selectRisques(sels);
   }

   /**
    * Select les risques dans la dropdown list.
    *
    * @param risks liste à selectionner
    */

   public void selectRisques(final List<Risque> risks){
      if(risks != null){
         ((Selectable<Risque>) risquesBox.getModel()).setSelection(risks);

         getBinder().loadAttribute(risquesBox, "selectedItems");
      }
   }

   public void clearRisques(){
      selectRisques(new ArrayList<Risque>());
   }

   public void initCollaborations(){
      if(this.prelevement.getServicePreleveur() != null){
         selectedService = this.prelevement.getServicePreleveur();
      }else{
         selectedService = null;
      }

      if(this.prelevement.getPreleveur() != null){
         selectedCollaborateur = this.prelevement.getPreleveur();
      }else{
         selectedCollaborateur = null;
      }

      // init des étabs
      etablissements = ManagerLocator.getEtablissementManager().findAllActiveObjectsWithOrderManager();
      etablissements.add(0, null);
      if(selectedService != null){
         selectedEtablissement = selectedService.getEtablissement();
      }else{
         selectedEtablissement = null;
      }

      // init des services
      if(selectedEtablissement != null){
         services = ManagerLocator.getEtablissementManager().getActiveServicesWithOrderManager(selectedEtablissement);
      }else{
         services = ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager();
      }
      services.add(0, null);
      final ListModel<Service> listTmp = new ListModelList<>(services);
      servicesBoxPrlvt.setModel(listTmp);
      servicesBoxPrlvt.setSelectedIndex(services.indexOf(selectedService));

      // init des collaborateurs
      if(selectedService != null){
         collaborateurs = ManagerLocator.getServiceManager().getActiveCollaborateursWithOrderManager(selectedService);
      }else{
         collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      }
      collaborateurs.add(0, null);

      final ListModel<Collaborateur> listCollabs = new ListModelList<>(collaborateurs);
      collaborateursBoxPrlvt.setModel(listCollabs);
      collaborateursBoxPrlvt.setSelectedIndex(collaborateurs.indexOf(selectedCollaborateur));

      getBinder().loadAll();
   }

   /**
    * Méthode inspirée initCollaborations et allégée pour appel uniquement
    * dans injection depuis interfaçage.
    * Teste la cohérence de l'injection Service - Collaborateur, envoie un
    * WARNING si incohérence.
    */
   protected void selectCollaborations(){
      if(this.prelevement.getServicePreleveur() != null){
         selectedService = this.prelevement.getServicePreleveur();
      }else{
         selectedService = null;
      }

      // init des services ssi etablissement specifié
      if(selectedService != null){
         selectedEtablissement = selectedService.getEtablissement();

         services.clear();
         services.addAll(ManagerLocator.getEtablissementManager().getActiveServicesWithOrderManager(selectedEtablissement));
         services.add(0, null);
         final ListModel<Service> servModel = new ListModelList<>(services);
         servicesBoxPrlvt.setModel(servModel);

         servicesBoxPrlvt.setSelectedIndex(services.indexOf(selectedService));

         // init des collaborateurs
         collaborateurs.clear();
         collaborateurs.addAll(ManagerLocator.getServiceManager().getActiveCollaborateursWithOrderManager(selectedService));
         collaborateurs.add(0, null);

         final ListModel<Collaborateur> listCollabs = new ListModelList<>(collaborateurs);
         collaborateursBoxPrlvt.setModel(listCollabs);
      }else{
         selectedEtablissement = null;
      }

      if(this.prelevement.getPreleveur() != null){
         selectedCollaborateur = this.prelevement.getPreleveur();
         final int idx = collaborateurs.indexOf(selectedCollaborateur);
         if(idx >= 0){
            collaborateursBoxPrlvt.setSelectedIndex(collaborateurs.indexOf(selectedCollaborateur));
         }else{ // warning incoherence appartenance service
            //				throw new WrongValueException(collaborateursBoxPrlvt,
            //						Labels.getLabel(
            //							"interfacage.injection.collaborateur.incoherent"));
            Messagebox.show(Labels.getLabel("interfacage.injection.collaborateur.incoherent"), Labels.getLabel("general.warning"),
               Messagebox.OK, Messagebox.EXCLAMATION);
         }
      }else{
         selectedCollaborateur = null;
      }
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.prelevement = (Prelevement) obj;
      this.maladie = this.prelevement.getMaladie();

      //super.setObject(obj);

      // Calendar boxes
      // datePrelCalBox.setValue(this.prelevement.getDatePrelevement());

      // fetch labos
      if(prelevement.getPrelevementId() != null){
         this.prelevement.setLaboInters(ManagerLocator.getPrelevementManager().getLaboIntersManager(prelevement));
      }

      super.setObject(obj);

   }

   @Override
   public Prelevement getObject(){
      return this.prelevement;
   }

   @Override
   public void setNewObject(){
      // la banque doit être assignée pour
      // la création conjointe des annotations
      final Prelevement newObj = new Prelevement();
      newObj.setBanque((Banque) sessionScope.get("Banque"));
      setObject(newObj);
      oldLabos = new ArrayList<>();
   }

   @Override
   public PrelevementController getObjectTabController(){
      return (PrelevementController) super.getObjectTabController();
   }

   @Override
   public TKdataObject getParentObject(){
      if(this.maladie != null){
         return this.maladie.getPatient();
      }
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){
      this.maladie = (Maladie) obj;
   }

   public void setNewObjectByCopy(final Prelevement prlvt){
      Prelevement newObj = new Prelevement();
      final List<AnnotationValeur> annos = new ArrayList<>();
      if(prlvt != null){
         newObj = prlvt.clone();
         newObj.setDelegate(prlvt.cloneDelegateTo(newObj));
         newObj.setPrelevementId(null);
         newObj.setCode(null);
         newObj.setNumeroLabo(null);
         newObj.setNature(null);

         // on recopie tous les labo inters
         final List<LaboInter> tmpLabos = ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(prlvt);
         for(int i = 0; i < tmpLabos.size(); i++){
            final LaboInter lab = new LaboInter();
            lab.setCollaborateur(tmpLabos.get(i).getCollaborateur());
            lab.setConservTemp(tmpLabos.get(i).getConservTemp());
            lab.setDateArrivee(tmpLabos.get(i).getDateArrivee());
            lab.setDateDepart(tmpLabos.get(i).getDateDepart());
            lab.setOrdre(tmpLabos.get(i).getOrdre());
            lab.setPrelevement(newObj);
            lab.setService(tmpLabos.get(i).getService());
            lab.setSterile(tmpLabos.get(i).getSterile());
            lab.setTransporteur(tmpLabos.get(i).getTransporteur());
            lab.setTransportTemp(tmpLabos.get(i).getTransportTemp());
            oldLabos.add(lab);
         }
         newObj.setLaboInters(new HashSet<>(oldLabos));

         // annotations
         final List<AnnotationValeur> annosToCopy = ManagerLocator.getAnnotationValeurManager().findByObjectManager(prlvt);
         AnnotationValeur val;
         for(int i = 0; i < annosToCopy.size(); i++){
            if(!annosToCopy.get(i).getChampAnnotation().getDataType().getType().equals("fichier")){
               val = annosToCopy.get(i).clone();
               val.setObjetId(null);
               val.setAnnotationValeurId(null);
               annos.add(val);
            }
         }
      }else{
         newObj.setBanque((Banque) sessionScope.get("Banque"));
      }
      setObject(newObj);
      if(getObjectTabController() != null && getObjectTabController().getFicheAnnotation() != null){
         getObjectTabController().getFicheAnnotation().setAnnotationValues(annos);
      }
   }

   @Override
   public void createNewObject(){
      final List<File> filesCreated = new ArrayList<>();

      try{
         setEmptyToNulls();
         setFieldsToUpperCase();

         // on ne change pas les associations qui ne sont pas présentes
         // dans le formulaire
         final Transporteur transporteur = this.prelevement.getTransporteur();
         final Collaborateur operateur = this.prelevement.getOperateur();
         final Unite quantiteUnite = this.prelevement.getQuantiteUnite();

         getObject().setRisques(findSelectedRisques());
         getObject().setLaboInters(new HashSet<LaboInter>());

         // update de l'objet
         ManagerLocator.getPrelevementManager().createObjectManager(prelevement,
            GatsbiController.enrichesBanqueWithEtudeContextes(getBanque(), sessionScope), selectedNature, this.maladie,
            selectedConsentType, selectedCollaborateur, selectedService, selectedMode, selectedConditType, selectedConditMilieu,
            transporteur, operateur, quantiteUnite, null,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(), filesCreated,
            SessionUtils.getLoggedUser(sessionScope), true, SessionUtils.getSystemBaseDir(), false);

         // rafraichit la maladie pour avoir les references
         this.maladie = ManagerLocator.getPrelevementManager().getMaladieManager(this.prelevement);

         // suppression du patientSip
         getObjectTabController().removePatientSip();
         // gestion de la communication des infos et de l'éventuel dossier externe
         getObjectTabController().handleExtCom(null, getObject(), getObjectTabController());
      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   @Override
   public boolean onLaterCreate(){
      try{
         // true ssi aucune exception n'est levée!
         if(super.onLaterCreate()){

            if(this.maladie != null){
               // retour vers la fiche patient au besoin
               if(getObjectTabController().getFromFichePatient()){
                  getObjectTabController().setFromFichePatient(false);
                  getObjectTabController().getReferencingObjectControllers().get(0)
                     .switchToFicheStaticMode(this.maladie.getPatient());

                  PatientController.backToMe(getMainWindow(), page);
                  // ouvre le panel maladie et la liste de prelevements
                  if(getObjectTabController().getFromFicheMaladie()){
                     ((FichePatientStatic) getObjectTabController().getReferencingObjectControllers().get(0).getFicheStatic())
                        .openMaladiePanel(maladie);
                     getObjectTabController().setFromFicheMaladie(false);
                  }
               }
               // on demande la creation d'un nouveau prélèvement
               getObjectTabController().createAnotherPrelevement(prelevement);
            }
         }

         return true;

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   @Override
   public void onClick$create(){

      // gatsbi overrides
      checkRequiredListboxes();

      // valide les dates donc
      validateAllDateComps();

      // récupere les objets à partir des formulaires embarqués
      setEmbeddedObjects();

      if(warnNoPatient()){ // warning si pas patient
         super.onClick$create();
      }
   }

   @Override
   public void onClick$validate(){

      // gatsbi overrides
      checkRequiredListboxes();

      // valide les dates donc
      validateAllDateComps();


      super.onClick$validate();
   }

   /**
    * @version 2.1
    */
   @Override
   public boolean onLaterUpdate(){

      try{
         if(super.onLaterUpdate()){
            getObjectTabController().showEchantillonsAfterUpdate(prelevement);
         }
         return true;

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         log.error(re.getMessage(), re);
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }


   @Override
   public void onClick$revert(){
      super.onClick$revert();
   }

   @Override
   public void onClick$cancel(){

      // retour vers la fiche patient au besoin
      if(getObjectTabController().getFromFichePatient()){
         getObjectTabController().setFromFichePatient(false);
         PatientController.backToMe(getMainWindow(), page);
      }

      SessionUtils.setDossierExterneInjection(sessionScope, null);

      super.onClick$cancel();
   }

   @Override
   public void updateObject(){
      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      try{
         Integer cascadeNonSterile = null;

         setEmptyToNulls();
         setFieldsToUpperCase();

         // casse la cascade stérilité
         if(ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(prelevement).size() > 0
            || ManagerLocator.getPrelevementManager().getEchantillonsManager(prelevement).size() > 0){
            if(sterileInit && !prelevement.getSterile()){
               cascadeNonSterile = 0;
               Clients.clearBusy();
               this.sterileBoxPrlvt.setChecked(false);
               if(Messagebox.show(Labels.getLabel("message.sterilite.cascade"), Labels.getLabel("message.sterilite.title"),
                  Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.NO){
                  cascadeNonSterile = null;
                  this.sterileBoxPrlvt.setChecked(true);
                  this.prelevement.setSterile(true);
                  return;
               }
               Clients.showBusy(Labels.getLabel("prelevement.creation.encours"));
            }
         }

         // on ne change pas les associations qui ne sont pas présentes
         // dans le formulaire
         final Transporteur transporteur = this.prelevement.getTransporteur();
         final Collaborateur operateur = this.prelevement.getOperateur();
         final Unite quantiteUnite = this.prelevement.getQuantiteUnite();

         getObject().getRisques().clear();
         getObject().getRisques().addAll(findSelectedRisques());

         //Update de l'objet
         ManagerLocator.getPrelevementManager().updateObjectManager(prelevement,
            GatsbiController.enrichesBanqueWithEtudeContextes(prelevement.getBanque(), sessionScope), selectedNature, maladie,
            selectedConsentType, selectedCollaborateur, selectedService, selectedMode, selectedConditType, selectedConditMilieu,
            transporteur, operateur, quantiteUnite, null,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
            getObjectTabController().getFicheAnnotation().getValeursToDelete(), filesCreated, filesToDelete,
            SessionUtils.getLoggedUser(sessionScope), cascadeNonSterile, true, SessionUtils.getSystemBaseDir(), false);


         // TK-427: Mettre à jour le délai de congélation des échantillons
         if (getObjectTabController().isDateFieldModified()) {
            getObjectTabController().miseAJourDelaiCongelation(prelevement);
         }


         getObjectTabController().handleExtCom(null, getObject(), getObjectTabController());

         for(final File f : filesToDelete){
            f.delete();
         }

      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }


   protected Set<Risque> findSelectedRisques(){
      final Set<Risque> rs = new HashSet<>();
      final Iterator<Listitem> its = risquesBox.getSelectedItems().iterator();
      while(its.hasNext()){
         rs.add(risques.get(risquesBox.getItems().indexOf(its.next())));
      }
      return rs;
   }

   @Override
   protected void setEmptyToNulls(){
      // si le numero labo est vide, on l'enregistre
      // a null
      if(prelevement.getNumeroLabo().equals("")){
         prelevement.setNumeroLabo(null);
      }
      if(ndaBox != null && ndaBox.getValue() != null && !this.ndaBox.getValue().equals("")){
         this.prelevement.setPatientNda(ndaBox.getValue());
      }else{
         this.prelevement.setPatientNda(null);
      }

      // calendarboxes
      // prelevement.setDatePrelevement(datePrelCalBox.getValue());
   }

   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(codeBoxPrlvt);
      Clients.clearWrongValue(numLaboBoxPrlvt);
   }

   /*************************************************************************/
   /************************** EMBEDDED  ************************************/
   /*************************************************************************/
   public void setMaladieEmbedded(final boolean malEmbedded){
      this.maladieEmbedded = malEmbedded;
   }

   public void setPatientEmbedded(final boolean patEmbedded){
      this.patientEmbedded = patEmbedded;
   }

   /**
    * Entraine l'affichage du résumé Patient-maladie (si true).
    */
   public boolean getIsMaladieNotNull(){
      return (this.isPatientMaladieStatic && this.maladie != null);
   }

   /**
    * Entraine l'affichage du referenceur Patient-maladie (si true).
    */
   public boolean getIsCreateMode(){
      return (!this.isPatientMaladieStatic);
   }

   /**
    * Vérifie la présence de formulaires embarqués afin de récupérer
    * les backing-beans.
    */
   protected void setEmbeddedObjects(){
      if(referenceur != null){

         // vérifie la présence du formulaire embarqué patient
         if(this.patientEmbedded){
            // maladie associee
            final Component embeddedMaladie = referenceur.getFellow("winRefPatient").getFellow("newPatientDiv")
               .getFellow("ficheMaladieWithPatientDiv").getFirstChild();

            if (embeddedMaladie != null) {
               // force la validation des dates maladies
               // car les dates de naissance
               // et deces du patient ont pu être modifée à posteriori
               ((FicheMaladie) embeddedMaladie.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
                  .validateAllDateComps();
               setMaladieFromEmbedded(embeddedMaladie);
            } else if (this.maladieEmbedded){ // @since gatsbi, nouveau patient, nouvelle maladie (visite) hors schema
               final Component embedded = referenceur.getFellow("winRefPatient").getFellow("embeddedFicheMaladieRow")
                  .getFellow("embeddedFicheMaladieDiv");
               setMaladieFromEmbedded(embedded);
            }

            final Component embeddedPatient =
               referenceur.getFellow("winRefPatient").getFellow("newPatientDiv").getFellow("fichePatientDiv").getFirstChild();
            // enregistre le formulaire dans le backing-bean
            // meme si il est vide...
            ((FichePatientEdit) embeddedPatient.getFellow("fwinPatientEdit").getAttributeOrFellow("fwinPatientEdit$composer",
               true)).getBinder().saveComponent(embeddedPatient.getFellow("fwinPatientEdit"));

            // prepare les valeurs des attributs
            ((FichePatientEdit) embeddedPatient.getFellow("fwinPatientEdit").getAttributeOrFellow("fwinPatientEdit$composer",
               true)).prepareDataBeforeSave(true);

            // re-assigne la reference ndaBox vers le composant embedded
            this.ndaBox = (Textbox) embeddedPatient.getFellow("fwinPatientEdit").getFellow("patientNdaBox");

            // vérifie la présence du formulaire embarqué maladie
         }else{
            if(this.maladieEmbedded){
               final Component embedded = referenceur.getFellow("winRefPatient").getFellow("embeddedFicheMaladieRow")
                  .getFellow("embeddedFicheMaladieDiv");
               setMaladieFromEmbedded(embedded);
            }else if(((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true)).getNoRadio()
               .isChecked()
               || ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true))
                  .getSelectedPatient() == null){
               this.maladie = null;
               this.prelevement.setMaladie(null);
            }
            if(((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true)).getNdaBox()
               .getValue() != null){
               this.ndaBox = ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true)).getNdaBox();
            }
         }

      }
   }

   protected void setMaladieFromEmbedded(final Component embedded){
      // enregistre le formulaire dans le backing-bean
      // meme si il est vide...
      ((FicheMaladie) embedded.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).getBinder()
         .saveComponent(embedded.getFellow("fwinMaladie"));
      // prepare les valeurs des attributs
      ((FicheMaladie) embedded.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
         .prepareDataBeforeSave(true);
      // recupere la maladie meme si empty
      this.maladie =
         ((FicheMaladie) embedded.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).getObject();
   }

   /***********************************************************/
   /*****************  NEXT ***********************************/
   /***********************************************************/

   /**
    * Processing echoEvent.
    *
    * @see onClick$next
    */
   public void onLaterNextStep(){

      // si nous sommes dans une action d'édition, on
      // appelle la page FicheLaboInter en mode edit
      if(this.prelevement.getPrelevementId() != null){
         getObjectTabController().switchToLaboInterEditMode(prelevement);
      }else{
         // si nous sommes dans une action de création, on
         // appelle la page FicheLaboInter en mode create
         getObjectTabController().switchToLaboInterCreateMode(this.prelevement, this.oldLabos);
      }

      Clients.clearBusy();
   }

   /**
    * Passe à la fiche des labos inters.
    *
    * @version 2.3.0-gatsbi
    */
   public void onClick$next(){

      // gatsbi overrides
      checkRequiredListboxes();

      // valide les dates donc
      validateAllDateComps();

      // récupere les objets à partir des formulaires embarqués
      setEmbeddedObjects();

      if(warnNoPatient()){ // warning si pas patient

         // on remplit le prlvt en fonction des champs nulls
         setEmptyToNulls();

         // on recopie les sélections dans le prlvt
         if(this.prelevement.getBanque() == null){
            this.prelevement.setBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
         }
         this.prelevement.setNature(selectedNature);
         this.prelevement.setConsentType(selectedConsentType);
         this.prelevement.setPreleveur(selectedCollaborateur);
         this.prelevement.setServicePreleveur(selectedService);
         this.prelevement.setPrelevementType(selectedMode);
         this.prelevement.setConditType(selectedConditType);
         this.prelevement.setConditMilieu(selectedConditMilieu);
         getObject().setRisques(findSelectedRisques());

         this.prelevement.setMaladie(this.maladie);

         // on vérifie que le prélèvement n'a pas de doublons
         if(!ManagerLocator.getPrelevementManager().findDoublonManager(prelevement)){

            Clients.showBusy(Labels.getLabel("general.wait"));
            Events.echoEvent("onLaterNextStep", self, null);

         }else{
            try{
               throw new DoublonFoundException("Prelevement", "creation", prelevement.getCode(), null);
            }catch(final DoublonFoundException re){
               Clients.clearBusy();

               final HashMap<String, Object> map = new HashMap<>();
               map.put("title", Labels.getLabel("error.unhandled"));
               map.put("message", handleExceptionMessage(re));
               map.put("exception", re);

               final Window window =
                  (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
               window.doModal();
            }catch(final RuntimeException re){
               throw new RuntimeException(handleExceptionMessage(re));
            }
         }
      }
   }

   /**
    * Raccourci quand clique 'return'.
    */
   @Override
   public void onOK(){
      Events.postEvent(new Event("onClick", next));
   }

   /**
    * Filtre les services par établissement.
    *
    * @param event Event : seléction sur la liste etabsBoxPrlvt.
    * @throws Exception
    */
   public void onSelect$etabsBoxPrlvt(final Event event) throws Exception{
      if(selectedEtablissement != null){
         services = ManagerLocator.getEtablissementManager().getActiveServicesWithOrderManager(selectedEtablissement);
      }else{
         services = ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager();
      }
      services.add(0, null);

      final ListModel<Service> list = new ListModelList<>(services);
      servicesBoxPrlvt.setModel(list);

      if(!services.contains(selectedService)){
         selectedService = null;
      }

      servicesBoxPrlvt.setSelectedIndex(services.indexOf(selectedService));

      getBinder().loadComponent(servicesBoxPrlvt);
   }

   /**
    * Filtre les collaborateurs par service.
    *
    * @param event Event : seléction sur la liste etabsBoxPrlvt.
    * @throws Exception
    */
   public void onSelect$servicesBoxPrlvt(final Event event) throws Exception{
      final int ind = servicesBoxPrlvt.getSelectedIndex();
      selectedService = services.get(ind);
      if(selectedService != null){
         collaborateurs = ManagerLocator.getServiceManager().getActiveCollaborateursWithOrderManager(selectedService);
      }else{
         collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      }
      collaborateurs.add(0, null);

      final ListModel<Collaborateur> list = new ListModelList<>(collaborateurs);
      collaborateursBoxPrlvt.setModel(list);

      if(!collaborateurs.contains(selectedCollaborateur)){
         selectedCollaborateur = null;
      }

      collaborateursBoxPrlvt.setSelectedIndex(collaborateurs.indexOf(selectedCollaborateur));

      getBinder().loadComponent(collaborateursBoxPrlvt);
   }

   /**
    * Sélectionne le collaborateur.
    *
    * @param event Event : seléction sur la liste collaborateursBoxPrlvt.
    * @throws Exception
    */
   public void onSelect$collaborateursBoxPrlvt(final Event event) throws Exception{
      final int ind = collaborateursBoxPrlvt.getSelectedIndex();
      selectedCollaborateur = collaborateurs.get(ind);
   }

   public void onSelect$naturesBoxPrlvt(){
      // validation
      if(selectedNature == null){
         Clients.scrollIntoView(naturesBoxPrlvt);
         throw new WrongValueException(naturesBoxPrlvt, Labels.getLabel("fichePrelevement.error.nature"));
      }
      Clients.clearWrongValue(naturesBoxPrlvt);
   }

   public void onSelect$consentTypesBoxPrlvt(){
      if(selectedConsentType == null){
         Clients.scrollIntoView(consentTypesBoxPrlvt);
         throw new WrongValueException(consentTypesBoxPrlvt, Labels.getLabel("fichePrelevement.error.consenType"));
      }
      Clients.clearWrongValue(consentTypesBoxPrlvt);
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codeBoxPrlvt. Cette valeur sera mise en majuscules.
    */
   public void onBlur$codeBoxPrlvt(){
      codeBoxPrlvt.setValue(codeBoxPrlvt.getValue().toUpperCase().trim());

      //On ne contrôle le code que s'il s'agit d'un nouveau prélèvement ou si le code a été modifié
      if(prelevement.getPrelevementId() == null || !codeBoxPrlvt.getValue().equals(prelevement.getCode())){
         validatePrelevementCode(codeBoxPrlvt.getValue());
      }

      // si le code a été modifié lors de l'update du prélèvement
      if(prelevement.getPrelevementId() != null && !((Prelevement) getCopy()).getCode().equals(codeBoxPrlvt.getValue())){
         getObjectTabController().setCodeUpdated(true);
         getObjectTabController().setOldCode(((Prelevement) getCopy()).getCode());
      }

   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.prelevement.getCode() != null){
         this.prelevement.setCode(this.prelevement.getCode().toUpperCase().trim());
      }
   }

   @Override
   public void setFocusOnElement(){
      codeBoxPrlvt.setFocus(true);
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/
   public ConstCode getCodeConstraint(){
      return PrelevementConstraints.getCodeConstraint();
   }

   public ConstCode getCodeNullConstraint(){
      return PrelevementConstraints.getCodeNullConstraint();
   }

   public ConstWord getNomConstraint(){
      return PatientConstraints.getNomConstraint();
   }

   public ConstWord getNomNullConstraint(){
      return PatientConstraints.getNomNullConstraint();
   }

   public ConstInt getConditNbConstraint(){
      return PrelevementConstraints.getNbConditConstraint();
   }

   public ConstCode getNdaConstraint(){
      return PrelevementConstraints.getNdaConstraint();
   }

   @Override
   protected void validateCoherenceDate(final Component comp, final Object value){
      Errors errs = null;
      String field = "";

      if(value == null || value.equals("")){
         // la contrainte est retiree
         //((Datebox) comp).setConstraint("");

         if(comp.getId().equals("datePrelCalBox")){
            ((CalendarBox) comp).clearErrorMessage(datePrelCalBox.getValue());
            ((CalendarBox) comp).setValue(null);
            this.prelevement.setDatePrelevement(null);
         }else if(comp.getId().equals("dateConsentBoxPrlvt")){
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
            this.prelevement.setConsentDate(null);
         }
      }else{

         // save les bindings des objets embarqués si besoin
         setEmbeddedObjects();
         this.prelevement.setMaladie(this.maladie);

         // date prelevement
         if(comp.getId().equals("datePrelCalBox")){
            field = "datePrelevement";
            this.prelevement.setDatePrelevement((Calendar) value);
            errs = ManagerLocator.getPrelevementValidator().checkDatePrelevementCoherence(this.prelevement);
         }

         // date consentement
         if(comp.getId().equals("dateConsentBoxPrlvt")){
            field = "consentDate";
            this.prelevement.setConsentDate((Date) value);
            errs = ManagerLocator.getPrelevementValidator().checkDateConsentCoherence(this.prelevement);
         }

         if(errs != null && errs.hasErrors()){
            Clients.scrollIntoView(comp);
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   /**
    * Validation "à la volée" du code prélèvement saisi
    * @param prltCode
    */
   private void validatePrelevementCode(final String prltCode){

      //Vérification de l'absence de doublons
      final List<Prelevement> doublons = ManagerLocator.getManager(PrelevementManager.class)
         .findByCodeInPlateformeManager(prltCode, SessionUtils.getCurrentPlateforme());

      if(!doublons.isEmpty()){
         final String collectionsDoublon =
            doublons.stream().map(Prelevement::getBanque).map(Banque::getNom).collect(Collectors.joining(", "));
         throw new WrongValueException(codeBoxPrlvt,
            Labels.getLabel("error.validation.doublon.code", new String[] {prltCode, collectionsDoublon}));
      }

   }

   /**
    * Remplissage automatique avec la date (sans heures) de prelevement
    * si champ vide, déclenche validation sinon.
    */
   public void onBlur$dateConsentBoxPrlvt(){
      boolean badDateFormat = false;
      if(dateConsentBoxPrlvt.getErrorMessage() != null
         && dateConsentBoxPrlvt.getErrorMessage().contains(dateConsentBoxPrlvt.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         if(!dateConsentBoxChanged && dateConsentBoxPrlvt.getValue() == null && prelevement.getDatePrelevement() != null){
            dateConsentBoxPrlvt.setValue(prelevement.getDatePrelevement().getTime());
         }else{
            dateConsentBoxPrlvt.clearErrorMessage(true);
            validateCoherenceDate(dateConsentBoxPrlvt, dateConsentBoxPrlvt.getValue());
         }
      }
      dateConsentBoxChanged = true;

   }

   /**
    * à la sortie du champs Date de Prélèvement
    */
   public void onBlur$datePrelCalBox(){
      getObjectTabController().setDateFieldModified(true);
      datePrelCalBox.clearErrorMessage(datePrelCalBox.getValue());
      if(getObjectTabController().isFicheLaboOpened()){
         prelevement.setLaboInters(new HashSet<>(getObjectTabController().getFicheLaboInter().getLaboInters()));
      }

      validateCoherenceDate(datePrelCalBox, datePrelCalBox.getValue());

      // remet à null le calcul du delai congelation au besoin
      if(getObjectTabController().getNextToEchanClicked()){
         ((EchantillonController) getObjectTabController().getReferencedObjectsControllers(true).get(0)).getMultiFicheEdit()
            .resetDelaiCgl();
      }
   }

   /**
    * Lance la validation de toutes les dates de prelevement car
    * les dates de references du Patient ont pu changer a posteriori.
    */
   public void validateAllDateComps(){
      if(datePrelCalBox.isVisible()){
         datePrelCalBox.clearErrorMessage(datePrelCalBox.getValue());
         validateCoherenceDate(datePrelCalBox, datePrelCalBox.getValue());
      }
      if(dateConsentBoxPrlvt.isVisible()){
         dateConsentBoxPrlvt.clearErrorMessage(true);
         validateCoherenceDate(dateConsentBoxPrlvt, dateConsentBoxPrlvt.getValue());
      }
   }

   public List<Nature> getNatures(){
      return natures;
   }

   public void setNatures(final List<Nature> n){
      this.natures = n;
   }

   public Nature getSelectedNature(){
      return selectedNature;
   }

   public void setSelectedNature(final Nature selected){
      this.selectedNature = selected;
   }

   public List<PrelevementType> getModes(){
      return modes;
   }

   public void setModes(final List<PrelevementType> m){
      this.modes = m;
   }

   public PrelevementType getSelectedMode(){
      return selectedMode;
   }

   public void setSelectedMode(final PrelevementType selected){
      this.selectedMode = selected;
   }

   public List<ConditType> getConditTypes(){
      return conditTypes;
   }

   public void setConditTypes(final List<ConditType> cTypes){
      this.conditTypes = cTypes;
   }

   public ConditType getSelectedConditType(){
      return selectedConditType;
   }

   public void setSelectedConditType(final ConditType selected){
      this.selectedConditType = selected;
   }

   public List<ConditMilieu> getConditMilieus(){
      return conditMilieus;
   }

   public void setConditMilieus(final List<ConditMilieu> cMilieus){
      this.conditMilieus = cMilieus;
   }

   public ConditMilieu getSelectedConditMilieu(){
      return selectedConditMilieu;
   }


   public void setSelectedConditMilieu(final ConditMilieu selected){
      this.selectedConditMilieu = selected;
   }

   public List<ConsentType> getConsentTypes(){
      return consentTypes;
   }

   public void setConsentTypes(final List<ConsentType> cTypes){
      this.consentTypes = cTypes;
   }

   public ConsentType getSelectedConsentType(){
      return selectedConsentType;
   }

   public void setSelectedConsentType(final ConsentType selected){
      this.selectedConsentType = selected;
   }

   public List<Etablissement> getEtablissements(){
      return etablissements;
   }

   public void setEtablissements(final List<Etablissement> etabs){
      this.etablissements = etabs;
   }

   public Etablissement getSelectedEtablissement(){
      return selectedEtablissement;
   }

   public void setSelectedEtablissement(final Etablissement selected){
      this.selectedEtablissement = selected;
   }

   public List<Service> getServices(){
      return services;
   }

   public void setServices(final List<Service> s){
      this.services = s;
   }

   public Service getSelectedService(){
      return selectedService;
   }

   public void setSelectedService(final Service selected){
      this.selectedService = selected;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final List<Collaborateur> collabs){
      this.collaborateurs = collabs;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selected){
      this.selectedCollaborateur = selected;
   }

   public List<LaboInter> getOldLabos(){
      return oldLabos;
   }

   public void setOldLabos(final List<LaboInter> old){
      this.oldLabos = old;
   }

   public List<Risque> getRisques(){
      return risques;
   }

   public void setRisques(final List<Risque> _r){
      risques.clear();
      risques.addAll(_r);
   }

   /**
    * Clic sur le bouton générant un code.
    */
   @Override
   public void onClick$numerotation(){
      prelevement.setCode(generateCodeAndUpdateNumerotation());
   }

   /**
    * Informe sous la forme d'un warning
    * l'utilisateur si un prélèvement en cours de creation est
    * associée à une maladie null donc à AUCUN patient.
    *
    * @return true si le patient continue la procédure
    */
   protected boolean warnNoPatient(){
      boolean ok = false;

      // si aucun patient défini
      if(this.prelevement.getPrelevementId() == null && this.maladie == null){

         final ReferenceurPatient ref = (ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true);
         String labelError;
         String labelWarning;
         // si la case "aucun" est cochée, on demande confirmation
         // à l'utilisateur
         // sinon, l'utilisateur ne pourra pas continuer.
         if(ref != null){
            if(ref.getSelectedPatient() == null){
               labelError = "fichePrelevement.noPatient.error";
               labelWarning = "fichePrelevement.noPatient.warning";
            }else{
               labelError = "fichePrelevement.noMaladie.error";
               labelWarning = "fichePrelevement.noMaladie.warning";
            }

            if(ref.getNoRadio().isChecked()){
               if(Messagebox.show(Labels.getLabel(labelWarning), Labels.getLabel("general.warning"),
                  Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION) == Messagebox.YES){
                  ok = true;
               }
            }else{
               ok = false;
               Messagebox.show(Labels.getLabel(labelError), Labels.getLabel("general.error"), Messagebox.OK, Messagebox.ERROR);
            }
         }
      }else{
         ok = true;
      }
      return ok;
   }

   /**
    * Rend le bouton "interfacage" de la fiche visible si des
    * ont été définis pour la banque.
    */
   public void setInterfacageButtonVisible(){
      if(getObjectTabController() != null && getObjectTabController().getEntiteTab() != null){
         if(SessionUtils.getEmetteursInterfacages(sessionScope).size() > 0){
            interfacage.setVisible(true);
         }else{
            interfacage.setVisible(false);
         }
      }else{
         interfacage.setVisible(false);
      }
   }

   public void onClick$interfacage(){
      final String value = codeBoxPrlvt.getValue();
      openSelectDossierExterneWindow(page, Path.getPath(self), value, false, null, false);
   }

   /**
    * Récupère le résultat de l'injection d'un dossier externe.
    *
    * @param e
    */
   public void onGetInjectionDossierExterneDone(final Event e){
      if(e.getData() != null){
         final ResultatInjection res = (ResultatInjection) e.getData();
         SessionUtils.setDossierExterneInjection(sessionScope, res);
         // getObjectTabController().setDossierExterne(
         //		res.getDossierExterne());

         Prelevement newObj = new Prelevement();
         Patient newPat = new Patient();
         if(res.getPrelevement().clone() != null){
            newObj = res.getPrelevement().clone();
            newObj.setBanque((Banque) sessionScope.get("Banque"));
            if(newObj.getMaladie() != null && newObj.getMaladie().getPatient() != null){
               newPat = newObj.getMaladie().getPatient();
            }
         }
         setObject(newObj);
         if(getObjectTabController() != null && getObjectTabController().getFicheAnnotation() != null
            && res.getAnnosPrelevement() != null){
            getObjectTabController().getFicheAnnotation().setAnnotationValues(res.getAnnosPrelevement());
         }

         setParentObject(newObj.getMaladie());
         initSelectedInLists();
         // initCollaborations();
         selectCollaborations();
         if(getObjectTabController().canUpdateAnnotation()){
            getObjectTabController().getFicheAnnotation().switchToStaticOrEditMode(false, false);
         }

         if(newObj.getMaladie() != null && referenceur != null){ // initialize le referenceur
            ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true))
               .setPatientAndMaladieFromOutSideReferenceur(newPat, newObj.getMaladie(), newObj.getPatientNda());
         }

         getBinder().loadComponent(self);

         // ajout opération consultation
         if(res.getDossierExterne() != null){
            ManagerLocator.getConsultationIntfManager().createObjectManager(res.getDossierExterne().getIdentificationDossier(),
               Calendar.getInstance(), res.getDossierExterne().getEmetteur().getIdentification(),
               SessionUtils.getLoggedUser(sessionScope));
         }
      }
   }

   public void injectPatientAndMaladieInFiche(final Patient pat, final Maladie mal){

      setParentObject(mal);

      // initialize le referenceur
      ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true))
         .setPatientAndMaladieFromOutSideReferenceur(pat, mal, null);
   }

   /**
    * Methode implémentée par la classe enfant
    * FichePrelevementEditSero.
    */
   public void clearProtocoles(){}

   @Override
   public boolean isAnonyme(){
      return isAnonyme;
   }

   @Override
   public void setAnonyme(final boolean isA){
      this.isAnonyme = isA;
   }

   /*************************************************************************/
   /************************** ASSOCIATIONS *********************************/
   /*************************************************************************/
   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * operateurAideSaisiePrel. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection du préleveur.
    */
   public void onClick$operateurAideSaisiePrel(){
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
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * operateurAideSaisieServ. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection du service.
    */
   public void onClick$operateurAideSaisieServ(){
      // on récupère le collaborateur actuellement sélectionné
      // pour l'afficher dans la modale
      final List<Object> old = new ArrayList<>();
      if(selectedService != null){
         old.add(selectedService);
      }

      // ouvre la modale
      openCollaborationsWindow(page, "general.recherche", "select", null, "Service", null, Path.getPath(self), old);
   }

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand
    * l'utilisateur sélectionne un collaborateur.
    *
    * @param e Event contenant le collaborateur sélectionné.
    * @throws Exception
    */
   public void onGetObjectFromSelection(final Event e) throws Exception{

      // les collaborateurs peuvent être modifiés dans la fenêtre
      // d'aide => maj de ceux-ci
      etablissements = ManagerLocator.getEtablissementManager().findAllActiveObjectsWithOrderManager();
      etablissements.add(0, null);
      services = ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager();
      services.add(0, null);
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      collaborateurs.add(0, null);

      if(this.prelevement.getServicePreleveur() != null && !services.contains(this.prelevement.getServicePreleveur())){
         services.add(this.prelevement.getServicePreleveur());
      }
      if(this.prelevement.getPreleveur() != null && !collaborateurs.contains(this.prelevement.getPreleveur())){
         collaborateurs.add(this.prelevement.getPreleveur());
      }

      servicesBoxPrlvt.setModel(new ListModelList<>(services));
      collaborateursBoxPrlvt.setModel(new ListModelList<>(collaborateurs));

      // si un collaborateur a été sélectionné
      if(e.getData() != null){
         selectedEtablissement = null;
         if(e.getData() instanceof Collaborateur){
            selectedCollaborateur = (Collaborateur) e.getData();
            selectedService = null;
            collaborateursBoxPrlvt.setSelectedIndex(collaborateurs.indexOf(selectedCollaborateur));
         }else if(e.getData() instanceof Service){
            selectedService = (Service) e.getData();
            servicesBoxPrlvt.setSelectedIndex(services.indexOf(selectedService));
            onSelect$servicesBoxPrlvt(e);
         }
      }

      getBinder().loadComponent(etabsBoxPrlvt);
      getBinder().loadComponent(servicesBoxPrlvt);
      getBinder().loadComponent(collaborateursBoxPrlvt);
   }

   /**
    * Peuple la table des consentements utilisés pour le patient.
    *
    * @param pat pat
    * @since 2.0.13
    */
   public void initConsentTypesUsedTable(final Patient pat){
      consentTypeUsed.clear();
      if(pat.getPatientId() != null){
         final List<Prelevement> prels = ManagerLocator.getPrelevementManager().findByPatientAndAuthorisationsManager(pat,
            SessionUtils.getCurrentPlateforme(), SessionUtils.getLoggedUser(sessionScope));
         ConsentType ct;
         for(final Prelevement prelevement : prels){
            ct = prelevement.getConsentType();
            // ajout key type used si n'existe pas
            if(!consentTypeUsed.containsKey(ct)){
               consentTypeUsed.put(ct, new ArrayList<PrelevementDecorator2>());
            }
            consentTypeUsed.get(ct).add(new PrelevementDecorator2(prelevement));
         }
         consentTypeUsed.setPatient(pat);
      }
   }

   /**
    * Affiche la modale détaillant les prélèvements consultables et leurs
    * types de consentement associés.
    * Ajoute ou nettoie également une coloration des items dans
    * la liste des consentements pour
    * faciliter la sélection.
    *
    * @since 2.0.13
    */
   public void onClick$typeUsedBulb(){
      if(this.maladie != null){
         if(!this.maladie.getPatient().equals(consentTypeUsed.getPatient())){
            initConsentTypesUsedTable(this.maladie.getPatient());
         }
         openConsentTypeUsed(consentTypeUsed);
      }else{
         consentTypeUsed.clear();
      }
      colorConsentTypeItem(maladie == null);
   }

   /**
    * Colore les items de la liste dont la valeur (ConsentType) correspond
    * à un consentement déja utilisé pour un autre prélèvement consultable.
    * Nettoie la coloration pour les autres (background: transparent)
    *
    * @param clear force nettoyage couleur
    * @since 2.0.13
    */
   protected void colorConsentTypeItem(final boolean clear){
      for(final Component li : consentTypesBoxPrlvt.getChildren()){
         if(li instanceof Listitem && ((Listitem) li).getValue() != null){
            if(!clear && consentTypeUsed.containsKey(((Listitem) li).getValue())){
               ((Listitem) li).setStyle("background-color: #b6fcd5");
            }else{ // clear color
               ((Listitem) li).setStyle("background-color: transparent");
            }
         }
      }
   }

   /**
    * Surcharge de l'ouverture de la modale recherche de patient
    * afin d'ajouter un clean up de la table des consentements utilisés
    * et des couleurs de certains items de la liste.
    *
    * @since 2.0.13
    */
   @Override
   public void openSelectPatientWindow(final String path, final String returnMethode, final Boolean isFusionPatients,
      final String critere, final Patient patAExclure, final Contexte contexte, final Banque banque){
      super.openSelectPatientWindow(path, returnMethode, isFusionPatients, critere, patAExclure, contexte, banque);
      consentTypeUsed.clear();
      colorConsentTypeItem(true);
   }

   /**
    * Validation sécifique des listboxes obligatoires
    * Gatsbi surcharge cette méthode
    * @since 2.3.0-gatsbi
    */
   protected void checkRequiredListboxes(){

      if(selectedNature == null){
         Clients.scrollIntoView(naturesBoxPrlvt);
         throw new WrongValueException(naturesBoxPrlvt, Labels.getLabel("fichePrelevement.error.nature"));
      }
      Clients.clearWrongValue(naturesBoxPrlvt);
      if(selectedConsentType == null){
         Clients.scrollIntoView(consentTypesBoxPrlvt);
         throw new WrongValueException(consentTypesBoxPrlvt, Labels.getLabel("fichePrelevement.error.consenType"));
      }
      Clients.clearWrongValue(consentTypesBoxPrlvt);
   }
   
   // @since 2.3.0-gatsbi,sera rédéfinie
   protected String getRefPatientCompPath() {
      return "/zuls/prelevement/ReferenceurPatient.zul";
   }
}
