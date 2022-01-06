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
import fr.aphp.tumorotek.dao.test.Config;
import fr.aphp.tumorotek.dao.stats.IndicateurDao;
import fr.aphp.tumorotek.dao.stats.SModeleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class IndicateurDaoTest extends AbstractDaoTest {

	@Autowired
	IndicateurDao indicateurDao;

	@Autowired
	SModeleDao modeleDao;

	private final Integer totCounts = 5;

	/**
	 * Test l'appel de la méthode findById().
	 */
	@Test
	public void testFindById() {
		final Indicateur s = indicateurDao.findById(1).get();
		assertNotNull(s);
		assertTrue(s.getNom().equals("prelevement.nature"));

		assertFalse(indicateurDao.findById(100).isPresent());
	}

	@Test
	public void testFindNullSubvivisionIndicateurs() {
		final List<Indicateur> sl = indicateurDao.findNullSubvivisionIndicateurs();
		assertNotNull(sl);
		assertTrue(sl.size() == 2);
		assertTrue(sl.get(0).equals(indicateurDao.findById(3).get()));
		assertTrue(sl.get(1).equals(indicateurDao.findById(4).get()));
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testFindAll() {
		final List<Indicateur> liste = IterableUtils.toList(IterableUtils.toList(indicateurDao.findAll()));
		assertTrue(liste.size() == totCounts);
	}

	// @Test
	// @Test
	// public void testFindByEntite() {
	// Entite entite = entiteDao.findById(2).get();
	// assertNotNull(entite);
	// List<Indicateur> l1 = indicateurDao.findByEntite(entite);
	// assertTrue(l1.size() == 1);
	// assertTrue(l1.get(0).getCallingProcedure().equals("count_prel_tot"));
	// l1 = indicateurDao.findByEntite(entiteDao.findById(3)).get();
	// assertTrue(l1.size() == 1);
	// assertTrue(l1.get(0).getCallingProcedure().equals("count_echan_tot"));
	//
	// l1 = indicateurDao.findByEntite(null);
	// assertTrue(l1.isEmpty());
	// }

	@Test
	public void testFindDoublon() {
		List<Indicateur> ss = indicateurDao.findByExcludedId(10);
		assertTrue(ss.size() == totCounts);
		ss = indicateurDao.findByExcludedId(null);
		assertTrue(ss.isEmpty());
	}

	/**
	 * Test l'appel de la méthode findByModele().
	 */
	@Test
	public void testFindBySModele() {
		final SModele m2 = modeleDao.findById(2).get();
		List<Indicateur> liste = indicateurDao.findBySModele(m2);
		assertTrue(liste.isEmpty());

		final SModele m1 = modeleDao.findById(1).get();
		liste = indicateurDao.findBySModele(m1);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getNom().equals("prelevement.nature"));
		assertTrue(liste.get(1).getNom().equals("echantillon.nature"));

		final SModele m3 = modeleDao.findById(3).get();
		liste = indicateurDao.findBySModele(m3);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("prelevement.nature"));

		liste = indicateurDao.findBySModele(null);
		assertTrue(liste.isEmpty());
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une ligne.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		// SModele m1 = modeleDao.findById(1).get();
		final Indicateur indic = new Indicateur();
		// sm.setIndicateurModele(m1);
		// sm.setOrdre(1);
		indic.setNom("count echantillon");
		indic.setCallingProcedure("call");
		indic.setDescription("Une courte description");
		// indic.setIsDefault(true);
		// indic.setEntite(entiteDao.findById(3)).get();

		// Test de l'insertion
		indicateurDao.save(indic);
		assertNotNull(indic.getIndicateurId());

		final Integer indicId = indic.getIndicateurId();

		final Indicateur indic2 = indicateurDao.findById(indicId).get();
		// Vérification des données entrées dans la base
		assertNotNull(indic2);
		// assertNotNull(sm2.getIndicateurModele());
		// assertTrue(sm2.getOrdre() == 1);
		assertTrue(indic2.getNom().equals("count echantillon"));
		assertTrue(indic2.getCallingProcedure().equals("call"));
		assertTrue(indic2.getDescription().equals("Une courte description"));
		// assertTrue(indic2.getIsDefault());
		// assertTrue(indic2.getEntite().equals(entiteDao.findById(3))).get();

		// Test de la mise à jour
		indic2.setNom("newcall");
		indic2.setCallingProcedure("call2");
		// indic2.setIsDefault(false);
		indic2.setDescription("une autre description");
		// sm.setOrdre(2);
		indicateurDao.save(indic2);
		// assertTrue(statementDao.findById(new Integer(3)).getOrdre() == 2).get();

		final Indicateur indic3 = indicateurDao.findById(indicId).get();
		// Vérification des données entrées dans la base
		assertNotNull(indic3);
		// assertNotNull(sm2.getIndicateurModele());
		// assertTrue(sm2.getOrdre() == 1);
		assertTrue(indic3.getNom().equals("newcall"));
		assertTrue(indic3.getCallingProcedure().equals("call2"));
		assertTrue(indic3.getDescription().equals("une autre description"));
		// assertFalse(indic3.getIsDefault());
		// assertTrue(indic3.getEntite().equals(entiteDao.findById(3))).get();

		// Test de la délétion
		indicateurDao.deleteById(indicId);
		assertFalse(indicateurDao.findById(indicId).isPresent());
		testFindAll();
	}

	/**
	 * Test de la methode surchargee equals
	 */
	// @Test
	// public void testEquals() {
	// Integer o1 = 1;
	// Integer o2 = 2;
	// SModele m1 = modeleDao.findById(1).get();
	// SModele m2 = modeleDao.findById(2).get();
	// Indicateur s1 = new Indicateur();
	// Indicateur s2 = new Indicateur();
	// s1.setOrdre(o1);
	// s1.setIndicateurModele(m1);
	// s2.setOrdre(o1);
	// s2.setIndicateurModele(m1);
	//
	// // L'objet 1 n'est pas égal à null
	// assertFalse(s1.equals(null));
	// // L'objet 1 est égale à lui même
	// assertTrue(s1.equals(s1));
	// // 2 objets sont égaux entre eux
	// assertTrue(s1.equals(s2));
	// assertTrue(s2.equals(s1));
	//
	// s1.setIndicateurModele(null);
	// s1.setOrdre(null);
	// s2.setIndicateurModele(null);
	// s2.setOrdre(null);
	// assertTrue(s1.equals(s2));
	// s2.setOrdre(o1);
	// assertFalse(s1.equals(s2));
	// s1.setOrdre(o1);
	// assertTrue(s1.equals(s2));
	// s2.setOrdre(o2);
	// assertFalse(s1.equals(s2));
	// s2.setOrdre(null);
	// assertFalse(s1.equals(s2));
	// s2.setIndicateurModele(m1);
	// assertFalse(s1.equals(s2));
	//
	// s1.setIndicateurModele(m1);
	// s1.setOrdre(null);
	// s2.setOrdre(null);
	// s2.setIndicateurModele(m1);
	// assertTrue(s1.equals(s2));
	// s2.setIndicateurModele(m2);
	// assertFalse(s1.equals(s2));
	// s2.setOrdre(o1);
	// assertFalse(s1.equals(s2));
	//
	// // Vérification de la différenciation de 2 objets
	// // même ordre
	// s1.setOrdre(o1);
	// assertFalse(s1.equals(s2));
	// // ordre different
	// s2.setOrdre(o2);
	// s2.setIndicateurModele(m1);
	// assertFalse(s1.equals(s2));
	//
	// Categorie c3 = new Categorie();
	// assertFalse(s1.equals(c3));
	// }

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {

	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {

	}

}
