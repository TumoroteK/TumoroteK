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
package fr.aphp.tumorotek.action.administration.annotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Liste les tables annotations en les groupant par entité.
 * Date: 18/03/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ListeTableAnnotation extends AbstractListeController2
{

   private static final long serialVersionUID = 2480945046066137749L;

   private Rows gridRows;

   private Group patientTablesGroup;
   private Group prelevementTablesGroup;
   private Group echantillonTablesGroup;
   private Group deriveTablesGroup;
   private Group cessionTablesGroup;
   private Group cataloguesGroup;

   private List<TableAnnotation> listObjects = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      final int height = getMainWindow().getListPanelHeight() + 140;
      listPanel.setHeight(height + "px");

      drawGridWithGroups();

      drawActionsForTables();
   }

   private void drawActionsForTables(){
      Boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }
      setCanNew(admin);
   }

   private void drawGridWithGroups(){
      // creation group
      patientTablesGroup = new Group();
      patientTablesGroup.setLabel(Labels.getLabel("general.patient"));
      patientTablesGroup.setOpen(false);
      objectsListGrid.getFellow("gridRows").appendChild(patientTablesGroup);
      prelevementTablesGroup = new Group();
      prelevementTablesGroup.setLabel(Labels.getLabel("general.prelevement"));
      prelevementTablesGroup.setOpen(false);
      objectsListGrid.getFellow("gridRows").appendChild(prelevementTablesGroup);
      echantillonTablesGroup = new Group();
      echantillonTablesGroup.setLabel(Labels.getLabel("general.echantillon"));
      echantillonTablesGroup.setOpen(false);
      objectsListGrid.getFellow("gridRows").appendChild(echantillonTablesGroup);
      deriveTablesGroup = new Group();
      deriveTablesGroup.setLabel(Labels.getLabel("general.derive"));
      deriveTablesGroup.setOpen(false);
      objectsListGrid.getFellow("gridRows").appendChild(deriveTablesGroup);
      cessionTablesGroup = new Group();
      cessionTablesGroup.setLabel(Labels.getLabel("general.cession"));
      cessionTablesGroup.setOpen(false);
      objectsListGrid.getFellow("gridRows").appendChild(cessionTablesGroup);

      // group catalogues
      cataloguesGroup = new Group();
      cataloguesGroup.setLabel(Labels.getLabel("annotation.catalogues"));
      cataloguesGroup.setOpen(false);
      objectsListGrid.getFellow("gridRows").appendChild(cataloguesGroup);

      final Iterator<?> tIt = getListObjects().iterator();
      while(tIt.hasNext()){
         drawATableAnnotationItem((TableAnnotation) tIt.next());
      }

      drawCataloguesItems();
   }

   @Override
   public List<TableAnnotation> getListObjects(){
      return this.listObjects;
   }

   
   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects = (List<TableAnnotation>) objs;
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(pos != null){
         getListObjects().add(pos.intValue(), (TableAnnotation) obj);
      }else{
         getListObjects().add((TableAnnotation) obj);
      }
   }

   @Override
   public void selectlastRow(final TKdataObject obj){
      if(obj != null){
         final Iterator<Component> rowsIt = objectsListGrid.getRows().getChildren().iterator();
         Row row = null;
         Object tab = null;
         while(rowsIt.hasNext() && !obj.equals(tab)){
            row = (Row) rowsIt.next();
            tab = row.getValue();
         }
         selectRowAndDisplayObject(row, obj);
      }
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){}

   @Override
   public List<Contrat> getSelectedObjects(){
      return null;
   }

   @Override
   public void addToSelectedObjects(final TKdataObject obj){}

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){}

   @Override
   public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
      return null;
   }

   @Override
   public AnnotationsController getObjectTabController(){
      return (AnnotationsController) super.getObjectTabController();
   }

   @Override
   public void passSelectedToList(){}

   @Override
   public void passListToSelected(){}

   @Override
   public void initObjectsBox(){
      // affiche dans la liste les tables assignées aux banques pour
      // lesquelles l'utilisateur loggé a les droits d'admin ou 
      // les banques appartenant à la plateforme dont l'utilisateur
      // loggé est admin.
      if(SessionUtils.getLoggedUser(sessionScope).isSuperAdmin()){
         setListObjects(
            ManagerLocator.getTableAnnotationManager().findByPlateformeManager(SessionUtils.getPlateforme(sessionScope)));
      }else{
         setListObjects(ManagerLocator.getTableAnnotationManager().findByBanquesManager(ManagerLocator.getBanqueManager()
            .findByUtilisateurIsAdminManager(SessionUtils.getLoggedUser(sessionScope), SessionUtils.getPlateforme(sessionScope)),
            false));

      }

      setCurrentRow(null);
      setCurrentObject(null);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   @Override
   public List<Integer> doFindObjects(){
      return null;
   }

   public void onClick$viewObject(final Event event){
      // déselection de la ligne courante
      deselectRow();

      // sélection de la nouvelle ligne
      selectRow((Row) event.getTarget().getParent(), (TKdataObject) event.getTarget().getAttribute("table"));

      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();

      // on envoie la table à la fiche
      final TableAnnotation edit = ((TableAnnotation) getCurrentObject()).clone();
      getObjectTabController().getFicheCombine().setObject(edit);
      getObjectTabController().getFicheCombine().switchToStaticMode();
   }

   /**
    * Mets à jour l'objet sélectionné de la liste.
    * @param objet
    */
   @Override
   public void updateObjectGridList(final Object obj){
      // l'objet passé en paramètre est cloné
      final TableAnnotation edit = ((TableAnnotation) obj).clone();

      // on vérifie que la liste a bien un objet sélectionné
      if(getCurrentObject() != null){
         // si l'objet édité est dans la liste, il est forcément
         // sélectionné.
         // On vérifie donc que l'objet sélectionné a le meme id
         // que celui édité
         final Integer idSelected = ((TableAnnotation) getCurrentObject()).getTableAnnotationId();
         final Integer idUpdated = edit.getTableAnnotationId();
         if(idSelected.equals(idUpdated)){
            ((Label) getCurrentRow().getFirstChild()).setValue(edit.getNom());
            ((Label) getCurrentRow().getFirstChild()).removeAttribute("table");
            ((Label) getCurrentRow().getFirstChild()).setAttribute("table", edit);

            // on re-sélectionne la liste contenant l'obj
            //				Rows rows = objectsListGrid.getRows();
            //				List<Component> comps = rows.getChildren();
            selectRow(getCurrentRow(), edit);
         }
      }
   }

   @Override
   public boolean updateObjectGridListFromOtherPage(final Object obj, final boolean select){
      return true;
   }

   /**
    * Ajoute une tableAnnotation (venant du formulaire de creation) a la liste
    * Cet objet devient l'objet courant.
    * @param protocole
    */
   @Override
   public void addToObjectList(final Object newObj){
      // L'objet inséré est un clone de celui du formulaire
      // afin d'éviter des effets de bord lors de la modif
      // du formulaire
      final TableAnnotation newTable = ((TableAnnotation) newObj).clone();
      getListObjects().add(newTable);

      // dessine la nouvelle table dans la sous-liste
      final Row newRow = drawATableAnnotationItem(newTable);

      deselectRow();
      selectRow(newRow, newTable);
   }

   //	@Override
   //	public void clearList() {
   //		this.listObjects = new ArrayList<TableAnnotation>();
   //		initObjectsBox(0);
   //
   //		//getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   //	}

   /**
    * Supprime une table a la liste.
    * @param tableAnnotation
    */
   @Override
   public void removeObjectAndUpdateList(final TKdataObject obj){
      final TableAnnotation table = (TableAnnotation) obj;

      // suppression de la liste
      this.listObjects.remove(table);
      getCurrentRow().detach();

      setCurrentRow(null);
      setCurrentObject(null);
   }

   @Override
   public void updateMultiObjectsGridListFromOtherPage(final List<TKdataObject> objects){}

   /**
    * Surcharge la methode deselect pour realiser deselection 
    * particuliere car le grid n'utilise pas model de decorators 
    * donc ind = -1 si modification du nom.
    */
   @Override
   public void deselectRow(){
      // on vérifie qu'une ligne est bien sélectionnée
      if(getCurrentObject() != null && getCurrentRow() != null){
         // int ind = listObjects.indexOf(currentObject);
         // on lui spécifie une couleur en fonction de son
         // numéro de ligne
         //if (ind > -1) {
         getCurrentRow().setStyle("background-color : #e2e9fe");
         setCurrentRow(null);
         setCurrentObject(null);
         //}
      }
   }

   /**
    * Dessine une ligne de la liste a partir d'un objet TableAnnotation.
    * Ajoute l'evenement onClick et l'objet en attribut du Label.
    * @return row Row dessinée (pour pouvoir la selectionner).
    */
   private Row drawATableAnnotationItem(final TableAnnotation table){
      final Row tableRow = new Row();
      final Label tableLabel = new Label();
      tableLabel.setAttribute("table", table);
      tableLabel.setValue(table.getNom());
      tableLabel.setClass("formLink");
      tableLabel.addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onClick$viewObject(event);
         }
      });
      tableRow.appendChild(tableLabel);
      if(table.getEntite().getNom().equals("Patient")){
         gridRows.insertBefore(tableRow, prelevementTablesGroup);
         if(!patientTablesGroup.isOpen()){
            patientTablesGroup.setOpen(true);
         }
      }else if(table.getEntite().getNom().equals("Prelevement")){
         gridRows.insertBefore(tableRow, echantillonTablesGroup);
         if(!prelevementTablesGroup.isOpen()){
            prelevementTablesGroup.setOpen(true);
         }
      }else if(table.getEntite().getNom().equals("Echantillon")){
         gridRows.insertBefore(tableRow, deriveTablesGroup);
         if(!echantillonTablesGroup.isOpen()){
            echantillonTablesGroup.setOpen(true);
         }
      }else if(table.getEntite().getNom().equals("ProdDerive")){
         gridRows.insertBefore(tableRow, cessionTablesGroup);
         if(!deriveTablesGroup.isOpen()){
            deriveTablesGroup.setOpen(true);
         }
      }else if(table.getEntite().getNom().equals("Cession")){
         gridRows.insertBefore(tableRow, cataloguesGroup);
         if(!cessionTablesGroup.isOpen()){
            cessionTablesGroup.setOpen(true);
         }
      }
      tableRow.setValue(table);

      return tableRow;
   }

   /**
    * Dessine une ligne pour chaque Catalogue.
    * Ajoute l'evenement onClick et la table d'annotation composite qui 
    * est associée aux champs éditables par l'utilisateur.
    * @return row Row dessinée (pour pouvoir la selectionner).
    */
   private void drawCataloguesItems(){
      final Iterator<Catalogue> cIt = ManagerLocator.getCatalogueManager().findAllObjectsManager().iterator();
      Iterator<TableAnnotation> tabsIt;
      TableAnnotation table;
      while(cIt.hasNext()){
         tabsIt = ManagerLocator.getTableAnnotationManager().findByCatalogueAndChpEditManager(cIt.next()).iterator();
         while(tabsIt.hasNext()){
            table = tabsIt.next();
            final Row tableRow = new Row();
            final Label tableLabel = new Label();
            tableLabel.setAttribute("table", table);
            tableLabel.setValue(table.getNom());
            tableLabel.setClass("formLink");
            tableLabel.addEventListener("onClick", new EventListener<Event>()
            {
               @Override
               public void onEvent(final Event event) throws Exception{
                  onClick$viewObject(event);
               }
            });
            tableRow.appendChild(tableLabel);
            gridRows.appendChild(tableRow);
            tableRow.setValue(table);

         }
      }
   }

   @Override
   public void onClickObject(final Event event){}

   /**
    * Selectionne le premier objet de la liste par défaut.
    * Surcharge car la première ligne est nécessairement un groupe.
    */
   @Override
   public void selectFirstObjet(){
      if(getListObjects().size() > 0){
         Component firstRow = objectsListGrid.getRows().getFirstChild();

         while(firstRow instanceof Group){
            firstRow = firstRow.getNextSibling();
         }

         if(firstRow instanceof Row){
            selectRowAndDisplayObject(((Row) firstRow), (TKdataObject) ((Row) firstRow).getValue());
         }
      }
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      return null;
   }

   @Override
   public void onClick$addNew(final Event event) throws Exception{
      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();
      getObjectTabController().getFicheCombine().setNewObject();
      getObjectTabController().getFicheCombine().switchToCreateMode();
      switchToEditMode(true);
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }
}
