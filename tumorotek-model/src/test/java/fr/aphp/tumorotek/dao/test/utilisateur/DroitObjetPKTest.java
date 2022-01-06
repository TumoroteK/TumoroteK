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
package fr.aphp.tumorotek.dao.test.utilisateur;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.test.Config;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjetPK;
import fr.aphp.tumorotek.model.utilisateur.Profil;

/**
 *
 * Classe de test pour le bean du domaine DroitObjetPK.
 *
 * @author Pierre Ventadour.
 * @version 18/05/2010
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class DroitObjetPKTest extends AbstractDaoTest {

	@Autowired
	ProfilDao profilDao;

	@Autowired
	EntiteDao entiteDao;

	@Autowired
	OperationTypeDao operationTypeDao;

	/**
	 * Test de la méthode surchargée "equals".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testEquals() throws ParseException {
		final DroitObjetPK pk1 = new DroitObjetPK();
		final DroitObjetPK pk2 = new DroitObjetPK();

		// L'objet 1 n'est pas égal à null
		assertFalse(pk1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(pk1.equals(pk1));

		/* null */
		assertTrue(pk1.equals(pk2));
		assertTrue(pk2.equals(pk1));

		populateClefsToTestEqualsAndHashCode();

		final Categorie c3 = new Categorie();
		assertFalse(pk1.equals(c3));
	}

	/**
	 * Test de la méthode surchargée "hashcode".
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testHashCode() throws ParseException {
		final DroitObjetPK pk1 = new DroitObjetPK();
		final DroitObjetPK pk2 = new DroitObjetPK();

		/* null */
		assertTrue(pk1.hashCode() == pk2.hashCode());

		populateClefsToTestEqualsAndHashCode();

		// un même objet garde le même hashcode dans le temps
		final int hash = pk1.hashCode();
		assertTrue(hash == pk1.hashCode());
		assertTrue(hash == pk1.hashCode());
		assertTrue(hash == pk1.hashCode());
		assertTrue(hash == pk1.hashCode());
	}

	@Autowired
	void populateClefsToTestEqualsAndHashCode() throws ParseException {

		final Profil p1 = profilDao.findById(1).get();
		final Profil p2 = profilDao.findById(2).get();
		final Profil p3 = profilDao.findById(1).get();
		final Profil[] profilsId = new Profil[] { null, p1, p2, p3 };
		final OperationType o1 = operationTypeDao.findById(1).get();
		final OperationType o2 = operationTypeDao.findById(2).get();
		final OperationType o3 = operationTypeDao.findById(1).get();
		final OperationType[] operations = new OperationType[] { null, o1, o2, o3 };
		final Entite e1 = entiteDao.findById(3).get();
		final Entite e2 = entiteDao.findById(8).get();
		final Entite e3 = entiteDao.findById(3).get();
		final Entite[] entites = new Entite[] { null, e1, e2, e3 };

		final DroitObjetPK pk1 = new DroitObjetPK();
		final DroitObjetPK pk2 = new DroitObjetPK();

		for (int i = 0; i < profilsId.length; i++) {
			for (int j = 0; j < operations.length; j++) {
				for (int j2 = 0; j2 < entites.length; j2++) {

					for (int k = 0; k < profilsId.length; k++) {
						for (int l = 0; l < operations.length; l++) {
							for (int l2 = 0; l2 < entites.length; l2++) {

								pk1.setProfil(profilsId[i]);
								pk1.setOperationType(operations[j]);
								pk1.setEntite(entites[j2]);

								pk2.setProfil(profilsId[k]);
								pk2.setOperationType(operations[l]);
								pk2.setEntite(entites[l2]);

								if (((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4))
										&& ((j2 == l2) || (j2 + l2 == 4))) {
									assertTrue(pk1.equals(pk2));
									assertTrue(pk1.hashCode() == pk2.hashCode());
								} else {
									assertFalse(pk1.equals(pk2));
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Test la méthode toString.
	 */
	@Test
	public void testToString() {
		final Profil p1 = profilDao.findById(1).get();
		final OperationType o1 = operationTypeDao.findById(1).get();
		final Entite e1 = entiteDao.findById(3).get();
		final DroitObjetPK pk1 = new DroitObjetPK();
		pk1.setProfil(p1);
		pk1.setEntite(e1);
		pk1.setOperationType(o1);

		assertTrue(pk1.toString().equals("{" + pk1.getProfil().toString() + " (Profil), " + pk1.getEntite().toString()
				+ " (Entite), " + pk1.getOperationType() + " (OperationType)}"));

		pk1.setProfil(null);
		assertTrue(pk1.toString().equals("{Empty DroitObjetPK}"));
		pk1.setProfil(p1);

		pk1.setEntite(null);
		assertTrue(pk1.toString().equals("{Empty DroitObjetPK}"));
		pk1.setEntite(e1);

		pk1.setOperationType(null);
		assertTrue(pk1.toString().equals("{Empty DroitObjetPK}"));
		pk1.setOperationType(o1);

		pk1.setProfil(null);
		pk1.setEntite(null);
		assertTrue(pk1.toString().equals("{Empty DroitObjetPK}"));
		pk1.setEntite(e1);
		pk1.setProfil(p1);

		pk1.setEntite(null);
		pk1.setOperationType(null);
		assertTrue(pk1.toString().equals("{Empty DroitObjetPK}"));
		pk1.setOperationType(o1);
		pk1.setEntite(e1);

		pk1.setProfil(null);
		pk1.setOperationType(null);
		assertTrue(pk1.toString().equals("{Empty DroitObjetPK}"));
		pk1.setOperationType(o1);

		final DroitObjetPK pk2 = new DroitObjetPK();
		assertTrue(pk2.toString().equals("{Empty DroitObjetPK}"));
	}

}
