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
package fr.aphp.tumorotek.dao.test.cession;

import java.text.ParseException;
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

import fr.aphp.tumorotek.dao.cession.CederObjetDao;
import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 *
 * Classe de test pour le DAO CessionExamenDao et le bean du domaine
 * CessionExamen.
 *
 * @author Pierre Ventadour.
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class CederObjetDaoTest extends AbstractDaoTest {

	@Autowired
	CederObjetDao cederObjetDao;

	@Autowired
	CessionDao cessionDao;

	@Autowired
	UniteDao uniteDao;

	@Autowired
	EntiteDao entiteDao;

	@Test
	public void testReadAll() {
		final List<CederObjet> liste = IterableUtils.toList(cederObjetDao.findAll());
		assertTrue(liste.size() == 6);
	}

	/**
	 * Test l'appel de la méthode findById().
	 */
	@Test
	public void testFindById() {
		final Cession c1 = cessionDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		CederObjetPK pk = new CederObjetPK(c1, e1, 1);

		CederObjet co = cederObjetDao.findById(pk).get();
		assertNotNull(co);

		pk = new CederObjetPK(c1, e1, 15);
		assertFalse(cederObjetDao.findById(pk).isPresent());
	}

	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	@Test
	public void testFindByExcludedPK() {
		final Cession c1 = cessionDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		CederObjetPK pk = cederObjetDao.findById(new CederObjetPK(c1, e1, 1)).get().getPk();
		// CederObjetPK pk = new CederObjetPK(c1, e1, 1);

		List<CederObjet> liste = cederObjetDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 5);

		pk = new CederObjetPK(c1, e1, 10);
		liste = cederObjetDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 6);
	}

	/**
	 * Test l'appel de la méthode findByEntite().
	 */
	@Test
	public void testFindByEntite() {
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(2).get();

		List<CederObjet> liste = cederObjetDao.findByEntite(e1);
		assertTrue(liste.size() == 4);

		liste = cederObjetDao.findByEntite(e2);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetId().
	 */
	@Test
	public void testFindByObjetId() {
		List<CederObjet> liste = cederObjetDao.findByObjetId(1);
		assertTrue(liste.size() == 3);

		liste = cederObjetDao.findByObjetId(2);
		assertTrue(liste.size() == 1);

		liste = cederObjetDao.findByObjetId(3);
		assertTrue(liste.size() == 2);

		liste = cederObjetDao.findByObjetId(4);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByEntiteObjet().
	 */
	@Test
	public void testFindByEntiteObjet() {
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(2).get();

		List<CederObjet> liste = cederObjetDao.findByEntiteObjet(e1, 1);
		assertTrue(liste.size() == 2);

		liste = cederObjetDao.findByEntiteObjet(e1, 10);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByEntiteObjet(e2, 1);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByEntiteObjet(e1, null);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByEntiteObjet(null, 1);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByEntiteObjet(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByCessionEntite().
	 */
	@Test
	public void testFindByCessionEntite() {
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(2).get();
		final Cession c1 = cessionDao.findById(1).get();
		final Cession c2 = cessionDao.findById(3).get();

		List<CederObjet> liste = cederObjetDao.findByCessionEntite(c1, e1);
		assertTrue(liste.size() == 1);

		liste = cederObjetDao.findByCessionEntite(c1, e2);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByCessionEntite(c2, e1);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByCessionEntite(c1, null);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByCessionEntite(null, e1);
		assertTrue(liste.size() == 0);

		liste = cederObjetDao.findByCessionEntite(null, null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un cederObjet.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 *
	 **/
	@Rollback(false)
	@Test
	public void testCrudCederObjet() {

		final CederObjet co = new CederObjet();
		final Cession c = cessionDao.findById(1).get();
		final Entite e = entiteDao.findById(3).get();
		final Integer objetId = 2;
		final float upValue = (float) 10.63598;
		final Unite u = uniteDao.findById(1).get();

		co.setCession(c);
		co.setEntite(e);
		co.setObjetId(objetId);
		co.setQuantite(null);
		// co.setVolume(value);
		co.setQuantiteUnite(u);
		// co.setVolumeUnite(u);

		// Test de l'insertion
		cederObjetDao.save(co);
		assertTrue(IterableUtils.toList(cederObjetDao.findAll()).size() == 7);

		// Test de la mise à jour
		final CederObjetPK pk = new CederObjetPK();
		pk.setCession(c);
		pk.setEntite(e);
		pk.setObjetId(objetId);
		final CederObjet co2 = cederObjetDao.findById(pk).get();
		assertNotNull(co2);
		assertTrue(co2.getCession().equals(c));
		assertTrue(co2.getEntite().equals(e));
		assertTrue(co2.getObjetId() == objetId);
		assertNull(co2.getQuantite());
		assertTrue(co2.getQuantiteUnite().equals(u));

		// update
		co2.setQuantite(upValue);
		cederObjetDao.save(co2);
		assertTrue(cederObjetDao.findById(pk).get().equals(co2));
		assertTrue(cederObjetDao.findById(pk).get().getQuantite() == (float) 10.636);

		// Test de la délétion
		cederObjetDao.deleteById(pk);
		assertFalse(cederObjetDao.findById(pk).isPresent());
	}

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {

		final CederObjet co1 = new CederObjet();
		final CederObjet co2 = new CederObjet();

		// L'objet 1 n'est pas égal à null
		assertFalse(co1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(co1.equals(co1));
		// 2 objets sont égaux entre eux
		assertTrue(co1.equals(co2));
		assertTrue(co2.equals(co1));

		populateClefsToTestEqualsAndHashCode(co1, co2);

		// dummy test
		final Banque b = new Banque();
		assertFalse(co1.equals(b));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final CederObjet co1 = new CederObjet();
		final CederObjet co2 = new CederObjet();
		final CederObjet co3 = new CederObjet();

		assertTrue(co1.hashCode() == co2.hashCode());
		assertTrue(co2.hashCode() == co3.hashCode());
		assertTrue(co3.hashCode() > 0);

		// teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(co1, co2);

		final int hash = co1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(co1.hashCode() == co2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == co1.hashCode());
		assertTrue(hash == co1.hashCode());
		assertTrue(hash == co1.hashCode());
		assertTrue(hash == co1.hashCode());
	}

	private void populateClefsToTestEqualsAndHashCode(final CederObjet co1, final CederObjet co2) throws ParseException {

		final Cession c1 = cessionDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		final CederObjetPK pk1 = new CederObjetPK(c1, e1, 1);
		final CederObjetPK pk2 = new CederObjetPK(c1, e1, 2);
		final CederObjetPK pk3 = new CederObjetPK(c1, e1, 1);
		final CederObjetPK[] pks = new CederObjetPK[] { null, pk1, pk2, pk3 };

		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {

				co1.setPk(pks[i]);
				co2.setPk(pks[k]);

				if (((i == k) || (i + k == 4))) {
					assertTrue(co1.equals(co2));
					assertTrue(co1.hashCode() == co2.hashCode());
				} else {
					assertFalse(co1.equals(co2));
				}
			}
		}
	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Cession c1 = cessionDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		final CederObjetPK pk = new CederObjetPK(c1, e1, 1);
		final CederObjet co1 = cederObjetDao.findById(pk).get();

		assertTrue(co1.toString().equals("{" + co1.getPk().toString() + "}"));

		final CederObjet co2 = new CederObjet();
		co2.setPk(null);
		assertTrue(co2.toString().equals("{Empty CederObjet}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final Cession c1 = cessionDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		final CederObjetPK pk = new CederObjetPK(c1, e1, 1);

		final CederObjet co1 = cederObjetDao.findById(pk).get();
		CederObjet co2 = new CederObjet();
		co2 = co1.clone();

		assertTrue(co1.equals(co2));

		if (co1.getPk() != null) {
			assertTrue(co1.getPk().equals(co2.getPk()));
		} else {
			assertNull(co2.getPk());
		}

		if (co1.getQuantite() != null) {
			assertTrue(co1.getQuantite().equals(co2.getQuantite()));
		} else {
			assertNull(co2.getQuantite());
		}

		if (co1.getQuantiteUnite() != null) {
			assertTrue(co1.getQuantiteUnite().equals(co2.getQuantiteUnite()));
		} else {
			assertNull(co2.getQuantiteUnite());
		}

		/*
		 * if (co1.getVolume() != null) {
		 * assertTrue(co1.getVolume().equals(co2.getVolume())); } else {
		 * assertNull(co2.getVolume()); }
		 * 
		 * if (co1.getVolumeUnite() != null) {
		 * assertTrue(co1.getVolumeUnite().equals(co2.getVolumeUnite())); } else {
		 * assertNull(co2.getVolumeUnite()); }
		 */

	}

	@Test
	public void testCountObjectCessed() {
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(2).get();
		final Entite e8 = entiteDao.findById(8).get();
		final Cession c1 = cessionDao.findById(1).get();
		final Cession c2 = cessionDao.findById(2).get();

		Long count = cederObjetDao.findObjectsCessedCount(c1, e1).get(0);
		assertTrue(count == 1);

		count = cederObjetDao.findObjectsCessedCount(c1, e8).get(0);
		assertTrue(count == 1);

		count = cederObjetDao.findObjectsCessedCount(c1, e2).get(0);
		assertTrue(count == 0);

		count = cederObjetDao.findObjectsCessedCount(c2, e1).get(0);
		assertTrue(count == 3);

		count = cederObjetDao.findObjectsCessedCount(c2, e8).get(0);
		assertTrue(count == 0);

		count = cederObjetDao.findObjectsCessedCount(c1, null).get(0);
		assertTrue(count == 0);

		count = cederObjetDao.findObjectsCessedCount(null, e1).get(0);
		assertTrue(count == 0);

		count = cederObjetDao.findObjectsCessedCount(null, null).get(0);
		assertTrue(count == 0);
	}

	@Test
	public void testFindCodesEchantillonByCession() {

		final Cession c1 = cessionDao.findById(1).get();
		final Cession c2 = cessionDao.findById(2).get();
		final Cession c3 = cessionDao.findById(3).get();

		List<String> codes = cederObjetDao.findCodesEchantillonByCession(c1);
		assertTrue(codes.size() == 1);
		assertTrue(codes.contains("PTRA.1"));

		codes = cederObjetDao.findCodesEchantillonByCession(c2);
		assertTrue(codes.size() == 3);
		assertTrue(codes.get(0).equals("EHT.1"));
		assertTrue(codes.get(1).equals("PTRA.1"));
		assertTrue(codes.get(2).equals("PTRA.2"));

		codes = cederObjetDao.findCodesEchantillonByCession(c3);
		assertTrue(codes.isEmpty());

		codes = cederObjetDao.findCodesEchantillonByCession(null);
		assertTrue(codes.isEmpty());
	}

	@Test
	public void testFindCodesDeriveByCession() {

		final Cession c1 = cessionDao.findById(1).get();
		final Cession c2 = cessionDao.findById(2).get();
		final Cession c3 = cessionDao.findById(3).get();

		List<String> codes = cederObjetDao.findCodesDeriveByCession(c1);
		assertTrue(codes.size() == 1);
		assertTrue(codes.contains("EHT.1.1"));

		codes = cederObjetDao.findCodesDeriveByCession(c2);
		assertTrue(codes.isEmpty());

		codes = cederObjetDao.findCodesDeriveByCession(c3);
		assertTrue(codes.isEmpty());

		codes = cederObjetDao.findCodesDeriveByCession(null);
		assertTrue(codes.isEmpty());
	}

	@Test
	public void testFindCountObjCession() {
		// nulls
		Long cc = cederObjetDao.findCountObjCession(null, null).get(0);
		assertTrue(cc.longValue() == 0);
		cc = cederObjetDao.findCountObjCession(1, entiteDao.findById(3).get()).get(0);
		assertTrue(cc.longValue() == 2);
		cc = cederObjetDao.findCountObjCession(2, entiteDao.findById(3).get()).get(0);
		assertTrue(cc.longValue() == 1);
		cc = cederObjetDao.findCountObjCession(4, entiteDao.findById(3).get()).get(0);
		assertTrue(cc.longValue() == 0);
		cc = cederObjetDao.findCountObjCession(1, entiteDao.findById(8).get()).get(0);
		assertTrue(cc.longValue() == 1);

	}
}
