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

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prelevement.ListePrelevement;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.manager.exception.TKException;
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

   // flag passe à true si la colonne congelation est déja rendue
   // afin d'éviter que cette colonne soit rendue deux fois
   private boolean congColRendered = false;
   
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
      GatsbiController.addColumn(objectsListGrid, "general.code", null, null, null, "auto(code)", true);

      // ttes collection
      GatsbiController.addColumn(objectsListGrid, "Entite.Banque", null, null, null, "auto(code)", isTtesCollection());

      // patient, identifiant colonne toujours affichée
      GatsbiController.addColumn(objectsListGrid, "Champ.Patient.Identifiant", null, null, null, null, true);

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
      GatsbiController.addColumn(objectsListGrid, "prelevement.maladie", "150px", null, null, "auto(maladie.patient.nom)",
         getBanqueDefMaladies());

      // variable columns
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         addColumnForChpId(chpId);
      }

      // nb echantillons
      final Vbox vbox = new Vbox();
      vbox.setAlign("center");
      final Label nbEch = new Label(Labels.getLabel("prelevement.nbEchantillons"));
      nbEch.setStyle("font-weight : bold;font-family: Verdana, Arial, Helvetica, sans-serif;");
      nbEch.setParent(vbox);
      final Label nbEchRest = new Label(Labels.getLabel("prelevement.restants.total.stockes"));
      nbEchRest.setStyle("font-weight : bold;font-family: Verdana, Arial, Helvetica, sans-serif;");
      nbEchRest.setParent(vbox);
      nbEchantillonsColumn = GatsbiController.addColumn(objectsListGrid, null, null, "center", vbox, "auto", true);
      nbEchantillonsColumn.setId("nbEchantillonsColumn");
   }

   private void addColumnForChpId(final Integer chpId)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      switch(chpId){
         case 23:
            // "code" toujours rendu par défaut
            break;
         case 45: // numero labo
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.NumeroLabo", null, null, null, "auto(numeroLabo)",
               true);
            break;
         case 24: // nature
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.Nature.Nature", null, null, null, "auto(nature.nom)",
               true);
            break;
         case 44: // nda
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.PatientNda", null, null, null, "auto(patientNda)",
               true);
            break;
         case 30: // date prelevement
            GatsbiController.addColumn(objectsListGrid, "prelevement.datePrelevementCourt", null, null, null,
               "auto(datePrelevement)", true);
            break;
         case 31: // prelevement type
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.PrelevementType.Type", null, null, null,
               "auto(prelevementType.nom)", true);
            break;
         case 47: // sterile
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.Sterile", null, null, null, "auto(sterile)", true);
            break;
         case 249: // risques -> rendu sous la forme d'une icône
            break;
         case 29: // service preleveur
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.ServicePreleveur", null, null, null,
               "auto(servicePreleveur.nom)", true);
            break;
         case 28: // preleveur
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.Preleveur", null, null, null,
               "auto(preleveur.nomAndPrenom)", true);
            break;
         case 32: // condit type
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.ConditType.Type", null, null, null,
               "auto(conditType.nom)", true);
            break;
         case 34: // condit Nbr
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.ConditNbr", null, null, null, "auto(conditNbr)", true);
            break;
         case 33: // condit milieu
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.ConditMilieu", null, null, null,
               "auto(conditMilieu.nom)", true);
            break;
         case 26: // consent type
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.ConsentType", null, null, null,
               "auto(consentType.nom)", true);
            break;
         case 27: // consent date
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.ConsentDate", null, null, null, "auto(consentDate)",
               true);
            break;
         case 35: // date depart
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.DateDepart", null, null, null, "auto(dateDepart)",
               true);
            break;
         case 36: // transporteur
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.Transporteur", null, null, null,
               "auto(transporteur.nom)", true);
            break;
         case 37: // transport temperature
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.TransportTemp", null, null, null,
               "auto(transportTemp)", true);
            break;
         case 269: // cong depart
            if(!congColRendered){
               GatsbiController.addColumn(objectsListGrid, "prelevement.cong", null, null, null, "auto(transportTemp)", true);
               congColRendered = true;
            }
            break;
         case 38: // date arrivee
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.DateArrivee", null, null, null, "auto(dateArrivee)",
               true);
            break;
         case 39: // operateur
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.Operateur", null, null, null, "auto(operateur.nom)",
               true);
            break;
         case 40: // quantite
            GatsbiController.addColumn(objectsListGrid, "Champ.Prelevement.Quantite", null, null, null, "auto(quantite)", true);
            break;
         case 270: // cong arrivee
            if(!congColRendered){
               GatsbiController.addColumn(objectsListGrid, "prelevement.cong", null, null, null, "auto(transportTemp)", true);
               congColRendered = true;
            }
            break;
         case 256: // conforme arrivee -> rendu sous la forme d'une icône (chpId=257 raisons, ignore)
            break;
         default:
            break;
      }
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
                  } else { // parentObj = premier prelevement créé pour le patient
                     // createAnotherPrelevement
                     getObjectTabController().swithToCreatedModeFromCopy((Prelevement) 
                        ((Map<String, Object>) evt.getOrigin().getData()).get("parentObj"));
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
