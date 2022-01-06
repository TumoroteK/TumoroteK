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
package fr.aphp.tumorotek.dao.test.stockage;

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
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

import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO EmplacementDao et le bean du domaine Emplacement.
 *
 * @author Pierre Ventadour.
 * @version 18/03/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class EmplacementDaoTest extends AbstractDaoTest {

	@Autowired
	EmplacementDao emplacementDao;

	@Autowired
	TerminaleDao terminaleDao;

	@Autowired
	EntiteDao entiteDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<Emplacement> liste = IterableUtils.toList(IterableUtils.toList(emplacementDao.findAll()));
		assertTrue(liste.size() == 7);
	}

	/**
	 * Test l'appel de la méthode findByTerminaleWithOrder().
	 */
	@Test
	public void testFindByTerminaleWithOrder() {
		final Terminale t1 = terminaleDao.findById(1).get();
		List<Emplacement> liste = emplacementDao.findByTerminaleWithOrder(t1);
		assertTrue(liste.size() == 5);
		assertTrue(liste.get(0).getPosition() == 1);

		final Terminale t2 = terminaleDao.findById(2).get();
		liste = emplacementDao.findByTerminaleWithOrder(t2);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByTerminaleWithOrder(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByTerminaleAndVide().
	 */
	@Test
	public void testFindByTerminaleAndVide() {
		final Terminale t1 = terminaleDao.findById(1).get();
		List<Emplacement> liste = emplacementDao.findByTerminaleAndVide(t1, true);
		assertTrue(liste.size() == 2);

		liste = emplacementDao.findByTerminaleAndVide(t1, false);
		assertTrue(liste.size() == 3);

		final Terminale t2 = terminaleDao.findById(2).get();
		liste = emplacementDao.findByTerminaleAndVide(t2, true);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByTerminaleAndVide(t2, false);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByTerminaleAndVide(null, true);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCountTerminaleAndVide().
	 */
	@Test
	public void testFindByCountTerminaleAndVide() {
		final Terminale t1 = terminaleDao.findById(1).get();
		List<Long> liste = emplacementDao.findByCountTerminaleAndVide(t1, true);
		assertTrue(liste.get(0) == 2);

		liste = emplacementDao.findByCountTerminaleAndVide(t1, false);
		assertTrue(liste.get(0) == 3);

		final Terminale t2 = terminaleDao.findById(2).get();
		liste = emplacementDao.findByCountTerminaleAndVide(t2, true);
		assertTrue(liste.get(0) == 0);

		liste = emplacementDao.findByCountTerminaleAndVide(t2, false);
		assertTrue(liste.get(0) == 0);

		liste = emplacementDao.findByCountTerminaleAndVide(null, true);
		assertTrue(liste.get(0) == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetIdEntite().
	 */
	@Test
	public void testFindByObjetIdEntite() {
		final Entite e8 = entiteDao.findById(8).get();
		List<Emplacement> liste = emplacementDao.findByObjetIdEntite(1, e8);
		assertTrue(liste.size() == 1);

		liste = emplacementDao.findByObjetIdEntite(5, e8);
		assertTrue(liste.size() == 0);

		final Entite e3 = entiteDao.findById(3).get();
		liste = emplacementDao.findByObjetIdEntite(1, e3);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByObjetIdEntite(1, null);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByObjetIdEntite(null, e8);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByObjetIdEntite(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByTerminaleAndPosition().
	 */
	@Test
	public void testFindByTerminaleAndPosition() {
		final Terminale t1 = terminaleDao.findById(1).get();
		List<Emplacement> liste = emplacementDao.findByTerminaleAndPosition(t1, 1);
		assertTrue(liste.size() == 1);

		liste = emplacementDao.findByTerminaleAndPosition(t1, 5);
		assertTrue(liste.size() == 0);

		final Terminale t2 = terminaleDao.findById(2).get();
		liste = emplacementDao.findByTerminaleAndPosition(t2, 1);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByTerminaleAndPosition(t1, null);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByTerminaleAndPosition(null, 1);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByTerminaleAndPosition(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedIdTerminale().
	 */
	@Test
	public void testFindByExcludedIdTerminale() {
		final Terminale t2 = terminaleDao.findById(2).get();
		List<Emplacement> liste = emplacementDao.findByExcludedIdTerminale(1, t2);
		assertTrue(liste.size() == 0);

		final Terminale t1 = terminaleDao.findById(1).get();
		liste = emplacementDao.findByExcludedIdTerminale(1, t1);
		assertTrue(liste.size() == 4);

		liste = emplacementDao.findByExcludedIdTerminale(13, t1);
		assertTrue(liste.size() == 5);

		liste = emplacementDao.findByExcludedIdTerminale(1, null);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByExcludedIdTerminale(null, t1);
		assertTrue(liste.size() == 0);

		liste = emplacementDao.findByExcludedIdTerminale(null, null);
		assertTrue(liste.size() == 0);

	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un Emplacement.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final Emplacement e = new Emplacement();
		final Terminale t = terminaleDao.findById(1).get();
		final Entite ent = entiteDao.findById(3).get();

		e.setTerminale(t);
		e.setPosition(1);
		e.setObjetId(1);
		e.setEntite(ent);
		e.setVide(true);
		e.setAdrl("ADRL");
		e.setAdrp("ADRP");

		// Test de l'insertion
		emplacementDao.save(e);
		assertEquals(new Integer(8), e.getEmplacementId());

		final Emplacement e2 = emplacementDao.findById(new Integer(8)).get();
		// Vérification des données entrées dans la base
		assertNotNull(e2);
		assertNotNull(e2.getTerminale());
		assertNotNull(e2.getEntite());
		assertTrue(e2.getPosition() == 1);
		assertTrue(e2.getVide());
		assertTrue(e2.getAdrl().equals("ADRL"));
		assertTrue(e2.getAdrp().equals("ADRP"));

		// Test de la mise à jour
		e2.setPosition(2);
		emplacementDao.save(e2);
		assertTrue(emplacementDao.findById(new Integer(8)).get().getPosition() == 2);

		// Test de la délétion
		emplacementDao.deleteById(new Integer(8));
		assertFalse(emplacementDao.findById(new Integer(8)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {
		final Integer pos1 = 1;
		final Integer pos2 = 2;
		final Terminale t1 = terminaleDao.findById(1).get();
		final Terminale t2 = terminaleDao.findById(2).get();
		final Emplacement e1 = new Emplacement();
		final Emplacement e2 = new Emplacement();

		// L'objet 1 n'est pas égal à null
		assertFalse(e1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(e1.equals(e1));

		/* null */
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));

		/* position */
		e2.setPosition(pos1);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setPosition(pos2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setPosition(pos1);
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));

		/* Terminale */
		e2.setTerminale(t1);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setTerminale(t2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));
		e1.setTerminale(t1);
		assertTrue(e1.equals(e2));

		final Categorie c3 = new Categorie();
		assertFalse(e1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final Integer pos1 = 1;
		final Integer pos2 = 2;
		final Terminale t1 = terminaleDao.findById(1).get();
		final Terminale t2 = terminaleDao.findById(2).get();
		final Emplacement e1 = new Emplacement();
		final Emplacement e2 = new Emplacement();

		/* null */
		assertTrue(e1.hashCode() == e2.hashCode());

		/* Position */
		e2.setPosition(pos1);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setPosition(pos2);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setPosition(pos1);
		assertTrue(e1.hashCode() == e2.hashCode());

		/* Terminale */
		e2.setTerminale(t1);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setTerminale(t2);
		assertFalse(e1.hashCode() == e2.hashCode());
		e1.setTerminale(t1);
		assertTrue(e1.hashCode() == e2.hashCode());

		// un même objet garde le même hashcode dans le temps
		final int hash = e1.hashCode();
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Emplacement e1 = emplacementDao.findById(1).get();
		assertTrue(e1.toString().equals("{" + e1.getPosition() + " " + e1.getTerminale().toString() + "}"));

		final Emplacement e2 = new Emplacement();
		assertTrue(e2.toString().equals("{Empty Emplacement}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final Emplacement e1 = emplacementDao.findById(1).get();
		Emplacement e2 = new Emplacement();
		e2 = e1.clone();

		assertTrue(e1.equals(e2));

		if (e1.getEmplacementId() != null) {
			assertTrue(e1.getEmplacementId() == e2.getEmplacementId());
		} else {
			assertNull(e2.getEmplacementId());
		}

		if (e1.getTerminale() != null) {
			assertTrue(e1.getTerminale().equals(e2.getTerminale()));
		} else {
			assertNull(e2.getTerminale());
		}

		if (e1.getPosition() != null) {
			assertTrue(e1.getPosition() == e2.getPosition());
		} else {
			assertNull(e2.getPosition());
		}

		if (e1.getObjetId() != null) {
			assertTrue(e1.getObjetId() == e2.getObjetId());
		} else {
			assertNull(e2.getObjetId());
		}

		if (e1.getEntite() != null) {
			assertTrue(e1.getEntite().equals(e2.getEntite()));
		} else {
			assertNull(e2.getEntite());
		}

		if (e1.getVide() != null) {
			assertTrue(e1.getVide().equals(e2.getVide()));
		} else {
			assertNull(e2.getVide());
		}

		if (e1.getAdrl() != null) {
			assertTrue(e1.getAdrl().equals(e2.getAdrl()));
		} else {
			assertNull(e2.getAdrl());
		}

		if (e1.getAdrp() != null) {
			assertTrue(e1.getAdrp().equals(e2.getAdrp()));
		} else {
			assertNull(e2.getAdrp());
		}
	}
}
