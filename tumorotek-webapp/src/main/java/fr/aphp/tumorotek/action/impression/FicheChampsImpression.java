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
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheController;
import fr.aphp.tumorotek.decorator.BlocImpressionDecorator;
import fr.aphp.tumorotek.decorator.ChampImpressionDecorator;
import fr.aphp.tumorotek.decorator.ChampImpressionRowRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.impression.ChampEntiteBloc;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * @author mathieu
 * @version 2.2.3-genno
 */
public class FicheChampsImpression extends AbstractFicheController
{

   private static final long serialVersionUID = 7252909220936110139L;

   private Grid champsGrid;

   /**
    * Objets principaux.
    */
   private BlocImpressionDecorator blocImpressionDecorator;
   private List<ChampImpressionDecorator> champs = new ArrayList<>();
   private String path;

   private ChampImpressionRowRenderer champImpressionRenderer = new ChampImpressionRowRenderer();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      if(winPanel != null){
         winPanel.setHeight("327px");
      }

      getBinder().loadAll();
   }

   /**
    * Clique sur la checkbox d'un bloc.
    * @param event
    */
   public void onCheckChamp(final ForwardEvent event){
      // on récupère la checkbox associé à l'event
      final Checkbox box = (Checkbox) event.getOrigin().getTarget();
      // on récupère le bloc associé à l'event
      final ChampImpressionDecorator deco = (ChampImpressionDecorator) event.getData();

      if(box.isChecked()){
         deco.setImprimer(true);
      }else{
         deco.setImprimer(false);
      }
      coloriseRow((Row) box.getParent(), box.isChecked());
   }

   /**
    * Colore une ligne en fonction de si elle est à imprimer ou non.
    * @param row Row à colorier.
    * @param active True si la ligne doit être imprimée.
    */
   public void coloriseRow(final Row row, final Boolean active){
      if(active){
         row.setStyle(null);
      }else{
         row.setStyle("background-color : #E5E5E5;");
      }
      getBinder().loadComponent(row);
   }

   /**
    * Clique sur le bouton UP.
    * @param event
    */
   public void onClickUp(final ForwardEvent event){
      // on récupère le champ associé à l'event
      final ChampImpressionDecorator deco = (ChampImpressionDecorator) event.getData();
      upBloc(deco);
      getBinder().loadComponent(champsGrid);
   }

   /**
    * Clique sur le bouton Down.
    * @param event
    */
   public void onClickDown(final ForwardEvent event){
      // on récupère le champ associé à l'event
      final ChampImpressionDecorator deco = (ChampImpressionDecorator) event.getData();
      downBloc(deco);
      getBinder().loadComponent(champsGrid);
   }

   /**
    * Monte le bloc selectionné dans l'ordre de la liste.
    * @param deco
    */
   private void upBloc(final ChampImpressionDecorator deco){
      final int index = champs.indexOf(deco);
      ChampImpressionDecorator supObjectInList = null;
      if(index - 1 > -1){
         supObjectInList = champs.get(index - 1);
         champs.set(index, supObjectInList);
         champs.set(index - 1, deco);
      }
   }

   /**
    * Descend le deco selectionné dans la liste.
    * Equivaut à remonter le deco suivant le deco passé
    * en paramètre.
    * @param bloc
    */
   private void downBloc(final ChampImpressionDecorator deco){
      final int index = champs.indexOf(deco);
      if(index + 1 < champs.size()){
         upBloc(champs.get(index + 1));
      }
   }

   /**
    * Méthode appelée lors du clic sur le bouton validate : on va
    * sauvegarder la modification.
    */
   public void onClick$validate(){
      final List<ChampEntite> champsFinaux = new ArrayList<>();
      // on parcourt tous les champs et on ne conserve que ceux 
      // à imprimer
      for(int i = 0; i < champs.size(); i++){
         if(champs.get(i).getImprimer()){
            champsFinaux.add(champs.get(i).getChampEntite());
         }
      }
      blocImpressionDecorator.setChampEntites(champsFinaux);

      if(Path.getComponent(path) != null){
         // on envoie un event à cette page avec
         // le blocImpressionDecorator
         Events.postEvent(new Event("onGetChangeChampImpression", Path.getComponent(path), blocImpressionDecorator));
      }

      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Méthode appelée lors du clic sur le bouton revert : on ferme
    * la fenêtre.
    */
   public void onClick$revert(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public BlocImpressionDecorator getBlocImpressionDecorator(){
      return blocImpressionDecorator;
   }

   public void setBlocImpressionDecorator(final BlocImpressionDecorator bDecorator){
      this.blocImpressionDecorator = bDecorator;

      champs = new ArrayList<>();
      // on récupère les champs du bloc
      for(int i = 0; i < blocImpressionDecorator.getChampEntites().size(); i++){
         final ChampImpressionDecorator deco = new ChampImpressionDecorator(blocImpressionDecorator.getChampEntites().get(i));
         champs.add(deco);
      }

      // on extrait tous les champs disponibles
      final List<ChampEntiteBloc> cebs =
         ManagerLocator.getChampEntiteBlocManager().findByBlocManager(blocImpressionDecorator.getBlocImpression());
      
      // decoration
      for(int i = 0; i < cebs.size(); i++){
          if(!blocImpressionDecorator.getChampEntites().contains(cebs.get(i).getChampEntite())){
             final ChampImpressionDecorator deco = new ChampImpressionDecorator(cebs.get(i).getChampEntite());
             deco.setImprimer(false);
             champs.add(deco);
          }
       }
      
      // Vilain HACK !! contexte SEROLOGIE
      // suppr les champs échantillons
      if ("SEROLOGIE".equalsIgnoreCase(SessionUtils.getCurrentContexte().getNom())) {
	      if (blocImpressionDecorator.getBlocImpression().getNom().equals("bloc.prelevement.echantillons")) {
	    	  champs.remove(new ChampImpressionDecorator(new ChampEntite(ManagerLocator
					  .getEntiteManager().findByIdManager(3), "EchanQualiteId", null)));
			  champs.remove(new ChampImpressionDecorator(new ChampEntite(ManagerLocator
					  .getEntiteManager().findByIdManager(3), "AdicapOrganeId", null)));
			  champs.remove(new ChampImpressionDecorator(new ChampEntite(ManagerLocator
					  .getEntiteManager().findByIdManager(3), "CodeAssigneId", null)));
		  }
      }
     
      getBinder().loadComponent(self);
   }

   public List<ChampImpressionDecorator> getChamps(){
      return champs;
   }

   public void setChamps(final List<ChampImpressionDecorator> c){
      this.champs = c;
   }

   public ChampImpressionRowRenderer getChampImpressionRenderer(){
      return champImpressionRenderer;
   }

   public void setChampImpressionRenderer(final ChampImpressionRowRenderer cRenderer){
      this.champImpressionRenderer = cRenderer;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   @Override
   public TKdataObject getObject(){
      return null;
   }

   @Override
   public void setNewObject(){}

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

}
