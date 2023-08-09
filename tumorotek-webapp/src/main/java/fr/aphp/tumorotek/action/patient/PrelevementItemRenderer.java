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
package fr.aphp.tumorotek.action.patient;

import java.util.List;

import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * PrelevementRenderer affiche dans le listitem
 * les membres de Prelevement.
 * Le membre otherConsultBanks recoit la liste de banque dont l'acces
 * en consultation des prélèvements est autorisé.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 */
public class PrelevementItemRenderer implements ListitemRenderer<Prelevement>
{

   private List<Banque> otherConsultBanks = null;

   private boolean accessible = false;

   public void setFromOtherConsultBanks(final List<Banque> oBks){
      this.otherConsultBanks = oBks;
   }

   @Override
   public void render(final Listitem li, final Prelevement data, final int index){

      final Prelevement prel = data;

      // icones
      final Listcell rCell = new Listcell();
      final Hlayout icones = PrelevementUtils.drawListIcones(prel);
      rCell.appendChild(icones);
      rCell.setParent(li);

      // date prelevement
      new Listcell(ObjectTypesFormatters.dateRenderer2(prel.getDatePrelevement())).setParent(li);

      // code prelevement
      final Listcell codeCell = new Listcell(prel.getCode());
      codeCell.setParent(li);
      if(isAccessible() || (getOtherConsultBanks() != null && getOtherConsultBanks().contains(prel.getBanque()))){
         codeCell.addForward(null, li.getParent(), "onClickPrelevementCode", prel);
         codeCell.setClass("formLink");
      }

      // foreign bank
      if(getOtherConsultBanks() != null){ // mode otherBank prelevement
         new Listcell(prel.getBanque().getNom()).setParent(li);
      }

      // nature
      if(prel.getNature() != null){
         new Listcell(prel.getNature().getNom()).setParent(li);
      }else{
         new Listcell().setParent(li);
      }

      // type
      if(prel.getPrelevementType() != null){
         new Listcell(prel.getPrelevementType().getNom()).setParent(li);
      }else{
         new Listcell().setParent(li);
      }

      // affiche diagnostic anapath
      if(getOtherConsultBanks() == null){
         // organe
         ObjectTypesFormatters.drawCodesExpLabel(
            ManagerLocator.getCodeAssigneManager().findFirstCodesOrgByPrelevementManager(prel), null, li, true);

         // diagnostic
         ObjectTypesFormatters.drawCodesExpLabel(
            ManagerLocator.getCodeAssigneManager().findFirstCodesLesByPrelevementManager(prel), null, li, true);

      }else{ // pour foreign bank, le service preleveur
         new Listcell(prel.getServicePreleveur() != null ? prel.getServicePreleveur().getEtablissement().getNom() : "")
            .setParent(li);
      }

      // statut juridique
      if(prel.getConsentType() != null){
         new Listcell(prel.getConsentType().getNom()).setParent(li);
      }else{
         new Listcell().setParent(li);
      }
      new Listcell(PrelevementUtils.getNbEchanRestantsSurTotalEtStockes(prel)).setParent(li);

      switch(PrelevementUtils.getNbEchanRestants(prel)){
         case 0:
            li.setStyle("background-color : #FF8678");
            break;
         case 1:
            li.setStyle("background-color : #FFCB6B");
            break;
         default:
            break;
      }
   }

   public boolean isAccessible(){
      return accessible;
   }

   public void setAccessible(final boolean acc){
      this.accessible = acc;
   }

   public List<Banque> getOtherConsultBanks(){
      return otherConsultBanks;
   }

   public void setOtherConsultBanks(final List<Banque> ob){
      this.otherConsultBanks = ob;
   }
}
