/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.dao.test.impression;

import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.impression.CleImpressionDao;
import fr.aphp.tumorotek.dao.test.Config;
import fr.aphp.tumorotek.model.impression.CleImpression;

/**
 * Classe de test pour le DAO CleImpressionDao
 *
 * @author ABO
 * @version 2.2.0
 * @since 2.2.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class CleImpressionDaoTest // extends AbstractInMemoryTests
{

	@Autowired
	CleImpressionDao cleImpressionDao;

	@Test
	public void testCreate() {
		final CleImpression cleImp1 = new CleImpression();
		cleImp1.setNom("[[cle1]]");
		cleImpressionDao.save(cleImp1);

		final Integer cleId = cleImp1.getCleId();
		final CleImpression insertedCle = cleImpressionDao.findById(cleId).get();

		Assert.assertEquals(cleImp1, insertedCle);
	}

	@Test
	public void testUpdate() {
		final String cleNom = "[[cle1]]";
		final CleImpression cleImp1 = new CleImpression();
		cleImp1.setNom("[[cle1]]");
		cleImpressionDao.save(cleImp1);

		final Integer cleId = cleImp1.getCleId();
		final CleImpression insertedCle = cleImpressionDao.findById(cleId).get();
		insertedCle.setNom("[[cle2]]");
		cleImpressionDao.save(insertedCle);

		final CleImpression modifiedCle = cleImpressionDao.findById(cleId).get();

		Assert.assertNotEquals(cleNom, modifiedCle.getNom());
	}

	@Test
	public void testDelete() {
		final CleImpression cleImp1 = new CleImpression();
		cleImp1.setNom("[[cle1]]");
		cleImpressionDao.save(cleImp1);

		final Integer cleId = cleImp1.getCleId();
		cleImpressionDao.deleteById(cleId);
		assertFalse(cleImpressionDao.findById(cleId).isPresent());

	}

	@Test
	public void testFindByName() {
		final CleImpression cleImp1 = new CleImpression();
		cleImp1.setNom("[[cle1]]");
		cleImpressionDao.save(cleImp1);

		final CleImpression cleImp2 = new CleImpression();
		cleImp2.setNom("[[cle1]]");
		cleImpressionDao.save(cleImp2);

		final CleImpression cleImp3 = new CleImpression();
		cleImp3.setNom("[[cle1]]");
		cleImpressionDao.save(cleImp3);

		final List<CleImpression> cleList = cleImpressionDao.findByName("[[cle1]]");

		Assert.assertEquals(3, cleList.size());
	}

	@Test
	public void zDBIntegrity() {
		final List<CleImpression> cleList = cleImpressionDao.findByName("[[cle1]]");
		cleList.addAll(cleImpressionDao.findByName("[[cle2]]"));

		Assert.assertEquals(0, cleList.size());
	}
}
