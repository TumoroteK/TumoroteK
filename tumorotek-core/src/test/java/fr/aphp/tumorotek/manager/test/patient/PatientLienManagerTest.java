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

import java.text.ParseException;
import fr.aphp.tumorotek.dao.coeur.patient.LienFamilialDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientLienDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientLienManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.patient.PatientLienPK;

/**
 * 
 * Classe de test pour le manager PatientLienManagerTest.
 * Classe créée le 30/10/09.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class PatientLienManagerTest extends AbstractManagerTest {
	
	/* Managers injectes par Spring*/
	private PatientLienManager patientLienManager;
	private PatientLienDao patientLienDao;
	private LienFamilialDao lienFamilialDao;
	private PatientDao patientDao;

	public PatientLienManagerTest() {		
	}
	
	public void setPatientLienManagerTest(PatientLienManager plManager) {
		this.patientLienManager = plManager;
	}

	public void setPatientDao(PatientDao pDao) {
		this.patientDao = pDao;
	}


	public void setLienFamilialDao(LienFamilialDao lfDao) {
		this.lienFamilialDao = lfDao;
	}
	
	public void setPatientLienDao(PatientLienDao plDao) {
		this.patientLienDao = plDao;
	}

//	/**
//	 * Teste la méthode findDoublon.
//	 */
//	public void testFindDoublon() {
//		//Cree le doublon
//		PatientLienPK pk = new PatientLienPK();
//		pk.setPatient1(patientDao.findById(2));
//		pk.setPatient2(patientDao.findById(5));
//		PatientLien pl1 = patientLienDao.findById(pk);
//		PatientLien pl2 = new PatientLien();
//		pl2.setPk(pk);
//		assertTrue(pl2.equals(pl1));
//		assertTrue(patientLienManager.findDoublonManager(pl2));
//	}
	
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
		PatientLien pl = new PatientLien();
		/*Champs obligatoires*/
		Patient p1 = patientDao.findById(1);
		Patient p2 = patientDao.findById(2);
		LienFamilial l = lienFamilialDao.findById(1);
		//insertion
		patientLienManager.createOrUpdateObjectManager(pl, p1, p2, l, 
																"creation");
		assertNotNull(patientLienDao.findById(new PatientLienPK(p1, p2)));

		//Insertion d'un doublon engendrant une exception
		PatientLien pl2 = new PatientLien();
		Boolean catched = false;
		try {
			patientLienManager.createOrUpdateObjectManager(pl2, p2, p1, l, 
																 "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(patientLienDao.findAll().size() == 3);
		
		//validation test Type
		Patient[] patients = new Patient[]{null, p1, p2};
		LienFamilial[] liens = new LienFamilial[]{null, l};
		
		int i = 0, j = 0, k = 0;

		pl2 = new PatientLien();
		for (i = 0; i < patients.length; i++) {
			validationTest(pl2, patients[i], patients[j], liens[k], false);
		}
		i--;
		for (j = 0; j < patients.length; j++) {
			validationTest(pl2, patients[i], patients[j], liens[k], false);
		}
		j--;
		validationTest(pl2, patients[i], patients[j], liens[k], false);

	

		// teste operation mal renseigne
		try {
			patientLienManager.createOrUpdateObjectManager(pl2, null, null, 
																null, null);
		} catch (NullPointerException ne) {
			assertTrue(true);
		}
		try {
			patientLienManager.createOrUpdateObjectManager(pl2, null, null, 
																l, "dummy");
		} catch (IllegalArgumentException ie) {
			assertTrue(true);
		}
		assertTrue(patientLienDao.findAll().size() == 3);
	}
		
	private void validationTest(PatientLien pl, Patient p1, Patient
			p2, LienFamilial lien, boolean isValide) {
		try {
			if (!isValide) { //car creation valide
				patientLienManager.createOrUpdateObjectManager(pl, p1,
										p2, lien, "creation");
			}
		} catch (Exception e) {
			assertTrue(e.getClass().getSimpleName().equals(
											"RequiredObjectIsNullException"));
		}
	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	public void updateObjectManagerTest() throws ParseException {
		PatientLienPK pk = new PatientLienPK();
		pk.setPatient1(patientDao.findById(1));
		pk.setPatient2(patientDao.findById(2));		
		//Modification d'un enregistrement
		PatientLien pl = patientLienDao.findById(pk);
		pl.setLienFamilial(lienFamilialDao.findById(2));
		patientLienManager.createOrUpdateObjectManager(pl, null, null, 
													null, "modification");
		assertTrue(patientLienDao.findById(pk).getLienFamilial()
									.equals(lienFamilialDao.findById(2)));
		// Modification d'un membre de la clef = cree un nouvel object
		//Patient p3 = patientDao.findById(3);
//		patientLienManager.createOrUpdateObjectManager(pl, null, p3, 
//														null, "modification");
//		pk.setPatient2(p3);
//		assertNotNull(patientLienDao.findById(pk));
		
		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		try {
			patientLienManager.createOrUpdateObjectManager(pl, 
							patientDao.findById(2), patientDao.findById(5), 
								lienFamilialDao.findById(3), "modification");
		} catch (DoublonFoundException e) {
			catched = true;
		}
		assertTrue(catched);
		assertNotNull(patientLienDao.findById(pk));
		
		//test validation inutile car teste dans le create
	}
	
	/**
	 * Teste la methode removeObjectManager. 
	 */
	public void removeObjectManagerTest() {
		PatientLienPK pk = new PatientLienPK(patientDao.findById(1),
												patientDao.findById(2));
		//Suppression de l'enregistrement precedemment insere
		PatientLien pl1 = patientLienDao.findById(pk);
		patientLienManager.removeObjectManager(pl1);
		assertNull(patientLienDao.findById(pk));
		
		patientLienManager.removeObjectManager(null);
		assertTrue(patientLienDao.findAll().size() == 2);
	}
}
