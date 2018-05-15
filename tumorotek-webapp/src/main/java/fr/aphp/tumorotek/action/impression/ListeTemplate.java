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
package fr.aphp.tumorotek.action.impression;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListeTemplate extends AbstractListeController2
{

   private static final long serialVersionUID = 4198016623684863322L;

   private final List<Template> listObjects = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      final int height = getMainWindow().getListPanelHeight() + 145;
      listPanel.setHeight(height + "px");
   }

   @Override
   public List<Template> getListObjects(){
      return this.listObjects;
   }

   
   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects.clear();
      this.listObjects.addAll((List<Template>) objs);
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(pos != null){
         getListObjects().add(pos.intValue(), (Template) obj);
      }else{
         getListObjects().add((Template) obj);
      }
   }

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){
      getSelectedObjects().remove(obj);
   }

   @Override
   public List<? extends Object> getSelectedObjects(){
      return null;
   }

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){}

   @Override
   public TKSelectObjectRenderer getListObjectsRenderer(){
      return null;
   }

   @Override
   public ImpressionController getObjectTabController(){
      return ((ImpressionController) self.getParent().getParent().getParent().getFellow("winTemplate")
         .getAttributeOrFellow("winTemplate$composer", true));
   }

   @Override
   public void passSelectedToList(){}

   @Override
   public void passListToSelected(){}

   @Override
   public void addToSelectedObjects(final TKdataObject obj){}

   @Override
   public List<Integer> doFindObjects(){
      return null;
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public void initObjectsBox(){
      // TODO gérer liste des utilisateurs
      final List<Template> templates =
         ManagerLocator.getTemplateManager().findByBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));

      setListObjects(templates);
      setCurrentRow(null);
      setCurrentObject(null);

      if(getBinder() != null){
         getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
      }
   }

   public void onClick$viewObject(final Event event){
      // déselection de la ligne courante
      deselectRow();

      selectRowAndDisplayObject(getRow((ForwardEvent) event),
         (TKdataObject) AbstractListeController2.getBindingData((ForwardEvent) event, false));
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      return null;
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }
}
