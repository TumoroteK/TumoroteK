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
package fr.aphp.tumorotek.action.utilisateur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListeUtilisateur extends AbstractListeController2
{

   private static final long serialVersionUID = 295074764171509054L;

   private List<Utilisateur> listObjects = new ArrayList<>();
   private List<Plateforme> pfs = new ArrayList<>();
   boolean findArchive = false;

   private Textbox findLoginBox;
   private Checkbox comptesActifsBox;
   private Checkbox availPfsBox;
   private Column pfCol;

   private UtilisateurRowRenderer utilisateurRenderer = new UtilisateurRowRenderer();

   public List<Plateforme> getPfs(){
      return pfs;
   }

   public void setPfs(final List<Plateforme> pfs){
      this.pfs = pfs;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      modificationItem = new Menuitem();
      // plateforme courante par défaut pour filtrer les 
      // utilisateurs
      super.doAfterCompose(comp);

      final int height = getMainWindow().getListPanelHeight() + 142;
      listPanel.setHeight(height + "px");

      utilisateurRenderer.setUser(SessionUtils.getLoggedUser(sessionScope));
   }

   @Override
   public List<Utilisateur> getListObjects(){
      return this.listObjects;
   }

   
   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects.clear();
      this.listObjects.addAll((List<Utilisateur>) objs);
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(pos != null){
         getListObjects().add(pos.intValue(), (Utilisateur) obj);
      }else{
         getListObjects().add((Utilisateur) obj);
      }
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public void initObjectsBox(){
      if(pfs.isEmpty()){
         pfs.add(SessionUtils.getPlateforme(sessionScope));
      }
      final List<Utilisateur> utilisateurs = ManagerLocator.getUtilisateurManager().findByArchiveManager(findArchive, pfs);

      listObjects = utilisateurs;
      setCurrentRow(null);
      setCurrentObject(null);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   public void onCheck$availPfsBox(){
      pfs.clear();
      if(availPfsBox.isChecked()){
         if(SessionUtils.getLoggedUser(sessionScope).isSuperAdmin()){
            pfs.addAll(ManagerLocator.getPlateformeManager().findAllObjectsManager());
         }else{
            pfs.addAll(ManagerLocator.getUtilisateurManager().getPlateformesManager(SessionUtils.getLoggedUser(sessionScope)));
         }
         pfCol.setVisible(true);
      }else{
         pfs.add(SessionUtils.getPlateforme(sessionScope));
         pfCol.setVisible(false);
      }

      initObjectsBox();
   }

   public void onCheck$comptesActifsBox(){
      List<Utilisateur> utilisateurs = new ArrayList<>();

      findArchive = !comptesActifsBox.isChecked();

      utilisateurs = ManagerLocator.getUtilisateurManager().findByArchiveManager(false, pfs);
      if(findArchive){
         utilisateurs.addAll(ManagerLocator.getUtilisateurManager().findByArchiveManager(true, pfs));
         Collections.sort(utilisateurs);
      }

      listObjects = utilisateurs;
      setCurrentRow(null);
      setCurrentObject(null);

      getObjectTabController().getFicheCombine().clearData();

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   public void onClick$viewObject(final Event event){
      // déselection de la ligne courante
      deselectRow();

      selectRowAndDisplayObject(getRow((ForwardEvent) event),
         (TKdataObject) AbstractListeController2.getBindingData((ForwardEvent) event, false));
   }

   @Override
   public void onClickObject(final Event event){
      // déselection de la ligne courante
      deselectRow();

      // sélection de la nouvelle ligne
      selectRow(getRow((ForwardEvent) event), (TKdataObject) event.getData());

      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();

      // on envoie l'échantillon à la fiche
      final Utilisateur edit = ((Utilisateur) getCurrentObject()).clone();
      getFiche().setObject(edit);
      getFiche().switchToStaticMode();
   }

   @Override
   public void onClick$addNew(final Event event) throws Exception{
      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();
      getFiche().setNewObject();
      getFiche().switchToCreateMode();
   }

   @Override
   public void updateMultiObjectsGridListFromOtherPage(final List<TKdataObject> objects){
      for(int i = 0; i < objects.size(); i++){
         final Object obj = objects.get(i);
         updateObjectGridListFromOtherPage(obj, false);
      }
   }

   /**
    * Mets à jour l'objet sélectionné de la liste.
    * @param objet
    */
   public void updateCurrentObject(){
      // on vérifie que la liste a bien un objet sélectionné
      if(getCurrentObject() != null){
         // on passe en mode fiche & liste
         getObjectTabController().switchToFicheAndListeMode();

         // on envoie l'échantillon à la fiche
         final Utilisateur edit = ((Utilisateur) getCurrentObject()).clone();
         getFiche().setObject(edit);
         getFiche().switchToStaticMode();
      }
   }

   /**
    * Recupere le controller du composant representant la fiche associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche FicheUtilisateur
    */
   public AbstractFicheCombineController getFiche(){
      return getObjectTabController().getFicheCombine();
   }

   @Override
   public void applyDroitsOnListe(){
      boolean isAdminPF = false;
      if(sessionScope.containsKey("AdminPF")){
         isAdminPF = (Boolean) sessionScope.get("AdminPF");
      }

      // since 2.0.13
      if(isAdminPF){
         setCanNew(true);
      }else{
         setCanNew(false);
      }
      addNew.setDisabled(!isCanNew());
   }

   @Override
   public void disableToolBar(final boolean b){
      addNew.setDisabled(b || !isCanNew());
   }

   @Override
   public void addToSelectedObjects(final TKdataObject obj){}

   @Override
   public List<Integer> doFindObjects(){
      return null;
   }

   @Override
   public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
      return null;
   }

   @Override
   public List<? extends Object> getSelectedObjects(){
      return null;
   }

   @Override
   public void passListToSelected(){}

   @Override
   public void passSelectedToList(){}

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){}

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){}

   public UtilisateurRowRenderer getUtilisateurRenderer(){
      return utilisateurRenderer;
   }

   public void setUtilisateurRenderer(final UtilisateurRowRenderer uRenderer){
      this.utilisateurRenderer = uRenderer;
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      return null;
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }

   /**
    * Filtre les logins à partie de la valeur passé en paramètre.
    * Réactualise la liste
    * @since 2.0.10
    */
   @Override
   public void onClick$find(){

      if(findLoginBox.getValue() != null){

         final List<Utilisateur> utilisateurs =
            ManagerLocator.getUtilisateurManager().findByLoginAndArchiveManager(findLoginBox.getValue(), findArchive, pfs);

         listObjects = utilisateurs;
         setCurrentRow(null);
         setCurrentObject(null);

         getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
      }

   }
}
