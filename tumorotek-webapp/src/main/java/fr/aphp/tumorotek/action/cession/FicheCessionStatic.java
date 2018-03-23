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
package fr.aphp.tumorotek.action.cession;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.listmodel.ObjectPagingModel;
import fr.aphp.tumorotek.action.utils.CessionUtils;
import fr.aphp.tumorotek.decorator.CederObjetDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.factory.CederObjetDecoratorFactory;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import fr.aphp.tumorotek.webapp.general.export.Export;

public class FicheCessionStatic extends AbstractFicheStaticController
{

   //private Log log = LogFactory.getLog(FicheCessionStatic.class);

   private static final long serialVersionUID = -9020065604124760695L;

   // Groups
   private Group groupEchantillons;
   private Group groupProdDerives;

   private Row rowSanitaire;
   private Row rowRecherche1;
   private Row rowRecherche2;
   private Row rowDestruction;
   private Row rowDescription;
   private Row rowDestinataire;
   private Row rowService;
   private Row rowEtablissement;
   private Row rowSeparator1;
   private Row rowLine1;
   private Row rowSeparator2;
   private Row rowDateAndStatut;
   private Row rowStatutDestruction;
   private Row rowLine2;
   private Row rowExecutant;
   private Row rowDates;
   private Row rowTransporteurAndTemp;
   private Row rowSeparator3;
   // private Label codeProdDeriveCede;
   // private Label codeEchantillonCede;

   private Grid echantillonsList;
   private Paging echansPaging;
   private ObjectPagingModel echansModel;
   private final int _pageSizeE = 10;
   private int _startPageNumberE = 0;
   private int _totalSizeE = 0;
   private boolean _needsTotalSizeUpdateE = true;

   private Grid derivesList;
   private Paging derivesPaging;
   private ObjectPagingModel derivesModel;
   private final int _pageSizeP = 10;
   private int _startPageNumberP = 0;
   private int _totalSizeP = 0;
   private boolean _needsTotalSizeUpdateP = true;

   private boolean deriveExportRequest = false;

   // Objets Principaux.
   private Cession cession;
   private List<CederObjet> echantillonsCedes = new ArrayList<>();
   private List<CederObjet> derivesCedes = new ArrayList<>();
   // private List<CederObjetDecorator> echantillonsCedesDecores = new ArrayList<CederObjetDecorator>();
   // @since 2.1
   // distinct factories echantillons et dérivés
   private CederObjetDecoratorFactory cdEchansFactory;
   private CederObjetDecoratorFactory cdDerivesFactory;

   // private List<CederObjetDecorator> derivesCedesDecores = new ArrayList<CederObjetDecorator>();

   // gestion de l'export
   private boolean canExportEchantillons = false;
   private boolean canExportDerives = false;
   private boolean isExportAnonyme = false;
   private Menuitem exporterEchantillons;
   private Menuitem exporterDerives;

   private Menuitem printCessionPlan;

   // Variables formulaire.
   private String echantillonsGroupHeader = Labels.getLabel("cession.echantillons");
   private String derivesGroupHeader = Labels.getLabel("cession.prodDerive");

   // @since 2.1 checked Objects
   private final List<Integer> checkedEchantillonIds = new ArrayList<>();
   private final List<Integer> checkedDeriveIds = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.cession");
      setFantomable(true);
      setCascadable(false);
      setDeletable(true);

      cdEchansFactory = new CederObjetDecoratorFactory(isAnonyme(), checkedEchantillonIds);
      cdDerivesFactory = new CederObjetDecoratorFactory(isAnonyme(), checkedDeriveIds);
   }

   @Override
   public CessionController getObjectTabController(){
      return (CessionController) super.getObjectTabController();
   }

   @Override
   public void setObject(final TKdataObject c){
      this.cession = (Cession) c;

      initAssociations();

      StringBuffer sb = new StringBuffer();

      // header de la liste des echantillons cédés
      sb = new StringBuffer();
      sb.append(Labels.getLabel("cession.echantillons"));
      sb.append(" (");
      sb.append(echantillonsCedes.size());
      sb.append(")");
      echantillonsGroupHeader = sb.toString();
      groupEchantillons.setLabel(echantillonsGroupHeader);
      this.groupEchantillons.setOpen(false);

      // header de la liste des dérivés cédés
      sb = new StringBuffer();
      sb.append(Labels.getLabel("cession.prodDerive"));
      sb.append(" (");
      sb.append(derivesCedes.size());
      sb.append(")");
      derivesGroupHeader = sb.toString();
      groupProdDerives.setLabel(derivesGroupHeader);
      this.groupProdDerives.setOpen(false);

      // en fonction du ty pe de cession, on affiche différents formulaires
      if(this.cession.getCessionType() != null){
         if(this.cession.getCessionType().getType().toUpperCase().equals("SANITAIRE")){
            switchToSanitaireMode();
         }else if(this.cession.getCessionType().getType().toUpperCase().equals("RECHERCHE")){
            switchToRechercheMode();
         }else if(this.cession.getCessionType().getType().toUpperCase().equals("DESTRUCTION")){
            switchToDestructionMode();
         }
      }

      // gestion de l'export
      if(echantillonsCedes.size() > 0){
         exporterEchantillons.setDisabled(!canExportEchantillons);
      }else{
         exporterEchantillons.setDisabled(true);
      }
      if(derivesCedes.size() > 0){
         exporterDerives.setDisabled(!canExportDerives);
      }else{
         exporterDerives.setDisabled(true);
      }

      // gestion de l'impression du plan de destockage
      printCessionPlan.setDisabled(cession.getCessionStatut() == null || cession.getCessionStatut().getStatut().equals("VALIDEE")
         || (getEchantillonsCedes().isEmpty() && getDerivesCedes().isEmpty()));

      super.setObject(cession);
   }

   @Override
   public void setNewObject(){
      setObject(new Cession());

      // disable edit et delete
      super.setNewObject();
   }

   @Override
   public Cession getObject(){
      return this.cession;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public Prelevement getParentObject(){
      return null;
   }

   /**
    * Méthode initialisant les objets associés.
    * @version 2.1
    */
   private void initAssociations(){
      echantillonsCedes.clear();
      derivesCedes.clear();

      // @since 2.1
      // reset checks on change objects
      checkedEchantillonIds.clear();
      checkedDeriveIds.clear();

      if(this.cession.getCessionId() != null){
         // récupération des échantillons cédés
         echantillonsCedes = ManagerLocator.getCederObjetManager().getEchantillonsCedesByCessionManager(cession);

         echansPaging.setActivePage(0);
         _needsTotalSizeUpdateE = true;
         _startPageNumberE = 0;
         refreshModelE(_startPageNumberE);

         // récupération des dérivés cédés
         derivesCedes = ManagerLocator.getCederObjetManager().getProdDerivesCedesByCessionManager(cession);
         // derivesCedesDecores = new CederObjetDecorator().decorateListe(
         //		derivesCedes, isAnonyme());

         derivesPaging.setActivePage(0);
         _needsTotalSizeUpdateP = true;
         _startPageNumberP = 0;
         refreshModelP(_startPageNumberP);

         edit.setVisible(true);
         delete.setVisible(true);
      }
   }

   @Override
   public void prepareDeleteObject(){

      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
   }

   @Override
   public void removeObject(final String comments){
      final List<File> filesToDelete = new ArrayList<>();

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("newCessionTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);

      try{
         ManagerLocator.getCessionManager().removeObjectManager(getObject(), comments, SessionUtils.getLoggedUser(sessionScope),
            filesToDelete);

         // update de la liste des enfants et l'enfant en fiche
         getObjectTabController().updateReferencedObjects(revertAllCessedObjects());

         for(final File f : filesToDelete){
            f.delete();
         }
      }catch(final RuntimeException re){
         throw re;
      }
      ManagerLocator.getTxManager().commit(status);
   }

   /**
    * Override car les childrens doivent être mis à jour et non supprimés des
    * listes d'affichage.
    */
   @Override
   public void onLaterDelete(final Event event){
      try{
         final Contrat cont = getObject().getContrat();

         removeObject((String) event.getData());
         if(getObjectTabController() != null){
            if(getObjectTabController().getListe() != null){
               // on enlève l'objet de la liste
               getObjectTabController().getListe().removeObjectAndUpdateList(getObject());
            }
            getObjectTabController().clearStaticFiche();
            getObjectTabController().switchToOnlyListeMode();
         }

         // update contrats
         getObjectTabController().updateContrat(cont);

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   @Override
   public void onClick$addNew(){
      getObjectTabController().switchToCreateMode(null, null, null);
   }

   /*************************************************************************/
   /************************** FORMATTERS ************************************/
   /*************************************************************************/
   /**
    * Formate la date de validation de la cession.
    * 
    * @return Date de demande formatée.
    */
   public String getDateValidationFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getValidationDate());
      }else{
         return null;
      }
   }

   /**
    * Formate la date de départ de la cession.
    * 
    * @return Date de demande formatée.
    */
   public String getDateDepartFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getDepartDate());
      }else{
         return null;
      }
   }

   /**
    * Formate la date d'arrivéee de la cession.
    * 
    * @return Date de demande formatée.
    */
   public String getDateArriveeFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getArriveeDate());
      }else{
         return null;
      }
   }

   /**
    * Formate la date de destruction.
    * 
    * @return Date de destruction formatée.
    */
   public String getDateDestructionFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getDestructionDate());
      }else{
         return null;
      }
   }

   public String getEtablissement(){
      if(this.cession != null && this.cession.getServiceDest() != null
         && this.cession.getServiceDest().getEtablissement() != null){
         return this.cession.getServiceDest().getEtablissement().getNom();
      }else{
         return null;
      }
   }

   public String getSClassEtablissement(){
      if(this.cession != null && this.cession.getServiceDest() != null
         && this.cession.getServiceDest().getEtablissement() != null){
         return ObjectTypesFormatters.sClassEtablissement(this.cession.getServiceDest().getEtablissement());
      }else{
         return "";
      }
   }

   public String getSClassDemandeur(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.cession.getDemandeur());
      }else{
         return "";
      }
   }

   public String getSClassDestinataire(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.cession.getDestinataire());
      }else{
         return "";
      }
   }

   public String getSClassExecutant(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassCollaborateur(this.cession.getExecutant());
      }else{
         return "";
      }
   }

   public String getSClassServiceDest(){
      if(this.cession != null){
         return ObjectTypesFormatters.sClassService(this.cession.getServiceDest());
      }else{
         return "";
      }
   }

   /**
    * Formate la date de demande de la cession.
    * 
    * @return Date de demande formatée.
    */
   public String getDateDemandeFormated(){
      if(this.cession != null){
         return ObjectTypesFormatters.dateRenderer2(this.cession.getDemandeDate());
      }else{
         return null;
      }
   }

   /*************************************************************************/
   /************************** GROUPS ***************************************/
   /*************************************************************************/

   public boolean getEchantillonsListSizeSupOne(){
      // if (getDroitsConsultation().containsKey("Echantillon")
      //		&& getDroitsConsultation().get("Echantillon")) {
      return this.echantillonsCedes.size() > 1;
      // } else {
      //	return false;
      //}
   }

   public boolean getProdDerivesListSizeSupOne(){
      // if (getDroitsConsultation().containsKey("ProdDerive")
      //		&& getDroitsConsultation().get("ProdDerive")) {
      return this.derivesCedes.size() > 1;
      // } else {
      //	return false;
      //}
   }

   /*************************************************************************/
   /************************** TYPES ****************************************/
   /*************************************************************************/
   /**
    * Méthode affichant le formulaire du mode sanitaire.
    */
   public void switchToSanitaireMode(){
      rowSanitaire.setVisible(true);
      rowRecherche1.setVisible(false);
      rowRecherche2.setVisible(false);
      rowDestruction.setVisible(false);
      rowStatutDestruction.setVisible(false);

      rowDescription.setVisible(true);
      rowDestinataire.setVisible(true);
      rowService.setVisible(true);
      rowEtablissement.setVisible(true);
      rowSeparator1.setVisible(true);
      rowLine1.setVisible(true);
      rowSeparator2.setVisible(true);
      rowDateAndStatut.setVisible(true);
      rowLine2.setVisible(true);
      rowExecutant.setVisible(true);
      rowDates.setVisible(true);
      rowTransporteurAndTemp.setVisible(true);
      rowSeparator3.setVisible(true);

   }

   /**
    * Méthode affichant le formulaire du mode recherche.
    */
   public void switchToRechercheMode(){
      rowSanitaire.setVisible(false);
      rowRecherche1.setVisible(true);
      rowRecherche2.setVisible(true);
      rowDestruction.setVisible(false);
      rowStatutDestruction.setVisible(false);

      rowDescription.setVisible(true);
      rowDestinataire.setVisible(true);
      rowService.setVisible(true);
      rowEtablissement.setVisible(true);
      rowSeparator1.setVisible(true);
      rowLine1.setVisible(true);
      rowSeparator2.setVisible(true);
      rowDateAndStatut.setVisible(true);
      rowLine2.setVisible(true);
      rowExecutant.setVisible(true);
      rowDates.setVisible(true);
      rowTransporteurAndTemp.setVisible(true);
      rowSeparator3.setVisible(true);

   }

   /**
    * Méthode affichant le formulaire du mode destruction.
    */
   public void switchToDestructionMode(){
      rowSanitaire.setVisible(false);
      rowRecherche1.setVisible(false);
      rowRecherche2.setVisible(false);
      rowDestruction.setVisible(true);
      rowStatutDestruction.setVisible(true);

      rowDescription.setVisible(false);
      rowDestinataire.setVisible(false);
      rowService.setVisible(false);
      rowEtablissement.setVisible(false);
      rowSeparator1.setVisible(false);
      rowLine1.setVisible(false);
      rowSeparator2.setVisible(false);
      rowDateAndStatut.setVisible(false);
      rowLine2.setVisible(false);
      rowExecutant.setVisible(true);
      rowDates.setVisible(false);
      rowTransporteurAndTemp.setVisible(false);
      rowSeparator3.setVisible(false);

   }

   /*************************************************************************/
   /************************** LINKS ****************************************/
   /*************************************************************************/
   /** Affiche la fiche d'un échantillon.
    * 
    * @param event
    *            Event : clique sur un lien codeEchantillonCede dans la liste
    *            des échantillons.
    * @throws Exception
    */
   public void onClick$codeEchantillonCede(final Event event){
      onClickEchantillonCode(event);
   }

   /**
    * Affiche la fiche d'un produit dérivé.
    * 
    * @param event
    *            Event : clique sur un lien codeProdDeriveCede dans la liste
    *            des produits dérivés.
    * @throws Exception
    */
   public void onClick$codeProdDeriveCede(final Event event){
      onClickProdDeriveCode(event);
   }

   /**
    * Affiche la fiche d'un échantillon.
    */
   public void onClickEchantillonCode(final Event event){
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      displayObjectData(deco.getEchantillon());
   }

   public void onSelectAllEchantillons(final Event e){
      final List<CederObjetDecorator> decos = cdEchansFactory.decorateListe(echantillonsCedes);
      displayObjectsListData(new ArrayList<TKAnnotableObject>(cdEchansFactory.undecorateListe(decos)));
   }

   public void onClickProdDeriveCode(final Event event){
      final CederObjetDecorator deco = (CederObjetDecorator) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      displayObjectData(deco.getProdDerive());
   }

   public void onSelectAllDerives(final Event e){
      final List<CederObjetDecorator> decos = cdDerivesFactory.decorateListe(derivesCedes);
      displayObjectsListData(new ArrayList<TKAnnotableObject>(cdDerivesFactory.undecorateListe(decos)));
   }

   public void onClick$contratLabel(){
      if(this.cession.getContrat() != null){
         openFicheContratWindow(page, this.cession.getContrat());
      }
   }

   public void onClick$printAccord(){
      openBonLivraisonWindow(page, cession);
      // cdObjsFactory.decorateListe(echantillonsCedes),
      // cdObjsFactory.decorateListe(derivesCedes));
   }

   /**
    * Imprime le plan de boîte correspondant à la cession.
    */
   public void onClick$printCessionPlan(){
      final Document doc = CessionUtils.createFileHtmlToPrint(cession, echantillonsCedes, derivesCedes);
      if(doc != null){
         // Transformation du document en fichier
         byte[] dl = null;
         try{
            dl = ManagerLocator.getXmlUtils().creerBoiteHtml(doc);
         }catch(final Exception e){
            log.error(e);
         }

         // envoie du fichier à imprimer à l'utilisateur
         if(dl != null){
            // si c'est au format html, on va ouvrir une nouvelle
            // fenêtre
            try{
               sessionScope.put("File", dl);
               execution.sendRedirect("/impression", "_blank");
            }catch(final Exception e){
               if(sessionScope.containsKey("File")){
                  sessionScope.remove("File");
                  dl = null;
               }
            }
            dl = null;
         }
      }
   }

   /*
    * public void onClick$print() { StringBuffer sb = new StringBuffer();
    * sb.append(Labels.getLabel("impression.print.cession")); sb.append(" ");
    * sb.append(this.cession.getNumero());
    * 
    * openImpressionWindow(page, cession, sb.toString(), isAnonyme()); }
    */

   /**
    * Clic sur le bouton exporterEchantillons.
    */
   public void onClick$exporterEchantillons(){
      // Clients.showBusy(null);
      deriveExportRequest = false;
      openRestrictTablesModale(getObjectTabController().getListe(), self,
         ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
      // Events.echoEvent("onLaterExportEchantillon", self, null);
   }

   public void onClick$exportItem() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
      if(getObjectTabController().getListe().getRestrictedTableIds() != null){
         if(getObjectTabController().getListe().getRestrictedTableIds().isEmpty()){
            for(final TableAnnotation tb : ManagerLocator.getTableAnnotationManager()
               .findByBanquesManager(SessionUtils.getSelectedBanques(sessionScope), true)){
               getObjectTabController().getListe().getRestrictedTableIds().add(tb.getTableAnnotationId());
            }
         }
      }else{
         getObjectTabController().getListe().setRestrictedTableIds(new ArrayList<Integer>());
      }

      if(!deriveExportRequest){
         onLaterExportEchantillon();
      }else{
         onLaterExportDerives();
      }
   }

   /**
    * Méthode qui gère l'export des échantillons de la cession.
    * 
    * @throws ClassNotFoundException
    * @throws SecurityException
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InstantiationException
    */
   
   public void onLaterExportEchantillon() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
      // List<Echantillon> echans = new ArrayList<Echantillon>();

      // for (int i = 0; i < echantillonsCedesDecores.size(); i++) {
      //	echans.add(echantillonsCedesDecores.get(i).getEchantillon());
      //}

      final List<Integer> objsIds = new ArrayList<>();
      for(final CederObjet cObj : echantillonsCedes){
         objsIds.add(cObj.getObjetId());
      }

      final Class<?> exportThread = Class.forName(SessionUtils.getDatabasePathClass());
      final Constructor<?> constr = exportThread.getConstructor(Desktop.class, int.class, List.class, List.class, boolean.class,
         short.class, Utilisateur.class, List.class, HtmlMacroComponent.class, Map.class);
      final Object o =
         constr.newInstance(desktop, ConfigManager.ENTITE_ID_ECHANTILLON, objsIds, SessionUtils.getSelectedBanques(sessionScope),
            isExportAnonyme(), ConfigManager.DEFAULT_EXPORT, SessionUtils.getLoggedUser(sessionScope),
            getObjectTabController().getListe().getRestrictedTableIds(), callProgressBar(), null);
      final Method method = exportThread.getMethod("start");

      // put into session
      if(!desktop.hasAttribute("threads")){
         desktop.setAttribute("threads", new ArrayList<Export>());
      }
      ((List<Export>) desktop.getAttribute("threads")).add((Export) o);
      ((Export) o).setCession(getObject());

      method.invoke(o);
   }

   /**
    * Clic sur le bouton exporterDerives.
    */
   public void onClick$exporterDerives(){
      deriveExportRequest = true;
      openRestrictTablesModale(getObjectTabController().getListe(), self,
         ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
      // Events.echoEvent("onLaterExportDerives", self, null);
   }

   /**
    * Méthode qui gère l'export des dérivé de la cession.
    * 
    * @throws ClassNotFoundException
    * @throws SecurityException
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InstantiationException
    */
   
   public void onLaterExportDerives() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

      final List<Integer> objsIds = new ArrayList<>();

      for(final CederObjet cObj : derivesCedes){
         objsIds.add(cObj.getObjetId());
      }

      final Class<?> exportThread = Class.forName(SessionUtils.getDatabasePathClass());
      final Constructor<?> constr = exportThread.getConstructor(Desktop.class, int.class, List.class, List.class, boolean.class,
         short.class, Utilisateur.class, List.class, HtmlMacroComponent.class, Map.class);
      final Object o =
         constr.newInstance(desktop, ConfigManager.ENTITE_ID_DERIVE, objsIds, SessionUtils.getSelectedBanques(sessionScope),
            isExportAnonyme(), ConfigManager.DEFAULT_EXPORT, SessionUtils.getLoggedUser(sessionScope),
            getObjectTabController().getListe().getRestrictedTableIds(), callProgressBar(), null);
      final Method method = exportThread.getMethod("start");

      // put into session
      if(!desktop.hasAttribute("threads")){
         desktop.setAttribute("threads", new ArrayList<Export>());
      }
      ((List<Export>) desktop.getAttribute("threads")).add((Export) o);
      ((Export) o).setCession(getObject());

      method.invoke(o);
   }

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   @Override
   public void applyDroitsOnFiche(){
      drawActionsButtons("Cession");

      if(sessionScope.containsKey("ToutesCollections")){
         // donne aucun droit en creation
         setCanNew(false);
         // setCanDelete(true);
      }

      //		List<String> entites = new ArrayList<String>();
      //		entites.add("Echantillon");
      //		entites.add("ProdDerive");
      //		setDroitsConsultation(drawConsultationLinks(entites));

      // si pas le droit d'accès aux dérivés, on cache le lien
      //		if (!getDroitsConsultation().get("ProdDerive")) {
      //			codeProdDeriveCede.setSclass(null);
      //		} else {
      //			codeProdDeriveCede.setSclass("formLink");
      //		}
      //		// si pas le droit d'accès aux échantillons, on cache le lien
      //		if (!getDroitsConsultation().get("Echantillon")) {
      //			codeEchantillonCede.setSclass(null);
      //		} else {
      //			codeEchantillonCede.setSclass("formLink");
      //		}

      // gestion de l'export
      Boolean admin = false;
      if((Boolean) sessionScope.get("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      if(admin){
         setCanExportEchantillons(true);
         setCanExportDerives(true);
      }else{
         //Hashtable<String, List<OperationType>> droits = new Hashtable<String, List<OperationType>>();

         if(sessionScope.containsKey("Droits")){
            // on extrait les droits de l'utilisateur
            // droits = (Hashtable<String, List<OperationType>>) sessionScope
            //		.get("Droits");

            String export = "Non";
            // gestion de l'export
            if(sessionScope.containsKey("Export")){
               export = (String) sessionScope.get("Export");
            }

            // List<OperationType> ops = droits.get("Echantillon");
            if((export.equals("Export") || export.equals("ExportAnonyme")) && getDroitsConsultation().get("Echantillon")){
               setCanExportEchantillons(true);
            }else{
               setCanExportEchantillons(false);
            }
            // ops = droits.get("ProdDerive");
            if((export.equals("Export") || export.equals("ExportAnonyme")) && getDroitsConsultation().get("ProdDerive")){
               setCanExportDerives(true);
            }else{
               setCanExportDerives(false);
            }

            if(export.equals("ExportAnonyme")){
               isExportAnonyme = true;
            }else{
               isExportAnonyme = false;
            }
         }
      }

      if(canExportEchantillons){
         exporterEchantillons.setDisabled(false);
      }else{
         exporterEchantillons.setDisabled(true);
      }
      if(canExportDerives){
         exporterDerives.setDisabled(false);
      }else{
         exporterDerives.setDisabled(true);
      }

      super.applyDroitsOnFiche();
   }
   //
   //	/**
   //	 * Renvoie true si l'utilisateur loggé a le droit d'accéder à la fiche de ce
   //	 * dérivé.
   //	 * 
   //	 * @param event
   //	 * @return
   //	 */
   //	public Boolean getDroitProdDeriveConsultation(Event event) {
   //		ProdDerive derive = ((CederObjetDecorator) AbstractListeController2
   //				.getBindingData((ForwardEvent) event, false)).getProdDerive();
   //		boolean acces = false;
   //
   //		Banque bk = derive.getBanque();
   //		Utilisateur user = (Utilisateur) sessionScope.get("User");
   //
   //		Set<Plateforme> pfs = new HashSet<Plateforme>();
   //		if (user.isSuperAdmin()) {
   //			List<Plateforme> tmp = ManagerLocator.getPlateformeManager()
   //					.findAllObjectsManager();
   //			for (int i = 0; i < tmp.size(); i++) {
   //				pfs.add(tmp.get(i));
   //			}
   //		} else {
   //			pfs = ManagerLocator.getUtilisateurManager().getPlateformesManager(
   //					user);
   //		}
   //		if (pfs.contains(bk.getPlateforme())) {
   //			acces = true;
   //		} else {
   //			// on récupère le profil du user pour la banque
   //			// sélectionnée
   //			List<ProfilUtilisateur> profils = ManagerLocator
   //					.getProfilUtilisateurManager()
   //					.findByUtilisateurBanqueManager(user, bk);
   //
   //			if (profils.size() > 0) {
   //				Profil profil = profils.get(0).getProfil();
   //				// si l'utilisateur est admin pour la banque
   //				if (profil.getAdmin()) {
   //					acces = true;
   //				} else {
   //					List<OperationType> operations = ManagerLocator
   //							.getDroitObjetManager()
   //							.getOperationsByProfilEntiteManager(profil,
   //									"ProdDerive");
   //					OperationType opeation = ManagerLocator
   //							.getOperationTypeManager()
   //							.findByNomLikeManager("Consultation", true).get(0);
   //					if (operations.contains(opeation)) {
   //						acces = true;
   //					}
   //				}
   //			}
   //		}
   //
   //		return acces;
   //	}

   /**********************************************************************/
   /*********************** GETTERS **************************************/
   /**********************************************************************/

   public Cession getCession(){
      return cession;
   }

   public void setCession(final Cession c){
      setObject(c);
   }

   public List<CederObjet> getEchantillonsCedes(){
      return echantillonsCedes;
   }

   public List<CederObjet> getDerivesCedes(){
      return derivesCedes;
   }

   // public List<CederObjetDecorator> getEchantillonsCedesDecores() {
   //	return echantillonsCedesDecores;
   //}

   //	public List<CederObjetDecorator> getDerivesCedesDecores() {
   //		return derivesCedesDecores;
   //	}

   public String getEchantillonsGroupHeader(){
      return echantillonsGroupHeader;
   }

   public String getDerivesGroupHeader(){
      return derivesGroupHeader;
   }

   /**
    * Mets a jour les échantillons et derives cédés en cas de suppression de la
    * cession.
    * 
    * @return liste des children mis à jour.
    * @version 2.1.1
    */
   private List<TKdataObject> revertAllCessedObjects(){
      final List<TKdataObject> children = new ArrayList<>();

      if(!getCession().getCessionStatut().getStatut().equals("REFUSEE")){

         //	CederObjetDecorator deco = echantillonsCedesDecores.get(i);
         for(final Object deco : cdEchansFactory.decorateListe(echantillonsCedes)){

            revertEchantillon(deco);

            children.add(((CederObjetDecorator) deco).getEchantillon());
         }

         // pour chaque dérivé cédé
         for(final Object deco : cdDerivesFactory.decorateListe(derivesCedes)){

            revertProdDerive(deco);

            children.add(((CederObjetDecorator) deco).getProdDerive());
         }
      }else{
         for(final Object deco : cdEchansFactory.decorateListe(echantillonsCedes)){
            children.add(((CederObjetDecorator) deco).getEchantillon());
         }
         for(final Object deco : cdDerivesFactory.decorateListe(derivesCedes)){
            children.add(((CederObjetDecorator) deco).getProdDerive());
         }
      }
      return children;
   }

   public boolean isCanExportEchantillons(){
      return canExportEchantillons;
   }

   public void setCanExportEchantillons(final boolean c){
      this.canExportEchantillons = c;
   }

   public boolean isCanExportDerives(){
      return canExportDerives;
   }

   public void setCanExportDerives(final boolean c){
      this.canExportDerives = c;
   }

   @Override
   public boolean isExportAnonyme(){
      return isExportAnonyme;
   }

   public void setExportAnonyme(final boolean a){
      this.isExportAnonyme = a;
   }

   /**
    * Localise le type de cession
    * 
    * @return
    */
   public String getCessionType(){
      if(cession != null && cession.getCessionType() != null){
         return Labels.getLabel("cession.type." + cession.getCessionType().getType().toLowerCase());
      }else{
         return null;
      }
   }

   public String getCessionStatut(){
      return ObjectTypesFormatters.ILNObjectStatut(getObject().getCessionStatut());
   }

   /************************ Paging *********************************/

   private void refreshModelE(final int activePage){
      echansPaging.setPageSize(_pageSizeE);
      echansModel = new ObjectPagingModel(activePage, _pageSizeE, echantillonsCedes, cdEchansFactory, null);

      if(_needsTotalSizeUpdateE){
         _totalSizeE = echansModel.getTotalSize();
         _needsTotalSizeUpdateE = false;
      }

      echansPaging.setTotalSize(_totalSizeE);
      echantillonsList.setModel(echansModel);
   }

   public void onPaging$echansPaging(final ForwardEvent event){
      final PagingEvent pe = (PagingEvent) event.getOrigin();
      _startPageNumberE = pe.getActivePage();
      refreshModelE(_startPageNumberE);
   }

   private void refreshModelP(final int activePage){
      derivesPaging.setPageSize(_pageSizeP);
      derivesModel = new ObjectPagingModel(activePage, _pageSizeP, derivesCedes, cdDerivesFactory, null);

      if(_needsTotalSizeUpdateP){
         _totalSizeP = derivesModel.getTotalSize();
         _needsTotalSizeUpdateP = false;
      }

      derivesPaging.setTotalSize(_totalSizeP);
      derivesList.setModel(derivesModel);
   }

   public void onPaging$derivesPaging(final ForwardEvent event){
      final PagingEvent pe = (PagingEvent) event.getOrigin();
      _startPageNumberP = pe.getActivePage();
      refreshModelP(_startPageNumberP);
   }

   /**
    * Vérifie à partir de la liste de codes renvoyés par le scan de boîte 
    * si les objets correspondant sont bien dans la liste de cessions, 
    * envoie un warning si un objet scanné n'est pas dans la liste.
    * Si la totalité des objets de la cession sont trouvés dans le scan, 
    * estampille la cession avec un Scan Check OK, sinon attend qu'un (ou plusieurs) 
    * autres scans viennent compléter le check. 
    * @param sT
    */
   public void applyTKObjectsCheckFromScan(final ScanTerminale sT){
      final List<String> codes = ManagerLocator.getScanTerminaleManager().findTKObjectCodesManager(sT);

      // check echantillon
      final List<String> falseCkeckEchantillonCodes = new ArrayList<>();
      final CederObjet mockCObj = new CederObjet();
      final CederObjetPK mockPk = new CederObjetPK();
      mockCObj.setPk(mockPk);
      final Entite echanEntite = ManagerLocator.getEntiteManager().findByIdManager(3);
      boolean upCedesEchans = false;
      for(final Integer eId : ManagerLocator.getEchantillonManager().findByCodeInListManager(codes,
         SessionUtils.getSelectedBanques(sessionScope), new ArrayList<String>())){
         mockPk.setEntite(echanEntite);
         mockPk.setCession(getObject());
         mockPk.setObjetId(eId);
         if(!getEchantillonsCedes().contains(mockCObj)){
            falseCkeckEchantillonCodes.add(ManagerLocator.getEchantillonManager().findByIdManager(eId).getCode());
         }else if(!getCheckedEchantillonIds().contains(eId)){ // checked
            getCheckedEchantillonIds().add(eId);
            if(!upCedesEchans){
               upCedesEchans = true;
            }
         }
      }

      // check derives
      final List<String> falseCkeckDeriveCodes = new ArrayList<>();
      final Entite deriveEntite = ManagerLocator.getEntiteManager().findByIdManager(8);
      boolean upCedesDerives = false;
      for(final Integer pId : ManagerLocator.getProdDeriveManager().findByCodeInListManager(codes,
         SessionUtils.getSelectedBanques(sessionScope), new ArrayList<String>())){
         mockPk.setEntite(deriveEntite);
         mockPk.setCession(getObject());
         mockPk.setObjetId(pId);
         if(!getDerivesCedes().contains(mockCObj)){
            falseCkeckDeriveCodes.add(ManagerLocator.getProdDeriveManager().findByIdManager(pId).getCode());
         }else if(!getCheckedDeriveIds().contains(pId)){ // checked
            getCheckedDeriveIds().add(pId);
            if(!upCedesDerives){
               upCedesDerives = true;
            }
         }
      }

      // notifications!
      // complete check!!
      if(getCession().getLastScanCheckDate() == null && getCheckedEchantillonIds().size() == getEchantillonsCedes().size()
         && getCheckedDeriveIds().size() == getDerivesCedes().size()){
         Clients.showNotification(Labels.getLabel("scan.cession.objects.complete"), "info", null, null, 2000, true);
         ManagerLocator.getCessionManager().applyScanCheckDateManager(getCession(), sT.getDateScan());

         // update cession dans la liste
         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().updateObjectGridList(getObject());
         }

      }

      // refresh model lists
      if(upCedesEchans){
         refreshModelE(_startPageNumberE);
      }
      if(upCedesDerives){
         refreshModelP(_startPageNumberP);
      }

      // false checked objects
      if(!falseCkeckEchantillonCodes.isEmpty() || !falseCkeckDeriveCodes.isEmpty()){

         final StringBuilder codesDetails = new StringBuilder();
         // echans
         if(!falseCkeckEchantillonCodes.isEmpty()){
            codesDetails.append(Labels.getLabel("Entite.Echantillon.pluriel"));
            codesDetails.append(": ");
            final Iterator<String> cIt = falseCkeckEchantillonCodes.iterator();
            while(cIt.hasNext()){
               codesDetails.append(cIt.next());
               if(cIt.hasNext()){
                  codesDetails.append(", ");
               }
            }
         }
         // derives
         if(!falseCkeckDeriveCodes.isEmpty()){
            if(!falseCkeckEchantillonCodes.isEmpty()){
               codesDetails.append("<br>");
            }
            codesDetails.append(Labels.getLabel("Entite.ProdDerive.pluriel"));
            codesDetails.append(": ");
            final Iterator<String> cIt = falseCkeckDeriveCodes.iterator();
            while(cIt.hasNext()){
               codesDetails.append(cIt.next());
               if(cIt.hasNext()){
                  codesDetails.append(", ");
               }
            }
         }

         final HashMap<String, Object> map = new HashMap<>();
         map.put("title", Labels.getLabel("general.warning"));
         map.put("message", Labels.getLabel("scan.cession.objects.notfound"));
         map.put("exceptionDetails", codesDetails);

         final Window window = (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
         window.doModal();
      }
   }

   public List<Integer> getCheckedEchantillonIds(){
      return checkedEchantillonIds;
   }

   public List<Integer> getCheckedDeriveIds(){
      return checkedDeriveIds;
   }

   /************************* @since 2.1 Large Cession ********************************/
   /**
    * Lance la recherche des échantillons en fournissant un fichier Excel
    * contenant une liste de codes, afin de les ajouter à la cession
    * @throws IOException 
    */
   public void onClick$addListCodesEchan() throws IOException{

      // Ajout onLater showBusy

      // récupère les codes des échantillons présents dans le
      // fichier excel que l'utilisateur va uploader
      // List<String> codes = getObjectTabController()
      //							.getListe().getListStringToSearch();

      // warns
      // codes non trouves
      // if (!notfounds.isEmpty()) {

      // Clients.showNotification(ObjectTypesFormatters.getLabel("scan.objects.display.info", 
      //		new String[] {sT.getName(), ObjectTypesFormatters.dateRenderer2(sT.getDateScan()),
      //			String.valueOf(sT.getNbTubesStored()),
      //			String.valueOf(derives.size())})
      //	, derives.size() == sT.getNbTubesStored() ? "info" : "warning", null, null, 3000);

      Clients.clearBusy();

      final HashMap<String, Object> map = new HashMap<>();
      map.put("title", Labels.getLabel("error.unhandled"));
      map.put("message", "warning");
      map.put("exception", null);

      final Window window = (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
      window.doModal();
      // } else 

      // echantillons non stocke

      // ManagerLocator.getCessionManager()
      //	.addObjectsAndValidateCession(cession, ids, 3, SessionUtils.getLoggedUser(sessionScope));

      // refresh cession!

   }
}
