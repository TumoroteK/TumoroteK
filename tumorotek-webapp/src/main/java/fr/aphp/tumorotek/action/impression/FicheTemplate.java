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
package fr.aphp.tumorotek.action.impression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Group;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractImpressionController;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.io.FicheAffichage;
import fr.aphp.tumorotek.decorator.BlocImpressionDecorator;
import fr.aphp.tumorotek.decorator.BlocImpressionRowRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.ETemplateType;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheTemplate extends AbstractImpressionController
{

   private final Log log = LogFactory.getLog(FicheTemplate.class);

   private static final long serialVersionUID = -8743924081789346031L;

   private Media uploadedMedia;

   // Définition des groupes d'affichages

   /**
    * Groupe contenant la liste des blocs d'impression
    */
   private Group groupContenu;

   /**
    * Groupe contenant la liste des couples cles champs
    */

   private Group groupClesChamps;

   // Manipulation des lignes pour l'affichage / masquage

   /**
    * Ligne pour l'ajout d'un fichier
    */
   private Row fichierRow;

   private Row enTeteRow;

   private Row piedPageRow;

   /**
    * Ligne pour l'ajout (+) des blocs impression
    */
   private Row defineBlocsRow;

   //  Static Components pour le mode static.

   private Label nomLabel;

   private Label typeLabel;

   private Label entiteLabel;

   private Label descriptionLabel;

   /**
    * Message informatif affiché sous le bouton Upload
    */
   private Label messageInfoUploadBtn;

   private Label enteteLabel;

   private Label piedPageLabel;

   private Grid contenuStaticGrid;

   private Grid cleImpressionEditGrid;

   private Grid cleImpressionStaticGrid;

   //  Editable components : mode d'édition ou de création.

   private Textbox nomBox;

   private Listbox typesBox;

   private Textbox descriptionBox;

   private Textbox enteteBox;

   private Textbox piedPageBox;

   private Label nomRequired;

   private Label typeRequired;

   private Label entiteRequired;

   private Button uploadFichierBtn;

   private Listbox entitesBox;

   /**
    *  Objets Principaux.
    */
   private Template template;

   /**
    *  Associations.
    */
   private List<String> types = new ArrayList<>();

   private String selectedType;

   private List<Entite> entites = new ArrayList<>();

   private Entite selectedEntite;

   private List<CleImpression> cleImpressionList = new ArrayList<>();

   /**
   * Liste des champs à afficher pour le résultat de la recherche
   */
   private Champ selectedChamp;

   /**
    * Variables formulaire.
    */

   private final BlocImpressionRowRenderer blocImpressionRenderer = new BlocImpressionRowRenderer(false);

   private final BlocImpressionRowRenderer blocImpressionRendererEdit = new BlocImpressionRowRenderer(true);

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {nomLabel, typeLabel, entiteLabel, descriptionLabel, enteteLabel, piedPageLabel,
         groupClesChamps, cleImpressionStaticGrid, contenuStaticGrid, groupContenu});

      setObjBoxsComponents(new Component[] {nomBox, typesBox, descriptionBox, enteteBox, piedPageBox, cleImpressionEditGrid,
         defineBlocsRow, entitesBox, contenuEditGrid, uploadFichierBtn});

      setRequiredMarks(new Component[] {nomRequired, typeRequired, entiteRequired});

      initEditableMode();

      drawActionsForTemplate();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();
   }

   /**
    * Méthode pour l'initialisation du mode d'édition : récupération du contenu
    * des listes déroulantes (types, qualités...).
    */
   public void initEditableMode(){
      // init des types
      types = new ArrayList<>();
      types.add(null);
      types.addAll(ETemplateType.getTypeList());
      selectedType = types.get(0);
      // init des entités
      entites = new ArrayList<>();
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0));
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0));
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
      entites.add(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0));
      selectedEntite = entites.get(0);
   }

   @Override
   public TKdataObject getObject(){
      return this.template;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.template = Template.class.cast(obj);
      blocImpressionRenderer.setTemplate(template);
      blocImpressionsDecorated = new ArrayList<>();
      switchToStaticMode();

      super.setObject(obj);
   }

   /**
    * Méthode générant la liste des BlocImpressionDecorators en
    * fonction de l'ordre des objets en base.
    */
   public void generateListeBlocs(){
      final List<BlocImpressionTemplate> temps =
         ManagerLocator.getBlocImpressionTemplateManager().findByTemplateManager(template);

      final List<TableAnnotationTemplate> tables =
         ManagerLocator.getTableAnnotationTemplateManager().findByTemplateManager(template);

      int i = 0;
      int j = 0;
      // on parcourt les 2 listes en entier
      while(i < temps.size() || j < tables.size()){
         BlocImpressionTemplate bloc = null;
         TableAnnotationTemplate anno = null;
         if(i < temps.size()){
            bloc = temps.get(i);
         }
         if(j < tables.size()){
            anno = tables.get(j);
         }

         // si on arrive a extraire un bloc et une annotation
         if(bloc != null && anno != null){
            // on ajoute à la liste finale celui qui a le +
            // petit ordre
            if(anno.getOrdre() < bloc.getOrdre()){
               final BlocImpressionDecorator deco = new BlocImpressionDecorator(null, anno.getTableAnnotation(), template);
               blocImpressionsDecorated.add(deco);
               ++j;
            }else{
               final BlocImpressionDecorator deco = new BlocImpressionDecorator(bloc.getBlocImpression(), null, template);
               blocImpressionsDecorated.add(deco);
               ++i;
            }
         }else if(bloc != null){
            // s'il ne reste que des blocs
            final BlocImpressionDecorator deco = new BlocImpressionDecorator(bloc.getBlocImpression(), null, template);
            blocImpressionsDecorated.add(deco);
            ++i;
         }else if(anno != null){
            // s'il ne reste que des annotations
            final BlocImpressionDecorator deco = new BlocImpressionDecorator(null, anno.getTableAnnotation(), template);
            blocImpressionsDecorated.add(deco);
            ++j;
         }
      }

      getBinder().loadAttribute(contenuStaticGrid, "model");
      getBinder().loadComponent(contenuStaticGrid);
   }

   /**
    * Génère la liste des CleImpression
    */
   public void generateListeCles(){
      final List<CleImpression> cleImpressionList = ManagerLocator.getCleManager().findByTemplateManager(this.template);
      this.cleImpressionList = cleImpressionList;
   }

   @Override
   public void setNewObject(){
      setObject(new Template());
      super.setNewObject();
   }

   @Override
   public void cloneObject(){
      setClone(this.template.clone());
   }

   /**
    * Evenement lors de la selection du type de template
    */
   public void onSelect$typesBox(){
      // Obliger de 'sauvegarder' les champs au cas où ils soient renseignés avant, sinon ils se perdent
      this.template.setNom(nomBox.getText());
      this.template.setEntite(selectedEntite);
      this.template.setDescription(descriptionBox.getText());
      this.template.setEnTete(enteteBox.getText());
      this.template.setPiedPage(piedPageBox.getText());
      if(null != selectedType){
         this.template.setType(ETemplateType.valueOf(selectedType));
         if(ETemplateType.BLOC == this.template.getType()){
            enTeteRow.setVisible(true);
            piedPageRow.setVisible(true);
            fichierRow.setVisible(false);
            defineBlocsRow.setVisible(true);
            groupClesChamps.setVisible(false);
            cleImpressionEditGrid.setVisible(false);
         }else if(ETemplateType.DOC == this.template.getType()){
            enTeteRow.setVisible(false);
            piedPageRow.setVisible(false);
            fichierRow.setVisible(true);
            messageInfoUploadBtn.setValue(Labels.getLabel("template.messages.fichier"));
            groupContenu.setVisible(false);
            defineBlocsRow.setVisible(false);
            groupClesChamps.setVisible(true);
            cleImpressionEditGrid.setVisible(true);
         }
      }else{
         enTeteRow.setVisible(false);
         piedPageRow.setVisible(false);
         fichierRow.setVisible(false);
         groupContenu.setVisible(false);
         defineBlocsRow.setVisible(false);
         contenuEditGrid.setVisible(false);
         groupClesChamps.setVisible(false);
         cleImpressionEditGrid.setVisible(false);
      }
   }

   //   /**
   //    * TODO faire de cette méthode une action générale accessible à plusieurs endroits ?
   //    * PopUp window appelée permettant la sélection des champs à associer à une clé.
   //    * @param page dans laquelle inclure la modale
   //    * @param oldSelected Liste des champs déjà sélectionnés.
   //    * @bank Banque sur laquelle effectuer la recherche
   //    */
   //   public void openChampsAffichageWindow(Page page, List<Champ> oldSelected, Banque bank){
   //      if(!isBlockModal()){
   //
   //         setBlockModal(true);
   //
   //         // nouvelle fenêtre
   //         final Window win = new Window();
   //         win.setVisible(false);
   //         win.setId("champsAffichageWindow");
   //         win.setPage(page);
   //         win.setMaximizable(true);
   //         win.setSizable(true);
   //         win.setTitle(Labels.getLabel("champs.affichage.modale.title"));
   //         win.setBorder("normal");
   //         win.setWidth("500px");
   //         int height = 510;
   //         win.setHeight(String.valueOf(height) + "px");
   //         win.setClosable(true);
   //
   //         final HtmlMacroComponent ua;
   //         ua = (HtmlMacroComponent) page.getComponentDefinition("champsAffichageModale", false).newInstance(page, null);
   //         ua.setParent(win);
   //         ua.setId("champsAffichageModaleComponent");
   //         ua.applyProperties();
   //         ua.afterCompose();
   //
   //         ((FicheChampsAffichageModale) ua.getFellow("fwinChampsAffichageModale")
   //            .getAttributeOrFellow("fwinChampsAffichageModale$composer", true)).init(oldSelected, self, bank);
   //         ua.setVisible(false);
   //
   //         ((FicheChampsAffichageModale) ua.getFellow("fwinChampsAffichageModale")
   //            .getAttributeOrFellow("fwinChampsAffichageModale$composer", true)).getTtm().setMultiple(false);
   //
   //         win.addEventListener("onTimed", new EventListener<Event>()
   //         {
   //            public void onEvent(Event event) throws Exception{
   //               //progress.detach();
   //               ua.setVisible(true);
   //            }
   //         });
   //
   //         Timer timer = new Timer();
   //         timer.setDelay(500);
   //         timer.setRepeats(false);
   //         timer.addForward("onTimer", timer.getParent(), "onTimed");
   //         win.appendChild(timer);
   //         timer.start();
   //
   //         try{
   //            win.onModal();
   //            setBlockModal(false);
   //
   //         }catch(SuspendNotAllowedException e){
   //            log.error(e);
   //         }
   //      }
   //   }

   /**
    * Méthode appelée une fois que l'utilisateur a choisi les champs
    * à afficher.
    * @param e
    */
   public void onGetChamps(final Event e){
      this.selectedChamp = null;
      if(e.getData() != null){
         final Champ currChamp = Champ.class.cast(List.class.cast(e.getData()).get(0));
         Champ sousChampTmp = null;

         if(currChamp.getChampEntite() != null && currChamp.getChampEntite().getQueryChamp() != null){
            sousChampTmp = new Champ(currChamp.getChampEntite().getQueryChamp());
            sousChampTmp.setChampParent(currChamp);
         }

         if(sousChampTmp != null){
            this.selectedChamp = sousChampTmp;
         }else{
            this.selectedChamp = currChamp;
         }

      }
   }

   /**
    * Click sur l'édition d'une cleImpression permet de choisir/modifier
    * le champ à lié à une clé
    * @param event Clic sur une image d'édition
    */
   public void onClick$editImg(final Event event){
      // on récupère la cleImpression que l'utilisateur veut éditer
      final CleImpression cleToEdit = (CleImpression) AbstractListeController2.getBindingData((ForwardEvent) event, false);
      new FicheAffichage().openChampsAffichageWindow(page, self, new ArrayList<Champ>(),
         SessionUtils.getSelectedBanques(sessionScope).get(0), false);
      //      openChampsAffichageWindow(page, new ArrayList<Champ>(), SessionUtils.getSelectedBanques(sessionScope).get(0));

      // Ajout du champ sélectionné
      if(null != selectedChamp){
         // Si la clef possédait un ancien champs, on récupère l'ID afin de le mettre à jour.
         if(null != cleToEdit.getChamp() && null != cleToEdit.getChamp().getChampId()){
            selectedChamp.setChampId(cleToEdit.getChamp().getChampId());
         }
         cleToEdit.setChamp(selectedChamp);
         // Evenement envoyé à la liste des clefs pour rafraichissement des valeurs
         Events.echoEvent("onListChange", cleImpressionEditGrid, null);
      }

   }

   /**
    * Evenement click bouton upload fichier : Cherche les clé [[CLE]] dans le fichier
    * puis les affiche pour que l'utilisateur leur associe un champ
    */
   public void onClick$uploadFichierBtn(){

      // On cache le type du template
      typesBox.setVisible(false);
      typeRequired.setVisible(false);
      typeLabel.setVisible(true);

      messageInfoUploadBtn.setValue(Labels.getLabel("template.messages.fichier"));

      try{
         uploadedMedia = Fileupload.get();

         if(uploadedMedia == null){
            return;
         }

         // Récupération de l'extension et du nom du fichier
         final String fileStr = uploadedMedia.getName();
         final String fileExtension = fileStr.substring(fileStr.lastIndexOf("."));

         // Traitement d'un docx
         if(".docx".equals(fileExtension)){
            final XWPFDocument document = new XWPFDocument(uploadedMedia.getStreamData());

            //Extraction des clefs du document

            // Regex pour rechercher les clés sous forme: [[CLE]]
            //TODO 7007168 permettre à l'utilisateur d'utiliser un pattern différent ?
            final Pattern clePattern = Pattern.compile("\\[{2}(.*?)\\]{2}");
            final List<String> clesDocListe =
               new ArrayList<>(TemplateUtils.extractStringsInFileFromPattern(document, clePattern));

            // Création de cleImpressions et ajout au template
            final List<CleImpression> newCleImpressionList = new ArrayList<>();
            if(null != this.cleImpressionList && this.cleImpressionList.size() > 0){
               //Pour une modification, récupérer les clés déjà existantes/renseignées
               for(final String cleStr : clesDocListe){
                  Boolean keyFound = false;
                  for(final CleImpression oldCleImpr : this.cleImpressionList){
                     if(oldCleImpr.getNom().equals(cleStr)){
                        newCleImpressionList.add(oldCleImpr);
                        keyFound = true;
                        break;
                     }
                  }
                  if(!keyFound){
                     final CleImpression cleImpr = new CleImpression();
                     cleImpr.setNom(cleStr);
                     cleImpr.setTemplate(this.template);
                     newCleImpressionList.add(cleImpr);
                  }
               }
            }else{
               //Pour une création ou si l'ancien template ne contenait pas de clef, récupérer les clés déjà existantes/renseignées
               for(final String cleStr : clesDocListe){
                  final CleImpression cleImpr = new CleImpression();
                  cleImpr.setNom(cleStr);
                  cleImpr.setTemplate(this.template);
                  newCleImpressionList.add(cleImpr);
               }

            }

            this.cleImpressionList = newCleImpressionList;

            messageInfoUploadBtn.setValue("");
         }else{
            // Pas d'autres traitements possible pour le moment
            messageInfoUploadBtn.setValue("Le fichier doit être un .docx");
            messageInfoUploadBtn.setStyle("{color:red}");
            return;
         }

      }catch(final Exception e){
         log.error(e);
      }
   }

   @Override
   public void onClick$addNewC(){
      this.switchToCreateMode();
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$createC(){

      if(ETemplateType.BLOC == this.template.getType()){
         if(!checksBlocValid()){
            throw new WrongValueException(createC, Labels.getLabel("impression.error"));
         }
      }else if(ETemplateType.DOC == this.template.getType()){
         this.template.setEnTete("");
         this.template.setPiedPage("");
         this.template.setBlocImpressionTemplates(null);
         this.template.setChampImprimes(null);
         this.template.setTableAnnotationTemplates(null);

         if(null != uploadedMedia){
            // Enregistrement du modèle dans le FS
            TemplateUtils.saveDocTemplate(uploadedMedia, template);
         }
      }

      Clients.clearWrongValue(createC);
      Clients.showBusy(Labels.getLabel("template.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         // log.debug("fiche: obj modifie: " + this.template.toString());

         if(getObjectTabController().getListe() != null){
            // ajout du template à la liste
            getObjectTabController().getListe().addToObjectList(this.template);
         }
         setObject(template);
         this.switchToStaticMode();
      }catch(final RuntimeException e){
         log.error(e);
         throw e;
      }finally{
         Clients.clearBusy();
      }
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         // log.debug("fiche: obj modifie: " + this.template.toString());

         if(getObjectTabController().getListe() != null){
            // update du template dans la liste
            getObjectTabController().getListe().updateObjectGridList(this.template);
         }
         setObject(template);
         this.switchToStaticMode();
      }catch(final RuntimeException e){
         log.error(e);
         throw e;
      }finally{
         Clients.clearBusy();
      }
   }

   @Override
   public void onClick$deleteC(){
      if(this.template != null){

         if(Messagebox.show(
            ObjectTypesFormatters.getLabel("message.deletion.message",
               new String[] {Labels.getLabel("message.deletion.template")}),
            Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){

            //Suppression du fichier DOC
            if(null != template.getFichier()){
               TemplateUtils.deleteDocTemplate(template.getFichier());
            }

            // suppression de l'objet
            ManagerLocator.getTemplateManager().removeObjectManager(template);
            // log.debug("fiche: obj supprime: " + this.template);

            // on vérifie que l'on retrouve bien la page
            // contenant la liste des objets
            if(getObjectTabController().getListe() != null){

               // on enlève l'objet de la liste
               getObjectTabController().getListe().removeObjectAndUpdateList(this.template);
            }
            // clear form
            clearData();
         }

      }
   }

   @Override
   public void onClick$editC(){
      this.switchToEditMode();
   }

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void onClick$validateC(){
      if(ETemplateType.BLOC == this.template.getType()){
         if(!checksBlocValid()){
            throw new WrongValueException(createC, Labels.getLabel("impression.error"));
         }
      }else if(ETemplateType.DOC == this.template.getType()){
         this.template.setEnTete("");
         this.template.setPiedPage("");
         this.template.setBlocImpressionTemplates(null);
         this.template.setChampImprimes(null);
         this.template.setTableAnnotationTemplates(null);

         //Supprimer/Remplacer l'ancien modèle si modification
         if(null != uploadedMedia){
            if(null != template.getFichier()){
               TemplateUtils.deleteDocTemplate(template.getFichier());
            }
            // Enregistrement du modèle dans le FS
            TemplateUtils.saveDocTemplate(uploadedMedia, template);
         }
      }

      Clients.clearWrongValue(validateC);
      Clients.showBusy(Labels.getLabel("template.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   @Override
   public void setEmptyToNulls(){
      if(this.template.getDescription().equals("")){
         this.template.setDescription(null);
      }

      if(this.template.getEnTete().equals("")){
         this.template.setEnTete(null);
      }

      if(this.template.getPiedPage().equals("")){
         this.template.setPiedPage(null);
      }
   }

   @Override
   public void createNewObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      // listes des objets associés au template
      final List<BlocImpressionTemplate> blocs = new ArrayList<>();
      final List<ChampImprime> champs = new ArrayList<>();
      final List<TableAnnotationTemplate> tables = new ArrayList<>();

      int ordre = 0;
      // pour chaque bloc
      for(int i = 0; i < blocImpressionsDecorated.size(); i++){
         final BlocImpressionDecorator deco = blocImpressionsDecorated.get(i);
         // si le bloc doit être imprimé
         if(deco.getImprimer()){
            ++ordre;
            // si c'est un bloc d'impression
            if(deco.getBlocImpression() != null){
               // nouveau BlocImpressionTemplate
               final BlocImpressionTemplate bit = new BlocImpressionTemplate();
               bit.setOrdre(ordre);
               bit.setBlocImpression(deco.getBlocImpression());
               bit.setTemplate(template);
               blocs.add(bit);

               // si le bloc est une liste, on va traiter les
               // champs a afficher
               if(deco.getBlocImpression().getIsListe()){
                  int ordreChamps = 0;
                  for(int j = 0; j < deco.getChampEntites().size(); j++){
                     ++ordreChamps;
                     final ChampEntite champ = deco.getChampEntites().get(j);
                     // nouveau ChampImprime
                     final ChampImprime ci = new ChampImprime();
                     ci.setBlocImpression(deco.getBlocImpression());
                     ci.setChampEntite(champ);
                     ci.setTemplate(template);
                     ci.setOrdre(ordreChamps);
                     champs.add(ci);
                  }
               }
            }else if(deco.getTableAnnotation() != null){
               // si c'est une annotation
               // nouveau TableAnnotationTemplate
               final TableAnnotationTemplate tat = new TableAnnotationTemplate();
               tat.setOrdre(ordre);
               tat.setTableAnnotation(deco.getTableAnnotation());
               tat.setTemplate(template);
               tables.add(tat);
            }
         }
      }

      // create de l'objet
      ManagerLocator.getTemplateManager().createObjectManager(template, SessionUtils.getSelectedBanques(sessionScope).get(0),
         selectedEntite, blocs, champs, tables, cleImpressionList);
   }

   @Override
   public void updateObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();

      // listes des objets associés au template
      final List<BlocImpressionTemplate> blocs = new ArrayList<>();
      final List<ChampImprime> champs = new ArrayList<>();
      final List<TableAnnotationTemplate> tables = new ArrayList<>();
      final List<BlocImpressionTemplate> blocsToCreate = new ArrayList<>();
      final List<ChampImprime> champsToCreate = new ArrayList<>();
      final List<TableAnnotationTemplate> tablesToCreate = new ArrayList<>();
      final List<BlocImpressionTemplate> blocsOld =
         ManagerLocator.getBlocImpressionTemplateManager().findByTemplateManager(template);
      final List<ChampImprime> champsOld = ManagerLocator.getChampImprimeManager().findByTemplateManager(template);
      final List<TableAnnotationTemplate> tablesOld =
         ManagerLocator.getTableAnnotationTemplateManager().findByTemplateManager(template);
      final List<CleImpression> cles = new ArrayList<>();
      final List<CleImpression> clesToCreate = new ArrayList<>();
      final List<CleImpression> clesOld = ManagerLocator.getCleManager().findByTemplateManager(template);

      int ordre = 0;
      // pour chaque bloc
      for(int i = 0; i < blocImpressionsDecorated.size(); i++){
         final BlocImpressionDecorator deco = blocImpressionsDecorated.get(i);
         // si le bloc doit être imprimé
         if(deco.getImprimer()){
            ++ordre;
            // si c'est un bloc d'impression
            if(deco.getBlocImpression() != null){
               // nouveau BlocImpressionTemplate
               final BlocImpressionTemplate bit = new BlocImpressionTemplate();
               bit.setOrdre(ordre);
               bit.setBlocImpression(deco.getBlocImpression());
               bit.setTemplate(template);
               blocs.add(bit);
               if(!blocsOld.contains(bit)){
                  blocsToCreate.add(bit);
               }

               // si le bloc est une liste, on va traiter les
               // champs a afficher
               if(deco.getBlocImpression().getIsListe()){
                  int ordreChamps = 0;
                  for(int j = 0; j < deco.getChampEntites().size(); j++){
                     ++ordreChamps;
                     final ChampEntite champ = deco.getChampEntites().get(j);
                     // nouveau ChampImprime
                     final ChampImprime ci = new ChampImprime();
                     ci.setBlocImpression(deco.getBlocImpression());
                     ci.setChampEntite(champ);
                     ci.setTemplate(template);
                     ci.setOrdre(ordreChamps);
                     champs.add(ci);
                     if(!champsOld.contains(ci)){
                        champsToCreate.add(ci);
                     }
                  }
               }
            }else if(deco.getTableAnnotation() != null){
               // si c'est une annotation
               // nouveau TableAnnotationTemplate
               final TableAnnotationTemplate tat = new TableAnnotationTemplate();
               tat.setOrdre(ordre);
               tat.setTableAnnotation(deco.getTableAnnotation());
               tat.setTemplate(template);
               tables.add(tat);
               if(!tablesOld.contains(tat)){
                  tablesToCreate.add(tat);
               }
            }
         }
      }

      //Gestion des cles Impression
      for(final CleImpression cle : cleImpressionList){
         if(clesOld.contains(cle)){
            cles.add(cle);
         }else{
            clesToCreate.add(cle);
         }
      }

      // create de l'objet
      ManagerLocator.getTemplateManager().updateObjectManager(template, SessionUtils.getSelectedBanques(sessionScope).get(0),
         selectedEntite, blocs, blocsToCreate, champs, champsToCreate, tables, tablesToCreate, cles, clesToCreate);
   }

   /* Methode comportementales */
   /**
    * Change le mode de la fiche en creation.
    */
   @Override
   public void switchToCreateMode(){
      this.blocImpressionsDecorated = new ArrayList<>();
      this.cleImpressionList = new ArrayList<>();

      super.switchToCreateMode();
      initEditableMode();

      enTeteRow.setVisible(false);
      piedPageRow.setVisible(false);
      fichierRow.setVisible(false);
      defineBlocsRow.setVisible(false);
      contenuEditGrid.setVisible(false);
      cleImpressionEditGrid.setVisible(false);

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();
      selectedType = this.template.getType().getType();
      selectedEntite = this.template.getEntite();

      // on cache l'entité
      entiteRequired.setVisible(false);
      entitesBox.setVisible(false);
      entiteLabel.setVisible(true);
      defineBlocsRow.setVisible(false);

      //On cache le type
      typeRequired.setVisible(false);
      typesBox.setVisible(false);
      typeLabel.setVisible(true);

      if(ETemplateType.BLOC == this.template.getType()){
         groupContenu.setVisible(true);
         cleImpressionEditGrid.setVisible(false);
      }else if(ETemplateType.DOC == this.template.getType()){
         if(null == this.template.getFichier() || "".equals(this.template.getFichier())){
            messageInfoUploadBtn.setValue(Labels.getLabel("template.messages.fichier"));
         }else{
            messageInfoUploadBtn.setValue(Labels.getLabel("template.messages.modifier.fichier"));
         }
         groupClesChamps.setVisible(true);
         contenuEditGrid.setVisible(false);
      }

      // on récupère tous les blocs pour l'entité
      final List<BlocImpression> blocImpressions = ManagerLocator.getBlocImpressionManager().findByEntiteManager(selectedEntite);
      for(int i = 0; i < blocImpressions.size(); i++){
         final BlocImpressionDecorator deco = new BlocImpressionDecorator(blocImpressions.get(i), null, template);
         if(!blocImpressionsDecorated.contains(deco)){
            deco.setImprimer(false);
            blocImpressionsDecorated.add(deco);
         }
      }

      // on récupère toutes les tables d'annotations pour
      // l'entité et la banque
      final List<TableAnnotation> tables = ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(selectedEntite,
         SessionUtils.getSelectedBanques(sessionScope).get(0));
      for(int i = 0; i < tables.size(); i++){
         final BlocImpressionDecorator deco = new BlocImpressionDecorator(null, tables.get(i), template);
         if(!blocImpressionsDecorated.contains(deco)){
            deco.setImprimer(false);
            blocImpressionsDecorated.add(deco);
         }
      }

      getBinder().loadComponent(self);
   }

   /**
    * Change mode de la fiche en mode statique.
    * @param detailMode Si true, la fiche se trouve dans une nouvelle
    * fenêtre pour afficher un contrat
    */
   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.template.equals(new Template()));
      if(null == this.template.getTemplateId()){
         groupContenu.setVisible(false);
         contenuStaticGrid.setVisible(false);
         groupClesChamps.setVisible(false);
         cleImpressionStaticGrid.setVisible(false);
      }else{
         if(ETemplateType.BLOC == this.template.getType()){
            fichierRow.setVisible(false);
            enTeteRow.setVisible(true);
            piedPageRow.setVisible(true);
            groupContenu.setVisible(true);
            groupClesChamps.setVisible(false);
            cleImpressionStaticGrid.setVisible(false);
            generateListeBlocs();
         }else if(ETemplateType.DOC == this.template.getType()){
            messageInfoUploadBtn.setValue("");
            uploadFichierBtn.setVisible(false);
            fichierRow.setVisible(true);
            enTeteRow.setVisible(false);
            piedPageRow.setVisible(false);
            defineBlocsRow.setVisible(false);
            groupContenu.setVisible(false);
            contenuStaticGrid.setVisible(false);
            generateListeCles();
         }
      }

   }

   @Override
   public void setFocusOnElement(){
      nomBox.setFocus(true);
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void clearData(){
      blocImpressionsDecorated = new ArrayList<>();
      clearConstraints();
      super.clearData();
   }

   /**
    * Clic sur le bouton defineBlocs : l'utilisateur a choisi
    * l'entité, on crée la table contenant les blocs à imprimer.
    */
   public void onClick$defineBlocs(){
      groupContenu.setVisible(true);
      defineBlocsRow.setVisible(false);

      this.template.setEntite(selectedEntite);
      entitesBox.setVisible(false);
      entiteLabel.setVisible(true);

      this.template.setType(ETemplateType.valueOf(selectedType));
      typesBox.setVisible(false);
      typeLabel.setVisible(true);

      contenuEditGrid.setVisible(true);

      // on récupère tous les blocs pour l'entité
      final List<BlocImpression> blocImpressions = ManagerLocator.getBlocImpressionManager().findByEntiteManager(selectedEntite);

      for(int i = 0; i < blocImpressions.size(); i++){
         final BlocImpressionDecorator deco = new BlocImpressionDecorator(blocImpressions.get(i), null, template);
         blocImpressionsDecorated.add(deco);
      }

      // on récupère toutes les tables d'annotations pour
      // l'entité et la banque
      final List<TableAnnotation> tables = ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(selectedEntite,
         SessionUtils.getSelectedBanques(sessionScope).get(0));
      for(int i = 0; i < tables.size(); i++){
         final BlocImpressionDecorator deco = new BlocImpressionDecorator(null, tables.get(i), template);
         blocImpressionsDecorated.add(deco);
      }
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForTemplate(){
      Boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = (Boolean) sessionScope.get("AdminPF");
      }else if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      if(admin){
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
      }
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(nomBox);
      Clients.clearWrongValue(descriptionBox);
      Clients.clearWrongValue(enteteBox);
      Clients.clearWrongValue(piedPageBox);
   }

   /*******************************************************/
   /**                  GETTERS - SETTERS                 */
   /*******************************************************/
   public BlocImpressionRowRenderer getBlocImpressionRenderer(){
      return blocImpressionRenderer;
   }

   public ListModel<Entite> getEntites(){
      return new ListModelList<>(entites, true);
   }

   public void setEntites(final List<Entite> e){
      this.entites = e;
   }

   public List<String> getTypes(){
      return types;
   }

   public void setTypes(final List<String> e){
      this.types = e;
   }

   public String getSelectedType(){
      return selectedType;
   }

   public void setSelectedType(final String selected){
      this.selectedType = selected;
   }

   public Entite getSelectedEntite(){
      return selectedEntite;
   }

   public void setSelectedEntite(final Entite selected){
      this.selectedEntite = selected;
   }

   public BlocImpressionRowRenderer getBlocImpressionRendererEdit(){
      return blocImpressionRendererEdit;
   }

   public ConstWord getNomConstraint(){
      return TemplateConstraints.getNomConstraint();
   }

   public ConstWord getTypeConstraint(){
      return TemplateConstraints.getTypeConstraint();
   }

   public ConstWord getNomNullConstraint(){
      return TemplateConstraints.getNomNullConstraint();
   }

   public ConstText getDescConstraint(){
      return TemplateConstraints.getDescConstraint();
   }

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   public List<CleImpression> getCleImpressionList(){
      return cleImpressionList;
   }

   public void setCleImpressionList(final List<CleImpression> cleImpressionList){
      this.cleImpressionList = cleImpressionList;
   }

   public Champ getSelectedChamp(){
      return selectedChamp;
   }

   public void setSelectedChamp(final Champ selectedChamp){
      this.selectedChamp = selectedChamp;
   }

}
