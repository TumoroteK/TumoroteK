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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;

import bsh.org.objectweb.asm.Label;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.MultipleDoublonFoundException;
import fr.aphp.tumorotek.manager.impl.coeur.patient.PatientDoublonFound;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

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

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
   }

   @Init
   public void init(@ExecutionArgParam("title") final String _t, @ExecutionArgParam("message") final String _m,
      @ExecutionArgParam("exception") final Exception _e){
      this.title = _t;
      this.message = _m;
      this.exception = _e;
      details();

   }

   private void details(){

      if(exceptionDetails == null){
         if(exception instanceof DoublonFoundException){
            defineDetailForDoublonFoundException((DoublonFoundException) exception);
         }
         else if (exception instanceof MultipleDoublonFoundException) {
            for(DoublonFoundException doublonFoundException : ((MultipleDoublonFoundException)exception).getDoublonFoundExceptions()) {
               defineDetailForDoublonFoundException(doublonFoundException);
            }
         }
      }

   }

   private void defineDetailForDoublonFoundException(DoublonFoundException doublonFoundException){
      String label = "error.doublonfound.details";
      String banks = "";
      List<String> banksByCodeDoublon = new ArrayList<String>();
      //TK-426 : Dans le cas de mise à jour automatique du code des enfants d'un prélèvement, ce traitement peut afficher des doublonc sur des codes échantillon
      //ou sur des codes de produit dérivés. Pour éviter l'ambiguïté, le type de l'entité concerné est affiché dans le message de doublon
      //=> pour cela, définition d'une liste contenant le type de l'entité à afficher pour chaque cas de code en doublon. La valeur est internationalisée
      List<String> labelsForTypeEntiteKeyByCodeDoublon = new ArrayList<String>();
      switch(doublonFoundException.getEntite()){
         case "Prelevement":
            List<Prelevement> dbls = new ArrayList<Prelevement>();
            //Dans certains cas, plusieurs doublons ont pu être détectés en même temps :
            for(String code : doublonFoundException.getCodes()) {
               dbls = ManagerLocator.getPrelevementManager().findByCodeExactMatchInPlateforme(code, SessionUtils.getCurrentPlateforme());
               for(final Prelevement prelevement : dbls){
                  if(!banks.equals("")){
                     banks = banks + ", ";
                  }
                  banks = banks + prelevement.getBanque().getNom();
               }
               labelsForTypeEntiteKeyByCodeDoublon.add(Labels.getLabel("Entite.Prelevement"));
               banksByCodeDoublon.add(banks);
               banks="";
            }

            break;
         case "Echantillon":
            List<Echantillon> dblEs = new ArrayList<Echantillon>();
            for(String code : doublonFoundException.getCodes()) {
               dblEs = ManagerLocator.getEchantillonManager().findByCodeInPlateforme(code, SessionUtils.getCurrentPlateforme());
               for(final Echantillon echantillon : dblEs){
                  if(!banks.equals("")){
                     banks = banks + ", ";
                  }
                  banks = banks + echantillon.getBanque().getNom();
               }
               labelsForTypeEntiteKeyByCodeDoublon.add(Labels.getLabel("Entite.Echantillon"));
               banksByCodeDoublon.add(banks);
               banks="";
            }

            break;
         case "ProdDerive":
            List<ProdDerive> dblDs = new ArrayList<ProdDerive>();
            for(String code : doublonFoundException.getCodes()) {
               dblDs = ManagerLocator.getProdDeriveManager().findByCodeInPlateformeManager(code, SessionUtils.getCurrentPlateforme());
               for(final ProdDerive derive : dblDs){
                  if(!banks.equals("")){
                     banks = banks + ", ";
                  }
                  banks = banks + derive.getBanque().getNom();
               }
               labelsForTypeEntiteKeyByCodeDoublon.add(Labels.getLabel("Entite.ProdDerive"));
               banksByCodeDoublon.add(banks);
               banks="";
            }

            break;
         case "Cession":
            label = "error.doublonfound.details.cession";
            List<Cession> dblCs = new ArrayList<Cession>();
            for(String code : doublonFoundException.getCodes()) {
               dblCs = ManagerLocator.getCessionManager().findByNumeroInPlateformeManager(code, SessionUtils.getCurrentPlateforme());
               for(final Cession cession : dblCs){
                  if(!banks.equals("")){
                     banks = banks + ", ";
                  }
                  banks = banks + cession.getBanque().getNom();
               }
               banksByCodeDoublon.add(banks);
               banks="";
            }                  

            break;
         case "Patient":
            // @since 2.3.0-gatsbi, amélioration du message 
            PatientDoublonFound dbf = doublonFoundException.getPatientDoublonFound();
            if(dbf != null) {
               if (dbf.getNip() != null) {
                  label = ObjectTypesFormatters
                     .getLabel("validation.doublon.patient.nip", new String[] {dbf.getNip()});
               }else if (dbf.getIdentifiant() != null){
                  label = ObjectTypesFormatters
                     .getLabel("validation.doublon.patient.identifiant", new String[] {dbf.getIdentifiant()});
               }else {
                  label = ObjectTypesFormatters
                     .getLabel("validation.doublon.patient", new String[] {dbf.getNom()});
               }
            } else {
               label = AbstractController.handleExceptionMessage(exception);
            }
         default:
            break;
      }

      /*
      exceptionDetails = !banks.equals("")
         ? ObjectTypesFormatters.getLabel(label, new String[] {doublonFoundException.getCode(), banks})
         : label;
        */ 
      
      //Affiche un message par code en doublon en les séparant par une ligne blanche
      int nb = doublonFoundException.getCodes().size();
      if(exceptionDetails == null) {
         exceptionDetails="";         
      }
      for(int i=0; i< nb; i++) {
         if(!banksByCodeDoublon.isEmpty() && !banksByCodeDoublon.get(i).equals("")) {
            //Le nombre de paramètres à passer au libellé internationalisé dépendant du cas en cours de traitement, 
            //passage par une liste qui sera transformée en tableau lors de l'appel de la récupération du libellé internationalisé
            List<String> listParamLabel = new ArrayList<String>();
            if(!labelsForTypeEntiteKeyByCodeDoublon.isEmpty()) {
               listParamLabel.add(labelsForTypeEntiteKeyByCodeDoublon.get(i).toLowerCase());
            }
            listParamLabel.add(doublonFoundException.getCodes().get(i));
            listParamLabel.add(banksByCodeDoublon.get(i));
            exceptionDetails = exceptionDetails + ObjectTypesFormatters.getLabel(label, listParamLabel.toArray(new String[0]));
         }
         else {
            exceptionDetails= exceptionDetails + label;
            
         }
         exceptionDetails = exceptionDetails + "<br><br>";//prépare pour l'éventuel message suivant.
      }

   }

   public String getTitle(){
      return title;
   }

   public String getMessage(){
      return message;
   }

   public String getExceptionDetails(){
      return exceptionDetails;
   }

   public void setExceptionDetails(final String _e){
      this.exceptionDetails = _e;
   }
}
