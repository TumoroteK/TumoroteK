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
package fr.aphp.tumorotek.dao.test.impression;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Classe de test pour le DAO TemplateDao et le bean 
 * du domaine Template.
 * 
 * @author Pierre Ventadour.
 * @version 23/07/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class TemplateDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private TemplateDao templateDao;
	/** Bean Dao. */
	private BanqueDao banqueDao;
	/** Bean Dao. */
	private EntiteDao entiteDao;
	
	public TemplateDaoTest() {
		
	}

	public void setTemplateDao(TemplateDao tDao) {
		this.templateDao = tDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<Template> liste = templateDao.findAll();
		assertTrue(liste.size() == 3);
	}
	
	/**
	 * Test l'appel de la méthode findByBanque().
	 */
	public void testFindByBanque() {
		Banque b1 = banqueDao.findById(1);
		List<Template> liste = templateDao.findByBanque(b1);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getNom().equals("Fiche patient"));
		
		Banque b3 = banqueDao.findById(3);
		liste = templateDao.findByBanque(b3);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByBanque(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByBanqueEntite().
	 */
	public void testFindByBanqueEntite() {
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		Entite e1 = entiteDao.findById(1);
		Entite e3 = entiteDao.findById(3);
		List<Template> liste = templateDao.findByBanqueEntite(b1, e1);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("Fiche patient"));
		
		liste = templateDao.findByBanqueEntite(b1, e3);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByBanqueEntite(b3, e1);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByBanqueEntite(null, e1);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByBanqueEntite(b1, null);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByBanqueEntite(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		Banque b1 = banqueDao.findById(1);
		Banque b3 = banqueDao.findById(3);
		
		List<Template> liste = templateDao.findByExcludedId(b1, 1);
		assertTrue(liste.size() == 1);
		
		liste = templateDao.findByExcludedId(b1, 15);
		assertTrue(liste.size() == 2);
		
		liste = templateDao.findByExcludedId(b3, 1);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByExcludedId(null, 1);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByExcludedId(null, 15);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByExcludedId(b1, null);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByExcludedId(b3, null);
		assertTrue(liste.size() == 0);
		
		liste = templateDao.findByExcludedId(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un Template.
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	public void testCrud() throws Exception {
		
		Banque b = banqueDao.findById(1);
		Entite e = entiteDao.findById(2);
		Template t = new Template();
		String nom = "nom";
		String nomUp = "UPPP";
		
		t.setBanque(b);
		t.setEntite(e);
		t.setNom(nom);
		t.setDescription("DESC");
		t.setEnTete("TETE");
		t.setPiedPage("PIED");
		
		// Test de l'insertion
		templateDao.createObject(t);
		assertEquals(new Integer(4), t.getTemplateId());
		
		// Test de la mise à jour
		Template t2 = templateDao.findById(new Integer(4));
		assertNotNull(t2);
		assertNotNull(t2.getBanque());
		assertNotNull(t2.getEntite());
		assertTrue(t2.getNom().equals(nom));
		assertTrue(t2.getDescription().equals("DESC"));
		assertTrue(t2.getEnTete().equals("TETE"));
		assertTrue(t2.getPiedPage().equals("PIED"));

		t2.setNom(nomUp);
		templateDao.updateObject(t2);
		assertTrue(templateDao.findById(new Integer(4)).
				getNom().equals(nomUp));
		
		// Test de la délétion
		templateDao.removeObject(new Integer(4));
		assertNull(templateDao.findById(new Integer(4)));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "nom";
		String nom2 = "nom2";
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		Template t1 = new Template();
		Template t2 = new Template();
		t1.setNom(nom);
		t1.setBanque(b1);
		t2.setNom(nom);
		t2.setBanque(b1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(t1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(t1.equals(t1));
		// 2 objets sont égaux entre eux
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		
		t1.setBanque(null);
		t1.setNom(null);
		t2.setBanque(null);
		t2.setNom(null);
		assertTrue(t1.equals(t2));
		t2.setNom(nom);
		assertFalse(t1.equals(t2));
		t1.setNom(nom);
		assertTrue(t1.equals(t2));
		t2.setNom(nom2);
		assertFalse(t1.equals(t2));
		t2.setNom(null);
		assertFalse(t1.equals(t2));
		t2.setBanque(b1);
		assertFalse(t1.equals(t2));
		
		t1.setBanque(b1);
		t1.setNom(null);
		t2.setNom(null);
		t2.setBanque(b1);
		assertTrue(t1.equals(t2));
		t2.setBanque(b2);
		assertFalse(t1.equals(t2));
		t2.setNom(nom);
		assertFalse(t1.equals(t2));
		
		// Vérification de la différenciation de 2 objets
		t1.setNom(nom);
		assertFalse(t1.equals(t2));
		t2.setNom(nom2);
		t2.setBanque(b1);
		assertFalse(t1.equals(t2));
		

		Categorie c3 = new Categorie();
		assertFalse(t1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "nom";
		String nom2 = "nom2";
		Banque b1 = banqueDao.findById(1);
		Banque b2 = banqueDao.findById(2);
		Template t1 = new Template();
		Template t2 = new Template();
		//null
		assertTrue(t1.hashCode() == t2.hashCode());
		
		//Nom
		t2.setNom(nom);
		assertFalse(t1.hashCode() == t2.hashCode());
		t1.setNom(nom2);
		assertFalse(t1.hashCode() == t2.hashCode());
		t1.setNom(nom);
		assertTrue(t1.hashCode() == t2.hashCode());
		
		//ProtocoleType
		t2.setBanque(b1);
		assertFalse(t1.hashCode() == t2.hashCode());
		t1.setBanque(b2);
		assertFalse(t1.hashCode() == t2.hashCode());
		t1.setBanque(b1);
		assertTrue(t1.hashCode() == t2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = t1.hashCode();
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Template t1 = templateDao.findById(1);
		assertTrue(t1.toString().
				equals("{" + t1.getNom() + "}"));
		
		Template t2 = new Template();
		assertTrue(t2.toString().equals("{Empty Template}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Template t1 = templateDao.findById(1);
		Template t2 = new Template();
		t2 = t1.clone();
		
		assertTrue(t1.equals(t2));
		
		if (t1.getTemplateId() != null) {
			assertTrue(t1.getTemplateId() == t2.getTemplateId());
		} else {
			assertNull(t2.getTemplateId());
		}
		
		if (t1.getBanque() != null) {
			assertTrue(t1.getBanque().equals(t2.getBanque()));
		} else {
			assertNull(t2.getBanque());
		}
		
		if (t1.getNom() != null) {
			assertTrue(t1.getNom().equals(t2.getNom()));
		} else {
			assertNull(t2.getNom());
		}
		
		if (t1.getEntite() != null) {
			assertTrue(t1.getEntite().equals(t2.getEntite()));
		} else {
			assertNull(t2.getEntite());
		}
		
		if (t1.getDescription() != null) {
			assertTrue(t1.getDescription().equals(t2.getDescription()));
		} else {
			assertNull(t2.getDescription());
		}
		
		if (t1.getEnTete() != null) {
			assertTrue(t1.getEnTete().equals(t2.getEnTete()));
		} else {
			assertNull(t2.getEnTete());
		}
		
		if (t1.getPiedPage() != null) {
			assertTrue(t1.getPiedPage().equals(t2.getPiedPage()));
		} else {
			assertNull(t2.getPiedPage());
		}
	}
	
}
