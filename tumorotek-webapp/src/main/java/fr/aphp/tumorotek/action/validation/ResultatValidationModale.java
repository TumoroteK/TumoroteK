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
package fr.aphp.tumorotek.action.validation;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Composition;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.model.validation.NiveauValidation;

/**
 * Controleur de la popup d'affichage des résultats de validation
 * @author Gille Chapelot
 *
 */
public class ResultatValidationModale extends GenericForwardComposer<Component>
{

   /**
    * Classe interne destinée à contenir les informations retournées par la popup
    */
   public class ResultatValidationModaleData{
      
      private boolean validated;
      private String commentaire;
      
      /**
       * Construteur privé
       */
      //Empêche l'instanciation à l'extérieur de ce contrôleur
      private ResultatValidationModaleData(){}
      
      /**
       * @return the validated
       */
      public boolean isValidated(){
         return validated;
      }
      
      /**
       * @param validated the validated to set
       */
      public void setValidated(boolean validated){
         this.validated = validated;
      }
      
      /**
       * @return the commentaire
       */
      public String getCommentaire(){
         return commentaire;
      }
      
      /**
       * @param commentaire the commentaire to set
       */
      public void setCommentaire(String commentaire){
         this.commentaire = commentaire;
      }
      
   }
   
   private static final long serialVersionUID = 3752999697546611770L;

   private ValidationResultatCauseRenderer resultatCausesRenderer = new ValidationResultatCauseRenderer();
   private ValidationResultatAnomaliesRenderer resultatAnomaliesRenderer = new ValidationResultatAnomaliesRenderer();

   private Grid causesGrid;
   private Grid anomaliesGrid;
   private Label validationMessage;
   private Button validate;
   private Div validationCommentDiv;
   private Textbox validationComment;
   
   @Override
   /*
    * (non-Javadoc)
    * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
    */
   public void doAfterCompose(Component component) throws Exception{

      super.doAfterCompose(component);

      boolean readOnly = (Boolean) arg.get("readOnly");
      
      Window window = (Window) component;

      window.setVisible(false);
      window.setId("resultatValidationWindow");
      window.setPage(page);
      window.setMaximizable(true);
      window.setSizable(true);
      window.setTitle(Labels.getLabel("validation.popup.titre"));
      window.setBorder("normal");
      window.setWidth("650px");
      window.setPosition("center, top");
      window.setClosable(true);

      validate.setVisible( !readOnly );
      validationCommentDiv.setVisible( !readOnly );
      
      ResultatValidation resultatValidation = (ResultatValidation) arg.get("resultatValidation");
      String nomEntite = (String) arg.get("nomEntite");
      
      String labelEntite = Labels.getLabel("validation.popup.entite.label");

      if(nomEntite != null && Labels.getLabel("Entite." + nomEntite) != null){
         labelEntite = Labels.getLabel("Entite." + nomEntite);
      }

      anomaliesGrid.setVisible(false);
      causesGrid.setVisible(false);

      if( !resultatValidation.isValide() ){

         NiveauValidation niveauValidation = resultatValidation.getNiveauValidation();
         
         //Cas d'une entité non valide
         if(!resultatValidation.getCauses().isEmpty()){
            validationMessage.setValue(ObjectTypesFormatters.getLabel(niveauValidation.getCleMessage(), new String[] {labelEntite}));
            validationMessage.setStyle("background-color: " + niveauValidation.getCouleur() + ";");
            causesGrid.setModel(new ListModelMap<>(resultatValidation.getCauses()));
            causesGrid.setVisible(true);
         }

      }
      else {
         
         NiveauValidation niveauValidationOk = ManagerLocator.getNiveauValidationManager().findCriticiteLevelOk();
         validationMessage.setValue(ObjectTypesFormatters.getLabel(niveauValidationOk.getCleMessage(), new String[] {labelEntite}));
         validationMessage.setStyle("background-color: " + niveauValidationOk.getCouleur() + ";");
         
      }

      //Anomalies lors de la validation
      if(!resultatValidation.getAnomalie().isEmpty()){
         validationMessage.setValue(Labels.getLabel("validation.popup.anomalie.message"));
         validationMessage.setStyle("background-color: red;");
         anomaliesGrid.setModel(new ListModelMap<>(resultatValidation.getAnomalie()));
         anomaliesGrid.setVisible(true);
      }

   }

   /**
    * Affiche de la popup des réultats de validation
    * @param nomEntite nom de l'entité validée (Patient, Prelevement, etc.)
    * @param resultatValidation résultats de validation à afficher
    * @param banque collection utilisée
    * @param readOnly mode lecture seule
    * @param onCloseListener listener à exécuter à la fermeture de la popup
    */
   public static void showValidationResultatPopup(final String nomEntite, final ResultatValidation resultatValidation, final boolean readOnly,
      final EventListener<Event> onCloseListener){

      Map<String, Object> args = new HashMap<>();
      args.put("resultatValidation", resultatValidation);
      args.put("nomEntite", nomEntite);
      args.put("readOnly", Boolean.valueOf(readOnly));

      Executions.getCurrent().setAttribute(Composition.PARENT, null);
      Component parent = Executions.createComponents("/zuls/validation/ResultatValidationModale.zul", null, args);
      
      while(parent != null && !(parent instanceof Window)) {
         parent = parent.getParent();
      }

      if(parent instanceof Window){
         final Window window = (Window) parent;
         if(onCloseListener != null){
            window.addEventListener(Events.ON_CLOSE, onCloseListener);
         }

         window.doModal();

      }

   }

   /**
    * Affiche la popup de résultat de validation en mode "readOnly"
    * @param nomEntite nom de l'entité validée (Patient, Prelevement, etc.)
    * @param resultatValidation résultats de validation à afficher
    * @param banque collection utilisée
    */
   public static void showReadOnlyValidationResultatPopup(final String nomEntite, final ResultatValidation resultatValidation) {
      showValidationResultatPopup(nomEntite, resultatValidation, true, null);
   }
   
   /**
    * Ferme la popup de validation lors du clic sur le bouton "close"
    */
   public void onClick$close(){

      ResultatValidationModaleData data = new ResultatValidationModaleData();
      data.setValidated(false);
      
      Events.postEvent(Events.ON_CLOSE, self.getRoot(), data);
      
   }

   /**
    * Ferme la popup de validation lors du clic sur le bouton "validate"
    */
   public void onClick$validate(){

      ResultatValidationModaleData data = new ResultatValidationModaleData();
      data.setValidated(true);
      data.setCommentaire( validationComment.getValue() );
      
      Events.postEvent(Events.ON_CLOSE, self.getRoot(), data);

   }

   /**
    * @return the resultatCausesRenderer
    */
   public ValidationResultatCauseRenderer getResultatCausesRenderer(){
      return resultatCausesRenderer;
   }

   /**
    * @param resultatCausesRenderer the resultatCausesRenderer to set
    */
   public void setResultatCausesRenderer(ValidationResultatCauseRenderer resultatCausesRenderer){
      this.resultatCausesRenderer = resultatCausesRenderer;
   }

   /**
    * @return the resultatAnomaliesRenderer
    */
   public ValidationResultatAnomaliesRenderer getResultatAnomaliesRenderer(){
      return resultatAnomaliesRenderer;
   }

   /**
    * @param resultatAnomaliesRenderer the resultatAnomaliesRenderer to set
    */
   public void setResultatAnomaliesRenderer(ValidationResultatAnomaliesRenderer resultatAnomaliesRenderer){
      this.resultatAnomaliesRenderer = resultatAnomaliesRenderer;
   }

}
