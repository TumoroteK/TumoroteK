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
package fr.aphp.tumorotek.action.imprimante;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;

/**
 * LigneEtiquetteRowRenderer affiche dans le Row
 * les membres d'un champ  d'étiquette.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 15/06/2011
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class ChampLigneEtiquetteRowRenderer implements RowRenderer<ChampLigneEtiquette>
{

   public ChampLigneEtiquetteRowRenderer(){

   }

   @Override
   public void render(final Row row, final ChampLigneEtiquette data, final int index) throws Exception{
      final ChampLigneEtiquette cle = data;

      // champ
      String champ = "";
      if(cle.getChamp().getChampAnnotation() != null){
         champ = cle.getChamp().getChampAnnotation().getNom();
      }else{
         champ = ObjectTypesFormatters.getLabelForChampEntite(cle.getChamp().getChampEntite());
      }
      final Label champLabel = new Label(champ);
      champLabel.setParent(row);

      // ExpReg
      final StringBuffer sb = new StringBuffer();
      if(cle.getExpReg() != null){
         if(cle.getExpReg().contains(">")){
            sb.append(Labels.getLabel("fiche.ligne.etiquette.formatage.apres"));
            sb.append(" ");
            sb.append(cle.getExpReg().substring(1));
         }else if(cle.getExpReg().contains("<")){
            sb.append(Labels.getLabel("fiche.ligne.etiquette.formatage.avant"));
            sb.append(" ");
            sb.append(cle.getExpReg().substring(1));
         }else if(cle.getExpReg().contains("[")){
            sb.append(Labels.getLabel("fiche.ligne.etiquette.formatage.entre"));
            sb.append(" ");
            sb.append(cle.getExpReg().substring(1, cle.getExpReg().indexOf(",")));
            sb.append(" à ");
            sb.append(cle.getExpReg().substring(cle.getExpReg().indexOf(",") + 1, cle.getExpReg().indexOf("]")));
         }
      }else{
         sb.append(Labels.getLabel("fiche.ligne.etiquette.formatage.aucun"));
      }
      final Label expLabel = new Label(sb.toString());
      expLabel.setParent(row);

      // Entite
      final Label entiteLabel = new Label(cle.getEntite().getNom());
      entiteLabel.setParent(row);

      // div up
      final Div up = new Div();
      up.setWidth("12px");
      up.setHeight("12px");
      up.setSclass("upArrow");
      up.setStyle("cursor:pointer");
      up.addForward("onClick", up.getParent(), "onClickUpLigne", cle);
      up.setParent(row);

      // div down
      final Div down = new Div();
      down.setWidth("12px");
      down.setHeight("12px");
      down.setSclass("downArrow");
      down.setStyle("cursor:pointer");
      down.addForward("onClick", down.getParent(), "onClickDownLigne", cle);
      down.setParent(row);

      // div delete
      final Div divDelete = new Div();
      divDelete.setAlign("center");
      divDelete.setParent(row);
      final Div delete = new Div();
      delete.setWidth("12px");
      delete.setHeight("12px");
      delete.setSclass("gridDelete");
      delete.setStyle("cursor:pointer");
      delete.addForward(null, delete.getParent(), "onClickDeleteLigne", cle);
      delete.setParent(divDelete);
   }

}
