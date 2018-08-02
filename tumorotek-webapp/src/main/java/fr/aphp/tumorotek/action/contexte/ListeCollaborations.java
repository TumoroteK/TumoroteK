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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.webapp.tree.CollaborateurNode;
import fr.aphp.tumorotek.webapp.tree.ContexteRootNode;
import fr.aphp.tumorotek.webapp.tree.ContexteTreeitemRenderer;
import fr.aphp.tumorotek.webapp.tree.EtablissementNode;
import fr.aphp.tumorotek.webapp.tree.ServiceNode;
import fr.aphp.tumorotek.webapp.tree.TumoTreeModel;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 *
 * Controller gérant une liste de collaborations (collaborateurs,
 * services et établissements).
 * Controller créé le 16/12/2009.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ListeCollaborations extends AbstractController
{

   private static final long serialVersionUID = 969513581449671289L;

   private Panel listPanel;

   private Panel searchPanel;

   /**
    * Bouton de sélection d'un élément.
    */
   private Button selectItem;

   private Button refresh;

   /**
    * Tree qui contiendra les établissements, services et collaborateurs.
    */
   private Tree mainTreeContext;

   private Grid mainGridContext;

   private Group groupCollabsIsoles;

   private Treeitem selectedItem;

   private Menubar menuBarCollaborations;

   private boolean oldOpen;

   private Textbox nomBoxCollab;

   private Textbox nomBoxEtab;

   private Textbox nomBoxService;

   private Menuitem newEtabItem;

   private Menuitem newServiceItem;

   private Menuitem newCollabItem;

   /**
    * Liste contenant les collabs isolés.
    */
   private Listbox collabsIsolesBox;

   /**
    * Components de la recherche.
    */
   private Listbox typeSearchBox;

   private Box rowCollabNom;

   private Box rowEtabNom;

   private Box rowServiceNom;

   private Box rowEtabVille;

   private Radio nomCollab;

   private Radio nomEtab;

   private Radio villeEtab;

   private Radio nomService;

   /**
    * Variables formulaires.
    */
   // variables pour la recherche
   private String searchNomCollab;

   private String searchNomEtab;

   private String searchNomService;

   private String searchVilleEtab;

   private Integer selectedIndex = 0;

   private String collabsIsolesGroupHeader = Labels.getLabel("collaborateurs.isoles");

   // Variables pour la sélection
   private String typeObjectToSelect;

   private String pageToSendObject;

   // liste de noeuds principaux de l'arbre
   private List<TumoTreeNode> rootNodes = new ArrayList<>();

   /**
    * Variables pour l'arbre.
    */
   private TumoTreeModel ttm;

   private ContexteTreeitemRenderer ctr = new ContexteTreeitemRenderer();

   /**
    * Liste des collabs isolés.
    */
   private List<Collaborateur> collabsWithoutService = new ArrayList<>();

   private Collaborateur selectedCollaborateur;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setBinder(new AnnotateDataBinder(comp));

      // Init du noeud root de l'arbre
      final ContexteRootNode root = new ContexteRootNode();
      root.readChildren();
      rootNodes = root.getChildren();

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(root);

      // init des tailles de l'arbre et de la recherche
      oldOpen = false;
      //int height = getMainWindow().getListPanelHeight() - 12;
      // listPanel.setHeight(getMainWindow().getWindowAvailableHeight()
      //		- 180 + "px");
      // int height = getMainWindow().getWindowAvailableHeight() - 220;
      // mainGridContext.setHeight(height + "px");
      // int heightTree = height - 60;
      // mainTreeContext.setHeight(heightTree + "px");

      // Init du groupe des collabs isolés
      collabsWithoutService = ManagerLocator.getCollaborateurManager().findAllObjectsWithoutService();
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("collaborateurs.isoles"));
      sb.append(" (");
      sb.append(collabsWithoutService.size());
      sb.append(")");
      collabsIsolesGroupHeader = sb.toString();
      groupCollabsIsoles.setOpen(false);

      selectItem.setVisible(false);

      generateDroits();

      getBinder().loadAll();

   }

   public void onOpen$searchPanel(){
      if(!oldOpen){
         oldOpen = true;
         // listPanel.setHeight(getMainWindow().getWindowAvailableHeight()
         //		- 270 + "px");
         // int height = getMainWindow().getWindowAvailableHeight() - 320;
         // mainGridContext.setHeight(height + "px");
         // int heightTree = height - 60;
         // mainTreeContext.setHeight(heightTree + "px");

         if(nomBoxCollab.isVisible()){
            nomBoxCollab.setFocus(true);
         }else if(nomBoxEtab.isVisible()){
            nomBoxEtab.setFocus(true);
         }else if(nomBoxService.isVisible()){
            nomBoxService.setFocus(true);
         }
      }else{
         oldOpen = false;
         // listPanel.setHeight(getMainWindow().getWindowAvailableHeight()
         //		- 175 + "px");
         // int height = getMainWindow().getWindowAvailableHeight() - 220;
         // mainGridContext.setHeight(height + "px");
         // int heightTree = height - 60;
         // mainTreeContext.setHeight(heightTree + "px");
      }
   }

   /**
    * Passe la liste des collaborations en mode de sélection : l'utilisateur
    * pourra sélectionner un élément et l'envoyer vers une autre page.
    * @param type Classe de l'objet à sélectionner.
    * @param from Path de la page vers laquelle renvoyer l'objet.
    * @param filtres Liste d'objets (services, établissements...) sur lesquels
    * @param oldSelection Liste des objets qui étaient sélectionnés.
    * il faut réaliser un filtre lors de l'ouverture de la fenêtre.
    */
   public void swithToSelectionMode(final String type, final String from, List<Object> filtres, final List<Object> oldSelection){
      selectItem.setVisible(true);
      refresh.setVisible(true);
      typeObjectToSelect = type;
      pageToSendObject = from;
      menuBarCollaborations.setVisible(true);

      // on ouvre par défaut la partie recherche
      oldOpen = true;
      listPanel.setHeight(getMainWindow().getWindowAvailableHeight() - 270 + "px");
      listPanel.setHeight("100%");
      final int height = getMainWindow().getWindowAvailableHeight() - 320;
      mainGridContext.setHeight(height + "px");
      // int heightTree = height - 60;
      // mainTreeContext.setHeight(heightTree + "px");
      searchPanel.setOpen(true);
      nomBoxCollab.setFocus(true);

      if(oldSelection != null){
         ctr.setOldSelectedObjects(oldSelection);
         getBinder().loadAttribute(self.getFellow("mainTreeContext"), "treeitemRenderer");
      }

      // si des filtres sont fournis, on recherche ces éléments
      if(filtres != null){
         updateTreeAfterResearch(getEtablissementsForObjects(filtres));
      }else{
         filtres = oldSelection;
      }

      if(filtres != null){
         for(int i = 0; i < filtres.size(); i++){
            if(filtres.get(i).getClass().getSimpleName().equals("Etablissement")){

               openEtablissement((Etablissement) filtres.get(i), true, false);

            }else if(filtres.get(i).getClass().getSimpleName().equals("Service")){

               openService((Service) filtres.get(i), true, false);

            }else if(filtres.get(i).getClass().getSimpleName().equals("Collaborateur")){

               openCollaborateurInTree((Collaborateur) filtres.get(i), true, false);
            }
         }
      }
      selectItem.setDisabled(true);

   }

   /**
    * Passe les collaborations en mode d'affichage "details".
    */
   public void switchToDetailMode(){
      selectItem.setVisible(false);
      menuBarCollaborations.setVisible(false);
      refresh.setVisible(false);
   }

   /**
    * Méthode initialisant l'arbre : les établissements sont les
    * premiers noeuds visibles.
    */
   public void initTree(){

      // Init du noeud root de l'arbre
      final ContexteRootNode root = new ContexteRootNode();
      root.readChildren();
      rootNodes = root.getChildren();

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(root);

      // Init du groupe des collabs isolés
      collabsWithoutService = ManagerLocator.getCollaborateurManager().findAllObjectsWithoutService();
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("collaborateurs.isoles"));
      sb.append(" (");
      sb.append(collabsWithoutService.size());
      sb.append(")");
      collabsIsolesGroupHeader = sb.toString();
      groupCollabsIsoles.setOpen(false);
   }

   /**
    * Méthode qui met à jour le contenu de l'arbre après une
    * création/modification sur un établissement, service ou
    * collaborateur.
    */
   public void updateTree(){
      // Init du noeud root de l'arbre
      final ContexteRootNode root = new ContexteRootNode();
      root.readChildren();

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(root);
      // Maj de l'arbre
      getBinder().loadAttribute(self.getFellow("mainTreeContext"), "model");

      //Si pas d'établissement (0 noeuds) on cache le fait de pouvoir ajouter un service
      if(0 != ttm.getChildCount(ttm.getRoot()) && getDroitOnAction("Collaborateur", "Creation")){
         newServiceItem.setDisabled(false);
      }else if(0 == ttm.getChildCount(ttm.getRoot()) || !getDroitOnAction("Collaborateur", "Creation")){
         newServiceItem.setDisabled(true);
      }
   }

   /**
    * Méthode qui met à jour le contenu de l'arbre après une
    * recherche sur un établissement, service ou
    * collaborateur.
    */
   public void updateTreeAfterResearch(final List<Etablissement> etabs){
      final List<TumoTreeNode> nodes = new ArrayList<>();
      for(int i = 0; i < etabs.size(); i++){
         final EtablissementNode node = new EtablissementNode(etabs.get(i));
         nodes.add(node);
      }

      // Init du noeud root de l'arbre
      final ContexteRootNode root = new ContexteRootNode();
      root.setChildren((ArrayList<TumoTreeNode>) nodes);

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(root);

      // Maj de l'arbre
      getBinder().loadAttribute(self.getFellow("mainTreeContext"), "model");
   }

   /**
    * Méthode qui met à jour le contenu de l'arbre après une
    * recherche sur un service.
    */
   public void updateTreeForServicesAfterResearch(final List<Service> services){
      final List<TumoTreeNode> nodes = new ArrayList<>();
      // pour chaque service
      for(int i = 0; i < services.size(); i++){
         final Service serv = services.get(i);
         // creation d'un noeud avec l'étab du service
         EtablissementNode node = new EtablissementNode(serv.getEtablissement());
         // si ce noeud n'existe pas, on l'ajoute comme racine
         if(!nodes.contains(node)){
            nodes.add(node);
         }else{
            node = (EtablissementNode) nodes.get(nodes.indexOf(node));
         }

         // ajout du service aux enfants de l'établissement
         final ServiceNode sNode = new ServiceNode(serv);
         if(node.getChildren() == null || node.getChildren().size() == 0){
            node.setChildren(new ArrayList<TumoTreeNode>());
         }
         node.getChildren().add(sNode);
      }

      // Init du noeud root de l'arbre
      final ContexteRootNode root = new ContexteRootNode();
      root.setChildren((ArrayList<TumoTreeNode>) nodes);

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(root);

      // Maj de l'arbre
      getBinder().loadAttribute(self.getFellow("mainTreeContext"), "model");
   }

   /**
    * Méthode qui met à jour le contenu de l'arbre après une
    * recherche sur un collaborateur.
    */
   public void updateTreeForCollaborateursAfterResearch(final List<Collaborateur> collaborateurs){
      final List<TumoTreeNode> nodes = new ArrayList<>();
      // pour chaque collab
      for(int i = 0; i < collaborateurs.size(); i++){
         final Collaborateur collab = collaborateurs.get(i);
         // extraction des services
         final Set<Service> services = ManagerLocator.getCollaborateurManager().getServicesManager(collab);

         // pour chaque service
         final Iterator<Service> it = services.iterator();
         while(it.hasNext()){
            final Service serv = it.next();
            // création du noeud établissement
            EtablissementNode node = new EtablissementNode(serv.getEtablissement());
            // si ce noeud n'existe pas, on l'ajoute comme racine
            if(!nodes.contains(node)){
               nodes.add(node);
            }else{
               node = (EtablissementNode) nodes.get(nodes.indexOf(node));
            }

            ServiceNode sNode = new ServiceNode(serv);
            if(node.getChildren() == null || node.getChildren().size() == 0){
               node.setChildren(new ArrayList<TumoTreeNode>());
            }

            // si le noeud étab n'avait pas déja ce service
            if(!node.getChildren().contains(sNode)){
               node.getChildren().add(sNode);
            }else{
               sNode = (ServiceNode) node.getChildren().get(node.getChildren().indexOf(sNode));
            }

            // creation du noeud collab
            final CollaborateurNode cNode = new CollaborateurNode(collab);
            if(sNode.getChildren() == null || sNode.getChildren().size() == 0){
               sNode.setChildren(new ArrayList<TumoTreeNode>());
            }
            // si le noeud service n'avait pas déja ce collab
            if(!sNode.getChildren().contains(cNode)){
               sNode.getChildren().add(cNode);
            }
         }
      }

      // Init du noeud root de l'arbre
      final ContexteRootNode root = new ContexteRootNode();
      root.setChildren((ArrayList<TumoTreeNode>) nodes);

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(root);

      // Maj de l'arbre
      getBinder().loadAttribute(self.getFellow("mainTreeContext"), "model");
   }

   /**
    * Méthode qui met à jour le contenu de la liste des collabs
    * isolés après une création/modification sur un service ou un
    * collaborateur.
    */
   public void updateCollabsWithoutService(){

      // on récupère les collaborateurs sans service
      collabsWithoutService = ManagerLocator.getCollaborateurManager().findAllObjectsWithoutService();
      // on met à jour le nb de ces collabs
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("collaborateurs.isoles"));
      sb.append(" (");
      sb.append(collabsWithoutService.size());
      sb.append(")");
      collabsIsolesGroupHeader = sb.toString();

      // on met à jour l'intitulé
      groupCollabsIsoles.setOpen(false);
      groupCollabsIsoles.setLabel(collabsIsolesGroupHeader);

      // Maj de la liste
      getBinder().loadAttribute(self.getFellow("collabsIsolesBox"), "model");

   }

   /**
    * Recupere le controller du panel.
    * @param event Event
    * @return fiche EchantillonController
    */
   public CollaborationsController getCollaborationsController(){
      return ((CollaborationsController) self.getParent().getParent().getParent().getFellow("winCollaborations")
         .getAttributeOrFellow("winCollaborations$composer", true));
   }

   /**
    * Lors de la sélection d'un noeud de l'arbre, nous afficherons la fiche
    * correspondant à cet élément.
    */
   public void onSelect$mainTreeContext(){
      collabsIsolesBox.setSelectedItem(null);
      final Object obj = mainTreeContext.getSelectedItem().getValue();

      // Si c'est un noeud établissement => FicheEtablissement
      // Si c'est un noeud service => FicheService
      // Si c'est un noeud collaborateur => FicheCollaborateur
      if(obj instanceof EtablissementNode){

         final EtablissementNode node = (EtablissementNode) obj;
         getCollaborationsController().switchToFicheEtablissementMode(node.getEtablissement());

      }else if(obj instanceof ServiceNode){

         final ServiceNode node = (ServiceNode) obj;
         getCollaborationsController().switchToFicheServiceMode(node.getService());

      }else if(obj instanceof CollaborateurNode){
         final CollaborateurNode node = (CollaborateurNode) obj;
         getCollaborationsController().switchToFicheCollaborateurMode(node.getCollaborateur());
      }

      // si nous sommes en mode de sélection
      if(selectItem.isVisible()){
         // si l'objet sélectionné peut être renvoyé, le bouton devient
         // accessible
         if(obj != null && obj.getClass().getSimpleName().equals(typeObjectToSelect + "Node")){
            selectItem.setDisabled(false);
         }else{
            selectItem.setDisabled(true);
         }
      }
   }

   /**
    * Méthode appelée lors du clic sur le bouton selectItem. Elle va envoyer,
    * par l'intermédiaire d'un event, l'objet sélectionné à la page qui
    * attend une sélection.
    */
   public void onClick$selectItem(){

      if(mainTreeContext.getSelectedItem() != null){
         final Object obj = mainTreeContext.getSelectedItem().getValue();

         if(typeObjectToSelect.equals("Etablissement")){
            // Si l'objet attendu est un établissement et que la
            // sélection est correcte
            if(obj instanceof EtablissementNode){
               final EtablissementNode node = (EtablissementNode) obj;

               // si l'objet est archivé : sélection impossible
               if(node.getEtablissement().getArchive()){
                  throw new WrongValueException(selectItem, Labels.getLabel("message.wrong.selection.inactif.message"));
               }else{
                  // si le chemin d'accès à la page est correcte
                  if(Path.getComponent(pageToSendObject) != null){
                     // on envoie un event à cette page avec
                     // l'établissement sélectionné
                     Events.postEvent(
                        new Event("onGetObjectFromSelection", Path.getComponent(pageToSendObject), node.getEtablissement()));
                  }
                  // fermeture de la fenêtre
                  Events.postEvent(new Event("onClose", self.getRoot()));
               }
            }else{
               // si l'utilisateur sélectionne autre chose qu'un
               // établissement => message d'erreur
               final StringBuffer sb = new StringBuffer();
               sb.append(Labels.getLabel("message.wrong.selection.message"));
               sb.append(" ");
               sb.append(typeObjectToSelect);

               throw new WrongValueException(selectItem, sb.toString());
            }
         }else if(typeObjectToSelect.equals("Service")){
            // Si l'objet attendu est un service et que la
            // sélection est correcte
            if(obj instanceof ServiceNode){
               final ServiceNode node = (ServiceNode) obj;

               // si l'objet est archivé : sélection impossible
               if(node.getService().getArchive()){
                  throw new WrongValueException(selectItem, Labels.getLabel("message.wrong.selection.inactif.message"));
               }else{
                  // si le chemin d'accès à la page est correcte
                  if(Path.getComponent(pageToSendObject) != null){
                     // on envoie un event à cette page avec le
                     // service sélectionné
                     Events
                        .postEvent(new Event("onGetObjectFromSelection", Path.getComponent(pageToSendObject), node.getService()));
                  }
                  // fermeture de la fenêtre
                  Events.postEvent(new Event("onClose", self.getRoot()));
               }
            }else{
               // si l'utilisateur sélectionne autre chose qu'un
               // service => message d'erreur
               final StringBuffer sb = new StringBuffer();
               sb.append(Labels.getLabel("message.wrong.selection.message"));
               sb.append(" ");
               sb.append(typeObjectToSelect);

               throw new WrongValueException(selectItem, sb.toString());
            }
         }else if(typeObjectToSelect.equals("Collaborateur")){
            // Si l'objet attendu est un collaborateur et que la
            // sélection est correcte
            if(obj instanceof CollaborateurNode){
               final CollaborateurNode node = (CollaborateurNode) obj;

               // si l'objet est archivé : sélection impossible
               if(node.getCollaborateur().getArchive()){
                  throw new WrongValueException(selectItem, Labels.getLabel("message.wrong.selection.inactif.message"));
               }else{
                  // si le chemin d'accès à la page est correcte
                  if(Path.getComponent(pageToSendObject) != null){
                     // on envoie un event à cette page avec le
                     // collaborateur sélectionné
                     Events.postEvent(new Event("onGetObjectFromSelection", Path.getComponent(pageToSendObject),
                        node.getCollaborateur().clone()));
                  }
                  // fermeture de la fenêtre
                  Events.postEvent(new Event("onClose", self.getRoot()));
               }
            }else{
               // si l'utilisateur sélectionne autre chose qu'un
               // collaborateur => message d'erreur
               final StringBuffer sb = new StringBuffer();
               sb.append(Labels.getLabel("message.wrong.selection.message"));
               sb.append(" ");
               sb.append(typeObjectToSelect);

               throw new WrongValueException(selectItem, sb.toString());
            }
         }
      }else if(collabsIsolesBox.getSelectedItem() != null){

         if(typeObjectToSelect.equals("Etablissement")){
            // établissement => message d'erreur
            final StringBuffer sb = new StringBuffer();
            sb.append(Labels.getLabel("message.wrong.selection.message"));
            sb.append(" ");
            sb.append(typeObjectToSelect);

            throw new WrongValueException(selectItem, sb.toString());
         }else if(typeObjectToSelect.equals("Service")){
            // si l'utilisateur sélectionne autre chose qu'un
            // service => message d'erreur
            final StringBuffer sb = new StringBuffer();
            sb.append(Labels.getLabel("message.wrong.selection.message"));
            sb.append(" ");
            sb.append(typeObjectToSelect);

            throw new WrongValueException(selectItem, sb.toString());
         }else if(typeObjectToSelect.equals("Collaborateur")){
            final Collaborateur collab = (Collaborateur) collabsIsolesBox.getSelectedItem().getValue();
            // si l'objet est archivé : sélection impossible
            if(collab.getArchive()){
               throw new WrongValueException(selectItem, Labels.getLabel("message.wrong.selection.inactif.message"));
            }else{
               // si le chemin d'accès à la page est correcte
               if(Path.getComponent(pageToSendObject) != null){
                  // on envoie un event à cette page avec le
                  // collaborateur sélectionné
                  Events.postEvent(new Event("onGetObjectFromSelection", Path.getComponent(pageToSendObject), collab.clone()));
               }
               // fermeture de la fenêtre
               Events.postEvent(new Event("onClose", self.getRoot()));
            }
         }

      }else{
         Events.postEvent(new Event("onGetCollaborateurFromSelection", Path.getComponent(pageToSendObject), null));

         Events.postEvent(new Event("onClose", self.getRoot()));
      }
   }

   /**
    * Méthode appelée lors d'un clic sur le bouton newEtabItem : elle
    * renvoie vers la page FicheEtablissement en mode création.
    */
   public void onClick$newEtabItem(){
      getCollaborationsController().switchToFicheEtablissementCreationMode();
   }

   /**
    * Méthode appelée lors d'un clic sur le bouton newServiceItem : elle
    * renvoie vers la page FicheService en mode création.
    */
   public void onClick$newServiceItem(){
      getCollaborationsController().switchToFicheServiceCreationMode(null);
   }

   /**
    * Méthode appelée lors d'un clic sur le bouton newCollabItem : elle
    * renvoie vers la page FicheCollaborateur en mode création.
    */
   public void onClick$newCollabItem(){
      getCollaborationsController().switchToFicheCollaborateurCreationMode(null);
   }

   /**
    * Méthode appelée lors d'une sélection sur la liste typeSearchBox.
    * Les éléemnts de formulaire seront affichés en fct de cette
    * sélection.
    */
   public void onSelect$typeSearchBox(){

      selectedIndex = typeSearchBox.getSelectedIndex();

      // recherche de collaborateurs
      if(selectedIndex == 0){

         rowCollabNom.setVisible(true);
         rowEtabVille.setVisible(false);
         rowEtabNom.setVisible(false);
         rowServiceNom.setVisible(false);

      }else if(selectedIndex == 1){
         // recherche d'établissements
         rowCollabNom.setVisible(false);
         rowEtabVille.setVisible(true);
         rowEtabNom.setVisible(true);
         rowServiceNom.setVisible(false);

      }else if(selectedIndex == 2){
         // recherche de services
         rowCollabNom.setVisible(false);
         rowEtabVille.setVisible(false);
         rowEtabNom.setVisible(false);
         rowServiceNom.setVisible(true);

      }

   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * nomBoxCollab. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$nomBoxCollab(){
      nomCollab.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * nomBoxEtab. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$nomBoxEtab(){
      nomEtab.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * villeBoxEtab. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$villeBoxEtab(){
      villeEtab.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * nomBoxService. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$nomBoxService(){
      nomService.setChecked(true);
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBoxCollab. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBoxCollab(){
      nomBoxCollab.setValue(nomBoxCollab.getValue().toUpperCase());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBoxEtab. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBoxEtab(){
      nomBoxEtab.setValue(nomBoxEtab.getValue().toUpperCase());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBoxService. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBoxService(){
      nomBoxService.setValue(nomBoxService.getValue().toUpperCase());
   }

   /**
    * Méthode appelée lors du clic sur le bouton refresh. L'arborescence
    * sera alors mise à jour.
    */
   public void onClick$refresh(){
      updateTree();
   }

   /**
    * Méthode recherche appelée lors du clic sur le bouton find.
    */
   public void onClick$find(){
      closeAllNodes();
      selectedItem = null;
      if(selectedIndex == 0){
         // recherche de collaborateurs
         if(nomCollab.isChecked()){
            // Recherche des collabs par nom
            if(searchNomCollab == null){
               searchNomCollab = "";
            }
            searchNomCollab = searchNomCollab.toUpperCase();
            final List<Collaborateur> collabs =
               ManagerLocator.getCollaborateurManager().findByNomLikeBothSideManager(searchNomCollab);

            updateTreeForCollaborateursAfterResearch(collabs);
            boolean foundOne = false;
            // pour chaque collab
            for(int i = 0; i < collabs.size(); i++){
               // s'il n'est pas isolé, on le cherche dans l'arbre
               if(!collabsWithoutService.contains(collabs.get(i))){
                  foundOne = true;
                  openCollaborateurInTree(collabs.get(i), true, true);
               }else if(!foundOne){
                  // sinon on le cherche dans la liste
                  openCollaborateurInList(collabs.get(i), true, true);
               }
            }

            if(selectItem.isVisible()){
               // si nous sommes en mode de sélection
               if(collabs.size() > 0){
                  // si l'objet sélectionné peut être renvoyé,
                  // le bouton devient accessible
                  if(typeObjectToSelect.equals("Collaborateur")){
                     selectItem.setDisabled(false);
                  }else{
                     selectItem.setDisabled(true);
                  }
               }else{
                  selectItem.setDisabled(true);
               }
            }
         }
      }else if(selectedIndex == 1){
         // recherche d'établissement
         if(nomEtab.isChecked()){
            // Recherche par nom
            if(searchNomEtab == null){
               searchNomEtab = "";
            }
            searchNomEtab = searchNomEtab.toUpperCase();
            final List<Etablissement> etabs =
               ManagerLocator.getEtablissementManager().findByNomLikeBothSideManager(searchNomEtab);

            updateTreeAfterResearch(etabs);
            // on sélectionne le 1er étab dans l'arbre
            if(etabs.size() > 0){
               openEtablissement(etabs.get(0), false, true);
            }
         }else if(villeEtab.isChecked()){
            // recherche par ville
            final List<Etablissement> etabs = ManagerLocator.getEtablissementManager().findByVilleLikeManager(searchVilleEtab);

            updateTreeAfterResearch(etabs);
            // on ouvre le 1er étab dans l'arbre
            if(etabs.size() > 0){
               openEtablissement(etabs.get(0), false, true);
            }
         }
      }else if(selectedIndex == 2){
         // recherche de services
         if(nomService.isChecked()){
            // recherche par nom
            if(searchNomService == null){
               searchNomService = "";
            }
            searchNomService = searchNomService.toUpperCase();
            final List<Service> services = ManagerLocator.getServiceManager().findByNomLikeBothSideManager(searchNomService);

            updateTreeForServicesAfterResearch(services);
            // on ouvre chaque service dans l'arbre
            for(int i = 0; i < services.size(); i++){
               if(i == 0){
                  openService(services.get(0), false, true);
               }else{
                  openService(services.get(i), false, false);
               }
            }
         }
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur utiliser la touche ENTREE sur
    * l'un des éléments du formulaire de recherche : redirection vers la
    * méthode de recherche onClick$find().
    */
   public void onPressEnterKey(){
      onClick$find();
   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupCollabsIsoles.
    */
   public void onOpen$groupCollabsIsoles(){
      final String id = groupCollabsIsoles.getUuid();
      final String idTop = mainGridContext.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");

   }

   /**
    * Ferme tous les noeuds de l'arbre.
    */
   public void closeAllNodes(){
      // on récupère tous les Treeitems de niveau 1 : ceux contenant des
      // établissements
      final Collection<Treeitem> collection = mainTreeContext.getItems();
      final Iterator<Treeitem> it = collection.iterator();
      // on ferme tous les items
      while(it.hasNext()){
         final Treeitem item = it.next();
         item.setOpen(false);
      }
   }

   /**
    * Ouvre tous les chemins de l'arbre afin d'atteindre l'établissement.
    * @param etablissement Etablissement que l'on recherche.
    * @param open Si true, ouvre les enfants de l'établissement.
    * @param select Si true, selectionne l'établissement
    */
   public void openEtablissement(final Etablissement etablissement, final boolean open, final boolean select){
      // Création d'un noeud pour l'établissement que l'on comparera
      // aux autres noeuds
      final EtablissementNode etabNode = new EtablissementNode(etablissement);

      // on récupère tous les Treeitems de niveau 1 : ceux contenant des
      // établissements
      //cCollection<Treeitem> collection = mainTreeContext.getItems();
      // Iterator<Treeitem> it = collection.iterator();
      // Treeitem etabToSelect = null;
      // pour chque item, on recherche celui contenant l'établissement
      // de notre collaborateur
      // while (it.hasNext()) {
      //	Treeitem item = it.next();
      //	if (item.getValue().equals(etabNode)) {
      //		etabToSelect = item;
      //	}
      //}
      // ouverture du noeud contenant l'établissement
      if(open){
         //etabToSelect.setOpen(true);
         ttm.addOpenPath(ttm.getPath(etabNode));
      }
      // sélection de l'établissement si voulu
      if(select){
         ttm.addSelectionPath(ttm.getPath(etabNode));
         //selectedItem = mainTreeContext
         //	.renderItemByPath(ttm.getPath(etabNode));
         //mainTreeContext.setSelectedItem(selectedItem);
         //mainTreeContext.setSelectedItem(etabToSelect);
         //EtablissementNode node = (EtablissementNode)
         //	etabToSelect.getValue();
         getCollaborationsController()
            .switchToFicheEtablissementMode(((EtablissementNode) ttm.getChild(ttm.getPath(etabNode))).getEtablissement());
      }
   }

   /**
    * Ouvre tous les chemins de l'arbre afin d'atteindre le service.
    * @param service Service que l'on recherche.
    * @param open Si true, ouvre les enfants du Service.
    * @param select Si true, selectionne le Service.
    */
   public void openService(final Service service, final boolean open, final boolean select){

      final Etablissement etablissement = service.getEtablissement();
      // Création d'un noeud pour l'établissement que l'on comparera
      // aux autres noeuds
      final EtablissementNode etabNode = new EtablissementNode(etablissement);
      final ServiceNode servNode = new ServiceNode(service);

      // on récupère tous les Treeitems de niveau 1 : ceux contenant des
      // établissements
      // Collection<Treeitem> collection = mainTreeContext.getItems();
      // Iterator<Treeitem> it = collection.iterator();
      // Treeitem etabToSelect = null;
      // pour chque item, on recherche celui contenant l'établissement
      // de notre collaborateur
      // while (it.hasNext()) {
      //	Treeitem item = it.next();
      //	if (item.getValue().equals(etabNode)) {
      //		etabToSelect = item;
      //	}
      //}

      // if (etabToSelect != null) {
      // ouverture du noeud contenant l'établissement
      //	etabToSelect.setOpen(true);
      //}

      // si on a bien trouvé l'établissement
      //		Treeitem servToSelect = null;
      //		if (etabToSelect != null) {
      //			// Récupération de tous les enfants du noeud
      //			List<Component> objs = etabToSelect.getChildren();
      //			for (int i = 0; i < objs.size(); i++) {
      //				// on extrait uniquement l'enfant de type "Treechildren"
      //				if (objs.get(i).getClass().getSimpleName()
      //						.equals("Treechildren")) {
      //					// Ce Treechildren contient tous les items de niveau 2
      //					// => les noeuds services de l'établissement
      //					Treechildren tc = (Treechildren) objs.get(i);
      //					Collection<Treeitem> servItems =
      //						tc.getItems();
      //					Iterator<Treeitem> itServ = servItems.iterator();
      //
      //					// Pour chaque item
      //					while (itServ.hasNext()) {
      //						// on vérifie que l'item contient bien un ServiceNode et
      //						// que le service est un de ceux du collaborateur
      //						Treeitem item = itServ.next();
      //						if (item.getValue().getClass().getSimpleName()
      //								.equals("ServiceNode")) {
      //							if (service.equals(((ServiceNode)
      //									item.getValue()).getService())) {
      //								// Si c'est le cas, on le place dans un liste
      //								servToSelect = item;
      //							}
      //						}
      //					}
      //				}
      //			}
      //		}

      ttm.addOpenPath(ttm.getPath(etabNode));
      // ouverture du noeud contenant le service
      if(open){
         //servToSelect.setOpen(true);
         ttm.addOpenPath(ttm.getPath(servNode));
      }
      // sélection du service, si voulu
      if(select){
         ttm.addSelectionPath(ttm.getPath(servNode));
         getCollaborationsController().switchToFicheServiceMode(((ServiceNode) ttm.getChild(ttm.getPath(servNode))).getService());
         //mainTreeContext.setSelectedItem(servToSelect);
         //ServiceNode node = (ServiceNode) servToSelect.getValue();

      }
   }

   /**
    * Ouvre un collaborateur soit dans l'arbre soit dans la liste.
    * @param collab Collaborateur que l'on souhaite afficher.
    * @param open Si true, on ouvre le noeud.
    * @param select Si true, on affiche le collaborateur.
    */
   public void openCollaborateurInTreeOrList(final Collaborateur collab, final boolean open, final boolean select){

      // si le collab est isolé, on le recherche dans la liste
      // sinon on le recherche dans l'arbre
      if(collabsWithoutService.contains(collab)){
         openCollaborateurInList(collab, open, select);
      }else{
         openCollaborateurInTree(collab, open, select);
      }

   }

   /**
    * Ouvre tous les chemins de l'arbre afin d'atteindre le collaborateur.
    * @param collab Collaborateur que l'on recherche.
    * @param open Si true, ouvre les enfants du Collaborateur.
    * @param select Si true, selectionne le Collaborateur.
    */
   public void openCollaborateurInTree(final Collaborateur collab, final boolean open, final boolean select){
      // Création d'un noeud pour le collab recherché que l'on comparera
      // aux autres noeuds
      final CollaborateurNode collabNode = new CollaborateurNode(collab);

      // on récupère les services du collaborateur recherché
      final Set<Service> services = ManagerLocator.getCollaborateurManager().getServicesManager(collab);

      final List<EtablissementNode> etabs = new ArrayList<>();
      // Pour chaque service, on récupère l'établissement
      final Iterator<Service> itServices = services.iterator();
      while(itServices.hasNext()){
         final Service tmp = itServices.next();
         final EtablissementNode node = new EtablissementNode(tmp.getEtablissement());
         if(!etabs.contains(node)){
            etabs.add(node);
            ttm.addOpenPath(ttm.getPath(node));
         }

         final ServiceNode sNode = new ServiceNode(tmp);
         ttm.addOpenPath(ttm.getPath(sNode));
      }

      // on récupère tous les Treeitems de niveau 1 : ceux contenant des
      // établissements
      //Collection<Treeitem> collection = mainTreeContext.getItems();
      //Iterator<Treeitem> it = collection.iterator();
      //List<Treeitem> etabsToselect = new ArrayList<Treeitem>();
      // pour chque item, on recherche ceux contenant un établissement
      // des services de notre collaborateur
      //while (it.hasNext()) {
      //	Treeitem item = it.next();
      //	if (etabs.contains(item.getValue())) {
      //		etabsToselect.add(item);
      //	}
      //}

      // si on a bien trouvé l'établissement
      // List<Treeitem> servicesToSelect = new ArrayList<Treeitem>();
      // for (int k = 0; k < etabsToselect.size(); k++) {
      //	Treeitem etabToSelect = etabsToselect.get(k);
      // ouverture du noeud contenant l'établissement
      //	etabToSelect.setOpen(true);
      // Récupération de tous les enfants du noeud
      //	List<Component> objs = etabToSelect.getChildren();
      //	for (int i = 0; i < objs.size(); i++) {
      // on extrait uniquement l'enfant de type "Treechildren"
      //		if (objs.get(i).getClass().getSimpleName()
      //				.equals("Treechildren")) {
      // Ce Treechildren contient tous les items de niveau 2
      // => les noeuds services de l'établissement
      //			Treechildren tc = (Treechildren) objs.get(i);
      //			Collection<Treeitem> servItems = tc.getItems();

      //			Iterator<Treeitem> itServ = servItems.iterator();
      //			Treeitem servToSelect = null;
      // Pour chaque item
      //			while (itServ.hasNext()) {
      // on vérifie que l'item contient bien un ServiceNode et
      // que le service est un de ceux du collaborateur
      //				Treeitem item = itServ.next();
      //				if (item.getValue().getClass().getSimpleName()
      //						.equals("ServiceNode")) {
      //					if (services.contains(((ServiceNode)
      //							item.getValue()).getService())) {
      // Si c'est le cas, on le place dans un liste
      //						servToSelect = item;
      //						servicesToSelect.add(servToSelect);
      //					}
      //				}
      //			}
      //		}
      //	}
      //}

      //		List<Treeitem> collabsToSelect = new ArrayList<Treeitem>();
      //		// Pour tous les items contenant un service du collaborateur
      //		for (int k = 0; k < servicesToSelect.size(); k++) {
      //			Treeitem current = servicesToSelect.get(k);
      //			current.setOpen(true);
      //			// Récupération de tous les enfants du noeud
      //			List<Component> objs = current.getChildren();
      //			for (int i = 0; i < objs.size(); i++) {
      //				// on extrait uniquement l'enfant de type "Treechildren"
      //				if (objs.get(i).getClass().getSimpleName()
      //						.equals("Treechildren")) {
      //					// Ce Treechildren contient tous les items de niveau 3
      //					// => les noeuds collabs du service courant
      //					Treechildren tc = (Treechildren) objs.get(i);
      //					Collection<Treeitem> servItems = tc.getItems();
      //
      //					Iterator<Treeitem> itColl = servItems.iterator();
      //					// Pour chaque item
      //					while (itColl.hasNext()) {
      //						Treeitem item = itColl.next();
      //						// on vérifie que l'item contient bien un
      //						// CollaborateurNode et que le collab est
      //						// celui recherché
      //						if (item.getValue().getClass().getSimpleName()
      //								.equals("CollaborateurNode")) {
      //							if (collabNode.equals(item.getValue())) {
      //								// si c'est le cas, on le place dans une liste
      //								// (il peut etre plusieurs fois ds l'arbre)
      //								collabsToSelect.add(item);
      //							}
      //						}
      //					}
      //				}
      //			}
      //		}

      // pour chaque collab à afficher
      //		for (int i = 0; i < collabsToSelect.size(); i++) {
      //			// on ouvre le noeud
      //			if (open) {
      //				collabsToSelect.get(i).setOpen(true);
      //			}
      //
      //			// si c'est le 1er collab
      //			if (i == 0 && select) {
      //				mainTreeContext.setSelectedItem(collabsToSelect.get(i));
      //				CollaborateurNode node = (CollaborateurNode)
      //					collabsToSelect.get(i).getValue();
      //				getCollaborationsController()
      //					.switchToFicheCollaborateurMode(node.getCollaborateur());
      //
      //				selectedItem = collabsToSelect.get(i);
      //
      //			}
      //		}
      // si c'est le 1er collab
      if(select){
         ttm.addSelectionPath(ttm.getPath(collabNode));
         getCollaborationsController()
            .switchToFicheCollaborateurMode(((CollaborateurNode) ttm.getChild(ttm.getPath(collabNode))).getCollaborateur());

      }
   }

   /**
    * Sélectionne un collaborateur isolé dans la liste.
    * @param collab Collaborateur que l'on souhaite afficher.
    * @param open Si true, on ouvre le noeud.
    * @param select Si true, on affiche le collaborateur.
    */
   public void openCollaborateurInList(final Collaborateur collab, final boolean open, final boolean select){

      // si la liste contient ce collab
      if(collabsWithoutService.contains(collab)){
         if(select){
            // ouverture du groupe
            groupCollabsIsoles.setOpen(true);

            // sélection et affichage du collaborateur
            final int ind = collabsWithoutService.indexOf(collab);
            collabsIsolesBox.setSelectedIndex(ind);
            getCollaborationsController().switchToFicheCollaborateurMode(collab);
         }
      }

   }

   public List<Etablissement> getEtablissementForServices(final List<Service> services){
      final List<Etablissement> etabs = new ArrayList<>();

      for(int i = 0; i < services.size(); i++){
         if(!etabs.contains(services.get(i).getEtablissement())){
            etabs.add(services.get(i).getEtablissement());
         }
      }

      return etabs;
   }

   public List<Etablissement> getEtablissementForCollaborateurs(final List<Collaborateur> collabs){
      final List<Etablissement> etabs = new ArrayList<>();

      for(int i = 0; i < collabs.size(); i++){
         final Collaborateur collab = collabs.get(i);
         // on récupère les services du collaborateur
         final Set<Service> services = ManagerLocator.getCollaborateurManager().getServicesManager(collab);

         // Pour chaque service, on récupère l'établissement
         final Iterator<Service> itServices = services.iterator();
         while(itServices.hasNext()){
            final Service tmp = itServices.next();
            if(!etabs.contains(tmp.getEtablissement())){
               etabs.add(tmp.getEtablissement());
            }
         }
      }

      return etabs;
   }

   public List<Etablissement> getEtablissementsForObjects(final List<Object> objects){
      final List<Etablissement> etabs = new ArrayList<>();

      for(int i = 0; i < objects.size(); i++){
         final Object currentObject = objects.get(i);

         if(currentObject != null){

            if(currentObject.getClass().getSimpleName().equals("Etablissement")){
               final Etablissement currentEtab = (Etablissement) currentObject;

               if(!etabs.contains(currentEtab)){
                  etabs.add(currentEtab);
               }

            }else if(currentObject.getClass().getSimpleName().equals("Service")){
               final Service currentService = (Service) currentObject;

               if(!etabs.contains(currentService.getEtablissement())){
                  etabs.add(currentService.getEtablissement());
               }

            }else if(currentObject.getClass().getSimpleName().equals("Collaborateur")){

               final Collaborateur currentCollab = (Collaborateur) currentObject;

               // on récupère les services du collaborateur
               final Set<Service> services = ManagerLocator.getCollaborateurManager().getServicesManager(currentCollab);

               // Pour chaque service, on récupère l'établissement
               final Iterator<Service> itServices = services.iterator();
               while(itServices.hasNext()){
                  final Service tmp = itServices.next();
                  if(!etabs.contains(tmp.getEtablissement())){
                     etabs.add(tmp.getEtablissement());
                  }
               }
            }
         }
      }

      return etabs;
   }

   /**
    * Méthode appelée lors de la sélection d'un collab dans la liste
    * collabsIsolesBox. Cette méthode va ouvrir la page
    * FicheCollaborateur en static mode.
    */
   public void onSelect$collabsIsolesBox(){
      mainTreeContext.setSelectedItem(null);
      selectedCollaborateur = (Collaborateur) collabsIsolesBox.getSelectedItem().getValue();

      getCollaborationsController().switchToFicheCollaborateurMode(selectedCollaborateur);

      // si nous sommes en mode de sélection
      if(selectItem.isVisible()){
         // si l'objet sélectionné peut être renvoyé, le bouton devient
         // accessible
         if(typeObjectToSelect.equals("Collaborateur")){
            selectItem.setDisabled(false);
         }else{
            selectItem.setDisabled(true);
         }
      }
   }

   public String styleListCell(final Listcell cell){
      return "";
   }

   public void generateDroits(){
      if(getDroitOnAction("Collaborateur", "Creation")){
         newCollabItem.setDisabled(false);
         newEtabItem.setDisabled(false);
         newServiceItem.setDisabled(false);
      }else{
         newCollabItem.setDisabled(true);
         newEtabItem.setDisabled(true);
         newServiceItem.setDisabled(true);
      }

      //Si pas d'établissement (0 noeuds) on cache le fait de pouvoir ajouter un service
      if(0 != ttm.getChildCount(ttm.getRoot()) && getDroitOnAction("Collaborateur", "Creation")){
         newServiceItem.setDisabled(false);
      }else if(0 == ttm.getChildCount(ttm.getRoot()) || !getDroitOnAction("Collaborateur", "Creation")){
         newServiceItem.setDisabled(true);
      }
   }

   /****************************************************/
   /**                  ACCESSEURS                    **/
   /****************************************************/

   public TumoTreeModel getTtm(){
      return ttm;
   }

   public void setTtm(final TumoTreeModel t){
      this.ttm = t;
   }

   public ContexteTreeitemRenderer getCtr(){
      return ctr;
   }

   public void setCtr(final ContexteTreeitemRenderer c){
      this.ctr = c;
   }

   public List<Collaborateur> getCollabsWithoutService(){
      return collabsWithoutService;
   }

   public void setCollabsWithoutService(final List<Collaborateur> c){
      this.collabsWithoutService = c;
   }

   public String getCollabsIsolesGroupHeader(){
      return collabsIsolesGroupHeader;
   }

   public void setCollabsIsolesGroupHeader(final String groupHeader){
      this.collabsIsolesGroupHeader = groupHeader;
   }

   public Integer getSelectedIndex(){
      return selectedIndex;
   }

   public void setSelectedIndex(final Integer index){
      this.selectedIndex = index;
   }

   public String getSearchNomCollab(){
      return searchNomCollab;
   }

   public void setSearchNomCollab(final String search){
      this.searchNomCollab = search;
   }

   public String getSearchNomEtab(){
      return searchNomEtab;
   }

   public void setSearchNomEtab(final String search){
      this.searchNomEtab = search;
   }

   public String getSearchNomService(){
      return searchNomService;
   }

   public void setSearchNomService(final String search){
      this.searchNomService = search;
   }

   public String getSearchVilleEtab(){
      return searchVilleEtab;
   }

   public void setSearchVilleEtab(final String search){
      this.searchVilleEtab = search;
   }

   public String getTypeObjectToSelect(){
      return typeObjectToSelect;
   }

   public void setTypeObjectToSelect(final String typeObject){
      this.typeObjectToSelect = typeObject;
   }

   public String getPageToSendObject(){
      return pageToSendObject;
   }

   public void setPageToSendObject(final String page){
      this.pageToSendObject = page;
   }

   public Treeitem getSelectedItem(){
      return selectedItem;
   }

   public void setSelectedItem(final Treeitem selected){
      this.selectedItem = selected;
   }

   public List<TumoTreeNode> getRootNodes(){
      return rootNodes;
   }

   public void setRootNodes(final List<TumoTreeNode> nodes){
      this.rootNodes = nodes;
   }
}
