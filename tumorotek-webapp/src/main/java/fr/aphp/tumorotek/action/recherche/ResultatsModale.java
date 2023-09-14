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
package fr.aphp.tumorotek.action.recherche;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.ListeEchantillon;
import fr.aphp.tumorotek.action.prodderive.ListeProdDerive;
import fr.aphp.tumorotek.action.utilisateur.ProfilExport;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Dessine la fiche modale avertissant l'utilisateur
 * lors d'une recherche retournant trop de résultats.
 * Date: 11/01/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.2.3-rc1
 *
 */
public class ResultatsModale extends AbstractController
{

   private static final long serialVersionUID = -1203931482648923134L;

   private Component parent;

   private List<Integer> results = new ArrayList<>();

   private String entite;

   private boolean hasAnnots;

   private boolean cessionable = false;

   private boolean deletable = false;

   private boolean selectMode = false;

   // @since 2.2.3-rc1
   private boolean canExport = false;

   private AbstractObjectTabController controller;

   // Components
   private Label nbLabel;

   private Checkbox exportBox;

   private Button afficher;

   private Menubar actions;

   private Menuitem newCessionItem;

   private Menuitem deleteItem;

   private Button select;

   // private Button cancel;
   private Button exporter;

   private Menubar menuExportBar;

   private Menuitem exportItemINCa;

   private Menuitem exportItemTVGSO;

   private Menuitem exportItemTVGSOcsv;

   private Menuitem exportItemBIOCAP;

   //@since 2.1
   private Menuitem exportItemBIOBANQUES;

   private Menuitem exportItemAdv;

   protected Menuitem patientsItem;

   protected Menuitem prelevementsItem;

   protected Menuitem echantillonsItem;

   protected Menuitem derivesItem;

   protected Menuitem derivesAscItem;

   protected Menuitem cessionsItem;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      //@since 2.2.3-rc1
      canExport = !getProfilExport().equals(ProfilExport.NO);

      setBinder(new AnnotateDataBinder(comp));
      getBinder().loadComponent(comp);
   }

   /**
    * Initialise le composant à partir des paramètres d'affichage.
    * @param message avertissement affiché à l'utilisateur
    * @param fantomable indique si le textbox de commentaire doit
    * être affiché.
    * @param deletable inique si l'avertissement seul doit être affiché.
    * @param component parent ayant demandé la modale.
    */

   public void init(final List<Integer> res, final Component prt, final String ent, final AbstractObjectTabController c){

      setParent(prt);
      setResults(res);
      nbLabel.setValue(getNbResults().toString());
      entite = ent;
      controller = c;
      hasAnnots = controller != null ? controller.hasAnnoTables() : false;

      if(entite.equals("Echantillon")){
         // droits sur les exports
         boolean inca = false;
         boolean tvgso = false;
         boolean biocap = false;
         if(sessionScope.containsKey("catalogues")){
            final Map<String, Catalogue> catsMap = (Map<String, Catalogue>) sessionScope.get("catalogues");

            inca = catsMap.containsKey("INCa");
            tvgso = catsMap.containsKey("TVGSO");
            biocap = catsMap.containsKey("BIOCAP");
         }
         exportItemINCa.setVisible(inca);
         exportItemTVGSO.setVisible(tvgso);
         exportItemTVGSOcsv.setVisible(tvgso);
         exportItemBIOCAP.setVisible(biocap);
         // export biobanques concerne toutes collections CRB
         exportItemBIOBANQUES.setVisible(true);
      }
      exportItemAdv.setVisible(hasAnnots);

      disableObjectTreeButtons();

      // select mode
      if(controller != null && controller.getListe() != null){
         selectMode = controller.getListe().getMode().equals("select");
         cessionable = (controller.getEntiteTab().getNom().equals("Echantillon")
            && ((ListeEchantillon) controller.getListe()).isCanCession())
            || (controller.getEntiteTab().getNom().equals("ProdDerive")
               && ((ListeProdDerive) controller.getListe()).isCanCession());
         deletable = controller.getListe().isCanDelete();
      }
      newCessionItem.setVisible(cessionable);
      deleteItem.setVisible(deletable);

      afficher.setVisible(!selectMode && !cessionable && !deletable);
      actions.setVisible(!selectMode && (cessionable || deletable));
      select.setVisible(selectMode);
   }

   public void onCheck$exportBox(){
      if(exportBox.isChecked()){
         afficher.setVisible(false);
         actions.setVisible(false);
         select.setVisible(false);
         if(hasAnnots){
            menuExportBar.setVisible(true);
         }else{
            exporter.setVisible(true);
         }
      }else{
         afficher.setVisible(!selectMode);
         afficher.setVisible(!selectMode && !cessionable && !deletable);
         actions.setVisible(!selectMode && (cessionable || deletable));
         select.setVisible(selectMode);
         exporter.setVisible(false);
         menuExportBar.setVisible(false);
      }
   }

   public void onClick$cancel(){
      // réalise annulation
      Events.postEvent("onDoCancel", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$afficher(){
      // réalise l'affichage
      Events.postEvent("onShowResults", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$afficherItem(){
      // réalise l'affichage
      Events.postEvent("onShowResults", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$newCessionItem(){
      // envoie les objets vers une nouvelle cession
      Events.postEvent("onDoNewCession", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$deleteItem(){
      Messagebox.show(ObjectTypesFormatters.getLabel("message.deletion.multiple", new String[] {String.valueOf(getNbResults())}),
         Labels.getLabel("general.warning"), Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION,
         new org.zkoss.zk.ui.event.EventListener<Event>()
         {
            @Override
            public void onEvent(final Event e){
               if(Messagebox.ON_OK.equals(e.getName())){
                  // fermeture de la fenêtre
                  Events.sendEvent(new Event("onClose", self.getRoot()));
                  // envoie les objets vers le batch delete
                  Events.postEvent("onDoBatchDelete", getParent(), null);
               }else if(Messagebox.ON_CANCEL.equals(e.getName())){
                  //Cancel is clicked
               }
            }
         });
   }

   public void onClick$select(){
      // réalise la sélection
      Events.postEvent("onDoSelect", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$exporter(){
      // réalise l'expot
      Events.postEvent("onDoExport", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Export les résultats au format excel.
    */
   public void onClick$exportItem(){
      // réalise l'export
      Events.postEvent("onDoExport", getParent(), null);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Export les résultats au format excel.
    */
   public void onClick$exportItemAdv(){
      openRestrictTablesModale(controller.getListe(), self, controller.getEntiteTab());
   }

   public void onClick$exportItemINCa(){
      // réalise l'export
      Events.postEvent("onDoExport", getParent(), "INCa");
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$exportItemTVGSO(){
      // réalise l'expot
      Events.postEvent("onDoExport", getParent(), "TVGSO");
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$exportItemTVGSOcsv(){
      Events.postEvent("onDoExport", getParent(), "TVGSOcsv");
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$exportItemBIOCAP(){
      // réalise l'expot
      Events.postEvent("onDoExport", getParent(), "BIOCAP");
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public Integer getNbResults(){
      return results.size();
   }

   public List<Integer> getResults(){
      return results;
   }

   public void setResults(final List<Integer> r){
      this.results = r;
   }

   public void disableObjectTreeButtons(){

      final String entiteNom = controller.getEntiteTab().getNom();

      // objets tree item
      // recupère LA banque courante ou la première banque en toutes collections
      // car par définition, même profil pour toutes les banques en toutes collections
      Banque banqueToGetAuthorisation = null;
      if(!SessionUtils.getSelectedBanques(sessionScope).isEmpty()){
         banqueToGetAuthorisation = SessionUtils.getSelectedBanques(sessionScope).get(0);
      }
      if(patientsItem != null){
         patientsItem.setDisabled(!getDroitEntiteObjectConsultation(banqueToGetAuthorisation, "Patient"));
         patientsItem.setVisible(!entiteNom.equals("Patient"));
      }
      if(prelevementsItem != null){
         prelevementsItem.setDisabled(!getDroitEntiteObjectConsultation(banqueToGetAuthorisation, "Prelevement"));
         prelevementsItem.setVisible(!entiteNom.equals("Prelevement"));
      }
      if(echantillonsItem != null){
         echantillonsItem.setDisabled(!getDroitEntiteObjectConsultation(banqueToGetAuthorisation, "Echantillon"));
         echantillonsItem.setVisible(!entiteNom.equals("Echantillon"));
      }
      if(derivesItem != null){
         derivesItem.setDisabled(!getDroitEntiteObjectConsultation(banqueToGetAuthorisation, "ProdDerive"));
      }
      if(derivesAscItem != null){
         derivesAscItem.setDisabled(!getDroitEntiteObjectConsultation(banqueToGetAuthorisation, "ProdDerive"));
         derivesAscItem.setVisible(entiteNom.equals("ProdDerive"));
      }
      if(cessionsItem != null){
         cessionsItem.setDisabled(!getDroitEntiteObjectConsultation(banqueToGetAuthorisation, "Cession"));
         cessionsItem.setVisible(!entiteNom.equals("Cession"));
      }
   }

   private void postEventCloseParent(){
      if(getParent().getRoot() instanceof Window && ((Window) getParent().getRoot()).inModal()){
         Events.postEvent("onClose", getParent().getRoot(), null);
      }
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$patientsItem(){
      postEventCloseParent();
      Events.postEvent("onClick$patientsItem", controller.getListe().getSelfComponent(), getResults());
   }

   public void onClick$prelevementsItem(){
      postEventCloseParent();
      Events.postEvent("onClick$prelevementsItem", controller.getListe().getSelfComponent(), getResults());
   }

   public void onClick$echantillonsItem(){
      postEventCloseParent();
      Events.postEvent("onClick$echantillonsItem", controller.getListe().getSelfComponent(), getResults());
   }

   public void onClick$derivesItem(){
      postEventCloseParent();
      Events.postEvent("onClick$derivesItem", controller.getListe().getSelfComponent(), getResults());
   }

   public void onClick$derivesAscItem(){
      postEventCloseParent();
      Events.postEvent("onClick$derivesAscItem", controller.getListe().getSelfComponent(), getResults());
   }

   public void onClick$cessionsItem(){
      postEventCloseParent();
      Events.postEvent("onClick$cessionsItem", controller.getListe().getSelfComponent(), getResults());
   }

   public boolean getCanExport(){
      return canExport;
   }

}
