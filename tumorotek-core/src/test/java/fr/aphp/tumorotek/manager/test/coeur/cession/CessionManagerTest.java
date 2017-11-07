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
package fr.aphp.tumorotek.manager.test.coeur.cession;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.cession.CessionExamenDao;
import fr.aphp.tumorotek.dao.cession.CessionStatutDao;
import fr.aphp.tumorotek.dao.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.cession.DestructionMotifDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.cession.ContratManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.manager.validation.coeur.cession.CessionValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.cession.*;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.*;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * Classe de test pour le manager CessionManager.
 * Classe créée le 01/02/10.
 * 
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
public class CessionManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private CessionManager cessionManager;
	@Autowired
	private BanqueManager banqueManager;
	@Autowired
	private CessionTypeDao cessionTypeDao;
	@Autowired
	private CessionExamenDao cessionExamenDao;
	@Autowired
	private ContratManager contratManager;
	@Autowired
	private CollaborateurDao collaborateurDao;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private CessionStatutDao cessionStatutDao;
	@Autowired
	private TransporteurDao transporteurDao;
	@Autowired
	private DestructionMotifDao destructionMotifDao;
	@Autowired
	private UtilisateurManager utilisateurManager;
	@Autowired
	private UniteDao uniteDao;
	@Autowired
	private EntiteDao entiteDao;
	@Autowired
	private CederObjetManager cederObjetManager;
	@Autowired
	private ChampAnnotationDao champAnnotationDao;
	@Autowired
	private AnnotationValeurManager annotationValeurManager;
	@Autowired
	private TableAnnotationManager tableAnnotationManager;
	@Autowired
	private DataTypeDao dataTypeDao;
	@Autowired
	private OperationTypeDao operationTypeDao;
	@Autowired
	private PlateformeDao plateformeDao;
	
	public CessionManagerTest() {	
	}

	@Test
	public void testFindById() {
		Cession cess = cessionManager.findByIdManager(1);
		assertNotNull(cess);
		assertTrue(cess.getNumero().equals("55"));
		
		Cession cessNull = cessionManager.findByIdManager(10);
		assertNull(cessNull);
	}

	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll() {
		List<Cession> list = cessionManager.findAllObjectsManager();
		assertTrue(list.size() == 4);
	}
	
	/**
	 * Test la methode findByIdsInListManager.
	 */
	@Test
	public void testFindByIdsInListManager() {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		ids.add(10);
		List<Cession> liste = 
			cessionManager.findByIdsInListManager(ids);
		assertTrue(liste.size() == 3);
		
		ids = new ArrayList<Integer>();
		liste = cessionManager.findByIdsInListManager(ids);
		assertTrue(liste.size() == 0);
		
		liste = cessionManager.findByIdsInListManager(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test la méthode findAllObjectsByBanqueManager.
	 */
	@Test
	public void testFindAllObjectsByBanqueManager() {
		Banque b1 = banqueManager.findByIdManager(1);
		Banque b2 = banqueManager.findByIdManager(2);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		
		List<Cession> list = cessionManager
				.findAllObjectsByBanquesManager(banks);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNumero().equals("2"));

		banks.add(b2);
		list = cessionManager.findAllObjectsByBanquesManager(banks);
		assertTrue(list.size() == 4);
		
		Banque b3 = banqueManager.findByIdManager(3);
		banks.add(b3);
		list = cessionManager.findAllObjectsByBanquesManager(banks);
		assertTrue(list.size() == 4);
		
		list = cessionManager.findAllObjectsByBanquesManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findAllObjectsIdsByBanquesManager.
	 */
	@Test
	public void testFindAllObjectsIdsByBanquesManager() {
		Banque b1 = banqueManager.findByIdManager(1);
		Banque b2 = banqueManager.findByIdManager(2);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		
		List<Integer> list = cessionManager
				.findAllObjectsIdsByBanquesManager(banks);
		assertTrue(list.size() == 3);

		banks.add(b2);
		list = cessionManager.findAllObjectsIdsByBanquesManager(banks);
		assertTrue(list.size() == 4);
		
		Banque b3 = banqueManager.findByIdManager(3);
		banks.add(b3);
		list = cessionManager.findAllObjectsIdsByBanquesManager(banks);
		assertTrue(list.size() == 4);
		
		list = cessionManager.findAllObjectsIdsByBanquesManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByNumeroLikeManager.
	 */
	@Test
	public void testFindByNumeroLikeManager() {
		List<Cession> list = cessionManager
			.findByNumeroLikeManager("55", true);
		assertTrue(list.size() == 1);
		
		list = cessionManager.findByNumeroLikeManager("5", true);
		assertTrue(list.size() == 0);
		
		list = cessionManager.findByNumeroLikeManager("", true);
		assertTrue(list.size() == 0);
		
		list = cessionManager.findByNumeroLikeManager(null, true);
		assertTrue(list.size() == 0);
		
		list = cessionManager.findByNumeroLikeManager("55", false);
		assertTrue(list.size() == 1);
		
		list = cessionManager.findByNumeroLikeManager("5", false);
		assertTrue(list.size() == 1);
		
		list = cessionManager.findByNumeroLikeManager("", false);
		assertTrue(list.size() == 4);
		
		list = cessionManager.findByNumeroLikeManager(null, false);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByNumeroWithBanqueReturnIdsManager.
	 */
	@Test
	public void testFindByNumeroWithBanqueReturnIdsManager() {
		Banque b1 = banqueManager.findByIdManager(1);
		Banque b2 = banqueManager.findByIdManager(2);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		
		List<Integer> list = cessionManager
			.findByNumeroWithBanqueReturnIdsManager("55", banks, true);
		assertTrue(list.size() == 1);
		
		list = cessionManager
				.findByNumeroWithBanqueReturnIdsManager("55", banks, false);
			assertTrue(list.size() == 1);
			
		list = cessionManager
				.findByNumeroWithBanqueReturnIdsManager("5", banks, false);
		assertTrue(list.size() == 2);
		
		list = cessionManager
				.findByNumeroWithBanqueReturnIdsManager("5", banks, true);
		assertTrue(list.size() == 0);
		
		list = cessionManager
				.findByNumeroWithBanqueReturnIdsManager("5%", banks, true);
		assertTrue(list.size() == 1);
		
		banks.add(b2);
		list = cessionManager
			.findByNumeroWithBanqueReturnIdsManager("55", banks, true);
		assertTrue(list.size() == 1);
		
		list = cessionManager
				.findByNumeroWithBanqueReturnIdsManager("5", banks, false);
			assertTrue(list.size() == 2);
		
		list = cessionManager
			.findByNumeroWithBanqueReturnIdsManager("0", banks, false);
		assertTrue(list.size() == 0);
		
		list = cessionManager
			.findByNumeroWithBanqueReturnIdsManager(null, banks, false);
		assertTrue(list.size() == 0);
		
		list = cessionManager
			.findByNumeroWithBanqueReturnIdsManager("55", null, true);
		assertTrue(list.size() == 0);
		
		list = cessionManager
			.findByNumeroWithBanqueReturnIdsManager(null, null, false);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByStatutWithBanquesReturnIdsManager.
	 */
	@Test
	public void testFindByStatutWithBanquesReturnIdsManager() {
		Banque b1 = banqueManager.findByIdManager(1);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		
		List<Integer> list = cessionManager
			.findByStatutWithBanquesReturnIdsManager("VALIDEE", banks);
		assertTrue(list.size() == 2);
		
		list = cessionManager
			.findByStatutWithBanquesReturnIdsManager("EN ATTENTE", banks);
		assertTrue(list.size() == 0);
		
		Banque b2 = banqueManager.findByIdManager(2);
		banks.add(b2);
		list = cessionManager
			.findByStatutWithBanquesReturnIdsManager("EN ATTENTE", banks);
		assertTrue(list.size() == 1);
		
		list = cessionManager
			.findByStatutWithBanquesReturnIdsManager("", banks);
		assertTrue(list.size() == 0);
		
		list = cessionManager
			.findByStatutWithBanquesReturnIdsManager(null, banks);
		assertTrue(list.size() == 0);
		
		list = cessionManager
			.findByStatutWithBanquesReturnIdsManager("VALIDEE", null);
		assertTrue(list.size() == 0);
		
		list = cessionManager
			.findByStatutWithBanquesReturnIdsManager(null, null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test la méthode findByEtatIncompletWithBanquesReturnIdsManager.
	 */
	@Test
	public void testFindByEtatIncompletWithBanquesReturnIdsManager() {
		Banque b1 = banqueManager.findByIdManager(1);
		Banque b2 = banqueManager.findByIdManager(2);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		
		List<Integer> list = cessionManager
			.findByEtatIncompletWithBanquesReturnIdsManager(false, banks);
		assertTrue(list.size() == 3);
		
		list = cessionManager
			.findByEtatIncompletWithBanquesReturnIdsManager(true, banks);
		assertTrue(list.size() == 0);
		
		banks.add(b2);
		list = cessionManager
			.findByEtatIncompletWithBanquesReturnIdsManager(false, banks);
		assertTrue(list.size() == 3);
		
		list = cessionManager
			.findByEtatIncompletWithBanquesReturnIdsManager(true, banks);
		assertTrue(list.size() == 1);
		
		list = cessionManager
			.findByEtatIncompletWithBanquesReturnIdsManager(true, null);
		assertTrue(list.size() == 0);
		
	}
	
	/**
	 * Test la méthode findAllCodesForBanqueManager.
	 */
	@Test
	public void testFindAllCodesForBanqueManager() {
		Banque b = banqueManager.findByIdManager(1);
		List<String> numeros = 
			cessionManager.findAllCodesForBanqueManager(b);
		assertTrue(numeros.size() == 3);
		assertTrue(numeros.get(0).equals("2"));
		
		Banque b2 = banqueManager.findByIdManager(2);
		numeros = cessionManager.findAllCodesForBanqueManager(b2);
		assertTrue(numeros.size() == 1);
		
		Banque b3 = banqueManager.findByIdManager(3);
		numeros = cessionManager.findAllCodesForBanqueManager(b3);
		assertTrue(numeros.size() == 0);
		
		numeros = cessionManager.findAllCodesForBanqueManager(null);
		assertTrue(numeros.size() == 0);
	}
	
	/**
	 * Test la méthode getCederObjetsManager.
	 */
	@Test
	public void testGetCederObjetsManager() {
		Cession cess1 = cessionManager.findByIdManager(1);
		Set<CederObjet> list = 
			cessionManager.getCederObjetsManager(cess1);
		assertTrue(list.size() == 2);
		
		Cession cess3 = cessionManager.findByIdManager(3);
		list = cessionManager.getCederObjetsManager(cess3);
		assertTrue(list.size() == 0);
		
		list = cessionManager.getCederObjetsManager(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * @version 2.1
	 */
	@Test
	public void testFindDoublon() {
		
		String numero1 = "55";
		String numero2 = "5";
		Banque b1 = banqueManager.findByIdManager(1);
		Banque b2 = banqueManager.findByIdManager(2);
		
		Cession cess1 = new Cession();
		cess1.setNumero(numero1);
		cess1.setBanque(b1);
		assertTrue(cess1.equals(cessionManager.findByIdManager(1)));
		assertTrue(cessionManager.findDoublonManager(cess1));
		
		cess1.setNumero(numero2);
		assertFalse(cess1.equals(cessionManager.findByIdManager(1)));
		assertFalse(cessionManager.findDoublonManager(cess1));
		
		cess1.setNumero(numero1);
		cess1.setBanque(b2);
		assertFalse(cess1.equals(cessionManager.findByIdManager(1)));
		assertTrue(cessionManager.findDoublonManager(cess1));
		
		// pf
		cess1.setBanque(banqueManager.findByIdManager(4));
		assertFalse(cess1.equals(cessionManager.findByIdManager(1)));
		assertFalse(cessionManager.findDoublonManager(cess1));

		// null
		cess1.setBanque(null);
		assertFalse(cess1.equals(cessionManager.findByIdManager(1)));
		assertFalse(cessionManager.findDoublonManager(cess1));
		
		Cession cess2 = cessionManager.findByIdManager(2);
		assertFalse(cessionManager.findDoublonManager(cess2));
		
		cess2.setNumero(numero1);
		assertTrue(cessionManager.findDoublonManager(cess2));
		
		cess2.setBanque(b2);
		assertTrue(cessionManager.findDoublonManager(cess2));
		
		cess2.setBanque(banqueManager.findByIdManager(4));
		assertFalse(cessionManager.findDoublonManager(cess2));
		
		assertFalse(cessionManager.findDoublonManager(null));
				
	}
	
	/**
	 * Test la méthode isUsedObjectManager.
	 */
	@Test
	public void testIsUsedObjectManager() {
		Cession cess = cessionManager.findByIdManager(1);
		assertTrue(cessionManager.isUsedObjectManager(cess));
		
		cess = cessionManager.findByIdManager(3);
		assertFalse(cessionManager.isUsedObjectManager(cess));
		
		assertFalse(cessionManager.isUsedObjectManager(null));
	}
	
	/**
	 * Test la methode findAfterDateCreationReturnIdsManager.
	 * @throws ParseException 
	 */
	@Test
	public void testFindAfterDateCreationReturnIdsManager() 
												throws ParseException {
		List<Banque> banks = new ArrayList<Banque>();
		Banque b1 = banqueManager.findByIdManager(1);
		banks.add(b1);
		//recherche fructueuse
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2009");
		Calendar cal = Calendar.getInstance();
		cal.setTime(search);
		List<Integer> liste = cessionManager
						.findAfterDateCreationReturnIdsManager(cal, banks);
		assertTrue(liste.size() == 2);
		//recherche infructueuse
		Banque b2 = banqueManager.findByIdManager(2);
		banks.add(b2);
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2010");
		cal.setTime(search);
		liste = cessionManager.findAfterDateCreationReturnIdsManager(
				cal, banks);
		assertTrue(liste.size() == 0);
		//null recherche
		liste = cessionManager.findAfterDateCreationReturnIdsManager(
				null, banks);
		assertTrue(liste.size() == 0);
		
		// recherche avec une banque null
		search = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
		cal.setTime(search);
		liste = cessionManager
			.findAfterDateCreationReturnIdsManager(cal, null);
		assertTrue(liste.size() == 0);
		//null recherche
		liste = cessionManager.findAfterDateCreationReturnIdsManager(
				null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test la methode findAfterDateModificationManager.
	 * @throws ParseException 
	 */
	@Test
	public void testFindAfterDateModificationManager() 
												throws ParseException {
		Banque b1 = banqueManager.findByIdManager(1);
		//recherche fructueuse
		Date search = new SimpleDateFormat("dd/MM/yyyy").parse("09/12/2009");
		Calendar cal = Calendar.getInstance();
		cal.setTime(search);
		List<Cession> liste = cessionManager
						.findAfterDateModificationManager(cal, b1);
		assertTrue(liste.size() == 1);
		//recherche infructueuse
		search = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2010");
		cal.setTime(search);
		liste = cessionManager.findAfterDateModificationManager(cal, b1);
		assertTrue(liste.size() == 0);
		//null recherche
		liste = cessionManager.findAfterDateModificationManager(null, b1);
		assertTrue(liste.size() == 0);
		
		// recherche avec une banque null
		search = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
		cal.setTime(search);
		liste = cessionManager
			.findAfterDateModificationManager(cal, null);
		assertTrue(liste.size() == 0);
		//null recherche
		liste = cessionManager.findAfterDateModificationManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test la méthode findLastCreationManager.
	 */
	@Test
	public void testFindLastCreationManager() {
		Banque b1 = banqueManager.findByIdManager(1);
		Banque b2 = banqueManager.findByIdManager(2);
		Banque b3 = banqueManager.findByIdManager(3);
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		
		List<Cession> list = cessionManager
			.findLastCreationManager(banks, 5);
		assertTrue(list.size() == 3);
		Calendar date1 = getOperationManager()
			.findDateCreationManager(list.get(0));
		Calendar date2 = getOperationManager()
			.findDateCreationManager(list.get(1));
		assertTrue(date1.after(date2));
		
		list = cessionManager.findLastCreationManager(banks, 1);
		assertTrue(list.size() == 1);
		
		list = cessionManager.findLastCreationManager(banks, 0);
		assertTrue(list.size() == 0);
		
		banks.add(b2);		
		list = cessionManager.findLastCreationManager(banks, 5);
		assertTrue(list.size() == 4);
		
		banks.add(b3);
		banks.remove(b1);
		list = cessionManager.findLastCreationManager(banks, 10);
		assertTrue(list.size() == 1);
		
		list = cessionManager.findLastCreationManager(null, 10);
		assertTrue(list.size() == 0);
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
	private void createObjectManagerTest() throws ParseException {
		
		Banque bank = banqueManager.findByIdManager(1);
		CessionType type = cessionTypeDao.findById(1);
		CessionExamen exam = cessionExamenDao.findById(1);
		Collaborateur destinataire = collaborateurDao.findById(1);
		Service servDest = serviceDao.findById(1);
		Collaborateur demandeur = collaborateurDao.findById(2);
		CessionStatut statut = cessionStatutDao.findById(1);
		Collaborateur executant = collaborateurDao.findById(3);
		Transporteur transp = transporteurDao.findById(1);
		DestructionMotif motif = destructionMotifDao.findById(1);
		Utilisateur utilisateur = utilisateurManager.findByIdManager(1);
		Unite qteU = uniteDao.findById(5);
		
		Date dateDemande = new SimpleDateFormat(
				"dd/MM/yyyy").parse("01/11/2008");
		Date dateValidation = new SimpleDateFormat(
				"dd/MM/yyyy").parse("02/11/2008");
		Calendar dateDepart = Calendar.getInstance();
		dateDepart.setTime(new SimpleDateFormat(
				"dd/MM/yyyy").parse("03/11/2008"));
		Calendar dateArrivee = Calendar.getInstance();
		dateArrivee.setTime(new SimpleDateFormat(
				"dd/MM/yyyy").parse("04/11/2008"));
		Calendar dateDestrcution = Calendar.getInstance();
		dateDestrcution.setTime(new SimpleDateFormat(
				"dd/MM/yyyy").parse("05/11/2008"));
		float temp = (float) -15.0;
		float qte = (float) 15.0;
		
		Cession cess1 = new Cession();
		cess1.setNumero("1");
		
		Boolean catched = false;
		// on test l'insertion avec la banque nulle
		try {
			cessionManager.createObjectManager(cess1, null, type, null, null, 
					null, null, null, statut, null, null, null, null, null, 
					utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		
		// on test l'insertion avec le cessiontype null
		try {
			cessionManager.createObjectManager(cess1, bank, null, null, null, 
					null, null, null, statut, null, null, null, null, null,
					utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		
		// on test l'insertion avec le CessionStatut null
		try {
			cessionManager.createObjectManager(cess1, bank, type, null, null, 
					null, null, null, null, null, null, null, null, null,
					utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		
		// on test l'insertion d'un doublon
		cess1.setNumero("55");
		try {
			cessionManager.createObjectManager(cess1, bank, type, null, null, 
					null, null, null, statut, null, null, null, null, null,
					utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		
		// Test de la validation lors de la création
		try {
			validationInsert(cess1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		
		// On test une insertion valide mais avec une erreur sur les
		// CederObjets
		cess1.setNumero("1");
		CederObjet ceder1 = new CederObjet();
		List<CederObjet> list = new ArrayList<CederObjet>();
		list.add(ceder1);
		try {
			cessionManager.createObjectManager(cess1, bank, type, null, 
					null, null, null, null, statut, null, null, null, null, null,
					utilisateur, list, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
		
		// on teste une insertion avec toutes les assos non obligatories à
		// null
		cessionManager.createObjectManager(cess1, bank, type, null, null, 
				null, null, null, statut, null, null, null, null, null,
				utilisateur, null, null);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		int idCess1 = cess1.getCessionId();
		
		// On test une insertion valide avec toutes les assos remplies
		// et de nouveaux CederObjets
		Cession cess2 = new Cession();
		cess2.setNumero("259");
		cess2.setDemandeDate(dateDemande);
		cess2.setEtudeTitre("TITRE");
		cess2.setDescription("DESC");
		cess2.setValidationDate(dateValidation);
		cess2.setDepartDate(dateDepart);
		cess2.setArriveeDate(dateArrivee);
		cess2.setObservations("OBS");
		cess2.setTemperature(temp);
		cess2.setDestructionDate(dateDestrcution);
		Contrat contrat = contratManager.findByIdManager(1);
		Entite entEchan = entiteDao.findByNom("Echantillon").get(0);
		Entite entDerive = entiteDao.findByNom("ProdDerive").get(0);
		ceder1.setCession(cess2);
		ceder1.setEntite(entEchan);
		ceder1.setObjetId(4);
		ceder1.setQuantite(qte);
		ceder1.setQuantiteUnite(qteU);
		CederObjet ceder2 = new CederObjet();
		ceder2.setCession(cess2);
		ceder2.setEntite(entDerive);
		ceder2.setObjetId(4);
		ceder2.setQuantite(qte);
		ceder2.setQuantiteUnite(qteU);
		list = new ArrayList<CederObjet>();
		list.add(ceder1);
		list.add(ceder2);
		
		cessionManager.createObjectManager(cess2, bank, type, exam, 
				contrat, destinataire, servDest, demandeur, statut, 
				executant, transp, motif, null, null, utilisateur, list, null);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 6);
		assertTrue(getOperationManager()
								.findByObjectManager(cess2).size() == 1);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 8);
		int idCess2 = cess2.getCessionId();
		
		Cession cessTest = cessionManager.findByIdManager(idCess2);
		assertNotNull(cessTest);
		assertTrue(cessTest.getNumero().equals("259"));
		assertTrue(cessTest.getBanque().equals(bank));
		assertTrue(cessTest.getCessionType().equals(type));
		assertTrue(cessTest.getDemandeDate().equals(dateDemande));
		assertTrue(cessTest.getCessionExamen().equals(exam));
		assertTrue(cessTest.getContrat().equals(contrat));
		assertTrue(cessTest.getDestinataire().equals(destinataire));
		assertTrue(cessTest.getServiceDest().equals(servDest));
		assertTrue(cessTest.getEtudeTitre().equals("TITRE"));
		assertTrue(cessTest.getDescription().equals("DESC"));
		assertTrue(cessTest.getDemandeur().equals(demandeur));
		assertTrue(cessTest.getCessionStatut().equals(statut));
		assertTrue(cessTest.getValidationDate().equals(dateValidation));
		assertTrue(cessTest.getExecutant().equals(executant));
		assertTrue(cessTest.getTransporteur().equals(transp));
		assertTrue(cessTest.getDepartDate().equals(dateDepart));
		assertTrue(cessTest.getArriveeDate().equals(dateArrivee));
		assertTrue(cessTest.getObservations().equals("OBS"));
		assertTrue(cessTest.getTemperature().equals(temp));
		assertTrue(cessTest.getDestructionMotif().equals(motif));
		assertTrue(cessTest.getDestructionDate().equals(dateDestrcution));
		CederObjet cederTest = cederObjetManager
			.findByCessionEntiteManager(cessTest, entEchan).get(0);
		assertNotNull(cederTest);
		assertTrue(cederTest.getObjetId() == 4);
		assertTrue(cederTest.getQuantite().equals(qte));
		assertTrue(cederTest.getQuantiteUnite().equals(qteU));

		Cession cessToRemove1 = cessionManager.findByIdManager(idCess1);
		cessionManager.removeObjectManager(cessToRemove1, null, utilisateur, null);
		Cession cessToRemove2 = cessionManager.findByIdManager(idCess2);
		cessionManager.removeObjectManager(cessToRemove2, null, utilisateur, null);
		
		assertTrue(getOperationManager().findByObjectManager(
				cessToRemove1).size() == 0);
		assertTrue(getOperationManager().findByObjectManager(
				cessToRemove2).size() == 0);
		
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cessToRemove1);
		fs.add(cessToRemove2);
		cleanUpFantomes(fs);	
	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	private void updateObjectManagerTest() throws ParseException {
		
		Banque bank = banqueManager.findByIdManager(1);
		CessionType type = cessionTypeDao.findById(1);
		CessionExamen exam = cessionExamenDao.findById(1);
		Collaborateur destinataire = collaborateurDao.findById(1);
		Service servDest = serviceDao.findById(1);
		Collaborateur demandeur = collaborateurDao.findById(2);
		CessionStatut statut = cessionStatutDao.findById(1);
		Collaborateur executant = collaborateurDao.findById(3);
		Transporteur transp = transporteurDao.findById(1);
		DestructionMotif motif = destructionMotifDao.findById(1);
		Utilisateur utilisateur = utilisateurManager.findByIdManager(1);
		Unite qteU = uniteDao.findById(5);
		
		Date dateDemande = new SimpleDateFormat(
				"dd/MM/yyyy").parse("01/11/2008");
		Date dateValidation = new SimpleDateFormat(
				"dd/MM/yyyy").parse("02/11/2008");
		Calendar dateDepart = Calendar.getInstance();
		dateDepart.setTime(new SimpleDateFormat(
				"dd/MM/yyyy").parse("03/11/2008"));
		Calendar dateArrivee = Calendar.getInstance();
		dateArrivee.setTime(new SimpleDateFormat(
				"dd/MM/yyyy").parse("04/11/2008"));
		Calendar dateDestrcution = Calendar.getInstance();
		dateDestrcution.setTime(new SimpleDateFormat(
				"dd/MM/yyyy").parse("05/11/2008"));
		float temp = (float) -15.0;
		float qte = (float) 15.0;
		
		Cession cess = new Cession();
		cess.setNumero("1");
		
		// Première insertion
		cessionManager.createObjectManager(cess, bank, type, null, null, 
				null, null, null, statut, null, null, null, null, null,
				utilisateur, null, null);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		int idCess = cess.getCessionId();
		
		Cession cessUp1 = cessionManager.findByIdManager(idCess);
		Boolean catched = false;
		// on test l'update avec la banque nulle
		try {
			cessionManager.updateObjectManager(cessUp1, null, type, null, null, 
					null, null, null, statut, null, null, null, null, null, 
					null, null, utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		
		// on test l'update avec le cessionType nulle
		try {
			cessionManager.updateObjectManager(cessUp1, bank, null, null, null, 
					null, null, null, statut, null, null, null, null, null,  
					null, null, utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		
		// on test l'update avec le CessionStatut null
		try {
			cessionManager.updateObjectManager(cessUp1, bank, type, null, null, 
					null, null, null, null, null, null, null, null, null, 
					null, null, utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		
		// on test l'update d'un doublon
		cessUp1.setNumero("55");
		try {
			cessionManager.updateObjectManager(cessUp1, bank, type, null, null, 
					null, null, null, statut, null, null, null, null, null,  
					null, null, utilisateur, null, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		
		// Test de la validation lors de l'update
		try {
			validationUpdate(cessUp1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		
		// On test un update valide mais avec une erreur sur les
		// CederObjets
		cessUp1.setNumero("1");
		CederObjet ceder1 = new CederObjet();
		List<CederObjet> list = new ArrayList<CederObjet>();
		list.add(ceder1);
		try {
			cessionManager.updateObjectManager(cessUp1, bank, type, null, 
					null, null, null, null, statut, null, null, null,
					null, null, null, null, utilisateur, list, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
		
		// on teste un update avec toutes les assos non obligatories à
		// null
		cessUp1.setNumero("999");
		cessionManager.updateObjectManager(cessUp1, bank, type, null, 
				null, null, null, null, statut, null, null, null,
				null, null, null, null, utilisateur, null, null);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		
		Cession cessUp2 = cessionManager.findByIdManager(idCess);
		assertTrue(cessUp2.getNumero().equals("999"));
		
		// On test un update valide avec un nouveau ProtocoleExt, un
		// nouveau Contrat et de nouveaux CederObjets
		cessUp2.setNumero("259");
		cessUp2.setDemandeDate(dateDemande);
		cessUp2.setEtudeTitre("TITRE");
		cessUp2.setDescription("DESC");
		cessUp2.setValidationDate(dateValidation);
		cessUp2.setDepartDate(dateDepart);
		cessUp2.setArriveeDate(dateArrivee);
		cessUp2.setObservations("OBS");
		cessUp2.setTemperature(temp);
		cessUp2.setDestructionDate(dateDestrcution);
		Contrat contrat = contratManager.findByIdManager(1);
		Entite entEchan = entiteDao.findByNom("Echantillon").get(0);
		Entite entDerive = entiteDao.findByNom("ProdDerive").get(0);
		ceder1.setCession(cessUp2);
		ceder1.setEntite(entEchan);
		ceder1.setObjetId(4);
		ceder1.setQuantite(qte);
		ceder1.setQuantiteUnite(qteU);
		CederObjet ceder2 = new CederObjet();
		ceder2.setCession(cessUp2);
		ceder2.setEntite(entDerive);
		ceder2.setObjetId(4);
		ceder2.setQuantite(qte);
		ceder2.setQuantiteUnite(qteU);
		list = new ArrayList<CederObjet>();
		list.add(ceder1);
		list.add(ceder2);
		
		cessionManager.updateObjectManager(cessUp2, bank, type, exam,
				contrat, destinataire, servDest, demandeur, statut, 
				executant, transp, motif, null, null, null, null, 
				utilisateur, list, null);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		assertTrue(getOperationManager()
							.findByObjectManager(cessUp2).size() == 3);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 8);
		CederObjetPK idCeder1 = ceder1.getPk();
		CederObjetPK idCeder2 = ceder2.getPk();
		
		Cession cessTest = cessionManager.findByIdManager(idCess);
		assertNotNull(cessTest);
		assertTrue(cessTest.getNumero().equals("259"));
		assertTrue(cessTest.getBanque().equals(bank));
		assertTrue(cessTest.getCessionType().equals(type));
		assertTrue(cessTest.getDemandeDate().equals(dateDemande));
		assertTrue(cessTest.getCessionExamen().equals(exam));
		assertTrue(cessTest.getContrat().equals(contrat));
		assertTrue(cessTest.getDestinataire().equals(destinataire));
		assertTrue(cessTest.getServiceDest().equals(servDest));
		assertTrue(cessTest.getEtudeTitre().equals("TITRE"));
		assertTrue(cessTest.getDescription().equals("DESC"));
		assertTrue(cessTest.getDemandeur().equals(demandeur));
		assertTrue(cessTest.getCessionStatut().equals(statut));
		assertTrue(cessTest.getValidationDate().equals(dateValidation));
		assertTrue(cessTest.getExecutant().equals(executant));
		assertTrue(cessTest.getTransporteur().equals(transp));
		assertTrue(cessTest.getDepartDate().equals(dateDepart));
		assertTrue(cessTest.getArriveeDate().equals(dateArrivee));
		assertTrue(cessTest.getObservations().equals("OBS"));
		assertTrue(cessTest.getTemperature().equals(temp));
		assertTrue(cessTest.getDestructionMotif().equals(motif));
		assertTrue(cessTest.getDestructionDate().equals(dateDestrcution));
		CederObjet cederTest = cederObjetManager
			.findByCessionEntiteManager(cessTest, entEchan).get(0);
		assertNotNull(cederTest);
		assertTrue(cederTest.getObjetId() == 4);
		assertTrue(cederTest.getQuantite().equals(qte));
		assertTrue(cederTest.getQuantiteUnite().equals(qteU));
		
		// On test un update valide avec des CederObjets existants
		Cession cess3 = cessionManager.findByIdManager(idCess);
		cess3.setNumero("387");
		CederObjet cederUp1 = cederObjetManager.findByIdManager(idCeder1);
		CederObjet cederUp2 = cederObjetManager.findByIdManager(idCeder2);
		cederUp1.setQuantite((float) 20);
		cederUp2.setQuantite((float) 20);
		list = new ArrayList<CederObjet>();
		list.add(cederUp1);
		list.add(cederUp2);
		cessionManager.updateObjectManager(cess3, bank, type, null, contrat, 
				null, null, null, statut, null, null, null, null, null,  
				null, null, utilisateur, list, null);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 8);
		
		Cession cessTest2 = cessionManager.findByIdManager(idCess);
		CederObjet cederTest2 = cederObjetManager
			.findByCessionEntiteManager(cessTest2, entEchan).get(0);
		assertNotNull(cederTest2);
		assertTrue(cederTest2.getObjetId() == 4);
		assertTrue(cederTest2.getQuantite() == 20);
		assertTrue(cederTest2.getQuantiteUnite().equals(qteU));
		
		// On teste la suppression d'un CederObjet lors de l'update
		CederObjet cederUp3 = cederObjetManager.findByIdManager(idCeder1);
		cederUp3.setQuantite((float) 0.0);
		list = new ArrayList<CederObjet>();
		list.add(cederUp3);
		cessionManager.updateObjectManager(cessTest2, bank, type, null, 
				contrat, 
				null, null, null, statut, null, null, null, null, null,  
				null, null, utilisateur, list, null);
		Cession cessTest3 = cessionManager.findByIdManager(idCess);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 7);
		assertTrue(cessionManager.getCederObjetsManager(cessTest3).size() == 1);
		
		// On teste la suppression d'un CederObjet lors de l'update
		cessionManager.updateObjectManager(cessTest2, bank, type, null, 
				contrat, null, null, null, statut, null, null, null, null, 
				null, null, null, utilisateur, null, null);
		Cession cessTest4 = cessionManager.findByIdManager(idCess);
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 5);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
		assertTrue(cessionManager.getCederObjetsManager(cessTest4).size() == 0);
		
		CederObjet newLast = new CederObjet();
		newLast.setCession(cessTest4);
		newLast.setEntite(entEchan);
		newLast.setObjetId(4);
		newLast.setQuantite(qte);
		newLast.setQuantiteUnite(qteU);
		cessionManager.updateObjectManager(cessTest2, bank, type, null, 
				contrat, 
				null, null, null, statut, null, null, null, null, null, 
				null, null,
				utilisateur, list, null);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 7);
		
		Cession cessToRemove = cessionManager.findByIdManager(idCess);
		cessionManager.removeObjectManager(cessToRemove, null, utilisateur, null);
		
		assertTrue(getOperationManager().findByObjectManager(
				cessToRemove).size() == 0);
				
		assertTrue(contratManager.findAllObjectsManager().size() == 4);
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
		assertTrue(cederObjetManager.findAllObjectsManager().size() == 6);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cessToRemove);
		cleanUpFantomes(fs);	
		
	}
	
	/**
	 * Teste la methode removeObjectManager. 
	 */
	private void removeObjectManagerTest() {
		cessionManager.removeObjectManager(null, null, null, null);
		assertTrue(cessionManager.findAllObjectsManager().size() == 4);
	}
	
	/**
	 * Test la validation d'une cession lors de sa création.
	 * @param cession Cession à tester.
	 * @throws ParseException 
	 */
	private void validationInsert(Cession cession) 
		throws ParseException {
		Banque banque = banqueManager.findByIdManager(1);
		CessionStatut statut = cessionStatutDao.findById(1);
		CessionType type = cessionTypeDao.findById(1);
		
		boolean catchedInsert = false;
		// On teste une insertion avec un attribut numero non valide
		String[] numeros = new String[]{"", "  ", null, "%$*gd¤¤", 
				createOverLength(100)};
		for (int i = 0; i < numeros.length; i++) {
			catchedInsert = false;
			try {
				cession.setNumero(numeros[i]);
				cessionManager.createObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		cession.setNumero("10");
		
		// On teste une insertion avec un attribut obs non valide
		String[] emptyValues = new String[]{"", "  "};
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				cession.setObservations(emptyValues[i]);
				cessionManager.createObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		cession.setObservations(null);
		
		// On teste une insertion avec un attribut description non valide
		for (int i = 0; i < emptyValues.length; i++) {
			catchedInsert = false;
			try {
				cession.setDescription(emptyValues[i]);
				cessionManager.createObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		cession.setDescription(null);
		
		// On teste une insertion avec un attribut titre non valide
		String[] titreValues = new String[]{"", "  ", "%$*gd¤¤", 
				createOverLength(100)};
		for (int i = 0; i < titreValues.length; i++) {
			catchedInsert = false;
			try {
				cession.setEtudeTitre(titreValues[i]);
				cessionManager.createObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		cession.setEtudeTitre("titre");
		
	}
	
	/**
	 * Test la validation d'une cession lors de son update.
	 * @param cession Cession à tester.
	 * @throws ParseException 
	 */
	private void validationUpdate(Cession cession) 
		throws ParseException {
		Banque banque = banqueManager.findByIdManager(1);
		CessionStatut statut = cessionStatutDao.findById(1);
		CessionType type = cessionTypeDao.findById(1);
		
		boolean catched = false;
		// On teste un update avec un attribut numero non valide
		String[] numeros = new String[]{"", "  ", null, "%$*gd¤¤", 
				createOverLength(100)};
		for (int i = 0; i < numeros.length; i++) {
			catched = false;
			try {
				cession.setNumero(numeros[i]);
				cessionManager.updateObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		cession.setNumero("10");
		
		// On teste un update avec un attribut obs non valide
		String[] emptyValues = new String[]{"", "  "};
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				cession.setObservations(emptyValues[i]);
				cessionManager.updateObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		cession.setObservations(null);
		
		// On teste une insertion avec un attribut description non valide
		for (int i = 0; i < emptyValues.length; i++) {
			catched = false;
			try {
				cession.setDescription(emptyValues[i]);
				cessionManager.updateObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null,
						null, null, null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		cession.setDescription(null);
		
		// On teste un update avec un attribut titre non valide
		String[] titreValues = new String[]{"", "  ", "%$*gd¤¤", 
				createOverLength(100)};
		for (int i = 0; i < titreValues.length; i++) {
			catched = false;
			try {
				cession.setEtudeTitre(titreValues[i]);
				cessionManager.updateObjectManager(cession, banque, type, 
						null, null, null, null, null, statut, null, null, null,
						null, null, null, null, null, null, null);
			} catch (Exception e) {
				if (e.getClass().getSimpleName().equals(
						"ValidationException")) {
					catched = true;
				}
			}
			assertTrue(catched);
		}
		cession.setEtudeTitre("titre");
		
	}
	
	@Test
	public void testDemandeDateCoherence() throws ParseException {
		Cession c = new Cession();
		
		// null validation
		c.setDemandeDate(null);
		Errors errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		// limites inf
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		// limites sup
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertEquals("date.validation.supValidationDate",
									errs.getFieldError().getCode());
		
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setValidationDate(null);
		Calendar depart = Calendar.getInstance();
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("13/07/2010 12:20:05"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertEquals("date.validation.supDepartDateCession",
									errs.getFieldError().getCode());
	
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 10:00:00"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 00:00:00"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setDepartDate(null);
		Calendar arrivee = Calendar.getInstance();
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("13/07/2010 12:20:05"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertEquals("date.validation.supArriveeDateCession",
									errs.getFieldError().getCode());
		
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 10:00:00"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 00:00:00"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setArriveeDate(null);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
										.parse("13/07/2199"));
		errs = CessionValidator.checkDemandeDateCoherence(c);
		assertEquals("date.validation.supDateActuelle",
									errs.getFieldError().getCode());
		
	}
	
	@Test
	public void testValidationDateCoherence() throws ParseException {
		Cession c = new Cession();
		
		// null validation
		c.setValidationDate(null);
		Errors errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		// limites inf
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		// limites inf
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertEquals("date.validation.infDemandeDate",
									errs.getFieldError().getCode());
		
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		// limtes sup
		Calendar depart = Calendar.getInstance();
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("13/07/2010 12:20:05"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertEquals("date.validation.supDepartDateCession",
									errs.getFieldError().getCode());
	
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 10:00:00"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 00:00:00"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setDepartDate(null);
		Calendar arrivee = Calendar.getInstance();
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("13/07/2010 12:20:05"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertEquals("date.validation.supArriveeDateCession",
									errs.getFieldError().getCode());
		
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 10:00:00"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 00:00:00"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setArriveeDate(null);
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
										.parse("13/07/2199"));
		errs = CessionValidator.checkValidationDateCoherence(c);
		assertEquals("date.validation.supDateActuelle",
									errs.getFieldError().getCode());
		
	}
	
	@Test
	public void testDepartDateCoherence() throws ParseException {
		Cession c = new Cession();
		
		// null validation
		c.setDepartDate(null);
		Errors errs = CessionValidator.checkDepartDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		Calendar depart = Calendar.getInstance();
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:20:05"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkDepartDateCoherence(c);

		// limites inf
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertEquals("date.validation.infValidationDate",
										errs.getFieldError().getCode());

		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		c.setValidationDate(null);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertEquals("date.validation.infDemandeDate",
									errs.getFieldError().getCode());
		
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		// limtes sup
		Calendar arrivee = Calendar.getInstance();
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:19:05"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertEquals("date.validation.supArriveeDateCession",
									errs.getFieldError().getCode());
		
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 14:00:00"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:20:05"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setArriveeDate(null);
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy")
													.parse("13/07/2199"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkDepartDateCoherence(c);
		assertEquals("date.validation.supDateActuelle",
									errs.getFieldError().getCode());
		
	}
	
	@Test
	public void testArriveeDateCoherence() throws ParseException {
		Cession c = new Cession();
		
		// null validation
		c.setArriveeDate(null);
		Errors errs = CessionValidator.checkArriveeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		Calendar arrivee = Calendar.getInstance();
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:20:05"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);

		// limites inf
		Calendar depart = Calendar.getInstance();
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:22:05"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertEquals("date.validation.infDepartDateCession",
									errs.getFieldError().getCode());
		
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 08:00:00"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		depart.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:20:05"));
		c.setDepartDate(depart);
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		c.setDepartDate(null);
		
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertEquals("date.validation.infValidationDate",
										errs.getFieldError().getCode());

		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		c.setValidationDate(null);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertEquals("date.validation.infDemandeDate",
									errs.getFieldError().getCode());
		
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		// limites sup	
		arrivee.setTime(new SimpleDateFormat("dd/MM/yyyy")
										.parse("14/07/2199"));
		c.setArriveeDate(arrivee);
		errs = CessionValidator.checkArriveeDateCoherence(c);
		assertEquals("date.validation.supDateActuelle",
									errs.getFieldError().getCode());
	}
	
	@Test
	public void testDestructionDateCoherence() throws ParseException {
		Cession c = new Cession();
		
		// null validation
		c.setDestructionDate(null);
		Errors errs = CessionValidator.checkDestructionDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		
		Calendar destr = Calendar.getInstance();
		destr.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
										.parse("14/07/2010 12:20:05"));
		c.setDestructionDate(destr);
		errs = CessionValidator.checkDestructionDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);

		// limites inf
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkDestructionDateCoherence(c);
		assertEquals("date.validation.infValidationDate",
										errs.getFieldError().getCode());

		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkDestructionDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setValidationDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		c.setValidationDate(null);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("15/07/2010"));
		errs = CessionValidator.checkDestructionDateCoherence(c);
		assertEquals("date.validation.infDemandeDate",
									errs.getFieldError().getCode());
		
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("14/07/2010"));
		errs = CessionValidator.checkDestructionDateCoherence(c);
		assertTrue(errs.getAllErrors().size() == 0);
		c.setDemandeDate(new SimpleDateFormat("dd/MM/yyyy")
												.parse("13/07/2010"));
		
		// limites sup	
		destr.setTime(new SimpleDateFormat("dd/MM/yyyy")
										.parse("14/07/2199"));
		c.setDestructionDate(destr);
		errs = CessionValidator.checkDestructionDateCoherence(c);
		assertEquals("date.validation.supDateActuelle",
									errs.getFieldError().getCode());
	}
	
	@Test
	public void testCreateUpdateCessionWithAnnots() {

		Banque b1 = banqueManager.findByIdManager(1);
		Utilisateur utilisateur = utilisateurManager.findByIdManager(1);
		CessionType cType = cessionTypeDao.findById(2);
		CessionStatut cStatut = cessionStatutDao.findById(1);
		
		TableAnnotation table = new TableAnnotation();
		/*Champs obligatoires*/
		table.setNom("TABLE_TEST");
		Entite e = entiteDao.findByNom("Cession").get(0);
		// references vers banques
		List<Banque> banks = new ArrayList<Banque>();
		banks.add(b1);
		ChampAnnotation c1 = new ChampAnnotation();
		c1.setNom("alpha_table_test");
		c1.setDataType(dataTypeDao.findById(1));
		c1.setOrdre(1);
		//c2.setTableAnnotation(table);
		List<ChampAnnotation> champs = new ArrayList<ChampAnnotation>();
		champs.add(c1); 
		tableAnnotationManager
			.createOrUpdateObjectManager(table, e, null, 
					champs, banks, null, utilisateur, "creation", null, null);
		assertTrue((tableAnnotationManager
				.findByNomLikeManager("TABLE_TEST", true)).size() == 1);
		
		// on insert 1 nouveau patient pour les tests
		Cession cession = new Cession();
		cession.setNumero("666");
		
		List<AnnotationValeur> listAnnots = new ArrayList<AnnotationValeur>();
		List<AnnotationValeur> listDelete = new ArrayList<AnnotationValeur>();
		
		AnnotationValeur alpha1 = new AnnotationValeur();
		alpha1.setChampAnnotation(c1);
		alpha1.setAlphanum("val1");
		alpha1.setBanque(b1);
		listAnnots.add(alpha1);
		
		// teste erreur sur annots -> rollback
		alpha1.setAlphanum("&é**$$$¤¤");
		boolean catched = false;
		try {
			cessionManager
				.createObjectManager(cession, b1, cType, 
						null, null, null, null, null, cStatut, null, 
						null, null, listAnnots, null, utilisateur, null, "/tmp/");
		} catch (ValidationException ve) {
			catched = true;
		}
		assertTrue(catched);
		
		testFindAll();
		
		alpha1.setAlphanum("val1");	
		cessionManager
			.createObjectManager(cession, b1, cType, 
				null, null, null, null, null, cStatut, null, 
				null, null, listAnnots, null, utilisateur, null, "/tmp/");
		
		assertTrue(cessionManager
							.findByNumeroLikeManager("666", true).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c1, cession).get(0)
												.getAlphanum().equals("val1"));
		
		assertTrue(getOperationManager()
				.findByObjectManager(cession).size() == 2);
		assertTrue(getOperationManager()
				.findByObjetIdEntiteAndOpeTypeManager(cession, 
				operationTypeDao.findByNom("Annotation").get(0)).size() == 1);
		
		// on teste une deletion puis insertion nouvelle valeur
		listDelete.add(annotationValeurManager
				.findByChampAndObjetManager(c1, cession).get(0));
		listAnnots.clear();
		AnnotationValeur alpha2 = new AnnotationValeur();
		alpha2.setChampAnnotation(c1);
		alpha2.setAlphanum("val2");
		alpha2.setBanque(b1);
		listAnnots.add(alpha2);
		
		cession.setNumero("676");
		
		cessionManager
			.updateObjectManager(cession, cession.getBanque(), cType, 
				null, null, null, null, null, cStatut, null, null, null, 
				listAnnots, listDelete, null, null, utilisateur, null, "/tmp/");
		
		assertTrue(cessionManager
				.findByNumeroLikeManager("676", true).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c1, cession).size() == 1);
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c1, cession).get(0)
									.getAlphanum().equals("val2"));
		assertTrue(getOperationManager()
				.findByObjectManager(cession).size() == 4);
		assertTrue(getOperationManager()
				.findByObjectManager(cession).get(3).getOperationType().getNom()
				.equals("Annotation"));
		
		// suppression annots
		listDelete.add(annotationValeurManager
				.findByChampAndObjetManager(c1, cession).get(0));
		listAnnots.clear();
		
		cessionManager
		.updateObjectManager(cession, cession.getBanque(), cType, 
				null, null, null, null, null, cStatut, null, 
				null, null, listAnnots, listDelete, null, null, 
				utilisateur, null, "/tmp/");
		
		assertTrue(annotationValeurManager
				.findByChampAndObjetManager(c1, cession).size() == 0);
		
		assertTrue(getOperationManager()
				.findByObjectManager(cession).size() == 6);
		assertTrue(getOperationManager()
				.findByObjectManager(cession).get(5).getOperationType().getNom()
				.equals("Annotation"));
		
		AnnotationValeur alpha3 = new AnnotationValeur();
		alpha3.setChampAnnotation(c1);
		alpha3.setAlphanum("val2");
		alpha3.setBanque(b1);
		listAnnots.add(alpha3);
		listDelete.clear();
				
		cessionManager
			.updateObjectManager(cession, cession.getBanque(), cType, 
				null, null, null, null, null, cStatut, null, 
				null, null, listAnnots, listDelete, null, null, 
				utilisateur, null, "/tmp/");
		
		// Nettoyage
		cessionManager.removeObjectManager(cession, null, utilisateur, null);
		Set<ChampAnnotation> chps = 
				tableAnnotationManager.getChampAnnotationsManager(table);
		tableAnnotationManager
					.removeObjectManager(table, null, utilisateur, null);
		assertTrue(getOperationManager()
								.findByObjectManager(cession).size() == 0);
		assertTrue(annotationValeurManager
										.findAllObjectsManager().size() == 12);
		assertEquals(48, champAnnotationDao.findAll().size());
		assertTrue(tableAnnotationManager.findAllObjectsManager().size() == 11);
		testFindAll();	
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cession); fs.add(table); fs.addAll(chps);
		cleanUpFantomes(fs);
	}
	
	@Test
	public void testGetTypesAndCountsManager() {
		Cession c1 = cessionManager.findByIdManager(1);
		
		Map<String,Number> cts = cessionManager.getTypesAndCountsManager(c1);
		assertTrue(cts.size() == 2);
		assertTrue(cts.get("CELLULES").intValue() == 1);
		assertTrue(cts.get("ARN").intValue() == 1);

		Cession c2 = cessionManager.findByIdManager(2);	
		cts = cessionManager.getTypesAndCountsManager(c2);
		assertTrue(cts.size() == 2);
		assertTrue(cts.get("CELLULES").intValue() == 2);
		assertTrue(cts.get("CULOT SEC").intValue() == 1);
		
		Cession c3 = cessionManager.findByIdManager(3);
		cts = cessionManager.getTypesAndCountsManager(c3);
		assertTrue(cts.isEmpty());
		
		Cession c4 = cessionManager.findByIdManager(4);		
		cts = cessionManager.getTypesAndCountsManager(c4);
		assertTrue(cts.size() == 1);
		assertTrue(cts.get("ADN").intValue() == 1);
		
		cts = cessionManager.getTypesAndCountsManager(null);
		assertTrue(cts.isEmpty());
		
		cts = cessionManager.getTypesAndCountsManager(new Cession());
		assertTrue(cts.isEmpty());

	}
	
	/**
	 * @since 2.1
	 */
	@Test
	public void testFindByNumeroInPlateformeBanqueManager() {
		Plateforme p1 = plateformeDao.findById(1);
		List<Cession> cessions = cessionManager
				.findByNumeroInPlateformeManager("55", p1);
		assertTrue(cessions.size() == 1);
		cessions = cessionManager
				.findByNumeroInPlateformeManager("%", p1);
		assertTrue(cessions.size() == 4);
		cessions = cessionManager
				.findByNumeroInPlateformeManager(null, p1);
		assertTrue(cessions.size() == 0);
		cessions = cessionManager
				.findByNumeroInPlateformeManager("%", null);
		assertTrue(cessions.size() == 0);
	}
	
	/**
	 * @throws ParseException 
	 * @since 2.1
	 */
	@Test
	public void testApplyScanCheckDateManager() throws ParseException {
		Cession c = null;
		Calendar dateS = Calendar.getInstance();
		dateS.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-07-18 09:28:46"));
		cessionManager.applyScanCheckDateManager(c, dateS);
		testFindAll(); // aucune cession supp enreg
		c = new Cession();
		cessionManager.applyScanCheckDateManager(c, dateS);
		testFindAll(); // aucune cession supp enreg
		c = cessionManager.findByIdManager(1);
		cessionManager.applyScanCheckDateManager(c, dateS);
		c = cessionManager.findByIdManager(1);
		assertTrue(cessionManager.findByIdManager(1)
				.getLastScanCheckDate().getTime().equals(dateS.getTime()));
		// clean cession1
		cessionManager.applyScanCheckDateManager(c, null);
		assertNull(cessionManager.findByIdManager(1).getLastScanCheckDate());
		
		testFindAll(); // aucune cession supp enreg
	}
	
//	@Test
//	public void testAddObjectsAndValidateProcedure() {
//		Utilisateur u1 = utilisateurManager.findByIdManager(1);
//		
//		Integer echanNb = echantillonManager.findAllObjectsManager().size();
//		Integer emplNb = emplacementDao.findAll().size();
//		
//		Echantillon e1 = new Echantillon(); // STOCKE EMP=4 BANQUE1
//		e1.setCode("ECHAN1");
//		e1.setQuantite(new Float(100));
//		e1.setQuantiteInit(new Float(100));
//		
//		echantillonManager.createObjectManager(e1, banqueManager.findByIdManager(1), null, null, 
//				objetStatutDao.findById(1), emplacementDao.findById(4), echantillonTypeDao.findById(1), null, 
//				uniteDao.findById(4), null, null, null, null, null, u1, false, null, false);
//		
//		Echantillon e2 = new Echantillon(); // STOCKE EMP=5 BANQUE2
//		e2.setCode("ECHAN2");
//		e2.setQuantite(new Float(100));
//		e2.setQuantiteInit(new Float(100));
//		
//		echantillonManager.createObjectManager(e2, banqueManager.findByIdManager(2), null, null, 
//				objetStatutDao.findById(1), emplacementDao.findById(5), echantillonTypeDao.findById(1), null, 
//				uniteDao.findById(4), null, null, null, null, null, u1, false, null, false);
//		
//		Echantillon e3 = new Echantillon(); // NON STOCKE BANQUE1
//		e3.setCode("ECHAN3");
//		e3.setQuantite(new Float(100));
//		e3.setQuantiteInit(new Float(100));
//		
//		echantillonManager.createObjectManager(e3, banqueManager.findByIdManager(1), null, null, 
//				objetStatutDao.findById(4), null, echantillonTypeDao.findById(1), null, 
//				uniteDao.findById(4), null, null, null, null, null, u1, false, null, false);
//		
//		Echantillon e4 = new Echantillon(); // STOCKE EMP=6 BANQUE1 CODE WILL NOT BE FOUND
//		e4.setCode("ECHAN4");
//		e4.setQuantite(new Float(100));
//		e4.setQuantiteInit(new Float(100));
//		
//		echantillonManager.createObjectManager(e4, banqueManager.findByIdManager(1), null, null, 
//				objetStatutDao.findById(1), emplacementDao.findById(6), echantillonTypeDao.findById(1), null, 
//				uniteDao.findById(4), null, null, null, null, null, u1, false, null, false);
//		
//		// clean up
//		e1.setEmplacement(null);
//		echantillonManager.removeObjectManager(e1, null, utilisateurManager.findByIdManager(1), null);
//		e2.setEmplacement(null);
//		echantillonManager.removeObjectManager(e2, null, utilisateurManager.findByIdManager(1), null);
//		echantillonManager.removeObjectManager(e3, null, utilisateurManager.findByIdManager(1), null);
//		e4.setEmplacement(null);
//		echantillonManager.removeObjectManager(e4, null, utilisateurManager.findByIdManager(1), null);
//		
//		// emplacement id = 6 doit être vide!
//		
//		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
//		fs.add(e1); fs.add(e2); fs.add(e3); fs.add(e4);
//		cleanUpFantomes(fs);
//		
//		assertTrue(echantillonManager.findAllObjectsManager().size() == echanNb);
//		assertTrue(emplacementDao.findAll().size() == emplNb);
//	} 

}
