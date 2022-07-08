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
package fr.aphp.tumorotek.action.cession;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.event.PagingEvent;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.listmodel.ObjectPagingModel;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.action.utils.CessionUtils;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.component.ValeurDecimaleModale;
import fr.aphp.tumorotek.decorator.CederObjetDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.factory.CederObjetDecoratorFactory;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.validation.coeur.cession.CessionValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.CessionStatut;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.cession.ECederObjetStatut;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Temperature;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.2.3-rc1
 */
public class FicheCessionEdit extends AbstractFicheEditController
{

   private final Log log = LogFactory.getLog(FicheCessionEdit.class);

   private static final long serialVersionUID = 6300875937416491348L;

   protected Row rowSanitaire;

   protected Row rowRecherche1;

   protected Row rowRecherche2;

   protected Row rowDestruction;

   protected Row rowDescription;

   protected Row rowDestinataire;

   protected Row rowService;

   protected Row rowEtablissement;

   protected Row rowSeparator1;

   protected Row rowLine1;

   protected Row rowSeparator2;

   protected Row rowDateAndStatut;

   protected Row rowStatutDestruction;

   protected Row rowLine2;

   protected Row rowExecutant;

   protected Row rowDates;

   protected Row rowTransporteurAndTemp;

   protected Row rowSeparator3;

   private Group groupEchantillons;

   private Group groupProdDerives;

   private Div cederEchantillonsHbox;

   private Div cederDerivesHbox;

   private Combobox echantillonsBox;

   private Combobox derivesBox;

   private Hbox cederMultiEchantillonsBox;

   private Hbox cederMultiProdDerivesBox;

   private Label lastEchantillonsLabel;

   private Button cederEchantillonsButton;

   private Button cederDerivesButton;

   // Labels
   private Label cessionStatutLabel;

   private Label cessionStatutDestructionLabel;

   // Editable components : mode d'édition ou de création.
   private Textbox numeroBox;

   private Datebox dateDemandeBox;

   private CalendarBox dateDestructionCalBox;

   private Textbox descriptionBox;

   private Datebox dateValidationBox;

   private CalendarBox dateDepartCalBox;

   private CalendarBox dateArriveeCalBox;

   private Decimalbox temperatureBox;

   private Textbox observationsBox;

   protected Listbox typesBox;

   private Listbox statutsBox;

   private Listbox statutsBox2;

   private Combobox demandeurBox;

   private Combobox executantBox;

   private Combobox contratsComboBox;

   private Label demandeurHelpLabel;

   private Label descriptionHelpLabel;

   private Label dateValidationHelpLabel;

   private Label titreEtudeHelpLabel;

   private Label codeProdDeriveCede;

   private Label codeEchantillonCede;

   private Label echantillonsNonStockesLabel;

   private Label prodDerivesNonStockesLabel;

   private Listbox banquesEchantillonsBox;

   private Listbox banquesProdDerivesBox;

   //private Listbox etabsBoxCession;
   private Listbox servicesBoxCession;

   private Listbox destinataireBoxCession;

   private Column echanQteColBox;

   private Column deriveQteColBox;

   private Column deriveQteUniteColBox;

   private Column echanQteCol;

   private Column deriveQteCol;

   private Column deriveQteUniteCol;

   private Column echanDeleteCol;

   private Column deriveDeleteCol;

   // Objets Principaux.
   private Cession cession;

   private List<CederObjet> echantillonsCedes = new ArrayList<>();

   private List<CederObjet> derivesCedes = new ArrayList<>();

   private List<CederObjetDecorator> echantillonsCedesDecores = new ArrayList<>();

   private List<CederObjetDecorator> derivesCedesDecores = new ArrayList<>();

   private CederObjetDecoratorFactory cedeObjFactory;

   // paging
   private Grid echantillonsList;

   private Paging echansPaging;

   private ObjectPagingModel echansModel;

   private final int _pageSizeE = 10;

   private int _startPageNumberE = 0;

   private int _totalSizeE = 0;

   private boolean _needsTotalSizeUpdateE = true;

   private Grid derivesList;

   private Paging derivesPaging;

   private ObjectPagingModel derivesModel;

   private final int _pageSizeP = 10;

   private int _startPageNumberP = 0;

   private int _totalSizeP = 0;

   private boolean _needsTotalSizeUpdateP = true;

   // Associations.
   protected List<CessionType> types = new ArrayList<>();

   protected CessionType selectedCessionType;

   private List<CessionExamen> examens = new ArrayList<>();

   private CessionExamen selectedCessionExamen;

   private List<Contrat> contrats = new ArrayList<>();

   private List<String> contratsNoms = new ArrayList<>();

   private Contrat selectedContrat;

   private List<CessionStatut> statuts = new ArrayList<>();

   private CessionStatut selectedCessionStatut;

   private List<Transporteur> transporteurs = new ArrayList<>();

   private Transporteur selectedTransporteur;

   private List<DestructionMotif> motifs = new ArrayList<>();

   private DestructionMotif selectedDestructionMotif;

   private List<Collaborateur> collaborateurs = new ArrayList<>();

   private Collaborateur selectedDemandeur;

   private Collaborateur selectedExecutant;

   private List<String> nomsAndPrenoms = new ArrayList<>();

   private List<Service> services = new ArrayList<>();

   private List<String> nomsService = new ArrayList<>();

   private List<String> codesEchans = new ArrayList<>();

   private List<String> codesDerives = new ArrayList<>();

   private Terminale terminaleDestockage;

   private List<Banque> availableBanques = new ArrayList<>();

   private List<Temperature> temperatures = new ArrayList<>();

   private Temperature selectedTemperature;

   private List<Retour> retoursToComplete = new ArrayList<>();

   // nouveaux objets cedes
   private final List<TKStockableObject> newlyCeds = new ArrayList<>();
   // private Hashtable<TKAnnotableObject, String> oldEmpsForNewlyCeds =
   //	new Hashtable<TKAnnotableObject, String>();

   // gestion des destinataires
   private List<Collaborateur> destinataires = new ArrayList<>();

   private Collaborateur selectedDestinataire;

   private List<Service> servicesDest = new ArrayList<>();

   private Service selectedService;

   private List<Etablissement> etablissements = new ArrayList<>();

   private Etablissement selectedEtablissement;

   // Variables formulaire.
   private String echantillonsGroupHeader = Labels.getLabel("cession.echantillons");

   private String derivesGroupHeader = Labels.getLabel("cession.prodDerive");

   // droits
   private boolean isAnonyme = false;

   // 2.0.10.1 : date saisie pour remplir automatiquement le retour de
   // stockage lors d'une cession partielle
   private Calendar dateRetourEchansComplete;

   private Calendar dateRetourDerivesComplete;

   private CancelFromModalException cancelException;

   Float test;

   // @since 2.1.1 obj to be reverted onLaterEdit
   private final List<CederObjetDecorator> revertedObjs = new ArrayList<>();

   // @since 2.2.3-rc1
   private Label cederMultiEchantillonBanquesLabel;

   private ListModelList<Banque> availableBanquesForEchantillonModel;

   private Label cederMultiProdDeriveBanquesLabel;

   private ListModelList<Banque> availableBanquesForDeriveModel;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setWaitLabel("cession.creation.encours");

      drawActionsForCessions();

      cedeObjFactory = new CederObjetDecoratorFactory(isAnonyme(), null);
   }

   @Override
   public void setObject(final TKdataObject c){
      this.cession = (Cession) c;

      if(cession.getCessionId() != null){
         // récupération des échantillons cédés
         echantillonsCedes = ManagerLocator.getCederObjetManager().getEchantillonsCedesByCessionManager(cession);
         echantillonsCedesDecores = cedeObjFactory.decorateListe(echantillonsCedes);

         // récupération des dérivés cédés
         derivesCedes = ManagerLocator.getCederObjetManager().getProdDerivesCedesByCessionManager(cession);
         derivesCedesDecores = cedeObjFactory.decorateListe(derivesCedes);

         selectedContrat = this.cession.getContrat();
      }

      if(this.cession.getDestinataire() != null && destinataires.contains(this.cession.getDestinataire())){
         selectedDestinataire = this.cession.getDestinataire();
      }else if(this.cession.getDestinataire() != null){
         destinataires.add(this.cession.getDestinataire());
         selectedDestinataire = this.cession.getDestinataire();
      }else{
         selectedDestinataire = null;
      }

      if(this.cession.getServiceDest() != null && services.contains(this.cession.getServiceDest())){
         selectedService = this.cession.getServiceDest();
      }else if(this.cession.getServiceDest() != null){
         services.add(this.cession.getServiceDest());
         selectedService = this.cession.getServiceDest();
      }else{
         selectedService = null;
      }

      super.setObject(cession);

      initEchansModel(0);
      initDerivesModel(0);
   }

   private void initEchansModel(final int pageN){
      echansPaging.setActivePage(pageN);
      _needsTotalSizeUpdateE = true;
      _startPageNumberE = pageN;
      refreshModelE(_startPageNumberE);
   }

   private void initDerivesModel(final int pageN){
      derivesPaging.setActivePage(pageN);
      _needsTotalSizeUpdateP = true;
      _startPageNumberP = pageN;
      refreshModelP(_startPageNumberP);
   }

   @Override
   public Cession getObject(){
      return this.cession;
   }

   @Override
   public void setNewObject(){
      setObject(new Cession());
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public Prelevement getParentObject(){
      return null;
   }

   @Override
   public Cession getCopy(){
      return (Cession) super.getCopy();
   }

   @Override
   public CessionController getObjectTabController(){
      return (CessionController) super.getObjectTabController();
   }

   public List<TKStockableObject> getNewlyCeds(){
      return newlyCeds;
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   @Override
   public void switchToEditMode(){
      super.switchToEditMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      // en fonction du type de cession, on affiche différents formulaires
      switchToCessionTypeToDisplay(this.cession.getCessionType().getType().toUpperCase());

      // si la cession est en attente et incomplete, on affiche le formulaire
      // pour modifier cette donnée ainsi que la version éditable des
      // objets cédés
      if(this.cession.getCessionStatut().getStatut().equals("EN ATTENTE")){
         // this.echantillonsListEdit.setVisible(true);
         this.cederEchantillonsHbox.setVisible(true);
         if(!cederEchantillonsButton.isDisabled()){
            this.cederMultiEchantillonsBox.setVisible(true);
         }else{
            this.cederMultiEchantillonsBox.setVisible(false);
         }
         if(!cederDerivesButton.isDisabled()){
            this.cederMultiProdDerivesBox.setVisible(true);
         }else{
            this.cederMultiProdDerivesBox.setVisible(false);
         }
         this.cederDerivesHbox.setVisible(true);

         echanQteCol.setVisible(false);
         deriveQteCol.setVisible(false);
         echanQteColBox.setVisible(true);
         deriveQteColBox.setVisible(true);
         deriveQteUniteColBox.setVisible(true);
         deriveQteUniteCol.setVisible(false);
         echanDeleteCol.setVisible(true);
         deriveDeleteCol.setVisible(true);

      }else{
         this.cederEchantillonsHbox.setVisible(false);
         this.cederDerivesHbox.setVisible(false);
         this.cederMultiEchantillonsBox.setVisible(false);
         this.cederMultiProdDerivesBox.setVisible(false);

         echanQteCol.setVisible(true);
         deriveQteCol.setVisible(true);
         echanQteColBox.setVisible(false);
         deriveQteColBox.setVisible(false);
         deriveQteUniteColBox.setVisible(false);
         deriveQteUniteCol.setVisible(true);
         echanDeleteCol.setVisible(false);
         deriveDeleteCol.setVisible(false);
      }

      // header de la liste des echantillons cédés
      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("cession.echantillons"));
      sb.append(" (");
      sb.append(echantillonsCedes.size());
      sb.append(")");
      echantillonsGroupHeader = sb.toString();
      groupEchantillons.setLabel(echantillonsGroupHeader);
      this.groupEchantillons.setOpen(true);

      // header de la liste des dérivés cédés
      sb = new StringBuffer();
      sb.append(Labels.getLabel("cession.prodDerive"));
      sb.append(" (");
      sb.append(derivesCedes.size());
      sb.append(")");
      derivesGroupHeader = sb.toString();
      groupProdDerives.setLabel(derivesGroupHeader);
      this.groupProdDerives.setOpen(true);

      getBinder().loadComponent(self);
   }

   /**
    * Change le mode de la fiche en creation.
    *
    * @param echansACeder  Liste d'échantillons à céder dans cette nouvelle
    *                      cession.
    * @param derivesACeder Liste de dérivés à céder dans cette nouvelle
    *                      cession.
    */
   public void switchToCreateMode(final List<Echantillon> echansACeder, final List<ProdDerive> derivesACeder,
      final Terminale terminale){
      // init de tous les nouveaux objets
      // TK-64 : Les annotations de cession ne se déverrouillent pas quand on fait une nouvelle cession
      //setNewObject();
      this.terminaleDestockage = terminale;

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      super.switchToCreateMode();

      // en fonction du type de cession, on affiche différents formulaires
      switchToCessionTypeToDisplay(this.selectedCessionType.getType().toUpperCase());

      // lors de la création, les objets cédés sont éditables
      // this.echantillonsListEdit.setVisible(true);
      this.cederEchantillonsHbox.setVisible(true);
      // this.derivesListEdit.setVisible(true);
      this.cederDerivesHbox.setVisible(true);
      if(!cederEchantillonsButton.isDisabled()){
         this.cederMultiEchantillonsBox.setVisible(true);
      }else{
         this.cederMultiEchantillonsBox.setVisible(false);
      }
      if(!cederDerivesButton.isDisabled()){
         this.cederMultiProdDerivesBox.setVisible(true);
      }else{
         this.cederMultiProdDerivesBox.setVisible(false);
      }
      // this.echantillonsList.setVisible(false);
      // this.derivesList.setVisible(false);

      // header de la liste des echantillons cédés
      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("cession.echantillons"));
      sb.append(" (");
      sb.append(echantillonsCedes.size());
      sb.append(")");
      echantillonsGroupHeader = sb.toString();
      groupEchantillons.setLabel(echantillonsGroupHeader);
      this.groupEchantillons.setOpen(true);

      // header de la liste des dérivés cédés
      sb = new StringBuffer();
      sb.append(Labels.getLabel("cession.prodDerive"));
      sb.append(" (");
      sb.append(derivesCedes.size());
      sb.append(")");
      derivesGroupHeader = sb.toString();
      groupProdDerives.setLabel(derivesGroupHeader);
      this.groupProdDerives.setOpen(true);

      // si une liste d'échantillons à céder est passée en paramètre
      if(echansACeder != null){
         // pour chaque échantillon
         boolean nonStocke = false;
         for(int i = echansACeder.size() - 1; i >= 0; i--){
            final Echantillon echan = echansACeder.get(i);
            // si l'échantillon est stocké
            if(echan.getObjetStatut().getStatut().equals("STOCKE")){
               // on cède l'échantillon
               cederEchantillon(echan);
            }else{
               nonStocke = true;
            }
         }
         if(nonStocke){
            echantillonsNonStockesLabel.setVisible(true);
         }else{
            echantillonsNonStockesLabel.setVisible(false);
         }
      }

      // si une liste de dérivés à céder est passée en paramètre
      if(derivesACeder != null){
         // pour chaque dérivé
         boolean nonStocke = false;
         for(int i = derivesACeder.size() - 1; i >= 0; i--){
            final ProdDerive derive = derivesACeder.get(i);
            // si le derive est stocké
            if(derive.getObjetStatut().getStatut().equals("STOCKE")){
               // on cède le derive
               cederProdDerive(derive);
            }else{
               nonStocke = true;
            }
         }
         if(nonStocke){
            prodDerivesNonStockesLabel.setVisible(true);
         }else{
            prodDerivesNonStockesLabel.setVisible(false);
         }
      }

      cederDerivesButton.setVisible(true);
      cederEchantillonsButton.setVisible(true);
      getBinder().loadComponent(self);

      initEchansModel(0);
      initDerivesModel(0);
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */

   public void initEditableMode(){
      // init des types. Si la cession n'a pas de type, c'est le type
      // RECHERCHE qui sera sélectionné au départ
      types = ManagerLocator.getCessionTypeManager().findAllObjectsManager();
      final CessionType tmp = ManagerLocator.getCessionTypeManager().findByTypeLikeManager("Recherche", false).get(0);
      typesBox.setModel(new SimpleListModel<>(types));
      if(this.cession.getCessionType() != null){
         selectedCessionType = this.cession.getCessionType();
         typesBox.setSelectedIndex(types.indexOf(this.cession.getCessionType()));
      }else{
         if(types.contains(tmp)){
            selectedCessionType = types.get(types.indexOf(tmp));
         }else{
            selectedCessionType = types.get(0);
         }

         typesBox.setSelectedIndex(types.indexOf(this.selectedCessionType));
      }
      // init des examens
      examens = ManagerLocator.getCessionExamenManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      examens.add(0, null);
      selectedCessionExamen = this.cession.getCessionExamen();

      // init des Mtas
      contrats = ManagerLocator.getContratManager().findAllObjectsByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
      selectedContrat = this.cession.getContrat();

      contratsNoms = new ArrayList<>();
      for(final Contrat contrat : contrats){
         final StringBuilder sb = new StringBuilder();
         sb.append(contrat.getNumero());
         if(contrat.getTitreProjet() != null && !contrat.getTitreProjet().equals("")){
            sb.append(" (");
            sb.append(contrat.getTitreProjet());
            sb.append(")");
         }
         contratsNoms.add(sb.toString());
      }
      contratsComboBox.setModel(new CustomSimpleListModel(contratsNoms));
      if(selectedContrat != null){
         final StringBuilder sb = new StringBuilder();
         sb.append(selectedContrat.getNumero());
         if(selectedContrat.getTitreProjet() != null && !selectedContrat.getTitreProjet().equals("")){
            sb.append(" (");
            sb.append(selectedContrat.getTitreProjet());
            sb.append(")");
         }
         contratsComboBox.setValue(sb.toString());
      }

      // init des transporteurs
      transporteurs = ManagerLocator.getTransporteurManager().findAllActiveManager();
      transporteurs.add(0, null);
      selectedTransporteur = this.cession.getTransporteur();

      // init des motifs
      motifs = ManagerLocator.getDestructionMotifManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      motifs.add(0, null);
      selectedDestructionMotif = this.cession.getDestructionMotif();

      temperatures = ManagerLocator.getTemperatureManager().findAllObjectsManager();
      temperatures.add(0, null);
      selectedTemperature = null;

      // init des collaborateurs
      nomsAndPrenoms = new ArrayList<>();
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      for(final Collaborateur collaborateur : collaborateurs){
         nomsAndPrenoms.add(collaborateur.getNomAndPrenom());
      }

      if(this.cession.getDemandeur() != null && !collaborateurs.contains(this.cession.getDemandeur())){
         collaborateurs.add(this.cession.getDemandeur());
         nomsAndPrenoms.add(this.cession.getDemandeur().getNomAndPrenom());
      }
      if(this.cession.getDestinataire() != null && !collaborateurs.contains(this.cession.getDestinataire())){
         collaborateurs.add(this.cession.getDestinataire());
         nomsAndPrenoms.add(this.cession.getDestinataire().getNomAndPrenom());
      }
      if(this.cession.getExecutant() != null && !collaborateurs.contains(this.cession.getExecutant())){
         collaborateurs.add(this.cession.getExecutant());
         nomsAndPrenoms.add(this.cession.getExecutant().getNomAndPrenom());
      }
      demandeurBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
      executantBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      if(this.cession.getDemandeur() != null){
         selectedDemandeur = this.cession.getDemandeur();
         demandeurBox.setValue(selectedDemandeur.getNomAndPrenom());
      }else{
         demandeurBox.setValue("");
      }

      if(this.cession.getExecutant() != null){
         selectedExecutant = this.cession.getExecutant();
         executantBox.setValue(selectedExecutant.getNomAndPrenom());
      }else{
         executantBox.setValue("");
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
      servicesBoxCession.setModel(listTmp);
      servicesBoxCession.setSelectedIndex(services.indexOf(selectedService));

      // init des collaborateurs
      if(selectedService != null){
         destinataires = ManagerLocator.getServiceManager().getActiveCollaborateursWithOrderManager(selectedService);
      }else{
         destinataires = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      }
      destinataires.add(0, null);

      final ListModel<Collaborateur> listCollabs = new ListModelList<>(destinataires);
      destinataireBoxCession.setModel(listCollabs);
      destinataireBoxCession.setSelectedIndex(destinataires.indexOf(selectedDestinataire));

      // Gestion des statuts
      // si la cession n'a pas de statut, c'est le statut "EN ATTENTE"
      // qui sera sélectionné
      statuts = ManagerLocator.getCessionStatutManager().findAllObjectsManager();
      statutsBox.setModel(new SimpleListModel<>(statuts));
      statutsBox2.setModel(new SimpleListModel<>(statuts));
      final CessionStatut validee = ManagerLocator.getCessionStatutManager().findByStatutLikeManager("VALIDEE", true).get(0);
      final CessionStatut refusee = ManagerLocator.getCessionStatutManager().findByStatutLikeManager("REFUSEE", true).get(0);
      final CessionStatut attente = ManagerLocator.getCessionStatutManager().findByStatutLikeManager("EN ATTENTE", true).get(0);
      if(this.cession.getCessionStatut() != null){
         selectedCessionStatut = this.cession.getCessionStatut();
      }else{
         selectedCessionStatut = statuts.get(statuts.indexOf(attente));
      }
      statutsBox.setSelectedIndex(statuts.indexOf(selectedCessionStatut));
      statutsBox2.setSelectedIndex(statuts.indexOf(selectedCessionStatut));

      if(selectedCessionStatut.equals(validee) || selectedCessionStatut.equals(refusee)){
         cessionStatutLabel.setVisible(true);
         cessionStatutDestructionLabel.setVisible(true);
         statutsBox.setVisible(false);
         statutsBox2.setVisible(false);
         rowStatutDestruction.setVisible(false);
      }else{
         cessionStatutLabel.setVisible(false);
         cessionStatutDestructionLabel.setVisible(false);
      }

      // si la cession est incomplete, on recupere les codes echantillons
      // et des dérivés qui peuvent être cédés
      if(selectedCessionStatut.getStatut().equals("EN ATTENTE")){
         final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0);
         if(sessionScope.containsKey("AdminPF")){
            availableBanques = ManagerLocator.getBanqueManager()
               .findByPlateformeAndArchiveManager(SessionUtils.getPlateforme(sessionScope), false);
         }else{
            // init des banques dispos
            availableBanques = ManagerLocator.getBanqueManager().findByEntiteModifByUtilisateurManager(
               SessionUtils.getLoggedUser(sessionScope), entite, SessionUtils.getPlateforme(sessionScope));
         }

         // init des listbox des banques
         final SimpleListModel<Banque> list = new SimpleListModel<>(availableBanques);
         banquesEchantillonsBox.setModel(list);
         banquesEchantillonsBox.setSelectedIndex(availableBanques.indexOf(SessionUtils.getSelectedBanques(sessionScope).get(0)));
         banquesProdDerivesBox.setModel(list);
         banquesProdDerivesBox.setSelectedIndex(availableBanques.indexOf(SessionUtils.getSelectedBanques(sessionScope).get(0)));

         // @since 2.2.3-rc1
         // possibilité de choisir les banques pour la sélection des échantillons
         availableBanquesForEchantillonModel = new ListModelList<>(availableBanques);
         availableBanquesForEchantillonModel.setMultiple(true);
         availableBanquesForEchantillonModel.addToSelection(SessionUtils.getCurrentBanque(sessionScope));
         availableBanquesForDeriveModel = new ListModelList<>(availableBanques);
         availableBanquesForDeriveModel.setMultiple(true);
         availableBanquesForDeriveModel.addToSelection(SessionUtils.getCurrentBanque(sessionScope));

         codesEchans = ManagerLocator.getEchantillonManager()
            .findAllCodesForBanqueAndStockesManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
         echantillonsBox.setModel(new CustomSimpleListModel(codesEchans));

         codesDerives = ManagerLocator.getProdDeriveManager()
            .findAllCodesForBanqueAndStockesManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
         derivesBox.setModel(new CustomSimpleListModel(codesDerives));
      }
   }

   /**
    * Méthode affichant le formulaire du mode sanitaire.
    */
   public void switchToSanitaireMode(){
      setVisibleRow(rowSanitaire, true);
      setVisibleRow(rowRecherche1, false);
      setVisibleRow(rowRecherche2, false);
      setVisibleRow(rowDestruction, false);
      setVisibleRow(rowStatutDestruction, false);

      setVisibleRow(rowDescription, true);
      setVisibleRow(rowDestinataire, true);
      setVisibleRow(rowService, true);
      setVisibleRow(rowEtablissement, true);
      setVisibleRow(rowSeparator1, true);
      setVisibleRow(rowLine1, true);
      setVisibleRow(rowSeparator2, true);
      setVisibleRow(rowDateAndStatut, true);
      setVisibleRow(rowLine2, true);
      setVisibleRow(rowExecutant, true);
      setVisibleRow(rowDates, true);
      setVisibleRow(rowTransporteurAndTemp, true);
      setVisibleRow(rowSeparator3, true);
   }

   /**
    * Méthode affichant le formulaire du mode recherche.
    */
   public void switchToRechercheMode(){
      setVisibleRow(rowSanitaire, false);
      setVisibleRow(rowRecherche1, true);
      setVisibleRow(rowRecherche2, true);
      setVisibleRow(rowDestruction, false);
      setVisibleRow(rowStatutDestruction, false);

      setVisibleRow(rowDescription, true);
      setVisibleRow(rowDestinataire, true);
      setVisibleRow(rowService, true);
      setVisibleRow(rowEtablissement, true);
      setVisibleRow(rowSeparator1, true);
      setVisibleRow(rowLine1, true);
      setVisibleRow(rowSeparator2, true);
      setVisibleRow(rowDateAndStatut, true);
      setVisibleRow(rowLine2, true);
      setVisibleRow(rowExecutant, true);
      setVisibleRow(rowDates, true);
      setVisibleRow(rowTransporteurAndTemp, true);
      setVisibleRow(rowSeparator3, true);
   }

   /**
    * Méthode affichant le formulaire du mode destruction.
    */
   public void switchToDestructionMode(){
      setVisibleRow(rowSanitaire, false);
      setVisibleRow(rowRecherche1, false);
      setVisibleRow(rowRecherche2, false);
      setVisibleRow(rowDestruction, true);
      setVisibleRow(rowStatutDestruction, true);

      setVisibleRow(rowDescription, false);
      setVisibleRow(rowDestinataire, false);
      setVisibleRow(rowService, false);
      setVisibleRow(rowEtablissement, false);
      setVisibleRow(rowSeparator1, false);
      setVisibleRow(rowLine1, false);
      setVisibleRow(rowSeparator2, false);
      setVisibleRow(rowDateAndStatut, false);
      setVisibleRow(rowLine2, false);
      setVisibleRow(rowExecutant, true);
      setVisibleRow(rowDates, false);
      setVisibleRow(rowTransporteurAndTemp, false);
      setVisibleRow(rowSeparator3, false);
   }

   @Override
   public void createNewObject(){

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("newCessionTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);

      final List<File> filesCreated = new ArrayList<>();

      try{
         // on remplit l'échantillon en fonction des champs nulls
         setEmptyToNulls();

         // on place tous les objets cédés dans une seule liste
         final List<CederObjet> objetsCedes = new ArrayList<>();
         echantillonsCedes.clear();
         for(final CederObjetDecorator echantillonsCedesDecore : echantillonsCedesDecores){
            objetsCedes.add(echantillonsCedesDecore.getCederObjet());
            echantillonsCedes.add(echantillonsCedesDecore.getCederObjet());
         }
         derivesCedes.clear();
         for(final CederObjetDecorator derivesCedesDecore : derivesCedesDecores){
            objetsCedes.add(derivesCedesDecore.getCederObjet());
            derivesCedes.add(derivesCedesDecore.getCederObjet());
         }

         cession.setCessionStatut(getSelectedCessionStatut());

         prepareCedesTKobjs(true);

         // create de l'objet
         ManagerLocator.getCessionManager().createObjectManager(cession, SessionUtils.getSelectedBanques(sessionScope).get(0),
            selectedCessionType, selectedCessionExamen, selectedContrat, selectedDestinataire, selectedService, selectedDemandeur,
            selectedCessionStatut, selectedExecutant, selectedTransporteur, selectedDestructionMotif,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(), filesCreated,
            SessionUtils.getLoggedUser(sessionScope), objetsCedes, SessionUtils.getSystemBaseDir());

         Document doc = null;
         if(cession.getCessionStatut().getStatut().equals("VALIDEE")){
            doc = CessionUtils.createFileHtmlToPrint(cession, echantillonsCedes, derivesCedes);
         }

         // s'il n'y a pas eu d'erreurs lors de la création, on
         // met à jour les échantillons et les produits dérivés
         // (quantité, volume et statut)
         updateCedesTkObjs();

         // on vérifie que l'on retrouve bien la page contenant la liste
         // des stockages
         if(getMainWindow().isFullfilledComponent("stockagePanel", "winStockages")){
            if(getStockageController() != null){
               getStockageController().clearAllPage();
               getStockageController().getListeStockages().updateAllConteneurs(false);
            }
         }

         if(doc != null){
            // Transformation du document en fichier
            byte[] dl = null;
            try{
               dl = ManagerLocator.getXmlUtils().creerBoiteHtml(doc);
            }catch(final Exception e){
               log.error(e);
            }

            // envoie du fichier à imprimer à l'utilisateur
            if(dl != null){
               // si c'est au format html, on va ouvrir une nouvelle
               // fenêtre
               try{
                  sessionScope.put("File", dl);
                  execution.sendRedirect("/impression", "_blank");
               }catch(final Exception e){
                  if(sessionScope.containsKey("File")){
                     sessionScope.remove("File");
                     dl = null;
                  }
               }
               dl = null;
            }
         }
      }catch(final RuntimeException re){
         ManagerLocator.getTxManager().rollback(status);
         for(final File f : filesCreated){
            f.delete();
         }
         //			if (cancelException != null) {
         //				cession.setCessionId(null);
         //			}
         throw re;
      }finally{
         cancelException = null;
         // détache la modale de complétion des retours en cas d'erreur / annulation
         if(self.hasFellow("dateRetourCompleteModale")){
            self.getFellow("dateRetourCompleteModale").detach();
         }
      }
      ManagerLocator.getTxManager().commit(status);
   }

   @Override
   public void updateObject(){

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("newCessionTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);

      try{
         // on remplit l'échantillon en fonction des champs nulls
         setEmptyToNulls();

         // on place tous les objets cédés dans une seule liste
         final List<CederObjet> objetsCedes = new ArrayList<>();
         echantillonsCedes.clear();
         for(final CederObjetDecorator echantillonsCedesDecore : echantillonsCedesDecores){
            objetsCedes.add(echantillonsCedesDecore.getCederObjet());
            echantillonsCedes.add(echantillonsCedesDecore.getCederObjet());
         }
         derivesCedes.clear();
         for(final CederObjetDecorator derivesCedesDecore : derivesCedesDecores){
            objetsCedes.add(derivesCedesDecore.getCederObjet());
            derivesCedes.add(derivesCedesDecore.getCederObjet());
         }

         cession.setCessionStatut(selectedCessionStatut);

         prepareCedesTKobjs(true);

         // update de l'objet
         ManagerLocator.getCessionManager().updateObjectManager(cession, cession.getBanque(), selectedCessionType,
            selectedCessionExamen, selectedContrat, selectedDestinataire, selectedService, selectedDemandeur,
            selectedCessionStatut, selectedExecutant, selectedTransporteur, selectedDestructionMotif,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
            getObjectTabController().getFicheAnnotation().getValeursToDelete(), filesCreated, filesToDelete,
            SessionUtils.getLoggedUser(sessionScope), objetsCedes, SessionUtils.getSystemBaseDir());

         // revert
         if(!revertedObjs.isEmpty()){
            getObjectTabController().updateReferencedObjects(revertCessedObjects());
         }

         // s'il n'y a pas d'erreurs et que la quantité, le volume ou
         // le statut des objets ont pu changé (la cession était
         // EN ATTENTE), on met a jour les dérivés et les échantillons
         if(getCopy().getCessionStatut().getStatut().equals("EN ATTENTE")){
            // on affiche la page HTML permettant d'imprimer les opérations
            // pour céder les objets
            Document doc = null;
            if(cession.getCessionStatut().getStatut().equals("VALIDEE")){
               doc = CessionUtils.createFileHtmlToPrint(cession, echantillonsCedes, derivesCedes);
            }

            updateCedesTkObjs();

            // on vérifie que l'on retrouve bien la page contenant la liste
            // des stockages
            if(getMainWindow().isFullfilledComponent("stockagePanel", "winStockages")){
               if(getStockageController() != null){
                  getStockageController().clearAllPage();
                  getStockageController().getListeStockages().updateAllConteneurs(false);
               }
            }

            // update contrats
            getObjectTabController().updateContrat(getObject().getContrat());

            if(doc != null){
               // Transformation du document en fichier
               byte[] dl = null;
               try{
                  dl = ManagerLocator.getXmlUtils().creerBoiteHtml(doc);
               }catch(final Exception e){
                  log.error(e);
               }

               // envoie du fichier à imprimer à l'utilisateur
               if(dl != null){
                  // si c'est au format html, on va ouvrir une nouvelle
                  // fenêtre
                  try{
                     sessionScope.put("File", dl);
                     execution.sendRedirect("/impression", "_blank");
                  }catch(final Exception e){
                     if(sessionScope.containsKey("File")){
                        sessionScope.remove("File");
                        dl = null;
                     }
                  }
                  dl = null;
               }
            }
         }

         for(final File f : filesToDelete){
            f.delete();
         }

      }catch(final RuntimeException re){
         ManagerLocator.getTxManager().rollback(status);
         for(final File f : filesCreated){
            f.delete();
         }
         throw re;
      }finally{
         cancelException = null;
         // détache la modale de complétion des retours en cas d'erreur / annulation
         if(self.hasFellow("dateRetourCompleteModale")){
            self.getFellow("dateRetourCompleteModale").detach();
         }
      }
      ManagerLocator.getTxManager().commit(status);

   }

   @Override
   public void onClick$create(){
      // validation des dates
      onBlur$dateDepartCalBox();
      onBlur$dateArriveeCalBox();
      onBlur$dateDestructionCalBox();

      super.onClick$create();
   }

   @Override
   public boolean onLaterCreate(){
      try{
         // validation des dates
         onBlur$dateDepartCalBox();
         onBlur$dateArriveeCalBox();
         onBlur$dateDestructionCalBox();

         super.onLaterCreate();

         if(!getNewlyCeds().isEmpty()){
            Date date = null;
            if(cession.getDepartDate() != null){
               date = cession.getDepartDate().getTime();
            }else if(cession.getValidationDate() != null){
               date = cession.getValidationDate();
            }

            proposeRetoursCreation(getNewlyCeds(), null, date, cession, null, null,
               ManagerLocator.getOperationTypeManager().findByNomLikeManager("Creation", true).get(0),
               Labels.getLabel("ficheRetour.observations.cession"), getSelectedExecutant());
         }
         if(terminaleDestockage != null){
            getStockageController().switchToFicheTerminaleMode(terminaleDestockage, null);
         }

         // storage robotisé
         proposeStorageRobotItem();

         return true;
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   @Override
   public void onClick$validate(){
      // validation des dates
      onBlur$dateDepartCalBox();
      onBlur$dateArriveeCalBox();
      onBlur$dateDestructionCalBox();

      super.onClick$validate();
   }

   @Override
   public boolean onLaterUpdate(){
      try{

         if(super.onLaterUpdate()){

            if(!getNewlyCeds().isEmpty()){

               Date date = null;
               if(cession.getDepartDate() != null){
                  date = cession.getDepartDate().getTime();
               }else if(cession.getValidationDate() != null){
                  date = cession.getValidationDate();
               }

               proposeRetoursCreation(getNewlyCeds(), null, date, cession, null, null,
                  ManagerLocator.getOperationTypeManager().findByNomLikeManager("Modification", true).get(0),
                  Labels.getLabel("ficheRetour.observations.cession"), getSelectedExecutant());
            }
         }

         // storage robotisé
         proposeStorageRobotItem();

         return true;
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         if(!(re instanceof CancelFromModalException)){
            Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         }
         return false;
      }
   }

   /**
    * Mets a jour les échantillons et derives cédés qui ont été supprimés de la
    * cession.
    *
    * @return liste des children mis à jour.
    * @since 2.1.1
    */
   private List<TKdataObject> revertCessedObjects(){
      final List<TKdataObject> children = new ArrayList<>();

      for(final CederObjetDecorator deco : revertedObjs){

         if(deco.getEchantillon() != null){
            revertEchantillon(deco);
            children.add(deco.getEchantillon());
         }else if(deco.getProdDerive() != null){
            revertProdDerive(deco);
            children.add(deco.getProdDerive());
         }
      }
      return children;
   }

   @Override
   protected void setEmptyToNulls(){
      if(this.cession.getDescription() != null && this.cession.getDescription().equals("")){
         this.cession.setDescription(null);
      }

      if(this.cession.getObservations() != null && this.cession.getObservations().equals("")){
         this.cession.setObservations(null);
      }

      if(this.cession.getEtudeTitre() != null && this.cession.getEtudeTitre().equals("")){
         this.cession.setEtudeTitre(null);
      }

      // on place tous les objets cédés dans une seule liste
      final List<CederObjet> objetsCedes = new ArrayList<>();
      echantillonsCedes = new ArrayList<>();
      for(int i = 0; i < echantillonsCedesDecores.size(); i++){
         objetsCedes.add(echantillonsCedesDecores.get(i).getCederObjet());
         echantillonsCedes.add(echantillonsCedesDecores.get(i).getCederObjet());
      }
      derivesCedes = new ArrayList<>();
      for(int i = 0; i < derivesCedesDecores.size(); i++){
         objetsCedes.add(derivesCedesDecores.get(i).getCederObjet());
         derivesCedes.add(derivesCedesDecores.get(i).getCederObjet());
      }

      // si la cession est "en attente" on récupère son etat dans la
      // checkbox, sinon elle est automatiquement complète
      if(selectedCessionStatut.getStatut().equals("EN ATTENTE")){
         cession.setEtatIncomplet(true);
      }else{
         cession.setEtatIncomplet(false);
      }

      // Gestion du demandeur
      final String selectedNomAndPremonDemandeur = this.demandeurBox.getValue().toUpperCase();
      this.demandeurBox.setValue(selectedNomAndPremonDemandeur);
      int ind = nomsAndPrenoms.indexOf(selectedNomAndPremonDemandeur);
      if(ind > -1){
         selectedDemandeur = collaborateurs.get(ind);
      }else{
         selectedDemandeur = null;
      }

      // Gestion de l'exécutant
      final String selectedNomAndPremonExecutant = this.executantBox.getValue().toUpperCase();
      this.executantBox.setValue(selectedNomAndPremonExecutant);
      ind = nomsAndPrenoms.indexOf(selectedNomAndPremonExecutant);
      if(ind > -1){
         selectedExecutant = collaborateurs.get(ind);
      }else{
         selectedExecutant = null;
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){
      numeroBox.setFocus(true);

   }

   /*************************************************************************/
   /************************** FORMATTERS ***********************************/
   /*************************************************************************/
   public Date getDepartDateProxy(){
      if(cession != null && cession.getDepartDate() != null){
         return cession.getDepartDate().getTime();
      }
      return null;
   }

   public void setDepartDateProxy(final Date date){
      final Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cession.setDepartDate(cal);
   }

   public Date getArriveeDateProxy(){
      if(cession != null && cession.getArriveeDate() != null){
         return cession.getArriveeDate().getTime();
      }
      return null;
   }

   public void setArriveeDateProxy(final Date date){
      final Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cession.setArriveeDate(cal);
   }

   public Date getDestructionDateProxy(){
      if(cession != null && cession.getDestructionDate() != null){
         return cession.getDestructionDate().getTime();
      }
      return null;
   }

   public void setDestructionDateProxy(final Date date){
      final Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cession.setDestructionDate(cal);
   }

   /**
    * Retourne le controller de la fiche d'un échantillon.
    *
    * @return
    */
   @Override
   public EchantillonController getEchantillonController(){
      if(getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){
         return ((EchantillonController) getMainWindow().getMainTabbox().getTabpanels().getFellow("echantillonPanel")
            .getFellow("winEchantillon").getAttributeOrFellow("winEchantillon$composer", true));
      }
      return null;
   }

   /**
    * Retourne le controller de la fiche d'un produit dérivé.
    *
    * @return
    */
   private StockageController getStockageController(){
      if(getMainWindow().isFullfilledComponent("stockagePanel", "winStockages")){
         return ((StockageController) getMainWindow().getMainTabbox().getTabpanels().getFellow("stockagePanel")
            .getFellow("winStockages").getAttributeOrFellow("winStockages$composer", true));
      }
      return null;
   }

   /**
    * Cette méthode va retourner le Service contenu dans la ligne
    * courante de la grille servicesListEdit.
    *
    * @param target Event sur la grille contenant la liste des Services.
    * @return Un Service.
    */
   public Object getBindingData(Component target){

      try{
         while(!(target instanceof Row || target instanceof Listitem)){
            target = target.getParent();
         }
         final Map<?, ?> map = (Map<?, ?>) target.getAttribute("zkplus.databind.TEMPLATEMAP");
         return map.get(target.getAttribute("zkplus.databind.VARNAME"));
      }catch(final NullPointerException e){
         return null;
      }
   }

   /*************************************************************************/
   /************************** LINKS ****************************************/
   /*************************************************************************/
   /**
    * Affiche la fiche d'un échantillon.
    *
    * @param event Event : clique sur un lien codeEchantillonCedeEdit dans
    *              la liste des échantillons.
    * @throws Exception
    */
   public void onClick$codeEchantillonCedeEdit(final Event event){
      onClickEchantillonCode(event);
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    *
    * @param event Event : clique sur un lien codeProdDeriveCedeEdit dans
    *              la liste des produits dérivés.
    * @throws Exception
    */
   public void onClick$codeProdDeriveCedeEdit(final Event event){
      onClickProdDeriveCode(event);
   }

   /**
    * Affiche la fiche d'un échantillon.
    *
    * @param event Event : clique sur un lien codeEchantillonCede dans la liste
    *              des échantillons.
    * @throws Exception
    */
   public void onClick$codeEchantillonCede(final Event event){
      onClickEchantillonCode(event);
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    *
    * @param event Event : clique sur un lien codeProdDeriveCede dans la liste
    *              des produits dérivés.
    * @throws Exception
    */
   public void onClick$codeProdDeriveCede(final Event event){
      onClickProdDeriveCode(event);
   }

   /**
    * Affiche la fiche d'un échantillon.
    */
   public void onClickEchantillonCode(final Event event){
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      displayObjectData(deco.getEchantillon());
   }

   public void onSelectAllEchantillons(){
      final List<CederObjetDecorator> decos = cedeObjFactory.decorateListe(echantillonsCedes);
      displayObjectsListData(new ArrayList<TKAnnotableObject>(cedeObjFactory.undecorateListe(decos)));
   }

   public void onClickProdDeriveCode(final Event event){
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      displayObjectData(deco.getProdDerive());
   }

   public void onSelectAllDerives(){
      final List<CederObjetDecorator> decos = cedeObjFactory.decorateListe(derivesCedes);
      displayObjectsListData(new ArrayList<TKAnnotableObject>(cedeObjFactory.undecorateListe(decos)));
   }

   /**
    * Méthode appelée lorsque l'utilisateur sélectionne un type de cession.
    * Le formulaire changera en fonction du type choisi.
    */
   public void onSelect$typesBox(){
      final int ind = typesBox.getSelectedIndex();

      if(ind > -1){
         selectedCessionType = types.get(ind);
      }

      if(selectedCessionType.getType().toUpperCase().equals("SANITAIRE")){
         switchToSanitaireMode();
         this.demandeurHelpLabel.setVisible(false);
         this.descriptionHelpLabel.setVisible(false);
         this.dateValidationHelpLabel.setVisible(false);
         this.titreEtudeHelpLabel.setVisible(false);
         statutsBox.setSelectedIndex(statuts.indexOf(selectedCessionStatut));
         dateDestructionCalBox.setValue(null);

         // on vide les champs non sanitaires
         selectedDestructionMotif = null;
         cession.setDestructionDate(null);
         selectedContrat = null;
         contratsComboBox.setValue(null);
         cession.setEtudeTitre(null);
      }else if(selectedCessionType.getType().toUpperCase().equals("RECHERCHE")){
         switchToRechercheMode();
         this.demandeurHelpLabel.setVisible(true);
         this.descriptionHelpLabel.setVisible(true);
         this.dateValidationHelpLabel.setVisible(true);
         this.titreEtudeHelpLabel.setVisible(true);
         statutsBox.setSelectedIndex(statuts.indexOf(selectedCessionStatut));
         dateDestructionCalBox.setValue(null);

         // on vide les champs non recherches
         selectedDestructionMotif = null;
         cession.setDestructionDate(null);
         selectedCessionExamen = null;
      }else if(selectedCessionType.getType().toUpperCase().equals("DESTRUCTION")){
         switchToDestructionMode();
         this.demandeurHelpLabel.setVisible(false);
         this.descriptionHelpLabel.setVisible(false);
         this.dateValidationHelpLabel.setVisible(false);
         this.titreEtudeHelpLabel.setVisible(false);
         statutsBox2.setSelectedIndex(statuts.indexOf(selectedCessionStatut));
         dateDepartCalBox.setValue(null);
         dateArriveeCalBox.setValue(null);
         dateValidationBox.setValue(null);

         // on vide les champs non destructions
         selectedContrat = null;
         contratsComboBox.setValue(null);
         cession.setEtudeTitre(null);
         selectedCessionExamen = null;
         cession.setDescription(null);
         selectedEtablissement = null;
         selectedService = null;
         selectedDestinataire = null;
         cession.setValidationDate(null);
         cession.setDepartDate(null);
         cession.setArriveeDate(null);
         selectedTransporteur = null;
         cession.setTemperature(null);
         servicesBoxCession.setSelectedIndex(0);
         destinataireBoxCession.setSelectedIndex(0);
      }
      getBinder().loadComponent(self);
   }

   /**
    * Méthode appelée lorsque l'utilisateur sélectionne un statut de cession.
    */
   public void onSelect$statutsBox(){
      final int ind = statutsBox.getSelectedIndex();

      if(ind > -1){
         selectedCessionStatut = statuts.get(ind);
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur sélectionne un statut de cession.
    */
   public void onSelect$statutsBox2(){
      final int ind = statutsBox2.getSelectedIndex();

      if(ind > -1){
         selectedCessionStatut = statuts.get(ind);
      }
   }

   /**
    * sélection d'une banque pour choisir les échantillons à céder.
    */
   public void onSelect$banquesEchantillonsBox(){
      final Banque selected = availableBanques.get(banquesEchantillonsBox.getSelectedIndex());

      codesEchans = ManagerLocator.getEchantillonManager().findAllCodesForBanqueAndStockesManager(selected);
      echantillonsBox.setModel(new CustomSimpleListModel(codesEchans));

      getBinder().loadComponent(echantillonsBox);
   }

   /**
    * sélection d'une banque pour choisir les dérivés à céder.
    */
   public void onSelect$banquesProdDerivesBox(){
      final Banque selected = availableBanques.get(banquesProdDerivesBox.getSelectedIndex());

      codesDerives = ManagerLocator.getProdDeriveManager().findAllCodesForBanqueAndStockesManager(selected);
      derivesBox.setModel(new CustomSimpleListModel(codesDerives));

      getBinder().loadComponent(derivesBox);
   }

   /**
    * Méthode appelée lors de la sélection d'une température dans la
    * liste temperatureListBox : mets à jour la valeur dans
    * temperatureBox.
    *
    * @param event Select sur la liste temperatureListBox.
    */
   public void onSelect$temperatureListBox(final Event event){

      if(selectedTemperature != null){
         cession.setTemperature(selectedTemperature.getTemperature());
      }else{
         cession.setTemperature(null);
      }
   }

   /**
    * Filtre les services par établissement.
    *
    * @param event Event : seléction sur la liste etabsBoxCession.
    * @throws Exception
    */
   public void onSelect$etabsBoxCession(final Event event) throws Exception{
      if(selectedEtablissement != null){
         services = ManagerLocator.getEtablissementManager().getActiveServicesWithOrderManager(selectedEtablissement);
      }else{
         services = ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager();
      }
      services.add(0, null);

      final ListModel<Service> list = new ListModelList<>(services);
      servicesBoxCession.setModel(list);

      if(!services.contains(selectedService)){
         selectedService = null;
      }

      servicesBoxCession.setSelectedIndex(services.indexOf(selectedService));
   }

   /**
    * Filtre les collaborateurs par service.
    *
    * @param event Event : seléction sur la liste servicesBoxCession.
    * @throws Exception
    */
   public void onSelect$servicesBoxCession(final Event event) throws Exception{
      final int ind = servicesBoxCession.getSelectedIndex();
      selectedService = services.get(ind);
      if(selectedService != null){
         destinataires = ManagerLocator.getServiceManager().getActiveCollaborateursWithOrderManager(selectedService);
      }else{
         destinataires = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      }
      destinataires.add(0, null);

      final ListModel<Collaborateur> list = new ListModelList<>(destinataires);
      destinataireBoxCession.setModel(list);

      if(!destinataires.contains(selectedDestinataire)){
         selectedDestinataire = null;
      }

      destinataireBoxCession.setSelectedIndex(destinataires.indexOf(selectedDestinataire));
   }

   /**
    * Sélectionne le collaborateur.
    *
    * @param event Event : seléction sur la liste destinataireBoxCession.
    * @throws Exception
    */
   public void onSelect$destinataireBoxCession(final Event event) throws Exception{
      final int ind = destinataireBoxCession.getSelectedIndex();
      selectedDestinataire = destinataires.get(ind);
   }

   @Override
   public void onClick$numerotation(){
      cession.setNumero(generateCodeAndUpdateNumerotation());
   }

   /**
    * Méthode appelée lors du clic sur le bouton remplissageAuto. Le
    * destinataire et le service de la cession seront alors automatiquement
    * remplis à partir de ceux du protocoleExt.
    */
   public void onClick$remplissageAuto(){
      if(selectedContrat != null){

         // remplissage auto du du demandeur
         final Collaborateur coll = selectedContrat.getCollaborateur();
         if(coll != null){
            if(nomsAndPrenoms.contains(coll.getNomAndPrenom())){
               final int indColl = nomsAndPrenoms.indexOf(coll.getNomAndPrenom());
               selectedDemandeur = collaborateurs.get(indColl);
               demandeurBox.setValue(selectedDemandeur.getNomAndPrenom());
            }
         }

         // remplissage de la date de validation
         if(selectedContrat.getDateValidation() != null){
            this.cession.setValidationDate(selectedContrat.getDateValidation());
         }

         // remplissage du titre de l'étude
         if(selectedContrat.getTitreProjet() != null){
            this.cession.setEtudeTitre(selectedContrat.getTitreProjet());
         }

         // remplissage de la description
         if(selectedContrat.getDescription() != null){
            this.cession.setDescription(selectedContrat.getDescription());
         }
      }
   }

   /**
    * Colore les lignes de la liste en fonction du nb d'échantillons
    * restants.
    */
   public void showLastEchantillons(final int start, final int end, final int pageIdx){
      final Rows rows = echantillonsList.getRows();
      final List<Component> comps = rows.getChildren();

      int nb = 0;

      // permet d'assigner un warning au dernier échantillons quand
      // tous les échantillons d'un patient sont sélectionnés
      final Hashtable<Integer, Integer> prels = new Hashtable<>();

      for(int i = start; i < end; i++){
         final Prelevement prel = echantillonsCedesDecores.get(i).getEchantillon().getPrelevement();
         if(prel != null){
            if(prels.containsKey(prel.getPrelevementId())){
               prels.put(prel.getPrelevementId(), prels.get(prel.getPrelevementId()) + 1);
            }else{
               prels.put(prel.getPrelevementId(), 1);
            }
            if(PrelevementUtils.getNbEchanRestants(prel) - prels.get(prel.getPrelevementId()) <= 0){
               ++nb;
               final Row row = (Row) comps.get(i - pageIdx);
               row.setStyle("background-color : #FEBAB3");
            }
         }
      }

      if(nb > 0){
         lastEchantillonsLabel.setVisible(true);
      }else{
         lastEchantillonsLabel.setVisible(false);
      }
   }

   /**
    * Méthode appelée lors du clic sur le bouton cederEchantillonsButton.
    * L'échantillon dont le code est saisi dans le champ echantillonsBox
    * sera alors ajouté à la liste des échantillons cédés.
    */
   public void onClick$cederEchantillonsButton(){
      final String code = echantillonsBox.getValue(); //.toUpperCase();
      echantillonsBox.setValue(code);
      final Banque selected = availableBanques.get(banquesEchantillonsBox.getSelectedIndex());

      // on vérifie que l'utilisateur a bien saisie un code
      if(code != null && !code.equals("")){
         // on vérifie que ce code est valide : il appartient à la
         // liste proposée
         if(codesEchans.contains(code)){
            // on récupère l'échantillon correspondant au code
            final Echantillon added =
               ManagerLocator.getEchantillonManager().findByCodeLikeWithBanqueManager(code, selected, true).get(0);

            // on cède l'échantillon
            cederEchantillon(added);

            initEchansModel(0);

         }else{
            throw new WrongValueException(echantillonsBox, Labels.getLabel("cession.error.code.invalid.echantillon"));
         }
      }else{
         throw new WrongValueException(echantillonsBox, Labels.getLabel("cession.error.empty.code.echantillon"));
      }
   }

   /**
    * Méthode appelée lors du clic sur le lien cederMultiEchantillonsLabel.
    * Une modale sera ouverte pour permettre la sélection de plusieurs
    * échantillons parmis toutes les banques disponibles
    */
   public void onClick$cederMultiEchantillonsLabel(){
      EchantillonController.backToMe(getMainWindow(), page);
      // on bloque tous les panels sauf celui des échantillons
      getMainWindow().blockAllPanelsExceptOne("echantillonTab");
      getEchantillonController().switchToSelectMode(Path.getPath(self),
         new ArrayList<>(availableBanquesForEchantillonModel.getSelection()));
   }

   /**
    * Méthode permettant de recevoir les échantillons sélectionnés dans
    * la modal.
    *
    * @param e Event contenant la liste des échantillons à céder
    */

   public void onGetEchantillonsFromSelection(final Event e){
      final List<Echantillon> echansACeder = (List<Echantillon>) e.getData();
      // si une liste d'objets à céder est passée en paramètre
      if(echansACeder != null){
         boolean nonStocke = false;
         for(int i = echansACeder.size() - 1; i >= 0; i--){
            // on vérifie que l'élément renvoyé est bien un échantillon
            if(echansACeder.get(i).getClass().getSimpleName().equals("Echantillon")){
               final Echantillon echan = echansACeder.get(i);
               // si l'échantillon est stocké, on le cède
               if(echan.getObjetStatut().getStatut().equals("STOCKE")){
                  cederEchantillon(echan);
               }else{
                  nonStocke = true;
               }
            }
         }
         if(nonStocke){
            echantillonsNonStockesLabel.setVisible(true);
         }else{
            echantillonsNonStockesLabel.setVisible(false);
         }
      }

      initEchansModel(0);

   }

   /**
    * Méthode permettant de créer un CederObjet à partir d'un échantillon.
    *
    * @param echantillonACeder Echantillon que l'on souhaite céder.
    */
   public void cederEchantillon(final Echantillon echantillonACeder){

      if(echantillonACeder != null){
         // création d'un nouveau CederObjet
         final CederObjet obj = new CederObjet();
         obj.setCession(this.cession);
         obj.setEntite(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
         obj.setObjetId(echantillonACeder.getEchantillonId());
         // Init de la quanité et du volume
         if(echantillonACeder.getQuantite() != null){
            obj.setQuantite((float) 0.0);
         }else{
            obj.setQuantite(null);
         }
         obj.setQuantiteUnite(echantillonACeder.getQuantiteUnite());

         // on vérifie que l'échantillon n'a pas déja été cédé
         // dans cette cession
         if(!echantillonsCedes.contains(obj)){
            // création d'un Decorator
            final CederObjetDecorator decorated = new CederObjetDecorator(obj, true, isAnonyme(), false);

            // Maj de la liste
            echantillonsCedes.add(0, obj);
            echantillonsCedesDecores.add(0, decorated);
            // ListModel<CederObjetDecorator> list =
            // 	new ListModelList<CederObjetDecorator> (echantillonsCedesDecores);
            // echantillonsListEdit.setModel(list);

            // header de la liste des echantillons cédés
            final StringBuffer sb = new StringBuffer();
            sb.append(Labels.getLabel("cession.echantillons"));
            sb.append(" (");
            sb.append(echantillonsCedes.size());
            sb.append(")");
            echantillonsGroupHeader = sb.toString();
            groupEchantillons.setLabel(echantillonsGroupHeader);

            getBinder().loadComponent(echantillonsList);
         }

         //			if (!newlyCeds.contains(echantillonACeder)) {
         //				newlyCeds.add(echantillonACeder);
         //			}

      }else{
         throw new WrongValueException(echantillonsBox, Labels.getLabel("cession.error.code.invalid.echantillon"));
      }
   }

   /**
    * Méthode appelée lors du clic sur le bouton cederDerivesButton.
    * Le dérivé dont le code est saisi dans le champ deriveBox
    * sera alors ajouté à la liste des dérivés cédés.
    */
   public void onClick$cederDerivesButton(){
      final String code = derivesBox.getValue().toUpperCase();
      derivesBox.setValue(code);
      final Banque selected = availableBanques.get(banquesProdDerivesBox.getSelectedIndex());

      // on vérifie que l'utilisateur a bien saisie un code
      if(code != null && !code.equals("")){
         // on vérifie que ce code est valide : il appartient à la
         // liste proposée
         if(codesDerives.contains(code)){
            // on récupère le dérivé correspondant au code
            final ProdDerive addedDerive =
               ManagerLocator.getProdDeriveManager().findByCodeOrLaboWithBanqueManager(code, selected, true).get(0);

            cederProdDerive(addedDerive);

            initDerivesModel(0);

         }else{
            throw new WrongValueException(derivesBox, Labels.getLabel("cession.error.code.invalid.prodDerive"));
         }
      }else{
         throw new WrongValueException(derivesBox, Labels.getLabel("cession.error.empty.code.prodDerive"));
      }
   }

   /**
    * Méthode appelée lors du clic sur le lien cederMultiProdDerivesLabel.
    * Une modale sera ouverte pour permettre la sélection de plusieurs
    * dérivés.
    */
   public void onClick$cederMultiProdDerivesLabel(){
      ProdDeriveController.backToMe(getMainWindow(), page);

      // on bloque tous les panels sauf celui des échantillons
      getMainWindow().blockAllPanelsExceptOne("deriveTab");
      // on passe en mode de sélection
      getProdDeriveController().switchToSelectMode(Path.getPath(self),
         new ArrayList<>(availableBanquesForDeriveModel.getSelection()));

   }

   /**
    * Méthode permettant de recevoir les dérivés sélectionnés dans
    * la modal.
    *
    * @param e Event contenant la liste des dérivés à céder
    */

   public void onGetDerivesFromSelection(final Event e){
      final List<ProdDerive> derivesACeder = (List<ProdDerive>) e.getData();
      // si une liste d'objets à céder est passée en paramètre
      if(derivesACeder != null){
         boolean nonStocke = false;
         for(int i = derivesACeder.size() - 1; i >= 0; i--){
            // on vérifie que l'élément renvoyé est bien un dérivé
            if(derivesACeder.get(i).getClass().getSimpleName().equals("ProdDerive")){
               final ProdDerive derive = derivesACeder.get(i);
               // si le derive est stocké, on le cède
               if(derive.getObjetStatut().getStatut().equals("STOCKE")){
                  cederProdDerive(derive);
               }else{
                  nonStocke = true;
               }
            }
         }
         if(nonStocke){
            prodDerivesNonStockesLabel.setVisible(true);
         }else{
            prodDerivesNonStockesLabel.setVisible(false);
         }
      }

      initDerivesModel(0);
   }

   /**
    * Méthode permettant de créer un CederObjet à partir d'un dérivé.
    *
    * @param deriveACeder ProdDerive que l'on souhaite céder.
    */
   public void cederProdDerive(final ProdDerive deriveACeder){

      if(deriveACeder != null){
         // création d'un nouveau CederObjet
         final CederObjet obj = new CederObjet();
         obj.setCession(this.cession);
         obj.setEntite(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
         obj.setObjetId(deriveACeder.getProdDeriveId());

         // Init de la quanité et du volume
         if(deriveACeder.getQuantite() != null){
            obj.setQuantite((float) 0.0);
            obj.setQuantiteUnite(deriveACeder.getQuantiteUnite());
         }else if(deriveACeder.getVolume() != null){
            obj.setQuantite((float) 0.0);
            obj.setQuantiteUnite(deriveACeder.getVolumeUnite());
         }else{
            obj.setQuantite(null);
         }

         // on vérifie que le dérivé n'a pas déja été cédé
         // dans cette cession
         if(!derivesCedes.contains(obj)){
            final CederObjetDecorator decorated = new CederObjetDecorator(obj, true, isAnonyme(), false);

            // Maj de la liste
            derivesCedes.add(0, obj);
            derivesCedesDecores.add(0, decorated);
            // ListModel<CederObjetDecorator> list =
            //	new ListModelList<CederObjetDecorator> (derivesCedesDecores);
            // derivesListEdit.setModel(list);

            // header de la liste des dérivés cédés
            final StringBuffer sb = new StringBuffer();
            sb.append(Labels.getLabel("cession.prodDerive"));
            sb.append(" (");
            sb.append(derivesCedes.size());
            sb.append(")");
            derivesGroupHeader = sb.toString();
            groupProdDerives.setLabel(derivesGroupHeader);

            getBinder().loadComponent(derivesList);
         }

         //			if (!newlyCeds.contains(deriveACeder)) {
         //				newlyCeds.add(deriveACeder);
         //			}

      }else{
         throw new WrongValueException(derivesBox, Labels.getLabel("cession.error.code.invalid.prodDerive"));
      }
   }

   /**
    * Méthode appelée une fois que l'utilisateur a remplie une quantité
    * à céder pour un échantillon. La quantité restante sera alors mise
    * à jour.
    *
    * @param event Blur sur le champ echantillonQuantiteBox.
    */
   public void onBlur$echantillonQuantiteBox(final Event event){
      // on récupère le decorator modifié
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      // si la quantité demandée inférieure ou égale à celle disponile
      if(deco.getCederObjet().getQuantite() != null && deco.getCederObjet().getQuantite() <= deco.getQuantiteMax()){
         final Float res = deco.getQuantiteMax() - deco.getCederObjet().getQuantite();
         // arrondi pour éviter les valeurs 20.00012
         deco.setQuantiteRestante(ObjectTypesFormatters.floor(res, 3));
      }
   }

   /**
    * Méthode appelée une fois que l'utilisateur a rempli une quantité
    * à céder pour un dérivé. La quantité restante sera alors mise
    * à jour.
    *
    * @param event Blur sur le champ deriveQuantiteBox.
    */
   public void onBlur$deriveQuantiteBox(final Event event){
      // on récupère le decorator modifié
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      // si la quantité demandée est inférieure ou égale à celle disponile
      if(deco.getCederObjet().getQuantite() != null && deco.getCederObjet().getQuantite() <= deco.getQuantiteMax()){
         final Float res = deco.getQuantiteMax() - deco.getCederObjet().getQuantite();
         // arrondi pour éviter les valeurs 20.00012
         deco.setQuantiteRestante(ObjectTypesFormatters.floor(res, 3));
      }
   }

   public void onSelect$deriveUnitesBox(final Event event){
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      deco.setSelectedUnite(deco.getUnites().get(((Listbox) ((ForwardEvent) event).getOrigin().getTarget()).getSelectedIndex()));

      deco.switchQuantiteAndVolume(deco.getSelectedUnite());
   }

   /**
    * Méthode appelée lors du clic sur le bouton deleteEchan.
    * L'échantillon correspondant au bouton cliqué
    * sera alors supprimé à la liste des échantillons cédés.
    */
   public void onClick$deleteEchan(final Event event){

      // on demande confirmation à l'utilisateur
      // de supprimer l'échantillon
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.cession.retrait",
            new String[] {Labels.getLabel("message.deletion.echantillon")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

         // on récupère le decorator correspondant
         final CederObjetDecorator deco =
            (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
         final CederObjet obj = deco.getCederObjet();
         // si l'objet à céder se trouve dans la base
         if(this.cession.getCessionId() != null && ManagerLocator.getCederObjetManager().findByIdManager(obj.getPk()) != null){

            // maj de la liste
            echantillonsCedesDecores.remove(deco);
            echantillonsCedes.remove(obj);

            revertedObjs.add(deco);
         }else{
            // maj de la liste
            echantillonsCedesDecores.remove(deco);
            echantillonsCedes.remove(obj);
         }

         if(echantillonsCedesDecores.size() % 10 == 0 && _startPageNumberE > 0){
            _startPageNumberE--;
         }

         initEchansModel(_startPageNumberE);

         // header de la liste des echantillons cédés
         final StringBuffer sb = new StringBuffer();
         sb.append(Labels.getLabel("cession.echantillons"));
         sb.append(" (");
         sb.append(echantillonsCedes.size());
         sb.append(")");
         echantillonsGroupHeader = sb.toString();
         groupEchantillons.setLabel(echantillonsGroupHeader);

         //newlyCeds.remove(deco.getEchantillon());
      }

   }

   /**
    * Méthode appelée lors du clic sur le bouton deleteDerive.
    * Le dérivé correspondant au bouton cliqué
    * sera alors supprimé à la liste des dérivés cédés.
    */
   public void onClick$deleteDerive(final Event event){

      // on demande confirmation à l'utilisateur
      // de supprimer le dérivé
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.cession.retrait", new String[] {Labels.getLabel("message.deletion.derive")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

         // on récupère le decorator correspondant
         final CederObjetDecorator deco =
            (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
         final CederObjet obj = deco.getCederObjet();
         // si l'objet à céder se trouve dans la base
         if(this.cession.getCessionId() != null && ManagerLocator.getCederObjetManager().findByIdManager(obj.getPk()) != null){

            // maj de la liste
            derivesCedesDecores.remove(deco);
            derivesCedes.remove(obj);

            revertedObjs.add(deco);
         }else{
            // maj de la liste
            derivesCedesDecores.remove(deco);
            derivesCedes.remove(obj);
         }
         if(derivesCedesDecores.size() % 10 == 0 && _startPageNumberP > 0){
            _startPageNumberP--;
         }

         initDerivesModel(_startPageNumberP);

         // header de la liste des dérivés cédés
         final StringBuffer sb = new StringBuffer();
         sb.append(Labels.getLabel("cession.prodDerive"));
         sb.append(" (");
         sb.append(derivesCedes.size());
         sb.append(")");
         derivesGroupHeader = sb.toString();
         groupProdDerives.setLabel(derivesGroupHeader);

      }

   }

   /**
    * Mets a jour les objets cédés.
    *
    * @param calculerValeurs Si true, les valeurs de volume et de quantité
    *                        seront recalculées.
    */
   public void prepareCedesTKobjs(final boolean calculerValeurs){

      final List<Retour> incompletes = new ArrayList<>();
      final List<Integer> ids = new ArrayList<>();
      Entite e;

      final List<CederObjetDecorator> cedeDecos = new ArrayList<>();
      cedeDecos.addAll(echantillonsCedesDecores);
      cedeDecos.addAll(derivesCedesDecores);

      // pour chaque échantillon cédé
      // Float temperatureEpuisement = null;
      for(final CederObjetDecorator deco : cedeDecos){

         if(selectedCessionType.getType().equals("Traitement")){
            if(null == deco.getCederObjet().getPk() || null == deco.getCederObjet().getStatut()){
               deco.getCederObjet().setStatut(ECederObjetStatut.TRAITEMENT);
            }
         }

         final TKStockableObject tkObj = deco.getTKobj();

         // si les valeurs ont pu changées ou que la cession a été
         // refusée => maj quantité et volume
         if(calculerValeurs || this.cession.getCessionStatut().getStatut().equals("REFUSEE")){
            // si la cession est refusée => quantité et volume au MAX
            if(this.cession.getCessionStatut().getStatut().equals("REFUSEE")){
               revertedObjs.add(deco);

            }else{
               if(deco.getSelectedUnite() == null || !deco.getSelectedUnite().getType().equals("volume")){
                  tkObj.setQuantite(deco.getQuantiteRestante());
               }else{
                  if(tkObj instanceof ProdDerive){
                     ((ProdDerive) tkObj).setVolume(deco.getQuantiteRestante());
                  }else{
                     tkObj.setQuantite(deco.getQuantiteRestante());
                  }
               }
            }

            if(tkObj instanceof ProdDerive){
               if(deco.getSelectedUnite() != null && deco.getSelectedUnite().getType().equals("volume")){
                  ((ProdDerive) tkObj)
                     .setQuantite(calculerQuantiteRestante(((ProdDerive) tkObj), ((ProdDerive) tkObj).getVolume()));
               }else{
                  ((ProdDerive) tkObj).setVolume(calculerVolumeRestant(((ProdDerive) tkObj), ((ProdDerive) tkObj).getQuantite()));
               }
            }
         }

         // maj du statut
         deco.setNewStatut(findStatutForTKStockableObject(tkObj));

         // recuperation eventuel retour incomplet
         e = ManagerLocator.getEntiteManager().findByNomManager(tkObj.entiteNom()).get(0);
         incompletes.clear();
         ids.clear();
         ids.add(tkObj.listableObjectId());
         incompletes.addAll(ManagerLocator.getRetourManager().findByObjectDateRetourEmptyManager(ids, e));
         if(!incompletes.isEmpty()){
            // il ne doit y en avoir qu'un
            deco.setRetour(incompletes.get(0));
         }

         if(deco.getNewStatut().getStatut().equals("EPUISE") || deco.getNewStatut().getStatut().equals("RESERVE")){
            // creation d'un retour automatique si aucun retour à
            // compléter
            if(deco.getRetour() == null){
               final Retour epuisement = new Retour();
               Calendar dateS = Calendar.getInstance();
               if(cession.getDepartDate() != null){
                  dateS = cession.getDepartDate();
               }else if(cession.getValidationDate() != null){
                  dateS.setTime(cession.getValidationDate());
               }
               epuisement.setDateSortie(dateS);

               //               //Pas super propre mais permet de ne saisir la température qu'une seule fois par cession
               //               if(temperatureEpuisement == null){
               //                  final Retour tmp = new Retour();
               //                  final Float temperatureCession =
               //                     temperatureBox.getValue() != null ? temperatureBox.getValue().floatValue() : 20f;
               //                  Clients.clearBusy();
               //                  ValeurDecimaleModale.show(Labels.getLabel("listeRetour.title"), Labels.getLabel("Champ.Retour.TempMoyenne"),
               //                     temperatureCession, false, evt -> tmp.setTempMoyenne((Float) evt.getData()));
               //                  Clients.showBusy(Labels.getLabel(getWaitLabel()));
               //                  temperatureEpuisement = tmp.getTempMoyenne();
               //               }
               //
               //               epuisement.setTempMoyenne(temperatureEpuisement);
               epuisement.setObservations(Labels.getLabel("ficheRetour.observations.cession") + " "
                  + Labels.getLabel("ficheRetour.observations.epuisement"));
               epuisement.setEntite(e);
               epuisement.setObjetId(tkObj.listableObjectId());
               final Errors errs = ManagerLocator.getRetourValidator().checkDateSortieCoherence(epuisement);
               if(errs.hasErrors()){
                  final List<Errors> errsList = new ArrayList<>();
                  errsList.add(errs);
                  throw new ValidationException(errsList);
               }
               deco.setRetour(epuisement);
            }
         }
      }
   }

   public void getRetourIncompleteFromModale(final List<CederObjetDecorator> decos){

      if(!cession.getCessionStatut().getStatut().equals("EN ATTENTE")){

         final Map<String, String> echansRetourIncompletes = new HashMap<>();
         final Map<String, String> derivesRetourIncompletes = new HashMap<>();
         // seuls sont concernés les tkObjs en cession partielle
         // avec un retour précédemment créé par une cession
         // mise EN ATTENTE
         for(final CederObjetDecorator deco : decos){
            if(deco.getRetour() != null && deco.getRetour().getRetourId() != null && deco.getTKobj().getObjetStatut() != null
               && !deco.getTKobj().getObjetStatut().getStatut().equals("RESERVE")){
               if(deco.getEchantillon() != null){
                  echansRetourIncompletes.put(deco.getEchantillonCode(),
                     ObjectTypesFormatters.dateRenderer2(deco.getRetour().getDateSortie()));
               }else{
                  derivesRetourIncompletes.put(deco.getProdDeriveCode(),
                     ObjectTypesFormatters.dateRenderer2(deco.getRetour().getDateSortie()));
               }
            }
         }

         if(!echansRetourIncompletes.isEmpty() || !derivesRetourIncompletes.isEmpty()){

            final Calendar dateInit = cession.getDepartDate() != null ? cession.getDepartDate() : Calendar.getInstance();

            Clients.clearBusy();
            openDateRetourCompleteModale(echansRetourIncompletes, derivesRetourIncompletes, dateInit, this, self);

            Clients.showBusy(Labels.getLabel(getWaitLabel()));
         }
      }
   }

   public void updateCedesTkObjs(){

      final ObjetStatut nonstocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0);

      final List<CederObjetDecorator> cedeDecos = new ArrayList<>();
      cedeDecos.addAll(echantillonsCedesDecores);
      cedeDecos.addAll(derivesCedesDecores);

      getRetourIncompleteFromModale(cedeDecos);

      // cancel requested from modale
      if(cancelException != null){
         throw cancelException;
      }

      Float temperatureEpuisement = null;

      // pour chaque échantillon cédé
      for(final CederObjetDecorator deco : cedeDecos){

         final TKStockableObject tkObj = deco.getTKobj();

         List<OperationType> ops = new ArrayList<>();
         // ObjetStatut oldStatut = tkObj.getObjetStatut();

         // maj du statut après création de la cession
         // gestion des cessions EN ATTENTE statut RESERVE
         deco.setNewStatut(findStatutForTKStockableObject(tkObj));

         if(deco.getNewStatut().getStatut().equals("EPUISE")){
            Emplacement empl;
            if(tkObj instanceof Echantillon){
               empl = ManagerLocator.getEchantillonManager().getEmplacementManager((Echantillon) tkObj);
            }else{
               empl = ManagerLocator.getProdDeriveManager().getEmplacementManager((ProdDerive) tkObj);
            }
            deco.setOldEmplacement(empl);
            empl.setEntite(null);
            empl.setObjetId(null);
            empl.setVide(true);
            tkObj.setEmplacement(null);

            ManagerLocator.getEmplacementManager().updateObjectManager(empl, empl.getTerminale(), null);

            // fork du statut EPUISE pour DETRUIT si cession Destruction
            if(cession.getCessionType().getCessionTypeId() == 3){
               deco.setNewStatut(ManagerLocator.getObjetStatutManager().findByStatutLikeManager("DETRUIT", true).get(0));
            }

            if(deco.getNewStatut().getStatut().equals("EPUISE") || deco.getNewStatut().getStatut().equals("DETRUIT")){
               // passage temporaire au statut NON STOCKE
               // afin que ce statut soit enregistré dans le retour correspondant
               // à l'épuisement de l'échantillon
               tkObj.setObjetStatut(nonstocke);

               deco.getRetour().setObjetStatut(deco.getNewStatut());

               //Pas super propre mais permet de ne saisir la température qu'une seule fois par cession
               if(temperatureEpuisement == null){
                  final Retour tmp = new Retour();
                  final Float temperatureCession =
                     temperatureBox.getValue() != null ? temperatureBox.getValue().floatValue() : 20f;
                  Clients.clearBusy();
                  ValeurDecimaleModale.show(Labels.getLabel("listeRetour.title"), Labels.getLabel("Champ.Retour.TempMoyenne"),
                     temperatureCession, false, evt -> tmp.setTempMoyenne((Float) evt.getData()));
                  Clients.showBusy(Labels.getLabel(getWaitLabel()));
                  temperatureEpuisement = tmp.getTempMoyenne();
               }

               deco.getRetour().setTempMoyenne(temperatureEpuisement);

               ManagerLocator.getRetourManager().createOrUpdateObjectManager(deco.getRetour(), tkObj, deco.getOldEmplacement(),
                  getSelectedExecutant(), getCession(), null, null, SessionUtils.getLoggedUser(sessionScope), "creation");

            }
         }else if(!cession.getCessionStatut().getStatut().equals("EN ATTENTE") && deco.getRetour() != null
            && deco.getRetour().getRetourId() != null){

            if(deco.getEchantillon() != null){
               deco.getRetour().setDateRetour(dateRetourEchansComplete);
            }else if(deco.getProdDerive() != null){
               deco.getRetour().setDateRetour(dateRetourDerivesComplete);
            }

            deco.setNewStatut(deco.getRetour().getObjetStatut());

            ManagerLocator.getRetourManager().createOrUpdateObjectManager(deco.getRetour(), tkObj, deco.getOldEmplacement(),
               getSelectedExecutant(), getCession(), null, null, SessionUtils.getLoggedUser(sessionScope), "creation");
         }

         if(deco.getNewStatut() != null
            && (deco.getNewStatut().getStatut().equals("EPUISE") || deco.getNewStatut().getStatut().equals("DETRUIT"))){
            ops = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Destockage", true);
         }

         String oPanel = null;
         String oWin = null;
         AbstractObjectTabController controller = null;
         // Update objet
         if(tkObj instanceof Echantillon){
            final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager((Echantillon) tkObj);
            ManagerLocator.getEchantillonManager().updateObjectManager((Echantillon) tkObj, ((Echantillon) tkObj).getBanque(),
               prlvt, ((Echantillon) tkObj).getCollaborateur(), deco.getNewStatut(), tkObj.getEmplacement(),
               ((Echantillon) tkObj).getEchantillonType(), null, null, tkObj.getQuantiteUnite(),
               ((Echantillon) tkObj).getEchanQualite(), ((Echantillon) tkObj).getModePrepa(),
               // ((Echantillon) tkObj).getCrAnapath(), null,
               null, null, null, null, SessionUtils.getLoggedUser(sessionScope), false, ops, null);

            oPanel = "echantillonPanel";
            oWin = "winEchantillon";
            controller = getEchantillonController();

         }else{
            ManagerLocator.getProdDeriveManager().updateObjectManager(((ProdDerive) tkObj), tkObj.getBanque(),
               ((ProdDerive) tkObj).getProdType(), deco.getNewStatut(), ((ProdDerive) tkObj).getCollaborateur(),
               tkObj.getEmplacement(), ((ProdDerive) tkObj).getVolumeUnite(), ((ProdDerive) tkObj).getConcUnite(),
               ((ProdDerive) tkObj).getQuantiteUnite(), ((ProdDerive) tkObj).getModePrepaDerive(),
               ((ProdDerive) tkObj).getProdQualite(), ((ProdDerive) tkObj).getTransformation(), null, null, null, null,
               SessionUtils.getLoggedUser(sessionScope), false, ops, null);

            oPanel = "derivePanel";
            oWin = "winProdDerive";
            controller = getProdDeriveController();
         }

         if(!newlyCeds.contains(tkObj) && !tkObj.getObjetStatut().getStatut().equals("EPUISE")
            && !tkObj.getObjetStatut().getStatut().equals("RESERVE")
            && (deco.getRetour() == null || deco.getRetour().getRetourId() == null)){
            newlyCeds.add(tkObj);
            // oldEmpsForNewlyCeds.put(echanToUpdate, oldAdrl);
         }

         // on vérifie que l'on retrouve bien la page contenant la liste
         // des échantillons
         if(getMainWindow().isFullfilledComponent(oPanel, oWin)){
            if(controller != null && controller.getListe() != null){
               // update de l'échantillon dans la liste
               controller.getListe().updateObjectGridListFromOtherPage(tkObj, false);
            }
         }
      }
   }

   @Override
   protected void validateCoherenceDate(final Component comp, final Object value){
      Errors errs = null;
      String field = "";

      if(value == null){
         // la contrainte est retiree
         //((Datebox) comp).setConstraint("");
         if(comp.getId().equals("dateDemandeBox")){
            this.cession.setDemandeDate(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }else if(comp.getId().equals("dateValidationBox")){
            this.cession.setValidationDate(null);
            ((Datebox) comp).clearErrorMessage(true);
            ((Datebox) comp).setValue(null);
         }else if(comp.getId().equals("dateDestructionCalBox")){
            this.cession.setDestructionDate(null);
            ((CalendarBox) comp).clearErrorMessage(null);
            ((CalendarBox) comp).setValue(null);
         }else if(comp.getId().equals("dateDepartCalBox")){
            this.cession.setDepartDate(null);
            ((CalendarBox) comp).clearErrorMessage(null);
            ((CalendarBox) comp).setValue(null);
         }else if(comp.getId().equals("dateArriveeCalBox")){
            ((CalendarBox) comp).clearErrorMessage(null);
            ((CalendarBox) comp).setValue(null);
            this.cession.setArriveeDate(null);
         }

      }else{
         if(comp.getId().equals("dateDemandeBox")){
            field = "demandeDate";
            this.cession.setDemandeDate((Date) value);
            errs = CessionValidator.checkDemandeDateCoherence(cession);
         }else if(comp.getId().equals("dateValidationBox")){
            field = "validationDate";
            this.cession.setValidationDate((Date) value);
            errs = CessionValidator.checkValidationDateCoherence(cession);
         }else if(comp.getId().equals("dateDestructionCalBox")){
            field = "destructionDate";
            this.cession.setDestructionDate((Calendar) value);
            errs = CessionValidator.checkDestructionDateCoherence(cession);
         }else if(comp.getId().equals("dateDepartCalBox")){
            field = "departDate";
            this.cession.setDepartDate((Calendar) value);
            errs = CessionValidator.checkDepartDateCoherence(cession);
         }else if(comp.getId().equals("dateArriveeCalBox")){
            field = "arriveeDate";
            this.cession.setArriveeDate((Calendar) value);
            errs = CessionValidator.checkArriveeDateCoherence(cession);
         }

         // Si la date n'est pas vide, on applique la contrainte
         if(errs != null && errs.hasErrors()){
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   public void onBlur$dateDemandeBox(){
      boolean badDateFormat = false;
      if(dateDemandeBox.getErrorMessage() != null && dateDemandeBox.getErrorMessage().contains(dateDemandeBox.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateDemandeBox.clearErrorMessage(true);
         validateCoherenceDate(dateDemandeBox, dateDemandeBox.getValue());
      }
   }

   public void onBlur$dateValidationBox(){
      boolean badDateFormat = false;
      if(dateValidationBox.getErrorMessage() != null
         && dateValidationBox.getErrorMessage().contains(dateValidationBox.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateValidationBox.clearErrorMessage(true);
         validateCoherenceDate(dateValidationBox, dateValidationBox.getValue());
      }
   }

   public void onBlur$dateDepartCalBox(){
      final Datebox box = (Datebox) dateDepartCalBox.getFirstChild().getFirstChild();
      boolean badDateFormat = false;
      if(box.getErrorMessage() != null && box.getErrorMessage().contains(box.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateDepartCalBox.clearErrorMessage(dateDepartCalBox.getValue());
         validateCoherenceDate(dateDepartCalBox, dateDepartCalBox.getValue());
      }else{
         throw new WrongValueException(dateDepartCalBox, Labels.getLabel("validation.invalid.date"));
      }
   }

   public void onBlur$dateArriveeCalBox(){
      final Datebox box = (Datebox) dateArriveeCalBox.getFirstChild().getFirstChild();
      boolean badDateFormat = false;
      if(box.getErrorMessage() != null && box.getErrorMessage().contains(box.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateArriveeCalBox.clearErrorMessage(dateArriveeCalBox.getValue());
         validateCoherenceDate(dateArriveeCalBox, dateArriveeCalBox.getValue());
      }else{
         throw new WrongValueException(dateArriveeCalBox, Labels.getLabel("validation.invalid.date"));
      }
   }

   public void onBlur$dateDestructionCalBox(){
      final Datebox box = (Datebox) dateDestructionCalBox.getFirstChild().getFirstChild();
      boolean badDateFormat = false;
      if(box.getErrorMessage() != null && box.getErrorMessage().contains(box.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         dateDestructionCalBox.clearErrorMessage(dateDestructionCalBox.getValue());
         validateCoherenceDate(dateDestructionCalBox, dateDestructionCalBox.getValue());
      }else{
         throw new WrongValueException(dateDestructionCalBox, Labels.getLabel("validation.invalid.date"));
      }
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(numeroBox);
      Clients.clearWrongValue(dateDemandeBox);
      Clients.clearWrongValue(dateDestructionCalBox);
      Clients.clearWrongValue(descriptionBox);
      Clients.clearWrongValue(dateValidationBox);
      Clients.clearWrongValue(dateArriveeCalBox);
      Clients.clearWrongValue(dateDepartCalBox);
      Clients.clearWrongValue(temperatureBox);
      Clients.clearWrongValue(observationsBox);

      Clients.clearWrongValue(echantillonsBox);
      this.echantillonsBox.setValue("");
      Clients.clearWrongValue(derivesBox);
      this.derivesBox.setValue("");
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForCessions(){
      // si pas le droit d'accès aux dérivés, on cache le lien
      if(!getDroitsConsultation().get("ProdDerive")){
         codeProdDeriveCede.setSclass(null);
      }else{
         codeProdDeriveCede.setSclass("formLink");
      }
      // si pas le droit d'accès aux échantillons, on cache le lien
      if(!getDroitsConsultation().get("Echantillon")){
         codeEchantillonCede.setSclass(null);
      }else{
         codeEchantillonCede.setSclass("formLink");
      }

      if(!getDroitOnAction("Echantillon", "Modification")){
         cederEchantillonsButton.setDisabled(true);
      }

      if(!getDroitOnAction("ProdDerive", "Modification")){
         cederDerivesButton.setDisabled(true);
      }

      if(sessionScope.containsKey("Anonyme") && (Boolean) sessionScope.get("Anonyme")){
         isAnonyme = true;
      }else{
         isAnonyme = false;
      }
   }

   /**
    * Sélection d'un contrat.
    *
    * @param event Event : sélection dans la combobox contratsComboBox.
    * @throws Exception
    */
   public void onSelect$contratsComboBox(final Event event) throws Exception{
      if(contratsComboBox.getSelectedItem() != null){
         final String value = contratsComboBox.getSelectedItem().getLabel();

         if(contratsNoms.contains(value)){
            selectedContrat = contrats.get(contratsNoms.indexOf(value));
         }else{
            selectedContrat = null;
         }
      }
   }

   public void onBlur$numeroBox(){

      final String numCession = numeroBox.getValue();

      final List<Cession> doublons = ManagerLocator.getManager(CessionManager.class).findByNumeroInPlateformeManager(numCession,
         SessionUtils.getCurrentPlateforme());

      if(!doublons.isEmpty()){
         final String banques =
            doublons.stream().map(Cession::getBanque).distinct().map(Banque::getNom).collect(Collectors.joining(", "));
         throw new WrongValueException(numeroBox,
            Labels.getLabel("cession.doublon.error.num", new String[] {numCession, banques}));
      }

   }

   /**
    * Mise à jour de la valeur sélectionné d'un contrat.
    *
    * @param event Event : sélection dans la combobox contratsComboBox.
    * @throws Exception
    */
   public void onBlur$contratsComboBox(final Event event) throws Exception{
      if(contratsComboBox.getSelectedItem() != null){
         final String value = contratsComboBox.getSelectedItem().getLabel();

         if(contratsNoms.contains(value)){
            selectedContrat = contrats.get(contratsNoms.indexOf(value));
         }else{
            selectedContrat = null;
         }
      }else if(contratsComboBox.getValue() != null){
         if(contratsNoms.contains(contratsComboBox.getValue().toUpperCase())){
            selectedContrat = contrats.get(contratsNoms.indexOf(contratsComboBox.getValue().toUpperCase()));
         }else{
            selectedContrat = null;
         }
      }else{
         selectedContrat = null;
      }
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public Cession getCession(){
      return cession;
   }

   public void setCession(final Cession c){
      setObject(c);
   }

   public boolean getEchantillonsListSizeSupOne(){
      if(getDroitsConsultation().containsKey("Echantillon") && getDroitsConsultation().get("Echantillon")
         && this.cession.getCessionStatut() != null && !this.cession.getCessionStatut().getStatut().equals("EN ATTENTE")){
         return this.echantillonsCedes.size() > 1;
      }
      return false;
   }

   public boolean getProdDerivesListSizeSupOne(){
      if(getDroitsConsultation().containsKey("ProdDerive") && getDroitsConsultation().get("ProdDerive")
         && this.cession.getCessionStatut() != null && this.cession.getCessionStatut().getStatut().equals("EN ATTENTE")){
         return this.derivesCedes.size() > 1;
      }
      return false;
   }

   public List<CederObjet> getEchantillonsCedes(){
      return echantillonsCedes;
   }

   public List<CederObjet> getDerivesCedes(){
      return derivesCedes;
   }

   public List<CederObjetDecorator> getEchantillonsCedesDecores(){
      return echantillonsCedesDecores;
   }

   public List<CederObjetDecorator> getDerivesCedesDecores(){
      return derivesCedesDecores;
   }

   /**
    * Formate la date de demande de la cession.
    *
    * @return Date de demande formatée.
    */
   public String getDateDemandeFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getDemandeDate());
      }
      return null;
   }

   public String getEchantillonsGroupHeader(){
      return echantillonsGroupHeader;
   }

   public String getDerivesGroupHeader(){
      return derivesGroupHeader;
   }

   public List<CessionType> getTypes(){
      return types;
   }

   public CessionType getSelectedCessionType(){
      return selectedCessionType;
   }

   public void setSelectedCessionType(final CessionType selected){
      this.selectedCessionType = selected;
   }

   public List<CessionExamen> getExamens(){
      return examens;
   }

   public void setExamens(final List<CessionExamen> exams){
      this.examens = exams;
   }

   public CessionExamen getSelectedCessionExamen(){
      return selectedCessionExamen;
   }

   public void setSelectedCessionExamen(final CessionExamen selected){
      this.selectedCessionExamen = selected;
   }

   public List<Contrat> getContrats(){
      return contrats;
   }

   public Contrat getSelectedContrat(){
      return selectedContrat;
   }

   public void setSelectedContrat(final Contrat selected){
      this.selectedContrat = selected;
   }

   public List<CessionStatut> getStatuts(){
      return statuts;
   }

   public CessionStatut getSelectedCessionStatut(){
      return selectedCessionStatut;
   }

   public void setSelectedCessionStatut(final CessionStatut selected){
      this.selectedCessionStatut = selected;
   }

   public List<Transporteur> getTransporteurs(){
      return transporteurs;
   }

   public Transporteur getSelectedTransporteur(){
      return selectedTransporteur;
   }

   public void setSelectedTransporteur(final Transporteur selected){
      this.selectedTransporteur = selected;
   }

   public List<DestructionMotif> getMotifs(){
      return motifs;
   }

   public DestructionMotif getSelectedDestructionMotif(){
      return selectedDestructionMotif;
   }

   public void setSelectedDestructionMotif(final DestructionMotif selected){
      this.selectedDestructionMotif = selected;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public Collaborateur getSelectedDemandeur(){
      return selectedDemandeur;
   }

   public void setSelectedDemandeur(final Collaborateur selected){
      this.selectedDemandeur = selected;
   }

   public Collaborateur getSelectedDestinataire(){
      return selectedDestinataire;
   }

   public void setSelectedDestinataire(final Collaborateur selected){
      this.selectedDestinataire = selected;
   }

   public Collaborateur getSelectedExecutant(){
      return selectedExecutant;
   }

   public void setSelectedExecutant(final Collaborateur selected){
      this.selectedExecutant = selected;
   }

   public List<String> getNomsAndPrenoms(){
      return nomsAndPrenoms;
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

   public List<String> getNomsService(){
      return nomsService;
   }

   public void setNomsService(final List<String> nomsS){
      this.nomsService = nomsS;
   }

   public List<String> getCodesEchans(){
      return codesEchans;
   }

   public List<String> getCodesDerives(){
      return codesDerives;
   }

   /**
    * Formate la date de validation de la cession.
    *
    * @return Date de demande formatée.
    */
   public String getDateValidationFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getValidationDate());
      }
      return null;
   }

   /**
    * Formate la date de demande du Contrat.
    *
    * @return Date de demande formatée.
    */
   public String getDateDemandeContratFormated(){
      if(this.selectedContrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.selectedContrat.getDateDemandeCession());
      }
      return null;
   }

   /**
    * Formate la date de signature du Contrat.
    *
    * @return Date de demande formatée.
    */
   public String getDateSignatureContratFormated(){
      if(this.selectedContrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.selectedContrat.getDateSignature());
      }
      return null;
   }

   /**
    * Formate la date de validation de la cession.
    *
    * @return Date de demande formatée.
    */
   public String getDateValidationContratFormated(){
      if(this.selectedContrat != null){
         return ObjectTypesFormatters.dateRenderer2(this.selectedContrat.getDateValidation());
      }
      return null;
   }

   /**
    * Contrainte vérifiant que la quantite restante n'est pas inférieure
    * à la quantite demandée.
    *
    * @author Pierre Ventadour.
    */
   public class ConstQuantiteDemandee implements Constraint
   {
      /**
       * Méthode de validation du champ echantillonQuantiteBox.
       */
      @Override
      public void validate(final Component comp, final Object value){
         // on récupère la valeur
         final BigDecimal quantiteValue = (BigDecimal) value;

         final CederObjetDecorator deco = (CederObjetDecorator) getBindingData(comp);

         if(quantiteValue != null){
            final Float qteDemandee = quantiteValue.floatValue();
            final float zero = (float) 0.0;

            // la quantité demandée doit etre positive
            if(qteDemandee >= zero){

               // la qté demandée doit être inférieure à la quantité
               // restante
               if(qteDemandee > deco.getQuantiteMax()){
                  final StringBuilder sb = new StringBuilder();
                  sb.append(Labels.getLabel("cession.error.ceder.quantite.invalid"));
                  sb.append(" ");
                  sb.append(deco.getQuantiteMax());
                  if(deco.getCederObjet().getQuantiteUnite() != null){
                     sb.append(deco.getCederObjet().getQuantiteUnite().getNom());
                  }

                  throw new WrongValueException(comp, sb.toString());
               }
            }else{
               throw new WrongValueException(comp, Labels.getLabel("cession.error.ceder.quantite.negative"));
            }
         }else{
            if(deco.getTKobj().getQuantite() != null
               || (deco.getTKobj() instanceof ProdDerive && deco.getProdDerive().getVolume() != null)){
               if((deco.getTKobj() instanceof Echantillon && echanQteColBox.isVisible())
                  || (deco.getTKobj() instanceof ProdDerive && deriveQteColBox.isVisible())){ // decimalbox is not visible TK-223 fix
                  throw new WrongValueException(comp, Labels.getLabel("cession.error.ceder.quantite.obligatoire"));
               }
            }
         }
      }
   }

   private Constraint cttQuantiteDemandee = new ConstQuantiteDemandee();

   public Constraint getCttQuantiteDemandee(){
      return cttQuantiteDemandee;
   }

   public void setCttQuantiteDemandee(final Constraint ctt){
      this.cttQuantiteDemandee = ctt;
   }

   public String getSClassDemandeur(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.cession.getDemandeur());
      }
      return "";
   }

   public String getSClassDestinataire(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.cession.getDestinataire());
      }
      return "";
   }

   public String getSClassExecutant(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.cession.getExecutant());
      }
      return "";
   }

   public String getSClassServiceDest(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassService(this.cession.getServiceDest());
      }
      return "";
   }

   public String getSClassCollaborateurHelp(){
      if(this.selectedContrat != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.selectedContrat.getCollaborateur());
      }
      return "";
   }

   public void setVisibleRow(final Row row, final boolean visible){
      for(int i = 0; i < row.getChildren().size(); i++){
         final Component comp = row.getChildren().get(i);
         comp.setVisible(visible);
      }
      row.setVisible(visible);
   }

   public Terminale getTerminaleDestockage(){
      return terminaleDestockage;
   }

   public void setTerminaleDestockage(final Terminale terminale){
      this.terminaleDestockage = terminale;
   }

   public Constraint getNumeroConstraint(){
      return CessionConstraints.getNumeroCessionConstraint();
   }

   public ConstWord getTitreEtudeConstraint(){
      return CessionConstraints.getTitreEtudeConstraint();
   }

   public ConstText getCessionDescrConstraint(){
      return CessionConstraints.getCessionDescrConstraint();
   }

   public ConstText getDescrConstraint(){
      return CessionConstraints.getDescrConstraint();
   }

   public List<Banque> getAvailableBanques(){
      return availableBanques;
   }

   public void setAvailableBanques(final List<Banque> aBanques){
      this.availableBanques = aBanques;
   }

   public List<Temperature> getTemperatures(){
      return temperatures;
   }

   public void setTemperatures(final List<Temperature> t){
      this.temperatures = t;
   }

   public Temperature getSelectedTemperature(){
      return selectedTemperature;
   }

   public void setSelectedTemperature(final Temperature selected){
      this.selectedTemperature = selected;
   }

   public List<Collaborateur> getDestinataires(){
      return destinataires;
   }

   public void setDestinataires(final List<Collaborateur> d){
      this.destinataires = d;
   }

   public List<Service> getServicesDest(){
      return servicesDest;
   }

   public void setServicesDest(final List<Service> s){
      this.servicesDest = s;
   }

   public List<Etablissement> getEtablissements(){
      return etablissements;
   }

   public void setEtablissements(final List<Etablissement> e){
      this.etablissements = e;
   }

   public Etablissement getSelectedEtablissement(){
      return selectedEtablissement;
   }

   public void setSelectedEtablissement(final Etablissement selected){
      this.selectedEtablissement = selected;
   }

   public List<String> getContratsNoms(){
      return contratsNoms;
   }

   public void setContratsNoms(final List<String> contratsNs){
      this.contratsNoms = contratsNs;
   }

   @Override
   public boolean isAnonyme(){
      return isAnonyme;
   }

   @Override
   public void setAnonyme(final boolean isA){
      this.isAnonyme = isA;
   }

   public String getCessionStatut(){
      return ObjectTypesFormatters.ILNObjectStatut(getObject().getCessionStatut());
   }

   private void refreshModelE(final int activePage){
      echansPaging.setPageSize(_pageSizeE);
      echansModel = new ObjectPagingModel(activePage, _pageSizeE, echantillonsCedesDecores, null, null);

      if(_needsTotalSizeUpdateE){
         _totalSizeE = echansModel.getTotalSize();
         _needsTotalSizeUpdateE = false;
      }

      echansPaging.setTotalSize(_totalSizeE);
      echantillonsList.setModel(echansModel);

      final int start = _startPageNumberE * _pageSizeE;
      int end = start + _pageSizeE;
      if(end >= echantillonsCedesDecores.size()){
         end = echantillonsCedesDecores.size();
      }

      showLastEchantillons(start, end, _startPageNumberE * _pageSizeE);
   }

   public void onPaging$echansPaging(final ForwardEvent event){
      final PagingEvent pe = (PagingEvent) event.getOrigin();
      _startPageNumberE = pe.getActivePage();
      refreshModelE(_startPageNumberE);
   }

   private void refreshModelP(final int activePage){
      derivesPaging.setPageSize(_pageSizeP);
      derivesModel = new ObjectPagingModel(activePage, _pageSizeP, derivesCedesDecores, null, null);

      if(_needsTotalSizeUpdateP){
         _totalSizeP = derivesModel.getTotalSize();
         _needsTotalSizeUpdateP = false;
      }

      derivesPaging.setTotalSize(_totalSizeP);
      derivesList.setModel(derivesModel);
   }

   public void onPaging$derivesPaging(final ForwardEvent event){
      final PagingEvent pe = (PagingEvent) event.getOrigin();
      _startPageNumberP = pe.getActivePage();
      refreshModelP(_startPageNumberP);
   }

   public List<Retour> getRetoursToComplete(){
      return retoursToComplete;
   }

   public void setRetoursToComplete(final List<Retour> rc){
      this.retoursToComplete = rc;
   }

   public void setDateRetourEchansComplete(final Calendar dec){
      this.dateRetourEchansComplete = dec;
   }

   public void setDateRetourDerivesComplete(final Calendar drc){
      this.dateRetourDerivesComplete = drc;
   }

   /**
    * Exception obtenue à partir de la modale qui demande
    * l'annulation de la modification de la cession
    *
    * @param ce Exception
    */
   public void setCancelException(final CancelFromModalException ce){
      cancelException = ce;
   }

   /**
    * Ajout objets à céder depuis fichier Excel
    * Excel contenant une liste de codes échantillons (codes).
    */
   public void onClick$validateCessionByEchansListUpload(){
      // récupère les patients des derives présents dans le
      // fichier excel que l'utilisateur va uploader
      // List<String> codes = getObjectTabController().getListe().getListStringToSearch();
      // List<Integer> derives = ManagerLocator.getProdDeriveManager()
      //	.findByPatientNomOrNipInListManager(pats,
      //			SessionUtils.getSelectedBanques(sessionScope));
      // affichage de ces résultats
      // showResultsAfterSearchByList(derives);
   }

   /**
    * En fonction du type de cession à afficher, certains champs sont masqués
    *
    * @param switchCondition type de cession : SANITAIRE, RECHERCHE, DESTRUCTION ou IMPLANTATION
    * @since 2.2.0
    */
   public void switchToCessionTypeToDisplay(final String switchCondition){
      switch(switchCondition){
         case "SANITAIRE":
            switchToSanitaireMode();
            this.demandeurHelpLabel.setVisible(false);
            this.descriptionHelpLabel.setVisible(false);
            this.dateValidationHelpLabel.setVisible(false);
            this.titreEtudeHelpLabel.setVisible(false);
            break;
         case "RECHERCHE":
            switchToRechercheMode();
            break;
         case "DESTRUCTION":
            switchToDestructionMode();
            this.demandeurHelpLabel.setVisible(false);
            this.descriptionHelpLabel.setVisible(false);
            this.dateValidationHelpLabel.setVisible(false);
            this.titreEtudeHelpLabel.setVisible(false);
            break;
         default:
            break;
      }
   }

   /**
    * Affiche / cache la liste de choix des collections
    * pour la sélection d'échantillons
    * @since 2.2.3-rc1
    */
   public void onClick$cederMultiEchantillonBanquesLabel(){
      cederMultiEchantillonBanquesLabel.getParent().getNextSibling()
         .setVisible(!cederMultiEchantillonBanquesLabel.getParent().getNextSibling().isVisible());
   }

   /**
    * Affiche / cache la liste de choix des collections
    * pour la sélection de dérivés
    * @since 2.2.3-rc1
    */
   public void onClick$cederMultiProdDeriveBanquesLabel(){
      cederMultiProdDeriveBanquesLabel.getParent().getNextSibling()
         .setVisible(!cederMultiProdDeriveBanquesLabel.getParent().getNextSibling().isVisible());
   }

   /***** Recepteur interfacage stockage automatisé -> portoir de transfert *******/

   /**
    * @since 2.2.1-IRELEC
    */
   public void proposeStorageRobotItem(){
      // set storageRobotItem visible ssi IRELEC
      // && cession validée
      boolean isStorageRobotEnabled = false;
      for(final Recepteur recept : SessionUtils.getRecepteursInterfacages(sessionScope)){
         if(recept.getLogiciel().getNom().equals("IRELEC")){
            isStorageRobotEnabled = true;
            break;
         }
      }

      if(isStorageRobotEnabled && getObject() != null && getObject().getCessionStatut() != null
         && getObject().getCessionStatut().getCessionStatutId().equals(2)){
         final List<Integer> echanIds = new ArrayList<>();
         for(final CederObjet cObj : echantillonsCedes){
            echanIds.add(cObj.getObjetId());
         }
         postStorageData(ManagerLocator.getEchantillonManager().findByIdsInListManager(echanIds), true);
      }
   }

   public ListModel<Banque> getAvailableBanquesForEchantillonModel(){
      return availableBanquesForEchantillonModel;
   }

   public ListModel<Banque> getAvailableBanquesForDeriveModel(){
      return availableBanquesForDeriveModel;
   }
}
