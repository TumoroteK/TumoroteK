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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampEntiteBlocPK;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le bean du domaine BlocImpressionTemplatePK.
 *
 * @author Pierre Ventadour.
 * @version 22/07/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ChampEntiteBlocPKTest extends AbstractDaoTest {

	@Autowired
	BlocImpressionDao blocImpressionDao;

	@Autowired
	ChampEntiteDao champEntiteDao;

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {
		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK();
		final ChampEntiteBlocPK pk2 = new ChampEntiteBlocPK();

		// L'objet 1 n'est pas égal à null
		assertFalse(pk1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(pk1.equals(pk1));

		/* null */
		assertTrue(pk1.equals(pk2));
		assertTrue(pk2.equals(pk1));

		populateClefsToTestEqualsAndHashCode();

		final Categorie c3 = new Categorie();
		assertFalse(pk1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK();
		final ChampEntiteBlocPK pk2 = new ChampEntiteBlocPK();

		/* null */
		assertTrue(pk1.hashCode() == pk2.hashCode());

		populateClefsToTestEqualsAndHashCode();

		// un même objet garde le même hashcode dans le temps
		final int hash = pk1.hashCode();
		assertTrue(hash == pk1.hashCode());
		assertTrue(hash == pk1.hashCode());
		assertTrue(hash == pk1.hashCode());
		assertTrue(hash == pk1.hashCode());
	}

	@Autowired
	void populateClefsToTestEqualsAndHashCode() throws ParseException {

		final BlocImpression bi1 = blocImpressionDao.findById(1).get();
		final BlocImpression bi2 = blocImpressionDao.findById(2).get();
		final BlocImpression bi3 = blocImpressionDao.findById(1).get();
		final BlocImpression[] blocs = new BlocImpression[] { null, bi1, bi2, bi3 };
		final ChampEntite c1 = champEntiteDao.findById(1).get();
		final ChampEntite c2 = champEntiteDao.findById(2).get();
		final ChampEntite c3 = champEntiteDao.findById(1).get();
		final ChampEntite[] champs = new ChampEntite[] { null, c1, c2, c3 };

		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK();
		final ChampEntiteBlocPK pk2 = new ChampEntiteBlocPK();

		for (int i = 0; i < blocs.length; i++) {
			for (int j = 0; j < champs.length; j++) {
				for (int k = 0; k < blocs.length; k++) {
					for (int l = 0; l < champs.length; l++) {

						pk1.setBlocImpression(blocs[i]);
						pk1.setChampEntite(champs[j]);

						pk2.setBlocImpression(blocs[k]);
						pk2.setChampEntite(champs[l]);

						if (((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4))) {
							assertTrue(pk1.equals(pk2));
							assertTrue(pk1.hashCode() == pk2.hashCode());
						} else {
							assertFalse(pk1.equals(pk2));
						}
					}
				}
			}
		}
	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final BlocImpression bi1 = blocImpressionDao.findById(1).get();
		final ChampEntite c1 = champEntiteDao.findById(1).get();
		final ChampEntiteBlocPK pk1 = new ChampEntiteBlocPK();
		pk1.setBlocImpression(bi1);
		pk1.setChampEntite(c1);

		assertTrue(pk1.toString().equals("{" + pk1.getChampEntite().toString() + " (ChampEntite), "
				+ pk1.getBlocImpression().toString() + " (BlocImpression)}"));

		pk1.setBlocImpression(null);
		assertTrue(pk1.toString().equals("{Empty ChampEntiteBlocPK}"));
		pk1.setBlocImpression(bi1);

		pk1.setChampEntite(null);
		assertTrue(pk1.toString().equals("{Empty ChampEntiteBlocPK}"));
		pk1.setChampEntite(c1);

		pk1.setBlocImpression(null);
		pk1.setChampEntite(null);
		assertTrue(pk1.toString().equals("{Empty ChampEntiteBlocPK}"));

		final ChampEntiteBlocPK pk2 = new ChampEntiteBlocPK();
		assertTrue(pk2.toString().equals("{Empty ChampEntiteBlocPK}"));
	}

}
