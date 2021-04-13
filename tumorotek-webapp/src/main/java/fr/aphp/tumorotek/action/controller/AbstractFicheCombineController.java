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
package fr.aphp.tumorotek.action.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.retour.ListeRetour;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Abstract classe regroupant les comportements partagés par les fiches
 * utilisées pour l'affichage combiné statique et dynamique (formulaires)
 * des données de l'application.
 * Utilise les listes de composants déclarés comme statiques ou dynamiques
 * pour assigner leur affichage visible true/false. Utile pour les objets
 * ne contenant que peu de données (petits formulaires).
 * Date: 26/07/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public abstract class AbstractFicheCombineController extends AbstractFicheController
{

   private static final long serialVersionUID = 1L;

   protected Button createC;
   protected Button cancelC;
   protected Button validateC;
   protected Button revertC;
   protected Button addNewC;
   protected Button deleteC;
   protected Button editC;
   protected Button printC;
   protected Menuitem historique;
   private boolean isAnonyme;

   private TKdataObject clone;

   private Component[] objLabelsComponents;
   private Component[] objBoxsComponents;
   private Component[] requiredMarks;

   private boolean canEdit;
   private boolean canDelete;
   private boolean canNew;
   private boolean canSeeHistorique;
   private boolean admin;

   private String deletionMessage;

   private ListeRetour listeRetour;

   public String getDeletionMessage(){
      return deletionMessage;
   }

   public void setDeletionMessage(final String delMessage){
      this.deletionMessage = delMessage;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      //applyDroitsOnListe();
      if(sessionScope.containsKey("Anonyme") && (Boolean) sessionScope.get("Anonyme")){
         isAnonyme = true;
      }else{
         isAnonyme = false;
      }
   }

   public TKdataObject getClone(){
      return clone;
   }

   public void setClone(final TKdataObject c){
      this.clone = c;
   }

   public Component[] getObjLabelsComponents(){
      return objLabelsComponents;
   }

   public void setObjLabelsComponents(final Component[] cs){
      this.objLabelsComponents = cs;
   }

   public Component[] getObjBoxsComponents(){
      return objBoxsComponents;
   }

   public void setObjBoxsComponents(final Component[] cs){
      this.objBoxsComponents = cs;
   }

   public Component[] getRequiredMarks(){
      return requiredMarks;
   }

   public void setRequiredMarks(final Component[] rM){
      this.requiredMarks = rM;
   }

   public ListeRetour getListeRetour(){
      return listeRetour;
   }

   public void setListeRetour(final ListeRetour lR){
      this.listeRetour = lR;
   }

   @Override
   public void setObject(final TKdataObject obj){
      cloneObject();
      disableToolBar(false);
      getBinder().loadComponent(self);
   }

   @Override
   public void setNewObject(){
      editC.setDisabled(true);
      deleteC.setDisabled(true);
      //printC.setDisabled(true);
   }

   /**
    * Change mode de la fiche en mode statique.
    * Permet l'affichage du bouton addNew
    * @param boolean si l'objet populant la fiche est empty ou non
    */
   public void switchToStaticMode(final boolean isEmptyObj){

      //switch buttons
      this.validateC.setVisible(false);
      this.revertC.setVisible(false);
      this.createC.setVisible(false);
      this.cancelC.setVisible(false);
      this.addNewC.setVisible(true);
      // edit and delete enabled if obj not empty
      this.deleteC.setVisible(!isEmptyObj);
      this.editC.setVisible(!isEmptyObj);

      switchComponentsVisibility(true);

      if(getObjectTabController() != null && getObjectTabController().getListe() != null){
         getObjectTabController().getListe().switchToEditMode(false);
      }
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   public void switchToEditMode(){
      switchButtonsToCreateOrEdit(false);
      switchComponentsVisibility(false);
      setFocusOnElement();

      if(getObjectTabController() != null && getObjectTabController().getListe() != null){
         getObjectTabController().getListe().switchToEditMode(true);
      }
   }

   /**
    * Change mode de la fiche en mode création.
    */
   public void switchToCreateMode(){
      setNewObject();
      switchButtonsToCreateOrEdit(true);
      switchComponentsVisibility(false);
      setFocusOnElement();

      if(getObjectTabController() != null && getObjectTabController().getListe() != null){
         getObjectTabController().getListe().switchToEditMode(true);
      }
   }

   /**
    * Gèle ou dégèle les boutons de la toolbar de la fiche statique.
    * Cascade vers les boutons de la liste et de la fiche annotation.
    * @param b
    */
   public void disableToolBar(final boolean b){
      editC.setDisabled(b || !isCanEdit());
      deleteC.setDisabled(b || !isCanDelete());
      addNewC.setDisabled(b || !isCanNew());

      if(historique != null){
         historique.setDisabled(b || !isCanSeeHistorique());
      }
   }

   private void switchButtonsToCreateOrEdit(final boolean isCreate){
      validateC.setVisible(!isCreate);
      revertC.setVisible(!isCreate);
      createC.setVisible(isCreate);
      cancelC.setVisible(isCreate);
      addNewC.setVisible(false);
      editC.setVisible(false);
      deleteC.setVisible(false);
   }

   private void switchComponentsVisibility(final boolean isStatic){
      //switch properties components
      for(int i = 0; i < getObjBoxsComponents().length; i++){
         getObjBoxsComponents()[i].setVisible(!isStatic);
      }
      for(int i = 0; i < getObjLabelsComponents().length; i++){
         getObjLabelsComponents()[i].setVisible(isStatic);
      }
      for(int i = 0; i < getRequiredMarks().length; i++){
         getRequiredMarks()[i].setVisible(!isStatic);
      }
   }

   /**
    * Efface le contenu de la fiche.
    */
   public void clearData(){
      setNewObject();
      switchToStaticMode();
   }

   public void onClick$addNewC(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterAddNew", self, null);
   }

   public void onLaterAddNew(){
      switchToCreateMode();
      // ferme wait message
      Clients.clearBusy();
   }

   public void onClick$createC(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         switchToStaticMode();
         disableToolBar(false);
         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   public void revertObject(){
      setObject(getClone());
   }

   /**
    * Supprime l'objet de la base.
    * Retire l'objet de la liste.
    */
   public void onClick$deleteC(){
      if(getObject() != null){

         final boolean archive = prepareDeleteObject();

         openDeleteWindow(page, getDeleteMessage(), isCascadable(), isFantomable(), isDeletable(), self, archive);
      }
   }

   /**
    * Recoit l'evenement envoyé depuis la modale pour 
    * commander la suppression. L'evenement contient en data 
    * les commentaires liés à la suppression.
    * @param event
    */
   public void onDeleteTriggered(final Event event){
      Clients.showBusy(getDeleteWaitLabel());
      String comments = null;
      if(event.getData() != null){
         comments = (String) event.getData();
      }
      Events.echoEvent("onLaterDelete", self, comments);
   }

   public void onLaterDelete(final Event event){
      try{
         final List<Integer> ids = new ArrayList<>();
         ids.add(getObject().listableObjectId());
         final Map<Entite, List<Integer>> children = getObjectTabController().getChildrenObjectsIds(ids);

         final Map<Entite, List<Integer>> parents = getObjectTabController().getParentsObjectsIds(ids);

         removeObject((String) event.getData());

         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().removeObjectAndUpdateList(getObject());
         }
         // clear form
         clearData();

         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().selectFirstObjet();
         }

         // update de l'objet parent
         //			if (!getObjectTabController()
         //					.getReferencingObjectControllers().isEmpty()
         //												&& parent != null) {
         //				for (int i = 0; i < getObjectTabController()
         //							.getReferencingObjectControllers().size(); i++) {
         //					if (getObjectTabController()
         //							.getReferencingObjectControllers()
         //												.get(i).getListe() != null) {
         //						getObjectTabController()
         //							.getReferencingObjectControllers().get(i).getListe()
         //						.updateObjectGridListFromOtherPage(parent, true);
         //					}
         //				}
         //			}

         // update de la liste des parents
         getObjectTabController().updateParentsReferences(parents);

         // update de la liste des enfants
         getObjectTabController().updateChildrenReferences(children, true);

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   /**
    * Méthode appelée lors de l'appui sur la touche ENTREE : 
    * valide le formulaire en mode création ou validation.
    */
   public void onOK(){
      if(validateC.isVisible()){
         Events.postEvent(new Event("onClick", validateC));
      }else if(createC.isVisible()){
         Events.postEvent(new Event("onClick", createC));
      }
   }

   public void onClick$editC(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterEdit", self, null);
   }

   public void onLaterEdit(){
      try{
         if(getObject() != null){
            switchToEditMode();
         }
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   public void onClick$validateC(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterValidate", self, null);
   }

   public void onLaterValidate(){
      try{
         updateObject();
         cloneObject();
         switchToStaticMode();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   public void onClick$revertC(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterRevert", self, null);
   }

   public void onLaterRevert(){
      try{
         switchToStaticMode();
         revertObject();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   public void onClick$cancelC(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterCancel", self, null);
   }

   public void onLaterCancel(){
      try{
         clearData();
         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().deselectRow();
            getObjectTabController().getListe().selectFirstObjet();
         }
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   /*************************************************************************/
   /************************** ABSTRACTS ************************************/
   /*************************************************************************/
   public abstract void cloneObject();

   public abstract void createNewObject();

   public abstract void updateObject();

   /**
    * Prepare la méthode de suppression de l'objet. Verifie si ce dernier 
    * est utilisé ou référencé.
    * @return true si la suppression devient archivage.
    */
   public abstract boolean prepareDeleteObject();

   /**
    * Appelle la méthode de suppression de l'objet en base de données.
    * @param commentaires liés à la suppression.
    */
   public abstract void removeObject(String comments);

   public abstract void setEmptyToNulls();

   public abstract void switchToStaticMode();

   public abstract void setFocusOnElement();

   public abstract void setFieldsToUpperCase();

   @Override
   public abstract void setParentObject(TKdataObject obj);

   @Override
   public abstract TKdataObject getParentObject();

   public abstract String getDeleteWaitLabel();

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   /**
    * Rend les boutons edit, addNew et delete cliquable en fonction
    * des droits de l'utilisateur.
    * @param nomEntite Entite (ex.:ProdDerive).
    */
   
   public void drawActionsButtons(final String nomEntite){
      Boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin => boutons cliquables
      if(admin){
    	 setAdmin(true);
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
         setCanSeeHistorique(true);
      }else{
         // on extrait les OperationTypes de la base
         final OperationType creation = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Creation", true).get(0);
         final OperationType modification =
            ManagerLocator.getOperationTypeManager().findByNomLikeManager("Modification", true).get(0);
         final OperationType archivage = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Archivage", true).get(0);

         Hashtable<String, List<OperationType>> droits = new Hashtable<>();

         if(sessionScope.containsKey("Droits")){
            // on extrait les droits de l'utilisateur
            droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

            final List<OperationType> ops = new ArrayList<>();
            if(droits.containsKey(nomEntite)){
               ops.addAll(droits.get(nomEntite));
            }
            setCanNew(ops.contains(creation));
            setCanEdit(ops.contains(modification));
            setCanDelete(ops.contains(archivage));
            setCanSeeHistorique(false);
         }
      }

      //applyDroitsOnListe();
      if(sessionScope.containsKey("Anonyme") && (Boolean) sessionScope.get("Anonyme")){
         isAnonyme = true;
      }else{
         isAnonyme = false;
      }
   }

   /**
    * Méthode qui ferme la fiche : seule la liste sera visible.
    * @param event onClose de la fiche.
    */
   public void onClose$winPanel(final Event event){
      Events.getRealOrigin((ForwardEvent) event).stopPropagation();
      if(getObjectTabController() != null){
         getObjectTabController().switchToOnlyListeMode();
      }
      if(validateC.isVisible()){
         onClick$revertC();
      }else if(createC.isVisible()){
         onClick$cancelC();
      }
   }

   public boolean isCanEdit(){
      return canEdit;
   }

   public void setCanEdit(final boolean cEdit){
      this.canEdit = cEdit;
   }

   public boolean isCanDelete(){
      return canDelete;
   }

   public void setCanDelete(final boolean cDelete){
      this.canDelete = cDelete;
   }

   public boolean isCanNew(){
      return canNew;
   }

   public void setCanNew(final boolean cNew){
      this.canNew = cNew;
   }

   @Override
   public boolean isAnonyme(){
      return isAnonyme;
   }

   @Override
   public void setAnonyme(final boolean isAno){
      this.isAnonyme = isAno;
   }

   public boolean isCanSeeHistorique(){
      return canSeeHistorique;
   }

   public void setCanSeeHistorique(final boolean canHistorique){
      this.canSeeHistorique = canHistorique;
   }

	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean _a) {
		this.admin = _a;
	}
}