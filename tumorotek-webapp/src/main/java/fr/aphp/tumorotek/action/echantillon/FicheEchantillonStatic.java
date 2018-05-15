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
package fr.aphp.tumorotek.action.echantillon;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.cession.retour.ListeRetour;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveController;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveRowRenderer;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.decorator.CederObjetDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.factory.CederObjetDecoratorFactory;
import fr.aphp.tumorotek.manager.impl.xml.CoupleSimpleValeur;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.LigneDeuxColonnesParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.LigneSimpleParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.manager.impl.xml.SousParagraphe;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche static d'un échantillon.
 * Controller créé le 21/06/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FicheEchantillonStatic extends AbstractFicheStaticController
{

   private final Log log = LogFactory.getLog(FicheEchantillonStatic.class);

   private static final long serialVersionUID = 4995144588806344858L;

   private Button addDerive;
   private Menuitem printINCa;

   // Groups
   private Group groupDerivesEchan;
   private Group groupCessionsEchan;
   private Group groupSortiesEchan;
   protected Group groupInfosCompEchan;
   private Grid prodDerivesGrid;
   private Grid cessionsGrid;

   // Infos prelevement
   //private Group groupPrlvt;
   private Row row1PrlvtEchan;
   private Row row2PrlvtEchan;
   private Row row3PrlvtEchan;
   private Label codePrlvtLabel;
   private Label prlvtInconnuEchan;
   private Label patientLabel;
   private Label anapathLabel;

   private Vbox risquesBox;

   // Objets Principaux.
   private Echantillon echantillon = new Echantillon();
   private Prelevement prelevement;
   private List<ProdDerive> derives = new ArrayList<>();
   private List<CederObjetDecorator> cedesDecorated = new ArrayList<>();

   // Variables formulaire.
   private String valeurQuantite = "";
   private String delaiLabel = "";
   private String prodDerivesGroupHeader;
   private String cessionsGroupHeader;
   private String sortiesGroupHeader;
   private String emplacementAdrl = "";

   private boolean canAccessDerives = true;

   private static ProdDeriveRowRenderer prodDeriveRenderer = new ProdDeriveRowRenderer(false, false);

   //Codes assignes
   private Div codesOrganeDiv;
   private Div codesMorphoDiv;

   // INCa
   private Integer nbItemsINCaTotaux;
   private Integer nbItemsINCaRemplis;

   // Labels à rendre anonymes
   private Label emplacementLabelEchan;

   // INCa
   private String hautPageInca;
   private String piedPageInca;

   private ListeRetour listeRetour;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.echantillon");
      setFantomable(true);

      this.prodDerivesGrid.setVisible(false);
      this.cessionsGrid.setVisible(false);
      this.addDerive.setDisabled(true);

      groupDerivesEchan.setOpen(false);
      groupCessionsEchan.setOpen(false);
      groupSortiesEchan.setOpen(true);

      prodDeriveRenderer.setEmbedded(true);
      prodDeriveRenderer.setTtesCollections(getTtesCollections());

      listeRetour =
         ((ListeRetour) self.getFellow("listeRetour").getFellow("lwinRetour").getAttributeOrFellow("lwinRetour$composer", true));
   }

   @Override
   public EchantillonController getObjectTabController(){
      return (EchantillonController) super.getObjectTabController();
   }

   @Override
   public void setObject(final TKdataObject e){
      this.echantillon = (Echantillon) e;

      initAssociations();

      // Initialisation du nombre de dérivés à afficher sur la page
      StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("ficheEchantillon.prodDerives"));
      sb.append(" (");
      sb.append(derives.size());
      sb.append(")");
      prodDerivesGroupHeader = sb.toString();

      // Initialisation du nombre de cessions à afficher sur la page
      sb = new StringBuffer();
      sb.append(Labels.getLabel("ficheEchantillon.cessions"));
      sb.append(" (");
      sb.append(cedesDecorated.size());
      sb.append(")");
      cessionsGroupHeader = sb.toString();

      // Initialisation du nombre de sorties à afficher sur la page
      updateSortiesHeader(false);

      // Initilisation des variables formulaires
      initQuantite();
      initDelaiCgl();

      if(derives.size() == 0){
         this.prodDerivesGrid.setVisible(false);
         groupDerivesEchan.setOpen(false);
      }else{
         this.prodDerivesGrid.setVisible(true);
         groupDerivesEchan.setOpen(true);
      }
      if(cedesDecorated.size() == 0){
         cessionsGrid.setVisible(false);
         groupCessionsEchan.setOpen(false);
      }else{
         cessionsGrid.setVisible(true);
         groupCessionsEchan.setOpen(true);
      }
      if(listeRetour.getListObjects().size() == 0){
         if(listeRetour.getObjectsListGrid().isVisible()){
            groupSortiesEchan.setOpen(false);
            listeRetour.getObjectsListGrid().setVisible(false);
         }
      }else{
         if(!listeRetour.getObjectsListGrid().isVisible()){
            groupSortiesEchan.setOpen(true);
            listeRetour.getObjectsListGrid().setVisible(true);
         }
      }
      listeRetour.getLwinRetour().invalidate();

      //		for (int i = 0; i < infosPrlvt.length; i++) {
      //			infosPrlvt[i].setVisible(true);
      //		}

      if(prelevement != null){
         if(getDroitsConsultation().get("Prelevement")){
            codePrlvtLabel.setSclass("formLink");
         }else{
            codePrlvtLabel.setSclass("formValue");
         }
         if(getDroitsConsultation().get("Patient")){
            patientLabel.setSclass("formLink");
         }else{
            patientLabel.setSclass("formValue");
         }
         row2PrlvtEchan.setVisible(true);
         row3PrlvtEchan.setVisible(true);
         row1PrlvtEchan.setVisible(false);
         prlvtInconnuEchan.setVisible(false);
      }else{
         row1PrlvtEchan.setVisible(true);
         row2PrlvtEchan.setVisible(false);
         row3PrlvtEchan.setVisible(false);
         prlvtInconnuEchan.setVisible(true);
      }

      if(isAnonyme()){
         AbstractController.makeLabelAnonyme(patientLabel, false);
         AbstractController.makeLabelAnonyme(anapathLabel, false);
      }else{
         patientLabel.setValue(getNomPatient());
         if(echantillon.getCrAnapath() != null){
            anapathLabel.setValue(echantillon.getCrAnapath().getNom());
         }else{
            anapathLabel.setValue(null);
         }
      }

      drawRisquesFormatted();

      groupInfosCompEchan
         .setOpen(echantillon.getBanque() != null && echantillon.getBanque().getContexte().getNom().equals("anatomopathologie"));

      // annotations
      super.setObject(echantillon);

      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       * Il pourrait y avoir optimisation ICI car le bloc inline est redessiné à chaque fois
       * qu'un nouvel objet est affiché.
       * A priori, il ne serait utile de re-dessiner que si la collection change!
       * Cette optimisation est valable pour FicheAnnotation également.
       */
      /*FicheAnnotation inline = getFicheAnnotationInline();
      if(inline != null){ // re-dessine le bloc inline annotation
      	inline.setObj((TKAnnotableObject) echantillon);
      }*/

      getBinder().loadComponent(self);
   }

   @Override
   public void disableToolBar(final boolean b){
      super.disableToolBar(b);
      addDerive.setDisabled(b || !isCanCreateDerive());
   }

   /**
    * Méthode initialisant les objets associés.
    */
   public void initAssociations(){
      prelevement = null;
      derives.clear();
      cedesDecorated.clear();
      emplacementAdrl = "";

      if(this.echantillon.getEchantillonId() != null){
         // on récupère la liste des dérivés de l'échantillon
         derives = new ArrayList<>();
         if(canAccessDerives){
            derives = ManagerLocator.getEchantillonManager().getProdDerivesManager(echantillon);
         }
         // on récupère le prélèvement parent
         prelevement = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
         // on récupère les cédés
         cedesDecorated = new CederObjetDecoratorFactory(isAnonyme(), null)
            .decorateListe(ManagerLocator.getCederObjetManager().findByObjetManager(this.echantillon));
         emplacementAdrl = ManagerLocator.getEchantillonManager().getEmplacementAdrlManager(echantillon);

         // dessine les codes organes
         EchantillonController.drawCodesAssignes(echantillon, codesOrganeDiv, true, true);
         EchantillonController.drawCodesAssignes(echantillon, codesMorphoDiv, false, true);
      }
      // sorties
      listeRetour.setObject(echantillon);
   }

   @Override
   public void setNewObject(){
      setObject(new Echantillon());

      // disable edit et delete
      super.setNewObject();
      // disable autres buttons
      addDerive.setDisabled(true);
   }

   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getEchantillonManager().findByIdManager(id);
   }

   @Override
   public Echantillon getObject(){
      return this.echantillon;
   }

   @Override
   public Prelevement getParentObject(){
      return this.prelevement;
   }

   @Override
   public void setParentObject(final TKdataObject obj){
      this.prelevement = ((Prelevement) obj);
   }

   @Override
   public void prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getEchantillonManager().isUsedObjectManager(getObject());
      final boolean isCessed = ManagerLocator.getEchantillonManager().isCessedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      if(!isCessed){
         if(isUsed){
            setDeleteMessage(Labels.getLabel("echantillon.deletion.isUsedCascade"));
         }
      }else{ //objet non supprimable car cédé
         setDeleteMessage(Labels.getLabel("echantillon.deletion.isUsedNonCascade"));
      }
      setFantomable(!isCessed);
      setDeletable(!isCessed);
      setCascadable(!isCessed && isUsed);
   }

   @Override
   public void removeObject(final String comments){
      final List<File> filesToDelete = new ArrayList<>();
      if(!isCascadable()){
         ManagerLocator.getEchantillonManager().removeObjectManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope), filesToDelete);
      }else{
         ManagerLocator.getEchantillonManager().removeObjectCascadeManager(getObject(), comments,
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
    * Méthode initialisant les champs de formulaire pour la quantité.
    */
   public void initQuantite(){
      final StringBuffer sb = new StringBuffer();
      if(this.echantillon.getQuantite() != null){
         sb.append(this.echantillon.getQuantite());
      }else{
         sb.append("-");
      }
      sb.append(" / ");
      if(this.echantillon.getQuantiteInit() != null){
         sb.append(this.echantillon.getQuantiteInit());
      }else{
         sb.append("-");
      }
      if(this.echantillon.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.echantillon.getQuantiteUnite().getUnite());
      }
      valeurQuantite = sb.toString();

   }

   /**
    * Méthode initialisant le champs de formulaire pour le délai
    * de congélation.
    */
   public void initDelaiCgl(){

      if(this.echantillon.getDelaiCgl() != null && this.echantillon.getDelaiCgl() > -1){
         delaiLabel = ObjectTypesFormatters.getHeureMinuteLabel(this.echantillon.getDelaiCgl().intValue());
      }else{
         delaiLabel = Labels.getLabel("ficheEchantillon.delaiCgl.inconnu");
      }
   }

   public String getDatePrelevementFormated(){
      return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDatePrelevement());
   }

   public String getDatecongelationFormated(){
      return ObjectTypesFormatters.dateRenderer2(this.echantillon.getDateStock());
   }

   public String getSterileFormated(){
      return ObjectTypesFormatters.booleanLitteralFormatter(this.echantillon.getSterile());
   }

   public String getConformeTraitementFormated(){
      final StringBuffer sb = new StringBuffer();
      if(this.echantillon != null){
         if(this.echantillon.getConformeTraitement() != null){
            sb.append(ObjectTypesFormatters.booleanLitteralFormatter(this.echantillon.getConformeTraitement()));

            if(this.echantillon.getConformeTraitement() != null && !this.echantillon.getConformeTraitement()){
               sb.append(" - ");
               final List<ObjetNonConforme> list =
                  ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(echantillon, "Traitement");

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
      if(this.echantillon != null){
         if(this.echantillon.getConformeCession() != null){
            sb.append(ObjectTypesFormatters.booleanLitteralFormatter(this.echantillon.getConformeCession()));

            if(this.echantillon.getConformeCession() != null && !this.echantillon.getConformeCession()){
               sb.append(" - ");
               final List<ObjetNonConforme> list =
                  ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(echantillon, "Cession");

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

   public String getTumoralFormated(){
      return ObjectTypesFormatters.booleanLitteralFormatter(this.echantillon.getTumoral());
   }

   /**
    * Renvoie la valeur de lateralite internationalisée
    * si non nulle.
    */
   public String getLateraliteLabel(){
      if(echantillon.getLateralite() != null){
         return Labels.getLabel("echantillon.lateralite." + echantillon.getLateralite());
      }
         return null;
      }

   public String getSClassCession(){
      if(getDroitsConsultation().get("Cession")){
         return "formLink";
      }
         return null;
      }

   public String getSClassOperateur(){
      if(this.echantillon != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.echantillon.getCollaborateur());
      }
         return null;
      }

   public String getSClassStockage(){
      if(isCanStockage()){
         return "formLink";
      }
         return "formValue";
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
    * groupDerivesEchan.
    */
   public void onOpen$groupDerivesEchan(){
      final String id = groupDerivesEchan.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");

   }

   /**
    * Cett méthode descend la barre de scroll au niveau du groupe
    * groupCessionsEchan.
    */
   public void onOpen$groupCessionsEchan(){
      final String id = groupCessionsEchan.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Clic sur le bouton printINCa.
    */
   public void onClick$printINCa(){
      openHeadersWindow(page, self);
   }

   public void onGetHeaders(final Event event){
      if(event.getData() != null){
         final String[] headers = (String[]) event.getData();
         if(headers[0] != null){
            hautPageInca = headers[0];
         }else{
            hautPageInca = null;
         }

         if(headers[1] != null){
            piedPageInca = headers[1];
         }else{
            piedPageInca = null;
         }
      }else{
         hautPageInca = null;
         piedPageInca = null;
      }
      Clients.showBusy(Labels.getLabel("impression.encours"));
      Events.echoEvent("onLaterPrintINCa", self, null);
   }

   /**
    * Génère la fiche INCa et la télécharge.
    */
   public void onLaterPrintINCa(){
      // création du document XML contenant les données à imprimer
      final Document document = createDocumentXMLForINCa();

      // Transformation du document en fichier
      byte[] dl = null;
      try{
         dl = ManagerLocator.getXmlUtils().creerPdf(document);

      }catch(final Exception e){
         log.error(e);
      }

      // ferme wait message
      Clients.clearBusy();

      // génération du nom du fichier
      final StringBuffer sb = new StringBuffer();
      final Calendar cal = Calendar.getInstance();
      final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
      sb.append("fiche_INCa");
      sb.append(date);
      sb.append(".pdf");

      // envoie du fichier à imprimer à l'utilisateur
      if(dl != null){
         Filedownload.save(dl, "application/pdf", sb.toString());
         dl = null;
      }
   }

   /**
    * Génère le Document JDOM contenant les infos INCa à imprimer.
    * @return Document JDOM.
    */
   public Document createDocumentXMLForINCa(){

      final Document document = ManagerLocator.getXmlUtils().createJDomDocument();
      final Element root = document.getRootElement();

      // ajout de la date en pied de page
      final StringBuffer sb = new StringBuffer();
      final Calendar cal = Calendar.getInstance();
      final String date = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
      sb.append(date);

      if(piedPageInca != null && piedPageInca.length() > 0){
         sb.append(" - ");
         sb.append(piedPageInca);
      }

      if(hautPageInca == null || hautPageInca.length() == 0){
         hautPageInca = " ";
      }

      ManagerLocator.getXmlUtils().addBasDePage(root, sb.toString());
      final StringBuffer adrImage = new StringBuffer();
      adrImage.append("/images/icones/catalogues/INCA_Logo.jpg");
      ManagerLocator.getXmlUtils().addHautDePage(root, hautPageInca, true,
         ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getServletContext().getRealPath(adrImage.toString()));

      nbItemsINCaRemplis = 0;
      nbItemsINCaTotaux = 0;
      final List<Paragraphe> prgs = new ArrayList<>();
      prgs.add(addINCaPatientData());
      prgs.add(addINCaMaladieData());
      prgs.add(addINCaPrelevementData());
      prgs.add(addINCaEchantillonData());
      prgs.add(addINCaRessourcesBiologiquesData());
      prgs.add(addINCaComplementairesData());
      // les items tabac ne seront affichés que si les TableAnnotations
      // sont associées aux banques
      final TableAnnotation tabacPatient =
         ManagerLocator.getTableAnnotationManager().findByNomLikeManager("INCa-Patient-Tabac", true).get(0);
      final TableAnnotation tabacEchan =
         ManagerLocator.getTableAnnotationManager().findByNomLikeManager("INCa-Echantillon-Tabac", true).get(0);
      final List<TableAnnotation> tabs =
         ManagerLocator.getTableAnnotationManager().findByBanquesManager(SessionUtils.getSelectedBanques(sessionScope), true);
      if(tabs.contains(tabacPatient) || tabs.contains(tabacEchan)){
         prgs.add(addINCaTabacData());
      }

      final int pourcentage = (nbItemsINCaRemplis * 100) / nbItemsINCaTotaux;

      final Element page1 = ManagerLocator.getXmlUtils().addPage(root, ObjectTypesFormatters.getLabel("impression.inca.titre",
         new String[] {echantillon.getCode(), String.valueOf(pourcentage)}));

      for(int i = 0; i < 6; i++){
         ManagerLocator.getXmlUtils().addParagraphe(page1, prgs.get(i));
      }

      return document;
   }

   /**
    * Ajoute les informations INCa relatives au patient.
    * @param page
    */
   public Paragraphe addINCaPatientData(){

      String tmp = "";

      Patient patient = null;
      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
      if(prlvt != null && prlvt.getMaladie() != null && prlvt.getMaladie().getPatient() != null){
         patient = prlvt.getMaladie().getPatient();
      }

      // Site de soin
      ++nbItemsINCaTotaux;
      if(prelevement.getServicePreleveur() != null && prelevement.getServicePreleveur().getEtablissement() != null){
         tmp = prelevement.getServicePreleveur().getEtablissement().getNom();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("recherche.inca.item.1"), tmp);
      final LigneDeuxColonnesParagraphe li1 = new LigneDeuxColonnesParagraphe(cp1);

      // Date de naissance
      ++nbItemsINCaTotaux;
      if(patient != null && patient.getDateNaissance() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(patient.getDateNaissance());
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("recherche.inca.item.3"), tmp);
      // Sexe
      ++nbItemsINCaTotaux;
      if(patient != null && patient.getSexe() != null){
         tmp = PatientUtils.setSexeFromDBValue(patient);
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("recherche.inca.item.4"), tmp);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp3, cp4});

      // Etat
      ++nbItemsINCaTotaux;
      if(patient != null && patient.getPatientEtat() != null){
         tmp = PatientUtils.setEtatFromDBValue(patient);
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("recherche.inca.item.5"), tmp);
      // Date état
      ++nbItemsINCaTotaux;
      if(patient != null && patient.getDateEtat() != null){
         tmp = PatientUtils.getDateDecesOrEtat(patient);
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("recherche.inca.item.6"), tmp);
      final LigneParagraphe li4 = new LigneParagraphe("", new CoupleValeur[] {cp5, cp6});

      // Cause du décès
      /*CoupleValeur cp7 = createCoupleForAnnotation(
      		"076 : Cause du décès", patient, false);*/
      final CoupleValeur cp7 =
         (CoupleValeur) createCoupleForAnnotation("076 : Cause du décès", "recherche.inca.item.76", patient, false, false);
      final LigneDeuxColonnesParagraphe li5 = new LigneDeuxColonnesParagraphe(cp7);

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.patient"),
         new Object[] {li1, li3, li4, li5}, null, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa relatives à la maladie.
    * @param page
    */
   public Paragraphe addINCaMaladieData(){
      String tmp = "";

      Maladie maladie = null;
      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
      if(prlvt != null && prlvt.getMaladie() != null){
         maladie = prlvt.getMaladie();
      }

      // Code diagnostic
      ++nbItemsINCaTotaux;
      final StringBuffer diag = new StringBuffer();
      if(maladie != null && maladie.getCode() != null){
         diag.append(maladie.getCode());
         ++nbItemsINCaRemplis;
      }else{
         diag.append("-");
      }
      if(maladie != null && maladie.getLibelle() != null){
         diag.append(" (");
         diag.append(maladie.getLibelle());
         diag.append(")");
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("recherche.inca.item.7"), diag.toString());
      final LigneDeuxColonnesParagraphe lid = new LigneDeuxColonnesParagraphe(cp1);

      // Date diagnostic
      ++nbItemsINCaTotaux;
      if(maladie != null && maladie.getDateDiagnostic() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(maladie.getDateDiagnostic());
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("recherche.inca.item.8"), tmp);
      final LigneDeuxColonnesParagraphe lid2 = new LigneDeuxColonnesParagraphe(cp2);

      // version cTNM
      final CoupleValeur cp3 =
         (CoupleValeur) createCoupleForAnnotation("009 : version cTNM", "recherche.inca.item.9", prlvt, false, false);
      // Taille de la tumeur
      final CoupleValeur cp4 = (CoupleValeur) createCoupleForAnnotation("010 : Taille de la tumeur : cT",
         "recherche.inca.item.10", prlvt, false, false);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp3, cp4});

      // Envahissement ganglionnaire
      final CoupleValeur cp5 = (CoupleValeur) createCoupleForAnnotation("011 : Envahissement ganglionnaire : cN",
         "recherche.inca.item.11", prlvt, false, false);
      // Extension métastatique
      final CoupleValeur cp6 = (CoupleValeur) createCoupleForAnnotation("012 : Extension métastatique : cM",
         "recherche.inca.item.12", prlvt, false, false);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp5, cp6});

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.maladie"),
         new Object[] {lid, lid2, li2, li3}, null, null, null);
      return par1;
   }

   /**
    * Ajoute les informations INCa relatives au Prelevement.
    * @param page
    */
   public Paragraphe addINCaPrelevementData(){

      String tmp = "";

      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);

      // centre de stockage
      ++nbItemsINCaTotaux;
      final Emplacement empl = ManagerLocator.getEchantillonManager().getEmplacementManager(echantillon);
      if(empl != null){
         final Conteneur cont = ManagerLocator.getEmplacementManager().getConteneurManager(empl);
         if(cont.getService() != null && cont.getService().getEtablissement() != null){
            tmp = cont.getService().getEtablissement().getNom();
            ++nbItemsINCaRemplis;
         }
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("recherche.inca.item.13"), tmp);
      final LigneDeuxColonnesParagraphe ldc1 = new LigneDeuxColonnesParagraphe(cp1);

      // Contact
      ++nbItemsINCaTotaux;
      if(echantillon.getBanque() != null && echantillon.getBanque().getContact() != null){
         tmp = echantillon.getBanque().getContact().getNomAndPrenom();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("recherche.inca.item.66"), tmp);
      final LigneDeuxColonnesParagraphe ldc2 = new LigneDeuxColonnesParagraphe(cp2);

      // Mail + tel
      ++nbItemsINCaTotaux;
      ++nbItemsINCaTotaux;
      Set<Coordonnee> coords = new HashSet<>();
      if(echantillon.getBanque().getContact() != null){
         coords = ManagerLocator.getCollaborateurManager().getCoordonneesManager(echantillon.getBanque().getContact());
      }
      final Iterator<Coordonnee> it = coords.iterator();
      final StringBuffer mail = new StringBuffer();
      final StringBuffer tel = new StringBuffer();
      boolean hasTel = false;
      boolean hasMail = false;
      while(it.hasNext()){
         final Coordonnee coord = it.next();
         if(coord.getMail() != null && !coord.getMail().equals("")){
            mail.append(coord.getMail());
            hasMail = true;
            if(it.hasNext()){
               mail.append(", ");
            }
         }
         if(coord.getTel() != null && !coord.getTel().equals("")){
            tel.append(coord.getTel());
            hasTel = true;
            if(it.hasNext()){
               tel.append(", ");
            }
         }
      }
      if(hasMail){
         ++nbItemsINCaRemplis;
      }
      if(hasTel){
         ++nbItemsINCaRemplis;
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("recherche.inca.item.67"), mail.toString());
      final LigneDeuxColonnesParagraphe ldc3 = new LigneDeuxColonnesParagraphe(cp3);
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("recherche.inca.item.68"), tel.toString());
      final LigneDeuxColonnesParagraphe ldc4 = new LigneDeuxColonnesParagraphe(cp4);

      // ID prelevement
      ++nbItemsINCaTotaux;
      if(prlvt != null && prlvt.getCode() != null){
         tmp = prlvt.getCode();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("recherche.inca.item.14"), tmp);
      final LigneDeuxColonnesParagraphe ldc5 = new LigneDeuxColonnesParagraphe(cp5);

      // Date prlvt
      ++nbItemsINCaTotaux;
      if(prlvt != null && prlvt.getDatePrelevement() != null){
         tmp = ObjectTypesFormatters.dateRenderer2(prlvt.getDatePrelevement());
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("recherche.inca.item.15"), tmp);
      // prele type
      ++nbItemsINCaTotaux;
      if(prlvt != null && prlvt.getPrelevementType() != null){
         tmp = prlvt.getPrelevementType().getType();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("recherche.inca.item.16"), tmp);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp6, cp7});

      // Classification utlisée
      ++nbItemsINCaTotaux;
      StringBuffer sb = new StringBuffer();
      if(prlvt != null){
         final List<BanqueTableCodage> btcs =
            ManagerLocator.getBanqueManager().getBanqueTableCodageByBanqueManager(prlvt.getBanque());
         for(int i = 0; i < btcs.size(); i++){
            sb.append(btcs.get(i).getTableCodage().getNom());
            if(i < btcs.size() - 1){
               sb.append(", ");
            }
         }
      }
      if(!sb.toString().equals("")){
         tmp = sb.toString();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("recherche.inca.item.17"), tmp);
      final LigneDeuxColonnesParagraphe li3 = new LigneDeuxColonnesParagraphe(cp8);

      // Organe
      ++nbItemsINCaTotaux;
      List<String> codes = ManagerLocator.getCodeAssigneManager()
         .formatCodesAsStringsManager(ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echantillon));
      sb = new StringBuffer();
      for(int i = 0; i < codes.size(); i++){
         sb.append(codes.get(i));
         if(i + 1 < codes.size()){
            sb.append(", ");
         }
      }
      if(!sb.toString().equals("")){
         tmp = sb.toString();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp9 = new CoupleValeur(Labels.getLabel("recherche.inca.item.18.20"), tmp);
      final LigneDeuxColonnesParagraphe ldc6 = new LigneDeuxColonnesParagraphe(cp9);
      // Codes lésionnels
      ++nbItemsINCaTotaux;
      codes = ManagerLocator.getCodeAssigneManager()
         .formatCodesAsStringsManager(ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echantillon));
      sb = new StringBuffer();
      for(int i = 0; i < codes.size(); i++){
         sb.append(codes.get(i));
         if(i + 1 < codes.size()){
            sb.append(", ");
         }
      }
      if(!sb.toString().equals("")){
         tmp = sb.toString();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp10 = new CoupleValeur(Labels.getLabel("recherche.inca.item.19.21"), tmp);
      final LigneDeuxColonnesParagraphe li4 = new LigneDeuxColonnesParagraphe(cp10);

      // Type évènement
      final CoupleValeur cp11 =
         (CoupleValeur) createCoupleForAnnotation("022 : Type évènement", "recherche.inca.item.22", prlvt, false, false);
      final LigneDeuxColonnesParagraphe ldc7 = new LigneDeuxColonnesParagraphe(cp11);

      // Version du pTNM
      final CoupleValeur cp12 =
         (CoupleValeur) createCoupleForAnnotation("023 : Version du pTNM", "recherche.inca.item.23", prlvt, false, false);
      // Taille de la tumeur primitive
      final CoupleValeur cp13 = (CoupleValeur) createCoupleForAnnotation("024 : Taille de la tumeur primitive : pT",
         "recherche.inca.item.24", prlvt, false, false);
      final LigneParagraphe li5 = new LigneParagraphe("", new CoupleValeur[] {cp12, cp13});

      // Envahissement ganglionnaire
      final CoupleValeur cp14 = (CoupleValeur) createCoupleForAnnotation("025 : Envahissement ganglionnaire : pN",
         "recherche.inca.item.25", prlvt, false, false);
      // Extension métastatique
      final CoupleValeur cp15 = (CoupleValeur) createCoupleForAnnotation("026 : Extension métastatique : pM",
         "recherche.inca.item.26", prlvt, false, false);
      final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp14, cp15});

      final SousParagraphe sousPar1 =
         new SousParagraphe("", new Object[] {ldc5, li2, li3, ldc6, li4, ldc7, li5, li6}, null, null);

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.prelevement"),
         new Object[] {ldc1, ldc2, ldc3, ldc4}, new SousParagraphe[] {sousPar1}, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa relatives au Echantillon.
    * @param page
    */
   public Paragraphe addINCaEchantillonData(){

      String tmp = "";

      // Tumoral
      ++nbItemsINCaTotaux;
      if(echantillon.getTumoral() != null){
         tmp = ObjectTypesFormatters.booleanLitteralFormatter(echantillon.getTumoral());
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("recherche.inca.item.27.39"), tmp);
      // Température
      ++nbItemsINCaTotaux;
      final Float temp = getTemp();
      if(temp != null && temp != 0){
         ++nbItemsINCaRemplis;
         if(temp == -20){
            tmp = "-20°C";
         }else if(temp == -80){
            tmp = "-80°C";
         }else if(temp <= -140 && temp > -196){
            tmp = "-140°C et <";
         }else if(temp == -196){
            tmp = "azote";
         }else{
            tmp = "autre";
         }
      }else{
         tmp = "-";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("recherche.inca.item.28.40"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Type
      ++nbItemsINCaTotaux;
      if(echantillon.getEchantillonType() != null){
         if(echantillon.getEchantillonType().getType().toLowerCase().contains("tissu")){
            tmp = "tissu";
         }else if(echantillon.getEchantillonType().getType().toLowerCase().contains("cellule")){
            tmp = "cellules";
         }else{
            tmp = "autre";
         }
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("recherche.inca.item.29.41"), tmp);
      // Mode préparation
      ++nbItemsINCaTotaux;
      if(echantillon.getModePrepa() != null){
         if(echantillon.getModePrepa().getNom().toLowerCase().contains("dmso")){
            tmp = "DMSO";
         }else if(echantillon.getModePrepa().getNom().toLowerCase().contains("culot")){
            tmp = "culot";
         }else if(echantillon.getModePrepa().getNom().toLowerCase().contains("tissu")){
            tmp = "tissu";
         }else{
            tmp = "autre";
         }
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("recherche.inca.item.30.42"), tmp);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp3, cp4});

      // Délai cgl
      ++nbItemsINCaTotaux;
      if(echantillon.getDelaiCgl() != null && echantillon.getDelaiCgl() >= 0){
         if(echantillon.getDelaiCgl() <= 30){
            tmp = "jusqu'à 30min";
         }else{
            tmp = "plus de 30min";
         }
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("recherche.inca.item.31.43"), tmp);
      // Contrôle sur tissu
      final CoupleValeur cp6 = (CoupleValeur) createCoupleForAnnotation("032/044 : Contrôle sur tissu",
         "recherche.inca.item.32.44", echantillon, false, false);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp5, cp6});

      // Quantité
      ++nbItemsINCaTotaux;
      if(echantillon.getQuantite() != null){
         tmp = String.valueOf(echantillon.getQuantite());
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("recherche.inca.item.33.45"), tmp);
      // Unité
      ++nbItemsINCaTotaux;
      if(echantillon.getQuantiteUnite() != null){
         tmp = echantillon.getQuantiteUnite().getUnite();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("recherche.inca.item.34.46"), tmp);
      final LigneParagraphe li4 = new LigneParagraphe("", new CoupleValeur[] {cp7, cp8});

      // Pourcentage de cellules tumorales
      final List<ChampAnnotation> champs =
         ManagerLocator.getChampAnnotationManager().findByNomManager("035 : Pourcentage de cellules tumorales");

      // s'il y a des résultats
      Integer value = null;
      if(champs.size() > 0){
         ++nbItemsINCaTotaux;
         final ChampAnnotation chp = champs.get(0);

         // on extrait les valeurs
         final List<AnnotationValeur> avs =
            ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(chp, echantillon);
         //StringBuffer sb = new StringBuffer();
         if(avs.size() > 0){
            final Float v = Float.valueOf(avs.get(0).getAlphanum());
            if(v != null){
               value = v.intValue();
            }
         }

         if(value != null){
            tmp = value.toString();
            ++nbItemsINCaRemplis;
         }else{
            tmp = "-";
         }
      }
      final CoupleValeur cp9 = new CoupleValeur(Labels.getLabel("recherche.inca.item.35"), tmp);
      // ADN dérivé
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      List<ProdDerive> derivesTmp = ManagerLocator.getProdDeriveManager().findByParentAndTypeManager(echantillon, "ADN");
      final CoupleValeur cp10 = new CoupleValeur(Labels.getLabel("recherche.inca.item.36.47"),
         ObjectTypesFormatters.booleanLitteralFormatter(derivesTmp.size() > 0));
      final LigneParagraphe li5 = new LigneParagraphe("", new CoupleValeur[] {cp9, cp10});

      // ARN
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      derivesTmp = ManagerLocator.getProdDeriveManager().findByParentAndTypeManager(echantillon, "ARN");
      final CoupleValeur cp11 = new CoupleValeur(Labels.getLabel("recherche.inca.item.37.48"),
         ObjectTypesFormatters.booleanLitteralFormatter(derivesTmp.size() > 0));
      // Protéine dérivé
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      derivesTmp = ManagerLocator.getProdDeriveManager().findByParentAndTypeManager(echantillon, "PROTEINE");
      final CoupleValeur cp12 = new CoupleValeur(Labels.getLabel("recherche.inca.item.38.49"),
         ObjectTypesFormatters.booleanLitteralFormatter(derivesTmp.size() > 0));
      final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp11, cp12});

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.echantillon"),
         new LigneParagraphe[] {li1, li2, li3, li4, li5, li6}, null, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa relatives aus ressources
    * biologiques.
    * @param page
    */
   public Paragraphe addINCaRessourcesBiologiquesData(){
      // Sérum
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("recherche.inca.item.50"), ObjectTypesFormatters
         .booleanLitteralFormatter(ManagerLocator.getEchantillonManager().itemINCa50To53Manager(echantillon, "SERUM%")));
      // Plasma
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("recherche.inca.item.51"), ObjectTypesFormatters
         .booleanLitteralFormatter(ManagerLocator.getEchantillonManager().itemINCa50To53Manager(echantillon, "PLASMA%")));
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Liquides
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("recherche.inca.item.52"), ObjectTypesFormatters
         .booleanLitteralFormatter(ManagerLocator.getEchantillonManager().itemINCa50To53Manager(echantillon, "LIQUIDE%")));
      // ADN
      ++nbItemsINCaTotaux;
      ++nbItemsINCaRemplis;
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("recherche.inca.item.53"), ObjectTypesFormatters
         .booleanLitteralFormatter(ManagerLocator.getEchantillonManager().itemINCa50To53Manager(echantillon, "ADN%")));
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp3, cp4});

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.biologique"),
         new LigneParagraphe[] {li1, li2}, null, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa relatives aux infos
    * complementaires.
    * @param page
    */
   public Paragraphe addINCaComplementairesData(){
      String tmp = "";

      Patient patient = null;
      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
      if(prlvt != null && prlvt.getMaladie() != null && prlvt.getMaladie().getPatient() != null){
         patient = prlvt.getMaladie().getPatient();
      }

      // CR anapath standardisé interrogeable
      final CoupleSimpleValeur cp1 = (CoupleSimpleValeur) createCoupleForAnnotation("054 : CR anapath standardisé interrogeable",
         "recherche.inca.item.54", prlvt, false, true);
      final LigneSimpleParagraphe li1 = new LigneSimpleParagraphe(cp1);

      // Données cliniques disponibles dans une base
      final CoupleSimpleValeur cp2 =
         (CoupleSimpleValeur) createCoupleForAnnotation("055 : Données cliniques disponibles dans une base",
            "recherche.inca.item.55", patient, false, true);
      final LigneSimpleParagraphe li2 = new LigneSimpleParagraphe(cp2);

      // Inclusion dans protcole thérapeutique
      final CoupleSimpleValeur cp3 =
         (CoupleSimpleValeur) createCoupleForAnnotation("056 : Inclusion dans un protocole thérapeutique",
            "recherche.inca.item.56", patient, false, true);
      final LigneSimpleParagraphe li3 = new LigneSimpleParagraphe(cp3);

      // Nom du protocole thérapeutique
      final CoupleSimpleValeur cp4 = (CoupleSimpleValeur) createCoupleForAnnotation("057 : Nom du protocole thérapeutique",
         "recherche.inca.item.57", patient, false, true);
      final LigneSimpleParagraphe li4 = new LigneSimpleParagraphe(cp4);

      // Caryotype
      final CoupleSimpleValeur cp5 =
         (CoupleSimpleValeur) createCoupleForAnnotation("058 : Caryotype", "recherche.inca.item.58", patient, false, true);
      final LigneSimpleParagraphe li5 = new LigneSimpleParagraphe(cp5);

      // Anomalie éventuelle
      final CoupleSimpleValeur cp6 = (CoupleSimpleValeur) createCoupleForAnnotation("059 : Anomalie éventuelle",
         "recherche.inca.item.59", patient, false, true);
      final LigneSimpleParagraphe li6 = new LigneSimpleParagraphe(cp6);

      // Anomalie génomique
      final CoupleSimpleValeur cp7 = (CoupleSimpleValeur) createCoupleForAnnotation("060 : Anomalie génomique",
         "recherche.inca.item.60", patient, false, true);
      final LigneSimpleParagraphe li7 = new LigneSimpleParagraphe(cp7);

      // Description anomalie génomique
      final CoupleSimpleValeur cp8 = (CoupleSimpleValeur) createCoupleForAnnotation("061 : Description anomalie génomique",
         "recherche.inca.item.61", patient, false, true);
      final LigneSimpleParagraphe li8 = new LigneSimpleParagraphe(cp8);

      // Controle qualité
      tmp = ObjectTypesFormatters.booleanLitteralFormatter(echantillon.getEchanQualite() != null);
      final CoupleSimpleValeur cp9 = new CoupleSimpleValeur(Labels.getLabel("recherche.inca.item.62"), tmp);
      final LigneSimpleParagraphe li9 = new LigneSimpleParagraphe(cp9);

      // Inclusion dans protcole recherche
      final CoupleSimpleValeur cp10 =
         (CoupleSimpleValeur) createCoupleForAnnotation("063 : Inclusion dans un programme de recherche",
            "recherche.inca.item.63", prlvt, false, true);
      final LigneSimpleParagraphe li10 = new LigneSimpleParagraphe(cp10);

      // Nom du programme de recherche
      final CoupleSimpleValeur cp11 = (CoupleSimpleValeur) createCoupleForAnnotation("064 : Nom du programme de recherche",
         "recherche.inca.item.64", prlvt, false, true);
      final LigneSimpleParagraphe li11 = new LigneSimpleParagraphe(cp11);

      // Description anomalie génomique
      final CoupleSimpleValeur cp12 = (CoupleSimpleValeur) createCoupleForAnnotation("065 : Champs spécifique du type cancer",
         "recherche.inca.item.65", prlvt, false, true);
      final LigneSimpleParagraphe li12 = new LigneSimpleParagraphe(cp12);

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.complementaires"),
         new Object[] {li1, li2, li3, li4, li5, li6, li7, li8, li9, li10, li11, li12}, null, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa relatives au Prelevement.
    * @param page
    */
   public Paragraphe addINCaContactData(){

      String tmp = "";
      final CoupleValeur cp2 = new CoupleValeur("", "");

      // Contact
      ++nbItemsINCaTotaux;
      if(echantillon.getBanque() != null && echantillon.getBanque().getContact() != null){
         tmp = echantillon.getBanque().getContact().getNomAndPrenom();
         ++nbItemsINCaRemplis;
      }else{
         tmp = "-";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("recherche.inca.item.66"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Mail + tel
      ++nbItemsINCaTotaux;
      ++nbItemsINCaTotaux;
      Set<Coordonnee> coords = new HashSet<>();
      if(echantillon.getBanque().getContact() != null){
         coords = ManagerLocator.getCollaborateurManager().getCoordonneesManager(echantillon.getBanque().getContact());
      }
      final Iterator<Coordonnee> it = coords.iterator();
      final StringBuffer mail = new StringBuffer();
      final StringBuffer tel = new StringBuffer();
      boolean hasTel = false;
      boolean hasMail = false;
      while(it.hasNext()){
         final Coordonnee coord = it.next();
         if(coord.getMail() != null && !coord.getMail().equals("")){
            mail.append(coord.getMail());
            hasMail = true;
            if(it.hasNext()){
               mail.append(", ");
            }
         }
         if(coord.getTel() != null && !coord.getTel().equals("")){
            tel.append(coord.getTel());
            hasTel = true;
            if(it.hasNext()){
               tel.append(", ");
            }
         }
      }
      if(hasMail){
         ++nbItemsINCaRemplis;
      }
      if(hasTel){
         ++nbItemsINCaRemplis;
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("recherche.inca.item.67"), mail.toString());
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("recherche.inca.item.68"), tel.toString());
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp3, cp4});

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.contact"),
         new LigneParagraphe[] {li1, li2}, null, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa Tabac.
    * @param page
    */
   public Paragraphe addINCaTabacData(){
      //String tmp = "";
      //CoupleValeur cpVide = new CoupleValeur("", "");

      Patient patient = null;
      final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon);
      if(prlvt != null && prlvt.getMaladie() != null && prlvt.getMaladie().getPatient() != null){
         patient = prlvt.getMaladie().getPatient();
      }

      // CR anapath standardisé interrogeable
      final CoupleValeur cp1 = createCoupleForAnnotation("069 : Disponibilité questionnaire antécédents tabac", patient, false);
      // Données cliniques disponibles dans une base
      final CoupleValeur cp2 = createCoupleForAnnotation("070 : Disponibilté questionnaire familial", patient, false);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});

      // Disponibilté questionnaire professionnel
      final CoupleValeur cp3 = createCoupleForAnnotation("071 : Disponibilté questionnaire professionnel", patient, false);
      // Echantillon radio-naïf
      final CoupleValeur cp4 = createCoupleForAnnotation("072 : Echantillon radio-naïf", echantillon, false);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp3, cp4});

      // Echantillon chimio-naïf
      final CoupleValeur cp5 = createCoupleForAnnotation("073 : Echantillon chimio-naïf", echantillon, false);
      // Statut tabac approfondi
      final CoupleValeur cp6 = createCoupleForAnnotation("074 : Statut tabac approfondi", patient, false);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp5, cp6});

      // NPA
      final CoupleValeur cp7 = createCoupleForAnnotation("075 : NPA", patient, false);
      // Cause du décès
      final CoupleValeur cp8 = createCoupleForAnnotation("076 : Cause du décès", patient, false);
      final LigneParagraphe li4 = new LigneParagraphe("", new CoupleValeur[] {cp7, cp8});

      final Paragraphe par1 = new Paragraphe(Labels.getLabel("recherche.inca.renseignements.specifique"),
         new LigneParagraphe[] {li1, li2, li3, li4}, null, null, null);

      return par1;
   }

   /**
    * Ajoute les informations INCa relatives au site.
    * @param page
    */
   public Paragraphe addINCaSiteData(){

      String tmp = "";
      nbItemsINCaTotaux++;
      // Site de gestion
      if(prelevement != null && prelevement.getServicePreleveur() != null
         && prelevement.getServicePreleveur().getEtablissement() != null){
         tmp = prelevement.getServicePreleveur().getEtablissement().getNom();
         nbItemsINCaRemplis++;
      }else{
         tmp = "";
      }
      final CoupleValeur cpS = new CoupleValeur(Labels.getLabel("recherche.inca.item.1"), tmp);
      final CoupleValeur cp2 = new CoupleValeur("", "");
      final LigneParagraphe liS = new LigneParagraphe("", new CoupleValeur[] {cpS, cp2});

      final Paragraphe par1 =
         new Paragraphe(Labels.getLabel("recherche.inca.renseignements.site"), new LigneParagraphe[] {liS}, null, null, null);

      return par1;
   }

   /**
    * Crée un couple de valeurs pour un ChampAnnotation.
    * @param champNom Nom du ChampAnnotation.
    * @param champTitle Label permettant d'afficher le titre de cette
    * annotation.
    * @param obj Objet dont on veut la valeur.
    * @param obligatoire True si le couple est obligatoire.
    * @return Un CoupleValeur ou un CoupleSimpleValeur.
    */
   public Object createCoupleForAnnotation(final String champNom, final String champTitle, final Object obj,
      final boolean obligatoire, final boolean simpleCouple){
      // on récup les ChampAnnotations
      final List<ChampAnnotation> champs = ManagerLocator.getChampAnnotationManager().findByNomManager(champNom);

      CoupleValeur cv = new CoupleValeur("", "");
      CoupleSimpleValeur csv = new CoupleSimpleValeur("", "");

      // s'il y a des résultats
      if(champs.size() > 0){
         ++nbItemsINCaTotaux;
         final ChampAnnotation chp = champs.get(0);

         // on extrait les valeurs
         final List<AnnotationValeur> avs =
            ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(chp, (TKAnnotableObject) obj);
         final StringBuffer sb = new StringBuffer();
         for(int j = 0; j < avs.size(); j++){
            // formatage des valeurs
            sb.append(avs.get(j).formateAnnotationValeur());
            if(j + 1 < avs.size()){
               sb.append(";");
            }
         }

         String tmp = "";
         if(!sb.toString().equals("")){
            tmp = sb.toString();
            ++nbItemsINCaRemplis;
         }else{
            tmp = "-";
         }

         cv = new CoupleValeur(Labels.getLabel(champTitle), tmp);
         csv = new CoupleSimpleValeur(Labels.getLabel(champTitle), tmp);
      }
      cv.setObligatoire(obligatoire);
      csv.setObligatoire(obligatoire);

      if(!simpleCouple){
         return cv;
      }
         return csv;
      }

   /**
    * Crée un couple de valeurs pour un ChampAnnotation.
    * @param champNom Nom du ChampAnnotation.
    * @param obj Objet dont on veut la valeur.
    * @param obligatoire True si le couple est obligatoire.
    * @return Un CoupleValeur.
    */
   public CoupleValeur createCoupleForAnnotation(final String champNom, final Object obj, final boolean obligatoire){
      // on récup les ChampAnnotations
      final List<ChampAnnotation> champs = ManagerLocator.getChampAnnotationManager().findByNomManager(champNom);

      CoupleValeur cv = new CoupleValeur("", "");

      // s'il y a des résultats
      if(champs.size() > 0){
         ++nbItemsINCaTotaux;
         final ChampAnnotation chp = champs.get(0);

         // on extrait les valeurs
         final List<AnnotationValeur> avs =
            ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(chp, (TKAnnotableObject) obj);
         final StringBuffer sb = new StringBuffer();
         for(int j = 0; j < avs.size(); j++){
            // formatage des valeurs
            sb.append(avs.get(j).formateAnnotationValeur());
            if(j + 1 < avs.size()){
               sb.append(";");
            }
         }

         String tmp = "";
         if(!sb.toString().equals("")){
            tmp = sb.toString();
            ++nbItemsINCaRemplis;
         }else{
            tmp = "-";
         }

         cv = new CoupleValeur(chp.getNom(), tmp);
      }
      cv.setObligatoire(obligatoire);

      return cv;
   }

   /**
    * Ajoute les informations INCa relatives au Echantillon.
    * @param page
    */
   public List<LigneParagraphe> addINCaAnnotationData(final List<TableAnnotation> tabs, final Object obj){
      final List<LigneParagraphe> lignes = new ArrayList<>();
      final CoupleValeur cpVide = new CoupleValeur("", "");

      // pour chaque tableannotation
      for(int i = 0; i < tabs.size(); i++){
         // dessine les champs
         final Iterator<ChampAnnotation> champsIt =
            ManagerLocator.getTableAnnotationManager().getChampAnnotationsManager(tabs.get(i)).iterator();
         // pour chaque champ
         int cpt = 0;
         CoupleValeur cp1 = null;
         CoupleValeur cp2 = null;
         while(champsIt.hasNext()){
            final ChampAnnotation champ = champsIt.next();
            ++cpt;
            // si c'est le premier champ de la ligne
            if(cpt == 1){
               // on extrait les valeurs
               final List<AnnotationValeur> avs =
                  ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(champ, (TKAnnotableObject) obj);
               final StringBuffer sb = new StringBuffer();
               for(int j = 0; j < avs.size(); j++){
                  // formatage des valeurs
                  sb.append(avs.get(j).formateAnnotationValeur());
                  if(j + 1 < avs.size()){
                     sb.append(";");
                  }
               }
               cp1 = new CoupleValeur(champ.getNom(), sb.toString());
            }else{
               // on extrait les valeurs
               final List<AnnotationValeur> avs =
                  ManagerLocator.getAnnotationValeurManager().findByChampAndObjetManager(champ, (TKAnnotableObject) obj);
               final StringBuffer sb = new StringBuffer();
               for(int j = 0; j < avs.size(); j++){
                  // formatage des valeurs
                  sb.append(avs.get(j).formateAnnotationValeur());
                  if(j + 1 < avs.size()){
                     sb.append(";");
                  }
               }
               cp2 = new CoupleValeur(champ.getNom(), sb.toString());
            }

            // si la ligne est complète ou que c'est le dernier champ
            // création d'une nouvelle ligne
            if(cpt == 2){
               cpt = 0;
               final LigneParagraphe li = new LigneParagraphe("", new CoupleValeur[] {cp1, cp2});
               lignes.add(li);
            }else if(!champsIt.hasNext()){
               final LigneParagraphe li = new LigneParagraphe("", new CoupleValeur[] {cp1, cpVide});
               lignes.add(li);
            }
         }
      }

      return lignes;
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
    * @param event Event : clique sur un lien codePrlvtLabel.
    * @throws Exception
    */
   public void onClick$codePrlvtLabel(final Event event) throws Exception{
      if(getDroitsConsultation().get("Prelevement")){
         if(!getObjectTabController().getReferencingObjectControllers().isEmpty()){
            if(!(getObjectTabController().getReferencingObjectControllers().get(0) instanceof PrelevementController)){
               getMainWindow().createMacroComponent("/zuls/prelevement/Prelevement.zul", "winPrelevement",
                  (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("prelevementPanel"));
            }
         }
         getObjectTabController().getReferencingObjectControllers().get(0).switchToFicheStaticMode(getPrelevement());
         PrelevementController.backToMe(getMainWindow(), page);
      }
   }

   /**
    * Affiche la fiche du patient.
    */
   public void onClick$patientLabel(){
      if(getDroitsConsultation().get("Patient")){
         final Maladie maladie = prelevement.getMaladie();
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

   /**
    * Affiche la fiche d'un produit dérivé.
    * @param event Event : clique sur un code d'un dérivé dans
    * la liste des produits dérivés.
    * @throws Exception
    */
   public void onClickProdDeriveCode(final Event event){

      ProdDeriveController tabController = null;

      if(event != null){
         final ProdDerive derive = (ProdDerive) event.getData();
         if(getDroitListableObjectConsultation(derive, true)){

            // change la banque au besoin
            if(!SessionUtils.getSelectedBanques(sessionScope).contains(derive.getBanque())){
               getMainWindow().updateSelectedBanque(derive.getBanque());
            }

            tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

            tabController.switchToFicheStaticMode(derive);
         }
      }else{
         final List<ProdDerive> derives = new ArrayList<>();

         for(final ProdDerive pd : ManagerLocator.getEchantillonManager().getProdDerivesManager(echantillon)){
            if(getDroitListableObjectConsultation(pd, false)){
               derives.add(pd);
            }
         }

         if(!derives.isEmpty()){
            tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

            tabController.getListe().setListObjects(ManagerLocator.getEchantillonManager().getProdDerivesManager(echantillon));
            tabController.getListe().setCurrentObject(null);
            tabController.clearStaticFiche();
            tabController.getListe().getBinder().loadAttribute(tabController.getListe().getObjectsListGrid(), "model");
            tabController.switchToOnlyListeMode();
         }
      }
      if(tabController != null){
         tabController.getListe().updateListResultsLabel(null);
      }
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
      final List<TKStockableObject> objs = new ArrayList<>();
      objs.add(echantillon);
      if(!getObjStatutIncompatibleForRetour(objs, "TRANSFORMATION")){
         final ProdDeriveController tabController = (ProdDeriveController) ProdDeriveController.backToMe(getMainWindow(), page);

         tabController.switchToCreateMode(this.echantillon);
      }
   }

   /*public void onClick$print() {
   	StringBuffer sb = new StringBuffer();
   	sb.append(Labels.getLabel("impression.print.echantillon"));
   	sb.append(" ");
   	sb.append(this.echantillon.getCode());
   	
   	openImpressionWindow(page, echantillon, sb.toString(), isAnonyme());
   }*/

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
      drawActionsButtons("Echantillon");
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
         emplacementLabelEchan.addForward("onClick", self, "onClickObjectEmplacementFromFiche");
      }else{
         emplacementLabelEchan.removeForward("onClick", self, "onClickObjectEmplacementFromFiche");
      }

      //		List<String> entites = new ArrayList<String>();
      //		entites.add("Patient");
      //		entites.add("Prelevement");
      //		entites.add("ProdDerive");
      //		entites.add("Cession");
      //		setDroitsConsultation(drawConsultationLinks(entites));
      //		
      // si pas le droit d'accès aux dérivés, on cache le lien
      if(!getDroitsConsultation().get("ProdDerive")){
         prodDeriveRenderer.setAccessible(false);
         canAccessDerives = false;
      }else{
         prodDeriveRenderer.setAccessible(true);
         canAccessDerives = true;
      }
      prodDeriveRenderer.setAccessStockage(canStockage);

      boolean inca = false;
      if(sessionScope.containsKey("catalogues")){
         final Map<String, Boolean> catsMap = (Map<String, Boolean>) sessionScope.get("catalogues");

         if(catsMap.containsKey("INCa")){
            inca = true;
         }
      }
      printINCa.setVisible(inca);

      super.applyDroitsOnFiche();
      addDerive.setDisabled(!canCreateDerive);
      prodDeriveRenderer.setAnonyme(isAnonyme());
   }

   /**********************************************************************/
   /*********************** GETTERS **************************************/
   /**********************************************************************/

   public Echantillon getEchantillon(){
      return echantillon;
   }

   public Prelevement getPrelevement(){
      return prelevement;
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

   public String getProdDerivesGroupHeader(){
      return prodDerivesGroupHeader;
   }

   public String getCessionsGroupHeader(){
      return cessionsGroupHeader;
   }

   public String getSortiesGroupHeader(){
      return sortiesGroupHeader;
   }

   public String getEmplacementAdrl(){
      if(isAnonyme()){
         makeLabelAnonyme(emplacementLabelEchan, false);
         return getAnonymeString();
      }
         emplacementLabelEchan.setSclass("formValue");
         return emplacementAdrl;
      }

   public String getTemperatureFormated(){
      return ObjectTypesFormatters.formatTemperature(getTemp());
   }

   private Float getTemp(){
      if(this.echantillon != null && this.echantillon.getEchantillonId() != null){
         final Emplacement emp = ManagerLocator.getEchantillonManager().getEmplacementManager(echantillon);

         if(emp != null){
            final Conteneur cont = ManagerLocator.getEmplacementManager().getConteneurManager(emp);
            if(cont != null && cont.getTemp() != null){
               return cont.getTemp();
            }
         }
      }
      return null;
   }

   public static ProdDeriveRowRenderer getProdDeriveRenderer(){
      return prodDeriveRenderer;
   }

   /**
    * Lien vers le compte-rendu anapath.
    */
   public void onClick$anapathLabel(){
      if(echantillon.getCrAnapath() != null && !isAnonyme()){
         try( FileInputStream fis = new FileInputStream(echantillon.getCrAnapath().getPath());){
            Filedownload.save(fis, echantillon.getCrAnapath().getMimeType(), echantillon.getCrAnapath().getNom());
         }catch(final Exception e){
            log.error(e);
         }
      }
   }

   public String getDelaiLabel(){
      return delaiLabel;
   }

   public void setDelaiLabel(final String delai){
      this.delaiLabel = delai;
   }

   public Integer getNbItemsINCaTotaux(){
      return nbItemsINCaTotaux;
   }

   public void setNbItemsINCaTotaux(final Integer nbItems){
      this.nbItemsINCaTotaux = nbItems;
   }

   public Integer getNbItemsINCaRemplis(){
      return nbItemsINCaRemplis;
   }

   public void setNbItemsINCaRemplis(final Integer nbItems){
      this.nbItemsINCaRemplis = nbItems;
   }

   public String getNomPatient(){
      if(this.prelevement != null){
         return PrelevementUtils.getPatientNomAndPrenom(prelevement);
      }
         return null;
      }

   public String getHautPageInca(){
      return hautPageInca;
   }

   public void setHautPageInca(final String hautPage){
      this.hautPageInca = hautPage;
   }

   public String getPiedPageInca(){
      return piedPageInca;
   }

   public void setPiedPageInca(final String piedPage){
      this.piedPageInca = piedPage;
   }

   /**
    * Recoit l'évenement de clique sur le lien emplacement contenu 
    * dans la fiche statique.
    * @param event
    */
   public void onClickObjectEmplacementFromFiche(final Event event){
      if(getObject().getEchantillonId() != null){
         Events.postEvent("onClickObjectEmplacement", self,
            ManagerLocator.getEchantillonManager().getEmplacementManager(getObject()));
      }
   }

   //	/**
   //	 * Ouvre la modale listant les operations de sortie du système de stockage.
   //	 */
   //	public void onClick$retours() {
   //		openListRetourModale(getObject());
   //	}

   private void drawRisquesFormatted(){
      Components.removeAllChildren(risquesBox);
      if(getPrelevement() != null && getPrelevement().getPrelevementId() != null){
         final Iterator<Risque> risksIt = ManagerLocator.getPrelevementManager().getRisquesManager(getPrelevement()).iterator();
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

   /**
    * Recoit l'evenement venant de la liste de retours quand 
    * un élément est ajouté.
    */
   public void onClickUpdateSorties$retourRow(final Event event){
      //updateSortiesHeader(true);
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
         groupSortiesEchan.setLabel(sortiesGroupHeader);
      }
   }

   public String getObjetStatut(){
      return ObjectTypesFormatters.ILNObjectStatut(getObject().getObjetStatut());
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
   /*public FicheAnnotationInline getFicheAnnotationInline(){
   	if(self.getFellow("ficheAnnotationInlineEchantillon") != null){
   		return ((FicheAnnotationInline) self.getFellow("ficheAnnotationInlineEchantillon")
   			.getFellow("fwinAnnotationInline")
   			.getAttributeOrFellow("fwinAnnotationInline$composer", true));
   	}else{
   		return null;
   	}
   }*/

   /**
    * ANNOTATION INLINE - Bêta
    *
    * Passe qq params au bloc inline annotation sans le dessiner la creation de la
    * fiche statique.
    *
    * @param controller
    * @since 2.2.0
    */
   /*public void setObjectTabController(AbstractObjectTabController controller){
   	super.setObjectTabController(controller);
   	//
   	FicheAnnotation inline = getFicheAnnotationInline();
   	if(null != inline){
   		// passe l'entite au controller
   		getFicheAnnotationInline().setEntite(getObjectTabController().getEntiteTab());
   
   		// à remplacer par ce controller
   		// setFicheController
   		getFicheAnnotationInline().setObjectTabController(getObjectTabController());
   	}
   }*/
}
