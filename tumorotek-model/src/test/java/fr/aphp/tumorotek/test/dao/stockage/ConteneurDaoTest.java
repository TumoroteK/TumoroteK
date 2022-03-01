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
package fr.aphp.tumorotek.test.dao.stockage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.ConteneurTypeDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ConteneurDao et le bean du domaine Conteneur.
 *
 * @author Pierre Ventadour.
 * @version 17/03/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ConteneurDaoTest extends AbstractDaoTest {

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	ConteneurDao conteneurDao;

	@Autowired
	PlateformeDao plateformeDao;

	@Autowired
	ConteneurTypeDao conteneurTypeDao;

	@Autowired
	ServiceDao serviceDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<Conteneur> liste = IterableUtils.toList(IterableUtils.toList(conteneurDao.findAll()));
		assertTrue(liste.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findByBanqueIdWithOrder().
	 */
	@Test
	public void testFindByBanqueIdWithOrder() {

		List<Conteneur> liste = conteneurDao.findByBanqueIdWithOrder(1);
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getNom().equals("Congélateur 1"));

		liste = conteneurDao.findByBanqueIdWithOrder(2);
		assertTrue(liste.size() == 4);

		liste = conteneurDao.findByBanqueIdWithOrder(4);
		assertTrue(liste.size() == 1);

		liste = conteneurDao.findByBanqueIdWithOrder(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByBanqueIdAndCode().
	 */
	@Test
	public void testFindByBanqueIdAndCode() {

		List<Conteneur> liste = conteneurDao.findByBanqueIdAndCode(1, "CC1");
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getNom().equals("Congélateur 1"));

		liste = conteneurDao.findByBanqueIdAndCode(1, "CCscqc1");
		assertTrue(liste.size() == 0);

		liste = conteneurDao.findByBanqueIdAndCode(2, "CC1");
		assertTrue(liste.size() == 1);

		liste = conteneurDao.findByBanqueIdAndCode(null, "CC1");
		assertTrue(liste.size() == 0);

		liste = conteneurDao.findByBanqueIdAndCode(1, null);
		assertTrue(liste.size() == 0);
	}

	@Test
	public void testFindByPlateformeOrigWithOrder() {

		List<Conteneur> liste = conteneurDao.findByPlateformeOrigWithOrder(plateformeDao.findById(1).get());
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getCode().equals("CC1"));

		liste = conteneurDao.findByPlateformeOrigWithOrder(plateformeDao.findById(2).get());
		assertTrue(liste.size() == 1);

		liste = conteneurDao.findByPlateformeOrigWithOrder(plateformeDao.findById(3).orElse(null));
		assertTrue(liste.size() == 0);

		liste = conteneurDao.findByPlateformeOrigWithOrder(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByBanqueIdWithExcludedId().
	 */
	@Test
	public void testFindByBanqueIdWithExcludedId() {
		List<Conteneur> liste = conteneurDao.findByBanqueIdWithExcludedId(1, 1);
		assertTrue(liste.size() == 2);

		liste = conteneurDao.findByBanqueIdWithExcludedId(1, 10);
		assertTrue(liste.size() == 3);

		liste = conteneurDao.findByBanqueIdWithExcludedId(null, 1);
		assertTrue(liste.size() == 0);

		liste = conteneurDao.findByBanqueIdWithExcludedId(1, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<Conteneur> liste = conteneurDao.findByExcludedId(1);
		assertTrue(liste.size() == 3);

		liste = conteneurDao.findByExcludedId(10);
		assertTrue(liste.size() == 4);

		liste = conteneurDao.findByExcludedId(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un Conteneur.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Transactional
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final Conteneur c = new Conteneur();
		final ConteneurType ct = conteneurTypeDao.findById(1).get();
		final Service s = serviceDao.findById(1).get();
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final Set<Banque> banques = new HashSet<>();
		banques.add(b1);
		final Plateforme p1 = plateformeDao.findById(1).get();
		final Plateforme p2 = plateformeDao.findById(2).get();
		final Set<ConteneurPlateforme> plateformes = new HashSet<>();
		final ConteneurPlateforme cp1 = new ConteneurPlateforme(c, p1);
		final ConteneurPlateforme cp2 = new ConteneurPlateforme(c, p2);
		cp2.setPartage(true);
		plateformes.add(cp1);
		plateformes.add(cp2);
		final String codeUp = "UP";
		final Float temp = (float) -50.0;

		c.setCode("CC");
		c.setConteneurType(ct);
		c.setNom("NOM");
		c.setTemp(temp);
		c.setPiece("PIECE");
		c.setNbrNiv(3);
		c.setNbrEnc(3);
		c.setDescription("DESC");
		c.setService(s);
		c.setBanques(banques);
		c.setConteneurPlateformes(plateformes);
		c.setPlateformeOrig(p2);

		c.setArchive(false);

		// Test de l'insertion
		conteneurDao.save(c);
		assertEquals(new Integer(5), c.getConteneurId());

		final Conteneur c2 = conteneurDao.findById(new Integer(5)).get();
		// Vérification des données entrées dans la base
		assertNotNull(c2);
		assertNotNull(c2.getService());
		assertNotNull(c2.getConteneurType());
		assertTrue(c2.getCode().equals("CC"));
		assertTrue(c2.getNom().equals("NOM"));
		assertTrue(c2.getTemp().equals(temp));
		assertTrue(c2.getPiece().equals("PIECE"));
		assertTrue(c2.getNbrNiv() == 3);
		assertTrue(c2.getNbrEnc() == 3);
		assertTrue(c2.getDescription().equals("DESC"));
		assertTrue(c2.getBanques().size() == 1);
		assertTrue(c2.getConteneurPlateformes().size() == 2);
		assertTrue(c2.getPlateformeOrig().equals(p2));

		assertTrue(conteneurDao.findByPartage(p2, true).contains(c2));

		// Test de la mise à jour
		c2.setCode(codeUp);
		banques.add(b2);
		cp1.setPartage(true);
		conteneurDao.save(c2);
		assertTrue(conteneurDao.findById(new Integer(5)).get().getCode().equals(codeUp));
		assertTrue(conteneurDao.findById(new Integer(5)).get().getBanques().size() == 2);
		assertTrue(conteneurDao.findById(new Integer(5)).get().getConteneurPlateformes().size() == 2);

		assertTrue(conteneurDao.findByPartage(p1, true).contains(c2));

		// Test de la délétion
		conteneurDao.deleteById(new Integer(5));
		assertFalse(conteneurDao.findById(new Integer(5)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		final String code = "C1";
		final String code2 = "C2";
		final Conteneur c1 = new Conteneur();
		final Conteneur c2 = new Conteneur();

		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(c1.equals(c1));

		/* null */
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));

		/* PlateformeOrig */
		c2.setPlateformeOrig(pf1);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setPlateformeOrig(pf2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setPlateformeOrig(pf1);
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));

		/* Code */
		c2.setCode(code);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setCode(code2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setCode(code);
		assertTrue(c1.equals(c2));

		final Categorie c3 = new Categorie();
		assertFalse(c1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		final String code = "C1";
		final String code2 = "C2";
		final Conteneur c1 = new Conteneur();
		final Conteneur c2 = new Conteneur();

		/* null */
		assertTrue(c1.hashCode() == c2.hashCode());

		/* PlateformeOrig */
		c2.setPlateformeOrig(pf1);
		assertFalse(c1.hashCode() == c2.hashCode());
		c1.setPlateformeOrig(pf2);
		assertFalse(c1.hashCode() == c2.hashCode());
		c1.setPlateformeOrig(pf1);
		assertTrue(c1.hashCode() == c2.hashCode());

		/* Code */
		c2.setCode(code);
		assertFalse(c1.hashCode() == c2.hashCode());
		c1.setCode(code2);
		assertFalse(c1.hashCode() == c2.hashCode());
		c1.setCode(code);
		assertTrue(c1.hashCode() == c2.hashCode());

		// un même objet garde le même hashcode dans le temps
		final int hash = c1.hashCode();
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Conteneur c1 = conteneurDao.findById(1).get();
		assertTrue(c1.toString().equals("{" + c1.getCode() + "}"));

		final Conteneur c2 = new Conteneur();
		assertTrue(c2.toString().equals("{Empty Conteneur}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	@Transactional
	public void testClone() {
		final Conteneur c1 = conteneurDao.findById(1).get();
		Conteneur c2 = new Conteneur();
		c2 = c1.clone();

		assertTrue(c1.equals(c2));

		if (c1.getConteneurId() != null) {
			assertTrue(c1.getConteneurId() == c2.getConteneurId());
		} else {
			assertNull(c2.getConteneurId());
		}

		if (c1.getConteneurType() != null) {
			assertTrue(c1.getConteneurType().equals(c2.getConteneurType()));
		} else {
			assertNull(c2.getConteneurType());
		}

		if (c1.getCode() != null) {
			assertTrue(c1.getCode().equals(c2.getCode()));
		} else {
			assertNull(c2.getCode());
		}

		if (c1.getNom() != null) {
			assertTrue(c1.getNom().equals(c2.getNom()));
		} else {
			assertNull(c2.getNom());
		}

		if (c1.getTemp() != null) {
			assertTrue(c1.getTemp().equals(c2.getTemp()));
		} else {
			assertNull(c2.getTemp());
		}

		if (c1.getPiece() != null) {
			assertTrue(c1.getPiece().equals(c2.getPiece()));
		} else {
			assertNull(c2.getPiece());
		}

		if (c1.getNbrNiv() != null) {
			assertTrue(c1.getNbrNiv() == c2.getNbrNiv());
		} else {
			assertNull(c2.getNbrNiv());
		}

		if (c1.getNbrEnc() != null) {
			assertTrue(c1.getNbrEnc() == c2.getNbrEnc());
		} else {
			assertNull(c2.getNbrEnc());
		}

		if (c1.getDescription() != null) {
			assertTrue(c1.getDescription().equals(c2.getDescription()));
		} else {
			assertNull(c2.getDescription());
		}

		if (c1.getService() != null) {
			assertTrue(c1.getService().equals(c2.getService()));
		} else {
			assertNull(c2.getService());
		}

		if (c1.getBanques() != null) {
			assertTrue(c1.getBanques().equals(c2.getBanques()));
		} else {
			assertNull(c2.getBanques());
		}

		if (c1.getConteneurPlateformes() != null) {
			assertTrue(c1.getConteneurPlateformes().equals(c2.getConteneurPlateformes()));
		} else {
			assertNull(c2.getConteneurPlateformes());
		}

		if (c1.getIncidents() != null) {
			assertTrue(c1.getIncidents().equals(c2.getIncidents()));
		} else {
			assertNull(c2.getIncidents());
		}

		if (c1.getRetours() != null) {
			assertTrue(c1.getRetours().equals(c2.getRetours()));
		} else {
			assertNull(c2.getRetours());
		}

		assertTrue(c1.getPlateformeOrig().equals(c2.getPlateformeOrig()));
	}

	@Test
	public void testFindByPartage() {
		List<Conteneur> conts = conteneurDao.findByPartage(plateformeDao.findById(1).get(), true);
		assertTrue(conts.size() == 1);
		assertTrue(conts.get(0).getCode().equals("CC1"));
		conts = conteneurDao.findByPartage(plateformeDao.findById(1).get(), false);
		assertTrue(conts.size() == 2);
		conts = conteneurDao.findByPartage(plateformeDao.findById(2).get(), true);
		assertTrue(conts.size() == 1);
		conts = conteneurDao.findByPartage(plateformeDao.findById(2).get(), false);
		assertTrue(conts.size() == 1);
		conts = conteneurDao.findByPartage(plateformeDao.findById(3).orElse(null), false);
		assertTrue(conts.isEmpty());
		conts = conteneurDao.findByPartage(null, true);
		assertTrue(conts.isEmpty());
		conts = conteneurDao.findByPartage(plateformeDao.findById(1).get(), null);
		assertTrue(conts.isEmpty());
	}

	@Test
	public void testFindTempForEmplacementId() {
		final List<Float> temps = new ArrayList<>();
		temps.addAll(conteneurDao.findTempForEmplacementId(1));
		assertEquals(new Float(-75.0), temps.get(0));
		temps.clear();
		temps.addAll(conteneurDao.findTempForEmplacementId(6));
		assertTrue(temps.get(0).equals(new Float(-75.0)));
		temps.clear();
		temps.addAll(conteneurDao.findTempForEmplacementId(10));
		assertTrue(temps.isEmpty());
		temps.clear();
		temps.addAll(conteneurDao.findTempForEmplacementId(null));
		assertTrue(temps.isEmpty());
	}

}
