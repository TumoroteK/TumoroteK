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
package fr.aphp.tumorotek.test.dao.coeur.prodderive;

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

import fr.aphp.tumorotek.dao.coeur.prodderive.ModePrepaDeriveDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ModePrepaDao et le bean du domaine
 * ModePrepaDerive.
 *
 * @author Pierre Ventadour.
 * @version 05/01/2011
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ModePrepaDeriveDaoTest extends AbstractDaoTest {

	@Autowired
	ModePrepaDeriveDao modePrepaDeriveDao;
	
	@Autowired
	PlateformeDao plateformeDao;

	private final String updatedNom = "Prepa mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllModePrepas() {
		final List<ModePrepaDerive> modes = IterableUtils.toList(modePrepaDeriveDao.findAll());
		assertTrue(modes.size() == 4);
	}

	@Test
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1).get();
		List<? extends TKThesaurusObject> list = modePrepaDeriveDao.findByPfOrder(pf);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNom().equals("PREPA1_DERIVE"));
		pf = plateformeDao.findById(2).get();
		list = modePrepaDeriveDao.findByPfOrder(pf);
		assertTrue(list.size() == 1);
		list = modePrepaDeriveDao.findByPfOrder(null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByNom().
	 */
	@Test
	public void testFindByNom() {
		List<ModePrepaDerive> modes = modePrepaDeriveDao.findByNom("PREPA1_DERIVE");
		assertTrue(modes.size() == 1);
		modes = modePrepaDeriveDao.findByNom("PREPA");
		assertTrue(modes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<ModePrepaDerive> liste = modePrepaDeriveDao.findByExcludedId(1);
		assertTrue(liste.size() == 3);
		final ModePrepaDerive mode = liste.get(0);
		assertNotNull(mode);
		assertTrue(mode.getId() == 2);

		liste = modePrepaDeriveDao.findByExcludedId(15);
		assertTrue(liste.size() == 4);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un ModePrepa.
	 * 
	 * @throws Exception Lance une exception.
	 */
	@Rollback(false)
	@Test
	public void testCrudModePrepa() throws Exception {

		final ModePrepaDerive m = new ModePrepaDerive();
		m.setPlateforme(plateformeDao.findById(1).get());
		m.setNom("PREPA");
		m.setNomEn("PREPA_EN");
		// Test de l'insertion
		modePrepaDeriveDao.save(m);
		assertEquals(new Integer(5), m.getId());

		// Test de la mise à jour
		final ModePrepaDerive m2 = modePrepaDeriveDao.findById(new Integer(5)).get();
		assertNotNull(m2);
		assertTrue(m2.getNom().equals("PREPA"));
		assertTrue(m2.getNomEn().equals("PREPA_EN"));
		m2.setNom(updatedNom);
		modePrepaDeriveDao.save(m2);
		assertTrue(modePrepaDeriveDao.findById(new Integer(5)).orElse(null).getNom().equals(updatedNom));

		// Test de la délétion
		modePrepaDeriveDao.deleteById(new Integer(5));
		assertFalse(modePrepaDeriveDao.findById(new Integer(5)).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "Nom";
		final String nom2 = "Nom2";
		final ModePrepaDerive m1 = new ModePrepaDerive();
		m1.setNom(nom);
		final ModePrepaDerive m2 = new ModePrepaDerive();
		m2.setNom(nom);

		// L'objet 1 n'est pas égal à null
		assertFalse(m1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(m1.equals(m1));
		// 2 objets sont égaux entre eux
		assertTrue(m1.equals(m2));
		assertTrue(m2.equals(m1));

		// Vérification de la différenciation de 2 objets
		m2.setNom(nom2);
		assertFalse(m1.equals(m2));
		assertFalse(m2.equals(m1));

		m2.setNom(null);
		assertFalse(m1.equals(m2));
		assertFalse(m2.equals(m1));

		m1.setNom(null);
		assertTrue(m1.equals(m2));
		m2.setNom(nom);
		assertFalse(m1.equals(m2));

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		m1.setNom(m2.getNom());
		m1.setPlateforme(pf1);
		m2.setPlateforme(pf1);
		assertTrue(m1.equals(m2));
		m2.setPlateforme(pf2);
		assertFalse(m1.equals(m2));

		final Categorie c = new Categorie();
		assertFalse(m1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "nom";
		final ModePrepaDerive m1 = new ModePrepaDerive();
		m1.setNom(nom);
		final ModePrepaDerive m2 = new ModePrepaDerive();
		m2.setNom(nom);
		final ModePrepaDerive m3 = new ModePrepaDerive();
		m3.setNom(null);
		assertTrue(m3.hashCode() > 0);

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		m1.setPlateforme(pf1);
		m2.setPlateforme(pf1);
		m3.setPlateforme(pf2);

		final int hash = m1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(m1.hashCode() == m2.hashCode());
		assertFalse(m1.hashCode() == m3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == m1.hashCode());
		assertTrue(hash == m1.hashCode());
		assertTrue(hash == m1.hashCode());
		assertTrue(hash == m1.hashCode());

	}

	/**
	 * Test toString().
	 */
	@Test
	public void testToString() {
		final ModePrepaDerive m1 = modePrepaDeriveDao.findById(1).get();
		assertTrue(m1.toString().equals("{" + m1.getNom() + "}"));

		final ModePrepaDerive m2 = new ModePrepaDerive();
		assertTrue(m2.toString().equals("{Empty ModePrepaDerive}"));
	}

}
