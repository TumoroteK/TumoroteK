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
package fr.aphp.tumorotek.action.prelevement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Box;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.echantillon.EchantillonRowRenderer;
import fr.aphp.tumorotek.action.patient.ResumePatient;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveRowRenderer;
import fr.aphp.tumorotek.decorator.LaboInterDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche static d'un prélèvement.
 * Controller créé le 23/06/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FichePrelevementStatic extends AbstractFicheStaticController
{

   private final Log log = LogFactory.getLog(FichePrelevementStatic.class);

   private static final long serialVersionUID = -7612780578022559022L;

   protected Menuitem addDerive;
   protected Menuitem addEchan;
   protected Menuitem changeCollection;
   protected Menuitem changeMaladie;
   protected Grid echantillonsGrid;
   protected Grid prodDerivesGrid;
   protected Grid laboIntersGrid;
   protected Grid gridFormPrlvtComp;
   // Groups
   protected Group groupPatient;
   protected Group groupLaboInter;
   protected Group groupEchans;
   protected Group groupDerivesPrlvt;

   private ResumePatient resumePatient;

   protected Vbox risquesBox;

   protected Label congDepartLabel;
   protected Div congDepartImg;
   protected Label congArriveeLabel;
   protected Div congArriveeImg;

   private Image imgDossierInbox;
   private Menuitem importDossier;

   /**
    *  Objets Principaux.
    */
   protected Prelevement prelevement = new Prelevement();
   protected Maladie maladie;
   protected Patient patient;
   protected List<LaboInterDecorator> laboInters = new ArrayList<>();
   protected List<Echantillon> echantillons = new ArrayList<>();
   protected List<ProdDerive> derives = new ArrayList<>();

   protected String valeurQuantite = "";
   protected String echantillonsGroupHeader;
   protected String prodDerivesGroupHeader;
   protected ProdDeriveRowRenderer prodDeriveRenderer = new ProdDeriveRowRenderer(false, false);
   protected EchantillonRowRenderer echantillonRenderer = new EchantillonRowRenderer(false, false);

   protected boolean isPatientAccessible = true;
   protected boolean canChangeCollection = false;
   protected boolean canChangeMaladie = false;

   protected boolean canAccessEchantillons = true;
   protected boolean canAccessDerives = true;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.prelevement");
      setFantomable(true);

      echantillonsGrid.setVisible(false);
      prodDerivesGrid.setVisible(false);
      addDerive.setDisabled(true);

      groupLaboInter.setOpen(false);
      groupEchans.setOpen(false);
      groupDerivesPrlvt.setOpen(false);

      echantillonRenderer.setEmbedded(true);
      echantillonRenderer.setTtesCollections(getTtesCollections());

      prodDeriveRenderer.setEmbedded(true);
      prodDeriveRenderer.setTtesCollections(getTtesCollections());

      setImportDossierVisible();

      resumePatient = new ResumePatient(groupPatient);
   }

   @Override
   public PrelevementController getObjectTabController(){
      return (PrelevementController) super.getObjectTabController();
   }

   @Override
   public void setObject(final TKdataObject p){
      this.prelevement = (Prelevement) p;
      this.maladie = this.prelevement.getMaladie();

      initAssociations();

      // Initialisation du nombre d'échantillons à afficher sur la page
      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("fichePrelevement.group.echantillons"));
      sb.append(" (");
      sb.append(echantillons.size());
      sb.append(")");
      echantillonsGroupHeader = sb.toString();

      // Initialisation du nombre de dérivés à afficher sur la page
      sb = new StringBuffer();
      sb.append(Labels.getLabel("fichePrelevement.group.prodDerives"));
      sb.append(" (");
      sb.append(derives.size());
      sb.append(")");
      prodDerivesGroupHeader = sb.toString();

      initQuantiteAndVolume();

      laboIntersGrid.setVisible(laboInters.size() > 0);
      echantillonsGrid.setVisible(echantillons.size() > 0);
      prodDerivesGrid.setVisible(derives.size() > 0);

      gridFormPrlvtComp.setVisible(true);
      // grise le libelle car pas de reference vers patient/maladie
      if(this.maladie != null || this.prelevement.equals(new Prelevement())){
         this.groupPatient.setClass("z-group");
      }else{
         this.groupPatient.setClass("z-group-dsd");
      }

      // dessine le resume si la maladie est non nulle
      if(maladie != null){
         resumePatient.setVisible(true);
         resumePatient.setAnonyme(isAnonyme());
         resumePatient.setMaladie(maladie);
         resumePatient.setPrelevement(prelevement);
         resumePatient.setPatientAccessible(isPatientAccessible);
         resumePatient.hideMaladieRows(SessionUtils.isAnyDefMaladieInBanques(SessionUtils.getSelectedBanques(sessionScope)));
      }else if(this.prelevement.getPrelevementId() != null){
         resumePatient.setVisible(false);
      }

      drawRisquesFormatted();

      // congelation
      if(prelevement.getCongArrivee() != null && prelevement.getCongArrivee()){
         congArriveeLabel.setValue(Labels.getLabel("Champ.Prelevement.CongArrivee"));
         congArriveeImg.setVisible(true);
      }else{
         congArriveeLabel.setValue(null);
         congArriveeImg.setVisible(false);
      }
      if(prelevement.getCongDepart() != null && prelevement.getCongDepart()){
         congDepartLabel.setValue(Labels.getLabel("Champ.Prelevement.CongDepart"));
         congDepartImg.setVisible(true);
      }else{
         congDepartLabel.setValue(null);
         congDepartImg.setVisible(false);
      }

      // chg collection disabled si cession
      if(canChangeCollection){
         changeCollection.setDisabled(prelevement.getPrelevementId() == null);
         //				|| ManagerLocator.getPrelevementManager()
         //					.hasCessedObjectManager(prelevement));
      }

      // chg de maladie si +ieurs maladies
      if(canChangeMaladie){
         if(prelevement.getPrelevementId() == null){
            changeMaladie.setDisabled(true);
         }else if(maladie == null){
            changeMaladie.setDisabled(true);
         }else{
            final List<Maladie> mals = ManagerLocator.getMaladieManager().findByPatientManager(maladie.getPatient());
            if(mals.size() > 1){
               changeMaladie.setDisabled(false);
            }else{
               changeMaladie.setDisabled(true);
            }
         }
      }

      // dessinne l'image de dossier inbox
      setDossierInboxVisible();

      // annotations
      super.setObject(prelevement);

      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       * Il pourrait y avoir optimisation ICI car le bloc inline est redessiné à chaque fois
       * qu'un nouvel objet est affiché.
       * A priori, il ne serait utile de re-dessiner que si la collection change!
       * Cette optimisation est valable pour FicheAnnotation également.
       */
      final FicheAnnotation inline = getFicheAnnotationInline();
      if(inline != null){ // re-dessine le bloc inline annotation
         inline.setObj((TKAnnotableObject) p);
      }
   }

   @Override
   public TKdataObject getParentObject(){
      if(this.maladie != null){
         return this.maladie.getPatient();
      }
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){
      this.maladie = (Maladie) obj;
   }

   @Override
   public void disableToolBar(final boolean b){
      super.disableToolBar(b);
      addDerive.setDisabled(b || !isCanCreateDerive());
      addEchan.setDisabled(b || !isCanCreateEchan());
   }

   /**
    * Méthode initialisant les objets associés.
    */
   public void initAssociations(){
      laboInters.clear();
      echantillons.clear();
      derives.clear();
      // Init des labos inters
      if(this.prelevement.getPrelevementId() != null){
         final Iterator<LaboInter> labsIt =
            ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(this.prelevement).iterator();
         while(labsIt.hasNext()){
            laboInters.add(new LaboInterDecorator(labsIt.next()));
         }

         // Init des échantillons
         final Set<Echantillon> echans = ManagerLocator.getPrelevementManager().getEchantillonsManager(prelevement);
         final Iterator<Echantillon> itEchan = echans.iterator();
         echantillons = new ArrayList<>();
         if(canAccessEchantillons){
            while(itEchan.hasNext()){
               final Echantillon tmp = itEchan.next();
               echantillons.add(tmp);
            }
         }

         // Init des dérivés
         derives = new ArrayList<>();
         if(canAccessDerives){
            derives = ManagerLocator.getPrelevementManager().getProdDerivesManager(prelevement);
         }
      }
   }

   @Override
   public void setNewObject(){
      setObject(new Prelevement());

      // disable edit et delete
      super.setNewObject();
      // disable autres buttons
      addDerive.setDisabled(true);
   }

   @Override
   public Prelevement getObject(){
      return this.prelevement;
   }

   @Override
   public void prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getPrelevementManager().isUsedObjectManager(getObject());
      setDeletable(true);
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(isUsed){
         setDeleteMessage(Labels.getLabel("prelevement.deletion.isUsedCascade"));
      }
      setCascadable(isUsed);
   }

   @Override
   public void removeObject(final String comments){
      final List<File> filesToDelete = new ArrayList<>();
      if(!isCascadable()){
         ManagerLocator.getPrelevementManager().removeObjectManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope), filesToDelete);
      }else{
         // ajoute labos inters pour cascade remove
         getObject()
            .setLaboInters(new HashSet<>(ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(this.prelevement)));
         ManagerLocator.getPrelevementManager().removeObjectCascadeManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope), filesToDelete);
      }
      for(final File f : filesToDelete){
         f.delete();
      }
   }

   /**
    * Entraine l'affichage du résumé Patient-maladie (si true). 
    */
   public boolean getIsMaladieNotNull(){
      return this.maladie != null;
   }

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/

   /**
    * Méthode initialisant les champs de formulaire pour la quantité et
    * le volume.
    */
   public void initQuantiteAndVolume(){
      final StringBuffer sb = new StringBuffer();
      if(this.prelevement.getQuantite() != null){
         sb.append(this.prelevement.getQuantite());
      }else{
         sb.append("-");
      }
      if(this.prelevement.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.prelevement.getQuantiteUnite().getNom());
      }
      valeurQuantite = sb.toString();
   }

   /**
    * Formate la date de prélèvement.
    * @return Date de stockage formatée.
    */
   public String getDatePrelevementFormated(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDatePrelevement());
      }
      return null;
   }

   /**
    * Formate la date de consentement.
    * @return Date de stockage formatée.
    */
   public String getDateConsentementFormated(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.dateRenderer2(this.prelevement.getConsentDate());
      }
      return null;
   }

   /**
    * Formate la date de départ.
    * @return Date de stockage formatée.
    */
   public String getDateDepartFormated(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDateDepart());
      }
      return null;
   }

   /**
    * Formate la valeur du champ stérile.
    * @return Oui ou non.
    */
   public String getSterileFormated(){

      if(this.prelevement != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.prelevement.getSterile());
      }
      return null;
   }

   /**
    * Formate la valeur du champ conformeArrivee.
    * @return Oui ou non.
    */
   public String getConformeArriveeFormated(){
      final StringBuffer sb = new StringBuffer();
      if(this.prelevement != null){
         if(this.prelevement.getConformeArrivee() != null){
            sb.append(ObjectTypesFormatters.booleanLitteralFormatter(this.prelevement.getConformeArrivee()));

            if(this.prelevement.getConformeArrivee() != null && !this.prelevement.getConformeArrivee()){
               sb.append(" - ");
               final List<ObjetNonConforme> list =
                  ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(prelevement, "Arrivee");

               if(list.size() > 0){
                  for(int i = 0; i < list.size(); i++){
                     sb.append(list.get(i).getNonConformite().getNom());
                     if(i < list.size() - 1){
                        sb.append(", ");
                     }else{
                        sb.append(".");
                     }
                  }
               }else{
                  sb.append(Labels.getLabel("nonConformite.raison.inconnue"));
               }
            }
         }else{
            sb.append("-");
         }
      }
      return sb.toString();
   }

   public String getSClassPreleveur(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.prelevement.getPreleveur());
      }
      return null;
   }

   public String getSClassOperateur(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.prelevement.getOperateur());
      }
      return null;
   }

   public String getSClassService(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.sClassService(this.prelevement.getServicePreleveur());
      }
      return null;
   }

   public String getEtablissementFormated(){
      final StringBuffer sb = new StringBuffer();
      Etablissement etab = null;
      if(this.prelevement != null && this.prelevement.getServicePreleveur() != null){
         etab = this.prelevement.getServicePreleveur().getEtablissement();
      }

      if(etab != null){
         if(etab.getNom() != null){
            sb.append(etab.getNom());
         }
         if(etab.getCoordonnee() != null && etab.getCoordonnee().getPays() != null && !etab.getCoordonnee().getPays().equals("")){
            sb.append(" (");
            sb.append(etab.getCoordonnee().getPays());
            sb.append(")");
         }
      }

      return sb.toString();
   }

   /**
    * Formate la date d'arrivée.
    * @return Date de stockage formatée.
    */
   public String getDateArriveeFormated(){
      if(this.prelevement != null){
         return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDateArrivee());
      }
      return null;
   }

   private void drawRisquesFormatted(){
      Components.removeAllChildren(risquesBox);
      if(getObject().getPrelevementId() != null){
         final Iterator<Risque> risksIt = ManagerLocator.getPrelevementManager().getRisquesManager(getObject()).iterator();
         Risque rs;
         Label lab;
         while(risksIt.hasNext()){
            rs = risksIt.next();
            lab = new Label(rs.getNom());
            if(!rs.getInfectieux()){
               lab.setSclass("formValue");
               risquesBox.appendChild(lab);
            }else{
               final Box box = new Box();
               box.setOrient("horizontal");
               //box.setValign("middle");
               box.setPack("center");
               lab.setSclass("formValueEmph");
               box.appendChild(lab);
               final Div div = new Div();
               div.setWidth("18px");
               div.setHeight("18px");
               div.setSclass("biohazard");
               box.appendChild(div);
               risquesBox.appendChild(box);
            }

         }
      }
   }

   /*************************************************************************/
   /************************** GROUPS ***************************************/
   /*************************************************************************/

   /**
    * Indique si la liste contient plus d'un échantillon.
    */
   public boolean getEchantillonsListSizeSupOne(){
      if(getDroitsConsultation().containsKey("Echantillon") && getDroitsConsultation().get("Echantillon")){
         return this.echantillons.size() > 1;
      }
      return false;
   }

   /**
    * Indique si la liste contient plus d'un dérivé.
    */
   public boolean getDerivesListSizeSupOne(){
      if(getDroitsConsultation().containsKey("ProdDerive") && getDroitsConsultation().get("ProdDerive")){
         return this.derives.size() > 1;
      }
      return false;
   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupLaboInter.
    */
   public void onOpen$groupLaboInter(){
      final String idGrid = gridFormPrlvtComp.getUuid();
      final String id = groupLaboInter.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();

      Clients.evalJavaScript("document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')"
         + ".offsetTop + document.getElementById('" + idGrid + "')" + ".offsetTop;");

   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupEchans.
    */
   public void onOpen$groupEchans(){
      final String idGrid = gridFormPrlvtComp.getUuid();
      final String id = groupEchans.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript("document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')"
         + ".offsetTop + document.getElementById('" + idGrid + "')" + ".offsetTop;");

   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupDerivesPrlvt.
    */
   public void onOpen$groupDerivesPrlvt(){
      final String idGrid = gridFormPrlvtComp.getUuid();
      final String id = groupDerivesPrlvt.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript("document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')"
         + ".offsetTop + document.getElementById('" + idGrid + "')" + ".offsetTop;");

   }

   /*************************************************************************/
   /************************** LINKS ****************************************/
   /*************************************************************************/
   public void onClickObject(final Event event){
      if(event.getData() instanceof Echantillon){
         onClickEchantillonCode(event);
      }else if(event.getData() instanceof ProdDerive){
         onClickProdDeriveCode(event);
      }
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    * @param event Event : clique sur un code d'un dérivé dans
    * la liste des produits dérivés.
    * @throws Exception
    */
   public void onClickEchantillonCode(final Event event){
      final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);

      if(event != null){
         final Echantillon echan = (Echantillon) event.getData();
         tabController.switchToFicheStaticMode(echan);
      }else{
         tabController.getListe()
            .setListObjects(new ArrayList<>(ManagerLocator.getPrelevementManager().getEchantillonsManager(prelevement)));
         tabController.getListe().setCurrentObject(null);
         tabController.clearStaticFiche();
         tabController.getListe().getBinder().loadAttribute(tabController.getListe().getObjectsListGrid(), "model");
         tabController.switchToOnlyListeMode();
      }
      tabController.getListe().updateListResultsLabel(null);
   }

   /**
    * Forward Event. 
    */
   public void onSelectAllEchantillons(){
      onClickEchantillonCode(null);
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    * @param event Event : clique sur un code d'un dérivé dans
    * la liste des produits dérivés.
    * @throws Exception
    */
   public void onClickProdDeriveCode(final Event event){
      final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

      if(event != null){
         final ProdDerive derive = (ProdDerive) event.getData();
         tabController.switchToFicheStaticMode(derive);
      }else{
         tabController.getListe().setListObjects(ManagerLocator.getPrelevementManager().getProdDerivesManager(prelevement));
         tabController.getListe().setCurrentObject(null);
         tabController.clearStaticFiche();
         tabController.getListe().getBinder().loadAttribute(tabController.getListe().getObjectsListGrid(), "model");
         tabController.switchToOnlyListeMode();
      }
      tabController.getListe().updateListResultsLabel(null);
   }

   /**
    * Forward Event. 
    */
   public void onSelectAllDerives(){
      onClickProdDeriveCode(null);
   }

   /**
    * Affiche la page de création des dérivés.
    * @param event Event
    * @throws Exception
    */
   public void onClick$addDerive(final Event event) throws Exception{

      final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

      tabController.switchToCreateMode(this.prelevement);
   }

   /**
    * Affiche la page de création des echantillons.
    * @param event Event
    * @throws Exception
    */
   public void onClick$addEchan(final Event event) throws Exception{

      final EchantillonController tabController = (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);

      tabController.switchToCreateMode(this.prelevement);
   }

   /*public void onClick$print() {
   	StringBuffer sb = new StringBuffer();
   	sb.append(Labels.getLabel("impression.print.prelevement"));
   	sb.append(" ");
   	sb.append(this.prelevement.getCode());
   	
   	openImpressionWindow(page, prelevement, 
   			sb.toString(), isAnonyme(), isCanSeeHistorique());
   }*/

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   private boolean canCreateDerive = false;
   private boolean canCreateEchan = false;

   public boolean isCanCreateEchan(){
      return canCreateEchan;
   }

   public boolean isCanCreateDerive(){
      return canCreateDerive;
   }

   @Override
   public void applyDroitsOnFiche(){
      drawActionsButtons("Prelevement");
      if(sessionScope.containsKey("Banque")){
         canCreateDerive = drawActionOnOneButton("ProdDerive", "Creation");
         canCreateEchan = drawActionOnOneButton("Echantillon", "Creation");
      }else if(sessionScope.containsKey("ToutesCollections")){
         // donne aucun droit en creation
         setCanNew(false);
         canCreateEchan = false;
         canCreateDerive = false;
         //setCanDelete(true);
      }

      //		List<String> entites = new ArrayList<String>();
      //		entites.add("Patient");
      //		entites.add("Echantillon");
      //		entites.add("ProdDerive");
      //		setDroitsConsultation(drawConsultationLinks(entites));

      // si pas le droit d'accès aux dérivés, on cache le lien
      if(!getDroitsConsultation().get("ProdDerive")){
         prodDeriveRenderer.setAccessible(false);
         canAccessDerives = false;
      }else{
         prodDeriveRenderer.setAccessible(true);
         canAccessDerives = true;
      }
      // si pas le droit d'accès aux échantillons, on cache le lien
      if(!getDroitsConsultation().get("Echantillon")){
         echantillonRenderer.setAccessible(false);
         canAccessEchantillons = false;
      }else{
         echantillonRenderer.setAccessible(true);
         canAccessEchantillons = true;
      }

      final boolean canStockage = getDroitOnAction("Stockage", "Consultation");
      echantillonRenderer.setAccessStockage(canStockage);
      prodDeriveRenderer.setAccessStockage(canStockage);

      // gestion des droits sur les patients
      if(getDroitsConsultation().get("Patient")){
         isPatientAccessible = true;
      }else{
         isPatientAccessible = false;
      }

      super.applyDroitsOnFiche();
      addDerive.setDisabled(!canCreateDerive);
      echantillonRenderer.setAnonyme(isAnonyme());
      prodDeriveRenderer.setAnonyme(isAnonyme());

      // change collection
      if(sessionScope.containsKey("AdminPF")){
         canChangeCollection = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         canChangeCollection = (Boolean) sessionScope.get("Admin");
      }
      changeCollection.setDisabled(!canChangeCollection);

      // change maladie
      //		if (sessionScope.containsKey("AdminPF")) {
      //			canChangeMaladie = (Boolean) sessionScope.get("AdminPF");
      //		} else if  (sessionScope.get("Admin")) {
      //			canChangeMaladie = (Boolean) sessionScope.get("Admin");
      //		}

      // 2.0.10
      canChangeMaladie = isCanEdit();

      changeMaladie.setDisabled(!canChangeMaladie);

   }

   /**********************************************************************/
   /*********************** GETTERS / SETTERS ****************************/
   /**********************************************************************/
   public Maladie getMaladie(){
      return maladie;
   }

   public void setMaladie(final Maladie m){
      this.maladie = m;
   }

   public Patient getPatient(){
      return patient;
   }

   public void setPatient(final Patient p){
      this.patient = p;
   }

   public List<LaboInterDecorator> getLaboInters(){
      return laboInters;
   }

   public void setLaboInters(final List<LaboInterDecorator> li){
      this.laboInters = li;
   }

   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> e){
      this.echantillons = e;
   }

   public List<ProdDerive> getDerives(){
      return derives;
   }

   public void setDerives(final List<ProdDerive> d){
      this.derives = d;
   }

   public String getEchantillonsGroupHeader(){
      return echantillonsGroupHeader;
   }

   public String getProdDerivesGroupHeader(){
      return prodDerivesGroupHeader;
   }

   public ProdDeriveRowRenderer getProdDeriveRenderer(){
      return prodDeriveRenderer;
   }

   public EchantillonRowRenderer getEchantillonRenderer(){
      return echantillonRenderer;
   }

   public String getValeurQuantite(){
      return valeurQuantite;
   }

   public boolean getIsPatientAccessible(){
      return isPatientAccessible;
   }

   /**********************************************************************/
   /****************** Modale changement collection **********************/
   /**********************************************************************/
   /**
    * Ouvre la modale permettant le changement de collection.
    */
   public void onClick$changeCollection(){
      openChangeModaleWindow();
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le menu item
    * pour changer le prelevement de collection.
    */
   public void openChangeModaleWindow(){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("changeCollectionWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fichePrelevement.switchBanque.title"));
         win.setBorder("normal");
         win.setWidth("650px");
         win.setPosition("center, top");
         //win.setHeight("600px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateChangeCollectionModal(getObject(), win, page, getMainWindow());
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               //progress.detach();
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   private static HtmlMacroComponent populateChangeCollectionModal(final Prelevement prel, final Window win, final Page page,
      final MainWindow main){

      final Prelevement[] prlvt = {prel};
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("changeBanqueModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openchangeCollectionModale");
      ua.applyProperties();
      ua.afterCompose();

      ((ChangeBanqueModale) ua.getFellow("fwinChangeBanqueModale").getAttributeOrFellow("fwinChangeBanqueModale$composer", true))
         .init(prlvt, main);

      return ua;
   }

   /**********************************************************************/
   /****************** Modale changement maladie    **********************/
   /**********************************************************************/
   /**
    * Ouvre la modale permettant le changement de maladie.
    */
   public void onClick$changeMaladie(){
      openChangeMaladieModaleWindow();
   }

   /**
    * Méthode appelée lorsque l'utilisateur clique sur le menu item
    * pour changer le prelevement de maladie.
    */
   public void openChangeMaladieModaleWindow(){
      if(!isBlockModal()){

         setBlockModal(true);

         // nouvelle fenêtre
         final Window win = new Window();
         win.setVisible(false);
         win.setId("changeCollectionWindow");
         win.setPage(page);
         win.setMaximizable(true);
         win.setSizable(true);
         win.setTitle(Labels.getLabel("fichePrelevement.switchMaladie.title"));
         win.setBorder("normal");
         win.setWidth("650px");
         win.setPosition("center, top");
         //win.setHeight("600px");
         win.setClosable(true);

         final HtmlMacroComponent ua = populateChangeMaladieModal(getObject(), win, page, getMainWindow());
         ua.setVisible(false);

         win.addEventListener("onTimed", new EventListener<Event>()
         {
            @Override
            public void onEvent(final Event event) throws Exception{
               //progress.detach();
               ua.setVisible(true);
            }
         });

         final Timer timer = new Timer();
         timer.setDelay(500);
         timer.setRepeats(false);
         timer.addForward("onTimer", timer.getParent(), "onTimed");
         win.appendChild(timer);
         timer.start();

         try{
            win.onModal();
            setBlockModal(false);

         }catch(final SuspendNotAllowedException e){
            log.error(e);
         }
      }
   }

   private static HtmlMacroComponent populateChangeMaladieModal(final Prelevement prel, final Window win, final Page page,
      final MainWindow main){
      HtmlMacroComponent ua;
      ua = (HtmlMacroComponent) page.getComponentDefinition("changeMaladieModale", false).newInstance(page, null);
      ua.setParent(win);
      ua.setId("openchangeMaladieModale");
      ua.applyProperties();
      ua.afterCompose();

      ((ChangeMaladieModale) ua.getFellow("fwinChangeMaladieModale").getAttributeOrFellow("fwinChangeMaladieModale$composer",
         true)).init(prel, main);

      return ua;
   }

   private boolean blockModal = false;

   @Override
   public boolean isBlockModal(){
      return blockModal;
   }

   @Override
   public void setBlockModal(final boolean b){
      this.blockModal = b;
   }

   public boolean isCanChangeMaladie(){
      return canChangeMaladie;
   }

   public void setCanChangeMaladie(final boolean canMaladie){
      this.canChangeMaladie = canMaladie;
   }

   public Prelevement getPrelevement(){
      return this.prelevement;
   }

   /**
    * Rend le div dossier_inbox de la fiche visible si des
    * dossiers sont en attente pour ce prélèvement.
    * @since 2.0.13.1 pivot code prelevement ou numero labo
    */
   public void setDossierInboxVisible(){
      if(getObjectTabController() != null && getObjectTabController().getEntiteTab() != null && this.prelevement != null
         && this.prelevement.getCode() != null){
         if(SessionUtils.getEmetteursInterfacages(sessionScope).size() > 0){
            if(ManagerLocator.getDossierExterneManager()
               .findByEmetteurInListAndIdentificationManager(SessionUtils.getEmetteursInterfacages(sessionScope),
                  this.prelevement.getCode())
               .size() > 0
               || ManagerLocator.getDossierExterneManager().findByEmetteurInListAndIdentificationManager(
                  SessionUtils.getEmetteursInterfacages(sessionScope), this.prelevement.getNumeroLabo()).size() > 0){
               imgDossierInbox.setVisible(true);
               importDossier.setDisabled(false);
            }else{
               imgDossierInbox.setVisible(false);
               importDossier.setDisabled(true);
            }
         }else{
            imgDossierInbox.setVisible(false);
            importDossier.setDisabled(true);
         }
      }else{
         imgDossierInbox.setVisible(false);
         importDossier.setDisabled(true);
      }
   }

   /**
    * Rend le div dossier_inbox de la fiche visible si des
    * dossiers sont en attente pour ce prélèvement.
    */
   public void setImportDossierVisible(){
      if(SessionUtils.getEmetteursInterfacages(sessionScope).size() > 0){
         importDossier.setVisible(true);
      }else{
         importDossier.setVisible(false);
      }
   }

   /**
    * Ouvre la fenêtre de sélection du dossier à importer.
    */
   public void onClick$importDossier(){
      openSelectDossierExterneWindow(page, Path.getPath(self), this.prelevement.getCode(), true, this.prelevement.clone());
   }

   /**
    * Maj des différentes fiches après update du prélèvement depuis
    * un interfacage.
    */

   public void onGetPrelevementUpdatedFromInterfacage(final Event e){
      if(e.getData() != null){
         setObject((TKdataObject) e.getData());

         // update de la liste
         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().updateObjectGridList(getObject());
         }

         // update de l'objet parent
         if(!getObjectTabController().getReferencingObjectControllers().isEmpty() && getParentObject() != null){
            for(int i = 0; i < getObjectTabController().getReferencingObjectControllers().size(); i++){
               if(getObjectTabController().getReferencingObjectControllers().get(i).getListe() != null){
                  getObjectTabController().getReferencingObjectControllers().get(i).getListe()
                     .updateObjectGridListFromOtherPage(getParentObject(), true);
               }
            }
         }

         // update de la liste des enfants et l'enfant en fiche
         getObjectTabController()
            .updateReferencedObjects((List<TKdataObject>) getObjectTabController().getChildrenObjects(prelevement));

         // commande le passage en mode statique
         getObjectTabController().onEditDone(getObject());
      }
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * Copie depuis AbstractObjectTabController
    * Récupère le controller de la fiche
    *
    * @return
    * @since 2.2.0
    */
   public FicheAnnotationInline getFicheAnnotationInline(){
      if(self.getFellow("ficheTissuInlineAnnoPrelevement") != null){
         return ((FicheAnnotationInline) self.getFellow("ficheTissuInlineAnnoPrelevement").getFellow("fwinAnnotationInline")
            .getAttributeOrFellow("fwinAnnotationInline$composer", true));
      }
      return null;
   }

   /**
    * ANNOTATION INLINE - Bêta
    *
    * Passe qq params au bloc inline annotation sans le dessiner la creation de la
    * fiche statique.
    *
    * @param controller
    * @since 2.2.0
    */
   @Override
   public void setObjectTabController(final AbstractObjectTabController controller){
      super.setObjectTabController(controller);
      //
      final FicheAnnotation inline = getFicheAnnotationInline();
      if(null != inline){
         // passe l'entite au controller
         getFicheAnnotationInline().setEntite(getObjectTabController().getEntiteTab());

         // à remplacer par ce controller
         // setFicheController
         getFicheAnnotationInline().setObjectTabController(getObjectTabController());
      }
   }
}
