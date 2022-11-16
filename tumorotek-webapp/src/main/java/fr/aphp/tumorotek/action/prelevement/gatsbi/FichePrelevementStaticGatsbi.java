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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.echantillon.EchantillonRowRenderer;
import fr.aphp.tumorotek.action.echantillon.gatsbi.EchantillonRowRendererGatsbi;
import fr.aphp.tumorotek.action.echantillon.gatsbi.GatsbiControllerEchantillon;
import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prelevement.FichePrelevementStatic;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche static d'un prélèvement GATSBI.
 *
 * @author mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class FichePrelevementStaticGatsbi extends FichePrelevementStatic
{

   private static final long serialVersionUID = -7612780578022559022L;

   private Groupbox groupPrlvt;

   private Contexte contexte;

   private final EchantillonRowRendererGatsbi echantillonRendererGatsbi =
      new EchantillonRowRendererGatsbi(false, false, false, false);

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      contexte = GatsbiController.initWireAndDisplay(this, 2, false, null, null, null, groupPrlvt, (Groupbox) gridFormPrlvtComp);
      
      // affichage conditionnel des champs patients
      GatsbiControllerPrelevement.applyPatientContext(groupPatient, false);

      // prelevement specific
      if(groupLaboInter != null){
         groupLaboInter.setVisible(contexte != null && contexte.getSiteIntermediaire());
      }

      // Injection contexte echantillon pour inner list
      // ce contexte peut être (null) non paramétré pour l'étude
      // donc GET le contexte defaut pour le ContexteType Echantillon
      Contexte echanContexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(3);
      if(echanContexte == null){
         echanContexte = GatsbiController.getGastbiDefautContexteForType(ContexteType.ECHANTILLON);
         echantillonRendererGatsbi.setContexte(echanContexte);
      }

      // inner list
      // non deletable
      // ne force pas affichage emplacement et statut stockage en fin de grid
      GatsbiControllerEchantillon.drawColumnsForEchantillons(echanContexte, echantillonsGrid, echantillonRendererGatsbi, false,
         false, getTtesCollections());
   }

   @Override
   protected ResumePatient initResumePatient(){
      return new ResumePatient(groupPatient, SessionUtils.getCurrentGatsbiContexteForEntiteId(1), null);
   }

   @Override
   protected void enablePatientGroup(final boolean b){
      ((Groupbox) this.groupPatient).setOpen(b);
      ((Groupbox) this.groupPatient).setClosable(b);
   }

   /**
    * Gatsbi surcharge pour intercaler une modale de sélection des parametrages
    * proposés par le contexte.
    *
    * @param click event
    */
   @Override
   public void onClick$addNew(){
      GatsbiController.addNewObjectForContext(contexte, self, e -> {
         try{
            super.onClick$addNew();
         }catch(final Exception ex){
            Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }, null, null);
   }

   /**
    * Un parametrage a été sélectionné.
    *
    * @param param
    * @throws Exception
    */
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
                  super.onClick$addNew();
               }catch(final Exception ex){
                  Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
               }
            }, evt);
      }catch(final GatsbiException e){
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void onClick$addEchan(final Event event) throws Exception{

      final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);

      GatsbiController.addNewObjectForContext(SessionUtils.getCurrentGatsbiContexteForEntiteId(3),
         tabController.getListe().getSelfComponent(), e -> {
            try{
               super.onClick$addEchan(event);
            }catch(final Exception ex){
               Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
            }
         }, event, this.prelevement);
   }

   /*********** inner lists ******************/

   @Override
   public EchantillonRowRenderer getEchantillonRenderer(){
      return echantillonRendererGatsbi;
   }
}
