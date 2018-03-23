package fr.aphp.tumorotek.action.prelevement.bto;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.action.prelevement.ReferenceurPatient;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * TODO Javadoc
 */
public class FichePrelevementEditBTO extends FichePrelevementEdit
{
   private static final long serialVersionUID = 1L;

   protected CalendarBox datePeremptionCalBox;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      datePeremptionCalBox.setDisabled(true);
   }

   @Override
   public void initLists(){
      super.initLists();
      setConsentTypes(ManagerLocator.getConsentTypeManager().findByTypeLikeManager("GREFFON", false));
      if(!getConsentTypes().isEmpty() && getConsentTypes().size() > 0){
         setSelectedConsentType(getConsentTypes().get(0));
      }
   }

   @Override
   public void createNewObject(){

      final List<File> filesCreated = new ArrayList<>();

      try{
         setEmptyToNulls();
         setFieldsToUpperCase();

         // on ne change pas les associations qui ne sont pas présentes
         // dans le formulaire
         final Transporteur transporteur = this.prelevement.getTransporteur();
         final Collaborateur operateur = this.prelevement.getOperateur();
         final Unite quantiteUnite = this.prelevement.getQuantiteUnite();

         getObject().setRisques(findSelectedRisques());
         getObject().setLaboInters(new HashSet<LaboInter>());

         // update de l'objet
         ManagerLocator.getPrelevementManager().createObjectManager(prelevement,
            SessionUtils.getSelectedBanques(sessionScope).get(0), selectedNature, this.maladie, selectedConsentType,
            selectedCollaborateur, selectedService, selectedMode, selectedConditType, selectedConditMilieu, transporteur,
            operateur, quantiteUnite, null, getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
            filesCreated, SessionUtils.getLoggedUser(sessionScope), true, SessionUtils.getSystemBaseDir(), false);

         if(null != referenceur){
            if(this.patientEmbedded){

               final Component embeddedPatient =
                  referenceur.getFellow("winRefPatient").getFellow("newPatientDiv").getFellow("fichePatientDiv").getFirstChild();
               // enregistre le formulaire dans le backing-bean
               // meme si il est vide...
               ((FichePatientEdit) embeddedPatient.getFellow("fwinPatientEditBTO")
                  .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).getBinder()
                     .saveComponent(embeddedPatient.getFellow("fwinPatientEditBTO"));

            }
         }
         // rafraichit la maladie pour avoir les references
         this.maladie = ManagerLocator.getPrelevementManager().getMaladieManager(this.prelevement);

         // suppression du patientSip
         getObjectTabController().removePatientSip();
         // gestion de la communication des infos et de l'éventuel dossier externe
         getObjectTabController().handleExtCom(getObject(), getObjectTabController());
      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   @Override
   protected void setEmbeddedObjects(){
      if(referenceur != null){

         // vérifie la présence du formulaire embarqué patient
         if(this.patientEmbedded){
            // maladie associee
            final Component embeddedMaladie = referenceur.getFellow("winRefPatient").getFellow("newPatientDiv")
               .getFellow("ficheMaladieWithPatientDiv").getFirstChild();

            // force la validation des dates maladies
            // car les dates de naissance
            // et deces du patient ont pu être modifée à posteriori
            ((FicheMaladie) embeddedMaladie.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
               .validateAllDateComps();

            setMaladieFromEmbedded(embeddedMaladie);

            final Component embeddedPatient =
               referenceur.getFellow("winRefPatient").getFellow("newPatientDiv").getFellow("fichePatientDiv").getFirstChild();
            // enregistre le formulaire dans le backing-bean
            // meme si il est vide...
            ((FichePatientEdit) embeddedPatient.getFellow("fwinPatientEditBTO")
               .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).getBinder()
                  .saveComponent(embeddedPatient.getFellow("fwinPatientEditBTO"));

            // prepare les valeurs des attributs
            ((FichePatientEdit) embeddedPatient.getFellow("fwinPatientEditBTO")
               .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).prepareDataBeforeSave(true);

            // re-assigne la reference ndaBox vers le composant embedded
            this.ndaBox = (Textbox) embeddedPatient.getFellow("fwinPatientEditBTO").getFellow("ndaBox");

            // vérifie la présence du formulaire embarqué maladie
         }else{
            if(this.maladieEmbedded){
               final Component embedded = referenceur.getFellow("winRefPatient").getFellow("embeddedFicheMaladieRow")
                  .getFellow("embeddedFicheMaladieDiv");
               setMaladieFromEmbedded(embedded);
            }else if(((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true)).getNoRadio()
               .isChecked()
               || ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true))
                  .getSelectedPatient() == null){
               this.maladie = null;
               this.prelevement.setMaladie(null);
            }
            if(((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true)).getNdaBox()
               .getValue() != null){
               this.ndaBox = ((ReferenceurPatient) referenceur.getAttributeOrFellow("winRefPatient$composer", true)).getNdaBox();
            }
         }
      }
   }

   /**
    * Calcul automaticuament la date de péremption, 5 ans après la date de prélèvement
    */
   private void autoDatePeremption(){
      final Calendar c = Calendar.getInstance();

      if(null != datePrelCalBox.getValue()){
         c.setTime(datePrelCalBox.getValue().getTime());
         c.add(Calendar.YEAR, 5);

         datePeremptionCalBox.setValue(c);
         this.prelevement.setDatePeremption(datePeremptionCalBox.getValue());
      }
   }

   /**
    * à la sortie du champs Date de Prélèvement on calcul la date de péremption 5 ans après
    */
   @Override
   public void onBlur$datePrelCalBox(){
      super.onBlur$datePrelCalBox();
      this.autoDatePeremption();
   }

   /**
    * Lors du click sur la date de prélèvement, on défini la date de péremption 5 ans après
    */
   public void onClick$datePrelCalBox(){
      this.autoDatePeremption();
   }

   /**
    * Lorsque l'utilisateur sors du champs Date de Peremption
    */
   public void onBlur$datePeremptionCalBox(){
      if(null != datePeremptionCalBox && null != datePeremptionCalBox.getValue()){
         this.prelevement.setDatePeremption(datePeremptionCalBox.getValue());
      }

   }

}
