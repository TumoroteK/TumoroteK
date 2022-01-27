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
package fr.aphp.tumorotek.test.dao.utilisateur;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateurPK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.AbstractDaoTest;
import fr.aphp.tumorotek.test.dao.Config;

/**
 *
 * Classe de test pour le bean du domaine ProfilUtilisateurPK.
 *
 * @author Pierre Ventadour.
 * @version 18/05/2010.
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Config.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ProfilUtilisateurPKTest extends AbstractDaoTest {

	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	BanqueDao banqueDao;

	@Autowired
	ProfilDao profilDao;

	@Test
	public void testEquals() throws ParseException {
		final ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK();
		final ProfilUtilisateurPK pk2 = new ProfilUtilisateurPK();

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

	@Test
	public void testHashCode() throws ParseException {
		final ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK();
		final ProfilUtilisateurPK pk2 = new ProfilUtilisateurPK();

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
		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Utilisateur u2 = utilisateurDao.findById(2).get();
		final Utilisateur u3 = utilisateurDao.findById(1).get();
		final Utilisateur[] utilisateurs = new Utilisateur[] { null, u1, u2, u3 };
		final Banque b1 = banqueDao.findById(1).get();
		final Banque b2 = banqueDao.findById(2).get();
		final Banque b3 = banqueDao.findById(1).get();
		final Banque[] banques = new Banque[] { null, b1, b2, b3 };

		final ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK();
		final ProfilUtilisateurPK pk2 = new ProfilUtilisateurPK();

		for (int i = 0; i < profilsId.length; i++) {
			for (int j = 0; j < utilisateurs.length; j++) {
				for (int j2 = 0; j2 < banques.length; j2++) {

					for (int k = 0; k < profilsId.length; k++) {
						for (int l = 0; l < utilisateurs.length; l++) {
							for (int l2 = 0; l2 < banques.length; l2++) {

								pk1.setProfil(profilsId[i]);
								pk1.setUtilisateur(utilisateurs[j]);
								pk1.setBanque(banques[j2]);

								pk2.setProfil(profilsId[k]);
								pk2.setUtilisateur(utilisateurs[l]);
								pk2.setBanque(banques[l2]);

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

	@Test
	public void testToString() {
		final Profil p1 = profilDao.findById(1).get();
		final Utilisateur u1 = utilisateurDao.findById(1).get();
		final Banque b1 = banqueDao.findById(1).get();
		final ProfilUtilisateurPK pk1 = new ProfilUtilisateurPK();
		pk1.setProfil(p1);
		pk1.setUtilisateur(u1);
		pk1.setBanque(b1);

		assertTrue(pk1.toString().equals("{" + pk1.getProfil().toString() + " (Profil), "
				+ pk1.getUtilisateur().toString() + " (Utilisateur), " + pk1.getBanque() + " (Banque)}"));

		pk1.setProfil(null);
		assertTrue(pk1.toString().equals("{Empty ProfilUtilisateurPK}"));
		pk1.setProfil(p1);

		pk1.setUtilisateur(null);
		assertTrue(pk1.toString().equals("{Empty ProfilUtilisateurPK}"));
		pk1.setUtilisateur(u1);

		pk1.setBanque(null);
		assertTrue(pk1.toString().equals("{Empty ProfilUtilisateurPK}"));
		pk1.setBanque(b1);

		pk1.setProfil(null);
		pk1.setUtilisateur(null);
		assertTrue(pk1.toString().equals("{Empty ProfilUtilisateurPK}"));
		pk1.setUtilisateur(u1);
		pk1.setProfil(p1);

		pk1.setUtilisateur(null);
		pk1.setBanque(null);
		assertTrue(pk1.toString().equals("{Empty ProfilUtilisateurPK}"));
		pk1.setBanque(b1);
		pk1.setUtilisateur(u1);

		pk1.setProfil(null);
		pk1.setBanque(null);
		assertTrue(pk1.toString().equals("{Empty ProfilUtilisateurPK}"));
		pk1.setBanque(b1);

		final ProfilUtilisateurPK pk2 = new ProfilUtilisateurPK();
		assertTrue(pk2.toString().equals("{Empty ProfilUtilisateurPK}"));
	}

}
