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

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.stockage.ConteneurTypeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;

/**
 * 
 * Classe de test pour le manager ConteneurTypeManager.
 * Classe créée le 17/03/10.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ConteneurTypeManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private ConteneurTypeManager conteneurTypeManager;
	@Autowired
	private PlateformeDao plateformeDao;

	public ConteneurTypeManagerTest() {		
	}

	@Test
	public void testFindById() {
		ConteneurType ct1 = conteneurTypeManager.findByIdManager(1);
		assertNotNull(ct1);
		assertTrue(ct1.getType().equals("CONGELATEUR"));
		
		ConteneurType csNull = conteneurTypeManager.findByIdManager(10);
		assertNull(csNull);
	}
	
//	/**
//	 * Test la méthode findAllObjects.
//	 */
//	@Test
//	public void testFindAll() {
//		List<ConteneurType> list = conteneurTypeManager.findAllObjectsManager();
//		assertTrue(list.size() == 3);
//		assertTrue(list.get(1).getType().equals("CRYOCONSERVATEUR"));
//	}
	
	/**
	 * Teste la méthode findDoublon.
	 */
	@Test
	public void testFindDoublon() {
		//Cree le doublon
		ConteneurType c1 = conteneurTypeManager.findByIdManager(1);
		assertFalse(conteneurTypeManager.findDoublonManager(c1));
		c1.setType("CRYOCONSERVATEUR");
		assertTrue(conteneurTypeManager.findDoublonManager(c1));
		ConteneurType c2 = new ConteneurType();
		c2.setType(c1.getType());
		c2.setPlateforme(c1.getPlateforme());
		assertTrue(c2.equals(c1));
		assertTrue(conteneurTypeManager.findDoublonManager(c2));
		c2.setType("ROOO");
		assertFalse(conteneurTypeManager.findDoublonManager(c2));
	}
	
	/**
	 * Teste la méthode isUsedObject.
	 */
	@Test
	public void testIsUsedObjectManager() {
		//Enregistrement est reference
		ConteneurType c1 = conteneurTypeManager.findByIdManager(1);
		assertTrue(conteneurTypeManager.isUsedObjectManager(c1));
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
		ConteneurType c1 = new ConteneurType();
		c1.setType("NEW");
		c1.setPlateforme(plateformeDao.findById(1));
		conteneurTypeManager.createObjectManager(c1);
		assertTrue(conteneurTypeManager
			.findByOrderManager(c1.getPlateforme()).size() == 4);
		//Insertion d'un doublon engendrant une exception
		Boolean catched = false;
		ConteneurType c1Bis = new ConteneurType();
		c1Bis.setType("NEW");
		try {
			conteneurTypeManager.createObjectManager(c1Bis);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		c1Bis.setPlateforme(c1.getPlateforme());
		assertTrue(catched);
		catched = false;
		try {
			conteneurTypeManager.createObjectManager(c1Bis);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(conteneurTypeManager
				.findByOrderManager(c1.getPlateforme()).size() == 4);
		
		//Validation test
		String[] emptyValues = new String[]{"", "  ", "=¤¤12$$.K", 
											createOverLength(200), null};
		ConteneurType c2 = new ConteneurType();
		c2.setPlateforme(plateformeDao.findById(1));
		for (int i = 0; i < emptyValues.length; i++) {
			try {
				c2.setType(emptyValues[i]);
				conteneurTypeManager.createObjectManager(c2);
			} catch (ValidationException e) {
				//verifie qu'aucune ligne n'a ete ajoutee
				assertTrue(conteneurTypeManager
					.findByOrderManager(c1.getPlateforme()).size() == 4);
			}
		}		
	}
	
	private void updateObjectManagerTest() {
		//Modification d'un enregistrement
		ConteneurType c1 = conteneurTypeManager.findByIdManager(4);
		c1.setType("NEW BIS");
		conteneurTypeManager.updateObjectManager(c1);
		ConteneurType c1Bis = conteneurTypeManager.findByIdManager(4);
		assertTrue(c1Bis.getType().equals("NEW BIS"));
		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		try {
			c1Bis.setType("CONGELATEUR");
			conteneurTypeManager.updateObjectManager(c1Bis);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		
		//Validation test
		String[] emptyValues = new String[]{"", "  ", "plk$¤¤$_",
											createOverLength(200), null};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				c1Bis.setType(emptyValues[i]);
				conteneurTypeManager.updateObjectManager(c1Bis);
			} catch (ValidationException e) {
				//verifie que l'enregistrement n'a pas ete modifie
				catched = true;
			}
			assertTrue(catched);
		}
	}
	
	private void removeObjectManagerTest() {
		//Suppression de l'enregistrement precedemment insere
		ConteneurType c1 = conteneurTypeManager.findByIdManager(4);
		conteneurTypeManager.removeObjectManager(c1);
		assertTrue(conteneurTypeManager
				.findByOrderManager(c1.getPlateforme()).size() == 3);
		//Suppression engrendrant une exception
//		Boolean catched = false;
//		try {
//			ConteneurType c2 = conteneurTypeManager.findByIdManager(1);
//			conteneurTypeManager.removeObjectManager(c2);
//		} catch (Exception e) {
//			if (e.getClass().getSimpleName().equals(
//					"ObjectUsedException")) {
//				catched = true;
//			}
//		}
//		assertTrue(catched);
		//null remove
		conteneurTypeManager.removeObjectManager(null);
		assertTrue(conteneurTypeManager
				.findByOrderManager(c1.getPlateforme()).size() == 3);
	}
	
	@Test
	public void testFindByOrderManager() {
		Plateforme pf = plateformeDao.findById(1);
		List< ? extends TKThesaurusObject> list = 
			conteneurTypeManager.findByOrderManager(pf);
		assertTrue(list.size() == 3);
		assertTrue(list.get(1).getNom().equals("CRYOCONSERVATEUR"));
		pf = plateformeDao.findById(2);
		list = conteneurTypeManager.findByOrderManager(pf);
		assertTrue(list.size() == 0);
		list = conteneurTypeManager.findByOrderManager(null);
		assertTrue(list.size() == 0);
	}

}
