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

import fr.aphp.tumorotek.dao.contexte.CategorieDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO CategorieDao et le bean du domaine Categorie.
 * Classe de test créée le 08/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class CategorieDaoTest extends AbstractDaoTest {

	@Autowired
	CategorieDao categorieDao;

	/** Valeur du nom pour la maj. */
	private final String updatedNom = "Cat mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllCategories() {
		final List<Categorie> categories = IterableUtils.toList(categorieDao.findAll());
		assertTrue(categories.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Categorie> list = categorieDao.findByOrder();
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getNom().equals("CAT1"));
	}

	/**
	 * Test l'appel de la méthode findByNom().
	 */
	@Test
	public void testFindByNom() {
		List<Categorie> categories = categorieDao.findByNom("CAT1");
		assertTrue(categories.size() == 1);
		categories = categorieDao.findByNom("CAT5");
		assertTrue(categories.size() == 0);
		categories = categorieDao.findByNom("CA%");
		assertTrue(categories.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByEtablissementId().
	 */
	@Test
	public void testFindByEtablissementId() {
		List<Categorie> categories = categorieDao.findByEtablissementId(1);
		assertTrue(categories.size() == 1);
		categories = categorieDao.findByEtablissementId(4);
		assertTrue(categories.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<Categorie> liste = categorieDao.findByExcludedId(1);
		assertTrue(liste.size() == 1);
		final Categorie cat = liste.get(0);
		assertNotNull(cat);
		assertTrue(cat.getId() == 2);

		liste = categorieDao.findByExcludedId(15);
		assertTrue(liste.size() == 2);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une catégorie.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	@Test
	public void testCrudCategorie() throws Exception {
		final Categorie c = new Categorie();

		c.setNom("CAT3");
		// Test de l'insertion
		categorieDao.save(c);
		assertEquals(new Integer(3), c.getId());

		// Test de la mise à jour
		final Categorie c2 = categorieDao.findById(new Integer(3)).get();
		assertNotNull(c2);
		assertTrue(c2.getNom().equals("CAT3"));
		c2.setNom(updatedNom);
		categorieDao.save(c2);
		assertTrue(categorieDao.findById(new Integer(3)).get().getNom().equals(updatedNom));

		// Test de la délétion
		categorieDao.deleteById(new Integer(3));
		assertFalse(categorieDao.findById(new Integer(3)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "Categorie";
		final String nom2 = "Categorie2";
		final Categorie c1 = new Categorie();
		c1.setNom(nom);
		final Categorie c2 = new Categorie();
		c2.setNom(nom);

		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(c1.equals(c1));
		// 2 objets sont égaux entre eux
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));

		// Vérification de la différenciation de 2 objets
		c2.setNom(nom2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		c2.setNom(null);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		c1.setNom(null);
		assertTrue(c1.equals(c2));
		c2.setNom(nom);
		assertFalse(c1.equals(c2));

		final Banque b = new Banque();
		assertFalse(c1.equals(b));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "Categorie";
		final Categorie c1 = new Categorie();
		c1.setId(1);
		c1.setNom(nom);
		final Categorie c2 = new Categorie();
		c2.setId(1);
		c2.setNom(nom);
		final Categorie c3 = new Categorie();
		c3.setId(1);
		c3.setNom(null);
		assertTrue(c3.hashCode() > 0);

		final int hash = c1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(c1.hashCode() == c2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());

	}

}
