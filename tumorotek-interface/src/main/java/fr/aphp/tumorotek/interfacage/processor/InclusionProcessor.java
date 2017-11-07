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
package fr.aphp.tumorotek.interfacage.processor;

import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import fr.aphp.tumorotek.interfacage.PatientHandler;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.CreateRequest;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.CreateResponse;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.StudySubjectType;

public class InclusionProcessor implements Processor {
	
	private PatientHandler patientHandler;
    
	public void setPatientHandler(PatientHandler pr) {
		this.patientHandler = pr;
	}

	public void process(Exchange exchange) throws Exception {
        CreateRequest request = exchange.getIn().getBody(CreateRequest.class);
       
        Iterator<StudySubjectType> it = request.getStudySubject().iterator();
        
        CreateResponse response = new CreateResponse();
        try {	
        	while (it.hasNext()) {
        		patientHandler.handleInclusionMelbase(it.next());
        	}
        } catch (RuntimeException e) {
        	response.setLabel("error");
        	response.setResult(e.getMessage());
			response.getError().add(e.getMessage());
		}
        
        response.setLabel("patient created");
    	response.setResult("patient created");
        exchange.getOut().setBody(response);
    }
}
