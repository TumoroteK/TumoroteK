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

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.patient.LienFamilialDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientLienDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientLienManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
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
public class PatientLienManagerTest extends AbstractManagerTest4
{

	@Autowired
	private PatientLienManager patientLienManager;
	@Autowired
	private PatientLienDao patientLienDao;
	@Autowired
	private LienFamilialDao lienFamilialDao;
	@Autowired
	private PatientDao patientDao;

	public PatientLienManagerTest(){}


	@Test
	public void testCRUD() throws ParseException{
		createObjectManagerTest();
		updateObjectManagerTest();
		removeObjectManagerTest();
	}

	private void createObjectManagerTest() throws ParseException{
		//Insertion nouvel enregistrement
		final PatientLien pl = new PatientLien();
		/*Champs obligatoires*/
		final Patient p1 = patientDao.findById(1).orElse(null);
		final Patient p2 = patientDao.findById(2).orElse(null);
		final LienFamilial l = lienFamilialDao.findById(1).orElse(null);
		//insertion
		patientLienManager.createOrUpdateObjectManager(pl, p1, p2, l, "creation");
		assertNotNull(patientLienDao.findById(new PatientLienPK(p1, p2))).orElse(null);

		//Insertion d'un doublon engendrant une exception
		PatientLien pl2 = new PatientLien();
		Boolean catched = false;
		try{
			patientLienManager.createOrUpdateObjectManager(pl2, p2, p1, l, "creation");
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("DoublonFoundException")){
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(IterableUtils.toList(patientLienDao.findAll()).size() == 3);

		//validation test Type
		final Patient[] patients = new Patient[] {null, p1, p2};
		final LienFamilial[] liens = new LienFamilial[] {null, l};

		int i = 0, j = 0;
		final int k = 0;

		pl2 = new PatientLien();
		for(i = 0; i < patients.length; i++){
			validationTest(pl2, patients[i], patients[j], liens[k], false);
		}
		i--;
		for(j = 0; j < patients.length; j++){
			validationTest(pl2, patients[i], patients[j], liens[k], false);
		}
		j--;
		validationTest(pl2, patients[i], patients[j], liens[k], false);

		// teste operation mal renseigne
		try{
			patientLienManager.createOrUpdateObjectManager(pl2, null, null, null, null);
		}catch(final NullPointerException ne){
			assertTrue(true);
		}
		try{
			patientLienManager.createOrUpdateObjectManager(pl2, null, null, l, "dummy");
		}catch(final IllegalArgumentException ie){
			assertTrue(true);
		}
		assertTrue(IterableUtils.toList(patientLienDao.findAll()).size() == 3);
	}

	private void validationTest(final PatientLien pl, final Patient p1, final Patient p2, final LienFamilial lien,
			final boolean isValide){
		try{
			if(!isValide){ //car creation valide
				patientLienManager.createOrUpdateObjectManager(pl, p1, p2, lien, "creation");
			}
		}catch(final Exception e){
			assertTrue(e.getClass().getSimpleName().equals("RequiredObjectIsNullException"));
		}
	}

	private void updateObjectManagerTest() throws ParseException{
		final PatientLienPK pk = new PatientLienPK();
		pk.setPatient1(patientDao.findById(1)).orElse(null);
		pk.setPatient2(patientDao.findById(2)).orElse(null);
		//Modification d'un enregistrement
		final PatientLien pl = patientLienDao.findById(pk).orElse(null);
		pl.setLienFamilial(lienFamilialDao.findById(2)).orElse(null);
		patientLienManager.createOrUpdateObjectManager(pl, null, null, null, "modification");
		assertTrue(patientLienDao.findById(pk).getLienFamilial().equals(lienFamilialDao.findById(2))).orElse(null);
		// Modification d'un membre de la clef = cree un nouvel object
		//Patient p3 = patientDao.findById(3).orElse(null);
		//		patientLienManager.createOrUpdateObjectManager(pl, null, p3, 
		//														null, "modification");
		//		pk.setPatient2(p3);
		//		assertNotNull(patientLienDao.findById(pk)).orElse(null);

		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		try{
			patientLienManager.createOrUpdateObjectManager(pl, patientDao.findById(2), patientDao.findById(5).orElse(null),
					lienFamilialDao.findById(3), "modification").orElse(null);
		}catch(final DoublonFoundException e){
			catched = true;
		}
		assertTrue(catched);
		assertNotNull(patientLienDao.findById(pk)).orElse(null);

		//test validation inutile car teste dans le create
	}

	private void removeObjectManagerTest(){
		final PatientLienPK pk = new PatientLienPK(patientDao.findById(1), patientDao.findById(2)).orElse(null);
		//Suppression de l'enregistrement precedemment insere
		final PatientLien pl1 = patientLienDao.findById(pk).orElse(null);
		patientLienManager.removeObjectManager(pl1);
		assertNull(patientLienDao.findById(pk)).orElse(null);

		patientLienManager.removeObjectManager(null);
		assertTrue(IterableUtils.toList(patientLienDao.findAll()).size() == 2);
	}
}