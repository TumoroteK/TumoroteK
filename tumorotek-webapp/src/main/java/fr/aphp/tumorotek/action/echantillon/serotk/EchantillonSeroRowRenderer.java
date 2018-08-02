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
package fr.aphp.tumorotek.action.echantillon.serotk;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.echantillon.EchantillonRowRenderer;
import fr.aphp.tumorotek.action.utils.TKStockableObjectUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;

/**
 * EchantillonRenderer affiche dans le Row
 * les membres d'Echantillon sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/04/2010
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class EchantillonSeroRowRenderer extends EchantillonRowRenderer
{
   
   private final Log log = LogFactory.getLog(EchantillonSeroRowRenderer.class);

   public EchantillonSeroRowRenderer(boolean select, boolean cols){
      super(select, cols);
   }

   @Override
   public void render(final Row row, final Echantillon data, final int index){

      log.debug("init render");

      // dessine le checkbox
      drawCheckbox(row, data, index);

      log.debug("super");

      final Echantillon echan = data;

      final Hlayout icones = TKStockableObjectUtils.drawListIcones(echan, null, null);

      //infectieux
      final Prelevement prel = ManagerLocator.getEchantillonManager().getPrelevementManager(echan);
      if(prel != null){
         final Iterator<Risque> risksIt = ManagerLocator.getPrelevementManager().getRisquesManager(prel).iterator();
         final Div bioHzd = new Div();
         boolean risky = false;
         String risks = "";
         Risque risque;
         while(risksIt.hasNext()){
            risque = risksIt.next();
            if(risque.getInfectieux()){
               risky = true;
               risks = risks + " " + risque.getNom();
            }
         }
         if(risky){
            bioHzd.setWidth("18px");
            bioHzd.setHeight("18px");
            bioHzd.setSclass("biohazard");
            bioHzd.setTooltiptext(ObjectTypesFormatters.getLabel("tooltip.risque", new String[] {risks}));
         }

         icones.insertBefore(bioHzd, icones.getFirstChild());
      }
      log.debug("risque");

      icones.setParent(row);

      // code
      final Label codeLabel = new Label(echan.getCode());
      if(isAccessible()){
         codeLabel.addForward(null, codeLabel.getParent(), "onClickObject", echan);
         codeLabel.setClass("formLink");
      }
      codeLabel.setParent(row);
      log.debug("code");

      if(isTtesCollections()){
         new Label(echan.getBanque().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // patient
      if(!isEmbedded()){
         if(!anonyme){
            new Label(getPatient(echan)).setParent(row);
         }else{
            createAnonymeBlock().setParent(row);
         }
      }

      // date de stockage
      new Label(ObjectTypesFormatters.dateRenderer2(echan.getDateStock())).setParent(row);
      log.debug("date");

      // délai congélation
      new Label(getDelaiCgl(echan)).setParent(row);
      
      log.debug("délai de congélation");

      // type
      if(echan.getEchantillonType() != null){
         new Label(echan.getEchantillonType().getType()).setParent(row);
      }else{
         new Label().setParent(row);
      }
      log.debug("type");

      // quantité
      new Label(getQuantite(echan)).setParent(row);
      log.debug("quantite");

      // objet statut
      if(echan.getObjetStatut() != null){
         final Label statut = new Label(ObjectTypesFormatters.ILNObjectStatut(echan.getObjetStatut()));
         if(echan.getObjetStatut().getStatut().equals("ENCOURS")){
            statut.setStyle("color: red");
         }
         statut.setParent(row);
      }else{
         new Label().setParent(row);
      }
      log.debug("objet statut");

      // emplacement
      if(!isAnonyme() && isAccessStockage()){
         final Label emplLabel = new Label(getEmplacementAdrl(echan));
         if(isAccessStockage()){
            emplLabel.setSclass("formLink");
            emplLabel.addForward(null, emplLabel.getParent(), "onClickObjectEmplacement",
               ManagerLocator.getEchantillonManager().getEmplacementManager(echan));
         }
         emplLabel.setParent(row);
      }else{
         createAnonymeBlock().setParent(row);
      }

      // nb prodderives
      new Label(String.valueOf(getNbDerives(echan))).setParent(row);

      // nb cessions
      new Label(String.valueOf(getNbCessions(echan))).setParent(row);
      log.debug("cessions");
   }
}
