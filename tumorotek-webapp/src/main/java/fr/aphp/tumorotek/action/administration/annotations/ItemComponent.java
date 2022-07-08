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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.model.coeur.annotation.Item;

/**
 * MacroComponent dessinant les composant editables permettant à
 * l'utilisateur d'enregistrer un item de thésaurus.
 * Date 12/05/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ItemComponent extends HtmlMacroComponent
{

   private static final long serialVersionUID = 1L;

   private Item item = new Item();

   private boolean isCreate = true;

   private boolean disableDefaut = false;

   private boolean isDefaut = false;

   private boolean defautChged = false;

   private Component backComp;

   public void setItem(final Item it){
      this.item = it;
   }

   public void setBackComp(final Component bC){
      this.backComp = bC;
   }

   public void setIsCreate(final boolean isC){
      this.isCreate = isC;
   }

   public void setDisableDefaut(final boolean dD){
      this.disableDefaut = dD;
   }

   public void setIsDefaut(final boolean isDef){
      this.isDefaut = isDef;
   }

   @Override
   public void afterCompose(){
      super.afterCompose();

      // textboxes values
      ((Textbox) getFirstChild().getFellow("labelBox")).setValue(item.getLabel());

      ((Textbox) getFirstChild().getFellow("valBox")).setValue(item.getValeur());

      final ConstWord constr = new ConstWord();
      constr.setNullable(false);
      ((Textbox) getFirstChild().getFellow("labelBox")).setConstraint(constr);
      final ConstWord constr2 = new ConstWord();
      constr2.setNullable(true);
      ((Textbox) getFirstChild().getFellow("valBox")).setConstraint(constr2);

      // checkbox defaut
      ((Checkbox) getFirstChild().getFellow("defautBox")).setDisabled(disableDefaut);
      if(!disableDefaut){
         ((Checkbox) getFirstChild().getFellow("defautBox")).setChecked(isDefaut);
      }

      // Events listeners
      getFirstChild().getFellow("valImg").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            recordItem();
         }
      });
      getFirstChild().getFellow("cancelImg").addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            cancelItem();
         }
      });
      getFirstChild().getFellow("defautBox").addEventListener("onCheck", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            defautChged = true;
         }
      });
   }

   /**
    * Recupere les valeurs specifiée pour les assigner à l'Item.
    * Ferme la modale.
    * Post back un event pour rafraichir la liste d'item
    * dans la modale defaut.
    * Post back un event pour rafraichir la liste de valeurs par defaut.
    */
   private void recordItem(){

      item.setLabel(((Textbox) getFirstChild().getFellow("labelBox")).getValue());
      if(!("".equals(((Textbox) getFirstChild().getFellow("valBox")).getValue()))){
         item.setValeur(((Textbox) getFirstChild().getFellow("valBox")).getValue());
      }else{
         item.setValeur(null);
      }

      // defaut
      if(defautChged){
         if(((Checkbox) getFirstChild().getFellow("defautBox")).isChecked()){
            Events.postEvent(new Event("onAddDefautForItem", backComp, item));
         }else{
            Events.postEvent(new Event("onDropDefautForItem", backComp, item));
         }
      }

      if(isCreate){
         Events.postEvent(new Event("onAddItem", backComp, item));
      }else{
         Events.postEvent(new Event("onUpdateItem", backComp, item));
      }

      Events.postEvent(new Event("onClose", getParent()));
   }

   /**
    * Ferme la modale.
    */
   private void cancelItem(){
      Events.postEvent(new Event("onClose", getParent()));
   }
}
