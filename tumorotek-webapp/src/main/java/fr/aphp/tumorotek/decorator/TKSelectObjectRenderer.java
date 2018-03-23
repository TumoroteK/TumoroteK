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
package fr.aphp.tumorotek.decorator;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;

/**
 * Classe parente des renderers utilisés dans les listes d'affichages des
 * objets principaux coeur de TK.
 * @since 2.0.10 possède une référence vers la selection qui permet
 * de cocher les boxes si conservés entre deux rendering (exemple: utilisation de
 * la fonction de tri)
 *
 * @author mathieu
 * @version 2.0.10
 *
 */
public class TKSelectObjectRenderer implements RowRenderer<Object>
{

   private boolean selectionMode = true;
   private boolean checkAll = false;
   protected boolean anonyme = false;
   private boolean ttesCollections = false;
   private List<? extends Object> selectedObjects;

   public TKSelectObjectRenderer(){
      super();
   }

   @Override
   public void render(final Row row, final Object data, final int index){

      if(selectionMode){
         // checkbox
         final Checkbox check = new Checkbox();
         if(checkAll || (selectedObjects != null && selectedObjects.contains(data))){
            check.setChecked(true);
         }
         check.addForward("onCheck", check.getParent(), "onCheckObject", data);
         check.setParent(row);
      }
   }

   /**
    * Récupère la date de création système du patient.
    * @param Parent
    * @return Date de création.
    */
   protected String getDateCreation(final Object obj){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(obj);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }else{
         return null;
      }
   }

   /**
    * Crée un bloc anonyme pour cacher la valeur d'un champ.
    * @return
    */
   protected Label createAnonymeBlock(){
      final Label anonymeLabel = new Label();
      AbstractController.makeLabelAnonyme(anonymeLabel, false);

      return anonymeLabel;
   }

   /**
    * Crée un lien anonyme pour cacher la valeur d'un champ.
    * @return
    */
   protected Label createAnonymeLink(){

      final Label anonymeLabel = new Label();
      AbstractController.makeLabelAnonyme(anonymeLabel, true);

      return anonymeLabel;
   }

   /**
    * Dessine dans un label le ou les libelles 
    * l'utilisation d'un tooltip pour afficher la totalité.
    * @param
    * @param row Parent
    */
   protected void drawListLabel(final List<String> liste, final Row row, final Component parent){

      if(!liste.isEmpty()){
         final Label label1 = new Label(liste.get(0));
         // dessine le label avec un lien vers popup 
         if(liste.size() > 1){
            final Hbox labelAndLinkBox = new Hbox();
            labelAndLinkBox.setSpacing("5px");
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup popUp = new Popup();
            popUp.setParent(parent);
            final Iterator<String> it = liste.iterator();
            String label = null;
            Label libelleStaticLabel = null;
            final Vbox popupVbox = new Vbox();
            while(it.hasNext()){
               label = it.next();
               libelleStaticLabel = new Label(label);
               libelleStaticLabel.setSclass("formValue");

               popupVbox.appendChild(libelleStaticLabel);
            }
            popUp.appendChild(popupVbox);
            moreLabel.setTooltip(popUp);
            labelAndLinkBox.appendChild(label1);
            labelAndLinkBox.appendChild(moreLabel);
            labelAndLinkBox.setParent(row);
         }else{
            label1.setParent(row);
         }
      }else{
         new Label().setParent(row);
      }
   }

   public boolean isSelectionMode(){
      return selectionMode;
   }

   public void setSelectionMode(final boolean selection){
      this.selectionMode = selection;
   }

   public boolean isCheckAll(){
      return checkAll;
   }

   public void setCheckAll(final boolean check){
      this.checkAll = check;
   }

   public boolean isAnonyme(){
      return anonyme;
   }

   public void setAnonyme(final boolean ano){
      this.anonyme = ano;
   }

   public boolean isTtesCollections(){
      return ttesCollections;
   }

   public void setTtesCollections(final boolean c){
      this.ttesCollections = c;
   }

   public void setSelectedObjects(final List<? extends Object> s){
      this.selectedObjects = s;
   }

}
