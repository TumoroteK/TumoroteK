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
package fr.aphp.tumorotek.dao.test.imprimante;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.imprimante.ModeleType;

/**
 * 
 * Classe de test pour le DAO ModeleDao et le bean du domaine Modele.
 * 
 * @author Pierre Ventadour.
 * @version 18/03/2011
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ModeleDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private ModeleDao modeleDao;
	/** Bean Dao. */
	private ModeleTypeDao modeleTypeDao;
	/** Bean Dao. */
	private PlateformeDao plateformeDao;
	
	public ModeleDaoTest() {
		
	}

	public void setModeleDao(ModeleDao mDao) {
		this.modeleDao = mDao;
	}

	public void setModeleTypeDao(ModeleTypeDao mDao) {
		this.modeleTypeDao = mDao;
	}

	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<Modele> liste = modeleDao.findAll();
		assertTrue(liste.size() == 3);
	}
	
	/**
	 * Test l'appel de la méthode findByPlateforme().
	 */
	public void testFindByPlateforme() {
		Plateforme pf1 = plateformeDao.findById(1);
		List<Modele> liste = modeleDao.findByPlateforme(pf1);
		assertTrue(liste.size() == 3);
		
		Plateforme pf2 = plateformeDao.findById(2);
		liste = modeleDao.findByPlateforme(pf2);
		assertTrue(liste.size() == 0);
		
		liste = modeleDao.findByPlateforme(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByNomAndPlateforme().
	 */
	public void testFindByNomAndPlateforme() {
		Plateforme pf1 = plateformeDao.findById(1);
		List<Modele> liste = modeleDao.findByNomAndPlateforme("NBT", pf1);
		assertTrue(liste.size() == 1);
		
		liste = modeleDao.findByNomAndPlateforme("qscc", pf1);
		assertTrue(liste.size() == 0);
		
		Plateforme pf2 = plateformeDao.findById(2);
		liste = modeleDao.findByNomAndPlateforme("NBT", pf2);
		assertTrue(liste.size() == 0);
		
		liste = modeleDao.findByNomAndPlateforme("NBT", null);
		assertTrue(liste.size() == 0);
		
		liste = modeleDao.findByNomAndPlateforme(null, pf2);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<Modele> liste = modeleDao.findByExcludedId(1);
		assertTrue(liste.size() == 2);
		
		liste = modeleDao.findByExcludedId(100);
		assertTrue(liste.size() == 3);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un modèle.
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	public void testCrud() throws Exception {
		
		ModeleType mt = modeleTypeDao.findById(1);
		Plateforme pf = plateformeDao.findById(1);
		Modele m1 = new Modele();
		m1.setNom("TEST");
		m1.setModeleType(mt);
		m1.setPlateforme(pf);
		m1.setIsDefault(true);
		m1.setTexteLibre("TEXTE");
		m1.setIsQRCode(false);
		
		// Test de l'insertion
		modeleDao.createObject(m1);
		assertEquals(new Integer(4), m1.getModeleId());
		
		Modele m2 = modeleDao.findById(new Integer(4));
		// Vérification des données entrées dans la base
		assertNotNull(m2);
		assertNotNull(m2.getPlateforme());
		assertNotNull(m2.getModeleType());
		assertTrue(m2.getNom().equals("TEST"));
		assertTrue(m2.getIsDefault());
		assertTrue(m2.getTexteLibre().equals("TEXTE"));
		assertFalse(m2.getIsQRCode());
		
		// Test de la mise à jour
		m2.setNom("UP");
		m2.setIsQRCode(true);
		modeleDao.updateObject(m2);
		assertTrue(modeleDao.findById(
				new Integer(4)).getNom().equals("UP"));
		assertTrue(m2.getIsQRCode());
		
		// Test de la délétion
		modeleDao.removeObject(new Integer(4));
		assertNull(modeleDao.findById(new Integer(4)));
		
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "imp1";
		String nom2 = "imp2";
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		Modele m1 = new Modele();
		Modele m2 = new Modele();
		m1.setNom(nom);
		m1.setPlateforme(pf1);
		m2.setNom(nom);
		m2.setPlateforme(pf1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(m1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(m1.equals(m1));
		// 2 objets sont égaux entre eux
		assertTrue(m1.equals(m2));
		assertTrue(m2.equals(m1));
		
		m1.setPlateforme(null);
		m1.setNom(null);
		m2.setPlateforme(null);
		m2.setNom(null);
		assertTrue(m1.equals(m2));
		m2.setNom(nom);
		assertFalse(m1.equals(m2));
		m1.setNom(nom);
		assertTrue(m1.equals(m2));
		m2.setNom(nom2);
		assertFalse(m1.equals(m2));
		m2.setNom(null);
		assertFalse(m1.equals(m2));
		m2.setPlateforme(pf1);
		assertFalse(m1.equals(m2));
		
		m1.setPlateforme(pf1);
		m1.setNom(null);
		m2.setNom(null);
		m2.setPlateforme(pf1);
		assertTrue(m1.equals(m2));
		m2.setPlateforme(pf2);
		assertFalse(m1.equals(m2));
		m2.setNom(nom);
		assertFalse(m1.equals(m2));
		
		// Vérification de la différenciation de 2 objets
		m1.setNom(nom);
		assertFalse(m1.equals(m2));
		m2.setNom(nom2);
		m2.setPlateforme(pf1);
		assertFalse(m1.equals(m2));
		

		Categorie c3 = new Categorie();
		assertFalse(m1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "imp1";
		String nom2 = "imp2";
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		Modele m1 = new Modele();
		Modele m2 = new Modele();
		//null
		assertTrue(m1.hashCode() == m2.hashCode());
		
		//Nom
		m2.setNom(nom);
		assertFalse(m1.hashCode() == m2.hashCode());
		m1.setNom(nom2);
		assertFalse(m1.hashCode() == m2.hashCode());
		m1.setNom(nom);
		assertTrue(m1.hashCode() == m2.hashCode());
		
		//ProtocoleType
		m2.setPlateforme(pf1);
		assertFalse(m1.hashCode() == m2.hashCode());
		m1.setPlateforme(pf2);
		assertFalse(m1.hashCode() == m2.hashCode());
		m1.setPlateforme(pf1);
		assertTrue(m1.hashCode() == m2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = m1.hashCode();
		assertTrue(hash == m1.hashCode());
		assertTrue(hash == m1.hashCode());
		assertTrue(hash == m1.hashCode());
		assertTrue(hash == m1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Modele m1 = modeleDao.findById(1);
		assertTrue(m1.toString().
				equals("{" + m1.getNom() + ", " 
				+ m1.getPlateforme().getNom() + "(Plateforme)}"));
		
		Modele m2 = new Modele();
		assertTrue(m2.toString().equals("{Empty Modele}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Modele m1 = modeleDao.findById(1);
		Modele m2 = new Modele();
		m2 = m1.clone();
		
		assertTrue(m1.equals(m2));
		
		if (m1.getModeleId() != null) {
			assertTrue(m1.getModeleId() == m2.getModeleId());
		} else {
			assertNull(m2.getModeleId());
		}
		
		if (m1.getNom() != null) {
			assertTrue(m1.getNom().equals(m2.getNom()));
		} else {
			assertNull(m2.getNom());
		}
		
		if (m1.getIsDefault() != null) {
			assertTrue(m1.getIsDefault() == m2.getIsDefault());
		} else {
			assertNull(m2.getIsDefault());
		}
		
		if (m1.getTexteLibre() != null) {
			assertTrue(m1.getTexteLibre().equals(m2.getTexteLibre()));
		} else {
			assertNull(m2.getTexteLibre());
		}
		
		if (m1.getModeleType() != null) {
			assertTrue(m1.getModeleType().equals(m2.getModeleType()));
		} else {
			assertNull(m2.getModeleType());
		}
		
		if (m1.getPlateforme() != null) {
			assertTrue(m1.getPlateforme().equals(m2.getPlateforme()));
		} else {
			assertNull(m2.getPlateforme());
		}
		
		if (m1.getAffectationImprimantes() != null) {
			assertTrue(m1.getAffectationImprimantes()
					.equals(m2.getAffectationImprimantes()));
		} else {
			assertNull(m2.getAffectationImprimantes());
		}
	}
	
}
