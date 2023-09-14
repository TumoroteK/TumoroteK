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
package fr.aphp.tumorotek.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * Composant dérivé d'une grid dont chaque ligne est un 'petit' objet
 * enfant directement qui peut être directement ajouter, éditer, supprimer
 * de la grid.
 * Date: 12/07/2010
 *
 * @author mathieu BARTHELEMY
 * @version 2.0
 */
public class SmallObjsEditableGrid extends AbstractController
{

   // private Log log = LogFactory.getLog(SmallObjsEditableGrid.class);

   private static final long serialVersionUID = 1L;

   protected Grid objGrid;

   protected Rows rows;

   private String deletionMessageKey;

   private List<SmallObjDecorator> objs = new ArrayList<>();

   private final List<SmallObjDecorator> objToCreateOrEdit = new ArrayList<>();

   private final List<SmallObjDecorator> objToDelete = new ArrayList<>();

   private Object beforeEditObjClone;

   private SmallObjDecorator currentObjEdited;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setBinder(new AnnotateDataBinder(comp));
   }

   public void setDeletionMessageKey(final String dKey){
      this.deletionMessageKey = dKey;
   }

   public String getDeletionMessageKey(){
      return deletionMessageKey;
   }

   public List<SmallObjDecorator> getObjs(){
      return objs;
   }

   public List<SmallObjDecorator> getObjToCreateOrEdit(){
      return objToCreateOrEdit;
   }

   public List<SmallObjDecorator> getObjToDelete(){
      return objToDelete;
   }

   public SmallObjDecorator getCurrentObjEdited(){
      return currentObjEdited;
   }

   public void setCurrentObjEdited(final SmallObjDecorator c){
      this.currentObjEdited = c;
   }

   public void setObjs(final List<SmallObjDecorator> os){
      this.objs = os;
      objToCreateOrEdit.clear();
      objToDelete.clear();
   }

   public Object getBeforeEditObjClone(){
      return beforeEditObjClone;
   }

   public void setBeforeEditObjClone(final Object bec){
      this.beforeEditObjClone = bec;
   }

   /**
    * Passe la ligne en mode éditable en assignant true le booleen
    * edition du SmallObjectDecorator.
    * Verifie qu'un autre élément n'est pas en cours d'edition,
    * si c'est un élément en cours modification -> revert
    * si c'est un élément en cours de creation -> remove.
    * @param event
    * @throws IOException
    */
   public void onClick$editObj(final Event event) throws IOException{
      revertCurrentObjEdition();
      this.currentObjEdited = (SmallObjDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      this.currentObjEdited.setEdition(true);
      reloadGrid();
      this.beforeEditObjClone = this.currentObjEdited.getObjClone();

      // colore Row
      ((Row) rows.getChildren().get(getObjs().indexOf(currentObjEdited))).setStyle("background-color: #fddfa9");
   }

   /**
    * Supprime un élément de la liste.
    * @param event
    */
   public void onClick$deleteObj(final Event event){
      revertCurrentObjEdition();
      // confirmation
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(deletionMessageKey)}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

         this.currentObjEdited = (SmallObjDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

         objs.remove(this.currentObjEdited);

         if(objToCreateOrEdit.contains(this.currentObjEdited)){
            objToCreateOrEdit.remove(this.currentObjEdited);
         }

         // si l'élément existait dans la BDD on l'ajoute à la
         // liste des éléments à supprimer (il ne sera délété que
         // lors de la sauvegarde finale)
         if(this.currentObjEdited.getObjDbId() != null){
            objToDelete.add(this.currentObjEdited);
         }
         this.currentObjEdited = null;
      }
      reloadGrid();
   }

   /**
    * Passe la ligne en mode statique en validant la creation ou la
    * modification du decorator.
    * Ajoute l'élément dans les codes a editer ou créer.
    * @param event
    */
   public void onClick$validateObj(final Event event){
      // lance constraints
      ((Textbox) ((Row) AbstractListeController2.getBindingData((ForwardEvent) event, true)).getFirstChild().getFirstChild())
         .getValue();

      if(currentObjEdited != null){
         getObjs().remove(currentObjEdited);
         this.currentObjEdited.setEdition(false);
         this.currentObjEdited.setValidated(true);
         // ajout a la liste des éléments a creer ou editer
         if(!getObjToCreateOrEdit().contains(getCurrentObjEdited()) && !getObjs().contains(getCurrentObjEdited())){
            getObjs().add(currentObjEdited);
            getObjToCreateOrEdit().add(getCurrentObjEdited());
         }else{ // bug fix 2.1 doublon add code assigne
            this.currentObjEdited.setEdition(true);
            this.currentObjEdited.setValidated(false);
            revertCurrentObjEdition();
         }
      }
      reloadGrid();
      this.beforeEditObjClone = null;
      this.currentObjEdited = null;
   }

   /**
    * Annule la modification ou la creation du decorator
    * en cours.
    * @param event
    */
   public void onClick$revertObj(final Event event){
      revertCurrentObjEdition();

      this.currentObjEdited = null;
      this.beforeEditObjClone = null;
      reloadGrid();
   }

   /**
    * Passe la ligne qui est en cours d'edition (creation ou modification)
    * en statique en appliquant revert si modification et deletion si
    * creation.
    * Revert modification si objet deja enregistre en base ou cree precedemment
    * mais validé!
    */
   protected void revertCurrentObjEdition(){
      if(this.currentObjEdited != null){
         // obj en cours edition deja enregistre en base ou valide
         if(this.currentObjEdited.getObjDbId() != null || this.currentObjEdited.isValidated()){
            // revert
            this.currentObjEdited.setObj(this.beforeEditObjClone);
            this.currentObjEdited.setEdition(false);
         }else{ // nouvel élément
            objs.remove(this.currentObjEdited);
            if(objToCreateOrEdit.contains(this.currentObjEdited)){
               objToCreateOrEdit.remove(this.currentObjEdited);
            }
         }
      }
   }

   /**
    * Rafraichit le contenu du grid.
    */
   public void reloadGrid(){
      getBinder().loadAttribute(objGrid, "model");
   }

   /**
    * Monte un objet dans l'ordre de la liste.
    * @param event
    */
   public void onClick$upObj(final Event event){
      revertCurrentObjEdition();
      upObject((SmallObjDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false));
      reloadGrid();
   }

   /**
    * Descend l'objet dans l'ordre de la liste.
    * @param event
    */
   public void onClick$downObj(final Event event){
      revertCurrentObjEdition();
      final SmallObjDecorator deco = (SmallObjDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      final int tabIndex = getObjs().indexOf(deco);
      if(tabIndex + 1 < getObjs().size()){
         upObject(getObjs().get(tabIndex + 1));
      }
      reloadGrid();
   }

   /**
    * Effectue l'operation de mouvements des objets au sein de la liste.
    * @param objet a monter d'un cran
    */
   private void upObject(final SmallObjDecorator obj){
      final int tabIndex = getObjs().indexOf(obj);
      SmallObjDecorator supObjectInList = null;
      final Integer ordreObj = obj.getOrdre();
      if(tabIndex - 1 > -1){
         supObjectInList = getObjs().get(tabIndex - 1);
         obj.setOrdre(supObjectInList.getOrdre());
         supObjectInList.setOrdre(ordreObj);
         getObjs().set(tabIndex, supObjectInList);
         getObjs().set(tabIndex - 1, obj);
      }
   }

   public void reset(){
      getObjToDelete().clear();
      getObjs().clear();
      getObjToCreateOrEdit().clear();
      setCurrentObjEdited(null);
      setBeforeEditObjClone(null);
   }

   public Grid getObjGrid(){
      return objGrid;
   }
}
