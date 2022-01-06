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
package fr.aphp.tumorotek.dao.test.stockage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import fr.aphp.tumorotek.dao.stockage.ConteneurDao;
import fr.aphp.tumorotek.dao.stockage.EnceinteDao;
import fr.aphp.tumorotek.dao.stockage.IncidentDao;
import fr.aphp.tumorotek.dao.stockage.TerminaleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Incident;
import fr.aphp.tumorotek.model.stockage.Terminale;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class IncidentDaoTest extends AbstractDaoTest {

	@Autowired
	IncidentDao incidentDao;

	@Autowired
	ConteneurDao conteneurDao;

	@Autowired
	EnceinteDao enceinteDao;

	@Autowired
	TerminaleDao terminaleDao;

	private String updatedNom = "Mis a jour";

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAlls() {
		final List<Incident> liste = IterableUtils.toList(IterableUtils.toList(incidentDao.findAll()));
		assertTrue(liste.size() == 5);
	}

	/**
	 * Test l'appel de la méthode findByConteneurOrderByDate().
	 */
	@Test
	public void testFindByConteneurOrderByDate() {
		final Conteneur c1 = conteneurDao.findById(1).get();
		assertNotNull(c1);
		List<Incident> list = incidentDao.findByConteneurOrderByDate(c1);
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getNom().equals("CHUTE MATERIEL"));

		final Conteneur c2 = conteneurDao.findById(2).get();
		list = incidentDao.findByConteneurOrderByDate(c2);
		assertTrue(list.size() == 1);

		list = incidentDao.findByConteneurOrderByDate(null);
		assertTrue(list.size() == 0);
	}

	@Test
	public void testFindByEnceinte() {
		final Enceinte e1 = enceinteDao.findById(1).get();
		List<Incident> list = incidentDao.findByEnceinte(e1);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getNom().equals("TIROIR EN VRAC"));

		final Enceinte e2 = enceinteDao.findById(2).get();
		list = incidentDao.findByEnceinte(e2);
		assertTrue(list.isEmpty());

		list = incidentDao.findByEnceinte(null);
		assertTrue(list.isEmpty());
	}

	@Test
	public void testFindByTerminale() {
		final Terminale e3 = terminaleDao.findById(3).get();
		List<Incident> list = incidentDao.findByTerminale(e3);
		assertTrue(list.size() == 1);
		assertTrue(list.get(0).getNom().equals("BOITE PAR TERRE"));

		final Terminale e2 = terminaleDao.findById(2).get();
		list = incidentDao.findByTerminale(e2);
		assertTrue(list.isEmpty());

		list = incidentDao.findByTerminale(null);
		assertTrue(list.isEmpty());
	}

	/**
	 * Test l'appel de la méthode findByExcludedIdAndConteneur().
	 */
	@Test
	public void testFindByExcludedIdAndConteneur() {
		final Conteneur c1 = conteneurDao.findById(1).get();
		List<Incident> list = incidentDao.findByExcludedIdAndConteneur(1, c1);
		assertTrue(list.size() == 1);

		list = incidentDao.findByExcludedIdAndConteneur(10, c1);
		assertTrue(list.size() == 2);

		list = incidentDao.findByExcludedIdAndConteneur(null, c1);
		assertTrue(list.size() == 0);

		final Conteneur c2 = conteneurDao.findById(2).get();
		list = incidentDao.findByExcludedIdAndConteneur(3, c2);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndConteneur(1, null);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndConteneur(null, null);
		assertTrue(list.size() == 0);
	}

	@Test
	public void testFindByExcludedIdAndEnceinte() {
		final Enceinte e1 = enceinteDao.findById(1).get();
		List<Incident> list = incidentDao.findByExcludedIdAndEnceinte(4, e1);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndEnceinte(10, e1);
		assertTrue(list.size() == 1);

		list = incidentDao.findByExcludedIdAndEnceinte(null, e1);
		assertTrue(list.size() == 0);

		final Enceinte e2 = enceinteDao.findById(2).get();
		list = incidentDao.findByExcludedIdAndEnceinte(4, e2);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndEnceinte(4, null);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndEnceinte(null, null);
		assertTrue(list.size() == 0);
	}

	@Test
	public void testFindByExcludedIdAndTerminale() {
		final Terminale t3 = terminaleDao.findById(3).get();
		List<Incident> list = incidentDao.findByExcludedIdAndTerminale(1, t3);
		assertTrue(list.size() == 1);

		list = incidentDao.findByExcludedIdAndTerminale(5, t3);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndTerminale(null, t3);
		assertTrue(list.size() == 0);

		final Terminale t1 = terminaleDao.findById(1).get();
		list = incidentDao.findByExcludedIdAndTerminale(1, t1);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndTerminale(1, null);
		assertTrue(list.size() == 0);

		list = incidentDao.findByExcludedIdAndEnceinte(null, null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'un CessionExamen.
	 * 
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	@Test
	public void testCrud() throws Exception {

		final Incident i = new Incident();
		final Conteneur c = conteneurDao.findById(1).get();

		i.setNom("TEST");
		i.setConteneur(c);
		final Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
		i.setDate(d1);
		// Test de l'insertion
		incidentDao.save(i);

		final Integer iId = i.getIncidentId();

		// Test de la mise à jour
		final Incident ct2 = incidentDao.findById(iId).get();
		assertNotNull(ct2);
		assertTrue(ct2.getDate().equals(d1));
		ct2.setNom(updatedNom);
		incidentDao.save(ct2);
		assertTrue(incidentDao.findById(iId).get().getNom().equals(updatedNom));

		// Test de la délétion
		incidentDao.deleteById(iId);
		assertFalse(incidentDao.findById(iId).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {
		final String nom = "Inc1";
		final String nom2 = "Inc2";
		final Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
		final Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("10/09/2009");
		final Conteneur c1 = conteneurDao.findById(1).get();
		final Conteneur c2 = conteneurDao.findById(2).get();
		final Enceinte e1 = enceinteDao.findById(1).get();
		final Enceinte e2 = enceinteDao.findById(2).get();
		final Terminale t1 = terminaleDao.findById(1).get();
		final Terminale t2 = terminaleDao.findById(2).get();
		final Incident i1 = new Incident();
		final Incident i2 = new Incident();

		// L'objet 1 n'est pas égal à null
		assertFalse(i1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(i1.equals(i1));

		/* null */
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));

		/* Nom */
		i2.setNom(nom);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setNom(nom2);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setNom(nom);
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));

		/* Prenom */
		i2.setDate(d1);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setDate(d2);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setDate(d1);
		assertTrue(i1.equals(i2));

		/* Conteneur */
		i2.setConteneur(c1);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setConteneur(c2);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setConteneur(c1);
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));

		/* Enceinte */
		i2.setEnceinte(e1);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setEnceinte(e2);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setEnceinte(e1);
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));

		/* Terminale */
		i2.setTerminale(t1);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setTerminale(t2);
		assertFalse(i1.equals(i2));
		assertFalse(i2.equals(i1));
		i1.setTerminale(t1);
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));

		final Categorie c3 = new Categorie();
		assertFalse(i1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final String nom = "Ini1";
		final String nom2 = "Ini2";
		final Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
		final Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("10/09/2009");
		final Conteneur c1 = conteneurDao.findById(1).get();
		final Conteneur c2 = conteneurDao.findById(2).get();
		final Enceinte e1 = enceinteDao.findById(1).get();
		final Enceinte e2 = enceinteDao.findById(2).get();
		final Terminale t1 = terminaleDao.findById(1).get();
		final Terminale t2 = terminaleDao.findById(2).get();
		final Incident i1 = new Incident();
		final Incident i2 = new Incident();

		/* null */
		assertTrue(i1.hashCode() == i2.hashCode());

		/* Nom */
		i2.setNom(nom);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setNom(nom2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setNom(nom);
		assertTrue(i1.hashCode() == i2.hashCode());

		/* Prenom */
		i2.setDate(d1);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setDate(d2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setDate(d1);
		assertTrue(i1.hashCode() == i2.hashCode());

		/* Conteneur */
		i2.setConteneur(c1);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setConteneur(c2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setConteneur(c1);
		assertTrue(i1.hashCode() == i2.hashCode());

		/* Enceinte */
		i2.setEnceinte(e1);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setEnceinte(e2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setEnceinte(e1);
		assertTrue(i1.hashCode() == i2.hashCode());

		/* Terminale */
		i2.setTerminale(t1);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setTerminale(t2);
		assertFalse(i1.hashCode() == i2.hashCode());
		i1.setTerminale(t1);
		assertTrue(i1.hashCode() == i2.hashCode());

		// un même objet garde le même hashcode dans le temps
		final int hash = i1.hashCode();
		assertTrue(hash == i1.hashCode());
		assertTrue(hash == i1.hashCode());
		assertTrue(hash == i1.hashCode());
		assertTrue(hash == i1.hashCode());

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Incident i1 = incidentDao.findById(1).get();
		assertTrue(i1.toString().equals("{" + i1.getNom() + " " + i1.getDate() + "}"));

		final Incident i2 = new Incident();
		assertTrue(i2.toString().equals("{Empty Incident}"));
	}

	/**
	 * Test la méthode clone.
	 */
	@Test
	public void testClone() {
		final Incident incident1 = incidentDao.findById(1).get();
		Incident incident2 = new Incident();
		incident2 = incident1.clone();

		assertTrue(incident1.equals(incident2));

		if (incident1.getIncidentId() != null) {
			assertTrue(incident1.getIncidentId() == incident2.getIncidentId());
		} else {
			assertNull(incident2.getIncidentId());
		}

		if (incident1.getNom() != null) {
			assertTrue(incident1.getNom().equals(incident2.getNom()));
		} else {
			assertNull(incident2.getNom());
		}

		if (incident1.getDate() != null) {
			assertTrue(incident1.getDate().equals(incident2.getDate()));
		} else {
			assertNull(incident2.getDate());
		}

		if (incident1.getDescription() != null) {
			assertTrue(incident1.getDescription().equals(incident2.getDescription()));
		} else {
			assertNull(incident2.getDescription());
		}

		if (incident1.getConteneur() != null) {
			assertTrue(incident1.getConteneur().equals(incident2.getConteneur()));
		} else {
			assertNull(incident2.getConteneur());
		}

	}

}
