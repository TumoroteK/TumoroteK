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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;

/**
 * AffectationRowRenderer affiche dans le Row
 * les membres d'Imprimante sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 28/03/2011
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class AffectationRowRenderer implements RowRenderer<Object>
{

   public AffectationRowRenderer(final Plateforme pf){
      plateforme = pf;
   }

   private Plateforme plateforme;
   private boolean canEdit = false;

   @Override
   public void render(final Row row, final Object data, final int index) throws Exception{
      final AffectationDecorator aff = (AffectationDecorator) data;

      final Listbox liImprimantes = new Listbox();
      final Listbox liModeles = new Listbox();
      final List<Object> datas = new ArrayList<>();
      datas.add(liImprimantes);
      datas.add(liModeles);
      datas.add(aff);

      // utilisateur
      String tmp = null;
      if(aff.isFirst()){
         tmp = aff.getUtilisateur().getLogin();
      }
      final Label userLabel = new Label(tmp);
      userLabel.setClass("formLabel");
      userLabel.setParent(row);

      // banque
      final Label banqueLabel = new Label(aff.getBanque().getNom());
      banqueLabel.setParent(row);

      AffectationImprimante affi = null;
      final List<AffectationImprimante> liste =
         ManagerLocator.getAffectationImprimanteManager().findByBanqueUtilisateurManager(aff.getBanque(), aff.getUtilisateur());
      if(liste.size() > 0){
         affi = liste.get(0);
      }
      // Imprimante
      tmp = null;
      // si le decorator n'est pas en edition
      if(!aff.isEdit()){
         if(affi != null){
            tmp = affi.getImprimante().getNom();
         }
         final Label imprimanteLabel = new Label(tmp);
         imprimanteLabel.setParent(row);
      }else{
         // sinon, on récupère les imprimantes de la pf
         final List<Imprimante> imps = ManagerLocator.getImprimanteManager().findByPlateformeManager(plateforme);
         imps.add(0, null);
         // on extrait leurs noms
         final List<String> impsString = new ArrayList<>();
         for(int i = 0; i < imps.size(); i++){
            if(imps.get(i) != null){
               impsString.add(imps.get(i).getNom());
            }else{
               impsString.add(null);
            }
         }
         // on affiche une listbox
         final ListModel<String> l = new ListModelList<String>(impsString);
         liImprimantes.setMold("select");
         liImprimantes.setRows(1);
         liImprimantes.setModel(l);
         liImprimantes.setWidth("130px");
         if(affi != null){
            liImprimantes.setSelectedIndex(imps.indexOf(affi.getImprimante()));
         }else{
            liImprimantes.setSelectedIndex(0);
         }
         liImprimantes.setParent(row);
      }

      // Modele
      tmp = null;
      // si le decorator n'est pas en edition
      if(!aff.isEdit()){
         if(affi != null && affi.getModele() != null){
            tmp = affi.getModele().getNom();
         }
         final Label modeleLabel = new Label(tmp);
         modeleLabel.setParent(row);
      }else{
         // sinon, on récupère les modèles de la pf
         final List<Modele> mods = ManagerLocator.getModeleManager().findByPlateformeManager(plateforme);
         mods.add(0, null);
         final List<String> modsString = new ArrayList<>();
         for(int i = 0; i < mods.size(); i++){
            if(mods.get(i) != null){
               modsString.add(mods.get(i).getNom());
            }else{
               modsString.add(null);
            }
         }
         // on affiche une listbox
         final ListModel<String> l = new ListModelList<String>(modsString);
         liModeles.setMold("select");
         liModeles.setRows(1);
         liModeles.setModel(l);
         liModeles.setWidth("130px");
         if(affi != null && affi.getModele() != null){
            liModeles.setSelectedIndex(mods.indexOf(affi.getModele()));
         }else{
            liModeles.setSelectedIndex(0);
         }
         liModeles.setParent(row);
      }

      // si le decorator n'est pas en edition
      // on affiche le bouton d'édition
      if(!aff.isEdit()){
         final Div divEdit = new Div();
         divEdit.setWidth("25px");
         divEdit.setAlign("center");
         // si l'utilisateur peut éditer cette affectation
         if(canEdit){
            final Div divImage = new Div();
            divImage.setWidth("12px");
            divImage.setHeight("12px");
            divImage.setSclass("gridEdit");
            divImage.addForward("onClick", divEdit.getParent(), "onClickEditAffectation", aff);
            divImage.setParent(divEdit);
         }
         divEdit.setParent(row);
      }else{
         // sinon on affiche le bouton de validation
         final Div divValidate = new Div();
         divValidate.setWidth("25px");
         divValidate.setAlign("center");
         final Div divImage = new Div();
         divImage.setWidth("12px");
         divImage.setHeight("12px");
         divImage.setSclass("gridValidate");
         divImage.addForward("onClick", divValidate.getParent(), "onClickValidateAffectation", datas);
         divImage.setParent(divValidate);
         divValidate.setParent(row);
      }

      // si le decorator n'est pas en edition
      // on affiche le bouton de délétion
      if(!aff.isEdit()){
         final Div divDelete = new Div();
         divDelete.setWidth("25px");
         divDelete.setAlign("center");
         // si l'utilisateur peut éditer cette affectation
         if(canEdit){
            final Div divImage = new Div();
            divImage.setWidth("12px");
            divImage.setHeight("12px");
            divImage.setSclass("gridDelete");
            divImage.addForward("onClick", divDelete.getParent(), "onClickDeleteAffectation", aff);
            divImage.setParent(divDelete);
         }
         divDelete.setParent(row);
      }else{
         // sinon on affiche le bouton d'annulation
         final Div divCancel = new Div();
         divCancel.setWidth("25px");
         divCancel.setAlign("center");
         final Div divImage = new Div();
         divImage.setWidth("12px");
         divImage.setHeight("12px");
         divImage.setSclass("gridCancel");
         divImage.addForward("onClick", divCancel.getParent(), "onClickCancelAffectation", aff);
         divImage.setParent(divCancel);
         divCancel.setParent(row);
      }

      // style pour uniquement différencier les lignes quand on
      // change d'utilisateur
      final StringBuffer rowStyle = new StringBuffer();
      if(!aff.isFirst()){
         rowStyle.append("border-top-color : #e2e9fe;");
      }
      if(!aff.isLast()){
         rowStyle.append("border-bottom-color : #e2e9fe;");
      }
      if(rowStyle.length() > 0){
         row.setStyle(rowStyle.toString());
      }
   }

   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme pf){
      this.plateforme = pf;
   }

   public boolean isCanEdit(){
      return canEdit;
   }

   public void setCanEdit(final boolean ce){
      this.canEdit = ce;
   }

}
