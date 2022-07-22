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
package fr.aphp.tumorotek.action.echantillon;

import static fr.aphp.tumorotek.model.contexte.EContexte.SEROLOGIE;
import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentContexte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Box;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.cession.CessionController;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.serotk.ListeEchantillonSero;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller de l'onglet échantillon.
 * Controller créé le 02/11/2009.
 *
 * @author Pierre Ventadour
 * @version 2.2.0
 * @since 2.1.0
 */
public class EchantillonController extends AbstractObjectTabController
{
   private static final long serialVersionUID = -3799945305452822008L;

   private Div divEchantillonStatic;

   private Div divEchantillonEdit;

   private Div modifMultiDiv;

   private String createZulPath = "/zuls/echantillon/FicheMultiEchantillons.zul";

   // flag ordonnant le retour vers la fiche prelevement
   private boolean fromFichePrelevement = false;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      setEntiteTab(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));

      super.doAfterCompose(comp);

      setStaticDiv(divEchantillonStatic);
      setEditDiv(divEchantillonEdit);
      setModifMultiDiv(modifMultiDiv);

      switch(getCurrentContexte()){
         case SEROLOGIE:
            setEditZulPath("/zuls/echantillon/serotk/FicheEchantillonEditSero.zul");
            createZulPath = "/zuls/echantillon/serotk/FicheMultiEchantillonsSero.zul";
            setMultiEditZulPath("/zuls/echantillon/serotk/FicheModifMultiEchantillonSero.zul");
            setStaticZulPath("/zuls/echantillon/serotk/FicheEchantillonStaticSero.zul");
            setListZulPath("/zuls/echantillon/serotk/ListeEchantillonSero.zul");
            break;
         default:
            if(SessionUtils.getCurrentGatsbiContexteForEntiteId(3) == null){
               createZulPath = "/zuls/echantillon/FicheMultiEchantillons.zul";
               setEditZulPath("/zuls/echantillon/FicheEchantillonEdit.zul");
               setMultiEditZulPath("/zuls/echantillon/FicheModifMultiEchantillon.zul");
               setStaticZulPath("/zuls/echantillon/FicheEchantillonStatic.zul");
               setListZulPath("/zuls/echantillon/ListeEchantillon.zul");
            }else{
               createZulPath = "/zuls/echantillon/gatsbi/FicheMultiEchantillonsGatsbi.zul";
               setEditZulPath("/zuls/echantillon/gatsbi/FicheEchantillonEditGatsbi.zul");
               setStaticZulPath("/zuls/echantillon/gatsbi/FicheEchantillonStaticGatsbi.zul");
               setMultiEditZulPath("/zuls/echantillon/gatsbi/FicheModifMultiEchantillonGatsbi.zul");
               setListZulPath("/zuls/echantillon/gatsbi/ListeEchantillonGatsbi.zul");
            }
            break;
      }

      drawListe();

      initFicheStatic();

      switchToOnlyListeMode();
      orderAnnotationDraw(false);

   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getEchantillonManager().findByIdManager(id);
   }

   @Override
   public FicheEchantillonStatic getFicheStatic(){
      return ((FicheEchantillonStatic) self.getFellow("divEchantillonStatic").getFellow("fwinEchantillonStatic")
         .getAttributeOrFellow("fwinEchantillonStatic$composer", true));
   }

   @Override
   public FicheEchantillonEdit getFicheEdit(){
      if(hasMultiFicheEdit()){
         return getMultiFicheEdit();
      }
      return ((FicheEchantillonEdit) self.getFellow("divEchantillonEdit").getFellow("fwinEchantillonEdit")
         .getAttributeOrFellow("fwinEchantillonEdit$composer", true));
   }

   public FicheMultiEchantillons getMultiFicheEdit(){
      return ((FicheMultiEchantillons) self.getFellow("divEchantillonEdit").getFellow("fwinMultiEchantillons")
         .getAttributeOrFellow("fwinMultiEchantillons$composer", true));
   }

   public boolean hasMultiFicheEdit(){
      return self.getFellow("divEchantillonEdit").getFellowIfAny("fwinMultiEchantillons") != null;
   }

   @Override
   public ListeEchantillon getListe(){
      if(SEROLOGIE.equals(getCurrentContexte())){
         return ((ListeEchantillonSero) self.getFellow("lwinEchantillonSero").getAttributeOrFellow("lwinEchantillonSero$composer",
            true));
      }
      return ((ListeEchantillon) self.getFellow("lwinEchantillon").getAttributeOrFellow("lwinEchantillon$composer", true));
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return ((FicheModifMultiEchantillon) self.getFellow("modifMultiDiv").getFellow("fwinModifMultiEchantillon")
         .getAttributeOrFellow("fwinModifMultiEchantillon$composer", true));
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      if(self.getFellowIfAny("ficheAnnoEchantillon") != null){
         return ((FicheAnnotation) self.getFellow("ficheAnnoEchantillon").getFellow("fwinAnnotation")
            .getAttributeOrFellow("fwinAnnotation$composer", true));
      }
      return null;
   }

   @Override
   public void initLinkedControllers(){

      // prelevement
      if(!getMainWindow().isFullfilledComponent("prelevementPanel", "winPrelevement")){
         getMainWindow().createMacroComponent("/zuls/prelevement/Prelevement.zul", "winPrelevement",
            (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("prelevementPanel"));
      }

      final PrelevementController prelController = (PrelevementController) getMainWindow().getMainTabbox().getTabpanels()
         .getFellow("prelevementPanel").getFellow("winPrelevement").getAttributeOrFellow("winPrelevement$composer", true);

      getReferencingObjectControllers().add(prelController);

      if(!prelController.getReferencedObjectsControllers().contains(this)){
         prelController.getReferencedObjectsControllers().add(0, this);
      }

      // derive
      if(getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")){

         final ProdDeriveController deriveController = (ProdDeriveController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("derivePanel").getFellow("winProdDerive").getAttributeOrFellow("winProdDerive$composer", true);

         getReferencedObjectsControllers().add(deriveController);

         if(!deriveController.getReferencingObjectControllers().contains(this)){
            deriveController.getReferencingObjectControllers().add(this);
         }
      }

      // cession
      if(getMainWindow().isFullfilledComponent("cessionPanel", "winCession")){

         final CessionController cessionController = (CessionController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("cessionPanel").getFellow("winCession").getAttributeOrFellow("winCession$composer", true);

         getReferencedObjectsControllers().add(cessionController);

         if(!cessionController.getReferencedObjectsControllers().contains(this)){
            cessionController.getReferencedObjectsControllers().add(this);
         }
      }
   }

   /**
    * Selectionne le tab et renvoie le controller Echantillon.
    *
    * @param page
    * @return controller tab
    */
   public static AbstractObjectTabController backToMe(final MainWindow window, final Page page){

      EchantillonController tabController = null;

      // on récupère les panels
      final Tabbox panels = (Tabbox) page.getFellow("mainWin").getFellow("main").getFellow("mainTabbox");

      if(panels != null){
         // on récupère le panel de l'entite
         final Tabpanel panel = (Tabpanel) panels.getFellow("echantillonPanel");

         window.createMacroComponent("/zuls/echantillon/Echantillon.zul", "winEchantillon", panel);

         tabController =
            ((EchantillonController) panel.getFellow("winEchantillon").getAttributeOrFellow("winEchantillon$composer", true));

         panels.setSelectedPanel(panel);
      }

      return tabController;
   }

   /**
    * Dessine le composant représentant l'affiche statique des codes assignes
    * dans la fiche.
    *
    * @param echan dont on veut dessiner les codes
    * @param parent      dans lequel dessiner
    * @param isOrgane    dessine les codes organes, les codes morpho sinon.
    * @param orientV de la liste verticale si true
    */
   public static void drawCodesAssignes(final Echantillon echan, final Component parent, final boolean isOrgane,
      final boolean orientV){

      //Codes assignes
      List<CodeAssigne> codes;
      CodeAssigne codeToExport = null;

      Box codeAssBox;
      if(orientV){
         codeAssBox = new Vbox();
      }else{
         codeAssBox = new Hbox();
         codeAssBox.setSpacing("5px");
      }

      // nettoie la div
      Components.removeAllChildren(parent);
      codeAssBox.setParent(parent);

      if(isOrgane){
         codes = ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echan);
         //codeToExport = echan.getCodeOrganeExport();
      }else{
         codes = ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echan);
         //codeToExport = echan.getCodeLesExport();
      }

      for(int i = 0; i < codes.size(); i++){
         if(codes.get(i).getExport()){
            codeToExport = codes.get(i);
            break;
         }
      }

      final Iterator<CodeAssigne> it = codes.iterator();
      CodeAssigne next;
      String label = null;
      Label codeStaticLabel = null;
      while(it.hasNext()){
         next = it.next();
         if(isOrgane){
            if(next.getLibelle() != null){
               label = next.getLibelle() + " [" + next.getCode() + "]";
            }else{
               label = next.getCode();
            }
         }else{
            label = next.getCode();
            if(next.getLibelle() != null){
               label = label + " " + next.getLibelle();
            }
         }
         codeStaticLabel = new Label(label);
         // modifie le style pour le code exporté.
         if(next.equals(codeToExport) && codes.size() > 0){
            codeStaticLabel.setStyle("font-style: italic; font-weight: bold");
         }
         codeStaticLabel.setSclass("formValue");

         codeAssBox.appendChild(codeStaticLabel);
         if(it.hasNext() && !orientV){
            codeAssBox.appendChild(new Label(" - "));
         }
      }
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   public boolean getFromFichePrelevement(){
      return fromFichePrelevement;
   }

   public void setFromFichePrelevement(final boolean fP){
      this.fromFichePrelevement = fP;
   }

   @Override
   public void switchToCreateMode(final TKdataObject parent){

      // si il y eu edit avant et addNew depuis liste
      clearEditDiv();

      Executions.createComponents(createZulPath, getEditDiv(), null);
      getMultiFicheEdit().setObjectTabController(this);
      getMultiFicheEdit().setNewObject();
      getMultiFicheEdit().switchToCreateMode((Prelevement) parent);

      getFicheAnnotation().switchToStaticOrEditMode(false, false);

      if(!annoRegion.isOpen() && annoRegion.isVisible()){
         annoRegion.setOpen(true);
      }

      getListeRegion().setOpen(false);
      if(getListe() != null){
         getListe().switchToEditMode(true);
      }

      showStatic(false);
   }

   //	public void populateFicheEdit(){
   //		if(isStaticEditMode()){
   //			clearEditDiv();
   //			Executions.createComponents(createZulPath, getEditDiv(), null);
   //		}
   //	}

   public void switchToPrelevementEditMode(final Object obj){

      // si il y eu edit avant et addNew depuis liste
      clearEditDiv();

      Executions.createComponents(createZulPath, divEchantillonEdit, null);
      getMultiFicheEdit().setObjectTabController(this);
      getMultiFicheEdit().setNewObject();
      getMultiFicheEdit().switchToEditMode((Prelevement) obj);

      getFicheAnnotation().switchToStaticOrEditMode(false, false);
      //getFicheAnnotation().showButtonsBar(false);

      if(!annoRegion.isOpen() && annoRegion.isVisible()){
         annoRegion.setOpen(true);
      }

      showStatic(false);

      getListeRegion().setOpen(false);
      if(getListe() != null){
         getListe().switchToEditMode(true);
      }
   }

   @Override
   public List<? extends Object> getChildrenObjects(final TKdataObject obj){
      final List<TKAnnotableObject> childrens = new ArrayList<>();
      childrens.addAll(ManagerLocator.getProdDeriveManager().findByParentManager((Echantillon) obj, true));
      final Iterator<CederObjet> cedIt = ManagerLocator.getCederObjetManager().findByObjetManager(obj).iterator();
      while(cedIt.hasNext()){
         childrens.add(cedIt.next().getCession());
      }
      return childrens;
   }

   @Override
   public Map<Entite, List<Integer>> getChildrenObjectsIds(final List<Integer> ids){
      final Map<Entite, List<Integer>> children = new HashMap<>();

      // derives
      final Entite deriveEntite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      children.put(deriveEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         deriveEntite, SessionUtils.getSelectedBanques(sessionScope), true));

      // cession
      final Entite cessionEntite = ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0);

      children.put(cessionEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         cessionEntite, SessionUtils.getSelectedBanques(sessionScope), false));

      return children;
   }

   @Override
   public Map<Entite, List<Integer>> getParentsObjectsIds(final List<Integer> ids){
      final Map<Entite, List<Integer>> parents = new HashMap<>();

      // prelevement
      final Entite prelEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      parents.put(prelEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         prelEntite, SessionUtils.getSelectedBanques(sessionScope), true));

      return parents;
   }

   /**
    * Gère un scan full-rack barcode 2D au niveau de l'onglet echantillon:
    * - si mode liste: affichage contenu
    * - si mode creation: application code 2D liste de travail
    *
    * @param sT
    * @since 2.1
    */
   @Override
   public void handleScanTerminale(final ScanTerminale sT){
      if(sT != null){
         if(getStaticDiv().isVisible()){
            getListe().displayTKObjectsFromCodes(ManagerLocator.getScanTerminaleManager().findTKObjectCodesManager(sT), sT);
         }else if(getEditDiv().isVisible() && hasMultiFicheEdit()){
            getMultiFicheEdit().applyTKObjectsCodesFromScan(sT);
         }
      }
   }
}
