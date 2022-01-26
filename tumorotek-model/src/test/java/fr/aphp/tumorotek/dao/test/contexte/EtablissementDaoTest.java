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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.test.Config;

import fr.aphp.tumorotek.dao.contexte.CategorieDao;
import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
import fr.aphp.tumorotek.model.contexte.Etablissement;

/**
 *
 * Classe de test pour le DAO EtablissementDao et le bean du domaine
 * Etablissement.
 *
 * @author Pierre Ventadour
 * @version 09/09/2009
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class EtablissementDaoTest extends AbstractDaoTest {

	@Autowired
	EtablissementDao etablissementDao;

	@Autowired
	CoordonneeDao coordonneeDao;

	@Autowired
	CategorieDao categorieDao;

	private final String updatedNom = "Etab mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllEtablissements() {
		final List<Etablissement> etabs = IterableUtils.toList(etablissementDao.findAll());
		assertTrue(etabs.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Etablissement> etabs = etablissementDao.findByOrder();
		assertTrue(etabs.size() == 4);
		assertTrue(etabs.get(0).getNom().equals("BEAUVAIS CH"));
		assertTrue(etabs.get(3).getNom().equals("SAINT LOUIS"));
	}

	/**
	 * Test l'appel de la méthode findByNom().
	 */
	@Test
	public void testFindByNom() {
		List<Etablissement> etabs = etablissementDao.findByNom("SAINT LOUIS");
		assertTrue(etabs.size() == 1);
		etabs = etablissementDao.findByNom("BICHAT");
		assertTrue(etabs.size() == 0);

		etabs = etablissementDao.findByNom("B%");
		assertTrue(etabs.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByVille().
	 */
	@Test
	public void testFindByVille() {
		List<Etablissement> etabs = etablissementDao.findByVille("PARIS");
		assertTrue(etabs.size() == 1);
		etabs = etablissementDao.findByVille("BEAU");
		assertTrue(etabs.size() == 0);

		etabs = etablissementDao.findByVille("B%");
		assertTrue(etabs.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByFiness().
	 */
	@Test
	public void testFindByFiness() {
		List<Etablissement> etabs = etablissementDao.findByFiness("1111");
		assertTrue(etabs.size() == 1);
		etabs = etablissementDao.findByFiness("548969125");
		assertTrue(etabs.size() == 0);
		etabs = etablissementDao.findByFiness("11%");
		assertTrue(etabs.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByLocal().
	 */
	@Test
	public void testFindByLocal() {
		final List<Etablissement> etabs = etablissementDao.findByLocal(true);
		assertTrue(etabs.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByArchiveWithOrder().
	 */
	@Test
	public void testFindByArchiveWithOrder() {
		List<Etablissement> etabs = etablissementDao.findByArchiveWithOrder(false);
		assertTrue(etabs.size() == 3);
		assertTrue(etabs.get(0).getNom().equals("BEAUVAIS CH"));

		etabs = etablissementDao.findByArchiveWithOrder(true);
		assertTrue(etabs.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByCoordonneeId().
	 */
	@Test
	public void testFindByCoordonnee() {
		Coordonnee c = coordonneeDao.findById(1).get();
		List<Etablissement> etabs = etablissementDao.findByCoordonnee(c);
		assertTrue(etabs.size() == 1);
		c = coordonneeDao.findById(5).get();
		etabs = etablissementDao.findByCoordonnee(c);
		assertTrue(etabs.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCategorie().
	 */
	@Test
	public void testFindByCategorie() {
		Categorie c = categorieDao.findById(1).get();
		List<Etablissement> etabs = etablissementDao.findByCategorie(c);
		assertTrue(etabs.size() == 3);
		// c = categorieDao.findById(10).get();
		etabs = etablissementDao.findByCategorie(null);
		assertTrue(etabs.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByIdWithFetch().
	 */
	@Test
	public void testFindByIdWithFetch() {
		final List<Etablissement> etabs = etablissementDao.findByIdWithFetch(1);
		final Etablissement etab = etabs.get(0);
		assertNotNull(etab);
		assertTrue(etab.getCoordonnee().getCoordonneeId() == 1);
		assertTrue(etab.getCategorie().getId() == 1);
	}

	/**
	 * Test l'appel de la méthode findByServiceId().
	 */
	@Test
	public void testFindByServiceId() {
		List<Etablissement> etabs = etablissementDao.findByServiceId(1);
		assertTrue(etabs.size() == 1);
		assertTrue(etabs.get(0).getEtablissementId() == 1);
		etabs = etablissementDao.findByServiceId(10);
		assertTrue(etabs.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCollaborateurId().
	 */
	@Test
	public void testFindByCollaborateurId() {
		List<Etablissement> etabs = etablissementDao.findByCollaborateurId(3);
		assertTrue(etabs.size() == 1);
		assertTrue(etabs.get(0).getEtablissementId() == 1);
		etabs = etablissementDao.findByCollaborateurId(10);
		assertTrue(etabs.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<Etablissement> list = etablissementDao.findByExcludedId(1);
		assertTrue(list.size() == 3);

		list = etablissementDao.findByExcludedId(10);
		assertTrue(list.size() == 4);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un établissement.
	 * 
	 * @throws Exception en cas d'erreur dans le traitement des données.
	 */
	// @Rollback(false)
	@Test
	@Transactional
	public void testCrudEtablissement() throws Exception {

		final Etablissement e = new Etablissement();
		// Coordonnee c = coordonneeDao.findById(5).get();
		final Categorie cat = categorieDao.findById(2).get();
		e.setNom("Etablissement");
		e.setFiness("452323");
		e.setLocal(false);
		e.setArchive(false);
		// e.setCoordonnee(c);
		e.setCategorie(cat);
		// Test de l'insertion
		etablissementDao.save(e);
		assertEquals(new Integer(5), e.getEtablissementId());

		// Test de la mise à jour
		final Etablissement e2 = etablissementDao.findById(new Integer(5)).get();
		assertNotNull(e2);
		assertTrue(e2.getNom().equals("Etablissement"));
		assertTrue(e2.getFiness().equals("452323"));
		assertFalse(e2.isLocal());
		assertFalse(e2.getArchive());
		assertNotNull(e2.getCategorie());
		// assertNotNull(e2.getCoordonnee());
		e2.setNom(updatedNom);
		etablissementDao.save(e2);
		assertTrue(etablissementDao.findById(new Integer(5)).get().getNom().equals(updatedNom));

		// Test de la délétion
		etablissementDao.deleteById(5);
		assertFalse(etablissementDao.findById(new Integer(5)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "Etab";
		final String nom2 = "Etab2";
		final String finess = "Finess";
		final String finess2 = "Finess2";
		final Etablissement e1 = new Etablissement();
		e1.setNom(nom);
		e1.setFiness(finess);
		final Etablissement e2 = new Etablissement();
		e2.setNom(nom);
		e2.setFiness(finess);

		// L'objet 1 n'est pas égal à null
		assertFalse(e1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(e1.equals(e1));
		// 2 objets sont égaux entre eux
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));

		e1.setNom(null);
		e1.setFiness(null);
		e2.setNom(null);
		e2.setFiness(null);
		assertTrue(e1.equals(e2));
		e2.setFiness(finess);
		assertFalse(e1.equals(e2));
		e1.setFiness(finess);
		assertTrue(e1.equals(e2));
		e2.setFiness(finess2);
		assertFalse(e1.equals(e2));
		e2.setFiness(null);
		assertFalse(e1.equals(e2));
		e2.setNom(nom);
		assertFalse(e1.equals(e2));

		e1.setNom(nom);
		e1.setFiness(null);
		e2.setFiness(null);
		e2.setNom(nom);
		assertTrue(e1.equals(e2));
		e2.setNom(nom2);
		assertFalse(e1.equals(e2));
		e2.setFiness(finess);
		assertFalse(e1.equals(e2));

		// Vérification de la différenciation de 2 objets
		e2.setNom(nom2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e2.setNom(nom);
		e2.setFiness(finess2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));

		/*
		 * e2.setFiness(null); assertFalse(e1.equals(e2)); assertFalse(e2.equals(e1));
		 * e2.setNom(null); e2.setFiness(finess); assertFalse(e1.equals(e2));
		 * assertFalse(e2.equals(e1));
		 */

		final Categorie c = new Categorie();
		assertFalse(e1.equals(c));

		// teste doublons
		final Etablissement e = etablissementDao.findById(1).get();
		final Etablissement et2 = new Etablissement();
		et2.setNom(e.getNom());
		et2.setFiness(e.getFiness());
		assertTrue(IterableUtils.toList(etablissementDao.findAll()).contains(et2));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "Etab";
		final String finess = "Finess";
		final Etablissement e1 = new Etablissement();
		e1.setEtablissementId(1);
		e1.setNom(nom);
		e1.setFiness(finess);
		e1.setArchive(true);
		e1.setLocal(true);
		final Etablissement e2 = new Etablissement();
		e2.setEtablissementId(1);
		e2.setNom(nom);
		e2.setFiness(finess);
		e2.setArchive(true);
		e2.setLocal(true);
		final Etablissement e3 = new Etablissement();
		e3.setNom(null);
		e3.setFiness(null);
		assertTrue(e3.hashCode() > 0);

		final int hash = e1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(e1.hashCode() == e2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());

	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Etablissement e1 = etablissementDao.findById(1).get();
		assertTrue(e1.toString().equals("{" + e1.getNom() + "}"));

		final Etablissement e2 = new Etablissement();
		assertTrue(e2.toString().equals("{Empty Etablissement}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final Etablissement e1 = etablissementDao.findById(1).get();
		final Etablissement e2 = e1.clone();
		assertTrue(e1.equals(e2));

		if (e1.getEtablissementId() != null) {
			assertTrue(e1.getEtablissementId() == e2.getEtablissementId());
		} else {
			assertNull(e2.getEtablissementId());
		}

		if (e1.getCoordonnee() != null) {
			assertTrue(e1.getCoordonnee().equals(e2.getCoordonnee()));
		} else {
			assertNull(e2.getCoordonnee());
		}

		if (e1.getCategorie() != null) {
			assertTrue(e1.getCategorie().equals(e2.getCategorie()));
		} else {
			assertNull(e2.getCategorie());
		}

		if (e1.getNom() != null) {
			assertTrue(e1.getNom().equals(e2.getNom()));
		} else {
			assertNull(e2.getNom());
		}

		if (e1.getFiness() != null) {
			assertTrue(e1.getFiness().equals(e2.getFiness()));
		} else {
			assertNull(e2.getFiness());
		}

		assertTrue(e1.getArchive() == e2.getArchive());

		assertTrue(e1.isLocal() == e2.isLocal());
	}

}
