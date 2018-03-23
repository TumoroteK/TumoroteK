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
package fr.aphp.tumorotek.action.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.impression.FicheChampsImpression;
import fr.aphp.tumorotek.decorator.BlocImpressionDecorator;

public abstract class AbstractImpressionController extends AbstractFicheCombineController
{

   private static final long serialVersionUID = 1L;

   private boolean blockModal = false;

   /**
    * Edition des blocs d'impression
    */
   protected Grid contenuEditGrid;

   protected List<BlocImpressionDecorator> blocImpressionsDecorated = new ArrayList<>();

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * pour modifier les champs à imprimer. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection de ces champs.
    * @param page dans laquelle inclure la modale
    * @param deco BlocImpressionDecorator pour lequel on va modifier
    * les champs.
    * @param path vers le composant parent declenchant la modal.
    */
   public void openChampsWindow(final Page page, final BlocImpressionDecorator deco, final String path){
      if(!blockModal){

         blockModal = true;

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("aideWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("blocImpression.selection.colonnes"));
         win.setBorder("normal");
         win.setWidth("400px");
         win.setHeight("400px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateChampsModal(win, page, deco, path);
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            blockModal = false;

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   private static HtmlMacroComponent populateChampsModal(final Window win, final Page page, final BlocImpressionDecorator deco,
      final String path){
      // HtmlMacroComponent contenu dans la fenêtre : il correspond
      // au composant des collaborations.
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("champImpression", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openChampImpression");
      ua.applyProperties();
      ua.afterCompose();

      ((FicheChampsImpression) ua.getFellow("winChampImpression").getAttributeOrFellow("winChampImpression$composer", true))
         .setBlocImpressionDecorator(deco);

      ((FicheChampsImpression) ua.getFellow("winChampImpression").getAttributeOrFellow("winChampImpression$composer", true))
         .setPath(path);

      return ua;
   }

   /**
    * Monte le bloc selectionné dans l'ordre de la liste.
    * @param bloc
    */
   public void upBloc(final BlocImpressionDecorator deco){
      final int blocIndex = blocImpressionsDecorated.indexOf(deco);
      BlocImpressionDecorator supObjectInList = null;
      if(blocIndex - 1 > -1){
         supObjectInList = blocImpressionsDecorated.get(blocIndex - 1);
         blocImpressionsDecorated.set(blocIndex, supObjectInList);
         blocImpressionsDecorated.set(blocIndex - 1, deco);
      }
   }

   /**
    * Descend le bloc selectionné dans la liste.
    * Equivaut à remonter le bloc suivant le bloc passé
    * en paramètre.
    * @param bloc
    */
   public void downBloc(final BlocImpressionDecorator deco){
      final int blocIndex = blocImpressionsDecorated.indexOf(deco);
      if(blocIndex + 1 < blocImpressionsDecorated.size()){
         upBloc(blocImpressionsDecorated.get(blocIndex + 1));
      }
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
    * Clique sur la checkbox d'un bloc.
    * @param event
    */
   public void onCheckBloc(final ForwardEvent event){
      // on récupère la checkbox associé à l'event
      final Checkbox box = (Checkbox) event.getOrigin().getTarget();
      // on récupère le bloc associé à l'event
      final BlocImpressionDecorator deco = (BlocImpressionDecorator) event.getData();

      if(box.isChecked()){
         deco.setImprimer(true);
      }else{
         deco.setImprimer(false);
      }
      coloriseRow((Row) box.getParent(), box.isChecked());
   }

   /**
    * Clique sur le bouton Down.
    * @param event
    */
   public void onClickDown(final ForwardEvent event){
      // on récupère le bloc associé à l'event
      final BlocImpressionDecorator deco = (BlocImpressionDecorator) event.getData();
      downBloc(deco);
      getBinder().loadComponent(contenuEditGrid);
   }

   /**
    * Clique sur le bouton Down.
    * @param event
    */
   public void onClickEditChamps(final ForwardEvent event){
      // on récupère le bloc associé à l'event
      final BlocImpressionDecorator deco = (BlocImpressionDecorator) event.getData();

      openChampsWindow(page, deco, Path.getPath(self));
   }

   /**
    * Méthode qui récupère l'évenement quand l'utilisateur a fini de
    * modifier la liste des champs.
    * @param e
    */
   public void onGetChangeChampImpression(final Event e){
      final BlocImpressionDecorator deco = (BlocImpressionDecorator) e.getData();

      deco.updateListeChamps();
      getBinder().loadComponent(contenuEditGrid);
   }

   /**
    * Clique sur le bouton UP.
    * @param event
    */
   public void onClickUp(final ForwardEvent event){
      // on récupère le bloc associé à l'event
      final BlocImpressionDecorator deco = (BlocImpressionDecorator) event.getData();
      upBloc(deco);
      getBinder().loadComponent(contenuEditGrid);
   }

   /**
    * Vérifie qu'il y a au moins bloc à imprimer.
    * @return True si l'impression est valide.
    */
   public boolean checksBlocValid(){
      int nb = 0;
      for(int i = 0; i < blocImpressionsDecorated.size(); i++){
         if(blocImpressionsDecorated.get(i).getImprimer()){
            ++nb;
         }
      }

      return (nb > 0);
   }

   /**
    * Arrondi d'un double avec n éléments après la virgule.
    * @param a La valeur à convertir.
    * @param n Le nombre de décimales à conserver.
    * @return La valeur arrondi à n décimales.
    */
   public static float floor(final float a, final int n){
      final double p = Math.pow(10.0, n);
      return (float) (Math.floor((a * p) + 0.5) / p);
   }

   @Override
   public boolean isBlockModal(){
      return blockModal;
   }

   @Override
   public void setBlockModal(final boolean block){
      this.blockModal = block;
   }

   public List<BlocImpressionDecorator> getBlocImpressionsDecorated(){
      return blocImpressionsDecorated;
   }

   public void setBlocImpressionsDecorated(final List<BlocImpressionDecorator> bDecorated){
      this.blocImpressionsDecorated = bDecorated;
   }

}
