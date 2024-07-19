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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.ListePatient;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class ListePatientGatsbi extends ListePatient
{

   private static final long serialVersionUID = 1L;

   private Contexte contexte;
   
   private boolean firstOrganeCode = false;
   
   // Critères de recherche.
   private Radio identifiantPatient;
   private Textbox identifiantBoxPatient;
   private String searchIdentifiantPatient;

   public ListePatientGatsbi(){
      
      contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
      
      firstOrganeCode = SessionUtils
         .getCurrentGatsbiContexteForEntiteId(3).isChampInTableau(229);
      
      setListObjectsRenderer(new PatientRowRendererGatsbi(true, firstOrganeCode));
   }
   
   @Override
   public void doAfterCompose(Component comp) throws Exception{
      super.doAfterCompose(comp);
      ((PatientRowRendererGatsbi) getListObjectsRenderer())
                  .setCurBanque(SessionUtils.getCurrentBanque(sessionScope));
   }

   public void onCheckAll$gridColumns(){
      onCheck$checkAll();
   }

   @Override
   protected void drawColumnsForVisibleChampEntites()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{

      // check box first column, toujours affichée
      final Checkbox cbox = new Checkbox();
      cbox.setId("checkAll");
      cbox.addForward("onCheck", objectsListGrid.getColumns(), "onCheckAll");
      GatsbiController.addColumn(objectsListGrid, null, "40px", null, cbox, null, true);
      
      // identifiant column, toujours affichée
      //TG-182 : tri sur le champ identifiant du getter getIdentifiant() (qui retourne un PatientIdentifiant) de patient
      GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Identifiant", null, null, null, "auto(identifiant.identifiant)", true);

      // variable columns
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         GatsbiControllerPatient.addColumnForChpId(chpId, objectsListGrid);
      }
      
      // nb prels
      nbPrelevementsColumn = GatsbiController.addColumn(objectsListGrid, "patient.nbPrelevements", 
                                             null, null, null, "auto", true);
      
      // premier code organe
      if (firstOrganeCode) {
         GatsbiController.addColumn(objectsListGrid, "Champ.Echantillon.Organe", 
                                    null, null, null, null, true);
      }      
   }
   
   @Override
   protected void drawQuickSearchForVisibleChampEntites() {
      // nom visible
      if (!contexte.isChampIdVisible(3)) {
         nomPatient.getParent().setVisible(false);
      }
      // nip visible
      if (!contexte.isChampIdVisible(2)) {
         nipPatient.getParent().setVisible(false);
      }
      
   }
   
   /********** recherches par identifiant ********************/
   
   /**
    * Méthode appelée après lors du focus sur le champ
    * identifiantBoxPatient. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$identifiantBoxPatient(){
      identifiantPatient.setChecked(true);
   }
   
   @Override
   public void applyDroitsOnListe(){
      super.applyDroitsOnListe();
      
      identifiantPatient.setDisabled(isAnonyme());
      identifiantBoxPatient.setDisabled(isAnonyme());
   }
   
   @Override
   public List<Integer> doFindObjects(){
      List<Integer> patients = new ArrayList<>();

       if(identifiantPatient.isChecked()){
         if(!searchIdentifiantPatient.equals("")){
            if(searchIdentifiantPatient.contains(",")){
               final List<String> pats = ObjectTypesFormatters.formateStringToList(searchIdentifiantPatient);
               patients =
                  ManagerLocator.getPatientManager()
                     .findByIdentifiantsInListManager(pats, SessionUtils.getSelectedBanques(sessionScope));
            }else{
               patients = ManagerLocator.getPatientManager().findByIdentifiantLikeBothSideReturnIdsManager(searchIdentifiantPatient,
                  SessionUtils.getSelectedBanques(sessionScope), true);
            }
         }else{
            if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {"Patient"}),
               Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               patients = ManagerLocator.getPatientManager()
                  .findAllObjectsIdsWithBanquesManager(SessionUtils.getSelectedBanques(sessionScope));
            }
         }
      } else {
         patients = super.doFindObjects();
      }
      return patients;
   }
   
   /**
    * Lance la recherche des patients en fournissant un fichier
    * Excel contenant une liste d'identifiants.
    */
   public void onClick$findByListPatientIdentifiants(){
      // récupère les nips des patients présents dans le
      // fichier excel que l'utilisateur va uploader
      final List<String> identifiants = getListStringToSearch();
      final List<Integer> patients =
         ManagerLocator.getPatientManager()
            .findByIdentifiantsInListManager(identifiants, SessionUtils.getSelectedBanques(sessionScope));
      // affichage de ces résultats
      showResultsAfterSearchByList(patients);
   }

   public String getSearchIdentifiantPatient(){
      return searchIdentifiantPatient;
   }

   public void setSearchIdentifiantPatient(String _i){
      this.searchIdentifiantPatient = _i;
   }
}
