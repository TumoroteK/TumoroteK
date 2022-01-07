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
package fr.aphp.tumorotek.dao.test.coeur.echantillon;

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

import fr.aphp.tumorotek.dao.coeur.echantillon.EchanQualiteDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO EchanQualiteDao et le bean du domaine
 * EchanQualite.
 *
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class EchanQualiteDaoTest extends AbstractDaoTest {

	@Autowired
	EchanQualiteDao echanQualiteDao;

	@Autowired
	PlateformeDao plateformeDao;

	private final String updatedNom = "Stat mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllEchanQualites() {
		final List<EchanQualite> qualites = IterableUtils.toList(echanQualiteDao.findAll());
		assertTrue(qualites.size() == 3);
	}

	@Test
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1).get();
		List<? extends TKThesaurusObject> list = echanQualiteDao.findByPfOrder(pf);
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getNom().equals("MELANGE MO"));
		pf = plateformeDao.findById(2).get();
		list = echanQualiteDao.findByPfOrder(pf);
		assertTrue(list.size() == 1);
		list = echanQualiteDao.findByPfOrder(null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByQualite().
	 */
	@Test
	public void testFindByQualite() {
		List<EchanQualite> qualites = echanQualiteDao.findByQualite("MELANGE MO");
		assertTrue(qualites.size() == 1);
		qualites = echanQualiteDao.findByQualite("MELANGE");
		assertTrue(qualites.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByEchantillonId().
	 */
	@Test
	public void testFindByEchantillonId() {
		List<EchanQualite> qualites = echanQualiteDao.findByEchantillonId(1);
		assertTrue(qualites.size() == 1);
		assertTrue(qualites.get(0).getId() == 1);
		qualites = echanQualiteDao.findByEchantillonId(3);
		assertTrue(qualites.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<EchanQualite> liste = echanQualiteDao.findByExcludedId(1);
		assertTrue(liste.size() == 2);
		final EchanQualite qualite = liste.get(0);
		assertNotNull(qualite);
		assertTrue(qualite.getId() == 2);

		liste = echanQualiteDao.findByExcludedId(15);
		assertTrue(liste.size() == 3);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un EchanQualite.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrudEchanQualite() throws Exception {

		final EchanQualite e = new EchanQualite();

		e.setNom("MELANGE");
		e.setPlateforme(plateformeDao.findById(1).get());
		// Test de l'insertion
		echanQualiteDao.save(e);
		assertEquals(new Integer(4), e.getId());

		// Test de la mise à jour
		final EchanQualite e2 = echanQualiteDao.findById(new Integer(4)).get();
		assertNotNull(e2);
		assertTrue(e2.getNom().equals("MELANGE"));
		e2.setNom(updatedNom);
		echanQualiteDao.save(e2);
		assertTrue(echanQualiteDao.findById(new Integer(4)).get().getNom().equals(updatedNom));

		// Test de la délétion
		echanQualiteDao.deleteById(new Integer(4));
		assertFalse(echanQualiteDao.findById(new Integer(4)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String qualite = "Qualite";
		final String qualite2 = "Qualite2";
		final EchanQualite e1 = new EchanQualite();
		e1.setNom(qualite);
		final EchanQualite e2 = new EchanQualite();
		e2.setNom(qualite);

		// L'objet 1 n'est pas égal à null
		assertFalse(e1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(e1.equals(e1));
		// 2 objets sont égaux entre eux
		assertTrue(e1.equals(e2));
		assertTrue(e2.equals(e1));

		// Vérification de la différenciation de 2 objets
		e2.setNom(qualite2);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));

		e2.setNom(null);
		assertFalse(e1.equals(e2));
		assertFalse(e2.equals(e1));

		e1.setNom(null);
		assertTrue(e1.equals(e2));
		e2.setNom(qualite);
		assertFalse(e1.equals(e2));

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		e1.setNom(e2.getNom());
		e1.setPlateforme(pf1);
		e2.setPlateforme(pf1);
		assertTrue(e1.equals(e2));
		e2.setPlateforme(pf2);
		assertFalse(e1.equals(e2));

		final Categorie c = new Categorie();
		assertFalse(e1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String qualite = "Qualite";
		final EchanQualite e1 = new EchanQualite(1, qualite);
		e1.setNom(qualite);
		final EchanQualite e2 = new EchanQualite(2, qualite);
		e2.setNom(qualite);
		final EchanQualite e3 = new EchanQualite(3, null);
		assertTrue(e3.hashCode() > 0);

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		e1.setPlateforme(pf1);
		e2.setPlateforme(pf1);
		e3.setPlateforme(pf2);

		final int hash = e1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(e1.hashCode() == e2.hashCode());
		assertFalse(e1.hashCode() == e3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());
		assertTrue(hash == e1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final EchanQualite qual1 = echanQualiteDao.findById(1).get();
		assertTrue(qual1.toString().equals("{" + qual1.getNom() + "}"));

		final EchanQualite qual2 = new EchanQualite();
		assertTrue(qual2.toString().equals("{Empty EchanQualite}"));
	}

}
