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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementEdit;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 *
 * Controller gérant la fiche formulaire d'un prélèvement sous le gestionnaire
 * GATSBI. Controller créé le 25/05/2021.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePrelevementEditGatsbi extends FichePrelevementEdit
{

   private static final long serialVersionUID = 1L;

   private final List<Listbox> reqListboxes = new ArrayList<>();

   private final List<Combobox> reqComboboxes = new ArrayList<>();

   private final List<Div> reqConformeDivs = new ArrayList<>();

   // @wire
   private Groupbox groupPrlvt;

   private Contexte c;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      c = GatsbiController.initWireAndDisplay(this, 2, true, reqListboxes, reqComboboxes, reqConformeDivs, groupPrlvt,
         groupPrlvt);

      // setRows ne marche pas ?
      // seul moyen trouvé pour augmenter hauteur et voir tous les items de la listbox
      risquesBox.setHeight(c.getThesaurusValuesForChampEntiteId(249).size() * 25 + "px");
   }

   @Override
   protected ResumePatient initResumePatient(){
      return new ResumePatient(groupPatient, true);
   }

   @Override
   protected void enablePatientGroup(final boolean b){
      ((Groupbox) this.groupPatient).setOpen(b);
      ((Groupbox) this.groupPatient).setClosable(b);
   }

   /**
    * Surcharge Gastbi pour conserver sélectivement la
    * contrainte de sélection obligatiure des listes nature et statut juridique
    * dans le contexte TK historique
    */
   @Override
   protected void checkRequiredListboxes(){
      GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
   }

   /**
    * Processing echoEvent. Gatsbi surcharge... si aucun champ de formulaire dans
    * la page de transfert vers le site de stockage, passe directement à
    * l'échantillon.
    *
    * @see onClick$next
    */
   @Override
   public void onLaterNextStep(){

      log.debug("Surcharge Gastbi pour vérifier que la page de transfert des sites intermédiaire est affichée");

      // vérifie si au moins un des champs de formulaires est affiché
      final boolean oneDivVisible = c.getChampEntites().stream()
         .filter(c -> Arrays.asList(35, 36, 37, 38, 39, 40, 256, 267, 268).contains(c.getChampEntiteId()))
         .anyMatch(c -> c.getVisible());

      if(oneDivVisible || c.getSiteIntermediaire()){
         super.onLaterNextStep();
      }else{ // aucun formulaire n'est affiché -> passage direct à l'onglet échantillon
         log.debug("Aucun formulaire à affiché dans la page transfert vers le site préleveur...");
         if(this.prelevement.getPrelevementId() != null){
            getObjectTabController().switchToMultiEchantillonsEditMode(this.prelevement, new ArrayList<LaboInter>(),
               new ArrayList<LaboInter>());
         }else{
            // si nous sommes dans une action de création, on
            // appelle la page FicheMultiEchantillons en mode create
            getObjectTabController().switchToMultiEchantillonsCreateMode(this.prelevement, new ArrayList<LaboInter>());
         }

         Clients.clearBusy();
      }
   }

   /**
    * Surcharge pour gérer la redirection d'évènement lors du choix d'un paramétrage
    */
   @Override
   public void onGetInjectionDossierExterneDone(final Event e){
      super.onGetInjectionDossierExterneDone(((ForwardEvent) e).getOrigin());
   }

   /**
    * Plus d'obligation
    */
   @Override
   public void onSelect$naturesBoxPrlvt(){}

   @Override
   public void onSelect$consentTypesBoxPrlvt(){}

}
