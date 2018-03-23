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
package fr.aphp.tumorotek.action.cession;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListeContrat extends AbstractListeController2
{

   private static final long serialVersionUID = 2480945046066137749L;

   private List<ContratDecorator> listObjects = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      modificationItem = new Menuitem();
      super.doAfterCompose(comp);

      final int height = getMainWindow().getListPanelHeight() + 142;
      listPanel.setHeight(height + "px");
   }

   @Override
   public List<ContratDecorator> getListObjects(){
      return this.listObjects;
   }

   
   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects = (List<ContratDecorator>) objs;
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(pos != null){
         getListObjects().add(pos.intValue(), (ContratDecorator) obj);
      }else{
         getListObjects().add((ContratDecorator) obj);
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
      return null;
   }

   @Override
   public ContratController getObjectTabController(){
      return (ContratController) super.getObjectTabController();
   }

   @Override
   public void passSelectedToList(){}

   @Override
   public void passListToSelected(){}

   @Override
   public void initObjectsBox(){
      final List<Contrat> contrats =
         ManagerLocator.getContratManager().findAllObjectsByPlateformeManager(SessionUtils.getPlateforme(sessionScope));

      listObjects = ContratDecorator.decorateListe(contrats);
      setCurrentRow(null);
      setCurrentObject(null);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   @Override
   public void onClickObject(final Event event){
      // déselection de la ligne courante
      deselectRow();

      // sélection de la nouvelle ligne
      selectRow(getRow((ForwardEvent) event),
         (TKdataObject) AbstractListeController2.getBindingData((ForwardEvent) event, false));

      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();

      // on envoie l'échantillon à la fiche
      final Contrat edit = ((ContratDecorator) getCurrentObject()).getContrat().clone();
      getObjectTabController().getFicheCombine().setObject(edit);
      getObjectTabController().getFicheCombine().switchToStaticMode();
   }

   @Override
   public void onClick$addNew(final Event event) throws Exception{
      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();
      getObjectTabController().getFicheCombine().onClick$addNewC();
   }

   /**
    * Mets à jour l'objet sélectionné de la liste.
    * @param objet
    */
   @Override
   public void updateObjectGridList(final Object obj){
      // l'objet passé en paramètre est cloné
      final ContratDecorator edit = new ContratDecorator(((Contrat) obj).clone());

      // on vérifie que la liste a bien un objet sélectionné
      if(getCurrentObject() != null){
         // si l'objet édité est dans la liste, il est forcément
         // sélectionné.
         // On vérifie donc que l'objet sélectionné a le meme id
         // que celui édité
         final Integer idSelected = ((ContratDecorator) getCurrentObject()).getContrat().getContratId();
         final Integer idUpdated = edit.getContrat().getContratId();
         if(idSelected.equals(idUpdated)){
            final int ind = getListObjects().indexOf(getCurrentObject());
            // si c'est le cas, maj de la liste par 
            // suppression/insertion
            if(ind > -1){
               getListObjects().remove(ind);
               addToListObjects(edit, new Integer(ind));

               getBinder().loadAttribute(objectsListGrid, "model");

               // on re-sélctionne la liste contenant l'obj
               final Rows rows = objectsListGrid.getRows();
               final List<Component> comps = rows.getChildren();
               selectRow((Row) comps.get(ind), edit);
            }
         }
      }
   }

   @Override
   public boolean updateObjectGridListFromOtherPage(final Object obj, final boolean select){
      boolean updated = false;

      // l'objet passé en paramètre est cloné
      final ContratDecorator edit = new ContratDecorator((Contrat) obj);

      // si la liste contient l'objet updaté
      if(getListObjects().contains(edit)){

         // déselection de la liste courante
         deselectRow();

         // on récupère l'objet et on le met à jour par
         // suppression/insertion dans la liste
         final int ind = getListObjects().indexOf(edit);
         getListObjects().remove(ind);
         addToListObjects(edit, new Integer(ind));

         // maj de la grille
         getBinder().loadAttribute(objectsListGrid, "model");

         if(select){
            // on récupère toutes les lignes de la grille et on
            // sélectionne celle qui contient l'obj updaté
            final Rows rows = objectsListGrid.getRows();
            final List<Component> comps = rows.getChildren();
            selectRow((Row) comps.get(ind), edit);

            // on affiche la page contenant l'objet
            objectsListGrid.getPaginal().setActivePage(getPageNumberForObject(getCurrentObject()));

            // on passe l'objet à la fiche
            getObjectTabController().getFicheCombine().setObject(edit.getContrat());
         }
         updated = true;
      }
      return updated;
   }

   /**
    * Ajoute un protocole (venant du formulaire de creation) a la liste
    * Cet objet devient l'objet courant.
    * @param protocole
    */
   @Override
   public void addToObjectList(final Object newObj){
      // L'objet inséré est un clone de celui du formulaire
      // afin d'éviter des effets de bord lors de la modif
      // du formulaire
      final ContratDecorator newContrat = new ContratDecorator(((Contrat) newObj).clone());
      this.listObjects.add(newContrat);

      // update de la liste
      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");

      // on récupère toutes les lignes de la grille et on
      // sélectionne celle du nouvel objet
      final Rows rows = objectsListGrid.getRows();
      final List<Component> comps = rows.getChildren();
      selectRow((Row) comps.get(this.listObjects.indexOf(newContrat)), newContrat);

      // on affiche la page sur laquelle se trouve l'objet
      objectsListGrid.getPaginal().setActivePage(getPageNumberForObject(getCurrentObject()));
   }

   @Override
   public void removeObjectAndUpdateList(final TKdataObject obj){
      final ContratDecorator deco = new ContratDecorator((Contrat) obj);
      super.removeObjectAndUpdateList(deco);
   }

   @Override
   public void onClick$find(){}

   @Override
   public void updateMultiObjectsGridListFromOtherPage(final List<TKdataObject> objects){}

   @Override
   public List<Integer> doFindObjects(){
      return null;
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      return null;
   }

   @Override
   public void applyDroitsOnListe(){
      boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }

      // si l'utilisateur est admin PF => boutons cliquables
      if(admin){
         addNew.setDisabled(false);
      }else{
         addNew.setDisabled(true);
      }
   }

   @Override
   public void disableToolBar(final boolean b){
      addNew.setDisabled(b);
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }
}
