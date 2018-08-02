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

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.ImprimanteApi;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe gérant la création et l'édition des imrpimantes définies dans le
 * système.
 * Créée le 23/03/2011.
 * @author Pierre VENTADOUR.
 * @version 2.0.11
 *
 */
public class FicheImprimanteModale extends AbstractFicheCombineController
{

   private static final long serialVersionUID = -4707630800681153754L;

   // Components
   private Combobox imprimantesBox;
   private Listbox orientationsBox;
   private Listbox imprimanteApisBox;
   private Row selectImprimante;
   private Row descImprimante;
   private Row rowMbioPrinter;

   // Objets principaux
   private Imprimante imprimante;
   private Component parent;
   private List<ImprimanteApi> imprimanteApis = new ArrayList<>();
   private ImprimanteApi selectedImprimanteApi;
   private List<String> orientations = new ArrayList<>();
   private String selectedOrientation;
   private List<String> availableImprimantes = new ArrayList<>();
   private String selectedImprimante;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      //		if (winPanel != null) {
      //			winPanel.setHeight("340px");
      //		}

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      getBinder().loadAll();
   }

   /**
    * Initialise la fenêtre : le parent, l'imprimante et passe en mode
    * create ou edit.
    * @param imp
    * @param comp
    */
   public void initImprimanteModale(final Imprimante imp, final Component comp){
      setParent(comp);
      setObject(imp);
      if(imprimante.getImprimanteId() != null){
         switchToEditMode();
      }else{
         switchToCreateMode();
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.imprimante.clone());
   }

   @Override
   public void onClick$cancelC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$revertC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$createC(){
      // vérifiction qu'une imprimante est sélectionnée
      if(selectedImprimante == null){
         throw new WrongValueException(imprimantesBox, Labels.getLabel("fiche.imprimante.error.imprimante"));
      }

      // vérifiction qu'une imprimanteApi est sélectionnée
      if(selectedImprimanteApi == null){
         throw new WrongValueException(imprimanteApisBox, Labels.getLabel("fiche.imprimante.error.imprimanteApi"));
      }

      // vérifiction qu'une orientation est sélectionnée
      if(selectedOrientation == null){
         throw new WrongValueException(orientationsBox, Labels.getLabel("fiche.imprimante.error.orientation"));
      }

      if(imprimante.getAdresseIp() != null && imprimante.getAdresseIp().equals("")){
         imprimante.setAdresseIp(null);
      }

      Clients.showBusy(Labels.getLabel("imprimante.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();

         // ferme wait message
         Clients.clearBusy();
         // fermeture de la fenetre + message envoyé au parent
         postValidateEvent();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void onClick$validateC(){
      // vérifiction qu'une imprimante api est sélectionnée
      if(selectedImprimanteApi == null){
         throw new WrongValueException(imprimanteApisBox, Labels.getLabel("fiche.imprimante.error.imprimanteApi"));
      }

      // vérifiction qu'une orientation est sélectionnée
      if(selectedOrientation == null){
         throw new WrongValueException(orientationsBox, Labels.getLabel("fiche.imprimante.error.orientation"));
      }

      if(imprimante.getAdresseIp() != null && imprimante.getAdresseIp().equals("")){
         imprimante.setAdresseIp(null);
      }

      Clients.showBusy(Labels.getLabel("imprimante.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();

         // ferme wait message
         Clients.clearBusy();
         // fermeture de la fenetre + message envoyé au parent
         postValidateEvent();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void createNewObject(){
      imprimante.setNom(selectedImprimante);

      if(selectedOrientation.equals(Labels.getLabel("Champ.Imprimante.Orientation.portrait"))){
         imprimante.setOrientation(1);
      }else{
         imprimante.setOrientation(2);
      }

      if(!selectedImprimanteApi.getNom().equals("mbio")){
         imprimante.setMbioPrinter(null);
      }else{
         imprimante.setNom(selectedImprimante + " - " + imprimante.getMbioPrinter());
      }

      // update de l'objet
      ManagerLocator.getImprimanteManager().createObjectManager(imprimante, SessionUtils.getPlateforme(sessionScope),
         selectedImprimanteApi);
   }

   public void postValidateEvent(){
      // si le chemin d'accès à la page est correcte
      if(getParent() != null){
         // on envoie un event à cette page
         Events.postEvent(new Event("onGetImprimanteDone", getParent(), null));
      }

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onSelect$imprimanteApisBox(){
      if(selectedImprimanteApi == null){
         throw new WrongValueException(imprimanteApisBox, Labels.getLabel("fiche.imprimante.error.imprimanteApi"));
      }
      Clients.clearWrongValue(imprimanteApisBox);

      if(selectedImprimanteApi.getNom().equals("mbio")){
         rowMbioPrinter.setVisible(true);
      }else{
         rowMbioPrinter.setVisible(false);
      }
   }

   public void onSelect$imprimantesBox(){
      if(selectedImprimante == null){
         throw new WrongValueException(imprimantesBox, Labels.getLabel("fiche.imprimante.error.imprimante"));
      }
      Clients.clearWrongValue(imprimantesBox);
   }

   public void onSelect$orientationsBox(){
      if(selectedOrientation == null){
         throw new WrongValueException(orientationsBox, Labels.getLabel("fiche.imprimante.error.orientation"));
      }
      Clients.clearWrongValue(orientationsBox);
   }

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
   public void updateObject(){
      if(selectedOrientation.equals(Labels.getLabel("Champ.Imprimante.Orientation.portrait"))){
         imprimante.setOrientation(1);
      }else{
         imprimante.setOrientation(2);
      }

      if(!selectedImprimanteApi.getNom().equals("mbio")){
         imprimante.setMbioPrinter(null);
      }

      // update de l'objet
      ManagerLocator.getImprimanteManager().updateObjectManager(imprimante, SessionUtils.getPlateforme(sessionScope),
         selectedImprimanteApi);
   }

   @Override
   public TKdataObject getObject(){
      return this.imprimante;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.imprimante = (Imprimante) obj;

      super.setObject(obj);
   }

   @Override
   public void setNewObject(){
      final Imprimante imp = new Imprimante();
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(0);
      imp.setLongueur(0);
      setObject(imp);
   }

   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();

      selectImprimante.setVisible(true);
      descImprimante.setVisible(false);

      initEditableMode();
      // ajout d'une imprimante fictive
      availableImprimantes.add(Labels.getLabel("fiche.imprimante.fictive"));
      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.imprimante.equals(new Imprimante()));
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();

      selectImprimante.setVisible(false);
      descImprimante.setVisible(true);

      initEditableMode();
      getBinder().loadComponent(self);
   }

   public void initEditableMode(){
      // init des orientations
      orientations = new ArrayList<>();
      orientations.add(null);
      orientations.add(Labels.getLabel("Champ.Imprimante.Orientation.portrait"));
      orientations.add(Labels.getLabel("Champ.Imprimante.Orientation.paysage"));

      // init des apis
      imprimanteApis = ManagerLocator.getImprimanteApiManager().findAllObjectsManager();
      imprimanteApis.add(0, null);

      availableImprimantes = new ArrayList<>();
      // recherche des imprimantes visible sur le réseau
      final PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
      // recherche des noms d'imprimante déjà définies
      final List<String> nomsUtilises =
         ManagerLocator.getImprimanteManager().findByPlateformeSelectNomManager(SessionUtils.getPlateforme(sessionScope));
      for(int i = 0; i < printServices.length; i++){
         // si nom valide et imprimante pas encore définie
         if(printServices[i].getName() != null && !printServices[i].getName().equals("null")
            && !nomsUtilises.contains(printServices[i].getName())){
            availableImprimantes.add(printServices[i].getName());
         }
      }
      availableImprimantes.add(0, null);

      if(this.imprimante.getImprimanteId() != null){
         selectedImprimante = imprimante.getNom();
         if(imprimante.getOrientation() == 1){
            selectedOrientation = Labels.getLabel("Champ.Imprimante.Orientation.portrait");
         }else{
            selectedOrientation = Labels.getLabel("Champ.Imprimante.Orientation.paysage");
         }
         selectedImprimanteApi = imprimante.getImprimanteApi();
         if(selectedImprimanteApi.getNom().equals("mbio")){
            rowMbioPrinter.setVisible(true);
         }
      }else{
         selectedImprimante = null;
         selectedOrientation = null;
         selectedImprimanteApi = null;
      }
   }

   /**********************************************************************/
   /*********************** GETTERS **************************************/
   /**********************************************************************/

   public Imprimante getImprimante(){
      return imprimante;
   }

   public void setImprimante(final Imprimante i){
      this.imprimante = i;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public List<ImprimanteApi> getImprimanteApis(){
      return imprimanteApis;
   }

   public void setImprimanteApis(final List<ImprimanteApi> i){
      this.imprimanteApis = i;
   }

   public ImprimanteApi getSelectedImprimanteApi(){
      return selectedImprimanteApi;
   }

   public void setSelectedImprimanteApi(final ImprimanteApi s){
      this.selectedImprimanteApi = s;
   }

   public List<String> getOrientations(){
      return orientations;
   }

   public void setOrientations(final List<String> o){
      this.orientations = o;
   }

   public String getSelectedOrientation(){
      return selectedOrientation;
   }

   public void setSelectedOrientation(final String s){
      this.selectedOrientation = s;
   }

   public List<String> getAvailableImprimantes(){
      return availableImprimantes;
   }

   public void setAvailableImprimantes(final List<String> a){
      this.availableImprimantes = a;
   }

   public String getSelectedImprimante(){
      return selectedImprimante;
   }

   public void setSelectedImprimante(final String s){
      this.selectedImprimante = s;
   }

}
