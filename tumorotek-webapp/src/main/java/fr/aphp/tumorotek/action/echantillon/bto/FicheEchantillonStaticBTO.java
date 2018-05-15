package fr.aphp.tumorotek.action.echantillon.bto;

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
import fr.aphp.tumorotek.action.echantillon.FicheEchantillonStatic;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale.ResultatValidationModaleData;
import fr.aphp.tumorotek.decorator.CollaborateurDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.delegate.AbstractEchantillonDelegate;
import fr.aphp.tumorotek.model.coeur.echantillon.delegate.EchantillonBTO;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.ActionType;
import fr.aphp.tumorotek.model.validation.DetailValidation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheEchantillonStaticBTO extends FicheEchantillonStatic
{

   private static final long serialVersionUID = 1L;

   private Window fwinEchantillonStaticBTO;
   private Group validationGroup;
   private Button validate;
   private Button invalidate;
   private Div validationStatus;
   private Label consultValidation;

   private boolean canValidate;
   private boolean canUnvalidate;

   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.action.echantillon.FicheEchantillonStatic#setObject(fr.aphp.tumorotek.model.TKdataObject)
    */
   @Override
   public void setObject(final TKdataObject e){

      super.setObject(e);

      final AbstractEchantillonDelegate delegate = getEchantillon().getDelegate();

      boolean validated = false;

      if(delegate != null){

         final EchantillonBTO echantillonBTO = (EchantillonBTO) delegate;
         final DetailValidation detailValidation = echantillonBTO.getDetailValidation();

         validated = detailValidation != null;

         if(validated){
            edit.setDisabled(true);
            getObjectTabController().getFicheAnnotation().disableEdit(true);
            validationStatus.setStyle("background-color: " + detailValidation.getNiveauValidation().getCouleur() + ";");
            consultValidation.addForward(null, fwinEchantillonStaticBTO, "onConsultValidation");
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

      final AbstractEchantillonDelegate delegate = getEchantillon().getDelegate();

      final boolean validated = delegate != null && ((EchantillonBTO) delegate).getDetailValidation() != null;

      final boolean validateVisible = !validated && isCanValidate();
      final boolean invalidateVisible = validated && isCanUnvalidate();

      validate.setVisible(validateVisible);
      invalidate.setVisible(invalidateVisible);

      if(validateVisible){

         final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(getEchantillon().entiteNom()).get(0);
         final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);

         validate.setDisabled(action == null);

      }

   }

   /**
    * Listener du bouton de dévalidation
    */
   public void onClick$invalidate(){

      final Echantillon echantillon = getEchantillon();

      //A ce niveau, le delegate ne peut être nul sinon, les boutons ne sont pas affichés
      //on part donc du principe que le delegate est présent pour simplifier l'écriture
      final EchantillonBTO echantillonBTO = (EchantillonBTO) echantillon.getDelegate();

      //Mise  jour du prélèvement (suppression des informations de validation)
      echantillonBTO.setDetailValidation(null);

      final OperationType operationType =
         ManagerLocator.getOperationTypeManager().findByNomLikeManager("InvalidationEntite", true).get(0);

      final List<OperationType> listOperationsType = new ArrayList<>();
      listOperationsType.add(operationType);

      ManagerLocator.getEchantillonManager().updateObjectManager(echantillon, echantillon.getBanque(),
         echantillon.getPrelevement(), echantillon.getCollaborateur(), echantillon.getObjetStatut(), echantillon.getEmplacement(),
         echantillon.getEchantillonType(), null, null, echantillon.getQuantiteUnite(), echantillon.getEchanQualite(),
         echantillon.getModePrepa(), echantillon.getReservation(), null, null, null, null,
         SessionUtils.getLoggedUser(sessionScope), false, listOperationsType, null);

      //Mise à jour de la liste de prélèvements
      getObjectTabController().getListe().updateObjectGridList(echantillon);

      setObject(echantillonBTO.getDelegator());

   }

   /**
    * Listener du bouton de validation
    */
   public void onClick$validate(){

      final Echantillon echantillon = getEchantillon();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(echantillon.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(echantillon, action);

      //Appel de la popup
      ResultatValidationModale.showValidationResultatPopup(echantillon.entiteNom(), resultatValidation, false,
         new EventListener<Event>()
         {

            @Override
            public void onEvent(final Event event) throws Exception{

               final ResultatValidationModaleData dataModale = (ResultatValidationModaleData) event.getData();

               //Si la validation du prélèvement est confirmée dans la popup
               if(dataModale != null && dataModale.isValidated()){

                  final AbstractEchantillonDelegate delegate = getEchantillon().getDelegate();
                  EchantillonBTO echantillonBTO = null;
                  DetailValidation detailValidation = null;

                  if(delegate != null){

                     echantillonBTO = (EchantillonBTO) delegate;
                     detailValidation = echantillonBTO.getDetailValidation();

                     if(detailValidation == null){
                        detailValidation = new DetailValidation();
                        echantillonBTO.setDetailValidation(detailValidation);
                     }

                  }else{

                     echantillonBTO = new EchantillonBTO();
                     echantillonBTO.setDelegator(echantillon);

                     detailValidation = new DetailValidation();
                     echantillonBTO.setDetailValidation(detailValidation);

                     echantillon.setDelegate(echantillonBTO);

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

                  ManagerLocator.getEchantillonManager().updateObjectManager(echantillon, echantillon.getBanque(),
                     echantillon.getPrelevement(), echantillon.getCollaborateur(), echantillon.getObjetStatut(),
                     echantillon.getEmplacement(), echantillon.getEchantillonType(), null, null, echantillon.getQuantiteUnite(),
                     echantillon.getEchanQualite(), echantillon.getModePrepa(), echantillon.getReservation(), null, null, null,
                     null, SessionUtils.getLoggedUser(sessionScope), false, listOperationsType, null);

                  //Mise à jour de la liste de prélèvements
                  getObjectTabController().getListe().updateObjectGridList(echantillon);

                  setObject(echantillon);

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

      final Echantillon echantillon = getEchantillon();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(echantillon.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(echantillon, action);

      //Appel de la popup
      ResultatValidationModale.showReadOnlyValidationResultatPopup(echantillon.entiteNom(), resultatValidation);

   }

   /**
    * Renvoie le valideur de l'échantillon formaté pour l'affichage si il existe
    * @return
    */
   public String getValideur(){

      String res = null;

      final AbstractEchantillonDelegate delegate = getEchantillon().getDelegate();

      if(delegate != null){

         final EchantillonBTO echantillonBTO = (EchantillonBTO) delegate;

         if(echantillonBTO.getDetailValidation() != null && echantillonBTO.getDetailValidation().getValideur() != null){

            final Utilisateur valideur = echantillonBTO.getDetailValidation().getValideur();

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
    * Retourne la date de validation de l'échantillon formatée pour l'affichage si elle existe
    * @return
    */
   public String getDateValidationFormatee(){

      String dateFormatee = null;
      final AbstractEchantillonDelegate delegate = getEchantillon().getDelegate();

      if(delegate != null){

         final EchantillonBTO echantillonBTO = (EchantillonBTO) delegate;

         if(echantillonBTO.getDetailValidation() != null){
            dateFormatee = ObjectTypesFormatters.dateRenderer2(echantillonBTO.getDetailValidation().getDateValidation());
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
