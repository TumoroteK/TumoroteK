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
package fr.aphp.tumorotek.dao.test.utilisateur;

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilUtilisateurDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateurPK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Classe de test pour le DAO ProfilUtilisateurDao et le bean 
 * du domaine ProfilUtilisateur.
 * 
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ProfilUtilisateurDaoTest extends AbstractDaoTest {
	
	/** Bean DAO. */
	private ProfilUtilisateurDao profilUtilisateurDao;
	/** Bean DAO. */
	private UtilisateurDao utilisateurDao;
	/** Bean DAO. */
	private BanqueDao banqueDao;
	/** Bean DAO. */
	private ProfilDao profilDao;
	
	public ProfilUtilisateurDaoTest() {
		
	}

	public void setProfilUtilisateurDao(ProfilUtilisateurDao pDao) {
		this.profilUtilisateurDao = pDao;
	}

	public void setUtilisateurDao(UtilisateurDao uDao) {
		this.utilisateurDao = uDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setProfilDao(ProfilDao pDao) {
		this.profilDao = pDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<ProfilUtilisateur> liste = profilUtilisateurDao.findAll();
		assertTrue(liste.size() == 8);
	}
	
	/**
	 * Test l'appel de la méthode findById().
	 */
	public void testFindById() {
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		ProfilUtilisateurPK pk = new ProfilUtilisateurPK(p1, u3, b2);
		
		ProfilUtilisateur po = profilUtilisateurDao.findById(pk);
		assertNotNull(po);
		
		pk = new ProfilUtilisateurPK(p1, u4, b1);
		po = profilUtilisateurDao.findById(pk);
		assertNull(po);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	public void testFindByExcludedPK() {
		Profil p1 = profilDao.findById(1);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		ProfilUtilisateurPK pk = new ProfilUtilisateurPK(p1, u3, b2);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 7);
		
		pk = new ProfilUtilisateurPK(p1, u4, b1);
		liste = profilUtilisateurDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 8);
	}
	
	/**
	 * Test l'appel de la méthode findByProfil().
	 * @version 2.1
	 */
	public void testFindByProfil() {
		Profil p1 = profilDao.findById(1);
		Profil p3 = profilDao.findById(3);
		Profil p4 = profilDao.findById(4);
		
		List<ProfilUtilisateur> liste = profilUtilisateurDao.findByProfil(p1, false);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurDao.findByProfil(p1, true);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 3);
		
		liste = profilUtilisateurDao.findByProfil(p3, true);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurDao.findByProfil(p3, false);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByProfil(p4, true);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 3);
		liste = profilUtilisateurDao.findByProfil(p4, false);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getUtilisateur().getUtilisateurId() == 1);
		assertTrue(liste.get(1).getUtilisateur().getUtilisateurId() == 1);
		
		liste = profilUtilisateurDao.findByProfil(null, false);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurDao.findByProfil(null, true);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateur().
	 */
	public void testFindByUtilisateur() {
		Utilisateur u2 = utilisateurDao.findById(2);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
				profilUtilisateurDao.findByUtilisateur(u2, false);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getBanque().getNom().equals("BANQUE1"));
		assertTrue(liste.get(1).getBanque().getNom().equals("BANQUE2"));
		liste = profilUtilisateurDao.findByUtilisateur(u2, true);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getBanque().getNom().equals("BANQUE4"));
		
		liste = profilUtilisateurDao.findByUtilisateur(u4, true);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurDao.findByUtilisateur(u4, false);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateur(null, true);
		assertTrue(liste.size() == 0);
		liste = profilUtilisateurDao.findByUtilisateur(null, false);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * @version 2.1
	 */
	public void testFindByBanque() {
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		
		List<ProfilUtilisateur> liste = 
				profilUtilisateurDao.findByBanque(b1, false);
		assertTrue(liste.size() == 2);
		
		liste = profilUtilisateurDao.findByBanque(b1, true);
		assertTrue(liste.size() == 1);
		
		liste = profilUtilisateurDao.findByBanque(b3, true);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByBanque(b3, false);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByBanque(null, false);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByBanque(b1, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateurProfil().
	 */
	public void testFindByUtilisateurProfil() {
		Profil p2 = profilDao.findById(2);
		Profil p4 = profilDao.findById(4);
		Utilisateur u2 = utilisateurDao.findById(2);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurDao.findByUtilisateurProfil(u2, p2);
		assertTrue(liste.size() == 2);
		
		liste = profilUtilisateurDao.findByUtilisateurProfil(u2, p4);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurProfil(u4, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurProfil(u2, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurProfil(null, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurProfil(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByUtilisateurBanque().
	 */
	public void testFindByUtilisateurBanque() {
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		Utilisateur u2 = utilisateurDao.findById(2);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurDao.findByUtilisateurBanque(u2, b1);
		assertTrue(liste.size() == 1);
		
		liste = profilUtilisateurDao.findByUtilisateurBanque(u2, b3);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurBanque(u4, b1);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurBanque(u2, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurBanque(null, b1);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByUtilisateurBanque(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByBanqueProfil().
	 */
	public void testFindByBanqueProfil() {
		Profil p2 = profilDao.findById(2);
		Profil p4 = profilDao.findById(4);
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		
		List<ProfilUtilisateur> liste = 
			profilUtilisateurDao.findByBanqueProfil(b1, p2);
		assertTrue(liste.size() == 1);
		
		liste = profilUtilisateurDao.findByBanqueProfil(b1, p4);
		assertTrue(liste.size() == 2);
		
		liste = profilUtilisateurDao.findByBanqueProfil(b3, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByBanqueProfil(b1, null);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByBanqueProfil(null, p2);
		assertTrue(liste.size() == 0);
		
		liste = profilUtilisateurDao.findByBanqueProfil(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression 
	 * d'un ProfilUtilisateur.
	 **/
	@Rollback(false)
	public void testCrud() {
		
		ProfilUtilisateur po = new ProfilUtilisateur();
		Profil p = profilDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		
		po.setProfil(p);
		po.setUtilisateur(u4);
		po.setBanque(b3);
		
		// Test de l'insertion
		profilUtilisateurDao.createObject(po);
		assertTrue(profilUtilisateurDao.findAll().size() == 9);	
		
		// Test de la mise à jour
		ProfilUtilisateurPK pk = new ProfilUtilisateurPK();
		pk.setProfil(p);
		pk.setUtilisateur(u4);
		pk.setBanque(b3);
		ProfilUtilisateur poUp = profilUtilisateurDao.findById(pk);
		assertNotNull(poUp);
		assertNotNull(poUp.getProfil());
		assertNotNull(poUp.getBanque());
		assertNotNull(poUp.getUtilisateur());
		
		// Test de la délétion
		profilUtilisateurDao.removeObject(pk);
		assertNull(profilUtilisateurDao.findById(pk));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException {

		ProfilUtilisateur po1 = new ProfilUtilisateur();
		ProfilUtilisateur po2 = new ProfilUtilisateur();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(po1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(po1.equals(po1));
		// 2 objets sont égaux entre eux
		assertTrue(po1.equals(po2));
		assertTrue(po2.equals(po1));
	
		populateClefsToTestEqualsAndHashCode(po1, po2);
			
		//dummy test
		Banque b = new Banque();
		assertFalse(po1.equals(b));		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws ParseException 
	 */
	public void testHashCode() throws ParseException {
		ProfilUtilisateur po1 = new ProfilUtilisateur();
		ProfilUtilisateur po2 = new ProfilUtilisateur();
		ProfilUtilisateur po3 = new ProfilUtilisateur();
		
		assertTrue(po1.hashCode() == po2.hashCode());
		assertTrue(po2.hashCode() == po3.hashCode());
		assertTrue(po3.hashCode() > 0);
		
		//teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(po1, po2);
		
		int hash = po1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(po1.hashCode() == po2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == po1.hashCode());
		assertTrue(hash == po1.hashCode());
		assertTrue(hash == po1.hashCode());
		assertTrue(hash == po1.hashCode());
	}
	
	private void populateClefsToTestEqualsAndHashCode(
			ProfilUtilisateur po1, 
			ProfilUtilisateur po2) throws ParseException {
		
		Profil p = profilDao.findById(1);
		Banque b = banqueDao.findById(3);
		Utilisateur u3 = utilisateurDao.findById(3);
		Utilisateur u4 = utilisateurDao.findById(4);
		ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK(p, u3, b);
		ProfilUtilisateurPK pk2 = new ProfilUtilisateurPK(p, u4, b);
		ProfilUtilisateurPK pk3 = new ProfilUtilisateurPK(p, u3, b);
		ProfilUtilisateurPK[] pks = 
			new ProfilUtilisateurPK[]{null, pk1, pk2, pk3};
				
		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {
								
				po1.setPk(pks[i]);				
				po2.setPk(pks[k]);
				
				if (((i == k) || (i + k == 4))) {
						assertTrue(po1.equals(po2));
						assertTrue(po1.hashCode() 
								== po2.hashCode());
				} else {
					assertFalse(po1.equals(po2));
				}
			}
		}
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Profil p = profilDao.findById(4);
		Banque b = banqueDao.findById(1);
		Utilisateur u = utilisateurDao.findById(1);
		ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK(p, u, b);
		ProfilUtilisateur po1 = profilUtilisateurDao.findById(pk1);
		
		assertTrue(po1.toString().
				equals("{" + po1.getPk().toString() + "}"));
		
		ProfilUtilisateur po2 = new ProfilUtilisateur();
		po2.setPk(null);
		assertTrue(po2.toString().equals("{Empty ProfilUtilisateur}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Profil p = profilDao.findById(4);
		Banque b = banqueDao.findById(1);
		Utilisateur u = utilisateurDao.findById(1);
		ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK(p, u, b);
		ProfilUtilisateur po1 = profilUtilisateurDao.findById(pk1);
		
		ProfilUtilisateur po2 = new ProfilUtilisateur();
		po2 = po1.clone();
		
		assertTrue(po1.equals(po2));
				
		if (po1.getPk() != null) {
			assertTrue(po1.getPk().equals(po2.getPk()));
		} else {
			assertNull(po2.getPk());
		}
		
	}

}
