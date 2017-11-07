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
package fr.aphp.tumorotek.manager.test.contexte;

import static org.junit.Assert.*;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurPlateformeDao;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurPlateformeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateformePK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
/**
 * 
 * Classe de test pour le manager PlateformeManager.
 * Classe créée le 01/10/09.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class PlateformeManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private PlateformeManager plateformeManager;
	@Autowired
	private CollaborateurDao collaborateurDao;
	@Autowired
	private UtilisateurManager utilisateurManager;
	@Autowired
	private OperationManager operationManager;
	@Autowired
	private ConteneurManager conteneurManager;
	@Autowired
	private ConteneurPlateformeDao conteneurPlateformeDao;
	@Autowired
	private ConteneurPlateformeManager conteneurPlateformeManager;
	
	public PlateformeManagerTest() {
	}


	@Test
	public void testFindById() {
		Plateforme pf = plateformeManager.findByIdManager(1);
		assertNotNull(pf);
		assertTrue(pf.getNom().equals("PLATEFORME 1"));
		
		Plateforme pfNull = plateformeManager.findByIdManager(3);
		assertNull(pfNull);
	}

	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll() {
		List<Plateforme> list = plateformeManager.findAllObjectsManager();
		assertTrue(list.size() == 2);
	}

	/**
	 * Test la méthode getPlateformesManager.
	 */
	@Test
	public void testGetBanquesManager() {
		Plateforme pf1 = plateformeManager.findByIdManager(1);
		assertNotNull(pf1);
		Set<Banque> banques = plateformeManager.getBanquesManager(pf1);
		assertTrue(banques.size() == 3);
		
		Plateforme pf2 = plateformeManager.findByIdManager(2);
		assertNotNull(pf2);
		banques = plateformeManager.getBanquesManager(pf2);
		assertTrue(banques.size() == 1);
	}
	
	/**
	 * Test la méthode getUtilisateursManager.
	 */
	@Test
	public void testGetUtilisateursManager() {
		Plateforme pf1 = plateformeManager.findByIdManager(1);
		assertNotNull(pf1);
		Set<Utilisateur> liste = plateformeManager
			.getUtilisateursManager(pf1);
		assertTrue(liste.size() == 1);
		
		Plateforme pf2 = plateformeManager.findByIdManager(2);
		assertNotNull(pf2);
		liste = plateformeManager.getUtilisateursManager(pf2);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test la méthode findDoublon.
	 */
	@Test
	public void testFindDoublon() {
		
		String nom1 = "PLATEFORME 1";
		String nom2 = "PF";
		
		Plateforme pf1 = new Plateforme();
		pf1.setNom(nom1);
		assertTrue(plateformeManager.findDoublonManager(pf1));
		
		pf1.setNom(nom2);
		assertFalse(plateformeManager.findDoublonManager(pf1));
		
		Plateforme pf2 = plateformeManager.findByIdManager(2);
		assertFalse(plateformeManager.findDoublonManager(pf2));
		
		pf2.setNom(nom1);
		assertTrue(plateformeManager.findDoublonManager(pf2));
		
		assertFalse(plateformeManager.findDoublonManager(null));
	}
	
	@Test
	public void testUpdateObjectManager() {
		Plateforme pf = plateformeManager.findByIdManager(2);
		List<Utilisateur> users = new ArrayList<Utilisateur>();
		Utilisateur admin = utilisateurManager.findByIdManager(5);
		Conteneur ct1 = conteneurManager.findByIdManager(1);
		Conteneur ct2 = conteneurManager.findByIdManager(2);
		Conteneur ct3 = conteneurManager.findByIdManager(3);
		Conteneur ct4 = conteneurManager.findByIdManager(4);
		List<Conteneur> conteneurs = new ArrayList<Conteneur>();
		conteneurs.add(ct2);
		conteneurs.add(ct3);
		
		boolean catched = false;
		// on test l'update d'un doublon
		pf.setNom("PLATEFORME 1");
		try {
			plateformeManager.updateObjectManager(pf, null, null, null, admin);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
		
		// Test de la validation
		pf.setNom("PLATEFORME 2");
		try {
			validationUpdate(pf);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
		
		// update avec les assos à null
		pf.setNom("TEST");
		pf.setAlias("AL");
		plateformeManager.updateObjectManager(pf, null, null, null, admin);
		assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
		assertTrue(operationManager.findAllObjectsManager().size() == 20);
		
		Plateforme pf2 = plateformeManager.findByIdManager(2);
		assertTrue(pf2.getNom().equals("TEST"));
		assertTrue(pf2.getAlias().equals("AL"));
		assertNull(pf2.getCollaborateur());
		assertTrue(plateformeManager.getUtilisateursManager(pf2).size() == 0);
		
		// update avec un collaborateur et 2 utilisateurs
		Utilisateur u1 = utilisateurManager.findByIdManager(1);
		Utilisateur u2 = utilisateurManager.findByIdManager(2);
		users.add(u1);
		users.add(u2);
		Collaborateur c1 = collaborateurDao.findById(1);
		plateformeManager.updateObjectManager(pf2, c1, users, 
				conteneurs, admin);
		assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
		assertTrue(operationManager.findAllObjectsManager().size() == 21);
		
		Plateforme pf3 = plateformeManager.findByIdManager(2);
		assertTrue(pf3.getNom().equals("TEST"));
		assertTrue(pf3.getAlias().equals("AL"));
		assertTrue(pf3.getCollaborateur().equals(c1));
		assertTrue(plateformeManager.getUtilisateursManager(pf3).size() == 2);
		assertTrue(conteneurManager.findByPlateformeOrigWithOrderManager(
				pf3).size() == 1);
		assertTrue(conteneurManager.findByPartageManager(pf3, true).size() == 2);
		assertTrue(conteneurManager.findByPartageManager(pf3, false).size() == 2);
		assertTrue(conteneurManager.findByPartageManager(pf3, true).contains(ct2));
		assertTrue(conteneurManager.findByPartageManager(pf3, true).contains(ct3));
		ct4.setPlateformeOrig(pf3);
		assertTrue(conteneurManager.findByPartageManager(pf3, false).contains(ct1));
		assertTrue(conteneurManager.findByPartageManager(pf3, false).contains(ct4));
		
		// ajout d'un utilisateur et suppression d'un autre
		Utilisateur u3 = utilisateurManager.findByIdManager(3);
		users = new ArrayList<Utilisateur>();
		users.add(u1);
		users.add(u3);
		conteneurs = new ArrayList<Conteneur>();
		conteneurs.add(ct2);
		plateformeManager.updateObjectManager(pf3, c1, users, conteneurs,
				admin);
		assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
		assertTrue(operationManager.findAllObjectsManager().size() == 22);
		assertTrue(conteneurManager.findByPlateformeOrigWithOrderManager(
				pf3).size() == 1);
		assertTrue(conteneurManager.findByPlateformeOrigWithOrderManager(
				pf3).contains(conteneurManager.findByIdManager(4)));
		assertTrue(conteneurManager.findByPartageManager(pf3, true).size() == 1);
		assertTrue(conteneurManager.findByPartageManager(pf3, false).size() == 3);
		assertTrue(conteneurManager.findByPartageManager(pf3, true).contains(ct2));
		assertTrue(conteneurManager.findByPartageManager(pf3, false).contains(ct1));
		assertTrue(conteneurManager.findByPartageManager(pf3, false).contains(ct3));
		assertTrue(conteneurManager.findByPartageManager(pf3, false).contains(ct4));
		
		Plateforme pf4 = plateformeManager.findByIdManager(2);
		assertTrue(plateformeManager.getUtilisateursManager(pf4).size() == 2);
		assertTrue(plateformeManager.getUtilisateursManager(pf4).contains(u3));
		assertFalse(plateformeManager.getUtilisateursManager(pf4).contains(u2));
		
		// remise à zéro de la plateforme
		pf4.setNom("PLATEFORME 2");
		pf4.setAlias("PF2");
		users = new ArrayList<Utilisateur>();
		conteneurs = new ArrayList<Conteneur>();
		conteneurs.add(ct1);
		Collaborateur c4 = collaborateurDao.findById(4);
		plateformeManager.updateObjectManager(pf4, c4, users, conteneurs,
				admin);
		assertTrue(plateformeManager.findAllObjectsManager().size() == 2);
		assertTrue(operationManager.findAllObjectsManager().size() == 23);
		
		Plateforme pf5 = plateformeManager.findByIdManager(2);
		assertTrue(pf5.getNom().equals("PLATEFORME 2"));
		assertTrue(pf5.getAlias().equals("PF2"));
		assertTrue(pf5.getCollaborateur().equals(c4));
		assertTrue(plateformeManager.getUtilisateursManager(pf5).size() == 0);
		assertTrue(conteneurManager.findByPlateformeOrigWithOrderManager(
				pf5).size() == 1);
		assertTrue(conteneurManager.findByPlateformeOrigWithOrderManager(
				pf5).contains(conteneurManager.findByIdManager(4)));
		assertTrue(conteneurManager.findByPartageManager(pf5, true).size() == 1);
		assertTrue(conteneurManager.findByPartageManager(pf5, true).contains(ct1));
		assertTrue(conteneurManager.findByPartageManager(pf5, false).size() == 3);
		assertTrue(conteneurManager.findByPartageManager(pf5, false).contains(ct2));
		assertFalse(conteneurManager.findByPartageManager(pf5, false).contains(ct1));
		assertTrue(conteneurManager.findByPartageManager(pf5, false).contains(ct3));
		ct4 = conteneurManager.findByIdManager(4);
		assertTrue(conteneurManager.findByPartageManager(pf5, false).contains(ct4));
		
		// clean up 
		conteneurPlateformeManager.removeObjectManager(conteneurPlateformeDao
				.findById(new ConteneurPlateformePK(ct2, pf5)));
		conteneurPlateformeManager.removeObjectManager(conteneurPlateformeDao
				.findById(new ConteneurPlateformePK(ct3, pf5)));
		assertTrue(conteneurPlateformeDao.findAll().size() == 5);
		
		// suppression des opérations créées pendant le test
		List<Operation> ops = operationManager.findByObjectManager(pf5);
		for (int i = 0; i < ops.size(); i++) {
			operationManager.removeObjectManager(ops.get(i));
		}
		assertTrue(operationManager.findAllObjectsManager().size() == 19);
	}
	
	/**
	 * Test la validation d'une plateforme lors de son update.
	 * @param plateforme à tester.
	 * @throws ParseException 
	 */
	private void validationUpdate(Plateforme pf) 
		throws ParseException {
		
		boolean catched = false;
		Utilisateur admin = utilisateurManager.findByIdManager(5);
		// On teste une insertion avec un attribut nom non valide
		String[] emptyValues = new String[]{null, "", "  ", "%$$*gd¤", 
				createOverLength(50)};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				pf.setNom(emptyValues[i]);
				plateformeManager.updateObjectManager(
						pf, null, null, null, admin);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		pf.setNom("PLATEFORME 2");
				
		// On teste un update avec un attribut alias non valide
		emptyValues = new String[]{"", "  ", "%$$*d", 
				createOverLength(5)};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				pf.setAlias(emptyValues[i]);
				plateformeManager.updateObjectManager(
						pf, null, null, null, admin);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		pf.setAlias("PF2");
	}
}
