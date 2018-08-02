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
package fr.aphp.tumorotek.interfacage.sender.ack.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.interfacage.sender.ack.TumoLinkUrd;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.model.v24.message.UDM_Q05;
import ca.uhn.hl7v2.model.v24.segment.DSP;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.URD;

public class TumoLinkUrdImpl implements TumoLinkUrd
{

   private final Log log = LogFactory.getLog(TumoLinkUrdImpl.class);

   private ProducerTemplate camelTemplate;

   public void setCamelTemplate(final ProducerTemplate ct){
      this.camelTemplate = ct;
   }

   @Override
   public void sendMessage(final TKAnnotableObject prel, final String dosExtId, final String url){

      if(prel != null && dosExtId != null && url != null){
         final String datef = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
         try{
            camelTemplate.sendBodyAndHeader("direct:ack-sgl", createUDMMessage((Prelevement) prel, dosExtId, url).encode(),
               "CamelFileName", setFileName(prel, false, null, datef));

            // ok file
            camelTemplate.sendBodyAndHeader("direct:ack-sgl", "", "CamelFileName", setFileName(prel, true, null, datef));

            log.debug("Message send: " + dosExtId);
         }catch(final Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
         }
      }
   }

   public UDM_Q05 createUDMMessage(final Prelevement prel, final String dosExtId, final String url)
      throws HL7Exception, IOException{

      final UDM_Q05 udm = new UDM_Q05();
      udm.initQuickstart("UDM", "Q05", "P");

      //MSH
      final MSH mshSegment = udm.getMSH();
      mshSegment.getSendingApplication().getNamespaceID().setValue("TumoroteK");
      mshSegment.getReceivingApplication().getNamespaceID().setValue("DIAMIC");
      final Calendar now = Calendar.getInstance();
      mshSegment.getMsh7_DateTimeOfMessage().getTimeOfAnEvent().setDateMinutePrecision(now.get(Calendar.YEAR),
         now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR), now.get(Calendar.MINUTE));
      mshSegment.getMsh9_MessageType().getMessageStructure().clear();
      mshSegment.getMsh10_MessageControlID().setValue("TKUDIA000" + mshSegment.getMsh10_MessageControlID().getValue());

      //URD
      final URD urdSegment = udm.getURD();
      urdSegment.getUrd1_RUDateTime().getTimeOfAnEvent().setDateMinutePrecision(now.get(Calendar.YEAR),
         now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR), now.get(Calendar.MINUTE));
      urdSegment.getUrd2_ReportPriority().setValue("R");

      final XCN msh3 = urdSegment.getRUWhoSubjectDefinition(0);
      msh3.getIDNumber().setValue(dosExtId);
      msh3.getXcn2_FamilyName().getSurname().setValue("TK_HTTP");

      urdSegment.getRUWhoSubjectDefinition(0);
      urdSegment.getUrd7_RUResultsLevel().setValue("T");

      //DSP
      final DSP dspSegment = udm.getDSP();
      dspSegment.getDataLine().setValue(url);

      log.debug(udm);

      return udm;

   }

   @Override
   public String setFileName(final TKAnnotableObject prel, final boolean isOkFile, final Integer part, final String currtime){
      return ((Prelevement) prel).getPrelevementId() + "_" + currtime + (!isOkFile ? ".hl7" : ".ok");
   }

   @Override
   public boolean useRecepteur(final Recepteur r){
      if(r != null && r.getLogiciel() != null && r.getLogiciel().getNom().equals("DIAMIC")
         && r.getIdentification().matches(".*ACK.*")){
         return true;
      }
      return false;
   }

   @Override
   public void sendMessages(final List<TKAnnotableObject> objs, final Integer b){}

}
