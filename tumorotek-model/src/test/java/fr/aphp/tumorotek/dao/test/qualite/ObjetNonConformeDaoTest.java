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
package fr.aphp.tumorotek.dao.test.qualite;

import java.util.ArrayList;
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

import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO ObjetNonConformeDao et le bean du domaine
 * ObjetNonConforme.
 *
 * @author Pierre Ventadour.
 * @version 08/11/2011
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ObjetNonConformeDaoTest extends AbstractDaoTest {

	@Autowired
	ObjetNonConformeDao objetNonConformeDao;

	@Autowired
	NonConformiteDao nonConformiteDao;

	@Autowired
	EntiteDao entiteDao;

	@Autowired
	ConformiteTypeDao conformiteTypeDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<ObjetNonConforme> liste = IterableUtils.toList(IterableUtils.toList(objetNonConformeDao.findAll()));
		assertTrue(liste.size() == 6);
	}

	/**
	 * Test de la méthode findByNonConformite.
	 */
	@Test
	public void testfindByNonConformite() {
		final NonConformite n6 = nonConformiteDao.findById(6).get();
		List<ObjetNonConforme> liste = objetNonConformeDao.findByNonConformite(n6);
		assertTrue(liste.size() == 2);

		final NonConformite n1 = nonConformiteDao.findById(1).get();
		liste = objetNonConformeDao.findByNonConformite(n1);
		assertTrue(liste.size() == 1);

		final NonConformite n3 = nonConformiteDao.findById(3).get();
		liste = objetNonConformeDao.findByNonConformite(n3);
		assertTrue(liste.size() == 0);

		liste = objetNonConformeDao.findByNonConformite(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetAndEntite().
	 */
	@Test
	public void testFindByObjetAndEntite() {
		final Entite e3 = entiteDao.findById(3).get();
		List<ObjetNonConforme> liste = objetNonConformeDao.findByObjetAndEntite(3, e3);
		assertTrue(liste.size() == 2);

		liste = objetNonConformeDao.findByObjetAndEntite(2, e3);
		assertTrue(liste.size() == 1);

		liste = objetNonConformeDao.findByObjetAndEntite(150, e3);
		assertTrue(liste.size() == 0);

		final Entite e1 = entiteDao.findById(1).get();
		liste = objetNonConformeDao.findByObjetAndEntite(2, e1);
		assertTrue(liste.size() == 0);

		final Entite e8 = entiteDao.findById(8).get();
		liste = objetNonConformeDao.findByObjetAndEntite(3, e8);
		assertTrue(liste.size() == 2);

		liste = objetNonConformeDao.findByObjetAndEntite(null, e3);
		assertTrue(liste.size() == 0);

		liste = objetNonConformeDao.findByObjetAndEntite(1, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetEntiteAndType().
	 */
	@Test
	public void testFindByObjetEntiteAndType() {
		final Entite e3 = entiteDao.findById(3).get();
		final ConformiteType t3 = conformiteTypeDao.findById(3).get();
		List<ObjetNonConforme> liste = objetNonConformeDao.findByObjetEntiteAndType(3, e3, t3);
		assertTrue(liste.size() == 1);

		liste = objetNonConformeDao.findByObjetEntiteAndType(2, e3, t3);
		assertTrue(liste.size() == 1);

		final ConformiteType t2 = conformiteTypeDao.findById(2).get();
		liste = objetNonConformeDao.findByObjetEntiteAndType(2, e3, t2);
		assertTrue(liste.size() == 0);

		liste = objetNonConformeDao.findByObjetEntiteAndType(150, e3, t3);
		assertTrue(liste.size() == 0);

		final Entite e1 = entiteDao.findById(1).get();
		liste = objetNonConformeDao.findByObjetEntiteAndType(2, e1, t3);
		assertTrue(liste.size() == 0);

		liste = objetNonConformeDao.findByObjetEntiteAndType(null, e3, t3);
		assertTrue(liste.size() == 0);

		liste = objetNonConformeDao.findByObjetEntiteAndType(1, null, t3);
		assertTrue(liste.size() == 0);

		liste = objetNonConformeDao.findByObjetEntiteAndType(1, e3, null);
		assertTrue(liste.size() == 0);
	}

	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final ObjetNonConforme o1 = new ObjetNonConforme();
		final NonConformite n1 = nonConformiteDao.findById(1).get();
		final Entite e2 = entiteDao.findById(2).get();
		o1.setNonConformite(n1);
		o1.setEntite(e2);
		o1.setObjetId(10);

		// Test de l'insertion
		objetNonConformeDao.save(o1);
		assertNotNull(o1.getObjetNonConformeId());
		final Integer oId = o1.getObjetNonConformeId();

		final ObjetNonConforme o2 = objetNonConformeDao.findById(oId).get();
		// Vérification des données entrées dans la base
		assertNotNull(o2);
		assertNotNull(o2.getNonConformite());
		assertNotNull(o2.getEntite());
		assertNotNull(o2.getObjetId() == 10);
		assertTrue(o2.getObjetNonConformeId() == oId);

		// Test de la mise à jour
		o2.setObjetId(15);
		objetNonConformeDao.save(o2);
		assertTrue(objetNonConformeDao.findById(oId).get().getObjetId() == 15);

		// Test de la délétion
		objetNonConformeDao.deleteById(oId);
		assertFalse(objetNonConformeDao.findById(oId).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final Integer obj1 = 1;
		final Integer obj2 = 2;
		final Entite e1 = entiteDao.findById(1).get();
		final Entite e2 = entiteDao.findById(2).get();
		final NonConformite n1 = nonConformiteDao.findById(1).get();
		final NonConformite n2 = nonConformiteDao.findById(2).get();
		final ObjetNonConforme o1 = new ObjetNonConforme();
		final ObjetNonConforme o2 = new ObjetNonConforme();

		// L'objet 1 n'est pas égal à null
		assertFalse(o1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(o1.equals(o1));

		/* null */
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));

		/* ObjetId */
		o2.setObjetId(obj1);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setObjetId(obj2);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setObjetId(obj1);
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));

		/* Entite */
		o2.setEntite(e1);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setEntite(e2);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setEntite(e1);
		assertTrue(o1.equals(o2));

		/* Non conformité */
		o2.setNonConformite(n1);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setNonConformite(n2);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setNonConformite(n1);
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));

		final Categorie c3 = new Categorie();
		assertFalse(o1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final Integer obj1 = 1;
		final Integer obj2 = 2;
		final Entite e1 = entiteDao.findById(1).get();
		final Entite e2 = entiteDao.findById(2).get();
		final NonConformite n1 = nonConformiteDao.findById(1).get();
		final NonConformite n2 = nonConformiteDao.findById(2).get();
		final ObjetNonConforme o1 = new ObjetNonConforme();
		final ObjetNonConforme o2 = new ObjetNonConforme();

		/* null */
		assertTrue(o1.hashCode() == o2.hashCode());

		/* Obj Id */
		o2.setObjetId(obj1);
		assertFalse(o1.hashCode() == o2.hashCode());
		o1.setObjetId(obj2);
		assertFalse(o1.hashCode() == o2.hashCode());
		o1.setObjetId(obj1);
		assertTrue(o1.hashCode() == o2.hashCode());

		/* Entite */
		o2.setEntite(e1);
		assertFalse(o1.hashCode() == o2.hashCode());
		o1.setEntite(e2);
		assertFalse(o1.hashCode() == o2.hashCode());
		o1.setEntite(e1);
		assertTrue(o1.hashCode() == o2.hashCode());

		/* Non conformité */
		o2.setNonConformite(n1);
		assertFalse(o1.hashCode() == o2.hashCode());
		o1.setNonConformite(n2);
		assertFalse(o1.hashCode() == o2.hashCode());
		o1.setNonConformite(n1);
		assertTrue(o1.hashCode() == o2.hashCode());

		// un même objet garde le même hashcode dans le temps
		final int hash = o1.hashCode();
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final ObjetNonConforme o1 = objetNonConformeDao.findById(1).get();
		assertTrue(o1.toString().equals("{" + o1.getObjetId() + ", " + o1.getEntite().getNom() + "(Entite), "
				+ o1.getNonConformite().getNom() + "(NonConformite)}"));

		final ObjetNonConforme o2 = new ObjetNonConforme();
		assertTrue(o2.toString().equals("{Empty ObjetNonConforme}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final ObjetNonConforme o1 = objetNonConformeDao.findById(1).get();
		final ObjetNonConforme o2 = o1.clone();
		assertTrue(o1.equals(o2));

		if (o1.getObjetNonConformeId() != null) {
			assertTrue(o1.getObjetNonConformeId() == o2.getObjetNonConformeId());
		} else {
			assertNull(o2.getObjetNonConformeId());
		}

		if (o1.getObjetId() != null) {
			assertTrue(o1.getObjetId() == o2.getObjetId());
		} else {
			assertNull(o2.getObjetId());
		}

		if (o1.getNonConformite() != null) {
			assertTrue(o1.getNonConformite().equals(o2.getNonConformite()));
		} else {
			assertNull(o2.getNonConformite());
		}

		if (o1.getEntite() != null) {
			assertTrue(o1.getEntite().equals(o2.getEntite()));
		} else {
			assertNull(o2.getEntite());
		}
	}

	@Test
	public void testFindObjetIdsByNonConformites() {
		final List<NonConformite> noconfs = new ArrayList<>();
		noconfs.add(nonConformiteDao.findById(6).get());
		List<Integer> ids = objetNonConformeDao.findObjetIdsByNonConformites(noconfs);
		assertTrue(ids.size() == 2);
		assertTrue(ids.contains(2));
		assertTrue(ids.contains(3));

		noconfs.add(nonConformiteDao.findById(1).get());
		ids = objetNonConformeDao.findObjetIdsByNonConformites(noconfs);
		assertTrue(ids.size() == 2);
		assertTrue(ids.contains(2));
		assertTrue(ids.contains(3));

		noconfs.remove(nonConformiteDao.findById(6).get());
		ids = objetNonConformeDao.findObjetIdsByNonConformites(noconfs);
		assertTrue(ids.size() == 1);
		assertTrue(ids.contains(2));
	}
}
