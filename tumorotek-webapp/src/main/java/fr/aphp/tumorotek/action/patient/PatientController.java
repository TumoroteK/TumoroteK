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
package fr.aphp.tumorotek.action.patient;

import static fr.aphp.tumorotek.model.contexte.EContexte.BTO;
import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentContexte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.patient.bto.FichePatientEditBTO;
import fr.aphp.tumorotek.action.patient.bto.FichePatientStaticBTO;
import fr.aphp.tumorotek.action.patient.bto.ListePatientBTO;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller de l'onglet patient.
 * Controller créé partir de l'équivalent prélèvement crée
 * par Pierre.
 * Date: 01/12/2009
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientController extends AbstractObjectTabController
{

   private static final long serialVersionUID = -5315034328095152785L;

   private Div divPatientStatic;
   private Div divPatientEdit;
   private Div modifMultiDiv;
   private Component listePatient;
   private Component listePatientBTO;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      setEntiteTab(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0));

      super.doAfterCompose(comp);
      if(BTO.equals(getCurrentContexte())){
         listePatientBTO.setVisible(true);
      }else{
         listePatient.setVisible(true);
      }

      setStaticDiv(divPatientStatic);
      setEditDiv(divPatientEdit);
      setModifMultiDiv(modifMultiDiv);

      if(BTO.equals(getCurrentContexte())){
         setStaticZulPath("/zuls/patient/bto/FichePatientStaticBTO.zul");
         setEditZulPath("/zuls/patient/bto/FichePatientEditBTO.zul");
      }else{
         setStaticZulPath("/zuls/patient/FichePatientStatic.zul");
         setEditZulPath("/zuls/patient/FichePatientEdit.zul");
      }

      setMultiEditZulPath("/zuls/patient/FicheModifMultiPatient.zul");

      initFicheStatic();

      switchToOnlyListeMode();
      orderAnnotationDraw(false);
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getPatientManager().findByIdManager(id);
   }

   @Override
   public AbstractFicheEditController getFicheEdit(){
      if(BTO.equals(getCurrentContexte())){
         return ((FichePatientEditBTO) self.getFellow("divPatientEdit").getFellow("fwinPatientEditBTO")
            .getAttributeOrFellow("fwinPatientEditBTO$composer", true));
      }else{
         return ((FichePatientEdit) self.getFellow("divPatientEdit").getFellow("fwinPatientEdit")
            .getAttributeOrFellow("fwinPatientEdit$composer", true));
      }
   }

   @Override
   public AbstractFicheStaticController getFicheStatic(){
      if(BTO.equals(getCurrentContexte())){
         return ((FichePatientStaticBTO) self.getFellow("divPatientStatic").getFellow("fwinPatientStaticBTO")
            .getAttributeOrFellow("fwinPatientStaticBTO$composer", true));
      }else{
         return ((FichePatientStatic) self.getFellow("divPatientStatic").getFellow("fwinPatientStatic")
            .getAttributeOrFellow("fwinPatientStatic$composer", true));
      }
   }

   @Override
   public ListePatient getListe(){
      if(BTO.equals(getCurrentContexte())){
         return ((ListePatientBTO) self.getFellow("listePatientBTO").getFellow("lwinPatientBTO")
            .getAttributeOrFellow("lwinPatientBTO$composer", true));
      }else{
         return ((ListePatient) self.getFellow("listePatient").getFellow("lwinPatient")
            .getAttributeOrFellow("lwinPatient$composer", true));
      }
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return ((FicheModifMultiPatient) self.getFellow("modifMultiDiv").getFellow("fwinModifMultiPatient")
         .getAttributeOrFellow("fwinModifMultiPatient$composer", true));
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      if(self.getFellowIfAny("ficheAnnoPatient") != null){
         return ((FicheAnnotation) self.getFellow("ficheAnnoPatient").getFellow("fwinAnnotation")
            .getAttributeOrFellow("fwinAnnotation$composer", true));
      }else{
         return null;
      }
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * @return
    * @since 2.2.0
    */
   @Override
   public FicheAnnotationInline getFicheAnnotationInline(){
      if(null != self.getFellow("divPatientEdit").getFellowIfAny("fwinPatientEdit")
         && null != self.getFellow("divPatientEdit").getFellow("fwinPatientEdit").getFellow("ficheTissuInlineAnnoPatient")){
         return ((FicheAnnotationInline) self.getFellow("divPatientEdit").getFellow("fwinPatientEdit")
            .getFellow("ficheTissuInlineAnnoPatient").getFellow("fwinAnnotationInline")
            .getAttributeOrFellow("fwinAnnotationInline$composer", true));
      }else if(null != self.getFellow("divPatientEdit").getFellowIfAny("fwinPatientEditBTO")
         && null != self.getFellow("divPatientEdit").getFellow("fwinPatientEditBTO").getFellow("ficheTissuInlineAnnoPatient")){
         return ((FicheAnnotationInline) self.getFellow("divPatientEdit").getFellow("fwinPatientEditBTO")
            .getFellow("ficheTissuInlineAnnoPatient").getFellow("fwinAnnotationInline")
            .getAttributeOrFellow("fwinAnnotationInline$composer", true));
      }else{
         return null;
      }
   }

   @Override
   public void initLinkedControllers(){
      if(getMainWindow().isFullfilledComponent("prelevementPanel", "winPrelevement")){

         final PrelevementController prelController = (PrelevementController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("prelevementPanel").getFellow("winPrelevement").getAttributeOrFellow("winPrelevement$composer", true);

         getReferencedObjectsControllers().add(prelController);

         if(!prelController.getReferencingObjectControllers().contains(this)){
            prelController.getReferencingObjectControllers().add(this);
         }
      }
   }

   /**
    * Surchage la methode pour prendre en compte l'affichage cumulatif
    * des champs annotation en toutes collections.
    */
   @Override
   public void orderAnnotationDraw(final boolean isMulti){
      // passe l'entite au controller
      getFicheAnnotation().setEntite(getEntiteTab());
      getFicheAnnotation().setObjectTabController(this);

      // ordonne le dessin du contenu en mode cumulatif si toutes collections
      // toutes collections
      final List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
      if(banks.size() == 1){
         getFicheAnnotation().setBankUsedToDrawChamps(banks.get(0));
         getFicheAnnotation().drawAnnotationPanelContent(isMulti, false);
      }else{
         // clean up
         Components.removeAllChildren(getFicheAnnotation().getAnnoRows());
         /*Iterator<Banque> banksIt = banks.iterator();
         while (banksIt.hasNext()) {
         	getFicheAnnotation().setBankUsedToDrawChamps(banksIt.next());
         	getFicheAnnotation().drawAnnotationPanelContent(isMulti, true);
         }*/
      }

      setAnnoRegionVisible();
   }

   /**
    * Selectionne le tab et renvoie le controller Patient.
    * @param page
    * @return controller tab
    */
   public static AbstractObjectTabController backToMe(final MainWindow window, final Page page){

      PatientController tabController = null;

      // on récupère les panels
      final Tabbox panels = (Tabbox) page.getFellow("mainWin").getFellow("main").getFellow("mainTabbox");

      if(panels != null){
         // on récupère le panel de l'entite
         final Tabpanel panel = (Tabpanel) panels.getFellow("patientPanel");

         window.createMacroComponent("/zuls/patient/Patient.zul", "winPatient", panel);

         tabController = ((PatientController) panel.getFellow("winPatient").getAttributeOrFellow("winPatient$composer", true));

         panels.setSelectedPanel(panel);
      }

      return tabController;
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   @Override
   public List<? extends Object> getChildrenObjects(final TKdataObject obj){
      return ManagerLocator.getPrelevementManager().findByPatientManager((Patient) obj);
   }

   @Override
   public Map<Entite, List<Integer>> getChildrenObjectsIds(final List<Integer> ids){
      final Map<Entite, List<Integer>> children = new HashMap<>();

      // prelevements
      final Entite prelEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      children.put(prelEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         prelEntite, SessionUtils.getSelectedBanques(sessionScope), false));

      return children;
   }
}
