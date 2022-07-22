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
package fr.aphp.tumorotek.action.interfacage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.prelevement.DossierExterneRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 */
public class SelectDossierExterneModale extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(SelectDossierExterneModale.class);

   private static final long serialVersionUID = 5225330380328203307L;

   private Listbox dossiersBox;

   private Button select;

   private String path = "";

   private String numDossier = "";

   private List<DossierExterne> dossierExternes = new ArrayList<>();

   private DossierExterne selectedDossierExterne;

   private DossierExterneRenderer dossierExterneRenderer = new DossierExterneRenderer();

   private Listitem currentIten;

   private DossierExterne currentDossierExterne;

   // mode edition d'un prélèvement
   private boolean edit = false;

   private Prelevement prelevement;

   // @since 2.2.3-genno
   private boolean derive = false;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      // reference vers des boutons non affichés
      editC = new Button();
      validateC = new Button();
      createC = new Button();
      revertC = new Button();
      deleteC = new Button();
      cancelC = new Button();
      addNewC = new Button();

      super.doAfterCompose(comp);

      if(winPanel != null){
         winPanel.setHeight("425px");
      }

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      getBinder().loadAll();
   }

   /**
    * Méthode intialisant le composant.
    * @param pathToPage Chemin vers la page qui demande une modif
    * @param critere
    * @param isEdit
    * @param prelevement
    * @param boolean derive interfacage
    */
   public void init(final String pathToPage, final String critere, final boolean isEdit, final Prelevement prlvt,
      final boolean _d){
      this.path = pathToPage;
      this.numDossier = critere;
      this.edit = isEdit;
      this.prelevement = prlvt;
      this.derive = _d;

      searchForDossierExternes();

      getBinder().loadComponent(self);
   }

   /**
    * Méthode initialisant les dossiers externes.
    * @since 2.0.13.1 pivot code prélèvement ou numéro labo
    * @since 2.1 view query
    */
   public void searchForDossierExternes(){
      dossierExternes.clear();
      final List<Emetteur> emetteurs = SessionUtils.getEmetteursInterfacages(sessionScope);

      // Set intermediaire pour éviter doublons
      final Set<DossierExterne> dosSet = new HashSet<>();

      dosSet
         .addAll(ManagerLocator.getDossierExterneManager().findByEmetteurInListAndIdentificationManager(emetteurs, numDossier));
      if(prelevement != null){
         dosSet.addAll(ManagerLocator.getDossierExterneManager().findByEmetteurInListAndIdentificationManager(emetteurs,
            prelevement.getNumeroLabo()));
      }

      dossierExternes.addAll(dosSet);
      Collections.sort(dossierExternes, new Comparator<DossierExterne>()
      {
         @Override
         public int compare(final DossierExterne o1, final DossierExterne o2){
            return o1.getDateOperation().compareTo(o2.getDateOperation());
         }
      });

      // view
      final Banque currBank = SessionUtils.getCurrentBanque(sessionScope);
      DossierExterne dExt;
      for(final Emetteur emet : emetteurs){
         dExt = ManagerLocator.getViewHandlerFactory().sendQuery(emet, numDossier, currBank);
         if(dExt != null){
            dossierExternes.add(dExt);
         }
         if(prelevement != null){
            dExt = ManagerLocator.getViewHandlerFactory().sendQuery(emet, prelevement.getNumeroLabo(), currBank);
            if(dExt != null){
               dossierExternes.add(dExt);
            }
         }
      }

      selectedDossierExterne = null;
   }

   public void onSelect$dossiersBox(){
      deselectRow();

      selectRow(dossiersBox.getSelectedItem(), selectedDossierExterne);

      if(getCurrentDossierExterne() != null){
         select.setDisabled(false);
      }else{
         select.setDisabled(true);
      }
   }

   /**
    * Déselectionne la ligne actuellement sélectionnée.
    */
   public void deselectRow(){
      // on vérifie qu'une ligne est bien sélectionnée
      if(getCurrentDossierExterne() != null && getCurrentIten() != null){
         final int ind = dossierExternes.indexOf(getCurrentDossierExterne());
         // on lui spécifie une couleur en fonction de son
         // numéro de ligne
         if(ind > -1){
            getCurrentIten().setStyle("background-color : #e2e9fe");

            setCurrentDossierExterne(null);
            setCurrentIten(null);
         }
      }
   }

   /**
    * Sélectionne la ligne passée en paramètre.
    * @param row Row à sélectionner.
    * @param obj Objet se trouvant dans la ligne
    */
   public void selectRow(final Listitem item, final DossierExterne doss){
      setCurrentIten(item);
      setCurrentDossierExterne(doss);

      getCurrentIten().setStyle("background-color : #b3c8e8");
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$select(){
      Clients.showBusy(null);
      Events.echoEvent("onLaterSelect", self, null);
   }

   public void onLaterSelect(){
      // si on est pas en édition, on va injecter le dossier dans
      // la fiche du prélèvement
      if(!edit){
         try{
            final ResultatInjection resultat = !derive
               ? ManagerLocator.getInjectionManager().injectDossierManager(selectedDossierExterne,
                  SessionUtils.getSelectedBanques(sessionScope).get(0))
               : ManagerLocator.getInjectionManager().injectDossierDeriveManager(selectedDossierExterne,
                  SessionUtils.getSelectedBanques(sessionScope).get(0));
            resultat.setDossierExterne(selectedDossierExterne);

            // si le chemin d'accès à la page est correcte
            if(Path.getComponent(path) != null){
               // on envoie un event à cette page avec
               // le patient sélectionné
               Events.postEvent(new Event("onGetInjectionDossierExterneDone", Path.getComponent(path), resultat));
            }

            // fermeture de la fenêtre
            Events.postEvent(new Event("onClose", self.getRoot()));
            // ferme wait message
            Clients.clearBusy();

         }catch(final RuntimeException re){
            re.printStackTrace();
            log.error(re);
            // fermeture de la fenêtre
            Events.postEvent(new Event("onClose", self.getRoot()));
            // ferme wait message
            Clients.clearBusy();
            Messagebox.show(Labels.getLabel("erreur.interface"), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }else{
         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));
         // ferme wait message
         Clients.clearBusy();
         // mode édition du prlvt
         openImportDossierExterneWindow(page, path);
      }
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * pour voir recherché les dossiers existants pour l'interfacage
    * avec d'autres logiciels.
    */
   public void openImportDossierExterneWindow(final Page page, final String p){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("importDossierExterneWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(ObjectTypesFormatters.getLabel("import.dossierExternes.title", new String[] {prelevement.getCode()}));
         win.setBorder("normal");
         win.setWidth("650px");
         final int height = getMainWindow().getPanelHeight() + 30;
         win.setHeight(height + "px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateImportDossierExterneModal(win, page, p, prelevement, selectedDossierExterne);
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

   private static HtmlMacroComponent populateImportDossierExterneModal(final Window win, final Page page, final String p,
      final Prelevement prlvt, final DossierExterne dossier){
      // HtmlMacroComponent contenu dans la fenêtre : il correspond
      // au composant des collaborations.
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("importDossierExterneModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openImportDossierExterneModale");
      ua.applyProperties();
      ua.afterCompose();

      ((ImportDossierExterneModale) ua.getFellow("fwinImportDossierModale")
         .getAttributeOrFellow("fwinImportDossierModale$composer", true)).init(p, prlvt, dossier);

      return ua;
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

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
   public void setEmptyToNulls(){}

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   public String getNumDossier(){
      return numDossier;
   }

   public void setNumDossier(final String n){
      this.numDossier = n;
   }

   public List<DossierExterne> getDossierExternes(){
      return dossierExternes;
   }

   public void setDossierExternes(final List<DossierExterne> d){
      this.dossierExternes = d;
   }

   public DossierExterne getSelectedDossierExterne(){
      return selectedDossierExterne;
   }

   public void setSelectedDossierExterne(final DossierExterne s){
      this.selectedDossierExterne = s;
   }

   public DossierExterneRenderer getDossierExterneRenderer(){
      return dossierExterneRenderer;
   }

   public void setDossierExterneRenderer(final DossierExterneRenderer renderer){
      this.dossierExterneRenderer = renderer;
   }

   public Listitem getCurrentIten(){
      return currentIten;
   }

   public void setCurrentIten(final Listitem c){
      this.currentIten = c;
   }

   public DossierExterne getCurrentDossierExterne(){
      return currentDossierExterne;
   }

   public void setCurrentDossierExterne(final DossierExterne c){
      this.currentDossierExterne = c;
   }

   public boolean isEdit(){
      return edit;
   }

   public void setEdit(final boolean e){
      this.edit = e;
   }

   public Prelevement getPrelevement(){
      return prelevement;
   }

   public void setPrelevement(final Prelevement p){
      this.prelevement = p;
   }

}
