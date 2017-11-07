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
package fr.aphp.tumorotek.manager.impl.interfacage;

import java.util.Calendar;
import java.util.List;

import fr.aphp.tumorotek.dao.interfacage.ConsultationIntfDao;
import fr.aphp.tumorotek.manager.interfacage.ConsultationIntfManager;
import fr.aphp.tumorotek.model.interfacage.ConsultationIntf;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Implémentation du manager du bean de domaine CONSULTATION_INTF.
 * Classe créée le 25/02/2016.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.13.1
 *
 */
public class ConsultationIntfManagerImpl implements ConsultationIntfManager {
	
	private ConsultationIntfDao consultationIntfDao;

	public void setConsultationIntfDao(ConsultationIntfDao c) {
		this.consultationIntfDao = c;
	}

	@Override
	public List<ConsultationIntf> findByUtilisateurOrEmetteurInDatesManager(
			Utilisateur u, String e, Calendar date1, Calendar date2) {
		
		// si emetteur empty -> recherche tous les emetteurs
		if (e == null) {
			e = "%";
		}
		
		// si pas de limite basse pour la date -> date factice
		if (date1 == null) {
			date1 = Calendar.getInstance();
			date1.setTimeInMillis(0);
		}
		
		// si pas de limite haute pour la date -> date current
		if (date2 == null) {
			date2 = Calendar.getInstance();
		}
		
		// si pas utilisateur -> recherche uniquement par emetteur
		if (u == null) {
			return consultationIntfDao.findByEmetteurInDates(e, date1, date2);
		} else {
			return consultationIntfDao.findByUtilisateurEmetteurInDates(u, e, date1, date2);
		}
	}

	@Override
	public void createObjectManager(String _i, Calendar _c, String _e,
			Utilisateur u) {

		ConsultationIntf consult = new ConsultationIntf();
		consult.setIdentification(_i);
		consult.setUtilisateur(u);
		consult.setEmetteurIdent(_e);
		consult.setDate(_c);
		
		consultationIntfDao.createObject(consult);
	}
	
	@Override
	public void removeObjectManager(ConsultationIntf c) {
		if (c!= null) {
			consultationIntfDao.removeObject(c.getConsultationIntfId());
		}
	}
	
}
