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
package fr.aphp.tumorotek.webapp.tree;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 * Classe gérant l'affichage des informations dans un arbre de type
 * TumoTreeModel contenant les collaborations médicales.
 * @author pierre
 *
 */
public class ContexteTreeitemRenderer implements TreeitemRenderer<Object>
{

   private List<Object> oldSelectedObjects = new ArrayList<>();

   @Override
   public void render(final Treeitem item, final Object data, final int index) throws Exception{
      item.setValue(data);

      // Construct treecells
      final Treecell tcNamn = new Treecell(data.toString());
      Treerow tr = null;

      if(item.getTreerow() == null){
         tr = new Treerow();
         tr.setParent(item);
      }else{
         tr = item.getTreerow();
         tr.getChildren().clear();
      }
      // Attach treecells to treerow
      tcNamn.setParent(tr);

      String nom = null;
      boolean archive = false;
      boolean oldSelected = false;

      if(data instanceof EtablissementNode){
         final EtablissementNode node = (EtablissementNode) data;
         nom = node.getEtablissement().getNom();
         archive = node.getEtablissement().getArchive();
         if(oldSelectedObjects.contains(node.getEtablissement())){
            oldSelected = true;
         }
      }else if(data instanceof ServiceNode){
         final ServiceNode node = (ServiceNode) data;
         nom = node.getService().getNom();
         archive = node.getService().getArchive();
         if(oldSelectedObjects.contains(node.getService())){
            oldSelected = true;
         }
      }else if(data instanceof CollaborateurNode){
         final CollaborateurNode node = (CollaborateurNode) data;
         nom = node.getCollaborateur().getNomAndPrenom();
         archive = node.getCollaborateur().getArchive();
         if(oldSelectedObjects.contains(node.getCollaborateur())){
            oldSelected = true;
         }
      }

      if(nom != null){
         item.setLabel(nom);
      }

      if(archive && oldSelected){
         tcNamn.setStyle("font-style:italic;color:#07811C;font-weight: bold;");
      }else if(archive && !oldSelected){
         tcNamn.setStyle("font-style:italic;color:#7F7F7F;");
      }else if(!archive && oldSelected){
         tcNamn.setStyle("color:#07811C;font-weight: bold;");
      }

      item.getTreerow().setDraggable("true");
      item.getTreerow().setDroppable("true");
      item.setOpen(false);

      // gère l'ouverture d'un Treeitem lors du clic sur le
      // nom de celui-ci
      tcNamn.addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            final Treeitem item = (Treeitem) event.getTarget().getParent().getParent();

            if(item.isOpen()){
               item.setOpen(false);
            }else{
               item.setOpen(true);
            }
         }
      });

   }

   public List<Object> getOldSelectedObjects(){
      return oldSelectedObjects;
   }

   public void setOldSelectedObjects(final List<Object> oldSelected){
      this.oldSelectedObjects = oldSelected;
   }

}
