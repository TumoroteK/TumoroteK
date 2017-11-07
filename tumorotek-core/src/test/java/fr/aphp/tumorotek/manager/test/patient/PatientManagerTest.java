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

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.coeur.patient.*;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientValidator;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.patient.*;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.qualite.Fantome;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import org.junit.Test;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * Classe de test pour le manager PatientManager.
 * Classe créée le 30/10/09.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class PatientManagerTest extends AbstractManagerTest {
	
	/* Managers injectes par Spring*/
	private PatientManager patientManager;
	private MaladieDao maladieDao;
	private PatientMedecinDao patientMedecinDao;
	private PatientLienDao patientLienDao;
	private UtilisateurDao utilisateurDao;
	private CollaborateurDao collaborateurDao;
	private LienFamilialDao lienFamilialDao;
	private PatientValidator patientValidator;
	private MaladieManager maladieManager;
	private BanqueManager banqueManager;
	private ChampAnnotationDao champAnnotationDao;
	private AnnotationValeurManager annotationValeurManager;
	private PrelevementManager prelevementManager;
	private NatureDao natureDao;
	private ConsentTypeDao consentTypeDao;
	private ItemDao itemDao;
	private PatientDao patientDao;

	public PatientManagerTest() {		
	}
	
	public void setPatientManager(PatientManager mManager) {
		this.patientManager = mManager;
	}
	
	public void setMaladieDao(MaladieDao mDao) {
		this.maladieDao = mDao;
	}

	public void setPatientMedecinDao(PatientMedecinDao pmDao) {
		this.patientMedecinDao = pmDao;
	}

	public void setPatientLienDao(PatientLienDao plDao) {
		this.patientLienDao = plDao;
	}

	public void setUtilisateurDao(UtilisateurDao uDao) {
		this.utilisateurDao = uDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}

	public void setLienFamilialDao(LienFamilialDao lfDao) {
		this.lienFamilialDao = lfDao;
	}

	public void setPatientValidator(PatientValidator pValidator) {
		this.patientValidator = pValidator;
	}

	public void setMaladieManager(MaladieManager mManager) {
		this.maladieManager = mManager;
	}

	public void setBanqueManager(BanqueManager bManager) {
		this.banqueManager = bManager;
	}

	public void setChampAnnotationDao(ChampAnnotationDao cDao) {
		this.champAnnotationDao = cDao;
	}

	public void setAnnotationValeurManager(AnnotationValeurManager aManager) {
		this.annotationValeurManager = aManager;
	}

	public void setPrelevementManager(PrelevementManager pManager) {
		this.prelevementManager = pManager;
	}

	public void setNatureDao(NatureDao nDao) {
		this.natureDao = nDao;
	}

	public void setConsentTypeDao(ConsentTypeDao cDao) {
		this.consentTypeDao = cDao;
	}

	public void setItemDao(ItemDao iDao) {
		this.itemDao = iDao;
	}

	public void setPatientDao(PatientDao pDao) {
		this.patientDao = pDao;
	}

	/**
	 * Test la méthode findAllObjectsManager.
	 */
	public void testFindAllObjectsManager() {
		List<Patient> pats = patientManager.findAllObjectsManager();
		assertEquals(5, pats.size());
	}
	
	/**
	 * Test la méthode findAllObjectsManager.
	 */
	public void testFindAllObjectsIdsManager() {
		List<Integer> ids = patientManager.findAllObjectsIdsManager();
		assertEquals(5, ids.size());
	}
	
	/**
	 * Test la méthode findAllObjectsIdsWithBanquesManager.
	 */
	public void testFindAllObjectsIdsWithBanquesManager() {
		List<Banque> bks = new ArrayList<Banque>();
		bks.add(banqueManager.findByIdManager(1));
		
		List<Integer> ids = patientManager
			.findAllObjectsIdsWithBanquesManager(bks);
		assertEquals(1, ids.size());
		
		bks.add(banqueManager.findByIdManager(2));
		ids = patientManager.findAllObjectsIdsWithBanquesManager(bks);
		assertTrue(ids.size() == 2);
		
		bks = new ArrayList<Banque>();
		ids = patientManager.findAllObjectsIdsWithBanquesManager(bks);
		assertTrue(ids.size() == 0);
		
		ids = patientManager.findAllObjectsIdsWithBanquesManager(null);
		assertTrue(ids.size() == 0);
	}
	
	/**
	 * Test la methode findByIdsInListManager.
	 */
	public void testFindByIdsInListManager() {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		ids.add(10);
		List<Patient> liste = 
			patientManager.findByIdsInListManager(ids);
		assertEquals(3, liste.size());
		
		ids = new ArrayList<Integer>();
		liste = patientManager.findByIdsInListManager(ids);
		assertTrue(liste.size() == 0);
		
		liste = patientManager.findByIdsInListManager(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test la methode findByNomLikeManager.
	 */
	public void testFindByNomLikeManager() {
		//teste une recherche exactMatch
		List<Patient> pats = 
			patientManager.findByNomLikeManager("SOLIS", true);
		assertTrue(pats.size() == 1);
		//teste une recherche non exactMatch
		pats = patientManager.findByNomLikeManager("MA", false);
		assertTrue(pats.size() == 2);
		//teste une recherche infructueuse
		pats = patientManager.findByNomLikeManager("LUX", true);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByNomLikeManager(null, false);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findByNipLikeManager.
	 */
	public void testFindByNipLikeManager() {
		//teste une recherche exactMatch
		List<Patient> pats = 
			patientManager.findByNipLikeManager("12", true);
		assertTrue(pats.size() == 1);
		//teste une recherche non exactMatch
		pats = patientManager.findByNipLikeManager("87", false);
		assertTrue(pats.size() == 3);
		//teste une recherche infructueuse
		pats = patientManager.findByNipLikeManager("13.3", true);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByNipLikeManager(null, false);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findByNomLikeBothSideManager.
	 */
	public void testFindByNomLikeBothSideManager() {
		//teste une recherche exactMatch
		List<Patient> pats = 
			patientManager.findByNomLikeBothSideManager("SOLIS", true);
		assertTrue(pats.size() == 1);
		//teste une recherche non exactMatch
		pats = patientManager.findByNomLikeBothSideManager("AY", false);
		assertTrue(pats.size() == 1);
		//teste une recherche infructueuse
		pats = patientManager.findByNomLikeBothSideManager("LUX", true);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByNomLikeBothSideManager(null, false);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findByNomLikeBothSideReturnIdsManager.
	 */
	public void testFindByNomLikeBothSideReturnIdsManager() {
		List<Banque> bks = new ArrayList<Banque>();
		bks.add(banqueManager.findByIdManager(1));
		//teste une recherche exactMatch
		List<Integer> pats = 
			patientManager.findByNomLikeBothSideReturnIdsManager(
					"DELPHINO", bks, true);
		assertTrue(pats.size() == 1);
		//teste une recherche non exactMatch
		pats = patientManager.findByNomLikeBothSideReturnIdsManager(
				"LPHI", bks, false);
		assertTrue(pats.size() == 1);
		//teste une recherche infructueuse
		pats = patientManager.findByNomLikeBothSideReturnIdsManager(
				"LUX", bks, true);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByNomLikeBothSideReturnIdsManager(
				null, bks, false);
		assertTrue(pats.size() == 0);
		pats = patientManager.findByNomLikeBothSideReturnIdsManager(
				"LPHI", null, false);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findByNipLikeBothSideReturnIdsManager.
	 */
	public void testFindByNipLikeBothSideReturnIdsManager() {
		List<Banque> bks = new ArrayList<Banque>();
		bks.add(banqueManager.findByIdManager(1));
		//teste une recherche exactMatch
		List<Integer> pats = 
			patientManager.findByNipLikeBothSideReturnIdsManager(
					"876", bks, true);
		assertTrue(pats.size() == 1);
		//teste une recherche non exactMatch
		pats = patientManager.findByNipLikeBothSideReturnIdsManager(
				"87", bks, false);
		assertTrue(pats.size() == 1);
		//teste une recherche infructueuse
		pats = patientManager.findByNipLikeBothSideReturnIdsManager(
				"13.3", bks, true);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByNipLikeBothSideReturnIdsManager(
				null, bks, false);
		assertTrue(pats.size() == 0);
		pats = patientManager.findByNipLikeBothSideReturnIdsManager(
				"87", null, false);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findByDateNaissanceManager.
	 * @throws ParseException 
	 */
	public void testFindByDateNaissanceManager() throws ParseException {
		//recherche fructueuse
		Date search = new SimpleDateFormat("dd/MM/yyyy")
											.parse("03/09/1974");
		List<Patient> pats = patientManager
						.findByDateNaissanceManager(search);
		assertTrue(pats.size() == 1);
		//recherche infructueuse
		search = new SimpleDateFormat("dd/MM/yyyy").parse("15/09/1988");
		pats = patientManager.findByDateNaissanceManager(search);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByDateNaissanceManager(null);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findByEtatIncompletManager.
	 * @throws ParseException 
	 */
	public void testFindByEtatIncompletManager() throws ParseException {
		List<Patient> pats = patientManager
						.findByEtatIncompletManager();
		assertTrue(pats.size() == 1);
	}
	
	/**
	 * Test la methode findByNdaLikeManager.
	 */
	public void testFindByNdaLikeManager() {
		//teste une recherche exactMatch
		List<Patient> pats = 
			patientManager.findByNdaLikeManager("NDA234", true);
		assertTrue(pats.size() == 2);
		//teste une recherche non exactMatch
		pats = patientManager.findByNdaLikeManager("NDA", false);
		assertTrue(pats.size() == 3);
		//teste une recherche infructueuse
		pats = patientManager.findByNdaLikeManager("LUX", true);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findByNdaLikeManager(null, false);
		assertTrue(pats.size() == 0);
	}
	

	/**
	 * Test la methode findAfterDateCreationReturnIdsManager.
	 * @throws ParseException 
	 */
	public void testFindAfterDateCreationReturnIdsManager() 
												throws ParseException {
		List<Banque> bks = new ArrayList<Banque>();
		bks.add(banqueManager.findByIdManager(1));
		//recherche fructueuse
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("30/10/2009");
		Calendar cal = Calendar.getInstance();
		cal.setTime(search);
		List<Integer> pats = patientManager
							.findAfterDateCreationReturnIdsManager(cal, bks);
		assertEquals(1, pats.size());
		bks.add(banqueManager.findByIdManager(2));
		pats = patientManager
			.findAfterDateCreationReturnIdsManager(cal, bks);
		assertTrue(pats.size() == 2);
		//recherche infructueuse
		search = new SimpleDateFormat("dd/MM/yyyy").parse("05/11/2009");
		cal.setTime(search);
		pats = patientManager.findAfterDateCreationReturnIdsManager(cal, bks);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findAfterDateCreationReturnIdsManager(null, bks);
		assertTrue(pats.size() == 0);
		pats = patientManager.findAfterDateCreationReturnIdsManager(cal, null);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la methode findAfterDateModificationManager.
	 * @throws ParseException 
	 */
	public void testFindAfterDateModificationManager() 
												throws ParseException {
		//recherche fructueuse
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
		Calendar cal = Calendar.getInstance();
		cal.setTime(search);
		List<Patient> pats = patientManager
						.findAfterDateModificationManager(cal);
		assertTrue(pats.size() == 1);
		//recherche infructueuse
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
		cal.setTime(search);
		pats = patientManager.findAfterDateModificationManager(cal);
		assertTrue(pats.size() == 0);
		//null recherche
		pats = patientManager.findAfterDateModificationManager(null);
		assertTrue(pats.size() == 0);
	}
	
	/**
	 * Test la méthode findLastCreationManager.
	 */
	public void testFindLastCreationManager() {
		List<Banque> bks = new ArrayList<Banque>();
		bks.add(banqueManager.findByIdManager(1));
		List<Patient> list = patientManager.findLastCreationManager(bks, 5);
		assertEquals(1, list.size());
		
		bks.add(banqueManager.findByIdManager(2));
		list = patientManager.findLastCreationManager(bks, 5);
		assertTrue(list.size() == 2);
//		Calendar date1 = getOperationManager()
//								.findDateCreationManager(list.get(0));
//		Calendar date2 = getOperationManager()
//			.findDateCreationManager(list.get(1));
//		assertTrue(date1.after(date2));
		
		list = patientManager.findLastCreationManager(bks, 1);
		assertTrue(list.size() == 1);
		
		list = patientManager.findLastCreationManager(bks, 0);
		assertTrue(list.size() == 0);
		
		list = patientManager.findLastCreationManager(null, 5);
		assertTrue(list.size() == 0);
		
		list = patientManager.findLastCreationManager(new ArrayList<Banque>(), 
				5);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findAllNipsManager.
	 */
	public void testFindAllNipsManager() {
		List<String> nips = 
			patientManager.findAllNipsManager();
		assertEquals(4, nips.size());
		assertTrue(nips.get(2).equals("876"));
	}
	
	/**
	 * Test la méthode findAllNomsManager.
	 */
	public void testFindAllNomsManager() {
		List<String> noms = 
			patientManager.findAllNomsManager();
		assertEquals(5, noms.size());
		assertTrue(noms.get(2).equals("JACKSON"));
	}
	
	/**
	 * Teste la méthode findDoublon.
	 */
	public void testFindDoublon() {
		// Doublon sur le EQUALS avec nouveau patient
		Patient p1 = patientManager
						.findByNipLikeManager("12", true).get(0);
		Patient pNew = new Patient();
		assertFalse(patientManager.findDoublonManager(pNew));
		pNew.setNom(p1.getNom());
		pNew.setPrenom(p1.getPrenom());
		pNew.setDateNaissance(p1.getDateNaissance());
		pNew.setVilleNaissance(p1.getVilleNaissance());
		assertTrue(pNew.equals(p1));
		assertTrue(patientManager.findDoublonManager(pNew));
		
		// Doublon sur le EQUALS avec patient existant
		Patient p2 = patientManager
			.findByNipLikeManager("0987", true).get(0);
		assertFalse(patientManager.findDoublonManager(p2));
		p2.setNom(p1.getNom());
		p2.setPrenom(p1.getPrenom());
		p2.setDateNaissance(p1.getDateNaissance());
		p2.setVilleNaissance(p1.getVilleNaissance());
		assertTrue(p2.equals(p1));
		assertTrue(patientManager.findDoublonManager(p2));
		
		// Doublon sur le NIP avec nouveau patient
		Patient pNew2 = new Patient();
		pNew2.setNom("Nom");
		pNew2.setPrenom("Prenom");
		pNew2.setDateNaissance(p1.getDateNaissance());
		pNew2.setVilleNaissance("PARIS");
		assertFalse(patientManager.findDoublonManager(pNew2));
		pNew2.setNip(p1.getNip());
		assertTrue(patientManager.findDoublonManager(pNew2));
		
		// Doublon sur le NIP avec patient existant
		p2 = patientManager
			.findByNipLikeManager("0987", true).get(0);
		assertFalse(patientManager.findDoublonManager(p2));
		p2.setNip(p1.getNip());
		assertTrue(patientManager.findDoublonManager(p2));
	}
	
	public void testGetMedecinsManager() {
		Patient p = patientManager.findByNomLikeManager("MAYER", true).get(0);
		assertTrue(patientManager.getMedecinsManager(p).size() == 3);
		assertTrue(patientManager
			.getMedecinsManager(p).get(0).equals(collaborateurDao.findById(1)));
		assertTrue(patientManager
			.getMedecinsManager(p).get(1).equals(collaborateurDao.findById(2)));
		assertTrue(patientManager
			.getMedecinsManager(p).get(2).equals(collaborateurDao.findById(3)));
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
	
	public void createObjectManagerTest() throws ParseException {
		//Insertion nouvel enregistrement
		Patient p = new Patient();
		/*Champs obligatoires*/
		p.setNom("2 MAKOUN");
		p.setNomNaissance("PFANG");
		p.setPrenom("Jean");
		p.setSexe("M");
		p.setPatientEtat("V");
		Date dateNaissance = new SimpleDateFormat("dd/MM/yyyy")
												.parse("17/09/1996");
		p.setDateNaissance(dateNaissance);
		//maladies
		List<Maladie> maladies = new ArrayList<Maladie>();
		Maladie m1 = new Maladie();
		m1.setLibelle("maladie1");
		m1.setPatient(p);
		Maladie m2 = new Maladie();
		m2.setLibelle("maladie2");
		m2.setPatient(p);
		maladies.add(m1);
		maladies.add(m2);
		//medecins
		List<Collaborateur> colls = new ArrayList<Collaborateur>();
		Collaborateur c = collaborateurDao.findById(1);
		Collaborateur c2 = collaborateurDao.findById(2);
		Collaborateur c3 = collaborateurDao.findById(3);
		colls.add(c);
		colls.add(c2);
		colls.add(c3);
		//Liens familiaux
		List<PatientLien> liens = new ArrayList<PatientLien>();
		PatientLien pl1 = new PatientLien();
		Patient mayer = patientManager.findByNomLikeManager("MAYER", true)
																	.get(0);
		pl1.setPatient2(mayer);
		pl1.setLienFamilial(lienFamilialDao.findById(1));
		PatientLien pl2 = new PatientLien();
		pl2.setPatient2(patientManager.findByNomLikeManager("SOLIS", true)
				.get(0));
		pl2.setLienFamilial(lienFamilialDao.findById(2));
		liens.add(pl1);
		liens.add(pl2);
		Utilisateur u = utilisateurDao.findById(2);
		//insertion
		patientManager.createOrUpdateObjectManager(p, maladies, colls, 
						liens, null, null, null, null, u, "creation", "/tmp/",
									false);
		assertTrue((patientManager
				.findByNomLikeManager("2 MAKOUN", true)).size() == 1);
		assertTrue((maladieManager.getMaladiesManager(p)).size() == 2);
		assertTrue(patientManager.getPatientMedecinsManager(p).size() == 3);
		assertTrue(patientMedecinDao
				.findById(new PatientMedecinPK(c3, p)).getOrdre().equals(3));
		assertTrue(patientManager.getPatientLiensManager(p).size() == 2);
		assertTrue(patientLienDao
				.findById(new PatientLienPK(p, mayer)).getLienFamilial()
										.equals(lienFamilialDao.findById(1)));
		assertEquals(1, patientManager.getPatientLiensManager(mayer).size());
		assertTrue(getOperationManager().findByObjectManager(p).size() == 1);
		assertTrue(getOperationManager().findByObjectManager(p).get(0)
										.getOperationType().getNom()
														.equals("Creation"));
		//Insertion d'un doublon engendrant une exception
		Patient p2 = new Patient();
		p2.setNom(p.getNom());
		p2.setPrenom(p.getPrenom());
		p2.setSexe(p.getSexe());
		p2.setDateNaissance(p.getDateNaissance());
		p2.setVilleNaissance(p.getVilleNaissance());
		p2.setPatientEtat("V");
		p2.setDateNaissance(p.getDateNaissance());
		assertTrue(p.equals(p2));
		Boolean catched = false;
		try {
			patientManager.createOrUpdateObjectManager(p2, null, null, null, 
									null, null, null, null,	u, "creation", "/tmp/",
											false);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(patientManager
				.findByNomLikeManager("2 MAKOUN", false).size() == 1);
		
		//validation test Type
		String[] noms = new String[]{"", "  ", null, "12}.4", 
								createOverLength(50), "Mr Jean-2Pierre"};
		String[] prenoms = new String[]{"", "  ",  "e456$$", 
								createOverLength(50), null, "Jeff-you Jr"};
		String[] nips = new String[]{"", "  ", "12%$.4", 
									createOverLength(20), null, "55.8-7"};
		String[] sexes = new String[]{"", "  ", "Test", "Masc", "M"};
		String[] nomNais = new String[]{"", "  ", "*kII$$", 
									createOverLength(50), null, "YAYA"};
		String[] villes = new String[]{"", "  ", "{ERE", 
									createOverLength(100), null, "lOUISI"};
		String[] pays = new String[]{"", "  ", "kllo==$$", 
									createOverLength(100), null, "les Açores"};
		String[] etats = new String[]{"", "  ", null, "Decede", "D"};
		
		int i = 0, j = 0, k = 0, l = 0, o = 0, n = 0, q = 0, r = 0;
		//boolean isValide = (i > 3 && j == 5 && k > 3 && l > 3 
		//							&& o > 3 && n > 3 && q > 3 && r > 3);	
		p2 = new Patient();
		for (i = 0; i < nips.length; i++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		i--;
		for (j = 0; j < noms.length; j++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		j--;
		for (l = 0; l < nomNais.length; l++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		l--;
		for (k = 0; k < prenoms.length; k++) {	
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		k--;
		for (q = 0; q < sexes.length; q++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		q--;
		for (o = 0; o < villes.length; o++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		o--;
		for (n = 0; n < pays.length; n++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], false);
		}
		n--;
		for (r = 0; r < etats.length; r++) {
			validationTest(p2, nips[i], noms[j], nomNais[l], prenoms[k], 
					sexes[q], villes[o], pays[n], etats[r], (r == 4));
		}
		
		//concordance des dates
		Date avant = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2008");
		Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2008");
		Date apres = new SimpleDateFormat("dd/MM/yyyy").parse("03/11/2008");
		Date futur = new SimpleDateFormat("dd/MM/yyyy").parse("31/11/2119");
		Date[] naissances = new Date[]{futur, null, date2};
		Date[] dates = new Date[]{futur, avant, null, date2, apres}; 
		
		for (int x = 0; x < naissances.length; x++) {
			for (int y = 0; y < dates.length; y++) {
				for (int z = 0; z < dates.length; z++) {
					p2.setDateNaissance(naissances[x]);
					p2.setDateEtat(dates[y]);
					p2.setDateDeces(dates[z]);
					try {
						if ((x == 0 || y == 0  || z == 0) 
								|| (x == 2 && (y == 1 || z == 1))) { //valide
							patientManager
								.createOrUpdateObjectManager(p2, null,
								null, null, null, null, null, null, null, "creation", null,
								false);
						}
					} catch (Exception e) {
						assertTrue(e.getClass().getSimpleName().equals(
														"ValidationException"));
					}
				}
			}
		}
		//teste incoherence Etat decede Date deces
		p2.setPatientEtat("V");
		try {
			patientManager.createOrUpdateObjectManager(p2, null,
				null, null, null, null, null, null, null, "creation", null,
								false);
		} catch (Exception e) {
			assertTrue(e.getClass().getSimpleName().equals(
											"ValidationException"));
		}
		p2.setDateDeces(null);		
			
		// teste operation mal renseigne
		try {
		patientManager.createOrUpdateObjectManager(p2, null, null, 
										null, null, null, null,	null, u, null, null,
										false);
		} catch (NullPointerException ne) {
			assertTrue(true);
		}
		try {
		patientManager.createOrUpdateObjectManager(p2, null, null, 
									null, null, null, null,	null, u, "dummy", null,
									false);
		} catch (IllegalArgumentException ie) {
			assertTrue(true);
		}
		assertEquals(6, patientManager.findAllObjectsManager().size());
	}
		
	private void validationTest(Patient p2, String nip, String nom, 
				String nomNais, String prenom, String sexe, String villeNais, 
						String paysNais, String etat, boolean isValide) {
		p2.setNip(nip);
		p2.setNom(nom);
		p2.setPrenom(prenom);
		p2.setNomNaissance(nomNais);
		p2.setSexe(sexe);
		p2.setVilleNaissance(villeNais);
		p2.setPaysNaissance(paysNais);	
		p2.setPatientEtat(etat);
		try {
			if (!isValide) { //car creation valide
				patientManager.createOrUpdateObjectManager(p2, null,
						null, null,	null, null, null, null, null, "creation", null,
								false);
			}
		} catch (Exception e) {
			assertTrue(e.getClass().getSimpleName().equals(
											"ValidationException"));
		}
	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	public void updateObjectManagerTest() throws ParseException {
		//Modification d'un enregistrement
		Patient p = patientManager
						.findByNomLikeManager("2 MAKOUN", false).get(0);
		/*Champs obligatoires*/
		p.setPatientEtat("D");
		Date dateDeces = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
											.parse("18/12/2001 12:00:10");
		p.setDateDeces(dateDeces);
		List<Maladie> maladies = new ArrayList<Maladie>();
		Maladie m3 = new Maladie();
		m3.setLibelle("maladie3");
		m3.setPatient(p);
		maladies.add(m3);
		Maladie m2 = maladieDao.findByLibelle("maladie2").get(0);
		assertTrue(m2.getPatient().equals(p));
		maladies.add(m2);
		//medecins
		List<Collaborateur> colls = new ArrayList<Collaborateur>();
		Collaborateur c4 = collaborateurDao.findById(4);
		Collaborateur c3 = collaborateurDao.findById(3);
		colls.add(c4);
		colls.add(c3);
		//Liens familiaux
		List<PatientLien> liens = new ArrayList<PatientLien>();
		PatientLien pl1 = new PatientLien();
		Patient delphino = patientManager.findByNomLikeManager("DELPHINO", true)
																		.get(0);
		pl1.setPatient2(delphino);
		pl1.setLienFamilial(lienFamilialDao.findById(3));
		PatientLienPK pk = new PatientLienPK(p, 
					patientManager.findByNomLikeManager("SOLIS", true).get(0));
		liens.add(pl1);
		liens.add(patientLienDao.findById(pk));
		Utilisateur u = utilisateurDao.findById(2);
		patientManager.createOrUpdateObjectManager(p, maladies, colls, 
								liens, null, null,	
								null, null, u, "modification", null,
								false);
		assertTrue((patientManager
				.findByNomLikeManager("2 MAKOUN", true))
									.get(0).getPrenom().equals("Jean"));
		assertTrue(maladieManager.getMaladiesManager(p).size() == 2);
		assertTrue(maladieManager.getMaladiesManager(p).contains(m3));
		assertTrue(patientManager.getPatientMedecinsManager(p).size() == 2);
		assertTrue(patientMedecinDao
				.findById(new PatientMedecinPK(c4, p)).getOrdre().equals(1));
		assertTrue(patientMedecinDao
				.findById(new PatientMedecinPK(c3, p)).getOrdre().equals(2));
		assertTrue(patientManager.getPatientLiensManager(p).size() == 2);
		assertTrue(patientLienDao
				.findById(new PatientLienPK(p, delphino)).getLienFamilial()
										.equals(lienFamilialDao.findById(3)));
		assertTrue(patientManager.getPatientLiensManager(patientManager
					.findByNomLikeManager("MAYER", true).get(0)).size() == 0);
		assertTrue(getOperationManager().findByObjectManager(p).size() == 2);
		assertTrue(getOperationManager().findByObjectManager(p).get(1)
										.getOperationType().getNom()
												.equals("Modification"));
		// supprime toutes les associations
		patientManager.createOrUpdateObjectManager(p, null, null, null,
						null, null, null, null,	 u, "modification", null,
										false);
		assertTrue(maladieManager.getMaladiesManager(p).size() == 2);
		assertTrue((patientManager.getPatientMedecinsManager(p)).size() == 2);
		assertTrue((patientManager.getPatientLiensManager(p)).size() == 2);
		patientManager.createOrUpdateObjectManager(p, 
									new ArrayList<Maladie>(), 
									new ArrayList<Collaborateur>(), 
									new ArrayList<PatientLien>(), 
									null, 
									null, null, null,	
									u, "modification", null,
									false);
		assertTrue(maladieManager.getMaladiesManager(p).size() == 0);
		assertTrue((patientManager.getPatientMedecinsManager(p)).size() == 0);
		assertTrue((patientManager.getPatientLiensManager(p)).size() == 0);
		// pour tester la cascade plus loin lors du testRemove
		patientManager.createOrUpdateObjectManager(p, maladies, 
				colls, liens, null, null, null, null, u, "modification", null,
						false);
		assertTrue(maladieManager.getMaladiesManager(p).size() == 2);
		assertTrue((patientManager.getPatientMedecinsManager(p)).size() == 2);
		assertTrue((patientManager.getPatientLiensManager(p)).size() == 2);
		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		// date naissance null
		try {
			p.setNom("DELPHINO");
			p.setPrenom("Mike");
			p.setDateNaissance(null);
			p.setVilleNaissance(null);
			patientManager.createOrUpdateObjectManager(p, null, null, null, 
					null, null,	null, null,	u, "modification", null, false);
		} catch (ValidationException e) {
			catched = true;
		}
		assertTrue(catched);
		assertTrue(patientManager
				.findByNomLikeManager("DELPHINO", true).size() == 1);
		
		// doublon
		catched = true;
		try {
			Patient mayer = patientDao.findById(1);
			p.setNom(mayer.getNom());
			p.setPrenom(mayer.getPrenom());
			p.setVilleNaissance(mayer.getVilleNaissance());
			p.setDateNaissance(mayer.getDateNaissance());
			patientManager.createOrUpdateObjectManager(p, null, null, null, 
					null, null,	null, null,	u, "modification", null,
										false);
		} catch (DoublonFoundException e) {
			catched = true;
		}
		assertTrue(catched);
		assertTrue(patientManager
				.findByNomLikeManager("MAYER", true).size() == 1);
	}
	
	/**
	 * Teste la methode removeObjectManager. 
	 */
	public void removeObjectManagerTest() {
		Utilisateur u = utilisateurDao.findById(1);
		//Suppression de l'enregistrement precedemment insere
		Patient p1 = patientManager
						.findByNomLikeManager("2 MAKOUN", true).get(0);
		patientManager.removeObjectManager(p1, "suppr", u, null);
		assertTrue(patientManager
					.findByNomLikeManager("2 MAKOUN", true).size() == 0);
		assertTrue(getOperationManager().findByObjectManager(p1).size() == 0);
		//verification de la suppression cascade des associations
		assertTrue(maladieDao.findByLibelle("maladie%").size() == 0);
		assertTrue(patientMedecinDao.findAll().size() == 3);
		assertTrue(patientLienDao.findAll().size() == 2);
		
		// isUsedObject
		boolean catched = false;
		try {
			patientManager.removeObjectManager(patientManager
				.findByNomLikeManager("DELPHINO", true).get(0), null, u, null);
		} catch (ObjectUsedException oe) {
			catched = true;
			assertTrue(oe.getKey().equals("patient.deletion.isUsed"));
			assertFalse(oe.isCascadable());
		}
		assertTrue(catched);

		patientManager.removeObjectManager(null, "suppr22", u, null);
	
		// fantome verification et suppression
		assertTrue(getOperationManager().findAllObjectsManager().size() == 22);
		List<Fantome> fs = getFantomeDao().findByNom(p1.getPhantomData());
		assertTrue(fs.size() == 1);
		assertTrue(fs.get(0).getEntite().getNom().equals("Patient"));
		assertTrue(fs.get(0).getCommentaires().equals("suppr"));
		getOperationManager()
				.removeObjectManager(getOperationManager()
										.findByObjectManager(fs.get(0)).get(0));
		
		fs.clear();
		fs.add(getFantomeDao()
				.findByNom(p1.getPhantomData() + ": maladie2").get(0));
		fs.add(getFantomeDao()
				.findByNom(p1.getPhantomData() + ": maladie3").get(0));
		
		for (int i = 0; i < fs.size(); i++) {
			getOperationManager()
				.removeObjectManager(getOperationManager()
								.findByObjectManager(fs.get(i)).get(0));
		}
		
		//verifie que l'etat des tables modifies est revenu identique
		assertTrue(getOperationManager().findAllObjectsManager().size() == 19);
		assertTrue(getFantomeDao().findAll().size() == 5);
		assertTrue(maladieDao.findAll().size() == 6);
		testFindAllObjectsManager();	
	}
	
	
	public void testDateNaissanceCoherence() throws ParseException {
		Patient p = new Patient();
		
		// null validation
		p.setDateNaissance(null);
		Errors errs = patientValidator.checkDateNaissanceCoherence(p);
		assertEquals("patient.dateNaissance.empty",
										errs.getFieldError().getCode());
		
		// limites sup
		p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
														.parse("01/01/1970"));
		errs = patientValidator.checkDateNaissanceCoherence(p);
		assertTrue(errs.getAllErrors().size() == 0);
		
		p.setDateDeces(new SimpleDateFormat("dd/MM/yyyy")
														.parse("01/01/1969"));
		errs = patientValidator.checkDateNaissanceCoherence(p);
		assertEquals("date.validation.supDateDeces",
												errs.getFieldError().getCode());
		
		p = patientManager.findByNomLikeManager("DELPHINO", true).get(0);
		p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
														.parse("14/08/2000"));
		errs = patientValidator.checkDateNaissanceCoherence(p);
		assertEquals("date.validation.supDateUnPrelevement",
												errs.getFieldError().getCode());
		
		p = patientManager.findByNomLikeManager("MAYER", true).get(0);
		p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
														.parse("18/09/1983"));
		errs = patientValidator.checkDateNaissanceCoherence(p);
		assertEquals("date.validation.supDateUnPrelevement",
												errs.getFieldError().getCode());
		
		// limites sup		
		p = new Patient();
		p.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
														.parse("01/01/2069"));
		errs = patientValidator.checkDateNaissanceCoherence(p);
		assertEquals("date.validation.supDateActuelle",
												errs.getFieldError().getCode());
	}
	
	public void testGetTotMaladiesCountManager() {
		Patient p = patientManager.findByNomLikeManager("MAYER", true).get(0);
		assertTrue(patientManager.getTotMaladiesCountManager(p)
												.equals(new Long(2)));
		p = patientManager.findByNomLikeManager("SOLIS", true).get(0);
		assertEquals(Long.valueOf(0), patientManager.getTotMaladiesCountManager(p));
	}
	
	/**
	 * Teste en parallele getTotPrelevementsCountManager
	 * et getCountPrelevementsByBanqueManager.
	 */
	public void testGetTotPrelevementsCountOrByBanqueManager() {
		Patient p = patientManager
						.findByNomLikeManager("DELPHINO", true).get(0);
		Banque b = banqueManager.findByIdManager(1);
		assertTrue(patientManager.getTotPrelevementsCountManager(p)
												.equals(new Long(3)));
		assertTrue(patientManager
			.getCountPrelevementsByBanqueManager(p, b).equals(new Long(2)));
		p = patientManager.findByNomLikeManager("MAYER", true).get(0);
		assertEquals(Long.valueOf(1), patientManager.getTotPrelevementsCountManager(p));
		assertTrue(patientManager
			.getCountPrelevementsByBanqueManager(p, b).equals(new Long(0)));
		p = patientManager.findByNomLikeManager("SOLIS", true).get(0);
		assertTrue(patientManager.getTotPrelevementsCountManager(p)
												.equals(new Long(0)));
		assertTrue(patientManager
			.getCountPrelevementsByBanqueManager(p, b).equals(new Long(0)));
	}
	
	/**
	 * Test la méthode updateMultipleObjectsManager.
	 * @throws ParseException 
	 */
	public void testUpdateMultipleObjectsManager() throws ParseException {
		Banque b1 = banqueManager.findByIdManager(1);
		Utilisateur utilisateur = utilisateurDao.findById(1);
		
		// on insert 3 nouveaux patients pour les tests
		Patient pat1 = new Patient();
		pat1.setNom("LEE");
		pat1.setPrenom("BRUCE");
		pat1.setPaysNaissance("HONG KONG");
		pat1.setVilleNaissance("XIAO");
		pat1.setPatientEtat("D");
		pat1.setSexe("M");
		pat1.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
											.parse("12/12/1943"));

		Patient pat2 = new Patient();
		pat2.setNom("ROURKE");
		pat2.setPrenom("MICKEY");
		pat2.setPaysNaissance("CANADA");
		pat2.setVilleNaissance("HALIFAX");
		pat2.setPatientEtat("Inconnu");
		pat2.setSexe("M");
		pat2.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
											.parse("20/08/1932"));
		
		Patient pat3 = new Patient();
		pat3.setNom("THERON");
		pat3.setPrenom("CHARLIZE");
		pat3.setPaysNaissance("AFRIQUE DU SUD");
		pat3.setVilleNaissance("PRETORIA");
		pat3.setPatientEtat("V");
		pat3.setSexe("F");
		pat3.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
											.parse("28/09/1978"));
		
		List<Patient> patients = new ArrayList<Patient>();
		patients.add(pat1); patients.add(pat2); patients.add(pat3);
		List<AnnotationValeur> listAnnots = new ArrayList<AnnotationValeur>();
		List<AnnotationValeur> listDelete = new ArrayList<AnnotationValeur>();
		
		AnnotationValeur alpha1 = new AnnotationValeur();
		alpha1.setChampAnnotation(champAnnotationDao.findById(1));
		alpha1.setAlphanum("bank1");
		listAnnots.add(alpha1);
		AnnotationValeur alpha2 = new AnnotationValeur();
		alpha2.setChampAnnotation(champAnnotationDao.findById(1));
		alpha2.setAlphanum("bank2");
		listAnnots.add(alpha2);
		AnnotationValeur alpha3 = new AnnotationValeur();
		alpha3.setChampAnnotation(champAnnotationDao.findById(1));
		alpha3.setAlphanum("bank3");
		listAnnots.add(alpha3);
		
		for (int i = 0; i < patients.size(); i++) {
			patientManager
				.createOrUpdateObjectManager(patients.get(i), null, null, null, 
						null, null,	null, null, utilisateur, "creation", null,
									false);
			annotationValeurManager
				.createOrUpdateObjectManager(listAnnots.get(i), null, 
					patients.get(i), b1, null, utilisateur, 
					"creation", "/tmp/", null, null);
		}
		
		int id1 = pat1.getPatientId();
		int id2 = pat2.getPatientId();
		
		assertEquals(8, patientManager.findAllObjectsManager().size());
		assertEquals(15, annotationValeurManager.findAllObjectsManager().size());
		
		// on teste une maj valide
		String paysUp = "FRANCE";
		Patient patUp1 = patientManager
								.findByNomLikeManager("LEE", true).get(0);
		Patient patUp2 = patientManager
								.findByNomLikeManager("ROURKE", true).get(0);
		Patient patUp3 = patientManager
								.findByNomLikeManager("THERON", true).get(0);
		patUp1.setPaysNaissance(paysUp);
		patUp2.setPaysNaissance(paysUp);
		patUp3.setPaysNaissance(paysUp);
		List<Patient> list = new ArrayList<Patient>();
		list.add(patUp1);
		list.add(patUp2);
		list.add(patUp3);
		listAnnots.clear();
		listAnnots.addAll(annotationValeurManager
			.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																pat1));
		listAnnots.addAll(annotationValeurManager
			.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																pat2));
		listAnnots.addAll(annotationValeurManager
			.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																pat3));
		listAnnots.get(0).setAlphanum("old");
		listAnnots.get(1).setAlphanum("mid");
		listAnnots.get(2).setAlphanum("young");
		
		patientManager.updateMultipleObjectsManager(list, null,
								listAnnots, listDelete, utilisateur, "/tmp/");
		
		Patient patTest1 = patientManager
							.findByNomLikeManager("LEE", true).get(0);
		Patient patTest2 = patientManager
							.findByNomLikeManager("ROURKE", true).get(0);
		Patient patTest3 = patientManager
							.findByNomLikeManager("THERON", true).get(0);
		assertTrue(patTest1.getPaysNaissance().equals(paysUp));
		assertTrue(patTest2.getPaysNaissance().equals(paysUp));
		assertTrue(patTest3.getPaysNaissance().equals(paysUp));
		
		assertTrue(annotationValeurManager
			.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																patTest1)
					.get(0).getAlphanum().equals("old"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																	patTest2)
						.get(0).getAlphanum().equals("mid"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																	patTest3)
						.get(0).getAlphanum().equals("young"));
		
		// on teste une maj non valide sur le 1er élément
		patTest1.setPaysNaissance("$*$¤¤");
		String villeUp = "Paris";
		patTest1.setVilleNaissance(villeUp);
		patTest2.setVilleNaissance(villeUp);
		patTest3.setVilleNaissance(villeUp);
		list = new ArrayList<Patient>();
		list.add(patTest1);
		list.add(patTest2);
		list.add(patTest3);
		listAnnots.get(0).setAlphanum("TEST");
		//listAnnots.get(1).setBool(true);
		listAnnots.get(2).setAlphanum("TEST3");
		
		boolean catched = false;
		try {
			patientManager.updateMultipleObjectsManager(list, null, 
												listAnnots, listDelete,
														utilisateur, "/tmp/");
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
				assertTrue(((ValidationException) e)
						.getEntiteObjetException()
						.equals("Patient"));
				assertTrue(((ValidationException) e)
						.getIdentificationObjetException()
						.equals(list.get(0).getNom()));
			}
		}
		assertTrue(catched);
		patTest1 = patientManager.findByNomLikeManager("LEE", true).get(0);
		patTest2 = patientManager.findByNomLikeManager("ROURKE", true).get(0);
		patTest3 = patientManager.findByNomLikeManager("THERON", true).get(0);
		assertFalse(patTest1.getVilleNaissance().equals(villeUp));
		assertFalse(patTest2.getVilleNaissance().equals(villeUp));
		assertFalse(patTest3.getVilleNaissance().equals(villeUp));
		assertFalse(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																	patTest1)
						.get(0).getAlphanum().equals("TEST"));
		
		// on teste une maj non valide sur le 3eme élément
		patTest3.setPaysNaissance("$*$¤¤");
		patTest1.setVilleNaissance(villeUp);
		patTest2.setVilleNaissance(villeUp);
		patTest3.setVilleNaissance(villeUp);
		list = new ArrayList<Patient>();
		list.add(patTest1);
		list.add(patTest2);
		list.add(patTest3);
		listAnnots.get(0).setAlphanum("TEST");
		//listAnnots.get(1).setBool(true);
		listAnnots.get(2).setAlphanum("TEST3");
		
		catched = false;
		try {
			patientManager
				.updateMultipleObjectsManager(list, list, listAnnots,
											listDelete, utilisateur, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		patTest1 = patientManager.findByNomLikeManager("LEE", true).get(0);
		patTest2 = patientManager.findByNomLikeManager("ROURKE", true).get(0);
		patTest3 = patientManager.findByNomLikeManager("THERON", true).get(0);
		list.clear();
		list.add(patTest1);
		list.add(patTest2);
		list.add(patTest3);
		assertFalse(patTest1.getVilleNaissance().equals(villeUp));
		assertFalse(patTest2.getVilleNaissance().equals(villeUp));
		assertFalse(patTest3.getVilleNaissance().equals(villeUp));
		assertFalse(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
																	patTest1)
						.get(0).getAlphanum().equals("TEST"));
		
		// on teste avec creation d'annotation
		listDelete.add(listAnnots.get(0));
		listDelete.add(listAnnots.get(1));
		listAnnots.clear();
		patTest1.setPaysNaissance("NEW");
		AnnotationValeur alpha4 = new AnnotationValeur();
		alpha4.setAlphanum("ADD");
		alpha4.setChampAnnotation(champAnnotationDao.findById(1));
		alpha4.setObjetId(id1);
		alpha4.setBanque(b1);
		listAnnots.add(alpha4);
		AnnotationValeur alpha5 = new AnnotationValeur();
		alpha5.setAlphanum("&&é**¤¤");
		alpha5.setChampAnnotation(champAnnotationDao.findById(1));
		alpha5.setObjetId(id2);
		alpha5.setBanque(b1);
		listAnnots.add(alpha5);
		// validationException
		try {
			patientManager
				.updateMultipleObjectsManager(list, list, listAnnots, listDelete, 
															utilisateur, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"ValidationException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		patTest1 = patientManager.findByNomLikeManager("LEE", true).get(0);
		assertFalse(patTest1.getPaysNaissance().equals("NEW"));
		assertFalse(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
								patTest1).get(0).getAlphanum().equals("ADD"));
		// modifications valides
		//alpha1.setAnnotationValeurId(null);
		alpha5.setAlphanum("yy");
		patientManager
				.updateMultipleObjectsManager(list, list, listAnnots, listDelete, 
															utilisateur, "/tmp/");
		patTest1 = patientManager.findByNomLikeManager("LEE", true).get(0);
		patTest2 = patientManager.findByNomLikeManager("ROURKE", true).get(0);
		patTest3 = patientManager.findByNomLikeManager("THERON", true).get(0);
		assertTrue(patTest1.getPaysNaissance().equals("NEW"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
								patTest1).get(0).getAlphanum().equals("ADD"));

		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
								patTest2).get(0).getAlphanum().equals("yy"));
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), 
							patTest3).get(0).getAlphanum().equals("young"));
		
		// Suppression
		for (int i = 0; i < list.size(); i++) {
			patientManager.removeObjectManager(list.get(i), null , utilisateur, null);
		}
		// verification de l'etat de la base
		testFindAllObjectsManager();
		assertTrue(annotationValeurManager
						.findAllObjectsManager().size() == 12);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.addAll(list);
		cleanUpFantomes(fs);
	}
	
	public void testCreateUpdatePatientWithAnnots() throws ParseException {

		Banque b1 = banqueManager.findByIdManager(1);
		Utilisateur utilisateur = utilisateurDao.findById(1);
		ChampAnnotation c = champAnnotationDao.findById(1);
		
		// on insert 1 nouveau patient pour les tests
		Patient pat = new Patient();
		pat.setNom("THERON");
		pat.setPrenom("CHARLIZE");
		pat.setPatientEtat("V");
		pat.setSexe("F");
		pat.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
												.parse("12/06/1978"));
		
		List<AnnotationValeur> listAnnots = new ArrayList<AnnotationValeur>();
		List<AnnotationValeur> listDelete = new ArrayList<AnnotationValeur>();
		
		AnnotationValeur alpha1 = new AnnotationValeur();
		alpha1.setChampAnnotation(c);
		alpha1.setAlphanum("val1");
		alpha1.setBanque(b1);
		listAnnots.add(alpha1);
		
		// teste erreur sur annots -> rollback
		alpha1.setAlphanum("&é**$$¤¤$");
		boolean catched = false;
		try {
			patientManager
				.createOrUpdateObjectManager(pat, null, null, null, 
						listAnnots, listDelete, null, null, 
						utilisateur, "creation", null,
						false);
		} catch (ValidationException ve) {
			catched = true;
		}
		assertTrue(catched);
		
		// verification de l'etat de la base
		testFindAllObjectsManager();
		
		alpha1.setAlphanum("val1");	
		patientManager
				.createOrUpdateObjectManager(pat, null, null, null, 
						listAnnots, listDelete, null, null,
						utilisateur, "creation", "/tmp/",
						false);
		
		assertTrue(patientManager
							.findByNomLikeManager("THERON", true).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c, pat).get(0)
												.getAlphanum().equals("val1"));
		assertTrue(getOperationManager()
				.findByObjectManager(pat).size() == 2);
		assertTrue(getOperationManager()
				.findByObjectManager(pat).get(1).getOperationType().getNom()
				.equals("Annotation"));
		
		// on teste une deletion puis insertion nouvelle valeur
		listDelete.add(annotationValeurManager
				.findByChampAndObjetManager(c, pat).get(0));
		listAnnots.clear();
		AnnotationValeur alpha2 = new AnnotationValeur();
		alpha2.setChampAnnotation(c);
		alpha2.setAlphanum("val2");
		alpha2.setBanque(b1);
		listAnnots.add(alpha2);
		
		pat.setNomNaissance("JUGGS");
		
		patientManager
			.createOrUpdateObjectManager(pat, null, null, null, 
					listAnnots, listDelete, null, null, 
					utilisateur, "modification", "/tmp/",
					false);
		assertTrue(getOperationManager()
				.findByObjectManager(pat).size() == 4);
		assertTrue(getOperationManager()
				.findByObjectManager(pat).get(3).getOperationType().getNom()
				.equals("Annotation"));
		
		assertTrue(patientManager
				.findByNomLikeManager("JUGGS", true).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c, pat).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c, pat).get(0)
									.getAlphanum().equals("val2"));
		
		// suppression annots
		listDelete.add(annotationValeurManager
				.findByChampAndObjetManager(c, pat).get(0));
		listAnnots.clear();
		
		patientManager
			.createOrUpdateObjectManager(pat, null, null, null, 
				listAnnots, listDelete, null, null, 
				utilisateur, "modification", "/tmp/",
				false);
		
		assertTrue(getOperationManager()
				.findByObjectManager(pat).size() == 6);
		assertTrue(getOperationManager()
				.findByObjectManager(pat).get(5).getOperationType().getNom()
				.equals("Annotation"));
		
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c, pat).size() == 0);
		
		AnnotationValeur alpha3 = new AnnotationValeur();
		alpha3.setChampAnnotation(c);
		alpha3.setAlphanum("val2");
		alpha3.setBanque(b1);
		listAnnots.add(alpha3);
		listDelete.clear();
				
		patientManager
			.createOrUpdateObjectManager(pat, null, null, null, 
					listAnnots, listDelete, null, null, 
					utilisateur, "modification", "/tmp/",
					false);
		
		// Nettoyage
		patientManager.removeObjectManager(pat, null, utilisateur, null);
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(pat);
		cleanUpFantomes(fs);
	}
	
	public void testIsSynchronizedPatientManager() 
			throws ParseException, NoSuchFieldException {
		// patient MAYER
		Patient p1 = patientManager.findByNipLikeManager("12", true).get(0);
		PatientSip patSip = new PatientSip();
		
		patSip.setNip(p1.getNip());
		patSip.setNom(p1.getNom());
		patSip.setPrenom(p1.getPrenom());
		patSip.setDateNaissance(p1.getDateNaissance());
		patSip.setSexe(p1.getSexe());
		patSip.setPatientEtat(p1.getPatientEtat());
		patSip.setVilleNaissance(p1.getVilleNaissance());
		patSip.setPaysNaissance(p1.getPaysNaissance());
		
		assertTrue(patientManager
					.isSynchronizedPatientManager(patSip, p1).isEmpty());
		
		// modification chps obligatoires
		PatientSip patSip2 = patSip.clone();
		patSip2.setNip("567890");
		patSip2.setNom("BEN LADEN");
		patSip2.setPrenom("Oussama");
		patSip2.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
												.parse("01/02/1935"));
		patSip2.setSexe("M");
		patSip2.setPatientEtat("D");
		List<Field> fields = patientManager
								.isSynchronizedPatientManager(patSip2, p1);
		assertTrue(fields.size() == 6);
		assertTrue(fields.contains(Patient.class.getDeclaredField("nip")));
		assertTrue(fields.contains(Patient.class.getDeclaredField("nom")));
		assertTrue(fields.contains(Patient.class.getDeclaredField("prenom")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("dateNaissance")));
		assertTrue(fields.contains(Patient.class.getDeclaredField("sexe")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("patientEtat")));
		
		// modification des champs non obigatoires
		p1.setNomNaissance("JAVIER");
		p1.setDateDeces(new SimpleDateFormat("dd/MM/yyyy")
													.parse("01/01/2011"));
		p1.setDateEtat(new SimpleDateFormat("dd/MM/yyyy")
													.parse("01/01/2011"));
		
		patSip2 = patSip.clone();
		patSip2.setNomNaissance("MALIK");
		patSip2.setVilleNaissance("Islamabad");
		patSip2.setPaysNaissance("PAKISTAN");
		patSip2.setDateEtat(new SimpleDateFormat("dd/MM/yyyy")
													.parse("01/05/2011"));
		patSip2.setDateDeces(new SimpleDateFormat("dd/MM/yyyy")
													.parse("01/05/2011"));
		
		fields = patientManager.isSynchronizedPatientManager(patSip2, p1);
		assertTrue(fields.size() == 5);
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("nomNaissance")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("villeNaissance")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("paysNaissance")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("dateDeces")));
		assertTrue(fields.contains(Patient.class.getDeclaredField("dateEtat")));
		
		// valeurs identiques pour les champs non-nulls 
		patSip2 = patSip.clone();
		patSip2.setNomNaissance(p1.getNomNaissance());
		patSip2.setDateDeces(p1.getDateDeces());
		patSip2.setDateEtat(p1.getDateEtat());
		assertTrue(patientManager
				.isSynchronizedPatientManager(patSip2, p1).isEmpty());

		// nulls coté sip -> n'efface pas les valeurs PATIENT
		PatientSip patSip3 = patSip.clone();
		patSip3.setNomNaissance(null);
		patSip3.setVilleNaissance(null);
		patSip3.setPaysNaissance(null);
		patSip3.setDateDeces(null);
		patSip3.setDateEtat(null);
		
		fields = patientManager.isSynchronizedPatientManager(patSip3, p1);
		assertTrue(fields.size() == 1);
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("nomNaissance")));
//		assertTrue(fields
//				.contains(Patient.class.getDeclaredField("villeNaissance")));
//		assertTrue(fields
//				.contains(Patient.class.getDeclaredField("paysNaissance")));
//		assertTrue(fields
//				.contains(Patient.class.getDeclaredField("dateDeces")));
//		assertTrue(fields.contains(Patient.class.getDeclaredField("dateEtat")));
		
		// nulls coté patient
		p1.setNomNaissance(null);
		p1.setVilleNaissance(null);
		p1.setPaysNaissance(null);
		p1.setDateDeces(null);
		p1.setDateEtat(null);
		
		fields = patientManager.isSynchronizedPatientManager(patSip2, p1);
		assertTrue(fields.size() == 5);
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("nomNaissance")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("villeNaissance")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("paysNaissance")));
		assertTrue(fields
				.contains(Patient.class.getDeclaredField("dateDeces")));
		assertTrue(fields.contains(Patient.class.getDeclaredField("dateEtat")));
		
		// nulls des deux cotés
		assertTrue(patientManager
				.isSynchronizedPatientManager(patSip3, p1).isEmpty());
		
		assertTrue(patientManager
				.isSynchronizedPatientManager(null, p1).isEmpty());
				
		assertTrue(patientManager
				.isSynchronizedPatientManager(patSip, null).isEmpty());
	}
	
	public void testFusionPatientManager() throws ParseException {
		// creation de deux patiens
		Patient p1 = new Patient();
		p1.setNip("1223a");
		p1.setNom("Tricky");
		p1.setPrenom("Dixie");
		p1.setSexe("M");
		p1.setPatientEtat("D");
		p1.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
													.parse("21/03/1980"));
		
		// création d'annotations
		List<AnnotationValeur> listAnnots = new ArrayList<AnnotationValeur>();
		AnnotationValeur alpha1b = new AnnotationValeur();
		alpha1b.setChampAnnotation(champAnnotationDao.findById(1));
		alpha1b.setAlphanum("val23");
		alpha1b.setBanque(banqueManager.findByIdManager(2));
		listAnnots.add(alpha1b);
		AnnotationValeur alpha2b = new AnnotationValeur();
		alpha2b.setChampAnnotation(champAnnotationDao.findById(20));
		alpha2b.setBool(true);
		alpha2b.setBanque(banqueManager.findByIdManager(1));
		listAnnots.add(alpha2b);
		AnnotationValeur alpha3b = new AnnotationValeur();
		alpha3b.setChampAnnotation(champAnnotationDao.findById(21));
		alpha3b.setItem(itemDao.findById(8));
		alpha3b.setBanque(banqueManager.findByIdManager(1));
		listAnnots.add(alpha3b);
		
		// maladies
		List<Maladie> maladies1 = new ArrayList<Maladie>();
		Maladie m1 = new Maladie(); m1.setLibelle("maladie1");
		m1.setPatient(p1);
		Maladie m2 = new Maladie(); m2.setLibelle("maladie2");
		m2.setPatient(p1); 
		maladies1.add(m1); maladies1.add(m2);
		//medecins
		List<Collaborateur> colls1 = new ArrayList<Collaborateur>();
		Collaborateur c1 = collaborateurDao.findById(1); colls1.add(c1);
		Collaborateur c2 = collaborateurDao.findById(2); colls1.add(c2);
		//Liens familiaux
		List<PatientLien> liens1 = new ArrayList<PatientLien>();
		PatientLien pl1 = new PatientLien();
		Patient mayer = patientManager.findByNomLikeManager("MAYER", true)
																	.get(0);
		pl1.setPatient2(mayer);
		pl1.setLienFamilial(lienFamilialDao.findById(1));
		PatientLien pl2 = new PatientLien();
		pl2.setPatient2(patientManager.findByNomLikeManager("SOLIS", true)
				.get(0));
		pl2.setLienFamilial(lienFamilialDao.findById(2));
		liens1.add(pl1); liens1.add(pl2);
		Utilisateur u = utilisateurDao.findById(2);
		
		patientManager.createOrUpdateObjectManager(p1, maladies1, colls1, 
				liens1, listAnnots, null, null, null, u, "creation", "/tmp/", false);
		
		// prelevements
		List<Maladie> malsP1 = new 
			ArrayList<Maladie>(maladieManager.getMaladiesManager(p1));
		Prelevement prel1 = new Prelevement();
		prel1.setCode("prel1");
		prelevementManager.createObjectManager(prel1, 
			banqueManager.findByIdManager(1), 
			natureDao.findById(1), malsP1.get(0), consentTypeDao.findById(1), 
			null, null, null, null, null, null, null, null, null, null, null, u, 
			false, null,
			false);
		Prelevement prel2 = new Prelevement();
		prel2.setCode("prel2");
		prelevementManager.createObjectManager(prel2, 
			banqueManager.findByIdManager(1), 
			natureDao.findById(1), malsP1.get(1), consentTypeDao.findById(1), 
			null, null, null, null, null, null, null, null, null, null, null, u, 
			false, null,
			false);
		
		// nulls tests
		patientManager.fusionPatientManager(p1, null, null, null);
		patientManager.fusionPatientManager(null, p1, null, null);
		
		assertTrue(malsP1.size() == 2);
		assertTrue(patientManager.getPatientMedecinsManager(p1).size() == 2);
		assertTrue(patientManager.getPatientLiensManager(p1).size() == 2);
		assertTrue(getOperationManager().findByObjectManager(p1).size() == 2);
		assertTrue(patientManager.getTotPrelevementsCountManager(p1) == 2);
		assertTrue(annotationValeurManager.findByObjectManager(p1).size() == 3);
		
		Patient p2 = new Patient();
		p2.setNip("1223b");
		p2.setNom("Nixon");
		p2.setPrenom("Richard");
		p2.setSexe("M");
		p2.setPatientEtat("D");
		p2.setDateNaissance(new SimpleDateFormat("dd/MM/yyyy")
													.parse("21/03/1980"));
		
		// création d'annotations
		listAnnots = new ArrayList<AnnotationValeur>();
		AnnotationValeur alpha1 = new AnnotationValeur();
		alpha1.setChampAnnotation(champAnnotationDao.findById(1));
		alpha1.setAlphanum("val1");
		alpha1.setBanque(banqueManager.findByIdManager(1));
		listAnnots.add(alpha1);
		AnnotationValeur alpha2 = new AnnotationValeur();
		alpha2.setChampAnnotation(champAnnotationDao.findById(19));
		alpha2.setBool(true);
		alpha2.setBanque(banqueManager.findByIdManager(1));
		listAnnots.add(alpha2);
		AnnotationValeur alpha3 = new AnnotationValeur();
		alpha3.setChampAnnotation(champAnnotationDao.findById(21));
		alpha3.setItem(itemDao.findById(7));
		alpha3.setBanque(banqueManager.findByIdManager(1));
		listAnnots.add(alpha3);
		
		// maladies
		List<Maladie> maladies2 = new ArrayList<Maladie>();
		Maladie m3 = new Maladie(); m3.setLibelle("maladie3");
		m3.setPatient(p2);
		m2.setPatient(p2); 
		maladies2.add(m2); maladies2.add(m3);
		//medecins
		List<Collaborateur> colls2 = new ArrayList<Collaborateur>();
		Collaborateur c3 = collaborateurDao.findById(3); 
		colls2.add(c3); colls2.add(c1);
		//Liens familiaux
		List<PatientLien> liens2 = new ArrayList<PatientLien>();
		PatientLien pl3 = new PatientLien();
		Patient delphino = patientManager
							.findByNomLikeManager("DELPHINO", true).get(0);
		pl3.setPatient2(delphino);
		pl3.setLienFamilial(lienFamilialDao.findById(1));
		PatientLien pl4 = new PatientLien();
		pl4.setPatient2(patientManager.findByNomLikeManager("SOLIS", true)
				.get(0));
		pl4.setLienFamilial(lienFamilialDao.findById(2));
		liens2.add(pl3); liens2.add(pl4);
		
		patientManager.createOrUpdateObjectManager(p2, maladies2, colls2, 
								liens2, listAnnots, null, null, null, u, 
								"creation", "/tmp/",
								false);
		
		List<Maladie> malsP2 = new 
			ArrayList<Maladie>(maladieManager.getMaladiesManager(p2));
		Prelevement prel3 = new Prelevement();
		prel3.setCode("prel3");
		prelevementManager.createObjectManager(prel3, 
			banqueManager.findByIdManager(1), 
			natureDao.findById(1), malsP2.get(0), consentTypeDao.findById(1), 
			null, null, null, null, null, null, null, null, null, null, null, u, 
			false, null,
			false);
		Prelevement prel4 = new Prelevement();
		prel4.setCode("prel4");
		prelevementManager.createObjectManager(prel4, 
			banqueManager.findByIdManager(1), 
			natureDao.findById(1), malsP2.get(1), consentTypeDao.findById(1), 
			null, null, null, null, null, null, null, null, null, null, null, u, 
			false, null,
			false);
		
		assertTrue(patientManager.getTotPrelevementsCountManager(p2) == 2);
		assertTrue(annotationValeurManager.findByObjectManager(p2).size() == 3);
		
		// fusion
		patientManager.fusionPatientManager(p2, p1, u, "fusion!");
		assertTrue((patientManager
				.findByNipLikeManager("1223", false)).size() == 1);
		Patient fus = patientManager
							.findByNipLikeManager("1223b", true).get(0);
		assertTrue((maladieManager.getMaladiesManager(fus)).size() == 3);
		assertTrue(patientManager.getPatientMedecinsManager(fus).size() == 3);
		assertTrue(patientMedecinDao
			.findById(new PatientMedecinPK(c2, fus)).getOrdre().equals(3));
		assertTrue(patientManager.getPatientLiensManager(fus).size() == 3);
		assertTrue(patientManager.getPatientLiensManager(fus)
			.contains(patientLienDao
					.findById(new PatientLienPK(fus, mayer))));
		assertTrue(getOperationManager().findByObjectManager(fus).size() == 4);
		assertTrue(getOperationManager().findByObjectManager(fus).get(2)
										.getOperationType().getNom()
														.equals("Fusion"));
		assertTrue(!getFantomeDao().findByNom(p1.getPhantomData()).isEmpty());
		
		// prelevements
		m2 = maladieDao.findByLibelle("maladie2").get(0);
		assertTrue(patientManager.getTotPrelevementsCountManager(fus) == 4);
		assertTrue(maladieManager.getPrelevementsManager(m2).size() == 2);
		
		// annotations
		assertEquals(16, annotationValeurManager.findAllObjectsManager().size());
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), fus).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(champAnnotationDao.findById(1), fus).get(0)
				.getAlphanum().equals("val1"));
		assertTrue(annotationValeurManager
				.findByObjectManager(fus).size() == 4);
		
		// clean up
		List<Maladie> mals = new ArrayList<Maladie>();
		mals.addAll(maladieManager.getMaladiesManager(fus));
		m2.setPatient(p1);
		mals.add(m2);
		List<Prelevement> prels = prelevementManager.findByPatientManager(fus);
		for (int i = 0; i < prels.size(); i++) {
			prelevementManager.removeObjectManager(prels.get(i), null, u, null);
		}
		patientManager.removeObjectManager(fus, null, u, null);

		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(fus);
		fs.add(p1);
		fs.addAll(mals);
		fs.addAll(prels);
		cleanUpFantomes(fs);
		testFindAllObjectsManager();
		
	}
	
	public void testFindByNipInListManager() {
		List<String> criteres = new ArrayList<String>();
		criteres.add("12");
		criteres.add("0987");
		criteres.add("876");
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(banqueManager.findByIdManager(1));
		banks.add(banqueManager.findByIdManager(2));
		
		List<Integer> liste = patientManager.findByNipInListManager(
				criteres, banks);
		assertTrue(liste.size() == 2);
		
		criteres = new ArrayList<String>();
		criteres.add("12");
		liste = patientManager.findByNipInListManager(
				criteres, banks);
		assertTrue(liste.size() == 1);
		
		liste = patientManager.findByNipInListManager(
				null, banks);
		assertTrue(liste.size() == 0);
		liste = patientManager.findByNipInListManager(
				criteres, null);
		assertTrue(liste.size() == 0);
		liste = patientManager.findByNipInListManager(
				new ArrayList<String>(), banks);
		assertTrue(liste.size() == 0);
		liste = patientManager.findByNipInListManager(
				criteres, new ArrayList<Banque>());
		assertTrue(liste.size() == 0);
	}
	
	public void testFindByNomInListManager() {
		List<String> criteres = new ArrayList<String>();
		criteres.add("DELPHINO");
		criteres.add("MAYER");
		criteres.add("SOLIS");
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(banqueManager.findByIdManager(1));
		banks.add(banqueManager.findByIdManager(2));
		
		List<Integer> liste = patientManager.findByNomInListManager(
				criteres, banks);
		assertTrue(liste.size() == 2);
		
		criteres = new ArrayList<String>();
		criteres.add("DELPHINO");
		liste = patientManager.findByNomInListManager(criteres, banks);
		assertTrue(liste.size() == 1);
		
		liste = patientManager.findByNomInListManager(null, banks);
		assertTrue(liste.size() == 0);
		liste = patientManager.findByNomInListManager(
				new ArrayList<String>(), banks);
		assertTrue(liste.size() == 0);
		liste = patientManager.findByNomInListManager(
				criteres, null);
		assertTrue(liste.size() == 0);
		liste = patientManager.findByNomInListManager(
				criteres, new ArrayList<Banque>());
		assertTrue(liste.size() == 0);
	}
	
	@Test
	public void testRemoveListFromIds() throws ParseException {
		Utilisateur u2 = utilisateurDao.findById(2);
		
		List<Integer> ids = new ArrayList<Integer>();
		
		// null list
		patientManager.removeListFromIdsManager(null, null, u2);
		// empty list
		patientManager.removeListFromIdsManager(ids, "test", u2);
		// non existing ids
		ids.add(22); ids.add(33);
		patientManager.removeListFromIdsManager(ids, "", u2);

		testFindAllObjectsManager();
		
		Patient p1 = new Patient();
		/*Champs obligatoires*/
		p1.setNom("PAT1");
		p1.setPrenom("Jean");
		p1.setSexe("M");
		p1.setPatientEtat("V");
		Date dateNaissance = new SimpleDateFormat("dd/MM/yyyy")
												.parse("17/09/1996");
		p1.setDateNaissance(dateNaissance);
		patientManager.createOrUpdateObjectManager(p1, null, null, 
						null, null, null, null, null, u2, "creation", "/tmp/",
									false);
		
		Patient p2 = new Patient();
		p2.setNom("PAT2");
		p2.setPrenom("Jean");
		p2.setSexe("M");
		p2.setPatientEtat("V");
		p2.setDateNaissance(dateNaissance);
		patientManager.createOrUpdateObjectManager(p2, null, null, 
				null, null, null, null, null, u2, "creation", "/tmp/",
							false);
		
		Patient p3 = new Patient();
		/*Champs obligatoires*/
		p3.setNom("PAT3");
		p3.setPrenom("Jean");
		p3.setSexe("M");
		p3.setPatientEtat("V");
		p3.setDateNaissance(dateNaissance);
		patientManager.createOrUpdateObjectManager(p3, null, null, 
				null, null, null, null, null, u2, "creation", "/tmp/",
				false);

		assertTrue(patientDao.findByNom("PAT_").size() == 3);
		
		ids.add(p1.getPatientId());
		ids.add(p2.getPatientId());
		ids.add(p3.getPatientId());
		Integer patUsedObject = new Integer(1);
		ids.add(patUsedObject); // ce patient est parent d'un prélèvement
		
		// rollback used object Exception
		boolean catched = false;
		try {
			patientManager
				.removeListFromIdsManager(ids, "suppr list ids", u2);
		} catch (ObjectUsedException oe) {
			catched = true;
			assertTrue(oe.getMessage().equals("patient.deletion.isUsed"));
		}
		assertTrue(catched);
		assertTrue(patientDao.findByNom("PAT_").size() == 3);

		ids.remove(patUsedObject);
		patientManager.removeListFromIdsManager(ids, "suppr list ids", u2);
		assertTrue(patientDao.findByNom("PAT_").isEmpty());
		
		assertTrue(getFantomeDao().findByNom("PAT2 Jean").get(0)
				.getCommentaires().equals("suppr list ids"));
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(p1);
		fs.add(p2);
		fs.add(p3);
		cleanUpFantomes(fs);
		
		testFindAllObjectsManager();
	}	
	
	public void testGetExistingPatientManager() {
		assertNull(patientManager.getExistingPatientManager(null)); 
		assertNull(patientManager.getExistingPatientManager(new Patient())); 
		
		Patient p1 = new Patient();
		p1.setNom("DELPHINOS"); // probleme ici
		p1.setPrenom("MIKA"); // probleme ici
		p1.setDateNaissance(null);
		p1.setSexe("M"); 
		assertNull(patientManager.getExistingPatientManager(p1));
		
		p1.setNom("DELPHINO");
		assertNull(patientManager.getExistingPatientManager(p1));
		
		p1.setPrenom("MIKE");
		assertTrue(patientManager.getExistingPatientManager(p1).getPatientId() == 3);
		
		
	}
}
