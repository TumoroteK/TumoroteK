package fr.aphp.tumorotek.action.impression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.io.RechercheUtils;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe fournissant les méthodes et propriétés statiques pour les Templates
 * @author 7007168
 *
 */
public class TemplateUtils
{

   //TODO 7007168 Revoir l'emplacement et la déclaration du dossier contenant les documents templates
   /**
    * Dossier où sont stockés les documents Template
    */
   public static String TEMPLATE_DOC_FOLDER = SessionUtils.getSystemBaseDir() + "/administration/templates/docs/";

   private static Log log = LogFactory.getLog(TemplateUtils.class);

   public static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy_mm_dd_HH.mm.ss");

   /**
    * Sauvegarde le fichier dans le répertoire Template
    * @param media le média à enregistrer
    */
   public static void saveDocTemplate(final Media media){
      final File dest = new File(TemplateUtils.TEMPLATE_DOC_FOLDER + media.getName());
      try{
         Files.copy(dest, media.getStreamData());
      }catch(final Exception e){
         //TODO Auto-generated catch-block
         log.error(e);
      }
   }

   /**
    * Sauvegarde le fichier dans le répertoire Template avec un nom unique, et le lie au template
    * @param media le média à enregistrer
    * @param template le template lié
    */
   public static void saveDocTemplate(final Media media, final Template template){
      final String newFileName = FORMAT_DATE.format(new Date()) + "_" + template.getNom() + "." + media.getFormat();
      final File dest = new File(TemplateUtils.TEMPLATE_DOC_FOLDER + newFileName);
      try{
         Files.copy(dest, media.getStreamData());
         template.setFichier(newFileName);
      }catch(final Exception e){
         //TODO Auto-generated catch-block
         e.printStackTrace();
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
    * Recherche dans un document Word docx et extrait les Strings correspondants au pattern spécifié
    * @param document Le document Word DOCX
    * @param pattern pattern/regex à extraire
    */
   static List<String> extractStringsInFileFromPattern(final XWPFDocument document, final Pattern pattern){

      final List<String> cles = new ArrayList<>();

      //Boucle sur les paragraphes
      for(final XWPFParagraph p : document.getParagraphs()){
         final String text = p.getText();
         if(null != text){
            final Matcher m = pattern.matcher(text);
            while(m.find()){
               for(int i = 0; i < m.groupCount(); i++){
                  if(!cles.contains(m.group(i))){
                     ;
                  }
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
                     final Matcher m = pattern.matcher(text);
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
    * Remplacement de texte dans un document selon une map Clé à remplacer/valeur de remplacement
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
    * TODO 7007168 revoir le nom et le process de cette fonction
    * Lit le fichier modèle docx et remplace les clés incluses dans ce fichier puis permet au client de le télécharger
    * par les valeurs correspondantes
    * @param template le template type Doc
    * @param objectToPrint l'objet à imprimer
    */
   static void replaceKeysInDocumentTemplateAndDownload(final Template template, final Object objectToPrint){
      final File fileIn = new File(TemplateUtils.TEMPLATE_DOC_FOLDER + template.getFichier());
      try( FileInputStream fis = new FileInputStream(fileIn.getAbsolutePath());){

         final XWPFDocument documentIn = new XWPFDocument(fis);

         final List<CleImpression> cles = ManagerLocator.getCleManager().findByTemplateManager(template);

         final List<Object> objects = RechercheUtilsManager.getListeObjetsCorrespondants(objectToPrint, cles, null);

         final Map<String, String> cleValeurMap = new HashMap<>();

         loadClesValues(objects, cleValeurMap, cles);

         replaceKeysByValues(documentIn, cleValeurMap);

         //Enregistrement du document modifier dans un stream
         final ByteArrayOutputStream boa = new ByteArrayOutputStream();
         documentIn.write(boa);

         //Téléchargement du stream fichier côté client
         final String fileExtension = template.getFichier().substring(template.getFichier().lastIndexOf("."));
         final String outFileName = template.getNom() + "_" + objectToPrint.toString() + fileExtension;
         Filedownload.save(boa.toByteArray(), new MimetypesFileTypeMap().getContentType(fileIn), outFileName);

      }catch(final FileNotFoundException e){
         Clients.clearBusy();
         e.printStackTrace();
         //FIXME Probleme de logger, pas de trace ?
         log.error(e);
         Messagebox.show("Fichier Template Introuvable", "Erreur", Messagebox.OK, Messagebox.ERROR);
      }catch(final Exception e){
         Clients.clearBusy();
         e.printStackTrace();
         //FIXME Probleme de logger, pas de trace ?
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
               final String champValeur = ObjectTypesFormatters.formatObject(RechercheUtilsManager.getChampValueFromObjectList(cle.getChamp(), listObjects));
               if(null == champValeur){
                  throw new TKException(
                     "Le champ " + cle.getChamp() + " correspondant à la clé " + cle.getNom() + " n'est pas renseigné");
               }
               mapCleVal.put(cle.getNom(), champValeur);
            }
         }
      }
   }
}
