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
package fr.aphp.tumorotek.action.cession;

import static fr.aphp.tumorotek.model.contexte.EContexte.BTO;
import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentContexte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.administration.AdministrationController;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.cession.bto.FicheCessionEditBTO;
import fr.aphp.tumorotek.action.cession.bto.FicheCessionStaticBTO;
import fr.aphp.tumorotek.action.cession.bto.ListeCessionBTO;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller de l'onglet Cession.
 * Controller créé le 02/01/2010.
 *
 * @author Pierre Ventadour
 * @version 2.2.0
 */
public class CessionController extends AbstractObjectTabController
{

   private static final long serialVersionUID = 5668387010539952631L;

   private Div divCessionStatic;
   private Div divCessionEdit;
   private Component listeCession;
   private Component listeCessionBTO;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      setEntiteTab(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0));

      super.doAfterCompose(comp);

      setStaticDiv(divCessionStatic);
      setEditDiv(divCessionEdit);
      if(BTO.equals(getCurrentContexte())){
         listeCessionBTO.setVisible(true);
         setEditZulPath("/zuls/cession/bto/FicheCessionEditBTO.zul");
         setStaticZulPath("/zuls/cession/bto/FicheCessionStaticBTO.zul");
      }else{
         listeCession.setVisible(true);
         setEditZulPath("/zuls/cession/FicheCessionEdit.zul");
         setStaticZulPath("/zuls/cession/FicheCessionStatic.zul");
      }

      initFicheStatic();

      orderAnnotationDraw(false);

      switchToOnlyListeMode();
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getCessionManager().findByIdManager(id);
   }

   @Override
   public FicheCessionStatic getFicheStatic(){
      if(BTO.equals(getCurrentContexte())){
         return ((FicheCessionStaticBTO) this.self
            //.getFellow("ficheCession")
            .getFellow("fwinCessionStaticBTO").getAttributeOrFellow("fwinCessionStaticBTO$composer", true));
      }else{
         return ((FicheCessionStatic) this.self
            //.getFellow("ficheCession")
            .getFellow("fwinCessionStatic").getAttributeOrFellow("fwinCessionStatic$composer", true));
      }
   }

   @Override
   public FicheCessionEdit getFicheEdit(){
      if(BTO.equals(getCurrentContexte())){
         return ((FicheCessionEditBTO) self.getFellow("divCessionEdit").getFellow("fwinCessionEditBTO")
            .getAttributeOrFellow("fwinCessionEditBTO$composer", true));
      }else{
         return ((FicheCessionEdit) self.getFellow("divCessionEdit").getFellow("fwinCessionEdit")
            .getAttributeOrFellow("fwinCessionEdit$composer", true));
      }
   }

   @Override
   public ListeCession getListe(){
      if(BTO.equals(getCurrentContexte())){
         return ((ListeCessionBTO) self.getFellow("listeCessionBTO").getFellow("lwinCessionBTO")
            .getAttributeOrFellow("lwinCessionBTO$composer", true));
      }else{
         return ((ListeCession) self.getFellow("listeCession").getFellow("lwinCession")
            .getAttributeOrFellow("lwinCession$composer", true));
      }
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return null;
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      if(self.getFellowIfAny("ficheAnnoCession") != null){
         return ((FicheAnnotation) self.getFellow("ficheAnnoCession").getFellow("fwinAnnotation")
            .getAttributeOrFellow("fwinAnnotation$composer", true));
      }else{
         return null;
      }
   }

   @Override
   public FicheAnnotationInline getFicheAnnotationInline(){
      return null;
   }

   @Override
   public void initLinkedControllers(){

      // echantillon
      if(getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){

         final EchantillonController echanController = (EchantillonController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("echantillonPanel").getFellow("winEchantillon").getAttributeOrFellow("winEchantillon$composer", true);

         getReferencedObjectsControllers().add(echanController);

         if(!echanController.getReferencedObjectsControllers().contains(this)){
            echanController.getReferencedObjectsControllers().add(this);
         }
      }

      // derives
      if(getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")){

         final ProdDeriveController deriveController = (ProdDeriveController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("derivePanel").getFellow("winProdDerive").getAttributeOrFellow("winProdDerive$composer", true);

         getReferencedObjectsControllers().add(deriveController);

         if(!deriveController.getReferencedObjectsControllers().contains(this)){
            deriveController.getReferencedObjectsControllers().add(this);
         }
      }
   }

   /**
    * Selectionne le tab et renvoie le controller Cession.
    *
    * @param page
    * @return controller tab
    */
   public static AbstractObjectTabController backToMe(final MainWindow window, final Page page){

      CessionController tabController = null;

      // on récupère les panels
      final Tabbox panels = (Tabbox) page.getFellow("mainWin").getFellow("main").getFellow("mainTabbox");

      if(panels != null){
         // on récupère le panel de l'entite
         final Tabpanel panel = (Tabpanel) panels.getFellow("cessionPanel");

         window.createMacroComponent("/zuls/cession/Cession.zul", "winCession", panel);

         tabController = ((CessionController) panel.getFellow("winCession").getAttributeOrFellow("winCession$composer", true));

         panels.setSelectedPanel(panel);
      }

      return tabController;
   }

   /**
    * Méthode permettant de passer en mode "fiche création" : seule
    * la fiche de la cession est visible.
    */
   public void switchToCreateMode(final List<Echantillon> echansACeder, final List<ProdDerive> derivesACeder,
      final Terminale terminale){

      super.switchToCreateMode(null);

      // on cache la liste
      getFicheEdit().switchToCreateMode(echansACeder, derivesACeder, terminale);
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   /**
    * Met à jour la fiche contrat lors d'une modification
    * ou suppression de cession.
    *
    * @param Contrat cont
    */
   public void updateContrat(final Contrat cont){
      if(cont != null){
         // contrats
         if(getMainWindow().isFullfilledComponent("administrationPanel", "winAdministration")){

            if(((AdministrationController) getMainWindow().getMainTabbox().getTabpanels().getFellow("administrationPanel")
               .getFellow("winAdministration").getAttributeOrFellow("winAdministration$composer", true))
                  .isFullfilledComponent("contratPanel", "contratMacro")){
               ((AdministrationController) getMainWindow().getMainTabbox().getTabpanels().getFellow("administrationPanel")
                  .getFellow("winAdministration").getAttributeOrFellow("winAdministration$composer", true)).getContratController()
                     .getListe().updateObjectGridListFromOtherPage(cont, true);
            }

         }
      }
   }

   @Override
   public List<? extends Object> getChildrenObjects(final TKdataObject obj){
      final List<TKAnnotableObject> childrens = new ArrayList<>();
      final Iterator<CederObjet> echIt =
         ManagerLocator.getCederObjetManager().getEchantillonsCedesByCessionManager((Cession) obj).iterator();

      while(echIt.hasNext()){
         childrens.add(ManagerLocator.getEchantillonManager().findByIdManager(echIt.next().getObjetId()));
      }

      final Iterator<CederObjet> derIt =
         ManagerLocator.getCederObjetManager().getProdDerivesCedesByCessionManager((Cession) obj).iterator();
      while(derIt.hasNext()){
         childrens.add(ManagerLocator.getProdDeriveManager().findByIdManager(derIt.next().getObjetId()));
      }
      return childrens;
   }

   @Override
   public Map<Entite, List<Integer>> getChildrenObjectsIds(final List<Integer> ids){
      final Map<Entite, List<Integer>> children = new HashMap<>();

      // echantillon
      final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      children.put(echanEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         echanEntite, SessionUtils.getSelectedBanques(sessionScope), true));

      // derives
      final Entite deriveEntite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      children.put(deriveEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         deriveEntite, SessionUtils.getSelectedBanques(sessionScope), true));

      return children;
   }

   /**
    * Gère un scan full-rack barcode 2D au niveau de l'onglet cession:
    * - si mode fiche statique ET cession validée: check!
    *
    * @param ScanTerminale
    * @since 2.1
    */
   @Override
   public void handleScanTerminale(final ScanTerminale sT){
      if(sT != null){
         if(getStaticDiv().isVisible() && getFicheStatic().getObject() != null
            && getFicheStatic().getObject().getCessionId() != null
            && getFicheStatic().getObject().getCessionStatut().getStatut().equals("VALIDEE")){
            getFicheStatic().applyTKObjectsCheckFromScan(sT);
         }
      }
   }
}
