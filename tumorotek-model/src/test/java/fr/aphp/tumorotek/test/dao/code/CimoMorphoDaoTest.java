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
package fr.aphp.tumorotek.test.dao.code;

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

import fr.aphp.tumorotek.dao.code.CimoMorphoDao;
import fr.aphp.tumorotek.model.code.CimoMorpho;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.ConfigCodes;

/**
 *
 * Classe de test pour le DAO CimoMorphoDao et le bean du domaine CimoMorpho.
 * Classe de test créée le 21/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ConfigCodes.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class CimoMorphoDaoTest extends AbstractDaoTest {

	@Autowired
	CimoMorphoDao cimoMorphoDao;

	@Test
	public void testReadAllCategories() {
		final List<CimoMorpho> cimos = IterableUtils.toList(cimoMorphoDao.findAll());
		assertTrue(cimos.size() == 1161);
	}

	@Test
	public void testFindByCodeLike() {
		List<CimoMorpho> cimos = cimoMorphoDao.findByCodeLike("%D0-%");
		assertTrue(cimos.size() == 6);
		cimos = cimoMorphoDao.findByCodeLike("TEST");
		assertTrue(cimos.size() == 0);
	}

	@Test
	public void testFindByLibelleLike() {
		List<CimoMorpho> cimos = cimoMorphoDao.findByLibelleLike("%naevus%");
		assertTrue(cimos.size() == 8);
		cimos = cimoMorphoDao.findByLibelleLike("TEST");
		assertTrue(cimos.size() == 0);
	}

	@Test
	public void testFindByCimRefLike() {
		List<CimoMorpho> cimos = cimoMorphoDao.findByCimRefLike("%C44%");
		assertTrue(cimos.size() == 79);
		cimos = cimoMorphoDao.findByCimRefLike("TEST");
		assertTrue(cimos.size() == 0);
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final Integer id1 = 1;
		final Integer id2 = 2;
		final CimoMorpho c1 = new CimoMorpho();
		final CimoMorpho c2 = new CimoMorpho();

		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(c1.equals(c1));

		/* null --> Ids ne pouvant etre nuls car table systemes */
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));

		/* Id */
		c2.setCimoMorphoId(id1);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setCimoMorphoId(id2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setCimoMorphoId(id1);
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));

		final Categorie c = new Categorie();
		assertFalse(c1.equals(c));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {

		final String code1 = "C1";
		final CimoMorpho c1 = new CimoMorpho();
		c1.setCode(code1);
		final CimoMorpho c2 = new CimoMorpho();
		c2.setCode(code1);
		final CimoMorpho c3 = new CimoMorpho();
		c3.setCode(null);
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

	@Test
	public void testToString() {
		final CimoMorpho a = new CimoMorpho();
		a.setCode("12.12");
		assertTrue(a.toString().equals("{CimoMorpho: 12.12}"));
	}

}
