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
package fr.aphp.tumorotek.dao.test.stats;

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
import fr.aphp.tumorotek.dao.test.Config;

import fr.aphp.tumorotek.dao.stats.SModeleDao;
import fr.aphp.tumorotek.dao.stats.SubdivisionDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.Subdivision;

/**
 *
 * Classe de test pour le DAO Subdivision et le bean du domaine Subdivision.
 * Date: 24/07/2015
 *
 * @author Marc Deschamps
 * @version 2.0.10
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class SubdivisionDaoTest extends AbstractDaoTest {

	@Autowired
	SubdivisionDao subdivisionDao;

	@Autowired
	SModeleDao sModeleDao;

	public SubdivisionDaoTest() {
	}

	/**
	 * Test l'appel de la méthode findById().
	 */
	@Test
	public void testFindById() {
		Subdivision s = subdivisionDao.findById(1).get();
		assertNotNull(s);
		assertTrue(s.getNom().equals("nature"));

		assertFalse(subdivisionDao.findById(100).isPresent());
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testFindAll() {
		final List<Subdivision> liste = IterableUtils.toList(IterableUtils.toList(subdivisionDao.findAll()));
		assertTrue(liste.size() == 3);
	}

	@Test
	public void testFindByModele() {
		final SModele sM1 = sModeleDao.findById(1).get();
		final List<Subdivision> subdivs = subdivisionDao.findByModele(sM1);
		assertTrue(subdivs.size() == 1);
		assertTrue(subdivs.contains(subdivisionDao.findById(1).get()));
		final SModele sM2 = sModeleDao.findById(2).get();
		assertTrue(subdivisionDao.findByModele(sM2).isEmpty());
		assertTrue(subdivisionDao.findByModele(null).isEmpty());

	}

}
