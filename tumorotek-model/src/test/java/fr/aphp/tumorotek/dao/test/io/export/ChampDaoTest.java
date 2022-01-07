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
package fr.aphp.tumorotek.dao.test.io.export;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

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

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.Config;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Classe de test pour le DAO ChampDao et le bean du domaine Champ. Classe de
 * test créée le 23/10/09. Modifiée le 25/11/09 par Maxime GOUSSEAU (Création de
 * ChampEntite)
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ChampDaoTest extends AbstractDaoTest {

	@Autowired
	ChampDao champDao;

	@Autowired
	ChampEntiteDao champEntiteDao;

	@Autowired
	ChampAnnotationDao champAnnotationDao;

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un champ.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Test
	@Rollback(false)
	@Transactional
	public void testCrudChamp() throws Exception {
		final int champEntiteId = 200;

		final ChampAnnotation champAnnotation = null;
		final ChampEntite champEntite = champEntiteDao.findById(champEntiteId).get();

		final Champ c = new Champ();
		c.setChampAnnotation(champAnnotation);
		c.setChampEntite(champEntite);

		// Test de l'insertion
		Integer idObject = new Integer(-1);
		this.champDao.save(c);
		final List<Champ> champs = IterableUtils.toList(this.champDao.findAll());
		final Iterator<Champ> itChamp = champs.iterator();
		boolean found = false;
		while (itChamp.hasNext()) {
			final Champ temp = itChamp.next();
			if (temp.equals(c)) {
				found = true;
				idObject = temp.getChampId();
				break;
			}
		}
		assertTrue(found);

		// Test de la mise à jour
		final Champ c2 = this.champDao.findById(idObject).get();
		assertNotNull(c2);
		if (c2.getChampAnnotation() != null) {
			assertNotNull(c2.getChampAnnotation());
		}
		if (c2.getChampEntite() != null) {
			assertTrue(c2.getChampEntite().equals(champEntite));
		}

		final int updatedChampAnnotationId = 2;

		final ChampAnnotation updatedChampAnnotation = champAnnotationDao.findById(updatedChampAnnotationId).get();
		final ChampEntite updatedChampEntite = null;

		c2.setChampAnnotation(updatedChampAnnotation);
		c2.setChampEntite(updatedChampEntite);

		this.champDao.save(c2);
		if (this.champDao.findById(idObject).get().getChampAnnotation() != null) {
			assertTrue(this.champDao.findById(idObject).get().getChampAnnotation().equals(updatedChampAnnotation));
		} else {
			assertNull(updatedChampAnnotation);
		}
		if (this.champDao.findById(idObject).get().getChampEntite() != null) {
			assertTrue(this.champDao.findById(idObject).get().getChampEntite().equals(updatedChampEntite));
		} else {
			assertNull(updatedChampEntite);
		}
		// Test de la délétion
		this.champDao.deleteById(idObject);
		assertFalse(this.champDao.findById(idObject).isPresent());
	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final Champ c1 = champDao.findById(2).get();
		assertTrue(c1.toString().equals("Echantillon.Quantite"));

		final Champ c2 = new Champ();
		assertTrue(c2.toString().equals("{Empty Champ}"));
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		// On boucle sur les 4 possibilités
		for (int i = 0; i < Math.pow(2, 2); i++) {
			final Champ champ1 = new Champ();
			final Champ champ2 = new Champ();
			ChampAnnotation champAnnotation = null;
			if (i >= 2) {
				champAnnotation = champAnnotationDao.findById(2).get();
			}
			champ1.setChampAnnotation(champAnnotation);
			champ2.setChampAnnotation(champAnnotation);
			ChampEntite champEntite = null;
			final int toTest = i % 2;
			if (toTest > 0) {
				champEntite = champEntiteDao.findById(3).get();
			}
			champ1.setChampEntite(champEntite);
			champ2.setChampEntite(champEntite);
			// On compare les 2 champs
			assertTrue(champ1.equals(champ2));
		}
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		// On boucle sur les 4 possibilités
		for (int i = 0; i < Math.pow(2, 2); i++) {
			final Champ champ = new Champ();
			int hash = 1;
			ChampAnnotation champAnnotation = null;
			int hashChampAnnotation = 0;
			if (i >= 2) {
				champAnnotation = champAnnotationDao.findById(3).get();
				hashChampAnnotation = champAnnotation.hashCode();
			}
			final int toTest = i % 2;
			ChampEntite champEntite = null;
			int hashChampEntite = 0;
			if (toTest > 0) {
				champEntite = champEntiteDao.findById(2).get();
				hashChampEntite = champEntite.hashCode();
			}
			int hashChampDelegue = 0;
			// TODO utiliser des valeurs pour tester l'égalité du champ délégué
			hash = 31 * hash + hashChampAnnotation;
			hash = 31 * hash + hashChampEntite;
			hash = 31 * hash + hashChampDelegue;
			champ.setChampAnnotation(champAnnotation);
			champ.setChampEntite(champEntite);
			champ.setChampDelegue(null);
			// On vérifie que le hashCode est bon
			assertTrue(champ.hashCode() == hash);
			assertTrue(champ.hashCode() == hash);
			assertTrue(champ.hashCode() == hash);
		}
	}
}
