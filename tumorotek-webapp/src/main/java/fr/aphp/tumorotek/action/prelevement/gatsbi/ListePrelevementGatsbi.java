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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prelevement.ListePrelevement;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class ListePrelevementGatsbi extends ListePrelevement
{

   private static final long serialVersionUID = 1L;

   private Contexte contexte;
   
   public ListePrelevementGatsbi(){
      
      contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);
      
      setListObjectsRenderer(new PrelevementRowRendererGatsbi(true, false));
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

      // icones column, visible si non conformites OU risque est visible OU interfacages
      if(contexte.isChampIdVisible(249) || contexte.isChampIdVisible(256)
         || SessionUtils.getEmetteursInterfacages(sessionScope).size() > 0){
         GatsbiController.addColumn(objectsListGrid, null,
            (contexte.isChampIdVisible(249) && contexte.isChampIdVisible(256)) ? "70px" : "35px", "center", null, null, true);

         // indique au row renderer qu'il doit dessiner les icones
         getListObjectsRenderer().setIconesRendered(true);
      }

      // code prel column, toujours affichée
      // GatsbiController.addColumn(objectsListGrid, "general.code", null, null, null, "auto(code)", true);
      GatsbiControllerPrelevement.drawCodeColumn(objectsListGrid);

      // ttes collection
      GatsbiControllerPrelevement.drawBanqueColumn(objectsListGrid, isTtesCollection());

      // patient, identifiant colonne toujours affichée
      GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Identifiant", null, null, null, "auto(maladie.patient.identifiant.identifiant)", true);

      // patient
      Contexte patientContexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
      
      // nom + prenom s'affichent dans une seule colonne si pas contexte Gatsbi
      // ou si spécifié par contexte Gatsbi (visible par défaut)
      if (patientContexte == null 
            || (patientContexte.isChampIdVisible(3) && patientContexte.isChampInTableau(3))) {
         GatsbiController.addColumn(objectsListGrid, "prelevement.patient", 
                        null, null, null, "auto(maladie.patient.nom)", true);
      }
      
      // nip s'affiche dans une colonne si spécifié par contexte Gatsbi (invisible par défaut)
      if (patientContexte != null 
            && patientContexte.isChampIdVisible(2) && patientContexte.isChampInTableau(2)) {
         GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Nip", 
                        null, null, null, "auto(maladie.patient.nip)", false);
      }

      // maladie, colonne visible si banque définit le niveau
      GatsbiController.addColumn(objectsListGrid, "prelevement.visite", "150px", null, null, "auto(maladie.patient.nom)",
         getBanqueDefMaladies());

      // variable columns
      boolean congColRendered = false;
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
                
         congColRendered = GatsbiControllerPrelevement
            .addColumnForChpIdAndReturnsIfCongColRendered(chpId, objectsListGrid, congColRendered);
      }

      // nb echantillons
      nbEchantillonsColumn = GatsbiControllerPrelevement.drawEchanNbColumn(objectsListGrid, "nbEchantillonsColumn");
   }

   /**
    * Gatsbi surcharge pour intercaler une modale de sélection des parametrages
    * proposés par le contexte.
    *
    * @param click event
    */
   @Override
   public void onClick$addNew(final Event event) throws Exception{
      GatsbiController.addNewObjectForContext(contexte, self, e -> {
         try{
            super.onClick$addNew(e);
         }catch(final Exception ex){
            Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }, event, null);
   }

   /**
    * Un parametrage a été sélectionné.
    *
    * @param param
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void onGetSelectedParametrage(final ForwardEvent evt) throws Exception{

      try{

         GatsbiController.getSelectedParametrageFromSelectEvent(contexte, SessionUtils.getCurrentBanque(sessionScope),
            getObjectTabController(), p -> {
               // cong depart OU cong arrivee
               if(p.getDefaultValuesForChampEntiteId(269) != null && p.getDefaultValuesForChampEntiteId(269).contentEquals("1")
                  && p.getDefaultValuesForChampEntiteId(270) != null
                  && p.getDefaultValuesForChampEntiteId(270).contentEquals("1")){
                  throw new TKException("gatsbi.illegal.parametrage.prelevement.cong");
               }
            }, () -> {
               try{
                  if (evt == null || evt.getOrigin() == null || evt.getOrigin().getData() == null 
                        || ((Map<String, Object>) evt.getOrigin().getData()).get("parentObj") == null) {
                     super.onClick$addNew(null);
                  } else { 
                     // parentObj, si = Prelevement alors premier prelevement créé pour le patient
                     //   -> createAnotherPrelevement
                     // si = Maladie, alors création prélèvement
                     TKdataObject parent = (TKdataObject) 
                        ((Map<String, Object>) evt.getOrigin().getData()).get("parentObj");
                     if (parent instanceof Prelevement) { // createAnotherPrelevement
                        getObjectTabController().swithToCreatedModeFromCopy((Prelevement) parent);
                     } else if (parent instanceof Maladie) { // 
                        getObjectTabController().switchToCreateMode(parent);
                     }
                  }
               }catch(final Exception ex){
                  Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
               }
            }, evt);
      }catch(final GatsbiException e){
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   protected List<Integer> findPrelevementsByPatientCodes(List<String> pats){
      return ManagerLocator.getPrelevementManager().findByPatientIdentifiantOrNomOrNipInListManager(pats,
         SessionUtils.getSelectedBanques(sessionScope));
   }
   
   @Override
   protected List<Integer> searchPrelevementByPatientInfos(String search){
      return ManagerLocator.getPrelevementManager().findByPatientIdentifiantOrNomOrNipReturnIdsManager(search,
         SessionUtils.getSelectedBanques(sessionScope), true);
   }
}
