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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.camel.ProducerTemplate;

import fr.aphp.tumorotek.interfacage.jaxb.hm.Dme;
import fr.aphp.tumorotek.interfacage.jaxb.hm.Formulaire;
import fr.aphp.tumorotek.interfacage.sender.ack.HmMessageSender;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.interfacage.RecepteurManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

public class HmMessageSenderImpl implements HmMessageSender {
	
	private ProducerTemplate camelTemplate;
	private EchantillonManager echantillonManager;
	private RecepteurManager recepteurManager;
	private final Integer recepteurId = 2;
	
	public void setCamelTemplate(ProducerTemplate ct) {
		this.camelTemplate = ct;
	}
	
	public void setEchantillonManager(EchantillonManager pM) {
		this.echantillonManager = pM;
	}

	public void setRecepteurManager(RecepteurManager rManager) {
		this.recepteurManager = rManager;
	}

	@Override
	public void sendMessage(TKAnnotableObject prel, String dosExtId, String url) {
		if (prel != null) {
			
			// time stamp
			String datef = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(Calendar.getInstance().getTimeInMillis());
			
			camelTemplate.sendBodyAndHeader("direct:ack-dme", 
					createHmSingleObjMessage(prel), 
					"CamelFileName", setFileName(null, false, null, datef));
			
			// ok file
			camelTemplate.sendBodyAndHeader("direct:ack-dme", 
					"", "CamelFileName", setFileName(null, true, null, datef));
			
			recepteurManager.updateRecepteurEnvoiNum(recepteurManager
									.findByIdManager(recepteurId), false);
		}
	}
	
	@Override
	public void sendMessages(List<TKAnnotableObject> objs, Integer b) {
		if (objs != null) {
			
			int buff = (b != null && b > 0) ? b : 100;
			
			// time stamp
			String datef = new SimpleDateFormat("yyyyMMddHHmmss")
							.format(Calendar.getInstance().getTimeInMillis());
			
			int counter = 0;
			Integer sendPart = 1;
			List<TKAnnotableObject> objsToSend = new ArrayList<TKAnnotableObject>();
			for (TKAnnotableObject tkObj : objs) {
				objsToSend.add(tkObj);
				counter ++;
				// envoi si compteur atteint
				if (counter == buff) {
					camelTemplate.sendBodyAndHeader("direct:ack-dme", 
							createHmListObjMessage(objsToSend), 
							"CamelFileName", setFileName(null, false, sendPart, datef));
					
					// ok file
					camelTemplate.sendBodyAndHeader("direct:ack-dme", 
							"", "CamelFileName", setFileName(null, true, sendPart, datef));
					
					counter = 0;
					sendPart++;
					objsToSend.clear();
				}
			}
			
			// envoi si liste n'est pas vide
			if (!objsToSend.isEmpty()) {
				camelTemplate.sendBodyAndHeader("direct:ack-dme", 
						createHmListObjMessage(objsToSend), 
						"CamelFileName", setFileName(null, false, sendPart, datef));
				
				// ok file
				camelTemplate.sendBodyAndHeader("direct:ack-dme", 
						"", "CamelFileName", setFileName(null, true, sendPart, datef));
			}
			
			recepteurManager.updateRecepteurEnvoiNum(recepteurManager
					.findByIdManager(recepteurId), false);
		}
	}	

	public Dme createHmSingleObjMessage(TKAnnotableObject prel) {
		Dme dme = new Dme();
			dme.getFormulaires()
					.add(new Formulaire((Prelevement) prel, true, 
					echantillonManager
						.findCountByPrelevementManager((Prelevement) prel)
							.intValue()));
		return dme;	
	}
	
	public Dme createHmListObjMessage(List<TKAnnotableObject> prels) {
		Dme dme = new Dme();
		if (prels != null) {
			for (TKAnnotableObject tkObj : prels) {
				dme.getFormulaires()
					.add(new Formulaire((Prelevement) tkObj, true, 
					echantillonManager
						.findCountByPrelevementManager((Prelevement) tkObj)
							.intValue()));
			}
		}
		return dme;	
	}
	
	@Override
	public String setFileName(TKAnnotableObject prel, boolean isOkFile, Integer part, 
			String currtime) {
		
		String out = "TUMTK_" + String.format("%06d", recepteurManager
				.findByIdManager(recepteurId).getEnvoiNum());
		
		out = out + "_" + currtime;
		// extension
		out = out + (part != null ? "_p" + part.toString() : "");
		out = out + (!isOkFile ? ".xml" : ".ok");
		return out;	
	}

	@Override
	public boolean useRecepteur(Recepteur r) {
		if (r != null && recepteurId.equals(r.getRecepteurId())) {
				// r.getLogiciel() != null
				// && r.getLogiciel().getNom().equals("HOPITAL MANAGER")r
				// && r.getIdentification().matches(".*DME.*")) {
			return true;
		}
		return false;
	}
}
