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
package fr.aphp.tumorotek.component;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

import bsh.org.objectweb.asm.Label;

/**
 * Window modale representant une custom MessageBox permettant d'afficher
 * un paragraphe de détails sous le message d'erreur.
 * Utilisé pour détailler les doublons par collections.
 * Date: 30/03/2016
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class DynamicMultiLineMessageBox
{

   private String title;
   private String message;
   private Exception exception;

   private String exceptionDetails = null;

   @Wire
   private Label detailsLabel;

   private Boolean detailsLabelVisible = false;

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   @Init
   public void init(@ExecutionArgParam("title") final String _t, @ExecutionArgParam("message") final String _m,
      @ExecutionArgParam("exception") final Exception _e, @ExecutionArgParam("exceptionDetails") final String _s){
      this.title = _t;
      this.message = _m;
      this.exception = _e;
      this.exceptionDetails = _s;
   }

   @Command
   @NotifyChange({"detailsLabelVisible", "exceptionDetails"})
   public void details(){
      detailsLabelVisible = !detailsLabelVisible;

      if(exceptionDetails == null){
         if(exception instanceof DoublonFoundException){
            String label = "error.doublonfound.details";
            String banks = "";
            switch(((DoublonFoundException) exception).getEntite()){
               case "Prelevement":
                  final List<Prelevement> dbls = ManagerLocator.getPrelevementManager().findByCodeInPlateformeManager(
                     ((DoublonFoundException) exception).getCode(), SessionUtils.getCurrentPlateforme());
                  for(final Prelevement prelevement : dbls){
                     if(!banks.equals("")){
                        banks = banks + ", ";
                     }
                     banks = banks + prelevement.getBanque().getNom();
                  }
                  break;
               case "Echantillon":
                  final List<Echantillon> dblEs = ManagerLocator.getEchantillonManager().findByCodeInPlateformeManager(
                     ((DoublonFoundException) exception).getCode(), SessionUtils.getCurrentPlateforme());
                  for(final Echantillon echantillon : dblEs){
                     if(!banks.equals("")){
                        banks = banks + ", ";
                     }
                     banks = banks + echantillon.getBanque().getNom();
                  }
                  break;
               case "ProdDerive":
                  final List<ProdDerive> dblDs = ManagerLocator.getProdDeriveManager().findByCodeInPlateformeManager(
                     ((DoublonFoundException) exception).getCode(), SessionUtils.getCurrentPlateforme());
                  for(final ProdDerive derive : dblDs){
                     if(!banks.equals("")){
                        banks = banks + ", ";
                     }
                     banks = banks + derive.getBanque().getNom();
                  }
                  break;
               case "Cession":
                  label = "error.doublonfound.details.cession";
                  final List<Cession> dblCs = ManagerLocator.getCessionManager().findByNumeroInPlateformeManager(
                     ((DoublonFoundException) exception).getCode(), SessionUtils.getCurrentPlateforme());
                  for(final Cession cession : dblCs){
                     if(!banks.equals("")){
                        banks = banks + ", ";
                     }
                     banks = banks + cession.getBanque().getNom();
                  }
                  break;
               case "Patient":
                  final List<Patient> pats =
                     ManagerLocator.getPatientManager().findByNipLikeManager(((DoublonFoundException) exception).getCode(), true);
                  if(!pats.isEmpty()){
                     label = ObjectTypesFormatters.getLabel("validation.doublon.patient.nip",
                        new String[] {((DoublonFoundException) exception).getCode()});
                  }else{
                     label = Labels.getLabel("validation.doublon.patient");
                  }
               default:
                  break;
            }

            exceptionDetails = !banks.equals("")
               ? ObjectTypesFormatters.getLabel(label, new String[] {((DoublonFoundException) exception).getCode(), banks})
               : label;
         }
      }

   }

   public String getTitle(){
      return title;
   }

   public String getMessage(){
      return message;
   }

   public Boolean getDetailsLabelVisible(){
      return detailsLabelVisible;
   }

   public void setDetailsLabelVisible(final Boolean _d){
      this.detailsLabelVisible = _d;
   }

   public String getExceptionDetails(){
      return exceptionDetails;
   }

   public void setExceptionDetails(final String _e){
      this.exceptionDetails = _e;
   }
}
