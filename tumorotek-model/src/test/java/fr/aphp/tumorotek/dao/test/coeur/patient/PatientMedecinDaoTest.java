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
package fr.aphp.tumorotek.dao.test.coeur.patient;

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientMedecinDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecinPK;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 * 
 * Classe de test pour le DAO PatientMedecinDao et le 
 * bean du domaine PatientMedecin.
 * Classe de test créée le 28/10/09.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientMedecinDaoTest extends AbstractDaoTest {
	
	/** Beans Dao. */
	private PatientMedecinDao patientMedecinDao;
	private PatientDao patientDao;
	private CollaborateurDao collaborateurDao;
	
	/**
	 * Constructeur.
	 */
	public PatientMedecinDaoTest() {
	}
	
	public void setPatientMedecinDao(PatientMedecinDao mDao) {
		this.patientMedecinDao = mDao;
	}
	
	public void setPatientDao(PatientDao pDao) {
		this.patientDao = pDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}

	/**
	 * Test l'appel de la méthode toString().
	 */
	public void testToString() {
		PatientMedecinPK pk = new PatientMedecinPK(collaborateurDao.findById(1),
												patientDao.findById(1));
		PatientMedecin pm1 = patientMedecinDao.findById(pk);
		assertTrue(pm1.toString()
				.equals("{" + pm1.getPatient() + " - " 
											+ pm1.getCollaborateur() + "}"));
		pm1 = new PatientMedecin();
		assertTrue(pm1.toString().equals("{Empty PatientMedecin}"));
		pm1.setCollaborateur(collaborateurDao.findById(1));
		assertTrue(pm1.toString().equals("{Empty PatientMedecin}"));
		pm1.setCollaborateur(null);
		pm1.setPatient(patientDao.findById(1));
		assertTrue(pm1.toString().equals("{Empty PatientMedecin}"));
	}	
	
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllPatientMedecins() {
		List<PatientMedecin> patientMedecins = patientMedecinDao.findAll();
		assertTrue(patientMedecins.size() == 3);
	}
		
	/**
	 * Test l'insertion, la mise à jour et la suppression 
	 * d'un patientMedecin.
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 *
	 **/
	@Rollback(false)
	public void testCrudLienFamilial() {
		PatientMedecin pm = new PatientMedecin();
		Patient p1 = patientDao.findById(2);
		Collaborateur c1 = collaborateurDao.findById(4);
		pm.setPatient(p1);
		pm.setCollaborateur(c1);
		pm.setOrdre(1);
		// Test de l'insertion
		patientMedecinDao.createObject(pm);
		assertTrue(patientMedecinDao.findAll().size() == 4);	
		// Test de la mise à jour
		PatientMedecinPK pk = new PatientMedecinPK();
		pk.setPatient(p1);
		pk.setCollaborateur(c1);
		PatientMedecin pm2 = patientMedecinDao.findById(pk);
		assertNotNull(pm2);
		assertTrue(pm2.getPatient().equals(p1));
		assertTrue(pm2.getCollaborateur().equals(c1));
		assertTrue(pm2.getOrdre().equals(1));
		//update
		pm2.setOrdre(2);
		patientMedecinDao.updateObject(pm2);
		assertTrue(patientMedecinDao.findById(pk).equals(pm2));
		assertTrue(patientMedecinDao.findById(pk).getOrdre().equals(2));
		// Test de la délétion
		patientMedecinDao.removeObject(pk);
		assertNull(patientMedecinDao.findById(pk));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException {

		PatientMedecin pm1 = new PatientMedecin();
		PatientMedecin pm2 = new PatientMedecin();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(pm1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(pm1.equals(pm1));
		// 2 objets sont égaux entre eux
		assertTrue(pm1.equals(pm2));
		assertTrue(pm2.equals(pm1));
	
		populateClefsToTestEqualsAndHashCode(pm1, pm2);
			
		//dummy test
		Banque b = new Banque();
		assertFalse(pm1.equals(b));		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws ParseException 
	 */
	public void testHashCode() throws ParseException {
		PatientMedecin pm1 = new PatientMedecin();
		PatientMedecin pm2 = new PatientMedecin();
		PatientMedecin pm3 = new PatientMedecin();
		
		assertTrue(pm1.hashCode() == pm2.hashCode());
		assertTrue(pm2.hashCode() == pm3.hashCode());
		assertTrue(pm3.hashCode() > 0);
		
		//teste dans methode precedente
		//populateClefsToTestEqualsAndHashCode(pm1, pm2);
		
		int hash = pm1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(pm1.hashCode() == pm2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == pm1.hashCode());
		assertTrue(hash == pm1.hashCode());
		assertTrue(hash == pm1.hashCode());
		assertTrue(hash == pm1.hashCode());
	}
	
	private void populateClefsToTestEqualsAndHashCode(PatientMedecin pm1, 
								PatientMedecin pm2) throws ParseException {
		
		Patient p1 = patientDao.findById(1);
		Patient p2 = patientDao.findById(2);
		Patient p3 = patientDao.findById(1);
		Patient[] patients = new Patient[]{null, p1, p2, p3};
		Collaborateur c1 = collaborateurDao.findById(1);
		Collaborateur c2 = collaborateurDao.findById(2);
		Collaborateur c3 = collaborateurDao.findById(1);
		Collaborateur[] collabs = new Collaborateur[]{null, c1, c2, c3};
		
		PatientMedecinPK pk1 = new PatientMedecinPK();
		PatientMedecinPK pk2 = new PatientMedecinPK();
		
		for (int i = 0; i < patients.length; i++) {
			for (int j = 0; j < collabs.length; j++) {
				for (int k = 0; k < patients.length; k++) {
					for (int l = 0; l < collabs.length; l++) {
						pk1.setPatient(patients[i]);
						pk1.setCollaborateur(collabs[j]);
						pk2.setPatient(patients[k]);
						pk2.setCollaborateur(collabs[l]);					
						pm1.setPk(pk1);
						pm2.setPk(pk2);
						if (((i == k) || (i + k == 4)) 
										&& ((j == l) || (j + l == 4))) {
								assertTrue(pm1.equals(pm2));
								assertTrue(pm1.hashCode() 
										== pm2.hashCode());
						} else {
							assertFalse(pm1.equals(pm2));
						}
					}
				}
			}
		}
		
		//pk testing
		assertTrue(pk1.equals(pk1));
		pm2.setPk(pk1);
		assertTrue(pm1.equals(pm2));
		assertTrue(pm1.hashCode() == pm2.hashCode());
		assertFalse(pk1.equals(null));		
		pk1 = null;
		pm1.setPk(pk1);
		assertFalse(pm1.equals(pm2));
		assertFalse(pm1.hashCode() == pm2.hashCode());
		Banque b = new Banque();
		assertFalse(pk2.equals(b));	
	}

}
