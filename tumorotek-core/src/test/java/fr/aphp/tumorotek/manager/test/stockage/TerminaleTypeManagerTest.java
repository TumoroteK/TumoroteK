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
package fr.aphp.tumorotek.manager.test.stockage;

import static org.junit.Assert.*;


import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.stockage.TerminaleTypeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
/**
 * 
 * Classe de test pour le manager TerminaleTypeManager.
 * Classe créée le 17/03/10.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class TerminaleTypeManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private TerminaleTypeManager terminaleTypeManager;
	
	public TerminaleTypeManagerTest() {		
	}

	/**
	 * Test la méthode findById.
	 */
	@Test
	public void testFindById() {
		TerminaleType tt1 = terminaleTypeManager.findByIdManager(1);
		assertNotNull(tt1);
		assertTrue(tt1.getType().equals("RECTANGULAIRE_100"));
		
		TerminaleType ttNull = terminaleTypeManager.findByIdManager(100);
		assertNull(ttNull);
	}
	
	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll() {
		List<TerminaleType> list = terminaleTypeManager.findAllObjectsManager();
		assertTrue(list.size() == 6);
		assertTrue(list.get(0).getType().equals("RECTANGULAIRE_100"));
	}
	
	@Test
	public void testFindByTypeManager() {
		List<TerminaleType> list = terminaleTypeManager.findByTypeManager(
				"RECTANGULAIRE_100");
		assertTrue(list.size() == 1);
		
		list = terminaleTypeManager.findByTypeManager("RECTANGULAIRE");
		assertTrue(list.size() == 0);
		
		list = terminaleTypeManager.findByTypeManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Teste la méthode findDoublon.
	 */
	@Test
	public void testFindDoublon() {
		//Cree le doublon
		TerminaleType t1 = terminaleTypeManager.findByIdManager(1);
		assertFalse(terminaleTypeManager.findDoublonManager(t1));
		t1.setType("RECTANGULAIRE_169");
		assertTrue(terminaleTypeManager.findDoublonManager(t1));
		TerminaleType t2 = new TerminaleType();
		t2.setType(t1.getType());
		assertTrue(t2.equals(t1));
		assertTrue(terminaleTypeManager.findDoublonManager(t2));
		t2.setType("ROOO");
		assertFalse(terminaleTypeManager.findDoublonManager(t2));
	}
	
	/**
	 * Teste la méthode isUsedObject.
	 */
	@Test
	public void testIsUsedObjectManager() {
		//Enregistrement est reference
		TerminaleType t1 = terminaleTypeManager.findByIdManager(1);
		assertTrue(terminaleTypeManager.isUsedObjectManager(t1));
		//Enregistrement n'est pas reference
		TerminaleType t2 = terminaleTypeManager.findByIdManager(2);
		assertFalse(terminaleTypeManager.isUsedObjectManager(t2));
	}
	
	/**
	 * Teste les methodes CRUD. 
	 */
	@Test
	public void testCRUD() {
		createObjectManagerTest();
		updateObjectManagerTest();
		removeObjectManagerTest();
	}
	
	private void createObjectManagerTest() {
		//Insertion nouvel enregistrement
		TerminaleType t1 = new TerminaleType();
		t1.setType("TEST");
		t1.setNbPlaces(10);
		t1.setLongueur(10);
		t1.setHauteur(1);
		t1.setDepartNumHaut(true);
		terminaleTypeManager.createObjectManager(t1);
		assertTrue(terminaleTypeManager.findAllObjectsManager().size() == 7);
		//Insertion d'un doublon engendrant une exception
		Boolean catched = false;
		TerminaleType t1Bis = new TerminaleType();
		t1Bis.setType("TEST");
		t1Bis.setNbPlaces(10);
		t1Bis.setLongueur(10);
		t1Bis.setHauteur(1);
		t1Bis.setDepartNumHaut(true);
		try {
			terminaleTypeManager.createObjectManager(t1Bis);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(terminaleTypeManager.findAllObjectsManager().size() == 7);
		
		try {
			validationInsert(t1Bis);
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		assertTrue(terminaleTypeManager.findAllObjectsManager().size() == 7);
	}
	
	private void updateObjectManagerTest() {
		//Modification d'un enregistrement
		TerminaleType t1 = terminaleTypeManager.findByIdManager(7);
		t1.setType("TEST BIS");
		terminaleTypeManager.updateObjectManager(t1);
		TerminaleType t1Bis = terminaleTypeManager.findByIdManager(7);
		assertTrue(t1Bis.getType().equals("TEST BIS"));
		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		try {
			t1Bis.setType("RECTANGULAIRE_100");
			terminaleTypeManager.updateObjectManager(t1Bis);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		
		//Validation test
		try {
			validationUpdate(t1Bis);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void removeObjectManagerTest() {
		//Suppression de l'enregistrement precedemment insere
		TerminaleType t1 = terminaleTypeManager.findByIdManager(7);
		terminaleTypeManager.removeObjectManager(t1);
		assertTrue(terminaleTypeManager.findAllObjectsManager().size() == 6);
		//Suppression engrendrant une exception
		Boolean catched = false;
		try {
			TerminaleType t2 = terminaleTypeManager.findByIdManager(1);
			terminaleTypeManager.removeObjectManager(t2);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ObjectUsedException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(terminaleTypeManager.findAllObjectsManager().size() == 6);
		//null remove
		terminaleTypeManager.removeObjectManager(null);
		assertTrue(terminaleTypeManager.findAllObjectsManager().size() == 6);
	}
	
	/**
	 * Test la validation d'un type de terminale lors de sa création.
	 * @param type TerminaleType à tester.
	 * @throws ParseException 
	 */
	private void validationInsert(TerminaleType type) 
		throws ParseException {
		
		boolean catchedInsert = false;
		
		// On teste une insertion avec un attribut nom non valide
		String[] emptyValues = new String[]{null, "", "  ", "%¤¤$$*gd", 
				createOverLength(25)};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				type.setType(emptyValues[i]);
				terminaleTypeManager.createObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setType("TEST2");
		
		// On teste une insertion avec un attribut nbPlaces non valide
		Integer[] nbPlacesValues = new Integer[]{null, -1, 0};
		for (int i = 0; i < nbPlacesValues.length; i++) {
			catchedInsert = false;
			try {
				type.setNbPlaces(nbPlacesValues[i]);
				terminaleTypeManager.createObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setNbPlaces(6);
		
		// On teste une insertion avec un attribut hauteur non valide
		Integer[] hauteurValues = new Integer[]{null, -1};
		for (int i = 0; i < hauteurValues.length; i++) {
			catchedInsert = false;
			try {
				type.setHauteur(hauteurValues[i]);
				terminaleTypeManager.createObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setHauteur(6);
		
		// On teste une insertion avec un attribut longueur non valide
		for (int i = 0; i < hauteurValues.length; i++) {
			catchedInsert = false;
			try {
				type.setLongueur(hauteurValues[i]);
				terminaleTypeManager.createObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setLongueur(6);
		
		catchedInsert = false;
		try {
			type.setDepartNumHaut(null);
			terminaleTypeManager.createObjectManager(type);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catchedInsert = true;
			}
		}
		assertTrue(catchedInsert);
		type.setDepartNumHaut(true);
	}
	
	/**
	 * Test la validation d'un type de terminale lors de son update.
	 * @param type TerminaleType à tester.
	 * @throws ParseException 
	 */
	private void validationUpdate(TerminaleType type) 
		throws ParseException {
		
		boolean catchedInsert = false;
		
		// On teste une insertion avec un attribut nom non valide
		String[] emptyValues = new String[]{null, "", "  ", "%$$¤¤*gd", 
				createOverLength(25)};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				type.setType(emptyValues[i]);
				terminaleTypeManager.updateObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setType("TEST2");
		
		// On teste une insertion avec un attribut nbPlaces non valide
		Integer[] nbPlacesValues = new Integer[]{null, -1, 0};
		for (int i = 0; i < nbPlacesValues.length; i++) {
			catchedInsert = false;
			try {
				type.setNbPlaces(nbPlacesValues[i]);
				terminaleTypeManager.updateObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setNbPlaces(6);
		
		// On teste une insertion avec un attribut hauteur non valide
		Integer[] hauteurValues = new Integer[]{null, -1};
		for (int i = 0; i < hauteurValues.length; i++) {
			catchedInsert = false;
			try {
				type.setHauteur(hauteurValues[i]);
				terminaleTypeManager.updateObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setHauteur(6);
		
		// On teste une insertion avec un attribut longueur non valide
		for (int i = 0; i < hauteurValues.length; i++) {
			catchedInsert = false;
			try {
				type.setLongueur(hauteurValues[i]);
				terminaleTypeManager.updateObjectManager(type);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		type.setLongueur(6);
		
		catchedInsert = false;
		try {
			type.setDepartNumHaut(null);
			terminaleTypeManager.updateObjectManager(type);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catchedInsert = true;
			}
		}
		assertTrue(catchedInsert);
		type.setDepartNumHaut(true);
	}

}
