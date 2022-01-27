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
package fr.aphp.tumorotek.test.dao.impression;

import java.text.ParseException;
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

import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.impression.ChampEntiteBlocDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampEntiteBloc;
import fr.aphp.tumorotek.model.impression.ChampEntiteBlocPK;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ChampEntiteBlocDao et le bean du domaine
 * ChampEntiteBloc.
 *
 * @author Pierre Ventadour.
 * @version 30/07/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ChampEntiteBlocDaoTest extends AbstractDaoTest {

	@Autowired
	ChampEntiteBlocDao champEntiteBlocDao;

	@Autowired
	ChampEntiteDao champEntiteDao;

	@Autowired
	BlocImpressionDao blocImpressionDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<ChampEntiteBloc> liste = IterableUtils.toList(champEntiteBlocDao.findAll());
		assertTrue(liste.size() > 0);
	}

	/**
	 * Test l'appel de la méthode findById().
	 */
	@Test
	public void testFindById() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final ChampEntite ce23 = champEntiteDao.findById(23).get();
		ChampEntiteBlocPK pk = new ChampEntiteBlocPK(b1, ce23);

		ChampEntiteBloc ceb = champEntiteBlocDao.findById(pk).get();
		assertNotNull(ceb);

		final ChampEntite ce1 = champEntiteDao.findById(1).get();
		pk = new ChampEntiteBlocPK(b1, ce1);
		assertFalse(champEntiteBlocDao.findById(pk).isPresent());
	}

	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	@Test
	public void testFindByExcludedPK() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final ChampEntite ce23 = champEntiteDao.findById(23).get();
		ChampEntiteBlocPK pk = new ChampEntiteBlocPK(b1, ce23);

		List<ChampEntiteBloc> liste = champEntiteBlocDao.findByExcludedPK(pk);
		assertTrue(liste.size() == IterableUtils.toList(champEntiteBlocDao.findAll()).size() - 1);

		final ChampEntite ce1 = champEntiteDao.findById(1).get();
		pk = new ChampEntiteBlocPK(b1, ce1);
		liste = champEntiteBlocDao.findByExcludedPK(pk);
		assertTrue(liste.size() == IterableUtils.toList(champEntiteBlocDao.findAll()).size());
	}

	/**
	 * Test l'appel de la méthode findByBlocImpression().
	 */
	@Test
	public void testFindByBlocImpression() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		List<ChampEntiteBloc> liste = champEntiteBlocDao.findByBlocImpression(b1);
		assertTrue(liste.size() == 3);

		final BlocImpression b6 = blocImpressionDao.findById(6).get();
		liste = champEntiteBlocDao.findByBlocImpression(b6);
		assertTrue(liste.size() == 7);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un
	 * BlocImpressionTemplate.
	 **/
	@Rollback(false)
	@Test
	public void testCrud() {

		final ChampEntiteBloc ceb = new ChampEntiteBloc();
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final ChampEntite ce1 = champEntiteDao.findById(1).get();
		final Integer ordre = 5;
		final Integer ordreUp = 6;

		ceb.setBlocImpression(b1);
		ceb.setChampEntite(ce1);
		ceb.setOrdre(ordre);

		// Test de l'insertion
		champEntiteBlocDao.save(ceb);

		// Test de la mise à jour
		final ChampEntiteBlocPK pk = new ChampEntiteBlocPK();
		pk.setBlocImpression(b1);
		pk.setChampEntite(ce1);
		final ChampEntiteBloc ceb2 = champEntiteBlocDao.findById(pk).get();
		assertNotNull(ceb2);
		assertTrue(ceb2.getChampEntite().equals(ce1));
		assertTrue(ceb2.getBlocImpression().equals(b1));
		assertTrue(ceb2.getOrdre() == ordre);

		// update
		ceb2.setOrdre(ordreUp);
		champEntiteBlocDao.save(ceb2);
		assertTrue(champEntiteBlocDao.findById(pk).get().equals(ceb2));
		assertTrue(champEntiteBlocDao.findById(pk).get().getOrdre() == ordreUp);

		// Test de la délétion
		champEntiteBlocDao.deleteById(pk);
		assertFalse(champEntiteBlocDao.findById(pk).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {

		final ChampEntiteBloc ceb1 = new ChampEntiteBloc();
		final ChampEntiteBloc ceb2 = new ChampEntiteBloc();

		// L'objet 1 n'est pas égal à null
		assertFalse(ceb1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(ceb1.equals(ceb1));
		// 2 objets sont égaux entre eux
		assertTrue(ceb1.equals(ceb2));
		assertTrue(ceb2.equals(ceb1));

		populateClefsToTestEqualsAndHashCode(ceb1, ceb2);

		// dummy test
		final Banque b = new Banque();
		assertFalse(ceb1.equals(b));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final ChampEntiteBloc ceb1 = new ChampEntiteBloc();
		final ChampEntiteBloc ceb2 = new ChampEntiteBloc();
		final ChampEntiteBloc ceb3 = new ChampEntiteBloc();

		assertTrue(ceb1.hashCode() == ceb2.hashCode());
		assertTrue(ceb2.hashCode() == ceb3.hashCode());
		assertTrue(ceb3.hashCode() > 0);

		// teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(ceb1, ceb2);

		final int hash = ceb1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(ceb1.hashCode() == ceb2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == ceb1.hashCode());
		assertTrue(hash == ceb1.hashCode());
		assertTrue(hash == ceb1.hashCode());
		assertTrue(hash == ceb1.hashCode());
	}

	private void populateClefsToTestEqualsAndHashCode(final ChampEntiteBloc ceb1, final ChampEntiteBloc ceb2)
			throws ParseException {

		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final BlocImpression b2 = blocImpressionDao.findById(2).get();
		final ChampEntite ce1 = champEntiteDao.findById(1).get();
		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK(b1, ce1);
		final ChampEntiteBlocPK pk2 = new ChampEntiteBlocPK(b2, ce1);
		final ChampEntiteBlocPK pk3 = new ChampEntiteBlocPK(b1, ce1);
		final ChampEntiteBlocPK[] pks = new ChampEntiteBlocPK[] { null, pk1, pk2, pk3 };

		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {

				ceb1.setPk(pks[i]);
				ceb2.setPk(pks[k]);

				if (((i == k) || (i + k == 4))) {
					assertTrue(ceb1.equals(ceb2));
					assertTrue(ceb1.hashCode() == ceb2.hashCode());
				} else {
					assertFalse(ceb1.equals(ceb2));
				}
			}
		}
	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final ChampEntite ce1 = champEntiteDao.findById(23).get();
		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK(b1, ce1);
		final ChampEntiteBloc ceb1 = champEntiteBlocDao.findById(pk1).get();

		assertTrue(ceb1.toString().equals("{" + ceb1.getPk().toString() + "}"));

		final ChampEntiteBloc ceb2 = new ChampEntiteBloc();
		ceb2.setPk(null);
		assertTrue(ceb2.toString().equals("{Empty ChampEntiteBloc}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final ChampEntite ce1 = champEntiteDao.findById(23).get();
		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK(b1, ce1);
		final ChampEntiteBloc ceb1 = champEntiteBlocDao.findById(pk1).get();

		ChampEntiteBloc ceb2 = new ChampEntiteBloc();
		ceb2 = ceb1.clone();

		assertTrue(ceb1.equals(ceb2));

		if (ceb1.getPk() != null) {
			assertTrue(ceb1.getPk().equals(ceb2.getPk()));
		} else {
			assertNull(ceb2.getPk());
		}

		if (ceb1.getOrdre() != null) {
			assertTrue(ceb1.getOrdre() == ceb2.getOrdre());
		} else {
			assertNull(ceb2.getOrdre());
		}
	}

}
