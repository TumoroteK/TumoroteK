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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.Validator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Box;
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
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.stockage.StockageController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DeriveBatchSaveException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EmplacementDoublonFoundException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.helper.FileBatch;
import fr.aphp.tumorotek.manager.impl.xml.BoiteImpression;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche de création de
 * plusieurs dérivés.
 * Controller créé le 16/02/10.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class FicheMultiProdDerive extends FicheProdDeriveEdit
{

   private final Log log = LogFactory.getLog(FicheModifMultiProdDerive.class);

   private static final long serialVersionUID = 8521632198848434054L;

   // Boutons supplémentaires.
   // private Group groupInfosCompDerive;

   private Combobox separatorBox;
   private Intbox premierCodeBoxDerive;
   private Intbox dernierCodeBoxDerive;
   private Textbox premiereLettreBoxDerive;
   private Textbox derniereLettreBoxDerive;
   private Radio numNombres;
   private Radio numLettres;
   private Box choixNumerotation;
   private Button changeNumerotation;

   private Grid derivesList;

   private Button stockageDerives;

   //Objets Principaux.
   // ajout de dérivés
   private List<ProdDerive> prodDerives = new ArrayList<>();
   private List<ProdDeriveDecorator2> prodDerivesDecorated = new ArrayList<>();
   private List<List<ProdDerive>> addedProdDerives = new ArrayList<>();
   private List<String> usedCodesProdDerives = new ArrayList<>();
   private Hashtable<ProdDerive, Emplacement> derivesEmpl = new Hashtable<>();

   // Variables formulaire.
   //private String valeurQuantiteRestante = "";
   //private String valeurVolumeRestant = "";
   private Integer premierCode;
   private Integer dernierCode;
   private String premiereLettre;
   private String derniereLettre;
   private List<String> lettres = new ArrayList<>();
   // quantite max que peut saisir l'utilisateur pour la transformation
   //private Float quantiteMax;
   //private Float quantiteTransformation;
   private boolean selectParent = false;
   //private String codeParent = "";
   //private Date dateTransformation;
   //private Float quantiteInit;
   private Date dateSortie;

   private final ProdDeriveDecoratorRowRenderer deriveDecoRenderer = new ProdDeriveDecoratorRowRenderer();

   private Retour transfoRetour = null;

   private final List<List<AnnotationValeur>> annoBatches = new ArrayList<>();
   private final List<FileBatch> batches = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

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

      lettres = Utils.createListChars(26, null, new ArrayList<String>());

      getBinder().loadAll();
   }

   /**
    * Change le mode de la fiche en creation.
    * @param parent Objet dont est issu le dérivé.
    */
   public void switchToCreateMode(final TKdataObject parent){
      // init with empty object
      //dateTransformation = null;
      //banque = getMainSelectedBanque();

      // Init du parent
      setParentObject(parent);

      super.switchToCreateMode();

      Clients.scrollIntoView(formGrid.getColumns());

      // si le parent n'est pas null, on l'associe au
      // nouveau dérivé
      if(parent != null){
         selectParent = false;
         setTypeParent(parent.getClass().getSimpleName());
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

      getBinder().loadComponent(self);
   }

   @Override
   public void onClick$create(){

      boolean ok = false;

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

         // proposition d'un événement de stockage
         if((getTypeParent().equals("Echantillon") || getTypeParent().equals("ProdDerive")) && transfoRetour == null){
            // on place l'ancienne adresse de stockage dans la
            // hashtable qu'on passera à la FicheRetour
            final HashMap<TKStockableObject, Emplacement> oldEmps = new HashMap<>();
            if(getOldEmplacement() != null){
               oldEmps.put((TKStockableObject) getParentObject(), getOldEmplacement());
            }
            // on place le parent des dérivés dans la
            // liste qu'on passera à la FicheRetour
            final List<TKStockableObject> list = new ArrayList<>();
            list.add((TKStockableObject) getParentObject());

            proposeRetoursCreation(list, oldEmps, dateSortie, null, getTransformation(), null,
               ManagerLocator.getOperationTypeManager().findByNomLikeManager("Creation", true).get(0),
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
         if(getTypeParent().equals("Echantillon")){
            statut = prepareUpdateParentEchantillon();
         }else if(getTypeParent().equals("ProdDerive")){
            statut = prepareUpdateParentProduitDerive();
         }

         // validation retour si epuisement
         // ou si retour incomplet
         transfoRetour = null;
         if(getTypeParent().equals("Echantillon") || getTypeParent().equals("ProdDerive")){

            final Calendar dateS = Calendar.getInstance();
            if(dateSortie != null){
               dateS.setTime(dateSortie);
            }

            final List<Integer> id = new ArrayList<>();
            id.add(((TKStockableObject) getParentObject()).listableObjectId());
            final List<Retour> rets =
               ManagerLocator.getRetourManager().findByObjectDateRetourEmptyManager(id, getTransformation().getEntite());
            if(!rets.isEmpty()){
               transfoRetour = rets.get(0);
            }

            if(statut.getStatut().equals("EPUISE")){
               if(transfoRetour == null){
                  transfoRetour = new Retour();
                  transfoRetour.setDateSortie(dateS);
                  //finalRetour.setDateRetour(Utils.getCurrentSystemCalendar());
                  // température arbitraire
                  transfoRetour.setTempMoyenne(new Float(20.0));
                  transfoRetour.setObjetId(((TKStockableObject) getParentObject()).listableObjectId());
                  transfoRetour.setEntite(entite);
               }else{
                  transfoRetour.setObjetStatut(null);
               }
               transfoRetour.setObservations(Labels.getLabel("ficheRetour.observations.derives") + " "
                  + Labels.getLabel("ficheRetour.observations.epuisement"));

            }else{ // complete le retour existant
               if(transfoRetour != null){
                  transfoRetour.setDateRetour(dateS);
                  statut = transfoRetour.getObjetStatut();
                  transfoRetour.setObjetStatut(null);
               }
            }
            if(transfoRetour != null){
               BeanValidator.validateObject(transfoRetour, new Validator[] {ManagerLocator.getRetourValidator()});
            }
         }

         for(int i = 0; i < addedProdDerives.size(); i++){

            ManagerLocator.getProdDeriveManager().createDeriveListWithAnnotsManager(addedProdDerives.get(i), getBanque(),
               getTransformation(), SessionUtils.getLoggedUser(sessionScope),
               //	getObjectTabController().getFicheAnnotation()
               //							.getValeursToCreateOrUpdate(),
               annoBatches.get(i), SessionUtils.getSystemBaseDir(), null, null);
         }

         saveEmplacements();

         for(int i = 0; i < prodDerivesDecorated.size(); i++){
            if(prodDerivesDecorated.get(i).getProdDerive().getConformeTraitement() != null
               && !prodDerivesDecorated.get(i).getProdDerive().getConformeTraitement()){
               // enregistrement de la non conformité 
               // après traitement
               ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(
                  prodDerivesDecorated.get(i).getProdDerive(), prodDerivesDecorated.get(i).getNonConformiteTraitements(),
                  "Traitement");
            }

            if(prodDerivesDecorated.get(i).getProdDerive().getConformeCession() != null
               && !prodDerivesDecorated.get(i).getProdDerive().getConformeCession()){
               // enregistrement de la non conformité 
               // à la cession
               ManagerLocator.getObjetNonConformeManager().createUpdateOrRemoveListObjectManager(
                  prodDerivesDecorated.get(i).getProdDerive(), prodDerivesDecorated.get(i).getNonConformiteCessions(), "Cession");
            }
         }

         // annotation file batches
         for(final FileBatch batch : batches){
            ManagerLocator.getAnnotationValeurManager().createFileBatchForTKObjectsManager(batch.getObjs(), batch.getFile(),
               batch.getStream(), batch.getChamp(), getBanque(), SessionUtils.getLoggedUser(sessionScope),
               SessionUtils.getSystemBaseDir(), filesCreated);
         }

         // statut EPUISE --> creation retours automatique
         if(transfoRetour != null){
            // passage temporaire au statut NON STOCKE 
            // afin que ce statut soit enregistré dans le retour correspondant 
            // à l'épuisement de l'échantillon
            ((TKStockableObject) getParentObject())
               .setObjetStatut(ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0));

            ManagerLocator.getRetourManager().createOrUpdateObjectManager(transfoRetour, (TKStockableObject) getParentObject(),
               getOldEmplacement(), null, null, getTransformation(), null, SessionUtils.getLoggedUser(sessionScope), "creation");

         }

         // s'il n'y a pas d'erreurs, on met à jour le parent : modif
         // de sa quantité et de son volume
         if(getTypeParent().equals("Prelevement")){
            updateParentPrelevement();
         }else if(getTypeParent().equals("Echantillon")){
            updateParentEchantillon(statut);
         }else if(getTypeParent().equals("ProdDerive")){
            updateParentProduitDerive(statut);
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

      createFileHtmlToPrint();
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

      //		} catch (ValidationException ve) {
      //			errorMsg.add("- Erreur lors de la validation.");
      //		} catch (InvalidPositionException ipose) {
      //			errorMsg.add("- Erreur sur la " 
      //					+ "position d'un emplacement.");
      //		} catch (RequiredObjectIsNullException re) {
      //			errorMsg.add("- Objet manquant lors du stockage.");
      //		} catch (EntiteObjectIdNotExistException nee) {
      //			errorMsg.add("- Objet à stocker inexistant.");
      //		} catch (DoublonFoundException de) {
      //			errorMsg.add("- Erreur sur l'emplacement de stockage.");
      //		}

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
            null, null, deriveToUpdate.getReservation(), SessionUtils.getLoggedUser(sessionScope), false, ops, null);

         final ProdDeriveDecorator2 deco = new ProdDeriveDecorator2(deriveToUpdate);
         // si le dérivé existait déjà, on MAJ sa fiche
         // dans la liste des dérivés
         for(final List<ProdDerive> derives : addedProdDerives){
            if(!derives.contains(deco)){
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
                  final Integer pos = bi.getPositions().size() + 1;
                  bi.getPositions().add(emp.getPosition());
                  bi.getElements().add(ObjectTypesFormatters.getLabel("impression.boite.numero.prodDerive",
                     new String[] {pos.toString(), derive.getCode()}));
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
    * @param Event : clic sur le lien addProdDerives.
    */
   public void onClick$addProdDerives(final Event event){
      try{
         onBlur$dateStockCalBox();
         onBlur$dateTransfoCalBox();
         if(getSelectedType() == null){
            Clients.scrollIntoView(typesBoxDerive);
            throw new WrongValueException(typesBoxDerive, Labels.getLabel("ficheProdDerive.error.type"));
         }

         // si le dérivé est issu d'un parent connu
         if(selectParent && !getTypeParent().equals("Aucun")){
            cttCodeParent.validate(codesParentBoxDerive, null);
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

         final ObjetStatut statut = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("NON STOCKE", true).get(0);

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
            first = premierCode;
            last = dernierCode;
         }else{
            first = lettres.indexOf(premiereLettre.toUpperCase());
            last = lettres.indexOf(derniereLettre.toUpperCase());
         }

         // Création de tous les nouveaux échantillons
         for(int i = first; i <= last; i++){

            // création du code échantillon en fct de celui du prlvt et
            // du numéro saisi
            final StringBuffer sb = new StringBuffer();

            // 2.0.10.6 VIROBIOTEC création codes sans prefixe
            if(getCodePrefixe() != null && !getCodePrefixe().trim().equals("")){
               sb.append(getCodePrefixe());
               //sb.append(".");
               sb.append(separatorBox.getValue() != null ? separatorBox.getValue() : "");
            }
            if(numNombres.isChecked()){
               sb.append(i);
            }else{
               sb.append(lettres.get(i));
            }

            usedCodesProdDerives.add(sb.toString());

            final ProdDerive newProdDerive = new ProdDerive();

            newProdDerive.setBanque(this.getBanque());
            newProdDerive.setProdType(getSelectedType());
            newProdDerive.setCode(sb.toString());
            newProdDerive.setCodeLabo(getProdDerive().getCodeLabo());
            newProdDerive.setObjetStatut(statut);
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
            newProdDerive.setReservation(null);
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

         //			String id = groupInfosCompDerive.getUuid();
         //			String idTop = panelChildrenWithScroll.getUuid();
         //			Clients.evalJavaScript("document.getElementById('" + idTop + "')" 
         //					+ ".scrollTop = document.getElementById('" + id + "')" 
         //					+ ".offsetTop;");
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
      //		EchantillonDecorator2 deco = (EchantillonDecorator2) 
      //			AbstractListeController2
      //				.getBindingData((ForwardEvent) event, false);

      ProdDeriveDecorator2 deco = (ProdDeriveDecorator2) event.getOrigin().getData();

      ProdDerive deletedDerive = deco.getProdDerive();

      for(final List<ProdDerive> derives : addedProdDerives){
         if(derives.contains(deletedDerive)){
            derives.remove(deletedDerive);
            prodDerives.remove(deletedDerive);
            prodDerivesDecorated.remove(deco);
            usedCodesProdDerives.remove(deletedDerive.getCode());

            // on enlève l'association entre le dérivé et son
            // emplacement dans la hashtable
            if(derivesEmpl.containsKey(deletedDerive)){
               derivesEmpl.remove(deletedDerive);
            }

            final ListModel<ProdDeriveDecorator2> list = new ListModelList<>(prodDerivesDecorated);
            derivesList.setModel(list);

            // derive retire fileBatches
            for(final FileBatch batch : batches){
               batch.getObjs().remove(deletedDerive);
            }

            deletedDerive = null;
            deco = null;

            break;
         }
      }

      // desactive le lien vers le stockage
      if(prodDerives.isEmpty()){
         stockageDerives.setDisabled(true);
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
         // on récupère tous les dérivés NON STOCKE
         final List<ProdDerive> derivesToStock = new ArrayList<>();
         for(int i = 0; i < prodDerives.size(); i++){
            if(prodDerives.get(i).getObjetStatut().getStatut().equals("NON STOCKE")){
               derivesToStock.add(prodDerives.get(i));
            }
         }

         // si des emplacements ont déja ete définis pour des dérivés
         // ils sont réservés
         final Enumeration<Emplacement> empls = derivesEmpl.elements();
         final List<Emplacement> reserves = new ArrayList<>();
         while(empls.hasMoreElements()){
            reserves.add(empls.nextElement());
         }

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
    * @param table dérivé - emplacements
    */
   private void updateDecoList(final Hashtable<ProdDerive, Emplacement> results){
      final ObjetStatut stocke = ManagerLocator.getObjetStatutManager().findByStatutLikeManager("STOCKE", true).get(0);

      final Set<ProdDerive> prods = results.keySet();
      final Iterator<ProdDerive> it = prods.iterator();
      // pour chaque dérivé qui vient d'être stocké
      while(it.hasNext()){
         final ProdDerive tmp = it.next();
         final ProdDeriveDecorator2 deco = new ProdDeriveDecorator2(tmp);
         // on récupère son decorator correspondant dans la liste
         if(prodDerivesDecorated.contains(deco)){
            final ProdDeriveDecorator2 decoUp = prodDerivesDecorated.get(prodDerivesDecorated.indexOf(deco));

            // MAJ du stockage de cet échantillon
            decoUp.setAdrlTmp(results.get(tmp).getAdrl());
            decoUp.getProdDerive().setObjetStatut(stocke);
         }

         // ajout du couple dans la hashtable
         if(!derivesEmpl.contains(tmp)){
            derivesEmpl.put(tmp, results.get(tmp));
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

      setTypes((List<ProdType>) ManagerLocator.getProdTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
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

      setQualites(
         (List<ProdQualite>) ManagerLocator.getProdQualiteManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      getQualites().add(0, null);

      setModePrepaDerives((List<ModePrepaDerive>) ManagerLocator.getModePrepaDeriveManager()
         .findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
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
      usedCodesProdDerives = ManagerLocator.getProdDeriveManager().findAllCodesForBanqueManager(getBanque());

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
    * Recherche des doublons pour le permier et dernier codes saisis.
    * @param premier Premier code des dérivés à créer.
    * @param dernier Dernier code des dérivés à créer.
    * @return True s'il y a des doublons, false sinon.
    */
   public List<String> findDoublons(final Integer premier, final Integer dernier){

      final List<String> doublons = new ArrayList<>();
      final String codePrefixeTmp = this.codePrefixeLabelDerive.getValue();
      for(int i = premier; i <= dernier; i++){
         final StringBuffer sb = new StringBuffer();
         if(codePrefixeTmp != null && !codePrefixeTmp.trim().equals("")){
            sb.append(codePrefixeTmp);
            // sb.append(".");
            sb.append(separatorBox.getValue() != null ? separatorBox.getValue() : "");
         }
         // sb.append(codePrefixeTmp);
         // sb.append(".");
         sb.append(i);

         if(usedCodesProdDerives.contains(sb.toString())){
            doublons.add(String.valueOf(i));
         }
      }

      return doublons;
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
   public class ConstPremierCode implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){

         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des chiffres en
         // numérotation
         if(numNombres.isChecked()){
            // on récupère la valeur dans premierCodeBoxDerive
            premierCode = (Integer) value;
            // si une valeur est saisie dans le champ premierCodeBoxDerive
            if(premierCode != null){
               // si le premier code est négatif
               if(premierCode <= 0){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.code.sup.zero"));
               }
               // Si le dernierCode est null (1ère édition de la page)
               // on va récupérer la valeur du champ dernierCodeBoxDerive
               if(dernierCode == null){
                  // on enlève la contrainte de dernierCodeBoxDerive pour
                  // pouvoir récupérer sa valeur
                  dernierCodeBoxDerive.setConstraint("");
                  dernierCodeBoxDerive.clearErrorMessage();
                  dernierCode = dernierCodeBoxDerive.getValue();
                  // on remet la contrainte
                  dernierCodeBoxDerive.setConstraint(cttDernierCode);
               }

               // si une valeur est saisie dans le champ dernierCodeBoxDerive
               if(dernierCode != null){
                  // si le dernier code est < au premier
                  if(dernierCode < premierCode){
                     throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.premier.code.superieur"));
                  }else{

                     // sinon on enlève toutes les erreurs affichées
                     Integer tmp = dernierCode;
                     Clients.clearWrongValue(dernierCodeBoxDerive);
                     dernierCodeBoxDerive.setConstraint("");
                     dernierCodeBoxDerive.setValue(tmp);
                     dernierCodeBoxDerive.setConstraint(cttDernierCode);

                     final List<String> doublons = findDoublons(premierCode, dernierCode);
                     // si des doublons existent pour les valeurs saisies
                     if(doublons.size() > 0){
                        final StringBuffer sb = new StringBuffer();
                        if(doublons.size() == 1){
                           sb.append(Labels.getLabel("ficheMultiProdDerive.error.un.prodDerive" + ".existant"));
                           sb.append(doublons.get(0));
                        }else{
                           sb.append(Labels.getLabel("ficheMultiProdDerive.error.plus.prodDerive" + ".existant"));
                           for(int i = 0; i < doublons.size(); i++){
                              sb.append(doublons.get(i));
                              if(i + 1 < doublons.size()){
                                 sb.append(", ");
                              }
                           }
                        }
                        sb.append(Labels.getLabel("ficheMultiProdDerive.error.prodDerive" + ".existant.saisie"));
                        throw new WrongValueException(comp, sb.toString());
                     }else{
                        // sinon on enlève toutes les erreurs affichées
                        tmp = dernierCode;
                        Clients.clearWrongValue(dernierCodeBoxDerive);
                        dernierCodeBoxDerive.setConstraint("");
                        dernierCodeBoxDerive.setValue(tmp);
                        dernierCodeBoxDerive.setConstraint(cttDernierCode);
                     }
                  }
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final Integer tmp = dernierCode;
                  Clients.clearWrongValue(dernierCodeBoxDerive);
                  dernierCodeBoxDerive.setConstraint("");
                  dernierCodeBoxDerive.setValue(tmp);
                  dernierCodeBoxDerive.setConstraint(cttDernierCode);
               }
            }else{
               throw new WrongValueException(comp, "Champ vide non autorisé. " + "Vous devez spécifier une valeur.");
            }
         }else{
            Clients.clearWrongValue(premierCodeBoxDerive);
            premierCodeBoxDerive.setConstraint("");
            premierCodeBoxDerive.setValue(null);
            premierCodeBoxDerive.setConstraint(cttPremierCode);
            Clients.clearWrongValue(dernierCodeBoxDerive);
            dernierCodeBoxDerive.setConstraint("");
            dernierCodeBoxDerive.setValue(null);
            dernierCodeBoxDerive.setConstraint(cttDernierCode);
         }
      }
   }

   /**
    * 
    * @author Pierre Ventadour.
    *
    */
   public class ConstDernierCode implements Constraint
   {

      // on ne prend en compte cette contrainte que si
      // l'utilisateur veut utiliser des chiffres en
      // numérotation
      @Override
      public void validate(final Component comp, final Object value){

         if(numNombres.isChecked()){
            // on récupère la valeur dans dernierCodeBoxDerive
            dernierCode = (Integer) value;
            // si une valeur est saisie dans le champ dernierCodeBoxDerive
            if(dernierCode != null){
               // si le dernier code est négatif
               if(dernierCode <= 0){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiProdDerive.error.code.sup.zero"));
               }
               // Si le premierCode est null (1ère édition de la page)
               // on va récupérer la valeur du champ premierCodeBoxDerive
               if(premierCode == null){
                  // on enlève la contrainte de premierCodeBoxDerive pour
                  // pouvoir récupérer sa valeur
                  premierCodeBoxDerive.setConstraint("");
                  premierCodeBoxDerive.clearErrorMessage();
                  premierCode = premierCodeBoxDerive.getValue();
                  // on remet la contrainte
                  premierCodeBoxDerive.setConstraint(cttPremierCode);
               }

               // si une valeur est saisie dans le champ premierCodeBoxDerive
               if(premierCode != null){
                  // si le dernier code est < au premier
                  if(dernierCode < premierCode){
                     throw new WrongValueException(comp,
                        Labels.getLabel("ficheMultiProdDerive.error.premier.code" + ".superieur"));
                  }else{

                     // sinon on enlève toutes les erreurs affichées
                     Integer tmp = premierCode;
                     Clients.clearWrongValue(premierCodeBoxDerive);
                     premierCodeBoxDerive.setConstraint("");
                     premierCodeBoxDerive.setValue(tmp);
                     premierCodeBoxDerive.setConstraint(cttPremierCode);

                     final List<String> doublons = findDoublons(premierCode, dernierCode);
                     // si des doublons existent pour les valeurs saisies
                     if(doublons.size() > 0){
                        final StringBuffer sb = new StringBuffer();
                        if(doublons.size() == 1){
                           sb.append(Labels.getLabel("ficheMultiProdDerive.error.un" + ".prodDerive.existant"));
                           sb.append(doublons.get(0));
                        }else{
                           sb.append(Labels.getLabel("ficheMultiProdDerive.error.plus." + "prodDerive.existant"));
                           for(int i = 0; i < doublons.size(); i++){
                              sb.append(doublons.get(i));
                              if(i + 1 < doublons.size()){
                                 sb.append(", ");
                              }
                           }
                        }
                        sb.append(Labels.getLabel("ficheMultiProdDerive.error.prodDerive" + ".existant.saisie"));
                        throw new WrongValueException(comp, sb.toString());
                     }else{
                        // sinon on enlève toutes les erreurs affichées
                        tmp = premierCode;
                        Clients.clearWrongValue(premierCodeBoxDerive);
                        premierCodeBoxDerive.setConstraint("");
                        premierCodeBoxDerive.setValue(tmp);
                        premierCodeBoxDerive.setConstraint(cttPremierCode);
                     }
                  }
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final Integer tmp = premierCode;
                  Clients.clearWrongValue(premierCodeBoxDerive);
                  premierCodeBoxDerive.setConstraint("");
                  premierCodeBoxDerive.setValue(tmp);
                  premierCodeBoxDerive.setConstraint(cttPremierCode);
               }
            }else{
               throw new WrongValueException(comp, "Champ vide non autorisé. " + "Vous devez spécifier une valeur.");
            }
         }else{
            Clients.clearWrongValue(premierCodeBoxDerive);
            premierCodeBoxDerive.setConstraint("");
            premierCodeBoxDerive.setValue(null);
            premierCodeBoxDerive.setConstraint(cttPremierCode);
            Clients.clearWrongValue(dernierCodeBoxDerive);
            dernierCodeBoxDerive.setConstraint("");
            dernierCodeBoxDerive.setValue(null);
            dernierCodeBoxDerive.setConstraint(cttDernierCode);
         }
      }
   }

   public class ConstPremiereLettre implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){
         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des lettres en
         // numérotation
         if(numLettres.isChecked()){
            // on récupère la valeur dans premiereLettreBoxDerive
            premiereLettre = (String) value;

            // si une valeur est saisie
            if(premiereLettre != null && !premiereLettre.equals("")){
               // si le premier code est invalide
               if(!lettres.contains(premiereLettre.toUpperCase())){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillons.lettre.invalide"));
               }
               // Si la derniereLettre est null (1ère édition de la page)
               // on va récupérer la valeur du champ derniereLettreBoxDerive
               if(derniereLettre == null || derniereLettre.equals("")){
                  // on enlève la contrainte de 
                  // derniereLettreBoxDerive pour
                  // pouvoir récupérer sa valeur
                  derniereLettreBoxDerive.setConstraint("");
                  derniereLettreBoxDerive.clearErrorMessage();
                  derniereLettre = derniereLettreBoxDerive.getValue();
                  // on remet la contrainte
                  derniereLettreBoxDerive.setConstraint(cttDerniereLettre);
               }

               // si une valeur est saisie dans le champ 
               // derniereLettreBoxDerive
               if(derniereLettre != null && !derniereLettre.equals("")){
                  // si la derniere est avant la premiere
                  if(lettres.indexOf(derniereLettre.toUpperCase()) < lettres.indexOf(premiereLettre.toUpperCase())){
                     throw new WrongValueException(comp,
                        "La première lettre saisie ne " + "peut pas être alphabétiquement " + "après la dernière.");
                  }else{

                     // sinon on enlève toutes les erreurs affichées
                     String tmp = derniereLettre;
                     Clients.clearWrongValue(derniereLettreBoxDerive);
                     derniereLettreBoxDerive.setConstraint("");
                     derniereLettreBoxDerive.setValue(tmp);
                     derniereLettreBoxDerive.setConstraint(cttDerniereLettre);

                     final List<String> doublons =
                        findDoublonsForLetters(premiereLettre.toUpperCase(), derniereLettre.toUpperCase());
                     // si des doublons existent pour les valeurs saisies
                     if(doublons.size() > 0){
                        final StringBuffer sb = new StringBuffer();
                        if(doublons.size() == 1){
                           sb.append("Echantillon déjà enregistré " + "pour la lettre : {");
                           sb.append(doublons.get(0));
                        }else{
                           sb.append("Echantillons déjà enregistrés " + "pour les lettres : {");
                           for(int i = 0; i < doublons.size(); i++){
                              sb.append(doublons.get(i));
                              if(i + 1 < doublons.size()){
                                 sb.append(", ");
                              }
                           }
                        }
                        sb.append("}. Veuillez modifier les lettres saisies.");
                        throw new WrongValueException(comp, sb.toString());
                     }else{
                        // sinon on enlève toutes les erreurs affichées
                        tmp = derniereLettre;
                        Clients.clearWrongValue(derniereLettreBoxDerive);
                        derniereLettreBoxDerive.setConstraint("");
                        derniereLettreBoxDerive.setValue(tmp);
                        derniereLettreBoxDerive.setConstraint(cttDerniereLettre);
                     }
                  }
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final String tmp = derniereLettre;
                  Clients.clearWrongValue(derniereLettreBoxDerive);
                  derniereLettreBoxDerive.setConstraint("");
                  derniereLettreBoxDerive.setValue(tmp);
                  derniereLettreBoxDerive.setConstraint(cttDerniereLettre);
               }
            }else{
               throw new WrongValueException(comp, "Champ vide non autorisé. " + "Vous devez spécifier une valeur.");
            }
         }else{
            Clients.clearWrongValue(premiereLettreBoxDerive);
            premiereLettreBoxDerive.setConstraint("");
            premiereLettreBoxDerive.setValue(null);
            premiereLettreBoxDerive.setConstraint(cttPremiereLettre);
            Clients.clearWrongValue(derniereLettreBoxDerive);
            derniereLettreBoxDerive.setConstraint("");
            derniereLettreBoxDerive.setValue(null);
            derniereLettreBoxDerive.setConstraint(cttDerniereLettre);
         }
      }
   }

   private Constraint cttPremiereLettre = new ConstPremiereLettre();

   /**
    * 
    * @author Pierre Ventadour.
    *
    */
   public class ConstDerniereLettre implements Constraint
   {

      @Override
      public void validate(final Component comp, final Object value){
         // on ne prend en compte cette contrainte que si
         // l'utilisateur veut utiliser des lettres en
         // numérotation
         if(numLettres.isChecked()){
            // on récupère la valeur dans derniereLettreBoxDerive
            derniereLettre = (String) value;

            // si une valeur est saisie
            if(derniereLettre != null && !derniereLettre.equals("")){
               // si la derniere lettre n'est pas valide
               if(!lettres.contains(derniereLettre.toUpperCase())){
                  throw new WrongValueException(comp, Labels.getLabel("ficheMultiEchantillons.lettre.invalide"));
               }
               // Si la premiere lettre est null (1ère édition de la page)
               // on va récupérer la valeur 
               // du champ premiereLettreBoxDerive
               if(premiereLettre == null || premiereLettre.equals("")){
                  // on enlève la contrainte pour
                  // pouvoir récupérer sa valeur
                  premiereLettreBoxDerive.setConstraint("");
                  premiereLettreBoxDerive.clearErrorMessage();
                  premiereLettre = premiereLettreBoxDerive.getValue();
                  // on remet la contrainte
                  premiereLettreBoxDerive.setConstraint(cttPremiereLettre);
               }

               // si une valeur est saisie dans le champ 
               // premiereLettreBoxDerive
               if(premiereLettre != null && !premiereLettre.equals("")){
                  // si le dernier code est < au premier
                  // si la derniere est avant la premiere
                  if(!lettres.contains(premiereLettre.toUpperCase())
                     || lettres.indexOf(derniereLettre.toUpperCase()) < lettres.indexOf(premiereLettre.toUpperCase())){
                     throw new WrongValueException(comp,
                        "La première lettre saisie ne " + "peut pas être alphabétiquement " + "après la dernière.");
                  }else{
                     // sinon on enlève toutes les erreurs affichées
                     String tmp = premiereLettre;
                     Clients.clearWrongValue(premiereLettreBoxDerive);
                     premiereLettreBoxDerive.setConstraint("");
                     premiereLettreBoxDerive.setValue(tmp);
                     premiereLettreBoxDerive.setConstraint(cttPremiereLettre);

                     final List<String> doublons =
                        findDoublonsForLetters(premiereLettre.toUpperCase(), derniereLettre.toUpperCase());
                     // si des doublons existent pour les valeurs saisies
                     if(doublons.size() > 0){
                        final StringBuffer sb = new StringBuffer();
                        if(doublons.size() == 1){
                           sb.append("Echantillon déjà enregistré " + "pour la lettre : {");
                           sb.append(doublons.get(0));
                        }else{
                           sb.append("Echantillons déjà enregistrés " + "pour les lettres : {");
                           for(int i = 0; i < doublons.size(); i++){
                              sb.append(doublons.get(i));
                              if(i + 1 < doublons.size()){
                                 sb.append(", ");
                              }
                           }
                        }
                        sb.append("}. Veuillez modifier les lettres saisies.");
                        throw new WrongValueException(comp, sb.toString());
                     }else{
                        // sinon on enlève toutes les erreurs affichées
                        tmp = premiereLettre;
                        Clients.clearWrongValue(premiereLettreBoxDerive);
                        premiereLettreBoxDerive.setConstraint("");
                        premiereLettreBoxDerive.setValue(tmp);
                        premiereLettreBoxDerive.setConstraint(cttPremiereLettre);
                     }
                  }
               }else{
                  // sinon on enlève toutes les erreurs affichées
                  final String tmp = premiereLettre;
                  Clients.clearWrongValue(premiereLettreBoxDerive);
                  premiereLettreBoxDerive.setConstraint("");
                  premiereLettreBoxDerive.setValue(tmp);
                  premiereLettreBoxDerive.setConstraint(cttPremiereLettre);
               }
            }else{
               throw new WrongValueException(comp, "Champ vide non autorisé. " + "Vous devez spécifier une valeur.");
            }
         }else{
            Clients.clearWrongValue(premiereLettreBoxDerive);
            premiereLettreBoxDerive.setConstraint("");
            premiereLettreBoxDerive.setValue(null);
            premiereLettreBoxDerive.setConstraint(cttPremiereLettre);
            Clients.clearWrongValue(derniereLettreBoxDerive);
            derniereLettreBoxDerive.setConstraint("");
            derniereLettreBoxDerive.setValue(null);
            derniereLettreBoxDerive.setConstraint(cttDerniereLettre);
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
   private String codeTmp;

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
      //typesParentBoxDerive.setVisible(false);
      //codesParentBoxDerive.setVisible(false);
      //typeParentLabelDerive.setVisible(false);
      //codeParentLabelDerive.setVisible(false);
      //requiredCodeParentDerive.setVisible(false);

      // on réinitialise les listes de codes.
      //		codesParent = new ArrayList<String>();
      //		dictCodesModel = new SimpleListModel(codesParent);
      //		codesParentBoxDerive.setConstraint("");
      //		codesParentBoxDerive.clearErrorMessage();
      //		codesParentBoxDerive.setValue("");
      //		codesParentBoxDerive.setModel(dictCodesModel);
      //		codesParentBoxDerive.setConstraint(cttCodeParent);

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
      //typesParentBoxDerive.setVisible(true);
      //codesParentBoxDerive.setVisible(true);
      //typeParentLabelDerive.setVisible(true);
      //codeParentLabelDerive.setVisible(true);
      //requiredCodeParentDerive.setVisible(true);

      // on cache les données sur le parent
      for(int i = 0; i < getObjLabelsPrlvtParent().length; i++){
         getObjLabelsPrlvtParent()[i].setVisible(false);
         getObjLabelsEchanParent()[i].setVisible(false);
         getObjLabelsDeriveParent()[i].setVisible(false);
      }

      // par défaut, on récupère la liste des codes échantillons
      codesParent = ManagerLocator.getEchantillonManager()
         .findAllCodesForBanqueAndQuantiteManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
      // ces codes sont placés dans un dictionnaire pour permettre
      // le filtre lorsque l'utilisateur saisit le code
      dictCodesModel = new CustomSimpleListModel(codesParent.toArray());
      codesParentBoxDerive.setModel(dictCodesModel);
      setTypeParent("Echantillon");
   }

   /**
    * Sélection d'un code parent.
    * @param event Event : sélection dans la combobox codesParentBoxDerive.
    * @throws Exception
    */
   /*public void onSelect$codesParentBoxDerive(Event event) throws Exception {
   	if (codesParentBoxDerive.getSelectedItem() != null) {
   		setCodePrefixe(codesParentBoxDerive.getSelectedItem().getLabel());
   		setCodeParent(getCodePrefixe());
   		// si le parent est un prlvt
   		if (getTypeParent().equals("Prelevement")) {
   			setParentObject(ManagerLocator.getPrelevementManager()
   				.findByCodeOrNumLaboLikeWithBanqueManager(
   						getCodeParent(), getBanque(), true).get(0));
   			
   			// l'unité de la transformation sera celle du parent
   			setSelectedTransfoQuantiteUnite(((Prelevement) 
   									getParentObject()).getQuantiteUnite());
   			
   			// on récupère la quantité et le volume disponible
   			setQuantiteMax(((Prelevement) getParentObject()).getQuantite());
   			// si le parent est un échantillon
   		} else if (getTypeParent().equals("Echantillon")) {
   			setParentObject(ManagerLocator.getEchantillonManager()
   				.findByCodeLikeWithBanqueManager(
   						getCodeParent(), getBanque(), true).get(0));
   			
   			// l'unité de la transformation sera celle du parent
   			setSelectedTransfoQuantiteUnite(((Echantillon) 
   									getParentObject()).getQuantiteUnite());
   			
   			// on récupère la quantité et le volume disponible
   			setQuantiteMax(((Echantillon) getParentObject()).getQuantite());
   			// si le parent est un dérivé
   		} else if (getTypeParent().equals("ProdDerive")) {
   			setParentObject(ManagerLocator.getProdDeriveManager()
   				.findByCodeOrLaboWithBanqueManager(
   						getCodeParent(), getBanque(), true).get(0));
   			
   			// l'unité de la transformation sera celle du parent
   			setSelectedTransfoQuantiteUnite(((ProdDerive) 
   									getParentObject()).getQuantiteUnite());
   			
   			// on récupère la quantité et le volume disponible
   			setQuantiteMax(((ProdDerive) getParentObject()).getQuantite());
   		}
   	} 
   }*/

   /**
    * Mise à jour de la variable codeTmp à chaque modification
    * de l'utilisateur.
    * @param event Event : modification dans la combobox codesParentBoxDerive.
    * @throws Exception
    */
   /*public void onChanging$codesParentBoxDerive(InputEvent event) 
   throws Exception {
   	codeTmp = event.getValue();
   }*/

   /**
    * Mise à jour de la valeur sélectionnée dans la combobox lorsque
    * l'utilisateur clique à l'extérieur de celle-ci.
    * @param event Event : clique à l'extérieur de la combobox 
    * codesParentBoxDerive.
    * @throws Exception
    */
   public void onBlur$codesParentBoxDerive(final Event event) throws Exception{
      // on enlève la contrainte de la combobox
      codesParentBoxDerive.setConstraint("");
      codesParentBoxDerive.clearErrorMessage();
      // si aucun élément n'est sélectionné, la valeur de la box correspond 
      // à ce qu'a tapé l'utilisateur.
      if(codesParentBoxDerive.getSelectedItem() == null){
         codesParentBoxDerive.setValue(codeTmp);
      }else{
         // on valide la sélection
         cttCodeParent.validate(codesParentBoxDerive, null);
         setCodePrefixe(codesParentBoxDerive.getSelectedItem().getLabel());
         setCodeParent(getCodePrefixe());
         // si le parent est un prlvt
         if(getTypeParent().equals("Prelevement")){
            setParentObject(ManagerLocator.getPrelevementManager()
               .findByCodeOrNumLaboLikeWithBanqueManager(getCodeParent(), getBanque(), true).get(0));
         }else if(getTypeParent().equals("Echantillon")){
            setParentObject(
               ManagerLocator.getEchantillonManager().findByCodeLikeWithBanqueManager(getCodeParent(), getBanque(), true).get(0));
            // si le parent est un dérivé
         }else if(getTypeParent().equals("ProdDerive")){
            setParentObject(ManagerLocator.getProdDeriveManager()
               .findByCodeOrLaboWithBanqueManager(getCodeParent(), getBanque(), true).get(0));
         }
         initDeriveInfosFromParent();
      }
      // on ré-active la contrainte
      //codesParentBoxDerive.setModel(dictCodesModel);
      codesParentBoxDerive.setConstraint(cttCodeParent);
      cttCodeParent.validate(codesParentBoxDerive, null);

      // date validation
      dateTransfoCalBox.clearErrorMessage(dateTransfoCalBox.getValue());
      validateCoherenceDate(dateTransfoCalBox, dateTransfoCalBox.getValue());
      dateStockCalBox.clearErrorMessage(dateStockCalBox.getValue());
      validateCoherenceDate(dateStockCalBox, dateStockCalBox.getValue());

      getBinder().loadComponent(self);
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
               .findAllCodesForBanqueAndQuantiteManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
            noParentMode(false);
         }else if(getTypeParent().equals("Prelevement")){
            codesParent = ManagerLocator.getPrelevementManager()
               .findAllCodesForBanqueManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
            noParentMode(false);
         }else if(getTypeParent().equals("ProdDerive")){
            codesParent = ManagerLocator.getProdDeriveManager()
               .findAllCodesForBanqueAndQuantiteManager(SessionUtils.getSelectedBanques(sessionScope).get(0));
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
   public class ConstCodeParent implements Constraint
   {
      /**
       * Méthode de validation du champ dateTransfoBox.
       */
      @Override
      public void validate(final Component comp, final Object value){
         codesParentBoxDerive.setConstraint("");
         codesParentBoxDerive.clearErrorMessage();
         String code = "";
         if(codesParentBoxDerive.getSelectedItem() != null){
            code = codesParentBoxDerive.getSelectedItem().getLabel();
            //codesParentBoxDerive.setValue(code);
         }else{
            code = codesParentBoxDerive.getValue();
         }
         codesParentBoxDerive.setConstraint(cttCodeParent);

         if(code.length() == 0){
            throw new WrongValueException(comp,
               "Champ vide non autorisé. Vous devez spécifier un " + "code présent dans la liste.");
         }else if(!codesParent.contains(code)){
            throw new WrongValueException(comp,
               "Valeur non autorisée. Vous devez spécifier une " + "valeur présente dans la liste.");
         }
      }
   }

   //	/**
   //	 * Contrainte vérifiant que la quantite de transformation n'est 
   //	 * pas inférieure à 0 et n'est pas supérieure à celle du parent.
   //	 * @author Pierre Ventadour.
   //	 *
   //	 */
   //	public class ConstQuantiteTransformation implements Constraint {
   //		/**
   //		 * Méthode de validation du champ quantiteInitBox.
   //		 */
   //		public void validate(Component comp, Object value) {
   //			BigDecimal quantiteTransfoValue = (BigDecimal) value;
   //			if (quantiteTransfoValue != null) {
   //				setQuantiteTransformation(quantiteTransfoValue.floatValue());
   //				
   //				if (getQuantiteTransformation() < 0) {
   //					throw new WrongValueException(comp,
   //					Labels.getLabel("ficheMultiProdDerive.error.code.negatif"));
   //				} else {
   //					// sinon on enlève toutes les erreurs affichées
   //					BigDecimal decimal = 
   //						new BigDecimal(getQuantiteTransformation());
   //					transfoQuantiteBoxDerive.Clients.clearWrongValue();
   //					transfoQuantiteBoxDerive.setConstraint("");
   //					transfoQuantiteBoxDerive.setValue(decimal);
   //					transfoQuantiteBoxDerive
   //						.setConstraint(cttQuantiteTransfo);
   //					
   //					if (getQuantiteMax() != null
   //							&& getQuantiteTransformation() > getQuantiteMax()) {
   //						
   //						StringBuffer sb = new StringBuffer();
   //						sb.append(Labels.getLabel(
   //								"ficheProdDerive.error.quantite.transfo"));
   //						sb.append(" ");
   //						
   //						if (getTypeParent().equals("Prelevement")) {
   //							sb.append(" ");
   //							sb.append(Labels.getLabel(
   //								"ficheProdDerive.error.parent.prelevement"));
   //							sb.append(" ");
   //							sb.append(getQuantiteMax());
   //							sb.append(((Prelevement) getParentObject())
   //									.getQuantiteUnite().getUnite());
   //							sb.append(".");
   //						} else if (getTypeParent().equals("Echantillon")) {
   //							sb.append(" ");
   //							sb.append(Labels.getLabel(
   //								"ficheProdDerive.error.parent.echantillon"));
   //							sb.append(" ");
   //							sb.append(getQuantiteMax());
   //							sb.append(((Echantillon) getParentObject())
   //											.getQuantiteUnite().getUnite());
   //							sb.append(".");
   //						} else if (getTypeParent().equals("ProdDerive")) {
   //							sb.append(" ");
   //							sb.append(Labels.getLabel(
   //									"ficheProdDerive.error.parent.derive"));
   //							sb.append(" ");
   //							sb.append(getQuantiteMax());
   //							sb.append(((ProdDerive) getParentObject())
   //											.getQuantiteUnite().getUnite());
   //							sb.append(".");
   //						}
   //						
   //						throw new WrongValueException(
   //								comp, sb.toString());
   //					} else {
   //						// sinon on enlève toutes les erreurs affichées
   //						decimal = new BigDecimal(getQuantiteTransformation());
   //						transfoQuantiteBoxDerive.Clients.clearWrongValue();
   //						transfoQuantiteBoxDerive.setConstraint("");
   //						transfoQuantiteBoxDerive.setValue(decimal);
   //						transfoQuantiteBoxDerive
   //							.setConstraint(cttQuantiteTransfo);
   //					}
   //				}
   //			}
   //		}
   //	}
   //	private Constraint cttQuantiteTransfo = new ConstQuantiteTransformation();
   //	
   //	

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
   public class ConstQuantiteInit implements Constraint
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
            }else{

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
   }

   //
   //	public Constraint getCttQuantiteTransfo() {
   //		return cttQuantiteTransfo;
   //	}
   //
   //	public void setCttQuantiteTransfo(Constraint ctt) {
   //		this.cttQuantiteTransfo = ctt;
   //	}

   /**
    * Applique la validation sur la date.
    */
   @Override
   public void onBlur$dateTransfoCalBox(){
      final Datebox box = (Datebox) dateTransfoCalBox.getFirstChild().getFirstChild();
      boolean badDateFormat = false;
      if(box.getErrorMessage() != null && box.getErrorMessage().contains(box.getFormat())){
         badDateFormat = true;
      }else{
         final Calendar calValue = Calendar.getInstance();
         if(box.getValue() != null){
            calValue.setTime(box.getValue());
            if(calValue != null && !calValue.equals("")){
               if(calValue.get(Calendar.YEAR) > 9999){
                  badDateFormat = true;
               }
            }
         }
      }
      if(!badDateFormat){
         dateTransfoCalBox.clearErrorMessage(dateTransfoCalBox.getValue());
         validateCoherenceDate(dateTransfoCalBox, dateTransfoCalBox.getValue());
      }else{
         throw new WrongValueException(dateTransfoCalBox, Labels.getLabel("validation.invalid.date"));
      }
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
            if(calValue != null && !calValue.equals("")){
               if(calValue.get(Calendar.YEAR) > 9999){
                  badDateFormat = true;
               }
            }
         }
      }
      if(!badDateFormat){
         dateStockCalBox.clearErrorMessage(dateStockCalBox.getValue());
         validateCoherenceDate(dateStockCalBox, dateStockCalBox.getValue());
      }else{
         throw new WrongValueException(dateStockCalBox, Labels.getLabel("validation.invalid.date"));
      }
   }

   //	@Override
   //	protected void validateCoherenceDate(Component comp, Object value) {
   //		Date dateValue = (Date) value;		
   //		Errors errs = null;
   //		String field = "";
   //				
   //		if (dateValue == null || dateValue.equals("")) { 
   //			((Datebox) comp).clearErrorMessage(true);
   //			((Datebox) comp).setValue(null);
   //		} else {		
   //			// date transformation
   //			if (comp.getId().equals("dateTransfoBoxDerive")) {
   //				field = "dateTransformation";
   //				if (getProdDerive().getProdDeriveId() == null
   //						&& (!parentPrlvt.equals(new Prelevement())
   //						|| !parentEchantillon.equals(new Echantillon())
   //							|| !parentProdDerive.equals(new ProdDerive()))) {
   //					transformation
   //						.setEntite(findEntiteAndSetObjetIdForTransformation());
   //					getProdDerive().setTransformation(transformation);
   //				}
   //				getProdDerive().setDateTransformation(dateValue);
   //					
   //				errs = ManagerLocator.getProdDeriveValidator()
   //								.checkDateTransfoCoherence(getProdDerive());
   //			}
   //			
   //			// date stockage
   //			if (comp.getId().equals("dateBoxDerive")) {
   //				field = "dateStock";
   //				if (getProdDerive().getProdDeriveId() == null
   //						&& (!parentPrlvt.equals(new Prelevement())
   //						|| !parentEchantillon.equals(new Echantillon())
   //							|| !parentProdDerive.equals(new ProdDerive()))) {
   //					transformation
   //						.setEntite(findEntiteAndSetObjetIdForTransformation());
   //					getProdDerive().setTransformation(transformation);
   //				}
   //				getProdDerive().setDateStock(dateValue);
   //					
   //				errs = ManagerLocator.getProdDeriveValidator()
   //								.checkDateStockCoherence(getProdDerive());
   //			}
   //			
   //			if (errs != null && errs.hasErrors()) {
   //				
   //				throw new WrongValueException(
   //					comp, handleErrors(errs, field));
   //			}			
   //		} 
   //	}

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
      }else{
         Clients.clearWrongValue(typesBoxDerive);
      }
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

   /**
    * Recherche des doublons pour la permiere et derniere letres saisies.
    * @return True s'il y a des doublons, false sinon.
    */
   public List<String> findDoublonsForLetters(final String premier, final String dernier){

      final List<String> doublons = new ArrayList<>();
      for(int i = lettres.indexOf(premier); i <= lettres.indexOf(dernier); i++){
         final StringBuffer sb = new StringBuffer();
         if(getCodePrefixe() != null && !getCodePrefixe().trim().equals("")){
            sb.append(getCodePrefixe());
            // sb.append(".");
            sb.append(separatorBox.getValue() != null ? separatorBox.getValue() : "");

         }
         // sb.append(codePrefixeEchan.getValue());
         // sb.append(".");
         sb.append(lettres.get(i));

         if(usedCodesProdDerives.contains(sb.toString())){
            doublons.add(lettres.get(i));
         }
      }

      return doublons;
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

   public List<String> getLettres(){
      return lettres;
   }

   public void setLettres(final List<String> lettres){
      this.lettres = lettres;
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
}
