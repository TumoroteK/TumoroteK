package fr.aphp.tumorotek.action.impression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe fournissant les méthodes et propriétés statiques pour les Templates
 * Classe créée le 
 * @author Answald Bournique
 * @since 2.2
 * @version 2.2
 */
public class TemplateUtils
{

   //TODO Revoir l'emplacement et la déclaration du dossier contenant les documents templates
   /**
    * Dossier où sont stockés les documents Template
    */
   public static String TEMPLATE_DOC_FOLDER = SessionUtils.getSystemBaseDir() + "/administration/templates/docs/";

   private static Log log = LogFactory.getLog(TemplateUtils.class);

   public static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy_MM_dd_HH.mm.ss");

   //TODO permettre à l'utilisateur d'utiliser un pattern différent ?
   public final static Pattern CLE_PATTERN = Pattern.compile("\\[{2}(.*?)\\]{2}");

   /**
    * Retourne l'extension du fichier du template
    */
   public static String getFileExtension(Template template){
      return getFileExtension(template.getFichier());
   }

   public static String getFileExtension(String nomFichier){
      if(null != nomFichier){
         return nomFichier.substring(nomFichier.lastIndexOf("."));
      }
      return null;
   }

   /**
    * Sauvegarde le fichier dans le répertoire Template
    * @param media le média à enregistrer
    */
   public static void saveDocTemplate(final Media media){
      final File dest = new File(TemplateUtils.TEMPLATE_DOC_FOLDER + media.getName());
      try{
         Files.copy(dest, media.getStreamData());
      }catch(final Exception e){
         log.error(e);
      }
   }

   /**
    * Sauvegarde le fichier dans le répertoire Template avec un nom unique, et le lie au template
    * @param media le média à enregistrer
    * @param template le template lié
    */
   public static void saveDocTemplate(final Media media, final Template template){
      String nomTemplate = template.getNom();
      //TODO A placer dans un Utils : remplace les charactères spéciaux par des "_"
      nomTemplate = Normalizer.normalize(nomTemplate, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "_");
      nomTemplate = nomTemplate.replaceAll("[-+.^:,\'\"&()]", "_");

      final String newFileName = FORMAT_DATE.format(new Date()) + "_" + nomTemplate + "." + media.getFormat();
      final File dest = new File(TemplateUtils.TEMPLATE_DOC_FOLDER + newFileName);
      try{
         Files.copy(dest, media.getStreamData());
         template.setFichier(newFileName);
      }catch(final Exception e){
         log.error(e);
      }
   }

   /**
    * Supprime un fichier doc Template
    * @param fileName nom du fichier
    */
   public static void deleteDocTemplate(final String fileName){
      new File(TemplateUtils.TEMPLATE_DOC_FOLDER + fileName).delete();
   }

   /**
    * Recherche dans un document Word .docx les Strings correspondants au pattern spécifié
    * @param document Le document Word DOCX
    */
   static List<String> extractStringsInFileFromPattern(final XWPFDocument document){

      final List<String> cles = new ArrayList<>();

      //Boucle sur les paragraphes
      for(final XWPFParagraph p : document.getParagraphs()){
         final String text = p.getText();
         if(null != text){
            final Matcher m = CLE_PATTERN.matcher(text);
            while(m.find()){
               for(int i = 0; i < m.groupCount(); i++){
                  cles.add(m.group(i));
               }
            }
         }
      }

      // Boucle sur les tableaux
      for(final XWPFTable tbl : document.getTables()){
         for(final XWPFTableRow row : tbl.getRows()){
            for(final XWPFTableCell cell : row.getTableCells()){
               for(final XWPFParagraph p : cell.getParagraphs()){
                  final String text = p.getText();
                  if(null != text){
                     final Matcher m = CLE_PATTERN.matcher(text);
                     if(m.find()){
                        for(int i = 0; i < m.groupCount(); i++){
                           cles.add(m.group(i));
                        }
                     }
                  }
               }
            }
         }
      }

      return cles;
   }

   /**
    * Recherche dans un document Word94 .doc les Strings correspondants au pattern spécifié
    * @param document Le document Word97 DOC
    */
   static List<String> extractStringsInFileFromPattern(final HWPFDocument document){

      final List<String> cles = new ArrayList<>();

      Range r = document.getRange();
      for(int i = 0; i < r.numSections(); ++i){
         Section s = r.getSection(i);
         for(int j = 0; j < s.numParagraphs(); j++){
            Paragraph p = s.getParagraph(j);
            for(int k = 0; k < p.numCharacterRuns(); k++){
               CharacterRun run = p.getCharacterRun(k);
               String text = run.text();
               final Matcher m = CLE_PATTERN.matcher(text);
               if(m.find()){
                  for(int l = 0; l < m.groupCount(); l++){
                     cles.add(m.group(l));
                  }
               }
            }
         }
      }

      return cles;
   }

   /**
    * Remplacement de texte dans un document Word .docx selon une map Clé à remplacer/valeur de remplacement
    * @param document le Document à traiter
    * @param keyValueMap la map Clé à remplacer/valeur de remplacement
    */
   static void replaceKeysByValues(final XWPFDocument document, final Map<String, String> keyValueMap){
      // Itération sur les paragraphes du document et remplacement des clés trouvées par la valeur contenue dans la map
      for(final XWPFParagraph p : document.getParagraphs()){
         String text = p.getText();
         if(null != text){
            for(final String cle : keyValueMap.keySet()){
               if(text.contains(cle)){
                  text = text.replace(cle, keyValueMap.get(cle));
                  final List<XWPFRun> runs = p.getRuns();
                  for(int i = runs.size() - 1; i > 0; i--){
                     p.removeRun(i);
                  }
                  final XWPFRun run = runs.get(0);
                  run.setText(text, 0);
               }
            }
         }
      }

      // Itération sur les tables du document et remplacement des clés trouvées par la valeur contenue dans la map
      for(final XWPFTable tbl : document.getTables()){
         for(final XWPFTableRow row : tbl.getRows()){
            for(final XWPFTableCell cell : row.getTableCells()){
               for(final XWPFParagraph p : cell.getParagraphs()){
                  String text = p.getText();
                  if(null != text){
                     for(final String cle : keyValueMap.keySet()){
                        if(text.contains(cle)){
                           text = text.replace(cle, keyValueMap.get(cle));
                           final List<XWPFRun> runs = p.getRuns();
                           for(int i = runs.size() - 1; i > 0; i--){
                              p.removeRun(i);
                           }
                           final XWPFRun run = runs.get(0);
                           run.setText(text, 0);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Remplacement de texte dans un document Word97 .doc selon une map Clé à remplacer/valeur de remplacement
    * @param document le Document à traiter
    * @param keyValueMap la map Clé à remplacer/valeur de remplacement
    */
   static void replaceKeysByValues(final HWPFDocument document, final Map<String, String> keyValueMap){
      Range r = document.getRange();
      for(int i = 0; i < r.numSections(); ++i){
         Section s = r.getSection(i);
         for(int j = 0; j < s.numParagraphs(); j++){
            Paragraph p = s.getParagraph(j);
            for(int k = 0; k < p.numCharacterRuns(); k++){
               CharacterRun run = p.getCharacterRun(k);
               String text = run.text();
               for(String cle : keyValueMap.keySet()){
                  if(text.contains(cle)){
                     run.replaceText(cle, keyValueMap.get(cle));
                  }
               }
            }
         }
      }
   }

   /**
    * Lit le fichier modèle docx et remplace les clés incluses dans ce fichier puis permet au client de le télécharger
    * par les valeurs correspondantes
    * @param template le template type Doc
    * @param objectToPrint l'objet à imprimer
    */
   static void replaceKeysInDocumentTemplateAndDownload(final Template template, final Object objectToPrint,
      final String outFileName){
      // TODO revoir le nom et le process de cette fonction
      final File fileIn = new File(TemplateUtils.TEMPLATE_DOC_FOLDER + template.getFichier());
      try( FileInputStream fis = new FileInputStream(fileIn.getAbsolutePath());){

         final List<CleImpression> cles = template.getCleImpressionList();

         final List<Object> objects = RechercheUtilsManager.getListeObjetsCorrespondants(objectToPrint, cles, null);

         final Map<String, String> cleValeurMap = new HashMap<>();

         loadClesValues(objects, cleValeurMap, cles);

         final String fileExtension = getFileExtension(template);

         final ByteArrayOutputStream boa = new ByteArrayOutputStream();

         switch(fileExtension){
            case ".docx":
               final XWPFDocument docxIn = new XWPFDocument(fis);
               replaceKeysByValues(docxIn, cleValeurMap);
               //Enregistrement du document modifié dans un stream
               docxIn.write(boa);
               break;
            case ".doc":
               final HWPFDocument docIn = new HWPFDocument(fis);
               replaceKeysByValues(docIn, cleValeurMap);
               //Enregistrement du document modifié dans un stream
               docIn.write(boa);
               break;
            default:
               break;
         }

         //Téléchargement du stream fichier côté client
         Filedownload.save(boa.toByteArray(), new MimetypesFileTypeMap().getContentType(fileIn), outFileName);

      }catch(final FileNotFoundException e){
         Clients.clearBusy();
         log.error(e);
         Messagebox.show(Labels.getLabel("template.messages.fichier.introuvable"), "Erreur", Messagebox.OK, Messagebox.ERROR);
      }catch(final Exception e){
         Clients.clearBusy();
         log.error(e);
         Messagebox.show(e.getMessage(), "Erreur", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Récupère les valeurs correspondantes aux clés et les enregistre dans la map CleVal
    * @param listObjects la liste des objets dans lesquels retrouver les valeurs
    * @param mapCleVal la map Clé Valeur dans laquelle seront enregistrés les résultats
    * @param cles la liste des clés dont il faut retrouver la valeur
    */
   public static void loadClesValues(final List<Object> listObjects, final Map<String, String> mapCleVal,
      final List<CleImpression> cles){
      // On itère les résultats
      final Iterator<CleImpression> itRes = cles.iterator();
      while(itRes.hasNext()){
         final CleImpression cle = itRes.next();
         /* On récupère l'entité depuis le champ du Resultat. */
         if(cle != null){
            if(cle.getChamp() != null){
               String champValeur = ObjectTypesFormatters
                  .formatObject(RechercheUtilsManager.getChampValueFromObjectList(cle.getChamp(), listObjects));
               champValeur = formatDuree(cle.getChamp(), champValeur);
               mapCleVal.put(cle.getNom(), champValeur);
            }
         }
      }
   }

   /**
    * Permet le formatage des durées
    * @param champ champ
    * @param champValeur valeur retournée du champ
    * @return la valeur formaté si durée, la valeur de départ si non.
    */
   private static String formatDuree(Champ champ, String champValeur){
      String dataType = champ.dataType().getType();
      if("calcule".equals(dataType)){
         dataType = champ.getChampAnnotation().getChampCalcule().getDataType().getType();
      }

      if("duree".equals(dataType) && null != champValeur && !"".equals(champValeur)){
         return ObjectTypesFormatters.formatDuree(new Duree(new Long(champValeur), Duree.SECONDE));
      }

      return champValeur;
   }

}
