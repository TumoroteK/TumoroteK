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
package fr.aphp.tumorotek.action.administration.annotations;

import java.util.Iterator;
import java.util.Set;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.Item;

/**
 * AnnoItemRenderer affiche dans le Row
 * un item assigné à une annotation type thésaurus.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 14/05/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class AnnoItemRowRenderer implements RowRenderer<Object>
{

   private Set<AnnotationDefaut> defauts;

   public AnnoItemRowRenderer(){}

   public void setDefauts(final Set<AnnotationDefaut> defs){
      this.defauts = defs;
   }

   @Override
   public void render(final Row row, final Object data, final int index){

      final Item item = (Item) data;

      // label row
      final Label labLabel = new Label(item.getLabel());
      labLabel.setParent(row);
      // valeur row
      final Label valLabel = new Label(item.getValeur());
      valLabel.setParent(row);

      // defaut row
      final Label defLabel = new Label(ObjectTypesFormatters.booleanLitteralFormatter(isItemDefaut(item, defauts)));
      defLabel.setParent(row);

      // edit-validate row
      final Div editImg = new Div();
      editImg.setSclass("gridEdit");
      editImg.setHeight("12px");
      editImg.setWidth("12px");
      editImg.addForward("onClick", row.getParent(), "onClickUpdateItem", item);
      editImg.setParent(row);

      // revert-delete row
      final Div delImg = new Div();
      delImg.setSclass("gridDelete");
      delImg.setHeight("12px");
      delImg.setWidth("12px");
      delImg.addForward("onClick", row.getParent(), "onClickDeleteItem", item);
      delImg.setParent(row);
   }

   /**
    * @return true si item associé à valeur par défaut.
    */
   public static boolean isItemDefaut(final Item it, final Set<AnnotationDefaut> defs){
      if(it != null){
         final Iterator<AnnotationDefaut> itor = defs.iterator();
         while(itor.hasNext()){
            if(it.equals(itor.next().getItem())){
               return true;
            }
         }
      }
      return false;
   }

}
