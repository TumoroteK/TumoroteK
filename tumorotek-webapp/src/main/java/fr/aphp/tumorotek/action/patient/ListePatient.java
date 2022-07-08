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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Column;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.comparator.PatientsNbPrelevementsComparator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListePatient extends AbstractListeController2
{

   // private Log log = LogFactory.getLog(ListePatient.class);

   private static final long serialVersionUID = -6167747099087709700L;

   private List<Patient> listObjects = new ArrayList<>();

   private List<Patient> selectedObjects = new ArrayList<>();

   // Critères de recherche.
   private Radio nomPatient;

   private Radio nipPatient;

   private Textbox nomBoxPatient;

   private Textbox nipBoxPatient;

   // Variables formulaire pour les critères.
   private String searchNomPatient;

   private String searchNipPatient;

   private Column maladiesCol;

   private Column nbPrelevementsColumn;

   private final PatientRowRenderer listObjectsRenderer = new PatientRowRenderer(true);

   private PatientsNbPrelevementsComparator comparatorAsc = new PatientsNbPrelevementsComparator(true);

   private PatientsNbPrelevementsComparator comparatorDesc = new PatientsNbPrelevementsComparator(false);

   /** Getters-setters. **/

   public String getSearchNomPatient(){
      return searchNomPatient;
   }

   public void setSearchNomPatient(final String search){
      this.searchNomPatient = search;
   }

   public String getSearchNipPatient(){
      return searchNipPatient;
   }

   public void setSearchNipPatient(final String search){
      this.searchNipPatient = search;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      this.listObjectsRenderer.setBanques(PatientUtils.getBanquesConsultForPrelevement(sessionScope));

      // retire le colonne maladies si aucune banque ne définit
      // de maladies
      if(!SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope))){
         maladiesCol.setVisible(false);
      }

      comparatorAsc.setBanques(PatientUtils.getBanquesConsultForPrelevement(sessionScope));
      comparatorDesc.setBanques(PatientUtils.getBanquesConsultForPrelevement(sessionScope));

      setOnGetEventName("onGetPatientsFromSelection");
   }

   @Override
   public List<Patient> getListObjects(){
      return this.listObjects;
   }

   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      clearSelection();
      this.listObjects.clear();
      this.listObjects.addAll((List<Patient>) objs);
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(obj != null){
         if(pos != null){
            getListObjects().add(pos.intValue(), (Patient) obj);
         }else{
            getListObjects().add((Patient) obj);
         }
      }
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){
      this.selectedObjects = (List<Patient>) objs;
   }

   @Override
   public List<Patient> getSelectedObjects(){
      return this.selectedObjects;
   }

   @Override
   public void addToSelectedObjects(final TKdataObject obj){
      if(!getSelectedObjects().contains(obj)){
         getSelectedObjects().add((Patient) obj);
      }
   }

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){
      if(getSelectedObjects().contains(obj)){
         getSelectedObjects().remove(obj);
      }
   }

   @Override
   public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
      return listObjectsRenderer;
   }

   @Override
   public PatientController getObjectTabController(){
      return (PatientController) super.getObjectTabController();
   }

   @Override
   public void passSelectedToList(){
      getListObjects().clear();
      getListObjects().addAll(getSelectedObjects());
   }

   @Override
   public void passListToSelected(){
      getSelectedObjects().clear();
      getSelectedObjects().addAll(getListObjects());
   }

   @Override
   public void initObjectsBox(){

      final List<Patient> patients = ManagerLocator.getPatientManager()
         .findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope), getNbLastObjs());

      this.listObjects = patients;
      setCurrentRow(null);
      setCurrentObject(null);

      nbPrelevementsColumn.setSortAscending(comparatorAsc);
      nbPrelevementsColumn.setSortDescending(comparatorDesc);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");

   }

   /**
    * Méthode appelée pour ouvrir la page de recherche avancée.
    */
   public void onClick$findMore(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("recherche.avancee.patients"));
      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);

      openRechercheAvanceeWindow(page, sb.toString(), entite, Path.getPath(self), isAnonyme(), this);
   }

   public void onClick$searchHistory(){
      onClick$findMore();
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * nomBoxPatient. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$nomBoxPatient(){
      nomPatient.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * nipBoxPatient. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$nipBoxPatient(){
      nipPatient.setChecked(true);
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBoxPatient. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBoxPatient(){
      nomBoxPatient.setValue(nomBoxPatient.getValue().toUpperCase());
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return ManagerLocator.getPatientManager().findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope),
         getNbLastObjs());
   }

   @Override
   public List<Integer> doFindObjects(){
      List<Integer> patients = new ArrayList<>();

      if(dateCreation.isChecked()){
         patients = ManagerLocator.getPatientManager().findAfterDateCreationReturnIdsManager(getSearchDateCreation(),
            SessionUtils.getSelectedBanques(sessionScope));
      }else if(nomPatient.isChecked()){
         if(searchNomPatient == null){
            searchNomPatient = "";
         }
         searchNomPatient = searchNomPatient.toUpperCase();
         nomBoxPatient.setValue(searchNomPatient);
         if(!searchNomPatient.equals("")){
            if(searchNomPatient.contains(",")){
               final List<String> pats = ObjectTypesFormatters.formateStringToList(searchNomPatient);
               patients =
                  ManagerLocator.getPatientManager().findByNomInListManager(pats, SessionUtils.getSelectedBanques(sessionScope));
            }else{
               patients = ManagerLocator.getPatientManager().findByNomLikeBothSideReturnIdsManager(searchNomPatient,
                  SessionUtils.getSelectedBanques(sessionScope), true);
            }
         }else{
            if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {"Patient"}),
               Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               patients = ManagerLocator.getPatientManager()
                  .findAllObjectsIdsWithBanquesManager(SessionUtils.getSelectedBanques(sessionScope));
            }
         }

      }else if(nipPatient.isChecked()){
         if(!searchNipPatient.equals("")){
            if(searchNipPatient.contains(",")){
               final List<String> pats = ObjectTypesFormatters.formateStringToList(searchNipPatient);
               patients =
                  ManagerLocator.getPatientManager().findByNipInListManager(pats, SessionUtils.getSelectedBanques(sessionScope));
            }else{
               patients = ManagerLocator.getPatientManager().findByNipLikeBothSideReturnIdsManager(searchNipPatient,
                  SessionUtils.getSelectedBanques(sessionScope), true);
            }
         }else{
            if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {"Patient"}),
               Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               patients = ManagerLocator.getPatientManager()
                  .findAllObjectsIdsWithBanquesManager(SessionUtils.getSelectedBanques(sessionScope));
            }
         }
      }
      return patients;
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      if(ids != null && ids.size() > 0){
         return ManagerLocator.getPatientManager().findByIdsInListManager(ids);
      }else{
         return new ArrayList<Patient>();
      }
   }

   /**
    * Lance la recherche des patients en fournissant un fichier
    * Excel contenant une liste de noms.
    */
   public void onClick$findByListPatientNoms(){
      // récupère les noms des patients présents dans le
      // fichier excel que l'utilisateur va uploader
      final List<String> noms = getListStringToSearch();
      final List<Integer> patList =
         ManagerLocator.getPatientManager().findByNomInListManager(noms, SessionUtils.getSelectedBanques(sessionScope));
      // affichage de ces résultats
      showResultsAfterSearchByList(patList);
   }

   /**
    * Lance la recherche des patients en fournissant un fichier
    * Excel contenant une liste de nips.
    */
   public void onClick$findByListPatientNips(){
      // récupère les nips des patients présents dans le
      // fichier excel que l'utilisateur va uploader
      final List<String> nips = getListStringToSearch();
      final List<Integer> patients =
         ManagerLocator.getPatientManager().findByNipInListManager(nips, SessionUtils.getSelectedBanques(sessionScope));
      // affichage de ces résultats
      showResultsAfterSearchByList(patients);
   }

   @Override
   public void applyDroitsOnListe(){
      drawActionsButtons();
      super.applyDroitsOnListe();
      listObjectsRenderer.setAnonyme(isAnonyme());

      if(isAnonyme()){
         nomPatient.setDisabled(true);
         nomBoxPatient.setDisabled(true);
         nipPatient.setDisabled(true);
         nipBoxPatient.setDisabled(true);
      }else{
         nomPatient.setDisabled(false);
         nomBoxPatient.setDisabled(false);
         nipPatient.setDisabled(false);
         nipBoxPatient.setDisabled(false);
      }
   }

   public PatientsNbPrelevementsComparator getComparatorAsc(){
      return comparatorAsc;
   }

   public void setComparatorAsc(final PatientsNbPrelevementsComparator c){
      this.comparatorAsc = c;
   }

   public PatientsNbPrelevementsComparator getComparatorDesc(){
      return comparatorDesc;
   }

   public void setComparatorDesc(final PatientsNbPrelevementsComparator c){
      this.comparatorDesc = c;
   }

   @Override
   public void batchDelete(final List<Integer> ids, final String comment){
      ManagerLocator.getPatientManager().removeListFromIdsManager(ids, comment, SessionUtils.getLoggedUser(sessionScope));
   }
}
