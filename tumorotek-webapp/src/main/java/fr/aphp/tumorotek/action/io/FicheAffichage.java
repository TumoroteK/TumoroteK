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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.EntiteDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.ResultatDecorator;
import fr.aphp.tumorotek.manager.xml.XmlUtils;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe gérant l'affichage des résultats d'une requête.
 * @author pierre
 *
 */
public class FicheAffichage extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(XmlUtils.class);

   private static final long serialVersionUID = 1L;

   /**
    *  Editable components : mode d'édition ou de création.
    */
   private Label intituleLabel;

   private Label intituleRequired;

   private Textbox intituleBox;

   private Label nbLignesLabel;

   private Label nbLignesRequired;

   private Intbox nbLignesBox;

   /**
    * Champs à afficher pour le résultat de la recherche (mode édition)
    */
   private Grid resultatsBox;

   /**
    * Champs à afficher pour le résultat de la recherche  (mode lecture)
    */
   private Grid resultatsBoxStatic;

   private Button addChamp;

   private Image removeImg;

   /** Objets principaux. */
   private Affichage affichage;

   private List<ResultatDecorator> resultats = new ArrayList<>();

   /**
    * Liste des champs à afficher pour le résultat de la recherche
    */
   private List<Resultat> resultatsToRemove = new ArrayList<>();

   private List<EntiteDecorator> entites = new ArrayList<>();

   public EventListener<Event> dropListener = new EventListener<Event>()
   {
      @Override
      public void onEvent(final Event e) throws Exception{
         final DropEvent event = (DropEvent) e;
         final Component dragged = event.getDragged();
         final Component target = event.getTarget();
         //On cherche le sens de déplacement
         Component previousSibling = dragged;
         while(previousSibling != null && previousSibling != target){
            previousSibling = previousSibling.getPreviousSibling();
         }
         /*Si on fait remonter les objets, on insère devant la cible puis on
         inverse les deux objets*/
         if(previousSibling == null){
            target.getParent().insertBefore(dragged, target);
            target.getParent().insertBefore(target, dragged);
         }else if(previousSibling.equals(target)){
            /*Sinon on fait remonter les objets, on insère devant la cible*/
            target.getParent().insertBefore(dragged, target);
         }

         final List<Resultat> res = new ArrayList<>();
         for(int i = 0; i < ((Listbox) target.getParent()).getItemCount(); i++){
            final Listitem temp = ((Listbox) target.getParent()).getItemAtIndex(i);
            final ResultatDecorator rDeco = ((ResultatDecorator) temp.getValue());
            final Resultat r = rDeco.getResultat();
            r.setPosition(i + 1);
            r.setOrdreTri(i + 1);
            res.add(r);
         }
         Collections.sort(res);
         resultats = ResultatDecorator.decorateListe(res);

         resultatsBox.setModel(new SimpleListModel<>(resultats));
         //addDropListeners();

      }
   };

   /**
    *  Gestion des contraintes.
    *  Variables conservant les valeurs saisies dans les champs.
    */

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      winPanel.setHeight(getMainWindow().getPanelHeight() - 110 + "px");

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {intituleLabel, nbLignesLabel, resultatsBoxStatic});
      setObjBoxsComponents(new Component[] {intituleBox, nbLignesBox, removeImg, addChamp, resultatsBox});
      setRequiredMarks(new Component[] {intituleRequired, nbLignesRequired});

      super.switchToStaticMode(true);

      drawActionsForAffichage();

      getBinder().loadAll();
   }

   @Override
   public TKdataObject getObject(){
      return this.affichage;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.affichage = (Affichage) obj;

      resultats.clear();
      if(affichage.getAffichageId() != null){
         resultats.addAll(ResultatDecorator.decorateListe(ManagerLocator.getResultatManager().findByAffichageManager(affichage)));
      }
      Collections.sort(resultats);
      resultatsBox.setModel(new SimpleListModel<>(resultats));

      super.setObject(obj);
   }

   @Override
   public void setNewObject(){
      setObject(new Affichage());
   }

   @Override
   public void cloneObject(){
      setClone(this.affichage.clone());
   }

   @Override
   public AffichageController getObjectTabController(){
      return (AffichageController) super.getObjectTabController();
   }

   /* Methode comportementales */
   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      //On initialise la liste d'entités
      if(this.entites.isEmpty()){
         this.entites.add(null);
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0)));
      }
      this.resultats.clear();
      resultatsBox.setModel(new SimpleListModel<>(resultats));

      //On ferme la liste des affichages
      setOpenListeAffichage(false);

      resultatsToRemove = new ArrayList<>();

      getBinder().loadComponent(self);

   }

   @Override
   public void switchToStaticMode(){

      final boolean isEmptyObj = this.affichage.equals(new Affichage());

      super.switchToStaticMode(isEmptyObj);

      //On ouvre la liste des affichages
      setOpenListeAffichage(true);

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){

      super.switchToEditMode();

      //On initialise la liste d'entités
      this.entites = new ArrayList<>();
      this.entites.add(null);
      this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0)));
      this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0)));
      this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0)));
      this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0)));
      this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0)));
      this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0)));

      //On rafraichit la liste pour faire apparaitre les croix delete
      Collections.sort(resultats);
      this.resultatsBox.setModel(new SimpleListModel<>(resultats));

      //addDropListeners();

      //On ferme la liste des affichages
      setOpenListeAffichage(false);

      resultatsToRemove = new ArrayList<>();

      getBinder().loadComponent(self);
   }

   @Override
   public void clearData(){
      clearDataResultat();
      clearConstraints();
      super.clearData();
   }

   protected void clearDataResultat(){}

   @Override
   public void createNewObject(){

      try{
         setEmptyToNulls();

         saveAffichage(SessionUtils.getLoggedUser(sessionScope));

      }catch(final Exception e){
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public void onClick$addNewC(){
      this.switchToCreateMode();
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   /**
    * Méthode qui vérifie que tous les noms donnés aux colonnes sont
    * différents.
    */
   public void checkNomsResultats(){
      boolean alreadyIn = false;

      for(int i = 0; i < resultats.size(); i++){
         for(int j = i + 1; j < resultats.size(); j++){
            if(resultats.get(i).getResultat().getNomColonne().equals(resultats.get(j).getResultat().getNomColonne())){
               alreadyIn = true;
            }
         }
      }

      if(alreadyIn){
         throw new RuntimeException(Labels.getLabel("affichage.doublon.nomColonne"));
      }
   }

   @Override
   public void onClick$createC(){
      if(resultats.size() <= 0){
         Messagebox.show(Labels.getLabel("affichage.validation.containsResultat"), Labels.getLabel("general.error"),
            Messagebox.OK, Messagebox.ERROR);
      }else{
         Clients.showBusy(Labels.getLabel("affichage.creation.encours"));
         Events.echoEvent("onLaterCreate", self, null);
      }
   }

   @Override
   public void onLaterCreate(){
      try{
         // vérification des noms de colonnes valides
         checkNomsResultats();
         createNewObject();
         cloneObject();
         disableToolBar(false);

         if(getListeAffichages() != null){
            // ajout de l'affichage à la liste
            getListeAffichages().addToObjectList(this.affichage);
         }
         clearData();
         // maj de la page d'exéction des requetes
         getObjectTabController().getTopController().resetExecutionController();
         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void onClick$deleteC(){

      if(affichage != null){
         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.affichage")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

            try{
               /**
                * On supprime l'objet en base de données
                */
               ManagerLocator.getAffichageManager().removeObjectManager(affichage);

               // On retire l'affichage de la liste
               getListeAffichages().removeObjectAndUpdateList(affichage);

               // On efface les données du formulaire
               clearData();
            }catch(final RuntimeException re){
               // ferme wait message
               Clients.clearBusy();
               Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
            }
         }
      }
   }

   @Override
   public void onClick$editC(){
      if(this.affichage != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){
      if(resultats.size() <= 0){
         Messagebox.show(Labels.getLabel("affichage.validation.containsResultat"), Labels.getLabel("general.error"),
            Messagebox.OK, Messagebox.ERROR);
      }else{
         Clients.showBusy(Labels.getLabel("affichage.creation.encours"));
         Events.echoEvent("onLaterUpdate", self, null);
      }
   }

   public void onLaterUpdate(){
      try{
         // vérification sur les noms de colonnes
         checkNomsResultats();
         updateObject();
         cloneObject();
         switchToStaticMode();
         getBinder().loadComponent(resultatsBox);

         if(getListeAffichages() != null){
            // ajout de l'utilisateur à la liste
            getListeAffichages().updateObjectGridList(this.affichage);
         }
         // maj de la fenetre d'exécution de la requête
         getObjectTabController().getTopController().resetExecutionController();
         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void updateObject(){

      try{
         setEmptyToNulls();

         updateAffichage(SessionUtils.getLoggedUser(sessionScope));
      }catch(final Exception e){
         log.error(e);
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(intituleBox);
   }

   @Override
   public void setEmptyToNulls(){
      if(this.affichage.getIntitule().equals("")){
         this.affichage.setIntitule(null);
      }
   }

   /**
    * Méthode qui enlève une colonne.
    * @param event Clic sur une image de délétion.
    */
   public void onClick$removeImg(final Event event){
      // on récupère le critère que l'utilisateur veut
      // suppimer
      final ResultatDecorator rDeco = (ResultatDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      if(rDeco.getResultat().getResultatId() != null){
         resultatsToRemove.add(rDeco.getResultat());
      }

      final Resultat r = rDeco.getResultat();
      final int ordreTri = r.getOrdreTri();
      // on met à jour la position de chaque colonne
      for(int i = 0; i < resultats.size(); i++){
         if(resultats.get(i).getResultat().getOrdreTri() > ordreTri){
            resultats.get(i).getResultat().setOrdreTri(resultats.get(i).getResultat().getOrdreTri() - 1);
            resultats.get(i).getResultat().setPosition(resultats.get(i).getResultat().getOrdreTri());
         }
      }
      resultats.remove(rDeco);

      resultatsBox.setModel(new SimpleListModel<>(resultats));
      getBinder().loadComponent(resultatsBox);
   }

   /**
    * Clic sur l'image pour monter une ligne.
    * @param event
    */
   public void onClick$upArrowDiv(final Event event){
      // on récupère le critère
      final ResultatDecorator rDeco = (ResultatDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      upObject(rDeco);

      resultatsBox.setModel(new SimpleListModel<>(resultats));
      getBinder().loadComponent(resultatsBox);
   }

   /**
    * Clic sur l'image pour descendre une ligne.
    * @param event
    */
   public void onClick$downArrowDiv(final Event event){
      // on récupère le critère
      final ResultatDecorator rDeco = (ResultatDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      final int tabIndex = resultats.indexOf(rDeco);
      if(tabIndex + 1 < resultats.size()){
         upObject(resultats.get(tabIndex + 1));
      }

      resultatsBox.setModel(new SimpleListModel<>(resultats));
      getBinder().loadComponent(resultatsBox);
   }

   /**
    * Effectue l'operation de mouvements des objets au sein de la liste.
    * @param objet a monter d'un cran
    */
   private void upObject(final ResultatDecorator obj){
      final int tabIndex = resultats.indexOf(obj);
      ResultatDecorator supObjectInList = null;
      if(tabIndex - 1 > -1){
         supObjectInList = resultats.get(tabIndex - 1);
         supObjectInList.getResultat().setPosition(supObjectInList.getResultat().getPosition() + 1);
         supObjectInList.getResultat().setOrdreTri(supObjectInList.getResultat().getOrdreTri() + 1);
         obj.getResultat().setPosition(obj.getResultat().getPosition() - 1);
         obj.getResultat().setOrdreTri(obj.getResultat().getOrdreTri() - 1);
         resultats.set(tabIndex, supObjectInList);
         resultats.set(tabIndex - 1, obj);
      }
   }

   /**
    * Sauvegarde de l'affichage
    * @param createur
    * @return
    */
   protected Affichage saveAffichage(final Utilisateur createur){
      ManagerLocator.getAffichageManager().createObjectManager(affichage, ResultatDecorator.extractListe(resultats), createur,
         SessionUtils.getSelectedBanques(sessionScope).get(0));
      return affichage;
   }

   /**
    * Update de l'affichage.
    * @param createur
    */
   protected void updateAffichage(final Utilisateur createur){
      ManagerLocator.getAffichageManager().updateObjectManager(affichage, ResultatDecorator.extractListe(resultats),
         resultatsToRemove);
   }

   /**
    * Métode appelée lors du clic sur le bouton addChamp : ouverture
    * de la fenêtre modale permettant la sélection des champs à
    * afficher.
    */
   public void onClick$addChamp(){
      final List<Champ> chps = new ArrayList<>();
      // on récupère les champs déjà sélectionnés pour qu'ils
      // n'apparaissent pas dans la fenêtre de sélection

      for(int i = 0; i < resultats.size(); i++){
         if(resultats.get(i).getResultat().getChamp().getChampParent() == null){
            chps.add(resultats.get(i).getResultat().getChamp());
         }else{
            chps.add(resultats.get(i).getResultat().getChamp().getChampParent());
         }
      }
      openChampsAffichageWindow(page, self, chps, SessionUtils.getSelectedBanques(sessionScope).get(0), true);
   }

   /**
    * Méthode appelée une fois que l'utilisateur a choisi les champs
    * à afficher.
    * @param e
    */
   public void onGetChamps(final Event e){
      if(e.getData() != null){

         // pour chaque champ
         for(final Object currChampObj : (List<?>) e.getData()){
            Champ currChamp = Champ.class.cast(currChampObj);
            Champ sousChampTmp = null;

            if(currChamp.getChampEntite() != null && currChamp.getChampEntite().getQueryChamp() != null){
               sousChampTmp = new Champ(currChamp.getChampEntite().getQueryChamp());
               sousChampTmp.setChampParent(currChamp);
            }

            // on extrait un nom par défaut
            String nom = "";
            if(currChamp.getChampEntite() != null || currChamp.getChampDelegue() != null){
               nom = ObjectTypesFormatters.getLabelForChamp(currChamp);
            }else if(currChamp.getChampAnnotation() != null){
               nom = currChamp.getChampAnnotation().getNom();
            }

            if(sousChampTmp != null){
               this.resultats.add(new ResultatDecorator(
                  new Resultat(nom, sousChampTmp, true, resultats.size() + 1, resultats.size() + 1, null, affichage)));
            }else{
               this.resultats.add(new ResultatDecorator(
                  new Resultat(nom, currChamp, true, resultats.size() + 1, resultats.size() + 1, null, affichage)));
            }
            // ajout à la liste
            this.resultatsBox.setModel(new SimpleListModel<>(resultats));

         }
      }
   }

   /**
    * Nouvelle fenêtre pour la FicheChampsAffichageModale
    * @param page page où ouvrir la fenêtre
    * @return
    */
   private Window createWindowForFicheChampsAffichageModale(Page page){
      // nouvelle fenêtre
      final Window win = new Window();
      win.setVisible(false);
      win.setId("champsAffichageWindow");
      win.setPage(page);
      win.setMaximizable(true);
      win.setSizable(true);
      win.setTitle(Labels.getLabel("champs.affichage.modale.title"));
      win.setBorder("normal");
      win.setWidth("500px");
      final int height = 510;
      win.setHeight(String.valueOf(height) + "px");
      win.setClosable(true);

      return win;
   }

   /**
    * Nouveau composant pour la FicheChampsAffichageModale
    * @param win fenêtre où inclure le composant
    * @param page page où inclure le composant ?
    * @return composant  HTML
    */
   private HtmlMacroComponent createComponentForChampsAffichageModale(Window win, Page page){
      final HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("champsAffichageModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("champsAffichageModaleComponent");
      ua.applyProperties();
      ua.afterCompose();
      ua.setVisible(false);

      return ua;
   }

   /**
    * Timer avant affichage de la FicheChampsAffichageModale ??
    * @param win fenêtre 
    * @param ua composant
    */
   private void setTimerForChampsAffichageModale(final Window win, final HtmlMacroComponent ua){
      final Timer timer = new Timer();
      timer.setDelay(500);
      timer.setRepeats(false);
      timer.addForward("onTimer", timer.getParent(), "onTimed");
      win.appendChild(timer);
      timer.start();

      win.addEventListener("onTimed", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            //progress.detach();
            ua.setVisible(true);
         }
      });

   }

   /**
    * Retourne la fiche... comment ?
    * @param ua composant où est la fiche
    * @return
    */
   private FicheChampsAffichageModale getFicheChampAffichageModale(HtmlMacroComponent ua){
      return ((FicheChampsAffichageModale) ua.getFellow("fwinChampsAffichageModale")
         .getAttributeOrFellow("fwinChampsAffichageModale$composer", true));
   }

   /**
    * PopUp window appelée permettant la sélection des champs à afficher.
    * @param page dans laquelle inclure la modale
    * @param parent composent parent auquel rattaché la modale
    * @param oldSelectedChamps Liste des champs déjà sélectionnés.
    * @param selectionMultiple activer/desactiver la selection multiple
    * @bank Banque sur laquelle effectuer la recherche
    */
   public void openChampsAffichageWindow(final Page page, final Component parent, final List<Champ> oldSelectedChamps,
      final Banque banque, final Boolean selectionMultiple){
      if(!isBlockModal()){

         setBlockModal(true);

         Window win = createWindowForFicheChampsAffichageModale(page);

         final HtmlMacroComponent ua = createComponentForChampsAffichageModale(win, page);

         setTimerForChampsAffichageModale(win, ua);

         FicheChampsAffichageModale ficheChampsAffichageModale = getFicheChampAffichageModale(ua);

         ficheChampsAffichageModale.init(oldSelectedChamps, parent, banque, selectionMultiple);

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   /**
    * PopUp window appelée permettant la sélection des champs à afficher, selon les datatypes voulus
    * @param page dans laquelle inclure la modale
    * @param parent composent parent auquel rattaché la modale
    * @param oldSelectedChamps Liste des champs déjà sélectionnés.
    * @param banque banque ou rechercher les champs
    * @param selectionMultiple activer/desactiver la selection multiple
    * @param dataTypeList liste des datatypes souhaités
    * @param excludeIds exclure les champs numérique de type Id
    */
   public void openChampsAffichageWindow(final Page page, final Component parent, final List<Champ> oldSelectedChamps,
      final Banque banque, final Boolean selectionMultiple, List<DataType> dataTypeList, Boolean excludeIds){
      if(!isBlockModal()){

         setBlockModal(true);

         Window win = createWindowForFicheChampsAffichageModale(page);

         final HtmlMacroComponent ua = createComponentForChampsAffichageModale(win, page);

         setTimerForChampsAffichageModale(win, ua);

         FicheChampsAffichageModale ficheChampsAffichageModale = getFicheChampAffichageModale(ua);

         ficheChampsAffichageModale.init(oldSelectedChamps, parent, banque, selectionMultiple, dataTypeList, excludeIds);

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   /*************************************************/
   /** ACCESSEURS **/
   /*************************************************/
   public Affichage getAffichage(){
      return affichage;
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @return controller ListeAffichages
    */
   public ListeAffichages getListeAffichages(){
      return ((ListeAffichages) self.getParent().getParent().getParent().getParent().getFellow("listeRegion")
         .getFellow("listeAffichages").getFellow("lwinAffichages").getAttributeOrFellow("lwinAffichages$composer", true));
   }

   /**
    * Ouvre ou ferme la liste d'Affichages.
    */
   private void setOpenListeAffichage(final boolean open){
      //On ferme la liste des affichages
      ((org.zkoss.zul.West) self.getParent().getParent().getParent().getParent().getFellow("listeRegion")).setOpen(open);
   }

   public void drawActionsForAffichage(){
      if(!sessionScope.containsKey("ToutesCollections")){
         drawActionsButtons("Requete");
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
      }
   }

   public ConstWord getIntituleConstraint(){
      return ExportConstraints.getIntituleConstraint();
   }

   public ConstWord getNomColonneConstraint(){
      return ExportConstraints.getNomColonneConstraint();
   }

   @Override
   public void setFocusOnElement(){}

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
   public void removeObject(final String comments){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   public List<ResultatDecorator> getResultats(){
      return resultats;
   }

   public void setResultats(final List<ResultatDecorator> r){
      this.resultats = r;
   }

   public List<Resultat> getResultatsToRemove(){
      return resultatsToRemove;
   }

   public void setResultatsToRemove(final List<Resultat> res){
      this.resultatsToRemove = res;
   }
}
