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
package fr.aphp.tumorotek.action.patient;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 */
public class FichePatientStatic extends AbstractFicheStaticController
{

   private static final long serialVersionUID = 7781723391910786070L;

   // button
   protected Button addMaladie;

   protected Button addPrelevement;

   // maladies
   protected Div malaDiv;

   private String maladieGroupHeaderTemplate;

   private String maladieGroupHeader;

   private int lastPanelId;

   // @since gatsbi 
   // gatsbi overrides group -> groupboxes
   // referents
   protected HtmlBasedComponent groupMaladies;
   protected HtmlBasedComponent groupMedecins;

   // Objets Principaux
   protected Patient patient;

   // dateEtatDeces
   private Label dateEtatDecesField;

   protected Listbox prelevementsFromOtherMaladiesBox;

   // Associations
   protected List<Maladie> maladies = new ArrayList<>();

   private List<Collaborateur> medecins = new ArrayList<>();

   // Labels à cacher en cas de compte anonyme
   protected Label nipLabel;

   protected Label nomLabel;

   protected Label nomNaisLabel;

   protected Label prenomLabel;

   private final List<Prelevement> prelevementsFromOtherMaladies = new ArrayList<>();

   // maladie systems -> contexte TK anapath par défaut
   private final PrelevementItemRenderer prelevementFromOtherMaladiesRenderer = new PrelevementItemRenderer();

   private final List<FicheMaladie> maladiePanels = new ArrayList<>();

   public Div getMalaDiv(){
      return malaDiv;
   }

   public List<Maladie> getMaladies(){
      return this.maladies;
   }

   public List<Collaborateur> getMedecins(){
      return this.medecins;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.patient");

      // cree maladie groupHeaderTemplate avec slots 1 et 2
      this.maladieGroupHeaderTemplate = "";
      if(SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope))){
         this.maladieGroupHeaderTemplate = Labels.getLabel("patient.maladies") + "({1}) - ";
      }
      this.maladieGroupHeaderTemplate = this.maladieGroupHeaderTemplate + Labels.getLabel("ficheMaladie.prelevements") + "({2})";

      getReferents().setFicheParent(this);
      getReferents().setMedecins(this.medecins);

      addMaladie.setVisible(SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope)));

      addPrelevement.setVisible(!addMaladie.isVisible());

      prelevementsFromOtherMaladiesBox.addEventListener("onClickPrelevementCode", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onClickPrelevementCode(event);
         }
      });
   }

   public List<FicheMaladie> getMaladiePanels(){
      return maladiePanels;
   }

   @Override
   public PatientController getObjectTabController(){
      return (PatientController) super.getObjectTabController();
   }

   @Override
   public void setObject(final TKdataObject pat){
      this.patient = (Patient) pat;

      this.maladies.clear();
      this.medecins.clear();
      this.prelevementsFromOtherMaladies.clear();

      accordDateToEtat();

      if(patient.getPatientId() != null){
         List<Maladie> otherMaladies = new ArrayList<>();
         if(sessionScope.containsKey("Banque")){
            // banque définit maladies -> une liste de maladies,
            // les autres sont maladies sytem
            if(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefMaladies()){
               this.maladies = new ArrayList<>(ManagerLocator.getMaladieManager().findByPatientNoSystemManager(patient));
               otherMaladies = ManagerLocator.getMaladieManager().findByPatientManager(patient);
               otherMaladies.removeAll(maladies);
            }else{
               // banque définit pas maladies -> une maladie system,
               // les autres sont maladies venant d'autres banques
               this.maladies = ManagerLocator.getMaladieManager().findByPatientManager(patient);
               otherMaladies = new ArrayList<>(ManagerLocator.getMaladieManager().findByPatientNoSystemManager(patient));
               maladies.removeAll(otherMaladies);
            }
         }else{
            // si au moins une banque définit une maladie
            if(SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope))){
               this.maladies = new ArrayList<>(ManagerLocator.getMaladieManager().findByPatientNoSystemManager(patient));
               otherMaladies = ManagerLocator.getMaladieManager().findByPatientManager(patient);
               otherMaladies.removeAll(maladies);
            }else{
               // n'affiche que des prelevements car maladies systems only
               this.maladies = ManagerLocator.getMaladieManager().findByPatientManager(patient);
            }
         }

         // prepare la liste de prélèvements
         final Iterator<Maladie> mals = otherMaladies.iterator();
         while(mals.hasNext()){
            this.prelevementsFromOtherMaladies.addAll(ManagerLocator.getMaladieManager().getPrelevementsManager(mals.next()));
         }

         // Retrouve les collections de prelevements consultables
         final List<Banque> banks = ManagerLocator.getBanqueManager().findByEntiteConsultByUtilisateurManager(
            SessionUtils.getLoggedUser(sessionScope), ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0),
            SessionUtils.getPlateforme(sessionScope));

         // configure le renderer pour inactiver les liens des
         // prélèvements non consultables
         prelevementFromOtherMaladiesRenderer.setFromOtherConsultBanks(banks);

         // medecins referents
         this.medecins = new ArrayList<>(ManagerLocator.getPatientManager().getMedecinsManager(patient));
      }

      // populate maladieGroupHeader
      populateMaladieGroupHeader();

      redrawMaladies();

      getReferents().setMedecins(this.medecins);
      setGroupMedecinsOpen(false);

      // annotations
      super.setObject(patient);

   }

   @Override
   public void setNewObject(){
      setObject(new Patient());

      // disable edit et delete
      super.setNewObject();
      // disable autres buttons
      addMaladie.setDisabled(true);
      addPrelevement.setDisabled(true);
   }

   @Override
   public Patient getObject(){
      return this.patient;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public void prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getPatientManager().isUsedObjectManager(getObject());
      setCascadable(false);
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isUsed){
         setDeleteMessage(Labels.getLabel("patient.deletion.isUsed"));
      }
      setDeletable(!isUsed);
      setFantomable(!isUsed);
   }

   @Override
   public void removeObject(final String comments){
      final List<File> filesToDelete = new ArrayList<>();
      ManagerLocator.getPatientManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope),
         filesToDelete);
      for(final File f : filesToDelete){
         f.delete();
      }

   }

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/

   /**
    * Affiche le label 'Date deces' ou 'Date Etat' en fonction
    * de l'etat du patient (car un seul champ de formulaire est
    * gère ces deux valeurs en base).
    */
   private void accordDateToEtat(){
      if (dateEtatDecesField != null) {
         if(!"D".equals(this.patient.getPatientEtat())){
            this.dateEtatDecesField.setValue(Labels.getLabel("Champ.Patient.DateEtat"));
         }else{
            this.dateEtatDecesField.setValue(Labels.getLabel("Champ.Patient.DateDeces"));
         }
      }
   }

   public String getDateNaisFormatted(){
      if(isAnonyme()){
         return getAnonymeString();
      }
      return ObjectTypesFormatters.dateRenderer2(this.patient.getDateNaissance());
   }

   public String getSexeFormatted(){
      return PatientUtils.setSexeFromDBValue(this.patient);
   }

   public String getEtatFormatted(){
      return PatientUtils.setEtatFromDBValue(this.patient);
   }

   public String getDateEtatDecesFormatted(){
      return PatientUtils.getDateDecesOrEtat(patient);
   }

   public String getMaladiesGroupHeader(){
      return this.maladieGroupHeader;
   }

   public String getReferentsGroupHeader(){
      return Labels.getLabel("patient.medecins") + "(" + String.valueOf(getReferents().getMedecins().size()) + ")";
   }

   /*************************************************************************/
   /************************** MALADIE ***************************************/
   /*************************************************************************/

   /**
    * Ajoute un nouveau panel maladie en create mode;
    * Spécifie l'ordre du panel en ajoutant à la liste de maladies.
    */
   public void onClick$addMaladie(){
      closeMaladiePanels();
      this.lastPanelId = this.lastPanelId + 1;
      final HtmlMacroComponent mPanel = drawAMaladiePanel(null, this.lastPanelId, false);
      // passe la fiche en mode create
      ((FicheMaladie) mPanel.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).switchToCreateMode();
      // passe le patient
      ((FicheMaladie) mPanel.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
         .setPatient(this.patient);
      // ouvre le panel
      ((FicheMaladie) mPanel.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).getContainer()
         .setOpen(true);
      // gele la toolbar en attendant que la maladie soit crée ou avortée.
      disableToolBar(true);
   }

   /**
    * Passe à la fiche Prelevement en mode creation avec la reference
    * patient pre-remplie.
    * Indique a la fiche que l'on doit revenir sur la fiche Patient après
    * enregistrement ou annulation.
    */
   public void onClick$addPrelevement(){

      final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

      // si on arrive à récupérer le panel prelevement et son controller
      if(tabController != null){
         // maladies doit contenir au moins une maladie
         if(this.maladies.size() == 0){ //cree la maladie sous-jacente
            final Maladie maladieSJ = new Maladie();
            if(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefMaladies()){
               if(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladie() != null){
                  maladieSJ.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladie());
                  maladieSJ.setCode(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefautMaladieCode());
               }else{
                  maladieSJ.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getNom() + "-defaut");
                  maladieSJ.setSystemeDefaut(true);
               }
            }else{ // new Maladie system defaut à creer
               maladieSJ.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getNom() + "-defaut");
               maladieSJ.setSystemeDefaut(true);
            }

            maladieSJ.setPatient(this.patient);
            this.maladies.add(maladieSJ);
         }
         tabController.switchToCreateMode(this.maladies.get(0));
         tabController.setFromFichePatient(true);

         // on cache la liste
         tabController.getListeRegion().setOpen(false);
      }
   }

   /**** Evenements provenant des fiches maladies. *****/

   public void onMaladieDone(final Event e){
      if(e.getData() != null){
         this.maladies.add((Maladie) e.getData());

         populateMaladieGroupHeader();

         if(getObjectTabController().getListe() != null){
            // ajout de l'échantillon à la liste
            getObjectTabController().getListe().updateObjectGridList(this.patient);
         }
      }
      // dégele la toolbar
      disableToolBar(false);
   }

   public void onMaladieDelete(final Event e){
      this.maladies.remove(e.getData());

      populateMaladieGroupHeader();

      if(getObjectTabController().getListe() != null){
         // ajout de l'échantillon à la liste
         getObjectTabController().getListe().updateObjectGridList(this.patient);
      }
      disableToolBar(false);
   }

   public void onMaladieEdit(){
      //gele la toolbar
      disableToolBar(true);
   }

   /**
    * Redessine les panels maladies au sein du bloc 'malaDiv' a partir
    * de la liste de maladies associée au patient.
    * Au besoin, appelle le loading du binder declaré pour 'malaDiv'
    * (le binder de la page ne fonctionne pas dans un macro component?)
    * pour réaliser les bindings.
    */
   private void redrawMaladies(){

      this.maladiePanels.clear();
      Components.removeAllChildren(malaDiv);

      if(maladies.size() > 0){
         // dessine les differents panel maladies
         HtmlMacroComponent ua = null;
         int i;
         for(i = 0; i < maladies.size(); i++){
            if(!maladies.get(i).getSystemeDefaut()){
               ua = drawAMaladiePanel(maladies.get(i), i, false);
            }else{
               ua = drawAMaladiePanel(maladies.get(i), i, true);
            }
         }

         /** Mis en commentaire car l'ouverture de la maladie
          * provoque un bug d'affichage sur les petits écrans :
          * les onglets disparaissent. */
         if(null != ua && i == 1){ // 1 seule maladie
            ((FicheMaladie) ua.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).openAll();
         }
         this.lastPanelId = i;

         getBinder().loadComponent(malaDiv);
      }
   }

   /**
    * Methode dessinant un panel Maladie dans le bloc id='malaDiv'
    * en lui attribuant un backing bean Maladie. Si ce dernier est
    * un objet vide, le panel est en create mode. L'ordre d'apparition des
    * maladies est passé en paramètre pour spécifier des ids distincts dans la
    * page.
    * Cette methode implique le loading du binder pour effectuer les bindings
    * au niveau du macrocomponent.
    *
    * @param maladie backing bean
    * @param int     ordre d'apparition du panel maladie
    * @param si      seuls les prelevements doivent être affichés
    * @return macro component ficheMaladie
    */
   private HtmlMacroComponent drawAMaladiePanel(final Maladie maladie, final int order, final boolean prelevementsOnly){
      HtmlMacroComponent ua;

      // Injection des contextes
      String compDef = "maladiePanel";
      
      // @since gatsbi
      if (SessionUtils.getCurrentGatsbiContexteForEntiteId(7) != null) {
         compDef = "maladieGatsbiPanel";
      } else if(SessionUtils.getCurrentContexte() == EContexte.SEROLOGIE){
         compDef = "maladieSeroPanel";
      }

      ua = (HtmlMacroComponent) page.getComponentDefinition(compDef, false).newInstance(page, null);
      ua.setParent(malaDiv);
      ua.setId("ficheMaladie" + String.valueOf(order));
      ua.setWidth("100%");
      ua.applyProperties();
      ua.afterCompose();
      if(maladie != null){
         ((FicheMaladie) ua.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).setObject(maladie);
      }
      ((FicheMaladie) ua.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).setFichePatient(this);
      ((FicheMaladie) ua.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
         .setObjectTabController(getObjectTabController());

      if(prelevementsOnly){
         ((FicheMaladie) ua.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)).setPrelevementsOnly();
      }

      this.maladiePanels.add(((FicheMaladie) ua.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true)));

      return ua;
   }

   /**
    * Ferme tous les panels maladie.
    */
   private void closeMaladiePanels(){
      final Iterator<Component> it = self.getFellow("malaDiv").getFellows().iterator();
      while(it.hasNext()){
         final Component comp = it.next();
         if(comp.getId().matches("ficheMaladie\\d+")){
            ((Panel) comp.getFellow("fwinMaladie").getFellow("container")).setOpen(false);
         }
      }
   }

   /**
    * Gèle ou dégèle les boutons de la toolbar.
    * Methode utilisée pour bloquer toute action sur le patient lors de la
    * création/édition d'une maladie.
    *
    * @param b
    */
   @Override
   public void disableToolBar(boolean b){
      if(isAnyMaladiePanelInEdition()){
         b = true;
      }
      super.disableToolBar(b);
      addMaladie.setDisabled(b || !isCanCreatePrel());
      addPrelevement.setDisabled(b || !isCanCreatePrel());
   }

   /**
    * Indique si un des panels maladie est en cours d'edition.
    *
    * @return true si oui.
    */
   private boolean isAnyMaladiePanelInEdition(){
      for(int i = 0; i < maladiePanels.size(); i++){
         if(maladiePanels.get(i).isInEdition()){
            return true;
         }
      }
      return false;
   }

   /**
    * Ouvre le panel maladie spécifié par la maladie passée en paramètre.
    *
    * @param maladie dont on veut ouvrir le panel
    */
   public void openMaladiePanel(final Maladie mal){
      final int pos = this.maladies.indexOf(mal);
      if(pos > -1){
         maladiePanels.get(pos).openAll();
      }
   }

   /**
    * Recupere le composant MedecinsReferents.
    *
    * @return composant MedecinReferents
    */
   public MedecinReferents getReferents(){
      return (MedecinReferents) self.getFellow("referentsDiv").getFellow("referents").getFellow("winReferents")
         .getAttributeOrFellow("winReferents$composer", false);
   }

   /**
    * Assigne les String nb de maladies et prelevements dans les bons slots
    * du template MaladiesGroupHeader.
    * Si la collection autorise cross patient, le compte sera la totalité des
    * prélèvements, sinon celui des prélèvements spécifiés pour la banque.
    */
   private void populateMaladieGroupHeader(){

      String nbPrelevements = "0";

      // si le patient est deja enregistre en base
      if(this.patient.getPatientId() != null){
         nbPrelevements = String.valueOf(
            PatientUtils.getNbPrelsForPatientAndUser(patient, PatientUtils.getBanquesConsultForPrelevement(sessionScope)));

      }

      this.maladieGroupHeader = (maladieGroupHeaderTemplate.replaceFirst("\\{1\\}", String.valueOf(this.maladies.size())))
         .replaceFirst("\\{2\\}", nbPrelevements);

      getBinder().loadAttribute(groupMaladies, "label");
   }

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   private boolean canCreatePrel = false;

   public boolean isCanCreatePrel(){
      return canCreatePrel;
   }

   @Override
   public void applyDroitsOnFiche(){
      drawActionsButtons("Patient");
      if(sessionScope.containsKey("Banque")){
         canCreatePrel = drawActionOnOneButton("Prelevement", "Creation");
      }else if(sessionScope.containsKey("ToutesCollections")){
         // donne les droits de creation/modification patient
         // car acune map de droits générée dans ce cas
         setCanEdit(true);
         setCanNew(true);
         canCreatePrel = false;
      }

      getReferents().drawActionsForMedecins();

      super.applyDroitsOnFiche();
   }

   public String getPrelevementsFromOtherMaladiesListSize(){
      if(this.prelevementsFromOtherMaladies.size() < 5 && this.prelevementsFromOtherMaladies.size() > 0){
         return String.valueOf(this.prelevementsFromOtherMaladies.size());
      }
      return "5";
   }

   /*************************************************************************/
   /******************* PRELEVEMENTS AUTRE MALADIES *************************/
   /*************************************************************************/

   public List<Prelevement> getPrelevementsFromOtherMaladies(){
      return prelevementsFromOtherMaladies;
   }

   public PrelevementItemRenderer getPrelevementsFromOtherMaladiesRenderer(){
      return prelevementFromOtherMaladiesRenderer;
   }

   public boolean getHasPrelevementsFromOtherMaladies(){
      return getHasPrelevementsToShow() && SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope));
   }

   public boolean getHasPrelevementsToShow(){
      return this.prelevementsFromOtherMaladies.size() > 0;
   }

   /**
    * Affiche la fiche d'un prélèvement.
    */
   private void onClickPrelevementCode(final Event e){

      final PrelevementController tabController = (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

      if(e != null){ // un seul element a afficher
         final Prelevement prel = (Prelevement) e.getData();
         // change la banque au besoin
         if(!SessionUtils.getSelectedBanques(sessionScope).contains(prel.getBanque())){
            getMainWindow().updateSelectedBanque(prel.getBanque());
         }
         tabController.switchToFicheStaticMode(prel);
      }
   }

   @Override
   public void onClick$print(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("impression.print.patient"));
      sb.append(" ");
      sb.append(this.patient.getNom());
      sb.append(" ");
      sb.append(this.patient.getPrenom());

      openImpressionWindow(page, this, sb.toString(), isAnonyme(), isCanSeeHistorique());
   }

   /**
    * Méthode pour formatter le NIP si jamais le compte est en anonyme.
    *
    * @return
    */
   public String getNipFormatted(){
      if(isAnonyme()){
         makeLabelAnonyme(nipLabel, false);
         return getAnonymeString();
      }
      nipLabel.setSclass("formValue");
      return this.getObject().getNip();
   }

   /**
    * Méthode pour formatter le nom si jamais le compte est en anonyme.
    *
    * @return
    */
   public String getNomFormatted(){
      if(isAnonyme()){
         makeLabelAnonyme(nomLabel, false);
         return getAnonymeString();
      }
      nomLabel.setSclass("formValue");
      return this.getObject().getNom();
   }

   /**
    * Méthode pour formatter le nom de naissance si jamais
    * le compte est en anonyme.
    *
    * @return
    */
   public String getNomNaissanceFormatted(){
      if(isAnonyme()){
         makeLabelAnonyme(nomNaisLabel, false);
         return getAnonymeString();
      }
      nomNaisLabel.setSclass("formValue");
      return this.getObject().getNomNaissance();
   }

   /**
    * Méthode pour formatter le NIP si jamais le compte est en anonyme.
    *
    * @return
    */
   public String getPrenomFormatted(){
      if(isAnonyme()){
         makeLabelAnonyme(prenomLabel, false);
         return getAnonymeString();
      }
      prenomLabel.setSclass("formValue");
      return this.getObject().getPrenom();
   }

   public Patient getPatient(){
      return this.patient;
   }
   
   // @since 2.3.0-gatsbi  
   protected void setGroupMedecinsOpen(final boolean b){
      if(groupMedecins instanceof Group){
         ((Group) groupMedecins).setOpen(b);
      }else{
         ((Groupbox) groupMedecins).setOpen(b);
      }      
   }
   
   protected void setGroupMaladiesOpen(final boolean b){
      if(groupMaladies instanceof Group){
         ((Group) groupMaladies).setOpen(b);
      }else{
         ((Groupbox) groupMaladies).setOpen(b);
      }      
   }

}
