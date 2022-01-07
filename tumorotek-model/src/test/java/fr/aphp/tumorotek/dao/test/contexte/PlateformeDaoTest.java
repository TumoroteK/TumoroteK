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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import javax.transaction.Transactional;

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

import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.PopulateBeanForTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO PlateformeDao et le bean du domaine Plateforme.
 *
 * Date: 09/09/2009
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class PlateformeDaoTest extends AbstractDaoTest {

	@Autowired
	PlateformeDao plateformeDao;

	@Autowired
	CollaborateurDao collaborateurDao;

	private final String updatedNom = "PF mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllPlateformes() {
		final List<Plateforme> plateformes = IterableUtils.toList(plateformeDao.findAll());
		assertTrue(plateformes.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<Plateforme> plateformes = plateformeDao.findByExcludedId(1);
		assertTrue(plateformes.size() == 1);
		plateformes = plateformeDao.findByExcludedId(10);
		assertTrue(plateformes.size() == 2);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Plateforme> list = plateformeDao.findByOrder();
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getNom().equals("PLATEFORME 1"));
	}

	/**
	 * Test l'appel de la méthode findByNom().
	 */
	@Test
	public void testFindByNom() {
		List<Plateforme> plateformes = plateformeDao.findByNom("PLATEFORME 1");
		assertTrue(plateformes.size() == 1);
		plateformes = plateformeDao.findByNom("PLATEFORME 15");
		assertTrue(plateformes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByAlias().
	 */
	@Test
	public void testFindByAlias() {
		List<Plateforme> plateformes = plateformeDao.findByAlias("PF2");
		assertTrue(plateformes.size() == 1);
		plateformes = plateformeDao.findByAlias("PF5");
		assertTrue(plateformes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCollaborateur().
	 */
	@Test
	public void testFindByCollaborateur() {
		Collaborateur c = collaborateurDao.findById(1).get();
		List<Plateforme> plateformes = plateformeDao.findByCollaborateur(c);
		assertTrue(plateformes.size() == 1);
		c = collaborateurDao.findById(3).get();
		plateformes = plateformeDao.findByCollaborateur(c);
		assertTrue(plateformes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByBanqueId().
	 */
	@Test
	public void testFindByBanqueId() {
		List<Plateforme> plateformes = plateformeDao.findByBanqueId(1);
		assertTrue(plateformes.size() == 1);
		plateformes = plateformeDao.findByBanqueId(5);
		assertTrue(plateformes.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByIdWithFetch().
	 */
	@Test
	public void testFindByIdWithFetch() {
		final List<Plateforme> plateformes = plateformeDao.findByIdWithFetch(1);
		final Plateforme plateforme = plateformes.get(0);
		assertNotNull(plateforme);
		assertTrue(plateforme.getCollaborateur().getCollaborateurId() == 1);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une plateforme.
	 * 
	 * @throws Exception lance une exception en cas d'erreurs sur les données.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudPlateforme() throws Exception {

		final Plateforme p = new Plateforme();
		final Collaborateur c = collaborateurDao.findById(3).get();
		// on remplit la nouvelle plateforme avec les données du fichier
		// "plateforme.properties"
		try {
			PopulateBeanForTest.populateBean(p, "plateforme");
		} catch (final Exception exc) {
			System.out.println(exc.getMessage());
		}
		p.setCollaborateur(c);
		// Test de l'insertion
		plateformeDao.save(p);
		assertEquals(new Integer(3), p.getPlateformeId());

		// Test de la mise à jour
		final Plateforme p2 = plateformeDao.findById(new Integer(3)).get();
		assertNotNull(p2);
		assertTrue(p2.getNom().equals("Plateforme 3"));
		assertTrue(p2.getAlias().equals("PF3"));
		assertNotNull(p2.getCollaborateur());
		p2.setNom(updatedNom);
		plateformeDao.save(p2);
		assertTrue(plateformeDao.findById(new Integer(3)).get().getNom().equals(updatedNom));

		// Test de la délétion
		plateformeDao.deleteById(new Integer(3));
		assertFalse(plateformeDao.findById(new Integer(3)).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "PF1";
		final String nom2 = "PF2";
		final Plateforme p1 = new Plateforme();
		p1.setNom(nom);
		final Plateforme p2 = new Plateforme();
		p2.setNom(nom);

		// L'objet 1 n'est pas égal à null
		assertFalse(p1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(p1.equals(p1));
		// 2 objets sont égaux entre eux
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		// Vérification de la différenciation de 2 objets
		p2.setNom(nom2);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));

		p2.setNom(null);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));

		p1.setNom(null);
		assertTrue(p1.equals(p2));
		p2.setNom(nom);
		assertFalse(p1.equals(p2));

		final Categorie c = new Categorie();
		assertFalse(p1.equals(c));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "PF1";
		final Plateforme p1 = new Plateforme(1, nom, "");
		p1.setNom(nom);
		final Plateforme p2 = new Plateforme(2, nom, "");
		p2.setNom(nom);
		final Plateforme p3 = new Plateforme(3, nom, "");
		p3.setNom(null);
		assertTrue(p3.hashCode() > 0);

		final int hash = p1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(p1.hashCode() == p2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());

	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testClone() {
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = pf1.clone();
		assertTrue(pf1.equals(pf2));

		if (pf1.getPlateformeId() != null) {
			assertTrue(pf1.getPlateformeId() == pf2.getPlateformeId());
		} else {
			assertNull(pf2.getPlateformeId());
		}

		if (pf1.getCollaborateur() != null) {
			assertTrue(pf1.getCollaborateur().equals(pf2.getCollaborateur()));
		} else {
			assertNull(pf2.getCollaborateur());
		}

		if (pf1.getAlias() != null) {
			assertTrue(pf1.getAlias().equals(pf2.getAlias()));
		} else {
			assertNull(pf2.getAlias());
		}

		if (pf1.getNom() != null) {
			assertTrue(pf1.getNom().equals(pf2.getNom()));
		} else {
			assertNull(pf2.getNom());
		}

		if (pf1.getBanques() != null) {
			assertTrue(pf1.getBanques().equals(pf2.getBanques()));
		} else {
			assertNull(pf2.getBanques());
		}

		if (pf1.getConteneurPlateformes() != null) {
			assertTrue(pf1.getConteneurPlateformes().equals(pf2.getConteneurPlateformes()));
		} else {
			assertNull(pf2.getConteneurPlateformes());
		}

		if (pf1.getUtilisateurs() != null) {
			assertTrue(pf1.getUtilisateurs().equals(pf2.getUtilisateurs()));
		} else {
			assertNull(pf2.getUtilisateurs());
		}

		if (pf1.getImprimantes() != null) {
			assertTrue(pf1.getImprimantes().equals(pf2.getImprimantes()));
		} else {
			assertNull(pf2.getImprimantes());
		}

		if (pf1.getModeles() != null) {
			assertTrue(pf1.getModeles().equals(pf2.getModeles()));
		} else {
			assertNull(pf2.getModeles());
		}
	}

}
