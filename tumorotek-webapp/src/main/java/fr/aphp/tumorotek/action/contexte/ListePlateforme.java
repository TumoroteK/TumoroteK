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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListePlateforme extends AbstractListeController2
{

   private static final long serialVersionUID = -8578278340784589344L;

   private List<Plateforme> listObjects = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      final int height = getMainWindow().getListPanelHeight() + 145;
      listPanel.setHeight(height + "px");
   }

   @Override
   public List<Plateforme> getListObjects(){
      return this.listObjects;
   }

   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects.clear();
      this.listObjects.addAll((List<Plateforme>) objs);
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(pos != null){
         getListObjects().add(pos.intValue(), (Plateforme) obj);
      }else{
         getListObjects().add((Plateforme) obj);
      }
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public void initObjectsBox(){
      final Utilisateur user = SessionUtils.getLoggedUser(sessionScope);
      List<Plateforme> plaList = new ArrayList<>();
      // on récupère les plateformes acessibles en fct du
      // role de l'utilisateur
      if(user.isSuperAdmin()){
         plaList = ManagerLocator.getPlateformeManager().findAllObjectsManager();
      }else if(sessionScope.containsKey("AdminPF")){
         final Set<Plateforme> plaSet = ManagerLocator.getUtilisateurManager().getPlateformesManager(user);
         final Iterator<Plateforme> it = plaSet.iterator();
         while(it.hasNext()){
            plaList.add(it.next());
         }
      }

      listObjects = plaList;
      setCurrentRow(null);
      setCurrentObject(null);

      if(getBinder() != null){
         getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
      }
   }

   public void onClick$viewObject(final Event event){

      // déselection de la ligne courante
      deselectRow();

      // sélection de la nouvelle ligne
      selectRow(getRow((ForwardEvent) event),
         (TKdataObject) AbstractListeController2.getBindingData((ForwardEvent) event, false));

      // on passe en mode fiche & liste
      getObjectTabController().switchToFicheAndListeMode();

      // on envoie l'échantillon à la fiche
      final Plateforme edit = ((Plateforme) getCurrentObject()).clone();
      getFiche().setObject(edit);
      getFiche().switchToStaticMode();
   }

   @Override
   public void onClickObject(final Event event){}

   @Override
   public void updateMultiObjectsGridListFromOtherPage(final List<TKdataObject> objects){}

   @Override
   public void onClick$find(){}

   /**
    * Recupere le controller du composant representant la fiche associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche FicheProfil
    */
   public AbstractFicheCombineController getFiche(){
      return getObjectTabController().getFicheCombine();
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

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      return null;
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return null;
   }

   @Override
   public void updateListResultsLabel(final Integer nbResults){
      if(getObjectTabController() != null && getObjectTabController().getListeRegion() != null){
         getObjectTabController().getListeRegion()
            //.getCaption().setLabel
            .setTitle(Labels.getLabel("liste.plateforme.titre") + " ("
               + (nbResults != null ? nbResults : getListObjects() != null ? getListObjects().size() : 0) + ")");
      }
   }

}
