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
package fr.aphp.tumorotek.test.dao.qualite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import fr.aphp.tumorotek.dao.qualite.OperationDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le DAO OperationDao et le bean du domaine Operation.
 * Classe de test créée le 13/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class OperationDaoTest extends AbstractDaoTest {

	@Autowired
	OperationDao operationDao;
	@Autowired
	EntiteDao entiteDao;
	@Autowired
	UtilisateurDao utilisateurDao;
	@Autowired
	OperationTypeDao operationTypeDao;

	/**
	 * Test l'appel de la méthode findAll().
	 */
	@Test
	public void testReadAllOperations() {
		final List<Operation> ops = IterableUtils.toList(IterableUtils.toList(operationDao.findAll()));
		assertTrue(ops.size() == 19);
	}

	/**
	 * Test l'appel de la méthode toString().
	 */
	@Test
	public void testToString() {
		Operation o1 = operationDao.findById(1).get();
		assertTrue(o1.toString().equals(
				"{" + o1.getOperationType().getNom() + ", " + o1.getEntite().getNom() + ", " + o1.getObjetId() + "}"));
		o1 = new Operation();
		assertTrue(o1.toString().equals("{Empty Operation}"));
	}

	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	@Test
	public void testFindByExcludedId() {
		List<Operation> ops = operationDao.findByExcludedId(1);
		assertEquals(18, ops.size());

		ops = operationDao.findByExcludedId(30);
		assertEquals(19, ops.size());
	}

	/**
	 * Test l'appel de la méthode findByUtilisateur().
	 */
	@Test
	public void testFindByUtilisateur() {
		final Utilisateur u = utilisateurDao.findById(1).get();
		List<Operation> ops = operationDao.findByUtilisateur(u);
		assertTrue(ops.size() == 17);
		final Utilisateur u2 = utilisateurDao.findById(3).get();
		ops = operationDao.findByUtilisateur(u2);
		assertTrue(ops.size() == 0);
		ops = operationDao.findByUtilisateur(null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetIdAndEntite().
	 */
	@Test
	public void testFindByObjetIdAndEntite() {
		final Entite e = entiteDao.findByNom("Prelevement").get(0);
		List<Operation> ops = operationDao.findByObjetIdAndEntite(2, e);
		assertTrue(ops.size() == 2);
		final Entite e2 = entiteDao.findByNom("Patient").get(0);
		ops = operationDao.findByObjetIdAndEntite(5, e);
		assertTrue(ops.size() == 0);
		ops = operationDao.findByObjetIdAndEntite(1, e2);
		assertTrue(ops.size() == 2);
		ops = operationDao.findByObjetIdAndEntite(null, null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetIdAndEntiteForHistorique().
	 */
	@Test
	public void testFindByObjetIdAndEntiteForHistorique() {
		final Entite e = entiteDao.findByNom("Prelevement").get(0);
		List<Operation> ops = operationDao.findByObjetIdAndEntiteForHistorique(2, e);
		assertTrue(ops.size() == 2);
		final Entite e2 = entiteDao.findByNom("Patient").get(0);
		ops = operationDao.findByObjetIdAndEntiteForHistorique(5, e);
		assertTrue(ops.size() == 0);
		ops = operationDao.findByObjetIdAndEntiteForHistorique(1, e2);
		assertTrue(ops.size() == 2);
		ops = operationDao.findByObjetIdAndEntiteForHistorique(null, null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test l'appel de la méthode findByObjetIdEntiteAndOperationType().
	 */
	@Test
	public void testFindByObjetIdEntiteAndOperationType() {
		final OperationType type = operationTypeDao.findByNom("Creation").get(0);
		final Entite e = entiteDao.findByNom("Prelevement").get(0);

		List<Operation> ops = operationDao.findByObjetIdEntiteAndOperationType(2, e, type);
		assertTrue(ops.size() == 1);

		ops = operationDao.findByObjetIdEntiteAndOperationType(5, e, type);
		assertTrue(ops.size() == 0);

		final Entite e2 = entiteDao.findByNom("Patient").get(0);
		ops = operationDao.findByObjetIdEntiteAndOperationType(1, e2, type);
		assertTrue(ops.size() == 1);

		ops = operationDao.findByObjetIdEntiteAndOperationType(null, null, null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test de la méthode findByDate().
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testFindByDate() throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<Operation> ops = operationDao.findByDate(cal);
		assertTrue(ops.size() == 1);

		date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2010");
		cal = Calendar.getInstance();
		cal.setTime(date);
		ops = operationDao.findByDate(cal);
		assertTrue(ops.size() == 0);

		ops = operationDao.findByDate(null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test de la méthode findByAfterDate().
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testFindByAfterDate() throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<Operation> ops = operationDao.findByAfterDate(cal);
		assertTrue(ops.size() == 16);

		date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2010");
		cal = Calendar.getInstance();
		cal.setTime(date);
		ops = operationDao.findByAfterDate(cal);
		assertTrue(ops.size() == 0);

		ops = operationDao.findByAfterDate(null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test de la méthode findByBeforeDate().
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testFindByBeforeDate() throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2008");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		List<Operation> ops = operationDao.findByBeforeDate(cal);
		assertTrue(ops.size() == 4);

		date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980");
		cal = Calendar.getInstance();
		cal.setTime(date);
		ops = operationDao.findByBeforeDate(cal);
		assertTrue(ops.size() == 0);

		ops = operationDao.findByBeforeDate(null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test de la méthode findByBetweenDates().
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testFindByBetweenDates() throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("31/10/2009");
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		date = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2009");
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		List<Operation> ops = operationDao.findByBetweenDates(cal1, cal2);
		assertTrue(ops.size() == 5);

		date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980");
		cal1.setTime(date);
		date = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/1980");
		cal2.setTime(date);
		ops = operationDao.findByBetweenDates(cal1, cal2);
		assertTrue(ops.size() == 0);

		ops = operationDao.findByBetweenDates(null, null);
		assertTrue(ops.size() == 0);
	}

	/**
	 * Test l'insertion, la mise à jour et la suppression d'une Operation.
	 * 
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	@Test
	public void testCrudOperation() throws Exception {
		final Operation o = new Operation();
		final Utilisateur u = utilisateurDao.findById(1).get();
		o.setUtilisateur(u);
		final OperationType oType = operationTypeDao.findByNom("Creation").get(0);
		o.setOperationType(oType);
		final Entite e = entiteDao.findByNom("Patient").get(0);
		o.setEntite(e);
		o.setObjetId(1);
		final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("07/09/2009");
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		o.setDate(cal);
		// Test de l'insertion
		operationDao.save(o);
		assertNotNull(o.getOperationId());

		final int oId = o.getOperationId();

		// Test de la mise à jour
		final Operation o2 = operationDao.findById(oId).get();
		assertNotNull(o2);
		assertTrue(o2.getUtilisateur().equals(u));
		assertTrue(o2.getEntite().equals(e));
		assertTrue(o2.getOperationType().equals(oType));
		assertTrue(o2.getObjetId().equals(1));
		assertTrue(o2.getDate().equals(cal));

		final Utilisateur u2 = utilisateurDao.findById(2).get();
		o2.setUtilisateur(u2);
		final OperationType oType2 = operationTypeDao.findByNom("Modification").get(0);
		o2.setOperationType(oType2);
		final Entite e2 = entiteDao.findByNom("Echantillon").get(0);
		o2.setEntite(e2);
		o2.setObjetId(2);
		final Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("09/10/1999");
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		o2.setDate(cal2);
		operationDao.save(o2);
		assertTrue(operationDao.findById(oId).get().getUtilisateur().equals(u2));
		assertTrue(operationDao.findById(oId).get().getEntite().equals(e2));
		assertTrue(operationDao.findById(oId).get().getOperationType().equals(oType2));
		assertTrue(operationDao.findById(oId).get().getObjetId().equals(2));
		assertTrue(operationDao.findById(oId).get().getDate().equals(cal2));

		// Test de la délétion
		operationDao.deleteById(oId);
		assertFalse(operationDao.findById(oId).isPresent());

	}

	/**
	 * Test de la méthode surchargée "equals".
	 */
	@Test
	public void testEquals() {
		final Operation o1 = new Operation();
		final Operation o2 = new Operation();

		// L'objet 1 n'est pas égal à null
		assertFalse(o1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(o1.equals(o1));
		// 2 objets sont égaux entre eux
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));

		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Utilisateur u2 = utilisateurDao.findById(2).get();
		final Utilisateur u3 = utilisateurDao.findById(1).get();
		assertTrue(u1.equals(u3));
		final Utilisateur[] utilisateurs = new Utilisateur[] { null, u1, u2, u3 };
		final OperationType ot1 = operationTypeDao.findById(1).get();
		final OperationType ot2 = operationTypeDao.findById(2).get();
		final OperationType ot3 = operationTypeDao.findById(1).get();
		assertTrue(ot1.equals(ot3));
		final OperationType[] oTypes = new OperationType[] { null, ot1, ot2, ot3 };
		final Entite e1 = entiteDao.findById(1).get();
		final Entite e2 = entiteDao.findById(2).get();
		final Entite e3 = entiteDao.findById(1).get();
		assertTrue(e1.equals(e3));
		final Entite[] entites = new Entite[] { null, e1, e2, e3 };
		final Integer[] objetIds = new Integer[] { null, 1, 2 };

		for (int i = 0; i < utilisateurs.length; i++) {
			for (int j = 0; j < oTypes.length; j++) {
				for (int k = 0; k < entites.length; k++) {
					for (int l = 0; l < objetIds.length; l++) {
						for (int m = 0; m < utilisateurs.length; m++) {
							for (int n = 0; n < oTypes.length; n++) {
								for (int o = 0; o < entites.length; o++) {
									for (int p = 0; p < objetIds.length; p++) {
										o1.setUtilisateur(utilisateurs[i]);
										o2.setUtilisateur(utilisateurs[m]);
										o1.setOperationType(oTypes[j]);
										o2.setOperationType(oTypes[n]);
										o1.setEntite(entites[k]);
										o2.setEntite(entites[o]);
										o1.setObjetId(objetIds[l]);
										o2.setObjetId(objetIds[p]);
										if (((i == m) || (i + m == 4)) && ((j == n) || (j + n == 4))
												&& ((k == o) || (k + o == 4)) && (l == p)) {
											assertTrue(o1.equals(o2));
										} else {
											assertFalse(o1.equals(o2));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// dummy test
		final Banque b = new Banque();
		assertFalse(o1.equals(b));

	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	@Test
	public void testHashCode() {
		final Operation o1 = new Operation();
		o1.setOperationId(1);
		final Operation o2 = new Operation();
		o2.setOperationId(2);
		final Operation t3 = new Operation();
		o1.setOperationId(3);
		assertTrue(t3.hashCode() > 0);

		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Utilisateur u2 = utilisateurDao.findById(2).get();
		final Utilisateur u3 = utilisateurDao.findById(1).get();
		assertTrue(u1.equals(u3));
		final Utilisateur[] utilisateurs = new Utilisateur[] { null, u1, u2, u3 };
		final OperationType ot1 = operationTypeDao.findById(1).get();
		final OperationType ot2 = operationTypeDao.findById(2).get();
		final OperationType ot3 = operationTypeDao.findById(1).get();
		assertTrue(ot1.equals(ot3));
		final OperationType[] oTypes = new OperationType[] { null, ot1, ot2, ot3 };
		final Entite e1 = entiteDao.findById(1).get();
		final Entite e2 = entiteDao.findById(2).get();
		final Entite e3 = entiteDao.findById(1).get();
		assertTrue(e1.equals(e3));
		final Entite[] entites = new Entite[] { null, e1, e2, e3 };
		final Integer[] objetIds = new Integer[] { null, 1, 2, 0 };

		for (int i = 0; i < utilisateurs.length; i++) {
			for (int j = 0; j < oTypes.length; j++) {
				for (int k = 0; k < entites.length; k++) {
					for (int l = 0; l < objetIds.length; l++) {
						for (int m = 0; m < utilisateurs.length; m++) {
							for (int n = 0; n < oTypes.length; n++) {
								for (int o = 0; o < entites.length; o++) {
									for (int p = 0; p < objetIds.length; p++) {
										o1.setUtilisateur(utilisateurs[i]);
										o2.setUtilisateur(utilisateurs[m]);
										o1.setOperationType(oTypes[j]);
										o2.setOperationType(oTypes[n]);
										o1.setEntite(entites[k]);
										o2.setEntite(entites[o]);
										o1.setObjetId(objetIds[l]);
										o2.setObjetId(objetIds[p]);
										if (((i == m) || (i + m == 4)) && ((j == n) || (j + n == 4))
												&& ((k == o) || (k + o == 4)) && (l == p)) {
											assertTrue(o1.hashCode() == o2.hashCode());
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final int hash = o1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(o1.hashCode() == o2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
		assertTrue(hash == o1.hashCode());
	}

}
