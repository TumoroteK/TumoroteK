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
package fr.aphp.tumorotek.dao.test.coeur.prelevement;

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

import fr.aphp.tumorotek.dao.coeur.prelevement.ConditMilieuDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO ConditMilieuDao et le bean du domaine
 * ConditMilieu. Classe de test créée le 01/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ConditMilieuDaoTest extends AbstractDaoTest {

	@Autowired
	ConditMilieuDao conditMilieuDao;

	@Autowired
	PlateformeDao plateformeDao;

	/** Valeur du milieu pour la maj. */
	final String updatedMilieu = "Milieu mis a jour";

	@Test
	public void testToString() {
		ConditMilieu cm1 = conditMilieuDao.findById(1).get();
		assertTrue(cm1.toString().equals("{" + cm1.getNom() + "}"));
		cm1 = new ConditMilieu();
		assertTrue(cm1.toString().equals("{Empty ConditMilieu}"));
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllConditMilieu() {
		final List<ConditMilieu> milieux = IterableUtils.toList(conditMilieuDao.findAll());
		assertTrue(milieux.size() == 2);
	}

	@Test
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1).get();
		List<? extends TKThesaurusObject> list = conditMilieuDao.findByPfOrder(pf);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getNom().equals("SEC"));
		pf = plateformeDao.findById(2).get();
		list = conditMilieuDao.findByPfOrder(pf);
		assertTrue(list.size() == 1);
		list = conditMilieuDao.findByPfOrder(null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByMilieu().
	 */
	@Test
	public void testFindByMilieu() {
		List<ConditMilieu> milieux = conditMilieuDao.findByMilieu("HEPARINE");
		assertTrue(milieux.size() == 1);
		milieux = conditMilieuDao.findByMilieu("EDTA");
		assertTrue(milieux.size() == 0);
		milieux = conditMilieuDao.findByMilieu("HEP%");
		assertTrue(milieux.size() == 1);
		milieux = conditMilieuDao.findByMilieu(null);
		assertTrue(milieux.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un milieu de
	 * conditionnement.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	@Test
	public void testCrudConditMilieu() throws Exception {
		final ConditMilieu ct = new ConditMilieu();

		ct.setNom("EDTA");
		ct.setPlateforme(plateformeDao.findById(1).get());
		// Test de l'insertion
		conditMilieuDao.save(ct);
		assertEquals(new Integer(3), ct.getId());

		// Test de la mise à jour
		final ConditMilieu ct2 = conditMilieuDao.findById(new Integer(3)).get();
		assertNotNull(ct2);
		assertTrue(ct2.getNom().equals("EDTA"));
		ct2.setNom(updatedMilieu);
		conditMilieuDao.save(ct2);
		assertTrue(conditMilieuDao.findById(new Integer(3)).get().getNom().equals(updatedMilieu));

		// Test de la délétion
		conditMilieuDao.deleteById(new Integer(3));
		assertFalse(conditMilieuDao.findById(new Integer(3)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String milieu = "Milieu";
		final String milieu2 = "Milieu2";
		final ConditMilieu ct1 = new ConditMilieu();
		ct1.setNom(milieu);
		final ConditMilieu ct2 = new ConditMilieu();
		ct2.setNom(milieu);
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();

		// L'objet 1 n'est pas égal à null
		assertFalse(ct1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(ct1.equals(ct1));
		// 2 objets sont égaux entre eux
		assertTrue(ct1.equals(ct2));
		assertTrue(ct2.equals(ct1));

		// Vérification de la différenciation de 2 objets
		ct2.setNom(milieu2);
		assertFalse(ct1.equals(ct2));
		assertFalse(ct2.equals(ct1));

		// passe la clef naturelle milieu a nulle pour un des objets
		ct2.setNom(null);
		assertFalse(ct1.equals(ct2));
		assertFalse(ct2.equals(ct1));

		// passe la clef naturelle milieu a nulle pour l'autre objet
		ct1.setNom(null);
		assertTrue(ct1.equals(ct2));
		ct2.setNom(milieu);
		assertFalse(ct1.equals(ct2));

		// plateforme
		ct1.setNom(ct2.getNom());
		ct1.setPlateforme(pf1);
		ct2.setPlateforme(pf1);
		assertTrue(ct1.equals(ct2));
		ct2.setPlateforme(pf2);
		assertFalse(ct1.equals(ct2));

		// dummy test
		final Banque b = new Banque();
		assertFalse(ct1.equals(b));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String milieu = "Milieu";
		final ConditMilieu ct1 = new ConditMilieu();
		ct1.setId(1);
		ct1.setNom(milieu);
		final ConditMilieu ct2 = new ConditMilieu();
		ct2.setId(2);
		ct2.setNom(milieu);
		final ConditMilieu ct3 = new ConditMilieu();
		ct3.setId(3);
		ct3.setNom(null);
		assertTrue(ct3.hashCode() > 0);

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		ct1.setPlateforme(pf1);
		ct2.setPlateforme(pf1);
		ct3.setPlateforme(pf2);

		final int hash = ct1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(ct1.hashCode() == ct2.hashCode());
		assertFalse(ct1.hashCode() == ct3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == ct1.hashCode());
		assertTrue(hash == ct1.hashCode());
		assertTrue(hash == ct1.hashCode());
		assertTrue(hash == ct1.hashCode());
	}
}