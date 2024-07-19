/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.patient.gatsbi;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.LabelCodeItem;
import fr.aphp.tumorotek.action.patient.PatientConstraints;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.action.prelevement.gatsbi.GatsbiControllerPrelevement;
import fr.aphp.tumorotek.modales.DateModale;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.gatsbi.PatientIdentifiant;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import org.zkoss.util.resource.Labels;

/**
 *
 * Controller gérant la fiche formulaire d'un prélèvement sous le gestionnaire
 * GATSBI. Controller créé le 29/08/2022.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePatientEditGatsbi extends FichePatientEdit
{

   private static final long serialVersionUID = 1L;

   private final List<Listbox> reqListboxes = new ArrayList<>();

   private Contexte contexte;
   
   private Div identifiantDiv;
   private Div patientNdaDiv;
   private Groupbox groupVisites;
   
   // utilisé pour gérer l'evt de retour de la provision 
   // de la date d'inclusion du schéma de visite
   // si true, c'est le ReferenceurPatient qui gère l'evt
   private boolean embedded  = false;
   
   private VisiteItemRenderer visiteItemRenderer = new VisiteItemRenderer();
   private List<Maladie> visites = new ArrayList<Maladie>();
   
   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      contexte = GatsbiController.initWireAndDisplay(this, 1, true, reqListboxes, null, null, new Groupbox[]{});
   }
   
   @Override
   public void setNewObject(){
      super.setNewObject();
      // banque (transient) utilisée pour la validation
      getObject().setBanque(SessionUtils.getCurrentBanque(sessionScope));
   }
   
   @Override
   public LabelCodeItem setSexeItemFromDBValue(final Patient pat){
      if(pat.getSexe() == null) {
         if (contexte.isChampIdRequired(6)) { // null mais champ obligatoire -> valeur par défaut
            return PatientUtils.SEXE_IND;
         } else {
            return null;
         }
      } else {
         return super.setSexeItemFromDBValue(pat);
      }
   }
   
   @Override
   public LabelCodeItem setPatientEtatFromValue(final Patient pat){
      if(pat.getPatientEtat() == null) { 
         if (contexte.isChampIdRequired(10)) { // null mais champ obligatoire -> valeur par défaut
            return PatientUtils.ETAT_I;
         } else {
            return null;
         }
      } else {
         return super.setPatientEtatFromValue(pat);
      }
   }
   
   /**
    * Nom peut être null
    */
   @Override
   protected void setEmptyToNulls(){
      super.setEmptyToNulls();
      if(this.patient.getNom().equals("")){
         this.patient.setNom(null);
      }
   }
   
   /**
    * Etat peut être null
    */
   @Override
   public void setEmptyToNullEtat() {
      if(this.selectedEtat != null){
         this.patient.setPatientEtat(this.selectedEtat.getCode());
      }
   }
   
   /**
    * Surcharge Gastbi pour conserver sélectivement la
    * contrainte de sélection obligatoire des listes nature et statut juridique
    * dans le contexte TK historique
    */
   @Override
   protected void checkRequiredListboxes(){
      GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
   }
   
   //initialement, il était prévu de gérer les champs date d'état et date de décès séparément d'où la surchage ci-dessous
   //mais Nathalie préfère que ce soit géré comme dans TK (TG-138) => mise en commentaire du bloc ci-dessous (au cas où l'idée de séparer revienne)
//   /**
//    * Surcharge Gastbi car accord affichage entre date état/décès et état 
//    * n'est plus applicable.
//    */
//   @Override
//   protected void accordDateToEtat() {
//   }
//   
//   @Override
//   protected void recordDateEtatDeces() {
//   }
   
   @Override
   protected void setReferentsGroupOpen(boolean _o) {
      ((Groupbox) referentsGroup).setOpen(_o);
   }
   
   @Override
   public void setEmbedded(Patient pat){
      super.setEmbedded(pat);
      
      groupVisites.setVisible(false);
      this.embedded = true;
            
      // nda contextualisé par prélèvement
      Contexte contextePrelevement = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);
      if (contextePrelevement != null) { // gatsbi peut rendre ndaDiv invisible 
         patientNdaDiv.setVisible(contextePrelevement.isChampIdVisible(44));
         GatsbiControllerPrelevement.applyPatientNdaRequiredLabel(patientNdaDiv);
      } else {
         patientNdaDiv.setVisible(true);
      }
      
      if (patientNdaDiv.isVisible()) {
         identifiantDiv.setSclass("item item-mid item-required");
      }
   }
   
   // TODO toutes collections ? Afficher une liste ? 
   //TG-182
   // CHT : pas modifiable en toutes collections => on ne va pas dans ce code lié à l'édition...
   public String getIdentifiant() {
      return patient.getIdentifiant();
   }
   
   // TODO toutes collections = non modifiable
   //TG-182
   public void setIdentifiant(String identifiant) {
      PatientIdentifiant currPatIdent = patient.getPatientIdentifiant();
      currPatIdent.setIdentifiant(identifiant);
      
      // met à jour (si nécessaire) la relation patient-banque-identifiant
      patient.addToIdentifiants(currPatIdent);
   }
   
   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();
      
      // schema de visites
      if (GatsbiControllerPatient.getSchemaVisitesDefinedByEtude(sessionScope)) {
         groupVisites.setVisible(true);
         
         // ouverture d'une modale de saisie de date baseline
         // une fois le composant FicheEditPatient rendu visible
         Events.echoEvent("onLaterSwitchToCreateMode", self, null);
      }
   }
   
   /**
    * Une fois la fiche Patient Edit affichée en create mode, 
    * si un schéma de visite pour l'étude est défini, il implique la saisie d'une date baseline
    * donc l'affichage de la modale de saisie de la date.
    */
   public void onLaterSwitchToCreateMode() {
      if (!embedded) {
         DateModale.show(Labels.getLabel("gatsbi.schema.visites.title"), 
            Labels.getLabel("gatsbi.schema.visites.label"), null, false, self);
      }   
   }
   
   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      
      // inclusion d'un patient existant dans une collection gatsbi
      //TG-182
      if (!patient.hasIdentifiant() 
         && GatsbiControllerPatient.getSchemaVisitesDefinedByEtude(sessionScope)) {
         
         patient.setMaladies(new HashSet<Maladie>(
            ManagerLocator.getMaladieManager().findByPatientNoSystemNorVisiteManager(patient)));
         
         groupVisites.setVisible(true);
         
         // ouverture d'une modale de saisie de date baseline
         // une fois le composant FicheEditPatient rendu visible
         Events.echoEvent("onLaterSwitchToEditMode", self, null);
      }
   }
   
   /**
    * Une fois la fiche Patient Edit affichée en edition mode, si le patient n'est pas 
    * inclu dans la collection gatsbi (pas d'identitifant trouvé pour la collection)
    * et si un schéma de visite pour l'étude est défini, il implique la saisie d'une date baseline
    * donc l'affichage de la modale de saisie de la date.
    */
   public void onLaterSwitchToEditMode() {
      DateModale.show(Labels.getLabel("gatsbi.schema.visites.title"), 
         Labels.getLabel("gatsbi.schema.visites.label"), null, false, self);
   }
   
   @Override
   public void setInconnus(Patient patient){
   }
   
   /**
    * Si un schéma de visites a été ajouté, 
    * prépare les objets patients et sa liste de maladies/visites 
    * à être modifiés = maladies existantes + visites
    * @return maladies
    */
   @Override
   protected List<Maladie> prepareMaladies(Patient patient){
      if (!visites.isEmpty()) {
         
         patient.getMaladies().removeIf(m -> m.getMaladieId() == null);
         
         List<Maladie> maladies = new ArrayList<Maladie>();
         maladies.addAll(patient.getMaladies());
         maladies.addAll(visites);
         
         return maladies;
      }
      return null;
   }
   
   /**
    * Une date d'inclusion (baseline) est renvoyée par la modale, 
    * la liste des visites peut être produite
    * @param e forwardEvent contenant la date
    */
   public void onFromDateProvided(final Event e){  
      
      visites.clear();
      
      try {
         // production du schéma de visites
         visites.addAll(GatsbiControllerPatient.produceSchemaVisitesForPatient(SessionUtils.getCurrentBanque(sessionScope), patient, 
            ((Date) e.getData()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
      } catch (Exception ex) {
         Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }
   
   public String getVisitesGroupHeader() {
      if (groupVisites.isVisible()) {
         return Labels.getLabel("gatsbi.schema.visites", new String[] { String.valueOf(patient.getMaladies().size())});
      }
      return null;
   }

   public VisiteItemRenderer getVisiteItemRenderer(){
      return visiteItemRenderer;
   }  
   
   public List<Maladie> getVisites() {
      return visites;
   }
   
   public ConstCode getIdentifiantConstraint(){
      return PatientConstraints.getIdentifiantConstraint();
   }
}