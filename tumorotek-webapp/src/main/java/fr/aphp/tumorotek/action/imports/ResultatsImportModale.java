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
package fr.aphp.tumorotek.action.imports;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.imports.strategy.ImportTemplateStrategy;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.exception.AbstractImportFileScopeException;
import fr.aphp.tumorotek.manager.exception.BadFileFormatException;
import fr.aphp.tumorotek.manager.exception.DeriveImportParentNotFoundException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.HeaderException;
import fr.aphp.tumorotek.manager.exception.ImportExceptionWithDetailByRowInterface;
import fr.aphp.tumorotek.manager.exception.ImportFileScopeException;
import fr.aphp.tumorotek.manager.exception.ImportKeyNotFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TransformationQuantiteOverDemandException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueForThesaurusException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueForThesaurusMException;
import fr.aphp.tumorotek.manager.exception.uimessage.MultipleUIMessageForRow;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForCell;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRow;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRowInterface;
import fr.aphp.tumorotek.manager.io.imports.ImportError;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.utils.io.EPoiColor;
import fr.aphp.tumorotek.utils.io.ExcelUtility;

/**
 * Dessine la fiche modale présentant à l'utilisateur
 * les résultats de l'import qu'il vient d'effectuer.
 * Date: 14/02/2011.
 * @since 2.0.6.10 download fichier correction .xlsx
 *
 * @author Pierre VENTADOUR
 * @version 2.2.3-genno
 *
 */
public class ResultatsImportModale extends GenericForwardComposer<Component>
{
   public static final String MODALE_WIDTH = "600px";
   public static final String MODALE_DEFAULT_HEIGHT = "370px";
   //height utilisée dans certains cas où la modale doit être plus haute
   public static final String MODALE_GREATER_HEIGHT = "460px";


   
   private final Logger log = LoggerFactory.getLogger(ResultatsImportModale.class);

   private static final long serialVersionUID = 462300734383948299L;

   private boolean importOk;

   private String currSheetName = null;

   private ImportHistorique currHistorique;

   private final Map<String, ImportHistorique> historiques = new LinkedHashMap<>();

   //gestion des erreurs dans le cas de la création (problème car toutes les erreurs du fichier sont bien détectées mais on n'en ramène qu'une 
   private List<ImportError> errors = new ArrayList<>();//correspond plutôt à la liste de ImportRowError
   //TK-538 : gestion des erreurs dans le cas de la modification d'un champ d'annotation (amélioration)
   private ImportFileScopeException importFileScopeException;
   private ImportTemplateStrategy importTemplateStrategy;
   //
   
   private InputStream fileStream;

   private Workbook workbook;

   private Component[] objOkComponents;

   private Component[] objErrorsComponents;

   private Integer nbPatients;

   private Integer nbPrelevements;

   private Integer nbEchantillons;

   private Integer nbProdDerives;

   List<Sheet> sheets = new ArrayList<>();

   List<TabFileSheet> seltbs = new ArrayList<>();

   private Div chooseSheetRow;

   private Image leftArrow;

   private Image rightArrow;

   private boolean resized = false;

   // components ok
   private Row okTitleRow;

   // Row contenant le tableau listant les types d'objet pouvant être importés avec le nombre d'imports réalisés
   private Row okStatsRow;

   private Label nbPatientsLabel;

   private Label nbPrelevementsLabel;

   private Label nbEchantillonsLabel;

   private Label nbDerivesLabel;

   //Row contenant le message introduisant le détail du tableau des objets TK importés (Label nbXXX ci-dessus)
   private Row okStatsPresentationRow;

   //correspond au message introduisant le détail du tableau des objets TK importés. Il contient du code html pour gérer la police d'où le type Html
   //ce composant est mis dans la Row okStatsPresentationRow ci-dessus
   private Html okStatsLabel;

   private Html okLabel;

   //Label contenant le titre du paragraphe détaillant le résultat de l'import : il est différent selon que le traitement a réussi ou échoué
   private Label detailsLabel;

   // components errors
   private Row warningTitleRow;

   private Row warnStatsRow;

   //row qui contient le message indiquant que le fichier de détail des erreurs peut être téléchargé
   private Row warnHelpRow;

   //row qui contient le bouton pour télécharger le fichier détaillant les erreurs. Cette row n'est visible que dans la cas d'erreur sur les données
   private Row warnDlRow;

   private Label warnStatsLabel;

   //label correspondant au message indiquant que le fichier de détail des erreurs peut être téléchargé. Il est mis dans warnHelpRow
   private Label warnHelpLabel;

   private Html warnLabel;

   private Button dlFile;
   
   //TK-538
   private Row confirmationRow;
   private Button continuer;
   private Button close;
   //le style des cellules porte également le format pour les dates
   //il faut donc définir un style pour l'affichage standard (DataFormat = 0) et pour les dates avec ou sans les heures.
   //ces styles sont stockés dans des maps dont les clés sont les dataFormats pour faciliter l'utilisation.
   private Map<Short, CellStyle> mapCellStyleByDataFormatForGreen = new HashMap<Short, CellStyle>();
   private Map<Short, CellStyle> mapCellStyleByDataFormatForRed = new HashMap<Short, CellStyle>();
   private Map<Short, CellStyle> mapCellStyleByDataFormatForRedAndBold = new HashMap<Short, CellStyle>();
   private Map<Short, CellStyle> mapCellStyleByDataFormatForOrange = new HashMap<Short, CellStyle>();
   private Map<Short, CellStyle> mapCellStyleByDataFormatForOrangeAndBold = new HashMap<Short, CellStyle>();
   
   private Component parent;

   private int keyIdx = 0;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      final AnnotateDataBinder binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);

      objOkComponents = new Component[] {this.okTitleRow, this.okStatsRow, this.okStatsPresentationRow};
      objErrorsComponents = new Component[] {this.warningTitleRow, this.warnDlRow, this.warnHelpRow, this.warnStatsRow, this.confirmationRow};
   }

   //utilisé dans le cas d'un import pour la création d'objets.
   ///!\ il existe une autre méthode init (pour l'import des modifications d'annotation) qui ressemble beaucoup mais elle prend en paramètre
   //une exception au lieu de List<ImportError>
   //A factoriser : cf TK-717
   /**
    * Initialise le composant à partir des paramètres d'affichage.
    * @param importOk true
    * @param histo objet ImportHistorique
    * @param errs erreurs relevées lors de l'import
    * @param is stream
    * @param wb Workbook
    * @param csti current sheet name imported
    * @version 2.0.10.6
    */
   public void init(final Boolean impOk, final ImportHistorique histo, final List<ImportError> errs, final InputStream is,
      final Workbook wb, final String cstn, final Component p, final ImportTemplateStrategy importTemplateStrategy){

      currHistorique = histo;
      errors = errs;
      fileStream = is;
      workbook = wb;
      currSheetName = cstn;
      importOk = impOk;
      parent = p;
      this.importTemplateStrategy = importTemplateStrategy;

      // inits sheets
      if(workbook != null){
         for(int i = 0; i < workbook.getNumberOfSheets(); i++){
            if(workbook.getSheetAt(i).getLastRowNum() > 0){
               sheets.add(workbook.getSheetAt(i));
            }
         }
      }

      update(importOk, currSheetName, currHistorique, errors);
   }

   //TK-538 : utilisé dans le cas d'un import pour modification d'un champ d'annotation. Cette méthode ressemble beaucoup à l'autre méthode init mais 
   //prend en paramètre une ImportException au lieu d'une liste de ImportError
   //A factoriser : cf TK-717
   public void init(final Boolean impOk, final ImportHistorique histo, final AbstractImportFileScopeException importException, final InputStream is,
      final Workbook wb, final String cstn, final Component p, final ImportTemplateStrategy importTemplateStrategy){

      currHistorique = histo;
      this.importFileScopeException = importException;
      fileStream = is;
      workbook = wb;
      currSheetName = cstn;
      importOk = impOk;
      parent = p;
      this.importTemplateStrategy = importTemplateStrategy;

      // inits sheets
      if(workbook != null){
         for(int i = 0; i < workbook.getNumberOfSheets(); i++){
            if(workbook.getSheetAt(i).getLastRowNum() > 0){
               sheets.add(workbook.getSheetAt(i));
            }
         }
      }

      update(importOk, currSheetName, currHistorique, importException);
   }
   
   //utilisé dans le cas d'un import pour la création d'objets
   ///!\ il existe une autre méthode update (pour l'import des modifications d'annotation) qui ressemble beaucoup
   //A factoriser : cf TK-717
   public void update(final boolean impOk, final String cstn, final ImportHistorique histo, final List<ImportError> errs){

      currHistorique = histo;
      errors = errs;
      currSheetName = cstn;
      importOk = impOk;

      // displays
      for(int i = 0; i < objOkComponents.length; i++){
         objOkComponents[i].setVisible(importOk);
      }
      for(int i = 0; i < objErrorsComponents.length; i++){
         objErrorsComponents[i].setVisible(!importOk);
      }

      if(importOk){
         initNbImports(currHistorique);
         historiques.put(currSheetName, currHistorique);

         // affiche les arrows si plusieurs historiques
         leftArrow.setVisible(historiques.size() > 1);
         rightArrow.setVisible(historiques.size() > 1);

         seltbs.add(new TabFileSheet(currSheetName, null, null));
         if(seltbs.size() < sheets.size()){
            final HashMap<String, Object> map = new HashMap<>();
            map.put("selSheets", seltbs);
            if(chooseSheetRow.getChildren().isEmpty()){ // creation chooseSheet
               map.put("sheets", sheets);
               map.put("parent", self);
               map.put("strategy", importTemplateStrategy);
               map.put("embedded", new Boolean(true));
               final Window win =
                  (Window) Executions.createComponents("/zuls/imports/ChooseSheetWindow.zul", chooseSheetRow, map);
               win.doEmbedded();
               // augmente la hauteur de la window
               if(!resized){
                  ((Window) self.getParent().getParent()).setHeight(MODALE_GREATER_HEIGHT);
                  resized = true;
               }
            }else{ // update le chooseSheet
               BindUtils.postGlobalCommand(null, null, "update", map);
            }
         }else{ // toutes les lignes ont été selectionnées
            chooseSheetRow.detach();
            ((Window) self.getParent().getParent()).setHeight(MODALE_DEFAULT_HEIGHT);
         }
      }else{
         if(errors != null && errors.size() > 0){

            warnLabel
               .setContent(ObjectTypesFormatters.getLabel("importTemplate.resultats.warn.title", new String[] {currSheetName}));

            detailsLabel.setValue(Labels.getLabel("importTemplate.statistiques.errors.infos"));

            warnStatsLabel.setValue(ObjectTypesFormatters.getLabel("importTemplate.statistiques.errors",
               new String[] {String.valueOf(errors.size())}));

            boolean dlVisible = false;

            if(errors.size() == 1){
               if(errors.get(0).getException().getClass().getSimpleName().equals("BadFileFormatException")){
                  final StringBuffer sb = new StringBuffer();
                  final BadFileFormatException bdfe = (BadFileFormatException) errors.get(0).getException();
                  if(bdfe.getColonnes() != null && bdfe.getColonnes().size() > 0){
                     for(int i = 0; i < bdfe.getColonnes().size(); i++){
                        sb.append(bdfe.getColonnes().get(i).getNom());

                        if(i < bdfe.getColonnes().size() - 1){
                           sb.append(", ");
                        }
                     }
                     warnHelpLabel.setValue(ObjectTypesFormatters.getLabel("importTemplate.statistiques.errors.colonnes",
                        new String[] {sb.toString()}));
                  }else{//on ne passe jamais dans ce else car BadFileFormatException n'est lancée que si il y a au moins une colonne manquante ...
                     warnHelpLabel.setValue(Labels.getLabel("importTemplate.statistiques.errors.file"));
                  }
               }else if(errors.get(0).getException().getClass().getSimpleName().equals("HeaderException")){
                  warnHelpLabel.setValue(ObjectTypesFormatters.getLabel("importTemplate.header.illegal",
                     new String[] {String.valueOf(((HeaderException) errors.get(0).getException()).getCol())}));
               }else{
                  dlFile.setLabel(Labels.getLabel("importTemplate.dl.correctif.button"));
                  dlVisible = true;
               }

               warnStatsRow.setVisible(dlVisible);
               warnDlRow.setVisible(dlVisible);
            }

            // augmente la hauteur de la window
            if(historiques.size() > 0 && !resized){
               ((Window) self.getParent().getParent()).setHeight(MODALE_GREATER_HEIGHT);
               resized = true;
            }

            try{
               if(fileStream != null && fileStream.available() == 0 && workbook == null){
                  warnDlRow.setVisible(false);
               }
            }catch(final IOException e){
               log.error(e.getMessage(), e); 
            }
         }
      }
   }

   //TK-538 : cette méthode update ressemble beaucoup à l'autre update mais gère différemment les exceptions : utilisation d'une ImportException au lieu d'une List<ImportError>
   //à terme, essayer de mutualiser mais nécessite de reprendre la gestion des erreurs du traitement d'import en création pour ne pas s'arrêter à la 1ere rencontrée notamment.
   //A factoriser : cf TK-717
   public void update(final boolean impOk, final String cstn, final ImportHistorique histo, final AbstractImportFileScopeException importException){

      currHistorique = histo;
      //errors = errs;
      this.importFileScopeException = importException;
      currSheetName = cstn;
      importOk = (importException == null);

      // displays
      for(int i = 0; i < objOkComponents.length; i++){
         objOkComponents[i].setVisible(importOk);
      }
      for(int i = 0; i < objErrorsComponents.length; i++){
         objErrorsComponents[i].setVisible(!importOk);
      }

      if(importOk){
         initNbImports(currHistorique);
         historiques.put(currSheetName, currHistorique);

         // affiche les arrows si plusieurs historiques
         leftArrow.setVisible(historiques.size() > 1);
         rightArrow.setVisible(historiques.size() > 1);

         seltbs.add(new TabFileSheet(currSheetName, null, null));
         if(seltbs.size() < sheets.size()){
            final HashMap<String, Object> map = new HashMap<>();
            map.put("selSheets", seltbs);
            if(chooseSheetRow.getChildren().isEmpty()){ // creation chooseSheet
               map.put("sheets", sheets);
               map.put("parent", self);
               map.put("strategy", importTemplateStrategy);
               map.put("embedded", new Boolean(true));
               final Window win =
                  (Window) Executions.createComponents("/zuls/imports/ChooseSheetWindow.zul", chooseSheetRow, map);
               win.doEmbedded();
               // augmente la hauteur de la window
               if(!resized){
                  ((Window) self.getParent().getParent()).setHeight(MODALE_GREATER_HEIGHT);
                  resized = true;
               }
            }else{ // update le chooseSheet
               BindUtils.postGlobalCommand(null, null, "update", map);
            }
         }else{ // toutes les lignes ont été selectionnées
            chooseSheetRow.detach();
            ((Window) self.getParent().getParent()).setHeight(MODALE_DEFAULT_HEIGHT);
         }
      }else{/// il faut adapter le else pour faire le cas du warning ....
         UIMessage mainMessage = importFileScopeException.retrieveUIMessageForMainMessage();
         // /!\ ajout du nom de la sheet en dernière position des paramètres :
         String[] mainMessageParams = mainMessage.getParams();
         String[] allParams = null;
         if(mainMessageParams == null) {
            allParams = new String[] {currSheetName};
         }
         else {
            int mainMessageParamsLength = mainMessageParams.length;
            allParams = new String[mainMessageParamsLength+1];
            System.arraycopy(mainMessageParams, 0, allParams, 0, mainMessageParamsLength);
            allParams[mainMessageParamsLength]=currSheetName;
         }
         
         warnLabel
         .setContent(ObjectTypesFormatters.getLabel(mainMessage.getI18nKey(), allParams));


         detailsLabel.setValue(Labels.getLabel("importTemplate.statistiques.errors.infos"));

         //gestion de l'affichage du message avec le nombre d'ereurs (bloc détails) :
         UIMessage statsUIMessage =  importFileScopeException.retrieveMessageAvecNombreErreur();
         boolean warnStatsRowVisible = false;
         if(statsUIMessage != null) {
            warnStatsRowVisible = true;
            warnStatsLabel.setValue(ObjectTypesFormatters.getLabel(statsUIMessage.getI18nKey(), statsUIMessage.getParams()));
         }
         warnStatsRow.setVisible(warnStatsRowVisible);
         
         //gestion du détail de l'erreur qui s'affichera en rouge et en italique
         UIMessage warnHelpMessage = importFileScopeException.retrieveDetail();
         if(warnHelpMessage != null) {
            warnHelpLabel.setValue(ObjectTypesFormatters.getLabel(warnHelpMessage.getI18nKey(), warnHelpMessage.getParams()));
         }
         warnHelpRow.setVisible(warnHelpMessage != null);
         
         //ajout du message personnalisé
         warnDlRow.setVisible(importFileScopeException.existCorrectionsFile());
         
         dlFile.setLabel(Labels.getLabel(importFileScopeException.retrieveI18KeyForDownloadButton()));
         
         continuer.setLabel(Labels.getLabel("importTemplate.correctif.warning.ecrasement.continuer.button"));
         close.setLabel(Labels.getLabel("importTemplate.correctif.warning.ecrasement.fermer.button"));
         //Si l'exception est un warning, on affiche la ligne contenant les boutons continuer et close
         confirmationRow.setVisible(importFileScopeException.isWarning());

         // augmente la hauteur de la window
         if(historiques.size() > 0 && !resized){
            ((Window) self.getParent().getParent()).setHeight(MODALE_GREATER_HEIGHT);
            resized = true;
         }

         try{
            if(fileStream != null && fileStream.available() == 0 && workbook == null){
               warnDlRow.setVisible(false);
            }
         }catch(final IOException e){
            log.error(e.getMessage(), e); 
         }

      }
   }

   
   
   private void initNbImports(final ImportHistorique hist){
      if(hist != null){

         okLabel.setContent(ObjectTypesFormatters.getLabel("importTemplate.resultats.ok.title", new String[] {currSheetName}));

         // Nb patients
         nbPatients =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEEntiteIdManager(hist, EEntiteId.PATIENT).size();
         nbPatientsLabel.setValue(String.valueOf(nbPatients));

         // Nb Prélèvements
         nbPrelevements =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEEntiteIdManager(hist, EEntiteId.PRELEVEMENT).size();
         nbPrelevementsLabel.setValue(String.valueOf(nbPrelevements));

         // Nb echans
         nbEchantillons =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEEntiteIdManager(hist, EEntiteId.ECHANTILLON).size();
         nbEchantillonsLabel.setValue(String.valueOf(nbEchantillons));

         // Nb dérivés
         nbProdDerives =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEEntiteIdManager(hist, EEntiteId.PROD_DERIVE).size();
         nbDerivesLabel.setValue(String.valueOf(nbProdDerives));

         okStatsLabel.setContent(ObjectTypesFormatters.getLabel("importTemplate.statistiques.ok",
            new String[] {currSheetName, ObjectTypesFormatters.dateRenderer2(hist.getDate())}));
      }else{ // title empty import et n'affiche pas stats
         for(int i = 0; i < objOkComponents.length; i++){
            objOkComponents[i].setVisible(false);
         }
         // display title
         okTitleRow.setVisible(true);
         okLabel.setContent(ObjectTypesFormatters.getLabel("importTemplate.resultats.empty.title", new String[] {currSheetName}));
         detailsLabel.setValue("");
      }
   }

   public void onClick$leftArrow(){

      // clean errors au besoin
      if(!importOk){
         for(int i = 0; i < objOkComponents.length; i++){
            objOkComponents[i].setVisible(true);
         }
         for(int i = 0; i < objErrorsComponents.length; i++){
            objErrorsComponents[i].setVisible(false);
         }
      }

      final List<ImportHistorique> histos = new ArrayList<>(historiques.values());
      final List<String> shNames = new ArrayList<>(historiques.keySet());
      final int idx = shNames.indexOf(currSheetName);
      if(idx > -1){ // si pas errors en cours, sinon conserve l'ancien idx
         keyIdx = idx;
      }

      if(keyIdx - 1 < 0){ // début de liste -> boucle affiche le dernier element
         keyIdx = histos.size() - 1;
      }else{
         keyIdx--;
      }

      currHistorique = histos.get(keyIdx);
      currSheetName = shNames.get(keyIdx);

      initNbImports(currHistorique);
   }

   public void onClick$rightArrow(){

      // clean errors au besoin
      if(!importOk){
         for(int i = 0; i < objOkComponents.length; i++){
            objOkComponents[i].setVisible(true);
         }
         for(int i = 0; i < objErrorsComponents.length; i++){
            objErrorsComponents[i].setVisible(false);
         }
      }

      final List<ImportHistorique> histos = new ArrayList<>(historiques.values());
      final List<String> shNames = new ArrayList<>(historiques.keySet());
      final int idx = shNames.indexOf(currSheetName);
      if(idx > -1){ // si pas errors en cours, sinon conserve l'ancien idx
         keyIdx = idx;
      }
      if(keyIdx + 1 >= histos.size()){ // tête de liste -> boucle affiche le premier element
         keyIdx = 0;
      }else{
         keyIdx++;
      }

      currHistorique = histos.get(keyIdx);
      currSheetName = shNames.get(keyIdx);

      initNbImports(currHistorique);
   }

   //le traitement des erreurs a été amélioré pour la partie modification des annotations pour que le traitement n'affiche pas que
   //la 1ere erreur rencontrée
   //Faire cette amélioration sur la partie création est lourd et impactant donc le code initial a été gardé sachant que toutes les 
   //erreurs ont bien été détectées mais seule la 1ere est rendue à l'utilisateur :-( ....
   public void onClick$dlFile(){
      if(importFileScopeException != null) {
         downloadFichierTraitementModifAnnotation();
      }else {   
         downloadFichierCorrectionsForTraitementCreation();
      }
   }
   
   //TK-538 : pour poursuivre le traitement si un message d'avertissement est affiché (par exemple dans le cas de la mise à jour des annotations
   //si des valeurs existants en base de données vont être écrasées.
   public void onClick$continuer(){
      Map<String, Object> eventData = new HashMap<String, Object>();
      eventData.put(FicheImportTemplate.EVENT_DATA__SHEET_NAME, currSheetName);
      eventData.put(FicheImportTemplate.EVENT_DATA__MODE_SOUS_SURVEILLANCE, new Boolean(false));
      Events.echoEvent(importTemplateStrategy.defineNomMethodePourExecuterImport(), parent, eventData);
   }
   
   public void onClick$close(){
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   protected void downloadFichierCorrectionsForTraitementCreation(){
      ByteArrayOutputStream out = null;
      Sheet sheet;
      Iterator<org.apache.poi.ss.usermodel.Row> rit;
      org.apache.poi.ss.usermodel.Row row = null;
      // ouvre fichier xls
      try{
         // fileStream.reset();
         // fileStream.mark(0);
         // wb = new HSSFWorkbook(fileStream);
         if(currSheetName != null){
            sheet = workbook.getSheet(currSheetName);
         }else{
            sheet = workbook.getSheetAt(0);
         }
         workbook.setActiveSheet(workbook.getSheetIndex(sheet.getSheetName()));
         // on se positionne sur la première ligne
         rit = sheet.rowIterator();
         row = rit.next();
         final int indCell = row.getPhysicalNumberOfCells();
         final Cell cellHeader = row.createCell((short) indCell);
         initMapCellStyleRedIfNecessary();
  
         final RichTextString textstr =
            sheet instanceof XSSFSheet ? new XSSFRichTextString(Labels.getLabel("importTemplate.header.colonne.erreurs"))
               : new HSSFRichTextString(Labels.getLabel("importTemplate.header.colonne.erreurs"));
  
         cellHeader.setCellValue(textstr);
         cellHeader.setCellStyle(mapCellStyleByDataFormatForRed.get(ExcelUtility.POI_DATA_FORMAT__GENERAL_INDEX));
  
         // int cpt = 0;
         while(rit.hasNext()){
            row = rit.next();
            ImportError err = null;
            //Algorithme à revoir car on boucle à chaque fois sur errors. Le break semble présent pour optimiser 
            //mais si une ligne a plusieurs erreurs, ça empêche de toutes les afficher...
            for(int i = 0; i < errors.size(); i++){
               if(row.getRowNum() == errors.get(i).getNbRow()){//PASSER PAR ImportException
                  err = errors.get(i);
                  break;
               }
            }
  
            //	if (cpt < errors.size()
            //			&& row.getRowNum() == errors.get(cpt).getNbRow()) {
            if(err != null){
               final Cell cell = row.createCell((short) indCell);
               cell.setCellValue(sheet instanceof XSSFSheet ? new XSSFRichTextString(handleExceptionMessage(err.getException()))
                  : new HSSFRichTextString(handleExceptionMessage(err.getException())));
  
               cell.setCellStyle(mapCellStyleByDataFormatForRed.get(ExcelUtility.POI_DATA_FORMAT__GENERAL_INDEX));
  
               for(int i = 0; i < indCell; i++){
                  final Cell tmp = row.getCell(i);
                  if(tmp != null){
                     tmp.setCellStyle(mapCellStyleByDataFormatForRed.get(tmp.getCellStyle().getDataFormat()));
                  }
               }
            }
  
            //	++cpt;
            //}
         }
  
         out = new ByteArrayOutputStream();
         workbook.write(out);
         Filedownload.save(out.toByteArray(),
            defineMimeTypeForWorkbook(),
            "corrections." + (workbook instanceof HSSFWorkbook ? "xls" : "xlsx"));
      }catch(final FileNotFoundException e){
         log.error(e.getMessage(), e); 
      }catch(final IOException e){
         log.error(e.getMessage(), e); 
      }finally{
         if(fileStream != null){
            try{
               fileStream.close();
            }catch(final Exception e){
               fileStream = null;
            }
         }
         if(out != null) {
            try{
               out.close();
            }catch(final Exception e){
               out = null;
            }            
         }
      }
   }
   
   //créer un fichier d'une colonne avec l'entête de la colonne passée en paramètre ainsi que les données à afficher pour chaque cellule de cette colonne
   //tout est écrit en noir
   private Workbook buildNewFileWithOneColumn(UIMessage libelleEnteteColonne, List<String> data) {
      Workbook workbookErreur = null;
      workbookErreur = new XSSFWorkbook();
      Sheet sheet = ExcelUtility.createSheet(workbookErreur, currSheetName);
      org.apache.poi.ss.usermodel.Row rowEntete = sheet.createRow(0);
      Cell cellHeader = rowEntete.createCell(0);
      cellHeader.setCellValue(ObjectTypesFormatters.getLabel(libelleEnteteColonne.getI18nKey(), libelleEnteteColonne.getParams()));
      int nbData = data.size();
      for(int i=0; i< nbData; i++) {
         org.apache.poi.ss.usermodel.Row row = sheet.createRow(i+1);
         Cell cellKey = row.createCell(0);
         cellKey.setCellValue(data.get(i));
      }
      
      return workbookErreur;
   }
   
   //les objets UIMessageForRowInterface de la liste passée en paramètre peuvent être :
   //  - UIMessageForRow, utilisé quand l'erreur concerne la ligne entière car un problème sur la clé empêche de la traiter (par exemple code en doublon). Toute la ligne sera affichée en rouge
   //  - MultipleUIMessageForRow, utilisé pour les erreurs liées aux données à importer : il peut y avoir plusieurs données incorrectes pour une ligne. Toute la ligne sera affichée en rouge 
   //mais la/les donnée(s) en erreur sera/seront mise(s) en gras
   //A noter que si des lignes ont été importées, ce sont forcément les premières car c'est le cas où la 1ere erreur rencontrée n'est pas sur le 1er lot traité
   private void addDetailTraitementColumnToCurrentSheetOfWorkbook(Workbook workbook, List<UIMessageForRowInterface> listUIMessageForRow, int nbRowsImportees) {

      Sheet sheet;
      Iterator<org.apache.poi.ss.usermodel.Row> rit;
      org.apache.poi.ss.usermodel.Row row = null;

      if(currSheetName != null){
         sheet = workbook.getSheet(currSheetName);
      }else{
         sheet = workbook.getSheetAt(0);
      }
      workbook.setActiveSheet(workbook.getSheetIndex(sheet.getSheetName()));
      // on se positionne sur la première ligne
      rit = sheet.rowIterator();
      row = rit.next();
      int indCellForMessage = row.getPhysicalNumberOfCells();
      Cell cellHeader = row.createCell((short) indCellForMessage);
     
      initMapCellStyleGreenIfNecessary();
     
      //cas d'une exception lancée dans une transaction avec la base => on ajoute une cellule à la fin des lignes traitées
      if(listUIMessageForRow == null) {
         addInfoImportFait(nbRowsImportees, sheet, rit, indCellForMessage, cellHeader);
      }
      else {
         addErrorsAndInfoImportFait(workbook, listUIMessageForRow, nbRowsImportees, sheet, rit, row, indCellForMessage,
            cellHeader);
      }
   }
  
   private void initMapCellStyleRedIfNecessary() {
      initMapCellStyleForColorIfNecessary(mapCellStyleByDataFormatForRed, EPoiColor.RED, false);
      initMapCellStyleForColorIfNecessary(mapCellStyleByDataFormatForRedAndBold, EPoiColor.RED, true);
   }
   
   private void initMapCellStyleOrangeIfNecessary() {
      initMapCellStyleForColorIfNecessary(mapCellStyleByDataFormatForOrange, EPoiColor.ORANGE, false);
      initMapCellStyleForColorIfNecessary(mapCellStyleByDataFormatForOrangeAndBold, EPoiColor.ORANGE, true);
   }
   
   private void initMapCellStyleGreenIfNecessary() {
      initMapCellStyleForColorIfNecessary(mapCellStyleByDataFormatForGreen, EPoiColor.GREEN, false);
   }
   
   private void initMapCellStyleForColorIfNecessary(Map<Short, CellStyle> mapCellStyleByDataFormat, EPoiColor ePoiColor, boolean bold) {
      if(mapCellStyleByDataFormat.isEmpty()) {
         CreationHelper createHelper = workbook.getCreationHelper();
         short formatForDateHeure = createHelper.createDataFormat().getFormat(ExcelUtility.POI_DATA_FORMAT__DATE_HEURE);
         short formatForJour = createHelper.createDataFormat().getFormat(ExcelUtility.POI_DATA_FORMAT__JOUR);

         CellStyle style = ExcelUtility.defineStyle(workbook, ePoiColor, bold);
         mapCellStyleByDataFormat.put(ExcelUtility.POI_DATA_FORMAT__GENERAL_INDEX, style);
         CellStyle styleForDateHeure = workbook.createCellStyle();
         styleForDateHeure.cloneStyleFrom(style);
         styleForDateHeure.setDataFormat(formatForDateHeure);
         mapCellStyleByDataFormat.put(formatForDateHeure, styleForDateHeure);
         CellStyle styleForJour = workbook.createCellStyle();
         styleForJour.cloneStyleFrom(style);
         styleForJour.setDataFormat(formatForJour);
         mapCellStyleByDataFormat.put(formatForJour, styleForJour);
      }
   }
   
   private void addErrorsAndInfoImportFait(Workbook workbook, List<UIMessageForRowInterface> listUIMessageForRow,
      int nbRowsImportees, Sheet sheet, Iterator<org.apache.poi.ss.usermodel.Row> rit, org.apache.poi.ss.usermodel.Row row,
      int indCellForMessage, Cell cellHeader){
      int nbRowAvecErreur = listUIMessageForRow.size();
      //plusieursErreursPossible permet de gérer une exception pouvant avoir plusieurs erreurs pour une même ligne : on met en gras la donnée dans la/les colonnes avec erreur
      boolean plusieursErreursPossible = false;
      if(listUIMessageForRow.get(0) instanceof MultipleUIMessageForRow) {
         plusieursErreursPossible= true;
      }
      
      String headerNewColumnI18nKey = null;
      CellStyle headerNewColumnStyle = null;
      Map<Short, CellStyle> mapCellStyleByDataFormatToUse = null;
      Map<Short, CellStyle> mapCellStyleByDataFormatToUseForBold = null;
      //gestion des couleurs dans la cellule ajoutée
      if(importFileScopeException.isWarning()) {
         initMapCellStyleOrangeIfNecessary();
         mapCellStyleByDataFormatToUse = mapCellStyleByDataFormatForOrange;
         mapCellStyleByDataFormatToUseForBold = mapCellStyleByDataFormatForOrangeAndBold;
      }
      else {
         initMapCellStyleRedIfNecessary();
         mapCellStyleByDataFormatToUse = mapCellStyleByDataFormatForRed;
         mapCellStyleByDataFormatToUseForBold = mapCellStyleByDataFormatForRedAndBold;
      }
      //si des rows ont été importés (plantage de la transaction sur le 2e lot ou les suivants ou 1er risque d'écrasement sur le 2e lot ou les suivants)
      //le libellé de l'entête de la colonne ajoutée est générale
      if(nbRowsImportees > 0) {
         headerNewColumnI18nKey = "importTemplate.header.colonne.detailTraitement";
         //on laisse le style par défaut (null)
         //initialisation de la couleur verte qui sera utilisée dans ce cas
         initMapCellStyleGreenIfNecessary();
      }
      else if(importFileScopeException.isWarning()) {
         headerNewColumnI18nKey = "importTemplate.header.colonne.avertissements";
         headerNewColumnStyle = mapCellStyleByDataFormatForOrange.get(ExcelUtility.POI_DATA_FORMAT__GENERAL_INDEX);
      }
      else {
         initMapCellStyleRedIfNecessary();
         headerNewColumnI18nKey = "importTemplate.header.colonne.erreurs";
         headerNewColumnStyle = mapCellStyleByDataFormatForRed.get(ExcelUtility.POI_DATA_FORMAT__GENERAL_INDEX);

      }
      
      final RichTextString textstr =
         sheet instanceof XSSFSheet ? new XSSFRichTextString(Labels.getLabel(headerNewColumnI18nKey))
            : new HSSFRichTextString(Labels.getLabel(headerNewColumnI18nKey));
  
      cellHeader.setCellValue(textstr);
      cellHeader.setCellStyle(headerNewColumnStyle);
  
      int rowNum = 1;//on est sur la première ligne d'entête
      UIMessageForRowInterface messageForRow = null;
      //1er étape, on gère les lignes éventuellement traitées
      while(rowNum < nbRowsImportees+1 && rit.hasNext()) {
         row = rit.next();
         //
         
         Cell cell = row.createCell((short) indCellForMessage);
         cell.setCellValue(Labels.getLabel("importTemplate.correctif.info.importFait"));
         cell.setCellStyle(mapCellStyleByDataFormatForGreen.get(cell.getCellStyle().getDataFormat()));
         rowNum++;
      }
      //on boucle sur la liste des messages d'erreur triés par ligne :
      for(int i=0; i< nbRowAvecErreur; i++) {
         messageForRow = listUIMessageForRow.get(i);
         while(rowNum < messageForRow.getRowNum() && rit.hasNext()) {
            row = rit.next();
            rowNum++;
         }
         //on crée une cellule en bout de ligne pour indiquer le message d'erreur :
         Cell cell = row.createCell((short) indCellForMessage);
         String value = null;
         List<UIMessageForCell> listUIMessageForCell = null;
  
         if(plusieursErreursPossible) {
            listUIMessageForCell = ((MultipleUIMessageForRow)messageForRow).getListUIMessageForCell();
            //concaténation des erreurs avec le séparateur | :
            value = listUIMessageForCell.stream()
               .map(uiMessageForCell -> ObjectTypesFormatters.getLabel(uiMessageForCell.getI18nKey(),  uiMessageForCell.getParams()))
               .collect(Collectors.joining(" | "));
         }
          else {
            value = ObjectTypesFormatters.getLabel(((UIMessageForRow)messageForRow).getI18nKey(),  ((UIMessageForRow)messageForRow).getParams());
         }
         cell.setCellValue(value);
         cell.setCellStyle(mapCellStyleByDataFormatToUse.get(ExcelUtility.POI_DATA_FORMAT__GENERAL_INDEX));
         
         //gestion de la cellule en gras dans le cas d'une erreur portant sur la cellule :
         List<Integer> listIndexColInError = null;
         if(plusieursErreursPossible) {
            listIndexColInError = listUIMessageForCell.stream().map(uiMessageForCell -> uiMessageForCell.getCellIndex()).collect(Collectors.toList());
         }
         
         //on met toute la ligne en rouge :
         for(int j = 0; j < indCellForMessage; j++){
            final Cell tmp = row.getCell(j);
            if(tmp != null){
               if(listIndexColInError != null && listIndexColInError.contains(j)) {
                  tmp.setCellStyle(mapCellStyleByDataFormatToUseForBold.get(tmp.getCellStyle().getDataFormat()));
               }
               else {
                  tmp.setCellStyle(mapCellStyleByDataFormatToUse.get(tmp.getCellStyle().getDataFormat()));
               }
            }
         }
      }
   }

   private void addInfoImportFait(int nbRowsImportees, Sheet sheet, Iterator<org.apache.poi.ss.usermodel.Row> rit,
      int indCellForMessage, Cell cellHeader){
      org.apache.poi.ss.usermodel.Row row;
      String headerNewColumnI18nKey = "importTemplate.header.colonne.detailTraitement";
      CellStyle headerNewColumnStyle = null;
      
      final RichTextString textstr =
         sheet instanceof XSSFSheet ? new XSSFRichTextString(Labels.getLabel(headerNewColumnI18nKey))
            : new HSSFRichTextString(Labels.getLabel(headerNewColumnI18nKey));
  
      cellHeader.setCellValue(textstr);
      cellHeader.setCellStyle(headerNewColumnStyle);

      int rowNum = 1;//on est sur la première ligne d'entête

      while(rowNum < nbRowsImportees+1 && rit.hasNext()) {
         row = rit.next();
         Cell cell = row.createCell((short) indCellForMessage);
         cell.setCellValue(Labels.getLabel("importTemplate.correctif.info.importFait"));
         cell.setCellStyle(mapCellStyleByDataFormatForGreen.get(cell.getCellStyle().getDataFormat()));
         rowNum++;
      }
   }

   public void downloadFichierTraitementModifAnnotation() {
      if(importFileScopeException != null) {

       ByteArrayOutputStream out = null;
          try{
             //cas de l'utilisation du fichier existant avec ajout d'une colonne pour indiquer les erreurs (et éventuellement les lignes traitées)
             //ou uniquement les lignes traitées dans le cas d'une ImportTransactionKOException
             if(importFileScopeException instanceof ImportExceptionWithDetailByRowInterface) {
                ImportExceptionWithDetailByRowInterface importExceptionWithDetailByRowInterface = (ImportExceptionWithDetailByRowInterface)importFileScopeException;
                List<UIMessageForRowInterface> listUIMessageForRowInterface = importExceptionWithDetailByRowInterface.buildSortedUIMessageForRow();
                String nomFichier = "corrections";
                if(listUIMessageForRowInterface == null) {
                   nomFichier = "traitement";
                }
                
                addDetailTraitementColumnToCurrentSheetOfWorkbook(workbook, listUIMessageForRowInterface, importExceptionWithDetailByRowInterface.getNbImportedRows());

                out = new ByteArrayOutputStream();
                workbook.write(out);
                Filedownload.save(out.toByteArray(), defineMimeTypeForWorkbook(), defineFileNameForWorkbook(nomFichier));
             }
             //cas de la création d'un nouveau fichier avec des éléments liés à l'erreur (généralement la clé fonctionnelle) dans la 1ere colonne du fichier généré
             else if(importFileScopeException instanceof ImportKeyNotFoundException) {
                ImportKeyNotFoundException importKeyNotFoundException = (ImportKeyNotFoundException)importFileScopeException;
                UIMessage messageForHeader = importKeyNotFoundException.retrieveMessageForHeaderOfFichierCorrections();
                Workbook workbookErreur = buildNewFileWithOneColumn(messageForHeader, importKeyNotFoundException.getListKeyNotFound());
                
                out = new ByteArrayOutputStream();
                workbookErreur.write(out);
                Filedownload.save(out.toByteArray(), ConfigManager.OFFICE_OPENXML_MIME_TYPE, "codesInexistants.xlsx");
             }
          }
          catch(final FileNotFoundException e){
             log.error(e.getMessage(), e); 
          }
          catch(final IOException e){
             log.error(e.getMessage(), e); 
          }
          finally{
             if(fileStream != null){
                try{
                   fileStream.close();
                }catch(final Exception e){
                   fileStream = null;
                }
             }
             if(out != null){
                try{
                   out.close();
                }catch(final Exception e){
                   out = null;
                }
             }          
          }
       }
   }

   private String defineMimeTypeForWorkbook(){
      return workbook instanceof HSSFWorkbook ? ConfigManager.OFFICE_EXCEL_MIME_TYPE : ConfigManager.OFFICE_OPENXML_MIME_TYPE;
   }
   
   private String defineFileNameForWorkbook(String fileNameWithoutExtension){
      if(fileNameWithoutExtension == null) {
         fileNameWithoutExtension = "TK";
      }
      return new StringBuilder(fileNameWithoutExtension).append(".").append(workbook instanceof HSSFWorkbook ? "xls" : "xlsx").toString();
   }    


   /**
    * Génère le message qui sera affiché dans la fenêtre d'erreurs.
    * @version 2.2.3-genno
    */
   public String handleExceptionMessage(final Exception ex){

      log.debug("handling exception {}", ex);

      String message = Labels.getLabel("validation.exception.inconnu");

      try{
         if(ex instanceof ValidationException){
            message = "";
            final Iterator<Errors> errs = (((ValidationException) ex).getErrors()).iterator();
            while(errs.hasNext()){
               final Errors er = errs.next();
               // si l'erreur n'est pas définie dans le fichier
               // i3.properties, on va créer l'erreur à afficher
               if(Labels.getLabel(er.getFieldError().getCode()) == null){
                  final String champ = getLabelForError(er);
                  if(champ != null){
                     message = message + ObjectTypesFormatters.getLabel("validation.invalid.import", new String[] {champ});
                  }else{
                     log.warn("internationalisation non trouvee pour {}",  er);
                     message = message + ObjectTypesFormatters.getLabel("validation.invalid.import", new String[] {"?"});
                  }
               }else{
                  message = message + Labels.getLabel("validation.error");
                  message = message + " " + Labels.getLabel(er.getFieldError().getCode());
               }
            }
         }else if(ex instanceof DoublonFoundException){
            message = ObjectTypesFormatters.getLabel("validation.doublon",
               new String[] {((DoublonFoundException) ex).getEntite(), ((DoublonFoundException) ex).getOperation()});
         }else if(ex instanceof RequiredObjectIsNullException){
            message = ObjectTypesFormatters.getLabel("validation.requiredObject",
               new String[] {((RequiredObjectIsNullException) ex).getEntite(),
                  ((RequiredObjectIsNullException) ex).getRequiredObject(), ((RequiredObjectIsNullException) ex).getOperation()});
         }else if(ex instanceof ObjectUsedException){
            message = Labels.getLabel(((ObjectUsedException) ex).getKey());
         }else if(ex instanceof ObjectReferencedException){
            message = Labels.getLabel(((ObjectReferencedException) ex).getKey());
         }else if(ex instanceof DeriveImportParentNotFoundException){
            message = ObjectTypesFormatters.getLabel(((DeriveImportParentNotFoundException) ex).getMessage(),
               new String[] {((DeriveImportParentNotFoundException) ex).getValeurAttendue(),
                  ((DeriveImportParentNotFoundException) ex).getColonne().getImportTemplate().getBanque().getNom()});
         }else if(ex instanceof TransformationQuantiteOverDemandException){
            message = ObjectTypesFormatters.getLabel(ex.getMessage(),
               new String[] {((TransformationQuantiteOverDemandException) ex).getQteDemandee().toString(),
                  ((TransformationQuantiteOverDemandException) ex).getQteRestante().toString()});
         }else if(ex instanceof WrongImportValueException){
            final ImportColonne ic = ((WrongImportValueException) ex).getColonne();
            if(ic.getChamp() != null){
               // si c'est une erreur sur un thesaurus
               if(ic.getChamp().getChampEntite() != null && ic.getChamp().getChampEntite().getQueryChamp() != null){
                  message = ObjectTypesFormatters.getLabel("validation.wrong.import.thesaurus",
                     new String[] {((WrongImportValueException) ex).getColonne().getNom()});
               }else if(ic.getChamp().getChampAnnotation() != null
                  && ic.getChamp().getChampAnnotation().getDataType().getType().equals("thesaurus")){
                  // si c'est une erreur sur un thesaurus
                  message = ObjectTypesFormatters.getLabel("validation.wrong.import.thesaurus",
                     new String[] {((WrongImportValueException) ex).getColonne().getNom()});
               }else if(ic.getChamp().getChampEntite() != null
                  && ic.getChamp().getChampEntite().getNom().equals("EmplacementId")){
                  // si c'est une erreur sur un emplacement
                  message = ObjectTypesFormatters.getLabel("validation.wrong.import.emplacement",
                     new String[] {((WrongImportValueException) ex).getColonne().getNom()});
               }else{
                  message = ObjectTypesFormatters.getLabel("validation.wrong.import.value",
                     new String[] {((WrongImportValueException) ex).getColonne().getNom(),
                        ((WrongImportValueException) ex).getValeurAttendue()});
               }
            }else{
               message = ObjectTypesFormatters.getLabel("validation.wrong.import.value", new String[] {
                  ((WrongImportValueException) ex).getColonne().getNom(), ((WrongImportValueException) ex).getValeurAttendue()});
            }
         }else if (ex instanceof WrongImportValueForThesaurusException) {//thesaurus
            WrongImportValueForThesaurusException wrongValueException = (WrongImportValueForThesaurusException)ex;
            UIMessage uiMessage = wrongValueException.buildUIMessage();
            message = ObjectTypesFormatters.getLabel(uiMessage.getI18nKey(), uiMessage.getParams());
         }else if (ex instanceof WrongImportValueForThesaurusMException) {//thesaurusM
            WrongImportValueForThesaurusMException wrongValueException = (WrongImportValueForThesaurusMException)ex;
            UIMessage uiMessage = wrongValueException.buildUIMessage();
            message = ObjectTypesFormatters.getLabel(uiMessage.getI18nKey(), uiMessage.getParams());
         }else if(ex instanceof UsedPositionException){
            message = ObjectTypesFormatters.getLabel("validation.emplacement.used", new String[] {
               ((UsedPositionException) ex).getEntite(), String.valueOf(((UsedPositionException) ex).getPosition())});
         }else if(ex instanceof WrongValueException){
            throw (WrongValueException)ex;
         }else if(ex.getCause() != null){
            return handleExceptionMessage((RuntimeException) ex.getCause());
         }else{
            message = Labels.getLabel(ex.getMessage());
         }
         // aucun message n'a pu être généré -> exception inattendue
         if(message == null){
            message = ex.getClass().getSimpleName() + " : " + ex.getMessage();
            log.debug(message);
         }
         // @since 2.2.3-genno capture NullPointer renvoyée par ObjectTypesFormatters
      }catch(final NullPointerException e){ // une exception inattendue survient dans le formatage du message
         log.warn("unexpected error occurred during import error message {}", ex);
         message = ex.getClass().getSimpleName() + " : " + ex.getMessage();
      }

      return message;
   }

   /**
    * Extrait le nom du champ du fichier i3.properties pour
    * une erreur.
    * @param er Erreur.
    * @return Le nom du champ.
    */
   public String getLabelForError(final Errors er){
      final StringBuffer iProperty = new StringBuffer();
      iProperty.append("Champ.");
      final String nomObjet = er.getObjectName().substring(er.getObjectName().lastIndexOf(".") + 1);
      iProperty.append(nomObjet);
      iProperty.append(".");

      final String champ = er.getFieldError().getField();

      String champOk = "";
      // si le nom du champ finit par "Id", on le retire
      if(champ.endsWith("Id")){
         champOk = champ.substring(0, champ.length() - 2);
      }else if(er.getFieldError().getCode().contains("codeOrganes")){ // 2.3.0-gatsbi
         champOk = "codeOrganes";
      }else if(er.getFieldError().getCode().contains("codeMorphos")){ // 2.3.0-gatsbi
         champOk = "codeMorphos";
      }else{
         champOk = champ;
      }
      champOk = champOk.replaceFirst(".", (champOk.charAt(0) + "").toUpperCase());
      iProperty.append(champOk);

      // si l'erreur porte sur une annotation, on va retourner
      // le nom du champannotation
      if(nomObjet.equals("AnnotationValeur")){
         final BindingResult res = (BindingResult) er;
         final AnnotationValeur av = (AnnotationValeur) res.getTarget();
         return av.getChampAnnotation().getNom();
      }else{
         // on ajoute la valeur du champ
         return Labels.getLabel(iProperty.toString());
      }
   }

   public void onLaterImport(final Event e){
      Events.echoEvent("onLaterImport", parent, e.getData());
   }
   
   public void onLaterImportForModificationAnnotation(final Event e){
      Events.echoEvent("onLaterImportForModificationAnnotation", parent, e.getData());
   }

   public void onCloseFromChooseSheet(final Event e){
      Events.postEvent(new Event("onClose", self.getRoot()));
   }
   
   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public ImportHistorique getCurrHistorique(){
      return currHistorique;
   }

   public void setCurrHistorique(final ImportHistorique h){
      this.currHistorique = h;
   }

   public List<ImportError> getErrors(){
      return errors;
   }

   public void setErrors(final List<ImportError> e){
      this.errors = e;
   }

   public Integer getNbPatients(){
      return nbPatients;
   }

   public void setNbPatients(final Integer nb){
      this.nbPatients = nb;
   }

   public Integer getNbPrelevements(){
      return nbPrelevements;
   }

   public void setNbPrelevements(final Integer nb){
      this.nbPrelevements = nb;
   }

   public Integer getNbEchantillons(){
      return nbEchantillons;
   }

   public void setNbEchantillons(final Integer nb){
      this.nbEchantillons = nb;
   }

   public Integer getNbProdDerives(){
      return nbProdDerives;
   }

   public void setNbProdDerives(final Integer nb){
      this.nbProdDerives = nb;
   }

   public InputStream getFileStream(){
      return fileStream;
   }

   public void setFileStream(final InputStream fStream){
      this.fileStream = fStream;
   }
}