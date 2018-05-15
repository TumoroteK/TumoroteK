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

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;

/**
 * Composant abstract qui va permettre le referencement des association
 * One-To-Many au niveau d'un objet. Exemple Banque referencant les
 * conteneurs ou les tables d'annotations Patient etc...
 * Date: 15/10/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public abstract class OneToManyComponent<T extends Object> extends AbstractController
{

   private static final long serialVersionUID = 8722970657499733752L;

   protected Grid objectsList;
   protected Button addObj;
   protected Column deleteHeader;
   protected Column linkHeader;
   protected Column staticHeader;
   protected Label objLinkLabel;
   protected Box addObjBox;
   protected Listbox objectsBox;

   private Group groupHeader;

   public void setGroupHeader(final Group gH){
      this.groupHeader = gH;
   }

   public Group getGroupHeader(){
      return groupHeader;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      setBinder(new AnnotateDataBinder(comp));
      super.doAfterCompose(comp);
      drawActionForComponent();
      getBinder().loadAll();
   }

   /**
    * Demande la msie à jour la liste et du 
    * group header associe au composant.
    */
   public void updateComponent(){
      if(getGroupHeader() != null){
         getGroupHeader().setLabel(getGroupHeaderValue());
      }
      getBinder().loadAttribute(objectsList, "model");
   }

   public void switchToEditMode(final boolean b){
      deleteHeader.setVisible(b);
      addObj.setVisible(b);
      linkHeader.setVisible(false);
      staticHeader.setVisible(true);
      getBinder().loadComponent(objectsList);
   }

   public void switchToCreateMode(){
      getObjects().clear();
      getBinder().loadComponent(objectsList);
      switchToEditMode(true);
   }

   public void switchToStaticMode(){
      deleteHeader.setVisible(false);
      addObj.setVisible(false);
      addObjBox.setVisible(false);
      linkHeader.setVisible(true);
      staticHeader.setVisible(false);
      getBinder().loadComponent(objectsList);
   }

   /**
    * Retire un objet de la liste d'associations.
    */
   public void onClick$deleteImage(final Event event){
      if(!addObj.isDisabled()){
         // recupere l'objet
         final Object obj = AbstractListeController2.getBindingData((ForwardEvent) event, false);

         deleteObj(obj);
      }
   }

   public void deleteObj(final Object obj){
      removeFromListObjects(obj);
      updateComponent();
   }

   /**
    * Recoit l'evenement de suppression envoyé depuis le 
    * click sur la delete image.
    * @param event
    */
   public void onDeleteObj$rows(final ForwardEvent event){
      if(!addObj.isDisabled()){
         // recupere l'objet
         final Object obj = event.getOrigin().getData();

         removeFromListObjects(obj);
         updateComponent();
      }
   }

   /**
    * Affiche la box permettant l'ajout d'un objet a partir d'une liste.
    */
   public void onClick$addObj(){
      objectsBox.invalidate();
      final ListModel<? extends Object> list = new ListModelList<>(findObjectsAddable());
      objectsBox.setModel(list);
      // getBinder().loadAttribute(objectsBox, "model");

      // affiche les composants
      addObjBox.setVisible(true);
      addObj.setVisible(false);
      deleteHeader.setVisible(false);
   }

   /**
    * Ajoute le conteneur selectionné dans la listbox à 
    * la liste des conteneurs. 
    */
   public void onClick$addSelObj(){
      if(objectsBox.getSelectedItem() != null){
         addToListObjects(objectsBox.getSelectedItem().getValue());
      }else{ // selectionne le premier de la liste
         if(objectsBox.getItemCount() > 0){
            addToListObjects(objectsBox.getItemAtIndex(0).getValue());
         }
      }
      updateComponent();

      // affiche les composants
      onClick$cancelSelObj();
   }

   /**
    * Annule l'ajout d'un objet en réaffichant et masquant
    * les composants adequats.
    */
   public void onClick$cancelSelObj(){
      // affiche les composants
      addObjBox.setVisible(false);
      addObj.setVisible(true);
      deleteHeader.setVisible(true);
   }

   /*************************************************************************/
   /************************** ABSTRACT**************************************/
   /*************************************************************************/

   public abstract List<T> getObjects();

   public abstract void setObjects(List<T> objs);

   /**
    * Renvoie la valeur d'entete affichee dans le Label du groupe Parent. 
    */
   public abstract String getGroupHeaderValue();

   /**
    * Lien vers la fiche detaillee de l'objet.
    */
   public abstract void onClick$objLinkLabel(Event event);

   /**
    * Prepare la liste des objects qui pourront être ajoutés en
    *  association. Retires donc de cette liste tous les objets 
    *  deja associés.
    * @return liste des objets associables.
    */
   public abstract List<? extends Object> findObjectsAddable();

   public abstract void addToListObjects(T obj);

   public abstract void removeFromListObjects(Object obj);

   /**
    * Applique les droits de l'utilisateur sur le composant 
    * d'association.
    */
   public abstract void drawActionForComponent();
}
