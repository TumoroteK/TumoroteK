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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.webapp.tree.TumoTreeModel;
import fr.aphp.tumorotek.webapp.tree.export.ChampNode;
import fr.aphp.tumorotek.webapp.tree.export.ChampTreeItemRenderer;
import fr.aphp.tumorotek.webapp.tree.export.ChampsRootNode;

/**
 * Dessine la fiche modale permettant à l'utilisateur de choisir
 * les champs qu'il souhaite afficher.
 * Date: 07/04/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
public class FicheChampsAffichageModale extends GenericForwardComposer<Component>
{
   private static final long serialVersionUID = 4714314507345963886L;

   private Panel winPanel;

   private Tree champsAffichageTree;

   private Component parent;

   private Banque banque;

   /**
    * Variables pour l'arbre.
    */
   private TumoTreeModel ttm;

   private ChampTreeItemRenderer ctr = new ChampTreeItemRenderer();

   private List<Champ> oldSelectedChamps = new ArrayList<>();

   private AnnotateDataBinder binder;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);

      if(winPanel != null){
         winPanel.setHeight("465px");
      }
   }

   /**
    * 
    * @param oldSelectedChamps oldSelected anciens champs sélectionnés qui ne seront pas affichés
    * @param parent composent parent
    * @param banque banque
    * @param selectionMultiple activer la sélection multiple
    */
   public void init(final List<Champ> oldSelectedChamps, final Component parent, final Banque banque, final Boolean selectionMultiple){
      this.oldSelectedChamps = oldSelectedChamps;
      this.parent = parent;
      this.banque = banque;

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(initRoot());
      ttm.setMultiple(selectionMultiple);

      binder.loadComponent(champsAffichageTree);
   }
   
   /**
    * @param oldSelectedChamps anciens champs sélectionnés qui ne seront pas affichés
    * @param parent composent parent
    * @param banque Banque
    * @param selectionMultiple activer la sélection multiple
    * @param dataTypeList liste des datatypes à afficher
    * @param excludeIds exclure les champs numériques représentant un id
    */
   public void init(final List<Champ> oldSelectedChamps, final Component parent, final Banque banque, final Boolean selectionMultiple, List<DataType> dataTypeList, Boolean excludeIds){
      this.oldSelectedChamps = oldSelectedChamps;
      this.parent = parent;
      this.banque = banque;

      // Init de l'arbre et de son affichage
      ttm = new TumoTreeModel(initRoot(dataTypeList, excludeIds));
      ttm.setMultiple(selectionMultiple);

      binder.loadComponent(champsAffichageTree);
   }
   
   /**
    * Init du noeud root de l'arbre
    */
   private ChampsRootNode initRoot(){
   // Init du noeud root de l'arbre
      final ChampsRootNode root = new ChampsRootNode();
      root.setOldSelectedChamps(oldSelectedChamps);
      root.setBanque(banque);
      root.readChildren();
      
      return root;
   }
   
   /**
    * Init du noeud root de l'arbre
    * @param dataTypeList liste des datatypes à afficher
    * @param excludeIds exclure les champs numériques représentant un id
    */
   private ChampsRootNode initRoot(final List<DataType> dataTypeList, final Boolean excludeIds){
   // Init du noeud root de l'arbre
      final ChampsRootNode root = new ChampsRootNode();
      root.setOldSelectedChamps(oldSelectedChamps);
      root.setBanque(banque);
      root.setDataTypeList(dataTypeList);
      root.setExcludeIds(excludeIds);
      root.readChildren();
      
      return root;
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$select(){

      final List<Champ> chps = new ArrayList<>();

      final int[][] ints = ttm.getSelectionPaths();
      if(null != ints){
         Arrays.sort(ints, new Comparator<int[]>()
         {
            @Override
            public int compare(final int[] o1, final int[] o2){
               if(o1[0] == o2[0]){
                  return new Integer(o1[1]).compareTo(new Integer(o2[1]));
               }
               return new Integer(o1[0]).compareTo(new Integer(o2[0]));
            }
         });

         Treeitem it;
         for(int i = 0; i < ints.length; i++){
            it = champsAffichageTree.renderItemByPath(ints[i]);
            if(it.getValue().getClass().getSimpleName().equals("ChampNode")){
               chps.add(((ChampNode) it.getValue()).getChamp());
            }
         }
      }
      Events.postEvent("onGetChamps", getParent(), chps);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public TumoTreeModel getTtm(){
      return ttm;
   }

   public void setTtm(final TumoTreeModel t){
      this.ttm = t;
   }

   public ChampTreeItemRenderer getCtr(){
      return ctr;
   }

   public void setCtr(final ChampTreeItemRenderer c){
      this.ctr = c;
   }

   public List<Champ> getOldSelectedChamps(){
      return oldSelectedChamps;
   }

   public void setOldSelectedChamps(final List<Champ> oldSelected){
      this.oldSelectedChamps = oldSelected;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }
}
