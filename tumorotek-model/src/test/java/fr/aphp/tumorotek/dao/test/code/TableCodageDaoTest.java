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
package fr.aphp.tumorotek.dao.test.code;

import java.util.List;

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

import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.Config;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 *
 * Classe de test pour le DAO TableCodesDao et le bean du domaine TableCodes.
 * Classe de test créée le 08/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.3
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class TableCodageDaoTest extends AbstractDaoTest {

	@Autowired
	TableCodageDao tableCodageDao;

	final String updatedNom = "Table maj";

	@Test
	public void testReadAlltableCodes() {
		final List<TableCodage> tables = IterableUtils.toList(tableCodageDao.findAll());
		assertTrue(tables.size() == 5);
	}

	/**
	 * Test l'appel de la méthode findByNom().
	 */
	@Test
	public void testFindByNom() {
		List<TableCodage> tables = tableCodageDao.findByNom("ADICAP");
		assertTrue(tables.size() == 1);
		tables = tableCodageDao.findByNom("TEST");
		assertTrue(tables.size() == 0);
	}

	// /**
	// * Test l'appel de la méthode findByDoublon().
	// */
	// @Test
	// public void testFindByDoublon() {
	// List<TableCodage> tables = tableCodageDao.findByDoublon("CIM_MASTER");
	// assertTrue(tables.size() == 1);
	// tables = tableCodageDao.findByDoublon("CIMO");
	// assertTrue(tables.size() == 0);
	// }

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une TableCodage.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {
		final TableCodage t = new TableCodage();

		t.setNom("CIMO");
		// Test de l'insertion
		tableCodageDao.save(t);
		assertEquals(new Integer(6), t.getTableCodageId());

		// Test de la mise à jour
		final TableCodage t2 = tableCodageDao.findById(new Integer(6)).get();
		assertNotNull(t2);
		assertTrue(t2.getNom().equals("CIMO"));
		t2.setNom(updatedNom);
		tableCodageDao.save(t2);
		assertTrue(tableCodageDao.findById(new Integer(6)).get().getNom().equals(updatedNom));

		// Test de la délétion
		tableCodageDao.deleteById(new Integer(6));
		assertFalse(tableCodageDao.findById(new Integer(6)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String nom = "Table1";
		final String nom2 = "Table2";
		final TableCodage t1 = new TableCodage();
		final TableCodage t2 = new TableCodage();

		// L'objet 1 n'est pas égal à null
		assertFalse(t1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(t1.equals(t1));

		/* null --> Ids ne pouvant etre nuls car table systemes */
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));

		/* Id */
		t2.setNom(nom);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		t1.setNom(nom2);
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1));
		t1.setNom(nom);
		assertTrue(t1.equals(t2));
		assertTrue(t2.equals(t1));

		final Categorie c = new Categorie();
		assertFalse(t1.equals(c));

		final Banque b = new Banque();
		assertFalse(t1.equals(b));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String nom = "Table";
		final TableCodage t1 = new TableCodage();
		t1.setNom(nom);
		final TableCodage t2 = new TableCodage();
		t2.setNom(nom);
		final TableCodage t3 = new TableCodage();
		t3.setNom(null);
		assertTrue(t3.hashCode() > 0);

		final int hash = t1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(t1.hashCode() == t2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
		assertTrue(hash == t1.hashCode());
	}

	@Test
	@Transactional
	public void testClone() {
		final TableCodage t1 = tableCodageDao.findById(1).get();
		t1.setVersion("12"); // pour eviter null
		final TableCodage clone = t1.clone();
		assertTrue(t1.equals(clone));
		assertTrue(clone.getTableCodageId().equals(t1.getTableCodageId()));
		assertTrue(clone.getNom().equals(t1.getNom()));
		assertTrue(clone.getVersion().equals(t1.getVersion()));
		assertTrue(clone.getCodeAssignes().equals(t1.getCodeAssignes()));
		assertTrue(clone.getCodeSelects().equals(t1.getCodeSelects()));
	}

	@Test
	public void testToString() {
		final TableCodage t1 = tableCodageDao.findById(1).get();
		assertTrue(t1.toString().equals("ADICAP 5.03"));
	}

}
