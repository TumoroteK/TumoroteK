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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.exception.BadFileFormatException;
import fr.aphp.tumorotek.manager.exception.DeriveImportParentNotFoundException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.HeaderException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TransformationQuantiteOverDemandException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueException;
import fr.aphp.tumorotek.manager.io.imports.ImportError;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.systeme.Entite;

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

   private final Logger log = LoggerFactory.getLogger(ResultatsImportModale.class);

   private static final long serialVersionUID = 462300734383948299L;

   private boolean importOk;

   private String currSheetName = null;

   private ImportHistorique currHistorique;

   private final Map<String, ImportHistorique> historiques = new LinkedHashMap<>();

   private List<ImportError> errors = new ArrayList<>();

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

   private Row okStatsRow;

   private Label nbPatientsLabel;

   private Label nbPrelevementsLabel;

   private Label nbEchantillonsLabel;

   private Label nbDerivesLabel;

   private Row okStatsPresentationRow;

   private Html okStatsLabel;

   private Html okLabel;

   private Label detailsLabel;

   // components errors
   private Row warningTitleRow;

   private Row warnStatsRow;

   private Row warnHelpRow;

   private Row warnDlRow;

   private Label warnStatsLabel;

   private Label warnHelpLabel;

   private Html warnLabel;

   private Component parent;

   private int keyIdx = 0;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      final AnnotateDataBinder binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);

      objOkComponents = new Component[] {this.okTitleRow, this.okStatsRow, this.okStatsPresentationRow};
      objErrorsComponents = new Component[] {this.warningTitleRow, this.warnDlRow, this.warnHelpRow, this.warnStatsRow};
   }

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
      final Workbook wb, final String cstn, final int winHeight, final Component p){

      currHistorique = histo;
      errors = errs;
      fileStream = is;
      workbook = wb;
      currSheetName = cstn;
      importOk = impOk;
      parent = p;

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
               map.put("embedded", new Boolean(true));
               final Window win =
                  (Window) Executions.createComponents("/zuls/imports/ChooseSheetWindow.zul", chooseSheetRow, map);
               win.doEmbedded();
               // augmente la hauteur de la window
               if(!resized){
                  ((Window) self.getParent().getParent()).setHeight("450px");
                  resized = true;
               }
            }else{ // update le chooseSheet
               BindUtils.postGlobalCommand(null, null, "update", map);
            }
         }else{ // toutes les lignes ont été selectionnées
            chooseSheetRow.detach();
            ((Window) self.getParent().getParent()).setHeight("340px");
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
                  }else{
                     warnHelpLabel.setValue(Labels.getLabel("importTemplate.statistiques.errors.file"));
                  }
               }else if(errors.get(0).getException().getClass().getSimpleName().equals("HeaderException")){
                  warnHelpLabel.setValue(ObjectTypesFormatters.getLabel("importTemplate.header.illegal",
                     new String[] {String.valueOf(((HeaderException) errors.get(0).getException()).getCol())}));
               }else{
                  dlVisible = true;
               }

               warnStatsRow.setVisible(dlVisible);
               warnDlRow.setVisible(dlVisible);
            }

            // augmente la hauteur de la window
            if(historiques.size() > 0 && !resized){
               ((Window) self.getParent().getParent()).setHeight("450px");
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

   private void initNbImports(final ImportHistorique hist){
      if(hist != null){

         okLabel.setContent(ObjectTypesFormatters.getLabel("importTemplate.resultats.ok.title", new String[] {currSheetName}));

         // Nb patients
         final Entite ePatient = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
         nbPatients =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, ePatient).size();
         nbPatientsLabel.setValue(String.valueOf(nbPatients));

         // Nb Prélèvements
         final Entite ePrlvt = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
         nbPrelevements =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, ePrlvt).size();
         nbPrelevementsLabel.setValue(String.valueOf(nbPrelevements));

         // Nb echans
         final Entite eEchan = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
         nbEchantillons =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, eEchan).size();
         nbEchantillonsLabel.setValue(String.valueOf(nbEchantillons));

         // Nb dérivés
         final Entite eDerive = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
         nbProdDerives =
            ManagerLocator.getImportHistoriqueManager().findImportationsByHistoriqueAndEntiteManager(hist, eDerive).size();
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

   public void onClick$dlFile(){
      // HSSFWorkbook wb;
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
         final CellStyle style = workbook.createCellStyle();
         final Font font = workbook.createFont();
         if(style instanceof HSSFCellStyle){
            font.setColor(Font.COLOR_RED);
         }else{
            ((XSSFFont) font).setColor(new XSSFColor(new java.awt.Color(255, 0, 0)));
         }
         style.setFont(font);

         final RichTextString textstr =
            sheet instanceof XSSFSheet ? new XSSFRichTextString(Labels.getLabel("importTemplate.header.colonne.erreurs"))
               : new HSSFRichTextString(Labels.getLabel("importTemplate.header.colonne.erreurs"));

         cellHeader.setCellValue(textstr);
         cellHeader.setCellStyle(style);

         // int cpt = 0;
         while(rit.hasNext()){
            row = rit.next();
            ImportError err = null;
            for(int i = 0; i < errors.size(); i++){
               if(row.getRowNum() == errors.get(i).getNbRow()){
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

               cell.setCellStyle(style);

               for(int i = 0; i < indCell; i++){
                  final Cell tmp = row.getCell(i);
                  if(tmp != null){
                     tmp.setCellStyle(style);
                  }
               }
            }

            //	++cpt;
            //}
         }

         ByteArrayOutputStream out = null;

         out = new ByteArrayOutputStream();
         workbook.write(out);
         Filedownload.save(out.toByteArray(),
            workbook instanceof HSSFWorkbook ? ConfigManager.OFFICE_EXCEL_MIME_TYPE : ConfigManager.OFFICE_OPENXML_MIME_TYPE,
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
      }

      // fermeture de la fenêtre
      //Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Génère le message qui sera affiché dans la fenêtre d'erreurs.
    * @version 2.2.3-genno
    */
   public String handleExceptionMessage(final RuntimeException ex){

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
                     log.warn("internationalisation non trouvee pour " + er.toString());
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
         }else if(ex instanceof UsedPositionException){
            message = ObjectTypesFormatters.getLabel("validation.emplacement.used", new String[] {
               ((UsedPositionException) ex).getEntite(), String.valueOf(((UsedPositionException) ex).getPosition())});
         }else if(ex instanceof WrongValueException){
            throw ex;
         }else if(ex.getCause() != null && ex.getCause() instanceof RuntimeException){
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