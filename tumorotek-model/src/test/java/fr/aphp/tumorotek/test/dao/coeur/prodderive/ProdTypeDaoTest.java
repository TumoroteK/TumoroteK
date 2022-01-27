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

import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO ProdTypeDao et le bean du domaine ProdType. Classe
 * créée le 28/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ProdTypeDaoTest extends AbstractDaoTest {

	@Autowired
	ProdTypeDao prodTypeDao;
	
	@Autowired
	PlateformeDao plateformeDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAll() {
		final List<ProdType> types = IterableUtils.toList(prodTypeDao.findAll());
		assertTrue(types.size() == 3);
	}

	@Test
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1).get();
		List<? extends TKThesaurusObject> list = prodTypeDao.findByPfOrder(pf);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNom().equals("ADN"));
		pf = plateformeDao.findById(2).get();
		list = prodTypeDao.findByPfOrder(pf);
		assertTrue(list.size() == 0);
		list = prodTypeDao.findByPfOrder(null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByType().
	 */
	@Test
	public void testFindByType() {
		List<ProdType> types = prodTypeDao.findByType("ADN");
		assertTrue(types.size() == 1);
		types = prodTypeDao.findByType("RNA");
		assertTrue(types.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByProdDeriveId().
	 */
	@Test
	public void testFindByProdDeriveId() {
		List<ProdType> types = prodTypeDao.findByProdDeriveId(1);
		assertTrue(types.size() == 1);
		assertTrue(types.get(0).getId() == 1);
		types = prodTypeDao.findByProdDeriveId(3);
		assertTrue(types.size() == 1);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<ProdType> liste = prodTypeDao.findByExcludedId(1);
		assertTrue(liste.size() == 2);
		final ProdType type = liste.get(0);
		assertNotNull(type);
		assertTrue(type.getId() == 2);

		liste = prodTypeDao.findByExcludedId(15);
		assertTrue(liste.size() == 3);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un ProdType.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrudProdQualite() throws Exception {

		final ProdType p = new ProdType();
		final String updatedType = "MAJ";
		p.setPlateforme(plateformeDao.findById(1).get());
		p.setNom("TYPE");
		// Test de l'insertion
		prodTypeDao.save(p);
		assertEquals(new Integer(4), p.getId());

		// Test de la mise à jour
		final ProdType p2 = prodTypeDao.findById(new Integer(4)).get();
		assertNotNull(p2);
		assertTrue(p2.getNom().equals("TYPE"));
		p2.setNom(updatedType);
		prodTypeDao.save(p2);
		assertTrue(prodTypeDao.findById(new Integer(4)).get().getNom().equals(updatedType));

		// Test de la délétion
		prodTypeDao.deleteById(new Integer(4));
		assertFalse(prodTypeDao.findById(new Integer(4)).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final String type = "Type";
		final String type2 = "Type2";
		final ProdType p1 = new ProdType();
		p1.setNom(type);
		final ProdType p2 = new ProdType();
		p2.setNom(type);

		// L'objet 1 n'est pas égal à null
		assertFalse(p1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(p1.equals(p1));
		// 2 objets sont égaux entre eux
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		// Vérification de la différenciation de 2 objets
		p2.setNom(type2);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));

		p2.setNom(null);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));

		p1.setNom(null);
		assertTrue(p1.equals(p2));
		p2.setNom(type);
		assertFalse(p1.equals(p2));

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		p1.setNom(p2.getNom());
		p1.setPlateforme(pf1);
		p2.setPlateforme(pf1);
		assertTrue(p1.equals(p2));
		p2.setPlateforme(pf2);
		assertFalse(p1.equals(p2));

		final Categorie c = new Categorie();
		assertFalse(p1.equals(c));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final String type = "Type";
		final ProdType p1 = new ProdType();
		p1.setNom(type);
		final ProdType p2 = new ProdType();
		p2.setNom(type);
		final ProdType p3 = new ProdType();
		p3.setNom(null);
		assertTrue(p3.hashCode() > 0);

		final Plateforme pf1 = plateformeDao.findById(1).get();
		final Plateforme pf2 = plateformeDao.findById(2).get();
		p1.setPlateforme(pf1);
		p2.setPlateforme(pf1);
		p3.setPlateforme(pf2);

		final int hash = p1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(p1.hashCode() == p2.hashCode());
		assertFalse(p1.hashCode() == p3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());

	}

	/**
	 * test toString().
	 */
	@Test
	public void testToString() {
		final ProdType p1 = prodTypeDao.findById(1).get();
		assertTrue(p1.toString().equals("{" + p1.getNom() + "}"));

		final ProdType p2 = new ProdType();
		assertTrue(p2.toString().equals("{Empty ProdType}"));
	}
}
