package fr.aphp.tumorotek.action.prelevement.bto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale.ResultatValidationModaleData;
import fr.aphp.tumorotek.decorator.CollaborateurDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.AbstractPrelevementDelegate;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementBTO;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.ActionType;
import fr.aphp.tumorotek.model.validation.DetailValidation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FichePrelevementStaticBTO extends FichePrelevementStatic
{

   private static final long serialVersionUID = 1L;

   private Window fwinPrelevementStaticBTO;
   private Group validationGroup;
   private Button validate;
   private Button invalidate;
   private Div validationStatus;
   private Label consultValidation;

   private boolean canValidate;
   private boolean canUnvalidate;

   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.action.prodderive.FicheProdDeriveStatic#setObject(fr.aphp.tumorotek.model.TKdataObject)
    */
   @Override
   public void setObject(final TKdataObject e){

      super.setObject(e);

      final AbstractPrelevementDelegate delegate = getPrelevement().getDelegate();

      boolean validated = false;

      if(delegate != null){

         final DetailValidation detailValidation = ((PrelevementBTO) delegate).getDetailValidation();

         validated = detailValidation != null;

         if(validated){
            edit.setDisabled(true);
            getObjectTabController().getFicheAnnotation().disableEdit(true);
            validationStatus.setStyle("background-color: " + detailValidation.getNiveauValidation().getCouleur() + ";");
            consultValidation.addForward(null, fwinPrelevementStaticBTO, "onConsultValidation");
            consultValidation.setClass("formLink");
         }

      }

      validationGroup.setVisible(validated);
      for(final Row row : validationGroup.getItems()){
         row.setVisible(validated);
      }

   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic#disableToolBar(boolean)
    */
   @Override
   public void disableToolBar(final boolean b){

      super.disableToolBar(b);

      final AbstractPrelevementDelegate delegate = getPrelevement().getDelegate();

      final boolean validated = delegate != null && ((PrelevementBTO) delegate).getDetailValidation() != null;

      final boolean validateVisible = !validated && isCanValidate();
      final boolean invalidateVisible = validated && isCanUnvalidate();

      validate.setVisible(validateVisible);
      invalidate.setVisible(invalidateVisible);

      if(validateVisible){

         final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(getPrelevement().entiteNom()).get(0);
         final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);

         validate.setDisabled(action == null);

      }

   }

   /**
    * Listener du bouton de dévalidation
    */
   public void onClick$invalidate(){

      final Prelevement prelevement = getPrelevement();

      //A ce niveau, le delegate ne peut être nul sinon, les boutons ne sont pas affichés
      //on part donc du principe que le delegate existe
      final PrelevementBTO prodDeriveBTO = (PrelevementBTO) prelevement.getDelegate();

      //Mise  jour du produit dérivé (suppression des informations de validation)
      prodDeriveBTO.setDetailValidation(null);

      final OperationType operationType =
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("InvalidationEntite", true).get(0);

      final List<OperationType> listOperationsType = new ArrayList<>();
      listOperationsType.add(operationType);

      ManagerLocator.getPrelevementManager().updateObjectManager(prelevement, prelevement.getBanque(), prelevement.getNature(),
         prelevement.getMaladie(), prelevement.getConsentType(), prelevement.getPreleveur(), prelevement.getServicePreleveur(),
         prelevement.getPrelevementType(), prelevement.getConditType(), prelevement.getConditMilieu(),
         prelevement.getTransporteur(), prelevement.getOperateur(), prelevement.getQuantiteUnite(),
         new ArrayList<>(prelevement.getLaboInters()), null, null, null, null, SessionUtils.getLoggedUser(sessionScope),
         Integer.valueOf(prelevement.getSterile() ? 1 : 0), true, SessionUtils.getSystemBaseDir(), false);

      //Mise à jour de la liste de prélèvements
      getObjectTabController().getListe().updateObjectGridList(prelevement);

      setObject(prelevement);

   }

   /**
    * Listener du bouton de validation
    */

   public void onClick$validate(){

      final Prelevement prelevement = getPrelevement();
      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(prelevement.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(prelevement, action);

      //Appel de la popup
      ResultatValidationModale.showValidationResultatPopup(prelevement.entiteNom(), resultatValidation, false,
         new EventListener<Event>()
         {

            @Override
            public void onEvent(final Event event) throws Exception{

               final ResultatValidationModaleData dataModale = (ResultatValidationModaleData) event.getData();

               //Si la validation du prélèvement est confirmée dans la popup
               if(dataModale != null && dataModale.isValidated()){

                  final AbstractPrelevementDelegate delegate = prelevement.getDelegate();
                  PrelevementBTO prelevementBTO = null;
                  DetailValidation detailValidation = null;

                  if(delegate != null){
                     prelevementBTO = (PrelevementBTO) delegate;
                     detailValidation = prelevementBTO.getDetailValidation();

                     if(detailValidation == null){
                        detailValidation = new DetailValidation();
                        prelevementBTO.setDetailValidation(detailValidation);
                     }

                  }else{

                     prelevementBTO = new PrelevementBTO();
                     prelevementBTO.setDelegator(prelevement);

                     detailValidation = new DetailValidation();
                     prelevementBTO.setDetailValidation(detailValidation);

                     prelevement.setDelegate(prelevementBTO);

                  }

                  //Mise à jour du prélèvement
                  detailValidation.setDateValidation(Calendar.getInstance());
                  detailValidation.setNiveauValidation(resultatValidation.getNiveauValidation());
                  detailValidation.setCommentaireValidation(dataModale.getCommentaire());
                  detailValidation.setValideur(SessionUtils.getLoggedUser(sessionScope));

                  final OperationType operationType =
                     ManagerLocator.getOperationTypeManager().findByNomLikeManager("ValidationEntite", true).get(0);

                  final List<OperationType> listOperationsType = new ArrayList<>();
                  listOperationsType.add(operationType);

                  ManagerLocator.getPrelevementManager().updateObjectManager(prelevement, prelevement.getBanque(),
                     prelevement.getNature(), prelevement.getMaladie(), prelevement.getConsentType(), prelevement.getPreleveur(),
                     prelevement.getServicePreleveur(), prelevement.getPrelevementType(), prelevement.getConditType(),
                     prelevement.getConditMilieu(), prelevement.getTransporteur(), prelevement.getOperateur(),
                     prelevement.getQuantiteUnite(), new ArrayList<>(prelevement.getLaboInters()), null, null, null, null,
                     SessionUtils.getLoggedUser(sessionScope), Integer.valueOf(prelevement.getSterile() ? 1 : 0), true,
                     SessionUtils.getSystemBaseDir(), false);

                  //Mise à jour de la liste de prélèvements
                  getObjectTabController().getListe().updateObjectGridList(prelevement);

                  setObject(prelevement);

               }

            }
         });

   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.action.controller.AbstractFicheStaticController#drawActionsButtons(java.lang.String)
    */
   @Override
   public void drawActionsButtons(final String nomEntite){

      super.drawActionsButtons(nomEntite);

      Boolean admin = false;

      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      setCanValidate(admin);
      setCanUnvalidate(admin);

   }

   /**
    * Consultation des résultats de validation
    * @param event
    */
   public void onConsultValidation(final Event event){

      final Prelevement prelevement = getPrelevement();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(prelevement.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(prelevement, action);

      //Appel de la popup
      ResultatValidationModale.showReadOnlyValidationResultatPopup(prelevement.entiteNom(), resultatValidation);

   }

   /**
    * Renvoie le valideur de l'échantillon formaté pour l'affichage si il existe
    * @return
    */
   public String getValideur(){

      String res = null;

      final AbstractPrelevementDelegate delegate = getPrelevement().getDelegate();

      if(delegate != null){

         final DetailValidation detailValidation = ((PrelevementBTO) delegate).getDetailValidation();

         if(detailValidation != null && detailValidation.getValideur() != null){

            final Utilisateur valideur = detailValidation.getValideur();

            if(valideur != null){
               res = valideur.getLogin();

               if(valideur.getCollaborateur() != null){
                  final Collaborateur coll = valideur.getCollaborateur();
                  res = new CollaborateurDecorator(coll).getNomPrenom();
               }
            }
         }
      }

      return res;

   }

   /**
    * Retourne la date de validation du prélèvement formatée pour l'affichage si elle existe
    * @return
    */
   public String getDateValidationFormatee(){

      String dateFormatee = null;
      final AbstractPrelevementDelegate delegate = getPrelevement().getDelegate();

      if(delegate != null){

         final PrelevementBTO prelevementBTO = (PrelevementBTO) delegate;

         if(prelevementBTO.getDetailValidation() != null){
            dateFormatee = ObjectTypesFormatters.dateRenderer2(prelevementBTO.getDetailValidation().getDateValidation());
         }

      }

      return dateFormatee;
   }

   /**
    * @return the canValidate
    */
   public boolean isCanValidate(){
      return canValidate;
   }

   /**
    * @param canValidate the canValidate to set
    */
   public void setCanValidate(final boolean canValidate){
      this.canValidate = canValidate;
   }

   /**
    * @return the canUnvalidate
    */
   public boolean isCanUnvalidate(){
      return canUnvalidate;
   }

   /**
    * @param canUnvalidate the canUnvalidate to set
    */
   public void setCanUnvalidate(final boolean canUnvalidate){
      this.canUnvalidate = canUnvalidate;
   }

}
