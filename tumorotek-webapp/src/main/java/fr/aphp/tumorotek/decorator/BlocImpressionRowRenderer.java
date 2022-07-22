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

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.impression.Template;

/**
 * BlocImpressionRowRenderer affiche dans le Row
 * les membres du BlocImpression sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 28/07/2010
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class BlocImpressionRowRenderer implements RowRenderer<Object>
{

   private Boolean edit = false;

   private Template template;

   public BlocImpressionRowRenderer(final Boolean isEdit){
      edit = isEdit;
   }

   @Override
   public void render(final Row row, final Object data, final int index) throws Exception{
      final BlocImpressionDecorator bloc = (BlocImpressionDecorator) data;

      row.setHeight("100%");
      // détail
      createDetailContenu(row, bloc);

      // Nom du bloc
      final Label nomLabel = new Label(bloc.getNomForBloc());
      nomLabel.setStyle("font-weight: bold;" + "font-family: Verdana, Arial, Helvetica, sans-serif;");
      nomLabel.setParent(row);

      // si on est en mode édition
      if(edit){
         // checkbox d'impression
         final Checkbox check = new Checkbox();
         check.setChecked(bloc.getImprimer());
         check.addForward("onCheck", check.getParent(), "onCheckBloc", bloc);
         check.setParent(row);
         if(bloc.getImprimer()){
            row.setStyle(null);
         }else{
            row.setStyle("background-color : #E5E5E5;");
         }

         // arrow-up row
         if(row.getPreviousSibling() != null){
            final Image upImg = new Image();
            upImg.setStyle("cursor:pointer");
            upImg.setSrc("/images/icones/uparrow.png");
            upImg.setHeight("9px");
            upImg.addForward("onClick", upImg.getParent(), "onClickUp", bloc);
            upImg.setParent(row);
         }else{ // cell vide
            new Label().setParent(row);
         }

         // arrow-down row
         if(row.getNextSibling() != null){
            final Image downImg = new Image();
            downImg.setStyle("cursor:pointer");
            downImg.setSrc("/images/icones/downarrow.png");
            downImg.setHeight("9px");
            downImg.addForward("onClick", downImg.getParent(), "onClickDown", bloc);
            downImg.setParent(row);
         }else{ // cell vide
            new Label().setParent(row);
         }
      }
   }

   /**
    * Méthode qui crée la partie détail de la grille.
    * @param row Ligne actuelle.
    * @param bloc Bloc actuel.
    */
   public void createDetailContenu(final Row row, final BlocImpressionDecorator bloc){
      // partie détail
      final Detail detail = new Detail();

      // hbox
      final Hbox hbox = new Hbox();
      hbox.setParent(detail);

      // Titre du détail
      final Div divTitle = new Div();
      divTitle.setWidth("75px");
      String nomContenu = "";
      if(bloc.getBlocImpression() != null && bloc.getBlocImpression().getIsListe()){
         nomContenu = Labels.getLabel("blocImpression.colonnes");
      }else{
         nomContenu = Labels.getLabel("blocImpression.contenu");
      }
      final Label contenuTiteLabel = new Label(nomContenu);
      contenuTiteLabel.setParent(divTitle);
      contenuTiteLabel.setStyle("text-decoration : underline;");
      divTitle.setParent(hbox);

      // contenu du détail
      final Div divContenu = new Div();
      divContenu.setWidth("350px");
      final Label contenuLabel = new Label(bloc.getDescription());
      contenuLabel.setParent(divContenu);
      contenuLabel.setStyle("font-style : italic;");
      divContenu.setParent(hbox);

      if(edit && bloc.getBlocImpression() != null && bloc.getBlocImpression().getIsListe()){
         final Label linkEdit = new Label(Labels.getLabel("blocImpression.edit.champs"));
         linkEdit.setSclass("formLink");
         linkEdit.addForward("onClick", linkEdit.getParent(), "onClickEditChamps", bloc);
         linkEdit.setParent(hbox);
      }

      detail.setParent(row);
   }

   public Boolean getEdit(){
      return edit;
   }

   public void setEdit(final Boolean e){
      this.edit = e;
   }

   public Template getTemplate(){
      return template;
   }

   public void setTemplate(final Template t){
      this.template = t;
   }

}
