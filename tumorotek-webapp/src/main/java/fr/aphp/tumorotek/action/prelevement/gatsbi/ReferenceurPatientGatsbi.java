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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.gatsbi.GatsbiControllerPatient;
import fr.aphp.tumorotek.action.prelevement.ReferenceurPatient;
import fr.aphp.tumorotek.decorator.MaladieDecorator;
import fr.aphp.tumorotek.decorator.gatsbi.PatientItemRendererGatsbi;
import fr.aphp.tumorotek.modales.DateModale;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 * @see <a href="http://docs.zkoss.org/wiki/Macro_Component">Macro_Component</a>
 * <p>
 */
public class ReferenceurPatientGatsbi extends ReferenceurPatient
{

   private static final long serialVersionUID = 1L;
   
   private Contexte contextePatient;
   
   private Row resultsRow;
   private Row findPatientRow;
   private Row resultsMaladiesRow;
   private Vbox newMaladieBox;
   
   private Patient patient;
   
   protected String getFichePatientComponent() {
      return "/zuls/patient/gatsbi/FichePatientEditGatsbi.zul";
   }
   
   private PatientItemRendererGatsbi patientRendererGatsbi = 
      new PatientItemRendererGatsbi(true, true);
   
   @Override
   public void doAfterCompose(Component comp) throws Exception{
      super.doAfterCompose(comp);
       
      contextePatient = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
      
      Map<Integer, String> widths = new HashMap<Integer, String>();
      widths.put(6, "50px"); // sexe listheader 50px
      
      GatsbiControllerPatient.drawListheadersForPatients(contextePatient, patientsBox, false, widths);  
      
      patientRendererGatsbi.setContexte(contextePatient);
      patientRendererGatsbi.setBanque(SessionUtils.getCurrentBanque(sessionScope));
      
      // ndaBox est contextualisé par prélèvement
      Contexte contextePrelevement = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);
      Div ndaDiv = (Div) ndaRow.getFellowIfAny("refNdaDiv");
      ndaDiv.setVisible(contextePrelevement == null || contextePrelevement.isChampIdVisible(44)); 
      GatsbiControllerPrelevement.applyPatientNdaRequiredLabel(ndaDiv);
   }
   
   @Override
   public void onCheck$radioGroup(){
      
      this.patient = null; // clear patient
      getMaladies().clear();

      super.onCheck$radioGroup();
   }
   
   @Override
   public Patient onGetPatientFromSelection(final Event e){
      
      Patient patSel = super.onGetPatientFromSelection(e);
      patSel.setBanque(SessionUtils.getCurrentBanque(sessionScope));
      return patSel;
   }
   
   @Override
   public void onClick$goForIt(){

      // nettoie les visites éventuelles qui aurait été ajoutées
      // à un autre patient
      getMaladies().clear();
      
      final String critereValue = nomNipNdaBox.getValue();

      final FichePrelevementEditGatsbi fichePrelevementEdit = 
            (FichePrelevementEditGatsbi) getFichePrelevementEditFromContexte();

      fichePrelevementEdit.getObjectTabController().setPatientSip(null);
      fichePrelevementEdit.openSelectPatientWindow(Path.getPath(self), "onGetPatientFromSelection", 
         false, critereValue, null, contextePatient, SessionUtils.getCurrentBanque(sessionScope));

   }
   
   @Override
   public FicheMaladie onClick$embedMaladieButton(){
      FicheMaladie ficheMaladie = super.onClick$embedMaladieButton();
      
      if (ficheMaladie.getObject().getPatient() == null) { // embedded new Patient
         ficheMaladie.getObject().setPatient(patient);
      }
      
      return ficheMaladie;
   }

   
   /**
    * Surcharge pour appliquer schéma de visites
    */
   @Override
   protected void embedFicheMaladie(FichePatientEdit fichePatient, Patient pat){
      if (GatsbiControllerPatient.getSchemaVisitesDefinedByEtude(sessionScope)) {
         
         // affiche uniquement la liste des maladies 
         // du composant patient existant
         existingPatientGrid.setVisible(true);
         findPatientRow.setVisible(false);
         resultsRow.setVisible(false);
         resultsMaladiesRow.setVisible(true);
         // maladiesBox.setVisible(true);
         newMaladieBox.setVisible(false);
                  
         this.patient = pat;
         pat.setBanque(SessionUtils.getCurrentBanque(sessionScope));
         
         DateModale.show(Labels.getLabel("gatsbi.schema.visites.title"), 
            Labels.getLabel("gatsbi.schema.visites.label"), null, true, self);
      } else {
         super.embedFicheMaladie(fichePatient, pat);
      }
   }
   
   @Override
   protected void displayExistingPatient(boolean show){
      super.displayExistingPatient(show);
      
      findPatientRow.setVisible(true);
      resultsRow.setVisible(true);      
   }
   
   @Override
   protected void fetchAndDecorateMaladieForPatient(Patient pat){
      
      patient = pat;

      // commence par ajouter les maladies communes à toutes les 
      // collections
      super.fetchAndDecorateMaladieForPatient(patient);
      
      // pour eviter Lazy
      patient.setMaladies(getMaladies()
         .stream().map(d -> d.getMaladie()).collect(Collectors.toSet()));
      
      patient.setBanque(SessionUtils.getCurrentBanque(sessionScope));
  
      // si patient n'a pas encore d'identifiant pour la collection, 
      // ajout du schéma de visites
      if (!patient.hasIdentifiant()) {
         DateModale.show(Labels.getLabel("gatsbi.schema.visites.title"), 
            Labels.getLabel("gatsbi.schema.visites.label"), null, true, self);
      } else { // ajout des visites existantes
         getMaladies().addAll(MaladieDecorator.decorateListe(
               ManagerLocator.getMaladieManager()
                  .findVisitesManager(patient, patient.getBanque())));
      }
   }
   
   /**
    * Une date d'inclusion (baseline) est renvoyée par la modale, 
    * la liste des visites peut être produite
    * @param e forwardEvent contenant la date
    */
   public void onFromDateProvided(final Event e){ 
      
//      getMaladies().clear();
      
      // production du schéma de visites
      List<MaladieDecorator> visiteDecos = MaladieDecorator.decorateListe(GatsbiControllerPatient
         .produceSchemaVisitesForPatient(SessionUtils.getCurrentBanque(sessionScope), patient, 
      ((Date) e.getData()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
      
      getMaladies().addAll(visiteDecos);
      
      if (!visiteDecos.isEmpty()) {
         setSelectedMaladie(visiteDecos.get(0));
         setFichePrelevementMaladie(visiteDecos.get(0).getMaladie());
      }
      
      setEmbeddedMaladieVisible(false);
   }
   
   @Override
   public ListitemRenderer<Patient> getPatientRenderer(){
      return patientRendererGatsbi;
   }
   
   @Override
   protected void createMaladieComponent(Div div){

      if(div.getChildren().isEmpty()){
         Executions.createComponents("/zuls/patient/gatsbi/FicheMaladieGatsbi.zul", div, null);
      }
   }
}
