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
package fr.aphp.tumorotek.manager.impl.interfacage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.EmetteurManager;
import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.utils.Utils;

/**
 * @version 2.2.3-genno
 * @author Mathieu BARTHELEMY
 *
 */
public class InterfacageParsingUtilsImpl implements InterfacageParsingUtils
{

   private final Logger log = LoggerFactory.getLogger(InterfacageParsingUtils.class);

   private DossierExterneManager dossierExterneManager;

   private EmetteurManager emetteurManager;

   private TerminaleManager terminaleManager;

   public void setDossierExterneManager(final DossierExterneManager dManager){
      this.dossierExterneManager = dManager;
   }

   public void setEmetteurManager(final EmetteurManager eManager){
      this.emetteurManager = eManager;
   }

   public void setTerminaleManager(final TerminaleManager tManager){
      this.terminaleManager = tManager;
   }

   @Override
   public ConfigurationParsing initConfigurationParsing(final Element parent){
      final ConfigurationParsing conf = new ConfigurationParsing();

      // on récupère l'elt Configuration
      final Element confElt = parent.getChild("Configuration");
      if(confElt != null){
         // on récupère tous les séparateurs
         Element sepElt = confElt.getChild("SeparateurChamps");
         if(sepElt != null){
            conf.setSeparateurChamps(sepElt.getText());
         }else{
            log.info("initConfigurationParsing : " + "Balise 'SeparateurChamps' absente du fichier.");
         }
         sepElt = confElt.getChild("SeparateurComposants");
         if(sepElt != null){
            conf.setSeparateurComposants(sepElt.getText());
         }else{
            log.info("initConfigurationParsing : " + "Balise 'SeparateurComposants' absente du fichier.");
         }
         sepElt = confElt.getChild("SeparateurSousComposants");
         if(sepElt != null){
            conf.setSeparateurSousComposants(sepElt.getText());
         }else{
            log.info("initConfigurationParsing : " + "Balise 'SeparateurSousComposants' " + "absente du fichier.");
         }
         sepElt = confElt.getChild("BlocLibreKey");
         if(sepElt != null){
            conf.setBlocLibreKey(sepElt.getText());
         }else{
            log.info("initConfigurationParsing : " + "Balise 'BlocLibreKey' absente du fichier.");
         }
      }else{
         log.warn("initConfigurationParsing : Balise 'Configuration' absente du fichier.");
      }

      return conf;
   }

   @Override
   public Emetteur extractEmetteurFromFileToInjectInTk(final String fileXml, final String file, final String boiteFtp)
      throws IOException{

      Emetteur emetteurObj = null;

      if(file != null && fileXml != null && boiteFtp != null){
         // On crée une instance de SAXBuilder
         final SAXBuilder sxb = new SAXBuilder();
         Document document = null;
         try{
            //On crée un nouveau document JDOM avec en
            // argument le fichier XML
            document = sxb.build(new File(fileXml));

            // On initialise un nouvel élément racine avec
            // l'élément racine du document.
            final Element racine = document.getRootElement();
            // on récupère toutes les boites ftp que l'on va parcourir
            final List<?> boites = racine.getChildren("BoiteFtp");
            for(int i = 0; i < boites.size(); i++){
               final Element boite = (Element) boites.get(i);
               // on va traiter la boite correspondant à celle passée
               // en paramètre
               if(boite.getAttributeValue("nom").equals(boiteFtp)){
                  // init de la config pour parser le message
                  final ConfigurationParsing config = initConfigurationParsing(boite);

                  // EXTRACTION DE L'EMETTEUR
                  // extraction du bloc
                  String bloc = boite.getChild("Emetteur").getChildText("Bloc");
                  // extraction de l'emplacement
                  String emplacement = boite.getChild("Emetteur").getChildText("Emplacement");

                  // prepare le contenu du message pour l'extraction des informations
                  final List<Hashtable<String, List<String>>> contenu = parseFileToInjectInTk(config, file);

                  String emetteur = null;
                  if(bloc != null && emplacement != null){
                     // les infos sont dans le premier segment MSH (avant les ORC)
                     emetteur = getValueFromBlocAndEmplacement(contenu.get(0), config, bloc, emplacement);
                  }

                  // EXTRACTION DU SERVICE
                  // extraction du bloc
                  bloc = boite.getChild("Service").getChildText("Bloc");
                  // extraction de l'emplacement
                  emplacement = boite.getChild("Service").getChildText("Emplacement");

                  String service = null;
                  if(bloc != null && emplacement != null){
                     // les infos sont dans le premier segment MSH (avant les ORC)
                     service = getValueFromBlocAndEmplacement(contenu.get(0), config, bloc, emplacement);
                  }

                  // recherche de l'emetteur en fct de son nom
                  // et de son service
                  final List<Emetteur> emts = emetteurManager.findByIdentificationAndServiceManager(emetteur, service);

                  if(emts.size() == 1){
                     emetteurObj = emts.get(0);
                  }
               }
            }
         }catch(final Exception e){
            log.error(e.getMessage(), e);
         }

      }else{
         if(file == null){
            log.warn("extractEmetteurFromFileToInjectInTk : Le fichier à injecter dans TK est NULL");
         }
         if(fileXml == null){
            log.warn("extractEmetteurFromFileToInjectInTk : Le ficher XML de configuration est NULL");
         }
         if(file == null){
            log.warn("extractEmetteurFromFileToInjectInTk : La boite ftp ayant reçu le message est NULL");
         }
      }
      return emetteurObj;
   }

   @Override
   public String extractInboxParamFromFile(final String fileXml, final String file, final String boiteFtp, final String balise)
      throws IOException{

      if(file != null && fileXml != null && boiteFtp != null){
         //				&& emetteur != null) {
         // On crée une instance de SAXBuilder
         final SAXBuilder sxb = new SAXBuilder();
         Document document = null;
         try{
            //On crée un nouveau document JDOM avec en
            // argument le fichier XML
            document = sxb.build(new File(fileXml));

            // On initialise un nouvel élément racine avec
            // l'élément racine du document.
            final Element racine = document.getRootElement();
            // on récupère toutes les boites ftp que l'on va parcourir
            final List<?> boites = racine.getChildren("BoiteFtp");
            for(int i = 0; i < boites.size(); i++){
               // on va traiter la boite correspondant à celle passée
               // en paramètre
               if(((Element) boites.get(i)).getAttributeValue("nom").equals(boiteFtp)){
                  final List<?> mappings = ((Element) boites.get(i)).getChildren("MappingXml");

                  // trouve le premier param rempli pour cette balise
                  String inboxParam = null;
                  for(int j = 0; j < mappings.size(); j++){
                     inboxParam = ((Element) mappings.get(j)).getChildText(balise);
                     if(!StringUtils.isAllBlank(inboxParam)){
                        return ((Element) mappings.get(j)).getChildText(balise);
                     }
                  }
               }
            }
         }catch(final Exception e){
            log.error(e.getMessage(), e);
         }

      }else{
         if(file == null){
            log.warn("extractXMLFIleFromFileToInjectInTk : Le fichier à injecter dans TK est NULL");
         }
         if(fileXml == null){
            log.warn("extractXMLFIleFromFileToInjectInTk : Le ficher XML de configuration est NULL");
         }
         if(file == null){
            log.warn("extractXMLFIleFromFileToInjectInTk : La boite ftp ayant reçu le message est NULL");
         }
      }
      return null;
   }

   @Override
   public List<Hashtable<String, List<String>>> parseFileToInjectInTk(final ConfigurationParsing conf, final String file)
      throws IOException{

      final InputStream is = new java.io.ByteArrayInputStream(file.getBytes());

      final BufferedReader buff = new BufferedReader(new InputStreamReader(is));

      final List<Hashtable<String, List<String>>> contenu = new ArrayList<>();

      // communs à tous les ORCs
      final List<String> mshRecord = new ArrayList<>();
      final List<String> pidRecord = new ArrayList<>();
      final List<String> pv1Record = new ArrayList<>();
      Hashtable<String, List<String>> record = new Hashtable<>();

      try{
         String line;
         String currentOBXKey = "";
         // Lecture du fichier ligne par ligne. Cette boucle se termine
         // quand la méthode retourne la valeur null.
         if(conf.getSeparateurChamps() != null){
            while((line = buff.readLine()) != null){

               // on divise la ligne en fct du séparateur
               final List<String> values = new ArrayList<>();

               // dans le cas où un élément ne serait pas
               // présent, les séparateurs seront à coté (||)
               // pour pouvoir conserver cet élément vide dans
               // la liste finale, on injecte un espace
               final StringBuffer sb1 = new StringBuffer();
               sb1.append(conf.getSeparateurChamps());
               sb1.append(conf.getSeparateurChamps());
               final StringBuffer sb2 = new StringBuffer();
               sb2.append(conf.getSeparateurChamps());
               sb2.append(" ");
               sb2.append(conf.getSeparateurChamps());
               while(line.contains(sb1.toString())){
                  line = line.replace(sb1.toString(), sb2.toString());
               }

               // si la ligne se termine par un séparateur, on rajoute
               // un espace pour que cette élément soit pris en compte
               if(line.endsWith(conf.getSeparateurChamps()) || line.endsWith(conf.getSeparateurComposants())
                  || line.endsWith(conf.getSeparateurSousComposants())){
                  final StringBuffer tmp = new StringBuffer(line);
                  tmp.append(" ");
                  line = tmp.toString();
               }

               final StringTokenizer st = new StringTokenizer(line, conf.getSeparateurChamps());
               int cpt = 0;
               String key = "";
               while(st.hasMoreTokens()){
                  if(cpt == 0){
                     // le 1er élt de la ligne est sa clé
                     key = st.nextToken();
                     if(!key.equals("OBX")){
                        // ne replace pas la current key si PID après SPM
                        if(!key.equals("PID")){
                           currentOBXKey = key;
                        }
                     }else{
                        // si la clé est un OBX, on va créer
                        // une clé basée sur l'élt parent de
                        // cet OBX (pusiqu'il peut y en avoir
                        // plusieurs
                        final StringBuffer sb = new StringBuffer();
                        sb.append(currentOBXKey);
                        sb.append("_");
                        sb.append(key);
                        key = sb.toString();
                     }
                  }else{
                     values.add(st.nextToken());
                  }
                  ++cpt;
               }

               // si c'est un OBX, on ajoute l'id de celui-ci
               if(key.equals("SPM") || key.contains("OBX")){
                  if(!StringUtils.isBlank(values.get(0))){
                     key = key.concat(values.get(0));
                  }
                  if(key.matches("SPM[0-9]*")){
                     currentOBXKey = key;
                  }
               }

               // segments particuliers
               // segments communs à tous les ORCs MSH-PID-PV1 sont uniques
               if(key.contains("MSH")){
                  mshRecord.addAll(values);
               }else if(key.contains("PID")){
                  pidRecord.addAll(values);
               }else if(key.contains("PV1")){
                  pv1Record.addAll(values);
               }else if(key.contains("ORC")){ // si ORC alors preparaion d'un nouveau record

                  // si record n'est pas vide -> début d'un nouveau ORC
                  // ajout du record actuel au contenu
                  if(!record.isEmpty()){
                     contenu.add(record);
                     record = new Hashtable<>();
                  }

                  // if(values.get(2) != null){ // ORC-3 utilisé pour indicer
                  //	key = key.concat(values.get(2));
                  // }

                  // ajoute la ligne ORC au record après les segments communs
                  record.put("MSH", mshRecord);
                  record.put("PID", pidRecord);
                  record.put("PV1", pv1Record);
                  record.put(key, values);
               }else{ // OBX SPM....
                  // ajoute la ligne au record
                  record.put(key, values);
               }
            }

            // no-ORC message, persiste au moins le PID !!
            if(!mshRecord.isEmpty() && !record.containsKey("MSH")){
               record.put("MSH", mshRecord);
            }
            if(!pidRecord.isEmpty() && !record.containsKey("PID")){
               record.put("PID", pidRecord);
            }
            if(!pv1Record.isEmpty() && !record.containsKey("PV1")){
               record.put("PV1", pv1Record);
            }

            // si record n'est pas vide
            // ajout du dernier record au contenu
            if(!record.isEmpty()){
               contenu.add(record);
            }
         }
      }finally{
         // dans tous les cas, on ferme nos flux
         buff.close();
      }

      return contenu;
   }

   @Override
   public List<String> parseFileToExtractBlocsLibres(final ConfigurationParsing conf, final String file) throws IOException{
      final InputStream is = new java.io.ByteArrayInputStream(file.getBytes());

      final BufferedReader buff = new BufferedReader(new InputStreamReader(is));

      final List<String> blocsLibres = new ArrayList<>();

      try{
         String line;
         // Lecture du fichier ligne par ligne. Cette boucle se termine
         // quand la méthode retourne la valeur null.
         while((line = buff.readLine()) != null){
            if(conf.getSeparateurChamps() != null){
               // dans le cas où un élément ne serait pas
               // présent, les séparateurs seront à coté (||)
               // pour pouvoir conserver cet élément vide dans
               // la liste finale, on injecte un espace
               final StringBuffer sb1 = new StringBuffer();
               sb1.append(conf.getSeparateurChamps());
               sb1.append(conf.getSeparateurChamps());
               final StringBuffer sb2 = new StringBuffer();
               sb2.append(conf.getSeparateurChamps());
               sb2.append(" ");
               sb2.append(conf.getSeparateurChamps());
               while(line.contains(sb1.toString())){
                  line = line.replace(sb1.toString(), sb2.toString());
               }

               // si la ligne se termine par un séparateur, on rajoute
               // un espace pour que cette élément soit pris en compte
               if(line.endsWith(conf.getSeparateurChamps()) || line.endsWith(conf.getSeparateurComposants())
                  || line.endsWith(conf.getSeparateurSousComposants())){
                  final StringBuffer tmp = new StringBuffer(line);
                  tmp.append(" ");
                  line = tmp.toString();
               }

               final StringTokenizer st = new StringTokenizer(line, conf.getSeparateurChamps());
               int cpt = 0;
               String key = "";
               while(st.hasMoreTokens()){
                  // on ne s'occupe que du 1er élément de la ligne
                  if(cpt == 0){
                     // le 1er élt de la ligne est sa clé
                     key = st.nextToken();
                     // si la clé correspond à un bloc libre
                     if(key.equals(conf.getBlocLibreKey())){
                        // on enlève cette clé et le premier séparateur
                        final StringBuffer sb = new StringBuffer();
                        sb.append(conf.getBlocLibreKey());
                        sb.append(conf.getSeparateurChamps());
                        // on ajoute le contenu de la ligne au résultat
                        final String value = line.replace(sb.toString(), "");
                        blocsLibres.add(value);
                     }
                  }else{
                     st.nextToken();
                  }
                  ++cpt;
               }
            }
         }
      }finally{
         // dans tous les cas, on ferme nos flux
         buff.close();
      }

      return blocsLibres;
   }

   @Override
   public Hashtable<String, String> extractMappingValuesForThesaurs(final Element element){
      final Hashtable<String, String> mappings = new Hashtable<>();

      if(element != null && element.getName().equals("Modifier") && element.getAttribute("nom").getValue().equals("Thesaurus")){

         // on extrait les mappings
         final List<?> mappingThes = element.getChildren("MappingThes");
         for(int i = 0; i < mappingThes.size(); i++){
            String source = null;
            String tk = null;
            final Element thes = (Element) mappingThes.get(i);
            // on extrait chaque mapping pour le mettre dans la
            // hashtable
            if(thes.getChild("SourceThes") != null && thes.getChild("SourceThes").getText() != null
               && !thes.getChild("SourceThes").getText().equals("")){
               source = thes.getChild("SourceThes").getText();
            }
            if(thes.getChild("TkThes") != null && thes.getChild("TkThes").getText() != null
               && !thes.getChild("TkThes").getText().equals("")){
               tk = thes.getChild("TkThes").getText();
            }

            if(source != null && tk != null){
               mappings.put(source, tk);
            }
         }

      }

      return mappings;
   }

   @Override
   public DossierExterne initNewDossierExterne(final ConfigurationParsing config, final Hashtable<String, List<String>> contenu,
      final Element racine){
      final DossierExterne dossier = new DossierExterne();

      if(config != null && contenu != null && racine != null){
         // on récupère l'elt Configuration
         final Element confElt = racine.getChild("Configuration");
         // extraction du numéro de dossier
         if(confElt.getChild("NumeroDossier") != null){
            String identificationDossier = null;
            final String bloc = confElt.getChild("NumeroDossier").getChildText("BlocValue");
            final String emp = confElt.getChild("NumeroDossier").getChildText("Key");
            identificationDossier = getValueFromBlocAndEmplacement(contenu, config, bloc, emp);
            if(confElt.getChild("IndexDossier") != null && confElt.getChild("IndexDossier").getContentSize() > 0){
               final String blocIndex = confElt.getChild("IndexDossier").getChildText("BlocValue");
               final String empIndex = confElt.getChild("IndexDossier").getChildText("Key");
               final String dossierIdx = getValueFromBlocAndEmplacement(contenu, config, blocIndex, empIndex);
               if(dossierIdx != null){
                  identificationDossier = identificationDossier.concat("-").concat(dossierIdx);
               }
            }
            dossier.setIdentificationDossier(identificationDossier);
         }else{
            log.info("initNewDossierExterne : " + "La balise 'NumeroDossier' est absente du fichier");
         }

         // extraction du numéro de la date
         if(confElt.getChild("DateDossier") != null){
            final String bloc = confElt.getChild("DateDossier").getChildText("BlocValue");
            final String emp = confElt.getChild("DateDossier").getChildText("Key");
            final String dateStr = getValueFromBlocAndEmplacement(contenu, config, bloc, emp);

            final Date date = parseHl7Date(dateStr);

            Calendar cal = Calendar.getInstance();
            if(date != null){
               cal.setTime(date);
            }else{
               cal = null;
            }
            dossier.setDateOperation(cal);
         }else{
            log.info("initNewDossierExterne : " + "La balise 'DateDossier' est absente du fichier");
         }

         // extraction du numéro de l'opération
         if(confElt.getChild("OperationDossier") != null){
            final String bloc = confElt.getChild("OperationDossier").getChildText("BlocValue");
            final String emp = confElt.getChild("OperationDossier").getChildText("Key");
            dossier.setOperation(getValueFromBlocAndEmplacement(contenu, config, bloc, emp));
         }else{
            log.info("initNewDossierExterne : " + "La balise 'OperationDossier' est absente du fichier");
         }
      }else{
         if(config == null){
            log.warn("initNewDossierExterne : " + "La configuration pour parser le message " + "est NULL");
         }
         if(contenu == null){
            log.warn("initNewDossierExterne : " + "Le fichier à injecter dans TK " + "est NULL");
         }
         if(racine == null){
            log.warn("initNewDossierExterne : " + "La racine du fichier de mapping " + "est NULL");
         }
      }

      return dossier;
   }

   @Override
   public String getValueFromEmplacement(final List<String> values, final ConfigurationParsing conf, final String emplacement){

      String value = null;

      if(values != null && conf != null && emplacement != null){
         // on divise l'emplacement
         final List<String> empls = new ArrayList<>();
         if(emplacement.contains(".")){
            final StringTokenizer st = new StringTokenizer(emplacement, ".");
            while(st.hasMoreTokens()){
               empls.add(st.nextToken());
            }
         }else{
            empls.add(emplacement);
         }

         String niv1 = null;
         // on récupère la valeur de la liste plassée à l'emplacement
         // demandé (si elle existe
         if(values.size() >= Integer.parseInt(empls.get(0))){
            niv1 = values.get(Integer.parseInt(empls.get(0)) - 1);
            if(niv1.equals(" ") || niv1.equals("")){
               niv1 = null;
            }
         }

         // si un deuxième niveau de subdivision est demandé
         if(empls.size() > 1 && niv1 != null){
            // dans le cas où un élément ne serait pas
            // présent, les séparateurs seront à coté (~~)
            // pour pouvoir conserver cet élément vide dans
            // la liste finale, on injecte un espace
            final StringBuffer sb1 = new StringBuffer();
            sb1.append(conf.getSeparateurComposants());
            sb1.append(conf.getSeparateurComposants());
            final StringBuffer sb2 = new StringBuffer();
            sb2.append(conf.getSeparateurComposants());
            sb2.append(" ");
            sb2.append(conf.getSeparateurComposants());
            while(niv1.contains(sb1.toString())){
               niv1 = niv1.replace(sb1.toString(), sb2.toString());
            }

            // si la ligne commence par un séparateur, on rajoute
            // un espace pour que cette élément soit pris en compte
            if(niv1.startsWith(conf.getSeparateurComposants()) || niv1.startsWith(conf.getSeparateurSousComposants())){
               final StringBuffer tmp = new StringBuffer(" ");
               tmp.append(niv1);
               niv1 = tmp.toString();
            }

            // si la ligne se termine par un séparateur, on rajoute
            // un espace pour que cette élément soit pris en compte
            if(niv1.endsWith(conf.getSeparateurComposants()) || niv1.endsWith(conf.getSeparateurSousComposants())){
               final StringBuffer tmp = new StringBuffer(niv1);
               tmp.append(" ");
               niv1 = tmp.toString();
            }

            // on sépare la valeur en fct du séparateur
            // de composants
            final List<String> niv2s = new ArrayList<>();
            if(niv1.contains(conf.getSeparateurComposants())){
               final StringTokenizer st = new StringTokenizer(niv1, conf.getSeparateurComposants());
               while(st.hasMoreTokens()){
                  niv2s.add(st.nextToken());
               }

               // on extrait la valeur plassée à l'emplacement
               // demandé
               String niv2 = null;
               if(niv2s.size() >= Integer.parseInt(empls.get(1))){
                  niv2 = niv2s.get(Integer.parseInt(empls.get(1)) - 1);
                  if(niv2.equals(" ") || niv2.equals("")){
                     niv2 = null;
                  }
               }

               // si un troisième niveau de subdivision est demandé
               if(empls.size() > 2 && niv2 != null){
                  // dans le cas où un élément ne serait pas
                  // présent, les séparateurs seront à coté (~~)
                  // pour pouvoir conserver cet élément vide dans
                  // la liste finale, on injecte un espace
                  final StringBuffer sb11 = new StringBuffer();
                  sb11.append(conf.getSeparateurSousComposants());
                  sb11.append(conf.getSeparateurSousComposants());
                  final StringBuffer sb22 = new StringBuffer();
                  sb22.append(conf.getSeparateurSousComposants());
                  sb22.append(" ");
                  sb22.append(conf.getSeparateurSousComposants());
                  while(niv2.contains(sb11.toString())){
                     niv2 = niv2.replace(sb11.toString(), sb22.toString());
                  }

                  // si la ligne commence par un séparateur,
                  // on rajoute  un espace pour que cette élément
                  // soit pris en compte
                  if(niv2.startsWith(conf.getSeparateurSousComposants())){
                     final StringBuffer tmp = new StringBuffer(" ");
                     tmp.append(niv2);
                     niv2 = tmp.toString();
                  }

                  // si la ligne se termine par un séparateur,
                  // on rajoute un espace pour que cette élément
                  // soit pris en compte
                  if(niv2.endsWith(conf.getSeparateurSousComposants())){
                     final StringBuffer tmp = new StringBuffer(niv2);
                     tmp.append(" ");
                     niv2 = tmp.toString();
                  }

                  // on sépare la valeur en fct du séparateur
                  // de composants
                  final List<String> niv3s = new ArrayList<>();
                  if(niv2.contains(conf.getSeparateurSousComposants())){
                     final StringTokenizer st3 = new StringTokenizer(niv2, conf.getSeparateurSousComposants());
                     while(st3.hasMoreTokens()){
                        niv3s.add(st3.nextToken());
                     }

                     // on extrait la valeur plassée à l'emplacement
                     // demandé
                     String niv3 = null;
                     if(niv3s.size() >= Integer.parseInt(empls.get(2))){
                        niv3 = niv3s.get(Integer.parseInt(empls.get(2)) - 1);
                        if(niv3.equals(" ") || niv3.equals("")){
                           niv3 = null;
                        }
                     }

                     value = niv3;
                  }else{
                     if(empls.get(2).equals("1")){
                        value = niv2;
                     }else{
                        return null;
                     }
                  }
               }else{
                  value = niv2;
               }
            }else{
               if(empls.get(1).equals("1")){
                  value = niv1;
               }else{
                  return null;
               }
            }
         }else{
            value = niv1;
         }
      }

      // check only spaces
      // @since 2.1
      if(value != null && value.trim().equals("")){
         value = null;
      }

      return value;
   }

   @Override
   public String getValueFromBlocAndEmplacement(final Hashtable<String, List<String>> contenu, final ConfigurationParsing conf,
      final String bloc, final String emplacement){
      String value = null;

      if(contenu != null && conf != null && bloc != null && emplacement != null){
         if(contenu.containsKey(bloc)){
            value = getValueFromEmplacement(contenu.get(bloc), conf, emplacement);
         }else{
            log.info("getValueFromBlocAndEmplacement : " + "Le bloc '" + bloc + "' n'est pas présent dans le fichier");
         }
      }else{
         if(conf == null){
            log.warn("getValueFromBlocAndEmplacement : " + "La configuration pour parser le message " + "est NULL");
         }
         if(bloc == null){
            log.warn("getValueFromBlocAndEmplacement : " + "Le bloc dans lequel extraire la valeur " + "est NULL");
         }
         if(contenu == null){
            log.warn("getValueFromBlocAndEmplacement : Le contenu du fichier est NULL");
         }
         if(emplacement == null){
            log.warn("getValueFromBlocAndEmplacement : " + "L'emplacement de la valeur à extraire " + "est NULL");
         }
      }

      return value;
   }

   @Override
   public String getValueFromBlocLibre(final List<String> blocsLibres, final ConfigurationParsing conf, final String bloc,
      final String keyBloc, final String emplacement){
      String value = null;

      if(blocsLibres != null && conf != null && bloc != null && emplacement != null && keyBloc != null){

         int found = -1;
         int cpt = 0;
         // on parcourt tous les blocslibres
         while(cpt < blocsLibres.size() && found < 0){
            // on recherche le bon bloc libre
            if(blocsLibres.get(cpt).contains(bloc)){
               final List<String> values = new ArrayList<>();
               // on découpe le bloc libre
               final StringTokenizer st = new StringTokenizer(blocsLibres.get(cpt), conf.getSeparateurChamps());
               while(st.hasMoreTokens()){
                  values.add(st.nextToken());
               }

               // on va vérifier que c'est le bon bloc libre
               final String nomBloc = getValueFromEmplacement(values, conf, keyBloc);
               if(bloc.equals(nomBloc)){
                  found = cpt;
               }
            }
            cpt++;
         }

         // si on a trouvé le bon bloc libre
         if(found >= 0){
            final List<String> values = new ArrayList<>();
            // on découpe le bloc libre
            final StringTokenizer st = new StringTokenizer(blocsLibres.get(found), conf.getSeparateurChamps());
            while(st.hasMoreTokens()){
               values.add(st.nextToken());
            }

            value = getValueFromEmplacement(values, conf, emplacement);
         }else{
            log.info("getValueFromBlocLibre : " + "Le bloc libre '" + bloc + "' n'est pas présent dans le fichier");
         }
      }else{
         if(conf == null){
            log.warn("getValueFromBlocLibre : " + "La configuration pour parser le message " + "est NULL");
         }
         if(bloc == null){
            log.warn("getValueFromBlocLibre : " + "Le bloc dans lequel extraire la valeur " + "est NULL");
         }
         if(blocsLibres == null){
            log.warn("getValueFromBlocLibre : La liste des blocs libres est NULL");
         }
         if(emplacement == null){
            log.warn("getValueFromBlocLibre : " + "L'emplacement de la valeur à extraire " + "est NULL");
         }
      }
      return value;
   }

   @Override
   public String formateValueUsingFunction(final Element fonctionElt, String value, final Hashtable<String, List<String>> contenu,
      final ConfigurationParsing conf, final String bloc, final StringBuilder emplacementAdrl,
      final Hashtable<String, String> mappingValues){
      if(fonctionElt != null){
         final String fonction = fonctionElt.getAttributeValue("nom");

         if(fonction != null && value != null){
            // en fonction du nom de la fonction, on va appeler
            // la bonne méthode pour formater la valeur
            if("stringToLowerCase".equals(fonction)){
               value = Utils.stringToLowerCase(value);
            }else if("stringToUpperCase".equals(fonction)){
               value = Utils.stringToUpperCase(value);
            }else if("replaceCommaByDot".equals(fonction)){
               value = Utils.replaceCommaByDot(value);
            }else if("concat".equals(fonction)){
               value = Utils.concat(fonctionElt.getAttributeValue("sep"), new String[] {value,
                  getValueFromBlocAndEmplacement(contenu, conf, bloc, fonctionElt.getAttributeValue("key"))});
            }else if("emplacementAddr".equals(fonction)){
               if(fonctionElt.getAttributeValue("niveau").equals("conteneur")){

                  // Modifier
                  if(mappingValues != null && value != null && mappingValues.containsKey(value)){
                     value = mappingValues.get(value);
                  }

                  emplacementAdrl.append(value.trim());

                  value = null; // afin de ne pas enregistrer de valeur externe

               }else if(fonctionElt.getAttributeValue("niveau").equals("enceinte")){
                  // split les niveaux
                  final String[] enceintes = StringUtils.split(value, conf.getSeparateurComposants());
                  // couples enceinte + idx
                  String enc;
                  for(int i = 0; i < enceintes.length; i++){
                     enc = enceintes[i].trim().concat(enceintes[i + 1]).trim();
                     i++;
                     // Modifier
                     if(mappingValues != null && enc != null && mappingValues.containsKey(enc)){
                        enc = mappingValues.get(enc);
                     }
                     emplacementAdrl.append(".");
                     emplacementAdrl.append(enc);
                  }

                  value = null; // afin de ne pas enregistrer de valeur externe

               }else if(fonctionElt.getAttributeValue("niveau").equals("boite")){

                  if(fonctionElt.getAttributeValue("alias") != null && fonctionElt.getAttributeValue("alias").equals("true")){

                     final List<Terminale> terms = terminaleManager.findByAliasManager(value);

                     // remplace l'alias par le nom de la première boite trouvée
                     // TODO que faire un alias = plusieurs boites ? 
                     // comparer les niveaux précédents ???
                     if(!terms.isEmpty()){
                        value = terms.get(0).getNom();
                     }

                  }

                  emplacementAdrl.append(".");
                  emplacementAdrl.append(value);

                  value = null; // afin de ne pas enregistrer de valeur externe

               }else if(fonctionElt.getAttributeValue("niveau").equals("emplacement")){
                  // Char-Num 
                  final String[] coords = StringUtils.split(value, conf.getSeparateurComposants());
                  emplacementAdrl.append(".");
                  emplacementAdrl.append(coords[0].trim());
                  emplacementAdrl.append("-");
                  emplacementAdrl.append(coords[1].trim().replaceFirst("^0+(?!$)", ""));

                  value = emplacementAdrl.toString(); // l'adresse de stockage sera enregistrée comme valeur externe
               }
            }else{
               log.info("formateValueUsingFunction : " + "La fonction de formatage '" + fonction + "' n'existe pas");
            }
         }
      }
      return value;
   }

   @Override
   public List<DossierExterne> parseInterfacageXmlFile(final String xmlFile, final String message, final Emetteur emetteur,
      final boolean delFile, final int max, final String entiteId) throws Exception{ //TODO Refactorer

      final List<DossierExterne> dossiers = new ArrayList<>();

      final SAXBuilder sxb = new SAXBuilder();
      Document document = null;

      try{
         //On crée un nouveau document JDOM avec en
         // argument le fichier XML
         document = sxb.build(new File(xmlFile));

         // liste des fichiers associés à supprimer
         // après traitement du message
         final List<File> relatedFiles = new ArrayList<>();

         // On initialise un nouvel élément racine avec
         // l'élément racine du document.
         final Element racine = document.getRootElement();

         // on init la configuration du parsing
         final ConfigurationParsing config = initConfigurationParsing(racine);

         // on parse le message pour le transformer en hashtable
         final List<Hashtable<String, List<String>>> contenu = parseFileToInjectInTk(config, message);

         // on parse le message pour récupérer tous les blocs libres
         final List<String> contenuLibre = parseFileToExtractBlocsLibres(config, message);

         final List<BlocExterne> blocExternes = new ArrayList<>();
         final Hashtable<BlocExterne, List<ValeurExterne>> valeurExternes = new Hashtable<>();
         final Hashtable<Integer, BlocExterne> entitesBlocs = new Hashtable<>();
         DossierExterne dossierExterne = null;

         // adresse de stockage qui sera complétée au parcours
         // des segments SPM_OBX avec la fonction emplacementAdr
         StringBuilder emplacementAdrl;

         // Table d'association en cours pour le bloc issue du mapping.xml
         Hashtable<String, String> mappingValues;

         for(final Hashtable<String, List<String>> record : contenu){
            blocExternes.clear();
            valeurExternes.clear();
            entitesBlocs.clear();

            // init du dossier externe
            dossierExterne = initNewDossierExterne(config, record, racine);
            dossierExterne.setEmetteur(emetteur);

            // @since 2.2.3-genno
            // sendingApp = geno-derive -> entite ProdDerive pour dossier
            // devra être généralisé en liant entite à BoiteFTP
            if(entiteId != null){
               dossierExterne.setEntiteId(Integer.parseInt(entiteId));
            }

            int ordreBloc = 1;

            // separation blocs SPM des autres				
            final List<Element> dossierBlocs = new ArrayList<>();
            final List<Element> spmBlocs = new ArrayList<>();
            final List<?> blocs = racine.getChildren("Bloc");
            for(final Object e : blocs){
               if(!(((Element) e).getAttributeValue("nom")).matches("SPM.*")
                  || (emetteur.getIdentification().matches(".*Genno.*"))){ // SSU messages SPM = bloc principal
                  dossierBlocs.add((Element) e);
               }else{ // SPM{X} = bloc echantillon
                  spmBlocs.add((Element) e);
               }
            }

            // parcours des blocs génériques au dossier (patient, prel, code diag/les echantillons)
            for(int i = 0; i < dossierBlocs.size(); i++){

               final Element bloc = dossierBlocs.get(i);
               final List<?> mappings = bloc.getChildren("Mapping");
               // pour chaque Mapping
               for(int j = 0; j < mappings.size(); j++){
                  final Element mapping = (Element) mappings.get(j);
                  final Element tkElt = mapping.getChild("Tk");
                  final Element entiteElt = tkElt.getChild("Entite");
                  final Integer entite = entiteElt.getAttribute("idChamp").getIntValue();

                  // reset
                  mappingValues = null;

                  BlocExterne blocCurrent = null;
                  if(entitesBlocs.containsKey(entite)){
                     blocCurrent = entitesBlocs.get(entite);
                  }else{
                     // création d'un nouveau bloc
                     blocCurrent = new BlocExterne();
                     blocCurrent.setDossierExterne(dossierExterne);
                     blocCurrent.setEntiteId(entite);
                     blocCurrent.setOrdre(ordreBloc);
                     ++ordreBloc;
                     entitesBlocs.put(entite, blocCurrent);
                     blocExternes.add(blocCurrent);
                     valeurExternes.put(blocCurrent, new ArrayList<ValeurExterne>());
                  }

                  // création de la valeur externe
                  final ValeurExterne valeur = new ValeurExterne();
                  valeur.setBlocExterne(blocCurrent);
                  if(tkElt.getChild("ChampEntite") != null){
                     valeur.setChampEntiteId(tkElt.getChild("ChampEntite").getAttribute("idChamp").getIntValue());
                  }else{
                     valeur.setChampAnnotationId(tkElt.getChild("Annotation").getAttribute("idChamp").getIntValue());
                  }

                  final Element sourceElt = mapping.getChild("Source");
                  byte[] file = null;
                  String value = null;

                  // extraction de la valeur dans le fichier
                  // @since 2.2.3-genno ajout d'une balise Const pour ajouter un mapping entre 
                  // un champ TK et une constante				
                  if(sourceElt.getChild("Key") != null){
                     value = getValueFromBlocAndEmplacement(record, config, bloc.getAttributeValue("nom"),
                        sourceElt.getChildText("Key"));
                  }else if(sourceElt.getChild("Const") != null){
                     value = sourceElt.getChildText("Const");
                  }

                  // on regarde si un mapping existe entre une valeur du
                  // fichier et une valeur de TK
                  if(sourceElt.getChild("Modifier") != null){
                     if(sourceElt.getChild("Modifier").getAttribute("nom").getValue().equals("Thesaurus")){
                        // si c'est la cas, on extrait ces mappings
                        mappingValues = extractMappingValuesForThesaurs(sourceElt.getChild("Modifier"));

                        if(value != null && mappingValues.containsKey(value)){
                           value = mappingValues.get(value);
                        }
                     }else if(sourceElt.getChild("Modifier").getAttribute("nom").getValue().equals("Fichier")){
                        final File f = new File(sourceElt.getChild("Modifier").getChild("Path").getValue() + value);
                        if(f.exists()){
                           file = FileUtils.readFileToByteArray(f);
                           relatedFiles.add(f);
                        }else{
                           log.error(
                              "hl7.file.path.empty: " + sourceElt.getChild("Modifier").getChild("Path").getValue() + value);
                           value = null;
                        }

                     }
                  }

                  // on regarde si des fonctions de formatage sont
                  // demandées
                  if(sourceElt.getChild("Fonctions") != null){
                     final List<?> fonctions = sourceElt.getChild("Fonctions").getChildren("Fonction");
                     for(int k = 0; k < fonctions.size(); k++){
                        final Element fonction = (Element) fonctions.get(k);
                        if(fonction.getAttribute("nom") != null){
                           value = formateValueUsingFunction(fonction, value, record, config, bloc.getAttributeValue("nom"), null,
                              mappingValues);
                        }
                     }
                  }

                  // on n'ajoute la valeur que si elle n'est pas Null
                  if(value != null){
                     valeur.setValeur(value);
                     valeur.setContenu(file);
                     // insertion de la valeur dans la liste d'éléments
                     final List<ValeurExterne> vals = valeurExternes.get(blocCurrent);
                     vals.add(valeur);
                     valeurExternes.remove(blocCurrent);
                     valeurExternes.put(blocCurrent, vals);
                  }else{
                     log.info("La valeur du bloc '" + bloc.getAttributeValue("nom") + "' à l'emplacement '"
                        + sourceElt.getChildText("Key") + "' est NULL");
                  }
               }
            }

            // blocs SPM -> 1 blocExterne / echantillon
            // isole les segments SPM parents
            final List<String> spmKeys = new ArrayList<>();
            spmKeys.addAll(record.keySet().stream().filter(k -> k.matches("^SPM[0-9]+$")).collect(Collectors.toList()));
            Collections.sort(spmKeys);

            // boucle pour chaque lots de segments SPM*
            // pour l'extraction des valeurs sous la forme d'un bloc externe
            // représenté par sa SPM[0-9]+ key 
            for(final String spmK : spmKeys){

               // creation d'un bloc externe
               final BlocExterne blocSpm = new BlocExterne();
               blocSpm.setEntiteId(3); // echantillon
               blocSpm.setOrdre(ordreBloc);
               ++ordreBloc;
               valeurExternes.put(blocSpm, new ArrayList<ValeurExterne>());

               emplacementAdrl = new StringBuilder();

               // mappings specifiques SPM
               for(int i = 0; i < spmBlocs.size(); i++){

                  final Element bloc = spmBlocs.get(i);
                  final List<?> mappings = bloc.getChildren("Mapping");
                  // pour chaque Mapping
                  for(int j = 0; j < mappings.size(); j++){
                     final Element mapping = (Element) mappings.get(j);
                     final Element tkElt = mapping.getChild("Tk");

                     // reset
                     mappingValues = null;

                     // création de la valeur externe
                     final ValeurExterne valeur = new ValeurExterne();
                     valeur.setBlocExterne(blocSpm);
                     if(tkElt.getChild("ChampEntite") != null){
                        valeur.setChampEntiteId(tkElt.getChild("ChampEntite").getAttribute("idChamp").getIntValue());
                     }else{
                        valeur.setChampAnnotationId(tkElt.getChild("Annotation").getAttribute("idChamp").getIntValue());
                     }

                     final Element sourceElt = mapping.getChild("Source");
                     // extraction de la valeur dans le fichier
                     // AVEC modification de la clef de mapping SPM par la clef de contenu suffixée SPM1
                     // ex: SPM_OBX1 -> SPM1_OBX1 = mapKey variable
                     final String mapKey = bloc.getAttributeValue("nom").contains("_")
                        ? spmK.concat(bloc.getAttributeValue("nom").substring(bloc.getAttributeValue("nom").indexOf("_"))) : spmK;
                     String value = getValueFromBlocAndEmplacement(record, config, mapKey, sourceElt.getChildText("Key"));
                     byte[] file = null;

                     // on regarde si un mapping existe entre une valeur du
                     // fichier et une valeur de TK
                     if(sourceElt.getChild("Modifier") != null){
                        if(sourceElt.getChild("Modifier").getAttribute("nom").getValue().equals("Thesaurus")){
                           // si c'est la cas, on extrait ces mappings
                           mappingValues = extractMappingValuesForThesaurs(sourceElt.getChild("Modifier"));

                           if(value != null && mappingValues.containsKey(value)){
                              value = mappingValues.get(value);
                           }
                        }else if(sourceElt.getChild("Modifier").getAttribute("nom").getValue().equals("Fichier")){
                           final File f = new File(sourceElt.getChild("Modifier").getChild("Path").getValue() + value);
                           if(f.exists()){
                              file = FileUtils.readFileToByteArray(f);
                              relatedFiles.add(f);
                           }else{
                              log.error(
                                 "hl7.file.path.empty: " + sourceElt.getChild("Modifier").getChild("Path").getValue() + value);
                              value = null;
                           }

                        }
                     }

                     // on regarde si des fonctions de formatage sont
                     // demandées
                     // AVEC modification de la clef de mapping SPM par la clef de contenu suffixée SPM1
                     // ex: SPM_OBX1 -> SPM1_OBX1 = mapKey variable
                     if(sourceElt.getChild("Fonctions") != null){
                        final List<?> fonctions = sourceElt.getChild("Fonctions").getChildren("Fonction");
                        for(int k = 0; k < fonctions.size(); k++){
                           final Element fonction = (Element) fonctions.get(k);
                           if(fonction.getAttribute("nom") != null){
                              value = formateValueUsingFunction(fonction, value, record, config, mapKey, emplacementAdrl,
                                 mappingValues);
                           }
                        }
                     }

                     // on n'ajoute la valeur que si elle n'est pas Null
                     if(value != null){
                        valeur.setValeur(value);
                        valeur.setContenu(file);
                        // insertion de la valeur dans la liste d'éléments
                        valeurExternes.get(blocSpm).add(valeur);
                     }else{
                        log.info("La valeur du bloc '" + bloc.getAttributeValue("nom") + "' à l'emplacement '"
                           + sourceElt.getChildText("Key") + "' est NULL");
                     }
                  }
               }

               // ajout bloc
               // ajout bloc si au moins une valeur mappée
               if(!valeurExternes.get(blocSpm).isEmpty()){
                  blocExternes.add(blocSpm);
               }
            }

            // on récupère tous les blocs libres
            final List<?> blocsLibres = racine.getChildren("BlocLibre");
            for(int i = 0; i < blocsLibres.size(); i++){
               final Element blocLibre = (Element) blocsLibres.get(i);
               final List<?> mappings = blocLibre.getChildren("Mapping");
               final Element sourceBloc = blocLibre.getChild("Source");
               final Element keyBloc = sourceBloc.getChild("Key");
               // pour chaque Mapping
               for(int j = 0; j < mappings.size(); j++){
                  final Element mapping = (Element) mappings.get(j);
                  final Element tkElt = mapping.getChild("Tk");
                  final Element entiteElt = tkElt.getChild("Entite");
                  final Integer entite = entiteElt.getAttribute("idChamp").getIntValue();

                  BlocExterne blocCurrent = null;
                  if(entitesBlocs.containsKey(entite)){
                     blocCurrent = entitesBlocs.get(entite);
                  }else{
                     // création d'un nouveau bloc
                     blocCurrent = new BlocExterne();
                     blocCurrent.setDossierExterne(dossierExterne);
                     blocCurrent.setEntiteId(entite);
                     blocCurrent.setOrdre(ordreBloc);
                     ++ordreBloc;
                     entitesBlocs.put(entite, blocCurrent);
                     blocExternes.add(blocCurrent);
                     valeurExternes.put(blocCurrent, new ArrayList<ValeurExterne>());
                  }

                  // création de la valeur externe
                  final ValeurExterne valeur = new ValeurExterne();
                  valeur.setBlocExterne(blocCurrent);
                  if(tkElt.getChild("ChampEntite") != null){
                     valeur.setChampEntiteId(tkElt.getChild("ChampEntite").getAttribute("idChamp").getIntValue());
                  }else{
                     valeur.setChampAnnotationId(tkElt.getChild("Annotation").getAttribute("idChamp").getIntValue());
                  }

                  final Element sourceElt = mapping.getChild("Source");
                  // extraction de la valeur dans le fichier
                  String value = getValueFromBlocLibre(contenuLibre, config, blocLibre.getAttributeValue("nom"),
                     keyBloc.getText(), sourceElt.getChildText("Key"));

                  byte[] file = null;

                  // on regarde si un mapping existe entre une valeur du
                  // fichier et une valeur de TK
                  if(sourceElt.getChild("Modifier") != null && value != null){
                     if(sourceElt.getChild("Modifier").getAttribute("nom").getValue().equals("Thesaurus")){
                        // si c'est la cas, on extrait ces mappings
                        mappingValues = extractMappingValuesForThesaurs(sourceElt.getChild("Modifier"));

                        if(mappingValues.containsKey(value)){
                           value = mappingValues.get(value);
                        }
                     }else if(sourceElt.getChild("Modifier").getAttribute("nom").getValue().equals("Fichier")){
                        final File f = new File(sourceElt.getChild("Modifier").getChild("Path").getValue() + value);
                        if(f.exists()){
                           file = FileUtils.readFileToByteArray(f);
                           relatedFiles.add(f);
                        }else{
                           log.error(
                              "hl7.file.path.empty: " + sourceElt.getChild("Modifier").getChild("Path").getValue() + value);
                           value = null;
                        }
                     }
                  }

                  // on regarde si des fonctions de formatage sont
                  // demandées
                  if(sourceElt.getChild("Fonctions") != null){
                     final List<?> fonctions = sourceElt.getChild("Fonctions").getChildren("Fonction");
                     for(int k = 0; k < fonctions.size(); k++){
                        final Element fonction = (Element) fonctions.get(k);
                        if(fonction.getAttribute("nom") != null){
                           value = formateValueUsingFunction(fonction, value, null, null, null, null, null);
                        }
                     }
                  }

                  // on n'ajoute la valeur que si elle n'est pas Null
                  if(value != null){
                     valeur.setValeur(value);
                     valeur.setContenu(file);
                     // insertion de la valeur dans la liste d'éléments
                     final List<ValeurExterne> vals = valeurExternes.get(blocCurrent);
                     vals.add(valeur);
                     valeurExternes.remove(blocCurrent);
                     valeurExternes.put(blocCurrent, vals);
                  }else{
                     log.info("La valeur du bloc libre '" + blocLibre.getAttributeValue("nom") + "' à l'emplacement '"
                        + sourceElt.getChildText("Key") + "' est NULL");
                  }
               }
            }

            // enregistrement du dossier externe
            dossierExterneManager.createObjectManager(dossierExterne, emetteur, blocExternes, valeurExternes, max);

            dossiers.add(dossierExterne);

            // supprimes les fichiers associés si tout s'est bien passé
            if(delFile){
               for(final File file : relatedFiles){
                  log.info("deletion du fichier: " + file.getName());
                  file.delete();
               }
            }
         }
      }catch(final Exception e){
         log.error(e.getMessage());
      }
      return dossiers;
   }

   @Override
   public Date parseHl7Date(final String dateStr){
      // on transforme le string en date
      Date date = null;
      if(dateStr != null){
         if(dateStr.equals("\"\"")){
            return null;
         }
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
         try{
            date = sdf.parse(dateStr);
         }catch(final ParseException e){
            sdf = new SimpleDateFormat("yyyyMMddHHmm");
            try{
               date = sdf.parse(dateStr);
            }catch(final ParseException e1){
               sdf = new SimpleDateFormat("yyyyMMdd");
               try{
                  date = sdf.parse(dateStr);
               }catch(final ParseException e2){
                  log.error("hl7 TS date parse exception");
                  throw new RuntimeException(e2);
               }
            }
         }
      }
      return date;
   }

}
