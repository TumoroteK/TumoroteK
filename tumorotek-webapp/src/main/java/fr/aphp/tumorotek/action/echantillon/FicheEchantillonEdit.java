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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.validation.Errors;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Group;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

import fr.aphp.tumorotek.action.CustomSimpleListModel;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.action.code.CodeAssigneEditableGrid;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstFilename;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.prelevement.PrelevementController;
import fr.aphp.tumorotek.action.utils.PrelevementUtils;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheEchantillonEdit extends AbstractFicheEditController
{

   // private Log log = LogFactory.getLog(FicheEchantillonEdit.class);

   private static final long serialVersionUID = 2661016754151830781L;

   protected Textbox codePrefixeEchan;
   protected Label operateurAideSaisieEchan;
   protected Textbox codeBoxEchan;
   protected Label quantiteBoxEchan;
   protected Decimalbox quantiteInitBoxEchan;
   protected Combobox collabBox;
   protected Listbox lateralitesBox;
   protected Listbox quaniteUnitesBoxEchan;
   protected Listbox prepasBox;
   protected Listbox qualitesBoxEchan;
   protected CalendarBox dateStockCalBox;
   protected Intbox heureBox;
   protected Intbox minBox;
   protected Checkbox tumoraleBox;
   protected Checkbox sterileBox;
   protected Checkbox validBox;
   protected Textbox crAnapathNomBox;
   //private Image addCrFile;
   protected Image deleteCrFile;
   protected Listbox typesBoxEchan;
   protected Group groupInfosCompEchan;

   protected Checkbox conformeTraitementBoxOui;
   protected Checkbox conformeTraitementBoxNon;
   protected Div conformeTraitementBox;
   protected Checkbox conformeCessionBoxOui;
   protected Checkbox conformeCessionBoxNon;
   protected Div conformeCessionBox;
   protected Listbox nonConformitesTraitementBox;
   protected Listbox nonConformitesCessionBox;

   // Objets Principaux.
   private Echantillon echantillon = new Echantillon();
   private String selectedLateralite = "";

   // Associations.
   private List<EchantillonType> types = new ArrayList<>();
   private EchantillonType selectedType;
   private List<Collaborateur> collaborateurs = new ArrayList<>();
   private Collaborateur selectedCollaborateur;
   private List<String> nomsAndPrenoms = new ArrayList<>();
   private List<EchanQualite> qualites = new ArrayList<>();
   private EchanQualite selectedQualite;
   private List<Unite> quantiteUnites = new ArrayList<>();
   private Unite selectedQuantiteUnite;
   private List<ModePrepa> prepas = new ArrayList<>();
   private ModePrepa selectedPrepa;
   private Fichier crAnapath = null;
   private InputStream anapathStream = null;
   private List<NonConformite> nonConformitesTraitement = new ArrayList<>();
   private NonConformite selectedNonConformiteTraitement;
   private List<NonConformite> nonConformitesCession = new ArrayList<>();
   private NonConformite selectedNonConformiteCession;
   //	private Set<Listitem> selectedNonConformitesTraitementItem = 
   //		new HashSet<Listitem>();
   //	private Set<Listitem> selectedNonConformitesCessionItem = 
   //		new HashSet<Listitem>();

   private List<LaboInter> laboInters = null;
   private List<LaboInter> laboIntersToDelete = null;

   // Variables formulaire.
   private String valeurQuantite = "";
   private String valeurQuantiteRestante = "";
   private Integer heureDelai = null;
   private Integer minDelai = null;
   protected String codePrefixe = "";
   //	private String codeSuffixe = "";
   private String emplacementAdrl = "";

   private List<CodeAssigne> codesToCreateOrEdit = new ArrayList<>();

   private List<CodeAssigne> codesAssignesToDelete = new ArrayList<>();

   /**
    *  Gestion des contraintes sur les quantités et volumes.
    *  Variables conservant les valeurs saisies dans les champs.
    */
   private Float quantite;
   private Float quantiteInit;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setWaitLabel("echantillon.creation.encours");

      getCodesOrganeController().setIsOrg(true);
      getCodesOrganeController().setBanque(getMainWindow().getSelectedBanque());
      getCodesMorphoController().setIsOrg(false);
      getCodesMorphoController().setIsMorpho(true);
      getCodesMorphoController().setBanque(getMainWindow().getSelectedBanque());

      if(SessionUtils.getSelectedBanques(sessionScope).size() > 0
         && SessionUtils.getSelectedBanques(sessionScope).get(0).getContexte() != null
         && SessionUtils.getSelectedBanques(sessionScope).get(0).getContexte().getNom().equals("anatomopathologie")){
         groupInfosCompEchan.setOpen(true);
      }else{
         groupInfosCompEchan.setOpen(false);
      }
   }

   @Override
   public void setObject(final TKdataObject echan){
      this.echantillon = (Echantillon) echan;

      prelevement = null;
      setCrAnapath(new Fichier());
      emplacementAdrl = "";

      if(this.echantillon.getEchantillonId() != null){
         setBanque(echantillon.getBanque());
         // on récupère le prélèvement parent
         setParentObject(ManagerLocator.getEchantillonManager().getPrelevementManager(echantillon));
         emplacementAdrl = ManagerLocator.getEchantillonManager().getEmplacementAdrlManager(echantillon);
         getCodesOrganeController().setObjs(CodeAssigneDecorator
            .decorateListe(ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(echantillon)));
         getCodesMorphoController().setObjs(CodeAssigneDecorator
            .decorateListe(ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(echantillon)));

         initDelaiCgl();

         // cr anapath
         if(echantillon.getCrAnapath() != null){
            showDeleteAndFileNameBox(true);
            crAnapathNomBox.setValue(echantillon.getCrAnapath().getNom());
            setCrAnapath(echantillon.getCrAnapath().clone());
         }

         // statut EN COURS -> modification quantite impossible
         quantiteInitBoxEchan.setDisabled(echantillon.getObjetStatut().getStatut().equals("ENCOURS"));
         quaniteUnitesBoxEchan.setDisabled(echantillon.getObjetStatut().getStatut().equals("ENCOURS"));
      }
      // envoie l'enchantillon au controller des codes assignes
      getCodesOrganeController().setEchantillon(echantillon);
      getCodesMorphoController().setEchantillon(echantillon);

      super.setObject(this.echantillon);

      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       */
      /*FicheAnnotation inline = getFicheAnnotationInline();
      if(null != inline){ // re-dessine le bloc inline annotation
         inline.setObj(this.echantillon);
      }*/
   }

   @Override
   public Echantillon getObject(){
      return this.echantillon;
   }

   @Override
   public void setNewObject(){
      setObject(new Echantillon());
   }

   public Echantillon getEchantillon(){
      return echantillon;
   }

   public void setEchantillon(final Echantillon e){
      this.echantillon = e;
   }

   @Override
   public void setParentObject(final TKdataObject obj){
      this.prelevement = ((Prelevement) obj);
   }

   @Override
   public Prelevement getParentObject(){
      return this.prelevement;
   }

   public String getNomPatient(){
      if(this.prelevement != null){
         return PrelevementUtils.getPatientNomAndPrenom(prelevement);
      }
      return null;
   }

   @Override
   public Echantillon getCopy(){
      return (Echantillon) super.getCopy();
   }

   @Override
   public EchantillonController getObjectTabController(){
      return (EchantillonController) super.getObjectTabController();
   }

   @Override
   public void switchToEditMode(){
      // Initialisation du mode (listes, valeurs...)
      initQuantiteAndVolume();
      initEditableMode();

      super.switchToEditMode();

      if(prelevement != null){
         row1PrlvtEchan.setVisible(false);
         row2PrlvtEchan.setVisible(true);
         //row3PrlvtEchan.setVisible(true);
      }else{
         row1PrlvtEchan.setVisible(true);
         row2PrlvtEchan.setVisible(false);
         //row3PrlvtEchan.setVisible(false);
      }

      if(!getDroitOnAction("Collaborateur", "Consultation")){
         operateurAideSaisieEchan.setVisible(false);
      }
      if(!getDroitOnAction("Prelevement", "Consultation")){
         codePrlvtLabel.setSclass("formValue");
      }else{
         codePrlvtLabel.setSclass("formLink");
      }

      getBinder().loadComponent(self);
   }

   @Override
   public void onClick$create(){
      super.onClick$create();
   }

   @Override
   public void createNewObject(){
      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       */
      // On ajoute à la liste des annotations verticales celles horizontales
      /*List<AnnotationValeur> annotationValeursToCreateOrUpdate = getObjectTabController().getFicheAnnotationInline().getValeursToCreateOrUpdate();
      annotationValeursToCreateOrUpdate.addAll(getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate());*/
   }

   @Override
   public void updateObject(){
      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       */
      // On ajoute à la liste des annotations verticales celles horizontales
      /*List<AnnotationValeur> annotationValeursToCreateOrUpdate = getObjectTabController().getFicheAnnotationInline().getValeursToCreateOrUpdate();
      annotationValeursToCreateOrUpdate.addAll(getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate());*/

      // On ajoute à la liste des annotations verticales celles horizontales
      /*List<AnnotationValeur> annotationValeursToDelete = getObjectTabController().getFicheAnnotationInline().getValeursToDelete();
      annotationValeursToDelete.addAll(getObjectTabController().getFicheAnnotation().getValeursToDelete());*/
      /** END **/

      // création du code échantillon en fct de celui du prlvt et
      // de celui saisi
      //StringBuffer sb = new StringBuffer();
      //sb.append(codePrefixe);
      //sb.append(".");
      //sb.append(codeSuffixe);
      //echantillon.setCode(sb.toString());
      echantillon.setCode(codePrefixe);

      // on remplit l'échantillon en fonction des champs nulls
      setEmptyToNulls();
      setFieldsToUpperCase();

      echantillon.setTumoral(tumoraleBox.isChecked());
      echantillon.setSterile(sterileBox.isChecked());

      // Gestion du collaborateur
      final String selectedNomAndPremon = this.collabBox.getValue().toUpperCase();
      this.collabBox.setValue(selectedNomAndPremon);
      final int ind = nomsAndPrenoms.indexOf(selectedNomAndPremon);
      if(ind > -1){
         selectedCollaborateur = collaborateurs.get(ind);
      }else{
         selectedCollaborateur = null;
      }

      final Emplacement emp = echantillon.getEmplacement();
      final ObjetStatut statut = findStatutForTKStockableObject(this.echantillon);

      // codes Organes + export
      prepareCodes();
      echantillon.setCodesAssignes(new HashSet<CodeAssigne>());

      // crAnapath
      prepareCrAnapath();

      // lateralite
      if(selectedLateralite != null && !selectedLateralite.equals("")){
         echantillon.setLateralite(selectedLateralite);
      }

      // gestion de la non conformitée après traitement
      final List<NonConformite> noconfsT = new ArrayList<>();
      if(conformeTraitementBoxOui.isChecked()){
         this.echantillon.setConformeTraitement(true);
      }else if(conformeTraitementBoxNon.isChecked()){
         this.echantillon.setConformeTraitement(false);
         noconfsT.addAll(findSelectedNonConformitesTraitement());
      }else{
         this.echantillon.setConformeTraitement(null);
      }

      // gestion de la non conformitée à la cession
      final List<NonConformite> noconfsC = new ArrayList<>();
      if(conformeCessionBoxOui.isChecked()){
         this.echantillon.setConformeCession(true);
      }else if(conformeCessionBoxNon.isChecked()){
         this.echantillon.setConformeCession(false);
         noconfsC.addAll(findSelectedNonConformitesCession());
      }else{
         this.echantillon.setConformeCession(null);
      }

      // update de l'objet
      ManagerLocator.getEchantillonManager().updateObjectWithNonConformitesManager(echantillon, getBanque(), prelevement,
         selectedCollaborateur, statut, emp, selectedType, codesToCreateOrEdit, codesAssignesToDelete, selectedQuantiteUnite,
         selectedQualite, selectedPrepa, getCrAnapath(), getAnapathStream(), null,
         getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
         getObjectTabController().getFicheAnnotation().getValeursToDelete(), SessionUtils.getLoggedUser(sessionScope), true, null,
         SessionUtils.getSystemBaseDir(), noconfsT, noconfsC);

      /**
       * ANNOTATION INLINE - Bêta
       *
       * @since 2.2.0
       */
      /*ManagerLocator.getEchantillonManager().updateObjectWithNonConformitesManager(
         echantillon, getBanque(), prelevement,
         selectedCollaborateur,
         statut, emp, selectedType,
         codesToCreateOrEdit,
         codesAssignesToDelete,
         selectedQuantiteUnite,
         selectedQualite, selectedPrepa,
         getCrAnapath(), getAnapathStream(), null,
         annotationValeursToCreateOrUpdate,
         annotationValeursToDelete,
         SessionUtils.getLoggedUser(sessionScope), true,
         null, SessionUtils.getSystemBaseDir(),
         noconfsT, noconfsC);*/

      // délétion des champs à supprimer
      //		for (int i = 0; i < codesAssignesToDelete.size(); i++) {
      // met à jour l'echantillon si le
      // code assigne delete est celui exporté
      //			if (codesAssignesToDelete.get(i)
      //									.getEchanExpOrg() != null) {
      //				codesAssignesToDelete.get(i)
      //									.setEchanExpOrg(echantillon);
      //			} else if (codesAssignesToDelete.get(i)
      //									.getEchanExpLes() != null) {
      //				codesAssignesToDelete.get(i)
      //									.setEchanExpLes(echantillon);
      //			}

      //			ManagerLocator.getCodeAssigneManager()
      //					.removeObjectManager(codesAssignesToDelete.get(i));

      //			if (codesAssignesToDelete.get(i).getIsOrgane()) {
      //				echantillon.getCodeOrganes()
      //						.remove(codesAssignesToDelete.get(i));
      //			} else {
      //				echantillon.getCodeMorphos()
      //						.remove(codesAssignesToDelete.get(i));
      //			}
      //			echantillon.getCodesAssignes()
      //								.remove(codesAssignesToDelete.get(i));

      //		}

   }

   @Override
   public boolean onLaterUpdate(){

      if(getSelectedType() == null){
         // ferme wait message
         Clients.clearBusy();
         throw new WrongValueException(typesBoxEchan, Labels.getLabel("ficheEchantillon.error.type"));
      }

      try{
         updateObjectWithAnnots();
         // updateObject();

         // update de la liste
         if(getObjectTabController().getListe() != null){
            getObjectTabController().getListe().updateObjectGridList(getObject());
         }

         if(getParentObject() != null){
            if(getPrelevementController() != null){
               getPrelevementController().getListe().updateObjectGridListFromOtherPage(getParentObject(), true);

               //update patient
               if(getParentObject().getMaladie() != null){
                  if(!getPrelevementController().getReferencingObjectControllers().isEmpty()){
                     getPrelevementController().getReferencingObjectControllers().get(0).getListe()
                        .updateObjectGridListFromOtherPage(getParentObject().getMaladie().getPatient(), true);
                  }
               }
            }
         }

         // update de la liste des enfants et l'enfant en fiche
         getObjectTabController()
            .updateReferencedObjects((List<TKdataObject>) getObjectTabController().getChildrenObjects(getObject()));

         // commande le passage en mode statique
         getObjectTabController().onEditDone(getObject());

         // ferme wait message
         Clients.clearBusy();

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
   protected void setEmptyToNulls(){

      setHeureDelai(heureBox.getValue());
      setMinDelai(minBox.getValue());

      Float delai = (float) -1;
      if(getHeureDelai() != null || getMinDelai() != null){
         delai += 1;
      }
      if(getHeureDelai() != null){
         delai += getHeureDelai() * 60;
      }
      if(getMinDelai() != null){
         delai += getMinDelai();
      }

      // si le delai = -1 alors on l'enregistre comme null
      if(delai == -1){
         echantillon.setDelaiCgl(null);
      }else{
         echantillon.setDelaiCgl(delai);
      }

      if(this.echantillon.getArchive() == null){
         this.echantillon.setArchive(false);
      }

      if(this.echantillon.getEtatIncomplet() == null){
         this.echantillon.setEtatIncomplet(false);
      }
   }

   /**
    * Retourne les non conformites sélectionnées.
    * @return
    */

   public List<NonConformite> findSelectedNonConformitesTraitement(){
      final List<NonConformite> ncf = new ArrayList<>();
      ncf.addAll(((Selectable<NonConformite>) nonConformitesTraitementBox.getModel()).getSelection());
      return ncf;
   }

   /**
    * Retourne les non conformites sélectionnées.
    * @return
    */

   public List<NonConformite> findSelectedNonConformitesCession(){
      final List<NonConformite> ncf = new ArrayList<>();
      ncf.addAll(((Selectable<NonConformite>) nonConformitesCessionBox.getModel()).getSelection());
      return ncf;
   }

   /*************************************************************************/
   /************************** PARENT    ************************************/
   /*************************************************************************/
   private Prelevement prelevement;

   // Infos prelevement
   private Row row1PrlvtEchan;
   private Row row2PrlvtEchan;
   //private Row row3PrlvtEchan;
   private Label codePrlvtLabel;

   /**
    * Affiche la fiche d'un prélèvement.
    * @param event Event : clique sur un lien prlvtLinkDerive.
    * @throws Exception
    */
   public void onClick$codePrlvtLabel(final Event event) throws Exception{
      if(getDroitOnAction("Prelevement", "Consultation")){
         if(getPrelevementController() == null){
            getMainWindow().createMacroComponent("/zuls/echantillon/Echantillon.zul", "winEchantillon",
               (Tabpanel) getMainWindow().getMainTabbox().getTabpanels().getFellow("echantillonPanel"));
         }
         getPrelevementController().switchToFicheStaticMode(getParentObject());
         PrelevementController.backToMe(getMainWindow(), page);
      }
   }

   public String getCodePrefixe(){
      return codePrefixe;
   }

   public void setCodePrefixe(final String prefixe){
      this.codePrefixe = prefixe;
   }

   /**
    * Formate la date de prélèvement.
    * @return Date du prélèvement formatée.
    */
   public String getDatePrelevementFormated(){
      if(getParentObject() != null){
         return ObjectTypesFormatters.dateRenderer2(getParentObject().getDatePrelevement());
      }
      return null;
   }

   /*************************************************************************/
   /************************** OPERATEUR ************************************/
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
      // on va appliquer à la fenêtre un filtre sur le service de
      // la banque
      final List<Object> list = new ArrayList<>();

      // choix de la banque si toutes collections, modification
      // reste possible
      if(getBanque() == null){ // echantillon en creation
         if(SessionUtils.getSelectedBanques(sessionScope).get(0).getProprietaire() != null){
            list.add(SessionUtils.getSelectedBanques(sessionScope).get(0).getProprietaire());
         }
      }else{ // echantillon en modification
         if(getBanque().getProprietaire() != null){
            list.add(getBanque().getProprietaire());
         }
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

      if(this.echantillon.getCollaborateur() != null && !collaborateurs.contains(this.echantillon.getCollaborateur())){
         collaborateurs.add(this.echantillon.getCollaborateur());
         nomsAndPrenoms.add(this.echantillon.getCollaborateur().getNomAndPrenom());
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
   /************************** FORMATTERS ***********************************/
   /*************************************************************************/

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codeBoxEchan. Cette valeur sera mise en majuscules.
    */
   public void onBlur$codeBoxEchan(){
      codeBoxEchan.setValue(codeBoxEchan.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codePrefixeEchan. Cette valeur sera mise en majuscules.
    */
   public void onBlur$codePrefixeEchan(){
      codePrefixeEchan.setValue(codePrefixeEchan.getValue().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.echantillon.getCode() != null){
         this.echantillon.setCode(this.echantillon.getCode().toUpperCase().trim());
      }
   }

   @Override
   public void setFocusOnElement(){
      codePrefixeEchan.setFocus(true);
   }

   /**
    * Méthode appelée lors d'une modification du champ quantiteInitBoxEchan.
    * En fonction de la quantité initiale saisie, la quantité restante sera
    * mise à jour.
    */
   public void onBlur$quantiteInitBoxEchan(){

      final BigDecimal value = quantiteInitBoxEchan.getValue();
      final Float zero = new Float(0.0);
      // si la quantité initiale a changé, on modifie la quantité
      // restante en fonction
      if(value != null){
         echantillon.setQuantiteInit(value.floatValue());
         final BigDecimal bd = new BigDecimal(echantillon.getQuantiteInit());
         quantiteInitBoxEchan.setValue(bd);
         Float diff = this.echantillon.getQuantiteInit();
         // si une quantité initiale était saisie
         if(getCopy().getQuantiteInit() != null){
            diff = diff - getCopy().getQuantiteInit();
         }else if(getCopy().getQuantiteInit() == null && getCopy().getQuantite() != null){
            echantillon.setQuantite(echantillon.getQuantiteInit());
            diff = zero;
            valeurQuantiteRestante = echantillon.getQuantite().toString();
         }
         // on vérifie qu'un changement a eu lieu
         if(diff != zero){
            Float restant;
            if(getCopy().getQuantite() != null){
               restant = getCopy().getQuantite() + diff;
            }else{
               restant = diff;
            }
            // la quantité restante ne doit pas etre negative
            if(restant >= zero){
               echantillon.setQuantite(restant);
            }else{
               echantillon.setQuantite(zero);
            }
            valeurQuantiteRestante = echantillon.getQuantite().toString();
         }
      }else{
         echantillon.setQuantiteInit(null);
         echantillon.setQuantite(null);
         valeurQuantiteRestante = "-";
      }
   }

   /*************************************************************************/
   /************************** INITS ****************************************/
   /*************************************************************************/

   /**
    * Méthode initialisant les champs de formulaire pour la quantité et
    * le volume.
    */
   public void initQuantiteAndVolume(){
      final StringBuffer sb = new StringBuffer();
      if(this.echantillon.getQuantite() != null){
         sb.append(this.echantillon.getQuantite());
      }else{
         sb.append("-");
      }
      valeurQuantiteRestante = sb.toString();
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

      if(this.echantillon.getDelaiCgl() != null && this.echantillon.getDelaiCgl() < 0){
         this.echantillon.setDelaiCgl(null);
      }

      if(this.echantillon.getDelaiCgl() != null){
         final Float heure = this.echantillon.getDelaiCgl() / 60;
         if(heure > 0){
            heureDelai = heure.intValue();
            minDelai = this.echantillon.getDelaiCgl().intValue() - (heureDelai * 60);
         }else{
            heureDelai = 0;
            minDelai = this.echantillon.getDelaiCgl().intValue();
         }
      }else{
         calculDelaiCgl();
      }
   }

   /**
    * Cette méthode calcule automatiquement le délaiCgl.
    * Calcule la différence entre la date de prélèvement, la date associée
    * à la congélation dans la fiche labointer ou la date de stockage de l'
    * echantillon.
    * Ces dates doivent contenir des heures/minutes pour etre prise en
    * compte dans le calcul.
    */
   public void calculDelaiCgl(){
      setHeureDelai(null);
      setMinDelai(null);
      // on vérifie que la date de prlvt est exploitables
      if(getParentObject() != null && getParentObject().getDatePrelevement() != null
         && (getParentObject().getDatePrelevement().get(Calendar.HOUR_OF_DAY) != 0
            || getParentObject().getDatePrelevement().get(Calendar.MINUTE) != 0
            || getParentObject().getDatePrelevement().get(Calendar.SECOND) != 0)){

         // creation ou update dans procedure
         //			if (getLaboInters() != null) { 
         //				((Prelevement) getParentObject())
         //					.setLaboInters(new HashSet<LaboInter>(getLaboInters()));
         //			}
         //			
         //			Calendar dateRefPrel = ManagerLocator
         //				.getPrelevementManager()
         //					.getDateCongelationManager(getParentObject());
         long milli = -1;
         //			if (dateRefPrel != null) {
         //				if (dateRefPrel.get(Calendar.HOUR_OF_DAY) != 0
         //					|| dateRefPrel.get(Calendar.MINUTE) != 0
         //					|| dateRefPrel.get(Calendar.SECOND) != 0) {
         //					milli = dateRefPrel.getTimeInMillis() 
         //							- getParentObject()
         //							.getDatePrelevement().getTimeInMillis();
         //				}			
         //			} else
         if(this.echantillon.getDateStock() != null){
            if(this.echantillon.getDateStock().get(Calendar.HOUR_OF_DAY) != 0
               || this.echantillon.getDateStock().get(Calendar.MINUTE) != 0
               || this.echantillon.getDateStock().get(Calendar.SECOND) != 0){
               milli =
                  this.echantillon.getDateStock().getTimeInMillis() - getParentObject().getDatePrelevement().getTimeInMillis();
            }
         }
         if(milli > 0){
            final Float min = (float) (milli / 60000);

            final Float heure = min / 60;
            if(heure > 0){
               setHeureDelai(heure.intValue());
               setMinDelai(min.intValue() - (getHeureDelai() * 60));
            }else{
               setHeureDelai(0);
               setMinDelai(min.intValue());
            }
         }
      }
   }

   //	/**
   //	 * Cette méthode calcul le délaiCgl en fonction de la date de
   //	 * prélèvement et de la date de congélation.
   //	 */
   //	public void calculDelaiCgl2() {
   //		// on vérifie que le prlvt et les dates ne sont pas nulles
   //		if (getParentObject() != null
   //				&& getParentObject().getDatePrelevement() != null
   //				&& this.echantillon.getDateStock() != null) {
   //			
   //			// on vérifie que les dates sont correctes
   //			if (getParentObject().getDatePrelevement()
   //					.before(this.echantillon.getDateStock())) {
   //				
   //				// on vérifie que les horaires ont bien été remplis
   //				if ((this.echantillon.getDateStock()
   //												.get(Calendar.HOUR_OF_DAY) != 0
   //					|| this.echantillon.getDateStock()
   //													.get(Calendar.MINUTE) != 0
   //					|| this.echantillon.getDateStock()
   //													.get(Calendar.SECOND) != 0)
   //					&& (getParentObject().getDatePrelevement()
   //												.get(Calendar.HOUR_OF_DAY) != 0
   //					|| getParentObject().getDatePrelevement()
   //													.get(Calendar.MINUTE) != 0
   //					|| getParentObject().getDatePrelevement()
   //												.get(Calendar.SECOND) != 0)) {
   //				
   //					// calcul de la date avec des Calendars
   //					Calendar calendarPrlvt = getParentObject()
   //													.getDatePrelevement();
   //					
   //					Calendar calendarCongelation = this.echantillon
   //														.getDateStock();
   //					
   //					long milli = calendarCongelation.getTimeInMillis() 
   //								- calendarPrlvt.getTimeInMillis();
   //					
   //					Float min = (float) (milli / 60000);
   //					
   //					Float heure = min / 60;
   //					if (heure > 0) {
   //						setHeureDelai(heure.intValue());
   //						setMinDelai(min.intValue() - (getHeureDelai() * 60));
   //					} else {
   //						setHeureDelai(0);
   //						setMinDelai(min.intValue());
   //					}
   //				
   //				} else {
   //					setHeureDelai(null);
   //					setMinDelai(null);
   //				}
   //				
   //			} else {
   //				setHeureDelai(null);
   //				setMinDelai(null);
   //			}
   //		} else {
   //			setHeureDelai(null);
   //			setMinDelai(null);
   //		}
   //	}

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */

   public void initEditableMode(){
      quantite = null;
      quantiteInit = null;

      codePrefixe = this.echantillon.getCode();

      types = ManagerLocator.getEchantillonTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      types.add(0, null);
      if(echantillon.getEchantillonType() != null){
         selectedType = echantillon.getEchantillonType();
      }else{
         selectedType = types.get(0);
      }

      // on récupère les unités de quantité
      quantiteUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("masse", true);
      List<Unite> tmpUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("discret", true);
      quantiteUnites.addAll(tmpUnites);
      tmpUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("volume", true);
      quantiteUnites.addAll(tmpUnites);
      quantiteUnites.add(0, null);
      selectedQuantiteUnite = echantillon.getQuantiteUnite();

      // init des collaborateurs
      nomsAndPrenoms = new ArrayList<>();
      collaborateurs = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();
      for(int i = 0; i < collaborateurs.size(); i++){
         nomsAndPrenoms.add(collaborateurs.get(i).getNomAndPrenom());
      }
      collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));

      if(this.echantillon.getCollaborateur() != null && collaborateurs.contains(this.echantillon.getCollaborateur())){
         selectedCollaborateur = this.echantillon.getCollaborateur();
         collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
      }else if(this.echantillon.getCollaborateur() != null){
         collaborateurs.add(this.echantillon.getCollaborateur());
         selectedCollaborateur = this.echantillon.getCollaborateur();
         nomsAndPrenoms.add(this.echantillon.getCollaborateur().getNomAndPrenom());
         collabBox.setModel(new CustomSimpleListModel(nomsAndPrenoms));
         collabBox.setValue(selectedCollaborateur.getNomAndPrenom());
      }else{
         collabBox.setValue("");
         selectedCollaborateur = null;
      }

      qualites = ManagerLocator.getEchanQualiteManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      qualites.add(0, null);
      selectedQualite = echantillon.getEchanQualite();

      prepas = ManagerLocator.getModePrepaManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      prepas.add(0, null);
      selectedPrepa = echantillon.getModePrepa();

      if(echantillon.getTumoral() != null){
         tumoraleBox.setChecked(echantillon.getTumoral());
      }else{
         tumoraleBox.setChecked(false);
      }

      if(echantillon.getSterile() != null){
         sterileBox.setChecked(echantillon.getSterile());
      }else{
         sterileBox.setChecked(false);
      }

      // si la quantité init est nulle mais pas la quantité restante
      // => quantiteInit = quantite
      if(this.echantillon.getQuantite() != null && this.echantillon.getQuantiteInit() == null){
         this.echantillon.setQuantiteInit(this.echantillon.getQuantite());
      }

      initNonConformites();
   }

   public void initNonConformites(){
      // init des non conformites de traitement
      nonConformitesTraitement = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
         SessionUtils.getPlateforme(sessionScope), "Traitement", getObjectTabController().getEntiteTab());
      selectedNonConformiteTraitement = null;
      if(echantillon != null && echantillon.getEchantillonId() != null){
         final List<ObjetNonConforme> tmp =
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(echantillon, "Traitement");
         if(tmp.size() > 0){
            if(nonConformitesTraitement.contains(tmp.get(0).getNonConformite())){
               selectedNonConformiteTraitement = tmp.get(0).getNonConformite();
            }
         }
      }
      if(this.echantillon.getConformeTraitement() != null){
         if(this.echantillon.getConformeTraitement()){
            conformeTraitementBoxOui.setChecked(true);
            conformeTraitementBoxNon.setChecked(false);
            conformeTraitementBox.setVisible(false);
         }else{
            conformeTraitementBoxOui.setChecked(false);
            conformeTraitementBoxNon.setChecked(true);
            conformeTraitementBox.setVisible(true);
         }
      }

      // init des non conformites à la cession
      nonConformitesCession = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
         SessionUtils.getPlateforme(sessionScope), "Cession", getObjectTabController().getEntiteTab());
      selectedNonConformiteCession = null;
      if(echantillon != null && echantillon.getEchantillonId() != null){
         final List<ObjetNonConforme> tmp =
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(echantillon, "Cession");
         if(tmp.size() > 0){
            if(nonConformitesCession.contains(tmp.get(0).getNonConformite())){
               selectedNonConformiteCession = tmp.get(0).getNonConformite();
            }
         }
      }
      if(this.echantillon.getConformeCession() != null){
         if(this.echantillon.getConformeCession()){
            conformeCessionBoxOui.setChecked(true);
            conformeCessionBoxNon.setChecked(false);
            conformeCessionBox.setVisible(false);
         }else{
            conformeCessionBoxOui.setChecked(false);
            conformeCessionBoxNon.setChecked(true);
            conformeCessionBox.setVisible(true);
         }
      }

      getBinder().loadComponent(nonConformitesTraitementBox);
      getBinder().loadComponent(nonConformitesCessionBox);

      selectNonConformites();
   }

   /**
    * Select les non conformites dans la dropdown list.
    * @param risks liste à selectionner
    */

   public void selectNonConformites(){
      final List<NonConformite> ncfTrait = new ArrayList<>();
      final List<NonConformite> ncfCess = new ArrayList<>();
      if(echantillon != null && echantillon.getEchantillonId() != null){
         List<ObjetNonConforme> list =
            ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(echantillon, "Traitement");
         for(int i = 0; i < list.size(); i++){
            ncfTrait.add(list.get(i).getNonConformite());
         }

         list = ManagerLocator.getObjetNonConformeManager().findByObjetAndTypeManager(echantillon, "Cession");
         for(int i = 0; i < list.size(); i++){
            ncfCess.add(list.get(i).getNonConformite());
         }
      }

      ((Selectable<NonConformite>) nonConformitesTraitementBox.getModel()).setSelection(ncfTrait);
      ((Selectable<NonConformite>) nonConformitesCessionBox.getModel()).setSelection(ncfCess);

      getBinder().loadAttribute(nonConformitesTraitementBox, "selectedItems");
      getBinder().loadAttribute(nonConformitesCessionBox, "selectedItems");

   }

   public void onCheck$conformeTraitementBoxOui(){
      if(conformeTraitementBoxOui.isChecked()){
         conformeTraitementBoxNon.setChecked(false);
         conformeTraitementBox.setVisible(false);
      }
   }

   public void onCheck$conformeTraitementBoxNon(){
      if(conformeTraitementBoxNon.isChecked()){
         conformeTraitementBoxOui.setChecked(false);
         conformeTraitementBox.setVisible(true);
      }else{
         conformeTraitementBox.setVisible(false);
      }
   }

   public void onCheck$conformeCessionBoxOui(){
      if(conformeCessionBoxOui.isChecked()){
         conformeCessionBoxNon.setChecked(false);
         conformeCessionBox.setVisible(false);
      }
   }

   public void onCheck$conformeCessionBoxNon(){
      if(conformeCessionBoxNon.isChecked()){
         conformeCessionBoxOui.setChecked(false);
         conformeCessionBox.setVisible(true);
      }else{
         conformeCessionBox.setVisible(false);
      }
   }

   public List<EchantillonType> getTypes(){
      return types;
   }

   public EchantillonType getSelectedType(){
      return selectedType;
   }

   public void setSelectedType(final EchantillonType selected){
      this.selectedType = selected;
   }

   public List<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final List<Collaborateur> colls){
      this.collaborateurs = colls;
   }

   public List<String> getNomsAndPrenoms(){
      return nomsAndPrenoms;
   }

   public void setNomsAndPrenoms(final List<String> nAndPs){
      this.nomsAndPrenoms = nAndPs;
   }

   public Collaborateur getSelectedCollaborateur(){
      return selectedCollaborateur;
   }

   public void setSelectedCollaborateur(final Collaborateur selectedCollab){
      this.selectedCollaborateur = selectedCollab;
   }

   public List<EchanQualite> getQualites(){
      return qualites;
   }

   public EchanQualite getSelectedQualite(){
      return selectedQualite;
   }

   public void setSelectedQualite(final EchanQualite selected){
      this.selectedQualite = selected;
   }

   public Unite getSelectedQuantiteUnite(){
      return selectedQuantiteUnite;
   }

   public void setSelectedQuantiteUnite(final Unite selectedQuantite){
      this.selectedQuantiteUnite = selectedQuantite;
   }

   public List<ModePrepa> getPrepas(){
      return prepas;
   }

   public ModePrepa getSelectedPrepa(){
      return selectedPrepa;
   }

   public void setSelectedPrepa(final ModePrepa selected){
      this.selectedPrepa = selected;
   }

   public String getValeurQuantite(){
      return valeurQuantite;
   }

   public void setValeurQuantite(final String q){
      this.valeurQuantite = q;
   }

   public String getValeurQuantiteRestante(){
      return valeurQuantiteRestante;
   }

   public void setValeurQuantiteRestante(final String valeurRestante){
      this.valeurQuantiteRestante = valeurRestante;
   }

   public Integer getHeureDelai(){
      return heureDelai;
   }

   public void setHeureDelai(final Integer heure){
      this.heureDelai = heure;
   }

   public Integer getMinDelai(){
      return minDelai;
   }

   public void setMinDelai(final Integer min){
      this.minDelai = min;
   }

   public List<Unite> getQuantiteUnites(){
      return quantiteUnites;
   }

   public Float getQuantite(){
      return quantite;
   }

   public void setQuantite(final Float q){
      this.quantite = q;
   }

   public Float getQuantiteInit(){
      return quantiteInit;
   }

   public void setQuantiteInit(final Float qInit){
      this.quantiteInit = qInit;
   }

   public String getEmplacementAdrl(){
      boolean isAnonyme = false;
      if(sessionScope.containsKey("Anonyme") && (Boolean) sessionScope.get("Anonyme")){
         isAnonyme = true;
      }else{
         isAnonyme = false;
      }

      if(isAnonyme){
         return "-";
      }
      return emplacementAdrl;
   }

   public String getTemperatureFormated(){
      Float temp = null;

      if(this.echantillon != null && this.echantillon.getEchantillonId() != null){
         final Emplacement emp = ManagerLocator.getEchantillonManager().getEmplacementManager(echantillon);

         if(emp != null){
            final Conteneur cont = ManagerLocator.getEmplacementManager().getConteneurManager(emp);
            if(cont != null && cont.getTemp() != null){
               temp = cont.getTemp();
            }
         }
      }

      return ObjectTypesFormatters.formatTemperature(temp);
   }

   public void setEmplacementAdrl(final String emplacementA){
      this.emplacementAdrl = emplacementA;
   }

   public String getSelectedLateralite(){
      return selectedLateralite;
   }

   public void setSelectedLateralite(final String selected){
      this.selectedLateralite = selected;
   }

   public Fichier getCrAnapath(){
      return crAnapath;
   }

   public void setCrAnapath(final Fichier c){
      this.crAnapath = c;
   }

   public InputStream getAnapathStream(){
      return anapathStream;
   }

   public void setAnapathStream(final InputStream aS){
      this.anapathStream = aS;
   }

   public List<LaboInter> getLaboInters(){
      return laboInters;
   }

   public void setLaboInters(final List<LaboInter> labos){
      this.laboInters = labos;
   }

   public List<LaboInter> getLaboIntersToDelete(){
      return laboIntersToDelete;
   }

   public void setLaboIntersToDelete(final List<LaboInter> labosToDelete){
      this.laboIntersToDelete = labosToDelete;
   }

   /*********************************************************/
   /********************** VALIDATION ***********************/
   /*********************************************************/
   private final Constraint cttQuantite = new ConstQuantite();
   private final Constraint cttQuantiteInit = new ConstQuantiteInit();

   public Constraint getCttQuantite(){
      return cttQuantite;
   }

   public Constraint getCttQuantiteInit(){
      return cttQuantiteInit;
   }

   /**
    * Contrainte vérifiant que la quantité n'est pas supérieure
    * à la quantité init.
    * @author Pierre Ventadour.
    *
    */
   public class ConstQuantite implements Constraint
   {
      /**
       * Méthode de validation du champ quantiteBox.
       */
      @Override
      public void validate(final Component comp, final Object value){
         // on récupère la valeur dans quantiteBox
         final BigDecimal quantiteValue = (BigDecimal) value;
         // Si la QuantiteInit est null (1ère édition de la page)
         // on va récupérer la valeur du champ quantiteInitBox
         if(quantiteInit == null){
            // on enlève la contrainte de quantiteInitBox pour
            // pouvoir récupérer sa valeur
            quantiteInitBoxEchan.setConstraint("");
            quantiteInitBoxEchan.clearErrorMessage();
            final BigDecimal quantiteInitValue = quantiteInitBoxEchan.getValue();
            // on remet la contrainte
            quantiteInitBoxEchan.setConstraint(cttQuantiteInit);
            // si la valeur n'est pas nulle, on initialise quantiteInit
            if(quantiteInitValue != null){
               quantiteInit = quantiteInitValue.floatValue();
            }
         }

         // si une valeur est saisie dans le champ quantiteBox
         if(quantiteValue != null){
            quantite = quantiteValue.floatValue();
            // la quantite doit être positive
            if(quantite < 0){
               throw new WrongValueException(comp, "Seul les nombres non-négatifs sont autorisés.");
            }
            // si une valeur est saisie dans le champ quantiteInitBox
            if(quantiteInit != null){
               // si la quantite est supérieure à la quantiteInit
               if(quantite > quantiteInit){
                  throw new WrongValueException(comp,
                     "La quantité actuelle ne peut pas être " + "supérieure à la quantité initiale.");
               }
               // sinon on enlève toutes les erreurs affichées
               final BigDecimal decimal = new BigDecimal(quantiteInit);
               Clients.clearWrongValue(quantiteInitBoxEchan);
               quantiteInitBoxEchan.setConstraint("");
               quantiteInitBoxEchan.setValue(decimal);
               quantiteInitBoxEchan.setConstraint(cttQuantiteInit);
            }else{
               // si aucune quantiteInit n'est saisie, on enlève les
               // erreurs
               Clients.clearWrongValue(quantiteInitBoxEchan);
               quantiteInitBoxEchan.setConstraint("");
               //quantiteInitBoxEchan.setValue(null);
               quantiteInitBoxEchan.setValue("");
               quantiteInitBoxEchan.setConstraint(cttQuantiteInit);
            }
         }else{
            // si aucune quantite n'est saisie, on enlève les
            // erreurs
            quantite = null;
            if(quantiteInit != null){
               final BigDecimal decimal = new BigDecimal(quantiteInit);
               Clients.clearWrongValue(quantiteInitBoxEchan);
               quantiteInitBoxEchan.setConstraint("");
               quantiteInitBoxEchan.setValue(decimal);
               quantiteInitBoxEchan.setConstraint(cttQuantiteInit);
            }else{
               Clients.clearWrongValue(quantiteInitBoxEchan);
               quantiteInitBoxEchan.setConstraint("");
               //quantiteInitBoxEchan.setValue(null);
               quantiteInitBoxEchan.setValue("");
               quantiteInitBoxEchan.setConstraint(cttQuantiteInit);
            }
         }
      }
   }

   /**
    * Contrainte vérifiant que la quantiteInit n'est pas inférieure
    * à la quantite.
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
            quantiteInit = quantiteInitValue.floatValue();
            // la quantiteInit doit être positive
            if(quantiteInit < 0){
               throw new WrongValueException(comp, Labels.getLabel("validation.negative.value"));
            }

            // si l'échantillon avait deja une valeur de quantite et de
            // quantiteInit
            if(getCopy().getQuantite() != null && getCopy().getQuantiteInit() != null){

               final Float diff = quantiteInitValue.floatValue() - getCopy().getQuantiteInit();
               final Float restant = getCopy().getQuantite() + diff;
               final Float zero = new Float(0.0);
               // si la Quantite restante est négative
               if(restant < zero){
                  // on calcule la quantiteInit minimale autorisée
                  final Float min = getCopy().getQuantiteInit() - getCopy().getQuantite();
                  throw new WrongValueException(comp, Labels.getLabel("validation.invalid.quantite.init") + " " + min);
               }
            }
         }
      }
   }

   @Override
   public void clearConstraints(){
      Clients.clearWrongValue(codePrefixeEchan);
      Clients.clearWrongValue(quantiteBoxEchan);
      Clients.clearWrongValue(quantiteInitBoxEchan);
      Clients.clearWrongValue(sterileBox);
      Clients.clearWrongValue(dateStockCalBox);
   }

   /**
    * Applique la validation sur la date.
    */
   public void onBlur$dateStockCalBox(){
      final Datebox box = (Datebox) dateStockCalBox.getFirstChild().getFirstChild();
      boolean badDateFormat = false;
      if(box.getErrorMessage() != null && box.getErrorMessage().contains(box.getFormat())){
         badDateFormat = true;
      }
      if(!badDateFormat){
         if(!dateStockCalBox.isHasChanged() && dateStockCalBox.getValue() == null){
            if(getParentObject() != null){
               dateStockCalBox.setValue(ObjectTypesFormatters.getDateWithoutHoursAndMins(getParentObject().getDatePrelevement()));
               getEchantillon().setDateStock(dateStockCalBox.getValue());
            }
         }else{
            dateStockCalBox.clearErrorMessage(dateStockCalBox.getValue());
            validateCoherenceDate(dateStockCalBox, dateStockCalBox.getValue());
         }
         echantillon.setDateStock(dateStockCalBox.getValue());
         calculDelaiCgl();
         dateStockCalBox.setHasChanged(true);
      }else{
         throw new WrongValueException(dateStockCalBox, Labels.getLabel("validation.invalid.date"));
      }
   }

   /**
    * Applique la validation sur la sterilite.
    */
   public void onCheck$sterileBox(){
      Clients.clearWrongValue(sterileBox);
      validateSterilite(sterileBox.isChecked());
   }

   public void onSelect$typesBoxEchan(){
      if(getSelectedType() == null){
         Clients.scrollIntoView(typesBoxEchan);
         throw new WrongValueException(typesBoxEchan, Labels.getLabel("ficheEchantillon.error.type"));
      }
      Clients.clearWrongValue(typesBoxEchan);
   }

   @Override
   protected void validateCoherenceDate(final Component comp, final Object value){
      Errors errs = null;
      String field = "";
      if(value == null || value.equals("")){
         ((CalendarBox) comp).clearErrorMessage(dateStockCalBox.getValue());
         ((CalendarBox) comp).setValue(null);
         echantillon.setDateStock(null);
      }else{
         // date stock
         if(comp.getId().equals("dateStockCalBox")){
            field = "dateStock";
            if(getParentObject() != null){
               if(getLaboInters() == null){ // edit simple
                  getParentObject().setLaboInters(
                     new HashSet<>(ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(getParentObject())));
               }else{ // creation ou update dans procedure
                  getParentObject().setLaboInters(new HashSet<>(getLaboInters()));
               }
            }

            getEchantillon().setPrelevement(getParentObject());
            getEchantillon().setDateStock((Calendar) value);
            errs = ManagerLocator.getEchantillonValidator().checkDateStockageCoherence(getEchantillon(), true);
         }

         if(errs != null && errs.hasErrors()){
            Clients.scrollIntoView(comp);
            heureBox.setValue(0);
            minBox.setValue(0);
            setMinDelai(0);
            throw new WrongValueException(comp, ObjectTypesFormatters.handleErrors(errs, field));
         }
      }
   }

   /**
    * Valide l'application de la sterilite au niveau echantillon.
    * @param value
    */
   private void validateSterilite(final boolean value){
      Errors errs = null;
      String field = "";

      if(value){
         field = "sterile";
         if(this.prelevement != null){
            this.prelevement.setLaboInters(
               new HashSet<>(ManagerLocator.getPrelevementManager().getLaboIntersWithOrderManager(this.prelevement)));
         }
         this.echantillon.setPrelevement(this.prelevement);
         this.echantillon.setSterile(true);
         errs = ManagerLocator.getEchantillonValidator().checkSteriliteAntecedence(this.echantillon);
      }

      if(errs != null && errs.hasErrors()){

         throw new WrongValueException(sterileBox, ObjectTypesFormatters.handleErrors(errs, field));
      }
   }

   public ConstCode getCodePrefixConstraint(){
      return EchantillonConstraints.getCodePrefixConstraint();
   }

   public ConstCode getCodePrefixNullableConstraint(){
      return EchantillonConstraints.getCodePrefixNullableConstraint();
   }

   public ConstCode getCodeSuffixConstraint(){
      return EchantillonConstraints.getCodeSuffixConstraint();
   }

   public ConstFilename getCrAnapathConstraint(){
      return EchantillonConstraints.getCrAnapathConstraint();
   }

   public List<CodeAssigne> getCodesToCreateOrEdit(){
      return codesToCreateOrEdit;
   }

   public void setCodesToCreateOrEdit(final List<CodeAssigne> cce){
      this.codesToCreateOrEdit = cce;
   }

   public List<CodeAssigne> getCodesAssignesToDelete(){
      return codesAssignesToDelete;
   }

   public void setCodesAssignesToDelete(final List<CodeAssigne> ccD){
      this.codesAssignesToDelete = ccD;
   }

   /*************************************************************/
   /*********************** Codes assignes. *********************/
   /*************************************************************/

   /**
    * Pour les codes organes et lésionnels,
    * parcourt chaqueCodeAssigneDecorator cree ou modifie, extrait le code,
    * recupere le codeOrganeToExport.
    */
   public void prepareCodes(){

      if(getCodesToCreateOrEdit() != null){
         getCodesToCreateOrEdit().clear();
      }else{
         setCodesToCreateOrEdit(new ArrayList<CodeAssigne>());
      }

      // prepare la liste de CodeAssigne a passer
      Iterator<SmallObjDecorator> it = getCodesOrganeController().getObjs().iterator();
      CodeAssigneDecorator next;
      //int ordre = 0;
      while(it.hasNext()){
         next = (CodeAssigneDecorator) it.next();
         next.syncOrdre();
         getCodesToCreateOrEdit().add((CodeAssigne) next.getObj());
      }

      // prepare la liste de CodeAssigne a passer
      it = getCodesMorphoController().getObjs().iterator();
      while(it.hasNext()){
         next = (CodeAssigneDecorator) it.next();
         next.syncOrdre();
         getCodesToCreateOrEdit().add((CodeAssigne) next.getObj());
      }

      // passe à null les listes si vides
      if(getCodesToCreateOrEdit().isEmpty()){
         setCodesToCreateOrEdit(null);
      }

      // codesAssigneToDelete
      it = getCodesOrganeController().getObjToDelete().iterator();
      while(it.hasNext()){
         getCodesAssignesToDelete().add((CodeAssigne) ((CodeAssigneDecorator) it.next()).getObj());
      }
      it = getCodesMorphoController().getObjToDelete().iterator();
      while(it.hasNext()){
         getCodesAssignesToDelete().add((CodeAssigne) ((CodeAssigneDecorator) it.next()).getObj());
      }

   }

   public CodeAssigneEditableGrid getCodesOrganeController(){
      return (CodeAssigneEditableGrid) self.getFellow("organesEditor").getFirstChild()
         .getAttributeOrFellow("codesAssitGridDiv$composer", true);
   }

   public CodeAssigneEditableGrid getCodesMorphoController(){
      return (CodeAssigneEditableGrid) self.getFellow("morphosEditor").getFirstChild()
         .getAttributeOrFellow("codesAssitGridDiv$composer", true);
   }

   /**
    * Sélection d'une latéralité pour l'échantillon.
    * @param event Event : sélection sur la liste lateralitesBox.
    * @throws Exception
    */
   public void onSelect$lateralitesBox(final Event event) throws Exception{
      selectedLateralite = lateralitesBox.getSelectedItem().getLabel();
   }

   /*************************************************************/
   /*********************** CCR anapath. ************************/
   /*************************************************************/

   /**
    * Recupere le nom du fichier et le stream depuis l'interface.
    */
   protected void prepareCrAnapath(){
      if(crAnapathNomBox.getValue() != null && !crAnapathNomBox.getValue().equals("")){
         getCrAnapath().setNom(crAnapathNomBox.getValue());
      }else{
         setCrAnapath(null);
         setAnapathStream(null);
      }
   }

   public void onClick$addCrFile(){
      Media[] medias;
      medias = Fileupload.get(
         ObjectTypesFormatters.getLabel("general.upload.limit",
            new String[] {String.valueOf(EchantillonConstraints.getSizeLimit())}),
         Labels.getLabel("ficheEchantillon" + ".crAnapath.upload.title"), 1, EchantillonConstraints.getSizeLimit(), true);
      if(medias != null && medias.length > 0){
         anapathStream = new ByteArrayInputStream(medias[0].getByteData());
         crAnapathNomBox.setValue(medias[0].getName());
         showDeleteAndFileNameBox(true);
         getCrAnapathConstraint().validate(crAnapathNomBox, medias[0].getName());
      }
   }

   public void onBlur$crAnapathNomBox(){
      getCrAnapathConstraint().validate(crAnapathNomBox, crAnapathNomBox.getValue());
   }

   public void onClick$deleteCrFile(){
      resetCrAnapathBoxes();
   }

   /**
    * Remet les composants d'enregistrement du fichier
    * à l'état initial, fichier vide.
    */
   public void resetCrAnapathBoxes(){
      setAnapathStream(null);
      crAnapathNomBox.setValue(null);
      showDeleteAndFileNameBox(false);
   }

   /**
    * Affiche ou non les composants delete/textbox pour une annotation fichier
    * en mode edit.
    * @param boolean visible
    */
   public void showDeleteAndFileNameBox(final boolean visible){
      crAnapathNomBox.setVisible(visible);
      deleteCrFile.setVisible(visible);
   }

   /**
    * Remet à 0 le delai de congélation.
    */
   public void resetDelaiCgl(){
      setHeureDelai(null);
      setMinDelai(null);
      getBinder().loadComponent(heureBox);
      getBinder().loadComponent(minBox);
   }

   /**
    * Bloque la navigation et passe le contrôle à la méthode
    * onLaterUpdate.
    */
   @Override
   public void onClick$validate(){
      onBlur$dateStockCalBox();
      Clients.showBusy(Labels.getLabel(getWaitLabel()));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public PrelevementController getPrelevementController(){
      if(!getObjectTabController().getReferencingObjectControllers().isEmpty()){
         return (PrelevementController) getObjectTabController().getReferencingObjectControllers().get(0);
      }
      return null;
   }

   public List<NonConformite> getNonConformitesTraitement(){
      return nonConformitesTraitement;
   }

   public void setNonConformitesTraitement(final List<NonConformite> nConformitesTraitement){
      this.nonConformitesTraitement = nConformitesTraitement;
   }

   public NonConformite getSelectedNonConformiteTraitement(){
      return selectedNonConformiteTraitement;
   }

   public void setSelectedNonConformiteTraitement(final NonConformite selectedNConformiteTraitement){
      this.selectedNonConformiteTraitement = selectedNConformiteTraitement;
   }

   public List<NonConformite> getNonConformitesCession(){
      return nonConformitesCession;
   }

   public void setNonConformitesCession(final List<NonConformite> nConformitesCession){
      this.nonConformitesCession = nConformitesCession;
   }

   public NonConformite getSelectedNonConformiteCession(){
      return selectedNonConformiteCession;
   }

   public void setSelectedNonConformiteCession(final NonConformite selectedNConformiteCession){
      this.selectedNonConformiteCession = selectedNConformiteCession;
   }

   //	public Set<Listitem> getSelectedNonConformitesTraitementItem() {
   //		return selectedNonConformitesTraitementItem;
   //	}
   //
   //	public void setSelectedNonConformitesTraitementItem(
   //			Set<Listitem> s) {
   //		this.selectedNonConformitesTraitementItem = s;
   //	}
   //
   //	public Set<Listitem> getSelectedNonConformitesCessionItem() {
   //		return selectedNonConformitesCessionItem;
   //	}
   //
   //	public void setSelectedNonConformitesCessionItem(
   //			Set<Listitem> s) {
   //		this.selectedNonConformitesCessionItem = s;
   //	}

   public String getObjetStatut(){
      return ObjectTypesFormatters.ILNObjectStatut(getObject().getObjetStatut());
   }

   /**
    * ANNOTATION INLINE<br>
    * Copie depuis AbstractObjectTabController
    * Récupère le controller de la fiche
    *
    * @return
    * @since 2.2.0
    */
   /*public FicheAnnotationInline getFicheAnnotationInline(){
      if(self.getFellowIfAny("ficheAnnotationInlineEchantillon") != null){
         return ((FicheAnnotationInline) self.getFellow("ficheAnnotationInlineEchantillon")
            .getFellow("fwinAnnotationInline").getAttributeOrFellow("fwinAnnotationInline$composer", true));
      }else{
         return null;
      }
   }*/

   /**
    * ANNOTATION INLINE<br>
    * Passe qq params au bloc inline annotation sans le dessiner la creation de la
    * fiche statique.
    *
    * @param controller
    * @since 2.2.0
    */
   /*@Override
   public void setObjectTabController(AbstractObjectTabController controller){
      super.setObjectTabController(controller);
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
