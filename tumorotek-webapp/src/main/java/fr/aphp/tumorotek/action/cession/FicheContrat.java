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
package fr.aphp.tumorotek.action.cession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstDateLimit;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.xml.CoupleSimpleValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneDeuxColonnesParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneSimpleParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.manager.validation.coeur.cession.ContratValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheContrat extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(FicheContrat.class);

   private static final long serialVersionUID = 6300875937416491348L;

   // Labels.
   private Label numeroLabel;

   private Label typeLabel;

   private Label descriptionLabel;

   private Label dateDemandeCessionLabel;

   private Label dateValidationLabel;

   private Label dateDemandeRedactionLabel;

   private Label dateEnvoiLabel;

   private Label dateSignatureLabel;

   private Label titreLabel;

   private Label etabLabelContrat;

   private Label serviceLabelContrat;

   private Label operateurLabelContrat;

   private Label montantLabel;

   private Group groupCessionsContrat;

   private Grid cessionsGrid;

   private Row rowGridCessions;

   private Row rowNbEchantillons;

   private Row rowNbProdDerives;

   private Menubar menuBar;

   private Label nbEchantillonsLabel;

   private Label nbProdDerivesLabel;

   private Group groupDelaisContrat;

   private Row rowDelaiValidation;

   private Row rowDelaiEnvoi;

   private Row rowDelaiSignature;

   private Row rowDelaiGlobal;

   private Label delaiValidationLabel;

   private Label delaiEnvoiLabel;

   private Label delaiSignatureLabel;

   private Label delaiGlobalLabel;

   // Editable components : mode d'édition ou de création.
   private Label numeroRequired;

   private Textbox numeroBox;

   private Textbox descriptionBox;

   private Listbox typesBox;

   private Datebox dateDemandeCessionBox;

   private Datebox dateValidationBox;

   private Datebox dateDemandeRedactionBox;

   private Datebox dateEnvoiBox;

   private Datebox dateSignatureBox;

   private Textbox titreBox;

   private Combobox etabsBoxContrat;

   private Combobox servicesBoxContrat;

   private Combobox operateursBoxContrat;

   private Decimalbox montantBox;

   private Div operateursBoxDiv;

   private Div servicesBoxDiv;

   // Objets Principaux.
   private Contrat contrat;

   // Associations.
   private final List<ProtocoleType> types = new ArrayList<>();

   private ProtocoleType selectedType;

   private List<String> nomsAndPrenoms = new ArrayList<>();

   private final List<Collaborateur> collaborateurs = new ArrayList<>();

   private Collaborateur selectedCollaborateur;

   private List<String> nomsServices = new ArrayList<>();

   private final List<Service> services = new ArrayList<>();

   private Service selectedService;

   private List<String> nomsEtablissements = new ArrayList<>();

   private List<Etablissement> etablissements = new ArrayList<>();

   private Etablissement selectedEtablissement;

   private List<Cession> cessions = new ArrayList<>();

   private boolean detailMode;

   // Variables formulaire.
   private String cessionsGroupHeader = Labels.getLabel("contrat.cessions.title");

   private final CessionRowRenderer cessionRenderer = new CessionRowRenderer(false, false);

   private Integer nbEchantillons;

   private Integer nbProdDerives;

   private String piedPage;

   private String hautPage;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.contrat");

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.numeroLabel, this.typeLabel, this.descriptionLabel,
         this.dateDemandeCessionLabel, this.dateValidationLabel, this.dateDemandeRedactionLabel, this.dateEnvoiLabel,
         this.dateSignatureLabel, this.titreLabel, this.etabLabelContrat, this.serviceLabelContrat, this.operateurLabelContrat,
         this.groupCessionsContrat, this.cessionsGrid, this.rowGridCessions, this.menuBar, this.montantLabel,
         this.rowNbEchantillons, this.rowNbProdDerives, this.groupDelaisContrat, this.rowDelaiEnvoi, this.rowDelaiGlobal,
         this.rowDelaiSignature, this.rowDelaiValidation});

      setObjBoxsComponents(new Component[] {this.numeroBox, this.typesBox, this.descriptionBox, this.dateDemandeCessionBox,
         this.dateValidationBox, this.dateDemandeRedactionBox, this.dateEnvoiBox, this.dateSignatureBox, this.titreBox,
         this.etabsBoxContrat, this.operateursBoxDiv, this.servicesBoxDiv, this.montantBox});

      setRequiredMarks(new Component[] {this.numeroRequired});

      drawActionsForContrats();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      menuBar.setVisible(false);

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){

      if(obj instanceof ContratDecorator){
         this.contrat = ((ContratDecorator) obj).getContrat();
      }else{
         this.contrat = (Contrat) obj;
      }

      cessions.clear();
      if(contrat.getContratId() != null){
         cessions.addAll(ManagerLocator.getContratManager().getCessionsManager(contrat));

         if(this.contrat.getProtocoleType() != null){
            selectedType = this.contrat.getProtocoleType();
         }else{
            selectedType = null;
         }

         if(this.contrat.getCollaborateur() != null && collaborateurs.contains(this.contrat.getCollaborateur())){
            selectedCollaborateur = this.contrat.getCollaborateur();
         }else if(this.contrat.getCollaborateur() != null){
            collaborateurs.add(this.contrat.getCollaborateur());
            selectedCollaborateur = this.contrat.getCollaborateur();
         }else{
            selectedCollaborateur = null;
         }

         if(this.contrat.getService() != null && services.contains(this.contrat.getService())){
            selectedService = this.contrat.getService();
         }else if(this.contrat.getService() != null){
            services.add(this.contrat.getService());
            selectedService = this.contrat.getService();
         }else{
            selectedService = null;
         }

      }else{
         cessions = new ArrayList<>();
         selectedCollaborateur = null;
         selectedEtablissement = null;
         selectedService = null;
         selectedType = null;
      }

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("contrat.cessions.title"));
      sb.append(" (");
      sb.append(cessions.size());
      sb.append(")");
      cessionsGroupHeader = sb.toString();

      // calcul du nb d'éléments cédés
      nbEchantillons = 0;
      nbProdDerives = 0;
      for(int i = 0; i < cessions.size(); i++){
         if(cessions.get(i).getCessionStatut() != null && !cessions.get(i).getCessionStatut().getStatut().equals("REFUSEE")){
            nbEchantillons += ManagerLocator.getCederObjetManager().getEchantillonsCedesByCessionManager(cessions.get(i)).size();
            nbProdDerives += ManagerLocator.getCederObjetManager().getProdDerivesCedesByCessionManager(cessions.get(i)).size();
         }
      }
      nbEchantillonsLabel
         .setValue(ObjectTypesFormatters.getLabel("contrat.nb.echantillons", new String[] {String.valueOf(nbEchantillons)}));
      nbProdDerivesLabel
         .setValue(ObjectTypesFormatters.getLabel("contrat.nb.prodDerives", new String[] {String.valueOf(nbProdDerives)}));

      // calcul du délai de validation
      if(this.contrat.getDateDemandeCession() != null && this.contrat.getDateValidation() != null){
         final Calendar calDem = Calendar.getInstance();
         calDem.setTime(contrat.getDateDemandeCession());
         final Calendar calVal = Calendar.getInstance();
         calVal.setTime(contrat.getDateValidation());
         final long diffMillis = calVal.getTimeInMillis() - calDem.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         delaiValidationLabel
            .setValue(ObjectTypesFormatters.getLabel("contrat.delai.validation", new String[] {String.valueOf(jours)}));
      }else{
         delaiValidationLabel.setValue(ObjectTypesFormatters.getLabel("contrat.delai.validation", new String[] {"-"}));
      }

      // calcul du délai d'envoi
      if(this.contrat.getDateDemandeRedaction() != null && this.contrat.getDateEnvoiContrat() != null){
         final Calendar calRedac = Calendar.getInstance();
         calRedac.setTime(contrat.getDateDemandeRedaction());
         final Calendar calEnvoi = Calendar.getInstance();
         calEnvoi.setTime(contrat.getDateEnvoiContrat());
         final long diffMillis = calEnvoi.getTimeInMillis() - calRedac.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         delaiEnvoiLabel.setValue(ObjectTypesFormatters.getLabel("contrat.delai.envoi", new String[] {String.valueOf(jours)}));
      }else{
         delaiEnvoiLabel.setValue(ObjectTypesFormatters.getLabel("contrat.delai.envoi", new String[] {"-"}));
      }

      // calcul du délai de signature
      if(this.contrat.getDateEnvoiContrat() != null && this.contrat.getDateSignature() != null){
         final Calendar calEnvoi = Calendar.getInstance();
         calEnvoi.setTime(contrat.getDateEnvoiContrat());
         final Calendar calSign = Calendar.getInstance();
         calSign.setTime(contrat.getDateSignature());
         final long diffMillis = calSign.getTimeInMillis() - calEnvoi.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         delaiSignatureLabel
            .setValue(ObjectTypesFormatters.getLabel("contrat.delai.signature", new String[] {String.valueOf(jours)}));
      }else{
         delaiSignatureLabel.setValue(ObjectTypesFormatters.getLabel("contrat.delai.signature", new String[] {"-"}));
      }

      // calcul du délai global
      if(this.contrat.getDateDemandeCession() != null && this.contrat.getDateSignature() != null){
         final Calendar calDem = Calendar.getInstance();
         calDem.setTime(contrat.getDateDemandeCession());
         final Calendar calSign = Calendar.getInstance();
         calSign.setTime(contrat.getDateSignature());
         final long diffMillis = calSign.getTimeInMillis() - calDem.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         delaiGlobalLabel.setValue(ObjectTypesFormatters.getLabel("contrat.delai.global", new String[] {String.valueOf(jours)}));
      }else{
         delaiGlobalLabel.setValue(ObjectTypesFormatters.getLabel("contrat.delai.global", new String[] {"-"}));
      }

      super.setObject(contrat);
   }

   @Override
   public void cloneObject(){
      setClone(this.contrat.clone());
   }

   @Override
   public void revertObject(){
      super.revertObject();
   }

   @Override
   public Contrat getObject(){
      return this.contrat;
   }

   @Override
   public ContratController getObjectTabController(){
      return (ContratController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Contrat());
      super.setNewObject();
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode statique.
    * @param detailMode Si true, la fiche se trouve dans une nouvelle
    * fenêtre pour afficher un contrat
    */
   @Override
   public void switchToStaticMode(final boolean dM){
      this.detailMode = dM;
      switchToStaticMode();

      if(this.contrat.getContratId() == null){
         menuBar.setVisible(false);
      }
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.contrat.equals(new Contrat()));

      editC.setVisible(!detailMode);
      addNewC.setVisible(!detailMode);
      deleteC.setVisible(!detailMode);

      if(this.contrat.getContratId() == null){
         menuBar.setVisible(false);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      initEditableMode();

      getBinder().loadComponent(self);
   }

   @Override
   public void setFocusOnElement(){
      numeroBox.setFocus(true);
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

      ManagerLocator.getContratManager().createObjectManager(contrat, selectedCollaborateur, selectedService,
         selectedEtablissement, selectedType, SessionUtils.getPlateforme(sessionScope), SessionUtils.getLoggedUser(sessionScope));

      if(getListeContrat() != null){
         // ajout du contrat à la liste
         getListeContrat().addToObjectList(this.contrat);
      }
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeContrat
    */
   private ListeContrat getListeContrat(){
      return getObjectTabController().getListe();
   }

   //	@Override
   //	public void onClick$cancelC() {
   //		clearData();
   //	}

   @Override
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getContratManager().isUsedObjectManager(getObject());
      setCascadable(false);
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isUsed){
         setDeleteMessage(Labels.getLabel("contrat.deletion.isReferenced"));
      }
      setDeletable(!isUsed);
      setFantomable(!isUsed);
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getContratManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope));
   }

   //	@Override
   //	public void onClick$editC() {
   //		if (this.contrat != null) {
   //			switchToEditMode();
   //		}
   //	}

   @Override
   public void onLaterRevert(){
      clearConstraints();
      super.onLaterRevert();
   }

   @Override
   public void onLaterCancel(){
      clearData();
      // ferme wait message
      Clients.clearBusy();
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * numeroBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$numeroBox(){
      numeroBox.setValue(numeroBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * titreBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$titreBox(){
      titreBox.setValue(titreBox.getValue().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.contrat.getNumero() != null){
         this.contrat.setNumero(this.contrat.getNumero().toUpperCase().trim());
      }

      if(this.contrat.getTitreProjet() != null){
         this.contrat.setTitreProjet(this.contrat.getTitreProjet().toUpperCase().trim());
      }
   }

   /**
    * Filtre les services par établissement.
    * @param event Event : seléction sur la liste etabsBoxContrat.
    * @throws Exception
    */
   public void onSelect$etabsBoxContrat(final Event event) throws Exception{
      final int ind = etabsBoxContrat.getSelectedIndex();
      if(ind > -1){
         selectedEtablissement = etablissements.get(ind);
      }else{
         selectedEtablissement = null;
      }
   }

   /**
    * Filtre les collaborateurs par service.
    * @param event Event : seléction sur la liste servicesBoxContrat.
    * @throws Exception
    */
   public void onSelect$servicesBoxContrat(final Event event) throws Exception{
      final int ind = servicesBoxContrat.getSelectedIndex();
      if(ind > -1){
         selectedService = services.get(ind);
      }else{
         selectedService = null;
      }
   }

   /**
    * Sélectionne le collaborateur.
    * @param event Event : seléction sur la liste operateursBoxContrat.
    * @throws Exception
    */
   public void onSelect$operateursBoxContrat(final Event event) throws Exception{
      final int ind = operateursBoxContrat.getSelectedIndex();
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }
   }

   public void onClickObject(final Event event){
      onClickNumeroCession(event);
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    * @param event Event : clique sur un code d'un dérivé dans
    * la liste des produits dérivés.
    * @throws Exception
    */
   public void onClickNumeroCession(final Event event){
      //		CessionController tabController =
      //			(CessionController) CessionController.backToMe(
      //					getMainWindow(), page);
      //
      //		if (event != null) {
      //			Cession cess = (Cession) event.getData();
      //			tabController.switchToFicheStaticMode(cess);
      //		} else {
      //			tabController.getListe().setListObjects(this.cessions);
      //			tabController.getListe().setCurrentObject(null);
      //			tabController.clearStaticFiche();
      //			tabController.getListe()
      //				.getBinder().loadAttribute(tabController.getListe()
      //										.getObjectsListGrid(), "model");
      //			tabController.switchToOnlyListeMode();
      //		}
      if(event != null){
         final Cession cess = (Cession) event.getData();
         displayObjectData(cess);
      }
   }

   /**
    * Forward Event.
    */
   public void onSelectAllCessions(final Event e){
      // onClickNumeroCession(null);
      displayObjectsListData(new ArrayList<TKAnnotableObject>(getCessions()));
   }

   @Override
   public void setEmptyToNulls(){

      if(this.contrat.getTitreProjet().equals("")){
         this.contrat.setTitreProjet(null);
      }

      if(this.contrat.getDescription().equals("")){
         this.contrat.setDescription(null);
      }

      // Gestion du collaborateur
      final String selectedNomAndPremon = operateursBoxContrat.getValue().toUpperCase();
      operateursBoxContrat.setValue(selectedNomAndPremon);
      int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      // Gestion du service
      final String selectedNomService = servicesBoxContrat.getValue().toUpperCase();
      servicesBoxContrat.setValue(selectedNomService);
      ind = nomsServices.indexOf(selectedNomService);
      if(ind > -1){
         selectedService = services.get(ind);
      }else{
         selectedService = null;
      }

      // Gestion de l'établissement
      final String selectedNom = etabsBoxContrat.getValue().toUpperCase();
      etabsBoxContrat.setValue(selectedNom);
      ind = nomsEtablissements.indexOf(selectedNom);
      if(ind > -1){
         selectedEtablissement = etablissements.get(ind);
      }else{
         selectedEtablissement = null;
      }
   }

   @Override
   public void updateObject(){

      setEmptyToNulls();
      setFieldsToUpperCase();

      ManagerLocator.getContratManager().updateObjectManager(contrat, selectedCollaborateur, selectedService,
         selectedEtablissement, selectedType, SessionUtils.getPlateforme(sessionScope), SessionUtils.getLoggedUser(sessionScope));

      if(getListeContrat() != null){
         // ajout de l'échantillon à la liste
         getListeContrat().updateObjectGridList(this.contrat);
      }

   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */

   public void initEditableMode(){
      if(types.isEmpty()){
         types.addAll(ManagerLocator.getProtocoleTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      }

      initCollaborations();
   }

   public void initCollaborations(){
      // init des étabs
      etablissements.clear();
      nomsEtablissements.clear();
      etablissements = ManagerLocator.getEtablissementManager().findAllActiveObjectsWithOrderManager();
      for(int i = 0; i < etablissements.size(); i++){
         nomsEtablissements.add(etablissements.get(i).getNom());
      }
      etabsBoxContrat.setModel(new CustomSimpleListModel(nomsEtablissements));
      etabsBoxContrat.setValue("");
      if(this.contrat.getEtablissement() != null){
         selectedEtablissement = this.contrat.getEtablissement();
         etabsBoxContrat.setValue(selectedEtablissement.getNom());
         if(!etablissements.contains(this.contrat.getEtablissement())){
            etablissements.add(this.contrat.getEtablissement());
            selectedEtablissement = this.contrat.getEtablissement();
            nomsEtablissements.add(this.contrat.getEtablissement().getNom());
         }
      }

      // init des services
      services.clear();
      nomsServices.clear();
      services.addAll(ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager());
      for(int i = 0; i < services.size(); i++){
         final StringBuffer sb = new StringBuffer();
         if(services.get(i) != null && services.get(i).getNom() != null){
            sb.append(services.get(i).getNom());
            if(services.get(i).getEtablissement() != null && services.get(i).getEtablissement().getNom() != null){
               sb.append(" (");
               sb.append(services.get(i).getEtablissement().getNom());
               sb.append(")");
            }
         }
         nomsServices.add(sb.toString());
      }
      servicesBoxContrat.setModel(new CustomSimpleListModel(nomsServices));
      servicesBoxContrat.setValue("");
      if(this.contrat.getService() != null){
         selectedService = this.contrat.getService();

         final StringBuffer sb = new StringBuffer();
         if(selectedService != null && selectedService.getNom() != null){
            sb.append(selectedService.getNom());
            if(selectedService.getEtablissement() != null && selectedService.getEtablissement().getNom() != null){
               sb.append(" (");
               sb.append(selectedService.getEtablissement().getNom());
               sb.append(")");
            }
         }

         servicesBoxContrat.setValue(sb.toString());
         if(!services.contains(this.contrat.getService())){
            services.add(this.contrat.getService());
            selectedService = this.contrat.getService();
            nomsServices.add(sb.toString());
         }
      }

      // init des collaborateurs
      collaborateurs.clear();
      nomsAndPrenoms.clear();
      collaborateurs.addAll(ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager());
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
      }
      operateursBoxContrat.setModel(new CustomSimpleListModel(nomsAndPrenoms));
      operateursBoxContrat.setValue("");
      if(this.contrat.getCollaborateur() != null){
         selectedCollaborateur = this.contrat.getCollaborateur();
         operateursBoxContrat.setValue(selectedCollaborateur.getNomAndPrenom());
         if(!collaborateurs.contains(this.contrat.getCollaborateur())){
            collaborateurs.add(this.contrat.getCollaborateur());
            selectedCollaborateur = this.contrat.getCollaborateur();
            nomsAndPrenoms.add(this.contrat.getCollaborateur().getNomAndPrenom());
         }
      }
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(numeroBox);
      Clients.clearWrongValue(descriptionBox);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForContrats(){
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

   /**
    * Clic sur le bouton print.
    */
   public void onClick$print(){
      openHeadersWindow(page, self);
   }

   public void onGetHeaders(final Event event){
      if(event.getData() != null){
         final String[] headers = (String[]) event.getData();
         if(headers[0] != null){
            hautPage = headers[0];
         }else{
            hautPage = null;
         }

         if(headers[1] != null){
            piedPage = headers[1];
         }else{
            piedPage = null;
         }
      }else{
         hautPage = null;
         piedPage = null;
      }
      Clients.showBusy(Labels.getLabel("impression.encours"));
      Events.echoEvent("onLaterPrint", self, null);
   }

   /**
    * Génère la fiche et la télécharge.
    */
   public void onLaterPrint(){
      // création du document XML contenant les données à imprimer
      final Document document = createDocumentXML();

      // Transformation du document en fichier
      byte[] dl = null;
      try{
         dl = ManagerLocator.getXmlUtils().creerPdf(document);

      }catch(final Exception e){
         log.error(e);
      }

      // ferme wait message
      Clients.clearBusy();

      // génération du nom du fichier
      final StringBuffer sb = new StringBuffer();
      final Calendar cal = Calendar.getInstance();
      final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
      sb.append("fiche_contrat");
      sb.append(date);
      sb.append(".pdf");

      // envoie du fichier à imprimer à l'utilisateur
      if(dl != null){
         Filedownload.save(dl, "application/pdf", sb.toString());
         dl = null;
      }
   }

   /**
    * Génère le Document JDOM contenant les infos à imprimer.
    * @return Document JDOM.
    */
   public Document createDocumentXML(){

      final Document document = ManagerLocator.getXmlUtils().createJDomDocument();
      final Element root = document.getRootElement();

      // ajout de la date en pied de page
      final StringBuffer sb = new StringBuffer();
      final Calendar cal = Calendar.getInstance();
      final String date = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
      sb.append(date);

      if(piedPage != null && piedPage.length() > 0){
         sb.append(" - ");
         sb.append(piedPage);
      }

      if(hautPage == null || hautPage.length() == 0){
         hautPage = " ";
      }

      ManagerLocator.getXmlUtils().addBasDePage(root, sb.toString());
      ManagerLocator.getXmlUtils().addHautDePage(root, hautPage, false, null);

      final Element page1 = ManagerLocator.getXmlUtils().addPage(root,
         ObjectTypesFormatters.getLabel("impression.contrat.title", new String[] {contrat.getNumero()}));
      addInfosProjetToPrint(page1);
      addInfosContratToPrint(page1);
      addInfosDelaisToPrint(page1);
      addInfosListeCessions(page1);

      return document;
   }

   /**
    * Ajout les infos générales à imprimer.
    * @param page
    */
   public void addInfosProjetToPrint(final Element page){
      // Date de demande
      String tmp = "";
      if(contrat.getDateDemandeCession() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(contrat.getDateDemandeCession());
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("contrat.dateDemande.cession"), tmp);
      // Date de validation du projet
      if(contrat.getDateValidation() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(contrat.getDateValidation());
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("contrat.dateValidation"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Titre du projet
      if(contrat.getTitreProjet() != null){
         tmp = contrat.getTitreProjet();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("contrat.titreProjet"), tmp);
      final LigneDeuxColonnesParagraphe li2 = new LigneDeuxColonnesParagraphe(cp3);

      // Type du projet
      if(contrat.getProtocoleType() != null){
         tmp = contrat.getProtocoleType().getType();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("contrat.protocoleType"), tmp);
      final LigneDeuxColonnesParagraphe li3 = new LigneDeuxColonnesParagraphe(cp4);

      // Description
      if(contrat.getDescription() != null){
         tmp = contrat.getDescription();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("contrat.description"), tmp);
      final LigneDeuxColonnesParagraphe li4 = new LigneDeuxColonnesParagraphe(cp5);

      // Etablissement
      if(contrat.getService() != null && contrat.getService().getEtablissement() != null
         && contrat.getService().getEtablissement().getNom() != null){
         tmp = contrat.getService().getEtablissement().getNom();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("contrat.etablissement"), tmp);
      final LigneDeuxColonnesParagraphe li5 = new LigneDeuxColonnesParagraphe(cp6);

      // Service
      if(contrat.getService() != null && contrat.getService().getNom() != null){
         tmp = contrat.getService().getNom();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("contrat.service"), tmp);
      final LigneDeuxColonnesParagraphe li6 = new LigneDeuxColonnesParagraphe(cp7);

      // Collaborateur
      if(contrat.getCollaborateur() != null){
         tmp = contrat.getCollaborateur().nomAndPrenom();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("contrat.collaborateur"), tmp);
      final LigneDeuxColonnesParagraphe li7 = new LigneDeuxColonnesParagraphe(cp8);

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("contrat.group.projet"),
         new Object[] {li1, li2, li3, li4, li5, li6, li7}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les infos générales à imprimer.
    * @param page
    */
   public void addInfosContratToPrint(final Element page){
      // Date de demande de rédaction
      String tmp = "";
      if(contrat.getDateDemandeRedaction() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(contrat.getDateDemandeRedaction());
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("contrat.dateDemande.redaction"), tmp);
      // Date d'envoie du contrat
      if(contrat.getDateEnvoiContrat() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(contrat.getDateEnvoiContrat());
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("contrat.dateEnvoi"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Date de signature
      if(contrat.getDateSignature() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(contrat.getDateSignature());
      }else{
         tmp = "-";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("contrat.dateSignature"), tmp);
      final LigneDeuxColonnesParagraphe li2 = new LigneDeuxColonnesParagraphe(cp3);

      // Montant
      if(contrat.getMontant() != null){
         tmp = String.valueOf(contrat.getMontant()) + " €";
      }else{
         tmp = "-";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("contrat.montant"), tmp);
      final LigneDeuxColonnesParagraphe li3 = new LigneDeuxColonnesParagraphe(cp4);

      final Paragraphe par1 =
         new Paragraphe(Labels.getLabel("contrat.group.contrat"), new Object[] {li1, li2, li3}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les infos générales à imprimer.
    * @param page
    */
   public void addInfosDelaisToPrint(final Element page){
      // delai de validation
      String tmp = "";
      if(this.contrat.getDateDemandeCession() != null && this.contrat.getDateValidation() != null){
         final Calendar calDem = Calendar.getInstance();
         calDem.setTime(contrat.getDateDemandeCession());
         final Calendar calVal = Calendar.getInstance();
         calVal.setTime(contrat.getDateValidation());
         final long diffMillis = calVal.getTimeInMillis() - calDem.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         tmp = String.valueOf(jours);
      }else{
         tmp = "-";
      }
      final CoupleSimpleValeur cp1 = new CoupleSimpleValeur(
         ObjectTypesFormatters.getLabel("contrat.delai.validation", new String[] {String.valueOf(tmp)}), "");
      final LigneSimpleParagraphe li1 = new LigneSimpleParagraphe(cp1);

      // calcul du délai d'envoi
      if(this.contrat.getDateDemandeRedaction() != null && this.contrat.getDateEnvoiContrat() != null){
         final Calendar calRedac = Calendar.getInstance();
         calRedac.setTime(contrat.getDateDemandeRedaction());
         final Calendar calEnvoi = Calendar.getInstance();
         calEnvoi.setTime(contrat.getDateEnvoiContrat());
         final long diffMillis = calEnvoi.getTimeInMillis() - calRedac.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         tmp = String.valueOf(jours);
      }else{
         tmp = "-";
      }
      final CoupleSimpleValeur cp2 =
         new CoupleSimpleValeur(ObjectTypesFormatters.getLabel("contrat.delai.envoi", new String[] {String.valueOf(tmp)}), "");
      final LigneSimpleParagraphe li2 = new LigneSimpleParagraphe(cp2);

      // calcul du délai de signature
      if(this.contrat.getDateEnvoiContrat() != null && this.contrat.getDateSignature() != null){
         final Calendar calEnvoi = Calendar.getInstance();
         calEnvoi.setTime(contrat.getDateEnvoiContrat());
         final Calendar calSign = Calendar.getInstance();
         calSign.setTime(contrat.getDateSignature());
         final long diffMillis = calSign.getTimeInMillis() - calEnvoi.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         tmp = String.valueOf(jours);
      }else{
         tmp = "-";
      }
      final CoupleSimpleValeur cp3 = new CoupleSimpleValeur(
         ObjectTypesFormatters.getLabel("contrat.delai.signature", new String[] {String.valueOf(tmp)}), "");
      final LigneSimpleParagraphe li3 = new LigneSimpleParagraphe(cp3);

      // calcul du délai global
      if(this.contrat.getDateDemandeCession() != null && this.contrat.getDateSignature() != null){
         final Calendar calDem = Calendar.getInstance();
         calDem.setTime(contrat.getDateDemandeCession());
         final Calendar calSign = Calendar.getInstance();
         calSign.setTime(contrat.getDateSignature());
         final long diffMillis = calSign.getTimeInMillis() - calDem.getTimeInMillis();
         // ie 86400000 = 24*60*60*1000
         // soit le nb de lillisecondes dans une année
         final long div = 86400000L;
         final int jours = (int) (diffMillis / div);
         tmp = String.valueOf(jours);
      }else{
         tmp = "-";
      }
      final CoupleSimpleValeur cp4 =
         new CoupleSimpleValeur(ObjectTypesFormatters.getLabel("contrat.delai.global", new String[] {String.valueOf(tmp)}), "");
      final LigneSimpleParagraphe li4 = new LigneSimpleParagraphe(cp4);

      final Paragraphe par1 =
         new Paragraphe(Labels.getLabel("contrat.group.delais"), new Object[] {li1, li2, li3, li4}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Crée le bloc contenant une liste de cessions.
    * @param cessions Cessions à imprimer.
    * @param champs Colonnes à imprimer.
    */
   public void addInfosListeCessions(final Element page){
      // Entete
      final String[] listeEntete = new String[6];
      listeEntete[0] = Labels.getLabel("Champ.Cession.Numero");
      listeEntete[1] = Labels.getLabel("echantillons.nb");
      listeEntete[2] = Labels.getLabel("derives.nb");
      listeEntete[3] = Labels.getLabel("Champ.Cession.DemandeDate");
      listeEntete[4] = Labels.getLabel("cessions.date.cession");
      listeEntete[5] = Labels.getLabel("Champ.Cession.CessionStatut");
      final EnteteListe entetes = new EnteteListe(listeEntete);

      // liste des cédés
      final LigneListe[] liste = new LigneListe[cessions.size()];
      for(int i = 0; i < cessions.size(); i++){
         final String[] valeurs = new String[6];
         // numero
         valeurs[0] = cessions.get(i).getNumero();
         // nb echantillons
         valeurs[1] =
            String.valueOf(ManagerLocator.getCederObjetManager().getEchantillonsCedesByCessionManager(cessions.get(i)).size());
         // nb derives
         valeurs[2] =
            String.valueOf(ManagerLocator.getCederObjetManager().getProdDerivesCedesByCessionManager(cessions.get(i)).size());
         // date de demande
         if(cessions.get(i).getDemandeDate() != null){
            valeurs[3] = ObjectTypesFormatters.dateRenderer2(cessions.get(i).getDemandeDate());
         }else{
            valeurs[3] = "-";
         }
         // date de cession
         if(cessions.get(i).getDepartDate() != null){
            valeurs[4] = ObjectTypesFormatters.dateRenderer2(cessions.get(i).getDepartDate());
         }else{
            valeurs[4] = "-";
         }
         // cession statut
         if(cessions.get(i).getCessionStatut() != null){
            valeurs[5] = ObjectTypesFormatters.ILNObjectStatut(cessions.get(i).getCessionStatut());
         }else{
            valeurs[5] = "-";
         }
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      ListeElement listeSites = null;
      if(cessions.size() > 0){
         listeSites = new ListeElement(null, entetes, liste);
      }

      // ligne nb Echantillons
      final CoupleSimpleValeur cp1 = new CoupleSimpleValeur(
         ObjectTypesFormatters.getLabel("contrat.nb.echantillons", new String[] {String.valueOf(nbEchantillons)}), "");
      final LigneSimpleParagraphe li1 = new LigneSimpleParagraphe(cp1);

      // ligne nb dérivés
      final CoupleSimpleValeur cp2 = new CoupleSimpleValeur(
         ObjectTypesFormatters.getLabel("contrat.nb.prodDerives", new String[] {String.valueOf(nbProdDerives)}), "");
      final LigneSimpleParagraphe li2 = new LigneSimpleParagraphe(cp2);

      // ajout du paragraphe
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("contrat.cessions.title"));
      sb.append(" (");
      sb.append(cessions.size());
      sb.append(")");
      final Paragraphe par = new Paragraphe(sb.toString(), new Object[] {li1, li2}, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   protected void validateCoherenceDate(final Component comp, final Object value){
      Errors errs = null;
      String field = "";

      if(value == null){
         // la contrainte est retiree
         if(comp.getId().equals("dateDemandeCessionBox")){
            this.contrat.setDateDemandeCession(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }else if(comp.getId().equals("dateValidationBox")){
            this.contrat.setDateValidation(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }else if(comp.getId().equals("dateDemandeRedactionBox")){
            this.contrat.setDateDemandeRedaction(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }else if(comp.getId().equals("dateEnvoiBox")){
            this.contrat.setDateEnvoiContrat(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }else if(comp.getId().equals("dateSignatureBox")){
            this.contrat.setDateSignature(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }

      }else{
         if(comp.getId().equals("dateDemandeCessionBox")){
            field = "dateDemandeCession";
            this.contrat.setDateDemandeCession((Date) value);
            errs = ContratValidator.checkDemandeDateCoherence(contrat);
         }else if(comp.getId().equals("dateValidationBox")){
            field = "dateValidation";
            this.contrat.setDateValidation((Date) value);
            errs = ContratValidator.checkDateValidation(contrat);
         }else if(comp.getId().equals("dateDemandeRedactionBox")){
            field = "dateDemandeRedaction";
            this.contrat.setDateDemandeRedaction((Date) value);
            errs = ContratValidator.checkDateDemandeRedaction(contrat);
         }else if(comp.getId().equals("dateEnvoiBox")){
            field = "dateEnvoiContrat";
            this.contrat.setDateEnvoiContrat((Date) value);
            errs = ContratValidator.checkDateEnvoiContrat(contrat);
         }else if(comp.getId().equals("dateSignatureBox")){
            field = "dateSignature";
            this.contrat.setDateSignature((Date) value);
            errs = ContratValidator.checkDateSignature(contrat);
         }

         // Si la date n'est pas vide, on applique la contrainte
         if(errs != null && errs.hasErrors()){
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   public void onBlur$dateDemandeCessionBox(){
      boolean badDateFormat = false;
      if(dateDemandeCessionBox.getErrorMessage() != null){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateDemandeCessionBox.clearErrorMessage(true);
         validateCoherenceDate(dateDemandeCessionBox, dateDemandeCessionBox.getValue());
      }
   }

   public void onBlur$dateValidationBox(){
      boolean badDateFormat = false;
      if(dateValidationBox.getErrorMessage() != null){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateValidationBox.clearErrorMessage(true);
         validateCoherenceDate(dateValidationBox, dateValidationBox.getValue());
      }
   }

   public void onBlur$dateDemandeRedactionBox(){
      boolean badDateFormat = false;
      if(dateDemandeRedactionBox.getErrorMessage() != null){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateDemandeRedactionBox.clearErrorMessage(true);
         validateCoherenceDate(dateDemandeRedactionBox, dateDemandeRedactionBox.getValue());
      }
   }

   public void onBlur$dateEnvoiBox(){
      boolean badDateFormat = false;
      if(dateEnvoiBox.getErrorMessage() != null){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateEnvoiBox.clearErrorMessage(true);
         validateCoherenceDate(dateEnvoiBox, dateEnvoiBox.getValue());
      }
   }

   public void onBlur$dateSignatureBox(){
      boolean badDateFormat = false;
      if(dateSignatureBox.getErrorMessage() != null){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateSignatureBox.clearErrorMessage(true);
         validateCoherenceDate(dateSignatureBox, dateSignatureBox.getValue());
      }
   }

   /*************************************************************************/
   /************************** OPERATEUR ************************************/
   /*************************************************************************/
   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * operateurAideSaisie. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection d'un collaborateur.
    */
   public void onClick$operateurAideSaisie(){
      // on récupère le collaborateur actuellement sélectionné
      // pour l'afficher dans la modale
      final List<Object> old = new ArrayList<>();
      if(getSelectedCollaborateur() != null){
         old.add(getSelectedCollaborateur());
      }

      // ouvre la modale
      openCollaborationsWindow(page, "general.recherche", "select", null, "Collaborateur", null, Path.getPath(self), old);
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * serviceAideSaisie. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection d'un service.
    */
   public void onClick$serviceAideSaisie(){
      // on récupère le service actuellement sélectionné
      // pour l'afficher dans la modale
      final List<Object> old = new ArrayList<>();
      if(getSelectedService() != null){
         old.add(getSelectedService());
      }

      // ouvre la modale
      openCollaborationsWindow(page, "general.recherche", "select", null, "Service", null, Path.getPath(self), old);
   }

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand
    * l'utilisateur sélectionne un collaborateur.
    * @param e Event contenant le collaborateur sélectionné.
    */
   public void onGetObjectFromSelection(final Event e){

      initCollaborations();

      if(e.getData() != null){
         if(e.getData() instanceof Collaborateur){
            selectCollaborateurFromModale((Collaborateur) e.getData());
         }else if(e.getData() instanceof Service){
            selectServiceFromModale((Service) e.getData());
         }
      }

   }

   /**
    * Sélectionne le collaborateur dans la liste (en provenance de
    * la modale). Il sélectionne également l'établissement et le service
    * dans les listes parentes.
    * @param collab
    */
   public void selectCollaborateurFromModale(final Collaborateur collab){
      if(nomsAndPrenoms.contains(collab.getNomAndPrenom())){
         final int ind = nomsAndPrenoms.indexOf(collab.getNomAndPrenom());
         selectedCollaborateur = collaborateurs.get(ind);
         operateursBoxContrat.setValue(selectedCollaborateur.getNomAndPrenom());

         final List<Service> tmpServices = new ArrayList<>();
         tmpServices.addAll(ManagerLocator.getCollaborateurManager().getServicesManager(selectedCollaborateur));
         if(tmpServices.size() == 1){
            selectedService = tmpServices.get(0);
         }else{
            int i = 0;
            boolean found = false;
            while(i < tmpServices.size() && !found){
               if(tmpServices.get(i).getEtablissement().equals(selectedCollaborateur.getEtablissement())){
                  selectedService = tmpServices.get(i);
                  found = true;
               }
               ++i;
            }
         }

         if(selectedService != null){
            servicesBoxContrat.setValue(nomsServices.get(services.indexOf(selectedService)));
            selectedEtablissement = selectedService.getEtablissement();
            etabsBoxContrat.setValue(selectedEtablissement.getNom());
         }
      }
   }

   /**
    * Sélectionne le service dans la liste (en provenance de
    * la modale). Il sélectionne également l'établissement
    * la liste parente.
    * @param collab
    */
   public void selectServiceFromModale(final Service serv){
      if(services.contains(serv)){
         selectedService = serv;
         servicesBoxContrat.setValue(nomsServices.get(services.indexOf(selectedService)));
         selectedEtablissement = selectedService.getEtablissement();
         etabsBoxContrat.setValue(selectedEtablissement.getNom());
      }
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/
   public Contrat getContrat(){
      return contrat;
   }

   public void setContrat(final Contrat c){
      setObject(c);
   }

   public boolean getCessionsListSizeSupOne(){
      return this.cessions.size() > 1;
   }

   public List<ProtocoleType> getTypes(){
      return types;
   }

   public ProtocoleType getSelectedType(){
      return selectedType;
   }

   public void setSelectedType(final ProtocoleType selected){
      this.selectedType = selected;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selected){
      this.selectedCollaborateur = selected;
   }

   public List<Service> getServices(){
      return services;
   }

   public Service getSelectedService(){
      return selectedService;
   }

   public void setSelectedService(final Service selected){
      this.selectedService = selected;
   }

   public String getSClassCollaborateur(){
      if(this.contrat != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.contrat.getCollaborateur());
      }
      return "";
   }

   public String getSClassService(){
      if(this.contrat != null){
         return ObjectTypesFormatters.sClassService(this.contrat.getService());
      }
      return "";
   }

   /**
    * Formate la date de demande du Contrat.
    * @return Date de demande formatée.
    */
   public String getDateDemandeCessionFormated(){
      if(this.contrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateDemandeCession());
      }
      return null;
   }

   /**
    * Formate la date de validation.
    * @return Date de validation formatée.
    */
   public String getDateValidationFormated(){
      if(this.contrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateValidation());
      }
      return null;
   }

   /**
    * Formate la date de demande de rédaction du Contrat.
    * @return Date de demande de rédactionformatée.
    */
   public String getDateDemandeRedactionFormated(){
      if(this.contrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateDemandeRedaction());
      }
      return null;
   }

   /**
    * Formate la date d'envoi du Contrat.
    * @return Date d'envoi formatée.
    */
   public String getDateEnvoiFormated(){
      if(this.contrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateEnvoiContrat());
      }
      return null;
   }

   /**
    * Formate la date de signature du Contrat.
    * @return Date de signature formatée.
    */
   public String getDateSignatureFormated(){
      if(this.contrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.contrat.getDateSignature());
      }
      return null;
   }

   public List<Etablissement> getEtablissements(){
      return etablissements;
   }

   public Etablissement getSelectedEtablissement(){
      return selectedEtablissement;
   }

   public void setSelectedEtablissement(final Etablissement selectedE){
      this.selectedEtablissement = selectedE;
   }

   public List<Cession> getCessions(){
      return cessions;
   }

   public String getCessionsGroupHeader(){
      return cessionsGroupHeader;
   }

   public CessionRowRenderer getCessionRenderer(){
      return cessionRenderer;
   }

   public ConstCode getNumeroConstraint(){
      return CessionConstraints.getNumeroConstraint();
   }

   public ConstWord getTitreProjetConstraint(){
      return CessionConstraints.getTitreProjetConstraint();
   }

   public ConstText getDescrConstraint(){
      return CessionConstraints.getDescrConstraint();
   }

   public ConstDateLimit getDateConstraint(){
      return CessionConstraints.getDateConstraint();
   }

   public Integer getNbEchantillons(){
      return nbEchantillons;
   }

   public void setNbEchantillons(final Integer nbE){
      this.nbEchantillons = nbE;
   }

   public Integer getNbProdDerives(){
      return nbProdDerives;
   }

   public void setNbProdDerives(final Integer nbP){
      this.nbProdDerives = nbP;
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }

   public String getPiedPage(){
      return piedPage;
   }

   public void setPiedPage(final String p){
      this.piedPage = p;
   }

   public String getHautPage(){
      return hautPage;
   }

   public void setHautPage(final String h){
      this.hautPage = h;
   }

   public List<String> getNomsAndPrenoms(){
      return nomsAndPrenoms;
   }

   public void setNomsAndPrenoms(final List<String> n){
      this.nomsAndPrenoms = n;
   }

   public List<String> getNomsServices(){
      return nomsServices;
   }

   public void setNomsServices(final List<String> n){
      this.nomsServices = n;
   }

   public List<String> getNomsEtablissements(){
      return nomsEtablissements;
   }

   public void setNomsEtablissements(final List<String> n){
      this.nomsEtablissements = n;
   }
}
