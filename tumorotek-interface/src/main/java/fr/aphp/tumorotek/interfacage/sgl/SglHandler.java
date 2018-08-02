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

import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.component.hl7.HL7DataFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.model.bundles.ResourceBundleTumo;
import fr.aphp.tumorotek.model.interfacage.Emetteur;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;

/**
 * CLasse de manipulation des messages venant des SGLs routés par
 * Camel.
 * Date: 08/11/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class SglHandler
{

   private final Log log = LogFactory.getLog(SglHandler.class);

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
    * Methode d'entree des messages venant du SGL APIX
    * routées par Camel. 
    * @param body
    * @throws HL7Exception 
    */
   public void handleMessageSGL(final String body) throws HL7Exception{

      String sendingApp = null;

      final Message msg = myHl7.getParser().parse(body);

      // les messages venant de APIX et DIAMIC sont ORU_R01 v2.5
      // sendingApp = ((ORU_R01) body.getMessage())
      sendingApp = ((ORU_R01) msg).getMSH().getSendingApplication().getHd1_NamespaceID().getValue();

      handleMessage(body, sendingApp);
   }

   /**
    * Traite le message HL7 pour l'enregistrer dans la base de données 
    * interfacages.
    * @param body message HL7 converti en String par Camel
    * @param inbox nom de la boite dans le fichier de configuration
    */
   public void handleMessage(final String body, final String inbox){

      ResourceBundle res = null;

      String inboxesProp = null;
      // tests only
      //		"/home/mathieu2/apache-tomcat-7.0.40/conf/Catalina/localhost/sgl/inboxes.xml";

      if(resourceBundleTumo.doesResourceBundleExists("tumorotek.properties")){
         res = resourceBundleTumo.getResourceBundle("tumorotek.properties");
         if(res != null){
            inboxesProp = resourceBundleTumo.getResourceBundle("tumorotek.properties").getString("INTERFACAGES_INBOXES");
         }
      }

      if(inboxesProp != null){

         Emetteur emet;
         try{
            emet = interfacageParsingUtils.extractEmetteurFromFileToInjectInTk(inboxesProp, body, inbox);
            log.debug(emet);
            final String sglMap = interfacageParsingUtils.extractXMLFIleFromFileToInjectInTk(inboxesProp, body, inbox);
            // , emet);
            log.debug(sglMap);
            interfacageParsingUtils.parseInterfacageXmlFile(sglMap, body, emet, true, getMaxDossierTableSize());

         }catch(final Exception e){
            log.error("TK interfacage configuration: ");
            log.error(e);
            throw new RuntimeException(e);
         }

      }else{
         throw new RuntimeException("Interfacage SGL: " + "tumorotek.properties not found!");
      }

   }

   public int getMaxDossierTableSize(){
      int max = 2000;

      try{
         Context ctx;
         ctx = new InitialContext();
         final Integer maxEnvV = (Integer) ctx.lookup("java:comp/env/interfacage/maxSglTableSize");
         if(maxEnvV != null){
            max = maxEnvV.intValue();
         }
      }catch(final Exception e){
         e.printStackTrace();
      }
      return max;
   }

}
