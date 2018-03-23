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
package fr.aphp.tumorotek.action.prelevement.serotk;

import java.util.Iterator;

import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prelevement.PrelevementRowRenderer;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;

/**
 * PrelevementRenderer affiche dans le Row
 * les membres de Prelevement sous forme de labels
 * pour le contexte Serotheque.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/03/2012
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 */
public class PrelevementSeroRowRenderer extends PrelevementRowRenderer
{

   public PrelevementSeroRowRenderer(final boolean select, final boolean c){
      super(select, c);
   }

   @Override
   public void render(final Row row, final Object data, final int index){

      // dessine le checkbox
      super.render(row, data, index);

   }

   @Override
   public void renderObjets(final Row row, final Object data){
      final Prelevement prel = (Prelevement) data;

      // init nbEchansRestants
      setNbEchansRestants(PrelevementUtils.getNbEchanRestants(prel));

      final Hbox icones = new Hbox();
      //infectieux
      final Iterator<Risque> risksIt = ManagerLocator.getPrelevementManager().getRisquesManager(prel).iterator();
      final Div bioHzd = new Div();
      while(risksIt.hasNext()){
         if(risksIt.next().getInfectieux()){
            bioHzd.setWidth("18px");
            bioHzd.setHeight("18px");
            bioHzd.setSclass("biohazard");
            break;
         }
      }
      bioHzd.setParent(icones);
      // non conformité
      if(prel != null && prel.getConformeArrivee() != null){
         final Div nonConf = new Div();
         nonConf.setWidth("18px");
         nonConf.setHeight("18px");
         if(prel.getConformeArrivee()){
            nonConf.setSclass("conformeArrivee");
         }else{
            nonConf.setSclass("nonConformeArrivee");
         }
         nonConf.setParent(icones);
      }

      // Dossier externe en attente
      // @since 2.0.13.1 pivot code prelevement ou numero labo
      if(prel != null && getEmetteurs().size() > 0){
         if(ManagerLocator.getDossierExterneManager().findByEmetteurInListAndIdentificationManager(getEmetteurs(), prel.getCode())
            .size() > 0
            || ManagerLocator.getDossierExterneManager()
               .findByEmetteurInListAndIdentificationManager(getEmetteurs(), prel.getNumeroLabo()).size() > 0){
            final Div nonDossier = new Div();
            nonDossier.setWidth("18px");
            nonDossier.setHeight("18px");
            nonDossier.setClass("dossierInbox");
            nonDossier.setParent(icones);
         }
      }
      icones.setParent(row);

      final Label codeLabel = new Label(prel.getCode());
      codeLabel.addForward(null, codeLabel.getParent(), "onClickObject", prel);
      codeLabel.setClass("formLink");
      codeLabel.setParent(row);
      if(prel.getMaladie() != null){
         if(anonyme){
            if(getAccessPatient()){
               final Label link = createAnonymeLink();
               link.addForward(null, link.getParent(), "onClickPatient", prel);
               link.setParent(row);
            }else{
               createAnonymeBlock().setParent(row);
            }
         }else{
            final Label patientLabel = new Label(PrelevementUtils.getPatientNomAndPrenom(prel));
            if(getAccessPatient()){
               patientLabel.addForward(null, patientLabel.getParent(), "onClickPatient", prel);
               patientLabel.setClass("formLink");
            }
            patientLabel.setParent(row);
         }
      }else{
         new Label().setParent(row);
      }
      if(prel.getMaladie() != null){
         final Label maladieLabel = new Label(prel.getMaladie().getLibelle());
         if(getAccessPatient()){
            maladieLabel.addForward(null, maladieLabel.getParent(), "onClickMaladie", prel);
            maladieLabel.setClass("formLink");
         }
         maladieLabel.setParent(row);
      }else{
         new Label().setParent(row);
      }
      new Label(ObjectTypesFormatters.dateRenderer2(prel.getDatePrelevement())).setParent(row);
      if(prel.getNature() != null){
         new Label(prel.getNature().getNature()).setParent(row);
      }else{
         new Label().setParent(row);
      }
      // protocoles : liste des protocoles
      if(prel.getDelegate() != null){
         ObjectTypesFormatters.drawProtocolesLabel(((PrelevementSero) prel.getDelegate()).getProtocoles(), row, null);
      }else{
         new Label().setParent(row);
      }

      new Label(getNbEchanRestantsSurTotalEtStockes(prel)).setParent(row);
      if(prel.getConsentType() != null){
         new Label(prel.getConsentType().getType()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      if(getNbEchansRestants() == 0){
         row.setStyle("background-color : #FEBAB3");
      }else if(getNbEchansRestants() == 1){
         row.setStyle("background-color : #FDDFA9");
      }
   }
}
