/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.controller;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Numerotation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Abstract classe regroupant les comportements partagés par les fiches
 * utilisées pour l'affichage dynamique (formulaires) des données de
 * l'application.
 * Date: 24/07/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public abstract class AbstractFicheEditController extends AbstractFicheController
{

   private static final long serialVersionUID = 1L;

   //Composants
   protected Grid formGrid;

   protected Button validate;

   protected Button revert;

   protected Button create;

   protected Button cancel;

   protected Button numerotation;

   private Object copy;

   // variable par defaut formulaire
   private final String empty = "";

   private boolean isCreate;

   private String waitLabel;

   // banque embarquée par l'objet en cours de modification
   // utile si modification dans cas recherche 'Toutes collections'
   private Banque banque;

   // numérotation
   private Numerotation currentNumerotation;

   public void setCopy(final Object c){
      this.copy = c;
   }

   public Object getCopy(){
      return copy;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   public String getEmpty(){
      return empty;
   }

   public String getWaitLabel(){
      return waitLabel;
   }

   public void setWaitLabel(final String w){
      this.waitLabel = w;
   }

   public boolean isCreate(){
      return isCreate;
   }

   public void setIsCreate(final boolean b){
      this.isCreate = b;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 2 + "px");
      }
   }

   public void onClose$winPanel(final Event event){
      Events.getRealOrigin((ForwardEvent) event).stopPropagation();
      getObjectTabController().switchToOnlyListeMode();
      if(isCreate()){
         onClick$cancel();
      }else{
         onClick$revert();
      }
   }

   /**
    * Affiche le couple edition-revert ou creation-cancel.
    */
   public void switchButtons(){
      validate.setVisible(!isCreate());
      revert.setVisible(!isCreate());
      create.setVisible(isCreate());
      cancel.setVisible(isCreate());
   }

   /**
    * Passe le mode de la fiche en creation.
    */
   public void switchToCreateMode(){
      setIsCreate(true);
      switchButtons();
      setFocusOnElement();
      setNumerotationButtonVisible();

      if(numerotation != null && numerotation.isVisible()){
         onClick$numerotation();
      }
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   public void switchToEditMode(){
      setIsCreate(false);
      switchButtons();
      setFocusOnElement();
   }

   /**
    * Raccourci quand clique 'return'.
    */
   public void onClikOk(){
      if(create.isVisible()){
         onClick$create();
      }else if(validate.isVisible()){
         onClick$validate();
      }
   }

   /**
    * Méthode appelée lors de l'appui sur la touche ENTREE :
    * valide le formulaire en mode création ou validation.
    */
   public void onOK(){
      if(validate.isVisible()){
         Events.postEvent(new Event("onClick", validate));
      }else if(create.isVisible()){
         Events.postEvent(new Event("onClick", create));
      }
   }

   /**
    * Bloque la navigation et passe le contrôle à la méthode
    * onLaterCreate.
    */
   public void onClick$create(){
      Clients.showBusy(Labels.getLabel(getWaitLabel()));
      Events.echoEvent("onLaterCreate", self, null);
   }

   /**
    * Enregistre un objet et ses annotations.
    */
   public void createObjectWithAnnots(){
      // prepare les listes de valeurs à manipuler
      getObjectTabController().getFicheAnnotation().populateValeursActionLists(true, false);

      createNewObject();

      // si tout s'est bien passé les objets manipulés par l'interface
      // ne sont plus utilisés donc pas besoin de mettre à jour les ids
      // ni l'affichage
      getObjectTabController().getFicheAnnotation().clearValeursLists(true);
   }

   /**
    * Quand l'operation de creation en base est réalisée, met à jour les
    * listes. Rend la navigation possible.
    */
   public boolean onLaterCreate(){
      try{
         createObjectWithAnnots();

         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().addToObjectList(getObject());
         }

         // mise à jour du parent
         if(getParentObject() != null){
            if(getObjectTabController().getReferencingObjectControllers() != null){
               // update du parent dans la liste
               for(int i = 0; i < getObjectTabController().getReferencingObjectControllers().size(); i++){
                  getObjectTabController().getReferencingObjectControllers().get(i).getListe()
                     .updateObjectGridListFromOtherPage(getParentObject(), true);
               }
            }
         }
         // commande le passage en mode statique
         getObjectTabController().onCreateDone();

         // ferme wait message
         Clients.clearBusy();

         return true;

      }catch(final DoublonFoundException re){
         Clients.clearBusy();
         final HashMap<String, Object> map = new HashMap<>();
         map.put("title", Labels.getLabel("error.unhandled"));
         map.put("message", handleExceptionMessage(re));
         map.put("exception", re);

         final Window window = (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
         window.doModal();

         return false;
      }catch(final Exception e){
         // ferme wait message
         Clients.clearBusy();

         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);

         return false;
      }
   }

   /**
    * Annule la creation d'un objet en détachant directement
    * les composants du formulaire sans avoir effectué d'enregistrement.
    */
   public void onClick$cancel(){

      getObjectTabController().onCancel();
   }

   /**
    * Bloque la navigation et passe le contrôle à la méthode
    * onLaterUpdate.
    */
   public void onClick$validate(){
      Clients.showBusy(Labels.getLabel(getWaitLabel()));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   /**
    * Enregistre les modifications sur un objet et ses annotations.
    */
   public void updateObjectWithAnnots(){
      // prepare les listes de valeurs à manipuler
      // 2.0.13.2 fix from true to false car
      // si champ fichier stream -> null
      getObjectTabController().getFicheAnnotation().clearValeursLists(false);

      // prepare les listes de valeurs à manipuler
      getObjectTabController().getFicheAnnotation().populateValeursActionLists(false, false);

      updateObject();

      // si tout s'est bien passé les objets manipulés par l'interface
      // ne sont plus utilisés donc pas besoin de mettre à jour les ids
      // ni l'affichage
      getObjectTabController().getFicheAnnotation().clearValeursLists(true);

   }

   /**
    * Quand l'operation de modification en base est réalisée, met à jour les
    * listes. Rend la navigation possible.
    * v2.1: gestion DoublonFoundException modale spécifique
    * v2.1: return boolean true
    *
    * @return true si aucune exception
    * @version 2.1
    */
   public boolean onLaterUpdate(){

      try{
         updateObjectWithAnnots();

         // update de la liste
         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().updateObjectGridList(getObject());
         }

         // update de l'objet parent
         if(!getObjectTabController().getReferencingObjectControllers().isEmpty() && getParentObject() != null){
            for(int i = 0; i < getObjectTabController().getReferencingObjectControllers().size(); i++){
               if(getObjectTabController().getReferencingObjectControllers().get(i).getListe() != null){
                  getObjectTabController().getReferencingObjectControllers().get(i).getListe()
                     .updateObjectGridListFromOtherPage(getParentObject(), true);
               }
            }
         }

         // update de la liste des enfants et l'enfant en fiche
         getObjectTabController()
            .updateReferencedObjects((List<TKdataObject>) getObjectTabController().getChildrenObjects(getObject()));

         // commande le passage en mode statique
         getObjectTabController().onEditDone(getObject());

         // ferme wait message
         Clients.clearBusy();

         return true;

      }catch(final DoublonFoundException re){
         Clients.clearBusy();

         final HashMap<String, Object> map = new HashMap<>();
         map.put("title", Labels.getLabel("error.unhandled"));
         map.put("message", handleExceptionMessage(re));
         map.put("exception", re);

         final Window window = (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
         window.doModal();

         return false;
      }catch(final Exception e){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
      return false;
   }

   /**
    * Efface les modifications apportees sur l'objet en détachant
    * les composants du formulaire sans avoir effectué d'enregistrement.
    */
   public void onClick$revert(){
      //revertObject();
      //clearConstraints();
      getObjectTabController().onRevert();
   }

   /**
    * Rend le bouton "numerotation" de la fiche visible si une
    * numérotation a été définie pour la banque et l'entité.
    */
   public void setNumerotationButtonVisible(){
      if(getObjectTabController() != null && getObjectTabController().getEntiteTab() != null){
         if(ManagerLocator.getNumerotationManager().findByBanqueAndEntiteManager(
            SessionUtils.getSelectedBanques(sessionScope).get(0), getObjectTabController().getEntiteTab()).size() > 0){
            currentNumerotation = ManagerLocator.getNumerotationManager().findByBanqueAndEntiteManager(
               SessionUtils.getSelectedBanques(sessionScope).get(0), getObjectTabController().getEntiteTab()).get(0);
            if(numerotation != null){
               numerotation.setVisible(true);
            }
         }else{
            currentNumerotation = null;
         }
      }else{
         currentNumerotation = null;
      }
   }

   /**
    * Incrémente la numérotation courante, met cette numérotation à jour
    * en base et retourne le code correspondant.
    * @return Le code généré.
    */
   public String generateCodeAndUpdateNumerotation(){
      if(currentNumerotation != null){
         currentNumerotation.setCurrentIncrement(currentNumerotation.getCurrentIncrement() + 1);
         // ManagerLocator.getNumerotationManager().updateObjectManager(
         // 		currentNumerotation,
         //		currentNumerotation.getBanque(),
         //		currentNumerotation.getEntite());
         return ManagerLocator.getNumerotationManager().getGeneratedCodeManager(currentNumerotation);
      }
      return null;
   }

   /*************************************************************************/
   /************************** ABSTRACTS ************************************/
   /*************************************************************************/
   /**
    * Enregistrement d'un nouvel objet dans la base.
    * @return Liste de message d'erreurs.
    */
   public abstract void createNewObject();

   /**
    * Modification de l'objet.
    * @return liste des messages d'erreurs
    */
   protected abstract void updateObject();

   /**
    * Transforme les empty values ("") provenant des input boxes en nulls.
    */
   protected abstract void setEmptyToNulls();

   /**
    * Ajoute la copy de l'objet passé en backing-bean. Permet de faire
    * un revert au besoin.
    */
   @Override
   public void setObject(final TKdataObject obj){
      setCopy(((TKAnnotableObject) obj).clone());
      super.setObject(obj);
   }

   public abstract void setFocusOnElement();

   public abstract void setFieldsToUpperCase();

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   /**
    * Test si une action est réalisable en fonction des
    * droits de l'utilisateur.
    * @param nomEntite Entite (ex.:ProdDerive).
    * @param nomOperation Type d'operation du bouton.
    */
   @Override
   public boolean getDroitOnAction(final String nomEntite, final String nomOperation){
      Boolean admin = false;
      if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin => bouton cliquable
      if(admin){
         return true;
      }
      // on extrait l'OperationType de la base
      final OperationType opeation = ManagerLocator.getOperationTypeManager().findByNomLikeManager(nomOperation, true).get(0);

      Hashtable<String, List<OperationType>> droits = new Hashtable<>();

      if(sessionScope.containsKey("Droits")){
         // on extrait les droits de l'utilisateur
         droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

         final List<OperationType> ops = droits.get(nomEntite);
         return ops.contains(opeation);
      }
      return false;
   }

   /*************************************************************************/
   /****************************** VALIDATION *******************************/
   /*************************************************************************/

   /**
    * Applique les validations assurant la cohérence de la date.
    * @param comp enregistrant date
    * @param value date
    */
   protected abstract void validateCoherenceDate(Component comp, Object value);

   public abstract void clearConstraints();

   public Numerotation getCurrentNumerotation(){
      return currentNumerotation;
   }

   public void setCurrentNumerotation(final Numerotation cNumerotation){
      this.currentNumerotation = cNumerotation;
   }

}
