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
package fr.aphp.tumorotek.action.contexte;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.BindingListModelSet;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.AdministrationController;
import fr.aphp.tumorotek.action.code.CodeUtils;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.contexte.catalogue.CataloguesRowRenderer;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.utilisateur.ProfilUtilisateurRowRenderer;
import fr.aphp.tumorotek.decorator.CouleurItemRenderer;
import fr.aphp.tumorotek.decorator.I3listBoxItemRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.ServiceWithEtablissementRenderer;
import fr.aphp.tumorotek.manager.impl.xml.CoupleSimpleValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneDeuxColonnesParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneSimpleParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.manager.utilisateur.ProfilManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Modifiée le 31/01/2013 pour corriger bug de non enregistrement des
 * modifications de l'ordre des tables annotations et ajout des tables
 * de cessions.
 *
 * Modifiée le 12/04/2016 pour affichier les comptes utilisateurs actifs, switch inactifs et lien
 * vers la fiche détaille du profil.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class FicheBanque extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(FicheBanque.class);

   private static final long serialVersionUID = 6300875937416491348L;

   // Labels.
   private Label nomLabel;

   private Label idLabel;

   private Label descrLabel;

   private Label proprioLabel;

   private Label respLabel;

   private Label contactLabel;

   private Label defMaladieLabel;

   private Label defautLibLabel;

   private Label defautCodeLabel;

   private Label autoCrossLabel;

   private Label contexteLabel;

   private Label etudeLabel;

   private Label couleurEchanLabel;

   private Label couleurDeriveLabel;

   private Menubar menuBar;

   // since 2.1
   private Label archiveLabel;

   // Editable components : mode d'édition ou de création.
   private Label nomRequired;

   private Label contexteRequired;

   private Textbox nomBox;

   private Textbox idBox;

   private Textbox descrBox;

   private Combobox proprioBox;

   private Div respBoxDiv;

   private Combobox collabBox;

   private Combobox contactBox;

   private Div contactBoxDiv;

   private Checkbox defMaladieBox;

   private Textbox defautLibBox;

   private Textbox defautCodeBox;

   private Checkbox autoCrossBox;

   private Listbox contexteBox;

   private Listbox etudeBox;

   private Label etudeRequired;

   private Listbox couleurEchanBox;

   private Listbox couleurDeriveBox;

   // @since 2.1
   private Checkbox utilisateursArchiveBox;

   private Checkbox archiveBox;

   // catalogues
   private Rows catRows;

   private Grid gridCatalogues;

   private Grid cataloguesBox;

   private Row maladieRow;

   // utilisateurs
   private Group groupProfilUtilisateurs;

   private Hlayout utilisateursArchiveLayout;

   private Grid gridProfilUtilisateur;

   private Grid gridAjoutUtilisateur;

   private Listbox listboxUtilisateurs;

   private Listbox listboxProfils;

   private BindingListModelSet<Utilisateur> utilisateursData;

   private BindingListModelSet<Profil> profilsData;

   // conteneurs
   private Group groupConteneurs;

   private Div conteneursAssocies;

   // codification
   private Group groupCodifications;

   private Div codificationsAssociees;

   // annotations
   private Group groupAnnotations;

   // couleurs
   private Window coulTypesEchanWin;

   private Window coulTypesDeriveWin;

   // Objets Principaux.
   private Banque banque;

   // Associations.
   private final List<Service> services = new ArrayList<>();

   private Service selectedService;

   private List<String> nomsServices = new ArrayList<>();

   private List<String> nomsAndPrenoms = new ArrayList<>();

   private List<Collaborateur> collaborateurs = new ArrayList<>();

   private Collaborateur selectedCollaborateur;

   private Collaborateur selectedContact;

   private final List<Contexte> contextes = new ArrayList<>();

   private Contexte selectedContexte;

   private final List<Catalogue> catalogues = new ArrayList<>();

   private final List<Catalogue> selectedCatalogues = new ArrayList<>();

   private final List<Etude> etudes = new ArrayList<>();

   private Etude selectedEtude;

   private final List<ProfilUtilisateur> profilUtilisateurs = new ArrayList<>();

   private final Set<Utilisateur> updatedUtilisateurs = new HashSet<>();

   private ProfilUtilisateurRowRenderer profilUtilisateurRowRenderer = new ProfilUtilisateurRowRenderer(false);

   private ServiceWithEtablissementRenderer serviceRenderer = new ServiceWithEtablissementRenderer();

   private final List<Couleur> couleurs = new ArrayList<>();

   private Couleur selectedEchanCouleur;

   private Couleur selectedDeriveCouleur;

   private final List<ConteneurDecorator> conteneurs = new ArrayList<>();

   private final List<ConteneurDecorator> copyConteneurs = new ArrayList<>();

   private final List<BanqueTableCodage> codifications = new ArrayList<>();

   private List<TableAnnotation> tablesAnnoPat = new ArrayList<>();

   private List<TableAnnotation> tablesAnnoPrel = new ArrayList<>();

   private List<TableAnnotation> tablesAnnoEchan = new ArrayList<>();

   private List<TableAnnotation> tablesAnnoDerive = new ArrayList<>();

   private List<TableAnnotation> tablesAnnoCess = new ArrayList<>();

   private final List<CouleurEntiteType> coulTypesEchan = new ArrayList<>();

   private final List<CouleurEntiteType> copyCoulTypesEchan = new ArrayList<>();

   private final List<CouleurEntiteType> coulTypesDerives = new ArrayList<>();

   private final List<CouleurEntiteType> copyCoulTypesDerives = new ArrayList<>();

   private final I3listBoxItemRenderer contexteRenderer = new I3listBoxItemRenderer("nom");

   private final CouleurItemRenderer couleurRenderer = new CouleurItemRenderer();

   private final CataloguesRowRenderer catalogueRenderer = new CataloguesRowRenderer();

   private String hautPage;

   private String piedPage;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.banque");
      setCascadable(false);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.nomLabel, this.idLabel, this.descrLabel, this.proprioLabel, this.respLabel,
         this.contactLabel, this.defMaladieLabel, this.defautLibLabel, this.defautCodeLabel, this.autoCrossLabel,
         this.contexteLabel, this.etudeLabel, this.couleurEchanLabel, this.couleurDeriveLabel, this.menuBar, this.gridCatalogues,
         this.archiveLabel});

      setObjBoxsComponents(new Component[] {this.nomBox, this.idBox, this.descrBox, this.proprioBox, this.respBoxDiv,
         this.contactBoxDiv, this.defMaladieBox, this.defautLibBox, this.defautCodeBox, this.autoCrossBox, this.contexteBox,
         this.etudeBox, this.couleurEchanBox, this.couleurDeriveBox,
         //this.checkCataCol,
         this.cataloguesBox, this.archiveBox});

      setRequiredMarks(new Component[] {this.nomRequired, this.contexteRequired, this.etudeRequired});

      drawActionsForBanques();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      // passe les refrences des group headers
      Executions.createComponents("/zuls/contexte/ConteneursAssocies.zul", conteneursAssocies, null);
      getConteneursAssocies().setGroupHeader(groupConteneurs);
      Executions.createComponents("/zuls/contexte/CodificationsAssociees.zul", codificationsAssociees, null);
      getCodificationsAssociees().setGroupHeader(groupCodifications);

      final Map<String, Object> coulTypesEchanArgs = new HashMap<>();
      coulTypesEchanArgs.put("isEchantillonTyped", Boolean.TRUE);
      Executions.createComponents("/zuls/contexte/CoulEntiteTypesAssociees.zul", coulTypesEchanWin, coulTypesEchanArgs);

      final Map<String, Object> coulTypesProdArgs = new HashMap<>();
      coulTypesProdArgs.put("isEchantillonTyped", Boolean.FALSE);
      Executions.createComponents("/zuls/contexte/CoulEntiteTypesAssociees.zul", coulTypesDeriveWin, coulTypesProdArgs);

      final ListitemRenderer<Utilisateur> utilisateurRenderer = (li, utilisateur, index) -> {
         li.setValue(utilisateur);
         li.setLabel(utilisateur.getLogin());
      };

      final ListitemRenderer<Profil> profilRenderer = (li, profil, index) -> {
         li.setValue(profil);
         li.setLabel(profil.getNom());
      };

      listboxUtilisateurs.setItemRenderer(utilisateurRenderer);
      listboxProfils.setItemRenderer(profilRenderer);

      utilisateursData = new BindingListModelSet<>(new HashSet<Utilisateur>(), true);
      profilsData = new BindingListModelSet<>(new HashSet<Profil>(), true);

      utilisateursData.setMultiple(true);

      this.menuBar.setVisible(false);

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.banque = (Banque) obj;

      initAssociations();

      super.setObject(banque);

      //initialisation de la liste des utilisateurs
      final Set<Utilisateur> utilisateursPlateforme = new HashSet<>(ManagerLocator.getManager(UtilisateurManager.class)
         .findByArchiveManager(false, Arrays.asList(SessionUtils.getCurrentPlateforme())));
      utilisateursData.clear();
      utilisateursData.addAll(utilisateursPlateforme);

      //Initialisation de la liste des profils
      final Set<Profil> profilsPlateforme = new HashSet<>(ManagerLocator.getManager(ProfilManager.class)
         .findByPlateformeAndArchiveManager(SessionUtils.getCurrentPlateforme(), false));
      profilsData.clear();
      profilsData.addAll(profilsPlateforme);

      // applique eventuellement acces au bouton modification si
      // utilisateur est admin de la collection
      if(!isCanEdit()){
         editC.setDisabled(!ManagerLocator.getBanqueManager()
            .findByUtilisateurIsAdminManager(SessionUtils.getLoggedUser(sessionScope), SessionUtils.getPlateforme(sessionScope))
            .contains(banque));
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.banque.clone());
      copyConteneurs.clear();
      for(int i = 0; i < conteneurs.size(); i++){
         copyConteneurs.add(conteneurs.get(i).clone());
      }
      //		copyCodifications.clear();
      //		for (int i = 0; i < codifications.size(); i++) {
      //			copyCodifications.add(codifications.get(i).clone());
      //		}
      copyCoulTypesEchan.clear();
      for(int i = 0; i < coulTypesEchan.size(); i++){
         copyCoulTypesEchan.add(coulTypesEchan.get(i).clone());
      }
      copyCoulTypesDerives.clear();
      for(int i = 0; i < coulTypesDerives.size(); i++){
         copyCoulTypesDerives.add(coulTypesDerives.get(i).clone());
      }
   }

   @Override
   public void revertObject(){
      super.revertObject();
      setConteneurs(getCopyConteneurs());
      //setCodifications(getCopyCodifications());
      setCoulTypesEchan(getCopyCoulTypesEchan());
      setCoulTypesDerives(getCopyCoulTypesDerives());
   }

   @Override
   public Banque getObject(){
      return this.banque;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public BanqueController getObjectTabController(){
      return (BanqueController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Banque());
      super.setNewObject();
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      profilUtilisateurRowRenderer.setEditMode(true);

      utilisateursArchiveLayout.setVisible(false);
      groupProfilUtilisateurs.setVisible(true);
      gridAjoutUtilisateur.setVisible(true);
      gridProfilUtilisateur.setVisible(true);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      getConteneursAssocies().switchToCreateMode();
      getCodificationsAssociees().switchToCreateMode();
      getCoulTypesEchanAssociees().switchToCreateMode();
      getCoulTypesProdDeriveAssociees().switchToCreateMode();

      groupAnnotations.setVisible(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.banque.equals(new Banque()));

      profilUtilisateurRowRenderer.setEditMode(false);

      groupProfilUtilisateurs.setVisible(true);
      gridProfilUtilisateur.setVisible(true);

      getConteneursAssocies().switchToStaticMode();
      getCodificationsAssociees().switchToStaticMode();
      getCoulTypesEchanAssociees().switchToStaticMode();
      getCoulTypesProdDeriveAssociees().switchToStaticMode();

      groupAnnotations.setVisible(true);
      getTablesPatSorterController().switchToStaticMode();
      getTablesPrelSorterController().switchToStaticMode();
      getTablesEchanSorterController().switchToStaticMode();
      getTablesDeriveSorterController().switchToStaticMode();
      getTablesCessSorterController().switchToStaticMode();

      //affiche le formulaire d'jout d'utilisateurs
      gridAjoutUtilisateur.setVisible(false);

      if(this.banque.getBanqueId() == null){
         menuBar.setVisible(false);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      Clients.clearBusy();
      Events.echoEvent("onLaterSwitch", self, null);
   }

   public void onLaterSwitch(){
      super.switchToEditMode();

      initEditableMode();

      profilUtilisateurRowRenderer.setEditMode(true);

      getConteneursAssocies().switchToEditMode(true);
      getCodificationsAssociees().switchToEditMode(true);
      getCoulTypesEchanAssociees().switchToEditMode(true);
      getCoulTypesProdDeriveAssociees().switchToEditMode(true);

      getTablesPatSorterController().switchToEditMode();
      getTablesPrelSorterController().switchToEditMode();
      getTablesEchanSorterController().switchToEditMode();
      getTablesDeriveSorterController().switchToEditMode();
      getTablesCessSorterController().switchToEditMode();

      // empeche modification maladie
      defMaladieLabel.setVisible(true);
      defMaladieBox.setVisible(false);

      // empeche modification contexte
      contexteLabel.setVisible(true);
      contexteBox.setVisible(false);

      // empeche modification etude
      etudeLabel.setVisible(true);
      etudeBox.setVisible(false);

      //affiche le formulaire d'jout d'utilisateurs
      gridAjoutUtilisateur.setVisible(true);

      getBinder().loadComponent(self);

      Clients.clearBusy();
   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){

      setEmptyToNulls();

      // prepare la liste de tables d'annotations associees au catalogue
      final List<TableAnnotation> tabs = ManagerLocator.getTableAnnotationManager().findByCataloguesManager(selectedCatalogues);
      List<TableAnnotation> tabsPat = new ArrayList<>();
      List<TableAnnotation> tabsPrel = new ArrayList<>();
      List<TableAnnotation> tabsEchan = new ArrayList<>();
      List<TableAnnotation> tabsDerive = new ArrayList<>();

      // Aucune table de cession dans les catalogues
      List<TableAnnotation> tabsCess = new ArrayList<>();

      for(int i = 0; i < tabs.size(); i++){
         switch(tabs.get(i).getEntite().getNom()){
            case "Patient":
               tabsPat.add(tabs.get(i));
               break;
            case "Prelevement":
               tabsPrel.add(tabs.get(i));
               break;
            case "Echantillon":
               tabsEchan.add(tabs.get(i));
               break;
            case "ProdDerive":
               tabsDerive.add(tabs.get(i));
               break;
            case "Cession":
               tabsCess.add(tabs.get(i));
               break;
            default:
               break;
         }
      }

      if(tabsPat.isEmpty()){
         tabsPat = null;
      }
      if(tabsPrel.isEmpty()){
         tabsPrel = null;
      }
      if(tabsEchan.isEmpty()){
         tabsEchan = null;
      }
      if(tabsDerive.isEmpty()){
         tabsDerive = null;
      }
      if(tabsCess.isEmpty()){
         tabsCess = null;
      }

      final List<CouleurEntiteType> coulTypes = new ArrayList<>();
      coulTypes.addAll(coulTypesEchan);
      coulTypes.addAll(coulTypesDerives);

      if(getGatsbiSelected()){
         this.banque.setEtude(selectedEtude);
      }

      ManagerLocator.getBanqueManager().createOrUpdateObjectManager(banque, SessionUtils.getPlateforme(sessionScope),
         selectedContexte, selectedService, selectedCollaborateur, selectedContact,
         ConteneurDecorator.extractConteneursFromDecos(conteneurs), codifications, tabsPat, tabsPrel, tabsEchan, tabsDerive,
         tabsCess, coulTypes, selectedEchanCouleur, selectedDeriveCouleur, SessionUtils.getLoggedUser(sessionScope),
         updatedUtilisateurs, "creation", SessionUtils.getSystemBaseDir());

      // refresh??? sinon modif associations ne marche pas sur cet objet
      banque = ManagerLocator.getBanqueManager().findByIdManager(banque.getBanqueId());

      // ajout de la banque à la liste
      getObjectTabController().getListe().addToObjectList(banque);
      getObjectTabController().getListe().selectFirstObjet();

      // ajout au MainBanquesListBox
      getMainWindow().prepareListBanques();
      //getMainWindow().resetMainBanquesListBox();

      getMainWindow().updateSelectedBanque(banque);
   }

   @Override
   public void onClick$addNewC(){
      switchToCreateMode();
   }

   //	@Override
   //	public void onClick$cancelC() {
   //		Clients.showBusy(null, true);
   //		Events.echoEvent("onLaterCancel", self, null);
   //	}
   //
   //	public void onLaterCancel() {
   //		clearData();
   //		super.onClick$cancelC();
   //		Clients.showBusy(null, false);
   //	}

   @Override
   public void onClick$createC(){
      // validation des champs obligatoires
      if(selectedContexte != null && selectedContexte.getContexteId() != null){
         Clients.clearWrongValue(contexteBox);
      }else{
         Clients.scrollIntoView(contexteBox);
         throw new WrongValueException(contexteBox, Labels.getLabel("validation.syntax.empty"));
      }

      if(getGatsbiSelected()){
         if(selectedEtude != null && selectedEtude.getEtudeId() != null){
            Clients.clearWrongValue(etudeBox);
         }else{
            Clients.scrollIntoView(etudeBox);
            throw new WrongValueException(etudeBox, Labels.getLabel("validation.syntax.empty"));
         }
      }

      Clients.showBusy(Labels.getLabel("ficheBanque.modification.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   /**
    * @see super.onClick$createC. Ajoute la gestion de la fermeture
    * de la busy box.
    */
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
      final boolean isReferenced = ManagerLocator.getBanqueManager().isReferencedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isReferenced){
         setDeleteMessage(Labels.getLabel("banque.deletion.isReferenced"));
      }
      setDeletable(!isReferenced);
      setFantomable(!isReferenced);
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getBanqueManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope),
         SessionUtils.getSystemBaseDir(), false);
      ManagerLocator.getPlateformeManager().getBanquesManager(SessionUtils.getPlateforme(sessionScope)).remove(getObject());
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         removeObject((String) event.getData());

         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().deselectRow();
            getObjectTabController().getListe().removeObjectAndUpdateList(getObject());
         }
         clearData();
         getMainWindow().prepareListBanques();
         if(getObjectTabController().getListe() != null && !getObjectTabController().getListe().getListObjects().isEmpty()){
            getObjectTabController().getListe().selectFirstObjet();

            getMainWindow().prepareListBanques();
            getMainWindow().updateSelectedBanque(getObjectTabController().getListe().getListObjects().get(0));
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

   @Override
   public void onClick$editC(){
      if(this.banque != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      Clients.clearBusy();
      Events.echoEvent("onLaterRevert", self, null);
   }

   //	public void onLaterRevert() {
   //		clearConstraints();
   //		super.onClick$revertC();
   //		Clients.showBusy(null, false);
   //	}

   @Override
   public void onClick$validateC(){
      // validation des champs obligatoires
      if(selectedContexte != null && selectedContexte.getContexteId() != null){
         Clients.clearWrongValue(contexteBox);
      }else{
         Clients.scrollIntoView(contexteBox);
         throw new WrongValueException(contexteBox, Labels.getLabel("validation.syntax.empty"));
      }

      Clients.showBusy(Labels.getLabel("ficheBanque.modification.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   /**
    * @see super.onClick$validateC. Ajoute la gestion de la fermeture
    * de la busy box.
    */
   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
         switchToStaticMode();

         // udate si banque courante
         if(banque.getBanqueId().equals(SessionUtils.getSelectedBanques(sessionScope).get(0).getBanqueId())){
            // @since 2.1 archivage -> select first bank
            if(!banque.getArchive()){
               getMainWindow().updateSelectedBanque(banque);
            }else{ // switch to first bank
               getMainWindow().updateSelectedBanque(getObjectTabController().getListe().getListObjects().get(0));
            }
         }

         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Sélectionne le service.
    * @param event Event : seléction sur la liste collabBox.
    * @throws Exception
    */
   public void onSelect$proprioBox(final Event event) throws Exception{
      final int ind = proprioBox.getSelectedIndex();
      if(ind > -1){
         selectedService = services.get(ind);
      }else{
         selectedService = null;
      }
   }

   /**
    * Sélectionne le collaborateur responsable.
    * @param event Event : seléction sur la liste collabBox.
    * @throws Exception
    */
   public void onSelect$collabBox(final Event event) throws Exception{
      final int ind = collabBox.getSelectedIndex();
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }
   }

   /**
    * Sélectionne le collaborateur contact.
    * @param event Event : seléction sur la liste contactBox.
    * @throws Exception
    */
   public void onSelect$contactBox(final Event event) throws Exception{
      final int ind = contactBox.getSelectedIndex();
      if(ind > -1){
         selectedContact = collaborateurs.get(ind);
      }else{
         selectedContact = null;
      }
   }

   /**
    * Affecte un profil utilisateur sur la collection courante
    * @param event
    */
   public void onClick$affecterProfilUtilisateur(){

      final Set<Utilisateur> selectedUsers = utilisateursData.getSelection();

      if(!selectedUsers.isEmpty() && !profilsData.getSelection().isEmpty()){

         final Profil selectedProfil = (Profil) listboxProfils.getSelectedItem().getValue();

         for(final Utilisateur user : selectedUsers){

            final Optional<ProfilUtilisateur> optionalPu =
               profilUtilisateurs.stream().filter(pu -> pu.getUtilisateur().equals(user)).findFirst();

            final ProfilUtilisateur profilUtilisateur;
            if(optionalPu.isPresent()){
               profilUtilisateur = optionalPu.get();
               //On retire l'ancien ProfilUtilisateur de l'utilisateur sur cette banque
               //On ne peut pas faire une simple modification de l'existant car
               //la clé primaire de ProfilUtilisateur est constituée de toutes ses colonnes...
               user.getProfilUtilisateurs().remove(profilUtilisateur);
            }else{
               profilUtilisateur = new ProfilUtilisateur();
               profilUtilisateur.setUtilisateur(user);
               profilUtilisateur.setBanque(this.getBanque());
               profilUtilisateurs.add(profilUtilisateur);
            }

            profilUtilisateur.setProfil(selectedProfil);
            user.getProfilUtilisateurs().add(profilUtilisateur);

            updatedUtilisateurs.add(user);

         }

      }

   }

   public void onClickRemoveProfil(final Event event){

      final ProfilUtilisateur profilUtilisateurToRemove = (ProfilUtilisateur) event.getData();
      final Utilisateur user = profilUtilisateurToRemove.getUtilisateur();

      user.getProfilUtilisateurs().remove(profilUtilisateurToRemove);
      profilUtilisateurs.remove(profilUtilisateurToRemove);

      updatedUtilisateurs.add(user);

      getBinder().loadAttribute(gridProfilUtilisateur, "model");

   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setEmptyToNulls(){

      if(this.banque.getNom() != null && this.banque.getNom().equals("")){
         this.banque.setNom(null);
      }

      if(this.banque.getIdentification() != null && this.banque.getIdentification().equals("")){
         this.banque.setIdentification(null);
      }

      if(this.banque.getDescription() != null && this.banque.getDescription().equals("")){
         this.banque.setDescription(null);
      }

      if(this.banque.getDefautMaladie() != null && this.banque.getDefautMaladie().equals("")){
         this.banque.setDefautMaladie(null);
      }

      if(this.banque.getDefautMaladieCode() != null && this.banque.getDefautMaladieCode().equals("")){
         this.banque.setDefautMaladieCode(null);
      }

      selectedEchanCouleur = null;
      if(couleurEchanBox.getSelectedItem() != null){
         if(((Couleur) couleurEchanBox.getSelectedItem().getValue()).getCouleurId() != null){
            selectedEchanCouleur = (Couleur) couleurEchanBox.getSelectedItem().getValue();
         }
      }

      selectedDeriveCouleur = null;
      if(couleurDeriveBox.getSelectedItem() != null){
         if(((Couleur) couleurDeriveBox.getSelectedItem().getValue()).getCouleurId() != null){
            selectedDeriveCouleur = (Couleur) couleurDeriveBox.getSelectedItem().getValue();
         }
      }

      // Gestion du contexte
      //		selectedContexte = null;
      //		if (contexteBox.getSelectedItem() != null) {
      //			if (((Contexte) contexteBox.getSelectedItem().getValue())
      //					.getContexteId() != null) {
      //				selectedContexte = (Contexte) contexteBox
      //										.getSelectedItem().getValue();
      //			}
      //		}
      if(selectedContexte != null && selectedContexte.getContexteId() == null){
         selectedContexte = null;
      }

      if(selectedEtude != null && selectedEtude.getEtudeId() == null){
         selectedEtude = null;
      }

      // Catalogues
      banque.getCatalogues().clear();
      banque.getCatalogues().addAll(selectedCatalogues);

      // Gestion du collaborateur
      String selectedNomAndPremon = collabBox.getValue().toUpperCase();
      collabBox.setValue(selectedNomAndPremon);
      int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      // Gestion du contact
      selectedNomAndPremon = contactBox.getValue().toUpperCase();
      contactBox.setValue(selectedNomAndPremon);
      ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedContact = collaborateurs.get(ind);
      }else{
         selectedContact = null;
      }

      // Gestion du service
      final String selectedNomService = proprioBox.getValue().toUpperCase();
      proprioBox.setValue(selectedNomService);
      ind = nomsServices.indexOf(selectedNomService);
      if(ind > -1){
         selectedService = services.get(ind);
      }else{
         selectedService = null;
      }
   }

   @Override
   public void updateObject(){

      setEmptyToNulls();

      final List<TableAnnotation> tabs = new ArrayList<>();
      final List<TableAnnotation> tabsPat = TableAnnotationDecorator.undecorateListe(getTablesPatSorterController().getObjs());
      final List<TableAnnotation> tabsPrel = TableAnnotationDecorator.undecorateListe(getTablesPrelSorterController().getObjs());
      final List<TableAnnotation> tabsEchan =
         TableAnnotationDecorator.undecorateListe(getTablesEchanSorterController().getObjs());
      final List<TableAnnotation> tabsDerive =
         TableAnnotationDecorator.undecorateListe(getTablesDeriveSorterController().getObjs());
      final List<TableAnnotation> tabsCess = TableAnnotationDecorator.undecorateListe(getTablesCessSorterController().getObjs());

      // suppr catalogue

      final List<Catalogue> supprCatalogues =
         ListUtils.removeAll(ManagerLocator.getCatalogueManager().findByAssignedBanqueManager(banque), selectedCatalogues);

      tabs.addAll(ManagerLocator.getTableAnnotationManager().findByCataloguesManager(supprCatalogues));

      for(int i = 0; i < tabs.size(); i++){
         if(tabs.get(i).getEntite().getNom().equals("Patient")){
            tabsPat.remove(tabs.get(i));
         }else if(tabs.get(i).getEntite().getNom().equals("Prelevement")){
            tabsPrel.remove(tabs.get(i));
         }else if(tabs.get(i).getEntite().getNom().equals("Echantillon")){
            tabsEchan.remove(tabs.get(i));
         }else if(tabs.get(i).getEntite().getNom().equals("ProdDerive")){
            tabsDerive.remove(tabs.get(i));
         }
      }

      // ajout catalogues
      tabs.clear();

      final List<Catalogue> newCatalogues =
         ListUtils.removeAll(selectedCatalogues, ManagerLocator.getCatalogueManager().findByAssignedBanqueManager(banque));
      tabs.addAll(ManagerLocator.getTableAnnotationManager().findByCataloguesManager(newCatalogues));

      for(int i = 0; i < tabs.size(); i++){
         if(tabs.get(i).getEntite().getNom().equals("Patient")){
            tabsPat.add(tabs.get(i));
         }else if(tabs.get(i).getEntite().getNom().equals("Prelevement")){
            tabsPrel.add(tabs.get(i));
         }else if(tabs.get(i).getEntite().getNom().equals("Echantillon")){
            tabsEchan.add(tabs.get(i));
         }else if(tabs.get(i).getEntite().getNom().equals("ProdDerive")){
            tabsDerive.add(tabs.get(i));
         }
      }

      final List<CouleurEntiteType> coulTypes = new ArrayList<>();
      coulTypes.addAll(coulTypesEchan);
      coulTypes.addAll(coulTypesDerives);

      coulTypes.forEach(ct -> ct.setBanque(banque));

      ManagerLocator.getBanqueManager().createOrUpdateObjectManager(banque, null, selectedContexte, selectedService,
         selectedCollaborateur, selectedContact, ConteneurDecorator.extractConteneursFromDecos(conteneurs), codifications,
         tabsPat, tabsPrel, tabsEchan, tabsDerive, tabsCess, coulTypes, selectedEchanCouleur, selectedDeriveCouleur,
         SessionUtils.getLoggedUser(sessionScope), updatedUtilisateurs, "modification", null);

      // update de la liste
      getObjectTabController().getListe().updateObjectGridList(banque);

      // reload mainBanquesListBox et mainWindow
      getMainWindow().prepareListBanques();
      getMainWindow().resetMainBanquesListBox();
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (collaborateur, services...).
    */
   public void initEditableMode(){

      // init des contextes
      if(contextes.isEmpty()){
         contextes.add(new Contexte());
         contextes.addAll(ManagerLocator.getContexteManager().findByOrderManager());
      }

      // init des collaborateurs
      collaborateurs.clear();
      nomsAndPrenoms.clear();
      collaborateurs.addAll(ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager());
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
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

      //init des couleurs
      if(couleurs.isEmpty()){
         couleurs.add(new Couleur());
         couleurs.addAll(ManagerLocator.getCouleurManager().findAllObjectsManager());
      }

      // gatsbi etude
      if(etudes.isEmpty()){
         etudes.addAll(ManagerLocator.getEtudeManager().findByPfOrderManager(SessionUtils.getCurrentPlateforme()));
      }

      initAssociations();

      if(selectedContexte == null){
         selectedContexte = contextes.get(0);
      }
      getBinder().loadAttribute(contexteBox, "model");
      //contexteBox.setSelectedIndex(contextes.indexOf(selectedContexte));

      proprioBox.setModel(new CustomSimpleListModel(nomsServices));
      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
      contactBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      couleurEchanBox.setModel(new SimpleListModel<>(couleurs));
      couleurEchanBox.setSelectedIndex(couleurs.indexOf(selectedEchanCouleur));
      couleurDeriveBox.setModel(new SimpleListModel<>(couleurs));
      couleurDeriveBox.setSelectedIndex(couleurs.indexOf(selectedDeriveCouleur));

      catRows.setAttribute("checkedCatalogues", selectedCatalogues);
   }

   /**
    * Etablit les associations de la banque courante.
    */
   private void initAssociations(){
      conteneurs.clear();
      codifications.clear();
      coulTypesEchan.clear();
      coulTypesDerives.clear();
      catalogues.clear();
      selectedCatalogues.clear();
      profilUtilisateurs.clear();
      updatedUtilisateurs.clear();
      tablesAnnoPat.clear();
      tablesAnnoPrel.clear();
      tablesAnnoEchan.clear();
      tablesAnnoDerive.clear();
      tablesAnnoCess.clear();

      selectedContexte = null;
      selectedEtude = null;
      selectedEchanCouleur = null;
      selectedDeriveCouleur = null;
      selectedCollaborateur = null;
      selectedService = null;
      collabBox.setValue("");
      contactBox.setValue("");
      proprioBox.setValue("");

      if(banque.getBanqueId() != null){
         selectedContexte = this.banque.getContexte();
         if(selectedContexte != null){
            catalogues.addAll(ManagerLocator.getContexteManager().getCataloguesManager(selectedContexte));
         }
         selectedCatalogues.addAll(ManagerLocator.getCatalogueManager().findByAssignedBanqueManager(banque));

         selectedEtude = this.banque.getEtude();

         if(this.banque.getCollaborateur() != null){
            selectedCollaborateur = this.banque.getCollaborateur();
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
            if(!collaborateurs.contains(this.banque.getCollaborateur())){
               collaborateurs.add(this.banque.getCollaborateur());
               selectedCollaborateur = this.banque.getCollaborateur();
               nomsAndPrenoms.add(this.banque.getCollaborateur().getNomAndPrenom());
            }
         }

         if(this.banque.getContact() != null){
            selectedContact = this.banque.getContact();
            contactBox.setValue(selectedContact.getNomAndPrenom());
            if(!collaborateurs.contains(this.banque.getContact())){
               collaborateurs.add(this.banque.getContact());
               selectedContact = this.banque.getContact();
               nomsAndPrenoms.add(this.banque.getContact().getNomAndPrenom());
            }
         }

         if(this.banque.getProprietaire() != null){
            selectedService = this.banque.getProprietaire();

            final StringBuffer sb = new StringBuffer();
            if(selectedService != null && selectedService.getNom() != null){
               sb.append(selectedService.getNom());
               if(selectedService.getEtablissement() != null && selectedService.getEtablissement().getNom() != null){
                  sb.append(" (");
                  sb.append(selectedService.getEtablissement().getNom());
                  sb.append(")");
               }
            }

            proprioBox.setValue(sb.toString());
            if(!services.contains(this.banque.getProprietaire())){
               services.add(this.banque.getProprietaire());
               selectedService = this.banque.getProprietaire();
               nomsServices.add(sb.toString());
            }
         }

         // since 2.1 init only non-archived users
         utilisateursArchiveBox.setChecked(true);
         profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByBanqueManager(banque, false));

         selectedEchanCouleur = banque.getEchantillonCouleur();
         selectedDeriveCouleur = banque.getProdDeriveCouleur();

         conteneurs.addAll(
            ConteneurDecorator.decorateListe(ManagerLocator.getConteneurManager().findByBanqueWithOrderManager(banque), null));

         codifications.addAll(ManagerLocator.getBanqueManager().getBanqueTableCodageByBanqueManager(banque));

         coulTypesEchan.addAll(ManagerLocator.getCouleurEntiteTypeManager().findAllCouleursForEchanTypeByBanqueManager(banque));

         coulTypesDerives.addAll(ManagerLocator.getCouleurEntiteTypeManager().findAllCouleursForProdTypeByBanqueManager(banque));

         tablesAnnoPat.addAll(ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0), banque));

         tablesAnnoPrel.addAll(ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0), banque));

         tablesAnnoEchan.addAll(ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0), banque));

         tablesAnnoDerive.addAll(ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0), banque));

         tablesAnnoCess.addAll(ManagerLocator.getTableAnnotationManager()
            .findByEntiteAndBanqueManager(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0), banque));
      }

      getConteneursAssocies().setPlateforme(SessionUtils.getCurrentPlateforme());
      getConteneursAssocies().setObjects(conteneurs);
      getCodificationsAssociees().setObjects(codifications);
      getCoulTypesEchanAssociees().setObjects(coulTypesEchan);
      getCoulTypesProdDeriveAssociees().setObjects(coulTypesDerives);
      getTablesPatSorterController().setObjs(TableAnnotationDecorator.decorateListe(tablesAnnoPat));
      getTablesPrelSorterController().setObjs(TableAnnotationDecorator.decorateListe(tablesAnnoPrel));
      getTablesEchanSorterController().setObjs(TableAnnotationDecorator.decorateListe(tablesAnnoEchan));
      getTablesDeriveSorterController().setObjs(TableAnnotationDecorator.decorateListe(tablesAnnoDerive));
      getTablesCessSorterController().setObjs(TableAnnotationDecorator.decorateListe(tablesAnnoCess));
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(idBox);
      Clients.clearWrongValue(descrBox);
      Clients.clearWrongValue(defautLibBox);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    * AdminPF a tous les droits sur ses banques.
    * L'admin de collection a le droit de modification sur sa banque courante
    * (mais pas sur les conteneurs).
    */
   public void drawActionsForBanques(){
      if(sessionScope.containsKey("AdminPF")){
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
         setCanSeeHistorique(true);
      }
   }

   /**
    * Active ou inactive le textbox permettant l'enregistrement
    * d'une maladie par defaut.
    */
   public void onCheck$defMaladieBox(){
      if(defMaladieBox.isChecked()){
         defautLibBox.setDisabled(false);
      }else{
         defautLibBox.setValue(null);
         Clients.clearWrongValue(defautLibBox);
         defautLibBox.setDisabled(true);
      }
   }

   public AdministrationController getAdministrationController(){
      return (AdministrationController) self.getParent().getParent().getParent().getParent().getParent().getParent().getParent()
         .getParent().getParent().getAttributeOrFellow("winAdministration$composer", true);
   }

   /**
    * Traite onClick hyperlien nom utilisateur
    * @param event
    */
   public void onClickProfilUtilisateur(final Event event){
      final ProfilUtilisateur pf = (ProfilUtilisateur) event.getData();
      getAdministrationController().selectUtilisateurInController(pf.getUtilisateur());
   }

   /**
    * Traite onClick hyperlien nom profil pour un utilisateur
    * @param event
    * @since 2.1
    */
   public void onClickProfilUtilisateurProfil(final Event event){
      final ProfilUtilisateur pf = (ProfilUtilisateur) event.getData();
      getAdministrationController().selectProfilInController(pf.getProfil());
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
      sb.append("fiche_collection");
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
         ObjectTypesFormatters.getLabel("impression.banque.title", new String[] {banque.getNom()}));
      addInfosBanqueToPrint(page1);
      addInfosAnnotationsToPrint(page1);
      addInfosConteneursToPrint(page1);
      addInfosCodificationsToPrint(page1);
      addInfosListeUtilisateurs(page1);

      return document;
   }

   /**
    * Ajout les infos générales à imprimer.
    * @param page
    */
   public void addInfosBanqueToPrint(final Element page){
      // Nom
      String tmp = "";
      if(banque.getNom() != null){
         tmp = banque.getNom();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("Champ.Banque.Nom"), tmp);
      // Identification
      if(banque.getIdentification() != null){
         tmp = banque.getIdentification();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("Champ.Banque.Identification"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Description
      if(banque.getDescription() != null){
         tmp = banque.getDescription();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("Champ.Banque.Description"), tmp);
      final LigneDeuxColonnesParagraphe li2 = new LigneDeuxColonnesParagraphe(cp3);

      // Propriétaire
      if(banque.getProprietaire() != null){
         final StringBuffer sb = new StringBuffer();
         sb.append(banque.getProprietaire().getNom());

         if(banque.getProprietaire().getEtablissement() != null){
            sb.append(" (");
            sb.append(banque.getProprietaire().getEtablissement().getNom());
            sb.append(")");
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("Champ.Banque.Proprietaire"), tmp);
      final LigneDeuxColonnesParagraphe li3 = new LigneDeuxColonnesParagraphe(cp4);

      // Responsable
      if(banque.getCollaborateur() != null){
         tmp = banque.getCollaborateur().getNomAndPrenom();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("Champ.Banque.Collaborateur"), tmp);
      final LigneDeuxColonnesParagraphe li4 = new LigneDeuxColonnesParagraphe(cp5);

      // Contact
      if(banque.getContact() != null){
         tmp = banque.getContact().getNomAndPrenom();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("Champ.Banque.Contact"), tmp);
      final LigneDeuxColonnesParagraphe li5 = new LigneDeuxColonnesParagraphe(cp6);

      // Maladie
      if(banque.getDefMaladies() != null){
         tmp = ObjectTypesFormatters.booleanLitteralFormatter(this.banque.getDefMaladies());
      }else{
         tmp = "-";
      }
      final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("Champ.Banque.DefMaladies"), tmp);
      // Libellé
      if(banque.getDefautMaladie() != null){
         tmp = banque.getDefautMaladie();
      }else{
         tmp = "-";
      }

      final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp7});

      final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("Champ.Banque.MaladieDefaut"), tmp);

      // Code
      if(banque.getDefautMaladieCode() != null){
         tmp = banque.getDefautMaladieCode();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp8b = new CoupleValeur(Labels.getLabel("Champ.Banque.MaladieCodeDefaut"), tmp);

      final LigneParagraphe li6b = new LigneParagraphe("", new CoupleValeur[] {cp8, cp8b});

      // Prlvts accessibles aux autres collections
      if(banque.getAutoriseCrossPatient() != null){
         tmp = ObjectTypesFormatters.booleanLitteralFormatter(this.banque.getAutoriseCrossPatient());
      }else{
         tmp = "-";
      }
      final CoupleSimpleValeur cp9 = new CoupleSimpleValeur(Labels.getLabel("Champ.Banque.AutoriseCrossPatient"), tmp);
      final LigneSimpleParagraphe li7 = new LigneSimpleParagraphe(cp9);

      // Contexte
      if(getContexte() != null){
         tmp = getContexte();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp10 = new CoupleValeur(Labels.getLabel("Champ.Banque.Contexte"), tmp);
      // Catalogues
      if(catalogues.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < catalogues.size(); i++){
            sb.append(catalogues.get(i).getNom());
            if(i + 1 < catalogues.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp11 = new CoupleValeur(Labels.getLabel("ficheBanque.contexte.catalogues"), tmp);
      final LigneParagraphe li8 = new LigneParagraphe("", new CoupleValeur[] {cp10, cp11});

      final Paragraphe par1 = new Paragraphe(null, new Object[] {li1, li2, li3, li4, li5, li6, li6b, li7, li8}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les infos annotations à imprimer.
    * @param page
    */
   public void addInfosAnnotationsToPrint(final Element page){
      String tmp = "";
      // Tables patients
      if(tablesAnnoPat.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < tablesAnnoPat.size(); i++){
            sb.append(tablesAnnoPat.get(i).getNom());
            if(i + 1 < tablesAnnoPat.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("Entite.Patient"), tmp);
      final LigneDeuxColonnesParagraphe li1 = new LigneDeuxColonnesParagraphe(cp1);

      // Tables prelevements
      if(tablesAnnoPrel.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < tablesAnnoPrel.size(); i++){
            sb.append(tablesAnnoPrel.get(i).getNom());
            if(i + 1 < tablesAnnoPrel.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("Entite.Prelevement"), tmp);
      final LigneDeuxColonnesParagraphe li2 = new LigneDeuxColonnesParagraphe(cp2);

      // Tables echantillons
      if(tablesAnnoEchan.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < tablesAnnoEchan.size(); i++){
            sb.append(tablesAnnoEchan.get(i).getNom());
            if(i + 1 < tablesAnnoEchan.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("Entite.Echantillon"), tmp);
      final LigneDeuxColonnesParagraphe li3 = new LigneDeuxColonnesParagraphe(cp3);

      // Tables dérivés
      if(tablesAnnoDerive.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < tablesAnnoDerive.size(); i++){
            sb.append(tablesAnnoDerive.get(i).getNom());
            if(i + 1 < tablesAnnoDerive.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("Entite.ProdDerive"), tmp);
      final LigneDeuxColonnesParagraphe li4 = new LigneDeuxColonnesParagraphe(cp4);

      // Tables cession
      if(tablesAnnoCess.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < tablesAnnoCess.size(); i++){
            sb.append(tablesAnnoCess.get(i).getNom());
            if(i + 1 < tablesAnnoCess.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("Entite.Cession"), tmp);
      final LigneDeuxColonnesParagraphe li5 = new LigneDeuxColonnesParagraphe(cp5);

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("ficheBanque.contexte.tablesAnnos"),
         new Object[] {li1, li2, li3, li4, li5}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les infos conteneurs à imprimer.
    * @param page
    */
   public void addInfosConteneursToPrint(final Element page){
      // Entete
      final String[] listeEntete = new String[5];
      listeEntete[0] = Labels.getLabel("conteneur.code");
      listeEntete[1] = Labels.getLabel("conteneur.nom");
      listeEntete[2] = Labels.getLabel("conteneur.temp");
      listeEntete[3] = Labels.getLabel("conteneur.service");
      listeEntete[4] = Labels.getLabel("service.etablissement");
      final EnteteListe entetes = new EnteteListe(listeEntete);

      // liste des cédés
      final LigneListe[] liste = new LigneListe[conteneurs.size()];
      for(int i = 0; i < conteneurs.size(); i++){
         final String[] valeurs = new String[5];
         // code
         valeurs[0] = conteneurs.get(i).getConteneur().getCode();
         // nom
         valeurs[1] = conteneurs.get(i).getConteneur().getNom();
         // température
         final StringBuffer sb = new StringBuffer();
         sb.append(conteneurs.get(i).getConteneur().getTemp());
         sb.append("°C");
         valeurs[2] = sb.toString();
         // service
         if(conteneurs.get(i).getConteneur().getService() != null){
            valeurs[3] = conteneurs.get(i).getConteneur().getService().getNom();
         }else{
            valeurs[3] = "-";
         }
         // etablissement
         if(conteneurs.get(i).getConteneur().getService() != null
            && conteneurs.get(i).getConteneur().getService().getEtablissement() != null){
            valeurs[4] = conteneurs.get(i).getConteneur().getService().getEtablissement().getNom();
         }else{
            valeurs[4] = "-";
         }
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      ListeElement listeSites = null;
      if(conteneurs.size() > 0){
         listeSites = new ListeElement(null, entetes, liste);
      }

      // ajout du paragraphe
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("Champ.Banque.Conteneurs"));
      sb.append(" (");
      sb.append(conteneurs.size());
      sb.append(")");
      final Paragraphe par = new Paragraphe(sb.toString(), null, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   /**
    * Ajout les infos codifications à imprimer.
    * @param page
    */
   public void addInfosCodificationsToPrint(final Element page){
      String tmp = "";

      // codifications
      if(codifications.size() > 0){
         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < codifications.size(); i++){
            sb.append(codifications.get(i).getTableCodage().getNom());
            if(!codifications.get(i).getLibelleExport()){
               sb.append("[" + Labels.getLabel("code.code") + "]");
            }else{
               sb.append("[" + Labels.getLabel("code.libelle") + "]");
            }
            if(i + 1 < codifications.size()){
               sb.append(", ");
            }else{
               sb.append(".");
            }
         }
         tmp = sb.toString();
      }else{
         tmp = "-";
      }
      final CoupleSimpleValeur cp1 = new CoupleSimpleValeur(Labels.getLabel("Champ.Banque.Codifications"), tmp);
      final LigneSimpleParagraphe li1 = new LigneSimpleParagraphe(cp1);

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("Champ.Banque.Codifications"));
      sb.append(" (");
      sb.append(codifications.size());
      sb.append(")");
      final Paragraphe par1 = new Paragraphe(sb.toString(), new Object[] {li1}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Crée le bloc contenant une liste de cessions.
    * @param cessions Cessions à imprimer.
    * @param champs Colonnes à imprimer.
    */
   public void addInfosListeUtilisateurs(final Element page){
      // Entete
      final String[] listeEntete = new String[2];
      listeEntete[0] = Labels.getLabel("utilisateur.login");
      listeEntete[1] = Labels.getLabel("utilisateur.profil");
      final EnteteListe entetes = new EnteteListe(listeEntete);

      final List<ProfilUtilisateur> tmp = new ArrayList<>();
      for(int i = 0; i < profilUtilisateurs.size(); i++){
         if(!profilUtilisateurs.get(i).getUtilisateur().isArchive()){
            tmp.add(profilUtilisateurs.get(i));
         }
      }

      // liste des cédés
      final LigneListe[] liste = new LigneListe[tmp.size()];
      for(int i = 0; i < tmp.size(); i++){
         final String[] valeurs = new String[2];
         // Login
         valeurs[0] = tmp.get(i).getUtilisateur().getLogin();
         // Profil
         valeurs[1] = tmp.get(i).getProfil().getNom();
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      ListeElement listeSites = null;
      if(tmp.size() > 0){
         listeSites = new ListeElement(null, entetes, liste);
      }

      // ajout du paragraphe
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("ficheBanque.utilisateurs"));
      sb.append(" (");
      sb.append(tmp.size());
      sb.append(")");
      final Paragraphe par = new Paragraphe(sb.toString(), null, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/
   public Banque getBanque(){
      return this.banque;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selected){
      this.selectedCollaborateur = selected;
   }

   public Collaborateur getSelectedContact(){
      return selectedContact;
   }

   public void setSelectedContact(final Collaborateur selected){
      this.selectedContact = selected;
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

   public Contexte getSelectedContexte(){
      return selectedContexte;
   }

   public void setSelectedContexte(final Contexte sContexte){
      this.selectedContexte = sContexte;
   }

   public List<Contexte> getContextes(){
      return contextes;
   }

   public I3listBoxItemRenderer getContexteRenderer(){
      return contexteRenderer;
   }

   public CouleurItemRenderer getCouleurRenderer(){
      return couleurRenderer;
   }

   public List<Catalogue> getCatalogues(){
      return catalogues;
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
      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      if(this.banque.getCollaborateur() != null && !collaborateurs.contains(this.banque.getCollaborateur())){
         collaborateurs.add(this.banque.getCollaborateur());
         nomsAndPrenoms.add(this.banque.getCollaborateur().getNomAndPrenom());
      }

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

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/

   public String getContexte(){
      if(selectedContexte != null){
         return Labels.getLabel("Contexte." + selectedContexte.getNom());
      }
      return null;
   }

   public String getSClassCollaborateur(){
      if(this.banque != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.banque.getCollaborateur());
      }
      return "";
   }

   public String getSClassContact(){
      if(this.banque != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.banque.getContact());
      }
      return "";
   }

   public String getSClassService(){
      if(this.banque != null){
         return ObjectTypesFormatters.sClassService(this.banque.getProprietaire());
      }
      return "";
   }

   /**
    * @since 2.1
    * @return boolean formatted
    */
   public String getArchiveFormatted(){
      if(this.banque != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.banque.getArchive());
      }
      return "";
   }

   public String getDefMaladieFormatted(){
      if(this.banque != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.banque.getDefMaladies());
      }
      return "";
   }

   public String getAutoCrossFormatted(){
      if(this.banque != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.banque.getAutoriseCrossPatient());
      }
      return "";
   }

   public String getStyleCouleurEchan(){
      if(this.banque != null && this.banque.getEchantillonCouleur() != null){
         return "color: " + this.banque.getEchantillonCouleur().getHexa();
      }
      return "";
   }

   public String getStyleCouleurDerive(){
      if(this.banque != null){
         return "color: " + this.banque.getProdDeriveCouleur().getHexa();
      }
      return "";
   }

   public String getEchantillonCouleur(){
      if(banque != null && banque.getEchantillonCouleur() != null){
         return Labels.getLabel("Couleur." + banque.getEchantillonCouleur().getCouleur());
      }
      return null;
   }

   public String getProdDeriveCouleur(){
      if(banque != null && banque.getProdDeriveCouleur() != null){
         return Labels.getLabel("Couleur." + banque.getProdDeriveCouleur().getCouleur());
      }
      return null;
   }

   /*************************************************************************/
   /************************** CONTEXTE *************************************/
   /*************************************************************************/
   public void onSelect$contexteBox(){
      // validation
      if(selectedContexte != null && selectedContexte.getContexteId() != null){
         Clients.clearWrongValue(contexteBox);
      }else{
         Clients.scrollIntoView(contexteBox);
         throw new WrongValueException(contexteBox, Labels.getLabel("validation.syntax.empty"));
      }

      selectedCatalogues.clear();
      catalogues.clear();
      if(selectedContexte != null && selectedContexte.getContexteId() != null){
         catalogues.addAll(ManagerLocator.getContexteManager().getCataloguesManager(selectedContexte));
      }
   }

   /*************************************************************************/
   /************************** CONTENEURS************************************/
   /*************************************************************************/
   public List<ConteneurDecorator> getConteneurs(){
      return conteneurs;
   }

   public void setConteneurs(final List<ConteneurDecorator> cts){
      this.conteneurs.clear();
      this.conteneurs.addAll(cts);
   }

   public List<ConteneurDecorator> getCopyConteneurs(){
      return copyConteneurs;
   }

   /**
    * Renvoie le controller associe au composant permettant la getsion
    * des associations one-to-many avec les conteneurs.
    */
   public ConteneursAssocies getConteneursAssocies(){
      return (ConteneursAssocies) self.getFellow("conteneursAssocies").getFellow("winConteneursAssocies")
         .getAttributeOrFellow("winConteneursAssocies$composer", true);
   }

   /**
    * Met à jour le composant conteneurs. Cette méthode est appelée
    * depuis d'autre onglets comme Stockage si mise à jour ou
    * suppression d'un conteneur.
    */
   public void updateConteneurs(){
      if(this.banque != null && this.banque.getBanqueId() != null){
         getConteneursAssocies().setObjects(ConteneurDecorator
            .decorateListe(ManagerLocator.getConteneurManager().findByBanqueWithOrderManager(banque), banque.getPlateforme()));
      }
   }

   /*************************************************************************/
   /************************** CODIFICATIONS ********************************/
   /*************************************************************************/
   public List<BanqueTableCodage> getCodifications(){
      return codifications;
   }

   public void setCodifications(final List<BanqueTableCodage> cdf){
      this.codifications.clear();
      this.codifications.addAll(cdf);
   }

   //	public List<TableCodage> getCopyCodifications() {
   //		return copyCodifications;
   //	}

   /**
    * Renvoie le controller associe au composant permettant la gestion
    * des associations one-to-many avec les tables de codifications.
    */
   public CodificationsAssociees getCodificationsAssociees(){
      return (CodificationsAssociees) self.getFellow("codificationsAssociees").getFellow("winCodificationsAssociees")
         .getAttributeOrFellow("winCodificationsAssociees$composer", true);
   }

   /*************************************************************************/
   /************************** COULEUR ENTITE TYPES**************************/
   /*************************************************************************/

   public List<CouleurEntiteType> getCoulTypesEchan(){
      return coulTypesEchan;
   }

   public void setCoulTypesEchan(final List<CouleurEntiteType> ctE){
      this.coulTypesEchan.clear();
      this.coulTypesEchan.addAll(ctE);
   }

   public List<CouleurEntiteType> getCopyCoulTypesEchan(){
      return copyCoulTypesEchan;
   }

   /**
    * Renvoie le controller associe au composant permettant la gestion
    * des associations one-to-many avec les couleurs assignees aux types
    * echantillons.
    */
   public CoulTypesAssociees getCoulTypesEchanAssociees(){
      return (CoulTypesAssociees) self.getFellow("coulTypesEchanWin").getFellow("winCoulTypesAssociees")
         .getAttributeOrFellow("winCoulTypesAssociees$composer", true);
   }

   public List<CouleurEntiteType> getCoulTypesDerives(){
      return coulTypesDerives;
   }

   public void setCoulTypesDerives(final List<CouleurEntiteType> ctD){
      this.coulTypesDerives.clear();
      this.coulTypesDerives.addAll(ctD);
   }

   public List<CouleurEntiteType> getCopyCoulTypesDerives(){
      return copyCoulTypesDerives;
   }

   /**
    * Renvoie le controller associe au composant permettant la gestion
    * des associations one-to-many avec les couleurs assignees
    * aux types de derives.
    */
   public CoulTypesAssociees getCoulTypesProdDeriveAssociees(){
      return (CoulTypesAssociees) self.getFellow("coulTypesDeriveWin").getFellow("winCoulTypesAssociees")
         .getAttributeOrFellow("winCoulTypesAssociees$composer", true);
   }

   public List<Couleur> getCouleurs(){
      return couleurs;
   }

   public List<ProfilUtilisateur> getProfilUtilisateurs(){
      return profilUtilisateurs;
   }

   public ConstWord getBanqueNomConstraint(){
      return ContexteConstraints.getBanqueNomConstraint();
   }

   public ConstWord getIdentificationConstraint(){
      return ContexteConstraints.getIdentificationConstraint();
   }

   public ConstText getDescrConstraint(){
      return ContexteConstraints.getDescrConstraint();
   }

   public ConstCode getCodeMaladieConstraint(){
      return ContexteConstraints.getCodeMaladieConstraint();
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }

   public ProfilUtilisateurRowRenderer getProfilUtilisateurRowRenderer(){
      return profilUtilisateurRowRenderer;
   }

   public void setProfilUtilisateurRowRenderer(final ProfilUtilisateurRowRenderer pRowRenderer){
      this.profilUtilisateurRowRenderer = pRowRenderer;
   }

   public String getServiceProprietaireFormated(){
      final StringBuffer sb = new StringBuffer();
      if(banque != null){
         if(banque.getProprietaire() != null){
            sb.append(banque.getProprietaire().getNom());

            if(banque.getProprietaire().getEtablissement() != null){
               sb.append(" (");
               sb.append(banque.getProprietaire().getEtablissement().getNom());
               sb.append(")");
            }
         }
      }
      return sb.toString();
   }

   /*************************************************************************/
   /************************** TABLE ANNOTATIONS ****************************/
   /*************************************************************************/
   public List<TableAnnotation> getTablesAnnoPat(){
      return tablesAnnoPat;
   }

   public void setTablesAnnoPat(final List<TableAnnotation> t){
      this.tablesAnnoPat = t;
   }

   public List<TableAnnotation> getTablesAnnoPrel(){
      return tablesAnnoPrel;
   }

   public void setTablesAnnoPrel(final List<TableAnnotation> t){
      this.tablesAnnoPrel = t;
   }

   public List<TableAnnotation> getTablesAnnoEchan(){
      return tablesAnnoEchan;
   }

   public void setTablesAnnoEchan(final List<TableAnnotation> t){
      this.tablesAnnoEchan = t;
   }

   public List<TableAnnotation> getTablesAnnoDerive(){
      return tablesAnnoDerive;
   }

   public void setTablesAnnoDerive(final List<TableAnnotation> t){
      this.tablesAnnoDerive = t;
   }

   public List<TableAnnotation> getTablesAnnoCess(){
      return tablesAnnoCess;
   }

   public void setTablesAnnoCess(final List<TableAnnotation> t){
      this.tablesAnnoCess = t;
   }

   public TableAnnoSortingGrid getTablesPatSorterController(){
      return (TableAnnoSortingGrid) self.getFellow("tablesPatSorter").getFirstChild()
         .getAttributeOrFellow("winAnnoTablesAssociees$composer", true);
   }

   public TableAnnoSortingGrid getTablesPrelSorterController(){
      return (TableAnnoSortingGrid) self.getFellow("tablesPrelSorter").getFirstChild()
         .getAttributeOrFellow("winAnnoTablesAssociees$composer", true);
   }

   public TableAnnoSortingGrid getTablesEchanSorterController(){
      return (TableAnnoSortingGrid) self.getFellow("tablesEchanSorter").getFirstChild()
         .getAttributeOrFellow("winAnnoTablesAssociees$composer", true);
   }

   public TableAnnoSortingGrid getTablesDeriveSorterController(){
      return (TableAnnoSortingGrid) self.getFellow("tablesDeriveSorter").getFirstChild()
         .getAttributeOrFellow("winAnnoTablesAssociees$composer", true);
   }

   public TableAnnoSortingGrid getTablesCessSorterController(){
      return (TableAnnoSortingGrid) self.getFellow("tablesCessSorter").getFirstChild()
         .getAttributeOrFellow("winAnnoTablesAssociees$composer", true);
   }

   public String getHautPage(){
      return hautPage;
   }

   public void setHautPage(final String h){
      this.hautPage = h;
   }

   public String getPiedPage(){
      return piedPage;
   }

   public void setPiedPage(final String p){
      this.piedPage = p;
   }

   public List<Catalogue> getSelectedCatalogues(){
      return selectedCatalogues;
   }

   public CataloguesRowRenderer getCatalogueRenderer(){
      return catalogueRenderer;
   }

   /**
    * Gere le clique sur la checkbox au sein d'une liste
    * de catalogues.
    * Ajoute ou supprime le catalogue de la liste si la box est
    * respectivement cochée ou décochée.
    * @param event
    * @param liste de CodeCommon manipulée par l'event (ie selTranscodes ou
    * selSelectedCodes)
    */
   public void onCheckCatalogue$catRows(final ForwardEvent event){
      // on récupère la checkbox associé à l'event
      final Checkbox box = (Checkbox) ((ForwardEvent) event.getOrigin()).getOrigin().getTarget();
      final Catalogue cat = (Catalogue) ((ForwardEvent) event.getOrigin()).getData();

      if(box.isChecked()){
         selectedCatalogues.add(cat);
      }else{
         selectedCatalogues.remove(cat);
      }
   }

   public ServiceWithEtablissementRenderer getServiceRenderer(){
      return serviceRenderer;
   }

   public void setServiceRenderer(final ServiceWithEtablissementRenderer sRenderer){
      this.serviceRenderer = sRenderer;
   }

   public List<String> getNomsServices(){
      return nomsServices;
   }

   public void setNomsServices(final List<String> n){
      this.nomsServices = n;
   }

   public BindingListModelSet<Utilisateur> getUtilisateursData(){
      return utilisateursData;
   }

   public BindingListModelSet<Profil> getProfilsData(){
      return profilsData;
   }

   public Hlayout getUtilisateursArchiveLayout(){
      return utilisateursArchiveLayout;
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codeDiagBox. Cette valeur sera mise en majuscules et une recherche
    * automatique du libelle correspondant est lancée.
    */
   public void onBlur$defautCodeBox(){

      if(!defautCodeBox.getValue().equals("")){
         Clients.showBusy(maladieRow, Labels.getLabel("libelle.recherche.encours"));
         Events.echoEvent("onLaterFindLibelle", self, null);
      }
      defautCodeBox.setValue(defautCodeBox.getValue().toUpperCase().trim());
   }

   public void onLaterFindLibelle(){

      final Set<CodeCommon> codes = new HashSet<>();
      CodeUtils.findCodesInAllTables(defautCodeBox.getValue(), true, true, codes, true,
         SessionUtils.getSelectedBanques(sessionScope));

      if(!codes.isEmpty()){
         defautLibBox.setValue(codes.iterator().next().getLibelle());
      }

      Clients.clearBusy(maladieRow);
   }

   /**
    * @since 2.1
    */
   public void onCheck$utilisateursArchiveBox(){
      profilUtilisateurs.clear();
      // display non-archived users only
      if(banque != null && banque.getBanqueId() != null){
         if(utilisateursArchiveBox.isChecked()){
            profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByBanqueManager(banque, false));
         }else{ // display all of them
            profilUtilisateurs.addAll(ManagerLocator.getProfilUtilisateurManager().findByBanqueManager(banque, null));
         }
      }
   }

   /**
    * Action déclenchée par le clic sur le bouton tout sélectionner
    */
   public void onClick$selectAllUsers(){
      utilisateursData.setSelection(utilisateursData);
   }

   /**
    * Action déclenchée par le clic sur le bouton tout sélectionner
    */
   public void onClick$unselectAllUsers(){
      utilisateursData.clearSelection();
   }

   /********************** Gatsbi ************************/
   public List<Etude> getEtudes(){
      return etudes;
   }

   public boolean getGatsbiSelected(){
      return selectedContexte != null && selectedContexte.getNom() != null && selectedContexte.getNom().equals("GATSBI");
   }

   public boolean getNotGatsbiSelected(){
      return !getGatsbiSelected();
   }

   public String getEtude(){
      if(banque != null && banque.getEtude() != null){
         return banque.getEtude().getTitre();
      }
      return null;
   }

   public Etude getSelectedEtude(){
      return selectedEtude;
   }

   public void setSelectedEtude(final Etude _e){
      this.selectedEtude = _e;
   }
}
