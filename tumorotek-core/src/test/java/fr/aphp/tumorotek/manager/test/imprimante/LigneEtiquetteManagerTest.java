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
package fr.aphp.tumorotek.manager.test.imprimante;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.LigneEtiquetteManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Classe de test pour le manager LigneEtiquetteManager.
 * Classe créée le 09/06/2011.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class LigneEtiquetteManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private LigneEtiquetteManager ligneEtiquetteManager;
	@Autowired
	private ModeleDao modeleDao;
	@Autowired
	private EntiteDao entiteDao;
	@Autowired
	private ChampLigneEtiquetteManager champLigneEtiquetteManager;
	@Autowired
	private ChampEntiteDao champEntiteDao;
	@Autowired
	private ChampManager champManager;
	
	
	public LigneEtiquetteManagerTest() {
		
	}
	
	/**
	 * Test la méthode findById.
	 */
	@Test
	public void testFindById() {
		LigneEtiquette le = ligneEtiquetteManager.findByIdManager(1);
		assertNotNull(le);
		
		LigneEtiquette leNull = ligneEtiquetteManager.findByIdManager(100);
		assertNull(leNull);
	}

	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll() {
		List<LigneEtiquette> list = ligneEtiquetteManager
			.findAllObjectsManager();
		assertTrue(list.size() == 7);
	}
	
	/**
	 * Test la méthode findByModeleManager.
	 */
	@Test
	public void testFindByModeleManager() {
		Modele m2 = modeleDao.findById(2);
		List<LigneEtiquette> list = ligneEtiquetteManager
			.findByModeleManager(m2);
		assertTrue(list.size() == 7);
		
		Modele m1 = modeleDao.findById(1);
		list = ligneEtiquetteManager.findByModeleManager(m1);
		assertTrue(list.size() == 0);
		
		list = ligneEtiquetteManager.findByModeleManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test de la méthode validateObjectManager().
	 */
	@Test
	public void testValidateObjectManager() {
		Modele m1 = modeleDao.findById(1);
		ChampLigneEtiquette cle1 = champLigneEtiquetteManager
			.findByIdManager(1);
		ChampLigneEtiquette cle2 = champLigneEtiquetteManager
			.findByIdManager(2);
		List<ChampLigneEtiquette> champs = 
			new ArrayList<ChampLigneEtiquette>();
		champs.add(cle1);
		champs.add(cle2);
		LigneEtiquette le1 = new LigneEtiquette();
		le1.setOrdre(1);
		le1.setEntete("ENTETE");
		le1.setContenu("CONTENU");
		le1.setFont("FONT");
		le1.setStyle("STYLE");
		
		Boolean catched = false;
		// on test avec le modele null
		try {
			ligneEtiquetteManager.validateObjectManager(
					le1, null, champs, "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		
		// on vérifie que l'exception est bien lancée depuis un champ
		champs.add(new ChampLigneEtiquette());
		try {
			ligneEtiquetteManager.validateObjectManager(
					le1, m1, champs, "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		
		// teste de la validation des champs
		validationChamps(le1, m1);
	}
	
	@Test
	public void testCrud() throws ParseException {
		createObjectManager();
		updateObjectManager();
	}
	
	private void createObjectManager() throws ParseException {
		
		int cTots = champManager.findAllObjectsManager().size();
		
		Modele m3 = modeleDao.findById(3);
		Entite e1 = entiteDao.findById(1);
		Entite e2 = entiteDao.findById(2);
		List<ChampLigneEtiquette> champs = 
			new ArrayList<ChampLigneEtiquette>();
		
		LigneEtiquette le1 = new LigneEtiquette();
		le1.setOrdre(1);
		le1.setEntete("ENTETE");
		le1.setContenu("CONTENU");
		le1.setFont("FONT");
		le1.setStyle("STYLE");
		le1.setIsBarcode(false);
		le1.setSize(4);
		
		Boolean catched = false;
		// on test l'insertion avec le modele null
		try {
			ligneEtiquetteManager.createObjectManager(
					le1, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
		
		// validation d'un champ
		champs.add(new ChampLigneEtiquette());
		try {
			ligneEtiquetteManager.createObjectManager(le1, m3, champs);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 10);
		
		// insertion valide avec les assos à null
		ligneEtiquetteManager.createObjectManager(le1, m3, null);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
		assertTrue(champLigneEtiquetteManager
					.findAllObjectsManager().size() == 10);
		Integer idL1 = le1.getLigneEtiquetteId();
		
		// Vérification
		LigneEtiquette leTest = ligneEtiquetteManager.findByIdManager(idL1);
		assertNotNull(leTest);
		assertNotNull(leTest.getModele());
		assertTrue(leTest.getEntete().equals("ENTETE"));
		assertTrue(leTest.getContenu().equals("CONTENU"));
		assertTrue(leTest.getFont().equals("FONT"));
		assertTrue(leTest.getStyle().equals("STYLE"));
		assertTrue(leTest.getOrdre() == 1);
		assertTrue(leTest.getSize() == 4);
		assertFalse(leTest.getIsBarcode());
		
		// insertion valide avec les assos
		ImportTemplate it2 = new ImportTemplate();
		it2.setNom("Autre template");
		List<Entite> entites = new ArrayList<Entite>();
		entites.add(e1);
		entites.add(e2);
		
		Champ ch1 = new Champ();
		ch1.setChampEntite(champEntiteDao.findById(1));
		Champ ch2 = new Champ();
		ch2.setChampEntite(champEntiteDao.findById(2));
		ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
		cle1.setChamp(ch1);
		cle1.setEntite(e1);
		cle1.setOrdre(1);
		ChampLigneEtiquette cle2 = new ChampLigneEtiquette();
		cle2.setChamp(ch2);
		cle2.setEntite(e2);
		cle2.setOrdre(2);
		champs = new ArrayList<ChampLigneEtiquette>();
		champs.add(cle1);
		champs.add(cle2);
		LigneEtiquette le2 = new LigneEtiquette();
		le2.setOrdre(2);
		le2.setEntete("ENTETE2");
		le2.setContenu("CONTENU2");
		le2.setFont("FONT2");
		le2.setStyle("STYLE2");
		le2.setIsBarcode(false);
		le2.setSize(4);
				
		ligneEtiquetteManager.createObjectManager(le2, m3, champs);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 9);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 12);
		assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
		Integer idL2 = le2.getLigneEtiquetteId();
		
		// Vérification
		LigneEtiquette leTest2 = ligneEtiquetteManager.findByIdManager(idL2);
		assertNotNull(leTest2);
		assertNotNull(leTest2.getModele());
		List<ChampLigneEtiquette> chps = champLigneEtiquetteManager
			.findByLigneEtiquetteManager(leTest2);
		assertTrue(chps.size() == 2);
		assertTrue(chps.get(0).getEntite().equals(e1));
		assertTrue(chps.get(0).getChamp().equals(ch1));
		assertTrue(chps.get(0).getOrdre() == 1);
		
		ligneEtiquetteManager.removeObjectManager(leTest);
		ligneEtiquetteManager.removeObjectManager(leTest2);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 10);
		assertTrue(champManager.findAllObjectsManager().size() == cTots);
	}
	
	private void updateObjectManager() throws ParseException {
		
		int cTots = champManager.findAllObjectsManager().size();
		
		Modele m3 = modeleDao.findById(3);
		Entite e1 = entiteDao.findById(1);
		Entite e2 = entiteDao.findById(2);
		List<ChampLigneEtiquette> champs = 
			new ArrayList<ChampLigneEtiquette>();
		
		LigneEtiquette le = new LigneEtiquette();
		le.setOrdre(1);
		le.setEntete("ENTETE");
		le.setContenu("CONTENU");
		le.setFont("FONT");
		le.setStyle("STYLE");
		le.setIsBarcode(false);
		le.setSize(4);
		
		Champ ch1 = new Champ();
		ch1.setChampEntite(champEntiteDao.findById(1));
		Champ ch2 = new Champ();
		ch2.setChampEntite(champEntiteDao.findById(2));
		ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
		cle1.setChamp(ch1);
		cle1.setEntite(e1);
		cle1.setOrdre(1);
		ChampLigneEtiquette cle2 = new ChampLigneEtiquette();
		cle2.setChamp(ch2);
		cle2.setEntite(e2);
		cle2.setOrdre(2);
		champs = new ArrayList<ChampLigneEtiquette>();
		champs.add(cle1);
		champs.add(cle2);
		
		ligneEtiquetteManager.createObjectManager(le, m3, champs);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 12);
		assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
		Integer idL = le.getLigneEtiquetteId();
		
		LigneEtiquette leUp = ligneEtiquetteManager.findByIdManager(idL);
		Boolean catched = false;
		// on test l'update avec le modele null
		try {
			ligneEtiquetteManager.updateObjectManager(
					leUp, null, champs, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
		
		// validation d'un champ
		champs = new ArrayList<ChampLigneEtiquette>();
		champs.add(new ChampLigneEtiquette());
		try {
			ligneEtiquetteManager.updateObjectManager(leUp, m3, champs, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 12);
		assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
		
		// update valide avec les assos à null
		leUp.setSize(15);
		leUp.setEntete("UP");
		ligneEtiquetteManager.updateObjectManager(leUp, m3, null, null);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 12);
		assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
		
		// Vérification
		LigneEtiquette leTest = ligneEtiquetteManager.findByIdManager(idL);
		assertNotNull(leTest);
		assertTrue(leTest.getEntete().equals("UP"));
		assertTrue(leTest.getSize() == 15);
		assertTrue(champLigneEtiquetteManager
				.findByLigneEtiquetteManager(leTest).size() == 2);
		
		// update valide avec des champs
		Champ ch3 = new Champ();
		ch3.setChampEntite(champEntiteDao.findById(3));
		ChampLigneEtiquette cle3 = new ChampLigneEtiquette();
		cle3.setChamp(ch3);
		cle3.setEntite(e2);
		cle3.setOrdre(2);
		champs = new ArrayList<ChampLigneEtiquette>();
		champs.add(champLigneEtiquetteManager
				.findByLigneEtiquetteManager(leTest).get(0));
		champs.add(cle3);
		List<ChampLigneEtiquette> champsToRemove = 
			new ArrayList<ChampLigneEtiquette>();
		champsToRemove.add(champLigneEtiquetteManager
				.findByLigneEtiquetteManager(leTest).get(1));
		
		ligneEtiquetteManager.updateObjectManager(
				leUp, m3, champs, champsToRemove);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 12);
		assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);
		
		// Vérification
		LigneEtiquette leTest3 = ligneEtiquetteManager.findByIdManager(idL);
		assertNotNull(leTest3);
		
		List<ChampLigneEtiquette> chps = champLigneEtiquetteManager
			.findByLigneEtiquetteManager(leTest3);
		assertTrue(chps.size() == 2);
		assertTrue(chps.get(0).getChamp().equals(ch1));
		assertTrue(chps.get(1).getChamp().equals(ch3));
		
		ligneEtiquetteManager.removeObjectManager(leTest3);
		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
		assertTrue(champLigneEtiquetteManager
				.findAllObjectsManager().size() == 10);
		assertTrue(champManager.findAllObjectsManager().size() == cTots);
	}
	
	private void validationChamps(LigneEtiquette le, Modele m) {
		// Test de la validation lors de la création
		// On teste une insertion avec un attribut entete non valide
		String[] emptyValues = new String[]{"", "  ", "¢¢kjs",  
				createOverLength(25)};
		for (int i = 0; i < emptyValues.length; i++) {
			boolean catched = false;
			try {
				le.setEntete(emptyValues[i]);
				ligneEtiquetteManager.validateObjectManager(
						le, m, null, "creation");
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		le.setEntete("ENTETE");
		
		// font
		for (int i = 0; i < emptyValues.length; i++) {
			boolean catched = false;
			try {
				le.setFont(emptyValues[i]);
				ligneEtiquetteManager.validateObjectManager(
						le, m, null, "creation");
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		le.setFont("FONT");
		
		// style
		for (int i = 0; i < emptyValues.length; i++) {
			boolean catched = false;
			try {
				le.setStyle(emptyValues[i]);
				ligneEtiquetteManager.validateObjectManager(
						le, m, null, "creation");
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		le.setStyle("STYLE");
		
		// contenu
		emptyValues = new String[]{"", "  ", "¢¢kjs",  
				createOverLength(50)};
		for (int i = 0; i < emptyValues.length; i++) {
			boolean catched = false;
			try {
				le.setContenu(emptyValues[i]);
				ligneEtiquetteManager.validateObjectManager(
						le, m, null, "creation");
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		le.setContenu("CONTENU");
		
		// ordre
		boolean catched = false;
		try {
			le.setOrdre(null);
			ligneEtiquetteManager.validateObjectManager(
					le, m, null, "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		try {
			le.setOrdre(0);
			ligneEtiquetteManager.validateObjectManager(
					le, m, null, "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		
		// size
		catched = false;
		try {
			le.setSize(0);
			ligneEtiquetteManager.validateObjectManager(
					le, m, null, "creation");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
	}

}
