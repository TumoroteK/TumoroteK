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
package fr.aphp.tumorotek.action.prelevement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.comparator.PatientNomComparator;
import fr.aphp.tumorotek.action.sip.Sip;
import fr.aphp.tumorotek.action.sip.SipFactory;
import fr.aphp.tumorotek.decorator.PatientItemRenderer;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.interfacage.PatientSip;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class SelectPatientModale
{

   private final Logger log = LoggerFactory.getLogger(SelectPatientModale.class);

   protected Boolean isFusionPatients = false;

   protected String critereValue = "";

   protected String path = "";

   protected String returnMethode = "";

   protected List<Patient> patients = new ArrayList<>();

   protected List<Patient> patientsSip = new ArrayList<>();

   protected Patient selectedPatient;

   protected Patient currentPatient;

   protected Listitem currentIten;

   private PatientItemRenderer patientRenderer = new PatientItemRenderer(false);

   @Wire
   protected Listbox patientsBox;

   protected Patient patientAExclure;

   @Wire
   protected Button select;

   @Wire
   protected Row legendeInTk;

   @Wire
   protected Row legendeInSip;

   @Wire("#fwinSelectPatientModale")
   protected Window fwinSelectPatientModale;

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);

      if(isFusionPatients){
         legendeInTk.setVisible(false);
         legendeInSip.setVisible(false);
         patientRenderer.setFusion(true);
      }

      // auto-select si 1 seul patient
      if(getPatients().size() == 1){
         setCurrentPatient(getPatients().get(0));
         select();
      }
   }

   @Init
   public void init(@ExecutionArgParam("path") final String pathToPage, @ExecutionArgParam("methode") final String methode,
      @ExecutionArgParam("isFusion") final Boolean isFusion, @ExecutionArgParam("critere") final String critere,
      @ExecutionArgParam("patAExclure") final Patient patAExclure){
      this.path = pathToPage;
      this.returnMethode = methode;
      this.isFusionPatients = isFusion;
      this.critereValue = critere;
      this.patientAExclure = patAExclure;

      searchForPatients();
   }

   /**
    * @since 2.0.9 Recherche sur le numéro de séjour
    * @since 2.3.0 recherche sur l'identifiant
    * @version 2.3.0
    */
   public void searchForPatients(){
      // recherche des patients dans la base TK
      final LinkedHashSet<Patient> res = searchPatientInTK();
      this.patients = new ArrayList<>(res);

      // si nous sommes dans le cas d'une recherche de patients pour une
      // fusion, on ne va pas les chercher dans le serveur d'identités
      // patient
      if(!isFusionPatients){
         //recherche dans le serveur identite patient si il est actif
         if(SipFactory.isDirectSip()){
            log.info("->Debut traitement recherche de patients " + "dans serveur d'identités");

            // plusieurs fichiers de connexion au Sip peuvent être
            // définis ils doivent etre de la forme pt1_serveur_Identites,
            // pt2_serveur_Identites... ou serveur_Identites
            boolean found = true;
            int idx = 1;
            // on itère sur les noms possibles tant qu'on trouve
            // un fichier correspondant
            while(found){
               final StringBuffer nomFichier = new StringBuffer();
               nomFichier.append("pt");
               nomFichier.append(idx);
               nomFichier.append("_serveur_Identites.properties");

               // on vérifie que le fichier existe
               found = ManagerLocator.getResourceBundleSip().doesResourceBundleExists(nomFichier.toString());

               // s'il existe
               if(found){
                  // recherche
                  searchPatientsInSip(nomFichier.toString());
               }else{
                  // sinon on utilise le fichier par défaut
                  // "serveur_Identites"
                  if(ManagerLocator.getResourceBundleSip().doesResourceBundleExists("serveur_Identites.properties")){
                     searchPatientsInSip("serveur_Identites.properties");
                  }
               }
               ++idx;
            }

         }else if(SipFactory.isMessagesSip()){
            log.debug("Recherche de patients par interfacage");

            final Set<PatientSip> listPatientSip = new HashSet<>();
            listPatientSip.addAll(ManagerLocator.getPatientSipManager().findByNipLikeManager(critereValue, true));
            listPatientSip.addAll(ManagerLocator.getPatientSipManager().findByNomLikeManager(critereValue, true));
            listPatientSip.addAll(ManagerLocator.getPatientSipManager().findByNumeroSejourManager(critereValue, true));

            final List<Patient> listPatientTmp = new ArrayList<>();
            final Iterator<PatientSip> it = listPatientSip.iterator();
            while(it.hasNext()){
               listPatientTmp.add(it.next().toPatient());
            }

            fusionerNouvelleListeAvecPrincipale(listPatientTmp);
            log.info("->Fin recherche de patients interfacage");
         }
         patients.addAll(patientsSip);

      }else{
         // si un patient doit être exclu, on le retire de la liste
         if(patientAExclure != null){
            int idx = -1;
            for(int i = 0; i < patients.size(); i++){
               if(patients.get(i).getPatientId() != null && patientAExclure.getPatientId() != null
                  && patients.get(i).getPatientId().equals(patientAExclure.getPatientId())){
                  idx = i;
               }
            }

            if(idx > -1){
               patients.remove(idx);
            }
         }
      }
      // on va trier les patients extraits en fct de leur nom
      Collections.sort(patients, new PatientNomComparator(true));
   }
   
   /**
    * Sera surchargé par Gatsbi
    * @since 2.3.0-gatsbi
    * @return liste de patient trouvé dans TK
    */
   protected LinkedHashSet<Patient> searchPatientInTK() {
      LinkedHashSet<Patient> res = new LinkedHashSet<>();
      if(critereValue != null && !critereValue.equals("")){
         res.addAll(ManagerLocator.getPatientManager().findByNipLikeManager(critereValue, false));
         res.addAll(ManagerLocator.getPatientManager().findByNomLikeManager(critereValue, false));
         res.addAll(ManagerLocator.getPatientManager().findByNdaLikeManager(critereValue, false));
      }
      return res;
   }

   public void searchPatientsInSip(final String filePropertiesName){
      log.info("->Debut traitement recherche de patients " + "dans serveur d'identités");
      final Sip sipActif = SipFactory.getSip(filePropertiesName);
      // recherche par nip
      List<Patient> listPatientTmp = new ArrayList<>();
      listPatientTmp = sipActif.getPatientsServeurNip(critereValue);
      // ajouts a la liste des patients, en supprimant les
      // doublons de la liste de resultats
      // provenant du serveur d'identite patient.
      fusionerNouvelleListeAvecPrincipale(listPatientTmp);

      // recherche par nom
      listPatientTmp = new ArrayList<>();
      listPatientTmp = sipActif.getPatientsServeurNom(critereValue);
      // ajouts a la liste des patients, en supprimant les
      // doublons de la liste de resultats
      // provenant du serveur d'identite patient.
      fusionerNouvelleListeAvecPrincipale(listPatientTmp);

      // recherche par nda
      listPatientTmp = new ArrayList<>();
      listPatientTmp = sipActif.getPatientsServeurNumDossier(critereValue);
      // ajouts a la liste des patients, en supprimant les
      // doublons de la liste de resultats
      // provenant du serveur d'identite patient.
      fusionerNouvelleListeAvecPrincipale(listPatientTmp);
      log.info("->Fin recherche de patients " + "dans serveur d'identités");
   }

   public void fusionerNouvelleListeAvecPrincipale(final List<Patient> newListe){
      for(int i = 0; i < newListe.size(); i++){
         if(!patients.contains(newListe.get(i))){
            patientsSip.add(newListe.get(i));
         }
      }
   }

   @Command
   public void onSelect$patientsBox(final Event event){
      deselectRow();

      selectRow(patientsBox.getSelectedItem(), selectedPatient);

      if(getCurrentPatient() != null){
         select.setDisabled(false);
      }else{
         select.setDisabled(true);
      }
   }

   @Command
   public void onDoubleClicked(){
      if(currentPatient != null){
         select();
      }
   }

   @Command
   public void select(){
      // dans le cas d'une fusion, on retourne le patient
      if(isFusionPatients){
         // si le chemin d'accès à la page est correcte
         if(Path.getComponent(path) != null){
            if(getCurrentPatient() != null){
               // on envoie un event à cette page avec
               // le patient sélectionné
               Events.postEvent(new Event(returnMethode, Path.getComponent(path), getCurrentPatient()));
            }
         }
      }else{
         // si le patient sélectionné est déjà dans TK
         //			if (isDoublonPatient()) {
         //				// on récupère le patient de TK
         //				List<Patient> liste = ManagerLocator.getPatientManager()
         //						.findByNomLikeManager(getCurrentPatient().getNom(),
         //								true);
         //
         //				Patient pat = null;
         //				for (int i = 0; i < liste.size(); i++) {
         //					Patient p = liste.get(i);
         //					if (getCurrentPatient().equals(p)) {
         //						pat = p;
         //					}
         //				}

         // openDoublonPatientWindow(page, pat);
         //			} else {
         // si le chemin d'accès à la page est correcte
         if(Path.getComponent(path) != null){
            if(getCurrentPatient() != null){
               // on envoie un event à cette page avec
               // le patient sélectionné
               Events.postEvent(new Event(returnMethode, Path.getComponent(path), getCurrentPatient()));
            }
         }
      }
      //		}
      // fermeture de la fenêtre
      cancel();
   }

   @Command
   public void cancel(){
      Events.postEvent("onClose", fwinSelectPatientModale, null);
   }

   /**
    * Déselectionne la ligne actuellement sélectionnée.
    */
   public void deselectRow(){
      // on vérifie qu'une ligne est bien sélectionnée
      if(getCurrentPatient() != null && getCurrentIten() != null){
         final int ind = patients.indexOf(getCurrentPatient());
         // on lui spécifie une couleur en fonction de son
         // numéro de ligne
         if(ind > -1){
            if(getCurrentPatient().getPatientId() == null){
               getCurrentIten().setStyle("background-color : #e2e9fe");
            }else{
               getCurrentIten().setStyle("background-color : #beff96");
            }

            setCurrentPatient(null);
            setCurrentIten(null);
         }
      }
   }

   /**
    * Sélectionne la ligne passée en paramètre.
    * @param row Row à sélectionner.
    * @param obj Objet se trouvant dans la ligne
    */
   public void selectRow(final Listitem item, final Patient pat){
      setCurrentIten(item);
      setCurrentPatient(pat);

      getCurrentIten().setStyle("background-color : #b3c8e8");
   }

   /**
    * Cette méthode va retourner la ligne courante.
    * @param event Event sur la grille contenant la liste des objets.
    * @return Une Row.
    */
   public static Listitem getItem(final ForwardEvent event){
      Component target = event.getOrigin().getTarget();
      try{
         while(!(target instanceof Listitem)){
            target = target.getParent();
         }
         //			Map map = (Map) target
         //				.getAttribute("zkplus.databind.TEMPLATEMAP");
         return (Listitem) target;
      }catch(final NullPointerException e){
         return null;
      }
   }

//   public boolean isDoublonPatient(){
//      if(getCurrentPatient().getPatientId() == null){
//         return ManagerLocator.getPatientManager().findDoublonManager(getCurrentPatient()).isPresent();
//      }else{
//         return false;
//      }
//   }


   /**
    * Retourne la taille de la colonne contenant le Nom.
    * @return
    */
   public String getNomHeaderWidth(){
      if(isFusionPatients){
         return "120px";
      }else{
         return "150px";
      }
   }

   /**
    * Retourne la taille de la colonne contenant le Prénom.
    * @return
    */
   public String getPrenomHeaderWidth(){
      if(isFusionPatients){
         return "120px";
      }else{
         return "150px";
      }
   }

   /**********************************************************/
   /****************** GETTERS - SETTERS *********************/
   /**********************************************************/

   public String getCritereValue(){
      return critereValue;
   }

   public void setCritereValue(final String c){
      this.critereValue = c;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   public List<Patient> getPatients(){
      return patients;
   }

   public void setPatients(final List<Patient> p){
      this.patients = p;
   }

   public Patient getSelectedPatient(){
      return selectedPatient;
   }

   public void setSelectedPatient(final Patient sPatient){
      this.selectedPatient = sPatient;
   }

   public ListitemRenderer<Patient> getPatientRenderer(){
      return patientRenderer;
   }

   public Patient getCurrentPatient(){
      return currentPatient;
   }

   public void setCurrentPatient(final Patient p){
      this.currentPatient = p;
   }

   public Listitem getCurrentIten(){
      return currentIten;
   }

   public void setCurrentIten(final Listitem i){
      this.currentIten = i;
   }

   //	@Override
   //	public List<? extends Object> getChildrenObjects() {
   //		return null;
   //	}
   //
   //	@Override
   //	public Object getParentObject() {
   //		return null;
   //	}
   //
   //	@Override
   //	public void setParentObject(Object obj) {
   //	}
   //
   //	@Override
   //	public String getDeleteWaitLabel() {
   //		return null;
   //	}
   //
   //	@Override
   //	public boolean prepareDeleteObject() {
   //		return false;
   //	}
   //
   //	@Override
   //	public void removeObject(String comments) {
   //	}

   public Boolean isFusionPatients(){
      return isFusionPatients;
   }

   public Boolean getFusionPatients(){
      return isFusionPatients;
   }

   public void setFusionPatients(final Boolean i){
      this.isFusionPatients = i;
   }

   public String getReturnMethode(){
      return returnMethode;
   }

   public void setReturnMethode(final String r){
      this.returnMethode = r;
   }

   public Patient getPatientAExclure(){
      return patientAExclure;
   }

   public void setPatientAExclure(final Patient p){
      this.patientAExclure = p;
   }

   public List<Patient> getPatientsSip(){
      return patientsSip;
   }

   public void setPatientsSip(final List<Patient> p){
      this.patientsSip = p;
   }
}