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
package fr.aphp.tumorotek.action.imprimante;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ImprimanteController extends AbstractObjectTabController
{

   private final Log log = LogFactory.getLog(ImprimanteController.class);

   private static final long serialVersionUID = -6529384484810872608L;

   // components
   private Group imprimantesGroup;
   private Group modelesGroup;
   private Group affectationsGroup;
   private Grid imprimantesGrid;
   private Grid modelesGrid;
   private Grid affectationsGrid;

   // objects
   private List<Imprimante> imprimantes = new ArrayList<>();
   private List<Modele> modeles = new ArrayList<>();
   private List<AffectationDecorator> affectationDecorators = new ArrayList<>();
   private static ImprimanteRowRenderer imprimanteRenderer = new ImprimanteRowRenderer();
   private static ModeleRowRenderer modeleRenderer = new ModeleRowRenderer();
   private static AffectationRowRenderer affectationRenderer;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setBinder(new AnnotateDataBinder(comp));

      if(mainBorder != null){
         mainBorder.setHeight(getMainWindow().getPanelHeight() + "px");
      }

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() + "px");
      }

      initAssociations();
      imprimantesGroup.setOpen(false);
      modelesGroup.setOpen(false);
      affectationsGroup.setOpen(false);

      affectationRenderer = new AffectationRowRenderer(SessionUtils.getPlateforme(sessionScope));

      getBinder().loadComponent(self);

   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getImprimanteManager().findByIdManager(id);
   }

   /**
    * Init des listes d'imprimantes et de modèles.
    */
   public void initAssociations(){
      imprimantes = ManagerLocator.getImprimanteManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
      imprimantesGroup.setLabel(ObjectTypesFormatters.getLabel("imprimante.controller.imprimantes.group",
         new String[] {String.valueOf(imprimantes.size())}));

      modeles = ManagerLocator.getModeleManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
      modelesGroup.setLabel(
         ObjectTypesFormatters.getLabel("imprimante.controller.modeles.group", new String[] {String.valueOf(modeles.size())}));

   }

   public void onClick$addImprimante(){
      openImprimanteWindow(page, self, new Imprimante());
   }

   public void onClickImprimante(final ForwardEvent event){
      final Object obj = event.getData();

      openImprimanteWindow(page, self, (Imprimante) obj);
   }

   public void onClick$addDefaultModele(){
      openModeleWindow(page, self, new Modele());
   }

   public void onClick$addDynamiqueModele(){
      openModeleDynWindow(page, self, new Modele());
   }

   public void onClickModele(final ForwardEvent event){
      if(event.getData() != null){
         final Modele mod = (Modele) event.getData();

         if(mod.getIsDefault()){
            openModeleWindow(page, self, mod);
         }else{
            openModeleDynWindow(page, self, mod);
         }
      }
   }

   public void onGetImprimanteDone(final Event e){
      imprimantes = ManagerLocator.getImprimanteManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
      imprimantesGroup.setLabel(ObjectTypesFormatters.getLabel("imprimante.controller.imprimantes.group",
         new String[] {String.valueOf(imprimantes.size())}));

      getBinder().loadComponent(imprimantesGrid);
      getBinder().loadComponent(imprimantesGroup);
   }

   /**
    * Méthode appelée lorsque l'utilisateur souhaite supprimer
    * une imrpiamnte.
    * @param event
    */
   public void onClickDeleteImprimante(final ForwardEvent event){
      final Imprimante imp = (Imprimante) event.getData();

      if(imp != null){
         // confirmation
         if(Messagebox.show(Labels.getLabel("imprimante.controller.delete.imprimante"), Labels.getLabel("message.deletion.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            // suppression
            ManagerLocator.getImprimanteManager().removeObjectManager(imp);
            // maj
            imprimantes = ManagerLocator.getImprimanteManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
            imprimantesGroup.setLabel(ObjectTypesFormatters.getLabel("imprimante.controller.imprimantes.group",
               new String[] {String.valueOf(imprimantes.size())}));

            getBinder().loadComponent(imprimantesGrid);
            getBinder().loadComponent(affectationsGrid);
         }
      }
   }

   public void onGetModeleDone(final Event e){
      modeles = ManagerLocator.getModeleManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
      modelesGroup.setLabel(
         ObjectTypesFormatters.getLabel("imprimante.controller.modeles.group", new String[] {String.valueOf(modeles.size())}));

      getBinder().loadComponent(modelesGrid);
      getBinder().loadComponent(modelesGroup);
   }

   /**
    * Méthode appelée lorsque l'utilisateur souhaite supprimer
    * un modèle.
    * @param event
    */
   public void onClickDeleteModele(final ForwardEvent event){
      final Modele mod = (Modele) event.getData();

      if(mod != null){
         // confirmation
         if(Messagebox.show(Labels.getLabel("imprimante.controller.delete.modele"), Labels.getLabel("message.deletion.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            // suppression
            ManagerLocator.getModeleManager().removeObjectManager(mod);
            // maj
            modeles = ManagerLocator.getModeleManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
            modelesGroup.setLabel(ObjectTypesFormatters.getLabel("imprimante.controller.modeles.group",
               new String[] {String.valueOf(modeles.size())}));

            getBinder().loadComponent(modelesGrid);
            getBinder().loadComponent(affectationsGrid);
         }
      }
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      return null;
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   @Override
   public AbstractFicheEditController getFicheEdit(){
      return null;
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return null;
   }

   @Override
   public AbstractFicheStaticController getFicheStatic(){
      return null;
   }

   @Override
   public AbstractListeController2 getListe(){
      return null;
   }

   /**********************************************************************/
   /*********************** Gestion des affectations**********************/
   /**********************************************************************/

   /**
    * Lors de l'ouverture du groupe, on recherche toutes les autorisations
    * des utilisateurs actifs.
    */
   public void onOpen$affectationsGroup(){
      if(affectationsGroup.isOpen()){
         final List<Plateforme> pfs = new ArrayList<>();
         pfs.add(SessionUtils.getPlateforme(sessionScope));
         // utilisateurs actifs
         final List<Utilisateur> users = ManagerLocator.getUtilisateurManager().findByArchiveManager(false, pfs);

         affectationDecorators = new ArrayList<>();
         // pour chaque tuilisateur
         for(int i = 0; i < users.size(); i++){
            // on extrait les banques accessibles pour la pf actuelle
            final List<Banque> bks = ManagerLocator.getBanqueManager().findByUtilisateurAndPFManager(users.get(i),
               SessionUtils.getPlateforme(sessionScope));

            // pour chaque banque, on crée un AffectationDecorator
            for(int j = 0; j < bks.size(); j++){
               final AffectationDecorator aff =
                  new AffectationDecorator(users.get(i), bks.get(j), j == 0, j == bks.size() - 1, bks.size());
               affectationDecorators.add(aff);
            }
         }
         getBinder().loadComponent(affectationsGrid);
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur souhaite modifier une
    * affectation.
    * @param event
    */
   public void onClickEditAffectation(final ForwardEvent event){
      final AffectationDecorator aff = (AffectationDecorator) event.getData();
      // on passe le décorator en éditable et on recharge la grid
      aff.setEdit(true);
      getBinder().loadComponent(affectationsGrid);
   }

   /**
    * Méthode appelée lorsque l'utilisateur valide une modif sur une
    * affectation.
    * @param event
    */

   public void onClickValidateAffectation(final ForwardEvent event){
      // on récupère les donneés : la listbox contenant les imprimantes,
      // celle contenant es modèles et le decorator d'affectation
      final List<Object> datas = (List<Object>) event.getData();
      final Listbox liImp = (Listbox) datas.get(0);
      final Listbox liMod = (Listbox) datas.get(1);
      final AffectationDecorator aff = (AffectationDecorator) datas.get(2);

      // on récupère l'imprimante sélectionnée
      Imprimante selectedImprimante = null;
      if(liImp.getSelectedItem().getValue() != null){
         selectedImprimante = ManagerLocator.getImprimanteManager()
            .findByNomAndPlateformeManager((String) liImp.getSelectedItem().getValue(), SessionUtils.getPlateforme(sessionScope))
            .get(0);
      }

      // on récupère le modèle sélectionné
      Modele selectedModele = null;
      if(liMod.getSelectedItem().getValue() != null){
         selectedModele = ManagerLocator.getModeleManager()
            .findByNomAndPlateformeManager((String) liMod.getSelectedItem().getValue(), SessionUtils.getPlateforme(sessionScope))
            .get(0);
      }

      try{
         AffectationImprimante ai = null;
         // on cherche si une affectation existait déjà
         final List<AffectationImprimante> liste = ManagerLocator.getAffectationImprimanteManager()
            .findByBanqueUtilisateurManager(aff.getBanque(), aff.getUtilisateur());
         if(liste.size() > 0){
            ai = liste.get(0);
         }else{
            ai = new AffectationImprimante();
         }

         // on sauvegarde l'affectation
         ManagerLocator.getAffectationImprimanteManager().createObjectManager(new AffectationImprimante(), aff.getUtilisateur(),
            aff.getBanque(), selectedImprimante, selectedModele);

         // l'imprimante faisant partie de la PK, si celle ci a changé
         // et qu'une affectation existait, on la supprime
         if(ai.getImprimante() != null && !ai.getImprimante().equals(selectedImprimante)){
            ManagerLocator.getAffectationImprimanteManager().removeObjectManager(ai);
         }

         // Maj de la grid
         aff.setEdit(false);
         getBinder().loadComponent(affectationsGrid);
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur souhaite supprimer
    * une affectation d'imrpiamnte.
    * @param event
    */
   public void onClickDeleteAffectation(final ForwardEvent event){
      final AffectationDecorator aff = (AffectationDecorator) event.getData();

      AffectationImprimante ai = null;
      // on cherche si une affectation existait
      final List<AffectationImprimante> liste =
         ManagerLocator.getAffectationImprimanteManager().findByBanqueUtilisateurManager(aff.getBanque(), aff.getUtilisateur());
      if(liste.size() > 0){
         ai = liste.get(0);
      }

      // si une affectation était définie
      if(ai != null){
         // confirmation
         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.affectationImprimante")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            // suppression
            ManagerLocator.getAffectationImprimanteManager().removeObjectManager(ai);
            // maj
            getBinder().loadComponent(affectationsGrid);
         }
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur annule une modif sur une
    * affectation.
    * @param event
    */
   public void onClickCancelAffectation(final ForwardEvent event){
      final AffectationDecorator aff = (AffectationDecorator) event.getData();
      // on repasse le decirator en non editable et on recharge
      // la grid
      aff.setEdit(false);
      getBinder().loadComponent(affectationsGrid);
   }

   /**********************************************************************/
   /*********************** MODALES WINDOWS ******************************/
   /**********************************************************************/

   public void openImprimanteWindow(final Page page, final Component comp, final Imprimante imp){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("imprimanteWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fiche.imprimante.title"));
         win.setBorder("normal");
         win.setWidth("550px");
         win.setHeight("450px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateFicheImprimanteModal(win, page, comp, imp);
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

   private static HtmlMacroComponent populateFicheImprimanteModal(final Window win, final Page page, final Component comp,
      final Imprimante imp){
      // HtmlMacroComponent contenu dans la fenêtre : il correspond
      // au composant des collaborations.
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("ficheImprimanteModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openImprimanteModale");
      ua.applyProperties();
      ua.afterCompose();

      ((FicheImprimanteModale) ua.getFellow("fwinImprimanteModale").getAttributeOrFellow("fwinImprimanteModale$composer", true))
         .initImprimanteModale(imp, comp);

      return ua;
   }

   /**
    * Ouvre une fenêtre permettant de définir un modèle d'étiquettes
    * basé sur celui de la v1 : il est fixe.
    * @param page Page.
    * @param comp Composant.
    * @param mod Modèle.
    */
   public void openModeleWindow(final Page page, final Component comp, final Modele mod){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("modeleWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fiche.modele.title"));
         win.setBorder("normal");
         win.setWidth("550px");
         win.setHeight("465px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateFicheModeleModal(win, page, comp, mod);
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

   private static HtmlMacroComponent populateFicheModeleModal(final Window win, final Page page, final Component comp,
      final Modele mod){
      // HtmlMacroComponent contenu dans la fenêtre : il correspond
      // au composant des collaborations.
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("ficheModeleModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openModeleModale");
      ua.applyProperties();
      ua.afterCompose();

      ((FicheModeleModale) ua.getFellow("fwinModeleModale").getAttributeOrFellow("fwinModeleModale$composer", true))
         .initImprimanteModale(mod, comp);

      return ua;
   }

   /**
    * Ouvre une fenêtre permettant de définir un modèle d'étiquettes
    * entièrement paramétrable.
    * @param page Page.
    * @param comp Composant.
    * @param mod Modèle.
    */
   public void openModeleDynWindow(final Page page, final Component comp, final Modele mod){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("modeleDynWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fiche.modele.dynamique.title"));
         win.setBorder("normal");
         win.setWidth("600px");
         win.setHeight("600px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateFicheModeleDynModal(win, page, comp, mod);
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

   private static HtmlMacroComponent populateFicheModeleDynModal(final Window win, final Page page, final Component comp,
      final Modele mod){
      // HtmlMacroComponent contenu dans la fenêtre : il correspond
      // au composant des modèles dynamiques.
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("ficheModeleDynModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openModeleDynModale");
      ua.applyProperties();
      ua.afterCompose();

      ((FicheModeleDynModale) ua.getFellow("fwinModeleDynModale").getAttributeOrFellow("fwinModeleDynModale$composer", true))
         .initImprimanteModale(mod, comp);

      return ua;
   }

   /**********************************************************************/
   /*********************** GETTERS **************************************/
   /**********************************************************************/

   public List<Imprimante> getImprimantes(){
      return imprimantes;
   }

   public void setImprimantes(final List<Imprimante> i){
      this.imprimantes = i;
   }

   public List<Modele> getModeles(){
      return modeles;
   }

   public void setModeles(final List<Modele> m){
      this.modeles = m;
   }

   public static ImprimanteRowRenderer getImprimanteRenderer(){
      return imprimanteRenderer;
   }

   public static void setImprimanteRenderer(final ImprimanteRowRenderer iRenderer){
      ImprimanteController.imprimanteRenderer = iRenderer;
   }

   public static ModeleRowRenderer getModeleRenderer(){
      return modeleRenderer;
   }

   public static void setModeleRenderer(final ModeleRowRenderer mRenderer){
      ImprimanteController.modeleRenderer = mRenderer;
   }

   public List<AffectationDecorator> getAffectationDecorators(){
      return affectationDecorators;
   }

   public void setAffectationDecorators(final List<AffectationDecorator> a){
      this.affectationDecorators = a;
   }

   public static AffectationRowRenderer getAffectationRenderer(){
      return affectationRenderer;
   }

   public static void setAffectationRenderer(final AffectationRowRenderer a){
      ImprimanteController.affectationRenderer = a;
   }

}
