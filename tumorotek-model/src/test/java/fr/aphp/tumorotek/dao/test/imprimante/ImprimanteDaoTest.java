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
import fr.aphp.tumorotek.dao.imprimante.ImprimanteApiDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.ImprimanteApi;

/**
 * 
 * Classe de test pour le DAO ImprimanteDao et le bean du domaine Imprimante.
 * 
 * @author Pierre Ventadour.
 * @version 2.0.11
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ImprimanteDaoTest extends AbstractDaoTest {
	
	private ImprimanteDao imprimanteDao;
	private ImprimanteApiDao imprimanteApiDao;
	private PlateformeDao plateformeDao;
	
	public ImprimanteDaoTest() {
		
	}

	public void setImprimanteDao(ImprimanteDao iDao) {
		this.imprimanteDao = iDao;
	}

	public void setImprimanteApiDao(ImprimanteApiDao iDao) {
		this.imprimanteApiDao = iDao;
	}

	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<Imprimante> liste = imprimanteDao.findAll();
		assertTrue(liste.size() == 2);
	}
	
	/**
	 * Test l'appel de la méthode findByPlateforme().
	 */
	public void testFindByPlateforme() {
		Plateforme pf1 = plateformeDao.findById(1);
		List<Imprimante> liste = imprimanteDao.findByPlateforme(pf1);
		assertTrue(liste.size() == 2);
		
		Plateforme pf2 = plateformeDao.findById(2);
		liste = imprimanteDao.findByPlateforme(pf2);
		assertTrue(liste.size() == 0);
		
		liste = imprimanteDao.findByPlateforme(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByNomAndPlateforme().
	 */
	public void testFindByNomAndPlateforme() {
		Plateforme pf1 = plateformeDao.findById(1);
		List<Imprimante> liste = imprimanteDao
			.findByNomAndPlateforme("PDF", pf1);
		assertTrue(liste.size() == 1);
		
		liste = imprimanteDao.findByNomAndPlateforme("raa", pf1);
		assertTrue(liste.size() == 0);
		
		Plateforme pf2 = plateformeDao.findById(2);
		liste = imprimanteDao.findByNomAndPlateforme("PDF", pf2);
		assertTrue(liste.size() == 0);
		
		liste = imprimanteDao.findByNomAndPlateforme(null, pf2);
		assertTrue(liste.size() == 0);
		
		liste = imprimanteDao.findByNomAndPlateforme("PDF", null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByPlateformeSelectNom().
	 */
	public void testFindByPlateformeSelectNom() {
		Plateforme pf1 = plateformeDao.findById(1);
		List<String> liste = imprimanteDao.findByPlateformeSelectNom(pf1);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).equals("PDF"));
		
		Plateforme pf2 = plateformeDao.findById(2);
		liste = imprimanteDao.findByPlateformeSelectNom(pf2);
		assertTrue(liste.size() == 0);
		
		liste = imprimanteDao.findByPlateformeSelectNom(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<Imprimante> liste = imprimanteDao.findByExcludedId(1);
		assertTrue(liste.size() == 1);
		
		liste = imprimanteDao.findByExcludedId(100);
		assertTrue(liste.size() == 2);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'une imprimante.
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	public void testCrud() throws Exception {
		
		ImprimanteApi ia = imprimanteApiDao.findById(1);
		Plateforme pf = plateformeDao.findById(1);
		Imprimante i1 = new Imprimante();
		i1.setNom("TEST");
		i1.setImprimanteApi(ia);
		i1.setPlateforme(pf);
		i1.setAbscisse(50);
		i1.setOrdonnee(55);
		i1.setLargeur(10);
		i1.setLongueur(20);
		i1.setOrientation(1);
		i1.setMbioPrinter(3);
		i1.setAdresseIp("12.12.12.0");
		i1.setResolution(244);
		i1.setPort(12012);
		
		// Test de l'insertion
		imprimanteDao.createObject(i1);
		assertEquals(new Integer(3), i1.getImprimanteId());
		
		Imprimante i2 = imprimanteDao.findById(new Integer(3));
		// Vérification des données entrées dans la base
		assertNotNull(i2);
		assertNotNull(i2.getPlateforme());
		assertNotNull(i2.getImprimanteApi());
		assertTrue(i2.getNom().equals("TEST"));
		assertTrue(i2.getAbscisse() == 50);
		assertTrue(i2.getOrdonnee() == 55);
		assertTrue(i2.getLargeur() == 10);
		assertTrue(i2.getLongueur() == 20);
		assertTrue(i2.getOrientation() == 1);
		assertTrue(i2.getMbioPrinter() == 3);
		assertTrue(i2.getAdresseIp().equals("12.12.12.0"));
		assertTrue(i2.getResolution() == 244);
		assertTrue(i2.getPort() == 12012);

		
		// Test de la mise à jour
		i2.setNom("UP");
		imprimanteDao.updateObject(i2);
		assertTrue(imprimanteDao.findById(
				new Integer(3)).getNom().equals("UP"));
		
		// Test de la délétion
		imprimanteDao.removeObject(new Integer(3));
		assertNull(imprimanteDao.findById(new Integer(3)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "imp1";
		String nom2 = "imp2";
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		Imprimante i1 = new Imprimante();
		Imprimante i2 = new Imprimante();
		i1.setNom(nom);
		i1.setPlateforme(pf1);
		i2.setNom(nom);
		i2.setPlateforme(pf1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(i1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(i1.equals(i1));
		// 2 objets sont égaux entre eux
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));
		
		i1.setPlateforme(null);
		i1.setNom(null);
		i2.setPlateforme(null);
		i2.setNom(null);
		assertTrue(i1.equals(i2));
		i2.setNom(nom);
		assertFalse(i1.equals(i2));
		i1.setNom(nom);
		assertTrue(i1.equals(i2));
		i2.setNom(nom2);
		assertFalse(i1.equals(i2));
		i2.setNom(null);
		assertFalse(i1.equals(i2));
		i2.setPlateforme(pf1);
		assertFalse(i1.equals(i2));
		
		i1.setPlateforme(pf1);
		i1.setNom(null);
		i2.setNom(null);
		i2.setPlateforme(pf1);
		assertTrue(i1.equals(i2));
		i2.setPlateforme(pf2);
		assertFalse(i1.equals(i2));
		i2.setNom(nom);
		assertFalse(i1.equals(i2));
		
		// Vérification de la différenciation de 2 objets
		i1.setNom(nom);
		assertFalse(i1.equals(i2));
		i2.setNom(nom2);
		i2.setPlateforme(pf1);
		assertFalse(i1.equals(i2));
		

		Categorie c3 = new Categorie();
		assertFalse(i1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "imp1";
		String nom2 = "imp2";
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		Imprimante i1 = new Imprimante();
		Imprimante i2 = new Imprimante();
		//null
		assertTrue(i1.hashCode() == i2.hashCode());
		
		//Nom
		i2.setNom(nom);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setNom(nom2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setNom(nom);
		assertTrue(i1.hashCode() == i2.hashCode());
		
		//ProtocoleType
		i2.setPlateforme(pf1);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setPlateforme(pf2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setPlateforme(pf1);
		assertTrue(i1.hashCode() == i2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = i1.hashCode();
		assertTrue(hash == i1.hashCode());
		assertTrue(hash == i1.hashCode());
		assertTrue(hash == i1.hashCode());
		assertTrue(hash == i1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Imprimante i1 = imprimanteDao.findById(1);
		assertTrue(i1.toString().
				equals("{" + i1.getNom() + ", " 
				+ i1.getPlateforme().getNom() + "(Plateforme)}"));
		
		Imprimante i2 = new Imprimante();
		assertTrue(i2.toString().equals("{Empty Imprimante}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		Imprimante i1 = imprimanteDao.findById(1);
		Imprimante i2 = new Imprimante();
		i2 = i1.clone();
		
		assertTrue(i1.equals(i2));
		
		if (i1.getImprimanteId() != null) {
			assertTrue(i1.getImprimanteId() == i2.getImprimanteId());
		} else {
			assertNull(i2.getImprimanteId());
		}
		
		if (i1.getNom() != null) {
			assertTrue(i1.getNom().equals(i2.getNom()));
		} else {
			assertNull(i2.getNom());
		}
		
		if (i1.getAbscisse() != null) {
			assertTrue(i1.getAbscisse() == i2.getAbscisse());
		} else {
			assertNull(i2.getAbscisse());
		}
		
		if (i1.getOrdonnee() != null) {
			assertTrue(i1.getOrdonnee() == i2.getOrdonnee());
		} else {
			assertNull(i2.getOrdonnee());
		}
		
		if (i1.getLargeur() != null) {
			assertTrue(i1.getLargeur() == i2.getLargeur());
		} else {
			assertNull(i2.getLargeur());
		}
		
		if (i1.getLongueur() != null) {
			assertTrue(i1.getLongueur() == i2.getLongueur());
		} else {
			assertNull(i2.getLongueur());
		}
		
		if (i1.getOrientation() != null) {
			assertTrue(i1.getOrientation() == i2.getOrientation());
		} else {
			assertNull(i2.getOrientation());
		}
		
		if (i1.getMbioPrinter() != null) {
			assertTrue(i1.getMbioPrinter() == i2.getMbioPrinter());
		} else {
			assertNull(i2.getMbioPrinter());
		}
		
		if (i1.getImprimanteApi() != null) {
			assertTrue(i1.getImprimanteApi().equals(i2.getImprimanteApi()));
		} else {
			assertNull(i2.getImprimanteApi());
		}
		
		if (i1.getPlateforme() != null) {
			assertTrue(i1.getPlateforme().equals(i2.getPlateforme()));
		} else {
			assertNull(i2.getPlateforme());
		}
		
		if (i1.getAffectationImprimantes() != null) {
			assertTrue(i1.getAffectationImprimantes()
					.equals(i2.getAffectationImprimantes()));
		} else {
			assertNull(i2.getAffectationImprimantes());
		}
		
		i2 = imprimanteDao.findById(2);
		Imprimante i3 = i2.clone();		
		assertTrue(i3.getResolution().equals(i2.getResolution()));
		assertTrue(i3.getAdresseIp().equals(i2.getAdresseIp()));
		assertTrue(i3.getPort().equals(i2.getPort()));

	}
	
}
