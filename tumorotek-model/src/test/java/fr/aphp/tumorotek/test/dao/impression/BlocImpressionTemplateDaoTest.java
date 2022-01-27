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
import fr.aphp.tumorotek.dao.impression.BlocImpressionTemplateDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO BlocImpressionTemplateDao et le bean du domaine
 * BlocImpressionTemplate.
 *
 * @author Pierre Ventadour.
 * @version 23/07/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class BlocImpressionTemplateDaoTest extends AbstractDaoTest {

	@Autowired
	BlocImpressionTemplateDao blocImpressionTemplateDao;

	@Autowired
	TemplateDao templateDao;

	@Autowired
	BlocImpressionDao blocImpressionDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<BlocImpressionTemplate> liste = IterableUtils.toList(blocImpressionTemplateDao.findAll());
		assertTrue(liste.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findById().
	 */
	@Test
	public void testFindById() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final Template t1 = templateDao.findById(1).get();
		BlocImpressionTemplatePK pk = new BlocImpressionTemplatePK(t1, b1);

		BlocImpressionTemplate bit = blocImpressionTemplateDao.findById(pk).get();
		assertNotNull(bit);

		final Template t2 = templateDao.findById(2).get();
		pk = new BlocImpressionTemplatePK(t2, b1);
		assertFalse(blocImpressionTemplateDao.findById(pk).isPresent());
	}

	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	@Test
	public void testFindByExcludedPK() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final Template t1 = templateDao.findById(1).get();
		BlocImpressionTemplatePK pk = new BlocImpressionTemplatePK(t1, b1);

		List<BlocImpressionTemplate> liste = blocImpressionTemplateDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 3);

		final Template t2 = templateDao.findById(2).get();
		pk = new BlocImpressionTemplatePK(t2, b1);
		liste = blocImpressionTemplateDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findByTemplate().
	 */
	@Test
	public void testFindByTemplate() {
		final Template t1 = templateDao.findById(1).get();
		List<BlocImpressionTemplate> liste = blocImpressionTemplateDao.findByTemplate(t1);
		assertTrue(liste.size() == 4);

		final Template t2 = templateDao.findById(2).get();
		liste = blocImpressionTemplateDao.findByTemplate(t2);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un
	 * BlocImpressionTemplate.
	 **/
	@Rollback(false)
	@Test
	public void testCrud() {

		final BlocImpressionTemplate bit = new BlocImpressionTemplate();
		final BlocImpression b1 = blocImpressionDao.findById(4).get();
		final Template t1 = templateDao.findById(1).get();
		final Integer ordre = 5;
		final Integer ordreUp = 6;

		bit.setBlocImpression(b1);
		bit.setTemplate(t1);
		bit.setOrdre(ordre);

		// Test de l'insertion
		blocImpressionTemplateDao.save(bit);
		assertTrue(IterableUtils.toList(blocImpressionTemplateDao.findAll()).size() == 5);

		// Test de la mise à jour
		final BlocImpressionTemplatePK pk = new BlocImpressionTemplatePK();
		pk.setBlocImpression(b1);
		pk.setTemplate(t1);
		final BlocImpressionTemplate bit2 = blocImpressionTemplateDao.findById(pk).get();
		assertNotNull(bit2);
		assertTrue(bit2.getTemplate().equals(t1));
		assertTrue(bit2.getBlocImpression().equals(b1));
		assertTrue(bit2.getOrdre() == ordre);

		// update
		bit2.setOrdre(ordreUp);
		blocImpressionTemplateDao.save(bit2);
		assertTrue(blocImpressionTemplateDao.findById(pk).get().equals(bit2));
		assertTrue(blocImpressionTemplateDao.findById(pk).get().getOrdre() == ordreUp);
		assertTrue(IterableUtils.toList(blocImpressionTemplateDao.findAll()).size() == 5);

		// Test de la délétion
		blocImpressionTemplateDao.deleteById(pk);
		assertFalse(blocImpressionTemplateDao.findById(pk).isPresent());
		assertTrue(IterableUtils.toList(blocImpressionTemplateDao.findAll()).size() == 4);
	}

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {

		final BlocImpressionTemplate bit1 = new BlocImpressionTemplate();
		final BlocImpressionTemplate bit2 = new BlocImpressionTemplate();

		// L'objet 1 n'est pas égal à null
		assertFalse(bit1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(bit1.equals(bit1));
		// 2 objets sont égaux entre eux
		assertTrue(bit1.equals(bit2));
		assertTrue(bit2.equals(bit1));

		populateClefsToTestEqualsAndHashCode(bit1, bit2);

		// dummy test
		final Banque b = new Banque();
		assertFalse(bit1.equals(b));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final BlocImpressionTemplate bit1 = new BlocImpressionTemplate();
		final BlocImpressionTemplate bit2 = new BlocImpressionTemplate();
		final BlocImpressionTemplate bit3 = new BlocImpressionTemplate();

		assertTrue(bit1.hashCode() == bit2.hashCode());
		assertTrue(bit2.hashCode() == bit3.hashCode());
		assertTrue(bit3.hashCode() > 0);

		// teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(bit1, bit2);

		final int hash = bit1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(bit1.hashCode() == bit2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == bit1.hashCode());
		assertTrue(hash == bit1.hashCode());
		assertTrue(hash == bit1.hashCode());
		assertTrue(hash == bit1.hashCode());
	}

	private void populateClefsToTestEqualsAndHashCode(final BlocImpressionTemplate bit1,
			final BlocImpressionTemplate bit2) throws ParseException {

		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final BlocImpression b2 = blocImpressionDao.findById(2).get();
		final Template t1 = templateDao.findById(1).get();
		final BlocImpressionTemplatePK pk1 = new BlocImpressionTemplatePK(t1, b1);
		final BlocImpressionTemplatePK pk2 = new BlocImpressionTemplatePK(t1, b2);
		final BlocImpressionTemplatePK pk3 = new BlocImpressionTemplatePK(t1, b1);
		final BlocImpressionTemplatePK[] pks = new BlocImpressionTemplatePK[] { null, pk1, pk2, pk3 };

		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {

				bit1.setPk(pks[i]);
				bit2.setPk(pks[k]);

				if (((i == k) || (i + k == 4))) {
					assertTrue(bit1.equals(bit2));
					assertTrue(bit1.hashCode() == bit2.hashCode());
				} else {
					assertFalse(bit1.equals(bit2));
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
		final Template t1 = templateDao.findById(1).get();
		final BlocImpressionTemplatePK pk1 = new BlocImpressionTemplatePK(t1, b1);
		final BlocImpressionTemplate bit1 = blocImpressionTemplateDao.findById(pk1).get();

		assertTrue(bit1.toString().equals("{" + bit1.getPk().toString() + "}"));

		final BlocImpressionTemplate bit2 = new BlocImpressionTemplate();
		bit2.setPk(null);
		assertTrue(bit2.toString().equals("{Empty BlocImpressionTemplate}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final BlocImpression b1 = blocImpressionDao.findById(1).get();
		final Template t1 = templateDao.findById(1).get();
		final BlocImpressionTemplatePK pk1 = new BlocImpressionTemplatePK(t1, b1);
		final BlocImpressionTemplate bit1 = blocImpressionTemplateDao.findById(pk1).get();

		BlocImpressionTemplate bit2 = new BlocImpressionTemplate();
		bit2 = bit1.clone();

		assertTrue(bit1.equals(bit2));

		if (bit1.getPk() != null) {
			assertTrue(bit1.getPk().equals(bit2.getPk()));
		} else {
			assertNull(bit2.getPk());
		}

		if (bit1.getOrdre() != null) {
			assertTrue(bit1.getOrdre() == bit2.getOrdre());
		} else {
			assertNull(bit2.getOrdre());
		}
	}
}
