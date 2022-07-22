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
package fr.aphp.tumorotek.action.code;

import java.util.List;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.code.CodeCommon;

/**
 * CodeRendere affiche dans le Row
 * les membres des classes heritant CodeCommon dans les labels.
 *
 * Affiche les boutons fleche-haut et fleche-bas qui permettent
 * d'ordonner la liste.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 10/06/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class CodesSelectedRowRenderer implements RowRenderer<Object>
{

   private boolean upAndDown = true;

   public CodesSelectedRowRenderer(){}

   public void setUpAndDown(final boolean b){
      this.upAndDown = b;
   }

   @Override
   public void render(final Row row, final Object data, final int index){

      final CodeCommon code = (CodeCommon) data;

      // checkbox
      final Checkbox checkCode = new Checkbox();
      checkCode.addForward("onCheck", row.getParent(), "onCheckObject", code);
      checkCode.setChecked(((List<CodeCommon>) row.getParent().getAttribute("checkedCodes")).contains(data));
      checkCode.setParent(row);

      new Label(code.getCode()).setParent(row);
      new Label(code.getLibelle()).setParent(row);

      // delete row
      final Image delImg = new Image();
      delImg.setStyle("cursor:pointer");
      delImg.setSrc("/images/icones/small_delete.png");
      delImg.addForward("onClick", row.getParent(), "onClickDeleteCode", code);
      delImg.setParent(row);

      if(upAndDown){
         // arrow-up row
         if(row.getPreviousSibling() != null){
            final Image upImg = new Image();
            upImg.setStyle("cursor:pointer");
            upImg.setSrc("/images/icones/uparrow.png");
            upImg.setHeight("8px");
            upImg.addForward("onClick", row.getParent(), "onClickUpCode", code);
            upImg.setParent(row);
         }else{ // cell vide
            new Label().setParent(row);
         }

         // arrow-down row
         if(row.getNextSibling() != null){
            final Image downImg = new Image();
            downImg.setStyle("cursor:pointer");
            downImg.setSrc("/images/icones/downarrow.png");
            downImg.setHeight("8px");
            downImg.addForward("onClick", row.getParent(), "onClickDownCode", code);
            downImg.setParent(row);
         }else{ // cell vide
            new Label().setParent(row);
         }
      }
   }
}
