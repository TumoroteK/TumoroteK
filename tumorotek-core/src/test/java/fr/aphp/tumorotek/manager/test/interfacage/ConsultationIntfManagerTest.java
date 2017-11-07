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
package fr.aphp.tumorotek.manager.test.interfacage;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.interfacage.ConsultationIntfManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.ConsultationIntf;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Classe de test pour le manager ConsultationIntf.
 * Classe créée le 26/02/2016.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0.13.1
 *
 */
public class ConsultationIntfManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private ConsultationIntfManager consultationIntfManager;
	
	@Autowired
	private UtilisateurDao utilisateurDao;
	
	@Test
	public void testFindByUtilisateurOrEmetteurInDatesManager() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Utilisateur u1 = utilisateurDao.findById(1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(sdf.parse("20/04/2015 12:12:00"));
		Calendar c2 = Calendar.getInstance();
		c2.setTime(sdf.parse("30/04/2015 16:55:24"));
		List<ConsultationIntf> liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(u1, "APIX", c1, c2);
		assertTrue(liste.size() == 2);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(u1, "%", c1, c2);
		assertTrue(liste.size() == 3);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(u1, null, c1, c2);
		assertTrue(liste.size() == 3);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(utilisateurDao.findById(2), 
						null, c1, c2);
		assertTrue(liste.size() == 0);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, "APIX", c1, c2);
		assertTrue(liste.size() == 2);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, "APIX%", c1, c2);
		assertTrue(liste.size() == 3);

		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, c1, c2);
		assertTrue(liste.size() == 4);
		
		c2.setTime(sdf.parse("30/04/2015 16:50:00"));
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, c1, c2);
		assertTrue(liste.size() == 3);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, c1, null);
		assertTrue(liste.size() == 4);
		
		c1.setTime(sdf.parse("25/04/2015 11:20:00"));
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, c1, null);
		assertTrue(liste.size() == 2);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, null, null);
		assertTrue(liste.size() == 4);
		
		liste = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(u1, null, c1, null);
		assertTrue(liste.size() == 1);
	}
	
	@Test
	@Rollback(false)
	public void testCreateConsultationManager() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Utilisateur u1 = utilisateurDao.findById(1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(sdf.parse("20/02/2016 12:12:12"));
		
		int totSize = consultationIntfManager
			.findByUtilisateurOrEmetteurInDatesManager(null, null, null, null).size();
		
		consultationIntfManager.createObjectManager("NEW_DOS_ID", c1, "GLIMS v88", u1);
		
		// Test de l'insertion
		assertTrue(consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, null, null)
				.size() == totSize + 1);
		
		ConsultationIntf consult = consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(u1, "GLIMS v88", c1, c1)
				.get(0);
		
		assertTrue(consult.getIdentification().equals("NEW_DOS_ID"));
		
		// deletion uniquement pour clean tests
		consultationIntfManager.removeObjectManager(consult);
		
		assertTrue(consultationIntfManager
				.findByUtilisateurOrEmetteurInDatesManager(null, null, null, null)
				.size() == totSize);
	}
}