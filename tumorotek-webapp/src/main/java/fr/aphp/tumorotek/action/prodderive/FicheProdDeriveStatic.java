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
package fr.aphp.tumorotek.action.prodderive;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.retour.ListeRetour;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.echantillon.EchantillonController;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.decorator.CederObjetDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.factory.CederObjetDecoratorFactory;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche static d'un produit dérivé.
 * Controller créé le 16/06/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FicheProdDeriveStatic extends AbstractFicheStaticController
{

   private static final long serialVersionUID = -5621480824535771884L;

   private Button addDerive;

   // Groups
   private Group groupParent;

   private Group groupDerivesDerive;

   private Group groupCessionsDerive;

   private Group groupSortiesDerive;

   private Grid prodDerivesGrid;

   private Grid cessionsGrid;

   private Label prlvtLinkDerive;

   private Label echanLinkDerive;

   private Row row2EchanDerive;

   private Row row3EchanDerive;

   private Row row4EchanDerive;

   private Row row2PrlvtDerive;

   private Row row3PrlvtDerive;

   private Row row4PrlvtDerive;

   private Row row2DeriveDerive;

   private Row row3DeriveDerive;

   private Row row4DeriveDerive;

   // Lignes pour la transformation
   private Row rowTransformation1;

   private Row rowTransformation2;

   private Row rowTransformation3;

   //private Row transformationInconnueLabel;
   private Component[] objLabelsPrlvtParent;

   private Component[] objLabelsEchanParent;

   private Component[] objLabelsDeriveParent;

   private Component[] objLabelsTransformation;

   private Label patientLabelDerive;

   // Composants a rendre anonymes
   private Label emplacementLabelDerive;

   private Label emplacementEchanLabelDerive;

   // Objets Principaux.
   private ProdDerive prodDerive = new ProdDerive();

   private Transformation transformation;

   private TKdataObject parentObj;

   private String typeParent;

   private List<ProdDerive> derives = new ArrayList<>();

   private List<CederObjetDecorator> cedesDecorated = new ArrayList<>();

   //  Variables formulaire.
   private String valeurQuantite = "";

   private String valeurQuantiteRestante = "";

   private String valeurVolume = "";

   private String valeurVolumeRestant = "";

   private String valeurConcentration = "";

   private String valeurTransfoQuantite = "";

   private String emplacementAdrl = "";

   private String prodDerivesGroupHeader;

   private String cessionsGroupHeader;

   private String sortiesGroupHeader;

   private static ProdDeriveRowRenderer prodDeriveRenderer = new ProdDeriveRowRenderer(false, false);

   private ListeRetour listeRetour;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.derive");

      // liste de composants pour le prlvt parent
      this.objLabelsPrlvtParent = new Component[] {this.row2PrlvtDerive, this.row3PrlvtDerive, this.row4PrlvtDerive};
      // liste de composants pour l'échantillon parent
      this.objLabelsEchanParent = new Component[] {this.row2EchanDerive, this.row3EchanDerive, this.row4EchanDerive};
      // liste de composants pour le dérivé parent
      this.objLabelsDeriveParent = new Component[] {this.row2DeriveDerive, this.row3DeriveDerive, this.row4DeriveDerive};
      // liste de composants pour la transformation
      this.objLabelsTransformation = new Component[] {this.rowTransformation1, this.rowTransformation2, this.rowTransformation3};

      this.prodDerivesGrid.setVisible(false);
      this.cessionsGrid.setVisible(false);
      this.addDerive.setDisabled(true);

      groupDerivesDerive.setOpen(false);
      groupCessionsDerive.setOpen(false);
      groupSortiesDerive.setOpen(false);

      prodDeriveRenderer.setEmbedded(true);
      prodDeriveRenderer.setTtesCollections(getTtesCollections());

      listeRetour =
         ((ListeRetour) self.getFellow("listeRetour").getFellow("lwinRetour").getAttributeOrFellow("lwinRetour$composer", true));

   }

   @Override
   public ProdDeriveController getObjectTabController(){
      return (ProdDeriveController) super.getObjectTabController();
   }

   @Override
   public void setObject(final TKdataObject e){
      this.prodDerive = (ProdDerive) e;

      initAssociations();

      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("ficheEchantillon.prodDerives"));
      sb.append(" (");
      sb.append(derives.size());
      sb.append(")");
      prodDerivesGroupHeader = sb.toString();

      sb = new StringBuffer();
      sb.append(Labels.getLabel("ficheProdDerive.cessions"));
      sb.append(" (");
      sb.append(cedesDecorated.size());
      sb.append(")");
      cessionsGroupHeader = sb.toString();

      // Initialisation du nombre de sorties à afficher sur la page
      updateSortiesHeader(false);

      initQuantiteVolumeAndConc();

      if(derives.size() == 0){
         prodDerivesGrid.setVisible(false);
         groupDerivesDerive.setOpen(false);
      }else{
         this.prodDerivesGrid.setVisible(true);
         groupDerivesDerive.setOpen(true);
      }
      if(cedesDecorated.size() == 0){
         cessionsGrid.setVisible(false);
         groupCessionsDerive.setOpen(false);
      }else{
         cessionsGrid.setVisible(true);
         groupCessionsDerive.setOpen(true);
      }
      if(listeRetour.getListObjects().size() == 0){
         if(listeRetour.getObjectsListGrid().isVisible()){
            groupSortiesDerive.setOpen(false);
            listeRetour.getObjectsListGrid().setVisible(false);
         }
      }else{
         if(!listeRetour.getObjectsListGrid().isVisible()){
            groupSortiesDerive.setOpen(true);
            listeRetour.getObjectsListGrid().setVisible(true);
         }
      }
      listeRetour.getLwinRetour().invalidate();

      showParentInformation();

      if(isAnonyme()){
         AbstractController.makeLabelAnonyme(patientLabelDerive, false);
      }else{
         patientLabelDerive.setValue(getNomPatient());
      }
      if(getDroitsConsultation().get("Patient")){
         patientLabelDerive.setSclass("formLink");
      }else{
         patientLabelDerive.setSclass("formValue");
      }

      // annotations
      super.setObject(prodDerive);
   }

   @Override
   public TKdataObject getParentObject(){
      return parentObj;
   }

   @Override
   public void setParentObject(final TKdataObject obj){
      parentObj = obj;
   }

   @Override
   public void disableToolBar(final boolean b){
      super.disableToolBar(b);
      addDerive.setDisabled(b || !isCanCreateDerive());
   }

   /**
    * Cette méthode va afficher les données du parent du dérivé
    * en fonction de son type (prlvt, échantillon, prodderive).
    */
   public void showParentInformation(){

      if(typeParent.equals("Prelevement")){
         groupParent.setLabel(Labels.getLabel("ficheProdDerive.prelevement.titre"));
         for(int i = 0; i < objLabelsPrlvtParent.length; i++){
            objLabelsPrlvtParent[i].setVisible(true);
            objLabelsEchanParent[i].setVisible(false);
            objLabelsDeriveParent[i].setVisible(false);
         }
         for(int i = 0; i < objLabelsTransformation.length; i++){
            objLabelsTransformation[i].setVisible(true);
         }
         groupParent.setVisible(true);
         groupParent.setOpen(true);
         //transformationInconnueLabel.setVisible(false);
         // si pas le droit, on cache le lien
         if(!getDroitsConsultation().get("Prelevement")){
            prlvtLinkDerive.setSclass("formValue");
         }
      }else if(typeParent.equals("Echantillon")){
         groupParent.setLabel(Labels.getLabel("ficheProdDerive.echantillon.titre"));
         for(int i = 0; i < objLabelsPrlvtParent.length; i++){
            objLabelsPrlvtParent[i].setVisible(false);
            objLabelsEchanParent[i].setVisible(true);
            objLabelsDeriveParent[i].setVisible(false);
         }
         for(int i = 0; i < objLabelsTransformation.length; i++){
            objLabelsTransformation[i].setVisible(true);
         }
         groupParent.setVisible(true);
         groupParent.setOpen(true);
         //transformationInconnueLabel.setVisible(false);
         // si pas le droit, on cache le lien
         if(!getDroitsConsultation().get("Echantillon")){
            echanLinkDerive.setSclass("formValue");
         }
      }else if(typeParent.equals("ProdDerive")){
         groupParent.setLabel(Labels.getLabel("ficheProdDerive.prodderive.titre"));
         for(int i = 0; i < objLabelsPrlvtParent.length; i++){
            objLabelsPrlvtParent[i].setVisible(false);
            objLabelsEchanParent[i].setVisible(false);
            objLabelsDeriveParent[i].setVisible(true);
         }
         for(int i = 0; i < objLabelsTransformation.length; i++){
            objLabelsTransformation[i].setVisible(true);
         }
         groupParent.setVisible(true);
         groupParent.setOpen(true);
         //transformationInconnueLabel.setVisible(false);
      }else if(typeParent.equals("Aucun")){
         for(int i = 0; i < objLabelsPrlvtParent.length; i++){
            objLabelsPrlvtParent[i].setVisible(false);
            objLabelsEchanParent[i].setVisible(false);
            objLabelsDeriveParent[i].setVisible(false);
         }
         for(int i = 0; i < objLabelsTransformation.length; i++){
            objLabelsTransformation[i].setVisible(false);
         }

         //transformationInconnueLabel.setVisible(true);
         groupParent.setOpen(false);
         groupParent.setVisible(false);
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void setNewObject(){
      setObject(new ProdDerive());

      // disable edit et delete
      super.setNewObject();
      // disable autres buttons
      addDerive.setDisabled(true);
   }

   @Override
   public ProdDerive getObject(){
      return this.prodDerive;
   }

   @Override
   public void prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getProdDeriveManager().isUsedObjectManager(getObject());
      final boolean isCessed = ManagerLocator.getProdDeriveManager().isCessedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(!isCessed){
         if(isUsed){
            setDeleteMessage(Labels.getLabel("derive.deletion.isUsedCascade"));
         }
      }else{ //objet non supprimable car cédé
         setDeleteMessage(Labels.getLabel("derive.deletion.isUsedNonCascade"));
      }
      setFantomable(!isCessed);
      setDeletable(!isCessed);
      setCascadable(!isCessed && isUsed);
   }

   @Override
   public void removeObject(final String comments){
      final List<File> filesToDelete = new ArrayList<>();
      if(!isCascadable()){
         ManagerLocator.getProdDeriveManager().removeObjectManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope), filesToDelete);
      }else{
         ManagerLocator.getProdDeriveManager().removeObjectCascadeManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope), filesToDelete);
      }
      for(final File f : filesToDelete){
         f.delete();
      }
   }

   /*************************************************************************/
   /************************** FORMATTERS************************************/
   /*************************************************************************/
   /**
    * Méthode initialisant les objets associés.
    */
   public void initAssociations(){
      transformation = null;
      if(this.prodDerive != null){
         transformation = this.prodDerive.getTransformation();
      }
      setParentObject(null);

      if(transformation != null){
         // on récupère le type du parent (prlvt, echan, prodderive)
         typeParent = transformation.getEntite().getNom();

         // en fonction du type, on récupère l'objet
         if(typeParent.equals("Prelevement")){
            setParentObject((Prelevement) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(transformation.getEntite(), transformation.getObjetId()));
         }else if(typeParent.equals("Echantillon")){
            setParentObject((Echantillon) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(transformation.getEntite(), transformation.getObjetId()));
         }else if(typeParent.equals("ProdDerive")){
            setParentObject((ProdDerive) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(transformation.getEntite(), transformation.getObjetId()));
         }
      }else{
         typeParent = "Aucun";
      }

      derives.clear();
      cedesDecorated.clear();
      emplacementAdrl = "";
      // on récupère les objets associés
      if(this.prodDerive.getProdDeriveId() != null){
         // on extrait les dérivés et on initialise le nombre à afficher
         derives = ManagerLocator.getProdDeriveManager().getProdDerivesManager(prodDerive);
         cedesDecorated = new CederObjetDecoratorFactory(isAnonyme(), null)
            .decorateListe(ManagerLocator.getCederObjetManager().findByObjetManager(this.prodDerive));
         emplacementAdrl = ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager(prodDerive);
      }

      // sorties
      listeRetour.setObject(prodDerive);
   }

   /**
    * Méthode initialisant les champs de formulaire pour la quantité,
    * le volume et la concentration.
    */
   public void initQuantiteVolumeAndConc(){
      StringBuffer sb = new StringBuffer();
      if(this.prodDerive.getQuantite() != null){
         sb.append(this.prodDerive.getQuantite());
      }else{
         sb.append("-");
      }
      valeurQuantiteRestante = sb.toString();
      sb.append(" / ");
      if(this.prodDerive.getQuantiteInit() != null){
         sb.append(this.prodDerive.getQuantiteInit());
      }else{
         sb.append("-");
      }
      if(this.prodDerive.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getQuantiteUnite().getUnite());
      }
      valeurQuantite = sb.toString();

      sb = new StringBuffer();
      if(this.prodDerive.getVolume() != null){
         sb.append(this.prodDerive.getVolume());
      }else{
         sb.append("-");
      }
      valeurVolumeRestant = sb.toString();
      sb.append(" / ");
      if(this.prodDerive.getVolumeInit() != null){
         sb.append(this.prodDerive.getVolumeInit());
      }else{
         sb.append("-");
      }
      if(this.prodDerive.getVolumeUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getVolumeUnite().getUnite());
      }
      valeurVolume = sb.toString();

      sb = new StringBuffer();
      if(this.prodDerive.getConc() != null){
         sb.append(this.prodDerive.getConc());
      }else{
         sb.append("-");
      }
      if(this.prodDerive.getConcUnite() != null){
         sb.append(" ");
         sb.append(this.prodDerive.getConcUnite().getUnite());
      }
      valeurConcentration = sb.toString();

      sb = new StringBuffer();
      if(this.transformation != null){
         if(this.transformation.getQuantite() != null){
            sb.append(this.transformation.getQuantite());
         }else{
            sb.append("-");
         }
         if(this.transformation.getQuantiteUnite() != null){
            sb.append(" ");
            sb.append(this.transformation.getQuantiteUnite().getUnite());
         }
      }
      valeurTransfoQuantite = sb.toString();
   }

   /**
    * Formate la date du prélèvement parent.
    * @return Date du prélèvement formatée.
    */
   public String getDatePrelevementFormated(){
      if(getParentPrlvt() != null){
         return ObjectTypesFormatters.dateRenderer2(getParentPrlvt().getDatePrelevement());
      }
      return null;
   }

   /**
    * Renvoie l'emplacement de l'échantillon parent.
    * @return
    */
   public String getEmplacementEchantillonAdrl(){

      String emplacementEchantillonAdrl = null;

      Boolean isAutorise;

      if(isAnonyme()){
         isAutorise = false;
      }else{
         isAutorise = getDroitOnAction("Stockage", "Consultation");
      }

      if(!isAutorise){
         makeLabelAnonyme(emplacementEchanLabelDerive, false);
         emplacementEchantillonAdrl = getAnonymeString();
      }else if(getParentEchantillon() != null){
         emplacementEchantillonAdrl = ManagerLocator.getEchantillonManager().getEmplacementAdrlManager(getParentEchantillon());
      }

      return emplacementEchantillonAdrl;

   }

   /**
    * Renvoie les codes lésionnels de l'échantillon parent.
    * @return
    */
   public String getCodeLesionnelEchantillon(){
      if(getParentEchantillon() != null){
         final StringBuffer sb = new StringBuffer();
         final List<CodeAssigne> codes =
            ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(getParentEchantillon());
         final Iterator<CodeAssigne> it = codes.iterator();
         while(it.hasNext()){
            final CodeAssigne next = it.next();
            sb.append(next.getCode());
            if(next.getLibelle() != null){
               sb.append(" (");
               sb.append(next.getLibelle());
               sb.append(")");
            }
            if(it.hasNext()){
               sb.append(", ");
            }
         }
         return sb.toString();
      }
      return null;
   }

   /**
    * Renvoie l'emplacement de l'échantillon parent.
    * @return
    */
   public String getEmplacementProdDeriveAdrl(){
      if(getParentProdDerive() != null){
         if(isAnonyme()){
            return null;
         }
         return ManagerLocator.getProdDeriveManager().getEmplacementAdrlManager(getParentProdDerive());
      }
      return null;
   }

   /**
    * Formate la date de stockage du dérivé.
    * @return Date de stockage formatée.
    */
   public String getDateCongelationFormated(){
      if(this.prodDerive != null){
         return ObjectTypesFormatters.dateRenderer2(this.prodDerive.getDateStock());
      }
      return null;
   }

   /**
    * Formate la date de transformation du dérivé.
    * @return Date de transformation formatée.
    */
   public String getDateTransformationFormated(){
      if(this.prodDerive != null){
         return ObjectTypesFormatters.dateRenderer2(this.prodDerive.getDateTransformation());
      }
      return null;
   }

   public String getSClassOperateur(){
      if(this.prodDerive != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.prodDerive.getCollaborateur());
      }
      return null;
   }

   public String getSClassCession(){
      if(getDroitsConsultation().get("Cession")){
         return "formLink";
      }
      return null;
   }

   public String getSClassStockage(){

      if(isCanStockage()){
         return "formLink";
      }

      return "formAnonymeBlock";

   }

   /*************************************************************************/
   /************************** GROUPS ***************************************/
   /*************************************************************************/
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
    * Indique si la liste contient plus d'une cession.
    */
   public boolean getCessionsListSizeSupOne(){
      if(getDroitsConsultation().containsKey("Cession") && getDroitsConsultation().get("Cession")){
         return this.cedesDecorated.size() > 1;
      }
      return false;
   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupDerivesDerive.
    */
   public void onOpen$groupDerivesDerive(){
      final String id = groupDerivesDerive.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupCessionsDerive.
    */
   public void onOpen$groupCessionsDerive(){
      final String id = groupCessionsDerive.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /*************************************************************************/
   /************************** LINKS ****************************************/
   /*************************************************************************/
   public void onClickObject(final Event event){
      if(event.getData() instanceof ProdDerive){
         onClickProdDeriveCode(event);
      }else if(event.getData() instanceof Cession){
         onClick$numCession(event);
      }
   }

   /**
    * Affiche la fiche d'un prélèvement.
    * @param event Event : clique sur un lien prlvtLinkDerive.
    * @throws Exception
    */
   public void onClick$prlvtLinkDerive(final Event event) throws Exception{
      if(getDroitsConsultation().get("Prelevement")){
         final PrelevementController tabController =
            (PrelevementController) PrelevementController.backToMe(getMainWindow(), page);

         tabController.switchToFicheStaticMode(getParentObject());
      }
   }

   /**
    * Affiche la fiche d'un échantillon.
    * @param event Event : clique sur un lien echanLinkDerive.
    * @throws Exception
    */
   public void onClick$echanLinkDerive(final Event event) throws Exception{
      if(getDroitsConsultation().get("Echantillon")){
         final EchantillonController tabController =
            (EchantillonController) EchantillonController.backToMe(getMainWindow(), page);

         tabController.switchToFicheStaticMode(getParentObject());
      }
   }

   /**
    * Affiche la fiche d'un dérivé.
    * @param event Event : clique sur un lien deriveLinkDerive.
    * @throws Exception
    */
   public void onClick$deriveLinkDerive(final Event event) throws Exception{
      getObjectTabController().switchToFicheStaticMode(getParentObject());
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    * @param event Event : clique sur un code d'un dérivé dans
    * la liste des produits dérivés.
    * @throws Exception
    */
   public void onClickProdDeriveCode(final Event event){
      if(event != null){
         final ProdDerive derive = (ProdDerive) event.getData();
         getObjectTabController().switchToFicheStaticMode(derive);
      }else{
         getObjectTabController().getListe().getListObjects().clear();
         getObjectTabController().getListe().getListObjects().addAll(this.derives);
         getObjectTabController().getListe().setCurrentObject(null);
         getObjectTabController().clearStaticFiche();
         getObjectTabController().getListe().getBinder().loadAttribute(getObjectTabController().getListe().getObjectsListGrid(),
            "model");
         getObjectTabController().switchToOnlyListeMode();
      }
      getObjectTabController().getListe().updateListResultsLabel(null);
   }

   /**
    * Forward Event.
    */
   public void onSelectAllDerives(){
      onClickProdDeriveCode(null);
   }

   /**
    * Affiche la fiche d'une cession.
    * @param event Event : clique sur un lien numCession dans
    * la liste des produits dérivés.
    * @throws Exception
    */
   public void onClick$numCession(final Event event){

      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      displayObjectData(deco.getCederObjet().getCession());
   }

   public void onSelectAllCessions(){
      final List<Cession> cessions = new ArrayList<>();
      for(int i = 0; i < cedesDecorated.size(); i++){
         final Cession cess = cedesDecorated.get(i).getCederObjet().getCession();
         cessions.add(cess);
      }

      displayObjectsListData(new ArrayList<TKAnnotableObject>(cessions));
   }

   /**
    * Affiche la page de création d'un nouveau dérivé.
    * @param event Event : clique sur le bouton addDeriveForEchan.
    * @throws Exception
    */
   public void onClick$addDerive(final Event event) throws Exception{
      if(this.prodDerive != null){
         final List<TKStockableObject> objs = new ArrayList<>();
         objs.add(prodDerive);
         if(!getObjStatutIncompatibleForRetour(objs, "TRANSFORMATION")){
            getObjectTabController().switchToCreateMode(this.prodDerive);
         }
      }
   }

   /**
    * Affiche la fiche du patient.
    */
   public void onClick$patientLabelDerive(){
      if(getDroitsConsultation().get("Patient")){
         Prelevement prlvt = null;
         if(prodDerive.getProdDeriveId() != null){
            prlvt = ManagerLocator.getProdDeriveManager().getPrelevementParent(prodDerive);
            final Maladie maladie = prlvt.getMaladie();
            Patient patient = null;
            if(maladie != null && maladie.getPatient() != null){
               patient = maladie.getPatient();
            }

            if(patient != null){
               final PatientController tabController = (PatientController) PatientController.backToMe(getMainWindow(), page);
               tabController.switchToFicheStaticMode(patient);
            }
         }
      }
   }

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   private boolean canCreateDerive = false;

   private boolean canStockage = true;

   public boolean isCanCreateDerive(){
      return canCreateDerive;
   }

   public boolean isCanStockage(){
      return canStockage;
   }

   @Override
   public void applyDroitsOnFiche(){
      drawActionsButtons("ProdDerive");
      if(sessionScope.containsKey("Banque")){
         canCreateDerive = drawActionOnOneButton("ProdDerive", "Creation");
      }else if(sessionScope.containsKey("ToutesCollections")){
         // donne aucun droit en creation
         setCanNew(false);
         canCreateDerive = false;
         //setCanDelete(true);
      }

      canStockage = getDroitOnAction("Stockage", "Consultation");
      if(canStockage){
         emplacementLabelDerive.addForward("onClick", self, "onClickObjectEmplacementFromFiche");
      }else{
         emplacementLabelDerive.removeForward("onClick", self, "onClickObjectEmplacementFromFiche");
      }

      super.applyDroitsOnFiche();
      addDerive.setDisabled(!canCreateDerive);
      prodDeriveRenderer.setAnonyme(isAnonyme());
   }

   /**********************************************************************/
   /*********************** GETTERS / SETTERS ****************************/
   /**********************************************************************/
   public Prelevement getParentPrlvt(){
      if(getParentObject() instanceof Prelevement){
         return (Prelevement) getParentObject();
      }
      return null;
   }

   public Echantillon getParentEchantillon(){
      if(getParentObject() instanceof Echantillon){
         return (Echantillon) getParentObject();
      }
      return null;
   }

   public ProdDerive getParentProdDerive(){
      if(getParentObject() instanceof ProdDerive){
         return (ProdDerive) getParentObject();
      }
      return null;
   }

   public String getTypeParent(){
      return typeParent;
   }

   public List<ProdDerive> getDerives(){
      return derives;
   }

   public List<CederObjetDecorator> getCedesDecorated(){
      return cedesDecorated;
   }

   public String getValeurQuantite(){
      return valeurQuantite;
   }

   public String getValeurQuantiteRestante(){
      return valeurQuantiteRestante;
   }

   public String getValeurVolume(){
      return valeurVolume;
   }

   public String getValeurVolumeRestant(){
      return valeurVolumeRestant;
   }

   public String getValeurConcentration(){
      return valeurConcentration;
   }

   public String getValeurTransfoQuantite(){
      return valeurTransfoQuantite;
   }

   public String getProdDerivesGroupHeader(){
      return prodDerivesGroupHeader;
   }

   public String getCessionsGroupHeader(){
      return cessionsGroupHeader;
   }

   public static ProdDeriveRowRenderer getProdDeriveRenderer(){
      return prodDeriveRenderer;
   }

   public String getEmplacementAdrl(){

      Boolean isAutorise;

      if(isAnonyme()){
         isAutorise = false;
      }else{
         isAutorise = getDroitOnAction("Stockage", "Consultation");
      }

      if(!isAutorise){
         makeLabelAnonyme(emplacementLabelDerive, false);
         return getAnonymeString();
      }

      return emplacementAdrl;

   }

   public String getTemperatureFormated(){

      Float temp = null;

      if(this.prodDerive != null && this.prodDerive.getProdDeriveId() != null){
         final Emplacement emp = ManagerLocator.getProdDeriveManager().getEmplacementManager(prodDerive);

         if(emp != null){
            final Conteneur cont = ManagerLocator.getEmplacementManager().getConteneurManager(emp);
            if(cont != null && cont.getTemp() != null){
               temp = cont.getTemp();
            }
         }
      }

      return ObjectTypesFormatters.formatTemperature(temp);
   }

   public String getConformeTraitementFormated(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive != null){
         if(this.prodDerive.getConformeTraitement() != null){
            sb.append(ObjectTypesFormatters.booleanLitteralFormatter(this.prodDerive.getConformeTraitement()));

            if(this.prodDerive.getConformeTraitement() != null && !this.prodDerive.getConformeTraitement()){
               sb.append(" - ");
               final List<ObjetNonConforme> list =
                  ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(prodDerive, "Traitement");

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

   public String getConformeCessionFormated(){
      final StringBuffer sb = new StringBuffer();
      if(this.prodDerive != null){
         if(this.prodDerive.getConformeCession() != null){
            sb.append(ObjectTypesFormatters.booleanLitteralFormatter(this.prodDerive.getConformeCession()));

            if(this.prodDerive.getConformeCession() != null && !this.prodDerive.getConformeCession()){
               sb.append(" - ");
               final List<ObjetNonConforme> list =
                  ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(prodDerive, "Cession");

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

   /*public void onClick$print() {
   	StringBuffer sb = new StringBuffer();
   	sb.append(Labels.getLabel("impression.print.prodDerive"));
   	sb.append(" ");
   	sb.append(this.prodDerive.getCode());

   	openImpressionWindow(page, prodDerive, sb.toString(), isAnonyme());
   }*/

   public String getNomPatient(){
      Prelevement prlvt = null;
      if(prodDerive.getProdDeriveId() != null){
         prlvt = ManagerLocator.getProdDeriveManager().getPrelevementParent(prodDerive);
      }
      if(prlvt != null){
         return PrelevementUtils.getPatientNomAndPrenomOrIdentifiantGatsbi(prlvt);
      }
      return null;
   }

   /**
    * Recoit l'évenement de clique sur le lien emplacement contenu
    * dans la fiche statique.
    * @param event
    */
   public void onClickObjectEmplacementFromFiche(final Event event){
      if(getObject().getProdDeriveId() != null){
         Events.postEvent("onClickObjectEmplacement", self,
            ManagerLocator.getProdDeriveManager().getEmplacementManager(getObject()));
      }
   }

   /**
    * Recoit l'evenement venant de la liste de retours quand
    * un élément est ajouté.
    */
   public void onClickUpdateSorties$retourRow(final Event event){

      getObjectTabController().getListe().updateObjectGridListFromOtherPage(((ForwardEvent) event).getOrigin().getData(), true);
   }

   /**
    * Rafraichit le header de la liste de sorties.
    * @param force force le rafraichissement du header si true
    */
   public void updateSortiesHeader(final boolean force){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("listeRetour.title"));
      sb.append(" (");
      sb.append(listeRetour.getListObjects().size());
      if(listeRetour.getAddedDelais() != null){
         sb.append(" - ");
         sb.append(ObjectTypesFormatters.getHeureMinuteLabel(listeRetour.getAddedDelais()));
      }
      sb.append(")");
      sortiesGroupHeader = sb.toString();

      if(force){
         groupSortiesDerive.setLabel(sortiesGroupHeader);
      }
   }

   public String getSortiesGroupHeader(){
      return sortiesGroupHeader;
   }

   public void setSortiesGroupHeader(final String s){
      this.sortiesGroupHeader = s;
   }

   public String getObjetStatut(){
      return ObjectTypesFormatters.ILNObjectStatut(getObject().getObjetStatut());
   }

   /**
    * @return the prodDerive
    */
   public ProdDerive getProdDerive(){
      return prodDerive;
   }

   /**
    * @param prodDerive the prodDerive to set
    */
   public void setProdDerive(final ProdDerive prodDerive){
      this.prodDerive = prodDerive;
   }

   /**
    * Surcharge de la méthode pour éviter la mise à jour du binding de la liste
    * de dérivés en cas de suppression de dérivés de dérivés.
    */
   @Override
   public void onLaterDelete(final Event event){
      try{
         final List<Integer> ids = new ArrayList<>();
         ids.add(((TKdataObject) getObject()).listableObjectId());

         final Map<Entite, List<Integer>> children = getObjectTabController().getChildrenObjectsIds(ids);

         final Map<Entite, List<Integer>> parents = getObjectTabController().getParentsObjectsIds(ids);

         removeObject((String) event.getData());
         if(getObjectTabController() != null){
            if(getObjectTabController().getListe() != null){

               if(getObjectTabController().getListe().getSelectedObjects() != null){
                  getObjectTabController().getListe().clearSelection();
               }
               // on déselectionne la ligne courante
               getObjectTabController().getListe().deselectRow();

               // on enlève l'objet de la liste
               getObjectTabController().getListe().removeObjectFromList(getObject());
            }
         }

         //			// update de l'objet parent
         //			if (!getObjectTabController()
         //					.getReferencingObjectControllers().isEmpty()
         //												&& parent != null) {
         //				for (int i = 0; i < getObjectTabController()
         //							.getReferencingObjectControllers().size(); i++) {
         //					if (getObjectTabController()
         //							.getReferencingObjectControllers()
         //												.get(i).getListe() != null) {
         //						getObjectTabController()
         //							.getReferencingObjectControllers().get(i).getListe()
         //						.updateObjectGridListFromOtherPage(parent, true);
         //					}
         //				}
         //			}

         // update de la liste des parents
         getObjectTabController().updateParentsReferences(parents);

         // update de la liste des enfants
         getObjectTabController().updateChildrenReferences(children, true);

         // update de la grille
         getObjectTabController().getListe().refreshListe();
         getObjectTabController().switchToOnlyListeMode();

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }
}
