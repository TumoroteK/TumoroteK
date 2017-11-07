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
package fr.aphp.tumorotek.manager.test.utilisateur;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.utilisateur.ProfilUtilisateurManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateurPK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Classe de test pour le manager ProfilUtilisateurManager.
 * Classe créée le 19/05/2010.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ProfilUtilisateurManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private ProfilUtilisateurManager profilUtilisateurManager;
	@Autowired
	private UtilisateurDao utilisateurDao;
	@Autowired
	private BanqueDao banqueDao;
	@Autowired
	private ProfilDao profilDao;
	
	public ProfilUtilisateurManagerTest() {	
	}

	
	@Test
	public void testFindAllObjectsManager() {
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findAllObjectsManager();
		assertTrue(liste.size() == 8);
	}
	
	/**
	 * Test l'appel de la méthode findByIdManager().
	 */
	@Test
	public void testFindByIdManager() {
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		ProfilUtilisateurPK pk = new ProfilUtilisateurPK(p1, u3, b2);
		
		ProfilUtilisateur po = profilUtilisateurManager.findByIdManager(pk);
		assertNotNull(po);
		
		pk = new ProfilUtilisateurPK(p1, u4, b1);
		po = profilUtilisateurManager.findByIdManager(pk);
		assertNull(po);
		
		po = profilUtilisateurManager.findByIdManager(null);
		assertNull(po);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedPKManager().
	 */
	@Test
	public void testFindByExcludedPKManager() {
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		ProfilUtilisateurPK pk = new ProfilUtilisateurPK(p1, u3, b2);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByExcludedPKManager(pk);
		assertTrue(liste.size() == 7);
		
		pk = new ProfilUtilisateurPK(p1, u4, b1);
		liste = profilUtilisateurManager.findByExcludedPKManager(pk);
		assertTrue(liste.size() == 8);
		
		liste = profilUtilisateurManager.findByExcludedPKManager(null);
		assertTrue(liste.size() == 8);
	}
	
	/**
	 * Test l'appel de la méthode findByProfilManager().
	 * @version 2.1
	 */
	@Test
	public void testFindByProfilManager() {
		Profil p1 = profilDao.findById(1);
		Profil p3 = profilDao.findById(3);
		Profil p4 = profilDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByProfilManager(p1, true);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 3);
		liste = profilUtilisateurManager.findByProfilManager(p1, false);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByProfilManager(p1, null);
		assertTrue(liste.size() == 1);	
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 3);
		
		liste = profilUtilisateurManager.findByProfilManager(p3, true);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByProfilManager(p3, false);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByProfilManager(p3, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByProfilManager(p4, true);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 3);
		liste = profilUtilisateurManager.findByProfilManager(p4, false);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 1);
		assertTrue(liste.get(1).getUtilisateur().getUtilisateurId() == 1);
		liste = profilUtilisateurManager.findByProfilManager(p4, null);
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 1);
		assertTrue(liste.get(1).getUtilisateur().getUtilisateurId() == 1);
		assertTrue(liste.get(2).getUtilisateur().getUtilisateurId() == 3);
		
		liste = profilUtilisateurManager.findByProfilManager(null, false);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByProfilManager(null, true);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByProfilManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateurManager().
	 */
	@Test
	public void testFindByUtilisateurManager() {
		Utilisateur u2 = utilisateurDao.findById(2);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByUtilisateurManager(u2, false);
		assertTrue(liste.size() == 2);
		liste = profilUtilisateurManager.findByUtilisateurManager(u2, true);
		assertTrue(liste.size() == 1);
		liste =  profilUtilisateurManager.findByUtilisateurManager(u2, null);
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getBanque().getNom().equals("BANQUE1"));
		assertTrue(liste.get(1).getBanque().getNom().equals("BANQUE2"));
		assertTrue(liste.get(2).getBanque().getNom().equals("BANQUE4"));
		
		liste = profilUtilisateurManager.findByUtilisateurManager(u4, true);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByUtilisateurManager(u4, false);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurManager.findByUtilisateurManager(u4, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByUtilisateurManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * @version 2.1
	 */
	@Test
	public void testFindByBanqueManager() {
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByBanqueManager(b1, false);
		assertTrue(liste.size() == 2);
		
		liste = profilUtilisateurManager.findByBanqueManager(b1, true);
		assertTrue(liste.size() == 1);
		
		liste = profilUtilisateurManager.findByBanqueManager(b1, null);
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 1);
		assertTrue(liste.get(1).getUtilisateur().getUtilisateurId() == 2);
		assertTrue(liste.get(2).getUtilisateur().getUtilisateurId() == 3);
		
		liste = profilUtilisateurManager.findByBanqueManager(b3, false);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByBanqueManager(b3, true);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByBanqueManager(b3, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByBanqueManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateurProfilManager().
	 */
	@Test
	public void testFindByUtilisateurProfilManager() {
		Profil p2 = profilDao.findById(2);
		Profil p4 = profilDao.findById(4);
		Utilisateur u2 = utilisateurDao.findById(2);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByUtilisateurProfilManager(u2, p2);
		assertTrue(liste.size() == 2);
		
		liste = profilUtilisateurManager.findByUtilisateurProfilManager(u2, p4);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByUtilisateurProfilManager(u4, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager
			.findByUtilisateurProfilManager(u2, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager
			.findByUtilisateurProfilManager(null, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager
			.findByUtilisateurProfilManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateurBanqueManager().
	 */
	@Test
	public void testFindByUtilisateurBanqueManager() {
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		Utilisateur u2 = utilisateurDao.findById(2);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByUtilisateurBanqueManager(u2, b1);
		assertTrue(liste.size() == 1);
		
		liste = profilUtilisateurManager.findByUtilisateurBanqueManager(u2, b3);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByUtilisateurBanqueManager(u4, b1);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager
			.findByUtilisateurBanqueManager(u2, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager
			.findByUtilisateurBanqueManager(null, b1);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager
			.findByUtilisateurBanqueManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByBanqueProfilManager().
	 */
	@Test
	public void testFindByBanqueProfilManager() {
		Profil p2 = profilDao.findById(2);
		Profil p4 = profilDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurManager.findByBanqueProfilManager(b1, p2);
		assertTrue(liste.size() == 1);
		
		liste = profilUtilisateurManager.findByBanqueProfilManager(b1, p4);
		assertTrue(liste.size() == 2);
		
		liste = profilUtilisateurManager.findByBanqueProfilManager(b3, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByBanqueProfilManager(b1, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByBanqueProfilManager(null, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurManager.findByBanqueProfilManager(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test la méthode findDoublonManager.
	 */
	@Test
	public void testFindDoublonManager() {
		
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		
		assertTrue(profilUtilisateurManager.findDoublonManager(u3, b2, p1));
		assertFalse(profilUtilisateurManager.findDoublonManager(u4, b1, p1));
		
		assertFalse(profilUtilisateurManager.findDoublonManager(null, b1, p1));
		assertFalse(profilUtilisateurManager.findDoublonManager(u3, null, p1));
		assertFalse(profilUtilisateurManager.findDoublonManager(u3, b1, null));
		
	}
	
	/**
	 * Test la méthode validateObjectManager.
	 */
	@Test
	public void testValidateObjectManager() {
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		
		Boolean catched = false;
		// on test l'insertion avec le profil null
		try {
			profilUtilisateurManager.validateObjectManager(
					u3, b1, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		
		// on test l'insertion avec l'utilisateur null
		try {
			profilUtilisateurManager.validateObjectManager(
					null, b1, p1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;

		// on test l'insertion avec la banque null
		try {
			profilUtilisateurManager.validateObjectManager(
					u3, null, p1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		
		// On teste l'insertion d'un doublon pour vérifier que l'exception
		// est lancée
		try {
			profilUtilisateurManager.validateObjectManager(
					u3, b2, p1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
	}
	

	@Test
	public void testCrud() {
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		
		ProfilUtilisateur pu1 = new ProfilUtilisateur();
		
		Boolean catched = false;
		// on test l'insertion avec le profil null
		try {
			profilUtilisateurManager.createObjectManager(pu1,
					u4, b1, null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 8);
		catched = false;
		
		// on test l'insertion avec l'utilisateur null
		try {
			profilUtilisateurManager.createObjectManager(pu1,
					null, b1, p1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;

		// on test l'insertion avec la banque null
		try {
			profilUtilisateurManager.createObjectManager(pu1,
					u4, null, p1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"RequiredObjectIsNullException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 8);
		catched = false;
		
		// On teste l'insertion d'un doublon pour vérifier que l'exception
		// est lancée
		try {
			profilUtilisateurManager.createObjectManager(pu1,
					u3, b2, p1);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 8);
		catched = false;
		
		// test d'une insertion valide
		profilUtilisateurManager.createObjectManager(pu1,
				u4, b1, p1);
		ProfilUtilisateurPK pk = pu1.getPk();
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 9);
		
		ProfilUtilisateur puTest = profilUtilisateurManager.findByIdManager(pk);
		assertNotNull(puTest);
		assertTrue(puTest.getProfil().equals(p1));
		assertTrue(puTest.getUtilisateur().equals(u4));
		assertTrue(puTest.getBanque().equals(b1));
		
		// suppression du profilUtilisateur ajouté
		profilUtilisateurManager.removeObjectManager(puTest);
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 8);
		
		// suppression d'un profilUtilisateur null
		profilUtilisateurManager.removeObjectManager(null);
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 8);
		
		// suppression d'un profilUtilisateur inexistant
		profilUtilisateurManager.removeObjectManager(puTest);
		assertTrue(profilUtilisateurManager
				.findAllObjectsManager().size() == 8);
	}
	
}
