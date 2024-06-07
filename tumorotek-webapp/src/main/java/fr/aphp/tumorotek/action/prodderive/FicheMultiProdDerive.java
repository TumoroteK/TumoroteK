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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.param.EParametreValeurParDefaut;
import fr.aphp.tumorotek.utils.MessagesUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.Validator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Text;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.component.ValeurDecimaleModale;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.exception.DeriveBatchSaveException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.helper.FileBatch;
import fr.aphp.tumorotek.manager.impl.coeur.cession.OldEmplTrace;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationTypeManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.ECederObjetStatut;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche de création de
 * plusieurs dérivés.
 * Controller créé le 16/02/10.
 *
 * @author Pierre Ventadour
 * @since 2.1
 * @version 2.2.3-genno
 *
 */
public class FicheMultiProdDerive extends FicheProdDeriveEdit
{

   private final Logger log = LoggerFactory.getLogger(FicheModifMultiProdDerive.class);

   private static final long serialVersionUID = 8521632198848434054L;

   // Boutons supplémentaires.

   private Combobox separatorBox;

   private Intbox premierCodeBoxDerive;

   private Intbox dernierCodeBoxDerive;

   private Textbox premiereLettreBoxDerive;

   private Textbox derniereLettreBoxDerive;

   private Radio numNombres;

   private Radio numLettres;

   private Box choixNumerotation;

   private Button changeNumerotation;

   private Label qteEchLabel;

   private Div qteEchDiv;

   private Label prelQteLabel;

   private Div prelQteDiv;

   private Label prodDeriveQteLabel;

   private Div prodDeriveQteDiv;

   private Grid derivesList;

   private Button stockageDerives;

   private Label requiredTransfoQuantiteLabel;


   //Objets Principaux.
   // ajout de dérivés
   private List<ProdDerive> prodDerives = new ArrayList<>();

   private List<ProdDeriveDecorator2> prodDerivesDecorated = new ArrayList<>();

   private List<List<ProdDerive>> addedProdDerives = new ArrayList<>();

   private List<String> usedCodesProdDerives = new ArrayList<>();

   private Hashtable<ProdDerive, Emplacement> derivesEmpl = new Hashtable<>();

   private Boolean prodDeriveFromRetourCession = false;

   private CederObjet objetRetourCession;

   // Variables formulaire.
   private Integer premierCode;

   private Integer dernierCode;

   private String premiereLettre;

   private String derniereLettre;

   //   private List<String> lettres = new ArrayList<>();
   private boolean selectParent = false;

   private Date dateSortie;

   private final ProdDeriveDecoratorRowRenderer deriveDecoRenderer = new ProdDeriveDecoratorRowRenderer();

   private Retour transfoRetour = null;

   private final List<List<AnnotationValeur>> annoBatches = new ArrayList<>();

   private final List<FileBatch> batches = new ArrayList<>();

   // @since 2.2.3-genno
   private Button interfacage;

   private DossierExterne dossierExt = null; // ref locale pour éviter conflit avec ref dossier prélèvement/échantillon

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      initializeQuantiteUtiliseObligatoireFromSession();

      setWaitLabel("ficheProdDerive.multi.creation.encours");

      // liste de composants pour le prlvt parent
      setObjLabelsPrlvtParent(new Component[] {this.row1PrlvtDerive, this.row2PrlvtDerive});
      // liste de composants pour l'échantillon parent
      setObjLabelsEchanParent(new Component[] {this.row1EchanDerive, this.row2EchanDerive});
      // liste de composants pour le dérivé parent
      setObjLabelsDeriveParent(new Component[] {this.row1DeriveDerive, this.row2DeriveDerive});
      // liste de composants pour la transformation
      setObjLabelsTransformation(new Component[] {this.rowTransformation1, this.rowTransformation2,});

      stockageDerives.setVisible(getDroitOnAction("Stockage", "Consultation"));

      //      lettres = Utils.createListChars(26, null, new ArrayList<String>());

      getBinder().loadAll();
   }




   /**
    * Change le mode de la fiche en creation.
    * @param parent Objet dont est issu le dérivé.
    */
   public void switchToCreateMode(final TKdataObject parent){
      // Init du parent
      TKdataObject newParent = null;

      if(parent instanceof CederObjet){
         prodDeriveFromRetourCession = true;
         objetRetourCession = (CederObjet) parent;
         if(this.objetRetourCession.getEntite().getNom().equals("Echantillon")){
            newParent = (Echantillon) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(objetRetourCession.getEntite(), objetRetourCession.getObjetId());
         }else if(this.objetRetourCession.getEntite().getNom().equals("ProdDerive")){
            newParent = (ProdDerive) ManagerLocator.getEntiteManager()
               .findObjectByEntiteAndIdManager(objetRetourCession.getEntite(), objetRetourCession.getObjetId());
         }
      }else{
         newParent = parent;
      }

      setParentObject(newParent);

      super.switchToCreateMode();

      getObjectTabController().setCodeUpdated(false);
      getObjectTabController().setOldCode(null);
      
      Clients.scrollIntoView(formGrid.getColumns());

      // si le parent n'est pas null, on l'associe au
      // nouveau dérivé
      if(prodDeriveFromRetourCession){
         transfoQuantiteLabel.setVisible(false);
         transfoQuantiteDiv.setVisible(false);
         qteEchLabel.setVisible(false);
         qteEchDiv.setVisible(false);
         prelQteLabel.setVisible(false);
         prelQteDiv.setVisible(false);
         prodDeriveQteLabel.setVisible(false);
         prodDeriveQteDiv.setVisible(false);
      }
      if(newParent != null){
         selectParent = false;
         setTypeParent(newParent.getClass().getSimpleName());
         removeSelectParentMode();
      }else{
         // sinon, l'utilisateur devra sélectionner le parent
         addSelectParentMode();
      }
      // Initialisation du mode d'édition (listes, valeurs formulaires...)
      initQuantiteVolumeAndConc();
      initEditableMode();

      super.switchToCreateMode();

      if(!getDroitOnAction("Collaborateur", "Consultation")){
         operateurAideSaisieDerive.setVisible(false);
      }

      // Rend le bouton "interfacage" de la fiche visible si des
      // émetteurs ont été définis pour la banque et si un des
      // des émetteurs est GENNO
      // @since 2.2.3-genno
      interfacage.setVisible(SessionUtils.getEmetteursInterfacages(sessionScope).stream()
         .anyMatch(e -> e.getLogiciel().getNom().equalsIgnoreCase("GENNO")));
      dossierExt = null;

      getBinder().loadComponent(self);
   }

   @Override
   public void onClick$create(){

      boolean ok = false;

      //      codesParentBoxDerive.getValue();

      if(addedProdDerives.size() == 0){
         if(Messagebox.show(Labels.getLabel("message.noDeriveAdded"), Labels.getLabel("message.save.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            ok = true;
         }else{
            ok = false;
         }
      }else{
         ok = true;
      }

      if(ok){
         super.onClick$create();
      }
   }

   @Override
   public boolean onLaterCreate(){

      try{
         createObjectWithAnnots();

         // ajout du dérivé à la liste
         for(int i = 0; i < addedProdDerives.size(); i++){
            for(final ProdDerive prod : this.addedProdDerives.get(i)){
               getObjectTabController().getListe().addToObjectList(prod);
            }
         }

         getObjectTabController().onCreateDone();

         // ferme wait message
         Clients.clearBusy();

         // Traitement d'un retour de cession
         if(prodDeriveFromRetourCession){
            this.objetRetourCession.getProduitRetourList().addAll(this.addedProdDerives.get(0));
            this.objetRetourCession.setStatut(ECederObjetStatut.TRAITE);
            ManagerLocator.getManager(CederObjetManager.class).updateObjectManager(this.objetRetourCession,
               this.objetRetourCession.getCession(), this.objetRetourCession.getEntite(),
               this.objetRetourCession.getQuantiteUnite());
         }

         // proposition d'un événement de stockage
         if((getTypeParent().equals("Echantillon") || getTypeParent().equals("ProdDerive")) && transfoRetour == null){

            // on place l'ancienne adresse de stockage dans la
            // hashtable qu'on passera à la FicheRetour
            // TK-291 old emplacement trace
            final List<OldEmplTrace> oldEmps = new ArrayList<>();
            if(getOldEmplacement() != null){
               // oldEmps.put((TKStockableObject) getParentObject(), getOldEmplacement());
               oldEmps.add(new OldEmplTrace((TKStockableObject) getParentObject(),
                  ManagerLocator.getEmplacementManager().getAdrlManager(getOldEmplacement(), false),
                  ManagerLocator.getEmplacementManager().getConteneurManager(getOldEmplacement()), getOldEmplacement()));
            }
            // on place le parent des dérivés dans la
            // liste qu'on passera à la FicheRetour
            final List<TKStockableObject> list = new ArrayList<>();
            list.add((TKStockableObject) getParentObject());

            proposeRetoursCreation(list, oldEmps, dateSortie, null, getTransformation(), null,
               ManagerLocator.getManager(OperationTypeManager.class).findByNomLikeManager("Creation", true).get(0),
               Labels.getLabel("ficheRetour.observations.derives"), getSelectedCollaborateur());
         }
         return true;

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();

         // ajuste l'affichage de la DoublonFoundException
         if(re instanceof DeriveBatchSaveException
            && ((DeriveBatchSaveException) re).getTargetExeption() instanceof DoublonFoundException){

            final HashMap<String, Object> map = new HashMap<>();
            map.put("title", Labels.getLabel("error.unhandled"));
            map.put("message", handleExceptionMessage(((DeriveBatchSaveException) re).getTargetExeption()));
            map.put("exception", ((DeriveBatchSaveException) re).getTargetExeption());

            final Window window =
               (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
            window.doModal();
            return false;
         }
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   @Override
   public void createNewObject(){

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("updatePrelAndEchansTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);

      final List<File> filesCreated = new ArrayList<>();

      try{
         // si le dérivé est issu d'un parent connu
         Entite entite = null;
         if(!getTypeParent().equals("Aucun")){
            // en fonction du parent, on va rechercher l'objet Entité afin
            // de remplir la nouvelle transformation
            entite = findEntiteAndSetObjetIdForTransformation();
            getTransformation().setQuantiteUnite(getSelectedTransfoQuantiteUnite());
            getTransformation().setEntite(entite);
         }

         ObjetStatut statut = null;
         if("Echantillon".equals(getTypeParent())){
            statut = prepareUpdateParentEchantillon();
         }else if("ProdDerive".equals(getTypeParent())){
            statut = prepareUpdateParentProduitDerive();
         }

         // validation retour
         transfoRetour = validerEtCreerRetour(entite, statut);

         for(int i = 0; i < addedProdDerives.size(); i++){

            ManagerLocator.getManager(ProdDeriveManager.class).createDeriveListWithAnnotsManager(addedProdDerives.get(i),
               getBanque(), getTransformation(), SessionUtils.getLoggedUser(sessionScope), annoBatches.get(i),
               SessionUtils.getSystemBaseDir(), null, null);
         }

         saveEmplacements();

         creerNonConformites(prodDerivesDecorated);

         // annotation file batches
         for(final FileBatch batch : batches){
            ManagerLocator.getAnnotationValeurManager().createFileBatchForTKObjectsManager(batch.getObjs(), batch.getFile(),
               batch.getStream(), batch.getChamp(), getBanque(), SessionUtils.getLoggedUser(sessionScope),
               SessionUtils.getSystemBaseDir(), filesCreated);
         }

         // s'il n'y a pas d'erreurs, on met à jour le parent : modif
         // de sa quantité et de son volume
         switch(getTypeParent()){
            case "Prelevement":
               updateParentPrelevement();
               break;
            case "Echantillon":
               updateParentEchantillon(statut);
               break;
            case "ProdDerive":
               updateParentProduitDerive(statut);
               break;
            default:
               break;
         }

      }catch(final RuntimeException re){
         ManagerLocator.getTxManager().rollback(status);

         for(final File f : filesCreated){
            f.delete();
         }

         // revert emplacement since 2.0.13.2
         // au stade onGetResultsFromStockage
         // se basant sur echEmpls
         final Iterator<ProdDeriveDecorator2> itP = prodDerivesDecorated.iterator();

         ProdDerive p;
         ProdDeriveDecorator2 ed;
         final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", false).get(0);
         final ObjetStatut nonstocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", false).get(0);
         while(itP.hasNext()){
            ed = itP.next();
            p = ed.getProdDerive();
            if(derivesEmpl.containsKey(p)){
               p.setObjetStatut(stocke);
               p.setEmplacement(null);
               // clean echanEmpl from emplacement in error
               // doublon ou emplacement occupé
               if((re instanceof EmplacementDoublonFoundException
                  && derivesEmpl.get(p).equals(((EmplacementDoublonFoundException) re).getEmplacementMock()))
                  || (re instanceof TKException && re.getMessage().equals("error.emplacement.notEmpty")
                     && ((TKException) re).getTkObj().equals(p))){
                  derivesEmpl.remove(p);
                  // reset decorateur
                  ed.setAdrlTmp(null);
                  ed.getProdDerive().setObjetStatut(nonstocke);
               }
            }
         }
         updateDecoList(derivesEmpl);

         throw re;
      }

      ManagerLocator.getTxManager().commit(status);

      // suppr le dossier externe importé (GENNO)
      getObjectTabController().handleExtCom(dossierExt, null, getObjectTabController());

      createFileHtmlToPrint();
   }

   /**
    * Valide et crée un retour de transformation
    * @param statut statut de l'objet parent
    * @param entite entité
    */
   private Retour validerEtCreerRetour(final Entite entite, ObjetStatut statut){

      Retour res = null;

      if("Echantillon".equals(getTypeParent()) || "ProdDerive".equals(getTypeParent())){

         final boolean epuise = "EPUISE".equals(statut.getStatut());

         final Calendar dateS = Calendar.getInstance();
         if(dateSortie != null){
            dateS.setTime(dateSortie);
         }

         final List<Integer> id = new ArrayList<>();
         id.add(((TKStockableObject) getParentObject()).listableObjectId());
         final List<Retour> rets = ManagerLocator.getManager(RetourManager.class).findByObjectDateRetourEmptyManager(id,
            getTransformation().getEntite());

         final Retour epuisement;
         if(!rets.isEmpty()){
            epuisement = rets.get(0);
         }else if(epuise){
            epuisement = new Retour();
         }else{
            epuisement = null;
         }

         if(epuise){

            //Si le Retour ne provient pas de la base
            if(epuisement.getRetourId() == null){

               Clients.clearBusy();
               ValeurDecimaleModale.show(Labels.getLabel("listeRetour.title"), Labels.getLabel("Champ.Retour.TempMoyenne"), 20f,
                  false, evt -> epuisement.setTempMoyenne((Float) evt.getData()));
               Clients.showBusy(Labels.getLabel(getWaitLabel()));

               epuisement.setDateSortie(dateS);

               epuisement.setObjetId(((TKStockableObject) getParentObject()).listableObjectId());
               epuisement.setEntite(entite);
            }else{
               epuisement.setObjetStatut(null);
            }
            epuisement.setObservations(Labels.getLabel("ficheRetour.observations.derives") + " "
               + Labels.getLabel("ficheRetour.observations.epuisement"));

         }else{
            // complete le retour existant
            if(epuisement != null){
               epuisement.setDateRetour(dateS);
               statut = epuisement.getObjetStatut();
               epuisement.setObjetStatut(null);
            }
         }
         if(epuisement != null){
            BeanValidator.validateObject(epuisement, new Validator[] {ManagerLocator.getRetourValidator()});
         }

         if(epuisement != null){
            // passage temporaire au statut NON STOCKE
            // afin que ce statut soit enregistré dans le retour correspondant
            // à l'épuisement de l'échantillon
            ((TKStockableObject) getParentObject())
               .setObjetStatut(ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0));

            ManagerLocator.getManager(RetourManager.class).createOrUpdateObjectManager(epuisement,
               (TKStockableObject) getParentObject(), getOldEmplacement(), null, null, getTransformation(), null,
               SessionUtils.getLoggedUser(sessionScope), "creation");

         }

         res = epuisement;

      }

      return res;

   }

   /**
    * Crée les non conformités pour une liste de produits dérivés décorés
    * @param listDecoratedDerives liste de produits dérivés décorés
    */
   private void creerNonConformites(final List<ProdDeriveDecorator2> listDecoratedDerives){

      for(final ProdDeriveDecorator2 prodDeriveDecorator : listDecoratedDerives){

         final ProdDerive decoratedDerive = prodDeriveDecorator.getProdDerive();

         //Conforme si le booléen est true ou null
         final Boolean conformeTraitement = Optional.ofNullable(decoratedDerive.getConformeTraitement()).orElse(true);
         final Boolean conformeCession = Optional.ofNullable(decoratedDerive.getConformeCession()).orElse(true);

         if(!conformeTraitement){
            ManagerLocator.getManager(ObjetNonConformeManager.class).createUpdateOrRemoveListObjectManager(decoratedDerive,
               prodDeriveDecorator.getNonConformiteTraitements(), "Traitement");
         }

         if(!conformeCession){
            ManagerLocator.getManager(ObjetNonConformeManager.class).createUpdateOrRemoveListObjectManager(decoratedDerive,
               prodDeriveDecorator.getNonConformiteCessions(), "Cession");
         }

      }

   }

   @Override
   public void setEmptyToNulls(){
      // si le code_labo est vide, on l'enregistre comme null
      if(getProdDerive().getCodeLabo().equals("")){
         getProdDerive().setCodeLabo(null);
      }

      if(getProdDerive().getArchive() == null){
         getProdDerive().setArchive(false);
      }

      if(getProdDerive().getEtatIncomplet() == null){
         getProdDerive().setEtatIncomplet(false);
      }
   }

   @Override
   public void onClick$validate(){}

   @Override
   public boolean onLaterUpdate(){
      return true;
   }

   @Override
   public void updateObject(){}

   /**
    * Méthode qui va sauvegarder le stockage des dérivés.
    * @return Une liste d'erreurs.
    */
   public void saveEmplacements(){

      // on parcourt la hashtable du stockage et on extrait
      // l'emplacement de chaque dérivé
      final List<Emplacement> emplsFinaux = new ArrayList<>();
      Set<ProdDerive> prods = derivesEmpl.keySet();
      Iterator<ProdDerive> it = prods.iterator();
      while(it.hasNext()){
         final ProdDerive tmp = it.next();
         final Emplacement empl = derivesEmpl.get(tmp);
         empl.setObjetId(tmp.getProdDeriveId());
         emplsFinaux.add(empl);
      }

      // enregistrement des emplacements
      ManagerLocator.getEmplacementManager().saveMultiEmplacementsManager(emplsFinaux);

      // on va MAJ chaque dérivé : son statut et son
      // emplacement
      prods = derivesEmpl.keySet();
      it = prods.iterator();
      final ObjetStatut statut = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);
      while(it.hasNext()){
         final ProdDerive deriveToUpdate = it.next();

         final List<OperationType> ops = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Stockage", true);
         // update du dérvié
         ManagerLocator.getProdDeriveManager().updateObjectManager(deriveToUpdate, deriveToUpdate.getBanque(),
            deriveToUpdate.getProdType(), statut, deriveToUpdate.getCollaborateur(), derivesEmpl.get(deriveToUpdate),
            deriveToUpdate.getVolumeUnite(), deriveToUpdate.getConcUnite(), deriveToUpdate.getQuantiteUnite(),
            deriveToUpdate.getModePrepaDerive(), deriveToUpdate.getProdQualite(), deriveToUpdate.getTransformation(), null, null,
            null, null, SessionUtils.getLoggedUser(sessionScope), false, ops, null);

         final ProdDeriveDecorator2 deco = new ProdDeriveDecorator2(deriveToUpdate);
         // si le dérivé existait déjà, on MAJ sa fiche
         // dans la liste des dérivés
         for(final List<ProdDerive> derives : addedProdDerives){
            if(!derives.contains(deco.getProdDerive())){
               // on vérifie que l'on retrouve bien la
               // page contenant la liste
               // des dérivés
               if(getObjectTabController().getListe() != null){
                  // update du dérivé dans la liste
                  getObjectTabController().getListe().updateObjectGridListFromOtherPage(deriveToUpdate, false);
               }
            }
         }
      }
   }

   /**
    * Méthode qui permet d'afficher la page d'impression de la cession.
    */
   public void createFileHtmlToPrint(){
      // s'il y a des éléments a stocker
      if(derivesEmpl.size() > 0){

         // création du document, de la page et de son titre
         final Document doc = ManagerLocator.getXmlUtils().createJDomDocumentBoites();
         final Element root = doc.getRootElement();
         final Element page =
            ManagerLocator.getXmlUtils().addPageBoite(root, Labels.getLabel("impression.boite.title.stockage.prodDerives"));

         final List<BoiteImpression> listeBoites = new ArrayList<>();

         // on parcourt la hashtable du stockage et on extrait
         //  chaque dérivé
         final Set<ProdDerive> prods = derivesEmpl.keySet();
         // pour chaque dérivé
         for(int k = 0; k < prodDerivesDecorated.size(); k++){
            // s'il vient d'être stocké
            if(prods.contains(prodDerivesDecorated.get(k).getProdDerive())){
               final ProdDerive derive = prodDerivesDecorated.get(k).getProdDerive();

               // on récupère son emplacement et la boite
               final Emplacement emp = ManagerLocator.getProdDeriveManager().getEmplacementManager(derive);
               final Terminale term = emp.getTerminale();

               final BoiteImpression newBi = new BoiteImpression();
               newBi.setBoite(term);

               // si c'est la 1ère fois que l'on rencontre cette boite
               if(!listeBoites.contains(newBi)){
                  // on remplit tous titres et légendes
                  newBi.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
                  newBi.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
                  newBi.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
                  newBi.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.stockage.prodDerives"));
                  newBi.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
                  newBi.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
                  newBi.setLegendeSelectionne(Labels.getLabel("impression.boite.legende.selectionne" + ".stockage.prodDerives"));

                  // création des intructions pour récupérer la boite
                  // Récup des parents de la boite
                  final List<Object> parents = ManagerLocator.getTerminaleManager().getListOfParentsManager(term);
                  final List<String> instructions = new ArrayList<>();
                  // pour chaque parent
                  for(int j = 0; j < parents.size(); j++){
                     if(parents.get(j).getClass().getSimpleName().equals("Conteneur")){
                        final Conteneur c = (Conteneur) parents.get(j);
                        instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.conteneur",
                           new String[] {c.getCode()}));
                     }else if(parents.get(j).getClass().getSimpleName().equals("Enceinte")){
                        final Enceinte e = (Enceinte) parents.get(j);
                        instructions.add(ObjectTypesFormatters.getLabel("impression.boite" + ".instruction.enceinte",
                           new String[] {e.getNom()}));
                     }
                  }
                  // ajout des instructions à la boite
                  instructions
                     .add(ObjectTypesFormatters.getLabel("impression.boite.instruction.terminale", new String[] {term.getNom()}));
                  instructions.add(Labels.getLabel("impression.boite.instruction" + ".stockage.prodDerives"));
                  newBi.setInstructions(instructions);

                  // ajout du dérivé à la liste des éléments
                  // a extraire
                  final List<String> elements = new ArrayList<>();
                  elements.add(
                     ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive", new String[] {"1", derive.getCode()}));
                  newBi.setElements(elements);

                  // ajout de la position du dérivé
                  final List<Integer> positions = new ArrayList<>();
                  positions.add(emp.getPosition());
                  newBi.setPositions(positions);

                  // ajout de la boite à la liste
                  listeBoites.add(newBi);

               }else{
                  // sinon on récupère la boite dans la liste
                  final BoiteImpression bi = listeBoites.get(listeBoites.indexOf(newBi));

                  // ajout du dérivé et de sa position aux
                  // éléments à extraire
                  final int pos = bi.getPositions().size() + 1;
                  bi.getPositions().add(emp.getPosition());
                  bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive",
                     new String[] {Integer.toString(pos), derive.getCode()}));
               }
            }
         }

         final StringBuffer adrImages = new StringBuffer();
         adrImages.append(((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getContextPath());
         adrImages.append("/images/icones/emplacements/");
         for(int i = 0; i < listeBoites.size(); i++){
            ManagerLocator.getXmlUtils().addBoite(page, listeBoites.get(i), adrImages.toString());
         }

         // Transformation du document en fichier
         byte[] dl = null;
         try{
            dl = ManagerLocator.getXmlUtils().creerBoiteHtml(doc);
         }catch(final Exception e){
            log.error(e.getMessage(), e); 
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

   @Override
   public void onClick$revert(){}

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand
    * l'utilisateur sélectionne un collaborateur.
    * @param e Event contenant le collaborateur sélectionné.
    */
   @Override
   public void onGetObjectFromSelection(final Event e){

      // les collaborateurs peuvent être modifiés dans la fenêtre
      // d'aide => maj de ceux-ci
      getNomsAndPrenoms().clear();
      setCollaborateurs(ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager());
      for(int i = 0; i < getCollaborateurs().size(); i++){
         getNomsAndPrenoms().add(getCollaborateurs().get(i).getNomAndPrenom());
      }
      collabBoxDerive.setModel(new CustomSimpleListModel(getNomsAndPrenoms()));

      // si un collaborateur a été sélectionné
      if(e.getData() != null){
         final Collaborateur coll = (Collaborateur) e.getData();
         if(getNomsAndPrenoms().contains(coll.getNomAndPrenom())){
            final int ind = getNomsAndPrenoms().indexOf(coll.getNomAndPrenom());
            setSelectedCollaborateur(getCollaborateurs().get(ind));
            collabBoxDerive.setValue(getSelectedCollaborateur().getNomAndPrenom());
         }
      }
   }

   /**
    * Méthode exécutée lors du clic sur le bouton addProdDerives.
    * Les nouveaux dérivés seront créés (mais pas encore
    * sauvegardés) et ajoutés à la liste.
    */
   public void onClick$addProdDerives(){

      try{

         validateCalendarBox(dateStockCalBox);
         validateCalendarBox(dateTransfoCalBox);

         if(getSelectedType() == null){
            Clients.scrollIntoView(typesBoxDerive);
            throw new WrongValueException(typesBoxDerive, Labels.getLabel("ficheProdDerive.error.type"));
         }
         // TK-434: Sécuriser la saisie de la quantité utilisée

         // Si le champ n'est pas renseigné
         if (transfoQuantiteBoxDerive.getValue() == null){
            // et la plateforme est configurée pour avoir la saisie de quantité utilisée obligatoire, on bloque l'ajout
            if (isQuantiteObligatoire){
               Clients.scrollIntoView(transfoQuantiteBoxDerive);
               // afficher un message d'erreur à côté du champ "quantité utilisée obligatoire"
               throw new WrongValueException(transfoQuantiteBoxDerive, Labels.getLabel("ficheMultiProdDerive.validation.quantite"));
            } else{
               // si la valeur du paramètre "quantité utilisée obligatoire" est false,  afficher une fenêtre d'avertissement
               boolean userAnswer = MessagesUtils.openQuestionModal(Labels.getLabel("general.warning"),
                                                                    Labels.getLabel("ficheProdDerive.warning.quantite"));
               if (!userAnswer) {
                  return;
               }
            }
         }

         // si le dérivé est issu d'un parent connu
         if(selectParent && !getTypeParent().equals("Aucun")){
            codesParentBoxDerive.getValue();
         }
         // on remplit le dérivé en fonction des champs nulls
         setEmptyToNulls();

         // Gestion du collaborateur
         final String selectedNomAndPremon = this.collabBoxDerive.getValue().toUpperCase();
         this.collabBoxDerive.setValue(selectedNomAndPremon);
         final int ind = getNomsAndPrenoms().indexOf(selectedNomAndPremon);
         if(ind > -1){
            setSelectedCollaborateur(getCollaborateurs().get(ind));
         }else{
            setSelectedCollaborateur(null);
         }

         // la quantité et le volume restants sont égaux aux valeurs
         // intiales
         getProdDerive().setQuantite(getProdDerive().getQuantiteInit());
         getProdDerive().setVolume(getProdDerive().getVolumeInit());

         final ObjetStatut statutNonStocke =
            ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0);

         if(getProdDerive().getDateTransformation() != null){
            dateSortie = getProdDerive().getDateTransformation().getTime();
         }else{
            dateSortie = null;
         }

         // prepare les listes de valeurs à manipuler
         getObjectTabController().getFicheAnnotation().populateValeursActionLists(true, false);
         // clone liste valeurs annotations
         final List<AnnotationValeur> clonesValeurs = new ArrayList<>();

         for(int i = 0; i < getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate().size(); i++){

            final AnnotationValeur val = getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate().get(i);
            // retire les annotations fichier car créés en batches ultérieurement
            if(val.getChampAnnotation().getDataType().getDataTypeId() == 8){
               if(val.getFichier() != null && val.getStream() != null){
                  final FileBatch batch = new FileBatch();
                  batch.setChamp(val.getChampAnnotation());
                  batch.setFile(val.getFichier());
                  batch.setStream(val.getStream());
                  batches.add(batch);
               }
            }else{
               clonesValeurs.add(getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate().get(i).clone());
            }
         }

         annoBatches.add(clonesValeurs);

         // gestion de la non conformitée après traitement
         if(conformeTraitementBoxOui.isChecked()){
            getProdDerive().setConformeTraitement(true);
         }else if(conformeTraitementBoxNon.isChecked()){
            getProdDerive().setConformeTraitement(false);
         }else{
            getProdDerive().setConformeTraitement(null);
         }

         if(getProdDerive().getConformeTraitement() == null){
            setSelectedNonConformiteTraitement(null);
         }else if(getProdDerive().getConformeTraitement()){
            setSelectedNonConformiteTraitement(null);
         }

         // gestion de la non conformitée à la cession
         if(conformeCessionBoxOui.isChecked()){
            getProdDerive().setConformeCession(true);
         }else if(conformeCessionBoxNon.isChecked()){
            getProdDerive().setConformeCession(false);
         }else{
            getProdDerive().setConformeCession(null);
         }

         if(getProdDerive().getConformeCession() == null){
            setSelectedNonConformiteCession(null);
         }else if(getProdDerive().getConformeCession()){
            setSelectedNonConformiteCession(null);
         }

         final List<ProdDerive> newDerivesBatch = new ArrayList<>();

         // gestion des bornes pour la création
         int first = 0;
         int last = 0;
         if(numNombres.isChecked()){
            first = premierCodeBoxDerive.getValue();
            last = dernierCodeBoxDerive.getValue();
         }else{
            first = premiereLettreBoxDerive.getValue().toUpperCase().charAt(0);
            last = derniereLettreBoxDerive.getValue().toUpperCase().charAt(0);
         }

         // 2.0.10.6 VIROBIOTEC création codes sans prefixe
         final String prefix = Optional.ofNullable(getCodePrefixe()).orElse("").trim();
         final String separator = Optional.ofNullable(separatorBox.getValue()).orElse("");

         // Création de tous les nouveaux produits dérivés
         for(int i = first; i <= last; i++){

            // création du code produit dérivé en fct de celui du parent et
            // de l'intervalle saisi
            final StringBuilder sb = new StringBuilder();
            sb.append(prefix).append(separator);

            if(numNombres.isChecked()){
               sb.append(i);
            }else{
               sb.append((char) i);
            }

            usedCodesProdDerives.add(sb.toString());

            final ProdDerive newProdDerive = new ProdDerive();

            newProdDerive.setBanque(this.getBanque());
            newProdDerive.setProdType(getSelectedType());
            newProdDerive.setCode(sb.toString());
            newProdDerive.setCodeLabo(getProdDerive().getCodeLabo());
            newProdDerive.setObjetStatut(statutNonStocke);
            newProdDerive.setCollaborateur(getSelectedCollaborateur());
            newProdDerive.setVolumeInit(getProdDerive().getVolumeInit());
            newProdDerive.setVolume(getProdDerive().getVolume());
            newProdDerive.setConc(getProdDerive().getConc());
            newProdDerive.setDateStock(getProdDerive().getDateStock());
            newProdDerive.setEmplacement(getProdDerive().getEmplacement());
            newProdDerive.setVolumeUnite(getSelectedVolumeUnite());
            newProdDerive.setConcUnite(getSelectedConcUnite());
            newProdDerive.setQuantiteInit(getProdDerive().getQuantiteInit());
            newProdDerive.setQuantite(getProdDerive().getQuantite());
            newProdDerive.setQuantiteUnite(getSelectedQuantiteUnite());
            newProdDerive.setProdQualite(getSelectedQualite());
            newProdDerive.setTransformation(getTransformation());
            newProdDerive.setDateTransformation(getProdDerive().getDateTransformation());
            newProdDerive.setEtatIncomplet(getProdDerive().getEtatIncomplet());
            newProdDerive.setArchive(getProdDerive().getArchive());
            newProdDerive.setModePrepaDerive(getSelectedModePrepaDerive());
            newProdDerive.setConformeTraitement(getProdDerive().getConformeTraitement());
            newProdDerive.setConformeCession(getProdDerive().getConformeCession());

            final ProdDeriveDecorator2 deco = new ProdDeriveDecorator2(newProdDerive);
            deco.setNonConformiteTraitements(findSelectedNonConformitesTraitement());
            deco.setNonConformiteCessions(findSelectedNonConformitesCession());
            prodDerives.add(newProdDerive);
            newDerivesBatch.add(newProdDerive);
            prodDerivesDecorated.add(deco);

            // file batches
            for(final FileBatch batch : batches){
               if(!batch.isCompleted()){
                  batch.getObjs().add(newProdDerive);
               }
            }
         }

         for(final FileBatch batch : batches){
            batch.setCompleted(true);
         }

         addedProdDerives.add(newDerivesBatch);

         final ListModel<ProdDeriveDecorator2> list = new ListModelList<>(prodDerivesDecorated);
         derivesList.setModel(list);

         getDeriveDecoRenderer().setUsedCodes(usedCodesProdDerives);

         clearForm(true);

         Clients.scrollIntoView(stockageDerives);

         // active le lien vers le stockage
         if(!prodDerives.isEmpty()){
            stockageDerives.setDisabled(false);
         }

      }catch(final ValidationException ve){
         Messagebox.show(handleExceptionMessage(ve), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }


   /**
    * Méthode supprimant un des dérivés que l'utilisateur
    * souhaitait créer.
    * @param event : clic sur le lien deleteDerive dans la liste
    * des dérivés.
    */
   public void onDeleteDeco$rows(final ForwardEvent event){

      final ProdDeriveDecorator2 deco = (ProdDeriveDecorator2) event.getOrigin().getData();

      final ProdDerive deletedDerive = deco.getProdDerive();

      final Optional<List<ProdDerive>> maybeDerives =
         addedProdDerives.stream().filter(list -> list.contains(deletedDerive)).findFirst();

      if(maybeDerives.isPresent()){

         final List<ProdDerive> derives = maybeDerives.get();

         derives.remove(deletedDerive);
         prodDerives.remove(deletedDerive);
         prodDerivesDecorated.remove(deco);
         usedCodesProdDerives.remove(deletedDerive.getCode());
         derivesEmpl.remove(deletedDerive);

         final ListModel<ProdDeriveDecorator2> list = new ListModelList<>(prodDerivesDecorated);
         derivesList.setModel(list);

         // derive retire fileBatches
         for(final FileBatch batch : batches){
            batch.getObjs().remove(deletedDerive);
         }

         //Si le dérivé supprimé est le dernier de la liste, désactive le lien vers le stockage
         if(prodDerives.isEmpty()){
            stockageDerives.setDisabled(true);
         }
      }

   }


   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codePrefixeLabelDerive. Cette valeur sera mise en majuscules.
    */
   @Override
   public void onBlur$codePrefixeLabelDerive(){
      codePrefixeLabelDerive.setValue(codePrefixeLabelDerive.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée lors du clic sur le bouton stockageDerives.
    */
   public void onClick$stockageDerives(){

      if(!stockageDerives.isDisabled()){

         final Predicate<ProdDerive> isNonStocke = prodDerive -> "NON STOCKE".equals(prodDerive.getObjetStatut().getStatut());

         // on récupère tous les dérivés NON STOCKE
         final List<ProdDerive> derivesToStock = prodDerives.stream().filter(isNonStocke).collect(Collectors.toList());

         // on récupère les emplacements déjà réservés
         final List<Emplacement> reserves = new ArrayList<>(derivesEmpl.values());

         final StockageController tabController = StockageController.backToMe(getMainWindow(), page);
         getMainWindow().blockAllPanelsExceptOne("stockageTab");
         tabController.clearAllPage();
         tabController.switchToStockerMode(null, derivesToStock, Path.getPath(self), "onGetResultsFromStockage", reserves);
      }

   }

   /**
    * Méthode qui récupère les emplacements choisis pour les
    * dérivés.
    * @param e Event contenant les emplacements.
    */

   public void onGetResultsFromStockage(final Event e){
      // les emplacements sont contenus dans une hashtable mappant
      // un dérivé avec son emplacement
      final Hashtable<ProdDerive, Emplacement> results = (Hashtable<ProdDerive, Emplacement>) e.getData();

      updateDecoList(results);
   }

   /**
    * Met à jour la liste de dérivés décorées
    * @since 2.0.13.2
    * @param results: table dérivé - emplacements
    */
   private void updateDecoList(final Hashtable<ProdDerive, Emplacement> results){

      final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);

      final Set<ProdDerive> prods = results.keySet();

      // pour chaque dérivé qui vient d'être stocké
      for(final ProdDerive prodDerive : prods){

         final Optional<ProdDeriveDecorator2> maybeDecorator =
            prodDerivesDecorated.stream().filter(pdd -> prodDerive.equals(pdd.getProdDerive())).findAny();

         if(maybeDecorator.isPresent()){
            final ProdDeriveDecorator2 decoUp = maybeDecorator.get();
            decoUp.setAdrlTmp(results.get(prodDerive).getAdrl());
            decoUp.getProdDerive().setObjetStatut(stocke);
         }

         // ajout du couple dans la hashtable
         if(!derivesEmpl.contains(prodDerive)){
            derivesEmpl.put(prodDerive, results.get(prodDerive));
         }

      }

      // MAJ de la liste
      getBinder().loadAttribute(self.getFellow("derivesList"), "model");

   }

   /**
    * Cette méthode va vider le formulaire une fois que l'utilisateur
    * aura cliqué sur le bouton addProdDerives.
    */
   public void clearForm(final boolean setParentStatic){
      setProdDerive(new ProdDerive());

      premierCode = null;
      dernierCode = null;
      premiereLettre = null;
      derniereLettre = null;

      setSelectedType(getTypes().get(0));
      typesBoxDerive.clearSelection();
      ((ListModelList<Object>) typesBoxDerive.getModel()).clearSelection();

      setSelectedQuantiteUnite(null);
      quantiteUnitesBoxDerive.clearSelection();
      ((ListModelList<Object>) quantiteUnitesBoxDerive.getModel()).clearSelection();

      setSelectedVolumeUnite(null);
      volumeUnitesBoxDerive.clearSelection();
      ((ListModelList<Object>) volumeUnitesBoxDerive.getModel()).clearSelection();

      setSelectedConcUnite(null);
      concentrationUnitesBoxDerive.clearSelection();
      ((ListModelList<Object>) concentrationUnitesBoxDerive.getModel()).clearSelection();

      //setSelectedCollaborateur(null);
      //collabBoxDerive.setValue("");

      setSelectedQualite(null);
      qualitesBoxDerive.clearSelection();
      ((ListModelList<Object>) qualitesBoxDerive.getModel()).clearSelection();

      setSelectedModePrepaDerive(null);
      modePrepaBoxDerive.clearSelection();
      ((ListModelList<Object>) modePrepaBoxDerive.getModel()).clearSelection();

      if(setParentStatic){
         if(selectParentRowDerive.isVisible()){
            removeSelectParentMode();
         }
      }else{
         setCodePrefixe(null);
      }

      if(getObjectTabController().canUpdateAnnotation()){
         getObjectTabController().getFicheAnnotation().clearValeursLists(true);
         getObjectTabController().getFicheAnnotation().updateAnnotationValues();
         getObjectTabController().getFicheAnnotation().switchToStaticOrEditMode(false, false);
         //getObjectTabController()
         //.getFicheAnnotation().showButtonsBar(false);
      }

      setSelectedNonConformiteTraitement(null);
      setSelectedNonConformiteCession(null);
      conformeTraitementBoxOui.setChecked(false);
      conformeTraitementBoxNon.setChecked(false);
      conformeTraitementBox.setVisible(false);
      conformeCessionBoxOui.setChecked(false);
      conformeCessionBoxNon.setChecked(false);
      conformeCessionBox.setVisible(false);

      getBinder().loadComponent(self);
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   @Override

   public void initEditableMode(){

      setTypes(ManagerLocator.getProdTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      getTypes().add(0, null);
      setSelectedType(getTypes().get(0));

      // on récupèé les unités de quantité
      setQuantiteUnites(ManagerLocator.getUniteManager().findByTypeLikeManager("masse", true));
      getQuantiteUnites().addAll(ManagerLocator.getUniteManager().findByTypeLikeManager("discret", true));
      getQuantiteUnites().add(0, null);

      // on récupèé les unités de volume
      setVolumeUnites(ManagerLocator.getUniteManager().findByTypeLikeManager("volume", true));
      getVolumeUnites().add(0, null);

      // on récupèé les unités de conc
      setConcUnites(ManagerLocator.getUniteManager().findByTypeLikeManager("concentration", true));
      getConcUnites().add(0, null);

      setQualites(ManagerLocator.getProdQualiteManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      getQualites().add(0, null);

      setModePrepaDerives(
         ManagerLocator.getModePrepaDeriveManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      getModePrepaDerives().add(0, null);

      // init des collaborateurs
      getNomsAndPrenoms().clear();
      setCollaborateurs(ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager());
      for(int i = 0; i < getCollaborateurs().size(); i++){
         getNomsAndPrenoms().add(getCollaborateurs().get(i).getNomAndPrenom());
      }
      collabBoxDerive.setModel(new CustomSimpleListModel(getNomsAndPrenoms()));
      if(getSelectedCollaborateur() != null && getCollaborateurs().contains(getSelectedCollaborateur())){
         collabBoxDerive.setValue(getSelectedCollaborateur().getNomAndPrenom());
      }else{
         collabBoxDerive.setValue("");
         setSelectedCollaborateur(null);
      }

      if(getParentObject() != null && ((TKAnnotableObject) getParentObject()).getBanque() != null){
         setBanque(((TKAnnotableObject) getParentObject()).getBanque());
      }else{
         setBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      }

      // si l'utilisateur ne devait pas sélectionner un parent
      if(!selectParent){
         initDeriveInfosFromParent();
      }

      // init des codes utilisés
      usedCodesProdDerives = ManagerLocator.getManager(ProdDeriveManager.class).findAllCodesForBanqueManager(getBanque());

      super.initNonConformites();
   }

   /**
    * Recupere les informations du parents pour préparer les champs de
    * formulaire suivants: code prefixe, quantiteMax, transfo quantité unité,
    *  collaborateurs et liste de dérivés.
    */
   private void initDeriveInfosFromParent(){
      // si le parent est un prlvt
      if(getTypeParent().equals("Prelevement")){
         setCodePrefixe(((Prelevement) getParentObject()).getCode());
         // l'unité de la transformation sera celle du parent
         setSelectedTransfoQuantiteUnite((((Prelevement) getParentObject()).getQuantiteUnite()));

         // on récupère la quantité et le volume disponible
         setQuantiteMax(((Prelevement) getParentObject()).getQuantite());

         prodDerives = ManagerLocator.getPrelevementManager().getProdDerivesManager((Prelevement) getParentObject());

         if(((Prelevement) getParentObject()).getOperateur() != null){
            setSelectedCollaborateur(((Prelevement) getParentObject()).getOperateur());
         }
         // si le parent est un echantillon
      }else if(getTypeParent().equals("Echantillon")){
         setCodePrefixe(((Echantillon) getParentObject()).getCode());
         // l'unité de la transformation sera celle du parent
         setSelectedTransfoQuantiteUnite(((Echantillon) getParentObject()).getQuantiteUnite());

         // on récupère la quantité et le volume disponible
         setQuantiteMax(((Echantillon) getParentObject()).getQuantite());

         prodDerives = ManagerLocator.getEchantillonManager().getProdDerivesManager(((Echantillon) getParentObject()));

         if(((Echantillon) getParentObject()).getCollaborateur() != null){
            setSelectedCollaborateur(((Echantillon) getParentObject()).getCollaborateur());
         }
         // si le parent est un dérivé
      }else if(getTypeParent().equals("ProdDerive")){
         setCodePrefixe(((ProdDerive) getParentObject()).getCode());
         // l'unité de la transformation sera celle du parent
         setSelectedTransfoQuantiteUnite(((ProdDerive) getParentObject()).getQuantiteUnite());

         // on récupère la quantité et le volume disponible
         setQuantiteMax(((ProdDerive) getParentObject()).getQuantite());

         prodDerives = ManagerLocator.getProdDeriveManager().getProdDerivesManager(((ProdDerive) getParentObject()));

         if(((ProdDerive) getParentObject()).getCollaborateur() != null){
            setSelectedCollaborateur(((ProdDerive) getParentObject()).getCollaborateur());
         }
      }else{
         prodDerives.clear();
      }

      prodDerivesDecorated = ProdDeriveDecorator2.decorateListe(prodDerives);
      final ListModel<ProdDeriveDecorator2> list = new ListModelList<>(prodDerivesDecorated);
      derivesList.setModel(list);
      getBinder().loadAttribute(derivesList, "model");

      // active le lien vers le stockage
      if(!prodDerives.isEmpty()){
         stockageDerives.setDisabled(false);
      }else{
         stockageDerives.setDisabled(true);
      }
   }

   /**
    * Recherche des doublons avec les codes produit dérivé existant pour l'échantillon parent avec l'intervalle saisi.
    * @param premier numéro du premier code à créer
    * @param dernier numéro du premier code à créer
    * @return liste des codes trouvés en doublon
    */
   private List<String> findDoublonsWithUsedCodes(final int premier, final int dernier){

      final String separator = Optional.ofNullable(separatorBox.getValue()).orElse("");
      final String prefix = Optional.ofNullable(codePrefixeLabelDerive.getValue()).orElse("");

      final Function<String, String> mapToCode = s -> new StringBuilder(prefix.trim()).append(separator).append(s).toString();

      return IntStream.rangeClosed(premier, dernier).mapToObj(String::valueOf).map(mapToCode)
         .filter(usedCodesProdDerives::contains).collect(Collectors.toList());

   }

   /**
    * Recherche des doublons avec les codes produit dérivé existant pour l'échantillon parent avec l'intervalle saisi.
    * @param premier lettre du premier code à créer
    * @param dernier lettre du premier code à créer
    * @return liste des codes trouvés en doublon
    */
   private List<String> findDoublonsWithUsedCodes(final char premier, final char dernier){

      final String separator = Optional.ofNullable(separatorBox.getValue()).orElse("");
      final String prefix = Optional.ofNullable(codePrefixeLabelDerive.getValue()).orElse("");

      final Function<String, String> mapToCode = s -> new StringBuilder(prefix.trim()).append(separator).append(s).toString();

      return IntStream.rangeClosed(premier, dernier).mapToObj(i -> String.valueOf((char) i)).map(mapToCode)
         .filter(usedCodesProdDerives::contains).collect(Collectors.toList());

   }

   /**
    * Recherche des doublons de codes produit dérivé sur la plateforme avec l'intervalle saisi.
    * @param premier numéro du premier code à créer
    * @param dernier numéro du premier code à créer
    * @return liste des codes trouvés en doublon
    */
   private List<ProdDerive> findDoublonsWithPlateforme(final int premier, final int dernier){

      final String separator = Optional.ofNullable(separatorBox.getValue()).orElse("");
      final String prefix = Optional.ofNullable(codePrefixeLabelDerive.getValue()).orElse("");

      final Function<String, String> mapToCode = s -> new StringBuilder(prefix.trim()).append(separator).append(s).toString();

      final List<String> codesToCreate =
         IntStream.rangeClosed(premier, dernier).mapToObj(String::valueOf).map(mapToCode).collect(Collectors.toList());

      return ManagerLocator.getManager(ProdDeriveManager.class).findByListCodeWithPlateforme(codesToCreate,
         SessionUtils.getCurrentPlateforme());

   }

   /**
    * Recherche des doublons de codes produit dérivé sur la plateforme avec l'intervalle saisi.
    * @param premier lettre du premier code à créer
    * @param dernier lettre du premier code à créer
    * @return liste des codes trouvés en doublon
    */
   private List<ProdDerive> findDoublonsWithPlateforme(final char premier, final char dernier){

      final String separator = Optional.ofNullable(separatorBox.getValue()).orElse("");
      final String prefix = Optional.ofNullable(codePrefixeLabelDerive.getValue()).orElse("");

      final Function<String, String> mapToCode = s -> new StringBuilder(prefix.trim()).append(separator).append(s).toString();

      final List<String> codesToCreate = IntStream.rangeClosed(premier, dernier).mapToObj(i -> String.valueOf((char) i))
         .map(mapToCode).collect(Collectors.toList());

      return ManagerLocator.getManager(ProdDeriveManager.class).findByListCodeWithPlateforme(codesToCreate,
         SessionUtils.getCurrentPlateforme());

   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/
   public List<ProdDerive> getProdDerives(){
      return prodDerives;
   }

   public void setProdDerives(final List<ProdDerive> derives){
      this.prodDerives = derives;
   }

   public List<ProdDeriveDecorator2> getProdDerivesDecorated(){
      return prodDerivesDecorated;
   }

   public void setProdDerivesDecorated(final List<ProdDeriveDecorator2> derivesDecorated){
      this.prodDerivesDecorated = derivesDecorated;
   }

   public List<List<ProdDerive>> getAddedProdDerives(){
      return addedProdDerives;
   }

   public void setAddedProdDerives(final List<List<ProdDerive>> addedDerives){
      this.addedProdDerives = addedDerives;
   }

   public List<String> getUsedCodesProdDerives(){
      return usedCodesProdDerives;
   }

   public void setUsedCodesProdDerives(final List<String> usedCodesDerives){
      this.usedCodesProdDerives = usedCodesDerives;
   }

   public Integer getPremierCode(){
      return premierCode;
   }

   public void setPremierCode(final Integer code){
      this.premierCode = code;
   }

   public Integer getDernierCode(){
      return dernierCode;
   }

   public void setDernierCode(final Integer code){
      this.dernierCode = code;
   }

   public boolean isSelectParent(){
      return selectParent;
   }

   public void setSelectParent(final boolean select){
      this.selectParent = select;
   }

   public Hashtable<ProdDerive, Emplacement> getDerivesEmpl(){
      return derivesEmpl;
   }

   public void setDerivesEmpl(final Hashtable<ProdDerive, Emplacement> derivesE){
      this.derivesEmpl = derivesE;
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/

   private Constraint cttDernierCode = new ConstDernierCode();

   private Constraint cttPremierCode = new ConstPremierCode();

   public Constraint getCttPremierCode(){
      return cttPremierCode;
   }

   public void setCttPremierCode(final Constraint ctt){
      this.cttPremierCode = ctt;
   }

   public Constraint getCttDernierCode(){
      return cttDernierCode;
   }

   public void setCttDernierCode(final Constraint ctt){
      this.cttDernierCode = ctt;
   }

   /**
    *
    * @author Pierre Ventadour.
    *
    */
   private class ConstPremierCode implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){

         if(numNombres.isChecked()){

            premierCodeBoxDerive.setRawValue(value);

            premierCodeBoxDerive.clearErrorMessage();
            dernierCodeBoxDerive.clearErrorMessage();

            if(value != null){

               final Integer debut = (Integer) value;

               if(debut <= 0){
                  throw new WrongValueException(comp, Labels.getLabel("error.sup.zero"));
               }

               //Récupération de la valeur de fin de l'intervalle (on utilise getRawValue
               //pour éviter de déclencher la validation de cette valeur qui causerait une
               //boucle de validation sans fin)
               final Integer fin = (Integer) dernierCodeBoxDerive.getRawValue();

               if(fin != null){

                  //Vérification de la cohérence des valeurs de début et fin d'intervalle
                  if(debut > fin){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.premier.code.superieur"));
                  }

                  //Recherche de doublons avec les dérivés existants pour le parent
                  final List<String> doublonsParent = findDoublonsWithUsedCodes(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsParent.size() > 0){
                     final String codesDoublons = doublonsParent.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("prodDerive.doublon.error.num", new String[] {codesDoublons}));
                  }

                  //Recherche de doublons avec les dérivés existants sur la plateforme
                  final List<ProdDerive> doublonsPf = findDoublonsWithPlateforme(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsPf.size() > 0){
                     final String codesDoublons = doublonsPf.stream().map(ProdDerive::getCode).collect(Collectors.joining(", "));
                     final String banques = doublonsPf.stream().map(ProdDerive::getBanque).distinct().map(Banque::getNom)
                        .collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, banques}));
                  }

               }

            }else{
               throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
            }

         }

      }

   }

   /**
    *
    * @author Pierre Ventadour.
    *
    */
   private class ConstDernierCode implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){

         if(numNombres.isChecked()){

            dernierCodeBoxDerive.setRawValue(value);

            premierCodeBoxDerive.clearErrorMessage();
            dernierCodeBoxDerive.clearErrorMessage();

            if(value != null){

               final Integer fin = (Integer) value;

               if(fin <= 0){
                  throw new WrongValueException(comp, Labels.getLabel("error.sup.zero"));
               }

               //Récupération de la valeur de début de l'intervalle (on utilise getRawValue
               //pour éviter de déclencher la validation de cette valeur qui causerait une
               //boucle de validation sans fin)
               final Integer debut = (Integer) premierCodeBoxDerive.getRawValue();

               if(debut != null){

                  //Vérification de la cohérence des valeurs de début et fin d'intervalle
                  if(debut > fin){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.premier.code.superieur"));
                  }

                  //Recherche de doublons avec les dérivés existants pour le parent
                  final List<String> doublonsParent = findDoublonsWithUsedCodes(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsParent.size() > 0){
                     final String codesDoublons = doublonsParent.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("prodDerive.doublon.error.num", new String[] {codesDoublons}));
                  }

                  //Recherche de doublons avec les dérivés existants sur la plateforme
                  final List<ProdDerive> doublonsPf = findDoublonsWithPlateforme(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsPf.size() > 0){
                     final String codesDoublons = doublonsPf.stream().map(ProdDerive::getCode).collect(Collectors.joining(", "));
                     final String banques = doublonsPf.stream().map(ProdDerive::getBanque).distinct().map(Banque::getNom)
                        .collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, banques}));
                  }

               }

            }else{
               throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
            }

         }
      }
   }

   private class ConstPremiereLettre implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value) throws WrongValueException{

         if(numLettres.isChecked()){

            premiereLettreBoxDerive.setRawValue(value);

            premiereLettreBoxDerive.clearErrorMessage();
            derniereLettreBoxDerive.clearErrorMessage();

            final String sValue = (String) value;

            if(StringUtils.isNotEmpty(sValue)){

               final char debut = sValue.toUpperCase().charAt(0);

               if(debut < 'A' || debut > 'Z'){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillons.lettre.invalide"));
               }

               //Récupération de la valeur de début de l'intervalle (on utilise getRawValue
               //pour éviter de déclencher la validation de cette valeur qui causerait une
               //boucle de validation sans fin)
               final String derniereLettre = (String) derniereLettreBoxDerive.getRawValue();

               if(derniereLettre != null){

                  final char fin = derniereLettre.toUpperCase().charAt(0);

                  //Vérification de la cohérence des valeurs de début et fin d'intervalle
                  if(debut > fin){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.premier.lettre.superieur"));
                  }

                  //Recherche de doublons avec les dérivés existants pour le parent
                  final List<String> doublonsParent = findDoublonsWithUsedCodes(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsParent.size() > 0){
                     final String codesDoublons = doublonsParent.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("prodDerive.doublon.error.num", new String[] {codesDoublons}));
                  }

                  //Recherche de doublons avec les dérivés existants sur la plateforme
                  final List<ProdDerive> doublonsPf = findDoublonsWithPlateforme(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsPf.size() > 0){
                     final String codesDoublons = doublonsPf.stream().map(ProdDerive::getCode).collect(Collectors.joining(", "));
                     final String banques = doublonsPf.stream().map(ProdDerive::getBanque).distinct().map(Banque::getNom)
                        .collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, banques}));
                  }

               }

            }else{
               throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
            }

         }

      }

   }

   private Constraint cttPremiereLettre = new ConstPremiereLettre();

   /**
    *
    * @author Pierre Ventadour.
    *
    */
   private class ConstDerniereLettre implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value) throws WrongValueException{

         if(numLettres.isChecked()){

            derniereLettreBoxDerive.setRawValue(value);

            derniereLettreBoxDerive.clearErrorMessage();
            premiereLettreBoxDerive.clearErrorMessage();

            final String sValue = (String) value;

            if(StringUtils.isNotEmpty(sValue)){

               final char fin = sValue.toUpperCase().charAt(0);

               if(fin < 'A' || fin > 'Z'){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillons.lettre.invalide"));
               }

               //Récupération de la valeur de début de l'intervalle (on utilise getRawValue
               //pour éviter de déclencher la validation de cette valeur qui causerait une
               //boucle de validation sans fin)
               final String premiereLettre = (String) premiereLettreBoxDerive.getRawValue();

               if(premiereLettre != null){

                  final char debut = premiereLettre.toUpperCase().charAt(0);

                  //Vérification de la cohérence des valeurs de début et fin d'intervalle
                  if(debut > fin){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.premier.lettre.superieur"));
                  }

                  //Recherche de doublons avec les dérivés existants pour le parent
                  final List<String> doublonsParent = findDoublonsWithUsedCodes(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsParent.size() > 0){
                     final String codesDoublons = doublonsParent.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("prodDerive.doublon.error.num", new String[] {codesDoublons}));
                  }

                  //Recherche de doublons avec les dérivés existants sur la plateforme
                  final List<ProdDerive> doublonsPf = findDoublonsWithPlateforme(debut, fin);
                  // si des doublons existent pour les valeurs saisies
                  if(doublonsPf.size() > 0){
                     final String codesDoublons = doublonsPf.stream().map(ProdDerive::getCode).collect(Collectors.joining(", "));
                     final String banques = doublonsPf.stream().map(ProdDerive::getBanque).distinct().map(Banque::getNom)
                        .collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, banques}));
                  }

               }

            }else{
               throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
            }

         }
      }

   }

   private Constraint cttDerniereLettre = new ConstDerniereLettre();

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(volumeInitBoxDerive);
      Clients.clearWrongValue(quantiteInitBoxDerive);
      Clients.clearWrongValue(dateStockCalBox);
      Clients.clearWrongValue(transfoQuantiteBoxDerive);
      Clients.clearWrongValue(codeLaboBoxDerive);
      Clients.clearWrongValue(concentrationBoxDerive);
      Clients.clearWrongValue(dateTransfoCalBox);
      Clients.clearWrongValue(codePrefixeLabelDerive);
      //super.clearConstraints();
      Clients.clearWrongValue(premierCodeBoxDerive);
      Clients.clearWrongValue(dernierCodeBoxDerive);
      Clients.clearWrongValue(premiereLettreBoxDerive);
      Clients.clearWrongValue(derniereLettreBoxDerive);
   }

   /*************************************************************************/
   /************************** PARENT    ************************************/
   /*************************************************************************/
   private List<String> listParentTypes = new ArrayList<>();

   private String selectedParent;

   private List<String> codesParent = new ArrayList<>();

   private CustomSimpleListModel dictCodesModel;

   private String selectedCodeParent;
   //Variable contenant le code parent saisi par l'utilisateur.
   //   private String codeTmp;

   // Mode de sélection du parent
   private Row selectParentRowDerive;

   private Listbox typesParentBoxDerive;

   private Combobox codesParentBoxDerive;

   //private Label typeParentLabelDerive;
   private Label codeParentLabelDerive;

   private Label requiredCodeParentDerive;

   public List<String> getListParentTypes(){
      return listParentTypes;
   }

   public String getSelectedParent(){
      return selectedParent;
   }

   public void setSelectedParent(final String selected){
      this.selectedParent = selected;
   }

   public List<String> getCodesParent(){
      return codesParent;
   }

   public CustomSimpleListModel getDictCodesModel(){
      return dictCodesModel;
   }

   public String getSelectedCodeParent(){
      return selectedCodeParent;
   }

   public void setSelectedCodeParent(final String selected){
      this.selectedCodeParent = selected;
   }

   /**
    * Cache le mode de sélection du parent.
    */
   public void removeSelectParentMode(){

      selectParent = false;

      // on cache les éléments.
      selectParentRowDerive.setVisible(false);
      showParentInformation();
   }

   /**
    * Lors de la création d'un dérivé, si son parent n'est
    * pas fourni, on propose une sélection de celui-ci.
    * Cette méthode permet l'affichage de cette sélection.
    */
   public void addSelectParentMode(){
      selectParent = true;
      setParentObject(null);
      setTransformation(new Transformation());

      // On initiailise les types de parent disponibles
      listParentTypes = new ArrayList<>();
      listParentTypes.add(Labels.getLabel("Entite.Echantillon"));
      listParentTypes.add(Labels.getLabel("Entite.Prelevement"));
      listParentTypes.add(Labels.getLabel("Entite.ProdDerive"));
      listParentTypes.add(Labels.getLabel("fichePodDerive.parent.aucun"));
      selectedParent = Labels.getLabel("Entite.Echantillon");

      // affichage des éléments
      selectParentRowDerive.setVisible(true);

      // on cache les données sur le parent
      for(int i = 0; i < getObjLabelsPrlvtParent().length; i++){
         getObjLabelsPrlvtParent()[i].setVisible(false);
         getObjLabelsEchanParent()[i].setVisible(false);
         getObjLabelsDeriveParent()[i].setVisible(false);
      }

      // par défaut, on récupère la liste des codes échantillons
      codesParent = ManagerLocator.getEchantillonManager()
         .findAllCodesForDerivesByBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      // ces codes sont placés dans un dictionnaire pour permettre
      // le filtre lorsque l'utilisateur saisit le code
      dictCodesModel = new CustomSimpleListModel(codesParent.toArray());
      codesParentBoxDerive.setModel(dictCodesModel);
      setTypeParent("Echantillon");
   }

   /**
    * Mise à jour de la valeur sélectionnée dans la combobox lorsque
    * l'utilisateur clique à l'extérieur de celle-ci.
    * @param event Event : clique à l'extérieur de la combobox
    * codesParentBoxDerive.
    * @throws Exception
    */
   public void onBlur$codesParentBoxDerive(final Event event) throws Exception{

      final String codePrefixe;
      if(codesParentBoxDerive.getSelectedItem() != null){
         codePrefixe = codesParentBoxDerive.getSelectedItem().getLabel();
      }else{
         codePrefixe = codesParentBoxDerive.getValue();
      }

      setCodePrefixe(codePrefixe);
      setCodeParent(getCodePrefixe());

      final TKdataObject parent;
      switch(getTypeParent()){
         case "Prelevement":
            parent = ManagerLocator.getManager(PrelevementManager.class)
               .findByCodeOrNumLaboLikeWithBanqueManager(getCodeParent(), getBanque(), true).get(0);
            break;
         case "Echantillon":
            parent = ManagerLocator.getManager(EchantillonManager.class)
               .findByCodeLikeWithBanqueManager(getCodeParent(), getBanque(), true).get(0);
            break;
         case "ProdDerive":
            parent = ManagerLocator.getManager(ProdDeriveManager.class)
               .findByCodeOrLaboWithBanqueManager(getCodeParent(), getBanque(), true).get(0);
            break;
         default:
            parent = null;
            break;
      }

      if(!"Prelevement".equals(getTypeParent())){
         createComponentForListeParent((TKStockableObject) parent);
      }else{
         setParentObject(parent);
      }

      initDeriveInfosFromParent();

      // date validation
      dateTransfoCalBox.clearErrorMessage(dateTransfoCalBox.getValue());
      validateCoherenceDate(dateTransfoCalBox, dateTransfoCalBox.getValue());
      dateStockCalBox.clearErrorMessage(dateStockCalBox.getValue());
      validateCoherenceDate(dateStockCalBox, dateStockCalBox.getValue());

      getBinder().loadComponent(self);

   }

   /**
    * Nouvelle fenêtre pour afficher la liste des parents disponibles
    * @param page page où ouvrir la fenêtre
    * @return Window
    */
   private Window createWindowForListeParent(final Page page){
      // nouvelle fenêtre
      final Window win = new Window();
      win.setVisible(true);
      //      win.setId("listeParentWindow");
      win.setPage(page);
      win.setMaximizable(true);
      win.setSizable(true);
      win.setTitle(selectedParent + " " + codesParentBoxDerive.getSelectedItem().getLabel() + ": "
         + Labels.getLabel("prodDerive.parents.disponibles"));
      win.setBorder("normal");
      win.setWidth("500px");
      win.setHeight("510px");
      win.setClosable(false);

      return win;
   }

   /**
    * Recherche si le parent est en stock et/ou présent dans une ou plusieurs cession.
    * Ouvre une fenêtre pour la sélection
    * @param tkStockObj objet parent
    * @since 2.2.0
    */
   private void createComponentForListeParent(final TKStockableObject tkStockObj){
      final ArrayList<String> statuts = new ArrayList<>();
      statuts.add("EPUISE");
      statuts.add("ENCOURS");
      statuts.add("RESERVE");

      final List<CederObjet> cederObjetRef =
         ManagerLocator.getCederObjetManager().findByObjetAndStatutManager(tkStockObj, ECederObjetStatut.TRAITEMENT);

      final String objetStockeStatus = tkStockObj.getObjetStatut() != null ? tkStockObj.getObjetStatut().getStatut() : null;

      if((null == tkStockObj.getQuantite() || 0 < tkStockObj.getQuantite()) && !statuts.contains(objetStockeStatus)){
         if(!cederObjetRef.isEmpty()){
            createGridForListeParent(tkStockObj, cederObjetRef);
         }else{
            switchToCreateMode(tkStockObj);
         }
      }else if(cederObjetRef.size() > 1){
         createGridForListeParent(cederObjetRef);
      }else if(cederObjetRef.size() == 1){
         switchToCreateMode(cederObjetRef.get(0));
      }

   }

   /**
    * Création d'une ligne pour un objet en stock
    * @param tkStockObj l'objet en stock
    * @return Row
    * @since 2.2.0
    */
   private Row createRowForStockedParent(final TKStockableObject tkStockObj, final Window win){
      final Row row = new Row();
      row.setClass("formLink");
      row.addEventListener("onClick", new EventListener<Event>()
      {

         @Override
         public void onEvent(final Event event) throws Exception{
            switchToCreateMode(tkStockObj);
            win.detach();
         }
      });
      final Cell typeCell = new Cell();
      row.appendChild(typeCell);

      final Cell qtteCell = new Cell();
      row.appendChild(qtteCell);

      final Text typeText = new Text(Labels.getLabel("Statut.STOCKE"));
      typeCell.appendChild(typeText);

      final Text qtteText = new Text(tkStockObj.getQuantite().toString() + " " + tkStockObj.getQuantiteUnite().getNom());
      qtteCell.appendChild(qtteText);

      return row;
   }

   /**
    * Création d'une ligne pour un objet cédé
    * @param cederObjetRef l'objet cédé
    * @return Row
    * @since 2.2.0
    */
   private Row createRowForCederObjetParent(final CederObjet cederObjetRef, final Window win){
      final Row row = new Row();
      row.setClass("formLink");
      row.addEventListener("onClick", new EventListener<Event>()
      {

         @Override
         public void onEvent(final Event event) throws Exception{
            switchToCreateMode(cederObjetRef);
            win.detach();
         }
      });

      final Cell typeCell = new Cell();
      row.appendChild(typeCell);

      final Text typeText = new Text(Labels.getLabel("Entite.Cession") + " "
         + Labels.getLabel("cession.type." + cederObjetRef.getCession().getCessionType().getType().toLowerCase()) + " - "
         + cederObjetRef.getCession().getNumero());
      typeCell.appendChild(typeText);

      final Cell qtteCell = new Cell();
      row.appendChild(qtteCell);

      final Text qtteText = new Text(cederObjetRef.getQuantite().toString() + " " + cederObjetRef.getQuantiteUnite().getNom());
      qtteCell.appendChild(qtteText);

      return row;
   }

   /**
    * Ouverture d'une fenêtre avec la liste des ojets en stock et en cédés
    * @param tkStockObj l'objet en stock
    * @param cederObjetRef la liste des objets cédés
    * @since 2.2.0
    */
   private void createGridForListeParent(final TKStockableObject tkStockObj, final List<CederObjet> cederObjetRef){

      final Grid grid = initiateGridForListeParent();

      final Rows rows = new Rows();
      grid.appendChild(rows);

      final Window win = createWindowForListeParent(page);

      win.appendChild(grid);

      rows.appendChild(createRowForStockedParent(tkStockObj, win));

      for(final CederObjet cederObj : cederObjetRef){
         rows.appendChild(createRowForCederObjetParent(cederObj, win));
      }

      win.onModal();
   }

   /**
    * Ouverture d'une fenêtre avec uniquement une liste d'objets cédés
    * @param cederObjetRef la liste des objets cédés
    * @since 2.2.0
    */
   private void createGridForListeParent(final List<CederObjet> cederObjetRef){
      final Grid grid = initiateGridForListeParent();

      final Rows rows = new Rows();
      grid.appendChild(rows);

      final Window win = createWindowForListeParent(page);

      win.appendChild(grid);

      for(final CederObjet cederObj : cederObjetRef){
         rows.appendChild(createRowForCederObjetParent(cederObj, win));
      }

      win.onModal();
   }

   /**
    * Grid vide qui va contenir la liste des Parents du Stock et/ou dans une cession
    * @return Grid
    * @since 2.2.0
    */
   private Grid initiateGridForListeParent(){
      final Grid grid = new Grid();
      grid.setClass("gridListStyle");
      final Columns cols = new Columns();
      grid.appendChild(cols);

      final Column typeCol = new Column(Labels.getLabel("Champ.ProdDerive.ProdType"));
      final Column quantité = new Column(Labels.getLabel("Champ.ProdDerive.Quantite"));

      cols.appendChild(typeCol);
      cols.appendChild(quantité);

      return grid;
   }

   /**
    * Recherche les codes du parent en fonction du type sélctionné.
    * @param event Event : seléction sur la liste typesParentBoxDerive.
    * @throws Exception
    */
   public void onSelect$typesParentBoxDerive(final Event event) throws Exception{

      // on récupère le type sélectionné
      final int index = typesParentBoxDerive.getSelectedIndex();
      String tmp = "";
      if(index == 0){
         tmp = "Echantillon";
      }else if(index == 1){
         tmp = "Prelevement";
      }else if(index == 2){
         tmp = "ProdDerive";
      }else if(index == 3){
         tmp = "Aucun";
      }

      // en fonction du type, on recherche les codes disponibles
      if(!tmp.equals(getTypeParent())){
         setTypeParent(tmp);
         if(getTypeParent().equals("Echantillon")){
            codesParent = ManagerLocator.getEchantillonManager()
               .findAllCodesForDerivesByBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
            noParentMode(false);
         }else if(getTypeParent().equals("Prelevement")){
            codesParent = ManagerLocator.getPrelevementManager()
               .findAllCodesForBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
            noParentMode(false);
         }else if(getTypeParent().equals("ProdDerive")){
            codesParent = ManagerLocator.getProdDeriveManager()
               .findAllCodesForDerivesByBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
            noParentMode(false);
         }else if(getTypeParent().equals("Aucun")){
            codesParent = new ArrayList<>();
            noParentMode(true);
         }

         prodDerives.clear();
         prodDerivesDecorated = ProdDeriveDecorator2.decorateListe(prodDerives);
         final ListModel<ProdDeriveDecorator2> list = new ListModelList<>(prodDerivesDecorated);
         derivesList.setModel(list);
         getBinder().loadAttribute(derivesList, "model");

         // on initialise la combobox avec les codes extraits
         dictCodesModel = new CustomSimpleListModel(codesParent.toArray());
         codesParentBoxDerive.setModel(dictCodesModel);
         codesParentBoxDerive.setConstraint("");
         codesParentBoxDerive.clearErrorMessage();
         codesParentBoxDerive.setValue("");
         setCodePrefixe(null);
         codesParentBoxDerive.setConstraint(cttCodeParent);
      }
   }

   /**
    * Méthode appelée lors de la sélection du type du parent : s'il
    * n'y a pas de parents, on va cacher la liste des codes.
    * @param noParent si true, le dérivé n'aura pas de parent.
    */
   public void noParentMode(final boolean noParent){
      setParentObject(null);
      if(noParent){
         codeParentLabelDerive.setVisible(false);
         requiredCodeParentDerive.setVisible(false);
         codesParentBoxDerive.setVisible(false);
         rowTransformation1.setVisible(false);
         rowTransformation2.setVisible(false);
         setCodePrefixe(null);
         setTransformation(null);
         dateTransfoCalBox.setValue(null);
      }else{
         codeParentLabelDerive.setVisible(true);
         requiredCodeParentDerive.setVisible(true);
         codesParentBoxDerive.setVisible(true);
         rowTransformation1.setVisible(true);
         rowTransformation2.setVisible(true);
         setCodePrefixe(null);
         setTransformation(new Transformation());
      }
   }

   /*********************************************************/
   /********************** VALIDATION ***********************/
   /*********************************************************/
   private Constraint cttCodeParent = new ConstCodeParent();

   public Constraint getCttCodeParent(){
      return cttCodeParent;
   }

   public void setCttCodeParent(final Constraint ctt){
      this.cttCodeParent = ctt;
   }

   /**
    * Contrainte vérifiant que la combobox pour le code du parent n'est
    * pas nulle et que son contenu se trouve bien dans la liste.
    * @author Pierre Ventadour.
    *
    */
   private class ConstCodeParent implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value) throws WrongValueException{

         ((InputElement) comp).clearErrorMessage();

         final String code = (String) value;

         if(code == null || code.length() == 0){
            throw new WrongValueException(comp, Labels.getLabel("validation.syntax.empty"));
         }else if(!codesParent.contains(code)){
            throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.parent.not.liste"));
         }

      }

   }

   private Constraint cttQuantiteInit = new ConstQuantiteInit();

   @Override
   public Constraint getCttQuantiteInit(){
      return cttQuantiteInit;
   }

   public void setCttQuantiteInit(final Constraint ctt){
      this.cttQuantiteInit = ctt;
   }

   /**
    * Contrainte vérifiant que la quantiteInit.
    * @author Pierre Ventadour.
    *
    */
   private class ConstQuantiteInit implements Constraint
   {
      /**
       * Méthode de validation du champ quantiteInitBox.
       */
      @Override
      public void validate(final Component comp, final Object value){
         // on récupère la valeur dans quantiteInitBox
         final BigDecimal quantiteInitValue = (BigDecimal) value;

         // si une valeur est saisie dans le champ quantiteInitBox
         if(quantiteInitValue != null){
            setQuantiteInit(quantiteInitValue.floatValue());
            // la quantiteInit doit être positive
            if(getQuantiteInit() < 0){
               throw new WrongValueException(comp, Labels.getLabel("validation.negative.value"));
            }

            // la quantité init doit respecter la formule :
            // qteInit = volInit * concentration
            if(getProdDerive().getVolumeInit() != null && getProdDerive().getConc() != null){

               Float res = getProdDerive().getVolumeInit() * getProdDerive().getConc();

               res = ObjectTypesFormatters.floor(res, 3);
               if(!getQuantiteInit().equals(res)){
                  throw new WrongValueException(comp, Labels.getLabel("validation.invalid.formule" + ".quantite.init"));
               }
            }
         }
      }
   }

   /**
    * Valide le contenu d'une calendarBox
    * @param calendarBox
    */
   private void validateCalendarBox(final CalendarBox calendarBox){

      calendarBox.clearErrorMessage(calendarBox.getValue());

      final Calendar dateStockage = calendarBox.getValue();

      if(dateStockage != null){
         if(dateStockage.get(Calendar.YEAR) <= 9999){
            validateCoherenceDate(calendarBox, calendarBox.getValue());
         }else{
            throw new WrongValueException(calendarBox, Labels.getLabel("validation.invalid.date"));
         }
      }

   }

   /**
    * Applique la validation sur la date.
    */
   @Override
   public void onBlur$dateTransfoCalBox(){
      validateCalendarBox(dateTransfoCalBox);
   }

   /**
    * Applique la validation sur la date.
    */
   @Override
   public void onBlur$dateStockCalBox(){
      validateCalendarBox(dateStockCalBox);
   }

   /***********************************************************/
   /*****************  CONC VOL QTE ***************************/
   /***********************************************************/
   /**
    * Méthode appelée lors d'une modification du champ volumeInitBoxDerive.
    * En fonction du volume initial saisi, le volume restant sera
    * mis à jour.
    */
   @Override
   public void onBlur$volumeInitBoxDerive(){

      if(getProdDerive().getVolumeInit() != null){

         if(getProdDerive().getConc() != null){

            final Float res = getProdDerive().getVolumeInit() * getProdDerive().getConc();
            getProdDerive().setQuantiteInit(res);

         }
      }
   }

   /**
    * Méthode appelée lors d'une modification du champ quantiteInitBoxDerive.
    * En fonction de la quantité initiale saisie, la quantité restante sera
    * mise à jour.
    */
   @Override
   public void onBlur$concentrationBoxDerive(){

      if(getProdDerive().getConc() != null){

         if(getProdDerive().getVolumeInit() != null){

            final Float res = getProdDerive().getVolumeInit() * getProdDerive().getConc();
            getProdDerive().setQuantiteInit(res);

         }
      }
   }

   public void onSelect$typesBoxDerive(){
      if(getSelectedType() == null){
         Clients.scrollIntoView(typesBoxDerive);
         throw new WrongValueException(typesBoxDerive, Labels.getLabel("ficheProdDerive.error.type"));
      }
      Clients.clearWrongValue(typesBoxDerive);
   }

   /**
    * Clic sur le bouton générant un code.
    */
   @Override
   public void onClick$numerotation(){
      setCodePrefixe(generateCodeAndUpdateNumerotation());
   }

   public Date getDateSortie(){
      return dateSortie;
   }

   public void setDateSortie(final Date d){
      this.dateSortie = d;
   }

   public ProdDeriveDecoratorRowRenderer getDeriveDecoRenderer(){
      return deriveDecoRenderer;
   }

   public Label getRequiredTransfoQuantiteLabel() {
      return requiredTransfoQuantiteLabel;
   }

   public void setRequiredTransfoQuantiteLabel(Label requiredTransfoQuantiteLabel) {
      this.requiredTransfoQuantiteLabel = requiredTransfoQuantiteLabel;
   }

   public void onClick$changeNumerotation(){
      changeNumerotation.setVisible(false);
      choixNumerotation.setVisible(true);
   }

   public void onCheck$numNombres(){
      premierCodeBoxDerive.setVisible(true);
      dernierCodeBoxDerive.setVisible(true);
      premiereLettreBoxDerive.setVisible(false);
      derniereLettreBoxDerive.setVisible(false);
      Clients.clearWrongValue(premiereLettreBoxDerive);
      premiereLettreBoxDerive.setConstraint("");
      premiereLettreBoxDerive.setValue(null);
      premiereLettreBoxDerive.setConstraint(cttPremiereLettre);
      Clients.clearWrongValue(derniereLettreBoxDerive);
      derniereLettreBoxDerive.setConstraint("");
      derniereLettreBoxDerive.setValue(null);
      derniereLettreBoxDerive.setConstraint(cttDerniereLettre);
      changeNumerotation.setVisible(true);
      choixNumerotation.setVisible(false);
      changeNumerotation.setImage("/images/icones/smallNumber.png");
   }

   public void onCheck$numLettres(){
      premierCodeBoxDerive.setVisible(false);
      dernierCodeBoxDerive.setVisible(false);
      premiereLettreBoxDerive.setVisible(true);
      derniereLettreBoxDerive.setVisible(true);
      Clients.clearWrongValue(premierCodeBoxDerive);
      premierCodeBoxDerive.setConstraint("");
      premierCodeBoxDerive.setValue(null);
      premierCodeBoxDerive.setConstraint(cttPremierCode);
      Clients.clearWrongValue(dernierCodeBoxDerive);
      dernierCodeBoxDerive.setConstraint("");
      dernierCodeBoxDerive.setValue(null);
      dernierCodeBoxDerive.setConstraint(cttDernierCode);
      changeNumerotation.setVisible(true);
      choixNumerotation.setVisible(false);
      changeNumerotation.setImage("/images/icones/smallLetter.png");
   }

   public String getPremiereLettre(){
      return premiereLettre;
   }

   public void setPremiereLettre(final String premiereLettre){
      this.premiereLettre = premiereLettre;
   }

   public String getDerniereLettre(){
      return derniereLettre;
   }

   public void setDerniereLettre(final String derniereLettre){
      this.derniereLettre = derniereLettre;
   }

   public Constraint getCttPremiereLettre(){
      return cttPremiereLettre;
   }

   public void setCttPremiereLettre(final Constraint c){
      this.cttPremiereLettre = c;
   }

   public Constraint getCttDerniereLettre(){
      return cttDerniereLettre;
   }

   public void setCttDerniereLettre(final Constraint c){
      this.cttDerniereLettre = c;
   }

   /**
    * Applique les codes obtenus depuis le scan dans l'ordre aux codes dérivés
    * en attente de création dans la liste de travail.
    * @param sT scanTerminale
    */
   public void applyTKObjectsCodesFromScan(final ScanTerminale sT){
      if(!getAddedProdDerives().isEmpty() && !sT.getScanTubes().isEmpty()){
         int i = 0;

         Textbox tb;
         for(final ProdDeriveDecorator2 deco : getProdDerivesDecorated()){
            if(deco.isNew() && deco.getAdrlTmp() == null){
               if(i < sT.getScanTubes().size()){
                  tb = (Textbox) derivesList.getRows().getChildren().get(getProdDerivesDecorated().indexOf(deco)).getFirstChild()
                     .getNextSibling();
                  tb.setValue(sT.getScanTubes().get(i).getCode());
                  Events.postEvent("onChange", tb, null);
                  i++;
               }else{
                  break;
               }
            }
         }
      }
   }

   /**
    * Initialise la quantité utilisée obligatoire à partir de la session.
    *
    * Note: La condition d'affichage de l'astérisque rouge est gérée dynamiquement dans cette méthode.
    * Si la quantité utilisée n'est pas obligatoire, l'astérisque est masqué, sinon il est affiché.
    *
    */
   private void initializeQuantiteUtiliseObligatoireFromSession(){
      // Récupérer le Enum
      EParametreValeurParDefaut deriveQteObligatoire = EParametreValeurParDefaut.DERIVE_QTE_OBLIGATOIRE;
      // Obtenir le DTO associé au paramètre
      ParametreDTO deriveQteObligatoireDto = SessionUtils.getParametreByCode(deriveQteObligatoire.getCode(), sessionScope);
      // Définir la visibilité du label

      isQuantiteObligatoire = Boolean.parseBoolean(deriveQteObligatoireDto.getValeur());

      requiredTransfoQuantiteLabel.setVisible(isQuantiteObligatoire);

   }

   /*************************************************************************/
   /************************** INTERFACAGES *********************************/
   /*************************************************************************/
   public void onClick$interfacage(){
      final String value = codePrefixeLabelDerive.getValue();
      dossierExt = null;
      openSelectDossierExterneWindow(page, Path.getPath(self), value, false, null, true);
   }

   /**
    * Récupère le résultat de l'injection d'un dossier externe
    * sous la forme dérivé.
    * @param e
    */
   public void onGetInjectionDossierExterneDone(final Event e){
      if(e.getData() != null){

         final ResultatInjection res = (ResultatInjection) e.getData();

         if(res != null){
            dossierExt = res.getDossierExterne();
            ProdDerive newObj = null;
            if(res.getProdDerive() != null){
               newObj = res.getProdDerive().clone();
               newObj.setBanque((Banque) sessionScope.get("Banque"));
               // recup info transformation
               newObj.setDateTransformation(getObject().getDateTransformation());
               setObject(newObj);
               setSelectedType(newObj.getProdType());
               setSelectedCollaborateur(newObj.getCollaborateur());
               setSelectedQuantiteUnite(newObj.getQuantiteUnite());
               setSelectedModePrepaDerive(newObj.getModePrepaDerive());
               setSelectedQualite(newObj.getProdQualite());
               setSelectedVolumeUnite(newObj.getVolumeUnite());
               setSelectedConcUnite(newObj.getConcUnite());
               selectOperateur();
            }

            if(getObjectTabController() != null && getObjectTabController().getFicheAnnotation() != null
               && res.getAnnosEchantillon() != null && !res.getAnnosEchantillon().isEmpty()){
               getObjectTabController().getFicheAnnotation().setAnnotationValues(res.getAnnosDerive());
            }

            getBinder().loadComponent(self);
         }
      }
   }

   /**
    * Méthode inspirée initCollaborations et allégée pour appel uniquement
    * dans injection opérateur depuis interfaçage.
    */
   private void selectOperateur(){
      if(getObject().getCollaborateur() != null){
         setSelectedCollaborateur(this.getObject().getCollaborateur());
         collabBoxDerive.setValue(getSelectedCollaborateur().getNomAndPrenom());
      }else{
         setSelectedCollaborateur(null);
      }
   }

  @Override
   public void setFocusOnElement(){
     // si l'origine est liste dérivée le focus sera sur Code du parent
      if (getParentObject() == null){
         codesParentBoxDerive.setFocus(true);
      } else {
         dateTransfoCalBox.setFocus(true);
      }
  }



}
