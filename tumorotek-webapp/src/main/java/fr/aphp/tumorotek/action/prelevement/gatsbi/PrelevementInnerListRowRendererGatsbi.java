/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.List;

import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.gatsbi.RowRendererGatsbiOtherConsultBanks;

/**
 * Controller gérant le rendu dynamique des lignes du tableau prélèvement sous
 * le gestionnaire GATSBI, pour les listes de prélèvements internes au bloc Maladie.
 * Donc étend et modifie le comportement du renderer Gatsi Liste prelevement en :
 *  - retirant la notion d'emetteur interfacage de la première colonne
 *  - supprimant les références patient / maladie
 *  - en modifiant l'evt de selection d'un prélèvement
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsi
 */
public class PrelevementInnerListRowRendererGatsbi extends PrelevementRowRendererGatsbi
   implements RowRendererGatsbiOtherConsultBanks
{

   private boolean accessible = false;
   
   private List<Banque> otherConsultBanks = null;

   public PrelevementInnerListRowRendererGatsbi(final boolean select, final boolean cols){
      super(select, cols);
   }

   @Override
   public void renderObjets(final Row row, final Object data){
      final Prelevement prel = (Prelevement) data;

      // init nbEchansRestants
      setNbEchansRestants(PrelevementUtils.getNbEchanRestants(prel));

      // @since gatsbi, icones peuvent ne jamais s'afficher
      // icones
      if(areIconesRendered()){
         final Hlayout icones = PrelevementUtils.drawListIcones(prel);
         icones.setParent(row);
      }

      // code prelevement
      final Label codeLabel = new Label(prel.getCode());
      codeLabel.setParent(row);
      if(isAccessible() || (getOtherConsultBanks() != null && getOtherConsultBanks().contains(prel.getBanque()))){
         codeLabel.setClass("formLink");
         codeLabel.addForward(null, codeLabel.getParent().getParent().getParent(), "onClickPrelevementCode", prel);
      }

      if(isTtesCollections()){
         new Label(prel.getBanque().getNom()).setParent(row);
      }

      try{
         renderPrelevement(row, prel);
      }catch(final Exception e){
         // une erreur inattendue levée dans la récupération
         // ou le rendu d'une propriété prel
         // va arrêter le rendu du reste du tableau
         throw new RuntimeException(e);
      }

      // row style
      if(getNbEchansRestants() == 0){
         row.setStyle("background-color : #FF8678");
      }else if(getNbEchansRestants() == 1){
         row.setStyle("background-color : #FFCB6B");
      }
   }

   @Override
   public List<Banque> getOtherConsultBanks(){
      return otherConsultBanks;
   }

   @Override
   public void setOtherConsultBanks(final List<Banque> ob){
      this.otherConsultBanks = ob;
   }

   @Override
   public boolean isAccessible(){
      return accessible;
   }

   @Override
   public void setAccessible(final boolean acc){
      this.accessible = acc;
   }
}
