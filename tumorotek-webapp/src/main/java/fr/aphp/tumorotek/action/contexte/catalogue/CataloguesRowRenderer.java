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
package fr.aphp.tumorotek.action.contexte.catalogue;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Box;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;

/**
 * Catalogues renderer affiche dans le Row
 * les catalogues du système.
 *
 * Sélectionne les checkboxes pour les catalogues assignés à la banque
 * courante et rend impossible toute modification pour les
 * catalogues impliquant une association avec les tables d'annotations.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 10/10/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class CataloguesRowRenderer implements RowRenderer<Catalogue>
{

   public CataloguesRowRenderer(){}

   
   @Override
   public void render(final Row row, final Catalogue data, final int index){

      final Catalogue catalogue = data;

      if(row.getParent().getAttribute("checkedCatalogues") != null){
         // checkbox
         final Checkbox checkCat = new Checkbox();
         checkCat.addForward("onCheck", row.getParent(), "onCheckCatalogue", catalogue);
         checkCat.setChecked(((List<Catalogue>) row.getParent().getAttribute("checkedCatalogues")).contains(data));
         final List<Catalogue> cats = new ArrayList<>();
         cats.add(catalogue);
         // checkCat.setDisabled(!ManagerLocator.getTableAnnotationManager()
         //					.findByCataloguesManager(cats).isEmpty() 
         //					&& checkCat.isChecked());
         checkCat.setParent(row);
      }

      final Box b = new Box();
      b.setOrient("horizontal");
      b.setSpacing("5px");
      //b.setValign("middle");
      b.setPack("center");

      // Image icon = new Image();
      // icon.setSrc(catalogue.getIcone());
      ///icon.setParent(b);
      final Div icon = new Div();
      icon.setWidth("65px");
      icon.setHeight("35px");
      icon.setSclass(catalogue.getIcone());
      icon.setParent(b);

      final Label nom = new Label();
      nom.setSclass("formValue");
      nom.setValue(catalogue.getNom());
      nom.setParent(b);

      b.setParent(row);
   }
}
