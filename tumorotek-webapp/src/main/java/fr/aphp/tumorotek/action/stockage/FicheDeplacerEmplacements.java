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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.EmplacementDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.stockage.TerminaleNode;

/**
 * @version 2.2.3-genno fix TK-291
 * @author Mathieu BARTHELEMY
 *
 */
public class FicheDeplacerEmplacements extends FicheTerminale
{

   private final Log log = LogFactory.getLog(FicheDeplacerEmplacements.class);

   private static final long serialVersionUID = -3813832001048689323L;

   // private Row rowViewBoite;
   // private Div modeleBoite;
   private Row badTerminaleRow;

   private Label badTerminaleLabel;

   private Grid gridHistorique;

   private Listbox lignesBox;

   private Listbox colonnesBox;

   private Column oldAdresseColonne;

   private Column newAdresseColonne;

   private Column flecheColonne;

   private Row rowHistoriqueTitle;

   /**
    *  Static Components pour le mode sélection.
    */
   private Component[] objSelectionComponents;

   private Row rowSelectionTitle;

   private Button validateSelection;

   private Button cancelSelection;

   /**
    *  Static Components pour le mode déplacement.
    */
   private Component[] objDeplacementComponents;

   private Row rowDeplacementTitle;

   private Row selectElementRow;

   private Button validateDeplacement;

   private Button cancelDeplacement;

   private Row entiteReserveeRow;

   private Label entiteReserveeLabel;

   /**
    * Static Components pour le mode stockage.
    */
   private Component[] objStockageComponents;

   private Row rowStockageTitle;

   private Row listErrorRow;

   private Button validateStockage;

   private Button cancelStockage;

   /**
    * Components pour le mode déstockage.
    */
   private Component[] objDestockageComponents;

   private Row rowDestockageTitle;

   private Button validateDestockage;

   private Button cancelDestockage;

   /**
    *  Objets Principaux.
    */
   // private Terminale terminale;

   //private List<EmplacementDecorator> emplacementDecos = new
   //	ArrayList<EmplacementDecorator>();
   private List<EmplacementDecorator> deplacements = new ArrayList<>();

   private List<EmplacementDecorator> deplacementsRestants = new ArrayList<>();

   private List<EmplacementDecorator> emplacementDepart = new ArrayList<>();

   private List<EmplacementDecorator> emplacementReserves = new ArrayList<>();

   private EmplacementDecorator selectedEmplacement;

   private Hashtable<EmplacementDecorator, EmplacementDecorator> emplacementsDestDep = new Hashtable<>();

   private List<Echantillon> echantillons = new ArrayList<>();

   private List<ProdDerive> derives = new ArrayList<>();

   // private HashMap<TKStockableObject, Emplacement> emplForRetours =
   //						new HashMap<TKStockableObject, Emplacement>();
   private final List<Emplacement> emplacementsFinaux = new ArrayList<>();

   /**
    *  Variables formulaire.
    */
   private boolean selectionMode = true;

   private boolean deplacementMode = false;

   private boolean stockerMode = false;

   private boolean destockageMode = false;

   private String mode = "";

   // private String adrlTerminale = "";
   private String badTerminaleError = "";

   // private List<Image> imagesEmplacements = new ArrayList<Image>();
   private List<String> lignes = new ArrayList<>();

   private List<String> colonnes = new ArrayList<>();

   private Integer numeroLigne = 0;

   private Integer numeroColonne = 0;

   private String entiteReservee = "";

   private String returnMethode = "";

   private String path = "";

   private String typeEntite = "";
   // private static String imageSrc = "/images/icones/emplacements/emplacement";

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // Initialisation des listes de composants
      this.objSelectionComponents = new Component[] {this.rowSelectionTitle, this.validateSelection, this.cancelSelection};

      this.objDeplacementComponents = new Component[] {this.rowDeplacementTitle, this.selectElementRow, this.validateDeplacement,
         this.cancelDeplacement, this.entiteReserveeRow};

      this.objStockageComponents = new Component[] {this.rowStockageTitle, this.selectElementRow, this.validateStockage,
         this.cancelStockage, this.entiteReserveeRow, this.listErrorRow};

      this.objDestockageComponents = new Component[] {this.rowDestockageTitle, this.validateDestockage, this.cancelDestockage};

      getBinder().loadAll();

   }

   @Override
   public StockageController getObjectTabController(){
      return super.getObjectTabController();
   }

   /**
    * Change mode de la fiche en mode sélection.
    * @param back boolean
    * @param mismatches Une liste de mismatches entre un scan et le stockage virtuel
    * peux être passée en paramètre pour pré-remplir les déplacements
    * @version 2.1
    */
   public void switchToSelectionMode(final boolean back, final Map<ScanTube, TKStockableObject> mismatches){
      // mode sélection
      selectionMode = true;
      deplacementMode = false;
      stockerMode = false;
      destockageMode = false;
      // la colonne des anciennes adresses est visible
      oldAdresseColonne.setVisible(true);
      newAdresseColonne.setVisible(true);
      flecheColonne.setVisible(true);

      for(int i = 0; i < objSelectionComponents.length; i++){
         objSelectionComponents[i].setVisible(true);
      }
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objStockageComponents.length; i++){
         objStockageComponents[i].setVisible(false);
      }
      for(int i = 0; i < objDestockageComponents.length; i++){
         objDestockageComponents[i].setVisible(false);
      }
      this.rowStockageTitle.setVisible(false);

      // on vide la modélisation de la boite
      modeleBoite.getChildren().clear();

      // si on arrive pour la 1ere fois sur la page,
      // nouveaux deplacements
      if(!back){
         deplacements = new ArrayList<>();
      }

      // si c'est une nouvelle terminale
      if(this.terminale == null || this.terminale.getTerminaleId() == null){
         // on cache les emplacements
         modeleBoite.setVisible(false);
      }else{
         // si la terminale n'est pas modifiable : elle est
         // réservée pour une autre banque
         if(this.terminale.getBanque() != null
            && !SessionUtils.getSelectedBanques(sessionScope).get(0).equals(this.terminale.getBanque())){
            // on cache les emplacements
            modeleBoite.setVisible(false);
         }else{
            modeleBoite.setVisible(true);
            initModelisation();
         }
      }

      getBinder().loadComponent(self);

      // @since 2.1
      // selectionne directement les emplacements à déplacer
      if(mismatches != null){
         Div img;
         final Emplacement empl = new Emplacement();
         for(final ScanTube tube : mismatches.keySet()){
            empl.setTerminale(getTerminale());
            empl.setPosition(tube.getPosition());
            img = getImagesEmplacements().get(getEmplacementDecos().indexOf(new EmplacementDecorator(empl)));
            setSelectedEmplacement(getEmplacementDecos().get(getEmplacementDecos().indexOf(new EmplacementDecorator(empl))));
            handleImage(img);
         }
      }
   }

   /**
    * Change mode de la fiche en mode deplacement.
    */
   @Override
   public void switchToDeplacerMode(){
      // mode déplacement
      selectionMode = false;
      deplacementMode = true;
      stockerMode = false;
      destockageMode = false;
      // la colonne des anciennes adresses est visible
      oldAdresseColonne.setVisible(true);
      newAdresseColonne.setVisible(true);
      flecheColonne.setVisible(true);

      for(int i = 0; i < objSelectionComponents.length; i++){
         objSelectionComponents[i].setVisible(false);
      }
      for(int i = 0; i < objStockageComponents.length; i++){
         objStockageComponents[i].setVisible(false);
      }
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(true);
      }
      for(int i = 0; i < objDestockageComponents.length; i++){
         objDestockageComponents[i].setVisible(false);
      }
      this.rowStockageTitle.setVisible(false);

      deplacementsRestants = new ArrayList<>();
      emplacementDepart = new ArrayList<>();
      // on init les deplacements restants et ceux de départ
      for(int i = 0; i < deplacements.size(); i++){
         deplacementsRestants.add(deplacements.get(i));
         emplacementDepart.add(deplacements.get(i));
      }
      selectedEmplacement = deplacementsRestants.get(0);

      emplacementsDestDep = new Hashtable<>();

      // on affiche si la terminale est réservée pour un type d'entité
      if(terminale.getEntite() != null){
         final StringBuffer sb = new StringBuffer();
         sb.append(Labels.getLabel("deplacer.emplacement.terminale.entite.reservee"));
         sb.append(" ");
         sb.append(terminale.getEntite().getNom());
         sb.append("s.");
         entiteReservee = sb.toString();
         entiteReserveeLabel.setValue(entiteReservee);
         entiteReserveeRow.setVisible(true);
      }else{
         entiteReserveeRow.setVisible(false);
      }

      getBinder().loadComponent(self);

   }

   /**
    * Change mode de la fiche en mode deplacement.
    * @param echans Echantillons à stocker.
    * @param der Dérivés à stocker.
    * @param p Chemin vers la page qui appelle le stockage.
    * @param methode Methode à appeler après le stockage.
    * @param reservation Liste d'emplacements réservés lors
    * d'un précédent stockage.
    */
   public void switchToStockerMode(final List<Echantillon> echans, final List<ProdDerive> der, final String p,
      final String methode, final List<Emplacement> reservation){
      // mode stockage
      selectionMode = false;
      deplacementMode = false;
      stockerMode = true;
      destockageMode = false;
      returnMethode = methode;
      path = p;
      this.terminale = null;
      modeleBoite.getChildren().clear();
      // la colonne des anciennes adresses n'est pas visible
      oldAdresseColonne.setVisible(false);
      newAdresseColonne.setVisible(true);
      flecheColonne.setVisible(true);

      for(int i = 0; i < objSelectionComponents.length; i++){
         objSelectionComponents[i].setVisible(false);
      }
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objStockageComponents.length; i++){
         objStockageComponents[i].setVisible(true);
      }
      for(int i = 0; i < objDestockageComponents.length; i++){
         objDestockageComponents[i].setVisible(false);
      }
      this.rowDeplacementTitle.setVisible(false);
      this.rowStockageTitle.setVisible(true);

      deplacementsRestants = new ArrayList<>();
      emplacementDepart = new ArrayList<>();
      deplacements = new ArrayList<>();
      // si l'on souhaite stocker des échantillons
      if(echans != null){
         echantillons = new ArrayList<>();
         typeEntite = "Echantillon";
         for(int i = 0; i < echans.size(); i++){
            // on ne traite que les échans non stockés
            if(echans.get(i).getObjetStatut().getStatut().equals("NON STOCKE")){
               echantillons.add(echans.get(i));
               final EmplacementDecorator deco = new EmplacementDecorator(new Emplacement());
               deco.setAdrl("--");
               deco.setCode(echans.get(i).getCode());
               final Entite e = ManagerLocator.getEntiteManager().findByNomManager(typeEntite).get(0);
               deco.getEmplacement().setEntite(e);
               deco.getEmplacement().setObjetId(echans.get(i).getEchantillonId());
               deco.getEmplacement().setPosition(i + 1);
               deplacements.add(deco);
            }
         }
      }else if(der != null){
         // si l'on souhaite stocker des dérivés
         typeEntite = "ProdDerive";
         derives = new ArrayList<>();
         for(int i = 0; i < der.size(); i++){
            // on ne traite que les dérivés non stockés
            if(der.get(i).getObjetStatut().getStatut().equals("NON STOCKE")){
               derives.add(der.get(i));
               final EmplacementDecorator deco = new EmplacementDecorator(new Emplacement());
               deco.setAdrl("--");
               deco.setCode(der.get(i).getCode());
               final Entite e = ManagerLocator.getEntiteManager().findByNomManager(typeEntite).get(0);
               deco.getEmplacement().setEntite(e);
               deco.getEmplacement().setObjetId(der.get(i).getProdDeriveId());
               deco.getEmplacement().setPosition(i + 1);
               deplacements.add(deco);
            }
         }
      }

      // si des emplacements ont été réservés
      if(reservation != null){
         emplacementReserves = new ArrayList<>();
         for(int i = 0; i < reservation.size(); i++){
            final Emplacement emp = reservation.get(i);
            final EmplacementDecorator deco = new EmplacementDecorator(emp);
            deco.setTerminale(emp.getTerminale());
            deco.setPosition(emp.getPosition());
            emplacementReserves.add(deco);
         }
      }

      for(int i = 0; i < deplacements.size(); i++){
         deplacementsRestants.add(deplacements.get(i));
         emplacementDepart.add(deplacements.get(i));
      }
      if(deplacementsRestants.size() > 0){
         selectedEmplacement = deplacementsRestants.get(0);
      }

      emplacementsDestDep = new Hashtable<>();

      if(terminale != null && terminale.getEntite() != null){
         final StringBuffer sb = new StringBuffer();
         sb.append(Labels.getLabel("deplacer.emplacement.terminale.entite.reservee"));
         sb.append(" ");
         sb.append(terminale.getEntite().getNom());
         sb.append("s.");
         entiteReservee = sb.toString();
         entiteReserveeLabel.setValue(entiteReservee);
         entiteReserveeRow.setVisible(true);
      }else{
         entiteReserveeRow.setVisible(false);
      }

      if(deplacements.size() > 0){
         listErrorRow.setVisible(false);
      }else{
         validateStockage.setVisible(false);
      }

      // on rafraichit les listes
      getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
      final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
      gridHistorique.setModel(list);

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode déstockage.
    * @param decos Liste Emplacement decorator pour sélection emplacement à destocker
    * @version 2.1
    */
   public void switchToDestockageMode(final List<EmplacementDecorator> decos){
      // mode sélection
      selectionMode = false;
      deplacementMode = false;
      stockerMode = false;
      destockageMode = true;
      // la colonne des anciennes adresses est visible
      oldAdresseColonne.setVisible(true);
      newAdresseColonne.setVisible(false);
      flecheColonne.setVisible(false);

      for(int i = 0; i < objSelectionComponents.length; i++){
         objSelectionComponents[i].setVisible(false);
      }
      for(int i = 0; i < objDeplacementComponents.length; i++){
         objDeplacementComponents[i].setVisible(false);
      }
      for(int i = 0; i < objStockageComponents.length; i++){
         objStockageComponents[i].setVisible(false);
      }
      for(int i = 0; i < objDestockageComponents.length; i++){
         objDestockageComponents[i].setVisible(true);
      }
      this.rowStockageTitle.setVisible(false);

      // on vide la modélisation de la boite
      modeleBoite.getChildren().clear();

      // si c'est une nouvelle terminale
      if(this.terminale == null || this.terminale.getTerminaleId() == null){
         // on cache les emplacements
         modeleBoite.setVisible(false);
      }else{
         // si la terminale n'est pas modifiable : elle est
         // réservée pour une autre banque
         if(this.terminale.getBanque() != null
            && !SessionUtils.getSelectedBanques(sessionScope).get(0).equals(this.terminale.getBanque())){
            // on cache les emplacements
            modeleBoite.setVisible(false);
         }else{
            modeleBoite.setVisible(true);
            initModelisation();
         }
      }

      getBinder().loadComponent(self);

      // @since 2.1
      // selectionne directement les emplacements à destocker
      if(decos != null){
         Div img;
         for(final EmplacementDecorator deco : decos){
            // indexOf should not throw errors as emplToFills must
            img = getImagesEmplacements().get(getEmplacementDecos().indexOf(deco));
            setSelectedEmplacement(deco);
            handleImage(img);
         }

      }
   }

   /**
    * Validation de la sélection.
    * @version 2.2.1
    */
   public void onClick$validateSelection(){
      if(deplacements.size() < 1){
         throw new WrongValueException(validateSelection, Labels.getLabel("deplacer.emplacement.selection.error"));
      }
      switchToDeplacerMode();
   }

   /**
    * Validation des déplacements.
    */
   @Override
   public void onClick$validateDeplacement(){
      Clients.showBusy(Labels.getLabel("deplacer.emplacement.encours"));
      Events.echoEvent("onLaterDeplacement", self, null);
   }

   /**
    * Validation du stockage.
    */
   public void onClick$validateStockage(){
      Clients.showBusy(Labels.getLabel("deplacer.stockage.encours"));

      if(this.returnMethode != null && !this.returnMethode.equals("")){
         Events.echoEvent("onLaterStockageNewObjects", self, null);
      }else{
         Events.echoEvent("onLaterStockageExistingObjects", self, null);
      }
   }

   /**
    * Validation du déstockage.
    */
   public void onClick$validateDestockage(){
      Clients.showBusy(Labels.getLabel("deplacer.destockage.encours"));
      Events.echoEvent("onLaterDestockage", self, null);
   }

   /**
    * Annulation de la sélection.
    */
   public void onClick$cancelSelection(){
      /*emplacementDecos = new ArrayList<EmplacementDecorator>();
      imagesEmplacements = new ArrayList<Image>();
      deplacements = new ArrayList<EmplacementDecorator>();
      emplacementReserves = new ArrayList<EmplacementDecorator>();
      // on vide la modélisation de la boite
      modeleBoite.getChildren().clear();
      getStockageController().getListeStockages()
      	.setDeplacementMode("normal");

      getStockageController().switchToFicheTerminaleMode(terminale);*/
      postDetachDeplacementEvent();
   }

   /**
    * Annulation des déplacements.
    */
   public void onClick$cancelDeplacement(){
      // emplacementDecos = new ArrayList<EmplacementDecorator>();
      getEmplacementDecos().clear();
      // imagesEmplacements = new ArrayList<Image>();
      getImagesEmplacements().clear();
      deplacementsRestants = new ArrayList<>();
      emplacementsDestDep = new Hashtable<>();
      emplacementReserves = new ArrayList<>();

      deplacements = new ArrayList<>();
      for(int i = 0; i < emplacementDepart.size(); i++){
         final EmplacementDecorator deco = emplacementDepart.get(i);
         deco.setAdrlDestination("");
         deco.setEmplDestination(null);
         deplacements.add(deco);
      }

      this.switchToSelectionMode(true, null);
   }

   /**
    * Annulation du stockage.
    * @param event
    */
   public void onClick$cancelStockage(final Event event){
      /*emplacementDecos = new ArrayList<EmplacementDecorator>();
      imagesEmplacements = new ArrayList<Image>();
      deplacementsRestants = new ArrayList<EmplacementDecorator>();
      emplacementsDestDep = new Hashtable
      	<EmplacementDecorator, EmplacementDecorator>();
      deplacements = new ArrayList<EmplacementDecorator>();
      echantillons = new ArrayList<Echantillon>();
      derives = new ArrayList<ProdDerive>();
      emplacementReserves = new ArrayList<EmplacementDecorator>();
      // on vide la modélisation de la boite
      modeleBoite.getChildren().clear();
      getStockageController().getListeStockages()
      	.switchToNormalMode();
      getStockageController().clearAllPage();
      getStockageController().getListeStockages()
      	.updateAllConteneurs();*/
      postDetachDeplacementEvent();

      //		if (returnMethode != null && !returnMethode.equals("")) {
      //			Tabpanel panel = null;
      getMainWindow().unblockAllPanels();
      if(typeEntite.equals("Echantillon")){
         //				panel = (Tabpanel) getMainWindow()
         //									.getMainTabbox()
         //									.getTabpanels()
         //									.getFellow("echantillonPanel");
         EchantillonController.backToMe(getMainWindow(), page);
      }else if(typeEntite.equals("ProdDerive")){
         //				panel = (Tabpanel) getMainWindow()
         //					.getMainTabbox()
         //					.getTabpanels()
         //					.getFellow("derivePanel");
         ProdDeriveController.backToMe(getMainWindow(), page);
      }

      // si on arrive à récupérer le panel
      //			if (panel != null) {
      //				getMainWindow().unblockAllPanels();
      //				getMainWindow().getMainTabbox().setSelectedPanel(panel);
      //			}
      //		} else {
      //			Tabpanel panel = null;
      //			if (typeEntite.equals("Echantillon")) {
      //				panel = (Tabpanel) getMainWindow()
      //									.getMainTabbox()
      //									.getTabpanels()
      //									.getFellow("echantillonPanel");
      //			} else if (typeEntite.equals("ProdDerive")) {
      //				panel = (Tabpanel) getMainWindow()
      //					.getMainTabbox()
      //					.getTabpanels()
      //					.getFellow("derivePanel");
      //			}
      //
      //			// si on arrive à récupérer le panel
      //			if (panel != null) {
      //				getMainWindow().unblockAllPanels();
      //				getMainWindow().getMainTabbox().setSelectedPanel(panel);
      //			}
      //		}
   }

   /**
    * Annulation du déstockage.
    */
   public void onClick$cancelDestockage(){
      /*emplacementDecos = new ArrayList<EmplacementDecorator>();
      imagesEmplacements = new ArrayList<Image>();
      deplacements = new ArrayList<EmplacementDecorator>();
      emplacementReserves = new ArrayList<EmplacementDecorator>();
      // on vide la modélisation de la boite
      modeleBoite.getChildren().clear();
      getStockageController().getListeStockages()
      	.setDeplacementMode("normal");

      getStockageController().switchToFicheTerminaleMode(terminale);*/
      postDetachDeplacementEvent();
   }

   /**
    * Sélection d'une ligne.
    */
   public void onSelect$lignesBox(){
      numeroLigne = lignesBox.getSelectedIndex() + 1;
      colonnes = ManagerLocator.getTerminaleManager().getCodesColonnesManager(terminale, numeroLigne);
      numeroColonne = 1;
   }

   /**
    * Sélection d'une colonne.
    */
   public void onSelect$colonnesBox(){
      numeroColonne = colonnesBox.getSelectedIndex() + 1;
   }

   /**
    * Validation de la coordonnée saisie.
    */
   public void onClick$selectCoordonnee(){
      final Integer position =
         ManagerLocator.getEmplacementManager().getPositionByCoordonnees(terminale, numeroLigne, numeroColonne);

      if(position > 0){
         final Div img = getImagesEmplacements().get(position - 1);
         // si on est en mode de sélection
         if(selectionMode){
            selectImageInSelectionMode(img);
         }else if(deplacementMode){
            selectImageIndeplacementMode(img);
         }else if(stockerMode){
            selectImageInStockageMode(img);
         }
      }
   }

   /**
    * Méthode gérant le stockage d'objets existants.
    */
   public void onLaterStockageExistingObjects(){

      // s'il n'y a pas d'erreurs lors de l'update
      if(stockExistingObjects().size() == 0){

         // on vide toutes les listes utilisées
         // emplacementDecos = new ArrayList<EmplacementDecorator>();
         getEmplacementDecos().clear();
         // imagesEmplacements = new ArrayList<Image>();
         getImagesEmplacements().clear();
         deplacementsRestants = new ArrayList<>();
         emplacementsDestDep = new Hashtable<>();
         deplacements = new ArrayList<>();
         emplacementDepart = new ArrayList<>();
         echantillons = new ArrayList<>();
         derives = new ArrayList<>();
         emplacementReserves = new ArrayList<>();

         Tabpanel panel = null;
         if(typeEntite.equals("Echantillon")){
            panel = (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("echantillonPanel");
         }else if(typeEntite.equals("ProdDerive")){
            panel = (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("derivePanel");
         }

         // si on arrive à récupérer le panel
         if(panel != null){
            getMainWindow().unblockAllPanels();
            getMainWindow().getMainTabbox().setSelectedPanel(panel);
         }
         // on vérifie que l'on retrouve bien la page contenant la liste
         // des stockages
         /*if (getStockageController() != null) {
         	getStockageController().getListeStockages()
         		.switchToNormalMode();
         	getStockageController().clearAllPage();
         	getStockageController().getListeStockages()
         		.updateAllConteneurs();
         }*/
         postDetachDeplacementEvent();
      }

      Clients.clearBusy();

   }

   /**
    * Méthode gérant le stockage de nouveuax objets.
    */
   public void onLaterStockageNewObjects(){

      // s'il n'y a pas d'erreurs lors de l'update
      final Hashtable<Object, Emplacement> results = stockNewObjects();

      // on vide toutes les listes utilisées
      //emplacementDecos = new ArrayList<EmplacementDecorator>();
      getEmplacementDecos().clear();
      // imagesEmplacements = new ArrayList<Image>();
      getImagesEmplacements().clear();
      deplacementsRestants = new ArrayList<>();
      emplacementsDestDep = new Hashtable<>();
      deplacements = new ArrayList<>();
      emplacementDepart = new ArrayList<>();
      echantillons = new ArrayList<>();
      derives = new ArrayList<>();
      emplacementReserves = new ArrayList<>();

      Tabpanel panel = null;
      if(typeEntite.equals("Echantillon")){
         panel = (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("echantillonPanel");
      }else if(typeEntite.equals("ProdDerive")){
         panel = (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("derivePanel");
      }

      // si on arrive à récupérer le panel
      if(panel != null){
         getMainWindow().unblockAllPanels();
         getMainWindow().getMainTabbox().setSelectedPanel(panel);
      }

      if(Path.getComponent(path) != null){
         // on envoie un event à cette page avec
         // les échantillons sélectionnés
         Events.postEvent(new Event(returnMethode, Path.getComponent(path), results));
      }

      postDetachDeplacementEvent();

      Clients.clearBusy();
   }

   /**
    * Méthode gérant le déplacement d'une enceinte terminale.
    */
   @Override
   public void onLaterDeplacement(){

      // s'il n'y a pas d'erreurs lors de l'update
      if(!getObjStatutIncompatibleForRetour(prepareObjectsAndEmplacements(), null) && deplacerEmplacements()){

         // Clients.clearBusy();
         if(getEmplForRetours().size() > 0){
            // on demande à l'utilisateur s'il souhaite
            // créer des retours
            if(askForRetoursCreation(getEmplForRetours().size(), null, null, new ArrayList<OldEmplTrace>(), null,
               ManagerLocator.getOperationTypeManager().findByNomLikeManager("creation", true).get(0))){

               // ouverture de la modale
               openRetourFormModale(null, true, null, null,
                  getEmplForRetours().stream().map(o -> o.getTkObj()).collect(Collectors.toList()), getEmplForRetours(), null,
                  null, null, null, Labels.getLabel("ficheRetour.deplacement"), null);
            }
         }

         postDetachDeplacementEvent();
      }else{
         Clients.clearBusy();
      }

   }

   /**
    * Sauvegarde le déplacements de plusieurs objets.
    * @return Liste d'erreurs
    */
   protected boolean deplacerEmplacements(){
      // List<String> errorMsg = new ArrayList<String>();

      List<BoiteImpression> listeBoitesDepart = new ArrayList<>();
      List<BoiteImpression> listeBoitesArrivee = new ArrayList<>();

      listeBoitesDepart = createBoitesDepart();

      // création du document, de la page et de son titre
      final Document doc = ManagerLocator.getXmlUtils().createJDomDocumentBoites();
      final Element root = doc.getRootElement();
      final Element page = ManagerLocator.getXmlUtils().addPageBoite(root, Labels.getLabel("impression.boite.title.deplacement"));

      final StringBuffer adrImages = new StringBuffer();
      adrImages.append(((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getContextPath());
      adrImages.append("/images/icones/emplacements/");
      for(int i = 0; i < listeBoitesDepart.size(); i++){
         ManagerLocator.getXmlUtils().addBoite(page, listeBoitesDepart.get(i), adrImages.toString());
      }

      try{

         ManagerLocator.getEmplacementManager().deplacerMultiEmplacementsManager(emplacementsFinaux,
            SessionUtils.getLoggedUser(sessionScope));

         // pour chaque emplacement, on maj l'objet contenu
         for(int i = 0; i < emplacementsFinaux.size(); i++){
            final Emplacement empl = emplacementsFinaux.get(i);
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
               if(getObjectTabController().getProdDeriveController() != null){
                  // update du dérivé dans la liste
                  getObjectTabController().getProdDeriveController().getListe().updateObjectGridListFromOtherPage(derive, false);
               }
            }
         }

         listeBoitesArrivee = createBoitesArrivee();

         for(int i = 0; i < listeBoitesArrivee.size(); i++){
            ManagerLocator.getXmlUtils().addBoite(page, listeBoitesArrivee.get(i), adrImages.toString());
         }

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

   /**
    * Méthode gérant le déstockage d'emplacements.
    */
   public void onLaterDestockage(){

      // s'il n'y a pas d'erreurs lors de l'update
      /*if (destockEmplacements().size() == 0) {
      	postDetachDeplacementEvent();
      }*/

      final List<Echantillon> echansToDestock = new ArrayList<>();
      final List<ProdDerive> derivesToDestock = new ArrayList<>();
      // pour chaque destockage
      for(int i = 0; i < deplacements.size(); i++){
         final EmplacementDecorator deco = deplacements.get(i);
         final Emplacement empl = deco.getEmplacement();

         if(empl.getEntite().getNom().equals("Echantillon")){
            final Echantillon echan = ManagerLocator.getEchantillonManager().findByIdManager(empl.getObjetId());
            if(echan != null){
               echansToDestock.add(echan);
            }
         }else if(empl.getEntite().getNom().equals("ProdDerive")){
            final ProdDerive derive = ManagerLocator.getProdDeriveManager().findByIdManager(empl.getObjetId());
            if(derive != null){
               derivesToDestock.add(derive);
            }
         }
      }

      final CessionController tabController = (CessionController) CessionController.backToMe(getMainWindow(), page);

      tabController.switchToCreateMode(echansToDestock, derivesToDestock, this.terminale);

      postDetachDeplacementEvent();

      Clients.clearBusy();

   }

   /**
    * Sauvegarde le stockage d'objets existants.
    * @return
    */
   protected List<String> stockExistingObjects(){
      final List<String> errorMsg = new ArrayList<>();
      final List<TKStockableObject> echansFinaux = new ArrayList<>();
      final List<ProdDerive> derivesFinaux = new ArrayList<>();
      final List<Emplacement> emplacementsFinaux = new ArrayList<>();

      final List<Integer> upObjsIds = new ArrayList<>();

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("saveEmplacementTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);
      try{
         final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
         final Entite deriveEntite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
         for(int i = 0; i < deplacements.size(); i++){
            final EmplacementDecorator deco = deplacements.get(i);

            if(deco.getEmplDestination() != null){
               final Emplacement empl = deco.getEmplDestination().getEmplacement();

               if(typeEntite.equals("Echantillon")){
                  empl.setEntite(echanEntite);
                  empl.setObjetId(echantillons.get(i).getEchantillonId());
                  echansFinaux.add(echantillons.get(i));
                  upObjsIds.add(echantillons.get(i).getEchantillonId());
               }else if(typeEntite.equals("ProdDerive")){
                  empl.setEntite(deriveEntite);
                  empl.setObjetId(derives.get(i).getProdDeriveId());
                  derivesFinaux.add(derives.get(i));
                  upObjsIds.add(derives.get(i).getProdDeriveId());
               }

               empl.setVide(false);
               emplacementsFinaux.add(empl);
            }
         }

         ManagerLocator.getEmplacementManager().saveMultiEmplacementsManager(emplacementsFinaux);

         if(typeEntite.equals("Echantillon")){
            updateEchantillons(emplacementsFinaux, echansFinaux);
         }else if(typeEntite.equals("ProdDerive")){
            updateProdDerives(emplacementsFinaux, derivesFinaux);
         }

         if(typeEntite.equals("Echantillon")){
            if(getObjectTabController().getEchantillonController() != null){
               getObjectTabController().getEchantillonController().getListe().updateGridByIds(upObjsIds, false, true);
            }

            // update prelevements parents
            if(getObjectTabController().getPrelevementController() != null){
               getObjectTabController().getPrelevementController().getListe()
                  .updateGridByIds(ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(upObjsIds, echanEntite,
                     ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0),
                     SessionUtils.getSelectedBanques(sessionScope), false), false, true);
            }
         }else if(typeEntite.equals("ProdDerive")){
            if(getObjectTabController().getProdDeriveController() != null){
               //					for (ProdDerive deriveToUpdate : derivesFinaux) {
               //						getObjectTabController().
               //							getProdDeriveController().getListe()
               //								.updateObjectGridListFromOtherPage(deriveToUpdate, false);
               //						getObjectTabController().
               //							getProdDeriveController().switchToOnlyListeMode();
               //					}
               getObjectTabController().getProdDeriveController().getListe().updateGridByIds(upObjsIds, false, true);
            }
            // update echantillon parents
            if(getObjectTabController().getEchantillonController() != null){
               getObjectTabController().getEchantillonController().getListe()
                  .updateGridByIds(ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(upObjsIds,
                     deriveEntite, ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0),
                     SessionUtils.getSelectedBanques(sessionScope), false), false, true);
            }

            // update prelevements parents
            if(getObjectTabController().getPrelevementController() != null){
               getObjectTabController().getPrelevementController().getListe()
                  .updateGridByIds(ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(upObjsIds,
                     deriveEntite, ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0),
                     SessionUtils.getSelectedBanques(sessionScope), false), false, true);
            }
         }

         ManagerLocator.getTxManager().commit(status);

         createFileHtmlToPrintLaterStockage();

         //		} catch (ValidationException ve) {
         //			errorMsg.add("- Erreur lors de la validation.");
         //		} catch (InvalidPositionException ipose) {
         //			errorMsg.add("- Erreur sur la "
         //					+ "position d'un emplacement.");
         //		} catch (RequiredObjectIsNullException re) {
         //			errorMsg.add("- Objet manquant lors du stockage.");
         //		} catch (EntiteObjectIdNotExistException nee) {
         //			errorMsg.add("- Objet à stocker inexistant.");
         //		} catch (DoublonFoundException de) {
         //			errorMsg.add("- Erreur sur l'emplacement de stockage.");
         //		}
      }catch(final Exception e){
         ManagerLocator.getTxManager().rollback(status);
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         Clients.clearBusy();
      }

      //		// s'il y a des erreurs, on fait apparaître une fenêtre contenant
      //		// la liste de celles-ci
      //		if (errorMsg.size() > 0) {
      //			// ferme wait message
      //			Clients.clearBusy();
      //
      //			StringBuffer sb = new StringBuffer();
      //			for (int i = 0; i < errorMsg.size(); i++) {
      //				sb.append(errorMsg.get(i));
      //				if (i < errorMsg.size() - 1) {
      //					sb.append("\n");
      //				}
      //			}
      //			Messagebox.show(sb.toString(), "Error",
      //					Messagebox.OK, Messagebox.ERROR);
      //		}

      return errorMsg;
   }

   protected Hashtable<Object, Emplacement> stockNewObjects(){
      final Hashtable<Object, Emplacement> results = new Hashtable<>();

      final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      final Entite deriveEntite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      for(int i = 0; i < deplacements.size(); i++){
         final EmplacementDecorator deco = deplacements.get(i);

         if(deco.getEmplDestination() != null){
            final Emplacement empl = deco.getEmplDestination().getEmplacement();

            empl.setVide(false);
            empl.setAdrl(deco.getEmplDestination().getAdrl());
            if(typeEntite.equals("Echantillon")){
               empl.setEntite(echanEntite);
               empl.setObjetId(echantillons.get(i).getEchantillonId());
               results.put(echantillons.get(i), empl);
            }else if(typeEntite.equals("ProdDerive")){
               empl.setEntite(deriveEntite);
               empl.setObjetId(derives.get(i).getProdDeriveId());
               results.put(derives.get(i), empl);
            }
         }
      }

      return results;
   }

   public void updateEchantillons(final List<Emplacement> emplFinaux, final List<TKStockableObject> echans){
      final ObjetStatut statut = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);

      for(int i = 0; i < echans.size(); i++){
         final Echantillon echanToUpdate = (Echantillon) echans.get(i);
         // Update echantillon
         final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echanToUpdate);
         final List<OperationType> ops = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Stockage", true);
         ManagerLocator.getEchantillonManager().updateObjectManager(echanToUpdate,
            SessionUtils.getSelectedBanques(sessionScope).get(0), prlvt, echanToUpdate.getCollaborateur(), statut,
            emplFinaux.get(i), echanToUpdate.getEchantillonType(), null, null, echanToUpdate.getQuantiteUnite(),
            echanToUpdate.getEchanQualite(), echanToUpdate.getModePrepa(), null, null, null, null,
            SessionUtils.getLoggedUser(sessionScope), false, ops, null);
      }
   }

   public void updateProdDerives(final List<Emplacement> emplFinaux, final List<ProdDerive> prods){
      final ObjetStatut statut = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);

      for(int i = 0; i < prods.size(); i++){
         final ProdDerive deriveToUpdate = prods.get(i);
         // update du dérvié
         final List<OperationType> ops = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Stockage", true);
         ManagerLocator.getProdDeriveManager().updateObjectManager(deriveToUpdate,
            SessionUtils.getSelectedBanques(sessionScope).get(0), deriveToUpdate.getProdType(), statut,
            deriveToUpdate.getCollaborateur(), emplFinaux.get(i), deriveToUpdate.getVolumeUnite(), deriveToUpdate.getConcUnite(),
            deriveToUpdate.getQuantiteUnite(), deriveToUpdate.getModePrepaDerive(), deriveToUpdate.getProdQualite(),
            deriveToUpdate.getTransformation(), null, null, null, null, SessionUtils.getLoggedUser(sessionScope), false, ops,
            null);
      }
   }

   /**
    * Sauvegarde le stockage d'objets existants.
    * @return
    */
   protected List<String> destockEmplacements(){
      /*List<String> errorMsg = new ArrayList<String>();
      List<Echantillon> echansToDestock = new ArrayList<Echantillon>();
      List<ProdDerive> derivesToDestock = new ArrayList<ProdDerive>();
      List<Emplacement> emplacementsFinaux = new ArrayList<Emplacement>();

      try {
      	// pour chaque destockage
      	for (int i = 0; i < deplacements.size(); i++) {
      		EmplacementDecorator deco = deplacements.get(i);
      		Emplacement empl = deco.getEmplacement();

      		if (empl.getEntite().getNom().equals("Echantillon")) {
      			Echantillon echan = ManagerLocator.getEchantillonManager()
      				.findByIdManager(empl.getObjetId());
      			if (echan != null) {
      				echansToDestock.add(echan);
      			}
      		} else if (empl.getEntite().getNom().equals("ProdDerive")) {
      			ProdDerive derive = ManagerLocator.getProdDeriveManager()
      				.findByIdManager(empl.getObjetId());
      			if (derive != null) {
      				derivesToDestock.add(derive);
      			}
      		}
      		empl.setVide(true);
      		empl.setEntite(null);
      		empl.setObjetId(null);
      		emplacementsFinaux.add(empl);
      	}

      	ManagerLocator.getEmplacementManager()
      		.saveMultiEmplacementsManager(
      		emplacementsFinaux);

      } catch (ValidationException ve) {
      	errorMsg.add("- Erreur lors de la validation.");
      } catch (InvalidPositionException ipose) {
      	errorMsg.add("- Erreur sur la "
      			+ "position d'un emplacement.");
      } catch (RequiredObjectIsNullException re) {
      	errorMsg.add("- Objet manquant lors du stockage.");
      } catch (EntiteObjectIdNotExistException nee) {
      	errorMsg.add("- Objet à stocker inexistant.");
      } catch (DoublonFoundException de) {
      	errorMsg.add("- Erreur sur l'emplacement de stockage.");
      }

      // s'il y a des erreurs, on fait apparaître une fenêtre contenant
      // la liste de celles-ci
      if (errorMsg.size() > 0) {
      	// ferme wait message
      	Clients.showBusy(null, false);

      	StringBuffer sb = new StringBuffer();
      	for (int i = 0; i < errorMsg.size(); i++) {
      		sb.append(errorMsg.get(i));
      		if (i < errorMsg.size() - 1) {
      			sb.append("\n");
      		}
      	}
      	try {
      		Messagebox.show(sb.toString(), "Error",
      				Messagebox.OK, Messagebox.ERROR);
      	} catch (InterruptedException e1) {
      		log.error(e1);
      	}
      } else {
      	destockEchantillons(echansToDestock);
      	destockProdDerives(derivesToDestock);
      }

      return errorMsg;*/
      return null;
   }

   /**
    * Méthode appelée lorsque l'utilisateur choisit une terminale.
    * @param node TerminaleNode représentant la terminale.
    * @param conteneur de destination
    * @param missBanks TK-251 le conteneur destination n'est pas accessible pas toutes ces banques
    * @version 2.2.1
    */
   @Override
   public void definirTerminaleDestination(final TerminaleNode node, final Conteneur cont, final List<Banque> anks){
      // si le node est vide, c'est que la destination n'est pas valide
      if(node != null){
         if(!node.isVide()){
            setTerminale(node.getTerminale());
            if(node.availableTerminale()){
               // on vide la modélisation de la boite
               badTerminaleRow.setVisible(false);
               modeleBoite.getChildren().clear();
               rowViewBoite.setVisible(true);
               modeleBoite.setVisible(true);
               // on modélise la boite
               initModelisation();
               getBinder().loadAttribute(self.getFellow("nomLabel"), "value");

               if(terminale.getEntite() != null && (deplacementMode || stockerMode)){
                  final StringBuffer sb = new StringBuffer();
                  sb.append(Labels.getLabel("deplacer.emplacement.terminale.entite.reservee"));
                  sb.append(" ");
                  sb.append(terminale.getEntite().getNom());
                  sb.append("s.");
                  entiteReservee = sb.toString();
                  entiteReserveeLabel.setValue(entiteReservee);
                  entiteReserveeRow.setVisible(true);
               }else{
                  entiteReserveeRow.setVisible(false);
               }
            }else{
               final StringBuffer sb = new StringBuffer();
               sb.append(Labels.getLabel("deplacer.emplacement.terminale.reservee"));
               sb.append(" ");
               sb.append(getTerminale().getBanque().getNom());
               badTerminaleLabel.setValue(sb.toString());
               badTerminaleRow.setVisible(true);

               modeleBoite.getChildren().clear();
               rowViewBoite.setVisible(false);
               getBinder().loadAttribute(self.getFellow("nomLabel"), "value");
            }
         }else{
            badTerminaleError = Labels.getLabel("deplacer.emplacement.terminale.new");
            badTerminaleLabel.setValue(badTerminaleError);
            badTerminaleRow.setVisible(true);
            modeleBoite.getChildren().clear();
            rowViewBoite.setVisible(false);
            setTerminale(node.getTerminale());
            getBinder().loadAttribute(self.getFellow("nomLabel"), "value");
         }
      }
   }

   @Override
   public void initEmplacements(){
      // on récupère les emplacements de la boite
      List<Emplacement> emplacements = ManagerLocator.getEmplacementManager().findByTerminaleWithOrder(terminale);
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
            final EmplacementDecorator deco = new EmplacementDecorator(new Emplacement());
            deco.getEmplacement().setPosition(cpt);
            deco.getEmplacement().setTerminale(terminale);
            deco.setPosition(cpt);
            deco.setTerminale(terminale);
            deco.generateLibelle(adrlTerminale, this.terminale);
            getEmplacementDecos().add(deco);
            ++cpt;
         }
         // on remplit un decorator avec l'emplacement
         final EmplacementDecorator deco = new EmplacementDecorator(emp);
         deco.setTerminale(terminale);
         deco.setPosition(emp.getPosition());
         deco.setCode(codes.get(i));
         deco.setType(typesObjs.get(i));
         deco.generateLibelle(adrlTerminale, this.terminale);
         if(emplacementDepart.contains(deco)){
            deco.setEmplacementDepart(true);
         }else{
            deco.setEmplacementDepart(false);
         }

         getEmplacementDecos().add(deco);
         ++cpt;

         // since 2.0.13 ajout objet au decorator pour retrouver facilement statut
         deco.setTkStockObj((TKStockableObject) empObjs.inverseBidiMap().get(emp));
      }

      // si aucun emplacement n'est défini à la position "cpt"
      // on crée un decorator vide
      while(terminale.getTerminaleType().getNbPlaces() > getEmplacementDecos().size()){
         final EmplacementDecorator deco = new EmplacementDecorator(new Emplacement());
         deco.getEmplacement().setPosition(cpt);
         deco.getEmplacement().setTerminale(terminale);
         deco.setPosition(cpt);
         deco.setTerminale(terminale);
         deco.generateLibelle(adrlTerminale, this.terminale);
         getEmplacementDecos().add(deco);
         ++cpt;
      }
      emplacements = null;
   }

   //
   //	/**
   //	 * Méthode modélisant la structure et le contenu d'une boite.
   //	 */
   @Override
   public void initModelisation(){
      //		// initialise les listes contenant les emplacements
      //		initEmplacements();
      //		imagesEmplacements = new ArrayList<Image>();
      //
      //		if (terminale.getTerminaleType().getScheme() == null) {
      //
      //			if (terminale.getTerminaleType().getHauteur() != null
      //					&& terminale.getTerminaleType().getLongueur() != null) {
      //				Vbox mainVbox = new Vbox();
      //				mainVbox.setSpacing("0");
      //
      //				// création des abscisses
      //				Hbox separator = new Hbox();
      //				separator.setHeight("5px");
      //				separator.setParent(mainVbox);
      //				Hbox hBoxA = new Hbox();
      //				hBoxA.setSpacing("0");
      //				Div div = new Div();
      //				div.setWidth("20px");
      //				div.setParent(hBoxA);
      //				for (int j = 0; j < terminale.getTerminaleType()
      //					.getLongueur(); j++) {
      //					Div divAbs = new Div();
      //					divAbs.setWidth("30px");
      //					divAbs.setParent(hBoxA);
      //					divAbs.setAttribute("align", "center");
      //					Label abs = new Label();
      //					abs.setSclass("formLabel");
      //					abs.setValue(getValueAbscisse(j + 1));
      //					abs.setParent(divAbs);
      //				}
      //				hBoxA.setParent(mainVbox);
      //
      //				int cpt = 0;
      //				List<Hbox> lignesBox = new ArrayList<Hbox>();
      //				for (int i = 0; i < terminale.getTerminaleType()
      //					.getHauteur(); i++) {
      //					Hbox hBox = new Hbox();
      //					hBox.setSpacing("0");
      //
      //					// création des ordonnées
      //					Div divOrd = new Div();
      //					divOrd.setWidth("20px");
      //					divOrd.setHeight("23px");
      //					divOrd.setParent(hBox);
      //					Label ord = new Label();
      //					ord.setSclass("formLabel");
      //					ord.setValue(getValueOrdonnee(i + 1));
      //					ord.setParent(divOrd);
      //					for (int j = 0; j < terminale.getTerminaleType()
      //						.getLongueur(); j++) {
      //						EmplacementDecorator deco = emplacementDecos.get(cpt);
      //
      //						Image img = createImage(deco);
      //						img.setParent(hBox);
      //						imagesEmplacements.add(img);
      //
      //						++cpt;
      //					}
      //					//hBox.setParent(mainVbox);
      //					// on stocke les lignes dans une liste
      //					lignesBox.add(hBox);
      //				}
      //
      //				// si la numérotation commence sur la 1ere ligne
      //				if (terminale.getTerminaleType().getDepartNumHaut()) {
      //					for (int i = 0; i < lignesBox.size(); i++) {
      //						lignesBox.get(i).setParent(mainVbox);
      //					}
      //				} else {
      //					// sinon on inverse l'affichage des lignes
      //					for (int i = lignesBox.size() - 1; i >= 0; i--) {
      //						lignesBox.get(i).setParent(mainVbox);
      //					}
      //				}
      //
      //				mainVbox.setParent(modeleBoite);
      //			}
      //
      //		} else {
      //
      //			String[] values = terminale.getTerminaleType()
      //				.getScheme()
      //				.split(";");
      //			Vbox mainVbox = new Vbox();
      //			mainVbox.setSpacing("0");
      //			mainVbox.setWidth("100%");
      //			mainVbox.setAlign("center");
      //			int cpt = 0;
      //			List<Hbox> lignesBox = new ArrayList<Hbox>();
      //			// si la numérotation commence sur la 1ere ligne
      //			if (this.terminale.getTerminaleType().getDepartNumHaut()) {
      //				for (int i = 0; i < values.length; i++) {
      //					int nbPlaces = Integer.parseInt(values[i]);
      //					Hbox hBox = new Hbox();
      //					hBox.setSpacing("0");
      //					hBox.setAlign("center");
      //					for (int j = 0; j < nbPlaces; j++) {
      //						EmplacementDecorator deco = emplacementDecos.get(cpt);
      //
      //						Image img = createImage(deco);
      //						img.setParent(hBox);
      //						imagesEmplacements.add(img);
      //
      //						++cpt;
      //					}
      //					//hBox.setParent(mainVbox);
      //					lignesBox.add(hBox);
      //				}
      //			} else {
      //				// sinon on inverse le parcours des lignes
      //				for (int i = values.length - 1; i >= 0; i--) {
      //					int nbPlaces = Integer.parseInt(values[i]);
      //					Hbox hBox = new Hbox();
      //					hBox.setSpacing("0");
      //					hBox.setAlign("center");
      //					for (int j = 0; j < nbPlaces; j++) {
      //						EmplacementDecorator deco = emplacementDecos.get(cpt);
      //
      //						Image img = createImage(deco);
      //						img.setParent(hBox);
      //						imagesEmplacements.add(img);
      //
      //						++cpt;
      //					}
      //					lignesBox.add(0, hBox);
      //				}
      //			}
      //			for (int i = 0; i < lignesBox.size(); i++) {
      //				lignesBox.get(i).setParent(mainVbox);
      //			}
      //			mainVbox.setParent(modeleBoite);
      //		}
      //
      super.initModelisation();

      // on init les listes pour saisir une coordonnée
      lignes = ManagerLocator.getTerminaleManager().getCodesLignesManager(terminale);
      numeroLigne = 1;
      colonnes = ManagerLocator.getTerminaleManager().getCodesColonnesManager(terminale, numeroLigne);
      numeroColonne = 1;
      getBinder().loadAttribute(self.getFellow("lignesBox"), "model");
      getBinder().loadAttribute(self.getFellow("colonnesBox"), "model");
   }

   @Override
   public String getValueAbscisse(final Integer num){
      if(terminale.getTerminaleNumerotation().getColonne().equals("CAR")){
         return Utils.createListChars(num, null, new ArrayList<String>()).get(num - 1);
      }
      return String.valueOf(num);
   }

   @Override
   public String getValueOrdonnee(final Integer num){
      if(terminale.getTerminaleNumerotation().getLigne().equals("CAR")){
         return Utils.createListChars(num, null, new ArrayList<String>()).get(num - 1);
      }
      return String.valueOf(num);
   }

   /**
    * Méthode créant une image représentant un emplacement.
    * @since 2.0.13 remplace l'objet Img par une pile de Div
    * (EmplacementDiv) pour gérer l'image + calque objet statut
    * en full CSS.
    * @param deco Decorator de 'emplacement.
    * @return L'image.
    * @version 2.0.13
    */
   @Override
   public Div createImage(final EmplacementDecorator deco){
      final EmplacementDiv div = new EmplacementDiv(28, 21, deco.getLibelle());
      div.setSclass("imageEmplacement");
      div.setAttribute("empDeco", deco);
      div.addForward("onClick", self, "onClickImage", div);

      div.setImageStyle(getImageStyle(deco));

      // si l'image est vide
      if(deco.getVide()){
         if(deplacementMode || stockerMode || selectionMode){
            div.setStyle("cursor:pointer;");
            div.setDraggable("true");
            div.setDroppable("true");
            div.addForward("onDrop", self, "onDropImage", div);
         }else if(destockageMode){
            div.setSclass("imageNonSelectableEmplacement");
         }
         if(emplacementsDestDep.containsKey(deco)){
            div.setSclass("imageMovedEmplacement");
         }
         if(emplacementReserves.contains(deco)){
            div.setSclass("imageMovedEmplacement");
         }
      }else{
         // sinon
         if(deplacementMode || selectionMode || destockageMode){
            div.setStyle("cursor:pointer;");
            div.setDraggable("true");
            div.setDroppable("true");
            div.addForward("onDrop", self, "onDropImage", div);
         }else if(stockerMode){
            div.setSclass("imageNonSelectableEmplacement");
         }

         if(emplacementsDestDep.containsKey(deco)){
            div.setSclass("imageMovedEmplacement");
         }

         if(deplacements.contains(deco)){
            final EmplacementDecorator tmp = deplacements.get(deplacements.indexOf(deco));
            if(tmp.getEmplacementDepart()){
               if(tmp.getEmplDestination() != null){
                  div.setSclass("imageValideEmplacement");
               }else{
                  div.setSclass("imageSelectedEmplacement");
               }
            }

            if(destockageMode){
               div.setSclass("imageDestockEmplacement");
            }
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
   //		sb.append(imageSrc);
   //
   //		// si l'image est vide
   //		if (deco.getVide()) {
   //			sb.append("VIDE");
   //		} else {
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
   //
   //		}
   //		sb.append(".png");
   //		return sb.toString();
   //	}

   public void descendreFenetre(){
      final String id = rowHistoriqueTitle.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Méthode appelée lors du clic sur une image.
    * @param e
    */
   @Override
   public void onClickImage(final Event e){

      if(e.getData() != null){
         final Div img = (Div) e.getData();
         handleImage(img);
      }
   }

   private void handleImage(final Div img){
      if(img != null){
         // si on est en mode de sélection
         if(selectionMode){
            selectImageInSelectionMode(img);
         }else if(deplacementMode){
            selectImageIndeplacementMode(img);
            if(deplacementsRestants.size() == 0){
               descendreFenetre();
            }
         }else if(stockerMode){
            selectImageInStockageMode(img);
            if(deplacementsRestants.size() == 0){
               descendreFenetre();
            }
         }else if(destockageMode){
            selectImageInDestockageMode(img);
         }
      }
   }

   @Override
   public void onDropImage(final DropEvent e){
      e.getTarget();
      if(e.getTarget() != null){

         final Div imgDep = (Div) e.getDragged();
         final Div imgDest = (Div) e.getTarget();
         //			EmplacementDecorator decoDep = (EmplacementDecorator) imgDep
         //					.getAttribute("empDeco");
         //			EmplacementDecorator decoDest = (EmplacementDecorator) imgDest
         //					.getAttribute("empDeco");
         int depIdx = getImagesEmplacements().indexOf(imgDep);
         int destIdx = getImagesEmplacements().indexOf(imgDest);

         // verifie l'ordre des indexes avant le subList
         // et corrige au besoin
         if(destIdx < depIdx){
            final int tempIdx = depIdx;
            depIdx = destIdx;
            destIdx = tempIdx;
         }

         final List<Div> imgs = new ArrayList<>();
         imgs.addAll(getImagesEmplacements().subList(depIdx, destIdx + 1));

         for(final Div image : imgs){
            handleImage(image);
            if(deplacementMode){
               if(deplacementsRestants.size() == 0){
                  break;
               }
            }else if(stockerMode){
               if(deplacementsRestants.size() == 0){
                  break;
               }
            }
         }
      }
   }

   /**
    * Sélectionne une image représentant un emplacement de la terminale
    * en mode "sélection".
    * @param img Div à sélectionner.
    */
   public void selectImageInSelectionMode(final Div img){
      final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");

      if(!deco.getVide()){
         // si l'emplacement a déjà été sélectionné, on
         // le déselctionne
         if(deplacements.contains(deco)){
            img.setSclass("imageEmplacement");
            deco.setEmplacementDepart(false);
            deplacements.remove(deco);
         }else{
            img.setSclass("imageSelectedEmplacement");
            deco.setEmplacementDepart(true);
            deplacements.add(deco);
         }
      }
      final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
      gridHistorique.setModel(list);
      getBinder().loadComponent(gridHistorique);
   }

   /**
    * Sélectionne une image représentant un emplacement de la terminale
    * en mode "déplacement".
    * @param img Image à sélectionner.
    */
   public void selectImageIndeplacementMode(final Div img){
      try{
         final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");
         // si l'emplacement n'est pas l'emplacement courant
         if(!deco.equals(selectedEmplacement)){
            // l'image ne représente pas un emplacement de destination
            if(!emplacementsDestDep.containsKey(deco)){
               if(selectedEmplacement != null){

                  // TK-251
                  checkEmplacementDeplacementConteneurCoherence(selectedEmplacement, deco);
                  // Le contrôle porte aussi sur le déplacement subsequent de l'échantillon/dérivé
                  // lors d'un échange de place
                  if(!deco.getVide()){
                     checkEmplacementDeplacementConteneurCoherence(deco, selectedEmplacement);
                  }

                  // on vérifie que la terminale peut contenir
                  // ce type d'objet
                  if(terminale.getEntite() == null || (terminale.getEntite() != null
                     && terminale.getEntite().equals(selectedEmplacement.getEmplacement().getEntite()))){
                     img.setSclass("imageMovedEmplacement");
                     // on met à jour l'emplacement courant => celui
                     // pour qui on vient de choisir la destination
                     final EmplacementDecorator upDeco = deplacements.get(deplacements.indexOf(selectedEmplacement));
                     upDeco.setAdrlDestination(deco.getAdrl());
                     upDeco.setEmplDestination(deco);
                     // s'il se trouve dans la boite actuelle
                     // on change le style de l'image en violet
                     if(terminale.equals(upDeco.getEmplacement().getTerminale())){
                        final Div upImg = getImagesEmplacements().get(upDeco.getPosition() - 1);
                        upImg.setSclass("imageValideEmplacement");
                     }
                     // on ajoute la destination à la hashtable
                     emplacementsDestDep.put(deco, upDeco);
                     emplacementsDestDep.put(upDeco, deco);
                     // si la destination n'est pas vide
                     if(!deco.getVide()){
                        // si l'emplacement n'est pas un de départ
                        if(!deco.getEmplacementDepart()){
                           // on l'ajoute à la liste des déplacements
                           deco.setAdrlDestination(upDeco.getAdrl());
                           deco.setEmplDestination(upDeco);
                           deplacements.add(deco);
                        }else{
                           // on met à jour l'emplacement
                           final EmplacementDecorator tmp = deplacements.get(deplacements.indexOf(deco));
                           tmp.setAdrlDestination(upDeco.getAdrl());
                           tmp.setEmplDestination(upDeco);
                           img.setSclass("imageValideEmplacement");
                           // on l'enlève de la liste des emplacements
                           // à déplacer
                           deplacementsRestants.remove(tmp);
                        }
                     }
                     final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
                     gridHistorique.setModel(list);
                     // on enlève l'emplacement courant de la liste des
                     // emplacements à déplacer
                     deplacementsRestants.remove(selectedEmplacement);
                     if(deplacementsRestants.size() > 0){
                        selectedEmplacement = deplacementsRestants.get(0);
                     }else{
                        selectedEmplacement = null;
                     }
                     getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
                  }
               }
            }else{
               if(deplacements.contains(emplacementsDestDep.get(deco))){
                  // on récupère l'emplacement de départ
                  final EmplacementDecorator decoOtherSide =
                     deplacements.get(deplacements.indexOf(emplacementsDestDep.get(deco)));
                  // si ce n'était pas un des emplacements départ
                  if(!decoOtherSide.getEmplacementDepart()){
                     // on l'enlève de la liste
                     deplacements.remove(decoOtherSide);

                     if(terminale.equals(decoOtherSide.getEmplacement().getTerminale())){
                        final Div upImg = getImagesEmplacements().get(decoOtherSide.getPosition() - 1);
                        upImg.setSclass("imageEmplacement");
                     }
                  }else{
                     // sinon, on vide sa destination
                     // MAJ de l'emplacement de départ
                     decoOtherSide.setAdrlDestination("");
                     decoOtherSide.setEmplDestination(null);
                     deplacementsRestants.add(0, decoOtherSide);
                     // s'il se trouve dans la boite actuelle
                     // on change le style de l'image en bleu
                     if(terminale.equals(decoOtherSide.getEmplacement().getTerminale())){
                        final Div upImg = getImagesEmplacements().get(decoOtherSide.getPosition() - 1);
                        upImg.setSclass("imageSelectedEmplacement");
                     }
                  }
                  emplacementsDestDep.remove(decoOtherSide);
               }else{
                  final EmplacementDecorator decoOtherSide = emplacementsDestDep.get(deco);
                  if(terminale.equals(decoOtherSide.getEmplacement().getTerminale())){
                     final Div upImg = getImagesEmplacements().get(decoOtherSide.getPosition() - 1);
                     upImg.setSclass("imageEmplacement");
                  }
                  emplacementsDestDep.remove(decoOtherSide);
               }
               // si la destination était vide
               if(!deplacements.contains(deco)){
                  img.setSclass("imageEmplacement");

               }else{
                  // sinon on récupère l'emplacement
                  final EmplacementDecorator decoDest = deplacements.get(deplacements.indexOf(deco));
                  // si ce n'était pas un des emplacements
                  // de départ
                  if(!decoDest.getEmplacementDepart()){
                     // on l'enlève de la liste
                     img.setSclass("imageEmplacement");
                     deplacements.remove(deco);
                  }else{
                     // sinon, on vide sa destination
                     img.setSclass("imageSelectedEmplacement");
                     decoDest.setAdrlDestination("");
                     decoDest.setEmplDestination(null);
                     deplacementsRestants.add(0, decoDest);
                  }
               }
               // l'emplacement courant est le 1er de la liste
               selectedEmplacement = deplacementsRestants.get(0);
               // on met a jour l'hashtable
               emplacementsDestDep.remove(deco);
               // on rafraichit les listes
               getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
               final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
               gridHistorique.setModel(list);
            }
         }
      }catch(final TKException e){
         throw new WrongValueException(img, e.getMessage());
      }
   }

   /**
    * TK-251 est-ce que le conteneur de destination de l'échantillon/dérivé
    * est accessible par la banque?
    * @param decoarteur emplacement source
    * @param decoarteur emplacement destination
    * @since 2.2.1
    */
   private void checkEmplacementDeplacementConteneurCoherence(final EmplacementDecorator source, final EmplacementDecorator dest){

      if(source != null && dest != null && source.getEmplacement() != null && dest.getEmplacement() != null){

         final Conteneur destCont = ManagerLocator.getEmplacementManager().getConteneurManager(dest.getEmplacement());
         //Set<Banque> banks = ManagerLocator.getConteneurManager().getBanquesManager(destCont);
         final List<Banque> banks = ManagerLocator.getBanqueManager().findByConteneurManager(destCont);

         if(!banks.contains(source.getTkStockObj().getBanque())){
            throw new TKException(ObjectTypesFormatters.getLabel("stockage.deplacement.conteneur.destination.banks",
               new String[] {destCont.getCode(), source.getTkStockObj().getBanque().getNom()}));
         }

      }
   }

   /**
    * Sélectionne une image représentant un emplacement de la terminale
    * en mode "stockage".
    * @param img Div à sélectionner.
    */
   public void selectImageInStockageMode(final Div img){
      final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");
      // si l'emplacement n'est pas l'emplacement courant
      if(!deco.equals(selectedEmplacement) && !emplacementReserves.contains(deco)){
         // l'image ne représente pas un emplacement de destination
         if(!emplacementsDestDep.containsKey(deco)){
            if(selectedEmplacement != null){
               // on vérifie que la terminale peut contenir
               // ce type d'objet
               if(terminale.getEntite() == null || (terminale.getEntite() != null
                  && terminale.getEntite().equals(selectedEmplacement.getEmplacement().getEntite()))){
                  // la destination doit être vide
                  if(deco.getVide()){
                     img.setSclass("imageMovedEmplacement");
                     // on met à jour l'emplacement courant => celui
                     // pour qui on vient de choisir la destination
                     final EmplacementDecorator upDeco = deplacements.get(deplacements.indexOf(selectedEmplacement));
                     upDeco.setAdrlDestination(deco.getAdrl());
                     upDeco.setEmplDestination(deco);
                     // on ajoute la destination à la hashtable
                     emplacementsDestDep.put(deco, upDeco);
                     final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
                     gridHistorique.setModel(list);
                     // on enlève l'emplacement courant de la liste des
                     // emplacements à déplacer
                     deplacementsRestants.remove(selectedEmplacement);
                     if(deplacementsRestants.size() > 0){
                        selectedEmplacement = deplacementsRestants.get(0);
                     }else{
                        selectedEmplacement = null;
                     }
                     getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
                  }
               }
            }
         }else{
            if(deplacements.contains(emplacementsDestDep.get(deco))){
               // on récupère l'emplacement de départ
               final EmplacementDecorator decoOtherSide = deplacements.get(deplacements.indexOf(emplacementsDestDep.get(deco)));
               // on vide sa destination
               // MAJ de l'emplacement de départ
               decoOtherSide.setAdrlDestination("");
               decoOtherSide.setEmplDestination(null);
               deplacementsRestants.add(0, decoOtherSide);

               img.setSclass("imageEmplacement");
               emplacementsDestDep.remove(deco);
            }

            // l'emplacement courant est le 1er de la liste
            selectedEmplacement = deplacementsRestants.get(0);
            // on met a jour l'hashtable
            emplacementsDestDep.remove(deco);
            // on rafraichit les listes
            getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
            final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
            gridHistorique.setModel(list);
         }
      }
   }

   /**
    * Sélectionne une image représentant un emplacement de la terminale
    * en mode "destockage".
    * @param img Div à sélectionner.
    */
   public void selectImageInDestockageMode(final Div img){
      final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");

      if(!deco.getVide()){
         // si l'emplacement a déjà été sélectionné, on
         // le déselctionne
         if(deplacements.contains(deco)){
            img.setSclass("imageEmplacement");
            deco.setEmplacementDepart(false);
            deplacements.remove(deco);
         }else{
            img.setSclass("imageDestockEmplacement");
            deco.setEmplacementDepart(false);
            deplacements.add(deco);
         }
      }
      final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
      gridHistorique.setModel(list);
   }

   public void onClick$deleteDeplacement(final Event event){
      // on récupère le déplacement que l'utilisateur veut
      // suppimer
      final EmplacementDecorator decoDep =
         (EmplacementDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(selectionMode){
         deleteDeplacementInSelectionMode(decoDep);
      }else if(deplacementMode){
         deleteDeplacementInDeplacementMode(decoDep);
      }else if(stockerMode){
         deleteDeplacementInStockageMode(decoDep);
      }else if(destockageMode){
         deleteDeplacementInDestockageMode(decoDep);
      }
   }

   /**
    * Métode qui supprime un déplacement en mode de sélection.
    * @param decoDep EmplacementDecorator à enlever des déplacements.
    */
   public void deleteDeplacementInSelectionMode(final EmplacementDecorator decoDep){
      if(decoDep != null){
         decoDep.setEmplacementDepart(false);
         deplacements.remove(decoDep);

         final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
         gridHistorique.setModel(list);

         if(decoDep.getEmplacement().getTerminale().equals(this.terminale)){
            final Div img = getImagesEmplacements().get(decoDep.getPosition() - 1);
            img.setSclass("imageEmplacement");
         }
      }
   }

   /**
    * Métode qui supprime un déplacement en mode de déplacement.
    * @param decoDep EmplacementDecorator à enlever des déplacements.
    */
   public void deleteDeplacementInDeplacementMode(final EmplacementDecorator decoDep){
      if(decoDep != null){
         if(decoDep.getEmplDestination() != null){
            if(!decoDep.getEmplacementDepart()){
               deplacements.remove(decoDep);

               if(decoDep.getEmplacement().getTerminale().equals(this.terminale)){
                  final Div img = getImagesEmplacements().get(decoDep.getPosition() - 1);
                  img.setSclass("imageEmplacement");
               }
            }else{
               decoDep.setAdrlDestination("");
               decoDep.setEmplDestination(null);
               deplacementsRestants.add(0, decoDep);

               if(decoDep.getEmplacement().getTerminale().equals(this.terminale)){
                  final Div img = getImagesEmplacements().get(decoDep.getPosition() - 1);
                  img.setSclass("imageSelectedEmplacement");
               }
            }

            if(deplacements.contains(emplacementsDestDep.get(decoDep))){
               final EmplacementDecorator decoDest = deplacements.get(deplacements.indexOf(emplacementsDestDep.get(decoDep)));

               if(!decoDest.getEmplacementDepart()){
                  deplacements.remove(decoDest);

                  if(decoDest.getEmplacement().getTerminale().equals(this.terminale)){
                     final Div img = getImagesEmplacements().get(decoDest.getPosition() - 1);
                     img.setSclass("imageEmplacement");
                  }
               }else{
                  decoDest.setAdrlDestination("");
                  decoDest.setEmplDestination(null);
                  deplacementsRestants.add(0, decoDest);

                  if(decoDest.getEmplacement().getTerminale().equals(this.terminale)){
                     final Div img = getImagesEmplacements().get(decoDest.getPosition() - 1);
                     img.setSclass("imageSelectedEmplacement");
                  }
               }
               emplacementsDestDep.remove(decoDest);
            }else{
               final EmplacementDecorator decoDest = emplacementsDestDep.get(decoDep);
               if(decoDest != null){
                  if(decoDest.getEmplacement().getTerminale().equals(this.terminale)){
                     final Div img = getImagesEmplacements().get(decoDest.getPosition() - 1);
                     img.setSclass("imageEmplacement");
                  }
                  emplacementsDestDep.remove(decoDest);
               }
            }

            // l'emplacement courant est le 1er de la liste
            selectedEmplacement = deplacementsRestants.get(0);
            // on met a jour l'hashtable
            emplacementsDestDep.remove(decoDep);
            // on rafraichit les listes
            getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
            final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
            gridHistorique.setModel(list);
         }
      }
   }

   /**
    * Métode qui supprime un déplacement en mode de stockage.
    * @param decoDep EmplacementDecorator à enlever des déplacements.
    */
   public void deleteDeplacementInStockageMode(final EmplacementDecorator decoDep){
      if(decoDep != null){
         if(decoDep.getEmplDestination() != null){
            final EmplacementDecorator decoDest = decoDep.getEmplDestination();

            if(decoDest.getEmplacement().getTerminale().equals(this.terminale)){
               final Div img = getImagesEmplacements().get(decoDest.getPosition() - 1);
               img.setSclass("imageEmplacement");
            }
            emplacementsDestDep.remove(decoDest);

            decoDep.setAdrlDestination("");
            decoDep.setEmplDestination(null);
            deplacementsRestants.add(0, decoDep);

            // l'emplacement courant est le 1er de la liste
            selectedEmplacement = deplacementsRestants.get(0);
            // on met a jour l'hashtable
            emplacementsDestDep.remove(decoDep);
            // on rafraichit les listes
            getBinder().loadAttribute(self.getFellow("elementsBox"), "model");
            final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
            gridHistorique.setModel(list);
         }
      }
   }

   /**
    * Métode qui supprime un déplacement en mode de déstockage.
    * @param decoDep EmplacementDecorator à enlever des déplacements.
    */
   public void deleteDeplacementInDestockageMode(final EmplacementDecorator decoDep){
      if(decoDep != null){
         deplacements.remove(decoDep);
         final ListModel<EmplacementDecorator> list = new ListModelList<>(deplacements);
         gridHistorique.setModel(list);

         if(decoDep.getEmplacement().getTerminale().equals(this.terminale)){
            final Div img = getImagesEmplacements().get(decoDep.getPosition() - 1);
            img.setSclass("imageEmplacement");
         }
      }
   }

   /**
    * Renvoie au controller general de l'onglet l'instruction que
    * l'édition est terminée.
    */
   private void postDetachDeplacementEvent(){
      Events.postEvent("onDeplacementDone", self.getParent().getParent().getParent().getFellow("winStockages"), null);
   }

   /**
    * Méthode qui va créer la liste des boites de départ des déplacements.
    * @return
    */
   private List<BoiteImpression> createBoitesDepart(){
      final List<BoiteImpression> listeBoitesDepart = new ArrayList<>();

      // pour chaque déplacement réalisé
      for(int i = 0; i < deplacements.size(); i++){
         final EmplacementDecorator deco = deplacements.get(i);
         // si l'emplacement a une destination
         if(deco.getEmplDestination() != null){

            // on récupère son emplacement et la boite
            final Emplacement emp = deco.getEmplacement();
            final Terminale term = emp.getTerminale();

            // Boite de départ
            final BoiteImpression newBiDep = new BoiteImpression();
            newBiDep.setBoite(term);

            // si c'est la 1ère fois que l'on rencontre cette boite
            if(!listeBoitesDepart.contains(newBiDep)){
               // on remplit tous titres et légendes
               newBiDep.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
               newBiDep.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
               newBiDep.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
               newBiDep.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.destockage.all"));
               newBiDep.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
               newBiDep.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
               newBiDep.setLegendeSelectionne(Labels.getLabel("impression.boite.legende" + ".selectionne.destockage.all"));

               // création des intructions pour récupérer la boite
               // Récup des parents de la boite
               final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(term);
               final List<String> instructions = new ArrayList<>();
               // pour chaque parent
               for(int j = 0; j < parents.size(); j++){
                  if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
                     final Conteneur c = (Conteneur) parents.get(j);
                     instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.conteneur",
                        new String[] {c.getCode()}));
                  }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
                     final Enceinte e = (Enceinte) parents.get(j);
                     instructions.add(
                        ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.enceinte", new String[] {e.getNom()}));
                  }
               }
               // ajout des instructions à la boite
               instructions
                  .add(ObjectTypesFormatters.getLabel("impression.boite.instruction.terminale", new String[] {term.getNom()}));
               instructions.add(Labels.getLabel("impression.boite.instruction" + ".destockage.all"));
               newBiDep.setInstructions(instructions);

               // ajout de l'élément à la liste des éléments
               // a extraire
               final List<String> elements = new ArrayList<>();
               // si l'élément a deplacer est un echantillon
               elements.add(ObjectTypesFormatters.getLabel("impression.boite.numero.objet", new String[] {"1", deco.getCode()}));
               newBiDep.setElements(elements);

               // ajout de la position du dérivé
               final List<Integer> positions = new ArrayList<>();
               positions.add(emp.getPosition());
               newBiDep.setPositions(positions);

               // ajout de la boite à la liste
               listeBoitesDepart.add(newBiDep);

            }else{
               // sinon on récupère la boite dans la liste
               final BoiteImpression bi = listeBoitesDepart.get(listeBoitesDepart.indexOf(newBiDep));

               // ajout du dérivé et de sa position aux
               // éléments à extraire
               final int pos = bi.getPositions().size() + 1;
               bi.getPositions().add(emp.getPosition());
               // si l'élément a deplacer est un echantillon
               bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.objet",
                  new String[] {Integer.toString(pos), deco.getCode()}));
            }
         }
      }

      return listeBoitesDepart;
   }

   /**
    * Méthode qui va créer la liste des boites d'arrivée des déplacements.
    * @return
    */
   private List<BoiteImpression> createBoitesArrivee(){

      final List<BoiteImpression> listeBoitesArrivee = new ArrayList<>();

      // pour chaque déplacement réalisé
      for(int i = 0; i < deplacements.size(); i++){
         final EmplacementDecorator deco = deplacements.get(i);
         // si l'emplacement a une destination
         if(deco.getEmplDestination() != null){

            // on récupère son emplacement et la boite
            final Emplacement emp = deco.getEmplacement();
            final Terminale term = emp.getTerminale();

            // Boite d'arrivée
            final BoiteImpression newBiArr = new BoiteImpression();
            newBiArr.setBoite(term);

            // si c'est la 1ère fois que l'on rencontre cette boite
            if(!listeBoitesArrivee.contains(newBiArr)){
               // on remplit tous titres et légendes
               newBiArr.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
               newBiArr.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
               newBiArr.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
               newBiArr.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.stockage.all"));
               newBiArr.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
               newBiArr.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
               newBiArr.setLegendeSelectionne(Labels.getLabel("impression.boite.legende" + ".selectionne.stockage.all"));

               // création des intructions pour récupérer la boite
               // Récup des parents de la boite
               final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(term);
               final List<String> instructions = new ArrayList<>();
               // pour chaque parent
               for(int j = 0; j < parents.size(); j++){
                  if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
                     final Conteneur c = (Conteneur) parents.get(j);
                     instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.conteneur",
                        new String[] {c.getCode()}));
                  }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
                     final Enceinte e = (Enceinte) parents.get(j);
                     instructions.add(
                        ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.enceinte", new String[] {e.getNom()}));
                  }
               }
               // ajout des instructions à la boite
               instructions
                  .add(ObjectTypesFormatters.getLabel("impression.boite.instruction.terminale", new String[] {term.getNom()}));
               instructions.add(Labels.getLabel("impression.boite.instruction" + ".stockage.all"));
               newBiArr.setInstructions(instructions);

               // ajout de l'élément à la liste des éléments
               // a extraire
               final List<String> elements = new ArrayList<>();
               // si l'élément a deplacer est un echantillon
               elements.add(ObjectTypesFormatters.getLabel("impression.boite.numero.objet", new String[] {"1", deco.getCode()}));
               newBiArr.setElements(elements);

               // ajout de la position du dérivé
               final List<Integer> positions = new ArrayList<>();
               positions.add(emp.getPosition());
               newBiArr.setPositions(positions);

               // ajout de la boite à la liste
               listeBoitesArrivee.add(newBiArr);

            }else{
               // sinon on récupère la boite dans la liste
               final BoiteImpression bi = listeBoitesArrivee.get(listeBoitesArrivee.indexOf(newBiArr));

               // ajout du dérivé et de sa position aux
               // éléments à extraire
               final int pos = bi.getPositions().size() + 1;
               bi.getPositions().add(emp.getPosition());
               // si l'élément a deplacer est un echantillon
               bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.objet",
                  new String[] {Integer.toString(pos), deco.getCode()}));
            }
         }
      }

      if(listeBoitesArrivee.size() > 0){
         listeBoitesArrivee.get(0).setSeparateur(true);
      }

      return listeBoitesArrivee;
   }

   /**
    * Méthode qui permet d'afficher la page d'impression de la cession.
    */
   public void createFileHtmlToPrintLaterStockage(){
      // s'il y a des éléments a stocker
      if(deplacements.size() > 0){

         // création du document, de la page et de son titre
         final Document doc = ManagerLocator.getXmlUtils().createJDomDocumentBoites();
         final Element root = doc.getRootElement();

         String titleKey;

         if("echantillon".equalsIgnoreCase(typeEntite)){
            titleKey = "impression.boite.title.stockage.echantillons";
         }else{
            titleKey = "impression.boite.title.stockage.prodDerives";
         }

         final Element page = ManagerLocator.getXmlUtils().addPageBoite(root, Labels.getLabel(titleKey));

         final List<BoiteImpression> listeBoites = new ArrayList<>();

         // on parcourt les déplacements
         for(int k = 0; k < deplacements.size(); k++){
            final EmplacementDecorator deco = deplacements.get(k);

            if(deco.getEmplDestination() != null){
               final Emplacement empl = deco.getEmplDestination().getEmplacement();

               Echantillon echan = null;
               ProdDerive derive = null;
               if(typeEntite.equals("Echantillon")){
                  echan = echantillons.get(k);
               }else if(typeEntite.equals("ProdDerive")){
                  derive = derives.get(k);
               }

               final Terminale term = empl.getTerminale();

               final BoiteImpression newBi = new BoiteImpression();
               newBi.setBoite(term);

               // si c'est la 1ère fois que l'on rencontre cette boite
               if(!listeBoites.contains(newBi)){
                  // on remplit tous titres et légendes
                  newBi.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
                  newBi.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
                  newBi.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
                  if(typeEntite.equals("Echantillon")){
                     newBi.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.stockage.echantillons"));
                  }else{
                     newBi.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.stockage.prodDerives"));
                  }
                  newBi.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
                  newBi.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
                  if(typeEntite.equals("Echantillon")){
                     newBi.setLegendeSelectionne(
                        Labels.getLabel("impression.boite.legende.selectionne" + ".stockage.echantillons"));
                  }else{
                     newBi
                        .setLegendeSelectionne(Labels.getLabel("impression.boite.legende.selectionne" + ".stockage.prodDerives"));
                  }

                  // création des intructions pour récupérer la boite
                  // Récup des parents de la boite
                  final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(term);
                  final List<String> instructions = new ArrayList<>();
                  // pour chaque parent
                  for(int j = 0; j < parents.size(); j++){
                     if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
                        final Conteneur c = (Conteneur) parents.get(j);
                        instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.conteneur",
                           new String[] {c.getCode()}));
                     }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
                        final Enceinte e = (Enceinte) parents.get(j);
                        instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.enceinte",
                           new String[] {e.getNom()}));
                     }
                  }
                  // ajout des instructions à la boite
                  instructions
                     .add(ObjectTypesFormatters.getLabel("impression.boite.instruction.terminale", new String[] {term.getNom()}));
                  if(typeEntite.equals("Echantillon")){
                     instructions.add(Labels.getLabel("impression.boite.instruction" + ".stockage.echantillons"));
                  }else{
                     instructions.add(Labels.getLabel("impression.boite.instruction" + ".stockage.prodDerives"));
                  }
                  newBi.setInstructions(instructions);

                  // ajout de l'élément à la liste des éléments
                  // a extraire
                  final List<String> elements = new ArrayList<>();
                  if(null != echan && typeEntite.equals("Echantillon")){
                     elements.add(ObjectTypesFormatters.getLabel("impression.boite.numero.echantillon",
                        new String[] {"1", echan.getCode()}));
                  }else if(null != derive){
                     elements.add(ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive",
                        new String[] {"1", derive.getCode()}));
                  }
                  newBi.setElements(elements);

                  // ajout de la position du dérivé
                  final List<Integer> positions = new ArrayList<>();
                  positions.add(empl.getPosition());
                  newBi.setPositions(positions);

                  // ajout de la boite à la liste
                  listeBoites.add(newBi);

               }else{
                  // sinon on récupère la boite dans la liste
                  final BoiteImpression bi = listeBoites.get(listeBoites.indexOf(newBi));

                  // ajout du dérivé et de sa position aux
                  // éléments à extraire
                  final int pos = bi.getPositions().size() + 1;
                  bi.getPositions().add(empl.getPosition());
                  if(null != echan && typeEntite.equals("Echantillon")){
                     bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.echantillon",
                        new String[] {Integer.toString(pos), echan.getCode()}));
                  }else if(null != derive){
                     bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive",
                        new String[] {Integer.toString(pos), derive.getCode()}));
                  }
               }
            }
         }

         final StringBuffer adrImages = new StringBuffer();
         adrImages.append(((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getContextPath());
         adrImages.append("/images/icones/emplacements/");
         for(int i = 0; i < listeBoites.size(); i++){
            ManagerLocator.getXmlUtils().addBoite(page, listeBoites.get(i), adrImages.toString());
         }

         // Transformation du document en fichier
         byte[] dl = null;
         try{
            dl = ManagerLocator.getXmlUtils().creerBoiteHtml(doc);
         }catch(final Exception e){

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

   /*************************************************/
   /**               ACCESSEURS                    **/
   /*************************************************/

   public String getMode(){
      return mode;
   }

   public void setMode(final String m){
      this.mode = m;
   }

   @Override
   public String getAdrlTerminale(){
      return adrlTerminale;
   }

   @Override
   public void setAdrlTerminale(final String adrl){
      this.adrlTerminale = adrl;
   }

   //	public List<EmplacementDecorator> getEmplacementDecos() {
   //		return emplacementDecos;
   //	}
   //
   //	public void setEmplacementDecos(List<EmplacementDecorator> emplacementD) {
   //		this.emplacementDecos = emplacementD;
   //	}

   public String getBadTerminaleError(){
      return badTerminaleError;
   }

   public void setBadTerminaleError(final String badTerminale){
      this.badTerminaleError = badTerminale;
   }

   public List<EmplacementDecorator> getDeplacements(){
      return deplacements;
   }

   public void setDeplacements(final List<EmplacementDecorator> depls){
      this.deplacements = depls;
   }

   //	public List<Image> getImagesEmplacements() {
   //		return imagesEmplacements;
   //	}
   //
   //	public void setImagesEmplacements(List<Image> imagesEmpls) {
   //		this.imagesEmplacements = imagesEmpls;
   //	}

   public boolean isSelectionMode(){
      return selectionMode;
   }

   public void setSelectionMode(final boolean selectionM){
      this.selectionMode = selectionM;
   }

   public EmplacementDecorator getSelectedEmplacement(){
      return selectedEmplacement;
   }

   public void setSelectedEmplacement(final EmplacementDecorator selectedEmpl){
      this.selectedEmplacement = selectedEmpl;
   }

   public List<EmplacementDecorator> getDeplacementsRestants(){
      return deplacementsRestants;
   }

   public void setDeplacementsRestants(final List<EmplacementDecorator> deplacementsRest){
      this.deplacementsRestants = deplacementsRest;
   }

   public Hashtable<EmplacementDecorator, EmplacementDecorator> getEmplacementsDestDep(){
      return emplacementsDestDep;
   }

   public void setEmplacementsDestDep(final Hashtable<EmplacementDecorator, EmplacementDecorator> emplacementsDD){
      this.emplacementsDestDep = emplacementsDD;
   }

   public List<EmplacementDecorator> getEmplacementDepart(){
      return emplacementDepart;
   }

   public void setEmplacementDepart(final List<EmplacementDecorator> emplacementDep){
      this.emplacementDepart = emplacementDep;
   }

   public List<String> getLignes(){
      return lignes;
   }

   public void setLignes(final List<String> ligs){
      this.lignes = ligs;
   }

   public List<String> getColonnes(){
      return colonnes;
   }

   public void setColonnes(final List<String> cols){
      this.colonnes = cols;
   }

   public Integer getNumeroLigne(){
      return numeroLigne;
   }

   public void setNumeroLigne(final Integer numeroL){
      this.numeroLigne = numeroL;
   }

   public Integer getNumeroColonne(){
      return numeroColonne;
   }

   public void setNumeroColonne(final Integer numeroC){
      this.numeroColonne = numeroC;
   }

   @Override
   public String getEntiteReservee(){
      return entiteReservee;
   }

   @Override
   public void setEntiteReservee(final String entiteRes){
      this.entiteReservee = entiteRes;
   }

   public boolean isDeplacementMode(){
      return deplacementMode;
   }

   public void setDeplacementMode(final boolean depMode){
      this.deplacementMode = depMode;
   }

   public boolean isStockerMode(){
      return stockerMode;
   }

   public void setStockerMode(final boolean stockMode){
      this.stockerMode = stockMode;
   }

   public String getReturnMethode(){
      return returnMethode;
   }

   public void setReturnMethode(final String returnM){
      this.returnMethode = returnM;
   }

   public String getTypeEntite(){
      return typeEntite;
   }

   public void setTypeEntite(final String typeE){
      this.typeEntite = typeE;
   }

   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> e){
      this.echantillons = e;
   }

   public List<ProdDerive> getDerives(){
      return derives;
   }

   public void setDerives(final List<ProdDerive> d){
      this.derives = d;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   public List<EmplacementDecorator> getEmplacementReserves(){
      return emplacementReserves;
   }

   public void setEmplacementReserves(final List<EmplacementDecorator> emplacementR){
      this.emplacementReserves = emplacementR;
   }

   //	public static String getImageSrc() {
   //		return imageSrc;
   //	}
   //
   //	public static void setImageSrc(String src) {
   //		FicheDeplacerEmplacements.imageSrc = src;
   //	}

   public boolean isDestockageMode(){
      return destockageMode;
   }

   public void setDestockageMode(final boolean dMode){
      this.destockageMode = dMode;
   }

   //	@Override
   //	public TKdataObject getObject() {
   //		return null;
   //	}

   @Override
   public void setNewObject(){}

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   //	public Map<TKStockableObject, Emplacement> getEmplForRetours() {
   //		return emplForRetours;
   //	}
   //
   //	public void setEmplForRetours(HashMap<TKStockableObject, Emplacement> e) {
   //		this.emplForRetours = e;
   //	}

   // @since 2.2.3-genno TK-291 fix
   @Override
   public List<TKStockableObject> prepareObjectsAndEmplacements(){
      getEmplForRetours().clear();
      emplacementsFinaux.clear();
      for(int i = 0; i < deplacements.size(); i++){
         final EmplacementDecorator deco = deplacements.get(i);
         // si l'emplacement a une destination
         if(deco.getEmplDestination() != null){
            final Emplacement empl = deco.getEmplacement();
            if(!empl.getVide()){
               if(empl.getEntite().getNom().equals("Echantillon")){
                  // getEmplForRetours().put(ManagerLocator.getEchantillonManager().findByIdManager(empl.getObjetId()),
                  //		empl.clone());
                  getEmplForRetours()
                     .add(new OldEmplTrace(ManagerLocator.getEchantillonManager().findByIdManager(empl.getObjetId()),
                        ManagerLocator.getEmplacementManager().getAdrlManager(empl, false),
                        ManagerLocator.getEmplacementManager().getConteneurManager(empl), empl.clone()));
               }else if(empl.getEntite().getNom().equals("ProdDerive")){
                  // getEmplForRetours().put(ManagerLocator.getProdDeriveManager().findByIdManager(empl.getObjetId()), empl.clone());
                  getEmplForRetours()
                     .add(new OldEmplTrace(ManagerLocator.getProdDeriveManager().findByIdManager(empl.getObjetId()),
                        ManagerLocator.getEmplacementManager().getAdrlManager(empl, false),
                        ManagerLocator.getEmplacementManager().getConteneurManager(empl), empl.clone()));
               }
            }
            // on maj son adresse
            empl.setTerminale(deco.getEmplDestination().getTerminale());
            empl.setPosition(deco.getEmplDestination().getPosition());
            emplacementsFinaux.add(empl);

            if(deco.getEmplDestination().getVide() && deco.getEmplDestination().getEmplacement().getEmplacementId() != null){
               final Emplacement vide = deco.getEmplDestination().getEmplacement();
               vide.setTerminale(deco.getTerminale());
               vide.setPosition(deco.getPosition());
               emplacementsFinaux.add(vide);
            }
         }
      }
      return getEmplForRetours().stream().map(o -> o.getTkObj()).collect(Collectors.toList());
   }

   /**
    * Sélectionne et affiche dans l'arborescence la terminale
    * à partir du scan, en mode déplacementEmplacement, en sélectionnant
    * les emplacements à stocker.
    * @param TKScanDTO dto
    * @since 2.1
    * @version 2.1
    */
   public void selectImagesFromScan(final TKScanTerminaleDTO dto){
      if(dto.getTerminale() != null){
         Events.echoEvent("onLaterScanTerminale", self, dto);
         Clients.showBusy(Labels.getLabel("general.display.wait"));
      }
   }

   public void onLaterScanTerminale(final Event e){
      final TKScanTerminaleDTO scanDTO = (TKScanTerminaleDTO) e.getData();
      // update Terminale selection
      getObjectTabController().getListeStockages().openTerminaleForDeplacementEmplacement(scanDTO.getTerminale());

      // find tkObjects from tubes
      EmplacementDecorator deco;
      Div img;
      int fillsFound = 0;
      for(final ScanTube tube : scanDTO.getEmplacementsToFill().keySet()){
         deco = null;
         img = null;
         // if tube code is found through tkObjects
         for(final EmplacementDecorator toBeFilled : getDeplacementsRestants()){
            if(toBeFilled.getCode().equals(tube.getCode())){
               deco = toBeFilled;
               fillsFound++;
               break;
            }
         }

         // deco can be null if terminale emplacement can be filled, but
         // tube code doesn't match selected echantillons to be stored,
         // or selected echantillons are to be stored in another terminale
         if(deco != null){

            // indexOf should not throw errors as emplToFills must
            // belong to emplacements defined for terminale
            img = getImagesEmplacements()
               .get(getEmplacementDecos().indexOf(new EmplacementDecorator(scanDTO.getEmplacementsToFill().get(tube))));

            setSelectedEmplacement(deco);
            handleImage(img);
            if(deplacementMode){
               if(deplacementsRestants.size() == 0){
                  break;
               }
            }else if(stockerMode){
               if(deplacementsRestants.size() == 0){
                  break;
               }
            }
         }
      }

      // notification
      // warn terminale not found
      //		if (scanDTO.getTerminale() == null) {
      //			Clients.showNotification(ObjectTypesFormatters.getLabel("scan.objects.terminale.notfound.warning",
      //					new String[] {scanDTO.getScanTerminale().getName(), ObjectTypesFormatters
      //							.dateRenderer2(scanDTO.getScanTerminale().getDateScan()),
      //					(getObjectTabController().getListeStockages().getSelectedEnceinte() != null ?
      //							getObjectTabController().getListeStockages().getSelectedEnceinte().getNom() :
      //					getObjectTabController().getListeStockages().getSelectedConteneur() != null ?
      //							getObjectTabController().getListeStockages().getSelectedConteneur().getCode() : "")}),
      //					"warning", null, null, 4000, true);
      //
      //		} else {
      Clients.showNotification(
         ObjectTypesFormatters.getLabel("scan.objects.stock.info",
            new String[] {scanDTO.getScanTerminale().getName(),
               ObjectTypesFormatters.dateRenderer2(scanDTO.getScanTerminale().getDateScan()),
               String.valueOf(scanDTO.getScanTerminale().getScanTubes().size()), String.valueOf(fillsFound),
               String.valueOf(scanDTO.getEmplacementsToFill().size())}),
         fillsFound > 0 ? "info" : "warning", null, null, 4000, true);
      //		}

      Clients.clearBusy();
   }
}
