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
package fr.aphp.tumorotek.action.prelevement;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.patient.serotk.FicheMaladieSero;
import fr.aphp.tumorotek.action.prelevement.serotk.FichePrelevementEditSero;
import fr.aphp.tumorotek.action.sip.SipFactory;
import fr.aphp.tumorotek.decorator.MaladieDecorator;
import fr.aphp.tumorotek.decorator.PatientItemRenderer;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.RisqueManager;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.PatientSipSejour;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller du référenceur Patient intégré dans la fiche prélèvement.
 * Propose le choix du référencement:
 * - nouveau patient
 * - recherche existant par nip, nom et nda
 * Affiche la fiche patient en mode 'create' par laquelle une seule maladie
 * pourra être créee ou une liste de patients permettant de
 * séléctionner une maladie ou d'en créer une nouvelle.
 *
 * @author mathieu
 * @version 2.3.0-gatsbi
 * @see <a href="http://docs.zkoss.org/wiki/Macro_Component">Macro_Component</a>
 * <p>
 * Date: 01/12/2009
 */
public class ReferenceurPatient extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 1L;

   // composants
   protected Radiogroup radioGroup;

   protected Grid existingPatientGrid;

   protected Textbox nomNipNdaBox;

   protected Listbox patientsBox;

   protected Row noPatientRow;

   protected Listbox maladiesBox;

   protected Row noMaladieRow;

   protected Button embedMaladieButton;

   protected Row embeddedFicheMaladieRow;

   protected Div embeddedFicheMaladieDiv;

   protected Radio newRadio;

   protected Radio findRadio;

   protected Radio noRadio;

   protected Div fichePatientDiv;

   protected Div ficheMaladieWithPatientDiv;

   protected Textbox ndaBox;

   protected Row ndaRow;

   private boolean banqueDefMaladies;

   // patientBox items
   private List<Patient> patients = new ArrayList<>();

   private Patient selectedPatient;

   // maladiesBox items
   private List<MaladieDecorator> maladies = new ArrayList<>();

   private MaladieDecorator selectedMaladie;

   private MaladieDecorator selectedMaladieByNda = null;

   private ListitemRenderer<Patient> patientRenderer = new PatientItemRenderer(true);

   protected AnnotateDataBinder referenceurBinder;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      referenceurBinder = new AnnotateDataBinder(comp);
      referenceurBinder.loadAll();

      // gestion des droits sur l'accès aux patients
      if(getDroitOnAction("Patient", "Consultation")){
         findRadio.setDisabled(false);
      }else{
         findRadio.setDisabled(true);
      }
      if(getDroitOnAction("Patient", "Creation")){
         newRadio.setDisabled(false);
      }else{
         newRadio.setDisabled(true);
      }
   }

   public Radio getNoRadio(){
      return noRadio;
   }

   /**
    * getter-setters.
    **/
   public List<Patient> getPatients(){
      return patients;
   }

   public ListitemRenderer<Patient> getPatientRenderer(){
      return patientRenderer;
   }

   public Patient getSelectedPatient(){
      return selectedPatient;
   }

   public void setSelectedPatient(final Patient selPat){
      this.selectedPatient = selPat;
   }

   public MaladieDecorator getSelectedMaladie(){
      return selectedMaladie;
   }

   public void setSelectedMaladie(final MaladieDecorator selMal){
      this.selectedMaladie = selMal;
   }

   public List<MaladieDecorator> getMaladies(){
      return maladies;
   }

   /***********************************************************/
   /*****************  Events controllers.  *******************/
   /***********************************************************/

   public Textbox getNdaBox(){
      return ndaBox;
   }

   /**
    * Initialize l'affichage du référenceur.
    *
    * @param defMaladies indique si la maladie sous-jacente doit être selectionnée
    *                    par défaut.
    */
   public void initialize(final boolean defMaladies){
      displayExistingPatient(true);
      displayEmbeddedPatient(false, null);
      if(!findRadio.isDisabled()){
         this.radioGroup.setSelectedIndex(2);
      }else{
         this.radioGroup.setSelectedIndex(0);
         displayNone();
      }
      this.banqueDefMaladies = defMaladies;
   }

   /**
    * Affiche la solution de référencement du patient en fonction du
    * radio bouton cliqué.
    */
   public void onCheck$radioGroup(){
      selectedPatient = null;
      selectedMaladie = null;
      selectedMaladieByNda = null;
      if(radioGroup.getSelectedItem().getValue().equals("find")){
         displayExistingPatient(true);
      }else if(radioGroup.getSelectedItem().getValue().equals("new")){
         displayEmbeddedPatient(true, new Patient());
      }else{
         displayNone();
      }
   }

   public void onOK$nomNipNdaBox(){
      onClick$goForIt();
   }

   public void onClick$goForIt(){

      final String critereValue = nomNipNdaBox.getValue();

      final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

      fichePrelevementEdit.getObjectTabController().setPatientSip(null);
      fichePrelevementEdit.openSelectPatientWindow(Path.getPath(self), 
                  "onGetPatientFromSelection", false, critereValue, null, null, null);

   }

   /**
    * Méthode permettant de recevoir le patient sélectionnés dans
    * la modal.
    *
    * @param e Event contenant le patient sélectionné.
    */
   public void onGetPatientFromSelection(final Event e){

      if(e.getData() != null){

         final Patient patSel = (Patient) e.getData();

         if(patSel.getPatientId() != null){

            this.patients = new ArrayList<>();
            patients.add(patSel);

            this.selectedPatient = patSel;
            selectPatientAuto(this.selectedPatient);

            referenceurBinder.loadAttribute(patientsBox, "model");

            this.patientsBox.setVisible(true);
            this.noPatientRow.setVisible(false);
            setVisibleNdaRow(true);

         }else{

            radioGroup.setSelectedItem(newRadio);
            displayEmbeddedPatient(true, patSel);

            final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

            fichePrelevementEdit.clearRisques();
            fichePrelevementEdit.clearProtocoles();

            if(SipFactory.isMessagesSip()){

               final PatientSip pSip = ManagerLocator.getPatientSipManager().findByNipLikeManager(patSel.getNip(), true).get(0);

               fichePrelevementEdit.getObjectTabController().setPatientSip(pSip);

               // copie me numero de sejour dans nda
               for(final PatientSipSejour sj : pSip.getSejours()){
                  if(sj.getNumero().equalsIgnoreCase(nomNipNdaBox.getValue())){
                     ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit")
                        .getAttributeOrFellow("fwinPatientEdit$composer", true)).getNdaBox().setValue(sj.getNumero());
                  }
               }

            }
         }
      }
   }

   /**
    * Méthode qui va setter le patient et la maladie après un interfacage.
    *
    * @param pat
    * @param maladie
    * @param nda
    * @since 2.0.13 passe le patientNda depuis l'interfaçage
    */
   public void setPatientAndMaladieFromOutSideReferenceur(final Patient pat, final Maladie maladie, final String nda){
      // si le patient existe
      if(pat.getPatientId() != null){
         this.patients = new ArrayList<>();
         patients.add(pat);

         this.selectedPatient = pat;
         selectPatientAuto(this.selectedPatient);

         referenceurBinder.loadAttribute(patientsBox, "model");

         this.patientsBox.setVisible(true);
         this.noPatientRow.setVisible(false);
         setVisibleNdaRow(true);

         // nda DIAMIC-TK 2.0.13 fix
         ndaBox.setValue(nda);
      }else{
         // sinon
         radioGroup.setSelectedItem(newRadio);
         displayEmbeddedPatient(true, pat);

         // nda DIAMIC-TK 2.0.13 fix
         ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit").getAttributeOrFellow("fwinPatientEdit$composer", true))
            .getNdaBox().setValue(nda);

         final FicheMaladie ficheMaladie =
            (FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true);
         final Maladie maladieInFiche = ficheMaladie.getObject();

         maladieInFiche.setLibelle(maladie.getLibelle());
         maladieInFiche.setCode(maladie.getCode());
         maladieInFiche.setDateDebut(maladie.getDateDebut());
         maladieInFiche.setDateDiagnostic(maladie.getDateDiagnostic());

         ficheMaladie.getBinder().loadAll();

         final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

         fichePrelevementEdit.clearRisques();
         fichePrelevementEdit.clearProtocoles();

      }
   }

   /**
    * Affiche la liste de maladies du patient selectionné si la banque définit
    * les maladies. Affiche le lien d'ajout d'une nouvelle maladie qui sera
    * enregistrée conjointement à la création du prélèvement.
    * Selectionne ou cree la maladie sous-jacente sinon.
    */
   public void onSelect$patientsBox(){
      selectPatientAuto(selectedPatient);
   }

   /**
    * Composante de la methode onSelect$patientsBox() qui permet la selection
    * automatique du premier patient quand eppelée hors de cette methode.
    *
    * @param selected
    * @since 2.0.13 fixs System Maladie defaut existant/non existant
    */
   private void selectPatientAuto(final Patient selected){

      final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

      if(this.banqueDefMaladies){

         final List<Maladie> res = new ArrayList<>(ManagerLocator.getMaladieManager().findByPatientNoSystemManager(selected));

         this.maladies = MaladieDecorator.decorateListe(res);
         //selectionne automatiquement la premiere maladie
         if(maladies.size() > 0){
            if(selectedMaladieByNda == null){

               this.selectedMaladie = this.maladies.get(0);

               fichePrelevementEdit.setMaladie(res.get(0));

            }else{
               this.selectedMaladie = this.maladies.get(maladies.indexOf(selectedMaladieByNda));
               fichePrelevementEdit.setMaladie(selectedMaladieByNda.getMaladie());
            }
         }
         referenceurBinder.loadAttribute(maladiesBox, "model");

         Components.removeAllChildren(embeddedFicheMaladieDiv);
         setEmbeddedMaladieVisible(false);
      }
      // selectionne la maladie sous-jacente ou la cree
      else{

         final List<Maladie> res =
            new ArrayList<>(ManagerLocator.getManager(MaladieManager.class).findByPatientManager(selected));

         // 2.0.13 fix ajout maladie defaut system
         Maladie maladie = res.stream().filter(Maladie::getSystemeDefaut).findFirst().orElse(null);

         // cree une maladie defaut system si inexistante
         if(maladie == null){
            maladie = new Maladie();
            maladie.setPatient(selected);
            maladie.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getNom() + "-defaut");
            maladie.setSystemeDefaut(true);
         }

         fichePrelevementEdit.setMaladie(maladie);

      }

      final List<Risque> risques = ManagerLocator.getManager(RisqueManager.class).findByPatientAndPlateformeManager(selected,
         SessionUtils.getPlateforme(sessionScope));
      fichePrelevementEdit.selectRisques(risques);

   }

   /**
    * Factorisation de la méthode de récupération du controller
    * FichePrelevementEdit en fonction du contexte collection.
    * @return
    */
   protected FichePrelevementEdit getFichePrelevementEditFromContexte(){
      switch(SessionUtils.getCurrentContexte()){
         case SEROLOGIE:
            return (FichePrelevementEditSero) self.getParent().getParent()
               .getAttributeOrFellow("fwinPrelevementEditSero$composer", true);
         default:
            return (FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true);
      }
   }

   /**
    * Assigne la référence vers la maladie à la fiche prélèvement.
    */
   public void onSelect$maladiesBox(){

      final Maladie selected = ((MaladieDecorator) this.maladiesBox.getSelectedItem().getValue()).getMaladie();

      final FichePrelevementEdit fichePrelevement = getFichePrelevementEditFromContexte();

      fichePrelevement.setMaladie(selected);

   }

   /**
    * Affiche le formulaire maladie embarqué permettant d'enregistrer
    * 'localement' une maladie dont la référence sera assignée à la fiche
    * prélèvement. Cette maladie ne sera effectivement crée que lors de
    * la création du prélèvement.
    * Efface le bouton et le message 'noMaladies', et rend la liste de
    * maladies inacessible.
    */
   public void onClick$embedMaladieButton(){

      createMaladieComponent(embeddedFicheMaladieDiv);
      setEmbeddedMaladieVisible(true);

      final FicheMaladie ficheMaladie;
      final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();
      switch(SessionUtils.getCurrentContexte()){
         case SEROLOGIE:
            ficheMaladie = (FicheMaladieSero) embeddedFicheMaladieDiv.getFellow("fwinMaladie")
               .getAttributeOrFellow("fwinMaladie$composer", true);
            break;
         default:
            ficheMaladie =
               (FicheMaladie) embeddedFicheMaladieDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true);
            break;
      }

      // informe la fiche prelevement de la presence du formulaire
      ficheMaladie.setEmbedded(true);
      ficheMaladie.getObject().setPatient(selectedPatient);
      fichePrelevementEdit.setMaladieEmbedded(true);
   }

   /**
    * Event controller fermeture fiche embarquée maladie.
    */
   public void onCloseMaladieClick(){

      Components.removeAllChildren(embeddedFicheMaladieDiv);
      setEmbeddedMaladieVisible(false);

      final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

      // informe la fiche prelevement de l'absence
      fichePrelevementEdit.setMaladieEmbedded(false);

   }

   /***********************************************************************/
   /*****************  Gestionnaires d'affichage composants. **************/
   /***********************************************************************/

   /**
    * Affiche ou non la partie concernant les maladies (liste, bouton,
    * fiche embarquée).
    *
    * @param bool
    */
   private void setEmbeddedMaladieVisible(final boolean bool){
      this.embeddedFicheMaladieRow.setVisible(bool);
      this.embedMaladieButton.setVisible(!bool);

      if(!bool){
         // affiche la liste de resultats ou no match
         if(this.maladies.size() > 0){
            if(this.maladies.size() < 5){
               this.maladiesBox.setPageSize(this.maladies.size());
            }else{
               this.maladiesBox.setPageSize(5);
            }
            this.maladiesBox.setVisible(true);
            this.noMaladieRow.setVisible(false);
         }else{
            this.maladiesBox.setVisible(false);
            this.noMaladieRow.setVisible(true);
         }
      }else{
         this.maladiesBox.setVisible(false);
         this.noMaladieRow.setVisible(false);
      }
   }

   public PatientController getObjectTabController(){
      return getObjectTabController();
   }

   /**
    * Affiche la fiche embarquée de création d'un nouveau patient
    * conjointement à la création d'un prélèvement.
    *
    * @param show
    * @param pat
    */
   private void displayEmbeddedPatient(final boolean show, final Patient pat){

      self.getFellow("newPatientDiv").setVisible(show);

      if(show){

         Components.removeAllChildren(fichePatientDiv);

         Executions.createComponents(getFichePatientComponent(), fichePatientDiv, null);
         final FichePatientEdit fichePatient = (FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit")
            .getAttributeOrFellow("fwinPatientEdit$composer", true);

         fichePatient.setEmbedded(pat);

         Components.removeAllChildren(embeddedFicheMaladieDiv);
         createMaladieComponent(ficheMaladieWithPatientDiv);

         final FicheMaladie ficheMaladie;
         switch(SessionUtils.getCurrentContexte()){
            case SEROLOGIE:
               ficheMaladie = (FicheMaladieSero) ficheMaladieWithPatientDiv.getFellow("fwinMaladie")
                  .getAttributeOrFellow("fwinMaladie$composer", true);
               break;
            default:
               ficheMaladie = (FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie")
                  .getAttributeOrFellow("fwinMaladie$composer", true);
               break;
         }

         ficheMaladie.setEmbedded(true);

         if(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefMaladies()){
            self.getFellow("newPatientDiv").getFellow("newMaladieBox").setVisible(true);
         }else{
            // efface le bloc div
            self.getFellow("newPatientDiv").getFellow("newMaladieBox").setVisible(false);
            // cree la maladie sous-jacente
            ((Textbox) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getFellow("libelleBox"))
               .setValue((SessionUtils.getSelectedBanques(sessionScope).get(0).getNom() + "-defaut"));
            ficheMaladie.getObject().setSystemeDefaut(true);

         }

         // attribution du patient a la maladie embedded
         ficheMaladie.getObject().setPatient(fichePatient.getObject());

         // efface la grid existing patient
         displayExistingPatient(false);

      }else{
         // detache les composants
         Components.removeAllChildren(fichePatientDiv);
         Components.removeAllChildren(ficheMaladieWithPatientDiv);
      }

      final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

      fichePrelevementEdit.setPatientEmbedded(show);
   }
   
   // sera surchargé par gatsbi
   protected String getFichePatientComponent() {
      return "/zuls/patient/FichePatientEdit.zul";
   }

   /**
    * Affiche la grid permettant la selection d'un patient existant
    * et la sélection ou l'ajout de la maladie associée.
    *
    * @param show
    */
   private void displayExistingPatient(final boolean show){

      this.existingPatientGrid.setVisible(show);

      if(show && selectedPatient != null){
         setVisibleNdaRow(true);
      }else{
         setVisibleNdaRow(false);
      }


      final FichePrelevementEdit fichePrelevementEdit = getFichePrelevementEditFromContexte();

      if(show){

         // efface les listes
         this.patientsBox.setVisible(false);
         this.maladiesBox.setVisible(false);
         this.noPatientRow.setVisible(false);
         this.noMaladieRow.setVisible(false);
         this.embedMaladieButton.setVisible(false);
         this.embeddedFicheMaladieRow.setVisible(false);

         // efface la fiche maladie
         Components.removeAllChildren(embeddedFicheMaladieDiv);

         // efface la grid existing patient
         displayEmbeddedPatient(false, null);
         this.patients.clear();
         this.selectedPatient = null;
         this.maladies.clear();
         this.selectedMaladie = null;

         fichePrelevementEdit.setMaladie(null);

      }else{

         fichePrelevementEdit.setMaladieEmbedded(false);

      }
   }

   /**
    * Affiche aucun composant de référencement vers un patient-maladie.
    */
   private void displayNone(){
      displayEmbeddedPatient(false, null);
      displayExistingPatient(false);
   }

   public AnnotateDataBinder getReferenceurBinder(){
      return referenceurBinder;
   }

   public void setReferenceurBinder(final AnnotateDataBinder refBinder){
      this.referenceurBinder = refBinder;
   }

   /**
    * Test si une action est réalisable en fonction des
    * droits de l'utilisateur.
    *
    * @param nomEntite    Entite (ex.:ProdDerive).
    * @param nomOperation Type d'operation du bouton.
    */

   public boolean getDroitOnAction(final String nomEntite, final String nomOperation){
      Boolean admin = false;
      if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin => bouton cliquable
      if(admin){
         return true;
      }
      // on extrait l'OperationType de la base
      final OperationType opeation = ManagerLocator.getOperationTypeManager().findByNomLikeManager(nomOperation, true).get(0);

      Hashtable<String, List<OperationType>> droits = new Hashtable<>();

      if(sessionScope.containsKey("Droits")){
         // on extrait les droits de l'utilisateur
         droits = (Hashtable<String, List<OperationType>>) sessionScope.get("Droits");

         final List<OperationType> ops = droits.get(nomEntite);
         return ops.contains(opeation);
      }
      return false;

   }

   /**
    * Injection contexte dans la creation du formulaire
    * maladie.
    */
   protected void createMaladieComponent(final Div div){

      final String zulPath;

      switch(SessionUtils.getCurrentContexte()){
         case SEROLOGIE:
            zulPath = "/zuls/patient/serotk/FicheMaladieSero.zul";
            break;
         default:
            zulPath = "/zuls/patient/FicheMaladie.zul";
            break;
      }

      if(div.getChildren().isEmpty()){
         Executions.createComponents(zulPath, div, null);
      }
   }
   
   // sera surchargé par gatsbi
   public void setVisibleNdaRow(boolean b) {
      ndaRow.setVisible(b);
   }
   
   public ConstCode getNdaConstraint(){
      return PrelevementConstraints.getNdaConstraint();
   }
}
