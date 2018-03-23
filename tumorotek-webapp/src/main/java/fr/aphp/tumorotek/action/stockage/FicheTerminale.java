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
package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.CouleurItemRenderer;
import fr.aphp.tumorotek.decorator.EmplacementDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.InvalidParentException;
import fr.aphp.tumorotek.manager.exception.InvalidPositionException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.impl.xml.BoiteContenu;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.stockage.TerminaleNode;

/**
 * Backing bean Fiche détail Terminale.
 *
 * @since 2.0.13 remplacement dessin emplacement Img en div + css pour pouvoir
 * ajouter une Div overlay qui joue un rôle de calque colorant en fonction du
 * statut de l'objet stocké (reserve = bleu-gris, encours = rouge).
 * Les emplacements ne sortent plus à l'impression navigateur, mais la terminale
 * peut être imprimée avec une fonctionalité dédiée avec Actions > Imprimer.
 *
 * @since 2.1 nettoyage mode déplacement + selection depuis full-rack scan
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class FicheTerminale extends AbstractFicheCombineStockageController
{

   private final Log log = LogFactory.getLog(FicheTerminale.class);

   private static final long serialVersionUID = -6631022113321896477L;

   private Component[] objRowsComponents;
   // private Button deplacer;
   private Menubar menuBarActionsTerminale;
   private Menubar menuBarSelectionsTerminale;
   private Menubar menuBar;
   private Menuitem deplacer;
   private Menuitem contenu;
   private Menuitem deplacerEmplacements;
   private Menuitem destockerEmplacements;
   private Menuitem afficherEchantillons;
   private Menuitem afficherDerives;

   /**
    * Static Components pour le mode static.
    */
   private Label nomLabel;
   private Label aliasLabel;
   private Label typeLabel;
   private Label entiteReserveeLabel;
   private Label banqueReserveeLabel;
   private Row entiteReserveeRow;
   private Row banqueReserveeRow;
   private Row rowNomAlias;
   private Row rowType;
   private Row rowNbPlaces;
   private Row rowNewTerminale;
   protected Div modeleBoite;
   protected Row rowViewBoite;
   private Image couleurTerminaleImg;

   /**
    * Editable components : mode d'édition ou de création.
    */
   private Label nomRequired;
   private Textbox nomBox;
   private Textbox aliasBox;
   private Listbox entiteBox;
   private Listbox banqueBox;
   private Listbox typesBox;
   private Row rowEntiteEdit;
   private Row rowBanqueEdit;
   private Row rowNumerotation;
   private Listbox couleurTerminaleBox;

   /**
    * Compnents pour le mode déplacement.
    */
   private Component[] objDeplacementComponents;
   private Row rowEmplacementTitle1;
   private Row rowAdresseActuelle;
   private Row rowPositionActuelle;
   private Row rowEmplacementTitle2;
   private Row rowAdresseDestination;
   private Row rowPositionDestination;
   private Row rowContenuDestination;
   private Row rowErreurDestination;
   private Row rowNomDepart;
   private Row rowNomDestination;
   private Button validateDeplacement;
   private Button revertDeplacement;
   private Label adresseDestinationLabel;
   private Label positionDestinationLabel;
   private Label contenuDestinationLabel;
   private Textbox nomDepartBox;
   private Textbox nomDestinationBox;

   /**
    * Compnents pour le mode contenu.
    */
   private Component[] objContenuComponents;
   private Row rowContenuInfo1;
   private Button validateContenu;
   private Button revertContenu;
   private Row rowHistorique;
   private Grid gridHistorique;
   private Row rowHistoriqueTitle;

   /**
    * Objets Principaux.
    */
   protected Terminale terminale;
   private Enceinte enceinte;
   private Integer position;
   private TerminaleNode terminaleDestination;
   private Terminale terminaleAEchanger;

   /**
    * Associations.
    */
   private List<Banque> banques = new ArrayList<>();
   private Banque selectedBanque;
   private List<Entite> entites = new ArrayList<>();
   private Entite selectedEntite;
   private List<TerminaleType> types = new ArrayList<>();
   private TerminaleType selectedTerminaleType;
   private List<TerminaleNumerotation> numerotations = new ArrayList<>();
   private TerminaleNumerotation selectedTerminaleNumerotation;
   private List<Emplacement> emplacements = new ArrayList<>();
   private List<EmplacementDecorator> emplacementDecos = new ArrayList<>();
   private List<Emplacement> movedEmplacements = new ArrayList<>();
   private CouleurItemRenderer couleurRenderer = new CouleurItemRenderer();
   private ListModelList<Couleur> couleurs = new ListModelList<>();
   private Couleur selectedCouleur;
   private HashMap<TKStockableObject, Emplacement> emplForRetours = new HashMap<>();

   /**
    * Variables formulaire.
    */
   private String entiteReservee;
   private String banqueReservee;
   private String adresseActuelle = "";
   private String adresseDestination = "";
   private Integer positionDestination;
   private String contenuDestination = "";
   protected String adrlTerminale = "";
   private List<Div> imagesEmplacements = new ArrayList<>();
   private static String imageSrc = "/images/icones/emplacements/emplacement";
   private static String colorSrc = "/images/icones/boites/big_pastille";

   private Emplacement currentSelection;
   private Div currentSelectedImg = null;

   //@since 2.1
   private TKScanTerminaleDTO currScanTerminaleDTO;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.terminale");

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.nomLabel, this.aliasLabel, this.entiteReserveeLabel, this.banqueReserveeLabel,
         this.modeleBoite, this.rowViewBoite, this.menuBar, this.couleurTerminaleImg, this.incidentsList});

      setObjBoxsComponents(new Component[] {this.nomBox, this.aliasBox, this.entiteBox, this.banqueBox, this.couleurTerminaleBox,
         this.incidentsListEdit});

      setRequiredMarks(new Component[] {this.nomRequired});

      this.objRowsComponents = new Component[] {this.rowNomAlias, this.rowType, this.rowNbPlaces};

      this.objDeplacementComponents = new Component[] {this.rowEmplacementTitle1, this.rowAdresseActuelle,
         this.rowPositionActuelle, this.rowEmplacementTitle2, this.rowAdresseDestination, this.rowPositionDestination,
         this.rowContenuDestination, this.validateDeplacement, this.revertDeplacement, this.rowNomDepart, this.rowNomDestination};

      this.objContenuComponents = new Component[] {this.rowContenuInfo1, this.validateContenu, this.revertContenu,
         this.rowHistorique, this.rowHistoriqueTitle};

      drawActionsForTerminale();

      getBinder().loadAll();
   }

   @Override
   public Terminale getObject(){
      return terminale;
   }

   @Override
   public void setObject(final TKdataObject t){
      this.terminale = (Terminale) t;
      selectedCouleur = null;
      currScanTerminaleDTO = null;

      setStockageObj(terminale);

      if(terminale.getTerminaleId() != null){

         initIncidents(ManagerLocator.getIncidentManager().findByTerminaleManager(terminale));

         StringBuffer sb = new StringBuffer();
         if(this.terminale.getEntite() != null){
            sb.append(Labels.getLabel("terminale.entite.reservee"));
            sb.append(" ");
            sb.append(this.terminale.getEntite().getNom());
            sb.append("s.");
         }
         entiteReservee = sb.toString();

         sb = new StringBuffer();
         if(this.terminale.getBanque() != null){
            sb.append(Labels.getLabel("terminale.banque.reservee"));
            sb.append(" ");
            sb.append(this.terminale.getBanque().getNom());
            sb.append(".");
         }
         banqueReservee = sb.toString();

         selectedTerminaleType = this.terminale.getTerminaleType();
         selectedTerminaleNumerotation = this.terminale.getTerminaleNumerotation();

         if(this.terminale != null && this.terminale.getTerminaleId() != null){
            adrlTerminale = ManagerLocator.getEmplacementManager().getTerminaleAdrlManager(terminale);
         }

         selectedCouleur = this.terminale.getCouleur();
      }

      if(!terminale.equals(new Terminale())){
         enceinte = this.terminale.getEnceinte();
         position = this.terminale.getPosition();
      }

      // clone er reload
      super.setObject(terminale);
   }

   @Override
   public void setNewObject(){
      setObject(new Terminale());
      super.setNewObject();
   }

   @Override
   public void cloneObject(){
      setClone(this.terminale.clone());
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public StockageController getObjectTabController(){
      return (StockageController) super.getObjectTabController();
   }

   @Override
   public void switchToCreateMode(){
      // Initialisation du mode d'édition (listes, valeurs formulaires...)
      initEditableMode();

      super.switchToCreateMode();

      for(int i = 0; i < objRowsComponents.length; i++){
         objRowsComponents[i].setVisible(true);
         rowNewTerminale.setVisible(false);
      }
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objContenuComponents.length; i++){
         objContenuComponents[i].setVisible(false);
      }

      this.rowEntiteEdit.setVisible(true);
      this.rowBanqueEdit.setVisible(true);
      this.menuBarActionsTerminale.setVisible(false);
      this.menuBarSelectionsTerminale.setVisible(false);
      typeLabel.setVisible(false);
      typesBox.setVisible(true);
      rowNumerotation.setVisible(true);

      groupIncidents.setVisible(false);
      incidentsListEdit.setVisible(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){

      super.switchToStaticMode(this.terminale.getTerminaleId() == null);

      if(this.terminale.getTerminaleId() == null){
         this.menuBar.setVisible(false);
      }

      // on cache les formulaires pour le contenu et le déplacement
      // de la boite
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objContenuComponents.length; i++){
         objContenuComponents[i].setVisible(false);
      }

      groupIncidents.setVisible(true);

      // on vide la modélisation de la boite
      modeleBoite.getChildren().clear();

      // si c'est une nouvelle terminale
      if(this.terminale.getTerminaleId() == null){
         // on cache les infos
         for(int i = 0; i < objRowsComponents.length; i++){
            objRowsComponents[i].setVisible(false);
         }
         // on cache les emplacements
         modeleBoite.setVisible(false);
         // on enlève les actions
         this.menuBarActionsTerminale.setVisible(false);
         this.menuBarSelectionsTerminale.setVisible(false);
         rowNewTerminale.setVisible(true);
      }else{
         // on affiche les infos
         for(int i = 0; i < objRowsComponents.length; i++){
            objRowsComponents[i].setVisible(true);
         }

         rowNewTerminale.setVisible(false);
         addNewC.setVisible(false);

         // si la terminale n'est pas modifiable : elle est
         // réservée pour une autre banque
         if(this.terminale.getBanque() != null
            && !SessionUtils.getSelectedBanques(sessionScope).get(0).equals(this.terminale.getBanque())){
            // on cache les emplacements
            modeleBoite.setVisible(false);
            // on enlève toutes les actions
            this.menuBarActionsTerminale.setVisible(false);
            this.menuBarSelectionsTerminale.setVisible(false);
            editC.setVisible(false);
            deleteC.setVisible(false);
         }else{
            modeleBoite.setVisible(true);
            initModelisation();
            this.menuBarActionsTerminale.setVisible(true);
            this.menuBarSelectionsTerminale.setVisible(true);
         }
      }

      if(this.terminale.getEntite() != null){
         entiteReserveeRow.setVisible(true);
      }else{
         entiteReserveeRow.setVisible(false);
      }

      if(this.terminale.getBanque() != null){
         banqueReserveeRow.setVisible(true);
      }else{
         banqueReserveeRow.setVisible(true);
      }

      this.rowEntiteEdit.setVisible(false);
      this.rowBanqueEdit.setVisible(false);
      rowNumerotation.setVisible(false);
      typeLabel.setVisible(true);
      typesBox.setVisible(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      super.switchToEditMode();

      for(int i = 0; i < objRowsComponents.length; i++){
         objRowsComponents[i].setVisible(true);
         rowNewTerminale.setVisible(false);
      }
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objContenuComponents.length; i++){
         objContenuComponents[i].setVisible(false);
      }

      this.rowEntiteEdit.setVisible(true);
      this.rowBanqueEdit.setVisible(true);
      this.menuBarActionsTerminale.setVisible(false);
      this.menuBarSelectionsTerminale.setVisible(false);
      rowNumerotation.setVisible(true);
      // si la terminale est vide on peut changer son type
      if(!ManagerLocator.getTerminaleManager().isUsedObjectManager(terminale)){
         typeLabel.setVisible(false);
         typesBox.setVisible(true);
      }else{
         typeLabel.setVisible(true);
         typesBox.setVisible(false);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   /**
    * Change mode de la fiche en mode déplacement.
    */
   public void switchToDeplacerMode(){
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(true);
      }
      for(int i = 0; i < objContenuComponents.length; i++){
         objContenuComponents[i].setVisible(false);
      }

      groupIncidents.setVisible(false);
      incidentsList.setVisible(false);

      deleteC.setVisible(false);
      this.menuBarActionsTerminale.setVisible(false);
      this.menuBarSelectionsTerminale.setVisible(false);
      editC.setVisible(false);
      this.rowViewBoite.setVisible(false);
      rowNomDestination.setVisible(false);
      menuBar.setVisible(false);

      // adresse de départ
      adresseActuelle = ManagerLocator.getEmplacementManager().getTerminaleAdrlManager(terminale);

      emplForRetours.clear();

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode déplacement.
    */
   public void switchToContenuMode(){
      gridHistorique.getRows().getChildren().clear();

      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objContenuComponents.length; i++){
         objContenuComponents[i].setVisible(true);
      }

      groupIncidents.setVisible(false);
      incidentsList.setVisible(false);

      deleteC.setVisible(false);
      this.menuBarActionsTerminale.setVisible(false);
      this.menuBarSelectionsTerminale.setVisible(false);
      editC.setVisible(false);
      menuBar.setVisible(false);

      for(int i = 0; i < getImagesEmplacements().size(); i++){
         final Div img = getImagesEmplacements().get(i);
         final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");

         if(!deco.getVide()){
            img.setDraggable("true");
            img.setDroppable("true");
            img.addForward("onDrop", self, "onDropImage", img);
         }else{
            img.setDroppable("true");
            img.addForward("onDrop", self, "onDropImage", img);
         }
      }

      movedEmplacements = new ArrayList<>();

      getBinder().loadComponent(self);
   }

   @Override
   public void clearData(){
      clearIncidents();
      clearConstraints();
      clearDestination();
      super.clearData();
   }

   @Override
   public void createNewObject(){

      try{
         setEmptyToNulls();
         setFieldsToUpperCase();
         this.terminale.setPosition(position);

         // update de l'objet
         ManagerLocator.getTerminaleManager().createObjectManager(terminale, this.enceinte, selectedTerminaleType, selectedBanque,
            selectedEntite, selectedTerminaleNumerotation, selectedCouleur, SessionUtils.getLoggedUser(sessionScope));
      }catch(final Exception e){
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getTerminaleManager().isUsedObjectManager(getObject());

      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      setCascadable(false);
      if(isUsed){
         setDeleteMessage(Labels.getLabel("terminale.deletion.isUsed"));
      }
      setDeletable(!isUsed);
      setFantomable(!isUsed);
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getTerminaleManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope));
   }

   @Override
   public void onClick$addNewC(){
      this.switchToCreateMode();
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$createC(){
      Clients.showBusy(Labels.getLabel("terminale.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onClick$editC(){
      if(this.terminale.getTerminaleId() != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   public void onClick$revertDeplacement(){
      revertObject();
      clearConstraints();
      clearDestination();
      getBinder().loadComponent(self);
      switchToStaticMode();
      rowErreurDestination.setVisible(false);
      getObjectTabController().getListeStockages().switchToNormalMode();
   }

   public void onClick$revertContenu(){
      revertObject();
      clearConstraints();
      getBinder().loadComponent(self);
      switchToStaticMode();
   }

   @Override
   public void onClick$validateC(){
      Clients.showBusy(Labels.getLabel("terminale.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onClick$validateDeplacement(){
      Clients.showBusy(Labels.getLabel("terminale.deplacement.encours"));
      Events.echoEvent("onLaterDeplacement", self, null);
   }

   public void onClick$validateContenu(){
      Clients.showBusy(Labels.getLabel("terminale.contenu.encours"));
      Events.echoEvent("onLaterContenu", self, null);
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ nomBox. Cette
    * valeur sera mise en majuscules.
    */
   public void onBlur$nomBox(){
      nomBox.setValue(nomBox.getValue().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.terminale.getNom() != null){
         this.terminale.setNom(this.terminale.getNom().toUpperCase().trim());
      }
   }

   /**
    * Passe en mode de déplacement de l'enceinte.
    */
   public void onClick$deplacer(){
      if(this.terminale.getTerminaleId() != null){
         cloneObject();
         switchToDeplacerMode();
         getObjectTabController().getListeStockages().switchToDeplacementMode();
      }
   }

   @Override
   public void setEmptyToNulls(){
      if(this.terminale.getAlias().equals("")){
         this.terminale.setAlias(null);
      }

      selectedCouleur = null;
      if(!couleurs.getSelection().isEmpty()){
         final Couleur c = couleurs.getSelection().iterator().next();
         if(c != null && c.getCouleur() != null){ // null si premier item de la liste (vide)
            selectedCouleur = c;
         }
      }
      //		if (couleurTerminaleBox.getSelectedItem() != null) {
      //			if (((Couleur) couleurTerminaleBox.getSelectedItem().getValue())
      //					.getCouleurId() != null) {
      //				selectedCouleur = (Couleur) couleurTerminaleBox
      //						.getSelectedItem().getValue();
      //			}
      //		}
   }

   @Override
   public void updateObject(){
      try{

         // on remplit la terminale en fonction des champs nulls
         setEmptyToNulls();
         setFieldsToUpperCase();

         // update de l'objet
         ManagerLocator.getTerminaleManager().updateObjectManager(terminale, this.terminale.getEnceinte(), selectedTerminaleType,
            selectedBanque, selectedEntite, selectedTerminaleNumerotation, selectedCouleur, getIncidents(),
            SessionUtils.getLoggedUser(sessionScope), null);

         super.updateObject();

      }catch(final Exception e){
         throw new RuntimeException(e.getMessage());
      }

      if(!((Terminale) getClone()).getNom().equals(this.terminale.getNom())){
         getObjectTabController().refreshListesEchantillonsDerives();
      }
   }

   protected List<String> deplacerTerminale(){
      final List<String> errorMsg = new ArrayList<>();
      // on récupère l'emplacement de départ
      final Enceinte ep = terminale.getEnceinte();
      final Integer p = terminale.getPosition();

      try{

         // si la destination est vide
         if(terminaleDestination != null && terminaleDestination.isVide()){
            terminale.setPosition(positionDestination);
            terminaleAEchanger = null;
            // update de l'objet
            final List<OperationType> ops = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Deplacement", true);
            ManagerLocator.getTerminaleManager().updateObjectManager(terminale, terminaleDestination.getTerminale().getEnceinte(),
               terminale.getTerminaleType(), terminale.getBanque(), terminale.getEntite(), terminale.getTerminaleNumerotation(),
               terminale.getCouleur(), null, SessionUtils.getLoggedUser(sessionScope), ops);
         }else{
            // on échange des emplacements des 2 terminales
            terminaleAEchanger = terminaleDestination.getTerminale().clone();
            terminale.setEnceinte(terminaleAEchanger.getEnceinte());
            terminale.setPosition(terminaleAEchanger.getPosition());

            terminaleAEchanger.setEnceinte(ep);
            terminaleAEchanger.setPosition(p);

            ManagerLocator.getTerminaleManager().echangerDeuxTerminalesManager(terminale, terminaleAEchanger,
               SessionUtils.getLoggedUser(sessionScope));
         }
      }catch(final ValidationException ve){
         errorMsg.add("- Erreur lors de la validation.");
      }catch(final DoublonFoundException dfe){
         errorMsg.add("- Doublon détecté lors du " + "déplacement de l'enceinte terminale.");
      }catch(final RequiredObjectIsNullException re){
         errorMsg.add("Objet obligatoire manquant lors du déplacement " + "de l'enceinte terminale.");
      }catch(final InvalidParentException ipe){
         errorMsg.add("- Erreur sur le " + "parent de l'enceinte terminale.");
      }catch(final InvalidPositionException ipose){
         errorMsg.add("- Erreur sur la " + "position de l'enceinte terminale.");
      }catch(final UsedPositionException upe){
         errorMsg.add("- Erreur sur la " + "position de l'enceinte terminale.");
      }

      // s'il y a des erreurs, on fait apparaître une fenêtre contenant
      // la liste de celles-ci
      if(errorMsg.size() > 0){
         // ferme wait message
         Clients.clearBusy();

         final StringBuffer sb = new StringBuffer();
         for(int i = 0; i < errorMsg.size(); i++){
            sb.append(errorMsg.get(i));
            if(i < errorMsg.size() - 1){
               sb.append("\n");
            }
         }
         Messagebox.show(sb.toString(), "Error", Messagebox.OK, Messagebox.ERROR);

         // en cas d'erreur, on annule les déplacements
         if(terminaleAEchanger != null){
            terminaleAEchanger.setEnceinte(terminale.getEnceinte());
            terminaleAEchanger.setPosition(terminale.getPosition());
         }
         terminale.setEnceinte(ep);
         terminale.setPosition(p);
      }else{
         getObjectTabController().refreshListesEchantillonsDerives();
      }

      return errorMsg;
   }

   /**
    * Méthode qui sauvegarde les modifications apportées sur le contenu de la
    * boite.
    * 
    * @return Les erreurs éventuelles lors de la sauvegarde.
    */
   protected boolean changeContenu(){

      try{

         ManagerLocator.getEmplacementManager().deplacerMultiEmplacementsManager(movedEmplacements,
            SessionUtils.getLoggedUser(sessionScope));

         for(int i = 0; i < movedEmplacements.size(); i++){
            final Emplacement empl = movedEmplacements.get(i);
            if(empl.getEntite() != null && empl.getObjetId() != null && empl.getEntite().getNom().equals("Echantillon")){
               final Echantillon echan = ManagerLocator.getEchantillonManager().findByIdManager(empl.getObjetId());
               // on vérifie que l'on retrouve bien la page
               // contenant la liste
               // des échantillons
               if(getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){
                  if(getObjectTabController().getEchantillonController() != null){
                     // update de l'échantillon dans la liste
                     getObjectTabController().getEchantillonController().getListe().updateObjectGridListFromOtherPage(echan,
                        false);
                  }
               }
            }else if(empl.getEntite() != null && empl.getObjetId() != null && empl.getEntite().getNom().equals("ProdDerive")){
               final ProdDerive derive = ManagerLocator.getProdDeriveManager().findByIdManager(empl.getObjetId());
               // on vérifie que l'on retrouve bien la page
               // contenant la liste
               // des dérivés
               if(getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")){
                  if(getObjectTabController().getProdDeriveController() != null){
                     // update du dérivé dans la liste
                     getObjectTabController().getProdDeriveController().getListe().updateObjectGridListFromOtherPage(derive,
                        false);
                  }
               }
            }
         }
      }catch(final Exception e){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }finally{
         Clients.clearBusy();
      }

      return true;
   }

   @Override
   public void onLaterCreate(){

      try{
         createNewObject();

         // on vérifie que l'on retrouve bien la page contenant la liste
         // des stockages
         if(getObjectTabController().getListeStockages() != null){

            getObjectTabController().getListeStockages().updateTerminale(terminale, true);
         }

         setObject(this.terminale);
         switchToStaticMode();
      }catch(final RuntimeException re){
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         Clients.clearBusy();
      }
   }

   public void onLaterUpdate(){

      try{
         updateObject();

         // on vérifie que l'on retrouve bien la page contenant la liste
         // des stockages
         if(getObjectTabController().getListeStockages() != null){

            getObjectTabController().getListeStockages().updateTerminale(terminale, false);
         }

         setObject(this.terminale);
         switchToStaticMode();
      }catch(final RuntimeException re){
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         Clients.clearBusy();
      }
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         removeObject((String) event.getData());

         this.terminale = new Terminale();
         this.terminale.setEnceinte(enceinte);
         this.terminale.setPosition(position);
         cloneObject();
         // on vérifie que l'on retrouve bien la page contenant la liste
         // des stockages
         if(getObjectTabController().getListeStockages() != null){
            getObjectTabController().getListeStockages().deleteTerminale(enceinte, position);
         }

         setObject(this.terminale);

         clearData();

         switchToStaticMode();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   /**
    * Méthode gérant le déplacement d'une enceinte terminale.
    */
   public void onLaterDeplacement(){

      // s'il n'y a pas d'erreurs lors de l'update
      if(!getObjStatutIncompatibleForRetour(prepareObjectsAndEmplacements(), null) && deplacerTerminale().size() == 0){

         // on vérifie que l'on retrouve bien la page contenant la liste
         // des stockages
         if(getObjectTabController().getListeStockages() != null){
            // echange des enceintes
            getObjectTabController().getListeStockages().deplacerDeuxTerminales(terminale, terminaleAEchanger);
         }

         setObject(this.terminale);

         clearDestination();

         switchToStaticMode();

         Clients.clearBusy();

         // création des retours
         createRetoursApresDeplacementTerminale();

      }else{
         Clients.clearBusy();
      }
   }

   private void clearDestination(){
      // clean destination
      setTerminaleDestination(null);
      setTerminaleAEchanger(null);
      positionDestination = null;
      adresseDestination = null;
      contenuDestination = null;
      validateDeplacement.setDisabled(true);
   }

   /**
    * Propose à l'utilisateur puis crée les retours associés au déplacement de
    * la terminale.
    */
   public void createRetoursApresDeplacementTerminale(){
      // on calcule le nb d'emplacements non vides concernés par
      // ce déplacement
      //		Integer nb = 0;
      //		Iterator<String> it = emplForRetours.keySet().iterator();
      //		while (it.hasNext()) {
      //			String key = it.next();
      //			nb += emplForRetours.get(key).size();
      //		}
      if(!emplForRetours.isEmpty()){
         // on demande à l'utilisateur s'il souhaite
         // créer des retours
         if(askForRetoursCreation(emplForRetours.size(), null, null, new HashMap<TKStockableObject, Emplacement>(), null,
            ManagerLocator.getOperationTypeManager().findByNomLikeManager("Creation", true).get(0))){
            //				List<TKStockableObject> objs = new ArrayList<TKStockableObject>();
            //				Hashtable<TKStockableObject, Emplacement> hashEmps = 
            //						new Hashtable<TKStockableObject, Emplacement>();
            //
            //				// on va récupérer les objets concernés par ce
            // déplacement et reconstituer leur ancienne
            // adresse
            //				it = emplForRetours.keySet().iterator();
            //				while (it.hasNext()) {
            //					String key = it.next();
            //					List<Emplacement> empls = emplForRetours.get(key);
            //
            //					for (int i = 0; i < empls.size(); i++) {
            //						TKStockableObject obj = null;
            //
            //						if (empls.get(i).getEntite().getNom()
            //								.equals("Echantillon")) {
            //							obj = ManagerLocator.getEchantillonManager()
            //									.findByIdManager(empls.get(i).getObjetId());
            //						} else if (empls.get(i).getEntite().getNom()
            //								.equals("ProdDerive")) {
            //							obj = ManagerLocator.getProdDeriveManager()
            //									.findByIdManager(empls.get(i).getObjetId());
            //						}
            //
            //						objs.add(obj);
            ////						StringBuffer sb = new StringBuffer();
            ////						sb.append(key);
            ////						sb.append(".");
            ////						// sb.append(empls.get(i).getPosition());
            ////						sb.append(ManagerLocator.getEmplacementManager()
            ////								.getNumerotationByPositionAndTerminaleManager(
            ////										empls.get(i).getPosition(),
            ////										empls.get(i).getTerminale()));
            //						hashEmps.put(obj, empls.get(i));
            //					}
            //				}

            // ouverture de la modale
            openRetourFormModale(null, true, null, null, new ArrayList<>(emplForRetours.keySet()),
               emplForRetours, null, null, null, null, Labels.getLabel("ficheRetour.deplacement"), null);
         }
      }
   }

   /**
    * Méthode gérant le contenu d'une enceinte terminale.
    */
   public void onLaterContenu(){

      // s'il n'y a pas d'erreurs lors de l'update
      if(!getObjStatutIncompatibleForRetour(new ArrayList<>(emplForRetours.keySet()), null) && changeContenu()){

         setObject(this.terminale);
         switchToStaticMode();

         // Clients.clearBusy();
         // création des retours
         createRetoursApresDeplacementTerminale();

      }else{
         Clients.clearBusy();
      }
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){

      banques = ManagerLocator.getUtilisateurManager()
         .getAvailableBanquesByPlateformeManager(SessionUtils.getLoggedUser(sessionScope), SessionUtils.getCurrentPlateforme());
      banques.add(0, null);
      selectedBanque = this.terminale.getBanque();

      entites = new ArrayList<>();
      entites.add(null);
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
      selectedEntite = this.terminale.getEntite();

      types = ManagerLocator.getTerminaleTypeManager().findAllObjectsManager();
      if(this.terminale.getTerminaleType() != null){
         selectedTerminaleType = this.terminale.getTerminaleType();
      }else{
         selectedTerminaleType = types.get(0);
      }

      numerotations = ManagerLocator.getTerminaleNumerotationManager().findAllObjectsManager();
      if(this.terminale.getTerminaleNumerotation() != null){
         selectedTerminaleNumerotation = this.terminale.getTerminaleNumerotation();
      }else{
         selectedTerminaleNumerotation = numerotations.get(0);
      }

      // init des couleurs
      if(couleurs.isEmpty()){
         couleurs.add(new Couleur());
         couleurs.addAll(ManagerLocator.getCouleurManager().findAllObjectsManager());
      }
      couleurTerminaleBox.setModel(couleurs);
      couleurs.addToSelection(selectedCouleur);
      // couleurTerminaleBox.setSelectedIndex(couleurs.indexOf(selectedCouleur));

      // clearIncidents();

   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans les
    * contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(aliasBox);
      Clients.clearWrongValue(nomDepartBox);
      Clients.clearWrongValue(nomDestinationBox);
   }

   /**
    * Méthode appelée lorsque l'utilisateur choisit un emplacement de
    * destination pour l'enceinte terminale.
    * 
    * @param node
    *            TerminaleNode représentant la destination de l'enceinte.
    */
   public void definirTerminaleDestination(final TerminaleNode node){
      // si le node est vide, c'est que la destination n'est pas valide
      if(node != null){
         validateDeplacement.setDisabled(false);
         terminaleDestination = node;

         positionDestination = terminaleDestination.getTerminale().getPosition();
         positionDestinationLabel.setValue(positionDestination.toString());

         // si la destination n'est pas vide, on récupère son contenu pour
         // l'afficher
         if(!terminaleDestination.isVide()){
            contenuDestination = terminaleDestination.getTerminale().getNom();
            adresseDestination =
               ManagerLocator.getEmplacementManager().getTerminaleAdrlManager(terminaleDestination.getTerminale());
            rowNomDestination.setVisible(true);
            getBinder().loadAttribute(self.getFellow("nomDestinationBox"), "value");

         }else{
            // sinon on affiche un contenu vide
            contenuDestination = Labels.getLabel("stockage.position.libre");
            final StringBuffer sb = new StringBuffer();
            sb.append(ManagerLocator.getEnceinteManager().getAdrlManager(terminaleDestination.getTerminale().getEnceinte()));
            sb.append(".");
            sb.append(terminaleDestination.getTerminale().getEnceinte().getNom());
            adresseDestination = sb.toString();

            rowNomDestination.setVisible(false);
         }
         contenuDestinationLabel.setValue(contenuDestination);
         adresseDestinationLabel.setValue(adresseDestination);

         rowErreurDestination.setVisible(false);
      }else{
         // on affiche un message d'erreur
         validateDeplacement.setDisabled(true);
         adresseDestination = null;
         adresseDestinationLabel.setValue(adresseDestination);
         positionDestination = null;
         positionDestinationLabel.setValue(null);
         contenuDestination = null;
         contenuDestinationLabel.setValue(contenuDestination);
         rowNomDestination.setVisible(false);
         rowErreurDestination.setVisible(true);
      }
   }

   /**
    * Lors du clic sur une image, on affiche la fenêtre permettant d'afficher
    * l'objet se trouvant à cet emplacement.
    * 
    * @param e
    *            Event : Clic sur l'image.
    */
   public void onClickImage(final Event e){

      if(e.getData() != null){
         final Div img = (Div) e.getData();
         final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");

         if(!deco.getVide()){
            if(deco.getEmplacement().getEntite().getNom().equals("Echantillon")){
               final Echantillon echan = (Echantillon) ManagerLocator.getEntiteManager()
                  .findObjectByEntiteAndIdManager(deco.getEmplacement().getEntite(), deco.getEmplacement().getObjetId());

               if(echan != null){
                  if(!echan.getBanque().getArchive()){
                     if(getDroitEchantillonConsultation(echan)){
                        switchToFicheObject(echan);
                     }else{
                        Messagebox.show(Labels.getLabel("cederObjet.acces.interdit"),
                           Labels.getLabel("cederObjet.acces" + ".interdit.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                     }
                  }else{ // banque archivée
                     Messagebox.show(Labels.getLabel("banque.archived.objet.warning"),
                        Labels.getLabel("cederObjet.acces.interdit"), Messagebox.OK, Messagebox.EXCLAMATION);
                  }
               }

            }else if(deco.getEmplacement().getEntite().getNom().equals("ProdDerive")){
               final ProdDerive derive = (ProdDerive) ManagerLocator.getEntiteManager()
                  .findObjectByEntiteAndIdManager(deco.getEmplacement().getEntite(), deco.getEmplacement().getObjetId());

               if(derive != null){
                  if(!derive.getBanque().getArchive()){
                     if(getDroitProdDeriveConsultation(derive)){
                        switchToFicheObject(derive);
                     }else{
                        Messagebox.show(Labels.getLabel("cederObjet.acces.interdit"),
                           Labels.getLabel("cederObjet.acces" + ".interdit.title"), Messagebox.OK, Messagebox.EXCLAMATION);
                     }
                  }else{ // banque archivée
                     Messagebox.show(Labels.getLabel("banque.archived.objet.warning"),
                        Labels.getLabel("cederObjet.acces.interdit"), Messagebox.OK, Messagebox.EXCLAMATION);
                  }
               }
            }
         }
      }

   }

   /**
    * Affiche la fiche de l'objet sur lequel l'utilisateur vient de cliquer.
    * 
    * @param obj
    */
   public void switchToFicheObject(final Object obj){
      if(obj instanceof Echantillon){

         if(!SessionUtils.getSelectedBanques(sessionScope).contains(((Echantillon) obj).getBanque())){
            getMainWindow().updateSelectedBanque(((Echantillon) obj).getBanque());
         }

         final EchantillonController tabController =
            (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);

         tabController.switchToFicheStaticMode((Echantillon) obj);
      }else if(obj instanceof ProdDerive){
         final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

         // change la banque au besoin
         if(!SessionUtils.getSelectedBanques(sessionScope).contains(((ProdDerive) obj).getBanque())){
            getMainWindow().updateSelectedBanque(((ProdDerive) obj).getBanque());
         }

         tabController.switchToFicheStaticMode((ProdDerive) obj);
      }
   }

   /**
    * Renvoie true si l'utilisateur loggé a le droit d'accéder à la fiche de
    * cet échantillon.
    * 
    * @param event
    * @return
    */
   public Boolean getDroitEchantillonConsultation(final Echantillon echan){
      boolean acces = false;

      final Banque bk = echan.getBanque();
      final Utilisateur user = (Utilisateur) sessionScope.get("User");

      Set<Plateforme> pfs = new HashSet<>();

      // si l'utilisateur est super admin, il a accès à toutes
      // les pfs
      if(user.isSuperAdmin()){
         final List<Plateforme> tmp = ManagerLocator.getPlateformeManager().findAllObjectsManager();
         pfs.addAll(tmp);
      }else{
         pfs = ManagerLocator.getUtilisateurManager().getPlateformesManager(user);
      }
      if(pfs.contains(bk.getPlateforme())){
         acces = true;
      }else{
         // on récupère le profil du user pour la banque
         // sélectionnée
         final List<ProfilUtilisateur> profils =
            ManagerLocator.getProfilUtilisateurManager().findByUtilisateurBanqueManager(user, bk);

         if(profils.size() > 0){
            final Profil profil = profils.get(0).getProfil();
            // si l'utilisateur est admin pour la banque
            if(profil.getAdmin()){
               acces = true;
            }else{
               final List<OperationType> operations =
                  ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "Echantillon");
               final OperationType opeation =
                  ManagerLocator.getOperationTypeManager().findByNomLikeManager("Consultation", true).get(0);
               if(operations.contains(opeation)){
                  acces = true;
               }
            }
         }
      }

      return acces;
   }

   /**
    * Renvoie true si l'utilisateur loggé a le droit d'accéder à la fiche de ce
    * dérivé.
    * 
    * @param event
    * @return
    * @version 2.1
    */
   public Boolean getDroitProdDeriveConsultation(final ProdDerive derive){
      boolean acces = false;

      final Banque bk = derive.getBanque();
      final Utilisateur user = (Utilisateur) sessionScope.get("User");

      Set<Plateforme> pfs = new HashSet<>();

      // si l'utilisateur est super admin, il a accès à toutes
      // les pfs
      // @since 2.1
      if(user.isSuperAdmin()){
         final List<Plateforme> tmp = ManagerLocator.getPlateformeManager().findAllObjectsManager();
         pfs.addAll(tmp);
      }else{
         pfs = ManagerLocator.getUtilisateurManager().getPlateformesManager(user);
      }
      if(pfs.contains(bk.getPlateforme())){
         acces = true;
      }else{
         // on récupère le profil du user pour la banque
         // sélectionnée
         final List<ProfilUtilisateur> profils =
            ManagerLocator.getProfilUtilisateurManager().findByUtilisateurBanqueManager(user, bk);

         if(profils.size() > 0){
            final Profil profil = profils.get(0).getProfil();
            // si l'utilisateur est admin pour la banque
            if(profil.getAdmin()){
               acces = true;
            }else{
               final List<OperationType> operations =
                  ManagerLocator.getDroitObjetManager().getOperationsByProfilEntiteManager(profil, "ProdDerive");
               final OperationType opeation =
                  ManagerLocator.getOperationTypeManager().findByNomLikeManager("Consultation", true).get(0);
               if(operations.contains(opeation)){
                  acces = true;
               }
            }
         }
      }

      return acces;
   }

   /**
    * Gestion le contenu par glisser déposer: enregistre de dêpot.
    * 
    * @param e
    * @version 2.0.13
    */
   public void onDropImage(final DropEvent e){
      e.getTarget();
      if(e.getTarget() != null){

         final EmplacementDiv imgDep = (EmplacementDiv) e.getDragged();
         final EmplacementDiv imgDest = (EmplacementDiv) e.getTarget();
         final EmplacementDecorator decoDep = (EmplacementDecorator) imgDep.getAttribute("empDeco");
         final EmplacementDecorator decoDest = (EmplacementDecorator) imgDest.getAttribute("empDeco");

         // création du message
         final StringBuffer sb = new StringBuffer();
         sb.append(Labels.getLabel("validation.drop.message"));
         sb.append(" '");
         sb.append(decoDep.getCode());
         sb.append("' ");
         sb.append(Labels.getLabel("validation.drop.emplacement.dep"));
         sb.append(" '");
         sb.append(decoDep.getAdrl());
         sb.append("' ");
         sb.append(Labels.getLabel("validation.drop.emplacement.dest"));
         sb.append(" '");
         sb.append(decoDest.getAdrl());
         sb.append("' ?");

         if((Messagebox.show(sb.toString(), Labels.getLabel("validation.drop.title"), Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION) == Messagebox.YES)){

            final Emplacement empDep = decoDep.getEmplacement();
            final Emplacement empDest = decoDest.getEmplacement();

            // retours
            if(!empDep.getVide()){
               if(empDep.getEntite().getNom().equals("Echantillon")){
                  emplForRetours.put(ManagerLocator.getEchantillonManager().findByIdManager(empDep.getObjetId()), empDep.clone());
               }else if(empDep.getEntite().getNom().equals("ProdDerive")){
                  emplForRetours.put(ManagerLocator.getProdDeriveManager().findByIdManager(empDep.getObjetId()), empDep.clone());
               }
            }
            if(!empDest.getVide()){
               if(empDest.getEntite().getNom().equals("Echantillon")){
                  emplForRetours.put(ManagerLocator.getEchantillonManager().findByIdManager(empDest.getObjetId()),
                     empDest.clone());
               }else if(empDest.getEntite().getNom().equals("ProdDerive")){
                  emplForRetours.put(ManagerLocator.getProdDeriveManager().findByIdManager(empDest.getObjetId()),
                     empDest.clone());
               }
            }

            if(movedEmplacements.contains(empDep)){
               movedEmplacements.remove(empDep);
            }
            if(movedEmplacements.contains(empDest)){
               movedEmplacements.remove(empDest);
            }

            final Integer posDep = decoDep.getPosition();
            final Integer posDest = decoDest.getPosition();

            decoDep.setPosition(posDest);
            decoDep.generateLibelle(adrlTerminale, terminale);
            decoDest.setPosition(posDep);
            decoDest.generateLibelle(adrlTerminale, terminale);

            imgDep.setAttribute("empDeco", decoDest);
            imgDest.setAttribute("empDeco", decoDep);
            imgDep.setTooltiptext(decoDest.getLibelle());
            imgDest.setTooltiptext(decoDep.getLibelle());

            // 2.0.13 EmplacementDiv MAJ CSS
            final String styleImgDep = imgDep.getImageStyle();
            final String styleImgDest = imgDest.getImageStyle();
            imgDep.setImageStyle(styleImgDest);
            imgDest.setImageStyle(styleImgDep);
            final String overlayClassDep = imgDep.getOverlay();
            final String overlayClassDest = imgDest.getOverlay();
            imgDep.setOverlay(overlayClassDest);
            imgDest.setOverlay(overlayClassDep);

            empDep.setPosition(decoDep.getPosition());
            // empDep.setAdrl(decoDep.getAdrl());
            empDest.setPosition(decoDest.getPosition());
            // empDest.setAdrl(decoDest.getAdrl());

            // si l'image est vide
            if(decoDep.getVide()){
               imgDest.setStyle(null);
               imgDest.setDraggable("false");
               imgDest.setDroppable("true");
               imgDest.addForward("onDrop", self, "onDropImage", imgDep);
               imgDest.setSclass("imageNonSelectableEmplacement");
            }else{
               // sinon
               imgDest.setStyle("cursor:pointer;");
               imgDest.setDraggable("true");
               imgDest.setDroppable("true");
               imgDest.addForward("onDrop", self, "onDropImage", imgDep);
               imgDest.setSclass("imageMovedEmplacement");
            }
            // si l'image est vide
            if(decoDest.getVide()){
               imgDep.setStyle(null);
               imgDep.setDraggable("false");
               imgDep.setDroppable("true");
               imgDep.addForward("onDrop", self, "onDropImage", imgDep);
               imgDep.setSclass("imageNonSelectableEmplacement");
            }else{
               // sinon
               imgDep.setDraggable("true");
               imgDep.setDroppable("true");
               imgDep.addForward("onDrop", self, "onDropImage", imgDep);
               imgDep.setStyle("cursor:pointer;");
               imgDep.setSclass("imageMovedEmplacement");
            }

            if(empDep.getEmplacementId() != null){
               movedEmplacements.add(empDep);
            }
            if(empDest.getEmplacementId() != null){
               movedEmplacements.add(empDest);
            }

            if(!decoDep.getVide()){
               final Row depRow = new Row();
               final Label code = new Label();
               code.setValue(decoDep.getCode());
               final Label dep = new Label();
               dep.setValue(decoDest.getAdrl());
               final Image img = new Image();
               img.setSrc("/images/icones/next.png");
               img.setHeight("15px");
               final Label dest = new Label();
               dest.setValue(decoDep.getAdrl());
               depRow.appendChild(code);
               depRow.appendChild(dep);
               depRow.appendChild(img);
               depRow.appendChild(dest);
               gridHistorique.getRows().appendChild(depRow);
            }

            if(!decoDest.getVide()){
               final Row destRow = new Row();
               final Label code = new Label();
               code.setValue(decoDest.getCode());
               final Label dep = new Label();
               dep.setValue(decoDep.getAdrl());
               final Image img = new Image();
               img.setSrc("/images/icones/next.png");
               img.setHeight("15px");
               final Label dest = new Label();
               dest.setValue(decoDest.getAdrl());
               destRow.appendChild(code);
               destRow.appendChild(dep);
               destRow.appendChild(img);
               destRow.appendChild(dest);
               gridHistorique.getRows().appendChild(destRow);
            }
            getBinder().loadComponent(gridHistorique);
            getBinder().loadComponent(rowHistorique);
         }
      }
   }

   public void onClick$contenu(){
      if(this.terminale.getTerminaleId() != null){

         emplForRetours.clear();

         cloneObject();

         if(currentSelectedImg != null){
            currentSelectedImg.setSclass("imageEmplacement");
            currentSelectedImg = null;
         }

         switchToContenuMode();
      }
   }

   /**
    * Passe la fiche terminale en deplacement mode.
    * Une liste de mismatches entre un scan et le stockage virtuel 
    * peux être passée en paramètre
    * @version 2.1
    */
   public void onClick$deplacerEmplacements(){
      Map<ScanTube, TKStockableObject> mismatches = null;
      if(currScanTerminaleDTO != null){
         mismatches = currScanTerminaleDTO.getEmplacementsMismatch();
         currScanTerminaleDTO = null;
      }
      getObjectTabController().switchToDeplacerEmplacementsMode(this.terminale, mismatches);
   }

   /**
    * Passe la fiche terminale en destockage mode.
    * Une liste d'emplacement à destocker peux être passée en paramètre
    * @version 2.1
    */
   public void onClick$destockerEmplacements(){
      List<EmplacementDecorator> decos = null;
      if(currScanTerminaleDTO != null){
         decos = new ArrayList<>();
         final Emplacement empl = new Emplacement();
         for(final ScanTube tube : currScanTerminaleDTO.getEmplacementsToFree().keySet()){
            empl.setTerminale(currScanTerminaleDTO.getTerminale());
            empl.setPosition(tube.getPosition());
            decos.add(getEmplacementDecos().get(getEmplacementDecos().indexOf(new EmplacementDecorator(empl))));
         }
         currScanTerminaleDTO = null;
      }
      getObjectTabController().switchToDestockageEmplacementsMode(this.terminale, decos);
   }

   /**
    * Clic sur le bouton afficherEchantillons : tous les échantillons de la
    * terminale sont envoyés dans le panel des échantillons.
    */
   public void onClick$afficherEchantillons(){
      Clients.showBusy(Labels.getLabel("terminale.selection.echantillons"));
      Events.echoEvent("onLaterAffichageEchantillons", self, null);
   }

   /**
    * Méthode gérant l'affichage des échans d'une enceinte terminale.
    */
   public void onLaterAffichageEchantillons(){

      // on met a jour la liste des échantillons avec ceux
      // contenus dans la terminale
      final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);
      tabController.getListe().updateListContent(ManagerLocator.getTerminaleManager().getEchantillonsManager(terminale));
      tabController.switchToOnlyListeMode();

      Clients.clearBusy();

   }

   /**
    * Clic sur le bouton afficherDerives : tous les dérivés de la terminale
    * sont envoyés dans le panel des dérivés.
    */
   public void onClick$afficherDerives(){
      Clients.showBusy(Labels.getLabel("terminale.selection.prodDerives"));
      Events.echoEvent("onLaterAffichageDerives", self, null);
   }

   /**
    * Méthode gérant l'affichage des dérivés d'une enceinte terminale.
    */
   public void onLaterAffichageDerives(){

      // on met a jour la liste des dérivés avec ceux
      // contenus dans la terminale
      final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);
      tabController.getListe().updateListContent(ManagerLocator.getTerminaleManager().getProdDerivesManager(terminale));
      tabController.switchToOnlyListeMode();

      Clients.clearBusy();

   }

   /**
    * Initialise les listes d'emplacements.
    */
   public void initEmplacements(){
      // on récupère les emplacements de la boite
      emplacements = ManagerLocator.getEmplacementManager().findByTerminaleWithOrder(terminale);
      // on récupère le code de l'objet se trouvant sur chaque
      // emplacement
      final List<String> codes = ManagerLocator.getEmplacementManager().getNomsForEmplacementsManager(emplacements);
      // emplacementDecos = new ArrayList<EmplacementDecorator>();
      getEmplacementDecos().clear();
      // on récupère le type de l'objet se trouvant sur chaque
      // emplacement
      final List<String> typesObjs = ManagerLocator.getEmplacementManager().getTypesForEmplacementsManager(emplacements);

      final BidiMap empObjs =
         new DualHashBidiMap(ManagerLocator.getTerminaleManager().getTkObjectsAndEmplacementsManager(terminale));

      int cpt = 1;
      // on parcourt la liste d'emplacements
      for(int i = 0; i < emplacements.size(); i++){
         final Emplacement emp = emplacements.get(i);

         // si aucun emplacement n'est défini à la position "cpt"
         // on crée un decorator vide
         while(emp.getPosition() > cpt){
            final Emplacement newE = new Emplacement();
            newE.setPosition(cpt);
            newE.setTerminale(terminale);
            final EmplacementDecorator deco = new EmplacementDecorator(newE);
            deco.setPosition(cpt);
            deco.setTerminale(terminale);
            deco.generateLibelle(adrlTerminale, this.terminale);
            getEmplacementDecos().add(deco);
            ++cpt;
         }
         // on remplit un decorator avec l'emplacement
         final EmplacementDecorator deco = new EmplacementDecorator(emp);
         deco.setCode(codes.get(i));
         deco.setType(typesObjs.get(i));
         deco.generateLibelle(adrlTerminale, this.terminale);

         // since 2.0.13 ajout objet au decorator pour retrouver facilement statut
         deco.setTkStockObj((TKStockableObject) empObjs.inverseBidiMap().get(emp));

         getEmplacementDecos().add(deco);
         ++cpt;
      }

      // si aucun emplacement n'est défini à la position "cpt"
      // on crée un decorator vide
      while(terminale.getTerminaleType().getNbPlaces() > getEmplacementDecos().size()){
         final Emplacement newE = new Emplacement();
         newE.setPosition(cpt);
         newE.setTerminale(terminale);
         final EmplacementDecorator deco = new EmplacementDecorator(newE);
         deco.setPosition(cpt);
         deco.generateLibelle(adrlTerminale, this.terminale);
         getEmplacementDecos().add(deco);
         ++cpt;
      }

      emplacements = null;
   }

   /**
    * Méthode modélisant la structure et le contenu d'une boite.
    */
   public void initModelisation(){
      // initialise les listes contenant les emplacements
      initEmplacements();
      // imagesEmplacements = new ArrayList<Div>();
      getImagesEmplacements().clear();

      if(terminale.getTerminaleType().getScheme() == null){

         if(terminale.getTerminaleType().getHauteur() != null && terminale.getTerminaleType().getLongueur() != null){
            final Vlayout mainVbox = new Vlayout();
            mainVbox.setSpacing("0");
            mainVbox.setStyle("overflow:visible");

            final int width = 20 + (30 * terminale.getTerminaleType().getLongueur());
            mainVbox.setWidth(width + "px");

            // création des abscisses
            final Hlayout separator = new Hlayout();
            separator.setHeight("5px");
            separator.setParent(mainVbox);
            separator.setStyle("overflow:visible");
            final Hlayout hBoxA = new Hlayout();
            hBoxA.setStyle("overflow:visible");
            hBoxA.setSpacing("0");
            final Div div = new Div();
            div.setWidth("20px");
            div.setParent(hBoxA);
            for(int j = 0; j < terminale.getTerminaleType().getLongueur(); j++){
               final Div divAbs = new Div();
               divAbs.setWidth("30px");
               divAbs.setParent(hBoxA);
               //divAbs.setAlign("center");
               divAbs.setStyle("text-align: center");
               final Label abs = new Label();
               abs.setSclass("formLabel");
               abs.setValue(getValueAbscisse(j + 1));
               abs.setParent(divAbs);
            }
            hBoxA.setParent(mainVbox);

            int cpt = 0;
            final List<Hlayout> lignes = new ArrayList<>();
            for(int i = 0; i < terminale.getTerminaleType().getHauteur(); i++){
               final Hlayout hBox = new Hlayout();
               hBox.setSpacing("0");
               hBox.setStyle("overflow:visible");

               // création des ordonnées
               final Div divOrd = new Div();
               divOrd.setWidth("20px");
               divOrd.setHeight("23px");
               divOrd.setParent(hBox);
               final Label ord = new Label();
               ord.setSclass("formLabel");
               ord.setValue(getValueOrdonnee(i + 1));
               ord.setParent(divOrd);
               for(int j = 0; j < terminale.getTerminaleType().getLongueur(); j++){
                  final EmplacementDecorator deco = getEmplacementDecos().get(cpt);

                  final Div img = createImage(deco);
                  img.setParent(hBox);
                  getImagesEmplacements().add(img);

                  ++cpt;
               }
               // on stocke les lignes dans une liste
               lignes.add(hBox);
            }

            // si la numérotation commence sur la 1ere ligne
            if(terminale.getTerminaleType().getDepartNumHaut()){
               for(int i = 0; i < lignes.size(); i++){
                  lignes.get(i).setParent(mainVbox);
               }
            }else{
               // sinon on inverse l'affichage des lignes
               for(int i = lignes.size() - 1; i >= 0; i--){
                  lignes.get(i).setParent(mainVbox);
               }
            }

            mainVbox.setParent(modeleBoite);
         }

      }else{

         final String[] values = terminale.getTerminaleType().getScheme().split(";");
         final Vbox mainVbox = new Vbox();
         mainVbox.setSpacing("0");
         mainVbox.setWidth("100%");
         mainVbox.setAlign("center");
         int cpt = 0;
         final List<Hbox> lignes = new ArrayList<>();
         // si la numérotation commence sur la 1ere ligne
         if(this.terminale.getTerminaleType().getDepartNumHaut()){
            for(int i = 0; i < values.length; i++){
               final int nbPlaces = Integer.parseInt(values[i]);
               final Hbox hBox = new Hbox();
               hBox.setSpacing("0");
               hBox.setAlign("center");
               for(int j = 0; j < nbPlaces; j++){
                  final EmplacementDecorator deco = getEmplacementDecos().get(cpt);

                  final Div img = createImage(deco);
                  img.setParent(hBox);
                  getImagesEmplacements().add(img);

                  ++cpt;
               }
               lignes.add(hBox);
            }
         }else{
            // sinon on inverse le parcours des lignes
            for(int i = values.length - 1; i >= 0; i--){
               final int nbPlaces = Integer.parseInt(values[i]);
               final Hbox hBox = new Hbox();
               hBox.setSpacing("0");
               hBox.setAlign("center");
               for(int j = 0; j < nbPlaces; j++){
                  final EmplacementDecorator deco = getEmplacementDecos().get(cpt);

                  final Div img = createImage(deco);
                  img.setParent(hBox);
                  getImagesEmplacements().add(img);

                  ++cpt;
               }
               lignes.add(0, hBox);
            }
         }
         for(int i = 0; i < lignes.size(); i++){
            lignes.get(i).setParent(mainVbox);
         }
         mainVbox.setParent(modeleBoite);
      }

   }

   public String getValueAbscisse(final Integer num){
      if(terminale.getTerminaleNumerotation().getColonne().equals("CAR")){
         return Utils.createListChars(num, null, new ArrayList<String>()).get(num - 1);
      }else{
         return String.valueOf(num);
      }
   }

   public String getValueOrdonnee(final Integer num){
      if(terminale.getTerminaleNumerotation().getLigne().equals("CAR")){
         return Utils.createListChars(num, null, new ArrayList<String>()).get(num - 1);
      }else{
         return String.valueOf(num);
      }
   }

   /**
    * Méthode créant une image représentant un emplacement.
    * 
    * @since 2.0.13 remplace l'objet Img par une pile de Div 
    * (EmplacementDiv) pour gérer l'image + calque objet statut 
    * en full CSS.
    * @param deco Decorator de 'emplacement.
    * @return L'image.
    * @version 2.0.13
    */
   public Div createImage(final EmplacementDecorator deco){
      final EmplacementDiv div = new EmplacementDiv(28, 21, deco.getLibelle());
      div.setAttribute("empDeco", deco);

      // dessine l'image
      div.setImageStyle(getImageStyle(deco));

      // si l'image est vide
      if(deco.getVide()){
         // background classique, non cliquable
         div.setSclass("imageNonSelectableEmplacement");
      }else{ // emplacement cliquable
         // background classique, cliquable
         div.setStyle("cursor:pointer; ");
         div.setSclass("imageEmplacement");
         div.addForward("onClick", self, "onClickImage", div);
         // selection --> suffixe image contourée			
         if(getCurrentSelection() != null && getCurrentSelection().getPosition().equals(deco.getEmplacement().getPosition())){
            div.setSclass("imageCurrentEmplacement");
            currentSelectedImg = div;
         }

         // applique un calque si objet statut = RESERVE ou ENCOURS
         if(deco.getObjectStatut() != null){
            div.setOverlay(deco.getObjectStatut().getStatut().toLowerCase());
         }
      }
      return div;
   }

   //	/**
   //	 * Cette méthode renvoie l'adresse de l'image en fonction du decorator
   //	 * passé en paramètre.
   //	 * @param deco Decorator de l'emplacement.
   //	 * @return Adresse de l'image.
   //	 */
   //	public String getImageSrc(EmplacementDecorator deco) {
   //		StringBuffer sb = new StringBuffer();
   //
   //		sb.append(imageSrc);
   //		
   //		// si l'image est vide
   //		if (deco.getVide()) {
   //			sb.append("VIDE");
   //		} else {
   //
   //			// si l'emplacement contient un échantillon
   //			if (deco.getEmplacement().getEntite().getNom()
   //					.equals("Echantillon")) {
   //				
   //				if (getMainWindow().getEchantillonTypesCouleur()
   //						.containsKey(deco.getType())) {
   //					sb.append(getMainWindow().getEchantillonTypesCouleur()
   //							.get(deco.getType()));
   //				} else if (getMainWindow().getSelectedBanque()
   //						.getEchantillonCouleur() != null) {
   //					sb.append(getMainWindow().getSelectedBanque()
   //							.getEchantillonCouleur().getCouleur());
   //				} else {
   //					sb.append("VERT");
   //				}
   //				
   //			} else if (deco.getEmplacement().getEntite().getNom()
   //					.equals("ProdDerive")) {
   //				// si l'emplacement contient un dérivé
   //				if (getMainWindow().getProdDeriveTypesCouleur()
   //						.containsKey(deco.getType())) {
   //					sb.append(getMainWindow().getProdDeriveTypesCouleur()
   //							.get(deco.getType()));
   //				} else if (getMainWindow().getSelectedBanque()
   //						.getProdDeriveCouleur() != null) {
   //					sb.append(getMainWindow().getSelectedBanque()
   //							.getProdDeriveCouleur().getCouleur());
   //				} else {
   //					sb.append("VERT");
   //				}
   //			}
   //		}
   //		sb.append(".png");
   //		return sb.toString();
   //	}

   /**
    * Cette méthode renvoie le style à appliquer à la Div pour dessiner 
    * l'emplacement en fonction du decorator passé en paramètre.
    * @param deco Decorator de l'emplacement.
    * @return commande CSS background: url appliqué à la Div.
    * @since 2.0.13
    */
   public String getImageStyle(final EmplacementDecorator deco){
      final StringBuffer sb = new StringBuffer();

      sb.append(imageSrc);

      // si l'image est vide
      if(deco.getVide()){
         sb.append("VIDE");
      }else{

         // si l'emplacement contient un échantillon
         if(deco.getEmplacement().getEntite().getNom().equals("Echantillon")){

            if(getMainWindow().getEchantillonTypesCouleur().containsKey(deco.getType())){
               sb.append(getMainWindow().getEchantillonTypesCouleur().get(deco.getType()));
            }else if(getMainWindow().getSelectedBanque().getEchantillonCouleur() != null){
               sb.append(getMainWindow().getSelectedBanque().getEchantillonCouleur().getCouleur());
            }else{
               sb.append("VERT");
            }

         }else if(deco.getEmplacement().getEntite().getNom().equals("ProdDerive")){
            // si l'emplacement contient un dérivé
            if(getMainWindow().getProdDeriveTypesCouleur().containsKey(deco.getType())){
               sb.append(getMainWindow().getProdDeriveTypesCouleur().get(deco.getType()));
            }else if(getMainWindow().getSelectedBanque().getProdDeriveCouleur() != null){
               sb.append(getMainWindow().getSelectedBanque().getProdDeriveCouleur().getCouleur());
            }else{
               sb.append("VERT");
            }
         }
      }
      sb.append(".png");

      return "background: transparent url('" + Executions.getCurrent().encodeURL(sb.toString()) + "');";
   }

   //	/**
   //	 * Cette méthode renvoie l'adresse de l'image en fonction du decorator passé
   //	 * en paramètre.
   //	 * 
   //	 * @param deco
   //	 *            Decorator de l'emplacement.
   //	 * @return Adresse de l'image.
   //	 */
   //	public String getImageSrc(EmplacementDecorator deco) {
   //
   //		// recupere le code du produit selectionne
   //		String[] split = deco.getLibelle().split(" : ");
   //		String currentObj = null;
   //		if (getCurrentSelection() != null) {
   //			if (split.length > 1
   //					&& split[1].equals(getCurrentSelection().getCode())) {
   //				currentObj = split[1];
   //			}
   //		}
   //
   //		StringBuffer sb = new StringBuffer();
   //
   //		if (currentObj != null) {
   //			sb.append("/images/icones/emplacements/emplacementROUGE-Fond");
   //			currentObj = null;
   //		} else {
   //			sb.append(imageSrc);
   //
   //			// si l'image est vide
   //			if (deco.getVide()) {
   //				sb.append("VIDE");
   //			} else {
   //				// si l'emplacement contient un échantillon
   //				if (deco.getEmplacement().getEntite().getNom()
   //						.equals("Echantillon")) {
   //
   //					if (getMainWindow().getEchantillonTypesCouleur()
   //							.containsKey(deco.getType())) {
   //						sb.append(getMainWindow().getEchantillonTypesCouleur()
   //								.get(deco.getType()));
   //					} else if (getMainWindow().getSelectedBanque()
   //							.getEchantillonCouleur() != null) {
   //						sb.append(getMainWindow().getSelectedBanque()
   //								.getEchantillonCouleur().getCouleur());
   //					} else {
   //						sb.append("VERT");
   //					}
   //
   //				} else if (deco.getEmplacement().getEntite().getNom()
   //						.equals("ProdDerive")) {
   //					// si l'emplacement contient un dérivé
   //					if (getMainWindow().getProdDeriveTypesCouleur()
   //							.containsKey(deco.getType())) {
   //						sb.append(getMainWindow().getProdDeriveTypesCouleur()
   //								.get(deco.getType()));
   //					} else if (getMainWindow().getSelectedBanque()
   //							.getProdDeriveCouleur() != null) {
   //						sb.append(getMainWindow().getSelectedBanque()
   //								.getProdDeriveCouleur().getCouleur());
   //					} else {
   //						sb.append("VERT");
   //					}
   //				}
   //
   //			}
   //		}
   //		sb.append(".png");
   //		return sb.toString();
   //	}

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForTerminale(){
      drawActionsButtons("Stockage");

      boolean canDeplace = false;

      if(!sessionScope.containsKey("ToutesCollections")){
         canDeplace = drawActionOnOneButton("Stockage", "Modification");
      }
      if(deplacer != null){
         deplacer.setDisabled(!canDeplace);
      }
      if(contenu != null){
         contenu.setDisabled(!canDeplace);
      }
      if(deplacerEmplacements != null){
         deplacerEmplacements.setDisabled(!canDeplace);
      }
      if(destockerEmplacements != null){
         destockerEmplacements.setDisabled(!canDeplace);
      }
      if(afficherEchantillons != null){
         if(getDroitOnAction("Echantillon", "Consultation")){
            afficherEchantillons.setDisabled(false);
         }else{
            afficherEchantillons.setDisabled(true);
         }
      }
      if(afficherDerives != null){
         if(getDroitOnAction("ProdDerive", "Consultation")){
            afficherDerives.setDisabled(false);
         }else{
            afficherDerives.setDisabled(true);
         }
      }
   }

   public void onClick$print(){
      if(this.terminale.getTerminaleId() != null){
         final Document doc = ManagerLocator.getXmlUtils().createJDomDocumentContenuBoite();
         final Element root = doc.getRootElement();
         final Element page =
            ManagerLocator.getXmlUtils().addPageContenuBoite(root, Labels.getLabel("impression.contenu.boite.title"));

         // création des parents pour récupérer la boite
         // Récup des parents de la boite
         final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(this.terminale);
         final List<String> instructions = new ArrayList<>();
         // pour chaque parent
         for(int j = 0; j < parents.size(); j++){
            if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
               final Conteneur c = (Conteneur) parents.get(j);
               instructions.add(
                  ObjectTypesFormatters.getLabel("impression.contenu" + ".boite.parent.conteneur", new String[] {c.getCode()}));
            }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
               final Enceinte e = (Enceinte) parents.get(j);
               instructions.add(
                  ObjectTypesFormatters.getLabel("impression.contenu" + ".boite.parent.enceinte", new String[] {e.getNom()}));
            }
         }
         instructions.add(
            ObjectTypesFormatters.getLabel("impression.contenu" + ".boite.parent.terminale", new String[] {terminale.getNom()}));

         final List<String> statistiques = new ArrayList<>();
         // nombre d'échantillons dans la boite
         statistiques.add(ObjectTypesFormatters.getLabel("impression.contenu" + ".boite.nb.echantillons",
            new String[] {String.valueOf(ManagerLocator.getTerminaleManager().getEchantillonsManager(terminale).size())}));

         // nombre de dérivés dans la boite
         statistiques.add(ObjectTypesFormatters.getLabel("impression.contenu" + ".boite.nb.prodDerives",
            new String[] {String.valueOf(ManagerLocator.getTerminaleManager().getProdDerivesManager(terminale).size())}));

         final BoiteContenu boite =
            new BoiteContenu(terminale, statistiques, instructions, Labels.getLabel("impression.contenu.boite.vide"));

         Hashtable<String, String> echantillonsTypesCouleurs = new Hashtable<>();
         String echantillonCouleur = null;
         if(getMainWindow().getSelectedBanque().getEchantillonCouleur() != null){
            echantillonCouleur = getMainWindow().getSelectedBanque().getEchantillonCouleur().getCouleur();
         }
         Hashtable<String, String> prodDerivesTypesCouleurs = new Hashtable<>();
         String prodDeriveCouleur = null;
         if(getMainWindow().getSelectedBanque().getProdDeriveCouleur() != null){
            prodDeriveCouleur = getMainWindow().getSelectedBanque().getProdDeriveCouleur().getCouleur();
         }
         if(getMainWindow().getEchantillonTypesCouleur() != null){
            echantillonsTypesCouleurs = getMainWindow().getEchantillonTypesCouleur();
         }
         // si l'emplacement contient un dérivé
         if(getMainWindow().getProdDeriveTypesCouleur() != null){
            prodDerivesTypesCouleurs = getMainWindow().getProdDeriveTypesCouleur();
         }

         final StringBuffer adrImages = new StringBuffer();
         adrImages.append(((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getContextPath());
         adrImages.append("/images/icones/emplacements/pastille");
         ManagerLocator.getXmlUtils().addBoiteContenu(page, boite, adrImages.toString(), echantillonsTypesCouleurs,
            echantillonCouleur, prodDerivesTypesCouleurs, prodDeriveCouleur);

         if(doc != null){
            // Transformation du document en fichier
            byte[] dl = null;
            try{
               dl = ManagerLocator.getXmlUtils().creerContenuHtml(doc);
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
   }

   public String getCouleurSrc(){
      final StringBuffer sb = new StringBuffer();

      if(this.terminale != null && this.terminale.getTerminaleId() != null && this.terminale.getCouleur() != null){
         sb.append(colorSrc);
         sb.append(this.terminale.getCouleur().getCouleur());
         sb.append(".png");
      }
      return sb.toString();
   }

   /*************************************************/
   /** ACCESSEURS **/
   /*************************************************/
   public String getEntiteReservee(){
      return entiteReservee;
   }

   public void setEntiteReservee(final String reservee){
      this.entiteReservee = reservee;
   }

   public String getBanqueReservee(){
      return banqueReservee;
   }

   public void setBanqueReservee(final String reservee){
      this.banqueReservee = reservee;
   }

   public String getDimensions(){
      final StringBuffer sb = new StringBuffer();
      if(this.selectedTerminaleType != null){
         if(this.selectedTerminaleType.getLongueur() != null){
            sb.append(this.selectedTerminaleType.getLongueur());
         }else{
            sb.append("-");
         }
         sb.append(" X ");
         if(this.selectedTerminaleType.getHauteur() != null){
            sb.append(this.selectedTerminaleType.getHauteur());
         }else{
            sb.append("-");
         }
      }

      return sb.toString();
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

   public List<Entite> getEntites(){
      return entites;
   }

   public void setEntites(final List<Entite> e){
      this.entites = e;
   }

   public Entite getSelectedEntite(){
      return selectedEntite;
   }

   public void setSelectedEntite(final Entite selected){
      this.selectedEntite = selected;
   }

   public Enceinte getEnceinte(){
      return enceinte;
   }

   public void setEnceinte(final Enceinte e){
      this.enceinte = e;
   }

   public Integer getPosition(){
      return position;
   }

   public void setPosition(final Integer p){
      this.position = p;
   }

   public List<TerminaleType> getTypes(){
      return types;
   }

   public void setTypes(final List<TerminaleType> t){
      this.types = t;
   }

   public TerminaleType getSelectedTerminaleType(){
      return selectedTerminaleType;
   }

   public void setSelectedTerminaleType(final TerminaleType selected){
      this.selectedTerminaleType = selected;
   }

   public List<TerminaleNumerotation> getNumerotations(){
      return numerotations;
   }

   public void setNumerotations(final List<TerminaleNumerotation> nums){
      this.numerotations = nums;
   }

   public TerminaleNumerotation getSelectedTerminaleNumerotation(){
      return selectedTerminaleNumerotation;
   }

   public void setSelectedTerminaleNumerotation(final TerminaleNumerotation selected){
      this.selectedTerminaleNumerotation = selected;
   }

   public List<Emplacement> getEmplacements(){
      return emplacements;
   }

   public void setEmplacements(final List<Emplacement> emp){
      this.emplacements = emp;
   }

   public String getAdresseActuelle(){
      return adresseActuelle;
   }

   public void setAdresseActuelle(final String adresseAct){
      this.adresseActuelle = adresseAct;
   }

   public String getAdresseDestination(){
      return adresseDestination;
   }

   public void setAdresseDestination(final String adresseDest){
      this.adresseDestination = adresseDest;
   }

   public Integer getPositionDestination(){
      return positionDestination;
   }

   public void setPositionDestination(final Integer positionDest){
      this.positionDestination = positionDest;
   }

   public String getContenuDestination(){
      return contenuDestination;
   }

   public void setContenuDestination(final String contenuDest){
      this.contenuDestination = contenuDest;
   }

   public TerminaleNode getTerminaleDestination(){
      return terminaleDestination;
   }

   public void setTerminaleDestination(final TerminaleNode terminaleDest){
      this.terminaleDestination = terminaleDest;
   }

   public Terminale getTerminaleAEchanger(){
      return terminaleAEchanger;
   }

   public void setTerminaleAEchanger(final Terminale terminaleAEch){
      this.terminaleAEchanger = terminaleAEch;
   }

   public String getAdrlTerminale(){
      return adrlTerminale;
   }

   public void setAdrlTerminale(final String adrlTerm){
      this.adrlTerminale = adrlTerm;
   }

   public List<Div> getImagesEmplacements(){
      return imagesEmplacements;
   }

   public void setImagesEmplacements(final List<Div> images){
      this.imagesEmplacements = images;
   }

   public List<Emplacement> getMovedEmplacements(){
      return movedEmplacements;
   }

   public void setMovedEmplacements(final List<Emplacement> movedEmps){
      this.movedEmplacements = movedEmps;
   }

   public static String getImageSrc(){
      return imageSrc;
   }

   public static void setImageSrc(final String src){
      FicheTerminale.imageSrc = src;
   }

   public Constraint getTerminaleNomConstraint(){
      return StockageConstraints.getTerminaleNomConstraint();
   }

   public Constraint getNomNullConstraint(){
      return StockageConstraints.getNomNullConstraint();
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("terminale.suppression.encours");
   }

   public CouleurItemRenderer getCouleurRenderer(){
      return couleurRenderer;
   }

   public void setCouleurRenderer(final CouleurItemRenderer c){
      this.couleurRenderer = c;
   }

   public ListModelList<Couleur> getCouleurs(){
      return couleurs;
   }

   public void setCouleurs(final ListModelList<Couleur> c){
      this.couleurs = c;
   }

   public Couleur getSelectedCouleur(){
      return selectedCouleur;
   }

   public void setSelectedCouleur(final Couleur s){
      this.selectedCouleur = s;
   }

   public Emplacement getCurrentSelection(){
      return currentSelection;
   }

   public void setCurrentSelection(final Emplacement c){
      this.currentSelection = c;
   }

   public HashMap<TKStockableObject, Emplacement> getEmplForRetours(){
      return emplForRetours;
   }

   public void setEmplForRetours(final HashMap<TKStockableObject, Emplacement> e){
      this.emplForRetours = e;
   }

   /**
    * Surcharge de la methode permettant d'afficher une fiche Terminale 
    * en lui assigant un emplacement sélectionnée à afficher à l'aide 
    * d'une image contourée.
    * @param empl
    */
   public void switchToStaticMode(final Emplacement empl){
      setCurrentSelection(empl);
      switchToStaticMode();
   }

   public List<TKStockableObject> prepareObjectsAndEmplacements(){
      emplForRetours.clear();
      // on place dans la HashMap l'ancienne adresse de toutes
      // les terminales de l'enceinte avec tous les emplacements 
      // non vides.
      // ces valeurs seront utilisées pour la création des
      // RETOURs dans le cadre de ce déplacement
      emplForRetours.putAll(ManagerLocator.getTerminaleManager().getTkObjectsAndEmplacementsManager(terminale));

      // si la destination n'est pas vide
      if(terminaleDestination != null && !terminaleDestination.isVide()){
         // on place dans la HashMap l'ancienne adresse de toutes
         // les terminales 
         // ces valeurs seront utilisées pour la création des
         // RETOURs dans le cadre de ce déplacement
         emplForRetours
            .putAll(ManagerLocator.getTerminaleManager().getTkObjectsAndEmplacementsManager(terminaleDestination.getTerminale()));
      }
      return new ArrayList<>(emplForRetours.keySet());
   }

   public Constraint getIncidentConstraint(){
      return StockageConstraints.getIncidentConstraint();
   }

   @Override
   public void onClick$addIncidentItem(){
      openAddIncidentWindow(page, self, null, null, terminale);
   }

   public Terminale getTerminale(){
      return terminale;
   }

   public void setTerminale(final Terminale t){
      this.terminale = t;

      if(this.terminale != null && this.terminale.getTerminaleId() != null){
         adrlTerminale = ManagerLocator.getEmplacementManager().getTerminaleAdrlManager(terminale);
      }
   }

   public List<EmplacementDecorator> getEmplacementDecos(){
      return emplacementDecos;
   }

   public void setEmplacementDecos(final List<EmplacementDecorator> e){
      this.emplacementDecos = e;
   }

   /**
    * Applique les vérifications de concordance entre le scan et les données 
    * enregistrées dans TK.
    * Surligne l'emplacement + notification en fonction de la discordance
    * @since 2.1
    * @version 2.1
    * @param dto TKScanTerminaleDTO
    */

   public void applyChecksOnScan(final TKScanTerminaleDTO dto){

      currScanTerminaleDTO = dto;

      EmplacementDiv img;
      final Emplacement empl = new Emplacement();

      // mismatches
      for(final ScanTube tube : dto.getEmplacementsMismatch().keySet()){
         empl.setTerminale(dto.getTerminale());
         empl.setPosition(tube.getPosition());
         img = (EmplacementDiv) getImagesEmplacements().get(getEmplacementDecos().indexOf(new EmplacementDecorator(empl)));
         img.setOverlay("mismatch");
         final String info = "mismatch:" + tube.getCode() + " " + dto.getEmplacementsMismatch().get(tube).getCode();
         Clients.evalJavaScript("notifyEmplacement('" + info + "',jq('#" + img.getUuid() + "'), 'error')");
      }

      // missings
      for(final ScanTube tube : dto.getEmplacementsToFill().keySet()){
         empl.setTerminale(dto.getTerminale());
         empl.setPosition(tube.getPosition());
         img = (EmplacementDiv) getImagesEmplacements().get(getEmplacementDecos().indexOf(new EmplacementDecorator(empl)));
         img.setOverlay("missing");
         final String info = "missing here :" + tube.getCode();
         Clients.evalJavaScript("notifyEmplacement('" + info + "',jq('#" + img.getUuid() + "'), 'warn')");
      }

      // filled
      for(final ScanTube tube : dto.getEmplacementsToFree().keySet()){
         empl.setTerminale(dto.getTerminale());
         empl.setPosition(tube.getPosition());
         img = (EmplacementDiv) getImagesEmplacements().get(getEmplacementDecos().indexOf(new EmplacementDecorator(empl)));
         img.setOverlay("filled");
         final String info = "filled here :" + dto.getEmplacementsToFree().get(tube).getCode();
         Clients.evalJavaScript("notifyEmplacement('" + info + "',jq('#" + img.getUuid() + "'), 'info')");
      }
   }
}
