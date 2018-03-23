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
import org.zkoss.zul.*;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.patient.FicheMaladie;
import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.patient.bto.FichePatientEditBTO;
import fr.aphp.tumorotek.action.prelevement.bto.FichePrelevementEditBTO;
import fr.aphp.tumorotek.action.sip.SipFactory;
import fr.aphp.tumorotek.decorator.MaladieDecorator;
import fr.aphp.tumorotek.decorator.PatientItemRenderer;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.PatientSipSejour;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

import static fr.aphp.tumorotek.model.contexte.EContexte.BTO;
import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentContexte;

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
 * @version 2.0.6
 * @see <a href="http://docs.zkoss.org/wiki/Macro_Component">Macro_Component</a>
 * <p>
 * Date: 01/12/2009
 */
public class ReferenceurPatient extends GenericForwardComposer<Component>
{

   // private Log log = LogFactory.getLog(ReferenceurPatient.class);

   private static final long serialVersionUID = 1L;

   // composants
   private Radiogroup radioGroup;
   private Grid existingPatientGrid;
   private Textbox nomNipNdaBox;
   private Listbox patientsBox;
   private Row noPatientRow;
   private Listbox maladiesBox;
   private Row noMaladieRow;
   private Button embedMaladieButton;
   private Row embeddedFicheMaladieRow;
   private Div embeddedFicheMaladieDiv;
   private Radio newRadio;
   private Radio findRadio;
   private Radio noRadio;
   private Div fichePatientDiv;
   private Div ficheMaladieWithPatientDiv;
   private Textbox ndaBox;
   private Row ndaRow;
   private Listheader listMedcinRef; //TODO Jamais utilisé ?
   private boolean banqueDefMaladies;

   // patientBox items
   private List<Patient> patients = new ArrayList<>();
   private Patient selectedPatient;

   // maladiesBox items
   private List<MaladieDecorator> maladies = new ArrayList<>();
   private MaladieDecorator selectedMaladie;
   private MaladieDecorator selectedMaladieByNda = null;
   private static ListitemRenderer<Patient> patientRenderer = new PatientItemRenderer(true);
   private AnnotateDataBinder referenceurBinder;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      /*if (BTO.equals(getCurrentContexte())) {
         // TODO : je ne pense pas que cette ligne soit necessaire - JDI
         self.getFellow("newPatientDiv").getFellow("newMaladieBox").setVisible(false);
         listMedcinRef.setVisible(false);

      }*/

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

   //	/**
   //	 * Peuple le combobox auto de recherche en fonction du critère de
   //	 * recherche choisi (nip, nom, nda).
   //	 */
   //	public void onSelect$patientAttrBox() {
   //		List<String> attrValuesList;
   //		this.attrSelected = (String) this.patientAttrBox
   //									.getSelectedItem().getValue();
   //
   //		if (this.attrSelected.equals("nip")) {
   //			attrValuesList = ManagerLocator.getPatientManager()
   //											.findAllNipsManager();
   //		} else if (this.attrSelected.equals("nda")) {
   //			attrValuesList = ManagerLocator
   //				.getPrelevementManager()
   //					.findAllNdasForBanqueManager(SessionUtils
   //							.getSelectedBanques(sessionScope).get(0));
   //		} else {
   //			attrValuesList = ManagerLocator.getPatientManager()
   //											.findAllNomsManager();
   //		}
   //		this.autoAttrBox.setModel(new SimpleListModel(attrValuesList));
   //	}

   //	/**
   //	 * Lance la recherche dans la base pour afficher la liste de patients
   //	 * correspondant à la valeur spécifiée.
   //	 */
   //	public void onClick$goForIt2() {
   //
   //		// efface tout ce qui concerne les maladies
   //		this.maladiesBox.setVisible(false);
   //		this.noMaladieRow.setVisible(false);
   //		this.embedMaladieButton.setVisible(false);
   //		this.embeddedFicheMaladieRow.setVisible(false);
   //		Components.removeAllChildren(embeddedFicheMaladieDiv);
   //
   //		LinkedHashSet<Patient> res = new LinkedHashSet<Patient>();
   //
   //		selectedMaladieByNda = null;
   //
   //		String critereValue = nomNipNdaBox.getValue();
   //		if (critereValue != null && !critereValue.equals("")) {
   //			res.addAll(ManagerLocator.getPatientManager()
   //				.findByNipLikeManager(critereValue, false));
   //			res.addAll(ManagerLocator.getPatientManager()
   //				.findByNomLikeManager(critereValue, false));
   //			boolean countZeroBeforeNda = res.size() == 0;
   //			res.addAll(ManagerLocator.getPatientManager()
   //				.findByNdaLikeManager(critereValue, false));
   //			// si le nda a ramebé un seul patient
   //			if (countZeroBeforeNda && res.size() == 1) {
   //				selectedMaladieByNda =
   //					new MaladieDecorator(ManagerLocator
   //							.getPrelevementManager()
   //								.findByNdaLikeManager("%" + critereValue + "%")
   //									.get(0).getMaladie());
   //			}
   //		}
   //
   ////		String critereValue = this.autoAttrBox.getValue();
   //		// verifie que le contenu n'est pas vide car requete non exact match!
   ////		if (critereValue != null && !critereValue.equals("")) {
   ////			if (this.attrSelected.equals("nip")) {
   ////				res = ManagerLocator.getPatientManager()
   ////								.findByNipLikeManager(critereValue, false);
   ////			} else if (this.attrSelected.equals("nom")) {
   ////				res = ManagerLocator.getPatientManager()
   ////								.findByNomLikeManager(critereValue, false);
   ////			} else {
   ////				res = ManagerLocator.getPatientManager()
   ////								.findByNdaLikeManager(critereValue, false);
   ////			}
   ////		}
   //		this.patients = new ArrayList<Patient>(res);
   //
   //		// select auto le premier patient
   //		if (this.patients.size() == 1) {
   //			this.selectedPatient = this.patients.get(0);
   //			selectPatientAuto(this.selectedPatient);
   //		} else {
   //			this.selectedPatient = null;
   //		}
   //
   //		//((AnnotateDataBinder) page.getAttributeOrFellow("binder"))
   //			//							.loadAttribute(patientsBox, "model");
   //		referenceurBinder.loadAttribute(patientsBox, "model");
   //
   //		// affiche la liste de resultats ou no match
   //		if (this.patients.size() > 0) {
   //			if (this.patients.size() < 5) {
   //				this.patientsBox.setPageSize(this.patients.size());
   //			} else {
   //				this.patientsBox.setPageSize(5);
   //			}
   //			this.patientsBox.setVisible(true);
   //			this.noPatientRow.setVisible(false);
   //		} else {
   //			this.patientsBox.setVisible(false);
   //			this.noPatientRow.setVisible(true);
   //		}
   //	}

   public void onClick$goForIt(){
      final String critereValue = nomNipNdaBox.getValue();

      if(BTO.equals(getCurrentContexte())){
         ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
            .getObjectTabController().setPatientSip(null);
         ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
            .openSelectPatientWindow(Path.getPath(self), "onGetPatientFromSelection", false, critereValue, null);
      }else{
         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
            .getObjectTabController().setPatientSip(null);
         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
            .openSelectPatientWindow(Path.getPath(self), "onGetPatientFromSelection", false, critereValue, null);
      }
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
            this.ndaRow.setVisible(true);
         }else{
            radioGroup.setSelectedItem(newRadio);
            displayEmbeddedPatient(true, patSel);

            if(BTO.equals(getCurrentContexte())){
               ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer",
                  true)).clearRisques();
               ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer",
                  true)).clearProtocoles();
            }else{
               ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
                  .clearRisques();
               ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
                  .clearProtocoles();
            }

            if(SipFactory.isMessagesSip()){
               final PatientSip pSip = ManagerLocator.getPatientSipManager().findByNipLikeManager(patSel.getNip(), true).get(0);
               if(BTO.equals(getCurrentContexte())){
                  ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer",
                     true)).getObjectTabController().setPatientSip(pSip);
               }else{
                  ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
                     .getObjectTabController().setPatientSip(pSip);
               }

               // copie me numero de sejour dans nda
               // TODO : A quoi ça sert? - JDI
               /*if (BTO.equals(getCurrentContexte())) {
                  for (PatientSipSejour sj : pSip.getSejours()) {
                     if (sj.getNumero().equalsIgnoreCase(nomNipNdaBox.getValue())) {
                        ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEditBTO")
                           .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).getNdaBox().setValue(sj.getNumero());
                     }
                  }
               } else {*/
               for(final PatientSipSejour sj : pSip.getSejours()){
                  if(sj.getNumero().equalsIgnoreCase(nomNipNdaBox.getValue())){
                     ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit")
                        .getAttributeOrFellow("fwinPatientEdit$composer", true)).getNdaBox().setValue(sj.getNumero());
                  }
               }
               //}
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
         this.ndaRow.setVisible(true);

         // nda DIAMIC-TK 2.0.13 fix
         ndaBox.setValue(nda);
      }else{
         // sinon
         radioGroup.setSelectedItem(newRadio);
         displayEmbeddedPatient(true, pat);

         // nda DIAMIC-TK 2.0.13 fix
         // TODO : A quoi ça sert? - JDI
         /*if (BTO.equals(getCurrentContexte())) {
            ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEditBTO")
               .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).getNdaBox().setValue(nda);
         } else {*/
         ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit").getAttributeOrFellow("fwinPatientEdit$composer", true))
            .getNdaBox().setValue(nda);
         //}

         ((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
            .getObject().setLibelle(maladie.getLibelle());

         ((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
            .getObject().setCode(maladie.getCode());

         ((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
            .getObject().setDateDebut(maladie.getDateDebut());

         ((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
            .getObject().setDateDiagnostic(maladie.getDateDiagnostic());

         ((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
            .getBinder().loadAll();

         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
            .clearRisques();

         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
            .clearProtocoles();
      }
   }

   /**
    * Affiche la liste de maladies du patient selectionné si la banque définit
    * les maladies. Affiche le lien d'ajout d'une nouvelle maladie qui sera
    * enregistrée conjointement à la création du prélèvement.
    * Selectionne ou cree la maladie sous-jacente sinon.
    */
   public void onSelect$patientsBox(){
      //Patient selected = (Patient)
      //						this.patientsBox.getSelectedItem().getValue();
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

      if(this.banqueDefMaladies){
         final List<Maladie> res = new ArrayList<>(ManagerLocator.getMaladieManager().findByPatientNoSystemManager(selected));

         this.maladies = MaladieDecorator.decorateListe(res);
         //selectionne automatiquement la premiere maladie
         if(maladies.size() > 0){
            if(selectedMaladieByNda == null){
               this.selectedMaladie = this.maladies.get(0);
               // TODO : A quoi ça sert? - JDI
               /*if (BTO.equals(getCurrentContexte())) {
                  ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer",
                     true)).setMaladie(res.get(0));
               } else {*/
               ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
                  .setMaladie(res.get(0));
               //}

            }else{
               this.selectedMaladie = this.maladies.get(maladies.indexOf(selectedMaladieByNda));
               ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
                  .setMaladie(selectedMaladieByNda.getMaladie());
            }
         }
         referenceurBinder.loadAttribute(maladiesBox, "model");

         Components.removeAllChildren(embeddedFicheMaladieDiv);
         setEmbeddedMaladieVisible(false);
      }else{ // selectionne la maladie sous-jacente ou la cree
         Maladie maladie = null;

         final List<Maladie> res = new ArrayList<>(ManagerLocator.getMaladieManager().findByPatientManager(selected));

         // 2.0.13 fix ajout maladie defaut system
         // si existante
         if(!res.isEmpty()){
            for(final Maladie mal : res){
               if(mal.getSystemeDefaut()){
                  maladie = mal;
                  break;
               }
            }
         }
         // cree une maladie defaut system si inexistante
         if(maladie == null){
            maladie = new Maladie();
            maladie.setPatient(selected);
            maladie.setLibelle(SessionUtils.getSelectedBanques(sessionScope).get(0).getNom() + "-defaut");
            maladie.setSystemeDefaut(true);
         }
         // TODO : Ne peut-on pas overider la classe Java pour les particularités de BTO? - JDI
         if(BTO.equals(getCurrentContexte())){
            ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
               .setMaladie(maladie);
         }else{
            ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
               .setMaladie(maladie);
         }
      }

      setRisquesIfAny(selected);
   }

   /**
    * Si le patient est deja enregistré dans TK, récupère les risques associés
    * aux prélèvements enregistrés dans les collections de la plateforme pour
    * les sélectionner par défaut dans le liste.
    */
   private void setRisquesIfAny(final Patient pat){
      // TODO : A quoi ça sert? - JDI
      if(BTO.equals(getCurrentContexte())){
         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
            .selectRisques(ManagerLocator.getRisqueManager().findByPatientAndPlateformeManager(pat,
               SessionUtils.getPlateforme(sessionScope)));
      }else{
         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
            .selectRisques(ManagerLocator.getRisqueManager().findByPatientAndPlateformeManager(pat,
               SessionUtils.getPlateforme(sessionScope)));
      }
   }

   /**
    * Assigne la référence vers la maladie à la fiche prélèvement.
    */
   public void onSelect$maladiesBox(){
      final Maladie selected = ((MaladieDecorator) this.maladiesBox.getSelectedItem().getValue()).getMaladie();
      ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
         .setMaladie(selected);
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
      //		Executions
      //			.createComponents("/zuls/patient/FicheMaladie.zul",
      //												embeddedFicheMaladieDiv, null);
      createMaladieComponent(embeddedFicheMaladieDiv);
      ((FicheMaladie) embeddedFicheMaladieDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
         .setEmbedded(true);
      // informe la fiche prelevement de la presence du formulaire
      // TODO : A quoi ça sert? - JDI
      /*if (BTO.equals(getCurrentContexte())) {
         ((FichePrelevementEditBTO) self.getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
            .setMaladieEmbedded(true);
      } else {*/
      ((FichePrelevementEdit) self.getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
         .setMaladieEmbedded(true);
      //}
      // attribution du patient a la maladie embedded
      (((FicheMaladie) embeddedFicheMaladieDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
         .getObject()).setPatient(selectedPatient);

      setEmbeddedMaladieVisible(true);
   }

   /**
    * Event controller fermeture fiche embarquée maladie.
    */
   public void onCloseMaladieClick(){
      Components.removeAllChildren(embeddedFicheMaladieDiv);
      setEmbeddedMaladieVisible(false);
      // informe la fiche prelevement de l'absence
      ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
         .setMaladieEmbedded(false);
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

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    * Passe qq params au bloc inline annotation sans le dessiner la creation de la
    * fiche statique.
    * @param controller TODO Jamais utilisé ?
    */
   public void setObjectTabController(final AbstractObjectTabController controller){
      //
      final FicheAnnotation inline = getFicheAnnotationInline();
      if(inline != null){
         // passe l'entite au controller
         getFicheAnnotationInline().setEntite(getObjectTabController().getEntiteTab());

         // à remplacer par ce controller
         // setFicheController
         getFicheAnnotationInline().setObjectTabController(getObjectTabController());
      }
   }

   public PatientController getObjectTabController(){
      return getObjectTabController();
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    * Il pourrait y avoir optimisation ICI car le bloc inline est redessiné à chaque fois
    * qu'un nouvel objet est affiché.
    * A priori, il ne serait utile de re-dessiner que si la collection change!
    * Cette optimisation est valable pour FicheAnnotation également.
    */
   public void setObject(final TKdataObject p){

      final FicheAnnotation inline = getFicheAnnotationInline();
      if(inline != null){ // re-dessine le bloc inline annotation
         inline.setObj((TKAnnotableObject) p);
      }
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @since 2.2.0
    * Copie depuis AbstractObjectTabController
    * Récupère le controller de la fiche
    */
   public FicheAnnotationInline getFicheAnnotationInline(){
      if(null != fichePatientDiv.getFellowIfAny("fwinPatientEdit")
         && null != fichePatientDiv.getFellow("fwinPatientEdit").getFellowIfAny("ficheTissuInlineAnnoPatient")){
         return ((FicheAnnotationInline) fichePatientDiv.getFellow("fwinPatientEdit").getFellow("ficheTissuInlineAnnoPatient")
            .getFellow("fwinAnnotationInline").getAttributeOrFellow("fwinAnnotationInline$composer", true));
      }else if(null != fichePatientDiv.getFellowIfAny("fwinPatientEditBTO")
         && null != fichePatientDiv.getFellow("fwinPatientEditBTO").getFellowIfAny("ficheTissuInlineAnnoPatient")){
         return ((FicheAnnotationInline) fichePatientDiv.getFellow("fwinPatientEditBTO").getFellow("ficheTissuInlineAnnoPatient")
            .getFellow("fwinAnnotationInline").getAttributeOrFellow("fwinAnnotationInline$composer", true));
      }else{
         return null;
      }
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
         if(BTO.equals(getCurrentContexte())){
            Executions.createComponents("/zuls/patient/bto/FichePatientEditBTO.zul", fichePatientDiv, null);
            ((FichePatientEditBTO) fichePatientDiv.getFellow("fwinPatientEditBTO")
               .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).setEmbedded(pat);
         }else{
            Executions.createComponents("/zuls/patient/FichePatientEdit.zul", fichePatientDiv, null);
            ((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit").getAttributeOrFellow("fwinPatientEdit$composer",
               true)).setEmbedded(pat);
         }

         Components.removeAllChildren(embeddedFicheMaladieDiv);
         createMaladieComponent(ficheMaladieWithPatientDiv);

         setObject(pat);

         //			Executions
         //				.createComponents("/zuls/patient/FicheMaladie.zul",
         //									ficheMaladieWithPatientDiv, null);
         ((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer", true))
            .setEmbedded(true);

         if(SessionUtils.getSelectedBanques(sessionScope).get(0).getDefMaladies()){
            // affiche le bloc div
            // TODO : A quoi ça sert? - JDI
            //if (!BTO.equals(getCurrentContexte()))
            self.getFellow("newPatientDiv").getFellow("newMaladieBox").setVisible(true);
         }else{
            // efface le bloc div
            self.getFellow("newPatientDiv").getFellow("newMaladieBox").setVisible(false);
            // cree la maladie sous-jacente
            ((Textbox) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getFellow("libelleBox"))
               .setValue((SessionUtils.getSelectedBanques(sessionScope).get(0).getNom() + "-defaut"));
            (((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer",
               true)).getObject()).setSystemeDefaut(true);

         }

         // attribution du patient a la maladie embedded
         if(BTO.equals(getCurrentContexte())){
            (((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer",
               true)).getObject())
                  .setPatient(((FichePatientEditBTO) fichePatientDiv.getFellow("fwinPatientEditBTO")
                     .getAttributeOrFellow("fwinPatientEditBTO$composer", true)).getObject());
         }else{
            (((FicheMaladie) ficheMaladieWithPatientDiv.getFellow("fwinMaladie").getAttributeOrFellow("fwinMaladie$composer",
               true)).getObject())
                  .setPatient(((FichePatientEdit) fichePatientDiv.getFellow("fwinPatientEdit")
                     .getAttributeOrFellow("fwinPatientEdit$composer", true)).getObject());
         }

         // efface la grid existing patient
         displayExistingPatient(false);
      }else{
         // detache les composants
         Components.removeAllChildren(fichePatientDiv);
         Components.removeAllChildren(ficheMaladieWithPatientDiv);
      }

      if(BTO.equals(getCurrentContexte())){
         ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
            .setPatientEmbedded(show);
      }else{
         // informe la fiche prelevement de la presence du formulaire
         ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
            .setPatientEmbedded(show);
      }
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
         ndaRow.setVisible(true);
      }else{
         ndaRow.setVisible(false);
      }
      ndaBox.setValue(null);
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

         if(BTO.equals(getCurrentContexte())){
            ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
               .setMaladie(null);
         }else{
            ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
               .setMaladie(null);
         }
      }else{
         // informe la fiche prelevement de l'absence de maladie embarquee
         /*((FichePrelevement) self.getParent()
         						.getParent()
         					.getAttributeOrFellow("fwinPrelevement$composer", true))
         					.setMaladieEmbedded(false);*/

         if(BTO.equals(getCurrentContexte())){
            ((FichePrelevementEditBTO) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEditBTO$composer", true))
               .setMaladieEmbedded(false);
         }else{
            ((FichePrelevementEdit) self.getParent().getParent().getAttributeOrFellow("fwinPrelevementEdit$composer", true))
               .setMaladieEmbedded(false);
         }
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
   private void createMaladieComponent(final Div div){
      final String zulPath = "/zuls/patient/FicheMaladie.zul";
      //		List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
      //		if (banks.size() == 1) {
      //			if (banks.get(0).getContexte().getNom().equals("CONT1")) {
      //				zulPath = "/zuls/patient/serotk/FicheMaladieSero.zul";
      //			}
      //		}
      if(div.getChildren().isEmpty()){
         Executions.createComponents(zulPath, div, null);
      }
   }
}
