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
package fr.aphp.tumorotek.test.dao.systeme;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO UniteDao et le bean du domaine Unite.
 *
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class UniteDaoTest extends AbstractDaoTest {

	@Autowired
	UniteDao uniteDao;

	/** valeur du nom pour la maj. */
	private String updatedUnite = "Unite mise a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllUnites() {
		final List<Unite> unites = IterableUtils.toList(IterableUtils.toList(uniteDao.findAll()));
		assertTrue(unites.size() == 14);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Unite> list = uniteDao.findByOrder();
		assertTrue(list.size() == 14);
		// assertTrue(list.get(0).getUnite().equals("10^6 CELL"));
	}

	/**
	 * Test l'appel de la méthode findByUnite().
	 */
	@Test
	public void testFindByUnite() {
		List<Unite> unites = uniteDao.findByUnite("µg");
		assertTrue(unites.size() == 1);
		unites = uniteDao.findByUnite("L");
		assertTrue(unites.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByTypeWithOrder().
	 */
	@Test
	public void testFindByTypeWithOrder() {
		List<Unite> unites = uniteDao.findByTypeWithOrder("masse");
		assertTrue(unites.size() == 4);
		assertTrue(unites.get(1).getNom().equals("mg"));

		unites = uniteDao.findByTypeWithOrder("poids");
		assertTrue(unites.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une Unite.
	 * 
	 * @throws Exception Lance une exception en cas d'erreur.
	 */
	@Test
	public void testCrudUnite() throws Exception {

		final Unite u = new Unite();

		u.setUnite("ML");
		// Test de l'insertion
		uniteDao.save(u);
		assertEquals(new Integer(15), u.getId());

		// Test de la mise à jour
		final Unite u2 = uniteDao.findById(new Integer(15)).get();
		assertNotNull(u2);
		assertTrue(u2.getNom().equals("ML"));
		u2.setUnite(updatedUnite);
		uniteDao.save(u2);
		assertTrue(uniteDao.findById(new Integer(15)).get().getNom().equals(updatedUnite));

		// Test de la délétion
		uniteDao.deleteById(new Integer(15));
		assertFalse(uniteDao.findById(new Integer(15)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String unite = "ML";
		final String unite2 = "CL";
		final String type = "type";
		final String type2 = "type2";
		final Unite u1 = new Unite();
		u1.setUnite(unite);
		u1.setType(type);
		final Unite u2 = new Unite();
		u2.setUnite(unite);
		u2.setType(type);

		// L'objet 1 n'est pas égal à null
		assertFalse(u1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(u1.equals(u1));
		// 2 objets sont égaux entre eux
		assertTrue(u1.equals(u2));
		assertTrue(u2.equals(u1));

		u1.setUnite(null);
		u1.setType(null);
		u2.setUnite(null);
		u2.setType(null);
		assertTrue(u1.equals(u2));
		u2.setType(type);
		assertFalse(u1.equals(u2));
		u1.setType(type);
		assertTrue(u1.equals(u2));
		u2.setType(type2);
		assertFalse(u1.equals(u2));
		u2.setType(null);
		assertFalse(u1.equals(u2));
		u2.setUnite(unite);
		assertFalse(u1.equals(u2));

		u1.setUnite(unite);
		u1.setType(null);
		u2.setType(null);
		u2.setUnite(unite);
		assertTrue(u1.equals(u2));
		u2.setUnite(unite2);
		assertFalse(u1.equals(u2));
		u2.setType(type);
		assertFalse(u1.equals(u2));

		// Vérification de la différenciation de 2 objets
		u2.setUnite(unite2);
		assertFalse(u1.equals(u2));
		assertFalse(u2.equals(u1));
		u2.setUnite(unite);
		u2.setType(type2);
		assertFalse(u1.equals(u2));
		assertFalse(u2.equals(u1));

		final Categorie c = new Categorie();
		assertFalse(u1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String unite = "ML";
		final String type = "type";
		final Unite u1 = new Unite(1, unite, type);
		final Unite u2 = new Unite(2, unite, type);
		final Unite u3 = new Unite(3, null, null);
		assertTrue(u3.hashCode() > 0);

		final int hash = u1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(u1.hashCode() == u2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == u1.hashCode());
		assertTrue(hash == u1.hashCode());
		assertTrue(hash == u1.hashCode());
		assertTrue(hash == u1.hashCode());

	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Unite u1 = uniteDao.findById(1).get();
		assertTrue(u1.toString().equals("{" + u1.getNom() + ", " + u1.getType() + "}"));

		final Unite u2 = new Unite();
		assertTrue(u2.toString().equals("{Empty Unite}"));
	}
}
