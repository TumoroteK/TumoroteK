/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.action.prelevement.gatsby;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.webapp.gatsby.DisplayItem;
import fr.aphp.tumorotek.webapp.gatsby.client.json.ChampEntite;
import fr.aphp.tumorotek.webapp.gatsby.client.json.Contexte;

/**
 *
 * Controller gérant la fiche static d'un prélèvement.
 * CONTEXTE SEROTK
 * Controller créé le 19/01/2012.
 *
 * @author mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class FichePrelevementStaticGatsby extends FichePrelevementStatic
{

//   protected Groupbox gridFormPrlvtComp;
//   protected Groupbox groupPatient;
//   protected Div groupLaboInter;
//   protected Groupbox groupEchans;
//   protected Groupbox groupDerivesPrlvt;

   private static final long serialVersionUID = -7612780578022559022L;
   
   private Div gatsbyContainer;
   
//   // Identifiants prélèvement
//   private Div identifiantBlockDiv;
//   private Div codeDiv;
//   private Div codeLaboDiv;
//   private Div natureDiv;
//   
//   // /////
//   // group patient + maladie
//   // private Groupbox groupPatient; // géré dans classe parente 
//   
//   // Resume patient
//   private Div patientBlockDiv;
//   private Div nipDiv;
//   private Div ndaDiv;
//   private Div nomDiv;
//   private Div prenomDiv;
//   private Div dateNaisDiv;
//   private Div sexeDiv;
//   
//   // Maladie
//   // private Div linkMaladie; // géré dans classe parente
//   private Div libelleDiv;
//   private Div codeMaladieDiv;
//   
//   // //////
//   // group prelevement
//   // private Groupbox groupPrlvt; // géré dans classe parente
//   
//   // Informations prélèvement
//   private Div infoPrelBlockDiv;
//   private Div datePrelDiv;
//   private Div typeDiv;
//   private Div sterileDiv;
//   private Div risquesDiv;
//   private Div etabPreleveurDiv;
//   private Div servicePreleveurDiv;
//   private Div preleveurDiv;
//   
//   // Conditionnement
//   private Div conditBlockDiv;
//   private Div conditTypeDiv;
//   private Div conditNbrDiv;
//   private Div conditMilieuDiv;
//   
//   // Consentement
//   private Div consentBlockDiv;
//   private Div consentTypeDiv;
//   private Div consentDateDiv;
//   
//   // //////
//   // Transfert site preleveur vers site stockage
//   // private Groupbox gridFormPrlvtComp; // géré dans classe parente
//   
//   // Depart site preleveur
//   private Div departBlockDiv;
//   private Div dateDepartDiv;
//   private Div transporteurDiv;
//   private Div tempTranspDiv;
//   private Div congPrelDiv;
//   
//   // Sites d'analyse / Labos intermédiaires
//   // private Div groupLaboInter; // géré dans classe parente
//   
//   // Arrivee site stockage
//   private Div arriveeBlockDiv;
//   private Div dateArriveeDiv;
//   private Div operateurDiv;
//   private Div quantiteDiv;
//   private Div conformeArriveeDiv;
//   private Div congBiothequeDiv;
//   
//   // //////
//   // Echantillons
//   // private Groupbox groupEchans; // géré dans classe parente
//
//   // //////
//   // Dérives
//   // private Groupbox groupDerivesPrlvt; // géré dans classe parente
//   
   
   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      
      List<Div> divs = GatsbyController.wireDivsFromMainComponent(gatsbyContainer);
    
      GatsbyController.showOrhideItems(divs, GatsbyController.mockOneContexte()); // TODO replace by collection.contexte
   }
   
   
   @Override
   protected ResumePatient initResumePatient() {
	  return new ResumePatient(groupPatient, true);
   }


   @Override
   protected void enablePatientGroup(boolean b) {
	   ((Groupbox) this.groupPatient).setOpen(b);
	   ((Groupbox) this.groupPatient).setClosable(b);
   }
}
