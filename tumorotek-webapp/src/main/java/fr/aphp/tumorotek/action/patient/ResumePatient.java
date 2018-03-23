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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Group;
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

/**
 * HtmlMacroComponent de la fiche résumé Patient.
 * Permet l'assignement dynamique des informations
 * @see http://docs.zkoss.org/wiki/Macro_Component
 *
 * Affiche le composant nda en static et en modifiable.
 *
 * Date: 01/12/0209
 *
 * @author mathieu
 * @version 2.0
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
   public String nip;
   private final Label prenomLabel;
   private final Label dateNaisLabel;
   private final Label sexeLabel;

   private final Row row1;
   private final Row row2;
   private final Row row3;
   private final Row linkMaladie;
   private final Row row5;
   private boolean anonyme = false;

   //	@Override
   //	public void afterCompose() {
   //		super.afterCompose();
   //		
   //		ndaLabel = (Label) getFirstChild().getFellow("ndaLabel");
   //		ndaBox = (Textbox) getFirstChild().getFellow("ndaBox");
   //		ndaBox.setConstraint(PrelevementConstraints.getNdaConstraint());
   //
   //	}

   //	@Override
   //	public void doAfterCompose(Component comp) throws Exception {
   //		super.doAfterCompose(comp);
   //		ndaBox.setConstraint(PrelevementConstraints.getNdaConstraint());
   //	}

   public ResumePatient(final Group resumePatientGroup){

      page = resumePatientGroup.getPage();

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
      linkMaladie = (Row) resumePatientGroup.getNextSibling().getNextSibling().getNextSibling().getNextSibling();

      // fifth Row
      row5 = (Row) resumePatientGroup.getNextSibling().getNextSibling().getNextSibling().getNextSibling().getNextSibling();
      linkMaladieLabel = (Label) row5.getFellowIfAny("linkMaladieLabel");
      codeDiagLabel = (Label) row5.getFellowIfAny("codeDiagLabel");
   }

   /**
    * Assigne les valeurs aux labels représentant les informations
    * de la maladie. Cette méthode est le point d'entrée des informations
    * dans la fiche résumé patient à partir de la fiche prélèvement.
    * @param maladie
    */
   public void setMaladie(final Maladie mal){
      this.maladie = mal;

      //		if (getFirstChild() != null) { // vérifie que le composant a été crée 
      //			// maladie property
      //			// setDynamicProperty("maladie", this.maladie);
      //			final Label libelleLabel = 
      //				(Label) getFirstChild().getFellow("linkMaladieLabel");
      //			final Label codeLabel = 
      //				(Label) getFirstChild().getFellow("codeDiagLabel");
      //			if (this.maladie != null) {
      //				libelleLabel.setValue(this.maladie.getLibelle()); 
      //				codeLabel.setValue(this.maladie.getCode()); 
      //				setPatientProperties(this.maladie.getPatient());
      //			} else {
      //				libelleLabel.setValue(null); 
      //				codeLabel.setValue(null);
      //				setPatientProperties(null);
      //			}				
      //		}

      if(this.maladie != null){
         linkMaladieLabel.setValue(this.maladie.getLibelle());
         if(this.maladie.getCode() != null){
            codeDiagLabel.setValue(this.maladie.getCode());
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
    * @param maladie
    */
   public void setPrelevement(final Prelevement prel){
      this.prelevement = prel;

      //		if (getFirstChild() != null) { // vérifie que le composant a été crée 
      //			// prelevement property
      //			// setDynamicProperty("prelevement", this.prelevement);
      //			if (this.prelevement != null) {
      //				if (!anonyme) {
      //					ndaLabel.setValue(this.prelevement.getPatientNda());
      //					ndaBox.setValue(this.prelevement.getPatientNda());
      //				} else {
      //					ndaLabel.setValue("                                 ");
      //					ndaLabel.setSclass("formAnonymeBlock");
      //					ndaLabel.setPre(true);
      //				}
      //			} else {
      //				ndaLabel.setValue(null);
      //			}
      //		}
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
      //		if (accessible) {
      //			if (!anonyme) {
      //				((Label) getFirstChild().getFellow("linkPatientLabel"))
      //											.setSclass("formLink");
      //			}
      //			((Label) getFirstChild().getFellow("linkMaladieLabel"))
      //											.setSclass("formLink");
      //			// Events listeners
      //			getFirstChild().getFellow("linkPatientLabel")
      //				.addEventListener("onClick", new EventListener<Event>() {
      //				public void onEvent(Event event) throws Exception {
      //					showPatientPanel(false);
      //				}
      //			});
      //			getFirstChild().getFellow("linkMaladieLabel")
      //				.addEventListener("onClick", new EventListener<Event>() {
      //					public void onEvent(Event event) throws Exception {
      //						showPatientPanel(true);
      //					}
      //			});
      //		} else {
      //			if (!anonyme) {
      //				((Label) getFirstChild()
      //						.getFellow("linkPatientLabel"))
      //						.setSclass("formValue");
      //			} else {
      //				AbstractController.makeLabelAnonyme(((Label) getFirstChild()
      //						.getFellow("linkPatientLabel")), false);
      //			}
      //			((Label) getFirstChild().getFellow("linkMaladieLabel"))
      //								.setSclass("formValue");
      //		}
      if(accessible){
         if(!anonyme){
            linkPatientLabel.setSclass("formLink");
         }
         linkMaladieLabel.setSclass("formLink");
         // Events listeners
         linkPatientLabel.addEventListener("onClick", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               showPatientPanel(false);
            }
         });
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
      //patient property
      //		final Label nipLabel = 
      //			(Label) getFirstChild().getFellow("nipLabel");
      //		final Label nomLabel = 
      //			(Label) getFirstChild().getFellow("linkPatientLabel");
      //		final Label prenomLabel = 
      //			(Label) getFirstChild().getFellow("prenomLabel");
      //		final Label dateNaisLabel = 
      //			(Label) getFirstChild().getFellow("dateNaisLabel");
      //		final Label sexeLabel = 
      //			(Label) getFirstChild().getFellow("sexeLabel");
      //		
      if(!anonyme){
         if(patient != null){
            // nip = patient.getNip();
            nipLabel.setValue(patient.getNip());
            linkPatientLabel.setValue(patient.getNom());
            prenomLabel.setValue(patient.getPrenom());
            dateNaisLabel.setValue(ObjectTypesFormatters.dateRenderer2(patient.getDateNaissance()));
            // MELBASE Hack
            //String dateN = null;
            //if (patient.getDateNaissance() != null) {
            //	Calendar c = Calendar.getInstance();
            //	c.setTime(patient.getDateNaissance());
            //	dateN = String.valueOf(c.get(Calendar.YEAR));
            //}
            //dateNaisLabel.setValue(dateN);
            sexeLabel.setValue(PatientUtils.setSexeFromDBValue(patient));
         }else{ //efface les propriétés du résumé
            nipLabel.setValue(null);
            // nip = null;
            linkPatientLabel.setValue(null);
            prenomLabel.setValue(null);
            dateNaisLabel.setValue(null);
            sexeLabel.setValue(null);
         }
      }else{
         AbstractController.makeLabelAnonyme(nipLabel, false);
         AbstractController.makeLabelAnonyme(prenomLabel, false);
         AbstractController.makeLabelAnonyme(dateNaisLabel, false);
         if(patient != null){
            sexeLabel.setValue(PatientUtils.setSexeFromDBValue(patient));
            AbstractController.makeLabelAnonyme(linkPatientLabel, true);
         }else{ //efface les propriétés du résumé
            sexeLabel.setValue(null);
            AbstractController.makeLabelAnonyme(linkPatientLabel, false);
         }
      }
   }

   /**
    * Affiche la fiche d'un patient.
    * @param boolean ouvre le panel de la maladie également.
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
         if(panel != null && tabController != null){
            tabController.switchToFicheStaticMode(this.maladie.getPatient());
            panels.setSelectedPanel(panel);
            if(opensMaladie){ //ouvre le panel maladie directement
               ((FichePatientStatic) tabController.getFicheStatic()).openMaladiePanel(this.maladie);
            }
         }
      }
   }

   /**
    * Affiche ou non les lignes correspondant à la maladie.
    * @param visible true/false
    */
   //	public void hideMaladieRows(boolean visible) {
   //		((Row) getFirstChild().getFellow("linkMaladie")).setVisible(visible);
   //		((Row) getFirstChild().getFellow("linkMaladie")
   //										.getNextSibling()).setVisible(visible);
   //	}

   public void hideMaladieRows(final boolean visible){
      linkMaladie.setVisible(visible);
      linkMaladie.getNextSibling().setVisible(visible);
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
      row1.setVisible(b);
      row2.setVisible(b);
      row3.setVisible(b);
      linkMaladie.setVisible(b);
      row5.setVisible(b);
   }
}
