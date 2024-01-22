/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.decorator.gatsbi.PatientItemRendererGatsbi;
import fr.aphp.tumorotek.dto.ServicesEtEtablissementsLiesADesCollaborateurs;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche formulaire d'un prélèvement sous le gestionnaire
 * GATSBI. Controller créé le 25/05/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePrelevementEditGatsbi extends FichePrelevementEdit
{

   private static final long serialVersionUID = 1L;

   private final List<Listbox> reqListboxes = new ArrayList<>();

   private final List<Combobox> reqComboboxes = new ArrayList<>();

   private final List<Div> reqConformeDivs = new ArrayList<>();

   // @wire
   private Groupbox groupPrlvt;

   private Contexte contexte;
   
   private Div ndaDiv;


   //TG-206 (Préleveur comme thesaurus et déduction du filtre pour le service préleveur à partir de celui-ci)
   //Ainsi si un filtre est défini dans Gatsbi sur le préleveur, ce champ est maître pour l'affichage 
   //des listes services et établissement. On garde l'information dans un champ
   private boolean filterOnPreleveurDefined = false;
   
   //booleans indiquant si les champs préleveur et service préleveur sont visibles
   private boolean champPreleveurVisible = true;
   private boolean champServicePreleveurVisible = true;
   
   private Label operateurAideSaisiePrel;
   private Label operateurAideSaisieServ;
   //TG-206 fin
   
   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      contexte = GatsbiController.initWireAndDisplay(this, 2, true, reqListboxes, reqComboboxes, reqConformeDivs, groupPrlvt,
         groupPrlvt);
      
      // affichage conditionnel des champs patients
      GatsbiControllerPrelevement.applyPatientContext(groupPatient, true);

      // setRows ne marche pas ?
      // seul moyen trouvé pour augmenter hauteur et voir tous les items de la listbox
      //  correction bug TG-124 (getThesaurusValuesForChampEntiteId ne récupère pas tous les thesaurus quand l'utilisateur n'a rien défini dans Gatsbi) :
      //  risquesBox.setHeight(contexte.getThesaurusValuesForChampEntiteId(249).size() * 25 + "px");
      //  une évolution a été faite côté Gatsbi pour envoyer tous les thesaurus quand l'utilisateur n'en a sélectionné aucun (TG-148)
      //  => la ligne ci-dessus fonctionnerait désormais...
      risquesBox.setHeight(reqListboxes.size() * 25 + "px");
   }
   
   @Override
   protected String getRefPatientCompPath() {
      return "/zuls/prelevement/gatsbi/ReferenceurPatientGatsbi.zul";
   }

   @Override
   protected ResumePatient initResumePatient(){
      return new ResumePatient(groupPatient, 
         SessionUtils.getCurrentGatsbiContexteForEntiteId(1), 
         SessionUtils.getCurrentBanque(sessionScope));      
   }

   @Override
   protected void enablePatientGroup(final boolean b){
      ((Groupbox) this.groupPatient).setOpen(b);
      ((Groupbox) this.groupPatient).setClosable(b);
   }

   /**
    * Surcharge Gastbi pour conserver sélectivement la
    * contrainte de sélection obligatiure des listes nature et statut juridique
    * dans le contexte TK historique
    */
   @Override
   protected void checkRequiredListboxes(){
      GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
   }

   /**
    * Processing echoEvent. Gatsbi surcharge... si aucun champ de formulaire dans
    * la page de transfert vers le site de stockage, passe directement à
    * l'échantillon.
    *
    * @see onClick$next
    */
   @Override
   public void onLaterNextStep(){

      log.debug("Surcharge Gastbi pour vérifier que la page de transfert des sites intermédiaire est affichée");

      // vérifie si au moins un des champs de formulaires est affiché
      final boolean oneDivVisible = GatsbiControllerPrelevement.isSitesIntermPageDisplayed(contexte);

      if(oneDivVisible){
         super.onLaterNextStep();
      }else{ // aucun formulaire n'est affiché -> passage direct à l'onglet échantillon
         log.debug("Aucun formulaire à affiché dans la page transfert vers le site préleveur...");
         if(this.prelevement.getPrelevementId() != null){
            getObjectTabController().switchToMultiEchantillonsEditMode(this.prelevement, new ArrayList<LaboInter>(),
               new ArrayList<LaboInter>());
         }else{
            // si nous sommes dans une action de création, on
            // appelle la page FicheMultiEchantillons en mode create
            getObjectTabController().switchToMultiEchantillonsCreateMode(this.prelevement, new ArrayList<LaboInter>());
         }

         Clients.clearBusy();
      }
   }
   
   @Override
   protected void setEmptyToNulls(){
      if(prelevement.getNumeroLabo().equals("")){
         prelevement.setNumeroLabo(null);
      }
      
      // patientNda required constraint
      // s'applique directement sur FichePrelevementEdit
      // sinon impossible d'appliquer constraint côté client 
      // sur ReferenceurPatient et FichePatientEdit
      // car pas de binding sur evenement possible
      if(ndaBox != null) { // concerne ReferenceurPatient et FichePatientEdit
         if (prelevement.getMaladie() != null || maladie != null) { // pas de bloc patient, le nda est toujours null
            if (!StringUtils.isEmpty(ndaBox.getValue())) {
               this.prelevement.setPatientNda(ndaBox.getValue());
            }else if (contexte.isChampIdRequired(44) 
                  && ndaBox.getParent().isVisible() && ndaBox.getParent().getParent().isVisible()) {
               throw new WrongValueException(ndaBox, Labels.getLabel("anno.alphanum.empty"));
            }else{
               this.prelevement.setPatientNda(null);
            }
         } else {
            prelevement.setPatientNda(null);
         }  
      }
      
      // nettoyage ancienne données si besoin
      if(prelevement.getPatientNda() != null && prelevement.getPatientNda().equals("")){
         prelevement.setPatientNda(null);
      }
   }

   /**
    * Surcharge pour gérer la redirection d'évènement lors du choix d'un paramétrage
    */
   @Override
   public void onGetInjectionDossierExterneDone(final Event e){
      super.onGetInjectionDossierExterneDone(((ForwardEvent) e).getOrigin());
   }
   
   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();
      
      // nda n'est visible que si bloc patient
      if (getMaladie() != null) {
         // applique une éventuelle contrainte d'obligation sur ndaDiv 
         GatsbiControllerPrelevement.applyPatientNdaRequired(ndaDiv);
      } else {
         GatsbiControllerPrelevement.removePatientNdaRequired(ndaDiv);
      }
      
   }
   
   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      
      // nda n'est visible que si bloc patient
      if (prelevement.getMaladie() != null) {
         // applique une éventuelle contrainte d'obligation sur ndaDiv 
         GatsbiControllerPrelevement.applyPatientNdaRequired(ndaDiv);
      }
      
   }
   
   @Override
   protected void setEmbeddedObjects(){
            
      // triggers identifiant textbox validation
      if (referenceur != null) {
         Textbox identifiantBox = ((PatientItemRendererGatsbi) 
            ((ReferenceurPatientGatsbi) referenceur.getFellow("winRefPatient")
                     .getAttributeOrFellow("winRefPatient$composer", true))
         .getPatientRenderer()).getIdentifiantBox();
         
         if (identifiantBox != null) {
            String identifiant = identifiantBox.getValue();
            
            if (StringUtils.isBlank(identifiant.trim())) {
               throw new WrongValueException(identifiantBox, "no empty");
            }
            
            ((Patient) identifiantBox.getAttribute("patient")).addToIdentifiants(identifiant.trim(), prelevement.getBanque());
         }
      }
      
      super.setEmbeddedObjects();
   }

   /**
    * Plus d'obligation
    */
   @Override
   public void onSelect$naturesBoxPrlvt(){}

   @Override
   public void onSelect$consentTypesBoxPrlvt(){}

   public Contexte getContexte(){
      return contexte;
   }

   /**
    * surcharge pour gérer les services préleveurs à partir du filtre défini sur les préleveur considérés comme un thesaurus Collaborateur
    * @since Gatsbi : TG-206 
    */
   @Override
   protected void initAllCollaborationPossible(){
      Contexte contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);
      //Le champ préleveur est-il visbible ?
      champPreleveurVisible = contexte.isChampIdVisible(28);
      //Le champ service préleveur est-il visbible ?
      champServicePreleveurVisible = contexte.isChampIdVisible(29);
      
      if(champPreleveurVisible) {
         //récupération de tous les collaborateurs (cas hors Gatsbi) pour ensuite filtrer éventuellement :
         List<Collaborateur> allCollaborateurSansFiltreGatsbi =
            ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
   
         //application du filtre en fonction du paramétrage Gatsbi
         List<Collaborateur> listCollaborateurFiltree =
            GatsbiController.filterExistingListModel(contexte, allCollaborateurSansFiltreGatsbi, 28);
         if(allCollaborateurSansFiltreGatsbi.size() == listCollaborateurFiltree.size()){
            populateAllEtablissementsAndAllServicesInDatabaseIfNecessary();
            allCollaborateurs = addNullCollaborateurIfNecessary(allCollaborateurSansFiltreGatsbi);
            filterOnPreleveurDefined = false;
         }else{
            populateEtablissementsServicesAndCollaborateurs(listCollaborateurFiltree);
            filterOnPreleveurDefined = true;
         }
         //on n'affiche pas l'aide à la saisie sur un filtre est défini sur les collaborateurs
         operateurAideSaisiePrel.setVisible(!filterOnPreleveurDefined);
      }
      else {
         populateAllEtablissementsAndAllServicesInDatabaseIfNecessary();
         allCollaborateurs = new ArrayList<Collaborateur>();
      }
      
      if(champServicePreleveurVisible) {
         //on n'affiche pas l'aide à la saisie sur un filtre est défini sur les collaborateurs
         operateurAideSaisieServ.setVisible(!filterOnPreleveurDefined);
      }
   }
   
   //TG-206 : si un filtre collaborateur est défini, on remonte les services à partir du collaborateur
   protected void doAfterSelectingCollaborateur() {
      if(champPreleveurVisible && filterOnPreleveurDefined) {
         if(selectedCollaborateur != null) {
            Etablissement currentSelectedEtablissement = selectedEtablissement;
            //récupération de ses services :
            List<Service> servicesDuCollaborateur =
               ManagerLocator.getServiceManager().findServicesActifsForOneCollaborateur(selectedCollaborateur);
            services = addNullServiceIfNecessary(servicesDuCollaborateur);
            if(servicesDuCollaborateur != null && servicesDuCollaborateur.size() == 1){
               selectedService = servicesDuCollaborateur.get(0);
               if(selectedService != null){
                  selectedEtablissement = selectedService.getEtablissement();
               }
            }else{
               selectedService = null;
               selectedEtablissement = selectedCollaborateur.getEtablissement();
            }

            //si l'établissement a été modifié (ça veut dire qu'avant il était null, sinon tous les services proposés appartiennent au même établissement),
            //filtre pour ne garder que les services de cet établissement et les collaborateurs de ces services
            if(selectedEtablissement != currentSelectedEtablissement){
               refreshServicesAndCollaborateursFromSelectedEtablissement();
            }

            refreshCollaborationComponents();            
         }
      }
   }
   
   private void populateAllEtablissementsAndAllServicesInDatabaseIfNecessary() {
      if(champServicePreleveurVisible) {
         allEtablissements =
            addNullEtablissementIfNecessary(ManagerLocator.getEtablissementManager().findAllActiveObjectsWithOrderManager());
         allServices = addNullServiceIfNecessary(ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager());
      }
      else {
         allEtablissements = new ArrayList<Etablissement>();
         allServices = new ArrayList<Service>();
      }
   }

   //Charge les listes établissements et services à partir des données du collaborateur
   private void populateEtablissementsServicesAndCollaborateurs(List<Collaborateur> listCollaborateurFiltree){
      //les services ne sont pas accessibles depuis le collaborateur (lazy implementation et pas de récupération avant "détachement du JPA" des objets services)
      //=> passage par un service :
      if(champServicePreleveurVisible) {
         ServicesEtEtablissementsLiesADesCollaborateurs servicesEtEtabs =
            ManagerLocator.getServiceManager().retrieveServicesEtEtablissementsLiesADesCollaborateurs(listCollaborateurFiltree);
         allEtablissements = addNullEtablissementIfNecessary(
            new ArrayList<Etablissement>(servicesEtEtabs.getEtablissementsActifsDesCollaborateurs()));
         allServices = addNullServiceIfNecessary(new ArrayList<Service>(servicesEtEtabs.getServicesActifsDesCollaborateurs()));
      }
      else {
         allEtablissements = new ArrayList<Etablissement>();
         allServices = new ArrayList<Service>();
      }
      
      if(champPreleveurVisible) {
         allCollaborateurs = addNullCollaborateurIfNecessary(new ArrayList<Collaborateur>(listCollaborateurFiltree));
      }
      else {
         allCollaborateurs = new ArrayList<Collaborateur>();
      }
   }

   /**
    * Sélectionne le collaborateur.
    *
    * @param event Event : seléction sur la liste collaborateursBoxPrlvt.
    * @throws Exception
    */
   public void onSelect$collaborateursBoxPrlvt(final Event event) throws Exception{
      //le champ préleveur est "maître" pour les 2 listes services et établissement
      if(filterOnPreleveurDefined){
         final int ind = collaborateursBoxPrlvt.getSelectedIndex();
         selectedCollaborateur = collaborateurs.get(ind);
         doAfterSelectingCollaborateur();
      }else{
         super.onSelect$collaborateursBoxPrlvt(event);
      }
   }

   @Override
   public void onSelect$servicesBoxPrlvt(final Event event) throws Exception{
      super.onSelect$servicesBoxPrlvt(event);
      //mise à jour éventuel de l'établissement si celui-ci est null 
      //(cas d'une page initialisée avec un collaborateur sélectionné ayant plusieurs services sur plusieurs établissements => c'est à l'utilisateur de choisir le service)
      if(selectedEtablissement == null){
         // /!\ la sélection du service ne doit pas être perdu => il faudra surcharger la règle générale contenue dans selectEtablissementFromSelectedServiceAndRefreshList
         Service currentSelectedService = selectedService;
         selectEtablissementFromSelectedServiceAndRefreshList();
         selectedService = currentSelectedService;
         refreshCollaborationComponents();
      }
   }

   @Override
   protected void initCollaborateurs(){
      if(champPreleveurVisible) {
         // /!\ bien qu'on appelle la méthode super, la ligne ci-dessous est bien spécifique à Gatsbi (application du filtre si existant)
         // car super.initCollaborateurs() appelle populateCollaborateursForSelectedService() qui
         // s'appuie sur retrieveActiveCollaborateursWithOrder(), méthode surchargé dans Gatsbi
         super.initCollaborateurs();
   
         //dans le cas de Gatsbi, si un filtre est défini sur les collaborateurs,
         //on peut remonter du service à l'établissement 
         if(filterOnPreleveurDefined){
            if(selectedService == null){ //
               if(allEtablissements.size() == 2){
                  selectedEtablissement = allEtablissements.get(1);
               }else{
                  selectedEtablissement = null;
               }
            }else{
               selectedEtablissement = selectedService.getEtablissement();
            }
            if(collaborateurs.size() == 2){
               selectedCollaborateur = allCollaborateurs.get(1);
            }else{
               if(!collaborateurs.contains(selectedCollaborateur)){
                  selectedCollaborateur = null;
               }
            }
         }
      }
   }

   // le nombre de collaborateurs étant relativement faible car un filtre est appliqué, si l'utilisateur
   // ne sélectionne pas de service alors qu'un établissement est sélectionné,
   // on affichera uniquement les collaborateurs rattachés à un service de l'établissement
   // /!\ un collaborateur sans service mais défini sur l'établissement sélectionné n'apparaîtra pas contrairement au cas standard 
   @Override
   protected void populateCollaborateursForSelectedService(){
      if(filterOnPreleveurDefined) {
         if(champPreleveurVisible) {
            if(selectedService != null){
               collaborateurs = addNullCollaborateurIfNecessary(retrieveActiveCollaborateursWithOrder(selectedService));
            }else{
               if(selectedEtablissement == null) {
                  collaborateurs = allCollaborateurs;
               }
               else {
                  populateCollaborateurForSelectedEtablissement();
               }
            }
         }
      }
      else {
         super.populateCollaborateursForSelectedService();
      }
   }
   

   
   //affiche tous les collaborateurs rattachés à tous les services possibles à l'instant t (services de 
   //l'établissement sélectionné)
   @Override
   protected void populateCollaborateurForSelectedEtablissement(){
      if(selectedEtablissement != null && filterOnPreleveurDefined){
         populateCollaborateursForAllServicesOfSelectedEtablissement();
      }
      else {
         super.populateCollaborateurForSelectedEtablissement();
      }
   }
   
   private void populateCollaborateursForAllServicesOfSelectedEtablissement() {
      //on affiche tous les collaborateurs des services (de l'établissement sélectionné) :
      List<Collaborateur> collaborateursForSelectedEtablissement = new ArrayList<Collaborateur>();
      for(Service service : services){
         if(service !=null) {
            //un collaborateur peut appartenir à plusieurs services : il ne doit être présent qu'une fois
            List<Collaborateur> collaborateursForService = filterCollaborateursByService(service);
            for(Collaborateur collabToAdd : collaborateursForService){
               if(collabToAdd != null && !collaborateursForSelectedEtablissement.contains(collabToAdd)){
                  collaborateursForSelectedEtablissement.add(collabToAdd);
               }
            }
         }
      }
      //tri :
      Collections.sort(collaborateursForSelectedEtablissement, Comparator.comparing(Collaborateur::getNomAndPrenom));
      collaborateurs = addNullCollaborateurIfNecessary(collaborateursForSelectedEtablissement);
   }
   
   @Override
   protected List<Collaborateur> retrieveActiveCollaborateursWithOrder(Service selectedService){
      if(champPreleveurVisible) {
         if(selectedService == null){
            return allCollaborateurs;
         }
   
         List<Collaborateur> allCollaborateursForService = super.retrieveActiveCollaborateursWithOrder(selectedService);
         if(filterOnPreleveurDefined){
            List<Collaborateur> collaborateursForServiceToReturn = new ArrayList<Collaborateur>();
   
            //filtre pour ne garder que ceux présents dans all :
            for(Collaborateur collaborateur : allCollaborateursForService){
               if(allCollaborateurs.contains(collaborateur)){
                  collaborateursForServiceToReturn.add(collaborateur);
               }
            }
   
            return collaborateursForServiceToReturn;
         }
   
         return allCollaborateursForService;
      }
      
      return new ArrayList<Collaborateur>();
   }

   protected List<Service> retrieveActiveServicesWithOrder(Etablissement selectedEtablissement){
      if(champServicePreleveurVisible) {
         if(selectedEtablissement == null){
            return allServices;
         }
   
         List<Service> allServicesForEtablissement = super.retrieveActiveServicesWithOrder(selectedEtablissement);
         if(filterOnPreleveurDefined){
            List<Service> serviceForEtablissementToReturn = new ArrayList<Service>();
   
            //filtre pour ne garder que ceux présents dans all :
            for(Service service : allServicesForEtablissement){
               if(allServices.contains(service)){
                  serviceForEtablissementToReturn.add(service);
               }
            }
   
            return serviceForEtablissementToReturn;
         }
   
         return allServicesForEtablissement;
      }
      
      return new ArrayList<Service>();
   }

   private void selectEtablissementFromSelectedServiceAndRefreshList(){
      if(selectedService != null){
         selectedEtablissement = selectedService.getEtablissement();
         refreshServicesAndCollaborateursFromSelectedEtablissement();
      }
   }

   private void refreshServicesAndCollaborateursFromSelectedEtablissement(){
      if(selectedEtablissement == null){
         services = allServices;
         collaborateurs = allCollaborateurs;
      }else{
         if(champServicePreleveurVisible) {
            //récupération de tous les services de l'établissement sélectionné
            //pour lesquels au moins un collaborateur fait partie du filtre Gatsbi
            // => utilisation de allServices déjà filtré sur les collaborateurs avec suppression
            // de la valeur null (subList)
            List<Service> serviceForSelectedEtablissement = allServices.subList(1, allServices.size()).stream()
               .filter(service -> service.getEtablissement().equals(selectedEtablissement)).collect(Collectors.toList());
            services = addNullServiceIfNecessary(serviceForSelectedEtablissement);
   
            if(services.size() == 2){
               selectedService = services.get(1);
               if(champPreleveurVisible) {
                  collaborateurs = addNullCollaborateurIfNecessary(filterCollaborateursByService(selectedService));
               }
            }else{
               selectedService = null;
               
               if(champPreleveurVisible) {
                  populateCollaborateursForAllServicesOfSelectedEtablissement();
               }
            }
         }
         else {
            if(champPreleveurVisible) {
               collaborateurs = allCollaborateurs;
            }
         }
      }
   }

   private List<Collaborateur> filterCollaborateursByService(Service service){
      if(champPreleveurVisible) {
         //récupération des collaborateurs pour ce service : on s'appuie sur tous les collaborateurs possibles (sans la valeur null)
         List<Collaborateur> collaborateursConcernes = allCollaborateurs.subList(1, allCollaborateurs.size());
         return addNullCollaborateurIfNecessary(
            ManagerLocator.getCollaborateurManager().filterCollaborateursByServiceWithOrder(service, collaborateursConcernes));
      }
      
      return new ArrayList<Collaborateur>(); 
   }

   //TG-206 : cette méthode pourrait être définie dans FichePrelevementEdit.java (à la place de celle existante)
   //pour faire profiter les "collections hors Gatsbi" de la valorisation automatique du service si il n'y a en qu'un
   //et celle du collaborateur si il n'y en a également qu'un seul. 
   //Mais avec la volumétrie des collaborations des HCL, il y a un temps de latence important entre la modification de l'établissement
   //et le rafraichissement des listes qui est perturbant => mise en place uniquement pour Gatsbi où les listes sont filtrées
   @Override
   public void onSelect$etabsBoxPrlvt(final Event event) throws Exception{
      if(filterOnPreleveurDefined) {
         populateServicesForSelectedEtablissement();
         
         if(selectedEtablissement == null) {
            services = allServices;
            selectedService = null;
            collaborateurs = allCollaborateurs;
            selectedCollaborateur = null;
         }
         else {
            if(!services.contains(selectedService)){
               selectedService = null;
               populateCollaborateurForSelectedEtablissement();
            }
            
            if(services.size() == 2) {
               selectedService = services.get(1);//le premier est la ligne vide
               //mise à jour des collaborateurs 
               populateCollaborateursForSelectedService();
               if(!collaborateurs.contains(selectedCollaborateur)){
                  selectedCollaborateur = null;
               }
            }
            
            if(collaborateurs.size() == 2) {
               selectedCollaborateur = collaborateurs.get(1);//le premier est la ligne vide
            }      
         }
         
         final ListModel<Service> list = new ListModelList<>(services);
         servicesBoxPrlvt.setModel(list);
         servicesBoxPrlvt.setSelectedIndex(services.indexOf(selectedService));
         getBinder().loadComponent(servicesBoxPrlvt);
         
         collaborateursBoxPrlvt.setModel(new ListModelList<>(collaborateurs));
         collaborateursBoxPrlvt.setSelectedIndex(collaborateurs.indexOf(selectedCollaborateur));
         getBinder().loadComponent(collaborateursBoxPrlvt);
      }
      else {
         super.onSelect$etabsBoxPrlvt(event);
      }
   }

   
   private void refreshCollaborationComponents(){
      if(champServicePreleveurVisible) {
         etabsBoxPrlvt.setModel(new ListModelList<>(etablissements));
         etabsBoxPrlvt.setSelectedIndex(etablissements.indexOf(selectedEtablissement));
         getBinder().loadComponent(etabsBoxPrlvt);
   
         servicesBoxPrlvt.setModel(new ListModelList<>(services));
         servicesBoxPrlvt.setSelectedIndex(services.indexOf(selectedService));
         getBinder().loadComponent(servicesBoxPrlvt);
      }
      
      if(champPreleveurVisible) {
         collaborateursBoxPrlvt.setModel(new ListModelList<>(collaborateurs));
         collaborateursBoxPrlvt.setSelectedIndex(collaborateurs.indexOf(selectedCollaborateur));
         getBinder().loadComponent(collaborateursBoxPrlvt);
      }
   }
}
