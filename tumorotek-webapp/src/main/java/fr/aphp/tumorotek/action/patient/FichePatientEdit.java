/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.patient;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.contexte.ContexteConstraints;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.validation.coeur.patient.PatientValidatorImpl;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class FichePatientEdit extends AbstractFicheEditController
{
   protected static final long serialVersionUID = 7781723391910786070L;

   // boxes
   protected Textbox nipBox;

   protected Textbox nomBox;

   protected Textbox nomNaisBox;

   protected Textbox prenomBox;

   protected Datebox dateNaisBox;

   protected Textbox villeNaisBox;

   protected Textbox paysNaisBox;

   protected Datebox dateEtatDecesBox;

   protected Listbox sexeBox;

   protected Toolbar toolbar;

   // included ndaBox (embedded in Prelevement mode only)
   // @since 2.3.0-gatsbi peut être label / div
   protected HtmlBasedComponent ndaFieldLabel;

   protected Textbox ndaBox;

   // referents
   // @since 2.3.0-gatsbi peut être Group / groupbox
   protected HtmlBasedComponent referentsGroup;

   // Objets Principaux
   protected Patient patient = new Patient();

   // dateEtatDeces
   protected Label dateEtatDecesField;

   protected Date dateEtatDeces;

   // traduction pour affichage
   protected LabelCodeItem selectedSexe;

   protected LabelCodeItem selectedEtat;

   // Associations
   protected List<Collaborateur> medecins = new ArrayList<>();

   protected List<PatientLien> liens = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      getReferents().setFicheParent(this);
      getReferents().setMedecins(this.medecins);
   }

   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();
      setReferentsGroupOpen(true);
      getReferents().switchToCreateMode();
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      setReferentsGroupOpen(true);
      getReferents().switchToEditMode();
   }

   /**
    * Recupere le composant MedecinsReferents.
    * @return composant MedecinReferents
    */
   public MedecinReferents getReferents(){
      return (MedecinReferents) self.getFellow("referentsDiv").getFellow("referents").getFellow("winReferents")
         .getAttributeOrFellow("winReferents$composer", false);
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.patient = (Patient) obj;
      this.selectedSexe = setSexeItemFromDBValue(this.patient);
      this.selectedEtat = setPatientEtatFromValue(this.patient);

      accordDateToEtat();

      if(patient.getPatientId() != null){
         // medecins referents
         this.medecins = new ArrayList<>(ManagerLocator.getPatientManager().getMedecinsManager(patient));
         //this.liens = new ArrayList<PatientLien>(ManagerLocator
         //				.getPatientManager().getPatientLiensManager(pat));
      }

      getReferents().setMedecins(this.medecins);
      setReferentsGroupOpen(false);

      super.setObject(obj);

   }

   @Override
   public Patient getObject(){
      return this.patient;
   }

   @Override
   public void setNewObject(){
      setObject(new Patient());
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public PatientController getObjectTabController(){
      return (PatientController) super.getObjectTabController();
   }

   public Map<String, Object> getAntecedentConsentementValue(final List<AnnotationValeur> annotationValue){
      String annotationRow;

      final Map<String, Object> mMap = new HashMap<>();
      for(int i = 0; i < annotationValue.size(); i++){
         if(annotationValue.get(i).getChampAnnotation() != null){
            annotationRow = annotationValue.get(i).getChampAnnotation().toString();
            if(annotationRow.contains("Antécédent_Médicaux.Conforme") || annotationRow.contains("Consentement.Conforme}")){

               mMap.put(annotationRow, annotationValue.get(i).getValeur());
            }
         }
      }
      return mMap;
   }

   @Override
   public void createNewObject(){
      final List<File> filesCreated = new ArrayList<>();

      try{
         prepareDataBeforeSave(false);
         if(patient != null){
            if(patient.getNom() == null || patient.getNom() == ""){
               patient.setNom("Inconnu");
            }
            if(patient.getPrenom() == null){
               patient.setPrenom("Inconnu");
            }
            if(patient.getNomNaissance() == null){
               patient.setNomNaissance("Inconnu");
            }
         }

         ManagerLocator.getPatientManager().createOrUpdateObjectManager(patient, null, medecins, liens,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(), null, filesCreated, null,
            SessionUtils.getLoggedUser(sessionScope), "creation", SessionUtils.getSystemBaseDir(), false);

      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   @Override
   protected void updateObject(){
      final List<File> filesCreated = new ArrayList<>();
      final List<File> filesToDelete = new ArrayList<>();

      try{
         prepareDataBeforeSave(false);

         ManagerLocator.getPatientManager().createOrUpdateObjectManager(patient, null, medecins, liens,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
            getObjectTabController().getFicheAnnotation().getValeursToDelete(), filesCreated, filesToDelete,
            SessionUtils.getLoggedUser(sessionScope), "modification", SessionUtils.getSystemBaseDir(), false);

         for(final File f : filesToDelete){
            f.delete();
         }

      }catch(final RuntimeException re){
         for(final File f : filesCreated){
            f.delete();
         }
         throw (re);
      }
   }

   @Override
   protected void setEmptyToNulls(){
      if(this.patient.getNip().equals("")){
         this.patient.setNip(null);
      }
      if(this.patient.getNomNaissance().equals("")){
         this.patient.setNomNaissance(null);
      }
      if(this.patient.getPrenom().equals("")){
         this.patient.setPrenom(null);
      }
      if(this.patient.getPaysNaissance().equals("")){
         this.patient.setPaysNaissance(null);
      }
      if(this.patient.getVilleNaissance().equals("")){
         this.patient.setVilleNaissance(null);
      }
      if(this.selectedSexe != null){
         if(selectedSexe.getCode() == null){
            throw new WrongValueException(sexeBox, Labels.getLabel("patient.error.sexe"));
         }
         patient.setSexe(this.selectedSexe.getCode());
      }
      
      setEmptyToNullEtat();
   }
   
   /**
    * @since 2.3.0-gatsbi état peut être null
    */   
   protected void setEmptyToNullEtat() {
      if(this.selectedEtat != null){
         this.patient.setPatientEtat(this.selectedEtat.getCode());
      }else{ //set 'Inconnu' par defaut
         this.patient.setPatientEtat("Inconnu");
      }
   }

   /**
    * Prepare les valeurs des attributs qui seront sauvées avec le
    * bean patient.
    * Recupere la liste de referents depuis le composant embarqué.
    * @param setMedecins specifiant si la liste de medecins doit être passée
    * par le setter (utile si fiche embarquée dans prélèvement)
    */
   public void prepareDataBeforeSave(final boolean setMedecins){
      
      checkRequiredListboxes();
      
      setEmptyToNulls();
      setFieldsToUpperCase();
      recordDateEtatDeces();

      this.medecins = getReferents().getMedecins();
      if(setMedecins && medecins.size() > 0){
         // cree la liste de PatientMedecins
         final Set<PatientMedecin> pmeds = new HashSet<>();
         PatientMedecin pm;
         for(int i = 0; i < medecins.size(); i++){
            pm = new PatientMedecin();
            pm.setPatient(this.patient);
            pm.setCollaborateur(medecins.get(i));
            pm.setOrdre(i + 1);
            pmeds.add(pm);
         }
         this.patient.setPatientMedecins(pmeds);
      }
   }
   
   /**
    * To be overriden by gatsbi
    */
   protected void checkRequiredListboxes(){
   }

   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(nipBox);
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(prenomBox);
      Clients.clearWrongValue(nomNaisBox);
      Clients.clearWrongValue(paysNaisBox);
      Clients.clearWrongValue(villeNaisBox);
   }

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/
   /**
    * Transforme la valeur 'sexe' récupérée de la base de données
    * en un objet utilisable par l'interface.
    * @param pat
    * @return LabelCodeItem sexe
    */
   public LabelCodeItem setSexeItemFromDBValue(final Patient pat){
      if(pat.getSexe() != null){
         if(pat.getSexe().equals("M")){
            return PatientUtils.SEXE_M;
         }else if(pat.getSexe().equals("F")){
            return PatientUtils.SEXE_F;
         }else{
            return PatientUtils.SEXE_IND;
         }
      }
      return PatientUtils.SEXE_EMPTY;
   }

   public LabelCodeItem getSelectedSexe(){
      return selectedSexe;
   }

   public void setSelectedSexe(final LabelCodeItem s){
      this.selectedSexe = s;
   }

   public LabelCodeItem getSelectedEtat(){
      return selectedEtat;
   }

   public void setSelectedEtat(final LabelCodeItem et){
      this.selectedEtat = et;
   }
   
   // @since 2.3.0-gatsbi, sera sur chargée
   public LabelCodeItem setPatientEtatFromValue(Patient pat){
      if(pat.getPatientEtat() != null){
         if(pat.getPatientEtat().equals("V")){
            if(!"F".equals(patient.getSexe())){
               return PatientUtils.ETAT_V;
            }else{
               return PatientUtils.ETAT_VF;
            }
         }else if(pat.getPatientEtat().equals("D")){
            if(!"F".equals(pat.getSexe())){
               return PatientUtils.ETAT_D;
            }else{
               return PatientUtils.ETAT_DF;
            }
         }else{
            return PatientUtils.ETAT_I;
         }
      }else{ // si new Patient
         if(!"F".equals(pat.getSexe())){
            return PatientUtils.ETAT_V;
         }else{
            return PatientUtils.ETAT_VF;
         }
      }
   }

   public List<LabelCodeItem> getSexes(){
      return PatientUtils.getSexes();
   }

   public List<LabelCodeItem> remove(final List<LabelCodeItem> etats){
      final Iterator<LabelCodeItem> i = etats.iterator();
      while(i.hasNext()){
         final LabelCodeItem o = i.next();
         if(o.getLabel() == Labels.getLabel("patient.etat.inconnu")){
            i.remove();
         }
      }
      return etats;
   }

   public List<LabelCodeItem> getEtats(){
      if(this.selectedSexe == null || !("F".equals(this.selectedSexe.getCode()))){
         return PatientUtils.getEtats();
      }
      return PatientUtils.getEtatsF();
   }

   public Date getDateEtatDeces(){
      return dateEtatDeces;
   }

   public void setDateEtatDeces(final Date dEd){
      this.dateEtatDeces = dEd;
   }

   public List<Collaborateur> getMedecins(){
      return this.medecins;
   }

   public String getReferentsGroupHeader(){
      return Labels.getLabel("patient.medecins") + "(" + String.valueOf(getReferents().getMedecins().size()) + ")";
   }

   public void onSelect$sexeBox(){
      if(selectedSexe.getCode() == null){
         throw new WrongValueException(sexeBox, Labels.getLabel("patient.error.sexe"));
      }

      Clients.clearWrongValue(sexeBox);
      accordEtatToGender();
   }

   /**
    * Modifie le label état en fonction du genre grammatical dicté
    * par le choix du sexe du patient (modes create/edit).
    */
   protected void accordEtatToGender(){
      if(!("F".equals(this.selectedSexe.getCode()))){
         if(this.selectedEtat != null && this.selectedEtat.getLabel().equals(Labels.getLabel("patient.etat.vivant.f"))){
            this.selectedEtat = PatientUtils.ETAT_V;
         }else if(this.selectedEtat != null && this.selectedEtat.getLabel().equals(Labels.getLabel("patient.etat.decede.f"))){
            this.selectedEtat = PatientUtils.ETAT_D;
         }
      }else{
         if(this.selectedEtat != null && this.selectedEtat.getLabel().equals(Labels.getLabel("patient.etat.vivant"))){
            this.selectedEtat = PatientUtils.ETAT_VF;
         }else if(this.selectedEtat != null && this.selectedEtat.getLabel().equals(Labels.getLabel("patient.etat.decede"))){
            this.selectedEtat = PatientUtils.ETAT_DF;
         }
      }
   }

   public void onSelect$etatBox(){
      accordDateToEtat();
   }

   /**
    * affecte la value de dateEtatDeces a la date etat ou a la date deces
    * en fonction de l'etat du patient.
    */
   protected void accordDateToEtat(){
      if(this.selectedEtat != null && ("D".equals(this.selectedEtat.getCode()))){
         this.dateEtatDecesField.setValue(Labels.getLabel("Champ.Patient.DateDeces"));
         dateEtatDeces = patient.getDateDeces();
      }else{
         this.dateEtatDecesField.setValue(Labels.getLabel("Champ.Patient.DateEtat"));
         dateEtatDeces = patient.getDateEtat();
      }
   }

   /**
    * enregistre la valeur dateEtatDeces en tant que date de l'etat
    * ou date de deces en fonction de l'état du patient.
    */
   protected void recordDateEtatDeces(){
      final boolean isDead = (this.selectedEtat != null && ("D".equals(this.selectedEtat.getCode())));
      if(isDead){
         this.patient.setDateDeces(this.dateEtatDeces);
         //			this.patient.setDateEtat(null);
      }else{
         this.patient.setDateDeces(null);
         this.patient.setDateEtat(this.dateEtatDeces);
      }
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBox(){
      nomBox.setValue(nomBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomNaisBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomNaisBox(){
      nomNaisBox.setValue(nomNaisBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * prenomBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$prenomBox(){
      prenomBox.setValue(prenomBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * paysNaisBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$paysNaisBox(){
      paysNaisBox.setValue(paysNaisBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * villeNaisBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$villeNaisBox(){
      villeNaisBox.setValue(villeNaisBox.getValue().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.patient.getNom() != null){
         this.patient.setNom(this.patient.getNom().toUpperCase().trim());
      }

      if(this.patient.getPrenom() != null){
         this.patient.setPrenom(this.patient.getPrenom().toUpperCase().trim());
      }

      if(this.patient.getNomNaissance() != null){
         this.patient.setNomNaissance(this.patient.getNomNaissance().toUpperCase().trim());
      }

      if(this.patient.getVilleNaissance() != null){
         this.patient.setVilleNaissance(this.patient.getVilleNaissance().toUpperCase().trim());
      }

      if(this.patient.getPaysNaissance() != null){
         this.patient.setPaysNaissance(this.patient.getPaysNaissance().toUpperCase().trim());
      }
   }

   @Override
   public void setFocusOnElement(){
      nipBox.setFocus(true);
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/
   public ConstCode getCodeNullConstraint(){
      return PatientConstraints.getCodeNullConstraint();
   }

   public ConstWord getNomConstraint(){
      return PatientConstraints.getNomConstraint();
   }

   public ConstWord getNomNullConstraint(){
      return PatientConstraints.getNomNullConstraint();
   }

   public ConstWord getVillePaysConstraint(){
      return ContexteConstraints.getVillePaysConstraint();
   }

   /**
    * Applique la validation sur la date et la date dependante.
    */
   public void onBlur$dateNaisBox(){
      Clients.clearWrongValue(dateNaisBox);
      validateCoherenceDate(dateNaisBox, dateNaisBox.getValue());
      Clients.clearWrongValue(dateEtatDecesBox);
      validateCoherenceDate(dateEtatDecesBox, dateEtatDecesBox.getValue());
   }

   /**
    * Applique la validation sur la date et la date dependante.
    */
   public void onBlur$dateEtatDecesBox(){
      Clients.clearWrongValue(dateEtatDecesBox);
      validateCoherenceDate(dateEtatDecesBox, dateEtatDecesBox.getValue());
      Clients.clearWrongValue(dateNaisBox);
      validateCoherenceDate(dateNaisBox, dateNaisBox.getValue());
   }

   @Override
   protected void validateCoherenceDate(final Component comp, final Object value){
      final Date dateValue = (Date) value;
      Errors errs = null;
      String field = "";

      if(dateValue == null || dateValue.equals("")){
         // la contrainte est retiree
         //((Datebox) comp).setConstraint("");
         ((Datebox) comp).clearErrorMessage(true);
         ((Datebox) comp).setValue(null);
         if(comp.getId().equals("dateNaisBox")){
            this.patient.setDateNaissance(dateValue);
         }
         if(comp.getId().equals("dateEtatDecesBox")){
            if(this.selectedEtat != null && ("D".equals(this.selectedEtat.getCode()))){ // mort
               this.patient.setDateDeces(dateValue);
            }else{ // vivant ou inconnu
               this.patient.setDateEtat(dateValue);
            }
         }

      }else{

         // date naissance
         if(comp.getId().equals("dateNaisBox")){
            field = "dateNaissance";
            this.patient.setDateNaissance(dateValue);
            errs = ManagerLocator.getPatientValidator().checkDateNaissanceCoherence(this.patient);
         }

         // date état
         if(comp.getId().equals("dateEtatDecesBox")){
            if(this.selectedEtat != null && ("D".equals(this.selectedEtat.getCode()))){ // mort
               field = "dateDeces";
               this.patient.setDateDeces(dateValue);
               this.patient.setPatientEtat("D");
               errs = PatientValidatorImpl.checkDateDecesCoherence(this.patient);
            }else{ // vivant ou inconnu
               field = "dateEtat";
               this.patient.setDateEtat(dateValue);
               errs = PatientValidatorImpl.checkDateEtatCoherence(this.patient);
            }
         }

         // Si la date n'est pas vide, on applique la contrainte
         if(errs != null && errs.hasErrors()){
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   /*************************************************************************/
   /****************** EMBEDDED INTO PRELEVEMENT*****************************/
   /*************************************************************************/

   /**
    * la fiche est embarquée donc en mode création uniquement sans affichage
    * des boutons. Une seule maladie doit être crée.
    * Le champ de formulaire ndaBox est affiché.
    */
   public void setEmbedded(final Patient pat){
      // mode embarqué
      setObject(pat);
      switchToCreateMode();
      this.ndaFieldLabel.setVisible(true);
      this.ndaBox.setVisible(true);
      toolbar.detach();
      setReferentsGroupOpen(true);
      this.winPanel.setTitle(Labels.getLabel("patient.reference.nouveau"));
      this.winPanel.setCollapsible(false);
      this.winPanel.setClosable(false);
      this.winPanel.setHeight("100%");
      this.winPanel.setClass("fichePanelv2Embedded");
      this.panelChildrenWithScroll.setStyle("overflow: visible");
      this.formGrid.setStyle("border-top-style: none");

      dateNaisBox.addEventListener("onBlur", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            ((CalendarBox) self.getParent().getParent().getParent().getParent().getParent().getParent()
               .getFellow("datePrelCalBox"))
                  .clearErrorMessage(((CalendarBox) self.getParent().getParent().getParent().getParent().getParent().getParent()
                     .getFellow("datePrelCalBox")).getValue());
            ((Datebox) self.getParent().getParent().getParent().getParent().getParent().getParent()
               .getFellow("dateConsentBoxPrlvt")).clearErrorMessage();
         }
      });

      getBinder().loadComponent(self);
   }

   /**
    * Raccourci quand clique 'return'.
    */
   @Override
   public void onOK(){
      if(!ndaBox.isVisible()){
         if(validate.isVisible()){
            Events.postEvent(new Event("onClick", validate));
         }else if(create.isVisible()){
            Events.postEvent(new Event("onClick", create));
         }
      }
   }

   public Textbox getNdaBox(){
      return ndaBox;
   }
   
   /**
    * Sera surhcargée par gatsbi, dans lequel le composant 
    * referentsGroup est un groupbox
    * @since 2.3.0-gatsbi
    * @param _o
    */
   protected void setReferentsGroupOpen(boolean _o) {
      ((Group) referentsGroup).setOpen(true);
   }
}
