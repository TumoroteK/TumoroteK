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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.constraints.ConstEmail;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.xml.CoupleValeur;
import fr.aphp.tumorotek.manager.impl.xml.EnteteListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneListe;
import fr.aphp.tumorotek.manager.impl.xml.LigneParagraphe;
import fr.aphp.tumorotek.manager.impl.xml.ListeElement;
import fr.aphp.tumorotek.manager.impl.xml.Paragraphe;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche d'un établissement.
 * Controller créé le 17/12/2009.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class FicheEtablissement extends AbstractFicheCombineController
{

   private final Logger log = LoggerFactory.getLogger(FicheEtablissement.class);

   private static final long serialVersionUID = -8941746249248191447L;

   private Group groupServicesEtab;

   private Button addService;

   private Menubar menuBar;

   // Labels
   private Label nomLabel;

   private Label finessLabel;

   private Label categorieLabel;

   private Label localLabel;

   private Label adresseLabel;

   private Label cpLabel;

   private Label villeLabel;

   private Label paysLabel;

   private Label telLabel;

   private Label faxLabel;

   private Label mailLabel;

   private Label archiveLabel;

   // Editable components : mode d'édition ou de création.
   private Label nomRequired;

   private Textbox nomBox;

   private Textbox finessBox;

   private Listbox categorieBox;

   private Textbox adresseBox;

   private Textbox cpBox;

   private Textbox villeBox;

   private Textbox paysBox;

   private Textbox telBox;

   private Textbox faxBox;

   private Textbox mailBox;

   private Checkbox localBox;

   private Checkbox archiveBox;

   // Objets principaux
   private Etablissement etablissement;

   private Coordonnee coordonnee;

   private Coordonnee copyCoord;

   // Associations.
   private Categorie selectedCategorie;

   private List<Categorie> categories = new ArrayList<>();

   private List<Service> services = new ArrayList<>();

   // Variables formulaire.
   private String servicesGroupHeader = Labels.getLabel("etablissement.services");

   private String mode;

   private boolean cascadeArchive = false;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.etablissement");
      setDeletable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(
         new Component[] {this.nomLabel, this.finessLabel, this.categorieLabel, this.localLabel, this.adresseLabel, this.cpLabel,
            this.villeLabel, this.paysLabel, this.telLabel, this.faxLabel, this.mailLabel, this.archiveLabel, this.menuBar});

      setObjBoxsComponents(new Component[] {this.nomBox, this.finessBox, this.categorieBox, this.adresseBox, this.cpBox,
         this.villeBox, this.paysBox, this.telBox, this.faxBox, this.mailBox, this.localBox, this.archiveBox});

      setRequiredMarks(new Component[] {this.nomRequired});

      groupServicesEtab.setOpen(false);

      drawActionsForEtablissement();

      mode = "modification";

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.etablissement = (Etablissement) obj;

      if(this.etablissement.getCoordonnee() != null){
         this.coordonnee = this.etablissement.getCoordonnee();
      }else{
         this.coordonnee = new Coordonnee();
      }

      services.clear();

      if(etablissement.getEtablissementId() != null){
         services = ManagerLocator.getEtablissementManager().getServicesWithOrderManager(etablissement);
      }

      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("etablissement.services"));
      sb.append(" (");
      sb.append(services.size());
      sb.append(")");
      servicesGroupHeader = sb.toString();

      super.setObject(etablissement);
   }

   @Override
   public void cloneObject(){
      setClone(this.etablissement.clone());
      setCopyCoord(coordonnee.clone());
   }

   @Override
   public Etablissement getObject(){
      return this.etablissement;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public CollaborationsController getObjectTabController(){
      return (CollaborationsController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Etablissement());
      this.coordonnee = new Coordonnee();
      super.setNewObject();
   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      services.clear();
      servicesGroupHeader = Labels.getLabel("etablissement.services");

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      addService.setVisible(false);

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode statique.
    * Modifie le membre modeFiche.
    */
   public void switchToStaticMode(final String modeFiche){
      this.mode = modeFiche;
      switchToStaticMode();
   }

   @Override
   public void switchToStaticMode(){

      super.switchToStaticMode(this.etablissement.equals(new Etablissement()));

      if(this.etablissement.equals(new Etablissement())){
         menuBar.setVisible(false);
      }

      if(this.mode.equals("modification")){
         switchToModificationMode(this.etablissement.equals(new Etablissement()));
      }else if(this.mode.equals("select")){
         switchToSelectMode(this.etablissement.equals(new Etablissement()));
      }else if(this.mode.equals("details")){
         switchToDetailMode();
      }

      getBinder().loadComponent(self);

      groupServicesEtab.setOpen(false);
   }

   /**
    * Change mode de la fiche en mode edition.
    */
   @Override
   public void switchToEditMode(){

      super.switchToEditMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      addService.setVisible(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   public void switchToModificationMode(final boolean isNew){
      addNewC.setVisible(true);
      editC.setVisible(!isNew);
      deleteC.setVisible(!isNew);
      addService.setVisible(!isNew);
   }

   public void switchToSelectMode(final boolean isNew){
      addNewC.setVisible(true);
      editC.setVisible(false);
      deleteC.setVisible(false);
      addService.setVisible(!isNew);
   }

   public void switchToDetailMode(){
      addNewC.setVisible(false);
      editC.setVisible(false);
      deleteC.setVisible(false);
      addService.setVisible(false);
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public void createNewObject(){

      setEmptyToNulls();
      setFieldsToUpperCase();
      this.etablissement.setArchive(archiveBox.isChecked());
      this.etablissement.setLocal(localBox.isChecked());

      ManagerLocator.getEtablissementManager().createObjectManager(etablissement, coordonnee, selectedCategorie,
         SessionUtils.getLoggedUser(sessionScope));

      if(getListeCollaborations() != null){
         getListeCollaborations().updateTree();
         getListeCollaborations().openEtablissement(etablissement, false, true);
      }
   }

   @Override
   public void updateObject(){

      setEmptyToNulls();
      setFieldsToUpperCase();

      this.etablissement.setArchive(archiveBox.isChecked());
      this.etablissement.setLocal(localBox.isChecked());

      ManagerLocator.getEtablissementManager().updateObjectManager(etablissement, coordonnee, selectedCategorie,
         SessionUtils.getLoggedUser(sessionScope), cascadeArchive);

      if(getListeCollaborations() != null){
         getListeCollaborations().updateTree();
         getListeCollaborations().openEtablissement(etablissement, false, true);
      }
   }

   @Override
   public boolean prepareDeleteObject(){
      final boolean isUsed = ManagerLocator.getEtablissementManager().isUsedObjectManager(getObject());
      final boolean isReferenced = ManagerLocator.getEtablissementManager().isReferencedObjectManager(getObject());
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      setDeletable(true);
      if(isUsed){
         setDeleteMessage(Labels.getLabel("etablissement.deletion.isUsedCascade"));
      }
      if(isReferenced){
         setDeleteMessage(Labels.getLabel("etablissement.deletion.isReferencedCascade"));
         if(getObject().getArchive()){ // deja archivé
            setDeletable(false);
            setDeleteMessage(Labels.getLabel("etablissement.deletion.isAlreadyArchived"));
         }
      }
      setFantomable(!isReferenced);
      setCascadable(isUsed);
      return isReferenced;
   }

   @Override
   public void removeObject(final String comments){
      if(isCascadable()){
         if(isFantomable()){ // suppression possible
            ManagerLocator.getEtablissementManager().removeObjectCascadeManager(getObject(), comments,
               SessionUtils.getLoggedUser(sessionScope));
         }else{ // archivage
            getObject().setArchive(true);
            ManagerLocator.getEtablissementManager().updateObjectManager(getObject(), getObject().getCoordonnee(),
               getObject().getCategorie(), SessionUtils.getLoggedUser(sessionScope), true);
         }
      }else{
         ManagerLocator.getEtablissementManager().removeObjectManager(getObject(), comments,
            SessionUtils.getLoggedUser(sessionScope));
      }
   }

   @Override
   public void onLaterDelete(final Event event){
      try{
         removeObject((String) event.getData());

         if(getListeCollaborations() != null){
            getListeCollaborations().updateTree();
            getListeCollaborations().updateCollabsWithoutService();
         }

         if(isFantomable()){
            // clear form
            clearData();
         }else{
            if(getListeCollaborations() != null){
               getListeCollaborations().openEtablissement(etablissement, false, true);
            }
         }
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         // ferme wait message
         Clients.clearBusy();
      }
   }

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeCollaborations
    */
   private ListeCollaborations getListeCollaborations(){
      return getObjectTabController().getListeCollaborations();
   }

   @Override
   public void onClick$addNewC(){
      this.switchToCreateMode();
   }

   public void onClick$addService(){
      getObjectTabController().switchToFicheServiceCreationMode(this.etablissement);
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$createC(){
      super.onClick$createC();
   }

   @Override
   public void onClick$editC(){
      if(this.etablissement.getEtablissementId() != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$revertC(){
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){
      super.onClick$validateC();
   }

   /**
    * Cette méthode descend la barre de scroll au niveau du groupe
    * groupServicesEtab.
    */
   public void onOpen$groupServicesEtab(){
      final String id = groupServicesEtab.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * nomBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$nomBox(){
      nomBox.setValue(nomBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * villeBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$villeBox(){
      villeBox.setValue(villeBox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * paysBox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$paysBox(){
      paysBox.setValue(paysBox.getValue().toUpperCase().trim());
   }

   @Override
   public void setFieldsToUpperCase(){
      if(this.etablissement.getNom() != null){
         this.etablissement.setNom(this.etablissement.getNom().toUpperCase().trim());
      }

      if(this.coordonnee.getVille() != null){
         this.coordonnee.setVille(this.coordonnee.getVille().toUpperCase().trim());
      }

      if(this.coordonnee.getPays() != null){
         this.coordonnee.setPays(this.coordonnee.getPays().toUpperCase().trim());
      }
   }

   /**
    * Check sur la checkbox archiveBox.
    */
   public void onCheck$archiveBox(){
      // on demande confirmation à l'utilisateur
      // de l'activation/inactivation
      if(archiveBox.isChecked()){
         if(Messagebox.show(Labels.getLabel("message.archive.message.etablissement"), Labels.getLabel("message.archive.title"),
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
            cascadeArchive = true;
         }else{
            cascadeArchive = false;
            archiveBox.setChecked(false);
         }
      }else{
         if(Messagebox.show(Labels.getLabel("message.not.archive.message.etablissement"),
            Labels.getLabel("messagenot.not.archive.title"), Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION) == Messagebox.YES){
            cascadeArchive = true;
         }else{
            cascadeArchive = false;
            archiveBox.setChecked(true);
         }
      }
   }

   @Override
   public void setEmptyToNulls(){

      if(this.etablissement.getFiness().equals("")){
         this.etablissement.setFiness(null);
      }

      if(this.coordonnee.getAdresse().equals("")){
         this.coordonnee.setAdresse(null);
      }
      if(this.coordonnee.getCp().equals("")){
         this.coordonnee.setCp(null);
      }
      if(this.coordonnee.getVille().equals("")){
         this.coordonnee.setVille(null);
      }
      if(this.coordonnee.getPays().equals("")){
         this.coordonnee.setPays(null);
      }
      if(this.coordonnee.getTel().equals("")){
         this.coordonnee.setTel(null);
      }
      if(this.coordonnee.getFax().equals("")){
         this.coordonnee.setFax(null);
      }
      if(this.coordonnee.getMail().equals("")){
         this.coordonnee.setMail(null);
      }

   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){
      categories = ManagerLocator.getCategorieManager().findAllObjectsManager();
      categories.add(0, null);
      selectedCategorie = this.etablissement.getCategorie();

      archiveBox.setChecked(this.etablissement.getArchive());

      localBox.setChecked(this.etablissement.isLocal());

      cascadeArchive = false;
   }

   //	public void revertEtablissement() {
   //		etablissement.setEtablissementId(copyEtab.getEtablissementId());
   //		etablissement.setCoordonnee(copyEtab.getCoordonnee());
   //		etablissement.setCategorie(copyEtab.getCategorie());
   //		etablissement.setNom(copyEtab.getNom());
   //		etablissement.setFiness(copyEtab.getFiness());
   //		etablissement.setLocal(copyEtab.isLocal());
   //		etablissement.setArchive(copyEtab.getArchive());
   //		etablissement.setServices(copyEtab.getServices());
   //		etablissement.setCollaborateurs(copyEtab.getCollaborateurs());
   //	}
   //
   //	public void revertCoordonnee() {
   //		coordonnee.setCoordonneeId(copyCoord.getCoordonneeId());
   //		coordonnee.setAdresse(copyCoord.getAdresse());
   //		coordonnee.setCp(copyCoord.getCp());
   //		coordonnee.setVille(copyCoord.getVille());
   //		coordonnee.setPays(copyCoord.getPays());
   //		coordonnee.setTel(copyCoord.getTel());
   //		coordonnee.setFax(copyCoord.getFax());
   //		coordonnee.setMail(copyCoord.getMail());
   //	}

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(finessBox);
      Clients.clearWrongValue(adresseBox);
      Clients.clearWrongValue(cpBox);
      Clients.clearWrongValue(villeBox);
      Clients.clearWrongValue(paysBox);
      Clients.clearWrongValue(telBox);
      Clients.clearWrongValue(faxBox);
      Clients.clearWrongValue(mailBox);
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForEtablissement(){
      drawActionsButtons("Collaborateur");
      addService.setDisabled(!drawActionOnOneButton("Collaborateur", "Creation"));
   }

   /**
    * Méthode pour l'impression de la fiche d'un établissement.
    */
   public void onClick$print(){
      if(this.etablissement != null){
         // création du document XML contenant les données à imprimer
         final Document document = ManagerLocator.getXmlUtils().createJDomDocument();
         final Element root = document.getRootElement();
         final Element pageXML = ManagerLocator.getXmlUtils().addPage(root, null);
         ManagerLocator.getXmlUtils().addHautDePage(root, Labels.getLabel("ficheEtablissement.panel.title"), false, null);
         ManagerLocator.getXmlUtils().addBasDePage(root, "");

         addInfosGeneralesToPrint(pageXML);
         addCoordonneesToPrint(pageXML);
         addServicesToPrint(pageXML);

         // Transformation du document en fichier
         byte[] dl = null;
         try{
            dl = ManagerLocator.getXmlUtils().creerPdf(document);
         }catch(final Exception e){
            log.error(e);
         }

         // envoie du fichier à imprimer à l'utilisateur
         if(dl != null){
            Filedownload.save(dl, "application/pdf", "etablissement.pdf");
            dl = null;
         }
      }
   }

   /**
    * Ajout les infos générales à imprimer.
    * @param page
    */
   public void addInfosGeneralesToPrint(final Element page){
      String tmp = "";
      final CoupleValeur cpVide = new CoupleValeur("", "");
      // Nom
      if(etablissement.getNom() != null){
         tmp = etablissement.getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp1 = new CoupleValeur(Labels.getLabel("etablissement.nom"), tmp);
      final LigneParagraphe li1 = new LigneParagraphe("", new CoupleValeur[] {cp1, cpVide});
      // Finess
      if(etablissement.getFiness() != null){
         tmp = etablissement.getFiness();
      }else{
         tmp = "";
      }
      final CoupleValeur cp2 = new CoupleValeur(Labels.getLabel("etablissement.finess"), tmp);
      final LigneParagraphe li2 = new LigneParagraphe("", new CoupleValeur[] {cp2, cpVide});
      // Categorie
      if(etablissement.getCategorie() != null){
         tmp = etablissement.getCategorie().getNom();
      }else{
         tmp = "";
      }
      final CoupleValeur cp3 = new CoupleValeur(Labels.getLabel("etablissement.categorie"), tmp);
      final LigneParagraphe li3 = new LigneParagraphe("", new CoupleValeur[] {cp3, cpVide});
      // Inactif
      if(etablissement.getArchive() != null){
         tmp = ObjectTypesFormatters.booleanLitteralFormatter(this.etablissement.getArchive());
      }else{
         tmp = "";
      }
      final CoupleValeur cp4 = new CoupleValeur(Labels.getLabel("etablissement.archive"), tmp);
      final LigneParagraphe li4 = new LigneParagraphe("", new CoupleValeur[] {cp4, cpVide});
      final Paragraphe par1 = new Paragraphe(null, new LigneParagraphe[] {li1, li2, li3, li4}, null, null, null);
      ManagerLocator.getXmlUtils().addParagraphe(page, par1);
   }

   /**
    * Ajout les coordonnées à imprimer.
    * @param page
    */
   public void addCoordonneesToPrint(final Element page){

      if(this.etablissement.getCoordonnee() != null){
         String tmp = "";
         final CoupleValeur cpVide = new CoupleValeur("", "");
         // Coordonnées adresse
         if(etablissement.getCoordonnee().getAdresse() != null){
            tmp = etablissement.getCoordonnee().getAdresse();
         }else{
            tmp = "";
         }
         final CoupleValeur cp5 = new CoupleValeur(Labels.getLabel("coordonnee.adresse"), tmp);
         final LigneParagraphe li5 = new LigneParagraphe("", new CoupleValeur[] {cp5, cpVide});
         // Coordonnées code postal
         if(etablissement.getCoordonnee().getCp() != null){
            tmp = etablissement.getCoordonnee().getCp();
         }else{
            tmp = "";
         }
         final CoupleValeur cp6 = new CoupleValeur(Labels.getLabel("coordonnee.cp"), tmp);
         final LigneParagraphe li6 = new LigneParagraphe("", new CoupleValeur[] {cp6, cpVide});
         // Coordonnées ville
         if(etablissement.getCoordonnee().getVille() != null){
            tmp = etablissement.getCoordonnee().getVille();
         }else{
            tmp = "";
         }
         final CoupleValeur cp7 = new CoupleValeur(Labels.getLabel("coordonnee.ville"), tmp);
         final LigneParagraphe li7 = new LigneParagraphe("", new CoupleValeur[] {cp7, cpVide});
         // Coordonnées pays
         if(etablissement.getCoordonnee().getPays() != null){
            tmp = etablissement.getCoordonnee().getPays();
         }else{
            tmp = "";
         }
         final CoupleValeur cp8 = new CoupleValeur(Labels.getLabel("coordonnee.pays"), tmp);
         final LigneParagraphe li8 = new LigneParagraphe("", new CoupleValeur[] {cp8, cpVide});
         // Coordonnées tel
         if(etablissement.getCoordonnee().getTel() != null){
            tmp = etablissement.getCoordonnee().getTel();
         }else{
            tmp = "";
         }
         final CoupleValeur cp9 = new CoupleValeur(Labels.getLabel("coordonnee.telephone"), tmp);
         final LigneParagraphe li9 = new LigneParagraphe("", new CoupleValeur[] {cp9, cpVide});
         // Coordonnées fax
         if(etablissement.getCoordonnee().getFax() != null){
            tmp = etablissement.getCoordonnee().getFax();
         }else{
            tmp = "";
         }
         final CoupleValeur cp10 = new CoupleValeur(Labels.getLabel("coordonnee.fax"), tmp);
         final LigneParagraphe li10 = new LigneParagraphe("", new CoupleValeur[] {cp10, cpVide});
         // Coordonnées mail
         if(etablissement.getCoordonnee().getMail() != null){
            tmp = etablissement.getCoordonnee().getMail();
         }else{
            tmp = "";
         }
         final CoupleValeur cp11 = new CoupleValeur(Labels.getLabel("coordonnee.mail"), tmp);
         final LigneParagraphe li11 = new LigneParagraphe("", new CoupleValeur[] {cp11, cpVide});
         final Paragraphe par2 = new Paragraphe(Labels.getLabel("coordonnee.group.infos"),
            new LigneParagraphe[] {li5, li6, li7, li8, li9, li10, li11}, null, null, null);
         ManagerLocator.getXmlUtils().addParagraphe(page, par2);
      }
   }

   /**
    * Ajout les services à imprimer.
    * @param page
    */
   public void addServicesToPrint(final Element page){
      // Services
      final EnteteListe entetes = new EnteteListe(new String[] {Labels.getLabel("service.nom")});
      final LigneListe[] liste = new LigneListe[services.size()];
      for(int i = 0; i < services.size(); i++){
         final String[] valeurs = new String[1];
         if(services.get(i).getNom() != null){
            valeurs[0] = services.get(i).getNom();
         }else{
            valeurs[0] = "-";
         }
         final LigneListe ligne = new LigneListe(valeurs);
         liste[i] = ligne;
      }
      final ListeElement listeSites = new ListeElement(null, entetes, liste);
      // ajout du paragraphe
      final StringBuffer titre = new StringBuffer();
      titre.append(Labels.getLabel("etablissement.services"));
      titre.append(" (");
      titre.append(services.size());
      titre.append(")");
      final Paragraphe par = new Paragraphe(titre.toString(), null, null, null, listeSites);
      ManagerLocator.getXmlUtils().addParagraphe(page, par);
   }

   public void onClick$serviceNom(final Event event){
      final Service serv = (Service) AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(getListeCollaborations() != null){
         getListeCollaborations().openService(serv, true, true);
      }
   }

   /*************************************************/
   /**               ACCESSEURS                    **/
   /*************************************************/

   public Etablissement getEtablissement(){
      return etablissement;
   }

   public void setEtablissement(final Etablissement e){
      setObject(e);
   }

   public Coordonnee getCoordonnee(){
      return coordonnee;
   }

   public void setCoordonnee(final Coordonnee c){
      this.coordonnee = c;
   }

   public Categorie getSelectedCategorie(){
      return selectedCategorie;
   }

   public void setSelectedCategorie(final Categorie selected){
      this.selectedCategorie = selected;
   }

   public List<Categorie> getCategories(){
      return categories;
   }

   public Coordonnee getCopyCoord(){
      return copyCoord;
   }

   public void setCopyCoord(final Coordonnee copy){
      this.copyCoord = copy;
   }

   public List<Service> getServices(){
      return services;
   }

   public String getServicesGroupHeader(){
      return servicesGroupHeader;
   }

   /**
    * Formate la valeur du champ local.
    * @return Oui ou non.
    */
   public String getLocalFormated(){

      if(etablissement != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.etablissement.isLocal());
      }
      return "";
   }

   /**
    * Formate la valeur du champ Archive.
    * @return Oui ou non.
    */
   public String getArchiveFormated(){

      if(this.etablissement != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.etablissement.getArchive());
      }
      return "";
   }

   public String getMode(){
      return mode;
   }

   public void setMode(final String m){
      this.mode = m;
   }

   public boolean isCascadeArchive(){
      return cascadeArchive;
   }

   public void setCascadeArchive(final boolean cascadeA){
      this.cascadeArchive = cascadeA;
   }

   public Constraint getNomConstraint(){
      return ContexteConstraints.getNomConstraint();
   }

   public Constraint getFinessConstraint(){
      return ContexteConstraints.getFinessConstraint();
   }

   public ConstText getAddrConstraint(){
      return ContexteConstraints.getAddrConstraint();
   }

   public ConstWord getVillePaysConstraint(){
      return ContexteConstraints.getVillePaysConstraint();
   }

   public ConstCode getCpConstraint(){
      return ContexteConstraints.getCpConstraint();
   }

   public ConstCode getTelFaxConstraint(){
      return ContexteConstraints.getTefFaxConstraint();
   }

   public ConstEmail getEmailConstraint(){
      return ContexteConstraints.getEmailConstraint();
   }

   @Override
   public String getDeleteWaitLabel(){
      if(isFantomable()){
         return Labels.getLabel("deletion.general.wait");
      }
      return Labels.getLabel("archivage.general.wait");
   }
}