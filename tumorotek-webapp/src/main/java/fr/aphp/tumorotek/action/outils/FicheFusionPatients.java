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
package fr.aphp.tumorotek.action.outils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Fiche permettant la fusion de deux patients.
 *
 * @author Pierre Ventadour
 * Le 27/07/2011.
 *
 */
public class FicheFusionPatients extends AbstractFicheCombineController
{

   private final Log log = LogFactory.getLog(FicheFusionPatients.class);

   private static final long serialVersionUID = -6573663322216502290L;

   private Textbox nomFirstBox;

   private Textbox nomSecondBox;

   private Div patientAConserverMaladiesDiv;

   private Div patientASupprimerMaladiesDiv;

   private Div patientAConserverMedecinsDiv;

   private Div patientASupprimerMedecinsDiv;

   private Div patientAConserverPrelevementsDiv;

   private Div patientASupprimerPrelevementsDiv;

   private Button fusionButton;

   private Patient patientAConserver;

   private Patient patientASupprimer;

   private String commentaires;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();

   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(true);

      editC.setVisible(false);
      deleteC.setVisible(false);
      createC.setVisible(false);
      addNewC.setVisible(false);

      getBinder().loadComponent(self);
   }

   /**
    * Recherche du patient à conserver.
    */
   public void onClick$searchFirstPatient(){
      final String critereValue = nomFirstBox.getValue();
      openSelectPatientWindow(Path.getPath(self), "onGetFirstPatientFromSelection", true, critereValue, patientASupprimer);
   }

   /**
    * Méthode permettant de recevoir le patient à conserver
    * sélectionné dans la modal.
    * @param e Event contenant le patient sélectionné.
    */
   public void onGetFirstPatientFromSelection(final Event e){
      if(e.getData() != null){

         patientAConserver = (Patient) e.getData();

         // on dessine les maladies
         final List<Maladie> maladies = ManagerLocator.getMaladieManager().findByPatientNoSystemManager(patientAConserver);
         List<String> values = new ArrayList<>();
         for(int i = 0; i < maladies.size(); i++){
            final StringBuffer sb = new StringBuffer();
            sb.append(maladies.get(i).getLibelle());
            if(maladies.get(i).getCode() != null){
               sb.append(" [");
               sb.append(maladies.get(i).getCode());
               sb.append("]");
            }
            values.add(sb.toString());
         }
         drawLabelWithPopup(values, patientAConserverMaladiesDiv);

         // on dessine les prlvts
         values = new ArrayList<>();
         for(int i = 0; i < maladies.size(); i++){
            final Iterator<Prelevement> it =
               ManagerLocator.getMaladieManager().getPrelevementsManager(maladies.get(i)).iterator();

            while(it.hasNext()){
               final Prelevement p = it.next();
               if(!values.contains(p.getCode())){
                  values.add(p.getCode());
               }
            }
         }
         drawLabelWithPopup(values, patientAConserverPrelevementsDiv);

         // on dessine les médecins
         final List<Collaborateur> medecins =
            new ArrayList<>(ManagerLocator.getPatientManager().getMedecinsManager(patientAConserver));
         values = new ArrayList<>();
         for(int i = 0; i < medecins.size(); i++){
            values.add(medecins.get(i).getNomAndPrenom());
         }
         drawLabelWithPopup(values, patientAConserverMedecinsDiv);

         if(patientAConserver != null && patientASupprimer != null){
            fusionButton.setDisabled(false);
         }
      }
   }

   /**
    * Suppression de la sélection du patient à conserver.
    */
   public void onClick$deleteFirstPatient(){
      patientAConserver = null;
      Components.removeAllChildren(patientAConserverMaladiesDiv);
      Components.removeAllChildren(patientAConserverPrelevementsDiv);
      Components.removeAllChildren(patientAConserverMedecinsDiv);
      nomFirstBox.setValue(null);
      getBinder().loadComponent(nomFirstBox);
      fusionButton.setDisabled(true);
   }

   /**
    * Recherche du patient à supprimer.
    */
   public void onClick$searchSecondPatient(){
      final String critereValue = nomSecondBox.getValue();
      openSelectPatientWindow(Path.getPath(self), "onGetSecondPatientFromSelection", true, critereValue, patientAConserver);
   }

   /**
    * Méthode permettant de recevoir le patient à supprimer
    * sélectionné dans la modal.
    * @param e Event contenant le patient sélectionné.
    */
   public void onGetSecondPatientFromSelection(final Event e){
      if(e.getData() != null){

         patientASupprimer = (Patient) e.getData();

         // on dessine les maladies
         final List<Maladie> maladies = ManagerLocator.getMaladieManager().findByPatientNoSystemManager(patientASupprimer);
         List<String> values = new ArrayList<>();
         for(int i = 0; i < maladies.size(); i++){
            final StringBuffer sb = new StringBuffer();
            sb.append(maladies.get(i).getLibelle());
            if(maladies.get(i).getCode() != null){
               sb.append(" [");
               sb.append(maladies.get(i).getCode());
               sb.append("]");
            }
            values.add(sb.toString());
         }
         drawLabelWithPopup(values, patientASupprimerMaladiesDiv);

         // on dessine les prlvts
         values = new ArrayList<>();
         for(int i = 0; i < maladies.size(); i++){
            final Iterator<Prelevement> it =
               ManagerLocator.getMaladieManager().getPrelevementsManager(maladies.get(i)).iterator();

            while(it.hasNext()){
               final Prelevement p = it.next();
               if(!values.contains(p.getCode())){
                  values.add(p.getCode());
               }
            }
         }
         drawLabelWithPopup(values, patientASupprimerPrelevementsDiv);

         // on dessine les médecins
         final List<Collaborateur> medecins =
            new ArrayList<>(ManagerLocator.getPatientManager().getMedecinsManager(patientASupprimer));
         values = new ArrayList<>();
         for(int i = 0; i < medecins.size(); i++){
            values.add(medecins.get(i).getNomAndPrenom());
         }
         drawLabelWithPopup(values, patientASupprimerMedecinsDiv);

         if(patientAConserver != null && patientASupprimer != null){
            fusionButton.setDisabled(false);
         }
      }
   }

   /**
    * Suppression de la sélection du patient à supprimer.
    */
   public void onClick$deleteSecondPatient(){
      patientASupprimer = null;
      Components.removeAllChildren(patientASupprimerMaladiesDiv);
      Components.removeAllChildren(patientASupprimerPrelevementsDiv);
      Components.removeAllChildren(patientASupprimerMedecinsDiv);
      nomSecondBox.setValue(null);
      getBinder().loadComponent(nomSecondBox);
      fusionButton.setDisabled(true);
   }

   /**
    * Fusion des deux patients.
    */
   public void onClick$fusionButton(){
      openFusionWindow(page, Labels.getLabel("message.fusion.label"));
   }

   /**
    * Récupération du commentaire saisi lors de la fusion.
    * @param event
    */
   public void onFusionTriggered(final Event event){
      Clients.showBusy(Labels.getLabel("deletion.general.wait"));
      if(event.getData() != null){
         commentaires = (String) event.getData();
      }
      Events.echoEvent("onLaterFusion", self, null);
   }

   /**
    * Réalise la fusion des deux patients.
    */
   public void onLaterFusion(){
      try{
         setEmptyToNulls();

         ManagerLocator.getPatientManager().fusionPatientManager(patientAConserver, patientASupprimer,
            SessionUtils.getLoggedUser(sessionScope), commentaires);

         // maj du panel patient
         if(getMainWindow().isFullfilledComponent("patientPanel", "winPatient")){
            final PatientController patientController = ((PatientController) getMainWindow().getMainTabbox().getTabpanels()
               .getFellow("patientPanel").getFellow("winPatient").getAttributeOrFellow("winPatient$composer", true));

            // on enlève le patient supprimé
            patientController.getListe().removeObjectFromList(patientASupprimer);

            // on update le patient conservé
            patientController.getListe().updateObjectGridListFromOtherPage(patientAConserver, true);

            // maj de la liste des prlvts
            if(getMainWindow().isFullfilledComponent("prelevementPanel", "winPrelevement")){
               final PrelevementController prelevementController =
                  ((PrelevementController) getMainWindow().getMainTabbox().getTabpanels().getFellow("prelevementPanel")
                     .getFellow("winPrelevement").getAttributeOrFellow("winPrelevement$composer", true));
               prelevementController.getListe().initObjectsBox();
            }
         }

         // suppression des objets sélectionnés
         patientAConserver = null;
         Components.removeAllChildren(patientAConserverMaladiesDiv);
         Components.removeAllChildren(patientAConserverPrelevementsDiv);
         Components.removeAllChildren(patientAConserverMedecinsDiv);
         nomFirstBox.setValue(null);

         patientASupprimer = null;
         Components.removeAllChildren(patientASupprimerMaladiesDiv);
         Components.removeAllChildren(patientASupprimerPrelevementsDiv);
         Components.removeAllChildren(patientASupprimerMedecinsDiv);
         nomSecondBox.setValue(null);

         fusionButton.setDisabled(true);
         getBinder().loadAll();

         // ferme wait message
         Clients.clearBusy();

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         log.error(re);
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void setEmptyToNulls(){
      if(this.patientAConserver.getNom() != null && this.patientAConserver.getNom().equals("")){
         this.patientAConserver.setNom(null);
      }
      if(this.patientAConserver.getNip() != null && this.patientAConserver.getNip().equals("")){
         this.patientAConserver.setNip(null);
      }
      if(this.patientAConserver.getNomNaissance() != null && this.patientAConserver.getNomNaissance().equals("")){
         this.patientAConserver.setNomNaissance(null);
      }
      if(this.patientAConserver.getPrenom() != null && this.patientAConserver.getPrenom().equals("")){
         this.patientAConserver.setPrenom(null);
      }
      if(this.patientAConserver.getPaysNaissance() != null && this.patientAConserver.getPaysNaissance().equals("")){
         this.patientAConserver.setPaysNaissance(null);
      }
      if(this.patientAConserver.getVilleNaissance() != null && this.patientAConserver.getVilleNaissance().equals("")){
         this.patientAConserver.setVilleNaissance(null);
      }
   }

   @Override
   public void cloneObject(){}

   @Override
   public void clearData(){
      super.clearData();
   }

   @Override
   public void createNewObject(){}

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$deleteC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setObject(final TKdataObject obj){}

   //	/**
   //	 * Méthode appelée lorsque l'utilisateur clique sur le lien
   //	 * pour voir recherché les patients existants lors de la
   //	 * création d'un nouveau prélèvement.
   //	 * @param page dans laquelle inclure la modale
   //	 * @param path Chemin vers la page ayant appelée cette modale.
   //	 * @param critere Critere de recherche des patients.
   //	 */
   //	public void openSelectPatientWindow(Page page,
   //			String path, String returnMethode,
   //			boolean isFusionPatients, String critere,
   //			Patient patientAExclure) {
   //		 if (!isBlockModal()) {
   //
   //			 setBlockModal(true);
   //
   //			// nouvelle fenêtre
   //			final Window win = new Window();
   //			win.setVisible(false);
   //			win.setId("selectPatientWindow");
   //			win.setPage(page);
   //			win.setMaximizable(true);
   //			win.setSizable(true);
   //			win.setTitle(Labels.getLabel("bloc.prelevement.patient"));
   //			win.setBorder("normal");
   //			win.setWidth("650px");
   ////			int height = 470;
   ////			win.setHeight(height + "px");
   //			win.setClosable(true);
   //
   //			final HtmlMacroComponent ua = populateSelectPatientModal(
   //					win, page, path, returnMethode, isFusionPatients,
   //					critere, patientAExclure);
   //			ua.setVisible(false);
   //
   //			win.addEventListener("onTimed", new EventListener<Event>() {
   //				public void onEvent(Event event) throws Exception {
   //					//progress.detach();
   //					ua.setVisible(true);
   //				}
   //			});
   //
   //			Timer timer = new Timer();
   //			timer.setDelay(500);
   //			timer.setRepeats(false);
   //			timer.addForward("onTimer", timer.getParent(), "onTimed");
   //			win.appendChild(timer);
   //			timer.start();
   //
   //			try {
   //				win.onModal();
   //				setBlockModal(false);
   //
   //			} catch (SuspendNotAllowedException e) { log.error(e);
   //			}
   //		 }
   //	}
   //
   //	private static HtmlMacroComponent populateSelectPatientModal(
   //			Window win, Page page,
   //			String path, String returnMethode,
   //			boolean isFusionPatients, String critere,
   //			Patient patientAExclure) {
   //		// HtmlMacroComponent contenu dans la fenêtre : il correspond
   //		// au composant des collaborations.
   //		HtmlMacroComponent ua;
   //		ua = (HtmlMacroComponent)
   //		page.getComponentDefinition("ficheSelectPatientModale", false)
   //			.newInstance(page, null);
   //		ua.setParent(win);
   //		ua.setId("openSelectPatientModale");
   //		ua.applyProperties();
   //		ua.afterCompose();
   //
   //		((SelectPatientModale) ua.getFellow("fwinSelectPatientModale")
   //				.getAttributeOrFellow("fwinSelectPatientModale$composer", true))
   //				.init(path, returnMethode, isFusionPatients, critere,
   //						patientAExclure);
   //
   //		return ua;
   //	}

   /**
    * Méthode affichant la modale permettant de valider la fusion
    * et de saisir un commentaire.
    * @param page
    * @param message
    */
   public void openFusionWindow(final Page page, final String message){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("fusionWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("message.fusion.title"));
         win.setBorder("normal");
         win.setWidth("400px");
         //			int height = 175;
         //			win.setHeight(String.valueOf(height) + "px");
         win.setClosable(true);

         final HtmlMacroComponent ua;
         ua = (HtmlMacroComponent) page.getComponentDefinition("fusionModale", false).newInstance(page, null);
         ua.setParent(win);
         ua.setId("fusionModaleComponent");
         ua.applyProperties();
         ua.afterCompose();

         ((FicheFusionModale) ua.getFellow("fwinFusionModale").getAttributeOrFellow("fwinFusionModale$composer", true))
            .init(message, self);
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

   @Override
   public void setFocusOnElement(){}

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){}

   /**
    * Dessine dans un label le ou les libelles avec
    * l'utilisation d'un tooltip pour afficher la totalité.
    * @param
    * @param row Parent
    */
   private void drawLabelWithPopup(final List<String> values, final Div div){
      Components.removeAllChildren(div);

      if(!values.isEmpty()){
         final Label label1 = new Label(values.get(0));
         label1.setSclass("formValue");
         // dessine le label avec un lien vers popup
         if(values.size() > 1){
            final Hbox labelAndLinkBox = new Hbox();
            labelAndLinkBox.setSpacing("5px");
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup malPopUp = new Popup();
            malPopUp.setParent(div);
            final Iterator<String> it = values.iterator();
            String next;
            Label libelleStaticLabel = null;
            final Vbox popupVbox = new Vbox();
            while(it.hasNext()){
               next = it.next();
               libelleStaticLabel = new Label(next);
               libelleStaticLabel.setSclass("formValue");

               popupVbox.appendChild(libelleStaticLabel);
            }
            malPopUp.appendChild(popupVbox);
            moreLabel.setTooltip(malPopUp);
            labelAndLinkBox.appendChild(label1);
            labelAndLinkBox.appendChild(moreLabel);
            labelAndLinkBox.setParent(div);
         }else{
            label1.setParent(div);
         }
      }else{
         new Label().setParent(div);
      }
   }

   /***********************************************************/
   /****************** GETTERS et SETTERS *********************/
   /***********************************************************/

   public String getPatientAConserverDateNaissanceFormated(){
      if(patientAConserver != null){
         return ObjectTypesFormatters.dateRenderer2(this.patientAConserver.getDateNaissance());
      }
      return null;
   }

   public String getPatientASupprimerDateNaissanceFormated(){
      if(patientASupprimer != null){
         return ObjectTypesFormatters.dateRenderer2(this.patientASupprimer.getDateNaissance());
      }
      return null;
   }

   public Patient getPatientAConserver(){
      return patientAConserver;
   }

   public void setPatientAConserver(final Patient p){
      this.patientAConserver = p;
   }

   public Patient getPatientASupprimer(){
      return patientASupprimer;
   }

   public void setPatientASupprimer(final Patient p){
      this.patientASupprimer = p;
   }

   public String getCommentaires(){
      return commentaires;
   }

   public void setCommentaires(final String c){
      this.commentaires = c;
   }
}
