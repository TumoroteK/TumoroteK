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
package fr.aphp.tumorotek.interfacage.sgl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.camel.component.hl7.HL7DataFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.message.SSU_U03;
import fr.aphp.tumorotek.manager.impl.interfacage.ConfigurationParsing;
import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.model.bundles.ResourceBundleTumo;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;

/**
 * CLasse de manipulation des messages venant des SGLs routés par
 * Camel.
 * Date: 08/11/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public class SglHandler
{

   private final static Integer DEFAULT_MAX_DOSSIER_TABLE_SIZE = 2000;

   private final Logger log = LoggerFactory.getLogger(SglHandler.class);

   private InterfacageParsingUtils interfacageParsingUtils;

   private ResourceBundleTumo resourceBundleTumo;

   private HL7DataFormat myHl7;

   public void setInterfacageParsingUtils(final InterfacageParsingUtils iU){
      this.interfacageParsingUtils = iU;
   }

   public void setResourceBundleTumo(final ResourceBundleTumo rTumo){
      this.resourceBundleTumo = rTumo;
   }

   public void setMyHl7(final HL7DataFormat my){
      this.myHl7 = my;
   }

   /**
    * Methode d'entree des messages venant des SGL APIX/DIAMIC
    * routés par Camel.
    * @param body
    * @throws HL7Exception
    * @since 2.2.3-genno
    */
   public void handleMessageSGL_ORU(final String body) throws HL7Exception{

      String sendingApp = null;

      final Message msg = myHl7.getParser().parse(body);

      // les messages venant de APIX et DIAMIC sont ORU_R01 v2.5
      // sendingApp = ((ORU_R01) body.getMessage())
      sendingApp = ((ORU_R01) msg).getMSH().getSendingApplication().getHd1_NamespaceID().getValue();

      handleMessage(body, sendingApp);
   }

   /**
    * Methode d'entree des messages venant du SGL GENNO
    * routés par Camel.
    * @param body
    * @throws HL7Exception
    * @throws IOException
    * @since 2.2.3-genno
    */
   public void handleMessageSGL_SSU(final String body) throws HL7Exception, IOException{

      String sendingApp = null;

      final Message msg = myHl7.getParser().parse(body);

      boolean skipMessage = false;

      // les messages venant de APIX et DIAMIC sont ORU_R01 v2.5
      // sendingApp = ((ORU_R01) body.getMessage())
      sendingApp = ((SSU_U03) msg).getMSH().getSendingApplication().getHd1_NamespaceID().getValue();

      String prelNature = null;
      String spm2 = null;
      String spmObx10 = null;

      // GENNO impose ce vilain hack
      // change la boite FTP en fonction du type SPM -> derive
      if(sendingApp.equalsIgnoreCase("Genno")){
         // ne respecte pas la norme SSU_U03...? en tous
         // en tous cas pas les superstructures HAPI
         // impossible d'utiliser HAPI

         final ConfigurationParsing config = new ConfigurationParsing();
         config.setSeparateurChamps("|");
         config.setSeparateurComposants("^");
         config.setSeparateurSousComposants("~");

         // on parse le message pour le transformer en hashtable
         final Hashtable<String, List<String>> contenu = interfacageParsingUtils.parseFileToInjectInTk(config, body).get(0);

         prelNature = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, config, "SPM", "5.2.1");

         log.debug("GENNO message SSU_U03 SPM-2 nature prélèvement: " + prelNature);

         spm2 = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, config, "SPM", "2");

         log.debug("GENNO message SSU_U03 SPM-2 code prélèvement: " + spm2);

         spmObx10 = interfacageParsingUtils.getValueFromBlocAndEmplacement(contenu, config, "SPM_OBX10", "5");

         log.debug("GENNO message SSU_U03 SPM_OBX-10 code prélèvement primaire: " + spmObx10);

         // si SPM-2 = SPM_OBX-10 alors transmission = prélèvement primaire
         if(spm2 != null && !spm2.equals(spmObx10)){
            sendingApp = sendingApp.concat("-DERIVE"); // redirection manuelle de la boiteFTP vers GENNO-DERIVE
            log.debug("GENNO message type dérivé redirigé vers BoiteFTP GENNO-DERIVE");

            // skips message si la nature du dérivé ne correspond pas aux natures attendues
            if(resourceBundleTumo.doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)
               && resourceBundleTumo.getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME)
                  .containsKey(TkParam.GENNO_DERIVES_NATURES.getKey())
               && !StringUtils.isBlank(resourceBundleTumo.getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME)
                  .getString(TkParam.GENNO_DERIVES_NATURES.getKey()))){
               skipMessage = !Arrays.asList(resourceBundleTumo.getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME)
                  .getString(TkParam.GENNO_DERIVES_NATURES.getKey()).split(",")).contains(prelNature);
            }
         }
      }

      if(!skipMessage){
         handleMessage(body, sendingApp);
      }else{
         log.info("GENNO message SSU_U03 " + spm2 + " non pris en compte - nature du dérivé filtrée: " + prelNature);
      }
   }

   /**
    * Traite le message HL7 pour l'enregistrer dans la base de données
    * interfacages.
    * @param body message HL7 converti en String par Camel
    * @param application émettrice, doit correspondre nom de la boite dans le fichier de configuration
    * @return dossiers créés
    */
   public void handleMessage(final String body, final String sendingApp){

      ResourceBundle res = null;

      String inboxesProp = null;
      // tests only
      //		"/home/mathieu2/apache-tomcat-7.0.40/conf/Catalina/localhost/sgl/inboxes.xml";

      if(resourceBundleTumo.doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
         res = resourceBundleTumo.getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);
         if(res != null){
            inboxesProp = resourceBundleTumo.getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME)
               .getString(TkParam.INTERFACAGES_INBOXES.getKey());
         }
      }

      if(inboxesProp != null){

         Emetteur emet;
         try{
            emet = interfacageParsingUtils.extractEmetteurFromFileToInjectInTk(inboxesProp, body, sendingApp);
            log.debug("Emetteur: {}", emet);
            final String sglMap = interfacageParsingUtils.extractInboxParamFromFile(inboxesProp, body, sendingApp, "XML");
            // , emet);
            log.debug(sglMap);
            interfacageParsingUtils.parseInterfacageXmlFile(sglMap, body, emet, true, getMaxDossierTableSize(),
               interfacageParsingUtils.extractInboxParamFromFile(inboxesProp, body, sendingApp, "EntiteId"));

         }catch(final Exception e){
            log.error("TK interfacage configuration: ");
            log.error("An error occurred: {}", e.toString()); 
            throw new RuntimeException(e);
         }

      }else{
         throw new RuntimeException("Interfacage SGL: " + TumorotekProperties.TUMO_PROPERTIES_PATH + " not found!");
      }

   }

   public int getMaxDossierTableSize(){
      Integer max = DEFAULT_MAX_DOSSIER_TABLE_SIZE;

      final String value = TkParam.SGL_MAX_TABLE_SIZE.getValue();

      try{
         max = Integer.parseInt(value);
      }catch(final Exception e){
         log.warn(
            "Valeur [" + value + "] invalide, utilisation de la valeur par défaut (" + DEFAULT_MAX_DOSSIER_TABLE_SIZE + ")");
      }
      return max;
   }

}
