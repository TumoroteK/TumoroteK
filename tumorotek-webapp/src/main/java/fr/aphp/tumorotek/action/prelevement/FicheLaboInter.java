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

import static fr.aphp.tumorotek.model.contexte.EContexte.BTO;
import static fr.aphp.tumorotek.webapp.general.SessionUtils.getCurrentContexte;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.BindingListModel;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.patient.FichePatientStatic;
import fr.aphp.tumorotek.action.patient.PatientController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Temperature;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche des labos inters.
 * Controller créé le 25/11/2009.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FicheLaboInter extends AbstractFicheEditController
{

   private final Log log = LogFactory.getLog(FicheLaboInter.class);

   private static final long serialVersionUID = -422768239086454672L;

   /**
    *  Static Components pour le mode static.
    */
   private Grid gridFormPrlvtComp;

   // Buttons
   private Button previous;
   private Button next;
   private Button addLabo;

   /**
    *  Editable components : mode d'édition ou de création.
    */
   private CalendarBox dateDepartCalBox;
   private CalendarBox dateArriveeCalBox;
   private Decimalbox quantiteBoxLabo;
   //private Listbox collaborateursBoxLabo;
   private Grid laboIntersGrid;
   private Label operateurAideSaisieEchan;
   private Combobox collabBox;

   //	private Datebox dateDepartInterBox;
   //	private Datebox dateArriveeInterBox;
   //	private Label laboInterValidationLabel;

   private Checkbox congDepartBox;
   private Checkbox congArriveeBox;
   private Checkbox conformeArriveeBoxOui;
   private Checkbox conformeArriveeBoxNon;
   private Div conformeArriveeBox;
   private Listbox nonConformitesBox;

   /**
    *  Objets Principaux.
    */
   private Prelevement prelevement;
   private Maladie maladie;
   //private LaboInter currentLabo;
   private Div divCongDepart;
   private Div divCongArrivee;
   private Label conformeArriveeLabel;
   /**
    *  Associations.
    */
   private List<LaboInter> laboInters = new ArrayList<>();
   private List<LaboInter> oldLaboInters = new ArrayList<>();
   private List<LaboInter> laboIntersToDelete = new ArrayList<>();
   private List<Transporteur> transporteurs = new ArrayList<>();
   private Transporteur selectedTransporteur;
   private List<Collaborateur> collaborateurs = new ArrayList<>();
   private Collaborateur selectedCollaborateur;
   private List<String> nomsAndPrenoms = new ArrayList<>();
   private List<Unite> quantiteUnites = new ArrayList<>();
   private Unite selectedQuantiteUnite;
   private List<Service> allServices = new ArrayList<>();
   private List<Collaborateur> allCollaborateurs = new ArrayList<>();
   private List<Etablissement> allEtablissements = new ArrayList<>();
   private List<Temperature> temperatures = new ArrayList<>();
   private Temperature selectedTemperature;
   private List<NonConformite> nonConformites = new ArrayList<>();
   private NonConformite selectedNonConformite;
   private Set<Listitem> selectedNonConformitesItem = new HashSet<>();

   private Row rowQuantite;

   /**
    *  Variables formulaire.
    */
   private String valeurQuantite = "";
   // ordre maximum des labos inters du prlvt
   private Integer ordreMax = 0;

   private LaboInter cascadeNonSterileFrom = null;
   private Checkbox currentSterileLaboBox;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      initEditableMode();

      super.doAfterCompose(comp);
      if(BTO.equals(getCurrentContexte())){
         divCongDepart.setVisible(false);
         divCongArrivee.setVisible(false);
         conformeArriveeLabel.setValue("Conforme à l'emballage");
         rowQuantite.setVisible(false);
      }

      next.setDisabled(!getDroitOnAction("Echantillon", "Creation"));
   }

   @Override
   public void switchToCreateMode(){

      // Init du parent
      // this.maladie = parent;

      super.switchToCreateMode();

      // bouton pour revenir sur la fiche prlvt
      this.previous.setVisible(true);
      this.next.setVisible(true);
      this.addLabo.setVisible(true);

      if(!getDroitOnAction("Collaborateur", "Consultation")){
         operateurAideSaisieEchan.setVisible(false);
      }

      initAssociations();

      // scroll up pour se placer en haut de la page
      Clients.scrollIntoView(gridFormPrlvtComp.getColumns());
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      // bouton pour revenir sur la fiche prlvt
      this.previous.setVisible(true);
      this.next.setVisible(true);
      this.addLabo.setVisible(true);

      if(!getDroitOnAction("Collaborateur", "Consultation")){
         operateurAideSaisieEchan.setVisible(false);
      }

      initAssociations();
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){

      transporteurs = ManagerLocator.getTransporteurManager().findAllActiveManager();
      transporteurs.add(0, null);

      // init des collaborateurs
      nomsAndPrenoms = new ArrayList<>();
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
      }
      selectedCollaborateur = null;

      // on récupèé les unités de quantité
      quantiteUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("masse", true);
      List<Unite> tmpUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("discret", true);
      quantiteUnites.addAll(tmpUnites);
      tmpUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("volume", true);
      quantiteUnites.addAll(tmpUnites);
      quantiteUnites.add(0, null);

      allServices = ManagerLocator.getServiceManager().findAllActiveObjectsWithOrderManager();
      allServices.add(0, null);

      allCollaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      allCollaborateurs.add(0, null);

      allEtablissements = ManagerLocator.getEtablissementManager().findAllActiveObjectsWithOrderManager();
      allEtablissements.add(0, null);

      temperatures = ManagerLocator.getTemperatureManager().findAllObjectsManager();
      temperatures.add(0, null);
      selectedTemperature = null;
   }

   /**
    * Select les non conformites dans la dropdown list.
    * @param risks liste à selectionner
    */
   public void selectNonConformites(){

      final List<NonConformite> ncf = new ArrayList<>();
      if(prelevement != null && prelevement.getPrelevementId() != null){
         final List<ObjetNonConforme> list =
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(prelevement, "Arrivee");
         for(int i = 0; i < list.size(); i++){
            ncf.add(list.get(i).getNonConformite());
         }
      }

      selectedNonConformitesItem.clear();

      for(int i = 0; i < ncf.size(); i++){
         if(nonConformites.indexOf(ncf.get(i)) >= 0){
            selectedNonConformitesItem.add(nonConformitesBox.getItemAtIndex(nonConformites.indexOf(ncf.get(i))));
         }
      }
      //risquesBox.setSelectedItems(selectedRisques);
      getBinder().loadAttribute(nonConformitesBox, "selectedItems");

   }

   public void initNonConformites(){
      // init des non conformites
      nonConformites = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
         SessionUtils.getPlateforme(sessionScope), "Arrivee", getObjectTabController().getEntiteTab());
      selectedNonConformite = null;
      if(prelevement != null && prelevement.getPrelevementId() != null){
         final List<ObjetNonConforme> tmp =
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(prelevement, "Arrivee");
         if(tmp.size() > 0){
            if(nonConformites.contains(tmp.get(0).getNonConformite())){
               selectedNonConformite = tmp.get(0).getNonConformite();
            }
         }
      }
      getBinder().loadComponent(nonConformitesBox);
   }

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
         sb.append(this.prelevement.getQuantiteUnite().getUnite());
      }
      valeurQuantite = sb.toString();
   }

   /**
    * Méthode initialisant les objets associés.
    */
   public void initAssociations(){
      // Init des labos inters
      if(this.prelevement.getPrelevementId() != null){
         laboInters = ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(this.prelevement);
      }else{
         if(oldLaboInters != null && oldLaboInters.size() > 0){
            laboInters = oldLaboInters;
         }else{
            laboInters.clear();
         }
      }

      // s'il y a des labos inters, on récupère l'ordre max
      if(laboInters.size() > 0){
         final LaboInter last = laboInters.get(laboInters.size() - 1);
         if(last.getOrdre() != null){
            ordreMax = last.getOrdre();
         }else{
            ordreMax = 0;
         }
      }else{
         ordreMax = 0;
      }

      getBinder().loadAttribute(laboIntersGrid, "model");

      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
      collabBox.setValue("");
      if(this.prelevement != null){
         selectedTransporteur = this.prelevement.getTransporteur();

         if(this.prelevement.getOperateur() != null && collaborateurs.contains(this.prelevement.getOperateur())){
            selectedCollaborateur = this.prelevement.getOperateur();
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
         }else if(this.prelevement.getOperateur() != null){
            collaborateurs.add(this.prelevement.getOperateur());
            selectedCollaborateur = this.prelevement.getOperateur();
            nomsAndPrenoms.add(this.prelevement.getOperateur().getNomAndPrenom());
            collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
         }
         selectedQuantiteUnite = this.prelevement.getQuantiteUnite();
      }

      selectNonConformites();

   }

   @Override
   public void setObject(final TKdataObject obj){
      this.prelevement = (Prelevement) obj;

      this.maladie = this.prelevement.getMaladie();

      initNonConformites();

      if(this.prelevement.getConformeArrivee() != null){
         if(this.prelevement.getConformeArrivee()){
            conformeArriveeBoxOui.setChecked(true);
            conformeArriveeBoxNon.setChecked(false);
            conformeArriveeBox.setVisible(false);
         }else{
            conformeArriveeBoxOui.setChecked(false);
            conformeArriveeBoxNon.setChecked(true);
            conformeArriveeBox.setVisible(true);
         }
      }

      // Calendar boxes
      // dateDepartCalBox.setValue(this.prelevement.getDateDepart());
      // dateArriveeCalBox.setValue(this.prelevement.getDateArrivee());	

      //super.setObject(obj); pour eviter mise à jour annotations
      setCopy(((TKAnnotableObject) obj).clone());
      getBinder().loadAll();
   }

   @Override
   public TKdataObject getObject(){
      return this.prelevement;
   }

   @Override
   public void setNewObject(){}

   @Override
   public PrelevementController getObjectTabController(){
      return (PrelevementController) super.getObjectTabController();
   }

   /**
    * Retourne la fiche d'un prlvt.
    * @param event
    * @return
    */
   public FichePrelevementEdit getFichePrelevementEdit(){
      return getObjectTabController().getFicheEdit();
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
   public void createNewObject(){

      // on remplit le prlvt en fonction des champs nulls
      setEmptyToNulls();

      // on ne change pas les associations qui ne sont pas présentes
      // dans le formulaire
      final Nature nature = this.prelevement.getNature();
      final ConsentType consentType = this.prelevement.getConsentType();
      final Collaborateur preleveur = this.prelevement.getPreleveur();
      final Service servicePreleveur = this.prelevement.getServicePreleveur();
      final PrelevementType mode = this.prelevement.getPrelevementType();
      final ConditType conditType = this.prelevement.getConditType();
      final ConditMilieu conditMilieu = this.prelevement.getConditMilieu();

      // gestion de la non conformitée
      List<NonConformite> ncfs = null;
      if(conformeArriveeBoxOui.isChecked()){
         this.prelevement.setConformeArrivee(true);
      }else if(conformeArriveeBoxNon.isChecked()){
         this.prelevement.setConformeArrivee(false);
         ncfs = findSelectedNonConformites();
      }else{
         this.prelevement.setConformeArrivee(null);
      }
      // enregistrement de la conformité
      //		if (this.prelevement.getConformeArrivee() != null
      //				&& !this.prelevement.getConformeArrivee()) {
      //			ManagerLocator.getObjetNonConformeManager()
      //				.createUpdateOrRemoveListObjectManager(
      //						prelevement, findSelectedNonConformites(), 
      //						"Arrivee");
      //		}

      // Gestion du collaborateur
      prepareSelectedCollaborateur();

      if(null != this.maladie){
         this.maladie.getPatient();
      }

      // create de l'objet
      ManagerLocator.getPrelevementManager().createObjectWithNonConformitesManager(prelevement,
         SessionUtils.getSelectedBanques(sessionScope).get(0), nature, maladie, consentType, preleveur, servicePreleveur, mode,
         conditType, conditMilieu, selectedTransporteur, selectedCollaborateur, selectedQuantiteUnite, laboInters,
         getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(), SessionUtils.getLoggedUser(sessionScope),
         true, SessionUtils.getSystemBaseDir(), false, ncfs);

      //mécanisme de validation à implémenter
      //		if (BANQUE_ORGANE.equals(getCurrentContexte())) {
      //			if (patientFake!=null) {
      //
      //
      //
      //
      //				if (validatePatientBox) {
      //					validatePatient(patientFake,getObjectTabController().getFicheAnnotation());
      //				}
      //			}
      //		}

      // suppression du patientSip
      getObjectTabController().removePatientSip();
      // gestion de la communication des infos et de l'éventuel dossier externe
      getObjectTabController().handleExtCom((Prelevement) getObject(), getObjectTabController());

      //			// pour chaque LaboInter
      //			for (int i = 0; i < laboInters.size(); i++) {
      //				LaboInter labo = laboInters.get(i);
      //				
      //				// si c'est un nouveau labointer : création
      //				if (labo.getLaboInterId() == null) {
      //					ManagerLocator.getLaboInterManager()
      //						.createObjectManager(labo, 
      //							prelevement, 
      //							labo.getService(), 
      //							labo.getCollaborateur(), 
      //							labo.getTransporteur());
      //					
      //					this.prelevement.getLaboInters().add(labo);
      //				} else {
      //					// si le labo existe déjà : update
      //					ManagerLocator.getLaboInterManager()
      //						.updateObjectManager(labo, 
      //							prelevement, 
      //							labo.getService(), 
      //							labo.getCollaborateur(), 
      //							labo.getTransporteur());
      //				}
      //			}
   }

   @Override
   public boolean onLaterCreate(){
      try{
         // true ssi aucune exception n'est levée!
         if(super.onLaterCreate()){

            if(this.maladie != null){
               // retour vers la fiche patient au besoin
               if(getObjectTabController().getFromFichePatient()
                  && !getObjectTabController().getReferencingObjectControllers().isEmpty()){
                  getObjectTabController().setFromFichePatient(false);
                  getObjectTabController().getReferencingObjectControllers().get(0)
                     .switchToFicheStaticMode(this.maladie.getPatient());

                  PatientController.backToMe(getMainWindow(), page);
                  // ouvre le panel maladie et la liste de prelevements
                  if(getObjectTabController().getFromFicheMaladie()){
                     ((FichePatientStatic) getObjectTabController().getReferencingObjectControllers().get(0).getFicheStatic())
                        .openMaladiePanel(maladie);
                     getObjectTabController().setFromFicheMaladie(false);
                  }
               }
               // on demande la creation d'un nouveau prélèvement
               getObjectTabController().createAnotherPrelevement(prelevement);
            }
         }
         return true;

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   @Override
   public void onClick$create(){
      // valide les dates donc 		
      validateAllDateComps();
      super.onClick$create();
   }

   @Override
   public void onClick$validate(){
      // valide les dates donc 		
      validateAllDateComps();
      super.onClick$validate();
   }

   /**
    * @version 2.1
    */
   @Override
   public boolean onLaterUpdate(){

      try{
         //			updateObjectWithAnnots();
         //			
         //			// update de la liste
         //			if (getObjectTabController().getListe() != null) {			
         //				getObjectTabController().getListe()
         //										.updateObjectGridList(getObject());
         //			}
         //			
         //			// update de l'objet parent
         //			if (!getObjectTabController()
         //					.getReferencingObjectControllers().isEmpty() 
         //												&& getParentObject() != null) {
         //				for (int i = 0; i < getObjectTabController()
         //							.getReferencingObjectControllers().size(); i++) {
         //					if (getObjectTabController()
         //							.getReferencingObjectControllers()
         //												.get(i).getListe() != null) {
         //						getObjectTabController()
         //							.getReferencingObjectControllers().get(i).getListe()
         //						.updateObjectGridListFromOtherPage(getParentObject(), true);
         //					}
         //				}
         //			}
         //			
         //			// update de la liste des enfants et l'enfant en fiche
         //			getObjectTabController()
         //				.updateReferencedObjects((List<TKdataObject>) 
         //						getObjectTabController().getChildrenObjects(prelevement));
         //			
         //			// commande le passage en mode statique
         //			getObjectTabController().onEditDone(getObject());
         //			
         //			// ferme wait message
         //			Clients.clearBusy();
         if(super.onLaterUpdate()){
            getObjectTabController().showEchantillonsAfterUpdate(prelevement);
         }
         return true;

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         log.error(re);
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
         return false;
      }
   }

   @Override
   public void onClick$cancel(){
      getFichePrelevementEdit().onClick$cancel();
   }

   @Override
   public void onClick$revert(){
      getFichePrelevementEdit().onClick$revert();
   }

   @Override
   protected void updateObject(){

      Integer cascadeNonSterile = null;

      setEmptyToNulls();

      // casse la cascade stérilité si le labo n'est pas supprimé 
      // ou n'est pas le dernier labo sans echantillons derrière
      if(cascadeNonSterileFrom != null && !laboIntersToDelete.contains(cascadeNonSterileFrom)
         && !(cascadeNonSterileFrom.equals(laboInters.get(laboInters.size() - 1))
            && this.prelevement.getEchantillons().size() == 0)){
         cascadeNonSterile = cascadeNonSterileFrom.getOrdre();
         Clients.clearBusy();
         if(this.currentSterileLaboBox != null){
            this.currentSterileLaboBox.setChecked(false);
         }
         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.sterilite.cascadeLabo", new String[] {cascadeNonSterile.toString()}),
            Labels.getLabel("message.sterilite.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.NO){
            cascadeNonSterile = null;
            this.currentSterileLaboBox.setChecked(true);
            this.cascadeNonSterileFrom = null;
            return;
         }
         Clients.showBusy(Labels.getLabel("prelevement.creation.encours"));
         // cascade les labos avant update pour eviter erreur
         // validation
         ManagerLocator.getPrelevementManager().cascadeNonSterileManager(prelevement, laboInters, cascadeNonSterile, false);
      }

      // on ne change pas les associations qui ne sont pas présentes
      // dans le formulaire
      final Nature nature = this.prelevement.getNature();
      final ConsentType consentType = this.prelevement.getConsentType();
      final Collaborateur preleveur = this.prelevement.getPreleveur();
      final Service servicePreleveur = this.prelevement.getServicePreleveur();
      final PrelevementType mode = this.prelevement.getPrelevementType();
      final ConditType conditType = this.prelevement.getConditType();
      final ConditMilieu conditMilieu = this.prelevement.getConditMilieu();

      // gestion de la non conformitée*
      List<NonConformite> ncfs = new ArrayList<>();
      if(conformeArriveeBoxOui.isChecked()){
         this.prelevement.setConformeArrivee(true);
      }else if(conformeArriveeBoxNon.isChecked()){
         this.prelevement.setConformeArrivee(false);
         ncfs = findSelectedNonConformites();
      }else{
         this.prelevement.setConformeArrivee(null);
      }
      // enregistrement de la conformité
      //		ManagerLocator.getObjetNonConformeManager()
      //			.createUpdateOrRemoveListObjectManager(
      //						prelevement, ncf, 
      //						"Arrivee");

      // Gestion du collaborateur
      final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
      this.collabBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      // délétion des labos à supprimer
      for(int i = 0; i < laboIntersToDelete.size(); i++){
         final LaboInter lab = laboIntersToDelete.get(i);
         ManagerLocator.getLaboInterManager().removeObjectManager(lab);
      }

      // update de l'objet
      ManagerLocator.getPrelevementManager().updateObjectWithNonConformitesManager(prelevement, prelevement.getBanque(), nature,
         maladie, consentType, preleveur, servicePreleveur, mode, conditType, conditMilieu, selectedTransporteur,
         selectedCollaborateur, selectedQuantiteUnite, laboInters,
         getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
         getObjectTabController().getFicheAnnotation().getValeursToDelete(), SessionUtils.getLoggedUser(sessionScope),
         cascadeNonSterile, true, SessionUtils.getSystemBaseDir(), false, ncfs);

      //			// pour chaque LaboInter
      //			for (int i = 0; i < laboInters.size(); i++) {
      //				LaboInter labo = laboInters.get(i);
      //				
      //				// si c'est un nouveau labointer : création
      //				if (labo.getLaboInterId() == null) {
      //					ManagerLocator.getLaboInterManager()
      //						.createObjectManager(labo, 
      //							prelevement, 
      //							labo.getService(), 
      //							labo.getCollaborateur(), 
      //							labo.getTransporteur());
      //					this.prelevement.getLaboInters().add(labo);
      //				} else {
      //					// si le labo existe déjà : update
      //					ManagerLocator.getLaboInterManager()
      //						.updateObjectManager(labo, 
      //							prelevement, 
      //							labo.getService(), 
      //							labo.getCollaborateur(), 
      //							labo.getTransporteur());
      //				}
      //			}
      getObjectTabController().handleExtCom((Prelevement) getObject(), getObjectTabController());
   }

   //	/**
   //	 * Processing echoEvent (no messagebox).
   //	 * @see onClick$validate()
   //	 */
   //	public void onLaterValidate() {
   //		// s'il n'y a pas d'erreurs lors de l'update
   //		List<String> errorMsg = updateObject();
   //		if (errorMsg != null && errorMsg.size() == 0) {
   //		
   //			log.debug("fiche: obj modifie: " + this.prelevement.toString());
   //		
   //			if (getListePrelevement() != null) {		
   //				// update du prélèvement dans la liste
   //				getListePrelevement().updateObjectGridList(this.prelevement);
   //			}
   //			
   //			if (getMainWindow().isFullfilledComponent(
   //					"patientPanel", "winPatient")) {
   //				if (getFichePrelevementEdit().getListePatient() != null
   //											&& this.maladie != null) {	
   //					// update du patient dans la liste
   //					getFichePrelevementEdit().getListePatient()
   //						.updateObjectGridListFromOtherPage(this.maladie
   //													.getPatient());
   //				}
   //			}
   //			
   //			// l'affichage revient sur la fiche prlvt, en mode static
   //			// avec le prlvt
   //			postDetachEditEvent();
   //			getPrelevementController().getFicheStatic()
   //				.setPrelevement(this.prelevement);
   //			
   //		} else {
   //			log.debug("Modification prelevement non effectuée à "
   //					+ "cause d'erreurs");
   //		}
   //		Clients.showBusy(null, false);
   //	}

   @Override
   protected void setEmptyToNulls(){
      // calendarboxes
      // prelevement.setDateDepart(dateDepartCalBox.getValue());
      // prelevement.setDateArrivee(dateArriveeCalBox.getValue());
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){
      congDepartBox.setFocus(true);
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(quantiteBoxLabo);
   }

   /*************************************************************************/
   /*****************  NEXT - PREVIOUS **************************************/
   /*************************************************************************/

   /**
    * Revient sur la page FichePrelevement dans l'état où elle
    * était avant d'arriver sur la page FicheLaboInter.
    */
   public void onClick$previous(){
      getObjectTabController().switchFromLaboToPrelevement();
   }

   /**
    * Passe à la fiche des échantillons.
    */
   public void onClick$next(){
      validateAllDateComps();

      getObjectTabController().getFicheAnnotation().validateComponents();
      // prepare les listes de valeurs à manipuler
      //getObjectTabController()
      //	.getFicheAnnotation()
      //	.populateValeursActionLists(prelevement
      //				.getPrelevementId() == null, false);

      //Clients.showBusy(Labels.getLabel("general.wait"), true);
      Clients.showBusy(Labels.getLabel("general.wait"));
      Events.echoEvent("onLaterNextStep", self, null);
   }

   /**
    * Raccourci quand clique 'return'.
    */
   @Override
   public void onOK(){
      Events.postEvent(new Event("onClick", next));
   }

   /**
    * Processing echoEvent.
    */
   public void onLaterNextStep(){
      // Gestion du collaborateur
      final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
      this.collabBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      // on recopie les sélections dans le prlvt		
      this.prelevement.setTransporteur(selectedTransporteur);
      this.prelevement.setOperateur(selectedCollaborateur);
      this.prelevement.setQuantiteUnite(selectedQuantiteUnite);

      // gestion de la non conformitée
      if(conformeArriveeBoxOui.isChecked()){
         this.prelevement.setConformeArrivee(true);
      }else if(conformeArriveeBoxNon.isChecked()){
         this.prelevement.setConformeArrivee(false);
      }else{
         this.prelevement.setConformeArrivee(null);
      }

      // si nous sommes dans une action d'édition, on
      // appelle la page FicheMultiEchantillons en mode edit
      if(this.prelevement.getPrelevementId() != null){
         getObjectTabController().switchToMultiEchantillonsEditMode(this.prelevement, this.laboInters, this.laboIntersToDelete);
      }else{
         // si nous sommes dans une action de création, on
         // appelle la page FicheMultiEchantillons en mode create
         getObjectTabController().switchToMultiEchantillonsCreateMode(this.prelevement, this.maladie, this.laboInters);
      }

      Clients.clearBusy();
   }

   /*************************************************************************/
   /************************** ASSOCIATIONS *********************************/
   /*************************************************************************/
   /**
    * Méthode appelée lorsque l'utilisateur clique sur le lien
    * operateurAideSaisieEchan. Cette méthode va créer une nouvelle
    * fenêtre contenant l'aide pour la sélection d'un collaborateur.
    */
   public void onClick$operateurAideSaisieEchan(){
      // on récupère le collaborateur actuellement sélectionné
      // pour l'afficher dans la modale
      final List<Object> old = new ArrayList<>();
      if(selectedCollaborateur != null){
         old.add(selectedCollaborateur);
      }
      // on va appliquer à cette fenêtre un filtre sur les services de
      // la banque
      final List<Object> list = new ArrayList<>();
      final Set<Service> services = ManagerLocator.getBanqueManager().getServicesStockageManager(prelevement.getBanque());
      final Iterator<Service> it = services.iterator();
      while(it.hasNext()){
         list.add(it.next());
      }

      // ouvre la modale
      openCollaborationsWindow(page, "general.recherche", "select", null, "Collaborateur", list, Path.getPath(self), old);
   }

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand
    * l'utilisateur sélectionne un collaborateur.
    * @param e Event contenant le collaborateur sélectionné.
    */
   public void onGetObjectFromSelection(final Event e){

      // les collaborateurs peuvent être modifiés dans la fenêtre
      // d'aide => maj de ceux-ci
      nomsAndPrenoms = new ArrayList<>();
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
      }
      if(this.prelevement.getOperateur() != null && !collaborateurs.contains(this.prelevement.getOperateur())){
         collaborateurs.add(this.prelevement.getOperateur());
         nomsAndPrenoms.add(this.prelevement.getOperateur().getNomAndPrenom());
      }
      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      // si un collaborateur a été sélectionné
      if(e.getData() != null){
         final Collaborateur coll = (Collaborateur) e.getData();
         if(nomsAndPrenoms.contains(coll.getNomAndPrenom())){
            final int ind = nomsAndPrenoms.indexOf(coll.getNomAndPrenom());
            selectedCollaborateur = collaborateurs.get(ind);
            collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
         }
      }
   }

   /*************************************************************************/
   /************************** STERILITE ************************************/
   /*************************************************************************/
   /**
    * Méthode qui verifie si il faut appliquer la cascade de non sterilite
    * a partir du labo courant, si update seulement.
    * @param event Blur sur la box.
    */
   public void onCheck$sterileLaboBox(final Event event){
      List<LaboInter> labos;
      if(this.prelevement.getPrelevementId() != null){
         labos = ManagerLocator.getLaboInterManager().findByPrelevementWithOrder(prelevement);
      }else{
         labos = getLaboInters();
      }

      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(((Checkbox) ((ForwardEvent) event).getOrigin().getTarget()).isChecked()){

         final Prelevement prel = new Prelevement();
         prel.setSterile(prelevement.getSterile());
         prel.setLaboInters(new HashSet<>(getLaboInters()));
         lab.setPrelevement(prel);
         lab.setSterile(true);
         Errors errs = null;
         errs = ManagerLocator.getLaboInterValidator().checkSteriliteAntecedence(lab);

         if(errs != null && errs.hasErrors()){
            //throw new WrongValueException(
            //sterileBox, handleErrors(errs, field));
            final List<Errors> errors = new ArrayList<>();
            errors.add(errs);
            Messagebox.show(handleExceptionMessage(new ValidationException(errors)), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }else{ // cascade si update
         // verification de la valeur de sterilite enregistre en base
         lab.setSterile(false);
         LaboInter laboInDb = null;
         for(int i = 0; i < labos.size(); i++){
            if(labos.get(i).equals(lab)){
               laboInDb = labos.get(i);
               break;
            }
         }
         // applique la cascade depuis le labo le plus inférieur
         if(laboInDb != null && laboInDb.getSterile() && !lab.getSterile()){
            if(cascadeNonSterileFrom == null || lab.getOrdre() > cascadeNonSterileFrom.getOrdre()){
               this.cascadeNonSterileFrom = lab;
               Component target = ((ForwardEvent) event).getOrigin().getTarget();
               try{
                  while(!(target instanceof Checkbox)){
                     target = target.getParent();
                  }
                  currentSterileLaboBox = (Checkbox) target;
               }catch(final NullPointerException e){
                  log.error(e);
               }
            }
         }else if(lab.equals(cascadeNonSterileFrom)){
            this.cascadeNonSterileFrom = null;
         }
      }
   }

   /*************************************************************************/
   /************************** LABO *****************************************/
   /*************************************************************************/
   /**
    * Méthode appelée lors du clic sur le bouton addLabo. Elle
    * va créer un nouveau LaboInter et l'ajouter à la liste.
    */
   public void onClick$addLabo(){

      // le nouveau labo prend l'ordre maximum
      final LaboInter newLab = new LaboInter();
      ++ordreMax;
      newLab.setOrdre(ordreMax);
      newLab.setPrelevement(this.prelevement);

      if(!this.prelevement.getSterile()){
         newLab.setSterile(false);
      }

      Calendar initDate = null;
      if(!laboInters.isEmpty()){
         final LaboInter last = laboInters.get(laboInters.size() - 1);
         if(last.getDateDepart() != null){
            initDate = ObjectTypesFormatters.getDateWithoutHoursAndMins(last.getDateDepart());
         }else if(last.getDateArrivee() != null){
            initDate = ObjectTypesFormatters.getDateWithoutHoursAndMins(last.getDateArrivee());
         }
      }
      if(initDate == null){
         if(prelevement.getDateDepart() != null){
            initDate = ObjectTypesFormatters.getDateWithoutHoursAndMins(prelevement.getDateDepart());
         }else if(prelevement.getDatePrelevement() != null){
            initDate = ObjectTypesFormatters.getDateWithoutHoursAndMins(prelevement.getDatePrelevement());
         }
      }
      newLab.setDateArrivee(initDate);
      newLab.setDateDepart(initDate);

      laboInters.add(newLab);

      // maj de la liste des labos
      final ListModel<LaboInter> list = new ListModelList<>(laboInters);
      laboIntersGrid.setModel(list);
   }

   /**
    * Cette méthode va supprimer un LaboInter de la liste.
    * @param event Clic sur une image deleteLabo.
    */
   public void onClick$deleteLabo(final Event event){
      // on demande confirmation à l'utilisateur
      // de supprimer de labo
      if(Messagebox.show(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel("message.deletion.laboInter")}),
         Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
         // on récupère le labo que l'utilisateur veut
         // suppimer
         final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);
         // on enlève le bao de la liste et on la met à jour
         laboInters.remove(lab);

         final ListModel<LaboInter> list = new ListModelList<>(laboInters);
         laboIntersGrid.setModel(list);

         // si le labo existait dans la BDD on l'ajoute à la
         // liste des labos à supprimer (il ne sera délété que
         // lors de la sauvegarde finale)
         if(lab.getLaboInterId() != null){
            laboIntersToDelete.add(lab);
         }
      }
   }

   /**
    * Cette méthode va retourner la liste pour laquelle un event vient de
    * se produire.
    * @param event Event sur une liste.
    * @return Une ListBox (de services, collabs ou transporteurs).
    */
   public Listbox getListBox(final ForwardEvent event){
      Component target = event.getOrigin().getTarget();
      try{
         while(!(target instanceof Listbox)){
            target = target.getParent();
         }
         return (Listbox) target;
      }catch(final NullPointerException e){
         return null;
      }
   }

   /**
    * Cette méthode va retourner le calendarBox pour lequelle un event vient de
    * se produire.
    * @param event Event sur un calendarBox.
    * @return un CalendarBox
    */
   public CalendarBox getCalendarBox(final ForwardEvent event){
      Component target = event.getOrigin().getTarget();
      try{
         while(!(target instanceof CalendarBox)){
            target = target.getParent();
         }
         return (CalendarBox) target;
      }catch(final NullPointerException e){
         return null;
      }
   }

   /**
    * Méthode initialisant le transporteur sélectionné pour le labo courant.
    * @param event Init de la liste transporteursBoxEachLabo.
    */
   public void onInitRender$transporteursBoxEachLabo(final Event event){
      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on récupère la liste de la ligne courante
      final Listbox tmp = getListBox((ForwardEvent) event);

      // init
      tmp.setSelectedIndex(transporteurs.indexOf(lab.getTransporteur()));
   }

   /**
    * Filtre les services par établissement.
    * @param event Event : seléction sur la liste etabsBoxEachLabo.
    * @throws Exception
    */
   public void onSelect$etabsBoxEachLabo(final Event event) throws Exception{

      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      // on récupère la liste de la ligne courante
      final Listbox listEtab = getListBox((ForwardEvent) event);

      // on extrait l'établissement qui vient d'être sélectionné
      final Etablissement etab = (Etablissement) listEtab.getSelectedItem().getValue();

      // on filtre les services en fonction de l'étab
      final List<Service> services = ManagerLocator.getEtablissementManager().getActiveServicesWithOrderManager(etab);
      services.add(0, null);

      // on sort les composants contenus dans la ligne suivante 
      // du formulaire
      final List<Component> comps = listEtab.getParent().getNextSibling().getChildren();

      // on recherche le composant de type Listbox qui gère les services
      int i = 0;
      while(i < comps.size() && !comps.get(i).getClass().getSimpleName().equals("Listbox")){
         ++i;
      }

      if(comps.get(i).getClass().getSimpleName().equals("Listbox")){
         final Listbox listService = (Listbox) comps.get(i);

         // service actuellement sélectionné
         Service serv = lab.getService();

         // MAJ du model de la liste
         final BindingListModel<Service> list = new BindingListModelList<>(services, false);
         listService.setModel(list);

         // si le service qui était sélectionné n'est plus dans la liste
         // filtrée
         if(!services.contains(serv)){
            serv = null;
         }
         lab.setService(serv);

         listService.setSelectedIndex(services.indexOf(serv));
      }
   }

   /**
    * Filtre les collaborateurs par le service sélectionné.
    * @param event Event : seléction sur la liste servicesBoxEachLabo.
    * @throws Exception
    */
   public void onSelect$servicesBoxEachLabo(final Event event){

      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      // on récupère la liste de la ligne courante
      final Listbox listServ = getListBox((ForwardEvent) event);

      // on extrait le service qui vient d'être sélectionné
      final Service serv = (Service) listServ.getSelectedItem().getValue();

      // on filtre les collaborateurs en fonction du service
      final List<Collaborateur> collabs = ManagerLocator.getServiceManager().getActiveCollaborateursWithOrderManager(serv);
      collabs.add(0, null);

      // on sort les composants contenus dans la ligne suivante 
      // du formulaire
      final List<Component> comps = listServ.getParent().getNextSibling().getChildren();

      // on recherche le composant de type Listbox qui gère les collabs
      int i = 0;
      while(i < comps.size() && !comps.get(i).getClass().getSimpleName().equals("Listbox")){
         ++i;
      }

      if(comps.get(i).getClass().getSimpleName().equals("Listbox")){
         final Listbox listCollabs = (Listbox) comps.get(i);

         // collaborateur actuellement sélectionné
         Collaborateur collab = lab.getCollaborateur();

         // MAJ du model de la liste
         final BindingListModel<Collaborateur> list = new BindingListModelList<>(collabs, false);
         listCollabs.setModel(list);

         // si le collab qui était sélectionné n'est plus dans la liste
         // filtrée
         if(!collabs.contains(collab)){
            collab = null;
         }
         lab.setCollaborateur(collab);

         listCollabs.setSelectedIndex(collabs.indexOf(collab));
      }
   }

   /**
    * Méthode qui récupère le collab sélectionné pour le labo courant
    * et qui met à jour cet objet.
    * @param event Select sur la liste collaborateursBoxEachLabo.
    */
   public void onSelect$collaborateursBoxEachLabo(final Event event){
      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on récupère la liste de la ligne courante
      final Listbox tmp = getListBox((ForwardEvent) event);

      // maj
      lab.setCollaborateur((Collaborateur) tmp.getSelectedItem().getValue());
   }

   /**
    * Méthode qui récupère le transporteur sélectionné pour le labo courant
    * et qui met à jour cet objet.
    * @param event Select sur la liste transporteursBoxEachLabo.
    */
   public void onSelect$transporteursBoxEachLabo(final Event event){
      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on récupère la liste de la ligne courante
      final Listbox tmp = getListBox((ForwardEvent) event);

      // maj
      lab.setTransporteur((Transporteur) tmp.getSelectedItem().getValue());
   }

   /**
    * Méthode appelée lors de la sélection d'une température dans la
    * liste temperatureListBoxLabo : mets à jour la valeur dans
    * temperatureBoxLabo.
    * @param event Select sur la liste temperatureListBoxLabo.
    */
   public void onSelect$temperatureListBoxLabo(final Event event){

      if(selectedTemperature != null){
         prelevement.setTransportTemp(selectedTemperature.getTemperature());
      }else{
         prelevement.setTransportTemp(null);
      }
   }

   /**
    * Méthode appelée lors de la sélection d'une température dans la
    * liste temperatureListBoxSite.
    * @param event Select sur la liste temperatureListBoxSite.
    */
   public void onSelect$temperatureListBoxSite(final Event event){
      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on récupère la liste de la ligne courante
      final Listbox tmp = getListBox((ForwardEvent) event);

      // maj
      final Temperature temp = (Temperature) tmp.getSelectedItem().getValue();
      if(temp != null){
         lab.setConservTemp(temp.getTemperature());
      }else{
         lab.setConservTemp(null);
      }
   }

   /**
    * Méthode appelée lors de la sélection d'une température dans la
    * liste temperatureListBoxSite2.
    * @param event Select sur la liste temperatureListBoxSite2.
    */
   public void onSelect$temperatureListBoxSite2(final Event event){
      // on récupère le labo pour la ligne courante
      final LaboInter lab = (LaboInter) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      // on récupère la liste de la ligne courante
      final Listbox tmp = getListBox((ForwardEvent) event);

      // maj
      final Temperature temp = (Temperature) tmp.getSelectedItem().getValue();
      if(temp != null){
         lab.setTransportTemp(temp.getTemperature());
      }else{
         lab.setTransportTemp(null);
      }
   }

   public void onCheck$conformeArriveeBoxOui(){
      if(conformeArriveeBoxOui.isChecked()){
         conformeArriveeBoxNon.setChecked(false);
         conformeArriveeBox.setVisible(false);
      }
   }

   public void onCheck$conformeArriveeBoxNon(){
      if(conformeArriveeBoxNon.isChecked()){
         conformeArriveeBoxOui.setChecked(false);
         conformeArriveeBox.setVisible(true);
      }else{
         conformeArriveeBox.setVisible(false);
      }
   }

   /**
    * Retourne les non conformites sélectionnées.
    * @return
    */
   public List<NonConformite> findSelectedNonConformites(){
      final List<NonConformite> ncf = new ArrayList<>();
      final Iterator<Listitem> its = nonConformitesBox.getSelectedItems().iterator();
      while(its.hasNext()){
         ncf.add(nonConformites.get(nonConformitesBox.getItems().indexOf(its.next())));
      }
      return ncf;
   }

   /*************************************************************************/
   /************************** VALIDATION ***********************************/
   /*************************************************************************/

   //	/**
   //	 * Gere la validationException pour isoler les erreurs liees aux
   //	 * LaboInter et les afficher dans le laboInterValidationLabel.
   //	 * @param ve ValidationException
   //	 * @return message d'erreur eventuel lie à une autre erreur de validation.
   //	 */
   //	public String handleValidationException(ValidationException ve) {
   //		String errorMsg = null;
   //		List<Errors> errors = ve.getErrors();
   //		Errors errs;
   //		FieldError err;
   //		String errMessage = "";
   //		Iterator<FieldError> fieldErrorIt;
   //		for (int i = 0; i < errors.size(); i++) {
   //			errs = errors.get(i);
   //			if (errs.getObjectName()
   //					.equals("fr.aphp.tumorotek.model."
   //									+ "coeur.prelevement.LaboInter")) {
   //				fieldErrorIt = errs.getFieldErrors().iterator();
   //			
   //				while (fieldErrorIt.hasNext()) {
   //					err = fieldErrorIt.next();
   //					if (errMessage.equals("")) {
   //						errMessage = Labels.getLabel(err.getCode());
   //					} else {
   //						errMessage = errMessage  + ";"
   //										+ Labels.getLabel(err.getCode());
   //					}
   //				}
   //			}
   //		}
   //		if (!errMessage.equals("")) {
   //			Clients.showBusy(null, false);
   ////			showLaboInterValidationError(
   ////					Labels.getLabel("laboInter.verifier.coherence")
   ////						+ "\n" + errMessage);
   //			errorMsg = Labels.getLabel("laboInter.verifier.coherence")
   //			+ "\n" + errMessage;
   //		} else {
   //			errorMsg = "- Erreur validation prelevement";
   //		}
   //		return errorMsg;
   //	}

   //	/**
   //	 * Remplissage automatique avec la date (sans heures) de prelevement 
   //	 * si champ vide, déclenche validation sinon.
   //	 */
   //	public void onBlur$dateDepartCalBox() {
   //		if (!dateDepartCalBox.isHasChanged() 
   //				&& dateDepartCalBox.getValue() == null) {
   //			dateDepartCalBox.setValue(ObjectTypesFormatters
   //				.getDateWithoutHoursAndMins(prelevement.getDatePrelevement()));
   //		} else {		
   //			dateDepartCalBox.clearErrorMessage(dateDepartCalBox.getValue());
   //			validateCoherenceDate(dateDepartCalBox, 
   //												dateDepartCalBox.getValue());
   //			dateArriveeCalBox.clearErrorMessage(dateArriveeCalBox.getValue());
   //			validateCoherenceDate(dateArriveeCalBox, 
   //												dateArriveeCalBox.getValue());
   //		}
   //		dateDepartCalBox.setHasChanged(true);
   //	}

   //	/**
   //	 * Applique la validation sur la date et les dates dependantes.
   //	 */
   //	public void onBlur$dateDepartInterBox(Event event) {
   //		this.currentLabo = (LaboInter) getBindingData((ForwardEvent) event);
   //		dateDepartInterBox.clearErrorMessage(true);
   //		validateCoherenceDate(dateDepartInterBox, 
   //												dateDepartInterBox.getValue());
   //		dateArriveeInterBox.clearErrorMessage(true);
   //		validateCoherenceDate(dateArriveeInterBox, 
   //												dateArriveeInterBox.getValue());
   //	}

   public void onBlur$dateArriveeCalBox(){
      if(!dateArriveeCalBox.isHasChanged() && dateArriveeCalBox.getValue() == null){
         dateArriveeCalBox.setValue(ObjectTypesFormatters.getDateWithoutHoursAndMins(prelevement.getDatePrelevement()));
         this.prelevement.setDateArrivee(ObjectTypesFormatters.getDateWithoutHoursAndMins(prelevement.getDatePrelevement()));
      }else{
         dateArriveeCalBox.clearErrorMessage(dateArriveeCalBox.getValue());
         validateCoherenceDate(dateArriveeCalBox, dateArriveeCalBox.getValue());
         dateDepartCalBox.clearErrorMessage(dateDepartCalBox.getValue());
         validateCoherenceDate(dateDepartCalBox, dateDepartCalBox.getValue());
      }
      dateArriveeCalBox.setHasChanged(true);
   }

   public void onBlur$dateDepartCalBox(){

      if(!dateDepartCalBox.isHasChanged() && dateDepartCalBox.getValue() == null){
         dateDepartCalBox.setValue(ObjectTypesFormatters.getDateWithoutHoursAndMins(prelevement.getDatePrelevement()));
         this.prelevement.setDateDepart(ObjectTypesFormatters.getDateWithoutHoursAndMins(prelevement.getDatePrelevement()));
      }else{
         dateDepartCalBox.clearErrorMessage(dateDepartCalBox.getValue());
         validateCoherenceDate(dateDepartCalBox, dateDepartCalBox.getValue());
         dateArriveeCalBox.clearErrorMessage(dateArriveeCalBox.getValue());
         validateCoherenceDate(dateArriveeCalBox, dateArriveeCalBox.getValue());
      }
      dateDepartCalBox.setHasChanged(true);
   }

   /**
    * Lance la validation de toutes les dates de prelevement pour
    * s'assurer de la cohérencence entre les pages.
    */
   public void validateAllDateComps(){
      dateDepartCalBox.clearErrorMessage(dateDepartCalBox.getValue());
      validateCoherenceDate(dateDepartCalBox, dateDepartCalBox.getValue());
      dateArriveeCalBox.clearErrorMessage(dateArriveeCalBox.getValue());
      validateCoherenceDate(dateArriveeCalBox, dateArriveeCalBox.getValue());
   }

   //	/**
   //	 * Applique la validation sur la date et les dates dependantes.
   //	 */
   //	public void onBlur$dateArriveeInterBox(Event event) {
   //		this.currentLabo = (LaboInter) getBindingData((ForwardEvent) event);
   //		dateArriveeInterBox.clearErrorMessage(true);
   //		validateCoherenceDate(dateArriveeInterBox, 
   //												dateArriveeInterBox.getValue());
   //		dateDepartInterBox.clearErrorMessage(true);
   //		validateCoherenceDate(dateDepartInterBox, 
   //												dateDepartInterBox.getValue());
   //	}	
   //	/**
   //	 * Declenche l'affichage du message d'erreur de validation
   //	 * des labos inters.
   //	 * @param message, si null, n'affiche pas
   //	 */
   //	private void showLaboInterValidationError(String message) {
   //		if (message != null) {
   //			laboInterValidationLabel.setValue(message);
   //			laboInterValidationLabel.setVisible(true);
   //			laboIntersGrid.setStyle("border-style : solid;" 
   //									+ "border-width : 1px;"
   //									+ "border-color : red;");
   //		} else {
   //			laboInterValidationLabel.setValue(null);
   //			laboInterValidationLabel.setVisible(false);
   //			laboIntersGrid.setStyle("");
   //		}
   //	}

   @Override
   protected void validateCoherenceDate(final Component comp, final Object value){
      Errors errs = null;
      String field = "";

      if(value == null || value.equals("")){
         // la contrainte est retiree
         //((Datebox) comp).setConstraint("");

         if(comp.getId().equals("dateDepartCalBox")){
            ((CalendarBox) comp).clearErrorMessage(dateDepartCalBox.getValue());
            ((CalendarBox) comp).setValue(null);
            this.prelevement.setDateDepart(null);
         }else if(comp.getId().equals("dateArriveeCalBox")){
            ((CalendarBox) comp).clearErrorMessage(dateArriveeCalBox.getValue());
            ((CalendarBox) comp).setValue(null);
            this.prelevement.setDateArrivee(null);
         }
      }else{
         this.prelevement.setMaladie(this.maladie);
         this.prelevement.setLaboInters(new HashSet<>(laboInters));
         // date depart
         if(comp.getId().equals("dateDepartCalBox")){
            field = "dateDepart";
            this.prelevement.setDateDepart((Calendar) value);
            errs = ManagerLocator.getPrelevementValidator().checkDateDepartCoherence(this.prelevement);
         }

         // date arrivee
         if(comp.getId().equals("dateArriveeCalBox")){
            field = "dateArrivee";
            this.prelevement.setDateArrivee((Calendar) value);
            errs = ManagerLocator.getPrelevementValidator().checkDateArriveeCoherence(this.prelevement);
         }

         if(errs != null && errs.hasErrors()){
            Clients.scrollIntoView(comp);
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   public List<LaboInter> getLaboInters(){
      return laboInters;
   }

   public void setLaboInters(final List<LaboInter> labos){
      this.laboInters = labos;
   }

   public String getValeurQuantite(){
      return valeurQuantite;
   }

   public void setValeurQuantite(final String valeur){
      this.valeurQuantite = valeur;
   }

   public List<Transporteur> getTransporteurs(){
      return transporteurs;
   }

   public void setTransporteurs(final List<Transporteur> transps){
      this.transporteurs = transps;
   }

   public Transporteur getSelectedTransporteur(){
      return selectedTransporteur;
   }

   public void setSelectedTransporteur(final Transporteur selected){
      this.selectedTransporteur = selected;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final List<Collaborateur> collabs){
      this.collaborateurs = collabs;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selected){
      this.selectedCollaborateur = selected;
   }

   public List<String> getNomsAndPrenoms(){
      return nomsAndPrenoms;
   }

   public void setNomsAndPrenoms(final List<String> nAndPs){
      this.nomsAndPrenoms = nAndPs;
   }

   public Unite getSelectedQuantiteUnite(){
      return selectedQuantiteUnite;
   }

   public void setSelectedQuantiteUnite(final Unite selected){
      this.selectedQuantiteUnite = selected;
   }

   public Integer getOrdreMax(){
      return ordreMax;
   }

   public void setOrdreMax(final Integer max){
      this.ordreMax = max;
   }

   public List<Service> getAllServices(){
      return allServices;
   }

   public void setAllServices(final List<Service> all){
      this.allServices = all;
   }

   public List<Collaborateur> getAllCollaborateurs(){
      return allCollaborateurs;
   }

   public void setAllCollaborateurs(final List<Collaborateur> all){
      this.allCollaborateurs = all;
   }

   public List<LaboInter> getLaboIntersToDelete(){
      return laboIntersToDelete;
   }

   public void setLaboIntersToDelete(final List<LaboInter> toDelete){
      this.laboIntersToDelete = toDelete;
   }

   public List<Unite> getQuantiteUnites(){
      return quantiteUnites;
   }

   public void setQuantiteUnites(final List<Unite> unites){
      this.quantiteUnites = unites;
   }

   public List<Etablissement> getAllEtablissements(){
      return allEtablissements;
   }

   public void setAllEtablissements(final List<Etablissement> e){
      this.allEtablissements = e;
   }

   public List<Temperature> getTemperatures(){
      return temperatures;
   }

   public void setTemperatures(final List<Temperature> temps){
      this.temperatures = temps;
   }

   public Temperature getSelectedTemperature(){
      return selectedTemperature;
   }

   public void setSelectedTemperature(final Temperature selected){
      this.selectedTemperature = selected;
   }

   /**
    * Assigne la valeur au collaborateur qui sera utilisé 
    * pour enregistrer le prelevement.
    */
   private void prepareSelectedCollaborateur(){
      final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
      this.collabBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }
   }

   /**
    * Prepare le prelevement en lui assignant les references vers
    *  ses objets associés afin d'etre enregistré au niveau de la 
    *  page MultiEchantillon.
    * @return
    */
   public Prelevement getPrelevementPrepared(){

      final Prelevement prepared = this.prelevement.clone();

      prepared.setMaladie(maladie);
      prepareSelectedCollaborateur();
      prepared.setTransporteur(selectedTransporteur);
      prepared.setQuantiteUnite(selectedQuantiteUnite);

      return prepared;
   }

   public List<LaboInter> getOldLaboInters(){
      return oldLaboInters;
   }

   public void setOldLaboInters(final List<LaboInter> oldLabos){
      this.oldLaboInters = oldLabos;
   }

   public void onCheck$congDepartBox(){
      if(congDepartBox.isChecked()){
         congArriveeBox.setChecked(false);
         prelevement.setCongArrivee(false);
         uncheckSiteCongelations();
      }
   }

   public void onCheck$congArriveeBox(){
      if(congArriveeBox.isChecked()){
         congDepartBox.setChecked(false);
         prelevement.setCongDepart(false);
         uncheckSiteCongelations();
      }
   }

   public void onCheck$congSiteBox(final Event event){
      if(((Checkbox) ((ForwardEvent) event).getOrigin().getTarget()).isChecked()){
         congDepartBox.setChecked(false);
         prelevement.setCongDepart(false);
         congArriveeBox.setChecked(false);
         prelevement.setCongArrivee(false);
         uncheckSiteCongelations();
         ((Checkbox) ((ForwardEvent) event).getOrigin().getTarget()).setChecked(true);
      }else{
         ((Checkbox) ((ForwardEvent) event).getOrigin().getTarget()).setChecked(false);
      }
   }

   /**
    * Uncheck tous les radios contenus dans la grid des 
    * labo inters.
    * @param checked
    */
   private void uncheckSiteCongelations(){
      final List<Component> rows = laboIntersGrid.getRows().getChildren();
      Checkbox check;
      for(int i = 0; i < rows.size(); i++){
         check = (Checkbox) rows.get(i).getFirstChild().getFirstChild().getChildren().get(7).getLastChild();
         if(check.isChecked()){
            check.setChecked(false);
         }
      }
   }

   public List<NonConformite> getNonConformites(){
      return nonConformites;
   }

   public void setNonConformites(final List<NonConformite> nConformites){
      this.nonConformites = nConformites;
   }

   public NonConformite getSelectedNonConformite(){
      return selectedNonConformite;
   }

   public void setSelectedNonConformite(final NonConformite sNonConformite){
      this.selectedNonConformite = sNonConformite;
   }

   public Set<Listitem> getSelectedNonConformitesItem(){
      return selectedNonConformitesItem;
   }

   public void setSelectedNonConformitesItem(final Set<Listitem> sItem){
      this.selectedNonConformitesItem = sItem;
   }
}
