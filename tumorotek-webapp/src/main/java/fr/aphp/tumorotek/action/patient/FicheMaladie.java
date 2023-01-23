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
package fr.aphp.tumorotek.action.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeUtils;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.validation.coeur.patient.MaladieValidator;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller de la fiche de consultation/création d'une maladie.
 * Embarquée dans la fiche patient, elle présente un comportement
 * autonome dans la création/modification d'une maladie.<br>
 *
 * Cette classe implémente les méthode statique et dynamiques.
 *
 * Si embarquée dans la fiche prélèvement, l'objet maladie
 * n'est pas enregistré en base tant que le prélèvement
 * n'est pas enregistré. Aucun bouton n'est accessible.
 * Date 03/12/09
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class FicheMaladie extends AbstractFicheCombineController
{

   // private Log log = LogFactory.getLog(FicheMaladie.class);

   private static final long serialVersionUID = 7781723391910786070L;

   //Etats
   protected boolean isEmbeddedWithPatient = false;

   protected FichePatientStatic fichePatient;

   //Panel
   protected Panel container;

   protected Grid formGrid;

   protected Label codeDiagFormLabel;

   protected Label dateDiagFormLabel;

   // Labels
   protected Label libelleLabel;

   protected Label libelleRequired;

   protected Label codeDiagLabel;

   protected Label dateDebutLabel;

   protected Label dateDiagLabel;

   // Editable components
   protected Textbox libelleBox;

   protected Textbox codeDiagBox;

   protected Datebox dateDebutBox;

   protected Datebox dateDiagBox;

   // inca
   private Image pop1;

   private Image pop2;

   protected Button codeAssistantButton;

   // @since 2.3.0-gatsbi 
   // Row devient Div dans fiche Gatsbi
   protected HtmlBasedComponent libelleRow;

   // Objets Principaux
   protected Maladie maladie = new Maladie();

   protected Button addPrelevement;

   protected Toolbar toolbar;

   // @since 2.3.0-gatsbi
   // group deviennent generic HtmlBasedComponent
   // car dans Gatsbi ce sont des groupboxes
   protected HtmlBasedComponent prelevementsMaladieGroup;
   protected HtmlBasedComponent referentsGroup;

   protected Listbox prelevementsMaladieBox;

   protected Listbox prelevementsFromOtherBanksMaladieBox;

   protected boolean canCreatePrelevement;

   protected Button selectAllprelevementsButton;

   private Button historique;

   // true si create/edit mode
   protected boolean isInEdition = false;

   protected List<Prelevement> prelevements = new ArrayList<>();

   protected Prelevement selectedPrelevement;

   protected List<Prelevement> prelevementsFromOtherBanks = new ArrayList<>();

   protected Prelevement selectedPrelevementFromOtherBank;

   protected List<Collaborateur> medecins = new ArrayList<>();

   // defaut contexte TK renderer
   protected PrelevementItemRenderer prelevementRenderer = new PrelevementItemRenderer();

   // other banks -> contexte defaut TK
   protected PrelevementItemRenderer prelevementFromOtherBanksRenderer = new PrelevementItemRenderer();
   
   // @since 2.3.0-gatsbi
   // sera surchargé
   protected MaladieValidator maladieValidator;

   public Panel getContainer(){
      return container;
   }

   public boolean isInEdition(){
      return isInEdition;
   }

   public List<Prelevement> getPrelevements(){
      return this.prelevements;
   }

   public List<Prelevement> getPrelevementsFromOtherBanks(){
      return this.prelevementsFromOtherBanks;
   }

   public void setPrelevementFromOtherBanksRenderer(final PrelevementItemRenderer pobr){
      this.prelevementFromOtherBanksRenderer = pobr;
   }

   public ListitemRenderer<Prelevement> getPrelevementRenderer(){
      return prelevementRenderer;
   }

   public void setPrelevementRenderer(final PrelevementItemRenderer pr){
      this.prelevementRenderer = pr;
   }

   public ListitemRenderer<Prelevement> getPrelevementFromOtherBanksRenderer(){
      return prelevementFromOtherBanksRenderer;
   }

   public Prelevement getSelectedPrelevement(){
      return selectedPrelevement;
   }

   public Prelevement getSelectedPrelevementFromOtherBank(){
      return selectedPrelevementFromOtherBank;
   }

   public void setSelectedPrelevement(final Prelevement sel){
      this.selectedPrelevement = sel;
   }

   public void setSelectedPrelevementFromOtherBank(final Prelevement sel){
      this.selectedPrelevementFromOtherBank = sel;
   }

   public Component getFichePatientComponent(){
      return self.getRoot().getFellow("main").getFellow("winPatient").getFellow("fwinPatientStatic");
   }

   public FichePatientStatic getFichePatient(){
      return fichePatient;
   }

   public void setFichePatient(final FichePatientStatic fP){
      this.fichePatient = fP;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.maladie");

      setObjLabelsComponents(new Component[] {this.libelleLabel, this.codeDiagLabel, this.dateDebutLabel, this.dateDiagLabel,
         this.prelevementsMaladieGroup, this.prelevementsMaladieBox, this.prelevementsFromOtherBanksMaladieBox});
      setObjBoxsComponents(
         new Component[] {this.libelleBox, this.codeDiagBox, this.dateDebutBox, this.dateDiagBox, codeAssistantButton});
      setRequiredMarks(new Component[] {this.libelleRequired});

      getReferents().setFicheParent(this);

      drawActionsForMaladie();

      prelevementsMaladieBox.addEventListener("onClickPrelevementCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onClickPrelevementCode(event);
         }
      });

      prelevementsFromOtherBanksMaladieBox.addEventListener("onClickPrelevementCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onClickPrelevementCode(event);
         }
      });
      
      // @since 2.3.0-gatsbi
      // regular Maladie Validator, sera surchargé par Gatsbi
      setMaladieValidator(ManagerLocator.getMaladieValidator());
   }

   //   /**
   //    * Fixe le contexte de la banque courante, ou si mode toutes collections, le contexte
   //    * commun à toutes les collections.
   //    * Si pas de contexte commun alors contexte 'null' = anapath par défaut
   //    * @since 2.2.1
   //    * @return
   //    */
   //   private Contexte initContexte() {
   //	   // une seule collection en cours
   //	   if(SessionUtils.getSelectedBanques(sessionScope).size() == 1) {
   //		   return SessionUtils.getSelectedBanques(sessionScope).get(0).getContexte();
   //	   } else { // toutes collections
   //		   List<Contexte> conts = SessionUtils.getSelectedBanques(sessionScope).stream().map(b -> b.getContexte())
   //				   .distinct().collect(Collectors.toList());
   //		   if (conts.size() == 1) { // 1 seul contexte pour toutes les banques
   //			   return conts.get(0);
   //		   } else { // contexte TK par défaut > null
   //			   return null;
   //		   }
   //	   }
   //   }

   public void setPatient(final Patient pat){
      this.maladie.setPatient(pat);
   }

   /**
    * N'affiche que la liste de prelevements et aucune informations
    * concernant la maladie sous-jacente à la collection.
    */
   public void setPrelevementsOnly(){
      if (this.formGrid != null) {
         formGrid.detach();
      }
      container.setClosable(false);
      container.setCollapsible(false);
      maladie.setLibelle(null);
      container.setOpen(true);
      container.setSclass(null);
      setGroupPrelevementsOpen(true);
      prelevementsMaladieGroup.setVisible(false);
      toolbar.setVisible(false);

   }

   @Override
   public Maladie getObject(){
      return this.maladie;
   }

   /**
    * Setter appelé par fichePatient lors du dessin des panels.
    * Cette méthode est donc appelée avec des maladies existantes et
    * une nouvelle maladie (empty) potentiellement.
    * Récupère les prélèvements pour la banque courante et les prèlèvements
    * pour une autre banque dans deux listes séparées.
    * @param maladie Maladie backing bean du panel
    */
   @Override
   public void setObject(final TKdataObject mal){
      this.maladie = (Maladie) mal;
      prelevements.clear();
      prelevementsFromOtherBanks.clear();

      // recupere les prelevements si objet non-vide uniquement
      if(!this.maladie.equals(new Maladie())){
         if(SessionUtils.getSelectedBanques(sessionScope).size() == 1){
            this.prelevements = ManagerLocator.getPrelevementManager().findByMaladieAndBanqueManager(this.maladie,
               SessionUtils.getSelectedBanques(sessionScope).get(0));

            // Retrouve les collections de prelevements consultables
            final List<Banque> banks = ManagerLocator.getBanqueManager().findByEntiteConsultByUtilisateurManager(
               SessionUtils.getLoggedUser(sessionScope), ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0),
               SessionUtils.getPlateforme(sessionScope));
            banks.remove(SessionUtils.getSelectedBanques(sessionScope).get(0));

            // configure le renderer pour inactiver les liens des
            // prélèvements non consultables
            prelevementFromOtherBanksRenderer.setFromOtherConsultBanks(banks);

            // etend la liste de banks à celle declarant
            // autoriseCrossPatient
            final Set<Banque> crossBanks =
               new HashSet<>(ManagerLocator.getBanqueManager().findByAutoriseCrossPatientManager(true));
            crossBanks.remove(SessionUtils.getSelectedBanques(sessionScope).get(0));
            crossBanks.addAll(banks);

            final Iterator<Banque> it = crossBanks.iterator();
            while(it.hasNext()){
               this.prelevementsFromOtherBanks
                  .addAll(ManagerLocator.getPrelevementManager().findByMaladieAndBanqueManager(this.maladie, it.next()));
            }

         }else{
            // recupere tous les prelevements pour la liste de banques
            final Iterator<Banque> it = SessionUtils.getSelectedBanques(sessionScope).iterator();
            while(it.hasNext()){
               this.prelevementsFromOtherBanks
                  .addAll(ManagerLocator.getPrelevementManager().findByMaladieAndBanqueManager(this.maladie, it.next()));
            }
            // tous les prélèvements sont consultables
            prelevementFromOtherBanksRenderer.setFromOtherConsultBanks(SessionUtils.getSelectedBanques(sessionScope));
         }
         this.addPrelevement.setVisible(true);
         this.historique.setVisible(true);

         // medecins referents
         this.medecins = new ArrayList<>(ManagerLocator.getMaladieManager().getCollaborateursManager(this.maladie));
         getReferents().setMedecins(this.medecins);
         setGroupMedecinsOpen(false);

      }else{
         // maladie en creation
         // applique le libelle de maladie defini par défaut
         if(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladie() != null){
            this.maladie.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladie());
            this.maladie.setCode(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladieCode());
         }else{
            setLibelleIndeterminee();
         }
      }

      setListBoxesMold();

      this.libelleRow.setVisible(false);

      // clone er reload
      super.setObject(maladie);
   }
   
   // @since 2.3.0-gatsbi
   // sera surchargée
   protected void setLibelleIndeterminee() {
      this.maladie.setLibelle(Labels.getLabel("maladie.indeterminee"));
   }

   /**
    * Passe les listes de prélèvements en mold paging si ces listes
    * contiennent plus de 5 prelevements.
    */
   public void setListBoxesMold(){
      /*if (prelevements.size() > 5) {
      	prelevementsMaladieBox.setMold("paging");
      	prelevementsMaladieBox.setPageSize(10);
      }
      if (prelevementsFromOtherBanks.size() > 5) {
      	prelevementsFromOtherBanksMaladieBox.setMold("paging");
      	prelevementsFromOtherBanksMaladieBox.setPageSize(10);
      }*/
   }

   @Override
   public void setNewObject(){
      setObject(new Maladie());
      this.prelevements.clear();
      this.prelevementsFromOtherBanks.clear();
      this.medecins.clear();
      super.setNewObject();
   }

   @Override
   public TKdataObject getParentObject(){
      return this.maladie.getPatient();
   }

   @Override
   public void setParentObject(final TKdataObject obj){
      this.maladie.setPatient((Patient) obj);
   }

   @Override
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getMaladieManager().isUsedObjectManager(getObject());
      setCascadable(false);
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isUsed){
         setDeleteMessage(Labels.getLabel("patient.deletion.isUsed"));
      }
      setDeletable(!isUsed);
      setFantomable(!isUsed);
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getMaladieManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope));
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         // suppr la reference vers ce composant
         getFichePatient().getMaladiePanels().remove(this);

         // force le rafraichissement du bloc 'malaDiv'
         // en envoyant la maladie la supprimmer de la liste.
         Events.postEvent(new Event("onMaladieDelete", getFichePatientComponent(), this.maladie));

         removeObject((String) event.getData());

         self.getParent().detach();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.maladie.clone());
   }

   @Override
   public void onClick$editC(){
      switchToEditMode();

      // gèle la toolbar de la fiche patient pendant l'edition
      Events.postEvent(new Event("onMaladieEdit", getFichePatientComponent(), null));
   }

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$deleteC(){
      if(!editC.isDisabled()){
         super.onClick$deleteC();
      }
   }

   @Override
   public void onLaterCreate(){
      super.onLaterCreate();
      // force le rafraichissement de la toolbar de la fiche patient
      // en envoyant la maladie pour l'ajouter à la liste.
      sendMaladieDoneEventToFichePatient(this.maladie);
   }

   @Override
   public void onLaterValidate(){
      super.onLaterValidate();
      // dégèle la toolbar de la fiche patient
      sendMaladieDoneEventToFichePatient(null);
   }

   @Override
   public void onClick$revertC(){
      try{
         switchToStaticMode();
         revertObject();
      }catch(final RuntimeException re){
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
      sendMaladieDoneEventToFichePatient(null);
   }

   @Override
   public void onLaterRevert(){
      super.onLaterRevert();
      // reactive la toolbar de la fiche patient
      sendMaladieDoneEventToFichePatient(null);
   }

   @Override
   public void onClick$cancelC(){
      getFichePatient().getMaladiePanels().remove(this);

      // reactive la toolbar de la fiche patient
      sendMaladieDoneEventToFichePatient(null);

      self.getParent().detach();
   }

   /**
    * Envoie un evenement à la fiche patient lui spécifiant que la manipulation
    * sur la maladie est terminée. La fiche patient peut alors reprendre le
    * contrôle en activant les boutons de sa toolbar.
    * @param maladie Maladie manipulée, peut être null (revert/cancel)
    */
   private void sendMaladieDoneEventToFichePatient(final Maladie mal){
      if(self.getRoot().getFellowIfAny("main") != null){
         // dégèle la toolbar de la fiche patient
         Events.postEvent(new Event("onMaladieDone", getFichePatientComponent(), mal));
      }
   }

   @Override
   public void createNewObject(){

      prepareDataBeforeSave(false);
      setFieldsToUpperCase();
      ManagerLocator.getMaladieManager().createOrUpdateObjectManager(this.maladie, null, medecins,
         SessionUtils.getLoggedUser(sessionScope), "creation");
      setObject(maladie);

      // update patient
      getObjectTabController().getListe().updateObjectGridList(this.maladie.getPatient());
   }

   @Override
   public void updateObject(){

      prepareDataBeforeSave(false);
      setFieldsToUpperCase();
      ManagerLocator.getMaladieManager().createOrUpdateObjectManager(this.maladie, null, medecins,
         SessionUtils.getLoggedUser(sessionScope), "modification");

      // update de la liste des prelevements de la maladie
      final List<TKdataObject> children = new ArrayList<>();
      final Iterator<Banque> it = SessionUtils.getSelectedBanques(sessionScope).iterator();
      while(it.hasNext()){
         children.addAll(ManagerLocator.getPrelevementManager().findByMaladieAndBanqueManager(this.maladie, it.next()));
      }
      getFichePatient().getObjectTabController().updateReferencedObjects(children);
      children.clear();

      // update patient
      getObjectTabController().getListe().updateObjectGridList(this.maladie.getPatient());
   }

   /**
    * Prepare les valeurs des attributs qui seront sauvées avec le
    * bean patient.
    * Recupere la liste de referents depuis le composant embarqué.
    * @param boolean specifiant si la liste de medecins doit être passée
    * par le setter (utile si fiche embarquée dans prélèvement)
    */
   public void prepareDataBeforeSave(final boolean setMedecins){
      setEmptyToNulls();
      this.medecins = getReferents().getMedecins();
      if(setMedecins && medecins.size() > 0){
         this.maladie.setCollaborateurs(new HashSet<>(this.medecins));
      }
   }

   @Override
   public void setEmptyToNulls(){
      if(this.maladie.getLibelle().equals("")){
         this.maladie.setLibelle(null);
      }
      if(this.maladie.getCode() != null && this.maladie.getCode().equals("")){
         this.maladie.setCode(null);
      }
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.maladie.equals(new Maladie()));
      libelleRow.setVisible(false);
      addPrelevement.setVisible(!this.maladie.equals(new Maladie()));
      historique.setVisible(!this.maladie.equals(new Maladie()));
      getReferents().switchToStaticMode();
      getBinder().loadComponent(self);

      // affiche les popups classification
      if(sessionScope.get("catalogues") != null){
         if(pop1 != null){
            pop1.setVisible(((Map<String, Boolean>) sessionScope.get("catalogues")).containsKey("INCa"));
            pop2.setVisible(((Map<String, Boolean>) sessionScope.get("catalogues")).containsKey("INCa"));
         }
         setRegularLabelStyle();
      }
      isInEdition = false;
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      this.libelleRow.setVisible(true);
      addPrelevement.setVisible(false);
      historique.setVisible(false);
      getReferents().switchToEditMode();

      setIncaLabelStyle();

      isInEdition = true;
   }

   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();
      this.libelleRow.setVisible(true);
      getReferents().switchToCreateMode();

      setIncaLabelStyle();

      isInEdition = true;
   }
   
   // @since 2.3.0-gatsbi, sera surchargée par gatsbi
   // car devenu inutile
   protected void setIncaLabelStyle() {
      codeDiagFormLabel.setSclass("incaLabel");
      dateDiagFormLabel.setSclass("incaLabel");
   }
   
   // @since 2.3.0-gatsbi, sera surchargée par gatsbi
   // car devenu inutile
   protected void setRegularLabelStyle() {
      codeDiagFormLabel.setSclass("formLabel");
      dateDiagFormLabel.setSclass("formLabel");
   }

   @Override
   public void setFocusOnElement(){
      libelleBox.setFocus(true);
   }

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/

   /**
    * Ecris le libelle de la fiche maladie sous
    * la forme libelle + count prelevements.
    * @return String libelle fiche maladie
    */
   public String getMaladieLibelle(){
      
      String libelle = null;
      
      if(!this.maladie.getSystemeDefaut()){
         libelle =  this.maladie.getLibelle() + " (" + String.valueOf(getTotPrelevementsCount()) + ")";
         
         // @since 2.3.0-gatsbi
         // en toutes collections ajoute collection au libelle visite
         if (Sessions.getCurrent().getAttribute("ToutesCollections") != null 
               && this.maladie.getBanque() != null) {
            libelle = libelle.concat(" - ").concat(this.maladie.getBanque().getNom());
         }        
      }
     
      return libelle;
   }

   public String getPrelevementsGroupLabel(){
      return Labels.getLabel("ficheMaladie.prelevements") + "(" + String.valueOf(getTotPrelevementsCount()) + ")";
   }

   public int getTotPrelevementsCount(){
      return this.prelevements.size() + this.prelevementsFromOtherBanks.size();
   }

   public String getDateDebutFormatted(){
      return ObjectTypesFormatters.dateRenderer2(this.maladie.getDateDebut());
   }

   public String getDateDiagFormatted(){
      return ObjectTypesFormatters.dateRenderer2(this.maladie.getDateDiagnostic());
   }

   public String getReferentsGroupHeader(){
      return Labels.getLabel("patient.medecins") + "(" + getReferents().getMedecins().size() + ")";
   }

   public boolean getPrelevementsListSizeSupOne(){
      if(getDroitsConsultation().containsKey("Prelevement") && getDroitsConsultation().get("Prelevement")){
         return this.prelevements.size() > 1;
      }
      return false;
   }

   public boolean getHasPrelevements(){
      return this.prelevements.size() > 0;
   }

   public boolean getHasPrelevementsFromOtherBanks(){
      return this.prelevementsFromOtherBanks.size() > 0;
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * libelleBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$libelleBox(){
      libelleBox.setValue(libelleBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codeDiagBox. Cette valeur sera mise en majuscules et une recherche
    * automatique du libelle correspondant est lancée.
    */
   public void onBlur$codeDiagBox(){

      if(!codeDiagBox.getValue().equals("")){
         Clients.showBusy(libelleRow, Labels.getLabel("libelle.recherche.encours"));
         Events.echoEvent("onLaterFindLibelle", self, null);
      }
      codeDiagBox.setValue(codeDiagBox.getValue().toUpperCase().trim());
   }

   public void onLaterFindLibelle(){

      final Set<CodeCommon> codes = new HashSet<>();
      CodeUtils.findCodesInAllTables(codeDiagBox.getValue(), true, true, codes, true,
         SessionUtils.getSelectedBanques(sessionScope));

      if(!codes.isEmpty()){
         libelleBox.setValue(codes.iterator().next().getLibelle());
      }

      Clients.clearBusy(libelleRow);
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.maladie.getLibelle() != null){
         this.maladie.setLibelle(this.maladie.getLibelle().toUpperCase().trim());
      }

      if(this.maladie.getCode() != null){
         this.maladie.setCode(this.maladie.getCode().toUpperCase().trim());
      }
   }

   /*************************************************************************/
   /****************************** VALIDATION *******************************/
   /*************************************************************************/

   /**
    * Applique les validations assurant la cohérence de la date.
    * @param comp enregistrant date
    * @param value date
    */
   private void validateCoherenceDate(final Component comp, final Object value){
      final Date dateValue = (Date) value;
      Errors errs = null;
      String field = "";

      if(dateValue == null || dateValue.equals("")){
         // la contrainte est retiree
         //((Datebox) comp).setConstraint("");
         ((Datebox) comp).clearErrorMessage(true);
         ((Datebox) comp).setValue(null);
         if(comp.getId().equals("dateDiagBox")){
            this.maladie.setDateDiagnostic(null);
         }else{
            this.maladie.setDateDebut(null);
         }
         // on remet la contrainte
         //((Datebox) comp).setConstraint(getCCoherenceDate());
      }else{

         // save les bindings de la fiche patient embedded avant validation
         if(this.isEmbeddedWithPatient){
            final Component embeddedPatient = self.getParent().getParent().getFellow("fichePatientDiv");

            if(embeddedPatient.getFellowIfAny("fwinPatientEdit") != null){
               ((FichePatientEdit) embeddedPatient.getFellow("fwinPatientEdit").getAttributeOrFellow("fwinPatientEdit$composer",
                  true)).getBinder().saveComponent(embeddedPatient.getFellow("fwinPatientEdit"));
            }
         }

         // date diagnostic
         if(comp.getId().equals("dateDiagBox")){
            field = "dateDiagnostic";
            this.maladie.setDateDiagnostic(dateValue);
            errs = getMaladieValidator().checkDateDiagCoherence(this.maladie);
         }

         // date début
         if(comp.getId().equals("dateDebutBox")){
            field = "dateDebut";
            this.maladie.setDateDebut(dateValue);
            errs = getMaladieValidator().checkDateDebutCoherence(this.maladie);
         }

         if(errs != null && errs.hasErrors()){
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   /**
    * Applique la validation sur la date et la date dépendante.
    */
   public void onBlur$dateDebutBox(){
      //		boolean badDateFormat = false;
      //		if (dateDebutBox.getErrorMessage() != null
      //				&& dateDebutBox.getErrorMessage().contains(
      //						dateDebutBox.getFormat())) {
      //			badDateFormat = true;
      //		}
      //		if (!badDateFormat) {
      Clients.clearWrongValue(dateDebutBox);
      validateCoherenceDate(dateDebutBox, dateDebutBox.getValue());
      Clients.clearWrongValue(dateDiagBox);
      validateCoherenceDate(dateDiagBox, dateDiagBox.getValue());
      //		}
   }

   /**
    * Applique la validation sur la date et la date dépendante.
    */
   public void onBlur$dateDiagBox(){
      //		boolean badDateFormat = false;
      //		if (dateDiagBox.getErrorMessage() != null
      //				&& dateDiagBox.getErrorMessage().contains(
      //						dateDiagBox.getFormat())) {
      //			badDateFormat = true;
      //		}
      //		if (!badDateFormat) {
      Clients.clearWrongValue(dateDiagBox);
      validateCoherenceDate(dateDiagBox, dateDiagBox.getValue());
      Clients.clearWrongValue(dateDebutBox);
      validateCoherenceDate(dateDebutBox, dateDebutBox.getValue());
      //		}
   }

   /**
    * Lance la validation de toutes les dates de maladie car
    * les dates de references du Patient ont pu changer a posteriori.
    */
   public void validateAllDateComps(){
      Clients.clearWrongValue(dateDebutBox);
      validateCoherenceDate(dateDebutBox, dateDebutBox.getValue());
      Clients.clearWrongValue(dateDiagBox);
      validateCoherenceDate(dateDiagBox, dateDiagBox.getValue());
   }

   /*************************************************************************/
   /************************** LINKS ****************************************/
   /*************************************************************************/

   /**
    * Affiche la fiche d'un prélèvement.
    * @param Event e indique le forward venant du selectAll si non null
    */
   private void onClickPrelevementCode(final Event e){

      final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

      if(e != null){ // un seul element a afficher
         final Prelevement prel = (Prelevement) e.getData();
         // change la banque au besoin
         if(!SessionUtils.getSelectedBanques(sessionScope).contains(prel.getBanque())){

            Clients.showBusy(Labels.getLabel("fichePrelevement.switchBanque.encours"));

            Events.echoEvent("onLaterSwitchBanque", self, prel);
         }else{
            tabController.switchToFicheStaticMode(prel);
         }
      }else{ // affiche la liste d'éléments
         tabController.getListe().setCurrentObject(null);
         tabController.clearStaticFiche();
         tabController.getListe().updateListContent(this.prelevements);
         tabController.switchToOnlyListeMode();
      }
      tabController.getListe().updateListResultsLabel(null);
   }

   public void onLaterSwitchBanque(final Event evt){
      getMainWindow().updateSelectedBanque(((Prelevement) evt.getData()).getBanque());

      Events.echoEvent("onSwitchBanqueFromMaladiePrelClick", getMainWindow().getSelfComponent(), evt.getData());
   }

   /**
    * Passe à la fiche Prelevement en mode creation avec la reference
    * patient et maladie pre-remplie.
    * Indique a la fiche que l'on doit revenir sur la fiche Patient après
    * enregistrement ou annulation.
    */
   public void onClick$addPrelevement(){

      final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

      // si on arrive à récupérer le panel prelevement et son controller
      if(tabController != null){
         /*tabController.getFiche()
         					.switchToCreateMode(this.maladie);*/
         tabController.setFromFichePatient(true);
         tabController.setFromFicheMaladie(true);
         tabController.switchToCreateMode(this.maladie);
      }
   }

   /**
    * Forward Event.
    */
   public void onSelectAllPrelevements(){
      onClickPrelevementCode(null);
   }

   /**
    * Ouvre le panel et le groupe de prélèvements.
    */
   public void openAll(){
      container.setOpen(true);
      setGroupPrelevementsOpen(true);
   }

   public void reloadListePrelevements(){
      getBinder().loadComponent(prelevementsMaladieBox);
      getBinder().loadComponent(prelevementsFromOtherBanksMaladieBox);
   }

   /**
    * Recupere le composant MedecinsReferents.
    * @return composant MedecinReferents
    */
   public MedecinReferents getReferents(){
      return (MedecinReferents) self.getFellow("maladieReferentsDiv").getFellow("maladieReferents").getFellow("winReferents")
         .getAttributeOrFellow("winReferents$composer", false);
   }

   /**
    * Ouvre la modale contenant l'assistant deployant les
    * codifications pré-filtrées pour les codes diagnostic.
    */
   public void onClick$codeAssistantButton(){
      openCodesModal(page, Path.getPath(self), false, false, true, false);
   }

   /**
    * Recupere le code depuis l'assistant afin de l'assigner
    * au champ code et libelle.
    * @param event
    */
   public void onGetCodeFromAssist(final Event event){
      if(event.getData() != null){
         this.maladie.setCode(((CodeCommon) event.getData()).getCode());
         this.maladie.setLibelle(((CodeCommon) event.getData()).getLibelle());
      }
   }

   /**
    * la fiche est embarquée donc en mode création uniquement sans affichage
    * des boutons.
    * @param withPatient indique si la fiche est embarquée avec la fiche
    * patient.
    */
   public void setEmbedded(final boolean withPatient){
      // mode embarqué
      switchToCreateMode();
      this.container.setOpen(true);
      this.container.setTitle(null);
      this.container.setCollapsible(false);
      this.container.setClosable(false);
      this.createC.setVisible(false);
      this.cancelC.setVisible(false);
      this.addPrelevement.setVisible(false);
      this.historique.setVisible(false);
      this.selectAllprelevementsButton.setVisible(false);
      this.container.setStyle("border: none");
      this.container.getPanelchildren().setStyle("border-top-style: none");
      if (this.formGrid != null) {
         this.formGrid.setStyle("border-top-style: none");
      }
      this.isEmbeddedWithPatient = withPatient;
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/


   public ConstWord getCodeNullConstraint(){
      return MaladieConstraints.getCodeNullConstraint();
   }

   public ConstWord getLibelleConstraint(){
      return MaladieConstraints.getLibelleConstraint();
   }

   public Maladie getMaladie(){
      return maladie;
   }

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   public boolean isCanCreatePrelevement(){
      return canCreatePrelevement;
   }

   public void setCanCreatePrelevement(final boolean ccp){
      this.canCreatePrelevement = ccp;
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForMaladie(){
      drawActionsButtons("Patient");
      canCreatePrelevement = drawActionOnOneButton("Prelevement", "Creation");

      // si pas le droit d'accès aux prelevements, on cache le lien
      if(!getDroitsConsultation().get("Prelevement")){
         prelevementRenderer.setAccessible(false);
      }else{
         prelevementRenderer.setAccessible(true);
      }

      // empeche creation prelevements si toutes collections
      addPrelevement.setDisabled(!canCreatePrelevement || !sessionScope.containsKey("Banque"));
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("maaldie.suppression.encours");
   }
   
   // @since 2.3.0-gatsbi  
   protected void setGroupMedecinsOpen(final boolean b){
      if(referentsGroup instanceof Group){
         ((Group) referentsGroup).setOpen(b);
      }else{
         ((Groupbox) referentsGroup).setOpen(b);
      }      
   }
   
   // @since 2.3.0-gatsbi  
   protected void setGroupPrelevementsOpen(final boolean b){
      if(prelevementsMaladieGroup instanceof Group){
         ((Group) prelevementsMaladieGroup).setOpen(b);
      }else{
         ((Groupbox) prelevementsMaladieGroup).setOpen(b);
      }      
   }

   // @since 2.3.0-gatsbi  
   public MaladieValidator getMaladieValidator(){
      return maladieValidator;
   }

   // @since 2.3.0-gatsbi  
   public void setMaladieValidator(MaladieValidator maladieValidator){
      this.maladieValidator = maladieValidator;
   }
   
}
