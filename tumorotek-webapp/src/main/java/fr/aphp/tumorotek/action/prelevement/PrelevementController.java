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
package fr.aphp.tumorotek.action.prelevement;

import static fr.aphp.tumorotek.model.contexte.EContexte.SEROLOGIE;
import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentContexte;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.utils.MessagesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.echantillon.FicheMultiEchantillons;
import fr.aphp.tumorotek.action.patient.MaladieConstraints;
import fr.aphp.tumorotek.action.patient.PatientConstraints;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.serotk.FichePrelevementEditSero;
import fr.aphp.tumorotek.action.prelevement.serotk.FichePrelevementStaticSero;
import fr.aphp.tumorotek.action.prelevement.serotk.ListePrelevementSero;
import fr.aphp.tumorotek.action.prelevement.serotk.PrelevementSeroRowRenderer;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller de l'onglet prelevement.
 * Controller créé le 23/11/2009.
 *
 * @author Pierre Ventadour
 * @version 2.3.0-gatsbi
 * @since 2.0.6
 */
public class PrelevementController extends AbstractObjectTabController
{

   private final Logger log = LoggerFactory.getLogger(PrelevementController.class);

   private static final long serialVersionUID = -5315034328095152785L;

   //private Prelevement prelevement;
   private Maladie maladie;

   private Div divPrelevementStatic;

   private Div divPrelevementEdit;

   private Div divLaboInter;

   //private Div divMultiEchantillons;
   private Div modifMultiDiv;
   // private Component listePrelevement;
   // private Component listePrelevementSero;

   // flag ordonnant le retour vers la fiche patient
   // et rafraichissement et ouverture panel maladie
   private boolean fromFichePatient = false;

   private boolean fromFicheMaladie = false;

   // flag indiquant que le panel echantillon a été atteint
   private boolean nextToEchanClicked = false;

   // dossier externe provenant d'un SGL
   // private DossierExterne dossierExterne;

   private PatientSip patientSip;


   private Calendar previousPrelevementDate;


   public Calendar getPreviousPrelevementDate(){
      return previousPrelevementDate;
   }

   public void setPreviousPrelevementDate(Calendar previousPrelevementDate) {
         this.previousPrelevementDate = previousPrelevementDate;
   }


   public Maladie getMaladie(){
      return maladie;
   }

   public void setMaladie(final Maladie m){
      this.maladie = m;
   }

   public boolean getFromFichePatient(){
      return fromFichePatient;
   }

   public void setFromFichePatient(final boolean fP){
      this.fromFichePatient = fP;
   }

   public boolean getFromFicheMaladie(){
      return fromFicheMaladie;
   }

   public void setFromFicheMaladie(final boolean fM){
      this.fromFicheMaladie = fM;
   }

   public boolean getNextToEchanClicked(){
      return nextToEchanClicked;
   }

   public void setNextToEchanClicked(final boolean n){
      this.nextToEchanClicked = n;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      setEntiteTab(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0));

      super.doAfterCompose(comp);

      setStaticDiv(divPrelevementStatic);
      setEditDiv(divPrelevementEdit);
      setModifMultiDiv(modifMultiDiv);

      switch(getCurrentContexte()){
         case SEROLOGIE:
            setListZulPath("/zuls/prelevement/serotk/ListePrelevementSero.zul");
            setMultiEditZulPath("/zuls/prelevement/serotk/FicheModifMultiPrelevementSero.zul");
            break;
         default:
            if(SessionUtils.getCurrentGatsbiContexteForEntiteId(2) == null){
               setListZulPath("/zuls/prelevement/ListePrelevement.zul");
               setMultiEditZulPath("/zuls/prelevement/FicheModifMultiPrelevement.zul");
            }else{
               setListZulPath("/zuls/prelevement/gatsbi/ListePrelevementGatsbi.zul");
               setMultiEditZulPath("/zuls/prelevement/gatsbi/FicheModifMultiPrelevementGatsbi.zul");
            }
            break;
      }

      // seul le choix du composant pour edit est factorisé
      setEditZulPath(getFichePrelevementEditZulPath());

      drawListe();

      initFicheStatic();

      switchToOnlyListeMode();
      orderAnnotationDraw(false);
      
      // @since 2.3.0-gatsbi
      // force constraints reset des status nullable
      PatientConstraints.resetNullableProps();
      MaladieConstraints.resetNullableProps();
      PrelevementConstraints.resetNullableProps();
   }
   
   /**
    * Factorisation du choix du zul path, car le choix de 
    * du composant d'édition est utilisé par plusieurs 
    * fonctionalités.
    * @return FichePrelevementEdit zul path
    */
   private String getFichePrelevementEditZulPath() {
      switch(getCurrentContexte()){
         case SEROLOGIE:
            return "/zuls/prelevement/serotk/FichePrelevementEditSero.zul";
         default:
            if(SessionUtils.getCurrentGatsbiContexteForEntiteId(2) == null){
               return "/zuls/prelevement/FichePrelevementEdit.zul";
            }else{
               return "/zuls/prelevement/gatsbi/FichePrelevementEditGatsbi.zul";
            }
      }
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getPrelevementManager().findByIdManager(id);
   }

   @Override
   public void drawListe(){
      if(SEROLOGIE.equals(getCurrentContexte())){
         setListZulPath("/zuls/prelevement/serotk/ListePrelevementSero.zul");
         setListRenderer(new PrelevementSeroRowRenderer(true, SessionUtils.getSelectedBanques(sessionScope).size() > 1));
      }
      super.drawListe();
   }

   @Override
   public void populateFicheStatic(){
      if(SEROLOGIE.equals(getCurrentContexte())){
         setStaticZulPath("/zuls/prelevement/serotk/FichePrelevementStaticSero.zul");
      }else{
         if(SessionUtils.getCurrentGatsbiContexteForEntiteId(2) == null){
            setStaticZulPath("/zuls/prelevement/FichePrelevementStatic.zul");
         }else{
            setStaticZulPath("/zuls/prelevement/gatsbi/FichePrelevementStaticGatsbi.zul");
         }
      }
      super.populateFicheStatic();
   }

   @Override
   public FichePrelevementStatic getFicheStatic(){
      if(SEROLOGIE.equals(getCurrentContexte())){
         return ((FichePrelevementStaticSero) this.self.getFellow("divPrelevementStatic").getFellow("fwinPrelevementStaticSero")
            .getAttributeOrFellow("fwinPrelevementStaticSero$composer", true));
      }
      return ((FichePrelevementStatic) this.self.getFellow("divPrelevementStatic").getFellow("fwinPrelevementStatic")
         .getAttributeOrFellow("fwinPrelevementStatic$composer", true));
   }

   @Override
   public FichePrelevementEdit getFicheEdit(){
      if(SEROLOGIE.equals(getCurrentContexte())){
         return ((FichePrelevementEditSero) this.self.getFellow("divPrelevementEdit").getFellow("fwinPrelevementEditSero")
            .getAttributeOrFellow("fwinPrelevementEditSero$composer", true));
      }
      return ((FichePrelevementEdit) this.self.getFellow("divPrelevementEdit").getFellow("fwinPrelevementEdit")
         .getAttributeOrFellow("fwinPrelevementEdit$composer", true));
   }

   @Override
   public ListePrelevement getListe(){
      if(SEROLOGIE.equals(getCurrentContexte())){
         return ((ListePrelevementSero) self.getFellow("lwinPrelevementSero").getAttributeOrFellow("lwinPrelevementSero$composer",
            true));
      }
      return ((ListePrelevement) self.getFellow("lwinPrelevement").getAttributeOrFellow("lwinPrelevement$composer", true));

   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return ((FicheModifMultiPrelevement) self.getFellow("modifMultiDiv").getFellow("fwinModifMultiPrelevement")
         .getAttributeOrFellow("fwinModifMultiPrelevement$composer", true));
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      if(self.getFellowIfAny("ficheAnnoPrelevement") != null){
         return ((FicheAnnotation) self.getFellow("ficheAnnoPrelevement").getFellow("fwinAnnotation")
            .getAttributeOrFellow("fwinAnnotation$composer", true));
      }
      return null;
   }

   @Override
   public void clearEditDiv(){
      super.clearEditDiv();
      Components.removeAllChildren(divLaboInter);
   }

   @Override
   public void switchToFicheAndListeMode(){
      super.switchToFicheAndListeMode();
   }

   @Override
   public void showStatic(final boolean s){
      super.showStatic(s);
      divLaboInter.setVisible(!s);
   }

   @Override
   public void initLinkedControllers(){

      if(getMainWindow().isFullfilledComponent("patientPanel", "winPatient")){

         final PatientController patController = (PatientController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("patientPanel").getFellow("winPatient").getAttributeOrFellow("winPatient$composer", true);
         getReferencingObjectControllers().add(patController);

         if(!patController.getReferencedObjectsControllers().contains(this)){
            patController.getReferencedObjectsControllers().add(this);
         }
      }

      if(getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){

         final EchantillonController echanController = (EchantillonController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("echantillonPanel").getFellow("winEchantillon").getAttributeOrFellow("winEchantillon$composer", true);
         getReferencedObjectsControllers().add(echanController);

         if(!echanController.getReferencingObjectControllers().contains(this)){
            echanController.getReferencingObjectControllers().add(this);
         }
      }

      // derives
      if(getMainWindow().isFullfilledComponent("derivePanel", "winProdDerive")){

         final ProdDeriveController deriveController = (ProdDeriveController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("derivePanel").getFellow("winProdDerive").getAttributeOrFellow("winProdDerive$composer", true);
         getReferencedObjectsControllers().add(deriveController);

         if(!deriveController.getReferencingObjectControllers().contains(this)){
            deriveController.getReferencingObjectControllers().add(this);
         }
      }
   }

   /**
    * Recupere la liste de controllers des objets qui sont référencés
    * par l'objet avec surcharge de méthode pour créer le controller echantillon si il n'existe
    * pas.
    *
    * @param force create cree le controller Echantillon si il n'existe pas.
    * @return la liste de controllers.
    */
   public List<AbstractObjectTabController> getReferencedObjectsControllers(final boolean force){

      // echantillons
      // cree le contenu du panel au besoin
      if(force && !getMainWindow().isFullfilledComponent("echantillonPanel", "winEchantillon")){

         getMainWindow().createMacroComponent("/zuls/echantillon/Echantillon.zul", "winEchantillon",
            (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("echantillonPanel"));

         final EchantillonController echanController = (EchantillonController) getMainWindow().getMainTabbox().getTabpanels()
            .getFellow("echantillonPanel").getFellow("winEchantillon").getAttributeOrFellow("winEchantillon$composer", true);

         getReferencedObjectsControllers().add(0, echanController);

         if(!echanController.getReferencingObjectControllers().contains(this)){
            echanController.getReferencingObjectControllers().add(this);
         }

      }

      return getReferencedObjectsControllers();
   }

   /**
    * Méthode permettant de passer en mode édition des labos.
    *
    * @param prlvt Prlvt.
    */
   public void switchToLaboInterEditMode(final Prelevement prlvt){
      final Prelevement edit = prlvt;
      divPrelevementStatic.setVisible(false);
      divPrelevementEdit.setVisible(false);
      divLaboInter.setVisible(true);
      //divMultiEchantillons.setVisible(false);

      if(divLaboInter.getChildren().size() == 0){
         if(SessionUtils.getCurrentGatsbiContexteForEntiteId(2) == null){
            Executions.createComponents("/zuls/prelevement/FicheLaboInter.zul", divLaboInter, null);
         }else{
            Executions.createComponents("/zuls/prelevement/gatsbi/FicheLaboInterGatsbi.zul", divLaboInter, null);
         }
         getFicheLaboInter().setObjectTabController(this);
         getFicheLaboInter().setObject(edit);
         getFicheLaboInter().switchToEditMode();
      }

   }

   /**
    * Méthode permettant de passer en mode création des labos.
    *
    * @param prlvt Prlvt.
    * @param labos labos
    */
   public void switchToLaboInterCreateMode(final Prelevement prlvt, final List<LaboInter> labos){

      final Prelevement newPrlvt = prlvt;
      divPrelevementStatic.setVisible(false);
      divPrelevementEdit.setVisible(false);
      divLaboInter.setVisible(true);
      //divMultiEchantillons.setVisible(false);

      if(divLaboInter.getChildren().size() == 0){
         if(SessionUtils.getCurrentGatsbiContexteForEntiteId(2) == null){
            Executions.createComponents("/zuls/prelevement/FicheLaboInter.zul", divLaboInter, null);
         }else{
            Executions.createComponents("/zuls/prelevement/gatsbi/FicheLaboInterGatsbi.zul", divLaboInter, null);
         }
         getFicheLaboInter().setObjectTabController(this);
         getFicheLaboInter().setObject(newPrlvt);
         getFicheLaboInter().setOldLaboInters(labos);
         getFicheLaboInter().switchToCreateMode();
      }
   }

   /**
    * Méthode permettant de passer en mode édition des échantillons
    * d'un prélèvement.
    *
    * @param prlvt Prlvt.
    */
   public void switchToMultiEchantillonsEditMode(final Prelevement prlvt, final List<LaboInter> labos,
      final List<LaboInter> labosToDelete){
      //divPrelevementStatic.setVisible(false);
      //divPrelevementEdit.setVisible(false);
      divLaboInter.setVisible(true);

      // enregistre le flag lors du premier acces a l'echantillon.
      if(!nextToEchanClicked){
         ((EchantillonController) getReferencedObjectsControllers(true).get(0)).switchToPrelevementEditMode(prlvt);
         getFicheMultiEchantillons().setPrelevementProcedure(true);
         getFicheMultiEchantillons().setLaboInters(labos);
         getFicheMultiEchantillons().setLaboIntersToDelete(labosToDelete);
         nextToEchanClicked = true;
      }

      // change d'onglet
      EchantillonController.backToMe(getMainWindow(), page);
   }

   /**
    * Méthode permettant de passer en mode création des échantillons
    * d'un prélèvement.
    *
    * @param prlvt Prlvt.
    */
   public void switchToMultiEchantillonsCreateMode(final Prelevement prlvt, final List<LaboInter> labos){

      divLaboInter.setVisible(true);

      // enregistre le flag lors du premier acces a l'echantillon.
      if(!nextToEchanClicked){
         ((EchantillonController) getReferencedObjectsControllers(true).get(0)).switchToCreateMode(prlvt);
         getFicheMultiEchantillons().setPrelevementProcedure(true);
         nextToEchanClicked = true;
      }else{
         if(((EchantillonController) getReferencedObjectsControllers(true).get(0)).hasMultiFicheEdit()){
            getFicheMultiEchantillons().setParentObject(prlvt);
         }else{
            ((EchantillonController) getReferencedObjectsControllers(true).get(0)).switchToCreateMode(prlvt);
            getFicheMultiEchantillons().setPrelevementProcedure(true);
            nextToEchanClicked = true;
         }
      }
      getFicheMultiEchantillons().setLaboInters(labos);

      // change d'onglet
      EchantillonController.backToMe(getMainWindow(), page);
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le bouton
    * "précédent" quand il est sur la page FicheLaboInter.
    */
   public void switchFromLaboToPrelevement(){
      divPrelevementStatic.setVisible(false);
      divPrelevementEdit.setVisible(true);
      divLaboInter.setVisible(false);
      //divMultiEchantillons.setVisible(false);
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le bouton
    * "précédent" quand il est sur la page FicheMultiEchantillons.
    */
   public void switchFromEchantillonsToLabo(){
      divPrelevementStatic.setVisible(false);
      divPrelevementEdit.setVisible(false);
      divLaboInter.setVisible(true);
      //divMultiEchantillons.setVisible(false);
      // change d'onglet
      PrelevementController.backToMe(getMainWindow(), page);
   }

   /**
    * Recupere le controller du composant representant la fiche associee
    * a l'entite de domaine a partir de l'evenement.
    *
    * @return fiche FicheLaboInter
    */
   public FicheLaboInter getFicheLaboInter(){
      return ((FicheLaboInter) this.self.getFellow("divLaboInter").getFellow("fwinLaboInter")
         .getAttributeOrFellow("fwinLaboInter$composer", true));
   }

   public boolean hasFicheLaboInter(){
      if(this.self.getFellow("divLaboInter").getFellowIfAny("fwinLaboInter") != null){
         return true;
      }
      return false;
   }

   public boolean isFicheLaboOpened(){
      return this.self.getFellow("divLaboInter").getFirstChild() != null;
   }

   /**
    * Recupere le controller du composant representant la fiche associee
    * a l'entite de domaine a partir de l'evenement.
    *
    * @return fiche FicheMultiEchantillons
    */
   public FicheMultiEchantillons getFicheMultiEchantillons(){
      return ((EchantillonController) getReferencedObjectsControllers(true).get(0)).getMultiFicheEdit();
   }

   /**
    * Selectionne le tab et renvoie le controller Prelevement.
    *
    * @param page
    * @return controller tab
    */
   public static AbstractObjectTabController backToMe(final MainWindow window, final Page page){

      PrelevementController tabController = null;

      // on récupère les panels
      final Tabbox panels = (Tabbox) page.getFellow("mainWin").getFellow("main").getFellow("mainTabbox");

      if(panels != null){
         // on récupère le panel de l'entite
         final Tabpanel panel = (Tabpanel) panels.getFellow("prelevementPanel");

         window.createMacroComponent("/zuls/prelevement/Prelevement.zul", "winPrelevement", panel);

         tabController =
            ((PrelevementController) panel.getFellow("winPrelevement").getAttributeOrFellow("winPrelevement$composer", true));

         panels.setSelectedPanel(panel);
      }

      return tabController;
   }

   @Override
   public void onCreateDone(){
      // create done hors fiche echantillon atteinte pourtant
      if(nextToEchanClicked){
         ((EchantillonController) getReferencedObjectsControllers(true).get(0)).onCancel();
         nextToEchanClicked = false;
      }
      super.onCreateDone();
   }

   @Override
   public void onEditDone(final TKdataObject obj){
      // update done hors fiche echantillon atteinte pourtant
      if(nextToEchanClicked){
         ((EchantillonController) getReferencedObjectsControllers(true).get(0)).onCancel();
         nextToEchanClicked = false;
      }
      super.onEditDone(obj);
   }

   @Override
   public AbstractFicheCombineController getFicheCombine(){
      return null;
   }

   /**
    * Méthode permettant de passer en mode "création", collapse la liste
    * et dessine la fiche édition/creation avec un objet vide.
    */
   @Override
   public void switchToCreateMode(final TKdataObject parent){
      super.switchToCreateMode(parent);

      if(nextToEchanClicked){
         (getReferencedObjectsControllers(true).get(0)).onCancel();
         nextToEchanClicked = false;
      }

      // on efface le dossier externe
      // setDossierExterne(null);
      SessionUtils.setDossierExterneInjection(sessionScope, null);
   }

   @Override
   public void onCancel(){
      if(nextToEchanClicked){
         (getReferencedObjectsControllers(true).get(0)).onCancel();
         nextToEchanClicked = false;
      }
      super.onCancel();
   }

   @Override
   public void onRevert(){
      // cancel done hors fiche echantillon atteinte pourtant
      if(nextToEchanClicked){
         ((EchantillonController) getReferencedObjectsControllers(true).get(0)).onCancel();
         nextToEchanClicked = false;
      }
      super.onRevert();
   }

   /**
    * @version 2.3.0-gatsbi
    * @param prlvt
    */
   public void createAnotherPrelevement(final Prelevement prlvt){
      if(Messagebox.show(Labels.getLabel("fichePrelevement.create.another.prelevement"),
            Labels.getLabel("message.new.prelevement.title"), Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION) == Messagebox.YES){
         
         
         backToMe(getMainWindow(), page);
         
         Contexte contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);

         
         // @since 2.3.0-gatsbi
         if (contexte != null) {
         
            GatsbiController.addNewObjectForContext(contexte, 
                              getListe().getSelfComponent(), e -> {
               try{
                  swithToCreatedModeFromCopy(prlvt);
               }catch(final Exception ex){
                  Messagebox.show(handleExceptionMessage(ex), "Error", Messagebox.OK, Messagebox.ERROR);
               }
            }, null, prlvt); 
          // prlvt utilisé comme 'parentObject' pour être renvoyé à ListePrelevement 
          // et ainsi commander l'appel de swithToCreatedModeFromCopy 
         } else {
            swithToCreatedModeFromCopy(prlvt);
         }
      }
   }
   
   /**
    * Ouvre le formulaire de création prélèvement à partir d'informations 
    * prélèvement précédemment créé (notamment patient/maladie).
    * @since 2.3.0-gatsbi
    * @version 2.3.0-gatsbi
    * @param prlvt
    * 
    */
   public void swithToCreatedModeFromCopy(Prelevement prlvt) { 
      // si il y eu edit avant et addNew depuis liste
      clearEditDiv();
      Executions.createComponents(getFichePrelevementEditZulPath(), divPrelevementEdit, null);

      getFicheEdit().setObjectTabController(this);
      getFicheEdit().setNewObjectByCopy(prlvt);
      getFicheEdit().setParentObject(prlvt.getMaladie());
      getFicheEdit().switchToCreateMode();
      getFicheEdit().initSelectedInLists();

      if(canUpdateAnnotation()){
         getFicheAnnotation().switchToStaticOrEditMode(false, false);
      }

      if(!annoRegion.isOpen() && annoRegion.isVisible()){
         annoRegion.setOpen(true);
      }

      getListeRegion().setOpen(false);

      showStatic(false);
   }

   /**
    * On est dans le cas où l'utilisateur a modifié le code de l'entité. Une fenêtre sera affichée pour lui proposer :
    *  - soit de mettre automatiquement à jour les codes des "enfants et petits enfants" (échantillons ou dérivés)
    *  - soit d'afficher ses échantillons / produits dérivés afin de mettre à jour leurs codes manuellement.
    *
    * @param prlvt
    */
   public void showEchantillonsAfterUpdate(final Prelevement prlvt){
      if(isCodeUpdated() && getOldCode() != null) {
         final List<Echantillon> echans = new ArrayList<>(ManagerLocator.getPrelevementManager().getEchantillonsManager(prlvt));
         final List<ProdDerive> prodDerives = ManagerLocator.getPrelevementManager().getProdDerivesManager(prlvt);
         // si le code a été mis à jour et que le prlvt a des
         // échantillons
         if(echans.size() > 0 || prodDerives.size() > 0){
            openAfterUpdateCodeModaleWindow(echans, prodDerives, getOldCode(), prlvt.getCode());
         }
      }
   }
   
   public PatientSip getPatientSip(){
      return patientSip;
   }

   public void setPatientSip(final PatientSip patientSip){
      this.patientSip = patientSip;
   }

   public void removePatientSip(){
      if(patientSip != null && patientSip.getPatientSipId() != null){
         ManagerLocator.getPatientSipManager().removeObjectManager(patientSip);

         setPatientSip(null);
      }
   }

   @Override
   public List<? extends Object> getChildrenObjects(final TKdataObject obj){
      final List<TKAnnotableObject> childrens = new ArrayList<>();

      final Set<Echantillon> echans = ManagerLocator.getPrelevementManager().getEchantillonsManager((Prelevement) obj);
      childrens.addAll(echans);
      for(final Echantillon echantillon : echans){
         childrens.addAll(ManagerLocator.getProdDeriveManager().findByParentManager(echantillon, true));
      }
      childrens.addAll(ManagerLocator.getProdDeriveManager().findByParentManager((Prelevement) obj, true));
      return childrens;
   }

   @Override
   public Map<Entite, List<Integer>> getChildrenObjectsIds(final List<Integer> ids){
      final Map<Entite, List<Integer>> children = new HashMap<>();

      // echantillo,s
      final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      children.put(echanEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         echanEntite, SessionUtils.getSelectedBanques(sessionScope), false));

      // derives
      final Entite deriveEntite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      children.put(deriveEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         deriveEntite, SessionUtils.getSelectedBanques(sessionScope), true));

      return children;
   }

   @Override
   public Map<Entite, List<Integer>> getParentsObjectsIds(final List<Integer> ids){
      final Map<Entite, List<Integer>> parents = new HashMap<>();

      // patient
      final Entite patEntite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      parents.put(patEntite, ManagerLocator.getCorrespondanceIdManager().findTargetIdsFromIdsManager(ids, getEntiteTab(),
         patEntite, SessionUtils.getSelectedBanques(sessionScope), true));

      return parents;
   }

   /**
    * Met à jour le délai de congélation pour un prélèvement, en vérifiant s'il existe des échantillons
    * avec des délais non calculés, avec interaction utilisateur le cas échéant.
    *
    * @param prelevement Le prélèvement à gérer.
    */
   public void miseAJourDelaiCongelation(Prelevement prelevement) {
      // Vérifie s'il existe au moins un échantillon avec un délai non calculé
      boolean hasEchantillonWithNonCalculatedDelai = ManagerLocator.getEchantillonManager()
         .hasEchantillonWithNonCalculatedDelai(prelevement, previousPrelevementDate);

      // S'il y en a, ouvre la boîte de dialogue
      if (hasEchantillonWithNonCalculatedDelai) {
         String title = Labels.getLabel("message.title.maj.delaicongelation");
         String message = Labels.getLabel("message.question.maj.delaicongelation");

         // Ouvre la fenêtre modale et récupère la réponse de l'utilisateur
         boolean isUserAccepted = MessagesUtils.openQuestionModal(title, message);

         // Si l'utilisateur clique sur "Oui", met à jour tous les échantillons
         if (isUserAccepted) {
            ManagerLocator.getEchantillonManager().updateDelaiCongelation(prelevement);
         }
      } else {
         // Met à jour uniquement les échantillons dont le délai n'est pas saisi manuellement
         List<Echantillon> echantillonsWithCalculatedDelai = ManagerLocator.getEchantillonManager()
            .findEchantillonWithCalculatedDelai(prelevement, previousPrelevementDate);

         ManagerLocator.getEchantillonManager().updateDelaiCongelation(echantillonsWithCalculatedDelai, prelevement);
      }
   }

}
