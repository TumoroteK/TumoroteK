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
package fr.aphp.tumorotek.test.dao.coeur.annotation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

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
import fr.aphp.tumorotek.dao.annotation.CatalogueDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationBanqueDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanque;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanquePK;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO TableAnnotationDao et le bean du domaine
 * TableAnnotation. Classe de test créée le 28/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class TableAnnotationDaoTest extends AbstractDaoTest {

	@Autowired
	TableAnnotationDao tableAnnotationDao;

	@Autowired
	TableAnnotationBanqueDao tableAnnotationBanqueDao;

	@Autowired
	EntiteDao entiteDao;

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	CatalogueDao catalogueDao;

	@Autowired
	PlateformeDao plateformeDao;

	@Test
	public void testToString() throws ParseException {
		final TableAnnotation t1 = tableAnnotationDao.findById(1).get();
		assertTrue(t1.toString().equals("{Patient.TABLE_PAT1}"));

		final TableAnnotation t2 = new TableAnnotation();
		assertTrue(t2.toString().equals("{Empty TableAnnotation}"));
		t2.setEntite(entiteDao.findById(1).get());
		assertTrue(t2.toString().equals("{Empty TableAnnotation}"));
		t2.setEntite(null);
		t2.setNom("testTable");
		assertTrue(t2.toString().equals("{Empty TableAnnotation}"));
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllTables() {
		final List<TableAnnotation> tabs = IterableUtils.toList(tableAnnotationDao.findAll());
		assertTrue(tabs.size() == 11);
	}

	@Test
	public void testFindByNom() {
		List<TableAnnotation> tabs = tableAnnotationDao.findByNom("TABLE_ECHAN%");
		assertTrue(tabs.size() == 2);
		tabs = tableAnnotationDao.findByNom("PREL%");
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByNom(null);
		assertTrue(tabs.size() == 0);
	}

	@Test
	public void testFindByEntiteAndBanque() {
		Entite e = entiteDao.findByNom("Echantillon").get(0);
		Banque b = banqueDao.findById(1).get();
		List<TableAnnotation> tabs = tableAnnotationDao.findByEntiteAndBanque(e, b);
		assertTrue(tabs.size() == 4);
		// verfie l'ordre
		assertTrue(tabs.get(0).equals(tableAnnotationDao.findById(9).get()));
		assertTrue(tabs.get(1).equals(tableAnnotationDao.findById(3).get()));
		assertTrue(tabs.get(2).equals(tableAnnotationDao.findById(4).get()));
		assertTrue(tabs.get(3).equals(tableAnnotationDao.findById(5).get()));
		e = entiteDao.findByNom("Patient").get(0);
		tabs = tableAnnotationDao.findByEntiteAndBanque(e, b);
		assertTrue(tabs.size() == 2);
		b = banqueDao.findById(2).get();
		tabs = tableAnnotationDao.findByEntiteAndBanque(e, b);
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteAndBanque(null, b);
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteAndBanque(e, null);
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteAndBanque(null, null);
		assertTrue(tabs.size() == 0);

	}

	@Test
	public void testFindByEntiteBanqueAndCatalogue() {
		final Entite e = entiteDao.findByNom("Echantillon").get(0);
		Banque b = banqueDao.findById(1).get();
		List<TableAnnotation> tabs = tableAnnotationDao.findByEntiteBanqueAndCatalogue(e, b, "INCa");
		assertTrue(tabs.size() == 2);
		// verfie l'ordre
		assertTrue(tabs.get(0).equals(tableAnnotationDao.findById(9).get()));
		assertTrue(tabs.get(1).equals(tableAnnotationDao.findById(3).get()));
		tabs = tableAnnotationDao.findByEntiteBanqueAndCatalogue(e, b, "tmp");
		assertTrue(tabs.size() == 0);
		b = banqueDao.findById(4).get();
		tabs = tableAnnotationDao.findByEntiteBanqueAndCatalogue(e, b, "INCa");
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteBanqueAndCatalogue(null, b, "INCa");
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteBanqueAndCatalogue(e, null, "INCa");
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteBanqueAndCatalogue(null, null, null);
		assertTrue(tabs.size() == 0);

	}

	@Test
	public void testFindByEntite() {
		Entite e = entiteDao.findByNom("Echantillon").get(0);
		final Plateforme pf1 = plateformeDao.findById(1).get();
		List<TableAnnotation> tabs = tableAnnotationDao.findByEntiteAndPlateforme(e, pf1);
		assertTrue(tabs.size() == 2);
		// verfie l'ordre
		// assertTrue(tabs.get(0).equals(tableAnnotationDao.findById(3))).get();
		assertTrue(tabs.get(0).equals(tableAnnotationDao.findById(4).get()));
		assertTrue(tabs.get(1).equals(tableAnnotationDao.findById(5).get()));
		e = entiteDao.findByNom("Patient").get(0);
		tabs = tableAnnotationDao.findByEntiteAndPlateforme(e, pf1);
		assertTrue(tabs.size() == 1);
		tabs = tableAnnotationDao.findByEntiteAndPlateforme(e, null);
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteAndPlateforme(null, pf1);
		assertTrue(tabs.size() == 0);
		tabs = tableAnnotationDao.findByEntiteAndPlateforme(null, null);
		assertTrue(tabs.size() == 0);
	}

	@Test
	public void testFindMaxOrdreForBanqueAndEntite() {
		Entite e = entiteDao.findByNom("Echantillon").get(0);
		Banque b = banqueDao.findById(1).get();
		List<Integer> maxOrdre = tableAnnotationDao.findMaxOrdreForBanqueAndEntite(e, b);
		assertNotNull(maxOrdre.get(0) == 4);
		e = entiteDao.findByNom("Patient").get(0);
		maxOrdre = tableAnnotationDao.findMaxOrdreForBanqueAndEntite(e, b);
		assertTrue(maxOrdre.get(0) == 2);
		b = banqueDao.findById(2).get();
		maxOrdre = tableAnnotationDao.findMaxOrdreForBanqueAndEntite(e, b);
		assertNull(maxOrdre.get(0));
		maxOrdre = tableAnnotationDao.findMaxOrdreForBanqueAndEntite(null, b);
		assertNull(maxOrdre.get(0));
		maxOrdre = tableAnnotationDao.findMaxOrdreForBanqueAndEntite(e, null);
		assertNull(maxOrdre.get(0));
		maxOrdre = tableAnnotationDao.findMaxOrdreForBanqueAndEntite(null, null);
		assertNull(maxOrdre.get(0));
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		final TableAnnotation t1 = tableAnnotationDao.findById(1).get();
		List<TableAnnotation> tabs = tableAnnotationDao.findByExcludedId(1);
		assertTrue(tabs.size() == 10);
		assertFalse(tabs.contains(t1));
		tabs = tableAnnotationDao.findByExcludedId(13);
		assertTrue(tabs.size() == 11);
		tabs = tableAnnotationDao.findByExcludedId(null);
		assertTrue(tabs.size() == 0);
	}

	@Test
	public void testFindByPlateforme() {
		final Plateforme pf1 = plateformeDao.findById(1).get();
		List<TableAnnotation> tabs = tableAnnotationDao.findByPlateforme(pf1);
		assertTrue(tabs.size() == 5);
		final Plateforme pf2 = plateformeDao.findById(2).get();
		tabs = tableAnnotationDao.findByPlateforme(pf2);
		assertTrue(tabs.size() == 0);
	}

	@Test
	public void testFindByBanques() {
		final List<Banque> banks = new ArrayList<>();
		banks.add(banqueDao.findById(1).get());
		List<TableAnnotation> tabs = tableAnnotationDao.findByBanques(banks);
		assertTrue(tabs.size() == 9);
		banks.add(banqueDao.findById(2).get());
		tabs = tableAnnotationDao.findByBanques(banks);
		assertTrue(tabs.size() == 9);
		banks.clear();
		banks.add(banqueDao.findById(4).get());
		tabs = tableAnnotationDao.findByBanques(banks);
		assertTrue(tabs.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une tableAnnotation.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudTableAnnotation() {

		final TableAnnotation table = new TableAnnotation();
		table.setNom("testTable");
		table.setDescription("testTab descr");
		table.setEntite(entiteDao.findByNom("Conteneur").get(0));
		final Catalogue cINca = catalogueDao.findById(1).get();
		table.setCatalogue(cINca);
		final TableAnnotationBanquePK pk1 = new TableAnnotationBanquePK(banqueDao.findById(1).get(), table);
		final TableAnnotationBanque tab1 = new TableAnnotationBanque();
		tab1.setPk(pk1);
		tab1.setOrdre(1);
		final TableAnnotationBanquePK pk2 = new TableAnnotationBanquePK(banqueDao.findById(2).get(), table);
		final TableAnnotationBanque tab2 = new TableAnnotationBanque();
		tab2.setPk(pk2);
		tab2.setOrdre(2);
		final Set<TableAnnotationBanque> tabs = new HashSet<>();
		tabs.add(tab1);
		tabs.add(tab2);
		table.setTableAnnotationBanques(tabs);

		// Test de l'insertion
		tableAnnotationDao.save(table);
		assertTrue(table.getTableAnnotationId().equals(12));

		// Test de la mise à jour
		final TableAnnotation table2 = tableAnnotationDao.findById(12).get();
		assertNotNull(table2);
		assertTrue(table2.getNom().equals("testTable"));
		assertTrue(table2.getDescription().equals("testTab descr"));
		assertTrue(table2.getEntite().equals(entiteDao.findByNom("Conteneur").get(0)));
		assertTrue(table2.getCatalogue().equals(cINca));
		assertTrue(table2.getTableAnnotationBanques().size() == 2);
		assertTrue(table2.getTableAnnotationBanques().contains(tab1));
		assertTrue(table2.getTableAnnotationBanques().contains(tab2));

		// update
		table2.setNom("test2");
		table2.setDescription(null);
		table2.setCatalogue(null);
		final TableAnnotationBanquePK pk3 = new TableAnnotationBanquePK(banqueDao.findById(3).get(), table2);
		final TableAnnotationBanque tab3 = new TableAnnotationBanque();
		tab3.setPk(pk3);
		tab3.setOrdre(1);
		table2.getTableAnnotationBanques().add(tableAnnotationBanqueDao.save(tab1));
		table2.getTableAnnotationBanques().add(tableAnnotationBanqueDao.save(tab2));
		table2.getTableAnnotationBanques().add(tableAnnotationBanqueDao.save(tab3));
		// table2.setTableAnnotationBanques(tabs);

		tableAnnotationDao.save(table2);

		assertTrue(tableAnnotationDao.findById(12).get().getNom().equals("test2"));
		assertFalse(tableAnnotationDao.findById(12).get().getDescription() != null);
		assertFalse(tableAnnotationDao.findById(12).get().getCatalogue() != null);
		assertTrue(tableAnnotationDao.findById(12).get().getEntite().equals(entiteDao.findByNom("Conteneur").get(0)));
		assertTrue(table2.getTableAnnotationBanques().size() == 3);
		assertTrue(table2.getTableAnnotationBanques().contains(tab2));
		assertTrue(table2.getTableAnnotationBanques().contains(tab3));
		assertTrue(table2.getTableAnnotationBanques().contains(tab1));

		// Test de la délétion
		tableAnnotationDao.deleteById(new Integer(12));
		assertFalse(tableAnnotationDao.findById(new Integer(12)).isPresent());
		testReadAllTables();
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEqualsAndHashCode() {
		final TableAnnotation t1 = new TableAnnotation();
		final TableAnnotation t2 = new TableAnnotation();
		assertFalse(t1.equals(null));
		assertNotNull(t2);
		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode() == t2.hashCode());

		final String s1 = "nom1";
		final String s2 = "nom2";
		final String s3 = new String("nom2");

		t1.setNom(s1);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		assertTrue(t1.hashCode() != t2.hashCode());
		t2.setNom(s2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		assertTrue(t1.hashCode() != t2.hashCode());
		t1.setNom(s2);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		assertTrue(t1.hashCode() == t2.hashCode());
		t1.setNom(s3);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		assertTrue(t1.hashCode() == t2.hashCode());

		final Entite e1 = entiteDao.findById(1).get();
		final Entite e2 = entiteDao.findById(2).get();
		final Entite e3 = new Entite();
		e3.setNom(e2.getNom());
		assertFalse(e1.equals(e2));
		assertFalse(e1.hashCode() == e2.hashCode());
		assertTrue(e2.equals(e3));
		t1.setEntite(e1);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		assertTrue(t1.hashCode() != t2.hashCode());
		t2.setEntite(e2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		assertTrue(t1.hashCode() != t2.hashCode());
		t1.setEntite(e3);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		assertTrue(t1.hashCode() == t2.hashCode());
		t1.setEntite(e2);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		assertTrue(t1.hashCode() == t2.hashCode());

		final Plateforme p1 = plateformeDao.findById(1).get();
		final Plateforme p2 = plateformeDao.findById(2).get();
		final Plateforme p3 = new Plateforme();
		p3.setNom(p2.getNom());
		assertFalse(p1.equals(p2));
		assertFalse(p1.hashCode() == p2.hashCode());
		assertTrue(p2.equals(p3));
		t1.setPlateforme(p1);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		assertTrue(t1.hashCode() != t2.hashCode());
		t2.setPlateforme(p2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		assertTrue(t1.hashCode() != t2.hashCode());
		t1.setPlateforme(p3);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		assertTrue(t1.hashCode() == t2.hashCode());
		t1.setPlateforme(p2);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));
		assertTrue(t1.hashCode() == t2.hashCode());

		// dummy
		final Categorie c = new Categorie();
		assertFalse(t1.equals(c));
	}

	@Test
	@Transactional
	public void testClone() {
		final TableAnnotation t = tableAnnotationDao.findById(1).get();
		final TableAnnotation t2 = t.clone();
		assertTrue(t.equals(t2));
		if (t.getTableAnnotationId() != null) {
			assertTrue(t.getTableAnnotationId().equals(t2.getTableAnnotationId()));
		} else {
			assertNull(t2.getTableAnnotationId());
		}
		if (t.getNom() != null) {
			assertTrue(t.getNom().equals(t2.getNom()));
		} else {
			assertNull(t2.getNom());
		}
		if (t.getDescription() != null) {
			assertTrue(t.getDescription().equals(t2.getDescription()));
		} else {
			assertNull(t2.getDescription());
		}
		if (t.getCatalogue() != null) {
			assertTrue(t.getCatalogue().equals(t2.getCatalogue()));
		} else {
			assertNull(t2.getCatalogue());

		}
		if (t.getEntite() != null) {
			assertTrue(t.getEntite().equals(t2.getEntite()));
		} else {
			assertNull(t2.getEntite());
		}
		if (t.getChampAnnotations() != null) {
			assertTrue(t.getChampAnnotations().equals(t2.getChampAnnotations()));
		} else {
			assertNull(t2.getChampAnnotations());
		}
		if (t.getPlateforme() != null) {
			assertTrue(t.getPlateforme().equals(t2.getPlateforme()));
		} else {
			assertNull(t2.getPlateforme());
		}
	}

	@Test
	public void testFindByCatalogues() {
		final Catalogue c = catalogueDao.findById(1).get();
		final List<Catalogue> catas = new ArrayList<>();
		catas.add(c);
		List<TableAnnotation> tabs = tableAnnotationDao.findByCatalogues(catas);
		assertTrue(tabs.size() == 4);

		final Catalogue c2 = catalogueDao.findById(2).get();
		catas.add(c2);
		tabs = tableAnnotationDao.findByCatalogues(catas);
		assertTrue(tabs.size() == 6);
		assertTrue(tabs.get(0).getNom().equals("INCa-Patient"));
		assertTrue(tabs.get(1).getNom().equals("INCa-Patient-Tabac"));
	}

	@Test
	public void testFindByCatalogueAndChpEdit() {
		final Catalogue c = catalogueDao.findById(1).get();
		final List<TableAnnotation> tabs = tableAnnotationDao.findByCatalogueAndChpEdit(c);
		assertTrue(tabs.size() == 3);
		assertTrue(tabs.get(0).getEntite().getNom().equals("Patient"));
		assertTrue(tabs.get(1).getEntite().getNom().equals("Prelevement"));
		assertTrue(tabs.get(2).getEntite().getNom().equals("Echantillon"));
	}
}
