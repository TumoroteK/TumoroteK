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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import fr.aphp.tumorotek.dao.annotation.AnnotationDefautDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO AnnotationDefautDao et le bean du domaine
 * AnnotationDefaut. Classe de test créée le 01/02/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class AnnotationDefautDaoTest extends AbstractDaoTest {

	@Autowired
	AnnotationDefautDao annotationDefautDao;

	@Autowired
	ItemDao itemDao;

	@Autowired
	ChampAnnotationDao champAnnotationDao;

	@Autowired
	TableAnnotationDao tableAnnotationDao;

	@Autowired
	DataTypeDao dataTypeDao;

	@Autowired
	BanqueDao banqueDao;

	@Test
	public void testToString() throws ParseException {
		final AnnotationDefaut ad1 = annotationDefautDao.findById(1).get();
		assertTrue(ad1.toString().equals("{Defaut: Alphanum1.AlphanumDefaut1}"));
		final AnnotationDefaut ad2 = new AnnotationDefaut();
		assertTrue(ad2.toString().equals("{Empty AnnotationDefaut}"));
		ad2.setChampAnnotation(champAnnotationDao.findById(1).get());
		assertTrue(ad2.toString().equals("{Empty AnnotationDefaut}"));
		ad2.setItem(itemDao.findById(1).get());
		assertTrue(ad2.toString().equals("{Defaut: Alphanum1.item1-1}"));
		ad2.setTexte("You and me always");
		assertTrue(ad2.toString().equals("{Defaut: Alphanum1.You a...}"));
		final Calendar sDate = Calendar.getInstance();
		sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/02/2006 12:02:45"));
		ad2.setDate(sDate);
		assertTrue(ad2.toString().equals("{Defaut: Alphanum1.12/02/2006 12:02:45}"));
		ad2.setBool(true);
		assertTrue(ad2.toString().equals("{Defaut: Alphanum1.true}"));
		ad2.setAlphanum("Dogg");
		assertTrue(ad2.toString().equals("{Defaut: Alphanum1.Dogg}"));

	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllDefauts() {
		final List<AnnotationDefaut> defauts = IterableUtils.toList(annotationDefautDao.findAll());
		assertTrue(defauts.size() == 11);
	}

	@Test
	public void testFindByChamp() {
		List<AnnotationDefaut> defauts = annotationDefautDao.findByChamp(champAnnotationDao.findById(12).get());
		assertTrue(defauts.size() == 2);
		defauts = annotationDefautDao.findByChamp(champAnnotationDao.findById(8).get());
		assertTrue(defauts.size() == 0);
		defauts = annotationDefautDao.findByChamp(null);
		assertTrue(defauts.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		final AnnotationDefaut ad1 = annotationDefautDao.findById(1).get();
		List<AnnotationDefaut> defauts = annotationDefautDao.findByExcludedId(1);
		assertTrue(defauts.size() == 10);
		assertFalse(defauts.contains(ad1));
		defauts = annotationDefautDao.findByExcludedId(12);
		assertTrue(defauts.size() == 11);
		defauts = annotationDefautDao.findByExcludedId(null);
		assertTrue(defauts.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un Item.
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testCrudDefaut() {

		// creation d'un Alphanum avec valeur par defaut
		final ChampAnnotation alpha = new ChampAnnotation();
		alpha.setNom("testAlpha");
		alpha.setDataType(dataTypeDao.findByType("alphanum").get(0));
		alpha.setTableAnnotation(tableAnnotationDao.findById(1).get());
		alpha.setCombine(false);
		alpha.setOrdre(4);
		// defaut
		final AnnotationDefaut ad1 = new AnnotationDefaut();
		ad1.setAlphanum("defautValue1");
		ad1.setChampAnnotation(alpha);
		ad1.setObligatoire(true);
		final Set<AnnotationDefaut> ads = new HashSet<>();
		ads.add(ad1);
		alpha.setAnnotationDefauts(ads);

		// Test de l'insertion a partir du champ
		champAnnotationDao.save(alpha);
		assertNotNull(alpha.getId());

		final Integer maxId = alpha.getId();

		// Test de l'insertion
		annotationDefautDao.save(ad1);
		final ChampAnnotation alpha2 = champAnnotationDao.findById(maxId).get();
		assertNotNull(alpha2);
		final Set<AnnotationDefaut> defauts = alpha2.getAnnotationDefauts();
		assertTrue(defauts.size() == 1);
		assertTrue(defauts.toArray()[0].equals(ad1));

		// Test de la délétion cascade
		champAnnotationDao.deleteById(maxId);
		testReadAllDefauts();
		assertTrue(IterableUtils.toList(champAnnotationDao.findAll()).size() == 48);

		// creation de valeurs par défaut pour un thésaurus
		final AnnotationDefaut ad2 = new AnnotationDefaut();
		ad2.setItem(itemDao.findById(3).get());
		ad2.setChampAnnotation(champAnnotationDao.findById(12).get());
		ad2.setObligatoire(false);

		annotationDefautDao.save(ad2);
		assertTrue(ad2.getAnnotationDefautId() == 13);
		assertTrue(annotationDefautDao.findByChamp(champAnnotationDao.findById(12).get()).size() == 3);
		assertTrue(annotationDefautDao.findById(13).get().getItem().equals(itemDao.findById(3).get()));
		assertFalse(annotationDefautDao.findById(13).get().getObligatoire());

		// update
		ad2.setObligatoire(true);

		annotationDefautDao.save(ad2);
		assertTrue(ad2.getAnnotationDefautId() == 13);
		assertTrue(annotationDefautDao.findById(13).get().getItem().equals(itemDao.findById(3).get()));
		assertTrue(annotationDefautDao.findById(13).get().getObligatoire());

		// Test de la délétion
		annotationDefautDao.deleteById(new Integer(13));
		assertFalse(annotationDefautDao.findById(new Integer(13)).isPresent());
		testReadAllDefauts();
	}

	/**
	 * Test des méthodes surchargées "equals" et hashcode.
	 */
	@Test
	public void testEqualsAndHashCode() {
		final AnnotationDefaut ad1 = new AnnotationDefaut();
		final AnnotationDefaut ad2 = new AnnotationDefaut();
		assertFalse(ad1.equals(null));
		assertNotNull(ad2);
		assertTrue(ad1.equals(ad1));
		assertTrue(ad1.equals(ad2));
		assertTrue(ad1.hashCode() == ad2.hashCode());

		final ChampAnnotation c1 = champAnnotationDao.findById(1).get();
		final ChampAnnotation c2 = champAnnotationDao.findById(2).get();
		final ChampAnnotation c3 = new ChampAnnotation();
		c3.setNom(c2.getNom());
		c3.setTableAnnotation(c2.getTableAnnotation());
		assertFalse(c1.equals(c2));
		assertFalse(c1.hashCode() == c2.hashCode());
		assertTrue(c2.equals(c3));
		ad1.setChampAnnotation(c1);
		assertFalse(ad1.equals(ad2));
		assertFalse(ad2.equals(ad1));
		assertTrue(ad1.hashCode() != ad2.hashCode());
		ad2.setChampAnnotation(c2);
		assertFalse(ad1.equals(ad2));
		assertFalse(ad2.equals(ad1));
		assertTrue(ad1.hashCode() != ad2.hashCode());
		ad1.setChampAnnotation(c3);
		assertTrue(ad1.equals(ad2));
		assertTrue(ad2.equals(ad1));
		assertTrue(ad1.hashCode() == ad2.hashCode());
		ad1.setChampAnnotation(c2);
		assertTrue(ad1.equals(ad2));
		assertTrue(ad2.equals(ad1));
		assertTrue(ad1.hashCode() == ad2.hashCode());

		final Item i1 = itemDao.findById(1).get();
		final Item i2 = itemDao.findById(2).get();
		final Item i3 = new Item();
		i3.setLabel(i2.getLabel());
		i3.setChampAnnotation(i2.getChampAnnotation());
		i3.setPlateforme(i2.getPlateforme());
		assertFalse(i1.equals(i2));
		assertFalse(i1.hashCode() == i2.hashCode());
		assertTrue(i2.equals(i3));
		ad1.setItem(i1);
		assertFalse(ad1.equals(ad2));
		assertFalse(ad2.equals(ad1));
		assertTrue(ad1.hashCode() != ad2.hashCode());
		ad2.setItem(i2);
		assertFalse(ad1.equals(ad2));
		assertFalse(ad2.equals(ad1));
		assertTrue(ad1.hashCode() != ad2.hashCode());
		ad1.setItem(i3);
		assertTrue(ad1.equals(ad2));
		assertTrue(ad2.equals(ad1));
		assertFalse(ad1.hashCode() == ad2.hashCode());
		ad1.setItem(i2);
		assertTrue(ad1.equals(ad2));
		assertTrue(ad2.equals(ad1));
		assertTrue(ad1.hashCode() == ad2.hashCode());

		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final Banque b3 = new Banque();
		b3.setNom(b2.getNom());
		b3.setPlateforme(b2.getPlateforme());
		assertFalse(b1.equals(b2));
		assertFalse(b1.hashCode() == b2.hashCode());
		assertTrue(b2.equals(b3));
		ad1.setBanque(b1);
		assertFalse(ad1.equals(ad2));
		assertFalse(ad2.equals(ad1));
		assertTrue(ad1.hashCode() != ad2.hashCode());
		ad2.setBanque(b2);
		assertFalse(ad1.equals(ad2));
		assertFalse(ad2.equals(ad1));
		assertTrue(ad1.hashCode() != ad2.hashCode());
		ad1.setBanque(b3);
		assertTrue(ad1.equals(ad2));
		assertTrue(ad2.equals(ad1));
		assertTrue(ad1.hashCode() == ad2.hashCode());
		ad1.setBanque(b2);
		assertTrue(ad1.equals(ad2));
		assertTrue(ad2.equals(ad1));
		assertTrue(ad1.hashCode() == ad2.hashCode());

		// dummy
		final Categorie c = new Categorie();
		assertFalse(ad1.equals(c));
	}

	@Test
	public void testClone() throws ParseException {
		final AnnotationDefaut ad1 = annotationDefautDao.findById(1).get();
		// rempli pour contourner les nulls
		ad1.setAlphanum("zert");
		ad1.setBool(true);
		ad1.setTexte("ee");
		ad1.setItem(itemDao.findById(1).get());
		final Calendar sDate = Calendar.getInstance();
		sDate.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/02/2006 12:02:45"));
		ad1.setDate(sDate);
		ad1.setBanque(banqueDao.findById(1).get());
		final AnnotationDefaut clone = ad1.clone();

		assertTrue(ad1.equals(clone));
		assertTrue(ad1.hashCode() == clone.hashCode());
		assertTrue(clone.getAnnotationDefautId().equals(ad1.getAnnotationDefautId()));
		assertTrue(clone.getChampAnnotation().equals(ad1.getChampAnnotation()));
		assertTrue(clone.getAlphanum().equals(ad1.getAlphanum()));
		assertTrue(clone.getBool().equals(ad1.getBool()));
		assertTrue(clone.getDate().equals(ad1.getDate()));
		assertTrue(clone.getTexte().equals(ad1.getTexte()));
		assertTrue(clone.getBanque().equals(ad1.getBanque()));
		assertTrue(clone.getObligatoire().equals(ad1.getObligatoire()));
	}
}
