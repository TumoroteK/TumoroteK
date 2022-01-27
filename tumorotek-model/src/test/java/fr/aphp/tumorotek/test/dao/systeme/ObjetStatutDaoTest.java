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
package fr.aphp.tumorotek.test.dao.systeme;

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

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ObjetStatutDao et le bean du domaine ObjetStatut.
 *
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ObjetStatutDaoTest extends AbstractDaoTest {

	@Autowired
	ObjetStatutDao objetStatutDao;

	/** valeur du nom pour la maj. */
	private String updatedNom = "Stat mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllObjetStatuts() {
		final List<ObjetStatut> objets = IterableUtils.toList(IterableUtils.toList(objetStatutDao.findAll()));
		assertTrue(objets.size() == 6);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<ObjetStatut> list = objetStatutDao.findByOrder();
		assertTrue(list.size() == 6);
		assertTrue(list.get(0).getStatut().equals("DETRUIT"));
	}

	/**
	 * Test l'appel de la méthode findByStatut().
	 */
	@Test
	public void testFindByStatut() {
		List<ObjetStatut> objets = objetStatutDao.findByStatut("STOCKE");
		assertTrue(objets.size() == 1);
		objets = objetStatutDao.findByStatut("CEDE");
		assertTrue(objets.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un ObjetStatut.
	 * 
	 * @throws Exception Lance une exception.
	 */
	@Rollback(false)
	@Test
	public void testCrudObjetStatut() throws Exception {

		final ObjetStatut o = new ObjetStatut();

		o.setStatut("CEDE");
		// Test de l'insertion
		objetStatutDao.save(o);
		assertEquals(new Integer(7), o.getObjetStatutId());

		// Test de la mise à jour
		final ObjetStatut o2 = objetStatutDao.findById(new Integer(7)).get();
		assertNotNull(o2);
		assertTrue(o2.getStatut().equals("CEDE"));
		o2.setStatut(updatedNom);
		objetStatutDao.save(o2);
		assertTrue(objetStatutDao.findById(new Integer(7)).get().getStatut().equals(updatedNom));

		// Test de la délétion
		objetStatutDao.deleteById(new Integer(7));
		assertFalse(objetStatutDao.findById(new Integer(7)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String statut = "cede";
		final String statut2 = "non cede";
		final ObjetStatut o1 = new ObjetStatut();
		o1.setStatut(statut);
		final ObjetStatut o2 = new ObjetStatut();
		o2.setStatut(statut);

		// L'objet 1 n'est pas égal à null
		assertFalse(o1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(o1.equals(o1));
		// 2 objets sont égaux entre eux
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));

		// Vérification de la différenciation de 2 objets
		o2.setStatut(statut2);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));

		o2.setStatut(null);
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		o1.setStatut(null);
		assertTrue(o1.equals(o2));
		o2.setStatut(statut);
		assertFalse(o1.equals(o2));

		final Categorie c = new Categorie();
		assertFalse(o1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String statut = "cede";
		final ObjetStatut o1 = new ObjetStatut(1, statut);
		final ObjetStatut o2 = new ObjetStatut(2, statut);
		final ObjetStatut o3 = new ObjetStatut(3, null);
		assertTrue(o3.hashCode() > 0);

		final int hash = o1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(o1.hashCode() == o2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());

	}

	@Test
	public void testToString() {
		final ObjetStatut obj = objetStatutDao.findById(1).get();
		assertTrue(obj.toString().equals("{" + obj.getStatut() + "}"));

		final ObjetStatut obj2 = new ObjetStatut();
		assertTrue(obj2.toString().equals("{Empty ObjetStatut}"));
	}

}
