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
package fr.aphp.tumorotek.test.dao.utilisateur;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.DroitObjetDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.DroitObjetPK;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO DroitObjetDao et le bean du domaine DroitObjet.
 *
 * @author Pierre Ventadour.
 * @version 18/05/2010.
 *
 */
@Transactional
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class DroitObjetDaoTest extends AbstractDaoTest {

	@Autowired
	DroitObjetDao droitObjetDao;
	
	@Autowired
	ProfilDao profilDao;
	
	@Autowired
	EntiteDao entiteDao;
	
	@Autowired
	OperationTypeDao operationTypeDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<DroitObjet> liste = IterableUtils.toList(IterableUtils.toList(droitObjetDao.findAll()));
		assertTrue(liste.size() == 9);
	}

	/**
	 * Test l'appel de la méthode findById().
	 */
	@Test
	public void testFindById() {
		final Profil p1 = profilDao.findById(1).get();
		final Entite e1 = entiteDao.findById(1).get();
		final Entite e5 = entiteDao.findById(5).get();
		final OperationType o1 = operationTypeDao.findById(1).get();
		DroitObjetPK pk = new DroitObjetPK(p1, e1, o1);

		DroitObjet dO = droitObjetDao.findById(pk).get();
		assertNotNull(dO);

		pk = new DroitObjetPK(p1, e5, o1);
		assertFalse(droitObjetDao.findById(pk).isPresent());
	}

	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	@Test
	public void testFindByExcludedPK() {
		final Profil p1 = profilDao.findById(1).get();
		final Entite e1 = entiteDao.findById(1).get();
		final Entite e5 = entiteDao.findById(5).get();
		final OperationType o1 = operationTypeDao.findById(1).get();
		DroitObjetPK pk = new DroitObjetPK(p1, e1, o1);

		List<DroitObjet> liste = droitObjetDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 8);

		pk = new DroitObjetPK(p1, e5, o1);
		liste = droitObjetDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 9);
	}

	/**
	 * Test l'appel de la méthode findByProfil().
	 */
	@Test
	public void testFindByProfil() {
		final Profil p1 = profilDao.findById(1).get();
		final Profil p3 = profilDao.findById(3).get();

		List<DroitObjet> liste = droitObjetDao.findByProfil(p1);
		assertTrue(liste.size() == 3);

		liste = droitObjetDao.findByProfil(p3);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByProfilEntite().
	 */
	@Test
	public void testFindByProfilEntite() {
		final Profil p2 = profilDao.findById(2).get();
		final Profil p3 = profilDao.findById(3).get();
		final Entite e2 = entiteDao.findById(2).get();
		final Entite e3 = entiteDao.findById(3).get();

		List<DroitObjet> liste = droitObjetDao.findByProfilEntite(p2, e2);
		assertTrue(liste.size() == 3);

		liste = droitObjetDao.findByProfilEntite(p2, e3);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilEntite(p3, e2);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilEntite(p2, null);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilEntite(null, e2);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilEntite(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByProfilOperation().
	 */
	@Test
	public void testFindByProfilOperation() {
		final Profil p2 = profilDao.findById(2).get();
		final Profil p3 = profilDao.findById(3).get();
		final OperationType o1 = operationTypeDao.findById(1).get();
		final OperationType o2 = operationTypeDao.findById(2).get();

		List<DroitObjet> liste = droitObjetDao.findByProfilOperation(p2, o1);
		assertTrue(liste.size() == 2);

		liste = droitObjetDao.findByProfilOperation(p2, o2);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilOperation(p3, o1);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilOperation(p3, null);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilOperation(null, o1);
		assertTrue(liste.size() == 0);

		liste = droitObjetDao.findByProfilOperation(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un DroitObjet.
	 **/
	@Rollback(false)
	@Test
	public void testCrud() {

		final DroitObjet dO = new DroitObjet();
		final Profil p = profilDao.findById(1).get();
		final Entite e = entiteDao.findById(5).get();
		final OperationType op = operationTypeDao.findById(1).get();

		dO.setProfil(p);
		dO.setEntite(e);
		dO.setOperationType(op);

		// Test de l'insertion
		droitObjetDao.save(dO);
		assertTrue(IterableUtils.toList(IterableUtils.toList(droitObjetDao.findAll())).size() == 10);

		// Test de la mise à jour
		final DroitObjetPK pk = new DroitObjetPK();
		pk.setProfil(p);
		pk.setEntite(e);
		pk.setOperationType(op);
		final DroitObjet doUp = droitObjetDao.findById(pk).get();
		assertNotNull(doUp);
		assertNotNull(doUp.getProfil());
		assertNotNull(doUp.getEntite());
		assertNotNull(doUp.getOperationType());

		// Test de la délétion
		droitObjetDao.deleteById(pk);
		assertFalse(droitObjetDao.findById(pk).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {

		final DroitObjet do1 = new DroitObjet();
		final DroitObjet do2 = new DroitObjet();

		// L'objet 1 n'est pas égal à null
		assertFalse(do1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(do1.equals(do1));
		// 2 objets sont égaux entre eux
		assertTrue(do1.equals(do2));
		assertTrue(do2.equals(do1));

		populateClefsToTestEqualsAndHashCode(do1, do2);

		// dummy test
		final Banque b = new Banque();
		assertFalse(do1.equals(b));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final DroitObjet do1 = new DroitObjet();
		final DroitObjet do2 = new DroitObjet();
		final DroitObjet do3 = new DroitObjet();

		assertTrue(do1.hashCode() == do2.hashCode());
		assertTrue(do2.hashCode() == do3.hashCode());
		assertTrue(do3.hashCode() > 0);

		// teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(do1, do2);

		final int hash = do1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(do1.hashCode() == do2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == do1.hashCode());
		assertTrue(hash == do1.hashCode());
		assertTrue(hash == do1.hashCode());
		assertTrue(hash == do1.hashCode());
	}

	private void populateClefsToTestEqualsAndHashCode(final DroitObjet do1, final DroitObjet do2) throws ParseException {

		final Profil p = profilDao.findById(1).get();
		final OperationType op = operationTypeDao.findById(1).get();
		final Entite e1 = entiteDao.findById(4).get();
		final Entite e2 = entiteDao.findById(5).get();
		final DroitObjetPK pk1 = new DroitObjetPK(p, e1, op);
		final DroitObjetPK pk2 = new DroitObjetPK(p, e2, op);
		final DroitObjetPK pk3 = new DroitObjetPK(p, e1, op);
		final DroitObjetPK[] pks = new DroitObjetPK[] { null, pk1, pk2, pk3 };

		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {

				do1.setPk(pks[i]);
				do2.setPk(pks[k]);

				if (((i == k) || (i + k == 4))) {
					assertTrue(do1.equals(do2));
					assertTrue(do1.hashCode() == do2.hashCode());
				} else {
					assertFalse(do1.equals(do2));
				}
			}
		}
	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Profil p = profilDao.findById(1).get();
		final OperationType op = operationTypeDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		final DroitObjetPK pk1 = new DroitObjetPK(p, e1, op);
		final DroitObjet do1 = droitObjetDao.findById(pk1).get();

		assertTrue(do1.toString().equals("{" + do1.getPk().toString() + "}"));

		final DroitObjet do2 = new DroitObjet();
		do2.setPk(null);
		assertTrue(do2.toString().equals("{Empty DroitObjet}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final Profil p = profilDao.findById(1).get();
		final OperationType op = operationTypeDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		final DroitObjetPK pk1 = new DroitObjetPK(p, e1, op);
		final DroitObjet do1 = droitObjetDao.findById(pk1).get();

		DroitObjet do2 = new DroitObjet();
		do2 = do1.clone();

		assertTrue(do1.equals(do2));

		if (do1.getPk() != null) {
			assertTrue(do1.getPk().equals(do2.getPk()));
		} else {
			assertNull(do2.getPk());
		}

	}

}
