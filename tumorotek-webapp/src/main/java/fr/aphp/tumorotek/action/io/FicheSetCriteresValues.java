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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.NamingException;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Column;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.annotations.DureeComponent;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.io.export.RechercheManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.tree.export.CritereNode;
import fr.aphp.tumorotek.webapp.tree.export.ExportNode;
import fr.aphp.tumorotek.webapp.tree.export.GroupementNode;

/**
 * Classe permettant à l'utilisateur de saisir les valeurs des critères
 * et d'exécuter la requête.
 * @author pierre
 *
 */
public class FicheSetCriteresValues extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 6152666419852522833L;

   private Panel winPanel;
   private Component parent;
   private Row rowOneCollection;
   private Row rowToutesCollections;
   private Row rowSelectionCollections;
   private Label labelOneCollection;
   private Label labelToutesCollections;
   private Listbox banquesBox;
   private Html presentationLabel;

   private Column critereValueCol;
   
   private List<Banque> banques = new ArrayList<>();
   private List<Banque> availableBanques = new ArrayList<>();
   private Recherche recherche;
   private AnnotateDataBinder binder;
   private Grid exportNodesGrid;
   private List<ExportNode> exportNodes = new ArrayList<>();
   private Hashtable<Integer, Object> criteresValues = new Hashtable<>();
   private Set<Listitem> selectedBanquesItem = new HashSet<>();

   //   private Div valueDureeBox; //FIXME Implémentation durée

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);

      if(winPanel != null){
         winPanel.setHeight("465px");
      }
   }

   public void init(final List<Banque> bks, final Component p, final Recherche r){
      parent = p;
      banques = bks;
      recherche = r;

      // création de l'arbre de la requête
      final GroupementNode node = GroupementNode.convertFromGroupement(recherche.getRequete().getGroupementRacine(), null);
      exportNodes = node.getExportNodeList();
      this.exportNodesGrid.setModel(new SimpleListModel<Object>(exportNodes));
      Rows rows = this.exportNodesGrid.getRows();
      for(Component rowComp : rows.getChildren()){
         Row row = (Row) rowComp;
         row.getChildren();
      }

      final boolean hasDelegates = exportNodes.stream().filter(en -> en instanceof CritereNode)
         .anyMatch(cn -> ((CritereNode) cn).getCritere().getChamp().getChampDelegue() != null);

      // init des banques disponibles
      if(banques.size() == 1){
         final Utilisateur user = SessionUtils.getLoggedUser(sessionScope);
         final Plateforme pf = SessionUtils.getPlateforme(sessionScope);
         availableBanques = ManagerLocator.getUtilisateurManager().getAvailableBanquesByPlateformeManager(user, pf, false);

         if(hasDelegates){
            final Contexte currentContexte = SessionUtils.getCurrentBanque(sessionScope).getContexte();
            availableBanques =
               availableBanques.stream().filter(b -> currentContexte.equals(b.getContexte())).collect(Collectors.toList());
         }

         availableBanques.remove(banques.get(0));

         rowToutesCollections.setVisible(false);
         rowOneCollection.setVisible(true);
         rowSelectionCollections.setVisible(true);
         labelOneCollection.setValue(
            ObjectTypesFormatters.getLabel("execution.recherche.liste.banques.1", new String[] {banques.get(0).getNom()}));

      }else{
         rowToutesCollections.setVisible(true);
         rowOneCollection.setVisible(false);
         rowSelectionCollections.setVisible(false);

         final String parameters = banques.stream().map(Banque::getNom).collect(Collectors.joining(", "));

         labelToutesCollections
            .setValue(ObjectTypesFormatters.getLabel("execution.recherche.liste.banques.2", new String[] {parameters}));
      }

      final List<Banque> tmp = ManagerLocator.getManager(RechercheManager.class).findBanquesManager(recherche);

      String label;
      if(tmp.size() > 1){
         label = Labels.getLabel("execution.recherche.banques");
      }else{
         label = Labels.getLabel("execution.recherche.banque");
      }

      label += " " + tmp.stream().map(Banque::getNom).collect(Collectors.joining(", "));

      presentationLabel.setContent(
         ObjectTypesFormatters.getLabel("execution.recherche.presentation", new String[] {recherche.getIntitule(), label}));

      getBinder().loadComponent(banquesBox);
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Lance la recherche.
    */
   public void onClick$execute(){
      boolean ok = true;

      // pour chaque noeud de la requete
      for(int i = 0; i < exportNodes.size(); i++){
         // si c'est un critere
         if(exportNodes.get(i) instanceof CritereNode){
            // on récupère le critère et la valeur saisie
            final Critere crit = ((CritereNode) exportNodes.get(i)).getCritere();
            Object value = null;

            if(crit.getOperateur().equals("is null")){
               value = "";
            }else{
               value = exportNodes.get(i).getCritereValue();
            }
            if(value == null){
               ok = false;
            }else{
               if(crit.getOperateur().equals("like") || crit.getOperateur().equals("not like")){
                  String tmp = (String) value;
                  if(!tmp.endsWith("%")){
                     tmp = tmp + "%";
                  }
                  if(!tmp.startsWith("%")){
                     tmp = "%" + tmp;
                  }
                  value = tmp;
               }
               criteresValues.put(crit.getCritereId(), value);
            }
         }
      }

      // si certaines valeurs ne sont pas saisies => erreur!!
      if(!ok){
         Messagebox.show(Labels.getLabel("critere.value.null"), "Error", Messagebox.OK, Messagebox.ERROR);
      }else{
         Clients.showBusy(Labels.getLabel("recherche.execution.encours"));
         Events.echoEvent("onLaterExecuteSearch", self, null);
      }
   }

   /**
    * Execution de la recherche.
    */
   public void onLaterExecuteSearch(){
      banques.addAll(findSelectedBanques());

      String jdbcDialect = SessionUtils.getDbms();

      final List<Object> objets = ManagerLocator.getTraitementRequeteManager().traitementRequeteManager(recherche.getRequete(),
         banques, criteresValues, jdbcDialect);

      // on renvoie les résultats
      Events.postEvent("onGetSearchResults", getParent(), objets);
      // ferme wait message
      Clients.clearBusy();
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Retourne les banques sélectionnées.
    * @return
    */
   private List<Banque> findSelectedBanques(){
      final List<Banque> bks = new ArrayList<>();
      final Iterator<Listitem> its = banquesBox.getSelectedItems().iterator();
      while(its.hasNext()){
         bks.add(availableBanques.get(banquesBox.getItems().indexOf(its.next())));
      }
      return bks;
   }

   public void onCreate$decimalBox(Event event){
      Decimalbox decimBox = (Decimalbox) event.getData();
      decimBox.getParent();
   }

   /**
    * Pour l'execution de la recherche, à la saisie des valeurs, 
    * remplace le champ de saisie numérique en un champ de saisie de durée
    * @param event
    */
   public void onCreate$valueDecimalBox(Event event){ //FIXME Champ Duree Recherche - Trouver un meilleure moyen d'implémenter la box durée
      // On récupère le critere node grace à la méthode magique
      Object data = AbstractListeController2.getBindingData((ForwardEvent) event, false);
      if(data instanceof CritereNode){
         CritereNode cn = (CritereNode) data;
         if("duree".equals(cn.getCritere().getChamp().dataType().getType())
            || ("calcule".equals(cn.getCritere().getChamp().dataType().getType())
               && "duree".equals(cn.getCritere().getChamp().getChampAnnotation().getChampCalcule().getDataType().getType()))){
            // On a besoin de la decimalBox qui contient la valeur décimale qui sera traitée pour ce champ
            Decimalbox decimalBox = (Decimalbox) ((ForwardEvent) event).getOrigin().getTarget();

            // On cache la box pour afficher la dureeBox
            decimalBox.setVisible(false);
            DureeComponent dureeBox = drawDureeBox();
            Component parent = decimalBox.getParent();
            parent.appendChild(dureeBox);

            /* 
             * Afin de pouvoir enregistrer la saisie de la durée, dureeComponent étant un div on peut pas faire grand chose (pas d'évènements DOM)
             * L'idée c'est donc d'enregistrer la durée à chaque changement de valeur dans les champs
             */
            for(Component compo : dureeBox.getChildren()){
               if(compo instanceof Longbox){
                  Longbox box = (Longbox) compo; // On récupère les box des champs de durée
                  box.addEventListener("onChange", new EventListener<Event>()
                  {
                     @Override
                     public void onEvent(Event event) throws Exception{
                        /*
                         * On enregistre la durée dans la decimalBox car elle est enregistree automatiquement
                         * dans le CritereNode (c.f. setCriteresValuesModale.zul)
                         */
                        decimalBox.setValue(new BigDecimal(dureeBox.getDuree().getTemps(Duree.SECONDE)));
                     }
                  });
               }
            }
            
            /* Rectification de l'affichage */
            critereValueCol.setHflex("1");
            exportNodesGrid.setHflex("1");
         }
      }
   }

   /**
    * Dessine le DureeComponent pour un Champ Duree
    * @return DureeComponent Div pour un Champ Duree
    * @since 2.2.0
    */
   private DureeComponent drawDureeBox(){
      final DureeComponent dureebox = new DureeComponent();
      return dureebox;
   }

   /****************************************************/
   /************     GETTERS et SETTERS     ************/
   /****************************************************/

   public Panel getWinPanel(){
      return winPanel;
   }

   public void setWinPanel(final Panel p){
      this.winPanel = p;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public Recherche getRecherche(){
      return recherche;
   }

   public void setRecherche(final Recherche r){
      this.recherche = r;
   }

   public List<ExportNode> getExportNodes(){
      return exportNodes;
   }

   public void setExportNodes(final List<ExportNode> e){
      this.exportNodes = e;
   }

   public Hashtable<Integer, Object> getCriteresValues(){
      return criteresValues;
   }

   public void setCriteresValues(final Hashtable<Integer, Object> cValues){
      this.criteresValues = cValues;
   }

   public List<Banque> getAvailableBanques(){
      return availableBanques;
   }

   public void setAvailableBanques(final List<Banque> bks){
      this.availableBanques = bks;
   }

   public Set<Listitem> getSelectedBanquesItem(){
      return selectedBanquesItem;
   }

   public void setSelectedBanquesItem(final Set<Listitem> selectedItem){
      this.selectedBanquesItem = selectedItem;
   }

   public AnnotateDataBinder getBinder(){
      return binder;
   }

   public void setBinder(final AnnotateDataBinder b){
      this.binder = b;
   }
}
