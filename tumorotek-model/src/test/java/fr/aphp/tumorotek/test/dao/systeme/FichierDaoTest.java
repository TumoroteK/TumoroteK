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
package fr.aphp.tumorotek.test.dao.systeme;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.systeme.FichierDao;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO FichierDao et le bean du domaine Fichier.
 *
 * @author Pierre Ventadour.
 * @version 10/09/2009
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class FichierDaoTest extends AbstractDaoTest {

	@Autowired
	FichierDao fichierDao;

	@Autowired
	EchantillonDao echantillonDao;

	@Autowired
	AnnotationValeurDao annotationValeurDao;

	final String updatedPath = "Path mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllCrAnapaths() {
		final List<Fichier> crAnapaths = IterableUtils.toList(IterableUtils.toList(fichierDao.findAll()));
		assertTrue(crAnapaths.size() == 4);
	}

	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	@Test
	public void testFindByOrder() {
		final List<Fichier> list = fichierDao.findByOrder();
		assertTrue(list.size() == 4);
		assertTrue(list.get(0).getPath().equals("PATH1"));
	}

	/**
	 * Test l'appel de la méthode findByPath().
	 */
	@Test
	public void testFindByPath() {
		List<Fichier> crAnapaths = fichierDao.findByPath("PATH1");
		assertTrue(crAnapaths.size() == 2);
		crAnapaths = fichierDao.findByPath("PATH5");
		assertTrue(crAnapaths.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByEchantillonId().
	 */
	@Test
	public void testFindByEchantillonId() {
		List<Fichier> crAnapaths = fichierDao.findByEchantillonId(1);
		assertTrue(crAnapaths.size() == 1);
		assertTrue(crAnapaths.get(0).getFichierId() == 1);
		crAnapaths = fichierDao.findByEchantillonId(4);
		assertTrue(crAnapaths.size() == 0);
	}

	@Test
	public void testFindByExcludedId() {
		List<Fichier> files = fichierDao.findByExcludedId(1);
		assertTrue(files.size() == 3);
		files = fichierDao.findByExcludedId(8);
		assertTrue(files.size() == 4);
		files = fichierDao.findByExcludedId(null);
		assertTrue(files.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un CrAnapath.
	 * 
	 * @throws Exception lance une exception en cas d'erreur sur les données.
	 */

	@Test
	public void testCrudCrAnapath() throws Exception {

		final Fichier c = new Fichier();

		c.setPath("PATH5");
		c.setNom("file5");
		c.setMimeType("application/octet-stream");
		// Test de l'insertion
		fichierDao.save(c);
		final Integer fId = c.getFichierId();
		assertEquals(fId, c.getFichierId());

		// Test de la mise à jour
		final Fichier c2 = fichierDao.findById(fId).get();
		assertNotNull(c2);
		assertTrue(c2.getPath().equals("PATH5"));
		assertTrue(c2.getMimeType().equals("application/octet-stream"));
		c2.setPath(updatedPath);
		fichierDao.save(c2);
		assertTrue(fichierDao.findById(fId).get().getPath().equals(updatedPath));

		// Test de la délétion
		fichierDao.deleteById(fId);
		assertFalse(fichierDao.findById(fId).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	// @Test
	// @Test
	// public void testEquals() {
	// String path1 = "/path1/coll_1/FILE_1";
	// String path2 = "/path2/coll_2/FILE_2";
	// Fichier c1 = new Fichier();
	// Fichier c2 = new Fichier();
	//
	// // L'objet 1 n'est pas égal à null
	// assertFalse(c1.equals(null));
	// // L'objet 1 est égale à lui même
	// assertTrue(c1.equals(c1));
	// // 2 objets sont égaux entre eux
	// assertFalse(c1.equals(c2));
	// assertFalse(c2.equals(c1));
	//
	// // Vérification de la différenciation de 2 objets
	// c1.setPath(path1);
	// assertFalse(c1.equals(c2));
	// assertFalse(c2.equals(c1));
	//
	// c2.setPath(path2);
	// assertFalse(c1.equals(c2));
	// assertFalse(c2.equals(c1));
	// c1.setPath(path2);
	// assertTrue(c1.equals(c2));
	// c1.setPath("/path2/coll_2/FILE_2");
	// assertTrue(c1.equals(c2));
	// c1.setPath(null);
	// assertFalse(c1.equals(c2));
	//
	// Categorie c = new Categorie();
	// assertFalse(c1.equals(c));
	// }
	//
	// /**
	// * Test de la méthode surchargée "hashcode".
	// */
	// @Test
	// @Test
	// public void testHashCode() {
	// String path = "Path1";
	// Fichier c1 = new Fichier(1, path);
	// c1.setPath(path);
	// Fichier c2 = new Fichier(2, path);
	// c2.setPath(path);
	// Fichier c3 = new Fichier(3, null);
	// assertTrue(c3.hashCode() > 0);
	//
	// int hash = c1.hashCode();
	// // 2 objets égaux ont le même hashcode
	// assertTrue(c1.hashCode() == c2.hashCode());
	// // un même objet garde le même hashcode dans le temps
	// assertTrue(hash == c1.hashCode());
	// assertTrue(hash == c1.hashCode());
	// assertTrue(hash == c1.hashCode());
	// assertTrue(hash == c1.hashCode());
	//
	// }

	@Test
	public void testEqualsAndHashCode() {
		final Fichier f1 = new Fichier();
		final Fichier f2 = new Fichier();
		assertFalse(f1.equals(null));
		assertNotNull(f2);
		assertTrue(f1.equals(f1));
		assertTrue(f1.equals(f2));
		assertTrue(f1.hashCode() == f2.hashCode());

		final String s1 = new String("/pt_1/path1");
		final String s2 = new String("/pt_1/path2");
		final String s3 = new String("/pt_1/path2");

		f1.setPath(s1);
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		assertTrue(f1.hashCode() != f2.hashCode());
		f2.setPath(s2);
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		assertTrue(f1.hashCode() != f2.hashCode());
		f1.setPath(s2);
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(f1.hashCode() == f2.hashCode());
		f1.setPath(s3);
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(f1.hashCode() == f2.hashCode());

		final Echantillon e1 = echantillonDao.findById(1).get();
		final Echantillon e2 = echantillonDao.findById(2).get();
		final Echantillon e3 = new Echantillon();
		e3.setCode(e2.getCode());
		e3.setBanque(e2.getBanque());
		assertFalse(e1.equals(e2));
		assertFalse(e1.hashCode() == e2.hashCode());
		assertTrue(e2.equals(e3));
		f1.setEchantillon(e1);
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		assertTrue(f1.hashCode() != f2.hashCode());
		f2.setEchantillon(e2);
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		assertTrue(f1.hashCode() != f2.hashCode());
		f1.setEchantillon(e3);
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(f1.hashCode() == f2.hashCode());
		f1.setEchantillon(e2);
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(f1.hashCode() == f2.hashCode());

		final AnnotationValeur av1 = annotationValeurDao.findById(1).get();
		final AnnotationValeur av2 = annotationValeurDao.findById(2).get();
		final AnnotationValeur av3 = new AnnotationValeur();
		av3.setObjetId(av2.getObjetId());
		av3.setChampAnnotation(av2.getChampAnnotation());
		av3.setBanque(av2.getBanque());
		av3.setItem(av2.getItem());
		assertFalse(av1.equals(av2));
		assertFalse(av1.hashCode() == av2.hashCode());
		assertTrue(av2.equals(av3));
		f1.setAnnotationValeur(av1);
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		assertTrue(f1.hashCode() != f2.hashCode());
		f2.setAnnotationValeur(av2);
		assertFalse(f1.equals(f2));
		assertFalse(f2.equals(f1));
		assertTrue(f1.hashCode() != f2.hashCode());
		f1.setAnnotationValeur(av3);
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(f1.hashCode() == f2.hashCode());
		f1.setAnnotationValeur(av2);
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(f1.hashCode() == f2.hashCode());

		// dummy
		final Categorie c = new Categorie();
		assertFalse(f1.equals(c));
	}

	@Test
	public void testFindFilesSharingPathForEchans() {
		final Fichier anapath = fichierDao.findById(1).get();
		List<Echantillon> echans = fichierDao.findFilesSharingPathForEchans(anapath.getPath());
		assertTrue(echans.size() == 2);
		final Fichier anapath2 = fichierDao.findById(3).get();
		echans = fichierDao.findFilesSharingPathForEchans(anapath2.getPath());
		assertTrue(echans.isEmpty());
		echans = fichierDao.findFilesSharingPathForEchans(null);
		assertTrue(echans.isEmpty());
	}

	@Test
	public void testFindFilesSharingPathForAnnos() {
		final Fichier f1 = fichierDao.findById(1).get();
		List<AnnotationValeur> annos = fichierDao.findFilesSharingPathForAnnos(f1.getPath());
		assertTrue(annos.size() == 1);
		final Fichier anapath2 = fichierDao.findById(3).get();
		annos = fichierDao.findFilesSharingPathForAnnos(anapath2.getPath());
		assertTrue(annos.isEmpty());
		annos = fichierDao.findFilesSharingPathForAnnos(null);
		assertTrue(annos.isEmpty());
	}

	@Test
	public void testToString() {
		final Fichier path1 = fichierDao.findById(1).get();
		assertTrue(path1.toString().equals("{" + path1.getPath() + "}"));

		final Fichier path2 = new Fichier();
		assertTrue(path2.toString().equals("{Empty Fichier}"));
	}

	@Test
	public void testClone() {
		final Fichier f1 = fichierDao.findById(1).get();
		final Fichier clone = f1.clone();
		assertTrue(clone.getFichierId().equals(f1.getFichierId()));
		assertTrue(clone.getNom().equals(f1.getNom()));
		assertTrue(clone.getPath().equals(f1.getPath()));
		assertTrue(clone.getMimeType().equals(f1.getMimeType()));
		assertTrue(clone.getAnnotationValeur().equals(f1.getAnnotationValeur()));
		assertTrue(clone.getEchantillon().equals(f1.getEchantillon()));
	}
}