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

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteTypeDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleNumerotationDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.stockage.*;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.stockage.*;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * Classe de test pour le manager ConteneurManager.
 * Classe créée le 23/03/10.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ConteneurManagerTest extends AbstractManagerTest4 {

	@Autowired
	private ConteneurManager conteneurManager;
	@Autowired
	private BanqueDao banqueDao;
	@Autowired
	private PlateformeDao plateformeDao;
	@Autowired
	private ConteneurTypeDao conteneurTypeDao;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private EnceinteTypeDao enceinteTypeDao;
	@Autowired
	private TerminaleTypeDao terminaleTypeDao;
	@Autowired
	private TerminaleNumerotationDao terminaleNumerotationDao;
	@Autowired
	private EnceinteManager enceinteManager;
	@Autowired
	private TerminaleManager terminaleManager;
	@Autowired
	private UtilisateurDao utilisateurDao;
	@Autowired
	private EmplacementManager emplacementManager;
	@Autowired
	private EntiteDao entiteDao;
	@Autowired
	private EchantillonDao echantillonDao;
	@Autowired
	private RetourManager retourManager;
	@Autowired
	private IncidentManager incidentManager;
	
	public ConteneurManagerTest() {		
	}

	
	@Test
	public void testFindById() {
		Conteneur c = conteneurManager.findByIdManager(1);
		assertNotNull(c);
		assertTrue(c.getNom().equals("Congélateur 1"));
		
		c = conteneurManager.findByIdManager(4);
		assertNotNull(c);
		
		Conteneur cNull = conteneurManager.findByIdManager(10);
		assertNull(cNull);
	}
	
	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll() {
		List<Conteneur> list = conteneurManager.findAllObjectsManager();
		assertTrue(list.size() == 4);
	}
	
	/**
	 * Test la méthode findByBanqueWithOrderManager.
	 */
	@Test
	public void testFindByBanqueWithOrderManager() {
		Banque b1 = banqueDao.findById(1);
		List<Conteneur> list = conteneurManager
			.findByBanqueWithOrderManager(b1);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNom().equals("Congélateur 1"));
		
		Banque b2 = banqueDao.findById(2);
		list = conteneurManager
			.findByBanqueWithOrderManager(b2);
		assertTrue(list.size() == 4);
		
		Banque newB = new Banque();
		list = conteneurManager.findByBanqueWithOrderManager(newB);
		assertTrue(list.size() == 0);
		
		list = conteneurManager.findByBanqueWithOrderManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByBanqueAndCodeManager.
	 */
	@Test
	public void testFindByBanqueAndCodeManager() {
		Banque b1 = banqueDao.findById(1);
		List<Conteneur> list = conteneurManager
			.findByBanqueAndCodeManager(b1, "CC1");
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getNom().equals("Congélateur 1"));
		
		list = conteneurManager
			.findByBanqueAndCodeManager(b1, "dcsc");
		assertTrue(list.size() == 0);
		
		Banque b2 = banqueDao.findById(2);
		list = conteneurManager
			.findByBanqueAndCodeManager(b2, "CC1");
		assertTrue(list.size() == 1);
		
		Banque newB = new Banque();
		list = conteneurManager.findByBanqueAndCodeManager(newB, "CC1");
		assertTrue(list.size() == 0);
		
		list = conteneurManager.findByBanqueAndCodeManager(null, "CC1");
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByBanquesWithOrderManager.
	 */
	@Test
	public void testFindByBanquesWithOrderManager() {
		List<Banque> banques = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		banques.add(b1);
		banques.add(b2);
		List<Conteneur> list = conteneurManager
			.findByBanquesWithOrderManager(banques);
		assertTrue(list.size() == 4);
		assertTrue(list.get(0).getNom().equals("Congélateur 1"));
		
		banques = new ArrayList<Banque>();
		banques.add(b1);
		banques.add(new Banque());
		banques.add(null);
		list = conteneurManager
			.findByBanquesWithOrderManager(banques);
		assertTrue(list.size() == 3);
		
		list = conteneurManager.findByBanquesWithOrderManager(
				new ArrayList<Banque>());
		assertTrue(list.size() == 0);
		
		list = conteneurManager.findByBanquesWithOrderManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByPlateformeWithOrderManager.
	 */
	@Test
	public void testFindByPlateformeWithOrderManager() {
		Plateforme p1 = plateformeDao.findById(1);
		List<Conteneur> list = conteneurManager
			.findByPlateformeOrigWithOrderManager(p1);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getCode().equals("CC1"));
		
		Plateforme p3 = plateformeDao.findById(3);
		list = conteneurManager
			.findByPlateformeOrigWithOrderManager(p3);
		assertTrue(list.size() == 0);
		
		Plateforme newP = new Plateforme();
		list = conteneurManager.findByPlateformeOrigWithOrderManager(newP);
		assertTrue(list.size() == 0);
		
		list = conteneurManager.findByPlateformeOrigWithOrderManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode getBanquesManager.
	 */
	@Test
	public void testGetBanquesManager() {
		
		Conteneur c1 = conteneurManager.findByIdManager(1);
		Set<Banque> set = conteneurManager.getBanquesManager(c1);
		assertTrue(set.size() == 4);
		
		Conteneur c2 = conteneurManager.findByIdManager(2);
		set = conteneurManager.getBanquesManager(c2);
		assertTrue(set.size() == 2);
		
		Conteneur newC = new Conteneur();
		set = conteneurManager.getBanquesManager(newC);
		assertTrue(set.size() == 0);
		
		set = conteneurManager.getBanquesManager(null);
		assertTrue(set.size() == 0);
	}
	
	/**
	 * Test la méthode getEnceintesManager.
	 */
	@Test
	public void testGetEnceintesManager() {
		
		Conteneur c1 = conteneurManager.findByIdManager(1);
		Set<Enceinte> set = conteneurManager.getEnceintesManager(c1);
		assertTrue(set.size() == 2);
		
		Conteneur c2 = conteneurManager.findByIdManager(2);
		set = conteneurManager.getEnceintesManager(c2);
		assertTrue(set.size() == 0);
		
		Conteneur newC = new Conteneur();
		set = conteneurManager.getEnceintesManager(newC);
		assertTrue(set.size() == 0);
		
		set = conteneurManager.getEnceintesManager(null);
		assertTrue(set.size() == 0);
	}
	
	/**
	 * Test la méthode getAllTerminalesInArborescenceManager.
	 */
	@Test
	public void testGetAllTerminalesInArborescenceManager() {
		
		Conteneur c1 = conteneurManager.findByIdManager(1);
		List<Terminale> list = conteneurManager
			.getAllTerminalesInArborescenceManager(c1);
		assertTrue(list.size() == 6);
		
		Conteneur c2 = conteneurManager.findByIdManager(2);
		list = conteneurManager.getAllTerminalesInArborescenceManager(c2);
		assertTrue(list.size() == 0);
		
		Conteneur newC = new Conteneur();
		list = conteneurManager.getAllTerminalesInArborescenceManager(newC);
		assertTrue(list.size() == 0);
		
		list = conteneurManager.getAllTerminalesInArborescenceManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode getIncidentsManager.
	 */
	@Test
	public void testGetIncidentsManager() {
		
		Conteneur c1 = conteneurManager.findByIdManager(1);
		Set<Incident> set = conteneurManager.getIncidentsManager(c1);
		assertTrue(set.size() == 2);
		
		Conteneur c2 = conteneurManager.findByIdManager(2);
		set = conteneurManager.getIncidentsManager(c2);
		assertTrue(set.size() == 1);
		
		Conteneur c3 = conteneurManager.findByIdManager(3);
		set = conteneurManager.getIncidentsManager(c3);
		assertTrue(set.size() == 0);
		
		Conteneur newC = new Conteneur();
		set = conteneurManager.getIncidentsManager(newC);
		assertTrue(set.size() == 0);
		
		set = conteneurManager.getIncidentsManager(null);
		assertTrue(set.size() == 0);
	}
	
	/**
	 * Test la méthode getPlateformesManager.
	 */
	@Test
	public void testGetConteneurPlateformesManager() {
		
		Conteneur c1 = conteneurManager.findByIdManager(1);
		Set<ConteneurPlateforme> set = conteneurManager.getConteneurPlateformesManager(c1);
		assertTrue(set.size() == 2);
		
		Conteneur c2 = conteneurManager.findByIdManager(2);
		set = conteneurManager.getConteneurPlateformesManager(c2);
		assertTrue(set.size() == 1);
		
		Conteneur newC = new Conteneur();
		set = conteneurManager.getConteneurPlateformesManager(newC);
		assertTrue(set.size() == 0);
		
		set = conteneurManager.getConteneurPlateformesManager(null);
		assertTrue(set.size() == 0);
	}
	
	/**
	 * Test la méthode findDoublon.
	 */
	@Test
	public void testFindDoublon() {
		
		String code1 = "C999";
		String code2 = "TTT";
		String nom1 = "Congélateur 999";
		String nom2 = "RAAAA";
		
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		List<Banque> banques = new ArrayList<Banque>();
		banques.add(b1);
		banques.add(b2);
		
		Conteneur c1 = new Conteneur();
		c1.setCode(code1);
		c1.setNom(nom1);
		c1.setPlateformeOrig(plateformeDao.findById(2));
		assertTrue(conteneurManager.findDoublonManager(c1, banques));
		
		c1.setCode(code2);
		assertFalse(conteneurManager.findDoublonManager(c1, banques));
		
		c1.setCode(code1);
		c1.setNom(nom2);
		assertTrue(conteneurManager.findDoublonManager(c1, banques));
		
		c1.setCode(code1);
		c1.setNom(nom1);
		banques = new ArrayList<Banque>();
		banques.add(b1);
		assertFalse(conteneurManager.findDoublonManager(c1, banques));
		
		banques = new ArrayList<Banque>();
		banques.add(b1);
		banques.add(b2);
		Conteneur c2 = conteneurManager.findByIdManager(2);
		assertFalse(conteneurManager.findDoublonManager(c2, banques));
		
		c2.setCode(code1);
		c2.setNom(nom1);
		assertFalse(conteneurManager.findDoublonManager(c2, banques));
		
		c2.setPlateformeOrig(plateformeDao.findById(2));
		assertTrue(conteneurManager.findDoublonManager(c2, banques));
		
		assertFalse(conteneurManager.findDoublonManager(null, banques));
		assertFalse(conteneurManager.findDoublonManager(c2, null));
				
	}
	
	/**
	 * Test la méthode isUsedObjectManager.
	 */
	@Test
	public void testIsUsedObjectManager() {
		Conteneur c1 = conteneurManager.findByIdManager(1);
		assertTrue(conteneurManager.isUsedObjectManager(c1));
		
		Conteneur c2 = conteneurManager.findByIdManager(2);
		assertFalse(conteneurManager.isUsedObjectManager(c2));
		
		Conteneur newC = new Conteneur();
		assertFalse(conteneurManager.isUsedObjectManager(newC));
		
		assertFalse(conteneurManager.isUsedObjectManager(null));
	}
	
	/**
	 * Test le CRUD d'un ProtocoleExt.
	 * @throws ParseException 
	 */
	@Test
	public void testCrud() throws ParseException {
		createObjectManagerTest();
		updateObjectManagerTest();
		removeObjectManagerTest();
	}
	
	/**
	 * Teste la methode createObjectManager. 
	 * @throws ParseException 
	 */
	@Test
	public void createObjectManagerTest() throws ParseException {
		
		ConteneurType type = conteneurTypeDao.findById(1);
		Service serv = serviceDao.findById(1);
		Banque bank1 = banqueDao.findById(1);
		Banque bank2 = banqueDao.findById(2);
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(bank1);
		banks.add(bank2);
		Utilisateur u = utilisateurDao.findById(1);
		
		Conteneur c1 = new Conteneur();
		c1.setCode("CR1");
		
		Boolean catched = false;
		// on test l'insertion avec le service null
		try {
			conteneurManager.createObjectManager(
					c1, null, null, null, null, u, plateformeDao.findById(1));
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertEquals(4, conteneurManager.findAllObjectsManager().size());
		
		catched = false;
		// on test l'insertion avec une pf Origine nulle
		try {
			conteneurManager.createObjectManager(
					c1, null, serv, null, null, u, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		
		// on test l'insertion d'un doublon
		c1.setCode("CC1");
		c1.setNom("Congélateur 1");
		try {
			conteneurManager.createObjectManager(
					c1, null, serv, banks, null, u, plateformeDao.findById(1));
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		
		// Test de la validation lors de la création
		c1.setNom("CR1");
		try {
			validationInsert(c1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		
		// On test une insertion valide avec les assos non obligatoires à null
		c1.setCode("CR");
		c1.setNom("Congel");
		c1.setTemp((float) -50.0);
		c1.setPiece("PIECE");
		c1.setNbrEnc(3);
		c1.setNbrNiv(3);
		c1.setDescription("DESC");
		c1.setArchive(false);
		conteneurManager.createObjectManager(
				c1, null, serv, null, null, u, plateformeDao.findById(1));
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		assertTrue(getOperationManager().findByObjectManager(c1).size() == 1);
		int id = c1.getConteneurId();
		// Vérification
		Conteneur cTest = conteneurManager.findByIdManager(id);
		assertNotNull(cTest);
		assertNotNull(cTest.getService());
		assertNull(cTest.getConteneurType());
		assertTrue(cTest.getNbrEnc() == 3);
		assertTrue(cTest.getNbrNiv() == 3);
		assertTrue(cTest.getNom().equals("Congel"));
		assertTrue(cTest.getCode().equals("CR"));
		assertTrue(cTest.getPiece().equals("PIECE"));
		assertTrue(cTest.getDescription().equals("DESC"));
		assertTrue(conteneurManager.getBanquesManager(cTest).size() == 0);
		assertTrue(conteneurManager.getConteneurPlateformesManager(cTest).size() == 0);
		assertTrue(cTest.getPlateformeOrig().getPlateformeId() == 1);
		
		// On test une insertion valide avec les assos non obligatoires
		Conteneur c2 = new Conteneur();
		c2.setCode("CD");
		c2.setNom("TEST");
		List<Plateforme> pfs = new ArrayList<Plateforme>();
		pfs.add(pf1);
		pfs.add(pf2);
		conteneurManager.createObjectManager(c2, type, serv, banks, pfs, u, 
				plateformeDao.findById(2));
		assertTrue(conteneurManager.findAllObjectsManager().size() == 6);
		assertTrue(getOperationManager().findByObjectManager(c2).size() == 1);
		int id2 = c2.getConteneurId();
		// Vérification
		Conteneur cTest2 = conteneurManager.findByIdManager(id2);
		assertNotNull(cTest2);
		assertNotNull(cTest2.getService());
		assertNotNull(cTest2.getConteneurType());
		assertTrue(cTest2.getCode().equals("CD"));
		assertTrue(conteneurManager.getBanquesManager(cTest2).size() == 2);
		assertTrue(conteneurManager.getConteneurPlateformesManager(cTest2).size() == 2);
		
		// Suppression
		conteneurManager.removeObjectManager(cTest, null, u);
		conteneurManager.removeObjectManager(cTest2, null, u);
		assertTrue(getOperationManager()
									.findByObjectManager(cTest).size() == 0);
		assertTrue(getOperationManager()
								.findByObjectManager(cTest2).size() == 0);
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cTest);
		fs.add(cTest2);
		cleanUpFantomes(fs);	
	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	@Test
	public void updateObjectManagerTest() throws ParseException {
		Utilisateur u = utilisateurDao.findById(1);
		ConteneurType type = conteneurTypeDao.findById(1);
		Service serv = serviceDao.findById(1);
		Banque bank1 = banqueDao.findById(1);
		Banque bank2 = banqueDao.findById(2);
		Banque bank3 = banqueDao.findById(3);
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(bank1);
		banks.add(bank2);
		
		Conteneur c = new Conteneur();
		c.setCode("CR");
		c.setNom("TEST UP");
		conteneurManager.createObjectManager(c, null, serv, null, null, u, pf1);
		assertTrue(getOperationManager().findByObjectManager(c).size() == 1);
		assertEquals(5, conteneurManager.findAllObjectsManager().size());
		Integer id = c.getConteneurId();
		
		Boolean catched = false;
		Conteneur cUp1 = conteneurManager.findByIdManager(id);
		// on test l'update avec le service null
		try {
			conteneurManager.updateObjectManager(
					cUp1, null, null, null, null, null, u);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		
		// on test l'update d'un doublon
		cUp1.setCode("CC1");
		cUp1.setNom("Congélateur 1");
		try {
			conteneurManager.updateObjectManager(
					cUp1, null, serv, banks, null, null, u);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		
		// Test de la validation lors de l'update
		cUp1.setNom("CR1");
		try {
			validationUpdate(cUp1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		
		// on teste un doublon sur les incidents
		cUp1.setCode("CR1");
		cUp1.setNom("Congel");
		cUp1.setTemp((float) -50.0);
		cUp1.setPiece("PIECE");
		cUp1.setNbrEnc(3);
		cUp1.setNbrNiv(3);
		cUp1.setDescription("DESC");
		cUp1.setArchive(false);
		List<Incident> incs = new ArrayList<Incident>();
		Incident inc1 = new Incident();
		inc1.setNom("TEST");
		Incident inc2 = new Incident();
		inc2.setNom("TEST");
		incs.add(inc1);
		incs.add(inc2);
		catched = false;
		try {
			conteneurManager.updateObjectManager(
					cUp1, null, serv, null, null, incs, u);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		
		// On test un update valide avec les assos non obligatoires à null
		incs = new ArrayList<Incident>();
		Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("08/11/2009");
		inc1.setDate(d1);
		incs.add(inc1);
		List<Plateforme> pfs = new ArrayList<Plateforme>();
		pfs.add(pf1);
		pfs.add(pf2);
		conteneurManager.updateObjectManager(
				cUp1, null, serv, banks, pfs, incs, u);
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		assertTrue(getOperationManager().findByObjectManager(cUp1).size() == 2);
		// Vérification
		Conteneur cTest = conteneurManager.findByIdManager(id);
		assertNotNull(cTest);
		assertNotNull(cTest.getService());
		assertNull(cTest.getConteneurType());
		assertTrue(cTest.getNbrEnc() == 3);
		assertTrue(cTest.getNbrNiv() == 3);
		assertTrue(cTest.getNom().equals("Congel"));
		assertTrue(cTest.getCode().equals("CR1"));
		assertTrue(cTest.getPiece().equals("PIECE"));
		assertTrue(cTest.getDescription().equals("DESC"));
		assertTrue(conteneurManager.getBanquesManager(cTest).size() == 2);
		assertTrue(conteneurManager.getConteneurPlateformesManager(cTest).size() == 2);
		assertTrue(conteneurManager.getIncidentsManager(cTest).size() == 1);
		
		// On test un update valide avec les assos non obligatoires
		Conteneur cUp2 = conteneurManager.findByIdManager(id);
		cUp2.setCode("CD");
		banks = new ArrayList<Banque>();
		banks.add(bank3);
		pfs = new ArrayList<Plateforme>();
		conteneurManager.updateObjectManager(
				cUp2, type, serv, banks, pfs, null, u);
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		assertTrue(getOperationManager().findByObjectManager(cUp2).size() == 3);
		// Vérification
		Conteneur cTest2 = conteneurManager.findByIdManager(id);
		assertNotNull(cTest2);
		assertNotNull(cTest2.getService());
		assertNotNull(cTest2.getConteneurType());
		assertTrue(cTest2.getCode().equals("CD"));
		assertTrue(conteneurManager.getBanquesManager(cTest2).size() == 1);
		assertTrue(conteneurManager.getConteneurPlateformesManager(cTest2).size() == 0);
		Iterator<Banque> it = conteneurManager.getBanquesManager(cTest2)
			.iterator();
		assertTrue(it.next().getNom().equals("BANQUE3"));
		
		// Suppression
		conteneurManager.removeObjectManager(cTest2, null, u);
		assertTrue(getOperationManager()
								.findByObjectManager(cTest2).size() == 0);
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(incidentManager.findAllObjectsManager().size() == 5);

		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cTest2);
		cleanUpFantomes(fs);
	}
	
	/**
	 * Teste la methode removeObjectManager. 
	 */
	@Test
	public void removeObjectManagerTest() {
		Utilisateur u = utilisateurDao.findById(1);
		// test de la suppression d'un objet null
		conteneurManager.removeObjectManager(null, null, null);
		assertEquals(4, conteneurManager.findAllObjectsManager().size());
		
		// test de la suppression d'un objet utilisé
		Conteneur c1 = conteneurManager.findByIdManager(1);
		boolean catched = false;
		try {
			conteneurManager.removeObjectManager(c1, null, u);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ObjectUsedException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		
		// création d'un nouveau conteneur pour tester la délétion
		ConteneurType type = conteneurTypeDao.findById(1);
		Service serv = serviceDao.findById(1);
		Banque bank1 = banqueDao.findById(1);
		List<Banque> banques = new ArrayList<Banque>();
		banques.add(bank1);
		Plateforme pf1 = plateformeDao.findById(1);
		List<Plateforme> plateformes = new ArrayList<Plateforme>();
		plateformes.add(pf1);
		EnceinteType eType1 = enceinteTypeDao.findById(6);
		EnceinteType eType2 = enceinteTypeDao.findById(2);
		EnceinteType eType3 = enceinteTypeDao.findById(1);
		List<Enceinte> enceintes = new ArrayList<Enceinte>();
		TerminaleType tType = terminaleTypeDao.findById(1);
		TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
		// conteneur
		Conteneur c = new Conteneur();
		c.setCode("C2");
		c.setNom("Congélateur N2");
		c.setTemp((float) -50);
		c.setNbrEnc(2);
		c.setNbrNiv(4);
		c.setConteneurType(type);
		c.setService(serv);
		c.setArchive(false);
		// Enceinte 1er niveau
		Enceinte e1 = new Enceinte();
		e1.setEnceinteType(eType1);
		e1.setNom("R");
		e1.setNbPlaces(3);
		// Enceinte 2eme niveau
		Enceinte e2 = new Enceinte();
		e2.setEnceinteType(eType2);
		e2.setNom("T");
		e2.setNbPlaces(3);
		// Enceinte 3eme niveau
		Enceinte e3 = new Enceinte();
		e3.setEnceinteType(eType3);
		e3.setNom("C");
		e3.setNbPlaces(5);
		enceintes.add(e1);
		enceintes.add(e2);
		enceintes.add(e3);
		// Terminale
		Terminale term = new Terminale();
		term.setTerminaleType(tType);
		term.setNom("BT");
		term.setTerminaleNumerotation(num);
		// 1ères positions
		List<Integer> positions = new ArrayList<Integer>();
		positions.add(null);
		positions.add(null);
		positions.add(null);
		positions.add(null);
		try {
			conteneurManager.createAllArborescenceManager(
					c, enceintes, term, positions,
					banques, plateformes, null, false, u, pf1);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 33);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 96);
		
		List<Enceinte> encs = enceinteManager
							.findAllEnceinteByConteneurManager(c);
		assertTrue(encs.size() == 26);
		
		List<Terminale> terminales = new ArrayList<Terminale>();
		Iterator<Enceinte> encIt = encs.iterator();
		while (encIt.hasNext()) {
			terminales.addAll(terminaleManager
							.findByEnceinteWithOrderManager(encIt.next()));
		}
		assertTrue(terminales.size() == 90);
		
		// suppression du conteneur
		Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
		conteneurManager.removeObjectManager(cTest, null, u);
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cTest);
		fs.addAll(encs);
		fs.addAll(terminales);
		cleanUpFantomes(fs);
	}
	
	/**
	 * Test la validation d'un conteneur lors de sa création.
	 * @param conteneur Conteneur à tester.
	 * @throws ParseException 
	 */
	private void validationInsert(Conteneur conteneur) 
		throws ParseException {
		Utilisateur u = utilisateurDao.findById(1);
		Service serv = serviceDao.findById(1);
		boolean catchedInsert = false;
		
		// On teste une insertion avec un attribut code non valide
		String[] emptyValues = new String[]{null, "", "  ", "¤¤%$$*d", 
				createOverLength(5)};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				conteneur.setCode(emptyValues[i]);
				conteneurManager.createObjectManager(
						conteneur, null, serv, null, null, u, plateformeDao.findById(1));
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		conteneur.setCode("CR");
		
		// On teste une insertion avec un attribut nom non valide
		emptyValues = new String[]{null, "", "  ", "%$$*gd¤¤", 
				createOverLength(50)};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				conteneur.setNom(emptyValues[i]);
				conteneurManager.createObjectManager(
						conteneur, null, serv, null, null, u, plateformeDao.findById(1));
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		conteneur.setNom(null);
		
		// On teste une insertion avec un attribut piece non valide
		emptyValues = new String[]{"", "  ", "%$$*gd¤¤", 
				createOverLength(10)};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				conteneur.setPiece(emptyValues[i]);
				conteneurManager.createObjectManager(
						conteneur, null, serv, null, null, u, plateformeDao.findById(1));
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		conteneur.setPiece(null);
		
		// On teste une insertion avec un attribut nbrNiv non valide
		catchedInsert = false;
		try {
			conteneur.setNbrNiv(-1);
			conteneurManager.createObjectManager(
					conteneur, null, serv, null, null, u, plateformeDao.findById(1));
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catchedInsert = true;
			}
		}
		assertTrue(catchedInsert);
		conteneur.setNbrNiv(null);
		
		// On teste une insertion avec un attribut nbrEnc non valide
		catchedInsert = false;
		try {
			conteneur.setNbrEnc(-1);
			conteneurManager.createObjectManager(
					conteneur, null, serv, null, null, u, plateformeDao.findById(1));
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catchedInsert = true;
			}
		}
		assertTrue(catchedInsert);
		conteneur.setNbrEnc(null);
		
		// On teste une insertion avec un attribut description non valide
		emptyValues = new String[]{"", "  ", 
				createOverLength(250)};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				conteneur.setDescription(emptyValues[i]);
				conteneurManager.createObjectManager(
						conteneur, null, serv, null, null, u, plateformeDao.findById(1));
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		conteneur.setDescription(null);
	}
	
	/**
	 * Test la validation d'un conteneur lors de son update.
	 * @param conteneur Conteneur à tester.
	 * @throws ParseException 
	 */
	private void validationUpdate(Conteneur conteneur) 
		throws ParseException {
		Utilisateur u = utilisateurDao.findById(1);
		Service serv = serviceDao.findById(1);
		boolean catched = false;
		
		// On teste un update avec un attribut code non valide
		String[] emptyValues = new String[]{null, "", "  ", "%$$*d¤¤", 
				createOverLength(5)};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				conteneur.setCode(emptyValues[i]);
				conteneurManager.updateObjectManager(
						conteneur, null, serv, null, null, null, u);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		conteneur.setCode("CR");
		
		// On teste un update avec un attribut nom non valide
		emptyValues = new String[]{null, "", "  ", "%$$*gd¤¤", 
				createOverLength(50)};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				conteneur.setNom(emptyValues[i]);
				conteneurManager.updateObjectManager(
						conteneur, null, serv, null, null, null, u);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		conteneur.setNom(null);
		
		// On teste un update avec un attribut piece non valide
		emptyValues = new String[]{"", "  ", "%$$*gd¤¤", 
				createOverLength(10)};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				conteneur.setPiece(emptyValues[i]);
				conteneurManager.updateObjectManager(
						conteneur, null, serv, null, null, null, u);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		conteneur.setPiece(null);
		
		// On teste un update avec un attribut nbrNiv non valide
		catched = false;
		try {
			conteneur.setNbrNiv(-1);
			conteneurManager.updateObjectManager(
					conteneur, null, serv, null, null, null, u);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		conteneur.setNbrNiv(null);
		
		// On teste un update avec un attribut nbrEnc non valide
		catched = false;
		try {
			conteneur.setNbrEnc(-1);
			conteneurManager.updateObjectManager(
					conteneur, null, serv, null, null, null, u);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		conteneur.setNbrEnc(null);
		
		// On teste un update avec un attribut description non valide
		emptyValues = new String[]{"", "  ", 
				createOverLength(250)};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				conteneur.setDescription(emptyValues[i]);
				conteneurManager.updateObjectManager(
						conteneur, null, serv, null, null, null, u);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		conteneur.setDescription(null);
	}
	
	/**
	 * Teste la methode createAllArborescenceManager. 
	 * @throws ParseException 
	 */
	@Test
	public void testCreateAllArborescenceManager() throws ParseException {
		Utilisateur u = utilisateurDao.findById(1);
		ConteneurType type = conteneurTypeDao.findById(1);
		Service serv = serviceDao.findById(1);
		Banque bank1 = banqueDao.findById(1);
		List<Banque> banques = new ArrayList<Banque>();
		banques.add(bank1);
		Plateforme pf1 = plateformeDao.findById(1);
		List<Plateforme> plateformes = new ArrayList<Plateforme>();
		plateformes.add(pf1);
		
		EnceinteType eType1 = enceinteTypeDao.findById(6);
		EnceinteType eType2 = enceinteTypeDao.findById(2);
		EnceinteType eType3 = enceinteTypeDao.findById(1);
		List<Enceinte> enceintes = new ArrayList<Enceinte>();
		
		TerminaleType tType = terminaleTypeDao.findById(1);
		TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
		
		// conteneur
		Conteneur c = new Conteneur();
		c.setCode("C2");
		c.setNom("Congélateur N2");
		c.setTemp((float) -50);
		c.setNbrEnc(2);
		c.setNbrNiv(4);
		c.setConteneurType(type);
		c.setService(serv);
		c.setArchive(false);
		
		// Enceinte 1er niveau
		Enceinte e1 = new Enceinte();
		e1.setEnceinteType(eType1);
		e1.setNom("R");
		e1.setNbPlaces(3);
		
		// Enceinte 2eme niveau
		Enceinte e2 = new Enceinte();
		e2.setEnceinteType(eType2);
		e2.setNom("T");
		e2.setNbPlaces(3);
		
		// Enceinte 3eme niveau
		Enceinte e3 = new Enceinte();
		e3.setEnceinteType(eType3);
		e3.setNom("C");
		e3.setNbPlaces(5);
		
		enceintes.add(e1);
		enceintes.add(e2);
		enceintes.add(e3);
		
		// Terminale
		Terminale term = new Terminale();
		term.setTerminaleType(tType);
		term.setNom("BT");
		term.setTerminaleNumerotation(num);
		
		// 1ères positions
		List<Integer> goodPositions = new ArrayList<Integer>();
		goodPositions.add(10);
		goodPositions.add(null);
		goodPositions.add(25);
		goodPositions.add(null);
		List<Integer> badPositions = new ArrayList<Integer>();
		badPositions.add(1);
		
		// test avec des éléments nulls ou de mauvais positions
		try {
		conteneurManager.createAllArborescenceManager(
				null, enceintes, term, goodPositions,
				banques, plateformes, null, false, u, pf1);
		conteneurManager.createAllArborescenceManager(
				c, null, term, goodPositions,
				banques, plateformes, null, false, u, pf1);
		conteneurManager.createAllArborescenceManager(
				c, enceintes, null, goodPositions,
				banques, plateformes, null, false, u, pf1);
		conteneurManager.createAllArborescenceManager(
				c, enceintes, term, null,
				banques, plateformes, null, false, u, pf1);
		conteneurManager.createAllArborescenceManager(
				c, enceintes, term, badPositions,
				banques, plateformes, null, false, u, pf1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		
		// test avec exception sur le conteneur
		c.setService(null);
		boolean catched = false;
		try {
			conteneurManager.createAllArborescenceManager(
					c, enceintes, term, goodPositions,
					banques, plateformes, null, false, u, pf1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		c.setService(serv);
		
		// test avec excpetion sur la derniere enceinte
		e3.setEnceinteType(null);
		try {
			conteneurManager.createAllArborescenceManager(
					c, enceintes, term, goodPositions,
					banques, plateformes, null, false, u, pf1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		e3.setEnceinteType(eType3);
		
		// test avec excpetion sur la terminale
		term.setTerminaleType(null);
		catched = false;
		try {
			conteneurManager.createAllArborescenceManager(
					c, enceintes, term, goodPositions,
					banques, plateformes, null, false, u, pf1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		term.setTerminaleType(tType);
		
		try {
			conteneurManager.createAllArborescenceManager(
				c, enceintes, term, goodPositions,
				banques, plateformes, null, false, u, pf1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 33);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 96);
		
		Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
		assertTrue(cTest.getCode().equals("C2"));
		assertTrue(conteneurManager.getEnceintesManager(cTest).size() == 2);
		
		Enceinte eTest1 = conteneurManager.getEnceintesManager(cTest)
			.iterator().next();
		assertTrue(eTest1.getNom().equals("R10"));
		assertTrue(enceinteManager.getEnceintesManager(eTest1).size() == 3);
		
		Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1)
			.iterator().next();
		assertTrue(eTest2.getNom().equals("T1"));
		assertTrue(enceinteManager.getEnceintesManager(eTest2).size() == 3);
		
		Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2)
			.iterator().next();
		assertTrue(eTest3.getNom().equals("C25"));
		assertTrue(enceinteManager.getEnceintesManager(eTest3).size() == 0);
		assertTrue(enceinteManager.getTerminalesManager(eTest3).size() == 5);
		
		Terminale tTest = enceinteManager.getTerminalesManager(eTest3)
			.iterator().next();
		assertTrue(tTest.getNom().equals("BT1"));
		
		// ajoute emplacement pour casser cascade
		Emplacement emp = new Emplacement();
		emp.setPosition(55);
		emp.setObjetId(1);
		emp.setAdrl("ADRL");
		emplacementManager.createObjectManager(emp, 
			terminaleManager.findByEnceinteWithOrderManager(eTest3).get(4), 
			entiteDao.findById(3));
		assertEquals(8, emplacementManager.findAllObjectsManager().size());
		assertFalse(emp.getVide());

		
		catched = false;
		try {
			conteneurManager.removeObjectManager(cTest, null, u);
		} catch (ObjectUsedException oe) {
			catched = true;
			assertTrue(oe.getKey().equals("conteneur.deletion.isUsed"));
			assertFalse(oe.isCascadable());
		} 
		assertTrue(catched);
		
		List<Enceinte> encs = enceinteManager
				.findAllEnceinteByConteneurManager(c);
		assertTrue(encs.size() == 26);
		List<Terminale> terminales = new ArrayList<Terminale>();
		Iterator<Enceinte> encIt = encs.iterator();
		while (encIt.hasNext()) {
			terminales.addAll(terminaleManager
				.findByEnceinteWithOrderManager(encIt.next()));
		}
		assertTrue(terminales.size() == 90);
		
		// si emplacement vide mais associé à évènement de stockage -> archivage
		emp.setObjetId(null);
		emp.setEntite(null);
		emplacementManager.updateObjectManager(emp, emp.getTerminale(), null);
		assertTrue(emp.getVide());
		
		Retour r = new Retour();
		/*Champs obligatoires*/
		Echantillon ec2 = echantillonDao.findById(2);
		r.setTempMoyenne(new Float(22.0));
		r.setDateSortie(Calendar.getInstance());
				
		retourManager.createOrUpdateObjectManager(r, ec2, emp, null, null, null, 
				null, u, "creation");
		
		assertTrue(retourManager.getRetoursForObjectManager(ec2).size() == 1);
		
		conteneurManager.removeObjectManager(cTest, null, u);
		// vérifie archivage du seul conteneur
		assertTrue(conteneurManager.findAllObjectsManager().size() == 5);
		cTest = conteneurManager.findByIdManager(c.getConteneurId());
		assertTrue(cTest.getArchive());
		assertTrue(conteneurManager.getEnceintesManager(cTest).isEmpty());
		assertTrue(conteneurManager.getAllTerminalesInArborescenceManager(cTest)
				.isEmpty());
		
		
		//suppr retour pour permettre cascade
		retourManager
			.removeObjectManager(retourManager
					.getRetoursForObjectManager(ec2).get(0));
		assertTrue(retourManager.getRetoursForObjectManager(ec2).isEmpty());
		
		conteneurManager.removeObjectManager(cTest, null, u);
		
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cTest);
		fs.addAll(encs);
		fs.addAll(terminales);
		cleanUpFantomes(fs);
	
	}
	
	@Test
	public void testCreateConteneurAPaillette() {
		Utilisateur u = utilisateurDao.findById(1);
		ConteneurType type = conteneurTypeDao.findById(1);
		Service serv = serviceDao.findById(1);
		Banque bank1 = banqueDao.findById(1);
		List<Banque> banques = new ArrayList<Banque>();
		banques.add(bank1);
		Plateforme pf1 = plateformeDao.findById(1);
		List<Plateforme> plateformes = new ArrayList<Plateforme>();
		plateformes.add(pf1);
		
		EnceinteType eType1 = enceinteTypeDao.findById(6);
		EnceinteType eType2 = enceinteTypeDao.findById(2);
		EnceinteType eType3 = enceinteTypeDao.findById(1);
		List<Enceinte> enceintes = new ArrayList<Enceinte>();
		
		TerminaleType tType = terminaleTypeDao.findById(1);
		TerminaleNumerotation num = terminaleNumerotationDao.findById(1);
		
		// conteneur
		Conteneur c = new Conteneur();
		c.setCode("C2");
		c.setNom("Congélateur N2");
		c.setTemp((float) -50);
		c.setNbrEnc(2);
		c.setNbrNiv(4);
		c.setConteneurType(type);
		c.setService(serv);
		c.setArchive(false);
		
		// Enceinte 1er niveau
		Enceinte e1 = new Enceinte();
		e1.setEnceinteType(eType1);
		e1.setNom("R");
		e1.setNbPlaces(3);
		
		// Enceinte 2eme niveau
		Enceinte e2 = new Enceinte();
		e2.setEnceinteType(eType2);
		e2.setNom("T");
		e2.setNbPlaces(3);
		
		// Enceinte 3eme niveau
		Enceinte e3 = new Enceinte();
		e3.setEnceinteType(eType3);
		e3.setNom("C");
		e3.setNbPlaces(5);
		
		enceintes.add(e1);
		enceintes.add(e2);
		enceintes.add(e3);
		
		// Terminale
		Terminale term = new Terminale();
		term.setTerminaleType(tType);
		term.setNom("BT");
		term.setTerminaleNumerotation(num);
		
		// 1ères positions
		List<Integer> goodPositions = new ArrayList<Integer>();
		goodPositions.add(10);
		goodPositions.add(null);
		goodPositions.add(25);
		goodPositions.add(null);
		
		try {
			conteneurManager.createAllArborescenceManager(
				c, enceintes, term, goodPositions,
				banques, plateformes, 16, true, u, pf1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals(5, conteneurManager.findAllObjectsManager().size());
		assertTrue(enceinteManager.findAllObjectsManager().size() == 33);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 96);
		
		Conteneur cTest = conteneurManager.findByIdManager(c.getConteneurId());
		assertTrue(cTest.getCode().equals("C2"));
		assertTrue(conteneurManager.getEnceintesManager(cTest).size() == 2);
		
		Enceinte eTest1 = conteneurManager.getEnceintesManager(cTest)
			.iterator().next();
		assertTrue(eTest1.getNom().equals("R10"));
		assertTrue(enceinteManager.getEnceintesManager(eTest1).size() == 3);
		
		Enceinte eTest2 = enceinteManager.getEnceintesManager(eTest1)
			.iterator().next();
		assertTrue(eTest2.getNom().equals("T1"));
		assertTrue(enceinteManager.getEnceintesManager(eTest2).size() == 3);
		
		Enceinte eTest3 = enceinteManager.getEnceintesManager(eTest2)
			.iterator().next();
		assertTrue(eTest3.getNom().equals("C25"));
		assertTrue(enceinteManager.getEnceintesManager(eTest3).size() == 0);
		assertTrue(enceinteManager.getTerminalesManager(eTest3).size() == 5);
		
		Terminale tTest = enceinteManager.getTerminalesManager(eTest3)
			.iterator().next();
		assertTrue(tTest.getNom().equals("TRANSPARENT1"));
		
		List<Enceinte> encs = enceinteManager
								.findAllEnceinteByConteneurManager(c);
		assertTrue(encs.size() == 26);
		
		List<Terminale> terminales = new ArrayList<Terminale>();
		Iterator<Enceinte> encIt = encs.iterator();
		while (encIt.hasNext()) {
		terminales.addAll(terminaleManager
				.findByEnceinteWithOrderManager(encIt.next()));
		}
		assertTrue(terminales.size() == 90);
		
		conteneurManager.removeObjectManager(cTest, null, u);
		
		assertTrue(conteneurManager.findAllObjectsManager().size() == 4);
		assertTrue(enceinteManager.findAllObjectsManager().size() == 7);
		assertTrue(terminaleManager.findAllObjectsManager().size() == 6);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cTest);
		fs.addAll(encs);
		fs.addAll(terminales);
		cleanUpFantomes(fs);
	}
	
	@Test
	public void testGetContainingEnceinteManager() {
		Conteneur c = conteneurManager.findByIdManager(1);
		List<Enceinte> enceintes;
		enceintes = conteneurManager.getContainingEnceinteManager(c);
		assertTrue(enceintes.size() == 7);
		
		c = null;
		enceintes = conteneurManager.getContainingEnceinteManager(c);
		assertTrue(enceintes.isEmpty());
	}
	
	@Test
	public void testRemoveBanqueFromConteneurAndEnceintes() {
		Utilisateur u = utilisateurDao.findById(1);
		Conteneur c = conteneurManager.findByIdManager(1);
		Set<Banque> banksC = conteneurManager.getBanquesManager(c);
		assertTrue(banksC.size() == 4);
		Enceinte e5 = enceinteManager.findByIdManager(5);
		Set<Banque> banske5 = enceinteManager.getBanquesManager(e5);
		assertTrue(banske5.size() == 2);
		Enceinte e7 = enceinteManager.findByIdManager(7);
		Set<Banque> banske7 = enceinteManager.getBanquesManager(e7);
		assertTrue(banske7.size() == 1);
		Banque b = banqueDao.findById(2);
		
		conteneurManager.removeBanqueFromContAndEncManager(c, b);
		assertTrue(conteneurManager.getBanquesManager(c).size() == 3);
		assertTrue(enceinteManager.getBanquesManager(e5).size() == 1);
		assertTrue(enceinteManager.getBanquesManager(e7).size() == 0);
		
		// remise en place 
		conteneurManager.updateObjectManager(c, c.getConteneurType(), 
				c.getService(), new ArrayList<Banque>(banksC), null, null, u);
		enceinteManager.updateObjectManager(e5, e5.getEnceinteType(), 
				e5.getConteneur(), e5.getEnceintePere(), e5.getEntite(), 
											new ArrayList<Banque>(banske5), 
											null, null,
											u,
											null);
		enceinteManager.updateObjectManager(e7, e7.getEnceinteType(), 
				e7.getConteneur(), e7.getEnceintePere(), e7.getEntite(), 
											new ArrayList<Banque>(banske7),
											null, null,
											u,
											null);
		
		assertTrue(conteneurManager.getBanquesManager(c).size() == 4);
		assertTrue(enceinteManager.getBanquesManager(e5).size() == 2);
		assertTrue(enceinteManager.getBanquesManager(e7).size() == 1);
		List<Operation> ops = getOperationManager().findByObjectManager(c);
		for (int i = 0; i < ops.size(); i++) {
			getOperationManager().removeObjectManager(ops.get(i));
		}
		ops = getOperationManager().findByObjectManager(e5);
		for (int i = 0; i < ops.size(); i++) {
			getOperationManager().removeObjectManager(ops.get(i));
		}
		ops = getOperationManager().findByObjectManager(e7);
		for (int i = 0; i < ops.size(); i++) {
			getOperationManager().removeObjectManager(ops.get(i));
		}
		assertEquals(19, getOperationManager().findAllObjectsManager().size());
	}
	
	@Test
	public void testFindConteneurFromEmplacementManager() {
		Emplacement e1 = emplacementManager.findByIdManager(1);
		assertEquals(conteneurManager.findFromEmplacementManager(e1), 
				conteneurManager.findByIdManager(1));
		Emplacement e6 = emplacementManager.findByIdManager(6);
		assertEquals(conteneurManager.findFromEmplacementManager(e6), 
				conteneurManager.findByIdManager(1));
		assertNull(conteneurManager.findFromEmplacementManager(null));
	}
	
	@Test
	public void testFindByPartageManager() {
		List<Conteneur> conts = conteneurManager
				.findByPartageManager(plateformeDao.findById(1), true);
		assertTrue(conts.size() == 1);
		conts = conteneurManager.findByPartageManager(null, true);
		assertTrue(conts.isEmpty());
		conts = conteneurManager.findByPartageManager(plateformeDao.findById(1), null);
		assertTrue(conts.isEmpty());
	}
	
	@Test
	public void testFindTempForEmplacementIdManager() {
		assertTrue(conteneurManager
				.findTempForEmplacementManager(emplacementManager.findByIdManager(2))
				.equals(new Float(-75.0)));
		assertTrue(conteneurManager
				.findTempForEmplacementManager(emplacementManager.findByIdManager(5))
				.equals(new Float(-75.0)));
		assertNull(conteneurManager
				.findTempForEmplacementManager(emplacementManager.findByIdManager(11)));
		assertNull(conteneurManager
				.findTempForEmplacementManager(null));
	}
}
