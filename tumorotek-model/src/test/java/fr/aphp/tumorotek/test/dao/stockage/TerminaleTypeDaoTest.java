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
package fr.aphp.tumorotek.test.dao.stockage;

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

import fr.aphp.tumorotek.dao.stockage.TerminaleTypeDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.stockage.TerminaleType;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class TerminaleTypeDaoTest extends AbstractDaoTest {

	@Autowired
	TerminaleTypeDao terminaleTypeDao;
	/** valeur du nom pour la maj. */

	private String updatedType = "Mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAlls() {
		final List<TerminaleType> liste = IterableUtils.toList(IterableUtils.toList(terminaleTypeDao.findAll()));
		assertTrue(liste.size() == 6);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<TerminaleType> list = terminaleTypeDao.findByOrder();
		assertTrue(list.size() == 6);
		assertTrue(list.get(0).getType().equals("RECTANGULAIRE_100"));
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<TerminaleType> liste = terminaleTypeDao.findByExcludedId(1);
		assertTrue(liste.size() == 5);
		final TerminaleType type = liste.get(0);
		assertNotNull(type);
		assertTrue(type.getTerminaleTypeId() == 2);

		liste = terminaleTypeDao.findByExcludedId(15);
		assertTrue(liste.size() == 6);
	}

	/**
	 * Test l'appel de la méthode findByType().
	 */
	@Test
	public void testFindByType() {
		List<TerminaleType> liste = terminaleTypeDao.findByType("VISOTUBE_16_TRI");
		assertTrue(liste.size() == 1);
		final TerminaleType type = liste.get(0);
		assertNotNull(type);
		assertTrue(type.getTerminaleTypeId() == 5);

		liste = terminaleTypeDao.findByType("TEST");
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final TerminaleType tt = new TerminaleType();

		tt.setType("Type");
		tt.setNbPlaces(100);
		tt.setHauteur(10);
		tt.setLongueur(10);
		tt.setDepartNumHaut(false);
		// Test de l'insertion
		terminaleTypeDao.save(tt);
		assertEquals(new Integer(7), tt.getTerminaleTypeId());

		// Test de la mise à jour
		final TerminaleType tt2 = terminaleTypeDao.findById(new Integer(7)).get();
		assertNotNull(tt2);
		assertTrue(tt2.getType().equals("Type"));
		assertFalse(tt2.getDepartNumHaut());
		tt2.setType(updatedType);
		tt2.setDepartNumHaut(true);
		terminaleTypeDao.save(tt2);
		assertTrue(terminaleTypeDao.findById(new Integer(7)).get().getType().equals(updatedType));
		assertTrue(terminaleTypeDao.findById(new Integer(7)).get().getDepartNumHaut());

		// Test de la délétion
		terminaleTypeDao.deleteById(new Integer(7));
		assertFalse(terminaleTypeDao.findById(new Integer(7)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String type = "TYPE";
		final String type2 = "TYPE2";
		final TerminaleType tt1 = new TerminaleType();
		tt1.setType(type);
		final TerminaleType tt2 = new TerminaleType();
		tt2.setType(type);

		// L'objet 1 n'est pas égal à null
		assertFalse(tt1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(tt1.equals(tt1));
		// 2 objets sont égaux entre eux
		assertTrue(tt1.equals(tt2));
		assertTrue(tt2.equals(tt1));

		// Vérification de la différenciation de 2 objets
		tt2.setType(type2);
		assertFalse(tt1.equals(tt2));
		assertFalse(tt2.equals(tt1));

		tt2.setType(null);
		assertFalse(tt1.equals(tt2));
		assertFalse(tt2.equals(tt1));

		tt1.setType(null);
		assertTrue(tt1.equals(tt2));
		tt2.setType(type2);
		assertFalse(tt1.equals(tt2));

		final Categorie c = new Categorie();
		assertFalse(tt1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String type = "TYPE";
		final TerminaleType tt1 = new TerminaleType();
		tt1.setType(type);
		final TerminaleType tt2 = new TerminaleType();
		tt2.setType(type);
		final TerminaleType ct3 = new TerminaleType();
		ct3.setType(type);
		assertTrue(ct3.hashCode() > 0);

		final int hash = tt1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(tt1.hashCode() == tt2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == tt1.hashCode());
		assertTrue(hash == tt1.hashCode());
		assertTrue(hash == tt1.hashCode());
		assertTrue(hash == tt1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final TerminaleType tt1 = terminaleTypeDao.findById(1).get();
		assertTrue(tt1.toString().equals("{" + tt1.getType() + "}"));

		final TerminaleType tt2 = new TerminaleType();
		assertTrue(tt2.toString().equals("{Empty TerminaleType}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final TerminaleType t1 = terminaleTypeDao.findById(1).get();
		TerminaleType t2 = new TerminaleType();
		t2 = t1.clone();

		assertTrue(t1.equals(t2));

		if (t1.getTerminaleTypeId() != null) {
			assertTrue(t1.getTerminaleTypeId() == t2.getTerminaleTypeId());
		} else {
			assertNull(t2.getTerminaleTypeId());
		}

		if (t1.getType() != null) {
			assertTrue(t1.getType().equals(t2.getType()));
		} else {
			assertNull(t2.getType());
		}

		if (t1.getNbPlaces() != null) {
			assertTrue(t1.getNbPlaces() == t2.getNbPlaces());
		} else {
			assertNull(t2.getNbPlaces());
		}

		if (t1.getHauteur() != null) {
			assertTrue(t1.getHauteur() == t2.getHauteur());
		} else {
			assertNull(t2.getHauteur());
		}

		if (t1.getLongueur() != null) {
			assertTrue(t1.getLongueur() == t2.getLongueur());
		} else {
			assertNull(t2.getLongueur());
		}

		if (t1.getScheme() != null) {
			assertTrue(t1.getScheme().equals(t2.getScheme()));
		} else {
			assertNull(t2.getScheme());
		}
	}

}
