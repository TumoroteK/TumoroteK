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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Menuitem;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant une liste de collections.
 * Controller créé le 16/12/2009.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class ListeBanque extends AbstractListeController2
{

   private static final long serialVersionUID = 2480945046066137749L;

   // @since 2.1
   private Checkbox banquesActifsBox;

   private final List<Banque> listObjects = new ArrayList<>();

   private final BanqueRowRenderer banqueRenderer = new BanqueRowRenderer();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      modificationItem = new Menuitem();
      super.doAfterCompose(comp);

      drawActionsForBanques();

      final int height = getMainWindow().getListPanelHeight() + 170;
      listPanel.setHeight(height + "px");
   }

   @Override
   public List<Banque> getListObjects(){
      return this.listObjects;
   }

   
   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects.clear();
      this.listObjects.addAll((List<Banque>) objs);
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(pos != null){
         getListObjects().add(pos.intValue(), (Banque) obj);
      }else{
         getListObjects().add((Banque) obj);
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
   public TKSelectObjectRenderer getListObjectsRenderer(){
      return banqueRenderer;
   }

   @Override
   public BanqueController getObjectTabController(){
      return (BanqueController) super.getObjectTabController();
   }

   @Override
   public void passSelectedToList(){}

   @Override
   public void passListToSelected(){}

   @Override
   public void initObjectsBox(){
      setListObjects(
         ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(SessionUtils.getPlateforme(sessionScope), false));

      setCurrentRow(null);
      setCurrentObject(null);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   @Override
   public List<Integer> doFindObjects(){
      return null;
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
   }

   public void drawActionsForBanques(){
      Boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }
      setCanNew(admin);
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }

   /**
    * @since 2.1
    */
   public void onCheck$banquesActifsBox(){

      listObjects.clear();

      if(banquesActifsBox.isChecked()){ // only active
         listObjects.addAll(
            ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(SessionUtils.getCurrentPlateforme(), false));
      }else{ // all of them
         listObjects.addAll(
            ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(SessionUtils.getCurrentPlateforme(), null));
      }

      //	setCurrentRow(null);
      //	setCurrentObject(null);

      //	getObjectTabController().getFicheCombine().clearData();

      // getBinder().loadAttribute(
      //		self.getFellow("objectsListGrid"), "model");
   }

}
