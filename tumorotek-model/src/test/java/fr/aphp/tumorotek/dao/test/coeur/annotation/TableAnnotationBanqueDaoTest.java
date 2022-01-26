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
package fr.aphp.tumorotek.dao.test.coeur.annotation;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationBanqueDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.Config;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanque;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanquePK;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Classe de test pour le DAO TableAnnotationBanqueDao et le bean du domaine
 * TableAnnotationBanque. Classe de test créée le 29/01/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class TableAnnotationBanqueDaoTest extends AbstractDaoTest {

	@Autowired
	TableAnnotationBanqueDao tableAnnotationBanqueDao;

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	TableAnnotationDao tableAnnotationDao;

	@Test
	public void testToString() {
		final TableAnnotationBanquePK pk = new TableAnnotationBanquePK(banqueDao.findById(1).get(),
				tableAnnotationDao.findById(1).get());
		TableAnnotationBanque tab1 = tableAnnotationBanqueDao.findById(pk).get();
		assertTrue(tab1.toString().equals("{" + tab1.getTableAnnotation() + " - " + tab1.getBanque() + "}"));
		tab1 = new TableAnnotationBanque();
		assertTrue(tab1.toString().equals("{Empty TableAnnotationBanque}"));
		tab1.setBanque(banqueDao.findById(1).get());
		assertTrue(tab1.toString().equals("{Empty TableAnnotationBanque}"));
		tab1.setBanque(null);
		tab1.setTableAnnotation(tableAnnotationDao.findById(1).get());
		assertTrue(tab1.toString().equals("{Empty TableAnnotationBanque}"));
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllTableAnnotations() {
		final List<TableAnnotationBanque> tableAnnotationBanques = IterableUtils
				.toList(tableAnnotationBanqueDao.findAll());
		assertTrue(tableAnnotationBanques.size() == 11);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une
	 * TableAnnotationBanque.
	 *
	 **/
	@Rollback(false)
	@Test
	public void testTableAnnotationBanque() {
		final TableAnnotationBanque tab = new TableAnnotationBanque();
		final TableAnnotation t1 = tableAnnotationDao.findById(2).get();
		final Banque b1 = banqueDao.findById(4).get();
		tab.setTableAnnotation(t1);
		tab.setBanque(b1);
		tab.setOrdre(3);
		// Test de l'insertion
		tableAnnotationBanqueDao.save(tab);
		assertTrue(IterableUtils.toList(tableAnnotationBanqueDao.findAll()).size() == 12);
		// Test de la mise à jour
		final TableAnnotationBanquePK pk = new TableAnnotationBanquePK();
		pk.setTableAnnotation(t1);
		pk.setBanque(b1);
		final TableAnnotationBanque tab2 = tableAnnotationBanqueDao.findById(pk).get();
		assertNotNull(tab2);
		assertTrue(tab2.getTableAnnotation().equals(t1));
		assertTrue(tab2.getBanque().equals(b1));
		assertTrue(tab2.getOrdre().equals(3));
		// update
		tab2.setOrdre(2);
		tableAnnotationBanqueDao.save(tab2);
		assertTrue(tableAnnotationBanqueDao.findById(pk).get().equals(tab2));
		assertTrue(tableAnnotationBanqueDao.findById(pk).get().getOrdre().equals(2));
		// Test de la délétion
		tableAnnotationBanqueDao.deleteById(pk);
		assertFalse(tableAnnotationBanqueDao.findById(pk).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {

		final TableAnnotationBanque tab1 = new TableAnnotationBanque();
		final TableAnnotationBanque tab2 = new TableAnnotationBanque();

		// L'objet 1 n'est pas égal à null
		assertFalse(tab1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(tab1.equals(tab1));
		// 2 objets sont égaux entre eux
		assertTrue(tab1.equals(tab2));
		assertTrue(tab2.equals(tab1));

		populateClefsToTestEqualsAndHashCode(tab1, tab2);

		// dummy test
		final Banque b = new Banque();
		assertFalse(tab1.equals(b));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final TableAnnotationBanque tab1 = new TableAnnotationBanque();
		final TableAnnotationBanque tab2 = new TableAnnotationBanque();
		final TableAnnotationBanque tab3 = new TableAnnotationBanque();

		assertTrue(tab1.hashCode() == tab2.hashCode());
		assertTrue(tab2.hashCode() == tab3.hashCode());
		assertTrue(tab3.hashCode() > 0);

		// teste dans methode precedente
		// populateClefsToTestEqualsAndHashCode(pm1, pm2);

		final int hash = tab1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(tab1.hashCode() == tab2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == tab1.hashCode());
		assertTrue(hash == tab1.hashCode());
		assertTrue(hash == tab1.hashCode());
		assertTrue(hash == tab1.hashCode());
	}

	private void populateClefsToTestEqualsAndHashCode(final TableAnnotationBanque tab1, final TableAnnotationBanque tab2) {

		final TableAnnotation t1 = tableAnnotationDao.findById(1).get();
		final TableAnnotation t2 = tableAnnotationDao.findById(2).get();
		final TableAnnotation t3 = tableAnnotationDao.findById(1).get();
		final TableAnnotation[] tableAnnotations = new TableAnnotation[] { null, t1, t2, t3 };
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final Banque b3 = banqueDao.findById(1).get();
		final Banque[] banks = new Banque[] { null, b1, b2, b3 };

		TableAnnotationBanquePK pk1 = new TableAnnotationBanquePK();
		final TableAnnotationBanquePK pk2 = new TableAnnotationBanquePK();

		for (int i = 0; i < tableAnnotations.length; i++) {
			for (int j = 0; j < banks.length; j++) {
				for (int k = 0; k < tableAnnotations.length; k++) {
					for (int l = 0; l < banks.length; l++) {
						pk1.setTableAnnotation(tableAnnotations[i]);
						pk1.setBanque(banks[j]);
						pk2.setTableAnnotation(tableAnnotations[k]);
						pk2.setBanque(banks[l]);
						tab1.setPk(pk1);
						tab2.setPk(pk2);
						if (((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4))) {
							assertTrue(tab1.equals(tab2));
							assertTrue(tab1.hashCode() == tab2.hashCode());
						} else {
							assertFalse(tab1.equals(tab2));
						}
					}
				}
			}
		}

		// pk testing
		assertTrue(pk1.equals(pk1));
		tab2.setPk(pk1);
		assertTrue(tab1.equals(tab2));
		assertTrue(tab1.hashCode() == tab2.hashCode());
		assertFalse(pk1.equals(null));
		pk1 = null;
		tab1.setPk(pk1);
		assertFalse(tab1.equals(tab2));
		assertFalse(tab1.hashCode() == tab2.hashCode());
		final Banque b = new Banque();
		assertFalse(pk2.equals(b));
	}

}
