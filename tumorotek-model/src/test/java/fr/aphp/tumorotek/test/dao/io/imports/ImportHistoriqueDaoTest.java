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
package fr.aphp.tumorotek.test.dao.io.imports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

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

import fr.aphp.tumorotek.dao.io.imports.ImportHistoriqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.io.imports.ImportationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ImportHistoriqueDao et le bean du domaine
 * ImportTemplate. Classe créée le 09/02/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ImportHistoriqueDaoTest extends AbstractDaoTest {

	@Autowired
	ImportHistoriqueDao importHistoriqueDao;
	
	@Autowired
	ImportTemplateDao importTemplateDao;
	
	@Autowired
	ImportationDao importationDao;
	
	@Autowired
	EntiteDao entiteDao;
	
	@Autowired
	UtilisateurDao utilisateurDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<ImportHistorique> liste = IterableUtils.toList(importHistoriqueDao.findAll());
		assertTrue(liste.size() == 3);
	}

	/**
	 * Test l'appel de la méthode findByTemplateWithOrder().
	 */
	@Test
	public void testFindByTemplateWithOrder() {
		final ImportTemplate it1 = importTemplateDao.findById(1).get();
		List<ImportHistorique> liste = importHistoriqueDao.findByTemplateWithOrder(it1);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(1).getImportHistoriqueId() == 1);

		final ImportTemplate it2 = importTemplateDao.findById(2).get();
		liste = importHistoriqueDao.findByTemplateWithOrder(it2);
		assertTrue(liste.size() == 1);

		final ImportTemplate it3 = importTemplateDao.findById(3).get();
		liste = importHistoriqueDao.findByTemplateWithOrder(it3);
		assertTrue(liste.size() == 0);

		liste = importHistoriqueDao.findByTemplateWithOrder(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<ImportHistorique> liste = importHistoriqueDao.findByExcludedId(1);
		assertTrue(liste.size() == 2);

		liste = importHistoriqueDao.findByExcludedId(100);
		assertTrue(liste.size() == 3);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un ImportTemplate.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Transactional
	@Test
	@Rollback(false)
	public void testCrud() throws Exception {

		final Entite e3 = entiteDao.findById(3).get();
		final ImportTemplate it = importTemplateDao.findById(1).get();
		final Utilisateur u = utilisateurDao.findById(1).get();
		final Calendar cal = Calendar.getInstance();

		final ImportHistorique ih1 = new ImportHistorique();
		ih1.setImportTemplate(it);
		ih1.setUtilisateur(u);
		ih1.setDate(cal);

		final Importation i1 = new Importation();
		i1.setObjetId(1);
		i1.setEntite(e3);
		i1.setImportHistorique(ih1);
		final Importation i2 = new Importation();
		i2.setObjetId(2);
		i2.setEntite(e3);
		i2.setImportHistorique(ih1);
		final Set<Importation> imports = new HashSet<>();
		imports.add(i1);
		imports.add(i2);
		ih1.setImportations(imports);

		// Test de l'insertion
		importHistoriqueDao.save(ih1);
		assertEquals(new Integer(4), ih1.getImportHistoriqueId());
		assertTrue(IterableUtils.toList(importationDao.findAll()).size() == 4);

		// Test de la mise à jour
		final ImportHistorique ih2 = importHistoriqueDao.findById(new Integer(4)).get();
		assertNotNull(ih2);
		assertNotNull(ih2.getImportTemplate());
		assertNotNull(ih2.getUtilisateur());
		assertTrue(ih2.getDate().equals(cal));
		assertTrue(ih2.getImportations().size() == 2);

		final Calendar cal2 = Calendar.getInstance();
		ih2.setDate(cal2);
		importHistoriqueDao.save(ih2);
		assertTrue(importHistoriqueDao.findById(new Integer(4)).get().getDate().equals(cal2));
		assertTrue(importHistoriqueDao.findById(new Integer(4)).get().getImportations().size() == 2);

		// Test de la délétion
		importHistoriqueDao.deleteById(new Integer(4));
		assertFalse(importHistoriqueDao.findById(new Integer(4)).isPresent());
		assertTrue(IterableUtils.toList(importationDao.findAll()).size() == 2);
	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MONTH, 1);
		final ImportTemplate t1 = importTemplateDao.findById(1).get();
		final ImportTemplate t2 = importTemplateDao.findById(2).get();
		final ImportHistorique ih1 = new ImportHistorique();
		final ImportHistorique ih2 = new ImportHistorique();

		// L'objet 1 n'est pas égal à null
		assertFalse(ih1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(ih1.equals(ih1));
		// 2 objets sont égaux entre eux
		assertTrue(ih1.equals(ih2));
		assertTrue(ih2.equals(ih1));

		ih1.setImportTemplate(null);
		ih1.setDate(null);
		ih2.setImportTemplate(null);
		ih2.setDate(null);
		assertTrue(ih1.equals(ih2));
		ih2.setDate(cal1);
		assertFalse(ih1.equals(ih2));
		ih1.setDate(cal1);
		assertTrue(ih1.equals(ih2));
		ih2.setDate(cal2);
		assertFalse(ih1.equals(ih2));
		ih2.setDate(null);
		assertFalse(ih1.equals(ih2));
		ih2.setImportTemplate(t1);
		assertFalse(ih1.equals(ih2));

		ih1.setImportTemplate(t1);
		ih1.setDate(null);
		ih2.setDate(null);
		ih2.setImportTemplate(t1);
		assertTrue(ih1.equals(ih2));
		ih2.setImportTemplate(t2);
		assertFalse(ih1.equals(ih2));
		ih2.setDate(cal1);
		assertFalse(ih1.equals(ih2));

		// Vérification de la différenciation de 2 objets
		ih1.setDate(cal1);
		assertFalse(ih1.equals(ih2));
		ih2.setDate(cal2);
		ih2.setImportTemplate(t1);
		assertFalse(ih1.equals(ih2));

		final Categorie c3 = new Categorie();
		assertFalse(ih1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MONTH, 1);
		final ImportTemplate t1 = importTemplateDao.findById(1).get();
		final ImportTemplate t2 = importTemplateDao.findById(2).get();
		final ImportHistorique ih1 = new ImportHistorique();
		final ImportHistorique ih2 = new ImportHistorique();
		// null
		assertTrue(ih1.hashCode() == ih2.hashCode());

		// Nom
		ih2.setDate(cal1);
		assertFalse(ih1.hashCode() == ih2.hashCode());
		ih1.setDate(cal2);
		assertFalse(ih1.hashCode() == ih2.hashCode());
		ih1.setDate(cal1);
		assertTrue(ih1.hashCode() == ih2.hashCode());

		// ProtocoleType
		ih2.setImportTemplate(t1);
		assertFalse(ih1.hashCode() == ih2.hashCode());
		ih1.setImportTemplate(t2);
		assertFalse(ih1.hashCode() == ih2.hashCode());
		ih1.setImportTemplate(t1);
		assertTrue(ih1.hashCode() == ih2.hashCode());

		// un même objet garde le même hashcode dans le temps
		final int hash = ih1.hashCode();
		assertTrue(hash == ih1.hashCode());
		assertTrue(hash == ih1.hashCode());
		assertTrue(hash == ih1.hashCode());
		assertTrue(hash == ih1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final ImportHistorique ih1 = importHistoriqueDao.findById(1).get();
		assertTrue(ih1.toString()
				.equals("{" + ih1.getDate() + ", " + ih1.getImportTemplate().getNom() + "(ImportTemplate)}"));

		final ImportHistorique ih2 = new ImportHistorique();
		assertTrue(ih2.toString().equals("{Empty ImportHistorique}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	@Transactional
	public void testClone() {
		final ImportHistorique ih1 = importHistoriqueDao.findById(1).get();
		ImportHistorique ih2 = new ImportHistorique();
		ih2 = ih1.clone();

		assertTrue(ih1.equals(ih2));

		if (ih1.getImportHistoriqueId() != null) {
			assertTrue(ih1.getImportHistoriqueId() == ih2.getImportHistoriqueId());
		} else {
			assertNull(ih2.getImportHistoriqueId());
		}

		if (ih1.getImportTemplate() != null) {
			assertTrue(ih1.getImportTemplate().equals(ih2.getImportTemplate()));
		} else {
			assertNull(ih2.getImportTemplate());
		}

		if (ih1.getUtilisateur() != null) {
			assertTrue(ih1.getUtilisateur().equals(ih2.getUtilisateur()));
		} else {
			assertNull(ih2.getUtilisateur());
		}

		if (ih1.getDate() != null) {
			assertTrue(ih1.getDate().equals(ih2.getDate()));
		} else {
			assertNull(ih2.getDate());
		}

		if (ih1.getImportations() != null) {
			assertTrue(ih1.getImportations().equals(ih2.getImportations()));
		} else {
			assertNull(ih2.getImportations());
		}
	}

	@Test
	public void testFindPrelevementByImportHistorique() {
		final ImportHistorique ih1 = importHistoriqueDao.findById(1).get();
		final List<Prelevement> prels = new ArrayList<>();
		prels.addAll(importHistoriqueDao.findPrelevementByImportHistorique(ih1));
		assertTrue(prels.size() == 1);
		assertTrue(prels.get(0).getPrelevementId() == 4);

		prels.clear();

		prels.addAll(importHistoriqueDao.findPrelevementByImportHistorique(importHistoriqueDao.findById(2).get()));
		assertTrue(prels.isEmpty());

		prels.addAll(importHistoriqueDao.findPrelevementByImportHistorique(null));
		assertTrue(prels.isEmpty());

	}

}
