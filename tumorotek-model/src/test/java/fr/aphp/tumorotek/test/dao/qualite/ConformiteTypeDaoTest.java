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
package fr.aphp.tumorotek.test.dao.qualite;

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

import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ConformiteTypeDao et le bean du domaine
 * ConformiteType. Date: 08/11/2011
 *
 * @author Pierre Ventadour.
 * @version 2.0.10
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ConformiteTypeDaoTest extends AbstractDaoTest {

	@Autowired
	ConformiteTypeDao conformiteTypeDao;

	@Autowired
	EntiteDao entiteDao;

	private int tot = 5;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<ConformiteType> liste = IterableUtils.toList(IterableUtils.toList(conformiteTypeDao.findAll()));
		assertTrue(liste.size() == tot);
	}

	/**
	 * Test l'appel de la méthode findByType().
	 */
	@Test
	public void testFindByEntiteAndType() {
		List<ConformiteType> liste = conformiteTypeDao.findByEntiteAndType("Arrivee", entiteDao.findById(2).get());
		assertTrue(liste.size() == 1);

		liste = conformiteTypeDao.findByEntiteAndType("Arrivee", null);
		assertTrue(liste.size() == 0);

		liste = conformiteTypeDao.findByEntiteAndType("Arrivee", entiteDao.findById(3).get());
		assertTrue(liste.size() == 0);

		liste = conformiteTypeDao.findByEntiteAndType("Traitement", entiteDao.findById(3).get());
		assertTrue(liste.size() == 1);

		liste = conformiteTypeDao.findByEntiteAndType("Traitement", entiteDao.findById(8).get());
		assertTrue(liste.size() == 1);

		liste = conformiteTypeDao.findByEntiteAndType("Test", entiteDao.findById(2).get());
		assertTrue(liste.size() == 0);

		liste = conformiteTypeDao.findByEntiteAndType(null, entiteDao.findById(2).get());
		assertTrue(liste.size() == 0);
	}

	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final ConformiteType type = new ConformiteType();
		type.setConformiteType("Test");
		type.setEntite(entiteDao.findById(8).get());

		// Test de l'insertion
		conformiteTypeDao.save(type);
		final Integer id = new Integer(tot + 1);
		assertEquals(id, type.getConformiteTypeId());

		final ConformiteType c2 = conformiteTypeDao.findById(id).get();
		// Vérification des données entrées dans la base
		assertNotNull(c2);
		assertTrue(c2.getConformiteType().equals("Test"));
		assertTrue(c2.getEntite().getNom().equals("ProdDerive"));

		// Test de la mise à jour
		c2.setConformiteType("UP");
		conformiteTypeDao.save(c2);
		assertTrue(conformiteTypeDao.findById(id).get().getConformiteType().equals("UP"));

		// Test de la délétion
		conformiteTypeDao.deleteById(id);
		assertFalse(conformiteTypeDao.findById(id).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "NOM";
		final String nom2 = "NOM2";
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(8).get();
		final ConformiteType c1 = new ConformiteType();
		c1.setConformiteType(nom);
		c1.setEntite(e1);
		final ConformiteType c2 = new ConformiteType();
		c2.setConformiteType(nom);
		c2.setEntite(e1);

		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(c1.equals(c1));
		// 2 objets sont égaux entre eux
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));

		// Vérification de la différenciation de 2 objets
		c2.setConformiteType(nom2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		c2.setConformiteType(null);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		c1.setConformiteType(null);
		assertTrue(c1.equals(c2));
		c2.setConformiteType(nom);
		assertFalse(c1.equals(c2));

		c1.setConformiteType(nom);
		assertTrue(c2.equals(c1));

		c2.setEntite(e2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		c2.setEntite(null);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		c1.setEntite(null);
		assertTrue(c1.equals(c2));
		c2.setEntite(e1);
		assertFalse(c1.equals(c2));

		final Categorie c = new Categorie();
		assertFalse(c1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "NOM";
		final Entite e1 = entiteDao.findById(2).get();
		final ConformiteType c1 = new ConformiteType();
		c1.setConformiteType(nom);
		c1.setEntite(e1);
		final ConformiteType c2 = new ConformiteType();
		c2.setConformiteType(nom);
		c2.setEntite(e1);
		final ConformiteType c3 = new ConformiteType();
		c3.setConformiteType(null);
		assertTrue(c3.hashCode() > 0);

		final int hash = c1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(c1.hashCode() == c2.hashCode());
		assertFalse(c1.hashCode() == c3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final ConformiteType c1 = conformiteTypeDao.findById(1).get();
		assertTrue(c1.toString().equals("{" + c1.getConformiteType() + "}"));

		final ConformiteType c2 = new ConformiteType();
		assertTrue(c2.toString().equals("{Empty ConformiteType}"));
	}

}
