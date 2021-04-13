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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.CritereDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.export.CritereNode;
import fr.aphp.tumorotek.webapp.tree.export.ExportNode;
import fr.aphp.tumorotek.webapp.tree.export.GroupementNode;

public class FicheRequete extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(FicheRequete.class);

   private static final long serialVersionUID = 1L;

   private Label intituleLabel;

   /**
    *  Editable components : mode d'édition ou de création.
    */
   private Label intituleRequired;
   private Textbox intituleBox;
   private Button addCritereButton;

   private Column returnCritereCol;

   private Row rowTemporaire;

   private Grid criteresGrid;

   private Grid exportNodesGrid;

   /** Objets principaux. */
   private Requete requete;
   private Critere critere;

   // private List<CritereDecorator> criteres = 
   //							new ArrayList<CritereDecorator>();
   private ListModelList<CritereDecorator> criteresModel = new ListModelList<>();

   private GroupementNode node;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      winPanel.setHeight(getMainWindow().getPanelHeight() - 110 + "px");

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.intituleLabel});

      setObjBoxsComponents(new Component[] {this.intituleBox, returnCritereCol, rowTemporaire, addCritereButton});

      setRequiredMarks(new Component[] {this.intituleRequired});

      drawActionsForRequete();

      super.switchToStaticMode(true);

      getBinder().loadAll();
   }

   @Override
   public TKdataObject getObject(){
      return this.requete;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.requete = (Requete) obj;

      this.criteresModel.clear();
      if(this.requete.getRequeteId() != null){
         node = GroupementNode.convertFromGroupement(requete.getGroupementRacine(), null);
         this.exportNodesGrid.setModel(new SimpleListModel<>(node.getExportNodeList()));
      }else{
         node = null;
         this.exportNodesGrid.setModel(new SimpleListModel<>(new ArrayList<ExportNode>()));
      }

      super.setObject(obj);
   }

   @Override
   public void setNewObject(){
      setObject(new Requete());
   }

   @Override
   public void cloneObject(){
      setClone(this.requete.clone());
   }

   @Override
   public RequeteController getObjectTabController(){
      return (RequeteController) super.getObjectTabController();
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      node = null;

      this.criteresModel.clear();
      // this.criteresGrid.setModel(new 
      //		SimpleListModel<CritereDecorator>(criteres));

      this.criteresGrid.setVisible(true);

      this.exportNodesGrid.setVisible(true);
      this.exportNodesGrid.setModel(new SimpleListModel<>(new ArrayList<ExportNode>()));

      //On ferme la liste des requetes
      setOpenListeRequete(false);

      getBinder().loadAll();
   }

   @Override
   public void switchToStaticMode(){

      super.switchToStaticMode(requete == null || this.requete.equals(new Requete()));

      this.criteresGrid.setVisible(false);

      this.exportNodesGrid.setVisible(true);

      //On ouvre la liste des requetes
      setOpenListeRequete(true);

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){

      super.switchToEditMode();

      node = GroupementNode.convertFromGroupement(requete.getGroupementRacine(), null);

      this.criteresModel.clear();
      // this.criteresGrid.setModel(new 
      //		SimpleListModel<CritereDecorator>(criteres));

      this.criteresGrid.setVisible(true);

      this.exportNodesGrid.setModel(new SimpleListModel<>(node.getExportNodeList()));
      this.exportNodesGrid.setVisible(true);

      //On ferme la liste des requetes
      setOpenListeRequete(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){
      try{
         setEmptyToNulls();
         setObject(saveRequete(SessionUtils.getLoggedUser(sessionScope)));
      }catch(final Exception e){
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public void onClick$addNewC(){
      switchToCreateMode();
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$createC(){
      if(node == null){
         Messagebox.show(Labels.getLabel("requete.validation.containsCritere"), Labels.getLabel("general.error"), Messagebox.OK,
            Messagebox.ERROR);
      }else{
         Clients.showBusy(Labels.getLabel("requete.creation.encours"));
         Events.echoEvent("onLaterCreate", self, null);
      }
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         disableToolBar(false);

         if(getListeRequetes() != null){
            // ajout de l'affichage à la liste
            getListeRequetes().addToObjectList(this.requete);
         }
         clearData();
         // maj de la fenetre d'execution de la requete
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

      if(requete != null){
         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.requete")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

            try{
               /**
                * On supprime l'objet en base de données, 
                * avec cascade sur les recherches
                */
               ManagerLocator.getRequeteManager().removeObjectManager(requete);

               // On retire la requete de la liste
               getListeRequetes().removeObjectAndUpdateList(requete);

               // On efface les données du formulaire
               clearData();
            }catch(final RuntimeException re){
               // ferme wait message
               Clients.clearBusy();
               Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
            }

            // On efface les données du formulaire
            clearData();
         }
      }
   }

   @Override
   public void onClick$editC(){
      if(this.requete != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      revertRequete();
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){
      if(node == null){
         Messagebox.show(Labels.getLabel("requete.validation.containsCritere"), Labels.getLabel("general.error"), Messagebox.OK,
            Messagebox.ERROR);
      }else{
         Clients.showBusy(Labels.getLabel("requete.creation.encours"));
         Events.echoEvent("onLaterUpdate", self, null);
      }
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
         switchToStaticMode();

         if(getListeRequetes() != null){
            getListeRequetes().updateObjectGridList(this.requete);
         }
         // maj de la fenetre d'execution de la requete
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

         setObject(updateRequete(SessionUtils.getLoggedUser(sessionScope)));
      }catch(final Exception e){
         log.error(e);
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   public void revertRequete(){
      requete.setCreateur(((Requete) getClone()).getCreateur());
      requete.setGroupementRacine(((Requete) getClone()).getGroupementRacine());
      requete.setIntitule(((Requete) getClone()).getIntitule());
      requete.setRequeteId(((Requete) getClone()).getRequeteId());
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(intituleBox);
   }

   @Override
   public void setEmptyToNulls(){
      if(this.requete.getIntitule().equals("")){
         this.requete.setIntitule(null);
      }
   }

   public void onClick$deleteCritere(final Event event){
      // on demande confirmation à l'utilisateur
      // de supprimer le critere
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel("message.deletion.critere")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
         // on récupère le critère que l'utilisateur veut
         // suppimer
         final CritereDecorator cr = (CritereDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
         // on enlève le critère de la liste et on la met à jour
         criteresModel.remove(cr);
         // ListModel<CritereDecorator> list = 
         //		new ListModelList<CritereDecorator>(criteres);
         // criteresGrid.setModel(list);
      }
   }

   /**
    * Ajout d'un critère à l'arbre de la requete.
    * @param event
    */
   public void onClick$putCritere(final Event event){

      // on récupère le critère que l'utilisateur veut
      // supprimer
      final CritereDecorator critereDecorator =
         (CritereDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      critere = critereDecorator.getCritere();
      if(node != null){
         // on affiche la fenêtre de saisie

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("saisieOperateurCrit");
         win.setPage(self.getPage());
         win.setMaximizable(true);
         win.setSizable(true);
         win.setClosable(true);
         win.setTitle(Labels.getLabel("ficheSaisieOperateurCritere.panel.title"));
         win.setBorder("normal");
         win.setWidth("600px");
         win.setHeight("240px");
         win.setParent(self);

         // HtmlMacroComponent contenu dans la fenêtre : il correspond
         // au composant des collaborations.
         HtmlMacroComponent ua;
         ua = (HtmlMacroComponent) page.getComponentDefinition("saisieOperateurCritere", false).newInstance(page, null);
         ua.setParent(win);
         ua.setId("saisie");
         ua.applyProperties();
         ua.afterCompose();

         ((FicheSaisieOperateurCritere) ua.getFellow("fwinSaisieOperateurCritere")
            .getAttributeOrFellow("fwinSaisieOperateurCritere$composer", true)).init(Path.getPath(self));

         try{
            win.onModal();

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }

      }else{
         node = new GroupementNode();
         node.addCritere(critere, null, null);

         //on supprime le critere de la liste des criteres
         criteresModel.remove(critereDecorator);
         // criteresGrid.setModel(new SimpleListModel<CritereDecorator>(criteres));

         //on affiche le tableau d'ExportNode
         exportNodesGrid.setModel(new SimpleListModel<>(node.getExportNodeList()));
      }

   }

   /**
    * Ouvre la fenetre permettant de creer un nouveau critere.
    */
   public void onClick$addCritereButton(){
      openAddCritereWindow(page, SessionUtils.getSelectedBanques(sessionScope).get(0));
   }

   /**
    * PopUp window appelée pour permettre la création d'un nouveau critère.
    * @param page dans laquelle inclure la modale
    */
   public void openAddCritereWindow(final Page page, final Banque b){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("addCritereWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("requete.modale.title"));
         win.setBorder("normal");
         win.setWidth("400px");
         final int height = 310;
         win.setHeight(String.valueOf(height) + "px");
         win.setClosable(true);

         final HtmlMacroComponent ua;
         ua = (HtmlMacroComponent) page.getComponentDefinition("addCritereModale", false).newInstance(page, null);
         ua.setParent(win);
         ua.setId("addCritereModaleComponent");
         ua.applyProperties();
         ua.afterCompose();

         ((FicheAddCritere) ua.getFellow("fwinAddCritereModale").getAttributeOrFellow("fwinAddCritereModale$composer", true))
            .init(self, b);
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               //progress.detach();
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   public void onGetCritere(final Event e){
      if(e.getData() != null){
         final CritereDecorator cd = (CritereDecorator) e.getData();

         /** On ajoute les données dans la liste affichable. */
         //			this.criteres.add(cd);
         criteresModel.add(cd);
         // this.criteresGrid.setModel(new SimpleListModel<CritereDecorator>(criteres));
         //			getBinder().loadComponent(criteresGrid);
      }
   }

   public void onCloseSaisieOperateurCritere(final Event e){
      if(e.getData() != null){
         // On ajoute un nouveau CritereNode au GroupementNode
         this.getNode().addCritere(this.getCritere(), (CritereNode) ((Object[]) e.getData())[0],
            (Integer) (((Object[]) e.getData())[1]));
         // on enlève le critère de la liste de critères de la fiche parente
         // et on la met à jour
         criteresModel.remove(new CritereDecorator(this.getCritere()));
         //			ListModel<CritereDecorator> list = 
         //					new ListModelList<CritereDecorator>(this.getCriteres());
         //			criteresGrid.setModel(list);
         this.setCritere(null);

         // on affiche le tableau d'ExportNode
         exportNodesGrid.setModel(new SimpleListModel<>(node.getExportNodeList()));
      }
   }

   public void onClick$returnCritere(final Event event){

      // on récupère les critères que l'utilisateur veut
      // remettre dans la liste temporaires
      final ExportNode en = (ExportNode) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on enlève le critère de la liste et on la met à jour
      List<Critere> crit = new ArrayList<>();
      crit = ExportNode.deleteNode(en, node);
      if(node != null && node.getChildCount() < 1){
         node = null;
      }
      criteresModel.addAll(CritereDecorator.decorateListe(crit));
      // ListModel<CritereDecorator> listCrit = 
      //				new ListModelList<CritereDecorator>(criteres);
      // criteresGrid.setModel(listCrit);
      if(node != null){
         final ListModel<ExportNode> list = new ListModelList<>(node.getExportNodeList());
         exportNodesGrid.setModel(list);
      }else{
         exportNodesGrid.setModel(new SimpleListModel<>(new ArrayList<ExportNode>()));
      }

   }

   public void onClick$removeCritere(final Event event){
      // on demande confirmation à l'utilisateur
      // de supprimer les criteres
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel("message.deletion.critere")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
         // on récupère le critère que l'utilisateur veut
         // suppimer
         final ExportNode en = (ExportNode) AbstractListeController2.getBindingData((ForwardEvent) event, false);
         ExportNode.deleteNode(en, node);
         if(node != null && node.getChildCount() < 1){
            node = null;
         }
         if(node != null){
            final ListModel<ExportNode> list = new ListModelList<>(node.getExportNodeList());
            exportNodesGrid.setModel(list);
         }else{
            exportNodesGrid.setModel(new SimpleListModel<>(new ArrayList<ExportNode>()));
         }
      }

   }

   protected Requete saveRequete(final Utilisateur createur){
      requete = new Requete();
      requete.setIntitule(intituleBox.getValue());
      requete.setGroupementRacine(node.convertToGroupement());
      ManagerLocator.getRequeteManager().createObjectManager(requete, requete.getGroupementRacine(), createur,
         SessionUtils.getSelectedBanques(sessionScope).get(0));
      return requete;
   }

   protected Requete updateRequete(final Utilisateur createur){
      ((Requete) getClone()).setIntitule(intituleBox.getValue());
      Groupement groupement = null;
      if(node != null){
         groupement = node.convertToGroupement();
      }
      ManagerLocator.getRequeteManager().updateObjectManager(((Requete) getClone()), groupement, createur);
      return ((Requete) getClone());
   }

   /*************************************************/
   /** ACCESSEURS **/
   /*************************************************/

   //	public List<CritereDecorator> getCriteres() {
   //		return criteres;
   //	}
   //
   //	public void setCriteres(List<CritereDecorator> crits) {
   //		this.criteres = crits;
   //	}

   public Critere getCritere(){
      return critere;
   }

   public void setCritere(final Critere c){
      this.critere = c;
   }

   public GroupementNode getNode(){
      return node;
   }

   public void setNode(final GroupementNode n){
      this.node = n;
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @return controller ListeRequetes
    */
   private ListeRequetes getListeRequetes(){
      return getObjectTabController().getListeRequetes();
   }

   //	private FicheRecherche getFicheRecherche() {
   //		return ((FicheRecherche) self.getParent().getParent().getParent()
   //				.getParent().getParent().getParent().getParent()
   //				.getFellow("panelRecherche")
   //				.getFellow("rechercheMacro")
   //				.getFellow("winRecherche")
   //				.getFellow("mainBorder")
   //				.getFellow("ficheRegion")
   //				.getFellow("ficheRecherche")
   //				.getFellow("winFicheRecherche").
   //				getAttributeOrFellow("winFicheRecherche$composer", true));
   //	}
   //	
   /**
    * Ouvre ou ferme la liste de Requetes.
    */
   private void setOpenListeRequete(final boolean open){
      //On ferme la liste des requetes
      ((org.zkoss.zul.West) self.getParent().getParent().getParent().getParent().getFellow("listeRegion")).setOpen(open);
   }

   public void drawActionsForRequete(){
      if(!sessionScope.containsKey("ToutesCollections")){
         drawActionsButtons("Requete");
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
      }
   }

   private final ConstWord constr = new ConstWord();
   {
      constr.setNullable(false);
   }

   public ConstWord getConstr(){
      return constr;
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

   public ListModelList<CritereDecorator> getCriteresModel(){
      return criteresModel;
   }

   public void setCriteresModel(final ListModelList<CritereDecorator> cs){
      this.criteresModel = cs;
   }
}
