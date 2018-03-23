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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Div;
import org.zkoss.zul.East;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.West;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.general.export.Export;

/**
 *
 * Abstract Controller d'un onglet affichant une entité de domaine.
 * Spécifie les méthodes de gestion de l'interaction de l'affichage de
 * la liste et de la fiche détaillée.
 * Date: 01/12/2009
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public abstract class AbstractObjectTabController extends AbstractController
{

   private static final long serialVersionUID = -3799945305452822008L;

   protected West listeRegion;
   protected Borderlayout mainBorder;
   protected East annoRegion;

   private String listWidthPercent = "20%";

   private Entite entiteTab = null;

   private Div staticDiv;
   private Div editDiv;
   private Div modifMultiDiv;
   private String staticZulPath;
   private String editZulPath;
   private String multiEditZulPath;
   private String listZulPath;
   private TKSelectObjectRenderer listRenderer;
   private final List<Banque> savedBanques = new ArrayList<>();

   private boolean isStaticEditMode = true;

   //protected boolean banqueOrgane;

   /*public boolean isBanqueOrgane() {
   	return banqueOrgane;
   }*/

   /*public void setBanqueOrgane(boolean banqueOrgane) {
   	this.banqueOrgane = banqueOrgane;
   }*/

   private final List<AbstractObjectTabController> referencingObjectControllers = new ArrayList<>();
   private final List<AbstractObjectTabController> referencedObjectControllers = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      //banqueOrgane=SessionUtils.isBanqueOrganeContexte(sessionScope);

      if(mainBorder != null){
         mainBorder.setHeight(getMainWindow().getPanelHeight() + "px");
      }

      if(getFicheCombine() != null){
         getFicheCombine().setObjectTabController(this);
         getFicheCombine().setNewObject();
      }

      // if (getListe() != null) {
      if(listeRegion != null && !listeRegion.getChildren().isEmpty()
      // listeRegion.getChildren().size() > 1
         && getListe() != null){
         getListe().setObjectTabController(this);
         getListe().applyDroitsOnListe();
      }

      initLinkedControllers();
   }

   /**
    * Renvoie true si une ou plusieurs tables d'annotations sont dessinées ou seront 
    * dessinées (si en toutes collections).
    * Utilisée pour afficher le bouton d'export Adv (filtre annotations)
    * @return
    */
   public boolean hasAnnoTables(){

      for(final TableAnnotation tb : ManagerLocator.getTableAnnotationManager()
         .findByBanquesManager(SessionUtils.getSelectedBanques(sessionScope), true)){
         if(tb.getEntite().equals(getEntiteTab())){
            return true;
         }
      }
      return false;
   }

   /**
    * Dessine le contenu de la region liste. Permet l'injection 
    * de contexte.
    */
   public void drawListe(){
      Components.removeAllChildren(listeRegion);

      //		Caption caption = new Caption();
      //		caption.setHflex("min");
      //		caption.setSclass("mainCaption");
      //		Div capDiv = new Div();
      //		capDiv.setSclass("captionButtons");
      //		caption.appendChild(capDiv);
      //		Button b1 = new Button("b1");
      //		b1.setSclass("captionButton");
      //		capDiv.appendChild(b1);
      //		Button b2 = new Button("b2");
      //		b2.setSclass("captionButton");
      //		capDiv.appendChild(b2);
      //		Button b3 = new Button("b3");
      //		b3.setSclass("captionButton");
      //		capDiv.appendChild(b3);
      //		Textbox tb = new Textbox();
      //		tb.setCols(3);
      //		// tb.setSclass("captionButton");
      //		capDiv.appendChild(tb);
      //		listeRegion.appendChild(caption);

      Map<String, Object> params = null;
      if(getListRenderer() != null){
         params = new HashMap<>();
         params.put("renderer", getListRenderer());
      }
      Executions.createComponents(getListZulPath(), listeRegion, params);
      getListe().setObjectTabController(this);
      getListe().applyDroitsOnListe();

      getListe().updateListResultsLabel(getListe().getListObjects().size());
   }

   /**
    * Dessine le formulaire static à partir
    * d'un objet vide.
    */
   public void initFicheStatic(){
      // Injection des contextes
      populateFicheStatic();

      if(getFicheStatic() != null){
         getFicheStatic().setObjectTabController(this);
         getFicheStatic().setNewObject();
      }
   }

   public void setEntiteTab(final Entite eTab){
      this.entiteTab = eTab;
   }

   public Entite getEntiteTab(){
      return entiteTab;
   }

   public void setStaticDiv(final Div sDiv){
      this.staticDiv = sDiv;
   }

   public Div getStaticDiv(){
      return staticDiv;
   }

   public void setEditDiv(final Div eDiv){
      this.editDiv = eDiv;
   }

   public Div getEditDiv(){
      return editDiv;
   }

   public void setModifMultiDiv(final Div mDiv){
      this.modifMultiDiv = mDiv;
   }

   public void setEditZulPath(final String ePath){
      this.editZulPath = ePath;
   }

   public String getEditZulPath(){
      return editZulPath;
   }

   public String getStaticZulPath(){
      return staticZulPath;
   }

   public void setStaticZulPath(final String staticZulPath){
      this.staticZulPath = staticZulPath;
   }

   public void setMultiEditZulPath(final String muEP){
      this.multiEditZulPath = muEP;
   }

   public String getListZulPath(){
      return listZulPath;
   }

   public void setListZulPath(final String lzp){
      this.listZulPath = lzp;
   }

   public TKSelectObjectRenderer getListRenderer(){
      return listRenderer;
   }

   public void setListRenderer(final TKSelectObjectRenderer lR){
      this.listRenderer = lR;
   }

   public boolean isStaticEditMode(){
      return isStaticEditMode;
   }

   public void setStaticEditMode(final boolean isMode){
      this.isStaticEditMode = isMode;
   }

   public West getListeRegion(){
      return listeRegion;
   }

   @Override
   public MainWindow getMainWindow(){
      return (MainWindow) page.getFellow("mainWin").getAttributeOrFellow("mainWin$composer", true);
   }

   /**
    * Méthode permettant de passer en mode "liste" : la liste des objets
    * apparait sur la gauche avec la fiche sur la droite.
    */
   public void switchToListeMode(){
      listeRegion.setOpen(true);
   }

   /**
    * Méthode permettant de passer en mode "liste" : seule la liste 
    * est visible.
    */
   public void switchToOnlyListeMode(){
      listeRegion.setWidth("100%");
      listeRegion.setOpen(true);
      // if (annoRegion != null && annoRegion.isVisible()) {
      // annoRegion.setOpen(false);
      // }
   }

   /**
    * Detruit les composants du mode edition/creation.
    */
   public void clearEditDiv(){
      Components.removeAllChildren(getEditDiv());
   }

   /**
    * Méthode permettant de passer en mode "fiche & liste" : liste 
    * et la fiche sont visibles.
    * Détache la fiche édition/création
    */
   public void switchToFicheAndListeMode(){
      if(listeRegion.getWidth().equals("100%")){
         listeRegion.setWidth(listWidthPercent);

         // on n'ouvre les annotations que si la liste était
         // en mode complet. Sinon, on laisse le panel des
         // annotations comme le veux l'utilisateur
         if(annoRegion != null && !annoRegion.isOpen() && annoRegion.isVisible()){
            annoRegion.setOpen(true);
         }
      }
      listeRegion.setOpen(true);

      if(isStaticEditMode()){
         showStatic(true);
         clearEditDiv();
      }
   }

   /**
    * Méthode permettant de passer en mode "fiche" : seule la fiche de
    * l'objet est visible.
    * Détache la fiche édition et sélectionne l'objet dans la liste si
    * présent.
    * @param obj à passer à la fiche
    */
   public void switchToFicheStaticMode(final TKdataObject obj){
      listeRegion.setOpen(false);
      // astuce pour éviter que le navigateur s'emmele et fasse chevaucher
      // les regions
      if(!listeRegion.isOpen()){
         if(annoRegion.isVisible()){
            annoRegion.setOpen(annoRegion.isOpen());
         }
      }

      // Injection des contextes
      populateFicheStatic();

      showStatic(true);
      clearEditDiv();

      if(getFicheStatic() != null){
         getFicheStatic().setObject(obj);
      }else{
         getFicheCombine().setObject(obj);
      }

      if(getListe() != null){
         // si la liste cachée contient l'objet, on le sélectionne
         if(getListe().getListObjects().contains(obj)){
            getListe().changeCurrentObject(obj);
         }else{
            getListe().deselectRow();
         }
         getListe().switchToEditMode(false);
      }
   }

   /**
    * Détruit la fiche edition/création et rend visible la fiche statique 
    * en la peuplant avec un nouvel objet vide.
    */
   public void clearStaticFiche(){

      if(getFicheStatic() != null){

         showStatic(true);
         clearEditDiv();

         getFicheStatic().setNewObject();
      }else if(getFicheCombine() != null){
         getFicheCombine().clearData();
      }
   }

   /**
    * Switch la visibilité des divs contenant les fiches statiques et 
    * crétaion/édition.
    */
   public void showStatic(final boolean showStatic){
      staticDiv.setVisible(showStatic);
      editDiv.setVisible(!showStatic);
      if(modifMultiDiv != null){
         modifMultiDiv.setVisible(!showStatic);
      }
   }

   /**
    * Méthode permettant de passer en mode de sélection : seule la liste 
    * est affichée. Elle est en mode "select".
    * @param path
    */
   public void switchToSelectMode(final String path, final List<Banque> banques){
      switchToOnlyListeMode();
      getListeRegion().setCollapsible(false);
      getListe().setPath(path);
      getListe().switchToSelectMode();
      // on bloque la liste des banques
      getMainWindow().disableBanqueListbox(true);

      // lors du passage en mode de sélection, on peut fournir une
      // liste des banques à interroger, qu'on va mettre dans la
      // session
      savedBanques.clear();
      if(banques != null && !banques.isEmpty()){
         savedBanques.addAll(SessionUtils.getSelectedBanques(sessionScope));

         if(sessionScope.containsKey("Banque")){
            sessionScope.put("ToutesCollections", banques);
            sessionScope.remove("Banque");
         }else{
            sessionScope.put("ToutesCollections", banques);
         }
         getListe().getListObjectsRenderer().setTtesCollections(true);
      }
   }

   /**
    * Méthode permettant de passer en mode de normal apres un
    * switchToSelectMode.
    */
   public void switchToNormalMode(){
      switchToOnlyListeMode();
      getListeRegion().setCollapsible(true);
      getListe().setPath("");
      getListe().switchToListMode();
      // getListe().clearList();
      // on débloque la liste des banques
      getMainWindow().disableBanqueListbox(false);

      // si une liste de banques avait été fournie, on va remettre
      // celle qui était précédemment sélectionnée
      if(!savedBanques.isEmpty()){
         if(savedBanques.size() > 1){
            sessionScope.put("ToutesCollections", savedBanques);
         }else{
            sessionScope.put("Banque", savedBanques.get(0));
            sessionScope.remove("ToutesCollections");
         }
         getListe().getListObjectsRenderer().setTtesCollections(savedBanques.size() > 1);

      }
      savedBanques.clear();
   }

   /**
    * Méthode permettant de passer en mode "création", collapse la liste
    * et dessine la fiche édition/creation avec un objet vide.
    */
   public void switchToCreateMode(final TKdataObject parent){

      // Injection des contextes
      populateFicheEdit();

      getFicheEdit().setObjectTabController(this);
      getFicheEdit().setNewObject();
      getFicheEdit().setParentObject(parent);
      getFicheEdit().switchToCreateMode();

      if(canUpdateAnnotation()){
         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         if(null != getFicheAnnotation()){
            getFicheAnnotation().switchToStaticOrEditMode(false, false);
            //getFicheAnnotation().showButtonsBar(false);
         }

         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         if(null != getFicheAnnotationInline()){
            getFicheAnnotationInline().switchToStaticOrEditMode(false, false);
         }
      }

      if(!annoRegion.isOpen() && annoRegion.isVisible()){
         annoRegion.setOpen(true);
      }

      getListeRegion().setOpen(false);
      if(getListe() != null){
         getListe().switchToEditMode(true);
      }

      showStatic(false);
   }

   /**
    * Méthode permettant de passer en mode "édition", dessine la fiche 
    * édition avec mapping vers l'objet passé en paramètre. 
    * Passe la fiche annotation en edition avec perte de controle des 
    * boutons de la fiche annotation.
    * @param obj edité.
    */
   public void switchToEditMode(final TKdataObject obj){
      // Injection des contextes
      populateFicheEdit();
      getFicheEdit().setObjectTabController(this);
      getFicheEdit().setObject(((TKAnnotableObject) obj).clone());
      getFicheEdit().switchToEditMode();

      if(canUpdateAnnotation()){
         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         if(null != getFicheAnnotation()){
            getFicheAnnotation().switchToStaticOrEditMode(false, false);
         }

         /**
          * ANNOTATION INLINE - Bêta
          *
          * @since 2.2.0
          */
         if(null != getFicheAnnotationInline()){
            getFicheAnnotationInline().switchToStaticOrEditMode(false, false);
         }
         //getFicheAnnotation().showButtonsBar(false);
      }

      if(!annoRegion.isOpen() && annoRegion.isVisible()){
         annoRegion.setOpen(true);
      }

      showStatic(false);

      getListeRegion().setOpen(false);
      if(getListe() != null){
         getListe().switchToEditMode(true);
      }
   }

   /**
    * Instruction recu quand la création est terminée.
    * Enregistre l'incrémentation de la numérotation
    * Detache le contenu de la div édition.
    * Passe la fiche annotation en statique.
    */
   public void onCreateDone(){

      if(getFicheEdit() != null && //Enregistre la numerotation
         getFicheEdit().getCurrentNumerotation() != null){
         ManagerLocator.getNumerotationManager().updateObjectManager(getFicheEdit().getCurrentNumerotation(),
            getFicheEdit().getCurrentNumerotation().getBanque(), getFicheEdit().getCurrentNumerotation().getEntite());
      }

      switchToOnlyListeMode();
      clearStaticFiche();
      //orderAnnotationDraw(false);
      getFicheAnnotation().switchToStaticOrEditMode(true, true);
      //getFicheAnnotation().showButtonsBar(true);
   }

   /**
    * Instruction recu quand la création est terminée par un
    * cancel.
    * Detache le contenu de la div édition.
    * Envoie le revert a la fiche annotation qui passe en statique.
    */
   public void onCancel(){
      switchToOnlyListeMode();
      clearStaticFiche();
      if(getFicheAnnotation() != null){
         getFicheAnnotation().onClick$revert();
         getFicheAnnotation().showButtonsBar(true);
      }
   }

   /**
    * Instruction recu quand l'édition est terminée.
    * Detache le contenu de la div édition.
    * Passe la fiche annotation en statique.
    */
   public void onEditDone(final TKdataObject obj){
      switchToFicheStaticMode(obj);
      getFicheAnnotation().switchToStaticOrEditMode(true, true);
      //getFicheAnnotation().showButtonsBar(true);
   }

   /**
    * Instruction recu quand l'édition est terminée par un revert.
    * Detache le contenu de la div édition.
    * Envoie le revert a la fiche annotation qui passe en statique.
    */
   public void onRevert(){
      showStatic(true);
      clearEditDiv();
      if(getFicheAnnotation() != null){
         getFicheAnnotation().onClick$revert();
         getFicheAnnotation().showButtonsBar(true);
      }
      if(getListe() != null){
         getListe().switchToEditMode(false);
      }
   }

   /**
    * Efface toutes les informations présentes dans la page : liste
    * d'éléments, fiche de présentation.
    */
   public void clearListeAndFiche(){
      getListe().clearList();
      clearStaticFiche();
   }

   /**
    * Commandes envoyées au controller ficheAnnotation pour
    * le dessiner.
    * @param isMulti si dessin en modification multiple
    */
   public void orderAnnotationDraw(final boolean isMulti){
      // passe l'entite au controller
      getFicheAnnotation().setEntite(entiteTab);
      getFicheAnnotation().setObjectTabController(this);

      // ordonne le dessin du contenu en mode normal sauf si 
      // toutes collections
      final List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
      if(banks.size() == 1){
         getFicheAnnotation().setBankUsedToDrawChamps(banks.get(0));
         getFicheAnnotation().drawAnnotationPanelContent(isMulti, false);
      }else{
         getFicheAnnotation().setBankUsedToDrawChamps(null);
         getFicheAnnotation().drawAnnotationPanelContent(false, false);
      }

      setAnnoRegionVisible();
   }

   /**
    * Affiche ou non la region east annotations du borderlayout en 
    * fonction du contenu de la fiche annotation.
    */
   public void setAnnoRegionVisible(){
      // ferme le panel si vide
      if(!getFicheAnnotation().getAnyChampAnnoDrawn()){
         annoRegion.setOpen(false);
         annoRegion.setVisible(false);
         listWidthPercent = "40%";
      }else{
         annoRegion.setVisible(true);
         listWidthPercent = "20%";
      }
   }

   /**
    * Attache le composant de modification multiple à la page et passe
    * la liste d'objets participant à la modification multiple.
    * @param objsToEdit
    */
   
   public void switchToModifMultiMode(final List<? extends Object> objsToEdit){

      Components.removeAllChildren(modifMultiDiv);
      Executions.createComponents(multiEditZulPath, modifMultiDiv, null);

      getFicheModifMulti().setObjsToEdit(objsToEdit);

      switchToFicheAndListeMode();
      staticDiv.setVisible(false);
      editDiv.setVisible(false);
      modifMultiDiv.setVisible(true);

      // dessine les annotations en mode modif multiple si une banque
      if(sessionScope.containsKey("Banque")){
         // passe les annotations en modif multiple
         getFicheAnnotation().setMultiObjs((List<TKAnnotableObject>) objsToEdit);
         orderAnnotationDraw(true);
         getFicheAnnotation().showButtonsBar(false);
      }else{ // toutes collections
         getFicheAnnotation().cleanPanel();
         annoRegion.setOpen(false);
      }

   }

   /**
    * Instruction recu quand la modification multiple est terminée.
    * Passe en mode liste. Redessine la fiche annotation.
    * Detache le contenu de la div modification multiple.
    */
   public void onMultiModifDone(){
      orderAnnotationDraw(false);
      Components.removeAllChildren(modifMultiDiv);
      showStatic(true);
      switchToOnlyListeMode();
   }

   /**
    * Reset le tab. Methode appelée lors d'un changement de Banque.
    */
   public void reset(){
      // droits
      getListe().drawActionsButtons();
      getListe().applyDroitsOnListe();
      getFicheStatic().applyDroitsOnFiche();

      clearListeAndFiche();
      clearStaticFiche();
      getListe().initObjectsBox();
      switchToOnlyListeMode();
      orderAnnotationDraw(false);
   }

   /**
    * Met à jour un tab, cad le contenu de la liste et éventuellement 
    * l'objet en fiche statique à partir d'une liste d'enfants dont 
    * le parent a subi une modification.
    * @param children
    */
   public void updateReferencedObjects(final List<TKdataObject> children){
      if(!getReferencedObjectsControllers().isEmpty() && children != null){
         for(int i = 0; i < getReferencedObjectsControllers().size(); i++){
            final TKdataObject selected =
               getReferencedObjectsControllers().get(i).getListe().updateGridListChildrenObjectsFromOtherPage(children, false);

            // à jour la fiche statique avec le selected child
            if(selected != null && !getReferencedObjectsControllers().get(i).isInOnlyListMode()){
               getReferencedObjectsControllers().get(i).switchToFicheStaticMode(selected);
            }
         }
      }
   }

   /**
    * Met à jour les tabs contenant les objets 'enfants' des objets du Tab actuel, 
    * cad le contenu de la liste et reset l'onglet 
    * en liste mode avec effacement de la fiche statique en cours.
    * La maj du binding  n'est pas appliquée dans le cas dérivés de dérivés car 
    * la liste est la même pour l'objet référencé et l'objet référencant.
    * @param objs référencé par entite
    * @param delete delete si deletion obj de la liste
    */
   public void updateChildrenReferences(final Map<Entite, List<Integer>> objs, final boolean delete){

      if(!getReferencedObjectsControllers().isEmpty() && objs != null){
         for(int i = 0; i < getReferencedObjectsControllers().size(); i++){
            getReferencedObjectsControllers().get(i).getListe().updateGridByIds(
               objs.get(getReferencedObjectsControllers().get(i).getEntiteTab()), delete,
               !getReferencedObjectsControllers().get(i).equals(this));

            if(!getReferencedObjectsControllers().get(i).equals(this)){
               // si on a supprimé le selected alors
               // passe en liste
               getReferencedObjectsControllers().get(i).clearStaticFiche();
               getReferencedObjectsControllers().get(i).switchToOnlyListeMode();
            }
         }
      }
   }

   /**
    * Met à jour les tabs contenant les objets 'parents' des objets du Tab actuel, 
    * cad le contenu de la liste et reset l'onglet 
    * en liste mode avec effacement de la fiche statique en cours.
    * La maj du binding  n'est pas appliquée dans le cas dérivés de dérivés car 
    * la liste est la même pour l'objet référencé et l'objet référencant.
    * @param objs référencant par entite
    */
   public void updateParentsReferences(final Map<Entite, List<Integer>> objs){

      if(!getReferencingObjectControllers().isEmpty() && objs != null){
         for(int i = 0; i < getReferencingObjectControllers().size(); i++){
            getReferencingObjectControllers().get(i).getListe().updateGridByIds(
               objs.get(getReferencingObjectControllers().get(i).getEntiteTab()), false,
               !getReferencingObjectControllers().get(i).equals(this));

            if(!getReferencingObjectControllers().get(i).equals(this)){
               // si on a supprimé le selected alors
               // passe en liste
               getReferencingObjectControllers().get(i).clearStaticFiche();
               getReferencingObjectControllers().get(i).switchToOnlyListeMode();
            }
         }
      }
   }

   /**
    * Reset tous les tabs référencés.
    * Utile lors d'une batch deletion
    */
   public void resetReferencedObjects(){
      if(!getReferencedObjectsControllers().isEmpty()){
         for(int i = 0; i < getReferencedObjectsControllers().size(); i++){
            getReferencedObjectsControllers().get(i).reset();
         }
      }
   }

   
   public boolean canUpdateAnnotation(){
      Boolean admin = false;
      boolean edit = false;
      if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin => boutons cliquables
      if(admin){
         edit = true;
      }else{
         // on extrait les OperationTypes de la base
         final OperationType annotation =
            ManagerLocator.getOperationTypeManager().findByNomLikeManager("Annotation", true).get(0);

         Hashtable<String, List<OperationType>> droits = new Hashtable<>();

         if(sessionScope.containsKey("Droits")){
            // on extrait les droits de l'utilisateur
            droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

            final List<OperationType> ops = droits.get(getEntiteTab().getNom());
            edit = ops.contains(annotation);
         }
      }

      return edit;
   }

   /***************** abstract methods **************************/
   /**
    * Charge un TK data objet à partir de son id
    * @param id
    * @return TK data object
    */
   public abstract TKdataObject loadById(Integer id);

   /**
    * Recupere le controller du composant representant la fiche 
    * d'affichage statique associee.
    */
   public abstract AbstractFicheStaticController getFicheStatic();

   /**
    * Recupere le controller du composant representant la fiche 
    * d'affichage statique associee.
    */
   public abstract AbstractFicheEditController getFicheEdit();

   /**
    * Recupere le controller du composant representant la liste associee.
    */
   public abstract AbstractListeController2 getListe();

   /**
    * Renvoie le controller de la fiche annotation associée.
    * @return FicheAnntotation controller
    */
   public abstract FicheAnnotation getFicheAnnotation();

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    */
   public abstract FicheAnnotationInline getFicheAnnotationInline();

   /**
    * Renvoie le controller de la fiche modification multiple associée.
    * @return AbstractFicheModifMultiController controller
    */
   public abstract AbstractFicheModifMultiController getFicheModifMulti();

   public abstract AbstractFicheCombineController getFicheCombine();

   /**
    * Peuplent après composition du componant les listes de controllers 
    * associés referencing and referenced 
    * controllers.
    * @since 2.0.12
    */
   public void initLinkedControllers(){}

   /**
    * Recupere la liste de controllers des objets qui référence l'objet 
    * dont la creation modification est controlée par le controller courant.
    * @return la liste de controllers.
    */
   public List<AbstractObjectTabController> getReferencingObjectControllers(){
      return referencingObjectControllers;
   }

   /**
    * Recupere la liste de controllers des objets qui sont référencés 
    * par l'objet dont la creation modification est controlée par 
    * le controller courant.
    * @return la liste de controllers.
    */
   public List<AbstractObjectTabController> getReferencedObjectsControllers(){
      return referencedObjectControllers;
   }

   public boolean isInOnlyListMode(){
      return listeRegion.getWidth().equals("100%");
   }

   /** Methodes permettant l'injection des contextes **/

   public void populateFicheStatic(){
      if(isStaticEditMode){
         // Posera problème lors implementation des contextes
         if(getStaticDiv().getChildren().isEmpty()){
            // Components.removeAllChildren(getStaticDiv());
            Executions.createComponents(getStaticZulPath(), getStaticDiv(), null);
            getFicheStatic().setObjectTabController(this);
         }
      }
   }

   public void populateFicheEdit(){
      if(isStaticEditMode){
         clearEditDiv();
         Executions.createComponents(getEditZulPath(), getEditDiv(), null);
      }
   }

   public void postIdsToOtherEntiteTab(final String entiteNom, final List<Integer> ids){
      AbstractObjectTabController tabController = null;

      // on récupère le controller correspondant aux objets
      // à extraire
      if(entiteNom.equals("Patient")){
         tabController = PatientController.backToMe(getMainWindow(), page);
      }else if(entiteNom.equals("Prelevement")){
         tabController = PrelevementController.backToMe(getMainWindow(), page);
      }else if(entiteNom.equals("Echantillon")){
         tabController = EchantillonController.backToMe(getMainWindow(), page);
      }else if(entiteNom.equals("ProdDerive")){
         tabController = ProdDeriveController.backToMe(getMainWindow(), page);
      }else if(entiteNom.equals("Cession")){
         tabController = CessionController.backToMe(getMainWindow(), page);
      }

      // on envoie les objets dans la liste
      if(tabController != null){
         tabController.getListe().setCurrentObject(null);
         tabController.clearStaticFiche();
         tabController.switchToOnlyListeMode();

         // clean doublons by hashSet passage
         final Set<Integer> idsSet = new LinkedHashSet<>(ids);
         ids.clear();
         ids.addAll(idsSet);

         if(ids.size() > 500){
            Clients.clearBusy();
            tabController.getListe().callResultatsModale(ids);
         }else{
            tabController.getListe().setResultatsIds(ids);
            tabController.getListe().onShowResults();
            //updateListContent(resultats);
         }
      }
   }

   /**
    * Methode generique des exports catalogue
    * @param params args passés à l'appel de la méthode
    * @version 2.1 
    */
   
   public void onLaterExportCatalogue(final List<Integer> ids, final short catalogId, final Integer entiteId, final boolean csv,
      final Map<String, ?> params){
      try{
         final Class<?> exportThread = Class.forName(SessionUtils.getDatabasePathClass());
         final Constructor<?> constr = exportThread.getConstructor(Desktop.class, int.class, List.class, List.class,
            boolean.class, short.class, Utilisateur.class, List.class, HtmlMacroComponent.class, Map.class);
         final Object o = constr.newInstance(desktop, entiteId != null ? entiteId : getEntiteTab().getEntiteId(), ids,
            SessionUtils.getSelectedBanques(sessionScope), isAnonyme(), catalogId, SessionUtils.getLoggedUser(sessionScope),
            new ArrayList<Integer>(), callProgressBar(), params);
         final Method method = exportThread.getMethod("start");

         ((Export) o).setContexte(SessionUtils.getSelectedBanques(sessionScope).get(0).getContexte());

         ((Export) o).setCsv(csv);

         // put into session
         if(!desktop.hasAttribute("threads")){
            desktop.setAttribute("threads", new ArrayList<Export>());
         }
         ((List<Export>) desktop.getAttribute("threads")).add((Export) o);

         method.invoke(o);

         // getRestrictedTableIds().clear();
      }catch(final Exception e){
         e.printStackTrace();
      }
   }

   public List<? extends Object> getChildrenObjects(final TKdataObject obj){
      return null;
   }

   public Map<Entite, List<Integer>> getChildrenObjectsIds(final List<Integer> ids){
      return null;
   }

   public Map<Entite, List<Integer>> getParentsObjectsIds(final List<Integer> ids){
      return null;
   }

   public void onBlur$pageSizeBox(final Event e){
      if(getListe() != null){
         getListe().getObjectsListGrid().setAutopaging(false);
         getListe().getObjectsListGrid().setMold("paging");
         final Integer value = ((Intbox) ((ForwardEvent) e).getOrigin().getTarget()).getValue();
         if(value != null){
            getListe().getObjectsListGrid().setPageSize(value);
         }
      }
   }

   /**
    * Gestion d'un scan de terminale renvoyé par un scanner full-rack
    * Par défaut, ne fait rien, sauf si methode surchargée dans tabs concernés (Echantillon, dérivé, stockage)
    * @since 2.1
    * @param sT
    */
   public void handleScanTerminale(final ScanTerminale sT){}
}
