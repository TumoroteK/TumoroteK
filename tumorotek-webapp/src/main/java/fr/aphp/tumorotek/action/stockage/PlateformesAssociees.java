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
package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelSet;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;

public class PlateformesAssociees extends OneToManyComponent<ConteneurPlateforme>
{

   private static final long serialVersionUID = 1L;

   private Listbox plateformeBox;

   private List<ConteneurPlateforme> objects = new ArrayList<>();
   private Conteneur conteneur;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      objLinkLabel = new Label();

      final ListitemRenderer<Plateforme> plateformeRenderer = (li, pf, idx) -> {
         li.setLabel(pf.getNom());
         li.setValue(pf);
      };

      plateformeBox.setItemRenderer(plateformeRenderer);

   }

   @Override
   public List<ConteneurPlateforme> getObjects(){
      return this.objects;
   }

   @Override
   public void setObjects(final List<ConteneurPlateforme> objs){
      this.objects = objs;
      updateComponent();
   }

   @Override
   public void addToListObjects(final ConteneurPlateforme obj){
      getObjects().add(obj);
   }

   @Override
   public void removeFromListObjects(final Object obj){
      
      final ConteneurPlateforme objToRemove = (ConteneurPlateforme)obj;
      
      getObjects().remove(obj);
      
      if(null != objToRemove.getPlateforme() && plateformeBox.getListModel() != null) {
         
         ListModelSet<Object> listModel = ((ListModelSet<Object>)plateformeBox.getListModel());
         
         listModel.add(objToRemove.getPlateforme());
         
      }
      
   }

   @Override
   public void updateComponent(){

      ListModelSet<Object> listModelPf = (ListModelSet<Object>)plateformeBox.getListModel();
      
      if(!addObj.isVisible() && null != listModelPf) {
         addObjBox.setVisible(!listModelPf.isEmpty());
      }
      
      super.updateComponent();
      
   }
   
   @Override
   public String getGroupHeaderValue(){
      return Labels.getLabel("conteneur.plateformes.accessibles", new Object[] {getObjects().size()});
   }

   @Override
   public List<Plateforme> findObjectsAddable(){

      final List<Plateforme> plateformesAssignees =
         getObjects().stream().map(ConteneurPlateforme::getPlateforme).collect(Collectors.toList());

      plateformesAssignees.add(conteneur.getPlateformeOrig());

      final List<Plateforme> plateformesAjoutables = ManagerLocator.getManager(PlateformeManager.class).findAllObjectsManager()
         .stream().filter(pf -> !plateformesAssignees.contains(pf)).sorted(Comparator.comparing(Plateforme::getNom))
         .collect(Collectors.toList());

      return plateformesAjoutables;

   }

   @Override
   public void onClick$addObj(){

      ListModel<Plateforme> plateformesList = new ListModelSet<>(findObjectsAddable());

      plateformeBox.setModel(plateformesList);

      // affiche les composants
      addObjBox.setVisible(true);
      addObj.setVisible(false);
   }

   @Override
   public void drawActionForComponent(){}

   @Override
   public void onClick$objLinkLabel(final Event event){}

   /**
    * Vérifies qu'aucun référencement sur ce conteneur, impliquant un 
    * probable stockage de matériel, n'a été établi à partir de la 
    * ConteneurPlateforme.	 
    **/
   @Override
   public void onClick$deleteImage(final Event event){

      final fr.aphp.tumorotek.model.stockage.ConteneurPlateforme cur =
         (ConteneurPlateforme) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(ManagerLocator.getConteneurManager().findByPartageManager(cur.getPlateforme(), true).contains(cur.getConteneur())){
         Messagebox.show(Labels.getLabel("conteneur.plateforme.remove.error"), Labels.getLabel("general.warning"), Messagebox.OK,
            Messagebox.ERROR);
      }else{
         super.onClick$deleteImage(event);
      }
   }

   @Override
   public void onClick$addSelObj(){

      final Listitem selectedItem = plateformeBox.getSelectedItem();

      if(null != selectedItem) {
         final Plateforme selectedPf = selectedItem.getValue();
         final ConteneurPlateforme conteneurPf = new ConteneurPlateforme();

         conteneurPf.setConteneur(conteneur);
         conteneurPf.setPlateforme(selectedPf);
         conteneurPf.setPartage(true);
         addToListObjects(conteneurPf);
         
         ListModelSet<Object> listModel = ((ListModelSet<Object>)plateformeBox.getListModel());
         
         listModel.remove(selectedPf);
      }

      updateComponent();
      
   }

   /**
    * Surcharge pour ne pas appliquer clear sur la liste.
    */
   @Override
   public void switchToCreateMode(){
      getBinder().loadComponent(objectsList);
      switchToEditMode(true);
   }

   /**
    * Surcharge pour imposer que la dernière pf ne puisse jamais
    * être éffacée laissant une table orpheline.
    */
   @Override
   public void switchToEditMode(final boolean b){
      super.switchToEditMode(b);
   }

   public List<Plateforme> getPlateformes(){
      final List<Plateforme> pfs = new ArrayList<>();

      for(final ConteneurPlateforme cp : getObjects()){
         pfs.add(cp.getPlateforme());
      }

      return pfs;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }
}
