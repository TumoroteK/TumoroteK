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
package fr.aphp.tumorotek.test.dao.interfacage;

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

import fr.aphp.tumorotek.dao.interfacage.LogicielDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.Logiciel;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.ConfigInterfacages;

/**
 *
 * Classe de test pour le DAO LogicielDao et le bean du domaine Logiciel.
 *
 * @author Pierre Ventadour.
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ConfigInterfacages.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class LogicielDaoTest extends AbstractDaoTest {

	@Autowired
	LogicielDao logicielDao;

	@Test
	public void testReadAll() {
		final List<Logiciel> liste = IterableUtils.toList(logicielDao.findAll());
		assertTrue(liste.size() == 3);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Logiciel> liste = logicielDao.findByOrder();
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getNom().equals("APIX"));
	}

	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final Logiciel log = new Logiciel();
		log.setNom("TEST");

		// Test de l'insertion
		logicielDao.save(log);
		assertEquals(new Integer(4), log.getLogicielId());

		final Logiciel log2 = logicielDao.findById(new Integer(4)).get();
		// Vérification des données entrées dans la base
		assertNotNull(log2);
		assertTrue(log2.getNom().equals("TEST"));
		assertNull(log2.getEditeur());
		assertNull(log2.getVersion());

		// Test de la mise à jour
		log2.setNom("UP");
		log2.setEditeur("EDIT");
		log2.setVersion("2.0");
		logicielDao.save(log2);
		assertTrue(logicielDao.findById(new Integer(4)).get().getNom().equals("UP"));
		assertTrue(logicielDao.findById(new Integer(4)).get().getEditeur().equals("EDIT"));
		assertTrue(logicielDao.findById(new Integer(4)).get().getVersion().equals("2.0"));

		// Test de la délétion
		logicielDao.deleteById(new Integer(4));
		assertFalse(logicielDao.findById(new Integer(4)).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "NOM";
		final String nom2 = "NOM2";
		final Logiciel l1 = new Logiciel();
		l1.setNom(nom);
		final Logiciel l2 = new Logiciel();
		l2.setNom(nom);

		// L'objet 1 n'est pas égal à null
		assertFalse(l1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(l1.equals(l1));
		// 2 objets sont égaux entre eux
		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l1));

		// Vérification de la différenciation de 2 objets
		l2.setNom(nom2);
		assertFalse(l1.equals(l2));
		assertFalse(l2.equals(l1));

		l2.setNom(null);
		assertFalse(l1.equals(l2));
		assertFalse(l2.equals(l1));

		l1.setNom(null);
		assertTrue(l1.equals(l2));
		l2.setNom(nom);
		assertFalse(l1.equals(l2));

		final Categorie c = new Categorie();
		assertFalse(l1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "NOM";
		final Logiciel l1 = new Logiciel();
		l1.setNom(nom);
		final Logiciel l2 = new Logiciel();
		l2.setNom(nom);
		final Logiciel l3 = new Logiciel();
		l3.setNom(null);
		assertTrue(l3.hashCode() > 0);

		final int hash = l1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(l1.hashCode() == l2.hashCode());
		assertFalse(l1.hashCode() == l3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == l1.hashCode());
		assertTrue(hash == l1.hashCode());
		assertTrue(hash == l1.hashCode());
		assertTrue(hash == l1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Logiciel l1 = logicielDao.findById(1).get();
		assertTrue(l1.toString().equals("{" + l1.getNom() + "}"));

		final Logiciel l2 = new Logiciel();
		assertTrue(l2.toString().equals("{Empty Logiciel}"));
	}

}
