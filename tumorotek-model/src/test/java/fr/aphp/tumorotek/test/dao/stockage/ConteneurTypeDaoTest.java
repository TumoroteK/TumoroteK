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

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ConteneurTypeDaoTest extends AbstractDaoTest {

	@Autowired
	ConteneurTypeDao conteneurTypeDao;

	@Autowired
	PlateformeDao plateformeDao;

	private String updatedType = "Mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAlls() {
		final List<ConteneurType> liste = IterableUtils.toList(IterableUtils.toList(conteneurTypeDao.findAll()));
		assertTrue(liste.size() == 3);
	}

	@Test
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1).get();
		List<? extends TKThesaurusObject> list = conteneurTypeDao.findByPfOrder(pf);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNom().equals("CONGELATEUR"));
		pf = plateformeDao.findById(2).get();
		list = conteneurTypeDao.findByPfOrder(pf);
		assertTrue(list.size() == 0);
		list = conteneurTypeDao.findByPfOrder(null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<ConteneurType> liste = conteneurTypeDao.findByExcludedId(1);
		assertTrue(liste.size() == 2);
		final ConteneurType type = liste.get(0);
		assertNotNull(type);
		assertTrue(type.getId() == 2);

		liste = conteneurTypeDao.findByExcludedId(15);
		assertTrue(liste.size() == 3);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final ConteneurType ct = new ConteneurType();
		ct.setPlateforme(plateformeDao.findById(1).get());
		ct.setNom("Type");
		// Test de l'insertion
		conteneurTypeDao.save(ct);
		assertEquals(new Integer(4), ct.getId());

		// Test de la mise à jour
		final ConteneurType ct2 = conteneurTypeDao.findById(new Integer(4)).get();
		assertNotNull(ct2);
		assertTrue(ct2.getNom().equals("Type"));
		ct2.setNom(updatedType);
		conteneurTypeDao.save(ct2);
		assertTrue(conteneurTypeDao.findById(new Integer(4)).get().getNom().equals(updatedType));

		// Test de la délétion
		conteneurTypeDao.deleteById(new Integer(4));
		assertFalse(conteneurTypeDao.findById(new Integer(4)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String type = "TYPE";
		final String type2 = "TYPE2";
		final ConteneurType ct1 = new ConteneurType();
		ct1.setNom(type);
		final ConteneurType ct2 = new ConteneurType();
		ct2.setNom(type);

		// L'objet 1 n'est pas égal à null
		assertFalse(ct1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(ct1.equals(ct1));
		// 2 objets sont égaux entre eux
		assertTrue(ct1.equals(ct2));
		assertTrue(ct2.equals(ct1));

		// Vérification de la différenciation de 2 objets
		ct2.setNom(type2);
		assertFalse(ct1.equals(ct2));
		assertFalse(ct2.equals(ct1));

		ct2.setNom(null);
		assertFalse(ct1.equals(ct2));
		assertFalse(ct2.equals(ct1));

		ct1.setNom(null);
		assertTrue(ct1.equals(ct2));
		ct2.setNom(type2);
		assertFalse(ct1.equals(ct2));

		// plateforme
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		ct1.setNom(ct2.getNom());
		ct1.setPlateforme(pf1);
		ct2.setPlateforme(pf1);
		assertTrue(ct1.equals(ct2));
		ct2.setPlateforme(pf2);
		assertFalse(ct1.equals(ct2));

		final Categorie c = new Categorie();
		assertFalse(ct1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String type = "TYPE";
		final ConteneurType ct1 = new ConteneurType();
		ct1.setNom(type);
		final ConteneurType ct2 = new ConteneurType();
		ct2.setNom(type);
		final ConteneurType ct3 = new ConteneurType();
		ct3.setNom(type);
		assertTrue(ct3.hashCode() > 0);

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		ct1.setPlateforme(pf1);
		ct2.setPlateforme(pf1);
		ct3.setPlateforme(pf2);

		final int hash = ct1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(ct1.hashCode() == ct2.hashCode());
		assertFalse(ct1.hashCode() == ct3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == ct1.hashCode());
		assertTrue(hash == ct1.hashCode());
		assertTrue(hash == ct1.hashCode());
		assertTrue(hash == ct1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final ConteneurType ct1 = conteneurTypeDao.findById(1).get();
		assertTrue(ct1.toString().equals("{" + ct1.getNom() + "}"));

		final ConteneurType ct2 = new ConteneurType();
		assertTrue(ct2.toString().equals("{Empty ConteneurType}"));
	}

}
