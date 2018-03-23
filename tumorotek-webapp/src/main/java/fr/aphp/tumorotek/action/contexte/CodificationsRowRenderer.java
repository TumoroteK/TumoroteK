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

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;

/**
 * CodificationsRenderer affiche dans le Row
 * les membres des classes heritant BanqueTableCodage dans les labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 10/11/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class CodificationsRowRenderer implements RowRenderer<BanqueTableCodage>
{

   public CodificationsRowRenderer(){}

   @Override
   public void render(final Row row, final BanqueTableCodage btc, final int index){

      // BanqueTableCodage btc = (BanqueTableCodage) data;

      final Label link = new Label();
      link.setSclass("formValue");
      link.setValue(btc.getTableCodage().getNom());
      link.setParent(row);

      final Label staticlink = new Label();
      staticlink.setSclass("formValue");
      staticlink.setValue(btc.getTableCodage().getNom());
      staticlink.setParent(row);

      final Label version = new Label();
      version.setSclass("formValue");
      version.setValue(btc.getTableCodage().getVersion());
      version.setParent(row);

      final Label export = new Label();
      export.setSclass("formValue");
      if(!btc.getLibelleExport()){
         export.setValue("[" + Labels.getLabel("code.code") + "]");
      }else{
         export.setValue("[" + Labels.getLabel("code.libelle") + "]");
      }
      export.setParent(row);

      // delete row
      final Image delImg = new Image();
      delImg.setWidth("12px");
      delImg.setHeight("12px");
      delImg.setStyle("cursor:pointer");
      delImg.setSrc("/images/icones/small_delete.png");
      delImg.addForward("onClick", row.getParent(), "onDeleteObj", btc);
      delImg.setParent(row);
   }
}
