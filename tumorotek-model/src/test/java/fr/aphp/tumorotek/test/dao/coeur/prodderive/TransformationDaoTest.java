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
package fr.aphp.tumorotek.test.dao.coeur.prodderive;

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

import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO TransformationDao et le bean du domaine
 * Transformation. Classe créée le 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class TransformationDaoTest extends AbstractDaoTest {

	@Autowired
	TransformationDao transformationDao;
	
	@Autowired
	EntiteDao entiteDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<Transformation> transfos = IterableUtils.toList(transformationDao.findAll());
		assertTrue(transfos.size() == 5);
	}

	/**
	 * Test l'appel de la méthode findByEntiteObjet().
	 */
	@Test
	public void testFindByEntiteObjet() {
		final Entite e = entiteDao.findById(3).get();
		List<Transformation> transfos = transformationDao.findByEntiteObjet(e, 1);
		assertTrue(transfos.size() == 2);
		transfos = transformationDao.findByEntiteObjet(e, 2);
		assertTrue(transfos.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<Transformation> transfos = transformationDao.findByExcludedId(1);
		assertTrue(transfos.size() == 4);

		transfos = transformationDao.findByExcludedId(10);
		assertTrue(transfos.size() == 5);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une Transformation.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final Transformation t = new Transformation();
		final Entite e = entiteDao.findById(1).get();
		final Integer objId = 4;

		t.setEntite(e);
		t.setObjetId(1);
		t.setQuantite(null);

		// Test de l'insertion
		transformationDao.save(t);
		assertEquals(new Integer(6), t.getTransformationId());

		// Test de la mise à jour
		final Transformation t2 = transformationDao.findById(new Integer(6)).get();
		assertNull(t2.getQuantite());
		t2.setQuantite((float) 15.1456);
		assertNotNull(t2);
		assertTrue(t2.getObjetId() == 1);
		t2.setObjetId(objId);
		transformationDao.save(t2);
		assertTrue(transformationDao.findById(new Integer(6)).get().getObjetId() == objId);
		assertTrue(transformationDao.findById(new Integer(6)).get().getQuantite() == (float) 15.146);

		// Test de la délétion
		transformationDao.deleteById(new Integer(6));
		assertFalse(transformationDao.findById(new Integer(6)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final Integer objId1 = 1;
		final Integer objId2 = 2;
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(4).get();
		final Transformation t1 = new Transformation();
		t1.setEntite(e1);
		t1.setObjetId(objId1);
		final Transformation t2 = new Transformation();
		t2.setEntite(e1);
		t2.setObjetId(objId1);

		// L'objet 1 n'est pas égal à null
		assertFalse(t1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(t1.equals(t1));
		// 2 objets sont égaux entre eux
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));

		t1.setObjetId(null);
		t1.setEntite(null);
		t2.setObjetId(null);
		t2.setEntite(null);
		assertTrue(t1.equals(t2));
		t2.setEntite(e1);
		assertFalse(t1.equals(t2));
		t1.setEntite(e1);
		assertTrue(t1.equals(t2));
		t2.setEntite(e2);
		assertFalse(t1.equals(t2));
		t2.setEntite(null);
		assertFalse(t1.equals(t2));
		t2.setObjetId(objId1);
		assertFalse(t1.equals(t2));

		t1.setObjetId(objId1);
		t1.setEntite(null);
		t2.setEntite(null);
		t2.setObjetId(objId1);
		assertTrue(t1.equals(t2));
		t2.setObjetId(objId2);
		assertFalse(t1.equals(t2));
		t2.setEntite(e1);
		assertFalse(t1.equals(t2));

		// Vérification de la différenciation de 2 objets
		t1.setEntite(e1);
		assertFalse(t1.equals(t2));
		t2.setEntite(e2);
		t2.setObjetId(objId1);
		assertFalse(t1.equals(t2));

		final Categorie c = new Categorie();
		assertFalse(t1.equals(c));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final Integer objId1 = 1;
		final Entite e1 = entiteDao.findById(3).get();
		final Transformation t1 = new Transformation();
		t1.setEntite(e1);
		t1.setObjetId(objId1);
		final Transformation t2 = new Transformation();
		t2.setEntite(e1);
		t2.setObjetId(objId1);
		final Transformation t3 = new Transformation();
		t3.setEntite(null);
		t3.setObjetId(null);
		assertTrue(t3.hashCode() > 0);

		final int hash = t1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(t1.hashCode() == t2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());

	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Transformation t1 = transformationDao.findById(1).get();
		assertTrue(t1.toString().equals("{" + t1.getObjetId() + ", " + t1.getEntite().toString() + "(Entite)}"));

		final Transformation t2 = new Transformation();
		assertTrue(t2.toString().equals("{Empty transformation}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final Transformation t1 = transformationDao.findById(1).get();
		final Transformation t2 = t1.clone();
		assertTrue(t1.equals(t2));

		if (t1.getTransformationId() != null) {
			assertTrue(t1.getTransformationId().equals(t2.getTransformationId()));
		} else {
			assertNull(t2.getTransformationId());
		}
		if (t1.getObjetId() != null) {
			assertTrue(t1.getObjetId().equals(t2.getObjetId()));
		} else {
			assertNull(t2.getObjetId());
		}
		if (t1.getEntite() != null) {
			assertTrue(t1.getEntite().equals(t2.getEntite()));
		} else {
			assertNull(t2.getEntite());
		}
		if (t1.getQuantite() != null) {
			assertTrue(t1.getQuantite().equals(t2.getQuantite()));
		} else {
			assertNull(t2.getQuantite());
		}
		if (t1.getQuantiteUnite() != null) {
			assertTrue(t1.getQuantiteUnite().equals(t2.getQuantiteUnite()));
		} else {
			assertNull(t2.getQuantiteUnite());
		}
		/*
		 * if (t1.getVolume() != null) {
		 * assertTrue(t1.getVolume().equals(t2.getVolume())); } else {
		 * assertNull(t2.getVolume()); } if (t1.getVolumeUnite() != null) {
		 * assertTrue(t1.getVolumeUnite().equals(t2.getVolumeUnite())); } else {
		 * assertNull(t2.getVolumeUnite()); }
		 */
	}
}
