package fr.aphp.tumorotek.action.patient.bto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.FichePatientStatic;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale;
import fr.aphp.tumorotek.action.validation.ResultatValidationModale.ResultatValidationModaleData;
import fr.aphp.tumorotek.decorator.CollaborateurDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.coeur.patient.delegate.AbstractPatientDelegate;
import fr.aphp.tumorotek.model.coeur.patient.delegate.PatientBTO;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.ActionType;
import fr.aphp.tumorotek.model.validation.DetailValidation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FichePatientStaticBTO extends FichePatientStatic
{

   private static final long serialVersionUID = 1L;

   private Window fwinCessionStaticBTO;
   private Group validationGroup;
   private Button validate;
   private Button invalidate;
   private Div validationStatus;
   private Label consultValidation;

   private boolean canValidate;
   private boolean canUnvalidate;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      addMaladie.setVisible(false);
   }

   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.action.prodderive.FicheProdDeriveStatic#setObject(fr.aphp.tumorotek.model.TKdataObject)
    */
   @Override
   public void setObject(final TKdataObject e){

      super.setObject(e);

      final AbstractPatientDelegate delegate = getPatient().getDelegate();

      boolean validated = false;

      if(delegate != null){

         final PatientBTO patientBTO = (PatientBTO) delegate;
         final DetailValidation detailValidation = patientBTO.getDetailValidation();

         validated = detailValidation != null;

         if(validated){
            edit.setDisabled(true);
            getObjectTabController().getFicheAnnotation().disableEdit(true);
            validationStatus.setStyle("background-color: " + detailValidation.getNiveauValidation().getCouleur() + ";");
            consultValidation.addForward(null, fwinCessionStaticBTO, "onConsultValidation");
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

      final AbstractPatientDelegate delegate = getPatient().getDelegate();

      final boolean validated = delegate != null && ((PatientBTO) delegate).getDetailValidation() != null;

      final boolean validateVisible = !validated && isCanValidate();
      final boolean invalidateVisible = validated && isCanUnvalidate();

      validate.setVisible(validateVisible);
      invalidate.setVisible(invalidateVisible);

      if(validateVisible){

         final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(getPatient().entiteNom()).get(0);
         final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);

         validate.setDisabled(action == null);

      }

   }

   /**
    * Listener du bouton de dévalidation
    */
   public void onClick$invalidate(){

      final Patient patient = getPatient();

      //A ce niveau, le delegate ne peut être nul sinon, les boutons ne sont pas affichés
      //on part donc du principe que le delegate est présent pour simplifier l'écriture
      final PatientBTO patientBTO = (PatientBTO) patient.getDelegate();

      //Mise  jour du prélèvement (suppression des informations de validation)
      patientBTO.setDetailValidation(null);

      final List<Collaborateur> medecins = new ArrayList<>();
      for(final PatientMedecin patientMedecin : patient.getPatientMedecins()){
         medecins.add(patientMedecin.getCollaborateur());
      }

      ManagerLocator.getPatientManager().createOrUpdateObjectManager(patient, new ArrayList<>(patient.getMaladies()), medecins,
         new ArrayList<>(patient.getPatientLiens()), null, null, null, null, SessionUtils.getLoggedUser(sessionScope),
         "invalidation", null, false);

      //Mise à jour de la liste de prélèvements
      getObjectTabController().getListe().updateObjectGridList(patient);

      setObject(patient);

   }

   /**
    * Listener du bouton de validation
    */

   public void onClick$validate(){

      final Patient patient = getPatient();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(patient.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(patient, action);

      //Appel de la popup
      ResultatValidationModale.showValidationResultatPopup(patient.entiteNom(), resultatValidation, false,
         new EventListener<Event>()
         {

            @Override
            public void onEvent(final Event event) throws Exception{

               final ResultatValidationModaleData dataModale = (ResultatValidationModaleData) event.getData();

               //Si la validation du prélèvement est confirmée dans la popup
               if(dataModale != null && dataModale.isValidated()){

                  final AbstractPatientDelegate delegate = patient.getDelegate();
                  PatientBTO patienBTO = null;
                  DetailValidation detailValidation = null;

                  if(delegate != null){
                     patienBTO = (PatientBTO) delegate;
                     detailValidation = patienBTO.getDetailValidation();

                     if(detailValidation == null){
                        detailValidation = new DetailValidation();
                        patienBTO.setDetailValidation(detailValidation);
                     }

                  }else{

                     patienBTO = new PatientBTO();
                     patienBTO.setDelegator(patient);

                     detailValidation = new DetailValidation();
                     patienBTO.setDetailValidation(detailValidation);

                     patient.setDelegate(patienBTO);

                  }

                  //Mise à jour du prélèvement
                  detailValidation.setDateValidation(Calendar.getInstance());
                  detailValidation.setNiveauValidation(resultatValidation.getNiveauValidation());
                  detailValidation.setCommentaireValidation(dataModale.getCommentaire());
                  detailValidation.setValideur(SessionUtils.getLoggedUser(sessionScope));

                  final List<Collaborateur> medecins = new ArrayList<>();
                  for(final PatientMedecin patientMedecin : patient.getPatientMedecins()){
                     medecins.add(patientMedecin.getCollaborateur());
                  }

                  ManagerLocator.getPatientManager().createOrUpdateObjectManager(patient, new ArrayList<>(patient.getMaladies()),
                     medecins, new ArrayList<>(patient.getPatientLiens()), null, null, null, null,
                     SessionUtils.getLoggedUser(sessionScope), "validation", null, false);

                  //Mise à jour de la liste de prélèvements
                  getObjectTabController().getListe().updateObjectGridList(patient);

                  setObject(patient);

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

      final Patient patient = getPatient();

      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager(patient.entiteNom()).get(0);

      //Exécution de la validation
      final Action action = ManagerLocator.getActionManager().findByEntiteAndType(entite, ActionType.VALIDATION);
      final ResultatValidation resultatValidation = ManagerLocator.getValidateurManager().validerAction(patient, action);

      //Appel de la popup
      ResultatValidationModale.showReadOnlyValidationResultatPopup(patient.entiteNom(), resultatValidation);

   }

   /**
    * Renvoie le valideur de l'échantillon formaté pour l'affichage si il existe
    * @return
    */
   public String getValideur(){

      String res = null;

      final AbstractPatientDelegate delegate = getPatient().getDelegate();

      if(delegate != null){

         final PatientBTO patientBTO = (PatientBTO) delegate;

         if(patientBTO.getDetailValidation() != null && patientBTO.getDetailValidation().getValideur() != null){

            final Utilisateur valideur = patientBTO.getDetailValidation().getValideur();

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
      final AbstractPatientDelegate delegate = getPatient().getDelegate();

      if(delegate != null){

         final PatientBTO patientBTO = (PatientBTO) delegate;

         if(patientBTO.getDetailValidation() != null){
            dateFormatee = ObjectTypesFormatters.dateRenderer2(patientBTO.getDetailValidation().getDateValidation());
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
