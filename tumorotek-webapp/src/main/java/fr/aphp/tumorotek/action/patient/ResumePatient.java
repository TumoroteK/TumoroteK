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
package fr.aphp.tumorotek.action.patient;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * HtmlMacroComponent de la fiche résumé Patient.
 * Permet l'assignement dynamique des informations
 * @see http://docs.zkoss.org/wiki/Macro_Component
 *
 * Affiche le composant nda en static et en modifiable.
 *
 * Date: 01/12/0209
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class ResumePatient
{

   private final Page page;

   private Maladie maladie;

   private Prelevement prelevement;

   private final Textbox ndaBox;
   private final Label ndaLabel;
   private final Label linkMaladieLabel;
   private final Label codeDiagLabel;
   private final Label linkPatientLabel;
   private final Label nipLabel;
   private final Label identifiantLabel;
   public String nip;
   private final Label prenomLabel;
   private final Label dateNaisLabel;
   private final Label sexeLabel;
   private Row row1;
   private Row row2;
   private Row row3;
   private Row row5;

   private boolean anonyme = false;

   // @since 2.3.0-gatsbi
   private Contexte patientContexte = null;

   private Div mainContainer;

   private Component linkMaladie;

   public ResumePatient(final Component resumePatientGroup, final Contexte _c){

      this.patientContexte = _c;
      page = resumePatientGroup.getPage();

      if(patientContexte == null){
         
         identifiantLabel = null;
         
         // first row
         row1 = (Row) resumePatientGroup.getNextSibling();
         nipLabel = (Label) row1.getFellowIfAny("nipLabel");
         ndaBox = (Textbox) row1.getFellowIfAny("ndaBox");
         ndaLabel = (Label) row1.getFellowIfAny("ndaLabel");

         // second Row
         row2 = (Row) resumePatientGroup.getNextSibling().getNextSibling();
         linkPatientLabel = (Label) row2.getFellowIfAny("linkPatientLabel");
         prenomLabel = (Label) row2.getFellowIfAny("prenomLabel");

         // third Row
         row3 = (Row) resumePatientGroup.getNextSibling().getNextSibling().getNextSibling();
         dateNaisLabel = (Label) row3.getFellowIfAny("dateNaisLabel");
         sexeLabel = (Label) row3.getFellowIfAny("sexeLabel");

         // fourth Row
         linkMaladie = resumePatientGroup.getNextSibling().getNextSibling().getNextSibling().getNextSibling();

         // fifth Row
         row5 = (Row) resumePatientGroup.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
         linkMaladieLabel = (Label) row5.getFellowIfAny("linkMaladieLabel");
         codeDiagLabel = (Label) row5.getFellowIfAny("codeDiagLabel");
      }else{ // gatsbi contexte
                  
         // labels mapping
         mainContainer = (Div) resumePatientGroup.getFellowIfAny("patientBlockDivContainer");
         
         identifiantLabel = (Label) resumePatientGroup.getFellowIfAny("identifiantLabel");        
         nipLabel = (Label) resumePatientGroup.getFellowIfAny("nipLabel");
         ndaBox = (Textbox) resumePatientGroup.getFellowIfAny("ndaBox");
         ndaLabel = (Label) resumePatientGroup.getFellowIfAny("ndaLabel");
         linkPatientLabel = (Label) resumePatientGroup.getFellowIfAny("linkPatientLabel");
         prenomLabel = (Label) resumePatientGroup.getFellowIfAny("prenomLabel");
         dateNaisLabel = (Label) resumePatientGroup.getFellowIfAny("dateNaisLabel");
         sexeLabel = (Label) resumePatientGroup.getFellowIfAny("sexeLabel");
         linkMaladie = resumePatientGroup.getFellowIfAny("linkMaladie");
         linkMaladieLabel = (Label) resumePatientGroup.getFellowIfAny("linkMaladieLabel");
         codeDiagLabel = (Label) resumePatientGroup.getFellowIfAny("codeDiagLabel");         
      }
   }

   /**
    * Assigne les valeurs aux labels représentant les informations
    * de la maladie. Cette méthode est le point d'entrée des informations
    * dans la fiche résumé patient à partir de la fiche prélèvement.
    * @param mal
    */
   public void setMaladie(final Maladie mal){
      this.maladie = mal;

      if(this.maladie != null){
         linkMaladieLabel.setValue(this.maladie.getLibelle());
         if(this.maladie.getCode() != null){
            codeDiagLabel.setValue(this.maladie.getCode());
         }else{
            codeDiagLabel.setValue(null);
         }
         setPatientProperties(this.maladie.getPatient());
      }else{
         linkMaladieLabel.setValue(null);
         codeDiagLabel.setValue(null);
         setPatientProperties(null);
      }
   }

   /**
    * Assigne les valeurs aux labels représentant les informations
    * du nda du associé au patient.
    * @param prel
    */
   public void setPrelevement(final Prelevement prel){
      this.prelevement = prel;
      if(this.prelevement != null){
         if(!anonyme){
            if(ndaLabel != null){
               ndaLabel.setValue(this.prelevement.getPatientNda());
            }
            if(ndaBox != null){
               ndaBox.setValue(this.prelevement.getPatientNda());
            }
         }else{
            if(ndaLabel != null){
               ndaLabel.setValue("                                 ");
               ndaLabel.setSclass("formAnonymeBlock");
               ndaLabel.setPre(true);
            }
         }
      }else{
         if(ndaLabel != null){
            ndaLabel.setValue(null);
         }
      }
   }

   public void setPatientAccessible(final boolean accessible){
      if(accessible){
         if(!anonyme && patientContexte == null){
            linkPatientLabel.setSclass("formLink");
         }
         linkMaladieLabel.setSclass("formLink");
         
         // Events listeners
         if (patientContexte == null) { // pas de lien sur ce champ dans Contexte Gatsbi
            linkPatientLabel.addEventListener("onClick", new EventListener<Event>()
            {
               @Override
               public void onEvent(final Event event) throws Exception{
                  showPatientPanel(false);
               }
            });
         } else { //link ajouté sur identifiant
            identifiantLabel.addEventListener("onClick", new EventListener<Event>()
            {
               @Override
               public void onEvent(final Event event) throws Exception{
                  showPatientPanel(false);
               }
            });
         }
         
         linkMaladieLabel.addEventListener("onClick", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               showPatientPanel(true);
            }
         });
      }else{
         if(!anonyme){
            linkPatientLabel.setSclass("formValue");
         }else{
            AbstractController.makeLabelAnonyme(linkPatientLabel, false);
         }
         linkMaladieLabel.setSclass("formValue");
      }
   }

   /**
    * Assigne les valeurs aux labels représentant les informations
    * du patient. Si le patient est null, efface la fiche résumé.
    * @param patient
    */
   public void setPatientProperties(final Patient patient){
      
      // @since 2.3.0-gatsbi
      if (patientContexte != null) { // identifiant must be set
         identifiantLabel.setValue(patient.getIdentifiantAsString(prelevement.getBanque()));
      }
      
      if(!anonyme){
         if(patient != null){        
            if (nipLabel.getParent().isVisible()) {
                nipLabel.setValue(patient.getNip());
            }
            if (linkPatientLabel.getParent().isVisible()) {
               linkPatientLabel.setValue(patient.getNom());
            }
            if (prenomLabel.getParent().isVisible()) {
               prenomLabel.setValue(patient.getPrenom());
            }
            if (dateNaisLabel.getParent().isVisible()) {
               dateNaisLabel.setValue(ObjectTypesFormatters.dateRenderer2(patient.getDateNaissance()));
            }
            if (sexeLabel.getParent().isVisible()) {
               sexeLabel.setValue(PatientUtils.setSexeFromDBValue(patient));
            }
         }else{ //efface les propriétés du résumé
            nipLabel.setValue(null);
            linkPatientLabel.setValue(null);
            prenomLabel.setValue(null);
            dateNaisLabel.setValue(null);
            sexeLabel.setValue(null);
         }
      }else{
         if (nipLabel.getParent().isVisible()) {
            AbstractController.makeLabelAnonyme(nipLabel, false);
         }
         if (prenomLabel.getParent().isVisible()) {
            AbstractController.makeLabelAnonyme(prenomLabel, false);
         }
         if (dateNaisLabel.getParent().isVisible()) {
            AbstractController.makeLabelAnonyme(dateNaisLabel, false);
         }
         if(patient != null){
            if (sexeLabel.getParent().isVisible()) {
               sexeLabel.setValue(PatientUtils.setSexeFromDBValue(patient));
            }
            if (linkPatientLabel.getParent().isVisible()) {
               AbstractController.makeLabelAnonyme(linkPatientLabel, patientContexte == null); // lien non cliquable si contexte gatsbi
            }
         }else{ //efface les propriétés du résumé
            if (sexeLabel.getParent().isVisible()) {  
               sexeLabel.setValue(null);
            }
            if (linkPatientLabel.getParent().isVisible()) {
               AbstractController.makeLabelAnonyme(linkPatientLabel, false);
            }
         }
      }
   }

   /**
    * Affiche la fiche d'un patient.
    * @param opensMaladie ouvre le panel de la maladie également.
    */
   public void showPatientPanel(final boolean opensMaladie){
      // on récupère les panels
      final Tabbox panels = (Tabbox) page.getFellow("mainWin").getFellow("main").getFellow("mainTabbox");

      if(panels != null && this.maladie != null){

         // on récupère le panel patient
         final Tabpanel panel = (Tabpanel) panels.getFellow("patientPanel");

         if(!panel.hasFellow("winPatient")){
            final Component comp = Executions.createComponents("/zuls/patient/Patient.zul", panel, null);
            comp.setId("winPatient");
         }

         final PatientController tabController =
            ((PatientController) panel.getFellow("winPatient").getAttributeOrFellow("winPatient$composer", true));

         // si on arrive à récupérer le panel échantillon et son controller
         if(tabController != null){
            tabController.switchToFicheStaticMode(this.maladie.getPatient());
            panels.setSelectedPanel(panel);
            if(opensMaladie){ //ouvre le panel maladie directement
               ((FichePatientStatic) tabController.getFicheStatic()).openMaladiePanel(this.maladie);
            }
         }
      }
   }

   public void hideMaladieRows(final boolean visible){
      linkMaladie.setVisible(visible);
      if(patientContexte == null){
         linkMaladie.getNextSibling().setVisible(visible);
      }
   }

   /**
    * Affiche ou non le box permettant de renseigner le NDA.
    * @param visible
    */
   public void setNdaBoxVisible(final boolean visible){
      if(ndaBox != null){
         ndaBox.setVisible(visible);
      }
      if(ndaLabel != null){
         ndaLabel.setVisible(!visible);
      }
   }

   public boolean isAnonyme(){
      return anonyme;
   }

   public void setAnonyme(final boolean ano){
      this.anonyme = ano;
   }

   public Maladie getMaladie(){
      return maladie;
   }

   public Prelevement getPrelevement(){
      return prelevement;
   }

   public Textbox getNdaBox(){
      return ndaBox;
   }

   public void setVisible(final boolean b){
      if(patientContexte == null){
         row1.setVisible(b);
         row2.setVisible(b);
         row3.setVisible(b);
         linkMaladie.setVisible(b);
         row5.setVisible(b);
      }else{ // contexte gatsbi
         if(mainContainer != null){
            mainContainer.setVisible(b);
         }
      }
   }
}
