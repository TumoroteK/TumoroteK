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
package fr.aphp.tumorotek.action.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Group;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller de la fiche Annotation associée à un objet.
 *
 * Dessine les champs et les commande simultanément pour
 * la recuperation des valeurs,
 * suppression, mise à jour ainsi que pour l'affichage en
 * mode static ou editable.
 * Date: 16/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class FicheAnnotation extends AbstractFicheController
{

   private static final long serialVersionUID = 1L;

   // Static Components
   protected Panel annoPanel;

   private Rows annoRows;

   private Button edit;

   private Button validate;

   private Button revert;

   // variables droits
   private boolean canEdit;

   // Objets Principaux
   protected TKAnnotableObject obj = null;

   protected Entite entite;

   private Banque bankUsedToDrawChamps = null;

   protected List<AnnotationComponent> champControllers = new ArrayList<>();

   public List<TableAnnotation> tables = new ArrayList<>();

   private final List<AnnotationValeur> valeursToCreateOrUpdate = new ArrayList<>();

   private final List<AnnotationValeur> valeursToDelete = new ArrayList<>();

   // modification multiple
   protected List<TKAnnotableObject> multiObjs;

   protected Boolean isMultipleMode = false;

   protected int tableNum = 0;

   private final List<Group> collapGroups = new ArrayList<>();

   private final List<Group> openedGroups = new ArrayList<>();

   protected boolean isStatic;

   private boolean showButtons;

   public void setObj(final TKAnnotableObject o){
      this.obj = o;

      // redessine si la banque de l'objet ne correspond pas
      // au composants d'annotation pour la banque actuelle
      // et repasse quoi qu'il arrive en mode normal
      // si multiple mode
      if((obj.getBanque() != null && !obj.getBanque().equals(bankUsedToDrawChamps)) || isMultipleMode){
         if(obj.getBanque() != null){
            bankUsedToDrawChamps = obj.getBanque();
         }
         drawAnnotationPanelContent(false, false);
         getObjectTabController().setAnnoRegionVisible();
      }

      updateAnnotationValues();
      // bouton edit visible si objet non null
      edit.setVisible(true);
      validate.setVisible(false);
      revert.setVisible(false);

      disableEdit(obj.listableObjectId() == null);
   }

   public void disableEdit(final boolean dis){
      edit.setDisabled(getObject().listableObjectId() == null || dis || !canEdit);
   }

   @Override
   public TKAnnotableObject getObject(){
      return obj;
   }

   /**
    * Medthode d'entree de l'entite. Lieu d'assignation des droits de
    * modification.
    * @param e entite.
    */
   public void setEntite(final Entite e){
      this.entite = e;
      // droits
      drawActionsButtons(e.getNom());
   }

   public Button getEdit(){
      return edit;
   }

   public List<AnnotationValeur> getValeursToCreateOrUpdate(){
      return valeursToCreateOrUpdate;
   }

   public List<AnnotationValeur> getValeursToDelete(){
      return valeursToDelete;
   }

   public void setMultiObjs(final List<TKAnnotableObject> mObjs){
      this.multiObjs = mObjs;
   }

   public List<? extends TKAnnotableObject> getMultiObjs(){
      return multiObjs;
   }

   public Boolean getIsMultipleMode(){
      return isMultipleMode;
   }

   public Rows getAnnoRows(){
      return annoRows;
   }

   public void setBankUsedToDrawChamps(final Banque cBanque){
      this.bankUsedToDrawChamps = cBanque;
   }

   public Banque getBankUsedToDrawChamps(){
      return bankUsedToDrawChamps;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      // specifie la hauteur du panel au max
      annoPanel.setHeight(getMainWindow().getAnnoPanelHeight() + "px");
   }

   /**
    * Dessine le contenu de la fiche annotation sous la forme
    * de group (table) contenant des composants AnnotationComponent.
    * @param multi true si fiche en modification multiple
    * @param cumulative true si on veut dessiner les champs annotations de
    * plusieurs banques sur la fiche (ex: patient en toutes collections).
    */
   public void drawAnnotationPanelContent(final boolean multi, final boolean cumulative){
      // efface le panel au besoin
      if(annoRows.hasGroup() && !cumulative){
         cleanPanel();
         tableNum = 0;
         this.tables.clear();
      }

      if(bankUsedToDrawChamps != null){
         // recupere les tables pour dessiner chaque groupe
         final List<TableAnnotation> tabs =
            ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(this.entite, bankUsedToDrawChamps);

         this.tables.addAll(tabs);

         Group annoTable = null;

         for(int i = 0; i < tabs.size(); i++){
            annoTable = drawTableAnnotationGroup(tabs.get(i), tableNum);
            annoRows.appendChild(annoTable);
            tableNum++;

            // si patient ou multiple mode, assigne une reference vers
            // la banque dans le groupe pour pouvoir la passer aux
            // AnnotationValeurs
            if(this.entite.getNom().equals("Patient") || multi){
               annoTable.setAttribute("bank", bankUsedToDrawChamps);
            }

            // couleur des Labels
            String colClass = null;
            if(tabs.get(i).getCatalogue() != null){
               if(tabs.get(i).getCatalogue().getNom().matches("INCa.*")){
                  colClass = "incaLabel";
               }
            }

            // dessine les champs
            final Iterator<ChampAnnotation> champsIt =
               ManagerLocator.getTableAnnotationManager().getChampAnnotationsManager(tabs.get(i)).iterator();
            while(champsIt.hasNext()){
               final Row newRow = new Row();
               newRow.appendChild(drawAnnotation(champsIt.next(), newRow, annoTable, multi, colClass));
               annoRows.appendChild(newRow);
            }
            collapGroups.add(annoTable);
         }
      }

      isMultipleMode = multi;
   }

   /**
    * Efface le contenu d'une fiche annotation, pour re-ecrire une nouvelle
    * fiche lors du changement de banque par exemple.
    * Re-initialise la liste des controllers des champs Annotation.
    */
   public void cleanPanel(){
      Components.removeAllChildren(annoRows);
      this.champControllers.clear();
   }

   /**
    * Renvoie true si au moins un champ annotation a été dessiné.
    * @return true/false
    */
   public boolean getAnyChampAnnoDrawn(){
      return annoRows.getFirstChild() != null;
   }

   /**
    * Dessine un groupe pour une table annotation.
    * Dessine les champs à l'intérieur du groupe.
    * Cette méthodes est appelée lors de la mise en place
    * de la page lorsque la banque a été selectionnée.
    * @param table
    * @param ordre
    * @return group
    */
   private Group drawTableAnnotationGroup(final TableAnnotation table, final Integer ordre){
      final Group tableGroup = new Group();
      tableGroup.setId("tableAnno" + ordre);
      tableGroup.setLabel(table.getNom());
      tableGroup.setOpen(false);
      tableGroup.setParent(annoRows);
      if(table.getCatalogue() != null){
         if(table.getCatalogue().getNom().contains("INCa")){
            tableGroup.setSclass("grpInca");
         }else if(table.getCatalogue().getNom().contains("PELICAN")){
            tableGroup.setSclass("grpPelican");
         }
      }

      return tableGroup;
   }

   /**
    * Dessine un composant ChampAnnotation afin de l'assigner dans une Row
    * sous le groupe TableAnnotation.
    * Cette méthodes est appelée lors de la mise en place
    * de la page lorsque la banque a été selectionnée.
    * Si modification multiple, le composant est déssiné différemment
    * et la liste des objets à modifier est passée.
    * @param chp
    * @param row
    * @param annoGroup
    * @param multi si Annotation en modif multiple.
    * @param colClass class pour definir coloration de l'anno label en association avec un catalogue
    * @return MacorComponent
    */
   private HtmlMacroComponent drawAnnotation(final ChampAnnotation chp, final Row row, final Group annoGroup, final boolean multi,
      final String colClass){
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("annoComp", false).newInstance(page, null);
      row.setAlign("left");
      ua.setParent(row);
      ua.setId("chp_" + chp.getId());
      ua.applyProperties();
      ua.setHflex("1");
      ua.afterCompose();
      ((AnnotationComponent) ua.getFellow("annoDiv").getAttributeOrFellow("annoDiv$composer", true)).setChamp(chp, multi);
      ((AnnotationComponent) ua.getFellow("annoDiv").getAttributeOrFellow("annoDiv$composer", true)).setTableComponent(annoGroup);
      ((AnnotationComponent) ua.getFellow("annoDiv").getAttributeOrFellow("annoDiv$composer", true)).setColClass(colClass);

      // ajoute le controller a la liste de references
      this.champControllers.add((AnnotationComponent) ua.getFellow("annoDiv").getAttributeOrFellow("annoDiv$composer", true));

      // passe la liste d'objets en modification multiple
      if(multi){
         ((AnnotationComponent) ua.getFellow("annoDiv").getAttributeOrFellow("annoDiv$composer", true))
            .setMultiObjs(this.multiObjs);
      }

      return ua;
   }

   /**
    * Met à jour les valeurs affichées pour les champs annotation.
    */
   public void updateAnnotationValues(){
      // update les champs
      if(this.obj.listableObjectId() != null){
         for(int i = 0; i < champControllers.size(); i++){
            this.champControllers.get(i).setValeurs(ManagerLocator.getAnnotationValeurManager()
               .findByChampAndObjetManager(this.champControllers.get(i).getChamp(), this.obj));
         }
      }else{ // nettoie toutes les valeurs d'annotations
         for(int i = 0; i < champControllers.size(); i++){
            this.champControllers.get(i).setValeurs(new ArrayList<AnnotationValeur>());
         }
      }
   }

   /**
    * Met à jour directement les valeurs affichées pour les champs annotation.
    * @param vals liste valeurs à afficher.
    */
   public void setAnnotationValues(final List<AnnotationValeur> vals){
      if(vals != null){
         // pour chaque valeur, passe au bon controller si le
         // champ correspond
         for(int i = 0; i < vals.size(); i++){
            for(int j = 0; j < champControllers.size(); j++){
               if(champControllers.get(j).getChamp().equals(vals.get(i).getChampAnnotation())){
                  champControllers.get(j).getValeurs().add(vals.get(i));
                  break;
               }
            }
         }

         // actualise les controllers
         for(int j = 0; j < champControllers.size(); j++){
            champControllers.get(j).setValeurs(champControllers.get(j).getValeurs());
         }
      }
   }

   /**
    * Passe tous les champs en mode editable.
    */
   public void switchToStaticOrEditMode(final boolean is, final boolean show){
      isStatic = is;
      showButtons = show;
      collapseAllGroups();
      //Events.echoEvent("onLaterSwitch", self, null);
      onLaterSwitch();
   }

   public void onLaterSwitch(){

      ChampAnnotation chp;

      // update les champs
      Iterator<ChampAnnotation> champsIt;
      for(int i = 0; i < tables.size(); i++){
         champsIt = ManagerLocator.getTableAnnotationManager().getChampAnnotationsManager(tables.get(i)).iterator();
         while(champsIt.hasNext()){
            chp = champsIt.next();
            ((AnnotationComponent) annoRows.getFellow("chp_" + chp.getId()).getFellow("annoDiv")
               .getAttributeOrFellow("annoDiv$composer", true)).switchToStaticOrEditMode(isStatic);
         }
      }

      // boutons
      validate.setVisible(!isStatic);
      revert.setVisible(!isStatic);

      showButtonsBar(showButtons);
      edit.setVisible(isStatic);

      openCollapsedGroups();
   }

   /**
    * Enregistre la modification pour tous les champs.
    */
   public void recordModifications(){

      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      try{
         populateValeursActionLists(false, false);

         // actionne les opérations sur les listes
         ManagerLocator.getAnnotationValeurManager().removeAnnotationValeurListManager(this.valeursToDelete, filesToDelete);
         final List<AnnotationValeur> updated =
            ManagerLocator.getAnnotationValeurManager().createAnnotationValeurListManager(this.valeursToCreateOrUpdate, this.obj,
               SessionUtils.getLoggedUser(sessionScope), null, SessionUtils.getSystemBaseDir(), filesCreated, filesToDelete);

         // ajoute une operation si delete annos uniquement
         if(this.valeursToCreateOrUpdate.isEmpty() && !this.valeursToDelete.isEmpty()){
            CreateOrUpdateUtilities.createAssociateOperation(obj, ManagerLocator.getOperationManager(),
               ManagerLocator.getOperationTypeManager().findByNomLikeManager("Annotation", true).get(0),
               SessionUtils.getLoggedUser(sessionScope));
         }

         // met à jour les ids sur les objets manipulés par l'interface
         for(int i = 0; i < this.valeursToCreateOrUpdate.size(); i++){
            if(this.valeursToCreateOrUpdate.get(i).getAnnotationValeurId() == null){
               this.valeursToCreateOrUpdate.get(i).setAnnotationValeurId(updated.get(i).getAnnotationValeurId());
            }
            if(this.valeursToCreateOrUpdate.get(i).getFichier() != null){
               this.valeursToCreateOrUpdate.get(i).setFichier(updated.get(i).getFichier());
            }
         }

         clearValeursLists(true);
         tellComponentsToUpdate();
         for(final File f : filesToDelete){
            f.delete();
         }
      }catch(final Exception e){
         for(final File f : filesCreated){
            f.delete();
         }
         final boolean isValEx = e instanceof WrongValueException || e instanceof ValidationException;
         // revert ssi e n'est pas une erreur de validation syntaxique
         if(!isValEx){
            tellComponentsToRevert();
         }
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Ordonne aux différents composants Annotation la creation de valeur(s).
    * Prepare les listes dans lesquelles sont placées les valeurs
    * en vue d'actions.
    * @param forceNew si la valeur doit être forcément nouvelle (utile lors de
    * la creation simultanée de plusieurs objets).
    * @param multi si les valeurs d'annotations à récupérer ont été générées
    * par modification multiple.
    */
   public void populateValeursActionLists(final boolean forceNew, final boolean multi){

      clearValeursLists(false);

      List<AnnotationValeur> vals;
      // update les champs
      for(int i = 0; i < this.champControllers.size(); i++){
         if(!multi){
            // appelle le backUp avant le chargement des modifications
            // utile en cas de rollback transaction creation
            // simultanée avec le TKAnnotableObjet
            this.champControllers.get(i).backUpValeursBeforeEdition();

            // prepare les listes modification/creation
            this.champControllers.get(i).createOrUpdateAnnotationValeur(forceNew);
         }

         //recupere les listes
         this.valeursToCreateOrUpdate.addAll(this.champControllers.get(i).getValeursToCreateOrUpdate());

         vals = this.champControllers.get(i).getValeursToDelete();
         for(int j = 0; j < vals.size(); j++){
            if(vals.get(j).getAnnotationValeurId() != null){
               this.valeursToDelete.add(vals.get(j));
            }
         }

         //this.champControllers.get(i).clearValeurLists();
      }
   }

   /**
    * Reset les listes de transfert d'AnnotationValeurs.
    * @param deep reset chaque component
    */
   public void clearValeursLists(final boolean deep){
      //clear listes
      this.valeursToCreateOrUpdate.clear();
      this.valeursToDelete.clear();

      if(deep){
         for(int i = 0; i < this.champControllers.size(); i++){
            this.champControllers.get(i).clearValeurLists();
         }
      }
   }

   /*************** Event listeners. ******************/

   public void onClick$edit(){
      switchToStaticOrEditMode(false, true);
   }

   /**
    * Lance l'enregisterement des annotations pour l'objet de la fiche.
    * Oblige l'utilisateur à sauver l'obj référencant les annotations
    * si ce dernier est en cours de création.
    */
   public void onClick$validate(){
      validateComponents();
      if(this.obj.listableObjectId() != null){
         recordModifications();
         switchToStaticOrEditMode(true, true);
      }else{
         throw new WrongValueException(edit, ObjectTypesFormatters.getLabel("annotation.save.noObjetId",
            new String[] {Labels.getLabel("Entite." + entite.getNom())}));
      }
   }

   public void onClick$revert(){
      switchToStaticOrEditMode(true, true);
      // revenir à l'état initial
      tellComponentsToRevert();
      tellComponentsToUpdate();
   }

   /**
    * Inactive l'utilisation des boutons.
    * boolean true si desactivation
    */
   public void showButtonsBar(final boolean show){
      edit.setVisible(show);
      if(!show && validate.isVisible()){
         validate.setVisible(false);
         revert.setVisible(false);
      }
   }

   //	/**
   //	 * Indiques aux AnnotationComponents que l'enregistrement
   //	 * des valeurs est effectué et donc met à jour le backup en vue
   //	 * d'autres modification.
   //	 */
   //	public void tellComponentToBackUp() {
   //		for (int i = 0; i < this.champControllers.size(); i++) {
   //			this.champControllers.get(i).backUpValeursBeforeEdition();
   //		}
   //	}

   /**
    * Indiques aux AnnotationComponents que l'enregistrement
    * des valeurs a échoué et donc recharger les valeurs du
    * backUp.
    */
   public void tellComponentsToRevert(){
      for(int i = 0; i < this.champControllers.size(); i++){
         this.champControllers.get(i).revertValeurs();
      }
   }

   /**
    * Indiques aux AnnotationComponents d'updater l'affichage
    * de la valeur annotation.
    * Realise un backup post-modification. Utile lors de
    * l'utilisation d'un revert modifications.
    */
   public void tellComponentsToUpdate(){
      for(int i = 0; i < this.champControllers.size(); i++){
         this.champControllers.get(i).setAnnotationValeur();
         this.champControllers.get(i).backUpValeursBeforeEdition();
      }
   }

   @Override
   public void setNewObject(){}

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   public boolean isCanEdit(){
      return canEdit;
   }

   public void setCanEdit(final boolean cEdit){
      this.canEdit = cEdit;
   }

   /**
    * Rend les boutons edit, addNew et delete cliquable en fonction
    * des droits de l'utilisateur.
    * @param nomEntite Entite (ex.:ProdDerive).
    */

   public void drawActionsButtons(final String nomEntite){
      Boolean admin = false;
      if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin => boutons cliquables
      if(admin){
         setCanEdit(true);
      }else{
         // on extrait les OperationTypes de la base
         final OperationType modification =
            ManagerLocator.getOperationTypeManager().findByNomLikeManager("Annotation", true).get(0);

         Hashtable<?, ?> droits = new Hashtable<>();

         if(sessionScope.containsKey("Droits")){
            // on extrait les droits de l'utilisateur
            if(sessionScope.get("Droits") instanceof Hashtable){
               //Hashtable<String, List<OperationType>>
               droits = (Hashtable<?, ?>) sessionScope.get("Droits");
               if(droits.get(nomEntite) instanceof List){
                  //List<OperationType>
                  final List<?> ops = (List<?>) droits.get(nomEntite);
                  setCanEdit(ops.contains(modification));
               }
            }
         }
      }
   }

   /**
    * Collapse tous les groups ouverts repertoriés sur la page.
    * Enregistre une référence vers les groupes qui était ouverts
    * pour les ré-ouvrir par la suite à la fin du traitement par
    * la navigateur.
    */
   private void collapseAllGroups(){
      openedGroups.clear();
      for(int i = 0; i < collapGroups.size(); i++){
         if(collapGroups.get(i).isOpen()){
            collapGroups.get(i).setOpen(false);
            openedGroups.add(collapGroups.get(i));
         }
      }
   }

   /**
    * Ouvre tous les groups qui étaient ouverts au début du traitement
    * par le navigateur.
    */
   private void openCollapsedGroups(){
      for(int i = 0; i < openedGroups.size(); i++){
         openedGroups.get(i).setOpen(true);
      }
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject p){}

   /**
    * Lance la validation pour l'ensemble des controllers
    * composant la fiche.
    */
   public void validateComponents(){
      for(int i = 0; i < this.champControllers.size(); i++){
         this.champControllers.get(i).validateComponent();
      }
   }

   public List<AnnotationComponent> getChampControllers(){
      return champControllers;
   }
}
