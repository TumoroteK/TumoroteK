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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.prelevement.gatsbi.GatsbiControllerPrelevement;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.helper.FileBatch;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche de création de plusieurs échantillons. Controller
 * créé le 26/11/2009.
 *
 * @author Pierre Ventadour
 * @version 2.2.1-IRELEC
 *
 */
public class FicheMultiEchantillons extends FicheEchantillonEdit
{

   private final Logger log = LoggerFactory.getLogger(FicheMultiEchantillons.class);

   private static final long serialVersionUID = 3863329092781960062L;

   // Buttons
   protected Button previous;

   // Editable components : mode d'édition ou de création.
   protected Label operateurAideSaisieEchan;

   protected Combobox separatorBox;

   protected Intbox premierCodeBoxEchan;

   protected Intbox dernierCodeBoxEchan;

   protected Textbox premiereLettreBoxEchan;

   protected Textbox derniereLettreBoxEchan;

   protected Radio numNombres;

   protected Radio numLettres;

   protected HtmlBasedComponent choixNumerotation;

   protected Button changeNumerotation;

   protected Grid echantillonsList;

   protected Button stockageEchantillons;

   // Objets Principaux.
   private Maladie maladie = new Maladie();

   private List<Echantillon> echantillons = new ArrayList<>();

   private List<Object> addedEchantillons = new ArrayList<>();

   private List<EchantillonDTO> echantillonsDecorated = new ArrayList<>();

   private final Map<TKStockableObject, Emplacement> echansEmpl = new HashMap<>();

   private List<String> usedCodesEchantillons = new ArrayList<>();

   // Variables formulaire.
   private SimpleListModel<String> connaissances = new SimpleListModel<>(Arrays.asList("OUI", "NON"));

   private Integer premierCode;

   private Integer dernierCode;

   private String premiereLettre;

   private String derniereLettre;

   private boolean isPrelevementProcedure = false;

   private final AbstractEchantillonDecoratorRowRenderer echanDecoRenderer = new EchantillonDecoratorRowRenderer();

   private final List<FileBatch> batches = new ArrayList<>();

   // @since 2.2.2-diamic evolutions
   private Listbox injectableEchantillonsBox;

   /**
    * Indique au comportement de la fiche si elle appartient à la procédure
    * d'enregistrement d'un prélèvement ou non.
    *
    * @param isProcedure true si procédure.
    */
   public void setPrelevementProcedure(final boolean isProcedure){
      this.isPrelevementProcedure = isProcedure;

      // bouton pour revenir sur la fiche prlvt
      this.previous.setVisible(isProcedure);
      if(isProcedure){
         setWaitLabel("prelevement.creation.encours");
      }else{
         setWaitLabel("echantillon.creation.encours");
      }
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      if(getDroitOnAction("Stockage", "Consultation")){
         stockageEchantillons.setVisible(true);
      }else{
         stockageEchantillons.setVisible(false);
      }

      getCodesOrganeController().setIsOrg(true);
      getCodesMorphoController().setIsOrg(false);
      getCodesMorphoController().setIsMorpho(true);

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject echan){
      setEchantillon((Echantillon) echan);
      super.setObject(echan);
   }

   @Override
   public void setEchantillon(final Echantillon e){
      // indispensable pour le rendu des codes assignés
      // dans la table temporaire
      e.setBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      super.setEchantillon(e);
   }

   /**
    * Fiche en mode creation classique avec passage ou non d'un prelevement parent.
    */
   public void switchToCreateMode(final Prelevement prel){

      // Init du parent
      setParentObject(prel);

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();
      initQuantiteAndVolume();
      initAssociations();

      // affiche le selecteur de parent au besoin.
      if(!isPrelevementProcedure){
         if(getParentObject() == null){
            addSelectParentMode();
         }else{
            sterileBox.setChecked(initSterileOrNot());
         }
      }

      // since 2.0.13
      initFormFromDossierExterne();

      super.switchToCreateMode();
      
      getObjectTabController().setCodeUpdated(false);
      getObjectTabController().setOldCode(null);

      scrollToTop();

      if(!getDroitOnAction("Collaborateur", "Consultation")){
         operateurAideSaisieEchan.setVisible(false);
      }

      setFocusOnElement();

      getBinder().loadComponent(self);
   }

   @Override
   public void onClick$create(){

      boolean ok = false;

      if(addedEchantillons.size() == 0){
         if(Messagebox.show(Labels.getLabel("message.noEchantillonAdded"), Labels.getLabel("message.save.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            // on vérifie que l'on vient bien de la fiche
            // prelevement
            if(getPrelevementController().hasFicheLaboInter()){
               getPrelevementController().getFicheLaboInter().onClick$create();
            }else{
               // si ce n'est pas le cas, aucune modif n'a été
               // fait sur le prlvt, aucun nouveaux echantillons
               // on va donc juste sauver les emplacements
               Clients.showBusy(Labels.getLabel(getWaitLabel()));
               Events.echoEvent("onLaterSaveEmplacements", self, null);
            }
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

   public void onLaterSaveEmplacements(){
      try{
         // enregistrement des emplacements
         saveEmplacements();

         createFileHtmlToPrint();

         getObjectTabController().onCancel();

         // ferme wait message
         Clients.clearBusy();

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }

   }

   @Override
   public boolean onLaterCreate(){

      try{
         createNewObject();

         // ajout des echantillons
         getObjectTabController().getListe().addListToObjectList(addedEchantillons);

         if(isPrelevementProcedure){
            getPrelevementController().getListe().addToObjectList(getParentObject());
         }else{
            if(getParentObject() != null){
               if(getPrelevementController() != null){
                  // update du parent dans la liste
                  getPrelevementController().getListe().updateObjectGridListFromOtherPage(getParentObject(), true);
               }
               // retour vers la fiche prelevement au besoin
               if(getObjectTabController().getFromFichePrelevement()){
                  getObjectTabController().setFromFichePrelevement(false);
                  PatientController.backToMe(getMainWindow(), page);
               }
            }
         }

         // update patient
         if(getParentObject() != null && getParentObject().getMaladie() != null){
            if(!getPrelevementController().getReferencingObjectControllers().isEmpty()){
               getPrelevementController().getReferencingObjectControllers().get(0).getListe()
                  .updateObjectGridListFromOtherPage(getParentObject().getMaladie().getPatient(), true);
            }
         }

         getObjectTabController().onCreateDone();

         // commande le passage en mode statique
         if(isPrelevementProcedure){
            getPrelevementController().setNextToEchanClicked(false);
            getPrelevementController().onCreateDone();
            // retour vers la fiche patient au besoin
            if(getPrelevementController().getFromFichePatient()){
               getPrelevementController().setFromFichePatient(false);
               PatientController.backToMe(getMainWindow(), page);
            }else{
               PrelevementController.backToMe(getMainWindow(), page);
            }
         }

         // ferme wait message
         Clients.clearBusy();

         // si c'est une procédure de création d'un prlvt, on
         // demande si l'utilisateur veut en creer un autre
         if(isPrelevementProcedure){
            getPrelevementController().createAnotherPrelevement(getParentObject());
         }

         return true;
      }catch(final DoublonFoundException re){
         Clients.clearBusy();

         final HashMap<String, Object> map = new HashMap<>();
         map.put("title", Labels.getLabel("error.unhandled"));
         map.put("message", handleExceptionMessage(re));
         map.put("exception", re);

         final Window window = (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
         window.doModal();

         return false;
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   @Override
   public void createNewObject(){

      boolean revertMaladie = false;
      boolean revertPatient = false;

      // Enregistrement ou reference vers le prelevement
      if(isPrelevementProcedure){
         getParentObject().setLaboInters(new HashSet<>(getLaboInters()));

         if(getParentObject().getMaladie() != null){
            revertMaladie = getParentObject().getMaladie().getMaladieId() == null;
            if(revertMaladie){
               revertPatient = getParentObject().getMaladie().getPatient().getPatientId() == null;
            }
         }

         getPrelevementController().getFicheAnnotation().populateValeursActionLists(true, false);

      }else{ // reference vers le patient
             // si l'utilisateur devait sélectionner un prlvt parent
         if(selectParent && connaissancesBoxEchan.getSelectedIndex() == 0){
            setParentObject(getPrelevementFromSelect());
            setBanque(getParentObject().getBanque());
         }
      }

      final List<File> filesCreated = new ArrayList<>();

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("updatePrelAndEchansTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);

      try{
         if(isPrelevementProcedure){

            // enregistrement du prelevement
            ManagerLocator.getPrelevementManager().createObjectManager(getParentObject(),
               GatsbiController.enrichesBanqueWithEtudeContextes(getBanque(), sessionScope), getParentObject().getNature(),
               getParentObject().getMaladie(), getParentObject().getConsentType(), getParentObject().getPreleveur(),
               getParentObject().getServicePreleveur(), getParentObject().getPrelevementType(), getParentObject().getConditType(),
               getParentObject().getConditMilieu(), getParentObject().getTransporteur(), getParentObject().getOperateur(),
               getParentObject().getQuantiteUnite(), new ArrayList<>(getParentObject().getLaboInters()),
               getPrelevementController().getFicheAnnotation().getValeursToCreateOrUpdate(), filesCreated,
               SessionUtils.getLoggedUser(sessionScope), true, SessionUtils.getSystemBaseDir(), false);
         }

         Fichier crAnapath = null;
         String crDataType = null;
         String crPath = null;

         // enregistrement de l'echantillon
         for(int i = 0; i < echantillonsDecorated.size(); i++){
            if(echantillonsDecorated.get(i).isNew()){
               final Echantillon newEchan = echantillonsDecorated.get(i).getEchantillon();

               // nettoie les code de la reference vers l'echantillon
               // utilisé comme base objet pour la creation multiple
               final List<CodeAssigne> codes = new ArrayList<>();
               codes.addAll(echantillonsDecorated.get(i).getCodesOrgsToCreateOrEdit());
               codes.addAll(echantillonsDecorated.get(i).getCodesLesToCreateOrEdit());

               // recupere path + data type si nouveau strea
               if(newEchan.getCrAnapath() != null && newEchan.getAnapathStream() == null){
                  newEchan.getCrAnapath().setMimeType(crDataType);
                  newEchan.getCrAnapath().setPath(crPath);
               }
               crAnapath = newEchan.getCrAnapath();
               newEchan.setCrAnapath(null);

               // création de l'objet
               ManagerLocator.getEchantillonManager().createObjectWithCrAnapathManager(newEchan,
                  GatsbiController.enrichesBanqueWithEtudeContextes(getBanque(), sessionScope), getParentObject(),
                  newEchan.getCollaborateur(),
                  ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0), null, // since 2.0.13.2
                  newEchan.getEchantillonType(), codes, newEchan.getQuantiteUnite(), newEchan.getEchanQualite(),
                  newEchan.getModePrepa(), crAnapath, newEchan.getAnapathStream(), filesCreated,
                  echantillonsDecorated.get(i).getValeursToCreateOrUpdate(), SessionUtils.getLoggedUser(sessionScope), true,
                  SessionUtils.getSystemBaseDir(), false);

               if(newEchan.getAnapathStream() != null){
                  crDataType = newEchan.getCrAnapath().getMimeType();
                  crPath = newEchan.getCrAnapath().getPath();
               }
            }
         }

         // enregistrement des emplacements
         saveEmplacements();

         // gestion de la non conformité
         if(isPrelevementProcedure){
            if(getParentObject().getConformeArrivee() != null && !getParentObject().getConformeArrivee()){
               ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(getParentObject(),
                  getPrelevementController().getFicheLaboInter().findSelectedNonConformites(), "Arrivee");
            }

            // suppression du patientSip
            getPrelevementController().removePatientSip();
         }

         for(int i = 0; i < echantillonsDecorated.size(); i++){

            // 2.1.4.1 FIX raisons non conformités disp ajout échantillons
            // https://tumorotek.myjetbrains.com/youtrack/issue/TK-281
            if(echantillonsDecorated.get(i).isNew()){

               if(echantillonsDecorated.get(i).getEchantillon().getConformeTraitement() != null
                  && !echantillonsDecorated.get(i).getEchantillon().getConformeTraitement()){
                  // enregistrement de la non conformité
                  // après traitement
                  ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(
                     echantillonsDecorated.get(i).getEchantillon(), echantillonsDecorated.get(i).getNonConformiteTraitements(),
                     "Traitement");
               }

               if(echantillonsDecorated.get(i).getEchantillon().getConformeCession() != null
                  && !echantillonsDecorated.get(i).getEchantillon().getConformeCession()){
                  // enregistrement de la non conformité
                  // à la cession
                  ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(
                     echantillonsDecorated.get(i).getEchantillon(), echantillonsDecorated.get(i).getNonConformiteCessions(),
                     "Cession");
               }
            }
         }

         // annotation file batches
         for(final FileBatch batch : batches){
            ManagerLocator.getAnnotationValeurManager().createFileBatchForTKObjectsManager(batch.getObjs(), batch.getFile(),
               batch.getStream(), batch.getChamp(), getBanque(), SessionUtils.getLoggedUser(sessionScope),
               SessionUtils.getSystemBaseDir(), filesCreated);
         }

      }catch(final RuntimeException re){
         ManagerLocator.getTxManager().rollback(status);

         for(final File f : filesCreated){
            f.delete();
         }

         if(isPrelevementProcedure){
            if(revertMaladie){
               getParentObject().getMaladie().setMaladieId(null);
               if(revertPatient){
                  getParentObject().getMaladie().getPatient().setPatientId(null);
               }
            }

            getParentObject().setPrelevementId(null);
            // revert Objects
            final Iterator<LaboInter> it = getParentObject().getLaboInters().iterator();
            while(it.hasNext()){
               it.next().setLaboInterId(null);
            }
         }
         final Iterator<EchantillonDTO> itE = echantillonsDecorated.iterator();

         Echantillon e;
         EchantillonDTO ed;
         final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", false).get(0);
         final ObjetStatut nonstocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", false).get(0);
         while(itE.hasNext()){
            ed = itE.next();
            e = ed.getEchantillon();
            if(e.getEchantillonId() != null){
               e.setEchantillonId(null);
               if(e.getCrAnapath() != null){
                  e.getCrAnapath().setFichierId(null);
               }
               // revert emplacement since 2.0.13.2
               // au stade onGetResultsFromStockage
               // se basant sur echEmpls
               if(echansEmpl.containsKey(e)){
                  e.setObjetStatut(stocke);
                  e.setEmplacement(null);
                  // clean echanEmpl from emplacement in error
                  // doublon ou emplacement occupé
                  if((re instanceof EmplacementDoublonFoundException
                     && echansEmpl.get(e).equals(((EmplacementDoublonFoundException) re).getEmplacementMock()))
                     || (re instanceof TKException && re.getMessage().equals("error.emplacement.notEmpty")
                        && ((TKException) re).getTkObj().equals(e))){
                     echansEmpl.remove(e);
                     // reset decorateur
                     ed.setAdrlTmp(null);
                     ed.getEchantillon().setObjetStatut(nonstocke);
                  }
               }

            }
            // la boucle arrive a l'echantillon planté.
            else{
               break;
            }
         }

         updateDecoList(echansEmpl);

         throw re;
      }
      ManagerLocator.getTxManager().commit(status);

      // envoi informations exterieures
      if(getParentObject() != null){
         getPrelevementController().handleExtCom(null, getParentObject(), getObjectTabController());
      }

      // 2.2.1-IRELEC removed
      // postStorageData(echansEmpl);

      createFileHtmlToPrint();

   }

   /**
    * Recupere l'objet prelevement à partir du module de selection.
    *
    * @return prelevement parent choisi.
    */
   private Prelevement getPrelevementFromSelect(){
      if(selectParentRowEchan.isVisible()){
         if(codesParentBoxEchan.isVisible()){
            // on valide la sélection
            cttCodeParentEchan.validate(codesParentBoxEchan, null);
            final String tmp = codesParentBoxEchan.getSelectedItem().getValue();
            // on récupère le prlvt en fonction de son code
            return ManagerLocator.getPrelevementManager().findByCodeLikeManager(tmp, true).get(0);
         }
      }
      return getParentObject();
   }

   @Override
   public void onClick$cancel(){
      super.onClick$cancel();
      if(isPrelevementProcedure){
         getPrelevementController().setNextToEchanClicked(false);
         if(getPrelevementController().hasFicheLaboInter()){
            getPrelevementController().getFicheLaboInter().onClick$cancel();
         }
      }
   }

   @Override
   public void onClick$validate(){

      boolean ok = false;

      if(addedEchantillons.size() == 0){
         if(Messagebox.show(Labels.getLabel("message.noEchantillonAdded"), Labels.getLabel("message.save.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            getPrelevementController().getFicheLaboInter().onClick$validate();
            saveEmplacements();
            createFileHtmlToPrint();
         }else{
            ok = false;
         }
      }else{
         ok = true;
      }

      if(ok){
         super.onClick$validate();
      }
   }

   @Override
   public boolean onLaterUpdate(){

      try{
         // updateObjectWithAnnots();
         updateObject();

         // ajout des echantillons
         for(int i = 0; i < addedEchantillons.size(); i++){
            getObjectTabController().getListe().addToObjectList(addedEchantillons.get(i));
         }

         getPrelevementController().getListe().updateObjectGridList(getParentObject());

         // update patient
         if(getParentObject().getMaladie() != null){
            if(!getPrelevementController().getReferencingObjectControllers().isEmpty()){
               getPrelevementController().getReferencingObjectControllers().get(0).getListe()
                  .updateObjectGridListFromOtherPage(getParentObject().getMaladie().getPatient(), true);
            }
         }

         // commande le passage en mode statique
         getObjectTabController().onCreateDone();
         getPrelevementController().setNextToEchanClicked(false);
         getPrelevementController().onEditDone(getParentObject());

         PrelevementController.backToMe(getMainWindow(), page);

         // ferme wait message
         Clients.clearBusy();

         getPrelevementController().showEchantillonsAfterUpdate(getPrelevement());
         return true;

      }catch(final DoublonFoundException re){
         Clients.clearBusy();

         final HashMap<String, Object> map = new HashMap<>();
         map.put("title", Labels.getLabel("error.unhandled"));
         map.put("message", handleExceptionMessage(re));
         map.put("exception", re);

         final Window window = (Window) Executions.createComponents("/zuls/component/DynamicMultiLineMessageBox.zul", null, map);
         window.doModal();

         return false;
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   /**
    * Modification de l'objet. Transactionnel.
    *
    * @return True si l'objet est modifié.
    */
   @Override
   public void updateObject(){

      final List<File> filesCreated = new ArrayList<>();

      final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setName("updatePrelAndEchansTx");
      def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

      final TransactionStatus status = ManagerLocator.getTxManager().getTransaction(def);
      try{
         getPrelevementController().getFicheLaboInter().updateObjectWithAnnots();

         setParentObject(getPrelevementController().getFicheLaboInter().getObject());
         getParentObject().setLaboInters(
            new HashSet<>(ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(getParentObject())));

         Fichier crAnapath = null;
         String crDataType = null;
         String crPath = null;

         for(int i = 0; i < echantillonsDecorated.size(); i++){
            if(echantillonsDecorated.get(i).isNew()){
               final Echantillon newEchan = echantillonsDecorated.get(i).getEchantillon();
               // nettoie les code de la reference vers l'echantillon
               // utilisé comme base objet pour la creation multiple
               final List<CodeAssigne> codes = new ArrayList<>();
               codes.addAll(echantillonsDecorated.get(i).getCodesOrgsToCreateOrEdit());
               codes.addAll(echantillonsDecorated.get(i).getCodesLesToCreateOrEdit());

               // recupere path + data type si nouveau strea
               if(newEchan.getCrAnapath() != null && newEchan.getAnapathStream() == null){
                  newEchan.getCrAnapath().setMimeType(crDataType);
                  newEchan.getCrAnapath().setPath(crPath);
               }
               crAnapath = newEchan.getCrAnapath();
               newEchan.setCrAnapath(null);

               // création de l'objet
               ManagerLocator.getEchantillonManager().createObjectWithCrAnapathManager(newEchan,
                  GatsbiController.enrichesBanqueWithEtudeContextes(getBanque(), sessionScope), getParentObject(),
                  newEchan.getCollaborateur(),
                  ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0),
                  // newEchan.getEmplacement(),
                  null, // since 2.0.13.2
                  newEchan.getEchantillonType(), codes, newEchan.getQuantiteUnite(), newEchan.getEchanQualite(),
                  newEchan.getModePrepa(), crAnapath, newEchan.getAnapathStream(), filesCreated,
                  echantillonsDecorated.get(i).getValeursToCreateOrUpdate(), SessionUtils.getLoggedUser(sessionScope), true,
                  SessionUtils.getSystemBaseDir(), false);

               if(newEchan.getAnapathStream() != null){
                  crDataType = newEchan.getCrAnapath().getMimeType();
                  crPath = newEchan.getCrAnapath().getPath();
               }
            }
         }

         // enregistrement des emplacements
         saveEmplacements();

         // gestion de la non conformité
         for(int i = 0; i < echantillonsDecorated.size(); i++){

            // 2.1.4.1 FIX raisons non conformités disp ajout échantillons
            // https://tumorotek.myjetbrains.com/youtrack/issue/TK-281
            if(echantillonsDecorated.get(i).isNew()){

               if(echantillonsDecorated.get(i).getEchantillon().getConformeTraitement() != null
                  && !echantillonsDecorated.get(i).getEchantillon().getConformeTraitement()){
                  // enregistrement de la non conformité
                  // après traitement
                  ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(
                     echantillonsDecorated.get(i).getEchantillon(), echantillonsDecorated.get(i).getNonConformiteTraitements(),
                     "Traitement");
               }

               if(echantillonsDecorated.get(i).getEchantillon().getConformeCession() != null
                  && !echantillonsDecorated.get(i).getEchantillon().getConformeCession()){
                  // enregistrement de la non conformité
                  // à la cession
                  ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(
                     echantillonsDecorated.get(i).getEchantillon(), echantillonsDecorated.get(i).getNonConformiteCessions(),
                     "Cession");
               }
            }
         }

      }catch(final RuntimeException ex){
         ManagerLocator.getTxManager().rollback(status);

         for(final File f : filesCreated){
            f.delete();
         }

         final Iterator<EchantillonDTO> itE = echantillonsDecorated.iterator();
         final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", false).get(0);
         final ObjetStatut nonstocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", false).get(0);
         Echantillon e;
         EchantillonDTO ed;
         while(itE.hasNext()){
            ed = itE.next();
            e = ed.getEchantillon();
            if(ed.isNew()){
               e.setEchantillonId(null);
               if(e.getCrAnapath() != null){
                  e.getCrAnapath().setFichierId(null);
               }
            }
            // revert emplacement since 2.0.13.2
            // au stade onGetResultsFromStockage
            // se basant sur echEmpls
            if(echansEmpl.containsKey(e)){
               e.setObjetStatut(stocke);
               e.setEmplacement(null);
               // clean echanEmpl from emplacement in error
               // doublon ou emplacement occupé
               if((ex instanceof EmplacementDoublonFoundException
                  && echansEmpl.get(e).equals(((EmplacementDoublonFoundException) ex).getEmplacementMock()))
                  || (ex instanceof TKException && ex.getMessage().equals("error.emplacement.notEmpty")
                     && ((TKException) ex).getTkObj().equals(e))){
                  echansEmpl.remove(e);
                  // reset decorateur
                  ed.setAdrlTmp(null);
                  ed.getEchantillon().setObjetStatut(nonstocke);
               }
            }
         }
         updateDecoList(echansEmpl);

         throw ex;
      }
      ManagerLocator.getTxManager().commit(status);

      // envoi informations exterieures
      if(getParentObject() != null){
         getPrelevementController().handleExtCom(null, getParentObject(), getObjectTabController());
      }

      // 2.2.1-IRELEC removed
      // postStorageData(echansEmpl);

      createFileHtmlToPrint();
   }

   /**
    * Méthode qui va sauvegarder le stockage des échantillons.
    */
   public void saveEmplacements(){

      // on parcourt la hashtable du stockage et on extrait
      // l'emplacement de chaque échantillon
      final List<Emplacement> emplsFinaux = new ArrayList<>();
      Set<TKStockableObject> echans = echansEmpl.keySet();
      Iterator<TKStockableObject> it = echans.iterator();
      while(it.hasNext()){
         final Echantillon echan = (Echantillon) it.next();
         final Emplacement empl = echansEmpl.get(echan);
         empl.setObjetId(echan.getEchantillonId());
         emplsFinaux.add(empl);
      }

      // enregistrement des emplacements
      ManagerLocator.getEmplacementManager().saveMultiEmplacementsManager(emplsFinaux);

      // on va MAJ chaque échantillon : son statut et son
      // emplacement
      echans = echansEmpl.keySet();
      it = echans.iterator();
      final ObjetStatut statut = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);
      while(it.hasNext()){
         final Echantillon echanToUpdate = (Echantillon) it.next();
         final List<OperationType> ops = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Stockage", true);

         // update de l'objet
         ManagerLocator.getEchantillonManager().saveEchantillonEmplacementManager(echanToUpdate, statut,
            echansEmpl.get(echanToUpdate), SessionUtils.getLoggedUser(sessionScope), ops);

         final EchantillonDTO deco = new EchantillonDTO(echanToUpdate);
         // si l'échantillon existait déjà, on MAJ sa fiche
         // dans la liste des échantillons
         if(!addedEchantillons.contains(deco.getEchantillon())){
            // update de l'échantillon dans la liste
            getObjectTabController().getListe().updateObjectGridListFromOtherPage(echanToUpdate, false);
         }
      }
   }

   /**
    * Méthode qui permet d'afficher la page d'impression de la cession.
    */
   public void createFileHtmlToPrint(){
      // s'il y a des éléments a stocker
      if(echansEmpl.size() > 0){
         // création du document, de la page et de son titre
         final Document doc = ManagerLocator.getXmlUtils().createJDomDocumentBoites();
         final Element root = doc.getRootElement();
         final Element page =
            ManagerLocator.getXmlUtils().addPageBoite(root, Labels.getLabel("impression.boite.title.stockage.echantillons"));

         final List<BoiteImpression> listeBoites = new ArrayList<>();

         // on parcourt la hashtable du stockage et on extrait
         // chaque dérivé
         final Set<TKStockableObject> echans = echansEmpl.keySet();
         // pour chaque échantillon
         for(int k = 0; k < echantillonsDecorated.size(); k++){
            // s'il vient d'être stocké
            if(echans.contains(echantillonsDecorated.get(k).getEchantillon())){
               final Echantillon ech = echantillonsDecorated.get(k).getEchantillon();

               // on récupère son emplacement et la boite
               final Emplacement emp = ManagerLocator.getEchantillonManager().getEmplacementManager(ech);
               final Terminale term = emp.getTerminale();

               final BoiteImpression newBi = new BoiteImpression();
               newBi.setBoite(term);

               // si c'est la 1ère fois que l'on rencontre cette boite
               if(!listeBoites.contains(newBi)){
                  // on remplit tous titres et légendes
                  newBi.setTitreModelisation(Labels.getLabel("impression.boite.title.visualisation"));
                  newBi.setTitreInstructions(Labels.getLabel("impression.boite.title.instructions"));
                  newBi.setNom(ObjectTypesFormatters.getLabel("impression.boite.nom", new String[] {term.getNom()}));
                  newBi.setTitreListe(Labels.getLabel("impression.boite.elements" + ".title.stockage.echantillons"));
                  newBi.setLegendeVide(Labels.getLabel("impression.boite.legende.vide"));
                  newBi.setLegendePris(Labels.getLabel("impression.boite.legende.pris"));
                  newBi.setLegendeSelectionne(Labels.getLabel("impression.boite.legende.selectionne" + ".stockage.echantillons"));

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
                  instructions.add(Labels.getLabel("impression.boite.instruction" + ".stockage.echantillons"));
                  newBi.setInstructions(instructions);

                  // ajout du dérivé à la liste des éléments
                  // a extraire
                  final List<String> elements = new ArrayList<>();
                  elements.add(
                     ObjectTypesFormatters.getLabel("impression.boite.numero.echantillon", new String[] {"1", ech.getCode()}));
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
                  bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.echantillon",
                     new String[] {Integer.toString(pos), ech.getCode()}));
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
   public void onClick$revert(){
      if(isPrelevementProcedure){
         getPrelevementController().getFicheLaboInter().onClick$revert();
      }
      getObjectTabController().onCancel();
   }

   /**
    * Revient sur la page FicheLaboInter dans l'état où elle était avant d'arriver
    * sur la page FicheMultiEchantillons.
    */
   public void onClick$previous(){
      clearConstraints();
      getPrelevementController().switchFromEchantillonsToLabo();
      
      // @since 2.3.0-gatsbi, retour direct à la première page 
      // si pas d'affichage de page site intermédiaire
      if (!GatsbiControllerPrelevement
            .isSitesIntermPageDisplayed(SessionUtils.getCurrentGatsbiContexteForEntiteId(2))) {
         getPrelevementController().switchFromLaboToPrelevement();
      }
   }

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand l'utilisateur
    * sélectionne un collaborateur.
    *
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
      collabBox.setModel(new CustomSimpleListModel(getNomsAndPrenoms()));

      // si un collaborateur a été sélectionné
      if(e.getData() != null){
         final Collaborateur coll = (Collaborateur) e.getData();
         if(getNomsAndPrenoms().contains(coll.getNomAndPrenom())){
            final int ind = getNomsAndPrenoms().indexOf(coll.getNomAndPrenom());
            setSelectedCollaborateur(getCollaborateurs().get(ind));
            collabBox.setValue(getSelectedCollaborateur().getNomAndPrenom());
         }
      }
   }

   /**
    * Méthode exécutée lors du clic sur le bouton addEchantillons. Les nouveaux
    * échantillons seront créés (mais pas encore sauvegardés) et ajoutés à la
    * liste.
    *
    * @param event : clic sur le lien addEchantillons.
    */
   public void onClick$addEchantillons(final Event event){
      try{

         // gestion des bornes pour la création
         // @since 2.2.2-diamic, first and last peuvent être vides, mais seulement en
         // même temps
         int first = -1;
         int last = -1;
         if(numNombres.isChecked()){
            first = premierCode != null ? premierCode : -1;
            last = dernierCode != null ? dernierCode : -1;
            if((first == -1 && last != -1) || (first != -1 && last == -1)){
               if(first == -1){
                  throw new WrongValueException(premierCodeBoxEchan, Labels.getLabel("validation.syntax.empty"));
               }else{
                  throw new WrongValueException(dernierCodeBoxEchan, Labels.getLabel("validation.syntax.empty"));
               }
            }
         }else{
            first = premiereLettre != null && !StringUtils.isEmpty(premiereLettre) ? premiereLettre.charAt(0) : -1;
            last = derniereLettre != null && !StringUtils.isEmpty(derniereLettre) ? derniereLettre.charAt(0) : -1;
            if((first == -1 && last != -1) || (first != -1 && last == -1)){
               if(first == -1){
                  throw new WrongValueException(premiereLettreBoxEchan, Labels.getLabel("validation.syntax.empty"));
               }else{
                  throw new WrongValueException(derniereLettreBoxEchan, Labels.getLabel("validation.syntax.empty"));
               }
            }
         }

         checkRequiredListboxes();

         validateSterilite(sterileBox.isChecked());

         // on remplit l'échantillon en fonction des champs nulls
         setEmptyToNulls();
         getEchantillon().setTumoral(tumoraleBox.isChecked());
         getEchantillon().setSterile(sterileBox.isChecked());

         // gestion de la non conformitée après traitement
         if(conformeTraitementBoxOui.isChecked()){
            getEchantillon().setConformeTraitement(true);
         }else if(conformeTraitementBoxNon.isChecked()){
            getEchantillon().setConformeTraitement(false);
         }else{
            getEchantillon().setConformeTraitement(null);
         }
         if(getEchantillon().getConformeTraitement() == null){
            setSelectedNonConformiteTraitement(null);
         }else if(getEchantillon().getConformeTraitement()){
            setSelectedNonConformiteTraitement(null);
         }

         // gestion de la non conformitée à la cession
         if(conformeCessionBoxOui.isChecked()){
            getEchantillon().setConformeCession(true);
         }else if(conformeCessionBoxNon.isChecked()){
            getEchantillon().setConformeCession(false);
         }else{
            getEchantillon().setConformeCession(null);
         }
         if(getEchantillon().getConformeCession() == null){
            setSelectedNonConformiteCession(null);
         }else if(getEchantillon().getConformeCession()){
            setSelectedNonConformiteCession(null);
         }

         // Gestion du collaborateur
         final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
         this.collabBox.setValue(selectedNomAndPremon);
         final int ind = getNomsAndPrenoms().indexOf(selectedNomAndPremon);
         if(ind > -1){
            setSelectedCollaborateur(getCollaborateurs().get(ind));
         }else{
            setSelectedCollaborateur(null);
         }

         // la quantité et le volume restants sont égaux aux valeurs
         // intiales
         getEchantillon().setQuantite(getEchantillon().getQuantiteInit());

         getEchantillon().setObjetStatut(findStatutForTKStockableObject(getEchantillon()));
         final ObjetStatut statut = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0);

         prepareCrAnapath();

         prepareCodes();

         String lateralite = null;
         // lateralite
         if(getSelectedLateralite() != null && !getSelectedLateralite().equals("")){
            lateralite = getSelectedLateralite();
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

         // @since 2.2.2-diamic emplacement peut être ajouté
         // au moment de l'ajout
         final Map<TKStockableObject, Emplacement> injectableEchanEmpl = new HashMap<>();

         // Création de tous les nouveaux échantillons
         for(int i = first; i <= last; i++){

            // création du code échantillon en fct de celui du prlvt et
            // du numéro saisi
            final StringBuffer sb = new StringBuffer();

            // 2.0.10.6 VIROBIOTEC création codes sans prefixe
            if(getCodePrefixe() != null && !getCodePrefixe().trim().equals("")){
               sb.append(getCodePrefixe());
            }

            if(i > -1){ // pas ajout suffixe si first=last=-1
               sb.append(separatorBox.getValue() != null ? separatorBox.getValue() : "");
               if(numNombres.isChecked()){
                  sb.append(i);
               }else{
                  sb.append((char) i);
               }
            }

            usedCodesEchantillons.add(sb.toString());

            final Echantillon newEchantillon = new Echantillon();
            newEchantillon.setBanque(getBanque());
            newEchantillon.setPrelevement(getParentObject());
            newEchantillon.setCollaborateur(getSelectedCollaborateur());
            newEchantillon.setCode(sb.toString());
            newEchantillon.setDateStock(getEchantillon().getDateStock());
            newEchantillon.setEchantillonType(getSelectedType());
            newEchantillon.setQuantite(getEchantillon().getQuantite());
            newEchantillon.setQuantiteInit(getEchantillon().getQuantiteInit());
            newEchantillon.setQuantiteUnite(getSelectedQuantiteUnite());
            newEchantillon.setDelaiCgl(getEchantillon().getDelaiCgl());
            newEchantillon.setEchanQualite(getSelectedQualite());
            newEchantillon.setTumoral(getEchantillon().getTumoral());
            newEchantillon.setModePrepa(getSelectedPrepa());
            newEchantillon.setCrAnapath(getCrAnapath() != null ? getCrAnapath().cloneNoId() : null);
            newEchantillon.setAnapathStream(i == first ? getAnapathStream() : null);
            newEchantillon.setSterile(getEchantillon().getSterile());
            newEchantillon.setObjetStatut(statut);
            newEchantillon.setLateralite(lateralite);
            newEchantillon.setConformeTraitement(getEchantillon().getConformeTraitement());
            newEchantillon.setConformeCession(getEchantillon().getConformeCession());

            echantillons.add(newEchantillon);
            addedEchantillons.add(newEchantillon);
            final EchantillonDTO deco = new EchantillonDTO(newEchantillon);

            deco.setCodesOrgsToCreateOrEdit(
               CodeAssigneDecorator.undecorateListe(getCodesOrganeController().getObjToCreateOrEdit()));
            deco.setCodesLesToCreateOrEdit(
               CodeAssigneDecorator.undecorateListe(getCodesMorphoController().getObjToCreateOrEdit()));

            deco.setValeursToCreateOrUpdate(clonesValeurs);

            deco.setNonConformiteTraitements(findSelectedNonConformitesTraitement());
            deco.setNonConformiteCessions(findSelectedNonConformitesCession());

            echantillonsDecorated.add(deco);

            // file batches
            for(final FileBatch batch : batches){
               if(!batch.isCompleted()){
                  batch.getObjs().add(newEchantillon);
               }
            }

            // @since 2.2.2-diamic
            // si ajout d'un échantillon transmis, mise à jour de som emplacement
            // automatiquement
            // et affichage d'erreur non bloquante
            if(getInjectableEchantillons().contains(newEchantillon)){
               try{
                  final String adrl = SessionUtils.getDossierExterneInjection(sessionScope).getEchanAdrls().get(newEchantillon);
                  if(adrl != null){
                     final Emplacement empl = ManagerLocator.getEmplacementManager().findByEmplacementAdrlManager(
                        SessionUtils.getDossierExterneInjection(sessionScope).getEchanAdrls().get(newEchantillon), getBanque());
                     if(empl != null && empl.getVide()){
                        empl.setVide(false);
                        empl.setAdrl(adrl);
                        empl.setEntite(getObjectTabController().getEntiteTab());
                        injectableEchanEmpl.put(newEchantillon, empl);
                     }else{
                        Clients.showNotification(
                           ObjectTypesFormatters.getLabel("interfacage.emplacement.warning", new String[] {adrl}), "warning",
                           null, null, 3000, true);
                     }
                  }
               }catch(final Exception e){ // error occurred
                  Clients.showNotification(
                     ObjectTypesFormatters.getLabel("interfacage.emplacement.error", new String[] {e.getMessage()}), "error",
                     null, null, 3000, true);
               }
            }
         }

         for(final FileBatch batch : batches){
            batch.setCompleted(true);
         }

         final ListModel<EchantillonDTO> list = new ListModelList<>(echantillonsDecorated);
         echantillonsList.setModel(list);

         getEchanDecoRenderer().setUsedCodes(usedCodesEchantillons);

         // @since 2.3.0-gatsi
         scrollToBottom();

         clearForm(true);

         // @since 2.2.2-diamic
         // updates Emplacement depuis transmission diamic
         if(!injectableEchanEmpl.isEmpty()){
            updateDecoList(injectableEchanEmpl);
         }

         // active le lien vers le stockage
         if(!echantillons.isEmpty()){
            stockageEchantillons.setDisabled(false);
         }
         
         // @since 2.3.0-gatsbi
         prepareNextEchantillons();
         
      }catch(final ValidationException ve){
         Messagebox.show(handleExceptionMessage(ve), "Error", Messagebox.OK, Messagebox.ERROR);
      }

   }

   /**
    * sera surchargée par gatsbi
    * @since 2.3.0-gatsbi
    */
   protected void scrollToBottom(){
      Clients.scrollIntoView(stockageEchantillons);      
   }

   // sera surchargée par Gatsbi
   protected void prepareNextEchantillons(){
   }

   /**
    * Méthode supprimant un des échantillons que l'utilisateur souhaitait créer.
    *
    * @param event : clic sur le lien deleteEchan dans la liste d'échantillons.
    */
   public void onDeleteDeco$rows(final ForwardEvent event){

      EchantillonDTO deco = (EchantillonDTO) event.getOrigin().getData();

      Echantillon deletedEchantillon = deco.getEchantillon();

      if(addedEchantillons.contains(deletedEchantillon)){
         addedEchantillons.remove(deletedEchantillon);
         echantillons.remove(deletedEchantillon);
         echantillonsDecorated.remove(deco);
         usedCodesEchantillons.remove(deletedEchantillon.getCode());

         // on enlève l'association entre l'échantillon et son
         // emplacement dans la hashtable
         if(echansEmpl.containsKey(deletedEchantillon)){
            echansEmpl.remove(deletedEchantillon);
         }

         final ListModel<EchantillonDTO> list = new ListModelList<>(echantillonsDecorated);
         echantillonsList.setModel(list);

         // echantillon retiré des fileBatches
         for(final FileBatch batch : batches){
            batch.getObjs().remove(deletedEchantillon);
         }

         deletedEchantillon = null;
         deco = null;

      }

      // desactive le lien vers le stockage
      if(echantillons.isEmpty()){
         stockageEchantillons.setDisabled(true);
      }
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ codePrefixeEchan.
    * Cette valeur sera mise en majuscules.
    */
   @Override
   public void onBlur$codePrefixeEchan(){
      codePrefixeEchan.setValue(codePrefixeEchan.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée lors du clic sur le bouton stockageEchantillons.
    */
   public void onClick$stockageEchantillons(){
      if(!stockageEchantillons.isDisabled()){
         // on récupère tous les échantillons NON STOCKE
         final List<Echantillon> echansToStock = new ArrayList<>();
         for(int i = 0; i < echantillons.size(); i++){
            if(echantillons.get(i).getObjetStatut().getStatut().equals("NON STOCKE")){
               echansToStock.add(echantillons.get(i));
            }
         }

         // si des emplacements ont déja ete définis pour des échantillons
         // ils sont réservés
         final Iterator<Emplacement> emplsIt = echansEmpl.values().iterator();
         final List<Emplacement> reserves = new ArrayList<>();
         while(emplsIt.hasNext()){
            reserves.add(emplsIt.next());
         }

         final StockageController tabController = StockageController.backToMe(getMainWindow(), page);
         getMainWindow().blockAllPanelsExceptOne("stockageTab");
         tabController.clearAllPage();
         tabController.switchToStockerMode(echansToStock, null, Path.getPath(self), "onGetResultsFromStockage", reserves);
      }
   }

   /**
    * Méthode qui récupère les emplacements choisis pour les échantillons.
    *
    * @param e Event contenant les emplacements.
    */

   public void onGetResultsFromStockage(final Event e){
      // les emplacements sont contenus dans une hashtable mappant
      // un échantillon avec son emplacement
      final Hashtable<TKStockableObject, Emplacement> results = (Hashtable<TKStockableObject, Emplacement>) e.getData();

      updateDecoList(results);
   }

   /**
    * Met à jour la liste d'échantillon décorées
    *
    * @since 2.0.13.2
    * @param empls echantillon - emplacements
    */
   private void updateDecoList(final Map<TKStockableObject, Emplacement> empls){
      final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);

      final Set<TKStockableObject> echans = empls.keySet();
      final Iterator<TKStockableObject> it = echans.iterator();
      Emplacement emp;
      // pour chaque échantillon qui vient d'être stocké
      while(it.hasNext()){
         final TKStockableObject tmp = it.next();
         final EchantillonDTO deco = new EchantillonDTO((Echantillon) tmp);
         // on récupère son decorator correspondant dans la liste
         if(echantillonsDecorated.contains(deco)){
            final EchantillonDTO decoUp = echantillonsDecorated.get(echantillonsDecorated.indexOf(deco));

            emp = empls.get(tmp);

            // since 2.0.13.2 empl peux être null
            // pour nettoyer echan stockage en erreur
            if(emp != null){
               // MAJ du stockage de cet échantillon
               decoUp.setAdrlTmp(emp.getAdrl());
               decoUp.getEchantillon().setObjetStatut(stocke);
            }
         }

         // ajout du couple dans la hashtable
         if(!echansEmpl.containsKey(tmp)){
            echansEmpl.put(tmp, empls.get(tmp));
         }
      }

      // MAJ de la liste
      getBinder().loadAttribute(self.getFellow("echantillonsList"), "model");
   }

   public void onClick$changeNumerotation(){
      changeNumerotation.setVisible(false);
      choixNumerotation.setVisible(true);
   }

   public void onCheck$numNombres(){
      premierCodeBoxEchan.setVisible(true);
      dernierCodeBoxEchan.setVisible(true);
      premiereLettreBoxEchan.setVisible(false);
      derniereLettreBoxEchan.setVisible(false);
      Clients.clearWrongValue(premiereLettreBoxEchan);
      premiereLettreBoxEchan.setConstraint("");
      premiereLettreBoxEchan.setValue(null);
      premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
      Clients.clearWrongValue(derniereLettreBoxEchan);
      derniereLettreBoxEchan.setConstraint("");
      derniereLettreBoxEchan.setValue(null);
      derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
      changeNumerotation.setVisible(true);
      choixNumerotation.setVisible(false);
      changeNumerotation.setImage("/images/icones/smallNumber.png");
   }

   public void onCheck$numLettres(){
      premierCodeBoxEchan.setVisible(false);
      dernierCodeBoxEchan.setVisible(false);
      premiereLettreBoxEchan.setVisible(true);
      derniereLettreBoxEchan.setVisible(true);
      Clients.clearWrongValue(premierCodeBoxEchan);
      premierCodeBoxEchan.setConstraint("");
      premierCodeBoxEchan.setValue(null);
      premierCodeBoxEchan.setConstraint(cttPremierCode);
      Clients.clearWrongValue(dernierCodeBoxEchan);
      dernierCodeBoxEchan.setConstraint("");
      dernierCodeBoxEchan.setValue(null);
      dernierCodeBoxEchan.setConstraint(cttDernierCode);
      changeNumerotation.setVisible(true);
      choixNumerotation.setVisible(false);
      changeNumerotation.setImage("/images/icones/smallLetter.png");
   }

   /**
    * Cette méthode va vider le formulaire une fois que l'utilisateur aura cliqué
    * sur le bouton addEchantillons.
    */

   public void clearForm(final boolean setParentStatic){
      setEchantillon(new Echantillon());

      premierCode = null;
      dernierCode = null;
      premiereLettre = null;
      derniereLettre = null;

      setQuantite(null);
      setQuantiteInit(null);

      setHeureDelai(null);
      setMinDelai(null);

      setSelectedLateralite(null);

      if(getTypes().size() > 0){
         setSelectedType(getTypes().get(0));
      }else{
         setSelectedType(null);
      }
      typesBoxEchan.clearSelection();
      ((ListModelList<Object>) typesBoxEchan.getModel()).clearSelection();

      setSelectedQuantiteUnite(null);
      quaniteUnitesBoxEchan.clearSelection();
      ((ListModelList<Object>) quaniteUnitesBoxEchan.getModel()).clearSelection();

      setSelectedQualite(null);
      qualitesBoxEchan.clearSelection();
      ((ListModelList<Object>) qualitesBoxEchan.getModel()).clearSelection();

      setSelectedPrepa(null);
      prepasBox.clearSelection();
      ((ListModelList<Object>) prepasBox.getModel()).clearSelection();

      tumoraleBox.setChecked(false);

      crAnapathNomBox.setValue(null);
      setCrAnapath(new Fichier());
      setAnapathStream(null);
      showDeleteAndFileNameBox(false);

      if(setParentStatic){
         if(selectParentRowEchan.isVisible()){
            removeSelectParentMode();
            if(getParentObject() == null){
               prelRow.setVisible(false);
               unknownPrelRow.setVisible(true);
            }
         }
      }else{
         setCodePrefixe(null);
      }

      sterileBox
         .setChecked(getParentObject() != null && getParentObject().getSterile() != null && getParentObject().getSterile());

      dateStockCalBox.setHasChanged(false);

      getCodesOrganeController().setObjs(new ArrayList<SmallObjDecorator>());
      getCodesOrganeController().reloadGrid();
      getCodesMorphoController().setObjs(new ArrayList<SmallObjDecorator>());
      getCodesMorphoController().reloadGrid();

      if(getObjectTabController().canUpdateAnnotation()){
         getObjectTabController().getFicheAnnotation().clearValeursLists(true);
         getObjectTabController().getFicheAnnotation().updateAnnotationValues();
         getObjectTabController().getFicheAnnotation().switchToStaticOrEditMode(false, false);
      }

      setSelectedNonConformiteTraitement(null);
      setSelectedNonConformiteCession(null);
      conformeTraitementBoxOui.setChecked(false);
      conformeTraitementBoxNon.setChecked(false);
      conformeTraitementBox.setVisible(false);
      conformeCessionBoxOui.setChecked(false);
      conformeCessionBoxNon.setChecked(false);
      conformeCessionBox.setVisible(false);
      ((Selectable<NonConformite>) nonConformitesCessionBox.getModel()).clearSelection();
      ((Selectable<NonConformite>) nonConformitesTraitementBox.getModel()).clearSelection();

      // conserve les informations communes au dossier externe si existe
      initFormFromDossierExterne();

      getBinder().loadComponent(self);
   }

   @Override
   public void initEditableMode(){

      super.initEditableMode();

      if(getParentObject() != null){
         if(getParentObject().getCode() != null){
            //TG-244
            initCodePrefixe();
         }
         if(getParentObject().getOperateur() != null && getCollaborateurs().contains(getParentObject().getOperateur())){
            setSelectedCollaborateur(getParentObject().getOperateur());
            collabBox.setValue(getSelectedCollaborateur().getNomAndPrenom());
         }
      }else{ // no parent
         connaissances.setSelection(Arrays.asList("NON"));
      }

      // active le lien vers le stockage
      if(!echantillons.isEmpty()){
         stockageEchantillons.setDisabled(false);
      }

      // @since 2.3.0-gatsbi
      // injection des codes assignes injectés
      if(getObject().getEchantillonId() == null){
         final List<CodeAssigne> codesOrgane = new ArrayList<>();
         final List<CodeAssigne> codesMorpho = new ArrayList<>();
         for(final CodeAssigne codeA : getObject().getCodesAssignes()){
            if(codeA.getIsOrgane()){
               codesOrgane.add(codeA);
            }else if(codeA.getIsMorpho()){
               codesMorpho.add(codeA);
            }
         }
         getCodesOrganeController().addCodesFromInjection(codesOrgane);
         getCodesMorphoController().addCodesFromInjection(codesMorpho);
      }
   }

   /**
    * Recherche des doublons pour le permier et dernier codes saisis.
    *
    * @param premier Premier code des échanstillons à créer.
    * @param dernier Dernier code des échanstillons à créer.
    * @return True s'il y a des doublons, false sinon.
    */
   private List<String> findDoublonsInUsedCodes(final Integer premier, final Integer dernier){

      final Function<Integer, String> codeEchantillonMapper =
         i -> new StringBuilder(getCodePrefixe()).append(separatorBox.getValue()).append(i).toString();
      return IntStream.rangeClosed(premier, dernier).boxed().map(codeEchantillonMapper)
         .filter(code -> usedCodesEchantillons.contains(code)).collect(Collectors.toList());

   }

   /**
    * Recherche s'il existe en base des doublons de code échantillon dans
    * l'intervalle saisi pour la plateforme courante
    *
    * @param premier numéro de début de l'intervalle
    * @param dernier numéro de fn de l'intervalle
    * @return liste des codes échantillon en doublon
    */
   private List<Echantillon> findDoublonsInPlateforme(final int premier, final int dernier){

      final Function<String, String> mapToCodeEchantillon =
         s -> new StringBuilder(getCodePrefixe()).append(separatorBox.getValue()).append(s).toString();

      final List<String> listeCodes = IntStream.rangeClosed(premierCode, dernierCode).mapToObj(String::valueOf)
         .map(mapToCodeEchantillon).collect(Collectors.toList());
      return ManagerLocator.getManager(EchantillonManager.class).findByCodeInListWithPlateforme(listeCodes,
         SessionUtils.getCurrentPlateforme());

   }

   /**
    * Recherche s'il existe des doublons de code échantillon dans entre
    * l'intervalle saisi et les codes échantillons connus du prélèvement parent
    *
    * @param premier lettre du début de l'intervalle
    * @param dernier lettre de fin de l'intervalle
    * @return liste des codes échantillon en doublon
    */
   private List<String> findDoublonsInUsedCodes(final String premier, final String dernier){

      final int begin = premier.charAt(0);
      final int end = dernier.charAt(0);

      final String prefix = getCodePrefixe();

      List<String> doublons = new ArrayList<>();

      if(!StringUtils.isEmpty(prefix)){

         final String delimiter = Optional.ofNullable(separatorBox.getValue()).orElse("");

         final Function<String, String> mapCharToCode =
            s -> new StringBuilder().append(prefix).append(delimiter).append(s).toString();
         final Predicate<String> isCodeUsed = s -> usedCodesEchantillons.contains(s);

         doublons = IntStream.rangeClosed(begin, end).mapToObj(i -> String.valueOf((char) i)).map(mapCharToCode)
            .filter(isCodeUsed).collect(Collectors.toList());

      }

      return doublons;

   }

   /**
    * Recherche s'il existe en base des doublons de code échantillon dans
    * l'intervalle saisi pour la plateforme courante
    *
    * @param premier lettre de début de l'intervalle
    * @param dernier lettre de fn de l'intervalle
    * @return liste des codes échantillon en doublon
    */
   private List<Echantillon> findDoublonsInPlateforme(final String premier, final String dernier){

      final int begin = premier.charAt(0);
      final int end = dernier.charAt(0);

      final Function<Character, String> mapToCodeEchantillon =
         s -> new StringBuilder(getCodePrefixe()).append(separatorBox.getValue()).append(s).toString();

      final List<String> listeCodes =
         IntStream.rangeClosed(begin, end).mapToObj(idx -> (char) idx).map(mapToCodeEchantillon).collect(Collectors.toList());
      return ManagerLocator.getManager(EchantillonManager.class).findByCodeInListWithPlateforme(listeCodes,
         SessionUtils.getCurrentPlateforme());

   }

   /**
    * Méthode initialisant les objets associés.
    */
   public void initAssociations(){
      if(getParentObject() != null && getParentObject().getBanque() != null){
         setBanque(getParentObject().getBanque());
      }else{
         setBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));
      }

      if(addedEchantillons.size() == 0){
         // Init des échantillons
         if(getParentObject() != null && getParentObject().getPrelevementId() != null){
            echantillons = ManagerLocator.getEchantillonManager().findByPrelevementManager(getParentObject());

            usedCodesEchantillons = ManagerLocator.getEchantillonManager().findAllCodesForBanqueManager(getBanque());

         }else{
            usedCodesEchantillons.clear();
            echantillons.clear();
         }

         echantillonsDecorated = ManagerLocator.getEchantillonDTOManager().decorateListeManager(echantillons);
         final ListModel<EchantillonDTO> list = new ListModelList<>(echantillonsDecorated);
         echantillonsList.setModel(list);

         if(echantillons.size() > 0){
            stockageEchantillons.setDisabled(false);
         }else{
            stockageEchantillons.setDisabled(true);
         }
      }
   }
   
   //TG-244 : sera surchargé par Gatsbi pour prendre en compte le paramétrage sur le code échantillon
   protected void initCodePrefixe() {
      setCodePrefixe(getParentObject().getCode());
   }
   
   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public Prelevement getPrelevement(){
      return getParentObject();
   }

   public ListModel<String> getConnaissances(){
      return connaissances;
   }

   public void setConnaissances(final SimpleListModel<String> c){
      this.connaissances = c;
   }

   public boolean isSelectParent(){
      return selectParent;
   }

   public void setSelectParent(final boolean parent){
      this.selectParent = parent;
   }

   public Maladie getMaladie(){
      return maladie;
   }

   public void setMaladie(final Maladie mal){
      this.maladie = mal;
   }

   public List<Echantillon> getEchantillons(){
      return echantillons;
   }

   public void setEchantillons(final List<Echantillon> echans){
      this.echantillons = echans;
   }

   public List<Object> getAddedEchantillons(){
      return addedEchantillons;
   }

   public void setAddedEchantillons(final List<Object> added){
      this.addedEchantillons = added;
   }

   public List<EchantillonDTO> getEchantillonsDecorated(){
      return echantillonsDecorated;
   }

   public void setEchantillonsDecorated(final List<EchantillonDTO> decorated){
      this.echantillonsDecorated = decorated;
   }

   public AbstractEchantillonDecoratorRowRenderer getEchanDecoRenderer(){
      return echanDecoRenderer;
   }

   public List<String> getUsedCodesEchantillons(){
      return usedCodesEchantillons;
   }

   public void setUsedCodesEchantillons(final List<String> usedCodes){
      this.usedCodesEchantillons = usedCodes;
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

   public Map<? extends TKStockableObject, Emplacement> getEchansEmpl(){
      return echansEmpl;
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/

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
   protected class ConstPremierCode implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){
         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des chiffres en
         // numérotation
         if(numNombres.isChecked()){
            // on récupère la valeur dans premierCodeBoxEchan
            premierCode = (Integer) value;

            // si une valeur est saisie dans le champ premierCodeBoxEchan
            if(premierCode != null){
               // si le premier code est négatif
               if(premierCode <= 0){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.code.sup.zero"));
               }
               // Si le dernierCode est null (1ère édition de la page)
               // on va récupérer la valeur du champ dernierCodeBoxEchan
               if(dernierCode == null){
                  // on enlève la contrainte de dernierCodeBoxEchan pour
                  // pouvoir récupérer sa valeur
                  dernierCodeBoxEchan.setConstraint("");
                  dernierCodeBoxEchan.clearErrorMessage();
                  dernierCode = dernierCodeBoxEchan.getValue();
                  // on remet la contrainte
                  dernierCodeBoxEchan.setConstraint(cttDernierCode);
               }

               // si une valeur est saisie dans le champ dernierCodeBoxEchan
               if(dernierCode != null){
                  // si le dernier code est < au premier
                  if(dernierCode < premierCode){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillon.error.premier.code.superieur"));
                  }

                  // sinon on enlève toutes les erreurs affichées
                  Integer tmp = dernierCode;
                  Clients.clearWrongValue(dernierCodeBoxEchan);
                  dernierCodeBoxEchan.setConstraint("");
                  dernierCodeBoxEchan.setValue(tmp);
                  dernierCodeBoxEchan.setConstraint(cttDernierCode);

                  // recherche de doublons dans les échantillons du parent
                  final List<String> doublonsParent = findDoublonsInUsedCodes(premierCode, dernierCode);
                  if(!doublonsParent.isEmpty()){
                     final String doublonsCodes = doublonsParent.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("echantillon.doublon.error.num", new String[] {doublonsCodes}));
                  }

                  // recherche de doublon dans les codes échantillons de la plateforme
                  final List<Echantillon> doublonsPf = findDoublonsInPlateforme(premierCode, dernierCode);
                  if(!doublonsPf.isEmpty()){
                     final String doublonsCodes = doublonsPf.stream().map(Echantillon::getCode).collect(Collectors.joining(", "));
                     final String doublonsBanques = doublonsPf.stream().map(Echantillon::getBanque).distinct().map(Banque::getNom)
                        .collect(Collectors.joining(", "));
                     throw new WrongValueException(dernierCodeBoxEchan,
                        Labels.getLabel("error.validation.doublon.code", new String[] {doublonsCodes, doublonsBanques}));
                  }

                  // sinon on enlève toutes les erreurs affichées
                  tmp = dernierCode;
                  Clients.clearWrongValue(dernierCodeBoxEchan);
                  dernierCodeBoxEchan.setConstraint("");
                  dernierCodeBoxEchan.setValue(tmp);
                  dernierCodeBoxEchan.setConstraint(cttDernierCode);
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final Integer tmp = dernierCode;
                  Clients.clearWrongValue(dernierCodeBoxEchan);
                  dernierCodeBoxEchan.setConstraint("");
                  dernierCodeBoxEchan.setValue(tmp);
                  dernierCodeBoxEchan.setConstraint(cttDernierCode);
               }
            }
            // else if (!StringUtils.isEmpty(dernierCodeBoxEchan.getValue())) {
            // throw new WrongValueException(comp,
            // Labels.getLabel("validation.syntax.empty"));
            // }
         }else{
            Clients.clearWrongValue(premierCodeBoxEchan);
            premierCodeBoxEchan.setConstraint("");
            premierCodeBoxEchan.setValue(null);
            premierCodeBoxEchan.setConstraint(cttPremierCode);
            Clients.clearWrongValue(dernierCodeBoxEchan);
            dernierCodeBoxEchan.setConstraint("");
            dernierCodeBoxEchan.setValue(null);
            dernierCodeBoxEchan.setConstraint(cttDernierCode);
         }
      }
   }

   protected Constraint cttPremierCode = new ConstPremierCode();

   /**
    *
    * @author Pierre Ventadour.
    *
    */
   protected class ConstDernierCode implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){
         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des chiffres en
         // numérotation
         if(numNombres.isChecked()){
            // on récupère la valeur dans dernierCodeBoxEchan
            dernierCode = (Integer) value;

            // si une valeur est saisie dans le champ dernierCodeBoxEchan
            if(dernierCode != null){
               // si le dernier code est négatif
               if(dernierCode <= 0){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.code.sup.zero"));
               }
               // Si le premierCode est null (1ère édition de la page)
               // on va récupérer la valeur du champ premierCodeBoxEchan
               if(premierCode == null){
                  // on enlève la contrainte de premierCodeBoxEchan pour
                  // pouvoir récupérer sa valeur
                  premierCodeBoxEchan.setConstraint("");
                  premierCodeBoxEchan.clearErrorMessage();
                  premierCode = premierCodeBoxEchan.getValue();
                  // on remet la contrainte
                  premierCodeBoxEchan.setConstraint(cttPremierCode);
               }

               // si une valeur est saisie dans le champ premierCodeBoxEchan
               if(premierCode != null){
                  // si le dernier code est < au premier
                  if(dernierCode < premierCode){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillon.error.premier.code.superieur"));
                  }

                  // sinon on enlève toutes les erreurs affichées
                  Integer tmp = premierCode;
                  Clients.clearWrongValue(premierCodeBoxEchan);
                  premierCodeBoxEchan.setConstraint("");
                  premierCodeBoxEchan.setValue(tmp);
                  premierCodeBoxEchan.setConstraint(cttPremierCode);

                  // recherche de doublons avec les échantillons du parent
                  final List<String> doublonsCode = findDoublonsInUsedCodes(premierCode, dernierCode);
                  if(!doublonsCode.isEmpty()){
                     final String codesDoublons = doublonsCode.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("echantillon.doublon.error.num", new String[] {codesDoublons}));
                  }

                  // si des doublons existent pour la plateforme
                  final List<Echantillon> doublonsEchantillons = findDoublonsInPlateforme(premierCode, dernierCode);
                  if(!doublonsEchantillons.isEmpty()){

                     final String codesDoublons =
                        doublonsEchantillons.stream().map(Echantillon::getCode).collect(Collectors.joining(", "));
                     final String banquesDoublons = doublonsEchantillons.stream().map(Echantillon::getBanque).distinct()
                        .map(Banque::getNom).collect(Collectors.joining(", "));

                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, banquesDoublons}));

                  }
                  // sinon on enlève toutes les erreurs affichées
                  tmp = premierCode;
                  Clients.clearWrongValue(premierCodeBoxEchan);
                  premierCodeBoxEchan.setConstraint("");
                  premierCodeBoxEchan.setValue(tmp);
                  premierCodeBoxEchan.setConstraint(cttPremierCode);
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final Integer tmp = premierCode;
                  Clients.clearWrongValue(premierCodeBoxEchan);
                  premierCodeBoxEchan.setConstraint("");
                  premierCodeBoxEchan.setValue(tmp);
                  premierCodeBoxEchan.setConstraint(cttPremierCode);
               }
            }
            // else if (!StringUtils.isEmpty(premierCodeBoxEchan.getValue())) {
            // throw new WrongValueException(comp,
            // Labels.getLabel("validation.syntax.empty"));
            // }
         }else{
            Clients.clearWrongValue(premierCodeBoxEchan);
            premierCodeBoxEchan.setConstraint("");
            premierCodeBoxEchan.setValue(null);
            premierCodeBoxEchan.setConstraint(cttPremierCode);
            Clients.clearWrongValue(dernierCodeBoxEchan);
            dernierCodeBoxEchan.setConstraint("");
            dernierCodeBoxEchan.setValue(null);
            dernierCodeBoxEchan.setConstraint(cttDernierCode);
         }
      }
   }

   protected Constraint cttDernierCode = new ConstDernierCode();

   /**
    *
    * @author Pierre Ventadour.
    *
    */
   protected class ConstPremiereLettre implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){

         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des lettres en
         // numérotation
         if(numLettres.isChecked()){

            final String sValue = (String) value;

            // si une valeur est saisie
            if(sValue != null && !"".equals(sValue)){

               // on récupère la valeur dans premiereLettreBoxEchan
               premiereLettre = sValue.toUpperCase();

               // si le premier code est invalide
               if(!premiereLettre.matches("[A-Z]")){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillons.lettre.invalide"));
               }
               // Si la derniereLettre est null (1ère édition de la page)
               // on va récupérer la valeur du champ derniereLettreBoxEchan
               if(derniereLettre == null || derniereLettre.equals("")){
                  // on enlève la contrainte de
                  // derniereLettreBoxEchan pour
                  // pouvoir récupérer sa valeur
                  derniereLettreBoxEchan.setConstraint("");
                  derniereLettreBoxEchan.clearErrorMessage();
                  derniereLettre = derniereLettreBoxEchan.getValue();
                  // on remet la contrainte
                  derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
               }

               // si une valeur est saisie dans le champ
               // derniereLettreBoxEchan
               if(derniereLettre != null && !derniereLettre.equals("")){
                  // si la derniere est avant la premiere
                  if(derniereLettre.compareToIgnoreCase(premiereLettre) < 0){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillon.error.premier.lettre.superieur"));
                  }

                  // sinon on enlève toutes les erreurs affichées
                  String tmp = derniereLettre;
                  Clients.clearWrongValue(derniereLettreBoxEchan);
                  derniereLettreBoxEchan.setConstraint("");
                  derniereLettreBoxEchan.setValue(tmp);
                  derniereLettreBoxEchan.setConstraint(cttDerniereLettre);

                  // recherche de doublons avec les échantillons du parent
                  final List<String> doublonsCodes = findDoublonsInUsedCodes(premiereLettre, derniereLettre);
                  if(!doublonsCodes.isEmpty()){
                     final String codesDoublons = doublonsCodes.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("echantillon.doublon.error.letter", new String[] {codesDoublons}));
                  }

                  // recherche de doublons sur la plateforme
                  final List<Echantillon> doublonsEchantillons = findDoublonsInPlateforme(premiereLettre, derniereLettre);
                  if(!doublonsEchantillons.isEmpty()){
                     final String codesDoublons =
                        doublonsEchantillons.stream().map(Echantillon::getCode).collect(Collectors.joining(", "));
                     final String pfDoublons = doublonsEchantillons.stream().map(Echantillon::getBanque).distinct()
                        .map(Banque::getNom).collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, pfDoublons}));
                  }

                  // sinon on enlève toutes les erreurs affichées
                  tmp = derniereLettre;
                  Clients.clearWrongValue(derniereLettreBoxEchan);
                  derniereLettreBoxEchan.setConstraint("");
                  derniereLettreBoxEchan.setValue(tmp);
                  derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final String tmp = derniereLettre;
                  Clients.clearWrongValue(derniereLettreBoxEchan);
                  derniereLettreBoxEchan.setConstraint("");
                  derniereLettreBoxEchan.setValue(tmp);
                  derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
               }
            }
            // else if (!StringUtils.isEmpty(premiereLettreBoxEchan.getValue())) {
            // throw new WrongValueException(comp,
            // Labels.getLabel("validation.syntax.empty"));
            // }
         }else{
            Clients.clearWrongValue(premiereLettreBoxEchan);
            premiereLettreBoxEchan.setConstraint("");
            premiereLettreBoxEchan.setValue(null);
            premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
            Clients.clearWrongValue(derniereLettreBoxEchan);
            derniereLettreBoxEchan.setConstraint("");
            derniereLettreBoxEchan.setValue(null);
            derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
         }
      }
   }

   protected Constraint cttPremiereLettre = new ConstPremiereLettre();

   /**
    *
    * @author Pierre Ventadour.
    *
    */
   protected class ConstDerniereLettre implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){

         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des lettres en
         // numérotation
         if(numLettres.isChecked()){

            final String sValue = (String) value;

            // si une valeur est saisie
            if(value != null && !"".equals(sValue)){

               // on récupère la valeur dans derniereLettreBoxEchan
               derniereLettre = sValue.toUpperCase();

               // si la derniere lettre n'est pas valide
               if(!derniereLettre.matches("[A-Z]")){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillons.lettre.invalide"));
               }
               // Si la premiere lettre est null (1ère édition de la page)
               // on va récupérer la valeur
               // du champ premiereLettreBoxEchan
               if(premiereLettre == null || premiereLettre.equals("")){
                  // on enlève la contrainte pour
                  // pouvoir récupérer sa valeur
                  premiereLettreBoxEchan.setConstraint("");
                  premiereLettre = premiereLettreBoxEchan.getValue();
                  premiereLettreBoxEchan.clearErrorMessage();
                  // on remet la contrainte
                  premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
               }

               // si une valeur est saisie dans le champ
               // premiereLettreBoxEchan
               if(premiereLettre != null && !premiereLettre.equals("")){
                  // si le dernier code est < au premier
                  // si la derniere est avant la premiere
                  if(!premiereLettre.matches("[A-Z]") || derniereLettre.compareToIgnoreCase(premiereLettre) < 0){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillon.error.premier.lettre.superieur"));
                  }
                  // sinon on enlève toutes les erreurs affichées
                  String tmp = premiereLettre;
                  Clients.clearWrongValue(premiereLettreBoxEchan);
                  premiereLettreBoxEchan.setConstraint("");
                  premiereLettreBoxEchan.setValue(tmp);
                  premiereLettreBoxEchan.setConstraint(cttPremiereLettre);

                  // recherche de doublons dans les échantillons du parent
                  final List<String> doublonsParent = findDoublonsInUsedCodes(premiereLettre, derniereLettre);
                  if(!doublonsParent.isEmpty()){
                     final String codesDoublons = doublonsParent.stream().collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("echantillon.doublon.error.letter", new String[] {codesDoublons}));
                  }

                  // recherche de doublons dans les échantillons de la plateforme
                  final List<Echantillon> doublonsPf = findDoublonsInPlateforme(premiereLettre, derniereLettre);
                  if(!doublonsPf.isEmpty()){
                     final String codesDoublons = doublonsPf.stream().map(Echantillon::getCode).collect(Collectors.joining(", "));
                     final String banquesDoublons = doublonsPf.stream().map(Echantillon::getBanque).distinct().map(Banque::getNom)
                        .collect(Collectors.joining(", "));
                     throw new WrongValueException(comp,
                        Labels.getLabel("error.validation.doublon.code", new String[] {codesDoublons, banquesDoublons}));
                  }

                  // sinon on enlève toutes les erreurs affichées
                  tmp = premiereLettre;
                  Clients.clearWrongValue(premiereLettreBoxEchan);
                  premiereLettreBoxEchan.setConstraint("");
                  premiereLettreBoxEchan.setValue(tmp);
                  premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final String tmp = premiereLettre;
                  Clients.clearWrongValue(premiereLettreBoxEchan);
                  premiereLettreBoxEchan.setConstraint("");
                  premiereLettreBoxEchan.setValue(tmp);
                  premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
               }
            }
            // else if (!StringUtils.isEmpty(premiereLettreBoxEchan.getValue())) {
            // throw new WrongValueException(comp,
            // Labels.getLabel("validation.syntax.empty"));
            // }
         }else{
            Clients.clearWrongValue(premiereLettreBoxEchan);
            premiereLettreBoxEchan.setConstraint("");
            premiereLettreBoxEchan.setValue(null);
            premiereLettreBoxEchan.setConstraint(cttPremiereLettre);
            Clients.clearWrongValue(derniereLettreBoxEchan);
            derniereLettreBoxEchan.setConstraint("");
            derniereLettreBoxEchan.setValue(null);
            derniereLettreBoxEchan.setConstraint(cttDerniereLettre);
         }
      }
   }

   protected Constraint cttDerniereLettre = new ConstDerniereLettre();

   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(codesParentBoxEchan);
      Clients.clearWrongValue(premierCodeBoxEchan);
      Clients.clearWrongValue(dernierCodeBoxEchan);
      Clients.clearWrongValue(quantiteInitBoxEchan);
      Clients.clearWrongValue(dateStockCalBox);
      Clients.clearWrongValue(sterileBox);
      Clients.clearWrongValue(premiereLettreBoxEchan);
      Clients.clearWrongValue(derniereLettreBoxEchan);
   }

   /**
    * Applique la validation sur la sterilite.
    */
   @Override
   public void onCheck$sterileBox(){
      Clients.clearWrongValue(sterileBox);
   }

   /**
    * Valide l'application de la sterilite au niveau echantillon.
    *
    * @param value
    */
   private void validateSterilite(final boolean value){
      Errors errs = null;

      if(value){
         // test objet
         final Echantillon testEchan = new Echantillon();
         testEchan.setSterile(true);
         Prelevement prel = null;
         if(isPrelevementProcedure){
            prel = getParentObject();
         }else{
            prel = getPrelevementFromSelect();
         }
         if(prel != null){
            if(getLaboInters() != null && !getLaboInters().isEmpty()){
               prel.setLaboInters(new HashSet<>(getLaboInters()));
            }else if(prel.getPrelevementId() != null){
               prel.setLaboInters(new HashSet<>(ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(prel)));
            }
            testEchan.setPrelevement(prel);

            errs = ManagerLocator.getEchantillonValidator().checkSteriliteAntecedence(testEchan);

            prel.setLaboInters(null);
         }
      }

      if(errs != null && errs.hasErrors()){
         final List<Errors> errors = new ArrayList<>();
         errors.add(errs);
         throw new ValidationException(errors);
      }
   }

   /*************************************************************************/
   /************************** PROCEDURE PRELEVEMENT *************************/
   /*************************************************************************/
   /**
    * Change mode de la fiche en mode create depuis la procedure de creation d'un
    * prelevement.
    */
   public void switchToEditMode(final Prelevement prel){

      // passage prelevement
      setParentObject(prel);

      setIsCreate(false);
      switchButtons();

      scrollToTop();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();
      initQuantiteAndVolume();
      initAssociations();

      if(!getDroitOnAction("Collaborateur", "Consultation")){
         operateurAideSaisieEchan.setVisible(false);
      }

      setFocusOnElement();

      getBinder().loadComponent(self);
   }

   protected void scrollToTop(){
      Clients.scrollIntoView(formGrid.getColumns());
   }

   /*************************************************************************/
   /************************** PARENT ************************************/
   /*************************************************************************/
   // Objets pour la sélection du parent
   private List<String> codesParent = new ArrayList<>();

   private CustomSimpleListModel dictCodesModel;

   private String selectedCodeParent;

   private boolean selectParent = false;

   // Mode de sélection du parent
   // gatsbi Row -> Div
   protected HtmlBasedComponent selectParentRowEchan;

   private HtmlBasedComponent prelRow;

   private HtmlBasedComponent unknownPrelRow;

   private Combobox codesParentBoxEchan;

   private Label codeParentLabelEchan;

   private Label requiredCodeParentEchan;

   private Listbox connaissancesBoxEchan;

   // Variable contenant le code prlvt saisi par l'utilisateur.
   private String codeTmp;

   public List<String> getCodesParent(){
      return codesParent;
   }

   public void setCodesParent(final List<String> codes){
      this.codesParent = codes;
   }

   public CustomSimpleListModel getDictCodesModel(){
      return dictCodesModel;
   }

   public void setDictCodesModel(final CustomSimpleListModel dictCodes){
      this.dictCodesModel = dictCodes;
   }

   public String getSelectedCodeParent(){
      return selectedCodeParent;
   }

   public void setSelectedCodeParent(final String selectedCode){
      this.selectedCodeParent = selectedCode;
   }

   /**
    * Cache le mode de sélection du prlvt parent.
    */
   public void removeSelectParentMode(){
      selectParent = false;

      // on cache les éléments.
      selectParentRowEchan.setVisible(false);
      prelRow.setVisible(true);

   }

   /**
    * Lors de la création d'un échantillon, si son prlvt parent n'est pas fourni,
    * on propose une sélection de celui-ci. Cette méthode permet l'affichage de
    * cette sélection.
    */
   public void addSelectParentMode(){
      selectParent = true;
      setParentObject(null);

      // affichage des éléments
      selectParentRowEchan.setVisible(true);
      connaissancesBoxEchan.setSelectedIndex(0);
      prelRow.setVisible(false);

      // on récupère la liste des codes des prlvts
      codesParent = ManagerLocator.getPrelevementManager()
         .findAllCodesForBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
      // ces codes sont placés dans un dictionnaire pour permettre
      // le filtre lorsque l'utilisateur saisit le code
      dictCodesModel = new CustomSimpleListModel(codesParent.toArray());
      codesParentBoxEchan.setModel(dictCodesModel);
   }

   /**
    * Mise à jour de la valeur sélectionné dans la combobox lorsque l'utilisateur
    * clique à l'extérieur de celle-ci.
    *
    * @param event Event : clique à l'extérieur de la combobox codesParentBoxEchan.
    * @throws Exception
    */
   public void onBlur$codesParentBoxEchan(final Event event) throws Exception{
      // on enlève la contrainte de la combobox
      codesParentBoxEchan.setConstraint("");
      codesParentBoxEchan.clearErrorMessage();
      // si aucun élément n'est sélectionné, la valeur de la box correspond
      // à ce qu'a tapé l'utilisateur.
      if(codesParentBoxEchan.getSelectedItem() == null){
         codesParentBoxEchan.setValue(codeTmp);
      }else{
         // on valide la sélection
         cttCodeParentEchan.validate(codesParentBoxEchan, null);

         setCodePrefixe(codesParentBoxEchan.getSelectedItem().getLabel());

         // on récupère le prlvt en fonction de son code
         setParentObject(ManagerLocator.getPrelevementManager().findByCodeOrNumLaboLikeWithBanqueManager(getCodePrefixe(),
            SessionUtils.getSelectedBanques(sessionScope).get(0), true).get(0));

         calculDelaiCgl();
         clearConstraints();

         initAssociations();

         sterileBox.setChecked(initSterileOrNot());
         codePrefixeEchan.setValue(getCodePrefixe());
      }

      setPremierCode(null);
      setPremiereLettre(null);
      setDernierCode(null);
      setDerniereLettre(null);

      // on ré-active la contrainte
      cttCodeParentEchan.validate(codesParentBoxEchan, null);
   }

   public void onSelect$connaissancesBoxEchan(){
      final int ind = connaissancesBoxEchan.getSelectedIndex();
      clearForm(false);
      if(ind > 0){
         setParentObject(null);
         codeParentLabelEchan.setVisible(false);
         requiredCodeParentEchan.setVisible(false);
         codesParentBoxEchan.setVisible(false);

         codesParentBoxEchan.setConstraint("");
         codesParentBoxEchan.clearErrorMessage();
         codesParentBoxEchan.setValue("");
         connaissancesBoxEchan.setSelectedIndex(1);
      }else{
         codeParentLabelEchan.setVisible(true);
         requiredCodeParentEchan.setVisible(true);
         codesParentBoxEchan.setVisible(true);

         codesParentBoxEchan.setConstraint("");
         codesParentBoxEchan.clearErrorMessage();
         codesParentBoxEchan.setValue("");
         connaissancesBoxEchan.setSelectedIndex(0);
      }

      calculDelaiCgl();
      clearConstraints();

      initAssociations();

      dateStockCalBox.setHasChanged(false);
   }

   /**
    * Clic sur le bouton générant un code.
    */
   @Override
   public void onClick$numerotation(){
      setCodePrefixe(generateCodeAndUpdateNumerotation());
   }

   /*********************************************************/
   /********************** VALIDATION ***********************/
   /*********************************************************/
   /**
    * Contrainte vérifiant que la combobox pour le code du parent n'est pas nulle
    * et que son contenu se trouve bien dans la liste.
    *
    * @author Pierre Ventadour.
    *
    */
   protected class ConstCodeParentEchan implements Constraint
   {
      /**
       * Méthode de validation du champ dateTransfoBox.
       */
      @Override
      public void validate(final Component comp, final Object value){
         codesParentBoxEchan.setConstraint("");
         codesParentBoxEchan.clearErrorMessage();
         String code = "";
         if(codesParentBoxEchan.getSelectedItem() != null){
            code = codesParentBoxEchan.getSelectedItem().getLabel();
         }else{
            code = codesParentBoxEchan.getValue();
         }

         if(code.length() == 0){
            throw new WrongValueException(comp,
               "Champ vide non autorisé. Vous devez " + "spécifier un code présent dans la liste.");
         }else if(!codesParent.contains(code)){
            throw new WrongValueException(comp,
               "Valeur non autorisée. Vous devez spécifier " + "une valeur présente dans la liste.");
         }
      }
   }

   protected final Constraint cttCodeParentEchan = new ConstCodeParentEchan();

   public Constraint getCttCodeParentEchan(){
      return cttCodeParentEchan;
   }

   /**
    * Applique la validation sur la date.
    */
   @Override
   public void onBlur$dateStockCalBox(){
      final Datebox box = (Datebox) dateStockCalBox.getFirstChild().getFirstChild();
      boolean badDateFormat = false;
      if(box.getErrorMessage() != null && box.getErrorMessage().contains(box.getFormat())){
         badDateFormat = true;
      }else{
         final Calendar calValue = Calendar.getInstance();
         if(box.getValue() != null){
            calValue.setTime(box.getValue());
            if(calValue.get(Calendar.YEAR) > 9999){
               badDateFormat = true;
            }
         }
      }
      if(!badDateFormat){
         // recupere le prelevement parent au besoin
         if(getParentObject() == null && connaissancesBoxEchan.isVisible()){
            setParentObject(getPrelevementFromSelect());
         }
         if(getParentObject() != null){
            getEchantillon().setPrelevement(getParentObject());
            if(!dateStockCalBox.isHasChanged() && dateStockCalBox.getValue() == null){
               dateStockCalBox.setValue(ObjectTypesFormatters.getDateWithoutHoursAndMins(getParentObject().getDatePrelevement()));
               getEchantillon().setDateStock(dateStockCalBox.getValue());
            }else{
               dateStockCalBox.clearErrorMessage(dateStockCalBox.getValue());
               validateCoherenceDate(dateStockCalBox, dateStockCalBox.getValue());
            }
         }
         getEchantillon().setDateStock(dateStockCalBox.getValue());
         calculDelaiCgl();
         dateStockCalBox.setHasChanged(true);
      }else{
         throw new WrongValueException(dateStockCalBox, Labels.getLabel("validation.invalid.date"));
      }
   }

   /**
    * Indique si le champ stérile doit être true ou false par défaut dans le cas de
    * procédure d'enregistrement d'un prélèvement.
    *
    * @return true/false.
    */
   private boolean initSterileOrNot(){
      if(getParentObject() == null || getParentObject().getSterile() == null || !getParentObject().getSterile()){
         return false;
      }

      Collection<LaboInter> labs = null;
      // aucun nouveau labo créé
      if(getLaboInters() == null || getLaboInters().isEmpty()){
         if(getParentObject().getPrelevementId() != null){
            labs = ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(getParentObject());
         }else{
            labs = new ArrayList<>();
         }
      }else{
         labs = getLaboInters();
      }

      final Iterator<LaboInter> it = labs.iterator();
      while(it.hasNext()){
         if(!it.next().getSterile()){
            return false;
         }
      }

      return true;
   }

   /*************************************************************************/
   /************************** INTERFACAGES *********************************/
   /*************************************************************************/
   public void initFormFromDossierExterne(){
      final ResultatInjection res = SessionUtils.getDossierExterneInjection(sessionScope);

      if(res != null){

         Echantillon newObj = null;
         if(res.getEchantillon() != null){
            newObj = res.getEchantillon().clone();
            newObj.setBanque((Banque) sessionScope.get("Banque"));
            newObj.setPrelevement(getParentObject());
            setEchantillon(newObj);
            setSelectedType(newObj.getEchantillonType());
            setSelectedPrepa(newObj.getModePrepa());
            setSelectedLateralite(newObj.getLateralite());
            setSelectedQualite(newObj.getEchanQualite());
            setSelectedCollaborateur(newObj.getCollaborateur());
            setSelectedQuantiteUnite(newObj.getQuantiteUnite());
         }

         if(getObjectTabController() != null && getObjectTabController().getFicheAnnotation() != null
            && res.getAnnosEchantillon() != null && !res.getAnnosEchantillon().isEmpty()){
            getObjectTabController().getFicheAnnotation().setAnnotationValues(res.getAnnosEchantillon());
         }

         // codes
         getCodesOrganeController().addCodesFromInjection(res.getCodesOrgane());
         getCodesMorphoController().addCodesFromInjection(res.getCodesMorpho());

         // cr anapath
         if(res.getCrAnapath() != null){
            setAnapathStream(res.getStream());
            crAnapathNomBox.setValue(res.getCrAnapath().getNom());
            showDeleteAndFileNameBox(true);
            getCrAnapathConstraint().validate(crAnapathNomBox, res.getCrAnapath().getNom());
         }
      }
   }

   @Override
   public void setLaboInters(final List<LaboInter> labos){
      super.setLaboInters(labos);
      if(isPrelevementProcedure){
         sterileBox.setChecked(initSterileOrNot());
      }
   }

   public String getPremiereLettre(){
      return premiereLettre;
   }

   public void setPremiereLettre(final String l){
      this.premiereLettre = l;
   }

   public String getDerniereLettre(){
      return derniereLettre;
   }

   public void setDerniereLettre(final String l){
      this.derniereLettre = l;
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
    * Applique les codes obtenus depuis le scan dans l'ordre aux codes échantillons
    * en attente de création dans la liste de travail.
    *
    * @param sT scanTerminale
    */
   public void applyTKObjectsCodesFromScan(final ScanTerminale sT){
      if(!getAddedEchantillons().isEmpty() && !sT.getScanTubes().isEmpty()){
         int i = 0;
         Textbox tb;
         for(final EchantillonDTO deco : getEchantillonsDecorated()){
            if(deco.isNew() && deco.getAdrlTmp() == null){
               if(i < sT.getScanTubes().size()){
                  tb = (Textbox) echantillonsList.getRows().getChildren().get(getEchantillonsDecorated().indexOf(deco))
                     .getFirstChild().getNextSibling();
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

   /************************ @since 2.2.2-diamic evolutions ****************/
   public boolean getHasInjectableEchantillons(){
      final ResultatInjection res = SessionUtils.getDossierExterneInjection(sessionScope);
      return res != null && !res.getEchanAdrls().isEmpty();
   }

   public List<Echantillon> getInjectableEchantillons(){
      final ResultatInjection res = SessionUtils.getDossierExterneInjection(sessionScope);
      final List<Echantillon> echans = new ArrayList<>();
      if(res != null){
         echans.addAll(res.getEchanAdrls().keySet());
         echans.sort(Comparator.comparing(Echantillon::getCode));
      }
      return echans;
   }

   public void onSelect$injectableEchantillonsBox(){

      final int ind = injectableEchantillonsBox.getSelectedIndex();
      Echantillon echan = null;

      if(ind > -1){
         echan = getInjectableEchantillons().get(ind);
      }

      injectEchantillon(echan);
   }

   protected void injectEchantillon(final Echantillon echan){
      if(echan != null){
         clearForm(false);

         setCodePrefixe(echan.getCode());
         setSelectedType(echan.getEchantillonType());

         final Echantillon newObj = echan.clone();
         echan.setPrelevement(getParentObject());
         setEchantillon(newObj);

         // car setObj passe prelevement à null..
         // setParentObject(echan.getPrelevement());

         initEditableMode();

         getBinder().loadComponent(self);

         // déclenche manuellement la validation de concordance
         // des dates
         // onBlur$dateStockCalBox();
      }
   }

   //	/**
   //	 * Selectionne les items dans les listes après une injection
   //	 */
   //	private void initSelectedInList() {
   //
   //		// Méthode inspirée initCollaborations et allégée pour appel uniquement dans
   //		// injection opérateur depuis interfaçage.
   //		if (getObject().getCollaborateur() != null) {
   //			setSelectedCollaborateur(this.getObject().getCollaborateur());
   //			collabBox.setValue(getSelectedCollaborateur().getNomAndPrenom());
   //		} else {
   //			setSelectedCollaborateur(null);
   //		}
   //	}

   /**
    * Validation sécifique des listboxes obligatoires Gatsbi surcharge cette
    * méthode
    *
    * @since 2.3.0-gatsbi
    */
   @Override
   protected void checkRequiredListboxes(){

      if(getSelectedType() == null){
         // ferme wait message si besoin
         Clients.clearBusy();
         Clients.scrollIntoView(typesBoxEchan);
         throw new WrongValueException(typesBoxEchan, Labels.getLabel("ficheEchantillon.error.type"));
      }
      Clients.clearWrongValue(typesBoxEchan);
   }
}