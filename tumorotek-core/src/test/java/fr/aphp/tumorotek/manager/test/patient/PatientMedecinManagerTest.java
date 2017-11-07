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
package fr.aphp.tumorotek.manager.test.patient;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientMedecinDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientMedecinManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecinPK;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

import java.text.ParseException;

/**
 * 
 * Classe de test pour le manager PatientMedecinManagerTest.
 * Classe créée le 02/11/09.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class PatientMedecinManagerTest extends AbstractManagerTest {
	
	/* Managers injectes par Spring*/
	private PatientMedecinManager patientMedecinManager;
	private PatientMedecinDao patientmedecinDao;
	private CollaborateurDao collaborateurDao;
	private PatientDao patientDao;

	public PatientMedecinManagerTest() {		
	}
	
	public void setPatientMedecinManager(PatientMedecinManager pmManager) {
		this.patientMedecinManager = pmManager;
	}

	public void setPatientDao(PatientDao pDao) {
		this.patientDao = pDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}
	
	public void setPatientMedecinDao(PatientMedecinDao plDao) {
		this.patientmedecinDao = plDao;
	}
	
	/**
	 * Teste les methodes CRUD. 
	 * @throws ParseException 
	 */
	public void testCRUD() throws ParseException {
		createObjectManagerTest();
		updateObjectManagerTest();
		removeObjectManagerTest();
	}
	
	/**
	 * Teste la methode createObjectManager. 
	 * @throws ParseException 
	 */
	public void createObjectManagerTest() throws ParseException {
		//Insertion nouvel enregistrement
		PatientMedecin pm = new PatientMedecin();
		/*Champs obligatoires*/
		pm.setOrdre(1);
		Patient p1 = patientDao.findById(2);
		Collaborateur c = collaborateurDao.findById(1);
		//insertion
		patientMedecinManager.createOrUpdateObjectManager(pm, p1, c, 
																"creation");
		assertNotNull(patientmedecinDao.findById(new PatientMedecinPK(c, p1)));

		//Insertion d'un doublon engendrant une exception
		PatientMedecin pm2 = new PatientMedecin();
		Boolean catched = false;
		try {
			pm2.setOrdre(6);
			patientMedecinManager.createOrUpdateObjectManager(pm2, p1, c, 
																 "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertEquals(4, patientmedecinDao.findAll().size());
		
		//validation test Type
		Patient[] patients = new Patient[]{null, patientDao.findById(3)};
		Collaborateur[] colls = new Collaborateur[]{null, c};
		Integer[] ordres = new Integer[]{null, -1, 0, 3};
		
		int i = 0, j = 0, k = 0;

		pm2 = new PatientMedecin();
		for (i = 0; i < patients.length; i++) {
			validationTest(pm2, patients[i], colls[j], ordres[k], 
																false, i, j, k);
		}
		i--;
		for (j = 0; j < patients.length; j++) {
			validationTest(pm2, patients[i], colls[j], ordres[k], 
																false, i, j, k);
		}
		j--;
		for (k = 0; k < ordres.length; k++) {
			validationTest(pm2, patients[i], colls[j], ordres[k], 
															(k == 3), i, j, k);
		}
		
		pm2.setOrdre(2);
		// teste operation mal renseigne
		try {
			patientMedecinManager.createOrUpdateObjectManager(pm2, null, null, 
															null);
		} catch (NullPointerException ne) {
			assertTrue(true);
		}
		try {
			patientMedecinManager.createOrUpdateObjectManager(pm2, null, null, 
															 "dummy");
		} catch (IllegalArgumentException ie) {
			assertTrue(true);
		}
		assertTrue(patientmedecinDao.findAll().size() == 4);
	}
		
	private void validationTest(PatientMedecin pm, Patient p1, Collaborateur
			c, Integer ordre, boolean isValide, int i, int j, int k) {
		try {
			if (!isValide) { //car creation valide
				pm.setOrdre(ordre);
				patientMedecinManager.createOrUpdateObjectManager(pm, p1,
										c, "creation");
			}
		} catch (Exception e) {
			if (i == 0 || j == 0) {
				assertTrue(e.getClass().getSimpleName().equals(
											"RequiredObjectIsNullException"));
			} else {
				assertTrue(e.getClass().getSimpleName().equals(
														"ValidationException"));
			}
		}
	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	public void updateObjectManagerTest() throws ParseException {
		PatientMedecinPK pk = new PatientMedecinPK();
		pk.setPatient(patientDao.findById(2));
		pk.setCollaborateur(collaborateurDao.findById(1));		
		//Modification d'un enregistrement
		PatientMedecin pm = patientmedecinDao.findById(pk);
		pm.setOrdre(3);
		patientMedecinManager.createOrUpdateObjectManager(pm, null, null, 
													 		"modification");
		assertTrue(patientmedecinDao.findById(pk).getOrdre().equals(3));
		// Modification d'un membre de la clef = cree un nouvel object

		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		pm.setOrdre(2);
		try {
			patientMedecinManager.createOrUpdateObjectManager(pm, 
							patientDao.findById(1), 
								collaborateurDao.findById(2), "modification");
		} catch (DoublonFoundException e) {
			catched = true;
		}
		assertTrue(catched);
		assertNotNull(patientmedecinDao.findById(pk));
		
		//test validation inutile car teste dans le create
	}
	
	/**
	 * Teste la methode removeObjectManager. 
	 */
	public void removeObjectManagerTest() {
		PatientMedecinPK pk = new PatientMedecinPK(collaborateurDao.findById(1),
														patientDao.findById(2));
		//Suppression de l'enregistrement precedemment insere
		PatientMedecin pm1 = patientmedecinDao.findById(pk);
		patientMedecinManager.removeObjectManager(pm1);
		assertNull(patientmedecinDao.findById(pk));
		
		patientMedecinManager.removeObjectManager(null);
		assertTrue(patientmedecinDao.findAll().size() == 3);
	}
}
